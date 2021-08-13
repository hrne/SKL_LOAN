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
import com.st1.itx.db.domain.LoanIfrsAp;
import com.st1.itx.db.domain.LoanIfrsApId;
import com.st1.itx.db.repository.online.LoanIfrsApRepository;
import com.st1.itx.db.repository.day.LoanIfrsApRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrsApRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrsApRepositoryHist;
import com.st1.itx.db.service.LoanIfrsApService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrsApService")
@Repository
public class LoanIfrsApServiceImpl implements LoanIfrsApService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(LoanIfrsApServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrsApRepository loanIfrsApRepos;

  @Autowired
  private LoanIfrsApRepositoryDay loanIfrsApReposDay;

  @Autowired
  private LoanIfrsApRepositoryMon loanIfrsApReposMon;

  @Autowired
  private LoanIfrsApRepositoryHist loanIfrsApReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrsApRepos);
    org.junit.Assert.assertNotNull(loanIfrsApReposDay);
    org.junit.Assert.assertNotNull(loanIfrsApReposMon);
    org.junit.Assert.assertNotNull(loanIfrsApReposHist);
  }

  @Override
  public LoanIfrsAp findById(LoanIfrsApId loanIfrsApId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + loanIfrsApId);
    Optional<LoanIfrsAp> loanIfrsAp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsAp = loanIfrsApReposDay.findById(loanIfrsApId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsAp = loanIfrsApReposMon.findById(loanIfrsApId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsAp = loanIfrsApReposHist.findById(loanIfrsApId);
    else 
      loanIfrsAp = loanIfrsApRepos.findById(loanIfrsApId);
    LoanIfrsAp obj = loanIfrsAp.isPresent() ? loanIfrsAp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrsAp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrsAp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrsApReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrsApReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrsApReposHist.findAll(pageable);
    else 
      slice = loanIfrsApRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrsAp holdById(LoanIfrsApId loanIfrsApId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + loanIfrsApId);
    Optional<LoanIfrsAp> loanIfrsAp = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsAp = loanIfrsApReposDay.findByLoanIfrsApId(loanIfrsApId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsAp = loanIfrsApReposMon.findByLoanIfrsApId(loanIfrsApId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsAp = loanIfrsApReposHist.findByLoanIfrsApId(loanIfrsApId);
    else 
      loanIfrsAp = loanIfrsApRepos.findByLoanIfrsApId(loanIfrsApId);
    return loanIfrsAp.isPresent() ? loanIfrsAp.get() : null;
  }

  @Override
  public LoanIfrsAp holdById(LoanIfrsAp loanIfrsAp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + loanIfrsAp.getLoanIfrsApId());
    Optional<LoanIfrsAp> loanIfrsApT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrsApT = loanIfrsApReposDay.findByLoanIfrsApId(loanIfrsAp.getLoanIfrsApId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrsApT = loanIfrsApReposMon.findByLoanIfrsApId(loanIfrsAp.getLoanIfrsApId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrsApT = loanIfrsApReposHist.findByLoanIfrsApId(loanIfrsAp.getLoanIfrsApId());
    else 
      loanIfrsApT = loanIfrsApRepos.findByLoanIfrsApId(loanIfrsAp.getLoanIfrsApId());
    return loanIfrsApT.isPresent() ? loanIfrsApT.get() : null;
  }

  @Override
  public LoanIfrsAp insert(LoanIfrsAp loanIfrsAp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + loanIfrsAp.getLoanIfrsApId());
    if (this.findById(loanIfrsAp.getLoanIfrsApId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrsAp.setCreateEmpNo(empNot);

    if(loanIfrsAp.getLastUpdateEmpNo() == null || loanIfrsAp.getLastUpdateEmpNo().isEmpty())
      loanIfrsAp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsApReposDay.saveAndFlush(loanIfrsAp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsApReposMon.saveAndFlush(loanIfrsAp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsApReposHist.saveAndFlush(loanIfrsAp);
    else 
    return loanIfrsApRepos.saveAndFlush(loanIfrsAp);
  }

  @Override
  public LoanIfrsAp update(LoanIfrsAp loanIfrsAp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + loanIfrsAp.getLoanIfrsApId());
    if (!empNot.isEmpty())
      loanIfrsAp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrsApReposDay.saveAndFlush(loanIfrsAp);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrsApReposMon.saveAndFlush(loanIfrsAp);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrsApReposHist.saveAndFlush(loanIfrsAp);
    else 
    return loanIfrsApRepos.saveAndFlush(loanIfrsAp);
  }

  @Override
  public LoanIfrsAp update2(LoanIfrsAp loanIfrsAp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + loanIfrsAp.getLoanIfrsApId());
    if (!empNot.isEmpty())
      loanIfrsAp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrsApReposDay.saveAndFlush(loanIfrsAp);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrsApReposMon.saveAndFlush(loanIfrsAp);
    else if (dbName.equals(ContentName.onHist))
        loanIfrsApReposHist.saveAndFlush(loanIfrsAp);
    else 
      loanIfrsApRepos.saveAndFlush(loanIfrsAp);	
    return this.findById(loanIfrsAp.getLoanIfrsApId());
  }

  @Override
  public void delete(LoanIfrsAp loanIfrsAp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + loanIfrsAp.getLoanIfrsApId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsApReposDay.delete(loanIfrsAp);	
      loanIfrsApReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsApReposMon.delete(loanIfrsAp);	
      loanIfrsApReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsApReposHist.delete(loanIfrsAp);
      loanIfrsApReposHist.flush();
    }
    else {
      loanIfrsApRepos.delete(loanIfrsAp);
      loanIfrsApRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrsAp> loanIfrsAp, TitaVo... titaVo) throws DBException {
    if (loanIfrsAp == null || loanIfrsAp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (LoanIfrsAp t : loanIfrsAp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsAp = loanIfrsApReposDay.saveAll(loanIfrsAp);	
      loanIfrsApReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsAp = loanIfrsApReposMon.saveAll(loanIfrsAp);	
      loanIfrsApReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsAp = loanIfrsApReposHist.saveAll(loanIfrsAp);
      loanIfrsApReposHist.flush();
    }
    else {
      loanIfrsAp = loanIfrsApRepos.saveAll(loanIfrsAp);
      loanIfrsApRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrsAp> loanIfrsAp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (loanIfrsAp == null || loanIfrsAp.size() == 0)
      throw new DBException(6);

    for (LoanIfrsAp t : loanIfrsAp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrsAp = loanIfrsApReposDay.saveAll(loanIfrsAp);	
      loanIfrsApReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsAp = loanIfrsApReposMon.saveAll(loanIfrsAp);	
      loanIfrsApReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsAp = loanIfrsApReposHist.saveAll(loanIfrsAp);
      loanIfrsApReposHist.flush();
    }
    else {
      loanIfrsAp = loanIfrsApRepos.saveAll(loanIfrsAp);
      loanIfrsApRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrsAp> loanIfrsAp, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrsAp == null || loanIfrsAp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrsApReposDay.deleteAll(loanIfrsAp);	
      loanIfrsApReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrsApReposMon.deleteAll(loanIfrsAp);	
      loanIfrsApReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrsApReposHist.deleteAll(loanIfrsAp);
      loanIfrsApReposHist.flush();
    }
    else {
      loanIfrsApRepos.deleteAll(loanIfrsAp);
      loanIfrsApRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrsAp_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrsApReposDay.uspL7LoanifrsapUpd(TBSDYF, EmpNo, NewAcFg);
    else if (dbName.equals(ContentName.onMon))
      loanIfrsApReposMon.uspL7LoanifrsapUpd(TBSDYF, EmpNo, NewAcFg);
    else if (dbName.equals(ContentName.onHist))
      loanIfrsApReposHist.uspL7LoanifrsapUpd(TBSDYF, EmpNo, NewAcFg);
   else
      loanIfrsApRepos.uspL7LoanifrsapUpd(TBSDYF, EmpNo, NewAcFg);
  }

}
