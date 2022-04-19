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
import com.st1.itx.db.domain.MonthlyLoanBal;
import com.st1.itx.db.domain.MonthlyLoanBalId;
import com.st1.itx.db.repository.online.MonthlyLoanBalRepository;
import com.st1.itx.db.repository.day.MonthlyLoanBalRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLoanBalRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLoanBalRepositoryHist;
import com.st1.itx.db.service.MonthlyLoanBalService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLoanBalService")
@Repository
public class MonthlyLoanBalServiceImpl extends ASpringJpaParm implements MonthlyLoanBalService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MonthlyLoanBalRepository monthlyLoanBalRepos;

	@Autowired
	private MonthlyLoanBalRepositoryDay monthlyLoanBalReposDay;

	@Autowired
	private MonthlyLoanBalRepositoryMon monthlyLoanBalReposMon;

	@Autowired
	private MonthlyLoanBalRepositoryHist monthlyLoanBalReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(monthlyLoanBalRepos);
		org.junit.Assert.assertNotNull(monthlyLoanBalReposDay);
		org.junit.Assert.assertNotNull(monthlyLoanBalReposMon);
		org.junit.Assert.assertNotNull(monthlyLoanBalReposHist);
	}

	@Override
	public MonthlyLoanBal findById(MonthlyLoanBalId monthlyLoanBalId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + monthlyLoanBalId);
		Optional<MonthlyLoanBal> monthlyLoanBal = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLoanBal = monthlyLoanBalReposDay.findById(monthlyLoanBalId);
		else if (dbName.equals(ContentName.onMon))
			monthlyLoanBal = monthlyLoanBalReposMon.findById(monthlyLoanBalId);
		else if (dbName.equals(ContentName.onHist))
			monthlyLoanBal = monthlyLoanBalReposHist.findById(monthlyLoanBalId);
		else
			monthlyLoanBal = monthlyLoanBalRepos.findById(monthlyLoanBalId);
		MonthlyLoanBal obj = monthlyLoanBal.isPresent() ? monthlyLoanBal.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MonthlyLoanBal> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLoanBal> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "BormNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "BormNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLoanBalReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLoanBalReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLoanBalReposHist.findAll(pageable);
		else
			slice = monthlyLoanBalRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MonthlyLoanBal holdById(MonthlyLoanBalId monthlyLoanBalId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + monthlyLoanBalId);
		Optional<MonthlyLoanBal> monthlyLoanBal = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLoanBal = monthlyLoanBalReposDay.findByMonthlyLoanBalId(monthlyLoanBalId);
		else if (dbName.equals(ContentName.onMon))
			monthlyLoanBal = monthlyLoanBalReposMon.findByMonthlyLoanBalId(monthlyLoanBalId);
		else if (dbName.equals(ContentName.onHist))
			monthlyLoanBal = monthlyLoanBalReposHist.findByMonthlyLoanBalId(monthlyLoanBalId);
		else
			monthlyLoanBal = monthlyLoanBalRepos.findByMonthlyLoanBalId(monthlyLoanBalId);
		return monthlyLoanBal.isPresent() ? monthlyLoanBal.get() : null;
	}

	@Override
	public MonthlyLoanBal holdById(MonthlyLoanBal monthlyLoanBal, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + monthlyLoanBal.getMonthlyLoanBalId());
		Optional<MonthlyLoanBal> monthlyLoanBalT = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLoanBalT = monthlyLoanBalReposDay.findByMonthlyLoanBalId(monthlyLoanBal.getMonthlyLoanBalId());
		else if (dbName.equals(ContentName.onMon))
			monthlyLoanBalT = monthlyLoanBalReposMon.findByMonthlyLoanBalId(monthlyLoanBal.getMonthlyLoanBalId());
		else if (dbName.equals(ContentName.onHist))
			monthlyLoanBalT = monthlyLoanBalReposHist.findByMonthlyLoanBalId(monthlyLoanBal.getMonthlyLoanBalId());
		else
			monthlyLoanBalT = monthlyLoanBalRepos.findByMonthlyLoanBalId(monthlyLoanBal.getMonthlyLoanBalId());
		return monthlyLoanBalT.isPresent() ? monthlyLoanBalT.get() : null;
	}

	@Override
	public MonthlyLoanBal insert(MonthlyLoanBal monthlyLoanBal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + monthlyLoanBal.getMonthlyLoanBalId());
		if (this.findById(monthlyLoanBal.getMonthlyLoanBalId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			monthlyLoanBal.setCreateEmpNo(empNot);

		if (monthlyLoanBal.getLastUpdateEmpNo() == null || monthlyLoanBal.getLastUpdateEmpNo().isEmpty())
			monthlyLoanBal.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLoanBalReposDay.saveAndFlush(monthlyLoanBal);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLoanBalReposMon.saveAndFlush(monthlyLoanBal);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLoanBalReposHist.saveAndFlush(monthlyLoanBal);
		else
			return monthlyLoanBalRepos.saveAndFlush(monthlyLoanBal);
	}

	@Override
	public MonthlyLoanBal update(MonthlyLoanBal monthlyLoanBal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + monthlyLoanBal.getMonthlyLoanBalId());
		if (!empNot.isEmpty())
			monthlyLoanBal.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLoanBalReposDay.saveAndFlush(monthlyLoanBal);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLoanBalReposMon.saveAndFlush(monthlyLoanBal);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLoanBalReposHist.saveAndFlush(monthlyLoanBal);
		else
			return monthlyLoanBalRepos.saveAndFlush(monthlyLoanBal);
	}

	@Override
	public MonthlyLoanBal update2(MonthlyLoanBal monthlyLoanBal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + monthlyLoanBal.getMonthlyLoanBalId());
		if (!empNot.isEmpty())
			monthlyLoanBal.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			monthlyLoanBalReposDay.saveAndFlush(monthlyLoanBal);
		else if (dbName.equals(ContentName.onMon))
			monthlyLoanBalReposMon.saveAndFlush(monthlyLoanBal);
		else if (dbName.equals(ContentName.onHist))
			monthlyLoanBalReposHist.saveAndFlush(monthlyLoanBal);
		else
			monthlyLoanBalRepos.saveAndFlush(monthlyLoanBal);
		return this.findById(monthlyLoanBal.getMonthlyLoanBalId());
	}

	@Override
	public void delete(MonthlyLoanBal monthlyLoanBal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + monthlyLoanBal.getMonthlyLoanBalId());
		if (dbName.equals(ContentName.onDay)) {
			monthlyLoanBalReposDay.delete(monthlyLoanBal);
			monthlyLoanBalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLoanBalReposMon.delete(monthlyLoanBal);
			monthlyLoanBalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLoanBalReposHist.delete(monthlyLoanBal);
			monthlyLoanBalReposHist.flush();
		} else {
			monthlyLoanBalRepos.delete(monthlyLoanBal);
			monthlyLoanBalRepos.flush();
		}
	}

	@Override
	public void insertAll(List<MonthlyLoanBal> monthlyLoanBal, TitaVo... titaVo) throws DBException {
		if (monthlyLoanBal == null || monthlyLoanBal.size() == 0)
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
		for (MonthlyLoanBal t : monthlyLoanBal) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			monthlyLoanBal = monthlyLoanBalReposDay.saveAll(monthlyLoanBal);
			monthlyLoanBalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLoanBal = monthlyLoanBalReposMon.saveAll(monthlyLoanBal);
			monthlyLoanBalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLoanBal = monthlyLoanBalReposHist.saveAll(monthlyLoanBal);
			monthlyLoanBalReposHist.flush();
		} else {
			monthlyLoanBal = monthlyLoanBalRepos.saveAll(monthlyLoanBal);
			monthlyLoanBalRepos.flush();
		}
	}

	@Override
	public void updateAll(List<MonthlyLoanBal> monthlyLoanBal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (monthlyLoanBal == null || monthlyLoanBal.size() == 0)
			throw new DBException(6);

		for (MonthlyLoanBal t : monthlyLoanBal)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLoanBal = monthlyLoanBalReposDay.saveAll(monthlyLoanBal);
			monthlyLoanBalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLoanBal = monthlyLoanBalReposMon.saveAll(monthlyLoanBal);
			monthlyLoanBalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLoanBal = monthlyLoanBalReposHist.saveAll(monthlyLoanBal);
			monthlyLoanBalReposHist.flush();
		} else {
			monthlyLoanBal = monthlyLoanBalRepos.saveAll(monthlyLoanBal);
			monthlyLoanBalRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<MonthlyLoanBal> monthlyLoanBal, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (monthlyLoanBal == null || monthlyLoanBal.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			monthlyLoanBalReposDay.deleteAll(monthlyLoanBal);
			monthlyLoanBalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLoanBalReposMon.deleteAll(monthlyLoanBal);
			monthlyLoanBalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLoanBalReposHist.deleteAll(monthlyLoanBal);
			monthlyLoanBalReposHist.flush();
		} else {
			monthlyLoanBalRepos.deleteAll(monthlyLoanBal);
			monthlyLoanBalRepos.flush();
		}
	}

	@Override
	public void Usp_L9_MonthlyLoanBal_Upd(int TBSDYF, String empNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			monthlyLoanBalReposDay.uspL9MonthlyloanbalUpd(TBSDYF, empNo);
		else if (dbName.equals(ContentName.onMon))
			monthlyLoanBalReposMon.uspL9MonthlyloanbalUpd(TBSDYF, empNo);
		else if (dbName.equals(ContentName.onHist))
			monthlyLoanBalReposHist.uspL9MonthlyloanbalUpd(TBSDYF, empNo);
		else
			monthlyLoanBalRepos.uspL9MonthlyloanbalUpd(TBSDYF, empNo);
	}

}
