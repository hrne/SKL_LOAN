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

	public List<Map<String, String>> convertToMap(List<Object> list) {
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
	
	public List<Map<String, String>> convertToMap(Query query, boolean isUpdate) {
		if (isUpdate) {
			query.executeUpdate();
			return new ArrayList<Map<String, String>>();
		} else
			return this.convertToMap(query);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, String>> convertToMap(Query query) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityLinkHashMapResultTransformer.INSTANCE);
//		.setResultTransformer(Transformers.aliasToBean(new LinkedHashMap<String, Object>().getClass()));
//		.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = query.getResultList();

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

				m.put("F" + Integer.toString(i), value);
				m.put(key, value);
				i++;
			}
			result.add(m);
		}
		this.info("result:" + result.size());
		return result;
	}
}
