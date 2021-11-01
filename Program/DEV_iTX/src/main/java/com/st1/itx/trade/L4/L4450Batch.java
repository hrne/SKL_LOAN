package com.st1.itx.trade.L4;

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

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.BankAuthActId;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.BankDeductDtlId;
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.service.AchDeductMediaService;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.db.service.PostDeductMediaService;
import com.st1.itx.db.service.springjpa.cm.L4450ServiceImpl;
import com.st1.itx.trade.L4.L4450Report;
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
	public L4450Report l4450Report;
	@Autowired
	public WebClient webClient;
	@Autowired
	public AuthLogCom authLogCom;
	@Autowired
	public L4450ServiceImpl l4450ServiceImpl;

//	寄送筆數
	private int commitCnt = 200;

	private int iEntryDate = 0;

	private int cnt = 0;

//	重複註記
	private HashMap<tmpBorm, Integer> flagMap = new HashMap<>();
//	繳息迄日 minIntStartDate
	private HashMap<tmpBorm, Integer> minIntStartDate = new HashMap<>();
//	計息起日 prevPayIntDateList
	private HashMap<tmpBorm, Integer> intStartDate = new HashMap<>();
//	計息止日 prevRepaidDateList
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
//	暫收款占用金額 = 未到期約定還本金額 + 法務費金額 
	private HashMap<tmpBorm, BigDecimal> bookAmtMap = new HashMap<>();

	private HashMap<tmpBorm, String> rpAcCodeMap = new HashMap<>();
//	預設true 若有錯誤改為 False 
	private Boolean checkFlag = true;
	private String checkMsg = " ";
//  是否有還款資料
	private boolean isLoanRepay = false;

	private List<Map<String, String>> fnAllList = new ArrayList<>();
	private List<BankDeductDtl> lBankDeductDtl = new ArrayList<BankDeductDtl>();

	private BigDecimal limitAmt = BigDecimal.ZERO;

//	由L4R11計算
//	入帳日：預設值為會計日之下下營業日
//	應繳日：
//	　郵局 ：若下營業日為設定之特定扣款日，則顯示下營業日
//	　ACH：會計日＋１日曆日～下營業日
//	二扣應繳日：會計日前五個營業日（含本日），為基準計算上述之該日應繳日
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4450Batch  Call by " + titaVo.getTxcd());
		baTxCom.setTxBuffer(this.getTxBuffer());
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
			int iDeductDate = parse.stringToInteger(titaVo.getParam("DeductDate")) + 19110000;
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
			this.info("iDeductDate : " + iDeductDate);

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
			if (checkFlag) {
				try {
					fnAllList = l4450ServiceImpl.findAll(titaVo);
				} catch (Exception e) {
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
						doBatxCom(iEntryDate, parse.stringToInteger(fnAllList.get(i).get("F0")),
								parse.stringToInteger(fnAllList.get(i).get("F1")), 1, titaVo); // 期款

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
						doBatxCom(iEntryDate, parse.stringToInteger(fnAllList.get(i).get("F0")),
								parse.stringToInteger(fnAllList.get(i).get("F1")), iRepayType, titaVo); // 還款類別

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
						"1" + titaVo.getParam("CustNo") + "00000" + titaVo.getParam("EntryDate")
								+ titaVo.getParam("EntryDate"),
						checkMsg, titaVo);
			} else {
				checkMsg = "執行失敗，" + checkMsg;
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "", "", "", checkMsg, titaVo);
			}
		}

		return null;

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

//		預設暫收款=0
		tmpAmtMap.put(tmp2, BigDecimal.ZERO);

//		期款占用未到期約定還本金額
		if (iRepayType == 1) {
			bookAmtMap.put(tmp2, this.getLoanBookAmt(tmp2, titaVo));
		}

		if (listBaTxVo != null && listBaTxVo.size() != 0) {
//			batxvo sort by CustNo, FacmNo, BormNo, 法務費, 暫收款, PayIntDate , RepayType
			listBaTxVo.sort((c1, c2) -> {
				int result = 0;
				if (c1.getCustNo() - c2.getCustNo() != 0) {
					result = c1.getCustNo() - c2.getCustNo();
				} else if (c1.getFacmNo() - c2.getFacmNo() != 0) {
					result = c1.getFacmNo() - c2.getFacmNo();
				} else if (c1.getBormNo() - c2.getBormNo() != 0) {
					result = c1.getBormNo() - c2.getBormNo();
				} else if (c1.getRepayType() == 6 && c1.getRepayType() != c2.getRepayType()) {
					result = -1;
				} else if (c2.getRepayType() == 6 && c1.getRepayType() != c2.getRepayType()) {
					result = 1;
				} else if (c1.getDataKind() - c2.getDataKind() != 0) {
					if (c1.getDataKind() == 3) {
						result = -1;
					} else if (c2.getDataKind() == 3) {
						result = 1;
					} else {
						result = c1.getDataKind() - c2.getDataKind();
					}
				} else if (c1.getPayIntDate() - c2.getPayIntDate() != 0) {
					result = c1.getPayIntDate() - c2.getPayIntDate();
				} else if (c1.getRepayType() - c2.getRepayType() != 0) {
					result = c1.getRepayType() - c2.getRepayType();
				} else {
					result = 0;
				}
				return result;
			});
			for (BaTxVo tBaTxVo : listBaTxVo) {

				tmpBorm tmp = new tmpBorm(tBaTxVo.getCustNo(), tBaTxVo.getFacmNo(), tBaTxVo.getBormNo(),
						tBaTxVo.getRepayType(), tBaTxVo.getPayIntDate());

				tmpBorm tmp3 = new tmpBorm(tBaTxVo.getCustNo(), tBaTxVo.getFacmNo(), tBaTxVo.getBormNo(), 0, 0);

				this.info("L4450 Test's Log Start");
				this.info("CustNo : " + tmp.getCustNo());
				this.info("FacmNo : " + tmp.getFacmNo());
				this.info("BormNo : " + tmp.getBormNo());
				this.info("DataKind : " + tBaTxVo.getDataKind());
				this.info("RepayType : " + tBaTxVo.getRepayType());
				this.info("PayIntDate : " + tBaTxVo.getPayIntDate());
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
					continue;
				}
				// 是否有還款資料
				if (tBaTxVo.getDataKind() == 2) {
					isLoanRepay = true;
				}

				if (tBaTxVo.getDataKind() > 3) {
					this.info("continue... getDataKind : " + tBaTxVo.getDataKind());
					continue;
				}

//				短繳後方合計第一筆欠款
				if (tBaTxVo.getDataKind() == 1 && tBaTxVo.getRepayType() == 1) {
					this.info("continue... 短繳期金");
					shortAmtMap.put(tmp3, tBaTxVo.getUnPaidAmt());
					continue;
				}

//          	暫收款占用金額 = 未到期約定還本金額 + 法務費金額 
				if (iRepayType == 1) {
					if (tBaTxVo.getRepayType() == 6) {
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

				// 暫收款
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

				if (minIntStartDate.containsKey(tmp)) {
					if (tBaTxVo.getIntStartDate() < minIntStartDate.get(tmp)) {
						minIntStartDate.put(tmp, tBaTxVo.getIntStartDate());
					}
				} else {
					minIntStartDate.put(tmp, tBaTxVo.getIntStartDate());
				}

//				 已送出媒體未回或未製成媒體 1.與輸入還款來源相同 2.與輸入還款來源不相同
				int sendCode = isMediaSent(tmp, iRepayType, minIntStartDate.get(tmp), titaVo);
				if (sendCode > 0) {
					if (sendCode == 1 && "L4451".equals(titaVo.getTxcd())) {
						checkMsg = "已送出媒體未回或未製成媒體";
						checkFlag = false;
					} else {
						this.info("continue 已送出未提回 : " + tmp.toString());
						continue;
					}
				}

//				應扣金額 shPayAmtMap - 暫收抵繳金額  tmpAmtMap = 扣款金額 repAmtMap 

				if (shPayAmtMap.containsKey(tmp)) {
					shPayAmtMap.put(tmp, tBaTxVo.getUnPaidAmt().add(shPayAmtMap.get(tmp)));
				} else {
					shPayAmtMap.put(tmp, tBaTxVo.getUnPaidAmt());
				}

//				短繳合計於第一筆欠款
				if (shortAmtMap.get(tmp3) != null && shortAmtMap.get(tmp3).compareTo(BigDecimal.ZERO) > 0) {
					shPayAmtMap.put(tmp, shPayAmtMap.get(tmp).add(shortAmtMap.get(tmp3)));
					shortAmtMap.put(tmp3, BigDecimal.ZERO);
				}

//              期款可暫收抵繳
//				應扣金額 shPayAmtMap - 暫收抵繳金額  tmpAmtMap = 扣款金額 repAmtMap 

//				若應扣金額<=暫收款
//				扣款金額=0
//				暫收款(該扣款代碼)=應扣金額
//				暫收款(目前額度下)=暫收款(目前額度下)-應扣金額
				if (tBaTxVo.getRepayType() <= 3) {
					if (shPayAmtMap.get(tmp).compareTo(tmpAmtMap.get(tmp2)) <= 0) {
						repAmtMap.put(tmp, BigDecimal.ZERO);
						tmpAmtMap.put(tmp, shPayAmtMap.get(tmp));
						tmpAmtMap.put(tmp2, tmpAmtMap.get(tmp2).subtract(shPayAmtMap.get(tmp)));
					} else {
//				若應扣金額>暫收款
//				扣款金額=應扣金額-暫收款
//				暫收款(該扣款代碼) = 暫收款(目前額度下)
//				暫收款(目前額度下)
						BigDecimal repayAmt = shPayAmtMap.get(tmp).subtract(tmpAmtMap.get(tmp2));
						repAmtMap.put(tmp, repayAmt);
//					合計至額度，限額計算用
						if (!repAmtFacMap.containsKey(tmp2)) {
							repAmtFacMap.put(tmp2, repayAmt);
						} else {
							repAmtFacMap.put(tmp2, repAmtFacMap.get(tmp2).add(repayAmt));
						}
						tmpAmtMap.put(tmp, tmpAmtMap.get(tmp2));
						tmpAmtMap.put(tmp2, BigDecimal.ZERO);
					}
				} else {
					repAmtMap.put(tmp, shPayAmtMap.get(tmp));
					tmpAmtMap.put(tmp, BigDecimal.ZERO);
//				合計至額度，限額計算用
					if (!repAmtFacMap.containsKey(tmp2)) {
						repAmtFacMap.put(tmp2, shPayAmtMap.get(tmp));
					} else {
						repAmtFacMap.put(tmp2, repAmtFacMap.get(tmp2).add(shPayAmtMap.get(tmp)));
					}
				}
//				費用才抓取BatxVo.AcctCode
				if (tBaTxVo.getRepayType() >= 4) {
					rpAcCodeMap.put(tmp, tBaTxVo.getAcctCode());
				} else {
					rpAcCodeMap.put(tmp, "");
				}
				intStartDate.put(tmp, tBaTxVo.getIntStartDate());
				intEndDate.put(tmp, tBaTxVo.getIntEndDate());

				this.info("shPayAmtMap : " + shPayAmtMap.get(tmp));
				this.info("tmpAmtMap by facm : " + tmpAmtMap.get(tmp2));
				this.info("tmpAmtMap by borm : " + tmpAmtMap.get(tmp));
				this.info("repAmtMap : " + repAmtMap.get(tmp));
				this.info("repAmtFacMap : " + repAmtFacMap.get(tmp2));
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
			tBankDeductDtlId.setBormNo(tmp.getBormNo());
			tBankDeductDtlId.setPayIntDate(tmp.getPayIntDate());
			tBankDeductDtlId.setRepayType(tmp.getRepayType());

			tBankDeductDtl.setBankDeductDtlId(tBankDeductDtlId);
			tBankDeductDtl.setEntryDate(iEntryDate);
			tBankDeductDtl.setCustNo(tmp.getCustNo());
			tBankDeductDtl.setFacmNo(tmp.getFacmNo());
			tBankDeductDtl.setBormNo(tmp.getBormNo());
			tBankDeductDtl.setPayIntDate(tmp.getPayIntDate());
			tBankDeductDtl.setRepayType(tmp.getRepayType());

			String sAcctCode = "";
			if ("".equals(rpAcCodeMap.get(tmp).trim())) {
				sAcctCode = acctCode.get(tmp2);
			} else {
				sAcctCode = rpAcCodeMap.get(tmp);
			}

			tBankDeductDtl.setAcctCode(sAcctCode);
			tBankDeductDtl.setPrevIntDate(minIntStartDate.get(tmp));

//			應扣金額 shPayAmtMap - 暫收抵繳金額  tmpAmtMap = 扣款金額 repAmtMap 
			tBankDeductDtl.setUnpaidAmt(shPayAmtMap.get(tmp));
			tBankDeductDtl.setTempAmt(tmpAmtMap.get(tmp));
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

			String failFlag = checkAcctAuth(tmp2, titaVo);

			this.info("amlRsp ... " + amlRsp);
			this.info("failFlag ... " + failFlag);
			this.info("repAmtMap.get(tmp) ... " + repAmtMap.get(tmp));
			this.info("limitAmt ... " + limitAmt);

			TempVo tTempVo = new TempVo();
//			Aml檢核未過
			if (!"0".equals(amlRsp)) {
				tTempVo.putParam("Aml", amlRsp);
			}
//			帳號授權檢核未過
			if (!"0".equals(failFlag)) {
				tTempVo.putParam("Auth", failFlag);
			}
//			Deduct 兩個只會發生一個
//			繳款金額=0(抵繳)
			if (repAmtMap.get(tmp).compareTo(BigDecimal.ZERO) == 0) {
				tTempVo.putParam("Deduct", "扣款金額為零");
			}
//			扣款金額(by 額度)超過帳號設定限額(限額為零不檢查)
			this.info("repAmtFacMap ..." + repAmtFacMap.get(tmp2));
			if (limitAmt.compareTo(BigDecimal.ZERO) > 0 && repAmtFacMap.get(tmp2).compareTo(limitAmt) > 0) {
				tTempVo.putParam("Deduct", "超過帳戶限額");
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
					checkMsg = "資料已入帳";
					checkFlag = false;
					break;
				}

//				NA:未產 Y:已產 N:已產
				if (!"".equals(tBankDeductDtl.getMediaCode())) {
					checkMsg = "已產生媒體";
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

	// 已送出媒體未回或未製成媒體 1.與輸入還款來源相同 2.與輸入還款來源不相同
	private int isMediaSent(tmpBorm tmp, int iRepayType, int prevIntDate, TitaVo titaVo) {
		int result = 0;

		Slice<BankDeductDtl> slBankDeductDtl = null;
		if (tmp.getRepayType() <= 3) {
			slBankDeductDtl = bankDeductDtlService.findL4450PrevIntDate(tmp.getCustNo(), tmp.getFacmNo(),
					tmp.getBormNo(), prevIntDate + 19110000, this.index, this.limit, titaVo);

		} else {
			slBankDeductDtl = bankDeductDtlService.findL4450Rng(tmp.getCustNo(), tmp.getFacmNo(), tmp.getBormNo(),
					tmp.getRepayType(), tmp.getPayIntDate() + 19110000, this.index, this.limit, titaVo);
		}
		if (slBankDeductDtl != null) {
			for (BankDeductDtl tBankDeductDtl : slBankDeductDtl.getContent()) {
				if ("".equals(tBankDeductDtl.getMediaCode().trim())
						|| "".equals(tBankDeductDtl.getReturnCode().trim())) {
					if ((iRepayType <= 3 && tBankDeductDtl.getRepayType() <= 3)
							|| iRepayType == tBankDeductDtl.getRepayType()) {
						result = 1;
					} else if (tmp.getRepayType() == tBankDeductDtl.getRepayType()) {
						result = 2;
					}
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
		int custNo = parse.stringToInteger(fnAllList.get(i).get("F0"));
		int facmNo = parse.stringToInteger(fnAllList.get(i).get("F1"));

		tmpBorm tmp2 = new tmpBorm(custNo, facmNo, 0, 0, 0);

		if (!acctCode.containsKey(tmp2))
			acctCode.put(tmp2, fnAllList.get(i).get("F3"));
		if (!repayBank.containsKey(tmp2))
			repayBank.put(tmp2, fnAllList.get(i).get("F4"));
		if (!repayAcct.containsKey(tmp2))
			repayAcct.put(tmp2, fnAllList.get(i).get("F5"));
		if (!postCode.containsKey(tmp2))
			postCode.put(tmp2, fnAllList.get(i).get("F6"));
		if (!relationCode.containsKey(tmp2))
			relationCode.put(tmp2, fnAllList.get(i).get("F7"));
		if (!relationName.containsKey(tmp2))
			relationName.put(tmp2, fnAllList.get(i).get("F8"));
		if (!relationId.containsKey(tmp2))
			relationId.put(tmp2, fnAllList.get(i).get("F9"));
		if (!relAcctBirthday.containsKey(tmp2))
			relAcctBirthday.put(tmp2, parse.stringToInteger(fnAllList.get(i).get("F10")));
		if (!relAcctGender.containsKey(tmp2))
			relAcctGender.put(tmp2, fnAllList.get(i).get("F11"));
		if (!repayAcctSeq.containsKey(tmp2))
			repayAcctSeq.put(tmp2, fnAllList.get(i).get("F12"));
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

	private String checkAcctAuth(tmpBorm tmp2, TitaVo titaVo) {
		String failFlag = " ";
		limitAmt = BigDecimal.ZERO;
		BankAuthAct tBankAuthAct = new BankAuthAct();
		BankAuthActId tBankAuthActId = new BankAuthActId();

		tBankAuthActId.setCustNo(tmp2.getCustNo());
		tBankAuthActId.setFacmNo(tmp2.getFacmNo());

		if ("700".equals(repayBank.get(tmp2))) {
			tBankAuthActId.setAuthType("01");
		} else {
			tBankAuthActId.setAuthType("00");
		}

		tBankAuthAct = bankAuthActService.findById(tBankAuthActId, titaVo);

		if (tBankAuthAct != null) {
			failFlag = tBankAuthAct.getStatus();
			limitAmt = tBankAuthAct.getLimitAmt();
		}

		return failFlag;
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

		private int getBormNo() {
			return bormNo;
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