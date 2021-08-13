package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.springjpa.cm.L4454BServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L4702ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9705ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * L9705Report
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L9705Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(L9705Report.class);

	@Autowired
	private L9705ServiceImpl l9705ServiceImpl;

	@Autowired
	private L4702ServiceImpl l4702ServiceImpl;

	@Autowired
	private L4454BServiceImpl l4454BServiceImpl;

	@Autowired
	private BaTxCom dBaTxCom;

	@Autowired
	private CdEmpService cdEmpService;

	@Autowired
	private Parse parse;

	@Override
	public void printHeader() {

		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(35);

	}

	public void exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {
		/*
		 * ["#R1+@會計日期",#ACCTDATE_ST,"～",#ACCTDATE_ED,"止有變動者"], ["#R2+@戶號",#CUSTNO],
		 * ["#R3+@選擇條件一",#CONDITION1,#CONDITION1X],
		 * ["#R4+@選擇條件二",#CONDITION2,#CONDITION2X], ["#R5+@戶別",#ID_TYPE,#ID_TYPEX],
		 * ["#R6+@企金別",#CORP_IND,#CORP_INDX], ["#R7+@業務科目",#APNO,#APNOX],
		 */

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9705", "放款本息攤還表暨繳息通知單", "密", "8.5,12", "P");

		String entdy = titaVo.getEntDy();

		List<Map<String, String>> l9705List = null;

		String conditionCode = "";

		if (titaVo.get("CONDITION1") != null) {
			conditionCode = titaVo.get("CONDITION1");
		}

//		A.匯款轉帳，已入金，有欠繳-------入帳完成後，於應處理事項清單執行L4702。
//		B.銀扣火險成功，期款失敗----------L4454.銀扣失敗通知(一扣)

		if ("A".equals(conditionCode)) {
			try {
				l9705List = l4702ServiceImpl.findAll(titaVo);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("l4702ServiceImpl.findAll error = " + errors.toString());
			}
		} else if ("B".equals(conditionCode)) {
			try {
				l9705List = l4454BServiceImpl.findAll(titaVo);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("l4454BServiceImpl.findAll error = " + errors.toString());
			}
		} else {
			try {
				l9705List = l9705ServiceImpl.findAll(titaVo);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("l9705ServiceImpl.findAll error = " + errors.toString());
			}
		}

		if (l9705List.size() == 0) {

			this.print(1, 20, "*******    查無資料   ******");

			this.info("查無資料 Return...");
		} else {

			int times = 0;
			int cnt = 0;
			this.info("l9705List-------->" + l9705List.size());
			for (Map<String, String> tL9Vo : l9705List) {

				this.info("Now Page :" + this.getNowPage());

				times++;
				List<BaTxVo> listBaTxVo = new ArrayList<>();

				dBaTxCom.setTxBuffer(txbuffer);

				String custNo = "";
				String facmNo = "";
				String custName = "";
				String repayType = "";

				if (tL9Vo.get("F0") != null) {
					custNo = FormatUtil.pad9(tL9Vo.get("F0"), 7);
				}
				if (tL9Vo.get("F1") != null) {
					facmNo = FormatUtil.pad9(tL9Vo.get("F1"), 3);
				}
				if (tL9Vo.get("F2") != null) {
					custName = tL9Vo.get("F2");
				}
				if (tL9Vo.get("F3") != null) {
					repayType = tL9Vo.get("F3");
				}

				if ("B".equals(conditionCode)) {
					try {
						listBaTxVo = dBaTxCom.termsPay(parse.stringToInteger(titaVo.getParam("EntryDate")),
								parse.stringToInteger(custNo), parse.stringToInteger(facmNo), 0, 6, titaVo);
					} catch (LogicException e) {
						this.info("listBaTxVo ErrorMsg :" + e.getMessage());
					}
				} else {
					try {
						listBaTxVo = dBaTxCom.termsPay(parse.stringToInteger(titaVo.getParam("ENTDY")),
								parse.stringToInteger(custNo), parse.stringToInteger(facmNo), 0, 6, titaVo);
					} catch (LogicException e) {
						this.info("listBaTxVo ErrorMsg :" + e.getMessage());
					}
				}

				if (listBaTxVo.size() > 0) {
					HashMap<Integer, BigDecimal> principal = new HashMap<>();
					HashMap<Integer, BigDecimal> interest = new HashMap<>();
					HashMap<Integer, BigDecimal> breachAmt = new HashMap<>();
					HashMap<Integer, Integer> flag = new HashMap<>();
					BigDecimal intRate;
					BigDecimal loanBal = BigDecimal.ZERO;
					BigDecimal unPaidAmt = BigDecimal.ZERO;
					BigDecimal acctFee = BigDecimal.ZERO;
					DecimalFormat df1 = new DecimalFormat("#,##0");
					int payIntDate = 0;

					this.info("l9705List.size()..." + l9705List.size());
					this.info("times..." + times);
					this.info("cnt..." + cnt);

//				不同戶號額度跳頁
					if (cnt >= 1) {
						this.info("new page...");
						this.newPage();
//						continue;
					}

					cnt = cnt + 1;

					// 未收本息 = 本金+利息 Principal + Interest
					// 違約金 有 但要扣成 0 BreachAmt
					// 溢短繳 UnPaidAmt
					// 合計 = 未收本息 + 違約金 - 溢短繳
					// 未還本金餘額
//				金額欄位 同下繳日的加總
					for (int i = 0; i < listBaTxVo.size(); i++) {
						payIntDate = listBaTxVo.get(i).getPayIntDate();
						this.info("loop-1 payIntDate ..." + payIntDate);

						if (principal.containsKey(payIntDate)) {
							principal.put(payIntDate, principal.get(payIntDate).add(listBaTxVo.get(i).getPrincipal()));
						} else {
							principal.put(payIntDate, listBaTxVo.get(i).getPrincipal());
						}

						if (interest.containsKey(payIntDate)) {
							interest.put(payIntDate, interest.get(payIntDate).add(listBaTxVo.get(i).getInterest()));
						} else {
							interest.put(payIntDate, listBaTxVo.get(i).getInterest());
						}

						if (breachAmt.containsKey(payIntDate)) {
							breachAmt.put(payIntDate, breachAmt.get(payIntDate).add(listBaTxVo.get(i).getBreachAmt()));
						} else {
							breachAmt.put(payIntDate, listBaTxVo.get(i).getBreachAmt());
						}
//					本金為總和
						loanBal = loanBal.add(listBaTxVo.get(i).getLoanBal());
//					暫收金額為總和
						if (listBaTxVo.get(i).getDataKind() == 4) {
							unPaidAmt = unPaidAmt.add(listBaTxVo.get(i).getUnPaidAmt());
						}
//					04.帳管費(總和)
						if (listBaTxVo.get(i).getRepayType() == 4) {
							acctFee = acctFee.add(listBaTxVo.get(i).getUnPaidAmt());
						}
					}

					intRate = listBaTxVo.get(0).getIntRate();

					String unPaidAmtX = "";
					String acctFeeX = "";

					if (unPaidAmt.compareTo(BigDecimal.ZERO) == 1) {
						unPaidAmtX = df1.format(unPaidAmt);
					}
					if (acctFee.compareTo(BigDecimal.ZERO) == 1) {
						acctFeeX = df1.format(acctFee);
					}

					this.print(1, 45, "放款本息攤還表暨繳息通知單", "C");
					this.print(1, 1, "");
					this.print(1, 1, "");
					this.print(1, 10, "製發日期：　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
					this.print(0, 20,
							entdy.substring(0, 3) + "/" + entdy.substring(3, 5) + "/" + entdy.substring(5, 7));
					this.print(0, 78, repayTypeX(repayType));
					this.print(1, 10, "戶號：　　　　　　　目前利率：　　　　%");
					this.print(0, 16, custNo + "-" + facmNo);
					this.print(0, 47, padStart(6, "" + intRate), "R");
					this.print(1, 10, "客戶名稱：　　　　　　　　　　　　　　　　　　　　　溢短繳：");
					this.print(0, 23, custName);
					this.print(0, 87, unPaidAmtX, "R");
					this.print(1, 10, "　　　　　　　　　　　　　　　　　　　　　　　　　　帳管費：");
					this.print(0, 87, acctFeeX, "R");
					this.print(1, 10, "　　　　　　　　　　　　　　　　每期應攤還　　　　　　　　　　　　未　　還　　暫　付");
					this.print(1, 10, "應繳日　　違約金　　　　　本金　　　　　　利息　　　應繳合計　　　本金餘額　　所得稅　　　應繳淨額");
					this.print(1, 7, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

					for (int i = 0; i < listBaTxVo.size(); i++) {
						payIntDate = listBaTxVo.get(i).getPayIntDate();

//					同一日期者金額加總只顯示一筆
						if (!flag.containsKey(payIntDate)) {
							this.info("loop-2 payIntDate ..." + payIntDate);
							flag.put(payIntDate, 1);
						} else {
							this.info("loop-2 continue ..." + payIntDate);
							continue;
						}

						String sPayIntDate = FormatUtil.pad9("" + payIntDate, 7);
						// 違約金
						// 本金
						// 利息
						// 未還本金餘額
						BigDecimal bBreachAmt = breachAmt.get(payIntDate);
						BigDecimal bPrincipal = principal.get(payIntDate);
						BigDecimal bInterest = interest.get(payIntDate);
						BigDecimal bSummry = bBreachAmt.add(bPrincipal.add(bInterest));

						loanBal = loanBal.subtract(bPrincipal);

						this.print(1, 1,
								"                                                                                                                                                                               ");
						if (!"00000000".equals(sPayIntDate)) {
							this.print(0, 7, sPayIntDate.substring(0, 3) + "/" + sPayIntDate.substring(3, 5) + "/"
									+ sPayIntDate.substring(5, 7));
						}

						this.print(0, 25, df1.format(bBreachAmt), "R");
						this.print(0, 40, df1.format(bPrincipal), "R");
						this.print(0, 56, df1.format(bInterest), "R");
						this.print(0, 70, df1.format(bSummry), "R");
						this.print(0, 84, df1.format(loanBal), "R");
						this.print(0, 94, "0", "R");
						this.print(0, 108, df1.format(bSummry), "R");
					} // loop -- batxCom

					this.print(2, 8, "＊＊舊繳息通知單作廢（以最新製發日期為準）。");
					if ("B".equals(conditionCode)) {
						this.print(1, 8,
								"＊＊註：違約金暫計到" + transRocChinese(titaVo.getParam("EntryDate")) + "，若提前或延後繳款，請電話查詢　該違約金額");
						this.print(1, 8, "＊＊您好！本月份扣款含年度火險、地震險保費、每月房貸期款，因您存款不足；");
						this.print(1, 8, "　　請速將本期款匯入期款專用帳號。");
					}

					this.print(1, 8, "＊＊新光銀行城內分行代號： 1030116");

					String iTLRNO = "";

					if (titaVo.getParam("TLRNO") != null) {
						iTLRNO = titaVo.getParam("TLRNO");
					}

					CdEmp tCdEmp = cdEmpService.findById(iTLRNO, titaVo);

					String empName = "";

					if (tCdEmp != null) {
						empName = tCdEmp.getFullname();
					}

					this.print(1, 86, "製表人 " + empName);

					this.info("l9705List.size()..." + l9705List.size());
					this.info("times..." + times);
				} else {
					if (cnt == 0) {
//					this.print(1, 20, "*******    查無資料   ******");
					}
				}
			} // loop -- Impl
		}
		long sno = this.close();
		this.toPdf(sno);
	}

	private String transRocChinese(String date) {
		String result = "";

		result = FormatUtil.pad9(date, 7);
		result = result.substring(0, 3) + " 年 " + result.substring(3, 5) + " 月 " + result.substring(5) + " 日";

		return result;
	}

	private String padStart(int size, String input) {
		for (int i = 0; i < size; i++) {
			if (input.length() < size) {
				input = input + "0";
			}
		}
		return input;
	}

	private String repayTypeX(String repaytype) {
		String result = "";
		switch (repaytype) {
		case "1":
			result = "匯款轉帳";
			break;
		case "2":
			result = "銀行扣款";
			break;
		case "3":
			result = "員工扣薪";
			break;
		case "4":
			result = "支票繳款";
			break;
		default:
			break;
		}

		return result;
	}
}
