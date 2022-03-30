package com.st1.ifx.dataVo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.hcomm.fmt.FormatUtil;

public class TotaList extends ArrayList<String> {
	static final Logger logger = LoggerFactory.getLogger(TotaList.class);

	private static final long serialVersionUID = 8948999343481205587L;

	private final int txrsutPos = 40;
	private final int msgIdPos = 41;
	private final int msgendPos = 39;
	private final int mrkeyPos = 47;
	private final int calDyPos = 23;
	private final int totaTextPos = 75;

	private String sTota = "";

	private List<String> totw = new ArrayList<String>();

	public TotaList(String tota) {
		this.sTota = tota;
		this.add(tota);
		this.deTotw();
	}

	private void deTotw() {
		try {
			byte[] bytes = null;
			// bytes = AStarLoadUtils.convertUnicodeStrToIBM937Bytes(this.sTota);
			bytes = this.sTota.getBytes("UTF-8");
			logger.info("bytes LEN:" + bytes.length);

			int offset = 0;
			byte[] lenArray = new byte[5];
			int len;
			String t;

			while (bytes != null && offset < bytes.length) {
				logger.info("offset:" + offset + ", size:" + bytes.length);
				System.arraycopy(bytes, offset, lenArray, 0, 5);
				t = new String(lenArray, "UTF-8");
				logger.info("len:" + t);
				len = Integer.parseInt(t);
				byte[] totwArray = new byte[len];
				System.arraycopy(bytes, offset, totwArray, 0, len);
				offset += len;

				t = new String(totwArray, "UTF-8");
				logger.info("value:" + t);
				t = t.substring(5);
				t = FormatUtil.pad9(this.countLen(t), 5) + t;
				logger.info("apan value:" + t);
				totw.add(t);
			}
		} catch (Exception e) {
			this.totw.clear();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

	}

	private boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	private boolean IsPrintableAsciiChar(char ch) {
		if (32 <= ch && ch <= 126)
			return true;
		return false;
	}

	private String countLen(String s) {
		int len = 0;
		for (int i = 0; i < s.length(); ++i) {
//			if (isChinese(s.charAt(i)))
			if (isChinese(s.charAt(i)) || !IsPrintableAsciiChar(s.charAt(i)))
				len = len + 2;
			else
				len++;
		}
		return len + "";
	}

	public List<String> makeErrorTota(String oldTota, String msgidAndText) {
		// extract tota before msgend, and set msgend 1
		String t = oldTota.substring(0, msgendPos) + "1";
		// set TXRSUT
		t += "E";
		// SPACES(9) -> MLDRY and FILLER
		t += msgidAndText.substring(0, 5) + "         " + msgidAndText.substring(5);
		List<String> list = new ArrayList<String>();
		list.add(t);
		return list;
	}

	public void replaceTota(List<String> list) {
		logger.info("original totw size:" + this.totw.size());
		logger.info("totwTmp size:" + list.size());
		this.clear();
		this.addAll(list);
		this.totw.clear();
		this.totw.addAll(list);
		logger.info("new totw size:" + this.totw.size());
	}

	public String getTxRsut() {
		String s = this.sTota.substring(txrsutPos, txrsutPos + 1);
		logger.info("txrsut = " + s);
		return s;
	}

	public String getMsgId() {
		String s = this.sTota.substring(msgIdPos, msgIdPos + 5);
		logger.info("msgId = " + s);
		return s;
	}

	public String getMsgId(String s) {
		s = s.substring(msgIdPos, msgIdPos + 5);
		logger.info("msgId = " + s);
		return s;
	}

	public String getMrkey() {
		String s = this.sTota.substring(mrkeyPos, mrkeyPos + 20);
		logger.info("mrkey = " + s);
		return s;
	}

	public String getCorrectSeq() {
		String s = this.sTota.substring(totaTextPos).trim();
		return s;
	}

	public String getBody(String s) {
		s = s.substring(totaTextPos).trim();
		return s;
	}

	public String getCaldyF(String s) {
		s = s.substring(calDyPos, calDyPos + 8);
		int date = Integer.parseInt(s);
		if (date < 19110101) {
			date = date + 1911;
			s = Integer.toString(date);
		}
		return s;
	}

	public Set<String> getFormSet() {
		Set<String> set = new HashSet<String>();
		for (String sBox : totw) {
			set.add(this.getMsgId(sBox));
		}
		return set;
	}

	public String getsTota() {
		return sTota;
	}

	public List<String> getTotw() {
		return totw;
	}

	public void setsTota(String sTota) {
		this.sTota = sTota;
	}

	public void setTotw(ArrayList<String> totw) {
		this.totw = totw;
	}

}
