package com.st1.ifx.file.fdp.item;

import java.io.File;
import java.io.IOException;

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

@Component
@Scope("prototype")
public class YangmeiConverter {
	private static final Logger logger = LoggerFactory.getLogger(YangmeiConverter.class);

	@Value("${import.yangmeibox}")
	private String outputyangmeiFolder;

	@Value("${import.copytoFolder}")
	private String copytoFolder;

	@Value("${import.backupFolder}")
	private String backupFolder;

	@ServiceActivator
	public void convert(Message msg) throws Throwable {
		logger.info("converting msg:{}", msg);
		File file = (File) msg.getPayload();
		logger.info(FilterUtils.escape("convert " + file.getAbsolutePath() + " to " + outputyangmeiFolder));

		copyFileToOutputFolder(file.getAbsolutePath());
		moveFileToBackupFolder(file.getAbsolutePath());

	}

	private void copyFileToOutputFolder(String filePath) {
		// errorFolder
		String fileNametp = FilenameUtils.getName(filePath);
		String fileDay = "";
		String fileName = "";
		logger.info("OLD fileName :" + fileNametp);
		// 中間才是黨名 ex:20190902-ALA1050513_1806
		fileName = fileNametp.substring(fileNametp.indexOf('-') + 1, fileNametp.indexOf('_'));
		fileDay = fileNametp.substring(0, fileNametp.indexOf('-'));
		logger.info("NEW fileName :" + fileName);

		String oflder = outputyangmeiFolder + "/" + fileDay;
		String newFilePath = FilenameUtils.concat(oflder, fileName);
		File of = new File(FilterUtils.filter(oflder));
		File nf = new File(FilterUtils.filter(newFilePath));
		// 重複->覆蓋
		// 一天不會多餘9個，如果超過則全數刪除重來。
		logger.info("before exists!");
		if (nf.exists()) {
			logger.warn("convertToyangmei " + newFilePath + " exists, delete it");
			nf.delete();
		} else if (of.exists() && of.listFiles().length >= 9) {
			logger.info("file >9,Path:" + of.getPath() + ",Delete All");
			deleteDirectory(of);
		}
		logger.info("after exists!");

		// newFilePath = oflder+"/"+日期+"/"
		String copyto = FilenameUtils.concat(copytoFolder, FilenameUtils.getPath(newFilePath));
		logger.info("copyto " + copyto);
		File filePathtp = new File(filePath);

		logger.info("nf:" + nf.getPath());
		try {
			// 搬移檔案順便換檔名
			FileUtils.copyFileToDirectory(filePathtp, new File(FilterUtils.filter(oflder)), true);
			// 換名字
			new File(FilenameUtils.concat(oflder, fileNametp))
					.renameTo(new File(FilenameUtils.concat(oflder, fileName)));

			FileUtils.copyFileToDirectory(filePathtp, new File(copyto), true);
			new File(FilenameUtils.concat(copyto, fileNametp))
					.renameTo(new File(FilenameUtils.concat(copyto, fileName)));

			File copytotmp = new File(FilenameUtils.concat(copyto, fileName));
			copytotmp.setExecutable(true, false);
			copytotmp.setWritable(true, false);
			copytotmp.setReadable(true, false);

		} catch (IOException e) {
			logger.error("convertToyangmei copy file", e);
		}

		logger.info("convertToyangmei " + fileName + " is copy to " + newFilePath);
	}

	private void moveFileToBackupFolder(String filePath) {
		// errorFolder
		String fileName = FilenameUtils.getName(filePath);
		String newFilePath = FilenameUtils.concat(backupFolder, fileName);
		File f = new File(newFilePath);
		if (f.exists()) {
			logger.warn("convertToyangmei " + newFilePath + " exists, delete it");
			f.delete();
		}

		try {
			FileUtils.moveFileToDirectory(new File(FilterUtils.filter(filePath)),
					new File(FilterUtils.filter(backupFolder)), true);
		} catch (IOException e) {
			logger.error("convertToyangmei move file", e);
		}

		logger.info("convertToyangmei " + fileName + " is moved to " + newFilePath);
	}

	/**
	 * Test Only!
	 */
	public static void main(String argv[]) {
		;
	}

	static public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
}
