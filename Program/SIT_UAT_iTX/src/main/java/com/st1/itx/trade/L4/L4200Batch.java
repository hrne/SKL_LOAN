package com.st1.itx.trade.L4;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AchDeductMedia;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.BankRmtf;
import com.st1.itx.db.domain.BankRmtfId;
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.LoanChequeId;
import com.st1.itx.db.domain.PostDeductMedia;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.domain.TxErrCode;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.AchDeductMediaService;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.BankRmtfService;
import com.st1.itx.db.service.BatxChequeService;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.service.PostDeductMediaService;
import com.st1.itx.db.service.TxErrCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.AchDeductFileVo;
import com.st1.itx.util.common.data.BankRmtfFileVo;
import com.st1.itx.util.common.data.BatxChequeFileVo;
import com.st1.itx.util.common.data.EmpDeductFileVo;
import com.st1.itx.util.common.data.PostDeductFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L4200Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4200Batch extends TradeBuffer {
	@Autowired
	public FileCom fileCom;

	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public BankRmtfFileVo bankRmtfFileVo;

	@Autowired
	public AchDeductFileVo achDeductFileVo;

	@Autowired
	public BatxHeadService batxHeadService;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public BankRmtfService bankRmtfService;

	@Autowired
	public AchDeductMediaService achDeductMediaService;

	@Autowired
	public EmpDeductFileVo empDeductFileVo;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public PostDeductFileVo postDeductFileVo;

	@Autowired
	public PostDeductMediaService postDeductMediaService;

	@Autowired
	public BatxChequeFileVo batxChequeFileVo;

	@Autowired
	public BatxChequeService batxChequeService;

	@Autowired
	public LoanBookService loanBookService;

	@Autowired
	public FacCloseService facCloseService;

	@Autowired
	public BankDeductDtlService bankDeductDtlService;

	@Autowired
	public TxAmlCom txAmlCom;

	@Autowired
	public BaTxCom baTxCom;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public LoanChequeService loanChequeService;

	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	public TxErrCodeService txErrCodeService;

	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	public WebClient webClient;

	private int iAcDate = 0;
	private String iBatchNo = "";
	private String iTlrNo = "";

	private int tableSize = 0;
	private BigDecimal bigDe100 = new BigDecimal("100");

	private TempVo tempVo = new TempVo();

//	檢核成功筆數
	int rmftSuccCnt = 0;
//	檢核失敗筆數
	int rmftFailCnt = 0;
//	檢核成功金額
	BigDecimal rmftSuccAmt = BigDecimal.ZERO;

//	檢核成功筆數
	int achSuccCnt = 0;
//	檢核失敗筆數
	int achFailCnt = 0;
//	檢核成功金額
	BigDecimal achSuccAmt = BigDecimal.ZERO;

//	檢核成功筆數
	int postSuccCnt = 0;
//	檢核失敗筆數
	int postFailCnt = 0;
//	檢核成功金額
	BigDecimal postSuccAmt = BigDecimal.ZERO;

//	檢核成功筆數
	int empASuccCnt = 0;
//	檢核失敗筆數
	int empAFailCnt = 0;
//	檢核成功金額
	BigDecimal empASuccAmt = BigDecimal.ZERO;

//	檢核成功筆數
	int empBSuccCnt = 0;
//	檢核失敗筆數
	int empBFailCnt = 0;
//	檢核成功金額
	BigDecimal empBSuccAmt = BigDecimal.ZERO;

//	檢核成功筆數
	int chequeSuccCnt = 0;
//	檢核失敗筆數
	int chequeFailCnt = 0;
//	檢核成功金額
	BigDecimal chequeSuccAmt = BigDecimal.ZERO;

//	檢核成功筆數
	private int totSuccCnt = 0;
//	檢核失敗筆數
	private int totFailCnt = 0;
//	檢核成功金額
	private BigDecimal totAmt = BigDecimal.ZERO;
	private String procStsCode = "";

	private List<BankDeductDtl> lBankDeductDtl = new ArrayList<BankDeductDtl>();
	private Slice<BankDeductDtl> sBankDeductDtl = null;

	private List<BatxDetail> lBatxDetail = new ArrayList<BatxDetail>();
	private Slice<BatxDetail> sBatxDetail = null;

	private List<LoanCheque> lLoanCheque = new ArrayList<LoanCheque>();
	private Slice<LoanCheque> sLoanCheque = null;

	private List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
	private Slice<AcReceivable> sAcReceivable = null;

//	寄送筆數
	private int commitCnt = 500;

//	郵局入帳日檢核記號
	private int postEntryDateFlag = 0;
//	ACH入帳日檢核記號
	private int achEntryDateFlag = 0;

	private String sendMsg = "";
	private Boolean checkFlag = true;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4200Batch ");
		this.totaVo.init(titaVo);

// 入檔流程：
//		A.寫入Head檔
//		  for(4種來源檔) {
//			B.(first check)  檢核資料與寫入檔是否相同  並回寫處理狀態(ProcStsCode)
//			C.(second check) 寫入Detail檔 (媒體檔)
//			D.寫VO入各個扣款明細檔 (Detail不為異常者) 
//	      }
//		E.回寫Head檔 總金額與總筆數(if與前兩天相同，則丟Warning)

		iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		iBatchNo = titaVo.getParam("BatchNo");

		iTlrNo = titaVo.getTlrNo();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

//		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "N", "", titaVo.getTlrNo(),
//				"L4200" + " - " + iBatchNo + " 整批處理中，請稍候", titaVo);

//		暫定路徑 待討論過後決定抓取路徑方法

		String filena = titaVo.getParam("FILENA").trim();

		if (filena.indexOf(";") < 0) {
			filena = filena + ";";
		}

//		檢核檔名、檔案格式、是否已入過檔(因L492A每個檔每天只能吃一次)
		checkFile(filena, titaVo);

//		A.寫入Head檔			
		BatxHead tBatxHead = new BatxHead();
		BatxHeadId tBatxHeadId = new BatxHeadId();
		if (checkFlag) {
			tBatxHeadId.setAcDate(iAcDate);
			tBatxHeadId.setBatchNo(iBatchNo);
			tBatxHead.setBatxHeadId(tBatxHeadId);
			tBatxHead.setBatxTotAmt(BigDecimal.ZERO);
			tBatxHead.setBatxTotCnt(0);
			tBatxHead.setUnfinishCnt(0);
			tBatxHead.setBatxExeCode("0");
			tBatxHead.setBatxStsCode("1");
			tBatxHead.setTitaTlrNo(titaVo.getTlrNo());
			tBatxHead.setTitaTxCd(titaVo.getTxcd());
			try {
				this.info("Insert BatxHead !!!");
				batxHeadService.insert(tBatxHead, titaVo);
			} catch (DBException e) {
				sendMsg = e.getMessage();
				checkFlag = false;
			}
		}

		if (checkFlag) {
			String[] filelist = filena.split(";");
			for (String filename : filelist) {
				this.info("fileName : " + filename);
//				重製VO的OccursList
				initialFileVoOccursList();

				if (filename.indexOf("rbalmall") >= 0 && checkFlag) {
					String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("匯款轉帳檔 Start With -> " + filePath1);

					ArrayList<String> dataLineList1 = new ArrayList<>();
					try {
						dataLineList1 = fileCom.intputTxt(filePath1, "big5");
					} catch (IOException e) {
						this.info("L4200Batch(" + filePath1 + ") : " + e.getMessage());
					}
					try {
						procBatxRmtf(filePath1, dataLineList1, titaVo);
					} catch (LogicException e) {
						sendMsg = e.getMessage();
						checkFlag = false;
					}
				} else if (filename.indexOf("AHR11P") >= 0 && checkFlag) {
					String filePath2A1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("銀行扣帳回應檔 Start With -> " + filePath2A1);

					ArrayList<String> dataLineList2A1 = new ArrayList<>();

					try {
						dataLineList2A1 = fileCom.intputTxt(filePath2A1, "UTF-8");
					} catch (IOException e) {
						this.info("L4200Batch(" + filePath2A1 + ") : " + e.getMessage());
					}

					try {
						procAchDeduct(filePath2A1, dataLineList2A1, titaVo);
					} catch (LogicException e) {
						sendMsg = e.getMessage();
						checkFlag = false;
					}
				} else if (filename.indexOf("AHR12P") >= 0 && checkFlag) {
					String filePath2A2 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("銀行扣帳回應檔 Start With -> " + filePath2A2);
					ArrayList<String> dataLineList2A2 = new ArrayList<>();

					try {
						dataLineList2A2 = fileCom.intputTxt(filePath2A2, "UTF-8");
					} catch (IOException e) {
						this.info("L4200Batch(" + filePath2A2 + ") : " + e.getMessage());
					}

					try {
						procAchDeduct(filePath2A2, dataLineList2A2, titaVo);
					} catch (LogicException e) {
						sendMsg = e.getMessage();
						checkFlag = false;
					}
				} else if (filename.indexOf("PRSBCP4_53N") >= 0 && checkFlag) {
					String filePath2P1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("銀行扣帳回應檔 Start With -> " + filePath2P1);

					ArrayList<String> dataLineList2P1 = new ArrayList<>();
					try {
						dataLineList2P1 = fileCom.intputTxt(filePath2P1, "UTF-8");
					} catch (IOException e) {
						this.info("L4200Batch(" + filePath2P1 + ") : " + e.getMessage());
					}

					try {
						procPostDeduct(filePath2P1, dataLineList2P1, titaVo);
					} catch (LogicException e) {
						sendMsg = e.getMessage();
						checkFlag = false;
					}
				} else if (filename.indexOf("PRSBCP4_8460001") >= 0 && checkFlag) {
					String filePath2P2 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("銀行扣帳回應檔 Start With -> " + filePath2P2);

					ArrayList<String> dataLineList2P2 = new ArrayList<>();
					try {
						dataLineList2P2 = fileCom.intputTxt(filePath2P2, "UTF-8");
					} catch (IOException e) {
						this.info("L4200Batch(" + filePath2P2 + ") : " + e.getMessage());
					}

					try {
						procPostDeduct(filePath2P2, dataLineList2P2, titaVo);
					} catch (LogicException e) {
						sendMsg = e.getMessage();
						checkFlag = false;
					}
				} else if (filename.indexOf("PRSBCP4_8460002") >= 0 && checkFlag) {
					String filePath2P3 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("銀行扣帳回應檔 Start With -> " + filePath2P3);

					ArrayList<String> dataLineList2P3 = new ArrayList<>();
					try {
						dataLineList2P3 = fileCom.intputTxt(filePath2P3, "UTF-8");
					} catch (IOException e) {
						this.info("L4200Batch(" + filePath2P3 + ") : " + e.getMessage());
					}

					try {
						procPostDeduct(filePath2P3, dataLineList2P3, titaVo);
					} catch (LogicException e) {
						sendMsg = e.getMessage();
						checkFlag = false;
					}
				} else if (filename.indexOf("10H00") >= 0 && checkFlag) {
					String filePath31 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("員工扣薪回應檔 Start With -> " + filePath31);

					ArrayList<String> dataLineList31 = new ArrayList<>();
					try {
						dataLineList31 = fileCom.intputTxt(filePath31, "big5");
					} catch (IOException e) {
						this.info("L4200Batch(" + filePath31 + ") : " + e.getMessage());
					}

					try {
						procEmpDeduct(filePath31, dataLineList31, titaVo);
					} catch (LogicException e) {
						sendMsg = e.getMessage();
						checkFlag = false;
					}

				} else if (filename.indexOf("LNM617P") >= 0 && checkFlag) {
					String filePath32 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("員工扣薪回應檔 Start With -> " + filePath32);

					ArrayList<String> dataLineList32 = new ArrayList<>();
					try {
						dataLineList32 = fileCom.intputTxt(filePath32, "big5");
					} catch (IOException e) {
						this.info("L4200Batch(" + filePath32 + ") : " + e.getMessage());
					}

					try {
						procEmpDeduct(filePath32, dataLineList32, titaVo);
					} catch (LogicException e) {
						sendMsg = e.getMessage();
						checkFlag = false;
					}

				} else if (filename.indexOf("mortgage") >= 0 && checkFlag) {
					String filePath4 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("支票兌現檔 Start With -> " + filePath4);

					ArrayList<String> dataLineList4 = new ArrayList<>();
					try {
						dataLineList4 = fileCom.intputTxt(filePath4, "UTF-8");
					} catch (IOException e) {
						this.info("L4200Batch(" + filePath4 + ") : " + e.getMessage());
					}

					try {
						procCheque(filePath4, dataLineList4, titaVo);
//						支票兌現檔整批入帳檢核，逾期未兌現時(本埠：到期日+一營業日 外埠：到期日+兩營業日)寫入應處理清單 
						txToDoCheque(titaVo);
					} catch (LogicException e) {
						sendMsg = e.getMessage();
						checkFlag = false;
					}
				}
			}

			if (checkFlag) {
				try {
					checkBatxHead(tBatxHeadId, tBatxHead, titaVo);
				} catch (LogicException e) {
					sendMsg = e.getMessage();
					checkFlag = false;
				}
			}
		}

//		執行無誤者連結查詢清單
		if (checkFlag) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4002", titaVo.getTlrNo(),
					sendMsg, titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4200", titaVo.getTlrNo(),
					sendMsg, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();

	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
//			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private void procBatxRmtf(String filePath1, ArrayList<String> dataLineList1, TitaVo titaVo) throws LogicException {

		BankRmtf tBankRmtf = new BankRmtf();
		BankRmtfId tBankRmtfId = new BankRmtfId();

		if (dataLineList1 != null && dataLineList1.size() != 0) {
			// 使用資料容器內定義的方法切資料
			bankRmtfFileVo.setValueFromFile(dataLineList1);

			ArrayList<OccursList> uploadFile = bankRmtfFileVo.getOccursList();
			HashMap<String, BigDecimal> negaDraw = new HashMap<>();
			HashMap<String, BigDecimal> negaDepo = new HashMap<>();
			HashMap<String, BigDecimal> posiDepo = new HashMap<>();

//			先抓出本日為負的帳號及金額
			for (OccursList tempOccursList : uploadFile) {
				String visAcctNo = tempOccursList.get("OccVirAcctNo");
//				資料負數位放至小數點第二位，需自行補0
				if (tempOccursList.get("OccDepositAmt").substring(12).equals("p")) {
					BigDecimal nDepositAmt = parse
							.stringToBigDecimal(tempOccursList.get("OccDepositAmt").substring(0, 12) + "0")
							.divide(bigDe100);
					negaDepo.put(visAcctNo, nDepositAmt);
				} else {
					BigDecimal pDepositAmt = parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt"))
							.divide(bigDe100);
					posiDepo.put(visAcctNo, pDepositAmt);
				}
				if (tempOccursList.get("OccWithDrawAmt").substring(12).equals("p")) {
					BigDecimal nWithDrawAmt = parse
							.stringToBigDecimal(tempOccursList.get("WithDrawAmt").substring(0, 12) + "0")
							.divide(bigDe100);
					negaDraw.put(visAcctNo, nWithDrawAmt);
				}
			}

//			資料筆數:n 				uploadFile.size()
//			資料庫,當天,同批號,未檢核筆數:m	lBatxDetail.size()
//			loop : m+1~m+n 
			sBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, this.index, this.limit, titaVo);

			lBatxDetail = sBatxDetail == null ? null : sBatxDetail.getContent();

			if (lBatxDetail != null && lBatxDetail.size() != 0) {
				tableSize = lBatxDetail.size();
			} else {
				tableSize = 0;
			}
			for (int i = tableSize; i < tableSize + uploadFile.size(); i++) {

				if (i % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				OccursList tempOccursList = new OccursList();
				tempOccursList = uploadFile.get(i - tableSize);

				int intCustNo = 0;
				int intRepayType = 0;
				String procCodeX = "";

				if (isNumeric(tempOccursList.get("OccVirAcctNo"))) {
					intCustNo = parse.stringToInteger(tempOccursList.get("OccVirAcctNo").substring(7));
				} else {
					intCustNo = 0;
				}

//				B.(first check)檢核資料與寫入檔是否相同  並回寫處理狀態(ProcStsCode)
//				轉帳匯款檔並無寫入檔
//				1.存款為負數 :  101-正負對沖 (一正一負) 
//				2.提款為負數 :  102-提款(借方)
//				3.摘要欄提示 :  104-ACH手續費 (前3碼為ACH) 120-法院 (有地院字眼)   106-特殊摘要 (不為數字else or 僅一筆存款負數 or 存款金額為0) 
//				            110-更正轉帳 (OccRemark為更正轉帳)
//				4.??? : 103-預先作業(上一營業日之ORTX 比照金額&戶號)  105-銀扣清算  (房貸 票交 字眼)
				String errorCode = FormatUtil.pad9("".trim(), 5);
				HashMap<String, Integer> err103map = new HashMap<>();
				err103map.put(iBatchNo, 0);

				if (negaDraw.containsKey(tempOccursList.get("OccVirAcctNo"))
						|| negaDepo.containsKey(tempOccursList.get("OccVirAcctNo"))
						|| posiDepo.containsKey(tempOccursList.get("OccVirAcctNo"))) {
				} else {
					BatxHead tBatxHead = new BatxHead();
					tBatxHead = batxHeadService.titaTxCdFirst(this.txBuffer.getTxCom().getLbsdyf(), "L4210", "8",
							titaVo);
					String batchNo = "";
					if (tBatxHead != null) {
						batchNo = tBatxHead.getBatchNo();
					}

					List<String> err103List = new ArrayList<String>();
					err103List.add("5");
					err103List.add("6");
					BatxDetail err103 = new BatxDetail();

//					上營業日 戶號 金額 相同者
					err103 = batxDetailService.findL4200BFirst(this.txBuffer.getTxCom().getLbsdyf(), batchNo, intCustNo,
							parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt")), err103List, titaVo);
					if (err103 != null) {
						err103map.put(iBatchNo, 1);
					}
				}

				if (isNumeric(tempOccursList.get("OccVirAcctNo"))) {
					if (negaDepo.containsKey(tempOccursList.get("OccVirAcctNo"))
							&& posiDepo.containsKey(tempOccursList.get("OccVirAcctNo"))) {
						if (negaDepo.get(tempOccursList.get("OccVirAcctNo")).compareTo(
								parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt").substring(0, 12))
										.divide(new BigDecimal("10"))) == 0) {
							errorCode = FormatUtil.pad9("101".trim(), 5);
						}
					} else if (err103map.get(iBatchNo) == 1) {
						errorCode = FormatUtil.pad9("103".trim(), 5);
					} else if (tempOccursList.get("OccDepositAmt").indexOf("p") >= 0) {
						errorCode = FormatUtil.pad9("106".trim(), 5);
					} else if (negaDraw.containsKey(tempOccursList.get("OccVirAcctNo"))) {
						errorCode = FormatUtil.pad9("102".trim(), 5);
					} else if (tempOccursList.get("OccRemark").indexOf("更正轉帳") >= 0) {
						errorCode = FormatUtil.pad9("110".trim(), 5);
					} else if (parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt"))
							.compareTo(BigDecimal.ZERO) == 0) {
						errorCode = FormatUtil.pad9("106".trim(), 5);
					} else {
						errorCode = FormatUtil.pad9("".trim(), 5);
					}
				} else if ("ACH".equals(tempOccursList.get("OccVirAcctNo").substring(0, 3))) {
					errorCode = FormatUtil.pad9("104".trim(), 5);
				} else if (tempOccursList.get("OccVirAcctNo").indexOf("地院") >= 0) {
					errorCode = FormatUtil.pad9("120".trim(), 5);
				} else if (tempOccursList.get("OccVirAcctNo").indexOf("房貸") >= 0) {
					errorCode = FormatUtil.pad9("105".trim(), 5);
				} else if (tempOccursList.get("OccVirAcctNo").indexOf("票交") >= 0) {
					errorCode = FormatUtil.pad9("105".trim(), 5);
				} else {
					errorCode = FormatUtil.pad9("106".trim(), 5);
				}
				this.info("title : " + tempOccursList.get("OccRemark") + ",,," + tempOccursList.get("OccVirAcctNo")
						+ ",,," + tempOccursList.get("OccDepositAmt"));
				this.info("CheckFlag : " + tempOccursList.get("CheckFlag"));
				this.info("errorCode : " + errorCode);

				if ("00000".equals(errorCode)) {
					rmftSuccCnt = rmftSuccCnt + 1;
					rmftSuccAmt = rmftSuccAmt
							.add(parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt")).divide(bigDe100));
				} else {
					rmftFailCnt = rmftFailCnt + 1;
				}

//			C.寫入Detail檔(second check) 
				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();

				tBatxDetailId.setAcDate(iAcDate);
				tBatxDetailId.setBatchNo(iBatchNo);
				tBatxDetailId.setDetailSeq(i + 1);
				tBatxDetail.setBatxDetailId(tBatxDetailId);
				tBatxDetail.setRepayCode(1);
				tBatxDetail.setFileName(filePath1.substring(filePath1.indexOf("rbalmall")));
				tBatxDetail.setEntryDate(parse.stringToInteger(tempOccursList.get("OccEntryDate")));
				tBatxDetail.setCustNo(intCustNo);
				tBatxDetail.setFacmNo(0);
				tBatxDetail.setRvNo("");
//				 --待確認
				tBatxDetail.setRepayType(intRepayType);
				tBatxDetail.setReconCode("P03");
//				--待確認
				tBatxDetail.setRepayAcCode("");
//				--待確認
				tBatxDetail.setAcquiredAmt(BigDecimal.ZERO);
				if ("00000".equals(errorCode)) {
					tBatxDetail.setRepayAmt(
							parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt")).divide(bigDe100));
				} else if ("00102".equals(errorCode)) {
					tBatxDetail.setRepayAmt(
							parse.stringToBigDecimal(tempOccursList.get("WithDrawAmt").substring(0, 12) + "0")
									.divide(bigDe100));
				} else if (isNumeric(tempOccursList.get("OccDepositAmt"))
						&& parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt")).divide(bigDe100)
								.compareTo(BigDecimal.ZERO) == 0) {
					tBatxDetail
							.setRepayAmt(parse.stringToBigDecimal(tempOccursList.get("WithDrawAmt")).divide(bigDe100));
				} else {
					tBatxDetail.setRepayAmt(BigDecimal.ZERO);
				}
				tBatxDetail.setAcctAmt(BigDecimal.ZERO);
				tBatxDetail.setDisacctAmt(BigDecimal.ZERO);

				if ("00000".equals(errorCode)) {
					tBatxDetail.setProcStsCode("0");
					procStsCode = "0";
				} else if ("00110".equals(errorCode)) {
					tBatxDetail.setProcStsCode("2");
					procStsCode = "2";
				} else if ("00101".equals(errorCode)) {
					tBatxDetail.setProcStsCode("1");
					procStsCode = "1";
				} else if ("00102".equals(errorCode)) {
					tBatxDetail.setProcStsCode("1");
					procStsCode = "1";
				} else if ("00103".equals(errorCode)) {
					tBatxDetail.setProcStsCode("1");
					procStsCode = "1";
				} else if ("00104".equals(errorCode)) {
					tBatxDetail.setProcStsCode("1");
					procStsCode = "1";
				} else {
//				0622 由420A做檢核再決定檢核錯誤
					tBatxDetail.setProcStsCode("2");
					procStsCode = "2";
				}

				tBatxDetail.setProcCode(errorCode);
				tempVo = new TempVo();
				tempVo.putParam("VirtualAcctNo", tempOccursList.get("OccVirAcctNo"));
				tempVo.putParam("DscptCode", tempOccursList.get("OccNoteCode"));
				if ("00000".equals(errorCode)) {
					tempVo.putParam("CheckMsg", "");
					tempVo.putParam("Note", tempOccursList.get("OccRemark"));

				} else if ("00110".equals(errorCode)) {
					tempVo.putParam("CheckMsg", tempOccursList.get("OccRemark"));
					tempVo.putParam("ReturnMsg", setProcCodeX(errorCode, procCodeX, titaVo));
				} else {
					if (isNumeric(tempOccursList.get("OccVirAcctNo"))) {
						if (tempOccursList.get("OccDepositAmt").indexOf("p") >= 0) {
							BigDecimal nDepositAmt = parse
									.stringToBigDecimal(tempOccursList.get("OccDepositAmt").substring(0, 12) + "0")
									.divide(bigDe100);

							tempVo.putParam("CheckMsg", "-" + nDepositAmt);
							tempVo.putParam("ReturnMsg", setProcCodeX(errorCode, procCodeX, titaVo));

						} else if (parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt"))
								.compareTo(BigDecimal.ZERO) == 0) {
							tempVo.putParam("CheckMsg", tempOccursList.get("OccRemark"));
							tempVo.putParam("ReturnMsg", setProcCodeX(errorCode, procCodeX, titaVo));
						} else {
							tempVo.putParam("CheckMsg",
									parse.stringToBigDecimal("" + negaDepo.get(tempOccursList.get("OccVirAcctNo"))));
							tempVo.putParam("ReturnMsg", setProcCodeX(errorCode, procCodeX, titaVo));
						}
					} else {
						if ("".equals(tempOccursList.get("OccVirAcctNo").trim())) {
							tempVo.putParam("CheckMsg", tempOccursList.get("OccRemark"));
							tempVo.putParam("ReturnMsg", setProcCodeX(errorCode, procCodeX, titaVo));
						} else {
							tempVo.putParam("CheckMsg", tempOccursList.get("OccVirAcctNo"));
							tempVo.putParam("ReturnMsg", setProcCodeX(errorCode, procCodeX, titaVo));
						}
					}
				}

				tBatxDetail.setTitaTlrNo(iTlrNo);
				tBatxDetail.setTitaTxtNo(iBatchNo.substring(6) + FormatUtil.pad9("" + (i + 1), 6));
				tBatxDetail.setMediaDate(0);
				tBatxDetail.setMediaKind("");
				tBatxDetail.setMediaSeq(0);

//			D.寫VO入各個對應Table (BankRmtf皆寫入)
				tBankRmtfId.setAcDate(iAcDate);
				tBankRmtfId.setBatchNo(iBatchNo);
				tBankRmtfId.setDetailSeq(i + 1);
				tBankRmtf.setBankRmtfId(tBankRmtfId);
				tBankRmtf.setAcDate(iAcDate);
				tBankRmtf.setBatchNo(iBatchNo);
				tBankRmtf.setDetailSeq(i + 1);
				tBankRmtf.setCustNo(intCustNo);
				if (tempOccursList.get("OccDepositAmt").indexOf("p") >= 0) {
					tBankRmtf.setRepayAmt(
							parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt").substring(0, 12) + "0")
									.divide(bigDe100));
				} else {
					tBankRmtf.setRepayAmt(
							parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt")).divide(bigDe100));
				}

				tBankRmtf.setDepAcctNo(tempOccursList.get("OccAcctNo"));
				;
				tBankRmtf.setEntryDate(parse.stringToInteger(tempOccursList.get("OccEntryDate")));

				tBankRmtf.setDscptCode(tempOccursList.get("OccNoteCode"));

				tBankRmtf.setVirtualAcctNo(tempOccursList.get("OccVirAcctNo"));

				if (tempOccursList.get("OccWithDrawAmt").indexOf("p") >= 0) {
					tBankRmtf.setWithdrawAmt(
							parse.stringToBigDecimal(tempOccursList.get("OccWithDrawAmt").substring(0, 12) + "0")
									.divide(bigDe100));
				} else {
					tBankRmtf.setWithdrawAmt(
							parse.stringToBigDecimal(tempOccursList.get("OccWithDrawAmt")).divide(bigDe100));
				}

				if (tempOccursList.get("OccDepositAmt").indexOf("p") >= 0) {
					tBankRmtf.setDepositAmt(
							parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt").substring(0, 12) + "0")
									.divide(bigDe100));
				} else {
					tBankRmtf.setDepositAmt(
							parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt")).divide(bigDe100));
				}

				tBankRmtf.setBalance(parse.stringToBigDecimal(tempOccursList.get("OccBalance")).divide(bigDe100));
				tBankRmtf.setRemintBank(tempOccursList.get("OccBankCode"));
				tBankRmtf.setTraderInfo(tempOccursList.get("OccTrader"));

//				E.AML 姓名檢核
				txAmlCom.setTxBuffer(this.getTxBuffer());
				tempVo = txAmlCom.bankRmtf(tempVo, tBankRmtf, titaVo);
				tBatxDetail.setProcNote(tempVo.getJsonString());

//				F.insert BankRmtf、BatxDetail
				try {
					bankRmtfService.insert(tBankRmtf, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "tBankRmtf Insert Fail : " + e.getErrorMsg());
				}

				try {
					batxDetailService.insert(tBatxDetail, titaVo);
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException("E0005", "BatxDetail Insert Fail");
				}

			}

		} else {
//			throw new LogicException("E0014", "L4200Batch No Content " + filePath1);
		}

	}

	private void procAchDeduct(String filePath2A, ArrayList<String> dataLineList2A, TitaVo titaVo)
			throws LogicException {

		// 編碼參數，設定為UTF-8 || big5

		if (dataLineList2A != null && dataLineList2A.size() != 0) {
			// 使用資料容器內定義的方法切資料

			achDeductFileVo.setValueFromFile(dataLineList2A);
			ArrayList<OccursList> uploadFile = achDeductFileVo.getOccursList();

//			int reEntryDate = parse.stringToInteger("" + achDeductFileVo.get("HeadProcessDate"));
			String procCode = "00000";
			String procCodeX = "";

			sBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, this.index, this.limit, titaVo);

			lBatxDetail = sBatxDetail == null ? null : sBatxDetail.getContent();

			if (lBatxDetail != null && lBatxDetail.size() != 0) {
				tableSize = lBatxDetail.size();
			} else {
				tableSize = 0;
			}
			for (int i = tableSize; i < tableSize + uploadFile.size(); i++) {

				if (i % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				OccursList tempOccursList = new OccursList();
				tempOccursList = uploadFile.get(i - tableSize);

				AchDeductMedia tAchDeductMedia = new AchDeductMedia();

//				B.(first check)檢核資料與寫入檔是否相同  並回寫處理狀態(ProcStsCode(1) ProcCode(5) AcquiredAmt)
				int reCustNo = parse.stringToInteger(tempOccursList.get("OccSenderRemarker").substring(0, 7));
				int reFacmNo = parse.stringToInteger(tempOccursList.get("OccSenderRemarker").substring(7, 10));
				int reRepayCode = parse.stringToInteger(tempOccursList.get("OccSenderRemarker").substring(10, 11));
				int reIntEndDate = parse.stringToInteger(tempOccursList.get("OccSenderRemarker").substring(11, 19));
				BigDecimal reRepayAmt = parse.stringToBigDecimal(tempOccursList.get("OccRepayAmt"));

//				if (reIntEndDate >= 10101) {
//					reIntEndDate = reIntEndDate + 19110000;
//				}

				tAchDeductMedia = achDeductMediaService.reseiveCheckFirst(reCustNo, reFacmNo, "" + reRepayCode,
						reIntEndDate, reRepayAmt, titaVo);

//				C.寫入Detail & 回寫媒體檔(second check) 
				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();

				tempVo = new TempVo();

				int achEntryDate = 0;
				int achRepayType = 0;
				String returnCode = tempOccursList.get("OccReturnCode");
				if (tAchDeductMedia == null) {
					// 媒體檔無此資料
					tBatxDetail.setProcCode("E0014");
					procCode = "E0014"; // 檔案錯誤
					procCodeX = "媒體檔無此資料";
				} else {
					tBatxDetail.setProcCode("002" + returnCode);
					if ("00".equals(returnCode)) {
						procCode = "00000";
					} else {
						procCode = "002" + returnCode;
					}

					achEntryDate = tAchDeductMedia.getEntryDate();

//					僅檢查第一筆
					if (i == tableSize) {
						if (iAcDate < (achEntryDate + 19110000)) {
							achEntryDateFlag = 1;
						}
					}

//					結案
//					if(tAchDeductMedia.getRepayType() == 3) {
//						achRepayType = 1;
//						tempVo.put("LastTermFg", "1");
//					} else {
					achRepayType = tAchDeductMedia.getRepayType();
//					}

//					回寫媒體檔
					tAchDeductMedia = achDeductMediaService.holdById(tAchDeductMedia, titaVo);
					tAchDeductMedia.setReturnCode(tempOccursList.get("OccReturnCode"));
					tAchDeductMedia.setAcDate(iAcDate);
					tAchDeductMedia.setBatchNo(iBatchNo);
					tAchDeductMedia.setDetailSeq(i + 1);
					try {
						achDeductMediaService.update(tAchDeductMedia, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "AchDeductMedia update Fail");
					}
				}

				tBatxDetailId.setAcDate(iAcDate);
				tBatxDetailId.setBatchNo(iBatchNo);
				tBatxDetailId.setDetailSeq(i + 1);
				tBatxDetail.setBatxDetailId(tBatxDetailId);
//				02.銀行扣款
				tBatxDetail.setRepayCode(2);
				tBatxDetail.setFileName(filePath2A.substring(filePath2A.indexOf("AHR1")));
				tBatxDetail.setEntryDate(achEntryDate);
				tBatxDetail.setCustNo(reCustNo);
				tBatxDetail.setFacmNo(reFacmNo);
				tBatxDetail.setRvNo("");
				tBatxDetail.setRepayType(achRepayType);
				tBatxDetail.setReconCode("C01"); // 暫收款－非核心資金運用
				tBatxDetail.setRepayAcCode("");
				tBatxDetail.setRepayAmt(reRepayAmt);

				if ("00000".equals(procCode)) {
					tBatxDetail.setProcStsCode("0");
//					檢核正常
					procStsCode = "0";
					achSuccCnt = achSuccCnt + 1;
					achSuccAmt = achSuccAmt.add(reRepayAmt);
				} else if ("E".equals(procCode.substring(0, 1))) {
					tBatxDetail.setProcStsCode("1");
//					資料錯誤  --人工處理
					procStsCode = "1";
					achFailCnt = achFailCnt + 1;
				} else {
					tBatxDetail.setProcStsCode("1");
//					回應碼為00以外者 --不處理
					procStsCode = "1";
					achFailCnt = achFailCnt + 1;
				}

				tBatxDetail.setProcCode(procCode);

				if ("00000".equals(procCode)) {
//					tempVo.putParam("Note", tempOccursList.get("OccReturnCode"));
					updateBankDeductDtl(tAchDeductMedia.getMediaDate(), tAchDeductMedia.getMediaKind(),
							tAchDeductMedia.getMediaSeq(), returnCode, titaVo);
				} else if ("E".equals(procCode.substring(0, 1))) {
					tempVo.putParam("CheckMsg", setProcCodeX(procCode, procCodeX, titaVo));
				} else {
//					回傳碼中文 code+cdCode.item
					tempVo.putParam("CheckMsg", setProcCodeX(procCode, procCodeX, titaVo));
//					回傳碼
					updateBankDeductDtl(tAchDeductMedia.getMediaDate(), tAchDeductMedia.getMediaKind(),
							tAchDeductMedia.getMediaSeq(), returnCode, titaVo);
				}

				this.info("1132...");

				tBatxDetail.setProcNote(tempVo.getJsonString());

				tBatxDetail.setTitaTlrNo(iTlrNo);
				tBatxDetail.setTitaTxtNo(iBatchNo.substring(6) + FormatUtil.pad9("" + (i + 1), 6));
				if (tAchDeductMedia != null) {
					tBatxDetail.setMediaDate(tAchDeductMedia.getMediaDate());
					tBatxDetail.setMediaKind(tAchDeductMedia.getMediaKind());
					tBatxDetail.setMediaSeq(tAchDeductMedia.getMediaSeq());
				}
				try {
					batxDetailService.insert(tBatxDetail, titaVo);
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException("E0005", "BatxDetail Insert Fail");
				}

//				D.寫VO入各個對應Table (Detail不為異常者)
			}
		} else {
//			throw new LogicException("E0014", "L4200Batch No Content " + filePath2A);
		}
	}

//	郵局扣款
	private void procPostDeduct(String filePath2P, ArrayList<String> dataLineList2P, TitaVo titaVo)
			throws LogicException {

		// 編碼參數，設定為UTF-8 || big5
		try {
			dataLineList2P = fileCom.intputTxt(filePath2P, "UTF-8");
		} catch (IOException e) {
//			throw new LogicException("E0014", "L4200Batch(" + filePath2P + ")");
		}

		if (dataLineList2P != null && dataLineList2P.size() != 0) {
			// 使用資料容器內定義的方法切資料
			postDeductFileVo.setValueFromFile(dataLineList2P);
			ArrayList<OccursList> uploadFile = postDeductFileVo.getOccursList();
			String procCode = "00000";

			sBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, this.index, this.limit, titaVo);

			lBatxDetail = sBatxDetail == null ? null : sBatxDetail.getContent();

			if (lBatxDetail != null && lBatxDetail.size() != 0) {
				tableSize = lBatxDetail.size();
			} else {
				tableSize = 0;
			}
			for (int i = tableSize; i < tableSize + uploadFile.size(); i++) {

				if (i % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				OccursList tempOccursList = new OccursList();
				tempOccursList = uploadFile.get(i - tableSize);

				PostDeductMedia tPostDeductMedia = new PostDeductMedia();

//				僅檢查第一筆
				if (i == tableSize) {
					if (iAcDate < parse.stringToInteger(tempOccursList.get("OccTxDate")) + 19110000) {
						postEntryDateFlag = 1;
					}
				}

//			B.(first check)檢核資料與寫入檔是否相同  並回寫處理狀態(ProcStsCode)
				BigDecimal reRepayAmt = parse.stringToBigDecimal(tempOccursList.get("OccRepayAmt")).divide(bigDe100);
				int reEntryDate = parse.stringToInteger(tempOccursList.get("OccTxDate"));
				int postRepayType = 0;
				String procCodeX = "";
//				PostUserNo = ,AND RepayAmt = ,AND OutsrcRemark = 
				tPostDeductMedia = postDeductMediaService.receiveCheckFirst(
						FormatUtil.padLeft(tempOccursList.get("OccCustMemo").trim(), 20), reRepayAmt,
						FormatUtil.padX(tempOccursList.get("OccRemark"), 20), titaVo);
				String returnCode = FormatUtil.pad9(tempOccursList.get("OccReturnCode").trim(), 2);

//			C.寫入Detail檔(second check) 
				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();
				tempVo = new TempVo();

				if (tPostDeductMedia != null) {
//					結案
//					if (tPostDeductMedia.getRepayType() == 3) {
//						postRepayType = 1;
//						tempVo.put("LastTermFg", "1");
//					} else {
					postRepayType = tPostDeductMedia.getRepayType();
//					}
					if ("00".equals(returnCode)) {
						procCode = "00000";
					} else {
						procCode = "003" + returnCode;
					}
					tBatxDetail.setProcCode(procCode);

//					回寫媒體檔
					tPostDeductMedia = postDeductMediaService.holdById(tPostDeductMedia, titaVo);
					tPostDeductMedia.setProcNoteCode(tempOccursList.get("OccReturnCode"));
					tPostDeductMedia.setAcDate(iAcDate);
					tPostDeductMedia.setBatchNo(iBatchNo);
					tPostDeductMedia.setDetailSeq(i + 1);
					try {
						postDeductMediaService.update(tPostDeductMedia, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "PostDeductMedia update Fail");
					}

				} else {
					// 媒體檔無此資料
					tBatxDetail.setProcCode("E0014"); // 檔案錯誤
					procCode = "E0014";
					procCodeX = "媒體檔無此資料";

				}

				tBatxDetailId.setAcDate(iAcDate);
				tBatxDetailId.setBatchNo(iBatchNo);
				tBatxDetailId.setDetailSeq(i + 1);
				tBatxDetail.setBatxDetailId(tBatxDetailId);
//				02.銀行扣款
				tBatxDetail.setRepayCode(2);
				tBatxDetail.setFileName(filePath2P.substring(filePath2P.indexOf("PRSBCP4")));
				tBatxDetail.setEntryDate(reEntryDate + 19110000);
				tBatxDetail.setCustNo(parse.stringToInteger(tempOccursList.get("OccCustNo")));
				tBatxDetail.setFacmNo(parse.stringToInteger(tempOccursList.get("OccFacmNo")));
				tBatxDetail.setRvNo("");
				tBatxDetail.setRepayType(postRepayType);
				tBatxDetail.setReconCode("P01");
				tBatxDetail.setRepayAcCode("");
				tBatxDetail.setRepayAmt(reRepayAmt);

				// 扣款成功
				if ("00000".equals(procCode)) {
					tBatxDetail.setProcStsCode("0"); // 未檢核
					procStsCode = "0";
					postSuccCnt = postSuccCnt + 1;
					postSuccAmt = postSuccAmt.add(reRepayAmt);
				}
				// 資料有錯
				else if ("E".equals(procCode.substring(0, 1))) {
					tBatxDetail.setProcStsCode("1"); // 人工處理
					procStsCode = "1";
					postFailCnt = postFailCnt + 1;
				}
				// 扣款失敗
				else {
					tBatxDetail.setProcStsCode("1");// 不處理
					procStsCode = "1";
					postFailCnt = postFailCnt + 1;
				}

				tBatxDetail.setProcCode(procCode);

				if ("00000".equals(procCode)) {
					updateBankDeductDtl(tPostDeductMedia.getMediaDate(), "3", tPostDeductMedia.getMediaSeq(),
							returnCode, titaVo);
				} else if ("E".equals(procCode.substring(0, 1))) {
					tempVo.putParam("CheckMsg", setProcCodeX(procCode, procCodeX, titaVo));

				} else {
//					回傳碼中文 code+cdCode.item
					tempVo.putParam("CheckMsg", setProcCodeX(procCode, procCodeX, titaVo));
//					回傳碼
					updateBankDeductDtl(tPostDeductMedia.getMediaDate(), "3", tPostDeductMedia.getMediaSeq(),
							returnCode, titaVo);
				}

				tBatxDetail.setProcNote(tempVo.getJsonString());
				tBatxDetail.setTitaTlrNo(iTlrNo);
				tBatxDetail.setTitaTxtNo(iBatchNo.substring(6) + FormatUtil.pad9("" + (i + 1), 6));
				if (tPostDeductMedia != null) {
					tBatxDetail.setMediaDate(tPostDeductMedia.getMediaDate());
					tBatxDetail.setMediaKind("3");
					tBatxDetail.setMediaSeq(tPostDeductMedia.getMediaSeq());
				}

				try {
					batxDetailService.insert(tBatxDetail, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "BatxDetail Insert Fail");
				}

//			D.寫VO入各個對應Table (Detail不為異常者)

			}
		} else {
//			throw new LogicException("E0014", "L4200Batch No Content " + filePath2P);
		}
	}

	private void procEmpDeduct(String filePath3, ArrayList<String> dataLineList3, TitaVo titaVo) throws LogicException {

		String mediaType = "";

		if (filePath3.indexOf("10H00") >= 0) {
			mediaType = "4";
		} else if (filePath3.indexOf("LNM617P") >= 0) {
			mediaType = "5";
		}

		if (dataLineList3 != null && dataLineList3.size() != 0) {
			// 使用資料容器內定義的方法切資料
			empDeductFileVo.setValueFromFile(dataLineList3);
			ArrayList<OccursList> uploadFile = empDeductFileVo.getOccursList();

			String procCode = "00000";
			String procCodeX = "";
			sBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, this.index, this.limit, titaVo);

			lBatxDetail = sBatxDetail == null ? null : sBatxDetail.getContent();

			if (lBatxDetail != null && lBatxDetail.size() != 0) {
				tableSize = lBatxDetail.size();
			} else {
				tableSize = 0;
			}

			for (int i = tableSize; i < tableSize + uploadFile.size(); i++) {

				if (i % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				OccursList tempOccursList = new OccursList();
				tempOccursList = uploadFile.get(i - tableSize);

//			B.(first check)檢核資料與寫入檔是否相同  並回寫處理狀態(ProcStsCode)
				EmpDeductMedia tEmpDeductMedia = new EmpDeductMedia();

				BigDecimal reRepayAmt = parse.stringToBigDecimal(tempOccursList.get("OccRepayAmt")); // 扣款金額
				BigDecimal reAcctAmt = parse.stringToBigDecimal(tempOccursList.get("OccAcctAmt")); // 實扣金額

				int reRepayCode = 0;
				if ("XH".equals(tempOccursList.get("OccUnknowC"))) {
					reRepayCode = 1;
				} else if ("92".equals(tempOccursList.get("OccUnknowC"))) {
					reRepayCode = 5;
				}

				tEmpDeductMedia = empDeductMediaService.receiveCheckFirst(mediaType,
						parse.stringToInteger(tempOccursList.get("OccCustNo")),
						parse.stringToInteger(tempOccursList.get("OccEntryDate")), reRepayCode, reRepayAmt, titaVo);

//			C.寫入Detail檔(second check) 
				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();

				if (tEmpDeductMedia == null) {
					procCode = "E0014"; // 檔案錯誤
					procCodeX = "媒體檔無此資料";
				} else {
					if ("01".equals(tempOccursList.get("OccReturnCode"))) {
						procCode = "00000";
						tEmpDeductMedia.setTxAmt(reAcctAmt);
					} else {
						procCode = "004" + tempOccursList.get("OccReturnCode");
					}
//					回寫媒體檔
					tEmpDeductMedia = empDeductMediaService.holdById(tEmpDeductMedia, titaVo);
					tEmpDeductMedia.setErrorCode(tempOccursList.get("OccReturnCode"));
					tEmpDeductMedia.setAcDate(iAcDate);
					tEmpDeductMedia.setBatchNo(iBatchNo);
					tEmpDeductMedia.setDetailSeq(i + 1);
					try {
						empDeductMediaService.update(tEmpDeductMedia, titaVo);
					} catch (DBException e) {
						e.printStackTrace();
						throw new LogicException("E0007", "EmpDeductMedia update Fail");
					}
				}
				tBatxDetail.setProcCode(procCode);
				tBatxDetailId.setAcDate(iAcDate);
				tBatxDetailId.setBatchNo(iBatchNo);
				tBatxDetailId.setDetailSeq(i + 1);
				tBatxDetail.setBatxDetailId(tBatxDetailId);
//				03.員工扣款
				tBatxDetail.setRepayCode(3);
				if ("4".equals(mediaType)) {
					tBatxDetail.setFileName(filePath3.substring(filePath3.indexOf("10H00")));
				} else if ("5".equals(mediaType)) {
					tBatxDetail.setFileName(filePath3.substring(filePath3.indexOf("LNM617P")));
				}
				tBatxDetail.setEntryDate(parse.stringToInteger(tempOccursList.get("OccEntryDate")));
				tBatxDetail.setCustNo(parse.stringToInteger(tempOccursList.get("OccCustNo")));
				tBatxDetail.setFacmNo(0);
				tBatxDetail.setRvNo("");
				tBatxDetail.setRepayType(reRepayCode);
				tBatxDetail.setReconCode("TEM");
				tBatxDetail.setRepayAcCode("");
				tBatxDetail.setRepayAmt(reAcctAmt); // 實扣金額

				// 扣款成功 && 實扣金額>0
				if ("00000".equals(procCode) && reAcctAmt.compareTo(BigDecimal.ZERO) > 0) {
					procStsCode = "0"; // 檢核正常
				}
				// 資料有錯
				else if ("E".equals(procCode.substring(0, 1))) {
					procStsCode = "1"; // 人工處理
				}
				// 實扣金額=0、扣款失敗
				else {
					procStsCode = "1"; // 不處理
				}

				tBatxDetail.setProcStsCode(procStsCode);
				tBatxDetail.setProcCode(procCode);
				if ("4".equals(mediaType)) {
					if ("0".equals(procStsCode)) {
						empASuccCnt = empASuccCnt + 1;
						empASuccAmt = empASuccAmt.add(reAcctAmt);
					} else {
						empAFailCnt = empAFailCnt + 1;
					}
				} else if ("5".equals(mediaType)) {
					if ("0".equals(procStsCode)) {
						empBSuccCnt = empBSuccCnt + 1;
						empBSuccAmt = empBSuccAmt.add(reAcctAmt);
					} else {
						empAFailCnt = empAFailCnt + 1;
					}
				}

				// Error Message
				tempVo = new TempVo();
				if (!"00000".equals(procCode)) {
					tempVo.putParam("CheckMsg", setProcCodeX(procCode, procCodeX, titaVo));
				}
				tBatxDetail.setProcNote(tempVo.getJsonString());

				tBatxDetail.setTitaTlrNo(iTlrNo);
				tBatxDetail.setTitaTxtNo(iBatchNo.substring(6) + FormatUtil.pad9("" + (i + 1), 6));

				if (tEmpDeductMedia != null) {
					tBatxDetail.setMediaDate(tEmpDeductMedia.getMediaDate());
					tBatxDetail.setMediaKind(tEmpDeductMedia.getMediaKind());
					tBatxDetail.setMediaSeq(tEmpDeductMedia.getMediaSeq());
				}

				try {
					batxDetailService.insert(tBatxDetail, titaVo);
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException("E0005", "BatxDetail Insert Fail");
				}

//			D.寫VO入各個對應Table (Detail不為異常者)
			}
		} else {
//			throw new LogicException("E0014", "L4200Batch No Content " + filePath3);
		}
	}

	private void procCheque(String filePath4, ArrayList<String> dataLineList4, TitaVo titaVo) throws LogicException {
		if (dataLineList4 != null && dataLineList4.size() != 0) {
			// 使用資料容器內定義的方法切資料
			batxChequeFileVo.setValueFromFile(dataLineList4);
			ArrayList<OccursList> uploadFile = batxChequeFileVo.getOccursList();

			sBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, this.index, this.limit, titaVo);

			lBatxDetail = sBatxDetail == null ? null : sBatxDetail.getContent();

			if (lBatxDetail != null && lBatxDetail.size() != 0) {
				tableSize = lBatxDetail.size();
			} else {
				tableSize = 0;
			}

			int cheqCnt = 1;

			for (int i = tableSize; i < tableSize + uploadFile.size(); i++) {

				if (i % commitCnt == 0) {
					this.batchTransaction.commit();
				}

				String procCode = "";
				String procCodeX = "";
				procStsCode = "0";
				OccursList tempOccursList = new OccursList();
				tempOccursList = uploadFile.get(i - tableSize);

				this.info("chequeAcct : " + tempOccursList.get("ChequeAcct"));
				this.info("chequeNo : " + tempOccursList.get("ChequeNo"));
				this.info("chequeAmt : " + tempOccursList.get("ChequeAmt"));
				this.info("chequeDate : " + tempOccursList.get("ChequeDateB"));

				int chequeAcct = parse.stringToInteger(tempOccursList.get("ChequeAcct"));
				int chequeNo = parse.stringToInteger(tempOccursList.get("ChequeNo"));
				BigDecimal chequeAmt = parse.stringToBigDecimal(tempOccursList.get("ChequeAmt"));
				int chequeDate = parse.stringToInteger(tempOccursList.get("ChequeDateB"));
				String rvno = parse.IntegerToString(chequeAcct, 9) + " " + parse.IntegerToString(chequeNo, 7);

//				1	ChequeAcct	支票銀行帳號	X	9	0	9	
//				2	ChequeNo	支票號碼	    X	7	9	16	
//				3	ReturnCode	回傳碼	    X	1	16	17	"H.成功 C.抽/退票"
//				4	ChequeDateA	異動日期	    A	7	17	24	YYYMMDD
//				5	ChequeAmt	金額	        A	10	24	34	
//				6	ChequeDateB	到期日期	    A	7	34	41	YYYMMDD
//				7	EntryDate	入帳日期	    A	7	41	48	YYYMMDD 若為抽/退票，此欄位為空值

				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();

//				檢查LoanCheque
				LoanChequeId tLoanChequeId = new LoanChequeId();
				LoanCheque tLoanCheque = new LoanCheque();
				tLoanChequeId.setChequeAcct(chequeAcct);
				tLoanChequeId.setChequeNo(chequeNo);
				tLoanCheque = loanChequeService.findById(tLoanChequeId, titaVo);

				this.info("Cheque ReturnCode : " + tempOccursList.get("ReturnCode"));

//              交換通過
				if (!"H".equals(tempOccursList.get("ReturnCode"))) {
					procCode = "00501";
					procStsCode = "1";
				}
				if ("0".equals(procStsCode) && tLoanCheque == null) {
					procCode = "E0014"; // 檔案錯誤
					procCodeX = "支票檔不存在";
					procStsCode = "1";
				}
				if ("0".equals(procStsCode) && chequeAmt.compareTo(tLoanCheque.getChequeAmt()) != 0) {
					procCode = "E0015"; // 檢查錯誤
					procCodeX = "支票檔金額不合";
					procStsCode = "1";
				}

				if ("0".equals(procStsCode)) {
//					於AcRecivible.RvNo(需拆字串 : 9票據帳號+1空白+7票號  ), 找到相同者(可能兩筆), 比對加總金額 若不相等則error
//					若相等則利用銷帳檔之戶號額度找尋相關欄位

					sAcReceivable = acReceivableService.acrvRvNoEq("TCK", tLoanCheque.getCustNo(), rvno, this.index,
							this.limit, titaVo);
					lAcReceivable = sAcReceivable == null ? null : sAcReceivable.getContent();

					BigDecimal acrvCheqAmt = BigDecimal.ZERO;

					if (lAcReceivable != null && lAcReceivable.size() != 0) {
						for (AcReceivable tAcReceivable : lAcReceivable) {
							acrvCheqAmt = acrvCheqAmt.add(tAcReceivable.getRvAmt());
							this.info("tAcReceivable : " + tAcReceivable.toString());
						}
					}
//					銷帳檔支票金額不合
					if (lAcReceivable == null || lAcReceivable.size() == 0 || chequeAmt.compareTo(acrvCheqAmt) != 0) {
						procCode = "E0015"; // 檢查錯誤
						procCodeX = "銷帳檔支票金額不合";
						procStsCode = "1";
					} else {
						procCode = "00000";
						chequeSuccCnt = chequeSuccCnt + 1;
						chequeSuccAmt = chequeSuccAmt.add(chequeAmt);
						// update LoanCheque
						LoanCheque t2LoanCheque = loanChequeService.holdById(tLoanCheque.getLoanChequeId(), titaVo);
						t2LoanCheque.setStatusCode("4");
						t2LoanCheque.setEntryDate(parse.stringToInteger(tempOccursList.get("EntryDate")) + 19110000);
						try {
							loanChequeService.update(t2LoanCheque, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0007", "LoanCheque update error : " + e.getErrorMsg());
						}
					}
				}
// 
				if ("0".equals(procStsCode) && procCode.equals("00000")) {
					chequeSuccCnt = chequeSuccCnt + 1;
					chequeSuccAmt = chequeSuccAmt.add(chequeAmt);
				} else {
					chequeFailCnt = chequeFailCnt + 1;
				}

//				 正常、且有銷帳檔
				tempVo = new TempVo();
				tempVo.putParam("Note", "支票帳號 : " + parse.IntegerToString(chequeAcct, 9) + " 支票號碼 : "
						+ parse.IntegerToString(chequeNo, 7) + " 支票金額 : " + chequeAmt);

				tempVo.putParam("Remark", chequeDate + " " + chequeAmt);
				if ("0".equals(procStsCode)) {
					int listCnt = 1;
					for (AcReceivable tAcReceivable : lAcReceivable) {
						tBatxDetailId.setAcDate(iAcDate);
						tBatxDetailId.setBatchNo(iBatchNo);
						tBatxDetailId.setDetailSeq(i + cheqCnt);
						tBatxDetail.setBatxDetailId(tBatxDetailId);

//						控制SEQ
						if (lAcReceivable.size() != listCnt) {
							cheqCnt = cheqCnt + 1;
							listCnt = listCnt + 1;
						}

						this.info("i : " + i);
						this.info("cheqCnt : " + cheqCnt);
						this.info("listCnt : " + listCnt);

//						04.支票兌現
						tBatxDetail.setRepayCode(4);
						tBatxDetail.setFileName(filePath4.substring(filePath4.indexOf("mortgage")));
						tBatxDetail.setEntryDate(parse.stringToInteger(tempOccursList.get("EntryDate")) + 19110000);
						tBatxDetail.setCustNo(tLoanCheque.getCustNo());
						tBatxDetail.setFacmNo(tAcReceivable.getFacmNo());
						tBatxDetail.setRvNo(rvno);
						if ("".equals(tLoanCheque.getUsageCode())) {
							tBatxDetail.setRepayType(1);
						} else {
							tBatxDetail.setRepayType(parse.stringToInteger(tLoanCheque.getUsageCode()));
						}
						tBatxDetail.setReconCode("TCK");
						tBatxDetail.setRepayAcCode("");
						tBatxDetail.setRepayAmt(tAcReceivable.getRvAmt());
						tBatxDetail.setDisacctAmt(BigDecimal.ZERO);
						tBatxDetail.setProcStsCode(procStsCode);
						tBatxDetail.setProcCode(procCode);
						tBatxDetail.setProcNote(tempVo.getJsonString());
						tBatxDetail.setTitaTlrNo(iTlrNo);
						tBatxDetail.setTitaTxtNo(iBatchNo.substring(4) + FormatUtil.pad9("" + (i + 1), 6));
						tBatxDetail.setMediaDate(0);
						tBatxDetail.setMediaKind("");
						tBatxDetail.setMediaSeq(0);
// 						AML 姓名檢核
						txAmlCom.setTxBuffer(this.getTxBuffer());
						tempVo = txAmlCom.batxCheque(tempVo, tLoanCheque, iBatchNo, titaVo);
						tBatxDetail.setProcNote(tempVo.getJsonString());

						try {
							batxDetailService.insert(tBatxDetail, titaVo);
						} catch (DBException e) {
							throw new LogicException("E0005", "BatxDetail Insert Fail : " + e.getErrorMsg());
						}
					}
				} else {
					tBatxDetailId.setAcDate(iAcDate);
					tBatxDetailId.setBatchNo(iBatchNo);
					tBatxDetailId.setDetailSeq(i + cheqCnt);
					tBatxDetail.setBatxDetailId(tBatxDetailId);

//					04.支票兌現
					tBatxDetail.setRepayCode(4);
					tBatxDetail.setFileName(filePath4.substring(filePath4.indexOf("mortgage")));
					tBatxDetail.setEntryDate(parse.stringToInteger(tempOccursList.get("EntryDate")) + 19110000);
					if (tLoanCheque != null) {
						tBatxDetail.setCustNo(tLoanCheque.getCustNo());
						tBatxDetail.setRepayType(parse.stringToInteger(tLoanCheque.getUsageCode()));
					}
					tBatxDetail.setFacmNo(0);
					tBatxDetail.setRvNo(FormatUtil.padX(tempOccursList.get("ChequeAcct"), 9) + " "
							+ FormatUtil.padX(tempOccursList.get("ChequeNo"), 7));
					tBatxDetail.setReconCode("TCK");
					tBatxDetail.setRepayAcCode("");
					tBatxDetail.setRepayAmt(chequeAmt);
					tBatxDetail.setDisacctAmt(BigDecimal.ZERO);

					tBatxDetail.setProcStsCode(procStsCode);
					tempVo.putParam("ReturnMsg", chequeNo + " " + chequeAmt);
					tempVo.putParam("CheckMsg", setProcCodeX(procCode, procCodeX, titaVo));

					tBatxDetail.setProcCode(procCode);
					tBatxDetail.setProcNote(tempVo.getJsonString());

					tBatxDetail.setTitaTlrNo(iTlrNo);
					tBatxDetail.setTitaTxtNo(iBatchNo.substring(4) + FormatUtil.pad9("" + (i + 1), 6));
					tBatxDetail.setMediaDate(0);
					tBatxDetail.setMediaKind("");
					tBatxDetail.setMediaSeq(0);

					try {
						batxDetailService.insert(tBatxDetail, titaVo);
					} catch (DBException e) {
						e.printStackTrace();
						throw new LogicException("E0005", "BatxDetail Insert Fail");
					}
				}
			}
		}
	}

	/* 支票兌現檔整批入帳檢核，逾期未兌現時(本埠：到期日+一營業日 外埠：到期日+兩營業日)寫入應處理清單 */
	private void txToDoCheque(TitaVo titaVo) throws LogicException {
		txToDoCom.setTxBuffer(this.txBuffer);
		dateUtil.setDate_1(this.txBuffer.getMgBizDate().getLbsDyf());
		TxBizDate bizDate = dateUtil.getForTxBizDate();
		// 前一營業日
		int LbsDy = this.txBuffer.getMgBizDate().getLbsDy();
		// 前二營業日
		int LLbsDy = bizDate.getLbsDy();
		List<String> lStatus = new ArrayList<String>();
		lStatus.add("0"); // 0: 未處理

		sLoanCheque = loanChequeService.statusCodeRange(lStatus, 0, LbsDy + 19110000, this.index, this.limit, titaVo);

		lLoanCheque = sLoanCheque == null ? null : sLoanCheque.getContent();

		if (lLoanCheque != null && lLoanCheque.size() != 0) {
			for (LoanCheque c : lLoanCheque) {
				// OutsideCode 本埠外埠 1: 本埠 2: 外埠
				if (("1".equals(c.getOutsideCode()) && c.getChequeDate() <= LbsDy)
						|| ("2".equals(c.getOutsideCode()) && c.getChequeDate() <= LLbsDy)) {
					TxToDoDetail tTxToDoDetail = new TxToDoDetail();
					tTxToDoDetail.setItemCode("CHCK00"); // 支票兌現檢核
					tTxToDoDetail.setCustNo(c.getCustNo());
					tTxToDoDetail.setDtlValue(c.getChequeAcct() + " " + c.getChequeNo()); // 支票帳號 + 支票號碼
					if ("1".equals(c.getOutsideCode()))
						tTxToDoDetail.setProcessNote("本埠票 到期日" + c.getChequeDate() + "<=" + LbsDy);
					else
						tTxToDoDetail.setProcessNote("外埠票 到期日" + c.getChequeDate() + "<=" + LLbsDy);
					txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
				}
			}
		}
	}

	private class tmpCheque {

		private int chequeAcct = 0;
		private int chequeNo = 0;

		private tmpCheque(int chequeAcct, int chequeNo) {
			this.setChequeAcct(chequeAcct);
			this.setChequeNo(chequeNo);
		}

//		private BigDecimal getChequeAcct() {
//			return chequeAcct;
//		}

		private void setChequeAcct(int chequeAcct) {
			this.chequeAcct = chequeAcct;
		}

//		private int getChequeNo() {
//			return chequeNo;
//		}

		private void setChequeNo(int chequeNo) {
			this.chequeNo = chequeNo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + chequeAcct;
			result = prime * result + chequeNo;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpCheque other = (tmpCheque) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (chequeAcct == 0) {
				if (other.chequeAcct != 0)
					return false;
			} else if (chequeAcct != other.chequeAcct)
				return false;
			if (chequeNo != other.chequeNo)
				return false;
			return true;
		}

		private L4200Batch getEnclosingInstance() {
			return L4200Batch.this;
		}
	}

	private String setProcCodeX(String procCode, String procCodeX, TitaVo titaVo) {
		String result = "";
		if (procCode.isEmpty()) {
			return result;
		}
		if ("00000".equals(procCode)) {
			return result;
		}
		//
		if ("E".equals(procCode.substring(0, 1))) {
			TxErrCode tTxErrCode = txErrCodeService.findById(procCode, titaVo);
			if (tTxErrCode == null) {
				result = procCode;
			} else {
				result = tTxErrCode.getErrContent();
			}
			return result;
		}
		//
		CdCode tCdCode = cdCodeService.findById(new CdCodeId("ProcCode", procCode), titaVo);
		if (tCdCode == null) {
			result = procCode;
		} else {
			result = tCdCode.getItem();
		}

		if (!"".equals(procCodeX)) {
			result += ", " + procCodeX;
		}
		return result;
	}

	private void initialFileVoOccursList() {
		ArrayList<OccursList> occursList = new ArrayList<OccursList>();
		bankRmtfFileVo.setOccursList(occursList);
		achDeductFileVo.setOccursList(occursList);
		postDeductFileVo.setOccursList(occursList);
		empDeductFileVo.setOccursList(occursList);
		batxChequeFileVo.setOccursList(occursList);
	}

	private void updateBankDeductDtl(int mediaDate, String mediaKind, int mediaSeq, String returnCode, TitaVo titaVo)
			throws LogicException {
		sBankDeductDtl = bankDeductDtlService.mediaSeqRng(mediaDate + 19110000, mediaKind, mediaSeq, this.index,
				this.limit, titaVo);
		lBankDeductDtl = sBankDeductDtl == null ? null : sBankDeductDtl.getContent();

		if (lBankDeductDtl != null && lBankDeductDtl.size() != 0) {
			for (BankDeductDtl tB : lBankDeductDtl) {
				BankDeductDtl tBankDeductDtl = bankDeductDtlService.holdById(tB, titaVo);
				tBankDeductDtl.setReturnCode(returnCode);
				try {
					bankDeductDtlService.update(tBankDeductDtl, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", " L4200Batch bankDeductDtlService update " + e.getErrorMsg());
				}
				// 扣款銀行
				tempVo.putParam("RepayBank", tBankDeductDtl.getRepayBank());
				// 銀扣期款應繳日
				if (tBankDeductDtl.getRepayType() == 1) {
					tempVo.putParam("PayIntDate", tBankDeductDtl.getPayIntDate());
				}
			}

		}

	}

	private String removeDot(String input) {

		if (input.indexOf(".") >= 0) {
			input = input.substring(0, input.indexOf("."));
		}

		return input;
	}

	private void checkFile(String fileName, TitaVo titaVo) throws LogicException {
		this.info("checkFile Start...");
//		1.檢核檔名，不為下列區間者為False
//		2.檢核檔案內容，若格式不合，此階段先提出錯誤
//		3.檢核該檔名是否已上傳過，若上傳過，該批只能是已刪除，否則為False

		this.info("fileName ..." + fileName);

		String[] filelist = fileName.split(";");

		if (filelist == null || filelist.length == 0) {
			sendMsg = sendMsg + "請輸入檔案。";
			checkFlag = false;
		}

		for (String filename : filelist) {
//			每次執行setValue後，需初始化occursList
			initialFileVoOccursList();

			this.info("filename ..." + filename);

			String filePath = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
					+ File.separatorChar + filename;

			ArrayList<String> dataLineList1 = new ArrayList<>();
			try {
				dataLineList1 = fileCom.intputTxt(filePath, "big5");
			} catch (IOException e) {
				this.info("L4200Batch(" + filePath + ") : " + e.getMessage());
			}

			if (filename.indexOf("rbalmall") >= 0) {
				try {
					bankRmtfFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("AHR11P") >= 0) {
				try {
					achDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("AHR12P") >= 0) {
				try {
					achDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("PRSBCP4_53N") >= 0) {
				try {
					postDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("PRSBCP4_8460001") >= 0) {
				try {
					postDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("PRSBCP4_8460002") >= 0) {
				try {
					postDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("10H00") >= 0) {
				try {
					empDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("LNM617P") >= 0) {
				try {
					empDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("mortgage") >= 0) {
				try {
					batxChequeFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else {
				sendMsg = sendMsg + removeDot(filename) + "，檔名不符本交易處理範圍。";
				checkFlag = false;
				break;
			}

			if (checkFlag) {
				sBatxDetail = batxDetailService.fileCheck(iAcDate, filename, this.index, this.limit, titaVo);

				lBatxDetail = sBatxDetail == null ? null : sBatxDetail.getContent();

				if (lBatxDetail != null && lBatxDetail.size() != 0) {
					BatxHead tBatxHead = new BatxHead();
					BatxHeadId tBatxHeadId = new BatxHeadId();

					tBatxHeadId.setAcDate(lBatxDetail.get(0).getAcDate() + 19110000);
					tBatxHeadId.setBatchNo(lBatxDetail.get(0).getBatchNo());
					tBatxHead = batxHeadService.findById(tBatxHeadId, titaVo);

					if (tBatxHead == null) {
						sendMsg = sendMsg + removeDot(filename) + "，明細檔存在，總數檔不存在";
						checkFlag = false;
						break;
					}
					if ("8".equals(tBatxHead.getBatxExeCode())) {
						checkFlag = true;
					} else {
						sendMsg = sendMsg + removeDot(filename) + "，已存在，需先刪除此批號 : " + lBatxDetail.get(0).getBatchNo();
						checkFlag = false;
						break;
					}
				} else {
					checkFlag = true;
				}
			}
		}
	}

	private void checkBatxHead(BatxHeadId tBatxHeadId, BatxHead tBatxHead, TitaVo titaVo) throws LogicException {
		totSuccCnt = totSuccCnt + rmftSuccCnt + achSuccCnt + postSuccCnt + empASuccCnt + empBSuccCnt + chequeSuccCnt;
		totFailCnt = totFailCnt + rmftFailCnt + achFailCnt + postFailCnt + empAFailCnt + empBFailCnt + chequeFailCnt;
		totAmt = totAmt.add(rmftSuccAmt).add(achSuccAmt).add(postSuccAmt).add(empBSuccAmt).add(chequeSuccAmt);

		String batxNoFin = tBatxHeadId.getBatchNo();

		if (totSuccCnt + totFailCnt > 0) {
			tBatxHead = batxHeadService.holdById(tBatxHeadId, titaVo);
			tBatxHead.setBatxTotAmt(totAmt);
			tBatxHead.setBatxTotCnt(totSuccCnt + totFailCnt);
//  		status 皆為0.未檢核 放入放0.未處理 其他則放入1.檢核有誤
			if ("0".equals(procStsCode)) {
				tBatxHead.setBatxExeCode("0");
			} else {
				tBatxHead.setBatxExeCode("1");
			}
			tBatxHead.setBatxStsCode("0");
			tBatxHead.setTitaTlrNo(titaVo.getTlrNo());
			tBatxHead.setTitaTxCd(titaVo.getTxcd());

			try {
				this.info("Insert BatxHead !!!");
				batxHeadService.update(tBatxHead, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "L4200Batch BatxHead update " + e.getErrorMsg());
			}
		}

//		E.回寫Head檔 總金額與總筆數(if與前兩天相同，則丟Warning)
//		本營業日
		int tbsdyf = this.getTxBuffer().getTxCom().getTbsdyf();
//		上營業日
		int lbsdyf = 0;
//		上上營業日
		int lLbsdyf = 0;

//		檢核今日批次若相同於檢核訊息提示警告
		List<BatxHead> toBatxHeadList = new ArrayList<BatxHead>();
		Slice<BatxHead> stodayBatxHeadList = null;

		BatxHead todayBatxHead = new BatxHead();
		BatxHeadId todayBatxHeadId = new BatxHeadId();
		stodayBatxHeadList = batxHeadService.acDateRange(tbsdyf, tbsdyf, this.index, this.limit, titaVo);

		toBatxHeadList = stodayBatxHeadList == null ? null : stodayBatxHeadList.getContent();

		HashMap<Integer, Integer> toBatxNo = new HashMap<>();
		int batxtno = 1;
		toBatxNo.put(tbsdyf, 0);

		if (toBatxHeadList != null && toBatxHeadList.size() != 0) {
			int batchCnt = 0;
			for (int i = 0; i < toBatxHeadList.size(); i++) {
				if ("L4200".equals(toBatxHeadList.get(i).getTitaTxCd())
						&& toBatxHeadList.get(i).getBatxTotCnt() == totSuccCnt
						&& toBatxHeadList.get(i).getBatxTotAmt().compareTo(totAmt) == 0
						&& !batxNoFin.equals(toBatxHeadList.get(i).getBatchNo())
						&& !"8".equals(toBatxHeadList.get(i).getBatxExeCode())) {
//				第一筆自己
					this.info("toBatxHeadList.get(i).getBatchNo() ..." + toBatxHeadList.get(i).getBatchNo());
					this.info("toBatxHeadList.get(i).ID.getBatchNo() ..."
							+ toBatxHeadList.get(i).getBatxHeadId().getBatchNo());
					batchCnt = batchCnt + 1;
					batxtno = parse.stringToInteger(toBatxHeadList.get(i).getBatxHeadId().getBatchNo().substring(4));
//				排除自己
					if (batxtno > toBatxNo.get(tbsdyf) && batchCnt > 0) {
						toBatxNo.put(tbsdyf, batxtno);
					}
				}
			}
		}
		String sBatchNo = "BATX" + FormatUtil.pad9("" + toBatxNo.get(tbsdyf), 2);
		todayBatxHeadId.setAcDate(tbsdyf);
		todayBatxHeadId.setBatchNo(sBatchNo);
		todayBatxHead = batxHeadService.findById(todayBatxHeadId, titaVo);

		lbsdyf = this.getTxBuffer().getTxCom().getLbsdyf();
		this.info("Lbsdyf : " + lbsdyf);

		lLbsdyf = dateUtil.getbussDate(lbsdyf, -1);

		this.info("lLbsdyf : " + lLbsdyf);

//	上營業日之最後一批
		List<BatxHead> yesterdayBatxHeadList = new ArrayList<BatxHead>();
		Slice<BatxHead> syesterdayBatxHeadList = null;

		BatxHead yesterdayBatxHead = new BatxHead();
		BatxHeadId yesterdayBatxHeadId = new BatxHeadId();
		syesterdayBatxHeadList = batxHeadService.acDateRange(lbsdyf, lbsdyf, this.index, this.limit, titaVo);

		yesterdayBatxHeadList = syesterdayBatxHeadList == null ? null : syesterdayBatxHeadList.getContent();

		HashMap<Integer, Integer> yesBatxNo = new HashMap<>();
		yesBatxNo.put(lbsdyf, 0);
		int batxyno = 1;

		if (yesterdayBatxHeadList != null && yesterdayBatxHeadList.size() != 0) {
			for (int i = 0; i < yesterdayBatxHeadList.size(); i++) {
				if ("L4200".equals(yesterdayBatxHeadList.get(i).getTitaTxCd())
						&& yesterdayBatxHeadList.get(i).getBatxTotCnt() == totSuccCnt
						&& yesterdayBatxHeadList.get(i).getBatxTotAmt().compareTo(totAmt) == 0
						&& !"8".equals(yesterdayBatxHeadList.get(i).getBatxExeCode())) {
					batxyno = parse
							.stringToInteger(yesterdayBatxHeadList.get(i).getBatxHeadId().getBatchNo().substring(4));
					if (batxyno > yesBatxNo.get(lbsdyf)) {
						yesBatxNo.put(lbsdyf, batxyno);
					}
				}
			}
		}
		sBatchNo = "BATX" + FormatUtil.pad9("" + yesBatxNo.get(lbsdyf), 2);
		yesterdayBatxHeadId.setAcDate(lbsdyf);
		yesterdayBatxHeadId.setBatchNo(sBatchNo);
		yesterdayBatxHead = batxHeadService.findById(yesterdayBatxHeadId, titaVo);

//	上上營業日之最後一批
		List<BatxHead> dayB4BatxHeadList = new ArrayList<BatxHead>();
		Slice<BatxHead> sdayB4BatxHeadList = null;

		BatxHead dayB4BatxHead = new BatxHead();
		BatxHeadId dayB4BatxHeadId = new BatxHeadId();
		sdayB4BatxHeadList = batxHeadService.acDateRange(lLbsdyf, lLbsdyf, this.index, this.limit, titaVo);

		dayB4BatxHeadList = sdayB4BatxHeadList == null ? null : sdayB4BatxHeadList.getContent();

		HashMap<Integer, Integer> dayB4BatxNo = new HashMap<>();
		dayB4BatxNo.put(lLbsdyf, 0);
		int dayB4Batxno = 1;

		if (dayB4BatxHeadList != null && dayB4BatxHeadList.size() != 0) {
			for (int i = 0; i < dayB4BatxHeadList.size(); i++) {
				if ("BATX".equals(dayB4BatxHeadList.get(i).getBatxHeadId().getBatchNo().substring(0, 4))
						&& dayB4BatxHeadList.get(i).getBatxTotCnt() == totSuccCnt
						&& dayB4BatxHeadList.get(i).getBatxTotAmt().compareTo(totAmt) == 0
						&& !"8".equals(dayB4BatxHeadList.get(i).getBatxExeCode())) {
					dayB4Batxno = parse
							.stringToInteger(dayB4BatxHeadList.get(i).getBatxHeadId().getBatchNo().substring(4));
					if (dayB4Batxno > dayB4BatxNo.get(lLbsdyf)) {
						dayB4BatxNo.put(lLbsdyf, dayB4Batxno);
					}
				}
			}
		}
		String sdayB4BatchNo = "BATX" + FormatUtil.pad9("" + dayB4BatxNo.get(lLbsdyf), 2);
		dayB4BatxHeadId.setAcDate(lLbsdyf);
		dayB4BatxHeadId.setBatchNo(sdayB4BatchNo);
		dayB4BatxHead = batxHeadService.findById(dayB4BatxHeadId, titaVo);

		if (yesterdayBatxHead != null) {
			sendMsg = sendMsg + "L4200" + " - " + iBatchNo + "整批處理完成。與批號 : " + yesterdayBatxHead.getAcDate() + "，"
					+ yesterdayBatxHead.getBatchNo() + " 筆數、金額相同";
		} else if (dayB4BatxHead != null) {
			sendMsg = sendMsg + "L4200" + " - " + iBatchNo + "整批處理完成。與日期 : " + dayB4BatxHead.getAcDate() + "，批號 : "
					+ dayB4BatxHead.getBatchNo() + " 筆數、金額相同";
		} else if (todayBatxHead != null) {
			sendMsg = sendMsg + "L4200" + " - " + iBatchNo + "整批處理完成。與日期 : " + todayBatxHead.getAcDate() + "，批號 : "
					+ todayBatxHead.getBatchNo() + " 筆數、金額相同";
		} else {
			sendMsg = sendMsg + "L4200" + " - " + iBatchNo + "，整批處理完成";
		}

		if (postEntryDateFlag == 1) {
			sendMsg = sendMsg + "，郵局扣款檔入帳日期大於會計日期";
		}
		if (achEntryDateFlag == 1) {
			sendMsg = sendMsg + "，銀行扣款檔入帳日期大於會計日期";
		}
	}
}