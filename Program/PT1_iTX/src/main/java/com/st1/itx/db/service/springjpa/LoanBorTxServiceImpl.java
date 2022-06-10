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
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.repository.online.LoanBorTxRepository;
import com.st1.itx.db.repository.day.LoanBorTxRepositoryDay;
import com.st1.itx.db.repository.mon.LoanBorTxRepositoryMon;
import com.st1.itx.db.repository.hist.LoanBorTxRepositoryHist;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanBorTxService")
@Repository
public class LoanBorTxServiceImpl extends ASpringJpaParm implements LoanBorTxService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanBorTxRepository loanBorTxRepos;

  @Autowired
  private LoanBorTxRepositoryDay loanBorTxReposDay;

  @Autowired
  private LoanBorTxRepositoryMon loanBorTxReposMon;

  @Autowired
  private LoanBorTxRepositoryHist loanBorTxReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanBorTxRepos);
    org.junit.Assert.assertNotNull(loanBorTxReposDay);
    org.junit.Assert.assertNotNull(loanBorTxReposMon);
    org.junit.Assert.assertNotNull(loanBorTxReposHist);
  }

  @Override
  public LoanBorTx findById(LoanBorTxId loanBorTxId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanBorTxId);
    Optional<LoanBorTx> loanBorTx = null;
    if (dbName.equals(ContentName.onDay))
      loanBorTx = loanBorTxReposDay.findById(loanBorTxId);
    else if (dbName.equals(ContentName.onMon))
      loanBorTx = loanBorTxReposMon.findById(loanBorTxId);
    else if (dbName.equals(ContentName.onHist))
      loanBorTx = loanBorTxReposHist.findById(loanBorTxId);
    else 
      loanBorTx = loanBorTxRepos.findById(loanBorTxId);
    LoanBorTx obj = loanBorTx.isPresent() ? loanBorTx.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanBorTx> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorTx> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "BormNo", "BorxNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "BormNo", "BorxNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorTxReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorTxReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorTxReposHist.findAll(pageable);
    else 
      slice = loanBorTxRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorTx> borxAcDateRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int acDate_5, int acDate_6, List<String> displayflag_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorTx> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("borxAcDateRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2 + " bormNo_3 : " +  bormNo_3 + " bormNo_4 : " +  bormNo_4 + " acDate_5 : " +  acDate_5 + " acDate_6 : " +  acDate_6 + " displayflag_7 : " +  displayflag_7);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorTxReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndAcDateGreaterThanEqualAndAcDateLessThanEqualAndDisplayflagInOrderByAcDateAscDisplayflagAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, acDate_5, acDate_6, displayflag_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorTxReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndAcDateGreaterThanEqualAndAcDateLessThanEqualAndDisplayflagInOrderByAcDateAscDisplayflagAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, acDate_5, acDate_6, displayflag_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorTxReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndAcDateGreaterThanEqualAndAcDateLessThanEqualAndDisplayflagInOrderByAcDateAscDisplayflagAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, acDate_5, acDate_6, displayflag_7, pageable);
    else 
      slice = loanBorTxRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndAcDateGreaterThanEqualAndAcDateLessThanEqualAndDisplayflagInOrderByAcDateAscDisplayflagAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, acDate_5, acDate_6, displayflag_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorTx> borxFacmNoEq(int custNo_0, int facmNo_1, int bormNo_2, int bormNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorTx> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("borxFacmNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " bormNo_3 : " +  bormNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorTxReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorTxReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorTxReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);
    else 
      slice = loanBorTxRepos.findAllByCustNoIsAndFacmNoIsAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorTx> borxBormNoEq(int custNo_0, int facmNo_1, int bormNo_2, int borxNo_3, int borxNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorTx> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("borxBormNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " borxNo_3 : " +  borxNo_3 + " borxNo_4 : " +  borxNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorTxReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoGreaterThanEqualAndBorxNoLessThanEqualOrderByBorxNoAsc(custNo_0, facmNo_1, bormNo_2, borxNo_3, borxNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorTxReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoGreaterThanEqualAndBorxNoLessThanEqualOrderByBorxNoAsc(custNo_0, facmNo_1, bormNo_2, borxNo_3, borxNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorTxReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoGreaterThanEqualAndBorxNoLessThanEqualOrderByBorxNoAsc(custNo_0, facmNo_1, bormNo_2, borxNo_3, borxNo_4, pageable);
    else 
      slice = loanBorTxRepos.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoGreaterThanEqualAndBorxNoLessThanEqualOrderByBorxNoAsc(custNo_0, facmNo_1, bormNo_2, borxNo_3, borxNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanBorTx borxTxtNoFirst(int acDate_0, String titaTlrNo_1, String titaTxtNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("borxTxtNoFirst " + dbName + " : " + "acDate_0 : " + acDate_0 + " titaTlrNo_1 : " +  titaTlrNo_1 + " titaTxtNo_2 : " +  titaTxtNo_2);
    Optional<LoanBorTx> loanBorTxT = null;
    if (dbName.equals(ContentName.onDay))
      loanBorTxT = loanBorTxReposDay.findTopByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2);
    else if (dbName.equals(ContentName.onMon))
      loanBorTxT = loanBorTxReposMon.findTopByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2);
    else if (dbName.equals(ContentName.onHist))
      loanBorTxT = loanBorTxReposHist.findTopByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2);
    else 
      loanBorTxT = loanBorTxRepos.findTopByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2);

    return loanBorTxT.isPresent() ? loanBorTxT.get() : null;
  }

  @Override
  public Slice<LoanBorTx> borxEntryDateRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int entryDate_5, int entryDate_6, List<String> displayflag_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorTx> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("borxEntryDateRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2 + " bormNo_3 : " +  bormNo_3 + " bormNo_4 : " +  bormNo_4 + " entryDate_5 : " +  entryDate_5 + " entryDate_6 : " +  entryDate_6 + " displayflag_7 : " +  displayflag_7);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorTxReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndDisplayflagInOrderByAcDateAscDisplayflagAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, entryDate_5, entryDate_6, displayflag_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorTxReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndDisplayflagInOrderByAcDateAscDisplayflagAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, entryDate_5, entryDate_6, displayflag_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorTxReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndDisplayflagInOrderByAcDateAscDisplayflagAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, entryDate_5, entryDate_6, displayflag_7, pageable);
    else 
      slice = loanBorTxRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndDisplayflagInOrderByAcDateAscDisplayflagAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, entryDate_5, entryDate_6, displayflag_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanBorTx custNoTxtNoFirst(int custNo_0, int facmNo_1, int bormNo_2, int acDate_3, String titaTlrNo_4, String titaTxtNo_5, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("custNoTxtNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " acDate_3 : " +  acDate_3 + " titaTlrNo_4 : " +  titaTlrNo_4 + " titaTxtNo_5 : " +  titaTxtNo_5);
    Optional<LoanBorTx> loanBorTxT = null;
    if (dbName.equals(ContentName.onDay))
      loanBorTxT = loanBorTxReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(custNo_0, facmNo_1, bormNo_2, acDate_3, titaTlrNo_4, titaTxtNo_5);
    else if (dbName.equals(ContentName.onMon))
      loanBorTxT = loanBorTxReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(custNo_0, facmNo_1, bormNo_2, acDate_3, titaTlrNo_4, titaTxtNo_5);
    else if (dbName.equals(ContentName.onHist))
      loanBorTxT = loanBorTxReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(custNo_0, facmNo_1, bormNo_2, acDate_3, titaTlrNo_4, titaTxtNo_5);
    else 
      loanBorTxT = loanBorTxRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(custNo_0, facmNo_1, bormNo_2, acDate_3, titaTlrNo_4, titaTxtNo_5);

    return loanBorTxT.isPresent() ? loanBorTxT.get() : null;
  }

  @Override
  public LoanBorTx bormNoDescFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("bormNoDescFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2);
    Optional<LoanBorTx> loanBorTxT = null;
    if (dbName.equals(ContentName.onDay))
      loanBorTxT = loanBorTxReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByBorxNoDesc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onMon))
      loanBorTxT = loanBorTxReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByBorxNoDesc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onHist))
      loanBorTxT = loanBorTxReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByBorxNoDesc(custNo_0, facmNo_1, bormNo_2);
    else 
      loanBorTxT = loanBorTxRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByBorxNoDesc(custNo_0, facmNo_1, bormNo_2);

    return loanBorTxT.isPresent() ? loanBorTxT.get() : null;
  }

  @Override
  public Slice<LoanBorTx> findByCustNoandFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorTx> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByCustNoandFacmNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorTxReposDay.findAllByCustNoIsAndFacmNoIsOrderByBormNoAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorTxReposMon.findAllByCustNoIsAndFacmNoIsOrderByBormNoAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorTxReposHist.findAllByCustNoIsAndFacmNoIsOrderByBormNoAsc(custNo_0, facmNo_1, pageable);
    else 
      slice = loanBorTxRepos.findAllByCustNoIsAndFacmNoIsOrderByBormNoAsc(custNo_0, facmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorTx> custNoTxtNoEq(int custNo_0, int acDate_1, String titaKinBr_2, String titaTlrNo_3, String titaTxtNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorTx> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoTxtNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " acDate_1 : " +  acDate_1 + " titaKinBr_2 : " +  titaKinBr_2 + " titaTlrNo_3 : " +  titaTlrNo_3 + " titaTxtNo_4 : " +  titaTxtNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorTxReposDay.findAllByCustNoIsAndAcDateIsAndTitaKinBrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByFacmNoAscBormNoAsc(custNo_0, acDate_1, titaKinBr_2, titaTlrNo_3, titaTxtNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorTxReposMon.findAllByCustNoIsAndAcDateIsAndTitaKinBrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByFacmNoAscBormNoAsc(custNo_0, acDate_1, titaKinBr_2, titaTlrNo_3, titaTxtNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorTxReposHist.findAllByCustNoIsAndAcDateIsAndTitaKinBrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByFacmNoAscBormNoAsc(custNo_0, acDate_1, titaKinBr_2, titaTlrNo_3, titaTxtNo_4, pageable);
    else 
      slice = loanBorTxRepos.findAllByCustNoIsAndAcDateIsAndTitaKinBrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByFacmNoAscBormNoAsc(custNo_0, acDate_1, titaKinBr_2, titaTlrNo_3, titaTxtNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorTx> findDueDateRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int dueDate_5, int dueDate_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorTx> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findDueDateRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2 + " bormNo_3 : " +  bormNo_3 + " bormNo_4 : " +  bormNo_4 + " dueDate_5 : " +  dueDate_5 + " dueDate_6 : " +  dueDate_6);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorTxReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndDueDateGreaterThanEqualAndDueDateLessThanEqualOrderByDueDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, dueDate_5, dueDate_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorTxReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndDueDateGreaterThanEqualAndDueDateLessThanEqualOrderByDueDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, dueDate_5, dueDate_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorTxReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndDueDateGreaterThanEqualAndDueDateLessThanEqualOrderByDueDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, dueDate_5, dueDate_6, pageable);
    else 
      slice = loanBorTxRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndDueDateGreaterThanEqualAndDueDateLessThanEqualOrderByDueDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, dueDate_5, dueDate_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorTx> findAcDateRange(int acDate_0, int acDate_1, List<String> titaHCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorTx> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findAcDateRange " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " titaHCode_2 : " +  titaHCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorTxReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndTitaHCodeInOrderByAcDateAscCustNoAscFacmNoAscBormNoAscBorxNoAsc(acDate_0, acDate_1, titaHCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorTxReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndTitaHCodeInOrderByAcDateAscCustNoAscFacmNoAscBormNoAscBorxNoAsc(acDate_0, acDate_1, titaHCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorTxReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndTitaHCodeInOrderByAcDateAscCustNoAscFacmNoAscBormNoAscBorxNoAsc(acDate_0, acDate_1, titaHCode_2, pageable);
    else 
      slice = loanBorTxRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndTitaHCodeInOrderByAcDateAscCustNoAscFacmNoAscBormNoAscBorxNoAsc(acDate_0, acDate_1, titaHCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorTx> findIntEndDateEq(int custNo_0, int facmNo_1, int bormNo_2, int bormNo_3, int intEndDate_4, List<String> titaHCode_5, int acDate_6, String titaTlrNo_7, String titaTxtNo_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorTx> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findIntEndDateEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " bormNo_3 : " +  bormNo_3 + " intEndDate_4 : " +  intEndDate_4 + " titaHCode_5 : " +  titaHCode_5 + " acDate_6 : " +  acDate_6 + " titaTlrNo_7 : " +  titaTlrNo_7 + " titaTxtNo_8 : " +  titaTxtNo_8);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorTxReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndIntEndDateIsAndTitaHCodeInAndAcDateIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, intEndDate_4, titaHCode_5, acDate_6, titaTlrNo_7, titaTxtNo_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorTxReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndIntEndDateIsAndTitaHCodeInAndAcDateIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, intEndDate_4, titaHCode_5, acDate_6, titaTlrNo_7, titaTxtNo_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorTxReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndIntEndDateIsAndTitaHCodeInAndAcDateIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, intEndDate_4, titaHCode_5, acDate_6, titaTlrNo_7, titaTxtNo_8, pageable);
    else 
      slice = loanBorTxRepos.findAllByCustNoIsAndFacmNoIsAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndIntEndDateIsAndTitaHCodeInAndAcDateIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, intEndDate_4, titaHCode_5, acDate_6, titaTlrNo_7, titaTxtNo_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorTx> borxIntEndDateDescRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int entryDate_5, int entryDate_6, List<String> displayflag_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorTx> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("borxIntEndDateDescRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2 + " bormNo_3 : " +  bormNo_3 + " bormNo_4 : " +  bormNo_4 + " entryDate_5 : " +  entryDate_5 + " entryDate_6 : " +  entryDate_6 + " displayflag_7 : " +  displayflag_7);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorTxReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndDisplayflagInOrderByIntEndDateDescAcDateAscTitaKinBrAscTitaTlrNoAscTitaTxtNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, entryDate_5, entryDate_6, displayflag_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorTxReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndDisplayflagInOrderByIntEndDateDescAcDateAscTitaKinBrAscTitaTlrNoAscTitaTxtNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, entryDate_5, entryDate_6, displayflag_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorTxReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndDisplayflagInOrderByIntEndDateDescAcDateAscTitaKinBrAscTitaTlrNoAscTitaTxtNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, entryDate_5, entryDate_6, displayflag_7, pageable);
    else 
      slice = loanBorTxRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndDisplayflagInOrderByIntEndDateDescAcDateAscTitaKinBrAscTitaTlrNoAscTitaTxtNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, entryDate_5, entryDate_6, displayflag_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanBorTx holdById(LoanBorTxId loanBorTxId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanBorTxId);
    Optional<LoanBorTx> loanBorTx = null;
    if (dbName.equals(ContentName.onDay))
      loanBorTx = loanBorTxReposDay.findByLoanBorTxId(loanBorTxId);
    else if (dbName.equals(ContentName.onMon))
      loanBorTx = loanBorTxReposMon.findByLoanBorTxId(loanBorTxId);
    else if (dbName.equals(ContentName.onHist))
      loanBorTx = loanBorTxReposHist.findByLoanBorTxId(loanBorTxId);
    else 
      loanBorTx = loanBorTxRepos.findByLoanBorTxId(loanBorTxId);
    return loanBorTx.isPresent() ? loanBorTx.get() : null;
  }

  @Override
  public LoanBorTx holdById(LoanBorTx loanBorTx, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanBorTx.getLoanBorTxId());
    Optional<LoanBorTx> loanBorTxT = null;
    if (dbName.equals(ContentName.onDay))
      loanBorTxT = loanBorTxReposDay.findByLoanBorTxId(loanBorTx.getLoanBorTxId());
    else if (dbName.equals(ContentName.onMon))
      loanBorTxT = loanBorTxReposMon.findByLoanBorTxId(loanBorTx.getLoanBorTxId());
    else if (dbName.equals(ContentName.onHist))
      loanBorTxT = loanBorTxReposHist.findByLoanBorTxId(loanBorTx.getLoanBorTxId());
    else 
      loanBorTxT = loanBorTxRepos.findByLoanBorTxId(loanBorTx.getLoanBorTxId());
    return loanBorTxT.isPresent() ? loanBorTxT.get() : null;
  }

  @Override
  public LoanBorTx insert(LoanBorTx loanBorTx, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanBorTx.getLoanBorTxId());
    if (this.findById(loanBorTx.getLoanBorTxId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanBorTx.setCreateEmpNo(empNot);

    if(loanBorTx.getLastUpdateEmpNo() == null || loanBorTx.getLastUpdateEmpNo().isEmpty())
      loanBorTx.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanBorTxReposDay.saveAndFlush(loanBorTx);	
    else if (dbName.equals(ContentName.onMon))
      return loanBorTxReposMon.saveAndFlush(loanBorTx);
    else if (dbName.equals(ContentName.onHist))
      return loanBorTxReposHist.saveAndFlush(loanBorTx);
    else 
    return loanBorTxRepos.saveAndFlush(loanBorTx);
  }

  @Override
  public LoanBorTx update(LoanBorTx loanBorTx, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanBorTx.getLoanBorTxId());
    if (!empNot.isEmpty())
      loanBorTx.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanBorTxReposDay.saveAndFlush(loanBorTx);	
    else if (dbName.equals(ContentName.onMon))
      return loanBorTxReposMon.saveAndFlush(loanBorTx);
    else if (dbName.equals(ContentName.onHist))
      return loanBorTxReposHist.saveAndFlush(loanBorTx);
    else 
    return loanBorTxRepos.saveAndFlush(loanBorTx);
  }

  @Override
  public LoanBorTx update2(LoanBorTx loanBorTx, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanBorTx.getLoanBorTxId());
    if (!empNot.isEmpty())
      loanBorTx.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanBorTxReposDay.saveAndFlush(loanBorTx);	
    else if (dbName.equals(ContentName.onMon))
      loanBorTxReposMon.saveAndFlush(loanBorTx);
    else if (dbName.equals(ContentName.onHist))
        loanBorTxReposHist.saveAndFlush(loanBorTx);
    else 
      loanBorTxRepos.saveAndFlush(loanBorTx);	
    return this.findById(loanBorTx.getLoanBorTxId());
  }

  @Override
  public void delete(LoanBorTx loanBorTx, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanBorTx.getLoanBorTxId());
    if (dbName.equals(ContentName.onDay)) {
      loanBorTxReposDay.delete(loanBorTx);	
      loanBorTxReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBorTxReposMon.delete(loanBorTx);	
      loanBorTxReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBorTxReposHist.delete(loanBorTx);
      loanBorTxReposHist.flush();
    }
    else {
      loanBorTxRepos.delete(loanBorTx);
      loanBorTxRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanBorTx> loanBorTx, TitaVo... titaVo) throws DBException {
    if (loanBorTx == null || loanBorTx.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanBorTx t : loanBorTx){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanBorTx = loanBorTxReposDay.saveAll(loanBorTx);	
      loanBorTxReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBorTx = loanBorTxReposMon.saveAll(loanBorTx);	
      loanBorTxReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBorTx = loanBorTxReposHist.saveAll(loanBorTx);
      loanBorTxReposHist.flush();
    }
    else {
      loanBorTx = loanBorTxRepos.saveAll(loanBorTx);
      loanBorTxRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanBorTx> loanBorTx, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanBorTx == null || loanBorTx.size() == 0)
      throw new DBException(6);

    for (LoanBorTx t : loanBorTx) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanBorTx = loanBorTxReposDay.saveAll(loanBorTx);	
      loanBorTxReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBorTx = loanBorTxReposMon.saveAll(loanBorTx);	
      loanBorTxReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBorTx = loanBorTxReposHist.saveAll(loanBorTx);
      loanBorTxReposHist.flush();
    }
    else {
      loanBorTx = loanBorTxRepos.saveAll(loanBorTx);
      loanBorTxRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanBorTx> loanBorTx, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanBorTx == null || loanBorTx.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanBorTxReposDay.deleteAll(loanBorTx);	
      loanBorTxReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBorTxReposMon.deleteAll(loanBorTx);	
      loanBorTxReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBorTxReposHist.deleteAll(loanBorTx);
      loanBorTxReposHist.flush();
    }
    else {
      loanBorTxRepos.deleteAll(loanBorTx);
      loanBorTxRepos.flush();
    }
  }

}
