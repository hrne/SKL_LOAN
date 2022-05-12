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
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.repository.online.AcReceivableRepository;
import com.st1.itx.db.repository.day.AcReceivableRepositoryDay;
import com.st1.itx.db.repository.mon.AcReceivableRepositoryMon;
import com.st1.itx.db.repository.hist.AcReceivableRepositoryHist;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("acReceivableService")
@Repository
public class AcReceivableServiceImpl extends ASpringJpaParm implements AcReceivableService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AcReceivableRepository acReceivableRepos;

  @Autowired
  private AcReceivableRepositoryDay acReceivableReposDay;

  @Autowired
  private AcReceivableRepositoryMon acReceivableReposMon;

  @Autowired
  private AcReceivableRepositoryHist acReceivableReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(acReceivableRepos);
    org.junit.Assert.assertNotNull(acReceivableReposDay);
    org.junit.Assert.assertNotNull(acReceivableReposMon);
    org.junit.Assert.assertNotNull(acReceivableReposHist);
  }

  @Override
  public AcReceivable findById(AcReceivableId acReceivableId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + acReceivableId);
    Optional<AcReceivable> acReceivable = null;
    if (dbName.equals(ContentName.onDay))
      acReceivable = acReceivableReposDay.findById(acReceivableId);
    else if (dbName.equals(ContentName.onMon))
      acReceivable = acReceivableReposMon.findById(acReceivableId);
    else if (dbName.equals(ContentName.onHist))
      acReceivable = acReceivableReposHist.findById(acReceivableId);
    else 
      acReceivable = acReceivableRepos.findById(acReceivableId);
    AcReceivable obj = acReceivable.isPresent() ? acReceivable.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AcReceivable> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcctCode", "CustNo", "FacmNo", "RvNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcctCode", "CustNo", "FacmNo", "RvNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAll(pageable);
    else 
      slice = acReceivableRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> acrvClsFlagEq(int clsFlag_0, String branchNo_1, String currencyCode_2, String acNoCode_3, String acSubCode_4, String acDtlCode_5, int custNo_6, int custNo_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acrvClsFlagEq " + dbName + " : " + "clsFlag_0 : " + clsFlag_0 + " branchNo_1 : " +  branchNo_1 + " currencyCode_2 : " +  currencyCode_2 + " acNoCode_3 : " +  acNoCode_3 + " acSubCode_4 : " +  acSubCode_4 + " acDtlCode_5 : " +  acDtlCode_5 + " custNo_6 : " +  custNo_6 + " custNo_7 : " +  custNo_7);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByClsFlagIsAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(clsFlag_0, branchNo_1, currencyCode_2, acNoCode_3, acSubCode_4, acDtlCode_5, custNo_6, custNo_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByClsFlagIsAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(clsFlag_0, branchNo_1, currencyCode_2, acNoCode_3, acSubCode_4, acDtlCode_5, custNo_6, custNo_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByClsFlagIsAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(clsFlag_0, branchNo_1, currencyCode_2, acNoCode_3, acSubCode_4, acDtlCode_5, custNo_6, custNo_7, pageable);
    else 
      slice = acReceivableRepos.findAllByClsFlagIsAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(clsFlag_0, branchNo_1, currencyCode_2, acNoCode_3, acSubCode_4, acDtlCode_5, custNo_6, custNo_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AcReceivable acrvFacmNoFirst(int custNo_0, int acctFlag_1, int facmNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("acrvFacmNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " acctFlag_1 : " +  acctFlag_1 + " facmNo_2 : " +  facmNo_2);
    Optional<AcReceivable> acReceivableT = null;
    if (dbName.equals(ContentName.onDay))
      acReceivableT = acReceivableReposDay.findTopByCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualOrderByFacmNoDescRvNoDesc(custNo_0, acctFlag_1, facmNo_2);
    else if (dbName.equals(ContentName.onMon))
      acReceivableT = acReceivableReposMon.findTopByCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualOrderByFacmNoDescRvNoDesc(custNo_0, acctFlag_1, facmNo_2);
    else if (dbName.equals(ContentName.onHist))
      acReceivableT = acReceivableReposHist.findTopByCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualOrderByFacmNoDescRvNoDesc(custNo_0, acctFlag_1, facmNo_2);
    else 
      acReceivableT = acReceivableRepos.findTopByCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualOrderByFacmNoDescRvNoDesc(custNo_0, acctFlag_1, facmNo_2);

    return acReceivableT.isPresent() ? acReceivableT.get() : null;
  }

  @Override
  public Slice<AcReceivable> acrvRvNoEq(String acctCode_0, int custNo_1, String rvNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acrvRvNoEq " + dbName + " : " + "acctCode_0 : " + acctCode_0 + " custNo_1 : " +  custNo_1 + " rvNo_2 : " +  rvNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByAcctCodeIsAndCustNoIsAndRvNoIsOrderByFacmNoAsc(acctCode_0, custNo_1, rvNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByAcctCodeIsAndCustNoIsAndRvNoIsOrderByFacmNoAsc(acctCode_0, custNo_1, rvNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByAcctCodeIsAndCustNoIsAndRvNoIsOrderByFacmNoAsc(acctCode_0, custNo_1, rvNo_2, pageable);
    else 
      slice = acReceivableRepos.findAllByAcctCodeIsAndCustNoIsAndRvNoIsOrderByFacmNoAsc(acctCode_0, custNo_1, rvNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> acrvFacmNoRange(int clsFlag_0, int custNo_1, int acctFlag_2, int facmNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acrvFacmNoRange " + dbName + " : " + "clsFlag_0 : " + clsFlag_0 + " custNo_1 : " +  custNo_1 + " acctFlag_2 : " +  acctFlag_2 + " facmNo_3 : " +  facmNo_3 + " facmNo_4 : " +  facmNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByClsFlagIsAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByOpenAcDateAscRvNoAsc(clsFlag_0, custNo_1, acctFlag_2, facmNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByClsFlagIsAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByOpenAcDateAscRvNoAsc(clsFlag_0, custNo_1, acctFlag_2, facmNo_3, facmNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByClsFlagIsAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByOpenAcDateAscRvNoAsc(clsFlag_0, custNo_1, acctFlag_2, facmNo_3, facmNo_4, pageable);
    else 
      slice = acReceivableRepos.findAllByClsFlagIsAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByOpenAcDateAscRvNoAsc(clsFlag_0, custNo_1, acctFlag_2, facmNo_3, facmNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> acrvOpenAcDateLq(String acctCode_0, int clsFlag_1, int openAcDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acrvOpenAcDateLq " + dbName + " : " + "acctCode_0 : " + acctCode_0 + " clsFlag_1 : " +  clsFlag_1 + " openAcDate_2 : " +  openAcDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByAcctCodeIsAndClsFlagIsAndOpenAcDateLessThanOrderByCustNoAscFacmNoAscRvNoAsc(acctCode_0, clsFlag_1, openAcDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByAcctCodeIsAndClsFlagIsAndOpenAcDateLessThanOrderByCustNoAscFacmNoAscRvNoAsc(acctCode_0, clsFlag_1, openAcDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByAcctCodeIsAndClsFlagIsAndOpenAcDateLessThanOrderByCustNoAscFacmNoAscRvNoAsc(acctCode_0, clsFlag_1, openAcDate_2, pageable);
    else 
      slice = acReceivableRepos.findAllByAcctCodeIsAndClsFlagIsAndOpenAcDateLessThanOrderByCustNoAscFacmNoAscRvNoAsc(acctCode_0, clsFlag_1, openAcDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> UseL5074(int clsFlag_0, List<String> acctCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("UseL5074 " + dbName + " : " + "clsFlag_0 : " + clsFlag_0 + " acctCode_1 : " +  acctCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByClsFlagIsAndAcctCodeInOrderByCustNoAsc(clsFlag_0, acctCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByClsFlagIsAndAcctCodeInOrderByCustNoAsc(clsFlag_0, acctCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByClsFlagIsAndAcctCodeInOrderByCustNoAsc(clsFlag_0, acctCode_1, pageable);
    else 
      slice = acReceivableRepos.findAllByClsFlagIsAndAcctCodeInOrderByCustNoAsc(clsFlag_0, acctCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> acctCodeEq(int clsFlag_0, String acctCode_1, int custNo_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acctCodeEq " + dbName + " : " + "clsFlag_0 : " + clsFlag_0 + " acctCode_1 : " +  acctCode_1 + " custNo_2 : " +  custNo_2 + " custNo_3 : " +  custNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByClsFlagIsAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(clsFlag_0, acctCode_1, custNo_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByClsFlagIsAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(clsFlag_0, acctCode_1, custNo_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByClsFlagIsAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(clsFlag_0, acctCode_1, custNo_2, custNo_3, pageable);
    else 
      slice = acReceivableRepos.findAllByClsFlagIsAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(clsFlag_0, acctCode_1, custNo_2, custNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> acrvOpenAcDateRange(String branchNo_0, String currencyCode_1, String acNoCode_2, String acSubCode_3, String acDtlCode_4, int openAcDate_5, int openAcDate_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acrvOpenAcDateRange " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acNoCode_2 : " +  acNoCode_2 + " acSubCode_3 : " +  acSubCode_3 + " acDtlCode_4 : " +  acDtlCode_4 + " openAcDate_5 : " +  openAcDate_5 + " openAcDate_6 : " +  openAcDate_6);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndOpenAcDateGreaterThanEqualAndOpenAcDateLessThanEqualOrderByOpenAcDateAscCustNoAscFacmNoAsc(branchNo_0, currencyCode_1, acNoCode_2, acSubCode_3, acDtlCode_4, openAcDate_5, openAcDate_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndOpenAcDateGreaterThanEqualAndOpenAcDateLessThanEqualOrderByOpenAcDateAscCustNoAscFacmNoAsc(branchNo_0, currencyCode_1, acNoCode_2, acSubCode_3, acDtlCode_4, openAcDate_5, openAcDate_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndOpenAcDateGreaterThanEqualAndOpenAcDateLessThanEqualOrderByOpenAcDateAscCustNoAscFacmNoAsc(branchNo_0, currencyCode_1, acNoCode_2, acSubCode_3, acDtlCode_4, openAcDate_5, openAcDate_6, pageable);
    else 
      slice = acReceivableRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndOpenAcDateGreaterThanEqualAndOpenAcDateLessThanEqualOrderByOpenAcDateAscCustNoAscFacmNoAsc(branchNo_0, currencyCode_1, acNoCode_2, acSubCode_3, acDtlCode_4, openAcDate_5, openAcDate_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AcReceivable useL2670First(String acctCode_0, int custNo_1, int facmNo_2, int openAcDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("useL2670First " + dbName + " : " + "acctCode_0 : " + acctCode_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2 + " openAcDate_3 : " +  openAcDate_3);
    Optional<AcReceivable> acReceivableT = null;
    if (dbName.equals(ContentName.onDay))
      acReceivableT = acReceivableReposDay.findTopByAcctCodeIsAndCustNoIsAndFacmNoIsAndOpenAcDateIsOrderByRvNoDesc(acctCode_0, custNo_1, facmNo_2, openAcDate_3);
    else if (dbName.equals(ContentName.onMon))
      acReceivableT = acReceivableReposMon.findTopByAcctCodeIsAndCustNoIsAndFacmNoIsAndOpenAcDateIsOrderByRvNoDesc(acctCode_0, custNo_1, facmNo_2, openAcDate_3);
    else if (dbName.equals(ContentName.onHist))
      acReceivableT = acReceivableReposHist.findTopByAcctCodeIsAndCustNoIsAndFacmNoIsAndOpenAcDateIsOrderByRvNoDesc(acctCode_0, custNo_1, facmNo_2, openAcDate_3);
    else 
      acReceivableT = acReceivableRepos.findTopByAcctCodeIsAndCustNoIsAndFacmNoIsAndOpenAcDateIsOrderByRvNoDesc(acctCode_0, custNo_1, facmNo_2, openAcDate_3);

    return acReceivableT.isPresent() ? acReceivableT.get() : null;
  }

  @Override
  public Slice<AcReceivable> useL2062Eq(String acctCode_0, int custNo_1, int facmNo_2, int facmNo_3, int clsFlag_4, int clsFlag_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("useL2062Eq " + dbName + " : " + "acctCode_0 : " + acctCode_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3 + " clsFlag_4 : " +  clsFlag_4 + " clsFlag_5 : " +  clsFlag_5);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByAcctCodeIsAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndClsFlagGreaterThanEqualAndClsFlagLessThanEqualOrderByOpenAcDateAsc(acctCode_0, custNo_1, facmNo_2, facmNo_3, clsFlag_4, clsFlag_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByAcctCodeIsAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndClsFlagGreaterThanEqualAndClsFlagLessThanEqualOrderByOpenAcDateAsc(acctCode_0, custNo_1, facmNo_2, facmNo_3, clsFlag_4, clsFlag_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByAcctCodeIsAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndClsFlagGreaterThanEqualAndClsFlagLessThanEqualOrderByOpenAcDateAsc(acctCode_0, custNo_1, facmNo_2, facmNo_3, clsFlag_4, clsFlag_5, pageable);
    else 
      slice = acReceivableRepos.findAllByAcctCodeIsAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndClsFlagGreaterThanEqualAndClsFlagLessThanEqualOrderByOpenAcDateAsc(acctCode_0, custNo_1, facmNo_2, facmNo_3, clsFlag_4, clsFlag_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> acrvClsFlagSubBook(int clsFlag_0, String acBookCode_1, String acSubBookCode_2, String branchNo_3, String currencyCode_4, String acNoCode_5, String acSubCode_6, String acDtlCode_7, int custNo_8, int custNo_9, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acrvClsFlagSubBook " + dbName + " : " + "clsFlag_0 : " + clsFlag_0 + " acBookCode_1 : " +  acBookCode_1 + " acSubBookCode_2 : " +  acSubBookCode_2 + " branchNo_3 : " +  branchNo_3 + " currencyCode_4 : " +  currencyCode_4 + " acNoCode_5 : " +  acNoCode_5 + " acSubCode_6 : " +  acSubCode_6 + " acDtlCode_7 : " +  acDtlCode_7 + " custNo_8 : " +  custNo_8 + " custNo_9 : " +  custNo_9);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, branchNo_3, currencyCode_4, acNoCode_5, acSubCode_6, acDtlCode_7, custNo_8, custNo_9, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, branchNo_3, currencyCode_4, acNoCode_5, acSubCode_6, acDtlCode_7, custNo_8, custNo_9, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, branchNo_3, currencyCode_4, acNoCode_5, acSubCode_6, acDtlCode_7, custNo_8, custNo_9, pageable);
    else 
      slice = acReceivableRepos.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, branchNo_3, currencyCode_4, acNoCode_5, acSubCode_6, acDtlCode_7, custNo_8, custNo_9, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> acrvFacmNoSubBook(int clsFlag_0, String acBookCode_1, String acSubBookCode_2, int custNo_3, int acctFlag_4, int facmNo_5, int facmNo_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acrvFacmNoSubBook " + dbName + " : " + "clsFlag_0 : " + clsFlag_0 + " acBookCode_1 : " +  acBookCode_1 + " acSubBookCode_2 : " +  acSubBookCode_2 + " custNo_3 : " +  custNo_3 + " acctFlag_4 : " +  acctFlag_4 + " facmNo_5 : " +  facmNo_5 + " facmNo_6 : " +  facmNo_6);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, custNo_3, acctFlag_4, facmNo_5, facmNo_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, custNo_3, acctFlag_4, facmNo_5, facmNo_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, custNo_3, acctFlag_4, facmNo_5, facmNo_6, pageable);
    else 
      slice = acReceivableRepos.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, custNo_3, acctFlag_4, facmNo_5, facmNo_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> acctCodeSubBook(int clsFlag_0, String acBookCode_1, String acSubBookCode_2, String acctCode_3, int custNo_4, int custNo_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acctCodeSubBook " + dbName + " : " + "clsFlag_0 : " + clsFlag_0 + " acBookCode_1 : " +  acBookCode_1 + " acSubBookCode_2 : " +  acSubBookCode_2 + " acctCode_3 : " +  acctCode_3 + " custNo_4 : " +  custNo_4 + " custNo_5 : " +  custNo_5);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, acctCode_3, custNo_4, custNo_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, acctCode_3, custNo_4, custNo_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, acctCode_3, custNo_4, custNo_5, pageable);
    else 
      slice = acReceivableRepos.findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(clsFlag_0, acBookCode_1, acSubBookCode_2, acctCode_3, custNo_4, custNo_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> useL2064Eq(int custNo_0, int acctFlag_1, int acctFlag_2, String rvNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("useL2064Eq " + dbName + " : " + "custNo_0 : " + custNo_0 + " acctFlag_1 : " +  acctFlag_1 + " acctFlag_2 : " +  acctFlag_2 + " rvNo_3 : " +  rvNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByCustNoIsAndAcctFlagGreaterThanEqualAndAcctFlagLessThanEqualAndRvNoLikeOrderByFacmNoAscRvNoDesc(custNo_0, acctFlag_1, acctFlag_2, rvNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByCustNoIsAndAcctFlagGreaterThanEqualAndAcctFlagLessThanEqualAndRvNoLikeOrderByFacmNoAscRvNoDesc(custNo_0, acctFlag_1, acctFlag_2, rvNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByCustNoIsAndAcctFlagGreaterThanEqualAndAcctFlagLessThanEqualAndRvNoLikeOrderByFacmNoAscRvNoDesc(custNo_0, acctFlag_1, acctFlag_2, rvNo_3, pageable);
    else 
      slice = acReceivableRepos.findAllByCustNoIsAndAcctFlagGreaterThanEqualAndAcctFlagLessThanEqualAndRvNoLikeOrderByFacmNoAscRvNoDesc(custNo_0, acctFlag_1, acctFlag_2, rvNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> useL2r58Eq(int custNo_0, int facmNo_1, int acctFlag_2, int acctFlag_3, String rvNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("useL2r58Eq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " acctFlag_2 : " +  acctFlag_2 + " acctFlag_3 : " +  acctFlag_3 + " rvNo_4 : " +  rvNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByCustNoIsAndFacmNoIsAndAcctFlagGreaterThanEqualAndAcctFlagLessThanEqualAndRvNoLikeOrderByFacmNoAscRvNoAsc(custNo_0, facmNo_1, acctFlag_2, acctFlag_3, rvNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByCustNoIsAndFacmNoIsAndAcctFlagGreaterThanEqualAndAcctFlagLessThanEqualAndRvNoLikeOrderByFacmNoAscRvNoAsc(custNo_0, facmNo_1, acctFlag_2, acctFlag_3, rvNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByCustNoIsAndFacmNoIsAndAcctFlagGreaterThanEqualAndAcctFlagLessThanEqualAndRvNoLikeOrderByFacmNoAscRvNoAsc(custNo_0, facmNo_1, acctFlag_2, acctFlag_3, rvNo_4, pageable);
    else 
      slice = acReceivableRepos.findAllByCustNoIsAndFacmNoIsAndAcctFlagGreaterThanEqualAndAcctFlagLessThanEqualAndRvNoLikeOrderByFacmNoAscRvNoAsc(custNo_0, facmNo_1, acctFlag_2, acctFlag_3, rvNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> useBs902Eq(int custNo_0, int custNo_1, int clsFlag_2, int receivableFlag_3, String rvNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("useBs902Eq " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " +  custNo_1 + " clsFlag_2 : " +  clsFlag_2 + " receivableFlag_3 : " +  receivableFlag_3 + " rvNo_4 : " +  rvNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndClsFlagIsAndReceivableFlagIsAndRvNoLikeOrderByCustNoAscFacmNoAsc(custNo_0, custNo_1, clsFlag_2, receivableFlag_3, rvNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndClsFlagIsAndReceivableFlagIsAndRvNoLikeOrderByCustNoAscFacmNoAsc(custNo_0, custNo_1, clsFlag_2, receivableFlag_3, rvNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndClsFlagIsAndReceivableFlagIsAndRvNoLikeOrderByCustNoAscFacmNoAsc(custNo_0, custNo_1, clsFlag_2, receivableFlag_3, rvNo_4, pageable);
    else 
      slice = acReceivableRepos.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndClsFlagIsAndReceivableFlagIsAndRvNoLikeOrderByCustNoAscFacmNoAsc(custNo_0, custNo_1, clsFlag_2, receivableFlag_3, rvNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> acrvClsFlag2SubBook(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, String acDtlCode_6, int custNo_7, int custNo_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acrvClsFlag2SubBook " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acNoCode_4 : " +  acNoCode_4 + " acSubCode_5 : " +  acSubCode_5 + " acDtlCode_6 : " +  acDtlCode_6 + " custNo_7 : " +  custNo_7 + " custNo_8 : " +  custNo_8);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, custNo_7, custNo_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, custNo_7, custNo_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, custNo_7, custNo_8, pageable);
    else 
      slice = acReceivableRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, custNo_7, custNo_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> acrvFacmNo2SubBook(String acBookCode_0, String acSubBookCode_1, int custNo_2, int acctFlag_3, int facmNo_4, int facmNo_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acrvFacmNo2SubBook " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " custNo_2 : " +  custNo_2 + " acctFlag_3 : " +  acctFlag_3 + " facmNo_4 : " +  facmNo_4 + " facmNo_5 : " +  facmNo_5);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, custNo_2, acctFlag_3, facmNo_4, facmNo_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, custNo_2, acctFlag_3, facmNo_4, facmNo_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, custNo_2, acctFlag_3, facmNo_4, facmNo_5, pageable);
    else 
      slice = acReceivableRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, custNo_2, acctFlag_3, facmNo_4, facmNo_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcReceivable> acctCode2SubBook(String acBookCode_0, String acSubBookCode_1, String acctCode_2, int custNo_3, int custNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcReceivable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acctCode2SubBook " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " acctCode_2 : " +  acctCode_2 + " custNo_3 : " +  custNo_3 + " custNo_4 : " +  custNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = acReceivableReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, acctCode_2, custNo_3, custNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acReceivableReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, acctCode_2, custNo_3, custNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acReceivableReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, acctCode_2, custNo_3, custNo_4, pageable);
    else 
      slice = acReceivableRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByAcctCodeAscCustNoAscFacmNoAsc(acBookCode_0, acSubBookCode_1, acctCode_2, custNo_3, custNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AcReceivable holdById(AcReceivableId acReceivableId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acReceivableId);
    Optional<AcReceivable> acReceivable = null;
    if (dbName.equals(ContentName.onDay))
      acReceivable = acReceivableReposDay.findByAcReceivableId(acReceivableId);
    else if (dbName.equals(ContentName.onMon))
      acReceivable = acReceivableReposMon.findByAcReceivableId(acReceivableId);
    else if (dbName.equals(ContentName.onHist))
      acReceivable = acReceivableReposHist.findByAcReceivableId(acReceivableId);
    else 
      acReceivable = acReceivableRepos.findByAcReceivableId(acReceivableId);
    return acReceivable.isPresent() ? acReceivable.get() : null;
  }

  @Override
  public AcReceivable holdById(AcReceivable acReceivable, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acReceivable.getAcReceivableId());
    Optional<AcReceivable> acReceivableT = null;
    if (dbName.equals(ContentName.onDay))
      acReceivableT = acReceivableReposDay.findByAcReceivableId(acReceivable.getAcReceivableId());
    else if (dbName.equals(ContentName.onMon))
      acReceivableT = acReceivableReposMon.findByAcReceivableId(acReceivable.getAcReceivableId());
    else if (dbName.equals(ContentName.onHist))
      acReceivableT = acReceivableReposHist.findByAcReceivableId(acReceivable.getAcReceivableId());
    else 
      acReceivableT = acReceivableRepos.findByAcReceivableId(acReceivable.getAcReceivableId());
    return acReceivableT.isPresent() ? acReceivableT.get() : null;
  }

  @Override
  public AcReceivable insert(AcReceivable acReceivable, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + acReceivable.getAcReceivableId());
    if (this.findById(acReceivable.getAcReceivableId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      acReceivable.setCreateEmpNo(empNot);

    if(acReceivable.getLastUpdateEmpNo() == null || acReceivable.getLastUpdateEmpNo().isEmpty())
      acReceivable.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acReceivableReposDay.saveAndFlush(acReceivable);	
    else if (dbName.equals(ContentName.onMon))
      return acReceivableReposMon.saveAndFlush(acReceivable);
    else if (dbName.equals(ContentName.onHist))
      return acReceivableReposHist.saveAndFlush(acReceivable);
    else 
    return acReceivableRepos.saveAndFlush(acReceivable);
  }

  @Override
  public AcReceivable update(AcReceivable acReceivable, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acReceivable.getAcReceivableId());
    if (!empNot.isEmpty())
      acReceivable.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acReceivableReposDay.saveAndFlush(acReceivable);	
    else if (dbName.equals(ContentName.onMon))
      return acReceivableReposMon.saveAndFlush(acReceivable);
    else if (dbName.equals(ContentName.onHist))
      return acReceivableReposHist.saveAndFlush(acReceivable);
    else 
    return acReceivableRepos.saveAndFlush(acReceivable);
  }

  @Override
  public AcReceivable update2(AcReceivable acReceivable, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acReceivable.getAcReceivableId());
    if (!empNot.isEmpty())
      acReceivable.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      acReceivableReposDay.saveAndFlush(acReceivable);	
    else if (dbName.equals(ContentName.onMon))
      acReceivableReposMon.saveAndFlush(acReceivable);
    else if (dbName.equals(ContentName.onHist))
        acReceivableReposHist.saveAndFlush(acReceivable);
    else 
      acReceivableRepos.saveAndFlush(acReceivable);	
    return this.findById(acReceivable.getAcReceivableId());
  }

  @Override
  public void delete(AcReceivable acReceivable, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + acReceivable.getAcReceivableId());
    if (dbName.equals(ContentName.onDay)) {
      acReceivableReposDay.delete(acReceivable);	
      acReceivableReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acReceivableReposMon.delete(acReceivable);	
      acReceivableReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acReceivableReposHist.delete(acReceivable);
      acReceivableReposHist.flush();
    }
    else {
      acReceivableRepos.delete(acReceivable);
      acReceivableRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AcReceivable> acReceivable, TitaVo... titaVo) throws DBException {
    if (acReceivable == null || acReceivable.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (AcReceivable t : acReceivable){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      acReceivable = acReceivableReposDay.saveAll(acReceivable);	
      acReceivableReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acReceivable = acReceivableReposMon.saveAll(acReceivable);	
      acReceivableReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acReceivable = acReceivableReposHist.saveAll(acReceivable);
      acReceivableReposHist.flush();
    }
    else {
      acReceivable = acReceivableRepos.saveAll(acReceivable);
      acReceivableRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AcReceivable> acReceivable, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (acReceivable == null || acReceivable.size() == 0)
      throw new DBException(6);

    for (AcReceivable t : acReceivable) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      acReceivable = acReceivableReposDay.saveAll(acReceivable);	
      acReceivableReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acReceivable = acReceivableReposMon.saveAll(acReceivable);	
      acReceivableReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acReceivable = acReceivableReposHist.saveAll(acReceivable);
      acReceivableReposHist.flush();
    }
    else {
      acReceivable = acReceivableRepos.saveAll(acReceivable);
      acReceivableRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AcReceivable> acReceivable, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (acReceivable == null || acReceivable.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      acReceivableReposDay.deleteAll(acReceivable);	
      acReceivableReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acReceivableReposMon.deleteAll(acReceivable);	
      acReceivableReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acReceivableReposHist.deleteAll(acReceivable);
      acReceivableReposHist.flush();
    }
    else {
      acReceivableRepos.deleteAll(acReceivable);
      acReceivableRepos.flush();
    }
  }

}
