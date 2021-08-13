package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.domain.LoanIfrsDp;
import com.st1.itx.db.domain.LoanIfrsDpId;
import com.st1.itx.db.repository.online.LoanIfrsDpRepository;
import com.st1.itx.db.repository.day.LoanIfrsDpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrsDpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrsDpRepositoryHist;
import com.st1.itx.db.service.LoanIfrsDpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrsDpService")
@Repository
public class LoanIfrsDpServiceImpl implements LoanIfrsDpService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(LoanIfrsDpServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrsDpRepository loanIfrsDpRepos;

  @Autowired
  private LoanIfrsDpRepositoryDay loanIfrsDpReposDay;

  @Autowired
  private LoanIfrsDpRepositoryMon loanIfrsDpReposMon;

  @Autowired
  private LoanIfrsDpRepositoryHist loanIfrsDpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrsDpRepos);
    org.junit.Assert.assertNotNull(loanIfrsDpReposDay);
    org.junit.Assert.assertNotNull(loanIfrsDpReposMon);
    org.junit.Assert.assertNotNull(loanIfrsDpReposHist);
  }

  @Override
  public LoanIfrsDp findById(LoanIfrsDpId loanIfrsDpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + loanIfrsDpId);
    Optional<LoanIfrsDp> loanIfrsDp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsDp = loanIfrsDpReposDay.findById(loanIfrsDpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsDp = loanIfrsDpReposMon.findById(loanIfrsDpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsDp = loanIfrsDpReposHist.findById(loanIfrsDpId);
    else 
      loanIfrsDp = loanIfrsDpRepos.findById(loanIfrsDpId);
    LoanIfrsDp obj = loanIfrsDp.isPresent() ? loanIfrsDp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrsDp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrsDp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrsDpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrsDpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrsDpReposHist.findAll(pageable);
    else 
      slice = loanIfrsDpRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrsDp holdById(LoanIfrsDpId loanIfrsDpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + loanIfrsDpId);
    Optional<LoanIfrsDp> loanIfrsDp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsDp = loanIfrsDpReposDay.findByLoanIfrsDpId(loanIfrsDpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsDp = loanIfrsDpReposMon.findByLoanIfrsDpId(loanIfrsDpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsDp = loanIfrsDpReposHist.findByLoanIfrsDpId(loanIfrsDpId);
    else 
      loanIfrsDp = loanIfrsDpRepos.findByLoanIfrsDpId(loanIfrsDpId);
    return loanIfrsDp.isPresent() ? loanIfrsDp.get() : null;
  }

  @Override
  public LoanIfrsDp holdById(LoanIfrsDp loanIfrsDp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + loanIfrsDp.getLoanIfrsDpId());
    Optional<LoanIfrsDp> loanIfrsDpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsDpT = loanIfrsDpReposDay.findByLoanIfrsDpId(loanIfrsDp.getLoanIfrsDpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrsDpT = loanIfrsDpReposMon.findByLoanIfrsDpId(loanIfrsDp.getLoanIfrsDpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrsDpT = loanIfrsDpReposHist.findByLoanIfrsDpId(loanIfrsDp.getLoanIfrsDpId());
    else 
      loanIfrsDpT = loanIfrsDpRepos.findByLoanIfrsDpId(loanIfrsDp.getLoanIfrsDpId());
    return loanIfrsDpT.isPresent() ? loanIfrsDpT.get() : null;
  }

  @Override
  public LoanIfrsDp insert(LoanIfrsDp loanIfrsDp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + loanIfrsDp.getLoanIfrsDpId());
    if (this.findById(loanIfrsDp.getLoanIfrsDpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrsDp.setCreateEmpNo(empNot);

    if(loanIfrsDp.getLastUpdateEmpNo() == null || loanIfrsDp.getLastUpdateEmpNo().isEmpty())
      loanIfrsDp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsDpReposDay.saveAndFlush(loanIfrsDp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsDpReposMon.saveAndFlush(loanIfrsDp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsDpReposHist.saveAndFlush(loanIfrsDp);
    else 
    return loanIfrsDpRepos.saveAndFlush(loanIfrsDp);
  }

  @Override
  public LoanIfrsDp update(LoanIfrsDp loanIfrsDp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + loanIfrsDp.getLoanIfrsDpId());
    if (!empNot.isEmpty())
      loanIfrsDp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsDpReposDay.saveAndFlush(loanIfrsDp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsDpReposMon.saveAndFlush(loanIfrsDp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsDpReposHist.saveAndFlush(loanIfrsDp);
    else 
    return loanIfrsDpRepos.saveAndFlush(loanIfrsDp);
  }

  @Override
  public LoanIfrsDp update2(LoanIfrsDp loanIfrsDp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + loanIfrsDp.getLoanIfrsDpId());
    if (!empNot.isEmpty())
      loanIfrsDp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrsDpReposDay.saveAndFlush(loanIfrsDp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrsDpReposMon.saveAndFlush(loanIfrsDp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrsDpReposHist.saveAndFlush(loanIfrsDp);
    else 
      loanIfrsDpRepos.saveAndFlush(loanIfrsDp);	
    return this.findById(loanIfrsDp.getLoanIfrsDpId());
  }

  @Override
  public void delete(LoanIfrsDp loanIfrsDp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + loanIfrsDp.getLoanIfrsDpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsDpReposDay.delete(loanIfrsDp);	
      loanIfrsDpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsDpReposMon.delete(loanIfrsDp);	
      loanIfrsDpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsDpReposHist.delete(loanIfrsDp);
      loanIfrsDpReposHist.flush();
    }
    else {
      loanIfrsDpRepos.delete(loanIfrsDp);
      loanIfrsDpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrsDp> loanIfrsDp, TitaVo... titaVo) throws DBException {
    if (loanIfrsDp == null || loanIfrsDp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (LoanIfrsDp t : loanIfrsDp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsDp = loanIfrsDpReposDay.saveAll(loanIfrsDp);	
      loanIfrsDpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsDp = loanIfrsDpReposMon.saveAll(loanIfrsDp);	
      loanIfrsDpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsDp = loanIfrsDpReposHist.saveAll(loanIfrsDp);
      loanIfrsDpReposHist.flush();
    }
    else {
      loanIfrsDp = loanIfrsDpRepos.saveAll(loanIfrsDp);
      loanIfrsDpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrsDp> loanIfrsDp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (loanIfrsDp == null || loanIfrsDp.size() == 0)
      throw new DBException(6);

    for (LoanIfrsDp t : loanIfrsDp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsDp = loanIfrsDpReposDay.saveAll(loanIfrsDp);	
      loanIfrsDpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsDp = loanIfrsDpReposMon.saveAll(loanIfrsDp);	
      loanIfrsDpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsDp = loanIfrsDpReposHist.saveAll(loanIfrsDp);
      loanIfrsDpReposHist.flush();
    }
    else {
      loanIfrsDp = loanIfrsDpRepos.saveAll(loanIfrsDp);
      loanIfrsDpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrsDp> loanIfrsDp, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrsDp == null || loanIfrsDp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsDpReposDay.deleteAll(loanIfrsDp);	
      loanIfrsDpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsDpReposMon.deleteAll(loanIfrsDp);	
      loanIfrsDpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsDpReposHist.deleteAll(loanIfrsDp);
      loanIfrsDpReposHist.flush();
    }
    else {
      loanIfrsDpRepos.deleteAll(loanIfrsDp);
      loanIfrsDpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrsDp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrsDpReposDay.uspL7LoanifrsdpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsDpReposMon.uspL7LoanifrsdpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsDpReposHist.uspL7LoanifrsdpUpd(TBSDYF, EmpNo);
   else
      loanIfrsDpRepos.uspL7LoanifrsdpUpd(TBSDYF, EmpNo);
  }

}
