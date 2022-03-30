package com.st1.ifx.file.excel.item.help2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.st1.ifx.file.excel.ExcelHelper;
import com.st1.ifx.file.excel.ExcelSplitter;
import com.st1.ifx.file.excel.SheetProcessor;
import com.st1.ifx.file.excel.item.help.HelpDef;
import com.st1.ifx.file.item.general.GeneralLine;
import com.st1.ifx.filter.FilterUtils;
import com.st1.util.cbl.CobolProcessor;

@Component
@Scope("prototype")
public class XlsConverter {
	private static final Logger logger = LoggerFactory.getLogger(XlsConverter.class);

	@Value("${import.inbox}")
	private String outputFolder;

	@Value("${import.backupFolder}")
	private String backupFolder;

	@Value("${import.convertXlsToExt}")
	private String extName = ".t12";

	@ServiceActivator
	public void convert(Message msg) throws Throwable {
		logger.info("converting msg:{}", msg);
		File file = (File) msg.getPayload();
		logger.info(FilterUtils.escape("convert " + file.getAbsolutePath() + " to " + outputFolder));

		convertToExcel(file.getAbsolutePath());
		moveFileToBackupFolder(file.getAbsolutePath());
	}

	private void convertToExcel(String filePath) throws Exception {
		String name = FilenameUtils.getBaseName(filePath);
		String targetFilePath = FilenameUtils.concat(outputFolder, name + extName);
		String tmpFilePath = targetFilePath + "~";

		logger.info("target path:" + targetFilePath);

		String[] ss = name.split("_");
		ss = ss[1].split("-");
		final String help = ss[0];
		logger.info("Help topic:" + help);

		final List<GeneralLine> results = new ArrayList<GeneralLine>();
		results.add(GeneralLine.makeDeleteAll(help));

		ExcelSplitter splitter = new ExcelSplitter();
		splitter.setExcelFile(filePath);
		splitter.setProcessor(new SheetProcessor() {
			@Override
			public void process(HSSFSheet sheet) {

				HSSFCell cell;
				int cellsInRow = 0;
				HelpDef def = null;
				boolean listWritten = false;
				logger.info(sheet.getSheetName() + ", rows:" + sheet.getPhysicalNumberOfRows());
				for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
					// logger.info("process row:"+ i);
					HSSFRow row = sheet.getRow(i);
					if (row == null)
						break;
					cell = row.getCell(0);
					if (cell != null && cell.toString().trim().length() > 0) {
						if (def != null && !listWritten) {
							convertToLines(def, results);
						}
						cellsInRow = row.getPhysicalNumberOfCells();
						def = initNewHelp(sheet, row, cell);
						listWritten = false;
						continue;
					}
					List<String> rowValues = new ArrayList<String>();
					for (int j = 1; j < cellsInRow; j++) {
						cell = row.getCell(j);
						if (cell != null) {
							rowValues.add(ExcelHelper.extract(cell));
						} else {
							rowValues.add("");
						}
					}
					if (def != null)
						def.addValues(rowValues);
				}
				if (!listWritten) {
					convertToLines(def, results);
					listWritten = true;
				}
			}
		});
		splitter.perform();
		List<String> lines = new ArrayList<String>();
		for (GeneralLine g : results) {
			String s = CobolProcessor.generate(g);
			lines.add(s);

		}
		File tmpFile = new File(tmpFilePath);
		FileUtils.writeLines(tmpFile, lines, false);
		FileUtils.moveFile(tmpFile, new File(targetFilePath));

	}

	private void convertToLines(HelpDef def, List<GeneralLine> results) {
		String s = null;
		try {
			logger.info("process " + def.getSheetName() + "." + def.getSegmentName());

			String name, value;
			// add HEAD line
			String segmentName = def.getSegmentName();
			results.add(GeneralLine.makeHead(segmentName, def.getColNames().toArray(new String[0])));

			// add data lines
			for (List<String> list : def.getValues()) {
				String key = list.remove(0);
				results.add(GeneralLine.makeData(segmentName, key, list.toArray(new String[0])));
			}

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
		}

	}

	private HelpDef initNewHelp(HSSFSheet sheet, HSSFRow row, HSSFCell segmentCell) {
		List<String> colNames = new ArrayList<String>();
		for (int z = 1; z < row.getPhysicalNumberOfCells(); z++) {
			colNames.add(row.getCell(z).toString());
		}
		return HelpDef.from(sheet.getSheetName(), ExcelHelper.extract(segmentCell), colNames);

	}

	private void moveFileToBackupFolder(String filePath) {
		// errorFolder
		String fileName = FilenameUtils.getName(filePath);
		String newFilePath = FilenameUtils.concat(backupFolder, fileName);
		File f = new File(newFilePath);
		if (f.exists()) {
			logger.warn(newFilePath + " exists, delete it");
			f.delete();
		}

		try {
			FileUtils.moveFileToDirectory(new File(FilterUtils.filter(filePath)),
					new File(FilterUtils.filter(backupFolder)), true);
		} catch (IOException e) {
			logger.error("move file", e);
		}

		logger.info(fileName + " is moved to " + newFilePath);
	}

	public static void main(String[] args) throws Exception {
		GeneralLine g = new GeneralLine();
		g.setHelp("CIFDEF");
		g.setKi("空白              ");
		g.setContent("為本國編號系列且正確無誤者                                         ");
		String s = CobolProcessor.generate(g);
		logger.info(s);

	}

}
