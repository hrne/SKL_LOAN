package com.st1.ifx.service.springjpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

import com.st1.ifx.domain.TranDocLog;
import com.st1.ifx.repository.TranDocLogRepository;
import com.st1.ifx.service.TranDocLogService;

@Service("tranDocLogService")
@Repository
@Transactional
public class TranDocLogServiceImpl implements TranDocLogService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(TranDocLogServiceImpl.class);

	@Autowired
	private TranDocLogRepository repository;

	@PersistenceContext
	private EntityManager em;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(repository);

	}

	@Override
	public TranDocLog save(TranDocLog tranDocLog) {
		return this.repository.save(tranDocLog);

	}

	@Override
	@Transactional(readOnly = true)
	public List<TranDocLog> findByDocId(Long docId) {
		return this.repository.findByDocId(docId);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getDupCounts(Long docId) {
		return this.repository.getDupCounts(docId);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getDupbrnCounts(Long docId, String brn) {
		return this.repository.getDupbrnCounts(docId, brn);
	}
}
