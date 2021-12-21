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
import com.st1.itx.db.domain.CdVarValue;
import com.st1.itx.db.repository.online.CdVarValueRepository;
import com.st1.itx.db.repository.day.CdVarValueRepositoryDay;
import com.st1.itx.db.repository.mon.CdVarValueRepositoryMon;
import com.st1.itx.db.repository.hist.CdVarValueRepositoryHist;
import com.st1.itx.db.service.CdVarValueService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdVarValueService")
@Repository
public class CdVarValueServiceImpl extends ASpringJpaParm implements CdVarValueService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdVarValueRepository cdVarValueRepos;

	@Autowired
	private CdVarValueRepositoryDay cdVarValueReposDay;

	@Autowired
	private CdVarValueRepositoryMon cdVarValueReposMon;

	@Autowired
	private CdVarValueRepositoryHist cdVarValueReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdVarValueRepos);
		org.junit.Assert.assertNotNull(cdVarValueReposDay);
		org.junit.Assert.assertNotNull(cdVarValueReposMon);
		org.junit.Assert.assertNotNull(cdVarValueReposHist);
	}

	@Override
	public CdVarValue findById(int yearMonth, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + yearMonth);
		Optional<CdVarValue> cdVarValue = null;
		if (dbName.equals(ContentName.onDay))
			cdVarValue = cdVarValueReposDay.findById(yearMonth);
		else if (dbName.equals(ContentName.onMon))
			cdVarValue = cdVarValueReposMon.findById(yearMonth);
		else if (dbName.equals(ContentName.onHist))
			cdVarValue = cdVarValueReposHist.findById(yearMonth);
		else
			cdVarValue = cdVarValueRepos.findById(yearMonth);
		CdVarValue obj = cdVarValue.isPresent() ? cdVarValue.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdVarValue> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdVarValue> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdVarValueReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdVarValueReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdVarValueReposHist.findAll(pageable);
		else
			slice = cdVarValueRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdVarValue> findYearMonth(int yearMonth_0, int yearMonth_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdVarValue> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findYearMonth " + dbName + " : " + "yearMonth_0 : " + yearMonth_0 + " yearMonth_1 : " + yearMonth_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdVarValueReposDay.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdVarValueReposMon.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdVarValueReposHist.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);
		else
			slice = cdVarValueRepos.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdVarValue findYearMonthFirst(int yearMonth_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findYearMonthFirst " + dbName + " : " + "yearMonth_0 : " + yearMonth_0);
		Optional<CdVarValue> cdVarValueT = null;
		if (dbName.equals(ContentName.onDay))
			cdVarValueT = cdVarValueReposDay.findTopByYearMonthLessThanEqualOrderByYearMonthDesc(yearMonth_0);
		else if (dbName.equals(ContentName.onMon))
			cdVarValueT = cdVarValueReposMon.findTopByYearMonthLessThanEqualOrderByYearMonthDesc(yearMonth_0);
		else if (dbName.equals(ContentName.onHist))
			cdVarValueT = cdVarValueReposHist.findTopByYearMonthLessThanEqualOrderByYearMonthDesc(yearMonth_0);
		else
			cdVarValueT = cdVarValueRepos.findTopByYearMonthLessThanEqualOrderByYearMonthDesc(yearMonth_0);

		return cdVarValueT.isPresent() ? cdVarValueT.get() : null;
	}

	@Override
	public CdVarValue holdById(int yearMonth, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + yearMonth);
		Optional<CdVarValue> cdVarValue = null;
		if (dbName.equals(ContentName.onDay))
			cdVarValue = cdVarValueReposDay.findByYearMonth(yearMonth);
		else if (dbName.equals(ContentName.onMon))
			cdVarValue = cdVarValueReposMon.findByYearMonth(yearMonth);
		else if (dbName.equals(ContentName.onHist))
			cdVarValue = cdVarValueReposHist.findByYearMonth(yearMonth);
		else
			cdVarValue = cdVarValueRepos.findByYearMonth(yearMonth);
		return cdVarValue.isPresent() ? cdVarValue.get() : null;
	}

	@Override
	public CdVarValue holdById(CdVarValue cdVarValue, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdVarValue.getYearMonth());
		Optional<CdVarValue> cdVarValueT = null;
		if (dbName.equals(ContentName.onDay))
			cdVarValueT = cdVarValueReposDay.findByYearMonth(cdVarValue.getYearMonth());
		else if (dbName.equals(ContentName.onMon))
			cdVarValueT = cdVarValueReposMon.findByYearMonth(cdVarValue.getYearMonth());
		else if (dbName.equals(ContentName.onHist))
			cdVarValueT = cdVarValueReposHist.findByYearMonth(cdVarValue.getYearMonth());
		else
			cdVarValueT = cdVarValueRepos.findByYearMonth(cdVarValue.getYearMonth());
		return cdVarValueT.isPresent() ? cdVarValueT.get() : null;
	}

	@Override
	public CdVarValue insert(CdVarValue cdVarValue, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdVarValue.getYearMonth());
		if (this.findById(cdVarValue.getYearMonth()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdVarValue.setCreateEmpNo(empNot);

		if (cdVarValue.getLastUpdateEmpNo() == null || cdVarValue.getLastUpdateEmpNo().isEmpty())
			cdVarValue.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdVarValueReposDay.saveAndFlush(cdVarValue);
		else if (dbName.equals(ContentName.onMon))
			return cdVarValueReposMon.saveAndFlush(cdVarValue);
		else if (dbName.equals(ContentName.onHist))
			return cdVarValueReposHist.saveAndFlush(cdVarValue);
		else
			return cdVarValueRepos.saveAndFlush(cdVarValue);
	}

	@Override
	public CdVarValue update(CdVarValue cdVarValue, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdVarValue.getYearMonth());
		if (!empNot.isEmpty())
			cdVarValue.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdVarValueReposDay.saveAndFlush(cdVarValue);
		else if (dbName.equals(ContentName.onMon))
			return cdVarValueReposMon.saveAndFlush(cdVarValue);
		else if (dbName.equals(ContentName.onHist))
			return cdVarValueReposHist.saveAndFlush(cdVarValue);
		else
			return cdVarValueRepos.saveAndFlush(cdVarValue);
	}

	@Override
	public CdVarValue update2(CdVarValue cdVarValue, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdVarValue.getYearMonth());
		if (!empNot.isEmpty())
			cdVarValue.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdVarValueReposDay.saveAndFlush(cdVarValue);
		else if (dbName.equals(ContentName.onMon))
			cdVarValueReposMon.saveAndFlush(cdVarValue);
		else if (dbName.equals(ContentName.onHist))
			cdVarValueReposHist.saveAndFlush(cdVarValue);
		else
			cdVarValueRepos.saveAndFlush(cdVarValue);
		return this.findById(cdVarValue.getYearMonth());
	}

	@Override
	public void delete(CdVarValue cdVarValue, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdVarValue.getYearMonth());
		if (dbName.equals(ContentName.onDay)) {
			cdVarValueReposDay.delete(cdVarValue);
			cdVarValueReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdVarValueReposMon.delete(cdVarValue);
			cdVarValueReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdVarValueReposHist.delete(cdVarValue);
			cdVarValueReposHist.flush();
		} else {
			cdVarValueRepos.delete(cdVarValue);
			cdVarValueRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdVarValue> cdVarValue, TitaVo... titaVo) throws DBException {
		if (cdVarValue == null || cdVarValue.size() == 0)
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
		for (CdVarValue t : cdVarValue) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdVarValue = cdVarValueReposDay.saveAll(cdVarValue);
			cdVarValueReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdVarValue = cdVarValueReposMon.saveAll(cdVarValue);
			cdVarValueReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdVarValue = cdVarValueReposHist.saveAll(cdVarValue);
			cdVarValueReposHist.flush();
		} else {
			cdVarValue = cdVarValueRepos.saveAll(cdVarValue);
			cdVarValueRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdVarValue> cdVarValue, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdVarValue == null || cdVarValue.size() == 0)
			throw new DBException(6);

		for (CdVarValue t : cdVarValue)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdVarValue = cdVarValueReposDay.saveAll(cdVarValue);
			cdVarValueReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdVarValue = cdVarValueReposMon.saveAll(cdVarValue);
			cdVarValueReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdVarValue = cdVarValueReposHist.saveAll(cdVarValue);
			cdVarValueReposHist.flush();
		} else {
			cdVarValue = cdVarValueRepos.saveAll(cdVarValue);
			cdVarValueRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdVarValue> cdVarValue, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdVarValue == null || cdVarValue.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdVarValueReposDay.deleteAll(cdVarValue);
			cdVarValueReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdVarValueReposMon.deleteAll(cdVarValue);
			cdVarValueReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdVarValueReposHist.deleteAll(cdVarValue);
			cdVarValueReposHist.flush();
		} else {
			cdVarValueRepos.deleteAll(cdVarValue);
			cdVarValueRepos.flush();
		}
	}

}
