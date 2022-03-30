package com.st1.ifx.service.springjpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

import com.st1.ifx.domain.ConsoleLog;
import com.st1.ifx.repository.ConsoleLogRepository;
import com.st1.ifx.service.ConsoleLogService;

@Service("consoleLogService")
@Repository
@Transactional
public class ConsoleLogServiceImpl implements ConsoleLogService, InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(ConsoleLogServiceImpl.class);

	private static final int PAGE_SIZE = 10;

	@Autowired
	private ConsoleLogRepository repos;

	@PersistenceContext
	private EntityManager em;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(repos);

	}

	@Override
	public Page<ConsoleLog> getConsoleLog(String brno, java.sql.Date date, Integer pageNumber) {

		PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "time");
		return repos.findByBrnoAndDate(brno, date, request);
	}

	@Override
	public void save(String orig, String brno, String level, String text) {
		ConsoleLog consoleLog = new ConsoleLog();
		consoleLog.setOrig(orig);
		consoleLog.setBrno(brno);
		consoleLog.setLevel(level);
		consoleLog.setText(text);
		consoleLog.touch();
		log.info(consoleLog.toString());
		repos.save(consoleLog);
	}

}
