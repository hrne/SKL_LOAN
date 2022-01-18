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
import com.st1.itx.db.domain.TxErrCode;
import com.st1.itx.db.repository.online.TxErrCodeRepository;
import com.st1.itx.db.repository.day.TxErrCodeRepositoryDay;
import com.st1.itx.db.repository.mon.TxErrCodeRepositoryMon;
import com.st1.itx.db.repository.hist.TxErrCodeRepositoryHist;
import com.st1.itx.db.service.TxErrCodeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txErrCodeService")
@Repository
public class TxErrCodeServiceImpl extends ASpringJpaParm implements TxErrCodeService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxErrCodeRepository txErrCodeRepos;

	@Autowired
	private TxErrCodeRepositoryDay txErrCodeReposDay;

	@Autowired
	private TxErrCodeRepositoryMon txErrCodeReposMon;

	@Autowired
	private TxErrCodeRepositoryHist txErrCodeReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txErrCodeRepos);
		org.junit.Assert.assertNotNull(txErrCodeReposDay);
		org.junit.Assert.assertNotNull(txErrCodeReposMon);
		org.junit.Assert.assertNotNull(txErrCodeReposHist);
	}

	@Override
	public TxErrCode findById(String errCode, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + errCode);
		Optional<TxErrCode> txErrCode = null;
		if (dbName.equals(ContentName.onDay))
			txErrCode = txErrCodeReposDay.findById(errCode);
		else if (dbName.equals(ContentName.onMon))
			txErrCode = txErrCodeReposMon.findById(errCode);
		else if (dbName.equals(ContentName.onHist))
			txErrCode = txErrCodeReposHist.findById(errCode);
		else
			txErrCode = txErrCodeRepos.findById(errCode);
		TxErrCode obj = txErrCode.isPresent() ? txErrCode.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxErrCode> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxErrCode> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ErrCode"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txErrCodeReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txErrCodeReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txErrCodeReposHist.findAll(pageable);
		else
			slice = txErrCodeRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxErrCode holdById(String errCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + errCode);
		Optional<TxErrCode> txErrCode = null;
		if (dbName.equals(ContentName.onDay))
			txErrCode = txErrCodeReposDay.findByErrCode(errCode);
		else if (dbName.equals(ContentName.onMon))
			txErrCode = txErrCodeReposMon.findByErrCode(errCode);
		else if (dbName.equals(ContentName.onHist))
			txErrCode = txErrCodeReposHist.findByErrCode(errCode);
		else
			txErrCode = txErrCodeRepos.findByErrCode(errCode);
		return txErrCode.isPresent() ? txErrCode.get() : null;
	}

	@Override
	public TxErrCode holdById(TxErrCode txErrCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txErrCode.getErrCode());
		Optional<TxErrCode> txErrCodeT = null;
		if (dbName.equals(ContentName.onDay))
			txErrCodeT = txErrCodeReposDay.findByErrCode(txErrCode.getErrCode());
		else if (dbName.equals(ContentName.onMon))
			txErrCodeT = txErrCodeReposMon.findByErrCode(txErrCode.getErrCode());
		else if (dbName.equals(ContentName.onHist))
			txErrCodeT = txErrCodeReposHist.findByErrCode(txErrCode.getErrCode());
		else
			txErrCodeT = txErrCodeRepos.findByErrCode(txErrCode.getErrCode());
		return txErrCodeT.isPresent() ? txErrCodeT.get() : null;
	}

	@Override
	public TxErrCode insert(TxErrCode txErrCode, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + txErrCode.getErrCode());
		if (this.findById(txErrCode.getErrCode()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txErrCode.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txErrCodeReposDay.saveAndFlush(txErrCode);
		else if (dbName.equals(ContentName.onMon))
			return txErrCodeReposMon.saveAndFlush(txErrCode);
		else if (dbName.equals(ContentName.onHist))
			return txErrCodeReposHist.saveAndFlush(txErrCode);
		else
			return txErrCodeRepos.saveAndFlush(txErrCode);
	}

	@Override
	public TxErrCode update(TxErrCode txErrCode, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txErrCode.getErrCode());
		if (!empNot.isEmpty())
			txErrCode.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txErrCodeReposDay.saveAndFlush(txErrCode);
		else if (dbName.equals(ContentName.onMon))
			return txErrCodeReposMon.saveAndFlush(txErrCode);
		else if (dbName.equals(ContentName.onHist))
			return txErrCodeReposHist.saveAndFlush(txErrCode);
		else
			return txErrCodeRepos.saveAndFlush(txErrCode);
	}

	@Override
	public TxErrCode update2(TxErrCode txErrCode, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txErrCode.getErrCode());
		if (!empNot.isEmpty())
			txErrCode.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txErrCodeReposDay.saveAndFlush(txErrCode);
		else if (dbName.equals(ContentName.onMon))
			txErrCodeReposMon.saveAndFlush(txErrCode);
		else if (dbName.equals(ContentName.onHist))
			txErrCodeReposHist.saveAndFlush(txErrCode);
		else
			txErrCodeRepos.saveAndFlush(txErrCode);
		return this.findById(txErrCode.getErrCode());
	}

	@Override
	public void delete(TxErrCode txErrCode, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txErrCode.getErrCode());
		if (dbName.equals(ContentName.onDay)) {
			txErrCodeReposDay.delete(txErrCode);
			txErrCodeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txErrCodeReposMon.delete(txErrCode);
			txErrCodeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txErrCodeReposHist.delete(txErrCode);
			txErrCodeReposHist.flush();
		} else {
			txErrCodeRepos.delete(txErrCode);
			txErrCodeRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxErrCode> txErrCode, TitaVo... titaVo) throws DBException {
		if (txErrCode == null || txErrCode.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (TxErrCode t : txErrCode)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txErrCode = txErrCodeReposDay.saveAll(txErrCode);
			txErrCodeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txErrCode = txErrCodeReposMon.saveAll(txErrCode);
			txErrCodeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txErrCode = txErrCodeReposHist.saveAll(txErrCode);
			txErrCodeReposHist.flush();
		} else {
			txErrCode = txErrCodeRepos.saveAll(txErrCode);
			txErrCodeRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxErrCode> txErrCode, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (txErrCode == null || txErrCode.size() == 0)
			throw new DBException(6);

		for (TxErrCode t : txErrCode)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txErrCode = txErrCodeReposDay.saveAll(txErrCode);
			txErrCodeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txErrCode = txErrCodeReposMon.saveAll(txErrCode);
			txErrCodeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txErrCode = txErrCodeReposHist.saveAll(txErrCode);
			txErrCodeReposHist.flush();
		} else {
			txErrCode = txErrCodeRepos.saveAll(txErrCode);
			txErrCodeRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxErrCode> txErrCode, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txErrCode == null || txErrCode.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txErrCodeReposDay.deleteAll(txErrCode);
			txErrCodeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txErrCodeReposMon.deleteAll(txErrCode);
			txErrCodeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txErrCodeReposHist.deleteAll(txErrCode);
			txErrCodeReposHist.flush();
		} else {
			txErrCodeRepos.deleteAll(txErrCode);
			txErrCodeRepos.flush();
		}
	}

}
