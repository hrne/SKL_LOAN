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
public class TutfConverter {
	private static final Logger logger = LoggerFactory.getLogger(TutfConverter.class);

	// 重複放入在inbox
	@Value("${import.inbox}")
	private String outputTufFolder;

	@Value("${import.copytoFolder}")
	private String copytoFolder;

	@ServiceActivator
	public void convert(Message msg) throws Throwable {
		logger.info("converting msg:{}", msg);
		File file = (File) msg.getPayload();
		moveFileToInboxFolder(file.getAbsolutePath());
	}

	private void moveFileToInboxFolder(String filePath) {
		String fileName = FilenameUtils.getName(filePath);
		logger.info("filePath!" + filePath);
		File fold = new File(filePath); // .t1-
		logger.info("fold:" + fold.getPath());
		File fnew = new File(fileName.substring(0, fileName.length() - 1));// BATCH.....t1
		logger.info("fnew:" + fnew.getPath());
		File fresult = new File(FilterUtils.filter(outputTufFolder)); // INBOX.....t1
		logger.info("fresult:" + fresult.getPath());
		FileInputStream fis = null;
		FileOutputStream fos = null;
		InputStreamReader isr = null;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		BufferedWriter out = null;
		try {
			fis = new FileInputStream(fold);
			isr = new InputStreamReader(fis, "BIG5");
			in = new BufferedReader(isr);
			logger.info("before fnew!");

			fos = new FileOutputStream(fnew);
			osw = new OutputStreamWriter(fos, "UTF-8");
			out = new BufferedWriter(osw);
			logger.info("go sCurrentLine!");

			String sCurrentLine = "";
			while ((sCurrentLine = in.readLine()) != null) {
				logger.info("Tutf:" + sCurrentLine);
				if (out != null) {
					out.write(sCurrentLine);
					out.write("\n");
				}
			}
			try {
				// 原移動改複製 留著才能看問題
				FileUtils.copyFileToDirectory(fnew, fresult, true);
				String copyto = FilenameUtils.concat(copytoFolder, FilenameUtils.getPath(fresult.getPath()));
				logger.info("copyto " + copyto);
				FileUtils.copyFileToDirectory(fnew, new File(copyto), true);
			} catch (IOException e) {
				logger.error("TutfConverter move file backup:", e);
			}
			fold.delete();
		} catch (Throwable t) {
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
		} finally {
			SafeClose.close(out);
			SafeClose.close(osw);
			SafeClose.close(fos);

			SafeClose.close(in);
			SafeClose.close(isr);
			SafeClose.close(fis);

			logger.info("moveFileToInboxFolder!");
		}
	}
}
