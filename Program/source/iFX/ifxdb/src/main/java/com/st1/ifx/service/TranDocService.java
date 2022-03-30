package com.st1.ifx.service;

import java.util.HashMap;
import java.util.List;

import com.st1.ifx.domain.TranDoc;

public interface TranDocService {
	public List<TranDoc> findByJnlId(Long jnlId, boolean bLazy);

	public TranDoc save(TranDoc tmpTranDoc, Long jnlId, String docName, String docPrompt, String docParameter,
			String content);

	public void save(List<TranDoc> tranDocList);

	public TranDoc getByDocId(Long docId);

	public void deleteByJnlId(Long jnlId);

	public List<TranDoc> findByCriteriaQuery(HashMap<String, String> requestMap);

	@SuppressWarnings("rawtypes")
	public List findByCriteriaQuerylast(HashMap<String, String> requestMap);

}
