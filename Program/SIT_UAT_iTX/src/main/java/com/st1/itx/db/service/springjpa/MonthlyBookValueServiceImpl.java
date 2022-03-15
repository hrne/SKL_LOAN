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
import com.st1.itx.db.domain.MonthlyBookValue;
import com.st1.itx.db.domain.MonthlyBookValueId;
import com.st1.itx.db.repository.online.MonthlyBookValueRepository;
import com.st1.itx.db.repository.day.MonthlyBookValueRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyBookValueRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyBookValueRepositoryHist;
import com.st1.itx.db.service.MonthlyBookValueService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyBookValueService")
@Repository
public class MonthlyBookValueServiceImpl extends ASpringJpaParm implements MonthlyBookValueService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MonthlyBookValueRepository monthlyBookValueRepos;

	@Autowired
	private MonthlyBookValueRepositoryDay monthlyBookValueReposDay;

	@Autowired
	private MonthlyBookValueRepositoryMon monthlyBookValueReposMon;

	@Autowired
	private MonthlyBookValueRepositoryHist monthlyBookValueReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(monthlyBookValueRepos);
		org.junit.Assert.assertNotNull(monthlyBookValueReposDay);
		org.junit.Assert.assertNotNull(monthlyBookValueReposMon);
		org.junit.Assert.assertNotNull(monthlyBookValueReposHist);
	}

	@Override
	public MonthlyBookValue findById(MonthlyBookValueId monthlyBookValueId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + monthlyBookValueId);
		Optional<MonthlyBookValue> monthlyBookValue = null;
		if (dbName.equals(ContentName.onDay))
			monthlyBookValue = monthlyBookValueReposDay.findById(monthlyBookValueId);
		else if (dbName.equals(ContentName.onMon))
			monthlyBookValue = monthlyBookValueReposMon.findById(monthlyBookValueId);
		else if (dbName.equals(ContentName.onHist))
			monthlyBookValue = monthlyBookValueReposHist.findById(monthlyBookValueId);
		else
			monthlyBookValue = monthlyBookValueRepos.findById(monthlyBookValueId);
		MonthlyBookValue obj = monthlyBookValue.isPresent() ? monthlyBookValue.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MonthlyBookValue> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyBookValue> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "BormNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyBookValueReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyBookValueReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyBookValueReposHist.findAll(pageable);
		else
			slice = monthlyBookValueRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MonthlyBookValue holdById(MonthlyBookValueId monthlyBookValueId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + monthlyBookValueId);
		Optional<MonthlyBookValue> monthlyBookValue = null;
		if (dbName.equals(ContentName.onDay))
			monthlyBookValue = monthlyBookValueReposDay.findByMonthlyBookValueId(monthlyBookValueId);
		else if (dbName.equals(ContentName.onMon))
			monthlyBookValue = monthlyBookValueReposMon.findByMonthlyBookValueId(monthlyBookValueId);
		else if (dbName.equals(ContentName.onHist))
			monthlyBookValue = monthlyBookValueReposHist.findByMonthlyBookValueId(monthlyBookValueId);
		else
			monthlyBookValue = monthlyBookValueRepos.findByMonthlyBookValueId(monthlyBookValueId);
		return monthlyBookValue.isPresent() ? monthlyBookValue.get() : null;
	}

	@Override
	public MonthlyBookValue holdById(MonthlyBookValue monthlyBookValue, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + monthlyBookValue.getMonthlyBookValueId());
		Optional<MonthlyBookValue> monthlyBookValueT = null;
		if (dbName.equals(ContentName.onDay))
			monthlyBookValueT = monthlyBookValueReposDay.findByMonthlyBookValueId(monthlyBookValue.getMonthlyBookValueId());
		else if (dbName.equals(ContentName.onMon))
			monthlyBookValueT = monthlyBookValueReposMon.findByMonthlyBookValueId(monthlyBookValue.getMonthlyBookValueId());
		else if (dbName.equals(ContentName.onHist))
			monthlyBookValueT = monthlyBookValueReposHist.findByMonthlyBookValueId(monthlyBookValue.getMonthlyBookValueId());
		else
			monthlyBookValueT = monthlyBookValueRepos.findByMonthlyBookValueId(monthlyBookValue.getMonthlyBookValueId());
		return monthlyBookValueT.isPresent() ? monthlyBookValueT.get() : null;
	}

	@Override
	public MonthlyBookValue insert(MonthlyBookValue monthlyBookValue, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + monthlyBookValue.getMonthlyBookValueId());
		if (this.findById(monthlyBookValue.getMonthlyBookValueId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			monthlyBookValue.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyBookValueReposDay.saveAndFlush(monthlyBookValue);
		else if (dbName.equals(ContentName.onMon))
			return monthlyBookValueReposMon.saveAndFlush(monthlyBookValue);
		else if (dbName.equals(ContentName.onHist))
			return monthlyBookValueReposHist.saveAndFlush(monthlyBookValue);
		else
			return monthlyBookValueRepos.saveAndFlush(monthlyBookValue);
	}

	@Override
	public MonthlyBookValue update(MonthlyBookValue monthlyBookValue, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + monthlyBookValue.getMonthlyBookValueId());
		if (!empNot.isEmpty())
			monthlyBookValue.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyBookValueReposDay.saveAndFlush(monthlyBookValue);
		else if (dbName.equals(ContentName.onMon))
			return monthlyBookValueReposMon.saveAndFlush(monthlyBookValue);
		else if (dbName.equals(ContentName.onHist))
			return monthlyBookValueReposHist.saveAndFlush(monthlyBookValue);
		else
			return monthlyBookValueRepos.saveAndFlush(monthlyBookValue);
	}

	@Override
	public MonthlyBookValue update2(MonthlyBookValue monthlyBookValue, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + monthlyBookValue.getMonthlyBookValueId());
		if (!empNot.isEmpty())
			monthlyBookValue.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			monthlyBookValueReposDay.saveAndFlush(monthlyBookValue);
		else if (dbName.equals(ContentName.onMon))
			monthlyBookValueReposMon.saveAndFlush(monthlyBookValue);
		else if (dbName.equals(ContentName.onHist))
			monthlyBookValueReposHist.saveAndFlush(monthlyBookValue);
		else
			monthlyBookValueRepos.saveAndFlush(monthlyBookValue);
		return this.findById(monthlyBookValue.getMonthlyBookValueId());
	}

	@Override
	public void delete(MonthlyBookValue monthlyBookValue, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + monthlyBookValue.getMonthlyBookValueId());
		if (dbName.equals(ContentName.onDay)) {
			monthlyBookValueReposDay.delete(monthlyBookValue);
			monthlyBookValueReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyBookValueReposMon.delete(monthlyBookValue);
			monthlyBookValueReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyBookValueReposHist.delete(monthlyBookValue);
			monthlyBookValueReposHist.flush();
		} else {
			monthlyBookValueRepos.delete(monthlyBookValue);
			monthlyBookValueRepos.flush();
		}
	}

	@Override
	public void insertAll(List<MonthlyBookValue> monthlyBookValue, TitaVo... titaVo) throws DBException {
		if (monthlyBookValue == null || monthlyBookValue.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (MonthlyBookValue t : monthlyBookValue)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyBookValue = monthlyBookValueReposDay.saveAll(monthlyBookValue);
			monthlyBookValueReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyBookValue = monthlyBookValueReposMon.saveAll(monthlyBookValue);
			monthlyBookValueReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyBookValue = monthlyBookValueReposHist.saveAll(monthlyBookValue);
			monthlyBookValueReposHist.flush();
		} else {
			monthlyBookValue = monthlyBookValueRepos.saveAll(monthlyBookValue);
			monthlyBookValueRepos.flush();
		}
	}

	@Override
	public void updateAll(List<MonthlyBookValue> monthlyBookValue, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (monthlyBookValue == null || monthlyBookValue.size() == 0)
			throw new DBException(6);

		for (MonthlyBookValue t : monthlyBookValue)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyBookValue = monthlyBookValueReposDay.saveAll(monthlyBookValue);
			monthlyBookValueReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyBookValue = monthlyBookValueReposMon.saveAll(monthlyBookValue);
			monthlyBookValueReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyBookValue = monthlyBookValueReposHist.saveAll(monthlyBookValue);
			monthlyBookValueReposHist.flush();
		} else {
			monthlyBookValue = monthlyBookValueRepos.saveAll(monthlyBookValue);
			monthlyBookValueRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<MonthlyBookValue> monthlyBookValue, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (monthlyBookValue == null || monthlyBookValue.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			monthlyBookValueReposDay.deleteAll(monthlyBookValue);
			monthlyBookValueReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyBookValueReposMon.deleteAll(monthlyBookValue);
			monthlyBookValueReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyBookValueReposHist.deleteAll(monthlyBookValue);
			monthlyBookValueReposHist.flush();
		} else {
			monthlyBookValueRepos.deleteAll(monthlyBookValue);
			monthlyBookValueRepos.flush();
		}
	}

	@Override
	public void Usp_L9_MonthlyLoanBal_Upd(int TBSDYF, String empNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			monthlyBookValueReposDay.uspL9MonthlyloanbalUpd(TBSDYF, empNo);
		else if (dbName.equals(ContentName.onMon))
			monthlyBookValueReposMon.uspL9MonthlyloanbalUpd(TBSDYF, empNo);
		else if (dbName.equals(ContentName.onHist))
			monthlyBookValueReposHist.uspL9MonthlyloanbalUpd(TBSDYF, empNo);
		else
			monthlyBookValueRepos.uspL9MonthlyloanbalUpd(TBSDYF, empNo);
	}

}
