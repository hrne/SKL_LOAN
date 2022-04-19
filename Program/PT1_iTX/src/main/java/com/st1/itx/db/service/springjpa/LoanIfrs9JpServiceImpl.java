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
import com.st1.itx.db.domain.LoanIfrs9Jp;
import com.st1.itx.db.domain.LoanIfrs9JpId;
import com.st1.itx.db.repository.online.LoanIfrs9JpRepository;
import com.st1.itx.db.repository.day.LoanIfrs9JpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrs9JpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrs9JpRepositoryHist;
import com.st1.itx.db.service.LoanIfrs9JpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrs9JpService")
@Repository
public class LoanIfrs9JpServiceImpl extends ASpringJpaParm implements LoanIfrs9JpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrs9JpRepository loanIfrs9JpRepos;

  @Autowired
  private LoanIfrs9JpRepositoryDay loanIfrs9JpReposDay;

  @Autowired
  private LoanIfrs9JpRepositoryMon loanIfrs9JpReposMon;

  @Autowired
  private LoanIfrs9JpRepositoryHist loanIfrs9JpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrs9JpRepos);
    org.junit.Assert.assertNotNull(loanIfrs9JpReposDay);
    org.junit.Assert.assertNotNull(loanIfrs9JpReposMon);
    org.junit.Assert.assertNotNull(loanIfrs9JpReposHist);
  }

  @Override
  public LoanIfrs9Jp findById(LoanIfrs9JpId loanIfrs9JpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanIfrs9JpId);
    Optional<LoanIfrs9Jp> loanIfrs9Jp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Jp = loanIfrs9JpReposDay.findById(loanIfrs9JpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Jp = loanIfrs9JpReposMon.findById(loanIfrs9JpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Jp = loanIfrs9JpReposHist.findById(loanIfrs9JpId);
    else 
      loanIfrs9Jp = loanIfrs9JpRepos.findById(loanIfrs9JpId);
    LoanIfrs9Jp obj = loanIfrs9Jp.isPresent() ? loanIfrs9Jp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrs9Jp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrs9Jp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "AcDateYM", "CustNo", "NewFacmNo", "NewBormNo", "OldFacmNo", "OldBormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "AcDateYM", "CustNo", "NewFacmNo", "NewBormNo", "OldFacmNo", "OldBormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrs9JpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrs9JpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrs9JpReposHist.findAll(pageable);
    else 
      slice = loanIfrs9JpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrs9Jp holdById(LoanIfrs9JpId loanIfrs9JpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9JpId);
    Optional<LoanIfrs9Jp> loanIfrs9Jp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Jp = loanIfrs9JpReposDay.findByLoanIfrs9JpId(loanIfrs9JpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Jp = loanIfrs9JpReposMon.findByLoanIfrs9JpId(loanIfrs9JpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Jp = loanIfrs9JpReposHist.findByLoanIfrs9JpId(loanIfrs9JpId);
    else 
      loanIfrs9Jp = loanIfrs9JpRepos.findByLoanIfrs9JpId(loanIfrs9JpId);
    return loanIfrs9Jp.isPresent() ? loanIfrs9Jp.get() : null;
  }

  @Override
  public LoanIfrs9Jp holdById(LoanIfrs9Jp loanIfrs9Jp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9Jp.getLoanIfrs9JpId());
    Optional<LoanIfrs9Jp> loanIfrs9JpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9JpT = loanIfrs9JpReposDay.findByLoanIfrs9JpId(loanIfrs9Jp.getLoanIfrs9JpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9JpT = loanIfrs9JpReposMon.findByLoanIfrs9JpId(loanIfrs9Jp.getLoanIfrs9JpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9JpT = loanIfrs9JpReposHist.findByLoanIfrs9JpId(loanIfrs9Jp.getLoanIfrs9JpId());
    else 
      loanIfrs9JpT = loanIfrs9JpRepos.findByLoanIfrs9JpId(loanIfrs9Jp.getLoanIfrs9JpId());
    return loanIfrs9JpT.isPresent() ? loanIfrs9JpT.get() : null;
  }

  @Override
  public LoanIfrs9Jp insert(LoanIfrs9Jp loanIfrs9Jp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanIfrs9Jp.getLoanIfrs9JpId());
    if (this.findById(loanIfrs9Jp.getLoanIfrs9JpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrs9Jp.setCreateEmpNo(empNot);

    if(loanIfrs9Jp.getLastUpdateEmpNo() == null || loanIfrs9Jp.getLastUpdateEmpNo().isEmpty())
      loanIfrs9Jp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9JpReposDay.saveAndFlush(loanIfrs9Jp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9JpReposMon.saveAndFlush(loanIfrs9Jp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9JpReposHist.saveAndFlush(loanIfrs9Jp);
    else 
    return loanIfrs9JpRepos.saveAndFlush(loanIfrs9Jp);
  }

  @Override
  public LoanIfrs9Jp update(LoanIfrs9Jp loanIfrs9Jp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Jp.getLoanIfrs9JpId());
    if (!empNot.isEmpty())
      loanIfrs9Jp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9JpReposDay.saveAndFlush(loanIfrs9Jp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9JpReposMon.saveAndFlush(loanIfrs9Jp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9JpReposHist.saveAndFlush(loanIfrs9Jp);
    else 
    return loanIfrs9JpRepos.saveAndFlush(loanIfrs9Jp);
  }

  @Override
  public LoanIfrs9Jp update2(LoanIfrs9Jp loanIfrs9Jp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Jp.getLoanIfrs9JpId());
    if (!empNot.isEmpty())
      loanIfrs9Jp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrs9JpReposDay.saveAndFlush(loanIfrs9Jp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9JpReposMon.saveAndFlush(loanIfrs9Jp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrs9JpReposHist.saveAndFlush(loanIfrs9Jp);
    else 
      loanIfrs9JpRepos.saveAndFlush(loanIfrs9Jp);	
    return this.findById(loanIfrs9Jp.getLoanIfrs9JpId());
  }

  @Override
  public void delete(LoanIfrs9Jp loanIfrs9Jp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanIfrs9Jp.getLoanIfrs9JpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9JpReposDay.delete(loanIfrs9Jp);	
      loanIfrs9JpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9JpReposMon.delete(loanIfrs9Jp);	
      loanIfrs9JpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9JpReposHist.delete(loanIfrs9Jp);
      loanIfrs9JpReposHist.flush();
    }
    else {
      loanIfrs9JpRepos.delete(loanIfrs9Jp);
      loanIfrs9JpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrs9Jp> loanIfrs9Jp, TitaVo... titaVo) throws DBException {
    if (loanIfrs9Jp == null || loanIfrs9Jp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanIfrs9Jp t : loanIfrs9Jp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Jp = loanIfrs9JpReposDay.saveAll(loanIfrs9Jp);	
      loanIfrs9JpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Jp = loanIfrs9JpReposMon.saveAll(loanIfrs9Jp);	
      loanIfrs9JpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Jp = loanIfrs9JpReposHist.saveAll(loanIfrs9Jp);
      loanIfrs9JpReposHist.flush();
    }
    else {
      loanIfrs9Jp = loanIfrs9JpRepos.saveAll(loanIfrs9Jp);
      loanIfrs9JpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrs9Jp> loanIfrs9Jp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanIfrs9Jp == null || loanIfrs9Jp.size() == 0)
      throw new DBException(6);

    for (LoanIfrs9Jp t : loanIfrs9Jp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Jp = loanIfrs9JpReposDay.saveAll(loanIfrs9Jp);	
      loanIfrs9JpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Jp = loanIfrs9JpReposMon.saveAll(loanIfrs9Jp);	
      loanIfrs9JpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Jp = loanIfrs9JpReposHist.saveAll(loanIfrs9Jp);
      loanIfrs9JpReposHist.flush();
    }
    else {
      loanIfrs9Jp = loanIfrs9JpRepos.saveAll(loanIfrs9Jp);
      loanIfrs9JpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrs9Jp> loanIfrs9Jp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrs9Jp == null || loanIfrs9Jp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9JpReposDay.deleteAll(loanIfrs9Jp);	
      loanIfrs9JpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9JpReposMon.deleteAll(loanIfrs9Jp);	
      loanIfrs9JpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9JpReposHist.deleteAll(loanIfrs9Jp);
      loanIfrs9JpReposHist.flush();
    }
    else {
      loanIfrs9JpRepos.deleteAll(loanIfrs9Jp);
      loanIfrs9JpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrs9Jp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9JpReposDay.uspL7Loanifrs9jpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9JpReposMon.uspL7Loanifrs9jpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9JpReposHist.uspL7Loanifrs9jpUpd(TBSDYF, EmpNo);
   else
      loanIfrs9JpRepos.uspL7Loanifrs9jpUpd(TBSDYF, EmpNo);
  }

}
