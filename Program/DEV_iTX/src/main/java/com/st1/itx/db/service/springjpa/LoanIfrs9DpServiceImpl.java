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
import com.st1.itx.db.domain.LoanIfrs9Dp;
import com.st1.itx.db.domain.LoanIfrs9DpId;
import com.st1.itx.db.repository.online.LoanIfrs9DpRepository;
import com.st1.itx.db.repository.day.LoanIfrs9DpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrs9DpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrs9DpRepositoryHist;
import com.st1.itx.db.service.LoanIfrs9DpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrs9DpService")
@Repository
public class LoanIfrs9DpServiceImpl extends ASpringJpaParm implements LoanIfrs9DpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrs9DpRepository loanIfrs9DpRepos;

  @Autowired
  private LoanIfrs9DpRepositoryDay loanIfrs9DpReposDay;

  @Autowired
  private LoanIfrs9DpRepositoryMon loanIfrs9DpReposMon;

  @Autowired
  private LoanIfrs9DpRepositoryHist loanIfrs9DpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrs9DpRepos);
    org.junit.Assert.assertNotNull(loanIfrs9DpReposDay);
    org.junit.Assert.assertNotNull(loanIfrs9DpReposMon);
    org.junit.Assert.assertNotNull(loanIfrs9DpReposHist);
  }

  @Override
  public LoanIfrs9Dp findById(LoanIfrs9DpId loanIfrs9DpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanIfrs9DpId);
    Optional<LoanIfrs9Dp> loanIfrs9Dp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Dp = loanIfrs9DpReposDay.findById(loanIfrs9DpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Dp = loanIfrs9DpReposMon.findById(loanIfrs9DpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Dp = loanIfrs9DpReposHist.findById(loanIfrs9DpId);
    else 
      loanIfrs9Dp = loanIfrs9DpRepos.findById(loanIfrs9DpId);
    LoanIfrs9Dp obj = loanIfrs9Dp.isPresent() ? loanIfrs9Dp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrs9Dp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrs9Dp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrs9DpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrs9DpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrs9DpReposHist.findAll(pageable);
    else 
      slice = loanIfrs9DpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrs9Dp holdById(LoanIfrs9DpId loanIfrs9DpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9DpId);
    Optional<LoanIfrs9Dp> loanIfrs9Dp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Dp = loanIfrs9DpReposDay.findByLoanIfrs9DpId(loanIfrs9DpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Dp = loanIfrs9DpReposMon.findByLoanIfrs9DpId(loanIfrs9DpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Dp = loanIfrs9DpReposHist.findByLoanIfrs9DpId(loanIfrs9DpId);
    else 
      loanIfrs9Dp = loanIfrs9DpRepos.findByLoanIfrs9DpId(loanIfrs9DpId);
    return loanIfrs9Dp.isPresent() ? loanIfrs9Dp.get() : null;
  }

  @Override
  public LoanIfrs9Dp holdById(LoanIfrs9Dp loanIfrs9Dp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9Dp.getLoanIfrs9DpId());
    Optional<LoanIfrs9Dp> loanIfrs9DpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9DpT = loanIfrs9DpReposDay.findByLoanIfrs9DpId(loanIfrs9Dp.getLoanIfrs9DpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9DpT = loanIfrs9DpReposMon.findByLoanIfrs9DpId(loanIfrs9Dp.getLoanIfrs9DpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9DpT = loanIfrs9DpReposHist.findByLoanIfrs9DpId(loanIfrs9Dp.getLoanIfrs9DpId());
    else 
      loanIfrs9DpT = loanIfrs9DpRepos.findByLoanIfrs9DpId(loanIfrs9Dp.getLoanIfrs9DpId());
    return loanIfrs9DpT.isPresent() ? loanIfrs9DpT.get() : null;
  }

  @Override
  public LoanIfrs9Dp insert(LoanIfrs9Dp loanIfrs9Dp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanIfrs9Dp.getLoanIfrs9DpId());
    if (this.findById(loanIfrs9Dp.getLoanIfrs9DpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrs9Dp.setCreateEmpNo(empNot);

    if(loanIfrs9Dp.getLastUpdateEmpNo() == null || loanIfrs9Dp.getLastUpdateEmpNo().isEmpty())
      loanIfrs9Dp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9DpReposDay.saveAndFlush(loanIfrs9Dp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9DpReposMon.saveAndFlush(loanIfrs9Dp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9DpReposHist.saveAndFlush(loanIfrs9Dp);
    else 
    return loanIfrs9DpRepos.saveAndFlush(loanIfrs9Dp);
  }

  @Override
  public LoanIfrs9Dp update(LoanIfrs9Dp loanIfrs9Dp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Dp.getLoanIfrs9DpId());
    if (!empNot.isEmpty())
      loanIfrs9Dp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9DpReposDay.saveAndFlush(loanIfrs9Dp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9DpReposMon.saveAndFlush(loanIfrs9Dp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9DpReposHist.saveAndFlush(loanIfrs9Dp);
    else 
    return loanIfrs9DpRepos.saveAndFlush(loanIfrs9Dp);
  }

  @Override
  public LoanIfrs9Dp update2(LoanIfrs9Dp loanIfrs9Dp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Dp.getLoanIfrs9DpId());
    if (!empNot.isEmpty())
      loanIfrs9Dp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrs9DpReposDay.saveAndFlush(loanIfrs9Dp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9DpReposMon.saveAndFlush(loanIfrs9Dp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrs9DpReposHist.saveAndFlush(loanIfrs9Dp);
    else 
      loanIfrs9DpRepos.saveAndFlush(loanIfrs9Dp);	
    return this.findById(loanIfrs9Dp.getLoanIfrs9DpId());
  }

  @Override
  public void delete(LoanIfrs9Dp loanIfrs9Dp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanIfrs9Dp.getLoanIfrs9DpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9DpReposDay.delete(loanIfrs9Dp);	
      loanIfrs9DpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9DpReposMon.delete(loanIfrs9Dp);	
      loanIfrs9DpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9DpReposHist.delete(loanIfrs9Dp);
      loanIfrs9DpReposHist.flush();
    }
    else {
      loanIfrs9DpRepos.delete(loanIfrs9Dp);
      loanIfrs9DpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrs9Dp> loanIfrs9Dp, TitaVo... titaVo) throws DBException {
    if (loanIfrs9Dp == null || loanIfrs9Dp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanIfrs9Dp t : loanIfrs9Dp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Dp = loanIfrs9DpReposDay.saveAll(loanIfrs9Dp);	
      loanIfrs9DpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Dp = loanIfrs9DpReposMon.saveAll(loanIfrs9Dp);	
      loanIfrs9DpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Dp = loanIfrs9DpReposHist.saveAll(loanIfrs9Dp);
      loanIfrs9DpReposHist.flush();
    }
    else {
      loanIfrs9Dp = loanIfrs9DpRepos.saveAll(loanIfrs9Dp);
      loanIfrs9DpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrs9Dp> loanIfrs9Dp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanIfrs9Dp == null || loanIfrs9Dp.size() == 0)
      throw new DBException(6);

    for (LoanIfrs9Dp t : loanIfrs9Dp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Dp = loanIfrs9DpReposDay.saveAll(loanIfrs9Dp);	
      loanIfrs9DpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Dp = loanIfrs9DpReposMon.saveAll(loanIfrs9Dp);	
      loanIfrs9DpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Dp = loanIfrs9DpReposHist.saveAll(loanIfrs9Dp);
      loanIfrs9DpReposHist.flush();
    }
    else {
      loanIfrs9Dp = loanIfrs9DpRepos.saveAll(loanIfrs9Dp);
      loanIfrs9DpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrs9Dp> loanIfrs9Dp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrs9Dp == null || loanIfrs9Dp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9DpReposDay.deleteAll(loanIfrs9Dp);	
      loanIfrs9DpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9DpReposMon.deleteAll(loanIfrs9Dp);	
      loanIfrs9DpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9DpReposHist.deleteAll(loanIfrs9Dp);
      loanIfrs9DpReposHist.flush();
    }
    else {
      loanIfrs9DpRepos.deleteAll(loanIfrs9Dp);
      loanIfrs9DpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrs9Dp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9DpReposDay.uspL7Loanifrs9dpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9DpReposMon.uspL7Loanifrs9dpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9DpReposHist.uspL7Loanifrs9dpUpd(TBSDYF, EmpNo);
   else
      loanIfrs9DpRepos.uspL7Loanifrs9dpUpd(TBSDYF, EmpNo);
  }

}
