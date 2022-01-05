package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.cm.L9703ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.CustNoticeCom;
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

	@Autowired
	L9703ServiceImpl l9703ServiceImpl;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	BaTxCom dBaTxCom;

	@Autowired
	CustNoticeCom custNoticeCom;

	@Autowired
	Parse parse;

	private String entdy = "";

	private int cnt = 0;

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

		String rptitem = "放款本息攤還表暨繳息通知單";

//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9703A", "放款本息攤還表暨繳息通知單", "密", "8.5,12", "P");
		openForm(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(),
				titaVo.getTxCode().isEmpty() ? "L9703B" : titaVo.getTxCode() + "B", rptitem, "inch,8.5,12", "P");

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

//		if (this.getNowPage() == 0) {
//
//			String iCUSTNO = titaVo.get("CustNo");
//			String iFACMNO = titaVo.get("FacmNo");
//
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			this.print(1, 10, "【限定本人拆閱，若無此人，請寄回本公司】");
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			String tmp = "";
//			this.print(1, 10, tmp);
//			this.print(1, 1, "");
//			this.print(1, 10, String.format("%07d", Integer.valueOf(iCUSTNO)) + "   ");
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//
//			// this.setFontSize(14);
//			this.print(1, 45, "放款本息攤還表暨繳息通知單", "C");
//
//			// this.setFontSize(10);
//			this.print(1, 1, "");
//			this.print(1, 1, "");
//			this.print(1, 10, "製發日期：");
//			this.print(0, 20, this.showRocDate(entdy, 1));
//
//			tmp = "";
//
//			this.print(0, 87, tmp);
//
//			this.print(1, 10, "戶號：　　　　　　　目前利率：　　　　%");
//			this.print(0, 16, String.format("%07d", Integer.valueOf(iCUSTNO)) + "-"
//					+ String.format("%03d", Integer.valueOf(iFACMNO)));
//			this.print(1, 10, "客戶名稱：　　　　　　　　　　　　　　　　　　　　　溢短繳：");
//			this.print(1, 10, "　　　　　　　　　　　　　　　　　　　　　　　　　　帳管費：");
//			this.print(1, 10, "　　　　　　　　　　　　　每期應攤還　　　　　　　　　　　　未　　還　　暫　付");
//			this.print(1, 10, "應繳日　　違約金　　　　本金　　　　　利息　　　應繳合計　　本金餘額　　所得稅　　應繳淨額");
//			this.print(1, 7, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
//			this.print(1, 20, "*******    查無資料   ******");

		if (this.getPrintCnt() == 0) {
			this.setRptItem("放款本息攤還表暨繳息通知單(無符合資料)");
			setFont(1, 14);
			printCm(1, 4, "【無符合資料】");
		}

		long sno = this.close();

		// 測試用
//		this.toPdf(sno);

		return sno;
	}

	private void report(Map<String, String> tL9703Vo, TxBuffer txbuffer, TitaVo titaVo) throws LogicException {
		ArrayList<BaTxVo> lBaTxVo = new ArrayList<>();
		ArrayList<BaTxVo> listBaTxVo = new ArrayList<>();
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
			lBaTxVo = dBaTxCom.termsPay(parse.stringToInteger(titaVo.getParam("ENTDY")),
					parse.stringToInteger(tL9703Vo.get("F4")), parse.stringToInteger(tL9703Vo.get("F5")), 0, termEnd,
					titaVo);
			listBaTxVo = dBaTxCom.addByPayintDate(lBaTxVo, titaVo);
		} catch (LogicException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("dBaTxCom.termsPay error = " + errors.toString());
		}

		this.info("listBaTxVo.size()-------->" + listBaTxVo.size());
		this.info("listBaTxVo-------->" + listBaTxVo.toString());
		// 溢短繳
		int excessive = dBaTxCom.getExcessive().intValue() - dBaTxCom.getShortfall().intValue();
		// 帳管費 + 契變手續費
		int acctFee = dBaTxCom.getAcctFee().intValue() + dBaTxCom.getModifyFee().intValue();
		// 目前利率
		double intRate = dBaTxCom.getFitRate().doubleValue();

		// 未收本息 = 本金+利息 Principal + Interest
		// 違約金 有 但要扣成 0 Bre
		// 溢短繳 UnPaidAmt
		// 合計 = 未收本息 + 違約金 - 溢短繳

		if (cnt > 0) {
			newPage();
		}

		cnt++;

		setFont(1, 14);

		printCm(1, 4, "【限定本人拆閱，若無此人，請寄回本公司】");

		printCm(2, 5, tranNum(tL9703Vo.get("F17")) + tranNum(tL9703Vo.get("F18")));

		int custNo = Integer.valueOf(tL9703Vo.get("F4"));

		CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);
		String addr = custNoticeCom.getCurrAddress(custMain, titaVo);

		printCm(2, 6, addr);

		printCm(2, 7, String.format("%07d", custNo) + "   " + tL9703Vo.get("F6"));

		setFont(1, 11);

		int top = 0;// 上下微調用
		double yy = 19;// 開始Y軸
		double h = 0.4;// 列高
		double l = 0;// 列數

		double y = top + yy;
		printCm(1.5, y, "製發日期：" + this.showRocDate(entdy, 1));

		String tmp = "";
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

		printCm(16, y, tmp);

		y = top + yy + (++l) * h;
		printCm(1.5, y,
				"戶    號：" + String.format("%07d", Integer.valueOf(tL9703Vo.get("F4"))) + "-"
						+ String.format("%03d", Integer.valueOf(tL9703Vo.get("F5"))) + "  目前利率："
						+ padStart(6, "" + intRate) + "%");

		y = top + yy + (++l) * h;
		printCm(1.5, y, "客戶名稱：" + tL9703Vo.get("F6"));
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
		printCm(1, y, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

		String tempDate = "";
		int Principal = 0;
		int Interest = 0;
		int BreachAmt = 0;
		int UnPaidAmt = 0;
		int LoanBal = 0;
		int size = 0;
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
		}

		dDateUtil.init();
		dDateUtil.setDate_1(entdy);
		dDateUtil.setMons(0);
		dDateUtil.setDays(1);

		int nextday = dDateUtil.getCalenderDay();

		l++;
		y = top + yy + (++l) * h;
		printCm(1, y, "＊＊舊繳息通知單作廢（以最新製發日期為準）。");
		y = top + yy + (++l) * h;
		printCm(1, y, "＊＊貴戶所借款項如業已屆期，本公司雖經收取利息及違約金但並無同意延期清償之意 , 貴戶仍需依約履行");
		y = top + yy + (++l) * h;
		printCm(1, y, "＊＊註：違約金暫計到" + showDate(titaVo.getParam("ENTDY"), 2) + " ,　若提前或延後繳款 , 請電話查詢" + "　該違約金金額");
		y = top + yy + (++l) * h;
		printCm(1, y, "＊＊截至" + showDate("" + nextday, 2) + "，貸款尚欠" + FormatUtil.pad9("" + size, 3) + "期。請撥空盡速繳納");
		y = top + yy + (++l) * h;
		printCm(1, y, "＊＊新光銀行城內分行代號： 1030116");

		String iTLRNO = "";

		if (tL9703Vo.get("F3") != null || "".equals(tL9703Vo.get("F3"))) {
			iTLRNO = tL9703Vo.get("F3");
		}
		y = top + yy + (++l) * h;
		printCm(14, y, "製表人 " + iTLRNO);

		String payIntAcct = "";
		String payPriAcct = "";

		if (tL9703Vo.get("F34") != null) {
			if ("0".equals(tL9703Vo.get("F34"))) {
				payIntAcct = "9510200" + String.format("%07d", Integer.valueOf(tL9703Vo.get("F4")));
				payPriAcct = "9510300" + String.format("%07d", Integer.valueOf(tL9703Vo.get("F4")));
			} else {
				payIntAcct = "9510100" + String.format("%07d", Integer.valueOf(tL9703Vo.get("F4")));
				payPriAcct = "9510100" + String.format("%07d", Integer.valueOf(tL9703Vo.get("F4")));
			}
		}

		printCm(4, 27, payIntAcct);
		printCm(14, 27, payPriAcct);
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
