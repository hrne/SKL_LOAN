package com.st1.itx.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.ResultTransformer;

public class AliasToEntityLinkHashMapResultTransformer implements ResultTransformer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1706758718600996106L;
	public static final AliasToEntityLinkHashMapResultTransformer INSTANCE = new AliasToEntityLinkHashMapResultTransformer();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		// 此处Map申明为LinkedHashMap, 即可使字段排序保持查询时的顺序
		Map result = new LinkedHashMap(tuple.length);
		for (int i = 0; i < tuple.length; ++i) {
			String alias = aliases[i];
			if (alias != null) {
				result.put(alias, tuple[i]);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List transformList(List list) {
		return list;
	}
}