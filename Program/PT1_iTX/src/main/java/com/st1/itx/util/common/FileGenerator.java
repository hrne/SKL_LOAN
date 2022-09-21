package com.st1.itx.util.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.config.AstrMapper;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.filter.SafeClose;

@Component
@Scope("prototype")
public class FileGenerator extends CommBuffer {

	// 檔案輸出路徑
	@Value("${iTXOutFolder}")
	private String outputFolder = "";

	/* DB服務注入 */
	@Autowired
	private TxFileService sTxFileService;

	@Autowired
	private AstrMapper astrMapper;

	// 資料明細
	private List<Map<String, Object>> listMap;

	private long fileNo;

	private String outputFile;

	private String charsetName;

	public void generateFile(long fileNo, String fileName) throws LogicException {
		this.info("generateFile=" + fileNo);

		this.fileNo = fileNo;

		settingFromTxFile(fileName);

		// 產製新檔
		FileOutputStream fo = null;
		BufferedOutputStream bos = null;
		try {
			this.info("outfile=" + outputFile + "/" + charsetName);
			fo = new FileOutputStream(outputFile, true);
			bos = new BufferedOutputStream(fo);
			this.info("opened");

			byte[] sl = new byte[2];
			sl[0] = (byte) 0xA1;
			sl[1] = (byte) 0xFE;
			String row;
			for (Map<String, Object> map : listMap) {
				row = map.get("d").toString();
				if (charsetName.toUpperCase(Locale.getDefault()).equals("BIG5")) {
					String[] ss = row.split("");
					for (String s : ss) {
						if (new String(s.getBytes(charsetName), "UTF-8").equals("?"))
							bos.write(astrMapper.getMapperChar(s.toCharArray()[0]));
						else
							bos.write(s.equals("＋") || s.equals("－") || s.equals("＊") || s.equals("／")   ? sl : s.getBytes(charsetName));
					}
					bos.write("\r\n".getBytes(charsetName));
				} else
					bos.write((row + "\r\n").getBytes(charsetName));
			}
			this.info("listmap");
			bos.flush();
			this.info("flush");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("IOException error = " + e.toString());
			throw new LogicException("EC009", "(FileGenerator)輸出檔(TxFile)序號:" + fileNo + ",產檔失敗");
		} finally {
			SafeClose.close(bos);
			SafeClose.close(fo);
		}
	}

	@SuppressWarnings("unchecked")
	private void settingFromTxFile(String fileName) throws LogicException {
		TxFile tTxFile = sTxFileService.findById(fileNo);
		if (tTxFile == null) {
			throw new LogicException(titaVo, "EC002", "(FileGenerator)輸出檔(TxFile)序號:" + fileNo);
		}
		try {
			this.listMap = new ObjectMapper().readValue(tTxFile.getFileData(), ArrayList.class);
		} catch (IOException e) {
			throw new LogicException("EC009", "(FileGenerator)輸出檔(TxFile)序號:" + fileNo + ",資料格式");
		}
		outputFile = outputFolder + tTxFile.getFileOutput();
		if (!"".equals(fileName)) {
			outputFile = outputFolder + fileName;
		}
		// 先刪除舊檔
		File file = new File(outputFile);
		try {
			Files.delete(file.toPath());
		} catch (IOException e) {
			this.info("Files.delete error =" + e.getMessage());
		}
		charsetName = "";
		if (tTxFile.getFileFormat() == 2) {
			charsetName = "big5";
		} else {
			charsetName = "utf-8";
		}
	}

	@Override
	public void exec() throws LogicException {
		// nothing
	}
}
