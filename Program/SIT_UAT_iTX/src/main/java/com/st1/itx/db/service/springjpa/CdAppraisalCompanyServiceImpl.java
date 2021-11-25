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
import com.st1.itx.db.domain.CdAppraisalCompany;
import com.st1.itx.db.repository.online.CdAppraisalCompanyRepository;
import com.st1.itx.db.repository.day.CdAppraisalCompanyRepositoryDay;
import com.st1.itx.db.repository.mon.CdAppraisalCompanyRepositoryMon;
import com.st1.itx.db.repository.hist.CdAppraisalCompanyRepositoryHist;
import com.st1.itx.db.service.CdAppraisalCompanyService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdAppraisalCompanyService")
@Repository
public class CdAppraisalCompanyServiceImpl extends ASpringJpaParm implements CdAppraisalCompanyService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdAppraisalCompanyRepository cdAppraisalCompanyRepos;

  @Autowired
  private CdAppraisalCompanyRepositoryDay cdAppraisalCompanyReposDay;

  @Autowired
  private CdAppraisalCompanyRepositoryMon cdAppraisalCompanyReposMon;

  @Autowired
  private CdAppraisalCompanyRepositoryHist cdAppraisalCompanyReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdAppraisalCompanyRepos);
    org.junit.Assert.assertNotNull(cdAppraisalCompanyReposDay);
    org.junit.Assert.assertNotNull(cdAppraisalCompanyReposMon);
    org.junit.Assert.assertNotNull(cdAppraisalCompanyReposHist);
  }

  @Override
  public CdAppraisalCompany findById(String appraisalCompany, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + appraisalCompany);
    Optional<CdAppraisalCompany> cdAppraisalCompany = null;
    if (dbName.equals(ContentName.onDay))
      cdAppraisalCompany = cdAppraisalCompanyReposDay.findById(appraisalCompany);
    else if (dbName.equals(ContentName.onMon))
      cdAppraisalCompany = cdAppraisalCompanyReposMon.findById(appraisalCompany);
    else if (dbName.equals(ContentName.onHist))
      cdAppraisalCompany = cdAppraisalCompanyReposHist.findById(appraisalCompany);
    else 
      cdAppraisalCompany = cdAppraisalCompanyRepos.findById(appraisalCompany);
    CdAppraisalCompany obj = cdAppraisalCompany.isPresent() ? cdAppraisalCompany.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdAppraisalCompany> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdAppraisalCompany> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AppraisalCompany"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AppraisalCompany"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdAppraisalCompanyReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdAppraisalCompanyReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdAppraisalCompanyReposHist.findAll(pageable);
    else 
      slice = cdAppraisalCompanyRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdAppraisalCompany holdById(String appraisalCompany, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + appraisalCompany);
    Optional<CdAppraisalCompany> cdAppraisalCompany = null;
    if (dbName.equals(ContentName.onDay))
      cdAppraisalCompany = cdAppraisalCompanyReposDay.findByAppraisalCompany(appraisalCompany);
    else if (dbName.equals(ContentName.onMon))
      cdAppraisalCompany = cdAppraisalCompanyReposMon.findByAppraisalCompany(appraisalCompany);
    else if (dbName.equals(ContentName.onHist))
      cdAppraisalCompany = cdAppraisalCompanyReposHist.findByAppraisalCompany(appraisalCompany);
    else 
      cdAppraisalCompany = cdAppraisalCompanyRepos.findByAppraisalCompany(appraisalCompany);
    return cdAppraisalCompany.isPresent() ? cdAppraisalCompany.get() : null;
  }

  @Override
  public CdAppraisalCompany holdById(CdAppraisalCompany cdAppraisalCompany, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdAppraisalCompany.getAppraisalCompany());
    Optional<CdAppraisalCompany> cdAppraisalCompanyT = null;
    if (dbName.equals(ContentName.onDay))
      cdAppraisalCompanyT = cdAppraisalCompanyReposDay.findByAppraisalCompany(cdAppraisalCompany.getAppraisalCompany());
    else if (dbName.equals(ContentName.onMon))
      cdAppraisalCompanyT = cdAppraisalCompanyReposMon.findByAppraisalCompany(cdAppraisalCompany.getAppraisalCompany());
    else if (dbName.equals(ContentName.onHist))
      cdAppraisalCompanyT = cdAppraisalCompanyReposHist.findByAppraisalCompany(cdAppraisalCompany.getAppraisalCompany());
    else 
      cdAppraisalCompanyT = cdAppraisalCompanyRepos.findByAppraisalCompany(cdAppraisalCompany.getAppraisalCompany());
    return cdAppraisalCompanyT.isPresent() ? cdAppraisalCompanyT.get() : null;
  }

  @Override
  public CdAppraisalCompany insert(CdAppraisalCompany cdAppraisalCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdAppraisalCompany.getAppraisalCompany());
    if (this.findById(cdAppraisalCompany.getAppraisalCompany()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdAppraisalCompany.setCreateEmpNo(empNot);

    if(cdAppraisalCompany.getLastUpdateEmpNo() == null || cdAppraisalCompany.getLastUpdateEmpNo().isEmpty())
      cdAppraisalCompany.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdAppraisalCompanyReposDay.saveAndFlush(cdAppraisalCompany);	
    else if (dbName.equals(ContentName.onMon))
      return cdAppraisalCompanyReposMon.saveAndFlush(cdAppraisalCompany);
    else if (dbName.equals(ContentName.onHist))
      return cdAppraisalCompanyReposHist.saveAndFlush(cdAppraisalCompany);
    else 
    return cdAppraisalCompanyRepos.saveAndFlush(cdAppraisalCompany);
  }

  @Override
  public CdAppraisalCompany update(CdAppraisalCompany cdAppraisalCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdAppraisalCompany.getAppraisalCompany());
    if (!empNot.isEmpty())
      cdAppraisalCompany.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdAppraisalCompanyReposDay.saveAndFlush(cdAppraisalCompany);	
    else if (dbName.equals(ContentName.onMon))
      return cdAppraisalCompanyReposMon.saveAndFlush(cdAppraisalCompany);
    else if (dbName.equals(ContentName.onHist))
      return cdAppraisalCompanyReposHist.saveAndFlush(cdAppraisalCompany);
    else 
    return cdAppraisalCompanyRepos.saveAndFlush(cdAppraisalCompany);
  }

  @Override
  public CdAppraisalCompany update2(CdAppraisalCompany cdAppraisalCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdAppraisalCompany.getAppraisalCompany());
    if (!empNot.isEmpty())
      cdAppraisalCompany.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdAppraisalCompanyReposDay.saveAndFlush(cdAppraisalCompany);	
    else if (dbName.equals(ContentName.onMon))
      cdAppraisalCompanyReposMon.saveAndFlush(cdAppraisalCompany);
    else if (dbName.equals(ContentName.onHist))
        cdAppraisalCompanyReposHist.saveAndFlush(cdAppraisalCompany);
    else 
      cdAppraisalCompanyRepos.saveAndFlush(cdAppraisalCompany);	
    return this.findById(cdAppraisalCompany.getAppraisalCompany());
  }

  @Override
  public void delete(CdAppraisalCompany cdAppraisalCompany, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdAppraisalCompany.getAppraisalCompany());
    if (dbName.equals(ContentName.onDay)) {
      cdAppraisalCompanyReposDay.delete(cdAppraisalCompany);	
      cdAppraisalCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAppraisalCompanyReposMon.delete(cdAppraisalCompany);	
      cdAppraisalCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAppraisalCompanyReposHist.delete(cdAppraisalCompany);
      cdAppraisalCompanyReposHist.flush();
    }
    else {
      cdAppraisalCompanyRepos.delete(cdAppraisalCompany);
      cdAppraisalCompanyRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdAppraisalCompany> cdAppraisalCompany, TitaVo... titaVo) throws DBException {
    if (cdAppraisalCompany == null || cdAppraisalCompany.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdAppraisalCompany t : cdAppraisalCompany){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdAppraisalCompany = cdAppraisalCompanyReposDay.saveAll(cdAppraisalCompany);	
      cdAppraisalCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAppraisalCompany = cdAppraisalCompanyReposMon.saveAll(cdAppraisalCompany);	
      cdAppraisalCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAppraisalCompany = cdAppraisalCompanyReposHist.saveAll(cdAppraisalCompany);
      cdAppraisalCompanyReposHist.flush();
    }
    else {
      cdAppraisalCompany = cdAppraisalCompanyRepos.saveAll(cdAppraisalCompany);
      cdAppraisalCompanyRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdAppraisalCompany> cdAppraisalCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdAppraisalCompany == null || cdAppraisalCompany.size() == 0)
      throw new DBException(6);

    for (CdAppraisalCompany t : cdAppraisalCompany) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdAppraisalCompany = cdAppraisalCompanyReposDay.saveAll(cdAppraisalCompany);	
      cdAppraisalCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAppraisalCompany = cdAppraisalCompanyReposMon.saveAll(cdAppraisalCompany);	
      cdAppraisalCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAppraisalCompany = cdAppraisalCompanyReposHist.saveAll(cdAppraisalCompany);
      cdAppraisalCompanyReposHist.flush();
    }
    else {
      cdAppraisalCompany = cdAppraisalCompanyRepos.saveAll(cdAppraisalCompany);
      cdAppraisalCompanyRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdAppraisalCompany> cdAppraisalCompany, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdAppraisalCompany == null || cdAppraisalCompany.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdAppraisalCompanyReposDay.deleteAll(cdAppraisalCompany);	
      cdAppraisalCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAppraisalCompanyReposMon.deleteAll(cdAppraisalCompany);	
      cdAppraisalCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAppraisalCompanyReposHist.deleteAll(cdAppraisalCompany);
      cdAppraisalCompanyReposHist.flush();
    }
    else {
      cdAppraisalCompanyRepos.deleteAll(cdAppraisalCompany);
      cdAppraisalCompanyRepos.flush();
    }
  }

}
