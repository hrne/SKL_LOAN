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
import com.st1.itx.db.domain.TxAmlLog;
import com.st1.itx.db.repository.online.TxAmlLogRepository;
import com.st1.itx.db.repository.day.TxAmlLogRepositoryDay;
import com.st1.itx.db.repository.mon.TxAmlLogRepositoryMon;
import com.st1.itx.db.repository.hist.TxAmlLogRepositoryHist;
import com.st1.itx.db.service.TxAmlLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txAmlLogService")
@Repository
public class TxAmlLogServiceImpl extends ASpringJpaParm implements TxAmlLogService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxAmlLogRepository txAmlLogRepos;

	@Autowired
	private TxAmlLogRepositoryDay txAmlLogReposDay;

	@Autowired
	private TxAmlLogRepositoryMon txAmlLogReposMon;

	@Autowired
	private TxAmlLogRepositoryHist txAmlLogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txAmlLogRepos);
		org.junit.Assert.assertNotNull(txAmlLogReposDay);
		org.junit.Assert.assertNotNull(txAmlLogReposMon);
		org.junit.Assert.assertNotNull(txAmlLogReposHist);
	}

	@Override
	public TxAmlLog findById(Long logNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + logNo);
		Optional<TxAmlLog> txAmlLog = null;
		if (dbName.equals(ContentName.onDay))
			txAmlLog = txAmlLogReposDay.findById(logNo);
		else if (dbName.equals(ContentName.onMon))
			txAmlLog = txAmlLogReposMon.findById(logNo);
		else if (dbName.equals(ContentName.onHist))
			txAmlLog = txAmlLogReposHist.findById(logNo);
		else
			txAmlLog = txAmlLogRepos.findById(logNo);
		TxAmlLog obj = txAmlLog.isPresent() ? txAmlLog.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxAmlLog> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAmlLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txAmlLogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAmlLogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAmlLogReposHist.findAll(pageable);
		else
			slice = txAmlLogRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxAmlLog> findByConfirmStatus(String brNo_0, String confirmStatus_1, int entdy_2, int entdy_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAmlLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByConfirmStatus " + dbName + " : " + "brNo_0 : " + brNo_0 + " confirmStatus_1 : " + confirmStatus_1 + " entdy_2 : " + entdy_2 + " entdy_3 : " + entdy_3);
		if (dbName.equals(ContentName.onDay))
			slice = txAmlLogReposDay.findAllByBrNoIsAndConfirmStatusIsAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(brNo_0, confirmStatus_1, entdy_2, entdy_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAmlLogReposMon.findAllByBrNoIsAndConfirmStatusIsAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(brNo_0, confirmStatus_1, entdy_2, entdy_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAmlLogReposHist.findAllByBrNoIsAndConfirmStatusIsAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(brNo_0, confirmStatus_1, entdy_2, entdy_3, pageable);
		else
			slice = txAmlLogRepos.findAllByBrNoIsAndConfirmStatusIsAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(brNo_0, confirmStatus_1, entdy_2, entdy_3, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxAmlLog> findByBrNo(String brNo_0, int entdy_1, int entdy_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxAmlLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByBrNo " + dbName + " : " + "brNo_0 : " + brNo_0 + " entdy_1 : " + entdy_1 + " entdy_2 : " + entdy_2);
		if (dbName.equals(ContentName.onDay))
			slice = txAmlLogReposDay.findAllByBrNoIsAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(brNo_0, entdy_1, entdy_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txAmlLogReposMon.findAllByBrNoIsAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(brNo_0, entdy_1, entdy_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txAmlLogReposHist.findAllByBrNoIsAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(brNo_0, entdy_1, entdy_2, pageable);
		else
			slice = txAmlLogRepos.findAllByBrNoIsAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(brNo_0, entdy_1, entdy_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxAmlLog findByTransactionIdFirst(int entdy_0, String transactionId_1, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findByTransactionIdFirst " + dbName + " : " + "entdy_0 : " + entdy_0 + " transactionId_1 : " + transactionId_1);
		Optional<TxAmlLog> txAmlLogT = null;
		if (dbName.equals(ContentName.onDay))
			txAmlLogT = txAmlLogReposDay.findTopByEntdyIsAndTransactionIdIsOrderByLogNoAsc(entdy_0, transactionId_1);
		else if (dbName.equals(ContentName.onMon))
			txAmlLogT = txAmlLogReposMon.findTopByEntdyIsAndTransactionIdIsOrderByLogNoAsc(entdy_0, transactionId_1);
		else if (dbName.equals(ContentName.onHist))
			txAmlLogT = txAmlLogReposHist.findTopByEntdyIsAndTransactionIdIsOrderByLogNoAsc(entdy_0, transactionId_1);
		else
			txAmlLogT = txAmlLogRepos.findTopByEntdyIsAndTransactionIdIsOrderByLogNoAsc(entdy_0, transactionId_1);
		return txAmlLogT.isPresent() ? txAmlLogT.get() : null;
	}

	@Override
	public TxAmlLog holdById(Long logNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + logNo);
		Optional<TxAmlLog> txAmlLog = null;
		if (dbName.equals(ContentName.onDay))
			txAmlLog = txAmlLogReposDay.findByLogNo(logNo);
		else if (dbName.equals(ContentName.onMon))
			txAmlLog = txAmlLogReposMon.findByLogNo(logNo);
		else if (dbName.equals(ContentName.onHist))
			txAmlLog = txAmlLogReposHist.findByLogNo(logNo);
		else
			txAmlLog = txAmlLogRepos.findByLogNo(logNo);
		return txAmlLog.isPresent() ? txAmlLog.get() : null;
	}

	@Override
	public TxAmlLog holdById(TxAmlLog txAmlLog, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txAmlLog.getLogNo());
		Optional<TxAmlLog> txAmlLogT = null;
		if (dbName.equals(ContentName.onDay))
			txAmlLogT = txAmlLogReposDay.findByLogNo(txAmlLog.getLogNo());
		else if (dbName.equals(ContentName.onMon))
			txAmlLogT = txAmlLogReposMon.findByLogNo(txAmlLog.getLogNo());
		else if (dbName.equals(ContentName.onHist))
			txAmlLogT = txAmlLogReposHist.findByLogNo(txAmlLog.getLogNo());
		else
			txAmlLogT = txAmlLogRepos.findByLogNo(txAmlLog.getLogNo());
		return txAmlLogT.isPresent() ? txAmlLogT.get() : null;
	}

	@Override
	public TxAmlLog insert(TxAmlLog txAmlLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + txAmlLog.getLogNo());
		if (this.findById(txAmlLog.getLogNo()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txAmlLog.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txAmlLogReposDay.saveAndFlush(txAmlLog);
		else if (dbName.equals(ContentName.onMon))
			return txAmlLogReposMon.saveAndFlush(txAmlLog);
		else if (dbName.equals(ContentName.onHist))
			return txAmlLogReposHist.saveAndFlush(txAmlLog);
		else
			return txAmlLogRepos.saveAndFlush(txAmlLog);
	}

	@Override
	public TxAmlLog update(TxAmlLog txAmlLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txAmlLog.getLogNo());
		if (!empNot.isEmpty())
			txAmlLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txAmlLogReposDay.saveAndFlush(txAmlLog);
		else if (dbName.equals(ContentName.onMon))
			return txAmlLogReposMon.saveAndFlush(txAmlLog);
		else if (dbName.equals(ContentName.onHist))
			return txAmlLogReposHist.saveAndFlush(txAmlLog);
		else
			return txAmlLogRepos.saveAndFlush(txAmlLog);
	}

	@Override
	public TxAmlLog update2(TxAmlLog txAmlLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txAmlLog.getLogNo());
		if (!empNot.isEmpty())
			txAmlLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txAmlLogReposDay.saveAndFlush(txAmlLog);
		else if (dbName.equals(ContentName.onMon))
			txAmlLogReposMon.saveAndFlush(txAmlLog);
		else if (dbName.equals(ContentName.onHist))
			txAmlLogReposHist.saveAndFlush(txAmlLog);
		else
			txAmlLogRepos.saveAndFlush(txAmlLog);
		return this.findById(txAmlLog.getLogNo());
	}

	@Override
	public void delete(TxAmlLog txAmlLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txAmlLog.getLogNo());
		if (dbName.equals(ContentName.onDay)) {
			txAmlLogReposDay.delete(txAmlLog);
			txAmlLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAmlLogReposMon.delete(txAmlLog);
			txAmlLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAmlLogReposHist.delete(txAmlLog);
			txAmlLogReposHist.flush();
		} else {
			txAmlLogRepos.delete(txAmlLog);
			txAmlLogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxAmlLog> txAmlLog, TitaVo... titaVo) throws DBException {
		if (txAmlLog == null || txAmlLog.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (TxAmlLog t : txAmlLog)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txAmlLog = txAmlLogReposDay.saveAll(txAmlLog);
			txAmlLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAmlLog = txAmlLogReposMon.saveAll(txAmlLog);
			txAmlLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAmlLog = txAmlLogReposHist.saveAll(txAmlLog);
			txAmlLogReposHist.flush();
		} else {
			txAmlLog = txAmlLogRepos.saveAll(txAmlLog);
			txAmlLogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxAmlLog> txAmlLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (txAmlLog == null || txAmlLog.size() == 0)
			throw new DBException(6);

		for (TxAmlLog t : txAmlLog)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txAmlLog = txAmlLogReposDay.saveAll(txAmlLog);
			txAmlLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAmlLog = txAmlLogReposMon.saveAll(txAmlLog);
			txAmlLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAmlLog = txAmlLogReposHist.saveAll(txAmlLog);
			txAmlLogReposHist.flush();
		} else {
			txAmlLog = txAmlLogRepos.saveAll(txAmlLog);
			txAmlLogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxAmlLog> txAmlLog, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txAmlLog == null || txAmlLog.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txAmlLogReposDay.deleteAll(txAmlLog);
			txAmlLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txAmlLogReposMon.deleteAll(txAmlLog);
			txAmlLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txAmlLogReposHist.deleteAll(txAmlLog);
			txAmlLogReposHist.flush();
		} else {
			txAmlLogRepos.deleteAll(txAmlLog);
			txAmlLogRepos.flush();
		}
	}

}
