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
import com.st1.itx.db.domain.CdCashFlow;
import com.st1.itx.db.domain.CdCashFlowId;
import com.st1.itx.db.repository.online.CdCashFlowRepository;
import com.st1.itx.db.repository.day.CdCashFlowRepositoryDay;
import com.st1.itx.db.repository.mon.CdCashFlowRepositoryMon;
import com.st1.itx.db.repository.hist.CdCashFlowRepositoryHist;
import com.st1.itx.db.service.CdCashFlowService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdCashFlowService")
@Repository
public class CdCashFlowServiceImpl extends ASpringJpaParm implements CdCashFlowService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdCashFlowRepository cdCashFlowRepos;

  @Autowired
  private CdCashFlowRepositoryDay cdCashFlowReposDay;

  @Autowired
  private CdCashFlowRepositoryMon cdCashFlowReposMon;

  @Autowired
  private CdCashFlowRepositoryHist cdCashFlowReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdCashFlowRepos);
    org.junit.Assert.assertNotNull(cdCashFlowReposDay);
    org.junit.Assert.assertNotNull(cdCashFlowReposMon);
    org.junit.Assert.assertNotNull(cdCashFlowReposHist);
  }

  @Override
  public CdCashFlow findById(CdCashFlowId cdCashFlowId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdCashFlowId);
    Optional<CdCashFlow> cdCashFlow = null;
    if (dbName.equals(ContentName.onDay))
      cdCashFlow = cdCashFlowReposDay.findById(cdCashFlowId);
    else if (dbName.equals(ContentName.onMon))
      cdCashFlow = cdCashFlowReposMon.findById(cdCashFlowId);
    else if (dbName.equals(ContentName.onHist))
      cdCashFlow = cdCashFlowReposHist.findById(cdCashFlowId);
    else 
      cdCashFlow = cdCashFlowRepos.findById(cdCashFlowId);
    CdCashFlow obj = cdCashFlow.isPresent() ? cdCashFlow.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdCashFlow> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCashFlow> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "BranchNo", "DataYearMonth", "TenDayPeriods"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BranchNo", "DataYearMonth", "TenDayPeriods"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdCashFlowReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCashFlowReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCashFlowReposHist.findAll(pageable);
    else 
      slice = cdCashFlowRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdCashFlow> findDataYearMonth(int dataYearMonth_0, int dataYearMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdCashFlow> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findDataYearMonth " + dbName + " : " + "dataYearMonth_0 : " + dataYearMonth_0 + " dataYearMonth_1 : " +  dataYearMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdCashFlowReposDay.findAllByDataYearMonthGreaterThanEqualAndDataYearMonthLessThanEqualOrderByDataYearMonthAsc(dataYearMonth_0, dataYearMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCashFlowReposMon.findAllByDataYearMonthGreaterThanEqualAndDataYearMonthLessThanEqualOrderByDataYearMonthAsc(dataYearMonth_0, dataYearMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCashFlowReposHist.findAllByDataYearMonthGreaterThanEqualAndDataYearMonthLessThanEqualOrderByDataYearMonthAsc(dataYearMonth_0, dataYearMonth_1, pageable);
    else 
      slice = cdCashFlowRepos.findAllByDataYearMonthGreaterThanEqualAndDataYearMonthLessThanEqualOrderByDataYearMonthAsc(dataYearMonth_0, dataYearMonth_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdCashFlow findDataYearMonthFirst(String branchNo_0, int dataYearMonth_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findDataYearMonthFirst " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " dataYearMonth_1 : " +  dataYearMonth_1);
    Optional<CdCashFlow> cdCashFlowT = null;
    if (dbName.equals(ContentName.onDay))
      cdCashFlowT = cdCashFlowReposDay.findTopByBranchNoIsAndDataYearMonthIsOrderByBranchNoAscDataYearMonthAscTenDayPeriodsDesc(branchNo_0, dataYearMonth_1);
    else if (dbName.equals(ContentName.onMon))
      cdCashFlowT = cdCashFlowReposMon.findTopByBranchNoIsAndDataYearMonthIsOrderByBranchNoAscDataYearMonthAscTenDayPeriodsDesc(branchNo_0, dataYearMonth_1);
    else if (dbName.equals(ContentName.onHist))
      cdCashFlowT = cdCashFlowReposHist.findTopByBranchNoIsAndDataYearMonthIsOrderByBranchNoAscDataYearMonthAscTenDayPeriodsDesc(branchNo_0, dataYearMonth_1);
    else 
      cdCashFlowT = cdCashFlowRepos.findTopByBranchNoIsAndDataYearMonthIsOrderByBranchNoAscDataYearMonthAscTenDayPeriodsDesc(branchNo_0, dataYearMonth_1);

    return cdCashFlowT.isPresent() ? cdCashFlowT.get() : null;
  }

  @Override
  public CdCashFlow holdById(CdCashFlowId cdCashFlowId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdCashFlowId);
    Optional<CdCashFlow> cdCashFlow = null;
    if (dbName.equals(ContentName.onDay))
      cdCashFlow = cdCashFlowReposDay.findByCdCashFlowId(cdCashFlowId);
    else if (dbName.equals(ContentName.onMon))
      cdCashFlow = cdCashFlowReposMon.findByCdCashFlowId(cdCashFlowId);
    else if (dbName.equals(ContentName.onHist))
      cdCashFlow = cdCashFlowReposHist.findByCdCashFlowId(cdCashFlowId);
    else 
      cdCashFlow = cdCashFlowRepos.findByCdCashFlowId(cdCashFlowId);
    return cdCashFlow.isPresent() ? cdCashFlow.get() : null;
  }

  @Override
  public CdCashFlow holdById(CdCashFlow cdCashFlow, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdCashFlow.getCdCashFlowId());
    Optional<CdCashFlow> cdCashFlowT = null;
    if (dbName.equals(ContentName.onDay))
      cdCashFlowT = cdCashFlowReposDay.findByCdCashFlowId(cdCashFlow.getCdCashFlowId());
    else if (dbName.equals(ContentName.onMon))
      cdCashFlowT = cdCashFlowReposMon.findByCdCashFlowId(cdCashFlow.getCdCashFlowId());
    else if (dbName.equals(ContentName.onHist))
      cdCashFlowT = cdCashFlowReposHist.findByCdCashFlowId(cdCashFlow.getCdCashFlowId());
    else 
      cdCashFlowT = cdCashFlowRepos.findByCdCashFlowId(cdCashFlow.getCdCashFlowId());
    return cdCashFlowT.isPresent() ? cdCashFlowT.get() : null;
  }

  @Override
  public CdCashFlow insert(CdCashFlow cdCashFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdCashFlow.getCdCashFlowId());
    if (this.findById(cdCashFlow.getCdCashFlowId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdCashFlow.setCreateEmpNo(empNot);

    if(cdCashFlow.getLastUpdateEmpNo() == null || cdCashFlow.getLastUpdateEmpNo().isEmpty())
      cdCashFlow.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdCashFlowReposDay.saveAndFlush(cdCashFlow);	
    else if (dbName.equals(ContentName.onMon))
      return cdCashFlowReposMon.saveAndFlush(cdCashFlow);
    else if (dbName.equals(ContentName.onHist))
      return cdCashFlowReposHist.saveAndFlush(cdCashFlow);
    else 
    return cdCashFlowRepos.saveAndFlush(cdCashFlow);
  }

  @Override
  public CdCashFlow update(CdCashFlow cdCashFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdCashFlow.getCdCashFlowId());
    if (!empNot.isEmpty())
      cdCashFlow.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdCashFlowReposDay.saveAndFlush(cdCashFlow);	
    else if (dbName.equals(ContentName.onMon))
      return cdCashFlowReposMon.saveAndFlush(cdCashFlow);
    else if (dbName.equals(ContentName.onHist))
      return cdCashFlowReposHist.saveAndFlush(cdCashFlow);
    else 
    return cdCashFlowRepos.saveAndFlush(cdCashFlow);
  }

  @Override
  public CdCashFlow update2(CdCashFlow cdCashFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdCashFlow.getCdCashFlowId());
    if (!empNot.isEmpty())
      cdCashFlow.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdCashFlowReposDay.saveAndFlush(cdCashFlow);	
    else if (dbName.equals(ContentName.onMon))
      cdCashFlowReposMon.saveAndFlush(cdCashFlow);
    else if (dbName.equals(ContentName.onHist))
        cdCashFlowReposHist.saveAndFlush(cdCashFlow);
    else 
      cdCashFlowRepos.saveAndFlush(cdCashFlow);	
    return this.findById(cdCashFlow.getCdCashFlowId());
  }

  @Override
  public void delete(CdCashFlow cdCashFlow, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdCashFlow.getCdCashFlowId());
    if (dbName.equals(ContentName.onDay)) {
      cdCashFlowReposDay.delete(cdCashFlow);	
      cdCashFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCashFlowReposMon.delete(cdCashFlow);	
      cdCashFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCashFlowReposHist.delete(cdCashFlow);
      cdCashFlowReposHist.flush();
    }
    else {
      cdCashFlowRepos.delete(cdCashFlow);
      cdCashFlowRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdCashFlow> cdCashFlow, TitaVo... titaVo) throws DBException {
    if (cdCashFlow == null || cdCashFlow.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdCashFlow t : cdCashFlow){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdCashFlow = cdCashFlowReposDay.saveAll(cdCashFlow);	
      cdCashFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCashFlow = cdCashFlowReposMon.saveAll(cdCashFlow);	
      cdCashFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCashFlow = cdCashFlowReposHist.saveAll(cdCashFlow);
      cdCashFlowReposHist.flush();
    }
    else {
      cdCashFlow = cdCashFlowRepos.saveAll(cdCashFlow);
      cdCashFlowRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdCashFlow> cdCashFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdCashFlow == null || cdCashFlow.size() == 0)
      throw new DBException(6);

    for (CdCashFlow t : cdCashFlow) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdCashFlow = cdCashFlowReposDay.saveAll(cdCashFlow);	
      cdCashFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCashFlow = cdCashFlowReposMon.saveAll(cdCashFlow);	
      cdCashFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCashFlow = cdCashFlowReposHist.saveAll(cdCashFlow);
      cdCashFlowReposHist.flush();
    }
    else {
      cdCashFlow = cdCashFlowRepos.saveAll(cdCashFlow);
      cdCashFlowRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdCashFlow> cdCashFlow, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdCashFlow == null || cdCashFlow.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdCashFlowReposDay.deleteAll(cdCashFlow);	
      cdCashFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdCashFlowReposMon.deleteAll(cdCashFlow);	
      cdCashFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdCashFlowReposHist.deleteAll(cdCashFlow);
      cdCashFlowReposHist.flush();
    }
    else {
      cdCashFlowRepos.deleteAll(cdCashFlow);
      cdCashFlowRepos.flush();
    }
  }

}
