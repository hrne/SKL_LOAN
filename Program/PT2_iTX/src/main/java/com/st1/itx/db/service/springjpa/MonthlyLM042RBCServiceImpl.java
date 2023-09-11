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
import com.st1.itx.db.domain.MonthlyLM042RBC;
import com.st1.itx.db.domain.MonthlyLM042RBCId;
import com.st1.itx.db.repository.online.MonthlyLM042RBCRepository;
import com.st1.itx.db.repository.day.MonthlyLM042RBCRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM042RBCRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM042RBCRepositoryHist;
import com.st1.itx.db.service.MonthlyLM042RBCService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM042RBCService")
@Repository
public class MonthlyLM042RBCServiceImpl extends ASpringJpaParm implements MonthlyLM042RBCService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private MonthlyLM042RBCRepository monthlyLM042RBCRepos;

  @Autowired
  private MonthlyLM042RBCRepositoryDay monthlyLM042RBCReposDay;

  @Autowired
  private MonthlyLM042RBCRepositoryMon monthlyLM042RBCReposMon;

  @Autowired
  private MonthlyLM042RBCRepositoryHist monthlyLM042RBCReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(monthlyLM042RBCRepos);
    org.junit.Assert.assertNotNull(monthlyLM042RBCReposDay);
    org.junit.Assert.assertNotNull(monthlyLM042RBCReposMon);
    org.junit.Assert.assertNotNull(monthlyLM042RBCReposHist);
  }

  @Override
  public MonthlyLM042RBC findById(MonthlyLM042RBCId monthlyLM042RBCId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + monthlyLM042RBCId);
    Optional<MonthlyLM042RBC> monthlyLM042RBC = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM042RBC = monthlyLM042RBCReposDay.findById(monthlyLM042RBCId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM042RBC = monthlyLM042RBCReposMon.findById(monthlyLM042RBCId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM042RBC = monthlyLM042RBCReposHist.findById(monthlyLM042RBCId);
    else 
      monthlyLM042RBC = monthlyLM042RBCRepos.findById(monthlyLM042RBCId);
    MonthlyLM042RBC obj = monthlyLM042RBC.isPresent() ? monthlyLM042RBC.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<MonthlyLM042RBC> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM042RBC> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "LoanType", "LoanItem", "RelatedCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "LoanType", "LoanItem", "RelatedCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM042RBCReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM042RBCReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM042RBCReposHist.findAll(pageable);
    else 
      slice = monthlyLM042RBCRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<MonthlyLM042RBC> findYearMonthAll(int yearMonth_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM042RBC> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findYearMonthAll " + dbName + " : " + "yearMonth_0 : " + yearMonth_0);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM042RBCReposDay.findAllByYearMonthIs(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM042RBCReposMon.findAllByYearMonthIs(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM042RBCReposHist.findAllByYearMonthIs(yearMonth_0, pageable);
    else 
      slice = monthlyLM042RBCRepos.findAllByYearMonthIs(yearMonth_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<MonthlyLM042RBC> findItem(int yearMonth_0, String loanType_1, String loanItem_2, String relatedCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM042RBC> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findItem " + dbName + " : " + "yearMonth_0 : " + yearMonth_0 + " loanType_1 : " +  loanType_1 + " loanItem_2 : " +  loanItem_2 + " relatedCode_3 : " +  relatedCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM042RBCReposDay.findAllByYearMonthIsAndLoanTypeIsAndLoanItemIsAndRelatedCodeIs(yearMonth_0, loanType_1, loanItem_2, relatedCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM042RBCReposMon.findAllByYearMonthIsAndLoanTypeIsAndLoanItemIsAndRelatedCodeIs(yearMonth_0, loanType_1, loanItem_2, relatedCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM042RBCReposHist.findAllByYearMonthIsAndLoanTypeIsAndLoanItemIsAndRelatedCodeIs(yearMonth_0, loanType_1, loanItem_2, relatedCode_3, pageable);
    else 
      slice = monthlyLM042RBCRepos.findAllByYearMonthIsAndLoanTypeIsAndLoanItemIsAndRelatedCodeIs(yearMonth_0, loanType_1, loanItem_2, relatedCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public MonthlyLM042RBC holdById(MonthlyLM042RBCId monthlyLM042RBCId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM042RBCId);
    Optional<MonthlyLM042RBC> monthlyLM042RBC = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM042RBC = monthlyLM042RBCReposDay.findByMonthlyLM042RBCId(monthlyLM042RBCId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM042RBC = monthlyLM042RBCReposMon.findByMonthlyLM042RBCId(monthlyLM042RBCId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM042RBC = monthlyLM042RBCReposHist.findByMonthlyLM042RBCId(monthlyLM042RBCId);
    else 
      monthlyLM042RBC = monthlyLM042RBCRepos.findByMonthlyLM042RBCId(monthlyLM042RBCId);
    return monthlyLM042RBC.isPresent() ? monthlyLM042RBC.get() : null;
  }

  @Override
  public MonthlyLM042RBC holdById(MonthlyLM042RBC monthlyLM042RBC, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM042RBC.getMonthlyLM042RBCId());
    Optional<MonthlyLM042RBC> monthlyLM042RBCT = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM042RBCT = monthlyLM042RBCReposDay.findByMonthlyLM042RBCId(monthlyLM042RBC.getMonthlyLM042RBCId());
    else if (dbName.equals(ContentName.onMon))
      monthlyLM042RBCT = monthlyLM042RBCReposMon.findByMonthlyLM042RBCId(monthlyLM042RBC.getMonthlyLM042RBCId());
    else if (dbName.equals(ContentName.onHist))
      monthlyLM042RBCT = monthlyLM042RBCReposHist.findByMonthlyLM042RBCId(monthlyLM042RBC.getMonthlyLM042RBCId());
    else 
      monthlyLM042RBCT = monthlyLM042RBCRepos.findByMonthlyLM042RBCId(monthlyLM042RBC.getMonthlyLM042RBCId());
    return monthlyLM042RBCT.isPresent() ? monthlyLM042RBCT.get() : null;
  }

  @Override
  public MonthlyLM042RBC insert(MonthlyLM042RBC monthlyLM042RBC, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + monthlyLM042RBC.getMonthlyLM042RBCId());
    if (this.findById(monthlyLM042RBC.getMonthlyLM042RBCId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      monthlyLM042RBC.setCreateEmpNo(empNot);

    if(monthlyLM042RBC.getLastUpdateEmpNo() == null || monthlyLM042RBC.getLastUpdateEmpNo().isEmpty())
      monthlyLM042RBC.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM042RBCReposDay.saveAndFlush(monthlyLM042RBC);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM042RBCReposMon.saveAndFlush(monthlyLM042RBC);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM042RBCReposHist.saveAndFlush(monthlyLM042RBC);
    else 
    return monthlyLM042RBCRepos.saveAndFlush(monthlyLM042RBC);
  }

  @Override
  public MonthlyLM042RBC update(MonthlyLM042RBC monthlyLM042RBC, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM042RBC.getMonthlyLM042RBCId());
    if (!empNot.isEmpty())
      monthlyLM042RBC.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM042RBCReposDay.saveAndFlush(monthlyLM042RBC);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM042RBCReposMon.saveAndFlush(monthlyLM042RBC);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM042RBCReposHist.saveAndFlush(monthlyLM042RBC);
    else 
    return monthlyLM042RBCRepos.saveAndFlush(monthlyLM042RBC);
  }

  @Override
  public MonthlyLM042RBC update2(MonthlyLM042RBC monthlyLM042RBC, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM042RBC.getMonthlyLM042RBCId());
    if (!empNot.isEmpty())
      monthlyLM042RBC.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      monthlyLM042RBCReposDay.saveAndFlush(monthlyLM042RBC);	
    else if (dbName.equals(ContentName.onMon))
      monthlyLM042RBCReposMon.saveAndFlush(monthlyLM042RBC);
    else if (dbName.equals(ContentName.onHist))
        monthlyLM042RBCReposHist.saveAndFlush(monthlyLM042RBC);
    else 
      monthlyLM042RBCRepos.saveAndFlush(monthlyLM042RBC);	
    return this.findById(monthlyLM042RBC.getMonthlyLM042RBCId());
  }

  @Override
  public void delete(MonthlyLM042RBC monthlyLM042RBC, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + monthlyLM042RBC.getMonthlyLM042RBCId());
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM042RBCReposDay.delete(monthlyLM042RBC);	
      monthlyLM042RBCReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM042RBCReposMon.delete(monthlyLM042RBC);	
      monthlyLM042RBCReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM042RBCReposHist.delete(monthlyLM042RBC);
      monthlyLM042RBCReposHist.flush();
    }
    else {
      monthlyLM042RBCRepos.delete(monthlyLM042RBC);
      monthlyLM042RBCRepos.flush();
    }
   }

  @Override
  public void insertAll(List<MonthlyLM042RBC> monthlyLM042RBC, TitaVo... titaVo) throws DBException {
    if (monthlyLM042RBC == null || monthlyLM042RBC.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (MonthlyLM042RBC t : monthlyLM042RBC){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM042RBC = monthlyLM042RBCReposDay.saveAll(monthlyLM042RBC);	
      monthlyLM042RBCReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM042RBC = monthlyLM042RBCReposMon.saveAll(monthlyLM042RBC);	
      monthlyLM042RBCReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM042RBC = monthlyLM042RBCReposHist.saveAll(monthlyLM042RBC);
      monthlyLM042RBCReposHist.flush();
    }
    else {
      monthlyLM042RBC = monthlyLM042RBCRepos.saveAll(monthlyLM042RBC);
      monthlyLM042RBCRepos.flush();
    }
    }

  @Override
  public void updateAll(List<MonthlyLM042RBC> monthlyLM042RBC, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (monthlyLM042RBC == null || monthlyLM042RBC.size() == 0)
      throw new DBException(6);

    for (MonthlyLM042RBC t : monthlyLM042RBC) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM042RBC = monthlyLM042RBCReposDay.saveAll(monthlyLM042RBC);	
      monthlyLM042RBCReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM042RBC = monthlyLM042RBCReposMon.saveAll(monthlyLM042RBC);	
      monthlyLM042RBCReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM042RBC = monthlyLM042RBCReposHist.saveAll(monthlyLM042RBC);
      monthlyLM042RBCReposHist.flush();
    }
    else {
      monthlyLM042RBC = monthlyLM042RBCRepos.saveAll(monthlyLM042RBC);
      monthlyLM042RBCRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<MonthlyLM042RBC> monthlyLM042RBC, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (monthlyLM042RBC == null || monthlyLM042RBC.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM042RBCReposDay.deleteAll(monthlyLM042RBC);	
      monthlyLM042RBCReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM042RBCReposMon.deleteAll(monthlyLM042RBC);	
      monthlyLM042RBCReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM042RBCReposHist.deleteAll(monthlyLM042RBC);
      monthlyLM042RBCReposHist.flush();
    }
    else {
      monthlyLM042RBCRepos.deleteAll(monthlyLM042RBC);
      monthlyLM042RBCRepos.flush();
    }
  }

}
