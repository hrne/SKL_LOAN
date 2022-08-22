package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.AchAuthLogId;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.BankAuthActId;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.springjpa.cm.L4040ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.AchAuthFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4040")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4040 extends TradeBuffer {

	private int aCnt = 0;
	private int bCnt = 0;

	@Autowired
	DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public AchAuthFileVo achAuthFileVo;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public L4040ServiceImpl l4040ServiceImpl;

	@Autowired
	public AchAuthLogService achAuthLogService;

	@Autowired
	public BankAuthActService bankAuthActService;

	@Autowired
	public CdCodeService cdCodeService;
	@Autowired
	L4040Report l4040Report;

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
		this.info("active L4040 ");
		this.totaVo.init(titaVo);
		txToDoCom.setTxBuffer(txBuffer);

//		FunctionCode 執行動作
//  	  1.篩選資料 -> 查詢條件A -> CustNo戶號 || PropDate提出日期 擇一輸入  => MediaCode=Y

//        2.產出媒體檔 -> MediaCode==Y && StampFinishDate ==0 => 寫txt並且產出媒體檔
//		              (判斷系統參數SystemParas.AchAuthOneTime,if == Y,需判斷當天無媒體記號為Y，始可執行)

//        3.重製媒體碼 -> MediaCode==Y && StampFinishDate ==0 => MediaCode=null	
//       	    	  (判斷系統參數SystemParas.AchAuthOneTime,if == Y,目前最大那批)
//		CreateFlag 查詢條件B
//		  1.新增授權 -> 授權狀態為空者
//        2.再次授權 -> 授權狀態為失敗
//        3.取消授權 -> 授權狀態為成功者

		int iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
		int iCreateFlag = parse.stringToInteger(titaVo.getParam("CreateFlag"));
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int iPropDate = parse.stringToInteger(titaVo.getParam("PropDate"));
		if (iPropDate != 0) {
			iPropDate = iPropDate + 19110000;
		}
		int iRepayBank = parse.stringToInteger(titaVo.getParam("RepayBank"));
		String batchNo = "";
		if (iRepayBank == 0) {
			batchNo = "00";
		} else if (iRepayBank == 1) {
			batchNo = "01";
		} else if (iRepayBank == 2) {
			batchNo = "02";
		}
		String lastAuthSeq = "";
		this.info("iFunctionCode : " + iFunctionCode);
		this.info("iCreateFlag : " + iCreateFlag);
		this.info("iCustNo : " + iCustNo);
		this.info("iPropDate : " + iPropDate);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		if (iFunctionCode == 1) {
			this.limit = 500;
		} else {
			this.limit = Integer.MAX_VALUE;
		}

		totaVo.put("PdfSno103", "0");
		totaVo.put("PdfSno998", "0");
		totaVo.put("PdfSno", "0");

		int nPropDate = dateUtil.getNowIntegerForBC();

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		switch (iFunctionCode) {
		case 1:
		case 2:
			this.info("case1!!");
//			不可一日多批時,篩選資料檢查本日是否以產出媒體檔
			if ("Y".equals(this.getTxBuffer().getSystemParas().getAchAuthOneTime())) {
				txtitaVo = new TitaVo();
				txtitaVo = (TitaVo) titaVo.clone();
				txtitaVo.putParam("FunctionCode", "3");
				try {
					// *** 折返控制相關 ***
					resultList = l4040ServiceImpl.findAll(nPropDate, this.index, Integer.MAX_VALUE, txtitaVo);
				} catch (Exception e) {
					this.error("l4040ServiceImpl findByCondition " + e.getMessage());
					throw new LogicException("E0013", e.getMessage());
				}

				if (resultList != null && resultList.size() != 0) {
					throw new LogicException(titaVo, "E0010", "不可一日多批"); // 功能選擇錯誤
				}
			}
		}

		resultList = new ArrayList<Map<String, String>>();
		try {
			// *** 折返控制相關 ***
			resultList = l4040ServiceImpl.findAll(0, this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l4040ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		switch (iFunctionCode) {
		case 1:
			this.info("case1!!");

		case 3:
			this.info("case3!!");

			if (resultList != null && resultList.size() != 0) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				if (resultList.size() == this.limit && hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				}

				// 重製需判斷扣款銀行來重製
				// 找BATCHNO最大的
				if (iFunctionCode == 3) {

					Slice<AchAuthLog> slAchAuthLog = achAuthLogService.propBatchNoEq(nPropDate, "Au" + batchNo + "%", 0,
							Integer.MAX_VALUE, titaVo);
					if (slAchAuthLog != null && slAchAuthLog.getContent().get(0).getBatchNo().length() >= 6) {
						lastAuthSeq = slAchAuthLog.getContent().get(0).getBatchNo();
					}
				}
				for (Map<String, String> result : resultList) {
					setData(result);

					// 重製需判斷扣款銀行來重製最後一筆
					if (iFunctionCode == 3) {
						if (!result.get("F13").equals(lastAuthSeq)) {
							continue;
						}
					}
					OccursList occursListOutput = new OccursList();
					occursListOutput.putParam("OOAuthCreateDate", authCreateDate);
					occursListOutput.putParam("OOCustNo", result.get("F2"));
					occursListOutput.putParam("OORepayBank", result.get("F3"));
					occursListOutput.putParam("OORepayAcct", result.get("F4"));
					occursListOutput.putParam("OOCreateFlag", result.get("F5"));
					occursListOutput.putParam("OOPropDate", propDate);
					occursListOutput.putParam("OOFacmNo", result.get("F6"));
					occursListOutput.putParam("OOProcessDate", processDate);
					occursListOutput.putParam("OORetrDate", retrDate);
					occursListOutput.putParam("OOAuthStatus", result.get("F9"));

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursListOutput);

					if (iFunctionCode == 3) {

						AchAuthLogId tAchAuthLogId = new AchAuthLogId();
						tAchAuthLogId.setAuthCreateDate(authCreateDate);
						tAchAuthLogId.setCreateFlag(result.get("F5"));
						tAchAuthLogId.setCustNo(parse.stringToInteger(result.get("F2")));
						tAchAuthLogId.setRepayAcct(result.get("F4"));
						tAchAuthLogId.setRepayBank(result.get("F3"));

						AchAuthLog tAchAuthLog = achAuthLogService.holdById(tAchAuthLogId, titaVo);

						tAchAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
						if ("A".equals(result.get("F5"))) {
							tAchAuthLog.setPropDate(0);
						}
						tAchAuthLog.setBatchNo("");
						tAchAuthLog.setMediaCode("");

						try {
							achAuthLogService.update(tAchAuthLog, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", "L4041 PostAuthLog update " + e.getErrorMsg());
						}
					}
				}
			} else {
				throw new LogicException(titaVo, "E0001", "查無資料");
			}
			break;
		case 2:
			this.info("case2!!");

			// 產出媒體檔 input output FileVo
			// RepayBank=103(新光) -> AH$P21P
			// else -> AH$P22P

//			String outputFilePath22 = outFolder + "AHP22P_授出 - 他行.txt";
//			String outputFilePath21 = outFolder + "AHP21P_授出 - 新光.txt";
			Slice<AchAuthLog> slAchAuthLog = achAuthLogService.propBatchNoEq(nPropDate, "Au" + batchNo + "%", 0,
					Integer.MAX_VALUE, titaVo);
			String AuthSeq = "00";
			// 一日一批時固定00,一日多批時找出最後一筆序號+1 若無則為1
			if (!"Y".equals(this.getTxBuffer().getSystemParas().getAchAuthOneTime())) {
				if (slAchAuthLog == null) {
					AuthSeq = "01";
				} else {
					if (slAchAuthLog.getContent().get(0).getBatchNo().length() >= 6) {
						AuthSeq = FormatUtil.pad9(""
								+ (parse.stringToInteger(slAchAuthLog.getContent().get(0).getBatchNo().substring(4, 6))
										+ 1),
								2);
					} else {
						AuthSeq = "01";
					}
				}
			}

			aCnt = 0;
			bCnt = 0;
			ArrayList<OccursList> aTmp = new ArrayList<>();
			ArrayList<OccursList> bTmp = new ArrayList<>();

			if (resultList != null && resultList.size() != 0) {
				int cnt = 0;
				for (Map<String, String> result : resultList) {
					setData(result);

					this.info("For Loop Start !*!*!*! ");
					this.info("!*!*!*! Size : " + resultList.size());
					this.info("!*!*!*! AuthStatus : " + result.get("F9"));
					this.info("!*!*!*! CustNo : " + result.get("F2"));
					String BankNo3 = "";
					String BankNo7 = "";
					if (!"Y".equals(result.get("F12")) && parse.stringToInteger(result.get("F14")) > 0) {

						cnt = cnt + 1;

						OccursList occursList = new OccursList();
//							2.OccTxCode				交易代號		X	3	9	801
						occursList.putParam("OccTxCode", "801");
//							3.OccSnederId			發動者統一編號	X	10	19	03458902
						occursList.putParam("OccSnederId", FormatUtil.padX("03458902", 10));
//							1.OccTxseq				交易序號		9	6	6	從1開始，靠右左補0
//							4.OccWdBankNo			提回行代號		9	7	26	依扣款銀行代號區分
//																			台新：8120012
//																			合庫：0060567
//																			新光：1030019
						Slice<CdCode> tCdCode = cdCodeService.defCodeEq("BankNo", result.get("F3") + "%", 0, this.limit,
								titaVo);
						if (tCdCode != null) {
							BankNo7 = tCdCode.getContent().get(0).getCode();
							BankNo3 = BankNo7.substring(0, 3);
						} else {
							throw new LogicException("E0015",
									"error RepayBank Code:" + result.get("F3") + ",請先新增代碼:BankNo");
						}

						this.info("BankNo3==" + BankNo3);
						this.info("BankNo7==" + BankNo7);
						if (("103").equals(BankNo3)) {
							aCnt++;
							occursList.putParam("OccTxseq", FormatUtil.pad9("" + aCnt, 6));
						} else {
							bCnt++;
							occursList.putParam("OccTxseq", FormatUtil.pad9("" + bCnt, 6));

						}
						occursList.putParam("OccWdBankNo", BankNo7);

//							5.OccRepayAcct			委繳戶帳號		X	14	40	扣款帳號
						occursList.putParam("OccRepayAcct", FormatUtil.padX(result.get("F4"), 14));
//							6.OccCustId				委繳戶統一編號	X	10	50	身分證字號
						occursList.putParam("OccCustId", FormatUtil.padX(result.get("F19"), 10));
//							7.OccCustNo				用戶號碼		X	20	70	借款人戶號
						occursList.putParam("OccCustNo",
								FormatUtil.padX(FormatUtil.pad9("" + result.get("F2"), 7), 20));
//							8.OccCreateFlag			新增或取消		X	1	71	A：新增(人工紙本)
//																			O：舊檔轉換用"
						occursList.putParam("OccCreateFlag", result.get("F5"));
//							9.OccAuthCreateDate		資料製作日期	9	8	79	
						occursList.putParam("OccPropDate", FormatUtil.pad9("" + propDate, 8));
//							10.OccSnederNo			提出行代號		9	7	86	1030116
						occursList.putParam("OccSnederNo", "1030116");
//							11.OccSenderRemarker	發動者專用區	X	20	106	戶號+額度
						occursList.putParam("OccSenderRemarker", FormatUtil.padX(
								FormatUtil.pad9("" + result.get("F2"), 7) + FormatUtil.pad9("" + result.get("F6"), 3),
								20));
//							12.OccTxType			交易型態		X	1	107	N：提出
						occursList.putParam("OccTxType", "N");
//							13.OccAuthStatus		回覆訊息		X	1	108	提出時填入空白
						occursList.putParam("OccAuthStatus", " ");
//							14.OccLimitAmt			每筆扣款限額	X	8	116	
						String limitAmt = FormatUtil.padLeft("", 8);
//						限額=0放空白
						if (parse.stringToBigDecimal(result.get("F11")).compareTo(BigDecimal.ZERO) == 1) {
							FormatUtil.padLeft("" + result.get("F11"), 8);
						}
						occursList.putParam("OccLimitAmt", limitAmt);
//							15.OccNote				備用			X	4	120	
						occursList.putParam("OccNote", FormatUtil.padX("", 4));

						if (("103").equals(BankNo3)) {
							// 裝進明細資料容器
							aTmp.add(occursList);
							this.info("aTmp 1==" + aTmp.get(0));
							this.info("aTmp 1==" + aTmp.get(0).get("OccPropDate"));
						} else {
							// 裝進明細資料容器
							bTmp.add(occursList);
						}

						OccursList occursListOutput = new OccursList();

						occursListOutput.putParam("OOAuthCreateDate", authCreateDate);
						occursListOutput.putParam("OOCustNo", result.get("F2"));
						occursListOutput.putParam("OORepayBank", result.get("F3"));
						occursListOutput.putParam("OORepayAcct", result.get("F4"));
						occursListOutput.putParam("OOCreateFlag", result.get("F5"));
						occursListOutput.putParam("OOPropDate", propDate);
						occursListOutput.putParam("OOFacmNo", result.get("F6"));
						occursListOutput.putParam("OOProcessDate", processDate);
						occursListOutput.putParam("OORetrDate", retrDate);
						occursListOutput.putParam("OOAuthStatus", result.get("F9"));
						occursListOutput.putParam("OOStampFinishDate", stampFinishDate);
						/* 將每筆資料放入Tota的OcList */
						this.totaVo.addOccursList(occursListOutput);

						AchAuthLogId tAchAuthLogId = new AchAuthLogId();
						tAchAuthLogId.setAuthCreateDate(parse.stringToInteger(result.get("F1")));
						tAchAuthLogId.setCustNo(parse.stringToInteger(result.get("F2")));
						tAchAuthLogId.setRepayBank(result.get("F3"));
						tAchAuthLogId.setRepayAcct(result.get("F4"));
						tAchAuthLogId.setCreateFlag(result.get("F5"));

						AchAuthLog tAchAuthLog = achAuthLogService.holdById(tAchAuthLogId, titaVo);

						tAchAuthLog.setProcessDate(dateUtil.getNowIntegerForBC());
						tAchAuthLog.setPropDate(dateUtil.getNowIntegerForBC());
						tAchAuthLog.setBatchNo("Au" + batchNo + AuthSeq);
						tAchAuthLog.setMediaCode("Y");
						try {
							// 送出到DB
							if (tAchAuthLog != null) {
								achAuthLogService.update(tAchAuthLog, titaVo);
							}
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", "L4040 AchAuthLog update " + e.getErrorMsg());
						}

						BankAuthAct tBankAuthAct = new BankAuthAct();
						BankAuthActId tBankAuthActId = new BankAuthActId();

						tBankAuthActId.setCustNo(parse.stringToInteger(result.get("F2")));
						tBankAuthActId.setFacmNo(parse.stringToInteger(result.get("F6")));
						tBankAuthActId.setAuthType("00");

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

//						2.回寫狀態
						TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
						tTxToDoDetailId.setCustNo(tAchAuthLog.getCustNo());
						tTxToDoDetailId.setFacmNo(tAchAuthLog.getFacmNo());
						tTxToDoDetailId.setBormNo(0);
						tTxToDoDetailId.setDtlValue(FormatUtil.pad9(tAchAuthLog.getRepayAcct(), 14));
						tTxToDoDetailId.setItemCode("ACHP00");

						txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);
					} else {
						this.info("continue !*!*!*! ");
						continue;
					}
				}
				if (cnt == 0) {
					throw new LogicException(titaVo, "CE001", "查無資料");
				}

				// 新光
				if (aTmp != null && aTmp.size() != 0) {
					this.info("aTmp...");

//					header
//					1	HeadIndex		首錄別		X	3	3	BOF
					achAuthFileVo.put("HeadIndex", "BOF");
//					2	HeadDataKCode	資料代號		X	6	9	ACHP02
					achAuthFileVo.put("HeadDataKCode", "ACHP02");
//					3	HeadRocTxday	交易日期		9	8	17	民國 YYYYMMDD
					achAuthFileVo.put("HeadRocTxday", "0" + dateUtil.getNowIntegerRoc());
//					4	HeadSubmitUnit	發送單位代號	9	7	24	代表行代號 1030000
					achAuthFileVo.put("HeadSubmitUnit", "1030000");
//					5	HeadNote		備用			X	96	120	
					achAuthFileVo.put("HeadNote", FormatUtil.padX("", 96));

//					Footer
//					1	FootIndex	尾錄別	X	3	3	EOF
					achAuthFileVo.put("FootIndex", "EOF");
//					2	FootTotCnt	總筆數	9	8	11	
					achAuthFileVo.put("TotaCnt", FormatUtil.pad9("" + aCnt, 8));
//					3	FootNote	備用		X	109	120	
					achAuthFileVo.put("FootNote", FormatUtil.padX("", 109));

					// 把明細資料容器裝到檔案資料容器內
					this.info("aTmp 2==" + aTmp.get(0));
					this.info("aTmp 2==" + aTmp.get(0).get("OccPropDate"));
					achAuthFileVo.setOccursList(aTmp);
					// 轉換資料格式
					ArrayList<String> aFile = achAuthFileVo.toFile();

					makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
							titaVo.getTxCode() + "-ACH授權提出媒體檔-新光", "AHP21P_授出-新光.txt", 2);

					for (String line : aFile) {
						makeFile.put(line);
					}

					long sno = makeFile.close();

					this.info("sno : " + sno);

					makeFile.toFile(sno);
					totaVo.put("PdfSno103", "" + sno);

					achAuthFileVo.clear();
				}
				// 他行
				if (bTmp != null && bTmp.size() != 0) {
					this.info("bTmp...");

					AchAuthFileVo achAuthFileVo = new AchAuthFileVo();
//					header
//					1	HeadIndex		首錄別		X	3	3	BOF
					achAuthFileVo.put("HeadIndex", "BOF");
//					2	HeadDataKCode	資料代號		X	6	9	ACHP02
					achAuthFileVo.put("HeadDataKCode", "ACHP02");
//					3	HeadRocTxday	交易日期		9	8	17	民國 YYYYMMDD
					achAuthFileVo.put("HeadRocTxday", "0" + dateUtil.getNowIntegerRoc());
//					4	HeadSubmitUnit	發送單位代號	9	7	24	代表行代號 1030000
					achAuthFileVo.put("HeadSubmitUnit", "1030000");
//					5	HeadNote		備用			X	96	120	
					achAuthFileVo.put("HeadNote", FormatUtil.padX("", 96));

					// 把明細資料容器裝到檔案資料容器內
					achAuthFileVo.setOccursList(bTmp);

//					Footer
//					1	FootIndex	尾錄別	X	3	3	EOF
					achAuthFileVo.put("FootIndex", "EOF");
//					2	FootTotCnt	總筆數	9	8	11	
					achAuthFileVo.put("TotbCnt", FormatUtil.pad9("" + bCnt, 9));
//					3	FootNote	備用		X	109	120	
					achAuthFileVo.put("FootNote", FormatUtil.padX("", 109));

					// 轉換資料格式
					ArrayList<String> bFile = achAuthFileVo.toFile();

					makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
							titaVo.getTxCode() + "-ACH授權提出媒體檔-他行", "AHP22P_授出-他行.txt", 2);

					for (String line : bFile) {
						makeFile.put(line);
					}

					long sno = makeFile.close();

					this.info("sno : " + sno);

					makeFile.toFile(sno);
					totaVo.put("PdfSno998", "" + sno);
				}
				l4040Report.setParentTranCode(titaVo.getTxcd());

				l4040Report.exec(resultList, titaVo);
				long sno = l4040Report.close();
				l4040Report.toPdf(sno);
				totaVo.put("PdfSno", "" + sno);

			} else {
				throw new LogicException(titaVo, "E0001", "查無資料");
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
		int cnt = l4040ServiceImpl.getSize();
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

	private void setData(Map<String, String> result) throws LogicException {
		// ID
		if (result.get("F19") == null || result.get("F19").length() == 0) {
			result.put("F19", result.get("F0"));
		}
		authCreateDate = parse.stringToInteger(result.get("F1"));
		processDate = parse.stringToInteger(result.get("F7"));
		stampFinishDate = parse.stringToInteger(result.get("F8"));
		propDate = parse.stringToInteger(result.get("F14"));
		retrDate = parse.stringToInteger(result.get("F15"));
		deleteDate = parse.stringToInteger(result.get("F16"));
		relAcctBirthday = parse.stringToInteger(result.get("F20"));

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