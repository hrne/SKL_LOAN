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
import com.st1.itx.db.domain.CdBonus;
import com.st1.itx.db.domain.CdBonusId;
import com.st1.itx.db.repository.online.CdBonusRepository;
import com.st1.itx.db.repository.day.CdBonusRepositoryDay;
import com.st1.itx.db.repository.mon.CdBonusRepositoryMon;
import com.st1.itx.db.repository.hist.CdBonusRepositoryHist;
import com.st1.itx.db.service.CdBonusService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdBonusService")
@Repository
public class CdBonusServiceImpl extends ASpringJpaParm implements CdBonusService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdBonusRepository cdBonusRepos;

  @Autowired
  private CdBonusRepositoryDay cdBonusReposDay;

  @Autowired
  private CdBonusRepositoryMon cdBonusReposMon;

  @Autowired
  private CdBonusRepositoryHist cdBonusReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdBonusRepos);
    org.junit.Assert.assertNotNull(cdBonusReposDay);
    org.junit.Assert.assertNotNull(cdBonusReposMon);
    org.junit.Assert.assertNotNull(cdBonusReposHist);
  }

  @Override
  public CdBonus findById(CdBonusId cdBonusId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdBonusId);
    Optional<CdBonus> cdBonus = null;
    if (dbName.equals(ContentName.onDay))
      cdBonus = cdBonusReposDay.findById(cdBonusId);
    else if (dbName.equals(ContentName.onMon))
      cdBonus = cdBonusReposMon.findById(cdBonusId);
    else if (dbName.equals(ContentName.onHist))
      cdBonus = cdBonusReposHist.findById(cdBonusId);
    else 
      cdBonus = cdBonusRepos.findById(cdBonusId);
    CdBonus obj = cdBonus.isPresent() ? cdBonus.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdBonus> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBonus> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "WorkMonth", "ConditionCode", "Condition"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "WorkMonth", "ConditionCode", "Condition"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdBonusReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBonusReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBonusReposHist.findAll(pageable);
    else 
      slice = cdBonusRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBonus> findCondition(int workMonth_0, int conditionCode_1, String condition_2, String condition_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBonus> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCondition " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " conditionCode_1 : " +  conditionCode_1 + " condition_2 : " +  condition_2 + " condition_3 : " +  condition_3);
    if (dbName.equals(ContentName.onDay))
      slice = cdBonusReposDay.findAllByWorkMonthIsAndConditionCodeIsAndConditionGreaterThanEqualAndConditionLessThanEqualOrderByWorkMonthAscConditionCodeAscConditionAsc(workMonth_0, conditionCode_1, condition_2, condition_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBonusReposMon.findAllByWorkMonthIsAndConditionCodeIsAndConditionGreaterThanEqualAndConditionLessThanEqualOrderByWorkMonthAscConditionCodeAscConditionAsc(workMonth_0, conditionCode_1, condition_2, condition_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBonusReposHist.findAllByWorkMonthIsAndConditionCodeIsAndConditionGreaterThanEqualAndConditionLessThanEqualOrderByWorkMonthAscConditionCodeAscConditionAsc(workMonth_0, conditionCode_1, condition_2, condition_3, pageable);
    else 
      slice = cdBonusRepos.findAllByWorkMonthIsAndConditionCodeIsAndConditionGreaterThanEqualAndConditionLessThanEqualOrderByWorkMonthAscConditionCodeAscConditionAsc(workMonth_0, conditionCode_1, condition_2, condition_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBonus> findYearMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBonus> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findYearMonth " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " workMonth_1 : " +  workMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdBonusReposDay.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBonusReposMon.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBonusReposHist.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);
    else 
      slice = cdBonusRepos.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdBonus findWorkMonthFirst(int workMonth_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findWorkMonthFirst " + dbName + " : " + "workMonth_0 : " + workMonth_0);
    Optional<CdBonus> cdBonusT = null;
    if (dbName.equals(ContentName.onDay))
      cdBonusT = cdBonusReposDay.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);
    else if (dbName.equals(ContentName.onMon))
      cdBonusT = cdBonusReposMon.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);
    else if (dbName.equals(ContentName.onHist))
      cdBonusT = cdBonusReposHist.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);
    else 
      cdBonusT = cdBonusRepos.findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(workMonth_0);

    return cdBonusT.isPresent() ? cdBonusT.get() : null;
  }

  @Override
  public CdBonus holdById(CdBonusId cdBonusId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdBonusId);
    Optional<CdBonus> cdBonus = null;
    if (dbName.equals(ContentName.onDay))
      cdBonus = cdBonusReposDay.findByCdBonusId(cdBonusId);
    else if (dbName.equals(ContentName.onMon))
      cdBonus = cdBonusReposMon.findByCdBonusId(cdBonusId);
    else if (dbName.equals(ContentName.onHist))
      cdBonus = cdBonusReposHist.findByCdBonusId(cdBonusId);
    else 
      cdBonus = cdBonusRepos.findByCdBonusId(cdBonusId);
    return cdBonus.isPresent() ? cdBonus.get() : null;
  }

  @Override
  public CdBonus holdById(CdBonus cdBonus, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdBonus.getCdBonusId());
    Optional<CdBonus> cdBonusT = null;
    if (dbName.equals(ContentName.onDay))
      cdBonusT = cdBonusReposDay.findByCdBonusId(cdBonus.getCdBonusId());
    else if (dbName.equals(ContentName.onMon))
      cdBonusT = cdBonusReposMon.findByCdBonusId(cdBonus.getCdBonusId());
    else if (dbName.equals(ContentName.onHist))
      cdBonusT = cdBonusReposHist.findByCdBonusId(cdBonus.getCdBonusId());
    else 
      cdBonusT = cdBonusRepos.findByCdBonusId(cdBonus.getCdBonusId());
    return cdBonusT.isPresent() ? cdBonusT.get() : null;
  }

  @Override
  public CdBonus insert(CdBonus cdBonus, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdBonus.getCdBonusId());
    if (this.findById(cdBonus.getCdBonusId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdBonus.setCreateEmpNo(empNot);

    if(cdBonus.getLastUpdateEmpNo() == null || cdBonus.getLastUpdateEmpNo().isEmpty())
      cdBonus.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBonusReposDay.saveAndFlush(cdBonus);	
    else if (dbName.equals(ContentName.onMon))
      return cdBonusReposMon.saveAndFlush(cdBonus);
    else if (dbName.equals(ContentName.onHist))
      return cdBonusReposHist.saveAndFlush(cdBonus);
    else 
    return cdBonusRepos.saveAndFlush(cdBonus);
  }

  @Override
  public CdBonus update(CdBonus cdBonus, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdBonus.getCdBonusId());
    if (!empNot.isEmpty())
      cdBonus.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBonusReposDay.saveAndFlush(cdBonus);	
    else if (dbName.equals(ContentName.onMon))
      return cdBonusReposMon.saveAndFlush(cdBonus);
    else if (dbName.equals(ContentName.onHist))
      return cdBonusReposHist.saveAndFlush(cdBonus);
    else 
    return cdBonusRepos.saveAndFlush(cdBonus);
  }

  @Override
  public CdBonus update2(CdBonus cdBonus, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdBonus.getCdBonusId());
    if (!empNot.isEmpty())
      cdBonus.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdBonusReposDay.saveAndFlush(cdBonus);	
    else if (dbName.equals(ContentName.onMon))
      cdBonusReposMon.saveAndFlush(cdBonus);
    else if (dbName.equals(ContentName.onHist))
        cdBonusReposHist.saveAndFlush(cdBonus);
    else 
      cdBonusRepos.saveAndFlush(cdBonus);	
    return this.findById(cdBonus.getCdBonusId());
  }

  @Override
  public void delete(CdBonus cdBonus, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdBonus.getCdBonusId());
    if (dbName.equals(ContentName.onDay)) {
      cdBonusReposDay.delete(cdBonus);	
      cdBonusReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBonusReposMon.delete(cdBonus);	
      cdBonusReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBonusReposHist.delete(cdBonus);
      cdBonusReposHist.flush();
    }
    else {
      cdBonusRepos.delete(cdBonus);
      cdBonusRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdBonus> cdBonus, TitaVo... titaVo) throws DBException {
    if (cdBonus == null || cdBonus.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdBonus t : cdBonus){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdBonus = cdBonusReposDay.saveAll(cdBonus);	
      cdBonusReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBonus = cdBonusReposMon.saveAll(cdBonus);	
      cdBonusReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBonus = cdBonusReposHist.saveAll(cdBonus);
      cdBonusReposHist.flush();
    }
    else {
      cdBonus = cdBonusRepos.saveAll(cdBonus);
      cdBonusRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdBonus> cdBonus, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdBonus == null || cdBonus.size() == 0)
      throw new DBException(6);

    for (CdBonus t : cdBonus) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdBonus = cdBonusReposDay.saveAll(cdBonus);	
      cdBonusReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBonus = cdBonusReposMon.saveAll(cdBonus);	
      cdBonusReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBonus = cdBonusReposHist.saveAll(cdBonus);
      cdBonusReposHist.flush();
    }
    else {
      cdBonus = cdBonusRepos.saveAll(cdBonus);
      cdBonusRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdBonus> cdBonus, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdBonus == null || cdBonus.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdBonusReposDay.deleteAll(cdBonus);	
      cdBonusReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBonusReposMon.deleteAll(cdBonus);	
      cdBonusReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBonusReposHist.deleteAll(cdBonus);
      cdBonusReposHist.flush();
    }
    else {
      cdBonusRepos.deleteAll(cdBonus);
      cdBonusRepos.flush();
    }
  }

}
