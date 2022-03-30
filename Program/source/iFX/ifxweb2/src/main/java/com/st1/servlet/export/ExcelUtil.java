package com.st1.servlet.export;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Double;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	private HSSFWorkbook workbook = null;
	private CellStyle cellStyle = null;
	private HSSFDataFormat df = null;

	public ExcelUtil() {
	}

	/**
	 * 導出EXCEL文件
	 * 
	 * @param outputStream 輸出流
	 * @param cols         行
	 * @param sql          SQL語句
	 */
	public void export(OutputStream outputStream, String[][] cols) {
		if (outputStream != null && cols != null && cols.length > 0) {
			try {
				this.workbook = new HSSFWorkbook();
				this.cellStyle = this.workbook.createCellStyle();
				this.df = workbook.createDataFormat();

				Sheet writableSheet = workbook.createSheet(" Sheet 1");
				// 寫columns名稱
				// writeRow(writableSheet, titles, 0);// 寫入標題

				for (int i = 0; i < cols.length - 1; i++)
					writeRow(writableSheet, cols[i], i, cols[cols.length - 1]);

				// 寫入Exel工作表
				workbook.write(outputStream);
				outputStream.flush();

				// 關閉Excel工作薄對象
				workbook.close();
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}
	}

	private void writeRow(Sheet ws, String[] contents, int rowNum, String[] types) {

		boolean NumFg = false;
		double Numeric = 0;
		String content, type = "";

		Row row = ws.createRow(rowNum);
		Cell cell = null;

		if (ws != null && contents != null) {
			for (int i = 0; i < contents.length; i++) {
				content = contents[i];
				if (content.trim().equals("HaderX"))
					continue;

				if (contents[contents.length - 1].equals("HaderX"))
					type = "X";
				else
					type = types[i].toLowerCase();

				logger.info("content : [" + content + "]");
				logger.info("type    : [" + type + "]");

				if (type.toUpperCase().equals("N") || type.toUpperCase().equals("M") || type.toUpperCase().equals("+N") || type.toUpperCase().equals("+M"))
					try {
						Numeric = Double.parseDouble(content.trim());
						NumFg = true;
						logger.info("parseDouble OK!!" + NumFg);
					} catch (NumberFormatException e) {
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						logger.error(errors.toString());
					}

				if (NumFg) {
					cell = row.createCell(i, CellType.NUMERIC);
					this.cellStyle.setDataFormat(this.df.getFormat("#,#0"));
					this.cellStyle.setAlignment(HorizontalAlignment.RIGHT);
					cell.setCellStyle(this.cellStyle);
					cell.setCellValue(Numeric);
					NumFg = !NumFg;
				} else {
					cell = row.createCell(i, CellType.STRING);
					cell.setCellValue(content.trim());
				}

			}
		}
	}
}