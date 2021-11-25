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
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;
import com.st1.itx.db.repository.online.LoanOverdueRepository;
import com.st1.itx.db.repository.day.LoanOverdueRepositoryDay;
import com.st1.itx.db.repository.mon.LoanOverdueRepositoryMon;
import com.st1.itx.db.repository.hist.LoanOverdueRepositoryHist;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanOverdueService")
@Repository
public class LoanOverdueServiceImpl extends ASpringJpaParm implements LoanOverdueService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanOverdueRepository loanOverdueRepos;

  @Autowired
  private LoanOverdueRepositoryDay loanOverdueReposDay;

  @Autowired
  private LoanOverdueRepositoryMon loanOverdueReposMon;

  @Autowired
  private LoanOverdueRepositoryHist loanOverdueReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanOverdueRepos);
    org.junit.Assert.assertNotNull(loanOverdueReposDay);
    org.junit.Assert.assertNotNull(loanOverdueReposMon);
    org.junit.Assert.assertNotNull(loanOverdueReposHist);
  }

  @Override
  public LoanOverdue findById(LoanOverdueId loanOverdueId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanOverdueId);
    Optional<LoanOverdue> loanOverdue = null;
    if (dbName.equals(ContentName.onDay))
      loanOverdue = loanOverdueReposDay.findById(loanOverdueId);
    else if (dbName.equals(ContentName.onMon))
      loanOverdue = loanOverdueReposMon.findById(loanOverdueId);
    else if (dbName.equals(ContentName.onHist))
      loanOverdue = loanOverdueReposHist.findById(loanOverdueId);
    else 
      loanOverdue = loanOverdueRepos.findById(loanOverdueId);
    LoanOverdue obj = loanOverdue.isPresent() ? loanOverdue.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanOverdue> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanOverdue> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "BormNo", "OvduNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "BormNo", "OvduNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanOverdueReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanOverdueReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanOverdueReposHist.findAll(pageable);
    else 
      slice = loanOverdueRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanOverdue> ovduCustNoRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int ovduNo_5, int ovduNo_6, List<Integer> status_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanOverdue> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ovduCustNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2 + " bormNo_3 : " +  bormNo_3 + " bormNo_4 : " +  bormNo_4 + " ovduNo_5 : " +  ovduNo_5 + " ovduNo_6 : " +  ovduNo_6 + " status_7 : " +  status_7);
    if (dbName.equals(ContentName.onDay))
      slice = loanOverdueReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndOvduNoGreaterThanEqualAndOvduNoLessThanEqualAndStatusInOrderByAcDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, ovduNo_5, ovduNo_6, status_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanOverdueReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndOvduNoGreaterThanEqualAndOvduNoLessThanEqualAndStatusInOrderByAcDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, ovduNo_5, ovduNo_6, status_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanOverdueReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndOvduNoGreaterThanEqualAndOvduNoLessThanEqualAndStatusInOrderByAcDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, ovduNo_5, ovduNo_6, status_7, pageable);
    else 
      slice = loanOverdueRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndOvduNoGreaterThanEqualAndOvduNoLessThanEqualAndStatusInOrderByAcDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, ovduNo_5, ovduNo_6, status_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanOverdue holdById(LoanOverdueId loanOverdueId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanOverdueId);
    Optional<LoanOverdue> loanOverdue = null;
    if (dbName.equals(ContentName.onDay))
      loanOverdue = loanOverdueReposDay.findByLoanOverdueId(loanOverdueId);
    else if (dbName.equals(ContentName.onMon))
      loanOverdue = loanOverdueReposMon.findByLoanOverdueId(loanOverdueId);
    else if (dbName.equals(ContentName.onHist))
      loanOverdue = loanOverdueReposHist.findByLoanOverdueId(loanOverdueId);
    else 
      loanOverdue = loanOverdueRepos.findByLoanOverdueId(loanOverdueId);
    return loanOverdue.isPresent() ? loanOverdue.get() : null;
  }

  @Override
  public LoanOverdue holdById(LoanOverdue loanOverdue, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanOverdue.getLoanOverdueId());
    Optional<LoanOverdue> loanOverdueT = null;
    if (dbName.equals(ContentName.onDay))
      loanOverdueT = loanOverdueReposDay.findByLoanOverdueId(loanOverdue.getLoanOverdueId());
    else if (dbName.equals(ContentName.onMon))
      loanOverdueT = loanOverdueReposMon.findByLoanOverdueId(loanOverdue.getLoanOverdueId());
    else if (dbName.equals(ContentName.onHist))
      loanOverdueT = loanOverdueReposHist.findByLoanOverdueId(loanOverdue.getLoanOverdueId());
    else 
      loanOverdueT = loanOverdueRepos.findByLoanOverdueId(loanOverdue.getLoanOverdueId());
    return loanOverdueT.isPresent() ? loanOverdueT.get() : null;
  }

  @Override
  public LoanOverdue insert(LoanOverdue loanOverdue, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanOverdue.getLoanOverdueId());
    if (this.findById(loanOverdue.getLoanOverdueId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanOverdue.setCreateEmpNo(empNot);

    if(loanOverdue.getLastUpdateEmpNo() == null || loanOverdue.getLastUpdateEmpNo().isEmpty())
      loanOverdue.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanOverdueReposDay.saveAndFlush(loanOverdue);	
    else if (dbName.equals(ContentName.onMon))
      return loanOverdueReposMon.saveAndFlush(loanOverdue);
    else if (dbName.equals(ContentName.onHist))
      return loanOverdueReposHist.saveAndFlush(loanOverdue);
    else 
    return loanOverdueRepos.saveAndFlush(loanOverdue);
  }

  @Override
  public LoanOverdue update(LoanOverdue loanOverdue, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanOverdue.getLoanOverdueId());
    if (!empNot.isEmpty())
      loanOverdue.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanOverdueReposDay.saveAndFlush(loanOverdue);	
    else if (dbName.equals(ContentName.onMon))
      return loanOverdueReposMon.saveAndFlush(loanOverdue);
    else if (dbName.equals(ContentName.onHist))
      return loanOverdueReposHist.saveAndFlush(loanOverdue);
    else 
    return loanOverdueRepos.saveAndFlush(loanOverdue);
  }

  @Override
  public LoanOverdue update2(LoanOverdue loanOverdue, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanOverdue.getLoanOverdueId());
    if (!empNot.isEmpty())
      loanOverdue.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanOverdueReposDay.saveAndFlush(loanOverdue);	
    else if (dbName.equals(ContentName.onMon))
      loanOverdueReposMon.saveAndFlush(loanOverdue);
    else if (dbName.equals(ContentName.onHist))
        loanOverdueReposHist.saveAndFlush(loanOverdue);
    else 
      loanOverdueRepos.saveAndFlush(loanOverdue);	
    return this.findById(loanOverdue.getLoanOverdueId());
  }

  @Override
  public void delete(LoanOverdue loanOverdue, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanOverdue.getLoanOverdueId());
    if (dbName.equals(ContentName.onDay)) {
      loanOverdueReposDay.delete(loanOverdue);	
      loanOverdueReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanOverdueReposMon.delete(loanOverdue);	
      loanOverdueReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanOverdueReposHist.delete(loanOverdue);
      loanOverdueReposHist.flush();
    }
    else {
      loanOverdueRepos.delete(loanOverdue);
      loanOverdueRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanOverdue> loanOverdue, TitaVo... titaVo) throws DBException {
    if (loanOverdue == null || loanOverdue.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanOverdue t : loanOverdue){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanOverdue = loanOverdueReposDay.saveAll(loanOverdue);	
      loanOverdueReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanOverdue = loanOverdueReposMon.saveAll(loanOverdue);	
      loanOverdueReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanOverdue = loanOverdueReposHist.saveAll(loanOverdue);
      loanOverdueReposHist.flush();
    }
    else {
      loanOverdue = loanOverdueRepos.saveAll(loanOverdue);
      loanOverdueRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanOverdue> loanOverdue, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanOverdue == null || loanOverdue.size() == 0)
      throw new DBException(6);

    for (LoanOverdue t : loanOverdue) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanOverdue = loanOverdueReposDay.saveAll(loanOverdue);	
      loanOverdueReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanOverdue = loanOverdueReposMon.saveAll(loanOverdue);	
      loanOverdueReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanOverdue = loanOverdueReposHist.saveAll(loanOverdue);
      loanOverdueReposHist.flush();
    }
    else {
      loanOverdue = loanOverdueRepos.saveAll(loanOverdue);
      loanOverdueRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanOverdue> loanOverdue, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanOverdue == null || loanOverdue.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanOverdueReposDay.deleteAll(loanOverdue);	
      loanOverdueReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanOverdueReposMon.deleteAll(loanOverdue);	
      loanOverdueReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanOverdueReposHist.deleteAll(loanOverdue);
      loanOverdueReposHist.flush();
    }
    else {
      loanOverdueRepos.deleteAll(loanOverdue);
      loanOverdueRepos.flush();
    }
  }

}
