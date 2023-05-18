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
import com.st1.itx.eum.ThreadVariable;

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
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "TxSeq", "ExecDate", "JobCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "TxSeq", "ExecDate", "JobCode"));
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
  public Slice<JobMain> findAllByTxSeq(String txSeq_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JobMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findAllByTxSeq " + dbName + " : " + "txSeq_0 : " + txSeq_0);
    if (dbName.equals(ContentName.onDay))
      slice = jobMainReposDay.findAllByTxSeqIs(txSeq_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jobMainReposMon.findAllByTxSeqIs(txSeq_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jobMainReposHist.findAllByTxSeqIs(txSeq_0, pageable);
    else 
      slice = jobMainRepos.findAllByTxSeqIs(txSeq_0, pageable);

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
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jobMain.getJobMainId());
    if (this.findById(jobMain.getJobMainId(), titaVo) != null)
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
		} else
       empNot = ThreadVariable.getEmpNot();

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
		} else
       empNot = ThreadVariable.getEmpNot();

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
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
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
		} else
       empNot = ThreadVariable.getEmpNot();

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
  public void Usp_Tx_TxHoliday_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspTxTxholidayIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspTxTxholidayIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspTxTxholidayIns(EmpNo);
   else
      jobMainRepos.uspTxTxholidayIns(EmpNo);
  }

  @Override
  public void Usp_L6_CdEmp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL6CdempIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL6CdempIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL6CdempIns(EmpNo);
   else
      jobMainRepos.uspL6CdempIns(EmpNo);
  }

  @Override
  public void Usp_L6_CdBcm_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL6CdbcmIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL6CdbcmIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL6CdbcmIns(EmpNo);
   else
      jobMainRepos.uspL6CdbcmIns(EmpNo);
  }

  @Override
  public void Usp_L6_QuitEmp_Ins(String InputEmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL6QuitempIns(InputEmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL6QuitempIns(InputEmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL6QuitempIns(InputEmpNo);
   else
      jobMainRepos.uspL6QuitempIns(InputEmpNo);
  }

  @Override
  public void Usp_L2_CustDataCtrl_Ins(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL2CustdatactrlIns(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL2CustdatactrlIns(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL2CustdatactrlIns(tbsdyf,  empNo);
   else
      jobMainRepos.uspL2CustdatactrlIns(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L5_CollList_Upd(int tbsdyf,  String empNo,String txtNo, int l6bsdyf, int l7bsdyf, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL5ColllistUpd(tbsdyf,  empNo,txtNo, l6bsdyf, l7bsdyf);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL5ColllistUpd(tbsdyf,  empNo,txtNo, l6bsdyf, l7bsdyf);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL5ColllistUpd(tbsdyf,  empNo,txtNo, l6bsdyf, l7bsdyf);
   else
      jobMainRepos.uspL5ColllistUpd(tbsdyf,  empNo,txtNo, l6bsdyf, l7bsdyf);
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
  public void Usp_L9_DailyTav_Ins(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9DailytavIns(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9DailytavIns(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9DailytavIns(tbsdyf,  empNo);
   else
      jobMainRepos.uspL9DailytavIns(tbsdyf,  empNo);
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
  public void Usp_L2_ForeclosureFinished_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL2ForeclosurefinishedUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL2ForeclosurefinishedUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL2ForeclosurefinishedUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL2ForeclosurefinishedUpd(tbsdyf,  empNo);
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
  public void Usp_L9_YearlyHouseLoanInt_Upd(int tbsdyf,  String empNo,int StartMonth,int EndMonth,int CustNo,String AcctCode, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9YearlyhouseloanintUpd(tbsdyf,  empNo,StartMonth,EndMonth,CustNo,AcctCode);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9YearlyhouseloanintUpd(tbsdyf,  empNo,StartMonth,EndMonth,CustNo,AcctCode);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9YearlyhouseloanintUpd(tbsdyf,  empNo,StartMonth,EndMonth,CustNo,AcctCode);
   else
      jobMainRepos.uspL9YearlyhouseloanintUpd(tbsdyf,  empNo,StartMonth,EndMonth,CustNo,AcctCode);
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
  public void Usp_L8_JcicRel_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL8JcicrelUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL8JcicrelUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL8JcicrelUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL8JcicrelUpd(tbsdyf,  empNo);
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
  public void Usp_L7_LoanIfrs9Ap_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Loanifrs9apUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Loanifrs9apUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Loanifrs9apUpd(tbsdyf,  empNo,newAcFg);
   else
      jobMainRepos.uspL7Loanifrs9apUpd(tbsdyf,  empNo,newAcFg);
  }

  @Override
  public void Usp_L7_LoanIfrs9Bp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Loanifrs9bpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Loanifrs9bpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Loanifrs9bpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Loanifrs9bpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_LoanIfrs9Cp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Loanifrs9cpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Loanifrs9cpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Loanifrs9cpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Loanifrs9cpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_LoanIfrs9Dp_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Loanifrs9dpUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Loanifrs9dpUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Loanifrs9dpUpd(tbsdyf,  empNo,newAcFg);
   else
      jobMainRepos.uspL7Loanifrs9dpUpd(tbsdyf,  empNo,newAcFg);
  }

  @Override
  public void Usp_L7_LoanIfrs9Fp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Loanifrs9fpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Loanifrs9fpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Loanifrs9fpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Loanifrs9fpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_LoanIfrs9Gp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Loanifrs9gpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Loanifrs9gpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Loanifrs9gpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Loanifrs9gpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_LoanIfrs9Hp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Loanifrs9hpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Loanifrs9hpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Loanifrs9hpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Loanifrs9hpUpd(tbsdyf,  empNo);
  }

  @Override
  public void Usp_L7_LoanIfrs9Ip_Upd(int tbsdyf,  String empNo,int newAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Loanifrs9ipUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Loanifrs9ipUpd(tbsdyf,  empNo,newAcFg);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Loanifrs9ipUpd(tbsdyf,  empNo,newAcFg);
   else
      jobMainRepos.uspL7Loanifrs9ipUpd(tbsdyf,  empNo,newAcFg);
  }

  @Override
  public void Usp_L7_LoanIfrs9Jp_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL7Loanifrs9jpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL7Loanifrs9jpUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL7Loanifrs9jpUpd(tbsdyf,  empNo);
   else
      jobMainRepos.uspL7Loanifrs9jpUpd(tbsdyf,  empNo);
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

  @Override
  public void Usp_L9_YearlyHouseLoanIntCheck_Upd(int tbsdyf,  String empNo,int YYYYMM,int StartMonth,int EndMonth,int CustNo,String AcctCode, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspL9YearlyhouseloanintcheckUpd(tbsdyf,  empNo,YYYYMM,StartMonth,EndMonth,CustNo,AcctCode);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspL9YearlyhouseloanintcheckUpd(tbsdyf,  empNo,YYYYMM,StartMonth,EndMonth,CustNo,AcctCode);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspL9YearlyhouseloanintcheckUpd(tbsdyf,  empNo,YYYYMM,StartMonth,EndMonth,CustNo,AcctCode);
   else
      jobMainRepos.uspL9YearlyhouseloanintcheckUpd(tbsdyf,  empNo,YYYYMM,StartMonth,EndMonth,CustNo,AcctCode);
  }

  @Override
  public void Usp_Cp_ForeignKeyControl_Upd(int TBSDYF, String empNo,int Switch, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpForeignkeycontrolUpd(TBSDYF, empNo,Switch);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpForeignkeycontrolUpd(TBSDYF, empNo,Switch);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpForeignkeycontrolUpd(TBSDYF, empNo,Switch);
   else
      jobMainRepos.uspCpForeignkeycontrolUpd(TBSDYF, empNo,Switch);
  }

  @Override
  public void Usp_Cp_AcAcctCheck_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAcacctcheckIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAcacctcheckIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAcacctcheckIns(EmpNo);
   else
      jobMainRepos.uspCpAcacctcheckIns(EmpNo);
  }

  @Override
  public void Usp_Cp_AcAcctCheckDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAcacctcheckdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAcacctcheckdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAcacctcheckdetailIns(EmpNo);
   else
      jobMainRepos.uspCpAcacctcheckdetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_AcClose_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAccloseIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAccloseIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAccloseIns(EmpNo);
   else
      jobMainRepos.uspCpAccloseIns(EmpNo);
  }

  @Override
  public void Usp_Cp_AcDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAcdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAcdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAcdetailIns(EmpNo);
   else
      jobMainRepos.uspCpAcdetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_AchAuthLog_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAchauthlogIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAchauthlogIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAchauthlogIns(EmpNo);
   else
      jobMainRepos.uspCpAchauthlogIns(EmpNo);
  }

  @Override
  public void Usp_Cp_AchAuthLogHistory_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAchauthloghistoryIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAchauthloghistoryIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAchauthloghistoryIns(EmpNo);
   else
      jobMainRepos.uspCpAchauthloghistoryIns(EmpNo);
  }

  @Override
  public void Usp_Cp_AchDeductMedia_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAchdeductmediaIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAchdeductmediaIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAchdeductmediaIns(EmpNo);
   else
      jobMainRepos.uspCpAchdeductmediaIns(EmpNo);
  }

  @Override
  public void Usp_Cp_AcLoanInt_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAcloanintIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAcloanintIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAcloanintIns(EmpNo);
   else
      jobMainRepos.uspCpAcloanintIns(EmpNo);
  }

  @Override
  public void Usp_Cp_AcLoanRenew_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAcloanrenewIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAcloanrenewIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAcloanrenewIns(EmpNo);
   else
      jobMainRepos.uspCpAcloanrenewIns(EmpNo);
  }

  @Override
  public void Usp_Cp_AcMain_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAcmainIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAcmainIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAcmainIns(EmpNo);
   else
      jobMainRepos.uspCpAcmainIns(EmpNo);
  }

  @Override
  public void Usp_Cp_AcReceivable_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAcreceivableIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAcreceivableIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAcreceivableIns(EmpNo);
   else
      jobMainRepos.uspCpAcreceivableIns(EmpNo);
  }

  @Override
  public void Usp_Cp_AmlCustList_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpAmlcustlistIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpAmlcustlistIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpAmlcustlistIns(EmpNo);
   else
      jobMainRepos.uspCpAmlcustlistIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BankAuthAct_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBankauthactIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBankauthactIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBankauthactIns(EmpNo);
   else
      jobMainRepos.uspCpBankauthactIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BankDeductDtl_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBankdeductdtlIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBankdeductdtlIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBankdeductdtlIns(EmpNo);
   else
      jobMainRepos.uspCpBankdeductdtlIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BankRelationCompany_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBankrelationcompanyIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBankrelationcompanyIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBankrelationcompanyIns(EmpNo);
   else
      jobMainRepos.uspCpBankrelationcompanyIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BankRelationFamily_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBankrelationfamilyIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBankrelationfamilyIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBankrelationfamilyIns(EmpNo);
   else
      jobMainRepos.uspCpBankrelationfamilyIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BankRelationSelf_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBankrelationselfIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBankrelationselfIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBankrelationselfIns(EmpNo);
   else
      jobMainRepos.uspCpBankrelationselfIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BankRelationSuspected_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBankrelationsuspectedIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBankrelationsuspectedIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBankrelationsuspectedIns(EmpNo);
   else
      jobMainRepos.uspCpBankrelationsuspectedIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BankRemit_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBankremitIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBankremitIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBankremitIns(EmpNo);
   else
      jobMainRepos.uspCpBankremitIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BankRmtf_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBankrmtfIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBankrmtfIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBankrmtfIns(EmpNo);
   else
      jobMainRepos.uspCpBankrmtfIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BatxCheque_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBatxchequeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBatxchequeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBatxchequeIns(EmpNo);
   else
      jobMainRepos.uspCpBatxchequeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BatxDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBatxdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBatxdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBatxdetailIns(EmpNo);
   else
      jobMainRepos.uspCpBatxdetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BatxHead_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBatxheadIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBatxheadIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBatxheadIns(EmpNo);
   else
      jobMainRepos.uspCpBatxheadIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BatxOthers_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBatxothersIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBatxothersIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBatxothersIns(EmpNo);
   else
      jobMainRepos.uspCpBatxothersIns(EmpNo);
  }

  @Override
  public void Usp_Cp_BatxRateChange_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpBatxratechangeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpBatxratechangeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpBatxratechangeIns(EmpNo);
   else
      jobMainRepos.uspCpBatxratechangeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdAcBook_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdacbookIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdacbookIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdacbookIns(EmpNo);
   else
      jobMainRepos.uspCpCdacbookIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdAcCode_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdaccodeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdaccodeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdaccodeIns(EmpNo);
   else
      jobMainRepos.uspCpCdaccodeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdAoDept_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdaodeptIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdaodeptIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdaodeptIns(EmpNo);
   else
      jobMainRepos.uspCpCdaodeptIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdAppraisalCompany_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdappraisalcompanyIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdappraisalcompanyIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdappraisalcompanyIns(EmpNo);
   else
      jobMainRepos.uspCpCdappraisalcompanyIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdAppraiser_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdappraiserIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdappraiserIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdappraiserIns(EmpNo);
   else
      jobMainRepos.uspCpCdappraiserIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdArea_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdareaIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdareaIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdareaIns(EmpNo);
   else
      jobMainRepos.uspCpCdareaIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdBank_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdbankIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdbankIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdbankIns(EmpNo);
   else
      jobMainRepos.uspCpCdbankIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdBankOld_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdbankoldIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdbankoldIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdbankoldIns(EmpNo);
   else
      jobMainRepos.uspCpCdbankoldIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdBaseRate_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdbaserateIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdbaserateIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdbaserateIns(EmpNo);
   else
      jobMainRepos.uspCpCdbaserateIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdBcm_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdbcmIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdbcmIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdbcmIns(EmpNo);
   else
      jobMainRepos.uspCpCdbcmIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdBonus_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdbonusIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdbonusIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdbonusIns(EmpNo);
   else
      jobMainRepos.uspCpCdbonusIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdBonusCo_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdbonuscoIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdbonuscoIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdbonuscoIns(EmpNo);
   else
      jobMainRepos.uspCpCdbonuscoIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdBranch_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdbranchIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdbranchIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdbranchIns(EmpNo);
   else
      jobMainRepos.uspCpCdbranchIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdBranchGroup_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdbranchgroupIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdbranchgroupIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdbranchgroupIns(EmpNo);
   else
      jobMainRepos.uspCpCdbranchgroupIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdBudget_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdbudgetIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdbudgetIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdbudgetIns(EmpNo);
   else
      jobMainRepos.uspCpCdbudgetIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdBuildingCost_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdbuildingcostIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdbuildingcostIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdbuildingcostIns(EmpNo);
   else
      jobMainRepos.uspCpCdbuildingcostIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdCashFlow_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdcashflowIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdcashflowIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdcashflowIns(EmpNo);
   else
      jobMainRepos.uspCpCdcashflowIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdCity_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdcityIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdcityIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdcityIns(EmpNo);
   else
      jobMainRepos.uspCpCdcityIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdCl_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdclIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdclIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdclIns(EmpNo);
   else
      jobMainRepos.uspCpCdclIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdCode_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdcodeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdcodeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdcodeIns(EmpNo);
   else
      jobMainRepos.uspCpCdcodeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdEmp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdempIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdempIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdempIns(EmpNo);
   else
      jobMainRepos.uspCpCdempIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdGseq_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdgseqIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdgseqIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdgseqIns(EmpNo);
   else
      jobMainRepos.uspCpCdgseqIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdGuarantor_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdguarantorIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdguarantorIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdguarantorIns(EmpNo);
   else
      jobMainRepos.uspCpCdguarantorIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdIndustry_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdindustryIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdindustryIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdindustryIns(EmpNo);
   else
      jobMainRepos.uspCpCdindustryIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdInsurer_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdinsurerIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdinsurerIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdinsurerIns(EmpNo);
   else
      jobMainRepos.uspCpCdinsurerIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdLandOffice_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdlandofficeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdlandofficeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdlandofficeIns(EmpNo);
   else
      jobMainRepos.uspCpCdlandofficeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdLandSection_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdlandsectionIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdlandsectionIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdlandsectionIns(EmpNo);
   else
      jobMainRepos.uspCpCdlandsectionIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdLoanNotYet_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdloannotyetIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdloannotyetIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdloannotyetIns(EmpNo);
   else
      jobMainRepos.uspCpCdloannotyetIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdOverdue_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdoverdueIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdoverdueIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdoverdueIns(EmpNo);
   else
      jobMainRepos.uspCpCdoverdueIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdPerformance_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdperformanceIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdperformanceIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdperformanceIns(EmpNo);
   else
      jobMainRepos.uspCpCdperformanceIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdPfParms_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdpfparmsIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdpfparmsIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdpfparmsIns(EmpNo);
   else
      jobMainRepos.uspCpCdpfparmsIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdReport_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdreportIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdreportIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdreportIns(EmpNo);
   else
      jobMainRepos.uspCpCdreportIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdStock_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdstockIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdstockIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdstockIns(EmpNo);
   else
      jobMainRepos.uspCpCdstockIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdSupv_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdsupvIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdsupvIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdsupvIns(EmpNo);
   else
      jobMainRepos.uspCpCdsupvIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdSyndFee_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdsyndfeeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdsyndfeeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdsyndfeeIns(EmpNo);
   else
      jobMainRepos.uspCpCdsyndfeeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdVarValue_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdvarvalueIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdvarvalueIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdvarvalueIns(EmpNo);
   else
      jobMainRepos.uspCpCdvarvalueIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CdWorkMonth_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCdworkmonthIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCdworkmonthIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCdworkmonthIns(EmpNo);
   else
      jobMainRepos.uspCpCdworkmonthIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClBuilding_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClbuildingIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClbuildingIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClbuildingIns(EmpNo);
   else
      jobMainRepos.uspCpClbuildingIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClBuildingOwner_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClbuildingownerIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClbuildingownerIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClbuildingownerIns(EmpNo);
   else
      jobMainRepos.uspCpClbuildingownerIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClBuildingPublic_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClbuildingpublicIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClbuildingpublicIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClbuildingpublicIns(EmpNo);
   else
      jobMainRepos.uspCpClbuildingpublicIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClBuildingReason_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClbuildingreasonIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClbuildingreasonIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClbuildingreasonIns(EmpNo);
   else
      jobMainRepos.uspCpClbuildingreasonIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClEva_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClevaIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClevaIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClevaIns(EmpNo);
   else
      jobMainRepos.uspCpClevaIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClFac_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClfacIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClfacIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClfacIns(EmpNo);
   else
      jobMainRepos.uspCpClfacIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClImm_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClimmIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClimmIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClimmIns(EmpNo);
   else
      jobMainRepos.uspCpClimmIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClImmRankDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClimmrankdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClimmrankdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClimmrankdetailIns(EmpNo);
   else
      jobMainRepos.uspCpClimmrankdetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClLand_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCllandIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCllandIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCllandIns(EmpNo);
   else
      jobMainRepos.uspCpCllandIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClLandOwner_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCllandownerIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCllandownerIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCllandownerIns(EmpNo);
   else
      jobMainRepos.uspCpCllandownerIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClLandReason_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCllandreasonIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCllandreasonIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCllandreasonIns(EmpNo);
   else
      jobMainRepos.uspCpCllandreasonIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClMain_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClmainIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClmainIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClmainIns(EmpNo);
   else
      jobMainRepos.uspCpClmainIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClMovables_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClmovablesIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClmovablesIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClmovablesIns(EmpNo);
   else
      jobMainRepos.uspCpClmovablesIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClNoMap_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClnomapIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClnomapIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClnomapIns(EmpNo);
   else
      jobMainRepos.uspCpClnomapIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClOther_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClotherIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClotherIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClotherIns(EmpNo);
   else
      jobMainRepos.uspCpClotherIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClOtherRights_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClotherrightsIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClotherrightsIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClotherrightsIns(EmpNo);
   else
      jobMainRepos.uspCpClotherrightsIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClOwnerRelation_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClownerrelationIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClownerrelationIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClownerrelationIns(EmpNo);
   else
      jobMainRepos.uspCpClownerrelationIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClParking_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClparkingIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClparkingIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClparkingIns(EmpNo);
   else
      jobMainRepos.uspCpClparkingIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClParkingType_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClparkingtypeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClparkingtypeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClparkingtypeIns(EmpNo);
   else
      jobMainRepos.uspCpClparkingtypeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ClStock_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpClstockIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpClstockIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpClstockIns(EmpNo);
   else
      jobMainRepos.uspCpClstockIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CollLaw_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpColllawIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpColllawIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpColllawIns(EmpNo);
   else
      jobMainRepos.uspCpColllawIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CollLetter_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCollletterIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCollletterIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCollletterIns(EmpNo);
   else
      jobMainRepos.uspCpCollletterIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CollList_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpColllistIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpColllistIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpColllistIns(EmpNo);
   else
      jobMainRepos.uspCpColllistIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CollListTmp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpColllisttmpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpColllisttmpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpColllisttmpIns(EmpNo);
   else
      jobMainRepos.uspCpColllisttmpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CollMeet_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCollmeetIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCollmeetIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCollmeetIns(EmpNo);
   else
      jobMainRepos.uspCpCollmeetIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CollRemind_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCollremindIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCollremindIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCollremindIns(EmpNo);
   else
      jobMainRepos.uspCpCollremindIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CollTel_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpColltelIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpColltelIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpColltelIns(EmpNo);
   else
      jobMainRepos.uspCpColltelIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CreditRating_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCreditratingIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCreditratingIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCreditratingIns(EmpNo);
   else
      jobMainRepos.uspCpCreditratingIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CustCross_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCustcrossIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCustcrossIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCustcrossIns(EmpNo);
   else
      jobMainRepos.uspCpCustcrossIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CustDataCtrl_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCustdatactrlIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCustdatactrlIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCustdatactrlIns(EmpNo);
   else
      jobMainRepos.uspCpCustdatactrlIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CustFin_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCustfinIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCustfinIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCustfinIns(EmpNo);
   else
      jobMainRepos.uspCpCustfinIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CustMain_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCustmainIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCustmainIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCustmainIns(EmpNo);
   else
      jobMainRepos.uspCpCustmainIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CustNotice_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCustnoticeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCustnoticeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCustnoticeIns(EmpNo);
   else
      jobMainRepos.uspCpCustnoticeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CustomerAmlRating_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCustomeramlratingIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCustomeramlratingIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCustomeramlratingIns(EmpNo);
   else
      jobMainRepos.uspCpCustomeramlratingIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CustRmk_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCustrmkIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCustrmkIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCustrmkIns(EmpNo);
   else
      jobMainRepos.uspCpCustrmkIns(EmpNo);
  }

  @Override
  public void Usp_Cp_CustTelNo_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpCusttelnoIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpCusttelnoIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpCusttelnoIns(EmpNo);
   else
      jobMainRepos.uspCpCusttelnoIns(EmpNo);
  }

  @Override
  public void Usp_Cp_DailyLoanBal_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpDailyloanbalIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpDailyloanbalIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpDailyloanbalIns(EmpNo);
   else
      jobMainRepos.uspCpDailyloanbalIns(EmpNo);
  }

  @Override
  public void Usp_Cp_EmpDeductDtl_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpEmpdeductdtlIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpEmpdeductdtlIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpEmpdeductdtlIns(EmpNo);
   else
      jobMainRepos.uspCpEmpdeductdtlIns(EmpNo);
  }

  @Override
  public void Usp_Cp_EmpDeductMedia_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpEmpdeductmediaIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpEmpdeductmediaIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpEmpdeductmediaIns(EmpNo);
   else
      jobMainRepos.uspCpEmpdeductmediaIns(EmpNo);
  }

  @Override
  public void Usp_Cp_EmpDeductSchedule_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpEmpdeductscheduleIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpEmpdeductscheduleIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpEmpdeductscheduleIns(EmpNo);
   else
      jobMainRepos.uspCpEmpdeductscheduleIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FacCaseAppl_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFaccaseapplIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFaccaseapplIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFaccaseapplIns(EmpNo);
   else
      jobMainRepos.uspCpFaccaseapplIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FacClose_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFaccloseIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFaccloseIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFaccloseIns(EmpNo);
   else
      jobMainRepos.uspCpFaccloseIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FacMain_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFacmainIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFacmainIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFacmainIns(EmpNo);
   else
      jobMainRepos.uspCpFacmainIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FacProd_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFacprodIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFacprodIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFacprodIns(EmpNo);
   else
      jobMainRepos.uspCpFacprodIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FacProdAcctFee_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFacprodacctfeeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFacprodacctfeeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFacprodacctfeeIns(EmpNo);
   else
      jobMainRepos.uspCpFacprodacctfeeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FacProdPremium_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFacprodpremiumIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFacprodpremiumIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFacprodpremiumIns(EmpNo);
   else
      jobMainRepos.uspCpFacprodpremiumIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FacProdStepRate_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFacprodsteprateIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFacprodsteprateIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFacprodsteprateIns(EmpNo);
   else
      jobMainRepos.uspCpFacprodsteprateIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FacRelation_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFacrelationIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFacrelationIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFacrelationIns(EmpNo);
   else
      jobMainRepos.uspCpFacrelationIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FacShareAppl_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFacshareapplIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFacshareapplIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFacshareapplIns(EmpNo);
   else
      jobMainRepos.uspCpFacshareapplIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FacShareLimit_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFacsharelimitIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFacsharelimitIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFacsharelimitIns(EmpNo);
   else
      jobMainRepos.uspCpFacsharelimitIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FacShareRelation_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFacsharerelationIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFacsharerelationIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFacsharerelationIns(EmpNo);
   else
      jobMainRepos.uspCpFacsharerelationIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FinReportCashFlow_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFinreportcashflowIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFinreportcashflowIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFinreportcashflowIns(EmpNo);
   else
      jobMainRepos.uspCpFinreportcashflowIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FinReportDebt_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFinreportdebtIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFinreportdebtIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFinreportdebtIns(EmpNo);
   else
      jobMainRepos.uspCpFinreportdebtIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FinReportProfit_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFinreportprofitIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFinreportprofitIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFinreportprofitIns(EmpNo);
   else
      jobMainRepos.uspCpFinreportprofitIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FinReportQuality_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFinreportqualityIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFinreportqualityIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFinreportqualityIns(EmpNo);
   else
      jobMainRepos.uspCpFinreportqualityIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FinReportRate_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFinreportrateIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFinreportrateIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFinreportrateIns(EmpNo);
   else
      jobMainRepos.uspCpFinreportrateIns(EmpNo);
  }

  @Override
  public void Usp_Cp_FinReportReview_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpFinreportreviewIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpFinreportreviewIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpFinreportreviewIns(EmpNo);
   else
      jobMainRepos.uspCpFinreportreviewIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ForeclosureFee_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpForeclosurefeeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpForeclosurefeeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpForeclosurefeeIns(EmpNo);
   else
      jobMainRepos.uspCpForeclosurefeeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ForeclosureFinished_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpForeclosurefinishedIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpForeclosurefinishedIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpForeclosurefinishedIns(EmpNo);
   else
      jobMainRepos.uspCpForeclosurefinishedIns(EmpNo);
  }

  @Override
  public void Usp_Cp_GraceCondition_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpGraceconditionIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpGraceconditionIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpGraceconditionIns(EmpNo);
   else
      jobMainRepos.uspCpGraceconditionIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Guarantor_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpGuarantorIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpGuarantorIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpGuarantorIns(EmpNo);
   else
      jobMainRepos.uspCpGuarantorIns(EmpNo);
  }

  @Override
  public void Usp_Cp_GuildBuilders_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpGuildbuildersIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpGuildbuildersIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpGuildbuildersIns(EmpNo);
   else
      jobMainRepos.uspCpGuildbuildersIns(EmpNo);
  }

  @Override
  public void Usp_Cp_HlAreaData_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpHlareadataIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpHlareadataIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpHlareadataIns(EmpNo);
   else
      jobMainRepos.uspCpHlareadataIns(EmpNo);
  }

  @Override
  public void Usp_Cp_HlAreaLnYg6Pt_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpHlarealnyg6ptIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpHlarealnyg6ptIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpHlarealnyg6ptIns(EmpNo);
   else
      jobMainRepos.uspCpHlarealnyg6ptIns(EmpNo);
  }

  @Override
  public void Usp_Cp_HlCusData_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpHlcusdataIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpHlcusdataIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpHlcusdataIns(EmpNo);
   else
      jobMainRepos.uspCpHlcusdataIns(EmpNo);
  }

  @Override
  public void Usp_Cp_HlEmpLnYg5Pt_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpHlemplnyg5ptIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpHlemplnyg5ptIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpHlemplnyg5ptIns(EmpNo);
   else
      jobMainRepos.uspCpHlemplnyg5ptIns(EmpNo);
  }

  @Override
  public void Usp_Cp_HlThreeDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpHlthreedetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpHlthreedetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpHlthreedetailIns(EmpNo);
   else
      jobMainRepos.uspCpHlthreedetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_HlThreeLaqhcp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpHlthreelaqhcpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpHlthreelaqhcpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpHlthreelaqhcpIns(EmpNo);
   else
      jobMainRepos.uspCpHlthreelaqhcpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ias34Ap_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIas34apIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIas34apIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIas34apIns(EmpNo);
   else
      jobMainRepos.uspCpIas34apIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ias34Bp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIas34bpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIas34bpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIas34bpIns(EmpNo);
   else
      jobMainRepos.uspCpIas34bpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ias34Cp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIas34cpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIas34cpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIas34cpIns(EmpNo);
   else
      jobMainRepos.uspCpIas34cpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ias34Dp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIas34dpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIas34dpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIas34dpIns(EmpNo);
   else
      jobMainRepos.uspCpIas34dpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ias34Ep_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIas34epIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIas34epIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIas34epIns(EmpNo);
   else
      jobMainRepos.uspCpIas34epIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ias34Gp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIas34gpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIas34gpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIas34gpIns(EmpNo);
   else
      jobMainRepos.uspCpIas34gpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ias39IntMethod_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIas39intmethodIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIas39intmethodIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIas39intmethodIns(EmpNo);
   else
      jobMainRepos.uspCpIas39intmethodIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ias39LGD_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIas39lgdIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIas39lgdIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIas39lgdIns(EmpNo);
   else
      jobMainRepos.uspCpIas39lgdIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ias39Loan34Data_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIas39loan34dataIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIas39loan34dataIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIas39loan34dataIns(EmpNo);
   else
      jobMainRepos.uspCpIas39loan34dataIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ias39LoanCommit_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIas39loancommitIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIas39loancommitIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIas39loancommitIns(EmpNo);
   else
      jobMainRepos.uspCpIas39loancommitIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ias39Loss_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIas39lossIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIas39lossIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIas39lossIns(EmpNo);
   else
      jobMainRepos.uspCpIas39lossIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ifrs9FacData_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIfrs9facdataIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIfrs9facdataIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIfrs9facdataIns(EmpNo);
   else
      jobMainRepos.uspCpIfrs9facdataIns(EmpNo);
  }

  @Override
  public void Usp_Cp_Ifrs9LoanData_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpIfrs9loandataIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpIfrs9loandataIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpIfrs9loandataIns(EmpNo);
   else
      jobMainRepos.uspCpIfrs9loandataIns(EmpNo);
  }

  @Override
  public void Usp_Cp_InnDocRecord_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpInndocrecordIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpInndocrecordIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpInndocrecordIns(EmpNo);
   else
      jobMainRepos.uspCpInndocrecordIns(EmpNo);
  }

  @Override
  public void Usp_Cp_InnFundApl_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpInnfundaplIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpInnfundaplIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpInnfundaplIns(EmpNo);
   else
      jobMainRepos.uspCpInnfundaplIns(EmpNo);
  }

  @Override
  public void Usp_Cp_InnLoanMeeting_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpInnloanmeetingIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpInnloanmeetingIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpInnloanmeetingIns(EmpNo);
   else
      jobMainRepos.uspCpInnloanmeetingIns(EmpNo);
  }

  @Override
  public void Usp_Cp_InnReCheck_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpInnrecheckIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpInnrecheckIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpInnrecheckIns(EmpNo);
   else
      jobMainRepos.uspCpInnrecheckIns(EmpNo);
  }

  @Override
  public void Usp_Cp_InsuComm_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpInsucommIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpInsucommIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpInsucommIns(EmpNo);
   else
      jobMainRepos.uspCpInsucommIns(EmpNo);
  }

  @Override
  public void Usp_Cp_InsuOrignal_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpInsuorignalIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpInsuorignalIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpInsuorignalIns(EmpNo);
   else
      jobMainRepos.uspCpInsuorignalIns(EmpNo);
  }

  @Override
  public void Usp_Cp_InsuRenew_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpInsurenewIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpInsurenewIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpInsurenewIns(EmpNo);
   else
      jobMainRepos.uspCpInsurenewIns(EmpNo);
  }

  @Override
  public void Usp_Cp_InsuRenewMediaTemp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpInsurenewmediatempIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpInsurenewmediatempIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpInsurenewmediatempIns(EmpNo);
   else
      jobMainRepos.uspCpInsurenewmediatempIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicAtomDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicatomdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicatomdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicatomdetailIns(EmpNo);
   else
      jobMainRepos.uspCpJcicatomdetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicAtomMain_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicatommainIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicatommainIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicatommainIns(EmpNo);
   else
      jobMainRepos.uspCpJcicatommainIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB080_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb080Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb080Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb080Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb080Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB085_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb085Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb085Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb085Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb085Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB090_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb090Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb090Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb090Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb090Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB091_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb091Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb091Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb091Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb091Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB092_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb092Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb092Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb092Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb092Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB093_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb093Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb093Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb093Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb093Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB094_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb094Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb094Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb094Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb094Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB095_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb095Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb095Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb095Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb095Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB096_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb096Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb096Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb096Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb096Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB201_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb201Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb201Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb201Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb201Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB204_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb204Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb204Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb204Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb204Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB207_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb207Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb207Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb207Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb207Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB211_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb211Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb211Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb211Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb211Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicB680_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicb680Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicb680Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicb680Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicb680Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicMonthlyLoanData_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicmonthlyloandataIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicmonthlyloandataIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicmonthlyloandataIns(EmpNo);
   else
      jobMainRepos.uspCpJcicmonthlyloandataIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicRel_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicrelIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicrelIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicrelIns(EmpNo);
   else
      jobMainRepos.uspCpJcicrelIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ040_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz040Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz040Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz040Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz040Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ040Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz040logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz040logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz040logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz040logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ041_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz041Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz041Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz041Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz041Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ041Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz041logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz041logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz041logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz041logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ042_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz042Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz042Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz042Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz042Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ042Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz042logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz042logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz042logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz042logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ043_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz043Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz043Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz043Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz043Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ043Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz043logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz043logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz043logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz043logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ044_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz044Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz044Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz044Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz044Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ044Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz044logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz044logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz044logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz044logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ045_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz045Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz045Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz045Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz045Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ045Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz045logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz045logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz045logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz045logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ046_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz046Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz046Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz046Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz046Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ046Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz046logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz046logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz046logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz046logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ047_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz047Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz047Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz047Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz047Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ047Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz047logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz047logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz047logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz047logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ048_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz048Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz048Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz048Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz048Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ048Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz048logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz048logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz048logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz048logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ049_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz049Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz049Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz049Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz049Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ049Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz049logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz049logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz049logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz049logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ050_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz050Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz050Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz050Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz050Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ050Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz050logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz050logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz050logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz050logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ051_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz051Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz051Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz051Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz051Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ051Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz051logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz051logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz051logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz051logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ052_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz052Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz052Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz052Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz052Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ052Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz052logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz052logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz052logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz052logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ053_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz053Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz053Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz053Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz053Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ053Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz053logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz053logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz053logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz053logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ054_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz054Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz054Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz054Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz054Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ054Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz054logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz054logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz054logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz054logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ055_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz055Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz055Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz055Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz055Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ055Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz055logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz055logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz055logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz055logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ056_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz056Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz056Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz056Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz056Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ056Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz056logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz056logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz056logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz056logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ060_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz060Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz060Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz060Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz060Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ060Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz060logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz060logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz060logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz060logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ061_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz061Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz061Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz061Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz061Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ061Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz061logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz061logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz061logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz061logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ062_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz062Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz062Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz062Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz062Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ062Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz062logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz062logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz062logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz062logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ063_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz063Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz063Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz063Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz063Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ063Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz063logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz063logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz063logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz063logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ440_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz440Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz440Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz440Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz440Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ440Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz440logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz440logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz440logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz440logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ442_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz442Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz442Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz442Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz442Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ442Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz442logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz442logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz442logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz442logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ443_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz443Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz443Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz443Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz443Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ443Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz443logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz443logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz443logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz443logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ444_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz444Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz444Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz444Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz444Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ444Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz444logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz444logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz444logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz444logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ446_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz446Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz446Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz446Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz446Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ446Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz446logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz446logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz446logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz446logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ447_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz447Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz447Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz447Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz447Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ447Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz447logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz447logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz447logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz447logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ448_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz448Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz448Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz448Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz448Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ448Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz448logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz448logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz448logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz448logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ450_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz450Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz450Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz450Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz450Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ450Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz450logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz450logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz450logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz450logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ451_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz451Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz451Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz451Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz451Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ451Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz451logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz451logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz451logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz451logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ454_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz454Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz454Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz454Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz454Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ454Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz454logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz454logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz454logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz454logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ570_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz570Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz570Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz570Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz570Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ570Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz570logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz570logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz570logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz570logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ571_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz571Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz571Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz571Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz571Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ571Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz571logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz571logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz571logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz571logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ572_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz572Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz572Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz572Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz572Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ572Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz572logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz572logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz572logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz572logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ573_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz573Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz573Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz573Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz573Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ573Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz573logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz573logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz573logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz573logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ574_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz574Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz574Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz574Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz574Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ574Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz574logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz574logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz574logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz574logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ575_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz575Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz575Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz575Ins(EmpNo);
   else
      jobMainRepos.uspCpJcicz575Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_JcicZ575Log_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJcicz575logIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJcicz575logIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJcicz575logIns(EmpNo);
   else
      jobMainRepos.uspCpJcicz575logIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JobDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJobdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJobdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJobdetailIns(EmpNo);
   else
      jobMainRepos.uspCpJobdetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_JobMain_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpJobmainIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpJobmainIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpJobmainIns(EmpNo);
   else
      jobMainRepos.uspCpJobmainIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanBook_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanbookIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanbookIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanbookIns(EmpNo);
   else
      jobMainRepos.uspCpLoanbookIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanBorMain_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanbormainIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanbormainIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanbormainIns(EmpNo);
   else
      jobMainRepos.uspCpLoanbormainIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanBorTx_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanbortxIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanbortxIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanbortxIns(EmpNo);
   else
      jobMainRepos.uspCpLoanbortxIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanCheque_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanchequeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanchequeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanchequeIns(EmpNo);
   else
      jobMainRepos.uspCpLoanchequeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanCustRmk_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoancustrmkIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoancustrmkIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoancustrmkIns(EmpNo);
   else
      jobMainRepos.uspCpLoancustrmkIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanIfrs9Ap_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanifrs9apIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanifrs9apIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanifrs9apIns(EmpNo);
   else
      jobMainRepos.uspCpLoanifrs9apIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanIfrs9Bp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanifrs9bpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanifrs9bpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanifrs9bpIns(EmpNo);
   else
      jobMainRepos.uspCpLoanifrs9bpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanIfrs9Cp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanifrs9cpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanifrs9cpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanifrs9cpIns(EmpNo);
   else
      jobMainRepos.uspCpLoanifrs9cpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanIfrs9Dp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanifrs9dpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanifrs9dpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanifrs9dpIns(EmpNo);
   else
      jobMainRepos.uspCpLoanifrs9dpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanIfrs9Fp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanifrs9fpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanifrs9fpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanifrs9fpIns(EmpNo);
   else
      jobMainRepos.uspCpLoanifrs9fpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanIfrs9Gp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanifrs9gpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanifrs9gpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanifrs9gpIns(EmpNo);
   else
      jobMainRepos.uspCpLoanifrs9gpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanIfrs9Hp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanifrs9hpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanifrs9hpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanifrs9hpIns(EmpNo);
   else
      jobMainRepos.uspCpLoanifrs9hpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanIfrs9Ip_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanifrs9ipIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanifrs9ipIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanifrs9ipIns(EmpNo);
   else
      jobMainRepos.uspCpLoanifrs9ipIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanIfrs9Jp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanifrs9jpIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanifrs9jpIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanifrs9jpIns(EmpNo);
   else
      jobMainRepos.uspCpLoanifrs9jpIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanIntDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanintdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanintdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanintdetailIns(EmpNo);
   else
      jobMainRepos.uspCpLoanintdetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanNotYet_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoannotyetIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoannotyetIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoannotyetIns(EmpNo);
   else
      jobMainRepos.uspCpLoannotyetIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanOverdue_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanoverdueIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanoverdueIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanoverdueIns(EmpNo);
   else
      jobMainRepos.uspCpLoanoverdueIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanRateChange_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoanratechangeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoanratechangeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoanratechangeIns(EmpNo);
   else
      jobMainRepos.uspCpLoanratechangeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanSynd_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoansyndIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoansyndIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoansyndIns(EmpNo);
   else
      jobMainRepos.uspCpLoansyndIns(EmpNo);
  }

  @Override
  public void Usp_Cp_LoanSyndItem_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpLoansynditemIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpLoansynditemIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpLoansynditemIns(EmpNo);
   else
      jobMainRepos.uspCpLoansynditemIns(EmpNo);
  }

  @Override
  public void Usp_Cp_MlaundryChkDtl_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMlaundrychkdtlIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMlaundrychkdtlIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMlaundrychkdtlIns(EmpNo);
   else
      jobMainRepos.uspCpMlaundrychkdtlIns(EmpNo);
  }

  @Override
  public void Usp_Cp_MlaundryDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMlaundrydetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMlaundrydetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMlaundrydetailIns(EmpNo);
   else
      jobMainRepos.uspCpMlaundrydetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_MlaundryParas_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMlaundryparasIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMlaundryparasIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMlaundryparasIns(EmpNo);
   else
      jobMainRepos.uspCpMlaundryparasIns(EmpNo);
  }

  @Override
  public void Usp_Cp_MlaundryRecord_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMlaundryrecordIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMlaundryrecordIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMlaundryrecordIns(EmpNo);
   else
      jobMainRepos.uspCpMlaundryrecordIns(EmpNo);
  }

  @Override
  public void Usp_Cp_MonthlyLM003_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMonthlylm003Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMonthlylm003Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMonthlylm003Ins(EmpNo);
   else
      jobMainRepos.uspCpMonthlylm003Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_MonthlyLM028_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMonthlylm028Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMonthlylm028Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMonthlylm028Ins(EmpNo);
   else
      jobMainRepos.uspCpMonthlylm028Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_MonthlyLM032_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMonthlylm032Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMonthlylm032Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMonthlylm032Ins(EmpNo);
   else
      jobMainRepos.uspCpMonthlylm032Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_MonthlyLM036Portfolio_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMonthlylm036portfolioIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMonthlylm036portfolioIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMonthlylm036portfolioIns(EmpNo);
   else
      jobMainRepos.uspCpMonthlylm036portfolioIns(EmpNo);
  }

  @Override
  public void Usp_Cp_MonthlyLM052AssetClass_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMonthlylm052assetclassIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMonthlylm052assetclassIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMonthlylm052assetclassIns(EmpNo);
   else
      jobMainRepos.uspCpMonthlylm052assetclassIns(EmpNo);
  }

  @Override
  public void Usp_Cp_MonthlyLM052LoanAsset_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMonthlylm052loanassetIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMonthlylm052loanassetIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMonthlylm052loanassetIns(EmpNo);
   else
      jobMainRepos.uspCpMonthlylm052loanassetIns(EmpNo);
  }

  @Override
  public void Usp_Cp_MonthlyLM052Loss_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMonthlylm052lossIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMonthlylm052lossIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMonthlylm052lossIns(EmpNo);
   else
      jobMainRepos.uspCpMonthlylm052lossIns(EmpNo);
  }

  @Override
  public void Usp_Cp_MonthlyLM052Ovdu_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMonthlylm052ovduIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMonthlylm052ovduIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMonthlylm052ovduIns(EmpNo);
   else
      jobMainRepos.uspCpMonthlylm052ovduIns(EmpNo);
  }

  @Override
  public void Usp_Cp_MonthlyLoanBal_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpMonthlyloanbalIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpMonthlyloanbalIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpMonthlyloanbalIns(EmpNo);
   else
      jobMainRepos.uspCpMonthlyloanbalIns(EmpNo);
  }

  @Override
  public void Usp_Cp_NegAppr_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpNegapprIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpNegapprIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpNegapprIns(EmpNo);
   else
      jobMainRepos.uspCpNegapprIns(EmpNo);
  }

  @Override
  public void Usp_Cp_NegAppr01_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpNegappr01Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpNegappr01Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpNegappr01Ins(EmpNo);
   else
      jobMainRepos.uspCpNegappr01Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_NegAppr02_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpNegappr02Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpNegappr02Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpNegappr02Ins(EmpNo);
   else
      jobMainRepos.uspCpNegappr02Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_NegFinAcct_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpNegfinacctIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpNegfinacctIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpNegfinacctIns(EmpNo);
   else
      jobMainRepos.uspCpNegfinacctIns(EmpNo);
  }

  @Override
  public void Usp_Cp_NegFinShare_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpNegfinshareIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpNegfinshareIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpNegfinshareIns(EmpNo);
   else
      jobMainRepos.uspCpNegfinshareIns(EmpNo);
  }

  @Override
  public void Usp_Cp_NegFinShareLog_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpNegfinsharelogIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpNegfinsharelogIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpNegfinsharelogIns(EmpNo);
   else
      jobMainRepos.uspCpNegfinsharelogIns(EmpNo);
  }

  @Override
  public void Usp_Cp_NegMain_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpNegmainIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpNegmainIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpNegmainIns(EmpNo);
   else
      jobMainRepos.uspCpNegmainIns(EmpNo);
  }

  @Override
  public void Usp_Cp_NegQueryCust_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpNegquerycustIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpNegquerycustIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpNegquerycustIns(EmpNo);
   else
      jobMainRepos.uspCpNegquerycustIns(EmpNo);
  }

  @Override
  public void Usp_Cp_NegTrans_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpNegtransIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpNegtransIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpNegtransIns(EmpNo);
   else
      jobMainRepos.uspCpNegtransIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfBsDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfbsdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfbsdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfbsdetailIns(EmpNo);
   else
      jobMainRepos.uspCpPfbsdetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfBsDetailAdjust_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfbsdetailadjustIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfbsdetailadjustIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfbsdetailadjustIns(EmpNo);
   else
      jobMainRepos.uspCpPfbsdetailadjustIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfBsOfficer_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfbsofficerIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfbsofficerIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfbsofficerIns(EmpNo);
   else
      jobMainRepos.uspCpPfbsofficerIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfCoOfficer_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfcoofficerIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfcoofficerIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfcoofficerIns(EmpNo);
   else
      jobMainRepos.uspCpPfcoofficerIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfCoOfficerLog_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfcoofficerlogIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfcoofficerlogIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfcoofficerlogIns(EmpNo);
   else
      jobMainRepos.uspCpPfcoofficerlogIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfDeparment_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfdeparmentIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfdeparmentIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfdeparmentIns(EmpNo);
   else
      jobMainRepos.uspCpPfdeparmentIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfdetailIns(EmpNo);
   else
      jobMainRepos.uspCpPfdetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfInsCheck_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfinscheckIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfinscheckIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfinscheckIns(EmpNo);
   else
      jobMainRepos.uspCpPfinscheckIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfIntranetAdjust_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfintranetadjustIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfintranetadjustIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfintranetadjustIns(EmpNo);
   else
      jobMainRepos.uspCpPfintranetadjustIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfItDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfitdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfitdetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfitdetailIns(EmpNo);
   else
      jobMainRepos.uspCpPfitdetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfItDetailAdjust_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfitdetailadjustIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfitdetailadjustIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfitdetailadjustIns(EmpNo);
   else
      jobMainRepos.uspCpPfitdetailadjustIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfReward_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfrewardIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfrewardIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfrewardIns(EmpNo);
   else
      jobMainRepos.uspCpPfrewardIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfRewardMedia_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfrewardmediaIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfrewardmediaIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfrewardmediaIns(EmpNo);
   else
      jobMainRepos.uspCpPfrewardmediaIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PfSpecParms_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPfspecparmsIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPfspecparmsIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPfspecparmsIns(EmpNo);
   else
      jobMainRepos.uspCpPfspecparmsIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PostAuthLog_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPostauthlogIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPostauthlogIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPostauthlogIns(EmpNo);
   else
      jobMainRepos.uspCpPostauthlogIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PostAuthLogHistory_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPostauthloghistoryIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPostauthloghistoryIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPostauthloghistoryIns(EmpNo);
   else
      jobMainRepos.uspCpPostauthloghistoryIns(EmpNo);
  }

  @Override
  public void Usp_Cp_PostDeductMedia_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpPostdeductmediaIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpPostdeductmediaIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpPostdeductmediaIns(EmpNo);
   else
      jobMainRepos.uspCpPostdeductmediaIns(EmpNo);
  }

  @Override
  public void Usp_Cp_ReltMain_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpReltmainIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpReltmainIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpReltmainIns(EmpNo);
   else
      jobMainRepos.uspCpReltmainIns(EmpNo);
  }

  @Override
  public void Usp_Cp_RepayActChangeLog_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpRepayactchangelogIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpRepayactchangelogIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpRepayactchangelogIns(EmpNo);
   else
      jobMainRepos.uspCpRepayactchangelogIns(EmpNo);
  }

  @Override
  public void Usp_Cp_RptJcic_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpRptjcicIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpRptjcicIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpRptjcicIns(EmpNo);
   else
      jobMainRepos.uspCpRptjcicIns(EmpNo);
  }

  @Override
  public void Usp_Cp_RptRelationCompany_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpRptrelationcompanyIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpRptrelationcompanyIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpRptrelationcompanyIns(EmpNo);
   else
      jobMainRepos.uspCpRptrelationcompanyIns(EmpNo);
  }

  @Override
  public void Usp_Cp_RptRelationFamily_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpRptrelationfamilyIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpRptrelationfamilyIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpRptrelationfamilyIns(EmpNo);
   else
      jobMainRepos.uspCpRptrelationfamilyIns(EmpNo);
  }

  @Override
  public void Usp_Cp_RptRelationSelf_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpRptrelationselfIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpRptrelationselfIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpRptrelationselfIns(EmpNo);
   else
      jobMainRepos.uspCpRptrelationselfIns(EmpNo);
  }

  @Override
  public void Usp_Cp_RptSubCom_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpRptsubcomIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpRptsubcomIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpRptsubcomIns(EmpNo);
   else
      jobMainRepos.uspCpRptsubcomIns(EmpNo);
  }

  @Override
  public void Usp_Cp_SlipEbsRecord_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpSlipebsrecordIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpSlipebsrecordIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpSlipebsrecordIns(EmpNo);
   else
      jobMainRepos.uspCpSlipebsrecordIns(EmpNo);
  }

  @Override
  public void Usp_Cp_SlipMedia_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpSlipmediaIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpSlipmediaIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpSlipmediaIns(EmpNo);
   else
      jobMainRepos.uspCpSlipmediaIns(EmpNo);
  }

  @Override
  public void Usp_Cp_SlipMedia2022_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpSlipmedia2022Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpSlipmedia2022Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpSlipmedia2022Ins(EmpNo);
   else
      jobMainRepos.uspCpSlipmedia2022Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_SpecInnReCheck_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpSpecinnrecheckIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpSpecinnrecheckIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpSpecinnrecheckIns(EmpNo);
   else
      jobMainRepos.uspCpSpecinnrecheckIns(EmpNo);
  }

  @Override
  public void Usp_Cp_StgCdEmp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpStgcdempIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpStgcdempIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpStgcdempIns(EmpNo);
   else
      jobMainRepos.uspCpStgcdempIns(EmpNo);
  }

  @Override
  public void Usp_Cp_SystemParas_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpSystemparasIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpSystemparasIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpSystemparasIns(EmpNo);
   else
      jobMainRepos.uspCpSystemparasIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TbJcicMu01_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTbjcicmu01Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTbjcicmu01Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTbjcicmu01Ins(EmpNo);
   else
      jobMainRepos.uspCpTbjcicmu01Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_TbJcicW020_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTbjcicw020Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTbjcicw020Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTbjcicw020Ins(EmpNo);
   else
      jobMainRepos.uspCpTbjcicw020Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_TbJcicZZ50_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTbjciczz50Ins(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTbjciczz50Ins(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTbjciczz50Ins(EmpNo);
   else
      jobMainRepos.uspCpTbjciczz50Ins(EmpNo);
  }

  @Override
  public void Usp_Cp_TxAgent_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxagentIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxagentIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxagentIns(EmpNo);
   else
      jobMainRepos.uspCpTxagentIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxAmlCredit_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxamlcreditIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxamlcreditIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxamlcreditIns(EmpNo);
   else
      jobMainRepos.uspCpTxamlcreditIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxAmlLog_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxamllogIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxamllogIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxamllogIns(EmpNo);
   else
      jobMainRepos.uspCpTxamllogIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxAmlNotice_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxamlnoticeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxamlnoticeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxamlnoticeIns(EmpNo);
   else
      jobMainRepos.uspCpTxamlnoticeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxAmlRating_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxamlratingIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxamlratingIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxamlratingIns(EmpNo);
   else
      jobMainRepos.uspCpTxamlratingIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxApLog_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxaplogIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxaplogIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxaplogIns(EmpNo);
   else
      jobMainRepos.uspCpTxaplogIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxApLogList_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxaploglistIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxaploglistIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxaploglistIns(EmpNo);
   else
      jobMainRepos.uspCpTxaploglistIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxArchiveTable_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxarchivetableIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxarchivetableIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxarchivetableIns(EmpNo);
   else
      jobMainRepos.uspCpTxarchivetableIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxArchiveTableLog_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxarchivetablelogIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxarchivetablelogIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxarchivetablelogIns(EmpNo);
   else
      jobMainRepos.uspCpTxarchivetablelogIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxAttachment_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxattachmentIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxattachmentIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxattachmentIns(EmpNo);
   else
      jobMainRepos.uspCpTxattachmentIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxAttachType_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxattachtypeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxattachtypeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxattachtypeIns(EmpNo);
   else
      jobMainRepos.uspCpTxattachtypeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxAuthGroup_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxauthgroupIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxauthgroupIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxauthgroupIns(EmpNo);
   else
      jobMainRepos.uspCpTxauthgroupIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxAuthority_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxauthorityIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxauthorityIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxauthorityIns(EmpNo);
   else
      jobMainRepos.uspCpTxauthorityIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxAuthorize_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxauthorizeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxauthorizeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxauthorizeIns(EmpNo);
   else
      jobMainRepos.uspCpTxauthorizeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxBizDate_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxbizdateIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxbizdateIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxbizdateIns(EmpNo);
   else
      jobMainRepos.uspCpTxbizdateIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxControl_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxcontrolIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxcontrolIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxcontrolIns(EmpNo);
   else
      jobMainRepos.uspCpTxcontrolIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxCruiser_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxcruiserIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxcruiserIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxcruiserIns(EmpNo);
   else
      jobMainRepos.uspCpTxcruiserIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxCurr_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxcurrIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxcurrIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxcurrIns(EmpNo);
   else
      jobMainRepos.uspCpTxcurrIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxDataLog_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxdatalogIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxdatalogIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxdatalogIns(EmpNo);
   else
      jobMainRepos.uspCpTxdatalogIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxErrCode_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxerrcodeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxerrcodeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxerrcodeIns(EmpNo);
   else
      jobMainRepos.uspCpTxerrcodeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxFile_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxfileIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxfileIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxfileIns(EmpNo);
   else
      jobMainRepos.uspCpTxfileIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxFlow_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxflowIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxflowIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxflowIns(EmpNo);
   else
      jobMainRepos.uspCpTxflowIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxHoliday_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxholidayIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxholidayIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxholidayIns(EmpNo);
   else
      jobMainRepos.uspCpTxholidayIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxInquiry_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxinquiryIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxinquiryIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxinquiryIns(EmpNo);
   else
      jobMainRepos.uspCpTxinquiryIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxLock_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxlockIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxlockIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxlockIns(EmpNo);
   else
      jobMainRepos.uspCpTxlockIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxPrinter_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxprinterIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxprinterIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxprinterIns(EmpNo);
   else
      jobMainRepos.uspCpTxprinterIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxProcess_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxprocessIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxprocessIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxprocessIns(EmpNo);
   else
      jobMainRepos.uspCpTxprocessIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxRecord_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxrecordIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxrecordIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxrecordIns(EmpNo);
   else
      jobMainRepos.uspCpTxrecordIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxTeller_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxtellerIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxtellerIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxtellerIns(EmpNo);
   else
      jobMainRepos.uspCpTxtellerIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxTellerAuth_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxtellerauthIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxtellerauthIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxtellerauthIns(EmpNo);
   else
      jobMainRepos.uspCpTxtellerauthIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxTellerTest_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxtellertestIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxtellertestIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxtellertestIns(EmpNo);
   else
      jobMainRepos.uspCpTxtellertestIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxTemp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxtempIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxtempIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxtempIns(EmpNo);
   else
      jobMainRepos.uspCpTxtempIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxToDoDetail_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxtododetailIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxtododetailIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxtododetailIns(EmpNo);
   else
      jobMainRepos.uspCpTxtododetailIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxToDoDetailReserve_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxtododetailreserveIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxtododetailreserveIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxtododetailreserveIns(EmpNo);
   else
      jobMainRepos.uspCpTxtododetailreserveIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxToDoMain_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxtodomainIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxtodomainIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxtodomainIns(EmpNo);
   else
      jobMainRepos.uspCpTxtodomainIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxTranCode_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxtrancodeIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxtrancodeIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxtrancodeIns(EmpNo);
   else
      jobMainRepos.uspCpTxtrancodeIns(EmpNo);
  }

  @Override
  public void Usp_Cp_TxUnLock_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpTxunlockIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpTxunlockIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpTxunlockIns(EmpNo);
   else
      jobMainRepos.uspCpTxunlockIns(EmpNo);
  }

  @Override
  public void Usp_Cp_UspErrorLog_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpUsperrorlogIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpUsperrorlogIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpUsperrorlogIns(EmpNo);
   else
      jobMainRepos.uspCpUsperrorlogIns(EmpNo);
  }

  @Override
  public void Usp_Cp_YearlyHouseLoanInt_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpYearlyhouseloanintIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpYearlyhouseloanintIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpYearlyhouseloanintIns(EmpNo);
   else
      jobMainRepos.uspCpYearlyhouseloanintIns(EmpNo);
  }

  @Override
  public void Usp_Cp_YearlyHouseLoanIntCheck_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jobMainReposDay.uspCpYearlyhouseloanintcheckIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jobMainReposMon.uspCpYearlyhouseloanintcheckIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jobMainReposHist.uspCpYearlyhouseloanintcheckIns(EmpNo);
   else
      jobMainRepos.uspCpYearlyhouseloanintcheckIns(EmpNo);
  }

}
