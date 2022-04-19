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
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;
import com.st1.itx.db.repository.online.CdWorkMonthRepository;
import com.st1.itx.db.repository.day.CdWorkMonthRepositoryDay;
import com.st1.itx.db.repository.mon.CdWorkMonthRepositoryMon;
import com.st1.itx.db.repository.hist.CdWorkMonthRepositoryHist;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdWorkMonthService")
@Repository
public class CdWorkMonthServiceImpl extends ASpringJpaParm implements CdWorkMonthService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdWorkMonthRepository cdWorkMonthRepos;

	@Autowired
	private CdWorkMonthRepositoryDay cdWorkMonthReposDay;

	@Autowired
	private CdWorkMonthRepositoryMon cdWorkMonthReposMon;

	@Autowired
	private CdWorkMonthRepositoryHist cdWorkMonthReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdWorkMonthRepos);
		org.junit.Assert.assertNotNull(cdWorkMonthReposDay);
		org.junit.Assert.assertNotNull(cdWorkMonthReposMon);
		org.junit.Assert.assertNotNull(cdWorkMonthReposHist);
	}

	@Override
	public CdWorkMonth findById(CdWorkMonthId cdWorkMonthId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + cdWorkMonthId);
		Optional<CdWorkMonth> cdWorkMonth = null;
		if (dbName.equals(ContentName.onDay))
			cdWorkMonth = cdWorkMonthReposDay.findById(cdWorkMonthId);
		else if (dbName.equals(ContentName.onMon))
			cdWorkMonth = cdWorkMonthReposMon.findById(cdWorkMonthId);
		else if (dbName.equals(ContentName.onHist))
			cdWorkMonth = cdWorkMonthReposHist.findById(cdWorkMonthId);
		else
			cdWorkMonth = cdWorkMonthRepos.findById(cdWorkMonthId);
		CdWorkMonth obj = cdWorkMonth.isPresent() ? cdWorkMonth.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdWorkMonth> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdWorkMonth> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Year", "Month"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Year", "Month"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdWorkMonthReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdWorkMonthReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdWorkMonthReposHist.findAll(pageable);
		else
			slice = cdWorkMonthRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdWorkMonth> findYearMonth(int year_0, int year_1, int month_2, int month_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdWorkMonth> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findYearMonth " + dbName + " : " + "year_0 : " + year_0 + " year_1 : " + year_1 + " month_2 : " + month_2 + " month_3 : " + month_3);
		if (dbName.equals(ContentName.onDay))
			slice = cdWorkMonthReposDay.findAllByYearGreaterThanEqualAndYearLessThanEqualAndMonthGreaterThanEqualAndMonthLessThanEqualOrderByYearAscMonthAsc(year_0, year_1, month_2, month_3,
					pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdWorkMonthReposMon.findAllByYearGreaterThanEqualAndYearLessThanEqualAndMonthGreaterThanEqualAndMonthLessThanEqualOrderByYearAscMonthAsc(year_0, year_1, month_2, month_3,
					pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdWorkMonthReposHist.findAllByYearGreaterThanEqualAndYearLessThanEqualAndMonthGreaterThanEqualAndMonthLessThanEqualOrderByYearAscMonthAsc(year_0, year_1, month_2, month_3,
					pageable);
		else
			slice = cdWorkMonthRepos.findAllByYearGreaterThanEqualAndYearLessThanEqualAndMonthGreaterThanEqualAndMonthLessThanEqualOrderByYearAscMonthAsc(year_0, year_1, month_2, month_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdWorkMonth findDateFirst(int startDate_0, int endDate_1, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findDateFirst " + dbName + " : " + "startDate_0 : " + startDate_0 + " endDate_1 : " + endDate_1);
		Optional<CdWorkMonth> cdWorkMonthT = null;
		if (dbName.equals(ContentName.onDay))
			cdWorkMonthT = cdWorkMonthReposDay.findTopByStartDateLessThanEqualAndEndDateGreaterThanEqual(startDate_0, endDate_1);
		else if (dbName.equals(ContentName.onMon))
			cdWorkMonthT = cdWorkMonthReposMon.findTopByStartDateLessThanEqualAndEndDateGreaterThanEqual(startDate_0, endDate_1);
		else if (dbName.equals(ContentName.onHist))
			cdWorkMonthT = cdWorkMonthReposHist.findTopByStartDateLessThanEqualAndEndDateGreaterThanEqual(startDate_0, endDate_1);
		else
			cdWorkMonthT = cdWorkMonthRepos.findTopByStartDateLessThanEqualAndEndDateGreaterThanEqual(startDate_0, endDate_1);

		return cdWorkMonthT.isPresent() ? cdWorkMonthT.get() : null;
	}

	@Override
	public CdWorkMonth holdById(CdWorkMonthId cdWorkMonthId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdWorkMonthId);
		Optional<CdWorkMonth> cdWorkMonth = null;
		if (dbName.equals(ContentName.onDay))
			cdWorkMonth = cdWorkMonthReposDay.findByCdWorkMonthId(cdWorkMonthId);
		else if (dbName.equals(ContentName.onMon))
			cdWorkMonth = cdWorkMonthReposMon.findByCdWorkMonthId(cdWorkMonthId);
		else if (dbName.equals(ContentName.onHist))
			cdWorkMonth = cdWorkMonthReposHist.findByCdWorkMonthId(cdWorkMonthId);
		else
			cdWorkMonth = cdWorkMonthRepos.findByCdWorkMonthId(cdWorkMonthId);
		return cdWorkMonth.isPresent() ? cdWorkMonth.get() : null;
	}

	@Override
	public CdWorkMonth holdById(CdWorkMonth cdWorkMonth, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdWorkMonth.getCdWorkMonthId());
		Optional<CdWorkMonth> cdWorkMonthT = null;
		if (dbName.equals(ContentName.onDay))
			cdWorkMonthT = cdWorkMonthReposDay.findByCdWorkMonthId(cdWorkMonth.getCdWorkMonthId());
		else if (dbName.equals(ContentName.onMon))
			cdWorkMonthT = cdWorkMonthReposMon.findByCdWorkMonthId(cdWorkMonth.getCdWorkMonthId());
		else if (dbName.equals(ContentName.onHist))
			cdWorkMonthT = cdWorkMonthReposHist.findByCdWorkMonthId(cdWorkMonth.getCdWorkMonthId());
		else
			cdWorkMonthT = cdWorkMonthRepos.findByCdWorkMonthId(cdWorkMonth.getCdWorkMonthId());
		return cdWorkMonthT.isPresent() ? cdWorkMonthT.get() : null;
	}

	@Override
	public CdWorkMonth insert(CdWorkMonth cdWorkMonth, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdWorkMonth.getCdWorkMonthId());
		if (this.findById(cdWorkMonth.getCdWorkMonthId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdWorkMonth.setCreateEmpNo(empNot);

		if (cdWorkMonth.getLastUpdateEmpNo() == null || cdWorkMonth.getLastUpdateEmpNo().isEmpty())
			cdWorkMonth.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdWorkMonthReposDay.saveAndFlush(cdWorkMonth);
		else if (dbName.equals(ContentName.onMon))
			return cdWorkMonthReposMon.saveAndFlush(cdWorkMonth);
		else if (dbName.equals(ContentName.onHist))
			return cdWorkMonthReposHist.saveAndFlush(cdWorkMonth);
		else
			return cdWorkMonthRepos.saveAndFlush(cdWorkMonth);
	}

	@Override
	public CdWorkMonth update(CdWorkMonth cdWorkMonth, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdWorkMonth.getCdWorkMonthId());
		if (!empNot.isEmpty())
			cdWorkMonth.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdWorkMonthReposDay.saveAndFlush(cdWorkMonth);
		else if (dbName.equals(ContentName.onMon))
			return cdWorkMonthReposMon.saveAndFlush(cdWorkMonth);
		else if (dbName.equals(ContentName.onHist))
			return cdWorkMonthReposHist.saveAndFlush(cdWorkMonth);
		else
			return cdWorkMonthRepos.saveAndFlush(cdWorkMonth);
	}

	@Override
	public CdWorkMonth update2(CdWorkMonth cdWorkMonth, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdWorkMonth.getCdWorkMonthId());
		if (!empNot.isEmpty())
			cdWorkMonth.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdWorkMonthReposDay.saveAndFlush(cdWorkMonth);
		else if (dbName.equals(ContentName.onMon))
			cdWorkMonthReposMon.saveAndFlush(cdWorkMonth);
		else if (dbName.equals(ContentName.onHist))
			cdWorkMonthReposHist.saveAndFlush(cdWorkMonth);
		else
			cdWorkMonthRepos.saveAndFlush(cdWorkMonth);
		return this.findById(cdWorkMonth.getCdWorkMonthId());
	}

	@Override
	public void delete(CdWorkMonth cdWorkMonth, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdWorkMonth.getCdWorkMonthId());
		if (dbName.equals(ContentName.onDay)) {
			cdWorkMonthReposDay.delete(cdWorkMonth);
			cdWorkMonthReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdWorkMonthReposMon.delete(cdWorkMonth);
			cdWorkMonthReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdWorkMonthReposHist.delete(cdWorkMonth);
			cdWorkMonthReposHist.flush();
		} else {
			cdWorkMonthRepos.delete(cdWorkMonth);
			cdWorkMonthRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdWorkMonth> cdWorkMonth, TitaVo... titaVo) throws DBException {
		if (cdWorkMonth == null || cdWorkMonth.size() == 0)
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
		for (CdWorkMonth t : cdWorkMonth) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdWorkMonth = cdWorkMonthReposDay.saveAll(cdWorkMonth);
			cdWorkMonthReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdWorkMonth = cdWorkMonthReposMon.saveAll(cdWorkMonth);
			cdWorkMonthReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdWorkMonth = cdWorkMonthReposHist.saveAll(cdWorkMonth);
			cdWorkMonthReposHist.flush();
		} else {
			cdWorkMonth = cdWorkMonthRepos.saveAll(cdWorkMonth);
			cdWorkMonthRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdWorkMonth> cdWorkMonth, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdWorkMonth == null || cdWorkMonth.size() == 0)
			throw new DBException(6);

		for (CdWorkMonth t : cdWorkMonth)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdWorkMonth = cdWorkMonthReposDay.saveAll(cdWorkMonth);
			cdWorkMonthReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdWorkMonth = cdWorkMonthReposMon.saveAll(cdWorkMonth);
			cdWorkMonthReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdWorkMonth = cdWorkMonthReposHist.saveAll(cdWorkMonth);
			cdWorkMonthReposHist.flush();
		} else {
			cdWorkMonth = cdWorkMonthRepos.saveAll(cdWorkMonth);
			cdWorkMonthRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdWorkMonth> cdWorkMonth, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdWorkMonth == null || cdWorkMonth.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdWorkMonthReposDay.deleteAll(cdWorkMonth);
			cdWorkMonthReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdWorkMonthReposMon.deleteAll(cdWorkMonth);
			cdWorkMonthReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdWorkMonthReposHist.deleteAll(cdWorkMonth);
			cdWorkMonthReposHist.flush();
		} else {
			cdWorkMonthRepos.deleteAll(cdWorkMonth);
			cdWorkMonthRepos.flush();
		}
	}

}
