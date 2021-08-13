package com.st1.itx.trade.LM;

import java.io.IOException;
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
import com.st1.itx.db.service.springjpa.cm.LM056ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM056Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM056Report.class);

	@Autowired
	LM056ServiceImpl lm056ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> fnAllList = new ArrayList<>();

		this.info("LM056Report exec");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM056", "表14-5會計部申報表", "LM056-表14-5會計部申報表", "LM056-表14-5會計部申報表.xlsx", "10804工作表");
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM056", "表14-5會計部申報表", "LM056、LM057_表14-5、表14-5 xls_會計部申報表", "LM056、LM057_表14-5、表14-5 xls_會計部申報表.xlsx", "10804工作表");
		makeExcel.setSheet("10804工作表", titaVo.get("ENTDY").substring(1, 6) + "工作表");

		




		try {
			fnAllList = lm056ServiceImpl.findAll(titaVo);
//			excelUnit();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM056ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {
			String fdnm = "";
			int row = 1;
			BigDecimal tot = new BigDecimal("0");

			for (Map<String, String> tLDVo : fnAllList) {
//				this.info("tLDVo-------->" + tLDVo.toString());
				row++;
				for (int i = 0; i < tLDVo.size(); i++) {
					
					fdnm = "F" + String.valueOf(i);
					switch (i) {
					case 1:
					case 2:
					case 3:
						// 戶號(數字右靠)
						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 4, 0);
						} else {
							makeExcel.setValue(row, i + 4, Integer.valueOf(tLDVo.get(fdnm)));
						}
						break;
					case 5:
						// 金額
						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 4, 0, "#,##0");
						} else {
							makeExcel.setValue(row, i + 4, Float.valueOf(tLDVo.get(fdnm)), "#,##0");
							tot = tot.add(new BigDecimal(tLDVo.get(fdnm)));
						}
						break;
					default:
						// 字串左靠
						makeExcel.setValue(row, i + 4, tLDVo.get(fdnm));
						break;
					}
				}
			}
			makeExcel.setValue(1, 8, row - 1);
			makeExcel.setValue(1, 9, tot, "#,##0");
		}
		
		makeExcel.setSheet("14-5申報表");
//		String wDay=String.valueOf(this.txBuffer.getTxCom().getTbsdyf());
//		String date=wDay.substring(0, 4).replaceFirst("^0", "")+"/"+wDay.substring(4,6).replaceFirst("^0", "")+"/"+wDay.substring(6, 8).replaceFirst("^0", "");
		String wDay=String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) );
		String date=wDay.substring(0, 4).replaceFirst("^0", "")+"/"+wDay.substring(4,6).replaceFirst("^0", "")+"/"+wDay.substring(6, 8).replaceFirst("^0", "");
		makeExcel.setValue(2, 4, date);


		
		
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
	
	public void excelUnit() throws IOException {
//		String path="C:\\SKL\\excel\\LM056-表14-5會計部申報表.xlsx";
//		FileInputStream stream = new FileInputStream(path);
//		@SuppressWarnings("resource")
//		HSSFWorkbook workbook = new HSSFWorkbook(stream);
//		HSSFSheet sheet= workbook.getSheet("14-5申報表");
//		HSSFRow row=sheet.createRow(0);
//		HSSFCell cell=row.createCell(0);
//		cell.setCellValue("合併列");
//		CellRangeAddress region=new CellRangeAddress(0, 0, 0, 5);
//		sheet.addMergedRegion(region);
//		
//		cell=row.createCell(6);
//		cell.setCellValue("合併行");
//		region=new CellRangeAddress(0, 5, 6, 6);
//		sheet.addMergedRegion(region);
//		HSSFSheet sheet = workbook.createSheet("Test");
//		HSSFRow row=sheet.createRow(2);
//		HSSFCell cell=row.createCell(4);
//		HSSFCellStyle style=workbook.createCellSty
//		cell.setCellStyle(style.getAlignment().RIGHT);
	}

}
