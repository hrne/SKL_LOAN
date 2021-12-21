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
import com.st1.itx.db.domain.MonthlyLM014B;
import com.st1.itx.db.domain.MonthlyLM014BId;
import com.st1.itx.db.repository.online.MonthlyLM014BRepository;
import com.st1.itx.db.repository.day.MonthlyLM014BRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM014BRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM014BRepositoryHist;
import com.st1.itx.db.service.MonthlyLM014BService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM014BService")
@Repository
public class MonthlyLM014BServiceImpl implements MonthlyLM014BService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(MonthlyLM014BServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private MonthlyLM014BRepository monthlyLM014BRepos;

	@Autowired
	private MonthlyLM014BRepositoryDay monthlyLM014BReposDay;

	@Autowired
	private MonthlyLM014BRepositoryMon monthlyLM014BReposMon;

	@Autowired
	private MonthlyLM014BRepositoryHist monthlyLM014BReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(monthlyLM014BRepos);
		org.junit.Assert.assertNotNull(monthlyLM014BReposDay);
		org.junit.Assert.assertNotNull(monthlyLM014BReposMon);
		org.junit.Assert.assertNotNull(monthlyLM014BReposHist);
	}

	@Override
	public MonthlyLM014B findById(MonthlyLM014BId monthlyLM014BId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + monthlyLM014BId);
		Optional<MonthlyLM014B> monthlyLM014B = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM014B = monthlyLM014BReposDay.findById(monthlyLM014BId);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014B = monthlyLM014BReposMon.findById(monthlyLM014BId);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014B = monthlyLM014BReposHist.findById(monthlyLM014BId);
		else
			monthlyLM014B = monthlyLM014BRepos.findById(monthlyLM014BId);
		MonthlyLM014B obj = monthlyLM014B.isPresent() ? monthlyLM014B.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<MonthlyLM014B> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLM014B> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "AcctCode", "AccountType", "AcBookCode", "EntCode", "RelsFlag", "ClFlag"));
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLM014BReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLM014BReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLM014BReposHist.findAll(pageable);
		else
			slice = monthlyLM014BRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<MonthlyLM014B> DataYMEq(int dataYM_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<MonthlyLM014B> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		logger.info("DataYMEq " + dbName + " : " + "dataYM_0 : " + dataYM_0);
		if (dbName.equals(ContentName.onDay))
			slice = monthlyLM014BReposDay.findAllByDataYMIs(dataYM_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = monthlyLM014BReposMon.findAllByDataYMIs(dataYM_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = monthlyLM014BReposHist.findAllByDataYMIs(dataYM_0, pageable);
		else
			slice = monthlyLM014BRepos.findAllByDataYMIs(dataYM_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public MonthlyLM014B holdById(MonthlyLM014BId monthlyLM014BId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + monthlyLM014BId);
		Optional<MonthlyLM014B> monthlyLM014B = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM014B = monthlyLM014BReposDay.findByMonthlyLM014BId(monthlyLM014BId);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014B = monthlyLM014BReposMon.findByMonthlyLM014BId(monthlyLM014BId);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014B = monthlyLM014BReposHist.findByMonthlyLM014BId(monthlyLM014BId);
		else
			monthlyLM014B = monthlyLM014BRepos.findByMonthlyLM014BId(monthlyLM014BId);
		return monthlyLM014B.isPresent() ? monthlyLM014B.get() : null;
	}

	@Override
	public MonthlyLM014B holdById(MonthlyLM014B monthlyLM014B, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + monthlyLM014B.getMonthlyLM014BId());
		Optional<MonthlyLM014B> monthlyLM014BT = null;
		if (dbName.equals(ContentName.onDay))
			monthlyLM014BT = monthlyLM014BReposDay.findByMonthlyLM014BId(monthlyLM014B.getMonthlyLM014BId());
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014BT = monthlyLM014BReposMon.findByMonthlyLM014BId(monthlyLM014B.getMonthlyLM014BId());
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014BT = monthlyLM014BReposHist.findByMonthlyLM014BId(monthlyLM014B.getMonthlyLM014BId());
		else
			monthlyLM014BT = monthlyLM014BRepos.findByMonthlyLM014BId(monthlyLM014B.getMonthlyLM014BId());
		return monthlyLM014BT.isPresent() ? monthlyLM014BT.get() : null;
	}

	@Override
	public MonthlyLM014B insert(MonthlyLM014B monthlyLM014B, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + monthlyLM014B.getMonthlyLM014BId());
		if (this.findById(monthlyLM014B.getMonthlyLM014BId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			monthlyLM014B.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM014BReposDay.saveAndFlush(monthlyLM014B);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM014BReposMon.saveAndFlush(monthlyLM014B);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM014BReposHist.saveAndFlush(monthlyLM014B);
		else
			return monthlyLM014BRepos.saveAndFlush(monthlyLM014B);
	}

	@Override
	public MonthlyLM014B update(MonthlyLM014B monthlyLM014B, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + monthlyLM014B.getMonthlyLM014BId());
		if (!empNot.isEmpty())
			monthlyLM014B.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return monthlyLM014BReposDay.saveAndFlush(monthlyLM014B);
		else if (dbName.equals(ContentName.onMon))
			return monthlyLM014BReposMon.saveAndFlush(monthlyLM014B);
		else if (dbName.equals(ContentName.onHist))
			return monthlyLM014BReposHist.saveAndFlush(monthlyLM014B);
		else
			return monthlyLM014BRepos.saveAndFlush(monthlyLM014B);
	}

	@Override
	public MonthlyLM014B update2(MonthlyLM014B monthlyLM014B, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + monthlyLM014B.getMonthlyLM014BId());
		if (!empNot.isEmpty())
			monthlyLM014B.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			monthlyLM014BReposDay.saveAndFlush(monthlyLM014B);
		else if (dbName.equals(ContentName.onMon))
			monthlyLM014BReposMon.saveAndFlush(monthlyLM014B);
		else if (dbName.equals(ContentName.onHist))
			monthlyLM014BReposHist.saveAndFlush(monthlyLM014B);
		else
			monthlyLM014BRepos.saveAndFlush(monthlyLM014B);
		return this.findById(monthlyLM014B.getMonthlyLM014BId());
	}

	@Override
	public void delete(MonthlyLM014B monthlyLM014B, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + monthlyLM014B.getMonthlyLM014BId());
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014BReposDay.delete(monthlyLM014B);
			monthlyLM014BReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014BReposMon.delete(monthlyLM014B);
			monthlyLM014BReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014BReposHist.delete(monthlyLM014B);
			monthlyLM014BReposHist.flush();
		} else {
			monthlyLM014BRepos.delete(monthlyLM014B);
			monthlyLM014BRepos.flush();
		}
	}

	@Override
	public void insertAll(List<MonthlyLM014B> monthlyLM014B, TitaVo... titaVo) throws DBException {
		if (monthlyLM014B == null || monthlyLM014B.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (MonthlyLM014B t : monthlyLM014B)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014B = monthlyLM014BReposDay.saveAll(monthlyLM014B);
			monthlyLM014BReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014B = monthlyLM014BReposMon.saveAll(monthlyLM014B);
			monthlyLM014BReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014B = monthlyLM014BReposHist.saveAll(monthlyLM014B);
			monthlyLM014BReposHist.flush();
		} else {
			monthlyLM014B = monthlyLM014BRepos.saveAll(monthlyLM014B);
			monthlyLM014BRepos.flush();
		}
	}

	@Override
	public void updateAll(List<MonthlyLM014B> monthlyLM014B, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (monthlyLM014B == null || monthlyLM014B.size() == 0)
			throw new DBException(6);

		for (MonthlyLM014B t : monthlyLM014B)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014B = monthlyLM014BReposDay.saveAll(monthlyLM014B);
			monthlyLM014BReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014B = monthlyLM014BReposMon.saveAll(monthlyLM014B);
			monthlyLM014BReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014B = monthlyLM014BReposHist.saveAll(monthlyLM014B);
			monthlyLM014BReposHist.flush();
		} else {
			monthlyLM014B = monthlyLM014BRepos.saveAll(monthlyLM014B);
			monthlyLM014BRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<MonthlyLM014B> monthlyLM014B, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (monthlyLM014B == null || monthlyLM014B.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			monthlyLM014BReposDay.deleteAll(monthlyLM014B);
			monthlyLM014BReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			monthlyLM014BReposMon.deleteAll(monthlyLM014B);
			monthlyLM014BReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			monthlyLM014BReposHist.deleteAll(monthlyLM014B);
			monthlyLM014BReposHist.flush();
		} else {
			monthlyLM014BRepos.deleteAll(monthlyLM014B);
			monthlyLM014BRepos.flush();
		}
	}

}
