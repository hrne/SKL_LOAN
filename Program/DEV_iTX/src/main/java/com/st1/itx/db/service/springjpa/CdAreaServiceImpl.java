package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.repository.online.CdAreaRepository;
import com.st1.itx.db.repository.day.CdAreaRepositoryDay;
import com.st1.itx.db.repository.mon.CdAreaRepositoryMon;
import com.st1.itx.db.repository.hist.CdAreaRepositoryHist;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdAreaService")
@Repository
public class CdAreaServiceImpl implements CdAreaService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CdAreaServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdAreaRepository cdAreaRepos;

  @Autowired
  private CdAreaRepositoryDay cdAreaReposDay;

  @Autowired
  private CdAreaRepositoryMon cdAreaReposMon;

  @Autowired
  private CdAreaRepositoryHist cdAreaReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdAreaRepos);
    org.junit.Assert.assertNotNull(cdAreaReposDay);
    org.junit.Assert.assertNotNull(cdAreaReposMon);
    org.junit.Assert.assertNotNull(cdAreaReposHist);
  }

  @Override
  public CdArea findById(CdAreaId cdAreaId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + cdAreaId);
    Optional<CdArea> cdArea = null;
    if (dbName.equals(ContentName.onDay))
      cdArea = cdAreaReposDay.findById(cdAreaId);
    else if (dbName.equals(ContentName.onMon))
      cdArea = cdAreaReposMon.findById(cdAreaId);
    else if (dbName.equals(ContentName.onHist))
      cdArea = cdAreaReposHist.findById(cdAreaId);
    else 
      cdArea = cdAreaRepos.findById(cdAreaId);
    CdArea obj = cdArea.isPresent() ? cdArea.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdArea> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdArea> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CityCode", "AreaCode"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdAreaReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdAreaReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdAreaReposHist.findAll(pageable);
    else 
      slice = cdAreaRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdArea> cityCodeEq(String cityCode_0, String cityCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdArea> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("cityCodeEq " + dbName + " : " + "cityCode_0 : " + cityCode_0 + " cityCode_1 : " +  cityCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdAreaReposDay.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualOrderByCityCodeAscAreaCodeAsc(cityCode_0, cityCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdAreaReposMon.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualOrderByCityCodeAscAreaCodeAsc(cityCode_0, cityCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdAreaReposHist.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualOrderByCityCodeAscAreaCodeAsc(cityCode_0, cityCode_1, pageable);
    else 
      slice = cdAreaRepos.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualOrderByCityCodeAscAreaCodeAsc(cityCode_0, cityCode_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdArea> areaCodeRange(String cityCode_0, String cityCode_1, String areaCode_2, String areaCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdArea> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("areaCodeRange " + dbName + " : " + "cityCode_0 : " + cityCode_0 + " cityCode_1 : " +  cityCode_1 + " areaCode_2 : " +  areaCode_2 + " areaCode_3 : " +  areaCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = cdAreaReposDay.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualAndAreaCodeGreaterThanEqualAndAreaCodeLessThanEqualOrderByCityCodeAscAreaCodeAsc(cityCode_0, cityCode_1, areaCode_2, areaCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdAreaReposMon.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualAndAreaCodeGreaterThanEqualAndAreaCodeLessThanEqualOrderByCityCodeAscAreaCodeAsc(cityCode_0, cityCode_1, areaCode_2, areaCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdAreaReposHist.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualAndAreaCodeGreaterThanEqualAndAreaCodeLessThanEqualOrderByCityCodeAscAreaCodeAsc(cityCode_0, cityCode_1, areaCode_2, areaCode_3, pageable);
    else 
      slice = cdAreaRepos.findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualAndAreaCodeGreaterThanEqualAndAreaCodeLessThanEqualOrderByCityCodeAscAreaCodeAsc(cityCode_0, cityCode_1, areaCode_2, areaCode_3, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdArea Zip3First(String zip3_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Zip3First " + dbName + " : " + "zip3_0 : " + zip3_0);
    Optional<CdArea> cdAreaT = null;
    if (dbName.equals(ContentName.onDay))
      cdAreaT = cdAreaReposDay.findTopByZip3IsOrderByCityCodeAscAreaCodeAsc(zip3_0);
    else if (dbName.equals(ContentName.onMon))
      cdAreaT = cdAreaReposMon.findTopByZip3IsOrderByCityCodeAscAreaCodeAsc(zip3_0);
    else if (dbName.equals(ContentName.onHist))
      cdAreaT = cdAreaReposHist.findTopByZip3IsOrderByCityCodeAscAreaCodeAsc(zip3_0);
    else 
      cdAreaT = cdAreaRepos.findTopByZip3IsOrderByCityCodeAscAreaCodeAsc(zip3_0);
    return cdAreaT.isPresent() ? cdAreaT.get() : null;
  }

  @Override
  public CdArea holdById(CdAreaId cdAreaId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdAreaId);
    Optional<CdArea> cdArea = null;
    if (dbName.equals(ContentName.onDay))
      cdArea = cdAreaReposDay.findByCdAreaId(cdAreaId);
    else if (dbName.equals(ContentName.onMon))
      cdArea = cdAreaReposMon.findByCdAreaId(cdAreaId);
    else if (dbName.equals(ContentName.onHist))
      cdArea = cdAreaReposHist.findByCdAreaId(cdAreaId);
    else 
      cdArea = cdAreaRepos.findByCdAreaId(cdAreaId);
    return cdArea.isPresent() ? cdArea.get() : null;
  }

  @Override
  public CdArea holdById(CdArea cdArea, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdArea.getCdAreaId());
    Optional<CdArea> cdAreaT = null;
    if (dbName.equals(ContentName.onDay))
      cdAreaT = cdAreaReposDay.findByCdAreaId(cdArea.getCdAreaId());
    else if (dbName.equals(ContentName.onMon))
      cdAreaT = cdAreaReposMon.findByCdAreaId(cdArea.getCdAreaId());
    else if (dbName.equals(ContentName.onHist))
      cdAreaT = cdAreaReposHist.findByCdAreaId(cdArea.getCdAreaId());
    else 
      cdAreaT = cdAreaRepos.findByCdAreaId(cdArea.getCdAreaId());
    return cdAreaT.isPresent() ? cdAreaT.get() : null;
  }

  @Override
  public CdArea insert(CdArea cdArea, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + cdArea.getCdAreaId());
    if (this.findById(cdArea.getCdAreaId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdArea.setCreateEmpNo(empNot);

    if(cdArea.getLastUpdateEmpNo() == null || cdArea.getLastUpdateEmpNo().isEmpty())
      cdArea.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdAreaReposDay.saveAndFlush(cdArea);	
    else if (dbName.equals(ContentName.onMon))
      return cdAreaReposMon.saveAndFlush(cdArea);
    else if (dbName.equals(ContentName.onHist))
      return cdAreaReposHist.saveAndFlush(cdArea);
    else 
    return cdAreaRepos.saveAndFlush(cdArea);
  }

  @Override
  public CdArea update(CdArea cdArea, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdArea.getCdAreaId());
    if (!empNot.isEmpty())
      cdArea.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdAreaReposDay.saveAndFlush(cdArea);	
    else if (dbName.equals(ContentName.onMon))
      return cdAreaReposMon.saveAndFlush(cdArea);
    else if (dbName.equals(ContentName.onHist))
      return cdAreaReposHist.saveAndFlush(cdArea);
    else 
    return cdAreaRepos.saveAndFlush(cdArea);
  }

  @Override
  public CdArea update2(CdArea cdArea, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdArea.getCdAreaId());
    if (!empNot.isEmpty())
      cdArea.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdAreaReposDay.saveAndFlush(cdArea);	
    else if (dbName.equals(ContentName.onMon))
      cdAreaReposMon.saveAndFlush(cdArea);
    else if (dbName.equals(ContentName.onHist))
        cdAreaReposHist.saveAndFlush(cdArea);
    else 
      cdAreaRepos.saveAndFlush(cdArea);	
    return this.findById(cdArea.getCdAreaId());
  }

  @Override
  public void delete(CdArea cdArea, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + cdArea.getCdAreaId());
    if (dbName.equals(ContentName.onDay)) {
      cdAreaReposDay.delete(cdArea);	
      cdAreaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAreaReposMon.delete(cdArea);	
      cdAreaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAreaReposHist.delete(cdArea);
      cdAreaReposHist.flush();
    }
    else {
      cdAreaRepos.delete(cdArea);
      cdAreaRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdArea> cdArea, TitaVo... titaVo) throws DBException {
    if (cdArea == null || cdArea.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (CdArea t : cdArea){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdArea = cdAreaReposDay.saveAll(cdArea);	
      cdAreaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdArea = cdAreaReposMon.saveAll(cdArea);	
      cdAreaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdArea = cdAreaReposHist.saveAll(cdArea);
      cdAreaReposHist.flush();
    }
    else {
      cdArea = cdAreaRepos.saveAll(cdArea);
      cdAreaRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdArea> cdArea, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (cdArea == null || cdArea.size() == 0)
      throw new DBException(6);

    for (CdArea t : cdArea) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdArea = cdAreaReposDay.saveAll(cdArea);	
      cdAreaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdArea = cdAreaReposMon.saveAll(cdArea);	
      cdAreaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdArea = cdAreaReposHist.saveAll(cdArea);
      cdAreaReposHist.flush();
    }
    else {
      cdArea = cdAreaRepos.saveAll(cdArea);
      cdAreaRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdArea> cdArea, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdArea == null || cdArea.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdAreaReposDay.deleteAll(cdArea);	
      cdAreaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAreaReposMon.deleteAll(cdArea);	
      cdAreaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAreaReposHist.deleteAll(cdArea);
      cdAreaReposHist.flush();
    }
    else {
      cdAreaRepos.deleteAll(cdArea);
      cdAreaRepos.flush();
    }
  }

}
