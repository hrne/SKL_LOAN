package com.st1.itx.dataVO;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;

public class TempVo extends LinkedHashMap<String, String> {

	static final Logger logger = LoggerFactory.getLogger(TempVo.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -5107815091374499855L;

	/**
	 * Associates the specified value with the specified key in this map. If the map
	 * previously contained a mapping for the key, the old value is replaced.
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param param value to be associated with the specified key
	 */
	public void putParam(String key, Object param) {
		if (param instanceof Integer)
			this.put(key, new BigDecimal((Integer) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Double)
			this.put(key, new BigDecimal((Double) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Float)
			this.put(key, new BigDecimal((Float) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Long)
			this.put(key, new BigDecimal((Long) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof BigDecimal)
			this.put(key, ((BigDecimal) (param == null ? 0 : param)).toPlainString());
		else
			this.put(key, (String) (param == null ? "" : param));
	}

	/**
	 * Returns the value to which the specified key is mapped,or null if this map
	 * contains no mapping for the key.<br>
	 * 
	 * More formally, if this map contains a mapping from a key k to a value v such
	 * that (key==null ? k==null :key.equals(k)), then this method returns v;
	 * otherwiseit returns null. (There can be at<br>
	 * most one such mapping.)<br>
	 * 
	 * A return value of null does not necessarilyindicate that the map contains no
	 * mapping for the key; it's alsopossible that the map explicitly maps the key
	 * to null.The containsKey operation may be<br>
	 * used todistinguish these two cases.<br>
	 * 
	 * @param key 欄位名稱
	 * @return String Value
	 * @throws LogicException 無此名稱時扔出
	 */
	public String getParam(String key) throws LogicException {
		String value = this.get(key);
		return value == null ? "" : value;
	}

	/**
	 * 
	 * @param msg String
	 * @return TitaVo Object
	 * @throws LogicException 轉換失敗
	 */
	public TempVo getVo(String msg) throws LogicException {
		try {
			if (msg == null || msg.trim().isEmpty())
				return this;
			return new ObjectMapper().readValue(msg, TempVo.class);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());

			throw new LogicException("CE000", "TempVo To Vo 轉換失敗!! ");
		}
	}

	/**
	 * get Json String
	 * 
	 * @return String this
	 * @throws LogicException if convert fail throw LogicException
	 */
	public String getJsonString() throws LogicException {
		try {
			if (this.isEmpty())
				return "";
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());

			throw new LogicException("CE000", "TempVo to String 轉換失敗 : " + this);
		}
	}

}
