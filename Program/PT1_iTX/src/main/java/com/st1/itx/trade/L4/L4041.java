package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.BankAuthActId;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogId;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.db.service.springjpa.cm.L4041ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.SortMapListCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.PostAuthFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4041")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4041 extends TradeBuffer {

	private int aCnt = 0;
	private int bCnt = 0;
	private int MediaDate = 0;

	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public L4041ServiceImpl l4041ServiceImpl;

	@Autowired
	public PostAuthLogService postAuthLogService;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public BankAuthActService bankAuthActService;
	@Autowired
	SortMapListCom sortMapListCom;

	@Autowired
	L4041Report l4041Report;

	private int authCreateDate = 0;
	private int processDate = 0;
	private int stampFinishDate = 0;
	private int propDate = 0;
	private int retrDate = 0;
	private int deleteDate = 0;
	private int relAcctBirthday = 0;
	private TitaVo txtitaVo;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4041 ");
		this.totaVo.init(titaVo);
//		FunctionCode 執行動作
//		1.篩選資料 -> 查詢條件A -> CustNo戶號 || PropDate提出日期 擇一輸入  => MediaCode=Y
//      2.產出媒體檔 -> MediaCode==Y && StampFinishDate ==0 => 寫txt並且產出媒體檔
//      3.重製媒體碼 -> MediaCode==Y && StampFinishDate ==0 => MediaCode=null	
//		CreateFlag 查詢條件B
//	    1.新增 -> 授權狀態為空者
//      2.終止 -> 授權狀態為成功者

		int iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
		int iAuthApplCode = parse.stringToInteger(titaVo.getParam("AuthApplCode"));
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iPropDate = parse.stringToInteger(titaVo.getParam("PropDate"));

		this.info("iPropDate : " + iPropDate);

		if (iPropDate != 0) {
			iPropDate = iPropDate + 19110000;
		}

		this.info("iFunctionCode : " + iFunctionCode);
		this.info("iAuthApplCode : " + iAuthApplCode);
		this.info("iCustNo : " + iCustNo);
		this.info("iPropDate : " + iPropDate);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		totaVo.put("PdfSno846", "0");
		totaVo.put("PdfSno53N", "0");

		int nPropDate = dateUtil.getNowIntegerForBC();

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> resultList1 = new ArrayList<Map<String, String>>();

		switch (iFunctionCode) {
		case 1:
			this.info("case1!!");
//			郵局不可一日多批,篩選資料檢查本日是否以產出媒體檔
			txtitaVo = new TitaVo();
			txtitaVo = (TitaVo) titaVo.clone();
			txtitaVo.putParam("FunctionCode", "3");
			try {
				// *** 折返控制相關 ***
				resultList = l4041ServiceImpl.findAll(dateUtil.getNowIntegerForBC() + 19110000, this.index,
						Integer.MAX_VALUE, txtitaVo);
			} catch (Exception e) {
				this.error("l4920ServiceImpl findByCondition " + e.getMessage());
				throw new LogicException("E0013", e.getMessage());
			}

			if (resultList != null && resultList.size() != 0) {
				throw new LogicException(titaVo, "E0010", "不可一日多批"); // 功能選擇錯誤
			}

		}

		resultList = new ArrayList<Map<String, String>>();
		try {
			// *** 折返控制相關 ***
			resultList = l4041ServiceImpl.findAll(nPropDate, this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l4041ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		switch (iFunctionCode) {
		case 1:
			if (resultList != null && resultList.size() != 0) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				if (resultList.size() == this.limit && hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				}
				// 篩選資料 set MediaCode=Y ProcessDate = Today or SearchDate
				for (Map<String, String> result : resultList) {
					setDate(result);

					OccursList occursListOutput = new OccursList();
					occursListOutput.putParam("OOCustNo", result.get("F2"));
					occursListOutput.putParam("OOFacmNo", result.get("F6"));
					occursListOutput.putParam("OOAuthCreateDate", authCreateDate);
					occursListOutput.putParam("OOAuthApplCode", result.get("F1"));
					occursListOutput.putParam("OOPostDepCode", result.get("F3"));
					occursListOutput.putParam("OORepayAcct", result.get("F4"));
					occursListOutput.putParam("OOPropDate", propDate);
					occursListOutput.putParam("OORetrDate", retrDate);
					occursListOutput.putParam("OOAuthCode", result.get("F5"));
					occursListOutput.putParam("OOAuthErrorCode", result.get("F14"));

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursListOutput);
				}
			} else {
				throw new LogicException(titaVo, "CE001", "查無資料");
			}
			break;
		case 2:
			this.info("case2!!");
			// 產出媒體檔 input output FileVo
			// RepayBank=103(新光) -> AH$P21P
			// else -> AH$P22P

			aCnt = 0;
			ArrayList<OccursList> aTmp = new ArrayList<>();
			ArrayList<OccursList> bTmp = new ArrayList<>();

			if (resultList != null && resultList.size() != 0) {
				this.info("For Loop Start !*!*!*! ");
				int cnt = 0;
				String tempCheckAcctNo = "";

				for (Map<String, String> result : resultList) {
					setDate(result);

					this.info("AuthCreateDate : " + authCreateDate);
					this.info("CustNo : " + result.get("F2"));
					this.info("RepayAcct : " + result.get("F4"));
					this.info("AuthErrorCode : " + result.get("F14"));
					this.info("PostMediaCode : " + result.get("F13"));
					this.info("PropDate : " + propDate);
					this.info("tempCheckAcctNo : " + tempCheckAcctNo);
					this.info("F5 : " + result.get("F5"));

					if (!"Y".equals(result.get("F13")) && propDate > 0) {
						cnt = cnt + 1;

						if ("1".equals(result.get("F5"))) {
							aCnt++;
						}
						if ("2".equals(result.get("F5"))) {
							bCnt++;
						}

						MediaDate = dateUtil.getNowIntegerForBC();

						OccursList occursList = new OccursList();
//	1   OccDataClass    資料別		0-1		X(1)	固定值為1	
//	2   OccOrgCode  	委託機構代號	1-4		X(3)	大寫英數字	
//	3   OccNoteA    	保留欄		4-8		X(4)	空白	
//	4   OccMediaDate    媒體產生日期	8-16	X(8)	西元年月日YYYYMMDD	
//	5   OccBatchNo  	批號			16-19	9(3)	固定值為001 	
//	6   OccDataSeq  	流水號		19-25	9(6)	每批自000001序編	
//	7   OccApprCode 	申請代號		25-26	X(1)	"委託機構送件：1：申請2：終止"	"郵局回送「帳戶至郵局辦理終止」檔：3：郵局終止4：誤終止-已回復為申請"
//	8   OccAcctType 	帳戶別		26-27	X(1)	P：存簿    G：劃撥	
//	9   OccRepayAcct    儲金帳號		27-41	9(14)	存簿：局帳號計14碼   劃撥：000000+8碼帳號	
//	10  OccCustNo   	用戶編號		41-61	X(20)	"右靠左補空，大寫英數字，不得填寫中文由委託機構自行編給其客戶之編號"	
//	11  OccCustId   	統一證號		61-71	X(10)	左靠右補空白	
//	12  OccStatusCode   狀況代號		71-73	X(2)	初始值為空白，回送資料請參閱媒體資料不符代號一覽表	
//	13  OccCheckInd 	核對註記		73-74	X(1)	初始值為空白，回送資料請參閱媒體資料不符代號一覽表	
//	14  OccNoteB    	保留欄		74-100	X(26)	空白	

						occursList.putParam("OccDataClass", "1");
						occursList.putParam("OccOrgCode", "846");
						occursList.putParam("OccNoteA", FormatUtil.padX("", 4));
						occursList.putParam("OccMediaDate", FormatUtil.pad9("" + MediaDate, 8));
						occursList.putParam("OccBatchNo", FormatUtil.pad9("1", 3));
						occursList.putParam("OccApprCode", result.get("F1"));
						occursList.putParam("OccAcctType", result.get("F3"));
						occursList.putParam("OccRepayAcct", FormatUtil.pad9("" + result.get("F4"), 14));
//						(new)帳號碼(文字2位) + 扣款人ID+郵局存款別+戶號
						String acctSeq = "";
						if (result.get("F8") != null && !"".equals(result.get("F8").trim())) {
							acctSeq = FormatUtil.pad9("" + result.get("F8"), 2);
						} else {
							acctSeq = FormatUtil.padX("", 2);
						}
						occursList.putParam("OccCustNo",
								FormatUtil.padLeft(acctSeq + FormatUtil.padX(result.get("F7"), 10) + result.get("F3")
										+ FormatUtil.pad9(result.get("F2"), 7), 20));
						occursList.putParam("OccCustId", FormatUtil.padX(result.get("F7"), 10));
						occursList.putParam("OccStatusCode", FormatUtil.padX("", 2));
						occursList.putParam("OccCheckInd", FormatUtil.padX("", 1));
						occursList.putParam("OccNoteB", FormatUtil.padX("", 26));

						if ("1".equals(result.get("F5"))) {
							occursList.putParam("OccDataSeq", FormatUtil.pad9("" + aCnt, 6));
							aTmp.add(occursList);
						}
						if ("2".equals(result.get("F5"))) {
							occursList.putParam("OccDataSeq", FormatUtil.pad9("" + bCnt, 6));
							occursList.putParam("OccOrgCode", "53N");
							bTmp.add(occursList);
						}

						OccursList occursListOutput = new OccursList();
						occursListOutput.putParam("OOCustNo", result.get("F2"));
						occursListOutput.putParam("OOFacmNo", result.get("F6"));
						occursListOutput.putParam("OOAuthCreateDate", authCreateDate);
						occursListOutput.putParam("OOAuthApplCode", result.get("F1"));
						occursListOutput.putParam("OOPostDepCode", result.get("F3"));
						occursListOutput.putParam("OORepayAcct", result.get("F4"));
						occursListOutput.putParam("OOPropDate", propDate);
						occursListOutput.putParam("OORetrDate", retrDate);
						occursListOutput.putParam("OOAuthCode", result.get("F5"));
						occursListOutput.putParam("OOAuthErrorCode", result.get("F14"));

						/* 將每筆資料放入Tota的OcList */
						this.totaVo.addOccursList(occursListOutput);
						PostAuthLogId tPostAuthLogId = new PostAuthLogId();
						tPostAuthLogId.setAuthCreateDate(authCreateDate);
						tPostAuthLogId.setAuthApplCode(result.get("F1"));
						tPostAuthLogId.setCustNo(parse.stringToInteger(result.get("F2")));
						tPostAuthLogId.setPostDepCode(result.get("F3"));
						tPostAuthLogId.setRepayAcct(result.get("F4"));
						tPostAuthLogId.setAuthCode(result.get("F5"));

						PostAuthLog tempPostAuthLog = postAuthLogService.holdById(tPostAuthLogId, titaVo);

						tempPostAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
						tempPostAuthLog.setPropDate(dateUtil.getNowIntegerForBC());
						tempPostAuthLog.setPostMediaCode("Y");

						if ("1".equals(tempPostAuthLog.getAuthCode())) {
							tempPostAuthLog.setFileSeq(aCnt);
						}
						if ("2".equals(tempPostAuthLog.getAuthCode())) {
							tempPostAuthLog.setFileSeq(bCnt);
						}

						try {
							// 送出到DB
							if (tempPostAuthLog != null) {
								postAuthLogService.update(tempPostAuthLog, titaVo);
							}
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", "L4041 PostAuthLog update " + e.getErrorMsg());
						}

						BankAuthAct tBankAuthAct = new BankAuthAct();
						BankAuthActId tBankAuthActId = new BankAuthActId();

						tBankAuthActId.setCustNo(parse.stringToInteger(result.get("F2")));
						tBankAuthActId.setFacmNo(parse.stringToInteger(result.get("F6")));
						tBankAuthActId.setAuthType("01");

						tBankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);

//						更新為已送出授權
						if (tBankAuthAct != null) {
							if ("".equals(tBankAuthAct.getStatus().trim())) {
								tBankAuthAct.setStatus("9");
								try {
									bankAuthActService.update(tBankAuthAct, titaVo);
								} catch (DBException e) {
									throw new LogicException("E0007", "BankAuthAct update error : " + e.getErrorMsg());
								}
							}
						}

						tBankAuthActId.setAuthType("02");

						tBankAuthAct = bankAuthActService.holdById(tBankAuthActId, titaVo);

//						更新為已送出授權
						if (tBankAuthAct != null) {
							if ("".equals(tBankAuthAct.getStatus().trim())) {
								tBankAuthAct.setStatus("9");
								try {
									bankAuthActService.update(tBankAuthAct, titaVo);
								} catch (DBException e) {
									throw new LogicException("E0007", "BankAuthAct update error : " + e.getErrorMsg());
								}
							}
						}

						TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
						tTxToDoDetailId.setCustNo(tempPostAuthLog.getCustNo());
						tTxToDoDetailId.setFacmNo(tempPostAuthLog.getFacmNo());
						tTxToDoDetailId.setBormNo(0);
						tTxToDoDetailId.setDtlValue(FormatUtil.pad9(tempPostAuthLog.getRepayAcct(), 14));
						tTxToDoDetailId.setItemCode("ACHP00");

						txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);

					} else {
						this.info("continue !*!*!*! ");
						continue;
					}
				}
				this.info("cnt = " + cnt);

				if (cnt == 0) {
					throw new LogicException(titaVo, "CE001", "查無資料");
				}

//				Footer
//				1	FootDataClass   資料別		0-1		X(1)	固定值為2	
//				2	FootOrgCode 	委託機構代號	1-4		X(3)	同明細	
//				3	FootNoteA   	保留欄		4-8		X(4)	空白	
//				4	FootMediaDate   媒體產生日期	8-16	X(8)	同明細	
//				5	FootBatchNo 	批號			16-19	9(3)	同明細	
//				6	FootCreateFlag  建檔記號		19-20	X(1)	固定值B：委託機構送件	固定值F：郵局回送「帳戶至郵局辦理終止」檔	
//				7	FootDataCnt 	總筆數		20-26	9(6)	右靠左補0	
//				8	FootCreateDate  資料建檔日期	26-34	X(8)	初始值為空白，	郵局回送「帳戶至郵局辦理終止」檔：空白回送時使用	
//				9	FootErrorCnt    錯誤筆數		34-40	9(6)	初始值為0，回送時使用	
//				10	FootSuccsCnt    成功筆數		40-46	9(6)	初始值為0，回送時使用	
//				11	FootNoteB   	保留欄		46-100	X(54)		

				PostAuthFileVo postAuthFileVo846 = new PostAuthFileVo();

				postAuthFileVo846.put("FootDataClass", "2");
				postAuthFileVo846.put("FootOrgCode", "846");
				postAuthFileVo846.put("FootNoteA", FormatUtil.padX("", 4));
				postAuthFileVo846.put("FootMediaDate", FormatUtil.pad9((MediaDate) + "", 8));
				postAuthFileVo846.put("FootBatchNo", FormatUtil.pad9("1", 3));
				postAuthFileVo846.put("FootCreateFlag", "B");
				postAuthFileVo846.put("FootDataCnt", FormatUtil.pad9("" + aCnt, 6));
				postAuthFileVo846.put("FootCreateDate", FormatUtil.padX("", 8));
				postAuthFileVo846.put("FootErrorCnt", FormatUtil.pad9("", 6));
				postAuthFileVo846.put("FootSuccsCnt", FormatUtil.pad9("", 6));
				postAuthFileVo846.put("FootNoteB", FormatUtil.padX("", 54));

				// 把明細資料容器裝到檔案資料容器內

				postAuthFileVo846.setOccursList(aTmp);
				// 轉換資料格式
				ArrayList<String> aFile = postAuthFileVo846.toFile();

				makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
						titaVo.getTxCode() + "-郵局授權提出媒體檔846", "PO$P11P_846授權出.txt", 2);

				for (String line : aFile) {
					makeFile.put(line);
				}

				long sno = makeFile.close();

				this.info("sno : " + sno);

				makeFile.toFile(sno);
				totaVo.put("PdfSno846", "" + sno);

				PostAuthFileVo postAuthFileVo53N = new PostAuthFileVo();

				postAuthFileVo53N.put("FootDataClass", "2");
				postAuthFileVo53N.put("FootOrgCode", "53N");
				postAuthFileVo53N.put("FootNoteA", FormatUtil.padX("", 4));
				postAuthFileVo53N.put("FootMediaDate", FormatUtil.pad9((MediaDate) + "", 8));
				postAuthFileVo53N.put("FootBatchNo", FormatUtil.pad9("1", 3));
				postAuthFileVo53N.put("FootCreateFlag", "B");
				postAuthFileVo53N.put("FootDataCnt", FormatUtil.pad9("" + bCnt, 6));
				postAuthFileVo53N.put("FootCreateDate", FormatUtil.padX("", 8));
				postAuthFileVo53N.put("FootErrorCnt", FormatUtil.pad9("", 6));
				postAuthFileVo53N.put("FootSuccsCnt", FormatUtil.pad9("", 6));
				postAuthFileVo53N.put("FootNoteB", FormatUtil.padX("", 54));

				// 把明細資料容器裝到檔案資料容器內
				postAuthFileVo53N.setOccursList(bTmp);

				// 轉換資料格式
				ArrayList<String> bFile = postAuthFileVo53N.toFile();

				makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
						titaVo.getTxCode() + "-郵局授權提出媒體檔53N", "PO$P12P_53N授權出.txt", 2);

				for (String line : bFile) {
					makeFile.put(line);
				}

				sno = makeFile.close();

				this.info("sno : " + sno);

				resultList1 = sortMapListCom.beginSort(resultList).ascString("F5").ascNumber("F2").getList();
				makeFile.toFile(sno);
				totaVo.put("PdfSno53N", "" + sno);

				l4041Report.setParentTranCode(titaVo.getTxcd());

//				l4041Report.exec(resultList, titaVo);
				l4041Report.exec(resultList1, titaVo);
				sno = l4041Report.close();
				l4041Report.toPdf(sno);

				totaVo.put("PdfSno", "" + sno);
			} else {
				throw new LogicException(titaVo, "CE001", "查無資料");
			}
			break;
		case 3:
			this.info("case3!!");
			// 重製媒體碼 set MediaCode=" "

			if (resultList != null && resultList.size() != 0) {
				int cnt = 0;
				for (Map<String, String> result : resultList) {
					if ("".equals(result.get("F14").trim())) {
						setDate(result);
						cnt = cnt + 1;

						OccursList occursListOutput = new OccursList();
						occursListOutput.putParam("OOCustNo", result.get("F2"));
						occursListOutput.putParam("OOFacmNo", result.get("F6"));
						occursListOutput.putParam("OOAuthCreateDate", authCreateDate);
						occursListOutput.putParam("OOAuthApplCode", result.get("F1"));
						occursListOutput.putParam("OOPostDepCode", result.get("F3"));
						occursListOutput.putParam("OORepayAcct", result.get("F4"));
						occursListOutput.putParam("OOPropDate", propDate);
						occursListOutput.putParam("OORetrDate", retrDate);
						occursListOutput.putParam("OOAuthCode", result.get("F5"));
						occursListOutput.putParam("OOAuthErrorCode", result.get("F14"));
						occursListOutput.putParam("OOStampFinishDate", stampFinishDate);

						/* 將每筆資料放入Tota的OcList */
						this.totaVo.addOccursList(occursListOutput);
						PostAuthLogId tPostAuthLogId = new PostAuthLogId();
						tPostAuthLogId.setAuthCreateDate(authCreateDate);
						tPostAuthLogId.setAuthApplCode(result.get("F1"));
						tPostAuthLogId.setCustNo(parse.stringToInteger(result.get("F2")));
						tPostAuthLogId.setPostDepCode(result.get("F3"));
						tPostAuthLogId.setRepayAcct(result.get("F4"));
						tPostAuthLogId.setAuthCode(result.get("F5"));

						PostAuthLog newPostAuthLog = postAuthLogService.holdById(tPostAuthLogId, titaVo);

						newPostAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
						if (result.get("F1").equals("1")) {
							newPostAuthLog.setPropDate(0);
						}
						newPostAuthLog.setPostMediaCode("");
						newPostAuthLog.setFileSeq(0);

						try {
							postAuthLogService.update(newPostAuthLog, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", "L4041 PostAuthLog update " + e.getErrorMsg());
						}

						TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
						tTxToDoDetailId.setCustNo(newPostAuthLog.getCustNo());
						tTxToDoDetailId.setFacmNo(newPostAuthLog.getFacmNo());
						tTxToDoDetailId.setBormNo(0);
						tTxToDoDetailId.setDtlValue(FormatUtil.pad9(newPostAuthLog.getRepayAcct(), 14));
						tTxToDoDetailId.setItemCode("ACHP00");

						txToDoCom.updDetailStatus(0, tTxToDoDetailId, titaVo);
					}
					if (cnt == 0) {
						throw new LogicException(titaVo, "CE001", "查無資料");
					}
				}
			} else {
				throw new LogicException(titaVo, "CE001", "查無資料");
			}
			break;
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l4041ServiceImpl.getSize();
		int size = times * this.limit;

		this.info("index ..." + this.index);
		this.info("times ..." + times);
		this.info("cnt ..." + cnt);
		this.info("size ..." + size);

		if (size == cnt) {
			result = false;
		}
		this.info("result ..." + result);

		return result;
	}

	private void setDate(Map<String, String> result) throws LogicException {
		authCreateDate = parse.stringToInteger(result.get("F0"));
		processDate = parse.stringToInteger(result.get("F9"));
		stampFinishDate = parse.stringToInteger(result.get("F10"));
		propDate = parse.stringToInteger(result.get("F16"));
		retrDate = parse.stringToInteger(result.get("F17"));
		deleteDate = parse.stringToInteger(result.get("F18"));
		relAcctBirthday = parse.stringToInteger(result.get("F22"));

		if (authCreateDate > 19110000) {
			authCreateDate = authCreateDate - 19110000;
		}
		if (processDate > 19110000) {
			processDate = processDate - 19110000;
		}
		if (stampFinishDate > 19110000) {
			stampFinishDate = stampFinishDate - 19110000;
		}
		if (propDate > 19110000) {
			propDate = propDate - 19110000;
		}
		if (retrDate > 19110000) {
			retrDate = retrDate - 19110000;
		}
		if (deleteDate > 19110000) {
			deleteDate = deleteDate - 19110000;
		}
		if (relAcctBirthday > 19110000) {
			relAcctBirthday = relAcctBirthday - 19110000;
		}

		this.info("authCreateDate ... " + authCreateDate);
		this.info("processDate ... " + processDate);
		this.info("stampFinishDate ... " + stampFinishDate);
		this.info("propDate ... " + propDate);
		this.info("retrDate ... " + retrDate);
		this.info("deleteDate ... " + deleteDate);
		this.info("relAcctBirthday ... " + relAcctBirthday);
	}

}