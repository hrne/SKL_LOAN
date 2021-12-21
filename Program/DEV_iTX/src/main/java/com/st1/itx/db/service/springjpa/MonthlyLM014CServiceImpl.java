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
import com.st1.itx.db.domain.MonthlyLM014C;
import com.st1.itx.db.domain.MonthlyLM014CId;
import com.st1.itx.db.repository.online.MonthlyLM014CRepository;
import com.st1.itx.db.repository.day.MonthlyLM014CRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM014CRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM014CRepositoryHist;
import com.st1.itx.db.service.MonthlyLM014CService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM014CService")
@Repository
public class MonthlyLM014CServiceImpl implements MonthlyLM014CService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(MonthlyLM014CServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MonthlyLM014CRepository monthlyLM014CRepos;

	@Autowired
	private MonthlyLM014CRepositoryDay monthlyLM014CReposDay;

	@Autowired
	private MonthlyLM014CRepositoryMon monthlyLM014CReposMon;

	@Autowired
	private MonthlyLM014CRepositoryHist monthlyLM014CReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(monthlyLM014CRepos);
		org.junit.Assert.assertNotNull(monthlyLM014CReposDay);
		org.junit.Assert.assertNotNull(monthlyLM014CReposMon);
		org.junit.Assert.assertNotNull(monthlyLM014CReposHist);
	}

	@Override
	public MonthlyLM014C findById(MonthlyLM014CId monthlyLM014CId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + monthlyLM014CId);
		Optional<MonthlyLM014C> monthlyLM014C = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM014C = monthlyLM014CReposDay.findById(monthlyLM014CId);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014C = monthlyLM014CReposMon.findById(monthlyLM014CId);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014C = monthlyLM014CReposHist.findById(monthlyLM014CId);
		else
			monthlyLM014C = monthlyLM014CRepos.findById(monthlyLM014CId);
		MonthlyLM014C obj = monthlyLM014C.isPresent() ? monthlyLM014C.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MonthlyLM014C> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLM014C> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "AcctCode", "AccountType", "AcBookCode", "RelsFlag", "ClFlag", "DepartmentCode"));
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLM014CReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLM014CReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLM014CReposHist.findAll(pageable);
		else
			slice = monthlyLM014CRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MonthlyLM014C> DataYMEq(int dataYM_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLM014C> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		logger.info("DataYMEq " + dbName + " : " + "dataYM_0 : " + dataYM_0);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLM014CReposDay.findAllByDataYMIs(dataYM_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLM014CReposMon.findAllByDataYMIs(dataYM_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLM014CReposHist.findAllByDataYMIs(dataYM_0, pageable);
		else
			slice = monthlyLM014CRepos.findAllByDataYMIs(dataYM_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MonthlyLM014C holdById(MonthlyLM014CId monthlyLM014CId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + monthlyLM014CId);
		Optional<MonthlyLM014C> monthlyLM014C = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM014C = monthlyLM014CReposDay.findByMonthlyLM014CId(monthlyLM014CId);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014C = monthlyLM014CReposMon.findByMonthlyLM014CId(monthlyLM014CId);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014C = monthlyLM014CReposHist.findByMonthlyLM014CId(monthlyLM014CId);
		else
			monthlyLM014C = monthlyLM014CRepos.findByMonthlyLM014CId(monthlyLM014CId);
		return monthlyLM014C.isPresent() ? monthlyLM014C.get() : null;
	}

	@Override
	public MonthlyLM014C holdById(MonthlyLM014C monthlyLM014C, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + monthlyLM014C.getMonthlyLM014CId());
		Optional<MonthlyLM014C> monthlyLM014CT = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM014CT = monthlyLM014CReposDay.findByMonthlyLM014CId(monthlyLM014C.getMonthlyLM014CId());
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014CT = monthlyLM014CReposMon.findByMonthlyLM014CId(monthlyLM014C.getMonthlyLM014CId());
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014CT = monthlyLM014CReposHist.findByMonthlyLM014CId(monthlyLM014C.getMonthlyLM014CId());
		else
			monthlyLM014CT = monthlyLM014CRepos.findByMonthlyLM014CId(monthlyLM014C.getMonthlyLM014CId());
		return monthlyLM014CT.isPresent() ? monthlyLM014CT.get() : null;
	}

	@Override
	public MonthlyLM014C insert(MonthlyLM014C monthlyLM014C, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + monthlyLM014C.getMonthlyLM014CId());
		if (this.findById(monthlyLM014C.getMonthlyLM014CId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			monthlyLM014C.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM014CReposDay.saveAndFlush(monthlyLM014C);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM014CReposMon.saveAndFlush(monthlyLM014C);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM014CReposHist.saveAndFlush(monthlyLM014C);
		else
			return monthlyLM014CRepos.saveAndFlush(monthlyLM014C);
	}

	@Override
	public MonthlyLM014C update(MonthlyLM014C monthlyLM014C, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + monthlyLM014C.getMonthlyLM014CId());
		if (!empNot.isEmpty())
			monthlyLM014C.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM014CReposDay.saveAndFlush(monthlyLM014C);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM014CReposMon.saveAndFlush(monthlyLM014C);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM014CReposHist.saveAndFlush(monthlyLM014C);
		else
			return monthlyLM014CRepos.saveAndFlush(monthlyLM014C);
	}

	@Override
	public MonthlyLM014C update2(MonthlyLM014C monthlyLM014C, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + monthlyLM014C.getMonthlyLM014CId());
		if (!empNot.isEmpty())
			monthlyLM014C.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			monthlyLM014CReposDay.saveAndFlush(monthlyLM014C);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014CReposMon.saveAndFlush(monthlyLM014C);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014CReposHist.saveAndFlush(monthlyLM014C);
		else
			monthlyLM014CRepos.saveAndFlush(monthlyLM014C);
		return this.findById(monthlyLM014C.getMonthlyLM014CId());
	}

	@Override
	public void delete(MonthlyLM014C monthlyLM014C, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + monthlyLM014C.getMonthlyLM014CId());
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014CReposDay.delete(monthlyLM014C);
			monthlyLM014CReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014CReposMon.delete(monthlyLM014C);
			monthlyLM014CReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014CReposHist.delete(monthlyLM014C);
			monthlyLM014CReposHist.flush();
		} else {
			monthlyLM014CRepos.delete(monthlyLM014C);
			monthlyLM014CRepos.flush();
		}
	}

	@Override
	public void insertAll(List<MonthlyLM014C> monthlyLM014C, TitaVo... titaVo) throws DBException {
		if (monthlyLM014C == null || monthlyLM014C.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (MonthlyLM014C t : monthlyLM014C)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014C = monthlyLM014CReposDay.saveAll(monthlyLM014C);
			monthlyLM014CReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014C = monthlyLM014CReposMon.saveAll(monthlyLM014C);
			monthlyLM014CReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014C = monthlyLM014CReposHist.saveAll(monthlyLM014C);
			monthlyLM014CReposHist.flush();
		} else {
			monthlyLM014C = monthlyLM014CRepos.saveAll(monthlyLM014C);
			monthlyLM014CRepos.flush();
		}
	}

	@Override
	public void updateAll(List<MonthlyLM014C> monthlyLM014C, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (monthlyLM014C == null || monthlyLM014C.size() == 0)
			throw new DBException(6);

		for (MonthlyLM014C t : monthlyLM014C)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014C = monthlyLM014CReposDay.saveAll(monthlyLM014C);
			monthlyLM014CReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014C = monthlyLM014CReposMon.saveAll(monthlyLM014C);
			monthlyLM014CReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014C = monthlyLM014CReposHist.saveAll(monthlyLM014C);
			monthlyLM014CReposHist.flush();
		} else {
			monthlyLM014C = monthlyLM014CRepos.saveAll(monthlyLM014C);
			monthlyLM014CRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<MonthlyLM014C> monthlyLM014C, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (monthlyLM014C == null || monthlyLM014C.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014CReposDay.deleteAll(monthlyLM014C);
			monthlyLM014CReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014CReposMon.deleteAll(monthlyLM014C);
			monthlyLM014CReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014CReposHist.deleteAll(monthlyLM014C);
			monthlyLM014CReposHist.flush();
		} else {
			monthlyLM014CRepos.deleteAll(monthlyLM014C);
			monthlyLM014CRepos.flush();
		}
	}

}
