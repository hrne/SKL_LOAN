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
import com.st1.itx.db.domain.MonthlyLM032;
import com.st1.itx.db.domain.MonthlyLM032Id;
import com.st1.itx.db.repository.online.MonthlyLM032Repository;
import com.st1.itx.db.repository.day.MonthlyLM032RepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM032RepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM032RepositoryHist;
import com.st1.itx.db.service.MonthlyLM032Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM032Service")
@Repository
public class MonthlyLM032ServiceImpl extends ASpringJpaParm implements MonthlyLM032Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MonthlyLM032Repository monthlyLM032Repos;

	@Autowired
	private MonthlyLM032RepositoryDay monthlyLM032ReposDay;

	@Autowired
	private MonthlyLM032RepositoryMon monthlyLM032ReposMon;

	@Autowired
	private MonthlyLM032RepositoryHist monthlyLM032ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(monthlyLM032Repos);
		org.junit.Assert.assertNotNull(monthlyLM032ReposDay);
		org.junit.Assert.assertNotNull(monthlyLM032ReposMon);
		org.junit.Assert.assertNotNull(monthlyLM032ReposHist);
	}

	@Override
	public MonthlyLM032 findById(MonthlyLM032Id monthlyLM032Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + monthlyLM032Id);
		Optional<MonthlyLM032> monthlyLM032 = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM032 = monthlyLM032ReposDay.findById(monthlyLM032Id);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM032 = monthlyLM032ReposMon.findById(monthlyLM032Id);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM032 = monthlyLM032ReposHist.findById(monthlyLM032Id);
		else
			monthlyLM032 = monthlyLM032Repos.findById(monthlyLM032Id);
		MonthlyLM032 obj = monthlyLM032.isPresent() ? monthlyLM032.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MonthlyLM032> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLM032> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ADTYMT", "LMSACN", "LMSAPN"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ADTYMT", "LMSACN", "LMSAPN"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLM032ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLM032ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLM032ReposHist.findAll(pageable);
		else
			slice = monthlyLM032Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MonthlyLM032 holdById(MonthlyLM032Id monthlyLM032Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + monthlyLM032Id);
		Optional<MonthlyLM032> monthlyLM032 = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM032 = monthlyLM032ReposDay.findByMonthlyLM032Id(monthlyLM032Id);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM032 = monthlyLM032ReposMon.findByMonthlyLM032Id(monthlyLM032Id);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM032 = monthlyLM032ReposHist.findByMonthlyLM032Id(monthlyLM032Id);
		else
			monthlyLM032 = monthlyLM032Repos.findByMonthlyLM032Id(monthlyLM032Id);
		return monthlyLM032.isPresent() ? monthlyLM032.get() : null;
	}

	@Override
	public MonthlyLM032 holdById(MonthlyLM032 monthlyLM032, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + monthlyLM032.getMonthlyLM032Id());
		Optional<MonthlyLM032> monthlyLM032T = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM032T = monthlyLM032ReposDay.findByMonthlyLM032Id(monthlyLM032.getMonthlyLM032Id());
		else if (dbName.equals(ContentName.onMon))
			monthlyLM032T = monthlyLM032ReposMon.findByMonthlyLM032Id(monthlyLM032.getMonthlyLM032Id());
		else if (dbName.equals(ContentName.onHist))
			monthlyLM032T = monthlyLM032ReposHist.findByMonthlyLM032Id(monthlyLM032.getMonthlyLM032Id());
		else
			monthlyLM032T = monthlyLM032Repos.findByMonthlyLM032Id(monthlyLM032.getMonthlyLM032Id());
		return monthlyLM032T.isPresent() ? monthlyLM032T.get() : null;
	}

	@Override
	public MonthlyLM032 insert(MonthlyLM032 monthlyLM032, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + monthlyLM032.getMonthlyLM032Id());
		if (this.findById(monthlyLM032.getMonthlyLM032Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			monthlyLM032.setCreateEmpNo(empNot);

		if (monthlyLM032.getLastUpdateEmpNo() == null || monthlyLM032.getLastUpdateEmpNo().isEmpty())
			monthlyLM032.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM032ReposDay.saveAndFlush(monthlyLM032);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM032ReposMon.saveAndFlush(monthlyLM032);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM032ReposHist.saveAndFlush(monthlyLM032);
		else
			return monthlyLM032Repos.saveAndFlush(monthlyLM032);
	}

	@Override
	public MonthlyLM032 update(MonthlyLM032 monthlyLM032, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + monthlyLM032.getMonthlyLM032Id());
		if (!empNot.isEmpty())
			monthlyLM032.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM032ReposDay.saveAndFlush(monthlyLM032);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM032ReposMon.saveAndFlush(monthlyLM032);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM032ReposHist.saveAndFlush(monthlyLM032);
		else
			return monthlyLM032Repos.saveAndFlush(monthlyLM032);
	}

	@Override
	public MonthlyLM032 update2(MonthlyLM032 monthlyLM032, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + monthlyLM032.getMonthlyLM032Id());
		if (!empNot.isEmpty())
			monthlyLM032.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			monthlyLM032ReposDay.saveAndFlush(monthlyLM032);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM032ReposMon.saveAndFlush(monthlyLM032);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM032ReposHist.saveAndFlush(monthlyLM032);
		else
			monthlyLM032Repos.saveAndFlush(monthlyLM032);
		return this.findById(monthlyLM032.getMonthlyLM032Id());
	}

	@Override
	public void delete(MonthlyLM032 monthlyLM032, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + monthlyLM032.getMonthlyLM032Id());
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM032ReposDay.delete(monthlyLM032);
			monthlyLM032ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM032ReposMon.delete(monthlyLM032);
			monthlyLM032ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM032ReposHist.delete(monthlyLM032);
			monthlyLM032ReposHist.flush();
		} else {
			monthlyLM032Repos.delete(monthlyLM032);
			monthlyLM032Repos.flush();
		}
	}

	@Override
	public void insertAll(List<MonthlyLM032> monthlyLM032, TitaVo... titaVo) throws DBException {
		if (monthlyLM032 == null || monthlyLM032.size() == 0)
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
		for (MonthlyLM032 t : monthlyLM032) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM032 = monthlyLM032ReposDay.saveAll(monthlyLM032);
			monthlyLM032ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM032 = monthlyLM032ReposMon.saveAll(monthlyLM032);
			monthlyLM032ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM032 = monthlyLM032ReposHist.saveAll(monthlyLM032);
			monthlyLM032ReposHist.flush();
		} else {
			monthlyLM032 = monthlyLM032Repos.saveAll(monthlyLM032);
			monthlyLM032Repos.flush();
		}
	}

	@Override
	public void updateAll(List<MonthlyLM032> monthlyLM032, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (monthlyLM032 == null || monthlyLM032.size() == 0)
			throw new DBException(6);

		for (MonthlyLM032 t : monthlyLM032)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM032 = monthlyLM032ReposDay.saveAll(monthlyLM032);
			monthlyLM032ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM032 = monthlyLM032ReposMon.saveAll(monthlyLM032);
			monthlyLM032ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM032 = monthlyLM032ReposHist.saveAll(monthlyLM032);
			monthlyLM032ReposHist.flush();
		} else {
			monthlyLM032 = monthlyLM032Repos.saveAll(monthlyLM032);
			monthlyLM032Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<MonthlyLM032> monthlyLM032, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (monthlyLM032 == null || monthlyLM032.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM032ReposDay.deleteAll(monthlyLM032);
			monthlyLM032ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM032ReposMon.deleteAll(monthlyLM032);
			monthlyLM032ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM032ReposHist.deleteAll(monthlyLM032);
			monthlyLM032ReposHist.flush();
		} else {
			monthlyLM032Repos.deleteAll(monthlyLM032);
			monthlyLM032Repos.flush();
		}
	}

	@Override
	public void Usp_L9_MonthlyLM032_Upd(int TBSDYF, String empNo, int LMBSDYF, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			monthlyLM032ReposDay.uspL9Monthlylm032Upd(TBSDYF, empNo, LMBSDYF);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM032ReposMon.uspL9Monthlylm032Upd(TBSDYF, empNo, LMBSDYF);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM032ReposHist.uspL9Monthlylm032Upd(TBSDYF, empNo, LMBSDYF);
		else
			monthlyLM032Repos.uspL9Monthlylm032Upd(TBSDYF, empNo, LMBSDYF);
	}

}
