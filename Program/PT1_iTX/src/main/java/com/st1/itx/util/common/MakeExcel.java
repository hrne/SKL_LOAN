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
import com.st1.itx.util.filter.SafeClose;

/**
 * 
 * ----------------------- MakeExcel ??????EXCEL?????????????????? ------------------*
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

	// ????????????
	List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

	private boolean needStyle = true;

	/* excel???????????? */
	@Value("${iTXOutFolder}")
	private String outputFolder = "";

	private ExcelFontStyleVo outputFontStyleVo = new ExcelFontStyleVo();

	private int rowNum = -1;

	public Sheet sheet = null;

	/* DB???????????? */
	@Autowired
	TxFileService sTxFileService;

	private float tempRowHeight = 0;

	private Workbook wb = null;

	/* excel???????????? */
	@Value("${iTXExcelFolder}")
	private String workingExcelFolder = "";

	private boolean xls = false;

	/**
	 * ???????????????(????????????)<br>
	 * ex:???A1~B3:addRengionBorder(A,1,B,3,1)
	 * 
	 * @param firstCell ?????????
	 * @param firstRow  ?????????
	 * @param lastCell  ?????????
	 * @param lastRow   ?????????
	 * @param point     ????????????
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
			throw new LogicException("EC004", "(MakeExcel)??????(date)????????????(MakeExcel)");
		}
		this.date = date;

		if ("".equals(brno)) {
			throw new LogicException("EC004", "(MakeExcel)??????(brno)????????????(MakeExcel)");
		}
		this.brno = brno;

		if ("".equals(fileCode)) {
			throw new LogicException("EC004", "(MakeExcel)????????????(fileCode)????????????(MakeExcel)");
		}
		if (haveChinese(fileCode)) {
			throw new LogicException("EC007", "(MakeExcel)????????????(fileCode)????????????????????????");
		}
		this.fileCode = fileCode;

		if ("".equals(fileItem)) {
			throw new LogicException("EC004", "(MakeExcel)????????????(fileItem)????????????(MakeExcel)");
		}
		this.fileItem = fileItem;

		if ("".equals(fileName)) {
			throw new LogicException("EC004", "(MakeExcel)????????????(outfile)????????????(MakeExcel)");
		}
		this.fileName = fileName;

	}

	/**
	 * ??????excel??????<br>
	 * 
	 * @return long ????????????
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

		tTxFile.setFileType(2); // ??????2:EXCEL
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
			throw new LogicException("EC009", "????????????");
		}

		// ???Txfile????????????onlineDB,???????????????titaVo?????????????????????DB
		TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();

		tmpTitaVo.putParam(ContentName.dataBase, ContentName.onLine);

		try {
			tTxFile = sTxFileService.insert(tTxFile, tmpTitaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "EC002", "(MakeExcel)?????????(TxFile):" + e.getErrorMsg());
		}
		return tTxFile.getFileNo();

	}

	/**
	 * ????????????(??????????????????)
	 * 
	 * @param column ??????????????????(??????A~ZZ??????)
	 */
	private int columnCal(String column) {

		int col = 0;
		int columnLength = column.length();

		if (columnLength > 0 && columnLength <= 2) {

			// ?????????
			column = column.toUpperCase();

			char[] tokens = column.toCharArray();

			for (char token : tokens) {

				// ??????A????????????65
				int index = Integer.valueOf(token) - 64;

				if (columnLength == 2) {
					col = index * 26;
				} else {
					col = col + index;
				}
				columnLength--;
			}
			if (columnLength > 0) {
				this.error("MakeExcel columnCal error ???????????? " + column + " ???????????????(??????A~Z??????).");
			}
		} else {
			if (columnLength > 2) {
				this.error("MakeExcel columnCal error ????????????????????????(???????????????)");
			} else {
				this.error("MakeExcel columnCal error ?????????????????????0");
			}
		}
		return col;
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param targetRow ??????????????????
	 * @param n         ????????????
	 */
	private void copyLastRowCellStyle(int targetRow, int n) {

		n += targetRow;

		for (; targetRow < n; targetRow++) {

			int sourceRow = targetRow - 1;

//			this.info("sourceRow = " + sourceRow);

			Row tmpSourceRow = this.sheet.getRow(sourceRow - 1);

			if (tmpSourceRow != null) {

				Row tmpTargetRow = this.sheet.getRow(targetRow - 1);

				if (tmpTargetRow == null) {
					tmpTargetRow = this.sheet.createRow(targetRow - 1);
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
//						this.info("targetRow = " + targetRow);
//						this.info("tmpTargetColumn columnIndex = " + columnIndex);
//						this.info("tmpSourceCellStyle cellStyleIndex = " + cellStyleIndex);
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
			throw new LogicException(titaVo, "E0013", "(MakeExcel)?????? SHEET (" + sheet + ") ?????????");
		}

		if (this.sheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)?????? SHEET (" + sheet + ") ?????????");
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
			throw new LogicException(titaVo, "EC002", "(MakeExcel)?????????(TxFile)??????:" + fileno);
		}

		try {
			this.listMap = new ObjectMapper().readValue(tTxFile.getFileData(), ArrayList.class);
		} catch (IOException e) {
			throw new LogicException("EC009", "(MakeExcel)?????????(TxFile)??????:" + fileno + ",????????????");
		}

		String outfile = outputFolder + tTxFile.getFileOutput();

		if (!"".equals(fileName)) {
			outfile = outputFolder + fileName;
		}

		// ???????????????
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
						// ???
						tmpRow = Integer.valueOf(entry.getValue().toString());
						break;
					case "c":
						// ???
						col = Integer.valueOf(entry.getValue().toString());
						break;
					case "lr":
						// ???
						lastRow = Integer.valueOf(entry.getValue().toString());
						break;
					case "lc":
						// ???
						lastCol = Integer.valueOf(entry.getValue().toString());
						break;
					case "v":
						// ????????????
						val = entry.getValue();
						break;
					case "f":
						// ????????????
						outputFontStyleVo.setFormat(entry.getValue().toString());
						break;
					case "size":
						// ????????????
						outputFontStyleVo.setSize(Short.valueOf(entry.getValue().toString()));
						break;
					case "fontType":
						// ??????
						outputFontStyleVo.setFont(Short.valueOf(entry.getValue().toString()));
						break;
					case "color":
						// ????????????
						outputFontStyleVo.setColor(entry.getValue().toString());
						break;
					case "I":
						// ??????
						outputFontStyleVo.setItalic((boolean) entry.getValue());
						break;
					case "B":
						// ??????
						outputFontStyleVo.setBold((boolean) entry.getValue());
						break;
					case "U":
						// ??????
						outputFontStyleVo.setUnderline((boolean) entry.getValue());
						break;
					case "borderAll":
						// ????????????
						outputFontStyleVo.setBorderAll(Short.valueOf(entry.getValue().toString()));
						break;
					case "align":
						// ????????????
						outputFontStyleVo.setAlign(entry.getValue().toString());
						break;
					case "bgColor":
						// ????????????
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
				// ?????????
				String startCol = map.get("firstcell").toString();
				// ?????????
				int startRow = Integer.parseInt(map.get("firstrow").toString());
				// ?????????
				String endCol = map.get("lastcell").toString();
				// ?????????
				int endRow = Integer.parseInt(map.get("lastrow").toString());
				// ?????? ??????
				int point = Integer.parseInt(map.get("point").toString());
				// ???????????????
				addRengionBorder(startCol, startRow, endCol, endRow, point);
			} else if ("7".equals(type)) {
				needStyle = false;
			} else if ("8".equals(type)) {
				int shiftRowFrom = Integer.parseInt(map.get("srf").toString());
				int shiftCounts = Integer.parseInt(map.get("n").toString());

				int finalRow = this.sheet.getLastRowNum();

//				this.info("shiftRowFrom - 1 = " + (shiftRowFrom - 1));

//				this.info("finalRow = " + finalRow);

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

		// ???????????????
		try (FileOutputStream fos = new FileOutputStream(new File(outfile))) {
			this.wb.write(fos);
			this.wb.close();
		} catch (IOException e) {
			throw new LogicException(this.titaVo, "E0013", "(MakeExcel)??????" + outfile + "??????");
		}

		this.info("MakeExcel finished.");

	}

	@Override
	public void exec() throws LogicException {
		// override this

	}

	/**
	 * ???????????????????????????
	 * 
	 * @param caculateRow    ?????????, 1-based
	 * @param caculateColumn ?????????, 1-based
	 */
	public void formulaCaculate(int caculateRow, int caculateColumn) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", "9");
		map.put("r", caculateRow);
		map.put("c", caculateColumn);
		listMap.add(map);
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param calculateRow    ?????????, 1-based
	 * @param calculateColumn ?????????, 1-based
	 */
	public void formulaCalculate(int calculateRow, int calculateColumn) {
		// xwh: ????????????formulaCaculate???typo, ???????????????api
		formulaCaculate(calculateRow, calculateColumn);
	}

	/**
	 * ?????????????????????????????????
	 * 
	 * @param calculateRowTop      ???????????????, 1-based
	 * @param calculateRowBottom   ???????????????, 1-based
	 * @param calculateColumnLeft  ???????????????, 1-based
	 * @param calculateColumnRight ???????????????, 1-based
	 */
	public void formulaRangeCalculate(int calculateRowTop, int calculateRowBottom, int calculateColumnLeft,
			int calculateColumnRight) {
		for (; calculateRowTop <= calculateRowBottom; calculateRowTop++) {
			for (; calculateColumnLeft <= calculateColumnRight; calculateColumnLeft++)
				formulaCaculate(calculateRowTop, calculateColumnLeft);
		}
	}

	/**
	 * ???????????????????????????<BR>
	 * ????????????Excel?????????
	 * 
	 * @param caculateRow    ?????????
	 * @param caculateColumn ?????????
	 */
	private void formulaCaculateToExcel(int caculateRow, int caculateColumn) {

		Row pRow = this.sheet.getRow(caculateRow - 1);
		if (pRow != null) {
			Cell tmpCell = pRow.getCell(caculateColumn - 1);

			if (tmpCell != null && tmpCell.getCellType() == CellType.FORMULA) {
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
	 * ???????????????/??????<br>
	 * 
	 * @param row ???
	 * @param col ???
	 * @return Object ?????????
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

		// ?????????CellStyle???????????? outputFontStyleVo
		CellStyle originalCellStyle = cell.getCellStyle();

		// ??????????????????
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

		// ????????????
		outputFontStyleVo.setSize(tmpFont.getFontHeightInPoints());

		// ????????????
		int indexOfColors = tmpFont.getColor();
		if (indexOfColors >= 0 && indexOfColors <= 64) {
			IndexedColors indexColors = IndexedColors.fromInt(indexOfColors);
			outputFontStyleVo.setColor(indexColors.toString());
		}

		String fontName = tmpFont.getFontName();

		// ????????????
		switch (fontName) {
		case "?????????":
			outputFontStyleVo.setFont((short) 1);
			break;
		case "???????????????":
			outputFontStyleVo.setFont((short) 2);
			break;
		case "Times New Roman":
			outputFontStyleVo.setFont((short) 3);
			break;
		case "Arial":
			outputFontStyleVo.setFont((short) 4);
			break;
		case "????????????":
			outputFontStyleVo.setFont((short) 5);
			break;
		default:
			break;
		}

		// ??????
		outputFontStyleVo.setBold(tmpFont.getBold());

		// ??????
		outputFontStyleVo.setItalic(tmpFont.getItalic());

		// ??????
		Byte tmpUnderline = tmpFont.getUnderline();
		if (tmpUnderline.equals(Font.U_SINGLE)) {
			outputFontStyleVo.setUnderline(true);
		}

		// ??????
		outputFontStyleVo.setWrapText(originalCellStyle.getWrapText());

		// ?????? ???(??????)???
		// ?????????????????????setBgColor
		Color originalBgColor = originalCellStyle.getFillBackgroundColorColor();

		int indexOfBgColor = originalCellStyle.getFillBackgroundColor();

		if (originalBgColor != null && !(originalBgColor instanceof HSSFColor) && indexOfBgColor > 0
				&& indexOfBgColor <= 64) {
			IndexedColors bgColor = IndexedColors.fromInt(indexOfBgColor);
			outputFontStyleVo.setBgColor(bgColor.toString());
		}
	}

	/**
	 * ??????Sheet<br>
	 * 
	 * @param sheetName ??????sheet??????
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
	 * ??????excel??????<br>
	 * 
	 * @param titaVo   titaVo
	 * @param date     ??????
	 * @param brno     ??????
	 * @param fileCode ????????????
	 * @param fileItem ????????????
	 * @param fileName ??????????????????(???????????????,?????????????????????.xlsx)
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, int date, String brno, String fileCode, String fileItem, String fileName)
			throws LogicException {

		// ?????????sheetnanme???,????????????????????????sheetnanme
		this.open(titaVo, date, brno, fileCode, fileItem, fileName, fileCode);
	}

	/**
	 * ??????excel??????<br>
	 * 
	 * @param titaVo    titaVo
	 * @param date      ??????
	 * @param brno      ??????
	 * @param fileCode  ????????????
	 * @param fileItem  ????????????
	 * @param fileName  ??????????????????(???????????????,?????????.xlsx)
	 * @param sheetName ??????Sheet??????
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
	 * @param date         ??????
	 * @param brno         ??????
	 * @param fileCode     ????????????
	 * @param fileItem     ????????????
	 * @param fileName     ??????????????????(???????????????,?????????.xlsx)
	 * @param defaultExcel ??????excel?????????
	 * @param defaultSheet ??????sheet,????????? sheet index or sheet name
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
	 * ??????excel??????<br>
	 * 
	 * @param titaVo       titaVo
	 * @param date         ??????
	 * @param brno         ??????
	 * @param fileCode     ????????????
	 * @param fileItem     ????????????
	 * @param fileName     ?????????????????? (???????????????,?????????.xlsx)
	 * @param defaultExcel ??????excel????????? (???????????????)
	 * @param defaultSheet ??????sheet,????????? sheet index or sheet name
	 * @param newSheetName ??????sheet??????
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
	 * @param fileName  CSV??????
	 * @param splitchar ??????????????????
	 * @throws LogicException LogicException
	 */
	public void openCsv(String fileName, String splitchar) throws LogicException {
		this.info("MakeExcel.openCsv=" + fileName);

		this.listMap = new ArrayList<HashMap<String, Object>>();

		try (BufferedReader csvReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
			String line = null;
			while ((line = csvReader.readLine()) != null) {
				// 2020-11-11 Wei??????
//				this.info("MakeExcel.openCsv.readline=" + line);
				String[] item = line.split(splitchar);
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < item.length; i++) {
					map.put("f" + (i + 1), item[i].trim());
				}
				this.listMap.add(map);
			}
		} catch (FileNotFoundException e) {
			throw new LogicException(titaVo, "EC001", "(MakeExcel)??????:" + fileName);
		} catch (IOException e) {
			throw new LogicException(titaVo, "EC009", "(MakeExcel)");
		}

	}

	/**
	 * ??????EXCEL
	 * 
	 * @param fileName  Excel??????
	 * @param sheetname ????????? sheet index or sheet name
	 * @throws LogicException LogicException
	 */
	public void openExcel(String fileName, Object sheetname) throws LogicException {
		this.openFile(fileName, false);

		this.doSetSheet(sheetname, "");
	}

	private void openFile(String fileName, boolean excelfolder) throws LogicException {
		// ???????????????
//		System.out.println("file type = " + fileName.substring(l-4, l));

		String fna = "";

		if (excelfolder) {
			fna = workingExcelFolder + fileName;
		} else {
			fna = fileName;
		}

		int l = fileName.length();

		InputStream is = null;
		try {
			is = new FileInputStream(fna);
			if (".xls".equals(fileName.substring(l - 4, l))) {
				this.xls = true;
				this.wb = new HSSFWorkbook(is);
			} else if (".xlsx".equals(fileName.substring(l - 5, l))) {
				this.xls = false;
				this.wb = new XSSFWorkbook(is);
			} else {
				throw new LogicException(titaVo, "E0013", "(MakeExcel)" + fna + "????????????EXCEL????????????");
			}
		} catch (IOException e) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)" + fna + "???????????????");
		} finally {
			SafeClose.close(is);
		}
	}

	/**
	 * ???????????????(????????????)<br>
	 * ex:???A1~B3:setAddRengionBorder(A,1,B,3,1)
	 * 
	 * @param firstCell ?????????
	 * @param firstRow  ?????????
	 * @param lastCell  ?????????
	 * @param lastRow   ?????????
	 * @param point     ????????????
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
	 * ???????????????(??????)???
	 * 
	 * @param bgColor ??????(???????????????????????????) <br>
	 *                ?????????????????????????????????IndexedColors??????????????????
	 */

	public void setBackGroundColor(String bgColor) {

		inputFontStyleVo.setBgColor(bgColor);

	}

	/**
	 * ???????????? ??????
	 * 
	 * @param borderAll ??????<br>
	 *                  1=???????????? <br>
	 */
	public void setBorder(int borderAll) {

		inputFontStyleVo.setBorderAll((short) borderAll);

	}

	/**
	 * ???????????? ??????
	 * 
	 * @param color ??????(???????????????????????????) <br>
	 *              ?????????????????????????????????IndexedColors??????????????????
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
	 * @param readStyle ??????????????????
	 * @throws CloneNotSupportedException
	 */
	private CellStyle setFontStyle(boolean readStyle) {

		// ?????????????????? CellStyle
		CellStyle resultCellStyle = null;

		if (isDefaultExcel) {

			int originalCellStyleIndex = cell.getCellStyle().getIndex();

			if (originalCellStyleIndex == 0) {
				// ???CellStyleIndex???0???,???outputFontStyleVo????????????
				readStyle = false;
			} else {
				readStyle = true;
				resultCellStyle = this.wb.getCellStyleAt(originalCellStyleIndex);
			}

//			this.info("????????? originalCellStyleIndex = " + originalCellStyleIndex);
//			this.info("????????? resultCellStyle.getIndex = " + resultCellStyle.getIndex());
		}

		// ????????????????????????,??????????????????????????????outputFontStyleVo
		if (readStyle) {
			modifyOuputFontStyleVo();
		}

		// CellStyle ??? Index
		int indexOfCellStyle;

		// ??????CellStyle??????
		int numOfCellStyle = this.wb.getNumCellStyles();

		// xlsx CellStyle ??????????????? 64000
		int limitOfCellStyle = 64000;

		if (xls) {
			// xls CellStyle ??????????????? 4000
			limitOfCellStyle = 4000;
		}

		// ??????ExcelFontStyleVo ???????????? Map
		// ?????????????????? CellStyle ??? Index
		// ??? ?????????CellStyle
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

		// ??? ExcelFontStyleVo ???????????? Map
		// ??? ??????CellStyle?????? <= CellStyle????????????
		// ??? ??????????????????
		// ??? ????????????CellStyle , ????????? CellStyle???Index ????????? Map
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

			// ??? ExcelFontStyleVo ???????????? Map
			// ??? ??????CellStyle?????? <= CellStyle????????????
			// ??? ???????????????
			// ??? ?????????????????????CellStyle , ????????? CellStyle???Index ????????? Map
		} else if (numOfCellStyle < limitOfCellStyle && isDefaultExcel) {

			// ??????????????????CellStyle??????,?????????CellStyle
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

			// ??????,???this.error,???????????????CellStyle
		} else {

			if (isFirstTimeToStyleLimit) {
				this.error("setFontStyle error : CellStyle?????????" + limitOfCellStyle + ",??????Style??????,??????CellStyle?????????"
						+ numOfCellStyle);
				isFirstTimeToStyleLimit = false;
			}
			return this.wb.getCellStyleAt(0);
		}

		// ????????????????????? <= limitOfCellStyle
		Font font = this.wb.createFont();

		// ??????font??????
		if (outputFontStyleVo.getSize() != 0) {
			font.setFontHeightInPoints(outputFontStyleVo.getSize());
		} else {
			// ??????12
			font.setFontHeightInPoints((short) 12);
		}
		// ????????????
		if (!outputFontStyleVo.getColor().isEmpty()) {
			try {
				IndexedColors indexColors = IndexedColors.valueOf(outputFontStyleVo.getColor().toUpperCase());
				font.setColor(indexColors.getIndex());
			} catch (Exception e) {
				this.error("MakeExcel colors erros = " + e.getMessage() + "," + e.getStackTrace());
			}
		}

		// ????????????
		switch (outputFontStyleVo.getFont()) {
		case 1:
			font.setFontName("?????????");
			break;
		case 2:
			font.setFontName("???????????????");
			break;
		case 3:
			font.setFontName("Times New Roman");
			break;
		case 4:
			font.setFontName("Arial");
			break;
		case 5:
			font.setFontName("????????????");
			break;
		default:
			break;
		}

		// ??????
		if (outputFontStyleVo.isBold()) {
			font.setBold(true);
		}

		// ??????
		if (outputFontStyleVo.isItalic()) {
			font.setItalic(true);
		}

		// ??????
		if (outputFontStyleVo.isUnderline()) {
			font.setUnderline(Font.U_SINGLE);
			font.getUnderline();
		}

		// font????????????style
		resultCellStyle.setFont(font);

		// ????????????.setDataFormat(this.df.getFormat(format));
		if (!outputFontStyleVo.getFormat().isEmpty()) {
			resultCellStyle.setDataFormat(this.df.getFormat(outputFontStyleVo.getFormat()));
		}

		// ????????????
		if (outputFontStyleVo.getBorderAll() > 0) {
			// ????????????
			resultCellStyle.setBorderTop(BorderStyle.valueOf(outputFontStyleVo.getBorderAll()));
			resultCellStyle.setBorderRight(BorderStyle.valueOf(outputFontStyleVo.getBorderAll()));
			resultCellStyle.setBorderBottom(BorderStyle.valueOf(outputFontStyleVo.getBorderAll()));
			resultCellStyle.setBorderLeft(BorderStyle.valueOf(outputFontStyleVo.getBorderAll()));
		}

		// ??????????????????: ????????????
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

		// ??????????????????: ????????????
		resultCellStyle.setVerticalAlignment(VerticalAlignment.TOP);

		// ?????? ???(??????)???
		if (!outputFontStyleVo.getBgColor().isEmpty()) {
			try {
				IndexedColors indexedColors = IndexedColors.valueOf(outputFontStyleVo.getBgColor().toUpperCase());

				resultCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				resultCellStyle.setFillForegroundColor(indexedColors.getIndex());
			} catch (Exception e) {
				this.error("MakeExcel set Indexed bgColors erros = " + e.getMessage() + "," + e.getStackTrace());
			}
		}

		// ??????
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
	 * ???????????? ??????
	 * 
	 * @param fontType ??????<br>
	 *                 1=?????????<br>
	 *                 2=???????????????<br>
	 *                 3=Times New Roman<br>
	 *                 4=Arial<br>
	 *                 5=????????????
	 */
	public void setFontType(int fontType) {

		this.inputFontStyleVo.setFont((short) fontType);

	}

	/**
	 * ???????????? (????????????0:????????????,???????????????????????????????????????)
	 * 
	 * @param row    ???
	 * @param height ???????????????,0:????????????
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
	 * ???????????? ?????? ?????? ??????
	 * 
	 * @param ibu ?????? ?????? ?????? <br>
	 *            I= ?????? <br>
	 *            B=?????? <br>
	 *            U=??????<br>
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
	 * ????????????
	 * 
	 * @param frow ?????????
	 * @param lrow ?????????
	 * @param fcol ?????????
	 * @param lcol ?????????
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
	 * ?????????/??? ????????????????????????<br>
	 * 
	 * @param row  ?????????
	 * @param lrow ?????????
	 * @param col  ?????????
	 * @param lcol ?????????
	 * @param val  ?????????
	 * @throws LogicException LogicException
	 */
	public void setMergedRegionValue(int row, int lrow, int col, int lcol, Object val) throws LogicException {

		setValueToMap(row, lrow, col, lcol, val);
	}

	/**
	 * ?????????/??? ????????????????????????<br>
	 * 
	 * @param row           ?????????
	 * @param lrow          ?????????
	 * @param col           ?????????
	 * @param lcol          ?????????
	 * @param val           ?????????
	 * @param formatOrAlign <br>
	 *                      ???????????????????????????????????????<br>
	 *                      ??????????????????<br>
	 *                      "#,##0" ?????????????????????????????????<br>
	 *                      "#,##0.00" ?????????????????????????????????????????????<br>
	 *                      "#0.0000"<br>
	 *                      ...????????????<br>
	 *                      <br>
	 *                      ????????????<br>
	 *                      L ????????????<br>
	 *                      C ????????????<br>
	 *                      R ????????????<br>
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
	 * ?????????/?????????<br>
	 * 
	 * @param row    ?????????
	 * @param lrow   ?????????
	 * @param col    ?????????
	 * @param lcol   ?????????
	 * @param val    ?????????
	 * @param format ????????????<br>
	 *               "#,##0" or "#,##0.00" or "#0.0000"
	 * @param align  L ????????????<br>
	 *               C ????????????<br>
	 *               R ????????????<br>
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
	 * ??????Sheet<br>
	 * 
	 * @param sheet ????????? sheet index or sheet name
	 * @throws LogicException LogicException
	 */
	public void setSheet(Object sheet) throws LogicException {

		this.setSheetToMap(sheet, "");

	}

	/**
	 * ??????Sheet<br>
	 * 
	 * @param sheet        ????????? sheet index or sheet name
	 * @param newSheetName ????????????sheet??????
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
	 * ?????????,?????????(??????????????????)???????????????????????????????????????
	 * 
	 * @param shiftRowFrom ??????????????????????????????
	 * @param shiftCounts  ????????????
	 */
	public void setShiftRow(int shiftRowFrom, int shiftCounts) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 8);
		map.put("srf", shiftRowFrom);
		map.put("n", shiftCounts);
		listMap.add(map);
	}

	/**
	 * ???????????? ??????
	 * 
	 * @param size ????????????
	 */
	public void setSize(int size) {

		this.inputFontStyleVo.setSize((short) size);

	}

	/**
	 * ?????????/?????????<br>
	 * 
	 * @param row ???
	 * @param col ???
	 * @param val ?????????
	 * @throws LogicException LogicException
	 */

	public void setValue(int row, int col, Object val) throws LogicException {

		setValueToMap(row, 0, col, 0, val);
	}

	/**
	 * ?????????/?????????<br>
	 * 
	 * @param row            ???
	 * @param col            ???
	 * @param val            ?????????
	 * @param tmpFontStyleVo ????????????
	 * @throws LogicException LogicException
	 */

	public void setValue(int row, int col, Object val, ExcelFontStyleVo tmpFontStyleVo) throws LogicException {

		this.setFontStyleVo(tmpFontStyleVo);

		setValueToMap(row, 0, col, 0, val);
	}

	/**
	 * ?????????/?????????<br>
	 * 
	 * @param row           ???
	 * @param col           ???
	 * @param val           ?????????
	 * @param formatOrAlign <br>
	 *                      ???????????????????????????????????????<br>
	 *                      ??????????????????<br>
	 *                      "#,##0" ?????????????????????????????????<br>
	 *                      "#,##0.00" ?????????????????????????????????????????????<br>
	 *                      "#0.0000"<br>
	 *                      ...????????????<br>
	 *                      <br>
	 *                      ????????????<br>
	 *                      L ????????????<br>
	 *                      C ????????????<br>
	 *                      R ????????????<br>
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
	 * ?????????/?????????<br>
	 * 
	 * @param row            ???
	 * @param col            ???
	 * @param val            ?????????
	 * @param formatOrAlign  <br>
	 *                       ???????????????????????????????????????<br>
	 *                       ??????????????????<br>
	 *                       "#,##0" ?????????????????????????????????<br>
	 *                       "#,##0.00" ?????????????????????????????????????????????<br>
	 *                       "#0.0000"<br>
	 *                       ...????????????<br>
	 *                       <br>
	 *                       ????????????<br>
	 *                       L ????????????<br>
	 *                       C ????????????<br>
	 *                       R ????????????<br>
	 * @param tmpFontStyleVo ????????????
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
	 * ?????????/?????????<br>
	 * 
	 * @param row    ???
	 * @param col    ???
	 * @param val    ?????????
	 * @param format ????????????<br>
	 *               "#,##0" or "#,##0.00" or "#0.0000"
	 * @param align  L ????????????<br>
	 *               C ????????????<br>
	 *               R ????????????<br>
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
	 * ?????????/?????????<br>
	 * 
	 * @param row            ???
	 * @param col            ???
	 * @param val            ?????????
	 * @param format         ????????????<br>
	 *                       "#,##0" or "#,##0.00" or "#0.0000"
	 * @param align          L ????????????<br>
	 *                       C ????????????<br>
	 *                       R ????????????<br>
	 * @param tmpFontStyleVo ????????????
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

//		this.info("val = " + val.toString());
//		this.info("needStyle = " + needStyle);
//		this.info("cellHasStyle = " + cellHasStyle);
		if (needStyle && cellHasStyle) {
			cell.setCellStyle(setFontStyle(false));
		}

		// ?????????????????????????????????????????????????????????*1.5???=?????????????????????
		// ??????????????????16 ??????????????????(??????)12 ??????/??????=1.33 ??????1.5
		float tempRH = (float) (this.wb.getFontAt(cell.getCellStyle().getFontIndexAsInt()).getFontHeightInPoints()
				* fontWrap * 1.5);
		// ???????????????
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

		// ??????list??????????????????
		inputFontStyleVo = inputFontStyleVo.init();
	}

	/**
	 * ?????????????????? (????????????0:????????????,???????????????????????????????????????)
	 * 
	 * @param col   ???
	 * @param width ???????????????, 0???????????????????????????????????????,???????????????????????????
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
	 * ???????????????????????????EXCEL<br>
	 * 
	 * @param fileno ????????????
	 * @throws LogicException LogicException
	 */
	public void toExcel(long fileno) throws LogicException {
		doToExcel(fileno, "");
	}

	/**
	 * ???????????????????????????EXCEL<br>
	 * 
	 * @param fileno   ????????????
	 * @param fileName ??????????????????
	 * @throws LogicException LogicException
	 */
	public void toExcel(long fileno, String fileName) throws LogicException {
		doToExcel(fileno, fileName);
	}

	// 2022.3.25 by eric for ??????openExcel??????EXCEL???
	public void setValueInt(int row, int col, int val) throws LogicException {
		if (this.sheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel) getValue sheet is null");
		}

		Row prow = this.sheet.getRow(row - 1);

		if (prow != null) {
			Cell tmpCell = prow.getCell(col - 1);

			tmpCell.setCellValue(val);
		} else {
			throw new LogicException(titaVo, "E0013", "(MakeExcel) setValueInt error = " + row + "/" + col + "/" + val);
		}

	}

	public void saveExcel(String outfile) throws LogicException {

		try (FileOutputStream fos = new FileOutputStream(new File(outfile))) {
			this.wb.write(fos);
			this.wb.close();
		} catch (IOException e) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel) close excel ");
		}
	}

}
