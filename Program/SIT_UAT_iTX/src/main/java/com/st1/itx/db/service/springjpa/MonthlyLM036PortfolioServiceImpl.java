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
import com.st1.itx.db.domain.MonthlyLM036Portfolio;
import com.st1.itx.db.repository.online.MonthlyLM036PortfolioRepository;
import com.st1.itx.db.repository.day.MonthlyLM036PortfolioRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM036PortfolioRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM036PortfolioRepositoryHist;
import com.st1.itx.db.service.MonthlyLM036PortfolioService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM036PortfolioService")
@Repository
public class MonthlyLM036PortfolioServiceImpl extends ASpringJpaParm implements MonthlyLM036PortfolioService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private MonthlyLM036PortfolioRepository monthlyLM036PortfolioRepos;

  @Autowired
  private MonthlyLM036PortfolioRepositoryDay monthlyLM036PortfolioReposDay;

  @Autowired
  private MonthlyLM036PortfolioRepositoryMon monthlyLM036PortfolioReposMon;

  @Autowired
  private MonthlyLM036PortfolioRepositoryHist monthlyLM036PortfolioReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(monthlyLM036PortfolioRepos);
    org.junit.Assert.assertNotNull(monthlyLM036PortfolioReposDay);
    org.junit.Assert.assertNotNull(monthlyLM036PortfolioReposMon);
    org.junit.Assert.assertNotNull(monthlyLM036PortfolioReposHist);
  }

  @Override
  public MonthlyLM036Portfolio findById(int dataMonth, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + dataMonth);
    Optional<MonthlyLM036Portfolio> monthlyLM036Portfolio = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM036Portfolio = monthlyLM036PortfolioReposDay.findById(dataMonth);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM036Portfolio = monthlyLM036PortfolioReposMon.findById(dataMonth);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM036Portfolio = monthlyLM036PortfolioReposHist.findById(dataMonth);
    else 
      monthlyLM036Portfolio = monthlyLM036PortfolioRepos.findById(dataMonth);
    MonthlyLM036Portfolio obj = monthlyLM036Portfolio.isPresent() ? monthlyLM036Portfolio.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<MonthlyLM036Portfolio> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM036Portfolio> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataMonth"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataMonth"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM036PortfolioReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM036PortfolioReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM036PortfolioReposHist.findAll(pageable);
    else 
      slice = monthlyLM036PortfolioRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<MonthlyLM036Portfolio> findDataMonthBetween(int dataMonth_0, int dataMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM036Portfolio> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findDataMonthBetween " + dbName + " : " + "dataMonth_0 : " + dataMonth_0 + " dataMonth_1 : " +  dataMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM036PortfolioReposDay.findAllByDataMonthGreaterThanEqualAndDataMonthLessThanEqualOrderByDataMonthAsc(dataMonth_0, dataMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM036PortfolioReposMon.findAllByDataMonthGreaterThanEqualAndDataMonthLessThanEqualOrderByDataMonthAsc(dataMonth_0, dataMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM036PortfolioReposHist.findAllByDataMonthGreaterThanEqualAndDataMonthLessThanEqualOrderByDataMonthAsc(dataMonth_0, dataMonth_1, pageable);
    else 
      slice = monthlyLM036PortfolioRepos.findAllByDataMonthGreaterThanEqualAndDataMonthLessThanEqualOrderByDataMonthAsc(dataMonth_0, dataMonth_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public MonthlyLM036Portfolio holdById(int dataMonth, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + dataMonth);
    Optional<MonthlyLM036Portfolio> monthlyLM036Portfolio = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM036Portfolio = monthlyLM036PortfolioReposDay.findByDataMonth(dataMonth);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM036Portfolio = monthlyLM036PortfolioReposMon.findByDataMonth(dataMonth);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM036Portfolio = monthlyLM036PortfolioReposHist.findByDataMonth(dataMonth);
    else 
      monthlyLM036Portfolio = monthlyLM036PortfolioRepos.findByDataMonth(dataMonth);
    return monthlyLM036Portfolio.isPresent() ? monthlyLM036Portfolio.get() : null;
  }

  @Override
  public MonthlyLM036Portfolio holdById(MonthlyLM036Portfolio monthlyLM036Portfolio, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM036Portfolio.getDataMonth());
    Optional<MonthlyLM036Portfolio> monthlyLM036PortfolioT = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM036PortfolioT = monthlyLM036PortfolioReposDay.findByDataMonth(monthlyLM036Portfolio.getDataMonth());
    else if (dbName.equals(ContentName.onMon))
      monthlyLM036PortfolioT = monthlyLM036PortfolioReposMon.findByDataMonth(monthlyLM036Portfolio.getDataMonth());
    else if (dbName.equals(ContentName.onHist))
      monthlyLM036PortfolioT = monthlyLM036PortfolioReposHist.findByDataMonth(monthlyLM036Portfolio.getDataMonth());
    else 
      monthlyLM036PortfolioT = monthlyLM036PortfolioRepos.findByDataMonth(monthlyLM036Portfolio.getDataMonth());
    return monthlyLM036PortfolioT.isPresent() ? monthlyLM036PortfolioT.get() : null;
  }

  @Override
  public MonthlyLM036Portfolio insert(MonthlyLM036Portfolio monthlyLM036Portfolio, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + monthlyLM036Portfolio.getDataMonth());
    if (this.findById(monthlyLM036Portfolio.getDataMonth()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      monthlyLM036Portfolio.setCreateEmpNo(empNot);

    if(monthlyLM036Portfolio.getLastUpdateEmpNo() == null || monthlyLM036Portfolio.getLastUpdateEmpNo().isEmpty())
      monthlyLM036Portfolio.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM036PortfolioReposDay.saveAndFlush(monthlyLM036Portfolio);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM036PortfolioReposMon.saveAndFlush(monthlyLM036Portfolio);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM036PortfolioReposHist.saveAndFlush(monthlyLM036Portfolio);
    else 
    return monthlyLM036PortfolioRepos.saveAndFlush(monthlyLM036Portfolio);
  }

  @Override
  public MonthlyLM036Portfolio update(MonthlyLM036Portfolio monthlyLM036Portfolio, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + monthlyLM036Portfolio.getDataMonth());
    if (!empNot.isEmpty())
      monthlyLM036Portfolio.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM036PortfolioReposDay.saveAndFlush(monthlyLM036Portfolio);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM036PortfolioReposMon.saveAndFlush(monthlyLM036Portfolio);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM036PortfolioReposHist.saveAndFlush(monthlyLM036Portfolio);
    else 
    return monthlyLM036PortfolioRepos.saveAndFlush(monthlyLM036Portfolio);
  }

  @Override
  public MonthlyLM036Portfolio update2(MonthlyLM036Portfolio monthlyLM036Portfolio, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + monthlyLM036Portfolio.getDataMonth());
    if (!empNot.isEmpty())
      monthlyLM036Portfolio.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      monthlyLM036PortfolioReposDay.saveAndFlush(monthlyLM036Portfolio);	
    else if (dbName.equals(ContentName.onMon))
      monthlyLM036PortfolioReposMon.saveAndFlush(monthlyLM036Portfolio);
    else if (dbName.equals(ContentName.onHist))
        monthlyLM036PortfolioReposHist.saveAndFlush(monthlyLM036Portfolio);
    else 
      monthlyLM036PortfolioRepos.saveAndFlush(monthlyLM036Portfolio);	
    return this.findById(monthlyLM036Portfolio.getDataMonth());
  }

  @Override
  public void delete(MonthlyLM036Portfolio monthlyLM036Portfolio, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + monthlyLM036Portfolio.getDataMonth());
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM036PortfolioReposDay.delete(monthlyLM036Portfolio);	
      monthlyLM036PortfolioReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM036PortfolioReposMon.delete(monthlyLM036Portfolio);	
      monthlyLM036PortfolioReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM036PortfolioReposHist.delete(monthlyLM036Portfolio);
      monthlyLM036PortfolioReposHist.flush();
    }
    else {
      monthlyLM036PortfolioRepos.delete(monthlyLM036Portfolio);
      monthlyLM036PortfolioRepos.flush();
    }
   }

  @Override
  public void insertAll(List<MonthlyLM036Portfolio> monthlyLM036Portfolio, TitaVo... titaVo) throws DBException {
    if (monthlyLM036Portfolio == null || monthlyLM036Portfolio.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (MonthlyLM036Portfolio t : monthlyLM036Portfolio){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM036Portfolio = monthlyLM036PortfolioReposDay.saveAll(monthlyLM036Portfolio);	
      monthlyLM036PortfolioReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM036Portfolio = monthlyLM036PortfolioReposMon.saveAll(monthlyLM036Portfolio);	
      monthlyLM036PortfolioReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM036Portfolio = monthlyLM036PortfolioReposHist.saveAll(monthlyLM036Portfolio);
      monthlyLM036PortfolioReposHist.flush();
    }
    else {
      monthlyLM036Portfolio = monthlyLM036PortfolioRepos.saveAll(monthlyLM036Portfolio);
      monthlyLM036PortfolioRepos.flush();
    }
    }

  @Override
  public void updateAll(List<MonthlyLM036Portfolio> monthlyLM036Portfolio, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (monthlyLM036Portfolio == null || monthlyLM036Portfolio.size() == 0)
      throw new DBException(6);

    for (MonthlyLM036Portfolio t : monthlyLM036Portfolio) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM036Portfolio = monthlyLM036PortfolioReposDay.saveAll(monthlyLM036Portfolio);	
      monthlyLM036PortfolioReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM036Portfolio = monthlyLM036PortfolioReposMon.saveAll(monthlyLM036Portfolio);	
      monthlyLM036PortfolioReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM036Portfolio = monthlyLM036PortfolioReposHist.saveAll(monthlyLM036Portfolio);
      monthlyLM036PortfolioReposHist.flush();
    }
    else {
      monthlyLM036Portfolio = monthlyLM036PortfolioRepos.saveAll(monthlyLM036Portfolio);
      monthlyLM036PortfolioRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<MonthlyLM036Portfolio> monthlyLM036Portfolio, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (monthlyLM036Portfolio == null || monthlyLM036Portfolio.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM036PortfolioReposDay.deleteAll(monthlyLM036Portfolio);	
      monthlyLM036PortfolioReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM036PortfolioReposMon.deleteAll(monthlyLM036Portfolio);	
      monthlyLM036PortfolioReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM036PortfolioReposHist.deleteAll(monthlyLM036Portfolio);
      monthlyLM036PortfolioReposHist.flush();
    }
    else {
      monthlyLM036PortfolioRepos.deleteAll(monthlyLM036Portfolio);
      monthlyLM036PortfolioRepos.flush();
    }
  }

  @Override
  public void Usp_L9_MonthlyLM036Portfolio_Ins(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      monthlyLM036PortfolioReposDay.uspL9Monthlylm036portfolioIns(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM036PortfolioReposMon.uspL9Monthlylm036portfolioIns(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM036PortfolioReposHist.uspL9Monthlylm036portfolioIns(TBSDYF, EmpNo);
   else
      monthlyLM036PortfolioRepos.uspL9Monthlylm036portfolioIns(TBSDYF, EmpNo);
  }

}
