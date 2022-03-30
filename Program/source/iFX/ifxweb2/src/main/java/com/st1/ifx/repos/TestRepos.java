package com.st1.ifx.repos;

import java.io.File;
import java.util.List;

public class TestRepos {

	public static void main(String[] args) {
		// testSwiftAutoPrintFile();
		testSwiftRename();
	}

	private static void testSwiftRename() {
		String brno = "1058";
		String today = "20150421";
		String ext = ".rpt";
		String filename = "A0012-103.1";
		SwiftRepository repos = new SwiftRepository();
		repos.setSwiftFilePrintedAgain(brno, today, filename + ext);
	}

	private static void testSwiftAutoPrintFile() {
		String brno = "1058";
		String today = "20150421";
		String ext = ".rp-";
		SwiftRepository repos = new SwiftRepository();
		List<File> files = repos.getSwiftFolder(brno, today, ext);
		for (File f : files) {
			System.out.printf("%s\t%s\t%s\n", MyFileUtl.removeExtension(f), MyFileUtl.toDateString(f), MyFileUtl.toSizeString(f));
			String name = MyFileUtl.removeExtension(f);
			File f2 = repos.getSwiftFile(brno, today, name + ext);
			System.out.printf("%s\t%s\t%s\n\n", MyFileUtl.removeExtension(f2), MyFileUtl.toDateString(f2), MyFileUtl.toSizeString(f2));
		}
	}

}
