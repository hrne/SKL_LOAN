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
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.springjpa.cm.LM008ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ExcelFontStyleVo;
import com.st1.itx.util.date.DateUtil;

/**
 * LM008-應收利息明細表
 * 
 * @author ST1-Wei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class LM008Report extends MakeReport {

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	LM008ServiceImpl lM008ServiceImpl;

	@Autowired
	CdAcCodeService cdAcCodeService;

	@Autowired
	DateUtil dateUtil;

	// 每頁小計
	private BigDecimal countsSubtotal; // 筆數小計
	private BigDecimal unpaidSubtotal; // 已到期未繳利息小計
	private BigDecimal unexpiredSubtotal; // 未到期應收利息小計
	private BigDecimal custIntSubtotal; // 歸戶利息合計之小計

	// 合計
	private BigDecimal countsTotal = BigDecimal.ZERO; // 筆數合計
	private BigDecimal unpaidTotal = BigDecimal.ZERO; // 已到期未繳利息合計
	private BigDecimal unexpiredTotal = BigDecimal.ZERO; // 未到期應收利息合計
	private BigDecimal custIntTotal = BigDecimal.ZERO; // 歸戶利息合計之合計

	private int row = 1; // 列數:記錄印到第幾列

	private void printSheetHeader(int page, String acctItem) throws LogicException {

		// 調整欄寬
		makeExcel.setWidth(1, 16);
		makeExcel.setWidth(2, 16);
		makeExcel.setWidth(3, 28);
		makeExcel.setWidth(4, 16);
		makeExcel.setWidth(5, 16);
		makeExcel.setWidth(6, 28);
		makeExcel.setWidth(7, 28);
		makeExcel.setWidth(8, 28);
		makeExcel.setWidth(9, 28);// 新增

		// 合併儲存格
		makeExcel.setMergedRegion(2, 2, 3, 8);// (2, 2, 3, 7)
		makeExcel.setMergedRegion(3, 4, 3, 8);// (3, 4, 3, 7)
		makeExcel.setMergedRegion(5, 5, 3, 8);// (5, 5, 3, 7)

		ExcelFontStyleVo headerStyleVo = new ExcelFontStyleVo();
		headerStyleVo.setBold(true);

		// 第1列
		makeExcel.setValue(row, 9, "機密等級：密", "L", headerStyleVo);// col:8
		row++;

		// 第2列
		makeExcel.setValue(row, 1, "程式ID：", "L", headerStyleVo);
		makeExcel.setValue(row, 2, this.getParentTranCode(), "L", headerStyleVo);
		makeExcel.setValue(row, 3, "新光人壽保險股份有限公司", "C", headerStyleVo);
		makeExcel.setValue(row, 9, "日　期：" + this.showBcDate(dateUtil.getNowStringBc(), 1), "L", headerStyleVo);// col:8
		row++;

		// 第3列
		makeExcel.setValue(row, 1, "報　表：", "L", headerStyleVo);
		makeExcel.setValue(row, 2, makeExcel.getFileCode(), "L", headerStyleVo);
		makeExcel.setValue(row, 3, "應收利息明細表", "C", headerStyleVo);
		makeExcel.setValue(row, 9, "時　間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "L",
				headerStyleVo);// col:8
		row++;

		// 第4列
		makeExcel.setValue(row, 9, "頁　數：" + page, "L", headerStyleVo);// col:8
		row++;

		// 第5列
		makeExcel.setValue(row, 3, showRocDate(makeExcel.getDate(), 0), "C", headerStyleVo);
		row++;

		// 第6列
		makeExcel.setValue(row, 1, "業務科目 ：", "L", headerStyleVo);
		makeExcel.setValue(row, 2, acctItem, "L", headerStyleVo);
		row++;

		// 第7列
		makeExcel.setValue(row, 1, "戶號", "C", headerStyleVo);
		makeExcel.setValue(row, 2, "額度", "C", headerStyleVo);// 新增
		makeExcel.setValue(row, 3, "戶名", "C", headerStyleVo);
		makeExcel.setValue(row, 4, "放款餘額", "C", headerStyleVo);// col:3
		makeExcel.setValue(row, 5, "利率", "C", headerStyleVo);// col:4
		makeExcel.setValue(row, 6, "繳息迄日", "C", headerStyleVo);// col:5
		makeExcel.setValue(row, 7, "已到期未繳利息", "C", headerStyleVo);// col:6
		makeExcel.setValue(row, 8, "未到期應收利息", "C", headerStyleVo);// col:7
		makeExcel.setValue(row, 9, "合計", "C", headerStyleVo);// col:8
		row++;
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.titaVo = titaVo;

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM008", "應收利息明細表", "LM008-應收利息明細表");

		List<Map<String, String>> acctCodeGroupList = null;

		try {
			acctCodeGroupList = lM008ServiceImpl.findAcctCodeGroup(titaVo);
		} catch (Exception e) {
			this.info("lM008ServiceImpl.findAcctCodeGroup error = " + e.getMessage());
		}

		String acctCode = "";

		int groupIndex = 1; // 群組索引

		if (acctCodeGroupList == null || acctCodeGroupList.size() == 0) {
			// 印表頭
			printSheetHeader(groupIndex, " ");

			makeExcel.setValue(row, 1, "本日無資料");

			// 畫框線
			makeExcel.setAddRengionBorder("A", 1, "I", row, 1);// ("A", 1, "H", row, 1)

			row++;

		} else {

			// 以業務科目分頁
			for (Map<String, String> acctCodeMap : acctCodeGroupList) {

				// 本頁業務科目
				acctCode = acctCodeMap.get("F0");

				this.info("LM008Report GroupIndex " + groupIndex + " , AcctCode = " + acctCode);

				// 查該業務科目中文
				CdAcCode tCdAcCode = cdAcCodeService.acCodeAcctFirst(acctCode);

				String acctItem = "";

				if (tCdAcCode != null) {
					acctItem = tCdAcCode.getAcctItem();
				} else {
					this.error("LM008Report acctCode = " + acctCode + ",查無對應中文名稱.");
				}

				// 群組換頁
				if (groupIndex == 1) {
					// 設定 Sheet Name
					makeExcel.setSheet(groupIndex, acctItem);
				} else {
					// 建立新的 Sheet 並設定 Sheet Name
					makeExcel.newSheet(acctItem);
				}

				// 小計歸零
				resetSubtotal();

				// 列數指回1
				row = 1;

				// 印表頭
				printSheetHeader(groupIndex, acctItem);

				// 印明細 & 計算小計、合計
				printSheetDetail(acctCode);

				// 印表尾
				printSheetFooter();

				// 更新群組索引
				groupIndex++;
			}
		}

		// 印合計
		printTotal();

		long sno = makeExcel.close();
		// makeExcel.toExcel(sno);
	}

	private void resetSubtotal() {
		countsSubtotal = BigDecimal.ZERO; // 筆數小計
		unpaidSubtotal = BigDecimal.ZERO; // 已到期未繳利息小計
		unexpiredSubtotal = BigDecimal.ZERO; // 未到期應收利息小計
		custIntSubtotal = BigDecimal.ZERO; // 歸戶利息合計之小計
	}

	private void printSheetDetail(String acctCode) throws LogicException {

		List<Map<String, String>> detailList = null;

		// 查明細資料
		try {
			detailList = lM008ServiceImpl.findDetail(acctCode, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM008ServiceImpl.findDetail error = " + e.getMessage());
		}

		if (detailList != null && detailList.size() != 0) {

			for (Map<String, String> detail : detailList) {

				String custNo = detail.get("F2");
				String facmNo = detail.get("F3");
				String custName = detail.get("F4"); // 額度(新增)
				BigDecimal loanBal = detail.get("F5") == null ? BigDecimal.ZERO : new BigDecimal(detail.get("F5"));// F4
				BigDecimal rate = detail.get("F6") == null ? BigDecimal.ZERO : new BigDecimal(detail.get("F6"));// F5
				String date = detail.get("F7");// F6
				BigDecimal unpaid = detail.get("F8") == null ? BigDecimal.ZERO : new BigDecimal(detail.get("F8"));// F7
				BigDecimal unexpired = detail.get("F9") == null ? BigDecimal.ZERO : new BigDecimal(detail.get("F9"));// F8
				BigDecimal custInt = unpaid.add(unexpired);

				makeExcel.setValue(row, 1, custNo, "R"); // 戶號
				makeExcel.setValue(row, 2, facmNo, "R"); // 戶名
				makeExcel.setValue(row, 3, custName, "L"); // 額度(新增)
				makeExcel.setValue(row, 4, loanBal, "#,##0", "R"); // 放款餘額 col:3
				makeExcel.setValue(row, 5, rate, "#,##0.0000", "R"); // 利率 col:4
				makeExcel.setValue(row, 6, showRocDate(date, 1), "C"); // 繳息迄日 col:5
				makeExcel.setValue(row, 7, unpaid, "#,##0", "R"); // 已到期未繳利息 col:6
				makeExcel.setValue(row, 8, unexpired, "#,##0", "R"); // 未到期應收利息 col:7
				makeExcel.setValue(row, 9, custInt, "#,##0", "R"); // 歸戶利息合計 col:8

				// 計算小計、合計
				computeTotal(unpaid, unexpired, custInt);

				row++;

			} // for

		} else {
			makeExcel.setValue(row, 1, "本日無資料");
			row++;
		}
	}

	private void computeTotal(BigDecimal unpaid, BigDecimal unexpired, BigDecimal custInt) {

		countsSubtotal = countsSubtotal.add(BigDecimal.ONE); // 筆數小計
		unpaidSubtotal = unpaidSubtotal.add(unpaid);// 已到期未繳利息小計
		unexpiredSubtotal = unexpiredSubtotal.add(unexpired); // 未到期應收利息小計
		custIntSubtotal = custIntSubtotal.add(custInt); // 歸戶利息合計之小計

		countsTotal = countsTotal.add(BigDecimal.ONE); // 筆數合計
		unpaidTotal = unpaidTotal.add(unpaid); // 已到期未繳利息合計
		unexpiredTotal = unexpiredTotal.add(unexpired); // 未到期應收利息合計
		custIntTotal = custIntTotal.add(custInt); // 歸戶利息合計之合計
	}

	private void printSheetFooter() throws LogicException {

		ExcelFontStyleVo footerStyleVo = new ExcelFontStyleVo();
		footerStyleVo.setBold(true);

		makeExcel.setValue(row, 5, "科目小計", "C", footerStyleVo);// col:4
		makeExcel.setValue(row, 6, countsSubtotal + "筆", "R", footerStyleVo); // 筆數小計 col:5
		makeExcel.setValue(row, 7, unpaidSubtotal, "#,##0", "R", footerStyleVo); // 已到期未繳利息小計 col:6
		makeExcel.setValue(row, 8, unexpiredSubtotal, "#,##0", "R", footerStyleVo); // 未到期應收利息小計 col:7
		makeExcel.setValue(row, 9, custIntSubtotal, "#,##0", "R", footerStyleVo); // 歸戶利息合計小計 col:8

		// 畫框線
		makeExcel.setAddRengionBorder("A", 1, "I", row, 1);// ("A", 1, "H", row, 1)

		row++;
	}

	private void printTotal() throws LogicException {

		ExcelFontStyleVo totalStyleVo = new ExcelFontStyleVo();
		totalStyleVo.setBold(true);

		makeExcel.setValue(row, 5, "合計", "C", totalStyleVo); // col:4
		makeExcel.setValue(row, 6, countsTotal + "筆", "R", totalStyleVo); // 筆數合計 col:5
		makeExcel.setValue(row, 7, unpaidTotal, "#,##0", "R", totalStyleVo); // 已到期未繳利息合計 col:6
		makeExcel.setValue(row, 8, unexpiredTotal, "#,##0", "R", totalStyleVo); // 未到期應收利息合計 col:7
		makeExcel.setValue(row, 9, custIntTotal, "#,##0", "R", totalStyleVo); // 歸戶利息合計合計 col:8

		// 畫框線(只畫合計這一行)
		makeExcel.setAddRengionBorder("A", row, "I", row, 1);// ("A", 1, "H", row, 1)
	}
}
