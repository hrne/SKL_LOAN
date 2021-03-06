package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.BankDeductDtlId;
import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.service.AchDeductMediaService;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.BatxHeadService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.db.service.PostDeductMediaService;
import com.st1.itx.db.service.springjpa.cm.L4450ServiceImpl;
import com.st1.itx.trade.BS.BS020;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.CheckAmlVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4450Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4450Batch extends TradeBuffer {

	@Autowired
	public Parse parse;
	@Autowired
	DateUtil dateUtil;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public BaTxCom baTxCom;
	@Autowired
	public PostAuthLogService postAuthLogService;
	@Autowired
	public TxAmlCom txAmlCom;
	@Autowired
	public BankDeductDtlService bankDeductDtlService;
	@Autowired
	public AchDeductMediaService achDeductMediaService;
	@Autowired
	public PostDeductMediaService postDeductMediaService;
	@Autowired
	public BankAuthActService bankAuthActService;
	@Autowired
	public LoanBookService loanBookService;
	@Autowired
	public BatxHeadService batxHeadService;
	@Autowired
	public L4450Report l4450Report;
	@Autowired
	public WebClient webClient;
	@Autowired
	public AuthLogCom authLogCom;
	@Autowired
	public L4450ServiceImpl l4450ServiceImpl;
	@Autowired
	public BS020 bs020;

//	????????????
	private int commitCnt = 200;

	private int iEntryDate = 0;

	private int cnt = 0;

//	????????????
	private HashMap<tmpBorm, Integer> flagMap = new HashMap<>();
//	???????????? minIntStartDate
	private HashMap<tmpBorm, Integer> prevIntDate = new HashMap<>();
//	???????????? prevPayIntDateList
	private HashMap<tmpBorm, Integer> intStartDate = new HashMap<>();
//	???????????? prevRepaidDateList
	private HashMap<tmpBorm, Integer> intEndDate = new HashMap<>();

	private HashMap<tmpBorm, String> acctCode = new HashMap<>();
	private HashMap<tmpBorm, String> repayBank = new HashMap<>();
	private HashMap<tmpBorm, String> repayAcct = new HashMap<>();
	private HashMap<tmpBorm, String> postCode = new HashMap<>();
	private HashMap<tmpBorm, String> relationCode = new HashMap<>();
	private HashMap<tmpBorm, String> relationName = new HashMap<>();
	private HashMap<tmpBorm, String> relationId = new HashMap<>();
	private HashMap<tmpBorm, Integer> relAcctBirthday = new HashMap<>();
	private HashMap<tmpBorm, String> relAcctGender = new HashMap<>();
	private HashMap<tmpBorm, String> repayAcctSeq = new HashMap<>();
	private HashMap<tmpBorm, String> status = new HashMap<>();
	private HashMap<tmpBorm, BigDecimal> limitAmt = new HashMap<>();

//	???????????????
	private HashMap<tmpBorm, BigDecimal> repAmtFacMap = new HashMap<>();

//	???????????? 
	private HashMap<tmpBorm, BigDecimal> shPayAmtMap = new HashMap<>();
//	?????????????????? 
	private HashMap<tmpBorm, BigDecimal> tmpAmtMap = new HashMap<>();
//	????????????
	private HashMap<tmpBorm, BigDecimal> repAmtMap = new HashMap<>();
//	???????????????
	private HashMap<tmpBorm, BigDecimal> shortAmtMap = new HashMap<>();
//	????????????????????? = ??????????????????????????? + ??????????????? 
	private HashMap<tmpBorm, BigDecimal> bookAmtMap = new HashMap<>();
//  ???????????????
	private HashMap<tmpBorm, String> insuNoMap = new HashMap<>();
//  ???????????????
	private HashMap<tmpBorm, Integer> insuDateMap = new HashMap<>();

	private HashMap<tmpBorm, String> rpAcCodeMap = new HashMap<>();
//	??????true ?????????????????? False 
	private Boolean checkFlag = true;
	private String checkMsg = " ";
//  ?????????????????????
	private boolean isLoanRepay = false;

	private List<Map<String, String>> fnAllList = new ArrayList<>();
	private List<BankDeductDtl> lBankDeductDtl = new ArrayList<BankDeductDtl>();

//	???L4R11??????
//	???????????????????????????????????????????????????
//	????????????
//	????????? ?????????????????????????????????????????????????????????????????????
//	???ACH??????????????????????????????????????????
//	??????????????????????????????????????????????????????????????????????????????????????????????????????
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		int tbsdyf = txBuffer.getMgBizDate().getTbsDyf();
		// ??????????????????????????????????????????
		BatxHead tBatxHead = batxHeadService.titaTxCdFirst(tbsdyf, "L4450", " ");
		if (tBatxHead == null) {
			tBatxHead = bs020.exec(titaVo, this.txBuffer);
			this.info("BatchNo = " + tBatxHead.getBatchNo());
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "F", "L4002", titaVo.getTlrNo(),
					"?????????????????????????????????(??????=" + tBatxHead.getBatchNo() + ")??????????????????(?????????????????????)", titaVo);
		}
		if ("4".equals(tBatxHead.getBatxExeCode()) || "8".equals(tBatxHead.getBatxExeCode())) {
			exec(titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "F", "L4002", titaVo.getTlrNo(),
					"?????????????????????????????????????????????(??????=" + tBatxHead.getBatchNo() + ")??????????????????(?????????????????????)", titaVo);
		}
		return this.sendList();
	}

	private void exec(TitaVo titaVo) throws LogicException {
		this.info("active L4450Batch  Call by " + titaVo.getTxcd());
		baTxCom.setTxBuffer(this.getTxBuffer());
//		 ?????????????????? titaVo.getReturnIndex() ???????????????0?????????????????????????????????
		this.index = titaVo.getReturnIndex();
//		????????????????????????????????? ??????500??? ????????????????????????
		this.limit = Integer.MAX_VALUE;
		iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate"));
		int iRepayType = 1; // ??????
		// ??????
		if ("L4450".equals(titaVo.getTxcd())) {
			int iAchSpecificDdFrom = parse.stringToInteger(titaVo.getParam("AchSpecificDdFrom")) + 19110000;
			int iAchSpecificDdTo = parse.stringToInteger(titaVo.getParam("AchSpecificDdTo")) + 19110000;
			int iAchSecondSpecificDdFrom = parse.stringToInteger(titaVo.getParam("AchSecondSpecificDdFrom")) + 19110000;
			int iAchSecondSpecificDdTo = parse.stringToInteger(titaVo.getParam("AchSecondSpecificDdTo")) + 19110000;
			int iDeductDateStart = parse.stringToInteger(titaVo.getParam("DeductDateStart")) + 19110000;
			int iDeductDateEnd = parse.stringToInteger(titaVo.getParam("DeductDateEnd")) + 19110000;
			int iOpItem = parse.stringToInteger(titaVo.getParam("OpItem"));

//		?????????????????????0?????????????????????????????????????????????????????????
//		????????????=??????????????????
			int today1 = 0;
			int today2 = 0;

//		???????????????
			today1 = parse.stringToInteger(titaVo.getParam("PostSpecificDd")) + 19110000;
			today2 = parse.stringToInteger(titaVo.getParam("PostSecondSpecificDd")) + 19110000;

			this.info("iAchSpecificDdFrom : " + iAchSpecificDdFrom);
			this.info("iAchSpecificDdTo : " + iAchSpecificDdTo);
			this.info("iAchSecondSpecificDdFrom : " + iAchSecondSpecificDdFrom);
			this.info("iAchSecondSpecificDdTo : " + iAchSecondSpecificDdTo);
			this.info("today1 : " + today1);
			this.info("today2 : " + today2);
			this.info("iDeductDateStart : " + iDeductDateStart);
			this.info("iDeductDateEnd : " + iDeductDateEnd);

			String ddp1 = ("" + today1).substring(6);
			String ddp2 = ("" + today2).substring(6);
			List<String> dda1 = findAchDD(iAchSpecificDdFrom, iAchSpecificDdTo);
			List<String> dda2 = findAchDD(iAchSecondSpecificDdFrom, iAchSecondSpecificDdTo);

			this.info("ddp1 : " + ddp1);
			this.info("ddp2 : " + ddp2);
			this.info("dda1 : " + dda1);
			this.info("dda2 : " + dda2);

//		???????????????(EntryDate) ?????????
			deleBankDeductDtl(iOpItem, titaVo);
			if (checkFlag) {
				try {
					fnAllList = l4450ServiceImpl.findAll(titaVo);
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.error("l4450ServiceImpl.findAll error = " + errors.toString());
					checkMsg = e.getMessage();
					checkFlag = false;
				}
			}

			if (checkFlag) {
				this.info("fnAllList.size() = " + fnAllList.size());
				if (fnAllList.size() == 0) {
					checkMsg = "??????????????????????????????";
					checkFlag = false;
				} else {
					int i = 0;
					for (int j = 1; j <= fnAllList.size(); j++) {
						i = j - 1;

						setFacmValue(fnAllList, i);

						// ????????????
						doBatxCom(iEntryDate, parse.stringToInteger(fnAllList.get(i).get("CustNo")),
								parse.stringToInteger(fnAllList.get(i).get("FacmNo")), 1, titaVo); // ??????

						if (i % commitCnt == 0) {
							this.batchTransaction.commit();
						}
					}
				}
				this.batchTransaction.commit();
			}
			if (checkFlag) {
				setBankDeductDtl(shPayAmtMap, titaVo);
				if (cnt == 0) {
					checkMsg = "E0001 ????????????";
					checkFlag = false;
				}
			}
			this.batchTransaction.commit();
			if (checkFlag) {
				try {
					l4450Report.doReport(lBankDeductDtl, titaVo);
				} catch (LogicException e) {
					checkMsg = e.getMessage();
					checkFlag = false;
				}
			}

			if (checkFlag) {
				checkMsg = "?????????????????????????????????????????????=" + cnt;
			}

			if (checkFlag) {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
						titaVo.getTlrNo()+"L4450", checkMsg, titaVo);
			} else {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4450",
						titaVo.getTlrNo(), checkMsg, titaVo);
			}
		}
		// ??????
		if ("L4451".equals(titaVo.getTxcd())) {
			iRepayType = parse.stringToInteger(titaVo.getParam("RepayType")); // ????????????
			try {
				fnAllList = l4450ServiceImpl.findSingle(titaVo);
			} catch (Exception e) {
				checkMsg = e.getMessage();
				checkFlag = false;
			}
			if (checkFlag) {
				this.info("fnAllList.size() = " + fnAllList.size());
				if (fnAllList.size() == 0) {
					checkMsg = "E0001 ??????????????????????????????";
					checkFlag = false;
				} else {

					int i = 0;
					for (int j = 1; j <= fnAllList.size(); j++) {
						i = j - 1;

						setFacmValue(fnAllList, i);

						// ????????????
						doBatxCom(iEntryDate, parse.stringToInteger(fnAllList.get(i).get("CustNo")),
								parse.stringToInteger(fnAllList.get(i).get("FacmNo")), iRepayType, titaVo); // ????????????

					}
				}
			}
			if (checkFlag) {
				// ?????????????????????
				if (iRepayType <= 3 && !isLoanRepay) {
					checkFlag = false;
					checkMsg = "E0001 ?????????????????????";
					if (iRepayType == 2) {
						checkMsg += ", ??????????????????????????????";
					}
				}
			}

			if (checkFlag) {
				setBankDeductDtl(shPayAmtMap, titaVo);
				if (cnt == 0) {
					checkMsg = "E0001 ???????????????????????????";
					checkFlag = false;
				} else {
					checkMsg = "????????????????????????????????????=" + cnt;
				}
			}

			this.batchTransaction.commit();

			if (checkFlag) {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4943",
						"1" + titaVo.getParam("CustNo") + "00000" + titaVo.getParam("EntryDate")
								+ titaVo.getParam("EntryDate"),
						checkMsg, titaVo);
			} else {
				checkMsg = "???????????????" + checkMsg;
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", "", checkMsg, titaVo);
			}
		}

	}

//	????????????List for each ??????RepayCode 1.??????  4.?????? 5.?????? 6.??????????????? 
	private void doBatxCom(int entryDate, int custNo, int facmNo, int iRepayType, TitaVo titaVo) throws LogicException {

		tmpBorm tmp2 = new tmpBorm(custNo, facmNo, 0, 0, 0);

//		????????????????????????????????????
		if (flagMap.containsKey(tmp2)) {
			this.info("custNo = " + custNo + " facmNo = " + facmNo + " ???????????????????????????????????? return...");
			return;
		} else {
			flagMap.put(tmp2, 1);
		}

		List<BaTxVo> listBaTxVo = new ArrayList<BaTxVo>();

		try {
			listBaTxVo = baTxCom.settingUnPaid(entryDate, custNo, facmNo, 0, iRepayType, BigDecimal.ZERO, titaVo);
		} catch (Exception e) {
			this.info("Error : " + e.getMessage());
		}

//		???????????????=0
		tmpAmtMap.put(tmp2, BigDecimal.ZERO);

//		???????????????????????????????????????
		if (iRepayType == 1) {
			bookAmtMap.put(tmp2, this.getLoanBookAmt(tmp2, titaVo));
		} else {
			bookAmtMap.put(tmp2, BigDecimal.ZERO);
		}

		if (listBaTxVo != null && listBaTxVo.size() != 0) {
//			batxvo sort by CustNo, FacmNo, ?????????, ?????????, RepayType(?????????)PayIntDate(?????????) 
			for (BaTxVo tBaTxVo : listBaTxVo) {
				if (tBaTxVo.getRepayType() == 7) {
					tBaTxVo.setDataKind(-2);
				}
				if (tBaTxVo.getDataKind() == 3) {
					tBaTxVo.setDataKind(-1);
				}
			}
			listBaTxVo.sort((c1, c2) -> {
				int result = 0;
				if (c1.getCustNo() - c2.getCustNo() != 0) {
					result = c1.getCustNo() - c2.getCustNo();
				} else if (c1.getFacmNo() - c2.getFacmNo() != 0) {
					result = c1.getFacmNo() - c2.getFacmNo();
				} else if (c1.getDataKind() - c2.getDataKind() != 0) {
					result = c1.getDataKind() - c2.getDataKind();
				} else if (c1.getRepayType() - c2.getRepayType() != 0) {
					result = c1.getRepayType() - c2.getRepayType();
				} else if (c1.getPayIntDate() - c2.getPayIntDate() != 0) {
					result = c1.getPayIntDate() - c2.getPayIntDate();
				} else {
					result = 0;
				}
				return result;
			});
			for (BaTxVo tBaTxVo : listBaTxVo) {
				if (tBaTxVo.getDataKind() == -2) {
					tBaTxVo.setDataKind(1);
				} else {
					if (tBaTxVo.getDataKind() == -1) {
						tBaTxVo.setDataKind(3);
					}
				}
				tmpBorm tmp = new tmpBorm(tBaTxVo.getCustNo(), tBaTxVo.getFacmNo(), 0, tBaTxVo.getRepayType(),
						tBaTxVo.getDataKind() == 2 ? tBaTxVo.getPayIntDate() : 0);
// 

//				???????????????????????????????????????????????????????????????????????????????????????
//				ex.3/16???????????????dtl???????????????=3/17???list?????????3/16 & 4/16??????
				if (tBaTxVo.getRepayType() == 1 && tBaTxVo.getPayIntDate() > iEntryDate) {
					this.info("continue... getPayIntDate : " + tBaTxVo.getPayIntDate());
					continue;
				}

				// ???????????? = 0
				if (tBaTxVo.getDataKind() == 2 && tBaTxVo.getPrincipal().add(tBaTxVo.getInterest())
						.add(tBaTxVo.getDelayInt()).add(tBaTxVo.getBreachAmt()).compareTo(BigDecimal.ZERO) == 0) {
					this.info("continue...  ???????????? = 0");
					continue;
				}
				// ?????????????????????
				if (tBaTxVo.getRepayType() >= 1 && tBaTxVo.getRepayType() <= 3) {
					isLoanRepay = true;
				}

				if (tBaTxVo.getDataKind() > 3) {
					this.info("continue... getDataKind : " + tBaTxVo.getDataKind());
					continue;
				}

//				?????????????????????????????????
				if (tBaTxVo.getDataKind() == 1 && tBaTxVo.getRepayType() == 1) {
					this.info("continue... ????????????");
					if (shortAmtMap.containsKey(tmp2)) {
						shortAmtMap.put(tmp2, tBaTxVo.getUnPaidAmt().add(shortAmtMap.get(tmp2)));
					} else {
						shortAmtMap.put(tmp2, tBaTxVo.getUnPaidAmt());
					}
					continue;
				}

//          	????????????????????? = ??????????????????????????? + ??????????????? 
				if (iRepayType == 1) {
					if (tBaTxVo.getRepayType() == 7) {
						if (bookAmtMap.containsKey(tmp2)) {
							bookAmtMap.put(tmp2, tBaTxVo.getUnPaidAmt().add(bookAmtMap.get(tmp2)));
						} else {
							bookAmtMap.put(tmp2, tBaTxVo.getUnPaidAmt());
						}
					}
				}
//				1.?????????2.???????????????3.?????? ==> ??? 1. ?????? (1.???????????? 4.????????? 5.????????? 6.???????????????)  2.??????????????? 
//              4.????????? 5.????????? 6.??????????????? 7.????????? ==> ????????????				
				if (tBaTxVo.getDataKind() == 1) {
					if (iRepayType <= 3) {
						if (tBaTxVo.getRepayType() == 1 || tBaTxVo.getRepayType() == 4 || tBaTxVo.getRepayType() == 5
								|| tBaTxVo.getRepayType() == 6) {
						} else {
							this.info("continue... DataKind=" + tBaTxVo.getDataKind() + ", RepayType="
									+ tBaTxVo.getRepayType());
							continue;
						}
					} else {
						if (tBaTxVo.getRepayType() == iRepayType) {
						} else {
							this.info("continue... DataKind=" + tBaTxVo.getDataKind() + ", RepayType="
									+ tBaTxVo.getRepayType());
							continue;
						}
					}
				}

				// ?????????
				if (tBaTxVo.getRepayType() == 5) {
					// ???????????????
					if (!insuNoMap.containsKey(tmp2)) {
						insuNoMap.put(tmp2, tBaTxVo.getRvNo());
					} else {
						insuNoMap.put(tmp2, insuNoMap.get(tmp2) + "," + tBaTxVo.getRvNo());
					}
					// ???????????????(??????)
					if (!insuDateMap.containsKey(tmp2)) {
						insuDateMap.put(tmp2, tBaTxVo.getPayIntDate());
					}
				}
				// ?????????
				if (tBaTxVo.getDataKind() == 3) {
					this.info("bookAmAmtMap : " + bookAmtMap.get(tmp2));
					if (tBaTxVo.getUnPaidAmt().compareTo(bookAmtMap.get(tmp2)) > 0) {
						if (tmpAmtMap.containsKey(tmp2)) {
							tmpAmtMap.put(tmp2,
									tmpAmtMap.get(tmp2).add(tBaTxVo.getUnPaidAmt().subtract(bookAmtMap.get(tmp2))));
						} else {
							tmpAmtMap.put(tmp2, tBaTxVo.getUnPaidAmt().subtract(bookAmtMap.get(tmp2)));
						}
						bookAmtMap.put(tmp2, BigDecimal.ZERO);
					} else {
						if (tmpAmtMap.containsKey(tmp2)) {
							bookAmtMap.put(tmp2, bookAmtMap.get(tmp2).subtract(tBaTxVo.getUnPaidAmt()));
						} else {
							bookAmtMap.put(tmp2, bookAmtMap.get(tmp2).subtract(tBaTxVo.getUnPaidAmt()));
						}
					}
					this.info("tmpAmtMap : " + tmpAmtMap.get(tmp2));
					continue;
				}

				if (tBaTxVo.getDataKind() == 2) {
					if (prevIntDate.containsKey(tmp)) {
						if (tBaTxVo.getIntEndDate() > prevIntDate.get(tmp)) {
							prevIntDate.put(tmp, tBaTxVo.getIntEndDate());
						}
					} else {
						prevIntDate.put(tmp, tBaTxVo.getIntEndDate());
					}
//				 // 1.??????????????????????????????????????? 2.????????????
					int sendCode = isMediaSent(tmp, iRepayType, prevIntDate.get(tmp), titaVo);
					if (sendCode == 1) {
						if (sendCode == 1 && "L4451".equals(titaVo.getTxcd())) {
							checkMsg = "???????????????????????????????????????";
							checkFlag = false;
						} else {
							this.info("continue ?????????????????? : " + tmp.toString());
							continue;
						}
					}
				}
//				???????????? shPayAmtMap - ??????????????????  tmpAmtMap = ???????????? repAmtMap 

				BigDecimal unPaidAmt = tBaTxVo.getUnPaidAmt();

//				??????????????????????????????
				if (tBaTxVo.getDataKind() == 2) {
					if (shortAmtMap.get(tmp2) != null && shortAmtMap.get(tmp2).compareTo(BigDecimal.ZERO) > 0) {
						unPaidAmt = unPaidAmt.add(shortAmtMap.get(tmp2));
						shortAmtMap.put(tmp2, BigDecimal.ZERO);
					}
				}

				if (shPayAmtMap.containsKey(tmp)) {
					shPayAmtMap.put(tmp, unPaidAmt.add(shPayAmtMap.get(tmp)));
				} else {
					shPayAmtMap.put(tmp, unPaidAmt);
				}
//              ??????????????????????????????
//				???????????? shPayAmtMap - ??????????????????  tmpAmtMap = ???????????? repAmtMap 

//				???????????????<=?????????
//				????????????=0
//				?????????(???????????????)=????????????
//				?????????(???????????????)=?????????(???????????????)-????????????
				if (tBaTxVo.getRepayType() == 5) {
					if (repAmtMap.containsKey(tmp)) {
						repAmtMap.put(tmp, repAmtMap.get(tmp).add(unPaidAmt));
					} else {
						repAmtMap.put(tmp, unPaidAmt);
					}
//				?????????????????????????????????
					if (repAmtFacMap.containsKey(tmp2)) {
						repAmtFacMap.put(tmp2, repAmtFacMap.get(tmp2).add(unPaidAmt));
					} else {
						repAmtFacMap.put(tmp2, unPaidAmt);
					}
				} else {
					if (unPaidAmt.compareTo(tmpAmtMap.get(tmp2)) <= 0) {
						if (repAmtMap.containsKey(tmp)) {
						} else {
							repAmtMap.put(tmp, BigDecimal.ZERO);
						}
						tmpAmtMap.put(tmp2, tmpAmtMap.get(tmp2).subtract(unPaidAmt));
					} else {
//				???????????????>?????????
//				????????????=????????????-?????????
//				?????????(???????????????) = ?????????(???????????????)
//				?????????(???????????????)
						BigDecimal repayAmt = unPaidAmt.subtract(tmpAmtMap.get(tmp2));
						if (repAmtMap.containsKey(tmp)) {
							repAmtMap.put(tmp, repAmtMap.get(tmp).add(repayAmt));
						} else {
							repAmtMap.put(tmp, repayAmt);
						}
						tmpAmtMap.put(tmp2, BigDecimal.ZERO);
//					?????????????????????????????????
						if (repAmtFacMap.containsKey(tmp2)) {
							repAmtFacMap.put(tmp2, repAmtFacMap.get(tmp2).add(repayAmt));
						} else {
							repAmtFacMap.put(tmp2, repayAmt);
						}
					}
				}
//				???????????????BatxVo.AcctCode
				if (tBaTxVo.getRepayType() >= 4) {
					rpAcCodeMap.put(tmp, tBaTxVo.getAcctCode());
				} else {
					rpAcCodeMap.put(tmp, "");
				}

				if (intStartDate.containsKey(tmp)) {
					if (tBaTxVo.getIntStartDate() < intStartDate.get(tmp)) {
						intStartDate.put(tmp, tBaTxVo.getIntStartDate());
					}
				} else {
					intStartDate.put(tmp, tBaTxVo.getIntStartDate());
				}
				if (intEndDate.containsKey(tmp)) {
					if (tBaTxVo.getIntEndDate() > intEndDate.get(tmp)) {
						intEndDate.put(tmp, tBaTxVo.getIntEndDate());
					}
				} else {
					intEndDate.put(tmp, tBaTxVo.getIntEndDate());
				}

				this.info("RepayType : " + tBaTxVo.getRepayType());
				this.info("shPayAmtMap : " + shPayAmtMap.get(tmp));
				this.info("repAmtMap : " + repAmtMap.get(tmp));
				this.info("tmpAmt : " + shPayAmtMap.get(tmp).subtract(repAmtMap.get(tmp)));
				this.info("repAmtFacMap : " + repAmtFacMap.get(tmp2));
				this.info("tmpAmtMap :: " + tmpAmtMap.get(tmp2));
			}
		}

	}

//	??????BankDeductDtl
	private void setBankDeductDtl(HashMap<tmpBorm, BigDecimal> shPayAmtMap, TitaVo titaVo) throws LogicException {
		this.info("setBankDeductDtl...");

		Set<tmpBorm> tempSet = shPayAmtMap.keySet();

		List<tmpBorm> tempList = new ArrayList<>();

		for (Iterator<tmpBorm> it = tempSet.iterator(); it.hasNext();) {
			tmpBorm tmpFacmVo = it.next();
			tempList.add(tmpFacmVo);
			this.info("tmpFacmVo = " + tmpFacmVo);
		}

		this.info("tempList.size() = " + tempList.size());

		for (tmpBorm tmp : tempList) {
			cnt++;

			tmpBorm tmp2 = new tmpBorm(tmp.getCustNo(), tmp.getFacmNo(), 0, 0, 0);

			BankDeductDtl tBankDeductDtl = new BankDeductDtl();
			BankDeductDtlId tBankDeductDtlId = new BankDeductDtlId();

			tBankDeductDtlId.setEntryDate(iEntryDate);
			tBankDeductDtlId.setCustNo(tmp.getCustNo());
			tBankDeductDtlId.setFacmNo(tmp.getFacmNo());
			tBankDeductDtlId.setPayIntDate(tmp.getPayIntDate());
			tBankDeductDtlId.setRepayType(tmp.getRepayType());

			tBankDeductDtl.setBankDeductDtlId(tBankDeductDtlId);
			tBankDeductDtl.setEntryDate(iEntryDate);
			tBankDeductDtl.setCustNo(tmp.getCustNo());
			tBankDeductDtl.setFacmNo(tmp.getFacmNo());
			tBankDeductDtl.setPayIntDate(tmp.getPayIntDate());
			tBankDeductDtl.setRepayType(tmp.getRepayType());

			String sAcctCode = "";
			if ("".equals(rpAcCodeMap.get(tmp).trim())) {
				sAcctCode = acctCode.get(tmp2);
			} else {
				sAcctCode = rpAcCodeMap.get(tmp);
			}

			tBankDeductDtl.setAcctCode(sAcctCode);
			if (prevIntDate.get(tmp) != null) {
				tBankDeductDtl.setPrevIntDate(prevIntDate.get(tmp));
			} else {
				tBankDeductDtl.setPrevIntDate(0);
			}
			this.info("1RepayType : " + tmp.getRepayType());
			this.info("1shPayAmtMap : " + shPayAmtMap.get(tmp));
			this.info("1repAmtMap : " + repAmtMap.get(tmp));
			this.info("1tmpAmt : " + shPayAmtMap.get(tmp).subtract(repAmtMap.get(tmp)));
			this.info("1repAmtFacMap : " + repAmtFacMap.get(tmp2));

//			 ?????????????????? = ???????????? shPayAmtMap -  ???????????? repAmtMap 
			tBankDeductDtl.setUnpaidAmt(shPayAmtMap.get(tmp));
			tBankDeductDtl.setTempAmt(shPayAmtMap.get(tmp).subtract(repAmtMap.get(tmp)));
			tBankDeductDtl.setRepayAmt(repAmtMap.get(tmp));

			tBankDeductDtl.setRepayBank(repayBank.get(tmp2));
			tBankDeductDtl.setRepayAcctNo(FormatUtil.pad9(repayAcct.get(tmp2), 14));
			tBankDeductDtl.setRepayAcctSeq(repayAcctSeq.get(tmp2));
			tBankDeductDtl.setIntStartDate(intStartDate.get(tmp));
			tBankDeductDtl.setIntEndDate(intEndDate.get(tmp));
			tBankDeductDtl.setPostCode(postCode.get(tmp2));
			tBankDeductDtl.setRelationCode(relationCode.get(tmp2));
			tBankDeductDtl.setRelCustName(relationName.get(tmp2));
			tBankDeductDtl.setRelCustId(relationId.get(tmp2));
			tBankDeductDtl.setRelAcctBirthday(relAcctBirthday.get(tmp2));
			tBankDeductDtl.setRelAcctGender(relAcctGender.get(tmp2));
			tBankDeductDtl.setAcDate(0);

			txAmlCom.setTxBuffer(this.getTxBuffer());
			CheckAmlVo tCheckAmlVo = txAmlCom.deduct(tBankDeductDtl, titaVo);

			String amlRsp = tCheckAmlVo.getConfirmStatus();
			tBankDeductDtl.setAmlRsp(amlRsp);
//			tmp.getPayIntDate() ?????????tempVo???AML?????????

			String failFlag = status.get(tmp2);

			this.info("amlRsp ... " + amlRsp);
			this.info("failFlag ... " + failFlag);
			this.info("repAmtMap.get(tmp) ... " + repAmtMap.get(tmp));
			this.info("limitAmt ... " + limitAmt.get(tmp2));

			TempVo tTempVo = new TempVo();
//			????????????????????????
			if (!"0".equals(failFlag)) {
				tTempVo.putParam("Auth", failFlag);
			}
//			Deduct ????????????????????????
//			????????????=0(??????)
			if (repAmtMap.get(tmp).compareTo(BigDecimal.ZERO) == 0) {
				tTempVo.putParam("Deduct", "??????????????????");
			}
//			????????????(by ??????)????????????????????????(?????????????????????)
			this.info("repAmtFacMap ..." + repAmtFacMap.get(tmp2));
			if (limitAmt.get(tmp2).compareTo(BigDecimal.ZERO) > 0
					&& repAmtFacMap.get(tmp2).compareTo(limitAmt.get(tmp2)) > 0) {
				tTempVo.putParam("Deduct", "??????????????????");
			}
			// ?????????
			if (tmp.getRepayType() == 5) {
				this.info("insuNoMap ..." + insuNoMap.get(tmp2));
				if (insuNoMap.get(tmp2) != null) {
					tTempVo.putParam("InsuNo", insuNoMap.get(tmp2));
				}
				if (insuDateMap.get(tmp2) != null) {
					tTempVo.putParam("InsuDate", insuDateMap.get(tmp2));
				}
			}
			tBankDeductDtl.setJsonFields(tTempVo.getJsonString());

			lBankDeductDtl.add(tBankDeductDtl);
		}
		if (cnt > 0) {
			try {
				bankDeductDtlService.insertAll(lBankDeductDtl, titaVo);
			} catch (DBException e) {
				checkMsg = e.getMessage();
				checkFlag = false;
				return;
			}
		}

	}

	private int findNextDay(int today) throws LogicException {
		dateUtil.init();
		dateUtil.setDate_1(today);
		dateUtil.setDays(1);
		today = dateUtil.getCalenderDay();
		return today;
	}

	private void deleBankDeductDtl(int iOpItem, TitaVo titaVo) throws LogicException {
		Slice<BankDeductDtl> slBankDeductDtl = null;
		switch (iOpItem) {
		case 1:
			slBankDeductDtl = bankDeductDtlService.repayBankNotEq("700", iEntryDate + 19110000, iEntryDate + 19110000,
					this.index, this.limit, titaVo);
			break;
		case 2:
			slBankDeductDtl = bankDeductDtlService.repayBankEq("700", iEntryDate + 19110000, iEntryDate + 19110000,
					this.index, this.limit, titaVo);
			break;
		default:
			slBankDeductDtl = bankDeductDtlService.entryDateRng(iEntryDate + 19110000, iEntryDate + 19110000,
					this.index, this.limit, titaVo);
			break;
		}

		// lBankDeductDtl = sBankDeductDtl == null ? null : sBankDeductDtl.getContent();

		if (slBankDeductDtl != null) {
			for (BankDeductDtl tBankDeductDtl : slBankDeductDtl.getContent()) {
				if (tBankDeductDtl.getAcDate() > 0) {
					checkMsg = "???????????????";
					checkFlag = false;
					break;
				}

//				NA:?????? Y:?????? N:??????
				if (!"".equals(tBankDeductDtl.getMediaCode())) {
					checkMsg = "???????????????????????????'L4452-???????????????'";
					checkFlag = false;
					break;
				}
				bankDeductDtlService.holdById(tBankDeductDtl, titaVo);
				try {
					bankDeductDtlService.delete(tBankDeductDtl, titaVo);
				} catch (DBException e) {
					checkMsg = e.getErrorMsg();
					checkFlag = false;
					break;
				}
			}
		}
	}

	// 1.???????????????????????????????????????
	private int isMediaSent(tmpBorm tmp, int iRepayType, int prevIntDate, TitaVo titaVo) {
		int result = 0;
		BankDeductDtl tBankDeductDtl = null;
		if (tmp.getRepayType() <= 3) {
			tBankDeductDtl = bankDeductDtlService.findL4450PrevIntDateFirst(tmp.getCustNo(), tmp.getFacmNo(),
					prevIntDate + 19110000, titaVo);
		} else {
			tBankDeductDtl = bankDeductDtlService.findL4450EntryDateFirst(tmp.getCustNo(), tmp.getFacmNo(),
					tmp.getRepayType(), titaVo);
		}

		if (tBankDeductDtl != null) {
			if ((iRepayType <= 3 && tBankDeductDtl.getRepayType() <= 3)
					|| iRepayType == tBankDeductDtl.getRepayType()) {
				if ("".equals(tBankDeductDtl.getMediaCode().trim())
						|| "".equals(tBankDeductDtl.getReturnCode().trim())) {
					result = 1;
				}
			}
		}

		return result;
	}

	private List<String> findAchDD(int day1, int day2) throws LogicException {
		List<String> ldd = new ArrayList<String>();
		int cnt = 0;

		ldd.add(("" + day1).substring(6));

		while (true) {
			if (day1 == day2) {
				break;
			}
			cnt++;
			if (cnt == 30) {
				break;
			}
			day1 = findNextDay(day1);
			ldd.add(("" + day1).substring(6));
		}
		return ldd;
	}

//	???????????????
	private void setFacmValue(List<Map<String, String>> fnAllList, int i) throws LogicException {
		int custNo = parse.stringToInteger(fnAllList.get(i).get("CustNo"));
		int facmNo = parse.stringToInteger(fnAllList.get(i).get("FacmNo"));

		tmpBorm tmp2 = new tmpBorm(custNo, facmNo, 0, 0, 0);

		if (!acctCode.containsKey(tmp2))
			acctCode.put(tmp2, fnAllList.get(i).get("AcctCode"));
		if (!repayBank.containsKey(tmp2))
			repayBank.put(tmp2, fnAllList.get(i).get("RepayBank"));
		if (!repayAcct.containsKey(tmp2))
			repayAcct.put(tmp2, fnAllList.get(i).get("RepayAcct"));
		if (!postCode.containsKey(tmp2))
			postCode.put(tmp2, fnAllList.get(i).get("PostDepCode"));
		if (!relationCode.containsKey(tmp2))
			relationCode.put(tmp2, fnAllList.get(i).get("RelationCode"));
		if (!relationName.containsKey(tmp2))
			relationName.put(tmp2, fnAllList.get(i).get("RelAcctName"));
		if (!relationId.containsKey(tmp2))
			relationId.put(tmp2, fnAllList.get(i).get("RelationId"));
		if (!relAcctBirthday.containsKey(tmp2))
			relAcctBirthday.put(tmp2, parse.stringToInteger(fnAllList.get(i).get("RelAcctBirthday")));
		if (!relAcctGender.containsKey(tmp2))
			relAcctGender.put(tmp2, fnAllList.get(i).get("RelAcctGender"));
		if (!repayAcctSeq.containsKey(tmp2))
			repayAcctSeq.put(tmp2, fnAllList.get(i).get("AcctSeq"));
		if (!status.containsKey(tmp2))
			status.put(tmp2, fnAllList.get(i).get("Status"));
		if (!limitAmt.containsKey(tmp2))
			limitAmt.put(tmp2, parse.stringToBigDecimal(fnAllList.get(i).get("LimitAmt")));
	}

	// ???????????????????????????
	private BigDecimal getLoanBookAmt(tmpBorm tmp, TitaVo titaVo) {
		// ???????????????????????????
		BigDecimal bookAmt = BigDecimal.ZERO;
		Slice<LoanBook> loanBookList = loanBookService.bookCustNoRange(tmp.getCustNo(), tmp.getCustNo(),
				tmp.getFacmNo(), tmp.getFacmNo(), 0, 0, 990, this.index, Integer.MAX_VALUE, titaVo);
		if (loanBookList != null) {
			for (LoanBook tLoanBook : loanBookList.getContent()) {
				if (tLoanBook.getStatus() == 0 && tLoanBook.getBookDate() >= titaVo.getEntDyI()) {
					bookAmt = bookAmt.add(tLoanBook.getBookAmt());
				}
			}
		}

		return bookAmt;
	}

//	????????????????????????
	private class tmpBorm {
		private int custNo = 0;
		private int facmNo = 0;
		private int bormNo = 0;
		private int payIntDate = 0;
		private int repayType = 0;

		public tmpBorm(int custNo, int facmNo, int bormNo, int repayType, int payIntDate) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
			this.setBormNo(bormNo);
			this.setPayIntDate(payIntDate);
			this.setRepayType(repayType);
		}

		@Override
		public String toString() {
			return "tmpBorm [custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", payIntDate="
					+ payIntDate + ", repayType=" + repayType + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + bormNo;
			result = prime * result + custNo;
			result = prime * result + facmNo;
			result = prime * result + payIntDate;
			result = prime * result + repayType;
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
			tmpBorm other = (tmpBorm) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (bormNo != other.bormNo)
				return false;
			if (custNo != other.custNo)
				return false;
			if (facmNo != other.facmNo)
				return false;
			if (payIntDate != other.payIntDate)
				return false;
			if (repayType != other.repayType)
				return false;
			return true;
		}

		private int getCustNo() {
			return custNo;
		}

		private void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		private int getFacmNo() {
			return facmNo;
		}

		private void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		private void setBormNo(int bormNo) {
			this.bormNo = bormNo;
		}

		private int getPayIntDate() {
			return payIntDate;
		}

		private void setPayIntDate(int payIntDate) {
			this.payIntDate = payIntDate;
		}

		private int getRepayType() {
			return repayType;
		}

		private void setRepayType(int repayType) {
			this.repayType = repayType;
		}

		private L4450Batch getEnclosingInstance() {
			return L4450Batch.this;
		}
	}
}