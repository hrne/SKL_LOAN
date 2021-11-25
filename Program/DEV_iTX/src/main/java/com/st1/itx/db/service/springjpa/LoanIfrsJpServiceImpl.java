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
import com.st1.itx.db.domain.LoanIfrsJp;
import com.st1.itx.db.domain.LoanIfrsJpId;
import com.st1.itx.db.repository.online.LoanIfrsJpRepository;
import com.st1.itx.db.repository.day.LoanIfrsJpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrsJpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrsJpRepositoryHist;
import com.st1.itx.db.service.LoanIfrsJpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrsJpService")
@Repository
public class LoanIfrsJpServiceImpl extends ASpringJpaParm implements LoanIfrsJpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrsJpRepository loanIfrsJpRepos;

  @Autowired
  private LoanIfrsJpRepositoryDay loanIfrsJpReposDay;

  @Autowired
  private LoanIfrsJpRepositoryMon loanIfrsJpReposMon;

  @Autowired
  private LoanIfrsJpRepositoryHist loanIfrsJpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrsJpRepos);
    org.junit.Assert.assertNotNull(loanIfrsJpReposDay);
    org.junit.Assert.assertNotNull(loanIfrsJpReposMon);
    org.junit.Assert.assertNotNull(loanIfrsJpReposHist);
  }

  @Override
  public LoanIfrsJp findById(LoanIfrsJpId loanIfrsJpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanIfrsJpId);
    Optional<LoanIfrsJp> loanIfrsJp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsJp = loanIfrsJpReposDay.findById(loanIfrsJpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsJp = loanIfrsJpReposMon.findById(loanIfrsJpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsJp = loanIfrsJpReposHist.findById(loanIfrsJpId);
    else 
      loanIfrsJp = loanIfrsJpRepos.findById(loanIfrsJpId);
    LoanIfrsJp obj = loanIfrsJp.isPresent() ? loanIfrsJp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrsJp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrsJp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "AcDateYM", "CustNo", "NewFacmNo", "NewBormNo", "OldFacmNo", "OldBormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "AcDateYM", "CustNo", "NewFacmNo", "NewBormNo", "OldFacmNo", "OldBormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrsJpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrsJpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrsJpReposHist.findAll(pageable);
    else 
      slice = loanIfrsJpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrsJp holdById(LoanIfrsJpId loanIfrsJpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrsJpId);
    Optional<LoanIfrsJp> loanIfrsJp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsJp = loanIfrsJpReposDay.findByLoanIfrsJpId(loanIfrsJpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsJp = loanIfrsJpReposMon.findByLoanIfrsJpId(loanIfrsJpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsJp = loanIfrsJpReposHist.findByLoanIfrsJpId(loanIfrsJpId);
    else 
      loanIfrsJp = loanIfrsJpRepos.findByLoanIfrsJpId(loanIfrsJpId);
    return loanIfrsJp.isPresent() ? loanIfrsJp.get() : null;
  }

  @Override
  public LoanIfrsJp holdById(LoanIfrsJp loanIfrsJp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrsJp.getLoanIfrsJpId());
    Optional<LoanIfrsJp> loanIfrsJpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsJpT = loanIfrsJpReposDay.findByLoanIfrsJpId(loanIfrsJp.getLoanIfrsJpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrsJpT = loanIfrsJpReposMon.findByLoanIfrsJpId(loanIfrsJp.getLoanIfrsJpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrsJpT = loanIfrsJpReposHist.findByLoanIfrsJpId(loanIfrsJp.getLoanIfrsJpId());
    else 
      loanIfrsJpT = loanIfrsJpRepos.findByLoanIfrsJpId(loanIfrsJp.getLoanIfrsJpId());
    return loanIfrsJpT.isPresent() ? loanIfrsJpT.get() : null;
  }

  @Override
  public LoanIfrsJp insert(LoanIfrsJp loanIfrsJp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanIfrsJp.getLoanIfrsJpId());
    if (this.findById(loanIfrsJp.getLoanIfrsJpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrsJp.setCreateEmpNo(empNot);

    if(loanIfrsJp.getLastUpdateEmpNo() == null || loanIfrsJp.getLastUpdateEmpNo().isEmpty())
      loanIfrsJp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsJpReposDay.saveAndFlush(loanIfrsJp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsJpReposMon.saveAndFlush(loanIfrsJp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsJpReposHist.saveAndFlush(loanIfrsJp);
    else 
    return loanIfrsJpRepos.saveAndFlush(loanIfrsJp);
  }

  @Override
  public LoanIfrsJp update(LoanIfrsJp loanIfrsJp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrsJp.getLoanIfrsJpId());
    if (!empNot.isEmpty())
      loanIfrsJp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsJpReposDay.saveAndFlush(loanIfrsJp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsJpReposMon.saveAndFlush(loanIfrsJp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsJpReposHist.saveAndFlush(loanIfrsJp);
    else 
    return loanIfrsJpRepos.saveAndFlush(loanIfrsJp);
  }

  @Override
  public LoanIfrsJp update2(LoanIfrsJp loanIfrsJp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrsJp.getLoanIfrsJpId());
    if (!empNot.isEmpty())
      loanIfrsJp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrsJpReposDay.saveAndFlush(loanIfrsJp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrsJpReposMon.saveAndFlush(loanIfrsJp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrsJpReposHist.saveAndFlush(loanIfrsJp);
    else 
      loanIfrsJpRepos.saveAndFlush(loanIfrsJp);	
    return this.findById(loanIfrsJp.getLoanIfrsJpId());
  }

  @Override
  public void delete(LoanIfrsJp loanIfrsJp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanIfrsJp.getLoanIfrsJpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsJpReposDay.delete(loanIfrsJp);	
      loanIfrsJpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsJpReposMon.delete(loanIfrsJp);	
      loanIfrsJpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsJpReposHist.delete(loanIfrsJp);
      loanIfrsJpReposHist.flush();
    }
    else {
      loanIfrsJpRepos.delete(loanIfrsJp);
      loanIfrsJpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrsJp> loanIfrsJp, TitaVo... titaVo) throws DBException {
    if (loanIfrsJp == null || loanIfrsJp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanIfrsJp t : loanIfrsJp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsJp = loanIfrsJpReposDay.saveAll(loanIfrsJp);	
      loanIfrsJpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsJp = loanIfrsJpReposMon.saveAll(loanIfrsJp);	
      loanIfrsJpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsJp = loanIfrsJpReposHist.saveAll(loanIfrsJp);
      loanIfrsJpReposHist.flush();
    }
    else {
      loanIfrsJp = loanIfrsJpRepos.saveAll(loanIfrsJp);
      loanIfrsJpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrsJp> loanIfrsJp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanIfrsJp == null || loanIfrsJp.size() == 0)
      throw new DBException(6);

    for (LoanIfrsJp t : loanIfrsJp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsJp = loanIfrsJpReposDay.saveAll(loanIfrsJp);	
      loanIfrsJpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsJp = loanIfrsJpReposMon.saveAll(loanIfrsJp);	
      loanIfrsJpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsJp = loanIfrsJpReposHist.saveAll(loanIfrsJp);
      loanIfrsJpReposHist.flush();
    }
    else {
      loanIfrsJp = loanIfrsJpRepos.saveAll(loanIfrsJp);
      loanIfrsJpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrsJp> loanIfrsJp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrsJp == null || loanIfrsJp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsJpReposDay.deleteAll(loanIfrsJp);	
      loanIfrsJpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsJpReposMon.deleteAll(loanIfrsJp);	
      loanIfrsJpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsJpReposHist.deleteAll(loanIfrsJp);
      loanIfrsJpReposHist.flush();
    }
    else {
      loanIfrsJpRepos.deleteAll(loanIfrsJp);
      loanIfrsJpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrsJp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrsJpReposDay.uspL7LoanifrsjpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsJpReposMon.uspL7LoanifrsjpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsJpReposHist.uspL7LoanifrsjpUpd(TBSDYF, EmpNo);
   else
      loanIfrsJpRepos.uspL7LoanifrsjpUpd(TBSDYF, EmpNo);
  }

}
