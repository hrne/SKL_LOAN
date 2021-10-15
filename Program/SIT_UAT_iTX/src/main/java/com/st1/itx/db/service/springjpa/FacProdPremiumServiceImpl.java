package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

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
import com.st1.itx.db.domain.FacProdPremium;
import com.st1.itx.db.domain.FacProdPremiumId;
import com.st1.itx.db.repository.online.FacProdPremiumRepository;
import com.st1.itx.db.repository.day.FacProdPremiumRepositoryDay;
import com.st1.itx.db.repository.mon.FacProdPremiumRepositoryMon;
import com.st1.itx.db.repository.hist.FacProdPremiumRepositoryHist;
import com.st1.itx.db.service.FacProdPremiumService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facProdPremiumService")
@Repository
public class FacProdPremiumServiceImpl extends ASpringJpaParm implements FacProdPremiumService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private FacProdPremiumRepository facProdPremiumRepos;

	@Autowired
	private FacProdPremiumRepositoryDay facProdPremiumReposDay;

	@Autowired
	private FacProdPremiumRepositoryMon facProdPremiumReposMon;

	@Autowired
	private FacProdPremiumRepositoryHist facProdPremiumReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(facProdPremiumRepos);
		org.junit.Assert.assertNotNull(facProdPremiumReposDay);
		org.junit.Assert.assertNotNull(facProdPremiumReposMon);
		org.junit.Assert.assertNotNull(facProdPremiumReposHist);
	}

	@Override
	public FacProdPremium findById(FacProdPremiumId facProdPremiumId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + facProdPremiumId);
		Optional<FacProdPremium> facProdPremium = null;
		if (dbName.equals(ContentName.onDay))
			facProdPremium = facProdPremiumReposDay.findById(facProdPremiumId);
		else if (dbName.equals(ContentName.onMon))
			facProdPremium = facProdPremiumReposMon.findById(facProdPremiumId);
		else if (dbName.equals(ContentName.onHist))
			facProdPremium = facProdPremiumReposHist.findById(facProdPremiumId);
		else
			facProdPremium = facProdPremiumRepos.findById(facProdPremiumId);
		FacProdPremium obj = facProdPremium.isPresent() ? facProdPremium.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<FacProdPremium> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacProdPremium> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ProdNo", "PremiumLow"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = facProdPremiumReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facProdPremiumReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facProdPremiumReposHist.findAll(pageable);
		else
			slice = facProdPremiumRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacProdPremium> premiumProdNoEq(String prodNo_0, BigDecimal premiumLow_1, BigDecimal premiumLow_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacProdPremium> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("premiumProdNoEq " + dbName + " : " + "prodNo_0 : " + prodNo_0 + " premiumLow_1 : " + premiumLow_1 + " premiumLow_2 : " + premiumLow_2);
		if (dbName.equals(ContentName.onDay))
			slice = facProdPremiumReposDay.findAllByProdNoIsAndPremiumLowGreaterThanEqualAndPremiumLowLessThanEqual(prodNo_0, premiumLow_1, premiumLow_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facProdPremiumReposMon.findAllByProdNoIsAndPremiumLowGreaterThanEqualAndPremiumLowLessThanEqual(prodNo_0, premiumLow_1, premiumLow_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facProdPremiumReposHist.findAllByProdNoIsAndPremiumLowGreaterThanEqualAndPremiumLowLessThanEqual(prodNo_0, premiumLow_1, premiumLow_2, pageable);
		else
			slice = facProdPremiumRepos.findAllByProdNoIsAndPremiumLowGreaterThanEqualAndPremiumLowLessThanEqual(prodNo_0, premiumLow_1, premiumLow_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacProdPremium holdById(FacProdPremiumId facProdPremiumId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facProdPremiumId);
		Optional<FacProdPremium> facProdPremium = null;
		if (dbName.equals(ContentName.onDay))
			facProdPremium = facProdPremiumReposDay.findByFacProdPremiumId(facProdPremiumId);
		else if (dbName.equals(ContentName.onMon))
			facProdPremium = facProdPremiumReposMon.findByFacProdPremiumId(facProdPremiumId);
		else if (dbName.equals(ContentName.onHist))
			facProdPremium = facProdPremiumReposHist.findByFacProdPremiumId(facProdPremiumId);
		else
			facProdPremium = facProdPremiumRepos.findByFacProdPremiumId(facProdPremiumId);
		return facProdPremium.isPresent() ? facProdPremium.get() : null;
	}

	@Override
	public FacProdPremium holdById(FacProdPremium facProdPremium, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facProdPremium.getFacProdPremiumId());
		Optional<FacProdPremium> facProdPremiumT = null;
		if (dbName.equals(ContentName.onDay))
			facProdPremiumT = facProdPremiumReposDay.findByFacProdPremiumId(facProdPremium.getFacProdPremiumId());
		else if (dbName.equals(ContentName.onMon))
			facProdPremiumT = facProdPremiumReposMon.findByFacProdPremiumId(facProdPremium.getFacProdPremiumId());
		else if (dbName.equals(ContentName.onHist))
			facProdPremiumT = facProdPremiumReposHist.findByFacProdPremiumId(facProdPremium.getFacProdPremiumId());
		else
			facProdPremiumT = facProdPremiumRepos.findByFacProdPremiumId(facProdPremium.getFacProdPremiumId());
		return facProdPremiumT.isPresent() ? facProdPremiumT.get() : null;
	}

	@Override
	public FacProdPremium insert(FacProdPremium facProdPremium, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + facProdPremium.getFacProdPremiumId());
		if (this.findById(facProdPremium.getFacProdPremiumId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			facProdPremium.setCreateEmpNo(empNot);

		if (facProdPremium.getLastUpdateEmpNo() == null || facProdPremium.getLastUpdateEmpNo().isEmpty())
			facProdPremium.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facProdPremiumReposDay.saveAndFlush(facProdPremium);
		else if (dbName.equals(ContentName.onMon))
			return facProdPremiumReposMon.saveAndFlush(facProdPremium);
		else if (dbName.equals(ContentName.onHist))
			return facProdPremiumReposHist.saveAndFlush(facProdPremium);
		else
			return facProdPremiumRepos.saveAndFlush(facProdPremium);
	}

	@Override
	public FacProdPremium update(FacProdPremium facProdPremium, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facProdPremium.getFacProdPremiumId());
		if (!empNot.isEmpty())
			facProdPremium.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facProdPremiumReposDay.saveAndFlush(facProdPremium);
		else if (dbName.equals(ContentName.onMon))
			return facProdPremiumReposMon.saveAndFlush(facProdPremium);
		else if (dbName.equals(ContentName.onHist))
			return facProdPremiumReposHist.saveAndFlush(facProdPremium);
		else
			return facProdPremiumRepos.saveAndFlush(facProdPremium);
	}

	@Override
	public FacProdPremium update2(FacProdPremium facProdPremium, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facProdPremium.getFacProdPremiumId());
		if (!empNot.isEmpty())
			facProdPremium.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			facProdPremiumReposDay.saveAndFlush(facProdPremium);
		else if (dbName.equals(ContentName.onMon))
			facProdPremiumReposMon.saveAndFlush(facProdPremium);
		else if (dbName.equals(ContentName.onHist))
			facProdPremiumReposHist.saveAndFlush(facProdPremium);
		else
			facProdPremiumRepos.saveAndFlush(facProdPremium);
		return this.findById(facProdPremium.getFacProdPremiumId());
	}

	@Override
	public void delete(FacProdPremium facProdPremium, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + facProdPremium.getFacProdPremiumId());
		if (dbName.equals(ContentName.onDay)) {
			facProdPremiumReposDay.delete(facProdPremium);
			facProdPremiumReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdPremiumReposMon.delete(facProdPremium);
			facProdPremiumReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdPremiumReposHist.delete(facProdPremium);
			facProdPremiumReposHist.flush();
		} else {
			facProdPremiumRepos.delete(facProdPremium);
			facProdPremiumRepos.flush();
		}
	}

	@Override
	public void insertAll(List<FacProdPremium> facProdPremium, TitaVo... titaVo) throws DBException {
		if (facProdPremium == null || facProdPremium.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (FacProdPremium t : facProdPremium) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			facProdPremium = facProdPremiumReposDay.saveAll(facProdPremium);
			facProdPremiumReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdPremium = facProdPremiumReposMon.saveAll(facProdPremium);
			facProdPremiumReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdPremium = facProdPremiumReposHist.saveAll(facProdPremium);
			facProdPremiumReposHist.flush();
		} else {
			facProdPremium = facProdPremiumRepos.saveAll(facProdPremium);
			facProdPremiumRepos.flush();
		}
	}

	@Override
	public void updateAll(List<FacProdPremium> facProdPremium, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (facProdPremium == null || facProdPremium.size() == 0)
			throw new DBException(6);

		for (FacProdPremium t : facProdPremium)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			facProdPremium = facProdPremiumReposDay.saveAll(facProdPremium);
			facProdPremiumReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdPremium = facProdPremiumReposMon.saveAll(facProdPremium);
			facProdPremiumReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdPremium = facProdPremiumReposHist.saveAll(facProdPremium);
			facProdPremiumReposHist.flush();
		} else {
			facProdPremium = facProdPremiumRepos.saveAll(facProdPremium);
			facProdPremiumRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<FacProdPremium> facProdPremium, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (facProdPremium == null || facProdPremium.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			facProdPremiumReposDay.deleteAll(facProdPremium);
			facProdPremiumReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facProdPremiumReposMon.deleteAll(facProdPremium);
			facProdPremiumReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facProdPremiumReposHist.deleteAll(facProdPremium);
			facProdPremiumReposHist.flush();
		} else {
			facProdPremiumRepos.deleteAll(facProdPremium);
			facProdPremiumRepos.flush();
		}
	}

}
