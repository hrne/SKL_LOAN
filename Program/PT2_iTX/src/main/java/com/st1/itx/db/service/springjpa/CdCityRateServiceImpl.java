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
import com.st1.itx.db.domain.CdCityRate;
import com.st1.itx.db.domain.CdCityRateId;
import com.st1.itx.db.repository.online.CdCityRateRepository;
import com.st1.itx.db.repository.day.CdCityRateRepositoryDay;
import com.st1.itx.db.repository.mon.CdCityRateRepositoryMon;
import com.st1.itx.db.repository.hist.CdCityRateRepositoryHist;
import com.st1.itx.db.service.CdCityRateService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdCityRateService")
@Repository
public class CdCityRateServiceImpl extends ASpringJpaParm implements CdCityRateService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdCityRateRepository cdCityRateRepos;

  @Autowired
  private CdCityRateRepositoryDay cdCityRateReposDay;

  @Autowired
  private CdCityRateRepositoryMon cdCityRateReposMon;

  @Autowired
  private CdCityRateRepositoryHist cdCityRateReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdCityRateRepos);
    org.junit.Assert.assertNotNull(cdCityRateReposDay);
    org.junit.Assert.assertNotNull(cdCityRateReposMon);
    org.junit.Assert.assertNotNull(cdCityRateReposHist);
  }

  @Override
  public CdCityRate findById(CdCityRateId cdCityRateId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdCityRateId);
    Optional<CdCityRate> cdCityRate = null;
    if (dbName.equals(ContentName.onDay))
      cdCityRate = cdCityRateReposDay.findById(cdCityRateId);
    else if (dbName.equals(ContentName.onMon))
      cdCityRate = cdCityRateReposMon.findById(cdCityRateId);
    else if (dbName.equals(ContentName.onHist))
      cdCityRate = cdCityRateReposHist.findById(cdCityRateId);
    else 
      cdCityRate = cdCityRateRepos.findById(cdCityRateId);
    CdCityRate obj = cdCityRate.isPresent() ? cdCityRate.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdCityRate> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCityRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "EffectYYMM", "CityCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EffectYYMM", "CityCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdCityRateReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCityRateReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCityRateReposHist.findAll(pageable);
    else 
      slice = cdCityRateRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdCityRate> findCityCodeEq(String cityCode_0, int effectYYMM_1, int effectYYMM_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCityRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCityCodeEq " + dbName + " : " + "cityCode_0 : " + cityCode_0 + " effectYYMM_1 : " +  effectYYMM_1 + " effectYYMM_2 : " +  effectYYMM_2);
    if (dbName.equals(ContentName.onDay))
      slice = cdCityRateReposDay.findAllByCityCodeIsAndEffectYYMMGreaterThanEqualAndEffectYYMMLessThanEqualOrderByEffectYYMMDesc(cityCode_0, effectYYMM_1, effectYYMM_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCityRateReposMon.findAllByCityCodeIsAndEffectYYMMGreaterThanEqualAndEffectYYMMLessThanEqualOrderByEffectYYMMDesc(cityCode_0, effectYYMM_1, effectYYMM_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCityRateReposHist.findAllByCityCodeIsAndEffectYYMMGreaterThanEqualAndEffectYYMMLessThanEqualOrderByEffectYYMMDesc(cityCode_0, effectYYMM_1, effectYYMM_2, pageable);
    else 
      slice = cdCityRateRepos.findAllByCityCodeIsAndEffectYYMMGreaterThanEqualAndEffectYYMMLessThanEqualOrderByEffectYYMMDesc(cityCode_0, effectYYMM_1, effectYYMM_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdCityRate> findEffectDateEq(int effectYYMM_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCityRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findEffectDateEq " + dbName + " : " + "effectYYMM_0 : " + effectYYMM_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdCityRateReposDay.findAllByEffectYYMMIsOrderByCityCodeAsc(effectYYMM_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCityRateReposMon.findAllByEffectYYMMIsOrderByCityCodeAsc(effectYYMM_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCityRateReposHist.findAllByEffectYYMMIsOrderByCityCodeAsc(effectYYMM_0, pageable);
    else 
      slice = cdCityRateRepos.findAllByEffectYYMMIsOrderByCityCodeAsc(effectYYMM_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdCityRate> findEffectDateRange(int effectYYMM_0, int effectYYMM_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCityRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findEffectDateRange " + dbName + " : " + "effectYYMM_0 : " + effectYYMM_0 + " effectYYMM_1 : " +  effectYYMM_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdCityRateReposDay.findAllByEffectYYMMGreaterThanEqualAndEffectYYMMLessThanEqualOrderByCityCodeAscEffectYYMMDesc(effectYYMM_0, effectYYMM_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCityRateReposMon.findAllByEffectYYMMGreaterThanEqualAndEffectYYMMLessThanEqualOrderByCityCodeAscEffectYYMMDesc(effectYYMM_0, effectYYMM_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCityRateReposHist.findAllByEffectYYMMGreaterThanEqualAndEffectYYMMLessThanEqualOrderByCityCodeAscEffectYYMMDesc(effectYYMM_0, effectYYMM_1, pageable);
    else 
      slice = cdCityRateRepos.findAllByEffectYYMMGreaterThanEqualAndEffectYYMMLessThanEqualOrderByCityCodeAscEffectYYMMDesc(effectYYMM_0, effectYYMM_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdCityRate holdById(CdCityRateId cdCityRateId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdCityRateId);
    Optional<CdCityRate> cdCityRate = null;
    if (dbName.equals(ContentName.onDay))
      cdCityRate = cdCityRateReposDay.findByCdCityRateId(cdCityRateId);
    else if (dbName.equals(ContentName.onMon))
      cdCityRate = cdCityRateReposMon.findByCdCityRateId(cdCityRateId);
    else if (dbName.equals(ContentName.onHist))
      cdCityRate = cdCityRateReposHist.findByCdCityRateId(cdCityRateId);
    else 
      cdCityRate = cdCityRateRepos.findByCdCityRateId(cdCityRateId);
    return cdCityRate.isPresent() ? cdCityRate.get() : null;
  }

  @Override
  public CdCityRate holdById(CdCityRate cdCityRate, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdCityRate.getCdCityRateId());
    Optional<CdCityRate> cdCityRateT = null;
    if (dbName.equals(ContentName.onDay))
      cdCityRateT = cdCityRateReposDay.findByCdCityRateId(cdCityRate.getCdCityRateId());
    else if (dbName.equals(ContentName.onMon))
      cdCityRateT = cdCityRateReposMon.findByCdCityRateId(cdCityRate.getCdCityRateId());
    else if (dbName.equals(ContentName.onHist))
      cdCityRateT = cdCityRateReposHist.findByCdCityRateId(cdCityRate.getCdCityRateId());
    else 
      cdCityRateT = cdCityRateRepos.findByCdCityRateId(cdCityRate.getCdCityRateId());
    return cdCityRateT.isPresent() ? cdCityRateT.get() : null;
  }

  @Override
  public CdCityRate insert(CdCityRate cdCityRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdCityRate.getCdCityRateId());
    if (this.findById(cdCityRate.getCdCityRateId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdCityRate.setCreateEmpNo(empNot);

    if(cdCityRate.getLastUpdateEmpNo() == null || cdCityRate.getLastUpdateEmpNo().isEmpty())
      cdCityRate.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdCityRateReposDay.saveAndFlush(cdCityRate);	
    else if (dbName.equals(ContentName.onMon))
      return cdCityRateReposMon.saveAndFlush(cdCityRate);
    else if (dbName.equals(ContentName.onHist))
      return cdCityRateReposHist.saveAndFlush(cdCityRate);
    else 
    return cdCityRateRepos.saveAndFlush(cdCityRate);
  }

  @Override
  public CdCityRate update(CdCityRate cdCityRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdCityRate.getCdCityRateId());
    if (!empNot.isEmpty())
      cdCityRate.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdCityRateReposDay.saveAndFlush(cdCityRate);	
    else if (dbName.equals(ContentName.onMon))
      return cdCityRateReposMon.saveAndFlush(cdCityRate);
    else if (dbName.equals(ContentName.onHist))
      return cdCityRateReposHist.saveAndFlush(cdCityRate);
    else 
    return cdCityRateRepos.saveAndFlush(cdCityRate);
  }

  @Override
  public CdCityRate update2(CdCityRate cdCityRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdCityRate.getCdCityRateId());
    if (!empNot.isEmpty())
      cdCityRate.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdCityRateReposDay.saveAndFlush(cdCityRate);	
    else if (dbName.equals(ContentName.onMon))
      cdCityRateReposMon.saveAndFlush(cdCityRate);
    else if (dbName.equals(ContentName.onHist))
        cdCityRateReposHist.saveAndFlush(cdCityRate);
    else 
      cdCityRateRepos.saveAndFlush(cdCityRate);	
    return this.findById(cdCityRate.getCdCityRateId());
  }

  @Override
  public void delete(CdCityRate cdCityRate, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdCityRate.getCdCityRateId());
    if (dbName.equals(ContentName.onDay)) {
      cdCityRateReposDay.delete(cdCityRate);	
      cdCityRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCityRateReposMon.delete(cdCityRate);	
      cdCityRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCityRateReposHist.delete(cdCityRate);
      cdCityRateReposHist.flush();
    }
    else {
      cdCityRateRepos.delete(cdCityRate);
      cdCityRateRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdCityRate> cdCityRate, TitaVo... titaVo) throws DBException {
    if (cdCityRate == null || cdCityRate.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdCityRate t : cdCityRate){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdCityRate = cdCityRateReposDay.saveAll(cdCityRate);	
      cdCityRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCityRate = cdCityRateReposMon.saveAll(cdCityRate);	
      cdCityRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCityRate = cdCityRateReposHist.saveAll(cdCityRate);
      cdCityRateReposHist.flush();
    }
    else {
      cdCityRate = cdCityRateRepos.saveAll(cdCityRate);
      cdCityRateRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdCityRate> cdCityRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdCityRate == null || cdCityRate.size() == 0)
      throw new DBException(6);

    for (CdCityRate t : cdCityRate) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdCityRate = cdCityRateReposDay.saveAll(cdCityRate);	
      cdCityRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCityRate = cdCityRateReposMon.saveAll(cdCityRate);	
      cdCityRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCityRate = cdCityRateReposHist.saveAll(cdCityRate);
      cdCityRateReposHist.flush();
    }
    else {
      cdCityRate = cdCityRateRepos.saveAll(cdCityRate);
      cdCityRateRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdCityRate> cdCityRate, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdCityRate == null || cdCityRate.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdCityRateReposDay.deleteAll(cdCityRate);	
      cdCityRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCityRateReposMon.deleteAll(cdCityRate);	
      cdCityRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCityRateReposHist.deleteAll(cdCityRate);
      cdCityRateReposHist.flush();
    }
    else {
      cdCityRateRepos.deleteAll(cdCityRate);
      cdCityRateRepos.flush();
    }
  }

}
