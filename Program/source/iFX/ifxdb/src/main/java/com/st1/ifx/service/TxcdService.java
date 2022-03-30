package com.st1.ifx.service;

import java.util.List;

import com.st1.ifx.domain.Txcd;

public interface TxcdService {
	public Txcd save(Txcd txcd);

	public void delete(Txcd txcd);

	public Txcd findById(String id);

	public void removeAll();

	public List<Txcd> findNotTypeOf(int type);

	public List<Txcd> findNotTypeOf(int type, String menuName);

	public List<String> findTxcdterm(int type, String term);

	public void evict();
}
