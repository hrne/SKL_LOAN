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
import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.domain.ReltMainId;
import com.st1.itx.db.repository.online.ReltMainRepository;
import com.st1.itx.db.repository.day.ReltMainRepositoryDay;
import com.st1.itx.db.repository.mon.ReltMainRepositoryMon;
import com.st1.itx.db.repository.hist.ReltMainRepositoryHist;
import com.st1.itx.db.service.ReltMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("reltMainService")
@Repository
public class ReltMainServiceImpl extends ASpringJpaParm implements ReltMainService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ReltMainRepository reltMainRepos;

	@Autowired
	private ReltMainRepositoryDay reltMainReposDay;

	@Autowired
	private ReltMainRepositoryMon reltMainReposMon;

	@Autowired
	private ReltMainRepositoryHist reltMainReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(reltMainRepos);
		org.junit.Assert.assertNotNull(reltMainReposDay);
		org.junit.Assert.assertNotNull(reltMainReposMon);
		org.junit.Assert.assertNotNull(reltMainReposHist);
	}

	@Override
	public ReltMain findById(ReltMainId reltMainId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + reltMainId);
		Optional<ReltMain> reltMain = null;
		if (dbName.equals(ContentName.onDay))
			reltMain = reltMainReposDay.findById(reltMainId);
		else if (dbName.equals(ContentName.onMon))
			reltMain = reltMainReposMon.findById(reltMainId);
		else if (dbName.equals(ContentName.onHist))
			reltMain = reltMainReposHist.findById(reltMainId);
		else
			reltMain = reltMainRepos.findById(reltMainId);
		ReltMain obj = reltMain.isPresent() ? reltMain.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ReltMain> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ReltMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CaseNo", "CustNo", "ReltUKey"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CaseNo", "CustNo", "ReltUKey"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = reltMainReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = reltMainReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = reltMainReposHist.findAll(pageable);
		else
			slice = reltMainRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ReltMain ReltUKeyFirst(String reltUKey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ReltUKeyFirst " + dbName + " : " + "reltUKey_0 : " + reltUKey_0);
		Optional<ReltMain> reltMainT = null;
		if (dbName.equals(ContentName.onDay))
			reltMainT = reltMainReposDay.findTopByReltUKeyIs(reltUKey_0);
		else if (dbName.equals(ContentName.onMon))
			reltMainT = reltMainReposMon.findTopByReltUKeyIs(reltUKey_0);
		else if (dbName.equals(ContentName.onHist))
			reltMainT = reltMainReposHist.findTopByReltUKeyIs(reltUKey_0);
		else
			reltMainT = reltMainRepos.findTopByReltUKeyIs(reltUKey_0);

		return reltMainT.isPresent() ? reltMainT.get() : null;
	}

	@Override
	public Slice<ReltMain> ReltUKeyEq(String reltUKey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ReltMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ReltUKeyEq " + dbName + " : " + "reltUKey_0 : " + reltUKey_0);
		if (dbName.equals(ContentName.onDay))
			slice = reltMainReposDay.findAllByReltUKeyIs(reltUKey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = reltMainReposMon.findAllByReltUKeyIs(reltUKey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = reltMainReposHist.findAllByReltUKeyIs(reltUKey_0, pageable);
		else
			slice = reltMainRepos.findAllByReltUKeyIs(reltUKey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ReltMain caseNoFirst(int caseNo_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("caseNoFirst " + dbName + " : " + "caseNo_0 : " + caseNo_0);
		Optional<ReltMain> reltMainT = null;
		if (dbName.equals(ContentName.onDay))
			reltMainT = reltMainReposDay.findTopByCaseNoIs(caseNo_0);
		else if (dbName.equals(ContentName.onMon))
			reltMainT = reltMainReposMon.findTopByCaseNoIs(caseNo_0);
		else if (dbName.equals(ContentName.onHist))
			reltMainT = reltMainReposHist.findTopByCaseNoIs(caseNo_0);
		else
			reltMainT = reltMainRepos.findTopByCaseNoIs(caseNo_0);

		return reltMainT.isPresent() ? reltMainT.get() : null;
	}

	@Override
	public Slice<ReltMain> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ReltMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = reltMainReposDay.findAllByCustNoIsOrderByCaseNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = reltMainReposMon.findAllByCustNoIsOrderByCaseNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = reltMainReposHist.findAllByCustNoIsOrderByCaseNoAsc(custNo_0, pageable);
		else
			slice = reltMainRepos.findAllByCustNoIsOrderByCaseNoAsc(custNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ReltMain custNoFirst(int custNo_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("custNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0);
		Optional<ReltMain> reltMainT = null;
		if (dbName.equals(ContentName.onDay))
			reltMainT = reltMainReposDay.findTopByCustNoIs(custNo_0);
		else if (dbName.equals(ContentName.onMon))
			reltMainT = reltMainReposMon.findTopByCustNoIs(custNo_0);
		else if (dbName.equals(ContentName.onHist))
			reltMainT = reltMainReposHist.findTopByCustNoIs(custNo_0);
		else
			reltMainT = reltMainRepos.findTopByCustNoIs(custNo_0);

		return reltMainT.isPresent() ? reltMainT.get() : null;
	}

	@Override
	public Slice<ReltMain> caseNoEq(int caseNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ReltMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("caseNoEq " + dbName + " : " + "caseNo_0 : " + caseNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = reltMainReposDay.findAllByCaseNoIsOrderByCustNoAsc(caseNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = reltMainReposMon.findAllByCaseNoIsOrderByCustNoAsc(caseNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = reltMainReposHist.findAllByCaseNoIsOrderByCustNoAsc(caseNo_0, pageable);
		else
			slice = reltMainRepos.findAllByCaseNoIsOrderByCustNoAsc(caseNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ReltMain> findByBoth(int caseNo_0, int custNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ReltMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByBoth " + dbName + " : " + "caseNo_0 : " + caseNo_0 + " custNo_1 : " + custNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = reltMainReposDay.findAllByCaseNoIsAndCustNoIs(caseNo_0, custNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = reltMainReposMon.findAllByCaseNoIsAndCustNoIs(caseNo_0, custNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = reltMainReposHist.findAllByCaseNoIsAndCustNoIs(caseNo_0, custNo_1, pageable);
		else
			slice = reltMainRepos.findAllByCaseNoIsAndCustNoIs(caseNo_0, custNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ReltMain holdById(ReltMainId reltMainId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + reltMainId);
		Optional<ReltMain> reltMain = null;
		if (dbName.equals(ContentName.onDay))
			reltMain = reltMainReposDay.findByReltMainId(reltMainId);
		else if (dbName.equals(ContentName.onMon))
			reltMain = reltMainReposMon.findByReltMainId(reltMainId);
		else if (dbName.equals(ContentName.onHist))
			reltMain = reltMainReposHist.findByReltMainId(reltMainId);
		else
			reltMain = reltMainRepos.findByReltMainId(reltMainId);
		return reltMain.isPresent() ? reltMain.get() : null;
	}

	@Override
	public ReltMain holdById(ReltMain reltMain, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + reltMain.getReltMainId());
		Optional<ReltMain> reltMainT = null;
		if (dbName.equals(ContentName.onDay))
			reltMainT = reltMainReposDay.findByReltMainId(reltMain.getReltMainId());
		else if (dbName.equals(ContentName.onMon))
			reltMainT = reltMainReposMon.findByReltMainId(reltMain.getReltMainId());
		else if (dbName.equals(ContentName.onHist))
			reltMainT = reltMainReposHist.findByReltMainId(reltMain.getReltMainId());
		else
			reltMainT = reltMainRepos.findByReltMainId(reltMain.getReltMainId());
		return reltMainT.isPresent() ? reltMainT.get() : null;
	}

	@Override
	public ReltMain insert(ReltMain reltMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + reltMain.getReltMainId());
		if (this.findById(reltMain.getReltMainId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			reltMain.setCreateEmpNo(empNot);

		if (reltMain.getLastUpdateEmpNo() == null || reltMain.getLastUpdateEmpNo().isEmpty())
			reltMain.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return reltMainReposDay.saveAndFlush(reltMain);
		else if (dbName.equals(ContentName.onMon))
			return reltMainReposMon.saveAndFlush(reltMain);
		else if (dbName.equals(ContentName.onHist))
			return reltMainReposHist.saveAndFlush(reltMain);
		else
			return reltMainRepos.saveAndFlush(reltMain);
	}

	@Override
	public ReltMain update(ReltMain reltMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + reltMain.getReltMainId());
		if (!empNot.isEmpty())
			reltMain.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return reltMainReposDay.saveAndFlush(reltMain);
		else if (dbName.equals(ContentName.onMon))
			return reltMainReposMon.saveAndFlush(reltMain);
		else if (dbName.equals(ContentName.onHist))
			return reltMainReposHist.saveAndFlush(reltMain);
		else
			return reltMainRepos.saveAndFlush(reltMain);
	}

	@Override
	public ReltMain update2(ReltMain reltMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + reltMain.getReltMainId());
		if (!empNot.isEmpty())
			reltMain.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			reltMainReposDay.saveAndFlush(reltMain);
		else if (dbName.equals(ContentName.onMon))
			reltMainReposMon.saveAndFlush(reltMain);
		else if (dbName.equals(ContentName.onHist))
			reltMainReposHist.saveAndFlush(reltMain);
		else
			reltMainRepos.saveAndFlush(reltMain);
		return this.findById(reltMain.getReltMainId());
	}

	@Override
	public void delete(ReltMain reltMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + reltMain.getReltMainId());
		if (dbName.equals(ContentName.onDay)) {
			reltMainReposDay.delete(reltMain);
			reltMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltMainReposMon.delete(reltMain);
			reltMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltMainReposHist.delete(reltMain);
			reltMainReposHist.flush();
		} else {
			reltMainRepos.delete(reltMain);
			reltMainRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ReltMain> reltMain, TitaVo... titaVo) throws DBException {
		if (reltMain == null || reltMain.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (ReltMain t : reltMain) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			reltMain = reltMainReposDay.saveAll(reltMain);
			reltMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltMain = reltMainReposMon.saveAll(reltMain);
			reltMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltMain = reltMainReposHist.saveAll(reltMain);
			reltMainReposHist.flush();
		} else {
			reltMain = reltMainRepos.saveAll(reltMain);
			reltMainRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ReltMain> reltMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (reltMain == null || reltMain.size() == 0)
			throw new DBException(6);

		for (ReltMain t : reltMain)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			reltMain = reltMainReposDay.saveAll(reltMain);
			reltMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltMain = reltMainReposMon.saveAll(reltMain);
			reltMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltMain = reltMainReposHist.saveAll(reltMain);
			reltMainReposHist.flush();
		} else {
			reltMain = reltMainRepos.saveAll(reltMain);
			reltMainRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ReltMain> reltMain, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (reltMain == null || reltMain.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			reltMainReposDay.deleteAll(reltMain);
			reltMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			reltMainReposMon.deleteAll(reltMain);
			reltMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			reltMainReposHist.deleteAll(reltMain);
			reltMainReposHist.flush();
		} else {
			reltMainRepos.deleteAll(reltMain);
			reltMainRepos.flush();
		}
	}

}
