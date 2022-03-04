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
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogHistory;
import com.st1.itx.db.domain.AchAuthLogId;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.BankAuthActId;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.PostAuthLogId;
import com.st1.itx.db.domain.RepayActChangeLog;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AchAuthLogHistoryService;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.PostAuthLogHistoryService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.db.service.RepayActChangeLogService;
import com.st1.itx.db.service.springjpa.cm.BankAuthActComServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * LOG、帳號檔維護 <BR>
 * 1.acctCheck <BR>
 * 1.1 L2153 僅新增：放行新增；訂正刪除 <BR>
 * 1.4 L4410、L4412 新增：若該額度已有授權成功帳號，僅寫入Log檔，待回傳檔L4414上傳更新帳號檔 <BR>
 * 1.5 L4410、L4412 維護：若為未授權可維護Key值以外欄位，否則僅能變更暫停或暫停回復記號 <BR>
 * 1.6 L4410、L4412 刪除：僅刪除Log檔 <BR>
 * 2 changeAcctNo <BR>
 * 1.2 L2154 維護：前端控制為原帳號或已授權之帳號，僅維護帳號檔 <BR>
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
	public RepayActChangeLogService repayActChangeLogService;;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public BankAuthActComServiceImpl bankAuthActComServiceImpl;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public TxAmlCom txAmlCom;

	@Autowired
	DataLog datalog;

	private String iPostDepCode = " ";
	private int iRepayCode = 0;
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
	private String iTitaTxCd = "";
	private String txType = "";
	private boolean isNewAct = false;
	private boolean isNewLog = false;
	private boolean isDelAct = false;
	private boolean isDelLog = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		return null;
	}

	/**
	 * 新增授權資料
	 * 
	 * @param createFlag A.新增授權, D.取消授權
	 * @param titaVo     ..
	 * @throws LogicException
	 */
	public void add(String createFlag, TitaVo titaVo) throws LogicException {
		this.info("bankAuthActCom add ...");
		setVarValue(titaVo);
		showLog();
		if ("A".equals(createFlag)) {
			if (titaVo.isHcodeNormal()) {
				addAuth(titaVo);
			}
			if (titaVo.isHcodeErase()) {
				addAuthDelete(titaVo);
			}
		}

		if ("D".equals(createFlag)) {
			if (titaVo.isHcodeNormal()) {
				addCancel(titaVo);
			}
			if (titaVo.isHcodeErase()) {
				addCancelDelete(titaVo);
			}
		}

		// 新增授權應處理明細
		if (this.isNewLog || this.isDelLog) {
			txToDoCom.setTxBuffer(this.txBuffer);
			TxToDoDetail tTxToDoDetail = new TxToDoDetail();
			if ("700".equals(iRepayBank)) {
				tTxToDoDetail.setItemCode("POSP00");
			} else {
				tTxToDoDetail.setItemCode("ACHP00");
			}
			tTxToDoDetail.setCustNo(iCustNo);
			tTxToDoDetail.setFacmNo(iFacmNo);
			tTxToDoDetail.setDtlValue(FormatUtil.pad9(iRepayAcct, 14));
			tTxToDoDetail.setStatus(0);
			txToDoCom.addDetail(true, this.isNewLog ? 0 : 1, tTxToDoDetail, titaVo);
		}
	}

	public void addRepayActChangeLog(TitaVo titaVo) throws LogicException {
		this.info("bankAuthActCom add ...");
		setVarValue(titaVo);
		showLog();

		// 還款方式為非銀扣時新增還款帳號變更(含還款方式)紀錄檔
		if (titaVo.isHcodeNormal()) {
			addRepayActChangeLog("", titaVo);
		}
		if (titaVo.isHcodeErase()) {
			addRepayActChangeLogDelete(titaVo);
		}

	}

	/**
	 * 刪除授權資料
	 * 
	 * @param createFlag A.新增授權, D.取消授權
	 * @param titaVo     ..
	 * @throws LogicException
	 */
	public void del(String createFlag, TitaVo titaVo) throws LogicException {
		this.info("bankAuthActCom del Start...");
		setVarValue(titaVo);
		showLog();

		if ("A".equals(createFlag)) {
			if (titaVo.isHcodeNormal()) {
				addAuthDelete(titaVo);
			}
			if (titaVo.isHcodeErase()) {
				addAuth(titaVo);
			}
		}
		if ("D".equals(createFlag)) {
			if (titaVo.isHcodeNormal()) {
				addCancelDelete(titaVo);
			}
			if (titaVo.isHcodeErase()) {
				addCancel(titaVo);
			}
		}

		// 新增授權應處理明細
		txToDoCom.setTxBuffer(this.txBuffer);
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		if ("700".equals(iRepayBank)) {
			tTxToDoDetail.setItemCode("POSP00");
		} else {
			tTxToDoDetail.setItemCode("ACHP00");
		}
		if (this.isNewLog || this.isDelLog) {
			tTxToDoDetail.setCustNo(iCustNo);
			tTxToDoDetail.setFacmNo(iFacmNo);
			tTxToDoDetail.setDtlValue(FormatUtil.pad9(iRepayAcct, 14));
			tTxToDoDetail.setStatus(0);
			txToDoCom.addDetail(true, this.isNewLog ? 0 : 1, tTxToDoDetail, titaVo);
		}
	}

	// 郵局授權提回更新帳號檔
	public void updPostAcct(PostAuthLog t, TitaVo titaVo) throws LogicException {
		this.info("bankAuthActCom updPostAcct ...");
		BankAuthAct tBankAuthAct = null;
		if ("1".equals(t.getAuthApplCode()) || "2".equals(t.getAuthApplCode())) {
			tBankAuthAct = bankAuthActService
					.findById(new BankAuthActId(t.getCustNo(), t.getFacmNo(), "0" + t.getAuthCode()), titaVo);
			if (tBankAuthAct == null) {
				throw new LogicException("E0015", "此筆授權 帳號檔找不到" + t.getCustNo() + t.getFacmNo() + t.getAuthCode()); // 檢查錯誤
			}
		}
		String status = " ";
		boolean isUpdFac = false; // 本額度更新
		boolean isUpdAct = false; // 本帳號更新
		switch (t.getAuthApplCode()) {
		case "1": // 1.申請
			if ("00".equals(t.getAuthErrorCode())) { // 00:成功
				status = "0"; // 0:授權成功
				updFacmRepayCode(t.getCustNo(), t.getFacmNo(), titaVo);
				isUpdFac = true;
			} else {
				status = "8"; // 8.授權失敗
				if (t.getRepayAcct().equals(tBankAuthAct.getRepayAcct())
						&& t.getPostDepCode().equals(tBankAuthAct.getPostDepCode())) {
					isUpdFac = true;
				}
			}
			isUpdAct = true;
			break;
		case "2": // 2.終止
			if ("00".equals(t.getAuthErrorCode())) { // 00:成功
				status = "2"; // 2.取消授權
				isUpdFac = true;
				isUpdAct = true;
			}
			break;
		case "3": // 3.郵局終止
			status = "1"; // 1:停止使用
			isUpdAct = true;
			break;
		case "4": // 4.誤終止
			status = "0"; // 0:授權成功
			isUpdAct = true;
			break;
		default:
			break;
		}

		if (isUpdFac) {
			tBankAuthAct = bankAuthActService.holdById(tBankAuthAct, titaVo);
			tBankAuthAct.setRepayBank("700");
			tBankAuthAct.setRepayAcct(t.getRepayAcct());
			tBankAuthAct.setPostDepCode(t.getPostDepCode());
			tBankAuthAct.setLimitAmt(BigDecimal.ZERO);
			tBankAuthAct.setAcctSeq(t.getRepayAcctSeq());
			tBankAuthAct.setStatus(status);
			try {
				bankAuthActService.update(tBankAuthAct, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "BankAuthAct" + e.getErrorMsg());
			}
		}

		if (isUpdAct) {
			Slice<BankAuthAct> slBankAuthAct = bankAuthActService.findAcctNo(t.getCustNo(), t.getRepayAcct(), "700", 0,
					999, 0, Integer.MAX_VALUE, titaVo);
			if (slBankAuthAct != null) {
				for (BankAuthAct t1 : slBankAuthAct.getContent()) {
					if (t1.getPostDepCode().equals(t.getPostDepCode())) {
						tBankAuthAct = bankAuthActService.holdById(t1, titaVo);
						tBankAuthAct.setStatus(status);
						try {
							bankAuthActService.update(tBankAuthAct, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0008", "BankAuthAct" + e.getErrorMsg());
						}

						insertRepayActChangeLog(tBankAuthAct, titaVo); // 新增還款帳號變更(含還款方式)紀錄檔

						if ("0".equals(status)) {
							updPostAcctDelAch(tBankAuthAct, titaVo); // 郵局授權成功刪除ACH舊帳號檔
						}
					}
				}
			}
		}
		// 新增郵局授權記錄歷史檔
		insertPostHistory(t, t.getAuthApplCode(), titaVo);

	}

	// ACH授權提回更新帳號檔
	public void updAchAcct(AchAuthLog t, TitaVo titaVo) throws LogicException {
		this.info("bankAuthActCom updAchAcct ...");
		BankAuthAct tBankAuthAct = null;
		tBankAuthAct = bankAuthActService.findById(new BankAuthActId(t.getCustNo(), t.getFacmNo(), "00"), titaVo);
		if (tBankAuthAct == null) {
			throw new LogicException("E0015", "此筆授權 帳號檔找不到" + t.getCustNo() + t.getFacmNo()); // 檢查錯誤
		}
		String status = " ";
		boolean isUpdFac = false; // 本額度更新
		boolean isUpdAct = false; // 本帳號更新
		switch (t.getCreateFlag()) {
		case "A":
			if ("0".equals(t.getAuthStatus())) { // 0:成功
				status = "0"; // 0:授權成功
				isUpdFac = true;
				updFacmRepayCode(t.getCustNo(), t.getFacmNo(), titaVo);
			} else {
				status = "8"; // 8.授權失敗
				if (t.getRepayAcct().equals(tBankAuthAct.getRepayAcct())) {
					isUpdFac = true;
				}
			}
			isUpdAct = true;
			break;
		case "2": // 2.終止
			if ("0".equals(t.getAuthStatus())) { // 0:成功
				status = "2"; // 2.取消授權
				isUpdFac = true;
				isUpdAct = true;
			}
			break;
		default:
			break;
		}

		if (isUpdFac) {
			tBankAuthAct = bankAuthActService.holdById(tBankAuthAct, titaVo);
			tBankAuthAct.setRepayBank(t.getRepayBank());
			tBankAuthAct.setRepayAcct(t.getRepayAcct());
			tBankAuthAct.setPostDepCode(" ");
			tBankAuthAct.setLimitAmt(t.getLimitAmt());
			tBankAuthAct.setAcctSeq("  ");
			tBankAuthAct.setStatus(status);
			try {
				bankAuthActService.update(tBankAuthAct, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "BankAuthAct" + e.getErrorMsg());
			}
		}

		if (isUpdAct) {
			Slice<BankAuthAct> slBankAuthAct = bankAuthActService.findAcctNo(t.getCustNo(), t.getRepayAcct(),
					t.getRepayBank(), 0, 999, 0, Integer.MAX_VALUE, titaVo);
			if (slBankAuthAct != null) {
				for (BankAuthAct t1 : slBankAuthAct.getContent()) {
					tBankAuthAct = bankAuthActService.holdById(t1, titaVo);
					tBankAuthAct.setStatus(status);
					try {
						bankAuthActService.update(tBankAuthAct, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", "BankAuthAct" + e.getErrorMsg());
					}

					insertRepayActChangeLog(tBankAuthAct, titaVo); // 新增還款帳號變更(含還款方式)紀錄檔

					if ("0".equals(status)) {
						updAchAcctDelPost(tBankAuthAct, titaVo); // ACH授權成功刪除舊郵局帳號檔
					}

				}
			}
		}

		// 新增ACH授權記錄歷史檔
		insertAchHistory(t, t.getCreateFlag(), titaVo);
	}

	// ACH授權成功刪除舊郵局帳號檔
	public void updAchAcctDelPost(BankAuthAct t, TitaVo titaVo) throws LogicException {
		this.info("bankAuthActCom updAchAcct ...");
		BankAuthAct tBankAuthAct = null;
		tBankAuthAct = bankAuthActService.holdById(new BankAuthActId(t.getCustNo(), t.getFacmNo(), "01"), titaVo);
		if (tBankAuthAct != null) {
			try {
				bankAuthActService.delete(tBankAuthAct, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0006", e.getErrorMsg());
			}
		}
		tBankAuthAct = bankAuthActService.holdById(new BankAuthActId(t.getCustNo(), t.getFacmNo(), "02"), titaVo);
		if (tBankAuthAct != null) {
			try {
				bankAuthActService.delete(tBankAuthAct, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0006", e.getErrorMsg());
			}
		}
	}

	// 郵局授權成功刪除ACH舊帳號檔
	public void updPostAcctDelAch(BankAuthAct t, TitaVo titaVo) throws LogicException {
		this.info("bankAuthActCom updAchAcct ...");
		BankAuthAct tBankAuthAct = null;
		tBankAuthAct = bankAuthActService.holdById(new BankAuthActId(t.getCustNo(), t.getFacmNo(), "00"), titaVo);
		if (tBankAuthAct != null) {
			try {
				bankAuthActService.delete(tBankAuthAct, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0006", e.getErrorMsg());
			}
		}
	}

	private void updFacmRepayCode(int custNo, int facmNo, TitaVo titaVo) throws LogicException {
		this.info("bankAuthActCom updAchAcct ...");
		FacMain tFacMain = facMainService.holdById(new FacMainId(custNo, facmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException("E0015", "額度檔找不到" + custNo + "-" + facmNo); // 檢查錯誤
		}
		if (tFacMain.getRepayCode() != 2) {
			FacMain beforeFacMain = (FacMain) datalog.clone(tFacMain);
			tFacMain.setRepayCode(2);
			try {
				tFacMain = facMainService.update2(tFacMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "額度主檔" + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			datalog.setEnv(titaVo, beforeFacMain, tFacMain);
			datalog.exec();
		}
	}

	/**
	 * 維護郵局授權檔
	 * 
	 * @param iStatus 1:停止使用 0:授權成功(恢復授權)
	 * @param titaVo  ..
	 * @throws LogicException
	 */
	public void mntPostAuth(String iStatus, TitaVo titaVo) throws LogicException {
		this.info("bankAuthActCom mntPostAuth ...");
		setVarValue(titaVo);
		showLog();
		this.info("iStatus = " + iStatus);

		PostAuthLogId tPostAuthLogId = new PostAuthLogId();
		tPostAuthLogId.setAuthCreateDate(parse.stringToInteger(titaVo.getParam("AuthCreateDate")));
		tPostAuthLogId.setAuthApplCode("1");
		tPostAuthLogId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tPostAuthLogId.setPostDepCode(titaVo.getParam("PostDepCode"));
		tPostAuthLogId.setRepayAcct(titaVo.getParam("RepayAcct"));
		tPostAuthLogId.setAuthCode(titaVo.getParam("AuthCode"));
		PostAuthLog tPostAuthLog = postAuthLogService.holdById(tPostAuthLogId, titaVo);
		if (tPostAuthLog == null) {
			throw new LogicException("E0015", "此筆授權資料檔找不到"); // 檢查錯誤
		}
		if (!"00".equals(tPostAuthLog.getAuthErrorCode())) {
			throw new LogicException("E0015", "需授權成功才可變動" + tPostAuthLog.getAuthErrorCode()); // 檢查錯誤
		}
		if ("1".equals(iStatus)) {
			tPostAuthLog.setDeleteDate(dateUtil.getNowIntegerRoc());
		} else {
			tPostAuthLog.setDeleteDate(0);
			tPostAuthLog.setCustId(titaVo.getParam("CustId"));
		}
		try {
			postAuthLogService.update(tPostAuthLog, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", e.getErrorMsg());
		}

		// 新增郵局授權記錄歷史檔
		insertPostHistory(tPostAuthLog, "1".equals(iStatus) ? "9" : "8", titaVo);

		// 更新授權帳號檔
		Slice<BankAuthAct> slBankAuthAct = bankAuthActService.findAcctNo(iCustNo, iRepayAcct, iRepayBank, 0, 999, 0,
				Integer.MAX_VALUE, titaVo);
		if (slBankAuthAct != null) {
			for (BankAuthAct t : slBankAuthAct.getContent()) {
				// 有有效額度使用則不允許暫停
				if ("1".equals(iStatus)) {
					FacMain tFacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
					if (tFacMain != null && tFacMain.getRepayCode() == 2
							&& tFacMain.getUtilAmt().compareTo(BigDecimal.ZERO) > 0) {
						throw new LogicException(titaVo, "E0015", "額度(" + t.getFacmNo() + ")使用中，不允許暫停"); // 檢查錯誤
					}
				}
				// 更新授權帳號檔
				BankAuthAct tBankAuthAct = bankAuthActService.holdById(t, titaVo);
				tBankAuthAct.setStatus(iStatus);
				try {
					bankAuthActService.update(tBankAuthAct, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", "BankAuthAct" + e.getErrorMsg());
				}
				insertRepayActChangeLog(tBankAuthAct, titaVo); // 新增還款帳號變更(含還款方式)紀錄檔

			}
		}
	}

	/**
	 * 維護ACH授權檔
	 * 
	 * @param iStatus 1:停止使用 0:授權成功
	 * @param titaVo  ..
	 * @throws LogicException
	 */
	public void mntAchAuth(String iStatus, TitaVo titaVo) throws LogicException {
		this.info("bankAuthActCom mntAchAuth ...");
		setVarValue(titaVo);
		showLog();
		this.info("iStatus = " + iStatus);

		AchAuthLogId tAchAuthLogId = new AchAuthLogId();
		tAchAuthLogId.setAuthCreateDate(parse.stringToInteger(titaVo.getParam("AuthCreateDate")));
		tAchAuthLogId.setCreateFlag("A");
		tAchAuthLogId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tAchAuthLogId.setRepayBank(titaVo.getParam("RepayBank"));
		tAchAuthLogId.setRepayAcct(titaVo.getParam("RepayAcct"));
		AchAuthLog tAchAuthLog = achAuthLogService.holdById(tAchAuthLogId, titaVo);
		if (tAchAuthLog == null) {
			throw new LogicException("E0015", "此筆授權資料檔找不到"); // 檢查錯誤
		}
		if (!"0".equals(tAchAuthLog.getAuthStatus())) {
			throw new LogicException("E0015", "需授權成功才可變動" + tAchAuthLog.getAuthStatus()); // 檢查錯誤
		}
		if ("1".equals(iStatus)) {
			tAchAuthLog.setDeleteDate(dateUtil.getNowIntegerRoc());
		} else {
			tAchAuthLog.setDeleteDate(0);
		}
		try {
			achAuthLogService.update(tAchAuthLog, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", e.getErrorMsg());
		}

		// 新增ACH授權記錄歷史檔
		insertAchHistory(tAchAuthLog, "0".equals(iStatus) ? "Y" : "Z", titaVo);

		// 更新授權帳號檔
		Slice<BankAuthAct> slBankAuthAct = bankAuthActService.findAcctNo(iCustNo, iRepayAcct, iRepayBank, 0, 999, 0,
				Integer.MAX_VALUE, titaVo);
		if (slBankAuthAct != null) {
			for (BankAuthAct t : slBankAuthAct.getContent()) {
				// 有有效額度使用則不允許暫停
				if ("1".equals(iStatus)) {
					FacMain tFacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
					if (tFacMain != null && tFacMain.getRepayCode() == 2
							&& tFacMain.getUtilAmt().compareTo(BigDecimal.ZERO) > 0) {
						throw new LogicException(titaVo, "E0015", "額度(" + t.getFacmNo() + ")使用中，不允許暫停"); // 檢查錯誤
					}
				}
				// 更新授權帳號檔
				BankAuthAct tBankAuthAct = bankAuthActService.holdById(t, titaVo);
				tBankAuthAct.setStatus(iStatus);
				try {
					bankAuthActService.update(tBankAuthAct, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", "BankAuthAct" + e.getErrorMsg());
				}
				insertRepayActChangeLog(tBankAuthAct, titaVo); // 新增還款帳號變更(含還款方式)紀錄檔

			}
		}
	}

	// 新增授權帳號
	private void addAuth(TitaVo titaVo) throws LogicException {
		// 新增帳號檔，同戶無該扣款帳號則新增授權檔
		this.info("addAuth ...");
		this.isNewLog = false;
		this.isNewAct = false;
		String status = " ";
		// 該額度扣款帳號
		BankAuthAct tBankAuthAct = null;
		Slice<BankAuthAct> slBankAuthAct = bankAuthActService.facmNoEq(iCustNo, iFacmNo, 0, Integer.MAX_VALUE, titaVo);
		if (slBankAuthAct == null) {
			this.isNewAct = true;
		} else {
			tBankAuthAct = slBankAuthAct.getContent().get(0);
			if ("".equals(tBankAuthAct.getStatus().trim())) {
				throw new LogicException("E0015", "該額度扣款帳號尚未提出授權 " + tBankAuthAct.getRepayAcct());
			}
		}
		// 同戶扣款帳號
		tBankAuthAct = getRepayAcct(titaVo);
		String acctSeq = "  ";
		if (tBankAuthAct == null) {
			if ("700".equals(iRepayBank)) {
				PostAuthLog tPostAuthLog = postAuthLogService.repayAcctFirst(iCustNo, iPostDepCode, iRepayAcct, "1",
						titaVo);
				if (tPostAuthLog == null) {
					this.isNewLog = true;
					acctSeq = getNewAcctSeq(titaVo); // 新帳號碼
				} else {
					if ("1".equals(tPostAuthLog.getAuthApplCode()) && "00".equals(tPostAuthLog.getAuthErrorCode())) {
						status = "0";
						acctSeq = tPostAuthLog.getRepayAcctSeq();
					} else {
						this.isNewLog = true;
					}
				}
			} else {
				AchAuthLog tAchAuthLog = achAuthLogService.repayAcctFirst(iCustNo, iRepayBank, iRepayAcct, titaVo);
				if (tAchAuthLog == null) {
					this.isNewLog = true;
				} else {
					if ("A".equals(tAchAuthLog.getCreateFlag()) && "0".equals(tAchAuthLog.getAuthStatus())) {
						status = "0";
					} else {
						this.isNewLog = true;
					}
				}
			}
		} else {
			acctSeq = tBankAuthAct.getAcctSeq();
			status = tBankAuthAct.getStatus();
		}
		switch (status) {
		case " ": // ''未授權
			break;
		case "0": // 0:授權成功
			this.isNewLog = false;
			break;
		case "2": // 2.取消授權
		case "8": // 8.授權失敗
			this.isNewLog = true;
			break;
		case "1": // 1:停止使用
			throw new LogicException("E0015", "此扣款帳號停止使用，請恢復使用"); // 檢查錯誤
		case "9": // 9:已送出授權
			throw new LogicException("E0015", "此扣款帳號已送出授權");
		default:
			throw new LogicException("E0015", "此扣款帳號狀態碼錯誤");
		}

		addRepayActChangeLog(status, titaVo);// 新增還款帳號變更(含還款方式)紀錄檔
		if ("700".equals(iRepayBank)) {
			if (this.isNewAct) {
				tBankAuthAct = inserBankAuthAct("01", acctSeq, status, titaVo);
			}
			if (this.isNewLog) {
				insertPostLog("1", "1", acctSeq, titaVo);
			}
			if (this.isNewAct) {
				tBankAuthAct = inserBankAuthAct("02", acctSeq, status, titaVo);
			}
			if (this.isNewLog) {
				insertPostLog("1", "2", acctSeq, titaVo);
			}
		} else {
			if (this.isNewAct) {
				tBankAuthAct = inserBankAuthAct("00", acctSeq, status, titaVo);
			}
			if (this.isNewLog) {
				insertAchLog("A", titaVo);
			}
		}
	}

	// 新增授權帳號刪除
	private void addAuthDelete(TitaVo titaVo) throws LogicException {
		// 刪除帳號檔，同戶無該扣款帳號則刪除授權檔，若有但未提出則將授權檔的額度改為最小序號
		this.info("addAuthDelete ...");
		// 同戶扣款帳號
		BankAuthAct tBankAuthAct = getRepayAcct(titaVo);
		if (tBankAuthAct != null && tBankAuthAct.getFacmNo() == iFacmNo) {
			switch (tBankAuthAct.getStatus()) {
			case " ": // ''未授權
			case "0": // 0:授權成功
			case "1": // 1:停止使用
			case "8": // 8.授權失敗
				this.isDelAct = true;
				break;
			case "2": // 2.取消授權
				this.isDelAct = false;
				break;
			case "9": // 9:已送出授權
				throw new LogicException("E0015", "此額度扣款帳號已送出授權");
			default:
				throw new LogicException("E0015", "此額度扣款帳號狀態碼錯誤");
			}
		}
		String status = " ";
		if (tBankAuthAct != null) {
			status = tBankAuthAct.getStatus();
		}
		if (this.isDelAct) {
			BankAuthActId tBankAuthActId = new BankAuthActId();
			tBankAuthActId.setCustNo(iCustNo);
			tBankAuthActId.setFacmNo(iFacmNo);
			if ("700".equals(iRepayBank)) {
				tBankAuthActId.setAuthType("01");
				deleteBankAuthAct(tBankAuthActId, titaVo);
				tBankAuthActId.setAuthType("02");
				deleteBankAuthAct(tBankAuthActId, titaVo);
			} else {
				tBankAuthActId.setAuthType("00");
				deleteBankAuthAct(tBankAuthActId, titaVo);
			}
		}
		// 同戶扣款帳號

		tBankAuthAct = getRepayAcct(titaVo);

		this.info("tBankAuthAct = " + tBankAuthAct);
		// 同戶無該扣款帳號、未授權
		this.info("status = " + status);
		if (tBankAuthAct == null && "".equals(status.trim())) {
			this.isDelLog = true;
			if ("700".equals(iRepayBank)) {
				deletePostAuthLog("1", "1", titaVo);
				deletePostAuthLog("1", "2", titaVo);
			} else {
				deleteAchAuthLog("A", titaVo);
			}
		}
		this.info("isDelLog = " + isDelLog);

		addRepayActChangeLogDelete(titaVo);// 刪除還款帳號變更(含還款方式)紀錄檔

	}

	// 取消授權帳號
	private void addCancel(TitaVo titaVo) throws LogicException {
		// 更新帳號檔狀態
		this.info("addCancel ...");
		String acctSeq = "";
		Slice<BankAuthAct> slBankAuthAct = bankAuthActService.findAcctNo(iCustNo, iRepayAcct, iRepayBank, 0, 999, 0,
				Integer.MAX_VALUE, titaVo);
		if (slBankAuthAct == null) {
			throw new LogicException("E0015", "此筆授權資料不存在"); // 檢查錯誤
		}

		for (BankAuthAct t : slBankAuthAct.getContent()) {
			acctSeq = t.getAcctSeq();
			// 有有效額度使用則不允許刪除
			FacMain tFacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
			if (tFacMain != null && tFacMain.getRepayCode() == 2
					&& tFacMain.getUtilAmt().compareTo(BigDecimal.ZERO) > 0) {
				throw new LogicException(titaVo, "E0015", "額度(" + t.getFacmNo() + ")使用中，不允許刪除"); // 檢查錯誤
			}

		}
		this.isNewLog = true;
		if ("700".equals(iRepayBank)) {
			if (this.isNewLog) {
				insertPostLog("2", "1", acctSeq, titaVo);
			}
			if (this.isNewLog) {
				insertPostLog("2", "2", acctSeq, titaVo);
			}
		} else {
			if (this.isNewLog) {
				insertAchLog("D", titaVo);
			}
		}
	}

	// 取消授權帳號刪除
	private void addCancelDelete(TitaVo titaVo) throws LogicException {
		this.info("addCancelDelete ...");

		this.isDelLog = true;
		if ("700".equals(iRepayBank)) {
			deletePostAuthLog("2", "1", titaVo);
			deletePostAuthLog("2", "2", titaVo);
		} else {
			deleteAchAuthLog("D", titaVo);
		}
	}

	// 新增授權記錄檔
	private void insertPostLog(String authApplCode, String authCode, String acctSeq, TitaVo titaVo)
			throws LogicException {
		this.info("insertPostLog ....");
		boolean isInsert = true;
		PostAuthLog tPostAuthLog = postAuthLogService.repayAcctFirst(iCustNo, iPostDepCode, iRepayAcct, authCode,
				titaVo);
		if (tPostAuthLog != null) {
			if (authApplCode.equals(tPostAuthLog.getAuthApplCode())) {
				if ("".equals(tPostAuthLog.getAuthErrorCode().trim())) {
					if ("1".equals(authApplCode)) {
						throw new LogicException("E0015", "此扣款帳號已有授權資料");
					} else {
						throw new LogicException("E0015", "此扣款帳號已有取消授權資料");

					}
				}
			}
			isInsert = false;
		}

		if (isInsert) {
			PostAuthLogId tPostAuthLogId = new PostAuthLogId();
			tPostAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
			tPostAuthLogId.setAuthApplCode(authApplCode);
			tPostAuthLogId.setCustNo(iCustNo);
			tPostAuthLogId.setPostDepCode(iPostDepCode);
			tPostAuthLogId.setRepayAcct(iRepayAcct);
			tPostAuthLogId.setAuthCode(authCode);
			tPostAuthLog = new PostAuthLog();
			tPostAuthLog.setPostAuthLogId(tPostAuthLogId);
		}
		tPostAuthLog.setFacmNo(iFacmNo);
		tPostAuthLog.setCustId(iCustId);
		tPostAuthLog.setRepayAcctSeq(acctSeq);
		tPostAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
//		tPostAuthLog.setProcessTime(Integer.parseInt(titaVo.getParam("SysTime")));
		tPostAuthLog.setAuthErrorCode(" ");
		tPostAuthLog.setRelationCode(iRelationCode);
		tPostAuthLog.setRelAcctName(iRelAcctName);
		tPostAuthLog.setRelationId(iRelationId);
		tPostAuthLog.setRelAcctBirthday(parse.stringToInteger(iRelAcctBirthday));
		tPostAuthLog.setRelAcctGender(iRelAcctGender);
		tPostAuthLog.setTitaTxCd(iTitaTxCd);
		try {
			postAuthLogService.insert(tPostAuthLog, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "PostAuthLog insert " + e.getErrorMsg());
		}
	}

	// 新增授權記錄檔
	private void insertAchLog(String createFlag, TitaVo titaVo) throws LogicException {
		this.info("insertAchLog ....");
		AchAuthLog tAchAuthLog = achAuthLogService.repayAcctFirst(iCustNo, iRepayBank, iRepayAcct, titaVo);
		if (tAchAuthLog != null) {
			if (tAchAuthLog.getCreateFlag().equals(createFlag)) {
				if ("".equals(tAchAuthLog.getAuthStatus().trim())) {
					if ("A".equals(createFlag)) {
						throw new LogicException("E0015", "此扣款帳號已有授權資料");
					} else {
						throw new LogicException("E0015", "此扣款帳號已有取消授權資料");
					}
				}
			}
		}
		AchAuthLogId tAchAuthLogId = new AchAuthLogId();
		tAchAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerRoc());
		tAchAuthLogId.setCustNo(iCustNo);
		tAchAuthLogId.setRepayBank(iRepayBank);
		tAchAuthLogId.setRepayAcct(iRepayAcct);
		tAchAuthLogId.setCreateFlag(createFlag);
		tAchAuthLog = new AchAuthLog();
		tAchAuthLog.setAchAuthLogId(tAchAuthLogId);
		tAchAuthLog.setFacmNo(iFacmNo);
		tAchAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
//		tAchAuthLog.setProcessTime(Integer.parseInt(titaVo.getParam("SysTime")));
		tAchAuthLog.setLimitAmt(iLimitAmt);
		tAchAuthLog.setAuthMeth("A");
		tAchAuthLog.setAuthStatus(" ");
		tAchAuthLog.setMediaCode("");
		tAchAuthLog.setRelationCode(iRelationCode);
		tAchAuthLog.setRelAcctName(iRelAcctName);
		tAchAuthLog.setRelationId(iRelationId);
		tAchAuthLog.setRelAcctBirthday(parse.stringToInteger(iRelAcctBirthday));
		tAchAuthLog.setRelAcctGender(iRelAcctGender);
		tAchAuthLog.setTitaTxCd(iTitaTxCd);
		try {
			achAuthLogService.insert(tAchAuthLog, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "PostAuthLog insert " + e.getErrorMsg());
		}
	}

	private void deletePostAuthLog(String authApplCode, String authCode, TitaVo titaVo) throws LogicException {
		this.info("deletePostAuthLog ... ");
		PostAuthLog tPostAuthLog = postAuthLogService.repayAcctFirst(iCustNo, iPostDepCode, iRepayAcct, authCode,
				titaVo);
		if (tPostAuthLog == null) {
			throw new LogicException("E0015", "此筆授權資料找不到"); // 檢查錯誤
		}
		if (authApplCode.equals("1")) {
			if (tPostAuthLog.getPropDate() > 0) {
				throw new LogicException("E0015", "此筆授權資料已提出"); // 檢查錯誤
			}
		}
		if (!authApplCode.equals(tPostAuthLog.getAuthApplCode())) {
			throw new LogicException("E0015", "最後一筆授權資料記號不符" + authApplCode + "/" + tPostAuthLog.getAuthApplCode()); // 檢查錯誤
		}
		postAuthLogService.holdById(tPostAuthLog, titaVo);
		try {
			postAuthLogService.delete(tPostAuthLog, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0006", e.getErrorMsg());
		}
	}

	private void deleteAchAuthLog(String createFlag, TitaVo titaVo) throws LogicException {
		this.info("deleteAchAuthLog ... ");
		AchAuthLog tAchAuthLog = achAuthLogService.repayAcctFirst(iCustNo, iRepayBank, iRepayAcct, titaVo);
		if (tAchAuthLog == null) {
			throw new LogicException("E0015", "此筆授權資料找不到"); // 檢查錯誤
		}
		if (createFlag.equals("A")) {
			if (tAchAuthLog.getPropDate() > 0) {
				throw new LogicException("E0015", "此筆授權資料已提出"); // 檢查錯誤
			}
		}
		if (("A".equals(createFlag) && !"A".equals(tAchAuthLog.getCreateFlag()))
				|| ("D".equals(createFlag) && !"D".equals(tAchAuthLog.getCreateFlag()))) {
			throw new LogicException("E0015", "最後一筆授權資料記號不符" + createFlag + "/" + tAchAuthLog.getCreateFlag()); // 檢查錯誤
		}
		achAuthLogService.holdById(tAchAuthLog, titaVo);
		try {
			achAuthLogService.delete(tAchAuthLog, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0006", e.getErrorMsg());
		}
	}

	// 新增授權帳號檔
	private BankAuthAct inserBankAuthAct(String authType, String acctSeq, String status, TitaVo titaVo)
			throws LogicException {
		BankAuthAct tBankAuthAct = new BankAuthAct();
		BankAuthActId tBankAuthActId = new BankAuthActId();
		tBankAuthActId.setCustNo(iCustNo);
		tBankAuthActId.setFacmNo(iFacmNo);
		tBankAuthActId.setAuthType(authType);
		tBankAuthAct.setBankAuthActId(tBankAuthActId);
		tBankAuthAct.setRepayBank(iRepayBank);
		tBankAuthAct.setRepayAcct(iRepayAcct);
		tBankAuthAct.setPostDepCode(iPostDepCode);
		tBankAuthAct.setLimitAmt(iLimitAmt);
		tBankAuthAct.setAcctSeq(acctSeq);
		tBankAuthAct.setStatus(status);

		try {
			bankAuthActService.insert(tBankAuthAct, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "授權帳號檔");
		}

		return tBankAuthAct;
	}

	//
	private void deleteBankAuthAct(BankAuthActId tBankAuthActId, TitaVo titaVo) throws LogicException {
		this.info("deleteBankAuthAct ...");
		BankAuthAct tBankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);

		try {
			bankAuthActService.delete(tBankAuthAct, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", "授權帳號檔");
		}
	}

	// 新帳號碼
	private String getNewAcctSeq(TitaVo titaVo) throws LogicException {
		this.info("getAcctSeq start...");
		int seq = 0;
		String acctSeq = "";
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = bankAuthActComServiceImpl.getAcctSeq(iCustNo, iPostDepCode, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

//		同戶號，計算帳號個數
		if (resultList != null && resultList.size() > 0) {
			seq = parse.stringToInteger(resultList.get(0).get("F1"));
		}

//		第一筆=空白，第二筆之後=01起編
		if (seq >= 1) {
			acctSeq = FormatUtil.pad9("" + seq, 2);
		}
		return acctSeq;
	}

	// 同戶扣款帳號
	private BankAuthAct getRepayAcct(TitaVo titaVo) throws LogicException {
		int minfacmNo = 999;
		BankAuthAct tBankAuthAct = null;
		Slice<BankAuthAct> slBankAuthAct = bankAuthActService.findAcctNo(iCustNo, iRepayAcct, iRepayBank, 0, 999, 0,
				Integer.MAX_VALUE, titaVo);
		if (slBankAuthAct != null) {
			for (BankAuthAct t : slBankAuthAct.getContent()) {
				if (t.getFacmNo() == iFacmNo) {
					tBankAuthAct = t;
					break;
				} else {
					if (t.getFacmNo() < minfacmNo) {
						tBankAuthAct = t;
					}
				}
			}
		}
		return tBankAuthAct;
	}

	// 新增郵局授權記錄歷史檔
	private void insertPostHistory(PostAuthLog t, String authApplCode, TitaVo titaVo) throws LogicException {
		this.info("insertPostHistory ...");
		PostAuthLogHistory tPostAuthLogHistory = new PostAuthLogHistory();
		tPostAuthLogHistory.setCustNo(t.getCustNo());
		tPostAuthLogHistory.setFacmNo(t.getFacmNo());
		tPostAuthLogHistory.setAuthCode(t.getAuthCode());
		tPostAuthLogHistory.setAuthCreateDate(t.getAuthCreateDate());
		tPostAuthLogHistory.setAuthApplCode(authApplCode);
		tPostAuthLogHistory.setPostDepCode(t.getPostDepCode());
		tPostAuthLogHistory.setRepayAcct(t.getRepayAcct());
		tPostAuthLogHistory.setCustId(t.getCustId());
		tPostAuthLogHistory.setRepayAcctSeq(t.getRepayAcctSeq());
		tPostAuthLogHistory.setProcessDate(t.getProcessDate());
		tPostAuthLogHistory.setProcessTime(t.getProcessTime());
		tPostAuthLogHistory.setStampFinishDate(t.getStampFinishDate());
		tPostAuthLogHistory.setStampCancelDate(t.getStampCancelDate());
		tPostAuthLogHistory.setStampCode(t.getStampCode());
		tPostAuthLogHistory.setPostMediaCode(t.getPostMediaCode());
		tPostAuthLogHistory.setAuthErrorCode(t.getAuthErrorCode());
		tPostAuthLogHistory.setFileSeq(t.getFileSeq());
		tPostAuthLogHistory.setPropDate(t.getPropDate());
		tPostAuthLogHistory.setRetrDate(t.getRetrDate());
		tPostAuthLogHistory.setDeleteDate(t.getDeleteDate());
		tPostAuthLogHistory.setRelationCode(t.getRelationCode());
		tPostAuthLogHistory.setRelAcctName(t.getRelAcctName());
		tPostAuthLogHistory.setRelationId(t.getRelationId());
		tPostAuthLogHistory.setRelAcctBirthday(t.getRelAcctBirthday());
		tPostAuthLogHistory.setRelAcctGender(t.getRelAcctGender());
		tPostAuthLogHistory.setAmlRsp(t.getAmlRsp());
		try {
			postAuthLogHistoryService.insert(tPostAuthLogHistory, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "PostAuthLogHistory insert " + e.getErrorMsg());
		}
	}

//	 * @param iStatus 1:停止使用 0:授權成功
	// 新增ACH授權記錄歷史檔
	private void insertAchHistory(AchAuthLog t, String createFlag, TitaVo titaVo) throws LogicException {
		this.info("insertAchHistory ...");
		AchAuthLogHistory tAchAuthLogHistory = new AchAuthLogHistory();
		tAchAuthLogHistory.setCustNo(t.getCustNo());
		tAchAuthLogHistory.setFacmNo(t.getFacmNo());
		tAchAuthLogHistory.setAuthCreateDate(t.getAuthCreateDate());
		tAchAuthLogHistory.setRepayBank(t.getRepayBank());
		tAchAuthLogHistory.setRepayAcct(t.getRepayAcct());
		tAchAuthLogHistory.setCreateFlag(createFlag);
		tAchAuthLogHistory.setProcessDate(t.getProcessDate());
		tAchAuthLogHistory.setProcessTime(t.getProcessTime());
		tAchAuthLogHistory.setStampFinishDate(t.getStampFinishDate());
		tAchAuthLogHistory.setAuthStatus(t.getAuthStatus());
		tAchAuthLogHistory.setAuthMeth(t.getAuthMeth());
		tAchAuthLogHistory.setLimitAmt(t.getLimitAmt());
		tAchAuthLogHistory.setMediaCode(t.getMediaCode());
		tAchAuthLogHistory.setBatchNo(t.getBatchNo());
		tAchAuthLogHistory.setPropDate(t.getPropDate());
		tAchAuthLogHistory.setRetrDate(t.getRetrDate());
		tAchAuthLogHistory.setDeleteDate(t.getDeleteDate());
		tAchAuthLogHistory.setRelationCode(t.getRelationCode());
		tAchAuthLogHistory.setRelAcctName(t.getRelAcctName());
		tAchAuthLogHistory.setRelationId(t.getRelationId());
		tAchAuthLogHistory.setRelAcctBirthday(t.getRelAcctBirthday());
		tAchAuthLogHistory.setRelAcctGender(t.getRelAcctGender());
		tAchAuthLogHistory.setAmlRsp(t.getAmlRsp());
		try {
			achAuthLogHistoryService.insert(tAchAuthLogHistory, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "AchAuthLogHistory insert " + e.getErrorMsg());
		}
	}

	// 新增還款帳號變更(含還款方式)紀錄檔
	private void addRepayActChangeLog(String status, TitaVo titaVo) throws LogicException {
		this.info("addRepayActChangeLog ...");
		RepayActChangeLog tRepayActChangeLog = new RepayActChangeLog();
		tRepayActChangeLog.setCustNo(iCustNo);
		tRepayActChangeLog.setFacmNo(iFacmNo);
		tRepayActChangeLog.setRepayCode(iRepayCode);
		tRepayActChangeLog.setRepayBank(iRepayBank);
		tRepayActChangeLog.setRepayAcct(iRepayAcct);
		tRepayActChangeLog.setPostDepCode(iPostDepCode);
		tRepayActChangeLog.setStatus(status);
		tRepayActChangeLog.setRelDy(this.txBuffer.getTxCom().getReldy());
		tRepayActChangeLog.setRelTxseq(this.txBuffer.getTxCom().getRelNo());
		try {
			repayActChangeLogService.insert(tRepayActChangeLog, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "還款帳號變更(含還款方式)紀錄檔");
		}
	}

	// 刪除還款帳號變更(含還款方式)紀錄檔
	private void addRepayActChangeLogDelete(TitaVo titaVo) throws LogicException {
		this.info("addRepayActChangeLogDelete ...");
		RepayActChangeLog tRepayActChangeLog = repayActChangeLogService.findRelTxseqFirst(
				this.txBuffer.getTxCom().getReldy() + 19110000, this.txBuffer.getTxCom().getRelNo(), titaVo);
		if (tRepayActChangeLog == null) {
			return;
		}
		tRepayActChangeLog = repayActChangeLogService.holdById(tRepayActChangeLog, titaVo);
		try {
			repayActChangeLogService.delete(tRepayActChangeLog, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", "還款帳號變更(含還款方式)紀錄檔");
		}
	}

	// 新增還款帳號變更(含還款方式)紀錄檔
	private void insertRepayActChangeLog(BankAuthAct t, TitaVo titaVo) throws LogicException {
		this.info("insertRepayActChangeLog ...");
		if (t.getAuthType().equals("02")) {
			return;
		}
		RepayActChangeLog tRepayActChangeLog = new RepayActChangeLog();
		tRepayActChangeLog.setCustNo(t.getCustNo());
		tRepayActChangeLog.setFacmNo(t.getFacmNo());
		tRepayActChangeLog.setRepayCode(2);
		tRepayActChangeLog.setRepayBank(t.getRepayBank());
		tRepayActChangeLog.setRepayAcct(t.getRepayAcct());
		tRepayActChangeLog.setPostDepCode(t.getPostDepCode());
		tRepayActChangeLog.setStatus(t.getStatus());
		tRepayActChangeLog.setRelDy(this.txBuffer.getTxCom().getReldy());
		tRepayActChangeLog.setRelTxseq(this.txBuffer.getTxCom().getRelNo());
		try {
			repayActChangeLogService.insert(tRepayActChangeLog, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "還款帳號變更(含還款方式)紀錄檔");
		}
	}

	private void setVarValue(TitaVo titaVo) throws LogicException {
		this.info("setVarValue ...");
		if ("L2".equals(titaVo.getTxcd().substring(0, 2))) {
			iPostDepCode = titaVo.get("PostCode");
			iRepayAcct = titaVo.get("RepayAcctNo");
			iRelationCode = titaVo.get("RelationCode");
			iRelAcctName = titaVo.get("RelationName");
			iRelAcctBirthday = titaVo.get("RelationBirthday");
			iRelAcctGender = titaVo.get("RelationGender");
			iCustId = titaVo.getParam("CustId");

		} else if ("L4".equals(titaVo.getTxcd().substring(0, 2))) {
			iPostDepCode = titaVo.get("PostDepCode");
			iRepayAcct = titaVo.get("RepayAcct");
			iRelationCode = titaVo.get("RelationCode");
			iRelAcctName = titaVo.get("RelAcctName");
			iRelAcctBirthday = titaVo.get("RelAcctBirthday");
			iRelAcctGender = titaVo.get("RelAcctGender");
			iCustId = titaVo.getParam("CustId");
		}
		if (iPostDepCode == null || "".equals(iPostDepCode)) {
			iPostDepCode = " ";
		}

		iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		iFacmNo = parse.stringToInteger(titaVo.get("FacmNo"));
		iRepayCode = parse.stringToInteger(titaVo.get("RepayCode"));
		iRepayBank = FormatUtil.pad9(titaVo.get("RepayBank"), 3);
		iRelationId = titaVo.get("RelationId");
		iTitaTxCd = titaVo.getTxcd();

		if (titaVo.get("LimitAmt") != null) {
			iLimitAmt = parse.stringToBigDecimal(titaVo.get("LimitAmt"));
		}
		if (iLimitAmt == null) {
			iLimitAmt = BigDecimal.ZERO;
		}

		if (titaVo.get("FuncCode") != null) {
			iFuncCode = parse.stringToInteger(titaVo.get("FuncCode"));
		}

		if (titaVo.getTxcd() != null && titaVo.getTxcd().length() >= 2) {
			txType = titaVo.getTxcd().substring(1, 2);
		}
	}

	private void showLog() throws LogicException {
		this.info("showLog ...");
		this.info("iFuncCode : " + iFuncCode);
		this.info("iCustId : " + iCustId);
		this.info("iCustNo : " + iCustNo);
		this.info("iFacmNo : " + iFacmNo);
		this.info("iRepayBank : " + iRepayBank);
		this.info("iPostDepCode : " + iPostDepCode);
		this.info("iRepayAcct : " + iRepayAcct);
		this.info("iRelationCode : " + iRelationCode);
		this.info("iRelAcctName : " + iRelAcctName);
		this.info("iRelAcctBirthday : " + iRelAcctBirthday);
		this.info("iRelAcctGender : " + iRelAcctGender);
		this.info("iRelationId : " + iRelationId);
		this.info("iLimitAmt : " + iLimitAmt);
		this.info("iTitaTxCd : " + iTitaTxCd);
		this.info("txType : " + txType);
		this.info("RepayCode : " + iRepayCode);
	}

}
