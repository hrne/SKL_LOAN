package com.st1.ifx.file.fdp.item;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

@Component
@Scope("prototype")
public class RepConverter {
	private static final Logger logger = LoggerFactory.getLogger(RepConverter.class);

	@Value("${import.repbox}")
	private String outputrepFolder;

	@Value("${import.copytoFolder}")
	private String copytoFolder;

	@Value("${import.backupFolder}")
	private String backupFolder;

	@Value("${import.convertRepToExt}")
	private String extName = ".rpt";

	@ServiceActivator
	public void convert(Message msg) throws Throwable {
		logger.info("converting msg:{}", msg);
		File file = (File) msg.getPayload();
		logger.info(FilterUtils.escape("convert " + file.getAbsolutePath() + " to " + outputrepFolder));

		convertTorep(file.getAbsolutePath());
		moveFileToBackupFolder(file.getAbsolutePath());
	}

	private void convertTorep(String filePath) throws Exception {
		logger.info("convertTorep filePath:" + filePath);
		String name = FilenameUtils.getBaseName(filePath);

		List<String> lines = new ArrayList<String>();

		BufferedReader br = null;
		File f = null;
		FileInputStream fis = null;
		String line = "";
		try {

			f = new File(filePath);
			fis = new FileInputStream(f);
			// java.io.FileNotFoundException
			logger.info("BufferedReader BIG5..");
			br = new BufferedReader(new InputStreamReader(fis, "BIG5")); // MFT只有BIG5
			// java.io.UnsupportedEncodingException
			// java.io.IOException
			logger.info("br.ready1? " + br.ready());
			int dowait = 0;
			boolean iswait = false;
			if (br.ready() != true) {
				logger.info("Thread.sleep first!");
				Thread.sleep(5000);
			}
			while (br.ready() != true || dowait >= 3) {
				iswait = true;
				logger.info("br.wait!!!");
				br.wait(10, 200000);
				dowait++;
			}

			if (iswait) {
				br.notify();
			}

			logger.info("br.ready2? " + br.ready());
			while ((line = br.readLine()) != null) {
				logger.info("List.L:" + line);
				lines.add(line);
			}
			logger.info("after readLine .");

		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			SafeClose.close(br);
			SafeClose.close(fis);
		}
		// BufferedWriter fw = null;
		// File tmpFile = new File(tmpFilePath);
		logger.info("RepConverter START");
		logger.info("lines.Size:" + lines.size());
		addContent(lines, name);
		logger.info("RepConverter done ");
		// FileUtils.moveFile(tmpFile, new File(targetFilePath));

	}

	private void moveFileToBackupFolder(String filePath) {
		// errorFolder
		String fileName = FilenameUtils.getName(filePath);
		String newFilePath = FilenameUtils.concat(backupFolder, fileName);
		File f = new File(newFilePath);
		if (f.exists()) {
			logger.warn("convertTorep " + newFilePath + " exists, delete it");
			f.delete();
		}

		try {
			FileUtils.moveFileToDirectory(new File(FilterUtils.filter(filePath)),
					new File(FilterUtils.filter(backupFolder)), true);
		} catch (IOException e) {
			logger.error("convertTorep move file", e);
		}

		logger.info("convertTorep " + fileName + " is moved to " + newFilePath);
	}

	/**
	 * REPORT 內容部分
	 */

	/**
	 * TPSTD11.配合民國一百年新增，對照TPSTD01; 2007/05/30
	 * 原報表抬頭部份(H-REPORT-NAME)為40BYTES;現改為60BYTES 原報表年月日之年(H-DATE-YY)改為3BYTES 01
	 * HEAD-1. 02 FILLER PIC X(01) VALUE SPACE. 02 H-BRANCH-ID PIC X(04) VALUE
	 * SPACE. 02 H-BRANCH-NAME PIC X(10) VALUE SPACE. 02 H-REPORT-NAME PIC X(60)
	 * VALUE SPACE. 02 H-EXPIRED-DAYS PIC X(14) VALUE SPACE. 02 H-BRANCH-AREA-NAME
	 * PIC X(08) VALUE SPACE.
	 * 
	 * 01 HEAD-2. 02 FILLER PIC X(01) VALUE SPACE. 02 H-REPORT-ID PIC X(07) VALUE
	 * SPACE. 02 H-DATE. 03 H-DATE-YY PIC X(3) VALUE SPACE JUST RIGHT. 03 H-DATE-MM
	 * PIC X(2) VALUE SPACE. 03 H-DATE-DD PIC X(2) VALUE SPACE. 02 H-PAGE-NO PIC
	 * 9(04) VALUE ZEROS. 02 H-ACC-NO PIC X(03) VALUE SPACE. 02 H-ACC-NAME PIC X(14)
	 * VALUE SPACE. 2009/5/21版本控制，印表的日期時間，程式須加入下列一行 2009/5/21 MOVE FUNCTION
	 * CURRENT-DATE (1:14) TO H-PRINT-TIME. 02 H-PRINT-TIME PIC X(14) VALUE SPACE.
	 * 
	 */
	private void addContent(List<String> list, String filename) {
		logger.info("Start addContent list...");

		// Date - reportName
		String[] filenm = filename.split("-");

		File targetfilepath = null;
		FileOutputStream fout = null;
		OutputStreamWriter osw = null;
		BufferedWriter fw = null;
		try {
			for (String o : list) {
				if (o.startsWith("1")) {
					Boolean newline = false;
					// 再次進入時先結束上次狀態
					if (fw != null) {
						fw.flush();
						SafeClose.close(fw);
					}
					// 依 分行 日期 業務 做分類
					String targetpath = FilenameUtils.concat(outputrepFolder,
							o.substring(2, 6) + "/" + filenm[0] + "/" + filenm[1].substring(0, 2)); // 一定超過6就不先檢查長度再substring了
					File uploadFilePath = new File(targetpath);
					if (!uploadFilePath.exists()) {
						uploadFilePath.mkdirs();
						uploadFilePath.setExecutable(true, false);
						uploadFilePath.setWritable(true, false);
						uploadFilePath.setReadable(true, false);
						logger.info("路徑不存在,但是已經成功創建了" + targetpath);
					} else {
						logger.info("文件路徑存在" + targetpath);
					}
					logger.info("報表檔案路徑:" + targetpath);
					logger.info("報表檔案名稱:" + FilterUtils.escape(filenm[1] + extName));
					// 檔名是否需要內容第一行之中文報表檔名? 規格為何?
					targetfilepath = new File(FilenameUtils.concat(targetpath, filenm[1] + extName));
					// 如果本來就有該檔案，則先換行，否則會黏在後面
					if (targetfilepath.exists()) {
						newline = true;
					}

					fout = new FileOutputStream(targetfilepath, true);
					osw = new OutputStreamWriter(fout, "UTF-8");
					fw = new BufferedWriter(osw);

					if (newline) {
						fw.newLine();
					}
					fw.write(o);
				} else {
					if (fw != null) {
						fw.newLine();
						fw.write(o);
					} else {
						// 增加錯誤時固定放入當天1058且業務為ER.
						// 依 分行 日期 業務 做分類
						String targetpath = FilenameUtils.concat(outputrepFolder,
								"1058" + "/" + filenm[0] + "/" + "ER");
						File uploadFilePath = new File(targetpath);
						if (!uploadFilePath.exists()) {
							uploadFilePath.mkdirs();
							uploadFilePath.setExecutable(true, false);
							uploadFilePath.setWritable(true, false);
							uploadFilePath.setReadable(true, false);
							logger.info("路徑不存在,但是已經成功創建了" + targetpath);
						} else {
							logger.info("文件路徑存在" + targetpath);
						}
						logger.info("報表檔案路徑:" + targetpath);
						logger.info("報表檔案名稱:" + FilterUtils.escape(filenm[1] + extName));
						targetfilepath = new File(FilenameUtils.concat(targetpath, "ER-" + filenm[1] + extName));

						fout = new FileOutputStream(targetfilepath);
						osw = new OutputStreamWriter(fout, "UTF-8");
						fw = new BufferedWriter(osw);
						fw.write(o);
						logger.info("Report報表轉檔可能發生錯誤!!!改存放入:" + targetfilepath.getPath());
					}
				}
			}

			SafeClose.close(fw);
			SafeClose.close(osw);
			SafeClose.close(fout);

			String copyto = "";
			if (targetfilepath != null) {
				copyto = FilenameUtils.concat(copytoFolder, FilenameUtils.getPath(targetfilepath.getPath()));
				logger.info("copyto " + copyto);
				File copytotmp = new File(copyto);
				targetfilepath.setExecutable(true, false);
				targetfilepath.setWritable(true, false);
				targetfilepath.setReadable(true, false);
				FileUtils.copyFileToDirectory(targetfilepath, copytotmp, true);
				copytotmp.setExecutable(true, false);
				copytotmp.setWritable(true, false);
				copytotmp.setReadable(true, false);
			}
		} catch (Throwable t) {
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			logger.warn("report rep gen file fail..." + filename);
			logger.warn(errors.toString());
		} finally {
			SafeClose.close(fw);
			SafeClose.close(osw);
			SafeClose.close(fout);
		}
	}
}
