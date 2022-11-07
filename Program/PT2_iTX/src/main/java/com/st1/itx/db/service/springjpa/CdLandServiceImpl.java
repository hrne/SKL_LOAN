package com.st1.itx.db.service.springjpa;

import java.util.ArrayList;
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
import com.st1.itx.db.domain.CdLand;
import com.st1.itx.db.domain.CdLandId;
import com.st1.itx.db.repository.online.CdLandRepository;
import com.st1.itx.db.repository.day.CdLandRepositoryDay;
import com.st1.itx.db.repository.mon.CdLandRepositoryMon;
import com.st1.itx.db.repository.hist.CdLandRepositoryHist;
import com.st1.itx.db.service.CdLandService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdLandService")
@Repository
public class CdLandServiceImpl extends ASpringJpaParm implements CdLandService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdLandRepository cdLandRepos;

  @Autowired
  private CdLandRepositoryDay cdLandReposDay;

  @Autowired
  private CdLandRepositoryMon cdLandReposMon;

  @Autowired
  private CdLandRepositoryHist cdLandReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdLandRepos);
    org.junit.Assert.assertNotNull(cdLandReposDay);
    org.junit.Assert.assertNotNull(cdLandReposMon);
    org.junit.Assert.assertNotNull(cdLandReposHist);
  }

  @Override
  public CdLand findById(CdLandId cdLandId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdLandId);
    Optional<CdLand> cdLand = null;
    if (dbName.equals(ContentName.onDay))
      cdLand = cdLandReposDay.findById(cdLandId);
    else if (dbName.equals(ContentName.onMon))
      cdLand = cdLandReposMon.findById(cdLandId);
    else if (dbName.equals(ContentName.onHist))
      cdLand = cdLandReposHist.findById(cdLandId);
    else 
      cdLand = cdLandRepos.findById(cdLandId);
    CdLand obj = cdLand.isPresent() ? cdLand.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdLand> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdLand> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CityCode", "LandOfficeCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CityCode", "LandOfficeCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdLandReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdLandReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdLandReposHist.findAll(pageable);
    else 
      slice = cdLandRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdLand> findCityCodeEq(String cityCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdLand> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCityCodeEq " + dbName + " : " + "cityCode_0 : " + cityCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdLandReposDay.findAllByCityCodeIsOrderByCityCodeAscLandOfficeCodeAsc(cityCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdLandReposMon.findAllByCityCodeIsOrderByCityCodeAscLandOfficeCodeAsc(cityCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdLandReposHist.findAllByCityCodeIsOrderByCityCodeAscLandOfficeCodeAsc(cityCode_0, pageable);
    else 
      slice = cdLandRepos.findAllByCityCodeIsOrderByCityCodeAscLandOfficeCodeAsc(cityCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdLand holdById(CdLandId cdLandId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdLandId);
    Optional<CdLand> cdLand = null;
    if (dbName.equals(ContentName.onDay))
      cdLand = cdLandReposDay.findByCdLandId(cdLandId);
    else if (dbName.equals(ContentName.onMon))
      cdLand = cdLandReposMon.findByCdLandId(cdLandId);
    else if (dbName.equals(ContentName.onHist))
      cdLand = cdLandReposHist.findByCdLandId(cdLandId);
    else 
      cdLand = cdLandRepos.findByCdLandId(cdLandId);
    return cdLand.isPresent() ? cdLand.get() : null;
  }

  @Override
  public CdLand holdById(CdLand cdLand, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdLand.getCdLandId());
    Optional<CdLand> cdLandT = null;
    if (dbName.equals(ContentName.onDay))
      cdLandT = cdLandReposDay.findByCdLandId(cdLand.getCdLandId());
    else if (dbName.equals(ContentName.onMon))
      cdLandT = cdLandReposMon.findByCdLandId(cdLand.getCdLandId());
    else if (dbName.equals(ContentName.onHist))
      cdLandT = cdLandReposHist.findByCdLandId(cdLand.getCdLandId());
    else 
      cdLandT = cdLandRepos.findByCdLandId(cdLand.getCdLandId());
    return cdLandT.isPresent() ? cdLandT.get() : null;
  }

  @Override
  public CdLand insert(CdLand cdLand, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdLand.getCdLandId());
    if (this.findById(cdLand.getCdLandId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdLand.setCreateEmpNo(empNot);

    if(cdLand.getLastUpdateEmpNo() == null || cdLand.getLastUpdateEmpNo().isEmpty())
      cdLand.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdLandReposDay.saveAndFlush(cdLand);	
    else if (dbName.equals(ContentName.onMon))
      return cdLandReposMon.saveAndFlush(cdLand);
    else if (dbName.equals(ContentName.onHist))
      return cdLandReposHist.saveAndFlush(cdLand);
    else 
    return cdLandRepos.saveAndFlush(cdLand);
  }

  @Override
  public CdLand update(CdLand cdLand, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdLand.getCdLandId());
    if (!empNot.isEmpty())
      cdLand.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdLandReposDay.saveAndFlush(cdLand);	
    else if (dbName.equals(ContentName.onMon))
      return cdLandReposMon.saveAndFlush(cdLand);
    else if (dbName.equals(ContentName.onHist))
      return cdLandReposHist.saveAndFlush(cdLand);
    else 
    return cdLandRepos.saveAndFlush(cdLand);
  }

  @Override
  public CdLand update2(CdLand cdLand, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdLand.getCdLandId());
    if (!empNot.isEmpty())
      cdLand.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdLandReposDay.saveAndFlush(cdLand);	
    else if (dbName.equals(ContentName.onMon))
      cdLandReposMon.saveAndFlush(cdLand);
    else if (dbName.equals(ContentName.onHist))
        cdLandReposHist.saveAndFlush(cdLand);
    else 
      cdLandRepos.saveAndFlush(cdLand);	
    return this.findById(cdLand.getCdLandId());
  }

  @Override
  public void delete(CdLand cdLand, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdLand.getCdLandId());
    if (dbName.equals(ContentName.onDay)) {
      cdLandReposDay.delete(cdLand);	
      cdLandReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdLandReposMon.delete(cdLand);	
      cdLandReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdLandReposHist.delete(cdLand);
      cdLandReposHist.flush();
    }
    else {
      cdLandRepos.delete(cdLand);
      cdLandRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdLand> cdLand, TitaVo... titaVo) throws DBException {
    if (cdLand == null || cdLand.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdLand t : cdLand){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdLand = cdLandReposDay.saveAll(cdLand);	
      cdLandReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdLand = cdLandReposMon.saveAll(cdLand);	
      cdLandReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdLand = cdLandReposHist.saveAll(cdLand);
      cdLandReposHist.flush();
    }
    else {
      cdLand = cdLandRepos.saveAll(cdLand);
      cdLandRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdLand> cdLand, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdLand == null || cdLand.size() == 0)
      throw new DBException(6);

    for (CdLand t : cdLand) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdLand = cdLandReposDay.saveAll(cdLand);	
      cdLandReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdLand = cdLandReposMon.saveAll(cdLand);	
      cdLandReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdLand = cdLandReposHist.saveAll(cdLand);
      cdLandReposHist.flush();
    }
    else {
      cdLand = cdLandRepos.saveAll(cdLand);
      cdLandRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdLand> cdLand, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdLand == null || cdLand.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdLandReposDay.deleteAll(cdLand);	
      cdLandReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdLandReposMon.deleteAll(cdLand);	
      cdLandReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdLandReposHist.deleteAll(cdLand);
      cdLandReposHist.flush();
    }
    else {
      cdLandRepos.deleteAll(cdLand);
      cdLandRepos.flush();
    }
  }

}
