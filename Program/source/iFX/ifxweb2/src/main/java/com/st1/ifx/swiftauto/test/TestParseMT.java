package com.st1.ifx.swiftauto.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.swiftauto.IncomingMessage;
import com.st1.ifx.swiftauto.MessageType;
import com.st1.ifx.swiftauto.TotaToPrn;
import com.st1.servlet.GlobalValues;

public class TestParseMT {
	static final Logger logger = LoggerFactory.getLogger(TestParseMT.class);

	static String folder = "D:\\ifxfolder\\runtime\\swift\\mt";
	static String crln = "\r\n";

	static org.springframework.context.support.GenericXmlApplicationContext ctx;

	static void load() {

		String[] xmls = { "file:d:/ifxfolder/webServerEnv/ifx-env.xml", "classpath:app-context-one.xml" };
		ctx = new org.springframework.context.support.GenericXmlApplicationContext();
		ctx.load(xmls);
		ctx.refresh();

		// SwiftAutoConfig cfg = ctx.getBean(SwiftAutoConfig.class);
		// System.out.println("swift.bicquery 1:"+cfg.getBicQueryType1());
		// System.out.println("swift.bicquery 2:"+cfg.getBicQueryType2());
		// System.out.println("swift.bicquery 3:"+cfg.getBicQueryType3());

	}

	// 獨立測試電文轉成報表檔
	public static void main(String[] args) throws Exception {
		// testB4();

		// setupGlobalValues();
		// System.out.println("A:" + System.getProperty("ifx_spring_env"));
		// System.setProperty("ifx_spring_env", "d:\\ifxfolder");
		// System.out.println(System.getProperty("ifx_spring_env"));
		load();
		// System.exit(0);

		// String mt = "mt950.txt";
		// String mtFileName = combinePaths(folder, mt);
		// System.out.println(mtFileName);

		// String t =
		// "5050026200053876DU0001001H52001074000 505020110104950
		// {1:F01CCBCTWT0AXXX5885556968}{4:{177:1212141941}{451:0}}{1:F01CCBCTWT0XXX5885556968}{2:O9501941121214ICBCUS33XXXX10155944431212141941N}{4:##:20:IS1147-01-121214##:25:2000191005271##:28C:00241/00001##:60F:C121214USD25904,40##:61:1212141214CD136902,00NTRFH003100818/01025//F61214509737000##B/O:CHINA
		// MINSHENG BANKING
		// CORPORA##:62F:121214USD162806,40##:64:C121214USD162806,40##-}{5:{CHK:1BF569B5EA1C}}{S:{COP:S}{CON:}{TRN:IS1147-01-121214}}
		// ";
		String t = makeFakeTotaWithSwift();

		TotaToPrn prn = ctx.getBean(TotaToPrn.class);
		for (int i = 0; i < 1; i++) {
			prn.fromTota(t);
			prn.setLinePrefix("     ");

			String filePath = prn.generate();

			System.out.println("saved to " + filePath);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}

	}

	private static void testB4() throws Exception {
		String text = "{1:F01TACBTWTPBXXX6606453638}{2:O1031255150422BOFAUS6SAXXX06585998601504230705N}{3:{108:1504220299692-04}}{4:\r\n" + ":20:2015042200299692\r\n" + ":23B:CRED\r\n"
				+ ":32A:150423USD564,\r\n" + ":33B:USD574,\r\n" + ":50K:/006550497524\r\n" + "FLEXTRONICS INTERNATIONAL LATI\r\n" + "847 GIBRALTAR DRIVE\r\n" + "MILPITAS          CA 95035      US\r\n"
				+ ":52D:/006550497524/\r\n" + "FLEXTRONICS INTL EUROPE BV\r\n" + "SITE 391\r\n" + "NOBELSTRAAT 10-14\r\n" + "OOSTRUM LB, 5807GA, NETHERLANDS\r\n" + ":57A:TACBTWTP544\r\n"
				+ ":59:/5447665102281\r\n" + "PSE TECHONOLGY COORPORATION\r\n" + "N2, TZU CHIANG 5TH, RD.CHUNG LI, IN\r\n" + "CHUNG LI TY          320        TW\r\n"
				+ ":70:/RFB/528777///PID/P528777/INV/ECC15\r\n" + "01340/\r\n" + ":71A:SHA\r\n" + ":71F:USD10,\r\n" + "-}{5:{MAC:00000000}{CHK:ED0495E967DE}{DLM:}}​";

		String reBlk4 = "\\{4:(.+?)}";
		Pattern p = Pattern.compile(reBlk4, Pattern.DOTALL);
		Matcher m = p.matcher(text);
		if (m.find()) {
			System.out.printf("%s\n", m.group());
		}
		System.exit(1);
	}

	public static String makeFakeTotaWithSwift() throws Exception {
		setupGlobalValues();
		String tota = makeTestTota();
		String text0 = "{1:F01CCBCTWT0AXXX5885556968}{4:{177:1212141941}{451:0}}{1:F01CCBCTWT0XXX5885556968}{2:O9501941121214ICBCUS33XXXX10155944431212141941N}{4:##:20:IS1147-01-121214##:25:2000191005271##:28C:00241/00001##:60F:C121214USD25904,40##:61:1212141214CD136902,00NTRFH003100818/01025//F61214509737000##B/O:CHINA MINSHENG BANKING CORPORA##:62F:121214USD162806,40##:64:C121214USD162806,40##-}{5:{CHK:1BF569B5EA1C}}{S:{COP:S}{CON:}{TRN:IS1147-01-121214}}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ";
		String text = "{1:F01TACBTWTPBXXX6606453638}{2:O1031255150422BOFAUS6SAXXX06585998601504230705N}{3:{108:1504220299692-04}}{4:\r\n" + ":20:2015042200299692\r\n" + ":23B:CRED\r\n"
				+ ":32A:150423USD564,\r\n" + ":33B:USD574,\r\n" + ":50K:/006550497524\r\n" + "FLEXTRONICS INTERNATIONAL LATI\r\n" + "847 GIBRALTAR DRIVE\r\n" + "MILPITAS          CA 95035      US\r\n"
				+ ":52D:/006550497524/\r\n" + "FLEXTRONICS INTL EUROPE BV\r\n" + "SITE 391\r\n" + "NOBELSTRAAT 10-14\r\n" + "OOSTRUM LB, 5807GA, NETHERLANDS\r\n" + ":57A:TACBTWTP544\r\n"
				+ ":59:/5447665102281\r\n" + "PSE TECHONOLGY COORPORATION\r\n" + "N2, TZU CHIANG 5TH, RD.CHUNG LI, IN\r\n" + "CHUNG LI TY          320        TW\r\n"
				+ ":70:/RFB/528777///PID/P528777/INV/ECC15\r\n" + "01340/\r\n" + ":71A:SHA\r\n" + ":71F:USD10,\r\n" + "-}{5:{MAC:00000000}{CHK:ED0495E967DE}{DLM:}}​";
		tota = tota + text;
		System.out.printf("fake tota:[%s]\n", tota);
		return tota;
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

	private static void setupGlobalValues() {
		GlobalValues.swiftFolder = "D:\\ifxfolder\\runtime\\swift\\mt\\";
		GlobalValues.REPOSITORY_ROOT = "D:\\ifxwriter\\repos\\report";
		GlobalValues.runtimeFolder = "D:\\ifxfolder\\runtime";
		String fmtFolder = GlobalValues.runtimeFolder + File.separator + "fmt" + "2" + File.separator;
		com.st1.ifx.hcomm.fmt.Env.setFmtFolder(fmtFolder);

	}

	private static void TestIncoming(String tota) {

		IncomingMessage incoming = new IncomingMessage(tota);
		// System.out.println(incoming.isFromMessage());
		incoming.parse();
	}

	private static boolean isTagBegin(String inputStr) {
		String patternStr = "^:(\\d{2}([A-Z])?):";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.find()) {
			System.out.println("yes:" + inputStr);
			System.out.println("\t" + matcher.group(0));
			System.out.println("\t" + matcher.group(1));
		} else {
			System.out.println("no:" + inputStr);
		}
		return matcher.find();
	}

	private static String getMT110_Tota() {

		return ":20:445" + crln + ":53A:/A/aaa" + crln + "ICBCUS33" + crln + ":54A:111213USD456," + crln + ":54B:JPY777,";

	}

	private static void parseJson2(String mtFileName) {
		MessageType mt = MessageType.fromFile(mtFileName);

	}

	private static void parseJson(String src) {
		ObjectMapper mapper = new ObjectMapper();

		try {

			JsonNode root = mapper.readTree(new File(src));
			JsonNode head = root.get("$");
			if (head.has("name")) {
				// String name = head.get("name").getTextValue();
				String name = root.path("$2").path("name").textValue();
				System.out.println(name);
			}
			JsonNode tags = root.get("tags");
			System.out.println(tags.size());
			for (JsonNode tag : root.get("tags")) {
				System.out.println(tag.path("$").path("name").textValue());
			}
		} catch (JsonProcessingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

	}

	public static String combinePaths(String... paths) {
		if (paths.length == 0) {
			return "";
		}
		File combined = new File(paths[0]);
		int i = 1;
		while (i < paths.length) {
			combined = new File(combined, paths[i]);
			++i;
		}
		return combined.getPath();
	}
}
