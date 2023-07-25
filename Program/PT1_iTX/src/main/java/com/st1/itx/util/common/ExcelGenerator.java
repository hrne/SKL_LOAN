package com.st1.itx.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.data.ExcelFontStyleVo;
import com.st1.itx.util.filter.SafeClose;

@Component
@Scope("prototype")
public class ExcelGenerator extends CommBuffer {

	/* excel輸出路徑 */
	@Value("${iTXOutFolder}")
	private String outputFolder = "";

	/* excel底稿路徑 */
	@Value("${iTXExcelFolder}")
	private String workingExcelFolder = "";

	/* DB服務注入 */
	@Autowired
	private TxFileService sTxFileService;

	// 資料明細
	private List<Map<String, Object>> listMap;

	private ExcelFontStyleVo outputFontStyleVo = new ExcelFontStyleVo();

	private final ExcelFontStyleVo emptyFontStyleVo = new ExcelFontStyleVo().init();

	private boolean cellHasStyle;

	private boolean needStyle;

	private boolean isDefaultExcel;

	private Workbook workbook;

	private Sheet sheet;

	private boolean isXls;

	private DataFormat dataFormat;

	private int rowNum;

	private float workRowHeight;

	private Map<ExcelFontStyleVo, Integer> fontStyleMap;

	private boolean isFirstTimeToStyleLimit;

	private long fileNo;

	private String outputFile;

	/**
	 * 畫表格框線(指定範圍)<br>
	 * ex:畫A1~B3:addRengionBorder(A,1,B,3,1)
	 * 
	 * @param firstCell 起始欄
	 * @param firstRow  起始列
	 * @param lastCell  結束欄
	 * @param lastRow   結束列
	 * @param point     框線粗細
	 */
	private void addRengionBorder(String firstCell, int firstRow, String lastCell, int lastRow, int point) {
		int fc = columnCal(firstCell) - 1;
		int lc = columnCal(lastCell) - 1;
		int fr = firstRow - 1;
		int lr = lastRow - 1;

		for (int procRow = fr; procRow <= lr; procRow++) {
			Row tRow = this.sheet.getRow(procRow);
			if (tRow == null) {
				tRow = this.sheet.createRow(procRow);
			}
			for (int procCol = fc; procCol <= lc; procCol++) {
				Cell cell = tRow.getCell(procCol);
				if (cell == null) {
					cell = tRow.createCell(procCol, CellType.BLANK);
				}
				outputFontStyleVo.setBorderAll((short) point);
				// 讀取原CellStyle設定寫進 outputFontStyleVo
				CellStyle originalCellStyle = cell.getCellStyle();
				cell.setCellStyle(setFontStyle(originalCellStyle, true));
			}
		}
	}

	private void changeSheetName(Map<String, Object> map) throws LogicException {
		String sheetName = map.get("s").toString();
		String newSheetName = map.get("n").toString();

		try {
			this.sheet = this.workbook.getSheet(sheetName);
			if (!newSheetName.isEmpty()) {
				int sheetindex = this.workbook.getSheetIndex(this.sheet);
				this.workbook.setSheetName(sheetindex, newSheetName);
			}
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)指定 SHEET (" + sheet + ") 不存在");
		}
		if (this.sheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)指定 SHEET (" + sheet + ") 不存在");
		}
	}

	/**
	 * 換算欄位(英文字換數字)
	 * 
	 * @param column 欄位英文字母(應在A~ZZ之間)
	 */
	private int columnCal(String column) {
		int col = 0;
		int columnLength = column.length();

		if (columnLength > 0 && columnLength <= 2) {
			// 轉大寫
			column = column.toUpperCase();

			char[] tokens = column.toCharArray();

			for (char token : tokens) {
				// 大寫A的編碼為65
				int index = Integer.valueOf(token) - 64;

				if (columnLength == 2) {
					col = index * 26;
				} else {
					col = col + index;
				}
				columnLength--;
			}
			if (columnLength > 0) {
				this.error("MakeExcel columnCal error 傳入字串 " + column + " 不在範圍內(應在A~Z之間).");
			}
		} else {
			if (columnLength > 2) {
				this.error("MakeExcel columnCal error 傳入參數長度太長(最大為兩碼)");
			} else {
				this.error("MakeExcel columnCal error 傳入參數長度為0");
			}
		}
		return col;
	}

	private void copyCellStyle(Row tmpSourceRow, Row tmpTargetRow) {
		for (Cell tmpSourceCell : tmpSourceRow) {
			int columnIndex = tmpSourceCell.getColumnIndex();
			CellStyle tmpSourceCellStyle = tmpSourceCell.getCellStyle();
			if (tmpSourceCellStyle != null) {
				short cellStyleIndex = tmpSourceCellStyle.getIndex();
				Cell tmpTargetColumn = tmpTargetRow.getCell(columnIndex);
				if (tmpTargetColumn == null) {
					tmpTargetColumn = tmpTargetRow.createCell(columnIndex);
				}
				tmpTargetColumn.setCellStyle(this.workbook.getCellStyleAt(cellStyleIndex));
			}
		}
	}

	/**
	 * 複製上一列整列格式
	 * 
	 * @param targetRow 複製到第幾列
	 * @param n         複製幾次
	 */
	private void copyLastRowCellStyle(int targetRow, int n) {
		n += targetRow;
		int sourceRow;
		Row tmpSourceRow;
		Row tmpTargetRow;
		for (; targetRow < n; targetRow++) {
			sourceRow = targetRow - 1;
			tmpSourceRow = this.sheet.getRow(sourceRow - 1);
			if (tmpSourceRow != null) {
				tmpTargetRow = this.sheet.getRow(targetRow - 1);
				if (tmpTargetRow == null) {
					tmpTargetRow = this.sheet.createRow(targetRow - 1);
				}
				copyCellStyle(tmpSourceRow, tmpTargetRow);
			}
		}
	}

	private void createSheet(Map<String, Object> map) {
		String sheetName = map.get("s").toString();
		this.sheet = this.workbook.createSheet(sheetName);
	}

	private void doSetHeight(int row, int height) {
		Row thisRow = sheet.getRow(row - 1);
		if (thisRow == null) {
			thisRow = sheet.createRow(row - 1);
		}
		short thisHeight;
		if (height == 0) {
			thisHeight = (short) -1;
		} else {
			thisHeight = (short) (height * 20);
		}
		thisRow.setHeight(thisHeight);
	}

	private void doSetWidth(int col, int width) {
		if (width == 0) {
			sheet.autoSizeColumn(col - 1);
		} else {
			this.sheet.setColumnWidth(col - 1, width * 256);
		}
	}

	@Override
	public void exec() throws LogicException {
		// nothing
	}

	private void formulaCalculate(Map<String, Object> map) {
		int calculateRow = Integer.parseInt(map.get("r").toString());
		int calculateColumn = Integer.parseInt(map.get("c").toString());
		this.info("MakeExcel.formulaCalculate calculateRow=" + calculateRow);
		this.info("MakeExcel.formulaCalculate calculateColumn=" + calculateColumn);
		Row pRow = this.sheet.getRow(calculateRow - 1);
		if (pRow != null) {
			Cell tmpCell = pRow.getCell(calculateColumn - 1);
			if (tmpCell != null && tmpCell.getCellType() == CellType.FORMULA) {
				this.info("MakeExcel.formulaCalculate do.");
				this.workbook.getCreationHelper().createFormulaEvaluator().evaluateFormulaCell(tmpCell);
			}
		}
	}

	public void generateExcel(long fileNo, String fileName) throws LogicException {
		this.info("MakeExcel.toExcel=" + fileNo);

		this.fileNo = fileNo;

		settingFromTxFile(fileName);

		init();

		String type;

		for (Map<String, Object> map : listMap) {
			type = map.get("t").toString();
			switch (type) {
			case "0":
				open(map);
				break;
			case "1":
				changeSheetName(map);
				break;
			case "2":
				createSheet(map);
				break;
			case "3":
				setValue(map);
				break;
			case "4":
				setWidthAndHeight(map);
				break;
			case "5":
				mergedRegion(map);
				break;
			case "6":
				setBorder(map);
				break;
			case "7":
				needStyle = false;
				break;
			case "8":
				shiftRows(map);
				break;
			case "9":
				formulaCalculate(map);
				break;
			case "A":
				setFormula(map);
				break;
			case "B":
				setLockColumn(map);
				break;
			case "C":
				setProtectSheet(map);
				break;
			default:
				// unknown type
				break;
			}
		}

		this.info("workbook numCellStyles = " + this.workbook.getNumCellStyles());

		output();

		this.info("MakeExcel finished.");
	}

	private void setProtectSheet(Map<String, Object> map) {
		this.sheet.protectSheet(map.get("pw").toString());
	}

	private void setLockColumn(Map<String, Object> map) throws LogicException {
		int rowStart = Integer.valueOf(map.get("rs").toString());
		int rowEnd = Integer.valueOf(map.get("re").toString());
		int columnStart = Integer.valueOf(map.get("cs").toString());
		int columnEnd = Integer.valueOf(map.get("ce").toString());
		int totalColumn = Integer.valueOf(map.get("tc").toString());

		this.info("lockColumn start");
		if (this.sheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)lockColumn sheet is null");
		}
		Map<Integer, CellStyle> cellStyleMap = new HashMap<>();
		for (int nowRow = rowStart; nowRow <= rowEnd; nowRow++) {
			Row processingRow = this.sheet.getRow(nowRow - 1);
			if (processingRow == null) {
				processingRow = this.sheet.createRow(nowRow - 1);
			}
			for (int nowColumn = columnStart; nowColumn <= totalColumn; nowColumn++) {
				this.info("nowColumn = " + nowColumn);
				Cell cell = null;
				cell = processingRow.getCell(nowColumn - 1);
				if (cell == null) {
					cell = processingRow.createCell(nowColumn - 1, CellType.BLANK);
				}
				CellStyle oriCellStyle = cell.getCellStyle();
				if (!cellStyleMap.containsKey(nowColumn)) {
					CellStyle tempStyle = this.workbook.createCellStyle();
					tempStyle.cloneStyleFrom(oriCellStyle);
					tempStyle.setLocked(nowColumn <= columnEnd); // 鎖定或不鎖定的判斷
					cellStyleMap.put(nowColumn, tempStyle);
				}
				cell.setCellStyle(cellStyleMap.get(nowColumn));
			}
		}
	}

	private void setFormula(Map<String, Object> map) throws LogicException {
		int targetRow = 0;
		int targetColumn = 0;
		Object value = null;
		String formula = "";
		String format = "";
		outputFontStyleVo = outputFontStyleVo.init();

		targetRow = Integer.valueOf(map.get("r").toString());
		targetColumn = Integer.valueOf(map.get("c").toString());
		value = map.get("v");
		formula = map.get("f").toString();
		format = map.get("ft").toString();

		outputFontStyleVo.setFormat(format);

		if (outputFontStyleVo.equals(emptyFontStyleVo) && !isDefaultExcel) {
			cellHasStyle = false;
		} else {
			cellHasStyle = true;
		}

		if (this.sheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)setValue sheet is null");
		}

		Row processingRow = this.sheet.getRow(targetRow - 1);
		if (processingRow == null) {
			processingRow = this.sheet.createRow(targetRow - 1);
		}

		int processingColumn = targetColumn - 1;

		Row nowRow = processingRow;
		if (formula == null) {
			formula = "";
		}

		Cell cell = null;

		cell = processingRow.getCell(processingColumn);
		if (cell == null) {
			cell = processingRow.createCell(processingColumn, CellType.NUMERIC);
		}
		BigDecimal bigDecValue = new BigDecimal(value.toString());
		cell.setCellValue(bigDecValue.doubleValue());
		cell.setCellFormula(formula);

		if (needStyle && cellHasStyle) {
			// 讀取原CellStyle設定寫進 outputFontStyleVo
			CellStyle originalCellStyle = cell.getCellStyle();
			cell.setCellStyle(setFontStyle(originalCellStyle, false));
		}

		// 取得當前表格內文字的高度，以文字高度去*1.5倍=適應文字的表格
		// 預設表格高度16 預設文字高度(大小)12 表格/文字=1.33 取整1.5
		float tempRH = (float) (this.workbook.getFontAt(cell.getCellStyle().getFontIndexAsInt()).getFontHeightInPoints()
				* 1 * 1.5);
		// 是否同一列
		if (this.rowNum != nowRow.getRowNum()) {
			this.rowNum = nowRow.getRowNum();
			workRowHeight = tempRH;
			nowRow.setHeightInPoints(workRowHeight);
		} else {
			if (workRowHeight < tempRH) {
				workRowHeight = tempRH;
				nowRow.setHeightInPoints(workRowHeight);
			}
		}

	}

	private void init() {
		outputFontStyleVo = new ExcelFontStyleVo();
		cellHasStyle = true;
		needStyle = true;
		isDefaultExcel = false;
		workbook = null;
		sheet = null;
		isXls = false;
		dataFormat = null;
		rowNum = -1;
		workRowHeight = 0;
		fontStyleMap = new HashMap<ExcelFontStyleVo, Integer>();
		isFirstTimeToStyleLimit = true;
	}

	private void mergedRegion(Map<String, Object> map) {
		int firstRow = Integer.parseInt(map.get("fr").toString());
		int lastRow = Integer.parseInt(map.get("lr").toString());
		int firstCol = Integer.parseInt(map.get("fc").toString());
		int lastCol = Integer.parseInt(map.get("lc").toString());
		this.sheet.addMergedRegion(new CellRangeAddress(firstRow - 1, lastRow - 1, firstCol - 1, lastCol - 1));
	}

	private void modifyOuputFontStyleVo(CellStyle originalCellStyle) {
		// 左右對齊方式
		HorizontalAlignment originalAlign = originalCellStyle.getAlignment();

		switch (originalAlign) {
		case LEFT:
			outputFontStyleVo.setAlign("L");
			break;
		case CENTER:
			outputFontStyleVo.setAlign("C");
			break;
		case RIGHT:
			outputFontStyleVo.setAlign("R");
			break;
		default:
			break;
		}

		String originalDataFormat = originalCellStyle.getDataFormatString();

		if (!originalDataFormat.equals("0") && !originalDataFormat.equals("General")) {
			outputFontStyleVo.setFormat(originalDataFormat);
		}

		int fontIndex = originalCellStyle.getFontIndexAsInt();

		Font tmpFont = this.workbook.getFontAt(fontIndex);

		// 字體大小
		outputFontStyleVo.setSize(tmpFont.getFontHeightInPoints());

		// 字體顏色
		int indexOfColors = tmpFont.getColor();
		if (indexOfColors >= 0 && indexOfColors <= 64) {
			IndexedColors indexColors = IndexedColors.fromInt(indexOfColors);
			outputFontStyleVo.setColor(indexColors.toString());
		}

		String fontName = tmpFont.getFontName();

		// 設定字型
		switch (fontName) {
		case "標楷體":
			outputFontStyleVo.setFont((short) 1);
			break;
		case "微軟正黑體":
			outputFontStyleVo.setFont((short) 2);
			break;
		case "Times New Roman":
			outputFontStyleVo.setFont((short) 3);
			break;
		case "Arial":
			outputFontStyleVo.setFont((short) 4);
			break;
		case "新細明體":
			outputFontStyleVo.setFont((short) 5);
			break;
		default:
			break;
		}

		// 粗體
		outputFontStyleVo.setBold(tmpFont.getBold());

		// 斜體
		outputFontStyleVo.setItalic(tmpFont.getItalic());

		// 底線
		Byte tmpUnderline = tmpFont.getUnderline();
		if (tmpUnderline.equals(Font.U_SINGLE)) {
			outputFontStyleVo.setUnderline(true);
		}

		// 多行
		outputFontStyleVo.setWrapText(originalCellStyle.getWrapText());

		// 表格 底(背景)色
		// 若實際有顏色才setBgColor
		Color originalBgColor = originalCellStyle.getFillBackgroundColorColor();

		int indexOfBgColor = originalCellStyle.getFillBackgroundColor();

		if (originalBgColor != null && !(originalBgColor instanceof HSSFColor) && indexOfBgColor > 0
				&& indexOfBgColor <= 64) {
			IndexedColors bgColor = IndexedColors.fromInt(indexOfBgColor);
			outputFontStyleVo.setBgColor(bgColor.toString());
		}
	}

	private void open(Map<String, Object> map) throws LogicException {
		String fileName = map.get("e").toString();
		if ("".equals(fileName)) {
			this.isXls = false;
			this.workbook = new XSSFWorkbook();
		} else {
			this.openFile(fileName, true);
			isDefaultExcel = true;
		}
		this.dataFormat = this.workbook.createDataFormat();
	}

	private void openFile(String fileName, boolean excelfolder) throws LogicException {
		String fna = "";

		if (excelfolder) {
			fna = workingExcelFolder + fileName;
		} else {
			fna = fileName;
		}

		int l = fileName.length();

		try (InputStream is = new FileInputStream(fna)) {
			if (".xls".equals(fileName.substring(l - 4, l))) {
				this.isXls = true;
				this.workbook = new HSSFWorkbook(is);
			} else if (".xlsx".equals(fileName.substring(l - 5, l))) {
				this.isXls = false;
				this.workbook = new XSSFWorkbook(is);
			} else {
				throw new LogicException(titaVo, "E0013", "(MakeExcel)" + fna + "，非預設EXCEL檔案格式");
			}
			SafeClose.close(is);
		} catch (IOException e) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)" + fna + "檔案不存在");
		}
	}

	private void output() throws LogicException {
		// 建立輸出流
		try (FileOutputStream fos = new FileOutputStream(new File(outputFile))) {
			this.workbook.write(fos);
			this.workbook.close();
		} catch (IOException e) {
			throw new LogicException(this.titaVo, "E0013", "(MakeExcel)輸出" + outputFile + "錯誤");
		}
	}

	private void setBorder(Map<String, Object> map) {
		// 起始欄
		String startCol = map.get("firstcell").toString();
		// 起始列
		int startRow = Integer.parseInt(map.get("firstrow").toString());
		// 結束欄
		String endCol = map.get("lastcell").toString();
		// 結束列
		int endRow = Integer.parseInt(map.get("lastrow").toString());
		// 框線 粗細
		int point = Integer.parseInt(map.get("point").toString());
		// 畫框線範圍
		addRengionBorder(startCol, startRow, endCol, endRow, point);
	}

	private CellStyle setFontStyle(CellStyle originalCellStyle, boolean readStyle) {
		// 預計要回傳的 CellStyle
		CellStyle resultCellStyle = null;

		int originalCellStyleIndex = originalCellStyle.getIndex();

		if (isDefaultExcel) {
			if (originalCellStyleIndex == 0) {
				// 原CellStyleIndex為0時,由outputFontStyleVo決定樣式
				readStyle = false;
			} else {
				readStyle = true;
				resultCellStyle = this.workbook.getCellStyleAt(originalCellStyleIndex);
			}
		}

		// 若需讀原格式設定,先讀取原格式設定寫進outputFontStyleVo
		if (readStyle) {
			modifyOuputFontStyleVo(originalCellStyle);
		}

		// CellStyle 的 Index
		int indexOfCellStyle;

		// 目前CellStyle數量
		int numOfCellStyle = this.workbook.getNumCellStyles();

		// xlsx CellStyle 數量限制為 64000
		int limitOfCellStyle = 64000;

		if (isXls) {
			// xls CellStyle 數量限制為 4000
			limitOfCellStyle = 4000;
		}

		// 若此ExcelFontStyleVo 已存在於 Map
		// 取與其對應的 CellStyle 的 Index
		// 並 回傳該CellStyle
		for (Iterator<Entry<ExcelFontStyleVo, Integer>> it = fontStyleMap.entrySet().iterator(); it.hasNext();) {
			Entry<ExcelFontStyleVo, Integer> tmpEntry = it.next();
			if (tmpEntry.getKey().equals(outputFontStyleVo)) {
				indexOfCellStyle = tmpEntry.getValue();
				resultCellStyle = this.workbook.getCellStyleAt(indexOfCellStyle);
				return resultCellStyle;
			}
		}

		// 若 ExcelFontStyleVo 不存在於 Map
		// 且 目前CellStyle數量 <= CellStyle數量限制
		// 且 不是讀取底稿
		// 則 創立新的CellStyle , 並將該 CellStyle的Index 記錄在 Map
		if (numOfCellStyle < limitOfCellStyle && !isDefaultExcel) {
			resultCellStyle = this.workbook.createCellStyle();
			indexOfCellStyle = resultCellStyle.getIndex();

			try {
				fontStyleMap.put(outputFontStyleVo.clone(), indexOfCellStyle);
			} catch (CloneNotSupportedException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("MakeExcel clone error = " + e.getMessage());
			}

			// 若 ExcelFontStyleVo 不存在於 Map
			// 且 目前CellStyle數量 <= CellStyle數量限制
			// 且 是讀取底稿
			// 則 使用原儲存格的CellStyle , 並將該 CellStyle的Index 記錄在 Map
		} else if (numOfCellStyle < limitOfCellStyle && isDefaultExcel) {

			// 若原儲存格的CellStyle為空,建立新CellStyle
			if (resultCellStyle == null) {
				resultCellStyle = this.workbook.createCellStyle();
			}

			indexOfCellStyle = resultCellStyle.getIndex();

			try {
				fontStyleMap.put(outputFontStyleVo.clone(), indexOfCellStyle);
			} catch (CloneNotSupportedException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("MakeExcel clone error = " + e.getMessage());
			}
		} else {
			// 否則,寫this.error,回傳第一個CellStyle
			if (isFirstTimeToStyleLimit) {
				this.error("setFontStyle error : CellStyle已超過" + limitOfCellStyle + ",不做Style設定,目前CellStyle數量為"
						+ numOfCellStyle);
				isFirstTimeToStyleLimit = false;
			}
			return this.workbook.getCellStyleAt(0);
		}

		// 此處新增數量會 <= limitOfCellStyle
		Font font = this.workbook.createFont();

		// 設定font大小
		if (outputFontStyleVo.getSize() != 0) {
			font.setFontHeightInPoints(outputFontStyleVo.getSize());
		} else {
			// 預設12
			font.setFontHeightInPoints((short) 12);
		}
		// 字體顏色
		if (!outputFontStyleVo.getColor().isEmpty()) {
			try {
				IndexedColors indexColors = IndexedColors.valueOf(outputFontStyleVo.getColor().toUpperCase());
				font.setColor(indexColors.getIndex());
			} catch (Exception e) {
				this.error("MakeExcel colors erros = " + e.getMessage() + "," + e.getStackTrace());
			}
		}

		// 設定字型
		switch (outputFontStyleVo.getFont()) {
		case 1:
			font.setFontName("標楷體");
			break;
		case 2:
			font.setFontName("微軟正黑體");
			break;
		case 3:
			font.setFontName("Times New Roman");
			break;
		case 4:
			font.setFontName("Arial");
			break;
		case 5:
			font.setFontName("新細明體");
			break;
		default:
			break;
		}

		// 粗體
		if (outputFontStyleVo.isBold()) {
			font.setBold(true);
		}

		// 斜體
		if (outputFontStyleVo.isItalic()) {
			font.setItalic(true);
		}

		// 底線
		if (outputFontStyleVo.isUnderline()) {
			font.setUnderline(Font.U_SINGLE);
			font.getUnderline();
		}

		// font設定裝進style
		resultCellStyle.setFont(font);

		// 設定格式.setDataFormat(this.df.getFormat(format));
		if (!outputFontStyleVo.getFormat().isEmpty()) {
			resultCellStyle.setDataFormat(this.dataFormat.getFormat(outputFontStyleVo.getFormat()));
		}

		// 框線粗細
		if (outputFontStyleVo.getBorderAll() > 0) {
			// 上右下左
			resultCellStyle.setBorderTop(BorderStyle.valueOf(outputFontStyleVo.getBorderAll()));
			resultCellStyle.setBorderRight(BorderStyle.valueOf(outputFontStyleVo.getBorderAll()));
			resultCellStyle.setBorderBottom(BorderStyle.valueOf(outputFontStyleVo.getBorderAll()));
			resultCellStyle.setBorderLeft(BorderStyle.valueOf(outputFontStyleVo.getBorderAll()));
		}

		// 左右對齊方式: 預設靠左
		switch (outputFontStyleVo.getAlign()) {
		case "R":
			resultCellStyle.setAlignment(HorizontalAlignment.RIGHT);
			break;
		case "L":
			resultCellStyle.setAlignment(HorizontalAlignment.LEFT);
			break;
		case "C":
			resultCellStyle.setAlignment(HorizontalAlignment.CENTER);
			break;
		default:
			break;
		}

		// 上下對齊方式: 預設置上
		resultCellStyle.setVerticalAlignment(VerticalAlignment.TOP);

		// 表格 底(背景)色
		if (!outputFontStyleVo.getBgColor().isEmpty()) {
			try {
				IndexedColors indexedColors = IndexedColors.valueOf(outputFontStyleVo.getBgColor().toUpperCase());
				resultCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				resultCellStyle.setFillForegroundColor(indexedColors.getIndex());
			} catch (Exception e) {
				this.error("MakeExcel set Indexed bgColors erros = " + e.getMessage() + "," + e.getStackTrace());
			}
		}

		// 多行
		if (outputFontStyleVo.isWrapText()) {
			resultCellStyle.setWrapText(true);
		}

		return resultCellStyle;
	}

	@SuppressWarnings("unchecked")
	private void setListMapFromJson(String fileData) throws LogicException {
		try {
			this.listMap = new ObjectMapper().readValue(fileData, ArrayList.class);
		} catch (IOException e) {
			throw new LogicException("EC009", "(MakeExcel)輸出檔(TxFile)序號:" + fileNo + ",資料格式");
		}
	}

	private void settingFromTxFile(String fileName) throws LogicException {

		TxFile tTxFile = sTxFileService.findById(fileNo);

		if (tTxFile == null) {
			throw new LogicException(titaVo, "EC002", "(MakeExcel)輸出檔(TxFile)序號:" + fileNo);
		}

		setListMapFromJson(tTxFile.getFileData());

		outputFile = outputFolder + tTxFile.getFileOutput();

		if (!"".equals(fileName)) {
			outputFile = outputFolder + fileName;
		}

		// 先刪除舊檔
		File file = new File(outputFile);

		if (file.exists() && file.isFile()) {
			try {
				Files.delete(file.toPath());
			} catch (IOException e) {
				this.error("MakeExcel Files.delete error =" + e.getMessage());
			}
		}
	}

	private void setValue(Map<String, Object> map) throws LogicException {
		int tmpRow = 0;
		int col = 0;
		int lastRow = 0;
		int lastCol = 0;
		Object val = null;
		outputFontStyleVo = outputFontStyleVo.init();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			switch (entry.getKey()) {
			case "r":
				// 行
				tmpRow = Integer.valueOf(entry.getValue().toString());
				break;
			case "c":
				// 列
				col = Integer.valueOf(entry.getValue().toString());
				break;
			case "lr":
				// 行
				lastRow = Integer.valueOf(entry.getValue().toString());
				break;
			case "lc":
				// 行
				lastCol = Integer.valueOf(entry.getValue().toString());
				break;
			case "v":
				// 輸出字串
				val = entry.getValue();
				break;
			case "f":
				// 格式編碼
				outputFontStyleVo.setFormat(entry.getValue().toString());
				break;
			case "size":
				// 文字大小
				outputFontStyleVo.setSize(Short.valueOf(entry.getValue().toString()));
				break;
			case "fontType":
				// 字型
				outputFontStyleVo.setFont(Short.valueOf(entry.getValue().toString()));
				break;
			case "color":
				// 文字顏色
				outputFontStyleVo.setColor(entry.getValue().toString());
				break;
			case "I":
				// 斜體
				outputFontStyleVo.setItalic((boolean) entry.getValue());
				break;
			case "B":
				// 粗體
				outputFontStyleVo.setBold((boolean) entry.getValue());
				break;
			case "U":
				// 底線
				outputFontStyleVo.setUnderline((boolean) entry.getValue());
				break;
			case "borderAll":
				// 框線粗細
				outputFontStyleVo.setBorderAll(Short.valueOf(entry.getValue().toString()));
				break;
			case "align":
				// 文字位置
				outputFontStyleVo.setAlign(entry.getValue().toString());
				break;
			case "bgColor":
				// 背景顏色
				outputFontStyleVo.setBgColor(entry.getValue().toString());
				break;
			default:
				break;
			}
		}
		if (outputFontStyleVo.equals(emptyFontStyleVo) && !isDefaultExcel) {
			cellHasStyle = false;
		} else {
			cellHasStyle = true;
		}

		this.setValueToExcel(tmpRow, col, lastRow, lastCol, val);
	}

	private void setValueToExcel(int row, int col, int lastRow, int lastCol, Object val) throws LogicException {
		if (this.sheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)setValue sheet is null");
		}

		Row prow = this.sheet.getRow(row - 1);
		if (prow == null) {
			prow = this.sheet.createRow(row - 1);
		}

		Row nowRow = prow;
		if (val == null) {
			val = "";
		}

		String[] checkVal = val.toString().split("\n");

		float fontWrap;

		if (checkVal.length == 0) {
			fontWrap = 1;
		} else {
			fontWrap = checkVal.length;
		}

		if (lastRow > 0 && lastCol > 0) {
			CellRangeAddress cra = new CellRangeAddress(row - 1, lastRow - 1, col - 1, lastCol - 1);
			this.sheet.addMergedRegion(cra);
		}

		Cell cell;

		if (val instanceof String) {
			cell = prow.getCell(col - 1);
			if (cell == null) {
				cell = prow.createCell(col - 1, CellType.STRING);
			}
			outputFontStyleVo.setWrapText(true);
			cellHasStyle = true;
			cell.setCellValue(val.toString());
		} else {
			cell = prow.getCell(col - 1);
			if (cell == null) {
				cell = prow.createCell(col - 1, CellType.NUMERIC);
			}
			BigDecimal bigDecValue = new BigDecimal(val.toString());
			cell.setCellValue(bigDecValue.doubleValue());
		}
		if (needStyle && cellHasStyle) {
			// 讀取原CellStyle設定寫進 outputFontStyleVo
			CellStyle originalCellStyle = cell.getCellStyle();
			cell.setCellStyle(setFontStyle(originalCellStyle, false));
		}

		// 取得當前表格內文字的高度，以文字高度去*1.5倍=適應文字的表格
		// 預設表格高度16 預設文字高度(大小)12 表格/文字=1.33 取整1.5
		float tempRH = (float) (this.workbook.getFontAt(cell.getCellStyle().getFontIndexAsInt()).getFontHeightInPoints()
				* fontWrap * 1.5);
		// 是否同一列
		if (this.rowNum != nowRow.getRowNum()) {
			this.rowNum = nowRow.getRowNum();
			workRowHeight = tempRH;
			nowRow.setHeightInPoints(workRowHeight);
		} else {
			if (workRowHeight < tempRH) {
				workRowHeight = tempRH;
				nowRow.setHeightInPoints(workRowHeight);
			}
		}
	}

	private void setWidthAndHeight(Map<String, Object> map) {
		String c = map.get("c") == null ? null : map.get("c").toString(); // col
		String w = map.get("w") == null ? null : map.get("w").toString(); // width
		String r = map.get("r") == null ? null : map.get("r").toString(); // row
		String h = map.get("h") == null ? null : map.get("h").toString(); // height
		if (c != null && !c.isEmpty() && w != null && !w.isEmpty()) {
			int col = Integer.parseInt(c);
			int width = Integer.parseInt(w);
			doSetWidth(col, width);
		}
		if (r != null && !r.isEmpty() && h != null && !h.isEmpty()) {
			int tmpRow = Integer.parseInt(r);
			int heigtht = Integer.parseInt(h);
			doSetHeight(tmpRow, heigtht);
		}
	}

	private void shiftRows(Map<String, Object> map) {
		int shiftRowFrom = Integer.parseInt(map.get("srf").toString());
		int shiftCounts = Integer.parseInt(map.get("n").toString());
		int finalRow = this.sheet.getLastRowNum();
		this.sheet.shiftRows(shiftRowFrom - 1, finalRow, shiftCounts);
		copyLastRowCellStyle(shiftRowFrom, shiftCounts);
	}
}