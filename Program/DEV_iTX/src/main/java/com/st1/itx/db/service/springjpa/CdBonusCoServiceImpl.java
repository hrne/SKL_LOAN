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
import com.st1.itx.db.domain.CdBonusCo;
import com.st1.itx.db.domain.CdBonusCoId;
import com.st1.itx.db.repository.online.CdBonusCoRepository;
import com.st1.itx.db.repository.day.CdBonusCoRepositoryDay;
import com.st1.itx.db.repository.mon.CdBonusCoRepositoryMon;
import com.st1.itx.db.repository.hist.CdBonusCoRepositoryHist;
import com.st1.itx.db.service.CdBonusCoService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdBonusCoService")
@Repository
public class CdBonusCoServiceImpl implements CdBonusCoService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CdBonusCoServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdBonusCoRepository cdBonusCoRepos;

  @Autowired
  private CdBonusCoRepositoryDay cdBonusCoReposDay;

  @Autowired
  private CdBonusCoRepositoryMon cdBonusCoReposMon;

  @Autowired
  private CdBonusCoRepositoryHist cdBonusCoReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdBonusCoRepos);
    org.junit.Assert.assertNotNull(cdBonusCoReposDay);
    org.junit.Assert.assertNotNull(cdBonusCoReposMon);
    org.junit.Assert.assertNotNull(cdBonusCoReposHist);
  }

  @Override
  public CdBonusCo findById(CdBonusCoId cdBonusCoId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + cdBonusCoId);
    Optional<CdBonusCo> cdBonusCo = null;
    if (dbName.equals(ContentName.onDay))
      cdBonusCo = cdBonusCoReposDay.findById(cdBonusCoId);
    else if (dbName.equals(ContentName.onMon))
      cdBonusCo = cdBonusCoReposMon.findById(cdBonusCoId);
    else if (dbName.equals(ContentName.onHist))
      cdBonusCo = cdBonusCoReposHist.findById(cdBonusCoId);
    else 
      cdBonusCo = cdBonusCoRepos.findById(cdBonusCoId);
    CdBonusCo obj = cdBonusCo.isPresent() ? cdBonusCo.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdBonusCo> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBonusCo> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "WorkMonth", "ConditionCode", "Condition"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdBonusCoReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBonusCoReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBonusCoReposHist.findAll(pageable);
    else 
      slice = cdBonusCoRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBonusCo> findCondition(int workMonth_0, int conditionCode_1, String condition_2, String condition_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBonusCo> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findCondition " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " conditionCode_1 : " +  conditionCode_1 + " condition_2 : " +  condition_2 + " condition_3 : " +  condition_3);
    if (dbName.equals(ContentName.onDay))
      slice = cdBonusCoReposDay.findAllByWorkMonthIsAndConditionCodeIsAndConditionGreaterThanEqualAndConditionLessThanEqualOrderByWorkMonthAscConditionCodeAscConditionAsc(workMonth_0, conditionCode_1, condition_2, condition_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBonusCoReposMon.findAllByWorkMonthIsAndConditionCodeIsAndConditionGreaterThanEqualAndConditionLessThanEqualOrderByWorkMonthAscConditionCodeAscConditionAsc(workMonth_0, conditionCode_1, condition_2, condition_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBonusCoReposHist.findAllByWorkMonthIsAndConditionCodeIsAndConditionGreaterThanEqualAndConditionLessThanEqualOrderByWorkMonthAscConditionCodeAscConditionAsc(workMonth_0, conditionCode_1, condition_2, condition_3, pageable);
    else 
      slice = cdBonusCoRepos.findAllByWorkMonthIsAndConditionCodeIsAndConditionGreaterThanEqualAndConditionLessThanEqualOrderByWorkMonthAscConditionCodeAscConditionAsc(workMonth_0, conditionCode_1, condition_2, condition_3, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBonusCo> findYearMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBonusCo> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findYearMonth " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " workMonth_1 : " +  workMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdBonusCoReposDay.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBonusCoReposMon.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBonusCoReposHist.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);
    else 
      slice = cdBonusCoRepos.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdBonusCo findWorkMonthFirst(int workMonth_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findWorkMonthFirst " + dbName + " : " + "workMonth_0 : " + workMonth_0);
    Optional<CdBonusCo> cdBonusCoT = null;
    if (dbName.equals(ContentName.onDay))
      cdBonusCoT = cdBonusCoReposDay.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);
    else if (dbName.equals(ContentName.onMon))
      cdBonusCoT = cdBonusCoReposMon.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);
    else if (dbName.equals(ContentName.onHist))
      cdBonusCoT = cdBonusCoReposHist.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);
    else 
      cdBonusCoT = cdBonusCoRepos.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);
    return cdBonusCoT.isPresent() ? cdBonusCoT.get() : null;
  }

  @Override
  public CdBonusCo holdById(CdBonusCoId cdBonusCoId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdBonusCoId);
    Optional<CdBonusCo> cdBonusCo = null;
    if (dbName.equals(ContentName.onDay))
      cdBonusCo = cdBonusCoReposDay.findByCdBonusCoId(cdBonusCoId);
    else if (dbName.equals(ContentName.onMon))
      cdBonusCo = cdBonusCoReposMon.findByCdBonusCoId(cdBonusCoId);
    else if (dbName.equals(ContentName.onHist))
      cdBonusCo = cdBonusCoReposHist.findByCdBonusCoId(cdBonusCoId);
    else 
      cdBonusCo = cdBonusCoRepos.findByCdBonusCoId(cdBonusCoId);
    return cdBonusCo.isPresent() ? cdBonusCo.get() : null;
  }

  @Override
  public CdBonusCo holdById(CdBonusCo cdBonusCo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdBonusCo.getCdBonusCoId());
    Optional<CdBonusCo> cdBonusCoT = null;
    if (dbName.equals(ContentName.onDay))
      cdBonusCoT = cdBonusCoReposDay.findByCdBonusCoId(cdBonusCo.getCdBonusCoId());
    else if (dbName.equals(ContentName.onMon))
      cdBonusCoT = cdBonusCoReposMon.findByCdBonusCoId(cdBonusCo.getCdBonusCoId());
    else if (dbName.equals(ContentName.onHist))
      cdBonusCoT = cdBonusCoReposHist.findByCdBonusCoId(cdBonusCo.getCdBonusCoId());
    else 
      cdBonusCoT = cdBonusCoRepos.findByCdBonusCoId(cdBonusCo.getCdBonusCoId());
    return cdBonusCoT.isPresent() ? cdBonusCoT.get() : null;
  }

  @Override
  public CdBonusCo insert(CdBonusCo cdBonusCo, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + cdBonusCo.getCdBonusCoId());
    if (this.findById(cdBonusCo.getCdBonusCoId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdBonusCo.setCreateEmpNo(empNot);

    if(cdBonusCo.getLastUpdateEmpNo() == null || cdBonusCo.getLastUpdateEmpNo().isEmpty())
      cdBonusCo.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBonusCoReposDay.saveAndFlush(cdBonusCo);	
    else if (dbName.equals(ContentName.onMon))
      return cdBonusCoReposMon.saveAndFlush(cdBonusCo);
    else if (dbName.equals(ContentName.onHist))
      return cdBonusCoReposHist.saveAndFlush(cdBonusCo);
    else 
    return cdBonusCoRepos.saveAndFlush(cdBonusCo);
  }

  @Override
  public CdBonusCo update(CdBonusCo cdBonusCo, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdBonusCo.getCdBonusCoId());
    if (!empNot.isEmpty())
      cdBonusCo.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBonusCoReposDay.saveAndFlush(cdBonusCo);	
    else if (dbName.equals(ContentName.onMon))
      return cdBonusCoReposMon.saveAndFlush(cdBonusCo);
    else if (dbName.equals(ContentName.onHist))
      return cdBonusCoReposHist.saveAndFlush(cdBonusCo);
    else 
    return cdBonusCoRepos.saveAndFlush(cdBonusCo);
  }

  @Override
  public CdBonusCo update2(CdBonusCo cdBonusCo, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdBonusCo.getCdBonusCoId());
    if (!empNot.isEmpty())
      cdBonusCo.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdBonusCoReposDay.saveAndFlush(cdBonusCo);	
    else if (dbName.equals(ContentName.onMon))
      cdBonusCoReposMon.saveAndFlush(cdBonusCo);
    else if (dbName.equals(ContentName.onHist))
        cdBonusCoReposHist.saveAndFlush(cdBonusCo);
    else 
      cdBonusCoRepos.saveAndFlush(cdBonusCo);	
    return this.findById(cdBonusCo.getCdBonusCoId());
  }

  @Override
  public void delete(CdBonusCo cdBonusCo, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + cdBonusCo.getCdBonusCoId());
    if (dbName.equals(ContentName.onDay)) {
      cdBonusCoReposDay.delete(cdBonusCo);	
      cdBonusCoReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBonusCoReposMon.delete(cdBonusCo);	
      cdBonusCoReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBonusCoReposHist.delete(cdBonusCo);
      cdBonusCoReposHist.flush();
    }
    else {
      cdBonusCoRepos.delete(cdBonusCo);
      cdBonusCoRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdBonusCo> cdBonusCo, TitaVo... titaVo) throws DBException {
    if (cdBonusCo == null || cdBonusCo.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (CdBonusCo t : cdBonusCo){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdBonusCo = cdBonusCoReposDay.saveAll(cdBonusCo);	
      cdBonusCoReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBonusCo = cdBonusCoReposMon.saveAll(cdBonusCo);	
      cdBonusCoReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBonusCo = cdBonusCoReposHist.saveAll(cdBonusCo);
      cdBonusCoReposHist.flush();
    }
    else {
      cdBonusCo = cdBonusCoRepos.saveAll(cdBonusCo);
      cdBonusCoRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdBonusCo> cdBonusCo, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (cdBonusCo == null || cdBonusCo.size() == 0)
      throw new DBException(6);

    for (CdBonusCo t : cdBonusCo) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdBonusCo = cdBonusCoReposDay.saveAll(cdBonusCo);	
      cdBonusCoReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBonusCo = cdBonusCoReposMon.saveAll(cdBonusCo);	
      cdBonusCoReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBonusCo = cdBonusCoReposHist.saveAll(cdBonusCo);
      cdBonusCoReposHist.flush();
    }
    else {
      cdBonusCo = cdBonusCoRepos.saveAll(cdBonusCo);
      cdBonusCoRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdBonusCo> cdBonusCo, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdBonusCo == null || cdBonusCo.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdBonusCoReposDay.deleteAll(cdBonusCo);	
      cdBonusCoReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBonusCoReposMon.deleteAll(cdBonusCo);	
      cdBonusCoReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBonusCoReposHist.deleteAll(cdBonusCo);
      cdBonusCoReposHist.flush();
    }
    else {
      cdBonusCoRepos.deleteAll(cdBonusCo);
      cdBonusCoRepos.flush();
    }
  }

}
