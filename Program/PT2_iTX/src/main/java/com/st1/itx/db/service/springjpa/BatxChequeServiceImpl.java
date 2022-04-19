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
import com.st1.itx.db.domain.BatxCheque;
import com.st1.itx.db.domain.BatxChequeId;
import com.st1.itx.db.repository.online.BatxChequeRepository;
import com.st1.itx.db.repository.day.BatxChequeRepositoryDay;
import com.st1.itx.db.repository.mon.BatxChequeRepositoryMon;
import com.st1.itx.db.repository.hist.BatxChequeRepositoryHist;
import com.st1.itx.db.service.BatxChequeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("batxChequeService")
@Repository
public class BatxChequeServiceImpl extends ASpringJpaParm implements BatxChequeService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private BatxChequeRepository batxChequeRepos;

	@Autowired
	private BatxChequeRepositoryDay batxChequeReposDay;

	@Autowired
	private BatxChequeRepositoryMon batxChequeReposMon;

	@Autowired
	private BatxChequeRepositoryHist batxChequeReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(batxChequeRepos);
		org.junit.Assert.assertNotNull(batxChequeReposDay);
		org.junit.Assert.assertNotNull(batxChequeReposMon);
		org.junit.Assert.assertNotNull(batxChequeReposHist);
	}

	@Override
	public BatxCheque findById(BatxChequeId batxChequeId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + batxChequeId);
		Optional<BatxCheque> batxCheque = null;
		if (dbName.equals(ContentName.onDay))
			batxCheque = batxChequeReposDay.findById(batxChequeId);
		else if (dbName.equals(ContentName.onMon))
			batxCheque = batxChequeReposMon.findById(batxChequeId);
		else if (dbName.equals(ContentName.onHist))
			batxCheque = batxChequeReposHist.findById(batxChequeId);
		else
			batxCheque = batxChequeRepos.findById(batxChequeId);
		BatxCheque obj = batxCheque.isPresent() ? batxCheque.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<BatxCheque> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<BatxCheque> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "BatchNo", "ChequeAcct", "ChequeNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "BatchNo", "ChequeAcct", "ChequeNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = batxChequeReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = batxChequeReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = batxChequeReposHist.findAll(pageable);
		else
			slice = batxChequeRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public BatxCheque holdById(BatxChequeId batxChequeId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + batxChequeId);
		Optional<BatxCheque> batxCheque = null;
		if (dbName.equals(ContentName.onDay))
			batxCheque = batxChequeReposDay.findByBatxChequeId(batxChequeId);
		else if (dbName.equals(ContentName.onMon))
			batxCheque = batxChequeReposMon.findByBatxChequeId(batxChequeId);
		else if (dbName.equals(ContentName.onHist))
			batxCheque = batxChequeReposHist.findByBatxChequeId(batxChequeId);
		else
			batxCheque = batxChequeRepos.findByBatxChequeId(batxChequeId);
		return batxCheque.isPresent() ? batxCheque.get() : null;
	}

	@Override
	public BatxCheque holdById(BatxCheque batxCheque, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + batxCheque.getBatxChequeId());
		Optional<BatxCheque> batxChequeT = null;
		if (dbName.equals(ContentName.onDay))
			batxChequeT = batxChequeReposDay.findByBatxChequeId(batxCheque.getBatxChequeId());
		else if (dbName.equals(ContentName.onMon))
			batxChequeT = batxChequeReposMon.findByBatxChequeId(batxCheque.getBatxChequeId());
		else if (dbName.equals(ContentName.onHist))
			batxChequeT = batxChequeReposHist.findByBatxChequeId(batxCheque.getBatxChequeId());
		else
			batxChequeT = batxChequeRepos.findByBatxChequeId(batxCheque.getBatxChequeId());
		return batxChequeT.isPresent() ? batxChequeT.get() : null;
	}

	@Override
	public BatxCheque insert(BatxCheque batxCheque, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + batxCheque.getBatxChequeId());
		if (this.findById(batxCheque.getBatxChequeId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			batxCheque.setCreateEmpNo(empNot);

		if (batxCheque.getLastUpdateEmpNo() == null || batxCheque.getLastUpdateEmpNo().isEmpty())
			batxCheque.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return batxChequeReposDay.saveAndFlush(batxCheque);
		else if (dbName.equals(ContentName.onMon))
			return batxChequeReposMon.saveAndFlush(batxCheque);
		else if (dbName.equals(ContentName.onHist))
			return batxChequeReposHist.saveAndFlush(batxCheque);
		else
			return batxChequeRepos.saveAndFlush(batxCheque);
	}

	@Override
	public BatxCheque update(BatxCheque batxCheque, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + batxCheque.getBatxChequeId());
		if (!empNot.isEmpty())
			batxCheque.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return batxChequeReposDay.saveAndFlush(batxCheque);
		else if (dbName.equals(ContentName.onMon))
			return batxChequeReposMon.saveAndFlush(batxCheque);
		else if (dbName.equals(ContentName.onHist))
			return batxChequeReposHist.saveAndFlush(batxCheque);
		else
			return batxChequeRepos.saveAndFlush(batxCheque);
	}

	@Override
	public BatxCheque update2(BatxCheque batxCheque, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + batxCheque.getBatxChequeId());
		if (!empNot.isEmpty())
			batxCheque.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			batxChequeReposDay.saveAndFlush(batxCheque);
		else if (dbName.equals(ContentName.onMon))
			batxChequeReposMon.saveAndFlush(batxCheque);
		else if (dbName.equals(ContentName.onHist))
			batxChequeReposHist.saveAndFlush(batxCheque);
		else
			batxChequeRepos.saveAndFlush(batxCheque);
		return this.findById(batxCheque.getBatxChequeId());
	}

	@Override
	public void delete(BatxCheque batxCheque, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + batxCheque.getBatxChequeId());
		if (dbName.equals(ContentName.onDay)) {
			batxChequeReposDay.delete(batxCheque);
			batxChequeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			batxChequeReposMon.delete(batxCheque);
			batxChequeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			batxChequeReposHist.delete(batxCheque);
			batxChequeReposHist.flush();
		} else {
			batxChequeRepos.delete(batxCheque);
			batxChequeRepos.flush();
		}
	}

	@Override
	public void insertAll(List<BatxCheque> batxCheque, TitaVo... titaVo) throws DBException {
		if (batxCheque == null || batxCheque.size() == 0)
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
		for (BatxCheque t : batxCheque) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			batxCheque = batxChequeReposDay.saveAll(batxCheque);
			batxChequeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			batxCheque = batxChequeReposMon.saveAll(batxCheque);
			batxChequeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			batxCheque = batxChequeReposHist.saveAll(batxCheque);
			batxChequeReposHist.flush();
		} else {
			batxCheque = batxChequeRepos.saveAll(batxCheque);
			batxChequeRepos.flush();
		}
	}

	@Override
	public void updateAll(List<BatxCheque> batxCheque, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (batxCheque == null || batxCheque.size() == 0)
			throw new DBException(6);

		for (BatxCheque t : batxCheque)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			batxCheque = batxChequeReposDay.saveAll(batxCheque);
			batxChequeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			batxCheque = batxChequeReposMon.saveAll(batxCheque);
			batxChequeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			batxCheque = batxChequeReposHist.saveAll(batxCheque);
			batxChequeReposHist.flush();
		} else {
			batxCheque = batxChequeRepos.saveAll(batxCheque);
			batxChequeRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<BatxCheque> batxCheque, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (batxCheque == null || batxCheque.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			batxChequeReposDay.deleteAll(batxCheque);
			batxChequeReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			batxChequeReposMon.deleteAll(batxCheque);
			batxChequeReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			batxChequeReposHist.deleteAll(batxCheque);
			batxChequeReposHist.flush();
		} else {
			batxChequeRepos.deleteAll(batxCheque);
			batxChequeRepos.flush();
		}
	}

}
