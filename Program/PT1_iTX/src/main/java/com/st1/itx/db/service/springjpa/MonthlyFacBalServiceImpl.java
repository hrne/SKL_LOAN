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
import com.st1.itx.db.domain.MonthlyFacBal;
import com.st1.itx.db.domain.MonthlyFacBalId;
import com.st1.itx.db.repository.online.MonthlyFacBalRepository;
import com.st1.itx.db.repository.day.MonthlyFacBalRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyFacBalRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyFacBalRepositoryHist;
import com.st1.itx.db.service.MonthlyFacBalService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyFacBalService")
@Repository
public class MonthlyFacBalServiceImpl extends ASpringJpaParm implements MonthlyFacBalService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private MonthlyFacBalRepository monthlyFacBalRepos;

  @Autowired
  private MonthlyFacBalRepositoryDay monthlyFacBalReposDay;

  @Autowired
  private MonthlyFacBalRepositoryMon monthlyFacBalReposMon;

  @Autowired
  private MonthlyFacBalRepositoryHist monthlyFacBalReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(monthlyFacBalRepos);
    org.junit.Assert.assertNotNull(monthlyFacBalReposDay);
    org.junit.Assert.assertNotNull(monthlyFacBalReposMon);
    org.junit.Assert.assertNotNull(monthlyFacBalReposHist);
  }

  @Override
  public MonthlyFacBal findById(MonthlyFacBalId monthlyFacBalId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + monthlyFacBalId);
    Optional<MonthlyFacBal> monthlyFacBal = null;
    if (dbName.equals(ContentName.onDay))
      monthlyFacBal = monthlyFacBalReposDay.findById(monthlyFacBalId);
    else if (dbName.equals(ContentName.onMon))
      monthlyFacBal = monthlyFacBalReposMon.findById(monthlyFacBalId);
    else if (dbName.equals(ContentName.onHist))
      monthlyFacBal = monthlyFacBalReposHist.findById(monthlyFacBalId);
    else 
      monthlyFacBal = monthlyFacBalRepos.findById(monthlyFacBalId);
    MonthlyFacBal obj = monthlyFacBal.isPresent() ? monthlyFacBal.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<MonthlyFacBal> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyFacBal> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyFacBalReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyFacBalReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyFacBalReposHist.findAll(pageable);
    else 
      slice = monthlyFacBalRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<MonthlyFacBal> findCl(int clCustNo_0, int clFacmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyFacBal> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCl " + dbName + " : " + "clCustNo_0 : " + clCustNo_0 + " clFacmNo_1 : " +  clFacmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyFacBalReposDay.findAllByClCustNoIsAndClFacmNoIs(clCustNo_0, clFacmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyFacBalReposMon.findAllByClCustNoIsAndClFacmNoIs(clCustNo_0, clFacmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyFacBalReposHist.findAllByClCustNoIsAndClFacmNoIs(clCustNo_0, clFacmNo_1, pageable);
    else 
      slice = monthlyFacBalRepos.findAllByClCustNoIsAndClFacmNoIs(clCustNo_0, clFacmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public MonthlyFacBal holdById(MonthlyFacBalId monthlyFacBalId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyFacBalId);
    Optional<MonthlyFacBal> monthlyFacBal = null;
    if (dbName.equals(ContentName.onDay))
      monthlyFacBal = monthlyFacBalReposDay.findByMonthlyFacBalId(monthlyFacBalId);
    else if (dbName.equals(ContentName.onMon))
      monthlyFacBal = monthlyFacBalReposMon.findByMonthlyFacBalId(monthlyFacBalId);
    else if (dbName.equals(ContentName.onHist))
      monthlyFacBal = monthlyFacBalReposHist.findByMonthlyFacBalId(monthlyFacBalId);
    else 
      monthlyFacBal = monthlyFacBalRepos.findByMonthlyFacBalId(monthlyFacBalId);
    return monthlyFacBal.isPresent() ? monthlyFacBal.get() : null;
  }

  @Override
  public MonthlyFacBal holdById(MonthlyFacBal monthlyFacBal, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyFacBal.getMonthlyFacBalId());
    Optional<MonthlyFacBal> monthlyFacBalT = null;
    if (dbName.equals(ContentName.onDay))
      monthlyFacBalT = monthlyFacBalReposDay.findByMonthlyFacBalId(monthlyFacBal.getMonthlyFacBalId());
    else if (dbName.equals(ContentName.onMon))
      monthlyFacBalT = monthlyFacBalReposMon.findByMonthlyFacBalId(monthlyFacBal.getMonthlyFacBalId());
    else if (dbName.equals(ContentName.onHist))
      monthlyFacBalT = monthlyFacBalReposHist.findByMonthlyFacBalId(monthlyFacBal.getMonthlyFacBalId());
    else 
      monthlyFacBalT = monthlyFacBalRepos.findByMonthlyFacBalId(monthlyFacBal.getMonthlyFacBalId());
    return monthlyFacBalT.isPresent() ? monthlyFacBalT.get() : null;
  }

  @Override
  public MonthlyFacBal insert(MonthlyFacBal monthlyFacBal, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + monthlyFacBal.getMonthlyFacBalId());
    if (this.findById(monthlyFacBal.getMonthlyFacBalId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      monthlyFacBal.setCreateEmpNo(empNot);

    if(monthlyFacBal.getLastUpdateEmpNo() == null || monthlyFacBal.getLastUpdateEmpNo().isEmpty())
      monthlyFacBal.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyFacBalReposDay.saveAndFlush(monthlyFacBal);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyFacBalReposMon.saveAndFlush(monthlyFacBal);
    else if (dbName.equals(ContentName.onHist))
      return monthlyFacBalReposHist.saveAndFlush(monthlyFacBal);
    else 
    return monthlyFacBalRepos.saveAndFlush(monthlyFacBal);
  }

  @Override
  public MonthlyFacBal update(MonthlyFacBal monthlyFacBal, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyFacBal.getMonthlyFacBalId());
    if (!empNot.isEmpty())
      monthlyFacBal.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyFacBalReposDay.saveAndFlush(monthlyFacBal);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyFacBalReposMon.saveAndFlush(monthlyFacBal);
    else if (dbName.equals(ContentName.onHist))
      return monthlyFacBalReposHist.saveAndFlush(monthlyFacBal);
    else 
    return monthlyFacBalRepos.saveAndFlush(monthlyFacBal);
  }

  @Override
  public MonthlyFacBal update2(MonthlyFacBal monthlyFacBal, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyFacBal.getMonthlyFacBalId());
    if (!empNot.isEmpty())
      monthlyFacBal.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      monthlyFacBalReposDay.saveAndFlush(monthlyFacBal);	
    else if (dbName.equals(ContentName.onMon))
      monthlyFacBalReposMon.saveAndFlush(monthlyFacBal);
    else if (dbName.equals(ContentName.onHist))
        monthlyFacBalReposHist.saveAndFlush(monthlyFacBal);
    else 
      monthlyFacBalRepos.saveAndFlush(monthlyFacBal);	
    return this.findById(monthlyFacBal.getMonthlyFacBalId());
  }

  @Override
  public void delete(MonthlyFacBal monthlyFacBal, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + monthlyFacBal.getMonthlyFacBalId());
    if (dbName.equals(ContentName.onDay)) {
      monthlyFacBalReposDay.delete(monthlyFacBal);	
      monthlyFacBalReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyFacBalReposMon.delete(monthlyFacBal);	
      monthlyFacBalReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyFacBalReposHist.delete(monthlyFacBal);
      monthlyFacBalReposHist.flush();
    }
    else {
      monthlyFacBalRepos.delete(monthlyFacBal);
      monthlyFacBalRepos.flush();
    }
   }

  @Override
  public void insertAll(List<MonthlyFacBal> monthlyFacBal, TitaVo... titaVo) throws DBException {
    if (monthlyFacBal == null || monthlyFacBal.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (MonthlyFacBal t : monthlyFacBal){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      monthlyFacBal = monthlyFacBalReposDay.saveAll(monthlyFacBal);	
      monthlyFacBalReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyFacBal = monthlyFacBalReposMon.saveAll(monthlyFacBal);	
      monthlyFacBalReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyFacBal = monthlyFacBalReposHist.saveAll(monthlyFacBal);
      monthlyFacBalReposHist.flush();
    }
    else {
      monthlyFacBal = monthlyFacBalRepos.saveAll(monthlyFacBal);
      monthlyFacBalRepos.flush();
    }
    }

  @Override
  public void updateAll(List<MonthlyFacBal> monthlyFacBal, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (monthlyFacBal == null || monthlyFacBal.size() == 0)
      throw new DBException(6);

    for (MonthlyFacBal t : monthlyFacBal) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      monthlyFacBal = monthlyFacBalReposDay.saveAll(monthlyFacBal);	
      monthlyFacBalReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyFacBal = monthlyFacBalReposMon.saveAll(monthlyFacBal);	
      monthlyFacBalReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyFacBal = monthlyFacBalReposHist.saveAll(monthlyFacBal);
      monthlyFacBalReposHist.flush();
    }
    else {
      monthlyFacBal = monthlyFacBalRepos.saveAll(monthlyFacBal);
      monthlyFacBalRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<MonthlyFacBal> monthlyFacBal, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (monthlyFacBal == null || monthlyFacBal.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      monthlyFacBalReposDay.deleteAll(monthlyFacBal);	
      monthlyFacBalReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyFacBalReposMon.deleteAll(monthlyFacBal);	
      monthlyFacBalReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyFacBalReposHist.deleteAll(monthlyFacBal);
      monthlyFacBalReposHist.flush();
    }
    else {
      monthlyFacBalRepos.deleteAll(monthlyFacBal);
      monthlyFacBalRepos.flush();
    }
  }

  @Override
  public void Usp_L9_MonthlyFacBal_Upd(int YYYYMM,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      monthlyFacBalReposDay.uspL9MonthlyfacbalUpd(YYYYMM,  empNo);
    else if (dbName.equals(ContentName.onMon))
      monthlyFacBalReposMon.uspL9MonthlyfacbalUpd(YYYYMM,  empNo);
    else if (dbName.equals(ContentName.onHist))
      monthlyFacBalReposHist.uspL9MonthlyfacbalUpd(YYYYMM,  empNo);
   else
      monthlyFacBalRepos.uspL9MonthlyfacbalUpd(YYYYMM,  empNo);
  }

}
