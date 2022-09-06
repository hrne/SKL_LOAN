package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
	private BaTxCom dBaTxCom;

	@Autowired
	private CdEmpService cdEmpService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	private Parse parse;

	@Autowired
	private WebClient webClient;

	@Autowired
	CustNoticeCom custNoticeCom;

	public int custNo = 0;
	public int facmNo = 0;
	public String repayCode = "";

	@Override
	public void printHeader() {

		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(60);

	}

	public long exec(List<Map<String, String>> l9705List, TitaVo titaVo, TxBuffer txbuffer) throws LogicException {

		long cls = 0;
		
		String rptitem = "";
		if ("L8101".equals(titaVo.getTxCode())) {
			String dataDt = titaVo.get("DataDt").trim();
			String custKey = titaVo.get("CustKey").trim();
			String batchNo = titaVo.get("BatchNo");
			String ReviewType = "定審" + titaVo.get("ReviewTypeX");
			if (batchNo == null) {
				batchNo = "";
			} else {
				batchNo = batchNo.trim();
			}

			if (batchNo.isEmpty()) {
				rptitem = "放款本息攤還表暨繳息通知單(" + ReviewType + "/" + dataDt + "/" + custKey + ")";
			} else {
				rptitem = "放款本息攤還表暨繳息通知單(" + ReviewType + "/" + titaVo.getParam("CALDY") + "整批)";
				this.setBatchNo(batchNo);
			}
		} else {
			rptitem = "放款本息攤還表暨繳息通知單";
		}

		String tran = titaVo.getTxCode().isEmpty() ? "L9705" : titaVo.getTxCode();

		if (l9705List.size() > 0) {
			boolean isOpenA3 = true;
			boolean isOpen = true;
			int count = 0;

			for (Map<String, String> r : l9705List) {

				try {
					Thread.sleep(1 * 500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				int custNo = 0;
				int facmNo = 0;
				if (r.get("CustNo") != null) {
					custNo = parse.stringToInteger(r.get("CustNo"));
				}
				if (r.get("FacmNo") != null) {
					facmNo = parse.stringToInteger(r.get("FacmNo"));
				}
				if (r.get("RepayCode") != null) {
					repayCode = r.get("RepayCode");
				}
				
				String reconCode = "N";

				if (!"L4454".equals(titaVo.getTxCode())) {
					reconCode = r.get("ReconCode");
				}
				

				// 同戶號 同額度使用同一頁否則換新頁
				if (this.custNo == custNo && this.facmNo == facmNo) {

				} else {
					
					if (count > 0 && !isOpenA3 && "A3".equals(reconCode) ) {
						this.info("isOpenA3 newPage");
						this.newPage();
					}else if(count > 0 && !isOpen ){
						this.info("isOpen newPage");
						this.newPage();
					}
				}

				this.custNo = custNo;
				this.facmNo = facmNo;

		
				
			
				
				// 分開兩張報表出，query已經有排序A3先
				if ("A3".equals(reconCode)) {

					if (isOpenA3) {
						this.info("isOpenA3");
						this.info("titaVo = " + titaVo);

						ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
								.setRptCode(tran + "A (A3)").setRptItem(rptitem+"(A3)").setRptSize("A4").setSecurity("")
								.setPageOrientation("P").build();

						this.openForm(titaVo, reportVo);

						isOpenA3 = false;
					}

					// 檢查 CustNoticeCom 確認此戶此報表是否能產出
					// input parameter: CUSTNO
					// L4702 會直接塞值呼叫這隻，那邊有分整批與個別功能，因此這裡透過判定 param CUSTNO 是否為空來看為個別或整批
					if (!custNoticeCom.checkIsLetterSendable(titaVo.get("CUSTNO"), custNo, facmNo, "L9705", titaVo))
						continue;

					exportData(l9705List, titaVo, txbuffer, "A3", count);
				} else {
					
					// 關閉A3的報表
					if (!isOpenA3 && !"A3".equals(reconCode)) {
						this.info("isCloseA3");
						isOpenA3 = true;
						cls = this.close();

					}

					if (isOpen) {
						this.info("isOpen");
						this.info("titaVo = " + titaVo);

						ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
								.setRptCode(tran + "A").setRptItem(rptitem).setRptSize("A4").setSecurity("")
								.setPageOrientation("P").build();

						this.openForm(titaVo, reportVo);

						isOpen = false;
					}
					exportData(l9705List, titaVo, txbuffer, "N", count);

				}
				
				//筆數
				count++;
				
				// 關閉非A3的報表 或 關閉只有A3資料的報表
				if (!isOpen && isOpenA3 && count == l9705List.size()) {
					this.info("isClose");
					isOpen = true;
					cls = this.close();
				} else if (!isOpenA3 && count == l9705List.size()) {
					this.info("isCloseA3 ... only A3 data");
					isOpenA3 = true;
					cls = this.close();
				}
				

			}

		} else {
			this.info("isOpen ... no data");

			ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
					.setRptCode(tran + "A").setRptItem(rptitem).setRptSize("inch,8.5,12").setSecurity("")
					.setPageOrientation("P").build();

			this.openForm(titaVo, reportVo);

			this.setRptItem("放款本息攤還表暨繳息通知單(查無資料)");

			this.printCm(1, 4, "*******    查無資料    ******");

			this.info("查無資料 Return...");

			cls = this.close();
		}

		if (titaVo.get("selectTotal") == null || titaVo.get("selectTotal").equals(titaVo.get("selectIndex"))) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"),
					titaVo.getTxCode().isEmpty() ? "L9705" : titaVo.getTxCode() + "放款本息攤還表暨繳息通知單已完成", titaVo);
		}

		
		return cls;

	}

	private void exportData(List<Map<String, String>> r, TitaVo titaVo, TxBuffer txbuffer, String reconcode, int c)
			throws LogicException {

		this.info("titaVo.getTxCode =" + titaVo.getTxCode());
		this.info("exportData.count = 第" + (c + 1) + "筆" + ",reconcode = " + reconcode);
		this.info("exportData.List = " + r.toString());
		// 設定試算期數 2021-12-24 智偉新增
		int terms = 6; // 預設印6期

		if (titaVo.containsKey("Terms") && titaVo.getParam("Terms") != null) {
			try {
				terms = Integer.parseInt(titaVo.getParam("Terms"));
			} catch (Exception e) {
				terms = 6; // 若無法轉為數值,改為預設6
			}
		}

		String entdy = titaVo.getEntDy();

		String conditionCode = "";
		this.info("conditionCode = " + titaVo.get("CONDITION1"));
		if (titaVo.get("CONDITION1") != null) {
			conditionCode = titaVo.get("CONDITION1");
		}

//		A.匯款轉帳，已入金，有欠繳-------入帳完成後，於應處理事項清單執行L4702。
//		B.銀扣火險成功，期款失敗----------L4454.銀扣失敗通知(一扣)

		int cnt = 0;

		ArrayList<BaTxVo> lBaTxVo = new ArrayList<>();
		ArrayList<BaTxVo> listBaTxVo = new ArrayList<>();

		dBaTxCom.setTxBuffer(txbuffer);

		int entryDate = parse.stringToInteger(titaVo.getParam("ENTDY"));

		String payIntAcct = "";
		String payPriAcct = "";
		DecimalFormat df1 = new DecimalFormat("#,##0");

		CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);
		String currAddress = custNoticeCom.getCurrAddress(custMain, titaVo);

		try {
			lBaTxVo = dBaTxCom.termsPay(entryDate, custNo, facmNo, 0, terms, 0, titaVo);
			listBaTxVo = dBaTxCom.addByPayintDate(lBaTxVo, titaVo);
		} catch (LogicException e) {
			this.error("listBaTxVo ErrorMsg :" + e.getMessage());
		}

		this.info("listBaTxVo.size()=" + listBaTxVo.size());

		if (listBaTxVo.size() == 0) {
			if (cnt == 0) {
				printCm(1, 4, "*******    查無資料   ******");
			}

			return;
		}

		// 溢短繳
		int excessive = dBaTxCom.getExcessive().intValue() - dBaTxCom.getShortfall().intValue();
		// 帳管費 + 契變手續費
		int acctFee = dBaTxCom.getAcctFee().intValue() + dBaTxCom.getModifyFee().intValue();
		// 目前利率
		double intRate = dBaTxCom.getFitRate().doubleValue();

		// 未收本息 = 本金+利息 Principal + Interest
		// 違約金 有 但要扣成 0 BreachAmt
		// 溢短繳 UnPaidAmt
		// 合計 = 未收本息 + 違約金 - 溢短繳
		// 未還本金餘額
//				金額欄位 同下繳日的加總

//					不同戶號額度跳頁
//			if (cnt >= 1) {

//				this.newPage();

//			}

//			cnt++;

		// 列印地址

		setFont(1, 11);
		printCm(1, 2.5, "【限定本人拆閱，若無此人，請寄回本公司】");
		printCm(2, 3.5, custMain.getCurrZip3().trim() + custMain.getCurrZip2().trim());
		if ("A3".equals(reconcode)) {
			printCm(15, 3.5, "限  時  專  送");
		}
		printCm(2, 4.5, currAddress);

		int nameLength = 20;
		if (custMain.getCustName().length() < 20) {
			nameLength = custMain.getCustName().length();
		}

		printCm(2, 5.5, String.format("%07d", custNo) + "   " + custMain.getCustName().substring(0, nameLength));

		int top = 0;// 上下微調用
		double yy = 20;// 開始Y軸
		double h = 0.4;// 列高
		double l = 0;// 列數

		double y = top + yy;

		setFont(1, 14);

		printCm(10, 19, "放款本息攤還表暨繳息通知單", "C");

		setFont(1, 11);

		printCm(1.5, y, "製發日期：" + this.showRocDate(entdy, 1));
		printCm(16, y, repayCodeX(repayCode));

		y = top + yy + (++l) * h;

		printCm(1.5, y, "戶    號：" + String.format("%07d", Integer.valueOf(custNo)) + "-"
				+ String.format("%03d", Integer.valueOf(facmNo)) + "  目前利率：" + padStart(6, "" + intRate) + "%");

		y = top + yy + (++l) * h;

		printCm(1.5, y, "客戶名稱：" + custMain.getCustName().substring(0, nameLength));
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
		int Principal = 0;
		int Interest = 0;
		int BreachAmt = 0;
		int UnPaidAmt = 0;
		int LoanBal = 0;
		int tmpCount = 0;

		for (BaTxVo baTxVo : listBaTxVo) {

			tmpCount++;

			if (tmpCount == 7) {
				break;
			}

			this.info("baTxVo.getDataKind()=" + baTxVo.getDataKind());
			// 本金、利息
//			if (baTxVo.getDataKind() != 2) {
//				continue;
//			}
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

			this.info("tempDate=" + tempDate);
			this.info("BreachAmt=" + BreachAmt);
			this.info("Principal=" + Principal);
			this.info("Interest=" + Interest);
			this.info("LoanBal=" + LoanBal);
			this.info("UnPaidAmt=" + UnPaidAmt);
			this.info("excessive=" + excessive);
			this.info("acctFee=" + acctFee);
			this.info("intRate=" + intRate);

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

		} // loop -- batxCom

		l++;

		if ("L8101".equals(titaVo.getTxCode())) {
			y = top + yy + (++l) * h;
			this.printCm(1, y, "＊＊房貸客戶提醒：為維護您的權益，戶籍或通訊地址、電子信箱及連絡電話、或姓名、身份證統一編號等");
			y = top + yy + (++l) * h;
			this.printCm(1, y, "　　重要資訊有異動時，敬請洽詢公司服務人員或客戶服務部（０８００—０３１１１５）辦理變更。");
		}

		y = top + yy + (++l) * h;
		printCm(1, y, "＊＊舊繳息通知單作廢（以最新製發日期為準）。");

		// 滯繳通知單
//					this.print(1, 8,
//							"＊＊註：違約金暫計到" + transRocChinese(titaVo.getParam("EntryDate")) + "，若提前或延後繳款，請電話查詢　該違約金額");

		// 銀扣火險成功期款失敗通知單
//					this.print(1, 8,
//							"＊＊註：違約金暫計到" + transRocChinese(titaVo.getParam("EntryDate")) + "，若提前或延後繳款，請電話查詢　該違約金額");
//					this.print(1, 8, "＊＊您好！本月份扣款含年度火險、地震險保費、每月房貸期款，因您存款不足；");
//					this.print(1, 8, "　　請速將本期款匯入期款專用帳號。");
		// L4454-A： 銀扣連續扣款失敗通知
		// L4454-B： 銀扣火險成功期款失敗通知單
		// L4702-C： 本日有匯款轉帳且有滯繳
		if ("B".equals(conditionCode)) {
			y = top + yy + (++l) * h;
			this.printCm(1, y, "＊＊註：違約金暫計到" + transRocChinese(titaVo.getParam("EntryDate")) + "，若提前或延後繳款，請電話查詢　該違約金額");
			y = top + yy + (++l) * h;
			this.printCm(1, y, "＊＊您好！本月份扣款含年度火險、地震險保費、每月房貸期款，因您存款不足；");
			y = top + yy + (++l) * h;
			this.printCm(1, y, "　　請速將本期款匯入期款專用帳號。");
		}

		y = top + yy + (++l) * h;
		this.printCm(1, y, "＊＊新光銀行城內分行代號： 1030116");

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
		this.printCm(14, y, "製表人 " + empName);

		if (r.get(c).get("CustNo") != null) {
//			共用代碼檔0:個金1:企金2:企金自然人
			if ("0".equals(r.get(c).get("EntCode"))) {
				payIntAcct = "9510200" + String.format("%07d", Integer.valueOf(custNo));
				payPriAcct = "9510300" + String.format("%07d", Integer.valueOf(custNo));
			} else {
				payIntAcct = "9510100" + String.format("%07d", Integer.valueOf(custNo));
				payPriAcct = "9510100" + String.format("%07d", Integer.valueOf(custNo));
			}
		}

		printCm(4, 28, payIntAcct);
		printCm(14, 28, payPriAcct);

		if ("C".equals(conditionCode)) {

//				String EntryDate = r.get(c).get("EntryDate"); // 入帳日期
//				BigDecimal RepayAmt = parse.stringToBigDecimal(r.get(c).get("RepayAmt"));
//
//				if (RepayAmt.compareTo(new BigDecimal("0")) > 0) {
////							y = top + yy + (++l) * h;
//					this.printCm(1, 29.5,
//							"◎台端於　" + transRocChinese(EntryDate) + " 所匯之還本金$" + df1.format(RepayAmt) + "業已入帳無誤。");
//				}
		}

//		if (RepayAmt.compareTo(new BigDecimal("0")) > 0) {
		if ("A3".equals(reconcode)) {
			String EntryDate = r.get(c).get("EntryDate"); // 入帳日期
			BigDecimal RepayAmt = parse.stringToBigDecimal(r.get(c).get("RepayAmt"));

			this.printCm(10, 28.5, "◎台端於　" + transRocChinese(EntryDate) + " 所匯之還本金$" + df1.format(RepayAmt) + " 業已入帳無誤。",
					"C");
		}

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
