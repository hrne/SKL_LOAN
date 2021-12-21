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
import com.st1.itx.db.domain.CdBuildingCost;
import com.st1.itx.db.domain.CdBuildingCostId;
import com.st1.itx.db.repository.online.CdBuildingCostRepository;
import com.st1.itx.db.repository.day.CdBuildingCostRepositoryDay;
import com.st1.itx.db.repository.mon.CdBuildingCostRepositoryMon;
import com.st1.itx.db.repository.hist.CdBuildingCostRepositoryHist;
import com.st1.itx.db.service.CdBuildingCostService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdBuildingCostService")
@Repository
public class CdBuildingCostServiceImpl extends ASpringJpaParm implements CdBuildingCostService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdBuildingCostRepository cdBuildingCostRepos;

	@Autowired
	private CdBuildingCostRepositoryDay cdBuildingCostReposDay;

	@Autowired
	private CdBuildingCostRepositoryMon cdBuildingCostReposMon;

	@Autowired
	private CdBuildingCostRepositoryHist cdBuildingCostReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdBuildingCostRepos);
		org.junit.Assert.assertNotNull(cdBuildingCostReposDay);
		org.junit.Assert.assertNotNull(cdBuildingCostReposMon);
		org.junit.Assert.assertNotNull(cdBuildingCostReposHist);
	}

	@Override
	public CdBuildingCost findById(CdBuildingCostId cdBuildingCostId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + cdBuildingCostId);
		Optional<CdBuildingCost> cdBuildingCost = null;
		if (dbName.equals(ContentName.onDay))
			cdBuildingCost = cdBuildingCostReposDay.findById(cdBuildingCostId);
		else if (dbName.equals(ContentName.onMon))
			cdBuildingCost = cdBuildingCostReposMon.findById(cdBuildingCostId);
		else if (dbName.equals(ContentName.onHist))
			cdBuildingCost = cdBuildingCostReposHist.findById(cdBuildingCostId);
		else
			cdBuildingCost = cdBuildingCostRepos.findById(cdBuildingCostId);
		CdBuildingCost obj = cdBuildingCost.isPresent() ? cdBuildingCost.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdBuildingCost> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBuildingCost> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CityCode", "FloorLowerLimit"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CityCode", "FloorLowerLimit"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdBuildingCostReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBuildingCostReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBuildingCostReposHist.findAll(pageable);
		else
			slice = cdBuildingCostRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdBuildingCost> getCost(String cityCode_0, int floorLowerLimit_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBuildingCost> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("getCost " + dbName + " : " + "cityCode_0 : " + cityCode_0 + " floorLowerLimit_1 : " + floorLowerLimit_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdBuildingCostReposDay.findAllByCityCodeIsAndFloorLowerLimitLessThanEqualOrderByFloorLowerLimitDesc(cityCode_0, floorLowerLimit_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBuildingCostReposMon.findAllByCityCodeIsAndFloorLowerLimitLessThanEqualOrderByFloorLowerLimitDesc(cityCode_0, floorLowerLimit_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBuildingCostReposHist.findAllByCityCodeIsAndFloorLowerLimitLessThanEqualOrderByFloorLowerLimitDesc(cityCode_0, floorLowerLimit_1, pageable);
		else
			slice = cdBuildingCostRepos.findAllByCityCodeIsAndFloorLowerLimitLessThanEqualOrderByFloorLowerLimitDesc(cityCode_0, floorLowerLimit_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdBuildingCost> findCityCode(String cityCode_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBuildingCost> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCityCode " + dbName + " : " + "cityCode_0 : " + cityCode_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdBuildingCostReposDay.findAllByCityCodeIsOrderByFloorLowerLimitAsc(cityCode_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBuildingCostReposMon.findAllByCityCodeIsOrderByFloorLowerLimitAsc(cityCode_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBuildingCostReposHist.findAllByCityCodeIsOrderByFloorLowerLimitAsc(cityCode_0, pageable);
		else
			slice = cdBuildingCostRepos.findAllByCityCodeIsOrderByFloorLowerLimitAsc(cityCode_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdBuildingCost holdById(CdBuildingCostId cdBuildingCostId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdBuildingCostId);
		Optional<CdBuildingCost> cdBuildingCost = null;
		if (dbName.equals(ContentName.onDay))
			cdBuildingCost = cdBuildingCostReposDay.findByCdBuildingCostId(cdBuildingCostId);
		else if (dbName.equals(ContentName.onMon))
			cdBuildingCost = cdBuildingCostReposMon.findByCdBuildingCostId(cdBuildingCostId);
		else if (dbName.equals(ContentName.onHist))
			cdBuildingCost = cdBuildingCostReposHist.findByCdBuildingCostId(cdBuildingCostId);
		else
			cdBuildingCost = cdBuildingCostRepos.findByCdBuildingCostId(cdBuildingCostId);
		return cdBuildingCost.isPresent() ? cdBuildingCost.get() : null;
	}

	@Override
	public CdBuildingCost holdById(CdBuildingCost cdBuildingCost, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdBuildingCost.getCdBuildingCostId());
		Optional<CdBuildingCost> cdBuildingCostT = null;
		if (dbName.equals(ContentName.onDay))
			cdBuildingCostT = cdBuildingCostReposDay.findByCdBuildingCostId(cdBuildingCost.getCdBuildingCostId());
		else if (dbName.equals(ContentName.onMon))
			cdBuildingCostT = cdBuildingCostReposMon.findByCdBuildingCostId(cdBuildingCost.getCdBuildingCostId());
		else if (dbName.equals(ContentName.onHist))
			cdBuildingCostT = cdBuildingCostReposHist.findByCdBuildingCostId(cdBuildingCost.getCdBuildingCostId());
		else
			cdBuildingCostT = cdBuildingCostRepos.findByCdBuildingCostId(cdBuildingCost.getCdBuildingCostId());
		return cdBuildingCostT.isPresent() ? cdBuildingCostT.get() : null;
	}

	@Override
	public CdBuildingCost insert(CdBuildingCost cdBuildingCost, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdBuildingCost.getCdBuildingCostId());
		if (this.findById(cdBuildingCost.getCdBuildingCostId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdBuildingCost.setCreateEmpNo(empNot);

		if (cdBuildingCost.getLastUpdateEmpNo() == null || cdBuildingCost.getLastUpdateEmpNo().isEmpty())
			cdBuildingCost.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdBuildingCostReposDay.saveAndFlush(cdBuildingCost);
		else if (dbName.equals(ContentName.onMon))
			return cdBuildingCostReposMon.saveAndFlush(cdBuildingCost);
		else if (dbName.equals(ContentName.onHist))
			return cdBuildingCostReposHist.saveAndFlush(cdBuildingCost);
		else
			return cdBuildingCostRepos.saveAndFlush(cdBuildingCost);
	}

	@Override
	public CdBuildingCost update(CdBuildingCost cdBuildingCost, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdBuildingCost.getCdBuildingCostId());
		if (!empNot.isEmpty())
			cdBuildingCost.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdBuildingCostReposDay.saveAndFlush(cdBuildingCost);
		else if (dbName.equals(ContentName.onMon))
			return cdBuildingCostReposMon.saveAndFlush(cdBuildingCost);
		else if (dbName.equals(ContentName.onHist))
			return cdBuildingCostReposHist.saveAndFlush(cdBuildingCost);
		else
			return cdBuildingCostRepos.saveAndFlush(cdBuildingCost);
	}

	@Override
	public CdBuildingCost update2(CdBuildingCost cdBuildingCost, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdBuildingCost.getCdBuildingCostId());
		if (!empNot.isEmpty())
			cdBuildingCost.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdBuildingCostReposDay.saveAndFlush(cdBuildingCost);
		else if (dbName.equals(ContentName.onMon))
			cdBuildingCostReposMon.saveAndFlush(cdBuildingCost);
		else if (dbName.equals(ContentName.onHist))
			cdBuildingCostReposHist.saveAndFlush(cdBuildingCost);
		else
			cdBuildingCostRepos.saveAndFlush(cdBuildingCost);
		return this.findById(cdBuildingCost.getCdBuildingCostId());
	}

	@Override
	public void delete(CdBuildingCost cdBuildingCost, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdBuildingCost.getCdBuildingCostId());
		if (dbName.equals(ContentName.onDay)) {
			cdBuildingCostReposDay.delete(cdBuildingCost);
			cdBuildingCostReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBuildingCostReposMon.delete(cdBuildingCost);
			cdBuildingCostReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBuildingCostReposHist.delete(cdBuildingCost);
			cdBuildingCostReposHist.flush();
		} else {
			cdBuildingCostRepos.delete(cdBuildingCost);
			cdBuildingCostRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdBuildingCost> cdBuildingCost, TitaVo... titaVo) throws DBException {
		if (cdBuildingCost == null || cdBuildingCost.size() == 0)
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
		for (CdBuildingCost t : cdBuildingCost) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdBuildingCost = cdBuildingCostReposDay.saveAll(cdBuildingCost);
			cdBuildingCostReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBuildingCost = cdBuildingCostReposMon.saveAll(cdBuildingCost);
			cdBuildingCostReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBuildingCost = cdBuildingCostReposHist.saveAll(cdBuildingCost);
			cdBuildingCostReposHist.flush();
		} else {
			cdBuildingCost = cdBuildingCostRepos.saveAll(cdBuildingCost);
			cdBuildingCostRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdBuildingCost> cdBuildingCost, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdBuildingCost == null || cdBuildingCost.size() == 0)
			throw new DBException(6);

		for (CdBuildingCost t : cdBuildingCost)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdBuildingCost = cdBuildingCostReposDay.saveAll(cdBuildingCost);
			cdBuildingCostReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBuildingCost = cdBuildingCostReposMon.saveAll(cdBuildingCost);
			cdBuildingCostReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBuildingCost = cdBuildingCostReposHist.saveAll(cdBuildingCost);
			cdBuildingCostReposHist.flush();
		} else {
			cdBuildingCost = cdBuildingCostRepos.saveAll(cdBuildingCost);
			cdBuildingCostRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdBuildingCost> cdBuildingCost, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdBuildingCost == null || cdBuildingCost.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdBuildingCostReposDay.deleteAll(cdBuildingCost);
			cdBuildingCostReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBuildingCostReposMon.deleteAll(cdBuildingCost);
			cdBuildingCostReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBuildingCostReposHist.deleteAll(cdBuildingCost);
			cdBuildingCostReposHist.flush();
		} else {
			cdBuildingCostRepos.deleteAll(cdBuildingCost);
			cdBuildingCostRepos.flush();
		}
	}

}
