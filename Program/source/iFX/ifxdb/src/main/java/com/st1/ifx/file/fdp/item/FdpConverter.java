package com.st1.ifx.file.fdp.item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
//import com.ibm.db2.jcc.a.b;
//import com.ibm.db2.jcc.a.c;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

//備註:只有表格內改雙分號";;"   其餘分隔都還是使用逗號","
@Component
@Scope("prototype")
public class FdpConverter {
	private static final Logger logger = LoggerFactory.getLogger(FdpConverter.class);
	private Properties pEmailInfo;
	@Value("${import.pdfbox}")
	private String outputpdfFolder;

	@Value("${import.copytoFolder}")
	private String copytoFolder;

	@Value("${import.backupFolder}")
	private String backupFolder;

	@Value("${import.convertFdpToExt}")
	private String extName = ".pdf";

	@Value("${import.mailIpAddress}")
	private String addrip = "";

	// 產生中文字型 ,fontRedCN, fontBlueCN, fontBlueSmallCN, fontBlackSmallCN
	// 產生字型,字體大小
	private static final Font emptyfont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL,
			new BaseColor(0, 0, 0));

	private static BaseFont bfChinese = null;
	// 變動文字參數
	private static Font _fontAllCN = null;
	// 表格標題
	private static Font _fontTable0 = null;
	// 文件尾巴
	private static Font _fontFooter = null;
	// private static final Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN,
	// 18,
	// Font.BOLD);

	// private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
	// Font.BOLD);

	// private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
	// Font.BOLD);
	private static Document document;

	@ServiceActivator
	public void convert(Message msg) throws Throwable {
		pEmailInfo = new Properties();
		pEmailInfo.setProperty("addrip", addrip);
		logger.info("converting msg:{}", msg);
		File file = (File) msg.getPayload();
		logger.info(FilterUtils.escape("convert " + file.getAbsolutePath() + " to " + outputpdfFolder));
		String filePath = file.getAbsolutePath();
		String name = FilenameUtils.getBaseName(filePath);
		String targetFilePath = FilenameUtils.concat(outputpdfFolder, name + extName);
		convertTopdf(filePath, targetFilePath);
		moveFileToBackupFolder(file.getAbsolutePath());
		MailConverter mc = new MailConverter();
		// mc.doSend(addRip, messqge_subject2, messqge_body2, tos, attachFiles);
		mc.doMail(pEmailInfo);
	}

	private void convertTopdf(String filePath, String targetFilePath) throws Exception {
		logger.info("convertTopdf filePath:" + filePath);
		String tmpFilePath = targetFilePath + "~";

		logger.info("convertTopdf target path:" + targetFilePath);

		// String[] ss = name.split("_");
		// ss = ss[1].split("-");
		// final String help = ss[0];
		// logger.info("Help topic:" + help);

		List<String> lines = new ArrayList<String>();

		BufferedReader br = null;
		File f = null;
		FileInputStream fis = null;
		InputStreamReader isr = null;

		try {

			String sCurrentLine;
			f = new File(filePath);
			fis = new FileInputStream(f);
			// java.io.FileNotFoundException
			logger.info("BufferedReader BIG5..");
			isr = new InputStreamReader(fis, "BIG5");
			br = new BufferedReader(isr); // MFT只有BIG5

			logger.info("temp after BufferedReader");
			while ((sCurrentLine = br.readLine()) != null) {
				logger.info("List:" + sCurrentLine);
				lines.add(sCurrentLine);
			}

		} catch (IOException e) {
			logger.error("IOException:", e);
		} finally {
			SafeClose.close(br);
			SafeClose.close(isr);
			SafeClose.close(fis);
		}

		File tmpFile = new File(tmpFilePath);
		// 建立假資料
		logger.info("ItxtSampleh START");
		logger.info("ItxtSampleh fileName:" + targetFilePath);

		FileOutputStream fos = null;
		try {
			// 產生中文字型,字體大小 ,
			// BaseFont bfChinese = BaseFont.createFont("MHei-Medium","UniCNS-UCS2-H",
			// BaseFont.EMBEDDED);
			// fontRedCN = new Font(bfChinese, 18, Font.BOLD, new BaseColor(255, 0, 0));
			// fontBlueCN = new Font(bfChinese, 18, Font.BOLD, new BaseColor(0, 0, 255));
			// fontBlueSmallCN = new Font(bfChinese, 12, Font.BOLD, new BaseColor(0, 255,
			// 0));
			// fontBlackSmallCN = new Font(bfChinese, 12, Font.BOLD, new BaseColor(0, 0,
			// 0));

			// 產生一個A4大小的PDF檔案
			document = new Document(PageSize.A4);
			fos = new FileOutputStream(tmpFile);
			PdfWriter writer = PdfWriter.getInstance(document, fos);
			// ** 加入pdf密碼 ,去除空白**
			String USER_PASS = lines.get(0).trim();
			lines.remove(0);
			String OWNER_PASS = "";
			if (!USER_PASS.isEmpty()) {
				logger.info("USER_PASS:" + USER_PASS);
				try {
					System.out.println("writer.setEncryption!");
					writer.setEncryption(USER_PASS.getBytes(), OWNER_PASS.getBytes(),
							PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
					System.out.println("after setEncryption!");
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					logger.error(errors.toString());
				}
			}

			document.open();
			// PDF 文件內容部分
			// addMetaDataTitle(document);
			// PDF 表頭部分
			// addTitlePage(document);
			// PDF 內容部分
			addContent(document, lines, pEmailInfo, targetFilePath);
			document.close();

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			logger.error("error about DocumentException:", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("error about IOException:", e);
		} finally {
			SafeClose.close(fos);
		}

		// FileUtils.writeLines(tmpFile, lines, false);
		logger.info("temp after writeLines");
		System.out.println("targetFilePath ==========>" + targetFilePath);
		FileUtils.moveFile(tmpFile, new File(targetFilePath));
		System.out.println("targetFilePath ==========>" + targetFilePath);
		String copyto = FilenameUtils.concat(copytoFolder, FilenameUtils.getPath(targetFilePath));
		System.out.println("copyto ==========>" + copyto);
		logger.info("copyto " + copyto);
		FileUtils.copyFileToDirectory(new File(targetFilePath), new File(copyto), true);
		logger.info("convertTopdf done ");
	}

	private void moveFileToBackupFolder(String filePath) {
		// errorFolder
		String fileName = FilenameUtils.getName(filePath);
		String newFilePath = FilenameUtils.concat(backupFolder, fileName);
		File f = new File(newFilePath);
		if (f.exists()) {
			logger.warn("convertTopdf " + newFilePath + " exists, delete it");
			f.delete();
		}

		try {
			FileUtils.moveFileToDirectory(new File(FilterUtils.filter(filePath)),
					new File(FilterUtils.filter(backupFolder)), true);
		} catch (IOException e) {
			logger.error("convertTopdf move file", e);
		}

		logger.info("convertTopdf " + fileName + " is moved to " + newFilePath);
	}

	/**
	 * PDF 內容部分
	 * 
	 * @param document
	 * @param list
	 * @throws DocumentException
	 */
	private static void addContent(Document document, List<String> list, Properties pEmailInfo, String targetFilePath)
			throws DocumentException {
		Paragraph paragraph = new Paragraph();
		Phrase phrase0;
		Paragraph paragraphTemp;
		String[] array;

		String colortype;
		int altrAlign = Element.ALIGN_LEFT;
		String altrAligntemp;
		int font, emptycont, size;
		boolean togethchunk = false;
		PdfPTable table = null;
		try {
			bfChinese = BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("error about createFont:", e);
		}
		_fontFooter = new Font(bfChinese, 10, Font.NORMAL, new BaseColor(0, 0, 0));
		_fontAllCN = new Font(bfChinese, 12, Font.NORMAL, new BaseColor(0, 0, 0));
		_fontTable0 = new Font(bfChinese, 12, Font.BOLD, new BaseColor(0, 0, 0));

		addMetaDataTitle();
		logger.info("Start addContent list");
		int iFlagCount = 0;
		String sFlag = "";// 用來判斷是否為結尾
		for (String o : list) {
			// 因中心是固定長,故統一右邊去空白
			o = o.replaceAll("\\s+$", "");
			array = null;
			if (o.startsWith("{{formfeed}}")) {
				document.newPage();
				if (o.indexOf("}}") != -1 && o.length() > o.indexOf("}}") + 2) {
					o = o.substring(o.indexOf("}}") + 2);
					if (!togethchunk) {
						paragraphTemp = new Paragraph(o, _fontAllCN);
						paragraphTemp.setAlignment(altrAlign);
						document.add(paragraphTemp);
					} else {
						phrase0 = new Phrase(new Chunk(o, _fontAllCN));
						document.add(phrase0);
					}
				}

			} else if (o.startsWith("{{emptyline")) { // {{emptyline,10}}
				array = o.split(",");
				String emptyctemp = array[1].toString();
				emptycont = Integer.parseInt(emptyctemp.substring(0, emptyctemp.indexOf("}}"))); // }}前數字轉空行
				logger.info("emptycont2:" + emptycont);
				addEmptyLine(emptycont);
				if (o.indexOf("}}") != -1 && o.length() > o.indexOf("}}") + 2) {
					logger.info("ori out string:" + o + ",index:" + o.indexOf("}}") + 2);
					o = o.substring(o.indexOf("}}") + 2);
					if (!togethchunk) {
						paragraphTemp = new Paragraph(o, _fontAllCN);
						paragraphTemp.setAlignment(altrAlign);
						document.add(paragraphTemp);
					} else {
						phrase0 = new Phrase(new Chunk(o, _fontAllCN));
						document.add(phrase0);
					}
				}
			} else if (o.startsWith("{{$")) { // {{$0,12,0,0}}
				o = o.substring(3);
				array = o.split(",");
				font = Integer.parseInt(array[0]);// 0正常,1粗體,2斜體
				size = Integer.parseInt(array[1]);// 文字大小
				colortype = array[2].toString();// 顏色0D黑色,1G綠色,2B藍色,3R紅色
				altrAligntemp = array[3].toString();// 文字位置LEFT 0, CENTER 1
				logger.info("end }} indexof:" + altrAligntemp.indexOf("}}"));
				altrAlign = Integer.parseInt(altrAligntemp.substring(0, altrAligntemp.indexOf("}}")));
				logger.info("font:" + font + ",size:" + size + ",color:" + colortype + ",altrAlign:" + altrAlign);
				if (colortype.equals("0") || colortype.equals("D")) {
					_fontAllCN = new Font(bfChinese, size, font, new BaseColor(0, 0, 0));
				} else if (colortype.equals("1") || colortype.equals("G")) {
					_fontAllCN = new Font(bfChinese, size, font, new BaseColor(0, 255, 0));
				} else if (colortype.equals("2") || colortype.equals("B")) {
					_fontAllCN = new Font(bfChinese, size, font, new BaseColor(0, 0, 255));
				} else if (colortype.equals("3") || colortype.equals("R")) {
					_fontAllCN = new Font(bfChinese, size, font, new BaseColor(255, 0, 0));
				}
				if (o.indexOf("}}") != -1 && o.length() > o.indexOf("}}") + 2) {
					o = o.substring(o.indexOf("}}") + 2);

					if (!togethchunk) {
						paragraphTemp = new Paragraph(o, _fontAllCN);
						paragraphTemp.setAlignment(altrAlign);
						document.add(paragraphTemp);
					} else {
						phrase0 = new Phrase(new Chunk(o, _fontAllCN));
						document.add(phrase0);
					}
				}
			} else if (o.startsWith("&}}")) { // {{&0,12,0}}
				togethchunk = false;
				if (o.indexOf("}}") != -1 && o.length() > o.indexOf("}}") + 2) {
					o = o.substring(o.indexOf("}}") + 2);
					paragraphTemp = new Paragraph(o, _fontAllCN);
					paragraphTemp.setAlignment(altrAlign);
					document.add(paragraphTemp);
				}
			} else if (o.startsWith("{{&") || togethchunk) { // {{&0,12,0}}
				togethchunk = true;
				if (o.startsWith("{{&")) {
					o = o.substring(3);
				}
				if (o.length() > 0) {
					phrase0 = new Phrase(new Chunk(o, _fontAllCN));
					document.add(phrase0);
				}
			} else if (o.startsWith("{{#") || o.startsWith("{{@")) {
				sFlag = o.substring(0, 3);
				altrAlign = Element.ALIGN_LEFT;
				// 設定字體大小等參數
				if (o.length() > 3) {
					o = o.substring(3);
					array = o.split(",");
					_fontAllCN = processFont(array, altrAlign);
				}
				iFlagCount++;
			} else if (StringUtils.isNotEmpty(sFlag)) {// 如果有FLAG就依照
				String sEndFlag = sFlag.substring(2) + "}}";
				if (sEndFlag.equals(o)) { // 結束
					if (FdpItemParameter.Fdp.pdf_tab.equals(sFlag)) {
						if (document != null && table != null)
							document.add(table);
					}
					sFlag = "";
					iFlagCount = 0;// 初始化移動到1
					_fontAllCN = new Font(bfChinese, 12, Font.NORMAL, new BaseColor(0, 0, 0));
					altrAlign = Element.ALIGN_LEFT;
				} else {
					if (FdpItemParameter.Fdp.email_Start.equals(sFlag)) {// EMAIL解析
						if (iFlagCount == 1) {
							pEmailInfo.setProperty("subject", o);
						} else if (iFlagCount == 2) {
							pEmailInfo.setProperty("sender", o);
						} else if (iFlagCount == 3) {
							if (o.isEmpty()) {
								o = targetFilePath;
							}
							logger.info("file o :" + o);
							pEmailInfo.setProperty("file", o);
						} else {
							Joiner joiner = Joiner.on("\n\r ").skipNulls();
							String sBody = "";
							sBody = joiner.join(o, null).trim() + "\r\n";
							sBody = pEmailInfo.getProperty("body", "") + sBody;
							pEmailInfo.setProperty("body", sBody);
						}
					} else if (FdpItemParameter.Fdp.pdf_tab.equals(sFlag)) {
						if (iFlagCount == 1) {// table參數
							table = processTable(o.split("\\|\\|"));
						} else if (iFlagCount == 2) { // header
							array = o.split("\\|\\|");
							for (int i = 0; i < array.length; i++) {
								// PdfPCell gcell = new PdfPCell(new Phrase(array[i],
								// _fontTable0));
								// gcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								table.addCell(new Phrase(array[i], _fontTable0));
								table.setHeaderRows(0);
							}
						} else {
							array = o.split("\\|\\|");
							for (int i = 0; i < array.length; i++) {
								table.addCell(new Phrase(array[i], _fontAllCN));
							}
						}
					}
					iFlagCount++;
				}
			} else {
				paragraphTemp = new Paragraph(o, _fontAllCN);
				paragraphTemp.setAlignment(altrAlign);
				document.add(paragraphTemp);
			}
			logger.info("out string:" + o);
		}

		// 加入文件尾
		addFooter(document);

	}

	/**
	 * 固定加入最後的段落
	 * 
	 * @param document
	 * @throws DocumentException
	 */
	private static void addFooter(Document document) throws DocumentException {
		Paragraph para = new Paragraph();
		Phrase phrase0 = new Phrase(new Chunk("#合作金庫銀行 Taiwan Cooperative Bank,", _fontFooter));
		para.add(phrase0);
		// 超連結
		Anchor anchor = new Anchor("https://www.tcb-bank.com.tw",
				FontFactory.getFont(FontFactory.HELVETICA, 10, Font.UNDERLINE, new BaseColor(0, 0, 255)));
		anchor.setReference("https://www.tcb-bank.com.tw");
		para.add(anchor);
		para.setAlignment(Element.ALIGN_CENTER);
		document.add(para);
	}

	/**
	 * 換行
	 * 
	 * @param paragraph
	 * @param number
	 */
	private static void addEmptyLine(int number) {
		logger.info("number:" + number);
		if (number != 0) {
			for (int i = 0; i < number; i++) {
				try {
					document.add(new Paragraph(" ", emptyfont));
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					logger.error("error about addEmptyLine:", e);
				}
			}
		}
	}

	/**
	 * 增加空白
	 * 
	 * @param blank
	 * @return
	 */
	private static String addBlank(int blank) {
		StringBuilder bu = new StringBuilder();
		if (blank > 0) {
			for (int i = 0; i <= blank; i++) {
				bu.append(" ");
			}
		}
		return bu.toString();
	}

	/**
	 * 文件內容部分
	 * 
	 * @param document
	 */
	private static void addMetaDataTitle() {

		// 增加標題
		document.addTitle("IMS Create PDF");
		// 增加作者
		document.addAuthor("合庫-中心主機");
		// 增加建立PDF時間以及修改PDF日期
		document.addCreationDate();
		// 增加PDF中的關鍵字
		// document.addKeywords("關鍵字");
		// 增加PDF的主題
		document.addSubject("PDF");
		// 增加自訂內容
		// document.addHeader("PDF1", "測試1");
		// document.addHeader("PDF2", "測試2");

	}

	// 表格數量,總寬,各格子寬度...
	private static PdfPTable processTable(String[] saData) {
		PdfPTable table = new PdfPTable(Integer.parseInt(saData[0]));
		int[] widths = new int[saData.length - 2];
		int totalwidths = Integer.parseInt(saData[1]);
		for (int i = 2; i < saData.length; i++) {
			widths[i - 2] = Integer.parseInt(saData[i]);
		}
		// int[] widths = {Integer.parseInt(saData[1]),Integer.parseInt(saData[2])} ;
		// //percentage
		try {
			table.setWidths(widths);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			logger.error("error about setWidths:", e);
		} // 设置列宽度
		// 边框属性
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setBorderColor(new BaseColor(0, 0, 0)); // 边框颜色
		table.getDefaultCell().setPadding(2); // space between content and
												// border
		table.getDefaultCell().setSpaceCharRatio(1); // Sets the ratio between the
														// extra
														// word spacing and the extra
														// character spacing when the
														// text
														// is fully justified.
		// table.getDefaultCell().setBorderWidth(1); //Sets the borderwidth of the
		// table.

		// KE 設定
		table.setTotalWidth(totalwidths);
		table.setLockedWidth(true);
		return table;
	}

	private static Font processFont(String[] saData, int altrAlign) {
		Font fontAllCN = new Font();
		String colortype;
		altrAlign = Element.ALIGN_LEFT;
		String altrAligntemp;
		int font, size;

		font = Integer.parseInt(saData[0]);// 0正常,1粗體,2斜體
		size = Integer.parseInt(saData[1]);// 文字大小
		colortype = saData[2].toString();// 顏色0D黑色,1G綠色,2B藍色,3R紅色
		altrAligntemp = saData[3].toString();// 文字位置LEFT 0, CENTER 1
		logger.info("end }} indexof:" + altrAligntemp.indexOf("}}"));
		altrAlign = Integer.parseInt(altrAligntemp);
		logger.info("font:" + font + ",size:" + size + ",color:" + colortype + ",altrAlign:" + altrAlign);
		if (colortype != null) {
			if (colortype.equals("0") || colortype.equals("D")) {
				fontAllCN = new Font(bfChinese, size, font, new BaseColor(0, 0, 0));
			} else if (colortype.equals("1") || colortype.equals("G")) {
				fontAllCN = new Font(bfChinese, size, font, new BaseColor(0, 255, 0));
			} else if (colortype.equals("2") || colortype.equals("B")) {
				fontAllCN = new Font(bfChinese, size, font, new BaseColor(0, 0, 255));
			} else if (colortype.equals("3") || colortype.equals("R")) {
				fontAllCN = new Font(bfChinese, size, font, new BaseColor(255, 0, 0));
			} else {
				fontAllCN = new Font(bfChinese, size, font, new BaseColor(0, 0, 0));
			}
		}
		return fontAllCN;
	}

	// public static void main(String argv[])
	public static void main(String argv[]) {
		;
	}

}
