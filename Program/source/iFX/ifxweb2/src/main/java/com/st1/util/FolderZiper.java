package com.st1.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.filter.SafeClose;

/***
 * 壓縮資料夾副程式
 * 
 * @author ST1-KE
 * 
 */
public class FolderZiper {
	public static void main(String[] a) throws Exception {
		File zipToDownload = zipFolder("C:\\Users\\ST1-KE\\Desktop\\a", "C:\\Users\\ST1-KE\\Desktop\\a.zip");
		System.out.println(zipToDownload.exists());

	}

	/***
	 * 壓縮資料夾函式
	 * 
	 * @param srcFolder   來源資料夾
	 * @param destZipFile 目的資料夾
	 * @return File檔案
	 * @throws Exception
	 */
	static public File zipFolder(String srcFolder, String destZipFile) throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		try {
			fileWriter = new FileOutputStream(FilterUtils.filter(destZipFile));
			zip = new ZipOutputStream(fileWriter);

			addFolderToZip("", srcFolder, zip);
			zip.flush();
		} finally {
			SafeClose.close(zip);
			SafeClose.close(fileWriter);
		}
		return new File(FilterUtils.filter(destZipFile));
	}

	static private void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {
		FileInputStream in = null;
		try {
			File folder = new File(FilterUtils.filter(srcFile));
			if (folder.isDirectory()) {
				addFolderToZip(path, srcFile, zip);
			} else {
				byte[] buf = new byte[1024];
				int len;
				in = new FileInputStream(FilterUtils.filter(srcFile));
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
				while ((len = in.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
			}
		} finally {
			SafeClose.close(in);
		}
	}

	static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		File folder = new File(FilterUtils.filter(srcFolder));

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}
}