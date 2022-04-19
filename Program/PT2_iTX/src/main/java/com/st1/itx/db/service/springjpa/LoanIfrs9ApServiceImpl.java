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
import com.st1.itx.db.domain.LoanIfrs9Ap;
import com.st1.itx.db.domain.LoanIfrs9ApId;
import com.st1.itx.db.repository.online.LoanIfrs9ApRepository;
import com.st1.itx.db.repository.day.LoanIfrs9ApRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrs9ApRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrs9ApRepositoryHist;
import com.st1.itx.db.service.LoanIfrs9ApService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrs9ApService")
@Repository
public class LoanIfrs9ApServiceImpl extends ASpringJpaParm implements LoanIfrs9ApService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanIfrs9ApRepository loanIfrs9ApRepos;

  @Autowired
  private LoanIfrs9ApRepositoryDay loanIfrs9ApReposDay;

  @Autowired
  private LoanIfrs9ApRepositoryMon loanIfrs9ApReposMon;

  @Autowired
  private LoanIfrs9ApRepositoryHist loanIfrs9ApReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanIfrs9ApRepos);
    org.junit.Assert.assertNotNull(loanIfrs9ApReposDay);
    org.junit.Assert.assertNotNull(loanIfrs9ApReposMon);
    org.junit.Assert.assertNotNull(loanIfrs9ApReposHist);
  }

  @Override
  public LoanIfrs9Ap findById(LoanIfrs9ApId loanIfrs9ApId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanIfrs9ApId);
    Optional<LoanIfrs9Ap> loanIfrs9Ap = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Ap = loanIfrs9ApReposDay.findById(loanIfrs9ApId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Ap = loanIfrs9ApReposMon.findById(loanIfrs9ApId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Ap = loanIfrs9ApReposHist.findById(loanIfrs9ApId);
    else 
      loanIfrs9Ap = loanIfrs9ApRepos.findById(loanIfrs9ApId);
    LoanIfrs9Ap obj = loanIfrs9Ap.isPresent() ? loanIfrs9Ap.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanIfrs9Ap> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrs9Ap> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrs9ApReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrs9ApReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrs9ApReposHist.findAll(pageable);
    else 
      slice = loanIfrs9ApRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanIfrs9Ap> dataEq(int custNo_0, int facmNo_1, int dataYM_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanIfrs9Ap> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("dataEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " dataYM_2 : " +  dataYM_2);
    if (dbName.equals(ContentName.onDay))
      slice = loanIfrs9ApReposDay.findAllByCustNoIsAndFacmNoIsAndDataYMIs(custNo_0, facmNo_1, dataYM_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanIfrs9ApReposMon.findAllByCustNoIsAndFacmNoIsAndDataYMIs(custNo_0, facmNo_1, dataYM_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanIfrs9ApReposHist.findAllByCustNoIsAndFacmNoIsAndDataYMIs(custNo_0, facmNo_1, dataYM_2, pageable);
    else 
      slice = loanIfrs9ApRepos.findAllByCustNoIsAndFacmNoIsAndDataYMIs(custNo_0, facmNo_1, dataYM_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanIfrs9Ap holdById(LoanIfrs9ApId loanIfrs9ApId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9ApId);
    Optional<LoanIfrs9Ap> loanIfrs9Ap = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9Ap = loanIfrs9ApReposDay.findByLoanIfrs9ApId(loanIfrs9ApId);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9Ap = loanIfrs9ApReposMon.findByLoanIfrs9ApId(loanIfrs9ApId);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9Ap = loanIfrs9ApReposHist.findByLoanIfrs9ApId(loanIfrs9ApId);
    else 
      loanIfrs9Ap = loanIfrs9ApRepos.findByLoanIfrs9ApId(loanIfrs9ApId);
    return loanIfrs9Ap.isPresent() ? loanIfrs9Ap.get() : null;
  }

  @Override
  public LoanIfrs9Ap holdById(LoanIfrs9Ap loanIfrs9Ap, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanIfrs9Ap.getLoanIfrs9ApId());
    Optional<LoanIfrs9Ap> loanIfrs9ApT = null;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9ApT = loanIfrs9ApReposDay.findByLoanIfrs9ApId(loanIfrs9Ap.getLoanIfrs9ApId());
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9ApT = loanIfrs9ApReposMon.findByLoanIfrs9ApId(loanIfrs9Ap.getLoanIfrs9ApId());
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9ApT = loanIfrs9ApReposHist.findByLoanIfrs9ApId(loanIfrs9Ap.getLoanIfrs9ApId());
    else 
      loanIfrs9ApT = loanIfrs9ApRepos.findByLoanIfrs9ApId(loanIfrs9Ap.getLoanIfrs9ApId());
    return loanIfrs9ApT.isPresent() ? loanIfrs9ApT.get() : null;
  }

  @Override
  public LoanIfrs9Ap insert(LoanIfrs9Ap loanIfrs9Ap, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanIfrs9Ap.getLoanIfrs9ApId());
    if (this.findById(loanIfrs9Ap.getLoanIfrs9ApId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanIfrs9Ap.setCreateEmpNo(empNot);

    if(loanIfrs9Ap.getLastUpdateEmpNo() == null || loanIfrs9Ap.getLastUpdateEmpNo().isEmpty())
      loanIfrs9Ap.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9ApReposDay.saveAndFlush(loanIfrs9Ap);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9ApReposMon.saveAndFlush(loanIfrs9Ap);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9ApReposHist.saveAndFlush(loanIfrs9Ap);
    else 
    return loanIfrs9ApRepos.saveAndFlush(loanIfrs9Ap);
  }

  @Override
  public LoanIfrs9Ap update(LoanIfrs9Ap loanIfrs9Ap, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Ap.getLoanIfrs9ApId());
    if (!empNot.isEmpty())
      loanIfrs9Ap.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanIfrs9ApReposDay.saveAndFlush(loanIfrs9Ap);	
    else if (dbName.equals(ContentName.onMon))
      return loanIfrs9ApReposMon.saveAndFlush(loanIfrs9Ap);
    else if (dbName.equals(ContentName.onHist))
      return loanIfrs9ApReposHist.saveAndFlush(loanIfrs9Ap);
    else 
    return loanIfrs9ApRepos.saveAndFlush(loanIfrs9Ap);
  }

  @Override
  public LoanIfrs9Ap update2(LoanIfrs9Ap loanIfrs9Ap, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanIfrs9Ap.getLoanIfrs9ApId());
    if (!empNot.isEmpty())
      loanIfrs9Ap.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanIfrs9ApReposDay.saveAndFlush(loanIfrs9Ap);	
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9ApReposMon.saveAndFlush(loanIfrs9Ap);
    else if (dbName.equals(ContentName.onHist))
        loanIfrs9ApReposHist.saveAndFlush(loanIfrs9Ap);
    else 
      loanIfrs9ApRepos.saveAndFlush(loanIfrs9Ap);	
    return this.findById(loanIfrs9Ap.getLoanIfrs9ApId());
  }

  @Override
  public void delete(LoanIfrs9Ap loanIfrs9Ap, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanIfrs9Ap.getLoanIfrs9ApId());
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9ApReposDay.delete(loanIfrs9Ap);	
      loanIfrs9ApReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9ApReposMon.delete(loanIfrs9Ap);	
      loanIfrs9ApReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9ApReposHist.delete(loanIfrs9Ap);
      loanIfrs9ApReposHist.flush();
    }
    else {
      loanIfrs9ApRepos.delete(loanIfrs9Ap);
      loanIfrs9ApRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanIfrs9Ap> loanIfrs9Ap, TitaVo... titaVo) throws DBException {
    if (loanIfrs9Ap == null || loanIfrs9Ap.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanIfrs9Ap t : loanIfrs9Ap){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Ap = loanIfrs9ApReposDay.saveAll(loanIfrs9Ap);	
      loanIfrs9ApReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Ap = loanIfrs9ApReposMon.saveAll(loanIfrs9Ap);	
      loanIfrs9ApReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Ap = loanIfrs9ApReposHist.saveAll(loanIfrs9Ap);
      loanIfrs9ApReposHist.flush();
    }
    else {
      loanIfrs9Ap = loanIfrs9ApRepos.saveAll(loanIfrs9Ap);
      loanIfrs9ApRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanIfrs9Ap> loanIfrs9Ap, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanIfrs9Ap == null || loanIfrs9Ap.size() == 0)
      throw new DBException(6);

    for (LoanIfrs9Ap t : loanIfrs9Ap) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9Ap = loanIfrs9ApReposDay.saveAll(loanIfrs9Ap);	
      loanIfrs9ApReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9Ap = loanIfrs9ApReposMon.saveAll(loanIfrs9Ap);	
      loanIfrs9ApReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9Ap = loanIfrs9ApReposHist.saveAll(loanIfrs9Ap);
      loanIfrs9ApReposHist.flush();
    }
    else {
      loanIfrs9Ap = loanIfrs9ApRepos.saveAll(loanIfrs9Ap);
      loanIfrs9ApRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanIfrs9Ap> loanIfrs9Ap, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanIfrs9Ap == null || loanIfrs9Ap.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanIfrs9ApReposDay.deleteAll(loanIfrs9Ap);	
      loanIfrs9ApReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanIfrs9ApReposMon.deleteAll(loanIfrs9Ap);	
      loanIfrs9ApReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanIfrs9ApReposHist.deleteAll(loanIfrs9Ap);
      loanIfrs9ApReposHist.flush();
    }
    else {
      loanIfrs9ApRepos.deleteAll(loanIfrs9Ap);
      loanIfrs9ApRepos.flush();
    }
  }

  @Override
  public void Usp_L7_LoanIfrs9Ap_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      loanIfrs9ApReposDay.uspL7Loanifrs9apUpd(TBSDYF, EmpNo, NewAcFg);
    else if (dbName.equals(ContentName.onMon))
      loanIfrs9ApReposMon.uspL7Loanifrs9apUpd(TBSDYF, EmpNo, NewAcFg);
    else if (dbName.equals(ContentName.onHist))
      loanIfrs9ApReposHist.uspL7Loanifrs9apUpd(TBSDYF, EmpNo, NewAcFg);
   else
      loanIfrs9ApRepos.uspL7Loanifrs9apUpd(TBSDYF, EmpNo, NewAcFg);
  }

}
