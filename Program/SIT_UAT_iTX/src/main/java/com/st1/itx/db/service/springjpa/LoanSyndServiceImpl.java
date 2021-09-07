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
import com.st1.itx.db.domain.LoanSynd;
import com.st1.itx.db.domain.LoanSyndId;
import com.st1.itx.db.repository.online.LoanSyndRepository;
import com.st1.itx.db.repository.day.LoanSyndRepositoryDay;
import com.st1.itx.db.repository.mon.LoanSyndRepositoryMon;
import com.st1.itx.db.repository.hist.LoanSyndRepositoryHist;
import com.st1.itx.db.service.LoanSyndService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanSyndService")
@Repository
public class LoanSyndServiceImpl extends ASpringJpaParm implements LoanSyndService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanSyndRepository loanSyndRepos;

  @Autowired
  private LoanSyndRepositoryDay loanSyndReposDay;

  @Autowired
  private LoanSyndRepositoryMon loanSyndReposMon;

  @Autowired
  private LoanSyndRepositoryHist loanSyndReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanSyndRepos);
    org.junit.Assert.assertNotNull(loanSyndReposDay);
    org.junit.Assert.assertNotNull(loanSyndReposMon);
    org.junit.Assert.assertNotNull(loanSyndReposHist);
  }

  @Override
  public LoanSynd findById(LoanSyndId loanSyndId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanSyndId);
    Optional<LoanSynd> loanSynd = null;
    if (dbName.equals(ContentName.onDay))
      loanSynd = loanSyndReposDay.findById(loanSyndId);
    else if (dbName.equals(ContentName.onMon))
      loanSynd = loanSyndReposMon.findById(loanSyndId);
    else if (dbName.equals(ContentName.onHist))
      loanSynd = loanSyndReposHist.findById(loanSyndId);
    else 
      loanSynd = loanSyndRepos.findById(loanSyndId);
    LoanSynd obj = loanSynd.isPresent() ? loanSynd.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanSynd> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanSynd> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "SyndNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "SyndNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanSyndReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanSyndReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanSyndReposHist.findAll(pageable);
    else 
      slice = loanSyndRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanSynd> syndCustNoRange(int custNo_0, int custNo_1, String leadingBank_2, int signingDate_3, int signingDate_4, int drawdownStartDate_5, int drawdownStartDate_6, int drawdownEndDate_7, int drawdownEndDate_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanSynd> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("syndCustNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " +  custNo_1 + " leadingBank_2 : " +  leadingBank_2 + " signingDate_3 : " +  signingDate_3 + " signingDate_4 : " +  signingDate_4 + " drawdownStartDate_5 : " +  drawdownStartDate_5 + " drawdownStartDate_6 : " +  drawdownStartDate_6 + " drawdownEndDate_7 : " +  drawdownEndDate_7 + " drawdownEndDate_8 : " +  drawdownEndDate_8);
    if (dbName.equals(ContentName.onDay))
      slice = loanSyndReposDay.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndLeadingBankLikeAndSigningDateGreaterThanEqualAndSigningDateLessThanEqualAndDrawdownStartDateGreaterThanEqualAndDrawdownStartDateLessThanEqualAndDrawdownEndDateGreaterThanEqualAndDrawdownEndDateLessThanEqualOrderByCustNoAscLeadingBankAscSigningDateAscDrawdownStartDateAsc(custNo_0, custNo_1, leadingBank_2, signingDate_3, signingDate_4, drawdownStartDate_5, drawdownStartDate_6, drawdownEndDate_7, drawdownEndDate_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanSyndReposMon.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndLeadingBankLikeAndSigningDateGreaterThanEqualAndSigningDateLessThanEqualAndDrawdownStartDateGreaterThanEqualAndDrawdownStartDateLessThanEqualAndDrawdownEndDateGreaterThanEqualAndDrawdownEndDateLessThanEqualOrderByCustNoAscLeadingBankAscSigningDateAscDrawdownStartDateAsc(custNo_0, custNo_1, leadingBank_2, signingDate_3, signingDate_4, drawdownStartDate_5, drawdownStartDate_6, drawdownEndDate_7, drawdownEndDate_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanSyndReposHist.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndLeadingBankLikeAndSigningDateGreaterThanEqualAndSigningDateLessThanEqualAndDrawdownStartDateGreaterThanEqualAndDrawdownStartDateLessThanEqualAndDrawdownEndDateGreaterThanEqualAndDrawdownEndDateLessThanEqualOrderByCustNoAscLeadingBankAscSigningDateAscDrawdownStartDateAsc(custNo_0, custNo_1, leadingBank_2, signingDate_3, signingDate_4, drawdownStartDate_5, drawdownStartDate_6, drawdownEndDate_7, drawdownEndDate_8, pageable);
    else 
      slice = loanSyndRepos.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndLeadingBankLikeAndSigningDateGreaterThanEqualAndSigningDateLessThanEqualAndDrawdownStartDateGreaterThanEqualAndDrawdownStartDateLessThanEqualAndDrawdownEndDateGreaterThanEqualAndDrawdownEndDateLessThanEqualOrderByCustNoAscLeadingBankAscSigningDateAscDrawdownStartDateAsc(custNo_0, custNo_1, leadingBank_2, signingDate_3, signingDate_4, drawdownStartDate_5, drawdownStartDate_6, drawdownEndDate_7, drawdownEndDate_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanSynd holdById(LoanSyndId loanSyndId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanSyndId);
    Optional<LoanSynd> loanSynd = null;
    if (dbName.equals(ContentName.onDay))
      loanSynd = loanSyndReposDay.findByLoanSyndId(loanSyndId);
    else if (dbName.equals(ContentName.onMon))
      loanSynd = loanSyndReposMon.findByLoanSyndId(loanSyndId);
    else if (dbName.equals(ContentName.onHist))
      loanSynd = loanSyndReposHist.findByLoanSyndId(loanSyndId);
    else 
      loanSynd = loanSyndRepos.findByLoanSyndId(loanSyndId);
    return loanSynd.isPresent() ? loanSynd.get() : null;
  }

  @Override
  public LoanSynd holdById(LoanSynd loanSynd, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanSynd.getLoanSyndId());
    Optional<LoanSynd> loanSyndT = null;
    if (dbName.equals(ContentName.onDay))
      loanSyndT = loanSyndReposDay.findByLoanSyndId(loanSynd.getLoanSyndId());
    else if (dbName.equals(ContentName.onMon))
      loanSyndT = loanSyndReposMon.findByLoanSyndId(loanSynd.getLoanSyndId());
    else if (dbName.equals(ContentName.onHist))
      loanSyndT = loanSyndReposHist.findByLoanSyndId(loanSynd.getLoanSyndId());
    else 
      loanSyndT = loanSyndRepos.findByLoanSyndId(loanSynd.getLoanSyndId());
    return loanSyndT.isPresent() ? loanSyndT.get() : null;
  }

  @Override
  public LoanSynd insert(LoanSynd loanSynd, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + loanSynd.getLoanSyndId());
    if (this.findById(loanSynd.getLoanSyndId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanSynd.setCreateEmpNo(empNot);

    if(loanSynd.getLastUpdateEmpNo() == null || loanSynd.getLastUpdateEmpNo().isEmpty())
      loanSynd.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanSyndReposDay.saveAndFlush(loanSynd);	
    else if (dbName.equals(ContentName.onMon))
      return loanSyndReposMon.saveAndFlush(loanSynd);
    else if (dbName.equals(ContentName.onHist))
      return loanSyndReposHist.saveAndFlush(loanSynd);
    else 
    return loanSyndRepos.saveAndFlush(loanSynd);
  }

  @Override
  public LoanSynd update(LoanSynd loanSynd, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + loanSynd.getLoanSyndId());
    if (!empNot.isEmpty())
      loanSynd.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanSyndReposDay.saveAndFlush(loanSynd);	
    else if (dbName.equals(ContentName.onMon))
      return loanSyndReposMon.saveAndFlush(loanSynd);
    else if (dbName.equals(ContentName.onHist))
      return loanSyndReposHist.saveAndFlush(loanSynd);
    else 
    return loanSyndRepos.saveAndFlush(loanSynd);
  }

  @Override
  public LoanSynd update2(LoanSynd loanSynd, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + loanSynd.getLoanSyndId());
    if (!empNot.isEmpty())
      loanSynd.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanSyndReposDay.saveAndFlush(loanSynd);	
    else if (dbName.equals(ContentName.onMon))
      loanSyndReposMon.saveAndFlush(loanSynd);
    else if (dbName.equals(ContentName.onHist))
        loanSyndReposHist.saveAndFlush(loanSynd);
    else 
      loanSyndRepos.saveAndFlush(loanSynd);	
    return this.findById(loanSynd.getLoanSyndId());
  }

  @Override
  public void delete(LoanSynd loanSynd, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanSynd.getLoanSyndId());
    if (dbName.equals(ContentName.onDay)) {
      loanSyndReposDay.delete(loanSynd);	
      loanSyndReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanSyndReposMon.delete(loanSynd);	
      loanSyndReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanSyndReposHist.delete(loanSynd);
      loanSyndReposHist.flush();
    }
    else {
      loanSyndRepos.delete(loanSynd);
      loanSyndRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanSynd> loanSynd, TitaVo... titaVo) throws DBException {
    if (loanSynd == null || loanSynd.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (LoanSynd t : loanSynd){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanSynd = loanSyndReposDay.saveAll(loanSynd);	
      loanSyndReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanSynd = loanSyndReposMon.saveAll(loanSynd);	
      loanSyndReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanSynd = loanSyndReposHist.saveAll(loanSynd);
      loanSyndReposHist.flush();
    }
    else {
      loanSynd = loanSyndRepos.saveAll(loanSynd);
      loanSyndRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanSynd> loanSynd, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (loanSynd == null || loanSynd.size() == 0)
      throw new DBException(6);

    for (LoanSynd t : loanSynd) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanSynd = loanSyndReposDay.saveAll(loanSynd);	
      loanSyndReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanSynd = loanSyndReposMon.saveAll(loanSynd);	
      loanSyndReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanSynd = loanSyndReposHist.saveAll(loanSynd);
      loanSyndReposHist.flush();
    }
    else {
      loanSynd = loanSyndRepos.saveAll(loanSynd);
      loanSyndRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanSynd> loanSynd, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanSynd == null || loanSynd.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanSyndReposDay.deleteAll(loanSynd);	
      loanSyndReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanSyndReposMon.deleteAll(loanSynd);	
      loanSyndReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanSyndReposHist.deleteAll(loanSynd);
      loanSyndReposHist.flush();
    }
    else {
      loanSyndRepos.deleteAll(loanSynd);
      loanSyndRepos.flush();
    }
  }

}
