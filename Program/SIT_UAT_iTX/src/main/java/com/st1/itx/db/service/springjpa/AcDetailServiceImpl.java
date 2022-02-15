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
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcDetailId;
import com.st1.itx.db.repository.online.AcDetailRepository;
import com.st1.itx.db.repository.day.AcDetailRepositoryDay;
import com.st1.itx.db.repository.mon.AcDetailRepositoryMon;
import com.st1.itx.db.repository.hist.AcDetailRepositoryHist;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("acDetailService")
@Repository
public class AcDetailServiceImpl extends ASpringJpaParm implements AcDetailService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AcDetailRepository acDetailRepos;

  @Autowired
  private AcDetailRepositoryDay acDetailReposDay;

  @Autowired
  private AcDetailRepositoryMon acDetailReposMon;

  @Autowired
  private AcDetailRepositoryHist acDetailReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(acDetailRepos);
    org.junit.Assert.assertNotNull(acDetailReposDay);
    org.junit.Assert.assertNotNull(acDetailReposMon);
    org.junit.Assert.assertNotNull(acDetailReposHist);
  }

  @Override
  public AcDetail findById(AcDetailId acDetailId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + acDetailId);
    Optional<AcDetail> acDetail = null;
    if (dbName.equals(ContentName.onDay))
      acDetail = acDetailReposDay.findById(acDetailId);
    else if (dbName.equals(ContentName.onMon))
      acDetail = acDetailReposMon.findById(acDetailId);
    else if (dbName.equals(ContentName.onHist))
      acDetail = acDetailReposHist.findById(acDetailId);
    else 
      acDetail = acDetailRepos.findById(acDetailId);
    AcDetail obj = acDetail.isPresent() ? acDetail.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AcDetail> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "RelDy", "RelTxseq", "AcSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "RelDy", "RelTxseq", "AcSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAll(pageable);
    else 
      slice = acDetailRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlRelTxseqEq(int relDy_0, String relTxseq_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlRelTxseqEq " + dbName + " : " + "relDy_0 : " + relDy_0 + " relTxseq_1 : " +  relTxseq_1);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByRelDyIsAndRelTxseqIsOrderByAcSeqAsc(relDy_0, relTxseq_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByRelDyIsAndRelTxseqIsOrderByAcSeqAsc(relDy_0, relTxseq_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByRelDyIsAndRelTxseqIsOrderByAcSeqAsc(relDy_0, relTxseq_1, pageable);
    else 
      slice = acDetailRepos.findAllByRelDyIsAndRelTxseqIsOrderByAcSeqAsc(relDy_0, relTxseq_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlAcDateRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, String acDtlCode_6, int acDate_7, int acDate_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlAcDateRange " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acNoCode_4 : " +  acNoCode_4 + " acSubCode_5 : " +  acSubCode_5 + " acDtlCode_6 : " +  acDtlCode_6 + " acDate_7 : " +  acDate_7 + " acDate_8 : " +  acDate_8);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, acDate_7, acDate_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, acDate_7, acDate_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, acDate_7, acDate_8, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, acDate_7, acDate_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlSumNoRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String sumNo_5, String sumNo_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlSumNoRange " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " acNoCode_3 : " +  acNoCode_3 + " acNoCode_4 : " +  acNoCode_4 + " sumNo_5 : " +  sumNo_5 + " sumNo_6 : " +  sumNo_6);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, sumNo_5, sumNo_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, sumNo_5, sumNo_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, sumNo_5, sumNo_6, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, sumNo_5, sumNo_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlTitaTlrNoRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String titaTlrNo_5, String titaTlrNo_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlTitaTlrNoRange " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " acNoCode_3 : " +  acNoCode_3 + " acNoCode_4 : " +  acNoCode_4 + " titaTlrNo_5 : " +  titaTlrNo_5 + " titaTlrNo_6 : " +  titaTlrNo_6);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaTlrNo_5, titaTlrNo_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaTlrNo_5, titaTlrNo_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaTlrNo_5, titaTlrNo_6, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaTlrNo_5, titaTlrNo_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlTitaBatchNoRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String titaBatchNo_5, String titaBatchNo_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlTitaBatchNoRange " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " acNoCode_3 : " +  acNoCode_3 + " acNoCode_4 : " +  acNoCode_4 + " titaBatchNo_5 : " +  titaBatchNo_5 + " titaBatchNo_6 : " +  titaBatchNo_6);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaBatchNo_5, titaBatchNo_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaBatchNo_5, titaBatchNo_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaBatchNo_5, titaBatchNo_6, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaBatchNo_5, titaBatchNo_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlDscptCodeRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String dscptCode_5, String dscptCode_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlDscptCodeRange " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " acNoCode_3 : " +  acNoCode_3 + " acNoCode_4 : " +  acNoCode_4 + " dscptCode_5 : " +  dscptCode_5 + " dscptCode_6 : " +  dscptCode_6);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, dscptCode_5, dscptCode_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, dscptCode_5, dscptCode_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, dscptCode_5, dscptCode_6, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, dscptCode_5, dscptCode_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlSlipBatNoRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, int slipBatNo_5, int slipBatNo_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlSlipBatNoRange " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " acNoCode_3 : " +  acNoCode_3 + " acNoCode_4 : " +  acNoCode_4 + " slipBatNo_5 : " +  slipBatNo_5 + " slipBatNo_6 : " +  slipBatNo_6);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, slipBatNo_5, slipBatNo_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, slipBatNo_5, slipBatNo_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, slipBatNo_5, slipBatNo_6, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, slipBatNo_5, slipBatNo_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlTitaSecNoRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String titaSecNo_5, String titaSecNo_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlTitaSecNoRange " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " acNoCode_3 : " +  acNoCode_3 + " acNoCode_4 : " +  acNoCode_4 + " titaSecNo_5 : " +  titaSecNo_5 + " titaSecNo_6 : " +  titaSecNo_6);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaSecNo_5, titaSecNo_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaSecNo_5, titaSecNo_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaSecNo_5, titaSecNo_6, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, titaSecNo_5, titaSecNo_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlAcNoCodeRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlAcNoCodeRange " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " acNoCode_3 : " +  acNoCode_3 + " acNoCode_4 : " +  acNoCode_4);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(branchNo_0, currencyCode_1, acDate_2, acNoCode_3, acNoCode_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlCustNo(String branchNo_0, String currencyCode_1, int acDate_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlCustNo " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " custNo_3 : " +  custNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndCustNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndCustNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndCustNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, custNo_3, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndCustNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, custNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlTitaTlrNo(String branchNo_0, String currencyCode_1, int acDate_2, String titaTlrNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlTitaTlrNo " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " titaTlrNo_3 : " +  titaTlrNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaTlrNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaTlrNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaTlrNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaTlrNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaTlrNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaTlrNo_3, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaTlrNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaTlrNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlTitaBatchNo(String branchNo_0, String currencyCode_1, int acDate_2, String titaBatchNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlTitaBatchNo " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " titaBatchNo_3 : " +  titaBatchNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaBatchNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaBatchNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaBatchNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaBatchNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaBatchNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaBatchNo_3, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaBatchNoIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaBatchNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlTitaTxCd(String branchNo_0, String currencyCode_1, int acDate_2, String titaTxCd_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlTitaTxCd " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " titaTxCd_3 : " +  titaTxCd_3);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaTxCdIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaTxCd_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaTxCdIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaTxCd_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaTxCdIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaTxCd_3, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaTxCdIsOrderByRelTxseqAscAcSeqAsc(branchNo_0, currencyCode_1, acDate_2, titaTxCd_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> findL9RptData(int acDate_0, int slipBatNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL9RptData " + dbName + " : " + "acDate_0 : " + acDate_0 + " slipBatNo_1 : " +  slipBatNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcDateIsAndSlipBatNoIsOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acDate_0, slipBatNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcDateIsAndSlipBatNoIsOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acDate_0, slipBatNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcDateIsAndSlipBatNoIsOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acDate_0, slipBatNo_1, pageable);
    else 
      slice = acDetailRepos.findAllByAcDateIsAndSlipBatNoIsOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acDate_0, slipBatNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> findL2613(String acctCode_0, int custNo_1, String rvNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL2613 " + dbName + " : " + "acctCode_0 : " + acctCode_0 + " custNo_1 : " +  custNo_1 + " rvNo_2 : " +  rvNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcctCodeIsAndCustNoIsAndRvNoIsOrderByRelDyAscRelTxseqAscAcSeqAsc(acctCode_0, custNo_1, rvNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcctCodeIsAndCustNoIsAndRvNoIsOrderByRelDyAscRelTxseqAscAcSeqAsc(acctCode_0, custNo_1, rvNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcctCodeIsAndCustNoIsAndRvNoIsOrderByRelDyAscRelTxseqAscAcSeqAsc(acctCode_0, custNo_1, rvNo_2, pageable);
    else 
      slice = acDetailRepos.findAllByAcctCodeIsAndCustNoIsAndRvNoIsOrderByRelDyAscRelTxseqAscAcSeqAsc(acctCode_0, custNo_1, rvNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> findTxtNoEq(int acDate_0, String titaKinbr_1, String titaTlrNo_2, int titaTxtNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findTxtNoEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " titaKinbr_1 : " +  titaKinbr_1 + " titaTlrNo_2 : " +  titaTlrNo_2 + " titaTxtNo_3 : " +  titaTxtNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcDateIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByAcSeqAsc(acDate_0, titaKinbr_1, titaTlrNo_2, titaTxtNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcDateIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByAcSeqAsc(acDate_0, titaKinbr_1, titaTlrNo_2, titaTxtNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcDateIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByAcSeqAsc(acDate_0, titaKinbr_1, titaTlrNo_2, titaTxtNo_3, pageable);
    else 
      slice = acDetailRepos.findAllByAcDateIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByAcSeqAsc(acDate_0, titaKinbr_1, titaTlrNo_2, titaTxtNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> findL4101(String branchNo_0, String currencyCode_1, int acDate_2, String titaBatchNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4101 " + dbName + " : " + "branchNo_0 : " + branchNo_0 + " currencyCode_1 : " +  currencyCode_1 + " acDate_2 : " +  acDate_2 + " titaBatchNo_3 : " +  titaBatchNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaBatchNoIsOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(branchNo_0, currencyCode_1, acDate_2, titaBatchNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaBatchNoIsOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(branchNo_0, currencyCode_1, acDate_2, titaBatchNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaBatchNoIsOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(branchNo_0, currencyCode_1, acDate_2, titaBatchNo_3, pageable);
    else 
      slice = acDetailRepos.findAllByBranchNoIsAndCurrencyCodeIsAndAcDateIsAndTitaBatchNoIsOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(branchNo_0, currencyCode_1, acDate_2, titaBatchNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> findL6908(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, String acDtlCode_6, int custNo_7, int facmNo_8, int acDate_9, int acDate_10, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL6908 " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acNoCode_4 : " +  acNoCode_4 + " acSubCode_5 : " +  acSubCode_5 + " acDtlCode_6 : " +  acDtlCode_6 + " custNo_7 : " +  custNo_7 + " facmNo_8 : " +  facmNo_8 + " acDate_9 : " +  acDate_9 + " acDate_10 : " +  acDate_10);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoIsAndFacmNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, custNo_7, facmNo_8, acDate_9, acDate_10, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoIsAndFacmNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, custNo_7, facmNo_8, acDate_9, acDate_10, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoIsAndFacmNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, custNo_7, facmNo_8, acDate_9, acDate_10, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoIsAndFacmNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, custNo_7, facmNo_8, acDate_9, acDate_10, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> bormNoAcDateRange(int custNo_0, int facmNo_1, int bormNo_2, int acctFlag_3, int acDate_4, int acDate_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("bormNoAcDateRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " acctFlag_3 : " +  acctFlag_3 + " acDate_4 : " +  acDate_4 + " acDate_5 : " +  acDate_5);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndAcctFlagIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateDescSlipNoDesc(custNo_0, facmNo_1, bormNo_2, acctFlag_3, acDate_4, acDate_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndAcctFlagIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateDescSlipNoDesc(custNo_0, facmNo_1, bormNo_2, acctFlag_3, acDate_4, acDate_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndAcctFlagIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateDescSlipNoDesc(custNo_0, facmNo_1, bormNo_2, acctFlag_3, acDate_4, acDate_5, pageable);
    else 
      slice = acDetailRepos.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndAcctFlagIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateDescSlipNoDesc(custNo_0, facmNo_1, bormNo_2, acctFlag_3, acDate_4, acDate_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookAcNoCodeRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookAcNoCodeRange " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookSumNoRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String sumNo_7, String sumNo_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookSumNoRange " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " sumNo_7 : " +  sumNo_7 + " sumNo_8 : " +  sumNo_8);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, sumNo_7, sumNo_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, sumNo_7, sumNo_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, sumNo_7, sumNo_8, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, sumNo_7, sumNo_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookTitaTlrNoRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaTlrNo_7, String titaTlrNo_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookTitaTlrNoRange " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " titaTlrNo_7 : " +  titaTlrNo_7 + " titaTlrNo_8 : " +  titaTlrNo_8);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaTlrNo_7, titaTlrNo_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaTlrNo_7, titaTlrNo_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaTlrNo_7, titaTlrNo_8, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaTlrNo_7, titaTlrNo_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookTitaBatchNoRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaBatchNo_7, String titaBatchNo_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookTitaBatchNoRange " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " titaBatchNo_7 : " +  titaBatchNo_7 + " titaBatchNo_8 : " +  titaBatchNo_8);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaBatchNo_7, titaBatchNo_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaBatchNo_7, titaBatchNo_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaBatchNo_7, titaBatchNo_8, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaBatchNo_7, titaBatchNo_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookDscptCodeRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String dscptCode_7, String dscptCode_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookDscptCodeRange " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " dscptCode_7 : " +  dscptCode_7 + " dscptCode_8 : " +  dscptCode_8);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, dscptCode_7, dscptCode_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, dscptCode_7, dscptCode_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, dscptCode_7, dscptCode_8, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, dscptCode_7, dscptCode_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookSlipBatNoRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, int slipBatNo_7, int slipBatNo_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookSlipBatNoRange " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " slipBatNo_7 : " +  slipBatNo_7 + " slipBatNo_8 : " +  slipBatNo_8);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, slipBatNo_7, slipBatNo_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, slipBatNo_7, slipBatNo_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, slipBatNo_7, slipBatNo_8, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, slipBatNo_7, slipBatNo_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookTitaSecNoRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaSecNo_7, String titaSecNo_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookTitaSecNoRange " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " titaSecNo_7 : " +  titaSecNo_7 + " titaSecNo_8 : " +  titaSecNo_8);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaSecNo_7, titaSecNo_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaSecNo_7, titaSecNo_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaSecNo_7, titaSecNo_8, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaSecNo_7, titaSecNo_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookAcNoCodeRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String rvNo_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookAcNoCodeRange1 " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " rvNo_7 : " +  rvNo_7);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, rvNo_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, rvNo_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, rvNo_7, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, rvNo_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookSumNoRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String sumNo_7, String sumNo_8, String rvNo_9, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookSumNoRange1 " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " sumNo_7 : " +  sumNo_7 + " sumNo_8 : " +  sumNo_8 + " rvNo_9 : " +  rvNo_9);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, sumNo_7, sumNo_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, sumNo_7, sumNo_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, sumNo_7, sumNo_8, rvNo_9, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSumNoGreaterThanEqualAndSumNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSumNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, sumNo_7, sumNo_8, rvNo_9, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookTitaTlrNoRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaTlrNo_7, String titaTlrNo_8, String rvNo_9, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookTitaTlrNoRange1 " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " titaTlrNo_7 : " +  titaTlrNo_7 + " titaTlrNo_8 : " +  titaTlrNo_8 + " rvNo_9 : " +  rvNo_9);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaTlrNo_7, titaTlrNo_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaTlrNo_7, titaTlrNo_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaTlrNo_7, titaTlrNo_8, rvNo_9, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaTlrNoGreaterThanEqualAndTitaTlrNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaTlrNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaTlrNo_7, titaTlrNo_8, rvNo_9, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookTitaBatchNoRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaBatchNo_7, String titaBatchNo_8, String rvNo_9, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookTitaBatchNoRange1 " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " titaBatchNo_7 : " +  titaBatchNo_7 + " titaBatchNo_8 : " +  titaBatchNo_8 + " rvNo_9 : " +  rvNo_9);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaBatchNo_7, titaBatchNo_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaBatchNo_7, titaBatchNo_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaBatchNo_7, titaBatchNo_8, rvNo_9, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaBatchNoGreaterThanEqualAndTitaBatchNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaBatchNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaBatchNo_7, titaBatchNo_8, rvNo_9, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookDscptCodeRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String dscptCode_7, String dscptCode_8, String rvNo_9, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookDscptCodeRange1 " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " dscptCode_7 : " +  dscptCode_7 + " dscptCode_8 : " +  dscptCode_8 + " rvNo_9 : " +  rvNo_9);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, dscptCode_7, dscptCode_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, dscptCode_7, dscptCode_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, dscptCode_7, dscptCode_8, rvNo_9, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndDscptCodeGreaterThanEqualAndDscptCodeLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscDscptCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, dscptCode_7, dscptCode_8, rvNo_9, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookSlipBatNoRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, int slipBatNo_7, int slipBatNo_8, String rvNo_9, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookSlipBatNoRange1 " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " slipBatNo_7 : " +  slipBatNo_7 + " slipBatNo_8 : " +  slipBatNo_8 + " rvNo_9 : " +  rvNo_9);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, slipBatNo_7, slipBatNo_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, slipBatNo_7, slipBatNo_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, slipBatNo_7, slipBatNo_8, rvNo_9, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndSlipBatNoGreaterThanEqualAndSlipBatNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscSlipBatNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, slipBatNo_7, slipBatNo_8, rvNo_9, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> SubBookTitaSecNoRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaSecNo_7, String titaSecNo_8, String rvNo_9, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubBookTitaSecNoRange1 " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acDate_4 : " +  acDate_4 + " acNoCode_5 : " +  acNoCode_5 + " acNoCode_6 : " +  acNoCode_6 + " titaSecNo_7 : " +  titaSecNo_7 + " titaSecNo_8 : " +  titaSecNo_8 + " rvNo_9 : " +  rvNo_9);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaSecNo_7, titaSecNo_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaSecNo_7, titaSecNo_8, rvNo_9, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaSecNo_7, titaSecNo_8, rvNo_9, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndTitaSecNoGreaterThanEqualAndTitaSecNoLessThanEqualAndRvNoLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAscTitaSecNoAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acDate_4, acNoCode_5, acNoCode_6, titaSecNo_7, titaSecNo_8, rvNo_9, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcDetail> acdtlAcDateRange2(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, int acDate_6, int acDate_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acdtlAcDateRange2 " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acNoCode_4 : " +  acNoCode_4 + " acSubCode_5 : " +  acSubCode_5 + " acDate_6 : " +  acDate_6 + " acDate_7 : " +  acDate_7);
    if (dbName.equals(ContentName.onDay))
      slice = acDetailReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDate_6, acDate_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acDetailReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDate_6, acDate_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acDetailReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDate_6, acDate_7, pageable);
    else 
      slice = acDetailRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDate_6, acDate_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AcDetail holdById(AcDetailId acDetailId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acDetailId);
    Optional<AcDetail> acDetail = null;
    if (dbName.equals(ContentName.onDay))
      acDetail = acDetailReposDay.findByAcDetailId(acDetailId);
    else if (dbName.equals(ContentName.onMon))
      acDetail = acDetailReposMon.findByAcDetailId(acDetailId);
    else if (dbName.equals(ContentName.onHist))
      acDetail = acDetailReposHist.findByAcDetailId(acDetailId);
    else 
      acDetail = acDetailRepos.findByAcDetailId(acDetailId);
    return acDetail.isPresent() ? acDetail.get() : null;
  }

  @Override
  public AcDetail holdById(AcDetail acDetail, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + acDetail.getAcDetailId());
    Optional<AcDetail> acDetailT = null;
    if (dbName.equals(ContentName.onDay))
      acDetailT = acDetailReposDay.findByAcDetailId(acDetail.getAcDetailId());
    else if (dbName.equals(ContentName.onMon))
      acDetailT = acDetailReposMon.findByAcDetailId(acDetail.getAcDetailId());
    else if (dbName.equals(ContentName.onHist))
      acDetailT = acDetailReposHist.findByAcDetailId(acDetail.getAcDetailId());
    else 
      acDetailT = acDetailRepos.findByAcDetailId(acDetail.getAcDetailId());
    return acDetailT.isPresent() ? acDetailT.get() : null;
  }

  @Override
  public AcDetail insert(AcDetail acDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + acDetail.getAcDetailId());
    if (this.findById(acDetail.getAcDetailId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      acDetail.setCreateEmpNo(empNot);

    if(acDetail.getLastUpdateEmpNo() == null || acDetail.getLastUpdateEmpNo().isEmpty())
      acDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acDetailReposDay.saveAndFlush(acDetail);	
    else if (dbName.equals(ContentName.onMon))
      return acDetailReposMon.saveAndFlush(acDetail);
    else if (dbName.equals(ContentName.onHist))
      return acDetailReposHist.saveAndFlush(acDetail);
    else 
    return acDetailRepos.saveAndFlush(acDetail);
  }

  @Override
  public AcDetail update(AcDetail acDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acDetail.getAcDetailId());
    if (!empNot.isEmpty())
      acDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acDetailReposDay.saveAndFlush(acDetail);	
    else if (dbName.equals(ContentName.onMon))
      return acDetailReposMon.saveAndFlush(acDetail);
    else if (dbName.equals(ContentName.onHist))
      return acDetailReposHist.saveAndFlush(acDetail);
    else 
    return acDetailRepos.saveAndFlush(acDetail);
  }

  @Override
  public AcDetail update2(AcDetail acDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + acDetail.getAcDetailId());
    if (!empNot.isEmpty())
      acDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      acDetailReposDay.saveAndFlush(acDetail);	
    else if (dbName.equals(ContentName.onMon))
      acDetailReposMon.saveAndFlush(acDetail);
    else if (dbName.equals(ContentName.onHist))
        acDetailReposHist.saveAndFlush(acDetail);
    else 
      acDetailRepos.saveAndFlush(acDetail);	
    return this.findById(acDetail.getAcDetailId());
  }

  @Override
  public void delete(AcDetail acDetail, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + acDetail.getAcDetailId());
    if (dbName.equals(ContentName.onDay)) {
      acDetailReposDay.delete(acDetail);	
      acDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acDetailReposMon.delete(acDetail);	
      acDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acDetailReposHist.delete(acDetail);
      acDetailReposHist.flush();
    }
    else {
      acDetailRepos.delete(acDetail);
      acDetailRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AcDetail> acDetail, TitaVo... titaVo) throws DBException {
    if (acDetail == null || acDetail.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (AcDetail t : acDetail){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      acDetail = acDetailReposDay.saveAll(acDetail);	
      acDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acDetail = acDetailReposMon.saveAll(acDetail);	
      acDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acDetail = acDetailReposHist.saveAll(acDetail);
      acDetailReposHist.flush();
    }
    else {
      acDetail = acDetailRepos.saveAll(acDetail);
      acDetailRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AcDetail> acDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (acDetail == null || acDetail.size() == 0)
      throw new DBException(6);

    for (AcDetail t : acDetail) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      acDetail = acDetailReposDay.saveAll(acDetail);	
      acDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acDetail = acDetailReposMon.saveAll(acDetail);	
      acDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acDetail = acDetailReposHist.saveAll(acDetail);
      acDetailReposHist.flush();
    }
    else {
      acDetail = acDetailRepos.saveAll(acDetail);
      acDetailRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AcDetail> acDetail, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (acDetail == null || acDetail.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      acDetailReposDay.deleteAll(acDetail);	
      acDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acDetailReposMon.deleteAll(acDetail);	
      acDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acDetailReposHist.deleteAll(acDetail);
      acDetailReposHist.flush();
    }
    else {
      acDetailRepos.deleteAll(acDetail);
      acDetailRepos.flush();
    }
  }

}
