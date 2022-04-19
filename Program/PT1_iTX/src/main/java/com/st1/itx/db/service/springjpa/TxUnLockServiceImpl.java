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
import com.st1.itx.db.domain.TxUnLock;
import com.st1.itx.db.repository.online.TxUnLockRepository;
import com.st1.itx.db.repository.day.TxUnLockRepositoryDay;
import com.st1.itx.db.repository.mon.TxUnLockRepositoryMon;
import com.st1.itx.db.repository.hist.TxUnLockRepositoryHist;
import com.st1.itx.db.service.TxUnLockService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txUnLockService")
@Repository
public class TxUnLockServiceImpl extends ASpringJpaParm implements TxUnLockService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxUnLockRepository txUnLockRepos;

	@Autowired
	private TxUnLockRepositoryDay txUnLockReposDay;

	@Autowired
	private TxUnLockRepositoryMon txUnLockReposMon;

	@Autowired
	private TxUnLockRepositoryHist txUnLockReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txUnLockRepos);
		org.junit.Assert.assertNotNull(txUnLockReposDay);
		org.junit.Assert.assertNotNull(txUnLockReposMon);
		org.junit.Assert.assertNotNull(txUnLockReposHist);
	}

	@Override
	public TxUnLock findById(Long lockNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + lockNo);
		Optional<TxUnLock> txUnLock = null;
		if (dbName.equals(ContentName.onDay))
			txUnLock = txUnLockReposDay.findById(lockNo);
		else if (dbName.equals(ContentName.onMon))
			txUnLock = txUnLockReposMon.findById(lockNo);
		else if (dbName.equals(ContentName.onHist))
			txUnLock = txUnLockReposHist.findById(lockNo);
		else
			txUnLock = txUnLockRepos.findById(lockNo);
		TxUnLock obj = txUnLock.isPresent() ? txUnLock.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxUnLock> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxUnLock> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LockNo"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txUnLockReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txUnLockReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txUnLockReposHist.findAll(pageable);
		else
			slice = txUnLockRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxUnLock> findByCustNo(int entdy_0, int custNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxUnLock> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findByCustNo " + dbName + " : " + "entdy_0 : " + entdy_0 + " custNo_1 : " + custNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = txUnLockReposDay.findAllByEntdyIsAndCustNoIsOrderByCreateDateAsc(entdy_0, custNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txUnLockReposMon.findAllByEntdyIsAndCustNoIsOrderByCreateDateAsc(entdy_0, custNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txUnLockReposHist.findAllByEntdyIsAndCustNoIsOrderByCreateDateAsc(entdy_0, custNo_1, pageable);
		else
			slice = txUnLockRepos.findAllByEntdyIsAndCustNoIsOrderByCreateDateAsc(entdy_0, custNo_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxUnLock holdById(Long lockNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + lockNo);
		Optional<TxUnLock> txUnLock = null;
		if (dbName.equals(ContentName.onDay))
			txUnLock = txUnLockReposDay.findByLockNo(lockNo);
		else if (dbName.equals(ContentName.onMon))
			txUnLock = txUnLockReposMon.findByLockNo(lockNo);
		else if (dbName.equals(ContentName.onHist))
			txUnLock = txUnLockReposHist.findByLockNo(lockNo);
		else
			txUnLock = txUnLockRepos.findByLockNo(lockNo);
		return txUnLock.isPresent() ? txUnLock.get() : null;
	}

	@Override
	public TxUnLock holdById(TxUnLock txUnLock, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txUnLock.getLockNo());
		Optional<TxUnLock> txUnLockT = null;
		if (dbName.equals(ContentName.onDay))
			txUnLockT = txUnLockReposDay.findByLockNo(txUnLock.getLockNo());
		else if (dbName.equals(ContentName.onMon))
			txUnLockT = txUnLockReposMon.findByLockNo(txUnLock.getLockNo());
		else if (dbName.equals(ContentName.onHist))
			txUnLockT = txUnLockReposHist.findByLockNo(txUnLock.getLockNo());
		else
			txUnLockT = txUnLockRepos.findByLockNo(txUnLock.getLockNo());
		return txUnLockT.isPresent() ? txUnLockT.get() : null;
	}

	@Override
	public TxUnLock insert(TxUnLock txUnLock, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + txUnLock.getLockNo());
		if (this.findById(txUnLock.getLockNo()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txUnLock.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txUnLockReposDay.saveAndFlush(txUnLock);
		else if (dbName.equals(ContentName.onMon))
			return txUnLockReposMon.saveAndFlush(txUnLock);
		else if (dbName.equals(ContentName.onHist))
			return txUnLockReposHist.saveAndFlush(txUnLock);
		else
			return txUnLockRepos.saveAndFlush(txUnLock);
	}

	@Override
	public TxUnLock update(TxUnLock txUnLock, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txUnLock.getLockNo());
		if (!empNot.isEmpty())
			txUnLock.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txUnLockReposDay.saveAndFlush(txUnLock);
		else if (dbName.equals(ContentName.onMon))
			return txUnLockReposMon.saveAndFlush(txUnLock);
		else if (dbName.equals(ContentName.onHist))
			return txUnLockReposHist.saveAndFlush(txUnLock);
		else
			return txUnLockRepos.saveAndFlush(txUnLock);
	}

	@Override
	public TxUnLock update2(TxUnLock txUnLock, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txUnLock.getLockNo());
		if (!empNot.isEmpty())
			txUnLock.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txUnLockReposDay.saveAndFlush(txUnLock);
		else if (dbName.equals(ContentName.onMon))
			txUnLockReposMon.saveAndFlush(txUnLock);
		else if (dbName.equals(ContentName.onHist))
			txUnLockReposHist.saveAndFlush(txUnLock);
		else
			txUnLockRepos.saveAndFlush(txUnLock);
		return this.findById(txUnLock.getLockNo());
	}

	@Override
	public void delete(TxUnLock txUnLock, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txUnLock.getLockNo());
		if (dbName.equals(ContentName.onDay)) {
			txUnLockReposDay.delete(txUnLock);
			txUnLockReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txUnLockReposMon.delete(txUnLock);
			txUnLockReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txUnLockReposHist.delete(txUnLock);
			txUnLockReposHist.flush();
		} else {
			txUnLockRepos.delete(txUnLock);
			txUnLockRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxUnLock> txUnLock, TitaVo... titaVo) throws DBException {
		if (txUnLock == null || txUnLock.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (TxUnLock t : txUnLock)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txUnLock = txUnLockReposDay.saveAll(txUnLock);
			txUnLockReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txUnLock = txUnLockReposMon.saveAll(txUnLock);
			txUnLockReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txUnLock = txUnLockReposHist.saveAll(txUnLock);
			txUnLockReposHist.flush();
		} else {
			txUnLock = txUnLockRepos.saveAll(txUnLock);
			txUnLockRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxUnLock> txUnLock, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (txUnLock == null || txUnLock.size() == 0)
			throw new DBException(6);

		for (TxUnLock t : txUnLock)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txUnLock = txUnLockReposDay.saveAll(txUnLock);
			txUnLockReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txUnLock = txUnLockReposMon.saveAll(txUnLock);
			txUnLockReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txUnLock = txUnLockReposHist.saveAll(txUnLock);
			txUnLockReposHist.flush();
		} else {
			txUnLock = txUnLockRepos.saveAll(txUnLock);
			txUnLockRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxUnLock> txUnLock, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txUnLock == null || txUnLock.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txUnLockReposDay.deleteAll(txUnLock);
			txUnLockReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txUnLockReposMon.deleteAll(txUnLock);
			txUnLockReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txUnLockReposHist.deleteAll(txUnLock);
			txUnLockReposHist.flush();
		} else {
			txUnLockRepos.deleteAll(txUnLock);
			txUnLockRepos.flush();
		}
	}

}
