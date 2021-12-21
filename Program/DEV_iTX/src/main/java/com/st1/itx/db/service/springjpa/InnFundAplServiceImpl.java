package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

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
import com.st1.itx.db.domain.InnFundApl;
import com.st1.itx.db.repository.online.InnFundAplRepository;
import com.st1.itx.db.repository.day.InnFundAplRepositoryDay;
import com.st1.itx.db.repository.mon.InnFundAplRepositoryMon;
import com.st1.itx.db.repository.hist.InnFundAplRepositoryHist;
import com.st1.itx.db.service.InnFundAplService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("innFundAplService")
@Repository
public class InnFundAplServiceImpl extends ASpringJpaParm implements InnFundAplService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private InnFundAplRepository innFundAplRepos;

	@Autowired
	private InnFundAplRepositoryDay innFundAplReposDay;

	@Autowired
	private InnFundAplRepositoryMon innFundAplReposMon;

	@Autowired
	private InnFundAplRepositoryHist innFundAplReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(innFundAplRepos);
		org.junit.Assert.assertNotNull(innFundAplReposDay);
		org.junit.Assert.assertNotNull(innFundAplReposMon);
		org.junit.Assert.assertNotNull(innFundAplReposHist);
	}

	@Override
	public InnFundApl findById(int acDate, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + acDate);
		Optional<InnFundApl> innFundApl = null;
		if (dbName.equals(ContentName.onDay))
			innFundApl = innFundAplReposDay.findById(acDate);
		else if (dbName.equals(ContentName.onMon))
			innFundApl = innFundAplReposMon.findById(acDate);
		else if (dbName.equals(ContentName.onHist))
			innFundApl = innFundAplReposHist.findById(acDate);
		else
			innFundApl = innFundAplRepos.findById(acDate);
		InnFundApl obj = innFundApl.isPresent() ? innFundApl.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<InnFundApl> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InnFundApl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = innFundAplReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = innFundAplReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = innFundAplReposHist.findAll(pageable);
		else
			slice = innFundAplRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<InnFundApl> acDateYearEq(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InnFundApl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("acDateYearEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " + acDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = innFundAplReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acDate_0, acDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = innFundAplReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acDate_0, acDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = innFundAplReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acDate_0, acDate_1, pageable);
		else
			slice = innFundAplRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acDate_0, acDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public InnFundApl acDateFirst(BigDecimal resrvStndrd_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("acDateFirst " + dbName + " : " + "resrvStndrd_0 : " + resrvStndrd_0);
		Optional<InnFundApl> innFundAplT = null;
		if (dbName.equals(ContentName.onDay))
			innFundAplT = innFundAplReposDay.findTopByResrvStndrdGreaterThanOrderByAcDateDesc(resrvStndrd_0);
		else if (dbName.equals(ContentName.onMon))
			innFundAplT = innFundAplReposMon.findTopByResrvStndrdGreaterThanOrderByAcDateDesc(resrvStndrd_0);
		else if (dbName.equals(ContentName.onHist))
			innFundAplT = innFundAplReposHist.findTopByResrvStndrdGreaterThanOrderByAcDateDesc(resrvStndrd_0);
		else
			innFundAplT = innFundAplRepos.findTopByResrvStndrdGreaterThanOrderByAcDateDesc(resrvStndrd_0);

		return innFundAplT.isPresent() ? innFundAplT.get() : null;
	}

	@Override
	public InnFundApl holdById(int acDate, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + acDate);
		Optional<InnFundApl> innFundApl = null;
		if (dbName.equals(ContentName.onDay))
			innFundApl = innFundAplReposDay.findByAcDate(acDate);
		else if (dbName.equals(ContentName.onMon))
			innFundApl = innFundAplReposMon.findByAcDate(acDate);
		else if (dbName.equals(ContentName.onHist))
			innFundApl = innFundAplReposHist.findByAcDate(acDate);
		else
			innFundApl = innFundAplRepos.findByAcDate(acDate);
		return innFundApl.isPresent() ? innFundApl.get() : null;
	}

	@Override
	public InnFundApl holdById(InnFundApl innFundApl, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + innFundApl.getAcDate());
		Optional<InnFundApl> innFundAplT = null;
		if (dbName.equals(ContentName.onDay))
			innFundAplT = innFundAplReposDay.findByAcDate(innFundApl.getAcDate());
		else if (dbName.equals(ContentName.onMon))
			innFundAplT = innFundAplReposMon.findByAcDate(innFundApl.getAcDate());
		else if (dbName.equals(ContentName.onHist))
			innFundAplT = innFundAplReposHist.findByAcDate(innFundApl.getAcDate());
		else
			innFundAplT = innFundAplRepos.findByAcDate(innFundApl.getAcDate());
		return innFundAplT.isPresent() ? innFundAplT.get() : null;
	}

	@Override
	public InnFundApl insert(InnFundApl innFundApl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + innFundApl.getAcDate());
		if (this.findById(innFundApl.getAcDate()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			innFundApl.setCreateEmpNo(empNot);

		if (innFundApl.getLastUpdateEmpNo() == null || innFundApl.getLastUpdateEmpNo().isEmpty())
			innFundApl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return innFundAplReposDay.saveAndFlush(innFundApl);
		else if (dbName.equals(ContentName.onMon))
			return innFundAplReposMon.saveAndFlush(innFundApl);
		else if (dbName.equals(ContentName.onHist))
			return innFundAplReposHist.saveAndFlush(innFundApl);
		else
			return innFundAplRepos.saveAndFlush(innFundApl);
	}

	@Override
	public InnFundApl update(InnFundApl innFundApl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + innFundApl.getAcDate());
		if (!empNot.isEmpty())
			innFundApl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return innFundAplReposDay.saveAndFlush(innFundApl);
		else if (dbName.equals(ContentName.onMon))
			return innFundAplReposMon.saveAndFlush(innFundApl);
		else if (dbName.equals(ContentName.onHist))
			return innFundAplReposHist.saveAndFlush(innFundApl);
		else
			return innFundAplRepos.saveAndFlush(innFundApl);
	}

	@Override
	public InnFundApl update2(InnFundApl innFundApl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + innFundApl.getAcDate());
		if (!empNot.isEmpty())
			innFundApl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			innFundAplReposDay.saveAndFlush(innFundApl);
		else if (dbName.equals(ContentName.onMon))
			innFundAplReposMon.saveAndFlush(innFundApl);
		else if (dbName.equals(ContentName.onHist))
			innFundAplReposHist.saveAndFlush(innFundApl);
		else
			innFundAplRepos.saveAndFlush(innFundApl);
		return this.findById(innFundApl.getAcDate());
	}

	@Override
	public void delete(InnFundApl innFundApl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + innFundApl.getAcDate());
		if (dbName.equals(ContentName.onDay)) {
			innFundAplReposDay.delete(innFundApl);
			innFundAplReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innFundAplReposMon.delete(innFundApl);
			innFundAplReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innFundAplReposHist.delete(innFundApl);
			innFundAplReposHist.flush();
		} else {
			innFundAplRepos.delete(innFundApl);
			innFundAplRepos.flush();
		}
	}

	@Override
	public void insertAll(List<InnFundApl> innFundApl, TitaVo... titaVo) throws DBException {
		if (innFundApl == null || innFundApl.size() == 0)
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
		for (InnFundApl t : innFundApl) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			innFundApl = innFundAplReposDay.saveAll(innFundApl);
			innFundAplReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innFundApl = innFundAplReposMon.saveAll(innFundApl);
			innFundAplReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innFundApl = innFundAplReposHist.saveAll(innFundApl);
			innFundAplReposHist.flush();
		} else {
			innFundApl = innFundAplRepos.saveAll(innFundApl);
			innFundAplRepos.flush();
		}
	}

	@Override
	public void updateAll(List<InnFundApl> innFundApl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (innFundApl == null || innFundApl.size() == 0)
			throw new DBException(6);

		for (InnFundApl t : innFundApl)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			innFundApl = innFundAplReposDay.saveAll(innFundApl);
			innFundAplReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innFundApl = innFundAplReposMon.saveAll(innFundApl);
			innFundAplReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innFundApl = innFundAplReposHist.saveAll(innFundApl);
			innFundAplReposHist.flush();
		} else {
			innFundApl = innFundAplRepos.saveAll(innFundApl);
			innFundAplRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<InnFundApl> innFundApl, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (innFundApl == null || innFundApl.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			innFundAplReposDay.deleteAll(innFundApl);
			innFundAplReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			innFundAplReposMon.deleteAll(innFundApl);
			innFundAplReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			innFundAplReposHist.deleteAll(innFundApl);
			innFundAplReposHist.flush();
		} else {
			innFundAplRepos.deleteAll(innFundApl);
			innFundAplRepos.flush();
		}
	}

}
