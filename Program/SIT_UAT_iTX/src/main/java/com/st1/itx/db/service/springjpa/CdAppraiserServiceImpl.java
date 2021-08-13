package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdAppraiser;
import com.st1.itx.db.repository.online.CdAppraiserRepository;
import com.st1.itx.db.repository.day.CdAppraiserRepositoryDay;
import com.st1.itx.db.repository.mon.CdAppraiserRepositoryMon;
import com.st1.itx.db.repository.hist.CdAppraiserRepositoryHist;
import com.st1.itx.db.service.CdAppraiserService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdAppraiserService")
@Repository
public class CdAppraiserServiceImpl implements CdAppraiserService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(CdAppraiserServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdAppraiserRepository cdAppraiserRepos;

	@Autowired
	private CdAppraiserRepositoryDay cdAppraiserReposDay;

	@Autowired
	private CdAppraiserRepositoryMon cdAppraiserReposMon;

	@Autowired
	private CdAppraiserRepositoryHist cdAppraiserReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdAppraiserRepos);
		org.junit.Assert.assertNotNull(cdAppraiserReposDay);
		org.junit.Assert.assertNotNull(cdAppraiserReposMon);
		org.junit.Assert.assertNotNull(cdAppraiserReposHist);
	}

	@Override
	public CdAppraiser findById(String appraiserCode, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + appraiserCode);
		Optional<CdAppraiser> cdAppraiser = null;
		if (dbName.equals(ContentName.onDay))
			cdAppraiser = cdAppraiserReposDay.findById(appraiserCode);
		else if (dbName.equals(ContentName.onMon))
			cdAppraiser = cdAppraiserReposMon.findById(appraiserCode);
		else if (dbName.equals(ContentName.onHist))
			cdAppraiser = cdAppraiserReposHist.findById(appraiserCode);
		else
			cdAppraiser = cdAppraiserRepos.findById(appraiserCode);
		CdAppraiser obj = cdAppraiser.isPresent() ? cdAppraiser.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdAppraiser> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdAppraiser> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AppraiserCode"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdAppraiserReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdAppraiserReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdAppraiserReposHist.findAll(pageable);
		else
			slice = cdAppraiserRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdAppraiser holdById(String appraiserCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + appraiserCode);
		Optional<CdAppraiser> cdAppraiser = null;
		if (dbName.equals(ContentName.onDay))
			cdAppraiser = cdAppraiserReposDay.findByAppraiserCode(appraiserCode);
		else if (dbName.equals(ContentName.onMon))
			cdAppraiser = cdAppraiserReposMon.findByAppraiserCode(appraiserCode);
		else if (dbName.equals(ContentName.onHist))
			cdAppraiser = cdAppraiserReposHist.findByAppraiserCode(appraiserCode);
		else
			cdAppraiser = cdAppraiserRepos.findByAppraiserCode(appraiserCode);
		return cdAppraiser.isPresent() ? cdAppraiser.get() : null;
	}

	@Override
	public CdAppraiser holdById(CdAppraiser cdAppraiser, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + cdAppraiser.getAppraiserCode());
		Optional<CdAppraiser> cdAppraiserT = null;
		if (dbName.equals(ContentName.onDay))
			cdAppraiserT = cdAppraiserReposDay.findByAppraiserCode(cdAppraiser.getAppraiserCode());
		else if (dbName.equals(ContentName.onMon))
			cdAppraiserT = cdAppraiserReposMon.findByAppraiserCode(cdAppraiser.getAppraiserCode());
		else if (dbName.equals(ContentName.onHist))
			cdAppraiserT = cdAppraiserReposHist.findByAppraiserCode(cdAppraiser.getAppraiserCode());
		else
			cdAppraiserT = cdAppraiserRepos.findByAppraiserCode(cdAppraiser.getAppraiserCode());
		return cdAppraiserT.isPresent() ? cdAppraiserT.get() : null;
	}

	@Override
	public CdAppraiser insert(CdAppraiser cdAppraiser, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + cdAppraiser.getAppraiserCode());
		if (this.findById(cdAppraiser.getAppraiserCode()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdAppraiser.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdAppraiserReposDay.saveAndFlush(cdAppraiser);
		else if (dbName.equals(ContentName.onMon))
			return cdAppraiserReposMon.saveAndFlush(cdAppraiser);
		else if (dbName.equals(ContentName.onHist))
			return cdAppraiserReposHist.saveAndFlush(cdAppraiser);
		else
			return cdAppraiserRepos.saveAndFlush(cdAppraiser);
	}

	@Override
	public CdAppraiser update(CdAppraiser cdAppraiser, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + cdAppraiser.getAppraiserCode());
		if (!empNot.isEmpty())
			cdAppraiser.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdAppraiserReposDay.saveAndFlush(cdAppraiser);
		else if (dbName.equals(ContentName.onMon))
			return cdAppraiserReposMon.saveAndFlush(cdAppraiser);
		else if (dbName.equals(ContentName.onHist))
			return cdAppraiserReposHist.saveAndFlush(cdAppraiser);
		else
			return cdAppraiserRepos.saveAndFlush(cdAppraiser);
	}

	@Override
	public CdAppraiser update2(CdAppraiser cdAppraiser, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + cdAppraiser.getAppraiserCode());
		if (!empNot.isEmpty())
			cdAppraiser.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdAppraiserReposDay.saveAndFlush(cdAppraiser);
		else if (dbName.equals(ContentName.onMon))
			cdAppraiserReposMon.saveAndFlush(cdAppraiser);
		else if (dbName.equals(ContentName.onHist))
			cdAppraiserReposHist.saveAndFlush(cdAppraiser);
		else
			cdAppraiserRepos.saveAndFlush(cdAppraiser);
		return this.findById(cdAppraiser.getAppraiserCode());
	}

	@Override
	public void delete(CdAppraiser cdAppraiser, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + cdAppraiser.getAppraiserCode());
		if (dbName.equals(ContentName.onDay)) {
			cdAppraiserReposDay.delete(cdAppraiser);
			cdAppraiserReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdAppraiserReposMon.delete(cdAppraiser);
			cdAppraiserReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdAppraiserReposHist.delete(cdAppraiser);
			cdAppraiserReposHist.flush();
		} else {
			cdAppraiserRepos.delete(cdAppraiser);
			cdAppraiserRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdAppraiser> cdAppraiser, TitaVo... titaVo) throws DBException {
		if (cdAppraiser == null || cdAppraiser.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (CdAppraiser t : cdAppraiser)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdAppraiser = cdAppraiserReposDay.saveAll(cdAppraiser);
			cdAppraiserReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdAppraiser = cdAppraiserReposMon.saveAll(cdAppraiser);
			cdAppraiserReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdAppraiser = cdAppraiserReposHist.saveAll(cdAppraiser);
			cdAppraiserReposHist.flush();
		} else {
			cdAppraiser = cdAppraiserRepos.saveAll(cdAppraiser);
			cdAppraiserRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdAppraiser> cdAppraiser, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (cdAppraiser == null || cdAppraiser.size() == 0)
			throw new DBException(6);

		for (CdAppraiser t : cdAppraiser)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdAppraiser = cdAppraiserReposDay.saveAll(cdAppraiser);
			cdAppraiserReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdAppraiser = cdAppraiserReposMon.saveAll(cdAppraiser);
			cdAppraiserReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdAppraiser = cdAppraiserReposHist.saveAll(cdAppraiser);
			cdAppraiserReposHist.flush();
		} else {
			cdAppraiser = cdAppraiserRepos.saveAll(cdAppraiser);
			cdAppraiserRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdAppraiser> cdAppraiser, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdAppraiser == null || cdAppraiser.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdAppraiserReposDay.deleteAll(cdAppraiser);
			cdAppraiserReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdAppraiserReposMon.deleteAll(cdAppraiser);
			cdAppraiserReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdAppraiserReposHist.deleteAll(cdAppraiser);
			cdAppraiserReposHist.flush();
		} else {
			cdAppraiserRepos.deleteAll(cdAppraiser);
			cdAppraiserRepos.flush();
		}
	}

}
