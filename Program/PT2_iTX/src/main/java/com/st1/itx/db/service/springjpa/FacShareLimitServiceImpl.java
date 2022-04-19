package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

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
import com.st1.itx.db.domain.FacShareLimit;
import com.st1.itx.db.repository.online.FacShareLimitRepository;
import com.st1.itx.db.repository.day.FacShareLimitRepositoryDay;
import com.st1.itx.db.repository.mon.FacShareLimitRepositoryMon;
import com.st1.itx.db.repository.hist.FacShareLimitRepositoryHist;
import com.st1.itx.db.service.FacShareLimitService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facShareLimitService")
@Repository
public class FacShareLimitServiceImpl extends ASpringJpaParm implements FacShareLimitService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private FacShareLimitRepository facShareLimitRepos;

	@Autowired
	private FacShareLimitRepositoryDay facShareLimitReposDay;

	@Autowired
	private FacShareLimitRepositoryMon facShareLimitReposMon;

	@Autowired
	private FacShareLimitRepositoryHist facShareLimitReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(facShareLimitRepos);
		org.junit.Assert.assertNotNull(facShareLimitReposDay);
		org.junit.Assert.assertNotNull(facShareLimitReposMon);
		org.junit.Assert.assertNotNull(facShareLimitReposHist);
	}

	@Override
	public FacShareLimit findById(int applNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + applNo);
		Optional<FacShareLimit> facShareLimit = null;
		if (dbName.equals(ContentName.onDay))
			facShareLimit = facShareLimitReposDay.findById(applNo);
		else if (dbName.equals(ContentName.onMon))
			facShareLimit = facShareLimitReposMon.findById(applNo);
		else if (dbName.equals(ContentName.onHist))
			facShareLimit = facShareLimitReposHist.findById(applNo);
		else
			facShareLimit = facShareLimitRepos.findById(applNo);
		FacShareLimit obj = facShareLimit.isPresent() ? facShareLimit.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<FacShareLimit> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacShareLimit> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ApplNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ApplNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = facShareLimitReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facShareLimitReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facShareLimitReposHist.findAll(pageable);
		else
			slice = facShareLimitRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacShareLimit> findMainApplNoEq(int mainApplNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacShareLimit> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findMainApplNoEq " + dbName + " : " + "mainApplNo_0 : " + mainApplNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = facShareLimitReposDay.findAllByMainApplNoIsOrderByKeyinSeqAsc(mainApplNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facShareLimitReposMon.findAllByMainApplNoIsOrderByKeyinSeqAsc(mainApplNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facShareLimitReposHist.findAllByMainApplNoIsOrderByKeyinSeqAsc(mainApplNo_0, pageable);
		else
			slice = facShareLimitRepos.findAllByMainApplNoIsOrderByKeyinSeqAsc(mainApplNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacShareLimit> findCustNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacShareLimit> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCustNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = facShareLimitReposDay.findAllByCustNoIsOrderByMainApplNoAscKeyinSeqAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facShareLimitReposMon.findAllByCustNoIsOrderByMainApplNoAscKeyinSeqAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facShareLimitReposHist.findAllByCustNoIsOrderByMainApplNoAscKeyinSeqAsc(custNo_0, pageable);
		else
			slice = facShareLimitRepos.findAllByCustNoIsOrderByMainApplNoAscKeyinSeqAsc(custNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacShareLimit holdById(int applNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + applNo);
		Optional<FacShareLimit> facShareLimit = null;
		if (dbName.equals(ContentName.onDay))
			facShareLimit = facShareLimitReposDay.findByApplNo(applNo);
		else if (dbName.equals(ContentName.onMon))
			facShareLimit = facShareLimitReposMon.findByApplNo(applNo);
		else if (dbName.equals(ContentName.onHist))
			facShareLimit = facShareLimitReposHist.findByApplNo(applNo);
		else
			facShareLimit = facShareLimitRepos.findByApplNo(applNo);
		return facShareLimit.isPresent() ? facShareLimit.get() : null;
	}

	@Override
	public FacShareLimit holdById(FacShareLimit facShareLimit, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facShareLimit.getApplNo());
		Optional<FacShareLimit> facShareLimitT = null;
		if (dbName.equals(ContentName.onDay))
			facShareLimitT = facShareLimitReposDay.findByApplNo(facShareLimit.getApplNo());
		else if (dbName.equals(ContentName.onMon))
			facShareLimitT = facShareLimitReposMon.findByApplNo(facShareLimit.getApplNo());
		else if (dbName.equals(ContentName.onHist))
			facShareLimitT = facShareLimitReposHist.findByApplNo(facShareLimit.getApplNo());
		else
			facShareLimitT = facShareLimitRepos.findByApplNo(facShareLimit.getApplNo());
		return facShareLimitT.isPresent() ? facShareLimitT.get() : null;
	}

	@Override
	public FacShareLimit insert(FacShareLimit facShareLimit, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + facShareLimit.getApplNo());
		if (this.findById(facShareLimit.getApplNo()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			facShareLimit.setCreateEmpNo(empNot);

		if (facShareLimit.getLastUpdateEmpNo() == null || facShareLimit.getLastUpdateEmpNo().isEmpty())
			facShareLimit.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facShareLimitReposDay.saveAndFlush(facShareLimit);
		else if (dbName.equals(ContentName.onMon))
			return facShareLimitReposMon.saveAndFlush(facShareLimit);
		else if (dbName.equals(ContentName.onHist))
			return facShareLimitReposHist.saveAndFlush(facShareLimit);
		else
			return facShareLimitRepos.saveAndFlush(facShareLimit);
	}

	@Override
	public FacShareLimit update(FacShareLimit facShareLimit, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + facShareLimit.getApplNo());
		if (!empNot.isEmpty())
			facShareLimit.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facShareLimitReposDay.saveAndFlush(facShareLimit);
		else if (dbName.equals(ContentName.onMon))
			return facShareLimitReposMon.saveAndFlush(facShareLimit);
		else if (dbName.equals(ContentName.onHist))
			return facShareLimitReposHist.saveAndFlush(facShareLimit);
		else
			return facShareLimitRepos.saveAndFlush(facShareLimit);
	}

	@Override
	public FacShareLimit update2(FacShareLimit facShareLimit, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + facShareLimit.getApplNo());
		if (!empNot.isEmpty())
			facShareLimit.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			facShareLimitReposDay.saveAndFlush(facShareLimit);
		else if (dbName.equals(ContentName.onMon))
			facShareLimitReposMon.saveAndFlush(facShareLimit);
		else if (dbName.equals(ContentName.onHist))
			facShareLimitReposHist.saveAndFlush(facShareLimit);
		else
			facShareLimitRepos.saveAndFlush(facShareLimit);
		return this.findById(facShareLimit.getApplNo());
	}

	@Override
	public void delete(FacShareLimit facShareLimit, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + facShareLimit.getApplNo());
		if (dbName.equals(ContentName.onDay)) {
			facShareLimitReposDay.delete(facShareLimit);
			facShareLimitReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareLimitReposMon.delete(facShareLimit);
			facShareLimitReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareLimitReposHist.delete(facShareLimit);
			facShareLimitReposHist.flush();
		} else {
			facShareLimitRepos.delete(facShareLimit);
			facShareLimitRepos.flush();
		}
	}

	@Override
	public void insertAll(List<FacShareLimit> facShareLimit, TitaVo... titaVo) throws DBException {
		if (facShareLimit == null || facShareLimit.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("InsertAll...");
		for (FacShareLimit t : facShareLimit) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			facShareLimit = facShareLimitReposDay.saveAll(facShareLimit);
			facShareLimitReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareLimit = facShareLimitReposMon.saveAll(facShareLimit);
			facShareLimitReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareLimit = facShareLimitReposHist.saveAll(facShareLimit);
			facShareLimitReposHist.flush();
		} else {
			facShareLimit = facShareLimitRepos.saveAll(facShareLimit);
			facShareLimitRepos.flush();
		}
	}

	@Override
	public void updateAll(List<FacShareLimit> facShareLimit, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (facShareLimit == null || facShareLimit.size() == 0)
			throw new DBException(6);

		for (FacShareLimit t : facShareLimit)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			facShareLimit = facShareLimitReposDay.saveAll(facShareLimit);
			facShareLimitReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareLimit = facShareLimitReposMon.saveAll(facShareLimit);
			facShareLimitReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareLimit = facShareLimitReposHist.saveAll(facShareLimit);
			facShareLimitReposHist.flush();
		} else {
			facShareLimit = facShareLimitRepos.saveAll(facShareLimit);
			facShareLimitRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<FacShareLimit> facShareLimit, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (facShareLimit == null || facShareLimit.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			facShareLimitReposDay.deleteAll(facShareLimit);
			facShareLimitReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareLimitReposMon.deleteAll(facShareLimit);
			facShareLimitReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareLimitReposHist.deleteAll(facShareLimit);
			facShareLimitReposHist.flush();
		} else {
			facShareLimitRepos.deleteAll(facShareLimit);
			facShareLimitRepos.flush();
		}
	}

}
