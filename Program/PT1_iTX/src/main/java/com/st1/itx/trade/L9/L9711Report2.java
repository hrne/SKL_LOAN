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
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L9711Report2 extends MakeReport {

	@Autowired
	public CustMainService custMainService;

	@Autowired
	CustNoticeCom custNoticeCom;

//	L9711ServiceImpl L9711ServiceIml
	@Autowired
	DateUtil dDateUtil;

	@Autowired
	private CdEmpService cdEmpService;

	@Autowired
	BaTxCom dBaTxCom;

	@Autowired
	public Parse parse;

	@Autowired
	public TxBuffer txBuffer;

	String ENTDY = "";
	Boolean printtimes = false;

	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");
		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(60);

	}

	public long exec(TitaVo titaVo, TxBuffer txbuffer, List<Map<String, String>> L9711List) throws LogicException {

		this.info("L9711Report2 exec");
		ENTDY = String.valueOf(Integer.parseInt(titaVo.getParam("ENTDY").toString()));
		String f4 = "";
		String f5 = "";
		int count = 0;
		// 製表人
//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9711", "放款本息攤還表暨繳息通知單", "密", "A4", "P");
		openForm(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(),
				titaVo.getTxCode().isEmpty() ? "L9711" : titaVo.getTxCode(), "放款本息攤還表暨繳息通知單", "inch,8.5,12", "P");

		if (L9711List.size() > 0) {

			for (Map<String, String> tL9711Vo : L9711List) {
				// 確認 CustNoticeCom 檢查是否能產出郵寄通知

				// inputCustNo: #CUSTNO
				// CustNo: Query.F4
				// FacmNo: Query.F5

				String inputCustNo = titaVo.get("CustNo");
				String recordCustNoString = tL9711Vo.get("F4");
				String recordFacmNoString = tL9711Vo.get("F5");
				int recordCustNo = parse.stringToInteger(recordCustNoString);
				int recordFacmNo = parse.stringToInteger(recordFacmNoString);
				if (!custNoticeCom.checkIsLetterSendable(inputCustNo, recordCustNo, recordFacmNo, "L9711", titaVo))
					continue;

				// 有一樣戶號額度的話 用同一張
				if (f4.equals(tL9711Vo.get("F4")) && f5.equals(tL9711Vo.get("F5"))) {

					report(tL9711Vo, txbuffer);
					if (printtimes) {
						count++;
					}

				} else {
					if (count != 0) {
						this.newPage();
						count = 0;
					}
					printtimes = false;
					report(tL9711Vo, txbuffer);
					if (printtimes) {
						count++;
					}
				}
				f4 = tL9711Vo.get("F4");
				f5 = tL9711Vo.get("F5");

			} // for

			if (count == 0) {
				this.printCm(1, 4, "*******    查無資料   ******");
			}

		} else {
			this.info("L9711List.reportEmpty");
			reportEmpty();
		}

		long sno = this.close();

		return sno;
//		 this.toPdf(sno);
	}

	private void reportEmpty() throws LogicException {

//		this.print(-4, 10, "【限定本人拆閱，若無此人，請寄回本公司】");
//
//		this.setFontSize(14);
//		this.print(-15, 37, "放款本息攤還表暨繳息通知單", "C");
//
//		this.setFontSize(10);
//
//		this.print(-22, 10, "製發日期：");
//		this.print(-22, 20, ENTDY.substring(0, 3) + "/" + ENTDY.substring(3, 5) + "/" + ENTDY.substring(5, 7));
//		this.print(-23, 10, "戶號：　　　　　　　目前利率：　　　　%");
//
//		this.print(-24, 10, "客戶名稱：　　　　　　　　　　　　　　　　　　　　　溢短繳：");
//
//		this.print(-25, 10, "　　　　　　　　　　　　　　　　　　　　　　　　　　管帳費：");
//
//		this.print(-26, 10, "　　　　　　　　　　　　　每期應攤還　　　　　　　　　　　　未　　還　　暫　付");
//		this.print(-27, 10, "應繳日　　違約金　　　　本金　　　　　利息　　　應繳合計　　本金餘額　　所得稅　　應繳淨額");
//		this.print(-28, 7, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
//		this.print(-29, 7, "本日無資料");
//
//		this.print(-31, 8, "＊＊舊繳息通知單作廢（以最新製發日期為準）。");
//		this.print(-32, 8, "＊＊貴戶所借款項如業已屆期，本公司雖經收取利息及違約金但並無同意延期清償之意 , 貴戶仍需依約履行");
//
//		this.print(-33, 8, "＊＊本額度自　　　年　　月　　日起本利均攤");
//		this.print(-34, 8, "　　貴戶所貸上列款項。於　　　年　　月　　日到期，請依約到本公司辦理清償或展期手續，請勿延誤");
//
//		this.print(-35, 8, "＊＊新光銀行　　分行代號：");
//
//		this.print(-36, 82, "製表人 ");
//		this.print(1, 1, " ");

		this.setRptItem("放款本息攤還表暨繳息通知單(查無資料)");

		this.printCm(1, 4, "*******    查無資料   ******");
	}

	private void report(Map<String, String> tL9711Vo, TxBuffer txbuffer) throws LogicException {
		ArrayList<BaTxVo> lBaTxVo = new ArrayList<>();
		ArrayList<BaTxVo> listBaTxVo = new ArrayList<>();
	
		try {
			dBaTxCom.setTxBuffer(txbuffer);
			lBaTxVo = dBaTxCom.termsPay(parse.stringToInteger(titaVo.getParam("ENTDY")),
					parse.stringToInteger(tL9711Vo.get("F4")), parse.stringToInteger(tL9711Vo.get("F5")), 0, 2, 0,
					titaVo);
			listBaTxVo = dBaTxCom.addByPayintDate(lBaTxVo, titaVo);
		} catch (LogicException e) {
			this.info("baTxCom.setTxBuffer ErrorMsg :" + e.getMessage());
		}

		this.info("listBaTxVo.size()-------->" + listBaTxVo.size());
		this.info("listBaTxVo-------->" + listBaTxVo.toString());
		if (listBaTxVo.size() == 0) {
			return;
		}
		// 溢短繳
		int excessive = dBaTxCom.getExcessive().intValue() - dBaTxCom.getShortfall().intValue();
		;
		// 帳管費 + 契變手續費
		int acctFee = dBaTxCom.getAcctFee().intValue() + dBaTxCom.getModifyFee().intValue();
		double IntRate = dBaTxCom.getFitRate().doubleValue();
		int custNo = Integer.valueOf(tL9711Vo.get("F4"));

		CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);

		setFont(1, 14);

		printCm(1, 4, "【限定本人拆閱，若無此人，請寄回本公司】");

		printCm(2, 5, tranNum(tL9711Vo.get("F17")) + tranNum(tL9711Vo.get("F18")));

		String addr = custNoticeCom.getCurrAddress(custMain, titaVo);
		printCm(2, 6, addr);

		printCm(2, 7, String.format("%07d", custNo) + "   " + tL9711Vo.get("F6"));

//		this.setFontSize(14);
//		this.print(-15, 37, "放款本息攤還表暨繳息通知單", "C");

		this.setFontSize(11);

		int top = 0;// 上下微調用
		double yy = 21;// 開始Y軸
		double h = 0.4;// 列高
		double l = 0;// 列數

		double y = top + yy;
		printCm(1.5, y, "製發日期：" + ENTDY.substring(0, 3) + "/" + ENTDY.substring(3, 5) + "/" + ENTDY.substring(5, 7));

		printCm(16, y, tL9711Vo.get("F20"));

		y = top + yy + (++l) * h;
		printCm(1.5, y,
				"戶    號：" + String.format("%07d", Integer.valueOf(tL9711Vo.get("F4"))) + "-"
						+ String.format("%03d", Integer.valueOf(tL9711Vo.get("F5"))) + "  目前利率："
						+ padStart(6, "" + IntRate) + "%");

		y = top + yy + (++l) * h;

		int nameLength = 20;
		if (tL9711Vo.get("F6").length() < 20) {
			nameLength = tL9711Vo.get("F6").length();
		}

		printCm(1.5, y, "客戶名稱：" + tL9711Vo.get("F6").substring(0, nameLength));
		printCm(12, y, "溢短繳：", "R");
		printCm(14, y, String.format("%,d", excessive), "R");

		y = top + yy + (++l) * h;
		printCm(12, y, "帳管費：", "R");
		printCm(14, y, String.format("%,d", acctFee), "R");

		y = top + yy + (++l) * h;
		printCm(8.8, y, "每期應攤還", "R");
		printCm(14.5, y, "未　　還", "R");
		printCm(16.5, y, "暫　付", "R");

		y = top + yy + (++l) * h;
		printCm(1, y, "應繳日");
		printCm(4.5, y, "違約金", "R");
		printCm(7, y, "本金", "R");
		printCm(9.5, y, "利息", "R");
		printCm(12, y, "應繳合計", "R");
		printCm(14.5, y, "本金餘額", "R");
		printCm(16.5, y, "所得稅", "R");
		printCm(19, y, "應繳淨額", "R");

		y = top + yy + (++l) * h;
		printCm(1, y, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		String tempDate = "";

		int dataRow = -29;
		int Principal = 0;
		int Interest = 0;
		int BreachAmt = 0;
		int UnPaidAmt = 0;
		int LoanBal = 0;
		excessive = excessive - acctFee;
		for (BaTxVo baTxVo : listBaTxVo) {
			if (baTxVo.getDataKind() != 2) {
				continue;
			}
			tempDate = String.valueOf(baTxVo.getPayIntDate()).toString();

			// 違約金
			BreachAmt = baTxVo.getBreachAmt().intValue() + baTxVo.getDelayInt().intValue();
			// 本金
			Principal = baTxVo.getPrincipal().intValue();
			// 利息
			Interest = baTxVo.getInterest().intValue();
			// 未還本金餘額
			LoanBal = baTxVo.getLoanBalPaid().intValue();

			UnPaidAmt = BreachAmt + Principal + Interest;
			if (UnPaidAmt == 0) {
				continue;
			}

			printtimes = true;
			if (excessive < 0) {
				UnPaidAmt = UnPaidAmt - excessive;
				excessive = 0;
			} else {
				if (UnPaidAmt > excessive) {
					UnPaidAmt = UnPaidAmt - excessive;
					excessive = 0;
				} else {
					UnPaidAmt = 0;
					excessive = excessive - UnPaidAmt;
				}
			}
			y = top + yy + (++l) * h;

			// 應繳日
			printCm(1, y, tempDate.substring(0, 3) + "/" + tempDate.substring(3, 5) + "/" + tempDate.substring(5, 7));
			// 違約金
			printCm(4.5, y, String.format("%,d", BreachAmt), "R");
			// 本金
			printCm(7, y, String.format("%,d", Principal), "R");
			// 利息
			printCm(9.5, y, String.format("%,d", Interest), "R");
			// 應繳合計
			printCm(12, y, String.format("%,d", UnPaidAmt), "R");
			// 未還 本金餘額
			printCm(14.5, y, String.format("%,d", LoanBal), "R");
			// 暫付 所得稅
			printCm(16.5, y, "0", "R");
			// 應繳淨額
			printCm(19, y, String.format("%,d", UnPaidAmt), "R");

			dataRow--;
		}

		dataRow--;

		l++;
		y = top + yy + (++l) * h;

		printCm(1, y, "＊＊舊繳息通知單作廢（以最新製發日期為準）。");
		y = top + yy + (++l) * h;
		printCm(1, y, "＊＊貴戶所借款項如業已屆期，本公司雖經收取利息及違約金但並無同意延期清償之意 , 貴戶仍需依約履行");
		if (tL9711Vo.get("F8").equals("0")) {
			y = top + yy + (++l) * h;
			printCm(1, y, "＊＊本額度自　　　年　　月　　日起本利均攤");
			y = top + yy + (++l) * h;
			printCm(1, y, "　　貴戶所貸上列款項。於　　　年　　月　　日到期，請依約到本公司辦理清償或展期手續，請勿延誤");
		} else {
			y = top + yy + (++l) * h;
			printCm(1, y, "　　貴戶所貸上列款項。於　" + showDate(tL9711Vo.get("F8"), 2) + "到期，請依約到本公司辦理清償或展期手續，請勿延誤");
		}

		// SQL尚缺分行代號
		y = top + yy + (++l) * h;
		printCm(1, y, "＊＊新光銀行城內分行代號： 1030116");
//		tmp = tL9711Vo.get("F3");
//		if (tmp == null) {
//			tmp = "";
//		}

		String iTLRNO = "";

		if (titaVo.getTlrNo() != null) {
			iTLRNO = titaVo.getTlrNo();
		}

		CdEmp tCdEmp = cdEmpService.findById(iTLRNO, titaVo);

		String empName = "";

		if (tCdEmp != null) {
			empName = tCdEmp.getFullname();
		}

		y = top + yy + (++l) * h;
		printCm(14, y, "製表人 " + empName);

		dataRow = dataRow - 2;

		String payIntAcct = "";
		String payPriAcct = "";

		if (tL9711Vo.get("F4") != null) {
//			共用代碼檔0:個金1:企金2:企金自然人
			if ("0".equals(tL9711Vo.get("F23"))) {
				payIntAcct = "9510200" + String.format("%07d", Integer.valueOf(tL9711Vo.get("F4")));
				payPriAcct = "9510300" + String.format("%07d", Integer.valueOf(tL9711Vo.get("F4")));
			} else {
				payIntAcct = "9510100" + String.format("%07d", Integer.valueOf(tL9711Vo.get("F4")));
				payPriAcct = "9510100" + String.format("%07d", Integer.valueOf(tL9711Vo.get("F4")));
			}
		}
		printCm(4, 28, payIntAcct);
		printCm(14, 28, payPriAcct);

//		this.newPage();

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

	private String padStart(int size, String input) {
		for (int i = 0; i < size; i++) {
			if (input.length() < size) {
				input = input + "0";
			}
		}
		return input;
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