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
import com.st1.itx.db.domain.TxAmlRating;
import com.st1.itx.db.repository.online.TxAmlRatingRepository;
import com.st1.itx.db.repository.day.TxAmlRatingRepositoryDay;
import com.st1.itx.db.repository.mon.TxAmlRatingRepositoryMon;
import com.st1.itx.db.repository.hist.TxAmlRatingRepositoryHist;
import com.st1.itx.db.service.TxAmlRatingService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txAmlRatingService")
@Repository
public class TxAmlRatingServiceImpl implements TxAmlRatingService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(TxAmlRatingServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxAmlRatingRepository txAmlRatingRepos;

	@Autowired
	private TxAmlRatingRepositoryDay txAmlRatingReposDay;

	@Autowired
	private TxAmlRatingRepositoryMon txAmlRatingReposMon;

	@Autowired
	private TxAmlRatingRepositoryHist txAmlRatingReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txAmlRatingRepos);
		org.junit.Assert.assertNotNull(txAmlRatingReposDay);
		org.junit.Assert.assertNotNull(txAmlRatingReposMon);
		org.junit.Assert.assertNotNull(txAmlRatingReposHist);
	}

	@Override
	public TxAmlRating findById(Long logNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + logNo);
		Optional<TxAmlRating> txAmlRating = null;
		if (dbName.equals(ContentName.onDay))
			txAmlRating = txAmlRatingReposDay.findById(logNo);
		else if (dbName.equals(ContentName.onMon))
			txAmlRating = txAmlRatingReposMon.findById(logNo);
		else if (dbName.equals(ContentName.onHist))
			txAmlRating = txAmlRatingReposHist.findById(logNo);
		else
			txAmlRating = txAmlRatingRepos.findById(logNo);
		TxAmlRating obj = txAmlRating.isPresent() ? txAmlRating.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxAmlRating> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAmlRating> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txAmlRatingReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAmlRatingReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAmlRatingReposHist.findAll(pageable);
		else
			slice = txAmlRatingRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxAmlRating> findByCaseNo(String caseNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAmlRating> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		logger.info("findByCaseNo " + dbName + " : " + "caseNo_0 : " + caseNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = txAmlRatingReposDay.findAllByCaseNoIsOrderByCaseNoAsc(caseNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAmlRatingReposMon.findAllByCaseNoIsOrderByCaseNoAsc(caseNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAmlRatingReposHist.findAllByCaseNoIsOrderByCaseNoAsc(caseNo_0, pageable);
		else
			slice = txAmlRatingRepos.findAllByCaseNoIsOrderByCaseNoAsc(caseNo_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxAmlRating holdById(Long logNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + logNo);
		Optional<TxAmlRating> txAmlRating = null;
		if (dbName.equals(ContentName.onDay))
			txAmlRating = txAmlRatingReposDay.findByLogNo(logNo);
		else if (dbName.equals(ContentName.onMon))
			txAmlRating = txAmlRatingReposMon.findByLogNo(logNo);
		else if (dbName.equals(ContentName.onHist))
			txAmlRating = txAmlRatingReposHist.findByLogNo(logNo);
		else
			txAmlRating = txAmlRatingRepos.findByLogNo(logNo);
		return txAmlRating.isPresent() ? txAmlRating.get() : null;
	}

	@Override
	public TxAmlRating holdById(TxAmlRating txAmlRating, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + txAmlRating.getLogNo());
		Optional<TxAmlRating> txAmlRatingT = null;
		if (dbName.equals(ContentName.onDay))
			txAmlRatingT = txAmlRatingReposDay.findByLogNo(txAmlRating.getLogNo());
		else if (dbName.equals(ContentName.onMon))
			txAmlRatingT = txAmlRatingReposMon.findByLogNo(txAmlRating.getLogNo());
		else if (dbName.equals(ContentName.onHist))
			txAmlRatingT = txAmlRatingReposHist.findByLogNo(txAmlRating.getLogNo());
		else
			txAmlRatingT = txAmlRatingRepos.findByLogNo(txAmlRating.getLogNo());
		return txAmlRatingT.isPresent() ? txAmlRatingT.get() : null;
	}

	@Override
	public TxAmlRating insert(TxAmlRating txAmlRating, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		logger.info("Insert..." + dbName + " " + txAmlRating.getLogNo());
		if (this.findById(txAmlRating.getLogNo()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txAmlRating.setCreateEmpNo(empNot);

		if (txAmlRating.getLastUpdateEmpNo() == null || txAmlRating.getLastUpdateEmpNo().isEmpty())
			txAmlRating.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txAmlRatingReposDay.saveAndFlush(txAmlRating);
		else if (dbName.equals(ContentName.onMon))
			return txAmlRatingReposMon.saveAndFlush(txAmlRating);
		else if (dbName.equals(ContentName.onHist))
			return txAmlRatingReposHist.saveAndFlush(txAmlRating);
		else
			return txAmlRatingRepos.saveAndFlush(txAmlRating);
	}

	@Override
	public TxAmlRating update(TxAmlRating txAmlRating, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + txAmlRating.getLogNo());
		if (!empNot.isEmpty())
			txAmlRating.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txAmlRatingReposDay.saveAndFlush(txAmlRating);
		else if (dbName.equals(ContentName.onMon))
			return txAmlRatingReposMon.saveAndFlush(txAmlRating);
		else if (dbName.equals(ContentName.onHist))
			return txAmlRatingReposHist.saveAndFlush(txAmlRating);
		else
			return txAmlRatingRepos.saveAndFlush(txAmlRating);
	}

	@Override
	public TxAmlRating update2(TxAmlRating txAmlRating, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + txAmlRating.getLogNo());
		if (!empNot.isEmpty())
			txAmlRating.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txAmlRatingReposDay.saveAndFlush(txAmlRating);
		else if (dbName.equals(ContentName.onMon))
			txAmlRatingReposMon.saveAndFlush(txAmlRating);
		else if (dbName.equals(ContentName.onHist))
			txAmlRatingReposHist.saveAndFlush(txAmlRating);
		else
			txAmlRatingRepos.saveAndFlush(txAmlRating);
		return this.findById(txAmlRating.getLogNo());
	}

	@Override
	public void delete(TxAmlRating txAmlRating, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + txAmlRating.getLogNo());
		if (dbName.equals(ContentName.onDay)) {
			txAmlRatingReposDay.delete(txAmlRating);
			txAmlRatingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAmlRatingReposMon.delete(txAmlRating);
			txAmlRatingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAmlRatingReposHist.delete(txAmlRating);
			txAmlRatingReposHist.flush();
		} else {
			txAmlRatingRepos.delete(txAmlRating);
			txAmlRatingRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxAmlRating> txAmlRating, TitaVo... titaVo) throws DBException {
		if (txAmlRating == null || txAmlRating.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		logger.info("InsertAll...");
		for (TxAmlRating t : txAmlRating) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			txAmlRating = txAmlRatingReposDay.saveAll(txAmlRating);
			txAmlRatingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAmlRating = txAmlRatingReposMon.saveAll(txAmlRating);
			txAmlRatingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAmlRating = txAmlRatingReposHist.saveAll(txAmlRating);
			txAmlRatingReposHist.flush();
		} else {
			txAmlRating = txAmlRatingRepos.saveAll(txAmlRating);
			txAmlRatingRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxAmlRating> txAmlRating, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (txAmlRating == null || txAmlRating.size() == 0)
			throw new DBException(6);

		for (TxAmlRating t : txAmlRating)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txAmlRating = txAmlRatingReposDay.saveAll(txAmlRating);
			txAmlRatingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAmlRating = txAmlRatingReposMon.saveAll(txAmlRating);
			txAmlRatingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAmlRating = txAmlRatingReposHist.saveAll(txAmlRating);
			txAmlRatingReposHist.flush();
		} else {
			txAmlRating = txAmlRatingRepos.saveAll(txAmlRating);
			txAmlRatingRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxAmlRating> txAmlRating, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txAmlRating == null || txAmlRating.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txAmlRatingReposDay.deleteAll(txAmlRating);
			txAmlRatingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAmlRatingReposMon.deleteAll(txAmlRating);
			txAmlRatingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAmlRatingReposHist.deleteAll(txAmlRating);
			txAmlRatingReposHist.flush();
		} else {
			txAmlRatingRepos.deleteAll(txAmlRating);
			txAmlRatingRepos.flush();
		}
	}

}
