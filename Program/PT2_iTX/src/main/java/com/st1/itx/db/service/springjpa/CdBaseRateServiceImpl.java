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
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.CdBaseRateId;
import com.st1.itx.db.repository.online.CdBaseRateRepository;
import com.st1.itx.db.repository.day.CdBaseRateRepositoryDay;
import com.st1.itx.db.repository.mon.CdBaseRateRepositoryMon;
import com.st1.itx.db.repository.hist.CdBaseRateRepositoryHist;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdBaseRateService")
@Repository
public class CdBaseRateServiceImpl extends ASpringJpaParm implements CdBaseRateService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdBaseRateRepository cdBaseRateRepos;

  @Autowired
  private CdBaseRateRepositoryDay cdBaseRateReposDay;

  @Autowired
  private CdBaseRateRepositoryMon cdBaseRateReposMon;

  @Autowired
  private CdBaseRateRepositoryHist cdBaseRateReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdBaseRateRepos);
    org.junit.Assert.assertNotNull(cdBaseRateReposDay);
    org.junit.Assert.assertNotNull(cdBaseRateReposMon);
    org.junit.Assert.assertNotNull(cdBaseRateReposHist);
  }

  @Override
  public CdBaseRate findById(CdBaseRateId cdBaseRateId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdBaseRateId);
    Optional<CdBaseRate> cdBaseRate = null;
    if (dbName.equals(ContentName.onDay))
      cdBaseRate = cdBaseRateReposDay.findById(cdBaseRateId);
    else if (dbName.equals(ContentName.onMon))
      cdBaseRate = cdBaseRateReposMon.findById(cdBaseRateId);
    else if (dbName.equals(ContentName.onHist))
      cdBaseRate = cdBaseRateReposHist.findById(cdBaseRateId);
    else 
      cdBaseRate = cdBaseRateRepos.findById(cdBaseRateId);
    CdBaseRate obj = cdBaseRate.isPresent() ? cdBaseRate.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdBaseRate> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBaseRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CurrencyCode", "BaseRateCode", "EffectDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CurrencyCode", "BaseRateCode", "EffectDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdBaseRateReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBaseRateReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBaseRateReposHist.findAll(pageable);
    else 
      slice = cdBaseRateRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBaseRate> baseRateCodeEq(String currencyCode_0, String baseRateCode_1, int effectDate_2, int effectDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBaseRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("baseRateCodeEq " + dbName + " : " + "currencyCode_0 : " + currencyCode_0 + " baseRateCode_1 : " +  baseRateCode_1 + " effectDate_2 : " +  effectDate_2 + " effectDate_3 : " +  effectDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = cdBaseRateReposDay.findAllByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBaseRateReposMon.findAllByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBaseRateReposHist.findAllByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3, pageable);
    else 
      slice = cdBaseRateRepos.findAllByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdBaseRate baseRateCodeDescFirst(String currencyCode_0, String baseRateCode_1, int effectDate_2, int effectDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("baseRateCodeDescFirst " + dbName + " : " + "currencyCode_0 : " + currencyCode_0 + " baseRateCode_1 : " +  baseRateCode_1 + " effectDate_2 : " +  effectDate_2 + " effectDate_3 : " +  effectDate_3);
    Optional<CdBaseRate> cdBaseRateT = null;
    if (dbName.equals(ContentName.onDay))
      cdBaseRateT = cdBaseRateReposDay.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3);
    else if (dbName.equals(ContentName.onMon))
      cdBaseRateT = cdBaseRateReposMon.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3);
    else if (dbName.equals(ContentName.onHist))
      cdBaseRateT = cdBaseRateReposHist.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3);
    else 
      cdBaseRateT = cdBaseRateRepos.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3);

    return cdBaseRateT.isPresent() ? cdBaseRateT.get() : null;
  }

  @Override
  public CdBaseRate baseRateCodeAscFirst(String currencyCode_0, String baseRateCode_1, int effectDate_2, int effectDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("baseRateCodeAscFirst " + dbName + " : " + "currencyCode_0 : " + currencyCode_0 + " baseRateCode_1 : " +  baseRateCode_1 + " effectDate_2 : " +  effectDate_2 + " effectDate_3 : " +  effectDate_3);
    Optional<CdBaseRate> cdBaseRateT = null;
    if (dbName.equals(ContentName.onDay))
      cdBaseRateT = cdBaseRateReposDay.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3);
    else if (dbName.equals(ContentName.onMon))
      cdBaseRateT = cdBaseRateReposMon.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3);
    else if (dbName.equals(ContentName.onHist))
      cdBaseRateT = cdBaseRateReposHist.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3);
    else 
      cdBaseRateT = cdBaseRateRepos.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectDate_2, effectDate_3);

    return cdBaseRateT.isPresent() ? cdBaseRateT.get() : null;
  }

  @Override
  public Slice<CdBaseRate> baseRateCodeRange(String currencyCode_0, String baseRateCode_1, String baseRateCode_2, int effectDate_3, int effectDate_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBaseRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("baseRateCodeRange " + dbName + " : " + "currencyCode_0 : " + currencyCode_0 + " baseRateCode_1 : " +  baseRateCode_1 + " baseRateCode_2 : " +  baseRateCode_2 + " effectDate_3 : " +  effectDate_3 + " effectDate_4 : " +  effectDate_4);
    if (dbName.equals(ContentName.onDay))
      slice = cdBaseRateReposDay.findAllByCurrencyCodeIsAndBaseRateCodeGreaterThanEqualAndBaseRateCodeLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByBaseRateCodeAscEffectDateAsc(currencyCode_0, baseRateCode_1, baseRateCode_2, effectDate_3, effectDate_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBaseRateReposMon.findAllByCurrencyCodeIsAndBaseRateCodeGreaterThanEqualAndBaseRateCodeLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByBaseRateCodeAscEffectDateAsc(currencyCode_0, baseRateCode_1, baseRateCode_2, effectDate_3, effectDate_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBaseRateReposHist.findAllByCurrencyCodeIsAndBaseRateCodeGreaterThanEqualAndBaseRateCodeLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByBaseRateCodeAscEffectDateAsc(currencyCode_0, baseRateCode_1, baseRateCode_2, effectDate_3, effectDate_4, pageable);
    else 
      slice = cdBaseRateRepos.findAllByCurrencyCodeIsAndBaseRateCodeGreaterThanEqualAndBaseRateCodeLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByBaseRateCodeAscEffectDateAsc(currencyCode_0, baseRateCode_1, baseRateCode_2, effectDate_3, effectDate_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBaseRate> effectFlagEq(String currencyCode_0, String baseRateCode_1, int effectFlag_2, int effectDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBaseRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("effectFlagEq " + dbName + " : " + "currencyCode_0 : " + currencyCode_0 + " baseRateCode_1 : " +  baseRateCode_1 + " effectFlag_2 : " +  effectFlag_2 + " effectDate_3 : " +  effectDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = cdBaseRateReposDay.findAllByCurrencyCodeIsAndBaseRateCodeIsAndEffectFlagIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectFlag_2, effectDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBaseRateReposMon.findAllByCurrencyCodeIsAndBaseRateCodeIsAndEffectFlagIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectFlag_2, effectDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBaseRateReposHist.findAllByCurrencyCodeIsAndBaseRateCodeIsAndEffectFlagIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectFlag_2, effectDate_3, pageable);
    else 
      slice = cdBaseRateRepos.findAllByCurrencyCodeIsAndBaseRateCodeIsAndEffectFlagIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, effectFlag_2, effectDate_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdBaseRate effectFlagDescFirst(String currencyCode_0, String baseRateCode_1, int effectFlag_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("effectFlagDescFirst " + dbName + " : " + "currencyCode_0 : " + currencyCode_0 + " baseRateCode_1 : " +  baseRateCode_1 + " effectFlag_2 : " +  effectFlag_2);
    Optional<CdBaseRate> cdBaseRateT = null;
    if (dbName.equals(ContentName.onDay))
      cdBaseRateT = cdBaseRateReposDay.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectFlagIsOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, effectFlag_2);
    else if (dbName.equals(ContentName.onMon))
      cdBaseRateT = cdBaseRateReposMon.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectFlagIsOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, effectFlag_2);
    else if (dbName.equals(ContentName.onHist))
      cdBaseRateT = cdBaseRateReposHist.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectFlagIsOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, effectFlag_2);
    else 
      cdBaseRateT = cdBaseRateRepos.findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectFlagIsOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, effectFlag_2);

    return cdBaseRateT.isPresent() ? cdBaseRateT.get() : null;
  }

  @Override
  public Slice<CdBaseRate> baseRateCodeEq2(String currencyCode_0, String baseRateCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBaseRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("baseRateCodeEq2 " + dbName + " : " + "currencyCode_0 : " + currencyCode_0 + " baseRateCode_1 : " +  baseRateCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdBaseRateReposDay.findAllByCurrencyCodeIsAndBaseRateCodeIsOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBaseRateReposMon.findAllByCurrencyCodeIsAndBaseRateCodeIsOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBaseRateReposHist.findAllByCurrencyCodeIsAndBaseRateCodeIsOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, pageable);
    else 
      slice = cdBaseRateRepos.findAllByCurrencyCodeIsAndBaseRateCodeIsOrderByEffectDateAsc(currencyCode_0, baseRateCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBaseRate> effectFlagDescFirst1(String currencyCode_0, String baseRateCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBaseRate> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("effectFlagDescFirst1 " + dbName + " : " + "currencyCode_0 : " + currencyCode_0 + " baseRateCode_1 : " +  baseRateCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdBaseRateReposDay.findAllByCurrencyCodeIsAndBaseRateCodeIsOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBaseRateReposMon.findAllByCurrencyCodeIsAndBaseRateCodeIsOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBaseRateReposHist.findAllByCurrencyCodeIsAndBaseRateCodeIsOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, pageable);
    else 
      slice = cdBaseRateRepos.findAllByCurrencyCodeIsAndBaseRateCodeIsOrderByEffectDateDesc(currencyCode_0, baseRateCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdBaseRate holdById(CdBaseRateId cdBaseRateId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdBaseRateId);
    Optional<CdBaseRate> cdBaseRate = null;
    if (dbName.equals(ContentName.onDay))
      cdBaseRate = cdBaseRateReposDay.findByCdBaseRateId(cdBaseRateId);
    else if (dbName.equals(ContentName.onMon))
      cdBaseRate = cdBaseRateReposMon.findByCdBaseRateId(cdBaseRateId);
    else if (dbName.equals(ContentName.onHist))
      cdBaseRate = cdBaseRateReposHist.findByCdBaseRateId(cdBaseRateId);
    else 
      cdBaseRate = cdBaseRateRepos.findByCdBaseRateId(cdBaseRateId);
    return cdBaseRate.isPresent() ? cdBaseRate.get() : null;
  }

  @Override
  public CdBaseRate holdById(CdBaseRate cdBaseRate, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdBaseRate.getCdBaseRateId());
    Optional<CdBaseRate> cdBaseRateT = null;
    if (dbName.equals(ContentName.onDay))
      cdBaseRateT = cdBaseRateReposDay.findByCdBaseRateId(cdBaseRate.getCdBaseRateId());
    else if (dbName.equals(ContentName.onMon))
      cdBaseRateT = cdBaseRateReposMon.findByCdBaseRateId(cdBaseRate.getCdBaseRateId());
    else if (dbName.equals(ContentName.onHist))
      cdBaseRateT = cdBaseRateReposHist.findByCdBaseRateId(cdBaseRate.getCdBaseRateId());
    else 
      cdBaseRateT = cdBaseRateRepos.findByCdBaseRateId(cdBaseRate.getCdBaseRateId());
    return cdBaseRateT.isPresent() ? cdBaseRateT.get() : null;
  }

  @Override
  public CdBaseRate insert(CdBaseRate cdBaseRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdBaseRate.getCdBaseRateId());
    if (this.findById(cdBaseRate.getCdBaseRateId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdBaseRate.setCreateEmpNo(empNot);

    if(cdBaseRate.getLastUpdateEmpNo() == null || cdBaseRate.getLastUpdateEmpNo().isEmpty())
      cdBaseRate.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBaseRateReposDay.saveAndFlush(cdBaseRate);	
    else if (dbName.equals(ContentName.onMon))
      return cdBaseRateReposMon.saveAndFlush(cdBaseRate);
    else if (dbName.equals(ContentName.onHist))
      return cdBaseRateReposHist.saveAndFlush(cdBaseRate);
    else 
    return cdBaseRateRepos.saveAndFlush(cdBaseRate);
  }

  @Override
  public CdBaseRate update(CdBaseRate cdBaseRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdBaseRate.getCdBaseRateId());
    if (!empNot.isEmpty())
      cdBaseRate.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBaseRateReposDay.saveAndFlush(cdBaseRate);	
    else if (dbName.equals(ContentName.onMon))
      return cdBaseRateReposMon.saveAndFlush(cdBaseRate);
    else if (dbName.equals(ContentName.onHist))
      return cdBaseRateReposHist.saveAndFlush(cdBaseRate);
    else 
    return cdBaseRateRepos.saveAndFlush(cdBaseRate);
  }

  @Override
  public CdBaseRate update2(CdBaseRate cdBaseRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdBaseRate.getCdBaseRateId());
    if (!empNot.isEmpty())
      cdBaseRate.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdBaseRateReposDay.saveAndFlush(cdBaseRate);	
    else if (dbName.equals(ContentName.onMon))
      cdBaseRateReposMon.saveAndFlush(cdBaseRate);
    else if (dbName.equals(ContentName.onHist))
        cdBaseRateReposHist.saveAndFlush(cdBaseRate);
    else 
      cdBaseRateRepos.saveAndFlush(cdBaseRate);	
    return this.findById(cdBaseRate.getCdBaseRateId());
  }

  @Override
  public void delete(CdBaseRate cdBaseRate, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdBaseRate.getCdBaseRateId());
    if (dbName.equals(ContentName.onDay)) {
      cdBaseRateReposDay.delete(cdBaseRate);	
      cdBaseRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBaseRateReposMon.delete(cdBaseRate);	
      cdBaseRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBaseRateReposHist.delete(cdBaseRate);
      cdBaseRateReposHist.flush();
    }
    else {
      cdBaseRateRepos.delete(cdBaseRate);
      cdBaseRateRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdBaseRate> cdBaseRate, TitaVo... titaVo) throws DBException {
    if (cdBaseRate == null || cdBaseRate.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdBaseRate t : cdBaseRate){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdBaseRate = cdBaseRateReposDay.saveAll(cdBaseRate);	
      cdBaseRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBaseRate = cdBaseRateReposMon.saveAll(cdBaseRate);	
      cdBaseRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBaseRate = cdBaseRateReposHist.saveAll(cdBaseRate);
      cdBaseRateReposHist.flush();
    }
    else {
      cdBaseRate = cdBaseRateRepos.saveAll(cdBaseRate);
      cdBaseRateRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdBaseRate> cdBaseRate, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdBaseRate == null || cdBaseRate.size() == 0)
      throw new DBException(6);

    for (CdBaseRate t : cdBaseRate) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdBaseRate = cdBaseRateReposDay.saveAll(cdBaseRate);	
      cdBaseRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBaseRate = cdBaseRateReposMon.saveAll(cdBaseRate);	
      cdBaseRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBaseRate = cdBaseRateReposHist.saveAll(cdBaseRate);
      cdBaseRateReposHist.flush();
    }
    else {
      cdBaseRate = cdBaseRateRepos.saveAll(cdBaseRate);
      cdBaseRateRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdBaseRate> cdBaseRate, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdBaseRate == null || cdBaseRate.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdBaseRateReposDay.deleteAll(cdBaseRate);	
      cdBaseRateReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBaseRateReposMon.deleteAll(cdBaseRate);	
      cdBaseRateReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBaseRateReposHist.deleteAll(cdBaseRate);
      cdBaseRateReposHist.flush();
    }
    else {
      cdBaseRateRepos.deleteAll(cdBaseRate);
      cdBaseRateRepos.flush();
    }
  }

}
