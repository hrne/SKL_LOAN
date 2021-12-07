package com.st1.itx.util.parse;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.util.filter.SafeClose;
import com.st1.itx.util.log.SysLogger;

@Component("zLibUtils")
@Scope("singleton")
public class ZLibUtils extends SysLogger {

	/**
	 * 7z Compress
	 * 
	 * @param inputFile File Can Be Directory
	 * @return byte[] after Compress
	 * @throws LogicException when Compress Fail
	 */
	public byte[] compress(File inputFile) throws LogicException {
		String name = inputFile.getName();
		String nameZip = inputFile.getPath() + ".7z";

		SevenZOutputFile sevenZOutput = null;
		BufferedInputStream inputStream = null;

		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;

		try {
			sevenZOutput = new SevenZOutputFile(new File(nameZip));

			if (inputFile.isDirectory()) {
				File[] childFiles = inputFile.listFiles();
				if (childFiles.length == 0) {
					SevenZArchiveEntry entry = sevenZOutput.createArchiveEntry(inputFile, name);
					sevenZOutput.putArchiveEntry(entry);
					sevenZOutput.closeArchiveEntry();
					this.info("Compress dir:" + inputFile.getPath());
				} else {
					for (File childFile : childFiles)
						this.compress(childFile);
				}
			} else {
				inputStream = new BufferedInputStream(new FileInputStream(inputFile));
				SevenZArchiveEntry entry = sevenZOutput.createArchiveEntry(inputFile, name);
				sevenZOutput.putArchiveEntry(entry);
				int len = -1;
				byte[] buffer = new byte[2048];
				while ((len = inputStream.read(buffer)) != -1)
					sevenZOutput.write(buffer, 0, len);
				this.info("Compress file:" + inputFile.getPath());
			}

			sevenZOutput.closeArchiveEntry();
			SafeClose.close(sevenZOutput);

			fis = new FileInputStream(nameZip);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[2048];
			int len = -1;
			while ((len = fis.read(buffer)) != -1)
				baos.write(buffer, 0, len);

			return baos.toByteArray();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			return null;
//			throw new LogicException("EC000!", "檔案壓縮失敗");
		} finally {
			SafeClose.close(baos);
			SafeClose.close(fis);
			SafeClose.close(sevenZOutput);
			SafeClose.close(inputStream);
		}
	}

	/**
	 * unCompress7z
	 * 
	 * @param dataByte Compress7z byteArray
	 * @return unCompress Byte
	 * @throws LogicException when unCompress Is Error
	 */
	public byte[] unCompress7z(byte[] dataByte) throws LogicException {
		SeekableInMemoryByteChannel inMemoryByteChannel = null;
		SevenZFile sevenZFile = null;
//		SevenZArchiveEntry entry = null;
		ByteArrayOutputStream baos = null;

		try {
			inMemoryByteChannel = new SeekableInMemoryByteChannel(dataByte);
			sevenZFile = new SevenZFile(inMemoryByteChannel);
			sevenZFile.getNextEntry();

			baos = new ByteArrayOutputStream();
			int len = -1;
			byte[] buffer = new byte[2048];
			while ((len = sevenZFile.read(buffer)) != -1)
				baos.write(buffer, 0, len);
			return baos.toByteArray();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			return null;
//			throw new LogicException("EC000!", "解壓縮失敗");
		} finally {
			SafeClose.close(baos);
			SafeClose.close(sevenZFile);
			SafeClose.close(inMemoryByteChannel);
		}
	}

//	public void unCompress7z(String compressFilePath, String outputDir) throws IOException {
//		File compressFile = new File(compressFilePath);
//		if (compressFile == null || !compressFile.exists()) {
//			throw new RuntimeException("7zFile not exists.");
//		}
//
//		File output = new File(outputDir);
//		if (output == null || !output.exists() || !output.isDirectory()) {
//			throw new RuntimeException("Invalid outputDir:" + outputDir);
//		}
//
//		// 循环解压
//		SevenZFile sevenZFile = new SevenZFile(compressFile);
//		SevenZArchiveEntry entry = null;
//		while ((entry = sevenZFile.getNextEntry()) != null) {
//			String newFilePath = outputDir + File.separator + entry.getName();
//			File newFile = new File(newFilePath);
//
//			// 处理目录
//			if (entry.isDirectory()) {
//				boolean mkdirs = newFile.mkdirs();
//				if (!mkdirs) {
//					throw new RuntimeException("Fail mkdir:" + newFilePath);
//				}
//				this.info("Mkdir:" + newFilePath);
//				continue;
//			}
//
//			// 解压文件
//			OutputStream outputStream = null;
//			try {
//				outputStream = new FileOutputStream(newFile);
//				int length = 0;
//				byte[] buffer = new byte[2048];
//				while ((length = sevenZFile.read(buffer)) != -1) {
//					outputStream.write(buffer, 0, length);
//				}
//				this.info("UnCompress file:" + newFilePath);
//			} catch (Exception e) {
//				if (outputStream != null) {
//					outputStream.flush();
//					outputStream.close();
//				}
//			}
//		}
//	}
}
