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
import com.st1.itx.db.domain.LoanIfrsGp;
import com.st1.itx.db.domain.LoanIfrsGpId;
import com.st1.itx.db.repository.online.LoanIfrsGpRepository;
import com.st1.itx.db.repository.day.LoanIfrsGpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrsGpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrsGpRepositoryHist;
import com.st1.itx.db.service.LoanIfrsGpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrsGpService")
@Repository
public class LoanIfrsGpServiceImpl extends ASpringJpaParm implements LoanIfrsGpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrsGpRepository loanIfrsGpRepos;

  @Autowired
  private LoanIfrsGpRepositoryDay loanIfrsGpReposDay;

  @Autowired
  private LoanIfrsGpRepositoryMon loanIfrsGpReposMon;

  @Autowired
  private LoanIfrsGpRepositoryHist loanIfrsGpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrsGpRepos);
    org.junit.Assert.assertNotNull(loanIfrsGpReposDay);
    org.junit.Assert.assertNotNull(loanIfrsGpReposMon);
    org.junit.Assert.assertNotNull(loanIfrsGpReposHist);
  }

  @Override
  public LoanIfrsGp findById(LoanIfrsGpId loanIfrsGpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanIfrsGpId);
    Optional<LoanIfrsGp> loanIfrsGp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsGp = loanIfrsGpReposDay.findById(loanIfrsGpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsGp = loanIfrsGpReposMon.findById(loanIfrsGpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsGp = loanIfrsGpReposHist.findById(loanIfrsGpId);
    else 
      loanIfrsGp = loanIfrsGpRepos.findById(loanIfrsGpId);
    LoanIfrsGp obj = loanIfrsGp.isPresent() ? loanIfrsGp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrsGp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrsGp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrsGpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrsGpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrsGpReposHist.findAll(pageable);
    else 
      slice = loanIfrsGpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrsGp holdById(LoanIfrsGpId loanIfrsGpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrsGpId);
    Optional<LoanIfrsGp> loanIfrsGp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsGp = loanIfrsGpReposDay.findByLoanIfrsGpId(loanIfrsGpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsGp = loanIfrsGpReposMon.findByLoanIfrsGpId(loanIfrsGpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsGp = loanIfrsGpReposHist.findByLoanIfrsGpId(loanIfrsGpId);
    else 
      loanIfrsGp = loanIfrsGpRepos.findByLoanIfrsGpId(loanIfrsGpId);
    return loanIfrsGp.isPresent() ? loanIfrsGp.get() : null;
  }

  @Override
  public LoanIfrsGp holdById(LoanIfrsGp loanIfrsGp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrsGp.getLoanIfrsGpId());
    Optional<LoanIfrsGp> loanIfrsGpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsGpT = loanIfrsGpReposDay.findByLoanIfrsGpId(loanIfrsGp.getLoanIfrsGpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrsGpT = loanIfrsGpReposMon.findByLoanIfrsGpId(loanIfrsGp.getLoanIfrsGpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrsGpT = loanIfrsGpReposHist.findByLoanIfrsGpId(loanIfrsGp.getLoanIfrsGpId());
    else 
      loanIfrsGpT = loanIfrsGpRepos.findByLoanIfrsGpId(loanIfrsGp.getLoanIfrsGpId());
    return loanIfrsGpT.isPresent() ? loanIfrsGpT.get() : null;
  }

  @Override
  public LoanIfrsGp insert(LoanIfrsGp loanIfrsGp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanIfrsGp.getLoanIfrsGpId());
    if (this.findById(loanIfrsGp.getLoanIfrsGpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrsGp.setCreateEmpNo(empNot);

    if(loanIfrsGp.getLastUpdateEmpNo() == null || loanIfrsGp.getLastUpdateEmpNo().isEmpty())
      loanIfrsGp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsGpReposDay.saveAndFlush(loanIfrsGp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsGpReposMon.saveAndFlush(loanIfrsGp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsGpReposHist.saveAndFlush(loanIfrsGp);
    else 
    return loanIfrsGpRepos.saveAndFlush(loanIfrsGp);
  }

  @Override
  public LoanIfrsGp update(LoanIfrsGp loanIfrsGp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrsGp.getLoanIfrsGpId());
    if (!empNot.isEmpty())
      loanIfrsGp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsGpReposDay.saveAndFlush(loanIfrsGp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsGpReposMon.saveAndFlush(loanIfrsGp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsGpReposHist.saveAndFlush(loanIfrsGp);
    else 
    return loanIfrsGpRepos.saveAndFlush(loanIfrsGp);
  }

  @Override
  public LoanIfrsGp update2(LoanIfrsGp loanIfrsGp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrsGp.getLoanIfrsGpId());
    if (!empNot.isEmpty())
      loanIfrsGp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrsGpReposDay.saveAndFlush(loanIfrsGp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrsGpReposMon.saveAndFlush(loanIfrsGp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrsGpReposHist.saveAndFlush(loanIfrsGp);
    else 
      loanIfrsGpRepos.saveAndFlush(loanIfrsGp);	
    return this.findById(loanIfrsGp.getLoanIfrsGpId());
  }

  @Override
  public void delete(LoanIfrsGp loanIfrsGp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanIfrsGp.getLoanIfrsGpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsGpReposDay.delete(loanIfrsGp);	
      loanIfrsGpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsGpReposMon.delete(loanIfrsGp);	
      loanIfrsGpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsGpReposHist.delete(loanIfrsGp);
      loanIfrsGpReposHist.flush();
    }
    else {
      loanIfrsGpRepos.delete(loanIfrsGp);
      loanIfrsGpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrsGp> loanIfrsGp, TitaVo... titaVo) throws DBException {
    if (loanIfrsGp == null || loanIfrsGp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanIfrsGp t : loanIfrsGp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsGp = loanIfrsGpReposDay.saveAll(loanIfrsGp);	
      loanIfrsGpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsGp = loanIfrsGpReposMon.saveAll(loanIfrsGp);	
      loanIfrsGpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsGp = loanIfrsGpReposHist.saveAll(loanIfrsGp);
      loanIfrsGpReposHist.flush();
    }
    else {
      loanIfrsGp = loanIfrsGpRepos.saveAll(loanIfrsGp);
      loanIfrsGpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrsGp> loanIfrsGp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanIfrsGp == null || loanIfrsGp.size() == 0)
      throw new DBException(6);

    for (LoanIfrsGp t : loanIfrsGp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsGp = loanIfrsGpReposDay.saveAll(loanIfrsGp);	
      loanIfrsGpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsGp = loanIfrsGpReposMon.saveAll(loanIfrsGp);	
      loanIfrsGpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsGp = loanIfrsGpReposHist.saveAll(loanIfrsGp);
      loanIfrsGpReposHist.flush();
    }
    else {
      loanIfrsGp = loanIfrsGpRepos.saveAll(loanIfrsGp);
      loanIfrsGpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrsGp> loanIfrsGp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrsGp == null || loanIfrsGp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsGpReposDay.deleteAll(loanIfrsGp);	
      loanIfrsGpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsGpReposMon.deleteAll(loanIfrsGp);	
      loanIfrsGpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsGpReposHist.deleteAll(loanIfrsGp);
      loanIfrsGpReposHist.flush();
    }
    else {
      loanIfrsGpRepos.deleteAll(loanIfrsGp);
      loanIfrsGpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrsGp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrsGpReposDay.uspL7LoanifrsgpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsGpReposMon.uspL7LoanifrsgpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsGpReposHist.uspL7LoanifrsgpUpd(TBSDYF, EmpNo);
   else
      loanIfrsGpRepos.uspL7LoanifrsgpUpd(TBSDYF, EmpNo);
  }

}
