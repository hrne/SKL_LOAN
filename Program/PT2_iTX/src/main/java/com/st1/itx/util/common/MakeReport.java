package com.st1.itx.util.common;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.domain.TxPrinter;
import com.st1.itx.db.domain.TxPrinterId;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.db.service.TxPrinterService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.report.ReportUtil;

/**
 * 
 * ----------------------- MakeReport 產生報表(PDF)共用工具 ------------------*
 * 
 * @author eric chang
 *
 */
@Component("makeReport")
@Scope("prototype")
public class MakeReport extends CommBuffer {

	// A4 : 595 * 842
	// 一英寸有72個點，如果你想要創建一個有a4矩形大小的pdf文檔，你必須計算點的數目：
	//
	// 21 cm / 2.54 = 8.2677 inch
	// 8.2677 * 72 = 595 points
	// 29.7 cm / 2.54 = 11.6929 inch
	// 11.6929 * 72 = 842 points

	// pdf底稿路徑
	@Value("${iTXPdfFolder}")
	private String pdfFolder = "";

	/* DB服務注入 */
	@Autowired
	private TxFileService txFileService;

	@Autowired
	private CdReportService cdReportService;

	@Autowired
	private TxPrinterService txPrinterService;

	@Autowired
	public DateUtil dDateUtil;

	@Autowired
	private PdfGenerator pdfGenerator;

	@Autowired
	private ReportUtil rptUtil;

	private int date = 0;

	// 單位別
	private String brno = "";

	// 源頭程式ID
	private String parentTranCode = "";

	// 報表代碼
	private String rptCode;

	// 報表名稱
	private String rptItem;

	// 報表機密等級(中文敍述)
	private String rptSecurity;

	// 紙張大小
	private String rptSize;

	// 紙張方向 P:Portrait Orientation (直印) , L:Landscape Orientation (橫印)
	private String pageOrientation;

	private ReportVo reportVo;

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
	public int NowRow; // TODO: 改為 getNowRow

	// header,footer處理記號
	private boolean hfProcess = false;

	// 輸出內容
	private int printCnt = 0;

	// 簽核
	private String signOff0 = "$$$Sign off0$$$";

	// 簽核
	private String signOff1 = "$$$Sign off1$$$";

	private String rptPassword = "";

	/**
	 * 印表機套印模式
	 */
	private boolean formMode = false;

	// 批號(控制分別出表,但記錄在同一TxFile)
	private String batchNo = "";

	// 目前的字距
	private int currentCharSpaces = 1;

	// 列印明細
	List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

	private void checkParm(ReportVo reportVo) throws LogicException {
		if (reportVo.getRptDate() <= 0) {
			throw new LogicException("EC004", "(MakeReport)日期(date)參數必須有值");
		}
		if (reportVo.getBrno() == null || reportVo.getBrno().isEmpty()) {
			throw new LogicException("EC004", "(MakeReport)單位(brno)參數必須有值");
		}
		if (reportVo.getRptCode() == null || reportVo.getRptCode().isEmpty()) {
			throw new LogicException("EC004", "(MakeReport)報表編號(rptCode)參數必須有值");
		}
		if (rptUtil.haveChinese(reportVo.getRptCode())) {
			throw new LogicException("EC004", "(MakeReport)報表編號(rptCode)參數不可有全形字");
		}
		if (reportVo.getRptItem() == null || "".equals(reportVo.getRptItem())) {
			throw new LogicException("EC004", "(MakeReport)報表說明(rptItem)參數必須有值");
		}
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
		if (rptUtil.haveChinese(rptCode)) {
			throw new LogicException("EC004", "(MakeReport)報表編號(rptCode)參數不可有全形字");
		}
		if (rptItem == null || "".equals(rptItem)) {
			throw new LogicException("EC004", "(MakeReport)報表說明(rptItem)參數必須有值");
		}
	}

	/**
	 * 結束製表<br>
	 * 
	 * @return long 檔案序號
	 * @throws LogicException LogicException
	 */
	public long close() throws LogicException {
		if (!reportVo.isUseDefault() && printCnt == 0) {
			this.print(1, 1, "無資料!!!");
		}
		if (this.nowPage > 0) {
			this.printFooter();
		}
		if (this.batchNo.isEmpty() && !formMode) {
			return newFile();
		} else {
			return findSameBatchNoAndAppendTxFileData();
		}
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
		return rptUtil.computeDivide(dividend, divisor, n);
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
		return rptUtil.convertAmtToChinese(amt);
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
		this.drawLine(x1, y1, x2, y2, (double) 1);
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

	@Override
	public void exec() throws LogicException {
		// nothing
	}

	/**
	 * @param amt 金額
	 * @param n   四捨五入至第n位
	 * @return String 具撇節的金額格式
	 */
	public String formatAmt(BigDecimal amt, int n) {
		return rptUtil.formatAmt(amt, n);
	}

	/**
	 * @param amt     金額
	 * @param n       四捨五入至第n位
	 * @param unitPow 每多少元為單位之次方數（如單位為千元，輸入3）
	 * @return String 具撇節並已除好的金額格式
	 */
	public String formatAmt(BigDecimal amt, int n, int unitPow) {
		return rptUtil.formatAmt(amt, n, unitPow);
	}

	/**
	 * @param amt 金額
	 * @param n   四捨五入至第n位
	 * @return String 具撇節的金額格式
	 */
	public String formatAmt(String amt, int n) {
		return rptUtil.formatAmt(amt, n);
	}

	/**
	 * @param amt     金額
	 * @param n       四捨五入至第n位
	 * @param unitPow 每多少元為單位之次方數（如單位為千元，輸入3）
	 * @return String 具撇節並已除好的金額格式
	 */
	public String formatAmt(String amt, int n, int unitPow) {
		return rptUtil.formatAmt(amt, n, unitPow);
	}

	public String getBatchNo() {
		return batchNo;
	}

	/**
	 * 傳入double,回傳BigDecimal,無法轉換為BigDecimal時給零
	 * 
	 * @param inputdouble 傳入字串
	 * @return BigDecimal
	 */
	public BigDecimal getBigDecimal(double inputdouble) {
		return rptUtil.getBigDecimal(inputdouble);
	}

	/**
	 * 傳入字串,回傳BigDecimal,無法轉換為BigDecimal時給零
	 * 
	 * @param inputString 傳入字串
	 * @return BigDecimal
	 */
	public BigDecimal getBigDecimal(String inputString) {
		return rptUtil.getBigDecimal(inputString);
	}

	/**
	 * 回傳目前紙張設定 X 軸正中央的座標 由 doToPdf 的 print 邏輯反推出來的
	 * 
	 * @return col for print
	 */
	public int getMidXAxis() {
//		this.info("getMidXAxis fontSize = " + this.fontSize);
//		this.info("getMidXAxis charSpaces = " + this.currentCharSpaces);
//		this.info("getMidXAxis pageOrientation = " + this.reportVo.getPageOrientation());
		int fontWidth = this.fontSize / 2 + this.currentCharSpaces; // 實際產檔時這邊存成 int, 因此這裡也用 int 以便完全模擬回去
		float paperWidthPt = ("P".equals(this.reportVo.getPageOrientation()) ? 8.3f : 11.7f) * 72f;
		float paperWidthPtHalf = paperWidthPt / 2f;
		int frameX = 5; // hard coded because it's hard coded in doToPdf()

		int result = (int) ((paperWidthPtHalf - frameX) / fontWidth + 1);

//		this.info("getMidXAxis result = " + result);

		return result;
	}

	public String getNowDate() {
		return nowDate;
	}

	/**
	 * 取製表頁次<br>
	 * 
	 * @return int 頁次
	 */
	public int getNowPage() {
		return this.nowPage;
	}

	public String getNowTime() {
		return nowTime;
	}

	public String getParentTranCode() {
		return parentTranCode;
	}

	public int getPrintCnt() {
		return printCnt;
	}

	/**
	 * 取製表日期<br>
	 * 
	 * @return int 西元製表日期
	 */
	public int getReportDate() {
		return this.reportVo.getRptDate();
	}

	public String getRptCode() {
		return reportVo.getRptCode();
	}

	public String getRptItem() {
		return reportVo.getRptItem();
	}

	public String getRptSecurity() {
		return reportVo.getSecurity();
	}

	/**
	 * 取中文日期<br>
	 * 
	 * @param date 日期
	 * @return String 中文日期
	 */
	public String getshowRocDate(int date) {
		return rptUtil.getChineseRocDate(date);
	}

	// 一般模式
	private void init() {
		// 使新舊方法可同時使用
		if (this.reportVo == null) {
			this.reportVo = ReportVo.builder().setBrno(this.brno).setRptDate(this.date).setRptCode(this.rptCode)
					.setRptItem(this.rptItem).setRptSize(this.rptSize).setSecurity(this.rptSecurity)
					.setPageOrientation(this.pageOrientation).setUseDefault(this.useDefault).build();
		} else {
			this.brno = this.reportVo.getBrno();
			this.date = this.reportVo.getRptDate();
			this.rptCode = this.reportVo.getRptCode();
			this.rptItem = this.reportVo.getRptItem();
			this.rptSize = this.reportVo.getRptSize();
			this.rptSecurity = this.reportVo.getSecurity();
			this.pageOrientation = this.reportVo.getPageOrientation();
			this.useDefault = this.reportVo.isUseDefault();
		}

		this.font = 1;
		this.fontSize = 10;

		this.rptBeginRow = 8;
		this.rptTotalRows = 40;
		this.NowRow = this.rptBeginRow - 1;

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.nowPage = 0;

		listMap = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 0);
		map.put("paper", reportVo.getRptSize().toUpperCase());
		map.put("paper.orientation", reportVo.getPageOrientation());
		map.put("font", this.font);
		map.put("font.size", this.fontSize);
		map.put("p", this.rptPassword);
		listMap.add(map);
	}

	// 底稿模式
	private void init9() {
		// 使新舊方法可同時使用
		if (this.reportVo == null) {
			this.reportVo = ReportVo.builder().setBrno(this.brno).setRptDate(this.date).setRptCode(this.rptCode)
					.setRptItem(this.rptItem).setRptSize(this.rptSize).setSecurity(this.rptSecurity)
					.setPageOrientation(this.pageOrientation).setUseDefault(this.useDefault).build();
		} else {
			this.brno = this.reportVo.getBrno();
			this.date = this.reportVo.getRptDate();
			this.rptCode = this.reportVo.getRptCode();
			this.rptItem = this.reportVo.getRptItem();
			this.rptSize = this.reportVo.getRptSize();
			this.rptSecurity = this.reportVo.getSecurity();
			this.pageOrientation = this.reportVo.getPageOrientation();
			this.useDefault = this.reportVo.isUseDefault();
		}

		this.font = 1;
		this.fontSize = 10;

		this.rptBeginRow = 8;
		this.rptTotalRows = 40;
		this.NowRow = this.rptBeginRow - 1;

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

		this.nowPage = 0;

		listMap = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 9);
		map.put("default", this.defaultPdf);
		map.put("paper.orientation", reportVo.getPageOrientation());
		map.put("font", this.font);
		map.put("font.size", this.fontSize);
		map.put("p", this.rptPassword);
		listMap.add(map);
	}

	private long newFile() throws LogicException {
		TxFile tTxFile = new TxFile();

		// 寫Txfile時需寫回onlineDB,但交易用的titaVo應維持原指向的DB
		TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();

		tmpTitaVo.putParam(ContentName.dataBase, ContentName.onLine);

		this.printRptFooter();

		// 檢查是否需核核
		CdReport tCdReport = cdReportService.findById(reportVo.getRptCode(), tmpTitaVo);
		if (tCdReport == null) {
			tTxFile.setSignCode("0");
		} else {
			tTxFile.setSignCode(String.valueOf(tCdReport.getSignCode()));
			// 2021-1-5 增加判斷 SignCode == 1 才印
			if (tCdReport.getSignCode() == 1 && !reportVo.isUseDefault()) {
				if ("P".equals(reportVo.getPageOrientation())) {
					this.print(1, this.getMidXAxis(), signOff0, "C");
					this.print(2, this.getMidXAxis(), "");
					this.print(1, this.getMidXAxis(), signOff1, "C");
				} else {
					this.print(1, this.getMidXAxis(), signOff0, "C");
					this.print(2, this.getMidXAxis(), "");
					this.print(1, this.getMidXAxis(), signOff1, "C");
				}
			}
		}

		if (formMode) {
			tTxFile.setFileType(6); // 固定6
		} else {
			tTxFile.setFileType(1); // 固定1:PDF
		}

		tTxFile.setFileFormat(1);
		tTxFile.setFileCode(reportVo.getRptCode());
		tTxFile.setFileItem(reportVo.getRptItem());
		tTxFile.setFileOutput(reportVo.getRptCode());
		tTxFile.setBrNo(reportVo.getBrno());
		tTxFile.setFileDate(reportVo.getRptDate());
		tTxFile.setBatchNo(this.batchNo);

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

	/**
	 * 換新頁<br>
	 * 
	 */
	public void newPage() {

		HashMap<String, Object> map = new HashMap<String, Object>();

		if (formMode) {
//			this.info("newPage nowPage = " + nowPage);
			if (this.nowPage > 0) {
				this.printContinueNext();
				this.printFooter();
			}
			this.nowPage++;
			this.printHeader();
			map.put("type", 1);
			listMap.add(map);
			printTitle();
			return;
		}

		// TODO: 參透這個欄位跟註解的真義
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

		// TODO: 參透這個欄位跟註解的真義
		// 必須
		this.hfProcess = false;

		this.NowRow = this.rptBeginRow - 1;

		printTitle();
	}

	/**
	 * 換新頁<br>
	 * 
	 * @param changeDefault 套印用強迫換頁
	 */

	public void newPage(boolean changeDefault) {
		reportVo.setUseDefault(changeDefault);
		newPage();
	}

	/**
	 * open <BR>
	 * 特殊處理: <BR>
	 * 紙張大小未設定時預設A4 <BR>
	 * 紙張方向未設定時預設橫印
	 * 
	 * @param titaVo   titaVo
	 * @param reportVo reportVo
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, ReportVo reportVo) throws LogicException {
		this.titaVo = titaVo;

		this.checkParm(reportVo);

		this.reportVo = reportVo;

		if (this.reportVo.getRptSize() == null || this.reportVo.getRptSize().isEmpty()) {
			this.reportVo.setRptSize("A4"); // 若未設定紙張大小，預設為A4
		}

		if (this.reportVo.getPageOrientation() == null || this.reportVo.getPageOrientation().isEmpty()
				|| !this.reportVo.getPageOrientation().equals("P")) {
			this.reportVo.setPageOrientation("L"); // 若未設定紙張方向 或者 不為P:直印，則預設為L:橫印
		}

		init();
	}

	/**
	 * @deprecated use {@link #open(TitaVo titaVo, ReportVo reportVo)} instead.
	 * @param titaVo   titaVo
	 * @param date     日期
	 * @param brno     單位
	 * @param rptCode  報表編號
	 * @param rptItem  報表說明
	 * @param security 報表機密等級(中文敍述)
	 * @throws LogicException LogicException
	 */
	@Deprecated
	public void open(TitaVo titaVo, int date, String brno, String rptCode, String rptItem, String security)
			throws LogicException {

		this.checkParm(date, brno, rptCode, rptItem);

		this.titaVo = titaVo;

		this.date = date;
		this.brno = brno;
		this.rptCode = rptCode;
		this.rptItem = rptItem;
		this.rptSecurity = security;

		this.rptSize = "A4"; // 紙張大小
		this.pageOrientation = "L"; // 紙張方向 P:Portrait Orientation (直印) , L:Landscape Orientation (橫印)

		this.useDefault = false;

		init();

	}

	/**
	 * open <BR>
	 * 有預設底稿者
	 * 
	 * @param titaVo     titaVo
	 * @param reportVo   reportVo
	 * @param defaultPdf 底稿檔案名稱
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, ReportVo reportVo, String defaultPdf) throws LogicException {
		this.titaVo = titaVo;

		this.checkParm(reportVo);

		this.reportVo = reportVo;

		if ("".equals(defaultPdf)) {
			throw new LogicException("EC004", "(MakeReport)預設PDF底稿(defaultPdf)參數必須有值");
		}

		// check defaultpdf
		String filename = pdfFolder + defaultPdf;

		File tempFile = new File(filename);
		if (!tempFile.exists()) {
			throw new LogicException("EC004", "(MakeReport)預設PDF底稿:" + filename + "不存在");
		}

		this.defaultPdf = defaultPdf;
		this.reportVo.setUseDefault(true);

		init9();
	}

	/**
	 * 
	 * @deprecated use
	 *             {@link #open(TitaVo titaVo, ReportVo reportVo, String defaultPdf)}
	 *             instead.
	 * @param titaVo     titaVo
	 * @param date       日期
	 * @param brno       單位
	 * @param rptCode    報表編號
	 * @param rptItem    報表說明
	 * @param security   報表機密等級(中文敍述)
	 * @param defaultPdf 預設PDF底稿
	 * @throws LogicException LogicException
	 */
	@Deprecated
	public void open(TitaVo titaVo, int date, String brno, String rptCode, String rptItem, String security,
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
		this.rptSecurity = security;
		this.defaultPdf = defaultPdf;
		this.useDefault = true;

		init9();
	}

	/**
	 * 開始製作報表<br>
	 * 
	 * @deprecated use {@link #open(TitaVo titaVo, ReportVo reportVo)} instead.
	 * @param titaVo          titaVo
	 * @param date            日期
	 * @param brno            單位
	 * @param rptCode         報表編號
	 * @param rptItem         報表說明
	 * @param Security        報表機密等級(中文敍述)
	 * @param pageSize        報表尺寸,例A4,A5,LETTER;自訂尺寸(寛,長)(單位:吋),例:8.5,5.5
	 * @param pageOrientation 報表方向,P:直印/L:橫印
	 * @throws LogicException LogicException
	 */
	@Deprecated
	public void open(TitaVo titaVo, int date, String brno, String rptCode, String rptItem, String Security,
			String pageSize, String pageOrientation) throws LogicException {

		this.checkParm(date, brno, rptCode, rptItem);

		this.titaVo = titaVo;

		this.date = date;
		this.brno = brno;
		this.rptCode = rptCode;
		this.rptItem = rptItem;
		this.rptSecurity = Security;

		this.rptSize = pageSize.toUpperCase();

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
	 * 開始製作印表機套印格式<BR>
	 * 
	 * @param titaVo   titaVo
	 * @param reportVo reportVo
	 * @throws LogicException LogicException
	 */
	public void openForm(TitaVo titaVo, ReportVo reportVo) throws LogicException {
		this.titaVo = titaVo;

		this.checkParm(reportVo);

		this.reportVo = reportVo;

		formMode = true;

		String[] rptSizes = this.reportVo.getRptSize().split(",");

		this.info("PageSize length = " + rptSizes.length);

		if (rptSizes.length != 1 && rptSizes.length != 3) {
			throw new LogicException("EC004", "(MakeReport)報表尺寸錯誤=" + this.reportVo.getRptSize());
		}

		this.reportVo.setPageOrientation(this.reportVo.getPageOrientation().toUpperCase());

		if (this.reportVo.getPageOrientation() == null || this.reportVo.getPageOrientation().isEmpty()
				|| !this.reportVo.getPageOrientation().equals("P")) {
			this.reportVo.setPageOrientation("L"); // 若未設定紙張方向 或者 不為P:直印，則預設為L:橫印
		}

		this.reportVo.setUseDefault(true);

		init();
	}

	/**
	 * 開始製作印表機套印格式<BR>
	 * 
	 * @deprecated use {@link #openForm(TitaVo titaVo, ReportVo reportVo)} instead.
	 * @param titaVo          titaVo
	 * @param date            日期
	 * @param brno            單位
	 * @param rptCode         報表編號
	 * @param rptItem         報表說明
	 * @param pageSize        報表尺寸,例A4,A5,LETTER;自訂尺寸(單位,寛,長)(單位:mm,cm,inch),例:cm,8.5,5.5
	 * @param pageOrientation 報表方向,P:直印/L:橫印
	 * @throws LogicException LogicException
	 */
	@Deprecated
	public void openForm(TitaVo titaVo, int date, String brno, String rptCode, String rptItem, String pageSize,
			String pageOrientation) throws LogicException {

		formMode = true;

		this.checkParm(date, brno, rptCode, rptItem);

		this.titaVo = titaVo;

		this.date = date;
		this.brno = brno;
		this.rptCode = rptCode;
		this.rptItem = rptItem;

		this.rptSize = pageSize.toUpperCase();

		String[] ss = pageSize.split(",");

		this.info("PageSize length = " + ss.length);

		if (ss.length != 1 && ss.length != 3) {
			throw new LogicException("EC004", "(MakeReport)報表尺寸錯誤=" + pageSize);
		}

		pageOrientation = pageOrientation.toUpperCase();
		if ("P".equals(pageOrientation)) {
			this.pageOrientation = pageOrientation;
		} else {
			this.pageOrientation = "L";
		}

		this.useDefault = true;

		init();
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
		toPrint(row, column, string, "L"); // 預設左靠
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
		toPrint(row, column, string, align);
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
	 * 預設續下頁
	 */
	public void printContinueNext() {
		// nothing
	}

	/**
	 * 預設表尾<br>
	 * 
	 */
	public void printFooter() {
		// nothing
	}

	/**
	 * 預設表頭<br>
	 */
	public void printHeader() {

		// 直接
		if ("P".equals(this.reportVo.getPageOrientation())) {
			printHeaderP();
		} else {
			printHeaderL();
		}

		// 明細起始列(自訂亦必須)
		this.setBeginRow(8);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(35);
	}

	// 預設表頭 - 橫印
	private void printHeaderL() {
		this.print(-1, 1, "程式ID：" + this.parentTranCode);
		this.print(-1, 70, "新光人壽保險股份有限公司", "C");
		this.print(-1, 120, "機密等級：" + this.reportVo.getSecurity());
		this.print(-2, 1, "報　表：" + this.reportVo.getRptCode());
		this.print(-2, 70, this.reportVo.getRptItem(), "C");
		this.print(-2, 120, "日　　期：" + showDate(this.nowDate));
		this.print(-3, 120, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 120, "頁　　次：" + this.nowPage);
		this.print(-6, 70, showRocDate(this.reportVo.getRptDate()), "C");
	}

	// 預設表頭 - 直印
	private void printHeaderP() {
		this.print(-1, 1, "程式ID：" + this.parentTranCode);
		this.print(-1, 50, "新光人壽保險股份有限公司", "C");
		this.print(-1, 80, "機密等級：" + this.reportVo.getSecurity());
		this.print(-2, 1, "報　表：" + this.reportVo.getRptCode());
		this.print(-2, 50, this.reportVo.getRptItem(), "C");
		this.print(-2, 80, "日　　期：" + showDate(this.nowDate));
		this.print(-3, 80, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 80, "頁　　次：" + this.nowPage);
		this.print(-6, 50, showRocDate(this.reportVo.getRptDate()), "C");
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
			// nothing
		} else if (this.nowPage == 0 || this.NowRow > (this.rptBeginRow + this.rptTotalRows - 1)) {
			newPage();
			this.NowRow += row;
			prow = this.NowRow;
		}

		return prow;
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
	 * @param x      x軸cm
	 * @param y      y軸cm
	 * @param width  每列列印半形字數
	 * @param height 每列高度px
	 * @param text   列印字串
	 */
	public void printRectCm(double x, double y, int width, int height, String text) {
		printRectCm(x, y, width, 0, height, text);
	}

	public void printRptFooter() {
		// nothing
	}

	/**
	 * 預設標題<br>
	 * 
	 */
	public void printTitle() {
		// nothing
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

	private void putFont() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 2);
		map.put("font", this.font);
		map.put("size", this.fontSize);
		listMap.add(map);
	}

	// 目前僅提供套form使用
	private long findSameBatchNoAndAppendTxFileData() throws LogicException {

		// 寫Txfile時需寫回onlineDB,但交易用的titaVo應維持原指向的DB
		TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();
		tmpTitaVo.putParam(ContentName.dataBase, ContentName.onLine);

		TxFile tTxFile = txFileService.findByBatchNoFirst(this.batchNo, tmpTitaVo);

		this.info("findSameBatchNoAndAppendTxFileData BatchNo = " + this.batchNo);
		if (tTxFile == null) {
			return newFile();
		}

		this.info("findSameBatchNoAndAppendTxFileData FileNo = " + tTxFile.getFileNo());

		List<HashMap<String, Object>> orgMap = new ArrayList<HashMap<String, Object>>();

		try {
			orgMap = new ObjectMapper().readValue(tTxFile.getFileData(),
					new TypeReference<List<Map<String, Object>>>() {
					});
		} catch (IOException e) {
			throw new LogicException("EC009",
					"(MakeReport)輸出檔(TxFile)序號:" + tTxFile.getFileNo() + ",資料格式 " + e.getMessage());
		}

		orgMap.addAll(listMap);

		try {
			ObjectMapper mapper = new ObjectMapper();
			tTxFile.setFileData(mapper.writeValueAsString(orgMap));
		} catch (IOException e) {
			throw new LogicException("EC009", "(MakeReport)資料格式 " + e.getMessage());
		}

		try {
			tTxFile = txFileService.update2(tTxFile, tmpTitaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "EC002", "(MakeReport)輸出檔(TxFile):" + e.getErrorMsg());
		}

		return tTxFile.getFileNo();
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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
	 * 設定字距<br>
	 * 
	 * @param charSpaces 字距點數
	 */
	public void setCharSpaces(int charSpaces) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 6);
		map.put("x", charSpaces);

		currentCharSpaces = charSpaces;

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
	 * 設定字體<br>
	 * 
	 * @param font 字體,1:標楷體 2:細明體
	 */
	public void setFont(int font) {
		this.font = font;
		putFont();
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
	 * 設定字體大小<br>
	 * 
	 * @param fontSize 字體大小,建議 8,10(預設),12,14
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		putFont();
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
	 * 設定明細列印列數<br>
	 * 
	 * @param rows 列數
	 */
	public void setMaxRows(int rows) {
		this.rptTotalRows = rows;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	public void setParentTranCode(String parentTranCode) {
		this.parentTranCode = parentTranCode;
	}

	/**
	 * 設定密碼，需在open之前 call
	 * 
	 * @param value 值
	 */
	public void setPassword(String value) {
		this.rptPassword = value;
	}

	public void setRptCode(String rptCode) {
		this.reportVo.setRptCode(rptCode);
	}

	public void setRptItem(String rptItem) {
		this.reportVo.setRptItem(rptItem);
	}

	public void setRptSecurity(String rptSecurity) {
		this.reportVo.setSecurity(rptSecurity);
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
		return rptUtil.showBcDate(date, type);
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
		return rptUtil.showBcDate(date, type);
	}

	/**
	 * 顯示民國年(樣式:yyy/mm/dd)
	 * 
	 * @param date 民國年yyymmdd
	 * @return 民國年yyy/mm/dd
	 */
	public String showDate(String date) {
		return rptUtil.showDate(date);
	}

	/**
	 * 顯示民國年(預設樣式:xxx 年 xx 月 xx 日)
	 * 
	 * @param date 西曆日期
	 * @return 中曆日期
	 */
	public String showRocDate(int date) {
		return rptUtil.showRocDate(date);
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
		return rptUtil.showRocDate(date, type);
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
		return rptUtil.showRocDate(date, type);
	}

	/**
	 * 顯示時間(樣式:hh:mm:ss)
	 * 
	 * @param time 時間hhmmss
	 * @return 時間hh:mm:ss
	 */
	public String showTime(String time) {
		return rptUtil.showTime(time);
	}

	/**
	 * 產製pdf檔<br>
	 * 
	 * @param pdfno 報表序號
	 * @throws LogicException LogicException
	 */
	public void toPdf(long pdfno) throws LogicException {
		pdfGenerator.generatePdf(pdfno, "");
	}

	/**
	 * 產製pdf檔<br>
	 * 
	 * @param pdfno    報表序號
	 * @param filename 指定輸出檔名
	 * @throws LogicException LogicException
	 */
	public void toPdf(long pdfno, String filename) throws LogicException {
		pdfGenerator.generatePdf(pdfno, filename);
	}

	private void toPrint(int row, int column, String string, String align) {
		// row : 列 < 0 表指定位置 , =0表目前列 , >0表跳行數
		// col : 行
		// string : 列印字串
		// align : L:左靠 , C:置中 , R:右靠

		int prow = printProw(row);

		align = align.toUpperCase();

		// 若非L、C、R，改為L
		if (!("L".equals(align) || "C".equals(align) || "R".equals(align))) {
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

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> toPrint(long pdfno, int pageno, String localIp) throws LogicException {
		this.info("MakeReport.toPrint = " + pdfno + "/" + pageno + "/" + localIp);

		if (pageno == 0)
			pageno = 1;

		TxFile tTxFile = txFileService.findById(pdfno);

		if (tTxFile == null) {
			throw new LogicException(titaVo, "EC001", "(MakeReport)輸出檔(TxFile)序號:" + pdfno);
		}

		if (tTxFile.getFileType() != 6) {
			throw new LogicException(titaVo, "E0015", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + "，不為套印格式");
		}

		TxPrinterId txPrinterId = new TxPrinterId();

		txPrinterId.setStanIp(localIp);
		txPrinterId.setFileCode(tTxFile.getFileCode());

		String serverIp = "";
		String printer = "";

		TxPrinter txPrinter = txPrinterService.findById(txPrinterId);
		if (txPrinter != null) {
			serverIp = txPrinter.getServerIp();
			printer = txPrinter.getPrinter();
		}

		List<HashMap<String, Object>> pMap = new ArrayList<HashMap<String, Object>>();

//		this.info("MakeReport.toPrint.FileData = " + tTxFile.getFileData());

		try {
			this.listMap = new ObjectMapper().readValue(tTxFile.getFileData(), ArrayList.class);
		} catch (IOException e) {
			throw new LogicException("EC009", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + ",資料格式 " + e.getMessage());
		}

		int printNowPage = 0;

		int morePage = 0;

		boolean type0 = true;
		for (HashMap<String, Object> map : this.listMap) {

			String type = map.get("type").toString();

			if ("0".equals(type) && type0) {
				type0 = false;
				// mew report
				HashMap<String, Object> map2 = new HashMap<String, Object>();
				map2.put("Action", 2);
				map2.put("Printer", printer);
				map2.put("ReportNo", tTxFile.getFileCode());
				map2.put("ReportNm", tTxFile.getFileItem());
				pMap.add(map2);

				printNowPage++;

				String papersize = map.get("paper").toString();
				String paperorientaton = map.get("paper.orientation").toString();

				String[] paperSizes = papersize.split(",");

				map2 = new HashMap<String, Object>();
				if (paperSizes.length == 3) {
					map2.put("Action", 3);
					map2.put("PageSize", "Custom");
					map2.put("PageUnit", paperSizes[0]);
					map2.put("PageWidth", paperSizes[1]);
					map2.put("PageHeight", paperSizes[2]);
					map2.put("Orientation", paperorientaton);
				} else {
					map2.put("Action", 3);
					map2.put("PageSize", papersize);
					map2.put("Orientation", paperorientaton);
				}
				pMap.add(map2);
			} else if ("0".equals(type) || "1".equals(type)) {
				printNowPage++;

				if (printNowPage > pageno) {
					morePage = 1;
					break;
				}
			} else if ("2".equals(type) && printNowPage == pageno) {
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
//				throw new LogicException("E0014", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + ",資料格式錯誤(type=3)");
			} else if ("4".equals(type) && printNowPage == pageno) {
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
			} else if ("9".equals(type)) {
				// 套pdf form
//				throw new LogicException("E0014", "(MakeReport)輸出檔(TxFile)序號:" + pdfno + ",資料格式錯誤(type=9)");
			}
		}

		if (pMap.size() > 0) {
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("Action", 9);
			pMap.add(map2);
		}

		HashMap<String, Object> rmap = new HashMap<String, Object>();

		rmap.put("ServerIp", serverIp);
		rmap.put("Printer", printer);
		rmap.put("morePage", morePage);
		rmap.put("printJson", pMap);

		return rmap;
	}

	public void checkLeftRows(int needRows) {
		this.info("checkLeftRows NowRow = " + this.NowRow);
		this.info("checkLeftRows rptTotalRows = " + this.rptTotalRows);
		this.info("checkLeftRows needRows = " + needRows);
		if ((rptTotalRows - NowRow) < needRows) {
			this.info("checkLeftRows 剩餘明細列數不足,先換頁.");
			this.newPage();
		} else {
			this.info("checkLeftRows 剩餘明細列數足夠,繼續列印明細.");
		}
	}
}