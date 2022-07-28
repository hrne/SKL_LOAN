package com.st1.itx.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
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

	public AstrMapper() {
	}

	@PostConstruct
	public void init() {
		File doc = new File(itxRf + "Astr.txt");

		FileReader fileR = null;
		BufferedReader bR = null;
		try {
			fileR = new FileReader(doc);
			bR = new BufferedReader(fileR);
			String s = "";
			while (!Objects.isNull(s = bR.readLine())) {
				String[] ss = s.split(",");
				astrMapBig5.put(ss[0].trim(), ss[2].trim());
				astrMapUni.put(ss[1].trim(), ss[2].trim());
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

	public String getMapperChar(char c) throws Exception {
		String sChar = astrMapUni.get(Integer.toHexString(c));
		if (!Objects.isNull(sChar))
			return sChar;
		else {
			try {
				byte[] ms950Byte = (c + "").getBytes("MS950");
				sChar = astrMapBig5.get(HexDump.toHexString(ms950Byte).trim());
				if (!Objects.isNull(sChar))
					return sChar;
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				return new String("　".getBytes("MS950"), "MS950");
			}
		}
		return new String("　".getBytes("MS950"), "MS950");
	}

}
