package com.st1.itx.trade.L9;

import java.util.ArrayList;
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
import com.st1.itx.util.parse.Parse;

@Component("L9710Report2")
@Scope("prototype")

public class L9710Report2 extends MakeReport {
	// private static final Logger logger =
	// LoggerFactory.getLogger(L9710Report2.class);

	@Autowired
	BaTxCom dBaTxCom;

	@Autowired
	Parse parse;

	String enydy = "";

	@Override
	public void printHeader() {

		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);

	}

	public void exec(TitaVo titaVo, TxBuffer txbuffer, List<Map<String, String>> l9710List) throws LogicException {

		this.info("L9710Report2 exec");
		enydy = String.valueOf(Integer.parseInt(titaVo.getParam("ENTDY")));
		String f4 = "";
		String f5 = "";

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9710", "放款本息攤還表暨繳息通知單", "密", "A4", "P");
		this.info("l9710List-------->" + l9710List.size());
		if (l9710List != null && l9710List.size() != 0) {
			for (Map<String, String> tL9710Vo : l9710List) {
				this.info("LOOP-------->" + tL9710Vo.get("F4") + "," + tL9710Vo.get("F5"));
				if (!f4.equals(tL9710Vo.get("F4")) || !f5.equals(tL9710Vo.get("F5"))) {
					report(tL9710Vo, txbuffer);
				}
				f4 = tL9710Vo.get("F4");
				f5 = tL9710Vo.get("F5");
			}
		} else {
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 10, "【限定本人拆閱，若無此人，請寄回本公司】");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 45, "放款本息攤還表暨繳息通知單", "C");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 10, "製發日期：");
			this.print(0, 20, enydy.substring(0, 3) + "/" + enydy.substring(3, 5) + "/" + enydy.substring(5, 7));
			this.print(0, 87, "");

			this.print(1, 10, "戶號：　　　　　　　目前利率：　　　　%");
			this.print(1, 10, "客戶名稱：　　　　　　　　　　　　　　　　　　　　　溢短繳：");
			this.print(1, 10, "　　　　　　　　　　　　　　　　　　　　　　　　　　管帳費：");
			this.print(1, 10, "　　　　　　　　　　　　　每期應攤還　　　　　　　　　　　　未　　還　　暫　付");
			this.print(1, 1, "");
			this.print(1, 10, "應繳日　　違約金　　　　本金　　　　　利息　　　應繳合計　　本金餘額　　所得稅　　應繳淨額");
			this.print(1, 7, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
			this.print(1, 7, "本日無資料");
			this.print(1, 1, "");
			this.print(2, 8, "＊＊舊繳息通知單作廢（以最新製發日期為準）。");
			this.print(1, 8, "＊＊本額度自　　　年　　月　　日起本利均攤");
			this.print(1, 8, "＊＊新光銀行城內分行代號： 1030116");
			this.print(1, 86, "製表人 ");
		}

		long sno = this.close();

		// 測試用
		this.toPdf(sno);
	}

	private void report(Map<String, String> tL9710Vo, TxBuffer txbuffer) throws LogicException {
		List<BaTxVo> listBaTxVo = new ArrayList<>();
		try {
			dBaTxCom.setTxBuffer(txbuffer);
			listBaTxVo = dBaTxCom.termsPay(parse.stringToInteger(titaVo.getParam("ENTDY")), parse.stringToInteger(tL9710Vo.get("F4")), parse.stringToInteger(tL9710Vo.get("F5")), 0, 6, titaVo);
			this.info("listBaTxVo.size()-------->" + listBaTxVo.size());
			this.info("listBaTxVo-------->" + listBaTxVo.toString());

		} catch (LogicException e) {
			this.info("baTxCom.setTxBuffer ErrorMsg :" + e.getMessage());
		}

		if (listBaTxVo.size() > 0) {
			int Principal = 0;
			int Interest = 0;
			int BreachAmt = 0;
			int UnPaidAmt = 0;
			int LoanBal = 0;
			double IntRate = 0.00;
			// 未收本息 = 本金+利息 Principal + Interest
			// 違約金 有 但要扣成 0 BreachAmt
			// 溢短繳 UnPaidAmt
			// 合計 = 未收本息 + 違約金 - 溢短繳

			UnPaidAmt = UnPaidAmt + listBaTxVo.get(0).getUnPaidAmt().intValue();
			IntRate = listBaTxVo.get(0).getIntRate().doubleValue();

			if (this.getNowPage() > 0) {
				newPage();
			}

			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 10, "【限定本人拆閱，若無此人，請寄回本公司】");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 10, tranNum(tL9710Vo.get("F17")) + tranNum(tL9710Vo.get("F18")));
			this.print(1, 1, "");
			String tmp = "";
			if (tL9710Vo.get("F19").length() > 0) {
				tmp = tmp + tL9710Vo.get("F19");
			}
			if (tL9710Vo.get("F20").length() > 0) {
				tmp = tmp + tL9710Vo.get("F20");
			}
			if (tL9710Vo.get("F21").length() > 0) {
				if (tL9710Vo.get("F21").substring(tL9710Vo.get("F21").length() - 1) == "路") {
					tmp = tmp + tL9710Vo.get("F21");
				} else if (tL9710Vo.get("F21").substring(tL9710Vo.get("F21").length() - 1) == "街") {
					tmp = tmp + tL9710Vo.get("F21");
				}
			}
			if (tL9710Vo.get("F22").length() > 0) {
				tmp = tmp + tL9710Vo.get("F22") + "段";
			}
			if (tL9710Vo.get("F23").length() > 0) {
				tmp = tmp + tL9710Vo.get("F23") + "巷";
			}
			if (tL9710Vo.get("F24").length() > 0) {
				tmp = tmp + tL9710Vo.get("F24") + "弄";
			}
			if (tL9710Vo.get("F25").length() > 0) {
				tmp = tmp + tL9710Vo.get("F25") + "號";
			}
			if (tL9710Vo.get("F26").length() > 0) {
				tmp = tmp + "之" + tL9710Vo.get("F26");
			}
			if (tL9710Vo.get("F27").length() > 0) {
				tmp = tmp + tL9710Vo.get("F27") + "樓";
			}
			if (tL9710Vo.get("F28").length() > 0) {
				tmp = tmp + "之" + tL9710Vo.get("F28");
			}
			this.print(1, 10, tmp);
			this.print(1, 1, "");
			this.print(1, 10, String.format("%07d", Integer.valueOf(tL9710Vo.get("F4"))) + "   " + tL9710Vo.get("F6"));
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");

			// this.setFontSize(14);
			this.print(1, 45, "放款本息攤還表暨繳息通知單", "C");

			// this.setFontSize(10);
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 10, "製發日期：");
			this.print(0, 20, enydy.substring(0, 3) + "/" + enydy.substring(3, 5) + "/" + enydy.substring(5, 7));

			tmp = "";
			switch (tL9710Vo.get("F29")) {
			case "1":
				tmp = "匯款轉帳";
				break;
			case "2":
				tmp = "銀行扣款";
				break;
			case "3":
				tmp = "員工扣薪";
				break;
			case "4":
				tmp = "支票";
				break;
			case "5":
				tmp = "特約金";
				break;
			case "6":
				tmp = "人事特約金";
				break;
			case "7":
				tmp = "定存特約";
				break;
			case "8":
				tmp = "劃撥存款";
				break;
			}
			this.print(0, 87, tmp);

			this.print(1, 10, "戶號：　　　　　　　目前利率：　　　　%");
			this.print(0, 16, String.format("%07d", Integer.valueOf(tL9710Vo.get("F4"))) + "-" + String.format("%03d", Integer.valueOf(tL9710Vo.get("F5"))));
			this.print(0, 47, String.format("%.4f", IntRate), "R");
			this.print(1, 10, "客戶名稱：　　　　　　　　　　　　　　　　　　　　　溢短繳：");
			this.print(0, 20, tL9710Vo.get("F6"));
			this.print(0, 78, String.format("%,d", UnPaidAmt), "R");
			this.print(1, 10, "　　　　　　　　　　　　　　　　　　　　　　　　　　管帳費：");
			this.print(1, 10, "　　　　　　　　　　　　　每期應攤還　　　　　　　　　　　　未　　還　　暫　付");
			this.print(1, 10, "應繳日　　違約金　　　　本金　　　　　利息　　　應繳合計　　本金餘額　　所得稅　　應繳淨額");
			this.print(1, 7, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
			String TempDate = String.valueOf(listBaTxVo.get(0).getPayIntDate());

			for (int i = 0; i < listBaTxVo.size(); i++) {
				String PayIntDate = String.valueOf(listBaTxVo.get(i).getPayIntDate());
				if (PayIntDate.equals(TempDate) && i != listBaTxVo.size() - 1) {
					// 正常
					// 違約金
					// 本金
					// 利息
					// 未還本金餘額
					BreachAmt = BreachAmt + listBaTxVo.get(i).getBreachAmt().intValue();
					Principal = Principal + listBaTxVo.get(i).getPrincipal().intValue();
					Interest = Interest + listBaTxVo.get(i).getInterest().intValue();
					LoanBal = LoanBal + listBaTxVo.get(i).getLoanBal().intValue();
				} else if (i == listBaTxVo.size() - 1) { // 最後一筆
					BreachAmt = BreachAmt + listBaTxVo.get(i).getBreachAmt().intValue();
					Principal = Principal + listBaTxVo.get(i).getPrincipal().intValue();
					Interest = Interest + listBaTxVo.get(i).getInterest().intValue();
					LoanBal = LoanBal + listBaTxVo.get(i).getLoanBal().intValue();
					if (!"0".equals(TempDate)) {
						this.print(1, 7, TempDate.substring(0, 3) + "/" + TempDate.substring(3, 5) + "/" + TempDate.substring(5, 7));
					}
					this.print(0, 25, String.format("%,d", BreachAmt), "R");
					this.print(0, 38, String.format("%,d", Principal), "R");
					this.print(0, 52, String.format("%,d", Interest), "R");
					this.print(0, 65, String.format("%,d", (BreachAmt + Principal + Interest)), "R");
					this.print(0, 78, String.format("%,d", LoanBal), "R");
					this.print(0, 87, "0", "R");
					this.print(0, 100, String.format("%,d", (BreachAmt + Principal + Interest)), "R");

				} else { // 當筆日期與之前不同
					if (!"0".equals(TempDate)) {
						this.print(1, 7, TempDate.substring(0, 3) + "/" + TempDate.substring(3, 5) + "/" + TempDate.substring(5, 7));
					}
					this.print(0, 25, String.format("%,d", BreachAmt), "R");
					this.print(0, 38, String.format("%,d", Principal), "R");
					this.print(0, 52, String.format("%,d", Interest), "R");
					this.print(0, 65, String.format("%,d", (BreachAmt + Principal + Interest)), "R");
					this.print(0, 78, String.format("%,d", LoanBal), "R");
					this.print(0, 87, "0", "R");
					this.print(0, 100, String.format("%,d", (BreachAmt + Principal + Interest)), "R");

					BreachAmt = 0;
					Principal = 0;
					Interest = 0;
					LoanBal = 0;
					BreachAmt = BreachAmt + listBaTxVo.get(i).getBreachAmt().intValue();
					Principal = Principal + listBaTxVo.get(i).getPrincipal().intValue();
					Interest = Interest + listBaTxVo.get(i).getInterest().intValue();
					LoanBal = LoanBal + listBaTxVo.get(i).getLoanBal().intValue();

					TempDate = String.valueOf(listBaTxVo.get(i).getPayIntDate());
				}
			}
			this.print(2, 8, "＊＊舊繳息通知單作廢（以最新製發日期為準）。");
			if (tL9710Vo.get("F8").equals("0")) {
				this.print(1, 8, "＊＊本額度自　　　年　　月　　日起本利均攤");
			} else {
				this.print(1, 8, "＊＊本額度自 " + showRocDate(tL9710Vo.get("F8"), 2) + "起本利均攤");
			}
			this.print(1, 8, "＊＊新光銀行城內分行代號： 1030116");
			tmp = tL9710Vo.get("F3");
			if (tmp == null) {
				tmp = "";
			}
			this.print(1, 86, "製表人 " + tmp);
		}
	}

	private String tranNum(String data) {
		this.info("tranDate1 = " + data);
		if (data == null || data.equals("")) {
			return "";
		}
		String tmp1 = data;
		this.info("tranData = " + tmp1);
		String tmp2 = "";

		int tranTemp = 0;
		int i = 0;
		char tmp;

		for (i = 0; i < tmp1.length(); i++) {

			this.info("tranDate i = " + i);
			this.info("tranDate X = " + tmp1.substring(i, i + 1));
			tmp = tmp1.charAt(i);

			tranTemp = tmp;

			tranTemp += 65248; // 此數字是 Unicode編碼轉為十進位 和 ASCII碼的 差

			this.info("tranDate XX= " + (char) tranTemp);
			tmp2 += (char) tranTemp;
		}

		return tmp2;
	}
}
