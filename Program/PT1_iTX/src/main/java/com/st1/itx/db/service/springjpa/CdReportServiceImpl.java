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
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.repository.online.CdReportRepository;
import com.st1.itx.db.repository.day.CdReportRepositoryDay;
import com.st1.itx.db.repository.mon.CdReportRepositoryMon;
import com.st1.itx.db.repository.hist.CdReportRepositoryHist;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdReportService")
@Repository
public class CdReportServiceImpl extends ASpringJpaParm implements CdReportService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdReportRepository cdReportRepos;

  @Autowired
  private CdReportRepositoryDay cdReportReposDay;

  @Autowired
  private CdReportRepositoryMon cdReportReposMon;

  @Autowired
  private CdReportRepositoryHist cdReportReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdReportRepos);
    org.junit.Assert.assertNotNull(cdReportReposDay);
    org.junit.Assert.assertNotNull(cdReportReposMon);
    org.junit.Assert.assertNotNull(cdReportReposHist);
  }

  @Override
  public CdReport findById(String formNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + formNo);
    Optional<CdReport> cdReport = null;
    if (dbName.equals(ContentName.onDay))
      cdReport = cdReportReposDay.findById(formNo);
    else if (dbName.equals(ContentName.onMon))
      cdReport = cdReportReposMon.findById(formNo);
    else if (dbName.equals(ContentName.onHist))
      cdReport = cdReportReposHist.findById(formNo);
    else 
      cdReport = cdReportRepos.findById(formNo);
    CdReport obj = cdReport.isPresent() ? cdReport.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdReport> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdReport> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "FormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "FormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdReportReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdReportReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdReportReposHist.findAll(pageable);
    else 
      slice = cdReportRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdReport> findCycle(int cycle_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdReport> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCycle " + dbName + " : " + "cycle_0 : " + cycle_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdReportReposDay.findAllByCycleIs(cycle_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdReportReposMon.findAllByCycleIs(cycle_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdReportReposHist.findAllByCycleIs(cycle_0, pageable);
    else 
      slice = cdReportRepos.findAllByCycleIs(cycle_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdReport> findEnable(String enable_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdReport> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findEnable " + dbName + " : " + "enable_0 : " + enable_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdReportReposDay.findAllByEnableIs(enable_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdReportReposMon.findAllByEnableIs(enable_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdReportReposHist.findAllByEnableIs(enable_0, pageable);
    else 
      slice = cdReportRepos.findAllByEnableIs(enable_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdReport> formNoLike(String formNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdReport> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("formNoLike " + dbName + " : " + "formNo_0 : " + formNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdReportReposDay.findAllByFormNoLikeOrderByFormNoAsc(formNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdReportReposMon.findAllByFormNoLikeOrderByFormNoAsc(formNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdReportReposHist.findAllByFormNoLikeOrderByFormNoAsc(formNo_0, pageable);
    else 
      slice = cdReportRepos.findAllByFormNoLikeOrderByFormNoAsc(formNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdReport> formNameLike(String formName_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdReport> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("formNameLike " + dbName + " : " + "formName_0 : " + formName_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdReportReposDay.findAllByFormNameLikeOrderByFormNoAsc(formName_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdReportReposMon.findAllByFormNameLikeOrderByFormNoAsc(formName_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdReportReposHist.findAllByFormNameLikeOrderByFormNoAsc(formName_0, pageable);
    else 
      slice = cdReportRepos.findAllByFormNameLikeOrderByFormNoAsc(formName_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdReport FormNoFirst(String formNo_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("FormNoFirst " + dbName + " : " + "formNo_0 : " + formNo_0);
    Optional<CdReport> cdReportT = null;
    if (dbName.equals(ContentName.onDay))
      cdReportT = cdReportReposDay.findTopByFormNoIs(formNo_0);
    else if (dbName.equals(ContentName.onMon))
      cdReportT = cdReportReposMon.findTopByFormNoIs(formNo_0);
    else if (dbName.equals(ContentName.onHist))
      cdReportT = cdReportReposHist.findTopByFormNoIs(formNo_0);
    else 
      cdReportT = cdReportRepos.findTopByFormNoIs(formNo_0);

    return cdReportT.isPresent() ? cdReportT.get() : null;
  }

  @Override
  public Slice<CdReport> findRptGroupNo(String groupNo_0, String formNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdReport> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findRptGroupNo " + dbName + " : " + "groupNo_0 : " + groupNo_0 + " formNo_1 : " +  formNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdReportReposDay.findAllByGroupNoIsAndFormNoLikeOrderByGroupNoAscFormNoAsc(groupNo_0, formNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdReportReposMon.findAllByGroupNoIsAndFormNoLikeOrderByGroupNoAscFormNoAsc(groupNo_0, formNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdReportReposHist.findAllByGroupNoIsAndFormNoLikeOrderByGroupNoAscFormNoAsc(groupNo_0, formNo_1, pageable);
    else 
      slice = cdReportRepos.findAllByGroupNoIsAndFormNoLikeOrderByGroupNoAscFormNoAsc(groupNo_0, formNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdReport holdById(String formNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + formNo);
    Optional<CdReport> cdReport = null;
    if (dbName.equals(ContentName.onDay))
      cdReport = cdReportReposDay.findByFormNo(formNo);
    else if (dbName.equals(ContentName.onMon))
      cdReport = cdReportReposMon.findByFormNo(formNo);
    else if (dbName.equals(ContentName.onHist))
      cdReport = cdReportReposHist.findByFormNo(formNo);
    else 
      cdReport = cdReportRepos.findByFormNo(formNo);
    return cdReport.isPresent() ? cdReport.get() : null;
  }

  @Override
  public CdReport holdById(CdReport cdReport, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdReport.getFormNo());
    Optional<CdReport> cdReportT = null;
    if (dbName.equals(ContentName.onDay))
      cdReportT = cdReportReposDay.findByFormNo(cdReport.getFormNo());
    else if (dbName.equals(ContentName.onMon))
      cdReportT = cdReportReposMon.findByFormNo(cdReport.getFormNo());
    else if (dbName.equals(ContentName.onHist))
      cdReportT = cdReportReposHist.findByFormNo(cdReport.getFormNo());
    else 
      cdReportT = cdReportRepos.findByFormNo(cdReport.getFormNo());
    return cdReportT.isPresent() ? cdReportT.get() : null;
  }

  @Override
  public CdReport insert(CdReport cdReport, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdReport.getFormNo());
    if (this.findById(cdReport.getFormNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdReport.setCreateEmpNo(empNot);

    if(cdReport.getLastUpdateEmpNo() == null || cdReport.getLastUpdateEmpNo().isEmpty())
      cdReport.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdReportReposDay.saveAndFlush(cdReport);	
    else if (dbName.equals(ContentName.onMon))
      return cdReportReposMon.saveAndFlush(cdReport);
    else if (dbName.equals(ContentName.onHist))
      return cdReportReposHist.saveAndFlush(cdReport);
    else 
    return cdReportRepos.saveAndFlush(cdReport);
  }

  @Override
  public CdReport update(CdReport cdReport, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdReport.getFormNo());
    if (!empNot.isEmpty())
      cdReport.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdReportReposDay.saveAndFlush(cdReport);	
    else if (dbName.equals(ContentName.onMon))
      return cdReportReposMon.saveAndFlush(cdReport);
    else if (dbName.equals(ContentName.onHist))
      return cdReportReposHist.saveAndFlush(cdReport);
    else 
    return cdReportRepos.saveAndFlush(cdReport);
  }

  @Override
  public CdReport update2(CdReport cdReport, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdReport.getFormNo());
    if (!empNot.isEmpty())
      cdReport.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdReportReposDay.saveAndFlush(cdReport);	
    else if (dbName.equals(ContentName.onMon))
      cdReportReposMon.saveAndFlush(cdReport);
    else if (dbName.equals(ContentName.onHist))
        cdReportReposHist.saveAndFlush(cdReport);
    else 
      cdReportRepos.saveAndFlush(cdReport);	
    return this.findById(cdReport.getFormNo());
  }

  @Override
  public void delete(CdReport cdReport, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdReport.getFormNo());
    if (dbName.equals(ContentName.onDay)) {
      cdReportReposDay.delete(cdReport);	
      cdReportReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdReportReposMon.delete(cdReport);	
      cdReportReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdReportReposHist.delete(cdReport);
      cdReportReposHist.flush();
    }
    else {
      cdReportRepos.delete(cdReport);
      cdReportRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdReport> cdReport, TitaVo... titaVo) throws DBException {
    if (cdReport == null || cdReport.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdReport t : cdReport){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdReport = cdReportReposDay.saveAll(cdReport);	
      cdReportReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdReport = cdReportReposMon.saveAll(cdReport);	
      cdReportReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdReport = cdReportReposHist.saveAll(cdReport);
      cdReportReposHist.flush();
    }
    else {
      cdReport = cdReportRepos.saveAll(cdReport);
      cdReportRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdReport> cdReport, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdReport == null || cdReport.size() == 0)
      throw new DBException(6);

    for (CdReport t : cdReport) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdReport = cdReportReposDay.saveAll(cdReport);	
      cdReportReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdReport = cdReportReposMon.saveAll(cdReport);	
      cdReportReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdReport = cdReportReposHist.saveAll(cdReport);
      cdReportReposHist.flush();
    }
    else {
      cdReport = cdReportRepos.saveAll(cdReport);
      cdReportRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdReport> cdReport, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdReport == null || cdReport.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdReportReposDay.deleteAll(cdReport);	
      cdReportReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdReportReposMon.deleteAll(cdReport);	
      cdReportReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdReportReposHist.deleteAll(cdReport);
      cdReportReposHist.flush();
    }
    else {
      cdReportRepos.deleteAll(cdReport);
      cdReportRepos.flush();
    }
  }

}
