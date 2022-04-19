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
import com.st1.itx.db.domain.TxControl;
import com.st1.itx.db.repository.online.TxControlRepository;
import com.st1.itx.db.repository.day.TxControlRepositoryDay;
import com.st1.itx.db.repository.mon.TxControlRepositoryMon;
import com.st1.itx.db.repository.hist.TxControlRepositoryHist;
import com.st1.itx.db.service.TxControlService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txControlService")
@Repository
public class TxControlServiceImpl extends ASpringJpaParm implements TxControlService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private TxControlRepository txControlRepos;

	@Autowired
	private TxControlRepositoryDay txControlReposDay;

	@Autowired
	private TxControlRepositoryMon txControlReposMon;

	@Autowired
	private TxControlRepositoryHist txControlReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(txControlRepos);
		org.junit.Assert.assertNotNull(txControlReposDay);
		org.junit.Assert.assertNotNull(txControlReposMon);
		org.junit.Assert.assertNotNull(txControlReposHist);
	}

	@Override
	public TxControl findById(String code, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + code);
		Optional<TxControl> txControl = null;
		if (dbName.equals(ContentName.onDay))
			txControl = txControlReposDay.findById(code);
		else if (dbName.equals(ContentName.onMon))
			txControl = txControlReposMon.findById(code);
		else if (dbName.equals(ContentName.onHist))
			txControl = txControlReposHist.findById(code);
		else
			txControl = txControlRepos.findById(code);
		TxControl obj = txControl.isPresent() ? txControl.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<TxControl> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<TxControl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Code"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Code"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = txControlReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = txControlReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = txControlReposHist.findAll(pageable);
		else
			slice = txControlRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public TxControl holdById(String code, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + code);
		Optional<TxControl> txControl = null;
		if (dbName.equals(ContentName.onDay))
			txControl = txControlReposDay.findByCode(code);
		else if (dbName.equals(ContentName.onMon))
			txControl = txControlReposMon.findByCode(code);
		else if (dbName.equals(ContentName.onHist))
			txControl = txControlReposHist.findByCode(code);
		else
			txControl = txControlRepos.findByCode(code);
		return txControl.isPresent() ? txControl.get() : null;
	}

	@Override
	public TxControl holdById(TxControl txControl, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + txControl.getCode());
		Optional<TxControl> txControlT = null;
		if (dbName.equals(ContentName.onDay))
			txControlT = txControlReposDay.findByCode(txControl.getCode());
		else if (dbName.equals(ContentName.onMon))
			txControlT = txControlReposMon.findByCode(txControl.getCode());
		else if (dbName.equals(ContentName.onHist))
			txControlT = txControlReposHist.findByCode(txControl.getCode());
		else
			txControlT = txControlRepos.findByCode(txControl.getCode());
		return txControlT.isPresent() ? txControlT.get() : null;
	}

	@Override
	public TxControl insert(TxControl txControl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + txControl.getCode());
		if (this.findById(txControl.getCode()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			txControl.setCreateEmpNo(empNot);

		if (txControl.getLastUpdateEmpNo() == null || txControl.getLastUpdateEmpNo().isEmpty())
			txControl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txControlReposDay.saveAndFlush(txControl);
		else if (dbName.equals(ContentName.onMon))
			return txControlReposMon.saveAndFlush(txControl);
		else if (dbName.equals(ContentName.onHist))
			return txControlReposHist.saveAndFlush(txControl);
		else
			return txControlRepos.saveAndFlush(txControl);
	}

	@Override
	public TxControl update(TxControl txControl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txControl.getCode());
		if (!empNot.isEmpty())
			txControl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return txControlReposDay.saveAndFlush(txControl);
		else if (dbName.equals(ContentName.onMon))
			return txControlReposMon.saveAndFlush(txControl);
		else if (dbName.equals(ContentName.onHist))
			return txControlReposHist.saveAndFlush(txControl);
		else
			return txControlRepos.saveAndFlush(txControl);
	}

	@Override
	public TxControl update2(TxControl txControl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + txControl.getCode());
		if (!empNot.isEmpty())
			txControl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			txControlReposDay.saveAndFlush(txControl);
		else if (dbName.equals(ContentName.onMon))
			txControlReposMon.saveAndFlush(txControl);
		else if (dbName.equals(ContentName.onHist))
			txControlReposHist.saveAndFlush(txControl);
		else
			txControlRepos.saveAndFlush(txControl);
		return this.findById(txControl.getCode());
	}

	@Override
	public void delete(TxControl txControl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + txControl.getCode());
		if (dbName.equals(ContentName.onDay)) {
			txControlReposDay.delete(txControl);
			txControlReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txControlReposMon.delete(txControl);
			txControlReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txControlReposHist.delete(txControl);
			txControlReposHist.flush();
		} else {
			txControlRepos.delete(txControl);
			txControlRepos.flush();
		}
	}

	@Override
	public void insertAll(List<TxControl> txControl, TitaVo... titaVo) throws DBException {
		if (txControl == null || txControl.size() == 0)
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
		for (TxControl t : txControl) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			txControl = txControlReposDay.saveAll(txControl);
			txControlReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txControl = txControlReposMon.saveAll(txControl);
			txControlReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txControl = txControlReposHist.saveAll(txControl);
			txControlReposHist.flush();
		} else {
			txControl = txControlRepos.saveAll(txControl);
			txControlRepos.flush();
		}
	}

	@Override
	public void updateAll(List<TxControl> txControl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (txControl == null || txControl.size() == 0)
			throw new DBException(6);

		for (TxControl t : txControl)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			txControl = txControlReposDay.saveAll(txControl);
			txControlReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txControl = txControlReposMon.saveAll(txControl);
			txControlReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txControl = txControlReposHist.saveAll(txControl);
			txControlReposHist.flush();
		} else {
			txControl = txControlRepos.saveAll(txControl);
			txControlRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<TxControl> txControl, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (txControl == null || txControl.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			txControlReposDay.deleteAll(txControl);
			txControlReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			txControlReposMon.deleteAll(txControl);
			txControlReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			txControlReposHist.deleteAll(txControl);
			txControlReposHist.flush();
		} else {
			txControlRepos.deleteAll(txControl);
			txControlRepos.flush();
		}
	}

}
