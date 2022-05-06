package com.st1.itx.trade.L4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.AchAuthLogId;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogId;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BankAuthActCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.AchAuthFileVo;
import com.st1.itx.util.common.data.PostAuthFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4414")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4414 extends TradeBuffer {
	@Autowired
	DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public AchAuthLogService achAuthLogService;

	@Autowired
	public PostAuthLogService postAuthLogService;

	@Autowired
	public CdBankService cdBankService;
	@Autowired
	public LoanCom loanCom;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public TotaVo totaA;

	@Autowired
	public TotaVo totaB;

	@Autowired
	public TotaVo totaC;

	@Autowired
	BankAuthActCom bankAuthActCom;
	@Autowired
	MakeReport makeReport;

	// 上傳預設目錄
	@Value("${iTXInFolder}")
	private String inFolder = "";

	private int cntA = 0;
	private int finishCntA = 0;
	private int cancelCntA = 0;
	private int cntB = 0;
	private int finishCntB = 0;
	private int cancelCntB = 0;
	private int cntC = 0;
	private int finishCntC = 0;
	private int cancelCntC = 0;
	private int headRocTxday = 0;
	private int footCreateDateC = 0;

	private int AchStampFinishDate = 0;
	// 郵局尾錄資料建檔日期
	private int PostCreateDate = 0;

	private String nowDate;
	private String nowTime;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4414 ");
		this.info("active L4414 ");
		this.totaVo.init(titaVo);

		bankAuthActCom.setTxBuffer(txBuffer);

		int achCnt = 0;
		int postCnt = 0;

		cntA = 0;
		finishCntA = 0;
		cancelCntA = 0;
		headRocTxday = 0;
		cntB = 0;
		finishCntB = 0;
		cancelCntB = 0;
		cntC = 0;
		finishCntC = 0;
		cancelCntC = 0;
		footCreateDateC = 0;

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

//		暫定路徑 待討論過後決定抓取路徑方法
		String filena = titaVo.getParam("FILENA").trim();

		if (filena.indexOf(";") < 0) {
			filena = filena + ";";
		}
		String[] filelist = filena.split(";");
		for (String filename : filelist) {
			this.info("fileName : " + filename);
			this.info("filename.substring(0, 1) =" + filename.substring(0, 1));
			this.info("filename.substring(0, 1).indexOf(\"A\") >= 0 =" + filename.substring(0, 1).indexOf("A"));
			this.info("filename.substring(0, 1).indexOf(\"P\") >= 0 =" + filename.substring(0, 1).indexOf("P"));
			if (filename.substring(0, 1).indexOf("A") >= 0) {
//					filePath1 = "D:\\temp\\TestingInPut\\AHP21P_授回.txt";
				String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
						+ File.separatorChar + filename;

				achCnt = achCnt + 1;

				ArrayList<String> dataLineList = new ArrayList<>();

				// 編碼參數，設定為UTF-8 || big5
				try {
					dataLineList = fileCom.intputTxt(filePath1, "UTF-8");
				} catch (IOException e) {
					throw new LogicException("E0014", "L4414(" + filePath1 + ")");
				}

				AchAuthFileVo achAuthFileVo = new AchAuthFileVo();

				// 使用資料容器內定義的方法切資料
				achAuthFileVo.setValueFromFile(dataLineList);
				AchStampFinishDate = Integer.parseInt(dataLineList.get(0).substring(9, 17)) + 19110000;
				this.info("AchStampFinishDate==" + AchStampFinishDate);

				setAchAuthLog(achAuthFileVo, titaVo);

			} else if (filename.substring(0, 1).indexOf("P") >= 0) {
				int postsize = 0;
//					POST
//					暫定路徑 待討論過後決定抓取路徑方法
//					filePath1 = "D:\\temp\\TestingInPut\\PO$P21P_846授權回.txt";
				String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
						+ File.separatorChar + filename;

				postCnt = postCnt + 1;

				ArrayList<String> dataLineList = new ArrayList<>();

				// 編碼參數，設定為UTF-8 || big5
				try {
					dataLineList = fileCom.intputTxt(filePath1, "UTF-8");
				} catch (IOException e) {
					throw new LogicException("E0014", "L4414(" + filePath1 + ") : " + e.getMessage());
				}

				PostAuthFileVo postAuthFileVo = new PostAuthFileVo();
				// 使用資料容器內定義的方法切資料
				postAuthFileVo.setValueFromFile(dataLineList);

				postsize = dataLineList.size() - 1;
				PostCreateDate = Integer.parseInt(dataLineList.get(postsize).substring(26, 34));

				setPostAuthLog(postAuthFileVo, titaVo);
			} else {
				throw new LogicException("E0014", filename + " 此檔案不符本交易處理範圍");
			}
		}

		// 編碼參數，設定為UTF-8 || big5
		// 取當前日期時間
		this.nowDate = makeReport.dDateUtil.getNowStringRoc();
		this.nowTime = makeReport.dDateUtil.getNowStringTime();
		String bcDate = makeReport.showBcDate(this.nowDate, 1);
		String time = makeReport.showTime(this.nowTime);

		totaA.putParam("MSGID", "L444A");
		totaA.putParam("OBcDateA", bcDate); // 日期
		totaA.putParam("OTimeA", time); // 時間
		totaA.putParam("OCntA", cntA); // 總筆數
		totaA.putParam("OTotalCntA", cntA); // 總筆數
		totaA.putParam("OFinishCntA", finishCntA); // 成功筆數
		totaA.putParam("OCancelCntA", cancelCntA); // 失敗筆數
		this.addList(this.totaA);

		totaB.putParam("MSGID", "L444B");
		totaB.putParam("OBcDateB", bcDate); // 日期
		totaB.putParam("OTimeB", time); // 時間
		totaB.putParam("OCntB", cntB); // 總筆數
		totaB.putParam("OTotalCntB", cntB); // 總筆數
		totaB.putParam("OFinishCntB", finishCntB); // 成功筆數
		totaB.putParam("OCancelCntB", cancelCntB); // 失敗筆數
		this.addList(this.totaB);

		totaC.putParam("MSGID", "L444C");
		totaC.putParam("OBcDateC", bcDate); // 日期
		totaC.putParam("OTimeC", time); // 時間
		totaC.putParam("OCntC", cntC); // 總筆數
		totaC.putParam("OTotalCntC", cntC); // 總筆數
		totaC.putParam("OFinishCntC", finishCntC); // 成功筆數
		totaC.putParam("OCancelCntC", cancelCntC); // 失敗筆數
		this.addList(this.totaC);

		return this.sendList();
	}

	private void setAchAuthLog(AchAuthFileVo achAuthFileVo, TitaVo titaVo) throws LogicException {

		// 取值
		headRocTxday = parse.stringToInteger("" + achAuthFileVo.get("HeadRocTxday"));

		ArrayList<OccursList> uploadFile = achAuthFileVo.getOccursList();

		if (uploadFile != null && uploadFile.size() != 0) {

//			排序 終止需再新增前
			uploadFile.sort((c1, c2) -> {
				int result = 0;
				if (c1.get("CreateFlag").compareTo(c2.get("CreateFlag")) != 0) {
					result = c2.get("CreateFlag").compareTo(c1.get("CreateFlag"));
				}
				return result;
			});

			for (OccursList tempOccursList : uploadFile) {
				AchAuthLog tAchAuthLog = new AchAuthLog();
				AchAuthLogId tAchAuthLogId = new AchAuthLogId();

				String authCheck = "" + tempOccursList.get("AuthCheck");
				this.info(" @@##$$!! authCheck :" + authCheck);

				if ("R".equals(authCheck)) {
//					tAchAuthLogId.setAuthCreateDate(parse.stringToInteger(tempOccursList.get("AuthCreateDate")));// TODO:改為提出日期
//					tAchAuthLogId.setCustNo(parse.stringToInteger(tempOccursList.get("CustNo")));
//					tAchAuthLogId.setRepayBank(tempOccursList.get("RepayBank"));
//					tAchAuthLogId.setRepayAcct(tempOccursList.get("RepayAcct"));
//					tAchAuthLogId.setCreateFlag(tempOccursList.get("CreateFlag"));

					this.info("CustNo :" + parse.stringToInteger(tempOccursList.get("CustNo")));
					this.info("FacmNo :" + parse.stringToInteger(tempOccursList.get("FacmNo")));
					this.info("RepayBank :" + tempOccursList.get("RepayBank"));
					this.info("RepayAcct :" + tempOccursList.get("RepayAcct"));
					this.info("PropDate :" + parse.stringToInteger(tempOccursList.get("PropDate")));

					tAchAuthLog = achAuthLogService.facmNoPropDateFirst(
							parse.stringToInteger(tempOccursList.get("CustNo")),
							parse.stringToInteger(tempOccursList.get("FacmNo")), tempOccursList.get("RepayBank"),
							tempOccursList.get("RepayAcct"),
							parse.stringToInteger(tempOccursList.get("PropDate")) + 19110000, titaVo);

					if (tAchAuthLog != null) {
						tAchAuthLog.setRetrDate(dateUtil.getNowIntegerForBC());
						tAchAuthLog.setAuthStatus(tempOccursList.get("AuthStatus"));

						this.info("AuthStatus : " + tempOccursList.get("AuthStatus"));

						if ("0".equals("" + tempOccursList.get("AuthStatus"))) {
							this.info("Update StampFinishDate !!!");
							tAchAuthLog.setStampFinishDate(headRocTxday);

						} else {

						}

						try {
							achAuthLogService.update(tAchAuthLog, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "L4414 AchAuthLog update " + e.getErrorMsg());
						}
//						變更帳號檔
						achToBankAuthAct(tAchAuthLog, titaVo);
//						輸出
						setOutput(tAchAuthLog, titaVo);

					} else {
						throw new LogicException("E0014", "ACH授權記錄檔查無資料，請確認檔案");
					}
				} else {
					throw new LogicException("E0014", "請確認是否為提回檔案");
				}
			}
		} else {
			throw new LogicException("E0014", "請確認檔案");
		}
	}

	private void setPostAuthLog(PostAuthFileVo postAuthFileVo, TitaVo titaVo) throws LogicException {

		ArrayList<OccursList> uploadFile = postAuthFileVo.getOccursList();

		String authCode = "";

		if (uploadFile != null && uploadFile.size() != 0) {

			// 取值
//			9	FootErrorCnt    錯誤筆數		34-40	9(6)	初始值為0，回送時使用	
//			10	FootSuccsCnt    成功筆數		40-46	9(6)	初始值為0，回送時使用	
			headRocTxday = parse.stringToInteger("" + postAuthFileVo.get("FootMediaDate"));
			int footErrorCnt = parse.stringToInteger("" + postAuthFileVo.get("FootErrorCnt"));
			int footSuccsCnt = parse.stringToInteger("" + postAuthFileVo.get("FootSuccsCnt"));
			footCreateDateC = parse.stringToInteger("" + postAuthFileVo.get("FootCreateDate")) - 19110000;

			if (footErrorCnt + footSuccsCnt == 0) {
				throw new LogicException("E0014", "請確認是否為提回檔案");
			}
			for (OccursList tempOccursList : uploadFile) {
				if ("846".equals(tempOccursList.get("OccOrgCode"))) {
					authCode = "1";
				} else if ("53N".equals(tempOccursList.get("OccOrgCode"))) {
					authCode = "2";
				}
				PostAuthLog tPostAuthLog = postAuthLogService.repayAcctFirst(
						parse.stringToInteger(tempOccursList.get("CustNo")), tempOccursList.get("PostDepCode"),
						tempOccursList.get("RepayAcct"), authCode, titaVo);
				if (tPostAuthLog == null) {
					throw new LogicException("E0014", "郵局授權記錄檔查無資料，請確認檔案");
				}
				tPostAuthLog = postAuthLogService.holdById(tPostAuthLog, titaVo);
				switch (tempOccursList.get("AuthApplCode")) {
				case "1": // 1.申請
					if (!"1".equals(tPostAuthLog.getAuthApplCode())) {
						throw new LogicException("E0014", "申請，郵局授權記錄檔申請代號<>1，" + tPostAuthLog.getAuthApplCode());
					}
					tPostAuthLog.setRetrDate(dateUtil.getNowIntegerForBC());
					tPostAuthLog.setStampCode(FormatUtil.pad9(tempOccursList.get("StampCode"), 1));
					this.info("StampCode==" + tempOccursList.get("StampCode").trim());
					this.info("AuthErrorCode==" + tempOccursList.get("AuthErrorCode").trim());

					tPostAuthLog.setAuthErrorCode(FormatUtil.pad9(tempOccursList.get("AuthErrorCode").trim(), 2));
					if ("00".equals(FormatUtil.pad9("" + tempOccursList.get("AuthErrorCode"), 2))) {
						this.info("Update StampFinishDate !!!");
//						申請UP核印完成日期,清空核印取消日期
						tPostAuthLog.setStampFinishDate(headRocTxday);
						tPostAuthLog.setStampCancelDate(0);

						finishCntC++;
					} else {
						cancelCntC++;
					}
//					檢查期款、火險需同時成功或失敗才更新帳號檔
					PostAuthLog tPostAuthLog3 = postAuthLogService.repayAcctFirst(tPostAuthLog.getCustNo(),
							tPostAuthLog.getPostDepCode(), tPostAuthLog.getRepayAcct(),
							tPostAuthLog.getAuthCode().equals("1") ? "2" : "1", titaVo);
					if (tPostAuthLog3 != null) {
						if (tPostAuthLog3.getAuthErrorCode().equals(tPostAuthLog.getAuthErrorCode())) {
							// 變更帳號檔
							postToBankAuthAct(tPostAuthLog, titaVo);
						}
					}
					try {
						postAuthLogService.update(tPostAuthLog, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "L4414 PostAuthLog update " + e.getErrorMsg());
					}
					break;

				case "2": // 2.終止
					if (!"2".equals(tPostAuthLog.getAuthApplCode())) {
						throw new LogicException("E0014", "終止，郵局授權記錄檔申請代號<>2，" + tPostAuthLog.getAuthApplCode());
					}
					tPostAuthLog.setRetrDate(dateUtil.getNowIntegerForBC());
					tPostAuthLog.setAuthErrorCode(FormatUtil.pad9(tempOccursList.get("AuthErrorCode").trim(), 2));
//					終止UP核印取消日期,清空核印完成日期
					tPostAuthLog.setStampCancelDate(dateUtil.getNowIntegerForBC());
					tPostAuthLog.setStampFinishDate(0);
					// 變更帳號檔
					postToBankAuthAct(tPostAuthLog, titaVo);
					try {
						postAuthLogService.update(tPostAuthLog, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "L4414 PostAuthLog update " + e.getErrorMsg());
					}

					finishCntC++;
					break;

				case "3": // 3.郵局終止
					if (!"1".equals(tPostAuthLog.getAuthApplCode())) {
						throw new LogicException("E0014", "郵局終止，郵局授權記錄檔申請代號<>1，" + tPostAuthLog.getAuthApplCode());
					}
					PostAuthLog tPostAuthLog2 = new PostAuthLog();
					PostAuthLogId tPostAuthLogId = new PostAuthLogId();
					tPostAuthLogId.setAuthCreateDate(dateUtil.getNowIntegerForBC());
					tPostAuthLogId.setAuthApplCode("3");
					tPostAuthLogId.setCustNo(tPostAuthLog.getCustNo());
					tPostAuthLogId.setPostDepCode(tPostAuthLog.getPostDepCode());
					tPostAuthLogId.setRepayAcct(tPostAuthLog.getRepayAcct());
					tPostAuthLogId.setAuthCode(tPostAuthLog.getAuthCode());
					tPostAuthLog2.setAuthCreateDate(dateUtil.getNowIntegerForBC());
					tPostAuthLog2.setAuthApplCode("3");
					tPostAuthLog2.setCustNo(tPostAuthLog.getCustNo());
					tPostAuthLog2.setPostDepCode(tPostAuthLog.getPostDepCode());
					tPostAuthLog2.setRepayAcct(tPostAuthLog.getRepayAcct());
					tPostAuthLog2.setAuthCode(tPostAuthLog.getAuthCode());
					tPostAuthLog2.setPostAuthLogId(tPostAuthLogId);
					tPostAuthLog2.setFacmNo(tPostAuthLog.getFacmNo());
					tPostAuthLog2.setCustId(tPostAuthLog.getCustId());
					tPostAuthLog2.setRepayAcctSeq(tPostAuthLog.getRepayAcctSeq());
					tPostAuthLog2.setProcessDate(dateUtil.getNowIntegerForBC());
					tPostAuthLog2.setRelationCode(tPostAuthLog.getRelationCode());
					tPostAuthLog2.setRelAcctName(tPostAuthLog.getRelAcctName());
					tPostAuthLog2.setRelationId(tPostAuthLog.getRelationId());
					tPostAuthLog2.setRelAcctBirthday((tPostAuthLog.getRelAcctBirthday()));
					tPostAuthLog2.setRelAcctGender(tPostAuthLog.getRelAcctGender());
					tPostAuthLog2.setRetrDate(dateUtil.getNowIntegerForBC());
					tPostAuthLog2.setAuthErrorCode(FormatUtil.pad9(tempOccursList.get("AuthErrorCode").trim(), 2));
					tPostAuthLog.setStampFinishDate(0);
					tPostAuthLog.setStampCancelDate(dateUtil.getNowIntegerForBC());
					// 變更帳號檔
					postToBankAuthAct(tPostAuthLog2, titaVo);
					try {
						postAuthLogService.insert(tPostAuthLog2, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "L4414 PostAuthLog " + e.getErrorMsg());
					}
					finishCntC++;

					break;

				case "4": // 4.誤終止
					if (!"3".equals(tPostAuthLog.getAuthApplCode())) {
						throw new LogicException("E0014", "誤終止，郵局授權記錄檔申請代號<>1，" + tPostAuthLog.getAuthApplCode());
					}

					tPostAuthLog.setAuthApplCode("4");
					tPostAuthLog.setStampFinishDate(headRocTxday);
					tPostAuthLog.setStampCancelDate(0);

					// 變更帳號檔
					postToBankAuthAct(tPostAuthLog, titaVo);
					try {
						postAuthLogService.delete(tPostAuthLog, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "L4414 PostAuthLog " + e.getErrorMsg());
					}

					finishCntC++;
					break;
				}

//						輸出
				setOutput(tPostAuthLog, titaVo);
			}
		}
	}

	// ACH授權提回更新帳號檔
	private void achToBankAuthAct(AchAuthLog tAchAuthLog, TitaVo titaVo) throws LogicException {
		bankAuthActCom.updAchAcct(tAchAuthLog, titaVo);
	}

	// Post授權提回更新帳號檔
	private void postToBankAuthAct(PostAuthLog tPostAuthLog, TitaVo titaVo) throws LogicException {
		bankAuthActCom.updPostAcct(tPostAuthLog, titaVo);

	}

	private String bankX(String bank, TitaVo titaVo) {
		String result = "";
		Slice<CdBank> sCdBank = null;
		sCdBank = cdBankService.bankCodeLike(bank, index, limit, titaVo);

		List<CdBank> lCdBank = new ArrayList<CdBank>();

		lCdBank = sCdBank == null ? null : sCdBank.getContent();

		result = lCdBank.get(0).getBankItem();

		if (result != null) {
			result = result.trim();
		}
		return result;
	}

	private void setOutput(AchAuthLog tAchAuthLog, TitaVo titaVo) throws LogicException {
		if ("103".equals(tAchAuthLog.getRepayBank())) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOCustNoA", tAchAuthLog.getCustNo());
			occursList.putParam("OOCustNameA", loanCom.getCustNameByNo(tAchAuthLog.getCustNo()));
			occursList.putParam("OOFacmNoA", tAchAuthLog.getFacmNo());
			occursList.putParam("OORepayAcctA", tAchAuthLog.getRepayAcct());
			occursList.putParam("OOAuthStatusA", tAchAuthLog.getAuthStatus());
			occursList.putParam("OOCreateFlagA", tAchAuthLog.getCreateFlag());

			totaA.putParam("OHeadRocTxdayA", tAchAuthLog.getPropDate()); // 資料建檔日期
			this.totaA.addOccursList(occursList);
			if ("0".equals(tAchAuthLog.getAuthStatus())) {
				finishCntA++;
			} else {
				cancelCntA++;
			}
			cntA++;
		} else {
			OccursList occursList = new OccursList();
			occursList.putParam("OOCustNoB", tAchAuthLog.getCustNo());
			occursList.putParam("OOCustNameB", loanCom.getCustNameByNo(tAchAuthLog.getCustNo()));
			occursList.putParam("OOFacmNoB", tAchAuthLog.getFacmNo());
			if (bankX(tAchAuthLog.getRepayBank(), titaVo).length() > 8) {
				occursList.putParam("OORepayBankXB", bankX(tAchAuthLog.getRepayBank(), titaVo).substring(0, 8));
			} else {
				occursList.putParam("OORepayBankXB", bankX(tAchAuthLog.getRepayBank(), titaVo));
			}
			occursList.putParam("OORepayAcctB", tAchAuthLog.getRepayAcct());
			occursList.putParam("OOAuthStatusB", tAchAuthLog.getAuthStatus());
			occursList.putParam("OOCreateFlagB", tAchAuthLog.getCreateFlag());

			totaB.putParam("OHeadRocTxdayB", tAchAuthLog.getPropDate()); // 資料建檔日期
			this.totaB.addOccursList(occursList);

			if ("0".equals(tAchAuthLog.getAuthStatus())) {
				finishCntB++;
			} else {
				cancelCntB++;
			}
			cntB++;
		}
	}

	private void setOutput(PostAuthLog tPostAuthLog, TitaVo titaVo) throws LogicException {
		OccursList occursList = new OccursList();
		occursList.putParam("OOCustNoC", tPostAuthLog.getCustNo());
		occursList.putParam("OOFacmNoC", tPostAuthLog.getFacmNo());
		occursList.putParam("OOCustNameC", loanCom.getCustNameByNo(tPostAuthLog.getCustNo()));
		occursList.putParam("OORepayAcctC", tPostAuthLog.getRepayAcct());
		occursList.putParam("OOStampCodeC", tPostAuthLog.getStampCode());
		occursList.putParam("OOAuthErrorCodeC", tPostAuthLog.getAuthErrorCode());
		totaC.putParam("OFootCreateDateC", tPostAuthLog.getPropDate()); // 資料建檔日期

		/* 將每筆資料放入Tota的OcList */
		this.totaC.addOccursList(occursList);
		cntC++;
	}
}