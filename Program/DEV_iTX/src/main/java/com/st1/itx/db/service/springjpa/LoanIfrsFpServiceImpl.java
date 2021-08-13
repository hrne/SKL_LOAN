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
import com.st1.itx.db.domain.LoanIfrsFp;
import com.st1.itx.db.domain.LoanIfrsFpId;
import com.st1.itx.db.repository.online.LoanIfrsFpRepository;
import com.st1.itx.db.repository.day.LoanIfrsFpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrsFpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrsFpRepositoryHist;
import com.st1.itx.db.service.LoanIfrsFpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrsFpService")
@Repository
public class LoanIfrsFpServiceImpl implements LoanIfrsFpService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(LoanIfrsFpServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrsFpRepository loanIfrsFpRepos;

  @Autowired
  private LoanIfrsFpRepositoryDay loanIfrsFpReposDay;

  @Autowired
  private LoanIfrsFpRepositoryMon loanIfrsFpReposMon;

  @Autowired
  private LoanIfrsFpRepositoryHist loanIfrsFpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrsFpRepos);
    org.junit.Assert.assertNotNull(loanIfrsFpReposDay);
    org.junit.Assert.assertNotNull(loanIfrsFpReposMon);
    org.junit.Assert.assertNotNull(loanIfrsFpReposHist);
  }

  @Override
  public LoanIfrsFp findById(LoanIfrsFpId loanIfrsFpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + loanIfrsFpId);
    Optional<LoanIfrsFp> loanIfrsFp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsFp = loanIfrsFpReposDay.findById(loanIfrsFpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsFp = loanIfrsFpReposMon.findById(loanIfrsFpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsFp = loanIfrsFpReposHist.findById(loanIfrsFpId);
    else 
      loanIfrsFp = loanIfrsFpRepos.findById(loanIfrsFpId);
    LoanIfrsFp obj = loanIfrsFp.isPresent() ? loanIfrsFp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrsFp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrsFp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "AgreeNo", "AgreeFg", "FacmNo", "BormNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrsFpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrsFpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrsFpReposHist.findAll(pageable);
    else 
      slice = loanIfrsFpRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrsFp holdById(LoanIfrsFpId loanIfrsFpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + loanIfrsFpId);
    Optional<LoanIfrsFp> loanIfrsFp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsFp = loanIfrsFpReposDay.findByLoanIfrsFpId(loanIfrsFpId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsFp = loanIfrsFpReposMon.findByLoanIfrsFpId(loanIfrsFpId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsFp = loanIfrsFpReposHist.findByLoanIfrsFpId(loanIfrsFpId);
    else 
      loanIfrsFp = loanIfrsFpRepos.findByLoanIfrsFpId(loanIfrsFpId);
    return loanIfrsFp.isPresent() ? loanIfrsFp.get() : null;
  }

  @Override
  public LoanIfrsFp holdById(LoanIfrsFp loanIfrsFp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + loanIfrsFp.getLoanIfrsFpId());
    Optional<LoanIfrsFp> loanIfrsFpT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsFpT = loanIfrsFpReposDay.findByLoanIfrsFpId(loanIfrsFp.getLoanIfrsFpId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrsFpT = loanIfrsFpReposMon.findByLoanIfrsFpId(loanIfrsFp.getLoanIfrsFpId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrsFpT = loanIfrsFpReposHist.findByLoanIfrsFpId(loanIfrsFp.getLoanIfrsFpId());
    else 
      loanIfrsFpT = loanIfrsFpRepos.findByLoanIfrsFpId(loanIfrsFp.getLoanIfrsFpId());
    return loanIfrsFpT.isPresent() ? loanIfrsFpT.get() : null;
  }

  @Override
  public LoanIfrsFp insert(LoanIfrsFp loanIfrsFp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + loanIfrsFp.getLoanIfrsFpId());
    if (this.findById(loanIfrsFp.getLoanIfrsFpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrsFp.setCreateEmpNo(empNot);

    if(loanIfrsFp.getLastUpdateEmpNo() == null || loanIfrsFp.getLastUpdateEmpNo().isEmpty())
      loanIfrsFp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsFpReposDay.saveAndFlush(loanIfrsFp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsFpReposMon.saveAndFlush(loanIfrsFp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsFpReposHist.saveAndFlush(loanIfrsFp);
    else 
    return loanIfrsFpRepos.saveAndFlush(loanIfrsFp);
  }

  @Override
  public LoanIfrsFp update(LoanIfrsFp loanIfrsFp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + loanIfrsFp.getLoanIfrsFpId());
    if (!empNot.isEmpty())
      loanIfrsFp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsFpReposDay.saveAndFlush(loanIfrsFp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsFpReposMon.saveAndFlush(loanIfrsFp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsFpReposHist.saveAndFlush(loanIfrsFp);
    else 
    return loanIfrsFpRepos.saveAndFlush(loanIfrsFp);
  }

  @Override
  public LoanIfrsFp update2(LoanIfrsFp loanIfrsFp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + loanIfrsFp.getLoanIfrsFpId());
    if (!empNot.isEmpty())
      loanIfrsFp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrsFpReposDay.saveAndFlush(loanIfrsFp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrsFpReposMon.saveAndFlush(loanIfrsFp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrsFpReposHist.saveAndFlush(loanIfrsFp);
    else 
      loanIfrsFpRepos.saveAndFlush(loanIfrsFp);	
    return this.findById(loanIfrsFp.getLoanIfrsFpId());
  }

  @Override
  public void delete(LoanIfrsFp loanIfrsFp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + loanIfrsFp.getLoanIfrsFpId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsFpReposDay.delete(loanIfrsFp);	
      loanIfrsFpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsFpReposMon.delete(loanIfrsFp);	
      loanIfrsFpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsFpReposHist.delete(loanIfrsFp);
      loanIfrsFpReposHist.flush();
    }
    else {
      loanIfrsFpRepos.delete(loanIfrsFp);
      loanIfrsFpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrsFp> loanIfrsFp, TitaVo... titaVo) throws DBException {
    if (loanIfrsFp == null || loanIfrsFp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (LoanIfrsFp t : loanIfrsFp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsFp = loanIfrsFpReposDay.saveAll(loanIfrsFp);	
      loanIfrsFpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsFp = loanIfrsFpReposMon.saveAll(loanIfrsFp);	
      loanIfrsFpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsFp = loanIfrsFpReposHist.saveAll(loanIfrsFp);
      loanIfrsFpReposHist.flush();
    }
    else {
      loanIfrsFp = loanIfrsFpRepos.saveAll(loanIfrsFp);
      loanIfrsFpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrsFp> loanIfrsFp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (loanIfrsFp == null || loanIfrsFp.size() == 0)
      throw new DBException(6);

    for (LoanIfrsFp t : loanIfrsFp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsFp = loanIfrsFpReposDay.saveAll(loanIfrsFp);	
      loanIfrsFpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsFp = loanIfrsFpReposMon.saveAll(loanIfrsFp);	
      loanIfrsFpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsFp = loanIfrsFpReposHist.saveAll(loanIfrsFp);
      loanIfrsFpReposHist.flush();
    }
    else {
      loanIfrsFp = loanIfrsFpRepos.saveAll(loanIfrsFp);
      loanIfrsFpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrsFp> loanIfrsFp, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrsFp == null || loanIfrsFp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsFpReposDay.deleteAll(loanIfrsFp);	
      loanIfrsFpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsFpReposMon.deleteAll(loanIfrsFp);	
      loanIfrsFpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsFpReposHist.deleteAll(loanIfrsFp);
      loanIfrsFpReposHist.flush();
    }
    else {
      loanIfrsFpRepos.deleteAll(loanIfrsFp);
      loanIfrsFpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrsFp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrsFpReposDay.uspL7LoanifrsfpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsFpReposMon.uspL7LoanifrsfpUpd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsFpReposHist.uspL7LoanifrsfpUpd(TBSDYF, EmpNo);
   else
      loanIfrsFpRepos.uspL7LoanifrsfpUpd(TBSDYF, EmpNo);
  }

}
