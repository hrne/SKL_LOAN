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
import com.st1.itx.db.domain.CdBudget;
import com.st1.itx.db.domain.CdBudgetId;
import com.st1.itx.db.repository.online.CdBudgetRepository;
import com.st1.itx.db.repository.day.CdBudgetRepositoryDay;
import com.st1.itx.db.repository.mon.CdBudgetRepositoryMon;
import com.st1.itx.db.repository.hist.CdBudgetRepositoryHist;
import com.st1.itx.db.service.CdBudgetService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdBudgetService")
@Repository
public class CdBudgetServiceImpl extends ASpringJpaParm implements CdBudgetService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdBudgetRepository cdBudgetRepos;

	@Autowired
	private CdBudgetRepositoryDay cdBudgetReposDay;

	@Autowired
	private CdBudgetRepositoryMon cdBudgetReposMon;

	@Autowired
	private CdBudgetRepositoryHist cdBudgetReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdBudgetRepos);
		org.junit.Assert.assertNotNull(cdBudgetReposDay);
		org.junit.Assert.assertNotNull(cdBudgetReposMon);
		org.junit.Assert.assertNotNull(cdBudgetReposHist);
	}

	@Override
	public CdBudget findById(CdBudgetId cdBudgetId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + cdBudgetId);
		Optional<CdBudget> cdBudget = null;
		if (dbName.equals(ContentName.onDay))
			cdBudget = cdBudgetReposDay.findById(cdBudgetId);
		else if (dbName.equals(ContentName.onMon))
			cdBudget = cdBudgetReposMon.findById(cdBudgetId);
		else if (dbName.equals(ContentName.onHist))
			cdBudget = cdBudgetReposHist.findById(cdBudgetId);
		else
			cdBudget = cdBudgetRepos.findById(cdBudgetId);
		CdBudget obj = cdBudget.isPresent() ? cdBudget.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdBudget> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBudget> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Year", "Month"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Year", "Month"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdBudgetReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBudgetReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBudgetReposHist.findAll(pageable);
		else
			slice = cdBudgetRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdBudget> findYearMonth(int year_0, int year_1, int month_2, int month_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBudget> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findYearMonth " + dbName + " : " + "year_0 : " + year_0 + " year_1 : " + year_1 + " month_2 : " + month_2 + " month_3 : " + month_3);
		if (dbName.equals(ContentName.onDay))
			slice = cdBudgetReposDay.findAllByYearGreaterThanEqualAndYearLessThanEqualAndMonthGreaterThanEqualAndMonthLessThanEqualOrderByYearAscMonthAsc(year_0, year_1, month_2, month_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBudgetReposMon.findAllByYearGreaterThanEqualAndYearLessThanEqualAndMonthGreaterThanEqualAndMonthLessThanEqualOrderByYearAscMonthAsc(year_0, year_1, month_2, month_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBudgetReposHist.findAllByYearGreaterThanEqualAndYearLessThanEqualAndMonthGreaterThanEqualAndMonthLessThanEqualOrderByYearAscMonthAsc(year_0, year_1, month_2, month_3, pageable);
		else
			slice = cdBudgetRepos.findAllByYearGreaterThanEqualAndYearLessThanEqualAndMonthGreaterThanEqualAndMonthLessThanEqualOrderByYearAscMonthAsc(year_0, year_1, month_2, month_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdBudget holdById(CdBudgetId cdBudgetId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdBudgetId);
		Optional<CdBudget> cdBudget = null;
		if (dbName.equals(ContentName.onDay))
			cdBudget = cdBudgetReposDay.findByCdBudgetId(cdBudgetId);
		else if (dbName.equals(ContentName.onMon))
			cdBudget = cdBudgetReposMon.findByCdBudgetId(cdBudgetId);
		else if (dbName.equals(ContentName.onHist))
			cdBudget = cdBudgetReposHist.findByCdBudgetId(cdBudgetId);
		else
			cdBudget = cdBudgetRepos.findByCdBudgetId(cdBudgetId);
		return cdBudget.isPresent() ? cdBudget.get() : null;
	}

	@Override
	public CdBudget holdById(CdBudget cdBudget, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdBudget.getCdBudgetId());
		Optional<CdBudget> cdBudgetT = null;
		if (dbName.equals(ContentName.onDay))
			cdBudgetT = cdBudgetReposDay.findByCdBudgetId(cdBudget.getCdBudgetId());
		else if (dbName.equals(ContentName.onMon))
			cdBudgetT = cdBudgetReposMon.findByCdBudgetId(cdBudget.getCdBudgetId());
		else if (dbName.equals(ContentName.onHist))
			cdBudgetT = cdBudgetReposHist.findByCdBudgetId(cdBudget.getCdBudgetId());
		else
			cdBudgetT = cdBudgetRepos.findByCdBudgetId(cdBudget.getCdBudgetId());
		return cdBudgetT.isPresent() ? cdBudgetT.get() : null;
	}

	@Override
	public CdBudget insert(CdBudget cdBudget, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdBudget.getCdBudgetId());
		if (this.findById(cdBudget.getCdBudgetId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdBudget.setCreateEmpNo(empNot);

		if (cdBudget.getLastUpdateEmpNo() == null || cdBudget.getLastUpdateEmpNo().isEmpty())
			cdBudget.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdBudgetReposDay.saveAndFlush(cdBudget);
		else if (dbName.equals(ContentName.onMon))
			return cdBudgetReposMon.saveAndFlush(cdBudget);
		else if (dbName.equals(ContentName.onHist))
			return cdBudgetReposHist.saveAndFlush(cdBudget);
		else
			return cdBudgetRepos.saveAndFlush(cdBudget);
	}

	@Override
	public CdBudget update(CdBudget cdBudget, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdBudget.getCdBudgetId());
		if (!empNot.isEmpty())
			cdBudget.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdBudgetReposDay.saveAndFlush(cdBudget);
		else if (dbName.equals(ContentName.onMon))
			return cdBudgetReposMon.saveAndFlush(cdBudget);
		else if (dbName.equals(ContentName.onHist))
			return cdBudgetReposHist.saveAndFlush(cdBudget);
		else
			return cdBudgetRepos.saveAndFlush(cdBudget);
	}

	@Override
	public CdBudget update2(CdBudget cdBudget, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdBudget.getCdBudgetId());
		if (!empNot.isEmpty())
			cdBudget.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdBudgetReposDay.saveAndFlush(cdBudget);
		else if (dbName.equals(ContentName.onMon))
			cdBudgetReposMon.saveAndFlush(cdBudget);
		else if (dbName.equals(ContentName.onHist))
			cdBudgetReposHist.saveAndFlush(cdBudget);
		else
			cdBudgetRepos.saveAndFlush(cdBudget);
		return this.findById(cdBudget.getCdBudgetId());
	}

	@Override
	public void delete(CdBudget cdBudget, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdBudget.getCdBudgetId());
		if (dbName.equals(ContentName.onDay)) {
			cdBudgetReposDay.delete(cdBudget);
			cdBudgetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBudgetReposMon.delete(cdBudget);
			cdBudgetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBudgetReposHist.delete(cdBudget);
			cdBudgetReposHist.flush();
		} else {
			cdBudgetRepos.delete(cdBudget);
			cdBudgetRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdBudget> cdBudget, TitaVo... titaVo) throws DBException {
		if (cdBudget == null || cdBudget.size() == 0)
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
		for (CdBudget t : cdBudget) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdBudget = cdBudgetReposDay.saveAll(cdBudget);
			cdBudgetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBudget = cdBudgetReposMon.saveAll(cdBudget);
			cdBudgetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBudget = cdBudgetReposHist.saveAll(cdBudget);
			cdBudgetReposHist.flush();
		} else {
			cdBudget = cdBudgetRepos.saveAll(cdBudget);
			cdBudgetRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdBudget> cdBudget, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdBudget == null || cdBudget.size() == 0)
			throw new DBException(6);

		for (CdBudget t : cdBudget)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdBudget = cdBudgetReposDay.saveAll(cdBudget);
			cdBudgetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBudget = cdBudgetReposMon.saveAll(cdBudget);
			cdBudgetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBudget = cdBudgetReposHist.saveAll(cdBudget);
			cdBudgetReposHist.flush();
		} else {
			cdBudget = cdBudgetRepos.saveAll(cdBudget);
			cdBudgetRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdBudget> cdBudget, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdBudget == null || cdBudget.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdBudgetReposDay.deleteAll(cdBudget);
			cdBudgetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBudgetReposMon.deleteAll(cdBudget);
			cdBudgetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBudgetReposHist.deleteAll(cdBudget);
			cdBudgetReposHist.flush();
		} else {
			cdBudgetRepos.deleteAll(cdBudget);
			cdBudgetRepos.flush();
		}
	}

}
