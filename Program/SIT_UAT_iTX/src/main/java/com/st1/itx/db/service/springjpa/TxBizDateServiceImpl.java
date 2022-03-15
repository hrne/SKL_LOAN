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
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.repository.online.TxBizDateRepository;
import com.st1.itx.db.repository.day.TxBizDateRepositoryDay;
import com.st1.itx.db.repository.mon.TxBizDateRepositoryMon;
import com.st1.itx.db.repository.hist.TxBizDateRepositoryHist;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txBizDateService")
@Repository
public class TxBizDateServiceImpl extends ASpringJpaParm implements TxBizDateService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxBizDateRepository txBizDateRepos;

	@Autowired
	private TxBizDateRepositoryDay txBizDateReposDay;

	@Autowired
	private TxBizDateRepositoryMon txBizDateReposMon;

	@Autowired
	private TxBizDateRepositoryHist txBizDateReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txBizDateRepos);
		org.junit.Assert.assertNotNull(txBizDateReposDay);
		org.junit.Assert.assertNotNull(txBizDateReposMon);
		org.junit.Assert.assertNotNull(txBizDateReposHist);
	}

	@Override
	public TxBizDate findById(String dateCode, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + dateCode);
		Optional<TxBizDate> txBizDate = null;
		if (dbName.equals(ContentName.onDay))
			txBizDate = txBizDateReposDay.findById(dateCode);
		else if (dbName.equals(ContentName.onMon))
			txBizDate = txBizDateReposMon.findById(dateCode);
		else if (dbName.equals(ContentName.onHist))
			txBizDate = txBizDateReposHist.findById(dateCode);
		else
			txBizDate = txBizDateRepos.findById(dateCode);
		TxBizDate obj = txBizDate.isPresent() ? txBizDate.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxBizDate> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxBizDate> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DateCode"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txBizDateReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txBizDateReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txBizDateReposHist.findAll(pageable);
		else
			slice = txBizDateRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxBizDate holdById(String dateCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + dateCode);
		Optional<TxBizDate> txBizDate = null;
		if (dbName.equals(ContentName.onDay))
			txBizDate = txBizDateReposDay.findByDateCode(dateCode);
		else if (dbName.equals(ContentName.onMon))
			txBizDate = txBizDateReposMon.findByDateCode(dateCode);
		else if (dbName.equals(ContentName.onHist))
			txBizDate = txBizDateReposHist.findByDateCode(dateCode);
		else
			txBizDate = txBizDateRepos.findByDateCode(dateCode);
		return txBizDate.isPresent() ? txBizDate.get() : null;
	}

	@Override
	public TxBizDate holdById(TxBizDate txBizDate, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txBizDate.getDateCode());
		Optional<TxBizDate> txBizDateT = null;
		if (dbName.equals(ContentName.onDay))
			txBizDateT = txBizDateReposDay.findByDateCode(txBizDate.getDateCode());
		else if (dbName.equals(ContentName.onMon))
			txBizDateT = txBizDateReposMon.findByDateCode(txBizDate.getDateCode());
		else if (dbName.equals(ContentName.onHist))
			txBizDateT = txBizDateReposHist.findByDateCode(txBizDate.getDateCode());
		else
			txBizDateT = txBizDateRepos.findByDateCode(txBizDate.getDateCode());
		return txBizDateT.isPresent() ? txBizDateT.get() : null;
	}

	@Override
	public TxBizDate insert(TxBizDate txBizDate, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + txBizDate.getDateCode());
		if (this.findById(txBizDate.getDateCode()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txBizDate.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txBizDateReposDay.saveAndFlush(txBizDate);
		else if (dbName.equals(ContentName.onMon))
			return txBizDateReposMon.saveAndFlush(txBizDate);
		else if (dbName.equals(ContentName.onHist))
			return txBizDateReposHist.saveAndFlush(txBizDate);
		else
			return txBizDateRepos.saveAndFlush(txBizDate);
	}

	@Override
	public TxBizDate update(TxBizDate txBizDate, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txBizDate.getDateCode());
		if (!empNot.isEmpty())
			txBizDate.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txBizDateReposDay.saveAndFlush(txBizDate);
		else if (dbName.equals(ContentName.onMon))
			return txBizDateReposMon.saveAndFlush(txBizDate);
		else if (dbName.equals(ContentName.onHist))
			return txBizDateReposHist.saveAndFlush(txBizDate);
		else
			return txBizDateRepos.saveAndFlush(txBizDate);
	}

	@Override
	public TxBizDate update2(TxBizDate txBizDate, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + txBizDate.getDateCode());
		if (!empNot.isEmpty())
			txBizDate.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txBizDateReposDay.saveAndFlush(txBizDate);
		else if (dbName.equals(ContentName.onMon))
			txBizDateReposMon.saveAndFlush(txBizDate);
		else if (dbName.equals(ContentName.onHist))
			txBizDateReposHist.saveAndFlush(txBizDate);
		else
			txBizDateRepos.saveAndFlush(txBizDate);
		return this.findById(txBizDate.getDateCode());
	}

	@Override
	public void delete(TxBizDate txBizDate, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txBizDate.getDateCode());
		if (dbName.equals(ContentName.onDay)) {
			txBizDateReposDay.delete(txBizDate);
			txBizDateReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txBizDateReposMon.delete(txBizDate);
			txBizDateReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txBizDateReposHist.delete(txBizDate);
			txBizDateReposHist.flush();
		} else {
			txBizDateRepos.delete(txBizDate);
			txBizDateRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxBizDate> txBizDate, TitaVo... titaVo) throws DBException {
		if (txBizDate == null || txBizDate.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (TxBizDate t : txBizDate)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txBizDate = txBizDateReposDay.saveAll(txBizDate);
			txBizDateReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txBizDate = txBizDateReposMon.saveAll(txBizDate);
			txBizDateReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txBizDate = txBizDateReposHist.saveAll(txBizDate);
			txBizDateReposHist.flush();
		} else {
			txBizDate = txBizDateRepos.saveAll(txBizDate);
			txBizDateRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxBizDate> txBizDate, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (txBizDate == null || txBizDate.size() == 0)
			throw new DBException(6);

		for (TxBizDate t : txBizDate)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txBizDate = txBizDateReposDay.saveAll(txBizDate);
			txBizDateReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txBizDate = txBizDateReposMon.saveAll(txBizDate);
			txBizDateReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txBizDate = txBizDateReposHist.saveAll(txBizDate);
			txBizDateReposHist.flush();
		} else {
			txBizDate = txBizDateRepos.saveAll(txBizDate);
			txBizDateRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxBizDate> txBizDate, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txBizDate == null || txBizDate.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txBizDateReposDay.deleteAll(txBizDate);
			txBizDateReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txBizDateReposMon.deleteAll(txBizDate);
			txBizDateReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txBizDateReposHist.deleteAll(txBizDate);
			txBizDateReposHist.flush();
		} else {
			txBizDateRepos.deleteAll(txBizDate);
			txBizDateRepos.flush();
		}
	}

}
