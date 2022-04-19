package com.st1.itx.trade.LM;

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
import com.st1.itx.db.service.springjpa.cm.LM018ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM018Report extends MakeReport {

	@Autowired
	public LM018ServiceImpl lM018ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;
	
	
	// 調整的訣竅:
	// 先對準表格, 再對準字
	// 字的位置會被表格Shift影響到
	
	// 字體大小
	private int fontSize = 10;
	
	// 最左欄寬度
	private int shiftFromLegends = 22;
	
	// 每季寬度
	private int shiftPerSeason = 20;
	
	// 主表格與最後小計表格之間的留白
	private int shiftBeforeSum = 14;
	
	// 主表格餘額shift (負數:因為是先向右畫好表格再回頭填數字)
	private int shiftMainBal = -14;
	
	// 主表格利收shift (負數:因為是先向右畫好表格再回頭填數字)
	private int shiftMainInt = -4;
	
	// 主表格年月shift
	private int shiftYearMonth = 11;
	
	// 小計表格餘額shift
	private int shiftSumBal = 9;
	
	// 小計表格利收shift
	private int shiftSumInt = 25;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		printHeaderL();

		this.setBeginRow(8);

		this.setMaxRows(50);
	}

	public void printHeaderL() {

		this.print(-3, 70, "專案放款餘額及利收明細", "C");

		this.setFontSize(fontSize);

		this.print(-5, 138, "機密等級：密", "R");
		this.print(-6, 138, "單位：億元　", "R");

		this.setCharSpaces(0);

	}

	private static enum subject {
		AA("AA", -17, false), IA("IA", -20, true), IB("IB", -23, true), IC("IC", -26, true), ID("ID", -29, true),
		IF("IF", -32, true), IH("IH", -35, true), ZZ("ZZ", -38, true);

		String name;
		int printY;
		boolean showsGroupIntOnly;

		BigDecimal lastBal = BigDecimal.ZERO;
		BigDecimal lastInt = BigDecimal.ZERO;

		subject(String name, int printY, boolean showsGroupIntOnly) {
			this.name = name;
			this.printY = printY;
			this.showsGroupIntOnly = showsGroupIntOnly;
		}

		public static subject get(String keyword) {
			for (subject s : subject.values()) {
				if (s.name.equals(keyword)) {
					return s;
				}
			}

			return null;
		}
	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM018", "專案放款餘額及利息收入", "密", "", "L");

		List<Map<String, String>> LM018List = null;
		try {
			LM018List = lM018ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lM018ServiceImpl.findAll error = " + errors.toString());
		}

		printHeader();

		// form length / print position calculating

		int xPivot = 1;
		int xShift = 0;

		// new form trigger

		String lastYearMonth = "";

		// draw start of the form

		this.print(-9,  xPivot + xShift, "┌──────────");
		this.print(-10, xPivot + xShift, "│　　　　　　　　　　");
		this.print(-11, xPivot + xShift, "│　　　　　　　　　　");
		this.print(-12, xPivot + xShift, "├──────────");
		this.print(-13, xPivot + xShift, "│　　　　　　　　　　");
		this.print(-14, xPivot + xShift, "│　　　　　　　　　　");
		this.print(-15, xPivot + xShift, "├──────────");
		this.print(-16, xPivot + xShift, "│　　　　　　　　　　");
		this.print(-17, xPivot + xShift, "│　　首次購物貸款　　");
		this.print(-18, xPivot + xShift, "├──────────");
		this.print(-19, xPivot + xShift, "│　IA　1200億　　　　");
		this.print(-20, xPivot + xShift, "│　青年優惠房屋貸款　");
		this.print(-21, xPivot + xShift, "├──────────");
		this.print(-22, xPivot + xShift, "│　IB　4000億　　　　");
		this.print(-23, xPivot + xShift, "│　優惠購屋專案貸款　");
		this.print(-24, xPivot + xShift, "├──────────");
		this.print(-25, xPivot + xShift, "│　IC　續辦2000億　　");
		this.print(-26, xPivot + xShift, "│　優惠購屋專案貸款　");
		this.print(-27, xPivot + xShift, "├──────────");
		this.print(-28, xPivot + xShift, "│　ID、IE續辦4800億　");
		this.print(-29, xPivot + xShift, "│　優惠購屋專案貸款　");
		this.print(-30, xPivot + xShift, "├──────────");
		this.print(-31, xPivot + xShift, "│　IF、IG續辦6000億　");
		this.print(-32, xPivot + xShift, "│　優惠購屋專案貸款　");
		this.print(-33, xPivot + xShift, "├──────────");
		this.print(-34, xPivot + xShift, "│　IH、II增撥4000億　");
		this.print(-35, xPivot + xShift, "│　優惠購屋專案貸款　");
		this.print(-36, xPivot + xShift, "├──────────");
		this.print(-37, xPivot + xShift, "│　　　　　　　　　　");
		this.print(-38, xPivot + xShift, "│　921 重建優惠房貸　");
		this.print(-39, xPivot + xShift, "├──────────");
		this.print(-40, xPivot + xShift, "│　　　　　　　　　　");
		this.print(-41, xPivot + xShift, "│　　　合計　　　　　");
		this.print(-42, xPivot + xShift, "└──────────");

		xShift += shiftFromLegends;

		if (LM018List != null && !LM018List.isEmpty()) {

			for (Map<String, String> LM018Vo : LM018List) {

				// only works if sql query is ordered by YearMonth

				if (!lastYearMonth.equals(LM018Vo.get("F1"))) {

					if (lastYearMonth.isEmpty()) {

						// draw new part of form, for the first time

						this.print(-9, xPivot + xShift, "┬─────────");
						this.print(-10, xPivot + xShift, "│　　　　　　　　　");
						this.print(-11, xPivot + xShift, "│　　　　　　　　　");
						this.print(-11, xPivot + xShift + shiftYearMonth,
								(parse.stringToInteger(LM018Vo.get("F1").substring(0, 4)) - 1911) + "年"
										+ LM018Vo.get("F1").substring(4) + "月",
								"C");
						this.print(-12, xPivot + xShift, "┼────┬────");
						this.print(-13, xPivot + xShift, "│　　　　│　　　　");
						this.print(-14, xPivot + xShift, "│　餘額　│　利收　");
						this.print(-15, xPivot + xShift, "┼────┼────");
						this.print(-16, xPivot + xShift, "│　　　　│　　　　");
						this.print(-17, xPivot + xShift, "│　　　　│　　　　");
						this.print(-18, xPivot + xShift, "┼────┼────");
						this.print(-19, xPivot + xShift, "│　　　　│　　　　");
						this.print(-20, xPivot + xShift, "│　　　　│　　　　");
						this.print(-21, xPivot + xShift, "┼────┤　　　　");
						this.print(-22, xPivot + xShift, "│　　　　│　　　　");
						this.print(-23, xPivot + xShift, "│　　　　│　　　　");
						this.print(-24, xPivot + xShift, "┼────┤　　　　");
						this.print(-25, xPivot + xShift, "│　　　　│　　　　");
						this.print(-26, xPivot + xShift, "│　　　　│　　　　");
						this.print(-27, xPivot + xShift, "┼────┤　　　　");
						this.print(-28, xPivot + xShift, "│　　　　│　　　　");
						this.print(-29, xPivot + xShift, "│　　　　│　　　　");
						this.print(-30, xPivot + xShift, "┼────┤　　　　");
						this.print(-31, xPivot + xShift, "│　　　　│　　　　");
						this.print(-32, xPivot + xShift, "│　　　　│　　　　");
						this.print(-33, xPivot + xShift, "┼────┤　　　　");
						this.print(-34, xPivot + xShift, "│　　　　│　　　　");
						this.print(-35, xPivot + xShift, "│　　　　│　　　　");
						this.print(-36, xPivot + xShift, "┼────┤　　　　");
						this.print(-37, xPivot + xShift, "│　　　　│　　　　");
						this.print(-38, xPivot + xShift, "│　　　　│　　　　");
						this.print(-39, xPivot + xShift, "┼────┼────");
						this.print(-40, xPivot + xShift, "│　　　　│　　　　");
						this.print(-41, xPivot + xShift, "│　　　　│　　　　");
						this.print(-42, xPivot + xShift, "┴────┴────");

					} else {

						// draw the a middle section

						this.print(-9, xPivot + xShift, "┬─────────");
						this.print(-10, xPivot + xShift, "│　　　　　　　　　");
						this.print(-11, xPivot + xShift, "│　　　　　　　　　");
						this.print(-11, xPivot + xShift + shiftYearMonth,
								(parse.stringToInteger(LM018Vo.get("F1").substring(0, 4)) - 1911) + "年"
										+ LM018Vo.get("F1").substring(4) + "月",
								"C");
						this.print(-12, xPivot + xShift, "┼────┬────");
						this.print(-13, xPivot + xShift, "│　　　　│　　　　");
						this.print(-14, xPivot + xShift, "│　餘額　│　利收　");
						this.print(-15, xPivot + xShift, "┼────┼────");
						this.print(-16, xPivot + xShift, "│　　　　│　　　　");
						this.print(-17, xPivot + xShift, "│　　　　│　　　　");
						this.print(-18, xPivot + xShift, "┼────┼────");
						this.print(-19, xPivot + xShift, "│　　　　│　　　　");
						this.print(-20, xPivot + xShift, "│　　　　│　　　　");
						this.print(-21, xPivot + xShift, "├────┤　　　　");
						this.print(-22, xPivot + xShift, "│　　　　│　　　　");
						this.print(-23, xPivot + xShift, "│　　　　│　　　　");
						this.print(-24, xPivot + xShift, "├────┤　　　　");
						this.print(-25, xPivot + xShift, "│　　　　│　　　　");
						this.print(-26, xPivot + xShift, "│　　　　│　　　　");
						this.print(-27, xPivot + xShift, "├────┤　　　　");
						this.print(-28, xPivot + xShift, "│　　　　│　　　　");
						this.print(-29, xPivot + xShift, "│　　　　│　　　　");
						this.print(-30, xPivot + xShift, "├────┤　　　　");
						this.print(-31, xPivot + xShift, "│　　　　│　　　　");
						this.print(-32, xPivot + xShift, "│　　　　│　　　　");
						this.print(-33, xPivot + xShift, "├────┤　　　　");
						this.print(-34, xPivot + xShift, "│　　　　│　　　　");
						this.print(-35, xPivot + xShift, "│　　　　│　　　　");
						this.print(-36, xPivot + xShift, "├────┤　　　　");
						this.print(-37, xPivot + xShift, "│　　　　│　　　　");
						this.print(-38, xPivot + xShift, "│　　　　│　　　　");
						this.print(-39, xPivot + xShift, "┼────┼────");
						this.print(-40, xPivot + xShift, "│　　　　│　　　　");
						this.print(-41, xPivot + xShift, "│　　　　│　　　　");
						this.print(-42, xPivot + xShift, "┴────┴────");

						// Output: year-to-be-ended group int total
						this.print(-27, xPivot + xShift + shiftMainInt, formatAmt(this.GetGroupIntTotal(), 3, 8), "C");

						// Output: year-to-be-ended sums
						this.print(-41, xPivot + xShift + shiftMainBal, formatAmt(this.GetBalTotal(), 3, 8), "C");
						this.print(-41, xPivot + xShift + shiftMainInt, formatAmt(this.GetIntTotal(), 3, 8), "C");

					}

					lastYearMonth = LM018Vo.get("F1"); // YYYYMM

					xShift += shiftPerSeason; // length of a new part of form

					// !!!!!

					// 這裡只重設 lastBal
					// 因為目前報表設計為只出同一年
					// 而參考樣張, 一年當中的利收顯示是累積的
					// 但如果要出跨年度的結果
					// lastInt 需要做每年重置的邏輯

					// !!!!!

					for (subject s : subject.values()) {
						s.lastBal = BigDecimal.ZERO;
					}

				}

				// get subject

				subject thisSubject = subject.get(LM018Vo.get("F0"));

				if (thisSubject != null) {

					BigDecimal f2 = getBigDecimal(LM018Vo.get("F2")).setScale(10, BigDecimal.ROUND_HALF_UP);
					BigDecimal f3 = getBigDecimal(LM018Vo.get("F3")).setScale(10, BigDecimal.ROUND_HALF_UP);

					// loanbal
					this.print(thisSubject.printY, xPivot + xShift + shiftMainBal, formatAmt(f2, 3, 8), "C");
					thisSubject.lastBal = f2;

					// interest
					if (!thisSubject.showsGroupIntOnly) {
						this.print(thisSubject.printY, xPivot + xShift + shiftMainInt, formatAmt(f3, 4, 8), "C");
					}
					thisSubject.lastInt = thisSubject.lastInt.add(f3);

				}

				// end of this iteration

			}

			// last yearMonth's GroupInt, totalSum and total Int

			this.print(-27, xPivot + xShift + shiftMainInt, formatAmt(this.GetGroupIntTotal(), 3, 8), "C");

			this.print(-41, xPivot + xShift + shiftMainBal, formatAmt(this.GetBalTotal(), 3, 8), "C");
			this.print(-41, xPivot + xShift + shiftMainInt, formatAmt(this.GetIntTotal(), 3, 8), "C");

		} else {
			this.print(-6, 1, "本日無資料");
		}

		// draw the end of the form and right-side total

		this.print(-9 , xPivot + xShift, "┐");
		this.print(-10, xPivot + xShift, "│");
		this.print(-11, xPivot + xShift, "│");
		this.print(-12, xPivot + xShift, "┤");
		this.print(-13, xPivot + xShift, "│");
		this.print(-14, xPivot + xShift, "│");
		this.print(-15, xPivot + xShift, "┤");
		this.print(-16, xPivot + xShift, "│");
		this.print(-17, xPivot + xShift, "│");
		this.print(-18, xPivot + xShift, "┤");
		this.print(-19, xPivot + xShift, "│");
		this.print(-20, xPivot + xShift, "│");
		this.print(-21, xPivot + xShift, "│");
		this.print(-22, xPivot + xShift, "│");
		this.print(-23, xPivot + xShift, "│");
		this.print(-24, xPivot + xShift, "│");
		this.print(-25, xPivot + xShift, "│");
		this.print(-26, xPivot + xShift, "│");
		this.print(-27, xPivot + xShift, "│");
		this.print(-28, xPivot + xShift, "│");
		this.print(-29, xPivot + xShift, "│");
		this.print(-30, xPivot + xShift, "│");
		this.print(-31, xPivot + xShift, "│");
		this.print(-32, xPivot + xShift, "│");
		this.print(-33, xPivot + xShift, "│");
		this.print(-34, xPivot + xShift, "│");
		this.print(-35, xPivot + xShift, "│");
		this.print(-36, xPivot + xShift, "│");
		this.print(-37, xPivot + xShift, "│");
		this.print(-38, xPivot + xShift, "│");
		this.print(-39, xPivot + xShift, "┤");
		this.print(-40, xPivot + xShift, "│");
		this.print(-41, xPivot + xShift, "│");
		this.print(-42, xPivot + xShift, "┘");

		xShift += shiftBeforeSum; // 2 for above; rest are extra spaces

		// 第二個表格
		this.print(-14, xPivot + xShift, "　　　 餘額　　　　　　利收");
		this.print(-15, xPivot + xShift, "┌───────┬───────┐");
		this.print(-16, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-17, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-18, xPivot + xShift, "├───────┼───────┤");
		this.print(-19, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-20, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-20, xPivot + xShift, "IA", "R");
		this.print(-21, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-22, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-23, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-23, xPivot + xShift, "IB", "R");
		this.print(-24, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-25, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-26, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-26, xPivot + xShift, "IC", "R");
		this.print(-27, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-28, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-29, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-29, xPivot + xShift, "ID+IE", "R");
		this.print(-30, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-31, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-32, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-32, xPivot + xShift, "IF+IG", "R");
		this.print(-33, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-34, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-35, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-35, xPivot + xShift, "IH+II", "R");
		this.print(-36, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-37, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-38, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-39, xPivot + xShift, "├───────┼───────┤");
		this.print(-40, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-41, xPivot + xShift, "│　　　　　　　│　　　　　　　│");
		this.print(-42, xPivot + xShift, "└───────┴───────┘");

		// Output: loanbal and not-group lastInt output
		for (subject s : subject.values()) {
			this.print(s.printY, xPivot + xShift + shiftSumBal, formatAmt(s.lastBal, 0), "C");

			if (!s.showsGroupIntOnly) {
				this.print(s.printY, xPivot + xShift + shiftSumInt, formatAmt(s.lastInt, 0), "C");
			}
		}

		// Output: group lastInt
		this.print(-28, xPivot + xShift + shiftSumInt, formatAmt(GetGroupIntTotal(), 0), "C");

		// Output: int and bal sum
		this.print(-41, xPivot + xShift + shiftSumBal, formatAmt(GetBalTotal(), 0), "C");
		this.print(-41, xPivot + xShift + shiftSumInt, formatAmt(GetIntTotal(), 0), "C");

		this.close();
		// this.toPdf(sno);

	}

	private BigDecimal GetGroupIntTotal() {
		BigDecimal t = BigDecimal.ZERO;

		for (subject s : subject.values()) {
			if (s.showsGroupIntOnly) {
				t = t.add(s.lastInt);
			}
		}

		return t;
	}

	private BigDecimal GetBalTotal() {
		BigDecimal t = BigDecimal.ZERO;

		for (subject s : subject.values()) {
			t = t.add(s.lastBal);
		}

		return t;
	}

	private BigDecimal GetIntTotal() {
		BigDecimal t = BigDecimal.ZERO;

		for (subject s : subject.values()) {
			t = t.add(s.lastInt);
		}

		return t;
	}
}
