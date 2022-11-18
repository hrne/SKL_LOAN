package com.st1.itx.util.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.filter.SafeClose;
import com.st1.itx.util.report.ReportUtil;

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

	/* excel底稿路徑 */
	@Value("${iTXExcelFolder}")
	private String workingExcelFolder = "";

	/* DB服務注入 */
	@Autowired
	private TxFileService sTxFileService;

	@Autowired
	private ReportUtil rptUtil;

	@Autowired
	private ExcelGenerator excelGenerator;

	private Workbook openedWorkbook = null;

	private Sheet openedSheet = null;

	private String outputFileName = "";

	// 資料明細
	private List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

	private ExcelFontStyleVo inputFontStyleVo = new ExcelFontStyleVo();

	private ReportVo reportVo;

	private boolean isNeedStyle = true;

	private boolean isXls = false;

	private void checkParameters(int date, String brno, String fileCode, String fileItem, String fileName)
			throws LogicException {
		if (date == 0) {
			throw new LogicException("EC004", "(MakeExcel)日期(date)必須有值(MakeExcel)");
		}
		if ("".equals(brno)) {
			throw new LogicException("EC004", "(MakeExcel)單位(brno)必須有值(MakeExcel)");
		}
		if ("".equals(fileCode)) {
			throw new LogicException("EC004", "(MakeExcel)檔案編號(fileCode)必須有值(MakeExcel)");
		}
		if (rptUtil.haveChinese(fileCode)) {
			throw new LogicException("EC007", "(MakeExcel)檔案編號(fileCode)參數不可有全形字");
		}
		if ("".equals(fileItem)) {
			throw new LogicException("EC004", "(MakeExcel)檔案名稱(fileItem)必須有值(MakeExcel)");
		}
		if ("".equals(fileName)) {
			throw new LogicException("EC004", "(MakeExcel)輸出檔名(outfile)必須有值(MakeExcel)");
		}
		this.outputFileName = fileName;
		reportVo = ReportVo.builder().setRptDate(date).setBrno(brno).setRptCode(fileCode).setRptItem(fileItem).build();
	}

	private void checkParameters(ReportVo reportVo, String fileName) throws LogicException {
		if (reportVo.getRptDate() == 0) {
			throw new LogicException("EC004", "(MakeExcel)日期(RptDate)必須有值(MakeExcel)");
		}
		if ("".equals(reportVo.getBrno())) {
			throw new LogicException("EC004", "(MakeExcel)單位(Brno)必須有值(MakeExcel)");
		}
		if ("".equals(reportVo.getRptCode())) {
			throw new LogicException("EC004", "(MakeExcel)檔案編號(RptCode)必須有值(MakeExcel)");
		}
		if (rptUtil.haveChinese(reportVo.getRptCode())) {
			throw new LogicException("EC007", "(MakeExcel)檔案編號(RptCode)參數不可有全形字");
		}
		if ("".equals(reportVo.getRptItem())) {
			throw new LogicException("EC004", "(MakeExcel)檔案名稱(RptItem)必須有值(MakeExcel)");
		}
		if ("".equals(fileName)) {
			throw new LogicException("EC004", "(MakeExcel)輸出檔名(outfile)必須有值(MakeExcel)");
		}
		this.outputFileName = fileName;
		this.reportVo = reportVo;
	}

	/**
	 * 結束excel製檔<br>
	 * 
	 * @return long 檔案序號
	 * @throws LogicException LogicException
	 */
	public long close() throws LogicException {
		if (isXls) {
			this.outputFileName += ".xls";
		} else {
			this.outputFileName += ".xlsx";
		}

		this.info("MakeExcel.close=" + this.outputFileName);
		TxFile tTxFile = new TxFile();

		tTxFile.setFileType(2); // 固定2:EXCEL
		tTxFile.setFileFormat(1);
		tTxFile.setFileCode(this.reportVo.getRptCode());
		tTxFile.setFileItem(this.reportVo.getRptItem());
		tTxFile.setFileOutput(this.outputFileName);
		tTxFile.setBrNo(this.reportVo.getBrno());
		tTxFile.setFileDate(this.reportVo.getRptDate());
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

	private void doNewSheet(String sheetName) {
		this.openedSheet = this.openedWorkbook.createSheet(sheetName);
	}

	private void doOpen(String fileName) throws LogicException {
		if ("".equals(fileName)) {
			this.isXls = false;

			this.openedWorkbook = new XSSFWorkbook();
		} else {
			this.openFile(fileName, true);
		}
	}

	private void doSetHeight(int row, int height) {

		Row thisRow = openedSheet.getRow(row - 1);

		if (thisRow == null) {
			thisRow = openedSheet.createRow(row - 1);
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
				this.openedSheet = this.openedWorkbook.getSheet(sheet.toString());
				sheetname = sheet.toString();
			} else {
				this.openedSheet = this.openedWorkbook.getSheetAt(Integer.valueOf(sheet.toString()) - 1);
				sheetname = this.openedSheet.getSheetName();
			}
			if (!"".equals(newSheetName)) {
				int sheetindex = this.openedWorkbook.getSheetIndex(this.openedSheet);
				this.openedWorkbook.setSheetName(sheetindex, newSheetName);
			}
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)指定 SHEET (" + sheet + ") 不存在");
		}

		if (this.openedSheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)指定 SHEET (" + sheet + ") 不存在");
		}

		return sheetname;
	}

	private void doSetWidth(int col, int width) {
		if (width == 0) {
			openedSheet.autoSizeColumn(col - 1);
		} else {
			this.openedSheet.setColumnWidth(col - 1, width * 256);
		}
	}

	@Override
	public void exec() throws LogicException {
		// override this
	}

	/**
	 * 公式儲存格重新計算
	 * 
	 * @param calculateRow    第幾列, 1-based
	 * @param calculateColumn 第幾欄, 1-based
	 */
	public void formulaCaculate(int calculateRow, int calculateColumn) {
		formulaCalculate(calculateRow, calculateColumn);
	}

	/**
	 * 公式儲存格重新計算
	 * 
	 * @param calculateRow    第幾列, 1-based
	 * @param calculateColumn 第幾欄, 1-based
	 */
	public void formulaCalculate(int calculateRow, int calculateColumn) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", "9");
		map.put("r", calculateRow);
		map.put("c", calculateColumn);
		listMap.add(map);
	}

	/**
	 * 公式儲存格範圍重新計算
	 * 
	 * @param calculateRowTop      範圍最頂列, 1-based
	 * @param calculateRowBottom   範圍最底列, 1-based
	 * @param calculateColumnLeft  範圍最左欄, 1-based
	 * @param calculateColumnRight 範圍最右欄, 1-based
	 */
	public void formulaRangeCalculate(int calculateRowTop, int calculateRowBottom, int calculateColumnLeft,
			int calculateColumnRight) {
		for (; calculateRowTop <= calculateRowBottom; calculateRowTop++)
			for (; calculateColumnLeft <= calculateColumnRight; calculateColumnLeft++)
				formulaCalculate(calculateRowTop, calculateColumnLeft);
	}

	public int getDate() {
		return reportVo.getRptDate();
	}

	public String getFileCode() {
		return reportVo.getRptCode();
	}

	public String getFileItem() {
		return reportVo.getRptItem();
	}

	/**
	 * @return the listMap
	 */
	public List<HashMap<String, Object>> getListMap() {
		return listMap;
	}

	public int getSheetLastRowNum() {
		return this.openedSheet.getLastRowNum();
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
		if (this.openedSheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel) openedSheet is null");
		}

		Row prow = this.openedSheet.getRow(row - 1);

		if (prow == null) {
			return "";
		} else {
			Cell tmpCell = prow.getCell(col - 1);
			Object result = null;
			switch (tmpCell.getCellType()) {
			case NUMERIC:
				result = tmpCell.getNumericCellValue();
				break;
			case BOOLEAN:
				result = tmpCell.getBooleanCellValue();
				break;
			case FORMULA:
				if (tmpCell.getCachedFormulaResultType() == CellType.NUMERIC) {
					result = tmpCell.getNumericCellValue();
				} else if (tmpCell.getCachedFormulaResultType() == CellType.STRING) {
					result = tmpCell.getStringCellValue();
				} else {
					result = tmpCell.getCachedFormulaResultType().toString();
				}
				break;
			case STRING:
			default:
				result = tmpCell.getStringCellValue();
			}
			return result == null ? "" : result;
		}
	}

	public boolean isNeedStyle() {
		return isNeedStyle;
	}

	/**
	 * 新增Sheet<br>
	 * 
	 * @param sheetName 新增sheet名稱
	 * @throws LogicException LogicException
	 */
	public void newSheet(String sheetName) throws LogicException {
		this.doNewSheet(sheetName);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 2);
		map.put("s", sheetName);
		listMap.add(map);
	}

	/**
	 * 開啟excel製檔<br>
	 * 
	 * @deprecated use
	 *             {@link #open(TitaVo titaVo, ReportVo reportVo, String fileName)}
	 *             instead.
	 * @param titaVo   titaVo
	 * @param date     日期
	 * @param brno     單位
	 * @param fileCode 檔案編號
	 * @param fileItem 檔案說明
	 * @param fileName 輸出檔案名稱(不含副檔名,預設檔案編號為.xlsx)
	 * @throws LogicException LogicException
	 */
	@Deprecated
	public void open(TitaVo titaVo, int date, String brno, String fileCode, String fileItem, String fileName)
			throws LogicException {
		// 未指定sheetnanme時,預設以檔案編號為sheetnanme
		this.open(titaVo, date, brno, fileCode, fileItem, fileName, fileCode);
	}

	/**
	 * 開啟excel製檔<br>
	 * 
	 * @deprecated use
	 *             {@link #open(TitaVo titaVo, ReportVo reportVo, String fileName, String sheetName)}
	 *             instead.
	 * @param titaVo    titaVo
	 * @param date      日期
	 * @param brno      單位
	 * @param fileCode  檔案編號
	 * @param fileItem  檔案說明
	 * @param fileName  輸出檔案名稱(不含副檔名,預設為.xlsx)
	 * @param sheetName 新建Sheet名稱
	 * @throws LogicException LogicException
	 */
	@Deprecated
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
	 * @deprecated use
	 *             {@link #open(TitaVo titaVo, ReportVo reportVo, String fileName, String defaultExcel, Object defaultSheet)}
	 *             instead.
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
	@Deprecated
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
	 * @deprecated use
	 *             {@link #open(TitaVo titaVo, ReportVo reportVo, String fileName, String defaultExcel, Object defaultSheet, String newSheetName)}
	 *             instead.
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
	@Deprecated
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
	 * 開啟excel製檔<br>
	 * 
	 * @param titaVo   titaVo
	 * @param reportVo reportVo
	 * @param fileName 檔案實際輸出名稱
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, ReportVo reportVo, String fileName) throws LogicException {
		// 未指定sheetnanme時,預設以檔案編號為sheetnanme
		this.open(titaVo, reportVo, fileName, reportVo.getRptCode());
	}

	/**
	 * 開啟excel製檔<br>
	 * 
	 * @param titaVo    titaVo
	 * @param reportVo  reportVo
	 * @param fileName  檔案實際輸出名稱
	 * @param sheetName 頁籤名稱
	 * @throws LogicException LogicException
	 */
	private void open(TitaVo titaVo, ReportVo reportVo, String fileName, String sheetName) throws LogicException {
		this.titaVo = titaVo;
		this.checkParameters(reportVo, fileName);

		this.doOpen("");

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 0);
		map.put("e", "");
		listMap.add(map);

		this.newSheet(sheetName);
	}

	/**
	 * 開啟excel製檔<br>
	 * 指定底稿
	 * 
	 * @param titaVo       titaVo
	 * @param reportVo     reportVo
	 * @param fileName     檔案實際輸出名稱
	 * @param defaultExcel 底稿檔案名稱
	 * @param defaultSheet 底稿頁籤名稱
	 * @throws LogicException
	 */
	public void open(TitaVo titaVo, ReportVo reportVo, String fileName, String defaultExcel, Object defaultSheet)
			throws LogicException {
		this.titaVo = titaVo;
		this.checkParameters(reportVo, fileName);

		this.doOpen(defaultExcel);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 0);
		map.put("e", defaultExcel);
		listMap.add(map);

		this.setSheet(defaultSheet);
	}

	/**
	 * 開啟excel製檔<br>
	 * 指定底稿,並修改頁籤名稱
	 * 
	 * @param titaVo       titaVo
	 * @param reportVo     reportVo
	 * @param fileName     檔案實際輸出名稱
	 * @param defaultExcel 底稿檔案名稱
	 * @param defaultSheet 底稿頁籤名稱
	 * @param newSheetName 修改頁籤名稱
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, ReportVo reportVo, String fileName, String defaultExcel, Object defaultSheet,
			String newSheetName) throws LogicException {
		this.titaVo = titaVo;
		this.checkParameters(reportVo, fileName);

		this.doOpen(defaultExcel);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", 0);
		map.put("e", defaultExcel);
		listMap.add(map);

		this.setSheet(defaultSheet, newSheetName);
	}

	/**
	 * 讀取CSV
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
	 * 讀取EXCEL
	 * 
	 * @param fileName  Excel檔名
	 * @param sheetname 可指定 sheet index or sheet name
	 * @throws LogicException LogicException
	 */
	public void openExcel(String fileName, Object sheetname) throws LogicException {
		this.info("openExcel start");
		try (FileInputStream fis = new FileInputStream(fileName)) {
			this.openedWorkbook = new XSSFWorkbook(fis);
//			this.openedSheet = openedWorkbook.getSheet(sheetname.toString());

			if (sheetname instanceof String) {
				this.openedSheet = this.openedWorkbook.getSheet(sheetname.toString());
				sheetname = sheetname.toString();
			} else {
				this.openedSheet = this.openedWorkbook.getSheetAt(Integer.valueOf(sheetname.toString()) - 1);
				sheetname = this.openedSheet.getSheetName();
			}

		} catch (FileNotFoundException e1) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)此檔案 (" + fileName + ") 不存在");
		} catch (IOException e) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)無法開啟此Excel檔案");
		}
		this.info("openExcel finished");
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
				this.openedWorkbook = new HSSFWorkbook(is);
			} else if (".xlsx".equals(fileName.substring(l - 5, l))) {
				this.isXls = false;
				this.openedWorkbook = new XSSFWorkbook(is);
			} else {
				throw new LogicException(titaVo, "E0013", "(MakeExcel)" + fna + "，非預設EXCEL檔案格式");
			}
			SafeClose.close(is);
		} catch (IOException e) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)" + fna + "檔案不存在");
		}
	}

	public void saveExcel(String outfile) throws LogicException {
		try (FileOutputStream fos = new FileOutputStream(new File(outfile))) {
			this.openedWorkbook.write(fos);
			this.openedWorkbook.close();
		} catch (IOException e) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel) close excel ");
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
		this.reportVo.setRptDate(date);
	}

	public void setFileCode(String fileCode) {
		this.reportVo.setRptCode(fileCode);
	}

	public void setFileItem(String fileItem) {
		this.reportVo.setRptItem(fileItem);
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
	 *            I=斜體<br>
	 *            B=粗體<br>
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
		this.isNeedStyle = needStyle;
		// 若不需要格式
		if (!this.isNeedStyle) {
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

	CellStyle errorColumnStyle = null;

	public void setErrorColumn(int row, int col) throws LogicException {
		if (this.openedSheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel) openedSheet is null");
		}

		Row prow = this.openedSheet.getRow(row - 1);

		if (errorColumnStyle == null) {
			errorColumnStyle = this.openedWorkbook.createCellStyle();
			errorColumnStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
			errorColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}

		if (prow != null) {
			Cell tmpCell = prow.getCell(col - 1);
			tmpCell.setCellStyle(errorColumnStyle);
		} else {
			throw new LogicException(titaVo, "E0013", "(MakeExcel) setErrorColumn error = " + row + "/" + col);
		}
	}

	CellStyle defaultColumnStyle = null;

	public void setDefaultColumn(int row, int col) throws LogicException {
		if (this.openedSheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel) openedSheet is null");
		}

		Row prow = this.openedSheet.getRow(row - 1);

		if (defaultColumnStyle == null) {
			defaultColumnStyle = this.openedWorkbook.createCellStyle();
			defaultColumnStyle.setFillForegroundColor(IndexedColors.AUTOMATIC.getIndex());
			defaultColumnStyle.setFillPattern(FillPatternType.NO_FILL);
		}

		if (prow != null) {
			Cell tmpCell = prow.getCell(col - 1);
			if (tmpCell != null) {
				tmpCell.setCellStyle(defaultColumnStyle);
			}
		}
	}

	// 2022.3.25 by eric for 配合openExcel變更EXCEL檔
	public void setValueInt(int row, int col, int val) throws LogicException {
		if (this.openedSheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel) openedSheet is null");
		}

		Row prow = this.openedSheet.getRow(row - 1);

		if (prow != null) {
			Cell tmpCell = prow.getCell(col - 1);
			tmpCell.setCellValue(val);
		} else {
			throw new LogicException(titaVo, "E0013", "(MakeExcel) setValueInt error = " + row + "/" + col + "/" + val);
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
		excelGenerator.generateExcel(fileno, "");
	}

	/**
	 * 將指定檔案序號產製EXCEL<br>
	 * 
	 * @param fileno   檔案序號
	 * @param fileName 指定輸出檔名
	 * @throws LogicException LogicException
	 */
	public void toExcel(long fileno, String fileName) throws LogicException {
		excelGenerator.generateExcel(fileno, fileName);
	}

	/**
	 * 設定公式到儲存格
	 * 
	 * @param row                目標行
	 * @param col                目標欄
	 * @param precalculatedValue 預先計算值
	 * @param formula            公式
	 * @param format             格式
	 */
	public void setFormula(int row, int col, BigDecimal precalculatedValue, String formula, String format) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("t", "A");
		map.put("r", row);
		map.put("c", col);
		map.put("v", precalculatedValue);
		map.put("f", formula);
		map.put("ft", format);
		listMap.add(map);
	}

	public void lockColumn(int rowStart, int rowEnd, int columnStart, int columnEnd, int totalColumn)
			throws LogicException {
		this.info("lockColumn start");
		if (this.openedSheet == null) {
			throw new LogicException(titaVo, "E0013", "(MakeExcel)lockColumn sheet is null");
		}
		Map<Integer, CellStyle> cellStyleMap = new HashMap<>();
		for (int nowRow = rowStart; nowRow <= rowEnd; nowRow++) {
			Row processingRow = this.openedSheet.getRow(nowRow - 1);
			if (processingRow == null) {
				processingRow = this.openedSheet.createRow(nowRow - 1);
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
					CellStyle tempStyle = this.openedWorkbook.createCellStyle();
					tempStyle.cloneStyleFrom(oriCellStyle);
					tempStyle.setLocked(nowColumn <= columnEnd); // 鎖定或不鎖定的判斷
					cellStyleMap.put(nowColumn, tempStyle);
				}
				cell.setCellStyle(cellStyleMap.get(nowColumn));
			}
		}
	}

	public void protectSheet(String groupNo) {
		this.openedSheet.protectSheet(groupNo);
	}
}
