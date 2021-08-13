package com.st1.itx.util.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.AchAuthLogHistory;
import com.st1.itx.db.domain.AchAuthLogHistoryId;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogHistory;
import com.st1.itx.db.domain.PostAuthLogHistoryId;
import com.st1.itx.db.domain.AchAuthLogId;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.BankAuthActId;
import com.st1.itx.db.domain.PostAuthLogId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AchAuthLogHistoryService;
//import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.PostAuthLogHistoryService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.db.service.springjpa.cm.BankAuthActComServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * LOG、帳號檔維護 <BR>
 * 1.1 L2153 僅新增：放行新增；訂正刪除 <BR>
 * 1.2 L2154 維護：前端控制為原帳號或已授權之帳號，僅寫帳號檔 <BR>
 * 1.3 L2154 刪除：刪除Log檔、帳號檔 <BR>
 * 1.4 L4410、L4412 新增：若該額度已有授權成功帳號，僅寫入Log檔，待回傳檔L4414上傳更新帳號檔 <BR>
 * 1.5 L4410、L4412 維護：若為未授權可維護Key值以外欄位，否則僅能變更暫停或暫停回復記號 <BR>
 * 1.6 L4410、L4412 刪除：僅刪除Log檔 <BR>
 * 2. call by L4410、L4412、L2153、L2154<BR>
 * 
 * @author St1
 * @version 1.0.0
 */

//   功能:
//   一. 新增銀扣授權帳號檔，及授權記錄檔 (根據tita之銀行代碼)
//       1.扣款帳號未存在於該戶號下授權檔者，才建立。
//       2.授權檔記錄首次建立此扣款帳號之額度。
//   二. 放行時執行 
//       if (titaVo.isActfgRelease()) {
//          int returnCode = bankAuthActCom.acctCheck(titaVo);
//          if (returnCode == 3 ) {
//	           throw new LogicException(titaVo, "E0005", "該戶號額度下已有相同帳號");
//          }
//       }
//   三. 訂正時，判斷戶號、額度、帳號相同者刪除後，新增訂正之資料
//   四. FuncCode : 1新增、2修改、4刪除
//   五. 授權成功及已送出授權者不可修改、刪除 
//       1.BankAuthAct  (成功:Status=0, 已送出:Status=9)
//       2.PostAuthLog  (成功:getAuthErrorCode=00, 已送出:MediaCode=Y)
//       3.ACHAuthLog   (成功:AuthStatus=0, 已送出:MediaCode=Y)
//   六. 依回應碼自行決定是否提示錯誤 
//        FuncCode == 1
//          申請 : ACH:A || POST:1
//          取消 : ACH:D || POST:2
//          returnCode = 0 (新帳號授權)         ->  新增銀扣授權帳號檔，及授權記錄檔   
//          returnCode = 1 (舊帳號授權)         ->  新增銀扣授權帳號檔
//          returnCode = 2 (同額度不同帳號)  ->  新增授權記錄檔 (變更帳號新增建檔)
//          returnCode = 3 (同額度同帳號重複授權)  ->  X
//        FuncCode == 2
//          returnCode = 0 (正常執行)
//          (Error, 授權成功或已送出授權者不可修改、刪除)
//		  FuncCode == 4
//          returnCode = 0 (正常執行)
//          (Error, 授權成功或已送出授權者不可修改、刪除)
//        Else
//          returnCode = 9 (Error, FuncCode != 1、2、4)

@Component("bankAuthActCom")
@Scope("prototype")
public class BankAuthActCom extends TradeBuffer {

	private TitaVo titaVo;

	private int returnCode = 0;
//	private List<TxToDoDetail> detailList = new ArrayList<TxToDoDetail>();
	private TxToDoDetail tTxToDoDetail = new TxToDoDetail();
	// private TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* DB服務注入 */
	@Autowired
	public BankAuthActService bankAuthActService;

	@Autowired
	public AchAuthLogService achAuthLogService;

	@Autowired
	public PostAuthLogService postAuthLogService;

	@Autowired
	public AchAuthLogHistoryService achAuthLogHistoryService;

	@Autowired
	public PostAuthLogHistoryService postAuthLogHistoryService;

	@Autowired
	public BankAuthActComServiceImpl bankAuthActComServiceImpl;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public TxAmlCom txAmlCom;

	private String iCreateFlag = "A";

	private String iAuthApplCode = "1";
	private String iPostDepCode = " ";
	private String iRepayAcct = "";
	private String iRelationCode = "0";
	private String iRelAcctName = "0";
	private String iRelAcctBirthday = "0";
	private String iRelAcctGender = "0";
	private int iFuncCode = 1;
	private int iCustNo = 0;
	private int iFacmNo = 0;
	private String iRepayBank = "";
	private BigDecimal iLimitAmt = BigDecimal.ZERO;
	private String iCustId = "";
	private String iRelationId = "";
	private String iAuthCreateDate = "0";
	private String acctSeq = "";
	private String iAcctSeq = "";
	private String checkFlag = " ";
	private String txType = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.titaVo = titaVo;
		return null;
	}

	/**
	 * 除L2154外，其他交易進入方式
	 * 
	 * @param titaVo
	 * @throws LogicException
	 */
	public int acctCheck(TitaVo titaVo) throws LogicException {

		this.titaVo = titaVo;

		this.info("bankAuthActCom Start...");

		txToDoCom.setTxBuffer(this.getTxBuffer());

		setVarValue(titaVo);

		showLog();

//		正常
		if (titaVo.isHcodeNormal()) {
			this.info("HcodeNormal...");

			this.info("RepayBank : " + iRepayBank);
			if (!"000".equals(iRepayBank)) {

//			BankAuthAct key = custNo + facmNo (一個額度對應一個帳號)
//			Post/ACH log Key = custNo + acctNo (額度之帳號變更歷程)
//				新增
//				BankAuthAct 寫入
//				Post/ACHlog 若為同戶號同帳號不同額度，則不更新，By狀態處理
//				**L441A、L440A單獨寫入D.取消  (要送出媒體)
//				**L4410、L4412經由此程式寫入A.新增(恢復授權)、暫停授權  (不送出媒體)
//				  A-1:新增(恢復授權) 狀態若為A，跳過；狀態為D、Z，新增
//				  D-2:取消                  不再此處理                 
//				  Z-9:暫停授權           狀態為D、Z，跳過；狀態為A，新增

//				新增訂正
//				BankAuthAct 刪除
//				Post/ACHlog 找不到，跳過

//				修改(僅L4410、L4412)，於頁面調Rim，已授權過的帳號後續欄位跳過、不給修改

//				20210321修改
//				從delete+insert改為update(找不到改為insert) ->須根據前筆判斷狀態決定現行狀態(如新增(Aor1)，需判斷是新授權還是前筆暫停)

//				BankAuthAct 找到，跳過。否則新增。
//				Post/ACHlog 若為同戶號同帳號不同額度，則不更新

//				修改無訂正(L2154、L2153無修改)

//				ACH-郵局

				if ("700".equals(iRepayBank)) {
					switch (iFuncCode) {
					case 1:
						bankAcctInsert();

						postLogInsert(titaVo);

						postLogHistoryInsert("1", titaVo);
						postLogHistoryInsert("2", titaVo);
						break;
					case 2:
//						bankAcctDelete();
//						bankAcctInsert();

						bankAcctUpdate();

						postLogDelete(titaVo);

						postLogInsert(titaVo);

						postLogHistoryInsert("1", titaVo);
						postLogHistoryInsert("2", titaVo);

						break;
					case 4:
//						20210311修改為刪除僅刪除LOG
//						bankAcctDelete();
						postLogDelete(titaVo);

						postLogHistoryInsert("1", titaVo);
						postLogHistoryInsert("2", titaVo);

						break;
					default:
						returnCode = 9;
						break;
					}
//			RepayBank = ACH
				} else {
					switch (iFuncCode) {
					case 1:
						bankAcctInsert();
						achLogInsert(titaVo);

						achLogHistoryInsert(titaVo);

						break;
					case 2:
//						bankAcctDelete();
//						bankAcctInsert();

						bankAcctUpdate();

						achLogDelete(titaVo);
						achLogInsert(titaVo);

						achLogHistoryInsert(titaVo);

						break;
					case 4:
//						20210311修改為刪除僅刪除LOG
//						20210406 L2154的刪除需刪除帳號檔
						if ("2".equals(txType)) {
							bankAcctDelete();
						}
						achLogDelete(titaVo);

						achLogHistoryInsert(titaVo);
						break;
					default:
						break;
					}
				}
			}
		}
//		訂正
		else if (titaVo.isHcodeErase()) {
			this.info("HcodeErase...");

			this.info("RepayBank : " + iRepayBank);
			if (!"000".equals(iRepayBank)) {
//				RepayBank = 郵局(700)
				if ("700".equals(iRepayBank)) {
					switch (iFuncCode) {
					case 1:
						bankAcctDelete();

						postLogDelete(titaVo);

						postLogHistoryDelete(dateUtil.getNowIntegerRoc(), "1", titaVo);
						postLogHistoryDelete(dateUtil.getNowIntegerRoc(), "2", titaVo);
						break;
					default:
						returnCode = 9;
						break;
					}
//				RepayBank = ACH
				} else {
					switch (iFuncCode) {
					case 1:
						bankAcctDelete();
						achLogDelete(titaVo);
						achLogHistoryDelete(dateUtil.getNowIntegerRoc(), titaVo);
						break;
					default:
						returnCode = 9;
						break;
					}
				}
			}
		}

		this.info("returnCode : " + returnCode);
		return returnCode;
	}

	private void setVarValue(TitaVo titaVo) throws LogicException {
		if ("L2".equals(titaVo.getTxcd().substring(0, 2))) {
			iPostDepCode = titaVo.get("PostCode");
			iRepayAcct = titaVo.get("RepayAcctNo");
			iRelationCode = titaVo.get("RelationCode");
			iRelAcctName = titaVo.get("RelationName");
			iRelAcctBirthday = titaVo.get("RelationBirthday");
			iRelAcctGender = titaVo.get("RelationGender");
			iCustId = titaVo.getParam("CustId");
			iAuthCreateDate = dateUtil.getNowStringRoc();

		} else if ("L4".equals(titaVo.getTxcd().substring(0, 2))) {
			iPostDepCode = titaVo.get("PostDepCode");
			iRepayAcct = titaVo.get("RepayAcct");
			iRelationCode = titaVo.get("RelationCode");
			iRelAcctName = titaVo.get("RelAcctName");
			iRelAcctBirthday = titaVo.get("RelAcctBirthday");
			iRelAcctGender = titaVo.get("RelAcctGender");
			iAuthCreateDate = titaVo.get("AuthCreateDate");
		}

		iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		iRepayBank = FormatUtil.pad9(titaVo.getParam("RepayBank"), 3);
		iRelationId = titaVo.getParam("RelationId");

		if (titaVo.get("LimitAmt") != null) {
			iLimitAmt = parse.stringToBigDecimal(titaVo.get("LimitAmt"));
		}

		if (titaVo.get("FuncCode") != null) {
			iFuncCode = parse.stringToInteger(titaVo.get("FuncCode"));
		}

		if ("L4410".equals(titaVo.getTxcd())) {
			iCreateFlag = titaVo.getParam("CreateFlag");
		}
		if ("L4412".equals(titaVo.getTxcd())) {
			iAuthApplCode = titaVo.get("AuthApplCode");
			iAcctSeq = titaVo.get("RepayAcctSeq");
			iCustId = titaVo.getParam("CustId");
		}

		if (titaVo.getTxcd() != null && titaVo.getTxcd().length() >= 2) {
			txType = titaVo.getTxcd().substring(1, 2);
		}
	}

	private void showLog() throws LogicException {
		this.info("iFuncCode : " + iFuncCode);
		this.info("iCreateFlag : " + iCreateFlag);
		this.info("iCustId : " + iCustId);
		this.info("iCustNo : " + iCustNo);
		this.info("iFacmNo : " + iFacmNo);
		this.info("iRepayBank : " + iRepayBank);
		this.info("iAuthApplCode : " + iAuthApplCode);
		this.info("iPostDepCode : " + iPostDepCode);
		this.info("iRepayAcct : " + iRepayAcct);
		this.info("iRelationCode : " + iRelationCode);
		this.info("iRelAcctName : " + iRelAcctName);
		this.info("iRelAcctBirthday : " + iRelAcctBirthday);
		this.info("iRelAcctGender : " + iRelAcctGender);
		this.info("iRelationId : " + iRelationId);
		this.info("iLimitAmt : " + iLimitAmt);
		this.info("txType : " + txType);
	}

	private boolean haveAuthLog() throws LogicException {
		boolean result = true;

		if ("700".equals(iRepayBank)) {
			PostAuthLog tPostAuthLog = new PostAuthLog();
			tPostAuthLog = postAuthLogService.pkFacmNoFirst(iAuthApplCode, iCustNo, iPostDepCode, iRepayAcct, "1", iFacmNo, titaVo);
			if (tPostAuthLog == null) {
				result = false;
			}
		} else {
			AchAuthLog tAchAuthLog = new AchAuthLog();
			tAchAuthLog = achAuthLogService.pkFacmNoFirst(iCustNo, iRepayBank, iRepayAcct, iFacmNo);
			if (tAchAuthLog == null) {
				result = false;
			}
		}

		return result;
	}

	private void bankAcctDelete() throws LogicException {
		this.info("bankAcctDelete Start...");

		BankAuthActId tBankAuthActId = new BankAuthActId();

		// Main Table Set Value
		tBankAuthActId.setCustNo(iCustNo);
		tBankAuthActId.setFacmNo(iFacmNo);
		if ("700".equals(iRepayBank)) {
			tBankAuthActId.setAuthType("01");
			bankAcctDelete(tBankAuthActId);
			tBankAuthActId.setAuthType("02");
			bankAcctDelete(tBankAuthActId);
		} else {
			tBankAuthActId.setAuthType("00");
			bankAcctDelete(tBankAuthActId);
		}
	}

	private void bankAcctDelete(BankAuthActId tBankAuthActId) throws LogicException {
		BankAuthAct tBankAuthAct = new BankAuthAct();
		tBankAuthAct = bankAuthActService.holdById(tBankAuthActId);

//		L2 訂正需刪除主檔，否則下次新增重複
		if (tBankAuthAct != null) {
			// 0成功 9正在授權
			if ("0".equals(tBankAuthAct.getStatus()) && "4".equals(txType)) {
//				throw new LogicException(titaVo, "E0006", "此筆已授權成功");
				this.info("此筆已授權成功，return... ");
				return;
			} else if ("9".equals(tBankAuthAct.getStatus()) && "4".equals(txType)) {
//				throw new LogicException(titaVo, "E0006", "此筆已送出授權");
				this.info("此筆已送出授權，return... ");
				return;
			} else {
				try {
					bankAuthActService.delete(tBankAuthAct);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
		} else {
			this.info("查無資料... ");
		}
	}

	private void bankAcctUpdate() throws LogicException {
		this.info("bankAcctDelete Start...");
		checkFlag();

		BankAuthActId tBankAuthActId = new BankAuthActId();
//		僅需計算一次
		getAcctSeq();

		// Main Table Set Value
		tBankAuthActId.setCustNo(iCustNo);
		tBankAuthActId.setFacmNo(iFacmNo);
		if ("700".equals(iRepayBank)) {
			tBankAuthActId.setAuthType("01");
			bankAcctUpdate(tBankAuthActId);
			tBankAuthActId.setAuthType("02");
			bankAcctUpdate(tBankAuthActId);
		} else {
			tBankAuthActId.setAuthType("00");
			bankAcctUpdate(tBankAuthActId);
		}
	}

//	BankAuthAct status
//	空白:未授權
//	0:授權成功       授權提回更新      
//	1:停止使用       0:授權成功時維護；恢復=>維護回0:授權成功
//	2.取消授權       授權提回更新 
//	9:已送出授權

//	PostAuthLog status
//	1.申請(恢復授權)
//	2.終止
//	3.郵局終止
//	4.誤終止
//	9.暫停授權

//	AchAuthLog status
//	A:新增(恢復授權)
//	D:取消
//	Z:暫停授權
	private void bankAcctUpdate(BankAuthActId tBankAuthActId) throws LogicException {
		this.info("bankAcctUpdate Start...");

//		僅L4412 L4410 修改(僅能修改狀態為暫停或恢復) 時，會進入Update
		List<BankAuthAct> lBankAuthAct = new ArrayList<BankAuthAct>();

		Slice<BankAuthAct> sBankAuthAct = bankAuthActService.authCheck(iCustNo, iRepayAcct, 0, 999, this.index, this.limit, titaVo);

		lBankAuthAct = sBankAuthAct == null ? null : sBankAuthAct.getContent();
		if (lBankAuthAct != null && lBankAuthAct.size() != 0) {
			for (BankAuthAct t2BankAuthAct : lBankAuthAct) {
				if (!t2BankAuthAct.getAuthType().equals(tBankAuthActId.getAuthType())) {
					this.info("t2BankAuthAct.getAuthType() : " + t2BankAuthAct.getAuthType());
					this.info("tBankAuthActId.getAuthType() : " + tBankAuthActId.getAuthType());
					this.info("continue ... ");
					continue;
				}

				this.info("iRepayBank : " + iRepayBank);
				this.info("iAuthApplCode : " + iAuthApplCode);
				this.info("checkFlag : " + checkFlag);
				this.info("t2BankAuthAct : " + t2BankAuthAct.toString());
				BankAuthAct tBankAuthAct = new BankAuthAct();

				tBankAuthAct.setBankAuthActId(t2BankAuthAct.getBankAuthActId());
				tBankAuthAct.setRepayBank(iRepayBank);
				tBankAuthAct.setPostDepCode(iPostDepCode);
				tBankAuthAct.setRepayAcct(iRepayAcct);

//				空白:未授權 0:授權成功 1:暫停使用 2.取消授權 9:已送出授權
				if ("700".equals(iRepayBank)) {
					if ("9".equals(iAuthApplCode)) {
						switch (t2BankAuthAct.getStatus()) {
						case " ":
							throw new LogicException("E0007", "此筆尚未授權");
						case "0":
							tBankAuthAct.setStatus("1");
							break;
						case "1":
							tBankAuthAct.setStatus("1");
							break;
						case "2":
							throw new LogicException("E0007", "此筆已取消授權");
						case "9":
							throw new LogicException("E0007", "此筆已送出授權");
						default:
							break;
						}
					} else {
						switch (t2BankAuthAct.getStatus()) {
						case " ":
							tBankAuthAct.setStatus(checkFlag);
							break;
						case "0":
							if (" ".equals(checkFlag)) {
//							已授權成功未授權跳過，待吃檔回寫
								return;
							} else {
								tBankAuthAct.setStatus(checkFlag);
							}
							break;
						case "1":
							tBankAuthAct.setStatus("0");
							break;
						case "2":
							tBankAuthAct.setStatus("2");
							break;
						case "9":
							throw new LogicException("E0007", "此筆已送出授權");
						default:
							break;
						}
					}
				} else {
					if ("Z".equals(iCreateFlag)) {
						switch (t2BankAuthAct.getStatus()) {
						case " ":
							throw new LogicException("E0007", "此筆尚未授權");
						case "0":
							tBankAuthAct.setStatus("1");
							break;
						case "1":
							tBankAuthAct.setStatus("1");
							break;
						case "2":
							throw new LogicException("E0007", "此筆已取消授權");
						case "9":
							throw new LogicException("E0007", "此筆已送出授權");
						default:
							break;
						}
					} else {
						switch (t2BankAuthAct.getStatus()) {
						case " ":
//								1.此帳號已授權過
							tBankAuthAct.setStatus(checkFlag);
							break;
						case "0":
							if (" ".equals(checkFlag)) {
//								已授權成功未授權跳過，待吃檔回寫
								return;
//								tBankAuthAct.setStatus(" ");
							} else {
								tBankAuthAct.setStatus(checkFlag);
							}
							break;
						case "1":
							tBankAuthAct.setStatus("0");
							break;
						case "2":
							tBankAuthAct.setStatus("2");
							break;
						case "9":
							throw new LogicException("E0007", "此筆已送出授權");
						default:
							break;
						}
					}
				}

				if (!"700".equals(iRepayBank)) {
					tBankAuthAct.setLimitAmt(iLimitAmt);
				}

//				來源僅L4412 
				tBankAuthAct.setAcctSeq(iAcctSeq);

				try {
					this.info("tBankAuthAct : " + tBankAuthAct.toString());
					bankAuthActService.update(tBankAuthAct, titaVo);
				} catch (DBException e) {
					this.info("帳號授權檔更新失敗 ..." + e.getErrorMsg());
					throw new LogicException(titaVo, "E0007", "");
				}
			}
		} else {
			bankAcctInsert(tBankAuthActId);
		}
	}

	private void bankAcctInsert() throws LogicException {
		this.info("bankAcctInsert Start...");
		BankAuthActId tBankAuthActId = new BankAuthActId();

		// Main Table Set Value
		tBankAuthActId.setCustNo(iCustNo);
		tBankAuthActId.setFacmNo(iFacmNo);
		if ("700".equals(iRepayBank)) {
//			僅需計算一次
			getAcctSeq();
			checkFlag();

			tBankAuthActId.setAuthType("01");
			bankAcctInsert(tBankAuthActId);
			tBankAuthActId.setAuthType("02");
			bankAcctInsert(tBankAuthActId);
		} else {
			tBankAuthActId.setAuthType("00");
			bankAcctInsert(tBankAuthActId);
		}

	}

	private void bankAcctInsert(BankAuthActId tBankAuthActId) throws LogicException {
//		BankAuthAct t2BankAuthAct = new BankAuthAct();
		Slice<BankAuthAct> sBankAuthAct = null;
		sBankAuthAct = bankAuthActService.facmNoEq(iCustNo, iFacmNo, index, limit, titaVo);

		if (sBankAuthAct != null) {
			this.info("重複戶號額度 ..." + sBankAuthAct.getContent().toString());
//			L4410、L4412進入
//			原為Ach或Post換建立另一個時，需其中一個已授權成功
			if (iFuncCode == 1) {
				PostAuthLog tPostAuthLog = postAuthLogService.facmNoBFirst(iCustNo, iFacmNo, titaVo);

				if (tPostAuthLog != null) {
					String errorCode = "";

					if (tPostAuthLog.getAuthErrorCode() != null) {
						errorCode = tPostAuthLog.getAuthErrorCode().trim();
					}

					this.info("errorCode ... " + errorCode);

//					已有未送回之LOG檔需中斷
					if ("".equals(errorCode)) {
						throw new LogicException(titaVo, "E0005", "重複戶號額度，郵局授權記錄檔已存在");
					}
//					郵局不同CustId重新申請授權，BankAuthAct檔狀態更新為空白(未授權)
					else {
						this.info("errorCode ... " + errorCode);
						if (!iCustId.equals(tPostAuthLog.getCustId())) {
							this.info("iCustId ... " + iCustId);
							this.info("getCustId ... " + tPostAuthLog.getCustId());
							BankAuthAct t3BankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);
							if (t3BankAuthAct != null) {
								if ("700".equals(t3BankAuthAct.getRepayBank()) && "00".equals(errorCode)) {
									t3BankAuthAct.setStatus(" ");
									try {
										bankAuthActService.update(t3BankAuthAct, titaVo);
									} catch (DBException e) {
										this.info("BankAuthAct insert error ..." + e.getErrorMsg());
										throw new LogicException(titaVo, "E0005", "重複戶號額度");
									}
								} else {
									this.info("RepayBank ... " + t3BankAuthAct.getRepayBank());
									this.info("errorCode ... " + errorCode);
								}
							} else {
								this.info("t3BankAuthAct null...");
							}
						} else {
							this.info("CustId 相同... ");
						}
					}
				}
				AchAuthLog tAchAuthLog = achAuthLogService.facmNoBFirst(iCustNo, iFacmNo, titaVo);

				if (tAchAuthLog != null) {
					String errorCode = "";

					if (tAchAuthLog.getAuthStatus() != null) {
						errorCode = tAchAuthLog.getAuthStatus().trim();
					}

					this.info("errorCode ... " + errorCode);

					if ("".equals(errorCode)) {
//						帳號重複申請
						throw new LogicException(titaVo, "E0005", "重複戶號額度，Ach授權記錄檔已存在");
					}
				}

//				需個別PK查詢，否則只新增期款，未新增火險
				BankAuthAct t2BankAuthAct = bankAuthActService.findById(tBankAuthActId, titaVo);

				if (t2BankAuthAct == null) {
					BankAuthAct tBankAuthAct = new BankAuthAct();

					tBankAuthAct.setBankAuthActId(tBankAuthActId);

					tBankAuthAct.setRepayBank(iRepayBank);
					tBankAuthAct.setRepayAcct(iRepayAcct);
					if (!"700".equals(iRepayBank)) {
						tBankAuthAct.setPostDepCode(" ");
					} else {
						tBankAuthAct.setPostDepCode(iPostDepCode);
					}

					tBankAuthAct.setStatus(checkFlag);

					if (!"700".equals(iRepayBank)) {
						tBankAuthAct.setLimitAmt(iLimitAmt);
					}

					tBankAuthAct.setAcctSeq(acctSeq);

					try {
						bankAuthActService.insert(tBankAuthAct);
					} catch (DBException e) {
						this.info("BankAuthAct insert error ..." + e.getErrorMsg());
						throw new LogicException(titaVo, "E0005", "重複戶號額度");
					}
				}
			}
		} else {
			BankAuthAct tBankAuthAct = new BankAuthAct();

			tBankAuthAct.setBankAuthActId(tBankAuthActId);

			tBankAuthAct.setRepayBank(iRepayBank);
			tBankAuthAct.setRepayAcct(iRepayAcct);
			if (!"700".equals(iRepayBank)) {
				tBankAuthAct.setPostDepCode(" ");
			} else {
				tBankAuthAct.setPostDepCode(iPostDepCode);
			}

			tBankAuthAct.setStatus(checkFlag);

			if (!"700".equals(iRepayBank)) {
				tBankAuthAct.setLimitAmt(iLimitAmt);
			}

			tBankAuthAct.setAcctSeq(acctSeq);

			try {
				bankAuthActService.insert(tBankAuthAct);
			} catch (DBException e) {
				this.info("BankAuthAct insert error ..." + e.getErrorMsg());
				throw new LogicException(titaVo, "E0005", "重複戶號額度");
			}
		}
	}

	private void postLogDelete(TitaVo titaVo) throws LogicException {
		this.info("postLogDelete Start...");
		// 訂正 郵局
		PostAuthLogId tPostAuthLogId = new PostAuthLogId();

		tPostAuthLogId.setAuthCreateDate(parse.stringToInteger(iAuthCreateDate));
		tPostAuthLogId.setAuthApplCode(iAuthApplCode);
		tPostAuthLogId.setCustNo(iCustNo);
		tPostAuthLogId.setPostDepCode(iPostDepCode);
		tPostAuthLogId.setRepayAcct(iRepayAcct);
		tPostAuthLogId.setAuthCode("1");
		postLogDelete(tPostAuthLogId, titaVo);

		tPostAuthLogId.setAuthCode("2");
		postLogDelete(tPostAuthLogId, titaVo);

		// 1 訂正應處理明細
		tTxToDoDetail.setItemCode("POSP00");
		tTxToDoDetail.setCustNo(iCustNo);
		tTxToDoDetail.setFacmNo(iFacmNo);
		tTxToDoDetail.setDtlValue(FormatUtil.pad9(iRepayAcct, 14));
		tTxToDoDetail.setStatus(0);
		txToDoCom.addDetail(true, 1, tTxToDoDetail, titaVo);

//		刪除更新主檔
		if (iFuncCode == 4) {
			changeAcctNo(titaVo);
		}
	}

	private void postLogDelete(PostAuthLogId tPostAuthLogId, TitaVo titaVo) throws LogicException {
		this.info("postLogDelete Start 2 ...");

		PostAuthLog tPostAuthLog = new PostAuthLog();

		tPostAuthLog = postAuthLogService.findById(tPostAuthLogId, titaVo);

		if (tPostAuthLog != null) {
			this.info("AuthErrorCode ..." + tPostAuthLog.getAuthErrorCode());
			this.info("PostMediaCode ..." + tPostAuthLog.getPostMediaCode());
			if ("00".equals(tPostAuthLog.getAuthErrorCode()) || "Y".equals(tPostAuthLog.getPostMediaCode())) {
//				暫停授權改為暫停取消，重複前筆改為刪除現在這筆

				PostAuthLog t2PostAuthLog = postAuthLogService.repayAcctFirst(tPostAuthLogId.getCustNo(), tPostAuthLogId.getPostDepCode(), tPostAuthLogId.getRepayAcct(), tPostAuthLogId.getAuthCode(),
						titaVo);
				if (t2PostAuthLog != null) {
					this.info("t2PostAuthLog ..." + t2PostAuthLog.toString());

					PostAuthLogId t2PostAuthLogId = new PostAuthLogId();

					String tempCode = "";
					if ("9".equals(iAuthApplCode)) {
						tempCode = "1";
					} else if ("1".equals(iAuthApplCode)) {
						tempCode = "9";
					}

					t2PostAuthLogId.setAuthCreateDate(parse.stringToInteger(iAuthCreateDate));
					t2PostAuthLogId.setAuthApplCode(tempCode);
					t2PostAuthLogId.setCustNo(iCustNo);
					t2PostAuthLogId.setPostDepCode(iPostDepCode);
					t2PostAuthLogId.setRepayAcct(iRepayAcct);
					t2PostAuthLogId.setAuthCode(tPostAuthLogId.getAuthCode());

					PostAuthLog t3PostAuthLog = postAuthLogService.holdById(t2PostAuthLogId, titaVo);

					if (t3PostAuthLog != null) {
						try {
							postAuthLogService.delete(t3PostAuthLog, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0006", e.getErrorMsg());
						}
					} else {
						throw new LogicException(titaVo, "E0006", "此筆已產出媒體");
					}
				} else {
					throw new LogicException(titaVo, "E0006", "此筆已產出媒體");
				}
			} else {
				try {
					postAuthLogService.delete(tPostAuthLog);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
		} else {
			this.info("postLogDelete null ...");
		}
	}

	private void postLogInsert(TitaVo titaVo) throws LogicException {
		this.info("postLogInsert Start...");

		PostAuthLogId tPostAuthLogId = new PostAuthLogId();

//		20200302改為此交易皆為申請，終止改為至4041選單勾選

//		QC.738 建檔日應為系統日期而非會計日期
		if (iFuncCode == 1) {
			tPostAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
		} else {
			int authCreateDate = parse.stringToInteger(iAuthCreateDate);

			if (authCreateDate > 0 && authCreateDate < 19110000) {
				authCreateDate = authCreateDate + 19110000;
			}
			tPostAuthLogId.setAuthCreateDate(authCreateDate);
		}
		tPostAuthLogId.setAuthApplCode(iAuthApplCode);
		tPostAuthLogId.setCustNo(iCustNo);
		tPostAuthLogId.setPostDepCode(iPostDepCode);
		tPostAuthLogId.setRepayAcct(iRepayAcct);
		tPostAuthLogId.setAuthCode("1");
		postLogInsert(tPostAuthLogId, titaVo);
		tPostAuthLogId.setAuthCode("2");
		postLogInsert(tPostAuthLogId, titaVo);

//		0=此帳號為授權成功過
		if (" ".equals(checkFlag)) {
			// 0 新增應處理明細
			tTxToDoDetail.setItemCode("POSP00");
			tTxToDoDetail.setCustNo(iCustNo);
			tTxToDoDetail.setFacmNo(iFacmNo);
			tTxToDoDetail.setDtlValue(FormatUtil.pad9(iRepayAcct, 14));
			tTxToDoDetail.setStatus(0);
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);
		}
	}

	private void postLogInsert(PostAuthLogId tPostAuthLogId, TitaVo titaVo) throws LogicException {
		this.info("postLogInsert 2  Start...");

		PostAuthLog tPostAuthLog = new PostAuthLog();
		PostAuthLog t2PostAuthLog = new PostAuthLog();

		t2PostAuthLog = postAuthLogService.repayAcctFirst(iCustNo, iPostDepCode, iRepayAcct, tPostAuthLogId.getAuthCode(), titaVo);

		if (t2PostAuthLog == null) {
			tPostAuthLog.setPostAuthLogId(tPostAuthLogId);
			tPostAuthLog.setFacmNo(iFacmNo);
			tPostAuthLog.setCustId(iCustId);
			tPostAuthLog.setRepayAcctSeq(acctSeq);
			tPostAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
//			1=已授權成功過 2=暫停(暫停改回成功也須改log為已授權)

			this.info("checkFlag ..." + checkFlag);

			switch (checkFlag) {
			case " ":
				tPostAuthLog.setAuthErrorCode(" ");
				break;
			case "0":
				tPostAuthLog.setAuthErrorCode("00");
				tPostAuthLog.setPostMediaCode("Y");
				break;
			case "1":
				tPostAuthLog.setAuthErrorCode("00");
				tPostAuthLog.setPostMediaCode("Y");
				break;
			case "2":
				tPostAuthLog.setAuthErrorCode("00");
				tPostAuthLog.setPostMediaCode("Y");
				break;
			case "9":
				tPostAuthLog.setAuthErrorCode(" ");
				tPostAuthLog.setPostMediaCode("Y");
				break;
			default:
				tPostAuthLog.setAuthErrorCode(" ");
				break;

			}
			tPostAuthLog.setRelationCode(iRelationCode);
			tPostAuthLog.setRelAcctName(iRelAcctName);
			tPostAuthLog.setRelationId(iRelationId);
			tPostAuthLog.setRelAcctBirthday(parse.stringToInteger(iRelAcctBirthday));
			tPostAuthLog.setRelAcctGender(iRelAcctGender);

			try {
				postAuthLogService.insert(tPostAuthLog);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "PostAuthLog insert " + e.getErrorMsg());
			}
		} else {
			this.info("郵局授權紀錄檔已有資料");

//			2153 新增時，若狀態為終止，新增一筆未授權
			int flag = 0;

			if ("2".equals(txType) && "2".equals(t2PostAuthLog.getAuthApplCode()) && "00".equals(t2PostAuthLog.getAuthErrorCode())) {
				flag = 1;
			}

			String tempCode = "";
			if ("9".equals(iAuthApplCode)) {
				tempCode = "1";
			} else if ("1".equals(iAuthApplCode)) {
				tempCode = "9";
			}
//			若第一筆為暫停需在insert一筆
			if ("4".equals(txType) && tempCode.equals(t2PostAuthLog.getAuthApplCode())) {
				flag = 2;
			}

//			同帳號，變更ID
			if ("4".equals(txType) && !iCustId.equals(t2PostAuthLog.getCustId())) {
				flag = 3;
			}

			this.info("flag ... " + flag);

			if (flag >= 1) {
				tPostAuthLog.setPostAuthLogId(tPostAuthLogId);
				tPostAuthLog.setFacmNo(iFacmNo);
				tPostAuthLog.setCustId(iCustId);
				tPostAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
//				1=狀態為終止，新增一筆未授權
//				2=暫停改授權/授權改暫停(暫停改回成功也須改log為已授權)
				if (flag == 2) {
					tPostAuthLog.setAuthErrorCode("00");
					tPostAuthLog.setPostMediaCode("Y");
					tPostAuthLog.setRepayAcctSeq(iAcctSeq);
				} else {
					tPostAuthLog.setAuthErrorCode(" ");
					tPostAuthLog.setRepayAcctSeq(acctSeq);
				}
				tPostAuthLog.setRelationCode(iRelationCode);
				tPostAuthLog.setRelAcctName(iRelAcctName);
				tPostAuthLog.setRelationId(iRelationId);
				tPostAuthLog.setRelAcctBirthday(parse.stringToInteger(iRelAcctBirthday));
				tPostAuthLog.setRelAcctGender(iRelAcctGender);

				try {
					postAuthLogService.insert(tPostAuthLog);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "PostAuthLog insert " + e.getErrorMsg());
				}
//				0 新增應處理明細
//				暫停新增不用寫入，終止寫入需寫
				if (flag == 1) {
					tTxToDoDetail.setItemCode("POSP00");
					tTxToDoDetail.setCustNo(iCustNo);
					tTxToDoDetail.setFacmNo(iFacmNo);
					tTxToDoDetail.setDtlValue(" ");
					tTxToDoDetail.setStatus(0);
					txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // boolen: true:重複時跳過， 0.正常1.訂正
				}
			}
		}
	}

//  L4043按鈕進入之新增、修改、刪除皆於History新增，且Seq+1
//	刪除=寫入刪除日期
//	刪除回復=回復被刪除之LOG、刪除有刪除日期之History
	private void postLogHistoryInsert(String authCode, TitaVo titaVo) throws LogicException {
		PostAuthLogHistory tPostAuthLogHistory = new PostAuthLogHistory();
		PostAuthLogHistoryId tPostAuthLogHistoryId = new PostAuthLogHistoryId();
		int postDetailSeq = 1;
		int authCreateDate = dateUtil.getNowIntegerRoc();
		int stampFinshDate = 0;
		int stampCancelDate = 0;
		int propDate = 0;
		int retrDate = 0;
		int deleteDate = 0;
		int procDate = 0;
		String amlRsp = "";
		String repayAcctSeq = "";
		String stampCode = "";
		String authErrorCode = "";
		String mediaCode = "";

		PostAuthLogHistory t1PostAuthLogHistory = postAuthLogHistoryService.facmNoCFirst(iCustNo, iFacmNo, authCode, titaVo);

		if (t1PostAuthLogHistory != null) {
			postDetailSeq = t1PostAuthLogHistory.getDetailSeq() + 1;
			authCreateDate = t1PostAuthLogHistory.getAuthCreateDate();
			stampFinshDate = t1PostAuthLogHistory.getStampFinishDate();
			stampCancelDate = t1PostAuthLogHistory.getStampCancelDate();
			propDate = t1PostAuthLogHistory.getPropDate();
			retrDate = t1PostAuthLogHistory.getRetrDate();
			deleteDate = t1PostAuthLogHistory.getDeleteDate();
			procDate = t1PostAuthLogHistory.getProcessDate();
			amlRsp = t1PostAuthLogHistory.getAmlRsp();
			repayAcctSeq = t1PostAuthLogHistory.getRepayAcctSeq();
			stampCode = t1PostAuthLogHistory.getStampCode();
			authErrorCode = t1PostAuthLogHistory.getAuthErrorCode();
			mediaCode = t1PostAuthLogHistory.getPostMediaCode();
		}

		if (iFuncCode != 1) {

			if (authCreateDate > 0 && authCreateDate < 19110000) {
				authCreateDate = authCreateDate + 19110000;
			}
			if (stampFinshDate > 0 && stampFinshDate < 19110000) {
				stampFinshDate = stampFinshDate + 19110000;
			}
			if (propDate > 0 && propDate < 19110000) {
				propDate = propDate + 19110000;
			}
			if (retrDate > 0 && retrDate < 19110000) {
				retrDate = retrDate + 19110000;
			}
		}

		tPostAuthLogHistoryId.setCustNo(iCustNo);
		tPostAuthLogHistoryId.setFacmNo(iFacmNo);
		tPostAuthLogHistoryId.setAuthCode(authCode);
		tPostAuthLogHistoryId.setDetailSeq(postDetailSeq);

		tPostAuthLogHistory.setPostAuthLogHistoryId(tPostAuthLogHistoryId);
		tPostAuthLogHistory.setAuthCreateDate(authCreateDate);
		tPostAuthLogHistory.setAuthApplCode(iAuthApplCode);
		tPostAuthLogHistory.setPostDepCode(iPostDepCode);
		tPostAuthLogHistory.setRepayAcct(iRepayAcct);
		tPostAuthLogHistory.setCustId(iCustId);
		tPostAuthLogHistory.setStampCode(stampCode);
		tPostAuthLogHistory.setRepayAcctSeq(repayAcctSeq);
		tPostAuthLogHistory.setStampFinishDate(stampFinshDate);
		tPostAuthLogHistory.setStampCancelDate(stampCancelDate);
		tPostAuthLogHistory.setPropDate(propDate);
		tPostAuthLogHistory.setRetrDate(retrDate);
		tPostAuthLogHistory.setAmlRsp(amlRsp);
		tPostAuthLogHistory.setProcessDate(dateUtil.getNowIntegerForBC());
//		1=已授權成功過 2=暫停(暫停改回成功也須改log為已授權)

		if (iFuncCode == 4) {
//			刪除
			if (deleteDate == 0) {
				tPostAuthLogHistory.setDeleteDate(dateUtil.getNowIntegerForBC());
			}
//			刪除回復
//			刪除僅可謂送出/提回資料，最基本欄位input
			else {
				postLogHistoryDelete(authCreateDate, authCode, titaVo);

				PostAuthLogId tPostAuthLogId = new PostAuthLogId();
				PostAuthLog tPostAuthLog = new PostAuthLog();
				tPostAuthLogId.setAuthCreateDate(authCreateDate);
				tPostAuthLogId.setAuthApplCode(iAuthApplCode);
				tPostAuthLogId.setPostDepCode(iPostDepCode);
				tPostAuthLogId.setRepayAcct(iRepayAcct);
				tPostAuthLogId.setCustNo(iCustNo);
				tPostAuthLogId.setAuthCode(authCode);

				tPostAuthLog.setPostAuthLogId(tPostAuthLogId);
				tPostAuthLog.setFacmNo(iFacmNo);
				tPostAuthLog.setCustId(iCustId);
				tPostAuthLog.setRepayAcctSeq(repayAcctSeq);
				tPostAuthLog.setProcessDate(procDate);

				tPostAuthLog.setAuthErrorCode(authErrorCode);
				tPostAuthLog.setPostMediaCode(mediaCode);

				tPostAuthLog.setRelationCode(iRelationCode);
				tPostAuthLog.setRelAcctName(iRelAcctName);
				tPostAuthLog.setRelationId(iRelationId);
				tPostAuthLog.setRelAcctBirthday(parse.stringToInteger(iRelAcctBirthday));
				tPostAuthLog.setRelAcctGender(iRelAcctGender);

				try {
					postAuthLogService.insert(tPostAuthLog);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "PostAuthLog insert " + e.getErrorMsg());
				}

				this.info("刪除回復結束 ...");
				return;
			}
		}

		this.info("checkFlag ..." + checkFlag);

		switch (checkFlag) {
		case " ":
			tPostAuthLogHistory.setAuthErrorCode(" ");
			break;
		case "0":
			tPostAuthLogHistory.setAuthErrorCode("00");
			tPostAuthLogHistory.setPostMediaCode("Y");
			break;
		case "1":
			tPostAuthLogHistory.setAuthErrorCode("00");
			tPostAuthLogHistory.setPostMediaCode("Y");
			break;
		case "2":
			tPostAuthLogHistory.setAuthErrorCode("00");
			tPostAuthLogHistory.setPostMediaCode("Y");
			break;
		case "9":
			tPostAuthLogHistory.setAuthErrorCode(" ");
			tPostAuthLogHistory.setPostMediaCode("Y");
			break;
		default:
			tPostAuthLogHistory.setAuthErrorCode(" ");
			break;

		}
		tPostAuthLogHistory.setRelationCode(iRelationCode);
		tPostAuthLogHistory.setRelAcctName(iRelAcctName);
		tPostAuthLogHistory.setRelationId(iRelationId);
		tPostAuthLogHistory.setRelAcctBirthday(parse.stringToInteger(iRelAcctBirthday));
		tPostAuthLogHistory.setRelAcctGender(iRelAcctGender);

		try {
			postAuthLogHistoryService.insert(tPostAuthLogHistory, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "");
		}
	}

//	訂正刪除該筆History
	private void postLogHistoryDelete(int authCreateDate, String authCode, TitaVo titaVo) throws LogicException {
		if (authCreateDate < 19110000) {
			authCreateDate += 19110000;
		}

		PostAuthLogHistory tPostAuthLogHistory = new PostAuthLogHistory();

		tPostAuthLogHistory = postAuthLogHistoryService.authLogFirst(authCreateDate, iAuthApplCode, iCustNo, iPostDepCode, iRepayAcct, authCode, titaVo);

		if (tPostAuthLogHistory != null) {

			tPostAuthLogHistory = postAuthLogHistoryService.holdById(tPostAuthLogHistory, titaVo);

			try {
				postAuthLogHistoryService.delete(tPostAuthLogHistory, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "");
			}
		} else {
			throw new LogicException(titaVo, "E0001", "PostAuthLogHistory檔，無訂正資料");
		}
	}

	private void achLogDelete(TitaVo titaVo) throws LogicException {
		this.info("achLogDelete Start...");

		AchAuthLog deleAchAuthLog = new AchAuthLog();
		AchAuthLogId deleAchAuthLogId = new AchAuthLogId();

		deleAchAuthLogId.setAuthCreateDate(parse.stringToInteger(iAuthCreateDate));
		deleAchAuthLogId.setCustNo(iCustNo);
		deleAchAuthLogId.setRepayBank(iRepayBank);
		deleAchAuthLogId.setRepayAcct(iRepayAcct);
		deleAchAuthLogId.setCreateFlag(iCreateFlag);

		deleAchAuthLog = achAuthLogService.holdById(deleAchAuthLogId);

		if (deleAchAuthLog != null) {
			if (deleAchAuthLog.getAuthStatus().equals("0") || deleAchAuthLog.getMediaCode().equals("Y")) {
//				暫停授權改為暫停取消，重複前筆改為刪除現在這筆

				AchAuthLog dele2AchAuthLog = achAuthLogService.repayAcctFirst(iCustNo, iRepayBank, iRepayAcct, titaVo);

				if (dele2AchAuthLog != null) {

					AchAuthLogId tAchAuthLogId = new AchAuthLogId();

					String tempCode = "";
					if ("Z".equals(iCreateFlag)) {
						tempCode = "A";
					} else if ("A".equals(iCreateFlag)) {
						tempCode = "Z";
					}

					tAchAuthLogId.setAuthCreateDate(parse.stringToInteger(iAuthCreateDate));
					tAchAuthLogId.setCustNo(iCustNo);
					tAchAuthLogId.setRepayBank(iRepayBank);
					tAchAuthLogId.setRepayAcct(iRepayAcct);
					tAchAuthLogId.setCreateFlag(tempCode);

					AchAuthLog tAchAuthLog = achAuthLogService.holdById(tAchAuthLogId, titaVo);

					if (tAchAuthLog != null) {
						try {
							achAuthLogService.delete(tAchAuthLog, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0008", e.getErrorMsg());
						}
					} else {
						throw new LogicException(titaVo, "E0008", "");
					}
				} else {
					throw new LogicException(titaVo, "E0008", "");
				}
			} else {
				try {
					achAuthLogService.delete(deleAchAuthLog);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}

				// 1 訂正應處理明細
				tTxToDoDetail.setItemCode("ACHP00");
				tTxToDoDetail.setCustNo(deleAchAuthLog.getAchAuthLogId().getCustNo());
				tTxToDoDetail.setFacmNo(deleAchAuthLog.getFacmNo());
				tTxToDoDetail.setDtlValue(" ");
				tTxToDoDetail.setStatus(0);
				txToDoCom.addDetail(true, 1, tTxToDoDetail, titaVo);

//				刪除更新主檔
				if (iFuncCode == 4) {
					changeAcctNo(titaVo);
				}
			}
		}
	}

	private void achLogInsert(TitaVo titaVo) throws LogicException {
		this.info("achLogInsert Start...");

		AchAuthLog tAchAuthLog = new AchAuthLog();
		AchAuthLog t2AchAuthLog = new AchAuthLog();
		AchAuthLogId achAuthLogId = new AchAuthLogId();

//		QC.738 建檔日應為系統日期而非會計日期
		if (iFuncCode == 1) {
			achAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
		} else {
			int authCreateDate = parse.stringToInteger(iAuthCreateDate);

			if (authCreateDate > 0 && authCreateDate < 19110000) {
				authCreateDate = authCreateDate + 19110000;
			}
			achAuthLogId.setAuthCreateDate(authCreateDate);
		}
		achAuthLogId.setCustNo(iCustNo);
		achAuthLogId.setRepayBank(iRepayBank);
		achAuthLogId.setRepayAcct(iRepayAcct);
		achAuthLogId.setCreateFlag(iCreateFlag);

		t2AchAuthLog = achAuthLogService.repayAcctFirst(iCustNo, iRepayBank, iRepayAcct, titaVo);

		if (t2AchAuthLog == null) {
			tAchAuthLog.setAchAuthLogId(achAuthLogId);

			tAchAuthLog.setFacmNo(iFacmNo);
			tAchAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
			tAchAuthLog.setLimitAmt(iLimitAmt);
			tAchAuthLog.setAuthMeth("A");

			switch (checkFlag) {
			case " ":
				tAchAuthLog.setAuthStatus(" ");
				break;
			case "0":
				tAchAuthLog.setAuthStatus("0");
				tAchAuthLog.setMediaCode("Y");
				break;
			case "1":
				tAchAuthLog.setAuthStatus("0");
				tAchAuthLog.setMediaCode("Y");
				break;
			case "2":
				tAchAuthLog.setAuthStatus("0");
				tAchAuthLog.setMediaCode("Y");
				break;
			case "9":
				tAchAuthLog.setAuthStatus(" ");
				tAchAuthLog.setMediaCode("Y");
				break;
			default:
				tAchAuthLog.setAuthStatus(" ");
				break;
			}
			tAchAuthLog.setRelationCode(iRelationCode);
			tAchAuthLog.setRelAcctName(iRelAcctName);
			tAchAuthLog.setRelationId(iRelationId);
			tAchAuthLog.setRelAcctBirthday(parse.stringToInteger(iRelAcctBirthday));
			tAchAuthLog.setRelAcctGender(iRelAcctGender);

			try {
				achAuthLogService.insert(tAchAuthLog);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "AchAuthLog insert " + e.getErrorMsg());
			}
//			1.已授權成功過
			if (" ".equals(checkFlag)) {
				// 0 新增應處理明細
				tTxToDoDetail.setItemCode("ACHP00");
				tTxToDoDetail.setCustNo(iCustNo);
				tTxToDoDetail.setFacmNo(iFacmNo);
				tTxToDoDetail.setDtlValue(" ");
				tTxToDoDetail.setStatus(0);
				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // boolen: true:重複時跳過， 0.正常1.訂正
			}
		} else {
			this.info("AchAuthLog 已有此帳號跳過...");
//			2153 新增時，若狀態為終止，新增一筆未授權
			int flag = 0;

			if ("2".equals(txType) && "D".equals(t2AchAuthLog.getCreateFlag()) && "0".equals(t2AchAuthLog.getAuthStatus())) {
				flag = 1;
			}

			String tempCode = "";
			if ("Z".equals(iCreateFlag)) {
				tempCode = "A";
			} else if ("A".equals(iCreateFlag)) {
				tempCode = "Z";
			}

//			若第一筆為暫停需在insert一筆
			if ("4".equals(txType) && tempCode.equals(t2AchAuthLog.getCreateFlag())) {
				flag = 2;
			}

			this.info("flag ... " + flag);

			if (flag >= 1) {
				tAchAuthLog.setAchAuthLogId(achAuthLogId);

				tAchAuthLog.setFacmNo(iFacmNo);
				tAchAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
				tAchAuthLog.setLimitAmt(iLimitAmt);
				tAchAuthLog.setAuthMeth("A");

				if (flag == 2) {
					tAchAuthLog.setAuthStatus("0");
					tAchAuthLog.setMediaCode("Y");
				} else {
					tAchAuthLog.setAuthStatus(" ");
				}
				tAchAuthLog.setRelationCode(iRelationCode);
				tAchAuthLog.setRelAcctName(iRelAcctName);
				tAchAuthLog.setRelationId(iRelationId);
				tAchAuthLog.setRelAcctBirthday(parse.stringToInteger(iRelAcctBirthday));
				tAchAuthLog.setRelAcctGender(iRelAcctGender);

				try {
					achAuthLogService.insert(tAchAuthLog);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "AchAuthLog insert " + e.getErrorMsg());
				}

				// 0 新增應處理明細
//				暫停新增不用寫入，終止寫入需寫
				if (flag == 1) {
					tTxToDoDetail.setItemCode("ACHP00");
					tTxToDoDetail.setCustNo(iCustNo);
					tTxToDoDetail.setFacmNo(iFacmNo);
					tTxToDoDetail.setDtlValue(" ");
					tTxToDoDetail.setStatus(0);
					txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // boolen: true:重複時跳過， 0.正常1.訂正
				}
			}
		}
	}

//  L4042按鈕進入之新增、修改、刪除皆於History新增，且Seq+1
//	刪除=寫入刪除日期
//	刪除回復=回復被刪除之LOG，刪除該筆History
	private void achLogHistoryInsert(TitaVo titaVo) throws LogicException {
		AchAuthLogHistory tAchAuthLogHistory = new AchAuthLogHistory();
		AchAuthLogHistoryId tAchAuthLogHistoryId = new AchAuthLogHistoryId();
		int achDetailSeq = 1;
		int stampFinshDate = 0;
		int propDate = 0;
		int retrDate = 0;
		int deleteDate = 0;
		int authCreateDate = 0;
		String amlRsp = "";
		String authStatus = "";
		String mediaCode = "";

		AchAuthLogHistory t1AchAuthLogHistory = achAuthLogHistoryService.facmNoCFirst(iCustNo, iFacmNo, titaVo);

		if (t1AchAuthLogHistory != null) {
			achDetailSeq = t1AchAuthLogHistory.getAchAuthLogHistoryId().getDetailSeq() + 1;
			stampFinshDate = t1AchAuthLogHistory.getStampFinishDate();
			propDate = t1AchAuthLogHistory.getPropDate();
			retrDate = t1AchAuthLogHistory.getRetrDate();
			amlRsp = t1AchAuthLogHistory.getAmlRsp();
			deleteDate = t1AchAuthLogHistory.getDeleteDate();
			authCreateDate = t1AchAuthLogHistory.getAuthCreateDate();
			authStatus = t1AchAuthLogHistory.getAuthStatus();
			mediaCode = t1AchAuthLogHistory.getMediaCode();
		}

		tAchAuthLogHistoryId.setCustNo(iCustNo);
		tAchAuthLogHistoryId.setFacmNo(iFacmNo);
		tAchAuthLogHistoryId.setDetailSeq(achDetailSeq);

		tAchAuthLogHistory.setAchAuthLogHistoryId(tAchAuthLogHistoryId);

		if (iFuncCode == 1) {
			tAchAuthLogHistory.setAuthCreateDate(dateUtil.getNowIntegerRoc());
		} else {
			if (authCreateDate > 0 && authCreateDate < 19110000) {
				authCreateDate = authCreateDate + 19110000;
			}
			if (stampFinshDate > 0 && stampFinshDate < 19110000) {
				stampFinshDate = stampFinshDate + 19110000;
			}
			if (propDate > 0 && propDate < 19110000) {
				propDate = propDate + 19110000;
			}
			if (retrDate > 0 && retrDate < 19110000) {
				retrDate = retrDate + 19110000;
			}
			amlRsp = t1AchAuthLogHistory.getAmlRsp();

			tAchAuthLogHistory.setAuthCreateDate(authCreateDate);
			tAchAuthLogHistory.setStampFinishDate(stampFinshDate);
			tAchAuthLogHistory.setPropDate(propDate);
			tAchAuthLogHistory.setRetrDate(retrDate);
			tAchAuthLogHistory.setAmlRsp(amlRsp);
		}
		tAchAuthLogHistory.setRepayBank(iRepayBank);
		tAchAuthLogHistory.setRepayAcct(iRepayAcct);
		tAchAuthLogHistory.setCreateFlag(iCreateFlag);

		tAchAuthLogHistory.setProcessDate(dateUtil.getNowIntegerForBC());
		tAchAuthLogHistory.setLimitAmt(iLimitAmt);
		tAchAuthLogHistory.setAuthMeth("A");

		if (iFuncCode == 4) {
//			刪除
			if (deleteDate == 0) {
				tAchAuthLogHistory.setDeleteDate(dateUtil.getNowIntegerForBC());
			}
//			刪除回復
//			刪除僅可謂送出/提回資料，最基本欄位input
			else {

				achLogHistoryDelete(authCreateDate, titaVo);

				AchAuthLogId achAuthLogId = new AchAuthLogId();
				AchAuthLog tAchAuthLog = new AchAuthLog();

				achAuthLogId.setAuthCreateDate(authCreateDate);
				achAuthLogId.setCustNo(iCustNo);
				achAuthLogId.setRepayBank(iRepayBank);
				achAuthLogId.setRepayAcct(iRepayAcct);
				achAuthLogId.setCreateFlag(iCreateFlag);

				tAchAuthLog.setAchAuthLogId(achAuthLogId);

				tAchAuthLog.setFacmNo(iFacmNo);
				tAchAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
				tAchAuthLog.setLimitAmt(iLimitAmt);
				tAchAuthLog.setAuthMeth("A");
				tAchAuthLog.setAuthStatus(authStatus);
				tAchAuthLog.setMediaCode(mediaCode);
				tAchAuthLog.setRelationCode(iRelationCode);
				tAchAuthLog.setRelAcctName(iRelAcctName);
				tAchAuthLog.setRelationId(iRelationId);
				tAchAuthLog.setRelAcctBirthday(parse.stringToInteger(iRelAcctBirthday));
				tAchAuthLog.setRelAcctGender(iRelAcctGender);

				try {
					achAuthLogService.insert(tAchAuthLog);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "AchAuthLog insert " + e.getErrorMsg());
				}

				return;
			}
		}

		switch (checkFlag) {
		case " ":
			tAchAuthLogHistory.setAuthStatus(" ");
			break;
		case "0":
			tAchAuthLogHistory.setAuthStatus("0");
			tAchAuthLogHistory.setMediaCode("Y");
			break;
		case "1":
			tAchAuthLogHistory.setAuthStatus("0");
			tAchAuthLogHistory.setMediaCode("Y");
			break;
		case "2":
			tAchAuthLogHistory.setAuthStatus("0");
			tAchAuthLogHistory.setMediaCode("Y");
			break;
		case "9":
			tAchAuthLogHistory.setAuthStatus(" ");
			tAchAuthLogHistory.setMediaCode("Y");
			break;
		default:
			tAchAuthLogHistory.setAuthStatus(" ");
			break;
		}
		tAchAuthLogHistory.setRelationCode(iRelationCode);
		tAchAuthLogHistory.setRelAcctName(iRelAcctName);
		tAchAuthLogHistory.setRelationId(iRelationId);
		tAchAuthLogHistory.setRelAcctBirthday(parse.stringToInteger(iRelAcctBirthday));
		tAchAuthLogHistory.setRelAcctGender(iRelAcctGender);

		try {
			achAuthLogHistoryService.insert(tAchAuthLogHistory, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "");
		}
	}

//	訂正刪除該筆History
	private void achLogHistoryDelete(int authCreateDate, TitaVo titaVo) throws LogicException {
		AchAuthLogHistory tAchAuthLogHistory = new AchAuthLogHistory();

		if (authCreateDate < 19110000) {
			authCreateDate += 19110000;
		}

		tAchAuthLogHistory = achAuthLogHistoryService.AuthLogKeyFirst(authCreateDate, iCustNo, iRepayBank, iRepayAcct, iCreateFlag, titaVo);

		if (tAchAuthLogHistory != null) {

			tAchAuthLogHistory = achAuthLogHistoryService.holdById(tAchAuthLogHistory, titaVo);

			try {
				achAuthLogHistoryService.delete(tAchAuthLogHistory, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "");
			}
		} else {
			throw new LogicException(titaVo, "E0001", "AchAuthLogHistory檔，無訂正資料");
		}
	}

	private void getAcctSeq() throws LogicException {
		this.info("getAcctSeq start...");
		int seq = 0;
		acctSeq = "";
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = bankAuthActComServiceImpl.getAcctSeq(iCustNo, iPostDepCode, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

//		同戶號、同帳號，計算筆數
		if (resultList != null && resultList.size() > 0) {
			seq = parse.stringToInteger(resultList.get(0).get("F1"));
		}

//		第一筆=空白，第二筆之後=01起編
		if (seq >= 1) {
			acctSeq = FormatUtil.pad9("" + seq, 2);
		}
	}

	private void checkFlag() {
//		initial
		checkFlag = " ";

		List<BankAuthAct> lBankAuthAct = new ArrayList<BankAuthAct>();

		Slice<BankAuthAct> sBankAuthAct = bankAuthActService.authCheck(iCustNo, iRepayAcct, 0, 999, this.index, this.limit, titaVo);

		lBankAuthAct = sBankAuthAct == null ? null : sBankAuthAct.getContent();

		if (lBankAuthAct != null && lBankAuthAct.size() != 0) {
			for (BankAuthAct tBankAuthAct : lBankAuthAct) {
				this.info("tBankAuthAct.getRepayBank() ... " + tBankAuthAct.getRepayBank());
				this.info("iRepayBank ... " + iRepayBank);
				this.info("tBankAuthAct.getStatus() ... " + tBankAuthAct.getStatus());

				if (iRepayBank.equals(tBankAuthAct.getRepayBank())) {
					checkFlag = tBankAuthAct.getStatus();
				}
			}
		}

		this.info("checkFlag ... " + checkFlag);
	}

//	L2154使用
	/**
	 * 舊額度新帳號更新使用(供L2154直接進入)
	 * 
	 * @param titaVo
	 * @throws LogicException
	 */
	public void changeAcctNo(TitaVo titaVo) throws LogicException {

//		if (!"2".equals(txType)) {
//			this.info("僅L2可進入 txType ... " + txType);
//			return;
//		}

		setVarValue(titaVo);

		showLog();

		if ("700".equals(iRepayBank)) {

			PostAuthLog t1PostAuthLog = postAuthLogService.repayAcctFirst(iCustNo, iPostDepCode, iRepayAcct, "1", titaVo);
			if (t1PostAuthLog != null) {
//				Post帳號變更紀錄				
				postLogHistoryInsert("1", titaVo);

				if (!"00".equals(t1PostAuthLog.getAuthErrorCode()) || "2".equals(t1PostAuthLog.getAuthApplCode())) {
					this.info("請輸入已授權成功之帳號 ... ");
//					if (iFuncCode != 4 && !"2".equals(txType)) {
//						throw new LogicException(titaVo, "E0001", "請輸入已授權成功之帳號");
//					}
				} else {
//					L2154 更換帳號 郵局換ACH 或ACH換郵局 需將舊的刪除，否則扣帳檔抓取會重複
					changeAcctNoDelete(titaVo);

					BankAuthAct tBankAuthAct = new BankAuthAct();
					BankAuthActId tBankAuthActId = new BankAuthActId();
					tBankAuthActId.setCustNo(iCustNo);
					tBankAuthActId.setFacmNo(iFacmNo);
					tBankAuthActId.setAuthType("01");

					tBankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);

					if (tBankAuthAct != null) {
						tBankAuthAct.setRepayBank(iRepayBank);
						tBankAuthAct.setRepayAcct(iRepayAcct);
						tBankAuthAct.setPostDepCode(iPostDepCode);
						if ("1".equals(t1PostAuthLog.getAuthApplCode())) {
							tBankAuthAct.setStatus("0");
						} else if ("9".equals(t1PostAuthLog.getAuthApplCode())) {
							tBankAuthAct.setStatus("1");
						}

						tBankAuthAct.setAcctSeq(t1PostAuthLog.getRepayAcctSeq());

						try {
							bankAuthActService.update(tBankAuthAct, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0008", e.getErrorMsg());
						}
					} else {
						BankAuthAct t2BankAuthAct = new BankAuthAct();
						BankAuthActId t2BankAuthActId = new BankAuthActId();
						t2BankAuthActId.setCustNo(iCustNo);
						t2BankAuthActId.setFacmNo(iFacmNo);
						t2BankAuthActId.setAuthType("01");
						t2BankAuthAct.setBankAuthActId(t2BankAuthActId);
						t2BankAuthAct.setPostDepCode(iPostDepCode);
						if ("1".equals(t1PostAuthLog.getAuthApplCode())) {
							t2BankAuthAct.setStatus("0");
						} else if ("9".equals(t1PostAuthLog.getAuthApplCode())) {
							t2BankAuthAct.setStatus("1");
						}
						t2BankAuthAct.setRepayBank(iRepayBank);
						t2BankAuthAct.setRepayAcct(iRepayAcct);
						t2BankAuthAct.setAcctSeq(t1PostAuthLog.getRepayAcctSeq());

						try {
							bankAuthActService.insert(t2BankAuthAct, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0008", e.getErrorMsg());
						}
					}
				}
			}

			PostAuthLog t2PostAuthLog = postAuthLogService.repayAcctFirst(iCustNo, iPostDepCode, iRepayAcct, "2", titaVo);

			if (t2PostAuthLog != null) {
//				Post帳號變更紀錄				
				postLogHistoryInsert("2", titaVo);

				if (!"00".equals(t2PostAuthLog.getAuthErrorCode()) || "2".equals(t1PostAuthLog.getAuthApplCode())) {
					this.info("請輸入已授權成功之帳號 ... ");
//					if ("4".equals(txType) && iFuncCode != 4) {
//						throw new LogicException(titaVo, "E0008", "請輸入已授權成功之帳號");
//					}
				} else {
//					L2154 更換帳號 郵局換ACH 或ACH換郵局 需將舊的刪除，否則扣帳檔抓取會重複
					changeAcctNoDelete(titaVo);

					BankAuthAct tBankAuthAct = new BankAuthAct();
					BankAuthActId tBankAuthActId = new BankAuthActId();
					tBankAuthActId.setCustNo(iCustNo);
					tBankAuthActId.setFacmNo(iFacmNo);
					tBankAuthActId.setAuthType("02");

					tBankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);

					if (tBankAuthAct != null) {
						tBankAuthAct.setRepayBank(iRepayBank);
						tBankAuthAct.setRepayAcct(iRepayAcct);
						tBankAuthAct.setPostDepCode(iPostDepCode);
						if ("1".equals(t1PostAuthLog.getAuthApplCode())) {
							tBankAuthAct.setStatus("0");
						} else if ("9".equals(t1PostAuthLog.getAuthApplCode())) {
							tBankAuthAct.setStatus("1");
						}
						tBankAuthAct.setAcctSeq(t2PostAuthLog.getRepayAcctSeq());

						try {
							bankAuthActService.update(tBankAuthAct, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0008", e.getErrorMsg());
						}
					} else {
						BankAuthAct t2BankAuthAct = new BankAuthAct();
						BankAuthActId t2BankAuthActId = new BankAuthActId();
						t2BankAuthActId.setCustNo(iCustNo);
						t2BankAuthActId.setFacmNo(iFacmNo);
						t2BankAuthActId.setAuthType("02");
						t2BankAuthAct.setBankAuthActId(t2BankAuthActId);
						t2BankAuthAct.setPostDepCode(iPostDepCode);
						if ("1".equals(t1PostAuthLog.getAuthApplCode())) {
							t2BankAuthAct.setStatus("0");
						} else if ("9".equals(t1PostAuthLog.getAuthApplCode())) {
							t2BankAuthAct.setStatus("1");
						}
						t2BankAuthAct.setRepayBank(iRepayBank);
						t2BankAuthAct.setRepayAcct(iRepayAcct);
						t2BankAuthAct.setAcctSeq(t2PostAuthLog.getRepayAcctSeq());

						try {
							bankAuthActService.insert(t2BankAuthAct, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0008", e.getErrorMsg());
						}
					}
				}
			}
		} else {
			AchAuthLog tAchAuthLog = achAuthLogService.repayAcctFirst(iCustNo, iRepayBank, iRepayAcct, titaVo);

			if (tAchAuthLog != null) {
//				ACH帳號變更紀錄				
				achLogHistoryInsert(titaVo);

				if (!"0".equals(tAchAuthLog.getAuthStatus()) || "Z".equals(tAchAuthLog.getCreateFlag())) {
					this.info("請輸入已授權成功之帳號 ... ");
//					if ("4".equals(txType) && iFuncCode != 4) {
//						throw new LogicException(titaVo, "E0001", "請輸入已授權成功之帳號");
//					}
				} else {
//					L2154 更換帳號 郵局換ACH 或ACH換郵局 需將舊的刪除，否則扣帳檔抓取會重複
					changeAcctNoDelete(titaVo);

					BankAuthAct tBankAuthAct = new BankAuthAct();
					BankAuthActId tBankAuthActId = new BankAuthActId();
					tBankAuthActId.setCustNo(iCustNo);
					tBankAuthActId.setFacmNo(iFacmNo);
					tBankAuthActId.setAuthType("00");

					tBankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);

					if (tBankAuthAct != null) {
						tBankAuthAct.setRepayBank(iRepayBank);
						tBankAuthAct.setRepayAcct(iRepayAcct);
						if ("A".equals(tAchAuthLog.getCreateFlag())) {
							tBankAuthAct.setStatus("0");
						} else if ("D".equals(tAchAuthLog.getCreateFlag())) {
							tBankAuthAct.setStatus("1");
						}

						try {
							bankAuthActService.update(tBankAuthAct, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0008", e.getErrorMsg());
						}
					} else {
						BankAuthAct t2BankAuthAct = new BankAuthAct();
						BankAuthActId t2BankAuthActId = new BankAuthActId();
						t2BankAuthActId.setCustNo(iCustNo);
						t2BankAuthActId.setFacmNo(iFacmNo);
						t2BankAuthActId.setAuthType("00");
						t2BankAuthAct.setBankAuthActId(t2BankAuthActId);
						t2BankAuthAct.setRepayBank(iRepayBank);
						if ("A".equals(tAchAuthLog.getCreateFlag())) {
							t2BankAuthAct.setStatus("0");
						} else if ("D".equals(tAchAuthLog.getCreateFlag())) {
							t2BankAuthAct.setStatus("1");
						}
						t2BankAuthAct.setRepayAcct(iRepayAcct);

						try {
							bankAuthActService.insert(t2BankAuthAct, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0008", e.getErrorMsg());
						}
					}
				}
			} else {
				this.info("查無資料");
			}
		}
	}

//	L2154 更換帳號 郵局換ACH 或ACH換郵局 需將舊的刪除，否則扣帳檔抓取會重複
	private void changeAcctNoDelete(TitaVo titaVo) throws LogicException {
		this.info("changeAcctNoDelete Start...");

		if ("2".equals(txType)) {
			Slice<BankAuthAct> sBankAuthAct = bankAuthActService.facmNoEq(iCustNo, iFacmNo, index, limit, titaVo);

			List<BankAuthAct> lBankAuthAct = new ArrayList<BankAuthAct>();

			lBankAuthAct = sBankAuthAct == null ? null : sBankAuthAct.getContent();

			if (lBankAuthAct != null && lBankAuthAct.size() != 0) {
				for (BankAuthAct tBankAuthAct : lBankAuthAct) {
					this.info("tBankAuthAct ... " + tBankAuthAct.toString());
					if ("700".equals(tBankAuthAct.getRepayBank())) {
						if ("700".equals(iRepayBank)) {
							this.info("同為郵局不刪除 ... ");
							continue;
						}
					} else {
						if (!"700".equals(iRepayBank)) {
							this.info("同為ACH不刪除 ... ");
							continue;
						}
					}
					bankAcctDelete(tBankAuthAct.getBankAuthActId());
				}
			} else {
				this.info("lBankAuthAct null...");
			}
		}
	}
}
