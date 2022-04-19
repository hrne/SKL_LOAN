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
import com.st1.itx.db.domain.CdSpecParms;
import com.st1.itx.db.domain.CdSpecParmsId;
import com.st1.itx.db.repository.online.CdSpecParmsRepository;
import com.st1.itx.db.repository.day.CdSpecParmsRepositoryDay;
import com.st1.itx.db.repository.mon.CdSpecParmsRepositoryMon;
import com.st1.itx.db.repository.hist.CdSpecParmsRepositoryHist;
import com.st1.itx.db.service.CdSpecParmsService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdSpecParmsService")
@Repository
public class CdSpecParmsServiceImpl extends ASpringJpaParm implements CdSpecParmsService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(CdSpecParmsServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdSpecParmsRepository cdSpecParmsRepos;

	@Autowired
	private CdSpecParmsRepositoryDay cdSpecParmsReposDay;

	@Autowired
	private CdSpecParmsRepositoryMon cdSpecParmsReposMon;

	@Autowired
	private CdSpecParmsRepositoryHist cdSpecParmsReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdSpecParmsRepos);
		org.junit.Assert.assertNotNull(cdSpecParmsReposDay);
		org.junit.Assert.assertNotNull(cdSpecParmsReposMon);
		org.junit.Assert.assertNotNull(cdSpecParmsReposHist);
	}

	@Override
	public CdSpecParms findById(CdSpecParmsId cdSpecParmsId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + cdSpecParmsId);
		Optional<CdSpecParms> cdSpecParms = null;
		if (dbName.equals(ContentName.onDay))
			cdSpecParms = cdSpecParmsReposDay.findById(cdSpecParmsId);
		else if (dbName.equals(ContentName.onMon))
			cdSpecParms = cdSpecParmsReposMon.findById(cdSpecParmsId);
		else if (dbName.equals(ContentName.onHist))
			cdSpecParms = cdSpecParmsReposHist.findById(cdSpecParmsId);
		else
			cdSpecParms = cdSpecParmsRepos.findById(cdSpecParmsId);
		CdSpecParms obj = cdSpecParms.isPresent() ? cdSpecParms.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdSpecParms> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdSpecParms> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ConditionCode", "Condition"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdSpecParmsReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdSpecParmsReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdSpecParmsReposHist.findAll(pageable);
		else
			slice = cdSpecParmsRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdSpecParms holdById(CdSpecParmsId cdSpecParmsId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdSpecParmsId);
		Optional<CdSpecParms> cdSpecParms = null;
		if (dbName.equals(ContentName.onDay))
			cdSpecParms = cdSpecParmsReposDay.findByCdSpecParmsId(cdSpecParmsId);
		else if (dbName.equals(ContentName.onMon))
			cdSpecParms = cdSpecParmsReposMon.findByCdSpecParmsId(cdSpecParmsId);
		else if (dbName.equals(ContentName.onHist))
			cdSpecParms = cdSpecParmsReposHist.findByCdSpecParmsId(cdSpecParmsId);
		else
			cdSpecParms = cdSpecParmsRepos.findByCdSpecParmsId(cdSpecParmsId);
		return cdSpecParms.isPresent() ? cdSpecParms.get() : null;
	}

	@Override
	public CdSpecParms holdById(CdSpecParms cdSpecParms, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdSpecParms.getCdSpecParmsId());
		Optional<CdSpecParms> cdSpecParmsT = null;
		if (dbName.equals(ContentName.onDay))
			cdSpecParmsT = cdSpecParmsReposDay.findByCdSpecParmsId(cdSpecParms.getCdSpecParmsId());
		else if (dbName.equals(ContentName.onMon))
			cdSpecParmsT = cdSpecParmsReposMon.findByCdSpecParmsId(cdSpecParms.getCdSpecParmsId());
		else if (dbName.equals(ContentName.onHist))
			cdSpecParmsT = cdSpecParmsReposHist.findByCdSpecParmsId(cdSpecParms.getCdSpecParmsId());
		else
			cdSpecParmsT = cdSpecParmsRepos.findByCdSpecParmsId(cdSpecParms.getCdSpecParmsId());
		return cdSpecParmsT.isPresent() ? cdSpecParmsT.get() : null;
	}

	@Override
	public CdSpecParms insert(CdSpecParms cdSpecParms, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + cdSpecParms.getCdSpecParmsId());
		if (this.findById(cdSpecParms.getCdSpecParmsId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdSpecParms.setCreateEmpNo(empNot);

		if (cdSpecParms.getLastUpdateEmpNo() == null || cdSpecParms.getLastUpdateEmpNo().isEmpty())
			cdSpecParms.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdSpecParmsReposDay.saveAndFlush(cdSpecParms);
		else if (dbName.equals(ContentName.onMon))
			return cdSpecParmsReposMon.saveAndFlush(cdSpecParms);
		else if (dbName.equals(ContentName.onHist))
			return cdSpecParmsReposHist.saveAndFlush(cdSpecParms);
		else
			return cdSpecParmsRepos.saveAndFlush(cdSpecParms);
	}

	@Override
	public CdSpecParms update(CdSpecParms cdSpecParms, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + cdSpecParms.getCdSpecParmsId());
		if (!empNot.isEmpty())
			cdSpecParms.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdSpecParmsReposDay.saveAndFlush(cdSpecParms);
		else if (dbName.equals(ContentName.onMon))
			return cdSpecParmsReposMon.saveAndFlush(cdSpecParms);
		else if (dbName.equals(ContentName.onHist))
			return cdSpecParmsReposHist.saveAndFlush(cdSpecParms);
		else
			return cdSpecParmsRepos.saveAndFlush(cdSpecParms);
	}

	@Override
	public CdSpecParms update2(CdSpecParms cdSpecParms, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + cdSpecParms.getCdSpecParmsId());
		if (!empNot.isEmpty())
			cdSpecParms.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdSpecParmsReposDay.saveAndFlush(cdSpecParms);
		else if (dbName.equals(ContentName.onMon))
			cdSpecParmsReposMon.saveAndFlush(cdSpecParms);
		else if (dbName.equals(ContentName.onHist))
			cdSpecParmsReposHist.saveAndFlush(cdSpecParms);
		else
			cdSpecParmsRepos.saveAndFlush(cdSpecParms);
		return this.findById(cdSpecParms.getCdSpecParmsId());
	}

	@Override
	public void delete(CdSpecParms cdSpecParms, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdSpecParms.getCdSpecParmsId());
		if (dbName.equals(ContentName.onDay)) {
			cdSpecParmsReposDay.delete(cdSpecParms);
			cdSpecParmsReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSpecParmsReposMon.delete(cdSpecParms);
			cdSpecParmsReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSpecParmsReposHist.delete(cdSpecParms);
			cdSpecParmsReposHist.flush();
		} else {
			cdSpecParmsRepos.delete(cdSpecParms);
			cdSpecParmsRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdSpecParms> cdSpecParms, TitaVo... titaVo) throws DBException {
		if (cdSpecParms == null || cdSpecParms.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (CdSpecParms t : cdSpecParms) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdSpecParms = cdSpecParmsReposDay.saveAll(cdSpecParms);
			cdSpecParmsReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSpecParms = cdSpecParmsReposMon.saveAll(cdSpecParms);
			cdSpecParmsReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSpecParms = cdSpecParmsReposHist.saveAll(cdSpecParms);
			cdSpecParmsReposHist.flush();
		} else {
			cdSpecParms = cdSpecParmsRepos.saveAll(cdSpecParms);
			cdSpecParmsRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdSpecParms> cdSpecParms, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (cdSpecParms == null || cdSpecParms.size() == 0)
			throw new DBException(6);

		for (CdSpecParms t : cdSpecParms)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdSpecParms = cdSpecParmsReposDay.saveAll(cdSpecParms);
			cdSpecParmsReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSpecParms = cdSpecParmsReposMon.saveAll(cdSpecParms);
			cdSpecParmsReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSpecParms = cdSpecParmsReposHist.saveAll(cdSpecParms);
			cdSpecParmsReposHist.flush();
		} else {
			cdSpecParms = cdSpecParmsRepos.saveAll(cdSpecParms);
			cdSpecParmsRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdSpecParms> cdSpecParms, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdSpecParms == null || cdSpecParms.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdSpecParmsReposDay.deleteAll(cdSpecParms);
			cdSpecParmsReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSpecParmsReposMon.deleteAll(cdSpecParms);
			cdSpecParmsReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSpecParmsReposHist.deleteAll(cdSpecParms);
			cdSpecParmsReposHist.flush();
		} else {
			cdSpecParmsRepos.deleteAll(cdSpecParms);
			cdSpecParmsRepos.flush();
		}
	}

}
