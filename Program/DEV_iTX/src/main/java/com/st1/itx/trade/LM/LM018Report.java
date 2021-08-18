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

@Component
@Scope("prototype")

public class LM018Report extends MakeReport {

	@Autowired
	public LM018ServiceImpl lM018ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		printHeaderL();

		this.setBeginRow(8);

		this.setMaxRows(50);
	}

	public void printHeaderL() {

		this.print(-3, 70, "專案放款餘額及利收明細", "C");

		// length number are funky in the later codes
		// I think it's MakeReport's problem, doesn't handle print pos well when
		// fontsize is changed.
		// all those numbers are tuned by eyes, but I think it's around +6 offset
		// globally.
		this.setFontSize(5);

		this.print(-5, 260, "機密等級：密");
		this.print(-6, 260, "單位：億元");

		this.setCharSpaces(0);

	}

	private static class Subject {
		public String name = "";
		public int printY = 0;
		public BigDecimal lastBal = BigDecimal.ZERO;
		public BigDecimal lastInt = BigDecimal.ZERO;
		public Boolean showsGroupIntOnly = false;

		public static Subject Init(String _name, int _printY, Boolean _isSecondGroup) {
			Subject s = new Subject();
			s.name = _name;
			s.printY = _printY;
			s.showsGroupIntOnly = _isSecondGroup;

			return s;
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

		// All output subjects
		// AA -17, IA -20, IB -23, IC -26, ID -29, IF -32, IH -35, ZZ -38

		Subject[] subjects = new Subject[] { Subject.Init("AA", -17, false), Subject.Init("IA", -20, true), Subject.Init("IB", -23, true), Subject.Init("IC", -26, true), Subject.Init("ID", -29, true),
				Subject.Init("IF", -32, true), Subject.Init("IH", -35, true), Subject.Init("ZZ", -38, true) };

		// new form trigger

		String lastYearMonth = "";

		// draw start of the form

		this.print(-9, xPivot + xShift, "┌──────────");
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

		xShift += 27;

		if (LM018List != null && LM018List.size() != 0) {

			for (Map<String, String> LM018Vo : LM018List) {

				// only works if sql query is ordered by YearMonth

				if (!lastYearMonth.equals(LM018Vo.get("F1"))) {

					if (lastYearMonth.isEmpty()) {

					// draw new part of form, for the first time

					this.print(-9, xPivot + xShift, "┬─────────");
					this.print(-10, xPivot + xShift, "│　　　　　　　　　");
					this.print(-11, xPivot + xShift, "│　　　　　　　　　");
					this.print(-11, xPivot + xShift + 13, (Integer.valueOf(LM018Vo.get("F1").substring(0, 4)) - 1911) + "年" + LM018Vo.get("F1").substring(4) + "月", "C");
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
					this.print(-11, xPivot + xShift + 13, (Integer.valueOf(LM018Vo.get("F1").substring(0, 4)) - 1911) + "年" + LM018Vo.get("F1").substring(4) + "月", "C");
					this.print(-12, xPivot + xShift, "┼────┬────");
					this.print(-13, xPivot + xShift, "│　　　　│　　　　");
					this.print(-14, xPivot + xShift, "│　餘額　│　利收　");
					this.print(-15, xPivot + xShift, "┼────┼────");
					this.print(-16, xPivot + xShift, "│　　　　│　　　　");
					this.print(-17, xPivot + xShift, "│　　　　│　　　　");
					this.print(-18, xPivot + xShift, "├────┼────");
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

					this.print(-27, xPivot + xShift - 6, this.GetGroupIntTotal(subjects).divide(new BigDecimal(100000000), 4, BigDecimal.ROUND_HALF_UP).toString(), "C");

					// Output: year-to-be-ended sums
					this.print(-41, xPivot + xShift - 18, this.GetBalTotal(subjects).divide(new BigDecimal(100000000), 3, BigDecimal.ROUND_HALF_UP).toString(), "C");
					this.print(-41, xPivot + xShift - 6, this.GetIntTotal(subjects).divide(new BigDecimal(100000000), 4, BigDecimal.ROUND_HALF_UP).toString(), "C");

					}

					lastYearMonth = LM018Vo.get("F1"); // YYYYMM

					xShift += 26; // length of a new part of form

					// reset all lastBal/lastInt

					for (Subject s : subjects) {
						s.lastBal = BigDecimal.ZERO;
						s.lastInt = BigDecimal.ZERO;
					}

				}

				BigDecimal f2 = new BigDecimal(LM018Vo.get("F2")).setScale(10, BigDecimal.ROUND_HALF_UP);
				BigDecimal f3 = new BigDecimal(LM018Vo.get("F3")).setScale(10, BigDecimal.ROUND_HALF_UP);

				// get subject

				Subject thisSubject = null;

				for (Subject s : subjects) {
					if (s.name.equals(LM018Vo.get("F0"))) {
						thisSubject = s;
						break;
					}
				}

				if (thisSubject != null) {

					// loanbal
					this.print(thisSubject.printY, xPivot + xShift - 18, f2.divide(new BigDecimal(100000000), 3, BigDecimal.ROUND_HALF_UP).toString(), "C");
					thisSubject.lastBal = f2;

					// interest
					if (!thisSubject.showsGroupIntOnly) {
						this.print(thisSubject.printY, xPivot + xShift - 6, f3.divide(new BigDecimal(100000000), 4, BigDecimal.ROUND_HALF_UP).toString(), "C");
					}
					thisSubject.lastInt = f3;

				}

				// end of this iteration

			}

			// last yearMonth's GroupInt, totalSum and total Int

			this.print(-27, xPivot + xShift - 6, this.GetGroupIntTotal(subjects).divide(new BigDecimal(100000000), 4, BigDecimal.ROUND_HALF_UP).toString(), "C");

			this.print(-41, xPivot + xShift - 18, this.GetBalTotal(subjects).divide(new BigDecimal(100000000), 3, BigDecimal.ROUND_HALF_UP).toString(), "C");
			this.print(-41, xPivot + xShift - 6, this.GetIntTotal(subjects).divide(new BigDecimal(100000000), 4, BigDecimal.ROUND_HALF_UP).toString(), "C");

		} else {
			this.print(-6, 1, "本日無資料");
		}

		// draw the end of the form and right-side total

		this.print(-9, xPivot + xShift, "┐");
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

		xShift += 14; // 2 for above; rest are extra spaces

		// 第二個表格
		this.print(-14, xPivot + xShift + 11, "餘額", "C");
		this.print(-14, xPivot + xShift + 31, "利收", "C");
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
		for (Subject s : subjects) {
			this.print(s.printY, xPivot + xShift + 11, formatAmt(s.lastBal, 0), "C");

			if (!s.showsGroupIntOnly) {
				this.print(s.printY, xPivot + xShift + 31, formatAmt(s.lastInt, 0), "C");
			}
		}

		// Output: group lastInt
		this.print(-28, xPivot + xShift + 31, formatAmt(GetGroupIntTotal(subjects), 0), "C");

		// Output: int and bal sum
		this.print(-41, xPivot + xShift + 11, formatAmt(GetBalTotal(subjects), 0), "C");
		this.print(-41, xPivot + xShift + 31, formatAmt(GetIntTotal(subjects), 0), "C");

		long sno = this.close();
		this.toPdf(sno);

	}

	private BigDecimal GetGroupIntTotal(Subject[] subjects) {
		BigDecimal t = BigDecimal.ZERO;

		for (Subject s : subjects) {
			if (s.showsGroupIntOnly) {
				t = t.add(s.lastInt);
			}
		}

		return t;
	}

	private BigDecimal GetBalTotal(Subject[] subjects) {
		BigDecimal t = BigDecimal.ZERO;

		for (Subject s : subjects) {
			t = t.add(s.lastBal);
		}

		return t;
	}

	private BigDecimal GetIntTotal(Subject[] subjects) {
		BigDecimal t = BigDecimal.ZERO;

		for (Subject s : subjects) {
			t = t.add(s.lastInt);
		}

		return t;
	}
}
