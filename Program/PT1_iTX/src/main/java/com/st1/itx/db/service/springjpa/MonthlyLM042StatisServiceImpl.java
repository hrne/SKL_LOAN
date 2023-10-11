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
import com.st1.itx.db.domain.MonthlyLM042Statis;
import com.st1.itx.db.domain.MonthlyLM042StatisId;
import com.st1.itx.db.repository.online.MonthlyLM042StatisRepository;
import com.st1.itx.db.repository.day.MonthlyLM042StatisRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM042StatisRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM042StatisRepositoryHist;
import com.st1.itx.db.service.MonthlyLM042StatisService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM042StatisService")
@Repository
public class MonthlyLM042StatisServiceImpl extends ASpringJpaParm implements MonthlyLM042StatisService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private MonthlyLM042StatisRepository monthlyLM042StatisRepos;

  @Autowired
  private MonthlyLM042StatisRepositoryDay monthlyLM042StatisReposDay;

  @Autowired
  private MonthlyLM042StatisRepositoryMon monthlyLM042StatisReposMon;

  @Autowired
  private MonthlyLM042StatisRepositoryHist monthlyLM042StatisReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(monthlyLM042StatisRepos);
    org.junit.Assert.assertNotNull(monthlyLM042StatisReposDay);
    org.junit.Assert.assertNotNull(monthlyLM042StatisReposMon);
    org.junit.Assert.assertNotNull(monthlyLM042StatisReposHist);
  }

  @Override
  public MonthlyLM042Statis findById(MonthlyLM042StatisId monthlyLM042StatisId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + monthlyLM042StatisId);
    Optional<MonthlyLM042Statis> monthlyLM042Statis = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM042Statis = monthlyLM042StatisReposDay.findById(monthlyLM042StatisId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM042Statis = monthlyLM042StatisReposMon.findById(monthlyLM042StatisId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM042Statis = monthlyLM042StatisReposHist.findById(monthlyLM042StatisId);
    else 
      monthlyLM042Statis = monthlyLM042StatisRepos.findById(monthlyLM042StatisId);
    MonthlyLM042Statis obj = monthlyLM042Statis.isPresent() ? monthlyLM042Statis.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<MonthlyLM042Statis> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM042Statis> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "LoanItem", "RelatedCode", "AssetClass"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "LoanItem", "RelatedCode", "AssetClass"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM042StatisReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM042StatisReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM042StatisReposHist.findAll(pageable);
    else 
      slice = monthlyLM042StatisRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<MonthlyLM042Statis> findYearMonthAll(int yearMonth_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM042Statis> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findYearMonthAll " + dbName + " : " + "yearMonth_0 : " + yearMonth_0);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM042StatisReposDay.findAllByYearMonthIs(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM042StatisReposMon.findAllByYearMonthIs(yearMonth_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM042StatisReposHist.findAllByYearMonthIs(yearMonth_0, pageable);
    else 
      slice = monthlyLM042StatisRepos.findAllByYearMonthIs(yearMonth_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<MonthlyLM042Statis> findItem(int yearMonth_0, String loanItem_1, String relatedCode_2, String assetClass_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM042Statis> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findItem " + dbName + " : " + "yearMonth_0 : " + yearMonth_0 + " loanItem_1 : " +  loanItem_1 + " relatedCode_2 : " +  relatedCode_2 + " assetClass_3 : " +  assetClass_3);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM042StatisReposDay.findAllByYearMonthIsAndLoanItemIsAndRelatedCodeIsAndAssetClassIs(yearMonth_0, loanItem_1, relatedCode_2, assetClass_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM042StatisReposMon.findAllByYearMonthIsAndLoanItemIsAndRelatedCodeIsAndAssetClassIs(yearMonth_0, loanItem_1, relatedCode_2, assetClass_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM042StatisReposHist.findAllByYearMonthIsAndLoanItemIsAndRelatedCodeIsAndAssetClassIs(yearMonth_0, loanItem_1, relatedCode_2, assetClass_3, pageable);
    else 
      slice = monthlyLM042StatisRepos.findAllByYearMonthIsAndLoanItemIsAndRelatedCodeIsAndAssetClassIs(yearMonth_0, loanItem_1, relatedCode_2, assetClass_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public MonthlyLM042Statis holdById(MonthlyLM042StatisId monthlyLM042StatisId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM042StatisId);
    Optional<MonthlyLM042Statis> monthlyLM042Statis = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM042Statis = monthlyLM042StatisReposDay.findByMonthlyLM042StatisId(monthlyLM042StatisId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM042Statis = monthlyLM042StatisReposMon.findByMonthlyLM042StatisId(monthlyLM042StatisId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM042Statis = monthlyLM042StatisReposHist.findByMonthlyLM042StatisId(monthlyLM042StatisId);
    else 
      monthlyLM042Statis = monthlyLM042StatisRepos.findByMonthlyLM042StatisId(monthlyLM042StatisId);
    return monthlyLM042Statis.isPresent() ? monthlyLM042Statis.get() : null;
  }

  @Override
  public MonthlyLM042Statis holdById(MonthlyLM042Statis monthlyLM042Statis, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM042Statis.getMonthlyLM042StatisId());
    Optional<MonthlyLM042Statis> monthlyLM042StatisT = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM042StatisT = monthlyLM042StatisReposDay.findByMonthlyLM042StatisId(monthlyLM042Statis.getMonthlyLM042StatisId());
    else if (dbName.equals(ContentName.onMon))
      monthlyLM042StatisT = monthlyLM042StatisReposMon.findByMonthlyLM042StatisId(monthlyLM042Statis.getMonthlyLM042StatisId());
    else if (dbName.equals(ContentName.onHist))
      monthlyLM042StatisT = monthlyLM042StatisReposHist.findByMonthlyLM042StatisId(monthlyLM042Statis.getMonthlyLM042StatisId());
    else 
      monthlyLM042StatisT = monthlyLM042StatisRepos.findByMonthlyLM042StatisId(monthlyLM042Statis.getMonthlyLM042StatisId());
    return monthlyLM042StatisT.isPresent() ? monthlyLM042StatisT.get() : null;
  }

  @Override
  public MonthlyLM042Statis insert(MonthlyLM042Statis monthlyLM042Statis, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + monthlyLM042Statis.getMonthlyLM042StatisId());
    if (this.findById(monthlyLM042Statis.getMonthlyLM042StatisId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      monthlyLM042Statis.setCreateEmpNo(empNot);

    if(monthlyLM042Statis.getLastUpdateEmpNo() == null || monthlyLM042Statis.getLastUpdateEmpNo().isEmpty())
      monthlyLM042Statis.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM042StatisReposDay.saveAndFlush(monthlyLM042Statis);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM042StatisReposMon.saveAndFlush(monthlyLM042Statis);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM042StatisReposHist.saveAndFlush(monthlyLM042Statis);
    else 
    return monthlyLM042StatisRepos.saveAndFlush(monthlyLM042Statis);
  }

  @Override
  public MonthlyLM042Statis update(MonthlyLM042Statis monthlyLM042Statis, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM042Statis.getMonthlyLM042StatisId());
    if (!empNot.isEmpty())
      monthlyLM042Statis.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM042StatisReposDay.saveAndFlush(monthlyLM042Statis);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM042StatisReposMon.saveAndFlush(monthlyLM042Statis);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM042StatisReposHist.saveAndFlush(monthlyLM042Statis);
    else 
    return monthlyLM042StatisRepos.saveAndFlush(monthlyLM042Statis);
  }

  @Override
  public MonthlyLM042Statis update2(MonthlyLM042Statis monthlyLM042Statis, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM042Statis.getMonthlyLM042StatisId());
    if (!empNot.isEmpty())
      monthlyLM042Statis.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      monthlyLM042StatisReposDay.saveAndFlush(monthlyLM042Statis);	
    else if (dbName.equals(ContentName.onMon))
      monthlyLM042StatisReposMon.saveAndFlush(monthlyLM042Statis);
    else if (dbName.equals(ContentName.onHist))
        monthlyLM042StatisReposHist.saveAndFlush(monthlyLM042Statis);
    else 
      monthlyLM042StatisRepos.saveAndFlush(monthlyLM042Statis);	
    return this.findById(monthlyLM042Statis.getMonthlyLM042StatisId());
  }

  @Override
  public void delete(MonthlyLM042Statis monthlyLM042Statis, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + monthlyLM042Statis.getMonthlyLM042StatisId());
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM042StatisReposDay.delete(monthlyLM042Statis);	
      monthlyLM042StatisReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM042StatisReposMon.delete(monthlyLM042Statis);	
      monthlyLM042StatisReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM042StatisReposHist.delete(monthlyLM042Statis);
      monthlyLM042StatisReposHist.flush();
    }
    else {
      monthlyLM042StatisRepos.delete(monthlyLM042Statis);
      monthlyLM042StatisRepos.flush();
    }
   }

  @Override
  public void insertAll(List<MonthlyLM042Statis> monthlyLM042Statis, TitaVo... titaVo) throws DBException {
    if (monthlyLM042Statis == null || monthlyLM042Statis.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (MonthlyLM042Statis t : monthlyLM042Statis){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM042Statis = monthlyLM042StatisReposDay.saveAll(monthlyLM042Statis);	
      monthlyLM042StatisReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM042Statis = monthlyLM042StatisReposMon.saveAll(monthlyLM042Statis);	
      monthlyLM042StatisReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM042Statis = monthlyLM042StatisReposHist.saveAll(monthlyLM042Statis);
      monthlyLM042StatisReposHist.flush();
    }
    else {
      monthlyLM042Statis = monthlyLM042StatisRepos.saveAll(monthlyLM042Statis);
      monthlyLM042StatisRepos.flush();
    }
    }

  @Override
  public void updateAll(List<MonthlyLM042Statis> monthlyLM042Statis, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (monthlyLM042Statis == null || monthlyLM042Statis.size() == 0)
      throw new DBException(6);

    for (MonthlyLM042Statis t : monthlyLM042Statis) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM042Statis = monthlyLM042StatisReposDay.saveAll(monthlyLM042Statis);	
      monthlyLM042StatisReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM042Statis = monthlyLM042StatisReposMon.saveAll(monthlyLM042Statis);	
      monthlyLM042StatisReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM042Statis = monthlyLM042StatisReposHist.saveAll(monthlyLM042Statis);
      monthlyLM042StatisReposHist.flush();
    }
    else {
      monthlyLM042Statis = monthlyLM042StatisRepos.saveAll(monthlyLM042Statis);
      monthlyLM042StatisRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<MonthlyLM042Statis> monthlyLM042Statis, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (monthlyLM042Statis == null || monthlyLM042Statis.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM042StatisReposDay.deleteAll(monthlyLM042Statis);	
      monthlyLM042StatisReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM042StatisReposMon.deleteAll(monthlyLM042Statis);	
      monthlyLM042StatisReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM042StatisReposHist.deleteAll(monthlyLM042Statis);
      monthlyLM042StatisReposHist.flush();
    }
    else {
      monthlyLM042StatisRepos.deleteAll(monthlyLM042Statis);
      monthlyLM042StatisRepos.flush();
    }
  }

}
