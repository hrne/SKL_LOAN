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
public class RopConverter {
	private static final Logger logger = LoggerFactory.getLogger(RopConverter.class);

	@Value("${import.ropbox}")
	private String outputropFolder;

	@Value("${import.copytoFolder}")
	private String copytoFolder;

	@Value("${import.backupFolder}")
	private String backupFolder;

	@Value("${import.convertRopToExt}")
	private String extName = ".ropt";

	@ServiceActivator
	public void convert(Message msg) throws Throwable {
		logger.info("converting msg:{}", msg);
		File file = (File) msg.getPayload();
		logger.info(FilterUtils.escape("convert " + file.getAbsolutePath() + " to " + outputropFolder));

		convertTorop(file.getAbsolutePath());
		moveFileToBackupFolder(file.getAbsolutePath());
	}

	private void convertTorop(String filePath) throws Exception {
		logger.info("convertTorop filePath:" + filePath);
		String name = FilenameUtils.getBaseName(filePath);

		BufferedReader br = null;
		File f = null;
		FileInputStream fis = null;
		String line = "";
		String result = "";
		try {

			f = new File(filePath);
			fis = new FileInputStream(f);
			// java.io.FileNotFoundException
			logger.info("BufferedReader BIG5..");
			br = new BufferedReader(new InputStreamReader(fis, "BIG5")); // MFT??????BIG5
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
				result = result + line + "\r\n";
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
		// ???????????????
		logger.info("RopConverter START");
		logger.info("result.length:" + result.length());
		moverightPath(result, name);
		logger.info("ropConverter done ");
		// FileUtils.moveFile(tmpFile, new File(targetFilePath));

	}

	private void moveFileToBackupFolder(String filePath) {
		// errorFolder
		String fileName = FilenameUtils.getName(filePath);
		String newFilePath = FilenameUtils.concat(backupFolder, fileName);
		File f = new File(newFilePath);
		if (f.exists()) {
			logger.warn("convertTorop " + newFilePath + " exists, delete it");
			f.delete();
		}

		try {
			FileUtils.moveFileToDirectory(new File(FilterUtils.filter(filePath)),
					new File(FilterUtils.filter(backupFolder)), true);
		} catch (IOException e) {
			logger.error("convertTorop move file", e);
		}

		logger.info("convertTorop " + fileName + " is moved to " + newFilePath);
	}

	// ???????????????,???????????????
	private void moverightPath(String result, String filename) {
		BufferedWriter fw = null;
		FileOutputStream fout = null;
		OutputStreamWriter osw = null;
		// ??????-??????+??????+????????????
		String[] filenm = filename.split("-");

		logger.info("Start moverightPath..");
		// ?????????????????? ??????????????????
		String targetpath = FilenameUtils.concat(outputropFolder, filenm[1].substring(0, 4) + "/" + filenm[0]); // ????????????6????????????????????????substring???
		File uploadFilePath = new File(targetpath);
		if (!uploadFilePath.exists()) {
			uploadFilePath.mkdirs();
			uploadFilePath.setExecutable(true, false);
			uploadFilePath.setWritable(true, false);
			uploadFilePath.setReadable(true, false);
			logger.info("???????????????,???????????????????????????" + targetpath);
		} else {
			logger.info("??????????????????" + targetpath);
		}
		File targetfilepath = new File(FilenameUtils.concat(targetpath, filenm[1] + extName));
		try {
			fout = new FileOutputStream(targetfilepath, false); // ??????
			osw = new OutputStreamWriter(fout, "UTF-8");
			fw = new BufferedWriter(osw);
			fw.write(result);
		} catch (Throwable t) {
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
		} finally {
			SafeClose.close(fw);
			SafeClose.close(osw);
			SafeClose.close(fout);
		}

		try {
			String copyto = FilenameUtils.concat(copytoFolder, FilenameUtils.getPath(targetfilepath.getPath()));
			logger.info("copyto " + copyto);
			File copytotmp = new File(copyto);
			targetfilepath.setExecutable(true, false);
			targetfilepath.setWritable(true, false);
			targetfilepath.setReadable(true, false);
			FileUtils.copyFileToDirectory(targetfilepath, copytotmp, true);
			copytotmp.setExecutable(true, false);
			copytotmp.setWritable(true, false);
			copytotmp.setReadable(true, false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			logger.warn("copyto error:" + e1.getMessage());
		}

	}
}
