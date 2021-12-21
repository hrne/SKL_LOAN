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
import com.st1.itx.db.domain.MonthlyLM014A;
import com.st1.itx.db.domain.MonthlyLM014AId;
import com.st1.itx.db.repository.online.MonthlyLM014ARepository;
import com.st1.itx.db.repository.day.MonthlyLM014ARepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM014ARepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM014ARepositoryHist;
import com.st1.itx.db.service.MonthlyLM014AService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM014AService")
@Repository
public class MonthlyLM014AServiceImpl implements MonthlyLM014AService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(MonthlyLM014AServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MonthlyLM014ARepository monthlyLM014ARepos;

	@Autowired
	private MonthlyLM014ARepositoryDay monthlyLM014AReposDay;

	@Autowired
	private MonthlyLM014ARepositoryMon monthlyLM014AReposMon;

	@Autowired
	private MonthlyLM014ARepositoryHist monthlyLM014AReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(monthlyLM014ARepos);
		org.junit.Assert.assertNotNull(monthlyLM014AReposDay);
		org.junit.Assert.assertNotNull(monthlyLM014AReposMon);
		org.junit.Assert.assertNotNull(monthlyLM014AReposHist);
	}

	@Override
	public MonthlyLM014A findById(MonthlyLM014AId monthlyLM014AId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + monthlyLM014AId);
		Optional<MonthlyLM014A> monthlyLM014A = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM014A = monthlyLM014AReposDay.findById(monthlyLM014AId);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014A = monthlyLM014AReposMon.findById(monthlyLM014AId);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014A = monthlyLM014AReposHist.findById(monthlyLM014AId);
		else
			monthlyLM014A = monthlyLM014ARepos.findById(monthlyLM014AId);
		MonthlyLM014A obj = monthlyLM014A.isPresent() ? monthlyLM014A.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MonthlyLM014A> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLM014A> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "AcctCode", "AccountType", "AcBookCode"));
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLM014AReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLM014AReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLM014AReposHist.findAll(pageable);
		else
			slice = monthlyLM014ARepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MonthlyLM014A> DataYMEq(int dataYM_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLM014A> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		logger.info("DataYMEq " + dbName + " : " + "dataYM_0 : " + dataYM_0);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLM014AReposDay.findAllByDataYMIs(dataYM_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLM014AReposMon.findAllByDataYMIs(dataYM_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLM014AReposHist.findAllByDataYMIs(dataYM_0, pageable);
		else
			slice = monthlyLM014ARepos.findAllByDataYMIs(dataYM_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MonthlyLM014A holdById(MonthlyLM014AId monthlyLM014AId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + monthlyLM014AId);
		Optional<MonthlyLM014A> monthlyLM014A = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM014A = monthlyLM014AReposDay.findByMonthlyLM014AId(monthlyLM014AId);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014A = monthlyLM014AReposMon.findByMonthlyLM014AId(monthlyLM014AId);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014A = monthlyLM014AReposHist.findByMonthlyLM014AId(monthlyLM014AId);
		else
			monthlyLM014A = monthlyLM014ARepos.findByMonthlyLM014AId(monthlyLM014AId);
		return monthlyLM014A.isPresent() ? monthlyLM014A.get() : null;
	}

	@Override
	public MonthlyLM014A holdById(MonthlyLM014A monthlyLM014A, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + monthlyLM014A.getMonthlyLM014AId());
		Optional<MonthlyLM014A> monthlyLM014AT = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM014AT = monthlyLM014AReposDay.findByMonthlyLM014AId(monthlyLM014A.getMonthlyLM014AId());
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014AT = monthlyLM014AReposMon.findByMonthlyLM014AId(monthlyLM014A.getMonthlyLM014AId());
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014AT = monthlyLM014AReposHist.findByMonthlyLM014AId(monthlyLM014A.getMonthlyLM014AId());
		else
			monthlyLM014AT = monthlyLM014ARepos.findByMonthlyLM014AId(monthlyLM014A.getMonthlyLM014AId());
		return monthlyLM014AT.isPresent() ? monthlyLM014AT.get() : null;
	}

	@Override
	public MonthlyLM014A insert(MonthlyLM014A monthlyLM014A, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + monthlyLM014A.getMonthlyLM014AId());
		if (this.findById(monthlyLM014A.getMonthlyLM014AId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			monthlyLM014A.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM014AReposDay.saveAndFlush(monthlyLM014A);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM014AReposMon.saveAndFlush(monthlyLM014A);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM014AReposHist.saveAndFlush(monthlyLM014A);
		else
			return monthlyLM014ARepos.saveAndFlush(monthlyLM014A);
	}

	@Override
	public MonthlyLM014A update(MonthlyLM014A monthlyLM014A, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + monthlyLM014A.getMonthlyLM014AId());
		if (!empNot.isEmpty())
			monthlyLM014A.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM014AReposDay.saveAndFlush(monthlyLM014A);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM014AReposMon.saveAndFlush(monthlyLM014A);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM014AReposHist.saveAndFlush(monthlyLM014A);
		else
			return monthlyLM014ARepos.saveAndFlush(monthlyLM014A);
	}

	@Override
	public MonthlyLM014A update2(MonthlyLM014A monthlyLM014A, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + monthlyLM014A.getMonthlyLM014AId());
		if (!empNot.isEmpty())
			monthlyLM014A.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			monthlyLM014AReposDay.saveAndFlush(monthlyLM014A);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014AReposMon.saveAndFlush(monthlyLM014A);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014AReposHist.saveAndFlush(monthlyLM014A);
		else
			monthlyLM014ARepos.saveAndFlush(monthlyLM014A);
		return this.findById(monthlyLM014A.getMonthlyLM014AId());
	}

	@Override
	public void delete(MonthlyLM014A monthlyLM014A, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + monthlyLM014A.getMonthlyLM014AId());
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014AReposDay.delete(monthlyLM014A);
			monthlyLM014AReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014AReposMon.delete(monthlyLM014A);
			monthlyLM014AReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014AReposHist.delete(monthlyLM014A);
			monthlyLM014AReposHist.flush();
		} else {
			monthlyLM014ARepos.delete(monthlyLM014A);
			monthlyLM014ARepos.flush();
		}
	}

	@Override
	public void insertAll(List<MonthlyLM014A> monthlyLM014A, TitaVo... titaVo) throws DBException {
		if (monthlyLM014A == null || monthlyLM014A.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (MonthlyLM014A t : monthlyLM014A)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014A = monthlyLM014AReposDay.saveAll(monthlyLM014A);
			monthlyLM014AReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014A = monthlyLM014AReposMon.saveAll(monthlyLM014A);
			monthlyLM014AReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014A = monthlyLM014AReposHist.saveAll(monthlyLM014A);
			monthlyLM014AReposHist.flush();
		} else {
			monthlyLM014A = monthlyLM014ARepos.saveAll(monthlyLM014A);
			monthlyLM014ARepos.flush();
		}
	}

	@Override
	public void updateAll(List<MonthlyLM014A> monthlyLM014A, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (monthlyLM014A == null || monthlyLM014A.size() == 0)
			throw new DBException(6);

		for (MonthlyLM014A t : monthlyLM014A)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014A = monthlyLM014AReposDay.saveAll(monthlyLM014A);
			monthlyLM014AReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014A = monthlyLM014AReposMon.saveAll(monthlyLM014A);
			monthlyLM014AReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014A = monthlyLM014AReposHist.saveAll(monthlyLM014A);
			monthlyLM014AReposHist.flush();
		} else {
			monthlyLM014A = monthlyLM014ARepos.saveAll(monthlyLM014A);
			monthlyLM014ARepos.flush();
		}
	}

	@Override
	public void deleteAll(List<MonthlyLM014A> monthlyLM014A, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (monthlyLM014A == null || monthlyLM014A.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014AReposDay.deleteAll(monthlyLM014A);
			monthlyLM014AReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014AReposMon.deleteAll(monthlyLM014A);
			monthlyLM014AReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014AReposHist.deleteAll(monthlyLM014A);
			monthlyLM014AReposHist.flush();
		} else {
			monthlyLM014ARepos.deleteAll(monthlyLM014A);
			monthlyLM014ARepos.flush();
		}
	}

}
