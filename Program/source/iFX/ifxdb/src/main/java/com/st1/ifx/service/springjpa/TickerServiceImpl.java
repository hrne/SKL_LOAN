package com.st1.ifx.service.springjpa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

import com.st1.ifx.domain.Ticker;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.repository.TickerReporitory;
import com.st1.ifx.service.TickerService;

@Service("tickerService")
@Repository
@Transactional
public class TickerServiceImpl implements TickerService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(TickerServiceImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private TickerReporitory tickerRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
	}

	@Override
	// @CacheEvict(value = "tickers", allEntries = true)
	public void save(Ticker ticker) {
		logger.info("Tickno:" + ticker.getTickno());
		if (ticker.isUniqueTick() || ticker.isUniqueSkiptick()) {
			java.util.Date today = new java.util.Date();
			long t = today.getTime();
			logger.info("Before findByBrnoAndDateAndTickno.");
			Ticker tOld = tickerRepository.findByBrnoAndDatedAndTickno(ticker.getBrno(), new java.sql.Date(t),
					ticker.getTickno());
			if (tOld != null) {
				logger.info(FilterUtils.escape("tOld:" + tOld.getId() + "," + tOld.getBrno() + "," + tOld.getTickno()));
				if (ticker.isUniqueTick()) {
					logger.info("isUniqueTick!");
					tickerRepository.deleteById(tOld.getId());
				} else {
					logger.info("isUniqueSkiptick!");
					// 跳過該跑馬燈訊息
					return;
				}
			} else {
				logger.info("no exist tOld!");
			}
		}
		logger.info("Before touch.");
		ticker.touch();
		em.persist(ticker);
	}

	@Override
	// @CacheEvict(value = "tickers", allEntries = true)
	public void save(List<Ticker> tickers) {
		for (Ticker tic : tickers) {
			logger.info("Tickno:" + tic.getTickno());
			if (tic.isUniqueTick() || tic.isUniqueSkiptick()) {
				java.util.Date today = new java.util.Date();
				long t = today.getTime();
				logger.info("Before findByBrnoAndDateAndTickno.");
				Ticker tOld = tickerRepository.findByBrnoAndDatedAndTickno(tic.getBrno(), new java.sql.Date(t),
						tic.getTickno());
				if (tOld != null) {
					logger.info(
							FilterUtils.escape("tOld:" + tOld.getId() + "," + tOld.getBrno() + "," + tOld.getTickno()));
					if (tic.isUniqueTick()) {
						logger.info("isUniqueTick!");
						tickerRepository.deleteById(tOld.getId());
					} else {
						logger.info("isUniqueSkiptick!");
						// 跳過該跑馬燈訊息
						continue;
					}
				} else {
					logger.info("no exist tOld!");
				}
			}
			logger.info("Before touch.");
			tic.touch();
			em.persist(tic);
		}
	}

	@Override
	@Transactional(readOnly = true)
	// @Cacheable("tickers")
	public String[] findValidTicker(String brno, String fdate) {
		String[] ss = new String[0];
		logger.info("db finding " + brno + " tickers");
		logger.info("fdate: " + fdate);
		// 要改成營業日期?
		String nowString = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
		if (!fdate.isEmpty()) {
			nowString = fdate + nowString.substring(8, 12);
			logger.info("nowString:" + nowString);
		}

		Long now = Long.parseLong(nowString);
		@SuppressWarnings("unchecked")
		List<String> result = em.createQuery(
				// "select concat(TRIM( c.content),' (', c.dated,' ',c.time,')') from
				// Ticker c where (c.brno=:av8d or c.brno=:brno) and c.stopTime >= :now
				// order by c.dated desc,c.time desc ")
				"select CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(TRIM(c.content),' ('), TO_CHAR(c.dated, 'YYYY-MM-DD')),' '), TO_CHAR(c.time, ' HH24:MI:SS')),')') from Ticker c where (c.brno=:av8d or c.brno=:brno) and  c.stopTime >= :now order by c.dated desc,c.time desc")
				.setParameter("av8d", "9999").setParameter("brno", brno).setParameter("now", now).getResultList();
		if (result != null && result.size() > 0) {
			ss = new String[result.size()];
			result.toArray(ss);
		}
		return ss;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Ticker> findStopTime(HashMap<String, String> requestMap) {
		String brno = requestMap.get("brno");
		Long starttime = Long.parseLong(requestMap.get("starttime"));
		Long stoptime = Long.parseLong(requestMap.get("stoptime"));
		logger.info("TickerfindStopTime : " + FilterUtils.escape(brno + starttime + stoptime));

		List<Ticker> list = null;
		try {
			list = tickerRepository.findByBrnoAndStopTimeGreaterThanEqualAndStopTimeLessThanEqualOrderByIdAsc(brno,
					starttime, stoptime);
			tickerRepository.flush();
		} catch (Exception ex) {
			logger.error("findByStopTime error :" + ex.getMessage());
		}
		return list;
	}

	@Override
	// @CacheEvict(value = "tickers", allEntries = true)
	public void evict() {
		logger.info("evict tickers");
	}

}
