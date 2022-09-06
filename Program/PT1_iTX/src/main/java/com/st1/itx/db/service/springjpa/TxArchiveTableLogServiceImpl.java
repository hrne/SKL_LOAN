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
import com.st1.itx.db.domain.TxArchiveTableLog;
import com.st1.itx.db.domain.TxArchiveTableLogId;
import com.st1.itx.db.repository.online.TxArchiveTableLogRepository;
import com.st1.itx.db.repository.day.TxArchiveTableLogRepositoryDay;
import com.st1.itx.db.repository.mon.TxArchiveTableLogRepositoryMon;
import com.st1.itx.db.repository.hist.TxArchiveTableLogRepositoryHist;
import com.st1.itx.db.service.TxArchiveTableLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txArchiveTableLogService")
@Repository
public class TxArchiveTableLogServiceImpl extends ASpringJpaParm implements TxArchiveTableLogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxArchiveTableLogRepository txArchiveTableLogRepos;

	@Autowired
	private TxArchiveTableLogRepositoryDay txArchiveTableLogReposDay;

	@Autowired
	private TxArchiveTableLogRepositoryMon txArchiveTableLogReposMon;

	@Autowired
	private TxArchiveTableLogRepositoryHist txArchiveTableLogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txArchiveTableLogRepos);
		org.junit.Assert.assertNotNull(txArchiveTableLogReposDay);
		org.junit.Assert.assertNotNull(txArchiveTableLogReposMon);
		org.junit.Assert.assertNotNull(txArchiveTableLogReposHist);
	}

	@Override
	public TxArchiveTableLog findById(TxArchiveTableLogId txArchiveTableLogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + txArchiveTableLogId);
		Optional<TxArchiveTableLog> txArchiveTableLog = null;
		if (dbName.equals(ContentName.onDay))
			txArchiveTableLog = txArchiveTableLogReposDay.findById(txArchiveTableLogId);
		else if (dbName.equals(ContentName.onMon))
			txArchiveTableLog = txArchiveTableLogReposMon.findById(txArchiveTableLogId);
		else if (dbName.equals(ContentName.onHist))
			txArchiveTableLog = txArchiveTableLogReposHist.findById(txArchiveTableLogId);
		else
			txArchiveTableLog = txArchiveTableLogRepos.findById(txArchiveTableLogId);
		TxArchiveTableLog obj = txArchiveTableLog.isPresent() ? txArchiveTableLog.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxArchiveTableLog> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxArchiveTableLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Type", "DataFrom", "DataTo", "ExecuteDate", "TableName", "BatchNo", "CustNo", "FacmNo", "BormNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Type", "DataFrom", "DataTo", "ExecuteDate", "TableName", "BatchNo", "CustNo", "FacmNo", "BormNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txArchiveTableLogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txArchiveTableLogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txArchiveTableLogReposHist.findAll(pageable);
		else
			slice = txArchiveTableLogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxArchiveTableLog holdById(TxArchiveTableLogId txArchiveTableLogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txArchiveTableLogId);
		Optional<TxArchiveTableLog> txArchiveTableLog = null;
		if (dbName.equals(ContentName.onDay))
			txArchiveTableLog = txArchiveTableLogReposDay.findByTxArchiveTableLogId(txArchiveTableLogId);
		else if (dbName.equals(ContentName.onMon))
			txArchiveTableLog = txArchiveTableLogReposMon.findByTxArchiveTableLogId(txArchiveTableLogId);
		else if (dbName.equals(ContentName.onHist))
			txArchiveTableLog = txArchiveTableLogReposHist.findByTxArchiveTableLogId(txArchiveTableLogId);
		else
			txArchiveTableLog = txArchiveTableLogRepos.findByTxArchiveTableLogId(txArchiveTableLogId);
		return txArchiveTableLog.isPresent() ? txArchiveTableLog.get() : null;
	}

	@Override
	public TxArchiveTableLog holdById(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txArchiveTableLog.getTxArchiveTableLogId());
		Optional<TxArchiveTableLog> txArchiveTableLogT = null;
		if (dbName.equals(ContentName.onDay))
			txArchiveTableLogT = txArchiveTableLogReposDay.findByTxArchiveTableLogId(txArchiveTableLog.getTxArchiveTableLogId());
		else if (dbName.equals(ContentName.onMon))
			txArchiveTableLogT = txArchiveTableLogReposMon.findByTxArchiveTableLogId(txArchiveTableLog.getTxArchiveTableLogId());
		else if (dbName.equals(ContentName.onHist))
			txArchiveTableLogT = txArchiveTableLogReposHist.findByTxArchiveTableLogId(txArchiveTableLog.getTxArchiveTableLogId());
		else
			txArchiveTableLogT = txArchiveTableLogRepos.findByTxArchiveTableLogId(txArchiveTableLog.getTxArchiveTableLogId());
		return txArchiveTableLogT.isPresent() ? txArchiveTableLogT.get() : null;
	}

	@Override
	public TxArchiveTableLog insert(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + txArchiveTableLog.getTxArchiveTableLogId());
		if (this.findById(txArchiveTableLog.getTxArchiveTableLogId(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txArchiveTableLog.setCreateEmpNo(empNot);

		if (txArchiveTableLog.getLastUpdateEmpNo() == null || txArchiveTableLog.getLastUpdateEmpNo().isEmpty())
			txArchiveTableLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txArchiveTableLogReposDay.saveAndFlush(txArchiveTableLog);
		else if (dbName.equals(ContentName.onMon))
			return txArchiveTableLogReposMon.saveAndFlush(txArchiveTableLog);
		else if (dbName.equals(ContentName.onHist))
			return txArchiveTableLogReposHist.saveAndFlush(txArchiveTableLog);
		else
			return txArchiveTableLogRepos.saveAndFlush(txArchiveTableLog);
	}

	@Override
	public TxArchiveTableLog update(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txArchiveTableLog.getTxArchiveTableLogId());
		if (!empNot.isEmpty())
			txArchiveTableLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txArchiveTableLogReposDay.saveAndFlush(txArchiveTableLog);
		else if (dbName.equals(ContentName.onMon))
			return txArchiveTableLogReposMon.saveAndFlush(txArchiveTableLog);
		else if (dbName.equals(ContentName.onHist))
			return txArchiveTableLogReposHist.saveAndFlush(txArchiveTableLog);
		else
			return txArchiveTableLogRepos.saveAndFlush(txArchiveTableLog);
	}

	@Override
	public TxArchiveTableLog update2(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txArchiveTableLog.getTxArchiveTableLogId());
		if (!empNot.isEmpty())
			txArchiveTableLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txArchiveTableLogReposDay.saveAndFlush(txArchiveTableLog);
		else if (dbName.equals(ContentName.onMon))
			txArchiveTableLogReposMon.saveAndFlush(txArchiveTableLog);
		else if (dbName.equals(ContentName.onHist))
			txArchiveTableLogReposHist.saveAndFlush(txArchiveTableLog);
		else
			txArchiveTableLogRepos.saveAndFlush(txArchiveTableLog);
		return this.findById(txArchiveTableLog.getTxArchiveTableLogId());
	}

	@Override
	public void delete(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txArchiveTableLog.getTxArchiveTableLogId());
		if (dbName.equals(ContentName.onDay)) {
			txArchiveTableLogReposDay.delete(txArchiveTableLog);
			txArchiveTableLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txArchiveTableLogReposMon.delete(txArchiveTableLog);
			txArchiveTableLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txArchiveTableLogReposHist.delete(txArchiveTableLog);
			txArchiveTableLogReposHist.flush();
		} else {
			txArchiveTableLogRepos.delete(txArchiveTableLog);
			txArchiveTableLogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException {
		if (txArchiveTableLog == null || txArchiveTableLog.size() == 0)
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
		for (TxArchiveTableLog t : txArchiveTableLog) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			txArchiveTableLog = txArchiveTableLogReposDay.saveAll(txArchiveTableLog);
			txArchiveTableLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txArchiveTableLog = txArchiveTableLogReposMon.saveAll(txArchiveTableLog);
			txArchiveTableLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txArchiveTableLog = txArchiveTableLogReposHist.saveAll(txArchiveTableLog);
			txArchiveTableLogReposHist.flush();
		} else {
			txArchiveTableLog = txArchiveTableLogRepos.saveAll(txArchiveTableLog);
			txArchiveTableLogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (txArchiveTableLog == null || txArchiveTableLog.size() == 0)
			throw new DBException(6);

		for (TxArchiveTableLog t : txArchiveTableLog)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txArchiveTableLog = txArchiveTableLogReposDay.saveAll(txArchiveTableLog);
			txArchiveTableLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txArchiveTableLog = txArchiveTableLogReposMon.saveAll(txArchiveTableLog);
			txArchiveTableLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txArchiveTableLog = txArchiveTableLogReposHist.saveAll(txArchiveTableLog);
			txArchiveTableLogReposHist.flush();
		} else {
			txArchiveTableLog = txArchiveTableLogRepos.saveAll(txArchiveTableLog);
			txArchiveTableLogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txArchiveTableLog == null || txArchiveTableLog.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txArchiveTableLogReposDay.deleteAll(txArchiveTableLog);
			txArchiveTableLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txArchiveTableLogReposMon.deleteAll(txArchiveTableLog);
			txArchiveTableLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txArchiveTableLogReposHist.deleteAll(txArchiveTableLog);
			txArchiveTableLogReposHist.flush();
		} else {
			txArchiveTableLogRepos.deleteAll(txArchiveTableLog);
			txArchiveTableLogRepos.flush();
		}
	}

}
