package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.springjpa.cm.L4510R3ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4510Report3")
@Scope("prototype")

public class L4510Report3 extends MakeReport {

	@Autowired
	public L4510R3ServiceImpl l4510R3ServiceImpl;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private CdCodeService cdCodeService;

	private int perfMonth = 0;
	private String procCode = "";
	private String repayCode = "";
	private String acctCode = "";
	private String acctItem = "";
	private int entryDate = 0;
	private int flag = 0;

//	每頁筆數
	private int pageIndex = 38;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		printHeaderP();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(9);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	public void printHeaderP() {
		this.print(-1, 1, "程式ID：" + "L4510Report3");
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-1, 130, "製表日期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-2, 1, "報　表：" + "L4510Report3");
		this.print(-2, 70, "扣薪媒體明細表", "C");
		this.print(-2, 130, "製表時間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-3, 130, "頁　　次：" + this.getNowPage(), "R");
		this.print(-4, 70, formatDateRoc(entryDate), "C");
		this.print(-5, 1, "業績年月：" + perfMonth);
		this.print(-5, 20, "流程別：" + procCode);
		this.print(-5, 40, "扣款代碼：" + repayCode);
		this.print(-5, 60, repayCodeX(repayCode));
		this.print(-6, 1, "業績科目：" + acctCode);
		this.print(-6, 20, acctItem);
		this.print(-7, 1, "戶號            戶名             員工代號    計息起迄日           扣款金額       本金      利息    違約金   欠繳本金   欠繳利息  暫收抵繳");
		this.print(-8, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public long exec(TitaVo titaVo1) throws LogicException {
		List<Map<String, String>> fnAllList = new ArrayList<>();

		long sno = 0;

		titaVo = titaVo1;
		this.info("L4510Report exec");

//		設定工作表名稱
//		String iENTDY = titaVo.get("ENTDY");
//		txExcel.setSheet("108.04", iENTDY.substring(1, 4) + "." + iENTDY.substring(4, 6));

		try {
			fnAllList = l4510R3ServiceImpl.findAll(entryDate, procCode, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L4510ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {

			perfMonth = parse.stringToInteger(fnAllList.get(0).get("F0")) - 191100;
			procCode = fnAllList.get(0).get("F1");
			repayCode = fnAllList.get(0).get("F2");
			acctCode = fnAllList.get(0).get("F3");
			acctItem = fnAllList.get(0).get("F4");
			entryDate = parse.stringToInteger(fnAllList.get(0).get("F5")) - 19110000;

			if (flag == 1) {
				this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4510Report3", "15日薪-扣薪媒體明細表", "", "A4", "L");
			} else {
				this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4510Report3", "非15日薪-扣薪媒體明細表", "", "A4", "L");
			}

			BigDecimal leglFee = BigDecimal.ZERO; // 違約金
			BigDecimal ovduAmt = BigDecimal.ZERO; // 滯繳本金
			BigDecimal ovduBal = BigDecimal.ZERO; // 滯繳利息
			BigDecimal tempAmt = BigDecimal.ZERO; // 暫收抵繳

//			by 流程別合計
			BigDecimal sumA1 = BigDecimal.ZERO;
			BigDecimal sumA2 = BigDecimal.ZERO;
			BigDecimal sumA3 = BigDecimal.ZERO;
			BigDecimal sumA4 = BigDecimal.ZERO;
			BigDecimal sumA5 = BigDecimal.ZERO;
			BigDecimal sumA6 = BigDecimal.ZERO;
			BigDecimal sumA7 = BigDecimal.ZERO;
//			by 報表總計
			BigDecimal sumB1 = BigDecimal.ZERO;
			BigDecimal sumB2 = BigDecimal.ZERO;
			BigDecimal sumB3 = BigDecimal.ZERO;
			BigDecimal sumB4 = BigDecimal.ZERO;
			BigDecimal sumB5 = BigDecimal.ZERO;
			BigDecimal sumB6 = BigDecimal.ZERO;
			BigDecimal sumB7 = BigDecimal.ZERO;

			int timeAs = 0, timeBs = 0, total = 0, i = 0, pageCnt = 0;
			for (int j = 1; j <= fnAllList.size(); j++) {

				i = j - 1;

				this.info("fnAllList.get(i)-------->" + fnAllList.get(i).toString());

				leglFee = BigDecimal.ZERO;
				ovduAmt = BigDecimal.ZERO;
				ovduBal = BigDecimal.ZERO;
				tempAmt = BigDecimal.ZERO;

				TempVo tempVo = new TempVo();
				tempVo = tempVo.getVo(fnAllList.get(i).get("F16"));

				if (tempVo.get("LeglFee") != null && tempVo.get("LeglFee").length() > 0) {
					leglFee = parse.stringToBigDecimal(tempVo.get("LeglFee"));
				}
				if (tempVo.get("OvduAmt") != null && tempVo.get("OvduAmt").length() > 0) {
					ovduAmt = parse.stringToBigDecimal(tempVo.get("OvduAmt"));
				}
				if (tempVo.get("OvduBal") != null && tempVo.get("OvduBal").length() > 0) {
					ovduBal = parse.stringToBigDecimal(tempVo.get("OvduBal"));
				}
				if (tempVo.get("TempAmt") != null && tempVo.get("TempAmt").length() > 0) {
					tempAmt = parse.stringToBigDecimal(tempVo.get("TempAmt"));
				}

				int lengthF9 = 8;
				if (fnAllList.get(i).get("F9").length() < 8) {
					lengthF9 = fnAllList.get(i).get("F9").length();
				}

//				1.每筆先印出明細
				this.print(1, 1,
						"                                                                                                                                                                               ");
				this.print(0, 1, FormatUtil.pad9(fnAllList.get(i).get("F6"), 7)); // 戶號
				this.print(0, 8, "-");
				this.print(0, 9, FormatUtil.pad9(fnAllList.get(i).get("F7"), 3));// 額度
				this.print(0, 12, "-");
				this.print(0, 13, FormatUtil.pad9(fnAllList.get(i).get("F8"), 3));// 撥款
				this.print(0, 17, fnAllList.get(i).get("F9").substring(0, lengthF9));// 戶名
				this.print(0, 34, fnAllList.get(i).get("F10"));// 員編
				this.print(0, 42, formatDate(parse.stringToInteger(fnAllList.get(i).get("F11"))));// 計息起
				this.print(0, 51, "-");
				this.print(0, 53, formatDate(parse.stringToInteger(fnAllList.get(i).get("F12"))));// 計息迄
				this.print(0, 72, format4Amt(parse.stringToBigDecimal(fnAllList.get(i).get("F13"))), "R");// 扣款金額
				this.print(0, 82, format4Amt(parse.stringToBigDecimal(fnAllList.get(i).get("F14"))), "R");// 本金
				this.print(0, 92, format4Amt(parse.stringToBigDecimal(fnAllList.get(i).get("F15"))), "R");// 利息
//				僅期款需顯示
				this.print(0, 102, format4Amt(leglFee), "R");// 違約金
				this.print(0, 112, format4Amt(ovduAmt), "R");// 欠繳本金
				this.print(0, 122, format4Amt(ovduBal), "R");// 欠繳利息
				this.print(0, 132, format4Amt(tempAmt), "R");// 暫收抵繳

				timeBs++;
//				by 報表合計
				sumB1 = sumB1.add(parse.stringToBigDecimal(fnAllList.get(i).get("F13")));
				sumB2 = sumB2.add(parse.stringToBigDecimal(fnAllList.get(i).get("F14")));
				sumB3 = sumB3.add(parse.stringToBigDecimal(fnAllList.get(i).get("F15")));
				sumB4 = sumB4.add(leglFee);
				sumB5 = sumB5.add(ovduAmt);
				sumB6 = sumB6.add(ovduBal);
				sumB7 = sumB7.add(tempAmt);

//				流程別筆數統計
				timeAs++;
//				by 流程別合計
				sumA1 = sumA1.add(parse.stringToBigDecimal(fnAllList.get(i).get("F13")));
				sumA2 = sumA2.add(parse.stringToBigDecimal(fnAllList.get(i).get("F14")));
				sumA3 = sumA3.add(parse.stringToBigDecimal(fnAllList.get(i).get("F15")));
				sumA4 = sumA4.add(leglFee);
				sumA5 = sumA5.add(ovduAmt);
				sumA6 = sumA6.add(ovduBal);
				sumA7 = sumA7.add(tempAmt);

//				全部筆數統計
				total++;

//				每頁筆數相加
				pageCnt++;

//				2.再與下一筆比較，決定是否換行或換頁
				if (j != fnAllList.size()) {
//					年月不同則跳頁，並且累計歸零
					if (!fnAllList.get(i).get("F1").equals(fnAllList.get(j).get("F1")) || !fnAllList.get(i).get("F2").equals(fnAllList.get(j).get("F2"))
							|| !fnAllList.get(i).get("F3").equals(fnAllList.get(j).get("F3"))) {
						this.info("RepayBank Not Match...");
						this.info("F1 = " + fnAllList.get(i).get("F1") + "," + fnAllList.get(j).get("F1"));
						this.info("F2 = " + fnAllList.get(i).get("F2") + "," + fnAllList.get(j).get("F2"));
						this.info("F3 = " + fnAllList.get(i).get("F3") + "," + fnAllList.get(j).get("F3"));
//						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
//						this.print(1, 51, FormatUtil.pad9("" + timeAs, 7));
//						this.print(0, 72, format4Amt(sumA1), "R");// 扣款金額
//						this.print(0, 82, format4Amt(sumA2), "R");// 本金
//						this.print(0, 92, format4Amt(sumA3), "R");// 利息
//						this.print(0, 102, format4Amt(sumA4), "R");// 違約金
//						this.print(0, 112, format4Amt(sumA5), "R");// 欠繳本金
//						this.print(0, 122, format4Amt(sumA6), "R");// 欠繳利息
//						this.print(0, 132, format4Amt(sumA7), "R");// 暫收抵繳
						
						
						
						this.print(1, 1,
								"--------------------------------------------------------------------------------------------------------------------------------------------------------");

						this.print(1, 44, "總　計：");
						this.print(0, 15, "" + perfMonth);
						this.print(0, 25, procCode);
						this.print(0, 51, FormatUtil.pad9("" + timeBs, 7));
						this.print(0, 72, format4Amt(sumB1), "R");// 扣款金額
						this.print(0, 82, format4Amt(sumB2), "R");// 本金
						this.print(0, 92, format4Amt(sumB3), "R");// 利息
						this.print(0, 102, format4Amt(sumB4), "R");// 違約金
						this.print(0, 112, format4Amt(sumB5), "R");// 欠繳本金
						this.print(0, 122, format4Amt(sumB6), "R");// 欠繳利息
						this.print(0, 132, format4Amt(sumB7), "R");// 暫收抵繳 this.print(1, 1,
						
//						扣除合計的行數
						this.print(pageIndex - pageCnt - 2, 70, "=====續下頁=====", "C");

						timeAs = 0;
						sumA1 = BigDecimal.ZERO;
						sumA2 = BigDecimal.ZERO;
						sumA3 = BigDecimal.ZERO;
						sumA4 = BigDecimal.ZERO;
						sumA5 = BigDecimal.ZERO;
						sumA6 = BigDecimal.ZERO;
						sumA7 = BigDecimal.ZERO;

						
						
//						若流程別相同則需出總計
//						if (!fnAllList.get(i).get("F1").equals(fnAllList.get(j).get("F1"))) {
//							this.print(1, 1,
//									"--------------------------------------------------------------------------------------------------------------------------------------------------------");
//
//							this.print(1, 44, "總　計：");
//							this.print(0, 15, "" + perfMonth);
//							this.print(0, 25, procCode);
//							this.print(0, 51, FormatUtil.pad9("" + timeBs, 7));
//							this.print(0, 72, format4Amt(sumB1), "R");// 扣款金額
//							this.print(0, 82, format4Amt(sumB2), "R");// 本金
//							this.print(0, 92, format4Amt(sumB3), "R");// 利息
//							this.print(0, 102, format4Amt(sumB4), "R");// 違約金
//							this.print(0, 112, format4Amt(sumB5), "R");// 欠繳本金
//							this.print(0, 122, format4Amt(sumB6), "R");// 欠繳利息
//							this.print(0, 132, format4Amt(sumB7), "R");// 暫收抵繳 this.print(1, 1,
////							扣除總計合計的行數 +1 
//							this.print(pageIndex - pageCnt - 2, 70, "=====報表結束=====", "C");
//
//							timeBs = 0;
//							sumB1 = BigDecimal.ZERO;
//							sumB2 = BigDecimal.ZERO;
//							sumB3 = BigDecimal.ZERO;
//							sumB4 = BigDecimal.ZERO;
//							sumB5 = BigDecimal.ZERO;
//							sumB6 = BigDecimal.ZERO;
//							sumB7 = BigDecimal.ZERO;
//
//						}

						perfMonth = parse.stringToInteger(fnAllList.get(j).get("F0")) - 191100;
						procCode = fnAllList.get(j).get("F1");
						repayCode = fnAllList.get(j).get("F2");
						acctCode = fnAllList.get(j).get("F3");
						acctItem = fnAllList.get(j).get("F4");
						entryDate = parse.stringToInteger(fnAllList.get(j).get("F5")) - 19110000;

						pageCnt = 0;
						this.newPage();
						continue;
					} // if

//					每頁第38筆 跳頁 
					if (pageCnt == pageIndex) {
						this.print(pageIndex - pageCnt + 1, 70, "=====續下頁=====", "C");

						pageCnt = 0;
						this.newPage();
						continue;
					}
				} else {
//				3.若為最後一筆，則固定產出小計、總計、報表合計
					if (total == fnAllList.size()) {
						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
						this.print(1, 51, FormatUtil.pad9("" + timeAs, 7));
						this.print(0, 72, format4Amt(sumA1), "R");// 扣款金額
						this.print(0, 82, format4Amt(sumA2), "R");// 本金
						this.print(0, 92, format4Amt(sumA3), "R");// 利息
						this.print(0, 102, format4Amt(sumA4), "R");// 違約金
						this.print(0, 112, format4Amt(sumA5), "R");// 欠繳本金
						this.print(0, 122, format4Amt(sumA6), "R");// 欠繳利息
						this.print(0, 132, format4Amt(sumA7), "R");// 暫收抵繳
						this.print(1, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");

						this.print(1, 44, "總　計：");
						this.print(0, 15, "" + perfMonth);
						this.print(0, 25, procCode);
						this.print(0, 51, FormatUtil.pad9("" + timeBs, 7));
						this.print(0, 72, format4Amt(sumB1), "R");// 扣款金額
						this.print(0, 82, format4Amt(sumB2), "R");// 本金
						this.print(0, 92, format4Amt(sumB3), "R");// 利息
						this.print(0, 102, format4Amt(sumB4), "R");// 違約金
						this.print(0, 112, format4Amt(sumB5), "R");// 欠繳本金
						this.print(0, 122, format4Amt(sumB6), "R");// 欠繳利息
						this.print(0, 132, format4Amt(sumB7), "R");// 暫收抵繳 this.print(1, 1,
//						扣除總計合計的行數 +1 
						this.print(pageIndex - pageCnt - 2, 70, "=====報表結束=====", "C");
					}
				}
			}
			sno = this.close();
			this.toPdf(sno);

		}
		return sno;
	}

	public long exec(int iEntryDate, List<String> iProcCode, int flags, TitaVo titaVo) throws LogicException {
		entryDate = iEntryDate;
		flag = flags;

		for (int i = 0; i < iProcCode.size(); i++) {
			if (i == 0) {
				procCode = iProcCode.get(0);
			} else {
				procCode += "," + iProcCode.get(i);
			}
		}

		return exec(titaVo);
	}

	private String formatDate(int date) {
		String result = "";

		if (date > 19110000) {
			date = date - 19110000;
		}
		result = FormatUtil.pad9("" + date, 7);

		result = result.substring(0, 3) + "/" + result.substring(3, 5) + "/" + result.substring(5);

		return result;
	}

	private String formatDateRoc(int date) {
		String result = "";

		if (date > 19110000) {
			date = date - 19110000;
		}
		result = FormatUtil.pad9("" + date, 7);

		result = result.substring(0, 3) + "年" + result.substring(3, 5) + "月" + result.substring(5) + "日";

		return result;
	}

	private String format4Amt(BigDecimal amt) throws LogicException {
		String result = "";
		DecimalFormat df1 = new DecimalFormat("#,##0");

//		=0的顯示空白
		if (amt.compareTo(BigDecimal.ZERO) > 0) {
			result = df1.format(amt);
		}

		return result;
	}

	private String repayCodeX(String repayCode) {
		String result = "";
//		1:扣薪件;2:特約件;3:滯繳件;4:人事特約件;5:房貸扣薪件
		switch (repayCode) {
		case "1":
			result = "扣薪件";
			break;
		case "2":
			result = "特約件";
			break;
		case "3":
			result = "滯繳件";
			break;
		case "4":
			result = "人事特約件";
			break;
		case "5":
			result = "房貸扣薪件";
			break;
		}

		return result;
	}

	private String limitLength(String str, int pos) {
		byte[] input = str.getBytes();

		int inputLength = input.length;

		this.info("str ..." + str);
		this.info("inputLength ..." + inputLength);

		int resultLength = inputLength;

		if (inputLength > pos) {
			resultLength = pos;
		}

		String result = "";

		if (resultLength > 0) {
			byte[] resultBytes = new byte[resultLength];
			System.arraycopy(input, 0, resultBytes, 0, resultLength);
			result = new String(resultBytes);
		}

		return result;
	}
}
