package com.st1.itx.util.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.date.DateUtil;

/**
 * 
 * ----------------------- MakeReport 產生報表(PDF)共用工具 ------------------*
 * 
 * @author eric chang
 *
 */

@Component("makeReport")
@Scope("prototype")
//  A4 : 595 * 842
//  一英寸有72個點，如果你想要創建一個有a4矩形大小的pdf文檔，你必須計算點的數目：
//
//  21 cm / 2.54 = 8.2677 inch
//  8.2677 * 72 = 595 points
//  29.7 cm / 2.54 = 11.6929 inch
//  11.6929 * 72 = 842 points

public class MakeReport extends CommBuffer {

	/* DB服務注入 */
	@Autowired
	TxFileService txFileService;

	@Autowired
	TxTellerService txTellerService;

	@Autowired
	CdReportService cdReportService;

	@Autowired
	CdEmpService cdEmpService;

	@Autowired
	public DateUtil dDateUtil;

	// pdf底稿路徑
	@Value("${iTXPdfFolder}")
	private String pdfFolder = "";

	// 檔案輸出路徑
	@Value("${iTXOutFolder}")
	private String outputFolder = "";

	// 字型放置路徑
	@Value("${iTXFontFolder}")
	private String fontFolder = "";

	// 資源路徑
	@Value("${iTXResourceFolder}")
	private String ResourceFolde = "";

	private int date = 0;

	private String brno = "";

	// 程式ID
	private String parentTranCode = "";

	// 報表代碼
	private String rptCode;
	// 報表名稱
	private String rptItem;

	// 是否需要浮水印
	private boolean watermarkFlag;

	// 報表機密等級(中文敍述)
	private String rptSecurity;
	// 紙張大小
	private String rptSize;
	// 紙張方向 P:Portrait Orientation (直印) , L:Landscape Orientation (橫印)
	private String pageOrientation;
	// 預設PDF底稿
	private String defaultPdf = "";
	private boolean useDefault = false;

	// 字型 1.標楷體 2.細明體
	private int font;
	// 字型大小
	private int fontSize;
	// 報表明細起始列數
	private int rptBeginRow;
	// 報表明細可印列數
	private int rptTotalRows;

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;
	// 目前頁數
	private int nowPage;
	// 目前row位置
	public int NowRow;

	// header,footer處理記號
	private boolean hfProcess = false;

	// 寬度點數
	private double xPoints = 0;
	// 高度點數
	private double yPoints = 0;

	// 輸出內容
	private int printCnt = 0;

	// 簽核
	private String signOff = "$$$Sign off$$$";

	private String rptPassword = "";

	private boolean formMode = false;

	// 列印明細
	List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

	/**
	 * 開始製作印表機套印格式
	 * 
	 * @param titaVo          titaVo
	 * @param date            日期
	 * @param brno            單位
	 * @param rptCode         報表編號
	 * @param rptItem         報表說明
	 * @param PageSize        報表尺寸,例A4,A5,LETTER;自訂尺寸(單位,寛,長)(單位:mm,cm,inch),例:cm,8.5,5.5
	 * @param pageOrientation 報表方向,P:直印/L:橫印
	 * @throws LogicException LogicException
	 */
	public void openForm(TitaVo titaVo, int date, String brno, String rptCode, String rptItem, String PageSize,
			String pageOrientation) throws LogicException {

		formMode = true;

		this.checkParm(date, brno, rptCode, rptItem);

		this.titaVo = titaVo;

		this.date = date;
		this.brno = brno;
		this.rptCode = rptCode;
		this.rptItem = rptItem;

		this.rptSize = PageSize.toUpperCase();

		String[] ss = PageSize.split(",");

		this.info("PageSize length = " + ss.length);

		if (ss.length != 1 && ss.length != 3) {
			throw new LogicException("EC004", "(MakeReport)報表尺寸錯誤=" + PageSize);
		}

		pageOrientation = pageOrientation.toUpperCase();
		if ("P".equals(pageOrientation)) {
			this.pageOrientation = pageOrientation;
		} else {
			this.pageOrientation = "L";
		}

		this.useDefault = false;

		init();
	}

	/**
	 * 開始製作報表<br>
	 * 
	 * @param titaVo          titaVo
	 * @param date            日期
	 * @param brno            單位
	 * @param rptCode         報表編號
	 * @param rptItem         報表說明
	 * @param Security        報表機密等級(中文敍述)
	 * @param PageSize        報表尺寸,例A4,A5,LETTER;自訂尺寸(寛,長)(單位:吋),例:8.5,5.5
	 * @param pageOrientation 報表方向,P:直印/L:橫印
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, int date, String brno, String rptCode, String rptItem, String Security,
			String PageSize, String pageOrientation) throws LogicException {

		this.checkParm(date, brno, rptCode, rptItem);

		this.titaVo = titaVo;

		this.date = date;
		this.brno = brno;
		this.rptCode = rptCode;
		this.rptItem = rptItem;
		this.rptSecurity = Security;

		this.rptSize = PageSize.toUpperCase();

		pageOrientation = pageOrientation.toUpperCase();
		if ("P".equals(pageOrientation)) {
			this.pageOrientation = pageOrientation;
		} else {
			this.pageOrientation = "L";
		}

		this.useDefault = false;

		init();

	}

	/**
	 * 
	 * @param titaVo   titaVo
	 * @param date     日期
	 * @param brno     單位
	 * @param rptCode  報表編號
	 * @param rptItem  報表說明
	 * @param Security 報表機密等級(中文敍述)
	 * @throws LogicException LogicException
	 */

	public void open(TitaVo titaVo, int date, String brno, String rptCode, String rptItem, String Security)
			throws LogicException {

		this.checkParm(date, brno, rptCode, rptItem);

		this.titaVo = titaVo;

		this.date = date;
		this.brno = brno;
		this.rptCode = rptCode;
		this.rptItem = rptItem;
		this.rptSecurity = Security;

		this.rptSize = "A4"; // 紙張大小
		this.pageOrientation = "L"; // 紙張方向 P:Portrait Orientation (直印) , L:Landscape Orientation (橫印)

		this.useDefault = false;

		init();

	}

	/**
	 * 
	 * @param titaVo     titaVo
	 * @param date       日期
	 * @param brno       單位
	 * @param rptCode    報表編號
	 * @param rptItem    報表說明
	 * @param Security   報表機密等級(中文敍述)
	 * @param defaultPdf 預設PDF底稿
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, int date, String brno, String rptCode, String rptItem, String Security,
			String defaultPdf) throws LogicException {

		this.checkParm(date, brno, rptCode, rptItem);

		if ("".equals(defaultPdf)) {
			throw new LogicException("EC004", "(MakeReport)預設PDF底稿(defaultPdf)參數必須有值");
		}

		// check defaultpdf
		String filename = pdfFolder + defaultPdf;

		File tempFile = new File(filename);
		if (!tempFile.exists()) {
			throw new LogicException("EC004", "(MakeReport)預設PDF底稿:" + filename + "不存在");
		}

		this.titaVo = titaVo;

		this.date = date;
		this.brno = brno;
		this.rptCode = rptCode;
		this.rptItem = rptItem;
		this.rptSecurity = Security;
		this.defaultPdf = defaultPdf;
		this.useDefault = true;

		init9();

	}

	private void checkParm(int date, String brno, String rptCode, String rptItem) throws LogicException {
		if (date <= 0) {
			throw new LogicException("EC004", "(MakeReport)日期(date)參數必須有值");
		}

		if (brno == null || "".equals(brno)) {
			throw new LogicException("EC004", "(MakeReport)單位(brno)參數必須有值");
		}
		if (rptCode == null || "".equals(rptCode)) {
			throw new LogicException("EC004", "(MakeReport)報表編號(rptCode)參數必須有值");
		}
		if (haveChinese(rptCode)) {
			throw new LogicException("EC004", "(MakeReport)報表編號(rptCode)參數不可有全形字");
		}
		if (rptItem == null || "".equals(rptItem)) {
			throw new LogicException("EC004", "(MakeReport)報表說明(rptItem)參數必須有值");

		}
	}

	// 一般模式
	private void init() {

		this.font = 1;
		this.fontSize = 10;

		this.rptBeginRow = 8;
		this.rptTotalRows = 40;
		this.NowRow = this.rptBeginRow - 1;

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.nowPage = 0;

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 0);
		map.put("paper", this.rptSize.toUpperCase());
		map.put("paper.orientation", this.pageOrientation);
		map.put("font", this.font);
		map.put("font.size", this.fontSize);
		map.put("p", this.rptPassword);
		listMap.add(map);
	}

	// 套form模式
	private void init9() {

		this.font = 1;
		this.fontSize = 10;

		this.rptBeginRow = 8;
		this.rptTotalRows = 40;
		this.NowRow = this.rptBeginRow - 1;

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.nowPage = 1;

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 9);
		map.put("default", this.defaultPdf);
		map.put("font", this.font);
		map.put("font.size", this.fontSize);
		map.put("p", this.rptPassword);
		listMap.add(map);
	}

	/**
	 * 指定位置列印 繪製圖檔
	 * 
	 * @param x        x軸cm
	 * @param y        Y軸cm
	 * @param percent  放大比例 %, 100 表原大小
	 * @param filename 影像檔名
	 */
	public void printImageCm(double x, double y, float percent, String filename) {
		int xx = (int) Math.ceil(x / 2.54 * 72);
		int yy = (int) Math.ceil(y / 2.54 * 72);
		printImage(xx, yy, percent, filename);
	}

	/**
	 * 指定位置列印 繪製圖檔
	 * 
	 * @param x        x軸px
	 * @param y        Y軸px
	 * @param percent  放大比例 %, 100 表原大小
	 * @param filename 影像檔名
	 */
	public void printImage(int x, int y, float percent, String filename) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", "A");
		map.put("x", x);
		map.put("y", y);
		map.put("p", percent);
		map.put("f", filename);
		listMap.add(map);

		this.printCnt++;

	}

	/**
	 * 設定字體及字體大小<br>
	 * 
	 * @param font     字體,1:標楷體 2:細明體 3.微軟正黑體
	 * @param fontSize 字體大小,建議 8,10(預設),12,14
	 */
	public void setFont(int font, int fontSize) {

		this.font = font;
		this.fontSize = fontSize;

		putFont();
	}

	/**
	 * 設定字體<br>
	 * 
	 * @param font 字體,1:標楷體 2:細明體
	 */
	public void setFont(int font) {
		this.font = font;

		putFont();

	}

	/**
	 * 設定字體大小<br>
	 * 
	 * @param fontSize 字體大小,建議 8,10(預設),12,14
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;

		putFont();
	}

	/**
	 * 設定明細起始列<br>
	 * 
	 * @param row 起始列
	 */
	public void setBeginRow(int row) {
		this.rptBeginRow = row;

	}

	/**
	 * 設定明細列印列數<br>
	 * 
	 * @param rows 列數
	 */
	public void setMaxRows(int rows) {
		this.rptTotalRows = rows;
	}

	/**
	 * 預設表頭<br>
	 */
	public void printHeader() {

		// 直接
		if ("P".equals(this.pageOrientation)) {
			printHeaderP();
		} else {
			printHeaderL();
		}

		// 明細起始列(自訂亦必須)
		this.setBeginRow(8);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(35);

	}

	// 預設表頭 - 直印
	private void printHeaderP() {
		this.print(-1, 1, "程式ID：" + this.parentTranCode);
		this.print(-1, 50, "新光人壽保險股份有限公司", "C");
		this.print(-1, 80, "機密等級：" + this.rptSecurity);
		this.print(-2, 1, "報　表：" + this.rptCode);
		this.print(-2, 50, this.rptItem, "C");
		this.print(-2, 80, "日　　期：" + showDate(this.nowDate));
		this.print(-3, 80, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 80, "頁　　次：" + this.nowPage);
		this.print(-6, 50, showRocDate(this.date), "C");

	}

	// 預設表頭 - 橫印
	private void printHeaderL() {
		this.print(-1, 1, "程式ID：" + this.parentTranCode);
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");
		this.print(-1, 120, "機密等級：" + this.rptSecurity);
		this.print(-2, 1, "報　表：" + this.rptCode);
		this.print(-2, 70, this.rptItem, "C");
		this.print(-2, 120, "日　　期：" + showDate(this.nowDate));
		this.print(-3, 120, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 120, "頁　　次：" + this.nowPage);
		this.print(-6, 70, showRocDate(this.date), "C");

	}

	/**
	 * 預設標題<br>
	 * 
	 */

	public void printTitle() {
	}

	/**
	 * 預設表尾<br>
	 * 
	 */
	public void printFooter() {

	}

	/**
	 * 預設續下頁
	 */
	public void printContinueNext() {

	}

	/**
	 * 換新頁<br>
	 * 
	 * @param changeDefault 套印用強迫換頁
	 */

	public void newPage(boolean changeDefault) {
		useDefault = changeDefault;
		newPage();
	}

	/**
	 * 換新頁<br>
	 * 
	 */
	public void newPage() {

		HashMap<String, Object> map = new HashMap<String, Object>();

		if (useDefault) {
			this.nowPage++;
			map.put("type", 1);
			listMap.add(map);
			return;
		}

		// 必須
		this.hfProcess = true;

		if (this.nowPage > 0) {
			this.printContinueNext();
			this.printFooter();
		}

		if (this.nowPage > 0) {

			map.put("type", 1);
			listMap.add(map);
		}

		this.nowPage++;

		this.NowRow = 1;

		this.printHeader();

		// 必須
		this.hfProcess = false;

		this.NowRow = this.rptBeginRow - 1;

		printTitle();

	}

	private void putFont() {

		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("type", 2);
		map.put("font", this.font);
		map.put("size", this.fontSize);

		listMap.add(map);
	}

	/**
	 * 指定列行印字串(左靠)<br>
	 * 
	 * @param row    列
	 * @param column 欄
	 * @param string 字串
	 */
	public void print(int row, int column, String string) {
		// row : 列 < 0 表指定位置 , =0表目前列 , >0表跳行數
		// col : 行
		// string : 列印字串

		toprint(row, column, string, "L"); // 預設左靠
	}

	/**
	 * 指定列行印字串<br>
	 * 
	 * @param row    列
	 * @param column 欄
	 * @param string 字串
	 * @param align  L:左靠 , C:置中 , R:右靠
	 */

	public void print(int row, int column, String string, String align) {
		// row : 列 < 0 表指定位置 , =0表目前列 , >0表跳行數
		// col : 行
		// string : 列印字串
		// align : L:左靠 , C:置中 , R:右靠

		toprint(row, column, string, align);
	}

	private void toprint(int row, int column, String string, String align) {
		// row : 列 < 0 表指定位置 , =0表目前列 , >0表跳行數
		// col : 行
		// string : 列印字串
		// align : L:左靠 , C:置中 , R:右靠

		int prow = printProw(row);

		align = align.toUpperCase();

		if ("L".equals(align) || "C".equals(align) || "R".equals(align)) {

		} else {
			align = "L";
		}

		if (string != null && !"".equals(string)) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("type", 3);
			map.put("row", prow);
			map.put("col", column);
			map.put("txt", string);
			map.put("align", align);
			listMap.add(map);

			this.printCnt++;
		}

	}

	private int printProw(int row) {

		int prow = 0;

		if (row < 0) {
			prow = -row;
			this.NowRow = prow;
		} else if (row == 0) {
			prow = this.NowRow;
		} else {
			this.NowRow += row;
			prow = this.NowRow;
		}

		// 換頁處理,排除
		// 1.表頭(header)及表尾(footer)
		// 2.指定列

		if (hfProcess || row <= 0) {

		} else if (this.nowPage == 0 || this.NowRow > (this.rptBeginRow + this.rptTotalRows - 1)) {
			newPage();
			this.NowRow += row;
			prow = this.NowRow;
		}

		return prow;
	}

	public void printCm(double x, double y, String string) {
		int xx = (int) Math.ceil(x / 2.54 * 72);
		int yy = (int) Math.ceil(y / 2.54 * 72);
		printXY(xx, yy, string, "L");

	}

	public void printCm(double x, double y, String string, String align) {
		int xx = (int) Math.ceil(x / 2.54 * 72);
		int yy = (int) Math.ceil(y / 2.54 * 72);
		printXY(xx, yy, string, align);
	}

	/**
	 * 指定XY軸列印字串(左靠)<br>
	 * 
	 * @param x      x軸
	 * @param y      y軸
	 * @param string 輸出內容
	 */
	public void printXY(int x, int y, String string) {
		printXY(x, y, string, "L");

	}

	/**
	 * 指定XY軸列印字串<br>
	 * 
	 * @param x      x軸
	 * @param y      y軸
	 * @param string 輸出內容
	 * @param align  L:左靠 , C:置中 , R:右靠
	 */
	public void printXY(int x, int y, String string, String align) {
		// row : 列 < 0 表指定位置 , =0表目前列 , >0表跳行數
		// col : 行
		// string : 列印字串
		// align : L:左靠 , C:置中 , R:右靠

		if (!"".equals(string) && string != null) {
			string = string.trim();
		}

		if (!"".equals(string)) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("type", 4);
			map.put("x", x);
			map.put("y", y);
			map.put("txt", string);
			map.put("align", align);
			listMap.add(map);

			this.printCnt++;
		}

	}

	/**
	 * 矩形區間列印字串
	 * 
	 * @param x      x軸cm
	 * @param y      y軸cm
	 * @param width  每列列印半形字數
	 * @param height 每列高度px
	 * @param text   列印字串
	 */
	public void printRectCm(double x, double y, int width, int height, String text) {

		printRectCm(x, y, width, 0, height, text);
	}

	/**
	 * 矩形區間列印字串
	 * 
	 * @param x      x軸cm
	 * @param y      y軸cm
	 * @param width  每列列印半形字數
	 * @param width2 第二行縮排半形字數
	 * @param height 每列高度px
	 * @param text   列印字串
	 */
	public void printRectCm(double x, double y, int width, int width2, int height, String text) {
		int xx = (int) Math.ceil(x / 2.54 * 72);
		int yy = (int) Math.ceil(y / 2.54 * 72);

		printRect(xx, yy, width, width2, height, text);
	}

	/**
	 * 矩形區間列印字串
	 * 
	 * @param x      x軸px
	 * @param y      y軸px
	 * @param width  每列列印半形字數
	 * @param height 每列高度px
	 * @param text   列印字串
	 */
	public void printRect(int x, int y, int width, int height, String text) {
		printRect(x, y, width, 0, height, text);
	}

	/**
	 * 矩形區間列印字串
	 * 
	 * @param x      x軸px
	 * @param y      y軸px
	 * @param width  每列列印半形字數
	 * @param width2 第二行縮排半形字數
	 * @param height 每列高度px
	 * @param text   列印字串
	 */
	public void printRect(int x, int y, int width, int width2, int height, String text) {
		if (!"".equals(text)) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("type", "B");
			map.put("x", x);
			map.put("y", y);
			map.put("w", width);
			map.put("w2", width2);
			map.put("h", height);
			map.put("s", text);
			listMap.add(map);

			this.printCnt++;
		}
	}

	/**
	 * 繪製線條(寬度1點)<br>
	 * 
	 * @param x1    繪製起始X軸
	 * @param y1    繪製起始Y軸
	 * @param x2    繪製結束X軸
	 * @param y2    繪製結束Y軸
	 * @param width 線條寛度點數
	 */
	public void drawLine(int x1, int y1, int x2, int y2, double width) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 5);
		map.put("x1", x1);
		map.put("y1", y1);
		map.put("x2", x2);
		map.put("y2", y2);
		map.put("w", width);
		listMap.add(map);

		this.printCnt++;

	}

	/**
	 * 繪製線條(寬度1點)<br>
	 * 
	 * @param x1 繪製起始X軸
	 * @param y1 繪製起始Y軸
	 * @param x2 繪製結束X軸
	 * @param y2 繪製結束Y軸
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 5);
		map.put("x1", x1);
		map.put("y1", y1);
		map.put("x2", x2);
		map.put("y2", y2);
		map.put("w", 1);
		listMap.add(map);

		this.printCnt++;
	}

	/**
	 * 設定字距<br>
	 * 
	 * @param charSpaces 字距點數
	 */
	public void setCharSpaces(int charSpaces) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 6);
		map.put("x", charSpaces);

		listMap.add(map);

	}

	/**
	 * 
	 * @param lineSpaces 行距點數
	 */
	public void setLineSpaces(int lineSpaces) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 8);
		map.put("y", lineSpaces);

		listMap.add(map);
	}

	/**
	 * 預設底稿套form用,設欄位值
	 * 
	 * @param field 欄位名稱
	 * @param value 欄位值
	 */
	public void setField(String field, String value) {
		if (field == null || "".equals(field) || value == null || "".equals(value))
			return;

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 7);
		map.put("f", field);
		map.put("v", value);
		listMap.add(map);
	}

	/**
	 * 設定密碼，需在open之前 call
	 * 
	 * @param value 值
	 */
	public void setPassword(String value) {

		this.rptPassword = value;
	}

	/**
	 * 結束製表<br>
	 * 
	 * @return long 檔案序號
	 * @throws LogicException LogicException
	 */
	public long close() throws LogicException {

		if (!useDefault && printCnt == 0) {
			this.print(1, 1, "無資料!!!");
		}

		if (this.nowPage > 0 && !useDefault) {
			this.printFooter();
		}

		TxFile tTxFile = new TxFile();

		// 寫Txfile時需寫回onlineDB,但交易用的titaVo應維持原指向的DB
		TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();

		tmpTitaVo.putParam(ContentName.dataBase, ContentName.onLine);

		// 檢查是否需核核
		CdReport tCdReport = cdReportService.findById(this.rptCode, tmpTitaVo);
		if (tCdReport == null) {
			tTxFile.setSignCode("0");
		} else {
			tTxFile.setSignCode(String.valueOf(tCdReport.getSignCode()));

			// 2021-1-5 增加判斷 SignCode == 1 才印
			if (tCdReport.getSignCode() == 1 && !useDefault) {
				if ("P".equals(pageOrientation)) {
					this.print(2, 40, signOff, "C");
				} else {
					this.print(2, 70, signOff, "C");
				}
			}
		}

		if (formMode) {
			tTxFile.setFileType(6); // 固定1:PDF
		} else {
			tTxFile.setFileType(1); // 固定1:PDF
		}

		tTxFile.setFileFormat(1);
		tTxFile.setFileCode(this.rptCode);
		tTxFile.setFileItem(this.rptItem);
		tTxFile.setFileOutput(this.rptCode);
		tTxFile.setBrNo(this.brno);
		tTxFile.setFileDate(this.date);
		try {
			ObjectMapper mapper = new ObjectMapper();
			tTxFile.setFileData(mapper.writeValueAsString(listMap));
		} catch (IOException e) {
			throw new LogicException("EC009", "(MakeReport)資料格式 " + e.getMessage());
		}

		try {
			tTxFile = txFileService.insert(tTxFile, tmpTitaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "EC002", "(MakeReport)輸出檔(TxFile):" + e.getErrorMsg());
		}
		return tTxFile.getFileNo();
	}

	private BaseFont setBaseFont(String type) throws IOException, DocumentException {
		// 標楷體
		String fontname = fontFolder + "kaiu.ttf";
		// 細明體
		if ("2".equals(type)) {
			fontname = fontFolder + "mingliu.ttc,0";
			// 微軟正黑體
		} else if ("3".equals(type)) {
			fontname = fontFolder + "msjh.ttc,0";
		}
		return BaseFont.createFont(fontname, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); // 標楷體
	}

	/**
	 * 產製pdf檔<br>
	 * 
	 * @param pdfno 報表序號
	 * @throws LogicException LogicException
	 */
	public void toPdf(long pdfno) throws LogicException {
		doToPdf(pdfno, "");
	}

	/**
	 * 產製pdf檔<br>
	 * 
	 * @param pdfno    報表序號
	 * @param filename 指定輸出檔名
	 * @throws LogicException LogicException
	 */
	public void toPdf(long pdfno, String filename) throws LogicException {
		doToPdf(pdfno, filename);
	}

	private String rptTlrNo = "";
	private Timestamp rptCreateDate = null;

	@SuppressWarnings("unchecked")
	private void doToPdf(long pdfno, String filename) throws LogicException {

		this.info("MakeReport.toPdf = " + pdfno);

		TxFile tTxFile = txFileService.findById(pdfno);

		if (tTxFile == null) {
			throw new LogicException(titaVo, "EC001", "(MakeReport)輸出檔(TxFile)序號:" + pdfno);
		}

		if (tTxFile.getFileType() != 1) {
			throw new LogicException(titaVo, "E0015", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + "，不為PDF格式");
		}

		if (tTxFile.getFileCode() != null) {
			this.rptCode = tTxFile.getFileCode();
		}

		rptTlrNo = tTxFile.getCreateEmpNo();

		rptCreateDate = tTxFile.getCreateDate();

		this.info("MakeRepor doToPdf rptTlrNo = " + rptTlrNo);

		try {
			this.listMap = new ObjectMapper().readValue(tTxFile.getFileData(), ArrayList.class);
		} catch (IOException e) {
			throw new LogicException("EC009", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + ",資料格式 " + e.getMessage());
		}

		String outfile = outputFolder + tTxFile.getFileOutput() + ".pdf";

		if (!"".equals(filename)) {
			outfile = outputFolder + filename + ".pdf";
		}

		this.info("MakeReport.toPdf.filename =" + outfile);

		if (this.rptCode == null) {
			this.info("makeReport rptCode is null");
		}

		// 檢查是否需浮水印
		CdReport tCdReport = cdReportService.findById(this.rptCode);

		watermarkFlag = false;

		if (tCdReport != null && tCdReport.getWatermarkFlag() == 1) {
			watermarkFlag = true;
		}

		// 先刪除舊檔
		File file = new File(outfile);

		try {
			Files.delete(file.toPath());
		} catch (IOException e) {
			this.info("MakeReport Files.delete error =" + e.getMessage());
		}

		try {

			// 輸出檔名
			FileOutputStream fos = null;

			// 建立一個Document物件，並設定頁面大小及左、右、上、下的邊界，rotate()橫印
			Document document = null;
			// 設定要輸出的Stream
			PdfWriter writer = null;

			// 套底稿用
			PdfStamper stamper = null;
			AcroFields fields = null;
			PdfReader reader = null;
			ByteArrayOutputStream baos = null;
			PdfCopy copy = null;
			String defaultname = null;

			// 讀取頁的長寛
			Rectangle page = null;

			// 設定要輸出的Stream
			PdfContentByte cb = null;

			int frameX = 5;
			int frameY = -5;

			// 預設字型
			BaseFont baseFont = setBaseFont("1");
			int fontsize = 10;
			int charSpaces = 1;
			int lineSpaces = 0;
			int fontwidth = fontsize / 2 + charSpaces;
			int fonthigh = fontsize + lineSpaces + 2;

			for (HashMap<String, Object> map : this.listMap) {

				String type = map.get("type").toString();

				if ("0".equals(type)) {
					this.useDefault = false;

					// 輸出檔名
					fos = new FileOutputStream(new File(outfile));
					// 報表啟始
					String papersize = map.get("paper").toString();

//					int i = papersize.indexOf(',');
					String[] ss = papersize.split(",");

					Rectangle pagesize = PageSize.A4;

					// this.info("B pagesize.getWidth() = " + pagesize.getWidth());
					// this.info("B pagesize.getHeight() = " + pagesize.getHeight());

					if ("LETTER".equals(papersize)) {
						pagesize = PageSize.LETTER;
					} else if ("A5".equals(papersize)) {
						pagesize = PageSize.A5;
//自訂尺寸,以inch為單位						
//					} else if (i != -1) {
					} else if (ss.length == 2) {

//						String s1 = papersize.substring(0, 2);
//						String s2 = papersize.substring(i+1);

						long n1 = (long) Math.ceil((Double.parseDouble(ss[0]) * 72));
						long n2 = (long) Math.ceil((Double.parseDouble(ss[1]) * 72));

						// this.info("compute width / height = " + n1 + "/" + n2);
						pagesize = new Rectangle(n1, n2);

						// this.info("MakeReport pagesize =" + papersize);
						// this.info("C pagesize.getWidth() = " + pagesize.getWidth());
						// this.info("C pagesize.getHeight() = " + pagesize.getHeight());
					}

					// this.info("A pagesize.getWidth() = " + pagesize.getWidth());
					// this.info("A pagesize.getHeight() = " + pagesize.getHeight());

					String paperorientaton = map.get("paper.orientation").toString();

					// 建立一個Document物件，並設定頁面大小及左、右、上、下的邊界，rotate()橫印
					if ("P".equals(paperorientaton)) {
//						document = new Document(PageSize.A4, 0, 0, 0, 0);
						document = new Document(pagesize, 0, 0, 0, 0);
					} else {
//						document = new Document(PageSize.A4.rotate(), 0, 0, 0, 0);
						document = new Document(pagesize.rotate(), 0, 0, 0, 0);
					}

					// 設定要輸出的Stream
					writer = PdfWriter.getInstance(document, fos);

					// 加密
					String password = map.get("p") == null ? "" : map.get("p").toString();

					if (!password.isEmpty()) {

						byte[] p = password.getBytes();

						String r = "" + (int) (Math.random() * 10e8);

						writer.setEncryption(p, r.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);

					}

					this.xPoints = writer.getPageSize().getWidth();
					this.yPoints = writer.getPageSize().getHeight();

					document.open();

					// 讀取頁的長寛
					page = writer.getPageSize();

					// 設定要輸出的Stream

					cb = writer.getDirectContent();
//
					String font = map.get("font").toString();
					baseFont = setBaseFont(font);
					fontsize = Integer.valueOf(map.get("font.size").toString());
					fontwidth = fontsize / 2 + charSpaces;
					fonthigh = fontsize + lineSpaces + 2;

					if (watermarkFlag) {
						this.setWatermark(writer.getDirectContentUnder(), document);
					}
				} else if ("9".equals(type)) {
					this.useDefault = true;

					this.defaultPdf = map.get("default").toString();

					defaultname = pdfFolder + this.defaultPdf;

					File tempFile = new File(defaultname);
					if (!tempFile.exists()) {
						throw new LogicException("EC004", "(MakeReport)預設PDF底稿:" + defaultname + "不存在");
					}

					document = new Document();
					copy = new PdfSmartCopy(document, new FileOutputStream(outfile));

					// 加密
					String password = map.get("p") == null ? "" : map.get("p").toString();

					if (password != null && !password.isEmpty()) {
						byte[] p = password.getBytes();

						String r = "" + (int) (Math.random() * 10e8);

						copy.setEncryption(p, r.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
					}

					document.open();

					// 讀取頁的長寛
					page = copy.getPageSize();

					// 設定要輸出的Stream

					baos = new ByteArrayOutputStream();
					reader = new PdfReader(defaultname);
					stamper = new PdfStamper(reader, baos);
					fields = stamper.getAcroFields();
					cb = stamper.getOverContent(1);
//
					String font = map.get("font").toString();
					baseFont = setBaseFont(font);
					fontsize = Integer.valueOf(map.get("font.size").toString());
					fontwidth = fontsize / 2 + charSpaces;
					fonthigh = fontsize + lineSpaces + 2;

					this.nowPage = 1;
					this.info("open = " + this.nowPage);

					this.xPoints = stamper.getWriter().getPageSize().getWidth();
					this.yPoints = stamper.getWriter().getPageSize().getHeight();

				} else if ("1".equals(type)) {
					// 新頁
					if (this.useDefault) {
						if (this.nowPage > 0) {
							stamper.setFormFlattening(true);
							stamper.close();
							reader.close();
							reader = new PdfReader(baos.toByteArray());
							copy.addDocument(reader);
//							reader.close();
						}
						this.nowPage++;
						// new
						baos = new ByteArrayOutputStream();
						reader = new PdfReader(defaultname);
						stamper = new PdfStamper(reader, baos);
						fields = stamper.getAcroFields();
						cb = stamper.getOverContent(this.nowPage - 1);
					} else {
						document.newPage();
					}

					if (watermarkFlag) {
						this.setWatermark(writer.getDirectContentUnder(), document);
					}
				} else if ("2".equals(type)) {
					// 設定字型
					String font = map.get("font").toString();
					baseFont = setBaseFont(font);
					fontsize = Integer.parseInt(map.get("size").toString());
					fontwidth = fontsize / 2 + charSpaces;
					fonthigh = fontsize + lineSpaces + 2;
				} else if ("3".equals(type)) {
					// 指定行列,列印字串
					int row = Integer.parseInt(map.get("row").toString());
					int col = Integer.parseInt(map.get("col").toString());
					String txt = map.get("txt").toString();

					// 簽核
					if (signOff.equals(txt)) {
						String tlrna = "";
						String supna = "";

						if (!"".equals(tTxFile.getTlrNo())) {
							TxTeller tTxTeller = txTellerService.findById(tTxFile.getTlrNo());

							if (tTxTeller != null) {
								tlrna = tTxTeller.getTlrItem();
							}
						}
						if (!"".equals(tTxFile.getSupNo())) {
							TxTeller tTxTeller = txTellerService.findById(tTxFile.getTlrNo());

							if (tTxTeller != null) {
								supna = tTxTeller.getTlrItem();
							}
						}
						txt = "經辦：" + tlrna + "                  主管：" + supna;
					}

					String align = map.get("align").toString();
					int x = (col - 1) * fontwidth;
					int y = (int) page.getHeight() - (row * fonthigh);
					cb.beginText();

					cb.setFontAndSize(baseFont, fontsize);
					cb.setCharacterSpacing(charSpaces);
					if ("L".equals(align)) {
						cb.showTextAligned(PdfContentByte.ALIGN_LEFT, txt, frameX + x, frameY + y, 0);
					} else if ("C".equals(align)) {
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, txt, frameX + x, frameY + y, 0);
					} else if ("R".equals(align)) {
						cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, txt, frameX + x, frameY + y, 0);

					}
					cb.endText();

					// this.info("lastrow = " + lastrow);
				} else if ("4".equals(type)) {
					// 指定XY軸列印字串
					int x = Integer.parseInt(map.get("x").toString());
					int y = Integer.parseInt(map.get("y").toString());

					int yy = (int) this.yPoints - y;

					String txt = map.get("txt").toString();
					String align = map.get("align").toString();
					cb.beginText();
					cb.setFontAndSize(baseFont, fontsize);
					cb.setCharacterSpacing(charSpaces);
					if ("L".equals(align)) {
						cb.showTextAligned(PdfContentByte.ALIGN_LEFT, txt, frameX + x, frameY + yy, 0);
					} else if ("C".equals(align)) {
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, txt, frameX + x, frameY + yy, 0);
					} else if ("R".equals(align)) {
						cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, txt, frameX + x, frameY + yy, 0);

					}
					cb.endText();
				} else if ("5".equals(type)) {
					// 畫線
					int x1 = Integer.parseInt(map.get("x1").toString());
					int y1 = Integer.parseInt(map.get("y1").toString());
					int x2 = Integer.parseInt(map.get("x2").toString());
					int y2 = Integer.parseInt(map.get("y2").toString());
					double w = Double.parseDouble(map.get("w").toString());

//					int py1 = (int) page.getHeight() - y1;
//					int py2 = (int) page.getHeight() - y2;

					int py1 = (int) this.yPoints - y1;
					int py2 = (int) this.yPoints - y2;

					cb.saveState();
					// 設定線條寬度
//					cb.setLineWidth(1f);
					cb.setLineWidth(w);
					// 设置画线的颜色
					cb.setColorStroke(BaseColor.BLACK);
					// 绘制起点坐标
					cb.moveTo(x1, py1);
					// 绘制终点坐标
					cb.lineTo(x2, py2);
					// 确认直线的绘制
					cb.stroke();
					cb.restoreState();

				} else if ("6".equals(type)) {
					// 設定字距

					String xx = map.get("x").toString();

					if (!"".equals(xx)) {
						int x = Integer.parseInt(xx);
						charSpaces = x;
						fontwidth = fontsize / 2 + charSpaces;
					}

				} else if ("7".equals(type)) {
					if (!this.useDefault) {
						break;
					}
					// 預設底稿套form用,設欄位值

					String field = map.get("f").toString();
					String value = map.get("v").toString();

					fields.setFieldProperty(field, "textfont", baseFont, null); // 設定字型
					fields.setField(field, value);
				} else if ("8".equals(type)) {
					// 設定行距

					String yy = map.get("y").toString();

					if (!"".equals(yy)) {
						int y = Integer.parseInt(yy);
						lineSpaces = y;
						fonthigh = fontsize + lineSpaces + 2;
					}
				} else if ("A".equals(type)) {
					// 列印圖片

					String fna = map.get("f").toString();
					int x = Integer.parseInt(map.get("x").toString());
					int y = Integer.parseInt(map.get("y").toString());
					int yy = (int) this.yPoints - y;
					float percent = Float.parseFloat(map.get("p").toString());

					String imagename = ResourceFolde + fna;

					this.info("MakeReport imagename = " + imagename);

					File tempFile = new File(imagename);
					if (!tempFile.exists()) {
						continue;
					}

					this.info("MakeReport percent = " + percent);

					Image image = Image.getInstance(imagename);

					this.info("MakeReport image b = " + image.getWidth() + "/" + image.getHeight());

					double imageH = Math.ceil(image.getHeight());

					if (percent != 0) {
						image.scalePercent(percent);
						imageH = Math.ceil(image.getHeight() * percent / 100);
					}

					this.info("MakeReport image a = " + image.getWidth() + "/" + image.getHeight());

					yy -= imageH;

					// 新增圖片
					image.setAbsolutePosition(x, yy);
					cb.addImage(image);

				} else if ("B".equals(type)) {
					// 指定區間列印字串
					int x = Integer.parseInt(map.get("x").toString());
					int y = Integer.parseInt(map.get("y").toString());
					int w = Integer.parseInt(map.get("w").toString());
					int w2 = Integer.parseInt(map.get("w2").toString());
					int h = Integer.parseInt(map.get("h").toString());
					int yy = (int) this.yPoints - y;
					String s = map.get("s").toString();

					String ps = "";
					int pw = 0;

					String prefix = "";
					for (int i = 0; i < w2; i++) {
						prefix += " ";
					}

					for (int i = 0; i < s.length(); i++) {
						String ss = s.substring(i, i + 1);

						ps += ss;

						int ww = 1;
						if (haveChinese(ss)) {
							ww = 2;
						}
						pw += ww;
						if (pw >= w) {
							cb.beginText();

							cb.setFontAndSize(baseFont, fontsize);
							cb.setCharacterSpacing(charSpaces);
							cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ps, frameX + x, frameY + yy, 0);
							cb.endText();

							ps = prefix;
							pw = w2;
							yy -= h;
						}
					}
					if (pw > 0) {
						cb.beginText();
						this.info("MakeReport basefont = " + BaseFont.TIMES_BOLD);
						cb.setFontAndSize(baseFont, fontsize);
						cb.setCharacterSpacing(charSpaces);
						cb.showTextAligned(PdfContentByte.ALIGN_LEFT, ps, frameX + x, frameY + yy, 0);

						cb.endText();
					}
				}
			}

			if (this.useDefault) {
				stamper.setFormFlattening(true);
				stamper.close();
				reader.close();
				reader = new PdfReader(baos.toByteArray());
				copy.addDocument(reader);
				reader.close();

				document.close();
			} else {
				document.close();
				fos.close();
			}
		} catch (

		IOException e) {
			throw new LogicException("EC009", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + ",輸出PDF " + e.getMessage());
		} catch (DocumentException e) {
			throw new LogicException("EC009", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + ",資料PDF " + e.getMessage());
		}

	}

	/**
	 * 浮水印
	 * 
	 * @param cb       PdfContentByte
	 * @param document Document
	 * @throws IOException       IOException
	 * @throws DocumentException DocumentException
	 */
	private void setWatermark(PdfContentByte cb, Document document) throws IOException, DocumentException {

		PdfGState graphicState = new PdfGState();
		graphicState.setFillOpacity(0.7f);
		graphicState.setStrokeOpacity(1f);

		BaseFont tmpBaseFont = this.setBaseFont("1");

		String watermark = "";

		watermark += rptTlrNo;
		watermark += " ";

		CdEmp tCdEmp = cdEmpService.findById(rptTlrNo);

		String empNm = "";
		if (tCdEmp != null) {
			empNm = tCdEmp.getFullname();
		}

		this.info("MakeReport setWatermark empNm = " + empNm);

		watermark += empNm;
		watermark += " ";
		String rptDate = new SimpleDateFormat("yyyyMMdd").format(rptCreateDate);
		String rptTime = new SimpleDateFormat("HHmmss").format(rptCreateDate);
		watermark += this.showRocDate(rptDate, 2);
		watermark += " ";
		watermark += this.showTime(rptTime);

		cb.setGState(graphicState);
		cb.beginText();
		cb.setFontAndSize(tmpBaseFont, 12);
		cb.setColorFill(BaseColor.LIGHT_GRAY);

		float widthMax = document.getPageSize().getWidth();
		float heightMax = document.getPageSize().getHeight();

		// this.info("widthMax = " + widthMax);
		// this.info("heightMax = " + heightMax);

		for (float w = 0; w < widthMax + 150f; w += 150f) {
			// this.info("w = " + w);
			for (float h = 0; h < heightMax + 80f; h += 80f) {
				// this.info("h = " + h);
				cb.showTextAligned(Element.ALIGN_CENTER, watermark, w, h, 15f);
			}
		}
		cb.endText();
	}

	/**
	 * 顯示民國年(樣式:yyy/mm/dd)
	 * 
	 * @param date 民國年yyymmdd
	 * @return 民國年yyy/mm/dd
	 */
	public String showDate(String date) {
		if (date == null || date.isEmpty()) {
			return "";
		}
		int ceDate = Integer.valueOf(date) + 19110000;
		return showRocDate(ceDate, 1);
	}

	/**
	 * 顯示時間(樣式:hh:mm:ss)
	 * 
	 * @param time 時間hhmmss
	 * @return 時間hh:mm:ss
	 */
	public String showTime(String time) {
		return time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);
	}

	/**
	 * 顯示民國年(預設樣式:xxx 年 xx 月 xx 日)
	 * 
	 * @param date 西曆日期
	 * @return 中曆日期
	 */
	public String showRocDate(int date) {
		return showRocDate(date, 0);
	}

	/**
	 * 顯示民國年<BR>
	 * <BR>
	 * type = 0: yyy 年 mm 月 dd 日<BR>
	 * type = 1: yyy/mm/dd<BR>
	 * type = 2: yyy-mm-dd<BR>
	 * type = 3: yyymmdd<BR>
	 * type = 4: （中文） yyy 年 mm 月<BR>
	 * type = 5: yyy 年 mm 月<BR>
	 * type = 6: yyy.mm.dd<BR>
	 * 
	 * @param date 西曆日期
	 * @param type 樣式<BR>
	 * @return 中曆日期
	 */
	public String showRocDate(String date, int type) {
		if (date == null || date.isEmpty()) {
			return "";
		}
		return showRocDate(Integer.parseInt(date), type);
	}

	/**
	 * 顯示西元年<BR>
	 * 
	 * @param date 西曆日期
	 * @param type 樣式<BR>
	 *             type = 0: yyyy/mm/dd<BR>
	 *             type = 1: mm/dd/yy<BR>
	 *             type = 2: yyyymmdd<BR>
	 * @return 西曆日期
	 */
	public String showBcDate(String date, int type) {
		if (date == null || date.isEmpty()) {
			return "";
		}
		int iDate = Integer.parseInt(date);
		return showBcDate(iDate, type);
	}

	/**
	 * 顯示西元年<BR>
	 * 
	 * @param date 西曆日期
	 * @param type 樣式<BR>
	 *             type = 0: yyyy/mm/dd<BR>
	 *             type = 1: mm/dd/yy<BR>
	 *             type = 2: yyyymmdd<BR>
	 * @return 西曆日期
	 */
	public String showBcDate(int date, int type) {

		if (date < 10101) {
			return "";
		}

		int bcdate = date;

		if (bcdate < 19110000) {
			bcdate += 19110000;
		}

		String xBcDate = String.valueOf(bcdate);

		String year = "";
		String month = "";
		String day = "";

		if (xBcDate.length() >= 8) {
			year = xBcDate.substring(0, 4);
			month = xBcDate.substring(4, 6);
			day = xBcDate.substring(6, 8);
		} else if (xBcDate.length() == 7) {
			year = xBcDate.substring(0, 3);
			month = xBcDate.substring(3, 5);
			day = xBcDate.substring(5, 7);
		} else if (xBcDate.length() == 6) {
			year = xBcDate.substring(0, 2);
			month = xBcDate.substring(2, 4);
			day = xBcDate.substring(4, 6);
		} else if (xBcDate.length() == 5) {
			year = xBcDate.substring(0, 1);
			month = xBcDate.substring(1, 3);
			day = xBcDate.substring(3, 5);
		}

		String result = "";
		switch (type) {
		case 0:
			result = year + "/" + month + "/" + day;
			break;
		case 1:
			result = month + "/" + day + "/" + year.substring(2, 4);
			break;
		case 2:
			result = year + month + day;
			break;
		default:
			result = year + month + day;
			break;
		}

		return result;
	}

	/**
	 * 顯示民國年<BR>
	 * <BR>
	 * type = 0: yyy 年 mm 月 dd 日<BR>
	 * type = 1: yyy/mm/dd<BR>
	 * type = 2: yyy-mm-dd<BR>
	 * type = 3: yyymmdd<BR>
	 * type = 4: （中文） yyy 年 mm 月<BR>
	 * type = 5: yyy 年 mm 月<BR>
	 * type = 6: yyy.mm.dd<BR>
	 * 
	 * @param date 西曆日期
	 * @param type 樣式<BR>
	 * @return 中曆日期
	 */
	public String showRocDate(int date, int type) {

		if (date <= 10101) {
			return "";
		}

		int rocdate = date;

		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);

		String rocYear = "";
		String rocMonth = "";
		String rocDay = "";

		if (rocdatex.length() >= 7) {
			rocYear = rocdatex.substring(0, 3);
			rocMonth = rocdatex.substring(3, 5);
			rocDay = rocdatex.substring(5, 7);
		} else if (rocdatex.length() == 6) {
			rocYear = rocdatex.substring(0, 2);
			rocMonth = rocdatex.substring(2, 4);
			rocDay = rocdatex.substring(4, 6);
		} else if (rocdatex.length() == 5) {
			rocYear = rocdatex.substring(0, 1);
			rocMonth = rocdatex.substring(1, 3);
			rocDay = rocdatex.substring(3, 5);
		}

		String result = "";

		switch (type) {
		case 0:
			result = rocYear + "年" + rocMonth + "月" + rocDay + "日";
			break;
		case 1:
			result = rocYear + "/" + rocMonth + "/" + rocDay;
			break;
		case 2:
			result = rocYear + "-" + rocMonth + "-" + rocDay;
			break;
		case 3:
			result = rocYear + rocMonth + rocDay;
			break;
		case 4:
			// 2020-12-29 Mata增加 取得中文年月
			char[] cc = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' };
			char[] dd = { '百', '拾' };
			char[] ff = { ' ', '一', '二', '三', '四', '五', '六', '七', '八', '九' };
			char[] gg = { ' ', '十' };
			String bb = "";
			String aa = "";
			int ee;
			int zz;
			int zero = 0;
			int s = 0;

			for (int i = 0; i < rocYear.length(); i++) {
				ee = Character.getNumericValue(rocYear.charAt(i));
				if (ee == 0) {
					zero++;
				} else {
					zero = 0;
				}
				if (ee == 0 && zero > 1) {
					;
				} else if (ee == 0 && i == rocYear.length() - 1) {
					;
				} else {
					bb = bb + cc[ee];
					if (s < 1) {
						if (bb.length() != 0) {
							bb += dd[0];
						}
					}
					if (ee != 0) {
						if (s != 0 && s != 2) {
							if (bb.length() != 0) {
								bb += dd[1];
							}
						}
					}
					s++;
				}
			}

			for (int i = 0; i < rocMonth.length(); i++) {
				zz = Character.getNumericValue(rocMonth.charAt(i));
				if (zz == 0) {
					zero++;
				} else {
					zero = 0;
				}

				if (zz == 0 && zero > 1) {
					;
				} else if (zz == 0 && i == rocMonth.length() - 1) {
					;
				} else {
					aa = aa + ff[zz];

					if (s < 1) {
						if (aa.length() != 0) {
							if (aa.length() != -1) {
								aa += gg[0];
							}
						}
					}
					if (zz != 0) {
						if (s != 0 && s != 2 && s != 4) {
							if (aa.length() != 0) {
								aa += gg[1];
							}
						}
					}
					s++;
				}
			}

			result = bb + "年" + aa + "月份";
			break;
		case 5:
			result = rocYear + "年" + rocMonth + "月";
			break;
		case 6:
			result = rocYear + "." + rocMonth + "." + rocDay;
			break;
		default:
			result = rocYear + rocMonth + rocDay;
			break;
		}

		return result;

	}

	// 衡修改

	/**
	 * 取製表日期<br>
	 * 
	 * @return int 西元製表日期
	 */
	public int getReportDate() {
		return this.date;
	}

	/**
	 * 取製表中文製表日期<br>
	 * 
	 * @param date 日期
	 * @return String 中文製表日期
	 */
	public String getshowRocDate(int date) {
		return this.showRocDate(date);
	}

	/**
	 * 取製表頁次<br>
	 * 
	 * @return int 頁次
	 */
	public int getNowPage() {
		return this.nowPage;
	}

	@Override
	public void exec() throws LogicException {
		// override this

	}

	// 為了 formatAmt() 預先建好的單位 prefabs
	// 利用 static block 做初始化, 確保 formatAmtTemplates 只會創建一次
	// unmodifiableMap 讓此 map 變成唯讀狀態

	// key 是次方數
	// value 是 prefab

	private static final Map<Integer, BigDecimal> formatAmtTemplates;
	static {
		HashMap<Integer, BigDecimal> m = new HashMap<Integer, BigDecimal>();
		m.put(1, BigDecimal.TEN); // 十
		m.put(2, new BigDecimal(100)); // 百
		m.put(3, new BigDecimal(1000)); // 千
		m.put(4, new BigDecimal(10000)); // 萬
		m.put(5, new BigDecimal(100000)); // 十萬
		m.put(6, new BigDecimal(1000000)); // 百萬
		m.put(7, new BigDecimal(10000000)); // 千萬
		m.put(8, new BigDecimal(100000000)); // 億
		formatAmtTemplates = Collections.unmodifiableMap(m);
	};

	/**
	 * @param amt     金額
	 * @param n       四捨五入至第n位
	 * @param unitPow 每多少元為單位之次方數（如單位為千元，輸入3）
	 * @return String 具撇節並已除好的金額格式
	 */
	public String formatAmt(BigDecimal amt, int n, int unitPow) {
		if (unitPow <= 1) {
			// do nothing
		} else if (formatAmtTemplates.containsKey(unitPow)) {
			amt = computeDivide(amt, formatAmtTemplates.get(unitPow), n);
		} else {
			// Math.Pow(a,b) -> a 的 b 次方
			amt = computeDivide(amt, new BigDecimal(Math.pow(10, unitPow)), n);
		}

		return formatAmt(amt, n);
	}

	/**
	 * @param amt     金額
	 * @param n       四捨五入至第n位
	 * @param unitPow 每多少元為單位之次方數（如單位為千元，輸入3）
	 * @return String 具撇節並已除好的金額格式
	 */
	public String formatAmt(String amt, int n, int unitPow) {
		return formatAmt(getBigDecimal(amt), n, unitPow);
	}

	/**
	 * @param amt 金額
	 * @param n   四捨五入至第n位
	 * @return String 具撇節的金額格式
	 */
	public String formatAmt(String amt, int n) {

		BigDecimal tmpAmt = BigDecimal.ZERO;

		if (amt == null || amt.isEmpty()) {
			this.warn("formatAmt input amt(String) is null or empty");
		} else {
			try {
				tmpAmt = new BigDecimal(amt);
			} catch (NumberFormatException e) {
				this.error("formatAmt input amt:\"" + amt + "\" parse to BigDecimal has NumberFormatException.");
				tmpAmt = BigDecimal.ZERO;
			}
		}

		return formatAmt(tmpAmt, n);
	}

	/**
	 * @param amt 金額
	 * @param n   四捨五入至第n位
	 * @return String 具撇節的金額格式
	 */
	public String formatAmt(BigDecimal amt, int n) {

		amt = amt == null ? BigDecimal.ZERO : amt;

		String result = "";

		String sAmt = amt.setScale(n, RoundingMode.HALF_UP).toString();

		String dec = "";

		// 若有保留小數位數 先擷取小數點及小數點後數字
		// 拆成兩段,僅有小數點前的數值需要加撇節
		if (n > 0) {
			int point = sAmt.indexOf(".");

			dec = sAmt.substring(point);

			sAmt = sAmt.substring(0, point);
		}

		String sign = "";

		// 負數時先把負號拔掉
		if (amt.compareTo(BigDecimal.ZERO) < 0) {
			sign = "-";
			sAmt = sAmt.substring(1);
		}

		// 取得整數總長
		int amtLength = sAmt.length();

		int remainder = amtLength % 3;

		for (int i = 1; i <= amtLength; i++) {
			result += sAmt.substring(i - 1, i);
			if ((i == remainder || (i - remainder) % 3 == 0) && i != amtLength) {
				result += ",";
			}
		}

		result += dec;

		// 負數時把負號組回
		if (amt.compareTo(BigDecimal.ZERO) < 0) {
			result = sign + result;
		}

		return result;
	}

	/**
	 * 傳入字串,回傳BigDecimal,無法轉換為BigDecimal時給零
	 * 
	 * @param inputString 傳入字串
	 * @return BigDecimal
	 */
	public BigDecimal getBigDecimal(String inputString) {
		BigDecimal result = BigDecimal.ZERO;

		if (inputString == null || inputString.isEmpty()) {
			this.warn("getBigDecimal inputString is null or empty");
		} else {
			try {
				result = new BigDecimal(inputString);
			} catch (NumberFormatException e) {
				this.error("getBigDecimal inputString : \"" + inputString
						+ "\" parse to BigDecimal has NumberFormatException.");
				result = BigDecimal.ZERO;
			}
		}
		return result;
	}

	/**
	 * 傳入double,回傳BigDecimal,無法轉換為BigDecimal時給零
	 * 
	 * @param inputdouble 傳入字串
	 * @return BigDecimal
	 */
	public BigDecimal getBigDecimal(double inputdouble) {
		BigDecimal result = BigDecimal.ZERO;

		try {
			result = BigDecimal.valueOf(inputdouble);
		} catch (NumberFormatException e) {
			this.error("getBigDecimal inputdouble : \"" + inputdouble
					+ "\" parse to BigDecimal has NumberFormatException.");
			result = BigDecimal.ZERO;
		}
		return result;
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

	/**
	 * 除法-四捨五入<br>
	 * 分母為0時回傳0
	 * 
	 * @param dividend 被除數(分子)
	 * @param divisor  除數(分母)
	 * @param n        四捨五入至小數點後第n位
	 * @return result 結果值
	 */
	public BigDecimal computeDivide(BigDecimal dividend, BigDecimal divisor, int n) {

		BigDecimal result = BigDecimal.ZERO;

		// 除數(分母)大於零時才運算
		if (divisor.compareTo(BigDecimal.ZERO) > 0) {
			result = dividend.divide(divisor, n, RoundingMode.HALF_UP);
		}

		return result;
	}

	/**
	 * 金額轉中文大寫<br>
	 * 例如:<br>
	 * 傳入 new BigDecimal("1234567890")<br>
	 * 回傳 壹拾貳億參仟肆佰伍拾陸萬柒仟捌佰玖拾<br>
	 * <br>
	 * ps.<br>
	 * 1.傳入值會被四捨五入至個位數<br>
	 * 2.傳入值小於零時,回傳值的第一個字為"負"<br>
	 * 3.最大處理數值為九千九百九十九兆,超過時拋錯<br>
	 * 
	 * @param amt 金額
	 * @return String 中文大寫金額
	 * @throws LogicException EC009 金額轉中文大寫時超過最大處理數值
	 */
	public String convertAmtToChinese(BigDecimal amt) throws LogicException {

		String result = "";

		// 將傳入值四捨五入至個位數
		amt = amt.setScale(0, RoundingMode.HALF_UP);

		// 若傳入值小於零
		if (amt.compareTo(BigDecimal.ZERO) < 0) {
			result = "負";
			amt = amt.abs(); // 取絕對值
		}

		// 金額轉字串
		String sAmt = amt.toString();

		// 最大處理數值 九千九百九十九兆...
		if (amt.compareTo(new BigDecimal("999999999999999")) > 0) {
			throw new LogicException("EC009", "(MakeReport)金額轉中文大寫時超過最大處理數值,傳入金額為:" + sAmt);
		}

		String[] chineseNumber = new String[] { "零", "壹", "貳", "參", "肆", "伍", "陸", "柒", "捌", "玖" }; // 漢字的數字
		String[] chineseBasicUnit = new String[] { "", "拾", "佰", "仟" }; // 基本單位
		String[] chineseAdvanceUnit = new String[] { "", "萬", "億", "兆" }; // 對應整數部分擴充套件單位

		// 若金額為零直接回傳零
		if (amt.compareTo(BigDecimal.ZERO) == 0) {
			result = chineseNumber[0];
			return result;
		}

		// 零的計數器
		int zeroCount = 0;

		// 金額轉字串後的長度
		int amtLength = sAmt.length();

		for (int i = 0; i < amtLength; i++) {

			// 目前處理的值
			String num = sAmt.substring(i, i + 1);

			// 目前處理的位數
			int digit = amtLength - i - 1;

			int advanceDigit = digit / 4;

			int basicDigit = digit % 4;

			if (num.equals("0")) {

				zeroCount++;

			} else {

				if (zeroCount > 0) {
					// 補中文零
					result += chineseNumber[0];
				}

				zeroCount = 0; // 歸零

				// 組中文數字+基本單位
				result += chineseNumber[Integer.parseInt(num)] + chineseBasicUnit[basicDigit];
			}

			// 組進階單位
			if (basicDigit == 0 && zeroCount < 4) {

				zeroCount = 0; // 歸零

				result += chineseAdvanceUnit[advanceDigit];
			}
		}

		return result;
	}

	public String getParentTranCode() {
		return parentTranCode;
	}

	public void setParentTranCode(String parentTranCode) {
		this.parentTranCode = parentTranCode;
	}

	public String getRptCode() {
		return rptCode;
	}

	public void setRptCode(String rptCode) {
		this.rptCode = rptCode;
	}

	public String getRptItem() {
		return rptItem;
	}

	public String getRptSecurity() {
		return rptSecurity;
	}

	public String getNowDate() {
		return nowDate;
	}

	public String getNowTime() {
		return nowTime;
	}

	public void setRptItem(String rptItem) {
		this.rptItem = rptItem;
	}

	public void setRptSecurity(String rptSecurity) {
		this.rptSecurity = rptSecurity;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> toPrint(long pdfno, int pageno, String printer) throws LogicException {
		this.info("MakeReport.toPrint = " + pdfno + "/" + pageno + "/" + printer);

		TxFile tTxFile = txFileService.findById(pdfno);

		if (tTxFile == null) {
			throw new LogicException(titaVo, "EC001", "(MakeReport)輸出檔(TxFile)序號:" + pdfno);
		}

		if (tTxFile.getFileType() != 6) {
			throw new LogicException(titaVo, "E0015", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + "，不為套印格式");
		}

		if (pageno == 0) {
			pageno = 1;
		}

		List<HashMap<String, Object>> pMap = new ArrayList<HashMap<String, Object>>();

//		this.info("MakeReport.toPrint.FileData = " + tTxFile.getFileData());

		try {
			this.listMap = new ObjectMapper().readValue(tTxFile.getFileData(), ArrayList.class);
		} catch (IOException e) {
			throw new LogicException("EC009", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + ",資料格式 " + e.getMessage());
		}

		int nowPage = 0;

		int morePage = 0;

//		this.info("MakeReport.toPrint listMap.size = " + listMap.size());

		for (HashMap<String, Object> map : this.listMap) {

			String type = map.get("type").toString();

//			this.info("MakeReport.toPrint type = " + type);

			if ("0".equals(type)) {
				// mew report
				HashMap<String, Object> map2 = new HashMap<String, Object>();
				map2.put("Action", 2);
				map2.put("Printer", printer);
				map2.put("ReportNo", tTxFile.getFileItem());
				pMap.add(map2);

				nowPage = 1;

				String papersize = map.get("paper").toString();
				String paperorientaton = map.get("paper.orientation").toString();

				String[] ss = papersize.split(",");

				map2 = new HashMap<String, Object>();
				if (ss.length == 3) {
					map2.put("Action", 3);
					map2.put("PageSize", "Custom");
					map2.put("PageUnit", ss[0]);
					map2.put("PageWidth", ss[1]);
					map2.put("PageHeight", ss[2]);
					map2.put("Orientation", paperorientaton);
				} else {
					map2.put("Action", 3);
					map2.put("PageSize", papersize);
					map2.put("Orientation", paperorientaton);
				}
				pMap.add(map2);
			} else if ("9".equals(type)) {
				// 套pdf form
				throw new LogicException("E0014", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + ",資料格式錯誤(type=9)");
			} else if ("1".equals(type)) {
				nowPage++;

				if (nowPage > pageno) {
					morePage = 1;
					break;
				}

				// new page
//				HashMap<String, Object> map2 = new HashMap<String, Object>();
//				map2.put("Action", 5);
//				pMap.add(map2);

			} else if ("2".equals(type) && nowPage == pageno) {
				// set font
//				String font = map.get("font").toString();
				int fontsize = Integer.parseInt(map.get("size").toString());

				HashMap<String, Object> map2 = new HashMap<String, Object>();
				map2.put("Action", 4);
				map2.put("Font", map.get("font").toString());
				map2.put("FontSize", fontsize);
				map2.put("FontWeight", "normal");
				map2.put("FontStyle", "normal");
				pMap.add(map2);
			} else if ("3".equals(type)) {
				// 指定行列,列印字串
				throw new LogicException("E0014", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + ",資料格式錯誤(type=3)");

			} else if ("4".equals(type) && nowPage == pageno) {
				// 指定XY軸列印字串

				int x = Integer.parseInt(map.get("x").toString());
				int y = Integer.parseInt(map.get("y").toString());

				double xx = x * 2.54 / 72 * 10;
				double yy = y * 2.54 / 72 * 10;

				String text = map.get("txt").toString();
				String align = map.get("align").toString();

				HashMap<String, Object> map2 = new HashMap<String, Object>();
				map2.put("Action", 5);
				map2.put("Unit", "mm");
				map2.put("X", xx);
				map2.put("Y", yy);
				map2.put("Align", align);
				map2.put("Text", text);
				pMap.add(map2);
			}
		}

		if (pMap.size() > 0) {
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("Action", 9);

			pMap.add(map2);
		}

		HashMap<String, Object> rmap = new HashMap<String, Object>();

		rmap.put("morePage", morePage);
		rmap.put("printJson", pMap);
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			rmap.put("printJson", mapper.writeValueAsString(pMap));
//		} catch (IOException e) {
//			throw new LogicException("EC009", "(MakeReport)資料格式 " + e.getMessage());
//		}

		return rmap;
	}
}
