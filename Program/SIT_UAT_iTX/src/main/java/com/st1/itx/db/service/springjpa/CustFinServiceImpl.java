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
import com.st1.itx.db.domain.CustFin;
import com.st1.itx.db.domain.CustFinId;
import com.st1.itx.db.repository.online.CustFinRepository;
import com.st1.itx.db.repository.day.CustFinRepositoryDay;
import com.st1.itx.db.repository.mon.CustFinRepositoryMon;
import com.st1.itx.db.repository.hist.CustFinRepositoryHist;
import com.st1.itx.db.service.CustFinService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("custFinService")
@Repository
public class CustFinServiceImpl extends ASpringJpaParm implements CustFinService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CustFinRepository custFinRepos;

	@Autowired
	private CustFinRepositoryDay custFinReposDay;

	@Autowired
	private CustFinRepositoryMon custFinReposMon;

	@Autowired
	private CustFinRepositoryHist custFinReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(custFinRepos);
		org.junit.Assert.assertNotNull(custFinReposDay);
		org.junit.Assert.assertNotNull(custFinReposMon);
		org.junit.Assert.assertNotNull(custFinReposHist);
	}

	@Override
	public CustFin findById(CustFinId custFinId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + custFinId);
		Optional<CustFin> custFin = null;
		if (dbName.equals(ContentName.onDay))
			custFin = custFinReposDay.findById(custFinId);
		else if (dbName.equals(ContentName.onMon))
			custFin = custFinReposMon.findById(custFinId);
		else if (dbName.equals(ContentName.onHist))
			custFin = custFinReposHist.findById(custFinId);
		else
			custFin = custFinRepos.findById(custFinId);
		CustFin obj = custFin.isPresent() ? custFin.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CustFin> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CustFin> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustUKey", "DataYear"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustUKey", "DataYear"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = custFinReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = custFinReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = custFinReposHist.findAll(pageable);
		else
			slice = custFinRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CustFin> custUKeyEq(String custUKey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CustFin> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custUKeyEq " + dbName + " : " + "custUKey_0 : " + custUKey_0);
		if (dbName.equals(ContentName.onDay))
			slice = custFinReposDay.findAllByCustUKeyIsOrderByDataYearDesc(custUKey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = custFinReposMon.findAllByCustUKeyIsOrderByDataYearDesc(custUKey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = custFinReposHist.findAllByCustUKeyIsOrderByDataYearDesc(custUKey_0, pageable);
		else
			slice = custFinRepos.findAllByCustUKeyIsOrderByDataYearDesc(custUKey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CustFin holdById(CustFinId custFinId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + custFinId);
		Optional<CustFin> custFin = null;
		if (dbName.equals(ContentName.onDay))
			custFin = custFinReposDay.findByCustFinId(custFinId);
		else if (dbName.equals(ContentName.onMon))
			custFin = custFinReposMon.findByCustFinId(custFinId);
		else if (dbName.equals(ContentName.onHist))
			custFin = custFinReposHist.findByCustFinId(custFinId);
		else
			custFin = custFinRepos.findByCustFinId(custFinId);
		return custFin.isPresent() ? custFin.get() : null;
	}

	@Override
	public CustFin holdById(CustFin custFin, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + custFin.getCustFinId());
		Optional<CustFin> custFinT = null;
		if (dbName.equals(ContentName.onDay))
			custFinT = custFinReposDay.findByCustFinId(custFin.getCustFinId());
		else if (dbName.equals(ContentName.onMon))
			custFinT = custFinReposMon.findByCustFinId(custFin.getCustFinId());
		else if (dbName.equals(ContentName.onHist))
			custFinT = custFinReposHist.findByCustFinId(custFin.getCustFinId());
		else
			custFinT = custFinRepos.findByCustFinId(custFin.getCustFinId());
		return custFinT.isPresent() ? custFinT.get() : null;
	}

	@Override
	public CustFin insert(CustFin custFin, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + custFin.getCustFinId());
		if (this.findById(custFin.getCustFinId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			custFin.setCreateEmpNo(empNot);

		if (custFin.getLastUpdateEmpNo() == null || custFin.getLastUpdateEmpNo().isEmpty())
			custFin.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return custFinReposDay.saveAndFlush(custFin);
		else if (dbName.equals(ContentName.onMon))
			return custFinReposMon.saveAndFlush(custFin);
		else if (dbName.equals(ContentName.onHist))
			return custFinReposHist.saveAndFlush(custFin);
		else
			return custFinRepos.saveAndFlush(custFin);
	}

	@Override
	public CustFin update(CustFin custFin, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + custFin.getCustFinId());
		if (!empNot.isEmpty())
			custFin.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return custFinReposDay.saveAndFlush(custFin);
		else if (dbName.equals(ContentName.onMon))
			return custFinReposMon.saveAndFlush(custFin);
		else if (dbName.equals(ContentName.onHist))
			return custFinReposHist.saveAndFlush(custFin);
		else
			return custFinRepos.saveAndFlush(custFin);
	}

	@Override
	public CustFin update2(CustFin custFin, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + custFin.getCustFinId());
		if (!empNot.isEmpty())
			custFin.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			custFinReposDay.saveAndFlush(custFin);
		else if (dbName.equals(ContentName.onMon))
			custFinReposMon.saveAndFlush(custFin);
		else if (dbName.equals(ContentName.onHist))
			custFinReposHist.saveAndFlush(custFin);
		else
			custFinRepos.saveAndFlush(custFin);
		return this.findById(custFin.getCustFinId());
	}

	@Override
	public void delete(CustFin custFin, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + custFin.getCustFinId());
		if (dbName.equals(ContentName.onDay)) {
			custFinReposDay.delete(custFin);
			custFinReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custFinReposMon.delete(custFin);
			custFinReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custFinReposHist.delete(custFin);
			custFinReposHist.flush();
		} else {
			custFinRepos.delete(custFin);
			custFinRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CustFin> custFin, TitaVo... titaVo) throws DBException {
		if (custFin == null || custFin.size() == 0)
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
		for (CustFin t : custFin) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			custFin = custFinReposDay.saveAll(custFin);
			custFinReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custFin = custFinReposMon.saveAll(custFin);
			custFinReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custFin = custFinReposHist.saveAll(custFin);
			custFinReposHist.flush();
		} else {
			custFin = custFinRepos.saveAll(custFin);
			custFinRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CustFin> custFin, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (custFin == null || custFin.size() == 0)
			throw new DBException(6);

		for (CustFin t : custFin)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			custFin = custFinReposDay.saveAll(custFin);
			custFinReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custFin = custFinReposMon.saveAll(custFin);
			custFinReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custFin = custFinReposHist.saveAll(custFin);
			custFinReposHist.flush();
		} else {
			custFin = custFinRepos.saveAll(custFin);
			custFinRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CustFin> custFin, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (custFin == null || custFin.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			custFinReposDay.deleteAll(custFin);
			custFinReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custFinReposMon.deleteAll(custFin);
			custFinReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custFinReposHist.deleteAll(custFin);
			custFinReposHist.flush();
		} else {
			custFinRepos.deleteAll(custFin);
			custFinRepos.flush();
		}
	}

}
