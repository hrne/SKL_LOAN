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
import com.st1.itx.db.domain.MonthlyLM003;
import com.st1.itx.db.domain.MonthlyLM003Id;
import com.st1.itx.db.repository.online.MonthlyLM003Repository;
import com.st1.itx.db.repository.day.MonthlyLM003RepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM003RepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM003RepositoryHist;
import com.st1.itx.db.service.MonthlyLM003Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM003Service")
@Repository
public class MonthlyLM003ServiceImpl implements MonthlyLM003Service, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(MonthlyLM003ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MonthlyLM003Repository monthlyLM003Repos;

	@Autowired
	private MonthlyLM003RepositoryDay monthlyLM003ReposDay;

	@Autowired
	private MonthlyLM003RepositoryMon monthlyLM003ReposMon;

	@Autowired
	private MonthlyLM003RepositoryHist monthlyLM003ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(monthlyLM003Repos);
		org.junit.Assert.assertNotNull(monthlyLM003ReposDay);
		org.junit.Assert.assertNotNull(monthlyLM003ReposMon);
		org.junit.Assert.assertNotNull(monthlyLM003ReposHist);
	}

	@Override
	public MonthlyLM003 findById(MonthlyLM003Id monthlyLM003Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + monthlyLM003Id);
		Optional<MonthlyLM003> monthlyLM003 = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM003 = monthlyLM003ReposDay.findById(monthlyLM003Id);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM003 = monthlyLM003ReposMon.findById(monthlyLM003Id);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM003 = monthlyLM003ReposHist.findById(monthlyLM003Id);
		else
			monthlyLM003 = monthlyLM003Repos.findById(monthlyLM003Id);
		MonthlyLM003 obj = monthlyLM003.isPresent() ? monthlyLM003.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MonthlyLM003> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLM003> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EntType", "DataYear", "DataMonth"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLM003ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLM003ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLM003ReposHist.findAll(pageable);
		else
			slice = monthlyLM003Repos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MonthlyLM003 holdById(MonthlyLM003Id monthlyLM003Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + monthlyLM003Id);
		Optional<MonthlyLM003> monthlyLM003 = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM003 = monthlyLM003ReposDay.findByMonthlyLM003Id(monthlyLM003Id);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM003 = monthlyLM003ReposMon.findByMonthlyLM003Id(monthlyLM003Id);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM003 = monthlyLM003ReposHist.findByMonthlyLM003Id(monthlyLM003Id);
		else
			monthlyLM003 = monthlyLM003Repos.findByMonthlyLM003Id(monthlyLM003Id);
		return monthlyLM003.isPresent() ? monthlyLM003.get() : null;
	}

	@Override
	public MonthlyLM003 holdById(MonthlyLM003 monthlyLM003, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + monthlyLM003.getMonthlyLM003Id());
		Optional<MonthlyLM003> monthlyLM003T = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM003T = monthlyLM003ReposDay.findByMonthlyLM003Id(monthlyLM003.getMonthlyLM003Id());
		else if (dbName.equals(ContentName.onMon))
			monthlyLM003T = monthlyLM003ReposMon.findByMonthlyLM003Id(monthlyLM003.getMonthlyLM003Id());
		else if (dbName.equals(ContentName.onHist))
			monthlyLM003T = monthlyLM003ReposHist.findByMonthlyLM003Id(monthlyLM003.getMonthlyLM003Id());
		else
			monthlyLM003T = monthlyLM003Repos.findByMonthlyLM003Id(monthlyLM003.getMonthlyLM003Id());
		return monthlyLM003T.isPresent() ? monthlyLM003T.get() : null;
	}

	@Override
	public MonthlyLM003 insert(MonthlyLM003 monthlyLM003, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + monthlyLM003.getMonthlyLM003Id());
		if (this.findById(monthlyLM003.getMonthlyLM003Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			monthlyLM003.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM003ReposDay.saveAndFlush(monthlyLM003);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM003ReposMon.saveAndFlush(monthlyLM003);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM003ReposHist.saveAndFlush(monthlyLM003);
		else
			return monthlyLM003Repos.saveAndFlush(monthlyLM003);
	}

	@Override
	public MonthlyLM003 update(MonthlyLM003 monthlyLM003, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + monthlyLM003.getMonthlyLM003Id());
		if (!empNot.isEmpty())
			monthlyLM003.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM003ReposDay.saveAndFlush(monthlyLM003);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM003ReposMon.saveAndFlush(monthlyLM003);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM003ReposHist.saveAndFlush(monthlyLM003);
		else
			return monthlyLM003Repos.saveAndFlush(monthlyLM003);
	}

	@Override
	public MonthlyLM003 update2(MonthlyLM003 monthlyLM003, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + monthlyLM003.getMonthlyLM003Id());
		if (!empNot.isEmpty())
			monthlyLM003.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			monthlyLM003ReposDay.saveAndFlush(monthlyLM003);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM003ReposMon.saveAndFlush(monthlyLM003);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM003ReposHist.saveAndFlush(monthlyLM003);
		else
			monthlyLM003Repos.saveAndFlush(monthlyLM003);
		return this.findById(monthlyLM003.getMonthlyLM003Id());
	}

	@Override
	public void delete(MonthlyLM003 monthlyLM003, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + monthlyLM003.getMonthlyLM003Id());
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM003ReposDay.delete(monthlyLM003);
			monthlyLM003ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM003ReposMon.delete(monthlyLM003);
			monthlyLM003ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM003ReposHist.delete(monthlyLM003);
			monthlyLM003ReposHist.flush();
		} else {
			monthlyLM003Repos.delete(monthlyLM003);
			monthlyLM003Repos.flush();
		}
	}

	@Override
	public void insertAll(List<MonthlyLM003> monthlyLM003, TitaVo... titaVo) throws DBException {
		if (monthlyLM003 == null || monthlyLM003.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (MonthlyLM003 t : monthlyLM003)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM003 = monthlyLM003ReposDay.saveAll(monthlyLM003);
			monthlyLM003ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM003 = monthlyLM003ReposMon.saveAll(monthlyLM003);
			monthlyLM003ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM003 = monthlyLM003ReposHist.saveAll(monthlyLM003);
			monthlyLM003ReposHist.flush();
		} else {
			monthlyLM003 = monthlyLM003Repos.saveAll(monthlyLM003);
			monthlyLM003Repos.flush();
		}
	}

	@Override
	public void updateAll(List<MonthlyLM003> monthlyLM003, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (monthlyLM003 == null || monthlyLM003.size() == 0)
			throw new DBException(6);

		for (MonthlyLM003 t : monthlyLM003)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM003 = monthlyLM003ReposDay.saveAll(monthlyLM003);
			monthlyLM003ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM003 = monthlyLM003ReposMon.saveAll(monthlyLM003);
			monthlyLM003ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM003 = monthlyLM003ReposHist.saveAll(monthlyLM003);
			monthlyLM003ReposHist.flush();
		} else {
			monthlyLM003 = monthlyLM003Repos.saveAll(monthlyLM003);
			monthlyLM003Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<MonthlyLM003> monthlyLM003, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (monthlyLM003 == null || monthlyLM003.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM003ReposDay.deleteAll(monthlyLM003);
			monthlyLM003ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM003ReposMon.deleteAll(monthlyLM003);
			monthlyLM003ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM003ReposHist.deleteAll(monthlyLM003);
			monthlyLM003ReposHist.flush();
		} else {
			monthlyLM003Repos.deleteAll(monthlyLM003);
			monthlyLM003Repos.flush();
		}
	}

	@Override
	public void Usp_L9_MonthlyLM003_Upd(int TBSDYF, String empNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			monthlyLM003ReposDay.uspL9Monthlylm003Upd(TBSDYF, empNo);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM003ReposMon.uspL9Monthlylm003Upd(TBSDYF, empNo);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM003ReposHist.uspL9Monthlylm003Upd(TBSDYF, empNo);
		else
			monthlyLM003Repos.uspL9Monthlylm003Upd(TBSDYF, empNo);
	}

}
