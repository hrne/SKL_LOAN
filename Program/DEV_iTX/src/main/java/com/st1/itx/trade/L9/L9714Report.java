package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9714ServiceImpl;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class L9714Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(L9714Report.class);

	@Autowired
	L9714ServiceImpl l9714ServiceImpl;

	String f0 = "";
	String f1 = "";
	String f2 = "";
	String f3 = "";
	String f4 = "";
	String f5 = "";
	String f6 = "";
	String f7 = "";
	String f8 = "";
	String f9 = "";
	String f10 = "";
	String isday = "";
	String ieday = "";
	int cnt = 0;

	int colCount = 8;

	@Override
	public void printHeader() {
		this.info("printHeader-----------");
		// this.setFontSize(13);
		this.setCharSpaces(0);

		if (f0.equals("")) {
			return;
		}

		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(80);
		// 明細起始列(自訂亦必須)
//		this.setBeginRow(31);

		this.print(-4, colCount + 1, "新　光　人　壽　保　險　股　份　有　限　公　司");
		this.print(-6, colCount + 6, "房　屋　擔　保　借　款　繳　息　清　單");
		this.print(-7, colCount + 6, "￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣");

		this.print(-2, colCount + 67, "  機密等級：密");
		this.print(-3, colCount + 67, "┌─┬─────────────┐");
		this.print(-4, colCount + 67, "│　│房屋擔保借款繳息證明專用章│");
		this.print(-5, colCount + 67, "│戳├─────────────┤");
		this.print(-6, colCount + 67, "│　│  ０３４５８９０２        │");
		this.print(-7, colCount + 67, "│記├─────────────┤");
		this.print(-8, colCount + 67, "│　│新光人壽保險股份有限公司　│");
		this.print(-9, colCount + 67, "│欄├─────────────┤");
		this.print(-10, colCount + 67, "│　│台北市忠孝西路一段６６號　│");
		this.print(-11, colCount + 67, "└─┴─────────────┘");

		this.print(-13, colCount + 1, "┌────┬──────┬────────┬───────────────────────────┐");
		this.print(-14, colCount + 1,
				"│借戶姓名│            │房屋所有權人姓名│※                                                    │");
		this.print(-15, colCount + 1, "├────┼──────┼────────┼───────────────────────────┤");
		this.print(-16, colCount + 1,
				"│統一編號│            │統一編號        │※                                                    │");
		this.print(-17, colCount + 1, "├────┼──────┼────────┼───────────────────────────┤");
		this.print(-18, colCount + 1,
				"│房屋稅籍│※          │房屋坐落        │※                                                    │");
		this.print(-19, colCount + 1, "└────┴──────┴────────┴───────────────────────────┘");

		this.print(-21, colCount + 1, "┌──────┬───────┬─────────┬──────┬──────┬─────────┐");
		this.print(-22, colCount + 1,
				"│房屋所有權　│              │                  │            │            │      本期未償還　│");
		this.print(-23, colCount + 1, "│取得日※    │ 貸款帳號     │     最初貸款金額 │   貸款起日 │   貸款迄日 │      本金額（元）│");
		this.print(-24, colCount + 1, "├──────┼───────┼─────────┼──────┼──────┼─────────┤");
		this.print(-25, colCount + 1,
				"│            │              │                  │            │            │                  │");
		this.print(-26, colCount + 1, "└──────┴───────┴─────────┴──────┴──────┴─────────┘");

		this.print(-28, colCount + 1, "┌──────────────┬────────────────┬────────────────┐");
		this.print(-29, colCount + 1,
				"│繳息所屬年月                │                      繳息金額　│                    用途別      │");
		this.print(-30, colCount + 1, "├──────────────┼────────────────┼────────────────┤");
		this.print(-31, colCount + 1,
				"│自                          │                                │                                │");
		this.print(-32, colCount + 1,
				"│至                          │                                │                                │");

		String tmp;
//		this.info("f0=" + f0 + ",f1=" + f1 + ",f2=" + f2 + ",f3=" + f3 + ",f4=" + f4 + ",f5=" + f5 + ",f6=" + f6
//				+ ",f7=" + f7);

		tmp = f0;
		if (tmp.length() > 5) {
			tmp = tmp.substring(0, 5);
		}
		this.print(-14, colCount + 14, tmp);
		this.print(-16, colCount + 14, f1);

		tmp = String.format("%07d", Integer.valueOf(f2)) + "-" + String.format("%03d", Integer.valueOf(f3));
		this.print(-25, colCount + 18, tmp);

		this.print(-25, colCount + 50, showAmt(f4), "R");
		this.print(-25, colCount + 64, showRocDate(f5, 1), "R");
		this.print(-25, colCount + 78, showRocDate(f6, 1), "R");
		this.print(-25, colCount + 98, showAmt(f7), "R");

		if (ieday.length() == 6) {
			this.print(-32, colCount + 7, ieday.substring(0, 2) + " 年 " + ieday.substring(2, 4) + " 月");
		} else {

			this.print(-32, colCount + 6, ieday.substring(0, 3) + " 年 " + ieday.substring(3, 5) + " 月");
		}

	}

	public boolean exec(TitaVo titaVo) throws LogicException {

		this.info("L9714Report exec");

		isday = titaVo.get("ACCTDATE_ST");
		ieday = titaVo.get("ACCTDATE_ED");

		List<Map<String, String>> l9714List = null;
		

		try {

			l9714List = l9714ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9714ServiceImpl.LoanBorTx error = " + errors.toString());
		}

//		if (l9714List == null || l9714List.isEmpty()) {
//			return false;
//		}

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9714", "繳息證明單", "", "A4", "P");


		if (l9714List.size() > 0) {
			for (Map<String, String> tL9714Vo : l9714List) {
			
				if (!tL9714Vo.get("F3").equals(f3)) {
//					if (this.getNowPage() > 0) {
//						printEnd();
//					}
					f0 = tL9714Vo.get("F0");
					f1 = tL9714Vo.get("F1");
					f2 = tL9714Vo.get("F2");
					f3 = tL9714Vo.get("F3");
					f4 = tL9714Vo.get("F4");
					f5 = tL9714Vo.get("F5");
					f6 = tL9714Vo.get("F6");
					f7 = tL9714Vo.get("F7");
					this.newPage();
					cnt = 0;

				}
				report1(tL9714Vo);
				printEnd();
			}
		} else {

	
			f0 = "本日無資料";
			f1 = " ";
			f2 = "0";
			f3 = "0";
			f4 = "0";
			f5 = "0";
			f6 = "0";
			f7 = "0";
			printHeader();
			
			printEnd();
		}

	

//		if (this.getNowPage() > 0) {
//			printEnd();
//		}
		// 為了不跑無資料
//		drawLine(0, 0, 0, 0);
		long sno = this.close();

		// 測試用
		this.toPdf(sno);
		if (l9714List.size() > 0) {
			return true;
		} else {
			return false;

		}
	}

	private void printEnd() {
//		if (cnt == 1) {
//			this.print(-31, colCount + 1,
//					"│至                          │                                │                                │");
//			if (ieday.length() == 6) {
//				this.print(-32, colCount + 7, ieday.substring(0, 2) + " 年 " + ieday.substring(2, 4) + " 月");
//			} else {
//				this.print(-32, colCount + 6, ieday.substring(0, 3) + " 年 " + ieday.substring(3, 5) + " 月");
//			}
//		}
		this.print(-33, colCount + 1, "└──────────────┴────────────────┴────────────────┘");
		this.print(-34, colCount + 1, " ( 註１：有※註記的欄位，由借戶自行填寫 )");
		this.print(-35, colCount + 1, " ( 註２：申報所得稅─非自用住宅借款利息，不得列舉 )");
	}

	private void report1(Map<String, String> tL9714Vo) {
		String tmp = "";

		cnt += 1;
		switch (tL9714Vo.get("F8")) {
		case "01":
			tmp = "週轉金";
			break;
		case "02":
			tmp = "購置不動產";
			break;
		case "03":
			tmp = "營業用資產";
			break;
		case "04":
			tmp = "固定資產";
			break;
		case "05":
			tmp = "企業投資";
			break;
		case "06":
			tmp = "購置動產";
			break;
		case "09":
			tmp = "其他";
			break;
		default:
			break;
		}

		if (cnt == 1) {
//			this.print(-31, colCount + 1,
//					"│自                          │                                │                                │");
			if (isday.length() == 6) {
				this.print(-31, colCount + 7, isday.substring(0, 2) + " 年 " + isday.substring(2, 4) + " 月");
			} else {

				this.print(-31, colCount + 6, isday.substring(0, 3) + " 年 " + isday.substring(3, 5) + " 月");
			}
		} else if (cnt == 2) {
//			this.print(-32, colCount + 1,
//					"│至                          │                                │                                │");
			if (ieday.length() == 6) {
				this.print(-32, colCount + 7, ieday.substring(0, 2) + " 年 " + ieday.substring(2, 4) + " 月");
			} else {
				this.print(-32, colCount + 6, ieday.substring(0, 3) + " 年 " + ieday.substring(3, 5) + " 月");
			}
		} else {
			this.print(1, colCount + 1,
					"│                            │                                │                                │");
		}
		this.print(0, colCount + 63, showAmt(tL9714Vo.get("F9")), "R");
		this.print(0, colCount + 87, tmp);
	}

	private String showAmt(String xamt) {
		if (xamt == null || xamt.equals("") || xamt.equals("0")) {
			return "";
		}
		int amt = Integer.valueOf(xamt);
		return String.format("%,d", amt);
	}

}
