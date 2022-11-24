package com.st1.itx.db.service.springjpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

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
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.LoanChequeId;
import com.st1.itx.db.repository.online.LoanChequeRepository;
import com.st1.itx.db.repository.day.LoanChequeRepositoryDay;
import com.st1.itx.db.repository.mon.LoanChequeRepositoryMon;
import com.st1.itx.db.repository.hist.LoanChequeRepositoryHist;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanChequeService")
@Repository
public class LoanChequeServiceImpl extends ASpringJpaParm implements LoanChequeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanChequeRepository loanChequeRepos;

  @Autowired
  private LoanChequeRepositoryDay loanChequeReposDay;

  @Autowired
  private LoanChequeRepositoryMon loanChequeReposMon;

  @Autowired
  private LoanChequeRepositoryHist loanChequeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanChequeRepos);
    org.junit.Assert.assertNotNull(loanChequeReposDay);
    org.junit.Assert.assertNotNull(loanChequeReposMon);
    org.junit.Assert.assertNotNull(loanChequeReposHist);
  }

  @Override
  public LoanCheque findById(LoanChequeId loanChequeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanChequeId);
    Optional<LoanCheque> loanCheque = null;
    if (dbName.equals(ContentName.onDay))
      loanCheque = loanChequeReposDay.findById(loanChequeId);
    else if (dbName.equals(ContentName.onMon))
      loanCheque = loanChequeReposMon.findById(loanChequeId);
    else if (dbName.equals(ContentName.onHist))
      loanCheque = loanChequeReposHist.findById(loanChequeId);
    else 
      loanCheque = loanChequeRepos.findById(loanChequeId);
    LoanCheque obj = loanCheque.isPresent() ? loanCheque.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanCheque> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanCheque> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ChequeAcct", "ChequeNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ChequeAcct", "ChequeNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanChequeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanChequeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanChequeReposHist.findAll(pageable);
    else 
      slice = loanChequeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanCheque> chequeDateRange(int chequeDate_0, int chequeDate_1, int chequeAcct_2, int chequeAcct_3, int chequeNo_4, int chequeNo_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanCheque> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("chequeDateRange " + dbName + " : " + "chequeDate_0 : " + chequeDate_0 + " chequeDate_1 : " +  chequeDate_1 + " chequeAcct_2 : " +  chequeAcct_2 + " chequeAcct_3 : " +  chequeAcct_3 + " chequeNo_4 : " +  chequeNo_4 + " chequeNo_5 : " +  chequeNo_5);
    if (dbName.equals(ContentName.onDay))
      slice = loanChequeReposDay.findAllByChequeDateGreaterThanEqualAndChequeDateLessThanEqualAndChequeAcctGreaterThanEqualAndChequeAcctLessThanEqualAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualOrderByChequeDateAscCustNoAscChequeAcctAscChequeNoAsc(chequeDate_0, chequeDate_1, chequeAcct_2, chequeAcct_3, chequeNo_4, chequeNo_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanChequeReposMon.findAllByChequeDateGreaterThanEqualAndChequeDateLessThanEqualAndChequeAcctGreaterThanEqualAndChequeAcctLessThanEqualAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualOrderByChequeDateAscCustNoAscChequeAcctAscChequeNoAsc(chequeDate_0, chequeDate_1, chequeAcct_2, chequeAcct_3, chequeNo_4, chequeNo_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanChequeReposHist.findAllByChequeDateGreaterThanEqualAndChequeDateLessThanEqualAndChequeAcctGreaterThanEqualAndChequeAcctLessThanEqualAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualOrderByChequeDateAscCustNoAscChequeAcctAscChequeNoAsc(chequeDate_0, chequeDate_1, chequeAcct_2, chequeAcct_3, chequeNo_4, chequeNo_5, pageable);
    else 
      slice = loanChequeRepos.findAllByChequeDateGreaterThanEqualAndChequeDateLessThanEqualAndChequeAcctGreaterThanEqualAndChequeAcctLessThanEqualAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualOrderByChequeDateAscCustNoAscChequeAcctAscChequeNoAsc(chequeDate_0, chequeDate_1, chequeAcct_2, chequeAcct_3, chequeNo_4, chequeNo_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanCheque> chequeCustNoEq(int custNo_0, List<String> statusCode_1, int chequeDate_2, int chequeDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanCheque> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("chequeCustNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " statusCode_1 : " +  statusCode_1 + " chequeDate_2 : " +  chequeDate_2 + " chequeDate_3 : " +  chequeDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = loanChequeReposDay.findAllByCustNoIsAndStatusCodeInAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeAcctAscChequeNoAsc(custNo_0, statusCode_1, chequeDate_2, chequeDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanChequeReposMon.findAllByCustNoIsAndStatusCodeInAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeAcctAscChequeNoAsc(custNo_0, statusCode_1, chequeDate_2, chequeDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanChequeReposHist.findAllByCustNoIsAndStatusCodeInAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeAcctAscChequeNoAsc(custNo_0, statusCode_1, chequeDate_2, chequeDate_3, pageable);
    else 
      slice = loanChequeRepos.findAllByCustNoIsAndStatusCodeInAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeAcctAscChequeNoAsc(custNo_0, statusCode_1, chequeDate_2, chequeDate_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanCheque> acDateRange(int acDate_0, int acDate_1, List<String> statusCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanCheque> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acDateRange " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " statusCode_2 : " +  statusCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = loanChequeReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndStatusCodeIn(acDate_0, acDate_1, statusCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanChequeReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndStatusCodeIn(acDate_0, acDate_1, statusCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanChequeReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndStatusCodeIn(acDate_0, acDate_1, statusCode_2, pageable);
    else 
      slice = loanChequeRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndStatusCodeIn(acDate_0, acDate_1, statusCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanCheque> statusCodeRange(List<String> statusCode_0, int chequeDate_1, int chequeDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanCheque> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("statusCodeRange " + dbName + " : " + "statusCode_0 : " + statusCode_0 + " chequeDate_1 : " +  chequeDate_1 + " chequeDate_2 : " +  chequeDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = loanChequeReposDay.findAllByStatusCodeInAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeDateAscChequeAcctAscChequeNoAsc(statusCode_0, chequeDate_1, chequeDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanChequeReposMon.findAllByStatusCodeInAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeDateAscChequeAcctAscChequeNoAsc(statusCode_0, chequeDate_1, chequeDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanChequeReposHist.findAllByStatusCodeInAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeDateAscChequeAcctAscChequeNoAsc(statusCode_0, chequeDate_1, chequeDate_2, pageable);
    else 
      slice = loanChequeRepos.findAllByStatusCodeInAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeDateAscChequeAcctAscChequeNoAsc(statusCode_0, chequeDate_1, chequeDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanCheque> forStatusCodeSelect(int chequeDate_0, int chequeDate_1, int chequeAcct_2, int chequeAcct_3, int chequeNo_4, int chequeNo_5, String statusCode_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanCheque> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("forStatusCodeSelect " + dbName + " : " + "chequeDate_0 : " + chequeDate_0 + " chequeDate_1 : " +  chequeDate_1 + " chequeAcct_2 : " +  chequeAcct_2 + " chequeAcct_3 : " +  chequeAcct_3 + " chequeNo_4 : " +  chequeNo_4 + " chequeNo_5 : " +  chequeNo_5 + " statusCode_6 : " +  statusCode_6);
    if (dbName.equals(ContentName.onDay))
      slice = loanChequeReposDay.findAllByChequeDateGreaterThanEqualAndChequeDateLessThanEqualAndChequeAcctGreaterThanEqualAndChequeAcctLessThanEqualAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualAndStatusCodeIsOrderByChequeDateAscCustNoAscChequeAcctAscChequeNoAsc(chequeDate_0, chequeDate_1, chequeAcct_2, chequeAcct_3, chequeNo_4, chequeNo_5, statusCode_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanChequeReposMon.findAllByChequeDateGreaterThanEqualAndChequeDateLessThanEqualAndChequeAcctGreaterThanEqualAndChequeAcctLessThanEqualAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualAndStatusCodeIsOrderByChequeDateAscCustNoAscChequeAcctAscChequeNoAsc(chequeDate_0, chequeDate_1, chequeAcct_2, chequeAcct_3, chequeNo_4, chequeNo_5, statusCode_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanChequeReposHist.findAllByChequeDateGreaterThanEqualAndChequeDateLessThanEqualAndChequeAcctGreaterThanEqualAndChequeAcctLessThanEqualAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualAndStatusCodeIsOrderByChequeDateAscCustNoAscChequeAcctAscChequeNoAsc(chequeDate_0, chequeDate_1, chequeAcct_2, chequeAcct_3, chequeNo_4, chequeNo_5, statusCode_6, pageable);
    else 
      slice = loanChequeRepos.findAllByChequeDateGreaterThanEqualAndChequeDateLessThanEqualAndChequeAcctGreaterThanEqualAndChequeAcctLessThanEqualAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualAndStatusCodeIsOrderByChequeDateAscCustNoAscChequeAcctAscChequeNoAsc(chequeDate_0, chequeDate_1, chequeAcct_2, chequeAcct_3, chequeNo_4, chequeNo_5, statusCode_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanCheque> receiveDateRange(int receiveDate_0, int receiveDate_1, List<String> statusCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanCheque> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("receiveDateRange " + dbName + " : " + "receiveDate_0 : " + receiveDate_0 + " receiveDate_1 : " +  receiveDate_1 + " statusCode_2 : " +  statusCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = loanChequeReposDay.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndStatusCodeIn(receiveDate_0, receiveDate_1, statusCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanChequeReposMon.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndStatusCodeIn(receiveDate_0, receiveDate_1, statusCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanChequeReposHist.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndStatusCodeIn(receiveDate_0, receiveDate_1, statusCode_2, pageable);
    else 
      slice = loanChequeRepos.findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndStatusCodeIn(receiveDate_0, receiveDate_1, statusCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanCheque> custNoChequeRange(int custNo_0, int custNo_1, List<String> statusCode_2, int chequeNo_3, int chequeNo_4, int chequeDate_5, int chequeDate_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanCheque> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoChequeRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " +  custNo_1 + " statusCode_2 : " +  statusCode_2 + " chequeNo_3 : " +  chequeNo_3 + " chequeNo_4 : " +  chequeNo_4 + " chequeDate_5 : " +  chequeDate_5 + " chequeDate_6 : " +  chequeDate_6);
    if (dbName.equals(ContentName.onDay))
      slice = loanChequeReposDay.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndStatusCodeInAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeAcctAscChequeNoAsc(custNo_0, custNo_1, statusCode_2, chequeNo_3, chequeNo_4, chequeDate_5, chequeDate_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanChequeReposMon.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndStatusCodeInAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeAcctAscChequeNoAsc(custNo_0, custNo_1, statusCode_2, chequeNo_3, chequeNo_4, chequeDate_5, chequeDate_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanChequeReposHist.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndStatusCodeInAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeAcctAscChequeNoAsc(custNo_0, custNo_1, statusCode_2, chequeNo_3, chequeNo_4, chequeDate_5, chequeDate_6, pageable);
    else 
      slice = loanChequeRepos.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndStatusCodeInAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeAcctAscChequeNoAsc(custNo_0, custNo_1, statusCode_2, chequeNo_3, chequeNo_4, chequeDate_5, chequeDate_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanCheque holdById(LoanChequeId loanChequeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanChequeId);
    Optional<LoanCheque> loanCheque = null;
    if (dbName.equals(ContentName.onDay))
      loanCheque = loanChequeReposDay.findByLoanChequeId(loanChequeId);
    else if (dbName.equals(ContentName.onMon))
      loanCheque = loanChequeReposMon.findByLoanChequeId(loanChequeId);
    else if (dbName.equals(ContentName.onHist))
      loanCheque = loanChequeReposHist.findByLoanChequeId(loanChequeId);
    else 
      loanCheque = loanChequeRepos.findByLoanChequeId(loanChequeId);
    return loanCheque.isPresent() ? loanCheque.get() : null;
  }

  @Override
  public LoanCheque holdById(LoanCheque loanCheque, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanCheque.getLoanChequeId());
    Optional<LoanCheque> loanChequeT = null;
    if (dbName.equals(ContentName.onDay))
      loanChequeT = loanChequeReposDay.findByLoanChequeId(loanCheque.getLoanChequeId());
    else if (dbName.equals(ContentName.onMon))
      loanChequeT = loanChequeReposMon.findByLoanChequeId(loanCheque.getLoanChequeId());
    else if (dbName.equals(ContentName.onHist))
      loanChequeT = loanChequeReposHist.findByLoanChequeId(loanCheque.getLoanChequeId());
    else 
      loanChequeT = loanChequeRepos.findByLoanChequeId(loanCheque.getLoanChequeId());
    return loanChequeT.isPresent() ? loanChequeT.get() : null;
  }

  @Override
  public LoanCheque insert(LoanCheque loanCheque, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanCheque.getLoanChequeId());
    if (this.findById(loanCheque.getLoanChequeId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanCheque.setCreateEmpNo(empNot);

    if(loanCheque.getLastUpdateEmpNo() == null || loanCheque.getLastUpdateEmpNo().isEmpty())
      loanCheque.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanChequeReposDay.saveAndFlush(loanCheque);	
    else if (dbName.equals(ContentName.onMon))
      return loanChequeReposMon.saveAndFlush(loanCheque);
    else if (dbName.equals(ContentName.onHist))
      return loanChequeReposHist.saveAndFlush(loanCheque);
    else 
    return loanChequeRepos.saveAndFlush(loanCheque);
  }

  @Override
  public LoanCheque update(LoanCheque loanCheque, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanCheque.getLoanChequeId());
    if (!empNot.isEmpty())
      loanCheque.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanChequeReposDay.saveAndFlush(loanCheque);	
    else if (dbName.equals(ContentName.onMon))
      return loanChequeReposMon.saveAndFlush(loanCheque);
    else if (dbName.equals(ContentName.onHist))
      return loanChequeReposHist.saveAndFlush(loanCheque);
    else 
    return loanChequeRepos.saveAndFlush(loanCheque);
  }

  @Override
  public LoanCheque update2(LoanCheque loanCheque, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanCheque.getLoanChequeId());
    if (!empNot.isEmpty())
      loanCheque.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanChequeReposDay.saveAndFlush(loanCheque);	
    else if (dbName.equals(ContentName.onMon))
      loanChequeReposMon.saveAndFlush(loanCheque);
    else if (dbName.equals(ContentName.onHist))
        loanChequeReposHist.saveAndFlush(loanCheque);
    else 
      loanChequeRepos.saveAndFlush(loanCheque);	
    return this.findById(loanCheque.getLoanChequeId());
  }

  @Override
  public void delete(LoanCheque loanCheque, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanCheque.getLoanChequeId());
    if (dbName.equals(ContentName.onDay)) {
      loanChequeReposDay.delete(loanCheque);	
      loanChequeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanChequeReposMon.delete(loanCheque);	
      loanChequeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanChequeReposHist.delete(loanCheque);
      loanChequeReposHist.flush();
    }
    else {
      loanChequeRepos.delete(loanCheque);
      loanChequeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanCheque> loanCheque, TitaVo... titaVo) throws DBException {
    if (loanCheque == null || loanCheque.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanCheque t : loanCheque){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanCheque = loanChequeReposDay.saveAll(loanCheque);	
      loanChequeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanCheque = loanChequeReposMon.saveAll(loanCheque);	
      loanChequeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanCheque = loanChequeReposHist.saveAll(loanCheque);
      loanChequeReposHist.flush();
    }
    else {
      loanCheque = loanChequeRepos.saveAll(loanCheque);
      loanChequeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanCheque> loanCheque, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanCheque == null || loanCheque.size() == 0)
      throw new DBException(6);

    for (LoanCheque t : loanCheque) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanCheque = loanChequeReposDay.saveAll(loanCheque);	
      loanChequeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanCheque = loanChequeReposMon.saveAll(loanCheque);	
      loanChequeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanCheque = loanChequeReposHist.saveAll(loanCheque);
      loanChequeReposHist.flush();
    }
    else {
      loanCheque = loanChequeRepos.saveAll(loanCheque);
      loanChequeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanCheque> loanCheque, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanCheque == null || loanCheque.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanChequeReposDay.deleteAll(loanCheque);	
      loanChequeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanChequeReposMon.deleteAll(loanCheque);	
      loanChequeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanChequeReposHist.deleteAll(loanCheque);
      loanChequeReposHist.flush();
    }
    else {
      loanChequeRepos.deleteAll(loanCheque);
      loanChequeRepos.flush();
    }
  }

}
