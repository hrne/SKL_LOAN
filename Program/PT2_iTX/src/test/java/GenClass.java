import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GenClass {

	public static final String path = "C:\\SKL\\iTX_T\\src\\main\\java\\com\\st1\\itx\\trade\\";
	public static final String path2 = "Z:\\iTX\\tim\\";
	public static final String tradeName = "L5578";
	public static final String tradeType = tradeName.substring(0, 2);
	public static final String author = "AdamPan";
	public static final String version = "/**\r\n" + " * \r\n" + " * \r\n" + " * @author " + author + "\r\n" + " * @version 1.0.0\r\n" + " */\r\n";

	public static void main(String[] args) {

		File pF = new File(path + "\\" + tradeType + "\\");
		File tsF = new File(path + "\\" + tradeType + "\\" + tradeName + ".java");

		if (!pF.exists())
			pF.mkdirs();

		pF = null;

		if (tsF.exists()) {
			System.out.println("檔案已存在!!");
			return;
		}

		FileOutputStream fout;
		OutputStreamWriter osw;
		BufferedWriter tsfw;
		try {
			fout = new FileOutputStream(tsF, true);
			osw = new OutputStreamWriter(fout, "UTF-8");
			tsfw = new BufferedWriter(osw);

			tsfw.write("package com.st1.itx.trade." + tradeType + ";\r\n\r\n");
			tsfw.write("import java.util.ArrayList;\r\n\r\n");

			tsfw.write("import org.slf4j.Logger;\r\n" + "import org.slf4j.LoggerFactory;\r\n" + "import org.springframework.beans.factory.annotation.Autowired;\r\n"
					+ "import org.springframework.context.annotation.Scope;\r\n" + "import org.springframework.stereotype.Service;\r\n\r\n");
			tsfw.write("import com.st1.itx.Exception.LogicException;\r\n" + "import com.st1.itx.Exception.DBException;\r\n" + "import com.st1.itx.dataVO.TitaVo;\r\n"
					+ "import com.st1.itx.dataVO.TotaVo;\r\n" +
					// "import com.st1.itx.db.domain.BizDate;\r\n" +
					// "import com.st1.itx.eum.ContentName;\r\n" +
					"import com.st1.itx.tradeService.TradeBuffer;\r\n");
			// "import com.st1.itx.util.format.FormatUtil;\r\n");
			tsfw.newLine();

			try {
				String inputStr[] = new String(Files.readAllBytes(Paths.get(path2 + tradeType + "\\" + tradeName + ".tim")), "UTF8").split("\r\n");
				tsfw.write("/**\r\n * Tita<br>\r\n");
				boolean is = false;
				for (int i = 0; i < inputStr.length; i++) {
					if (is)
						tsfw.write("* " + inputStr[i].trim() + "<br>\r\n");
					if (inputStr[i].indexOf("SYSFIL17") != -1)
						is = !is;
				}
				tsfw.write("*/\r\n");
				tsfw.newLine();
			} catch (Exception x) {
				;
			}

			tsfw.write("@Service(\"" + tradeName + "\")\r\n");
			tsfw.write("@Scope(\"prototype\")\r\n");
			tsfw.write(version);
			tsfw.write("public class " + tradeName + " extends TradeBuffer {\r\n");
			tsfw.write("	private static final Logger logger = LoggerFactory.getLogger(" + tradeName + ".class);\r\n\r\n");

//			tsfw.write("	@Autowired\r\n" + "	public TXBuffer txBuffer;\r\n" + "\r\n" + "	@Autowired\r\n" + "	public TotaVo totaVo;\r\n\r\n");
			tsfw.write("	@Override\r\n" + "	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {\r\n" + "		logger.info(\"active " + tradeName + " \");\r\n"
					+ "		this.totaVo.init(titaVo);\r\n\r\n");
			tsfw.write("		this.addList(this.totaVo);\r\n");
			tsfw.write("		return this.sendList();\r\n" + "	}\r\n" + "}");

			tsfw.flush();
			osw.flush();
			fout.flush();

			tsfw.close();
			osw.close();
			fout.close();

			System.out.println(tradeName + " done!!");
		} catch (Exception e) {
			tsfw = null;
			osw = null;
			fout = null;
			e.printStackTrace();
		}

	}

}
