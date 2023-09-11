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
import com.st1.itx.db.domain.MonthlyLM052AssetClass;
import com.st1.itx.db.domain.MonthlyLM052AssetClassId;
import com.st1.itx.db.repository.online.MonthlyLM052AssetClassRepository;
import com.st1.itx.db.repository.day.MonthlyLM052AssetClassRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM052AssetClassRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM052AssetClassRepositoryHist;
import com.st1.itx.db.service.MonthlyLM052AssetClassService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM052AssetClassService")
@Repository
public class MonthlyLM052AssetClassServiceImpl extends ASpringJpaParm implements MonthlyLM052AssetClassService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private MonthlyLM052AssetClassRepository monthlyLM052AssetClassRepos;

  @Autowired
  private MonthlyLM052AssetClassRepositoryDay monthlyLM052AssetClassReposDay;

  @Autowired
  private MonthlyLM052AssetClassRepositoryMon monthlyLM052AssetClassReposMon;

  @Autowired
  private MonthlyLM052AssetClassRepositoryHist monthlyLM052AssetClassReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(monthlyLM052AssetClassRepos);
    org.junit.Assert.assertNotNull(monthlyLM052AssetClassReposDay);
    org.junit.Assert.assertNotNull(monthlyLM052AssetClassReposMon);
    org.junit.Assert.assertNotNull(monthlyLM052AssetClassReposHist);
  }

  @Override
  public MonthlyLM052AssetClass findById(MonthlyLM052AssetClassId monthlyLM052AssetClassId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + monthlyLM052AssetClassId);
    Optional<MonthlyLM052AssetClass> monthlyLM052AssetClass = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052AssetClass = monthlyLM052AssetClassReposDay.findById(monthlyLM052AssetClassId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052AssetClass = monthlyLM052AssetClassReposMon.findById(monthlyLM052AssetClassId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052AssetClass = monthlyLM052AssetClassReposHist.findById(monthlyLM052AssetClassId);
    else 
      monthlyLM052AssetClass = monthlyLM052AssetClassRepos.findById(monthlyLM052AssetClassId);
    MonthlyLM052AssetClass obj = monthlyLM052AssetClass.isPresent() ? monthlyLM052AssetClass.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<MonthlyLM052AssetClass> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM052AssetClass> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "AssetClassNo", "AcSubBookCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "AssetClassNo", "AcSubBookCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM052AssetClassReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM052AssetClassReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM052AssetClassReposHist.findAll(pageable);
    else 
      slice = monthlyLM052AssetClassRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public MonthlyLM052AssetClass holdById(MonthlyLM052AssetClassId monthlyLM052AssetClassId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM052AssetClassId);
    Optional<MonthlyLM052AssetClass> monthlyLM052AssetClass = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052AssetClass = monthlyLM052AssetClassReposDay.findByMonthlyLM052AssetClassId(monthlyLM052AssetClassId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052AssetClass = monthlyLM052AssetClassReposMon.findByMonthlyLM052AssetClassId(monthlyLM052AssetClassId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052AssetClass = monthlyLM052AssetClassReposHist.findByMonthlyLM052AssetClassId(monthlyLM052AssetClassId);
    else 
      monthlyLM052AssetClass = monthlyLM052AssetClassRepos.findByMonthlyLM052AssetClassId(monthlyLM052AssetClassId);
    return monthlyLM052AssetClass.isPresent() ? monthlyLM052AssetClass.get() : null;
  }

  @Override
  public MonthlyLM052AssetClass holdById(MonthlyLM052AssetClass monthlyLM052AssetClass, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM052AssetClass.getMonthlyLM052AssetClassId());
    Optional<MonthlyLM052AssetClass> monthlyLM052AssetClassT = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052AssetClassT = monthlyLM052AssetClassReposDay.findByMonthlyLM052AssetClassId(monthlyLM052AssetClass.getMonthlyLM052AssetClassId());
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052AssetClassT = monthlyLM052AssetClassReposMon.findByMonthlyLM052AssetClassId(monthlyLM052AssetClass.getMonthlyLM052AssetClassId());
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052AssetClassT = monthlyLM052AssetClassReposHist.findByMonthlyLM052AssetClassId(monthlyLM052AssetClass.getMonthlyLM052AssetClassId());
    else 
      monthlyLM052AssetClassT = monthlyLM052AssetClassRepos.findByMonthlyLM052AssetClassId(monthlyLM052AssetClass.getMonthlyLM052AssetClassId());
    return monthlyLM052AssetClassT.isPresent() ? monthlyLM052AssetClassT.get() : null;
  }

  @Override
  public MonthlyLM052AssetClass insert(MonthlyLM052AssetClass monthlyLM052AssetClass, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + monthlyLM052AssetClass.getMonthlyLM052AssetClassId());
    if (this.findById(monthlyLM052AssetClass.getMonthlyLM052AssetClassId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      monthlyLM052AssetClass.setCreateEmpNo(empNot);

    if(monthlyLM052AssetClass.getLastUpdateEmpNo() == null || monthlyLM052AssetClass.getLastUpdateEmpNo().isEmpty())
      monthlyLM052AssetClass.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM052AssetClassReposDay.saveAndFlush(monthlyLM052AssetClass);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM052AssetClassReposMon.saveAndFlush(monthlyLM052AssetClass);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM052AssetClassReposHist.saveAndFlush(monthlyLM052AssetClass);
    else 
    return monthlyLM052AssetClassRepos.saveAndFlush(monthlyLM052AssetClass);
  }

  @Override
  public MonthlyLM052AssetClass update(MonthlyLM052AssetClass monthlyLM052AssetClass, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM052AssetClass.getMonthlyLM052AssetClassId());
    if (!empNot.isEmpty())
      monthlyLM052AssetClass.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM052AssetClassReposDay.saveAndFlush(monthlyLM052AssetClass);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM052AssetClassReposMon.saveAndFlush(monthlyLM052AssetClass);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM052AssetClassReposHist.saveAndFlush(monthlyLM052AssetClass);
    else 
    return monthlyLM052AssetClassRepos.saveAndFlush(monthlyLM052AssetClass);
  }

  @Override
  public MonthlyLM052AssetClass update2(MonthlyLM052AssetClass monthlyLM052AssetClass, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM052AssetClass.getMonthlyLM052AssetClassId());
    if (!empNot.isEmpty())
      monthlyLM052AssetClass.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      monthlyLM052AssetClassReposDay.saveAndFlush(monthlyLM052AssetClass);	
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052AssetClassReposMon.saveAndFlush(monthlyLM052AssetClass);
    else if (dbName.equals(ContentName.onHist))
        monthlyLM052AssetClassReposHist.saveAndFlush(monthlyLM052AssetClass);
    else 
      monthlyLM052AssetClassRepos.saveAndFlush(monthlyLM052AssetClass);	
    return this.findById(monthlyLM052AssetClass.getMonthlyLM052AssetClassId());
  }

  @Override
  public void delete(MonthlyLM052AssetClass monthlyLM052AssetClass, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + monthlyLM052AssetClass.getMonthlyLM052AssetClassId());
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052AssetClassReposDay.delete(monthlyLM052AssetClass);	
      monthlyLM052AssetClassReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052AssetClassReposMon.delete(monthlyLM052AssetClass);	
      monthlyLM052AssetClassReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052AssetClassReposHist.delete(monthlyLM052AssetClass);
      monthlyLM052AssetClassReposHist.flush();
    }
    else {
      monthlyLM052AssetClassRepos.delete(monthlyLM052AssetClass);
      monthlyLM052AssetClassRepos.flush();
    }
   }

  @Override
  public void insertAll(List<MonthlyLM052AssetClass> monthlyLM052AssetClass, TitaVo... titaVo) throws DBException {
    if (monthlyLM052AssetClass == null || monthlyLM052AssetClass.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (MonthlyLM052AssetClass t : monthlyLM052AssetClass){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052AssetClass = monthlyLM052AssetClassReposDay.saveAll(monthlyLM052AssetClass);	
      monthlyLM052AssetClassReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052AssetClass = monthlyLM052AssetClassReposMon.saveAll(monthlyLM052AssetClass);	
      monthlyLM052AssetClassReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052AssetClass = monthlyLM052AssetClassReposHist.saveAll(monthlyLM052AssetClass);
      monthlyLM052AssetClassReposHist.flush();
    }
    else {
      monthlyLM052AssetClass = monthlyLM052AssetClassRepos.saveAll(monthlyLM052AssetClass);
      monthlyLM052AssetClassRepos.flush();
    }
    }

  @Override
  public void updateAll(List<MonthlyLM052AssetClass> monthlyLM052AssetClass, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (monthlyLM052AssetClass == null || monthlyLM052AssetClass.size() == 0)
      throw new DBException(6);

    for (MonthlyLM052AssetClass t : monthlyLM052AssetClass) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052AssetClass = monthlyLM052AssetClassReposDay.saveAll(monthlyLM052AssetClass);	
      monthlyLM052AssetClassReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052AssetClass = monthlyLM052AssetClassReposMon.saveAll(monthlyLM052AssetClass);	
      monthlyLM052AssetClassReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052AssetClass = monthlyLM052AssetClassReposHist.saveAll(monthlyLM052AssetClass);
      monthlyLM052AssetClassReposHist.flush();
    }
    else {
      monthlyLM052AssetClass = monthlyLM052AssetClassRepos.saveAll(monthlyLM052AssetClass);
      monthlyLM052AssetClassRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<MonthlyLM052AssetClass> monthlyLM052AssetClass, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (monthlyLM052AssetClass == null || monthlyLM052AssetClass.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052AssetClassReposDay.deleteAll(monthlyLM052AssetClass);	
      monthlyLM052AssetClassReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052AssetClassReposMon.deleteAll(monthlyLM052AssetClass);	
      monthlyLM052AssetClassReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052AssetClassReposHist.deleteAll(monthlyLM052AssetClass);
      monthlyLM052AssetClassReposHist.flush();
    }
    else {
      monthlyLM052AssetClassRepos.deleteAll(monthlyLM052AssetClass);
      monthlyLM052AssetClassRepos.flush();
    }
  }

  @Override
  public void Usp_L9_MonthlyLM052AssetClass_Ins(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052AssetClassReposDay.uspL9Monthlylm052assetclassIns(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052AssetClassReposMon.uspL9Monthlylm052assetclassIns(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052AssetClassReposHist.uspL9Monthlylm052assetclassIns(tbsdyf,  empNo);
   else
      monthlyLM052AssetClassRepos.uspL9Monthlylm052assetclassIns(tbsdyf,  empNo);
  }

}
