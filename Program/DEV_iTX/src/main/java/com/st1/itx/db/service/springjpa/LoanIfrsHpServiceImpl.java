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
import com.st1.itx.db.domain.LoanIfrsHp;
import com.st1.itx.db.domain.LoanIfrsHpId;
import com.st1.itx.db.repository.online.LoanIfrsHpRepository;
import com.st1.itx.db.repository.day.LoanIfrsHpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrsHpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrsHpRepositoryHist;
import com.st1.itx.db.service.LoanIfrsHpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrsHpService")
@Repository
public class LoanIfrsHpServiceImpl extends ASpringJpaParm implements LoanIfrsHpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrsHpRepository loanIfrsHpRepos;

  @Autowired
  private LoanIfrsHpRepositoryDay loanIfrsHpReposDay;

  @Autowired
  private LoanIfrsHpRepositoryMon loanIfrsHpReposMon;

  @Autowired
  private LoanIfrsHpRepositoryHist loanIfrsHpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrsHpRepos);
    org.junit.Assert.assertNotNull(loanIfrsHpReposDay);
    org.junit.Assert.assertNotNull(loanIfrsHpReposMon);
    org.junit.Assert.assertNotNull(loanIfrsHpReposHist);
  }

  @Override
  public LoanIfrsHp findById(LoanIfrsHpId loanIfrsHpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanIfrsHpId);
    Optional<LoanIfrsHp> loanIfrsHp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsHp = loanIfrsHpReposDay.findById(loanIfrsHpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsHp = loanIfrsHpReposMon.findById(loanIfrsHpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsHp = loanIfrsHpReposHist.findById(loanIfrsHpId);
    else 
      loanIfrsHp = loanIfrsHpRepos.findById(loanIfrsHpId);
    LoanIfrsHp obj = loanIfrsHp.isPresent() ? loanIfrsHp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrsHp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrsHp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrsHpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrsHpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrsHpReposHist.findAll(pageable);
    else 
      slice = loanIfrsHpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrsHp holdById(LoanIfrsHpId loanIfrsHpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrsHpId);
    Optional<LoanIfrsHp> loanIfrsHp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsHp = loanIfrsHpReposDay.findByLoanIfrsHpId(loanIfrsHpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsHp = loanIfrsHpReposMon.findByLoanIfrsHpId(loanIfrsHpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsHp = loanIfrsHpReposHist.findByLoanIfrsHpId(loanIfrsHpId);
    else 
      loanIfrsHp = loanIfrsHpRepos.findByLoanIfrsHpId(loanIfrsHpId);
    return loanIfrsHp.isPresent() ? loanIfrsHp.get() : null;
  }

  @Override
  public LoanIfrsHp holdById(LoanIfrsHp loanIfrsHp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrsHp.getLoanIfrsHpId());
    Optional<LoanIfrsHp> loanIfrsHpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsHpT = loanIfrsHpReposDay.findByLoanIfrsHpId(loanIfrsHp.getLoanIfrsHpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrsHpT = loanIfrsHpReposMon.findByLoanIfrsHpId(loanIfrsHp.getLoanIfrsHpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrsHpT = loanIfrsHpReposHist.findByLoanIfrsHpId(loanIfrsHp.getLoanIfrsHpId());
    else 
      loanIfrsHpT = loanIfrsHpRepos.findByLoanIfrsHpId(loanIfrsHp.getLoanIfrsHpId());
    return loanIfrsHpT.isPresent() ? loanIfrsHpT.get() : null;
  }

  @Override
  public LoanIfrsHp insert(LoanIfrsHp loanIfrsHp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanIfrsHp.getLoanIfrsHpId());
    if (this.findById(loanIfrsHp.getLoanIfrsHpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrsHp.setCreateEmpNo(empNot);

    if(loanIfrsHp.getLastUpdateEmpNo() == null || loanIfrsHp.getLastUpdateEmpNo().isEmpty())
      loanIfrsHp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsHpReposDay.saveAndFlush(loanIfrsHp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsHpReposMon.saveAndFlush(loanIfrsHp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsHpReposHist.saveAndFlush(loanIfrsHp);
    else 
    return loanIfrsHpRepos.saveAndFlush(loanIfrsHp);
  }

  @Override
  public LoanIfrsHp update(LoanIfrsHp loanIfrsHp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrsHp.getLoanIfrsHpId());
    if (!empNot.isEmpty())
      loanIfrsHp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsHpReposDay.saveAndFlush(loanIfrsHp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsHpReposMon.saveAndFlush(loanIfrsHp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsHpReposHist.saveAndFlush(loanIfrsHp);
    else 
    return loanIfrsHpRepos.saveAndFlush(loanIfrsHp);
  }

  @Override
  public LoanIfrsHp update2(LoanIfrsHp loanIfrsHp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrsHp.getLoanIfrsHpId());
    if (!empNot.isEmpty())
      loanIfrsHp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrsHpReposDay.saveAndFlush(loanIfrsHp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrsHpReposMon.saveAndFlush(loanIfrsHp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrsHpReposHist.saveAndFlush(loanIfrsHp);
    else 
      loanIfrsHpRepos.saveAndFlush(loanIfrsHp);	
    return this.findById(loanIfrsHp.getLoanIfrsHpId());
  }

  @Override
  public void delete(LoanIfrsHp loanIfrsHp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanIfrsHp.getLoanIfrsHpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsHpReposDay.delete(loanIfrsHp);	
      loanIfrsHpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsHpReposMon.delete(loanIfrsHp);	
      loanIfrsHpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsHpReposHist.delete(loanIfrsHp);
      loanIfrsHpReposHist.flush();
    }
    else {
      loanIfrsHpRepos.delete(loanIfrsHp);
      loanIfrsHpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrsHp> loanIfrsHp, TitaVo... titaVo) throws DBException {
    if (loanIfrsHp == null || loanIfrsHp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanIfrsHp t : loanIfrsHp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsHp = loanIfrsHpReposDay.saveAll(loanIfrsHp);	
      loanIfrsHpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsHp = loanIfrsHpReposMon.saveAll(loanIfrsHp);	
      loanIfrsHpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsHp = loanIfrsHpReposHist.saveAll(loanIfrsHp);
      loanIfrsHpReposHist.flush();
    }
    else {
      loanIfrsHp = loanIfrsHpRepos.saveAll(loanIfrsHp);
      loanIfrsHpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrsHp> loanIfrsHp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanIfrsHp == null || loanIfrsHp.size() == 0)
      throw new DBException(6);

    for (LoanIfrsHp t : loanIfrsHp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsHp = loanIfrsHpReposDay.saveAll(loanIfrsHp);	
      loanIfrsHpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsHp = loanIfrsHpReposMon.saveAll(loanIfrsHp);	
      loanIfrsHpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsHp = loanIfrsHpReposHist.saveAll(loanIfrsHp);
      loanIfrsHpReposHist.flush();
    }
    else {
      loanIfrsHp = loanIfrsHpRepos.saveAll(loanIfrsHp);
      loanIfrsHpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrsHp> loanIfrsHp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrsHp == null || loanIfrsHp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsHpReposDay.deleteAll(loanIfrsHp);	
      loanIfrsHpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsHpReposMon.deleteAll(loanIfrsHp);	
      loanIfrsHpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsHpReposHist.deleteAll(loanIfrsHp);
      loanIfrsHpReposHist.flush();
    }
    else {
      loanIfrsHpRepos.deleteAll(loanIfrsHp);
      loanIfrsHpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrsHp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrsHpReposDay.uspL7LoanifrshpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsHpReposMon.uspL7LoanifrshpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsHpReposHist.uspL7LoanifrshpUpd(TBSDYF, EmpNo);
   else
      loanIfrsHpRepos.uspL7LoanifrshpUpd(TBSDYF, EmpNo);
  }

}
