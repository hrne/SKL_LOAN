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
import com.st1.itx.db.domain.MonthlyLM051;
import com.st1.itx.db.domain.MonthlyLM051Id;
import com.st1.itx.db.repository.online.MonthlyLM051Repository;
import com.st1.itx.db.repository.day.MonthlyLM051RepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM051RepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM051RepositoryHist;
import com.st1.itx.db.service.MonthlyLM051Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM051Service")
@Repository
public class MonthlyLM051ServiceImpl extends ASpringJpaParm implements MonthlyLM051Service, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MonthlyLM051Repository monthlyLM051Repos;

	@Autowired
	private MonthlyLM051RepositoryDay monthlyLM051ReposDay;

	@Autowired
	private MonthlyLM051RepositoryMon monthlyLM051ReposMon;

	@Autowired
	private MonthlyLM051RepositoryHist monthlyLM051ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(monthlyLM051Repos);
		org.junit.Assert.assertNotNull(monthlyLM051ReposDay);
		org.junit.Assert.assertNotNull(monthlyLM051ReposMon);
		org.junit.Assert.assertNotNull(monthlyLM051ReposHist);
	}

	@Override
	public MonthlyLM051 findById(MonthlyLM051Id monthlyLM051Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + monthlyLM051Id);
		Optional<MonthlyLM051> monthlyLM051 = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM051 = monthlyLM051ReposDay.findById(monthlyLM051Id);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM051 = monthlyLM051ReposMon.findById(monthlyLM051Id);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM051 = monthlyLM051ReposHist.findById(monthlyLM051Id);
		else
			monthlyLM051 = monthlyLM051Repos.findById(monthlyLM051Id);
		MonthlyLM051 obj = monthlyLM051.isPresent() ? monthlyLM051.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MonthlyLM051> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLM051> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLM051ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLM051ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLM051ReposHist.findAll(pageable);
		else
			slice = monthlyLM051Repos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MonthlyLM051 holdById(MonthlyLM051Id monthlyLM051Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + monthlyLM051Id);
		Optional<MonthlyLM051> monthlyLM051 = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM051 = monthlyLM051ReposDay.findByMonthlyLM051Id(monthlyLM051Id);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM051 = monthlyLM051ReposMon.findByMonthlyLM051Id(monthlyLM051Id);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM051 = monthlyLM051ReposHist.findByMonthlyLM051Id(monthlyLM051Id);
		else
			monthlyLM051 = monthlyLM051Repos.findByMonthlyLM051Id(monthlyLM051Id);
		return monthlyLM051.isPresent() ? monthlyLM051.get() : null;
	}

	@Override
	public MonthlyLM051 holdById(MonthlyLM051 monthlyLM051, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + monthlyLM051.getMonthlyLM051Id());
		Optional<MonthlyLM051> monthlyLM051T = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM051T = monthlyLM051ReposDay.findByMonthlyLM051Id(monthlyLM051.getMonthlyLM051Id());
		else if (dbName.equals(ContentName.onMon))
			monthlyLM051T = monthlyLM051ReposMon.findByMonthlyLM051Id(monthlyLM051.getMonthlyLM051Id());
		else if (dbName.equals(ContentName.onHist))
			monthlyLM051T = monthlyLM051ReposHist.findByMonthlyLM051Id(monthlyLM051.getMonthlyLM051Id());
		else
			monthlyLM051T = monthlyLM051Repos.findByMonthlyLM051Id(monthlyLM051.getMonthlyLM051Id());
		return monthlyLM051T.isPresent() ? monthlyLM051T.get() : null;
	}

	@Override
	public MonthlyLM051 insert(MonthlyLM051 monthlyLM051, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + monthlyLM051.getMonthlyLM051Id());
		if (this.findById(monthlyLM051.getMonthlyLM051Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			monthlyLM051.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM051ReposDay.saveAndFlush(monthlyLM051);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM051ReposMon.saveAndFlush(monthlyLM051);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM051ReposHist.saveAndFlush(monthlyLM051);
		else
			return monthlyLM051Repos.saveAndFlush(monthlyLM051);
	}

	@Override
	public MonthlyLM051 update(MonthlyLM051 monthlyLM051, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + monthlyLM051.getMonthlyLM051Id());
		if (!empNot.isEmpty())
			monthlyLM051.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM051ReposDay.saveAndFlush(monthlyLM051);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM051ReposMon.saveAndFlush(monthlyLM051);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM051ReposHist.saveAndFlush(monthlyLM051);
		else
			return monthlyLM051Repos.saveAndFlush(monthlyLM051);
	}

	@Override
	public MonthlyLM051 update2(MonthlyLM051 monthlyLM051, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + monthlyLM051.getMonthlyLM051Id());
		if (!empNot.isEmpty())
			monthlyLM051.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			monthlyLM051ReposDay.saveAndFlush(monthlyLM051);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM051ReposMon.saveAndFlush(monthlyLM051);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM051ReposHist.saveAndFlush(monthlyLM051);
		else
			monthlyLM051Repos.saveAndFlush(monthlyLM051);
		return this.findById(monthlyLM051.getMonthlyLM051Id());
	}

	@Override
	public void delete(MonthlyLM051 monthlyLM051, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + monthlyLM051.getMonthlyLM051Id());
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM051ReposDay.delete(monthlyLM051);
			monthlyLM051ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM051ReposMon.delete(monthlyLM051);
			monthlyLM051ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM051ReposHist.delete(monthlyLM051);
			monthlyLM051ReposHist.flush();
		} else {
			monthlyLM051Repos.delete(monthlyLM051);
			monthlyLM051Repos.flush();
		}
	}

	@Override
	public void insertAll(List<MonthlyLM051> monthlyLM051, TitaVo... titaVo) throws DBException {
		if (monthlyLM051 == null || monthlyLM051.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (MonthlyLM051 t : monthlyLM051)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM051 = monthlyLM051ReposDay.saveAll(monthlyLM051);
			monthlyLM051ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM051 = monthlyLM051ReposMon.saveAll(monthlyLM051);
			monthlyLM051ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM051 = monthlyLM051ReposHist.saveAll(monthlyLM051);
			monthlyLM051ReposHist.flush();
		} else {
			monthlyLM051 = monthlyLM051Repos.saveAll(monthlyLM051);
			monthlyLM051Repos.flush();
		}
	}

	@Override
	public void updateAll(List<MonthlyLM051> monthlyLM051, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (monthlyLM051 == null || monthlyLM051.size() == 0)
			throw new DBException(6);

		for (MonthlyLM051 t : monthlyLM051)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM051 = monthlyLM051ReposDay.saveAll(monthlyLM051);
			monthlyLM051ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM051 = monthlyLM051ReposMon.saveAll(monthlyLM051);
			monthlyLM051ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM051 = monthlyLM051ReposHist.saveAll(monthlyLM051);
			monthlyLM051ReposHist.flush();
		} else {
			monthlyLM051 = monthlyLM051Repos.saveAll(monthlyLM051);
			monthlyLM051Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<MonthlyLM051> monthlyLM051, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (monthlyLM051 == null || monthlyLM051.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM051ReposDay.deleteAll(monthlyLM051);
			monthlyLM051ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM051ReposMon.deleteAll(monthlyLM051);
			monthlyLM051ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM051ReposHist.deleteAll(monthlyLM051);
			monthlyLM051ReposHist.flush();
		} else {
			monthlyLM051Repos.deleteAll(monthlyLM051);
			monthlyLM051Repos.flush();
		}
	}

	@Override
	public void Usp_L9_MonthlyLM051_Upd(int TBSDYF, String empNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			monthlyLM051ReposDay.uspL9Monthlylm051Upd(TBSDYF, empNo);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM051ReposMon.uspL9Monthlylm051Upd(TBSDYF, empNo);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM051ReposHist.uspL9Monthlylm051Upd(TBSDYF, empNo);
		else
			monthlyLM051Repos.uspL9Monthlylm051Upd(TBSDYF, empNo);
	}

}
