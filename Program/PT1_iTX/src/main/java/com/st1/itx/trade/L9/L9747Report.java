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
import com.st1.itx.db.service.springjpa.cm.L9747ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L9747Report extends MakeReport {

	@Autowired
	L9747ServiceImpl L9747ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	@Override
	public void printTitle() {

	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param dataDate 資料日期
	 * @return 
	 * @throws LogicException
	 * 
	 */
	public boolean exec(TitaVo titaVo, int dataDate) throws LogicException {
		List<Map<String, String>> L9747List = null;
		try {
			L9747List = L9747ServiceImpl.findAll(titaVo, dataDate);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9747ServiceImpl.testExcel error = " + errors.toString());
		}

		if (L9747List.size() == 0 || L9747List == null) {
			return false;
		}

		exportExcel(titaVo, L9747List, dataDate);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LMList, int dataDate) throws LogicException {
		this.info("L9747Report exportExcel");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9747";
		String fileItem = "催收及呆帳戶暫收款明細表";
		String fileName = "L9747催收及呆帳戶暫收款明細表";
		String defaultExcel = "L9747_底稿_催收及呆帳戶暫收款明細表.xlsx";
		String defaultSheet = "D961211M";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		if (LMList == null || LMList.isEmpty()) {

			makeExcel.setValue(3, 1, "本日無資料");

		} else {
			int row = 3;
			BigDecimal total = BigDecimal.ZERO;
			for (Map<String, String> tLDVo : LMList) {

				for (int i = 0; i <= 6; i++) {

					String value = tLDVo.get("F" + i);
					int col = i + 1;

					switch (i) {
					case 6:
						BigDecimal bd = getBigDecimal(value);
						total = total.add(bd);
						makeExcel.setValue(row, col, bd, "#,##0", "R");
						break;
					default:
						makeExcel.setValue(row, col, value);
						break;
					}
				} // for

				row++;
			} // for

			int year = dataDate / 10000;
			int month = dataDate /100 % 100;

			makeExcel.setMergedRegion(row + 3, row + 3, 1, 7);
			makeExcel.setValue(row + 3, 1,
					"一、擬 " + year + "年" + month + "月份呆帳戶之暫收款項金額共計 $" + formatAmt(total, 0) + "元入呆帳回收。");
			makeExcel.setMergedRegion(row + 4, row + 4, 1, 7);
			makeExcel.setValue(row + 4, 1, "二、陳核。");

		}

		makeExcel.close();
		// makeExcel.toExcel(sno);
	}

}
