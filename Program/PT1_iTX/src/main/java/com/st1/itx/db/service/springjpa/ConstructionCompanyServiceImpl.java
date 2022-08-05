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
import com.st1.itx.db.domain.ConstructionCompany;
import com.st1.itx.db.repository.online.ConstructionCompanyRepository;
import com.st1.itx.db.repository.day.ConstructionCompanyRepositoryDay;
import com.st1.itx.db.repository.mon.ConstructionCompanyRepositoryMon;
import com.st1.itx.db.repository.hist.ConstructionCompanyRepositoryHist;
import com.st1.itx.db.service.ConstructionCompanyService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("constructionCompanyService")
@Repository
public class ConstructionCompanyServiceImpl extends ASpringJpaParm implements ConstructionCompanyService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ConstructionCompanyRepository constructionCompanyRepos;

  @Autowired
  private ConstructionCompanyRepositoryDay constructionCompanyReposDay;

  @Autowired
  private ConstructionCompanyRepositoryMon constructionCompanyReposMon;

  @Autowired
  private ConstructionCompanyRepositoryHist constructionCompanyReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(constructionCompanyRepos);
    org.junit.Assert.assertNotNull(constructionCompanyReposDay);
    org.junit.Assert.assertNotNull(constructionCompanyReposMon);
    org.junit.Assert.assertNotNull(constructionCompanyReposHist);
  }

  @Override
  public ConstructionCompany findById(int custNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + custNo);
    Optional<ConstructionCompany> constructionCompany = null;
    if (dbName.equals(ContentName.onDay))
      constructionCompany = constructionCompanyReposDay.findById(custNo);
    else if (dbName.equals(ContentName.onMon))
      constructionCompany = constructionCompanyReposMon.findById(custNo);
    else if (dbName.equals(ContentName.onHist))
      constructionCompany = constructionCompanyReposHist.findById(custNo);
    else 
      constructionCompany = constructionCompanyRepos.findById(custNo);
    ConstructionCompany obj = constructionCompany.isPresent() ? constructionCompany.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ConstructionCompany> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ConstructionCompany> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = constructionCompanyReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = constructionCompanyReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = constructionCompanyReposHist.findAll(pageable);
    else 
      slice = constructionCompanyRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ConstructionCompany holdById(int custNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + custNo);
    Optional<ConstructionCompany> constructionCompany = null;
    if (dbName.equals(ContentName.onDay))
      constructionCompany = constructionCompanyReposDay.findByCustNo(custNo);
    else if (dbName.equals(ContentName.onMon))
      constructionCompany = constructionCompanyReposMon.findByCustNo(custNo);
    else if (dbName.equals(ContentName.onHist))
      constructionCompany = constructionCompanyReposHist.findByCustNo(custNo);
    else 
      constructionCompany = constructionCompanyRepos.findByCustNo(custNo);
    return constructionCompany.isPresent() ? constructionCompany.get() : null;
  }

  @Override
  public ConstructionCompany holdById(ConstructionCompany constructionCompany, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + constructionCompany.getCustNo());
    Optional<ConstructionCompany> constructionCompanyT = null;
    if (dbName.equals(ContentName.onDay))
      constructionCompanyT = constructionCompanyReposDay.findByCustNo(constructionCompany.getCustNo());
    else if (dbName.equals(ContentName.onMon))
      constructionCompanyT = constructionCompanyReposMon.findByCustNo(constructionCompany.getCustNo());
    else if (dbName.equals(ContentName.onHist))
      constructionCompanyT = constructionCompanyReposHist.findByCustNo(constructionCompany.getCustNo());
    else 
      constructionCompanyT = constructionCompanyRepos.findByCustNo(constructionCompany.getCustNo());
    return constructionCompanyT.isPresent() ? constructionCompanyT.get() : null;
  }

  @Override
  public ConstructionCompany insert(ConstructionCompany constructionCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + constructionCompany.getCustNo());
    if (this.findById(constructionCompany.getCustNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      constructionCompany.setCreateEmpNo(empNot);

    if(constructionCompany.getLastUpdateEmpNo() == null || constructionCompany.getLastUpdateEmpNo().isEmpty())
      constructionCompany.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return constructionCompanyReposDay.saveAndFlush(constructionCompany);	
    else if (dbName.equals(ContentName.onMon))
      return constructionCompanyReposMon.saveAndFlush(constructionCompany);
    else if (dbName.equals(ContentName.onHist))
      return constructionCompanyReposHist.saveAndFlush(constructionCompany);
    else 
    return constructionCompanyRepos.saveAndFlush(constructionCompany);
  }

  @Override
  public ConstructionCompany update(ConstructionCompany constructionCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + constructionCompany.getCustNo());
    if (!empNot.isEmpty())
      constructionCompany.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return constructionCompanyReposDay.saveAndFlush(constructionCompany);	
    else if (dbName.equals(ContentName.onMon))
      return constructionCompanyReposMon.saveAndFlush(constructionCompany);
    else if (dbName.equals(ContentName.onHist))
      return constructionCompanyReposHist.saveAndFlush(constructionCompany);
    else 
    return constructionCompanyRepos.saveAndFlush(constructionCompany);
  }

  @Override
  public ConstructionCompany update2(ConstructionCompany constructionCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + constructionCompany.getCustNo());
    if (!empNot.isEmpty())
      constructionCompany.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      constructionCompanyReposDay.saveAndFlush(constructionCompany);	
    else if (dbName.equals(ContentName.onMon))
      constructionCompanyReposMon.saveAndFlush(constructionCompany);
    else if (dbName.equals(ContentName.onHist))
        constructionCompanyReposHist.saveAndFlush(constructionCompany);
    else 
      constructionCompanyRepos.saveAndFlush(constructionCompany);	
    return this.findById(constructionCompany.getCustNo());
  }

  @Override
  public void delete(ConstructionCompany constructionCompany, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + constructionCompany.getCustNo());
    if (dbName.equals(ContentName.onDay)) {
      constructionCompanyReposDay.delete(constructionCompany);	
      constructionCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      constructionCompanyReposMon.delete(constructionCompany);	
      constructionCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      constructionCompanyReposHist.delete(constructionCompany);
      constructionCompanyReposHist.flush();
    }
    else {
      constructionCompanyRepos.delete(constructionCompany);
      constructionCompanyRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ConstructionCompany> constructionCompany, TitaVo... titaVo) throws DBException {
    if (constructionCompany == null || constructionCompany.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (ConstructionCompany t : constructionCompany){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      constructionCompany = constructionCompanyReposDay.saveAll(constructionCompany);	
      constructionCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      constructionCompany = constructionCompanyReposMon.saveAll(constructionCompany);	
      constructionCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      constructionCompany = constructionCompanyReposHist.saveAll(constructionCompany);
      constructionCompanyReposHist.flush();
    }
    else {
      constructionCompany = constructionCompanyRepos.saveAll(constructionCompany);
      constructionCompanyRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ConstructionCompany> constructionCompany, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (constructionCompany == null || constructionCompany.size() == 0)
      throw new DBException(6);

    for (ConstructionCompany t : constructionCompany) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      constructionCompany = constructionCompanyReposDay.saveAll(constructionCompany);	
      constructionCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      constructionCompany = constructionCompanyReposMon.saveAll(constructionCompany);	
      constructionCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      constructionCompany = constructionCompanyReposHist.saveAll(constructionCompany);
      constructionCompanyReposHist.flush();
    }
    else {
      constructionCompany = constructionCompanyRepos.saveAll(constructionCompany);
      constructionCompanyRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ConstructionCompany> constructionCompany, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (constructionCompany == null || constructionCompany.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      constructionCompanyReposDay.deleteAll(constructionCompany);	
      constructionCompanyReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      constructionCompanyReposMon.deleteAll(constructionCompany);	
      constructionCompanyReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      constructionCompanyReposHist.deleteAll(constructionCompany);
      constructionCompanyReposHist.flush();
    }
    else {
      constructionCompanyRepos.deleteAll(constructionCompany);
      constructionCompanyRepos.flush();
    }
  }

}
