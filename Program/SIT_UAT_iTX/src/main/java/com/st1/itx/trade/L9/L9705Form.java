package com.st1.itx.trade.L9;

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
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
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
		this.openForm(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(),
				titaVo.getTxCode().isEmpty() ? "L9705" : titaVo.getTxCode(), "存入憑條", "cm,20,9.31333", "P");

		if (l9705List.size() > 0) {

			int cnt = 0;
			for (Map<String, String> tL9Vo : l9705List) {
				if (!"2".equals(tL9Vo.get("RepayCode"))) {
					continue;
				}

				int custNo = 0;
				int facmNo = 0;
				int entryDate = parse.stringToInteger(titaVo.getParam("ENTDY"));
				String repayCode = "";
				String custName = "";

				if (tL9Vo.get("CustNo") != null) {
					custNo = parse.stringToInteger(tL9Vo.get("CustNo"));
				}
				if (tL9Vo.get("FacmNo") != null) {
					facmNo = parse.stringToInteger(tL9Vo.get("FacmNo"));
				}
				if (tL9Vo.get("RepayCode") != null) {
					repayCode = tL9Vo.get("RepayCode");
				}

				if (tL9Vo.get("CustName") != null) {
					custName = tL9Vo.get("CustName");
				}

				List<BaTxVo> listBaTxVo = new ArrayList<>();

				dBaTxCom.setTxBuffer(txbuffer);

				try {
					listBaTxVo = dBaTxCom.termsPay(entryDate, custNo, facmNo, 0, 6, titaVo);
				} catch (LogicException e) {
					this.info("listBaTxVo ErrorMsg :" + e.getMessage());
				}

				if (listBaTxVo.size() > 0) {
					this.info("L9705Form = " + custNo + "-" + facmNo);
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
						if (ba.getDataKind() == 3) {
							unPaidAmt = unPaidAmt.add(ba.getUnPaidAmt());
						}
						if (ba.getDataKind() == 1 && ba.getRepayType() == 1) {
							unPaidAmt = unPaidAmt.subtract(ba.getUnPaidAmt());
						}
//					04.帳管費(總和)，含06.契變手續費
						if (ba.getRepayType() == 4 || ba.getRepayType() == 6) {
							acctFee = acctFee.add(ba.getUnPaidAmt());
						}
					}
					// 無計息資料
					if (payIntDate == 0) {
						continue;
					}

					if (cnt > 0)
						this.newPage(true);
					cnt++;

					for (BaTxVo ba : listBaTxVo) {
						// 本金、利息
						if (ba.getDataKind() != 2) {
							continue;
						}
						payIntDate = ba.getPayIntDate();

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

						printCm(4,4.8,custName);
						String custnoX = String.format("%07d", custNo);

						for (int i = 0; i < 7; i++) {
							double x = 4.2 + (i * 0.5);
							printCm(x, 6.2, custnoX.substring(i, i + 1));
						}

						String amtX = bSummry.toString() + "00";

						for (int i = 0; i < amtX.length(); i++) {
							double x = 14.6 - (i * 0.5);
							int ii = amtX.length() - 1 - i;
							String s = toChinese(amtX.substring(ii, ii + 1));
							printCm(x, 3.6, s);
							printCm(x, 6.2, s);
						}

						printCm(16, 6.2, titaVo.getTlrNo());
						
						break;
					} // loop -- batxCom
				}

			}
		}
		
		long sno = this.close();
		
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO"), titaVo.getTxCode().isEmpty() ? "L9705" : titaVo.getTxCode() + "存入憑條已完成", titaVo);
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
}
