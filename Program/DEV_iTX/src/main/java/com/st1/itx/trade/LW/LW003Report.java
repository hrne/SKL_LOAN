package com.st1.itx.trade.LW;

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
import com.st1.itx.db.service.springjpa.cm.LW003ServiceImpl;

import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LW003Report extends MakeReport {

	@Autowired
	LW003ServiceImpl LW003ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	/**
	 * 執行報表產出
	 * 
	 * @param tbsdy 帳務日(西元)
	 * 
	 */

	public void exec(TitaVo titaVo, int tbsdy) throws LogicException {

		this.info("LW003Report exec");

		int q = 1;

		List<Map<String, String>> findWK = new ArrayList<>();

		// 確認工作月
		try {

			findWK = LW003ServiceImpl.findWorkMonth(titaVo, tbsdy);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LW003ServiceImpl.findWorkMonth error = " + errors.toString());
		}

		// 工作月(西元)
		int wkYear = Integer.valueOf(findWK.get(0).get("F0"));
		int wkYearRoc = wkYear - 1911;
		// 工作日
		int wkMonth = Integer.valueOf(findWK.get(0).get("F1"));
		// 工作日 (西元日期)
		int endDate = (Integer.valueOf(findWK.get(0).get("F2")) - 19110000) % 10000;
		String endDateStr = (endDate / 100) + "/" + (endDate % 100);

		// 依據工作季(13工獨立)，使用哪個底稿
		if (wkMonth <= 13) {
			q = 5;
		}
		if (wkMonth <= 12) {
			q = 4;
		}
		if (wkMonth <= 9) {
			q = 3;
		}
		if (wkMonth <= 6) {
			q = 2;
		}
		if (wkMonth <= 3) {
			q = 1;
		}
		// getEntDyI()帳務日 getKinbr()單位
		/**
		 * 開啟excel製檔<br>
		 * 
		 * @param titaVo     titaVo
		 * @param date       日期
		 * @param brno       單位
		 * @param filecode   檔案編號
		 * @param fileitem   檔案說明
		 * @param filename   輸出檔案名稱(不含副檔名,預設為.xlsx)
		 * @param sheetnanme 新建Sheet名稱
		 * @throws LogicException LogicException
		 */
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LW003", "房貸獎勵費用率統計表", "LW003房貸獎勵費用率",
				"LW003_底稿_房貸獎勵費用率" + q + ".xls", "獎勵費用率");

		this.info("use excel LW003_底稿房貸獎勵費用率" + q);

		// 設定值
		makeExcel.setValue(1, 1, wkYearRoc + "年放款部第" + wkMonth + "工作月房貸獎勵費用率統計表\n[獎勵費/個金總業績]～" + endDateStr);

		for (int i = 4, col = 1; col <= wkMonth; i++, col++) {

			makeExcel.setValue(2, i, wkYearRoc + "." + String.format("%02d", col));
		}

		List<Map<String, String>> data1 = new ArrayList<>();
		List<Map<String, String>> data2 = new ArrayList<>();
		List<Map<String, String>> data3 = new ArrayList<>();
		List<Map<String, String>> data4 = new ArrayList<>();
		List<Map<String, String>> data5 = new ArrayList<>();

		// 確認工作月
		try {
			// 個金總業績
			data1 = LW003ServiceImpl.findAll1(titaVo, wkYear, wkMonth);
			// 區部 獎勵金額
			data2 = LW003ServiceImpl.findAll2(titaVo, wkYear, wkMonth);
			// 通訓處 獎勵金額
			data3 = LW003ServiceImpl.findAll3(titaVo, wkYear, wkMonth);
			// 介紹人個人獎勵
			data4 = LW003ServiceImpl.findAll4(titaVo, wkYear, wkMonth);
			// 專銷制單位
			data5 = LW003ServiceImpl.findAll5(titaVo, wkYear, wkMonth);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LW003ServiceImpl.findAll error = " + errors.toString());
		}
//		if (data1.size() > 0 && data2.size() > 0 && data3.size() > 0 && data4.size() > 0 && data5.size() > 0) {
		if (true) {
			exportExcel(data1, wkYear, wkMonth, q, 1);// 個金總業績
			exportExcel(data2, wkYear, wkMonth, q, 2);// 區部 獎勵金額
			exportExcel(data3, wkYear, wkMonth, q, 3);// 通訓處 獎勵金額
			exportExcel(data4, wkYear, wkMonth, q, 4);// 介紹人個人獎勵
			exportExcel(data5, wkYear, wkMonth, q, 5);// 專銷制單位

			// 重整公式

			for (int x = 1; x <= wkMonth; x++) {
				makeExcel.formulaCalculate(8, 3 + x);
				makeExcel.formulaCalculate(13, 3 + x);
				makeExcel.formulaCalculate(18, 3 + x);
				makeExcel.formulaCalculate(22, 3 + x);
				makeExcel.formulaCalculate(23, 3 + x);
			}

			int rCol = q == 1 ? 7 : q == 2 ? 10 : q == 3 ? 13 : q == 4 ? 16 : 17;
			
			for (int y = 3; y <= 23; y++) {
				makeExcel.formulaCalculate(y, rCol);
			}

		} else {
			makeExcel.setValue(4, 4, "本日無資料");
		}

		makeExcel.close();

	}

	private void exportExcel(List<Map<String, String>> data, int wkYear, int wkMonth, int Quarter, int form)
			throws LogicException {

		this.info("exportExcel...");
		// 個金總業績
		if (form == 1) {
			int num = 1;
			for (Map<String, String> lw003Vo : data) {

				// 總業績金Performance
				BigDecimal performance = new BigDecimal(lw003Vo.get("F1").toString());

				makeExcel.setValue(3, 3 + num, performance, "#,##0");

				if (num == wkMonth) {
					break;
				}

				num++;
			}

		}
		// 區部 獎勵金額
		if (form == 2) {
			int num = 1;
			// 列數
			int row = 0;
			int col = 0;
			for (Map<String, String> lw003Vo : data) {
				// 以區部分 DeptCode
				String deptCode = lw003Vo.get("F0");
				// 工作季Quarter
				int quarter = Integer.valueOf(lw003Vo.get("F1"));
				// 獎勵金Bonus
				BigDecimal bonus = lw003Vo.get("F2").isEmpty() ? BigDecimal.ZERO
						: new BigDecimal(lw003Vo.get("F2").toString());

				// 判斷列數
				switch (deptCode) {
				case "A0B000":
					row = 9;
					break;
				case "A0E000":
					row = 10;
					break;
				case "A0F000":
					row = 11;
					break;
				case "A0M000":
					row = 12;
					break;
				}

				
				// 判斷欄位
				col = Quarter == 1 ? 6 : Quarter == 2 ? 9 : Quarter == 3 ? 12 : Quarter == 4 ? 15 : 16;
			
			
				
				if (quarter == 5) {
					return;
				}
				
				if (num == wkMonth) {
					break;
				}
				
				makeExcel.setValue(row, col, bonus, "#,##0");
				
				num++;
			}

		}

		// 通訊處 獎勵金
		if (form == 3) {
			int num = 1;
			// 列數
			int row = 0;
			int col = 0;

			for (Map<String, String> lw003Vo : data) {
				// 以通訊處分 DeptCode
				String deptCode = lw003Vo.get("F0");
				// 工作季Quarter
				int quarter = Integer.valueOf(lw003Vo.get("F1"));
				// 獎勵金Bonus
				BigDecimal bonus = lw003Vo.get("F2").isEmpty() ? BigDecimal.ZERO
						: new BigDecimal(lw003Vo.get("F2").toString());

				// 判斷列數
				switch (deptCode) {
				case "A0B000":
					row = 14;
					break;
				case "A0E000":
					row = 15;
					break;
				case "A0F000":
					row = 16;
					break;
				case "A0M000":
					row = 17;
					break;
				}
				
				// 判斷欄位
				col = Quarter == 1 ? 6 : Quarter == 2 ? 9 : Quarter == 3 ? 12 : Quarter == 4 ? 15 : 16;
			
				
				if (quarter == 5) {
					return;
				}
				
				if (num == wkMonth) {
					break;
				}
				
				makeExcel.setValue(row, col, bonus, "#,##0");
				
				num++;

			}
		}

		// 介紹人個人獎立
		if (form == 4) {
			int num = 1;
			for (Map<String, String> lw003Vo : data) {

				// 總業績金
				BigDecimal performance = lw003Vo.get("F1").isEmpty() ? BigDecimal.ZERO
						: new BigDecimal(lw003Vo.get("F1").toString());

				makeExcel.setValue(19, 3 + num, performance, "#,##0");

				if (num == wkMonth) {
					break;
				}
				num++;
			}

		}

		// 專銷制單位
		if (form == 5) {
			int num = 1;
			// 列數
			int row = 20;
			int col = 0;

			for (Map<String, String> lw003Vo : data) {
				// 工作季Quarter
				int quarter = Integer.valueOf(lw003Vo.get("F0"));
				// 獎勵金Bonus
				BigDecimal bonus = lw003Vo.get("F1").isEmpty() ? BigDecimal.ZERO
						: new BigDecimal(lw003Vo.get("F1").toString());

				// 判斷欄位
				col = Quarter == 1 ? 6 : Quarter == 2 ? 9 : Quarter == 3 ? 12 : Quarter == 4 ? 15 : 16;
				
				if (quarter == 5) {
					return;
				}
				
				if (num == wkMonth) {
					break;
				}

				makeExcel.setValue(row, col, bonus, "#,##0");
			}
		}

	}

}
