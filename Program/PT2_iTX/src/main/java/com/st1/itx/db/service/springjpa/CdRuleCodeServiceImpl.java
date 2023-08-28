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
import com.st1.itx.db.domain.CdRuleCode;
import com.st1.itx.db.repository.online.CdRuleCodeRepository;
import com.st1.itx.db.repository.day.CdRuleCodeRepositoryDay;
import com.st1.itx.db.repository.mon.CdRuleCodeRepositoryMon;
import com.st1.itx.db.repository.hist.CdRuleCodeRepositoryHist;
import com.st1.itx.db.service.CdRuleCodeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdRuleCodeService")
@Repository
public class CdRuleCodeServiceImpl extends ASpringJpaParm implements CdRuleCodeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdRuleCodeRepository cdRuleCodeRepos;

  @Autowired
  private CdRuleCodeRepositoryDay cdRuleCodeReposDay;

  @Autowired
  private CdRuleCodeRepositoryMon cdRuleCodeReposMon;

  @Autowired
  private CdRuleCodeRepositoryHist cdRuleCodeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdRuleCodeRepos);
    org.junit.Assert.assertNotNull(cdRuleCodeReposDay);
    org.junit.Assert.assertNotNull(cdRuleCodeReposMon);
    org.junit.Assert.assertNotNull(cdRuleCodeReposHist);
  }

  @Override
  public CdRuleCode findById(String ruleCode, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + ruleCode);
    Optional<CdRuleCode> cdRuleCode = null;
    if (dbName.equals(ContentName.onDay))
      cdRuleCode = cdRuleCodeReposDay.findById(ruleCode);
    else if (dbName.equals(ContentName.onMon))
      cdRuleCode = cdRuleCodeReposMon.findById(ruleCode);
    else if (dbName.equals(ContentName.onHist))
      cdRuleCode = cdRuleCodeReposHist.findById(ruleCode);
    else 
      cdRuleCode = cdRuleCodeRepos.findById(ruleCode);
    CdRuleCode obj = cdRuleCode.isPresent() ? cdRuleCode.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdRuleCode> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdRuleCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "RuleCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "RuleCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdRuleCodeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdRuleCodeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdRuleCodeReposHist.findAll(pageable);
    else 
      slice = cdRuleCodeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdRuleCode> findCodeDate(int ruleStDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdRuleCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCodeDate " + dbName + " : " + "ruleStDate_0 : " + ruleStDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdRuleCodeReposDay.findAllByRuleStDateGreaterThanEqualOrderByRuleStDateAsc(ruleStDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdRuleCodeReposMon.findAllByRuleStDateGreaterThanEqualOrderByRuleStDateAsc(ruleStDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdRuleCodeReposHist.findAllByRuleStDateGreaterThanEqualOrderByRuleStDateAsc(ruleStDate_0, pageable);
    else 
      slice = cdRuleCodeRepos.findAllByRuleStDateGreaterThanEqualOrderByRuleStDateAsc(ruleStDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdRuleCode holdById(String ruleCode, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + ruleCode);
    Optional<CdRuleCode> cdRuleCode = null;
    if (dbName.equals(ContentName.onDay))
      cdRuleCode = cdRuleCodeReposDay.findByRuleCode(ruleCode);
    else if (dbName.equals(ContentName.onMon))
      cdRuleCode = cdRuleCodeReposMon.findByRuleCode(ruleCode);
    else if (dbName.equals(ContentName.onHist))
      cdRuleCode = cdRuleCodeReposHist.findByRuleCode(ruleCode);
    else 
      cdRuleCode = cdRuleCodeRepos.findByRuleCode(ruleCode);
    return cdRuleCode.isPresent() ? cdRuleCode.get() : null;
  }

  @Override
  public CdRuleCode holdById(CdRuleCode cdRuleCode, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdRuleCode.getRuleCode());
    Optional<CdRuleCode> cdRuleCodeT = null;
    if (dbName.equals(ContentName.onDay))
      cdRuleCodeT = cdRuleCodeReposDay.findByRuleCode(cdRuleCode.getRuleCode());
    else if (dbName.equals(ContentName.onMon))
      cdRuleCodeT = cdRuleCodeReposMon.findByRuleCode(cdRuleCode.getRuleCode());
    else if (dbName.equals(ContentName.onHist))
      cdRuleCodeT = cdRuleCodeReposHist.findByRuleCode(cdRuleCode.getRuleCode());
    else 
      cdRuleCodeT = cdRuleCodeRepos.findByRuleCode(cdRuleCode.getRuleCode());
    return cdRuleCodeT.isPresent() ? cdRuleCodeT.get() : null;
  }

  @Override
  public CdRuleCode insert(CdRuleCode cdRuleCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdRuleCode.getRuleCode());
    if (this.findById(cdRuleCode.getRuleCode(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdRuleCode.setCreateEmpNo(empNot);

    if(cdRuleCode.getLastUpdateEmpNo() == null || cdRuleCode.getLastUpdateEmpNo().isEmpty())
      cdRuleCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdRuleCodeReposDay.saveAndFlush(cdRuleCode);	
    else if (dbName.equals(ContentName.onMon))
      return cdRuleCodeReposMon.saveAndFlush(cdRuleCode);
    else if (dbName.equals(ContentName.onHist))
      return cdRuleCodeReposHist.saveAndFlush(cdRuleCode);
    else 
    return cdRuleCodeRepos.saveAndFlush(cdRuleCode);
  }

  @Override
  public CdRuleCode update(CdRuleCode cdRuleCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdRuleCode.getRuleCode());
    if (!empNot.isEmpty())
      cdRuleCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdRuleCodeReposDay.saveAndFlush(cdRuleCode);	
    else if (dbName.equals(ContentName.onMon))
      return cdRuleCodeReposMon.saveAndFlush(cdRuleCode);
    else if (dbName.equals(ContentName.onHist))
      return cdRuleCodeReposHist.saveAndFlush(cdRuleCode);
    else 
    return cdRuleCodeRepos.saveAndFlush(cdRuleCode);
  }

  @Override
  public CdRuleCode update2(CdRuleCode cdRuleCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdRuleCode.getRuleCode());
    if (!empNot.isEmpty())
      cdRuleCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdRuleCodeReposDay.saveAndFlush(cdRuleCode);	
    else if (dbName.equals(ContentName.onMon))
      cdRuleCodeReposMon.saveAndFlush(cdRuleCode);
    else if (dbName.equals(ContentName.onHist))
        cdRuleCodeReposHist.saveAndFlush(cdRuleCode);
    else 
      cdRuleCodeRepos.saveAndFlush(cdRuleCode);	
    return this.findById(cdRuleCode.getRuleCode());
  }

  @Override
  public void delete(CdRuleCode cdRuleCode, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdRuleCode.getRuleCode());
    if (dbName.equals(ContentName.onDay)) {
      cdRuleCodeReposDay.delete(cdRuleCode);	
      cdRuleCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdRuleCodeReposMon.delete(cdRuleCode);	
      cdRuleCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdRuleCodeReposHist.delete(cdRuleCode);
      cdRuleCodeReposHist.flush();
    }
    else {
      cdRuleCodeRepos.delete(cdRuleCode);
      cdRuleCodeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdRuleCode> cdRuleCode, TitaVo... titaVo) throws DBException {
    if (cdRuleCode == null || cdRuleCode.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdRuleCode t : cdRuleCode){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdRuleCode = cdRuleCodeReposDay.saveAll(cdRuleCode);	
      cdRuleCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdRuleCode = cdRuleCodeReposMon.saveAll(cdRuleCode);	
      cdRuleCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdRuleCode = cdRuleCodeReposHist.saveAll(cdRuleCode);
      cdRuleCodeReposHist.flush();
    }
    else {
      cdRuleCode = cdRuleCodeRepos.saveAll(cdRuleCode);
      cdRuleCodeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdRuleCode> cdRuleCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdRuleCode == null || cdRuleCode.size() == 0)
      throw new DBException(6);

    for (CdRuleCode t : cdRuleCode) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdRuleCode = cdRuleCodeReposDay.saveAll(cdRuleCode);	
      cdRuleCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdRuleCode = cdRuleCodeReposMon.saveAll(cdRuleCode);	
      cdRuleCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdRuleCode = cdRuleCodeReposHist.saveAll(cdRuleCode);
      cdRuleCodeReposHist.flush();
    }
    else {
      cdRuleCode = cdRuleCodeRepos.saveAll(cdRuleCode);
      cdRuleCodeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdRuleCode> cdRuleCode, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdRuleCode == null || cdRuleCode.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdRuleCodeReposDay.deleteAll(cdRuleCode);	
      cdRuleCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdRuleCodeReposMon.deleteAll(cdRuleCode);	
      cdRuleCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdRuleCodeReposHist.deleteAll(cdRuleCode);
      cdRuleCodeReposHist.flush();
    }
    else {
      cdRuleCodeRepos.deleteAll(cdRuleCode);
      cdRuleCodeRepos.flush();
    }
  }

}
