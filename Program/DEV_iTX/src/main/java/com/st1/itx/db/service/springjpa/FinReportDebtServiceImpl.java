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
import com.st1.itx.db.domain.FinReportDebt;
import com.st1.itx.db.domain.FinReportDebtId;
import com.st1.itx.db.repository.online.FinReportDebtRepository;
import com.st1.itx.db.repository.day.FinReportDebtRepositoryDay;
import com.st1.itx.db.repository.mon.FinReportDebtRepositoryMon;
import com.st1.itx.db.repository.hist.FinReportDebtRepositoryHist;
import com.st1.itx.db.service.FinReportDebtService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("finReportDebtService")
@Repository
public class FinReportDebtServiceImpl extends ASpringJpaParm implements FinReportDebtService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private FinReportDebtRepository finReportDebtRepos;

  @Autowired
  private FinReportDebtRepositoryDay finReportDebtReposDay;

  @Autowired
  private FinReportDebtRepositoryMon finReportDebtReposMon;

  @Autowired
  private FinReportDebtRepositoryHist finReportDebtReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(finReportDebtRepos);
    org.junit.Assert.assertNotNull(finReportDebtReposDay);
    org.junit.Assert.assertNotNull(finReportDebtReposMon);
    org.junit.Assert.assertNotNull(finReportDebtReposHist);
  }

  @Override
  public FinReportDebt findById(FinReportDebtId finReportDebtId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + finReportDebtId);
    Optional<FinReportDebt> finReportDebt = null;
    if (dbName.equals(ContentName.onDay))
      finReportDebt = finReportDebtReposDay.findById(finReportDebtId);
    else if (dbName.equals(ContentName.onMon))
      finReportDebt = finReportDebtReposMon.findById(finReportDebtId);
    else if (dbName.equals(ContentName.onHist))
      finReportDebt = finReportDebtReposHist.findById(finReportDebtId);
    else 
      finReportDebt = finReportDebtRepos.findById(finReportDebtId);
    FinReportDebt obj = finReportDebt.isPresent() ? finReportDebt.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<FinReportDebt> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FinReportDebt> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustUKey", "UKey"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustUKey", "UKey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = finReportDebtReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = finReportDebtReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = finReportDebtReposHist.findAll(pageable);
    else 
      slice = finReportDebtRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FinReportDebt> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FinReportDebt> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustUKey " + dbName + " : " + "custUKey_0 : " + custUKey_0);
    if (dbName.equals(ContentName.onDay))
      slice = finReportDebtReposDay.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = finReportDebtReposMon.findAllByCustUKeyIs(custUKey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = finReportDebtReposHist.findAllByCustUKeyIs(custUKey_0, pageable);
    else 
      slice = finReportDebtRepos.findAllByCustUKeyIs(custUKey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FinReportDebt findCustUKeyYearFirst(String custUKey_0, int startYY_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findCustUKeyYearFirst " + dbName + " : " + "custUKey_0 : " + custUKey_0 + " startYY_1 : " +  startYY_1);
    Optional<FinReportDebt> finReportDebtT = null;
    if (dbName.equals(ContentName.onDay))
      finReportDebtT = finReportDebtReposDay.findTopByCustUKeyIsAndStartYYIs(custUKey_0, startYY_1);
    else if (dbName.equals(ContentName.onMon))
      finReportDebtT = finReportDebtReposMon.findTopByCustUKeyIsAndStartYYIs(custUKey_0, startYY_1);
    else if (dbName.equals(ContentName.onHist))
      finReportDebtT = finReportDebtReposHist.findTopByCustUKeyIsAndStartYYIs(custUKey_0, startYY_1);
    else 
      finReportDebtT = finReportDebtRepos.findTopByCustUKeyIsAndStartYYIs(custUKey_0, startYY_1);

    return finReportDebtT.isPresent() ? finReportDebtT.get() : null;
  }

  @Override
  public FinReportDebt holdById(FinReportDebtId finReportDebtId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finReportDebtId);
    Optional<FinReportDebt> finReportDebt = null;
    if (dbName.equals(ContentName.onDay))
      finReportDebt = finReportDebtReposDay.findByFinReportDebtId(finReportDebtId);
    else if (dbName.equals(ContentName.onMon))
      finReportDebt = finReportDebtReposMon.findByFinReportDebtId(finReportDebtId);
    else if (dbName.equals(ContentName.onHist))
      finReportDebt = finReportDebtReposHist.findByFinReportDebtId(finReportDebtId);
    else 
      finReportDebt = finReportDebtRepos.findByFinReportDebtId(finReportDebtId);
    return finReportDebt.isPresent() ? finReportDebt.get() : null;
  }

  @Override
  public FinReportDebt holdById(FinReportDebt finReportDebt, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + finReportDebt.getFinReportDebtId());
    Optional<FinReportDebt> finReportDebtT = null;
    if (dbName.equals(ContentName.onDay))
      finReportDebtT = finReportDebtReposDay.findByFinReportDebtId(finReportDebt.getFinReportDebtId());
    else if (dbName.equals(ContentName.onMon))
      finReportDebtT = finReportDebtReposMon.findByFinReportDebtId(finReportDebt.getFinReportDebtId());
    else if (dbName.equals(ContentName.onHist))
      finReportDebtT = finReportDebtReposHist.findByFinReportDebtId(finReportDebt.getFinReportDebtId());
    else 
      finReportDebtT = finReportDebtRepos.findByFinReportDebtId(finReportDebt.getFinReportDebtId());
    return finReportDebtT.isPresent() ? finReportDebtT.get() : null;
  }

  @Override
  public FinReportDebt insert(FinReportDebt finReportDebt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + finReportDebt.getFinReportDebtId());
    if (this.findById(finReportDebt.getFinReportDebtId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      finReportDebt.setCreateEmpNo(empNot);

    if(finReportDebt.getLastUpdateEmpNo() == null || finReportDebt.getLastUpdateEmpNo().isEmpty())
      finReportDebt.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finReportDebtReposDay.saveAndFlush(finReportDebt);	
    else if (dbName.equals(ContentName.onMon))
      return finReportDebtReposMon.saveAndFlush(finReportDebt);
    else if (dbName.equals(ContentName.onHist))
      return finReportDebtReposHist.saveAndFlush(finReportDebt);
    else 
    return finReportDebtRepos.saveAndFlush(finReportDebt);
  }

  @Override
  public FinReportDebt update(FinReportDebt finReportDebt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + finReportDebt.getFinReportDebtId());
    if (!empNot.isEmpty())
      finReportDebt.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return finReportDebtReposDay.saveAndFlush(finReportDebt);	
    else if (dbName.equals(ContentName.onMon))
      return finReportDebtReposMon.saveAndFlush(finReportDebt);
    else if (dbName.equals(ContentName.onHist))
      return finReportDebtReposHist.saveAndFlush(finReportDebt);
    else 
    return finReportDebtRepos.saveAndFlush(finReportDebt);
  }

  @Override
  public FinReportDebt update2(FinReportDebt finReportDebt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + finReportDebt.getFinReportDebtId());
    if (!empNot.isEmpty())
      finReportDebt.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      finReportDebtReposDay.saveAndFlush(finReportDebt);	
    else if (dbName.equals(ContentName.onMon))
      finReportDebtReposMon.saveAndFlush(finReportDebt);
    else if (dbName.equals(ContentName.onHist))
        finReportDebtReposHist.saveAndFlush(finReportDebt);
    else 
      finReportDebtRepos.saveAndFlush(finReportDebt);	
    return this.findById(finReportDebt.getFinReportDebtId());
  }

  @Override
  public void delete(FinReportDebt finReportDebt, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + finReportDebt.getFinReportDebtId());
    if (dbName.equals(ContentName.onDay)) {
      finReportDebtReposDay.delete(finReportDebt);	
      finReportDebtReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportDebtReposMon.delete(finReportDebt);	
      finReportDebtReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportDebtReposHist.delete(finReportDebt);
      finReportDebtReposHist.flush();
    }
    else {
      finReportDebtRepos.delete(finReportDebt);
      finReportDebtRepos.flush();
    }
   }

  @Override
  public void insertAll(List<FinReportDebt> finReportDebt, TitaVo... titaVo) throws DBException {
    if (finReportDebt == null || finReportDebt.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (FinReportDebt t : finReportDebt){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      finReportDebt = finReportDebtReposDay.saveAll(finReportDebt);	
      finReportDebtReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportDebt = finReportDebtReposMon.saveAll(finReportDebt);	
      finReportDebtReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportDebt = finReportDebtReposHist.saveAll(finReportDebt);
      finReportDebtReposHist.flush();
    }
    else {
      finReportDebt = finReportDebtRepos.saveAll(finReportDebt);
      finReportDebtRepos.flush();
    }
    }

  @Override
  public void updateAll(List<FinReportDebt> finReportDebt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (finReportDebt == null || finReportDebt.size() == 0)
      throw new DBException(6);

    for (FinReportDebt t : finReportDebt) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      finReportDebt = finReportDebtReposDay.saveAll(finReportDebt);	
      finReportDebtReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportDebt = finReportDebtReposMon.saveAll(finReportDebt);	
      finReportDebtReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportDebt = finReportDebtReposHist.saveAll(finReportDebt);
      finReportDebtReposHist.flush();
    }
    else {
      finReportDebt = finReportDebtRepos.saveAll(finReportDebt);
      finReportDebtRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<FinReportDebt> finReportDebt, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (finReportDebt == null || finReportDebt.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      finReportDebtReposDay.deleteAll(finReportDebt);	
      finReportDebtReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      finReportDebtReposMon.deleteAll(finReportDebt);	
      finReportDebtReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      finReportDebtReposHist.deleteAll(finReportDebt);
      finReportDebtReposHist.flush();
    }
    else {
      finReportDebtRepos.deleteAll(finReportDebt);
      finReportDebtRepos.flush();
    }
  }

}
