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
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.springjpa.cm.L4702ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9705ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
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

	@Autowired
	private L9705ServiceImpl l9705ServiceImpl;

	@Autowired
	private L4702ServiceImpl l4702ServiceImpl;

	@Autowired
	private BaTxCom dBaTxCom;

	@Autowired
	private CdEmpService cdEmpService;

	@Autowired
	private Parse parse;

	@Autowired
	WebClient webClient;

	@Override
	public void printHeader() {

		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(35);

	}

	public void exec(List<Map<String, String>> l9705List, TitaVo titaVo, TxBuffer txbuffer) throws LogicException {
		/*
		 * ["#R1+@會計日期",#ACCTDATE_ST,"～",#ACCTDATE_ED,"止有變動者"], ["#R2+@戶號",#CUSTNO],
		 * ["#R3+@選擇條件一",#CONDITION1,#CONDITION1X],
		 * ["#R4+@選擇條件二",#CONDITION2,#CONDITION2X], ["#R5+@戶別",#ID_TYPE,#ID_TYPEX],
		 * ["#R6+@企金別",#CORP_IND,#CORP_INDX], ["#R7+@業務科目",#APNO,#APNOX],
		 */

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(),
				titaVo.getTxCode().isEmpty() ? "L9705" : titaVo.getTxCode(), "放款本息攤還表暨繳息通知單", "密", "8.5,12", "P");

		String entdy = titaVo.getEntDy();

		String conditionCode = "";

		if (titaVo.get("CONDITION1") != null) {
			conditionCode = titaVo.get("CONDITION1");
		}

//		A.匯款轉帳，已入金，有欠繳-------入帳完成後，於應處理事項清單執行L4702。
//		B.銀扣火險成功，期款失敗----------L4454.銀扣失敗通知(一扣)

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

				int custNo = 0;
				int facmNo = 0;
				int entryDate = parse.stringToInteger(titaVo.getParam("ENTDY"));
				String repayCode = "";
				String custName = "";
				String payIntAcct = "";
				String payPriAcct = "";
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

			
				if (tL9Vo.get("CustNo") != null) {
//					共用代碼檔0:個金1:企金2:企金自然人
				if ("0".equals(tL9Vo.get("EntCode"))) {
					payIntAcct = "9510200" + String.format("%07d", Integer.valueOf(custNo));
					payPriAcct = "9510300" + String.format("%07d", Integer.valueOf(custNo));
				} else {
					payIntAcct = "9510100" + String.format("%07d", Integer.valueOf(custNo));
					payPriAcct = "9510100" + String.format("%07d", Integer.valueOf(custNo));
				}
			}

			
				try {
					listBaTxVo = dBaTxCom.termsPay(entryDate, custNo, facmNo, 0, 6, titaVo);
				} catch (LogicException e) {
					this.info("listBaTxVo ErrorMsg :" + e.getMessage());
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

//					不同戶號額度跳頁
					if (cnt >= 1) {
						this.info("new page...");
						this.newPage();
					}

					cnt = cnt + 1;

					intRate = listBaTxVo.get(0).getIntRate();

					String unPaidAmtX = "";
					String acctFeeX = "";

					if (unPaidAmt.compareTo(BigDecimal.ZERO) != 0) {
						unPaidAmtX = df1.format(unPaidAmt);
					}
					if (acctFee.compareTo(BigDecimal.ZERO) != 0) {
						acctFeeX = df1.format(acctFee);
					}

					this.print(1, 45, "放款本息攤還表暨繳息通知單", "C");
					this.print(1, 1, "");
					this.print(1, 1, "");
					this.print(1, 10, "製發日期：　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　");
					this.print(0, 20,
							entdy.substring(1, 4) + "/" + entdy.substring(4, 6) + "/" + entdy.substring(6, 8));
					this.print(0, 78, repayCodeX(repayCode));
					this.print(1, 10, "戶號：　　　　　　　目前利率：　　　　%");
					this.print(0, 16, String.format("%07d", Integer.valueOf(custNo)) + "-" + String.format("%03d", Integer.valueOf(facmNo)));
					this.print(0, 47, padStart(6, "" + intRate), "R");
					this.print(1, 10, "客戶名稱：　　　　　　　　　　　　　　　　　　　　　溢短繳：");
					this.print(0, 23, custName);
					this.print(0, 87, unPaidAmtX, "R");
					this.print(1, 10, "　　　　　　　　　　　　　　　　　　　　　　　　　　帳管費：");
					this.print(0, 87, acctFeeX, "R");
					this.print(1, 10, "　　　　　　　　　　　　　　　　每期應攤還　　　　　　　　　　　　未　　還　　暫　付");
					this.print(1, 10, "應繳日　　違約金　　　　　本金　　　　　　利息　　　應繳合計　　　本金餘額　　所得稅　　　應繳淨額");
					this.print(1, 7, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

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
					
					// 滯繳通知單
//					this.print(1, 8,
//							"＊＊註：違約金暫計到" + transRocChinese(titaVo.getParam("EntryDate")) + "，若提前或延後繳款，請電話查詢　該違約金額");
					
					// 到期通知單
//					this.print(1, 8, "＊＊貴戶所借款項如業已屆期，本公司雖經收取利息及違約金但並無同意延期清償之意，貴戶仍應依約履行，");
//					this.print(1, 8, "　　貴戶所貸上列款項，於" + transRocChinese(titaVo.getParam("EntryDate")) + "到期，請依約到本公司辦理清償或展期手續，請勿延誤。");
					
					// 銀扣火險成功期款失敗通知單
//					this.print(1, 8,
//							"＊＊註：違約金暫計到" + transRocChinese(titaVo.getParam("EntryDate")) + "，若提前或延後繳款，請電話查詢　該違約金額");
//					this.print(1, 8, "＊＊您好！本月份扣款含年度火險、地震險保費、每月房貸期款，因您存款不足；");
//					this.print(1, 8, "　　請速將本期款匯入期款專用帳號。");
					
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
					

					this.print(3, 18, payIntAcct);
					this.print(0, 75, payPriAcct);
					if ("C".equals(conditionCode)) {
						
						String EntryDate = tL9Vo.get("EntryDate"); // 入帳日期
						BigDecimal RepayAmt = parse.stringToBigDecimal(tL9Vo.get("RepayAmt"));
						
						if(RepayAmt.compareTo(new BigDecimal("0")) > 0) {
							this.print(1, 8,"◎台端於　" + transRocChinese(EntryDate) + " 所匯之還本金$　　　　　　　　　業已入帳無誤。");
							this.print(0, 48,df1.format(RepayAmt),"");									
						}
					} 
				} else {
					if (cnt == 0) {
//					this.print(1, 20, "*******    查無資料   ******");
					}
				}
			} // loop -- Impl
		}
		long sno = this.close();
		this.toPdf(sno);
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO"), titaVo.getTxCode().isEmpty() ? "L9705" : titaVo.getTxCode() + "放款本息攤還表暨繳息通知單已完成", titaVo);
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

	private String repayCodeX(String repayCode) {
		String result = "";
		switch (repayCode) {
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
