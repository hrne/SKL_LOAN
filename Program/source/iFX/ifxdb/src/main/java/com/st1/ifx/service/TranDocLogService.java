package com.st1.ifx.service;

import java.util.List;

import com.st1.ifx.domain.TranDocLog;

public interface TranDocLogService {
	public TranDocLog save(TranDocLog tranDocLog);

	public List<TranDocLog> findByDocId(Long docId);

	public Long getDupCounts(Long docId);

	public Long getDupbrnCounts(Long docId, String brn);

}
