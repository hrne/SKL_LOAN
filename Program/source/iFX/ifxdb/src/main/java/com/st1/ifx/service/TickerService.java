package com.st1.ifx.service;

import java.util.HashMap;
import java.util.List;

import com.st1.ifx.domain.Ticker;

public interface TickerService {
	public void save(Ticker ticker);

	public void save(List<Ticker> tickers);

	public String[] findValidTicker(String brno, String fdate);

	public List<Ticker> findStopTime(HashMap<String, String> requestMap);

	public void evict();
}
