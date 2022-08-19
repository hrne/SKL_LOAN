package com.st1.itx.util.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.st1.itx.Exception.LogicException;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.filter.SafeClose;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.report.ReportUtil;

@Component
@Scope("prototype")
public class PdfGenerator extends CommBuffer {

	private static final int FRAME_X = 5;

	private static final int FRAME_Y = -5;

	// 檔案輸出路徑
	@Value("${iTXOutFolder}")
	private String outputFolder = "";

	// pdf底稿路徑
	@Value("${iTXPdfFolder}")
	private String pdfFolder = "";

	// 字型放置路徑
	@Value("${iTXFontFolder}")
	private String fontFolder = "";

	// 資源路徑
	@Value("${iTXResourceFolder}")
	private String resourceFolder = "";

	@Autowired
	private TxFileService txFileService;

	@Autowired
	private CdReportService cdReportService;

	@Autowired
	private CdEmpService cdEmpService;

	@Autowired
	private ReportUtil rptUtil;

	private long pdfNo;

	private String outputFile;

	private String rptTlrNo;

	private Timestamp rptCreateDate;

	private String signOffTeller;

	private String signOffSupervisor;

	// 列印明細
	private List<HashMap<String, Object>> listMap = null;

	// 寬度點數
	private double xPoints = 0;

	// 高度點數
	private double yPoints = 0;

	private int nowPage = 0;

	// 簽核
	private String signOff0 = "$$$Sign off0$$$";

	// 簽核
	private String signOff1 = "$$$Sign off1$$$";
	// 輸出檔名
	private FileOutputStream fos = null;

	// 建立一個Document物件，並設定頁面大小及左、右、上、下的邊界，rotate()橫印
	private Document document = null;
	// 設定要輸出的Stream
	private PdfWriter writer = null;
	// 套底稿用
	private PdfStamper stamper = null;
	private AcroFields fields = null;
	private PdfReader reader = null;
	private ByteArrayOutputStream baos = null;

	private PdfCopy copy = null;

	private String defaultname = null;

	// 讀取頁的長寛
	private Rectangle page = null;

	// 設定要輸出的Stream
	private PdfContentByte content = null;

	private PdfContentByte underContent;

	private ReportVo reportVo;

	private BaseFont baseFont;
	private int pdfFontSize;
	private int charSpaces;
	private int lineSpaces;
	private int fontWidth;
	private int fontHeight;
	private boolean watermarkFlag;

	private void adjPdfFontSize(int mapFontSize) {
		pdfFontSize = mapFontSize;
		fontWidth = pdfFontSize / 2 + charSpaces;
		fontHeight = pdfFontSize + lineSpaces + 2;
	}

	private void deleteOldFile(String outfile) {
		File file = new File(outfile);
		try {
			Files.delete(file.toPath());
		} catch (IOException e) {
			this.info("PdfGenerator Files.delete error =" + e.getMessage());
		}
	}

	private void drawLine(HashMap<String, Object> map) {
		// 畫線
		int x1 = Integer.parseInt(map.get("x1").toString());
		int y1 = Integer.parseInt(map.get("y1").toString());
		int x2 = Integer.parseInt(map.get("x2").toString());
		int y2 = Integer.parseInt(map.get("y2").toString());
		double w = Double.parseDouble(map.get("w").toString());

		int py1 = (int) (this.yPoints - y1);
		int py2 = (int) (this.yPoints - y2);

		content.saveState();
		// 設定線條寬度
		content.setLineWidth(w);
		// 设置画线的颜色
		content.setColorStroke(BaseColor.BLACK);
		// 绘制起点坐标
		content.moveTo(x1, (float) (FRAME_Y + py1));
		// 绘制终点坐标
		content.lineTo(x2, (float) (FRAME_Y + py2));
		// 确认直线的绘制
		content.stroke();
		content.restoreState();
	}

	@Override
	public void exec() throws LogicException {
		// nothing
	}

	public void generatePdf(long pdfNo, String fileName) throws LogicException {
		this.info("generatePdf pdfNo = " + pdfNo + ", fileName = " + fileName);

		this.pdfNo = pdfNo;

		settingFromTxFile(fileName);

		init();

		try {
			for (HashMap<String, Object> map : this.listMap) {

				String type = map.get("type").toString();

				if (!reportVo.isUseDefault() && type.equals("7")) {
					break;
				}

				switch (type) {
				case "0":
					openWithNewPdf(outputFile, map);
					break;
				case "9":
					openWithDefaultPdf(outputFile, map);
					break;
				case "1":
					setNewPage();
					break;
				case "2":
					setFont(map);
					break;
				case "3":
					printStringByRowAndColumn(map);
					break;
				case "4":
					printStringByPoint(map);
					break;
				case "5":
					drawLine(map);
					break;
				case "6":
					setFontWidth(map);
					break;
				case "7":
					setValueToField(map);
					break;
				case "8":
					setFontHeight(map);
					break;
				case "A":
					printPicture(map);
					break;
				case "B":
					printStringByRange(map);
					break;
				default:
					// unknown type
					break;
				}
			}

			if (reportVo.isUseDefault()) {
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
		} catch (DocumentException | IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throw new LogicException("EC009", "(PdfGenerator)輸出檔(TxFile)序號:" + this.pdfNo + ",輸出PDF " + e.getMessage());
		} finally {
			SafeClose.close(document);
			SafeClose.close(writer);
			SafeClose.close(reader);
			SafeClose.close(copy);
			SafeClose.close(stamper);
			SafeClose.close(baos);
			SafeClose.close(fos);
		}
	}

	private void init() throws LogicException {
		reportVo = ReportVo.builder().build();
		fos = null;
		document = null;
		writer = null;
		stamper = null;
		fields = null;
		reader = null;
		baos = null;
		copy = null;
		defaultname = null;
		page = null;
		content = null;
		// 預設字型
		try {
			baseFont = setBaseFont("1");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			throw new LogicException("EC009", "(PdfGenerator)輸出檔(TxFile)序號:" + this.pdfNo + ",輸出PDF " + e.getMessage());
		}
		// 預設字距
		charSpaces = 1;
		// 預設行距
		lineSpaces = 0;
		// 預設字型大小
		this.adjPdfFontSize(10);
	}

	private void openWithDefaultPdf(String outfile, HashMap<String, Object> map)
			throws LogicException, DocumentException, IOException {
		reportVo.setUseDefault(true);

		defaultname = pdfFolder + map.get("default").toString();

		File tempFile = new File(defaultname);
		if (!tempFile.exists()) {
			throw new LogicException("EC004", "(PdfGenerator)預設PDF底稿:" + defaultname + "不存在");
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
		content = stamper.getOverContent(1);

		String doToPdfFont = map.get("font").toString();
		baseFont = setBaseFont(doToPdfFont);
		int mapFontSize = Integer.parseInt(map.get("font.size").toString());
		this.adjPdfFontSize(mapFontSize);

		this.nowPage = 1;
		this.info("open = " + this.nowPage);

		this.xPoints = stamper.getWriter().getPageSize().getWidth();
		this.yPoints = stamper.getWriter().getPageSize().getHeight();

		underContent = stamper.getUnderContent(1);

		setWatermark();
	}

	private void openWithNewPdf(String outfile, Map<String, Object> map) throws DocumentException, IOException {
		reportVo.setUseDefault(false);

		// 輸出檔名
		fos = new FileOutputStream(new File(outfile));
		// 報表啟始
		String papersize = map.get("paper").toString();

		String[] pageSizes = papersize.split(",");

		Rectangle pagesize = PageSize.A4; // 預設A4

		if ("LETTER".equals(papersize)) {
			pagesize = PageSize.LETTER;
		} else if ("A5".equals(papersize)) {
			pagesize = PageSize.A5;
//		} else if (i != -1) {
			// 自訂尺寸,以inch為單位
		} else if (pageSizes.length == 2) {
			long n1 = (long) Math.ceil((Double.parseDouble(pageSizes[0]) * 72));
			long n2 = (long) Math.ceil((Double.parseDouble(pageSizes[1]) * 72));
			pagesize = new Rectangle(n1, n2);
		}

		String paperorientaton = map.get("paper.orientation").toString();

		// 建立一個Document物件，並設定頁面大小及左、右、上、下的邊界，rotate()橫印
		if ("P".equals(paperorientaton)) {
			document = new Document(pagesize, 0, 0, 0, 0);
		} else {
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

		if ("P".equals(paperorientaton)) {
			this.xPoints = writer.getPageSize().getWidth();
			this.yPoints = writer.getPageSize().getHeight();
		} else {
			this.xPoints = writer.getPageSize().getHeight();
			this.yPoints = writer.getPageSize().getWidth();
		}

		document.open();

		// 讀取頁的長寛
		page = writer.getPageSize();

		// 設定要輸出的Stream
		content = writer.getDirectContent();

		String mapFont = map.get("font").toString();
		int mapFontSize = Integer.parseInt(map.get("font.size").toString());
		baseFont = setBaseFont(mapFont);
		adjPdfFontSize(mapFontSize);

		underContent = writer.getDirectContentUnder();
		setWatermark();
	}

	private void printPicture(HashMap<String, Object> map) throws DocumentException, IOException, LogicException {
		// 列印圖片
		String fna = map.get("f").toString();
		int x = Integer.parseInt(map.get("x").toString());
		int y = Integer.parseInt(map.get("y").toString());
		int yy = (int) this.yPoints - y;
		float percent = Float.parseFloat(map.get("p").toString());

		String imagename = resourceFolder + fna;

		this.info("PdfGenerator imagename = " + imagename);

		File tempFile = new File(imagename);
		if (!tempFile.exists()) {
			throw new LogicException("EC009", "(PdfGenerator)輸出檔(TxFile)序號:" + this.pdfNo + ",圖片不存在 " + imagename);
		}

		this.info("PdfGenerator percent = " + percent);

		Image image = Image.getInstance(imagename);

		this.info("PdfGenerator image b = " + image.getWidth() + "/" + image.getHeight());

		double imageH = Math.ceil(image.getHeight());

		if (percent != 0) {
			image.scalePercent(percent);
			imageH = Math.ceil(image.getHeight() * percent / 100);
		}

		this.info("PdfGenerator image a = " + image.getWidth() + "/" + image.getHeight());

		yy -= imageH;

		// 新增圖片
		image.setAbsolutePosition(x, yy);
		content.addImage(image);
	}

	private void printStringByPoint(HashMap<String, Object> map) {
		// 指定XY軸列印字串
		int x = Integer.parseInt(map.get("x").toString());
		int y = Integer.parseInt(map.get("y").toString());

		int yy = (int) (this.yPoints - y);

		String txt = map.get("txt").toString();
		String align = map.get("align").toString();
		content.beginText();
		content.setFontAndSize(baseFont, pdfFontSize);
		content.setCharacterSpacing(charSpaces);
		if ("L".equals(align)) {
			content.showTextAligned(PdfContentByte.ALIGN_LEFT, txt, (float) (FRAME_X + x), (float) (FRAME_Y + yy), 0);
		} else if ("C".equals(align)) {
			content.showTextAligned(PdfContentByte.ALIGN_CENTER, txt, (float) (FRAME_X + x), (float) (FRAME_Y + yy), 0);
		} else if ("R".equals(align)) {
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, txt, (float) (FRAME_X + x), (float) (FRAME_Y + yy), 0);

		}
		content.endText();
	}

	private void printStringByRange(HashMap<String, Object> map) {
		// 指定區間列印字串
		int x = Integer.parseInt(map.get("x").toString());
		int y = Integer.parseInt(map.get("y").toString());
		int w = Integer.parseInt(map.get("w").toString());
		int w2 = Integer.parseInt(map.get("w2").toString());
		int h = Integer.parseInt(map.get("h").toString());
		int yy = (int) this.yPoints - y;
		String s = map.get("s").toString();

		StringBuilder ps = new StringBuilder();
		int pw = 0;

		StringBuilder prefix = new StringBuilder();
		for (int i = 0; i < w2; i++) {
			prefix.append(" ");
		}

		for (int i = 0; i < s.length(); i++) {
			String ss = s.substring(i, i + 1);

			ps.append(ss);

			int ww = 1;
			if (rptUtil.haveChinese(ss)) {
				ww = 2;
			}
			pw += ww;
			if (pw >= w) {
				content.beginText();

				content.setFontAndSize(baseFont, pdfFontSize);
				content.setCharacterSpacing(charSpaces);
				content.showTextAligned(PdfContentByte.ALIGN_LEFT, ps.toString(), (float) (FRAME_X + x),
						(float) (FRAME_Y + yy), 0);
				content.endText();

				ps = prefix;
				pw = w2;
				yy -= h;
			}
		}
		if (pw > 0) {
			content.beginText();
			this.info("PdfGenerator basefont = " + BaseFont.TIMES_BOLD);
			content.setFontAndSize(baseFont, pdfFontSize);
			content.setCharacterSpacing(charSpaces);
			content.showTextAligned(PdfContentByte.ALIGN_LEFT, ps.toString(), (float) (FRAME_X + x),
					(float) (FRAME_Y + yy), 0);
			content.endText();
		}
	}

	private void printStringByRowAndColumn(HashMap<String, Object> map) {
		// 指定行列,列印字串
		int row = Integer.parseInt(map.get("row").toString());
		int col = Integer.parseInt(map.get("col").toString());
		String txt = map.get("txt").toString();

		if (signOff0.equals(txt)) {
			txt = "=====　報　表　結　束　=====";
		}
		// 簽核
		if (signOff1.equals(txt)) {
			txt = FormatUtil.padX(" ", 60);
			txt += "經辦：";
			txt += FormatUtil.padX(signOffTeller, 40);
			txt += "主管：";
			txt += FormatUtil.padX(signOffSupervisor, 40);
		}

		String align = map.get("align").toString();
		int x = (col - 1) * fontWidth;
		int y = (int) page.getHeight() - (row * fontHeight);
		content.beginText();

		content.setFontAndSize(baseFont, pdfFontSize);
		content.setCharacterSpacing(charSpaces);
		if ("L".equals(align)) {
			content.showTextAligned(PdfContentByte.ALIGN_LEFT, txt, (float) (FRAME_X + x), (float) (FRAME_Y + y), 0);
		} else if ("C".equals(align)) {
			content.showTextAligned(PdfContentByte.ALIGN_CENTER, txt, (float) (FRAME_X + x), (float) (FRAME_Y + y), 0);
		} else if ("R".equals(align)) {
			content.showTextAligned(PdfContentByte.ALIGN_RIGHT, txt, (float) (FRAME_X + x), (float) (FRAME_Y + y), 0);
		}
		content.endText();
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

	private void setFont(HashMap<String, Object> map) throws IOException, DocumentException {
		// 設定字型
		String font = map.get("font").toString();
		baseFont = setBaseFont(font);
		int mapFontSize = Integer.parseInt(map.get("size").toString());
		adjPdfFontSize(mapFontSize);
	}

	private void setFontHeight(HashMap<String, Object> map) {
		// 設定行距
		String yy = map.get("y").toString();
		if (!"".equals(yy)) {
			int y = Integer.parseInt(yy);
			lineSpaces = y;
			this.fontHeight = pdfFontSize + lineSpaces + 2;
		}
	}

	private void setFontWidth(HashMap<String, Object> map) {
		// 設定字距
		String xx = map.get("x").toString();
		if (!"".equals(xx)) {
			int x = Integer.parseInt(xx);
			charSpaces = x;
			fontWidth = pdfFontSize / 2 + charSpaces;
		}
	}

	@SuppressWarnings("unchecked")
	private void setListMapFromJson(String fileData) throws LogicException {
		try {
			this.listMap = new ObjectMapper().readValue(fileData, ArrayList.class);
		} catch (IOException e) {
			throw new LogicException("EC009", "(PdfGenerator)輸出檔(TxFile)序號:" + this.pdfNo + ",資料格式 " + e.getMessage());
		}
	}

	private void setNewPage() throws DocumentException, IOException {
		// 新頁
		if (reportVo.isUseDefault()) {
			if (this.nowPage > 0) {
				this.info("setNewPage close page, nowPage = " + nowPage);
				stamper.setFormFlattening(true);
				stamper.close();
				reader.close();
				reader = new PdfReader(baos.toByteArray());
				copy.addDocument(reader);
//				reader.close();
			}
			this.nowPage++;
			this.info("setNewPage open new page, nowPage = " + nowPage);
			// new
			baos = new ByteArrayOutputStream();
			reader = new PdfReader(defaultname);
			stamper = new PdfStamper(reader, baos);
			fields = stamper.getAcroFields();
			content = stamper.getOverContent(1);
			underContent = stamper.getUnderContent(1);
		} else {
			document.newPage();
			content = writer.getDirectContent();
			underContent = writer.getDirectContentUnder();
		}
		setWatermark();
	}

	private String setOutputFile(String fileName, String fileOutput) {
		StringBuilder result = new StringBuilder();
		result.append(outputFolder);
		if (!"".equals(fileName)) {
			result.append(fileName);
		} else {
			result.append(fileOutput);
		}
		result.append(".pdf");
		this.info("OutputFile =" + result.toString());
		return result.toString();
	}

	private void settingFromTxFile(String fileName) throws LogicException {
		TxFile tTxFile = txFileService.findById(this.pdfNo);

		if (tTxFile == null) {
			throw new LogicException(titaVo, "EC001", "(PdfGenerator)輸出檔(TxFile)序號:" + this.pdfNo);
		}

		if (tTxFile.getFileType() != 1) {
			throw new LogicException(titaVo, "E0015", "(PdfGenerator)輸出檔(TxFile)序號:" + this.pdfNo + "，不為PDF格式");
		}

		rptTlrNo = tTxFile.getCreateEmpNo();

		rptCreateDate = tTxFile.getCreateDate();

		this.info("rptTlrNo = " + rptTlrNo);

		setListMapFromJson(tTxFile.getFileData());

		outputFile = setOutputFile(fileName, tTxFile.getFileOutput());

		setWatermarkFlag(tTxFile.getFileCode());

		// 先刪除舊檔
		deleteOldFile(outputFile);

		signOffTeller = "";
		signOffSupervisor = "";

		if (!"".equals(tTxFile.getTlrNo())) {
			CdEmp cdEmp = cdEmpService.findById(tTxFile.getTlrNo(), titaVo);

			if (cdEmp != null) {
				signOffTeller = cdEmp.getFullname();
			}
		}
		if (!"".equals(tTxFile.getSupNo())) {
			CdEmp cdEmp = cdEmpService.findById(tTxFile.getSupNo(), titaVo);

			if (cdEmp != null) {
				signOffSupervisor = cdEmp.getFullname();
			}
		}
	}

	private void setValueToField(HashMap<String, Object> map) throws IOException, DocumentException {
		// 預設底稿套form用,設欄位值
		String field = map.get("f").toString();
		String value = map.get("v").toString();
		fields.setFieldProperty(field, "textfont", baseFont, null); // 設定字型
		fields.setField(field, value);
	}

	/**
	 * 浮水印
	 * 
	 * @throws IOException       IOException
	 * @throws DocumentException DocumentException
	 */
	private void setWatermark() throws IOException, DocumentException {

		if (!watermarkFlag) {
			return;
		}

		PdfGState graphicState = new PdfGState();
		graphicState.setFillOpacity(0.7f);
		graphicState.setStrokeOpacity(1f);

		BaseFont tmpBaseFont = this.setBaseFont("1");

		StringBuilder watermark = new StringBuilder();

		watermark.append(rptTlrNo).append(" ");

		CdEmp tCdEmp = cdEmpService.findById(rptTlrNo);

		String empNm = "";
		if (tCdEmp != null) {
			empNm = tCdEmp.getFullname();
		}

		this.info("PdfGenerator setWatermark empNm = " + empNm);

		watermark.append(empNm).append(" ");

		String rptDate = new SimpleDateFormat("yyyyMMdd").format(rptCreateDate);
		String rptTime = new SimpleDateFormat("HHmmss").format(rptCreateDate);
		watermark.append(rptUtil.showRocDate(rptDate, 2)).append(" ").append(rptUtil.showTime(rptTime));

		underContent.setGState(graphicState);
		underContent.beginText();
		underContent.setFontAndSize(tmpBaseFont, 12);
		underContent.setColorFill(BaseColor.LIGHT_GRAY);

		float widthMax = document.getPageSize().getWidth();
		float heightMax = document.getPageSize().getHeight();

		for (float w = 0; w < widthMax + 150f; w += 150f) {
			for (float h = 0; h < heightMax + 80f; h += 80f) {
				underContent.showTextAligned(Element.ALIGN_CENTER, watermark.toString(), w, h, 15f);
			}
		}
		underContent.endText();
	}

	private void setWatermarkFlag(String fileCode) {
		watermarkFlag = true;
		// 檢查是否需浮水印
		CdReport tCdReport = cdReportService.findById(fileCode);
		if (tCdReport != null && tCdReport.getWatermarkFlag() != 1) {
			watermarkFlag = false;
		}
	}
}
