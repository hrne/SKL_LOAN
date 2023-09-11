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
import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegAppr01Id;
import com.st1.itx.db.repository.online.NegAppr01Repository;
import com.st1.itx.db.repository.day.NegAppr01RepositoryDay;
import com.st1.itx.db.repository.mon.NegAppr01RepositoryMon;
import com.st1.itx.db.repository.hist.NegAppr01RepositoryHist;
import com.st1.itx.db.service.NegAppr01Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("negAppr01Service")
@Repository
public class NegAppr01ServiceImpl extends ASpringJpaParm implements NegAppr01Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private NegAppr01Repository negAppr01Repos;

  @Autowired
  private NegAppr01RepositoryDay negAppr01ReposDay;

  @Autowired
  private NegAppr01RepositoryMon negAppr01ReposMon;

  @Autowired
  private NegAppr01RepositoryHist negAppr01ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(negAppr01Repos);
    org.junit.Assert.assertNotNull(negAppr01ReposDay);
    org.junit.Assert.assertNotNull(negAppr01ReposMon);
    org.junit.Assert.assertNotNull(negAppr01ReposHist);
  }

  @Override
  public NegAppr01 findById(NegAppr01Id negAppr01Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + negAppr01Id);
    Optional<NegAppr01> negAppr01 = null;
    if (dbName.equals(ContentName.onDay))
      negAppr01 = negAppr01ReposDay.findById(negAppr01Id);
    else if (dbName.equals(ContentName.onMon))
      negAppr01 = negAppr01ReposMon.findById(negAppr01Id);
    else if (dbName.equals(ContentName.onHist))
      negAppr01 = negAppr01ReposHist.findById(negAppr01Id);
    else 
      negAppr01 = negAppr01Repos.findById(negAppr01Id);
    NegAppr01 obj = negAppr01.isPresent() ? negAppr01.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<NegAppr01> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "TitaTlrNo", "TitaTxtNo", "FinCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "TitaTlrNo", "TitaTxtNo", "FinCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAll(pageable);
    else 
      slice = negAppr01Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByCustNoIsOrderByExportDateDescCustNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByCustNoIsOrderByExportDateDescCustNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByCustNoIsOrderByExportDateDescCustNoAsc(custNo_0, pageable);
    else 
      slice = negAppr01Repos.findAllByCustNoIsOrderByExportDateDescCustNoAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> custNoExportDateEq(int custNo_0, int exportDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoExportDateEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " exportDate_1 : " +  exportDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByCustNoIsAndExportDateIsOrderByExportDateDescCustNoAsc(custNo_0, exportDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByCustNoIsAndExportDateIsOrderByExportDateDescCustNoAsc(custNo_0, exportDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByCustNoIsAndExportDateIsOrderByExportDateDescCustNoAsc(custNo_0, exportDate_1, pageable);
    else 
      slice = negAppr01Repos.findAllByCustNoIsAndExportDateIsOrderByExportDateDescCustNoAsc(custNo_0, exportDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> custExporBetween(int custNo_0, int exportDate_1, int exportDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custExporBetween " + dbName + " : " + "custNo_0 : " + custNo_0 + " exportDate_1 : " +  exportDate_1 + " exportDate_2 : " +  exportDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByCustNoIsAndExportDateGreaterThanEqualAndExportDateLessThanEqualOrderByExportDateDescCustNoAsc(custNo_0, exportDate_1, exportDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByCustNoIsAndExportDateGreaterThanEqualAndExportDateLessThanEqualOrderByExportDateDescCustNoAsc(custNo_0, exportDate_1, exportDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByCustNoIsAndExportDateGreaterThanEqualAndExportDateLessThanEqualOrderByExportDateDescCustNoAsc(custNo_0, exportDate_1, exportDate_2, pageable);
    else 
      slice = negAppr01Repos.findAllByCustNoIsAndExportDateGreaterThanEqualAndExportDateLessThanEqualOrderByExportDateDescCustNoAsc(custNo_0, exportDate_1, exportDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> exportEq(int exportDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("exportEq " + dbName + " : " + "exportDate_0 : " + exportDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByExportDateIsOrderByExportDateDescCustNoAsc(exportDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByExportDateIsOrderByExportDateDescCustNoAsc(exportDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByExportDateIsOrderByExportDateDescCustNoAsc(exportDate_0, pageable);
    else 
      slice = negAppr01Repos.findAllByExportDateIsOrderByExportDateDescCustNoAsc(exportDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> exportDateBetween(int exportDate_0, int exportDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("exportDateBetween " + dbName + " : " + "exportDate_0 : " + exportDate_0 + " exportDate_1 : " +  exportDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByExportDateGreaterThanEqualAndExportDateLessThanEqualOrderByExportDateDescCustNoAsc(exportDate_0, exportDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByExportDateGreaterThanEqualAndExportDateLessThanEqualOrderByExportDateDescCustNoAsc(exportDate_0, exportDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByExportDateGreaterThanEqualAndExportDateLessThanEqualOrderByExportDateDescCustNoAsc(exportDate_0, exportDate_1, pageable);
    else 
      slice = negAppr01Repos.findAllByExportDateGreaterThanEqualAndExportDateLessThanEqualOrderByExportDateDescCustNoAsc(exportDate_0, exportDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> bringUpDateEq(int bringUpDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("bringUpDateEq " + dbName + " : " + "bringUpDate_0 : " + bringUpDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByBringUpDateIs(bringUpDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByBringUpDateIs(bringUpDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByBringUpDateIs(bringUpDate_0, pageable);
    else 
      slice = negAppr01Repos.findAllByBringUpDateIs(bringUpDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> findBatch(String batchTxtNo_0, String remitBank_1, int apprDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findBatch " + dbName + " : " + "batchTxtNo_0 : " + batchTxtNo_0 + " remitBank_1 : " +  remitBank_1 + " apprDate_2 : " +  apprDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByBatchTxtNoIsAndRemitBankIsAndApprDateIs(batchTxtNo_0, remitBank_1, apprDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByBatchTxtNoIsAndRemitBankIsAndApprDateIs(batchTxtNo_0, remitBank_1, apprDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByBatchTxtNoIsAndRemitBankIsAndApprDateIs(batchTxtNo_0, remitBank_1, apprDate_2, pageable);
    else 
      slice = negAppr01Repos.findAllByBatchTxtNoIsAndRemitBankIsAndApprDateIs(batchTxtNo_0, remitBank_1, apprDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> sumCustNo(int custNo_0, int caseSeq_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("sumCustNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " caseSeq_1 : " +  caseSeq_1);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByCustNoIsAndCaseSeqIs(custNo_0, caseSeq_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByCustNoIsAndCaseSeqIs(custNo_0, caseSeq_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByCustNoIsAndCaseSeqIs(custNo_0, caseSeq_1, pageable);
    else 
      slice = negAppr01Repos.findAllByCustNoIsAndCaseSeqIs(custNo_0, caseSeq_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> sumCustNoFinCode(int custNo_0, int caseSeq_1, String finCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("sumCustNoFinCode " + dbName + " : " + "custNo_0 : " + custNo_0 + " caseSeq_1 : " +  caseSeq_1 + " finCode_2 : " +  finCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByCustNoIsAndCaseSeqIsAndFinCodeIs(custNo_0, caseSeq_1, finCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByCustNoIsAndCaseSeqIsAndFinCodeIs(custNo_0, caseSeq_1, finCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByCustNoIsAndCaseSeqIsAndFinCodeIs(custNo_0, caseSeq_1, finCode_2, pageable);
    else 
      slice = negAppr01Repos.findAllByCustNoIsAndCaseSeqIsAndFinCodeIs(custNo_0, caseSeq_1, finCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> findTrans(int acDate_0, String titaTlrNo_1, int titaTxtNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findTrans " + dbName + " : " + "acDate_0 : " + acDate_0 + " titaTlrNo_1 : " +  titaTlrNo_1 + " titaTxtNo_2 : " +  titaTxtNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2, pageable);
    else 
      slice = negAppr01Repos.findAllByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(acDate_0, titaTlrNo_1, titaTxtNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> findByCustNoCaseSeq(int custNo_0, int caseSeq_1, int exportDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByCustNoCaseSeq " + dbName + " : " + "custNo_0 : " + custNo_0 + " caseSeq_1 : " +  caseSeq_1 + " exportDate_2 : " +  exportDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByCustNoIsAndCaseSeqIsAndExportDateIsOrderByFinCodeAscCreateDateDesc(custNo_0, caseSeq_1, exportDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByCustNoIsAndCaseSeqIsAndExportDateIsOrderByFinCodeAscCreateDateDesc(custNo_0, caseSeq_1, exportDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByCustNoIsAndCaseSeqIsAndExportDateIsOrderByFinCodeAscCreateDateDesc(custNo_0, caseSeq_1, exportDate_2, pageable);
    else 
      slice = negAppr01Repos.findAllByCustNoIsAndCaseSeqIsAndExportDateIsOrderByFinCodeAscCreateDateDesc(custNo_0, caseSeq_1, exportDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegAppr01> findExporFinCode(int exportDate_0, String finCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findExporFinCode " + dbName + " : " + "exportDate_0 : " + exportDate_0 + " finCode_1 : " +  finCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByExportDateIsAndFinCodeIsOrderByCustNoAsc(exportDate_0, finCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByExportDateIsAndFinCodeIsOrderByCustNoAsc(exportDate_0, finCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByExportDateIsAndFinCodeIsOrderByCustNoAsc(exportDate_0, finCode_1, pageable);
    else 
      slice = negAppr01Repos.findAllByExportDateIsAndFinCodeIsOrderByCustNoAsc(exportDate_0, finCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegAppr01 bringUpDateFirst(int bringUpDate_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("bringUpDateFirst " + dbName + " : " + "bringUpDate_0 : " + bringUpDate_0);
    Optional<NegAppr01> negAppr01T = null;
    if (dbName.equals(ContentName.onDay))
      negAppr01T = negAppr01ReposDay.findTopByBringUpDateGreaterThanEqualOrderByBringUpDateDesc(bringUpDate_0);
    else if (dbName.equals(ContentName.onMon))
      negAppr01T = negAppr01ReposMon.findTopByBringUpDateGreaterThanEqualOrderByBringUpDateDesc(bringUpDate_0);
    else if (dbName.equals(ContentName.onHist))
      negAppr01T = negAppr01ReposHist.findTopByBringUpDateGreaterThanEqualOrderByBringUpDateDesc(bringUpDate_0);
    else 
      negAppr01T = negAppr01Repos.findTopByBringUpDateGreaterThanEqualOrderByBringUpDateDesc(bringUpDate_0);

    return negAppr01T.isPresent() ? negAppr01T.get() : null;
  }

  @Override
  public NegAppr01 bringUpDateCustNoFirst(int custNo_0, int bringUpDate_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("bringUpDateCustNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " bringUpDate_1 : " +  bringUpDate_1);
    Optional<NegAppr01> negAppr01T = null;
    if (dbName.equals(ContentName.onDay))
      negAppr01T = negAppr01ReposDay.findTopByCustNoIsAndBringUpDateGreaterThanEqualOrderByBringUpDateDesc(custNo_0, bringUpDate_1);
    else if (dbName.equals(ContentName.onMon))
      negAppr01T = negAppr01ReposMon.findTopByCustNoIsAndBringUpDateGreaterThanEqualOrderByBringUpDateDesc(custNo_0, bringUpDate_1);
    else if (dbName.equals(ContentName.onHist))
      negAppr01T = negAppr01ReposHist.findTopByCustNoIsAndBringUpDateGreaterThanEqualOrderByBringUpDateDesc(custNo_0, bringUpDate_1);
    else 
      negAppr01T = negAppr01Repos.findTopByCustNoIsAndBringUpDateGreaterThanEqualOrderByBringUpDateDesc(custNo_0, bringUpDate_1);

    return negAppr01T.isPresent() ? negAppr01T.get() : null;
  }

  @Override
  public Slice<NegAppr01> bringUpDateCustNoEq(int custNo_0, int bringUpDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegAppr01> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("bringUpDateCustNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " bringUpDate_1 : " +  bringUpDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = negAppr01ReposDay.findAllByCustNoIsAndBringUpDateIsOrderByFinCodeAsc(custNo_0, bringUpDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negAppr01ReposMon.findAllByCustNoIsAndBringUpDateIsOrderByFinCodeAsc(custNo_0, bringUpDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negAppr01ReposHist.findAllByCustNoIsAndBringUpDateIsOrderByFinCodeAsc(custNo_0, bringUpDate_1, pageable);
    else 
      slice = negAppr01Repos.findAllByCustNoIsAndBringUpDateIsOrderByFinCodeAsc(custNo_0, bringUpDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegAppr01 holdById(NegAppr01Id negAppr01Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + negAppr01Id);
    Optional<NegAppr01> negAppr01 = null;
    if (dbName.equals(ContentName.onDay))
      negAppr01 = negAppr01ReposDay.findByNegAppr01Id(negAppr01Id);
    else if (dbName.equals(ContentName.onMon))
      negAppr01 = negAppr01ReposMon.findByNegAppr01Id(negAppr01Id);
    else if (dbName.equals(ContentName.onHist))
      negAppr01 = negAppr01ReposHist.findByNegAppr01Id(negAppr01Id);
    else 
      negAppr01 = negAppr01Repos.findByNegAppr01Id(negAppr01Id);
    return negAppr01.isPresent() ? negAppr01.get() : null;
  }

  @Override
  public NegAppr01 holdById(NegAppr01 negAppr01, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + negAppr01.getNegAppr01Id());
    Optional<NegAppr01> negAppr01T = null;
    if (dbName.equals(ContentName.onDay))
      negAppr01T = negAppr01ReposDay.findByNegAppr01Id(negAppr01.getNegAppr01Id());
    else if (dbName.equals(ContentName.onMon))
      negAppr01T = negAppr01ReposMon.findByNegAppr01Id(negAppr01.getNegAppr01Id());
    else if (dbName.equals(ContentName.onHist))
      negAppr01T = negAppr01ReposHist.findByNegAppr01Id(negAppr01.getNegAppr01Id());
    else 
      negAppr01T = negAppr01Repos.findByNegAppr01Id(negAppr01.getNegAppr01Id());
    return negAppr01T.isPresent() ? negAppr01T.get() : null;
  }

  @Override
  public NegAppr01 insert(NegAppr01 negAppr01, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + negAppr01.getNegAppr01Id());
    if (this.findById(negAppr01.getNegAppr01Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      negAppr01.setCreateEmpNo(empNot);

    if(negAppr01.getLastUpdateEmpNo() == null || negAppr01.getLastUpdateEmpNo().isEmpty())
      negAppr01.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negAppr01ReposDay.saveAndFlush(negAppr01);	
    else if (dbName.equals(ContentName.onMon))
      return negAppr01ReposMon.saveAndFlush(negAppr01);
    else if (dbName.equals(ContentName.onHist))
      return negAppr01ReposHist.saveAndFlush(negAppr01);
    else 
    return negAppr01Repos.saveAndFlush(negAppr01);
  }

  @Override
  public NegAppr01 update(NegAppr01 negAppr01, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + negAppr01.getNegAppr01Id());
    if (!empNot.isEmpty())
      negAppr01.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negAppr01ReposDay.saveAndFlush(negAppr01);	
    else if (dbName.equals(ContentName.onMon))
      return negAppr01ReposMon.saveAndFlush(negAppr01);
    else if (dbName.equals(ContentName.onHist))
      return negAppr01ReposHist.saveAndFlush(negAppr01);
    else 
    return negAppr01Repos.saveAndFlush(negAppr01);
  }

  @Override
  public NegAppr01 update2(NegAppr01 negAppr01, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + negAppr01.getNegAppr01Id());
    if (!empNot.isEmpty())
      negAppr01.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      negAppr01ReposDay.saveAndFlush(negAppr01);	
    else if (dbName.equals(ContentName.onMon))
      negAppr01ReposMon.saveAndFlush(negAppr01);
    else if (dbName.equals(ContentName.onHist))
        negAppr01ReposHist.saveAndFlush(negAppr01);
    else 
      negAppr01Repos.saveAndFlush(negAppr01);	
    return this.findById(negAppr01.getNegAppr01Id());
  }

  @Override
  public void delete(NegAppr01 negAppr01, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + negAppr01.getNegAppr01Id());
    if (dbName.equals(ContentName.onDay)) {
      negAppr01ReposDay.delete(negAppr01);	
      negAppr01ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negAppr01ReposMon.delete(negAppr01);	
      negAppr01ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negAppr01ReposHist.delete(negAppr01);
      negAppr01ReposHist.flush();
    }
    else {
      negAppr01Repos.delete(negAppr01);
      negAppr01Repos.flush();
    }
   }

  @Override
  public void insertAll(List<NegAppr01> negAppr01, TitaVo... titaVo) throws DBException {
    if (negAppr01 == null || negAppr01.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (NegAppr01 t : negAppr01){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      negAppr01 = negAppr01ReposDay.saveAll(negAppr01);	
      negAppr01ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negAppr01 = negAppr01ReposMon.saveAll(negAppr01);	
      negAppr01ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negAppr01 = negAppr01ReposHist.saveAll(negAppr01);
      negAppr01ReposHist.flush();
    }
    else {
      negAppr01 = negAppr01Repos.saveAll(negAppr01);
      negAppr01Repos.flush();
    }
    }

  @Override
  public void updateAll(List<NegAppr01> negAppr01, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (negAppr01 == null || negAppr01.size() == 0)
      throw new DBException(6);

    for (NegAppr01 t : negAppr01) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      negAppr01 = negAppr01ReposDay.saveAll(negAppr01);	
      negAppr01ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negAppr01 = negAppr01ReposMon.saveAll(negAppr01);	
      negAppr01ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negAppr01 = negAppr01ReposHist.saveAll(negAppr01);
      negAppr01ReposHist.flush();
    }
    else {
      negAppr01 = negAppr01Repos.saveAll(negAppr01);
      negAppr01Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<NegAppr01> negAppr01, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (negAppr01 == null || negAppr01.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      negAppr01ReposDay.deleteAll(negAppr01);	
      negAppr01ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negAppr01ReposMon.deleteAll(negAppr01);	
      negAppr01ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negAppr01ReposHist.deleteAll(negAppr01);
      negAppr01ReposHist.flush();
    }
    else {
      negAppr01Repos.deleteAll(negAppr01);
      negAppr01Repos.flush();
    }
  }

}
