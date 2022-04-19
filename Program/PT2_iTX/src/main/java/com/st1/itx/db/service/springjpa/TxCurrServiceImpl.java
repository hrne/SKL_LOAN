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
import com.st1.itx.db.domain.TxCurr;
import com.st1.itx.db.repository.online.TxCurrRepository;
import com.st1.itx.db.repository.day.TxCurrRepositoryDay;
import com.st1.itx.db.repository.mon.TxCurrRepositoryMon;
import com.st1.itx.db.repository.hist.TxCurrRepositoryHist;
import com.st1.itx.db.service.TxCurrService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txCurrService")
@Repository
public class TxCurrServiceImpl extends ASpringJpaParm implements TxCurrService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxCurrRepository txCurrRepos;

	@Autowired
	private TxCurrRepositoryDay txCurrReposDay;

	@Autowired
	private TxCurrRepositoryMon txCurrReposMon;

	@Autowired
	private TxCurrRepositoryHist txCurrReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txCurrRepos);
		org.junit.Assert.assertNotNull(txCurrReposDay);
		org.junit.Assert.assertNotNull(txCurrReposMon);
		org.junit.Assert.assertNotNull(txCurrReposHist);
	}

	@Override
	public TxCurr findById(int curCd, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + curCd);
		Optional<TxCurr> txCurr = null;
		if (dbName.equals(ContentName.onDay))
			txCurr = txCurrReposDay.findById(curCd);
		else if (dbName.equals(ContentName.onMon))
			txCurr = txCurrReposMon.findById(curCd);
		else if (dbName.equals(ContentName.onHist))
			txCurr = txCurrReposHist.findById(curCd);
		else
			txCurr = txCurrRepos.findById(curCd);
		TxCurr obj = txCurr.isPresent() ? txCurr.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxCurr> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxCurr> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CurCd"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txCurrReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txCurrReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txCurrReposHist.findAll(pageable);
		else
			slice = txCurrRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxCurr holdById(int curCd, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + curCd);
		Optional<TxCurr> txCurr = null;
		if (dbName.equals(ContentName.onDay))
			txCurr = txCurrReposDay.findByCurCd(curCd);
		else if (dbName.equals(ContentName.onMon))
			txCurr = txCurrReposMon.findByCurCd(curCd);
		else if (dbName.equals(ContentName.onHist))
			txCurr = txCurrReposHist.findByCurCd(curCd);
		else
			txCurr = txCurrRepos.findByCurCd(curCd);
		return txCurr.isPresent() ? txCurr.get() : null;
	}

	@Override
	public TxCurr holdById(TxCurr txCurr, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txCurr.getCurCd());
		Optional<TxCurr> txCurrT = null;
		if (dbName.equals(ContentName.onDay))
			txCurrT = txCurrReposDay.findByCurCd(txCurr.getCurCd());
		else if (dbName.equals(ContentName.onMon))
			txCurrT = txCurrReposMon.findByCurCd(txCurr.getCurCd());
		else if (dbName.equals(ContentName.onHist))
			txCurrT = txCurrReposHist.findByCurCd(txCurr.getCurCd());
		else
			txCurrT = txCurrRepos.findByCurCd(txCurr.getCurCd());
		return txCurrT.isPresent() ? txCurrT.get() : null;
	}

	@Override
	public TxCurr insert(TxCurr txCurr, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + txCurr.getCurCd());
		if (this.findById(txCurr.getCurCd()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txCurr.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txCurrReposDay.saveAndFlush(txCurr);
		else if (dbName.equals(ContentName.onMon))
			return txCurrReposMon.saveAndFlush(txCurr);
		else if (dbName.equals(ContentName.onHist))
			return txCurrReposHist.saveAndFlush(txCurr);
		else
			return txCurrRepos.saveAndFlush(txCurr);
	}

	@Override
	public TxCurr update(TxCurr txCurr, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txCurr.getCurCd());
		if (!empNot.isEmpty())
			txCurr.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txCurrReposDay.saveAndFlush(txCurr);
		else if (dbName.equals(ContentName.onMon))
			return txCurrReposMon.saveAndFlush(txCurr);
		else if (dbName.equals(ContentName.onHist))
			return txCurrReposHist.saveAndFlush(txCurr);
		else
			return txCurrRepos.saveAndFlush(txCurr);
	}

	@Override
	public TxCurr update2(TxCurr txCurr, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txCurr.getCurCd());
		if (!empNot.isEmpty())
			txCurr.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txCurrReposDay.saveAndFlush(txCurr);
		else if (dbName.equals(ContentName.onMon))
			txCurrReposMon.saveAndFlush(txCurr);
		else if (dbName.equals(ContentName.onHist))
			txCurrReposHist.saveAndFlush(txCurr);
		else
			txCurrRepos.saveAndFlush(txCurr);
		return this.findById(txCurr.getCurCd());
	}

	@Override
	public void delete(TxCurr txCurr, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txCurr.getCurCd());
		if (dbName.equals(ContentName.onDay)) {
			txCurrReposDay.delete(txCurr);
			txCurrReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txCurrReposMon.delete(txCurr);
			txCurrReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txCurrReposHist.delete(txCurr);
			txCurrReposHist.flush();
		} else {
			txCurrRepos.delete(txCurr);
			txCurrRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxCurr> txCurr, TitaVo... titaVo) throws DBException {
		if (txCurr == null || txCurr.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (TxCurr t : txCurr)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txCurr = txCurrReposDay.saveAll(txCurr);
			txCurrReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txCurr = txCurrReposMon.saveAll(txCurr);
			txCurrReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txCurr = txCurrReposHist.saveAll(txCurr);
			txCurrReposHist.flush();
		} else {
			txCurr = txCurrRepos.saveAll(txCurr);
			txCurrRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxCurr> txCurr, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (txCurr == null || txCurr.size() == 0)
			throw new DBException(6);

		for (TxCurr t : txCurr)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txCurr = txCurrReposDay.saveAll(txCurr);
			txCurrReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txCurr = txCurrReposMon.saveAll(txCurr);
			txCurrReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txCurr = txCurrReposHist.saveAll(txCurr);
			txCurrReposHist.flush();
		} else {
			txCurr = txCurrRepos.saveAll(txCurr);
			txCurrRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxCurr> txCurr, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txCurr == null || txCurr.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txCurrReposDay.deleteAll(txCurr);
			txCurrReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txCurrReposMon.deleteAll(txCurr);
			txCurrReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txCurrReposHist.deleteAll(txCurr);
			txCurrReposHist.flush();
		} else {
			txCurrRepos.deleteAll(txCurr);
			txCurrRepos.flush();
		}
	}

}
