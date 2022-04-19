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
import com.st1.itx.db.domain.LoanIfrs9Bp;
import com.st1.itx.db.domain.LoanIfrs9BpId;
import com.st1.itx.db.repository.online.LoanIfrs9BpRepository;
import com.st1.itx.db.repository.day.LoanIfrs9BpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrs9BpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrs9BpRepositoryHist;
import com.st1.itx.db.service.LoanIfrs9BpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrs9BpService")
@Repository
public class LoanIfrs9BpServiceImpl extends ASpringJpaParm implements LoanIfrs9BpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrs9BpRepository loanIfrs9BpRepos;

  @Autowired
  private LoanIfrs9BpRepositoryDay loanIfrs9BpReposDay;

  @Autowired
  private LoanIfrs9BpRepositoryMon loanIfrs9BpReposMon;

  @Autowired
  private LoanIfrs9BpRepositoryHist loanIfrs9BpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrs9BpRepos);
    org.junit.Assert.assertNotNull(loanIfrs9BpReposDay);
    org.junit.Assert.assertNotNull(loanIfrs9BpReposMon);
    org.junit.Assert.assertNotNull(loanIfrs9BpReposHist);
  }

  @Override
  public LoanIfrs9Bp findById(LoanIfrs9BpId loanIfrs9BpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanIfrs9BpId);
    Optional<LoanIfrs9Bp> loanIfrs9Bp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Bp = loanIfrs9BpReposDay.findById(loanIfrs9BpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Bp = loanIfrs9BpReposMon.findById(loanIfrs9BpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Bp = loanIfrs9BpReposHist.findById(loanIfrs9BpId);
    else 
      loanIfrs9Bp = loanIfrs9BpRepos.findById(loanIfrs9BpId);
    LoanIfrs9Bp obj = loanIfrs9Bp.isPresent() ? loanIfrs9Bp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrs9Bp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrs9Bp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrs9BpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrs9BpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrs9BpReposHist.findAll(pageable);
    else 
      slice = loanIfrs9BpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrs9Bp holdById(LoanIfrs9BpId loanIfrs9BpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9BpId);
    Optional<LoanIfrs9Bp> loanIfrs9Bp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Bp = loanIfrs9BpReposDay.findByLoanIfrs9BpId(loanIfrs9BpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Bp = loanIfrs9BpReposMon.findByLoanIfrs9BpId(loanIfrs9BpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Bp = loanIfrs9BpReposHist.findByLoanIfrs9BpId(loanIfrs9BpId);
    else 
      loanIfrs9Bp = loanIfrs9BpRepos.findByLoanIfrs9BpId(loanIfrs9BpId);
    return loanIfrs9Bp.isPresent() ? loanIfrs9Bp.get() : null;
  }

  @Override
  public LoanIfrs9Bp holdById(LoanIfrs9Bp loanIfrs9Bp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9Bp.getLoanIfrs9BpId());
    Optional<LoanIfrs9Bp> loanIfrs9BpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9BpT = loanIfrs9BpReposDay.findByLoanIfrs9BpId(loanIfrs9Bp.getLoanIfrs9BpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9BpT = loanIfrs9BpReposMon.findByLoanIfrs9BpId(loanIfrs9Bp.getLoanIfrs9BpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9BpT = loanIfrs9BpReposHist.findByLoanIfrs9BpId(loanIfrs9Bp.getLoanIfrs9BpId());
    else 
      loanIfrs9BpT = loanIfrs9BpRepos.findByLoanIfrs9BpId(loanIfrs9Bp.getLoanIfrs9BpId());
    return loanIfrs9BpT.isPresent() ? loanIfrs9BpT.get() : null;
  }

  @Override
  public LoanIfrs9Bp insert(LoanIfrs9Bp loanIfrs9Bp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanIfrs9Bp.getLoanIfrs9BpId());
    if (this.findById(loanIfrs9Bp.getLoanIfrs9BpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrs9Bp.setCreateEmpNo(empNot);

    if(loanIfrs9Bp.getLastUpdateEmpNo() == null || loanIfrs9Bp.getLastUpdateEmpNo().isEmpty())
      loanIfrs9Bp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9BpReposDay.saveAndFlush(loanIfrs9Bp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9BpReposMon.saveAndFlush(loanIfrs9Bp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9BpReposHist.saveAndFlush(loanIfrs9Bp);
    else 
    return loanIfrs9BpRepos.saveAndFlush(loanIfrs9Bp);
  }

  @Override
  public LoanIfrs9Bp update(LoanIfrs9Bp loanIfrs9Bp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Bp.getLoanIfrs9BpId());
    if (!empNot.isEmpty())
      loanIfrs9Bp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9BpReposDay.saveAndFlush(loanIfrs9Bp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9BpReposMon.saveAndFlush(loanIfrs9Bp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9BpReposHist.saveAndFlush(loanIfrs9Bp);
    else 
    return loanIfrs9BpRepos.saveAndFlush(loanIfrs9Bp);
  }

  @Override
  public LoanIfrs9Bp update2(LoanIfrs9Bp loanIfrs9Bp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Bp.getLoanIfrs9BpId());
    if (!empNot.isEmpty())
      loanIfrs9Bp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrs9BpReposDay.saveAndFlush(loanIfrs9Bp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9BpReposMon.saveAndFlush(loanIfrs9Bp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrs9BpReposHist.saveAndFlush(loanIfrs9Bp);
    else 
      loanIfrs9BpRepos.saveAndFlush(loanIfrs9Bp);	
    return this.findById(loanIfrs9Bp.getLoanIfrs9BpId());
  }

  @Override
  public void delete(LoanIfrs9Bp loanIfrs9Bp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanIfrs9Bp.getLoanIfrs9BpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9BpReposDay.delete(loanIfrs9Bp);	
      loanIfrs9BpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9BpReposMon.delete(loanIfrs9Bp);	
      loanIfrs9BpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9BpReposHist.delete(loanIfrs9Bp);
      loanIfrs9BpReposHist.flush();
    }
    else {
      loanIfrs9BpRepos.delete(loanIfrs9Bp);
      loanIfrs9BpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrs9Bp> loanIfrs9Bp, TitaVo... titaVo) throws DBException {
    if (loanIfrs9Bp == null || loanIfrs9Bp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanIfrs9Bp t : loanIfrs9Bp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Bp = loanIfrs9BpReposDay.saveAll(loanIfrs9Bp);	
      loanIfrs9BpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Bp = loanIfrs9BpReposMon.saveAll(loanIfrs9Bp);	
      loanIfrs9BpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Bp = loanIfrs9BpReposHist.saveAll(loanIfrs9Bp);
      loanIfrs9BpReposHist.flush();
    }
    else {
      loanIfrs9Bp = loanIfrs9BpRepos.saveAll(loanIfrs9Bp);
      loanIfrs9BpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrs9Bp> loanIfrs9Bp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanIfrs9Bp == null || loanIfrs9Bp.size() == 0)
      throw new DBException(6);

    for (LoanIfrs9Bp t : loanIfrs9Bp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Bp = loanIfrs9BpReposDay.saveAll(loanIfrs9Bp);	
      loanIfrs9BpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Bp = loanIfrs9BpReposMon.saveAll(loanIfrs9Bp);	
      loanIfrs9BpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Bp = loanIfrs9BpReposHist.saveAll(loanIfrs9Bp);
      loanIfrs9BpReposHist.flush();
    }
    else {
      loanIfrs9Bp = loanIfrs9BpRepos.saveAll(loanIfrs9Bp);
      loanIfrs9BpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrs9Bp> loanIfrs9Bp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrs9Bp == null || loanIfrs9Bp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9BpReposDay.deleteAll(loanIfrs9Bp);	
      loanIfrs9BpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9BpReposMon.deleteAll(loanIfrs9Bp);	
      loanIfrs9BpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9BpReposHist.deleteAll(loanIfrs9Bp);
      loanIfrs9BpReposHist.flush();
    }
    else {
      loanIfrs9BpRepos.deleteAll(loanIfrs9Bp);
      loanIfrs9BpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrs9Bp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9BpReposDay.uspL7Loanifrs9bpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9BpReposMon.uspL7Loanifrs9bpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9BpReposHist.uspL7Loanifrs9bpUpd(TBSDYF, EmpNo);
   else
      loanIfrs9BpRepos.uspL7Loanifrs9bpUpd(TBSDYF, EmpNo);
  }

}
