package com.st1.ifx.service;

import java.util.List;

import com.st1.ifx.domain.LocalRim;
import com.st1.ifx.file.item.rim.RimLine;

public interface LocalRimService {
	public LocalRim save(LocalRim localRim);

	public void removeTable(String tableName);

	public void removeKey(String tableName, String key);

	public LocalRim find(String tableName, String key);

	public LocalRim findnocache(String tableName, String key);

	public void updateList(List<RimLine> list);

	public void updateByFlag(RimLine rimLine);

	public List<LocalRim> findAll(String tableName);

	@SuppressWarnings("rawtypes")

	public List findLike1(String tableName, String matchKey);

	public List<LocalRim> findLike2(String tableName, String matchKey);

	public void evict();

	void findtablealltoKey(String tableName, String tokey, String recvTime);
}
