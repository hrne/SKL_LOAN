package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM030ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")
public class LM030Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM030Report.class);

	@Autowired
	LM030ServiceImpl lM030ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	DateUtil dateUtil;

	int row = 3;
	public Boolean exec(TitaVo titaVo) throws LogicException {


//		TxBizDate bizDate = new TxBizDate();
//		dateUtil.init();
// 		dateUtil.setDate_1(this.txBuffer.getTxBizDate().getNbsDyf());
//		bizDate = dateUtil.getForTxBizDate();		
//		dateUtil.init();
//		dateUtil.setDate_1(bizDate.getMfbsDyf());		
//		this.info("下下月月初營業日=" +  bizDate.getNbsDyf());

		List<Map<String, String>> listLM030 = null;

		try {
			listLM030 = lM030ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, listLM030);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM030ServiceImpl.testExcel error = " + errors.toString());
			return false;
		}
		
		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listLM030) throws LogicException {
		this.info("LM030Report exportExcel");
		
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM030", "轉催收明細總表", "LM030轉催收案件明細_核定總表",
				"轉催收案件明細_核定總表.xlsx", "10804");
		String yy = titaVo.get("ENTDY").substring(1, 4);
		String mm = titaVo.get("ENTDY").substring(4, 6);
		makeExcel.setSheet("10804", yy + mm);
		makeExcel.setValue(1, 1, yy + "." + mm + "  轉催收明細總表");
		BigDecimal total = BigDecimal.ZERO;
		if (listLM030.size() == 0) {
			makeExcel.setValue(3, 1, "本日無資料");
		} 
		
		for (Map<String, String> tLDVo : listLM030) {
			
			String ad = "";
			int col = 0;
			for (int i = 0; i < tLDVo.size(); i++) {

				ad = "F" + col;
				col++;
				switch (col) {
				case 6:
				case 11:
				case 12:
					int Cyear = (Integer.parseInt(tLDVo.get(ad)) - 19110000) / 10000;
					makeExcel.setValue(row, col, tLDVo.get(ad) == null? " ": String.valueOf(Cyear) + tLDVo.get(ad).substring(4, 8));
					break;
				case 7:
				case 8:
					// 金額
					makeExcel.setValue(row, col,
							tLDVo.get(ad) == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)), "#,##0");
					total = total.add(tLDVo.get(ad) == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)));
					break;
				case 9:
					// 利率
					makeExcel.setValue(row, col,
							tLDVo.get(ad) == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)), "#,##0.0000");
					break;
				case 10:
					if(tLDVo.get(ad) != null) {
						if(Integer.parseInt(tLDVo.get(ad)) != 0) {
						    int year = Integer.parseInt(tLDVo.get(ad).substring(0, 4)) - 1911;
							makeExcel.setValue(row, col, year + tLDVo.get(ad).substring(4, 8));
						}
						else {
							makeExcel.setValue(row, col, tLDVo.get(ad));
						}
					}
					break;
				default:
					makeExcel.setValue(row, col, tLDVo.get(ad) == null? " ": tLDVo.get(ad));
					break;
				}
			} // for
			row++;
		} // for
		row += 10;
		DecimalFormat df1 = new DecimalFormat("#,##0");
		// makeExcel.setMergedRegion(row, row, 1, 5);
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 1, "放款管理課", "C");
//		makeExcel.setAddRengionBorder("A", row, "M", row, 2);
		// makeExcel.setMergedRegion(row, row, 6, 9);
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 6, "放款服務課", "C");
//		makeExcel.setAddRengionBorder("A", row + 1, "J", row + 1, 2);
		// makeExcel.setMergedRegion(row, row, 10, 13);
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 10, "部室主管", "C");
//		makeExcel.setAddRengionBorder("J", row, "M", row, 2);
		// makeExcel.setMergedRegion(row + 1, row + 1, 1, 5);
		// makeExcel.setMergedRegion(row + 2, row + 2, 1, 5);
		makeExcel.setFontType(1);
		if(listLM030.size() == 0) {
			makeExcel.setValue(row + 1, 1, "一、經查本月逾期放款，無清償期屆滿六個\r\n" + 
					"    月需轉列催收款之案件。\r\n" + 
					"二、陳核。 ");
		}else {
			makeExcel.setValue(row + 1, 1, "一、經查本月逾期放款清償期屆滿六個月案件\r\n" + 
					"    ，依規將本金及應收利息轉列催收款項，\r\n" + 
					"    金額共計 $" + df1.format(total) + "元。");
			makeExcel.setValue(row + 2, 1, "二、本月轉入催收款案件未發生『撥款後繳款\r\n" + 
					"    期數未滿18個月即轉入催收戶』之情事。\r\n" + 
					"三、陳核。 ");
		}
//		makeExcel.setAddRengionBorder("A", row + 1, "E", row + 2, 2);
		// makeExcel.setMergedRegion(row + 1, row + 2, 6, 9);
//		makeExcel.setAddRengionBorder("F", row + 1, "I", row + 2, 2);
		// makeExcel.setMergedRegion(row + 1, row + 2, 10, 13);
//		makeExcel.setAddRengionBorder("J", row + 1, "M", row + 2, 2);
		// makeExcel.setMergedRegion(row + 3, row + 3, 5, 6);
		makeExcel.setColor("RED");
		makeExcel.setValue(row + 3, 5, listLM030.size(), "C");
		makeExcel.setColor("RED");
		makeExcel.setValue(row + 3, 7, total, "#,##0");
		makeExcel.setValue(row + 5, 1, "一、李案為年期延長後再協議案件，因未符合免列報逾放條件，故轉列催收款項。");
		makeExcel.setValue(row + 13, 5, listLM030.size());
		makeExcel.setValue(row + 13, 7, total, "#,##0");
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
//	private void setFinalPart(int row, int size, BigDecimal total) throws LogicException {
//		row += 10;
//		DecimalFormat df1 = new DecimalFormat("#,##0");
//		makeExcel.setMergedRegion(row, row, 1, 5);
//		makeExcel.setFontType(1);
//		makeExcel.setSize(14);
//		makeExcel.setIBU("B");
//		makeExcel.setValue(row, 1, "放款管理課", "C");
//		makeExcel.setAddRengionBorder("A", row, "E", row, 2);
//		makeExcel.setMergedRegion(row, row, 6, 9);
//		makeExcel.setFontType(1);
//		makeExcel.setSize(14);
//		makeExcel.setIBU("B");
//		makeExcel.setValue(row, 6, "放款服務課", "C");
//		makeExcel.setAddRengionBorder("F", row, "I", row, 2);
//		makeExcel.setMergedRegion(row, row, 10, 13);
//		makeExcel.setFontType(1);
//		makeExcel.setSize(14);
//		makeExcel.setIBU("B");
//		makeExcel.setValue(row, 10, "部室主管", "C");
//		makeExcel.setAddRengionBorder("J", row, "M", row, 2);
//		makeExcel.setMergedRegion(row + 1, row + 1, 1, 5);
//		makeExcel.setMergedRegion(row + 2, row + 2, 1, 5);
//		makeExcel.setFontType(1);
//		if(size == 0) {
//			makeExcel.setValue(row + 1, 1, "一、經查本月逾期放款，無清償期屆滿六個\r\n" + 
//					"    月需轉列催收款之案件。\r\n" + 
//					"二、陳核。 ");
//		}else {
//			makeExcel.setValue(row + 1, 1, "一、經查本月逾期放款清償期屆滿六個月案件\r\n" + 
//					"    ，依規將本金及應收利息轉列催收款項，\r\n" + 
//					"    金額共計 $" + df1.format(total) + "元。");
//			makeExcel.setValue(row + 2, 1, "二、本月轉入催收款案件未發生『撥款後繳款\r\n" + 
//					"    期數未滿18個月即轉入催收戶』之情事。\r\n" + 
//					"三、陳核。 ");
//		}
//		makeExcel.setAddRengionBorder("A", row + 1, "E", row + 2, 2);
//		makeExcel.setMergedRegion(row + 1, row + 2, 6, 9);
//		makeExcel.setAddRengionBorder("F", row + 1, "I", row + 2, 2);
//		makeExcel.setMergedRegion(row + 1, row + 2, 10, 13);
//		makeExcel.setAddRengionBorder("J", row + 1, "M", row + 2, 2);
//		makeExcel.setMergedRegion(row + 3, row + 3, 5, 6);
//		makeExcel.setColor("RED");
//		makeExcel.setValue(row + 3, 5, size, "C");
//		makeExcel.setColor("RED");
//		makeExcel.setValue(row + 3, 7, total, "#,##0");
//		makeExcel.setValue(row + 5, 1, "一、李案為年期延長後再協議案件，因未符合免列報逾放條件，故轉列催收款項。");
//	}
	private int findLastBsDay(int today) throws LogicException {
		dateUtil.init();
		while (true) {
			dateUtil.setDate_1(today);
			dateUtil.setDays(-1);
			today = dateUtil.getCalenderDay();
			if (!dateUtil.isHoliDay()) {
				break;
			}
		}
		return today;
	}

}
