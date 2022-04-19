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
import com.st1.itx.db.domain.TxTellerAuth;
import com.st1.itx.db.domain.TxTellerAuthId;
import com.st1.itx.db.repository.online.TxTellerAuthRepository;
import com.st1.itx.db.repository.day.TxTellerAuthRepositoryDay;
import com.st1.itx.db.repository.mon.TxTellerAuthRepositoryMon;
import com.st1.itx.db.repository.hist.TxTellerAuthRepositoryHist;
import com.st1.itx.db.service.TxTellerAuthService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txTellerAuthService")
@Repository
public class TxTellerAuthServiceImpl extends ASpringJpaParm implements TxTellerAuthService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxTellerAuthRepository txTellerAuthRepos;

	@Autowired
	private TxTellerAuthRepositoryDay txTellerAuthReposDay;

	@Autowired
	private TxTellerAuthRepositoryMon txTellerAuthReposMon;

	@Autowired
	private TxTellerAuthRepositoryHist txTellerAuthReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txTellerAuthRepos);
		org.junit.Assert.assertNotNull(txTellerAuthReposDay);
		org.junit.Assert.assertNotNull(txTellerAuthReposMon);
		org.junit.Assert.assertNotNull(txTellerAuthReposHist);
	}

	@Override
	public TxTellerAuth findById(TxTellerAuthId txTellerAuthId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + txTellerAuthId);
		Optional<TxTellerAuth> txTellerAuth = null;
		if (dbName.equals(ContentName.onDay))
			txTellerAuth = txTellerAuthReposDay.findById(txTellerAuthId);
		else if (dbName.equals(ContentName.onMon))
			txTellerAuth = txTellerAuthReposMon.findById(txTellerAuthId);
		else if (dbName.equals(ContentName.onHist))
			txTellerAuth = txTellerAuthReposHist.findById(txTellerAuthId);
		else
			txTellerAuth = txTellerAuthRepos.findById(txTellerAuthId);
		TxTellerAuth obj = txTellerAuth.isPresent() ? txTellerAuth.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxTellerAuth> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxTellerAuth> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "TlrNo", "AuthNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txTellerAuthReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txTellerAuthReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txTellerAuthReposHist.findAll(pageable);
		else
			slice = txTellerAuthRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxTellerAuth> findByTlrNo(String tlrNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxTellerAuth> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByTlrNo " + dbName + " : " + "tlrNo_0 : " + tlrNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = txTellerAuthReposDay.findAllByTlrNoIsOrderByTlrNoAscAuthNoAsc(tlrNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txTellerAuthReposMon.findAllByTlrNoIsOrderByTlrNoAscAuthNoAsc(tlrNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txTellerAuthReposHist.findAllByTlrNoIsOrderByTlrNoAscAuthNoAsc(tlrNo_0, pageable);
		else
			slice = txTellerAuthRepos.findAllByTlrNoIsOrderByTlrNoAscAuthNoAsc(tlrNo_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<TxTellerAuth> findByAuthNo(String authNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxTellerAuth> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByAuthNo " + dbName + " : " + "authNo_0 : " + authNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = txTellerAuthReposDay.findAllByAuthNoIsOrderByTlrNoAscAuthNoAsc(authNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txTellerAuthReposMon.findAllByAuthNoIsOrderByTlrNoAscAuthNoAsc(authNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txTellerAuthReposHist.findAllByAuthNoIsOrderByTlrNoAscAuthNoAsc(authNo_0, pageable);
		else
			slice = txTellerAuthRepos.findAllByAuthNoIsOrderByTlrNoAscAuthNoAsc(authNo_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxTellerAuth holdById(TxTellerAuthId txTellerAuthId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txTellerAuthId);
		Optional<TxTellerAuth> txTellerAuth = null;
		if (dbName.equals(ContentName.onDay))
			txTellerAuth = txTellerAuthReposDay.findByTxTellerAuthId(txTellerAuthId);
		else if (dbName.equals(ContentName.onMon))
			txTellerAuth = txTellerAuthReposMon.findByTxTellerAuthId(txTellerAuthId);
		else if (dbName.equals(ContentName.onHist))
			txTellerAuth = txTellerAuthReposHist.findByTxTellerAuthId(txTellerAuthId);
		else
			txTellerAuth = txTellerAuthRepos.findByTxTellerAuthId(txTellerAuthId);
		return txTellerAuth.isPresent() ? txTellerAuth.get() : null;
	}

	@Override
	public TxTellerAuth holdById(TxTellerAuth txTellerAuth, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txTellerAuth.getTxTellerAuthId());
		Optional<TxTellerAuth> txTellerAuthT = null;
		if (dbName.equals(ContentName.onDay))
			txTellerAuthT = txTellerAuthReposDay.findByTxTellerAuthId(txTellerAuth.getTxTellerAuthId());
		else if (dbName.equals(ContentName.onMon))
			txTellerAuthT = txTellerAuthReposMon.findByTxTellerAuthId(txTellerAuth.getTxTellerAuthId());
		else if (dbName.equals(ContentName.onHist))
			txTellerAuthT = txTellerAuthReposHist.findByTxTellerAuthId(txTellerAuth.getTxTellerAuthId());
		else
			txTellerAuthT = txTellerAuthRepos.findByTxTellerAuthId(txTellerAuth.getTxTellerAuthId());
		return txTellerAuthT.isPresent() ? txTellerAuthT.get() : null;
	}

	@Override
	public TxTellerAuth insert(TxTellerAuth txTellerAuth, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + txTellerAuth.getTxTellerAuthId());
		if (this.findById(txTellerAuth.getTxTellerAuthId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txTellerAuth.setCreateEmpNo(empNot);

		if (txTellerAuth.getLastUpdateEmpNo() == null || txTellerAuth.getLastUpdateEmpNo().isEmpty())
			txTellerAuth.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txTellerAuthReposDay.saveAndFlush(txTellerAuth);
		else if (dbName.equals(ContentName.onMon))
			return txTellerAuthReposMon.saveAndFlush(txTellerAuth);
		else if (dbName.equals(ContentName.onHist))
			return txTellerAuthReposHist.saveAndFlush(txTellerAuth);
		else
			return txTellerAuthRepos.saveAndFlush(txTellerAuth);
	}

	@Override
	public TxTellerAuth update(TxTellerAuth txTellerAuth, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txTellerAuth.getTxTellerAuthId());
		if (!empNot.isEmpty())
			txTellerAuth.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txTellerAuthReposDay.saveAndFlush(txTellerAuth);
		else if (dbName.equals(ContentName.onMon))
			return txTellerAuthReposMon.saveAndFlush(txTellerAuth);
		else if (dbName.equals(ContentName.onHist))
			return txTellerAuthReposHist.saveAndFlush(txTellerAuth);
		else
			return txTellerAuthRepos.saveAndFlush(txTellerAuth);
	}

	@Override
	public TxTellerAuth update2(TxTellerAuth txTellerAuth, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txTellerAuth.getTxTellerAuthId());
		if (!empNot.isEmpty())
			txTellerAuth.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txTellerAuthReposDay.saveAndFlush(txTellerAuth);
		else if (dbName.equals(ContentName.onMon))
			txTellerAuthReposMon.saveAndFlush(txTellerAuth);
		else if (dbName.equals(ContentName.onHist))
			txTellerAuthReposHist.saveAndFlush(txTellerAuth);
		else
			txTellerAuthRepos.saveAndFlush(txTellerAuth);
		return this.findById(txTellerAuth.getTxTellerAuthId());
	}

	@Override
	public void delete(TxTellerAuth txTellerAuth, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txTellerAuth.getTxTellerAuthId());
		if (dbName.equals(ContentName.onDay)) {
			txTellerAuthReposDay.delete(txTellerAuth);
			txTellerAuthReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txTellerAuthReposMon.delete(txTellerAuth);
			txTellerAuthReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txTellerAuthReposHist.delete(txTellerAuth);
			txTellerAuthReposHist.flush();
		} else {
			txTellerAuthRepos.delete(txTellerAuth);
			txTellerAuthRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxTellerAuth> txTellerAuth, TitaVo... titaVo) throws DBException {
		if (txTellerAuth == null || txTellerAuth.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (TxTellerAuth t : txTellerAuth) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			txTellerAuth = txTellerAuthReposDay.saveAll(txTellerAuth);
			txTellerAuthReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txTellerAuth = txTellerAuthReposMon.saveAll(txTellerAuth);
			txTellerAuthReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txTellerAuth = txTellerAuthReposHist.saveAll(txTellerAuth);
			txTellerAuthReposHist.flush();
		} else {
			txTellerAuth = txTellerAuthRepos.saveAll(txTellerAuth);
			txTellerAuthRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxTellerAuth> txTellerAuth, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (txTellerAuth == null || txTellerAuth.size() == 0)
			throw new DBException(6);

		for (TxTellerAuth t : txTellerAuth)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txTellerAuth = txTellerAuthReposDay.saveAll(txTellerAuth);
			txTellerAuthReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txTellerAuth = txTellerAuthReposMon.saveAll(txTellerAuth);
			txTellerAuthReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txTellerAuth = txTellerAuthReposHist.saveAll(txTellerAuth);
			txTellerAuthReposHist.flush();
		} else {
			txTellerAuth = txTellerAuthRepos.saveAll(txTellerAuth);
			txTellerAuthRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxTellerAuth> txTellerAuth, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txTellerAuth == null || txTellerAuth.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txTellerAuthReposDay.deleteAll(txTellerAuth);
			txTellerAuthReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txTellerAuthReposMon.deleteAll(txTellerAuth);
			txTellerAuthReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txTellerAuthReposHist.deleteAll(txTellerAuth);
			txTellerAuthReposHist.flush();
		} else {
			txTellerAuthRepos.deleteAll(txTellerAuth);
			txTellerAuthRepos.flush();
		}
	}

}
