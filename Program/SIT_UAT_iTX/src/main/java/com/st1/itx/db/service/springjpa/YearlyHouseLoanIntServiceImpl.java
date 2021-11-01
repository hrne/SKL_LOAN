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
import com.st1.itx.db.domain.YearlyHouseLoanInt;
import com.st1.itx.db.domain.YearlyHouseLoanIntId;
import com.st1.itx.db.repository.online.YearlyHouseLoanIntRepository;
import com.st1.itx.db.repository.day.YearlyHouseLoanIntRepositoryDay;
import com.st1.itx.db.repository.mon.YearlyHouseLoanIntRepositoryMon;
import com.st1.itx.db.repository.hist.YearlyHouseLoanIntRepositoryHist;
import com.st1.itx.db.service.YearlyHouseLoanIntService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("yearlyHouseLoanIntService")
@Repository
public class YearlyHouseLoanIntServiceImpl extends ASpringJpaParm implements YearlyHouseLoanIntService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private YearlyHouseLoanIntRepository yearlyHouseLoanIntRepos;

  @Autowired
  private YearlyHouseLoanIntRepositoryDay yearlyHouseLoanIntReposDay;

  @Autowired
  private YearlyHouseLoanIntRepositoryMon yearlyHouseLoanIntReposMon;

  @Autowired
  private YearlyHouseLoanIntRepositoryHist yearlyHouseLoanIntReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(yearlyHouseLoanIntRepos);
    org.junit.Assert.assertNotNull(yearlyHouseLoanIntReposDay);
    org.junit.Assert.assertNotNull(yearlyHouseLoanIntReposMon);
    org.junit.Assert.assertNotNull(yearlyHouseLoanIntReposHist);
  }

  @Override
  public YearlyHouseLoanInt findById(YearlyHouseLoanIntId yearlyHouseLoanIntId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + yearlyHouseLoanIntId);
    Optional<YearlyHouseLoanInt> yearlyHouseLoanInt = null;
    if (dbName.equals(ContentName.onDay))
      yearlyHouseLoanInt = yearlyHouseLoanIntReposDay.findById(yearlyHouseLoanIntId);
    else if (dbName.equals(ContentName.onMon))
      yearlyHouseLoanInt = yearlyHouseLoanIntReposMon.findById(yearlyHouseLoanIntId);
    else if (dbName.equals(ContentName.onHist))
      yearlyHouseLoanInt = yearlyHouseLoanIntReposHist.findById(yearlyHouseLoanIntId);
    else 
      yearlyHouseLoanInt = yearlyHouseLoanIntRepos.findById(yearlyHouseLoanIntId);
    YearlyHouseLoanInt obj = yearlyHouseLoanInt.isPresent() ? yearlyHouseLoanInt.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<YearlyHouseLoanInt> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<YearlyHouseLoanInt> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "UsageCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "CustNo", "FacmNo", "UsageCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = yearlyHouseLoanIntReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = yearlyHouseLoanIntReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = yearlyHouseLoanIntReposHist.findAll(pageable);
    else 
      slice = yearlyHouseLoanIntRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<YearlyHouseLoanInt> findYearMonth(int yearMonth_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<YearlyHouseLoanInt> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findYearMonth " + dbName + " : " + "yearMonth_0 : " + yearMonth_0);
    if (dbName.equals(ContentName.onDay))
      slice = yearlyHouseLoanIntReposDay.findAllByYearMonthIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = yearlyHouseLoanIntReposMon.findAllByYearMonthIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = yearlyHouseLoanIntReposHist.findAllByYearMonthIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, pageable);
    else 
      slice = yearlyHouseLoanIntRepos.findAllByYearMonthIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<YearlyHouseLoanInt> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<YearlyHouseLoanInt> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = yearlyHouseLoanIntReposDay.findAllByCustNoIsOrderByYearMonthAscCustNoAscFacmNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = yearlyHouseLoanIntReposMon.findAllByCustNoIsOrderByYearMonthAscCustNoAscFacmNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = yearlyHouseLoanIntReposHist.findAllByCustNoIsOrderByYearMonthAscCustNoAscFacmNoAsc(custNo_0, pageable);
    else 
      slice = yearlyHouseLoanIntRepos.findAllByCustNoIsOrderByYearMonthAscCustNoAscFacmNoAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<YearlyHouseLoanInt> findYearCustNo(int yearMonth_0, int custNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<YearlyHouseLoanInt> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findYearCustNo " + dbName + " : " + "yearMonth_0 : " + yearMonth_0 + " custNo_1 : " +  custNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = yearlyHouseLoanIntReposDay.findAllByYearMonthIsAndCustNoIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, custNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = yearlyHouseLoanIntReposMon.findAllByYearMonthIsAndCustNoIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, custNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = yearlyHouseLoanIntReposHist.findAllByYearMonthIsAndCustNoIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, custNo_1, pageable);
    else 
      slice = yearlyHouseLoanIntRepos.findAllByYearMonthIsAndCustNoIsOrderByYearMonthAscCustNoAscFacmNoAsc(yearMonth_0, custNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<YearlyHouseLoanInt> findbyYear(int yearMonth_0, int yearMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<YearlyHouseLoanInt> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findbyYear " + dbName + " : " + "yearMonth_0 : " + yearMonth_0 + " yearMonth_1 : " +  yearMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = yearlyHouseLoanIntReposDay.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = yearlyHouseLoanIntReposMon.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = yearlyHouseLoanIntReposHist.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);
    else 
      slice = yearlyHouseLoanIntRepos.findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(yearMonth_0, yearMonth_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public YearlyHouseLoanInt holdById(YearlyHouseLoanIntId yearlyHouseLoanIntId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + yearlyHouseLoanIntId);
    Optional<YearlyHouseLoanInt> yearlyHouseLoanInt = null;
    if (dbName.equals(ContentName.onDay))
      yearlyHouseLoanInt = yearlyHouseLoanIntReposDay.findByYearlyHouseLoanIntId(yearlyHouseLoanIntId);
    else if (dbName.equals(ContentName.onMon))
      yearlyHouseLoanInt = yearlyHouseLoanIntReposMon.findByYearlyHouseLoanIntId(yearlyHouseLoanIntId);
    else if (dbName.equals(ContentName.onHist))
      yearlyHouseLoanInt = yearlyHouseLoanIntReposHist.findByYearlyHouseLoanIntId(yearlyHouseLoanIntId);
    else 
      yearlyHouseLoanInt = yearlyHouseLoanIntRepos.findByYearlyHouseLoanIntId(yearlyHouseLoanIntId);
    return yearlyHouseLoanInt.isPresent() ? yearlyHouseLoanInt.get() : null;
  }

  @Override
  public YearlyHouseLoanInt holdById(YearlyHouseLoanInt yearlyHouseLoanInt, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + yearlyHouseLoanInt.getYearlyHouseLoanIntId());
    Optional<YearlyHouseLoanInt> yearlyHouseLoanIntT = null;
    if (dbName.equals(ContentName.onDay))
      yearlyHouseLoanIntT = yearlyHouseLoanIntReposDay.findByYearlyHouseLoanIntId(yearlyHouseLoanInt.getYearlyHouseLoanIntId());
    else if (dbName.equals(ContentName.onMon))
      yearlyHouseLoanIntT = yearlyHouseLoanIntReposMon.findByYearlyHouseLoanIntId(yearlyHouseLoanInt.getYearlyHouseLoanIntId());
    else if (dbName.equals(ContentName.onHist))
      yearlyHouseLoanIntT = yearlyHouseLoanIntReposHist.findByYearlyHouseLoanIntId(yearlyHouseLoanInt.getYearlyHouseLoanIntId());
    else 
      yearlyHouseLoanIntT = yearlyHouseLoanIntRepos.findByYearlyHouseLoanIntId(yearlyHouseLoanInt.getYearlyHouseLoanIntId());
    return yearlyHouseLoanIntT.isPresent() ? yearlyHouseLoanIntT.get() : null;
  }

  @Override
  public YearlyHouseLoanInt insert(YearlyHouseLoanInt yearlyHouseLoanInt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + yearlyHouseLoanInt.getYearlyHouseLoanIntId());
    if (this.findById(yearlyHouseLoanInt.getYearlyHouseLoanIntId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      yearlyHouseLoanInt.setCreateEmpNo(empNot);

    if(yearlyHouseLoanInt.getLastUpdateEmpNo() == null || yearlyHouseLoanInt.getLastUpdateEmpNo().isEmpty())
      yearlyHouseLoanInt.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return yearlyHouseLoanIntReposDay.saveAndFlush(yearlyHouseLoanInt);	
    else if (dbName.equals(ContentName.onMon))
      return yearlyHouseLoanIntReposMon.saveAndFlush(yearlyHouseLoanInt);
    else if (dbName.equals(ContentName.onHist))
      return yearlyHouseLoanIntReposHist.saveAndFlush(yearlyHouseLoanInt);
    else 
    return yearlyHouseLoanIntRepos.saveAndFlush(yearlyHouseLoanInt);
  }

  @Override
  public YearlyHouseLoanInt update(YearlyHouseLoanInt yearlyHouseLoanInt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + yearlyHouseLoanInt.getYearlyHouseLoanIntId());
    if (!empNot.isEmpty())
      yearlyHouseLoanInt.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return yearlyHouseLoanIntReposDay.saveAndFlush(yearlyHouseLoanInt);	
    else if (dbName.equals(ContentName.onMon))
      return yearlyHouseLoanIntReposMon.saveAndFlush(yearlyHouseLoanInt);
    else if (dbName.equals(ContentName.onHist))
      return yearlyHouseLoanIntReposHist.saveAndFlush(yearlyHouseLoanInt);
    else 
    return yearlyHouseLoanIntRepos.saveAndFlush(yearlyHouseLoanInt);
  }

  @Override
  public YearlyHouseLoanInt update2(YearlyHouseLoanInt yearlyHouseLoanInt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + yearlyHouseLoanInt.getYearlyHouseLoanIntId());
    if (!empNot.isEmpty())
      yearlyHouseLoanInt.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      yearlyHouseLoanIntReposDay.saveAndFlush(yearlyHouseLoanInt);	
    else if (dbName.equals(ContentName.onMon))
      yearlyHouseLoanIntReposMon.saveAndFlush(yearlyHouseLoanInt);
    else if (dbName.equals(ContentName.onHist))
        yearlyHouseLoanIntReposHist.saveAndFlush(yearlyHouseLoanInt);
    else 
      yearlyHouseLoanIntRepos.saveAndFlush(yearlyHouseLoanInt);	
    return this.findById(yearlyHouseLoanInt.getYearlyHouseLoanIntId());
  }

  @Override
  public void delete(YearlyHouseLoanInt yearlyHouseLoanInt, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + yearlyHouseLoanInt.getYearlyHouseLoanIntId());
    if (dbName.equals(ContentName.onDay)) {
      yearlyHouseLoanIntReposDay.delete(yearlyHouseLoanInt);	
      yearlyHouseLoanIntReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      yearlyHouseLoanIntReposMon.delete(yearlyHouseLoanInt);	
      yearlyHouseLoanIntReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      yearlyHouseLoanIntReposHist.delete(yearlyHouseLoanInt);
      yearlyHouseLoanIntReposHist.flush();
    }
    else {
      yearlyHouseLoanIntRepos.delete(yearlyHouseLoanInt);
      yearlyHouseLoanIntRepos.flush();
    }
   }

  @Override
  public void insertAll(List<YearlyHouseLoanInt> yearlyHouseLoanInt, TitaVo... titaVo) throws DBException {
    if (yearlyHouseLoanInt == null || yearlyHouseLoanInt.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (YearlyHouseLoanInt t : yearlyHouseLoanInt){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      yearlyHouseLoanInt = yearlyHouseLoanIntReposDay.saveAll(yearlyHouseLoanInt);	
      yearlyHouseLoanIntReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      yearlyHouseLoanInt = yearlyHouseLoanIntReposMon.saveAll(yearlyHouseLoanInt);	
      yearlyHouseLoanIntReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      yearlyHouseLoanInt = yearlyHouseLoanIntReposHist.saveAll(yearlyHouseLoanInt);
      yearlyHouseLoanIntReposHist.flush();
    }
    else {
      yearlyHouseLoanInt = yearlyHouseLoanIntRepos.saveAll(yearlyHouseLoanInt);
      yearlyHouseLoanIntRepos.flush();
    }
    }

  @Override
  public void updateAll(List<YearlyHouseLoanInt> yearlyHouseLoanInt, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (yearlyHouseLoanInt == null || yearlyHouseLoanInt.size() == 0)
      throw new DBException(6);

    for (YearlyHouseLoanInt t : yearlyHouseLoanInt) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      yearlyHouseLoanInt = yearlyHouseLoanIntReposDay.saveAll(yearlyHouseLoanInt);	
      yearlyHouseLoanIntReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      yearlyHouseLoanInt = yearlyHouseLoanIntReposMon.saveAll(yearlyHouseLoanInt);	
      yearlyHouseLoanIntReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      yearlyHouseLoanInt = yearlyHouseLoanIntReposHist.saveAll(yearlyHouseLoanInt);
      yearlyHouseLoanIntReposHist.flush();
    }
    else {
      yearlyHouseLoanInt = yearlyHouseLoanIntRepos.saveAll(yearlyHouseLoanInt);
      yearlyHouseLoanIntRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<YearlyHouseLoanInt> yearlyHouseLoanInt, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (yearlyHouseLoanInt == null || yearlyHouseLoanInt.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      yearlyHouseLoanIntReposDay.deleteAll(yearlyHouseLoanInt);	
      yearlyHouseLoanIntReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      yearlyHouseLoanIntReposMon.deleteAll(yearlyHouseLoanInt);	
      yearlyHouseLoanIntReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      yearlyHouseLoanIntReposHist.deleteAll(yearlyHouseLoanInt);
      yearlyHouseLoanIntReposHist.flush();
    }
    else {
      yearlyHouseLoanIntRepos.deleteAll(yearlyHouseLoanInt);
      yearlyHouseLoanIntRepos.flush();
    }
  }

}
