package com.st1.itx.trade.L4;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.LoanChequeId;
import com.st1.itx.db.domain.PostDeductMedia;
import com.st1.itx.db.domain.TxErrCode;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.AchDeductMediaService;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.BankRmtfService;
import com.st1.itx.db.service.BatxChequeService;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.EmpDeductDtlService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.service.PostDeductMediaService;
import com.st1.itx.db.service.TxErrCodeService;
import com.st1.itx.trade.BS.BS001;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.TxAmlCom;
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

	/* ?????????????????? */
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
	public EmpDeductDtlService empDeductDtlService;

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
	public LoanChequeService loanChequeService;

	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	public TxErrCodeService txErrCodeService;

	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	public WebClient webClient;

	@Autowired
	public BS001 bs001;

	private int iAcDate = 0;
	private String iBatchNo = "";

	private int tableSize = 0;
	private BigDecimal bigDe100 = new BigDecimal("100");

	private TempVo tempVo = new TempVo();

//	??????????????????
	int rmftSuccCnt = 0;
//	??????????????????
	int rmftFailCnt = 0;
//	??????????????????
	BigDecimal rmftSuccAmt = BigDecimal.ZERO;

//	??????????????????
	int achSuccCnt = 0;
//	??????????????????
	int achFailCnt = 0;
//	??????????????????
	BigDecimal achSuccAmt = BigDecimal.ZERO;

//	??????????????????
	int postSuccCnt = 0;
//	??????????????????
	int postFailCnt = 0;
//	??????????????????
	BigDecimal postSuccAmt = BigDecimal.ZERO;

//	??????????????????
	int empASuccCnt = 0;
//	??????????????????
	int empAFailCnt = 0;
//	??????????????????
	BigDecimal empASuccAmt = BigDecimal.ZERO;

//	??????????????????
	int empBSuccCnt = 0;
//	??????????????????
	int empBFailCnt = 0;
//	??????????????????
	BigDecimal empBSuccAmt = BigDecimal.ZERO;

//	??????????????????
	int chequeSuccCnt = 0;
//	??????????????????
	int chequeFailCnt = 0;
//	??????????????????
	BigDecimal chequeSuccAmt = BigDecimal.ZERO;

//	??????????????????
	private int totSuccCnt = 0;
//	??????????????????
	private int totFailCnt = 0;
//	?????????
	private int totCnt = 0;
//	?????????	
	private BigDecimal totAmt = BigDecimal.ZERO;
//	
	private int meadiaDatePost = 0; // ???????????????
	private int meadiaDateAch1 = 0; // ACH ???????????????
	private int meadiaDateAch2 = 0; // ACH???????????????
	private int fileSeq = 0; // ????????????

	private String procStsCode = "";

	private List<BankDeductDtl> lBankDeductDtl = new ArrayList<BankDeductDtl>();
	private Slice<BankDeductDtl> sBankDeductDtl = null;

	private List<BatxDetail> lBatxDetail = new ArrayList<BatxDetail>();
	private Slice<BatxDetail> sBatxDetail = null;

	private List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
	private Slice<AcReceivable> sAcReceivable = null;

//	????????????
	private int commitCnt = 500;

//	?????????
	private int entryDate = 0;

	private String sendMsg = "";
	private Boolean checkFlag = true;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4200Batch ");
		this.totaVo.init(titaVo);

// ???????????????
//		A.??????Head???
//		  for(4????????????) {
//			B.(first check)  ????????????????????????????????????  ?????????????????????(ProcStsCode)
//			C.(second check) ??????Detail??? (?????????)
//			D.???VO???????????????????????? (Detail???????????????) 
//	      }
//		E.??????Head??? ?????????????????????(if???????????????????????????Warning)

		iAcDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		iBatchNo = titaVo.getParam("BatchNo");

//		???????????? ???????????????????????????????????????

		String filena = titaVo.getParam("FILENA").trim();

		if (filena.indexOf(";") < 0) {
			filena = filena + ";";
		}

//		????????????????????????????????????????????????(???L492A??????????????????????????????)
		checkFile(filena, titaVo);

//		A.??????Head???			
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
//				??????VO???OccursList
				initialFileVoOccursList();

				if (filename.indexOf("rbalmall") >= 0 && checkFlag) {
					String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("??????????????? Start With -> " + filePath1);

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
					this.info("????????????????????? Start With -> " + filePath2A1);

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
					this.info("????????????????????? Start With -> " + filePath2A2);
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
					this.info("????????????????????? Start With -> " + filePath2P1);

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
					this.info("????????????????????? Start With -> " + filePath2P2);

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
					this.info("????????????????????? Start With -> " + filePath2P3);

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
				} else if (filename.indexOf("10H") >= 0 && checkFlag) {
					String filePath31 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("????????????????????? Start With -> " + filePath31);

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

				} else if (filename.indexOf("LNM") >= 0 && checkFlag) {
					String filePath32 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
							+ File.separatorChar + filename;
					this.info("????????????????????? Start With -> " + filePath32);

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
					this.info("??????????????? Start With -> " + filePath4);

					ArrayList<String> dataLineList4 = new ArrayList<>();
					try {
						dataLineList4 = fileCom.intputTxt(filePath4, "UTF-8");
					} catch (IOException e) {
						this.info("L4200Batch(" + filePath4 + ") : " + e.getMessage());
					}

					try {
						procCheque(filePath4, dataLineList4, titaVo);
					} catch (LogicException e) {
						sendMsg = e.getMessage();
						checkFlag = false;
					}
					// ??????????????????
					txToDoCheque(titaVo);
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

//		?????????????????????????????????
		if (checkFlag) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "F", "L4002",
					titaVo.getEntDyI() + "0" + titaVo.getTlrNo(), sendMsg, titaVo);
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
			// ?????????????????????????????????????????????
			bankRmtfFileVo.setValueFromFile(dataLineList1);

			// ????????????
			ArrayList<OccursList> uploadFile = bankRmtfFileVo.getOccursList();

//			????????????:n 				uploadFile.size()
//			?????????,??????,?????????,???????????????:m	lBatxDetail.size()
//			loop : m+1~m+n 
			sBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, 0, Integer.MAX_VALUE, titaVo);

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
				this.info("OccursList=" + tempOccursList.toString());
				int custNo = 0;
				int repayType = 0;
				if (isNumeric(tempOccursList.get("OccVirAcctNo"))) {
					custNo = parse.stringToInteger(tempOccursList.get("OccVirAcctNo").substring(7));
				} else {
					custNo = 0;
				}

//				B.(first check)????????????????????????????????????  ?????????????????????(ProcStsCode)
//				??????????????????????????????
//				1.??????????????? :  101-???????????? (????????????) 
//				2.??????????????? :  102-??????(??????)
//				3.??????????????? :  104-ACH????????? (???3??????ACH) 120-?????? (???????????????)   106-???????????? (????????????else or ????????????????????? or ???????????????0) 
//				            110-???????????? (OccRemark???????????????)
//				4.??? : 103-????????????(??????????????????ORTX ????????????&??????)  105-????????????  (?????? ?????? ??????)
				String errorCode = "00000";
				String procCodeX = "";
				BigDecimal repayAmt = parse.stringToBigDecimal(tempOccursList.get("OccRepayAmt"));
				String reconCode = "";
				procStsCode = "0";
				if (isNumeric(tempOccursList.get("OccVirAcctNo"))) {
					switch (tempOccursList.get("OccVirAcctNo").substring(0, 5)) {
					case "95101":
						reconCode = "A1";
						break;
					case "95102":
						repayType = 1;
						reconCode = "A2";
						break;
					case "95103":
						reconCode = "A" + tempOccursList.get("OccVirAcctNo").substring(4, 5);
						break;
					case "95105":
						repayType = 11;
						if (custNo == this.txBuffer.getSystemParas().getNegDeptCustNo()) {
							reconCode = "A7";
						} else {
							reconCode = "A6";
						}
						break;
					default:
						reconCode = "A3";
						break;
					}
				} else {
					reconCode = "A3";
					if (tempOccursList.get("OccVirAcctNo").indexOf("??????") >= 0) {
						errorCode = "00120";
						procStsCode = "2";
						procCodeX = tempOccursList.get("OccVirAcctNo");
					} else if ("ACH".equals(tempOccursList.get("OccVirAcctNo").substring(0, 3))) {
						errorCode = "00104";
						procStsCode = "1";
						procCodeX = tempOccursList.get("OccVirAcctNo");
					} else {
						errorCode = "00105"; // ????????????
						procStsCode = "1";
						procCodeX = tempOccursList.get("OccVirAcctNo");
					}
				}

				// ????????????
				if ("00000".equals(errorCode)) {
					if ("1".equals(tempOccursList.get("OccEraseFlag"))) {
						errorCode = "00101";
						procStsCode = "1";
					}
				}

				// ??????(??????)
				if ("00000".equals(errorCode) && isNumeric(tempOccursList.get("OccWithDrawAmt")) && parse
						.stringToBigDecimal(tempOccursList.get("OccWithDrawAmt")).compareTo(BigDecimal.ZERO) > 0) {
					errorCode = "00102";
					procStsCode = "1";
				}
				// ??????(??????)
				if ("00000".equals(errorCode) && tempOccursList.get("OccWithDrawAmt").indexOf("p") >= 0
						&& parse.stringToBigDecimal(tempOccursList.get("OccWithDrawAmt").substring(0, 12) + "0")
								.compareTo(BigDecimal.ZERO) > 0) {
					errorCode = "00102";
					procStsCode = "1";
					procCodeX = "??????";
				}
				// ????????????
				if ("00000".equals(errorCode) && tempOccursList.get("OccRemark").indexOf("????????????") >= 0) {
					errorCode = "00110";
					procStsCode = "1";
				}
				// ????????????
				if ("00000".equals(errorCode) && repayAmt.compareTo(BigDecimal.ZERO) < 0) {
					errorCode = "00106";
					procStsCode = "1";
				}

				// ???????????? ?????????????????? ???????????? ?????? ?????? ?????????
				if ("00000".equals(errorCode)) {
					List<String> procStsCodeList = Arrays.asList("5", "6", "7");
					BatxDetail err103 = batxDetailService.findL4200BFirst(this.txBuffer.getTxCom().getLbsdyf(), "L4210",
							custNo, repayAmt, procStsCodeList, titaVo);
					if (err103 != null) {
						errorCode = "00103";
						procCodeX = "" + this.txBuffer.getTxCom().getLbsdy() + " " + err103.getBatchNo();
					}
				}

				this.info("OccEraseFlag : " + tempOccursList.get("OccEraseFlag"));
				this.info("RepayAmt : " + repayAmt);
				this.info("errorCode : " + errorCode);

				if ("00000".equals(errorCode)) {
					rmftSuccCnt = rmftSuccCnt + 1;
					rmftSuccAmt = rmftSuccAmt.add(repayAmt);
				} else {
					rmftFailCnt = rmftFailCnt + 1;
				}

//			C.??????Detail???(second check) 
				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();

				tBatxDetailId.setAcDate(iAcDate);
				tBatxDetailId.setBatchNo(iBatchNo);
				tBatxDetailId.setDetailSeq(i + 1);
				tBatxDetail.setBatxDetailId(tBatxDetailId);
				tBatxDetail.setRepayCode(1);
				tBatxDetail.setFileName(filePath1.substring(filePath1.indexOf("rbalmall")));
				tBatxDetail.setEntryDate(parse.stringToInteger(tempOccursList.get("OccEntryDate")));
				tBatxDetail.setCustNo(custNo);
				tBatxDetail.setFacmNo(0);
				tBatxDetail.setRvNo("");
				tBatxDetail.setRepayType(repayType);
				tBatxDetail.setReconCode(reconCode);
				tBatxDetail.setRepayAcCode("");
				tBatxDetail.setRepayAmt(repayAmt);
				tBatxDetail.setAcquiredAmt(BigDecimal.ZERO);
				tBatxDetail.setAcctAmt(BigDecimal.ZERO);
				tBatxDetail.setDisacctAmt(BigDecimal.ZERO);
				tBatxDetail.setProcStsCode(procStsCode);
				tBatxDetail.setProcCode(errorCode);
				tempVo = new TempVo();
				tempVo.putParam("VirtualAcctNo", tempOccursList.get("OccVirAcctNo"));
				tempVo.putParam("DscptCode", tempOccursList.get("OccNoteCode"));
				if ("00000".equals(errorCode)) {
					tempVo.putParam("CheckMsg", "");
					tempVo.putParam("Note", tempOccursList.get("OccRemark"));
				} else {
					tempVo.putParam("CheckMsg",
							setProcCodeX(errorCode, procCodeX, titaVo) + ", " + tempOccursList.get("OccRemark"));
				}

				tBatxDetail.setTitaTlrNo("");
				tBatxDetail.setTitaTxtNo("");
				tBatxDetail.setMediaDate(0);
				tBatxDetail.setMediaKind("");
				tBatxDetail.setMediaSeq(i + 1);

//			D.???VO???????????????Table (BankRmtf?????????)
				tBankRmtfId.setAcDate(iAcDate);
				tBankRmtfId.setBatchNo(iBatchNo);
				tBankRmtfId.setDetailSeq(i + 1);
				tBankRmtf.setBankRmtfId(tBankRmtfId);
				tBankRmtf.setAcDate(iAcDate);
				tBankRmtf.setBatchNo(iBatchNo);
				tBankRmtf.setDetailSeq(i + 1);
				tBankRmtf.setCustNo(custNo);
				tBankRmtf.setRepayAmt(repayAmt);
				tBankRmtf.setDepAcctNo(tempOccursList.get("OccAcctNo"));
				tBankRmtf.setEntryDate(parse.stringToInteger(tempOccursList.get("OccEntryDate")));
				tBankRmtf.setDscptCode(tempOccursList.get("OccNoteCode"));
				tBankRmtf.setVirtualAcctNo(tempOccursList.get("OccVirAcctNo"));
				if (tempOccursList.get("OccWithDrawAmt").indexOf("p") >= 0) {
					tBankRmtf.setWithdrawAmt(BigDecimal.ZERO.subtract(
							parse.stringToBigDecimal(tempOccursList.get("OccWithDrawAmt").substring(0, 12) + "0")
									.divide(bigDe100)));
				} else {
					tBankRmtf.setWithdrawAmt(
							parse.stringToBigDecimal(tempOccursList.get("OccWithDrawAmt")).divide(bigDe100));
				}

				if (tempOccursList.get("OccDepositAmt").indexOf("p") >= 0) {
					tBankRmtf.setDepositAmt(BigDecimal.ZERO.subtract(
							parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt").substring(0, 12) + "0")
									.divide(bigDe100)));
				} else {
					tBankRmtf.setDepositAmt(
							parse.stringToBigDecimal(tempOccursList.get("OccDepositAmt")).divide(bigDe100));
				}

				tBankRmtf.setBalance(parse.stringToBigDecimal(tempOccursList.get("OccBalance")).divide(bigDe100));
				tBankRmtf.setRemintBank(tempOccursList.get("OccBankCode"));
				tBankRmtf.setTraderInfo(tempOccursList.get("OccTrader"));
				try {
					bankRmtfService.insert(tBankRmtf, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "tBankRmtf Insert Fail : " + e.getErrorMsg());
				}
//				E.AML ????????????
				txAmlCom.setTxBuffer(this.getTxBuffer());
				tempVo = txAmlCom.bankRmtf(tempVo, tBankRmtf, titaVo);
				tBatxDetail.setProcNote(tempVo.getJsonString());
//				F.insert BankRmtf???BatxDetail
				totAmt = totAmt.add(tBatxDetail.getRepayAmt());
				entryDate = tBatxDetail.getEntryDate();
				try {
					batxDetailService.insert(tBatxDetail, titaVo);
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException("E0005", "BatxDetail Insert Fail");
				}

			}

		} else

		{
//			throw new LogicException("E0014", "L4200Batch No Content " + filePath1);
		}

	}

	private void procAchDeduct(String filePath2A, ArrayList<String> dataLineList2A, TitaVo titaVo)
			throws LogicException {

		// ????????????????????????UTF-8 || big5

		if (dataLineList2A != null && dataLineList2A.size() != 0) {
			// ?????????????????????????????????????????????

			achDeductFileVo.setValueFromFile(dataLineList2A);
			ArrayList<OccursList> uploadFile = achDeductFileVo.getOccursList();

//			int reEntryDate = parse.stringToInteger("" + achDeductFileVo.get("HeadProcessDate"));
			String procCode = "00000";
			String procCodeX = "";

			sBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, 0, Integer.MAX_VALUE, titaVo);

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

				fileSeq = i - tableSize + 1;
				OccursList tempOccursList = new OccursList();
				tempOccursList = uploadFile.get(i - tableSize);

				AchDeductMedia tAchDeductMedia = new AchDeductMedia();

//				B.(first check)????????????????????????????????????  ?????????????????????(ProcStsCode(1) ProcCode(5) AcquiredAmt)
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

//				C.??????Detail & ???????????????(second check) 
				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();

				tempVo = new TempVo();
				tempVo.putParam("FileSeq", fileSeq);

				int achEntryDate = 0;
				int achRepayType = 0;
				String returnCode = tempOccursList.get("OccReturnCode");
				if (tAchDeductMedia == null) {
					// ?????????????????????
					procCode = "E0014"; // ????????????
					procCodeX = "?????????????????????";
				} else {
					if ("00".equals(returnCode)) {
						procCode = "00000";
					} else {
						procCode = "002" + returnCode;
					}

					achEntryDate = tAchDeductMedia.getEntryDate();
					achRepayType = tAchDeductMedia.getRepayType();
//					???????????????
					tAchDeductMedia = achDeductMediaService.holdById(tAchDeductMedia, titaVo);
					tAchDeductMedia.setReturnCode(tempOccursList.get("OccReturnCode"));
					tAchDeductMedia.setAcDate(iAcDate);
					tAchDeductMedia.setBatchNo(iBatchNo);
					tAchDeductMedia.setDetailSeq(i + 1);
					if ("1".equals(tAchDeductMedia.getMediaKind())) {
						meadiaDateAch1 = tAchDeductMedia.getMediaDate();
					} else {
						meadiaDateAch2 = tAchDeductMedia.getMediaDate();
					}
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
//				02.????????????
				tBatxDetail.setRepayCode(2);
				tBatxDetail.setFileName(filePath2A.substring(filePath2A.indexOf("AHR1")));
				tBatxDetail.setEntryDate(achEntryDate);
				tBatxDetail.setCustNo(reCustNo);
				tBatxDetail.setFacmNo(reFacmNo);
				tBatxDetail.setRvNo("");
				tBatxDetail.setRepayType(achRepayType);
				tBatxDetail.setReconCode("C01"); // ?????????????????????????????????
				tBatxDetail.setRepayAcCode("");
				tBatxDetail.setRepayAmt(reRepayAmt);

				if ("00000".equals(procCode)) {
					tBatxDetail.setProcStsCode("0");
//					????????????
					procStsCode = "0";
					achSuccCnt = achSuccCnt + 1;
					achSuccAmt = achSuccAmt.add(reRepayAmt);
				} else if ("E".equals(procCode.substring(0, 1))) {
					tBatxDetail.setProcStsCode("1");
//					????????????  --????????????
					procStsCode = "1";
					achFailCnt = achFailCnt + 1;
				} else {
					tBatxDetail.setProcStsCode("1");
//					????????????00????????? --?????????
					procStsCode = "1";
					achFailCnt = achFailCnt + 1;
				}

				tBatxDetail.setProcCode(procCode);

				// ???????????????
				if ("00000".equals(procCode)) {
//					tempVo.putParam("Note", tempOccursList.get("OccReturnCode"));
					updateBankDeductDtl(tAchDeductMedia.getMediaDate(), tAchDeductMedia.getMediaKind(),
							tAchDeductMedia.getMediaSeq(), returnCode, titaVo);
				} else if ("E".equals(procCode.substring(0, 1))) {
					tempVo.putParam("CheckMsg", setProcCodeX(procCode, procCodeX, titaVo));
				} else {
//					??????????????? code+cdCode.item
					tempVo.putParam("CheckMsg", setProcCodeX(procCode, procCodeX, titaVo));
//					?????????
					updateBankDeductDtl(tAchDeductMedia.getMediaDate(), tAchDeductMedia.getMediaKind(),
							tAchDeductMedia.getMediaSeq(), returnCode, titaVo);
				}
				this.info("1132...");

				tBatxDetail.setProcNote(tempVo.getJsonString());

				tBatxDetail.setTitaTlrNo("");
				tBatxDetail.setTitaTxtNo("");
				if (tAchDeductMedia != null) {
					tBatxDetail.setMediaDate(tAchDeductMedia.getMediaDate());
					tBatxDetail.setMediaKind(tAchDeductMedia.getMediaKind());
					tBatxDetail.setMediaSeq(tAchDeductMedia.getMediaSeq());
				}
				totAmt = totAmt.add(tBatxDetail.getRepayAmt());
				entryDate = tBatxDetail.getEntryDate();
				try {
					batxDetailService.insert(tBatxDetail, titaVo);
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException("E0005", "BatxDetail Insert Fail");
				}
			}
		}
	}

//	????????????
	private void procPostDeduct(String filePath2P, ArrayList<String> dataLineList2P, TitaVo titaVo)
			throws LogicException {

		// ????????????????????????UTF-8 || big5
		try {
			dataLineList2P = fileCom.intputTxt(filePath2P, "UTF-8");
		} catch (IOException e) {
//			throw new LogicException("E0014", "L4200Batch(" + filePath2P + ")");
		}

		if (dataLineList2P != null && dataLineList2P.size() != 0) {
			// ?????????????????????????????????????????????
			postDeductFileVo.setValueFromFile(dataLineList2P);
			ArrayList<OccursList> uploadFile = postDeductFileVo.getOccursList();
			String procCode = "00000";

			sBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, 0, Integer.MAX_VALUE, titaVo);

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

				fileSeq = i - tableSize + 1;
				OccursList tempOccursList = new OccursList();
				tempOccursList = uploadFile.get(i - tableSize);

				PostDeductMedia tPostDeductMedia = new PostDeductMedia();

//			B.(first check)????????????????????????????????????  ?????????????????????(ProcStsCode)
				BigDecimal reRepayAmt = parse.stringToBigDecimal(tempOccursList.get("OccRepayAmt")).divide(bigDe100);
				int reEntryDate = parse.stringToInteger(tempOccursList.get("OccTxDate"));
				int postRepayType = 0;
				String procCodeX = "";
//				PostUserNo = ,AND RepayAmt = ,AND OutsrcRemark = 
				tPostDeductMedia = postDeductMediaService.receiveCheckFirst(
						FormatUtil.padLeft(tempOccursList.get("OccCustMemo").trim(), 20), reRepayAmt,
						FormatUtil.padX(tempOccursList.get("OccRemark"), 20), titaVo);
				String returnCode = FormatUtil.pad9(tempOccursList.get("OccReturnCode").trim(), 2);

//			C.??????Detail???(second check) 
				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();
				tempVo = new TempVo();
				tempVo.putParam("FileSeq", fileSeq);

				if (tPostDeductMedia != null) {
					// mediaDate = tPostDeductMedia.getMediaDate();
					postRepayType = tPostDeductMedia.getRepayType();
//					}
					if ("00".equals(returnCode)) {
						procCode = "00000";
					} else {
						procCode = "003" + returnCode;
					}
					tBatxDetail.setProcCode(procCode);

//					???????????????
					tPostDeductMedia = postDeductMediaService.holdById(tPostDeductMedia, titaVo);
					tPostDeductMedia.setProcNoteCode(tempOccursList.get("OccReturnCode"));
					tPostDeductMedia.setAcDate(iAcDate);
					tPostDeductMedia.setBatchNo(iBatchNo);
					tPostDeductMedia.setDetailSeq(i + 1);
					meadiaDatePost = tPostDeductMedia.getMediaDate();
					try {
						postDeductMediaService.update(tPostDeductMedia, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "PostDeductMedia update Fail");
					}

				} else {
					// ?????????????????????
					tBatxDetail.setProcCode("E0014"); // ????????????
					procCode = "E0014";
					procCodeX = "?????????????????????";

				}

				tBatxDetailId.setAcDate(iAcDate);
				tBatxDetailId.setBatchNo(iBatchNo);
				tBatxDetailId.setDetailSeq(i + 1);
				tBatxDetail.setBatxDetailId(tBatxDetailId);
//				02.????????????
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

				// ????????????
				if ("00000".equals(procCode)) {
					tBatxDetail.setProcStsCode("0"); // ?????????
					procStsCode = "0";
					postSuccCnt = postSuccCnt + 1;
					postSuccAmt = postSuccAmt.add(reRepayAmt);
				}
				// ????????????
				else if ("E".equals(procCode.substring(0, 1))) {
					tBatxDetail.setProcStsCode("1"); // ?????????
					procStsCode = "1";
					postFailCnt = postFailCnt + 1;
				}
				// ????????????
				else {
					tBatxDetail.setProcStsCode("1");// ?????????
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
//					??????????????? code+cdCode.item
					tempVo.putParam("CheckMsg", setProcCodeX(procCode, procCodeX, titaVo));
//					?????????
					updateBankDeductDtl(tPostDeductMedia.getMediaDate(), "3", tPostDeductMedia.getMediaSeq(),
							returnCode, titaVo);
				}

				tBatxDetail.setProcNote(tempVo.getJsonString());
				tBatxDetail.setTitaTlrNo("");
				tBatxDetail.setTitaTxtNo("");
				if (tPostDeductMedia != null) {
					tBatxDetail.setMediaDate(tPostDeductMedia.getMediaDate());
					tBatxDetail.setMediaKind("3");
					tBatxDetail.setMediaSeq(tPostDeductMedia.getMediaSeq());
				}

				totAmt = totAmt.add(tBatxDetail.getRepayAmt());
				entryDate = tBatxDetail.getEntryDate();
				try {
					batxDetailService.insert(tBatxDetail, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "BatxDetail Insert Fail");
				}

//			D.???VO???????????????Table (Detail???????????????)

			}
		} else {
//			throw new LogicException("E0014", "L4200Batch No Content " + filePath2P);
		}
	}

	private void procEmpDeduct(String filePath3, ArrayList<String> dataLineList3, TitaVo titaVo) throws LogicException {

		String mediaType = "";

		if (filePath3.indexOf("10H") >= 0) {
			mediaType = "4";
		} else if (filePath3.indexOf("LNM") >= 0) {
			mediaType = "5";
		}

		if (dataLineList3 != null && dataLineList3.size() != 0) {
			// ?????????????????????????????????????????????
			empDeductFileVo.setValueFromFile(dataLineList3);
			ArrayList<OccursList> uploadFile = empDeductFileVo.getOccursList();

			String procCode = "00000";
			String procCodeX = "";
			sBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, 0, Integer.MAX_VALUE, titaVo);

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
				fileSeq = i - tableSize + 1;
				tempVo = new TempVo();
				tempVo.putParam("FileSeq", fileSeq);

//			B.(first check)????????????????????????????????????  ?????????????????????(ProcStsCode)
				EmpDeductMedia tEmpDeductMedia = new EmpDeductMedia();

				BigDecimal reRepayAmt = parse.stringToBigDecimal(tempOccursList.get("OccRepayAmt")); // ????????????
				BigDecimal reAcctAmt = parse.stringToBigDecimal(tempOccursList.get("OccAcctAmt")); // ????????????

				int reRepayCode = 0;
				if ("XH".equals(tempOccursList.get("OccUnknowC"))) {
					reRepayCode = 1;
				} else if ("92".equals(tempOccursList.get("OccUnknowC"))) {
					reRepayCode = 5;
				}

				tEmpDeductMedia = empDeductMediaService.receiveCheckFirst(mediaType,
						parse.stringToInteger(tempOccursList.get("OccCustNo")),
						parse.stringToInteger(tempOccursList.get("OccEntryDate")), reRepayCode, reRepayAmt, titaVo);

//			C.??????Detail???(second check) 
				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();

				if (tEmpDeductMedia == null) {
					procCode = "E0014"; // ????????????
					procCodeX = "?????????????????????";
				} else {
					Slice<EmpDeductDtl> slEmpDeductDtl = empDeductDtlService.mediaSeqEq(
							tEmpDeductMedia.getMediaDate() + 19110000, tEmpDeductMedia.getMediaKind(),
							tEmpDeductMedia.getMediaSeq(), 0, Integer.MAX_VALUE, titaVo);
					if (slEmpDeductDtl == null) {
						throw new LogicException("E0014", "?????????????????????" + tEmpDeductMedia.getEmpDeductMediaId()); // ????????????
					}
					for (EmpDeductDtl tEmpDeductDtl : slEmpDeductDtl.getContent()) {
						if (tEmpDeductDtl.getAcdate() > 0) {
							throw new LogicException("E0014", "????????????????????????" + tEmpDeductMedia.getEmpDeductMediaId()); // ????????????
						}
					}
//					???????????????
					tEmpDeductMedia = empDeductMediaService.holdById(tEmpDeductMedia, titaVo);
					tEmpDeductMedia.setTxAmt(reAcctAmt);
					if (reAcctAmt.compareTo(tEmpDeductMedia.getRepayAmt()) >= 0) {
						tEmpDeductMedia.setErrorCode("01");
					} else if (reAcctAmt.compareTo(BigDecimal.ZERO) > 0) {
						tEmpDeductMedia.setErrorCode("17");
					} else {
						tEmpDeductMedia.setErrorCode("16");
					}
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
//				03.????????????
				tBatxDetail.setRepayCode(3);
				if ("4".equals(mediaType)) {
					tBatxDetail.setFileName(filePath3.substring(filePath3.indexOf("10H")));
				} else if ("5".equals(mediaType)) {
					tBatxDetail.setFileName(filePath3.substring(filePath3.indexOf("LNM")));
				}
				tBatxDetail.setEntryDate(parse.stringToInteger(tempOccursList.get("OccEntryDate")));
				tBatxDetail.setCustNo(parse.stringToInteger(tempOccursList.get("OccCustNo")));
				tBatxDetail.setFacmNo(0);
				tBatxDetail.setRvNo("");
				tBatxDetail.setRepayType(reRepayCode);
				tBatxDetail.setReconCode("TEM");
				tBatxDetail.setRepayAcCode("");
				tBatxDetail.setRepayAmt(reAcctAmt); // ????????????
				// CheckMsg Message
				// 01-???????????? && 17-????????????(???02)
				if ("E".equals(procCode.substring(0, 1))) {
					procStsCode = "1"; // ?????????
				} else {
					if (reAcctAmt.compareTo(tEmpDeductMedia.getRepayAmt()) >= 0) {
						procStsCode = "0"; // ????????????
						procCode = "00000";
					} else if (reAcctAmt.compareTo(BigDecimal.ZERO) > 0) {
						procStsCode = "0"; // ????????????
						procCode = "00417";
					} else {
						procStsCode = "1"; // ?????????
						procCode = "00416";
					}
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

				if (!"00000".equals(procCode)) {
					tempVo.putParam("CheckMsg", setProcCodeX(procCode, procCodeX, titaVo));
				}
				tBatxDetail.setProcNote(tempVo.getJsonString());

				tBatxDetail.setTitaTlrNo("");
				tBatxDetail.setTitaTxtNo("");

				if (tEmpDeductMedia != null) {
					tBatxDetail.setMediaDate(tEmpDeductMedia.getMediaDate());
					tBatxDetail.setMediaKind(tEmpDeductMedia.getMediaKind());
					tBatxDetail.setMediaSeq(tEmpDeductMedia.getMediaSeq());
				}

				totAmt = totAmt.add(tBatxDetail.getRepayAmt());
				entryDate = tBatxDetail.getEntryDate();

				try {
					batxDetailService.insert(tBatxDetail, titaVo);
				} catch (DBException e) {
					e.printStackTrace();
					throw new LogicException("E0005", "BatxDetail Insert Fail");
				}

//			D.???VO???????????????Table (Detail???????????????)
			}
		} else {
//			throw new LogicException("E0014", "L4200Batch No Content " + filePath3);
		}
	}

	private void procCheque(String filePath4, ArrayList<String> dataLineList4, TitaVo titaVo) throws LogicException {
		if (dataLineList4 != null && dataLineList4.size() != 0) {
			// ?????????????????????????????????????????????
			batxChequeFileVo.setValueFromFile(dataLineList4);
			ArrayList<OccursList> uploadFile = batxChequeFileVo.getOccursList();

			sBatxDetail = batxDetailService.findL4200AEq(iAcDate, iBatchNo, 0, Integer.MAX_VALUE, titaVo);

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
				fileSeq = i - tableSize + 1;
				tempVo = new TempVo();
				tempVo.putParam("FileSeq", fileSeq);

				this.info("chequeAcct : " + tempOccursList.get("ChequeAcct"));
				this.info("chequeNo : " + tempOccursList.get("ChequeNo"));
				this.info("chequeAmt : " + tempOccursList.get("ChequeAmt"));
				this.info("chequeDate : " + tempOccursList.get("ChequeDateB"));

				int chequeAcct = parse.stringToInteger(tempOccursList.get("ChequeAcct"));
				int chequeNo = parse.stringToInteger(tempOccursList.get("ChequeNo"));
				BigDecimal chequeAmt = parse.stringToBigDecimal(tempOccursList.get("ChequeAmt"));
				int chequeDate = parse.stringToInteger(tempOccursList.get("ChequeDateB"));
				String rvno = parse.IntegerToString(chequeAcct, 9) + " " + parse.IntegerToString(chequeNo, 7);

//				1	ChequeAcct	??????????????????	X	9	0	9	
//				2	ChequeNo	????????????	    X	7	9	16	
//				3	ReturnCode	?????????	    X	1	16	17	"H.?????? C.???/??????"
//				4	ChequeDateA	????????????	    A	7	17	24	YYYMMDD
//				5	ChequeAmt	??????	        A	10	24	34	
//				6	ChequeDateB	????????????	    A	7	34	41	YYYMMDD
//				7	EntryDate	????????????	    A	7	41	48	YYYMMDD ?????????/???????????????????????????

				BatxDetail tBatxDetail = new BatxDetail();
				BatxDetailId tBatxDetailId = new BatxDetailId();

//				??????LoanCheque
				LoanChequeId tLoanChequeId = new LoanChequeId();
				LoanCheque tLoanCheque = new LoanCheque();
				tLoanChequeId.setChequeAcct(chequeAcct);
				tLoanChequeId.setChequeNo(chequeNo);
				tLoanCheque = loanChequeService.holdById(tLoanChequeId, titaVo);
				if ("0".equals(procStsCode) && tLoanCheque == null) {
					procCode = "E0014"; // ????????????
					procCodeX = "??????????????????";
					procStsCode = "1";
				}
				if (!"0".equals(tLoanCheque.getStatusCode())) {
					procCode = "E0015"; // ????????????
					procCodeX = "?????????????????????0.?????????";
					procStsCode = "1";
				}
				this.info("Cheque ReturnCode : " + tempOccursList.get("ReturnCode"));
//              ????????????
				if (!"H".equals(tempOccursList.get("ReturnCode"))) {
					procCode = "00501";
					procStsCode = "1";
				}
				if ("0".equals(procStsCode) && chequeAmt.compareTo(tLoanCheque.getChequeAmt()) != 0) {
					procCode = "E0015"; // ????????????
					procCodeX = "?????????????????????";
					procStsCode = "1";
				}

				if ("0".equals(procStsCode)) {
//					???AcRecivible.RvNo(???????????? : 9????????????+1??????+7??????  ), ???????????????(????????????), ?????????????????? ???????????????error
//					????????????????????????????????????????????????????????????

					sAcReceivable = acReceivableService.acrvRvNoEq("TCK", tLoanCheque.getCustNo(), rvno, 0,
							Integer.MAX_VALUE, titaVo);
					lAcReceivable = sAcReceivable == null ? null : sAcReceivable.getContent();

					BigDecimal acrvCheqAmt = BigDecimal.ZERO;

					if (lAcReceivable != null && lAcReceivable.size() != 0) {
						for (AcReceivable tAcReceivable : lAcReceivable) {
							acrvCheqAmt = acrvCheqAmt.add(tAcReceivable.getRvAmt());
							this.info("tAcReceivable : " + tAcReceivable.toString());
						}
					}
//					???????????????????????????
					if (lAcReceivable == null || lAcReceivable.size() == 0 || chequeAmt.compareTo(acrvCheqAmt) != 0) {
						procCode = "E0015"; // ????????????
						procCodeX = "???????????????????????????";
						procStsCode = "1";
					}
				}
				// update LoanCheque
				if ("0".equals(procStsCode)) {
					procCode = "00000";
					tLoanCheque.setStatusCode("4");
					tLoanCheque.setEntryDate(parse.stringToInteger(tempOccursList.get("EntryDate")));
					try {
						loanChequeService.update(tLoanCheque, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "LoanCheque update error : " + e.getErrorMsg());
					}
				}
// 
				if ("0".equals(procStsCode) && procCode.equals("00000")) {
					chequeSuccCnt = chequeSuccCnt + 1;
					chequeSuccAmt = chequeSuccAmt.add(chequeAmt);
				} else {
					chequeFailCnt = chequeFailCnt + 1;
				}

//				 ????????????????????????
				tempVo.putParam("Note", "???????????? : " + parse.IntegerToString(chequeAcct, 9) + " ???????????? : "
						+ parse.IntegerToString(chequeNo, 7) + " ???????????? : " + chequeAmt);

				tempVo.putParam("Remark", chequeDate + " " + chequeAmt);
				if ("0".equals(procStsCode)) {
					int listCnt = 1;
					for (AcReceivable tAcReceivable : lAcReceivable) {
						tBatxDetailId.setAcDate(iAcDate);
						tBatxDetailId.setBatchNo(iBatchNo);
						tBatxDetailId.setDetailSeq(i + cheqCnt);
						tBatxDetail.setBatxDetailId(tBatxDetailId);

//						??????SEQ
						if (lAcReceivable.size() != listCnt) {
							cheqCnt = cheqCnt + 1;
							listCnt = listCnt + 1;
						}

						this.info("i : " + i);
						this.info("cheqCnt : " + cheqCnt);
						this.info("listCnt : " + listCnt);

//						04.????????????
						tBatxDetail.setRepayCode(4);
						tBatxDetail.setFileName(filePath4.substring(filePath4.indexOf("mortgage")));
						tBatxDetail.setEntryDate(parse.stringToInteger(tempOccursList.get("EntryDate")));
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
						tBatxDetail.setTitaTlrNo("");
						tBatxDetail.setTitaTxtNo("");
						tBatxDetail.setMediaDate(0);
						tBatxDetail.setMediaKind("");
						tBatxDetail.setMediaSeq(i + cheqCnt);
// 						AML ????????????
						txAmlCom.setTxBuffer(this.getTxBuffer());
						tempVo = txAmlCom.batxCheque(tempVo, tLoanCheque, iBatchNo, titaVo);
						tBatxDetail.setProcNote(tempVo.getJsonString());
						totAmt = totAmt.add(tBatxDetail.getRepayAmt());
						entryDate = tBatxDetail.getEntryDate();

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

//					04.????????????
					tBatxDetail.setRepayCode(4);
					tBatxDetail.setFileName(filePath4.substring(filePath4.indexOf("mortgage")));
					tBatxDetail.setEntryDate(parse.stringToInteger(tempOccursList.get("EntryDate")));
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

					tBatxDetail.setTitaTlrNo("");
					tBatxDetail.setTitaTxtNo("");
					tBatxDetail.setMediaDate(0);
					tBatxDetail.setMediaKind("");
					tBatxDetail.setMediaSeq(i + cheqCnt);
					totAmt = totAmt.add(tBatxDetail.getRepayAmt());
					entryDate = tBatxDetail.getEntryDate();

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

	// ?????????????????????BS006 ??????????????????????????????????????????
	private void txToDoCheque(TitaVo titaVo) throws LogicException {
		// ??????????????????(??????????????????+???????????? ??????????????????+????????????)
		MySpring.newTask("BS006", this.txBuffer, titaVo);
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
		sBankDeductDtl = bankDeductDtlService.mediaSeqRng(mediaDate + 19110000, mediaKind, mediaSeq, 0,
				Integer.MAX_VALUE, titaVo);
		lBankDeductDtl = sBankDeductDtl == null ? null : sBankDeductDtl.getContent();

		if (lBankDeductDtl != null && lBankDeductDtl.size() != 0) {
			for (BankDeductDtl tB : lBankDeductDtl) {
				if (tB.getAcDate() > 0) {
					throw new LogicException("E0014", "?????????????????? " + tB.getBankDeductDtlId()); // // ????????????
				}
				BankDeductDtl tBankDeductDtl = bankDeductDtlService.holdById(tB, titaVo);
				tBankDeductDtl.setReturnCode(returnCode);
				// ?????????
				if (tBankDeductDtl.getRepayType() == 5) {
					TempVo fTempVo = new TempVo();
					fTempVo = fTempVo.getVo(tBankDeductDtl.getJsonFields());
					if (fTempVo.getParam("InsuDate") == null) {
						sAcReceivable = acReceivableService.useL2062Eq("F09", tBankDeductDtl.getCustNo(),
								tBankDeductDtl.getFacmNo(), tBankDeductDtl.getFacmNo(), 0, 0, 0, Integer.MAX_VALUE,
								titaVo);
						if (sAcReceivable == null) {
							sAcReceivable = acReceivableService.useL2062Eq("TMI", tBankDeductDtl.getCustNo(),
									tBankDeductDtl.getFacmNo(), tBankDeductDtl.getFacmNo(), 0, 0, 0, Integer.MAX_VALUE,
									titaVo);
						}
						lAcReceivable = sAcReceivable == null ? null : sAcReceivable.getContent();
						if (lAcReceivable != null) {
							fTempVo.putParam("InsuDate", lAcReceivable.get(0).getOpenAcDate());
							fTempVo.putParam("InsuNo", lAcReceivable.get(0).getRvNo());
							tBankDeductDtl.setJsonFields(fTempVo.getJsonString());
						}
					}
				}

				try {
					bankDeductDtlService.update(tBankDeductDtl, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", " L4200Batch bankDeductDtlService update " + e.getErrorMsg());
				}
				// ????????????
				tempVo.putParam("RepayBank", tBankDeductDtl.getRepayBank());
				// ?????????????????????
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
// --- ???????????? ---
//		1.???????????????????????? ??? ????????????
//        1).??????????????? rbalmall
//        2).ACH????????? AHR11P, AHR12P
//        3).???????????????PRSBCP4_53N, PRSBCP4_8460001, PRSBCP4_8460002
//        4).???????????????10H, LNM
//        5).???????????????mortgage
//		2.????????????  ...??? ???????????????????????????????????????????????????????????????
//		3.?????????????????????????????????????????????     
//      4.????????????(?????????????????????????????????????????????????????????)???????????????;

		this.info("fileName ..." + fileName);

		String[] filelist = fileName.split(";");

		if (filelist == null || filelist.length == 0) {
			sendMsg = sendMsg + "??????????????????";
			checkFlag = false;
		}

		int Rmtfkind = 0;
		int bankDeductKind = 0;
		int empDeductKind = 0;
		int mortgageKind = 0;
		for (String filename : filelist) {
//			????????????setValue??????????????????occursList
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
				Rmtfkind = 1;
				try {
					bankRmtfFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("AHR11P") >= 0) {
				bankDeductKind = 1;
				try {
					achDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("AHR12P") >= 0) {
				bankDeductKind = 1;
				try {
					achDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("PRSBCP4_53N") >= 0) {
				bankDeductKind = 1;
				try {
					postDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("PRSBCP4_8460001") >= 0) {
				bankDeductKind = 1;
				try {
					postDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("PRSBCP4_8460002") >= 0) {
				bankDeductKind = 1;
				try {
					postDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("10H") >= 0) {
				empDeductKind = 1;
				try {
					empDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("LNM") >= 0) {
				empDeductKind = 1;
				try {
					empDeductFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else if (filename.indexOf("mortgage") >= 0) {
				mortgageKind++;
				try {
					batxChequeFileVo.setValueFromFile(dataLineList1);
				} catch (LogicException e) {
					sendMsg = sendMsg + removeDot(filename) + ": " + e.getErrorMsg();
					checkFlag = false;
					break;
				}
			} else {
				sendMsg = sendMsg + removeDot(filename) + "??????????????????????????????";
				checkFlag = false;
				break;
			}
			if ((Rmtfkind + bankDeductKind + empDeductKind + mortgageKind) > 1) {
				sendMsg = sendMsg + "???????????????(?????????????????????????????????????????????????????????)??????????????????";
				checkFlag = false;
			}
			if (checkFlag) {
				sBatxDetail = batxDetailService.fileCheck(iAcDate, filename, 0, Integer.MAX_VALUE, titaVo);
				lBatxDetail = sBatxDetail == null ? null : sBatxDetail.getContent();
				if (lBatxDetail != null && lBatxDetail.size() != 0) {
					BatxHead tBatxHead = new BatxHead();
					BatxHeadId tBatxHeadId = new BatxHeadId();
					tBatxHeadId.setAcDate(lBatxDetail.get(0).getAcDate() + 19110000);
					tBatxHeadId.setBatchNo(lBatxDetail.get(0).getBatchNo());
					tBatxHead = batxHeadService.findById(tBatxHeadId, titaVo);
					if (tBatxHead == null) {
						sendMsg = sendMsg + removeDot(filename) + "???????????????????????????????????????";
						checkFlag = false;
						break;
					}
					if ("8".equals(tBatxHead.getBatxExeCode())) {
						checkFlag = true;
					} else {
						sendMsg = sendMsg + removeDot(filename) + "???????????????????????????????????????????????? : "
								+ lBatxDetail.get(0).getBatchNo();
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
// --- ???????????? ---
//  1.??????????????????????????? ??????= xxx , ?????? = xxxx
//  2.???????????? xxxxxxx ??????????????????  xxxxxxx
//  3.????????? : xxxxxxx  "????????? : xxxxxxx ?????????????????????
//    ?????????????????????????????????????????????
		totSuccCnt = totSuccCnt + rmftSuccCnt + achSuccCnt + postSuccCnt + empASuccCnt + empBSuccCnt + chequeSuccCnt;
		totFailCnt = totFailCnt + rmftFailCnt + achFailCnt + postFailCnt + empAFailCnt + empBFailCnt + chequeFailCnt;
//		?????????
		totCnt = totSuccCnt + totFailCnt;

		tBatxHead = batxHeadService.holdById(tBatxHeadId, titaVo);
		tBatxHead.setAcDate(tBatxHeadId.getAcDate());
		tBatxHead.setBatchNo(tBatxHeadId.getBatchNo());
		tBatxHead.setBatxTotAmt(totAmt);
		tBatxHead.setBatxTotCnt(totCnt);
		tBatxHead.setBatxExeCode("0");
		tBatxHead.setBatxStsCode("0");
		tBatxHead.setTitaTlrNo(titaVo.getTlrNo());
		tBatxHead.setTitaTxCd(titaVo.getTxcd());
		this.info("Insert BatxHead !!!");
		try {
			batxHeadService.update(tBatxHead, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "L4200Batch BatxHead update " + e.getErrorMsg());
		}
		this.info("Insert BatxHead end !!!");
		sendMsg = "??????: " + iBatchNo + "??????????????????????????????=" + totCnt + ", ????????? =" + totAmt + "???";

		// ??????????????????????????????????????????
		checkBatx(tBatxHead, titaVo);

		// ??????????????????????????????????????????????????????
		checkMedia(tBatxHead, titaVo);

		// ?????????????????? > ????????????
		if (entryDate > this.getTxBuffer().getTxCom().getTbsdy()) {
			sendMsg += "??????????????? " + entryDate + " ?????????????????? " + this.getTxBuffer().getTxCom().getTbsdy();
		}
	}

//	????????????????????????????????????????????????????????????????????????
	private void checkBatx(BatxHead tBatxHead, TitaVo titaVo) throws LogicException {
		int lbsdyf = this.getTxBuffer().getTxCom().getLbsdyf();
		int tbsdyf = this.getTxBuffer().getTxCom().getTbsdyf();
		int llbsdyf = dateUtil.getbussDate(lbsdyf, -1);
		Slice<BatxHead> slBatxHead = batxHeadService.acDateRange(llbsdyf, tbsdyf, 0, Integer.MAX_VALUE, titaVo);

		if (slBatxHead != null) {
			for (BatxHead ba : slBatxHead.getContent()) {
				if ("L4200".equals(ba.getTitaTxCd()) && ba.getBatxTotCnt() == totCnt
						&& ba.getBatxTotAmt().compareTo(totAmt) == 0 && !tBatxHead.getBatchNo().equals(ba.getBatchNo())
						&& !"8".equals(ba.getBatxExeCode())) {
					sendMsg += " ????????? : " + ba.getAcDate() + "????????? : " + ba.getBatchNo() + " ?????????????????????";
				}
			}
		}
	}

//	??????????????????????????????????????????????????????
	private void checkMedia(BatxHead tBatxHead, TitaVo titaVo) throws LogicException {
		int deductMediaCnt = 0;
		BigDecimal deductMediaAmt = BigDecimal.ZERO;
		this.info("meadiaDateAch1=" + meadiaDateAch1 + ", meadiaDateAch2=" + meadiaDateAch2 + ", meadiaDatePost="
				+ meadiaDatePost);
		if (meadiaDateAch1 + meadiaDateAch2 + meadiaDatePost == 0) {
			return;
		}
		if (meadiaDateAch1 > 0) {
			Slice<AchDeductMedia> slAchDeductMedia = achDeductMediaService.mediaDateEq(meadiaDateAch1 + 19110000, "1",
					0, Integer.MAX_VALUE, titaVo);
			if (slAchDeductMedia != null) {
				for (AchDeductMedia ach : slAchDeductMedia.getContent()) {
					deductMediaCnt++;
					deductMediaAmt = deductMediaAmt.add(ach.getRepayAmt());
				}
			}
		}
		if (meadiaDateAch2 > 0) {
			Slice<AchDeductMedia> slAchDeductMedia = achDeductMediaService.mediaDateEq(meadiaDateAch2 + 19110000, "2",
					0, Integer.MAX_VALUE, titaVo);
			if (slAchDeductMedia != null) {
				for (AchDeductMedia ach : slAchDeductMedia.getContent()) {
					deductMediaCnt++;
					deductMediaAmt = deductMediaAmt.add(ach.getRepayAmt());
				}
			}
		}

		if (meadiaDatePost > 0) {
			Slice<PostDeductMedia> slPostDeductMedia = postDeductMediaService.mediaDateEq(meadiaDatePost + 19110000, 0,
					Integer.MAX_VALUE, titaVo);
			if (slPostDeductMedia != null) {
				for (PostDeductMedia post : slPostDeductMedia.getContent()) {
					deductMediaCnt++;
					deductMediaAmt = deductMediaAmt.add(post.getRepayAmt());
				}
			}
		}
		if (deductMediaCnt != totCnt || !deductMediaAmt.equals(totAmt)) {
			sendMsg += " ??????????????????????????? ??????=" + deductMediaCnt + ", ?????? =" + deductMediaAmt;
		}
	}

}