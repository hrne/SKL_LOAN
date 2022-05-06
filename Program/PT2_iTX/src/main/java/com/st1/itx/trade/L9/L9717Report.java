package com.st1.itx.trade.L9;

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
import com.st1.itx.db.service.springjpa.cm.L9717ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9717ServiceImpl.OutputSortBy;
import com.st1.itx.util.common.MakeReport;

@Component("L9717Report")
@Scope("prototype")
public class L9717Report extends MakeReport {

	@Autowired
	L9717ServiceImpl l9717ServiceImpl;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L9717";
	private String reportItem = "";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;



	List<BigDecimal> list = new ArrayList<BigDecimal>(14);

	String rocYear;
	String rocMonth;

	// length: 164
	// usually we do print() with x at 1 instead of 0 so the last symbol output is
	// actually at 165;
	// hence the extra space to avoid print()s using newBorder.length() to pin the
	// output on right side and gets wrong position
	private String newBorder = "===================================================================================================================================================================="
			+ " ";

	private OutputSortBy currentSort;

	private class outputColumn {
		private int pos = 0;
		private String sort = "L";
		private Boolean isAmount = false;

		public outputColumn(int _pos, String _sort, Boolean _isAmount) {
			this.pos = _pos;
			this.sort = _sort;
			this.isAmount = _isAmount;
		}

		public int getPos() {
			return this.pos;
		}

		public String getSort() {
			return this.sort;
		}

		public Boolean getIsAmount() {
			return this.isAmount;
		}
	}

	// 自訂表頭
	@Override
	public void printHeader() {

		this.setFont(1, 10);

//		this.info("L9717Report.printHeader");

		this.print(-1, 1, "程式ID：" + this.getParentTranCode());
		this.print(-2, 1, "報  表：" + this.getRptCode());

		switch (currentSort) {
		case Year:
			this.print(-4, 1, "各年度撥款件統計");
			break;
		case LargeAmt_Agent:
			this.print(-4, 1, "大額件（五千萬以上）");
			break;
		case LargeAmt_Customer:
			this.print(-4, 1, "大額件（五千萬以上）客戶明細");
			break;
		default:
			this.print(-4, 1, "");
			break;
		}

		this.print(-1, newBorder.length() - 20, "機密等級：" + this.security);
		this.print(-2, newBorder.length() - 20, "基　　礎：" + showBcDate(this.nowDate, 0)); // longest; length 20
		this.print(-3, newBorder.length() - 20, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-4, newBorder.length() - 20, "時　　間：" + showTime(this.nowTime));
		this.print(-5, newBorder.length() - 20, "頁　　數：" + this.getNowPage());

		this.print(-1, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");
		this.print(-2, this.getMidXAxis(), this.reportItem, "C");
		this.print(-3, this.getMidXAxis(), "逾 01 - 99 期", "C");
		this.print(-4, this.getMidXAxis(), "撥款日期  70/01/01 起", "C");

		switch (currentSort) {
		case Agent:

			// 明細表頭
			/**
			 * ---------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
			 * ------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
			 */
			this.print(-6, 1,
					" 　　　　　 　 　     　　逾　一　期      　逾　二　期     　 逾　三　期　　　　逾　四　期　　　　逾　五　期　　　　逾　六　期　　　　轉　催　收　　　　合　　　計");
			this.print(-7, 1,
					" 員工編號　經辦　　     件數    　金額 　 件數　  　金額 　 件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額");
			this.print(-8, 1, newBorder);

			// 明細起始列(自訂亦必須)
			this.setBeginRow(9);

			// 設定明細列數(自訂亦必須)
			this.setMaxRows(38);

			break;
		case Year:

			// 明細表頭
			/**
			 * ---------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
			 * ------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
			 */
			this.print(-6, 1,
					" 　　　　　 　 　     　　逾　一　期      　逾　二　期     　 逾　三　期　　　　逾　四　期　　　　逾　五　期　　　　逾　六　期　　　　轉　催　收　　　　合　　　計");
			this.print(-7, 1,
					" 　　　　　　　　     　件數    　金額 　 件數　  　金額 　 件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額");
			this.print(-8, 1, newBorder);

			// 明細起始列(自訂亦必須)
			this.setBeginRow(9);

			// 設定明細列數(自訂亦必須)
			this.setMaxRows(38);

			break;
		case LargeAmt_Agent:

			// 明細表頭
			/**
			 * ---------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
			 * ------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
			 */
			this.print(-6, 1,
					" 　　　　　 　      　　　逾　一　期      　逾　二　期     　 逾　三　期　　　　逾　四　期　　　　逾　五　期　　　　逾　六　期　　　　轉　催　收　　　　合　　　計");
			this.print(-7, 1,
					" 　　　　　經辦     　　件數    　金額 　 件數　  　金額 　 件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額");
			this.print(-8, 1, newBorder);
			this.print(-8, 1, newBorder);

			// 明細起始列(自訂亦必須)
			this.setBeginRow(9);

			// 設定明細列數(自訂亦必須)
			this.setMaxRows(38);

			break;
		case LargeAmt_Customer:

			// 明細表頭
			/**
			 * ----------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
			 * -------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
			 */
			this.print(-6, 1, "          經辦　　　 　　戶號　　  　戶名　期別　　　　　　　　金額");
			this.print(-7, 1, newBorder);

			// 明細起始列(自訂亦必須)
			this.setBeginRow(8);

			// 設定明細列數(自訂亦必須)
			this.setMaxRows(38);

			break;
		default:
			break;
		}

	}

	// 自訂表尾
//	@Override
//	public void printFooter() {
//		this.print(-46, 88, "===== 報　表　結　束 =====", "C");
//	}

	private boolean fillData(TitaVo titaVo) {
		this.newPage();

		List<Map<String, String>> lL9717 = null;
		this.info("currentSort=" + currentSort);
		try {
			lL9717 = l9717ServiceImpl.findAll(titaVo, currentSort);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9717ServiceImpl.findAll error = " + errors.toString());
		}

		if (lL9717 != null && !lL9717.isEmpty()) {

			BigDecimal totalCount = BigDecimal.ZERO;
			BigDecimal totalAmt = BigDecimal.ZERO;

			for (Map<String, String> tLDVo : lL9717) {

				ArrayList<outputColumn> columnList = new ArrayList<outputColumn>(18);

				this.print(1, 0, "");

				switch (currentSort) {

				// tidy'd up this part from 3 arrays to 1 arraylist
				// still feels like there's some better way to do this...

				case Year:
					// F0 年度 14 R
					// F1 一期件數 26 C
					// F2 一期金額 39 R
					// F3 二期件數 44 C
					// F4 二期金額 57 R
					// F5 三期件數 62 C
					// F6 三期金額 75 R
					// F7 四期件數 80 C
					// F8 四期金額 93 R
					// F9 五期件數 98 C
					// F10 五期金額 111 R
					// F11 六期件數 116 C
					// F12 六期金額 129 R
					// F13 轉催收件數 134 C
					// F14 轉催收金額 147 R
					// F15 合計件數 152 C
					// F16 合計金額 165 R
					// 總計筆數 152 R
					// 總計金額 165 R

					columnList.add(new outputColumn(14, "R", false));
					columnList.add(new outputColumn(26, "C", false));
					columnList.add(new outputColumn(39, "R", true));
					columnList.add(new outputColumn(44, "C", false));
					columnList.add(new outputColumn(57, "R", true));
					columnList.add(new outputColumn(62, "C", false));
					columnList.add(new outputColumn(75, "R", true));
					columnList.add(new outputColumn(80, "C", false));
					columnList.add(new outputColumn(93, "R", true));
					columnList.add(new outputColumn(98, "C", false));
					columnList.add(new outputColumn(111, "R", true));
					columnList.add(new outputColumn(116, "C", false));
					columnList.add(new outputColumn(129, "R", true));
					columnList.add(new outputColumn(134, "C", false));
					columnList.add(new outputColumn(147, "R", true));
					columnList.add(new outputColumn(152, "C", false));
					columnList.add(new outputColumn(165, "R", true));

					totalCount = totalCount.add(getBigDecimal(tLDVo.get("F15")));
					totalAmt = totalAmt.add(getBigDecimal(tLDVo.get("F16")));

					break;

				case Agent:
					// F0 員編 2 L
					// F1 員工姓名 15 R
					// F2 一期件數 26 C
					// F3 一期金額 39 R
					// F4 二期件數 44 C
					// F5 二期金額 57 R
					// F6 三期件數 62 C
					// F7 三期金額 75 R
					// F8 四期件數 80 C
					// F9 四期金額 93 R
					// F10 五期件數 98 C
					// F11 五期金額 111 R
					// F12 六期件數 116 C
					// F13 六期金額 129 R
					// F14 轉催收件數 134 C
					// F15 轉催收金額 147 R
					// F16 合計件數 152 C
					// F17 合計金額 165 R
					// 總計筆數 152 R
					// 總計金額 165 R

					columnList.add(new outputColumn(2, "L", false));
					columnList.add(new outputColumn(14, "R", false));
					columnList.add(new outputColumn(26, "C", false));
					columnList.add(new outputColumn(39, "R", true));
					columnList.add(new outputColumn(44, "C", false));
					columnList.add(new outputColumn(57, "R", true));
					columnList.add(new outputColumn(62, "C", false));
					columnList.add(new outputColumn(75, "R", true));
					columnList.add(new outputColumn(80, "C", false));
					columnList.add(new outputColumn(93, "R", true));
					columnList.add(new outputColumn(98, "C", false));
					columnList.add(new outputColumn(111, "R", true));
					columnList.add(new outputColumn(116, "C", false));
					columnList.add(new outputColumn(129, "R", true));
					columnList.add(new outputColumn(134, "C", false));
					columnList.add(new outputColumn(147, "R", true));
					columnList.add(new outputColumn(152, "C", false));
					columnList.add(new outputColumn(165, "R", true));

					totalCount = totalCount.add(getBigDecimal(tLDVo.get("F16")));
					totalAmt = totalAmt.add(getBigDecimal(tLDVo.get("F17")));

					break;

				case LargeAmt_Customer:
					// F0 員編 2 L
					// F1 員工姓名 15 R
					// F2 戶號 27 C
					// F3 戶名 42 R
					// F4 期別 47 R
					// F5 餘額 68 R;

					columnList.add(new outputColumn(2, "L", false));
					columnList.add(new outputColumn(15, "R", false));
					columnList.add(new outputColumn(27, "C", false));
					columnList.add(new outputColumn(42, "R", false));
					columnList.add(new outputColumn(47, "R", false));
					columnList.add(new outputColumn(68, "R", true));

					totalCount = totalCount.add(getBigDecimal(1));
					totalAmt = totalAmt.add(getBigDecimal(tLDVo.get("F5")));

					break;
				case LargeAmt_Agent:
					// F0 員編 2 L
					// F1 員工姓名 15 R
					// F2 一期件數 26 C
					// F3 一期金額 39 R
					// F4 二期件數 44 C
					// F5 二期金額 57 R
					// F6 三期件數 62 C
					// F7 三期金額 75 R
					// F8 四期件數 80 C
					// F9 四期金額 93 R
					// F10 五期件數 98 C
					// F11 五期金額 111 R
					// F12 六期件數 116 C
					// F13 六期金額 129 R
					// F14 轉催收件數 134 C
					// F15 轉催收金額 147 R
					// F16 合計件數 152 C
					// F17 合計金額 165 R
					// 總計筆數 152 R
					// 總計金額 165 R

					columnList.add(new outputColumn(2, "L", false));
					columnList.add(new outputColumn(14, "R", false));
					columnList.add(new outputColumn(26, "C", false));
					columnList.add(new outputColumn(39, "R", true));
					columnList.add(new outputColumn(44, "C", false));
					columnList.add(new outputColumn(57, "R", true));
					columnList.add(new outputColumn(62, "C", false));
					columnList.add(new outputColumn(75, "R", true));
					columnList.add(new outputColumn(80, "C", false));
					columnList.add(new outputColumn(93, "R", true));
					columnList.add(new outputColumn(98, "C", false));
					columnList.add(new outputColumn(111, "R", true));
					columnList.add(new outputColumn(116, "C", false));
					columnList.add(new outputColumn(129, "R", true));
					columnList.add(new outputColumn(134, "C", false));
					columnList.add(new outputColumn(147, "R", true));
					columnList.add(new outputColumn(152, "C", false));
					columnList.add(new outputColumn(165, "R", true));

					totalCount = totalCount.add(getBigDecimal(tLDVo.get("F16")));
					totalAmt = totalAmt.add(getBigDecimal(tLDVo.get("F17")));

					break;
				default:
					break;
				}

				this.info("L9717Report: " + currentSort);

				for (int i = 0; i < columnList.size(); i++) {
					this.info(currentSort + " [" + i + "]: " + tLDVo.get("F" + i));
					this.print(0, columnList.get(i).getPos(),
							columnList.get(i).getIsAmount() == true ? formatAmt(tLDVo.get("F" + i), 0)
									: tLDVo.get("F" + i),
							columnList.get(i).getSort());
				}

				BigDecimal ovdu = BigDecimal.ZERO;
				BigDecimal amt = BigDecimal.ZERO;

				switch (currentSort) {
				case Year:
					// F1 一期件數 26 C
					// F2 一期金額 39 R
					// F3 二期件數 44 C
					// F4 二期金額 57 R
					// F5 三期件數 62 C
					// F6 三期金額 75 R
					// F7 四期件數 80 C
					// F8 四期金額 93 R
					// F9 五期件數 98 C
					// F10 五期金額 111 R
					// F11 六期件數 116 C
					// F12 六期金額 129 R
					// F13 轉催收件數 134 C
					// F14 轉催收金額 147 R

					for (int i = 1; i < 15; i++) {
						if (columnList.get(i).getIsAmount()) {
							amt = amt.add(new BigDecimal(tLDVo.get("F" + i)));
							if (list.size() == 14) {
								list.set(i - 1, amt);
							} else {
								list.add(amt);
							}
						} else {
							ovdu = ovdu.add(new BigDecimal(tLDVo.get("F" + i)));

							if (list.size() == 14) {
								list.set(i - 1, ovdu);
							} else {
								list.add(ovdu);
							}
						}

					}

					break;

				case Agent:
				case LargeAmt_Agent:

					// F2 一期件數 26 C
					// F3 一期金額 39 R
					// F4 二期件數 44 C
					// F5 二期金額 57 R
					// F6 三期件數 62 C
					// F7 三期金額 75 R
					// F8 四期件數 80 C
					// F9 四期金額 93 R
					// F10 五期件數 98 C
					// F11 五期金額 111 R
					// F12 六期件數 116 C
					// F13 六期金額 129 R
					// F14 轉催收件數 134 C
					// F15 轉催收金額 147 R

					for (int i = 2; i < 16; i++) {
						if (columnList.get(i).getIsAmount()) {
							amt = amt.add(new BigDecimal(tLDVo.get("F" + i)));
							if (list.size() == 14) {
								list.set(i - 2, amt);
							} else {
								list.add(amt);
							}
						} else {
							ovdu = ovdu.add(new BigDecimal(tLDVo.get("F" + i)));

							if (list.size() == 14) {
								list.set(i - 2, ovdu);
							} else {
								list.add(ovdu);
							}
						}

					}

					break;

				default:
					break;
				}

				// 根據樣張，大額客戶別不做每筆資料換行畫線
				if (currentSort != OutputSortBy.LargeAmt_Customer) {
					this.print(1, 1, newBorder);
				}

			}

			// 總計

			this.print(1, 0, "");

			int countX = 0;
			int amtX = 0;

			switch (currentSort) {
			case Agent:
				countX = 152;
				amtX = 165;
				break;
			case Year:
				countX = 152;
				amtX = 165;
				break;
			case LargeAmt_Agent:
				countX = 152;
				amtX = 165;
				break;
			case LargeAmt_Customer:
				countX = 47;
				amtX = 68;
				break;
			default:
				break;
			}

			if (currentSort != OutputSortBy.LargeAmt_Customer) {
				this.print(1, 1, newBorder);
				this.print(1, 1, "");
				this.print(1, 1, newBorder);

				// F2 一期件數 26 C
				// F3 一期金額 39 R
				// F4 二期件數 44 C
				// F5 二期金額 57 R
				// F6 三期件數 62 C
				// F7 三期金額 75 R
				// F8 四期件數 80 C
				// F9 四期金額 93 R
				// F10 五期件數 98 C
				// F11 五期金額 111 R
				// F12 六期件數 116 C
				// F13 六期金額 129 R
				// F14 轉催收件數 134 C
				// F15 轉催收金額 147 R

				for (int i = 0, space = 1; i < list.size(); i = i + 2, space++) {

					this.print(1, 26 + (18 * space), formatAmt(list.get(i), 0), "C");
				}

				for (int i = 1, space = 1; i < list.size(); i = i + 2, space++) {
					this.print(1, 39 + (18 * space), list.get(i).toString(), "R");
				}

				this.print(1, 1, "各期小計");
				this.print(1, 1, newBorder);
			}
			this.print(0, countX, "總計：　　" + formatAmt(totalCount, 0) + " 筆", "R");
			this.print(0, amtX, formatAmt(totalAmt, 0), "R");
			this.print(1, 1, newBorder);

			// 經辦別最後加這個
			if (currentSort == OutputSortBy.Agent) {
				this.print(1, 1, "『因組織變動因素，經辦人員餘期案件統計基準：94年元月前以授信人員為統計對象，94年元月起則更改為放款專員。』");
			}

			return true;

		} else {
			print(1, 1, "本日無資料!!!");
			return false;
		}
	}

	public boolean makePdf(TitaVo titaVo) throws LogicException {
		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		boolean result = fillData(titaVo);

		this.close();

		return result;

		// this.toPdf(this.close(), this.reportCode + "_" + this.reportItem);
	}

	public boolean exec(TitaVo titaVo, OutputSortBy sort) throws LogicException {
		this.info("L9717Report exec ...");

		this.currentSort = sort;

		switch (currentSort) {
		case Agent:
			this.reportItem = "逾期及轉催收件統計表_經辦別";
			break;
		case Year:
			this.reportItem = "逾期及轉催收件統計表_年度別";
			break;
		case LargeAmt_Agent:
			this.reportItem = "逾期及轉催收件統計表_大額件經辦別";
			break;
		case LargeAmt_Customer:
			this.reportItem = "逾期及轉催收件統計表_大額件客戶別";
			break;
		default:
			break;
		}

		this.reportDate = titaVo.getEntDyI() + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		return makePdf(titaVo);
	}
}
