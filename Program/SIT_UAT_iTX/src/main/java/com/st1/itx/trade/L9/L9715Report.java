package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9715ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9715Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L9715Report.class);

	@Autowired
	L9715ServiceImpl l9715ServiceImpl;

	@Autowired
	BaTxCom dBaTxCom;

	@Autowired
	Parse parse;

	private String name = "";

	private String fundDay = "";
	private String approDay = "";

	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");

		printHeaderL1();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(11);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(35);

	}

	public void printHeaderL1() {
		this.setFontSize(8);
		this.setCharSpaces(0);
		this.print(-1, 170, "機密等級：機密");
		this.print(-2, 170, "基　礎：" + dDateUtil.getNowStringBc().substring(0, 4) + "/"
				+ dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(6));
		this.print(-3, 8, "程式ID：" + this.getParentTranCode());
		this.print(-3, 100, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dDateUtil.getNowStringBc().substring(4, 6)));
		this.print(-3, 170, "日　期：" + tim + "/" + dDateUtil.getNowStringBc().substring(6) + "/"
				+ dDateUtil.getNowStringBc().substring(2, 4));
		this.print(-4, 8, "報　表：" + this.getRptCode());
		this.print(-4, 100, "業務專辦照顧十八個月明細表", "C");
		this.print(-4, 170, "時　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-5, 170, "頁　數：　" + this.getNowPage());
//		this.print(-6, 6, "經辦：" + name);
		this.print(-6, 100, "撥款日期　" + dDateUtil.getNowStringBc().substring(0, 4) + "/"
				+ dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(6) + "　起",
				"C");
		this.print(-6, 170, "單　位：元");
		this.print(-8, 6, " 押品　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　最近　　逾期");
		this.print(-9, 6,
				"地區別　繳款方式　　戶號　　　　戶名　　　　初貸日　　　本金餘額　 利率　 迄繳息日　 應繳日　日數　　　未收本息　　　　違約金　　溢短款　　　　合計　連絡人　　電話　　　　　　追蹤情形");
		this.print(-10, 5,
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {

		fundDay = titaVo.getParam("FUND_DAY");
		approDay = titaVo.getParam("APPRO_DAY");

		List<Map<String, String>> l9715List = null;
		try {
			l9715List = l9715ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9715ServiceImpl.findall error = " + errors.toString());
		}

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9715", "放款到期明細表及通知單", "密", "A4", "");

		if (l9715List.size() != 0) {

			int Yr = 0;
			int money = 0;
			int pi = 0, ba = 0, ua = 0, sum = 0;
			for (Map<String, String> tL9Vo : l9715List) {
				// input:
				// entryDate 入帳日期
//					CustNo	       戶號
//					FacmNo	       額度
//					BormNo	       撥款
//					RepayType	還款類別
//						01-期款        
//						02-部分償還    
//						03-結案
//						04-帳管費
//						05-火險費
//						06-契變手續費
//						07-法務費
//						09-其他
//						11-債協匯入款
//					TxAmt 	回收金額

				List<BaTxVo> listBaTxVo = new ArrayList<>();

				try {
					;
					dBaTxCom.setTxBuffer(txbuffer);
					listBaTxVo = dBaTxCom.settingUnPaid(parse.stringToInteger(titaVo.getParam("ENTDY")),
							parse.stringToInteger(tL9Vo.get("F4").substring(0,7)), parse.stringToInteger(tL9Vo.get("F4").substring(8)), 0, 1,
							BigDecimal.ZERO, titaVo);
					this.info("listBaTxVo.size()-------->" + listBaTxVo.size());
					this.info("listBaTxVo-------->" + listBaTxVo.toString());
					int Principal = 0, Interest = 0;
					int BreachAmt = 0;
					int UnPaidAmt = 0;
					double IntRate = 0.00;
					// 未收本息 = 本金+利息 Principal + Interest
					// 違約金 有 但要扣成 0 BreachAmt
					// 溢短繳 UnPaidAmt
					// 合計 = 未收本息 + 違約金 - 溢短繳

					for (int i = 0; i < listBaTxVo.size(); i++) {
						Principal = Principal + listBaTxVo.get(i).getPrincipal().intValue();
						Interest = Interest + listBaTxVo.get(i).getInterest().intValue();
						BreachAmt = BreachAmt + listBaTxVo.get(i).getBreachAmt().intValue();
						UnPaidAmt = UnPaidAmt + listBaTxVo.get(i).getUnPaidAmt().intValue();
						IntRate = listBaTxVo.get(i).getIntRate().doubleValue();
					}

					String nameNo = "";
					boolean openClose = true;
					name = tL9Vo.get("F1");
					// 先產生有的
					if (!nameNo.equals(tL9Vo.get("F0"))) {
						nameNo = tL9Vo.get("F0");
						if (openClose) {
							this.print(-6, 6, "經辦：" + name);
							openClose = false;
						}else {
							this.newPage();
							this.print(-6, 6, "經辦：" + name);
						}		
					}
					
					this.print(1, 6, tL9Vo.get("F2"));
					this.print(1,14,tL9Vo.get("F3"));
					this.print(0, 24,tL9Vo.get("F4"));
					this.print(0, 38, tL9Vo.get("F5"));

					if ("0".equals(tL9Vo.get("F6")) || tL9Vo.get("F6") == null) {
						this.print(0, 57, "", "R");
					} else {
						Yr = Integer.parseInt(tL9Vo.get("F5").toString()) - 19110000;
						this.print(0, 57, String.valueOf(Yr).substring(0, 3) + "/" + String.valueOf(Yr).substring(3, 5)
								+ "/" + String.valueOf(Yr).substring(5, 7), "R");
					}

					this.print(0, 70, String.format("%,d", Integer.parseInt(tL9Vo.get("F7").toString())), "R");
					money = money + Integer.parseInt(tL9Vo.get("F7").toString()); // 本金餘額
					/**********************/
					this.print(0, 79, padEnd(6, String.valueOf(IntRate)), "R");
					/**********************/
					if ("0".equals(tL9Vo.get("F9")) || tL9Vo.get("F9") == null) {
						this.print(0, 86, "", "R");
					} else {
						Yr = Integer.parseInt(tL9Vo.get("F9").toString()) - 19110000;
						this.print(0, 86, String.valueOf(Yr).substring(0, 3) + "/" + String.valueOf(Yr).substring(3, 5)
								+ "/" + String.valueOf(Yr).substring(5, 7), "R");
					}

					if ("0".equals(tL9Vo.get("F10")) || tL9Vo.get("F10") == null) {
						this.print(0, 97, "", "R");
					} else {

						Yr = Integer.parseInt(tL9Vo.get("F10").toString()) - 19110000;
						this.print(0, 97, String.valueOf(Yr).substring(0, 3) + "/" + String.valueOf(Yr).substring(3, 5)
								+ "/" + String.valueOf(Yr).substring(5, 7), "R");
					}

					this.print(0, 102, tL9Vo.get("F9"), "R");

					/*********** calljava ***********/
					this.print(0, 112, String.format("%,d", Principal + Interest), "R");
					pi = pi + Principal + Interest;
					this.print(0, 129, "0", "R");
					ba = ba + 0;
					this.print(0, 141, String.format("%,d", UnPaidAmt), "R");
					ua = ua + UnPaidAmt;
					this.print(0, 153, String.format("%,d", (Principal + Interest + BreachAmt - UnPaidAmt)), "R");
					sum = sum + (Principal + Interest + BreachAmt - UnPaidAmt);
					/**********************/

					this.print(0, 156, tL9Vo.get("F15"));

					this.print(0, 164, tL9Vo.get("F16"));
					this.print(0, 172, tL9Vo.get("F13"));
					this.print(1, 1, "　　　　 　　　　　 　　　　　　　其他電話： ");
					if (tL9Vo.get("F16") != null) {
						this.print(0, 37, tL9Vo.get("F16"));
					} else {
//							this.print(0, 42, "*");
					}

				} catch (LogicException e) {
					this.info("baTxCom.setTxBuffer ErrorMsg :" + e.getMessage());
				}

			} // for
		} else {
			this.print(-6, 6, "經辦：" + name);
			this.print(1, 8, "本日無資料");
		}

		long sno = this.close();

		// 測試用
		this.toPdf(sno);

	}

	private String padStart(int size, String input) {
		for (int i = 0; i < size; i++) {
			if (input.length() < size) {
				input = "0" + input;
			}
		}
		return input;
	}

	private String padEnd(int size, String input) {
		for (int i = 0; i < size; i++) {
			if (input.length() < size) {
				input = input + "0";
			}
		}
		return input;
	}

//	
//	this.print(1, 5, tL9Vo.get("F0"));
////	  this.print(1,10,tL9Vo.get("F1"));
//this.print(0, 24, padStart(7, tL9Vo.get("F2")) + "-" + padStart(3, tL9Vo.get("F3")));
//this.print(0, 38, tL9Vo.get("F4"));
//
//if ("0".equals(tL9Vo.get("F5")) || tL9Vo.get("F5") == null) {
//	this.print(0, 57, "", "R");
//} else {
//	Yr = Integer.parseInt(tL9Vo.get("F5").toString()) - 19110000;
//	this.print(0, 57, String.valueOf(Yr).substring(0, 3) + "/" + String.valueOf(Yr).substring(3, 5)
//			+ "/" + String.valueOf(Yr).substring(5, 7), "R");
//}
//
//this.print(0, 70, String.format("%,d", Integer.parseInt(tL9Vo.get("F6").toString())), "R");
//money = money + Integer.parseInt(tL9Vo.get("F6").toString()); // 本金餘額
///**********************/
//this.print(0, 79, padEnd(6, String.valueOf(IntRate)), "R");
///**********************/
//if ("0".equals(tL9Vo.get("F7")) || tL9Vo.get("F7") == null) {
//	this.print(0, 86, "", "R");
//} else {
//	Yr = Integer.parseInt(tL9Vo.get("F7").toString()) - 19110000;
//	this.print(0, 86, String.valueOf(Yr).substring(0, 3) + "/" + String.valueOf(Yr).substring(3, 5)
//			+ "/" + String.valueOf(Yr).substring(5, 7), "R");
//}
//
//if ("0".equals(tL9Vo.get("F8")) || tL9Vo.get("F8") == null) {
//	this.print(0, 97, "", "R");
//} else {
//
//	Yr = Integer.parseInt(tL9Vo.get("F8").toString()) - 19110000;
//	this.print(0, 97, String.valueOf(Yr).substring(0, 3) + "/" + String.valueOf(Yr).substring(3, 5)
//			+ "/" + String.valueOf(Yr).substring(5, 7), "R");
//}
//
//this.print(0, 102, tL9Vo.get("F9"), "R");
//
///*********** calljava ***********/
//this.print(0, 112, String.format("%,d", Principal + Interest), "R");
//pi = pi + Principal + Interest;
//this.print(0, 129, "0", "R");
//ba = ba + 0;
//this.print(0, 141, String.format("%,d", UnPaidAmt), "R");
//ua = ua + UnPaidAmt;
//this.print(0, 153, String.format("%,d", (Principal + Interest + BreachAmt - UnPaidAmt)), "R");
//sum = sum + (Principal + Interest + BreachAmt - UnPaidAmt);
///**********************/
//
//this.print(0, 156, tL9Vo.get("F10"));
//
//this.print(0, 160, tL9Vo.get("F11"));
//this.print(0, 172, tL9Vo.get("F13"));
//this.print(1, 1, "　　　　 　　　　　 　　　　　　　其他電話： ");
//if (tL9Vo.get("F12") != null) {
//	this.print(0, 37, tL9Vo.get("F12"));
//} else {
////		this.print(0, 42, "*");
//}
}
