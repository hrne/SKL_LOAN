package com.st1.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadExcelFromFile {
	static final Logger logger = LoggerFactory.getLogger(ReadExcelFromFile.class);

	/* The output stream to write to */
	private String inpath;
	private String outpath;

	/* The encoding to write */
	private String encoding;
	/* The workbook we are reading from */
	private Workbook workbook;
	private File inputWorkbook;
	private File outputHelpfile;
	private FileOutputStream fos;

	/**
	 * Constructor
	 * 
	 * @param w   The workbook to interrogate
	 * @param out The output stream to which the values are written
	 * @param enc The encoding used by the output stream. Null or unrecognized
	 *            values cause the encoding to default to UTF8
	 * @param f   Indicates whether the generated XML document should contain the
	 *            cell format information
	 * @exception java.io.IOException
	 */
	public ReadExcelFromFile() throws IOException {

		/*
		 * if (f) { writeFormattedXML(); }else{ writeXML(); }
		 */
	}

	/**
	 * Writes out the workbook data as XML, without formatting information
	 */
	public void writeHelp(String in, String out, String enc, boolean f) throws IOException, BiffException {
		try {
			encoding = enc;
			inputWorkbook = new File(in);
			outputHelpfile = new File(out);

			fos = new FileOutputStream(outputHelpfile);
			workbook = Workbook.getWorkbook(inputWorkbook);
			encoding = "BIG5";
			// if (encoding == null || !encoding.equals("UnicodeBig")){
			// encoding = "UTF8";
			// }

			OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
			BufferedWriter bw = new BufferedWriter(osw);

			bw.write("var Helpfile = new Array();");
			bw.newLine();
			String fildname[] = new String[100];
			String fildstr[] = new String[100];
			for (int sheet = 0; sheet < workbook.getNumberOfSheets(); sheet++) {
				Sheet s = workbook.getSheet(sheet);
				String sheetname = s.getName();
				String sessname = "";
				String scriptstr = "Helpfile[\"" + sheetname + "\"]=new Array();";
				bw.write(scriptstr);
				bw.newLine();
				Cell[] row = null;
				for (int i = 0; i < s.getRows(); i++) {
					row = s.getRow(i);
					if (row[0].getType() != CellType.EMPTY) {
						sessname = row[0].getContents();
						scriptstr = "Helpfile[\"" + sheetname + "\"][\"" + sessname + "\"]=new Array();";
						bw.write(scriptstr);
						bw.newLine();
						scriptstr = "Helpfile[\"" + sheetname + "\"][\"" + sessname + "\"][\"FILD\"]=new Array(";
						bw.write(scriptstr);
						scriptstr = "";
						fildname = new String[row.length - 1];
						fildstr = new String[row.length - 1];
						for (int j = 1; j < row.length; j++) {
							fildname[j - 1] = row[j].getContents();
							scriptstr = scriptstr + "\"" + row[j].getContents() + "\"";
							if (j < row.length - 1)
								scriptstr = scriptstr + ",";
						}
						bw.write(scriptstr);
						scriptstr = ");";
						bw.write(scriptstr);
						bw.newLine();
					} else {
						for (int j = 0; j < row.length - 1; j++) {
							if (fildstr[j] == null)
								fildstr[j] = "";

							fildstr[j] = fildstr[j] + "\"" + row[j + 1].getContents() + "\",";
						}
					}
					if (i == s.getRows() - 1 || s.getRow(i + 1)[0].getType() != CellType.EMPTY) {
						for (int j = 0; j < row.length - 1; j++) {
							scriptstr = "Helpfile[\"" + sheetname + "\"][\"" + sessname + "\"][\"" + fildname[j] + "\"]=new Array(" + fildstr[j] + ");";
							scriptstr = scriptstr.substring(0, scriptstr.length() - 3) + ");";
							bw.write(scriptstr);
							bw.newLine();
						}
					}
				}
			}
			bw.flush();
			bw.close();
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}

	}

	public static boolean isJSOlder(String excelFile, String jsFile) {
		File f1 = new File(excelFile);
		File f2 = new File(jsFile);

		if (!f2.exists())
			return true;
		Date d1 = new Date(f1.lastModified());
		Date d2 = new Date(f2.lastModified());
		if (d1.compareTo(d2) < 0)
			return false;
		else
			return true;
	}

	private static ReadExcelFromFile myExcel = null;

	public synchronized static void helpFactory(String excelFile, String jsFile, String enc, boolean forceNew) throws Exception {

		if (isJSOlder(excelFile, jsFile)) {
			System.out.println("excel file has been modified, produce new javascript file");
			myExcel = new ReadExcelFromFile();
			String tmpstr = myExcel.storeHelp(excelFile, jsFile, enc, true);
			PoorManFile poor = new PoorManFile(jsFile);
			poor.write(tmpstr);
			System.out.println(jsFile + " generated");
		} else {
			System.out.println("excel file is older than js file, no rebuild");
		}

	}

	private String storeHelp(String in, String out, String enc, boolean f) throws IOException, BiffException {
		try {
			encoding = enc;
			inputWorkbook = new File(in);

			// outputHelpfile = new File(out);

			// fos= new FileOutputStream(outputHelpfile);
			workbook = Workbook.getWorkbook(inputWorkbook);
			encoding = "BIG5";
			// if (encoding == null || !encoding.equals("UnicodeBig")){
			// encoding = "UTF8";
			// }

			// OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
			// BufferedWriter bw = new BufferedWriter(osw);
			StringBuffer bw = new StringBuffer();
			bw.append("var Helpfile = new Array();");
			bw.append("\n");
			String fildname[] = new String[1000];
			String fildstr[] = new String[1000];
			for (int sheet = 0; sheet < workbook.getNumberOfSheets(); sheet++) {
				Sheet s = workbook.getSheet(sheet);
				String sheetname = s.getName();
				String sessname = "";
				String scriptstr = "Helpfile[\"" + sheetname + "\"]=new Array();";
				bw.append(scriptstr + "\n");

				Cell[] row = null;
				// System.out.println("row: " + s.getRows());
				for (int i = 0; i < s.getRows(); i++) {
					row = s.getRow(i);
					if (row[0].getType() != CellType.EMPTY) {
						sessname = row[0].getContents();
						scriptstr = "Helpfile[\"" + sheetname + "\"][\"" + sessname + "\"]=new Array();";
						bw.append(scriptstr + "\n");
						// System.out.println(scriptstr);
						scriptstr = "Helpfile[\"" + sheetname + "\"][\"" + sessname + "\"][\"FILD\"]=new Array(";
						bw.append(scriptstr);
						scriptstr = "";
						fildname = new String[row.length - 1];
						fildstr = new String[row.length - 1];
						for (int j = 1; j < row.length; j++) {
							fildname[j - 1] = row[j].getContents();
							scriptstr = scriptstr + "\"" + row[j].getContents() + "\"";
							if (j < row.length - 1)
								scriptstr = scriptstr + ",";
						}
						// System.out.println("\t"+scriptstr);
						bw.append(scriptstr);
						scriptstr = ");";
						bw.append(scriptstr + "\n");

					} else {
						for (int j = 0; j < row.length - 1; j++) {
							if (fildstr[j] == null)
								fildstr[j] = "";

							fildstr[j] = fildstr[j] + "\"" + row[j + 1].getContents() + "\",";
						}
					}
					if (i == s.getRows() - 1 || s.getRow(i + 1)[0].getType() != CellType.EMPTY) {
						for (int j = 0; j < row.length - 1; j++) {
							scriptstr = "Helpfile[\"" + sheetname + "\"][\"" + sessname + "\"][\"" + fildname[j] + "\"]=new Array(" + fildstr[j] + ");";
							scriptstr = scriptstr.substring(0, scriptstr.length() - 3) + ");";
							bw.append(scriptstr + "\n");
						}
					}
				}
			}

			return bw.toString();
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
			// System.out.println(scriptstr);
			throw e;
		}

	}

	private void writeXML() throws IOException {
		/*
		 * try {
		 * 
		 * OutputStreamWriter osw = new OutputStreamWriter(out, encoding);
		 * BufferedWriter bw = new BufferedWriter(osw);
		 * 
		 * bw.write("<?xml version=\"1.0\" ?>"); bw.newLine();
		 * bw.write("<!DOCTYPE workbook SYSTEM \"workbook.dtd\">"); bw.newLine();
		 * bw.newLine(); bw.write("<workbook>"); bw.newLine(); for (int sheet = 0; sheet
		 * < workbook.getNumberOfSheets(); sheet++) { Sheet s =
		 * workbook.getSheet(sheet);
		 * 
		 * bw.write("  <sheet>"); bw.newLine();
		 * bw.write("    <name><![CDATA["+s.getName()+"]]></name>"); bw.newLine();
		 * 
		 * Cell[] row = null;
		 * 
		 * for (int i = 0 ; i < s.getRows() ; i++) { bw.write("    <row number=\"" + i +
		 * "\">"); bw.newLine(); row = s.getRow(i);
		 * 
		 * for (int j = 0 ; j < row.length; j++) { if (row[j].getType() !=
		 * CellType.EMPTY) { bw.write("      <col number=\"" + j + "\">");
		 * bw.write("<![CDATA["+row[j].getContents()+"]]>"); bw.write("</col>");
		 * bw.newLine(); } } bw.write("    </row>"); bw.newLine(); }
		 * bw.write("  </sheet>"); bw.newLine(); }
		 * 
		 * bw.write("</workbook>"); bw.newLine();
		 * 
		 * bw.flush(); bw.close(); } catch (UnsupportedEncodingException e){
		 * logger.error(e.toString()); }
		 */
	}

	/**
	 * Writes out the workbook data as XML, with formatting information
	 */
	private void writeFormattedXML() throws IOException {
		/*
		 * try{ OutputStreamWriter osw = new OutputStreamWriter(out, encoding);
		 * BufferedWriter bw = new BufferedWriter(osw);
		 * 
		 * bw.write("<?xml version=\"1.0\" ?>"); bw.newLine();
		 * bw.write("<!DOCTYPE workbook SYSTEM \"formatworkbook.dtd\">"); bw.newLine();
		 * bw.newLine(); bw.write("<workbook>"); bw.newLine(); for (int sheet = 0; sheet
		 * < workbook.getNumberOfSheets(); sheet++){ Sheet s = workbook.getSheet(sheet);
		 * 
		 * bw.write("  <sheet>"); bw.newLine();
		 * bw.write("    <name><![CDATA["+s.getName()+"]]></name>"); bw.newLine();
		 * 
		 * Cell[] row = null; CellFormat format = null; Font font = null;
		 * 
		 * for (int i = 0 ; i < s.getRows() ; i++){ bw.write("    <row number=\"" + i +
		 * "\">"); bw.newLine(); row = s.getRow(i);
		 * 
		 * for (int j = 0 ; j < row.length; j++){ // Remember that empty cells can
		 * contain format information if ((row[j].getType() != CellType.EMPTY) ||
		 * (row[j].getCellFormat() != null)) { format = row[j].getCellFormat();
		 * bw.write("      <col number=\"" + j + "\">"); bw.newLine();
		 * bw.write("        <data>"); bw.write("<![CDATA["+row[j].getContents()+"]]>");
		 * bw.write("</data>"); bw.newLine();
		 * 
		 * if (row[j].getCellFormat() != null) { bw.write("        <format wrap=\"" +
		 * format.getWrap() + "\""); bw.newLine(); bw.write("                align=\"" +
		 * format.getAlignment().getDescription() + "\""); bw.newLine();
		 * bw.write("                valign=\"" +
		 * format.getVerticalAlignment().getDescription() + "\""); bw.newLine();
		 * bw.write("                orientation=\"" +
		 * format.getOrientation().getDescription() + "\""); bw.write(">");
		 * bw.newLine();
		 * 
		 * // The font information font = format.getFont();
		 * bw.write("          <font name=\"" + font.getName() + "\""); bw.newLine();
		 * bw.write("                point_size=\"" + font.getPointSize() + "\"");
		 * bw.newLine(); bw.write("                bold_weight=\"" +
		 * font.getBoldWeight() + "\""); bw.newLine();
		 * bw.write("                italic=\"" + font.isItalic() + "\""); bw.newLine();
		 * bw.write("                underline=\"" +
		 * font.getUnderlineStyle().getDescription() + "\""); bw.newLine();
		 * bw.write("                colour=\"" + font.getColour().getDescription() +
		 * "\""); bw.newLine(); bw.write("                script=\"" +
		 * font.getScriptStyle().getDescription() + "\""); bw.write(" />");
		 * bw.newLine();
		 * 
		 * // The cell background information if (format.getBackgroundColour() !=
		 * Colour.DEFAULT_BACKGROUND || format.getPattern() != Pattern.NONE){
		 * bw.write("          <background colour=\"" +
		 * format.getBackgroundColour().getDescription() + "\""); bw.newLine();
		 * bw.write("                      pattern=\"" +
		 * format.getPattern().getDescription() + "\""); bw.write(" />"); bw.newLine();
		 * }
		 * 
		 * // The cell border, if it has one if (format.getBorder(Border.TOP ) !=
		 * BorderLineStyle.NONE || format.getBorder(Border.BOTTOM) !=
		 * BorderLineStyle.NONE || format.getBorder(Border.LEFT) != BorderLineStyle.NONE
		 * || format.getBorder(Border.RIGHT) != BorderLineStyle.NONE) {
		 * bw.write("          <border top=\"" +
		 * format.getBorder(Border.TOP).getDescription() + "\""); bw.newLine();
		 * bw.write("                  bottom=\"" +
		 * format.getBorder(Border.BOTTOM).getDescription() + "\""); bw.newLine();
		 * bw.write("                  left=\"" +
		 * format.getBorder(Border.LEFT).getDescription() + "\""); bw.newLine();
		 * bw.write("                  right=\"" +
		 * format.getBorder(Border.RIGHT).getDescription() + "\""); bw.write(" />");
		 * bw.newLine(); }
		 * 
		 * // The cell number/date format if
		 * (!format.getFormat().getFormatString().equals("")) {
		 * bw.write("          <format_string string=\"");
		 * bw.write(format.getFormat().getFormatString()); bw.write("\" />");
		 * bw.newLine(); }
		 * 
		 * bw.write("        </format>"); bw.newLine(); }
		 * 
		 * bw.write("      </col>"); bw.newLine(); } } bw.write("    </row>");
		 * bw.newLine(); } bw.write("  </sheet>"); bw.newLine(); }
		 * 
		 * bw.write("</workbook>"); bw.newLine();
		 * 
		 * bw.flush(); bw.close(); } catch (UnsupportedEncodingException e){
		 * logger.error(e.toString()); }
		 */
	}

	public static String getFileTimeStamp(File ff) {
		return new Date(ff.lastModified()).toString();
	}

	public static boolean isOlder(File f1, File f2) {
		Date d1 = new Date(f1.lastModified());
		Date d2 = new Date(f2.lastModified());
		if (d1.compareTo(d2) < 0)
			return true;
		else
			return false;
	}

	public static void main(String[] args) throws Exception {

		String helpJS = "d:/ifxfolder/runtime/props/HELP.js";
		String helpXls = "d:/ifxfolder/runtime/props/HELP.xls";
		System.out.println("excel:" + helpXls);
		System.out.println("js:" + helpJS);
		ReadExcelFromFile.helpFactory(helpXls, helpJS, "BIG5", false);

	}

}
