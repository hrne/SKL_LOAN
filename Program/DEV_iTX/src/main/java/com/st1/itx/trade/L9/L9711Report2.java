package com.st1.itx.trade.L9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L9711Report2 extends MakeReport {

//	L9711ServiceImpl L9711ServiceImlㄤ‧
	@Autowired
	DateUtil dDateUtil;

	@Autowired
	BaTxCom dBaTxCom;

	@Autowired
	public Parse parse;

	@Autowired
	public TxBuffer txBuffer;

	String ENTDY = "";

	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");
		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(60);

	}

	public void exec(TitaVo titaVo, TxBuffer txbuffer, List<Map<String, String>> L9711List) throws LogicException {

		this.info("L9711Report2 exec");
		ENTDY = String.valueOf(Integer.parseInt(titaVo.getParam("ENTDY").toString()));
		String f4 = "";
		String f5 = "";
		int count = 0;
		// 製表人
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9711", "放款本息攤還表暨繳息通知單", "密", "A4", "P");

		if (L9711List.size() > 0) {

			for (Map<String, String> tL9711Vo : L9711List) {
				// 有一樣戶號額度的話 用同一張
				if (f4.equals(tL9711Vo.get("F4")) && f5.equals(tL9711Vo.get("F5"))) {

					report(tL9711Vo, txbuffer);

				} else {
					if (count != 0) {
						this.newPage();

					}
					report(tL9711Vo, txbuffer);

					count++;

				}
				f4 = tL9711Vo.get("F4");
				f5 = tL9711Vo.get("F5");

			}
		} else {
			this.info("L9711List.reportEmpty");
			reportEmpty();
		}

		long sno = this.close();

		// this.toPdf(sno);
	}

	private void reportEmpty() throws LogicException {

		this.print(-4, 10, "【限定本人拆閱，若無此人，請寄回本公司】");

		this.setFontSize(14);
		this.print(-15, 37, "放款本息攤還表暨繳息通知單", "C");

		this.setFontSize(10);

		this.print(-22, 10, "製發日期：");
		this.print(-22, 20, ENTDY.substring(0, 3) + "/" + ENTDY.substring(3, 5) + "/" + ENTDY.substring(5, 7));
		this.print(-23, 10, "戶號：　　　　　　　目前利率：　　　　%");

		this.print(-24, 10, "客戶名稱：　　　　　　　　　　　　　　　　　　　　　溢短繳：");

		this.print(-25, 10, "　　　　　　　　　　　　　　　　　　　　　　　　　　管帳費：");

		this.print(-26, 10, "　　　　　　　　　　　　　每期應攤還　　　　　　　　　　　　未　　還　　暫　付");
		this.print(-27, 10, "應繳日　　違約金　　　　本金　　　　　利息　　　應繳合計　　本金餘額　　所得稅　　應繳淨額");
		this.print(-28, 7, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		this.print(-29, 7, "本日無資料");

		this.print(-31, 8, "＊＊舊繳息通知單作廢（以最新製發日期為準）。");
		this.print(-32, 8, "＊＊貴戶所借款項如業已屆期，本公司雖經收取利息及違約金但並無同意延期清償之意 , 貴戶仍需依約履行");

		this.print(-33, 8, "＊＊本額度自　　　年　　月　　日起本利均攤");
		this.print(-34, 8, "　　貴戶所貸上列款項。於　　　年　　月　　日到期，請依約到本公司辦理清償或展期手續，請勿延誤");

		this.print(-35, 8, "＊＊新光銀行　　分行代號：");

		this.print(-36, 82, "製表人 ");
		this.print(1, 1, " ");

	}

	private void report(Map<String, String> tL9711Vo, TxBuffer txbuffer) throws LogicException {
		List<BaTxVo> listBaTxVo = new ArrayList<>();
		try {
			dBaTxCom.setTxBuffer(txbuffer);
			listBaTxVo = dBaTxCom.termsPay(parse.stringToInteger(titaVo.getParam("ENTDY")),
					parse.stringToInteger(tL9711Vo.get("F4")), parse.stringToInteger(tL9711Vo.get("F5")), 0, 6, titaVo);

		} catch (LogicException e) {
			this.info("baTxCom.setTxBuffer ErrorMsg :" + e.getMessage());
		}

		this.info("listBaTxVo.size()-------->" + listBaTxVo.size());
		this.info("listBaTxVo-------->" + listBaTxVo.toString());

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

		this.print(1, 1, " ");
		this.print(-4, 10, "【限定本人拆閱，若無此人，請寄回本公司】");

		this.print(-7, 10, tranNum(tL9711Vo.get("F17")) + tranNum(tL9711Vo.get("F18")));

		String tmp = "";

		this.print(-9, 10, tL9711Vo.get("F19"));

		this.print(-11, 10, String.format("%07d", Integer.valueOf(tL9711Vo.get("F4"))) + "   " + tL9711Vo.get("F6"));

		this.setFontSize(14);
		this.print(-15, 37, "放款本息攤還表暨繳息通知單", "C");

		this.setFontSize(10);

		this.print(-22, 10, "製發日期：");
		this.print(-22, 20, ENTDY.substring(0, 3) + "/" + ENTDY.substring(3, 5) + "/" + ENTDY.substring(5, 7));

		this.print(-22, 83, tL9711Vo.get("F20"));

		this.print(-23, 10, "戶號：　　　　　　　目前利率：　　　　%");
		this.print(-23, 16, String.format("%07d", Integer.valueOf(tL9711Vo.get("F4"))) + "-"
				+ String.format("%03d", Integer.valueOf(tL9711Vo.get("F5"))));
		this.print(-23, 47, String.format("%.4f", IntRate), "R");
		this.print(-24, 10, "客戶名稱：　　　　　　　　　　　　　　　　　　　　　溢短繳：");
		this.print(-24, 20, tL9711Vo.get("F6"));
		this.print(-24, 70, String.format("%,d", UnPaidAmt));
		this.print(-25, 10, "　　　　　　　　　　　　　　　　　　　　　　　　　　管帳費：");
		this.print(-25, 78, "");
		this.print(-26, 10, "　　　　　　　　　　　　　每期應攤還　　　　　　　　　　　　未　　還　　暫　付");
		this.print(-27, 10, "應繳日　　違約金　　　　本金　　　　　利息　　　應繳合計　　本金餘額　　所得稅　　應繳淨額");
		this.print(-28, 7, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

		String tempDate = "";

		int dataRow = -29;

		if (listBaTxVo.size() > 0) {
			List<Map<String, Integer>> tempList = new ArrayList<Map<String, Integer>>();

			Map<String, Integer> tempMap = new HashMap<String, Integer>();

			String payIntDate = "";
			int a = 1;
			for (int i = 0; i < listBaTxVo.size(); i++) {

				this.info("No." + i);
				this.info("getCustNo=" + listBaTxVo.get(i).getCustNo());
				this.info("getFacmNo=" + listBaTxVo.get(i).getFacmNo());
				this.info("getPayIntDate=" + listBaTxVo.get(i).getPayIntDate());
				this.info("getBreachAmt=" + listBaTxVo.get(i).getBreachAmt());
				this.info("getPrincipal=" + listBaTxVo.get(i).getPrincipal());
				this.info("getInterest=" + listBaTxVo.get(i).getInterest());
				this.info("getLoanBal=" + listBaTxVo.get(i).getLoanBal());

				payIntDate = String.valueOf(listBaTxVo.get(i).getPayIntDate());
				if (payIntDate.equals("0")) {
					payIntDate = "0000000";
				}

				if (!payIntDate.equals(tempDate)) {

					// 防第一次沒資料就加入
					if (i > 0 && !payIntDate.equals(tempDate)) {

						tempList.add(tempMap);

						tempMap = new HashMap<String, Integer>();

//						this.info(a + " count join list");
//						a++;

						BreachAmt = 0;
						Principal = 0;
						Interest = 0;
						LoanBal = 0;
					}
					tempDate = payIntDate;

					// 違約金
					BreachAmt = listBaTxVo.get(i).getBreachAmt().intValue();

					// 本金
					Principal = listBaTxVo.get(i).getPrincipal().intValue();

					// 利息
					Interest = listBaTxVo.get(i).getInterest().intValue();

					// 未還本金餘額
					LoanBal = listBaTxVo.get(i).getLoanBal().intValue();

					tempMap.put("pday", Integer.valueOf(payIntDate));
					tempMap.put("bAmt", BreachAmt);
					tempMap.put("pAmt", Principal);
					tempMap.put("iAmt", Interest);
					tempMap.put("lAmt", LoanBal);

				} else {

					this.info("isRepeat" + a);

					// 違約金
					BreachAmt = BreachAmt + listBaTxVo.get(i).getBreachAmt().intValue();

					// 本金
					Principal = Principal + listBaTxVo.get(i).getPrincipal().intValue();

					// 利息
					Interest = Interest + listBaTxVo.get(i).getInterest().intValue();

					// 未還本金餘額
					LoanBal = LoanBal + listBaTxVo.get(i).getLoanBal().intValue();

					tempMap.put("bAmt", BreachAmt);
					tempMap.put("pAmt", Principal);
					tempMap.put("iAmt", Interest);
					tempMap.put("lAmt", LoanBal);

				}

				// 最後一筆需加入
				if (listBaTxVo.size() - 1 == i) {

					tempList.add(tempMap);
				}
			}

			for (Map<String, Integer> listData : tempList) {

				this.info("date=" + listData.get("pday").toString());
				tempDate = listData.get("pday").toString();
				// 應繳日
				this.print(dataRow, 7,
						tempDate.substring(0, 3) + "/" + tempDate.substring(3, 5) + "/" + tempDate.substring(5, 7));
				// 違約金
				this.print(dataRow, 26, String.format("%,d", listData.get("bAmt")), "R");
				// 本金
				this.print(dataRow, 38, String.format("%,d", listData.get("pAmt")), "R");
				// 利息
				this.print(dataRow, 52, String.format("%,d", listData.get("iAmt")), "R");
				// 應繳合計
				this.print(dataRow, 66,
						String.format("%,d", (listData.get("bAmt") + listData.get("pAmt") + listData.get("iAmt"))),
						"R");
				// 未還 本金餘額
				this.print(dataRow, 78, String.format("%,d", listData.get("lAmt")), "R");
				// 暫付 所得稅
				this.print(dataRow, 88, "0", "R");
				// 應繳淨額
				this.print(dataRow, 100,
						String.format("%,d", (listData.get("bAmt") + listData.get("pAmt") + listData.get("iAmt"))),
						"R");

				dataRow--;
			}

		}

		dataRow--;

		this.print(dataRow--, 8, "＊＊舊繳息通知單作廢（以最新製發日期為準）。");
		this.print(dataRow--, 8, "＊＊貴戶所借款項如業已屆期，本公司雖經收取利息及違約金但並無同意延期清償之意 , 貴戶仍需依約履行");
		if (tL9711Vo.get("F8").equals("0")) {
			this.print(dataRow--, 8, "＊＊本額度自　　　年　　月　　日起本利均攤");
			this.print(dataRow--, 8, "　　貴戶所貸上列款項。於　　　年　　月　　日到期，請依約到本公司辦理清償或展期手續，請勿延誤");
		} else {
			this.print(dataRow--, 8, "　　貴戶所貸上列款項。於　" + showDate(tL9711Vo.get("F8"), 2) + "到期，請依約到本公司辦理清償或展期手續，請勿延誤");
		}

		// SQL尚缺分行代號
		this.print(dataRow--, 8, "＊＊新光銀行城內分行代號： 1030116");
		tmp = tL9711Vo.get("F3");
		if (tmp == null) {
			tmp = "";
		}

		this.print(dataRow--, 82, "製表人 " + tmp);

		this.newPage();

	}

	// 顯示民國年
	private String showDate(String date, int iType) {
		this.info("MakeReport.toPdf showRocDate1 = " + date);
		if (date == null || date.equals("") || date.equals("0")) {
			return "";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);
		this.info("MakeReport.toPdf showRocDate2 = " + rocdatex);
		if (iType == 1) {
			if (rocdatex.length() == 6) {
				return rocdatex.substring(0, 2) + "/" + rocdatex.substring(2, 4) + "/" + rocdatex.substring(4, 6);
			} else {
				return rocdatex.substring(0, 3) + "/" + rocdatex.substring(3, 5) + "/" + rocdatex.substring(5, 7);
			}
		} else if (iType == 2) {
			if (rocdatex.length() == 6) {
				return rocdatex.substring(0, 2) + " 年 " + rocdatex.substring(2, 4) + " 月 " + rocdatex.substring(4, 6)
						+ " 日";
			} else {
				return rocdatex.substring(0, 3) + " 年 " + rocdatex.substring(3, 5) + " 月 " + rocdatex.substring(5, 7)
						+ " 日";
			}
		} else {
			return rocdatex;
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

			tranTemp = (int) tmp;

			tranTemp += 65248; // 此數字是 Unicode編碼轉為十進位 和 ASCII碼的 差

			this.info("tranDate XX= " + (char) tranTemp);
			tmp2 += (char) tranTemp;
		}

		return tmp2;
	}
}

//if (PayIntDate.equals(tempDate) && i != listBaTxVo.size() - 1) {
//
//	// 正常
//	// 違約金
//	// 本金
//	// 利息
//	// 未還本金餘額
//	BreachAmt = BreachAmt + listBaTxVo.get(i).getBreachAmt().intValue();
//	Principal = Principal + listBaTxVo.get(i).getPrincipal().intValue();
//	Interest = Interest + listBaTxVo.get(i).getInterest().intValue();
//	LoanBal = LoanBal + listBaTxVo.get(i).getLoanBal().intValue();
//
//} else if (i == listBaTxVo.size() - 1) { // 最後一筆
//	
//	BreachAmt = BreachAmt + listBaTxVo.get(i).getBreachAmt().intValue();
//	Principal = Principal + listBaTxVo.get(i).getPrincipal().intValue();
//	Interest = Interest + listBaTxVo.get(i).getInterest().intValue();
//	LoanBal = LoanBal + listBaTxVo.get(i).getLoanBal().intValue();
//	// 應繳日
////	if (!PayIntDate.equals(tempDate)) {
//		this.print(dataRow, 7, tempDate.substring(0, 3) + "/" + tempDate.substring(3, 5) + "/"
//				+ tempDate.substring(5, 7));
////	}
//	// 違約金
//	this.print(dataRow, 26, String.format("%,d", BreachAmt), "R");
//	// 本金
//	this.print(dataRow, 38, String.format("%,d", Principal), "R");
//	// 利息
//	this.print(dataRow, 52, String.format("%,d", Interest), "R");
//	// 應繳合計
//	this.print(dataRow, 66, String.format("%,d", (BreachAmt + Principal + Interest)), "R");
//	// 未還 本金餘額
//	this.print(dataRow, 78, String.format("%,d", LoanBal), "R");
//	// 暫付 所得稅
//	this.print(dataRow, 88, "0", "R");
//	// 應繳淨額
//	this.print(dataRow, 100, String.format("%,d", (BreachAmt + Principal + Interest)), "R");
//
//} else { // 當筆日期與之前不同
//	
//	tempDate = PayIntDate;
//	
//	BreachAmt = 0;
//	Principal = 0;
//	Interest = 0;
//	LoanBal = 0;
//	
//	BreachAmt = BreachAmt + listBaTxVo.get(i).getBreachAmt().intValue();
//	Principal = Principal + listBaTxVo.get(i).getPrincipal().intValue();
//	Interest = Interest + listBaTxVo.get(i).getInterest().intValue();
//	LoanBal = LoanBal + listBaTxVo.get(i).getLoanBal().intValue();
//	
//	// 應繳日
//	if (!PayIntDate.equals(tempDate)) {
//		this.print(dataRow, 7, tempDate.substring(0, 3) + "/" + tempDate.substring(3, 5) + "/"
//				+ tempDate.substring(5, 7));
//	}
//	// 違約金
//	this.print(dataRow, 26, String.format("%,d", BreachAmt), "R");
//	// 本金
//	this.print(dataRow, 38, String.format("%,d", Principal), "R");
//	// 利息
//	this.print(dataRow, 52, String.format("%,d", Interest), "R");
//	// 應繳合計
//	this.print(dataRow, 66, String.format("%,d", (BreachAmt + Principal + Interest)), "R");
//	// 未還 本金餘額
//	this.print(dataRow, 78, String.format("%,d", LoanBal), "R");
//	// 暫付 所得稅
//	this.print(dataRow, 88, "0", "R");
//	// 應繳淨額
//	this.print(dataRow, 100, String.format("%,d", (BreachAmt + Principal + Interest)), "R");
//
//	
//
//
//
//}