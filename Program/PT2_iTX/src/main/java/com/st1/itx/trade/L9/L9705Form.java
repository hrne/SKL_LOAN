package com.st1.itx.trade.L9;

import java.math.BigDecimal;
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
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * L9705Form 套印存入憑條
 * 
 * @author eric
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L9705Form extends MakeReport {

	@Autowired
	private BaTxCom dBaTxCom;

	@Autowired
	private Parse parse;

	@Autowired
	WebClient webClient;

	@Override
	public void printTitle() {
	}

	@Override
	public void printHeader() {
	}

	public void exec(List<Map<String, String>> l9705List, TitaVo titaVo, TxBuffer txbuffer) throws LogicException {

		String tran = "L9705".equals(titaVo.getTxcd()) ? "L9705" : titaVo.getTxcd();

		int terms = 6; // 預設印6期

		if (titaVo.containsKey("Terms") && titaVo.getParam("Terms") != null) {
			try {
				terms = Integer.parseInt(titaVo.getParam("Terms"));
			} catch (Exception e) {
				terms = 6; // 若無法轉為數值,改為預設6
			}
		}

		this.info("titaVo = " + titaVo);

		this.info("txbuffer = " + txbuffer.toString());

		this.info("l9705List.size() = " + l9705List.size());

		dBaTxCom.setTxBuffer(txbuffer);

		if (l9705List.size() > 0) {
			int count = 0;
			ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
					.setRptCode("L9705".equals(titaVo.getTxcd()) ? "L9705B" : tran + "C").setRptItem("存入憑條")
					.setRptSize("cm,20,9.31333").setSecurity(this.getSecurity()).setPageOrientation("P").build();
			this.openForm(titaVo, reportVo);

			for (Map<String, String> tL9Vo : l9705List) {
				if (count > 0) {
					this.newPage();
				}

				count++;
				this.info("tran = " + tran + ",count = " + count);
				this.info("CustNo = " + tL9Vo.get("CustNo"));
				this.info("FacmNo = " + tL9Vo.get("FacmNo"));
				this.info("ENTDY = " + titaVo.getParam("ENTDY"));
				this.info("CustName = " + tL9Vo.get("CustName"));
				this.info("RepayCode = " + tL9Vo.get("RepayCode"));
//				if (!"2".equals(tL9Vo.get("RepayCode"))) {
//					continue;
//				}

				int custNo = 0;
				int facmNo = 0;
				int entryDate = parse.stringToInteger(titaVo.getParam("ENTDY"));
				String custName = "";

				if (tL9Vo.get("CustNo") != null) {
					custNo = parse.stringToInteger(tL9Vo.get("CustNo"));
				}
				if (tL9Vo.get("FacmNo") != null) {
					facmNo = parse.stringToInteger(tL9Vo.get("FacmNo"));
				}
//				if (tL9Vo.get("RepayCode") != null) {
//					repayCode = tL9Vo.get("RepayCode");
//				}

				if (tL9Vo.get("CustName") != null) {
					custName = tL9Vo.get("CustName");
				}

//				ArrayList<BaTxVo> lBaTxVo = new ArrayList<>();
				ArrayList<BaTxVo> listBaTxVo = new ArrayList<>();

				dBaTxCom.setTxBuffer(txbuffer);

				try {
//
					listBaTxVo = dBaTxCom.termsPay(entryDate, custNo, facmNo, 0, terms, 0, titaVo);
				} catch (LogicException e) {
					this.info("listBaTxVo ErrorMsg :" + e.getMessage());
				}

				this.info("L9705Form = " + custNo + "-" + facmNo);
				this.info("listBaTxVo.size = " + listBaTxVo.size());
				this.info("listBaTxVo = " + listBaTxVo.toString());

				// 溢短繳
				int excessive = dBaTxCom.getExcessive().intValue() - dBaTxCom.getShortfall().intValue();
				// 帳管費 + 契變手續費
//				int acctFee = dBaTxCom.getAcctFee().intValue() + dBaTxCom.getModifyFee().intValue();

				if (listBaTxVo.size() > 0) {
					HashMap<Integer, BigDecimal> principal = new HashMap<>();
					HashMap<Integer, BigDecimal> interest = new HashMap<>();
					HashMap<Integer, BigDecimal> breachAmt = new HashMap<>();
					HashMap<Integer, Integer> flag = new HashMap<>();
//					BigDecimal intRate;
					BigDecimal loanBal = BigDecimal.ZERO;
					BigDecimal unPaidAmt = BigDecimal.ZERO;
					BigDecimal acctFee = BigDecimal.ZERO;
//					DecimalFormat df1 = new DecimalFormat("#,##0");
					int payIntDate = 0;

					// 未收本息 = 本金+利息 Principal + Interest
					// 違約金 有 但要扣成 0 BreachAmt
					// 溢短繳 UnPaidAmt
					// 合計 = 未收本息 + 違約金 - 溢短繳
					// 未還本金餘額
//				金額欄位 同下繳日的加總
					for (BaTxVo ba : listBaTxVo) {
						if (ba.getDataKind() == 2) {
							payIntDate = ba.getPayIntDate();
							if (principal.containsKey(payIntDate)) {
								principal.put(payIntDate, principal.get(payIntDate).add(ba.getPrincipal()));
							} else {
								principal.put(payIntDate, ba.getPrincipal());
							}

							if (interest.containsKey(payIntDate)) {
								interest.put(payIntDate, interest.get(payIntDate).add(ba.getInterest()));
							} else {
								interest.put(payIntDate, ba.getInterest());
							}

							if (breachAmt.containsKey(payIntDate)) {
								breachAmt.put(payIntDate, breachAmt.get(payIntDate).add(ba.getBreachAmt()));
							} else {
								breachAmt.put(payIntDate, ba.getBreachAmt());
							}
//					本金為總和
							loanBal = loanBal.add(ba.getLoanBal());
						}
//					溢短繳 = 暫收款 - 短繳期金
//						if (ba.getDataKind() == 3) {
//							unPaidAmt = unPaidAmt.add(ba.getUnPaidAmt());
//						}
//						if (ba.getDataKind() == 1 && ba.getRepayType() == 1) {
//							unPaidAmt = unPaidAmt.subtract(ba.getUnPaidAmt());
//						}
//					04.帳管費(總和)，含06.契變手續費
						if (ba.getRepayType() == 4 || ba.getRepayType() == 6) {
							acctFee = acctFee.add(ba.getUnPaidAmt());
						}
					}

					this.info("ba.payIntDate before = " + payIntDate);
					// 無計息資料
//					if (payIntDate == 0) {
//						continue;
//					}
//					this.info("ba.payIntDate after = " + payIntDate);
//					if (cnt > 0) {
//						this.newPage();
//					}
//					cnt++;
		
					int tmpUnPaidAmt = 0;

					if (excessive < 0) {
						tmpUnPaidAmt = unPaidAmt.intValue() - excessive;
						
						excessive = 0;
					} else {
						if (unPaidAmt.intValue() > excessive) {
							tmpUnPaidAmt = unPaidAmt.intValue() - excessive;
							excessive = 0;
						} else {
							tmpUnPaidAmt = 0;
							excessive = excessive - unPaidAmt.intValue();
						}
					}
					unPaidAmt = new BigDecimal(tmpUnPaidAmt);
					
					// 只有第一筆需要sum
					if (count != 1) {
						unPaidAmt = BigDecimal.ZERO;
						acctFee = BigDecimal.ZERO;
					}

					for (BaTxVo ba : listBaTxVo) {
						this.info("getCustNo=" + ba.getCustNo());

						this.info("getFacmNo=" + ba.getFacmNo());

						this.info("getPayIntDate=" + payIntDate);

						this.info("getDataKind=" + ba.getDataKind());

						payIntDate = ba.getPayIntDate();
						// 本金、利息
						if (ba.getDataKind() != 2) {
							continue;
						}

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
						// 本金a
						// 利息
						// 未還本金餘額
						// -溢短繳
						// +帳管費
						BigDecimal bBreachAmt = breachAmt.get(payIntDate);
						bBreachAmt = bBreachAmt == null ? BigDecimal.ZERO : bBreachAmt;
						BigDecimal bPrincipal = principal.get(payIntDate);
						bPrincipal = bPrincipal == null ? BigDecimal.ZERO : bPrincipal;
						BigDecimal bInterest = interest.get(payIntDate);
						bInterest = bInterest == null ? BigDecimal.ZERO : bInterest;

						BigDecimal bSummry = bBreachAmt.add(bPrincipal).add(bInterest).subtract(unPaidAmt).add(acctFee);
						this.info("bBreachAmt ..." + bBreachAmt);
						this.info("bPrincipal ..." + bPrincipal);
						this.info("bInterest ..." + bInterest);
						this.info("bSummry ..." + bSummry);

						loanBal = loanBal.subtract(bPrincipal);

//						this.print(1, 1,
//								"                                                                                                                                                                               ");
//						if (!"00000000".equals(sPayIntDate)) {
//							this.print(0, 7, sPayIntDate.substring(0, 3) + "/" + sPayIntDate.substring(3, 5) + "/"
//									+ sPayIntDate.substring(5, 7));
//						}

//						this.print(0, 25, df1.format(bBreachAmt), "R");
//						this.print(0, 40, df1.format(bPrincipal), "R");
//						this.print(0, 56, df1.format(bInterest), "R");
//						this.print(0, 70, df1.format(bSummry), "R");
//						this.print(0, 84, df1.format(loanBal), "R");
//						this.print(0, 94, "0", "R");
//						this.print(0, 108, df1.format(bSummry), "R");

						setFont(1, 14);

						printCm(4, 4, sPayIntDate.substring(0, 3) + "/" + sPayIntDate.substring(3, 5) + "/"
								+ sPayIntDate.substring(5, 7));

						printCm(4, 4.8, custName);
						String custnoX = String.format("%07d", custNo);

						for (int i = 0; i < 7; i++) {
							double x = 3.5 + (i * 0.5);
							printCm(x, 6.2, custnoX.substring(i, i + 1));
						}

						String amtX = bSummry.toString() + "00";

						for (int i = 0; i < amtX.length(); i++) {
							double x = 14.3 - (i * 0.5);
							int ii = amtX.length() - 1 - i;
							String s = toChinese(amtX.substring(ii, ii + 1));
							printCm(x, 3.6, s);
							String s2 = toChinese2(amtX.substring(ii, ii + 1));
							printCm(x, 6.2, s2);
						}

						printCm(16, 6.2, titaVo.getTlrNo());

						break;
					} // loop -- batxCom

				}

			}
		} else {
			ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
					.setRptCode("L9705".equals(titaVo.getTxcd()) ? "L9705B" : tran + "C").setRptItem("存入憑條")
					.setRptSize("cm,20,9.31333").setSecurity("").setPageOrientation("P").build();

			this.openForm(titaVo, reportVo);
			this.setRptItem("存入憑條(無符合資料)");
			printCm(1, 4, "【無符合資料】");
		}
		this.close();

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO"), titaVo.getTxCode().isEmpty() ? "L9705" : titaVo.getTxCode() + "存入憑條已完成",
				titaVo);

	}

	private String toChinese(String s) {
		String rs = "";

		if ("0".equals(s)) {
			rs = "０";
		} else if ("1".equals(s)) {
			rs = "１";
		} else if ("2".equals(s)) {
			rs = "２";
		} else if ("3".equals(s)) {
			rs = "３";
		} else if ("4".equals(s)) {
			rs = "４";
		} else if ("5".equals(s)) {
			rs = "５";
		} else if ("6".equals(s)) {
			rs = "６";
		} else if ("7".equals(s)) {
			rs = "７";
		} else if ("8".equals(s)) {
			rs = "８";
		} else if ("9".equals(s)) {
			rs = "９";
		}
		return rs;
	}

	private String toChinese2(String s) {
		String rs = "";

		if ("0".equals(s)) {
			rs = "零";
		} else if ("1".equals(s)) {
			rs = "壹";
		} else if ("2".equals(s)) {
			rs = "貳";
		} else if ("3".equals(s)) {
			rs = "參";
		} else if ("4".equals(s)) {
			rs = "肆";
		} else if ("5".equals(s)) {
			rs = "伍";
		} else if ("6".equals(s)) {
			rs = "陸";
		} else if ("7".equals(s)) {
			rs = "柒";
		} else if ("8".equals(s)) {
			rs = "捌";
		} else if ("9".equals(s)) {
			rs = "玖";
		}
		return rs;
	}
}
