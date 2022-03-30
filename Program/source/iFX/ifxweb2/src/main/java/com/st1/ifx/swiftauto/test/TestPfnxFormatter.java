package com.st1.ifx.swiftauto.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.hcomm.fmt.HostFormatter;
import com.st1.servlet.GlobalValues;

public class TestPfnxFormatter {

	public String format(String lines, HashMap<String, String> flds) {
		// /\{\{(#.+?)}{2}/g;
		Pattern pattern = Pattern.compile("\\{\\{(#.+?)}}");
		Matcher matcher = pattern.matcher(lines);
		while (matcher.find()) {
			System.out.println(matcher.groupCount());
			System.out.println(matcher.group(0));
			System.out.println(matcher.group(1));
			System.out.println("");
			String name = matcher.group(1);
			String value = "";
			if (flds.containsKey(name))
				value = flds.get(name);
			lines = lines.replace(name, value);
		}

		return lines;
	}

	public static void main(String[] args) throws Exception {
		test1();

		// setupGlobalValues();
		// String tota = makeTestTota();
		// String text =
		// "{1:F01CCBCTWT0AXXX5885556968}{4:{177:1212141941}{451:0}}{1:F01CCBCTWT0XXX5885556968}{2:O9501941121214ICBCUS33XXXX10155944431212141941N}{4:##:20:IS1147-01-121214##:25:2000191005271##:28C:00241/00001##:60F:C121214USD25904,40##:61:1212141214CD136902,00NTRFH003100818/01025//F61214509737000##B/O:CHINA
		// MINSHENG BANKING
		// CORPORA##:62F:121214USD162806,40##:64:C121214USD162806,40##-}{5:{CHK:1BF569B5EA1C}}{S:{COP:S}{CON:}{TRN:IS1147-01-121214}}
		// ";
		// tota = tota + text;
		// System.out.println(tota);
		// parseTota(tota);
	}

	private static void setupGlobalValues() {
		GlobalValues.runtimeFolder = "D:\\ifxfolder\\runtime";
		String fmtFolder = GlobalValues.runtimeFolder + File.separator + "fmt" + "2" + File.separator;
		com.st1.ifx.hcomm.fmt.Env.setFmtFolder(fmtFolder);

	}

	private static void parseTota(String tota) {
		String totaFmtFile = "SWIFT-AUTO.tom";
		HostFormatter formatter = new HostFormatter(totaFmtFile);
		Map<String,String> map = formatter.parse(false, tota);
	}

	private static String makeTestTota() throws Exception {
		StringBuffer sb = new StringBuffer();
		String totaFmtFile = com.st1.ifx.hcomm.fmt.Env.getFmtFolder() + "SWIFT-AUTO.tom";
		LineIterator it = FileUtils.lineIterator(new File(FilterUtils.filter(totaFmtFile)), "UTF-8");
		try {
			while (it.hasNext()) {
				String line = it.nextLine();
				line = line.trim();
				if (line.length() > 0 && !line.startsWith("#")) {
					String[] ss = line.split(",");
					int len = Integer.parseInt(ss[1]);
					if (len < 999999) {
						line = String.format("%1$-" + len + "s", ss[0].charAt(0));
						line = line.replace(' ', ss[0].charAt(0));
						sb.append(line);
					}
				}
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
		return sb.toString();
	}

	private static void test1() {
		String px = "  KEY:{{#KEYNAME}}   DATE:{{#SYSDATEF}}\n" // TIME:{{#now!}}\n"
				+ "Authentication Result: {{#TOTA_AUTHX}}\n" + "{{#TOTA_DUPX}}\nthis is a book\n";

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("#KEYNAME", "key123");
		map.put("#SYSDATEF", "20150719");
		map.put("#TOTA_AUTHX", "authXXX");
		map.put("#TOTA_DUPX", "-dupx-");

		TestPfnxFormatter fmt = new TestPfnxFormatter();
		String result = fmt.format(px, map);
		System.out.println(px);
		System.out.println("-----------------------------");
		System.out.println(result);
	}
}
