package com.st1.itx.trade.L4;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L4452Report")
@Scope("prototype")

public class L4452Report extends MakeReport {
	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	// 明細資料容器
	private ArrayList<OccursList> occursList = new ArrayList<>();

	private String repayBank = "";
	// 每頁筆數
	private int pageIndex = 38;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		printHeaderP();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(54);
	}

	public void printHeaderP() {
		this.print(-1, 1, "程式ID：" + "L4452");
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");
		String tim = String.valueOf(Integer.parseInt(dateUtil.getNowStringBc().substring(2, 4)));
//		月/日/年(西元後兩碼)
		this.print(-1, 130, "日　　期：" + dateUtil.getNowStringBc().substring(4, 6) + "/" + dateUtil.getNowStringBc().substring(6, 8) + "/" + tim, "R");
		this.print(-2, 1, "報　表：" + "L4452");
		this.print(-2, 70, "銀扣媒體檔未產出清單", "C");
		this.print(-2, 130, "時　　間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R");
		this.print(-3, 1, "扣款銀行：" + repayBank);
		this.print(-3, 130, "頁　　次：" + this.getNowPage(), "R");
//		this.print(-4, 1,
//				"入帳日期   戶號   額度 撥款    繳息迄日   應繳日   還款類別           應扣金額        暫收抵繳金額           扣款金額 媒體   會計日期");
		this.print(-4, 1, "戶號   額度         戶名                還款類別                扣款金額    摘要");
		this.print(-5, 1, "--------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L4452Report exec");

		if (occursList == null) {
			this.info("occursList = null ...");
			return;
		}

		this.info("occursList.size = " + occursList.size());

		// 排序資料 銀行別 戶號 額度 序號
		sort(occursList);

		if (occursList.size() > 0) {

			repayBank = occursList.get(0).get("OORepayBank");
			this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4452", "銀扣媒體檔未產出清單", "", "A4", "L");

			int i = 0, pageCnt = 0;
			for (int j = 1; j <= occursList.size(); j++) {

				i = j - 1;

//				每頁筆數相加
				pageCnt++;

				DecimalFormat df1 = new DecimalFormat("#,##0");

				this.info("occursList.get(i)-------->" + occursList.get(i).toString());
//				1.每筆先印出明細
				this.print(1, 1,
						"                                                                                                                                                                               ");
				this.print(0, 1, FormatUtil.pad9(occursList.get(i).get("OOCustNo"), 7));// 戶號
				this.print(0, 8, "-");
				this.print(0, 9, FormatUtil.pad9(occursList.get(i).get("OOFacmNo"), 3));// 額度
//				this.print(0, 12, "-");
//				this.print(0, 13, FormatUtil.pad9(occursList.get(i).get("OOBormNo"), 3));// 撥款
				this.print(0, 20, limitLength(occursList.get(i).get("OOCustName"), 20));// 戶名
				this.print(0, 40, occursList.get(i).get("OORepayType"));// 還款類別
				this.print(0, 70, df1.format(parse.stringToBigDecimal(occursList.get(i).get("OORepayAmt"))), "R");// 還款金額
				this.print(0, 75, occursList.get(i).get("OONote"));// 摘要

				if (j != occursList.size()) {
//					年月不同則跳頁，並且累計歸零
					repayBank = occursList.get(j).get("OORepayBank");
					if (!occursList.get(i).get("OORepayBank").equals(repayBank)) {
						this.info("RepayBank Not Match...");
						pageCnt = 0;
						this.newPage();
						continue;
					}
//					每頁第42筆 跳頁 
					if (pageCnt == 42) {
						this.print(1, 70, "=====續下頁=====", "C");

						pageCnt = 0;
						this.newPage();
						continue;
					}
				} else {
//				3.若為最後一筆，則固定產出小計、總計、報表合計
//					扣除總計合計的行數 +1 
					this.print(pageIndex - pageCnt - 2, 70, "=====報表結束=====", "C");
				}

			}
			long sno = this.close();
			this.toPdf(sno);
		}
	}

	public void doReport(ArrayList<OccursList> doReportOccurs, TitaVo titaVo) throws LogicException {
		this.info("doReportOccurs = " + doReportOccurs);
		occursList = doReportOccurs;

		this.exec(titaVo);
	}

	public void sort(ArrayList<OccursList> doReportOccurs) throws LogicException {

		// RepayBank ,CustNo ,FacmNo ,BormNo
		doReportOccurs.sort((c1, c2) -> {
			int result = 0;
			if (c1.get("OORepayBank") != c2.get("OORepayBank")) {
				result = Integer.valueOf(c1.get("OORepayBank").compareTo(c2.get("OORepayBank")));
			} else if (c1.get("OOCustNo") != c2.get("OOCustNo")) {
				result = Integer.valueOf(c1.get("OOCustNo").compareTo(c2.get("OOCustNo")));
			} else if (c1.get("OOFacmNo") != c2.get("OOFacmNo")) {
				result = Integer.valueOf(c1.get("OOFacmNo").compareTo(c2.get("OOFacmNo")));
			} else {
				result = 0;
			}

			return result;
		});

		this.occursList = doReportOccurs;
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
