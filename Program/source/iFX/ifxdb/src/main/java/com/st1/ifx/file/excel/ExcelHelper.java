package com.st1.ifx.file.excel;

import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.format.CellNumberFormatter;
import org.apache.poi.ss.usermodel.CellType;

public class ExcelHelper {
	public static String extract(HSSFCell cell) {
		String value;
		if (cell.getCellType() == CellType.NUMERIC) {
			String fmt = cell.getCellStyle().getDataFormatString();
			if (!fmt.startsWith("0")) {
				if (!(fmt.equals("@") || fmt.equals("General"))) {
					System.out.println("fmt:" + fmt);
					throw new RuntimeException("unknown fmt:" + fmt);
				}
				CellNumberFormatter formatter = new CellNumberFormatter(fmt);
				double d = cell.getNumericCellValue();
				value = formatter.simpleFormat(d);

			} else {
				DecimalFormat df = new DecimalFormat(fmt);
				value = df.format(cell.getNumericCellValue());
			}

		} else {
			value = cell.getStringCellValue();
		}
		return value;
	}

}
