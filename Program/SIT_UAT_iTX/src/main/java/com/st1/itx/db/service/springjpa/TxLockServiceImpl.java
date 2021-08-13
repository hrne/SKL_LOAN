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
import com.st1.itx.db.domain.TxLock;
import com.st1.itx.db.repository.online.TxLockRepository;
import com.st1.itx.db.repository.day.TxLockRepositoryDay;
import com.st1.itx.db.repository.mon.TxLockRepositoryMon;
import com.st1.itx.db.repository.hist.TxLockRepositoryHist;
import com.st1.itx.db.service.TxLockService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txLockService")
@Repository
public class TxLockServiceImpl implements TxLockService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(TxLockServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxLockRepository txLockRepos;

	@Autowired
	private TxLockRepositoryDay txLockReposDay;

	@Autowired
	private TxLockRepositoryMon txLockReposMon;

	@Autowired
	private TxLockRepositoryHist txLockReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txLockRepos);
		org.junit.Assert.assertNotNull(txLockReposDay);
		org.junit.Assert.assertNotNull(txLockReposMon);
		org.junit.Assert.assertNotNull(txLockReposHist);
	}

	@Override
	public TxLock findById(Long lockNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + lockNo);
		Optional<TxLock> txLock = null;
		if (dbName.equals(ContentName.onDay))
			txLock = txLockReposDay.findById(lockNo);
		else if (dbName.equals(ContentName.onMon))
			txLock = txLockReposMon.findById(lockNo);
		else if (dbName.equals(ContentName.onHist))
			txLock = txLockReposHist.findById(lockNo);
		else
			txLock = txLockRepos.findById(lockNo);
		TxLock obj = txLock.isPresent() ? txLock.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxLock> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxLock> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LockNo"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txLockReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txLockReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txLockReposHist.findAll(pageable);
		else
			slice = txLockRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxLock> findByCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxLock> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findByCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = txLockReposDay.findAllByCustNoIsOrderByLockNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txLockReposMon.findAllByCustNoIsOrderByLockNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txLockReposHist.findAllByCustNoIsOrderByLockNoAsc(custNo_0, pageable);
		else
			slice = txLockRepos.findAllByCustNoIsOrderByLockNoAsc(custNo_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxLock holdById(Long lockNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + lockNo);
		Optional<TxLock> txLock = null;
		if (dbName.equals(ContentName.onDay))
			txLock = txLockReposDay.findByLockNo(lockNo);
		else if (dbName.equals(ContentName.onMon))
			txLock = txLockReposMon.findByLockNo(lockNo);
		else if (dbName.equals(ContentName.onHist))
			txLock = txLockReposHist.findByLockNo(lockNo);
		else
			txLock = txLockRepos.findByLockNo(lockNo);
		return txLock.isPresent() ? txLock.get() : null;
	}

	@Override
	public TxLock holdById(TxLock txLock, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + txLock.getLockNo());
		Optional<TxLock> txLockT = null;
		if (dbName.equals(ContentName.onDay))
			txLockT = txLockReposDay.findByLockNo(txLock.getLockNo());
		else if (dbName.equals(ContentName.onMon))
			txLockT = txLockReposMon.findByLockNo(txLock.getLockNo());
		else if (dbName.equals(ContentName.onHist))
			txLockT = txLockReposHist.findByLockNo(txLock.getLockNo());
		else
			txLockT = txLockRepos.findByLockNo(txLock.getLockNo());
		return txLockT.isPresent() ? txLockT.get() : null;
	}

	@Override
	public TxLock insert(TxLock txLock, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + txLock.getLockNo());
		if (this.findById(txLock.getLockNo()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txLock.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txLockReposDay.saveAndFlush(txLock);
		else if (dbName.equals(ContentName.onMon))
			return txLockReposMon.saveAndFlush(txLock);
		else if (dbName.equals(ContentName.onHist))
			return txLockReposHist.saveAndFlush(txLock);
		else
			return txLockRepos.saveAndFlush(txLock);
	}

	@Override
	public TxLock update(TxLock txLock, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + txLock.getLockNo());
		if (!empNot.isEmpty())
			txLock.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txLockReposDay.saveAndFlush(txLock);
		else if (dbName.equals(ContentName.onMon))
			return txLockReposMon.saveAndFlush(txLock);
		else if (dbName.equals(ContentName.onHist))
			return txLockReposHist.saveAndFlush(txLock);
		else
			return txLockRepos.saveAndFlush(txLock);
	}

	@Override
	public TxLock update2(TxLock txLock, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + txLock.getLockNo());
		if (!empNot.isEmpty())
			txLock.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txLockReposDay.saveAndFlush(txLock);
		else if (dbName.equals(ContentName.onMon))
			txLockReposMon.saveAndFlush(txLock);
		else if (dbName.equals(ContentName.onHist))
			txLockReposHist.saveAndFlush(txLock);
		else
			txLockRepos.saveAndFlush(txLock);
		return this.findById(txLock.getLockNo());
	}

	@Override
	public void delete(TxLock txLock, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + txLock.getLockNo());
		if (dbName.equals(ContentName.onDay)) {
			txLockReposDay.delete(txLock);
			txLockReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txLockReposMon.delete(txLock);
			txLockReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txLockReposHist.delete(txLock);
			txLockReposHist.flush();
		} else {
			txLockRepos.delete(txLock);
			txLockRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxLock> txLock, TitaVo... titaVo) throws DBException {
		if (txLock == null || txLock.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (TxLock t : txLock)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txLock = txLockReposDay.saveAll(txLock);
			txLockReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txLock = txLockReposMon.saveAll(txLock);
			txLockReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txLock = txLockReposHist.saveAll(txLock);
			txLockReposHist.flush();
		} else {
			txLock = txLockRepos.saveAll(txLock);
			txLockRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxLock> txLock, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (txLock == null || txLock.size() == 0)
			throw new DBException(6);

		for (TxLock t : txLock)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txLock = txLockReposDay.saveAll(txLock);
			txLockReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txLock = txLockReposMon.saveAll(txLock);
			txLockReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txLock = txLockReposHist.saveAll(txLock);
			txLockReposHist.flush();
		} else {
			txLock = txLockRepos.saveAll(txLock);
			txLockRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxLock> txLock, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txLock == null || txLock.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txLockReposDay.deleteAll(txLock);
			txLockReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txLockReposMon.deleteAll(txLock);
			txLockReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txLockReposHist.deleteAll(txLock);
			txLockReposHist.flush();
		} else {
			txLockRepos.deleteAll(txLock);
			txLockRepos.flush();
		}
	}

}
