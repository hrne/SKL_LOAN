package com.st1.itx.util.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.config.AstrMapper;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.filter.SafeClose;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.eum.ContentName;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.List;
import java.io.*;
import java.nio.file.Files;

/**
 * 
 * ----------------------- MakeFile 產生檔案(TXT,CSV)共用工具 ------------------*
 * 
 * @author eric chang
 *
 */

@Component("makeFile")
@Scope("prototype")

public class MakeFile extends CommBuffer {

	/* DB服務注入 */
	@Autowired
	TxFileService sTxFileService;

	@Autowired
	private AstrMapper astrMapper;

	// 檔案輸出路徑
	@Value("${iTXOutFolder}")
	private String outputFolder = "";

	// 資料明細
	List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

	// 檔案日期
	private String BrNo;

	// 檔案日期
	private int fileDate;

	// 檔案編號
	private String fileCode;

	// 檔案說明
	private String fileItem;

	// 輸出檔案名稱
	private String fileName;

	// 輸出檔案格式
	private int fileFormat;

	/**
	 * 開始製檔<br>
	 * 
	 * @param titaVo   titaVo
	 * @param date     日期
	 * @param brno     單位
	 * @param fileCode 檔案編號
	 * @param fileItem 檔案說明
	 * @param fileName 輸出檔案名稱
	 * @param format   輸出檔案格式 1.UTF8 2.BIG5
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, int date, String brno, String fileCode, String fileItem, String fileName, int format) throws LogicException {

		this.titaVo = titaVo;
		this.init(date, brno, fileCode, fileItem, fileName, format);

		listMap = new ArrayList<HashMap<String, Object>>();

	}

	/**
	 * 開始製檔<br>
	 * 
	 * @param titaVo   titaVo
	 * @param date     日期
	 * @param brno     單位
	 * @param fileCode 檔案編號
	 * @param fileItem 檔案說明
	 * @param fileName 輸出檔案名稱
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, int date, String brno, String fileCode, String fileItem, String fileName) throws LogicException {

		this.titaVo = titaVo;
		this.init(date, brno, fileCode, fileItem, fileName, 1);
	}

	private void init(int date, String brno, String fileCode, String fileItem, String fileName, int format) throws LogicException {
		if ("".equals(brno)) {
			throw new LogicException("EC007", "(MakeFile)取檔單位不可為空白");
		}
		this.BrNo = brno;

		if (date == 0) {
			throw new LogicException("EC007", "(MakeFile)檔案日期不可為０");
		}
		this.fileDate = date;

		if ("".equals(fileCode)) {
			throw new LogicException("EC007", "(MakeFile)檔案編號不可空白");
		}

		if (haveChinese(fileCode)) {
			throw new LogicException("EC007", "(MakeFile)檔案編號不可有全形字");
		}

		this.fileCode = fileCode;

		if ("".equals(fileItem)) {
			throw new LogicException("EC007", "(MakeFile)檔案說明不可空白");
		}
		this.fileItem = fileItem;

		if ("".equals(fileName)) {
			throw new LogicException("EC007", "(MakeFile)輸出檔案名稱不可空白");
		}
		this.fileName = fileName;

		if (format != 1 && format != 2) {
			throw new LogicException("EC007", "(MakeFile)輸出檔案格式不可為" + format);
		}
		this.fileFormat = format;

	}

	/**
	 * 放入資料<br>
	 * 
	 * @param data 資料
	 * @throws LogicException LogicException
	 */
	public void put(String data) throws LogicException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("d", data);
		listMap.add(map);
	}

	/**
	 * 取固定長度字串，中文以2位計算長度<br>
	 * 
	 * @param string 字串
	 * @param length 字串長度
	 * @return String 字串
	 */
	public String cutString(String string, int length) {

		String rs = "";
		int tlen = 0;
		int clen = 0;
		for (int i = 0; i < string.length(); i++) {
			String c = string.substring(i, i + 1);
			if (c.matches("[\\u0391-\\uFFE5]+")) {
				clen = 2;
			} else {
				clen = 1;
			}
			if (tlen + clen == length) {
				rs += c;
				tlen += clen;
				break;
			} else if (tlen + clen > length) {
				break;
			} else {
				rs += c;
				tlen += clen;
			}

		}

		return rs;

	}

	/**
	 * 取固定長度字串，中文以2位計算長度，不足長度右側會自動補指定字元<br>
	 * 
	 * @param string   字串
	 * @param length   字串長度
	 * @param fillchar 填補字元
	 * @return String 字串
	 */
	public String fillStringR(String string, int length, char fillchar) {

		return this.fillString(string, length, fillchar, true);
	}

	/**
	 * 取固定長度字串，中文以2位計算長度，不足長度右側會自動補空白<br>
	 * 
	 * @param string 字串
	 * @param length 字串長度
	 * @return String 字串
	 */
	public String fillStringR(String string, int length) {

		return this.fillString(string, length, ' ', true);
	}

	/**
	 * 取固定長度字串，中文以2位計算長度，不足長度左側會自動補指定字元<br>
	 * 
	 * @param string   字串
	 * @param length   字串長度
	 * @param fillchar 字串長度
	 * @return String 字串
	 */
	public String fillStringL(String string, int length, char fillchar) {

		return this.fillString(string, length, fillchar, false);
	}

	/**
	 * 取固定長度字串，中文以2位計算長度，不足長度左側會自動補空白<br>
	 * 
	 * @param string 字串
	 * @param length 字串長度
	 * @return String 字串
	 */
	public String fillStringL(String string, int length) {

		return this.fillString(string, length, ' ', false);
	}

	private String fillString(String string, int length, char fillchar, boolean right) {
		String rs = "";
		int tlen = 0;
		int clen = 0;
		for (int i = 0; i < string.length(); i++) {
			String c = string.substring(i, i + 1);
			if (c.matches("[\\u0391-\\uFFE5]+")) {
				clen = 2;
			} else {
				clen = 1;
			}
			if (tlen + clen == length) {
				rs += c;
				tlen += clen;
				break;
			} else if (tlen + clen > length) {
				break;
			} else {
				rs += c;
				tlen += clen;
			}

		}

		if (length > tlen) {
			for (int i = 0; i < (length - tlen); i++) {
				if (right) {
					rs += fillchar;
				} else {
					rs = fillchar + rs;
				}

			}
		}

		return rs;

	}

	/**
	 * 結束製檔<br>
	 * 
	 * @return long 檔案序號
	 * @throws LogicException LogicException
	 */
	public long close() throws LogicException {

		TxFile tTxFile = new TxFile();

		tTxFile.setBrNo(this.BrNo);
		tTxFile.setFileDate(this.fileDate);
		tTxFile.setFileCode(this.fileCode);
		tTxFile.setFileItem(this.fileItem);
		tTxFile.setFileFormat(this.fileFormat);
		tTxFile.setFileOutput(this.fileName);

		int fileType = 3;
		if (this.fileName.lastIndexOf(".") > 0) {
			String subFileName = this.fileName.substring(this.fileName.lastIndexOf(".") + 1).toUpperCase();

			switch (subFileName) {
			case "TXT":
				fileType = 3;
				break;
			case "DBF":
				fileType = 4;
				break;
			case "CSV":
				fileType = 5;
				break;
			default:
				fileType = 3;
				break;
			}
		}
		tTxFile.setFileType(fileType); // 根據副檔名判斷 3:TXT 、 4:DBF 或 5:CSV (若無副檔名時預設3:TXT)
		try {
			ObjectMapper mapper = new ObjectMapper();
			tTxFile.setFileData(mapper.writeValueAsString(listMap));
		} catch (IOException e) {
			throw new LogicException("EC009", "(MakeFile)資料格式");
		}

		// 寫Txfile時需寫回onlineDB,但交易用的titaVo應維持原指向的DB
		TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();

		tmpTitaVo.putParam(ContentName.dataBase, ContentName.onLine);

		try {
			tTxFile = sTxFileService.insert(tTxFile, tmpTitaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "EC002", "(MakeFile)輸出檔(TxFile):" + e.getErrorMsg());
		}

		return tTxFile.getFileNo();
	}

	/**
	 * 將指定檔案序號產製檔案<br>
	 * 
	 * @param fileno 檔案序號
	 * @throws LogicException LogicException
	 */
	public void toFile(long fileno) throws LogicException {
		doToFile(fileno, "");
	}

	/**
	 * 將指定檔案序號產製檔案<br>
	 * 
	 * @param fileno   檔案序號
	 * @param fileName 指定輸出檔名
	 * @throws LogicException LogicException
	 */

	public void toFile(long fileno, String fileName) throws LogicException {
		doToFile(fileno, fileName);
	}

	@SuppressWarnings("unchecked")
	private void doToFile(long fileno, String fileName) throws LogicException {
		this.info("MakeFile.toFile=" + fileno);

		TxFile tTxFile = sTxFileService.findById(fileno);

		if (tTxFile == null) {
			throw new LogicException(titaVo, "EC002", "(MakeFile)輸出檔(TxFile)序號:" + fileno);
		}

		// 2021-01-18 會大量顯示，所以先關閉 (陳志嵩)
//		this.info("MakeFile.toFile.filedata=" + tTxFile.getFileData());

		try {
			this.listMap = new ObjectMapper().readValue(tTxFile.getFileData(), ArrayList.class);
		} catch (IOException e) {
			throw new LogicException("EC009", "(MakeFile)輸出檔(TxFile)序號:" + fileno + ",資料格式");
		}

		String outfile = outputFolder + tTxFile.getFileOutput();
		if (!"".equals(fileName)) {
			outfile = outputFolder + fileName;
		}
		// 先刪除舊檔
		File file = new File(outfile);

		try {
			Files.delete(file.toPath());
		} catch (IOException e) {
			this.info("MakeFile Files.delete error =" + e.getMessage());
		}

		String charsetName = "";
		if (tTxFile.getFileFormat() == 2) {
			charsetName = "big5";
		} else {
			charsetName = "utf-8";
		}

		// 產製新檔
		FileOutputStream fo = null;
		OutputStreamWriter osw = null;
		BufferedWriter fw = null;
		try {
			this.info("MakeFile.toFile outfile=" + outfile + "/" + charsetName);
			fo = new FileOutputStream(outfile, true);
			osw = new OutputStreamWriter(fo, charsetName);
			fw = new BufferedWriter(osw);
			this.info("MakeFile.toFile opened");

			for (HashMap<String, Object> map : listMap) {
				if (charsetName.toUpperCase(Locale.getDefault()).equals("BIG5")) {
					String[] ss = map.get("d").toString().split("");
					for (String s : ss)
						if (new String(s.getBytes(charsetName), "UTF-8").equals("?"))
							fw.write(astrMapper.getMapperChar(s.toCharArray()[0]));
						else
							fw.write(new String(s.getBytes(charsetName), charsetName.toUpperCase(Locale.getDefault())));
					fw.write("\r\n");
				} else
					fw.write(map.get("d").toString() + "\r\n");
			}

			this.info("MakeFile.toFile listmap");
			fw.flush();
			this.info("MakeFile.toFile flush");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("MakeFile  IOException error = " + e.toString());
			throw new LogicException("EC009", "(MakeFile)輸出檔(TxFile)序號:" + fileno + ",產檔失敗");
		} finally {
			SafeClose.close(fw);
			SafeClose.close(osw);
			SafeClose.close(fo);
		}

//		if (this.listMap != null) {
//			showListMap(this.listMap);
//		}
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

	@Override
	public void exec() throws LogicException {
		// override this

	}

}
