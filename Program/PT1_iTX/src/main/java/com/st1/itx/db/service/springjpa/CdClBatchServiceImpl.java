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
import com.st1.itx.db.domain.CdClBatch;
import com.st1.itx.db.repository.online.CdClBatchRepository;
import com.st1.itx.db.repository.day.CdClBatchRepositoryDay;
import com.st1.itx.db.repository.mon.CdClBatchRepositoryMon;
import com.st1.itx.db.repository.hist.CdClBatchRepositoryHist;
import com.st1.itx.db.service.CdClBatchService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdClBatchService")
@Repository
public class CdClBatchServiceImpl extends ASpringJpaParm implements CdClBatchService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdClBatchRepository cdClBatchRepos;

  @Autowired
  private CdClBatchRepositoryDay cdClBatchReposDay;

  @Autowired
  private CdClBatchRepositoryMon cdClBatchReposMon;

  @Autowired
  private CdClBatchRepositoryHist cdClBatchReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdClBatchRepos);
    org.junit.Assert.assertNotNull(cdClBatchReposDay);
    org.junit.Assert.assertNotNull(cdClBatchReposMon);
    org.junit.Assert.assertNotNull(cdClBatchReposHist);
  }

  @Override
  public CdClBatch findById(int applNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + applNo);
    Optional<CdClBatch> cdClBatch = null;
    if (dbName.equals(ContentName.onDay))
      cdClBatch = cdClBatchReposDay.findById(applNo);
    else if (dbName.equals(ContentName.onMon))
      cdClBatch = cdClBatchReposMon.findById(applNo);
    else if (dbName.equals(ContentName.onHist))
      cdClBatch = cdClBatchReposHist.findById(applNo);
    else 
      cdClBatch = cdClBatchRepos.findById(applNo);
    CdClBatch obj = cdClBatch.isPresent() ? cdClBatch.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdClBatch> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdClBatch> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ApplNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ApplNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdClBatchReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdClBatchReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdClBatchReposHist.findAll(pageable);
    else 
      slice = cdClBatchRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdClBatch holdById(int applNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + applNo);
    Optional<CdClBatch> cdClBatch = null;
    if (dbName.equals(ContentName.onDay))
      cdClBatch = cdClBatchReposDay.findByApplNo(applNo);
    else if (dbName.equals(ContentName.onMon))
      cdClBatch = cdClBatchReposMon.findByApplNo(applNo);
    else if (dbName.equals(ContentName.onHist))
      cdClBatch = cdClBatchReposHist.findByApplNo(applNo);
    else 
      cdClBatch = cdClBatchRepos.findByApplNo(applNo);
    return cdClBatch.isPresent() ? cdClBatch.get() : null;
  }

  @Override
  public CdClBatch holdById(CdClBatch cdClBatch, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdClBatch.getApplNo());
    Optional<CdClBatch> cdClBatchT = null;
    if (dbName.equals(ContentName.onDay))
      cdClBatchT = cdClBatchReposDay.findByApplNo(cdClBatch.getApplNo());
    else if (dbName.equals(ContentName.onMon))
      cdClBatchT = cdClBatchReposMon.findByApplNo(cdClBatch.getApplNo());
    else if (dbName.equals(ContentName.onHist))
      cdClBatchT = cdClBatchReposHist.findByApplNo(cdClBatch.getApplNo());
    else 
      cdClBatchT = cdClBatchRepos.findByApplNo(cdClBatch.getApplNo());
    return cdClBatchT.isPresent() ? cdClBatchT.get() : null;
  }

  @Override
  public CdClBatch insert(CdClBatch cdClBatch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdClBatch.getApplNo());
    if (this.findById(cdClBatch.getApplNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdClBatch.setCreateEmpNo(empNot);

    if(cdClBatch.getLastUpdateEmpNo() == null || cdClBatch.getLastUpdateEmpNo().isEmpty())
      cdClBatch.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdClBatchReposDay.saveAndFlush(cdClBatch);	
    else if (dbName.equals(ContentName.onMon))
      return cdClBatchReposMon.saveAndFlush(cdClBatch);
    else if (dbName.equals(ContentName.onHist))
      return cdClBatchReposHist.saveAndFlush(cdClBatch);
    else 
    return cdClBatchRepos.saveAndFlush(cdClBatch);
  }

  @Override
  public CdClBatch update(CdClBatch cdClBatch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdClBatch.getApplNo());
    if (!empNot.isEmpty())
      cdClBatch.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdClBatchReposDay.saveAndFlush(cdClBatch);	
    else if (dbName.equals(ContentName.onMon))
      return cdClBatchReposMon.saveAndFlush(cdClBatch);
    else if (dbName.equals(ContentName.onHist))
      return cdClBatchReposHist.saveAndFlush(cdClBatch);
    else 
    return cdClBatchRepos.saveAndFlush(cdClBatch);
  }

  @Override
  public CdClBatch update2(CdClBatch cdClBatch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdClBatch.getApplNo());
    if (!empNot.isEmpty())
      cdClBatch.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdClBatchReposDay.saveAndFlush(cdClBatch);	
    else if (dbName.equals(ContentName.onMon))
      cdClBatchReposMon.saveAndFlush(cdClBatch);
    else if (dbName.equals(ContentName.onHist))
        cdClBatchReposHist.saveAndFlush(cdClBatch);
    else 
      cdClBatchRepos.saveAndFlush(cdClBatch);	
    return this.findById(cdClBatch.getApplNo());
  }

  @Override
  public void delete(CdClBatch cdClBatch, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdClBatch.getApplNo());
    if (dbName.equals(ContentName.onDay)) {
      cdClBatchReposDay.delete(cdClBatch);	
      cdClBatchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdClBatchReposMon.delete(cdClBatch);	
      cdClBatchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdClBatchReposHist.delete(cdClBatch);
      cdClBatchReposHist.flush();
    }
    else {
      cdClBatchRepos.delete(cdClBatch);
      cdClBatchRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdClBatch> cdClBatch, TitaVo... titaVo) throws DBException {
    if (cdClBatch == null || cdClBatch.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdClBatch t : cdClBatch){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdClBatch = cdClBatchReposDay.saveAll(cdClBatch);	
      cdClBatchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdClBatch = cdClBatchReposMon.saveAll(cdClBatch);	
      cdClBatchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdClBatch = cdClBatchReposHist.saveAll(cdClBatch);
      cdClBatchReposHist.flush();
    }
    else {
      cdClBatch = cdClBatchRepos.saveAll(cdClBatch);
      cdClBatchRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdClBatch> cdClBatch, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdClBatch == null || cdClBatch.size() == 0)
      throw new DBException(6);

    for (CdClBatch t : cdClBatch) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdClBatch = cdClBatchReposDay.saveAll(cdClBatch);	
      cdClBatchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdClBatch = cdClBatchReposMon.saveAll(cdClBatch);	
      cdClBatchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdClBatch = cdClBatchReposHist.saveAll(cdClBatch);
      cdClBatchReposHist.flush();
    }
    else {
      cdClBatch = cdClBatchRepos.saveAll(cdClBatch);
      cdClBatchRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdClBatch> cdClBatch, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdClBatch == null || cdClBatch.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdClBatchReposDay.deleteAll(cdClBatch);	
      cdClBatchReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdClBatchReposMon.deleteAll(cdClBatch);	
      cdClBatchReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdClBatchReposHist.deleteAll(cdClBatch);
      cdClBatchReposHist.flush();
    }
    else {
      cdClBatchRepos.deleteAll(cdClBatch);
      cdClBatchRepos.flush();
    }
  }

}
