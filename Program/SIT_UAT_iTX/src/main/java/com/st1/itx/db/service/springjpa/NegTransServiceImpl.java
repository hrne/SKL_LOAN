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
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.db.repository.online.NegTransRepository;
import com.st1.itx.db.repository.day.NegTransRepositoryDay;
import com.st1.itx.db.repository.mon.NegTransRepositoryMon;
import com.st1.itx.db.repository.hist.NegTransRepositoryHist;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("negTransService")
@Repository
public class NegTransServiceImpl extends ASpringJpaParm implements NegTransService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private NegTransRepository negTransRepos;

  @Autowired
  private NegTransRepositoryDay negTransReposDay;

  @Autowired
  private NegTransRepositoryMon negTransReposMon;

  @Autowired
  private NegTransRepositoryHist negTransReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(negTransRepos);
    org.junit.Assert.assertNotNull(negTransReposDay);
    org.junit.Assert.assertNotNull(negTransReposMon);
    org.junit.Assert.assertNotNull(negTransReposHist);
  }

  @Override
  public NegTrans findById(NegTransId negTransId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + negTransId);
    Optional<NegTrans> negTrans = null;
    if (dbName.equals(ContentName.onDay))
      negTrans = negTransReposDay.findById(negTransId);
    else if (dbName.equals(ContentName.onMon))
      negTrans = negTransReposMon.findById(negTransId);
    else if (dbName.equals(ContentName.onHist))
      negTrans = negTransReposHist.findById(negTransId);
    else 
      negTrans = negTransRepos.findById(negTransId);
    NegTrans obj = negTrans.isPresent() ? negTrans.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<NegTrans> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "TitaTlrNo", "TitaTxtNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "TitaTlrNo", "TitaTxtNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAll(pageable);
    else 
      slice = negTransRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByCustNoIsOrderByCaseSeqDescAcDateDesc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByCustNoIsOrderByCaseSeqDescAcDateDesc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByCustNoIsOrderByCaseSeqDescAcDateDesc(custNo_0, pageable);
    else 
      slice = negTransRepos.findAllByCustNoIsOrderByCaseSeqDescAcDateDesc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> acDateBetween(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("acDateBetween " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(acDate_0, acDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(acDate_0, acDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(acDate_0, acDate_1, pageable);
    else 
      slice = negTransRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(acDate_0, acDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> custAndAcDate(int custNo_0, int acDate_1, int acDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custAndAcDate " + dbName + " : " + "custNo_0 : " + custNo_0 + " acDate_1 : " +  acDate_1 + " acDate_2 : " +  acDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByCustNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(custNo_0, acDate_1, acDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByCustNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(custNo_0, acDate_1, acDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByCustNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(custNo_0, acDate_1, acDate_2, pageable);
    else 
      slice = negTransRepos.findAllByCustNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(custNo_0, acDate_1, acDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> entryDateBetween(int entryDate_0, int entryDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("entryDateBetween " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " +  entryDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(entryDate_0, entryDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(entryDate_0, entryDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(entryDate_0, entryDate_1, pageable);
    else 
      slice = negTransRepos.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(entryDate_0, entryDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> custAndEntryDate(int custNo_0, int entryDate_1, int entryDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custAndEntryDate " + dbName + " : " + "custNo_0 : " + custNo_0 + " entryDate_1 : " +  entryDate_1 + " entryDate_2 : " +  entryDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByCustNoIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(custNo_0, entryDate_1, entryDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByCustNoIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(custNo_0, entryDate_1, entryDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByCustNoIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(custNo_0, entryDate_1, entryDate_2, pageable);
    else 
      slice = negTransRepos.findAllByCustNoIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(custNo_0, entryDate_1, entryDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> txStatusBetween(int txStatus_0, int txStatus_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("txStatusBetween " + dbName + " : " + "txStatus_0 : " + txStatus_0 + " txStatus_1 : " +  txStatus_1);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByTxStatusGreaterThanEqualAndTxStatusLessThanEqual(txStatus_0, txStatus_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByTxStatusGreaterThanEqualAndTxStatusLessThanEqual(txStatus_0, txStatus_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByTxStatusGreaterThanEqualAndTxStatusLessThanEqual(txStatus_0, txStatus_1, pageable);
    else 
      slice = negTransRepos.findAllByTxStatusGreaterThanEqualAndTxStatusLessThanEqual(txStatus_0, txStatus_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> txStatusDateEq(int txStatus_0, int repayDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("txStatusDateEq " + dbName + " : " + "txStatus_0 : " + txStatus_0 + " repayDate_1 : " +  repayDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByTxStatusIsAndRepayDateIs(txStatus_0, repayDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByTxStatusIsAndRepayDateIs(txStatus_0, repayDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByTxStatusIsAndRepayDateIs(txStatus_0, repayDate_1, pageable);
    else 
      slice = negTransRepos.findAllByTxStatusIsAndRepayDateIs(txStatus_0, repayDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> repayDateBetween(int repayDate_0, int repayDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("repayDateBetween " + dbName + " : " + "repayDate_0 : " + repayDate_0 + " repayDate_1 : " +  repayDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByRepayDateGreaterThanEqualAndRepayDateLessThanEqual(repayDate_0, repayDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByRepayDateGreaterThanEqualAndRepayDateLessThanEqual(repayDate_0, repayDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByRepayDateGreaterThanEqualAndRepayDateLessThanEqual(repayDate_0, repayDate_1, pageable);
    else 
      slice = negTransRepos.findAllByRepayDateGreaterThanEqualAndRepayDateLessThanEqual(repayDate_0, repayDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> repayDateEq(int repayDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("repayDateEq " + dbName + " : " + "repayDate_0 : " + repayDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByRepayDateIs(repayDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByRepayDateIs(repayDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByRepayDateIs(repayDate_0, pageable);
    else 
      slice = negTransRepos.findAllByRepayDateIs(repayDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> exportDateBetween(int exportDate_0, int exportDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("exportDateBetween " + dbName + " : " + "exportDate_0 : " + exportDate_0 + " exportDate_1 : " +  exportDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByExportDateGreaterThanEqualAndExportDateLessThanEqual(exportDate_0, exportDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByExportDateGreaterThanEqualAndExportDateLessThanEqual(exportDate_0, exportDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByExportDateGreaterThanEqualAndExportDateLessThanEqual(exportDate_0, exportDate_1, pageable);
    else 
      slice = negTransRepos.findAllByExportDateGreaterThanEqualAndExportDateLessThanEqual(exportDate_0, exportDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> exportDateEq(int exportDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("exportDateEq " + dbName + " : " + "exportDate_0 : " + exportDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByExportDateIs(exportDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByExportDateIs(exportDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByExportDateIs(exportDate_0, pageable);
    else 
      slice = negTransRepos.findAllByExportDateIs(exportDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> exportAcDateEq(int exportAcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("exportAcDateEq " + dbName + " : " + "exportAcDate_0 : " + exportAcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByExportAcDateIsOrderByCaseSeqDescAcDateDesc(exportAcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByExportAcDateIsOrderByCaseSeqDescAcDateDesc(exportAcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByExportAcDateIsOrderByCaseSeqDescAcDateDesc(exportAcDate_0, pageable);
    else 
      slice = negTransRepos.findAllByExportAcDateIsOrderByCaseSeqDescAcDateDesc(exportAcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegTrans> backFunc(int thisEntdy_0, String thisKinbr_1, String thisTlrNo_2, String thisTxtNo_3, String thisSeqNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegTrans> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("backFunc " + dbName + " : " + "thisEntdy_0 : " + thisEntdy_0 + " thisKinbr_1 : " +  thisKinbr_1 + " thisTlrNo_2 : " +  thisTlrNo_2 + " thisTxtNo_3 : " +  thisTxtNo_3 + " thisSeqNo_4 : " +  thisSeqNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = negTransReposDay.findAllByThisEntdyIsAndThisKinbrIsAndThisTlrNoIsAndThisTxtNoIsAndThisSeqNoIsOrderByThisEntdyAscThisSeqNoAsc(thisEntdy_0, thisKinbr_1, thisTlrNo_2, thisTxtNo_3, thisSeqNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negTransReposMon.findAllByThisEntdyIsAndThisKinbrIsAndThisTlrNoIsAndThisTxtNoIsAndThisSeqNoIsOrderByThisEntdyAscThisSeqNoAsc(thisEntdy_0, thisKinbr_1, thisTlrNo_2, thisTxtNo_3, thisSeqNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negTransReposHist.findAllByThisEntdyIsAndThisKinbrIsAndThisTlrNoIsAndThisTxtNoIsAndThisSeqNoIsOrderByThisEntdyAscThisSeqNoAsc(thisEntdy_0, thisKinbr_1, thisTlrNo_2, thisTxtNo_3, thisSeqNo_4, pageable);
    else 
      slice = negTransRepos.findAllByThisEntdyIsAndThisKinbrIsAndThisTlrNoIsAndThisTxtNoIsAndThisSeqNoIsOrderByThisEntdyAscThisSeqNoAsc(thisEntdy_0, thisKinbr_1, thisTlrNo_2, thisTxtNo_3, thisSeqNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegTrans holdById(NegTransId negTransId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + negTransId);
    Optional<NegTrans> negTrans = null;
    if (dbName.equals(ContentName.onDay))
      negTrans = negTransReposDay.findByNegTransId(negTransId);
    else if (dbName.equals(ContentName.onMon))
      negTrans = negTransReposMon.findByNegTransId(negTransId);
    else if (dbName.equals(ContentName.onHist))
      negTrans = negTransReposHist.findByNegTransId(negTransId);
    else 
      negTrans = negTransRepos.findByNegTransId(negTransId);
    return negTrans.isPresent() ? negTrans.get() : null;
  }

  @Override
  public NegTrans holdById(NegTrans negTrans, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + negTrans.getNegTransId());
    Optional<NegTrans> negTransT = null;
    if (dbName.equals(ContentName.onDay))
      negTransT = negTransReposDay.findByNegTransId(negTrans.getNegTransId());
    else if (dbName.equals(ContentName.onMon))
      negTransT = negTransReposMon.findByNegTransId(negTrans.getNegTransId());
    else if (dbName.equals(ContentName.onHist))
      negTransT = negTransReposHist.findByNegTransId(negTrans.getNegTransId());
    else 
      negTransT = negTransRepos.findByNegTransId(negTrans.getNegTransId());
    return negTransT.isPresent() ? negTransT.get() : null;
  }

  @Override
  public NegTrans insert(NegTrans negTrans, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + negTrans.getNegTransId());
    if (this.findById(negTrans.getNegTransId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      negTrans.setCreateEmpNo(empNot);

    if(negTrans.getLastUpdateEmpNo() == null || negTrans.getLastUpdateEmpNo().isEmpty())
      negTrans.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negTransReposDay.saveAndFlush(negTrans);	
    else if (dbName.equals(ContentName.onMon))
      return negTransReposMon.saveAndFlush(negTrans);
    else if (dbName.equals(ContentName.onHist))
      return negTransReposHist.saveAndFlush(negTrans);
    else 
    return negTransRepos.saveAndFlush(negTrans);
  }

  @Override
  public NegTrans update(NegTrans negTrans, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + negTrans.getNegTransId());
    if (!empNot.isEmpty())
      negTrans.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negTransReposDay.saveAndFlush(negTrans);	
    else if (dbName.equals(ContentName.onMon))
      return negTransReposMon.saveAndFlush(negTrans);
    else if (dbName.equals(ContentName.onHist))
      return negTransReposHist.saveAndFlush(negTrans);
    else 
    return negTransRepos.saveAndFlush(negTrans);
  }

  @Override
  public NegTrans update2(NegTrans negTrans, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + negTrans.getNegTransId());
    if (!empNot.isEmpty())
      negTrans.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      negTransReposDay.saveAndFlush(negTrans);	
    else if (dbName.equals(ContentName.onMon))
      negTransReposMon.saveAndFlush(negTrans);
    else if (dbName.equals(ContentName.onHist))
        negTransReposHist.saveAndFlush(negTrans);
    else 
      negTransRepos.saveAndFlush(negTrans);	
    return this.findById(negTrans.getNegTransId());
  }

  @Override
  public void delete(NegTrans negTrans, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + negTrans.getNegTransId());
    if (dbName.equals(ContentName.onDay)) {
      negTransReposDay.delete(negTrans);	
      negTransReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negTransReposMon.delete(negTrans);	
      negTransReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negTransReposHist.delete(negTrans);
      negTransReposHist.flush();
    }
    else {
      negTransRepos.delete(negTrans);
      negTransRepos.flush();
    }
   }

  @Override
  public void insertAll(List<NegTrans> negTrans, TitaVo... titaVo) throws DBException {
    if (negTrans == null || negTrans.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (NegTrans t : negTrans){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      negTrans = negTransReposDay.saveAll(negTrans);	
      negTransReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negTrans = negTransReposMon.saveAll(negTrans);	
      negTransReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negTrans = negTransReposHist.saveAll(negTrans);
      negTransReposHist.flush();
    }
    else {
      negTrans = negTransRepos.saveAll(negTrans);
      negTransRepos.flush();
    }
    }

  @Override
  public void updateAll(List<NegTrans> negTrans, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (negTrans == null || negTrans.size() == 0)
      throw new DBException(6);

    for (NegTrans t : negTrans) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      negTrans = negTransReposDay.saveAll(negTrans);	
      negTransReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negTrans = negTransReposMon.saveAll(negTrans);	
      negTransReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negTrans = negTransReposHist.saveAll(negTrans);
      negTransReposHist.flush();
    }
    else {
      negTrans = negTransRepos.saveAll(negTrans);
      negTransRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<NegTrans> negTrans, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (negTrans == null || negTrans.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      negTransReposDay.deleteAll(negTrans);	
      negTransReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negTransReposMon.deleteAll(negTrans);	
      negTransReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negTransReposHist.deleteAll(negTrans);
      negTransReposHist.flush();
    }
    else {
      negTransRepos.deleteAll(negTrans);
      negTransRepos.flush();
    }
  }

}
