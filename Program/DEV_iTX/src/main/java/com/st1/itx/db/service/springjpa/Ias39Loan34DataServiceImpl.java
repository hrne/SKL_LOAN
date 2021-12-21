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
import com.st1.itx.db.domain.Ias39Loan34Data;
import com.st1.itx.db.domain.Ias39Loan34DataId;
import com.st1.itx.db.repository.online.Ias39Loan34DataRepository;
import com.st1.itx.db.repository.day.Ias39Loan34DataRepositoryDay;
import com.st1.itx.db.repository.mon.Ias39Loan34DataRepositoryMon;
import com.st1.itx.db.repository.hist.Ias39Loan34DataRepositoryHist;
import com.st1.itx.db.service.Ias39Loan34DataService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ias39Loan34DataService")
@Repository
public class Ias39Loan34DataServiceImpl extends ASpringJpaParm implements Ias39Loan34DataService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Ias39Loan34DataRepository ias39Loan34DataRepos;

	@Autowired
	private Ias39Loan34DataRepositoryDay ias39Loan34DataReposDay;

	@Autowired
	private Ias39Loan34DataRepositoryMon ias39Loan34DataReposMon;

	@Autowired
	private Ias39Loan34DataRepositoryHist ias39Loan34DataReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(ias39Loan34DataRepos);
		org.junit.Assert.assertNotNull(ias39Loan34DataReposDay);
		org.junit.Assert.assertNotNull(ias39Loan34DataReposMon);
		org.junit.Assert.assertNotNull(ias39Loan34DataReposHist);
	}

	@Override
	public Ias39Loan34Data findById(Ias39Loan34DataId ias39Loan34DataId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + ias39Loan34DataId);
		Optional<Ias39Loan34Data> ias39Loan34Data = null;
		if (dbName.equals(ContentName.onDay))
			ias39Loan34Data = ias39Loan34DataReposDay.findById(ias39Loan34DataId);
		else if (dbName.equals(ContentName.onMon))
			ias39Loan34Data = ias39Loan34DataReposMon.findById(ias39Loan34DataId);
		else if (dbName.equals(ContentName.onHist))
			ias39Loan34Data = ias39Loan34DataReposHist.findById(ias39Loan34DataId);
		else
			ias39Loan34Data = ias39Loan34DataRepos.findById(ias39Loan34DataId);
		Ias39Loan34Data obj = ias39Loan34Data.isPresent() ? ias39Loan34Data.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<Ias39Loan34Data> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias39Loan34Data> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "ApplNo", "BormNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "ApplNo", "BormNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = ias39Loan34DataReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias39Loan34DataReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias39Loan34DataReposHist.findAll(pageable);
		else
			slice = ias39Loan34DataRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Ias39Loan34Data holdById(Ias39Loan34DataId ias39Loan34DataId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias39Loan34DataId);
		Optional<Ias39Loan34Data> ias39Loan34Data = null;
		if (dbName.equals(ContentName.onDay))
			ias39Loan34Data = ias39Loan34DataReposDay.findByIas39Loan34DataId(ias39Loan34DataId);
		else if (dbName.equals(ContentName.onMon))
			ias39Loan34Data = ias39Loan34DataReposMon.findByIas39Loan34DataId(ias39Loan34DataId);
		else if (dbName.equals(ContentName.onHist))
			ias39Loan34Data = ias39Loan34DataReposHist.findByIas39Loan34DataId(ias39Loan34DataId);
		else
			ias39Loan34Data = ias39Loan34DataRepos.findByIas39Loan34DataId(ias39Loan34DataId);
		return ias39Loan34Data.isPresent() ? ias39Loan34Data.get() : null;
	}

	@Override
	public Ias39Loan34Data holdById(Ias39Loan34Data ias39Loan34Data, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias39Loan34Data.getIas39Loan34DataId());
		Optional<Ias39Loan34Data> ias39Loan34DataT = null;
		if (dbName.equals(ContentName.onDay))
			ias39Loan34DataT = ias39Loan34DataReposDay.findByIas39Loan34DataId(ias39Loan34Data.getIas39Loan34DataId());
		else if (dbName.equals(ContentName.onMon))
			ias39Loan34DataT = ias39Loan34DataReposMon.findByIas39Loan34DataId(ias39Loan34Data.getIas39Loan34DataId());
		else if (dbName.equals(ContentName.onHist))
			ias39Loan34DataT = ias39Loan34DataReposHist.findByIas39Loan34DataId(ias39Loan34Data.getIas39Loan34DataId());
		else
			ias39Loan34DataT = ias39Loan34DataRepos.findByIas39Loan34DataId(ias39Loan34Data.getIas39Loan34DataId());
		return ias39Loan34DataT.isPresent() ? ias39Loan34DataT.get() : null;
	}

	@Override
	public Ias39Loan34Data insert(Ias39Loan34Data ias39Loan34Data, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + ias39Loan34Data.getIas39Loan34DataId());
		if (this.findById(ias39Loan34Data.getIas39Loan34DataId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			ias39Loan34Data.setCreateEmpNo(empNot);

		if (ias39Loan34Data.getLastUpdateEmpNo() == null || ias39Loan34Data.getLastUpdateEmpNo().isEmpty())
			ias39Loan34Data.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias39Loan34DataReposDay.saveAndFlush(ias39Loan34Data);
		else if (dbName.equals(ContentName.onMon))
			return ias39Loan34DataReposMon.saveAndFlush(ias39Loan34Data);
		else if (dbName.equals(ContentName.onHist))
			return ias39Loan34DataReposHist.saveAndFlush(ias39Loan34Data);
		else
			return ias39Loan34DataRepos.saveAndFlush(ias39Loan34Data);
	}

	@Override
	public Ias39Loan34Data update(Ias39Loan34Data ias39Loan34Data, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias39Loan34Data.getIas39Loan34DataId());
		if (!empNot.isEmpty())
			ias39Loan34Data.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias39Loan34DataReposDay.saveAndFlush(ias39Loan34Data);
		else if (dbName.equals(ContentName.onMon))
			return ias39Loan34DataReposMon.saveAndFlush(ias39Loan34Data);
		else if (dbName.equals(ContentName.onHist))
			return ias39Loan34DataReposHist.saveAndFlush(ias39Loan34Data);
		else
			return ias39Loan34DataRepos.saveAndFlush(ias39Loan34Data);
	}

	@Override
	public Ias39Loan34Data update2(Ias39Loan34Data ias39Loan34Data, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias39Loan34Data.getIas39Loan34DataId());
		if (!empNot.isEmpty())
			ias39Loan34Data.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			ias39Loan34DataReposDay.saveAndFlush(ias39Loan34Data);
		else if (dbName.equals(ContentName.onMon))
			ias39Loan34DataReposMon.saveAndFlush(ias39Loan34Data);
		else if (dbName.equals(ContentName.onHist))
			ias39Loan34DataReposHist.saveAndFlush(ias39Loan34Data);
		else
			ias39Loan34DataRepos.saveAndFlush(ias39Loan34Data);
		return this.findById(ias39Loan34Data.getIas39Loan34DataId());
	}

	@Override
	public void delete(Ias39Loan34Data ias39Loan34Data, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + ias39Loan34Data.getIas39Loan34DataId());
		if (dbName.equals(ContentName.onDay)) {
			ias39Loan34DataReposDay.delete(ias39Loan34Data);
			ias39Loan34DataReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39Loan34DataReposMon.delete(ias39Loan34Data);
			ias39Loan34DataReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39Loan34DataReposHist.delete(ias39Loan34Data);
			ias39Loan34DataReposHist.flush();
		} else {
			ias39Loan34DataRepos.delete(ias39Loan34Data);
			ias39Loan34DataRepos.flush();
		}
	}

	@Override
	public void insertAll(List<Ias39Loan34Data> ias39Loan34Data, TitaVo... titaVo) throws DBException {
		if (ias39Loan34Data == null || ias39Loan34Data.size() == 0)
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
		for (Ias39Loan34Data t : ias39Loan34Data) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			ias39Loan34Data = ias39Loan34DataReposDay.saveAll(ias39Loan34Data);
			ias39Loan34DataReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39Loan34Data = ias39Loan34DataReposMon.saveAll(ias39Loan34Data);
			ias39Loan34DataReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39Loan34Data = ias39Loan34DataReposHist.saveAll(ias39Loan34Data);
			ias39Loan34DataReposHist.flush();
		} else {
			ias39Loan34Data = ias39Loan34DataRepos.saveAll(ias39Loan34Data);
			ias39Loan34DataRepos.flush();
		}
	}

	@Override
	public void updateAll(List<Ias39Loan34Data> ias39Loan34Data, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (ias39Loan34Data == null || ias39Loan34Data.size() == 0)
			throw new DBException(6);

		for (Ias39Loan34Data t : ias39Loan34Data)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			ias39Loan34Data = ias39Loan34DataReposDay.saveAll(ias39Loan34Data);
			ias39Loan34DataReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39Loan34Data = ias39Loan34DataReposMon.saveAll(ias39Loan34Data);
			ias39Loan34DataReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39Loan34Data = ias39Loan34DataReposHist.saveAll(ias39Loan34Data);
			ias39Loan34DataReposHist.flush();
		} else {
			ias39Loan34Data = ias39Loan34DataRepos.saveAll(ias39Loan34Data);
			ias39Loan34DataRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<Ias39Loan34Data> ias39Loan34Data, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (ias39Loan34Data == null || ias39Loan34Data.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			ias39Loan34DataReposDay.deleteAll(ias39Loan34Data);
			ias39Loan34DataReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39Loan34DataReposMon.deleteAll(ias39Loan34Data);
			ias39Loan34DataReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39Loan34DataReposHist.deleteAll(ias39Loan34Data);
			ias39Loan34DataReposHist.flush();
		} else {
			ias39Loan34DataRepos.deleteAll(ias39Loan34Data);
			ias39Loan34DataRepos.flush();
		}
	}

	@Override
	public void Usp_L7_Ias39Loan34Data_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			ias39Loan34DataReposDay.uspL7Ias39loan34dataUpd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onMon))
			ias39Loan34DataReposMon.uspL7Ias39loan34dataUpd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onHist))
			ias39Loan34DataReposHist.uspL7Ias39loan34dataUpd(TBSDYF, EmpNo);
		else
			ias39Loan34DataRepos.uspL7Ias39loan34dataUpd(TBSDYF, EmpNo);
	}

}
