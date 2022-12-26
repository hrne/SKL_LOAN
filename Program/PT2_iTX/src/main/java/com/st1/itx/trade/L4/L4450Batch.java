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
import com.st1.itx.util.common.LoanCom;
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
	LoanCom loanCom;
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

//	寄送筆數
	private int commitCnt = 200;

	private int iEntryDate = 0;

	private int cnt = 0;
	private int deleteCnt = 0;

//	重複註記
	private HashMap<tmpBorm, Integer> flagMap = new HashMap<>();
//	繳息迄日 minIntStartDate
	private HashMap<tmpBorm, Integer> prevIntDate = new HashMap<>();
//	計息起日 prevPayIntDateList
	private HashMap<tmpBorm, Integer> intStartDate = new HashMap<>();
//	計息止日 prevRepaidDateList
	private HashMap<tmpBorm, Integer> intEndDate = new HashMap<>();

	private HashMap<tmpBorm, String> acctCode = new HashMap<>();
	private HashMap<tmpBorm, String> repayBank = new HashMap<>();
	private HashMap<tmpBorm, String> repayAcct = new HashMap<>();
	private HashMap<tmpBorm, String> postCode = new HashMap<>();
	private HashMap<tmpBorm, String> custId = new HashMap<>();
	private HashMap<tmpBorm, String> relationCode = new HashMap<>();
	private HashMap<tmpBorm, String> relationName = new HashMap<>();
	private HashMap<tmpBorm, String> relationId = new HashMap<>();
	private HashMap<tmpBorm, Integer> relAcctBirthday = new HashMap<>();
	private HashMap<tmpBorm, String> relAcctGender = new HashMap<>();
	private HashMap<tmpBorm, String> repayAcctSeq = new HashMap<>();
	private HashMap<tmpBorm, String> status = new HashMap<>();
	private HashMap<tmpBorm, BigDecimal> limitAmt = new HashMap<>();

//	皆取最小的
	private HashMap<tmpBorm, BigDecimal> repAmtFacMap = new HashMap<>();

//	應扣金額 
	private HashMap<tmpBorm, BigDecimal> shPayAmtMap = new HashMap<>();
//	暫收抵繳金額 
	private HashMap<tmpBorm, BigDecimal> tmpAmtMap = new HashMap<>();
//	扣款金額
	private HashMap<tmpBorm, BigDecimal> repAmtMap = new HashMap<>();
//	短欠繳金額
	private HashMap<tmpBorm, BigDecimal> shortAmtMap = new HashMap<>();
//	暫收款占用金額 = 未到期約定還本金額 + 法務費金額  + 已出檔未入帳
	private HashMap<tmpBorm, BigDecimal> bookAmtMap = new HashMap<>();
//  火險單號碼
	private HashMap<tmpBorm, String> insuNoMap = new HashMap<>();
//  火險單日期
	private HashMap<tmpBorm, Integer> insuDateMap = new HashMap<>();

	private HashMap<tmpBorm, String> rpAcCodeMap = new HashMap<>();
//	預設true 若有錯誤改為 False 
	private Boolean checkFlag = true;
	private String checkMsg = " ";
//  是否有還款資料
	private boolean isLoanRepay = false;
	private List<Map<String, String>> fnAllList = new ArrayList<>();
	private List<BankDeductDtl> lBankDeductDtl = new ArrayList<BankDeductDtl>();

//	由L4R11計算
//	入帳日：預設值為會計日之下下營業日
//	應繳日：
//	　郵局 ：若下營業日為設定之特定扣款日，則顯示下營業日
//	　ACH：會計日＋１日曆日～下營業日
//	二扣應繳日：會計日前五個營業日（含本日），為基準計算上述之該日應繳日
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		if ("L4450".equals(titaVo.getTxcd())) {
			int tbsdyf = txBuffer.getMgBizDate().getTbsDyf();
			// 暫收抵繳整批入帳完畢，再產檔
			BatxHead tBatxHead = batxHeadService.titaTxCdFirst(tbsdyf, "L4450", " ");
			if (tBatxHead == null) {
				tBatxHead = bs020.exec(titaVo, this.txBuffer);
				this.info("BatchNo = " + tBatxHead.getBatchNo());
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "F", "L4002",
						titaVo.getEntDyI() + "0" + titaVo.getTlrNo(),
						"請完成暫收抵繳整批入帳(批號=" + tBatxHead.getBatchNo() + ")，再重新執行(產出銀行扣帳檔)", titaVo);
			}
			if ("4".equals(tBatxHead.getBatxExeCode()) || "8".equals(tBatxHead.getBatxExeCode())) {
				exec(titaVo);
			} else {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "F", "L4002",
						titaVo.getEntDyI() + "0" + titaVo.getTlrNo(),
						"請先完成或刪除暫收抵繳整批入帳(批號=" + tBatxHead.getBatchNo() + ")，再重新執行(產出銀行扣帳檔)", titaVo);
			}
		}
		// 單筆
		if ("L4451".equals(titaVo.getTxcd())) {
			exec(titaVo);			
		}
		return this.sendList();
	}

	private void exec(TitaVo titaVo) throws LogicException {
		this.info("active L4450Batch  Call by " + titaVo.getTxcd());
		baTxCom.setTxBuffer(this.getTxBuffer());
		loanCom.setTxBuffer(this.getTxBuffer());
//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;
		iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate"));
		int iRepayType = 1; // 期款
		// 整批
		if ("L4450".equals(titaVo.getTxcd())) {
			int iAchSpecificDdFrom = parse.stringToInteger(titaVo.getParam("AchSpecificDdFrom")) + 19110000;
			int iAchSpecificDdTo = parse.stringToInteger(titaVo.getParam("AchSpecificDdTo")) + 19110000;
			int iAchSecondSpecificDdFrom = parse.stringToInteger(titaVo.getParam("AchSecondSpecificDdFrom")) + 19110000;
			int iAchSecondSpecificDdTo = parse.stringToInteger(titaVo.getParam("AchSecondSpecificDdTo")) + 19110000;
			int iDeductDateStart = parse.stringToInteger(titaVo.getParam("DeductDateStart")) + 19110000;
			int iDeductDateEnd = parse.stringToInteger(titaVo.getParam("DeductDateEnd")) + 19110000;
			int iOpItem = parse.stringToInteger(titaVo.getParam("OpItem"));

//		若特定輸入日為0者代表不與營業日相同，即不需輸入扣帳檔
//		判斷二扣=是否有會計日
			int today1 = 0;
			int today2 = 0;

//		特殊日扣款
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

//		清除扣帳日(EntryDate) 之資料
			deleBankDeductDtl(iOpItem, titaVo);
			if (titaVo.isHcodeErase()) {
				if (checkFlag) {
					checkMsg = "刪除筆數" + deleteCnt;
				}
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4450",
						titaVo.getTlrNo(), checkMsg, titaVo);

			}
			if (titaVo.isHcodeNormal()) {
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
						checkMsg = "查無吻合之撥款檔資料";
						checkFlag = false;
					} else {
						int i = 0;
						for (int j = 1; j <= fnAllList.size(); j++) {
							i = j - 1;

							setFacmValue(fnAllList, i);

							// 還款試算
							doBatxCom(iEntryDate, parse.stringToInteger(fnAllList.get(i).get("CustNo")),
									parse.stringToInteger(fnAllList.get(i).get("FacmNo")), 1, titaVo); // 期款

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
						checkMsg = "E0001 查無資料";
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
					checkMsg = "銀行扣款合計報表已完成，總筆數=" + cnt;
				}

				if (checkFlag) {
					webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
							titaVo.getTlrNo(), checkMsg, titaVo);
				} else {
					webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4450",
							titaVo.getTlrNo(), checkMsg, titaVo);
				}
			}
		}
		// 單筆
		if ("L4451".equals(titaVo.getTxcd())) {
			iRepayType = parse.stringToInteger(titaVo.getParam("RepayType")); // 還款類別
			try {
				fnAllList = l4450ServiceImpl.findSingle(titaVo);
			} catch (Exception e) {
				checkMsg = e.getMessage();
				checkFlag = false;
			}
			if (checkFlag) {
				this.info("fnAllList.size() = " + fnAllList.size());
				if (fnAllList.size() == 0) {
					checkMsg = "E0001 查無吻合之撥款檔資料";
					checkFlag = false;
				} else {

					int i = 0;
					for (int j = 1; j <= fnAllList.size(); j++) {
						i = j - 1;

						setFacmValue(fnAllList, i);

						// 還款試算
						doBatxCom(iEntryDate, parse.stringToInteger(fnAllList.get(i).get("CustNo")),
								parse.stringToInteger(fnAllList.get(i).get("FacmNo")), iRepayType, titaVo); // 還款類別

					}
				}
			}
			if (checkFlag) {
				// 是否有還款資料
				if (iRepayType <= 3 && !isLoanRepay) {
					checkFlag = false;
					checkMsg = "E0001 查無還本利資料";
					if (iRepayType == 2) {
						checkMsg += ", 請先登錄約定部分償還";
					}
				}
			}

			if (checkFlag) {
				setBankDeductDtl(shPayAmtMap, titaVo);
				if (cnt == 0) {
					checkMsg = "E0001 查無該還款類別資料";
					checkFlag = false;
				} else {
					checkMsg = "銀行扣款檔新增完成，筆數=" + cnt;
				}
			}

			this.batchTransaction.commit();

			if (checkFlag) {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4943",
						"01" + titaVo.getParam("CustNo") +"00000" +titaVo.getParam("EntryDate")
								+ titaVo.getParam("EntryDate"),
						checkMsg, titaVo);
			} else {
				checkMsg = "執行失敗，" + checkMsg;
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", "", checkMsg, titaVo);
			}
		}

	}

//	用既有之List for each 找出RepayCode 1.期款  4.帳管 5.火險 6.契變手續費 
	private void doBatxCom(int entryDate, int custNo, int facmNo, int iRepayType, TitaVo titaVo) throws LogicException {

		tmpBorm tmp2 = new tmpBorm(custNo, facmNo, 0, 0, 0);

//		同戶號額度僅進入計算一次
		if (flagMap.containsKey(tmp2)) {
			this.info("custNo = " + custNo + " facmNo = " + facmNo + " 同戶號額度僅進入計算一次 return...");
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
		// 取得暫收指定額度：000-全部非指定額度，或 > 0 => 單一額度
		int wkTmpFacmNo = loanCom.getTmpFacmNo(custNo, 0, facmNo, titaVo);
		tmpBorm tmpAmtFacmNo = new tmpBorm(custNo, wkTmpFacmNo, 0, 0, 0);

		// 取得暫收款餘額，暫收指定額度不同時寫入
		if (!tmpAmtMap.containsKey(tmpAmtFacmNo)) {
			tmpAmtMap.put(tmpAmtFacmNo, baTxCom.getExcessive());
		}

//		期款占用未到期約定還本金額
		if (iRepayType == 1) {
			bookAmtMap.put(tmp2, this.getLoanBookAmt(tmp2, titaVo));
		} else {
			bookAmtMap.put(tmp2, BigDecimal.ZERO);
		}

		if (listBaTxVo != null && listBaTxVo.size() != 0) {
//			batxvo sort by CustNo, FacmNo, 法務費, 暫收款, RepayType(大至小)PayIntDate(小至大) 
			for (BaTxVo tBaTxVo : listBaTxVo) {
				if (tBaTxVo.getRepayType() == 7) {
					tBaTxVo.setDataKind(-2);
				}
				if (tBaTxVo.getDataKind() == 3) {
					tBaTxVo.setDataKind(-1);
				}
				if (tBaTxVo.getDataKind() == 1 && tBaTxVo.getRepayType() == 1) {
					// 放期款第一筆
					for (BaTxVo t : listBaTxVo) {
						if (t.getDataKind() == 2) {
							tBaTxVo.setDataKind(2);
							tBaTxVo.setPayIntDate(t.getPayIntDate());
							tBaTxVo.setIntStartDate(t.getIntStartDate());
							tBaTxVo.setIntEndDate(t.getIntStartDate());
							this.info("1=" + tBaTxVo.toString());
							break;
						}
					}
					// 當期期款已繳但有短繳
					if (tBaTxVo.getDataKind() == 1) {
						if (baTxCom.getPrevPayIntDate() <= entryDate) {
							tBaTxVo.setDataKind(2);
							tBaTxVo.setPayIntDate(baTxCom.getPrevPayIntDate());
							tBaTxVo.setIntStartDate(0);
							tBaTxVo.setIntEndDate(baTxCom.getPrevPayIntDate());
							this.info("2=" + tBaTxVo.toString());
							break;
						}
					}
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
				this.info("Vo=" + tBaTxVo.toString());
				tmpBorm tmp = new tmpBorm(tBaTxVo.getCustNo(), tBaTxVo.getFacmNo(), 0, tBaTxVo.getRepayType(),
						tBaTxVo.getDataKind() == 2 ? tBaTxVo.getPayIntDate() : 0);
// 

//				因應此交易為提前做，故入帳日會大於應繳日，排除下一期之試算
//				ex.3/16應繳日產出dtl檔，入帳日=3/17，list會產出3/16 & 4/16兩期
				if (tBaTxVo.getRepayType() == 1 && tBaTxVo.getPayIntDate() > iEntryDate) {
					this.info("continue... getPayIntDate : " + tBaTxVo.getPayIntDate());
					continue;
				}

				// 本金利息 = 0
				if (tBaTxVo.getDataKind() == 2 && tBaTxVo.getPrincipal().add(tBaTxVo.getInterest())
						.add(tBaTxVo.getDelayInt()).add(tBaTxVo.getBreachAmt()).compareTo(BigDecimal.ZERO) == 0) {
					this.info("continue...  本金利息 = 0");
//					continue;
				}
				// 是否有還款資料
				if (tBaTxVo.getRepayType() >= 1 && tBaTxVo.getRepayType() <= 3) {
					isLoanRepay = true;
				}

				if (tBaTxVo.getDataKind() >= 4) {
					this.info("continue... getDataKind : " + tBaTxVo.getDataKind());
					continue;
				}

//				短繳後方合計第一筆欠款
//				if (tBaTxVo.getDataKind() == 1 && tBaTxVo.getRepayType() == 1) {
//					this.info("continue... 短繳期金");
//					if (shortAmtMap.containsKey(tmp2)) {
//						shortAmtMap.put(tmp2, tBaTxVo.getUnPaidAmt().add(shortAmtMap.get(tmp2)));
//					} else {
//						shortAmtMap.put(tmp2, tBaTxVo.getUnPaidAmt());
//					}
//					continue;
//				}

//          	暫收款占用金額 = 未到期約定還本金額 + 法務費金額 
				if (iRepayType == 1) {
					if (tBaTxVo.getRepayType() == 7) {
						if (bookAmtMap.containsKey(tmp2)) {
							bookAmtMap.put(tmp2, tBaTxVo.getUnPaidAmt().add(bookAmtMap.get(tmp2)));
						} else {
							bookAmtMap.put(tmp2, tBaTxVo.getUnPaidAmt());
						}
					}
				}
//				1.期款、2.部分償還、3.結案 ==> 含 1. 應收 (1.短繳期金 4.帳管費 5.火險費 6.契變手續費)  2.本金、利息 
//              4.帳管費 5.火險費 6.契變手續費 7.法務費 ==> 還款類別				
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

				// 火險單
				if (tBaTxVo.getRepayType() == 5) {
					// 火險單號碼
					if (!insuNoMap.containsKey(tmp2)) {
						insuNoMap.put(tmp2, tBaTxVo.getRvNo());
					} else {
						insuNoMap.put(tmp2, insuNoMap.get(tmp2) + "," + tBaTxVo.getRvNo());
					}
					// 火險單日期(最小)
					if (!insuDateMap.containsKey(tmp2)) {
						insuDateMap.put(tmp2, tBaTxVo.getPayIntDate());
					}
				}
				// 暫收款
				if (tBaTxVo.getDataKind() == 3) {
					if (bookAmtMap.get(tmp2).compareTo(BigDecimal.ZERO) > 0) {
						this.info("bookAmAmtMap : " + bookAmtMap.get(tmp2));
						if (tmpAmtMap.get(tmpAmtFacmNo).compareTo(bookAmtMap.get(tmp2)) > 0) {
							tmpAmtMap.put(tmpAmtFacmNo, tmpAmtMap.get(tmpAmtFacmNo).subtract(bookAmtMap.get(tmp2)));
							bookAmtMap.put(tmp2, BigDecimal.ZERO);
						} else {
							bookAmtMap.put(tmp2, bookAmtMap.get(tmp2).subtract(tmpAmtMap.get(tmpAmtFacmNo)));
							tmpAmtMap.put(tmpAmtFacmNo, BigDecimal.ZERO);
						}
					}
					this.info("tmpAmtMap : " + tmpAmtMap.get(tmpAmtFacmNo));
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
//				 // 1.已送出媒體未回或未製成媒體 2.期款二扣
					int sendCode = isMediaSent(tmp, iRepayType, prevIntDate.get(tmp), titaVo);
					if (sendCode == 1) {
						if (sendCode == 1 && "L4451".equals(titaVo.getTxcd())) {
							checkMsg = "已送出媒體未回或未製成媒體";
							checkFlag = false;
						} else {
							this.info("continue 已送出未提回 : " + tmp.toString());
							continue;
						}
					}
				}
//				應扣金額 shPayAmtMap - 暫收抵繳金額  tmpAmtMap = 扣款金額 repAmtMap 

				BigDecimal unPaidAmt = tBaTxVo.getUnPaidAmt();

//				短繳合計於第一筆期款
//				if (tBaTxVo.getDataKind() == 2) {
//					if (shortAmtMap.get(tmp2) != null && shortAmtMap.get(tmp2).compareTo(BigDecimal.ZERO) > 0) {
//						unPaidAmt = unPaidAmt.add(shortAmtMap.get(tmp2));
//						shortAmtMap.put(tmp2, BigDecimal.ZERO);
//					}
//				}

				if (shPayAmtMap.containsKey(tmp)) {
					shPayAmtMap.put(tmp, unPaidAmt.add(shPayAmtMap.get(tmp)));
				} else {
					shPayAmtMap.put(tmp, unPaidAmt);
				}
//              除火險費外可暫收抵繳
//				應扣金額 shPayAmtMap - 暫收抵繳金額  tmpAmtMap = 扣款金額 repAmtMap 

//				若應扣金額<=暫收款
//				扣款金額=0
//				暫收款(該扣款代碼)=應扣金額
//				暫收款(目前額度下)=暫收款(目前額度下)-應扣金額
				if (tBaTxVo.getRepayType() == 5) {
					if (repAmtMap.containsKey(tmp)) {
						repAmtMap.put(tmp, repAmtMap.get(tmp).add(unPaidAmt));
					} else {
						repAmtMap.put(tmp, unPaidAmt);
					}
//				合計至額度，限額計算用
					if (repAmtFacMap.containsKey(tmp2)) {
						repAmtFacMap.put(tmp2, repAmtFacMap.get(tmp2).add(unPaidAmt));
					} else {
						repAmtFacMap.put(tmp2, unPaidAmt);
					}
				} else {
					if (unPaidAmt.compareTo(tmpAmtMap.get(tmpAmtFacmNo)) <= 0) {
						if (repAmtMap.containsKey(tmp)) {
						} else {
							repAmtMap.put(tmp, BigDecimal.ZERO);
						}
						tmpAmtMap.put(tmpAmtFacmNo, tmpAmtMap.get(tmpAmtFacmNo).subtract(unPaidAmt));
					} else {
//				若應扣金額>暫收款
//				扣款金額=應扣金額-暫收款
//				暫收款(該扣款代碼) = 暫收款(目前額度下)
//				暫收款(目前額度下)
						BigDecimal repayAmt = unPaidAmt.subtract(tmpAmtMap.get(tmpAmtFacmNo));
						if (repAmtMap.containsKey(tmp)) {
							repAmtMap.put(tmp, repAmtMap.get(tmp).add(repayAmt));
						} else {
							repAmtMap.put(tmp, repayAmt);
						}
						tmpAmtMap.put(tmpAmtFacmNo, BigDecimal.ZERO);
//					合計至額度，限額計算用
						if (repAmtFacMap.containsKey(tmp2)) {
							repAmtFacMap.put(tmp2, repAmtFacMap.get(tmp2).add(repayAmt));
						} else {
							repAmtFacMap.put(tmp2, repayAmt);
						}
					}
				}
//				費用才抓取BatxVo.AcctCode
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
				this.info("tmpAmtMap :: " + tmpAmtMap.get(tmpAmtFacmNo));
			}
		}

	}

//	寫入BankDeductDtl
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
			this.info("tBankDeductDtlId = " + tBankDeductDtlId.toString());

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

//			 暫收抵繳金額 = 應扣金額 shPayAmtMap -  扣款金額 repAmtMap 
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
//			tmp.getPayIntDate() 需放入tempVo給AML檢核用

			String failFlag = status.get(tmp2);

			this.info("amlRsp ... " + amlRsp);
			this.info("failFlag ... " + failFlag);
			this.info("repAmtMap.get(tmp) ... " + repAmtMap.get(tmp));
			this.info("limitAmt ... " + limitAmt.get(tmp2));

			TempVo tTempVo = new TempVo();
//			帳號授權檢核未過
			if (!"0".equals(failFlag)) {
				tTempVo.putParam("Auth", failFlag);
			} else {

			}
//			Deduct 兩個只會發生一個
//			繳款金額=0(抵繳)
			if (repAmtMap.get(tmp).compareTo(BigDecimal.ZERO) == 0) {
				tTempVo.putParam("Deduct", "扣款金額為零");
			}
//			扣款金額(by 額度)超過帳號設定限額(限額為零不檢查)
			this.info("repAmtFacMap ..." + repAmtFacMap.get(tmp2));
			if (limitAmt.get(tmp2).compareTo(BigDecimal.ZERO) > 0
					&& repAmtFacMap.get(tmp2).compareTo(limitAmt.get(tmp2)) > 0) {
				tTempVo.putParam("Deduct", "超過帳戶限額");
			}
			// 火險單
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

		if (slBankDeductDtl != null) {
			for (BankDeductDtl tBankDeductDtl : slBankDeductDtl.getContent()) {
				if (tBankDeductDtl.getAcDate() > 0) {
					checkMsg = "資料已入帳";
					checkFlag = false;
					break;
				}

//				NA:未產 Y:已產 N:已產
				if (!"".equals(tBankDeductDtl.getMediaCode())) {
					checkMsg = "已產生媒體，須執行'L4452-重製媒體碼'";
					checkFlag = false;
					break;
				}
				deleteCnt++;
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

	// 1.已送出媒體未回或未製成媒體
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

//	設定變數值
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
		if (!custId.containsKey(tmp2))
			custId.put(tmp2, fnAllList.get(i).get("CustId"));
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

	// 未到期約定還本金額
	private BigDecimal getLoanBookAmt(tmpBorm tmp, TitaVo titaVo) {
		// 未到期約定還本金額
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

//	暫時紀錄戶號額度
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