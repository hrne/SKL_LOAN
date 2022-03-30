package com.st1.ifx.file.excel.item.help;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.junit.Assert;

import com.st1.ifx.file.excel.ExcelHelper;
import com.st1.ifx.file.excel.SheetProcessor;

public class HelpDefSheetProcessor implements SheetProcessor {
	private boolean listWritten = false;
	private boolean done = false;
	private int listCount = 0;
	LinkedBlockingDeque<Object> master;

	public HelpDefSheetProcessor(LinkedBlockingDeque<Object> master) {
		this.master = master;
	}

	public int getListCount() {
		return listCount;
	}

	@Override
	public void process(HSSFSheet sheet) {
		HSSFCell cell;
		int cellsInRow = 0;
		HelpDef def = null;

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
					rowValues.add(ExcelHelper.extract(cell));
				} else {
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
		setDone(true);
	}

	synchronized public boolean isDone() {
		return done;
	}

	synchronized private void setDone(boolean value) {
		this.done = value;
	}

	private HelpDef initNewHelp(HSSFSheet sheet, HSSFRow row, HSSFCell segmentCell) {
		List<String> colNames = new ArrayList<String>();
		for (int z = 1; z < row.getPhysicalNumberOfCells(); z++) {
			colNames.add(row.getCell(z).toString());
		}
		return HelpDef.from(sheet.getSheetName(), ExcelHelper.extract(segmentCell), colNames);

	}

	private void process(HelpDef def) {
		Assert.assertNotNull("HelpDef", def);
		Assert.assertNotNull("master", master);
		master.add(def);
	}

}
