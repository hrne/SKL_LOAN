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
import com.st1.itx.db.domain.LoanIfrs9Cp;
import com.st1.itx.db.domain.LoanIfrs9CpId;
import com.st1.itx.db.repository.online.LoanIfrs9CpRepository;
import com.st1.itx.db.repository.day.LoanIfrs9CpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrs9CpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrs9CpRepositoryHist;
import com.st1.itx.db.service.LoanIfrs9CpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrs9CpService")
@Repository
public class LoanIfrs9CpServiceImpl extends ASpringJpaParm implements LoanIfrs9CpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrs9CpRepository loanIfrs9CpRepos;

  @Autowired
  private LoanIfrs9CpRepositoryDay loanIfrs9CpReposDay;

  @Autowired
  private LoanIfrs9CpRepositoryMon loanIfrs9CpReposMon;

  @Autowired
  private LoanIfrs9CpRepositoryHist loanIfrs9CpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrs9CpRepos);
    org.junit.Assert.assertNotNull(loanIfrs9CpReposDay);
    org.junit.Assert.assertNotNull(loanIfrs9CpReposMon);
    org.junit.Assert.assertNotNull(loanIfrs9CpReposHist);
  }

  @Override
  public LoanIfrs9Cp findById(LoanIfrs9CpId loanIfrs9CpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanIfrs9CpId);
    Optional<LoanIfrs9Cp> loanIfrs9Cp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Cp = loanIfrs9CpReposDay.findById(loanIfrs9CpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Cp = loanIfrs9CpReposMon.findById(loanIfrs9CpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Cp = loanIfrs9CpReposHist.findById(loanIfrs9CpId);
    else 
      loanIfrs9Cp = loanIfrs9CpRepos.findById(loanIfrs9CpId);
    LoanIfrs9Cp obj = loanIfrs9Cp.isPresent() ? loanIfrs9Cp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrs9Cp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrs9Cp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrs9CpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrs9CpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrs9CpReposHist.findAll(pageable);
    else 
      slice = loanIfrs9CpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrs9Cp holdById(LoanIfrs9CpId loanIfrs9CpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9CpId);
    Optional<LoanIfrs9Cp> loanIfrs9Cp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Cp = loanIfrs9CpReposDay.findByLoanIfrs9CpId(loanIfrs9CpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Cp = loanIfrs9CpReposMon.findByLoanIfrs9CpId(loanIfrs9CpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Cp = loanIfrs9CpReposHist.findByLoanIfrs9CpId(loanIfrs9CpId);
    else 
      loanIfrs9Cp = loanIfrs9CpRepos.findByLoanIfrs9CpId(loanIfrs9CpId);
    return loanIfrs9Cp.isPresent() ? loanIfrs9Cp.get() : null;
  }

  @Override
  public LoanIfrs9Cp holdById(LoanIfrs9Cp loanIfrs9Cp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9Cp.getLoanIfrs9CpId());
    Optional<LoanIfrs9Cp> loanIfrs9CpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9CpT = loanIfrs9CpReposDay.findByLoanIfrs9CpId(loanIfrs9Cp.getLoanIfrs9CpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9CpT = loanIfrs9CpReposMon.findByLoanIfrs9CpId(loanIfrs9Cp.getLoanIfrs9CpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9CpT = loanIfrs9CpReposHist.findByLoanIfrs9CpId(loanIfrs9Cp.getLoanIfrs9CpId());
    else 
      loanIfrs9CpT = loanIfrs9CpRepos.findByLoanIfrs9CpId(loanIfrs9Cp.getLoanIfrs9CpId());
    return loanIfrs9CpT.isPresent() ? loanIfrs9CpT.get() : null;
  }

  @Override
  public LoanIfrs9Cp insert(LoanIfrs9Cp loanIfrs9Cp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanIfrs9Cp.getLoanIfrs9CpId());
    if (this.findById(loanIfrs9Cp.getLoanIfrs9CpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrs9Cp.setCreateEmpNo(empNot);

    if(loanIfrs9Cp.getLastUpdateEmpNo() == null || loanIfrs9Cp.getLastUpdateEmpNo().isEmpty())
      loanIfrs9Cp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9CpReposDay.saveAndFlush(loanIfrs9Cp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9CpReposMon.saveAndFlush(loanIfrs9Cp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9CpReposHist.saveAndFlush(loanIfrs9Cp);
    else 
    return loanIfrs9CpRepos.saveAndFlush(loanIfrs9Cp);
  }

  @Override
  public LoanIfrs9Cp update(LoanIfrs9Cp loanIfrs9Cp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Cp.getLoanIfrs9CpId());
    if (!empNot.isEmpty())
      loanIfrs9Cp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9CpReposDay.saveAndFlush(loanIfrs9Cp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9CpReposMon.saveAndFlush(loanIfrs9Cp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9CpReposHist.saveAndFlush(loanIfrs9Cp);
    else 
    return loanIfrs9CpRepos.saveAndFlush(loanIfrs9Cp);
  }

  @Override
  public LoanIfrs9Cp update2(LoanIfrs9Cp loanIfrs9Cp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Cp.getLoanIfrs9CpId());
    if (!empNot.isEmpty())
      loanIfrs9Cp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrs9CpReposDay.saveAndFlush(loanIfrs9Cp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9CpReposMon.saveAndFlush(loanIfrs9Cp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrs9CpReposHist.saveAndFlush(loanIfrs9Cp);
    else 
      loanIfrs9CpRepos.saveAndFlush(loanIfrs9Cp);	
    return this.findById(loanIfrs9Cp.getLoanIfrs9CpId());
  }

  @Override
  public void delete(LoanIfrs9Cp loanIfrs9Cp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanIfrs9Cp.getLoanIfrs9CpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9CpReposDay.delete(loanIfrs9Cp);	
      loanIfrs9CpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9CpReposMon.delete(loanIfrs9Cp);	
      loanIfrs9CpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9CpReposHist.delete(loanIfrs9Cp);
      loanIfrs9CpReposHist.flush();
    }
    else {
      loanIfrs9CpRepos.delete(loanIfrs9Cp);
      loanIfrs9CpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrs9Cp> loanIfrs9Cp, TitaVo... titaVo) throws DBException {
    if (loanIfrs9Cp == null || loanIfrs9Cp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanIfrs9Cp t : loanIfrs9Cp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Cp = loanIfrs9CpReposDay.saveAll(loanIfrs9Cp);	
      loanIfrs9CpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Cp = loanIfrs9CpReposMon.saveAll(loanIfrs9Cp);	
      loanIfrs9CpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Cp = loanIfrs9CpReposHist.saveAll(loanIfrs9Cp);
      loanIfrs9CpReposHist.flush();
    }
    else {
      loanIfrs9Cp = loanIfrs9CpRepos.saveAll(loanIfrs9Cp);
      loanIfrs9CpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrs9Cp> loanIfrs9Cp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanIfrs9Cp == null || loanIfrs9Cp.size() == 0)
      throw new DBException(6);

    for (LoanIfrs9Cp t : loanIfrs9Cp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Cp = loanIfrs9CpReposDay.saveAll(loanIfrs9Cp);	
      loanIfrs9CpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Cp = loanIfrs9CpReposMon.saveAll(loanIfrs9Cp);	
      loanIfrs9CpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Cp = loanIfrs9CpReposHist.saveAll(loanIfrs9Cp);
      loanIfrs9CpReposHist.flush();
    }
    else {
      loanIfrs9Cp = loanIfrs9CpRepos.saveAll(loanIfrs9Cp);
      loanIfrs9CpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrs9Cp> loanIfrs9Cp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrs9Cp == null || loanIfrs9Cp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9CpReposDay.deleteAll(loanIfrs9Cp);	
      loanIfrs9CpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9CpReposMon.deleteAll(loanIfrs9Cp);	
      loanIfrs9CpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9CpReposHist.deleteAll(loanIfrs9Cp);
      loanIfrs9CpReposHist.flush();
    }
    else {
      loanIfrs9CpRepos.deleteAll(loanIfrs9Cp);
      loanIfrs9CpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrs9Cp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9CpReposDay.uspL7Loanifrs9cpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9CpReposMon.uspL7Loanifrs9cpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9CpReposHist.uspL7Loanifrs9cpUpd(TBSDYF, EmpNo);
   else
      loanIfrs9CpRepos.uspL7Loanifrs9cpUpd(TBSDYF, EmpNo);
  }

}
