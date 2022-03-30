package com.st1.help.excel;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.format.CellNumberFormatter;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.SafeClose;

public class ExcelConverter2 {
	static final Logger logger = LoggerFactory.getLogger(ExcelConverter2.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String excelFile = "D:/temp/helpList/excel/HELP.xls";
		ExcelConverter2 converter = new ExcelConverter2(excelFile);
		converter.perform();
	}

	public ExcelConverter2(String file) {
		this.excelFile = file;

	}

	private String excelFile;

	int listCount = 0;
	boolean listWritten = true;
	HelpDefProcessor processor = new HelpDefToOldStyleJsonProcessor();

	public String perform() {
		FileInputStream fis = null;
		POIFSFileSystem fs = null;
		HSSFWorkbook book = null;

		try {
			fis = new FileInputStream(excelFile);
			fs = new POIFSFileSystem(fis);
			book = new HSSFWorkbook(fs);
			HSSFSheet sheet = book.getSheetAt(1);
			HSSFCell cell = null;

			System.out.println("num of sheets:" + book.getNumberOfSheets());
			for (int w = 0; w < book.getNumberOfSheets(); w++)
				processSheet(book.getSheetAt(w));

			return processor.output();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return null;
		} finally {
			SafeClose.close(book);
			SafeClose.close(fs);
			SafeClose.close(fis);
		}

	}

	private void processSheet(HSSFSheet sheet) throws Exception {
		HSSFCell cell;
		int cellsInRow = 0;
		HelpDef def = null;

		// System.out.println("\n\n\nsheet " + (w) + ":" + sheetName);
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {

			HSSFRow row = sheet.getRow(i);
			cell = row.getCell(0);
			if (cell != null && cell.toString().trim().length() > 0) {
				if (def != null && !listWritten) {
					process(def);
				}
				cellsInRow = row.getPhysicalNumberOfCells();
				def = initNewHelp(sheet, row, cell);
				listWritten = false;
				listCount++;
				continue;
			}
			List<String> rowValues = new ArrayList<String>();
			for (int j = 1; j < cellsInRow; j++) {
				cell = row.getCell(j);
				if (cell != null) {
					rowValues.add(extractValue(cell));
				} else {
					// System.err
					// .printf("WARNNING!! null cell at sheet:%s, segment:%s, row:%d,
					// col:%d\n",
					// def.sheetName, def.segmentName, i, j);
					// System.err.flush();
					// throw new Exception("missing cell value");
					rowValues.add("");
				}
			}
			if (def != null)
				def.addValues(rowValues);
		}
		if (!listWritten) {
			process(def);
			listWritten = true;
		}

	}

	private static String extractValue(HSSFCell cell) throws Exception {
		String value;
		if (cell.getCellType() == CellType.NUMERIC) {
			String fmt = cell.getCellStyle().getDataFormatString();
			// System.out.print(" fmt:"+ fmt + " ");
			if (!fmt.startsWith("0")) {
				if (!(fmt.equals("@") || fmt.equals("General"))) {
					System.out.println("fmt:" + fmt);

					throw new Exception("unknown fmt:" + fmt);
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

	private HelpDef initNewHelp(HSSFSheet sheet, HSSFRow row, HSSFCell segmentCell) throws Exception {
		List<String> colNames = new ArrayList<String>();
		for (int z = 1; z < row.getPhysicalNumberOfCells(); z++) {
			colNames.add(row.getCell(z).toString());
		}
		return HelpDef.from(sheet.getSheetName(), extractValue(segmentCell), colNames);

	}

	private void process(HelpDef def) {

		try {
			processor.process(def);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

	}

}
