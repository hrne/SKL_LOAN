package com.st1.itx.db.service.springjpa;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.hibernate.query.internal.NativeQueryImpl;
import com.st1.itx.config.AliasToEntityLinkHashMapResultTransformer;
import com.st1.itx.util.log.SysLogger;

public class ASpringJpaParm extends SysLogger {

	// *** 折返控制相關 ***
	protected int index;

	// *** 折返控制相關 ***
	protected int limit;

	// *** 折返控制相關 ***
	protected int pageCount = 0;

	// *** 折返控制相關 ***
	protected int totalSize = 0;

	protected List<Map<String, String>> convertToMap(List<Object> list) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		try {
			for (Iterator<Object> iter = list.iterator(); iter.hasNext();) {
				Object[] values = (Object[]) iter.next();
				Map<String, String> m = new LinkedHashMap<String, String>();
				for (int i = 0; i < values.length; i++)
					m.put("F" + Integer.toString(i), values[i] == null ? "" : values[i].toString());
				result.add(m);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
		this.info("result:" + result.size());
		return result;
	}

	protected List<Map<String, String>> convertToMap(Query query, boolean isUpdate) {
		if (isUpdate) {
			query.executeUpdate();
			return new ArrayList<Map<String, String>>();
		} else
			return this.convertToMap(query);
	}

	// 設定回傳的資料具有Query定義的欄位名稱
	private Query preparedQuery(Query query) {
		query = query.unwrap(NativeQueryImpl.class)
				.setResultTransformer(AliasToEntityLinkHashMapResultTransformer.INSTANCE);
//		.setResultTransformer(Transformers.aliasToBean(new LinkedHashMap<String, Object>().getClass()));
//		.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query;
	}

	protected List<Map<String, String>> convertToMap(Query query) {
		query = preparedQuery(query);
		List rows = query.getResultList();
		return convert(rows);
	}

	private List<Map<String, String>> convert(List rows) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (Object obj : rows) {
			Map<String, Object> row = (LinkedHashMap<String, Object>) obj;
			Map<String, String> m = new LinkedHashMap<String, String>();
			Set<String> set = row.keySet();
			Iterator<String> it = set.iterator();
			int i = 0;
			while (it.hasNext()) {
				String key = it.next();
				String value = "";

				if (row.get(key) != null && row.get(key) instanceof Clob) {
					Clob c = (Clob) row.get(key);
					try {
						value = c.getSubString(1, (int) c.length());
					} catch (SQLException e) {
						m.put(key, "");
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						this.error(errors.toString());
					}
				} else if (row.get(key) != null)
					value = row.get(key).toString();

				m.put(key, value);
				m.put("F" + Integer.toString(i), value);
				i++;
			}
			result.add(m);
		}
		this.info("result:" + result.size());
		return result;
	}

	// *** 折返控制相關 ***
	/**
	 * 具折返控制的查詢方式
	 * 
	 * @param query 查詢語句
	 * @return 查詢結果
	 * @throws Exception 查詢相關的Exception
	 */
	protected List<Map<String, String>> switchback(Query query) throws Exception {

		// *** 折返控制相關 ***
		// 只有第一次進來的時候多查詢一次拿總筆數
		if (this.index == 0) {
			totalSize = query.getResultList().size();
		}

//		// *** 折返控制相關 ***
//		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(this.index * this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMapWithQueryByPage(query);
	}

	// *** 折返控制相關 ***
	private List<Map<String, String>> convertToMapWithQueryByPage(Query query) {
		query = preparedQuery(query);
		List rows = query.getResultList();
		pageCount = rows.size(); // 紀錄本次筆數
		this.info("pageCount ..." + pageCount);
		return convert(rows);
	}

	// *** 折返控制相關 ***
	public boolean hasNext() {
		return (this.index * this.limit + this.pageCount) < this.totalSize;
	}
}
