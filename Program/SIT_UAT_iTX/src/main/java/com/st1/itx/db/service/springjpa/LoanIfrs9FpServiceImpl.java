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
import com.st1.itx.db.domain.LoanIfrs9Fp;
import com.st1.itx.db.domain.LoanIfrs9FpId;
import com.st1.itx.db.repository.online.LoanIfrs9FpRepository;
import com.st1.itx.db.repository.day.LoanIfrs9FpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrs9FpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrs9FpRepositoryHist;
import com.st1.itx.db.service.LoanIfrs9FpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrs9FpService")
@Repository
public class LoanIfrs9FpServiceImpl extends ASpringJpaParm implements LoanIfrs9FpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrs9FpRepository loanIfrs9FpRepos;

  @Autowired
  private LoanIfrs9FpRepositoryDay loanIfrs9FpReposDay;

  @Autowired
  private LoanIfrs9FpRepositoryMon loanIfrs9FpReposMon;

  @Autowired
  private LoanIfrs9FpRepositoryHist loanIfrs9FpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrs9FpRepos);
    org.junit.Assert.assertNotNull(loanIfrs9FpReposDay);
    org.junit.Assert.assertNotNull(loanIfrs9FpReposMon);
    org.junit.Assert.assertNotNull(loanIfrs9FpReposHist);
  }

  @Override
  public LoanIfrs9Fp findById(LoanIfrs9FpId loanIfrs9FpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanIfrs9FpId);
    Optional<LoanIfrs9Fp> loanIfrs9Fp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Fp = loanIfrs9FpReposDay.findById(loanIfrs9FpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Fp = loanIfrs9FpReposMon.findById(loanIfrs9FpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Fp = loanIfrs9FpReposHist.findById(loanIfrs9FpId);
    else 
      loanIfrs9Fp = loanIfrs9FpRepos.findById(loanIfrs9FpId);
    LoanIfrs9Fp obj = loanIfrs9Fp.isPresent() ? loanIfrs9Fp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrs9Fp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrs9Fp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "AgreeNo", "AgreeFg", "FacmNo", "BormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "AgreeNo", "AgreeFg", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrs9FpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrs9FpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrs9FpReposHist.findAll(pageable);
    else 
      slice = loanIfrs9FpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrs9Fp holdById(LoanIfrs9FpId loanIfrs9FpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9FpId);
    Optional<LoanIfrs9Fp> loanIfrs9Fp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Fp = loanIfrs9FpReposDay.findByLoanIfrs9FpId(loanIfrs9FpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Fp = loanIfrs9FpReposMon.findByLoanIfrs9FpId(loanIfrs9FpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Fp = loanIfrs9FpReposHist.findByLoanIfrs9FpId(loanIfrs9FpId);
    else 
      loanIfrs9Fp = loanIfrs9FpRepos.findByLoanIfrs9FpId(loanIfrs9FpId);
    return loanIfrs9Fp.isPresent() ? loanIfrs9Fp.get() : null;
  }

  @Override
  public LoanIfrs9Fp holdById(LoanIfrs9Fp loanIfrs9Fp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9Fp.getLoanIfrs9FpId());
    Optional<LoanIfrs9Fp> loanIfrs9FpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9FpT = loanIfrs9FpReposDay.findByLoanIfrs9FpId(loanIfrs9Fp.getLoanIfrs9FpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9FpT = loanIfrs9FpReposMon.findByLoanIfrs9FpId(loanIfrs9Fp.getLoanIfrs9FpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9FpT = loanIfrs9FpReposHist.findByLoanIfrs9FpId(loanIfrs9Fp.getLoanIfrs9FpId());
    else 
      loanIfrs9FpT = loanIfrs9FpRepos.findByLoanIfrs9FpId(loanIfrs9Fp.getLoanIfrs9FpId());
    return loanIfrs9FpT.isPresent() ? loanIfrs9FpT.get() : null;
  }

  @Override
  public LoanIfrs9Fp insert(LoanIfrs9Fp loanIfrs9Fp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanIfrs9Fp.getLoanIfrs9FpId());
    if (this.findById(loanIfrs9Fp.getLoanIfrs9FpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrs9Fp.setCreateEmpNo(empNot);

    if(loanIfrs9Fp.getLastUpdateEmpNo() == null || loanIfrs9Fp.getLastUpdateEmpNo().isEmpty())
      loanIfrs9Fp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9FpReposDay.saveAndFlush(loanIfrs9Fp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9FpReposMon.saveAndFlush(loanIfrs9Fp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9FpReposHist.saveAndFlush(loanIfrs9Fp);
    else 
    return loanIfrs9FpRepos.saveAndFlush(loanIfrs9Fp);
  }

  @Override
  public LoanIfrs9Fp update(LoanIfrs9Fp loanIfrs9Fp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Fp.getLoanIfrs9FpId());
    if (!empNot.isEmpty())
      loanIfrs9Fp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9FpReposDay.saveAndFlush(loanIfrs9Fp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9FpReposMon.saveAndFlush(loanIfrs9Fp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9FpReposHist.saveAndFlush(loanIfrs9Fp);
    else 
    return loanIfrs9FpRepos.saveAndFlush(loanIfrs9Fp);
  }

  @Override
  public LoanIfrs9Fp update2(LoanIfrs9Fp loanIfrs9Fp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Fp.getLoanIfrs9FpId());
    if (!empNot.isEmpty())
      loanIfrs9Fp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrs9FpReposDay.saveAndFlush(loanIfrs9Fp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9FpReposMon.saveAndFlush(loanIfrs9Fp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrs9FpReposHist.saveAndFlush(loanIfrs9Fp);
    else 
      loanIfrs9FpRepos.saveAndFlush(loanIfrs9Fp);	
    return this.findById(loanIfrs9Fp.getLoanIfrs9FpId());
  }

  @Override
  public void delete(LoanIfrs9Fp loanIfrs9Fp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanIfrs9Fp.getLoanIfrs9FpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9FpReposDay.delete(loanIfrs9Fp);	
      loanIfrs9FpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9FpReposMon.delete(loanIfrs9Fp);	
      loanIfrs9FpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9FpReposHist.delete(loanIfrs9Fp);
      loanIfrs9FpReposHist.flush();
    }
    else {
      loanIfrs9FpRepos.delete(loanIfrs9Fp);
      loanIfrs9FpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrs9Fp> loanIfrs9Fp, TitaVo... titaVo) throws DBException {
    if (loanIfrs9Fp == null || loanIfrs9Fp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanIfrs9Fp t : loanIfrs9Fp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Fp = loanIfrs9FpReposDay.saveAll(loanIfrs9Fp);	
      loanIfrs9FpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Fp = loanIfrs9FpReposMon.saveAll(loanIfrs9Fp);	
      loanIfrs9FpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Fp = loanIfrs9FpReposHist.saveAll(loanIfrs9Fp);
      loanIfrs9FpReposHist.flush();
    }
    else {
      loanIfrs9Fp = loanIfrs9FpRepos.saveAll(loanIfrs9Fp);
      loanIfrs9FpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrs9Fp> loanIfrs9Fp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanIfrs9Fp == null || loanIfrs9Fp.size() == 0)
      throw new DBException(6);

    for (LoanIfrs9Fp t : loanIfrs9Fp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Fp = loanIfrs9FpReposDay.saveAll(loanIfrs9Fp);	
      loanIfrs9FpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Fp = loanIfrs9FpReposMon.saveAll(loanIfrs9Fp);	
      loanIfrs9FpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Fp = loanIfrs9FpReposHist.saveAll(loanIfrs9Fp);
      loanIfrs9FpReposHist.flush();
    }
    else {
      loanIfrs9Fp = loanIfrs9FpRepos.saveAll(loanIfrs9Fp);
      loanIfrs9FpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrs9Fp> loanIfrs9Fp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrs9Fp == null || loanIfrs9Fp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9FpReposDay.deleteAll(loanIfrs9Fp);	
      loanIfrs9FpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9FpReposMon.deleteAll(loanIfrs9Fp);	
      loanIfrs9FpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9FpReposHist.deleteAll(loanIfrs9Fp);
      loanIfrs9FpReposHist.flush();
    }
    else {
      loanIfrs9FpRepos.deleteAll(loanIfrs9Fp);
      loanIfrs9FpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrs9Fp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9FpReposDay.uspL7Loanifrs9fpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9FpReposMon.uspL7Loanifrs9fpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9FpReposHist.uspL7Loanifrs9fpUpd(TBSDYF, EmpNo);
   else
      loanIfrs9FpRepos.uspL7Loanifrs9fpUpd(TBSDYF, EmpNo);
  }

}
