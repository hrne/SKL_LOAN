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
import com.st1.itx.db.domain.CdConvertCode;
import com.st1.itx.db.domain.CdConvertCodeId;
import com.st1.itx.db.repository.online.CdConvertCodeRepository;
import com.st1.itx.db.repository.day.CdConvertCodeRepositoryDay;
import com.st1.itx.db.repository.mon.CdConvertCodeRepositoryMon;
import com.st1.itx.db.repository.hist.CdConvertCodeRepositoryHist;
import com.st1.itx.db.service.CdConvertCodeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdConvertCodeService")
@Repository
public class CdConvertCodeServiceImpl extends ASpringJpaParm implements CdConvertCodeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdConvertCodeRepository cdConvertCodeRepos;

  @Autowired
  private CdConvertCodeRepositoryDay cdConvertCodeReposDay;

  @Autowired
  private CdConvertCodeRepositoryMon cdConvertCodeReposMon;

  @Autowired
  private CdConvertCodeRepositoryHist cdConvertCodeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdConvertCodeRepos);
    org.junit.Assert.assertNotNull(cdConvertCodeReposDay);
    org.junit.Assert.assertNotNull(cdConvertCodeReposMon);
    org.junit.Assert.assertNotNull(cdConvertCodeReposHist);
  }

  @Override
  public CdConvertCode findById(CdConvertCodeId cdConvertCodeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdConvertCodeId);
    Optional<CdConvertCode> cdConvertCode = null;
    if (dbName.equals(ContentName.onDay))
      cdConvertCode = cdConvertCodeReposDay.findById(cdConvertCodeId);
    else if (dbName.equals(ContentName.onMon))
      cdConvertCode = cdConvertCodeReposMon.findById(cdConvertCodeId);
    else if (dbName.equals(ContentName.onHist))
      cdConvertCode = cdConvertCodeReposHist.findById(cdConvertCodeId);
    else 
      cdConvertCode = cdConvertCodeRepos.findById(cdConvertCodeId);
    CdConvertCode obj = cdConvertCode.isPresent() ? cdConvertCode.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdConvertCode> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdConvertCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CodeType", "OrgCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CodeType", "OrgCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdConvertCodeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdConvertCodeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdConvertCodeReposHist.findAll(pageable);
    else 
      slice = cdConvertCodeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdConvertCode holdById(CdConvertCodeId cdConvertCodeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdConvertCodeId);
    Optional<CdConvertCode> cdConvertCode = null;
    if (dbName.equals(ContentName.onDay))
      cdConvertCode = cdConvertCodeReposDay.findByCdConvertCodeId(cdConvertCodeId);
    else if (dbName.equals(ContentName.onMon))
      cdConvertCode = cdConvertCodeReposMon.findByCdConvertCodeId(cdConvertCodeId);
    else if (dbName.equals(ContentName.onHist))
      cdConvertCode = cdConvertCodeReposHist.findByCdConvertCodeId(cdConvertCodeId);
    else 
      cdConvertCode = cdConvertCodeRepos.findByCdConvertCodeId(cdConvertCodeId);
    return cdConvertCode.isPresent() ? cdConvertCode.get() : null;
  }

  @Override
  public CdConvertCode holdById(CdConvertCode cdConvertCode, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdConvertCode.getCdConvertCodeId());
    Optional<CdConvertCode> cdConvertCodeT = null;
    if (dbName.equals(ContentName.onDay))
      cdConvertCodeT = cdConvertCodeReposDay.findByCdConvertCodeId(cdConvertCode.getCdConvertCodeId());
    else if (dbName.equals(ContentName.onMon))
      cdConvertCodeT = cdConvertCodeReposMon.findByCdConvertCodeId(cdConvertCode.getCdConvertCodeId());
    else if (dbName.equals(ContentName.onHist))
      cdConvertCodeT = cdConvertCodeReposHist.findByCdConvertCodeId(cdConvertCode.getCdConvertCodeId());
    else 
      cdConvertCodeT = cdConvertCodeRepos.findByCdConvertCodeId(cdConvertCode.getCdConvertCodeId());
    return cdConvertCodeT.isPresent() ? cdConvertCodeT.get() : null;
  }

  @Override
  public CdConvertCode insert(CdConvertCode cdConvertCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdConvertCode.getCdConvertCodeId());
    if (this.findById(cdConvertCode.getCdConvertCodeId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdConvertCode.setCreateEmpNo(empNot);

    if(cdConvertCode.getLastUpdateEmpNo() == null || cdConvertCode.getLastUpdateEmpNo().isEmpty())
      cdConvertCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdConvertCodeReposDay.saveAndFlush(cdConvertCode);	
    else if (dbName.equals(ContentName.onMon))
      return cdConvertCodeReposMon.saveAndFlush(cdConvertCode);
    else if (dbName.equals(ContentName.onHist))
      return cdConvertCodeReposHist.saveAndFlush(cdConvertCode);
    else 
    return cdConvertCodeRepos.saveAndFlush(cdConvertCode);
  }

  @Override
  public CdConvertCode update(CdConvertCode cdConvertCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdConvertCode.getCdConvertCodeId());
    if (!empNot.isEmpty())
      cdConvertCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdConvertCodeReposDay.saveAndFlush(cdConvertCode);	
    else if (dbName.equals(ContentName.onMon))
      return cdConvertCodeReposMon.saveAndFlush(cdConvertCode);
    else if (dbName.equals(ContentName.onHist))
      return cdConvertCodeReposHist.saveAndFlush(cdConvertCode);
    else 
    return cdConvertCodeRepos.saveAndFlush(cdConvertCode);
  }

  @Override
  public CdConvertCode update2(CdConvertCode cdConvertCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdConvertCode.getCdConvertCodeId());
    if (!empNot.isEmpty())
      cdConvertCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdConvertCodeReposDay.saveAndFlush(cdConvertCode);	
    else if (dbName.equals(ContentName.onMon))
      cdConvertCodeReposMon.saveAndFlush(cdConvertCode);
    else if (dbName.equals(ContentName.onHist))
        cdConvertCodeReposHist.saveAndFlush(cdConvertCode);
    else 
      cdConvertCodeRepos.saveAndFlush(cdConvertCode);	
    return this.findById(cdConvertCode.getCdConvertCodeId());
  }

  @Override
  public void delete(CdConvertCode cdConvertCode, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdConvertCode.getCdConvertCodeId());
    if (dbName.equals(ContentName.onDay)) {
      cdConvertCodeReposDay.delete(cdConvertCode);	
      cdConvertCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdConvertCodeReposMon.delete(cdConvertCode);	
      cdConvertCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdConvertCodeReposHist.delete(cdConvertCode);
      cdConvertCodeReposHist.flush();
    }
    else {
      cdConvertCodeRepos.delete(cdConvertCode);
      cdConvertCodeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdConvertCode> cdConvertCode, TitaVo... titaVo) throws DBException {
    if (cdConvertCode == null || cdConvertCode.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdConvertCode t : cdConvertCode){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdConvertCode = cdConvertCodeReposDay.saveAll(cdConvertCode);	
      cdConvertCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdConvertCode = cdConvertCodeReposMon.saveAll(cdConvertCode);	
      cdConvertCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdConvertCode = cdConvertCodeReposHist.saveAll(cdConvertCode);
      cdConvertCodeReposHist.flush();
    }
    else {
      cdConvertCode = cdConvertCodeRepos.saveAll(cdConvertCode);
      cdConvertCodeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdConvertCode> cdConvertCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdConvertCode == null || cdConvertCode.size() == 0)
      throw new DBException(6);

    for (CdConvertCode t : cdConvertCode) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdConvertCode = cdConvertCodeReposDay.saveAll(cdConvertCode);	
      cdConvertCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdConvertCode = cdConvertCodeReposMon.saveAll(cdConvertCode);	
      cdConvertCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdConvertCode = cdConvertCodeReposHist.saveAll(cdConvertCode);
      cdConvertCodeReposHist.flush();
    }
    else {
      cdConvertCode = cdConvertCodeRepos.saveAll(cdConvertCode);
      cdConvertCodeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdConvertCode> cdConvertCode, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdConvertCode == null || cdConvertCode.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdConvertCodeReposDay.deleteAll(cdConvertCode);	
      cdConvertCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdConvertCodeReposMon.deleteAll(cdConvertCode);	
      cdConvertCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdConvertCodeReposHist.deleteAll(cdConvertCode);
      cdConvertCodeReposHist.flush();
    }
    else {
      cdConvertCodeRepos.deleteAll(cdConvertCode);
      cdConvertCodeRepos.flush();
    }
  }

}
