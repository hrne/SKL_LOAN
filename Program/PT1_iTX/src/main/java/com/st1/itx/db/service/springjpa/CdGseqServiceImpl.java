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
import com.st1.itx.db.domain.CdGseq;
import com.st1.itx.db.domain.CdGseqId;
import com.st1.itx.db.repository.online.CdGseqRepository;
import com.st1.itx.db.repository.day.CdGseqRepositoryDay;
import com.st1.itx.db.repository.mon.CdGseqRepositoryMon;
import com.st1.itx.db.repository.hist.CdGseqRepositoryHist;
import com.st1.itx.db.service.CdGseqService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdGseqService")
@Repository
public class CdGseqServiceImpl extends ASpringJpaParm implements CdGseqService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdGseqRepository cdGseqRepos;

  @Autowired
  private CdGseqRepositoryDay cdGseqReposDay;

  @Autowired
  private CdGseqRepositoryMon cdGseqReposMon;

  @Autowired
  private CdGseqRepositoryHist cdGseqReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdGseqRepos);
    org.junit.Assert.assertNotNull(cdGseqReposDay);
    org.junit.Assert.assertNotNull(cdGseqReposMon);
    org.junit.Assert.assertNotNull(cdGseqReposHist);
  }

  @Override
  public CdGseq findById(CdGseqId cdGseqId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdGseqId);
    Optional<CdGseq> cdGseq = null;
    if (dbName.equals(ContentName.onDay))
      cdGseq = cdGseqReposDay.findById(cdGseqId);
    else if (dbName.equals(ContentName.onMon))
      cdGseq = cdGseqReposMon.findById(cdGseqId);
    else if (dbName.equals(ContentName.onHist))
      cdGseq = cdGseqReposHist.findById(cdGseqId);
    else 
      cdGseq = cdGseqRepos.findById(cdGseqId);
    CdGseq obj = cdGseq.isPresent() ? cdGseq.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdGseq> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdGseq> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "GseqDate", "GseqCode", "GseqType", "GseqKind"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "GseqDate", "GseqCode", "GseqType", "GseqKind"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdGseqReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdGseqReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdGseqReposHist.findAll(pageable);
    else 
      slice = cdGseqRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdGseq holdById(CdGseqId cdGseqId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdGseqId);
    Optional<CdGseq> cdGseq = null;
    if (dbName.equals(ContentName.onDay))
      cdGseq = cdGseqReposDay.findByCdGseqId(cdGseqId);
    else if (dbName.equals(ContentName.onMon))
      cdGseq = cdGseqReposMon.findByCdGseqId(cdGseqId);
    else if (dbName.equals(ContentName.onHist))
      cdGseq = cdGseqReposHist.findByCdGseqId(cdGseqId);
    else 
      cdGseq = cdGseqRepos.findByCdGseqId(cdGseqId);
    return cdGseq.isPresent() ? cdGseq.get() : null;
  }

  @Override
  public CdGseq holdById(CdGseq cdGseq, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdGseq.getCdGseqId());
    Optional<CdGseq> cdGseqT = null;
    if (dbName.equals(ContentName.onDay))
      cdGseqT = cdGseqReposDay.findByCdGseqId(cdGseq.getCdGseqId());
    else if (dbName.equals(ContentName.onMon))
      cdGseqT = cdGseqReposMon.findByCdGseqId(cdGseq.getCdGseqId());
    else if (dbName.equals(ContentName.onHist))
      cdGseqT = cdGseqReposHist.findByCdGseqId(cdGseq.getCdGseqId());
    else 
      cdGseqT = cdGseqRepos.findByCdGseqId(cdGseq.getCdGseqId());
    return cdGseqT.isPresent() ? cdGseqT.get() : null;
  }

  @Override
  public CdGseq insert(CdGseq cdGseq, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdGseq.getCdGseqId());
    if (this.findById(cdGseq.getCdGseqId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdGseq.setCreateEmpNo(empNot);

    if(cdGseq.getLastUpdateEmpNo() == null || cdGseq.getLastUpdateEmpNo().isEmpty())
      cdGseq.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdGseqReposDay.saveAndFlush(cdGseq);	
    else if (dbName.equals(ContentName.onMon))
      return cdGseqReposMon.saveAndFlush(cdGseq);
    else if (dbName.equals(ContentName.onHist))
      return cdGseqReposHist.saveAndFlush(cdGseq);
    else 
    return cdGseqRepos.saveAndFlush(cdGseq);
  }

  @Override
  public CdGseq update(CdGseq cdGseq, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdGseq.getCdGseqId());
    if (!empNot.isEmpty())
      cdGseq.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdGseqReposDay.saveAndFlush(cdGseq);	
    else if (dbName.equals(ContentName.onMon))
      return cdGseqReposMon.saveAndFlush(cdGseq);
    else if (dbName.equals(ContentName.onHist))
      return cdGseqReposHist.saveAndFlush(cdGseq);
    else 
    return cdGseqRepos.saveAndFlush(cdGseq);
  }

  @Override
  public CdGseq update2(CdGseq cdGseq, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdGseq.getCdGseqId());
    if (!empNot.isEmpty())
      cdGseq.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdGseqReposDay.saveAndFlush(cdGseq);	
    else if (dbName.equals(ContentName.onMon))
      cdGseqReposMon.saveAndFlush(cdGseq);
    else if (dbName.equals(ContentName.onHist))
        cdGseqReposHist.saveAndFlush(cdGseq);
    else 
      cdGseqRepos.saveAndFlush(cdGseq);	
    return this.findById(cdGseq.getCdGseqId());
  }

  @Override
  public void delete(CdGseq cdGseq, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdGseq.getCdGseqId());
    if (dbName.equals(ContentName.onDay)) {
      cdGseqReposDay.delete(cdGseq);	
      cdGseqReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdGseqReposMon.delete(cdGseq);	
      cdGseqReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdGseqReposHist.delete(cdGseq);
      cdGseqReposHist.flush();
    }
    else {
      cdGseqRepos.delete(cdGseq);
      cdGseqRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdGseq> cdGseq, TitaVo... titaVo) throws DBException {
    if (cdGseq == null || cdGseq.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdGseq t : cdGseq){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdGseq = cdGseqReposDay.saveAll(cdGseq);	
      cdGseqReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdGseq = cdGseqReposMon.saveAll(cdGseq);	
      cdGseqReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdGseq = cdGseqReposHist.saveAll(cdGseq);
      cdGseqReposHist.flush();
    }
    else {
      cdGseq = cdGseqRepos.saveAll(cdGseq);
      cdGseqRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdGseq> cdGseq, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdGseq == null || cdGseq.size() == 0)
      throw new DBException(6);

    for (CdGseq t : cdGseq) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdGseq = cdGseqReposDay.saveAll(cdGseq);	
      cdGseqReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdGseq = cdGseqReposMon.saveAll(cdGseq);	
      cdGseqReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdGseq = cdGseqReposHist.saveAll(cdGseq);
      cdGseqReposHist.flush();
    }
    else {
      cdGseq = cdGseqRepos.saveAll(cdGseq);
      cdGseqRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdGseq> cdGseq, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdGseq == null || cdGseq.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdGseqReposDay.deleteAll(cdGseq);	
      cdGseqReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdGseqReposMon.deleteAll(cdGseq);	
      cdGseqReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdGseqReposHist.deleteAll(cdGseq);
      cdGseqReposHist.flush();
    }
    else {
      cdGseqRepos.deleteAll(cdGseq);
      cdGseqRepos.flush();
    }
  }

}
