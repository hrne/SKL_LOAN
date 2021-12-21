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
import com.st1.itx.db.domain.CustCross;
import com.st1.itx.db.domain.CustCrossId;
import com.st1.itx.db.repository.online.CustCrossRepository;
import com.st1.itx.db.repository.day.CustCrossRepositoryDay;
import com.st1.itx.db.repository.mon.CustCrossRepositoryMon;
import com.st1.itx.db.repository.hist.CustCrossRepositoryHist;
import com.st1.itx.db.service.CustCrossService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("custCrossService")
@Repository
public class CustCrossServiceImpl extends ASpringJpaParm implements CustCrossService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CustCrossRepository custCrossRepos;

	@Autowired
	private CustCrossRepositoryDay custCrossReposDay;

	@Autowired
	private CustCrossRepositoryMon custCrossReposMon;

	@Autowired
	private CustCrossRepositoryHist custCrossReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(custCrossRepos);
		org.junit.Assert.assertNotNull(custCrossReposDay);
		org.junit.Assert.assertNotNull(custCrossReposMon);
		org.junit.Assert.assertNotNull(custCrossReposHist);
	}

	@Override
	public CustCross findById(CustCrossId custCrossId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + custCrossId);
		Optional<CustCross> custCross = null;
		if (dbName.equals(ContentName.onDay))
			custCross = custCrossReposDay.findById(custCrossId);
		else if (dbName.equals(ContentName.onMon))
			custCross = custCrossReposMon.findById(custCrossId);
		else if (dbName.equals(ContentName.onHist))
			custCross = custCrossReposHist.findById(custCrossId);
		else
			custCross = custCrossRepos.findById(custCrossId);
		CustCross obj = custCross.isPresent() ? custCross.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CustCross> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CustCross> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustUKey", "SubCompanyCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustUKey", "SubCompanyCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = custCrossReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = custCrossReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = custCrossReposHist.findAll(pageable);
		else
			slice = custCrossRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CustCross> custUKeyEq(String custUKey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CustCross> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custUKeyEq " + dbName + " : " + "custUKey_0 : " + custUKey_0);
		if (dbName.equals(ContentName.onDay))
			slice = custCrossReposDay.findAllByCustUKeyIsOrderByCustUKeyAscSubCompanyCodeAsc(custUKey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = custCrossReposMon.findAllByCustUKeyIsOrderByCustUKeyAscSubCompanyCodeAsc(custUKey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = custCrossReposHist.findAllByCustUKeyIsOrderByCustUKeyAscSubCompanyCodeAsc(custUKey_0, pageable);
		else
			slice = custCrossRepos.findAllByCustUKeyIsOrderByCustUKeyAscSubCompanyCodeAsc(custUKey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CustCross subCompanyCodeFirst(String custUKey_0, String subCompanyCode_1, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("subCompanyCodeFirst " + dbName + " : " + "custUKey_0 : " + custUKey_0 + " subCompanyCode_1 : " + subCompanyCode_1);
		Optional<CustCross> custCrossT = null;
		if (dbName.equals(ContentName.onDay))
			custCrossT = custCrossReposDay.findTopByCustUKeyIsAndSubCompanyCodeIs(custUKey_0, subCompanyCode_1);
		else if (dbName.equals(ContentName.onMon))
			custCrossT = custCrossReposMon.findTopByCustUKeyIsAndSubCompanyCodeIs(custUKey_0, subCompanyCode_1);
		else if (dbName.equals(ContentName.onHist))
			custCrossT = custCrossReposHist.findTopByCustUKeyIsAndSubCompanyCodeIs(custUKey_0, subCompanyCode_1);
		else
			custCrossT = custCrossRepos.findTopByCustUKeyIsAndSubCompanyCodeIs(custUKey_0, subCompanyCode_1);

		return custCrossT.isPresent() ? custCrossT.get() : null;
	}

	@Override
	public CustCross holdById(CustCrossId custCrossId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + custCrossId);
		Optional<CustCross> custCross = null;
		if (dbName.equals(ContentName.onDay))
			custCross = custCrossReposDay.findByCustCrossId(custCrossId);
		else if (dbName.equals(ContentName.onMon))
			custCross = custCrossReposMon.findByCustCrossId(custCrossId);
		else if (dbName.equals(ContentName.onHist))
			custCross = custCrossReposHist.findByCustCrossId(custCrossId);
		else
			custCross = custCrossRepos.findByCustCrossId(custCrossId);
		return custCross.isPresent() ? custCross.get() : null;
	}

	@Override
	public CustCross holdById(CustCross custCross, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + custCross.getCustCrossId());
		Optional<CustCross> custCrossT = null;
		if (dbName.equals(ContentName.onDay))
			custCrossT = custCrossReposDay.findByCustCrossId(custCross.getCustCrossId());
		else if (dbName.equals(ContentName.onMon))
			custCrossT = custCrossReposMon.findByCustCrossId(custCross.getCustCrossId());
		else if (dbName.equals(ContentName.onHist))
			custCrossT = custCrossReposHist.findByCustCrossId(custCross.getCustCrossId());
		else
			custCrossT = custCrossRepos.findByCustCrossId(custCross.getCustCrossId());
		return custCrossT.isPresent() ? custCrossT.get() : null;
	}

	@Override
	public CustCross insert(CustCross custCross, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + custCross.getCustCrossId());
		if (this.findById(custCross.getCustCrossId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			custCross.setCreateEmpNo(empNot);

		if (custCross.getLastUpdateEmpNo() == null || custCross.getLastUpdateEmpNo().isEmpty())
			custCross.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return custCrossReposDay.saveAndFlush(custCross);
		else if (dbName.equals(ContentName.onMon))
			return custCrossReposMon.saveAndFlush(custCross);
		else if (dbName.equals(ContentName.onHist))
			return custCrossReposHist.saveAndFlush(custCross);
		else
			return custCrossRepos.saveAndFlush(custCross);
	}

	@Override
	public CustCross update(CustCross custCross, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + custCross.getCustCrossId());
		if (!empNot.isEmpty())
			custCross.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return custCrossReposDay.saveAndFlush(custCross);
		else if (dbName.equals(ContentName.onMon))
			return custCrossReposMon.saveAndFlush(custCross);
		else if (dbName.equals(ContentName.onHist))
			return custCrossReposHist.saveAndFlush(custCross);
		else
			return custCrossRepos.saveAndFlush(custCross);
	}

	@Override
	public CustCross update2(CustCross custCross, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + custCross.getCustCrossId());
		if (!empNot.isEmpty())
			custCross.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			custCrossReposDay.saveAndFlush(custCross);
		else if (dbName.equals(ContentName.onMon))
			custCrossReposMon.saveAndFlush(custCross);
		else if (dbName.equals(ContentName.onHist))
			custCrossReposHist.saveAndFlush(custCross);
		else
			custCrossRepos.saveAndFlush(custCross);
		return this.findById(custCross.getCustCrossId());
	}

	@Override
	public void delete(CustCross custCross, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + custCross.getCustCrossId());
		if (dbName.equals(ContentName.onDay)) {
			custCrossReposDay.delete(custCross);
			custCrossReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custCrossReposMon.delete(custCross);
			custCrossReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custCrossReposHist.delete(custCross);
			custCrossReposHist.flush();
		} else {
			custCrossRepos.delete(custCross);
			custCrossRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CustCross> custCross, TitaVo... titaVo) throws DBException {
		if (custCross == null || custCross.size() == 0)
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
		for (CustCross t : custCross) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			custCross = custCrossReposDay.saveAll(custCross);
			custCrossReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custCross = custCrossReposMon.saveAll(custCross);
			custCrossReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custCross = custCrossReposHist.saveAll(custCross);
			custCrossReposHist.flush();
		} else {
			custCross = custCrossRepos.saveAll(custCross);
			custCrossRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CustCross> custCross, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (custCross == null || custCross.size() == 0)
			throw new DBException(6);

		for (CustCross t : custCross)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			custCross = custCrossReposDay.saveAll(custCross);
			custCrossReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custCross = custCrossReposMon.saveAll(custCross);
			custCrossReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custCross = custCrossReposHist.saveAll(custCross);
			custCrossReposHist.flush();
		} else {
			custCross = custCrossRepos.saveAll(custCross);
			custCrossRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CustCross> custCross, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (custCross == null || custCross.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			custCrossReposDay.deleteAll(custCross);
			custCrossReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			custCrossReposMon.deleteAll(custCross);
			custCrossReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			custCrossReposHist.deleteAll(custCross);
			custCrossReposHist.flush();
		} else {
			custCrossRepos.deleteAll(custCross);
			custCrossRepos.flush();
		}
	}

}
