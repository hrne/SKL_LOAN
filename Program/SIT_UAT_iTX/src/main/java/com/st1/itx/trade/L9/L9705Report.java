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

	@Override
	public void printHeader() {

		this.setCharSpaces(0);

		// 明細起始列(自訂亦必須)
		this.setBeginRow(3);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(35);

	}

	public long exec(List<Map<String, String>> l9705List, TitaVo titaVo, TxBuffer txbuffer) throws LogicException {

		// 設定試算期數 2021-12-24 智偉新增
		int terms = 6; // 預設印6期

		if (titaVo.containsKey("Terms") && titaVo.getParam("Terms") != null) {
			try {
				terms = Integer.parseInt(titaVo.getParam("Terms"));
			} catch (Exception e) {
				terms = 6; // 若無法轉為數值,改為預設6
			}
		}

		this.info("L9705Report exec Terms = " + terms);

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

		this.openForm(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), tran + "A", rptitem, "inch,8.5,12", "P");

		String entdy = titaVo.getEntDy();

		String conditionCode = "";

		if (titaVo.get("CONDITION1") != null) {
			conditionCode = titaVo.get("CONDITION1");
		}

//		A.匯款轉帳，已入金，有欠繳-------入帳完成後，於應處理事項清單執行L4702。
//		B.銀扣火險成功，期款失敗----------L4454.銀扣失敗通知(一扣)

		if (l9705List.size() == 0) {

			this.setRptItem("放款本息攤還表暨繳息通知單(查無資料)");

			this.printCm(1, 4, "*******    查無資料   ******");

			this.info("查無資料 Return...");
		} else {
			int cnt = 0;
			this.info("l9705List size = " + l9705List.size());
			for (Map<String, String> tL9Vo : l9705List) {

				ArrayList<BaTxVo> lBaTxVo = new ArrayList<>();
				ArrayList<BaTxVo> listBaTxVo = new ArrayList<>();

				dBaTxCom.setTxBuffer(txbuffer);

				int custNo = 0;
				int facmNo = 0;
				int entryDate = parse.stringToInteger(titaVo.getParam("ENTDY"));
				String repayCode = "";
				String payIntAcct = "";
				String payPriAcct = "";
				DecimalFormat df1 = new DecimalFormat("#,##0");
				if (tL9Vo.get("CustNo") != null) {
					custNo = parse.stringToInteger(tL9Vo.get("CustNo"));
				}
				if (tL9Vo.get("FacmNo") != null) {
					facmNo = parse.stringToInteger(tL9Vo.get("FacmNo"));
				}
				if (tL9Vo.get("RepayCode") != null) {
					repayCode = tL9Vo.get("RepayCode");
				}
				
				// 檢查 CustNoticeCom 確認此戶此報表是否能產出
				// input parameter: CUSTNO
				if (!custNoticeCom.checkIsLetterSendable(titaVo.get("CUSTNO"), custNo, facmNo, "L9705", titaVo))
					continue;
				
//				if (tL9Vo.get("CustName") != null) {
//					custName = tL9Vo.get("CustName");
//				}

				CustMain custMain = custMainService.custNoFirst(custNo, custNo, titaVo);
				String currAddress = custNoticeCom.getCurrAddress(custMain, titaVo);

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
					lBaTxVo = dBaTxCom.termsPay(entryDate, custNo, facmNo, 0, terms, titaVo);
					listBaTxVo = dBaTxCom.addByPayintDate(lBaTxVo, titaVo);
				} catch (LogicException e) {
					this.error("listBaTxVo ErrorMsg :" + e.getMessage());
				}

				if (listBaTxVo.size() > 0) {
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
					if (cnt >= 1) {
						this.newPage();
					}

					cnt++;

					// 列印地址

					setFont(1, 14);
					printCm(1, 4, "【限定本人拆閱，若無此人，請寄回本公司】");
					printCm(2, 5, custMain.getCurrZip3().trim() + custMain.getCurrZip2().trim());
					printCm(2, 6, currAddress);

					int nameLength = 20;
					if (custMain.getCustName().length() < 20) {
						nameLength = custMain.getCustName().length();
					}

					printCm(2, 7,
							String.format("%07d", custNo) + "   " + custMain.getCustName().substring(0, nameLength));

					setFont(1, 11);

					int top = 0;// 上下微調用
					double yy = 20;// 開始Y軸
					double h = 0.4;// 列高
					double l = 0;// 列數

					double y = top + yy;
					printCm(1.5, y, "製發日期：" + this.showRocDate(entdy, 1));
					printCm(16, y, repayCodeX(repayCode));

					y = top + yy + (++l) * h;

					printCm(1.5, y,
							"戶    號：" + String.format("%07d", Integer.valueOf(custNo)) + "-"
									+ String.format("%03d", Integer.valueOf(facmNo)) + "  目前利率："
									+ padStart(6, "" + intRate) + "%");

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
					

					for (BaTxVo baTxVo : listBaTxVo) {
						// 本金、利息
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
						printCm(1, y, tempDate.substring(0, 3) + "/" + tempDate.substring(3, 5) + "/"
								+ tempDate.substring(5, 7));
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
						this.printCm(1, y,
								"＊＊註：違約金暫計到" + transRocChinese(titaVo.getParam("EntryDate")) + "，若提前或延後繳款，請電話查詢　該違約金額");
						y = top + yy + (++l) * h;
						this.printCm(1, y, "＊＊您好！本月份扣款含年度火險、地震險保費、每月房貸期款，因您存款不足；");
						y = top + yy + (++l) * h;
						this.printCm(1, y, "　　請速將本期款匯入期款專用帳號。");
						y = top + yy + (++l) * h;
						this.printCm(1, y, "＊＊新光銀行城內分行代號： 1030116");
					}

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

					printCm(4, 28, payIntAcct);
					printCm(14, 28, payPriAcct);

					if ("C".equals(conditionCode)) {

						String EntryDate = tL9Vo.get("EntryDate"); // 入帳日期
						BigDecimal RepayAmt = parse.stringToBigDecimal(tL9Vo.get("RepayAmt"));

						if (RepayAmt.compareTo(new BigDecimal("0")) > 0) {
//							y = top + yy + (++l) * h;
							this.printCm(1, 29, "◎台端於　" + transRocChinese(EntryDate) + " 所匯之還本金$" + df1.format(RepayAmt)
									+ "業已入帳無誤。");
						}
					}

				} else {
					if (cnt == 0) {
						printCm(1, 4, "*******    查無資料   ******");
					}
				}
			} // for
			
			if (cnt == 0) {
				printCm(1, 4, "*******    查無資料   ******");
			}
			
		} // else 

		// 關閉報表
		long sno = this.close();

		if (titaVo.get("selectTotal") == null || titaVo.get("selectTotal").equals(titaVo.get("selectIndex"))) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"),
					titaVo.getTxCode().isEmpty() ? "L9705" : titaVo.getTxCode() + "放款本息攤還表暨繳息通知單已完成", titaVo);
		}
		return sno;
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
