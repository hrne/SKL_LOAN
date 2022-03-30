package com.st1.ifx.file.excel;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.filter.SafeClose;

import org.junit.Assert;

public class ExcelSplitter {
	private static final Logger logger = LoggerFactory.getLogger(ExcelSplitter.class);

	private String excelFile;
	private SheetProcessor processor;

	public void setProcessor(SheetProcessor processor) {
		this.processor = processor;
	}

	public void setExcelFile(String excelFile) {
		this.excelFile = excelFile;
	}

	public void perform() {
		FileInputStream fis = null;
		POIFSFileSystem fs = null;
		HSSFWorkbook book = null;
		try {
			Assert.assertNotNull(excelFile);

			fis = new FileInputStream(excelFile);
			fs = new POIFSFileSystem(fis);
			book = new HSSFWorkbook(fs);
			HSSFSheet sheet = book.getSheetAt(0);

			HSSFCell cell = null;

			logger.debug("num of sheets:" + book.getNumberOfSheets());
			for (int w = 0; w < book.getNumberOfSheets(); w++)
				processSheet(book.getSheetAt(w));

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			SafeClose.close(book);
			SafeClose.close(fs);
			SafeClose.close(fis);
		}

	}

	private void processSheet(HSSFSheet sheet) {
		Assert.assertNotNull("processor", processor);
		Assert.assertNotNull("sheet", sheet);
		logger.debug("callling processor for sheet:" + sheet.getSheetName());
		processor.process(sheet);
		logger.debug("done for sheet:" + sheet.getSheetName());
	}
}
