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
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.repository.online.CdCityRepository;
import com.st1.itx.db.repository.day.CdCityRepositoryDay;
import com.st1.itx.db.repository.mon.CdCityRepositoryMon;
import com.st1.itx.db.repository.hist.CdCityRepositoryHist;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdCityService")
@Repository
public class CdCityServiceImpl extends ASpringJpaParm implements CdCityService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdCityRepository cdCityRepos;

  @Autowired
  private CdCityRepositoryDay cdCityReposDay;

  @Autowired
  private CdCityRepositoryMon cdCityReposMon;

  @Autowired
  private CdCityRepositoryHist cdCityReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdCityRepos);
    org.junit.Assert.assertNotNull(cdCityReposDay);
    org.junit.Assert.assertNotNull(cdCityReposMon);
    org.junit.Assert.assertNotNull(cdCityReposHist);
  }

  @Override
  public CdCity findById(String cityCode, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cityCode);
    Optional<CdCity> cdCity = null;
    if (dbName.equals(ContentName.onDay))
      cdCity = cdCityReposDay.findById(cityCode);
    else if (dbName.equals(ContentName.onMon))
      cdCity = cdCityReposMon.findById(cityCode);
    else if (dbName.equals(ContentName.onHist))
      cdCity = cdCityReposHist.findById(cityCode);
    else 
      cdCity = cdCityRepos.findById(cityCode);
    CdCity obj = cdCity.isPresent() ? cdCity.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdCity> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCity> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CityCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CityCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdCityReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCityReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCityReposHist.findAll(pageable);
    else 
      slice = cdCityRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdCity> findUnitCode(String unitCode_0, String unitCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCity> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findUnitCode " + dbName + " : " + "unitCode_0 : " + unitCode_0 + " unitCode_1 : " +  unitCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdCityReposDay.findAllByUnitCodeGreaterThanEqualAndUnitCodeLessThanEqualOrderByCityCodeAsc(unitCode_0, unitCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCityReposMon.findAllByUnitCodeGreaterThanEqualAndUnitCodeLessThanEqualOrderByCityCodeAsc(unitCode_0, unitCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCityReposHist.findAllByUnitCodeGreaterThanEqualAndUnitCodeLessThanEqualOrderByCityCodeAsc(unitCode_0, unitCode_1, pageable);
    else 
      slice = cdCityRepos.findAllByUnitCodeGreaterThanEqualAndUnitCodeLessThanEqualOrderByCityCodeAsc(unitCode_0, unitCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdCity> findCityCode(String cityCode_0, String cityCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCity> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCityCode " + dbName + " : " + "cityCode_0 : " + cityCode_0 + " cityCode_1 : " +  cityCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdCityReposDay.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualOrderByCityCodeAsc(cityCode_0, cityCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCityReposMon.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualOrderByCityCodeAsc(cityCode_0, cityCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCityReposHist.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualOrderByCityCodeAsc(cityCode_0, cityCode_1, pageable);
    else 
      slice = cdCityRepos.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualOrderByCityCodeAsc(cityCode_0, cityCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdCity holdById(String cityCode, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cityCode);
    Optional<CdCity> cdCity = null;
    if (dbName.equals(ContentName.onDay))
      cdCity = cdCityReposDay.findByCityCode(cityCode);
    else if (dbName.equals(ContentName.onMon))
      cdCity = cdCityReposMon.findByCityCode(cityCode);
    else if (dbName.equals(ContentName.onHist))
      cdCity = cdCityReposHist.findByCityCode(cityCode);
    else 
      cdCity = cdCityRepos.findByCityCode(cityCode);
    return cdCity.isPresent() ? cdCity.get() : null;
  }

  @Override
  public CdCity holdById(CdCity cdCity, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdCity.getCityCode());
    Optional<CdCity> cdCityT = null;
    if (dbName.equals(ContentName.onDay))
      cdCityT = cdCityReposDay.findByCityCode(cdCity.getCityCode());
    else if (dbName.equals(ContentName.onMon))
      cdCityT = cdCityReposMon.findByCityCode(cdCity.getCityCode());
    else if (dbName.equals(ContentName.onHist))
      cdCityT = cdCityReposHist.findByCityCode(cdCity.getCityCode());
    else 
      cdCityT = cdCityRepos.findByCityCode(cdCity.getCityCode());
    return cdCityT.isPresent() ? cdCityT.get() : null;
  }

  @Override
  public CdCity insert(CdCity cdCity, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdCity.getCityCode());
    if (this.findById(cdCity.getCityCode()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdCity.setCreateEmpNo(empNot);

    if(cdCity.getLastUpdateEmpNo() == null || cdCity.getLastUpdateEmpNo().isEmpty())
      cdCity.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdCityReposDay.saveAndFlush(cdCity);	
    else if (dbName.equals(ContentName.onMon))
      return cdCityReposMon.saveAndFlush(cdCity);
    else if (dbName.equals(ContentName.onHist))
      return cdCityReposHist.saveAndFlush(cdCity);
    else 
    return cdCityRepos.saveAndFlush(cdCity);
  }

  @Override
  public CdCity update(CdCity cdCity, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdCity.getCityCode());
    if (!empNot.isEmpty())
      cdCity.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdCityReposDay.saveAndFlush(cdCity);	
    else if (dbName.equals(ContentName.onMon))
      return cdCityReposMon.saveAndFlush(cdCity);
    else if (dbName.equals(ContentName.onHist))
      return cdCityReposHist.saveAndFlush(cdCity);
    else 
    return cdCityRepos.saveAndFlush(cdCity);
  }

  @Override
  public CdCity update2(CdCity cdCity, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdCity.getCityCode());
    if (!empNot.isEmpty())
      cdCity.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdCityReposDay.saveAndFlush(cdCity);	
    else if (dbName.equals(ContentName.onMon))
      cdCityReposMon.saveAndFlush(cdCity);
    else if (dbName.equals(ContentName.onHist))
        cdCityReposHist.saveAndFlush(cdCity);
    else 
      cdCityRepos.saveAndFlush(cdCity);	
    return this.findById(cdCity.getCityCode());
  }

  @Override
  public void delete(CdCity cdCity, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdCity.getCityCode());
    if (dbName.equals(ContentName.onDay)) {
      cdCityReposDay.delete(cdCity);	
      cdCityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCityReposMon.delete(cdCity);	
      cdCityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCityReposHist.delete(cdCity);
      cdCityReposHist.flush();
    }
    else {
      cdCityRepos.delete(cdCity);
      cdCityRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdCity> cdCity, TitaVo... titaVo) throws DBException {
    if (cdCity == null || cdCity.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdCity t : cdCity){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdCity = cdCityReposDay.saveAll(cdCity);	
      cdCityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCity = cdCityReposMon.saveAll(cdCity);	
      cdCityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCity = cdCityReposHist.saveAll(cdCity);
      cdCityReposHist.flush();
    }
    else {
      cdCity = cdCityRepos.saveAll(cdCity);
      cdCityRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdCity> cdCity, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdCity == null || cdCity.size() == 0)
      throw new DBException(6);

    for (CdCity t : cdCity) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdCity = cdCityReposDay.saveAll(cdCity);	
      cdCityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCity = cdCityReposMon.saveAll(cdCity);	
      cdCityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCity = cdCityReposHist.saveAll(cdCity);
      cdCityReposHist.flush();
    }
    else {
      cdCity = cdCityRepos.saveAll(cdCity);
      cdCityRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdCity> cdCity, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdCity == null || cdCity.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdCityReposDay.deleteAll(cdCity);	
      cdCityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCityReposMon.deleteAll(cdCity);	
      cdCityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCityReposHist.deleteAll(cdCity);
      cdCityReposHist.flush();
    }
    else {
      cdCityRepos.deleteAll(cdCity);
      cdCityRepos.flush();
    }
  }

}
