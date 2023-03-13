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
import com.st1.itx.util.common.data.ReportVo;
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
	int calaulateCount = 0;
	String txcdW = "";

	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");
		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(0);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(60);

	}

	public long exec(TitaVo titaVo, TxBuffer txbuffer, List<Map<String, String>> L9711List,String nTxCd) throws LogicException {

		this.info("L9711Report2 exec");
		ENTDY = String.valueOf(Integer.parseInt(titaVo.getParam("ENTDY").toString()));
		int count = 0;

		String txcd = titaVo.getTxcd();
		txcdW = txcd;
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String reportItem = "放款本息攤還表暨繳息通知單";
		String security =this.getSecurity();
		String pageSize = "A4";
		String pageOrientation = "P";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(nTxCd)
				.setRptItem(reportItem).setSecurity(security).setRptSize(pageSize).setPageOrientation(pageOrientation)
				.build();
		this.openForm(titaVo, reportVo);

		if (L9711List.size() > 0) {

			for (Map<String, String> tL9711Vo : L9711List) {

				if (count > 0) {
					this.newPage();
				}

				try {
					Thread.sleep(1 * 500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// 確認 CustNoticeCom 檢查是否能產出郵寄通知

				// inputCustNo: #CUSTNO
				// CustNo: Query.F4
				// FacmNo: Query.F5

				String inputCustNo = titaVo.get("CustNo");
				String recordCustNoString = tL9711Vo.get("F4");
				String recordFacmNoString = tL9711Vo.get("F5");
				int recordCustNo = parse.stringToInteger(recordCustNoString);
				int recordFacmNo = parse.stringToInteger(recordFacmNoString);
				this.info("recordCustNoString=" + recordCustNoString);
				this.info("recordFacmNo=" + recordFacmNo);

				if (!custNoticeCom.checkIsLetterSendable(inputCustNo, recordCustNo, recordFacmNo, "L9711", titaVo)) {
					continue;
				}

				this.info("recordCustNoString2=" + recordCustNoString);
				this.info("recordFacmNo2=" + recordFacmNo);
				// 每次戶號額度都不一樣
				report(tL9711Vo, txbuffer);

				//

				count++;
			} // for

		} else {
			this.info("L9711List.reportEmpty");
			reportEmpty();
		}

		long sno = this.close();

		return sno;
	}

	private void reportEmpty() throws LogicException {

		this.setRptItem("放款本息攤還表暨繳息通知單(查無資料)");

		this.printCm(1, 4, "*******    查無資料   ******");
	}

	private void report(Map<String, String> tL9711Vo, TxBuffer txbuffer) throws LogicException {
		ArrayList<BaTxVo> lBaTxVo = new ArrayList<>();
		ArrayList<BaTxVo> listBaTxVo = new ArrayList<>();
		calaulateCount++;

		this.info("report.count = 第" + calaulateCount + "筆");
		this.info("report.List = " + tL9711Vo.toString());

		try {
			dBaTxCom.setTxBuffer(txbuffer);
			lBaTxVo = dBaTxCom.termsPay(parse.stringToInteger(titaVo.getParam("ENTDY")),
					parse.stringToInteger(tL9711Vo.get("F4")), parse.stringToInteger(tL9711Vo.get("F5")), 0, 6, 0,
					titaVo);
			listBaTxVo = dBaTxCom.addByPayintDate(lBaTxVo, titaVo);
		} catch (LogicException e) {
			this.info("baTxCom.setTxBuffer ErrorMsg :" + e.getMessage());
		}

		this.info("listBaTxVo.size()-------->" + listBaTxVo.size());
		if (listBaTxVo.size() == 0) {
			return;
		}
		// 溢短繳
		int excessive = dBaTxCom.getExcessive().intValue() - dBaTxCom.getShortfall().intValue();
		// 帳管費 + 契變手續費
		int acctFee = dBaTxCom.getAcctFee().intValue() + dBaTxCom.getModifyFee().intValue();
		double IntRate = dBaTxCom.getFitRate().doubleValue();
		int custNo = Integer.valueOf(tL9711Vo.get("F4"));

		CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);

		setFont(1, 11);

		printCm(1, 2.5, "【限定本人拆閱，若無此人，請寄回本公司】");

		printCm(2, 3.5, tranNum(tL9711Vo.get("F17")) + tranNum(tL9711Vo.get("F18")));

		if ("L9710".equals(txcdW)) {
			printCm(15, 3.5, "限  時  專  送");
		}

		String addr = custNoticeCom.getCurrAddress(custMain, titaVo);
		printCm(2, 4.5, addr);

		printCm(2, 5.5, String.format("%07d", custNo) + "   " + tL9711Vo.get("F6"));

		int top = 0;// 上下微調用
		double yy = 20;// 開始Y軸
		double h = 0.4;// 列高
		double l = 0;// 列數

		double y = top + yy;

		setFont(1, 14);

		 printCm(10, 19, " ", "C");

		setFont(1, 11);

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

		int tmpCount = 0;

		for (BaTxVo baTxVo : listBaTxVo) {
			tmpCount++;
			if (tmpCount == 7) {
				break;
			}
			this.info("baTxVo.getDataKind()=" + baTxVo.getDataKind());
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

			this.info("tempDateX=" + tempDate);
			this.info("BreachAmtX=" + BreachAmt);
			this.info("PrincipalX=" + Principal);
			this.info("InterestX=" + Interest);
			this.info("LoanBalX=" + LoanBal);
			this.info("UnPaidAmtX=" + UnPaidAmt);
			this.info("excessiveX=" + excessive);
			this.info("acctFeeX=" + acctFee);

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

		if ("L9710".equals(txcdW)) {
			printCm(1, y, "＊＊舊繳息通知單作廢（以最新製發日期為準）。");

			y = top + yy + (++l) * h;

			printCm(1, y, "＊＊本額度自  " + showDate(tL9711Vo.get("F8"), 2) + "起本利均攤");

			y = top + yy + (++l) * h;
			printCm(1, y, "＊＊新光銀行城內分行代號： 1030116");

		} else if ("L9711".equals(txcdW)) {

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
		}

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
			if ("0".equals(tL9711Vo.get("EntCode"))) {
				payIntAcct = "9510200" + String.format("%07d", Integer.valueOf(tL9711Vo.get("F4")));
				payPriAcct = "9510300" + String.format("%07d", Integer.valueOf(tL9711Vo.get("F4")));
			} else {
				payIntAcct = "9510100" + String.format("%07d", Integer.valueOf(tL9711Vo.get("F4")));
				payPriAcct = "9510100" + String.format("%07d", Integer.valueOf(tL9711Vo.get("F4")));
			}
		}
		printCm(4, 28, payIntAcct);
		printCm(14, 28, payPriAcct);

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
//		this.info("tranDate1 = " + data);
		if (data == null || data.equals("")) {
			return "";
		}
		String tmp1 = data;
//		this.info("tranData = " + tmp1);
		String tmp2 = "";

		int tranTemp = 0;
		int i = 0;
		char tmp;

		for (i = 0; i < tmp1.length(); i++) {

//			this.info("tranDate i = " + i);
//			this.info("tranDate X = " + tmp1.substring(i, i + 1));
			tmp = tmp1.charAt(i);

			tranTemp = (int) tmp;

			tranTemp += 65248; // 此數字是 Unicode編碼轉為十進位 和 ASCII碼的 差

//			this.info("tranDate XX= " + (char) tranTemp);
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
