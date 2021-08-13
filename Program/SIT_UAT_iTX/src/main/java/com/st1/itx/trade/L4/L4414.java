package com.st1.itx.trade.L4;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.BankAuthActId;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.data.AchAuthFileVo;
import com.st1.itx.util.common.data.PostAuthFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * UploadPath=X,50<br>
 */

@Service("L4414")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4414 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4414.class);
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
	public FileCom fileCom;

	@Autowired
	public TotaVo totaA;

	@Autowired
	public TotaVo totaB;

	@Autowired
	public TotaVo totaC;

	@Autowired
	public BankAuthActService bankAuthActService;

	// 上傳預設目錄
	@Value("${iTXInFolder}")
	private String inFolder = "";

	private int cntA = 0;
	private int cntB = 0;
	private int cntC = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4414 ");
		this.info("active L4414 ");
		this.totaVo.init(titaVo);

		int achCnt = 0;
		int postCnt = 0;

		cntA = 0;
		cntB = 0;
		cntC = 0;

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
			if (filename.indexOf("AHP21P") >= 0) {
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

				setAchAuthLog(achAuthFileVo, titaVo);

			} else if (filename.indexOf("AHP22P") >= 0) {
//					filePath2 = "D:\\temp\\TestingInPut\\AHP22P_授回.txt";
				String filePath2 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
						+ File.separatorChar + filename;

				achCnt = achCnt + 1;

				ArrayList<String> dataLineList = new ArrayList<>();

				try {
					dataLineList = fileCom.intputTxt(filePath2, "UTF-8");
				} catch (IOException e) {
					throw new LogicException("E0014", "L4414(" + filePath2 + ") : " + e.getMessage());
				}

				AchAuthFileVo achAuthFileVo = new AchAuthFileVo();
				// 使用資料容器內定義的方法切資料
				achAuthFileVo.setValueFromFile(dataLineList);

				setAchAuthLog(achAuthFileVo, titaVo);
			} else if (filename.indexOf("PO$P21P") >= 0) {
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

				setPostAuthLog(postAuthFileVo, titaVo);
			} else if (filename.indexOf("PO$P22P") >= 0) {
//					filePath2 = "D:\\temp\\TestingInPut\\PO$P22P_53N授權回.txt";
				String filePath2 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
						+ File.separatorChar + filename;

				postCnt = postCnt + 1;

				ArrayList<String> dataLineList = new ArrayList<>();

				// 編碼參數，設定為UTF-8 || big5
				try {
					dataLineList = fileCom.intputTxt(filePath2, "UTF-8");
				} catch (IOException e) {
					throw new LogicException("E0014", "L4414(" + filePath2 + ") : " + e.getMessage());
				}

				PostAuthFileVo postAuthFileVo = new PostAuthFileVo();
				// 使用資料容器內定義的方法切資料
				postAuthFileVo.setValueFromFile(dataLineList);

				setPostAuthLog(postAuthFileVo, titaVo);
			} else {
				throw new LogicException("E0014", filename + " 此檔案不符本交易處理範圍");
			}
		}

		// 編碼參數，設定為UTF-8 || big5

		totaA.putParam("MSGID", "L444A");
		totaA.putParam("OCntA", cntA);
		this.addList(this.totaA);

		totaB.putParam("MSGID", "L444B");
		totaB.putParam("OCntB", cntB);
		this.addList(this.totaB);

		totaC.putParam("MSGID", "L444C");
		totaC.putParam("OCntC", cntC);
		this.addList(this.totaC);

		return this.sendList();
	}

	private void setAchAuthLog(AchAuthFileVo achAuthFileVo, TitaVo titaVo) throws LogicException {

		// 取值
//		int retrDate = parse.stringToInteger("" + achAuthFileVo.get("RetrDate"));

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
					tAchAuthLogId.setAuthCreateDate(parse.stringToInteger(tempOccursList.get("AuthCreateDate")));
					tAchAuthLogId.setCustNo(parse.stringToInteger(tempOccursList.get("CustNo")));
					tAchAuthLogId.setRepayBank(tempOccursList.get("RepayBank"));
					tAchAuthLogId.setRepayAcct(tempOccursList.get("RepayAcct"));
					tAchAuthLogId.setCreateFlag(tempOccursList.get("CreateFlag"));

					this.info("tAchAuthLogId :" + tAchAuthLogId.toString());

					tAchAuthLog = achAuthLogService.holdById(tAchAuthLogId);

					if (tAchAuthLog != null) {
						tAchAuthLog.setRetrDate(dateUtil.getNowIntegerForBC());
						tAchAuthLog.setAuthStatus(tempOccursList.get("AuthStatus"));

						this.info("AuthStatus : " + tempOccursList.get("AuthStatus"));

						if ("0".equals("" + tempOccursList.get("AuthStatus"))) {
							this.info("Update StampFinishDate !!!");
							tAchAuthLog.setStampFinishDate(dateUtil.getNowIntegerForBC());
						}

						try {
							this.info("Update AchAuthLog !!!");
							achAuthLogService.update(tAchAuthLog);
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

//			排序 終止需再新增前
			uploadFile.sort((c1, c2) -> {
				int result = 0;
				if (c1.get("OccApprCode").compareTo(c2.get("OccApprCode")) != 0) {
					result = c2.get("OccApprCode").compareTo(c1.get("OccApprCode"));
				}
				return result;
			});

//			9	FootErrorCnt    錯誤筆數		34-40	9(6)	初始值為0，回送時使用	
//			10	FootSuccsCnt    成功筆數		40-46	9(6)	初始值為0，回送時使用	
			int footErrorCnt = parse.stringToInteger("" + postAuthFileVo.get("FootErrorCnt"));
			int footSuccsCnt = parse.stringToInteger("" + postAuthFileVo.get("FootSuccsCnt"));

			for (OccursList tempOccursList : uploadFile) {
				PostAuthLog tPostAuthLog = new PostAuthLog();

				if (footErrorCnt + footSuccsCnt > 0) {
					if ("846".equals(tempOccursList.get("OccOrgCode"))) {
						authCode = "1";
					} else if ("53N".equals(tempOccursList.get("OccOrgCode"))) {
						authCode = "2";
					}

					tPostAuthLog = postAuthLogService.fileSeqFirst(
							parse.stringToInteger(tempOccursList.get("OccMediaDate")), authCode,
							parse.stringToInteger(tempOccursList.get("OccDataSeq")));

					if (tPostAuthLog != null) {
						PostAuthLog t2PostAuthLog = postAuthLogService.holdById(tPostAuthLog);

						t2PostAuthLog.setRetrDate(dateUtil.getNowIntegerForBC());
						t2PostAuthLog.setStampCode(FormatUtil.pad9(tempOccursList.get("StampCode"), 1));
						t2PostAuthLog.setAuthErrorCode(FormatUtil.pad9(tempOccursList.get("AuthErrorCode").trim(), 2));

						if ("00".equals(FormatUtil.pad9("" + tempOccursList.get("AuthErrorCode"), 2))) {
							this.info("Update StampFinishDate !!!");
							t2PostAuthLog.setStampFinishDate(dateUtil.getNowIntegerForBC());
						}

						try {
							this.info("Update PostAuthLog !!!");
							postAuthLogService.update(t2PostAuthLog);
						} catch (DBException e) {
							throw new LogicException("E0007", "L4414 PostAuthLog update " + e.getErrorMsg());
						}

//						變更帳號檔
						postToBankAuthAct(tPostAuthLog, titaVo);
//						輸出
						setOutput(tPostAuthLog, titaVo);
					} else {
						throw new LogicException("E0014", "郵局授權記錄檔查無資料，請確認檔案");
					}

				} else {
					throw new LogicException("E0014", "請確認是否為提回檔案");
				}
			}
		} else {
			throw new LogicException("E0014", "請確認檔案");
		}
	}

//	整批修改帳號
	private void achToBankAuthAct(AchAuthLog tAchAuthLog, TitaVo titaVo) throws LogicException {
		List<BankAuthAct> lBankAuthAct = new ArrayList<BankAuthAct>();

		Slice<BankAuthAct> sBankAuthAct = bankAuthActService.authCheck(tAchAuthLog.getAchAuthLogId().getCustNo(),
				tAchAuthLog.getAchAuthLogId().getRepayAcct(), 0, 999, this.index, this.limit, titaVo);

		lBankAuthAct = sBankAuthAct == null ? null : sBankAuthAct.getContent();

		if (lBankAuthAct != null && lBankAuthAct.size() != 0) {
			for (BankAuthAct tBankAuthAct : lBankAuthAct) {
				this.info("tAchAuthLog.getAchAuthLogId() : " + tAchAuthLog.getAchAuthLogId().toString());
				this.info("tAchAuthLog.getRepayAcctNo() : " + tAchAuthLog.getAchAuthLogId().getRepayAcct());

//				戶號 額度 授權類別 扣款銀行 郵局存款別 扣款帳號 狀態碼 每筆扣款限額 帳號碼 授權代號
				updateBankAuthAct(tBankAuthAct.getBankAuthActId(), tAchAuthLog.getRepayBank(), "",
						tAchAuthLog.getAchAuthLogId().getRepayAcct(), tAchAuthLog.getAuthStatus(),
						tAchAuthLog.getLimitAmt(), "", tAchAuthLog.getAchAuthLogId().getCreateFlag(), titaVo);
			}
		} else {
			this.info("查無此戶號、帳號");
			if ("A".equals(tAchAuthLog.getAchAuthLogId().getCreateFlag())) {
				BankAuthActId tBankAuthActId = new BankAuthActId();
				tBankAuthActId.setCustNo(tAchAuthLog.getAchAuthLogId().getCustNo());
				tBankAuthActId.setFacmNo(tAchAuthLog.getFacmNo());
				tBankAuthActId.setAuthType("00");
//				再次授權可為不同帳號，取消需有才更新
//				戶號 額度 授權類別 扣款銀行 郵局存款別 扣款帳號 狀態碼 每筆扣款限額 帳號碼 授權代號
				updateBankAuthAct(tBankAuthActId, tAchAuthLog.getRepayBank(), "",
						tAchAuthLog.getAchAuthLogId().getRepayAcct(), tAchAuthLog.getAuthStatus(),
						tAchAuthLog.getLimitAmt(), "", tAchAuthLog.getAchAuthLogId().getCreateFlag(), titaVo);

			} else {
				this.info("不為申請");
			}
		}
	}

//	整批修改帳號
	private void postToBankAuthAct(PostAuthLog tPostAuthLog, TitaVo titaVo) throws LogicException {

		Slice<BankAuthAct> sBankAuthAct = bankAuthActService.authCheck(tPostAuthLog.getPostAuthLogId().getCustNo(),
				tPostAuthLog.getPostAuthLogId().getRepayAcct(), 0, 999, this.index, this.limit, titaVo);

		List<BankAuthAct> lBankAuthAct = new ArrayList<BankAuthAct>();

		lBankAuthAct = sBankAuthAct == null ? null : sBankAuthAct.getContent();

		if (lBankAuthAct != null && lBankAuthAct.size() != 0) {
			for (BankAuthAct tBankAuthAct : lBankAuthAct) {
				this.info("tPostAuthLog.getPostAuthLogId() : " + tPostAuthLog.getPostAuthLogId().toString());
				this.info("tPostAuthLog.getRepayAcctNo() : " + tPostAuthLog.getPostAuthLogId().getRepayAcct());

//				戶號 額度 授權類別 扣款銀行 郵局存款別 扣款帳號 狀態碼 每筆扣款限額 帳號碼 授權代號
				updateBankAuthAct(tBankAuthAct.getBankAuthActId(), "700", tPostAuthLog.getPostDepCode(),
						tPostAuthLog.getPostAuthLogId().getRepayAcct(), tPostAuthLog.getAuthErrorCode(),
						BigDecimal.ZERO, tPostAuthLog.getRepayAcctSeq(),
						tPostAuthLog.getPostAuthLogId().getAuthApplCode(), titaVo);
			}
		} else {
			this.info("查無此戶號、帳號");
			if ("1".equals(tPostAuthLog.getPostAuthLogId().getAuthApplCode())) {
				BankAuthActId tBankAuthActId = new BankAuthActId();
				tBankAuthActId.setCustNo(tPostAuthLog.getPostAuthLogId().getCustNo());
				tBankAuthActId.setFacmNo(tPostAuthLog.getFacmNo());
				tBankAuthActId.setAuthType("01");

//				再次授權可為不同帳號，取消需有才更新
//				戶號 額度 授權類別 扣款銀行 郵局存款別 扣款帳號 狀態碼 每筆扣款限額 帳號碼 授權代號
				updateBankAuthAct(tBankAuthActId, "700", tPostAuthLog.getPostDepCode(),
						tPostAuthLog.getPostAuthLogId().getRepayAcct(), tPostAuthLog.getAuthErrorCode(),
						BigDecimal.ZERO, tPostAuthLog.getRepayAcctSeq(),
						tPostAuthLog.getPostAuthLogId().getAuthApplCode(), titaVo);

				tBankAuthActId.setAuthType("02");

//				戶號 額度 授權類別 扣款銀行 郵局存款別 扣款帳號 狀態碼 每筆扣款限額 帳號碼 授權代號
				updateBankAuthAct(tBankAuthActId, "700", tPostAuthLog.getPostDepCode(),
						tPostAuthLog.getPostAuthLogId().getRepayAcct(), tPostAuthLog.getAuthErrorCode(),
						BigDecimal.ZERO, tPostAuthLog.getRepayAcctSeq(),
						tPostAuthLog.getPostAuthLogId().getAuthApplCode(), titaVo);
			} else {
				this.info("不為申請");
			}
		}
	}

//	戶號 額度 授權類別 扣款銀行 郵局存款別 扣款帳號 狀態碼 每筆扣款限額 帳號碼 授權代號
	private void updateBankAuthAct(BankAuthActId tBankAuthActId, String repayBank, String depCode, String repayAcct,
			String errorCode, BigDecimal limitAmt, String seq, String flag, TitaVo titaVo) throws LogicException {

		this.info("updateBankAuthAct Start ... ");
		this.info("repayBank ... " + repayBank);
		this.info("depCode ... " + depCode);
		this.info("repayAcct ... " + repayAcct);
		this.info("errorCode ... " + errorCode);
		this.info("limitAmt ... " + limitAmt);
		this.info("seq ... " + seq);
		this.info("flag ... " + flag);

		BankAuthAct tBankAuthAct = new BankAuthAct();
		tBankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);

		if (tBankAuthAct == null) {
			this.info("查無此戶號、額度 1 ");
			return;
//			throw new LogicException("E0001", "BankAuthAct null");
		}

		int enterFlag = 0;
		int checkFlag = 0;

		if ("700".equals(tBankAuthAct.getRepayBank()) && "2".equals(flag) && "00".equals(errorCode)) {
			enterFlag = 1;
		}

		if (!"700".equals(tBankAuthAct.getRepayBank()) && "D".equals(flag) && "0".equals(errorCode)) {
			enterFlag = 1;
		}

		this.info("enterFlag ... " + enterFlag);

		if (enterFlag == 0) {
//			更換帳號 郵局換ACH 或ACH換郵局 需將舊的刪除，否則扣帳檔抓取會重複
			checkFlag = checkBankAuthAct(tBankAuthAct, repayBank, titaVo);
		}

		this.info("checkFlag ... " + checkFlag);

		if (checkFlag == 1) {
			tBankAuthAct = new BankAuthAct();
			tBankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);

			if (tBankAuthAct == null) {
				this.info("查無此戶號、額度 2 ");
				return;
			}
		}

		this.info("tBankAuthAct.getRepayBank() ... " + tBankAuthAct.getRepayBank());

//		空白:未授權
//		0:授權成功       授權提回更新      
//		1:停止使用       0:授權成功時維護；恢復=>維護回0:授權成功
//		2.取消授權       授權提回更新 
//		9:已送出授權
		if ("700".equals(tBankAuthAct.getRepayBank())) {
			if ("00".equals(errorCode)) {
				if ("2".equals(flag)) {
					tBankAuthAct.setStatus("2");
					tBankAuthAct.setRepayAcct(repayAcct);
					tBankAuthAct.setRepayBank(repayBank);
					if (seq != null) {
						tBankAuthAct.setAcctSeq(seq);
					}
					tBankAuthAct.setPostDepCode(depCode);
				} else {
					tBankAuthAct.setStatus("0");
					tBankAuthAct.setRepayAcct(repayAcct);
					tBankAuthAct.setRepayBank(repayBank);
					if (seq != null) {
						tBankAuthAct.setAcctSeq(seq);
					}
					tBankAuthAct.setPostDepCode(depCode);
				}
			} else {
				if ("9".equals(tBankAuthAct.getStatus())) {
					tBankAuthAct.setStatus(" ");
				}
			}
		} else {
			if ("0".equals(errorCode)) {
				if ("D".equals(flag)) {
					tBankAuthAct.setStatus("2");
					tBankAuthAct.setRepayAcct(repayAcct);
					tBankAuthAct.setRepayBank(repayBank);
					tBankAuthAct.setLimitAmt(limitAmt);
				} else {
					tBankAuthAct.setStatus("0");
					tBankAuthAct.setRepayAcct(repayAcct);
					tBankAuthAct.setRepayBank(repayBank);
					tBankAuthAct.setLimitAmt(limitAmt);
				}
			} else {
				if ("9".equals(tBankAuthAct.getStatus())) {
					tBankAuthAct.setStatus(" ");
				}
			}
		}

		try {
			this.info("tBankAuthAct ..." + tBankAuthAct.toString());
			bankAuthActService.update(tBankAuthAct, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "BankAuthAct update error : " + e.getErrorMsg());
		}
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

//	 更換帳號 郵局換ACH 或ACH換郵局 需將舊的刪除，否則扣帳檔抓取會重複
	private int checkBankAuthAct(BankAuthAct tBankAuthAct, String repayBank, TitaVo titaVo) throws LogicException {
		int flag = 0;
		this.info("tBankAuthAct.getRepayBank() ... " + tBankAuthAct.getRepayBank());
		this.info("repayBank ... " + repayBank);

		if (tBankAuthAct.getRepayBank() == null || repayBank == null) {
			this.info("null return ... ");
			return 9;
		}

		Slice<BankAuthAct> sBankAuthAct = bankAuthActService.facmNoEq(tBankAuthAct.getCustNo(),
				tBankAuthAct.getFacmNo(), this.index, this.limit, titaVo);

		List<BankAuthAct> lBankAuthAct = new ArrayList<BankAuthAct>();

		lBankAuthAct = sBankAuthAct == null ? null : sBankAuthAct.getContent();

		if (lBankAuthAct != null && lBankAuthAct.size() != 0) {
			for (BankAuthAct t2BankAuthAct : lBankAuthAct) {
				if ("700".equals(t2BankAuthAct.getRepayBank())) {
					if ("700".equals(repayBank)) {
						this.info("同為郵局不刪除 ... ");
						continue;
					}
				} else {
					if (!"700".equals(repayBank)) {
						this.info("同為ACH不刪除 ... ");
						continue;
					}
				}

				flag = 1;

				try {
					bankAuthActService.delete(t2BankAuthAct);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
		} else {
			this.info("lBankAuthAct null ... ");
		}

		return flag;
	}

	private void setOutput(AchAuthLog tAchAuthLog, TitaVo titaVo) throws LogicException {
		if ("103".equals(tAchAuthLog.getRepayBank())) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOCustNoA", tAchAuthLog.getCustNo());
			occursList.putParam("OOFacmNoA", tAchAuthLog.getFacmNo());
			occursList.putParam("OORepayAcctA", tAchAuthLog.getRepayAcct());
			occursList.putParam("OOAuthStatusA", tAchAuthLog.getAuthStatus());
			occursList.putParam("OOCreateFlagA", tAchAuthLog.getCreateFlag());

			this.totaA.addOccursList(occursList);
			cntA++;
		} else {
			OccursList occursList = new OccursList();
			occursList.putParam("OOCustNoB", tAchAuthLog.getCustNo());
			occursList.putParam("OOFacmNoB", tAchAuthLog.getFacmNo());
			occursList.putParam("OORepayBankXB", bankX(tAchAuthLog.getRepayBank(), titaVo).substring(0, 8));
			occursList.putParam("OORepayAcctB", tAchAuthLog.getRepayAcct());
			occursList.putParam("OOAuthStatusB", tAchAuthLog.getAuthStatus());
			occursList.putParam("OOCreateFlagB", tAchAuthLog.getCreateFlag());

			this.totaB.addOccursList(occursList);
			cntB++;
		}
	}

	private void setOutput(PostAuthLog tPostAuthLog, TitaVo titaVo) throws LogicException {
		OccursList occursList = new OccursList();
		occursList.putParam("OOCustNoC", tPostAuthLog.getCustNo());
		occursList.putParam("OOFacmNoC", tPostAuthLog.getFacmNo());
		occursList.putParam("OORepayAcctC", tPostAuthLog.getRepayAcct());
		occursList.putParam("OOStampCodeC", tPostAuthLog.getStampCode());
		occursList.putParam("OOAuthErrorCodeC", tPostAuthLog.getAuthErrorCode());

		/* 將每筆資料放入Tota的OcList */
		this.totaC.addOccursList(occursList);
		cntC++;
	}
}