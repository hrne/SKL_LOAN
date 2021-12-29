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
import com.st1.itx.db.domain.JobDetail;
import com.st1.itx.db.domain.JobDetailId;
import com.st1.itx.db.repository.online.JobDetailRepository;
import com.st1.itx.db.repository.day.JobDetailRepositoryDay;
import com.st1.itx.db.repository.mon.JobDetailRepositoryMon;
import com.st1.itx.db.repository.hist.JobDetailRepositoryHist;
import com.st1.itx.db.service.JobDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jobDetailService")
@Repository
public class JobDetailServiceImpl extends ASpringJpaParm implements JobDetailService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JobDetailRepository jobDetailRepos;

  @Autowired
  private JobDetailRepositoryDay jobDetailReposDay;

  @Autowired
  private JobDetailRepositoryMon jobDetailReposMon;

  @Autowired
  private JobDetailRepositoryHist jobDetailReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jobDetailRepos);
    org.junit.Assert.assertNotNull(jobDetailReposDay);
    org.junit.Assert.assertNotNull(jobDetailReposMon);
    org.junit.Assert.assertNotNull(jobDetailReposHist);
  }

  @Override
  public JobDetail findById(JobDetailId jobDetailId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jobDetailId);
    Optional<JobDetail> jobDetail = null;
    if (dbName.equals(ContentName.onDay))
      jobDetail = jobDetailReposDay.findById(jobDetailId);
    else if (dbName.equals(ContentName.onMon))
      jobDetail = jobDetailReposMon.findById(jobDetailId);
    else if (dbName.equals(ContentName.onHist))
      jobDetail = jobDetailReposHist.findById(jobDetailId);
    else 
      jobDetail = jobDetailRepos.findById(jobDetailId);
    JobDetail obj = jobDetail.isPresent() ? jobDetail.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JobDetail> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JobDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "TxSeq", "ExecDate", "JobCode", "StepId"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "TxSeq", "ExecDate", "JobCode", "StepId"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jobDetailReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jobDetailReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jobDetailReposHist.findAll(pageable);
    else 
      slice = jobDetailRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JobDetail> jobCodeEq(int execDate_0, String jobCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JobDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("jobCodeEq " + dbName + " : " + "execDate_0 : " + execDate_0 + " jobCode_1 : " +  jobCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = jobDetailReposDay.findAllByExecDateIsAndJobCodeIsOrderByExecDateAscJobCodeAscStepIdAsc(execDate_0, jobCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jobDetailReposMon.findAllByExecDateIsAndJobCodeIsOrderByExecDateAscJobCodeAscStepIdAsc(execDate_0, jobCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jobDetailReposHist.findAllByExecDateIsAndJobCodeIsOrderByExecDateAscJobCodeAscStepIdAsc(execDate_0, jobCode_1, pageable);
    else 
      slice = jobDetailRepos.findAllByExecDateIsAndJobCodeIsOrderByExecDateAscJobCodeAscStepIdAsc(execDate_0, jobCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JobDetail> execDateEq(int execDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JobDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("execDateEq " + dbName + " : " + "execDate_0 : " + execDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jobDetailReposDay.findAllByExecDateIsOrderByExecDateAscJobCodeAscStepIdAsc(execDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jobDetailReposMon.findAllByExecDateIsOrderByExecDateAscJobCodeAscStepIdAsc(execDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jobDetailReposHist.findAllByExecDateIsOrderByExecDateAscJobCodeAscStepIdAsc(execDate_0, pageable);
    else 
      slice = jobDetailRepos.findAllByExecDateIsOrderByExecDateAscJobCodeAscStepIdAsc(execDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JobDetail execDateFirst(int execDate_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("execDateFirst " + dbName + " : " + "execDate_0 : " + execDate_0);
    Optional<JobDetail> jobDetailT = null;
    if (dbName.equals(ContentName.onDay))
      jobDetailT = jobDetailReposDay.findTopByExecDateGreaterThanEqualOrderByExecDateDesc(execDate_0);
    else if (dbName.equals(ContentName.onMon))
      jobDetailT = jobDetailReposMon.findTopByExecDateGreaterThanEqualOrderByExecDateDesc(execDate_0);
    else if (dbName.equals(ContentName.onHist))
      jobDetailT = jobDetailReposHist.findTopByExecDateGreaterThanEqualOrderByExecDateDesc(execDate_0);
    else 
      jobDetailT = jobDetailRepos.findTopByExecDateGreaterThanEqualOrderByExecDateDesc(execDate_0);

    return jobDetailT.isPresent() ? jobDetailT.get() : null;
  }

  @Override
  public Slice<JobDetail> findExecDateIn(int execDate_0, int execDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JobDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findExecDateIn " + dbName + " : " + "execDate_0 : " + execDate_0 + " execDate_1 : " +  execDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jobDetailReposDay.findAllByExecDateGreaterThanEqualAndExecDateLessThanEqualOrderByStepStartTimeDesc(execDate_0, execDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jobDetailReposMon.findAllByExecDateGreaterThanEqualAndExecDateLessThanEqualOrderByStepStartTimeDesc(execDate_0, execDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jobDetailReposHist.findAllByExecDateGreaterThanEqualAndExecDateLessThanEqualOrderByStepStartTimeDesc(execDate_0, execDate_1, pageable);
    else 
      slice = jobDetailRepos.findAllByExecDateGreaterThanEqualAndExecDateLessThanEqualOrderByStepStartTimeDesc(execDate_0, execDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JobDetail> findStatusExecDateIn(int execDate_0, int execDate_1, String status_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JobDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findStatusExecDateIn " + dbName + " : " + "execDate_0 : " + execDate_0 + " execDate_1 : " +  execDate_1 + " status_2 : " +  status_2);
    if (dbName.equals(ContentName.onDay))
      slice = jobDetailReposDay.findAllByExecDateGreaterThanEqualAndExecDateLessThanEqualAndStatusIsOrderByStepStartTimeDesc(execDate_0, execDate_1, status_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jobDetailReposMon.findAllByExecDateGreaterThanEqualAndExecDateLessThanEqualAndStatusIsOrderByStepStartTimeDesc(execDate_0, execDate_1, status_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jobDetailReposHist.findAllByExecDateGreaterThanEqualAndExecDateLessThanEqualAndStatusIsOrderByStepStartTimeDesc(execDate_0, execDate_1, status_2, pageable);
    else 
      slice = jobDetailRepos.findAllByExecDateGreaterThanEqualAndExecDateLessThanEqualAndStatusIsOrderByStepStartTimeDesc(execDate_0, execDate_1, status_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JobDetail holdById(JobDetailId jobDetailId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jobDetailId);
    Optional<JobDetail> jobDetail = null;
    if (dbName.equals(ContentName.onDay))
      jobDetail = jobDetailReposDay.findByJobDetailId(jobDetailId);
    else if (dbName.equals(ContentName.onMon))
      jobDetail = jobDetailReposMon.findByJobDetailId(jobDetailId);
    else if (dbName.equals(ContentName.onHist))
      jobDetail = jobDetailReposHist.findByJobDetailId(jobDetailId);
    else 
      jobDetail = jobDetailRepos.findByJobDetailId(jobDetailId);
    return jobDetail.isPresent() ? jobDetail.get() : null;
  }

  @Override
  public JobDetail holdById(JobDetail jobDetail, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jobDetail.getJobDetailId());
    Optional<JobDetail> jobDetailT = null;
    if (dbName.equals(ContentName.onDay))
      jobDetailT = jobDetailReposDay.findByJobDetailId(jobDetail.getJobDetailId());
    else if (dbName.equals(ContentName.onMon))
      jobDetailT = jobDetailReposMon.findByJobDetailId(jobDetail.getJobDetailId());
    else if (dbName.equals(ContentName.onHist))
      jobDetailT = jobDetailReposHist.findByJobDetailId(jobDetail.getJobDetailId());
    else 
      jobDetailT = jobDetailRepos.findByJobDetailId(jobDetail.getJobDetailId());
    return jobDetailT.isPresent() ? jobDetailT.get() : null;
  }

  @Override
  public JobDetail insert(JobDetail jobDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jobDetail.getJobDetailId());
    if (this.findById(jobDetail.getJobDetailId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jobDetail.setCreateEmpNo(empNot);

    if(jobDetail.getLastUpdateEmpNo() == null || jobDetail.getLastUpdateEmpNo().isEmpty())
      jobDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jobDetailReposDay.saveAndFlush(jobDetail);	
    else if (dbName.equals(ContentName.onMon))
      return jobDetailReposMon.saveAndFlush(jobDetail);
    else if (dbName.equals(ContentName.onHist))
      return jobDetailReposHist.saveAndFlush(jobDetail);
    else 
    return jobDetailRepos.saveAndFlush(jobDetail);
  }

  @Override
  public JobDetail update(JobDetail jobDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jobDetail.getJobDetailId());
    if (!empNot.isEmpty())
      jobDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jobDetailReposDay.saveAndFlush(jobDetail);	
    else if (dbName.equals(ContentName.onMon))
      return jobDetailReposMon.saveAndFlush(jobDetail);
    else if (dbName.equals(ContentName.onHist))
      return jobDetailReposHist.saveAndFlush(jobDetail);
    else 
    return jobDetailRepos.saveAndFlush(jobDetail);
  }

  @Override
  public JobDetail update2(JobDetail jobDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jobDetail.getJobDetailId());
    if (!empNot.isEmpty())
      jobDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jobDetailReposDay.saveAndFlush(jobDetail);	
    else if (dbName.equals(ContentName.onMon))
      jobDetailReposMon.saveAndFlush(jobDetail);
    else if (dbName.equals(ContentName.onHist))
        jobDetailReposHist.saveAndFlush(jobDetail);
    else 
      jobDetailRepos.saveAndFlush(jobDetail);	
    return this.findById(jobDetail.getJobDetailId());
  }

  @Override
  public void delete(JobDetail jobDetail, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jobDetail.getJobDetailId());
    if (dbName.equals(ContentName.onDay)) {
      jobDetailReposDay.delete(jobDetail);	
      jobDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jobDetailReposMon.delete(jobDetail);	
      jobDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jobDetailReposHist.delete(jobDetail);
      jobDetailReposHist.flush();
    }
    else {
      jobDetailRepos.delete(jobDetail);
      jobDetailRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JobDetail> jobDetail, TitaVo... titaVo) throws DBException {
    if (jobDetail == null || jobDetail.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JobDetail t : jobDetail){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jobDetail = jobDetailReposDay.saveAll(jobDetail);	
      jobDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jobDetail = jobDetailReposMon.saveAll(jobDetail);	
      jobDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jobDetail = jobDetailReposHist.saveAll(jobDetail);
      jobDetailReposHist.flush();
    }
    else {
      jobDetail = jobDetailRepos.saveAll(jobDetail);
      jobDetailRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JobDetail> jobDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jobDetail == null || jobDetail.size() == 0)
      throw new DBException(6);

    for (JobDetail t : jobDetail) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jobDetail = jobDetailReposDay.saveAll(jobDetail);	
      jobDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jobDetail = jobDetailReposMon.saveAll(jobDetail);	
      jobDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jobDetail = jobDetailReposHist.saveAll(jobDetail);
      jobDetailReposHist.flush();
    }
    else {
      jobDetail = jobDetailRepos.saveAll(jobDetail);
      jobDetailRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JobDetail> jobDetail, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jobDetail == null || jobDetail.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jobDetailReposDay.deleteAll(jobDetail);	
      jobDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jobDetailReposMon.deleteAll(jobDetail);	
      jobDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jobDetailReposHist.deleteAll(jobDetail);
      jobDetailReposHist.flush();
    }
    else {
      jobDetailRepos.deleteAll(jobDetail);
      jobDetailRepos.flush();
    }
  }

}
