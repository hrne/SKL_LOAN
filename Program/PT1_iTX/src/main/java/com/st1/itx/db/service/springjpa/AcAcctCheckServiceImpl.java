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
import com.st1.itx.db.domain.AcAcctCheck;
import com.st1.itx.db.domain.AcAcctCheckId;
import com.st1.itx.db.repository.online.AcAcctCheckRepository;
import com.st1.itx.db.repository.day.AcAcctCheckRepositoryDay;
import com.st1.itx.db.repository.mon.AcAcctCheckRepositoryMon;
import com.st1.itx.db.repository.hist.AcAcctCheckRepositoryHist;
import com.st1.itx.db.service.AcAcctCheckService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("acAcctCheckService")
@Repository
public class AcAcctCheckServiceImpl extends ASpringJpaParm implements AcAcctCheckService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AcAcctCheckRepository acAcctCheckRepos;

  @Autowired
  private AcAcctCheckRepositoryDay acAcctCheckReposDay;

  @Autowired
  private AcAcctCheckRepositoryMon acAcctCheckReposMon;

  @Autowired
  private AcAcctCheckRepositoryHist acAcctCheckReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(acAcctCheckRepos);
    org.junit.Assert.assertNotNull(acAcctCheckReposDay);
    org.junit.Assert.assertNotNull(acAcctCheckReposMon);
    org.junit.Assert.assertNotNull(acAcctCheckReposHist);
  }

  @Override
  public AcAcctCheck findById(AcAcctCheckId acAcctCheckId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + acAcctCheckId);
    Optional<AcAcctCheck> acAcctCheck = null;
    if (dbName.equals(ContentName.onDay))
      acAcctCheck = acAcctCheckReposDay.findById(acAcctCheckId);
    else if (dbName.equals(ContentName.onMon))
      acAcctCheck = acAcctCheckReposMon.findById(acAcctCheckId);
    else if (dbName.equals(ContentName.onHist))
      acAcctCheck = acAcctCheckReposHist.findById(acAcctCheckId);
    else 
      acAcctCheck = acAcctCheckRepos.findById(acAcctCheckId);
    AcAcctCheck obj = acAcctCheck.isPresent() ? acAcctCheck.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AcAcctCheck> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcAcctCheck> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "BranchNo", "CurrencyCode", "AcctCode", "AcSubBookCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "BranchNo", "CurrencyCode", "AcctCode", "AcSubBookCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = acAcctCheckReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acAcctCheckReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acAcctCheckReposHist.findAll(pageable);
    else 
      slice = acAcctCheckRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcAcctCheck> findAcDate(int acDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcAcctCheck> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findAcDate " + dbName + " : " + "acDate_0 : " + acDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = acAcctCheckReposDay.findAllByAcDateIsOrderByAcctCodeAscAcSubBookCodeAsc(acDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acAcctCheckReposMon.findAllByAcDateIsOrderByAcctCodeAscAcSubBookCodeAsc(acDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acAcctCheckReposHist.findAllByAcDateIsOrderByAcctCodeAscAcSubBookCodeAsc(acDate_0, pageable);
    else 
      slice = acAcctCheckRepos.findAllByAcDateIsOrderByAcctCodeAscAcSubBookCodeAsc(acDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AcAcctCheck holdById(AcAcctCheckId acAcctCheckId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acAcctCheckId);
    Optional<AcAcctCheck> acAcctCheck = null;
    if (dbName.equals(ContentName.onDay))
      acAcctCheck = acAcctCheckReposDay.findByAcAcctCheckId(acAcctCheckId);
    else if (dbName.equals(ContentName.onMon))
      acAcctCheck = acAcctCheckReposMon.findByAcAcctCheckId(acAcctCheckId);
    else if (dbName.equals(ContentName.onHist))
      acAcctCheck = acAcctCheckReposHist.findByAcAcctCheckId(acAcctCheckId);
    else 
      acAcctCheck = acAcctCheckRepos.findByAcAcctCheckId(acAcctCheckId);
    return acAcctCheck.isPresent() ? acAcctCheck.get() : null;
  }

  @Override
  public AcAcctCheck holdById(AcAcctCheck acAcctCheck, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acAcctCheck.getAcAcctCheckId());
    Optional<AcAcctCheck> acAcctCheckT = null;
    if (dbName.equals(ContentName.onDay))
      acAcctCheckT = acAcctCheckReposDay.findByAcAcctCheckId(acAcctCheck.getAcAcctCheckId());
    else if (dbName.equals(ContentName.onMon))
      acAcctCheckT = acAcctCheckReposMon.findByAcAcctCheckId(acAcctCheck.getAcAcctCheckId());
    else if (dbName.equals(ContentName.onHist))
      acAcctCheckT = acAcctCheckReposHist.findByAcAcctCheckId(acAcctCheck.getAcAcctCheckId());
    else 
      acAcctCheckT = acAcctCheckRepos.findByAcAcctCheckId(acAcctCheck.getAcAcctCheckId());
    return acAcctCheckT.isPresent() ? acAcctCheckT.get() : null;
  }

  @Override
  public AcAcctCheck insert(AcAcctCheck acAcctCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + acAcctCheck.getAcAcctCheckId());
    if (this.findById(acAcctCheck.getAcAcctCheckId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      acAcctCheck.setCreateEmpNo(empNot);

    if(acAcctCheck.getLastUpdateEmpNo() == null || acAcctCheck.getLastUpdateEmpNo().isEmpty())
      acAcctCheck.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acAcctCheckReposDay.saveAndFlush(acAcctCheck);	
    else if (dbName.equals(ContentName.onMon))
      return acAcctCheckReposMon.saveAndFlush(acAcctCheck);
    else if (dbName.equals(ContentName.onHist))
      return acAcctCheckReposHist.saveAndFlush(acAcctCheck);
    else 
    return acAcctCheckRepos.saveAndFlush(acAcctCheck);
  }

  @Override
  public AcAcctCheck update(AcAcctCheck acAcctCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acAcctCheck.getAcAcctCheckId());
    if (!empNot.isEmpty())
      acAcctCheck.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acAcctCheckReposDay.saveAndFlush(acAcctCheck);	
    else if (dbName.equals(ContentName.onMon))
      return acAcctCheckReposMon.saveAndFlush(acAcctCheck);
    else if (dbName.equals(ContentName.onHist))
      return acAcctCheckReposHist.saveAndFlush(acAcctCheck);
    else 
    return acAcctCheckRepos.saveAndFlush(acAcctCheck);
  }

  @Override
  public AcAcctCheck update2(AcAcctCheck acAcctCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acAcctCheck.getAcAcctCheckId());
    if (!empNot.isEmpty())
      acAcctCheck.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      acAcctCheckReposDay.saveAndFlush(acAcctCheck);	
    else if (dbName.equals(ContentName.onMon))
      acAcctCheckReposMon.saveAndFlush(acAcctCheck);
    else if (dbName.equals(ContentName.onHist))
        acAcctCheckReposHist.saveAndFlush(acAcctCheck);
    else 
      acAcctCheckRepos.saveAndFlush(acAcctCheck);	
    return this.findById(acAcctCheck.getAcAcctCheckId());
  }

  @Override
  public void delete(AcAcctCheck acAcctCheck, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + acAcctCheck.getAcAcctCheckId());
    if (dbName.equals(ContentName.onDay)) {
      acAcctCheckReposDay.delete(acAcctCheck);	
      acAcctCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acAcctCheckReposMon.delete(acAcctCheck);	
      acAcctCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acAcctCheckReposHist.delete(acAcctCheck);
      acAcctCheckReposHist.flush();
    }
    else {
      acAcctCheckRepos.delete(acAcctCheck);
      acAcctCheckRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AcAcctCheck> acAcctCheck, TitaVo... titaVo) throws DBException {
    if (acAcctCheck == null || acAcctCheck.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (AcAcctCheck t : acAcctCheck){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      acAcctCheck = acAcctCheckReposDay.saveAll(acAcctCheck);	
      acAcctCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acAcctCheck = acAcctCheckReposMon.saveAll(acAcctCheck);	
      acAcctCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acAcctCheck = acAcctCheckReposHist.saveAll(acAcctCheck);
      acAcctCheckReposHist.flush();
    }
    else {
      acAcctCheck = acAcctCheckRepos.saveAll(acAcctCheck);
      acAcctCheckRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AcAcctCheck> acAcctCheck, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (acAcctCheck == null || acAcctCheck.size() == 0)
      throw new DBException(6);

    for (AcAcctCheck t : acAcctCheck) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      acAcctCheck = acAcctCheckReposDay.saveAll(acAcctCheck);	
      acAcctCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acAcctCheck = acAcctCheckReposMon.saveAll(acAcctCheck);	
      acAcctCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acAcctCheck = acAcctCheckReposHist.saveAll(acAcctCheck);
      acAcctCheckReposHist.flush();
    }
    else {
      acAcctCheck = acAcctCheckRepos.saveAll(acAcctCheck);
      acAcctCheckRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AcAcctCheck> acAcctCheck, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (acAcctCheck == null || acAcctCheck.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      acAcctCheckReposDay.deleteAll(acAcctCheck);	
      acAcctCheckReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acAcctCheckReposMon.deleteAll(acAcctCheck);	
      acAcctCheckReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acAcctCheckReposHist.deleteAll(acAcctCheck);
      acAcctCheckReposHist.flush();
    }
    else {
      acAcctCheckRepos.deleteAll(acAcctCheck);
      acAcctCheckRepos.flush();
    }
  }

  @Override
  public void Usp_L6_AcAcctCheck_Upd(int tbsdyf,  String empNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      acAcctCheckReposDay.uspL6AcacctcheckUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onMon))
      acAcctCheckReposMon.uspL6AcacctcheckUpd(tbsdyf,  empNo);
    else if (dbName.equals(ContentName.onHist))
      acAcctCheckReposHist.uspL6AcacctcheckUpd(tbsdyf,  empNo);
   else
      acAcctCheckRepos.uspL6AcacctcheckUpd(tbsdyf,  empNo);
  }

}
