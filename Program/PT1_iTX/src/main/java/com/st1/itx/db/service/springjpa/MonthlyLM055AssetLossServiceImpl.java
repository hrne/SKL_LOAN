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
import com.st1.itx.db.domain.MonthlyLM055AssetLoss;
import com.st1.itx.db.domain.MonthlyLM055AssetLossId;
import com.st1.itx.db.repository.online.MonthlyLM055AssetLossRepository;
import com.st1.itx.db.repository.day.MonthlyLM055AssetLossRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM055AssetLossRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM055AssetLossRepositoryHist;
import com.st1.itx.db.service.MonthlyLM055AssetLossService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM055AssetLossService")
@Repository
public class MonthlyLM055AssetLossServiceImpl extends ASpringJpaParm implements MonthlyLM055AssetLossService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private MonthlyLM055AssetLossRepository monthlyLM055AssetLossRepos;

  @Autowired
  private MonthlyLM055AssetLossRepositoryDay monthlyLM055AssetLossReposDay;

  @Autowired
  private MonthlyLM055AssetLossRepositoryMon monthlyLM055AssetLossReposMon;

  @Autowired
  private MonthlyLM055AssetLossRepositoryHist monthlyLM055AssetLossReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(monthlyLM055AssetLossRepos);
    org.junit.Assert.assertNotNull(monthlyLM055AssetLossReposDay);
    org.junit.Assert.assertNotNull(monthlyLM055AssetLossReposMon);
    org.junit.Assert.assertNotNull(monthlyLM055AssetLossReposHist);
  }

  @Override
  public MonthlyLM055AssetLoss findById(MonthlyLM055AssetLossId monthlyLM055AssetLossId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + monthlyLM055AssetLossId);
    Optional<MonthlyLM055AssetLoss> monthlyLM055AssetLoss = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposDay.findById(monthlyLM055AssetLossId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposMon.findById(monthlyLM055AssetLossId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposHist.findById(monthlyLM055AssetLossId);
    else 
      monthlyLM055AssetLoss = monthlyLM055AssetLossRepos.findById(monthlyLM055AssetLossId);
    MonthlyLM055AssetLoss obj = monthlyLM055AssetLoss.isPresent() ? monthlyLM055AssetLoss.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<MonthlyLM055AssetLoss> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM055AssetLoss> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "LoanType", "LoanItem"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "LoanType", "LoanItem"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM055AssetLossReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM055AssetLossReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM055AssetLossReposHist.findAll(pageable);
    else 
      slice = monthlyLM055AssetLossRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<MonthlyLM055AssetLoss> findYearMonthAll(int yearMonth_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM055AssetLoss> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findYearMonthAll " + dbName + " : " + "yearMonth_0 : " + yearMonth_0);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM055AssetLossReposDay.findAllByYearMonthIs(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM055AssetLossReposMon.findAllByYearMonthIs(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM055AssetLossReposHist.findAllByYearMonthIs(yearMonth_0, pageable);
    else 
      slice = monthlyLM055AssetLossRepos.findAllByYearMonthIs(yearMonth_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public MonthlyLM055AssetLoss holdById(MonthlyLM055AssetLossId monthlyLM055AssetLossId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM055AssetLossId);
    Optional<MonthlyLM055AssetLoss> monthlyLM055AssetLoss = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposDay.findByMonthlyLM055AssetLossId(monthlyLM055AssetLossId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposMon.findByMonthlyLM055AssetLossId(monthlyLM055AssetLossId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposHist.findByMonthlyLM055AssetLossId(monthlyLM055AssetLossId);
    else 
      monthlyLM055AssetLoss = monthlyLM055AssetLossRepos.findByMonthlyLM055AssetLossId(monthlyLM055AssetLossId);
    return monthlyLM055AssetLoss.isPresent() ? monthlyLM055AssetLoss.get() : null;
  }

  @Override
  public MonthlyLM055AssetLoss holdById(MonthlyLM055AssetLoss monthlyLM055AssetLoss, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM055AssetLoss.getMonthlyLM055AssetLossId());
    Optional<MonthlyLM055AssetLoss> monthlyLM055AssetLossT = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM055AssetLossT = monthlyLM055AssetLossReposDay.findByMonthlyLM055AssetLossId(monthlyLM055AssetLoss.getMonthlyLM055AssetLossId());
    else if (dbName.equals(ContentName.onMon))
      monthlyLM055AssetLossT = monthlyLM055AssetLossReposMon.findByMonthlyLM055AssetLossId(monthlyLM055AssetLoss.getMonthlyLM055AssetLossId());
    else if (dbName.equals(ContentName.onHist))
      monthlyLM055AssetLossT = monthlyLM055AssetLossReposHist.findByMonthlyLM055AssetLossId(monthlyLM055AssetLoss.getMonthlyLM055AssetLossId());
    else 
      monthlyLM055AssetLossT = monthlyLM055AssetLossRepos.findByMonthlyLM055AssetLossId(monthlyLM055AssetLoss.getMonthlyLM055AssetLossId());
    return monthlyLM055AssetLossT.isPresent() ? monthlyLM055AssetLossT.get() : null;
  }

  @Override
  public MonthlyLM055AssetLoss insert(MonthlyLM055AssetLoss monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + monthlyLM055AssetLoss.getMonthlyLM055AssetLossId());
    if (this.findById(monthlyLM055AssetLoss.getMonthlyLM055AssetLossId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      monthlyLM055AssetLoss.setCreateEmpNo(empNot);

    if(monthlyLM055AssetLoss.getLastUpdateEmpNo() == null || monthlyLM055AssetLoss.getLastUpdateEmpNo().isEmpty())
      monthlyLM055AssetLoss.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM055AssetLossReposDay.saveAndFlush(monthlyLM055AssetLoss);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM055AssetLossReposMon.saveAndFlush(monthlyLM055AssetLoss);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM055AssetLossReposHist.saveAndFlush(monthlyLM055AssetLoss);
    else 
    return monthlyLM055AssetLossRepos.saveAndFlush(monthlyLM055AssetLoss);
  }

  @Override
  public MonthlyLM055AssetLoss update(MonthlyLM055AssetLoss monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM055AssetLoss.getMonthlyLM055AssetLossId());
    if (!empNot.isEmpty())
      monthlyLM055AssetLoss.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM055AssetLossReposDay.saveAndFlush(monthlyLM055AssetLoss);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM055AssetLossReposMon.saveAndFlush(monthlyLM055AssetLoss);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM055AssetLossReposHist.saveAndFlush(monthlyLM055AssetLoss);
    else 
    return monthlyLM055AssetLossRepos.saveAndFlush(monthlyLM055AssetLoss);
  }

  @Override
  public MonthlyLM055AssetLoss update2(MonthlyLM055AssetLoss monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM055AssetLoss.getMonthlyLM055AssetLossId());
    if (!empNot.isEmpty())
      monthlyLM055AssetLoss.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      monthlyLM055AssetLossReposDay.saveAndFlush(monthlyLM055AssetLoss);	
    else if (dbName.equals(ContentName.onMon))
      monthlyLM055AssetLossReposMon.saveAndFlush(monthlyLM055AssetLoss);
    else if (dbName.equals(ContentName.onHist))
        monthlyLM055AssetLossReposHist.saveAndFlush(monthlyLM055AssetLoss);
    else 
      monthlyLM055AssetLossRepos.saveAndFlush(monthlyLM055AssetLoss);	
    return this.findById(monthlyLM055AssetLoss.getMonthlyLM055AssetLossId());
  }

  @Override
  public void delete(MonthlyLM055AssetLoss monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + monthlyLM055AssetLoss.getMonthlyLM055AssetLossId());
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM055AssetLossReposDay.delete(monthlyLM055AssetLoss);	
      monthlyLM055AssetLossReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM055AssetLossReposMon.delete(monthlyLM055AssetLoss);	
      monthlyLM055AssetLossReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM055AssetLossReposHist.delete(monthlyLM055AssetLoss);
      monthlyLM055AssetLossReposHist.flush();
    }
    else {
      monthlyLM055AssetLossRepos.delete(monthlyLM055AssetLoss);
      monthlyLM055AssetLossRepos.flush();
    }
   }

  @Override
  public void insertAll(List<MonthlyLM055AssetLoss> monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException {
    if (monthlyLM055AssetLoss == null || monthlyLM055AssetLoss.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (MonthlyLM055AssetLoss t : monthlyLM055AssetLoss){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposDay.saveAll(monthlyLM055AssetLoss);	
      monthlyLM055AssetLossReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposMon.saveAll(monthlyLM055AssetLoss);	
      monthlyLM055AssetLossReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposHist.saveAll(monthlyLM055AssetLoss);
      monthlyLM055AssetLossReposHist.flush();
    }
    else {
      monthlyLM055AssetLoss = monthlyLM055AssetLossRepos.saveAll(monthlyLM055AssetLoss);
      monthlyLM055AssetLossRepos.flush();
    }
    }

  @Override
  public void updateAll(List<MonthlyLM055AssetLoss> monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (monthlyLM055AssetLoss == null || monthlyLM055AssetLoss.size() == 0)
      throw new DBException(6);

    for (MonthlyLM055AssetLoss t : monthlyLM055AssetLoss) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposDay.saveAll(monthlyLM055AssetLoss);	
      monthlyLM055AssetLossReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposMon.saveAll(monthlyLM055AssetLoss);	
      monthlyLM055AssetLossReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM055AssetLoss = monthlyLM055AssetLossReposHist.saveAll(monthlyLM055AssetLoss);
      monthlyLM055AssetLossReposHist.flush();
    }
    else {
      monthlyLM055AssetLoss = monthlyLM055AssetLossRepos.saveAll(monthlyLM055AssetLoss);
      monthlyLM055AssetLossRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<MonthlyLM055AssetLoss> monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (monthlyLM055AssetLoss == null || monthlyLM055AssetLoss.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM055AssetLossReposDay.deleteAll(monthlyLM055AssetLoss);	
      monthlyLM055AssetLossReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM055AssetLossReposMon.deleteAll(monthlyLM055AssetLoss);	
      monthlyLM055AssetLossReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM055AssetLossReposHist.deleteAll(monthlyLM055AssetLoss);
      monthlyLM055AssetLossReposHist.flush();
    }
    else {
      monthlyLM055AssetLossRepos.deleteAll(monthlyLM055AssetLoss);
      monthlyLM055AssetLossRepos.flush();
    }
  }

}
