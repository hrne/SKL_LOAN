package com.st1.itx.trade.LQ;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LQ002ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LQ002Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LQ002Report.class);

	@Autowired
	LQ002ServiceImpl LQ002ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	private BigDecimal totalLTV = BigDecimal.ZERO;

//	2020/12/10 Ted 
	public void exec(TitaVo titaVo) throws LogicException {
//		this.info("" + titaVo + "--" + titaVo.getEntDyI() + "--" + titaVo.getKinbr());

		String iENTDY = String.valueOf((Integer.valueOf(titaVo.get("ENTDY")) + 19110000));

		int qq = 0;

		// 2020
		int year = Integer.parseInt(iENTDY.substring(0, 4)) - 1911;
		//
		int month = Integer.parseInt(iENTDY.substring(4, 6));
		// sheet名稱使用 民國年、季

		if (month <= 3) {
			qq = 1;
		} else if (month <= 6) {
			qq = 2;
		} else if (month <= 9) {
			qq = 3;
		} else if (month <= 12) {
			qq = 4;
		}

		exportExcel_1(titaVo, year, month, qq);
		exportExcel_2(titaVo, year, month, qq);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel_1(TitaVo titaVo, int year, int month, int qq) throws LogicException {
		this.info("===========in exportExcel_1");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LQ002", "營建署季報貸款成數", "LQ002營建署季報貸款成數", "營建署季報-貸款成數結果.xls", "108年第3季營建署季報-貸款成數明細");

		makeExcel.setSheet("108年第3季營建署季報-貸款成數明細", year + "年第" + qq + "季營建署季報-貸款成數明細");

		List<Map<String, String>> findList = new ArrayList<>();

		try {
			findList = LQ002ServiceImpl.findAll(titaVo, year, month);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LQP002ServiceImpl.findDept error = " + errors.toString());
		}

		// 設定欄寬
		makeExcel.setWidth(1, 13);
		makeExcel.setWidth(2, 13);
		makeExcel.setWidth(3, 15);
		makeExcel.setWidth(4, 10);
		makeExcel.setWidth(5, 16);
		makeExcel.setWidth(6, 10);
		makeExcel.setWidth(7, 10);
		makeExcel.setWidth(8, 15);
		makeExcel.setWidth(9, 15);
		makeExcel.setWidth(10, 15);
//		makeExcel.setAddRengionBorder("A", 1, "I", 10, 1);

		// 起始列數
		int i = 2;
		int page = 1;
		int count = 0;
		double ltv = 0.00000000;
		if (findList.size() > 0) {

			// 格式
			DecimalFormat fm1 = new DecimalFormat("#,##0");

			for (Map<String, String> tLDVo : findList) {
				// 押品別1
				BigDecimal f0 = tLDVo.get("F0") == null || tLDVo.get("F1").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F0"));
				makeExcel.setValue(i, 1, f0.intValue(), "C");

				// 押品別2
				BigDecimal f1 = tLDVo.get("F1") == null || tLDVo.get("F1").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));
				makeExcel.setValue(i, 2, f1.intValue(), "C");

				// 押品號碼
				BigDecimal f2 = tLDVo.get("F2") == null || tLDVo.get("F2").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));
				makeExcel.setValue(i, 3, f2.intValue(), "C");

				// 地區
				BigDecimal f3 = tLDVo.get("F3") == null || tLDVo.get("F3").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(i, 4, f3.intValue(), "C");

				// 核准額度
				BigDecimal f4 = tLDVo.get("F4") == null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(i, 5, f4.intValue(), "#,##0", "R");

				// 戶號
				BigDecimal f5 = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F5"));
				makeExcel.setValue(i, 6, f5.intValue(), "C");

				// 額度
				BigDecimal f6 = tLDVo.get("F6") == null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F6"));
				makeExcel.setValue(i, 7, f6.intValue(), "C");

				// 首次撥款日
				BigDecimal f7 = tLDVo.get("F7") == null || tLDVo.get("F7").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F7"));
				makeExcel.setValue(i, 8, f7.intValue(), "C");

				// 評估淨值
				BigDecimal f8 = tLDVo.get("F8") == null || tLDVo.get("F8").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F8"));
				makeExcel.setValue(i, 9, f8.intValue(), "#,##0", "R");

				// 貸款成數
				BigDecimal f9 = tLDVo.get("F9") == null || tLDVo.get("F9").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F9"));
				makeExcel.setValue(i, 10, f9.doubleValue(), "R");
				ltv = ltv + f9.doubleValue();

				count++;

				i++;
				// xls列數不超過65535 所以取每5萬筆 建新分頁
				if (i > 50000) {

					// 設定列高
					makeExcel.setHeight(1, 20);
					i = 2;
					page++;
					makeExcel.newSheet(year + "年第" + qq + "季營建署季報-貸款成數明細" + page);

					// 設定欄寬
					makeExcel.setWidth(1, 13);
					makeExcel.setWidth(2, 13);
					makeExcel.setWidth(3, 15);
					makeExcel.setWidth(4, 10);
					makeExcel.setWidth(5, 16);
					makeExcel.setWidth(6, 10);
					makeExcel.setWidth(7, 10);
					makeExcel.setWidth(8, 15);
					makeExcel.setWidth(9, 15);
					makeExcel.setWidth(10, 15);

					makeExcel.setValue(1, 1, "押品別1", "C");
					makeExcel.setValue(1, 2, "押品別2", "C");
					makeExcel.setValue(1, 3, "押品號碼", "C");
					makeExcel.setValue(1, 4, "地區別", "C");
					makeExcel.setValue(1, 5, "核准額度", "C");
					makeExcel.setValue(1, 6, "戶號", "C");
					makeExcel.setValue(1, 7, "額度", "C");
					makeExcel.setValue(1, 8, "首次撥款日", "C");
					makeExcel.setValue(1, 9, "評估淨值", "C");
					makeExcel.setValue(1, 10, "貸款成數", "C");

				}
			}
			// 總貸放程術/
			totalLTV = new BigDecimal(ltv / count);
		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}

//		makeExcel.setAddRengionBorder("A", 1, "J", i - 1, 1);
		// 設定列高
		makeExcel.setHeight(1, 20);

	}

	private void exportExcel_2(TitaVo titaVo, int year, int month, int qq) throws LogicException {
		this.info("===========in exportExcel_2");

		List<Map<String, String>> adAllList = new ArrayList<>();

		try {
			adAllList = LQ002ServiceImpl.findTotal(titaVo, year, month);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 設定欄寬
		makeExcel.setWidth(2, 10);
		makeExcel.setWidth(3, 15);
		makeExcel.setWidth(4, 25);

		int i = 3;
		BigDecimal total = new BigDecimal("0");

		makeExcel.setSheet("108年第3季營建署季報-貸款成數結果", year + "年第" + qq + "季營建署季報-貸款成數結果");
//		makeExcel.setSheet("108年第3季營建署季報-貸款成數結果");
		if (adAllList.size() > 0) {

			for (Map<String, String> tLDVo : adAllList) {
//				this.info("2--" + tLDVo);

				// 地區別

				makeExcel.setValue(i, 2, tLDVo.get("F0"), "C");

				// 地區別代號
				BigDecimal f1 = tLDVo.get("F1") == null || tLDVo.get("F1").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));
				makeExcel.setValue(i, 3, f1.intValue(), "R");

				// 平均值 的貸款成數
				BigDecimal f2 = tLDVo.get("F2") == null || tLDVo.get("F2").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));
				makeExcel.setValue(i, 4, f2.doubleValue(), "R");
				total = total.add(f2);
				i++;

			}

			makeExcel.setValue(i, 3, "總計", "R");
			makeExcel.setValue(i, 4, totalLTV.doubleValue(), "R");
		} else {
			makeExcel.setValue(3, 4, "本日無資料");
		}
		// 設定列高
		makeExcel.setHeight(2, 20);
	}

}
