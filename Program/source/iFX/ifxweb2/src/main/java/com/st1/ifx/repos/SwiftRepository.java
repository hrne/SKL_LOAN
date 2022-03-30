package com.st1.ifx.repos;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.st1.ifx.domain.SwiftUnsolicitedMsg;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.service.SwiftUnsoMsgService;
import com.st1.servlet.GlobalValues;
import com.st1.util.MySpring;

public class SwiftRepository {
	String root;
	final String SWIFT_FOLDER = "_swift";

	public SwiftRepository() {
		root = GlobalValues.REPOSITORY_ROOT;
	}

	static SwiftUnsoMsgService swiftUnsoMsgService = MySpring.getSwiftUnsoMsgService();

	// 改從DB2抓取 FILE LIST FX_SWIFT_UNSO_MSG
	public List<File> getSwiftFolder(String brno, String dt, String ext) {
		String folder = combinePaths(root, brno, dt, SWIFT_FOLDER);
		File[] files = new File(folder).listFiles(new EndsWithFilter(ext));
		if (files == null)
			return new ArrayList<File>();
		return Arrays.asList(files);
	}

	// 抓取還未列印的電文稿
	@SuppressWarnings("rawtypes")
	public List<HashMap> getSwiftfromDb(String brndept, String dt, boolean mustfirst) {
		List<SwiftUnsolicitedMsg> list = null;
		List<HashMap> smapList = new ArrayList<HashMap>();
		if (mustfirst) {// 未列印過
			list = swiftUnsoMsgService.findSwiftListbybrndttime(brndept, dt, 0);
		} else { // 全部
			list = swiftUnsoMsgService.findSwiftListbybrndt(brndept, dt);
		}

		for (SwiftUnsolicitedMsg d : list) {
			HashMap<String, String> m = new HashMap<String, String>();
			m.put("filepath", d.getFilePath());
			m.put("filepath_rm", MyFileUtl.removeExtension(new File(FilterUtils.filter(d.getFilePath()))));
			m.put("msgstatus", d.getMsgStatus());
			m.put("msgtype", d.getMsgType());
			smapList.add(m);
		}

		return smapList;
	}

	// 取得該分行向下全部未列印電文稿
	public List<File> getAllswiftFolder(String brno, String ext) {
		String folder = combinePaths(root, brno);
		List<File> files = new ArrayList<File>();
		listFiles(files, new File(folder), ext);
		return files;
	}

	// 遍歷條件,上層資料夾是"_swift" 且為 ext結尾(0.rptsf)
	static void listFiles(List<File> files, File dir, String ext) {
		File[] listFiles = dir.listFiles();
		for (File f : listFiles) {
			if (f.isFile() && f.getParent().endsWith("_swift") && f.getName().endsWith(ext)) {
				files.add(f);
			} else if (f.isDirectory()) {
				listFiles(files, f, ext);
			}
		}
	}

	public File getSwiftFile(String brno, String dt, String filename) {
		String filePath = combinePaths(root, brno, dt, SWIFT_FOLDER, filename);
		File b = null;
		if (filePath != null) {
			b = new File(filePath);
		}
		return b;
	}

	public File getSwiftFile(String filePath) {
		File b = null;
		if (filePath != null) {
			b = new File(FilterUtils.filter(filePath));
		}
		return b;
	}

	// NNNN-MT_n.rp-
	//
	// NNNN : 序號或隨機號碼
	// MT : mt name
	// n:列印次數
	// NNNN-MT_0.rpt :尚未自動列印
	// NNNN-MT_1.rpt :印過一次
	// NNNN-MT_2.rpt: 印過2次

	// DB2的LIST更新即可 不用再更改檔案的名稱
	public void setSwiftFilePrintedAgain(String brno, String dt, String filename) {
		// String oldFile = combinePaths(root, brno, dt, SWIFT_FOLDER, filename + ext);
		// String[] ss = filename.split("_");
		// String name2 = String.format("%s_%d", ss[0], Integer.parseInt(ss[1]) + 1);
		// String newFile = combinePaths(root, brno, dt, SWIFT_FOLDER, name2 + ext);
		// try {
		// FileUtils.moveFile(FileUtils.getFile(oldFile), FileUtils.getFile(newFile));
		// } catch (IOException e) {
		// StringWriter errors = new StringWriter();
		// e.printStackTrace(new PrintWriter(errors));
		// logger.error(errors.toString());
		// }
		// 更新DB2資料 自動加1
		swiftUnsoMsgService.updataPrintTime(brno, dt, filename);
	}

	public static String combinePaths(String... paths) {
		if (paths.length == 0) {
			return "";
		}
		File combined = new File(FilterUtils.filter(paths[0]));
		int i = 1;
		while (i < paths.length) {
			combined = new File(combined, FilterUtils.filter(paths[i]));
			++i;
		}
		return combined.getPath();
	}

	class EndsWithFilter implements FilenameFilter {
		String ext;

		EndsWithFilter(String ext) {
			this.ext = ext;
		}

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(ext);
		}

	}

	// 取得該檔案路徑
	public String getSwiftFilePath(String brndept, String dt, String filename) {
		return swiftUnsoMsgService.findPathByBrndeptSrhdayFilename(brndept, dt, filename);
	}

}
