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
import com.st1.itx.db.domain.LoanIfrsCp;
import com.st1.itx.db.domain.LoanIfrsCpId;
import com.st1.itx.db.repository.online.LoanIfrsCpRepository;
import com.st1.itx.db.repository.day.LoanIfrsCpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrsCpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrsCpRepositoryHist;
import com.st1.itx.db.service.LoanIfrsCpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrsCpService")
@Repository
public class LoanIfrsCpServiceImpl extends ASpringJpaParm implements LoanIfrsCpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrsCpRepository loanIfrsCpRepos;

  @Autowired
  private LoanIfrsCpRepositoryDay loanIfrsCpReposDay;

  @Autowired
  private LoanIfrsCpRepositoryMon loanIfrsCpReposMon;

  @Autowired
  private LoanIfrsCpRepositoryHist loanIfrsCpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrsCpRepos);
    org.junit.Assert.assertNotNull(loanIfrsCpReposDay);
    org.junit.Assert.assertNotNull(loanIfrsCpReposMon);
    org.junit.Assert.assertNotNull(loanIfrsCpReposHist);
  }

  @Override
  public LoanIfrsCp findById(LoanIfrsCpId loanIfrsCpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanIfrsCpId);
    Optional<LoanIfrsCp> loanIfrsCp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsCp = loanIfrsCpReposDay.findById(loanIfrsCpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsCp = loanIfrsCpReposMon.findById(loanIfrsCpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsCp = loanIfrsCpReposHist.findById(loanIfrsCpId);
    else 
      loanIfrsCp = loanIfrsCpRepos.findById(loanIfrsCpId);
    LoanIfrsCp obj = loanIfrsCp.isPresent() ? loanIfrsCp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrsCp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrsCp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrsCpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrsCpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrsCpReposHist.findAll(pageable);
    else 
      slice = loanIfrsCpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrsCp holdById(LoanIfrsCpId loanIfrsCpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrsCpId);
    Optional<LoanIfrsCp> loanIfrsCp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsCp = loanIfrsCpReposDay.findByLoanIfrsCpId(loanIfrsCpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsCp = loanIfrsCpReposMon.findByLoanIfrsCpId(loanIfrsCpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsCp = loanIfrsCpReposHist.findByLoanIfrsCpId(loanIfrsCpId);
    else 
      loanIfrsCp = loanIfrsCpRepos.findByLoanIfrsCpId(loanIfrsCpId);
    return loanIfrsCp.isPresent() ? loanIfrsCp.get() : null;
  }

  @Override
  public LoanIfrsCp holdById(LoanIfrsCp loanIfrsCp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrsCp.getLoanIfrsCpId());
    Optional<LoanIfrsCp> loanIfrsCpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsCpT = loanIfrsCpReposDay.findByLoanIfrsCpId(loanIfrsCp.getLoanIfrsCpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrsCpT = loanIfrsCpReposMon.findByLoanIfrsCpId(loanIfrsCp.getLoanIfrsCpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrsCpT = loanIfrsCpReposHist.findByLoanIfrsCpId(loanIfrsCp.getLoanIfrsCpId());
    else 
      loanIfrsCpT = loanIfrsCpRepos.findByLoanIfrsCpId(loanIfrsCp.getLoanIfrsCpId());
    return loanIfrsCpT.isPresent() ? loanIfrsCpT.get() : null;
  }

  @Override
  public LoanIfrsCp insert(LoanIfrsCp loanIfrsCp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanIfrsCp.getLoanIfrsCpId());
    if (this.findById(loanIfrsCp.getLoanIfrsCpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrsCp.setCreateEmpNo(empNot);

    if(loanIfrsCp.getLastUpdateEmpNo() == null || loanIfrsCp.getLastUpdateEmpNo().isEmpty())
      loanIfrsCp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsCpReposDay.saveAndFlush(loanIfrsCp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsCpReposMon.saveAndFlush(loanIfrsCp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsCpReposHist.saveAndFlush(loanIfrsCp);
    else 
    return loanIfrsCpRepos.saveAndFlush(loanIfrsCp);
  }

  @Override
  public LoanIfrsCp update(LoanIfrsCp loanIfrsCp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrsCp.getLoanIfrsCpId());
    if (!empNot.isEmpty())
      loanIfrsCp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsCpReposDay.saveAndFlush(loanIfrsCp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsCpReposMon.saveAndFlush(loanIfrsCp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsCpReposHist.saveAndFlush(loanIfrsCp);
    else 
    return loanIfrsCpRepos.saveAndFlush(loanIfrsCp);
  }

  @Override
  public LoanIfrsCp update2(LoanIfrsCp loanIfrsCp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrsCp.getLoanIfrsCpId());
    if (!empNot.isEmpty())
      loanIfrsCp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrsCpReposDay.saveAndFlush(loanIfrsCp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrsCpReposMon.saveAndFlush(loanIfrsCp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrsCpReposHist.saveAndFlush(loanIfrsCp);
    else 
      loanIfrsCpRepos.saveAndFlush(loanIfrsCp);	
    return this.findById(loanIfrsCp.getLoanIfrsCpId());
  }

  @Override
  public void delete(LoanIfrsCp loanIfrsCp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanIfrsCp.getLoanIfrsCpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsCpReposDay.delete(loanIfrsCp);	
      loanIfrsCpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsCpReposMon.delete(loanIfrsCp);	
      loanIfrsCpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsCpReposHist.delete(loanIfrsCp);
      loanIfrsCpReposHist.flush();
    }
    else {
      loanIfrsCpRepos.delete(loanIfrsCp);
      loanIfrsCpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrsCp> loanIfrsCp, TitaVo... titaVo) throws DBException {
    if (loanIfrsCp == null || loanIfrsCp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanIfrsCp t : loanIfrsCp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsCp = loanIfrsCpReposDay.saveAll(loanIfrsCp);	
      loanIfrsCpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsCp = loanIfrsCpReposMon.saveAll(loanIfrsCp);	
      loanIfrsCpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsCp = loanIfrsCpReposHist.saveAll(loanIfrsCp);
      loanIfrsCpReposHist.flush();
    }
    else {
      loanIfrsCp = loanIfrsCpRepos.saveAll(loanIfrsCp);
      loanIfrsCpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrsCp> loanIfrsCp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanIfrsCp == null || loanIfrsCp.size() == 0)
      throw new DBException(6);

    for (LoanIfrsCp t : loanIfrsCp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsCp = loanIfrsCpReposDay.saveAll(loanIfrsCp);	
      loanIfrsCpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsCp = loanIfrsCpReposMon.saveAll(loanIfrsCp);	
      loanIfrsCpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsCp = loanIfrsCpReposHist.saveAll(loanIfrsCp);
      loanIfrsCpReposHist.flush();
    }
    else {
      loanIfrsCp = loanIfrsCpRepos.saveAll(loanIfrsCp);
      loanIfrsCpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrsCp> loanIfrsCp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrsCp == null || loanIfrsCp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsCpReposDay.deleteAll(loanIfrsCp);	
      loanIfrsCpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsCpReposMon.deleteAll(loanIfrsCp);	
      loanIfrsCpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsCpReposHist.deleteAll(loanIfrsCp);
      loanIfrsCpReposHist.flush();
    }
    else {
      loanIfrsCpRepos.deleteAll(loanIfrsCp);
      loanIfrsCpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrsCp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrsCpReposDay.uspL7LoanifrscpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsCpReposMon.uspL7LoanifrscpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsCpReposHist.uspL7LoanifrscpUpd(TBSDYF, EmpNo);
   else
      loanIfrsCpRepos.uspL7LoanifrscpUpd(TBSDYF, EmpNo);
  }

}
