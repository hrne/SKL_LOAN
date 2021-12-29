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
import com.st1.itx.db.domain.MonthlyLM052Loss;
import com.st1.itx.db.repository.online.MonthlyLM052LossRepository;
import com.st1.itx.db.repository.day.MonthlyLM052LossRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM052LossRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM052LossRepositoryHist;
import com.st1.itx.db.service.MonthlyLM052LossService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM052LossService")
@Repository
public class MonthlyLM052LossServiceImpl extends ASpringJpaParm implements MonthlyLM052LossService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private MonthlyLM052LossRepository monthlyLM052LossRepos;

  @Autowired
  private MonthlyLM052LossRepositoryDay monthlyLM052LossReposDay;

  @Autowired
  private MonthlyLM052LossRepositoryMon monthlyLM052LossReposMon;

  @Autowired
  private MonthlyLM052LossRepositoryHist monthlyLM052LossReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(monthlyLM052LossRepos);
    org.junit.Assert.assertNotNull(monthlyLM052LossReposDay);
    org.junit.Assert.assertNotNull(monthlyLM052LossReposMon);
    org.junit.Assert.assertNotNull(monthlyLM052LossReposHist);
  }

  @Override
  public MonthlyLM052Loss findById(int yearMonth, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + yearMonth);
    Optional<MonthlyLM052Loss> monthlyLM052Loss = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052Loss = monthlyLM052LossReposDay.findById(yearMonth);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052Loss = monthlyLM052LossReposMon.findById(yearMonth);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052Loss = monthlyLM052LossReposHist.findById(yearMonth);
    else 
      monthlyLM052Loss = monthlyLM052LossRepos.findById(yearMonth);
    MonthlyLM052Loss obj = monthlyLM052Loss.isPresent() ? monthlyLM052Loss.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<MonthlyLM052Loss> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM052Loss> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM052LossReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM052LossReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM052LossReposHist.findAll(pageable);
    else 
      slice = monthlyLM052LossRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<MonthlyLM052Loss> findYearMonth(int yearMonth_0, int yearMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM052Loss> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findYearMonth " + dbName + " : " + "yearMonth_0 : " + yearMonth_0 + " yearMonth_1 : " +  yearMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM052LossReposDay.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM052LossReposMon.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM052LossReposHist.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);
    else 
      slice = monthlyLM052LossRepos.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public MonthlyLM052Loss holdById(int yearMonth, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + yearMonth);
    Optional<MonthlyLM052Loss> monthlyLM052Loss = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052Loss = monthlyLM052LossReposDay.findByYearMonth(yearMonth);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052Loss = monthlyLM052LossReposMon.findByYearMonth(yearMonth);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052Loss = monthlyLM052LossReposHist.findByYearMonth(yearMonth);
    else 
      monthlyLM052Loss = monthlyLM052LossRepos.findByYearMonth(yearMonth);
    return monthlyLM052Loss.isPresent() ? monthlyLM052Loss.get() : null;
  }

  @Override
  public MonthlyLM052Loss holdById(MonthlyLM052Loss monthlyLM052Loss, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM052Loss.getYearMonth());
    Optional<MonthlyLM052Loss> monthlyLM052LossT = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052LossT = monthlyLM052LossReposDay.findByYearMonth(monthlyLM052Loss.getYearMonth());
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052LossT = monthlyLM052LossReposMon.findByYearMonth(monthlyLM052Loss.getYearMonth());
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052LossT = monthlyLM052LossReposHist.findByYearMonth(monthlyLM052Loss.getYearMonth());
    else 
      monthlyLM052LossT = monthlyLM052LossRepos.findByYearMonth(monthlyLM052Loss.getYearMonth());
    return monthlyLM052LossT.isPresent() ? monthlyLM052LossT.get() : null;
  }

  @Override
  public MonthlyLM052Loss insert(MonthlyLM052Loss monthlyLM052Loss, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + monthlyLM052Loss.getYearMonth());
    if (this.findById(monthlyLM052Loss.getYearMonth(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      monthlyLM052Loss.setCreateEmpNo(empNot);

    if(monthlyLM052Loss.getLastUpdateEmpNo() == null || monthlyLM052Loss.getLastUpdateEmpNo().isEmpty())
      monthlyLM052Loss.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM052LossReposDay.saveAndFlush(monthlyLM052Loss);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM052LossReposMon.saveAndFlush(monthlyLM052Loss);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM052LossReposHist.saveAndFlush(monthlyLM052Loss);
    else 
    return monthlyLM052LossRepos.saveAndFlush(monthlyLM052Loss);
  }

  @Override
  public MonthlyLM052Loss update(MonthlyLM052Loss monthlyLM052Loss, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM052Loss.getYearMonth());
    if (!empNot.isEmpty())
      monthlyLM052Loss.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM052LossReposDay.saveAndFlush(monthlyLM052Loss);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM052LossReposMon.saveAndFlush(monthlyLM052Loss);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM052LossReposHist.saveAndFlush(monthlyLM052Loss);
    else 
    return monthlyLM052LossRepos.saveAndFlush(monthlyLM052Loss);
  }

  @Override
  public MonthlyLM052Loss update2(MonthlyLM052Loss monthlyLM052Loss, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM052Loss.getYearMonth());
    if (!empNot.isEmpty())
      monthlyLM052Loss.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      monthlyLM052LossReposDay.saveAndFlush(monthlyLM052Loss);	
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052LossReposMon.saveAndFlush(monthlyLM052Loss);
    else if (dbName.equals(ContentName.onHist))
        monthlyLM052LossReposHist.saveAndFlush(monthlyLM052Loss);
    else 
      monthlyLM052LossRepos.saveAndFlush(monthlyLM052Loss);	
    return this.findById(monthlyLM052Loss.getYearMonth());
  }

  @Override
  public void delete(MonthlyLM052Loss monthlyLM052Loss, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + monthlyLM052Loss.getYearMonth());
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052LossReposDay.delete(monthlyLM052Loss);	
      monthlyLM052LossReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052LossReposMon.delete(monthlyLM052Loss);	
      monthlyLM052LossReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052LossReposHist.delete(monthlyLM052Loss);
      monthlyLM052LossReposHist.flush();
    }
    else {
      monthlyLM052LossRepos.delete(monthlyLM052Loss);
      monthlyLM052LossRepos.flush();
    }
   }

  @Override
  public void insertAll(List<MonthlyLM052Loss> monthlyLM052Loss, TitaVo... titaVo) throws DBException {
    if (monthlyLM052Loss == null || monthlyLM052Loss.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (MonthlyLM052Loss t : monthlyLM052Loss){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052Loss = monthlyLM052LossReposDay.saveAll(monthlyLM052Loss);	
      monthlyLM052LossReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052Loss = monthlyLM052LossReposMon.saveAll(monthlyLM052Loss);	
      monthlyLM052LossReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052Loss = monthlyLM052LossReposHist.saveAll(monthlyLM052Loss);
      monthlyLM052LossReposHist.flush();
    }
    else {
      monthlyLM052Loss = monthlyLM052LossRepos.saveAll(monthlyLM052Loss);
      monthlyLM052LossRepos.flush();
    }
    }

  @Override
  public void updateAll(List<MonthlyLM052Loss> monthlyLM052Loss, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (monthlyLM052Loss == null || monthlyLM052Loss.size() == 0)
      throw new DBException(6);

    for (MonthlyLM052Loss t : monthlyLM052Loss) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052Loss = monthlyLM052LossReposDay.saveAll(monthlyLM052Loss);	
      monthlyLM052LossReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052Loss = monthlyLM052LossReposMon.saveAll(monthlyLM052Loss);	
      monthlyLM052LossReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052Loss = monthlyLM052LossReposHist.saveAll(monthlyLM052Loss);
      monthlyLM052LossReposHist.flush();
    }
    else {
      monthlyLM052Loss = monthlyLM052LossRepos.saveAll(monthlyLM052Loss);
      monthlyLM052LossRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<MonthlyLM052Loss> monthlyLM052Loss, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (monthlyLM052Loss == null || monthlyLM052Loss.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052LossReposDay.deleteAll(monthlyLM052Loss);	
      monthlyLM052LossReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052LossReposMon.deleteAll(monthlyLM052Loss);	
      monthlyLM052LossReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052LossReposHist.deleteAll(monthlyLM052Loss);
      monthlyLM052LossReposHist.flush();
    }
    else {
      monthlyLM052LossRepos.deleteAll(monthlyLM052Loss);
      monthlyLM052LossRepos.flush();
    }
  }

}
