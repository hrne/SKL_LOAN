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
import com.st1.itx.db.domain.FacProdBreach;
import com.st1.itx.db.domain.FacProdBreachId;
import com.st1.itx.db.repository.online.FacProdBreachRepository;
import com.st1.itx.db.repository.day.FacProdBreachRepositoryDay;
import com.st1.itx.db.repository.mon.FacProdBreachRepositoryMon;
import com.st1.itx.db.repository.hist.FacProdBreachRepositoryHist;
import com.st1.itx.db.service.FacProdBreachService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facProdBreachService")
@Repository
public class FacProdBreachServiceImpl extends ASpringJpaParm implements FacProdBreachService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private FacProdBreachRepository facProdBreachRepos;

	@Autowired
	private FacProdBreachRepositoryDay facProdBreachReposDay;

	@Autowired
	private FacProdBreachRepositoryMon facProdBreachReposMon;

	@Autowired
	private FacProdBreachRepositoryHist facProdBreachReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(facProdBreachRepos);
		org.junit.Assert.assertNotNull(facProdBreachReposDay);
		org.junit.Assert.assertNotNull(facProdBreachReposMon);
		org.junit.Assert.assertNotNull(facProdBreachReposHist);
	}

	@Override
	public FacProdBreach findById(FacProdBreachId facProdBreachId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + facProdBreachId);
		Optional<FacProdBreach> facProdBreach = null;
		if (dbName.equals(ContentName.onDay))
			facProdBreach = facProdBreachReposDay.findById(facProdBreachId);
		else if (dbName.equals(ContentName.onMon))
			facProdBreach = facProdBreachReposMon.findById(facProdBreachId);
		else if (dbName.equals(ContentName.onHist))
			facProdBreach = facProdBreachReposHist.findById(facProdBreachId);
		else
			facProdBreach = facProdBreachRepos.findById(facProdBreachId);
		FacProdBreach obj = facProdBreach.isPresent() ? facProdBreach.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<FacProdBreach> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacProdBreach> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BreachNo", "BreachCode", "MonthStart"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = facProdBreachReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facProdBreachReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facProdBreachReposHist.findAll(pageable);
		else
			slice = facProdBreachRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacProdBreach> breachNoEq(String breachNo_0, String breachCode_1, String breachCode_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacProdBreach> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("breachNoEq " + dbName + " : " + "breachNo_0 : " + breachNo_0 + " breachCode_1 : " + breachCode_1 + " breachCode_2 : " + breachCode_2);
		if (dbName.equals(ContentName.onDay))
			slice = facProdBreachReposDay.findAllByBreachNoIsAndBreachCodeGreaterThanEqualAndBreachCodeLessThanEqual(breachNo_0, breachCode_1, breachCode_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facProdBreachReposMon.findAllByBreachNoIsAndBreachCodeGreaterThanEqualAndBreachCodeLessThanEqual(breachNo_0, breachCode_1, breachCode_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facProdBreachReposHist.findAllByBreachNoIsAndBreachCodeGreaterThanEqualAndBreachCodeLessThanEqual(breachNo_0, breachCode_1, breachCode_2, pageable);
		else
			slice = facProdBreachRepos.findAllByBreachNoIsAndBreachCodeGreaterThanEqualAndBreachCodeLessThanEqual(breachNo_0, breachCode_1, breachCode_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacProdBreach breachMonthFirst(String breachNo_0, String breachCode_1, int monthStart_2, int monthEnd_3, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("breachMonthFirst " + dbName + " : " + "breachNo_0 : " + breachNo_0 + " breachCode_1 : " + breachCode_1 + " monthStart_2 : " + monthStart_2 + " monthEnd_3 : " + monthEnd_3);
		Optional<FacProdBreach> facProdBreachT = null;
		if (dbName.equals(ContentName.onDay))
			facProdBreachT = facProdBreachReposDay.findTopByBreachNoIsAndBreachCodeIsAndMonthStartLessThanEqualAndMonthEndGreaterThan(breachNo_0, breachCode_1, monthStart_2, monthEnd_3);
		else if (dbName.equals(ContentName.onMon))
			facProdBreachT = facProdBreachReposMon.findTopByBreachNoIsAndBreachCodeIsAndMonthStartLessThanEqualAndMonthEndGreaterThan(breachNo_0, breachCode_1, monthStart_2, monthEnd_3);
		else if (dbName.equals(ContentName.onHist))
			facProdBreachT = facProdBreachReposHist.findTopByBreachNoIsAndBreachCodeIsAndMonthStartLessThanEqualAndMonthEndGreaterThan(breachNo_0, breachCode_1, monthStart_2, monthEnd_3);
		else
			facProdBreachT = facProdBreachRepos.findTopByBreachNoIsAndBreachCodeIsAndMonthStartLessThanEqualAndMonthEndGreaterThan(breachNo_0, breachCode_1, monthStart_2, monthEnd_3);
		return facProdBreachT.isPresent() ? facProdBreachT.get() : null;
	}

	@Override
	public FacProdBreach holdById(FacProdBreachId facProdBreachId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facProdBreachId);
		Optional<FacProdBreach> facProdBreach = null;
		if (dbName.equals(ContentName.onDay))
			facProdBreach = facProdBreachReposDay.findByFacProdBreachId(facProdBreachId);
		else if (dbName.equals(ContentName.onMon))
			facProdBreach = facProdBreachReposMon.findByFacProdBreachId(facProdBreachId);
		else if (dbName.equals(ContentName.onHist))
			facProdBreach = facProdBreachReposHist.findByFacProdBreachId(facProdBreachId);
		else
			facProdBreach = facProdBreachRepos.findByFacProdBreachId(facProdBreachId);
		return facProdBreach.isPresent() ? facProdBreach.get() : null;
	}

	@Override
	public FacProdBreach holdById(FacProdBreach facProdBreach, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facProdBreach.getFacProdBreachId());
		Optional<FacProdBreach> facProdBreachT = null;
		if (dbName.equals(ContentName.onDay))
			facProdBreachT = facProdBreachReposDay.findByFacProdBreachId(facProdBreach.getFacProdBreachId());
		else if (dbName.equals(ContentName.onMon))
			facProdBreachT = facProdBreachReposMon.findByFacProdBreachId(facProdBreach.getFacProdBreachId());
		else if (dbName.equals(ContentName.onHist))
			facProdBreachT = facProdBreachReposHist.findByFacProdBreachId(facProdBreach.getFacProdBreachId());
		else
			facProdBreachT = facProdBreachRepos.findByFacProdBreachId(facProdBreach.getFacProdBreachId());
		return facProdBreachT.isPresent() ? facProdBreachT.get() : null;
	}

	@Override
	public FacProdBreach insert(FacProdBreach facProdBreach, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + facProdBreach.getFacProdBreachId());
		if (this.findById(facProdBreach.getFacProdBreachId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			facProdBreach.setCreateEmpNo(empNot);

		if (facProdBreach.getLastUpdateEmpNo() == null || facProdBreach.getLastUpdateEmpNo().isEmpty())
			facProdBreach.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facProdBreachReposDay.saveAndFlush(facProdBreach);
		else if (dbName.equals(ContentName.onMon))
			return facProdBreachReposMon.saveAndFlush(facProdBreach);
		else if (dbName.equals(ContentName.onHist))
			return facProdBreachReposHist.saveAndFlush(facProdBreach);
		else
			return facProdBreachRepos.saveAndFlush(facProdBreach);
	}

	@Override
	public FacProdBreach update(FacProdBreach facProdBreach, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facProdBreach.getFacProdBreachId());
		if (!empNot.isEmpty())
			facProdBreach.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facProdBreachReposDay.saveAndFlush(facProdBreach);
		else if (dbName.equals(ContentName.onMon))
			return facProdBreachReposMon.saveAndFlush(facProdBreach);
		else if (dbName.equals(ContentName.onHist))
			return facProdBreachReposHist.saveAndFlush(facProdBreach);
		else
			return facProdBreachRepos.saveAndFlush(facProdBreach);
	}

	@Override
	public FacProdBreach update2(FacProdBreach facProdBreach, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facProdBreach.getFacProdBreachId());
		if (!empNot.isEmpty())
			facProdBreach.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			facProdBreachReposDay.saveAndFlush(facProdBreach);
		else if (dbName.equals(ContentName.onMon))
			facProdBreachReposMon.saveAndFlush(facProdBreach);
		else if (dbName.equals(ContentName.onHist))
			facProdBreachReposHist.saveAndFlush(facProdBreach);
		else
			facProdBreachRepos.saveAndFlush(facProdBreach);
		return this.findById(facProdBreach.getFacProdBreachId());
	}

	@Override
	public void delete(FacProdBreach facProdBreach, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + facProdBreach.getFacProdBreachId());
		if (dbName.equals(ContentName.onDay)) {
			facProdBreachReposDay.delete(facProdBreach);
			facProdBreachReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdBreachReposMon.delete(facProdBreach);
			facProdBreachReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdBreachReposHist.delete(facProdBreach);
			facProdBreachReposHist.flush();
		} else {
			facProdBreachRepos.delete(facProdBreach);
			facProdBreachRepos.flush();
		}
	}

	@Override
	public void insertAll(List<FacProdBreach> facProdBreach, TitaVo... titaVo) throws DBException {
		if (facProdBreach == null || facProdBreach.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (FacProdBreach t : facProdBreach) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			facProdBreach = facProdBreachReposDay.saveAll(facProdBreach);
			facProdBreachReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdBreach = facProdBreachReposMon.saveAll(facProdBreach);
			facProdBreachReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdBreach = facProdBreachReposHist.saveAll(facProdBreach);
			facProdBreachReposHist.flush();
		} else {
			facProdBreach = facProdBreachRepos.saveAll(facProdBreach);
			facProdBreachRepos.flush();
		}
	}

	@Override
	public void updateAll(List<FacProdBreach> facProdBreach, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (facProdBreach == null || facProdBreach.size() == 0)
			throw new DBException(6);

		for (FacProdBreach t : facProdBreach)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			facProdBreach = facProdBreachReposDay.saveAll(facProdBreach);
			facProdBreachReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdBreach = facProdBreachReposMon.saveAll(facProdBreach);
			facProdBreachReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdBreach = facProdBreachReposHist.saveAll(facProdBreach);
			facProdBreachReposHist.flush();
		} else {
			facProdBreach = facProdBreachRepos.saveAll(facProdBreach);
			facProdBreachRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<FacProdBreach> facProdBreach, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (facProdBreach == null || facProdBreach.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			facProdBreachReposDay.deleteAll(facProdBreach);
			facProdBreachReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdBreachReposMon.deleteAll(facProdBreach);
			facProdBreachReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdBreachReposHist.deleteAll(facProdBreach);
			facProdBreachReposHist.flush();
		} else {
			facProdBreachRepos.deleteAll(facProdBreach);
			facProdBreachRepos.flush();
		}
	}

}
