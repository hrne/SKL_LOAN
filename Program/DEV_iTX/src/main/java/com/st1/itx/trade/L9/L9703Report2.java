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
import com.st1.itx.db.service.springjpa.cm.L9703ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * L9703Report2
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L9703Report2 extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(L9703Report2.class);

	@Autowired
	L9703ServiceImpl l9703ServiceImpl;

	@Autowired
	BaTxCom dBaTxCom;

	@Autowired
	Parse parse;

	String entdy = "";

	// 自訂表頭
//	@Override
//	public void printHeader() {
//		
//		
//		//明細起始列(自訂亦必須)
//		this.setBeginRow(10);
//		
//		//設定明細列數(自訂亦必須)
//		this.setMaxRows(35);
//	}

	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");
		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);

	}

	public long exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {

		this.info("L9703Report2 exec");
		entdy = titaVo.getEntDy();
//		String f4 = "";
//		String f5 = "";

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9703A", "放款本息攤還表暨繳息通知單", "密", "8.5,12", "P");

		List<Map<String, String>> L9703List = null;
		try {
			L9703List = l9703ServiceImpl.queryForNotice(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9711ServiceImpl.LoanBorTx error = " + errors.toString());
		}

		this.info("L9703List-------->" + L9703List.size());
		for (Map<String, String> tL9703Vo : L9703List) {
//			if (!f4.equals(tL9703Vo.get("F4")) || !f5.equals(tL9703Vo.get("F5"))) {
			report(tL9703Vo, txbuffer, titaVo);
//			}
//			f4 = tL9703Vo.get("F4");
//			f5 = tL9703Vo.get("F5");
		}

		if (this.getNowPage() == 0) {

			String iCUSTNO = titaVo.get("CustNo");
			String iFACMNO = titaVo.get("FacmNo");

			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 10, "【限定本人拆閱，若無此人，請寄回本公司】");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			this.print(1, 1, "");
			String tmp = "";
			this.print(1, 10, tmp);
			this.print(1, 1, "");
			this.print(1, 10, String.format("%07d", Integer.valueOf(iCUSTNO)) + "   ");
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
			this.print(0, 20, this.showRocDate(entdy, 1));

			tmp = "";

			this.print(0, 87, tmp);

			this.print(1, 10, "戶號：　　　　　　　目前利率：　　　　%");
			this.print(0, 16, String.format("%07d", Integer.valueOf(iCUSTNO)) + "-"
					+ String.format("%03d", Integer.valueOf(iFACMNO)));
			this.print(1, 10, "客戶名稱：　　　　　　　　　　　　　　　　　　　　　溢短繳：");
			this.print(1, 10, "　　　　　　　　　　　　　　　　　　　　　　　　　　帳管費：");
			this.print(1, 10, "　　　　　　　　　　　　　每期應攤還　　　　　　　　　　　　未　　還　　暫　付");
			this.print(1, 10, "應繳日　　違約金　　　　本金　　　　　利息　　　應繳合計　　本金餘額　　所得稅　　應繳淨額");
			this.print(1, 7, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
			this.print(1, 20, "*******    查無資料   ******");
		}

		long sno = this.close();

		// 測試用
		this.toPdf(sno);

		return sno;
	}

	private void report(Map<String, String> tL9703Vo, TxBuffer txbuffer, TitaVo titaVo) throws LogicException {
		List<BaTxVo> listBaTxVo = new ArrayList<>();
//		int termStart = parse.stringToInteger(titaVo.getParam("UnpaidTermSt"));
		int termEnd = 0;

		if (titaVo.getParam("UnpaidTermEd") != null) {
			termEnd = parse.stringToInteger(titaVo.getParam("UnpaidTermEd"));
		}

//		未設定預設6期
		if (termEnd == 0) {
			termEnd = 6;
		}

		dBaTxCom.setTxBuffer(txbuffer);
		this.info("entdy = " + entdy);

		try {
			listBaTxVo = dBaTxCom.termsPay(parse.stringToInteger(titaVo.getParam("ENTDY")),
					parse.stringToInteger(tL9703Vo.get("F4")), parse.stringToInteger(tL9703Vo.get("F5")), 0, termEnd,
					titaVo);
		} catch (LogicException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("dBaTxCom.termsPay error = " + errors.toString());
		}

		this.info("listBaTxVo.size()-------->" + listBaTxVo.size());
		this.info("listBaTxVo-------->" + listBaTxVo.toString());
		HashMap<Integer, BigDecimal> principal = new HashMap<>();
		HashMap<Integer, BigDecimal> interest = new HashMap<>();
		HashMap<Integer, BigDecimal> breachAmt = new HashMap<>();
		HashMap<Integer, BigDecimal> delayInt = new HashMap<>();
		HashMap<Integer, Integer> flag = new HashMap<>();
		BigDecimal intRate = BigDecimal.ZERO;
		BigDecimal loanBal = BigDecimal.ZERO;
		BigDecimal unPaidAmt = BigDecimal.ZERO;
		BigDecimal acctFee = BigDecimal.ZERO;
		DecimalFormat df1 = new DecimalFormat("#,##0");
		int payIntDate = 0;

		// 未收本息 = 本金+利息 Principal + Interest
		// 違約金 有 但要扣成 0 Bre
		// 溢短繳 UnPaidAmt
		// 合計 = 未收本息 + 違約金 - 溢短繳

		if (this.getNowPage() > 0) {
			newPage();
		}

		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 10, "【限定本人拆閱，若無此人，請寄回本公司】");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 10, tranNum(tL9703Vo.get("F17")) + tranNum(tL9703Vo.get("F18")));
		this.print(1, 1, "");
		String tmp = "";
		if (tL9703Vo.get("F19") != null) {
			tmp = tmp + tL9703Vo.get("F19");
		}
		if (tL9703Vo.get("F20") != null) {
			tmp = tmp + tL9703Vo.get("F20");
		}
		if (tL9703Vo.get("F21") != null) {
			tmp = tmp + tL9703Vo.get("F21") + "路";
		}
		if (tL9703Vo.get("F22") != null) {
			tmp = tmp + tL9703Vo.get("F22") + "段";
		}
		if (tL9703Vo.get("F23") != null) {
			tmp = tmp + tL9703Vo.get("F23") + "巷";
		}
		if (tL9703Vo.get("F24") != null) {
			tmp = tmp + tL9703Vo.get("F24") + "弄";
		}
		if (tL9703Vo.get("F25") != null) {
			tmp = tmp + tL9703Vo.get("F25") + "號";
		}
		if (tL9703Vo.get("F26") != null && !"".equals(tL9703Vo.get("F26").trim())) {
			tmp = tmp + "之" + tL9703Vo.get("F26") + "，";
		}
		if (tL9703Vo.get("F27") != null) {
			tmp = tmp + tL9703Vo.get("F27") + "樓";
		}
		if (tL9703Vo.get("F28") != null && !"".equals(tL9703Vo.get("F28").trim())) {
			tmp = tmp + "之" + tL9703Vo.get("F28");
		}
		this.print(1, 10, tmp);
		this.print(1, 1, "");
		this.print(1, 10, String.format("%07d", Integer.valueOf(tL9703Vo.get("F4"))) + "   " + tL9703Vo.get("F6"));
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
		this.print(0, 20, this.showRocDate(entdy, 1));

		tmp = "";
		switch (tL9703Vo.get("F29")) {
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
		default:
			break;
		}

		for (int i = 0; i < listBaTxVo.size(); i++) {
			payIntDate = listBaTxVo.get(i).getPayIntDate();
			// 同應繳日合算為一筆

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

			if (delayInt.containsKey(payIntDate)) {
				delayInt.put(payIntDate, delayInt.get(payIntDate).add(listBaTxVo.get(i).getDelayInt()));
			} else {
				delayInt.put(payIntDate, listBaTxVo.get(i).getDelayInt());
			}

//				本金為總和
			loanBal = loanBal.add(listBaTxVo.get(i).getLoanBal());
//				暫收金額為總和
			if (listBaTxVo.get(i).getDataKind() == 4) {
				unPaidAmt = unPaidAmt.add(listBaTxVo.get(i).getUnPaidAmt());
			}
//				04.帳管費(總和)
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

		this.print(0, 87, tmp);
		this.print(1, 10, "戶號：　　　　　　　目前利率：　　　　%");
		this.print(0, 16, String.format("%07d", Integer.valueOf(tL9703Vo.get("F4"))) + "-"
				+ String.format("%03d", Integer.valueOf(tL9703Vo.get("F5"))));
		this.print(0, 47, padStart(6, "" + intRate), "R");
		this.print(1, 10, "客戶名稱：　　　　　　　　　　　　　　　　　　　　　溢短繳：");
		this.print(0, 20, tL9703Vo.get("F6"));
		this.print(0, 78, unPaidAmtX, "R");
		this.print(1, 10, "　　　　　　　　　　　　　　　　　　　　　　　　　　帳管費：");
		this.print(0, 78, acctFeeX, "R");
		this.print(1, 10, "　　　　　　　　　　　　　每期應攤還　　　　　　　　　　　　未　　還　　暫　付");
		this.print(1, 10, "應繳日　　違約金　　　　本金　　　　　利息　　　應繳合計　　本金餘額　　所得稅　　應繳淨額");
		this.print(1, 7, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

		for (int i = 0; i < listBaTxVo.size(); i++) {
			payIntDate = listBaTxVo.get(i).getPayIntDate();

//				同一日期者金額加總只顯示一筆
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
			BigDecimal bBreachAmt = BigDecimal.ZERO;
			BigDecimal bPrincipal = BigDecimal.ZERO;
			BigDecimal bInterest = BigDecimal.ZERO;
			BigDecimal bSummry = BigDecimal.ZERO;

			if (breachAmt.get(payIntDate) != null) {
				bBreachAmt = breachAmt.get(payIntDate);
			}
			if (delayInt.get(payIntDate) != null) {
				bBreachAmt = bBreachAmt.add(delayInt.get(payIntDate));
			}
			if (principal.get(payIntDate) != null) {
				bPrincipal = principal.get(payIntDate);
			}
			if (interest.get(payIntDate) != null) {
				bInterest = interest.get(payIntDate);
			}
			bSummry = bBreachAmt.add(bPrincipal.add(bInterest));

			loanBal = loanBal.subtract(bPrincipal);

			this.print(1, 1,
					"                                                                                                                                                                               ");
			if (!"00000000".equals(sPayIntDate)) {
				this.print(0, 7, sPayIntDate.substring(0, 3) + "/" + sPayIntDate.substring(3, 5) + "/"
						+ sPayIntDate.substring(5, 7));
			}

			this.print(0, 25, df1.format(bBreachAmt), "R");
			this.print(0, 38, df1.format(bPrincipal), "R");
			this.print(0, 52, df1.format(bInterest), "R");
			this.print(0, 65, df1.format(bSummry), "R");
			this.print(0, 78, df1.format(loanBal), "R");
			this.print(0, 87, "0", "R");
			this.print(0, 100, df1.format(bSummry), "R");
		} // loop -- batxCom

		this.print(2, 8, "＊＊舊繳息通知單作廢（以最新製發日期為準）。");
		this.print(1, 8, "＊＊貴戶所借款項如業已屆期，本公司雖經收取利息及違約金但並無同意延期清償之意 , 貴戶仍需依約履行");
		if (tL9703Vo.get("F8").equals("0")) {
			this.print(1, 8, "＊＊本額度自　　　年　　月　　日起本利均攤");
			this.print(1, 8, "　　貴戶所貸上列款項。於　　　年　　月　　日到期，請依約到本公司辦理清償或展期手續，請勿延誤");
		} else {
			this.print(1, 8, "　　貴戶所貸上列款項。於　" + showDate(tL9703Vo.get("F8"), 2) + "到期，請依約到本公司辦理清償或展期手續，請勿延誤");
		}
		this.print(1, 8, "＊＊新光銀行城內分行代號： 1030116");

		String iTLRNO = "";

		if (tL9703Vo.get("F3") != null || "".equals(tL9703Vo.get("F3"))) {
			iTLRNO = tL9703Vo.get("F3");
		}

		this.info("製表人 = " + iTLRNO);
		this.print(1, 86, "製表人 " + iTLRNO);

//			8 65
		String payIntAcct = "";
		String payPriAcct = "";

		if (tL9703Vo.get("F34") != null) {
//				共用代碼檔0:個金1:企金2:企金自然人
			if ("0".equals(tL9703Vo.get("F34"))) {
				payIntAcct = "9510200" + String.format("%07d", Integer.valueOf(tL9703Vo.get("F4")));
				payPriAcct = "9510300" + String.format("%07d", Integer.valueOf(tL9703Vo.get("F4")));
			} else {
				payIntAcct = "9510100" + String.format("%07d", Integer.valueOf(tL9703Vo.get("F4")));
				payPriAcct = "9510100" + String.format("%07d", Integer.valueOf(tL9703Vo.get("F4")));
			}
		}

		this.print(2, 8, "期款專用帳號：" + payIntAcct);
		this.print(0, 65, "還本專用帳號： " + payPriAcct);
	}

	// 顯示民國年
	private String showDate(String date, int iType) {
		this.info("MakeReport.toPdf showRocDate1 = " + date);
		if (date == null || date.equals("") || date.equals("0")) {
			return "";
		}
		int rocdate = Integer.parseInt(date);
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

	private String padStart(int size, String input) {
		for (int i = 0; i < size; i++) {
			if (input.length() < size) {
				input = input + "0";
			}
		}
		return input;
	}
}
