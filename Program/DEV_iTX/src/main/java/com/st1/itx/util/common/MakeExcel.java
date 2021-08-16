package com.st1.itx.util.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.data.ExcelFontStyleVo;

/**
 * 
 * ----------------------- MakeExcel 產生EXCEL檔案共用工具 ------------------*
 * 
 * @author eric chang
 *
 */

@Component("makeExcel")
@Scope("prototype")

public class MakeExcel extends CommBuffer {

	private String brno = "";

	private Cell cell = null;

	private boolean cellHasStyle = true;

	private int date = 0;

	private DataFormat df = null;

	private final ExcelFontStyleVo emptyFontStyleVo = new ExcelFontStyleVo().init();

	private String fileCode = "";

	private String fileItem = "";

	private String fileName = "";

	private HashMap<ExcelFontStyleVo, Integer> fontStyleMap = new HashMap<ExcelFontStyleVo, Integer>();

	private ExcelFontStyleVo inputFontStyleVo = new ExcelFontStyleVo();

	private boolean isDefaultExcel = false;

	private boolean isFirstTimeToStyleLimit = true;

	// 資料明細
	List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

	private boolean needStyle = true;

	/* excel輸出路徑 */
	@Value("${iTXOutFolder}")
	private String outputFolder = "";

	private ExcelFontStyleVo outputFontStyleVo = new ExcelFontStyleVo();

	private int rowNum = -1;

	private Sheet sheet = null;

	/* DB服務注入 */
	@Autowired
	TxFileService sTxFileService;

	private float tempRowHeight = 0;

	private Workbook wb = null;

	/* excel底稿路徑 */
	@Value("${iTXExcelFolder}")
	private String workingExcelFolder = "";

	private boolean xls = false;

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

				cell = tRow.getCell(procCol);

				if (cell == null) {
					cell = tRow.createCell(procCol, CellType.BLANK);
				}

				outputFontStyleVo.setBorderAll((short) point);

				cell.setCellStyle(setFontStyle(true));
			}
		}
	}

	private void checkParameters(int date, String brno, String fileCode, String fileItem, String fileName)
			throws LogicException {

		if (date == 0) {
			throw new LogicException("EC004", "(MakeExcel)日期(date)必須有值(MakeExcel)");
		}
		this.date = date;

		if ("".equals(brno)) {
			throw new LogicException("EC004", "(MakeExcel)單位(brno)必須有值(MakeExcel)");
		}
		this.brno = brno;

		if ("".equals(fileCode)) {
			throw new LogicException("EC004", "(MakeExcel)檔案編號(fileCode)必須有值(MakeExcel)");
		}
		if (haveChinese(fileCode)) {
			throw new LogicException("EC007", "(MakeExcel)檔案編號(fileCode)參數不可有全形字");
		}
		this.fileCode = fileCode;

		if ("".equals(fileItem)) {
			throw new LogicException("EC004", "(MakeExcel)檔案名稱(fileItem)必須有值(MakeExcel)");
		}
		this.fileItem = fileItem;

		if ("".equals(fileName)) {
			throw new LogicException("EC004", "(MakeExcel)輸出檔名(outfile)必須有值(MakeExcel)");
		}
		this.fileName = fileName;

	}

	/**
	 * 結束excel製檔<br>
	 * 
	 * @return long 檔案序號
	 * @throws LogicException LogicException
	 */
	public long close() throws LogicException {

		if (xls) {
			this.fileName += ".xls";
		} else {
			this.fileName += ".xlsx";
		}

		this.info("MakeExcel.close=" + this.fileName);
		TxFile tTxFile = new TxFile();

		tTxFile.setFileType(2);
		tTxFile.setFileFormat(1);
		tTxFile.setFileCode(this.fileCode);
		tTxFile.setFileItem(this.fileItem);
		tTxFile.setFileOutput(this.fileName);
		tTxFile.setBrNo(this.brno);
		tTxFile.setFileDate(this.date);
		tTxFile.setCreateEmpNo(this.titaVo.getTlrNo());
		try {
			ObjectMapper mapper = new ObjectMapper();
			tTxFile.setFileData(mapper.writeValueAsString(listMap));
		} catch (IOException e) {
			throw new LogicException("EC009", "資料格式");
		}

		// 寫Txfile時需寫回onlineDB,但交易用的titaVo應維持原指向的DB
		TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();

		tmpTitaVo.putParam(ContentName.dataBase, ContentName.onLine);

		try {
			tTxFile = sTxFileService.insert(tTxFile, tmpTitaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "EC002", "(MakeExcel)輸出檔(TxFile):" + e.getErrorMsg());
		}
		return tTxFile.getFileNo();

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

	/**
	 * 複製上一列整列格式
	 * 
	 * @param targetRow 複製到第幾列
	 * @param n         複製幾次
	 */
	private void copyLastRowCellStyle(int targetRow, int n) {

		n += targetRow;

		for (; targetRow <= n; targetRow++) {

			int sourceRow = targetRow - 1;

			this.info("sourceRow = " + sourceRow);

			Row tmpSourceRow = this.sheet.getRow(sourceRow);

			if (tmpSourceRow != null) {

				Row tmpTargetRow = this.sheet.getRow(targetRow);

				if (tmpTargetRow == null) {
					tmpTargetRow = this.sheet.createRow(targetRow);
				}

				for (Cell tmpSourceCell : tmpSourceRow) {

					int columnIndex = tmpSourceCell.getColumnIndex();

					CellStyle tmpSourceCellStyle = tmpSourceCell.getCellStyle();

					if (tmpSourceCellStyle != null) {
						short cellStyleIndex = tmpSourceCellStyle.getIndex();

						Cell tmpTargetColumn = tmpTargetRow.getCell(columnIndex);

						if (tmpTargetColumn == null) {
							tmpTargetColumn = tmpTargetRow.createCell(columnIndex);
						}
						this.info("targetRow = " + targetRow);
						this.info("tmpTargetColumn columnIndex = " + columnIndex);
						this.info("tmpSourceCellStyle cellStyleIndex = " + cellStyleIndex);
						tmpTargetColumn.setCellStyle(this.wb.getCellStyleAt(cellStyleIndex));
					}
				}
			}
		}
	}

	private void doNewSheet(String sheetName) {
		this.sheet = this.wb.createSheet(sheetName);

	}

	private void doOpen(String fileName) throws LogicException {
		if ("".equals(fileName)) {
			this.xls = false;

			this.wb = new XSSFWorkbook();
		} else {
			this.openFile(fileName, true);
			isDefaultExcel = true;
		}
		this.df = this.wb.createDataFormat();

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

	private String doSetSheet(Object sheet, String newSheetName) throws LogicException {
		String sheetname = "";
		try {
			if (sheet instanceof String) {
				this.sheet = this.wb.getSheet(sheet.toString());
				sheetname = sheet.toString();
			} else {
				this.sheet = this.wb.getSheetAt(Integer.valueOf(sheet.toString()) - 1);
				sheetname = this.sheet.getSheetName();
			}
			if (!"".equals(newSheetName)) {
				int sheetindex = this.wb.getSheetIndex(this.sheet);
				this.wb.setSheetName(sheetindex, newSheetName);
			}
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)指定 SHEET (" + sheet + ") 不存在");
		}

		if (this.sheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)指定 SHEET (" + sheet + ") 不存在");
		}

		return sheetname;
	}

	private void doSetWidth(int col, int width) {

		if (width == 0) {
			sheet.autoSizeColumn(col - 1);
		} else {
			this.sheet.setColumnWidth(col - 1, width * 256);
		}

	}

	@SuppressWarnings("unchecked")
	private void doToExcel(long fileno, String fileName) throws LogicException {
		this.info("MakeExcel.toExcel=" + fileno);

		TxFile tTxFile = sTxFileService.findById(fileno);

		if (tTxFile == null) {
			throw new LogicException(titaVo, "EC002", "(MakeExcel)輸出檔(TxFile)序號:" + fileno);
		}

		try {
			this.listMap = new ObjectMapper().readValue(tTxFile.getFileData(), ArrayList.class);
		} catch (IOException e) {
			throw new LogicException("EC009", "(MakeExcel)輸出檔(TxFile)序號:" + fileno + ",資料格式");
		}

		String outfile = outputFolder + tTxFile.getFileOutput();

		if (!"".equals(fileName)) {
			outfile = outputFolder + fileName;
		}

		// 先刪除舊檔
		File file = new File(outfile);

		if (file.exists() && file.isFile()) {
			try {
				Files.delete(file.toPath());
			} catch (IOException e) {
				this.error("MakeExcel Files.delete error =" + e.getMessage());
			}
		}

		for (HashMap<String, Object> map : listMap) {

			String type = map.get("t").toString();
			if ("0".equals(type)) {
				String excel = map.get("e").toString();
				this.doOpen(excel);
			} else if ("1".equals(type)) {
				String sheetname = map.get("s").toString();
				String newsheetname = map.get("n").toString();
				this.doSetSheet(sheetname, newsheetname);
			} else if ("2".equals(type)) {
				String sheetname = map.get("s").toString();
				this.doNewSheet(sheetname);
			} else if ("3".equals(type)) {
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
			} else if ("4".equals(type)) {
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
			} else if ("5".equals(type)) {
				int firstRow = Integer.parseInt(map.get("fr").toString());
				int lastRow = Integer.parseInt(map.get("lr").toString());
				int firstCol = Integer.parseInt(map.get("fc").toString());
				int lastCol = Integer.parseInt(map.get("lc").toString());
				this.sheet.addMergedRegion(new CellRangeAddress(firstRow - 1, lastRow - 1, firstCol - 1, lastCol - 1));
			} else if ("6".equals(type)) {
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
			} else if ("7".equals(type)) {
				needStyle = false;
			} else if ("8".equals(type)) {
				int shiftRowFrom = Integer.parseInt(map.get("srf").toString());
				int shiftCounts = Integer.parseInt(map.get("n").toString());

				int finalRow = this.sheet.getLastRowNum();

				this.info("shiftRowFrom - 1 = " + (shiftRowFrom - 1));

				this.info("finalRow = " + finalRow);

				this.sheet.shiftRows(shiftRowFrom - 1, finalRow, shiftCounts);

				copyLastRowCellStyle(shiftRowFrom, shiftCounts);

			} else if ("9".equals(type)) {
				int caculateRow = Integer.parseInt(map.get("r").toString());
				int caculateColumn = Integer.parseInt(map.get("c").toString());

				formulaCaculateToExcel(caculateRow, caculateColumn);
			}
		}

		int numCellStyles = this.wb.getNumCellStyles();
		this.info("MakeExcel numCellStyles = " + numCellStyles);

//		this.info("MakeExcel fontStyleMap = " + fontStyleMap.toString());

		// 建立輸出流
		try (FileOutputStream fos = new FileOutputStream(new File(outfile))) {
			this.wb.write(fos);
			this.wb.close();
		} catch (IOException e) {
			throw new LogicException(this.titaVo, "E0013", "(MakeExcel)輸出" + outfile + "錯誤");
		}

		this.info("MakeExcel finished.");

	}

	@Override
	public void exec() throws LogicException {
		// override this

	}

	/**
	 * 公式儲存格重新計算
	 * 
	 * @param caculateRow    第幾列
	 * @param caculateColumn 第幾欄
	 */
	public void formulaCaculate(int caculateRow, int caculateColumn) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", "9");
		map.put("r", caculateRow);
		map.put("c", caculateColumn);
		listMap.add(map);
	}

	/**
	 * 公式儲存格重新計算<BR>
	 * 實際寫入Excel時使用
	 * 
	 * @param caculateRow    第幾列
	 * @param caculateColumn 第幾欄
	 */
	private void formulaCaculateToExcel(int caculateRow, int caculateColumn) {

		Row pRow = this.sheet.getRow(caculateRow - 1);
		if (pRow != null) {
			Cell tmpCell = pRow.getCell(caculateColumn - 1);

			if (tmpCell.getCellType() == CellType.FORMULA) {
				this.wb.getCreationHelper().createFormulaEvaluator().evaluateFormulaCell(tmpCell);
			}
		}
	}

	public int getDate() {
		return date;
	}

	public String getFileCode() {
		return fileCode;
	}

	public String getFileItem() {
		return fileItem;
	}

	/**
	 * @return the listMap
	 */
	public List<HashMap<String, Object>> getListMap() {
		return listMap;
	}

	/**
	 * 讀取指定列/欄值<br>
	 * 
	 * @param row 列
	 * @param col 欄
	 * @return Object 欄位值
	 * @throws LogicException LogicException
	 */
	public Object getValue(int row, int col) throws LogicException {
		if (this.sheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel) getValue sheet is null");
		}

		Row prow = this.sheet.getRow(row - 1);

		if (prow == null) {
			return "";
		} else {
			Cell tmpCell = prow.getCell(col - 1);
			if (tmpCell.getCellType() == CellType.NUMERIC) {
				return tmpCell.getNumericCellValue();
			} else if (tmpCell.getCellType() == CellType.STRING) {
				return tmpCell.getStringCellValue();
			} else if (tmpCell.getCellType() == CellType.BOOLEAN) {
				return tmpCell.getBooleanCellValue();
			} else if (tmpCell.getCellType() == CellType.FORMULA) {
				if (tmpCell.getCachedFormulaResultType().toString().equals("STRING")) {
					return tmpCell.getStringCellValue();
				} else if (tmpCell.getCachedFormulaResultType().toString().equals("NUMERIC")) {
					return tmpCell.getNumericCellValue();
				} else {
					return tmpCell.getCachedFormulaResultType().toString();
				}
			} else {
				return tmpCell.getStringCellValue();
			}
		}

	}

	private boolean haveChinese(String string) {
		for (int i = 0; i < string.length(); i++) {
			String c = string.substring(i, i + 1);
			if (c.matches("[\\u0391-\\uFFE5]+")) {
				return true;
			}
		}
		return false;
	}

	public boolean isNeedStyle() {
		return needStyle;
	}

	private void modifyOuputFontStyleVo() {

		// 讀取原CellStyle設定寫進 outputFontStyleVo
		CellStyle originalCellStyle = cell.getCellStyle();

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

//		this.info("originalDataFormat = " + originalDataFormat);

		if (!originalDataFormat.equals("0") && !originalDataFormat.equals("General")) {
			outputFontStyleVo.setFormat(originalDataFormat);
		}

		int fontIndex = originalCellStyle.getFontIndexAsInt();

		Font tmpFont = this.wb.getFontAt(fontIndex);

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

	/**
	 * 新增Sheet<br>
	 * 
	 * @param sheetName 新增sheet名稱
	 * @throws LogicException LogicException
	 */
	public void newSheet(String sheetName) throws LogicException {

//		this.sheet = this.wb.createSheet(sheetName);
		this.doNewSheet(sheetName);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 2);
		map.put("s", sheetName);
		listMap.add(map);
	}

	/**
	 * 開啟excel製檔<br>
	 * 
	 * @param titaVo   titaVo
	 * @param date     日期
	 * @param brno     單位
	 * @param fileCode 檔案編號
	 * @param fileItem 檔案說明
	 * @param fileName 輸出檔案名稱(不含副檔名,預設檔案編號為.xlsx)
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, int date, String brno, String fileCode, String fileItem, String fileName)
			throws LogicException {

		// 未指定sheetnanme時,預設以檔案編號為sheetnanme
		this.open(titaVo, date, brno, fileCode, fileItem, fileName, fileCode);
	}

	/**
	 * 開啟excel製檔<br>
	 * 
	 * @param titaVo    titaVo
	 * @param date      日期
	 * @param brno      單位
	 * @param fileCode  檔案編號
	 * @param fileItem  檔案說明
	 * @param fileName  輸出檔案名稱(不含副檔名,預設為.xlsx)
	 * @param sheetName 新建Sheet名稱
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, int date, String brno, String fileCode, String fileItem, String fileName,
			String sheetName) throws LogicException {

		this.titaVo = titaVo;
		this.checkParameters(date, brno, fileCode, fileItem, fileName);

		this.doOpen("");

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 0);
		map.put("e", "");
		listMap.add(map);

		this.newSheet(sheetName);

	}

	/**
	 * 
	 * @param titaVo       titaVo
	 * @param date         日期
	 * @param brno         單位
	 * @param fileCode     檔案編號
	 * @param fileItem     檔案說明
	 * @param fileName     輸出檔案名稱(不含副檔名,預設為.xlsx)
	 * @param defaultExcel 預設excel底稿檔
	 * @param defaultSheet 預設sheet,可指定 sheet index or sheet name
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, int date, String brno, String fileCode, String fileItem, String fileName,
			String defaultExcel, Object defaultSheet) throws LogicException {

		this.titaVo = titaVo;
		this.checkParameters(date, brno, fileCode, fileItem, fileName);

		this.doOpen(defaultExcel);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 0);
		map.put("e", defaultExcel);
		listMap.add(map);

		this.setSheet(defaultSheet);

	}

	/**
	 * 開啟excel製檔<br>
	 * 
	 * @param titaVo       titaVo
	 * @param date         日期
	 * @param brno         單位
	 * @param fileCode     檔案編號
	 * @param fileItem     檔案說明
	 * @param fileName     輸出檔案名稱 (不含副檔名,預設為.xlsx)
	 * @param defaultExcel 預設excel底稿檔 (需含副檔名)
	 * @param defaultSheet 預設sheet,可指定 sheet index or sheet name
	 * @param newSheetName 修改sheet名稱
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, int date, String brno, String fileCode, String fileItem, String fileName,
			String defaultExcel, Object defaultSheet, String newSheetName) throws LogicException {

		this.titaVo = titaVo;
		this.checkParameters(date, brno, fileCode, fileItem, fileName);

		this.doOpen(defaultExcel);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 0);
		map.put("e", defaultExcel);
		listMap.add(map);

		this.setSheet(defaultSheet, newSheetName);

	}

	/**
	 * 
	 * @param fileName  CSV檔名
	 * @param splitchar 欄位分隔符號
	 * @throws LogicException LogicException
	 */
	public void openCsv(String fileName, String splitchar) throws LogicException {
		this.info("MakeExcel.openCsv=" + fileName);

		this.listMap = new ArrayList<HashMap<String, Object>>();

		try (BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
			String line = null;
			while ((line = csvReader.readLine()) != null) {
				// 2020-11-11 Wei關閉
//				this.info("MakeExcel.openCsv.readline=" + line);
				String[] item = line.split(splitchar);
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < item.length; i++) {
					map.put("f" + (i + 1), item[i].trim());
				}
				this.listMap.add(map);
			}
		} catch (FileNotFoundException e) {
			throw new LogicException(titaVo, "EC001", "(MakeExcel)檔案:" + fileName);
		} catch (IOException e) {
			throw new LogicException(titaVo, "EC009", "(MakeExcel)");
		}

	}

	/**
	 * 開啟EXCEL
	 * 
	 * @param fileName  Excel檔名
	 * @param sheetname 可指定 sheet index or sheet name
	 * @throws LogicException LogicException
	 */
	public void openExcel(String fileName, Object sheetname) throws LogicException {
		this.openFile(fileName, false);

		this.doSetSheet(sheetname, "");
	}

	private void openFile(String fileName, boolean excelfolder) throws LogicException {
		// 讀取既有的
//		System.out.println("file type = " + fileName.substring(l-4, l));

		String fna = "";

		if (excelfolder) {
			fna = workingExcelFolder + fileName;
		} else {
			fna = fileName;
		}

		int l = fileName.length();

		try (InputStream is = new FileInputStream(fna)) {
			if (".xls".equals(fileName.substring(l - 4, l))) {
				this.xls = true;
				this.wb = new HSSFWorkbook(is);
			} else if (".xlsx".equals(fileName.substring(l - 5, l))) {
				this.xls = false;
				this.wb = new XSSFWorkbook(is);
			} else {
				throw new LogicException(titaVo, "E0013", "(MakeExcel)" + fna + "，非預設EXCEL檔案格式");
			}
		} catch (IOException e) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)" + fna + "檔案不存在");
		}
	}

	/**
	 * 畫表格框線(指定範圍)<br>
	 * ex:畫A1~B3:setAddRengionBorder(A,1,B,3,1)
	 * 
	 * @param firstCell 起始欄
	 * @param firstRow  起始列
	 * @param lastCell  結束欄
	 * @param lastRow   結束列
	 * @param point     框線粗細
	 */
	public void setAddRengionBorder(String firstCell, int firstRow, String lastCell, int lastRow, int point) {
//		String[] range=new String[5];
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", "6");
		map.put("firstcell", firstCell);
		map.put("firstrow", firstRow);
		map.put("lastcell", lastCell);
		map.put("lastrow", lastRow);
		map.put("point", point);
		listMap.add(map);
	}

	/**
	 * 設定表格底(背景)色
	 * 
	 * @param bgColor 顏色(英文字，大小寫皆可) <br>
	 *                直接打顏色英文字或使用IndexedColors呼叫顏色代號
	 */

	public void setBackGroundColor(String bgColor) {

		inputFontStyleVo.setBgColor(bgColor);

	}

	/**
	 * 設定框線 粗細
	 * 
	 * @param borderAll 粗細<br>
	 *                  1=一般粗點 <br>
	 */
	public void setBorder(int borderAll) {

		inputFontStyleVo.setBorderAll((short) borderAll);

	}

	/**
	 * 設定文字 顏色
	 * 
	 * @param color 顏色(英文字，大小寫皆可) <br>
	 *              直接打顏色英文字或使用IndexedColors呼叫顏色代號
	 */

	public void setColor(String color) {

		this.inputFontStyleVo.setColor(color);

	}

	public void setDate(int date) {
		this.date = date;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public void setFileItem(String fileItem) {
		this.fileItem = fileItem;
	}

	/**
	 * @param readStyle 是否讀取格式
	 * @throws CloneNotSupportedException
	 */
	private CellStyle setFontStyle(boolean readStyle) {

		// 預計要回傳的 CellStyle
		CellStyle resultCellStyle = null;

		if (isDefaultExcel) {

			int originalCellStyleIndex = cell.getCellStyle().getIndex();

			if (originalCellStyleIndex == 0) {
				// 原CellStyleIndex為0時,由outputFontStyleVo決定樣式
				readStyle = false;
			} else {
				readStyle = true;
				resultCellStyle = this.wb.getCellStyleAt(originalCellStyleIndex);
			}

//			this.info("讀底稿 originalCellStyleIndex = " + originalCellStyleIndex);
//			this.info("讀底稿 resultCellStyle.getIndex = " + resultCellStyle.getIndex());
		}

		// 若需讀原格式設定,先讀取原格式設定寫進outputFontStyleVo
		if (readStyle) {
			modifyOuputFontStyleVo();
		}

		// CellStyle 的 Index
		int indexOfCellStyle;

		// 目前CellStyle數量
		int numOfCellStyle = this.wb.getNumCellStyles();

		// xlsx CellStyle 數量限制為 64000
		int limitOfCellStyle = 64000;

		if (xls) {
			// xls CellStyle 數量限制為 4000
			limitOfCellStyle = 4000;
		}

		// 若此ExcelFontStyleVo 已存在於 Map
		// 取與其對應的 CellStyle 的 Index
		// 並 回傳該CellStyle
		for (Iterator<Entry<ExcelFontStyleVo, Integer>> it = fontStyleMap.entrySet().iterator(); it.hasNext();) {
			Entry<ExcelFontStyleVo, Integer> tmpEntry = it.next();

//			this.info("tmpEntry.getKey() = " + tmpEntry.getKey().toString());
//			this.info("outputFontStyleVo = " + outputFontStyleVo.toString());

			if (tmpEntry.getKey().equals(outputFontStyleVo)) {
//				this.info("equals.");

				indexOfCellStyle = tmpEntry.getValue();

				resultCellStyle = this.wb.getCellStyleAt(indexOfCellStyle);

//				this.info("indexOfCellStyle = " + indexOfCellStyle);
				return resultCellStyle;
			}
		}
//		this.info("no equals.");

		// 若 ExcelFontStyleVo 不存在於 Map
		// 且 目前CellStyle數量 <= CellStyle數量限制
		// 且 不是讀取底稿
		// 則 創立新的CellStyle , 並將該 CellStyle的Index 記錄在 Map
		if (numOfCellStyle < limitOfCellStyle && !isDefaultExcel) {

			resultCellStyle = this.wb.createCellStyle();

			indexOfCellStyle = resultCellStyle.getIndex();

			try {
				fontStyleMap.put(outputFontStyleVo.clone(), indexOfCellStyle);
			} catch (CloneNotSupportedException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("MakeExcel clone error = " + e.getMessage());
			}

//			this.info("fontStyleMap put outputFontStyleVo = " + outputFontStyleVo.toString());
//			this.info("fontStyleMap put outputFontStyleVo hashCode = " + outputFontStyleVo.hashCode());
//			this.info("fontStyleMap put indexOfCellStyle = " + indexOfCellStyle);

			// 若 ExcelFontStyleVo 不存在於 Map
			// 且 目前CellStyle數量 <= CellStyle數量限制
			// 且 是讀取底稿
			// 則 使用原儲存格的CellStyle , 並將該 CellStyle的Index 記錄在 Map
		} else if (numOfCellStyle < limitOfCellStyle && isDefaultExcel) {

			// 若原儲存格的CellStyle為空,建立新CellStyle
			if (resultCellStyle == null) {

				resultCellStyle = this.wb.createCellStyle();
			}

			indexOfCellStyle = resultCellStyle.getIndex();

//			this.info("fontStyleMap put value = " + indexOfCellStyle);

			try {
				fontStyleMap.put(outputFontStyleVo.clone(), indexOfCellStyle);
			} catch (CloneNotSupportedException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("MakeExcel clone error = " + e.getMessage());
			}

			// 否則,寫this.error,回傳第一個CellStyle
		} else {

			if (isFirstTimeToStyleLimit) {
				this.error("setFontStyle error : CellStyle已超過" + limitOfCellStyle + ",不做Style設定,目前CellStyle數量為"
						+ numOfCellStyle);
				isFirstTimeToStyleLimit = false;
			}
			return this.wb.getCellStyleAt(0);
		}

		// 此處新增數量會 <= limitOfCellStyle
		Font font = this.wb.createFont();

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
			resultCellStyle.setDataFormat(this.df.getFormat(outputFontStyleVo.getFormat()));
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

	/**
	 * package com.st1.itx.util.common.data
	 * 
	 * @param fontStyleVo the fontStyleVo to set
	 */
	public void setFontStyleVo(ExcelFontStyleVo fontStyleVo) {
		try {
			this.inputFontStyleVo = fontStyleVo.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 設定文字 字型
	 * 
	 * @param fontType 字型<br>
	 *                 1=標楷體<br>
	 *                 2=微軟正黑體<br>
	 *                 3=Times New Roman<br>
	 *                 4=Arial<br>
	 *                 5=新細明體
	 */
	public void setFontType(int fontType) {

		this.inputFontStyleVo.setFont((short) fontType);

	}

	/**
	 * 設定列高 (若要使用0:自動調整,請在全部值存入後再設定欄寬)
	 * 
	 * @param row    列
	 * @param height 高度字元數,0:自動調整
	 * @throws LogicException LogicException
	 */
	public void setHeight(int row, int height) throws LogicException {

		if (row <= 0 || height < 0) {
			return;
		}

		doSetHeight(row, height);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 4);
		map.put("r", row);
		map.put("h", height);

		listMap.add(map);
	}

	/**
	 * 設定文字 斜體 粗體 底線
	 * 
	 * @param ibu 斜體 粗體 底線 <br>
	 *            I= 斜體 <br>
	 *            B=粗體 <br>
	 *            U=底線<br>
	 */
	public void setIBU(String ibu) {

		for (String s : ibu.split("")) {
			switch (s.toUpperCase()) {
			case "I":
				inputFontStyleVo.setItalic(true);
				break;
			case "B":
				inputFontStyleVo.setBold(true);
				break;
			case "U":
				inputFontStyleVo.setUnderline(true);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 欄位合併
	 * 
	 * @param frow 起始列
	 * @param lrow 結束列
	 * @param fcol 起始欄
	 * @param lcol 結束欄
	 */
	public void setMergedRegion(int frow, int lrow, int fcol, int lcol) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 5);
		map.put("fr", frow);
		map.put("lr", lrow);
		map.put("fc", fcol);
		map.put("lc", lcol);

		listMap.add(map);
	}

	/**
	 * 指定列/欄 設值並合併儲存格<br>
	 * 
	 * @param row  起始列
	 * @param lrow 結束列
	 * @param col  起始欄
	 * @param lcol 結束欄
	 * @param val  設定值
	 * @throws LogicException LogicException
	 */
	public void setMergedRegionValue(int row, int lrow, int col, int lcol, Object val) throws LogicException {

		setValueToMap(row, lrow, col, lcol, val);
	}

	/**
	 * 指定列/欄 設值並合併儲存格<br>
	 * 
	 * @param row           起始列
	 * @param lrow          結束列
	 * @param col           起始欄
	 * @param lcol          結束欄
	 * @param val           設定值
	 * @param formatOrAlign <br>
	 *                      金額格式、對齊方式擇一輸入<br>
	 *                      金額格式範例<br>
	 *                      "#,##0" 加上撇節，顯示到個數位<br>
	 *                      "#,##0.00" 加上撇節，顯示到小數點後第二位<br>
	 *                      "#0.0000"<br>
	 *                      ...依此類推<br>
	 *                      <br>
	 *                      對齊方式<br>
	 *                      L 靠左對齊<br>
	 *                      C 至中對齊<br>
	 *                      R 靠右對齊<br>
	 * @throws LogicException LogicException
	 */
	public void setMergedRegionValue(int row, int lrow, int col, int lcol, Object val, String formatOrAlign)
			throws LogicException {

		switch (formatOrAlign) {
		case "L":
		case "C":
		case "R":
			inputFontStyleVo.setAlign(formatOrAlign);
			break;
		default:
			inputFontStyleVo.setFormat(formatOrAlign);
			break;
		}

		setValueToMap(row, lrow, col, lcol, val);
	}

	/**
	 * 指定列/欄設值<br>
	 * 
	 * @param row    起始列
	 * @param lrow   結束列
	 * @param col    起始欄
	 * @param lcol   結束欄
	 * @param val    設定值
	 * @param format 金額格式<br>
	 *               "#,##0" or "#,##0.00" or "#0.0000"
	 * @param align  L 靠左對齊<br>
	 *               C 至中對齊<br>
	 *               R 靠右對齊<br>
	 * @throws LogicException LogicException
	 */
	public void setMergedRegionValue(int row, int lrow, int col, int lcol, Object val, String format, String align)
			throws LogicException {

		if (format != null && !format.isEmpty()) {
			inputFontStyleVo.setFormat(format);
		}

		if (align != null && !align.isEmpty()) {
			inputFontStyleVo.setAlign(align);
		}

		setValueToMap(row, lrow, col, lcol, val);
	}

	public void setNeedStyle(boolean needStyle) {
		this.needStyle = needStyle;

		if (!this.needStyle) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("t", 7);

			listMap.add(map);
		}
	}

	/**
	 * 指定Sheet<br>
	 * 
	 * @param sheet 可指定 sheet index or sheet name
	 * @throws LogicException LogicException
	 */
	public void setSheet(Object sheet) throws LogicException {

		this.setSheetToMap(sheet, "");

	}

	/**
	 * 指定Sheet<br>
	 * 
	 * @param sheet        可指定 sheet index or sheet name
	 * @param newSheetName 修改指定sheet名稱
	 * @throws LogicException LogicException
	 */
	public void setSheet(Object sheet, String newSheetName) throws LogicException {

		this.setSheetToMap(sheet, newSheetName);
	}

	private void setSheetToMap(Object sheet, String newSheetName) throws LogicException {

		String sheetname = this.doSetSheet(sheet, newSheetName);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 1);
		map.put("s", sheetname);
		map.put("n", newSheetName);
		listMap.add(map);

	}

	/**
	 * 插入列,從該列(包含該列本身)開始有值的列數都會向下搬移
	 * 
	 * @param shiftRowFrom 從第幾列開始向下搬移
	 * @param shiftCounts  搬移幾列
	 */
	public void setShiftRow(int shiftRowFrom, int shiftCounts) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 8);
		map.put("srf", shiftRowFrom);
		map.put("n", shiftCounts);
		listMap.add(map);
	}

	/**
	 * 設定文字 大小
	 * 
	 * @param size 字體大小
	 */
	public void setSize(int size) {

		this.inputFontStyleVo.setSize((short) size);

	}

	/**
	 * 指定列/欄設值<br>
	 * 
	 * @param row 列
	 * @param col 欄
	 * @param val 設定值
	 * @throws LogicException LogicException
	 */

	public void setValue(int row, int col, Object val) throws LogicException {

		setValueToMap(row, 0, col, 0, val);
	}

	/**
	 * 指定列/欄設值<br>
	 * 
	 * @param row            列
	 * @param col            欄
	 * @param val            設定值
	 * @param tmpFontStyleVo 格式設定
	 * @throws LogicException LogicException
	 */

	public void setValue(int row, int col, Object val, ExcelFontStyleVo tmpFontStyleVo) throws LogicException {

		this.setFontStyleVo(tmpFontStyleVo);

		setValueToMap(row, 0, col, 0, val);
	}

	/**
	 * 指定列/欄設值<br>
	 * 
	 * @param row           列
	 * @param col           欄
	 * @param val           設定值
	 * @param formatOrAlign <br>
	 *                      金額格式、對齊方式擇一輸入<br>
	 *                      金額格式範例<br>
	 *                      "#,##0" 加上撇節，顯示到個數位<br>
	 *                      "#,##0.00" 加上撇節，顯示到小數點後第二位<br>
	 *                      "#0.0000"<br>
	 *                      ...依此類推<br>
	 *                      <br>
	 *                      對齊方式<br>
	 *                      L 靠左對齊<br>
	 *                      C 至中對齊<br>
	 *                      R 靠右對齊<br>
	 * @throws LogicException LogicException
	 */
	public void setValue(int row, int col, Object val, String formatOrAlign) throws LogicException {

		switch (formatOrAlign) {
		case "L":
		case "C":
		case "R":
			inputFontStyleVo.setAlign(formatOrAlign);
			break;
		default:
			inputFontStyleVo.setFormat(formatOrAlign);
			break;
		}

		setValueToMap(row, 0, col, 0, val);
	}

	/**
	 * 指定列/欄設值<br>
	 * 
	 * @param row            列
	 * @param col            欄
	 * @param val            設定值
	 * @param formatOrAlign  <br>
	 *                       金額格式、對齊方式擇一輸入<br>
	 *                       金額格式範例<br>
	 *                       "#,##0" 加上撇節，顯示到個數位<br>
	 *                       "#,##0.00" 加上撇節，顯示到小數點後第二位<br>
	 *                       "#0.0000"<br>
	 *                       ...依此類推<br>
	 *                       <br>
	 *                       對齊方式<br>
	 *                       L 靠左對齊<br>
	 *                       C 至中對齊<br>
	 *                       R 靠右對齊<br>
	 * @param tmpFontStyleVo 格式設定
	 * @throws LogicException LogicException
	 */
	public void setValue(int row, int col, Object val, String formatOrAlign, ExcelFontStyleVo tmpFontStyleVo)
			throws LogicException {

		this.setFontStyleVo(tmpFontStyleVo);

		switch (formatOrAlign) {
		case "L":
		case "C":
		case "R":
			inputFontStyleVo.setAlign(formatOrAlign);
			break;
		default:
			inputFontStyleVo.setFormat(formatOrAlign);
			break;
		}

		setValueToMap(row, 0, col, 0, val);
	}

	/**
	 * 指定列/欄設值<br>
	 * 
	 * @param row    列
	 * @param col    欄
	 * @param val    設定值
	 * @param format 金額格式<br>
	 *               "#,##0" or "#,##0.00" or "#0.0000"
	 * @param align  L 靠左對齊<br>
	 *               C 至中對齊<br>
	 *               R 靠右對齊<br>
	 * @throws LogicException LogicException
	 */
	public void setValue(int row, int col, Object val, String format, String align) throws LogicException {

		if (format != null && !format.isEmpty()) {
			inputFontStyleVo.setFormat(format);
		}

		if (align != null && !align.isEmpty()) {
			inputFontStyleVo.setAlign(align);
		}

		setValueToMap(row, 0, col, 0, val);
	}

	/**
	 * 指定列/欄設值<br>
	 * 
	 * @param row            列
	 * @param col            欄
	 * @param val            設定值
	 * @param format         金額格式<br>
	 *                       "#,##0" or "#,##0.00" or "#0.0000"
	 * @param align          L 靠左對齊<br>
	 *                       C 至中對齊<br>
	 *                       R 靠右對齊<br>
	 * @param tmpFontStyleVo 格式設定
	 * @throws LogicException LogicException
	 */
	public void setValue(int row, int col, Object val, String format, String align, ExcelFontStyleVo tmpFontStyleVo)
			throws LogicException {

		this.setFontStyleVo(tmpFontStyleVo);

		if (format != null && !format.isEmpty()) {
			inputFontStyleVo.setFormat(format);
		}

		if (align != null && !align.isEmpty()) {
			inputFontStyleVo.setAlign(align);
		}

		setValueToMap(row, 0, col, 0, val);
	}

	private void setValueToExcel(int row, int col, int lastRow, int lastCol, Object val) throws LogicException {

		if (this.sheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)setValue sheet is null");
		}

		Row prow = this.sheet.getRow(row - 1);

		if (prow == null) {
			prow = this.sheet.createRow(row - 1);
		}
		// -------------------
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

		// -------------------
		if (lastRow > 0 && lastCol > 0) {
			CellRangeAddress cra = new CellRangeAddress(row - 1, lastRow - 1, col - 1, lastCol - 1);
			this.sheet.addMergedRegion(cra);
		}

//		boolean isNumber = true;
//
//		try {
//			new BigDecimal(val.toString());
//		} catch (NumberFormatException e) {
//			isNumber = false;
//		}

		if (val instanceof String && !(val instanceof BigDecimal)) {

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

//		this.info("val = " + val.toString());
//		this.info("needStyle = " + needStyle);
//		this.info("cellHasStyle = " + cellHasStyle);
		if (needStyle && cellHasStyle) {
			cell.setCellStyle(setFontStyle(false));
		}

		// 取得當前表格內文字的高度，以文字高度去*1.5倍=適應文字的表格
		// 預設表格高度16 預設文字高度(大小)12 表格/文字=1.33 取整1.5
		float tempRH = (float) (this.wb.getFontAt(cell.getCellStyle().getFontIndexAsInt()).getFontHeightInPoints()
				* fontWrap * 1.5);
		// 是否同一列
		if (this.rowNum != nowRow.getRowNum()) {
			this.rowNum = nowRow.getRowNum();
			tempRowHeight = tempRH;
			nowRow.setHeightInPoints(tempRowHeight);
		} else {
			if (tempRowHeight < tempRH) {
				tempRowHeight = tempRH;
				nowRow.setHeightInPoints(tempRowHeight);
			}
		}
	}

	private void setValueToMap(int row, int lrow, int col, int lcol, Object val) {

		HashMap<String, Object> map = new HashMap<String, Object>();

		if (inputFontStyleVo.getSize() != 0) {
			map.put("size", inputFontStyleVo.getSize());
		}
		if (!inputFontStyleVo.getColor().isEmpty()) {
			map.put("color", inputFontStyleVo.getColor());
		}
		if (inputFontStyleVo.getFont() != 0) {
			map.put("fontType", inputFontStyleVo.getFont());
		}
		if (inputFontStyleVo.isItalic()) {
			map.put("I", inputFontStyleVo.isItalic());
		}
		if (inputFontStyleVo.isBold()) {
			map.put("B", inputFontStyleVo.isBold());
		}
		if (inputFontStyleVo.isUnderline()) {
			map.put("U", inputFontStyleVo.isUnderline());
		}
		if (inputFontStyleVo.getBorderAll() != 0) {
			map.put("borderAll", inputFontStyleVo.getBorderAll());
		}
		if (!inputFontStyleVo.getBgColor().isEmpty()) {
			map.put("bgColor", inputFontStyleVo.getBgColor());
		}
		if (!inputFontStyleVo.getFormat().isEmpty()) {
			map.put("f", inputFontStyleVo.getFormat());
		}
		if (!inputFontStyleVo.getAlign().isEmpty()) {
			map.put("align", inputFontStyleVo.getAlign());
		}

		map.put("t", 3);
		map.put("r", row);

		if (lrow > 0 && lcol > 0) {
			map.put("lr", lrow);
			map.put("lc", lcol);
		}

		map.put("c", col);

		if (val != null) {
			map.put("v", val);
		}

		listMap.add(map);

		// 加入list後，清除樣式
		inputFontStyleVo = inputFontStyleVo.init();
	}

	/**
	 * 設定欄位寬度 (若要使用0:自動調整,請在全部值存入後再設定欄寬)
	 * 
	 * @param col   欄
	 * @param width 寬度字元數, 0表系統依欄位內容值自動調整,否依指定字元數設定
	 * @throws LogicException LogicException
	 */
	public void setWidth(int col, int width) throws LogicException {

		if (col <= 0 || width < 0) {
			return;
		}

		doSetWidth(col, width);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 4);
		map.put("c", col);
		map.put("w", width);

		listMap.add(map);
	}

	/**
	 * 將指定檔案序號產製EXCEL<br>
	 * 
	 * @param fileno 檔案序號
	 * @throws LogicException LogicException
	 */
	public void toExcel(long fileno) throws LogicException {
		doToExcel(fileno, "");
	}

	/**
	 * 將指定檔案序號產製EXCEL<br>
	 * 
	 * @param fileno   檔案序號
	 * @param fileName 指定輸出檔名
	 * @throws LogicException LogicException
	 */
	public void toExcel(long fileno, String fileName) throws LogicException {
		doToExcel(fileno, fileName);
	}
}
