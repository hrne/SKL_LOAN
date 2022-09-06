package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM035ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM035Report extends MakeReport {

	@Autowired
	LM035ServiceImpl lM035ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	private static final BigDecimal million = new BigDecimal(1000000);
	private static final BigDecimal hundred = new BigDecimal(100);

	@Override
	public void printTitle() {

	}

	int col = 2;

	public void exec(TitaVo titaVo) throws LogicException {
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM035", "地區逾放比", "LM035地區逾放比", "LM035地區逾放比.xlsx", "10804");
		int yearSeasonWalker = (parse.stringToInteger(titaVo.get("ENTDY")) + 19110000) / 100;
		int yearSeasonWalkTarget = 0;

		List<Integer> yearSeason = new ArrayList<Integer>();
		this.info("entdy1 = " + yearSeasonWalker);

		// yearSeason:
		// 包括出表當月,
		// 出到 5 年前的Q4為止
		// 假如entdy1是 202111, 則出表範圍為:

		// 202111
		// 202109
		// 202106
		// 202103
		// 202012
		// 202009
		// 202006
		// 202003
		// 201912
		// 201812
		// 201712
		// 201612

		// 加入當月
		yearSeason.add(yearSeasonWalker);

		// 先向前跳到這個月所屬的Q
		this.info("Start from: " + yearSeasonWalker);
		this.info("Shift: +" + yearSeasonWalker / 100 % 3);
		yearSeasonWalker += yearSeasonWalker / 100 % 3;

		// 回去求 n 及 n-1 年的所有Q
		yearSeasonWalkTarget = yearSeasonWalker - 100;
		yearSeasonWalkTarget -= yearSeasonWalkTarget % 100;

		this.info("2 year ago: " + yearSeasonWalkTarget);

		do {

			if (yearSeasonWalker % 100 == 3) {
				// 跳到前一年
				yearSeasonWalker -= 91;
			} else {
				// 繼續在今年
				yearSeasonWalker -= 3;
			}

			yearSeason.add(yearSeasonWalker);

		} while (yearSeasonWalker > yearSeasonWalkTarget);

		// 這時候 yearSeasonWalker 會停在 n-2 年Q4, 且已經加入yearSeason
		// 再加上前三年即可

		for (int i = 1; i <= 3; i++) {
			yearSeasonWalker -= 100;
			yearSeason.add(yearSeasonWalker);
		}

		this.info("yearSeason = " + yearSeason);

		List<Map<String, String>> LM035List = null;

		// iterate 時倒過來, 在 excel 上輸出才會是愈新的年份在愈右
		for (int i = yearSeason.size() - 1; i >= 0; i--) {

			// 年月輸出
			// 只有最後一年會是 YYYMM
			// 其他都是 YYYQQ
			if (i == 0) {
				makeExcel.setValue(2, col, yearSeason.get(i) - 191100);
			} else {
				int year = yearSeason.get(i) / 100 - 1911;
				int month = yearSeason.get(i) % 100;
				makeExcel.setValue(2, col, year + "Q" + ((month - 1) / 3 + 1));
			}

			// 產年月下方的數字
			try {
				LM035List = lM035ServiceImpl.findAll(titaVo, yearSeason.get(i));
				exportExcel(titaVo, LM035List, i);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LM035ServiceImpl.testExcel error = " + errors.toString());
			}
			col++;
		}

		// 最後寫一下最右邊的說明欄

		makeExcel.setValue(1, yearSeason.size() + 2, "單位：百萬元");
		makeExcel.setValue(2, yearSeason.size() + 2, "放款餘額");

		makeExcel.close();
		// makeExcel.toExcel(sno);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList, int timesLeft) throws LogicException {
		this.info("LM035Report exportExcel");
		BigDecimal ovduBal = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		int row = 3;
		int count = 1;

		for (Map<String, String> tLDVo : LDList) {

			// 原本的做法是用 excel format "0.00%", 但實際輸出時不會穩定輸出百分比格式
			// 這邊改成完整用字串組好後輸出, 實際輸出後 excel grids 不會跳綠箭頭

			makeExcel.setValue(row, col, getBigDecimal(tLDVo.get("F5")).multiply(hundred).setScale(2) + "%", "C");
			if (timesLeft == 0) {
				// 最後一次輸出 (產表當年月份的資料)
				makeExcel.setValue(row, col + 1, computeDivide(getBigDecimal(tLDVo.get("F2")), million, 2), "#,##0.00", "R");
			}
			total = total.add(getBigDecimal(tLDVo.get("F2")));
			ovduBal = ovduBal.add(getBigDecimal(tLDVo.get("F3"))).add(getBigDecimal(tLDVo.get("F4")));
			row++;
			if (count == LDList.size()) {
				if (total.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal division = computeDivide(ovduBal, total, 4);
					this.info("Print Avg !!!" + "    row = " + row + "    col = " + col + "    value = " + division);
					makeExcel.setValue(row, col, division.multiply(hundred).setScale(2) + "%", "C");
				} else {
					this.info("Print Avg0 !!!" + "    row = " + row + "    col = " + col + "    value = " + BigDecimal.ZERO);
					makeExcel.setValue(row, col, BigDecimal.ZERO.setScale(2) + "%", "C");
				}
			}
			count++;
		}

		if (timesLeft == 0) {
			// 最後的最後, 輸出放款餘額的累積
			makeExcel.setValue(row, col + 1, computeDivide(total, million, 0), "#,##0", "R");
		}
	}

}
