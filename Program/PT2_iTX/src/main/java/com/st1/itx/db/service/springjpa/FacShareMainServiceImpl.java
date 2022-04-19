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
import com.st1.itx.db.domain.FacShareMain;
import com.st1.itx.db.domain.FacShareMainId;
import com.st1.itx.db.repository.online.FacShareMainRepository;
import com.st1.itx.db.repository.day.FacShareMainRepositoryDay;
import com.st1.itx.db.repository.mon.FacShareMainRepositoryMon;
import com.st1.itx.db.repository.hist.FacShareMainRepositoryHist;
import com.st1.itx.db.service.FacShareMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facShareMainService")
@Repository
public class FacShareMainServiceImpl extends ASpringJpaParm implements FacShareMainService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private FacShareMainRepository facShareMainRepos;

	@Autowired
	private FacShareMainRepositoryDay facShareMainReposDay;

	@Autowired
	private FacShareMainRepositoryMon facShareMainReposMon;

	@Autowired
	private FacShareMainRepositoryHist facShareMainReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(facShareMainRepos);
		org.junit.Assert.assertNotNull(facShareMainReposDay);
		org.junit.Assert.assertNotNull(facShareMainReposMon);
		org.junit.Assert.assertNotNull(facShareMainReposHist);
	}

	@Override
	public FacShareMain findById(FacShareMainId facShareMainId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + facShareMainId);
		Optional<FacShareMain> facShareMain = null;
		if (dbName.equals(ContentName.onDay))
			facShareMain = facShareMainReposDay.findById(facShareMainId);
		else if (dbName.equals(ContentName.onMon))
			facShareMain = facShareMainReposMon.findById(facShareMainId);
		else if (dbName.equals(ContentName.onHist))
			facShareMain = facShareMainReposHist.findById(facShareMainId);
		else
			facShareMain = facShareMainRepos.findById(facShareMainId);
		FacShareMain obj = facShareMain.isPresent() ? facShareMain.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<FacShareMain> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacShareMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "CreditSysNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = facShareMainReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facShareMainReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facShareMainReposHist.findAll(pageable);
		else
			slice = facShareMainRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacShareMain> findCreditSysNoEq(int creditSysNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacShareMain> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCreditSysNoEq " + dbName + " : " + "creditSysNo_0 : " + creditSysNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = facShareMainReposDay.findAllByCreditSysNoIsOrderByCustNoAsc(creditSysNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facShareMainReposMon.findAllByCreditSysNoIsOrderByCustNoAsc(creditSysNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facShareMainReposHist.findAllByCreditSysNoIsOrderByCustNoAsc(creditSysNo_0, pageable);
		else
			slice = facShareMainRepos.findAllByCreditSysNoIsOrderByCustNoAsc(creditSysNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacShareMain holdById(FacShareMainId facShareMainId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facShareMainId);
		Optional<FacShareMain> facShareMain = null;
		if (dbName.equals(ContentName.onDay))
			facShareMain = facShareMainReposDay.findByFacShareMainId(facShareMainId);
		else if (dbName.equals(ContentName.onMon))
			facShareMain = facShareMainReposMon.findByFacShareMainId(facShareMainId);
		else if (dbName.equals(ContentName.onHist))
			facShareMain = facShareMainReposHist.findByFacShareMainId(facShareMainId);
		else
			facShareMain = facShareMainRepos.findByFacShareMainId(facShareMainId);
		return facShareMain.isPresent() ? facShareMain.get() : null;
	}

	@Override
	public FacShareMain holdById(FacShareMain facShareMain, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facShareMain.getFacShareMainId());
		Optional<FacShareMain> facShareMainT = null;
		if (dbName.equals(ContentName.onDay))
			facShareMainT = facShareMainReposDay.findByFacShareMainId(facShareMain.getFacShareMainId());
		else if (dbName.equals(ContentName.onMon))
			facShareMainT = facShareMainReposMon.findByFacShareMainId(facShareMain.getFacShareMainId());
		else if (dbName.equals(ContentName.onHist))
			facShareMainT = facShareMainReposHist.findByFacShareMainId(facShareMain.getFacShareMainId());
		else
			facShareMainT = facShareMainRepos.findByFacShareMainId(facShareMain.getFacShareMainId());
		return facShareMainT.isPresent() ? facShareMainT.get() : null;
	}

	@Override
	public FacShareMain insert(FacShareMain facShareMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + facShareMain.getFacShareMainId());
		if (this.findById(facShareMain.getFacShareMainId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			facShareMain.setCreateEmpNo(empNot);

		if (facShareMain.getLastUpdateEmpNo() == null || facShareMain.getLastUpdateEmpNo().isEmpty())
			facShareMain.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facShareMainReposDay.saveAndFlush(facShareMain);
		else if (dbName.equals(ContentName.onMon))
			return facShareMainReposMon.saveAndFlush(facShareMain);
		else if (dbName.equals(ContentName.onHist))
			return facShareMainReposHist.saveAndFlush(facShareMain);
		else
			return facShareMainRepos.saveAndFlush(facShareMain);
	}

	@Override
	public FacShareMain update(FacShareMain facShareMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facShareMain.getFacShareMainId());
		if (!empNot.isEmpty())
			facShareMain.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facShareMainReposDay.saveAndFlush(facShareMain);
		else if (dbName.equals(ContentName.onMon))
			return facShareMainReposMon.saveAndFlush(facShareMain);
		else if (dbName.equals(ContentName.onHist))
			return facShareMainReposHist.saveAndFlush(facShareMain);
		else
			return facShareMainRepos.saveAndFlush(facShareMain);
	}

	@Override
	public FacShareMain update2(FacShareMain facShareMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facShareMain.getFacShareMainId());
		if (!empNot.isEmpty())
			facShareMain.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			facShareMainReposDay.saveAndFlush(facShareMain);
		else if (dbName.equals(ContentName.onMon))
			facShareMainReposMon.saveAndFlush(facShareMain);
		else if (dbName.equals(ContentName.onHist))
			facShareMainReposHist.saveAndFlush(facShareMain);
		else
			facShareMainRepos.saveAndFlush(facShareMain);
		return this.findById(facShareMain.getFacShareMainId());
	}

	@Override
	public void delete(FacShareMain facShareMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + facShareMain.getFacShareMainId());
		if (dbName.equals(ContentName.onDay)) {
			facShareMainReposDay.delete(facShareMain);
			facShareMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareMainReposMon.delete(facShareMain);
			facShareMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareMainReposHist.delete(facShareMain);
			facShareMainReposHist.flush();
		} else {
			facShareMainRepos.delete(facShareMain);
			facShareMainRepos.flush();
		}
	}

	@Override
	public void insertAll(List<FacShareMain> facShareMain, TitaVo... titaVo) throws DBException {
		if (facShareMain == null || facShareMain.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (FacShareMain t : facShareMain) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			facShareMain = facShareMainReposDay.saveAll(facShareMain);
			facShareMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareMain = facShareMainReposMon.saveAll(facShareMain);
			facShareMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareMain = facShareMainReposHist.saveAll(facShareMain);
			facShareMainReposHist.flush();
		} else {
			facShareMain = facShareMainRepos.saveAll(facShareMain);
			facShareMainRepos.flush();
		}
	}

	@Override
	public void updateAll(List<FacShareMain> facShareMain, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (facShareMain == null || facShareMain.size() == 0)
			throw new DBException(6);

		for (FacShareMain t : facShareMain)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			facShareMain = facShareMainReposDay.saveAll(facShareMain);
			facShareMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareMain = facShareMainReposMon.saveAll(facShareMain);
			facShareMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareMain = facShareMainReposHist.saveAll(facShareMain);
			facShareMainReposHist.flush();
		} else {
			facShareMain = facShareMainRepos.saveAll(facShareMain);
			facShareMainRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<FacShareMain> facShareMain, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (facShareMain == null || facShareMain.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			facShareMainReposDay.deleteAll(facShareMain);
			facShareMainReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facShareMainReposMon.deleteAll(facShareMain);
			facShareMainReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facShareMainReposHist.deleteAll(facShareMain);
			facShareMainReposHist.flush();
		} else {
			facShareMainRepos.deleteAll(facShareMain);
			facShareMainRepos.flush();
		}
	}

}
