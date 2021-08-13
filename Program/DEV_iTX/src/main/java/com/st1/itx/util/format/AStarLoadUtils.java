package com.st1.itx.util.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.AStar.TBConvert.Big5.ConvertBig5_UCS2;
import com.AStar.TBConvert.Big5.TB_Big5_NHC;
import com.AStar.TBConvert.Big5.TB_Big5_UCS2;
import com.AStar.TBConvert.CNS.ConvertCNS_UCS2;
import com.AStar.TBConvert.CNS.TB_CNS_UCS2;
import com.AStar.TBConvert.Customize.TBConvertTCB;
import com.AStar.TBConvert.NHC.ConvertNHC_UCS2;
import com.AStar.TBConvert.NHC.TB_NHC_Big5;
import com.AStar.TBConvert.NHC.TB_NHC_UCS2;
import com.AStar.TBConvert.NHC.ConvertNHC_Big5;
import com.AStar.TBConvert.Big5.ConvertBig5_NHC;
import com.AStar.TBConvert.UCS2.ConvertUCS2_CNS;
import com.AStar.TBConvert.UCS2.ConvertUCS2_NHC;
import com.AStar.TBConvert.UCS2.TB_UCS2_CNS;
import com.AStar.TBConvert.UCS2.TB_UCS2_NHC;
import com.AStar.TBConvert.UCS2.TB_UCS2_Web;
import com.AStar.TBConvert.UTF8.UTF8WebValidate;
import com.st1.itx.util.filter.FilterUtils;

/*
 * 
 * @ClassName: com.tcb.twnb.web.util.AStarLoadUtils
 * 
 * @Description: 滿天星工具類別.
 * 
 * @Copyright : Copyright (c) IBM Corp. 2012. All Rights Reserved.
 * 
 * @Company: IBM GBS Team.
 * 
 * @author AnsonTsai
 * 
 * @version 1.0, Jun 12, 2012
 */

//@Component
//@Scope("singleton")
public class AStarLoadUtils {
	static final Logger logger = LoggerFactory.getLogger(AStarLoadUtils.class);

	@Value("${iTXConfig}")
	public String twnbconfigFilePath;

	public boolean isloaded = false;

	private TB_Big5_NHC n = null;
	private TB_UCS2_Web n8 = null;
	private TB_CNS_UCS2 n7 = null;
	private TB_UCS2_CNS n6 = null;
	private TB_Big5_UCS2 n5 = null;
	private TB_NHC_UCS2 n4 = null;
	private TB_NHC_UCS2 n3 = null;
	private TB_UCS2_NHC n2 = null;
	private TB_NHC_Big5 n1 = null;

	@PostConstruct
	public void init() {
		logger.info("AStarLoadUtils.init !!!!");

		long nRet = 0;
		try {
			twnbconfigFilePath += "TBConvert.conf";
			logger.info("AStarLoadUtils twnbconfigFilePath = [" + FilterUtils.escape(twnbconfigFilePath) + "]");
			nRet = TBConvertTCB.init(twnbconfigFilePath);
			if (nRet == TBConvertTCB.Result_Succeed) {
				isloaded = true;
			} else {
				logger.info("TBConvertTCB.init fail as [" + nRet + "]");
			}
		} catch (Exception e) {
			logger.error("TBConvertTCB.init fail");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
	}

	public byte[] convertBIG5StrToIBM937Bytes(String data) throws Exception {
		validateTNSCode();

		byte[] byt = data.getBytes("Big5_HKSCS");

		ConvertBig5_NHC converter = new ConvertBig5_NHC();
		long nRet = converter.convert(n, byt, byt.length);
		if (nRet != 0) {
			logger.debug("字霸轉換失敗！errCode = ConvertBig5_NHC.convert() = [" + nRet + "]");
			throw new Exception("字霸轉換失敗！errCode = ConvertBig5_NHC.convert() = [" + nRet + "]");
		}
		return converter.getResult();
	}

	public String convertIBM937BytesToBIG5(byte[] data) throws Exception {
		boolean p_WantShiftInOut = true;
		validateTNSCode();

		ConvertNHC_Big5 converter = new ConvertNHC_Big5();
		long nRet = converter.convert(n1, data, data.length, p_WantShiftInOut);
		if (nRet != 0) {
			logger.debug("字霸轉換失敗！errCode = ConvertNHC_Big5.convert() = [" + nRet + "]");
			throw new Exception("字霸轉換失敗！errCode = ConvertNHC_Big5.convert() = [" + nRet + "]");
		}

		byte[] udata = converter.getResult();

		return new String(udata, "Big5_HKSCS");
	}

	public byte[] convertUnicodeStrToIBM937Bytes(String data) throws Exception {
		validateTNSCode();

		int sosiCount = 0;

		char[] chs = new char[data.length()];
		data.getChars(0, data.length(), chs, 0);

		ConvertUCS2_NHC converter = new ConvertUCS2_NHC();
		long nRet = converter.convert(n2, chs, chs != null ? chs.length : 0);
		if (nRet != 0) {
			logger.debug("字霸轉換失敗！errCode = ConvertUCS2_NHC.convert() = [" + nRet + "]");
			throw new Exception("字霸轉換失敗！errCode = ConvertUCS2_NHC.convert() = [" + nRet + "]");
		}
		// 040中文0f07040中文0f07 -> 370e中文0f2f370e中文0f2f -> 0e中文0f0e中文0f
		// 潘 unicode轉回cp937切byteb時,如中文符號接續會有問題,須轉完後再刪除補位符號x37&x2f
		// 因0x37 & 0x2f 看起來是無意義碼..所以這樣判斷應該可以?應該不會切到中文半形,因為中文一定包在0x0e 0x0f裡
		byte[] dataUse = converter.getResult();
		for (int i = 0; i < dataUse.length; i++)
			if ((dataUse[i] == 0x0e && dataUse[i - 1] == 0x37) || (dataUse[i] == 0x2f && dataUse[i - 1] == 0x0f))
				sosiCount++;

		if (sosiCount > 0) {
			byte[] newDatasix = new byte[dataUse.length - sosiCount];

			for (int i = 0, j = 0; i < dataUse.length; i++) {
				if (dataUse[i] == 0x37 && dataUse[i + 1] == 0x0e) {
					;
				} else {
					if (dataUse[i] == 0x0f) {
						newDatasix[j] = dataUse[i];
						j++;
					} else {
						if (dataUse[i] == 0x2f && dataUse[i - 1] == 0x0f) {
							;
						} else {
							newDatasix[j] = dataUse[i];
							j++;
						}
					}
				}
			}
			return newDatasix;
		} else {
			return converter.getResult();
		}
	}

	// public String convertIBM937BytesToUnicodeStrA(byte[] data) throws
	// Exception
	// {
	// validateTNSCode();
	//
	// ConvertNHC_UCS2 converter = new ConvertNHC_UCS2();
	// long nRet = converter.convert(n, data, data.length);
	// if (nRet != 0) {
	// logger.debug("字霸轉換失敗！errCode = ConvertNHC_UCS2.convert() = [" + nRet + "]");
	// throw new Exception("字霸轉換失敗！errCode = ConvertNHC_UCS2.convert() = [" + nRet
	// + "]");
	// }
	//
	// char[] udata = converter.getResult();
	//
	// return new String(udata);
	// }

	public String convertIBM937BytesToUnicodeStr(byte[] data) throws Exception {
		validateTNSCode();

		ConvertNHC_UCS2 converter = new ConvertNHC_UCS2();
		byte[] dataUse = Arrays.copyOfRange(data, 0, data.length);

		int sosiCount = 0;

		for (int i = 0; i < dataUse.length; i++)
			if ((dataUse[i] == 0x0E) || (dataUse[i] == 0x0F))
				sosiCount++;

		byte[] newDatasix = new byte[dataUse.length + sosiCount];
		for (int i = 0, j = 0; i < dataUse.length; i++) {
			if (dataUse[i] == 0x0E) {
				newDatasix[j++] = 0x37; // 0x37 會被轉成 0x04
				newDatasix[j++] = 0x0E; // 0x0E 會被刪除
			} else {
				if (dataUse[i] == 0x0F) {
					newDatasix[j++] = 0x0F; // 0x0F 會被刪除
					newDatasix[j++] = 0x2F; // 0x2F 會被轉成 0x07
					// newData2[j++] = 0x40;
					// newData2[j++] = 0x40;
				} else {
					newDatasix[j++] = dataUse[i];
				}
			}
		}

		long nRet = converter.convert(n3, newDatasix, newDatasix.length);
		if (nRet != 0) {
			logger.debug("字霸轉換失敗！errCode = ConvertNHC_UCS2.convert() = [" + nRet + "]");
			throw new Exception("字霸轉換失敗！errCode = ConvertNHC_UCS2.convert() = [" + nRet + "]");
		}

		char[] udata = converter.getResult();

		return new String(udata);
	}

	public String convertIBM937BytesToUnicodeStrorg(byte[] data) throws Exception {
		validateTNSCode();

		ConvertNHC_UCS2 converter = new ConvertNHC_UCS2();

		long nRet = converter.convert(n4, data, data.length);
		if (nRet != 0) {
			logger.debug("字霸轉換失敗！errCode = ConvertNHC_UCS2.convert() = [" + nRet + "]");
			throw new Exception("字霸轉換失敗！errCode = ConvertNHC_UCS2.convert() = [" + nRet + "]");
		}

		char[] udata = converter.getResult();

		return new String(udata);
	}

	public String convertBIG5BytesToUnicodeStr(byte[] data) throws Exception {
		validateTNSCode();

		ConvertBig5_UCS2 converter = new ConvertBig5_UCS2();
		long nRet = converter.convert(n5, data, data.length);
		if (nRet != 0) {
			logger.debug("字霸轉換失敗！errCode = ConvertBig5_UCS2.convert() = [" + nRet + "]");
			throw new Exception("字霸轉換失敗！errCode = ConvertBig5_UCS2.convert() = [" + nRet + "]");
		}

		char[] udata = converter.getResult();

		return new String(udata);
	}

	public String convertUnicodeStrToCNSStr(String data) throws Exception {
		validateTNSCode();

		char[] chs = new char[data.length()];
		data.getChars(0, data.length(), chs, 0);
		ConvertUCS2_CNS converter = new ConvertUCS2_CNS();
		long nRet = converter.convert(n6, chs, chs.length);
		if (nRet != 0) {
			logger.debug("字霸轉換失敗！errCode = ConvertUCS2_CNS.convert() = [" + nRet + "]");
			throw new Exception("字霸轉換失敗！errCode = ConvertUCS2_CNS.convert() = [" + nRet + "]");
		}

		byte[] udata = converter.getResult();

		return new String(udata);
	}

	public String convertCNSStrToUnicodeStr(String data) throws Exception {
		validateTNSCode();

		byte[] byt = new byte[data.length()];
		byt = data.getBytes();

		ConvertCNS_UCS2 converter = new ConvertCNS_UCS2();
		long nRet = converter.convert(n7, byt, byt.length);

		if (nRet != 0) {
			logger.debug("字霸轉換失敗！errCode = ConvertCNS_UCS2.convert() = [" + nRet + "]");
			throw new Exception("字霸轉換失敗！errCode = ConvertCNS_UCS2.convert() = [" + nRet + "]");
		}

		return new String(converter.getResult());
	}

	private void validateTNSCode() throws Exception {
		if (!isloaded) {
			logger.debug("字霸字碼表尚未載入！");
			throw new Exception("字霸字碼表尚未載入！");
		}
	}

	/**
	 * 網頁內容轉換.
	 * 
	 * @param data String
	 * @return String uft8
	 * @throws Exception exception
	 */
	public String convertWebInputWord4check(String data) throws Exception {
		validateTNSCode();

		// TB_UCS2_Web p_Table = new TB_UCS2_Web();
		// if(TBConvertTCB.isCustomize())
		// p_Table = (TB_UCS2_Web)TBConvertTCB.getTable("TB_UCS2_WEB.BIN");
		// byte[] byt = new byte[data.length()];
		// byt = data.getBytes();
		// UTF8WebValidate validate = new UTF8WebValidate();
		// long nRet = validate.validate(p_Table, byt, byt.length);

		byte[] byt = new byte[data.length()];
		byt = data.getBytes("UTF-8");
		UTF8WebValidate validate = new UTF8WebValidate();
		long nRet = validate.validate(n8, byt, byt.length);

		if (nRet != 0) {
			logger.debug("網頁內容檢核失敗！errCode = UTF8WebValidate.validate() = [" + nRet + "]");
			throw new Exception("網頁內容檢核失敗！errCode = UTF8WebValidate.validate() = [" + nRet + "]");
		}

		return new String(validate.getResult(), "UTF-8");
	}
}
