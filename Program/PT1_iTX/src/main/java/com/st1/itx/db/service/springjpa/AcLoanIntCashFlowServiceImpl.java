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
import com.st1.itx.db.domain.AcLoanIntCashFlow;
import com.st1.itx.db.domain.AcLoanIntCashFlowId;
import com.st1.itx.db.repository.online.AcLoanIntCashFlowRepository;
import com.st1.itx.db.repository.day.AcLoanIntCashFlowRepositoryDay;
import com.st1.itx.db.repository.mon.AcLoanIntCashFlowRepositoryMon;
import com.st1.itx.db.repository.hist.AcLoanIntCashFlowRepositoryHist;
import com.st1.itx.db.service.AcLoanIntCashFlowService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("acLoanIntCashFlowService")
@Repository
public class AcLoanIntCashFlowServiceImpl extends ASpringJpaParm implements AcLoanIntCashFlowService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AcLoanIntCashFlowRepository acLoanIntCashFlowRepos;

  @Autowired
  private AcLoanIntCashFlowRepositoryDay acLoanIntCashFlowReposDay;

  @Autowired
  private AcLoanIntCashFlowRepositoryMon acLoanIntCashFlowReposMon;

  @Autowired
  private AcLoanIntCashFlowRepositoryHist acLoanIntCashFlowReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(acLoanIntCashFlowRepos);
    org.junit.Assert.assertNotNull(acLoanIntCashFlowReposDay);
    org.junit.Assert.assertNotNull(acLoanIntCashFlowReposMon);
    org.junit.Assert.assertNotNull(acLoanIntCashFlowReposHist);
  }

  @Override
  public AcLoanIntCashFlow findById(AcLoanIntCashFlowId acLoanIntCashFlowId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + acLoanIntCashFlowId);
    Optional<AcLoanIntCashFlow> acLoanIntCashFlow = null;
    if (dbName.equals(ContentName.onDay))
      acLoanIntCashFlow = acLoanIntCashFlowReposDay.findById(acLoanIntCashFlowId);
    else if (dbName.equals(ContentName.onMon))
      acLoanIntCashFlow = acLoanIntCashFlowReposMon.findById(acLoanIntCashFlowId);
    else if (dbName.equals(ContentName.onHist))
      acLoanIntCashFlow = acLoanIntCashFlowReposHist.findById(acLoanIntCashFlowId);
    else 
      acLoanIntCashFlow = acLoanIntCashFlowRepos.findById(acLoanIntCashFlowId);
    AcLoanIntCashFlow obj = acLoanIntCashFlow.isPresent() ? acLoanIntCashFlow.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AcLoanIntCashFlow> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcLoanIntCashFlow> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "BormNo", "TermNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "BormNo", "TermNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = acLoanIntCashFlowReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acLoanIntCashFlowReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acLoanIntCashFlowReposHist.findAll(pageable);
    else 
      slice = acLoanIntCashFlowRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcLoanIntCashFlow> findYearMonthEq(int yearMonth_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcLoanIntCashFlow> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findYearMonthEq " + dbName + " : " + "yearMonth_0 : " + yearMonth_0);
    if (dbName.equals(ContentName.onDay))
      slice = acLoanIntCashFlowReposDay.findAllByYearMonthIsOrderByCustNoAscFacmNoAscBormNoAscTermNoAsc(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acLoanIntCashFlowReposMon.findAllByYearMonthIsOrderByCustNoAscFacmNoAscBormNoAscTermNoAsc(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acLoanIntCashFlowReposHist.findAllByYearMonthIsOrderByCustNoAscFacmNoAscBormNoAscTermNoAsc(yearMonth_0, pageable);
    else 
      slice = acLoanIntCashFlowRepos.findAllByYearMonthIsOrderByCustNoAscFacmNoAscBormNoAscTermNoAsc(yearMonth_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AcLoanIntCashFlow holdById(AcLoanIntCashFlowId acLoanIntCashFlowId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acLoanIntCashFlowId);
    Optional<AcLoanIntCashFlow> acLoanIntCashFlow = null;
    if (dbName.equals(ContentName.onDay))
      acLoanIntCashFlow = acLoanIntCashFlowReposDay.findByAcLoanIntCashFlowId(acLoanIntCashFlowId);
    else if (dbName.equals(ContentName.onMon))
      acLoanIntCashFlow = acLoanIntCashFlowReposMon.findByAcLoanIntCashFlowId(acLoanIntCashFlowId);
    else if (dbName.equals(ContentName.onHist))
      acLoanIntCashFlow = acLoanIntCashFlowReposHist.findByAcLoanIntCashFlowId(acLoanIntCashFlowId);
    else 
      acLoanIntCashFlow = acLoanIntCashFlowRepos.findByAcLoanIntCashFlowId(acLoanIntCashFlowId);
    return acLoanIntCashFlow.isPresent() ? acLoanIntCashFlow.get() : null;
  }

  @Override
  public AcLoanIntCashFlow holdById(AcLoanIntCashFlow acLoanIntCashFlow, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acLoanIntCashFlow.getAcLoanIntCashFlowId());
    Optional<AcLoanIntCashFlow> acLoanIntCashFlowT = null;
    if (dbName.equals(ContentName.onDay))
      acLoanIntCashFlowT = acLoanIntCashFlowReposDay.findByAcLoanIntCashFlowId(acLoanIntCashFlow.getAcLoanIntCashFlowId());
    else if (dbName.equals(ContentName.onMon))
      acLoanIntCashFlowT = acLoanIntCashFlowReposMon.findByAcLoanIntCashFlowId(acLoanIntCashFlow.getAcLoanIntCashFlowId());
    else if (dbName.equals(ContentName.onHist))
      acLoanIntCashFlowT = acLoanIntCashFlowReposHist.findByAcLoanIntCashFlowId(acLoanIntCashFlow.getAcLoanIntCashFlowId());
    else 
      acLoanIntCashFlowT = acLoanIntCashFlowRepos.findByAcLoanIntCashFlowId(acLoanIntCashFlow.getAcLoanIntCashFlowId());
    return acLoanIntCashFlowT.isPresent() ? acLoanIntCashFlowT.get() : null;
  }

  @Override
  public AcLoanIntCashFlow insert(AcLoanIntCashFlow acLoanIntCashFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + acLoanIntCashFlow.getAcLoanIntCashFlowId());
    if (this.findById(acLoanIntCashFlow.getAcLoanIntCashFlowId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      acLoanIntCashFlow.setCreateEmpNo(empNot);

    if(acLoanIntCashFlow.getLastUpdateEmpNo() == null || acLoanIntCashFlow.getLastUpdateEmpNo().isEmpty())
      acLoanIntCashFlow.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acLoanIntCashFlowReposDay.saveAndFlush(acLoanIntCashFlow);	
    else if (dbName.equals(ContentName.onMon))
      return acLoanIntCashFlowReposMon.saveAndFlush(acLoanIntCashFlow);
    else if (dbName.equals(ContentName.onHist))
      return acLoanIntCashFlowReposHist.saveAndFlush(acLoanIntCashFlow);
    else 
    return acLoanIntCashFlowRepos.saveAndFlush(acLoanIntCashFlow);
  }

  @Override
  public AcLoanIntCashFlow update(AcLoanIntCashFlow acLoanIntCashFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acLoanIntCashFlow.getAcLoanIntCashFlowId());
    if (!empNot.isEmpty())
      acLoanIntCashFlow.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acLoanIntCashFlowReposDay.saveAndFlush(acLoanIntCashFlow);	
    else if (dbName.equals(ContentName.onMon))
      return acLoanIntCashFlowReposMon.saveAndFlush(acLoanIntCashFlow);
    else if (dbName.equals(ContentName.onHist))
      return acLoanIntCashFlowReposHist.saveAndFlush(acLoanIntCashFlow);
    else 
    return acLoanIntCashFlowRepos.saveAndFlush(acLoanIntCashFlow);
  }

  @Override
  public AcLoanIntCashFlow update2(AcLoanIntCashFlow acLoanIntCashFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acLoanIntCashFlow.getAcLoanIntCashFlowId());
    if (!empNot.isEmpty())
      acLoanIntCashFlow.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      acLoanIntCashFlowReposDay.saveAndFlush(acLoanIntCashFlow);	
    else if (dbName.equals(ContentName.onMon))
      acLoanIntCashFlowReposMon.saveAndFlush(acLoanIntCashFlow);
    else if (dbName.equals(ContentName.onHist))
        acLoanIntCashFlowReposHist.saveAndFlush(acLoanIntCashFlow);
    else 
      acLoanIntCashFlowRepos.saveAndFlush(acLoanIntCashFlow);	
    return this.findById(acLoanIntCashFlow.getAcLoanIntCashFlowId());
  }

  @Override
  public void delete(AcLoanIntCashFlow acLoanIntCashFlow, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + acLoanIntCashFlow.getAcLoanIntCashFlowId());
    if (dbName.equals(ContentName.onDay)) {
      acLoanIntCashFlowReposDay.delete(acLoanIntCashFlow);	
      acLoanIntCashFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanIntCashFlowReposMon.delete(acLoanIntCashFlow);	
      acLoanIntCashFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanIntCashFlowReposHist.delete(acLoanIntCashFlow);
      acLoanIntCashFlowReposHist.flush();
    }
    else {
      acLoanIntCashFlowRepos.delete(acLoanIntCashFlow);
      acLoanIntCashFlowRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AcLoanIntCashFlow> acLoanIntCashFlow, TitaVo... titaVo) throws DBException {
    if (acLoanIntCashFlow == null || acLoanIntCashFlow.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (AcLoanIntCashFlow t : acLoanIntCashFlow){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      acLoanIntCashFlow = acLoanIntCashFlowReposDay.saveAll(acLoanIntCashFlow);	
      acLoanIntCashFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanIntCashFlow = acLoanIntCashFlowReposMon.saveAll(acLoanIntCashFlow);	
      acLoanIntCashFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanIntCashFlow = acLoanIntCashFlowReposHist.saveAll(acLoanIntCashFlow);
      acLoanIntCashFlowReposHist.flush();
    }
    else {
      acLoanIntCashFlow = acLoanIntCashFlowRepos.saveAll(acLoanIntCashFlow);
      acLoanIntCashFlowRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AcLoanIntCashFlow> acLoanIntCashFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (acLoanIntCashFlow == null || acLoanIntCashFlow.size() == 0)
      throw new DBException(6);

    for (AcLoanIntCashFlow t : acLoanIntCashFlow) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      acLoanIntCashFlow = acLoanIntCashFlowReposDay.saveAll(acLoanIntCashFlow);	
      acLoanIntCashFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanIntCashFlow = acLoanIntCashFlowReposMon.saveAll(acLoanIntCashFlow);	
      acLoanIntCashFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanIntCashFlow = acLoanIntCashFlowReposHist.saveAll(acLoanIntCashFlow);
      acLoanIntCashFlowReposHist.flush();
    }
    else {
      acLoanIntCashFlow = acLoanIntCashFlowRepos.saveAll(acLoanIntCashFlow);
      acLoanIntCashFlowRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AcLoanIntCashFlow> acLoanIntCashFlow, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (acLoanIntCashFlow == null || acLoanIntCashFlow.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      acLoanIntCashFlowReposDay.deleteAll(acLoanIntCashFlow);	
      acLoanIntCashFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acLoanIntCashFlowReposMon.deleteAll(acLoanIntCashFlow);	
      acLoanIntCashFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acLoanIntCashFlowReposHist.deleteAll(acLoanIntCashFlow);
      acLoanIntCashFlowReposHist.flush();
    }
    else {
      acLoanIntCashFlowRepos.deleteAll(acLoanIntCashFlow);
      acLoanIntCashFlowRepos.flush();
    }
  }

}
