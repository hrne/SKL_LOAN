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
import com.st1.itx.db.domain.JobMain;
import com.st1.itx.db.domain.JobMainId;
import com.st1.itx.db.repository.online.JobMainRepository;
import com.st1.itx.db.repository.day.JobMainRepositoryDay;
import com.st1.itx.db.repository.mon.JobMainRepositoryMon;
import com.st1.itx.db.repository.hist.JobMainRepositoryHist;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jobMainService")
@Repository
public class JobMainServiceImpl extends ASpringJpaParm implements JobMainService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JobMainRepository jobMainRepos;

  @Autowired
  private JobMainRepositoryDay jobMainReposDay;

  @Autowired
  private JobMainRepositoryMon jobMainReposMon;

  @Autowired
  private JobMainRepositoryHist jobMainReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jobMainRepos);
    org.junit.Assert.assertNotNull(jobMainReposDay);
    org.junit.Assert.assertNotNull(jobMainReposMon);
    org.junit.Assert.assertNotNull(jobMainReposHist);
  }

  @Override
  public JobMain findById(JobMainId jobMainId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jobMainId);
    Optional<JobMain> jobMain = null;
    if (dbName.equals(ContentName.onDay))
      jobMain = jobMainReposDay.findById(jobMainId);
    else if (dbName.equals(ContentName.onMon))
      jobMain = jobMainReposMon.findById(jobMainId);
    else if (dbName.equals(ContentName.onHist))
      jobMain = jobMainReposHist.findById(jobMainId);
    else 
      jobMain = jobMainRepos.findById(jobMainId);
    JobMain obj = jobMain.isPresent() ? jobMain.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JobMain> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JobMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ExecDate", "JobCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ExecDate", "JobCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jobMainReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jobMainReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jobMainReposHist.findAll(pageable);
    else 
      slice = jobMainRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JobMain holdById(JobMainId jobMainId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jobMainId);
    Optional<JobMain> jobMain = null;
    if (dbName.equals(ContentName.onDay))
      jobMain = jobMainReposDay.findByJobMainId(jobMainId);
    else if (dbName.equals(ContentName.onMon))
      jobMain = jobMainReposMon.findByJobMainId(jobMainId);
    else if (dbName.equals(ContentName.onHist))
      jobMain = jobMainReposHist.findByJobMainId(jobMainId);
    else 
      jobMain = jobMainRepos.findByJobMainId(jobMainId);
    return jobMain.isPresent() ? jobMain.get() : null;
  }

  @Override
  public JobMain holdById(JobMain jobMain, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jobMain.getJobMainId());
    Optional<JobMain> jobMainT = null;
    if (dbName.equals(ContentName.onDay))
      jobMainT = jobMainReposDay.findByJobMainId(jobMain.getJobMainId());
    else if (dbName.equals(ContentName.onMon))
      jobMainT = jobMainReposMon.findByJobMainId(jobMain.getJobMainId());
    else if (dbName.equals(ContentName.onHist))
      jobMainT = jobMainReposHist.findByJobMainId(jobMain.getJobMainId());
    else 
      jobMainT = jobMainRepos.findByJobMainId(jobMain.getJobMainId());
    return jobMainT.isPresent() ? jobMainT.get() : null;
  }

  @Override
  public JobMain insert(JobMain jobMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jobMain.getJobMainId());
    if (this.findById(jobMain.getJobMainId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jobMain.setCreateEmpNo(empNot);

    if(jobMain.getLastUpdateEmpNo() == null || jobMain.getLastUpdateEmpNo().isEmpty())
      jobMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jobMainReposDay.saveAndFlush(jobMain);	
    else if (dbName.equals(ContentName.onMon))
      return jobMainReposMon.saveAndFlush(jobMain);
    else if (dbName.equals(ContentName.onHist))
      return jobMainReposHist.saveAndFlush(jobMain);
    else 
    return jobMainRepos.saveAndFlush(jobMain);
  }

  @Override
  public JobMain update(JobMain jobMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jobMain.getJobMainId());
    if (!empNot.isEmpty())
      jobMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jobMainReposDay.saveAndFlush(jobMain);	
    else if (dbName.equals(ContentName.onMon))
      return jobMainReposMon.saveAndFlush(jobMain);
    else if (dbName.equals(ContentName.onHist))
      return jobMainReposHist.saveAndFlush(jobMain);
    else 
    return jobMainRepos.saveAndFlush(jobMain);
  }

  @Override
  public JobMain update2(JobMain jobMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jobMain.getJobMainId());
    if (!empNot.isEmpty())
      jobMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.saveAndFlush(jobMain);	
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.saveAndFlush(jobMain);
    else if (dbName.equals(ContentName.onHist))
        jobMainReposHist.saveAndFlush(jobMain);
    else 
      jobMainRepos.saveAndFlush(jobMain);	
    return this.findById(jobMain.getJobMainId());
  }

  @Override
  public void delete(JobMain jobMain, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jobMain.getJobMainId());
    if (dbName.equals(ContentName.onDay)) {
      jobMainReposDay.delete(jobMain);	
      jobMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jobMainReposMon.delete(jobMain);	
      jobMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jobMainReposHist.delete(jobMain);
      jobMainReposHist.flush();
    }
    else {
      jobMainRepos.delete(jobMain);
      jobMainRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JobMain> jobMain, TitaVo... titaVo) throws DBException {
    if (jobMain == null || jobMain.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JobMain t : jobMain){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jobMain = jobMainReposDay.saveAll(jobMain);	
      jobMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jobMain = jobMainReposMon.saveAll(jobMain);	
      jobMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jobMain = jobMainReposHist.saveAll(jobMain);
      jobMainReposHist.flush();
    }
    else {
      jobMain = jobMainRepos.saveAll(jobMain);
      jobMainRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JobMain> jobMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jobMain == null || jobMain.size() == 0)
      throw new DBException(6);

    for (JobMain t : jobMain) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jobMain = jobMainReposDay.saveAll(jobMain);	
      jobMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jobMain = jobMainReposMon.saveAll(jobMain);	
      jobMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jobMain = jobMainReposHist.saveAll(jobMain);
      jobMainReposHist.flush();
    }
    else {
      jobMain = jobMainRepos.saveAll(jobMain);
      jobMainRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JobMain> jobMain, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jobMain == null || jobMain.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jobMainReposDay.deleteAll(jobMain);	
      jobMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jobMainReposMon.deleteAll(jobMain);	
      jobMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jobMainReposHist.deleteAll(jobMain);
      jobMainReposHist.flush();
    }
    else {
      jobMainRepos.deleteAll(jobMain);
      jobMainRepos.flush();
    }
  }

  @Override
  public void Usp_L5_CollList_Upd(int tbsdyf,  String empNo, int l6bsdyf, int l7bsdyf, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL5ColllistUpd(tbsdyf,  empNo, l6bsdyf, l7bsdyf);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL5ColllistUpd(tbsdyf,  empNo, l6bsdyf, l7bsdyf);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL5ColllistUpd(tbsdyf,  empNo, l6bsdyf, l7bsdyf);
   else
      jobMainRepos.uspL5ColllistUpd(tbsdyf,  empNo, l6bsdyf, l7bsdyf);
  }

  @Override
  public void Usp_L5_InnReCheck_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL5InnrecheckUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL5InnrecheckUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL5InnrecheckUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL5InnrecheckUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L9_DailyLoanBal_Upd(int tbsdyf,  String empNo, int mfbsdyf, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9DailyloanbalUpd(tbsdyf,  empNo, mfbsdyf);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9DailyloanbalUpd(tbsdyf,  empNo, mfbsdyf);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9DailyloanbalUpd(tbsdyf,  empNo, mfbsdyf);
   else
      jobMainRepos.uspL9DailyloanbalUpd(tbsdyf,  empNo, mfbsdyf);
  }

  @Override
  public void Usp_L8_JcicB204_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb204Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb204Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb204Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb204Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB211_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb211Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb211Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb211Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb211Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L9_MonthlyLoanBal_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9MonthlyloanbalUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9MonthlyloanbalUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9MonthlyloanbalUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL9MonthlyloanbalUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L9_MonthlyFacBal_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9MonthlyfacbalUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9MonthlyfacbalUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9MonthlyfacbalUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL9MonthlyfacbalUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_Ias39Loan34Data_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Ias39loan34dataUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Ias39loan34dataUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Ias39loan34dataUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Ias39loan34dataUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicMonthlyLoanData_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8JcicmonthlyloandataUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8JcicmonthlyloandataUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8JcicmonthlyloandataUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8JcicmonthlyloandataUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_Ifrs9LoanData_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Ifrs9loandataUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Ifrs9loandataUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Ifrs9loandataUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Ifrs9loandataUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_Ifrs9FacData_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Ifrs9facdataUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Ifrs9facdataUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Ifrs9facdataUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Ifrs9facdataUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_Ias39LoanCommit_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Ias39loancommitUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Ias39loancommitUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Ias39loancommitUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Ias39loancommitUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L9_MonthlyLM003_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9Monthlylm003Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9Monthlylm003Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9Monthlylm003Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL9Monthlylm003Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L9_MonthlyLM028_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9Monthlylm028Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9Monthlylm028Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9Monthlylm028Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL9Monthlylm028Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L9_MonthlyLM051_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9Monthlylm051Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9Monthlylm051Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9Monthlylm051Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL9Monthlylm051Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L9_MonthlyLM032_Upd(int TBSDYF, String empNo,int LMBSDYF, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9Monthlylm032Upd(TBSDYF, empNo,LMBSDYF);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9Monthlylm032Upd(TBSDYF, empNo,LMBSDYF);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9Monthlylm032Upd(TBSDYF, empNo,LMBSDYF);
   else
      jobMainRepos.uspL9Monthlylm032Upd(TBSDYF, empNo,LMBSDYF);
  }

  @Override
  public void Usp_L9_YearlyHouseLoanInt_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9YearlyhouseloanintUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9YearlyhouseloanintUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9YearlyhouseloanintUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL9YearlyhouseloanintUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB201_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb201Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb201Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb201Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb201Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB207_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb207Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb207Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb207Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb207Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB080_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb080Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb080Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb080Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb080Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB085_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb085Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb085Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb085Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb085Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB090_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb090Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb090Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb090Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb090Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB092_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb092Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb092Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb092Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb092Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB093_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb093Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb093Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb093Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb093Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB094_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb094Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb094Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb094Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb094Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB095_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb095Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb095Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb095Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb095Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB096_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb096Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb096Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb096Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb096Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L8_JcicB680_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8Jcicb680Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8Jcicb680Upd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8Jcicb680Upd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8Jcicb680Upd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_Ias34Ap_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Ias34apUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Ias34apUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Ias34apUpd(tbsdyf,  empNo,newAcFg);
   else
      jobMainRepos.uspL7Ias34apUpd(tbsdyf,  empNo,newAcFg);
  }

  @Override
  public void Usp_L7_Ias34Bp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Ias34bpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Ias34bpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Ias34bpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Ias34bpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_Ias34Cp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Ias34cpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Ias34cpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Ias34cpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Ias34cpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_Ias34Dp_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Ias34dpUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Ias34dpUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Ias34dpUpd(tbsdyf,  empNo,newAcFg);
   else
      jobMainRepos.uspL7Ias34dpUpd(tbsdyf,  empNo,newAcFg);
  }

  @Override
  public void Usp_L7_Ias34Ep_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Ias34epUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Ias34epUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Ias34epUpd(tbsdyf,  empNo,newAcFg);
   else
      jobMainRepos.uspL7Ias34epUpd(tbsdyf,  empNo,newAcFg);
  }

  @Override
  public void Usp_L7_Ias34Gp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Ias34gpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Ias34gpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Ias34gpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Ias34gpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_LoanIfrsAp_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7LoanifrsapUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7LoanifrsapUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7LoanifrsapUpd(tbsdyf,  empNo,newAcFg);
   else
      jobMainRepos.uspL7LoanifrsapUpd(tbsdyf,  empNo,newAcFg);
  }

  @Override
  public void Usp_L7_LoanIfrsBp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7LoanifrsbpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7LoanifrsbpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7LoanifrsbpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7LoanifrsbpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_LoanIfrsCp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7LoanifrscpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7LoanifrscpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7LoanifrscpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7LoanifrscpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_LoanIfrsDp_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7LoanifrsdpUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7LoanifrsdpUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7LoanifrsdpUpd(tbsdyf,  empNo,newAcFg);
   else
      jobMainRepos.uspL7LoanifrsdpUpd(tbsdyf,  empNo,newAcFg);
  }

  @Override
  public void Usp_L7_LoanIfrsFp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7LoanifrsfpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7LoanifrsfpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7LoanifrsfpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7LoanifrsfpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_LoanIfrsGp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7LoanifrsgpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7LoanifrsgpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7LoanifrsgpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7LoanifrsgpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_LoanIfrsHp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7LoanifrshpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7LoanifrshpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7LoanifrshpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7LoanifrshpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_LoanIfrsIp_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7LoanifrsipUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7LoanifrsipUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7LoanifrsipUpd(tbsdyf,  empNo,newAcFg);
   else
      jobMainRepos.uspL7LoanifrsipUpd(tbsdyf,  empNo,newAcFg);
  }

  @Override
  public void Usp_L7_LoanIfrsJp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7LoanifrsjpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7LoanifrsjpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7LoanifrsjpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7LoanifrsjpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L9_MonthlyLM052AssetClass_Ins(int TYYMM, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9Monthlylm052assetclassIns(TYYMM, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9Monthlylm052assetclassIns(TYYMM, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9Monthlylm052assetclassIns(TYYMM, EmpNo);
   else
      jobMainRepos.uspL9Monthlylm052assetclassIns(TYYMM, EmpNo);
  }

  @Override
  public void Usp_L9_MonthlyLM052LoanAsset_Ins(int TYYMM, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9Monthlylm052loanassetIns(TYYMM, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9Monthlylm052loanassetIns(TYYMM, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9Monthlylm052loanassetIns(TYYMM, EmpNo);
   else
      jobMainRepos.uspL9Monthlylm052loanassetIns(TYYMM, EmpNo);
  }

  @Override
  public void Usp_L9_MonthlyLM052Ovdu_Ins(int TYYMM, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9Monthlylm052ovduIns(TYYMM, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9Monthlylm052ovduIns(TYYMM, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9Monthlylm052ovduIns(TYYMM, EmpNo);
   else
      jobMainRepos.uspL9Monthlylm052ovduIns(TYYMM, EmpNo);
  }

}
