package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.text.NumberFormat;

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
	@SuppressWarnings("unused")
	private String nowDate;
	// 製表時間
	@SuppressWarnings("unused")
	private String nowTime;

	// number with commas
	NumberFormat nfNum = NumberFormat.getNumberInstance();

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

	// 自訂表頭
	@Override
	public void printHeader() {

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

		this.print(-1, 88, "新光人壽保險股份有限公司", "C");
		this.print(-2, 88, this.reportItem, "C");
		this.print(-3, 88, "逾 01 - 99 期", "C");
		this.print(-4, 88, "撥款日期  70/01/01 起", "C");

		switch (currentSort) {
		case Agent:

			// 明細表頭
			/**
			 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
			 * ----------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
			 */
			this.print(-6, 1,
					" 　　　　　 　 　     　　逾　一　期      　逾　二　期     　 逾　三　期　　　　逾　四　期　　　　逾　五　期　　　　逾　六　期　　　　轉　催　收　　　　合　　　計");
			this.print(-7, 1,
					" 員工編號　經辦　　     件數    　金額 　 件數　  　金額 　 件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額");
			this.print(-8, 1, newBorder);

			// 明細起始列(自訂亦必須)
			this.setBeginRow(9);

			// 設定明細列數(自訂亦必須)
			this.setMaxRows(46);

			break;
		case Year:

			// 明細表頭
			/**
			 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
			 * ----------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
			 */
			this.print(-6, 1,
					" 　　　　　 　 　     　　逾　一　期      　逾　二　期     　 逾　三　期　　　　逾　四　期　　　　逾　五　期　　　　逾　六　期　　　　轉　催　收　　　　合　　　計");
			this.print(-7, 1,
					" 　　　　　　　　     　件數    　金額 　 件數　  　金額 　 件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額　　件數　　　金額");
			this.print(-8, 1, newBorder);

			// 明細起始列(自訂亦必須)
			this.setBeginRow(9);

			// 設定明細列數(自訂亦必須)
			this.setMaxRows(43);

			break;
		case LargeAmt_Agent:

			// 明細表頭
			/**
			 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
			 * ----------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
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
			this.setMaxRows(43);

			break;
		case LargeAmt_Customer:

			// 明細表頭
			/**
			 * -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
			 * ----------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
			 */
			this.print(-6, 1, "        經辦　　　　　　　戶號　　　戶名　　期別　　　　　　　　金額");
			this.print(-7, 1, newBorder);

			// 明細起始列(自訂亦必須)
			this.setBeginRow(8);

			// 設定明細列數(自訂亦必須)
			this.setMaxRows(43);

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

	private void fillData(TitaVo titaVo) {
		this.newPage();

		List<Map<String, String>> lL9717 = null;

		try {
			lL9717 = l9717ServiceImpl.findAll(titaVo, currentSort);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9717ServiceImpl.findAll error = " + errors.toString());
		}

		if (lL9717 != null && lL9717.size() != 0) {

			int[] cnt = new int[7];
			int[] amt = new int[7];

			int amtTotal = 0;
			int cntTotal = 0;

			int ovduterm = 0;
			int itemCnt = 0;
			int itemAmt = 0;
			int count = 0;

			this.info("L9717 doing: " + currentSort + ", got results: " + lL9717.size() + " peko");

			int tempYear = 0;
			String tempBsOfficer = "0";

			for (Map<String, String> tLDVo : lL9717) {
				count++;

				switch (currentSort) {
				case Year:
					// 明細資料新的一行
					if (tempYear != Integer.parseInt(tLDVo.get("F0"))) {

						tempYear = Integer.parseInt(tLDVo.get("F0"));

						print(1, 1, "    ");

						print(0, 2, Integer.parseInt(tLDVo.get("F0")) - 1911 + " 年撥款件");

					}

					ovduterm = Integer.parseInt(tLDVo.get("F1")) == 990 ? 7 : Integer.parseInt(tLDVo.get("F1"));
					itemCnt = Integer.parseInt(tLDVo.get("F2"));
					itemAmt = Integer.parseInt(tLDVo.get("F3"));

					print(0, 26 + ovduterm * 18, nfNum.format(itemCnt), "R");
					print(0, 39 + ovduterm * 18, nfNum.format(itemAmt), "R");

					cntTotal += itemCnt;
					amtTotal += itemAmt;

					break;

				case Agent:
					// 明細資料新的一行
					if (tempBsOfficer != tLDVo.get("F0")) {

						tempBsOfficer = tLDVo.get("F0") == null ? " " : tLDVo.get("F0").toString();

						print(1, 1, "    ");

						print(0, 5, tempBsOfficer, "C");

						print(0, 15, tLDVo.get("F4") == null ? " " : tLDVo.get("F4").toString(), "C");

					}

					ovduterm = Integer.parseInt(tLDVo.get("F1")) == 990 ? 7 : Integer.parseInt(tLDVo.get("F1"));
					itemCnt = Integer.parseInt(tLDVo.get("F2"));
					itemAmt = Integer.parseInt(tLDVo.get("F3"));

					print(0, 26 + ovduterm * 18, nfNum.format(itemCnt), "R");
					print(0, 39 + ovduterm * 18, nfNum.format(itemAmt), "R");
					
					cnt[ovduterm - 1] += itemCnt;
					amt[ovduterm - 1] += itemAmt;

					cntTotal += itemCnt;
					amtTotal += itemAmt;

					break;
				case LargeAmt_Customer:
					// 因目前無資料 無格式 比對
					print(0, 2, tLDVo.get("F0"));
					print(0, 15, tLDVo.get("F1"), "C");
					print(0, 29, tLDVo.get("F2"), "C");
					print(0, 38, tLDVo.get("F3"), "C");
					print(0, 46, tLDVo.get("F4"), "C");
					print(0, 70, nfNum.format(Integer.parseInt(tLDVo.get("F5"))), "R");
					break;
				case LargeAmt_Agent:
					// 明細資料新的一行
					if (tempBsOfficer != tLDVo.get("F0")) {

						tempBsOfficer = tLDVo.get("F0") == null ? " " : tLDVo.get("F0").toString();

						print(1, 1, "    ");

						print(0, 5, tempBsOfficer, "C");

						print(0, 13, tLDVo.get("F4") == null ? " " : tLDVo.get("F4").toString(), "C");

					}

					ovduterm = Integer.parseInt(tLDVo.get("F1")) == 990 ? 7 : Integer.parseInt(tLDVo.get("F1"));
					itemCnt = Integer.parseInt(tLDVo.get("F2"));
					itemAmt = Integer.parseInt(tLDVo.get("F3"));

					print(0, 26 + ovduterm * 18, nfNum.format(itemCnt), "R");
					print(0, 39 + ovduterm * 18, nfNum.format(itemAmt), "R");

					
					cntTotal += itemCnt;
					amtTotal += itemAmt;


					break;
				default:
					break;
				}

				print(1, 1, newBorder);
			}

			if (count == lL9717.size()) {

				switch (currentSort) {
				case Agent:
					print(1, 2, "各期小計");

					for (int i = 0; i < 7; i++) {

						print(0, 26 + i * 18, nfNum.format(cnt[i]), "R");
						print(0, 39 + i * 18, nfNum.format(amt[i]), "R");

					}

					print(1, 1, newBorder);
					print(1, newBorder.length(), "總計：" + nfNum.format(cntTotal) + " 筆　" + nfNum.format(amtTotal), "R");
					print(1, 1, newBorder);
					print(1, 2, "備註：『因組織變動因素，經辦人員逾期案件統計基準：94 年元月前以授信人員為統計對象，94 年元月起則更改為放款專員。");
					break;
				case Year:
					print(1, newBorder.length(), "總計：" + nfNum.format(cntTotal) + " 筆　" + nfNum.format(amtTotal), "R");
					print(1, 1, newBorder);

					break;
				case LargeAmt_Agent:
					print(1, newBorder.length(), "總計：" + nfNum.format(cntTotal) + " 筆　" + nfNum.format(amtTotal), "R");
					print(1, 1, newBorder);
					break;
				case LargeAmt_Customer:
					print(1, 27, "總計： " + nfNum.format(cntTotal) + "筆　" + nfNum.format(amtTotal), "L");
					print(1, 1, newBorder);
					break;
				default:
					break;
				}

				this.print(-46, 88, "===== 報　表　結　束 =====", "C");

				cntTotal = 0;
				amtTotal = 0;

			}
		} else {
			print(1, 1, "本日無資料!!!");
		}
	}

	public void makePdf(TitaVo titaVo) throws LogicException {
		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);

		this.setCharSpaces(0);

		fillData(titaVo);

		this.toPdf(this.close(), this.reportCode + "_" + this.reportItem);
	}

	public boolean exec(TitaVo titaVo, OutputSortBy sort) throws LogicException {
		this.info("L9717Report exec ...");

		this.currentSort = sort;

		this.reportItem = "逾期及轉催收件統計表";

		switch (currentSort) {
		case Agent:
			this.reportItem += "_經辦別";
			break;
		case Year:
			this.reportItem += "_年度別";
			break;
		case LargeAmt_Agent:
			this.reportItem += "_大額件經辦別";
			break;
		case LargeAmt_Customer:
			this.reportItem += "_大額件客戶別";
			break;
		default:
			break;
		}

		;

		// 設定字體1:標楷體 字體大小12
		this.setFont(1, 12);

		this.reportDate = titaVo.getEntDyI() + 19110000;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		makePdf(titaVo);

		return true;
	}
}
