package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9740ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9740Report extends MakeReport {

	@Autowired
	L9740ServiceImpl l9740ServiceImpl;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	String txcd = "L9740";
	String txname = "公會無自用住宅利率報送 ";
	BigDecimal rate = null;

	@Override
	public void printHeader() {
		this.print(1, 0, " ");
		this.setBeginRow(1);
		this.setMaxRows(45);
	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @return
	 * @throws LogicException
	 *
	 * 
	 */
	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9740 exec");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String reportItem = txname;
		String brno = titaVo.getBrno();
		String security = "";
		String pageSize = "A4";
		String pageOrientation = "P";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(reportItem).setSecurity(security).setRptSize(pageSize).setPageOrientation(pageOrientation)
				.build();
		// 開啟報表
		this.open(titaVo, reportVo);

		List<Map<String, String>> listL9740Data1 = null;

		List<Map<String, String>> listL9740Data2 = null;

		List<Map<String, String>> listL9740Data3 = null;

		int drawDownDateA1 = Integer.valueOf(titaVo.getParam("DrawDownDateA1")) + 19110000;
		int drawDownDateA2 = Integer.valueOf(titaVo.getParam("DrawDownDateA2")) + 19110000;
		String acctCodeA = titaVo.getParam("AcctCodeA");
		String renewFlagA = titaVo.getParam("RenewFlagA");

		int drawDownDateB1 = Integer.valueOf(titaVo.getParam("DrawDownDateB1")) + 19110000;
		String acctCodeB = titaVo.getParam("AcctCodeB");
		String statusB = titaVo.getParam("StatusB");

		int drawDownDateC1 = Integer.valueOf(titaVo.getParam("DrawDownDateC1")) + 19110000;
		String acctCodeC = titaVo.getParam("AcctCodeC");
		String statusC = titaVo.getParam("StatusC");
		BigDecimal rateC = new BigDecimal(titaVo.getParam("RateC"));

		try {

			// Q9309141 新撥款之戶號
			listL9740Data1 = l9740ServiceImpl.findPage1(titaVo, drawDownDateA1, drawDownDateA2, acctCodeA, renewFlagA);

			// Q9309142 續期放款利率 最低、最高
			listL9740Data2 = l9740ServiceImpl.findPage2(titaVo, drawDownDateB1, acctCodeB, statusB);

			// Q9309143 利率超過 X.XX% 之借戶
			listL9740Data3 = l9740ServiceImpl.findPage3(titaVo, drawDownDateC1, acctCodeC, statusC, rateC);

			exportData(listL9740Data1, listL9740Data2, listL9740Data3);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		// 關閉報表
		this.close();

		boolean result = true;

		if (listL9740Data1.size() == 0 && listL9740Data2.size() == 0 && listL9740Data3.size() == 0) {
			result = false;
		}

		return result;

	}

	private void exportData(List<Map<String, String>> listL9740Data1, List<Map<String, String>> listL9740Data2,
			List<Map<String, String>> listL9740Data3) throws LogicException {

		this.setFont(1, 12);

		String date = this.showBcDate(dateUtil.getNowStringBc(), 1);
		String time = dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4)
				+ ":" + dateUtil.getNowStringTime().substring(4, 6);
		String name = "Q9309141  新撥款之戶號";
		String page = "PAGE   " + 1;// 暫時固定

		String titleText = date + "  " + time + "  " + name + "  " + page;
		this.print(1, 3, titleText);

		if (listL9740Data1.size() > 0) {
			// 因此表User說目前還沒看到過有資料，所以暫不確定有資料的格式(各個欄位的項目)
		} else {
			this.print(1, 10, "利率");

			this.print(3, 3, "No records in query report.");
		}

		this.print(1, 3, "＊＊＊＊＊＊＊＊    End of report    ＊＊＊＊＊＊＊＊ ");

		/*-----------------------------------------------------------------------------*/

		this.newPage();

		date = this.showBcDate(dateUtil.getNowStringBc(), 1);
		time = dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":"
				+ dateUtil.getNowStringTime().substring(4, 6);
		name = "Q9309142  續期放款利率  最低、最高";

		titleText = date + "  " + time + "  " + name + "  " + page;
		this.print(1, 3, titleText);

		if (listL9740Data2.size() > 0) {
			this.print(1, 10, "利率");

			this.print(2, 3, "FINAL TOTALS");
			BigDecimal minRate = listL9740Data2.get(0).get("minRate").isEmpty() ? new BigDecimal("0.0")
					: new BigDecimal(listL9740Data2.get(0).get("minRate"));
			BigDecimal maxRate = listL9740Data2.get(0).get("maxRate").isEmpty() ? new BigDecimal("0.0")
					: new BigDecimal(listL9740Data2.get(0).get("maxRate"));
			this.print(1, 3, "MIN  " + fillUpWord(String.valueOf(minRate), 6, "0", "R"));
			this.print(1, 3, "MAX  " + fillUpWord(String.valueOf(maxRate), 6, "0", "R"));
			this.print(1, 1, " ");

		} else {
			this.print(1, 10, "利率");

			this.print(3, 3, "No records in query report.");
		}

		this.print(1, 3, "＊＊＊＊＊＊＊＊    End of report    ＊＊＊＊＊＊＊＊ ");

		/*-----------------------------------------------------------------------------*/

		this.newPage();

		date = this.showBcDate(dateUtil.getNowStringBc(), 1);
		time = dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":"
				+ dateUtil.getNowStringTime().substring(4, 6);
		name = "Q9309143  利率超過 " + this.rate + "% 之借戶";

		titleText = date + "  " + time + "  " + name + "  " + page;
		this.print(1, 3, titleText);

		this.print(1, 1, " ");
		this.print(1, 8, "戶號");
		this.print(0, 18, "額度");
		this.print(0, 25, "撥款");
		this.print(0, 32, "撥款日期");
		this.print(0, 46, "撥款金額");
		this.print(0, 56, "利率");
		this.print(0, 64, "繳息迄日");
		this.print(1, 1, " ");

		if (listL9740Data3.size() > 0) {

			for (Map<String, String> r3 : listL9740Data3) {

				this.print(1, 8, r3.get("CustNo"));
				this.print(0, 18, r3.get("FacmNo"));
				this.print(0, 25, r3.get("BormNo"));
				this.print(0, 32, this.showBcDate(r3.get("DrawdownDate"), 0));
				this.print(0, 46, this.formatAmt(r3.get("DrawdownAmt"), 0));
				BigDecimal rate = r3.get("StoreRate").isEmpty() ? BigDecimal.ZERO : new BigDecimal(r3.get("StoreRate"));
				this.print(0, 56, fillUpWord(String.valueOf(rate), 6, "0", "R"));
				this.print(0, 64, this.showBcDate(r3.get("NextPayIntDate"), 0));

			}

			this.print(1, 1, " ");

		} else {

			this.print(3, 3, "No records in query report.");
		}

		this.print(1, 3, "＊＊＊＊＊＊＊＊    End of report    ＊＊＊＊＊＊＊＊ ");

	}

	/**
	 * 文字內容處理
	 * 
	 * @param text 文字
	 * @param all  上限位數
	 * @param word 要補滿的符號或文字(單字佳)
	 * @param pos  向左補齊(L)/向右補齊(R) 如果文字字數大於上限位數 自動截斷
	 *
	 */

	private String fillUpWord(String text, int allCount, String word, String pos) {

		int[] num = new int[text.length()];

		String tmpText = "";
		int strCode = 0;

		for (int i = 0; i < num.length; i++) {
			strCode = text.charAt(i);
			tmpText = text.substring(i, i + 1);
			// 中文字數為2，全形為2，非中文為1
			if (tmpText.matches("[\\u4E00-\\u9FA5]+")) {
				num[i] = 2;
			} else if ((strCode > 65248) || (strCode == 12288)) {
				num[i] = 2;
			} else {
				num[i] = 1;
			}

		}

		// 計算用
		int tmpAllCount = 0;
		// 截斷位置用
		int tmpI = 0;
		// 最終文字數
		int tmpHaveCount = 0;

		for (int i = 0; i < num.length; i++) {
			tmpAllCount = tmpAllCount + num[i];
			if (allCount >= tmpAllCount) {
				tmpI = i;
				tmpHaveCount = tmpAllCount;
			}
		}

		String str = text.substring(0, tmpI + 1);
		String lstr = "";

		// 總字數 減去 目前有的字數 = 空的字數
		int emptyCount = 0;
		emptyCount = allCount - tmpHaveCount;

		// 重新組合
		for (int i = 0; i < emptyCount; i++) {
			if (pos == "L") {
				lstr += word;
			} else if (pos == "R") {
				str += word;
			}
		}
		if (pos == "L") {
			str = lstr + text;
		}

		return str;
	}

}
