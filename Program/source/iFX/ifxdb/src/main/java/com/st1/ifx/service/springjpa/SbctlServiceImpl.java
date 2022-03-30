package com.st1.ifx.service.springjpa;

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

import com.st1.ifx.domain.Sbctl;
import com.st1.ifx.domain.SbctlId;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.repository.SbctlRepository;
import com.st1.ifx.service.SbctlService;

@Service("sbctlService")
@Repository
@Transactional
public class SbctlServiceImpl implements SbctlService, InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(SbctlServiceImpl.class);

	@Autowired
	private SbctlRepository repository;

	@PersistenceContext
	private EntityManager em;

	@Override
	public Sbctl save(Sbctl sbctl) {
		log.info("saving:" + "SbctlId [type=" + sbctl.getType() + ", brno=" + sbctl.getBrno() + ", tlrno=" + sbctl.getTlrno() + ", sbtyp=" + sbctl.getSbtyp() + "]");
		return repository.save(sbctl);
	}

	@Override
	@CacheEvict(value = "sbctl", allEntries = true)
	public void removeAll() {
		log.info("remove all...");
		// repository.deleteAll();
		Query q = em.createQuery("delete from Sbctl s1");
		int i = q.executeUpdate();
		log.info("remove all...done!!, remove " + i + " records");
		// log.info("remove all...done!!");

	}

	@Override
	@CacheEvict(value = "sbctl", allEntries = true)
	public void delete(Sbctl sbctl) {
		log.info(FilterUtils.escape("deleting:" + "SbctlId [type=" + sbctl.getType() + ", brno=" + sbctl.getBrno() + ", tlrno=" + sbctl.getTlrno() + ", sbtyp=" + sbctl.getSbtyp() + "]"));
		repository.delete(sbctl);

	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable("sbctl")
	public Sbctl findById(int type, String brno, String tlrno, String sbtyp) {
		Optional<Sbctl> sbctl = repository.findById(new SbctlId(type, brno, tlrno, sbtyp));
		if (sbctl.isPresent())
			return sbctl.get();
		else
			return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(em);
		Assert.assertNotNull(repository);

	}

}
