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
import com.st1.itx.db.domain.FacProdStepRate;
import com.st1.itx.db.domain.FacProdStepRateId;
import com.st1.itx.db.repository.online.FacProdStepRateRepository;
import com.st1.itx.db.repository.day.FacProdStepRateRepositoryDay;
import com.st1.itx.db.repository.mon.FacProdStepRateRepositoryMon;
import com.st1.itx.db.repository.hist.FacProdStepRateRepositoryHist;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facProdStepRateService")
@Repository
public class FacProdStepRateServiceImpl extends ASpringJpaParm implements FacProdStepRateService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private FacProdStepRateRepository facProdStepRateRepos;

	@Autowired
	private FacProdStepRateRepositoryDay facProdStepRateReposDay;

	@Autowired
	private FacProdStepRateRepositoryMon facProdStepRateReposMon;

	@Autowired
	private FacProdStepRateRepositoryHist facProdStepRateReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(facProdStepRateRepos);
		org.junit.Assert.assertNotNull(facProdStepRateReposDay);
		org.junit.Assert.assertNotNull(facProdStepRateReposMon);
		org.junit.Assert.assertNotNull(facProdStepRateReposHist);
	}

	@Override
	public FacProdStepRate findById(FacProdStepRateId facProdStepRateId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + facProdStepRateId);
		Optional<FacProdStepRate> facProdStepRate = null;
		if (dbName.equals(ContentName.onDay))
			facProdStepRate = facProdStepRateReposDay.findById(facProdStepRateId);
		else if (dbName.equals(ContentName.onMon))
			facProdStepRate = facProdStepRateReposMon.findById(facProdStepRateId);
		else if (dbName.equals(ContentName.onHist))
			facProdStepRate = facProdStepRateReposHist.findById(facProdStepRateId);
		else
			facProdStepRate = facProdStepRateRepos.findById(facProdStepRateId);
		FacProdStepRate obj = facProdStepRate.isPresent() ? facProdStepRate.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<FacProdStepRate> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacProdStepRate> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ProdNo", "MonthStart"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = facProdStepRateReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facProdStepRateReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facProdStepRateReposHist.findAll(pageable);
		else
			slice = facProdStepRateRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacProdStepRate> stepRateProdNoEq(String prodNo_0, int monthStart_1, int monthStart_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacProdStepRate> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("stepRateProdNoEq " + dbName + " : " + "prodNo_0 : " + prodNo_0 + " monthStart_1 : " + monthStart_1 + " monthStart_2 : " + monthStart_2);
		if (dbName.equals(ContentName.onDay))
			slice = facProdStepRateReposDay.findAllByProdNoIsAndMonthStartGreaterThanEqualAndMonthStartLessThanEqual(prodNo_0, monthStart_1, monthStart_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facProdStepRateReposMon.findAllByProdNoIsAndMonthStartGreaterThanEqualAndMonthStartLessThanEqual(prodNo_0, monthStart_1, monthStart_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facProdStepRateReposHist.findAllByProdNoIsAndMonthStartGreaterThanEqualAndMonthStartLessThanEqual(prodNo_0, monthStart_1, monthStart_2, pageable);
		else
			slice = facProdStepRateRepos.findAllByProdNoIsAndMonthStartGreaterThanEqualAndMonthStartLessThanEqual(prodNo_0, monthStart_1, monthStart_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacProdStepRate holdById(FacProdStepRateId facProdStepRateId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facProdStepRateId);
		Optional<FacProdStepRate> facProdStepRate = null;
		if (dbName.equals(ContentName.onDay))
			facProdStepRate = facProdStepRateReposDay.findByFacProdStepRateId(facProdStepRateId);
		else if (dbName.equals(ContentName.onMon))
			facProdStepRate = facProdStepRateReposMon.findByFacProdStepRateId(facProdStepRateId);
		else if (dbName.equals(ContentName.onHist))
			facProdStepRate = facProdStepRateReposHist.findByFacProdStepRateId(facProdStepRateId);
		else
			facProdStepRate = facProdStepRateRepos.findByFacProdStepRateId(facProdStepRateId);
		return facProdStepRate.isPresent() ? facProdStepRate.get() : null;
	}

	@Override
	public FacProdStepRate holdById(FacProdStepRate facProdStepRate, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facProdStepRate.getFacProdStepRateId());
		Optional<FacProdStepRate> facProdStepRateT = null;
		if (dbName.equals(ContentName.onDay))
			facProdStepRateT = facProdStepRateReposDay.findByFacProdStepRateId(facProdStepRate.getFacProdStepRateId());
		else if (dbName.equals(ContentName.onMon))
			facProdStepRateT = facProdStepRateReposMon.findByFacProdStepRateId(facProdStepRate.getFacProdStepRateId());
		else if (dbName.equals(ContentName.onHist))
			facProdStepRateT = facProdStepRateReposHist.findByFacProdStepRateId(facProdStepRate.getFacProdStepRateId());
		else
			facProdStepRateT = facProdStepRateRepos.findByFacProdStepRateId(facProdStepRate.getFacProdStepRateId());
		return facProdStepRateT.isPresent() ? facProdStepRateT.get() : null;
	}

	@Override
	public FacProdStepRate insert(FacProdStepRate facProdStepRate, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + facProdStepRate.getFacProdStepRateId());
		if (this.findById(facProdStepRate.getFacProdStepRateId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			facProdStepRate.setCreateEmpNo(empNot);

		if (facProdStepRate.getLastUpdateEmpNo() == null || facProdStepRate.getLastUpdateEmpNo().isEmpty())
			facProdStepRate.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facProdStepRateReposDay.saveAndFlush(facProdStepRate);
		else if (dbName.equals(ContentName.onMon))
			return facProdStepRateReposMon.saveAndFlush(facProdStepRate);
		else if (dbName.equals(ContentName.onHist))
			return facProdStepRateReposHist.saveAndFlush(facProdStepRate);
		else
			return facProdStepRateRepos.saveAndFlush(facProdStepRate);
	}

	@Override
	public FacProdStepRate update(FacProdStepRate facProdStepRate, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facProdStepRate.getFacProdStepRateId());
		if (!empNot.isEmpty())
			facProdStepRate.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facProdStepRateReposDay.saveAndFlush(facProdStepRate);
		else if (dbName.equals(ContentName.onMon))
			return facProdStepRateReposMon.saveAndFlush(facProdStepRate);
		else if (dbName.equals(ContentName.onHist))
			return facProdStepRateReposHist.saveAndFlush(facProdStepRate);
		else
			return facProdStepRateRepos.saveAndFlush(facProdStepRate);
	}

	@Override
	public FacProdStepRate update2(FacProdStepRate facProdStepRate, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facProdStepRate.getFacProdStepRateId());
		if (!empNot.isEmpty())
			facProdStepRate.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			facProdStepRateReposDay.saveAndFlush(facProdStepRate);
		else if (dbName.equals(ContentName.onMon))
			facProdStepRateReposMon.saveAndFlush(facProdStepRate);
		else if (dbName.equals(ContentName.onHist))
			facProdStepRateReposHist.saveAndFlush(facProdStepRate);
		else
			facProdStepRateRepos.saveAndFlush(facProdStepRate);
		return this.findById(facProdStepRate.getFacProdStepRateId());
	}

	@Override
	public void delete(FacProdStepRate facProdStepRate, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + facProdStepRate.getFacProdStepRateId());
		if (dbName.equals(ContentName.onDay)) {
			facProdStepRateReposDay.delete(facProdStepRate);
			facProdStepRateReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdStepRateReposMon.delete(facProdStepRate);
			facProdStepRateReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdStepRateReposHist.delete(facProdStepRate);
			facProdStepRateReposHist.flush();
		} else {
			facProdStepRateRepos.delete(facProdStepRate);
			facProdStepRateRepos.flush();
		}
	}

	@Override
	public void insertAll(List<FacProdStepRate> facProdStepRate, TitaVo... titaVo) throws DBException {
		if (facProdStepRate == null || facProdStepRate.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (FacProdStepRate t : facProdStepRate) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			facProdStepRate = facProdStepRateReposDay.saveAll(facProdStepRate);
			facProdStepRateReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdStepRate = facProdStepRateReposMon.saveAll(facProdStepRate);
			facProdStepRateReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdStepRate = facProdStepRateReposHist.saveAll(facProdStepRate);
			facProdStepRateReposHist.flush();
		} else {
			facProdStepRate = facProdStepRateRepos.saveAll(facProdStepRate);
			facProdStepRateRepos.flush();
		}
	}

	@Override
	public void updateAll(List<FacProdStepRate> facProdStepRate, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (facProdStepRate == null || facProdStepRate.size() == 0)
			throw new DBException(6);

		for (FacProdStepRate t : facProdStepRate)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			facProdStepRate = facProdStepRateReposDay.saveAll(facProdStepRate);
			facProdStepRateReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdStepRate = facProdStepRateReposMon.saveAll(facProdStepRate);
			facProdStepRateReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdStepRate = facProdStepRateReposHist.saveAll(facProdStepRate);
			facProdStepRateReposHist.flush();
		} else {
			facProdStepRate = facProdStepRateRepos.saveAll(facProdStepRate);
			facProdStepRateRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<FacProdStepRate> facProdStepRate, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (facProdStepRate == null || facProdStepRate.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			facProdStepRateReposDay.deleteAll(facProdStepRate);
			facProdStepRateReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdStepRateReposMon.deleteAll(facProdStepRate);
			facProdStepRateReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdStepRateReposHist.deleteAll(facProdStepRate);
			facProdStepRateReposHist.flush();
		} else {
			facProdStepRateRepos.deleteAll(facProdStepRate);
			facProdStepRateRepos.flush();
		}
	}

}
