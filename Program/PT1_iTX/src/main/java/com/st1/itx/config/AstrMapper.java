package com.st1.itx.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.util.dump.HexDump;
import com.st1.itx.util.filter.SafeClose;
import com.st1.itx.util.log.SysLogger;

@Service("astrMapper")
@Scope("singleton")
public class AstrMapper extends SysLogger {

	@Value("${iTXResourceFolder}")
	private String itxRf;

	private Map<String, String> astrMapBig5 = new HashMap<String, String>();

	private Map<String, String> astrMapUni = new HashMap<String, String>();

	private Map<String, String> astrMapUni2Big = new HashMap<String, String>();

	public AstrMapper() {
	}

	@PostConstruct
	public void init() {
		File doc = new File(itxRf + "Astr.txt");

		FileReader fileR = null;
		FileInputStream fileI = null;
		InputStreamReader isr = null;
		BufferedReader bR = null;
		try {
//			fileR = new FileReader(doc);
			fileI = new FileInputStream(doc);
			isr = new InputStreamReader(fileI, "UTF-16");
			bR = new BufferedReader(isr);
			String s = "";
			while (!Objects.isNull(s = bR.readLine())) {
				this.info(s);
				String[] ss = s.split(",");
				astrMapBig5.put(ss[0].trim(), ss[2].trim());
				astrMapUni.put(ss[1].trim(), ss[2].trim());
				astrMapUni2Big.put(ss[1].trim(), ss[0].trim());
			}
		} catch (Exception e) {
			this.error("Read Astr Error!!");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		} finally {
			SafeClose.close(bR);
			SafeClose.close(fileR);
		}
	}

	public byte[] getMapperChar(char c) throws Exception {
		try {
			byte[] uniByte = new byte[2];
			uniByte[0] = (c + "").getBytes("UNICODE")[2];
			uniByte[1] = (c + "").getBytes("UNICODE")[3];

			String sChar = astrMapUni2Big.get(HexDump.toHexString(uniByte).trim());
			if (!Objects.isNull(sChar))
				return HexDump.hexStringToByteArray(sChar);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			return "　".getBytes("BIG5");
		}

		return "　".getBytes("BIG5");
	}

}
