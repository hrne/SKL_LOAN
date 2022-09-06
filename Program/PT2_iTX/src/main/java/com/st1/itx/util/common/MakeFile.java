package com.st1.itx.util.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.report.ReportUtil;

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
	private TxFileService sTxFileService;

	@Autowired
	private ReportUtil reportUtil;

	@Autowired
	private FileGenerator fileGenerator;

	// 資料明細
	private List<Map<String, Object>> listMap;

	// 輸出檔案名稱
	private String fileName;

	// 輸出檔案格式
	private int fileFormat;

	private ReportVo reportVo;

	/**
	 * 開始製檔<br>
	 * 
	 * @param titaVo   titaVo
	 * @param reportVo reportVo
	 * @param fileName 輸出檔案名稱
	 * @param format   輸出檔案格式 1.UTF8 2.BIG5
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, ReportVo reportVo, String fileName, int format) throws LogicException {
		this.titaVo = titaVo;
		this.init(reportVo, fileName, format);
	}

	/**
	 * 開始製檔<br>
	 * 
	 * @param titaVo   titaVo
	 * @param reportVo reportVo
	 * @param fileName 輸出檔案名稱
	 * @throws LogicException LogicException
	 */
	public void open(TitaVo titaVo, ReportVo reportVo, String fileName) throws LogicException {
		this.titaVo = titaVo;
		this.init(reportVo, fileName, 1);
	}

	/**
	 * 開始製檔<br>
	 * 
	 * @deprecated use
	 *             {@link #open(TitaVo titaVo, ReportVo reportVo, String fileName, int format)}
	 *             instead.
	 * @param titaVo   titaVo
	 * @param date     日期
	 * @param brno     單位
	 * @param fileCode 檔案編號
	 * @param fileItem 檔案說明
	 * @param fileName 輸出檔案名稱
	 * @param format   輸出檔案格式 1.UTF8 2.BIG5
	 * @throws LogicException LogicException
	 */
	@Deprecated
	public void open(TitaVo titaVo, int date, String brno, String fileCode, String fileItem, String fileName, int format) throws LogicException {
		this.titaVo = titaVo;
		this.init(date, brno, fileCode, fileItem, fileName, format);
	}

	/**
	 * 開始製檔<br>
	 * 
	 * @deprecated use
	 *             {@link #open(TitaVo titaVo, ReportVo reportVo, String fileName)}
	 *             instead.
	 * @param titaVo   titaVo
	 * @param date     日期
	 * @param brno     單位
	 * @param fileCode 檔案編號
	 * @param fileItem 檔案說明
	 * @param fileName 輸出檔案名稱
	 * @throws LogicException LogicException
	 */
	@Deprecated
	public void open(TitaVo titaVo, int date, String brno, String fileCode, String fileItem, String fileName) throws LogicException {
		this.titaVo = titaVo;
		this.init(date, brno, fileCode, fileItem, fileName, 1);
	}

	private void init(ReportVo reportVo, String fileName, int format) throws LogicException {
		if ("".equals(reportVo.getBrno())) {
			throw new LogicException("EC007", "(MakeFile)取檔單位不可為空白");
		}
		if (reportVo.getRptDate() == 0) {
			throw new LogicException("EC007", "(MakeFile)檔案日期不可為０");
		}
		if ("".equals(reportVo.getRptCode())) {
			throw new LogicException("EC007", "(MakeFile)檔案編號不可空白");
		}
		if (reportUtil.haveChinese(reportVo.getRptCode())) {
			throw new LogicException("EC007", "(MakeFile)檔案編號不可有全形字");
		}
		if ("".equals(reportVo.getRptItem())) {
			throw new LogicException("EC007", "(MakeFile)檔案說明不可空白");
		}
		if ("".equals(fileName)) {
			throw new LogicException("EC007", "(MakeFile)輸出檔案名稱不可空白");
		}
		if (format != 1 && format != 2) {
			throw new LogicException("EC007", "(MakeFile)輸出檔案格式不可為" + format);
		}
		this.fileName = fileName;
		this.fileFormat = format;
		this.reportVo = reportVo;
		listMap = new ArrayList<Map<String, Object>>();
	}

	private void init(int date, String brno, String fileCode, String fileItem, String fileName, int format) throws LogicException {
		if ("".equals(brno)) {
			throw new LogicException("EC007", "(MakeFile)取檔單位不可為空白");
		}
		if (date == 0) {
			throw new LogicException("EC007", "(MakeFile)檔案日期不可為０");
		}
		if ("".equals(fileCode)) {
			throw new LogicException("EC007", "(MakeFile)檔案編號不可空白");
		}
		if (reportUtil.haveChinese(fileCode)) {
			throw new LogicException("EC007", "(MakeFile)檔案編號不可有全形字");
		}
		if ("".equals(fileItem)) {
			throw new LogicException("EC007", "(MakeFile)檔案說明不可空白");
		}
		if ("".equals(fileName)) {
			throw new LogicException("EC007", "(MakeFile)輸出檔案名稱不可空白");
		}
		if (format != 1 && format != 2) {
			throw new LogicException("EC007", "(MakeFile)輸出檔案格式不可為" + format);
		}
		this.fileName = fileName;
		this.fileFormat = format;
		listMap = new ArrayList<Map<String, Object>>();
		reportVo = ReportVo.builder().setRptDate(date).setBrno(brno).setRptCode(fileCode).setRptItem(fileItem).build();
	}

	/**
	 * 放入資料<br>
	 * 
	 * @param data 資料
	 * @throws LogicException LogicException
	 */
	public void put(String data) throws LogicException {
		Map<String, Object> map = new HashMap<>();
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

		tTxFile.setBrNo(reportVo.getBrno());
		tTxFile.setFileDate(reportVo.getRptDate());
		tTxFile.setFileCode(reportVo.getRptCode());
		tTxFile.setFileItem(reportVo.getRptItem());
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
		fileGenerator.generateFile(fileno, "");
	}

	/**
	 * 將指定檔案序號產製檔案<br>
	 * 
	 * @param fileno   檔案序號
	 * @param fileName 指定輸出檔名
	 * @throws LogicException LogicException
	 */
	public void toFile(long fileno, String fileName) throws LogicException {
		fileGenerator.generateFile(fileno, fileName);
	}

	@Override
	public void exec() throws LogicException {
		// override this
	}
}