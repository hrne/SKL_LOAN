package com.st1.ifx.service.springjpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;
import org.springframework.util.StopWatch;

import com.st1.ifx.domain.Txcd;
import com.st1.ifx.repository.TxcdRepository;
import com.st1.ifx.service.TxcdService;

//**更新help版號時同步更新四角號碼和MENU選單

@Service("txcdService")
@Repository
@Transactional
public class TxcdServiceImpl implements TxcdService, InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(TxcdServiceImpl.class);

	@Autowired
	private TxcdRepository repository;

	@PersistenceContext
	private EntityManager em;

	@Override
	@CacheEvict(value = { "txcdfnone", "txcdfntwo", "txcdtern" }, allEntries = true)
	public Txcd save(Txcd txcd) {
		// log.info("saving:" + txcd.getTxcd());
		return repository.save(txcd);
	}

	@Override
	@CacheEvict(value = { "txcdfnone", "txcdfntwo", "txcdtern" }, allEntries = true)
	public void delete(Txcd txcd) {
		// log.info("deleting:" + txcd.getTxcd());
		repository.delete(txcd);

	}

	@Override
	@Transactional(readOnly = true)
	public Txcd findById(String txcode) {
		// log.info("finding:" + txcode);
		Txcd txcd = null;
		Optional<Txcd> td = repository.findById(txcode);
		if (td.isPresent())
			txcd = td.get();
		else
			txcd = null;
		return txcd;
	}

	@Override
	@CacheEvict(value = { "txcdfnone", "txcdfntwo", "txcdtern" }, allEntries = true)
	public void removeAll() {
		// log.info("remove all...");
		// repository.deleteAll();
		Query q = em.createQuery("delete from Txcd t1");
		int i = q.executeUpdate();
		// log.info("remove all...done!!, remove " + i + " records");

	}

	@Override
	@Cacheable("txcdfnone") // 20180118 潘
	@Transactional(readOnly = true)
	public List<Txcd> findNotTypeOf(int type) {
		// log.info("find not type of: " + type);
		List<Txcd> list = repository.findNotTypeOf(type);
		for (Txcd t : list)
			t.setTxdnm(t.getTxdnm().trim());
		return list;
	}

	@Override
	@Cacheable("txcdfntwo") // 20180118 潘
	@Transactional(readOnly = true)
	public List<Txcd> findNotTypeOf(int type, String menuName) {
		StopWatch watch = new StopWatch();
		watch.start();
		List<Txcd> list = findNotTypeOf(type);
		List<Txcd> result = new ArrayList<Txcd>();
		boolean bRootLevel = menuName == null || menuName.length() == 0;
		String txcode;
		if (bRootLevel) { // find A, B,C...
			for (Txcd t : list) {
				txcode = t.getTxcd().trim();
				// if (txcode.length() == 1) //testmenu
				if (txcode.length() == 2 && txcode.startsWith("L"))
					result.add(t);
			}
		} else { // find Axxxx, Bxxxx,...
			for (Txcd t : list) {
				txcode = t.getTxcd().trim();
				if (txcode.startsWith(menuName))
					result.add(t);
			}
		}
		watch.stop();
		log.info("Total execution time to get txcd list(findNotTypeOf) in millis: " + watch.getTotalTimeMillis());
		return result;
	}

	@Override
	@Cacheable("txcdtern") // 20180118 潘
	@Transactional(readOnly = true)
	public List<String> findTxcdterm(int type, String term) {
		// log.info("findTxcdterm");
		try {
			Query q = em.createQuery(
					"select concat(c.txcd,' ',trim(c.txdnm)) from Txcd c where c.txcd like :term and c.type=:type ");
			q.setParameter("term", term + '%');
			q.setParameter("type", type);
			@SuppressWarnings("unchecked")
			List<String> result = q.getResultList();
			log.info("find txcd term size:" + result.size());
			if (result.size() > 0) {
				log.info("find txcd return");
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("Exception e1:" + e.toString());
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(repository);

	}

	@Override
	@CacheEvict(value = { "txcdfnone", "txcdfntwo", "txcdtern" }, allEntries = true)
	public void evict() {
		log.info("evict txcd");
	}

}
