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
import com.st1.itx.db.domain.TxApLog;
import com.st1.itx.db.repository.online.TxApLogRepository;
import com.st1.itx.db.repository.day.TxApLogRepositoryDay;
import com.st1.itx.db.repository.mon.TxApLogRepositoryMon;
import com.st1.itx.db.repository.hist.TxApLogRepositoryHist;
import com.st1.itx.db.service.TxApLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txApLogService")
@Repository
public class TxApLogServiceImpl implements TxApLogService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(TxApLogServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxApLogRepository txApLogRepos;

	@Autowired
	private TxApLogRepositoryDay txApLogReposDay;

	@Autowired
	private TxApLogRepositoryMon txApLogReposMon;

	@Autowired
	private TxApLogRepositoryHist txApLogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txApLogRepos);
		org.junit.Assert.assertNotNull(txApLogReposDay);
		org.junit.Assert.assertNotNull(txApLogReposMon);
		org.junit.Assert.assertNotNull(txApLogReposHist);
	}

	@Override
	public TxApLog findById(Long autoSeq, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + autoSeq);
		Optional<TxApLog> txApLog = null;
		if (dbName.equals(ContentName.onDay))
			txApLog = txApLogReposDay.findById(autoSeq);
		else if (dbName.equals(ContentName.onMon))
			txApLog = txApLogReposMon.findById(autoSeq);
		else if (dbName.equals(ContentName.onHist))
			txApLog = txApLogReposHist.findById(autoSeq);
		else
			txApLog = txApLogRepos.findById(autoSeq);
		TxApLog obj = txApLog.isPresent() ? txApLog.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxApLog> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxApLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AutoSeq"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txApLogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txApLogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txApLogReposHist.findAll(pageable);
		else
			slice = txApLogRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxApLog> findTlrNo(String tlrNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxApLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findTlrNo " + dbName + " : " + "tlrNo_0 : " + tlrNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = txApLogReposDay.findAllByTlrNoIsOrderByTlrNoAsc(tlrNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txApLogReposMon.findAllByTlrNoIsOrderByTlrNoAsc(tlrNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txApLogReposHist.findAllByTlrNoIsOrderByTlrNoAsc(tlrNo_0, pageable);
		else
			slice = txApLogRepos.findAllByTlrNoIsOrderByTlrNoAsc(tlrNo_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxApLog> findEntdy(int entdy_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxApLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findEntdy " + dbName + " : " + "entdy_0 : " + entdy_0);
		if (dbName.equals(ContentName.onDay))
			slice = txApLogReposDay.findAllByEntdyIsOrderByEntdyAsc(entdy_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txApLogReposMon.findAllByEntdyIsOrderByEntdyAsc(entdy_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txApLogReposHist.findAllByEntdyIsOrderByEntdyAsc(entdy_0, pageable);
		else
			slice = txApLogRepos.findAllByEntdyIsOrderByEntdyAsc(entdy_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxApLog> findEntdyAndTlrno(int entdy_0, String tlrNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxApLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findEntdyAndTlrno " + dbName + " : " + "entdy_0 : " + entdy_0 + " tlrNo_1 : " + tlrNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = txApLogReposDay.findAllByEntdyIsAndTlrNoIsOrderByEntdyAscTlrNoAsc(entdy_0, tlrNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txApLogReposMon.findAllByEntdyIsAndTlrNoIsOrderByEntdyAscTlrNoAsc(entdy_0, tlrNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txApLogReposHist.findAllByEntdyIsAndTlrNoIsOrderByEntdyAscTlrNoAsc(entdy_0, tlrNo_1, pageable);
		else
			slice = txApLogRepos.findAllByEntdyIsAndTlrNoIsOrderByEntdyAscTlrNoAsc(entdy_0, tlrNo_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxApLog holdById(Long autoSeq, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + autoSeq);
		Optional<TxApLog> txApLog = null;
		if (dbName.equals(ContentName.onDay))
			txApLog = txApLogReposDay.findByAutoSeq(autoSeq);
		else if (dbName.equals(ContentName.onMon))
			txApLog = txApLogReposMon.findByAutoSeq(autoSeq);
		else if (dbName.equals(ContentName.onHist))
			txApLog = txApLogReposHist.findByAutoSeq(autoSeq);
		else
			txApLog = txApLogRepos.findByAutoSeq(autoSeq);
		return txApLog.isPresent() ? txApLog.get() : null;
	}

	@Override
	public TxApLog holdById(TxApLog txApLog, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + txApLog.getAutoSeq());
		Optional<TxApLog> txApLogT = null;
		if (dbName.equals(ContentName.onDay))
			txApLogT = txApLogReposDay.findByAutoSeq(txApLog.getAutoSeq());
		else if (dbName.equals(ContentName.onMon))
			txApLogT = txApLogReposMon.findByAutoSeq(txApLog.getAutoSeq());
		else if (dbName.equals(ContentName.onHist))
			txApLogT = txApLogReposHist.findByAutoSeq(txApLog.getAutoSeq());
		else
			txApLogT = txApLogRepos.findByAutoSeq(txApLog.getAutoSeq());
		return txApLogT.isPresent() ? txApLogT.get() : null;
	}

	@Override
	public TxApLog insert(TxApLog txApLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + txApLog.getAutoSeq());
		if (this.findById(txApLog.getAutoSeq()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txApLog.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txApLogReposDay.saveAndFlush(txApLog);
		else if (dbName.equals(ContentName.onMon))
			return txApLogReposMon.saveAndFlush(txApLog);
		else if (dbName.equals(ContentName.onHist))
			return txApLogReposHist.saveAndFlush(txApLog);
		else
			return txApLogRepos.saveAndFlush(txApLog);
	}

	@Override
	public TxApLog update(TxApLog txApLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + txApLog.getAutoSeq());
		if (!empNot.isEmpty())
			txApLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txApLogReposDay.saveAndFlush(txApLog);
		else if (dbName.equals(ContentName.onMon))
			return txApLogReposMon.saveAndFlush(txApLog);
		else if (dbName.equals(ContentName.onHist))
			return txApLogReposHist.saveAndFlush(txApLog);
		else
			return txApLogRepos.saveAndFlush(txApLog);
	}

	@Override
	public TxApLog update2(TxApLog txApLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + txApLog.getAutoSeq());
		if (!empNot.isEmpty())
			txApLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txApLogReposDay.saveAndFlush(txApLog);
		else if (dbName.equals(ContentName.onMon))
			txApLogReposMon.saveAndFlush(txApLog);
		else if (dbName.equals(ContentName.onHist))
			txApLogReposHist.saveAndFlush(txApLog);
		else
			txApLogRepos.saveAndFlush(txApLog);
		return this.findById(txApLog.getAutoSeq());
	}

	@Override
	public void delete(TxApLog txApLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + txApLog.getAutoSeq());
		if (dbName.equals(ContentName.onDay)) {
			txApLogReposDay.delete(txApLog);
			txApLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txApLogReposMon.delete(txApLog);
			txApLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txApLogReposHist.delete(txApLog);
			txApLogReposHist.flush();
		} else {
			txApLogRepos.delete(txApLog);
			txApLogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxApLog> txApLog, TitaVo... titaVo) throws DBException {
		if (txApLog == null || txApLog.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (TxApLog t : txApLog)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txApLog = txApLogReposDay.saveAll(txApLog);
			txApLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txApLog = txApLogReposMon.saveAll(txApLog);
			txApLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txApLog = txApLogReposHist.saveAll(txApLog);
			txApLogReposHist.flush();
		} else {
			txApLog = txApLogRepos.saveAll(txApLog);
			txApLogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxApLog> txApLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (txApLog == null || txApLog.size() == 0)
			throw new DBException(6);

		for (TxApLog t : txApLog)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txApLog = txApLogReposDay.saveAll(txApLog);
			txApLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txApLog = txApLogReposMon.saveAll(txApLog);
			txApLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txApLog = txApLogReposHist.saveAll(txApLog);
			txApLogReposHist.flush();
		} else {
			txApLog = txApLogRepos.saveAll(txApLog);
			txApLogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxApLog> txApLog, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txApLog == null || txApLog.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txApLogReposDay.deleteAll(txApLog);
			txApLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txApLogReposMon.deleteAll(txApLog);
			txApLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txApLogReposHist.deleteAll(txApLog);
			txApLogReposHist.flush();
		} else {
			txApLogRepos.deleteAll(txApLog);
			txApLogRepos.flush();
		}
	}

}
