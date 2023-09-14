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
import com.st1.itx.db.domain.MonthlyLM052LoanAsset;
import com.st1.itx.db.domain.MonthlyLM052LoanAssetId;
import com.st1.itx.db.repository.online.MonthlyLM052LoanAssetRepository;
import com.st1.itx.db.repository.day.MonthlyLM052LoanAssetRepositoryDay;
import com.st1.itx.db.repository.mon.MonthlyLM052LoanAssetRepositoryMon;
import com.st1.itx.db.repository.hist.MonthlyLM052LoanAssetRepositoryHist;
import com.st1.itx.db.service.MonthlyLM052LoanAssetService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("monthlyLM052LoanAssetService")
@Repository
public class MonthlyLM052LoanAssetServiceImpl extends ASpringJpaParm implements MonthlyLM052LoanAssetService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private MonthlyLM052LoanAssetRepository monthlyLM052LoanAssetRepos;

  @Autowired
  private MonthlyLM052LoanAssetRepositoryDay monthlyLM052LoanAssetReposDay;

  @Autowired
  private MonthlyLM052LoanAssetRepositoryMon monthlyLM052LoanAssetReposMon;

  @Autowired
  private MonthlyLM052LoanAssetRepositoryHist monthlyLM052LoanAssetReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(monthlyLM052LoanAssetRepos);
    org.junit.Assert.assertNotNull(monthlyLM052LoanAssetReposDay);
    org.junit.Assert.assertNotNull(monthlyLM052LoanAssetReposMon);
    org.junit.Assert.assertNotNull(monthlyLM052LoanAssetReposHist);
  }

  @Override
  public MonthlyLM052LoanAsset findById(MonthlyLM052LoanAssetId monthlyLM052LoanAssetId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + monthlyLM052LoanAssetId);
    Optional<MonthlyLM052LoanAsset> monthlyLM052LoanAsset = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposDay.findById(monthlyLM052LoanAssetId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposMon.findById(monthlyLM052LoanAssetId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposHist.findById(monthlyLM052LoanAssetId);
    else 
      monthlyLM052LoanAsset = monthlyLM052LoanAssetRepos.findById(monthlyLM052LoanAssetId);
    MonthlyLM052LoanAsset obj = monthlyLM052LoanAsset.isPresent() ? monthlyLM052LoanAsset.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<MonthlyLM052LoanAsset> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MonthlyLM052LoanAsset> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "YearMonth", "LoanAssetCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "YearMonth", "LoanAssetCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = monthlyLM052LoanAssetReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = monthlyLM052LoanAssetReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = monthlyLM052LoanAssetReposHist.findAll(pageable);
    else 
      slice = monthlyLM052LoanAssetRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public MonthlyLM052LoanAsset holdById(MonthlyLM052LoanAssetId monthlyLM052LoanAssetId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM052LoanAssetId);
    Optional<MonthlyLM052LoanAsset> monthlyLM052LoanAsset = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposDay.findByMonthlyLM052LoanAssetId(monthlyLM052LoanAssetId);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposMon.findByMonthlyLM052LoanAssetId(monthlyLM052LoanAssetId);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposHist.findByMonthlyLM052LoanAssetId(monthlyLM052LoanAssetId);
    else 
      monthlyLM052LoanAsset = monthlyLM052LoanAssetRepos.findByMonthlyLM052LoanAssetId(monthlyLM052LoanAssetId);
    return monthlyLM052LoanAsset.isPresent() ? monthlyLM052LoanAsset.get() : null;
  }

  @Override
  public MonthlyLM052LoanAsset holdById(MonthlyLM052LoanAsset monthlyLM052LoanAsset, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + monthlyLM052LoanAsset.getMonthlyLM052LoanAssetId());
    Optional<MonthlyLM052LoanAsset> monthlyLM052LoanAssetT = null;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052LoanAssetT = monthlyLM052LoanAssetReposDay.findByMonthlyLM052LoanAssetId(monthlyLM052LoanAsset.getMonthlyLM052LoanAssetId());
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052LoanAssetT = monthlyLM052LoanAssetReposMon.findByMonthlyLM052LoanAssetId(monthlyLM052LoanAsset.getMonthlyLM052LoanAssetId());
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052LoanAssetT = monthlyLM052LoanAssetReposHist.findByMonthlyLM052LoanAssetId(monthlyLM052LoanAsset.getMonthlyLM052LoanAssetId());
    else 
      monthlyLM052LoanAssetT = monthlyLM052LoanAssetRepos.findByMonthlyLM052LoanAssetId(monthlyLM052LoanAsset.getMonthlyLM052LoanAssetId());
    return monthlyLM052LoanAssetT.isPresent() ? monthlyLM052LoanAssetT.get() : null;
  }

  @Override
  public MonthlyLM052LoanAsset insert(MonthlyLM052LoanAsset monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + monthlyLM052LoanAsset.getMonthlyLM052LoanAssetId());
    if (this.findById(monthlyLM052LoanAsset.getMonthlyLM052LoanAssetId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      monthlyLM052LoanAsset.setCreateEmpNo(empNot);

    if(monthlyLM052LoanAsset.getLastUpdateEmpNo() == null || monthlyLM052LoanAsset.getLastUpdateEmpNo().isEmpty())
      monthlyLM052LoanAsset.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM052LoanAssetReposDay.saveAndFlush(monthlyLM052LoanAsset);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM052LoanAssetReposMon.saveAndFlush(monthlyLM052LoanAsset);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM052LoanAssetReposHist.saveAndFlush(monthlyLM052LoanAsset);
    else 
    return monthlyLM052LoanAssetRepos.saveAndFlush(monthlyLM052LoanAsset);
  }

  @Override
  public MonthlyLM052LoanAsset update(MonthlyLM052LoanAsset monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM052LoanAsset.getMonthlyLM052LoanAssetId());
    if (!empNot.isEmpty())
      monthlyLM052LoanAsset.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return monthlyLM052LoanAssetReposDay.saveAndFlush(monthlyLM052LoanAsset);	
    else if (dbName.equals(ContentName.onMon))
      return monthlyLM052LoanAssetReposMon.saveAndFlush(monthlyLM052LoanAsset);
    else if (dbName.equals(ContentName.onHist))
      return monthlyLM052LoanAssetReposHist.saveAndFlush(monthlyLM052LoanAsset);
    else 
    return monthlyLM052LoanAssetRepos.saveAndFlush(monthlyLM052LoanAsset);
  }

  @Override
  public MonthlyLM052LoanAsset update2(MonthlyLM052LoanAsset monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + monthlyLM052LoanAsset.getMonthlyLM052LoanAssetId());
    if (!empNot.isEmpty())
      monthlyLM052LoanAsset.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      monthlyLM052LoanAssetReposDay.saveAndFlush(monthlyLM052LoanAsset);	
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052LoanAssetReposMon.saveAndFlush(monthlyLM052LoanAsset);
    else if (dbName.equals(ContentName.onHist))
        monthlyLM052LoanAssetReposHist.saveAndFlush(monthlyLM052LoanAsset);
    else 
      monthlyLM052LoanAssetRepos.saveAndFlush(monthlyLM052LoanAsset);	
    return this.findById(monthlyLM052LoanAsset.getMonthlyLM052LoanAssetId());
  }

  @Override
  public void delete(MonthlyLM052LoanAsset monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + monthlyLM052LoanAsset.getMonthlyLM052LoanAssetId());
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052LoanAssetReposDay.delete(monthlyLM052LoanAsset);	
      monthlyLM052LoanAssetReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052LoanAssetReposMon.delete(monthlyLM052LoanAsset);	
      monthlyLM052LoanAssetReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052LoanAssetReposHist.delete(monthlyLM052LoanAsset);
      monthlyLM052LoanAssetReposHist.flush();
    }
    else {
      monthlyLM052LoanAssetRepos.delete(monthlyLM052LoanAsset);
      monthlyLM052LoanAssetRepos.flush();
    }
   }

  @Override
  public void insertAll(List<MonthlyLM052LoanAsset> monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException {
    if (monthlyLM052LoanAsset == null || monthlyLM052LoanAsset.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (MonthlyLM052LoanAsset t : monthlyLM052LoanAsset){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposDay.saveAll(monthlyLM052LoanAsset);	
      monthlyLM052LoanAssetReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposMon.saveAll(monthlyLM052LoanAsset);	
      monthlyLM052LoanAssetReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposHist.saveAll(monthlyLM052LoanAsset);
      monthlyLM052LoanAssetReposHist.flush();
    }
    else {
      monthlyLM052LoanAsset = monthlyLM052LoanAssetRepos.saveAll(monthlyLM052LoanAsset);
      monthlyLM052LoanAssetRepos.flush();
    }
    }

  @Override
  public void updateAll(List<MonthlyLM052LoanAsset> monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (monthlyLM052LoanAsset == null || monthlyLM052LoanAsset.size() == 0)
      throw new DBException(6);

    for (MonthlyLM052LoanAsset t : monthlyLM052LoanAsset) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposDay.saveAll(monthlyLM052LoanAsset);	
      monthlyLM052LoanAssetReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposMon.saveAll(monthlyLM052LoanAsset);	
      monthlyLM052LoanAssetReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052LoanAsset = monthlyLM052LoanAssetReposHist.saveAll(monthlyLM052LoanAsset);
      monthlyLM052LoanAssetReposHist.flush();
    }
    else {
      monthlyLM052LoanAsset = monthlyLM052LoanAssetRepos.saveAll(monthlyLM052LoanAsset);
      monthlyLM052LoanAssetRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<MonthlyLM052LoanAsset> monthlyLM052LoanAsset, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (monthlyLM052LoanAsset == null || monthlyLM052LoanAsset.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      monthlyLM052LoanAssetReposDay.deleteAll(monthlyLM052LoanAsset);	
      monthlyLM052LoanAssetReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      monthlyLM052LoanAssetReposMon.deleteAll(monthlyLM052LoanAsset);	
      monthlyLM052LoanAssetReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      monthlyLM052LoanAssetReposHist.deleteAll(monthlyLM052LoanAsset);
      monthlyLM052LoanAssetReposHist.flush();
    }
    else {
      monthlyLM052LoanAssetRepos.deleteAll(monthlyLM052LoanAsset);
      monthlyLM052LoanAssetRepos.flush();
    }
  }

  @Override
  public void Usp_L9_MonthlyLM052LoanAsset_Ins(int tbsdyf,  String empNo,  String jobTxSeq, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      monthlyLM052LoanAssetReposDay.uspL9Monthlylm052loanassetIns(tbsdyf,  empNo,  jobTxSeq);
    else if (dbName.equals(ContentName.onMon))
      monthlyLM052LoanAssetReposMon.uspL9Monthlylm052loanassetIns(tbsdyf,  empNo,  jobTxSeq);
    else if (dbName.equals(ContentName.onHist))
      monthlyLM052LoanAssetReposHist.uspL9Monthlylm052loanassetIns(tbsdyf,  empNo,  jobTxSeq);
   else
      monthlyLM052LoanAssetRepos.uspL9Monthlylm052loanassetIns(tbsdyf,  empNo,  jobTxSeq);
  }

}
