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
import com.st1.itx.db.domain.CdCashFlow;
import com.st1.itx.db.repository.online.CdCashFlowRepository;
import com.st1.itx.db.repository.day.CdCashFlowRepositoryDay;
import com.st1.itx.db.repository.mon.CdCashFlowRepositoryMon;
import com.st1.itx.db.repository.hist.CdCashFlowRepositoryHist;
import com.st1.itx.db.service.CdCashFlowService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdCashFlowService")
@Repository
public class CdCashFlowServiceImpl implements CdCashFlowService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CdCashFlowServiceImpl.class);

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
  public CdCashFlow findById(int dataYearMonth, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + dataYearMonth);
    Optional<CdCashFlow> cdCashFlow = null;
    if (dbName.equals(ContentName.onDay))
      cdCashFlow = cdCashFlowReposDay.findById(dataYearMonth);
    else if (dbName.equals(ContentName.onMon))
      cdCashFlow = cdCashFlowReposMon.findById(dataYearMonth);
    else if (dbName.equals(ContentName.onHist))
      cdCashFlow = cdCashFlowReposHist.findById(dataYearMonth);
    else 
      cdCashFlow = cdCashFlowRepos.findById(dataYearMonth);
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
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYearMonth"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdCashFlowReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCashFlowReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCashFlowReposHist.findAll(pageable);
    else 
      slice = cdCashFlowRepos.findAll(pageable);

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
    logger.info("findDataYearMonth " + dbName + " : " + "dataYearMonth_0 : " + dataYearMonth_0 + " dataYearMonth_1 : " +  dataYearMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdCashFlowReposDay.findAllByDataYearMonthGreaterThanEqualAndDataYearMonthLessThanEqualOrderByDataYearMonthAsc(dataYearMonth_0, dataYearMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdCashFlowReposMon.findAllByDataYearMonthGreaterThanEqualAndDataYearMonthLessThanEqualOrderByDataYearMonthAsc(dataYearMonth_0, dataYearMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdCashFlowReposHist.findAllByDataYearMonthGreaterThanEqualAndDataYearMonthLessThanEqualOrderByDataYearMonthAsc(dataYearMonth_0, dataYearMonth_1, pageable);
    else 
      slice = cdCashFlowRepos.findAllByDataYearMonthGreaterThanEqualAndDataYearMonthLessThanEqualOrderByDataYearMonthAsc(dataYearMonth_0, dataYearMonth_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdCashFlow holdById(int dataYearMonth, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + dataYearMonth);
    Optional<CdCashFlow> cdCashFlow = null;
    if (dbName.equals(ContentName.onDay))
      cdCashFlow = cdCashFlowReposDay.findByDataYearMonth(dataYearMonth);
    else if (dbName.equals(ContentName.onMon))
      cdCashFlow = cdCashFlowReposMon.findByDataYearMonth(dataYearMonth);
    else if (dbName.equals(ContentName.onHist))
      cdCashFlow = cdCashFlowReposHist.findByDataYearMonth(dataYearMonth);
    else 
      cdCashFlow = cdCashFlowRepos.findByDataYearMonth(dataYearMonth);
    return cdCashFlow.isPresent() ? cdCashFlow.get() : null;
  }

  @Override
  public CdCashFlow holdById(CdCashFlow cdCashFlow, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdCashFlow.getDataYearMonth());
    Optional<CdCashFlow> cdCashFlowT = null;
    if (dbName.equals(ContentName.onDay))
      cdCashFlowT = cdCashFlowReposDay.findByDataYearMonth(cdCashFlow.getDataYearMonth());
    else if (dbName.equals(ContentName.onMon))
      cdCashFlowT = cdCashFlowReposMon.findByDataYearMonth(cdCashFlow.getDataYearMonth());
    else if (dbName.equals(ContentName.onHist))
      cdCashFlowT = cdCashFlowReposHist.findByDataYearMonth(cdCashFlow.getDataYearMonth());
    else 
      cdCashFlowT = cdCashFlowRepos.findByDataYearMonth(cdCashFlow.getDataYearMonth());
    return cdCashFlowT.isPresent() ? cdCashFlowT.get() : null;
  }

  @Override
  public CdCashFlow insert(CdCashFlow cdCashFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + cdCashFlow.getDataYearMonth());
    if (this.findById(cdCashFlow.getDataYearMonth()) != null)
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
		}
    logger.info("Update..." + dbName + " " + cdCashFlow.getDataYearMonth());
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
		}
    logger.info("Update..." + dbName + " " + cdCashFlow.getDataYearMonth());
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
    return this.findById(cdCashFlow.getDataYearMonth());
  }

  @Override
  public void delete(CdCashFlow cdCashFlow, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + cdCashFlow.getDataYearMonth());
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
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
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
		}
    logger.info("UpdateAll...");
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
    logger.info("DeleteAll...");
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
