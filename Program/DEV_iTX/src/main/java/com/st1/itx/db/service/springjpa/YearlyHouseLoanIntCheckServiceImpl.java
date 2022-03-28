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
import com.st1.itx.db.domain.YearlyHouseLoanIntCheck;
import com.st1.itx.db.domain.YearlyHouseLoanIntCheckId;
import com.st1.itx.db.repository.online.YearlyHouseLoanIntCheckRepository;
import com.st1.itx.db.repository.day.YearlyHouseLoanIntCheckRepositoryDay;
import com.st1.itx.db.repository.mon.YearlyHouseLoanIntCheckRepositoryMon;
import com.st1.itx.db.repository.hist.YearlyHouseLoanIntCheckRepositoryHist;
import com.st1.itx.db.service.YearlyHouseLoanIntCheckService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("yearlyHouseLoanIntCheckService")
@Repository
public class YearlyHouseLoanIntCheckServiceImpl extends ASpringJpaParm implements YearlyHouseLoanIntCheckService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private YearlyHouseLoanIntCheckRepository yearlyHouseLoanIntCheckRepos;

  @Autowired
  private YearlyHouseLoanIntCheckRepositoryDay yearlyHouseLoanIntCheckReposDay;

  @Autowired
  private YearlyHouseLoanIntCheckRepositoryMon yearlyHouseLoanIntCheckReposMon;

  @Autowired
  private YearlyHouseLoanIntCheckRepositoryHist yearlyHouseLoanIntCheckReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(yearlyHouseLoanIntCheckRepos);
    org.junit.Assert.assertNotNull(yearlyHouseLoanIntCheckReposDay);
    org.junit.Assert.assertNotNull(yearlyHouseLoanIntCheckReposMon);
    org.junit.Assert.assertNotNull(yearlyHouseLoanIntCheckReposHist);
  }

  @Override
  public YearlyHouseLoanIntCheck findById(YearlyHouseLoanIntCheckId yearlyHouseLoanIntCheckId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + yearlyHouseLoanIntCheckId);
    Optional<YearlyHouseLoanIntCheck> yearlyHouseLoanIntCheck = null;
    if (dbName.equals(ContentName.onDay))
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposDay.findById(yearlyHouseLoanIntCheckId);
    else if (dbName.equals(ContentName.onMon))
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposMon.findById(yearlyHouseLoanIntCheckId);
    else if (dbName.equals(ContentName.onHist))
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposHist.findById(yearlyHouseLoanIntCheckId);
    else 
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckRepos.findById(yearlyHouseLoanIntCheckId);
    YearlyHouseLoanIntCheck obj = yearlyHouseLoanIntCheck.isPresent() ? yearlyHouseLoanIntCheck.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<YearlyHouseLoanIntCheck> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<YearlyHouseLoanIntCheck> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "UsageCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "UsageCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = yearlyHouseLoanIntCheckReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = yearlyHouseLoanIntCheckReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = yearlyHouseLoanIntCheckReposHist.findAll(pageable);
    else 
      slice = yearlyHouseLoanIntCheckRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<YearlyHouseLoanIntCheck> findYearMonth(int yearMonth_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<YearlyHouseLoanIntCheck> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findYearMonth " + dbName + " : " + "yearMonth_0 : " + yearMonth_0);
    if (dbName.equals(ContentName.onDay))
      slice = yearlyHouseLoanIntCheckReposDay.findAllByYearMonthIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = yearlyHouseLoanIntCheckReposMon.findAllByYearMonthIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = yearlyHouseLoanIntCheckReposHist.findAllByYearMonthIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, pageable);
    else 
      slice = yearlyHouseLoanIntCheckRepos.findAllByYearMonthIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public YearlyHouseLoanIntCheck holdById(YearlyHouseLoanIntCheckId yearlyHouseLoanIntCheckId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + yearlyHouseLoanIntCheckId);
    Optional<YearlyHouseLoanIntCheck> yearlyHouseLoanIntCheck = null;
    if (dbName.equals(ContentName.onDay))
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposDay.findByYearlyHouseLoanIntCheckId(yearlyHouseLoanIntCheckId);
    else if (dbName.equals(ContentName.onMon))
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposMon.findByYearlyHouseLoanIntCheckId(yearlyHouseLoanIntCheckId);
    else if (dbName.equals(ContentName.onHist))
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposHist.findByYearlyHouseLoanIntCheckId(yearlyHouseLoanIntCheckId);
    else 
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckRepos.findByYearlyHouseLoanIntCheckId(yearlyHouseLoanIntCheckId);
    return yearlyHouseLoanIntCheck.isPresent() ? yearlyHouseLoanIntCheck.get() : null;
  }

  @Override
  public YearlyHouseLoanIntCheck holdById(YearlyHouseLoanIntCheck yearlyHouseLoanIntCheck, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + yearlyHouseLoanIntCheck.getYearlyHouseLoanIntCheckId());
    Optional<YearlyHouseLoanIntCheck> yearlyHouseLoanIntCheckT = null;
    if (dbName.equals(ContentName.onDay))
      yearlyHouseLoanIntCheckT = yearlyHouseLoanIntCheckReposDay.findByYearlyHouseLoanIntCheckId(yearlyHouseLoanIntCheck.getYearlyHouseLoanIntCheckId());
    else if (dbName.equals(ContentName.onMon))
      yearlyHouseLoanIntCheckT = yearlyHouseLoanIntCheckReposMon.findByYearlyHouseLoanIntCheckId(yearlyHouseLoanIntCheck.getYearlyHouseLoanIntCheckId());
    else if (dbName.equals(ContentName.onHist))
      yearlyHouseLoanIntCheckT = yearlyHouseLoanIntCheckReposHist.findByYearlyHouseLoanIntCheckId(yearlyHouseLoanIntCheck.getYearlyHouseLoanIntCheckId());
    else 
      yearlyHouseLoanIntCheckT = yearlyHouseLoanIntCheckRepos.findByYearlyHouseLoanIntCheckId(yearlyHouseLoanIntCheck.getYearlyHouseLoanIntCheckId());
    return yearlyHouseLoanIntCheckT.isPresent() ? yearlyHouseLoanIntCheckT.get() : null;
  }

  @Override
  public YearlyHouseLoanIntCheck insert(YearlyHouseLoanIntCheck yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + yearlyHouseLoanIntCheck.getYearlyHouseLoanIntCheckId());
    if (this.findById(yearlyHouseLoanIntCheck.getYearlyHouseLoanIntCheckId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      yearlyHouseLoanIntCheck.setCreateEmpNo(empNot);

    if(yearlyHouseLoanIntCheck.getLastUpdateEmpNo() == null || yearlyHouseLoanIntCheck.getLastUpdateEmpNo().isEmpty())
      yearlyHouseLoanIntCheck.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return yearlyHouseLoanIntCheckReposDay.saveAndFlush(yearlyHouseLoanIntCheck);	
    else if (dbName.equals(ContentName.onMon))
      return yearlyHouseLoanIntCheckReposMon.saveAndFlush(yearlyHouseLoanIntCheck);
    else if (dbName.equals(ContentName.onHist))
      return yearlyHouseLoanIntCheckReposHist.saveAndFlush(yearlyHouseLoanIntCheck);
    else 
    return yearlyHouseLoanIntCheckRepos.saveAndFlush(yearlyHouseLoanIntCheck);
  }

  @Override
  public YearlyHouseLoanIntCheck update(YearlyHouseLoanIntCheck yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + yearlyHouseLoanIntCheck.getYearlyHouseLoanIntCheckId());
    if (!empNot.isEmpty())
      yearlyHouseLoanIntCheck.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return yearlyHouseLoanIntCheckReposDay.saveAndFlush(yearlyHouseLoanIntCheck);	
    else if (dbName.equals(ContentName.onMon))
      return yearlyHouseLoanIntCheckReposMon.saveAndFlush(yearlyHouseLoanIntCheck);
    else if (dbName.equals(ContentName.onHist))
      return yearlyHouseLoanIntCheckReposHist.saveAndFlush(yearlyHouseLoanIntCheck);
    else 
    return yearlyHouseLoanIntCheckRepos.saveAndFlush(yearlyHouseLoanIntCheck);
  }

  @Override
  public YearlyHouseLoanIntCheck update2(YearlyHouseLoanIntCheck yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + yearlyHouseLoanIntCheck.getYearlyHouseLoanIntCheckId());
    if (!empNot.isEmpty())
      yearlyHouseLoanIntCheck.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      yearlyHouseLoanIntCheckReposDay.saveAndFlush(yearlyHouseLoanIntCheck);	
    else if (dbName.equals(ContentName.onMon))
      yearlyHouseLoanIntCheckReposMon.saveAndFlush(yearlyHouseLoanIntCheck);
    else if (dbName.equals(ContentName.onHist))
        yearlyHouseLoanIntCheckReposHist.saveAndFlush(yearlyHouseLoanIntCheck);
    else 
      yearlyHouseLoanIntCheckRepos.saveAndFlush(yearlyHouseLoanIntCheck);	
    return this.findById(yearlyHouseLoanIntCheck.getYearlyHouseLoanIntCheckId());
  }

  @Override
  public void delete(YearlyHouseLoanIntCheck yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + yearlyHouseLoanIntCheck.getYearlyHouseLoanIntCheckId());
    if (dbName.equals(ContentName.onDay)) {
      yearlyHouseLoanIntCheckReposDay.delete(yearlyHouseLoanIntCheck);	
      yearlyHouseLoanIntCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      yearlyHouseLoanIntCheckReposMon.delete(yearlyHouseLoanIntCheck);	
      yearlyHouseLoanIntCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      yearlyHouseLoanIntCheckReposHist.delete(yearlyHouseLoanIntCheck);
      yearlyHouseLoanIntCheckReposHist.flush();
    }
    else {
      yearlyHouseLoanIntCheckRepos.delete(yearlyHouseLoanIntCheck);
      yearlyHouseLoanIntCheckRepos.flush();
    }
   }

  @Override
  public void insertAll(List<YearlyHouseLoanIntCheck> yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException {
    if (yearlyHouseLoanIntCheck == null || yearlyHouseLoanIntCheck.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (YearlyHouseLoanIntCheck t : yearlyHouseLoanIntCheck){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposDay.saveAll(yearlyHouseLoanIntCheck);	
      yearlyHouseLoanIntCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposMon.saveAll(yearlyHouseLoanIntCheck);	
      yearlyHouseLoanIntCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposHist.saveAll(yearlyHouseLoanIntCheck);
      yearlyHouseLoanIntCheckReposHist.flush();
    }
    else {
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckRepos.saveAll(yearlyHouseLoanIntCheck);
      yearlyHouseLoanIntCheckRepos.flush();
    }
    }

  @Override
  public void updateAll(List<YearlyHouseLoanIntCheck> yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (yearlyHouseLoanIntCheck == null || yearlyHouseLoanIntCheck.size() == 0)
      throw new DBException(6);

    for (YearlyHouseLoanIntCheck t : yearlyHouseLoanIntCheck) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposDay.saveAll(yearlyHouseLoanIntCheck);	
      yearlyHouseLoanIntCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposMon.saveAll(yearlyHouseLoanIntCheck);	
      yearlyHouseLoanIntCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckReposHist.saveAll(yearlyHouseLoanIntCheck);
      yearlyHouseLoanIntCheckReposHist.flush();
    }
    else {
      yearlyHouseLoanIntCheck = yearlyHouseLoanIntCheckRepos.saveAll(yearlyHouseLoanIntCheck);
      yearlyHouseLoanIntCheckRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<YearlyHouseLoanIntCheck> yearlyHouseLoanIntCheck, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (yearlyHouseLoanIntCheck == null || yearlyHouseLoanIntCheck.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      yearlyHouseLoanIntCheckReposDay.deleteAll(yearlyHouseLoanIntCheck);	
      yearlyHouseLoanIntCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      yearlyHouseLoanIntCheckReposMon.deleteAll(yearlyHouseLoanIntCheck);	
      yearlyHouseLoanIntCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      yearlyHouseLoanIntCheckReposHist.deleteAll(yearlyHouseLoanIntCheck);
      yearlyHouseLoanIntCheckReposHist.flush();
    }
    else {
      yearlyHouseLoanIntCheckRepos.deleteAll(yearlyHouseLoanIntCheck);
      yearlyHouseLoanIntCheckRepos.flush();
    }
  }

}
