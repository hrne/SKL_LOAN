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
import com.st1.itx.db.domain.BatxOthers;
import com.st1.itx.db.domain.BatxOthersId;
import com.st1.itx.db.repository.online.BatxOthersRepository;
import com.st1.itx.db.repository.day.BatxOthersRepositoryDay;
import com.st1.itx.db.repository.mon.BatxOthersRepositoryMon;
import com.st1.itx.db.repository.hist.BatxOthersRepositoryHist;
import com.st1.itx.db.service.BatxOthersService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("batxOthersService")
@Repository
public class BatxOthersServiceImpl extends ASpringJpaParm implements BatxOthersService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private BatxOthersRepository batxOthersRepos;

  @Autowired
  private BatxOthersRepositoryDay batxOthersReposDay;

  @Autowired
  private BatxOthersRepositoryMon batxOthersReposMon;

  @Autowired
  private BatxOthersRepositoryHist batxOthersReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(batxOthersRepos);
    org.junit.Assert.assertNotNull(batxOthersReposDay);
    org.junit.Assert.assertNotNull(batxOthersReposMon);
    org.junit.Assert.assertNotNull(batxOthersReposHist);
  }

  @Override
  public BatxOthers findById(BatxOthersId batxOthersId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + batxOthersId);
    Optional<BatxOthers> batxOthers = null;
    if (dbName.equals(ContentName.onDay))
      batxOthers = batxOthersReposDay.findById(batxOthersId);
    else if (dbName.equals(ContentName.onMon))
      batxOthers = batxOthersReposMon.findById(batxOthersId);
    else if (dbName.equals(ContentName.onHist))
      batxOthers = batxOthersReposHist.findById(batxOthersId);
    else 
      batxOthers = batxOthersRepos.findById(batxOthersId);
    BatxOthers obj = batxOthers.isPresent() ? batxOthers.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<BatxOthers> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "BatchNo", "DetailSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "BatchNo", "DetailSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAll(pageable);
    else 
      slice = batxOthersRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchRuleA(int acDate_0, int acDate_1, String batchNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchRuleA " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " batchNo_2 : " +  batchNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIs(acDate_0, acDate_1, batchNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIs(acDate_0, acDate_1, batchNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIs(acDate_0, acDate_1, batchNo_2, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIs(acDate_0, acDate_1, batchNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchRuleB(int acDate_0, int acDate_1, String batchNo_2, int repayCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchRuleB " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " batchNo_2 : " +  batchNo_2 + " repayCode_3 : " +  repayCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndRepayCodeIs(acDate_0, acDate_1, batchNo_2, repayCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndRepayCodeIs(acDate_0, acDate_1, batchNo_2, repayCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndRepayCodeIs(acDate_0, acDate_1, batchNo_2, repayCode_3, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndRepayCodeIs(acDate_0, acDate_1, batchNo_2, repayCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchRuleC(int acDate_0, int acDate_1, String batchNo_2, String createEmpNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchRuleC " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " batchNo_2 : " +  batchNo_2 + " createEmpNo_3 : " +  createEmpNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndCreateEmpNoIs(acDate_0, acDate_1, batchNo_2, createEmpNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndCreateEmpNoIs(acDate_0, acDate_1, batchNo_2, createEmpNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndCreateEmpNoIs(acDate_0, acDate_1, batchNo_2, createEmpNo_3, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndCreateEmpNoIs(acDate_0, acDate_1, batchNo_2, createEmpNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchRuleD(int acDate_0, int acDate_1, String batchNo_2, int repayCode_3, String createEmpNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchRuleD " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " batchNo_2 : " +  batchNo_2 + " repayCode_3 : " +  repayCode_3 + " createEmpNo_4 : " +  createEmpNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, batchNo_2, repayCode_3, createEmpNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, batchNo_2, repayCode_3, createEmpNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, batchNo_2, repayCode_3, createEmpNo_4, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, batchNo_2, repayCode_3, createEmpNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BatxOthers detSeqFirst(int acDate_0, String batchNo_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("detSeqFirst " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1);
    Optional<BatxOthers> batxOthersT = null;
    if (dbName.equals(ContentName.onDay))
      batxOthersT = batxOthersReposDay.findTopByAcDateIsAndBatchNoIsOrderByDetailSeqDesc(acDate_0, batchNo_1);
    else if (dbName.equals(ContentName.onMon))
      batxOthersT = batxOthersReposMon.findTopByAcDateIsAndBatchNoIsOrderByDetailSeqDesc(acDate_0, batchNo_1);
    else if (dbName.equals(ContentName.onHist))
      batxOthersT = batxOthersReposHist.findTopByAcDateIsAndBatchNoIsOrderByDetailSeqDesc(acDate_0, batchNo_1);
    else 
      batxOthersT = batxOthersRepos.findTopByAcDateIsAndBatchNoIsOrderByDetailSeqDesc(acDate_0, batchNo_1);

    return batxOthersT.isPresent() ? batxOthersT.get() : null;
  }

  @Override
  public Slice<BatxOthers> searchRuleE(int acDate_0, int acDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchRuleE " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqual(acDate_0, acDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqual(acDate_0, acDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqual(acDate_0, acDate_1, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqual(acDate_0, acDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchRuleF(int acDate_0, int acDate_1, int repayCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchRuleF " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " repayCode_2 : " +  repayCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIs(acDate_0, acDate_1, repayCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIs(acDate_0, acDate_1, repayCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIs(acDate_0, acDate_1, repayCode_2, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIs(acDate_0, acDate_1, repayCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchRuleG(int acDate_0, int acDate_1, String createEmpNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchRuleG " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " createEmpNo_2 : " +  createEmpNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCreateEmpNoIs(acDate_0, acDate_1, createEmpNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCreateEmpNoIs(acDate_0, acDate_1, createEmpNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCreateEmpNoIs(acDate_0, acDate_1, createEmpNo_2, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCreateEmpNoIs(acDate_0, acDate_1, createEmpNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchRuleH(int acDate_0, int acDate_1, int repayCode_2, String createEmpNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchRuleH " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " repayCode_2 : " +  repayCode_2 + " createEmpNo_3 : " +  createEmpNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, repayCode_2, createEmpNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, repayCode_2, createEmpNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, repayCode_2, createEmpNo_3, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, repayCode_2, createEmpNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BatxOthers findTxSeqFirst(int titaEntdy_0, String titaTlrNo_1, String titaTxtNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findTxSeqFirst " + dbName + " : " + "titaEntdy_0 : " + titaEntdy_0 + " titaTlrNo_1 : " +  titaTlrNo_1 + " titaTxtNo_2 : " +  titaTxtNo_2);
    Optional<BatxOthers> batxOthersT = null;
    if (dbName.equals(ContentName.onDay))
      batxOthersT = batxOthersReposDay.findTopByTitaEntdyIsAndTitaTlrNoIsAndTitaTxtNoIs(titaEntdy_0, titaTlrNo_1, titaTxtNo_2);
    else if (dbName.equals(ContentName.onMon))
      batxOthersT = batxOthersReposMon.findTopByTitaEntdyIsAndTitaTlrNoIsAndTitaTxtNoIs(titaEntdy_0, titaTlrNo_1, titaTxtNo_2);
    else if (dbName.equals(ContentName.onHist))
      batxOthersT = batxOthersReposHist.findTopByTitaEntdyIsAndTitaTlrNoIsAndTitaTxtNoIs(titaEntdy_0, titaTlrNo_1, titaTxtNo_2);
    else 
      batxOthersT = batxOthersRepos.findTopByTitaEntdyIsAndTitaTlrNoIsAndTitaTxtNoIs(titaEntdy_0, titaTlrNo_1, titaTxtNo_2);

    return batxOthersT.isPresent() ? batxOthersT.get() : null;
  }

  @Override
  public Slice<BatxOthers> searchCustNoA(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchCustNoA " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " custNo_2 : " +  custNo_2 + " batchNo_3 : " +  batchNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchCustNoB(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, int repayCode_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchCustNoB " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " custNo_2 : " +  custNo_2 + " batchNo_3 : " +  batchNo_3 + " repayCode_4 : " +  repayCode_4);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndRepayCodeIs(acDate_0, acDate_1, custNo_2, batchNo_3, repayCode_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndRepayCodeIs(acDate_0, acDate_1, custNo_2, batchNo_3, repayCode_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndRepayCodeIs(acDate_0, acDate_1, custNo_2, batchNo_3, repayCode_4, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndRepayCodeIs(acDate_0, acDate_1, custNo_2, batchNo_3, repayCode_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchCustNoC(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, String createEmpNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchCustNoC " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " custNo_2 : " +  custNo_2 + " batchNo_3 : " +  batchNo_3 + " createEmpNo_4 : " +  createEmpNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, createEmpNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, createEmpNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, createEmpNo_4, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, createEmpNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchCustNoD(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, int repayCode_4, String createEmpNo_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchCustNoD " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " custNo_2 : " +  custNo_2 + " batchNo_3 : " +  batchNo_3 + " repayCode_4 : " +  repayCode_4 + " createEmpNo_5 : " +  createEmpNo_5);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, repayCode_4, createEmpNo_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, repayCode_4, createEmpNo_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, repayCode_4, createEmpNo_5, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, batchNo_3, repayCode_4, createEmpNo_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchCustNoE(int acDate_0, int acDate_1, int custNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchCustNoE " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " custNo_2 : " +  custNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIs(acDate_0, acDate_1, custNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIs(acDate_0, acDate_1, custNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIs(acDate_0, acDate_1, custNo_2, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIs(acDate_0, acDate_1, custNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchCustNoF(int acDate_0, int acDate_1, int custNo_2, int repayCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchCustNoF " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " custNo_2 : " +  custNo_2 + " repayCode_3 : " +  repayCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndRepayCodeIs(acDate_0, acDate_1, custNo_2, repayCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndRepayCodeIs(acDate_0, acDate_1, custNo_2, repayCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndRepayCodeIs(acDate_0, acDate_1, custNo_2, repayCode_3, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndRepayCodeIs(acDate_0, acDate_1, custNo_2, repayCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchCustNoG(int acDate_0, int acDate_1, int custNo_2, String createEmpNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchCustNoG " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " custNo_2 : " +  custNo_2 + " createEmpNo_3 : " +  createEmpNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, createEmpNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, createEmpNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, createEmpNo_3, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, createEmpNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxOthers> searchCustNoH(int acDate_0, int acDate_1, int custNo_2, int repayCode_3, String createEmpNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxOthers> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("searchCustNoH " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " custNo_2 : " +  custNo_2 + " repayCode_3 : " +  repayCode_3 + " createEmpNo_4 : " +  createEmpNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = batxOthersReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, repayCode_3, createEmpNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxOthersReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, repayCode_3, createEmpNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxOthersReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, repayCode_3, createEmpNo_4, pageable);
    else 
      slice = batxOthersRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndRepayCodeIsAndCreateEmpNoIs(acDate_0, acDate_1, custNo_2, repayCode_3, createEmpNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BatxOthers holdById(BatxOthersId batxOthersId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + batxOthersId);
    Optional<BatxOthers> batxOthers = null;
    if (dbName.equals(ContentName.onDay))
      batxOthers = batxOthersReposDay.findByBatxOthersId(batxOthersId);
    else if (dbName.equals(ContentName.onMon))
      batxOthers = batxOthersReposMon.findByBatxOthersId(batxOthersId);
    else if (dbName.equals(ContentName.onHist))
      batxOthers = batxOthersReposHist.findByBatxOthersId(batxOthersId);
    else 
      batxOthers = batxOthersRepos.findByBatxOthersId(batxOthersId);
    return batxOthers.isPresent() ? batxOthers.get() : null;
  }

  @Override
  public BatxOthers holdById(BatxOthers batxOthers, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + batxOthers.getBatxOthersId());
    Optional<BatxOthers> batxOthersT = null;
    if (dbName.equals(ContentName.onDay))
      batxOthersT = batxOthersReposDay.findByBatxOthersId(batxOthers.getBatxOthersId());
    else if (dbName.equals(ContentName.onMon))
      batxOthersT = batxOthersReposMon.findByBatxOthersId(batxOthers.getBatxOthersId());
    else if (dbName.equals(ContentName.onHist))
      batxOthersT = batxOthersReposHist.findByBatxOthersId(batxOthers.getBatxOthersId());
    else 
      batxOthersT = batxOthersRepos.findByBatxOthersId(batxOthers.getBatxOthersId());
    return batxOthersT.isPresent() ? batxOthersT.get() : null;
  }

  @Override
  public BatxOthers insert(BatxOthers batxOthers, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + batxOthers.getBatxOthersId());
    if (this.findById(batxOthers.getBatxOthersId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      batxOthers.setCreateEmpNo(empNot);

    if(batxOthers.getLastUpdateEmpNo() == null || batxOthers.getLastUpdateEmpNo().isEmpty())
      batxOthers.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return batxOthersReposDay.saveAndFlush(batxOthers);	
    else if (dbName.equals(ContentName.onMon))
      return batxOthersReposMon.saveAndFlush(batxOthers);
    else if (dbName.equals(ContentName.onHist))
      return batxOthersReposHist.saveAndFlush(batxOthers);
    else 
    return batxOthersRepos.saveAndFlush(batxOthers);
  }

  @Override
  public BatxOthers update(BatxOthers batxOthers, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + batxOthers.getBatxOthersId());
    if (!empNot.isEmpty())
      batxOthers.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return batxOthersReposDay.saveAndFlush(batxOthers);	
    else if (dbName.equals(ContentName.onMon))
      return batxOthersReposMon.saveAndFlush(batxOthers);
    else if (dbName.equals(ContentName.onHist))
      return batxOthersReposHist.saveAndFlush(batxOthers);
    else 
    return batxOthersRepos.saveAndFlush(batxOthers);
  }

  @Override
  public BatxOthers update2(BatxOthers batxOthers, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + batxOthers.getBatxOthersId());
    if (!empNot.isEmpty())
      batxOthers.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      batxOthersReposDay.saveAndFlush(batxOthers);	
    else if (dbName.equals(ContentName.onMon))
      batxOthersReposMon.saveAndFlush(batxOthers);
    else if (dbName.equals(ContentName.onHist))
        batxOthersReposHist.saveAndFlush(batxOthers);
    else 
      batxOthersRepos.saveAndFlush(batxOthers);	
    return this.findById(batxOthers.getBatxOthersId());
  }

  @Override
  public void delete(BatxOthers batxOthers, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + batxOthers.getBatxOthersId());
    if (dbName.equals(ContentName.onDay)) {
      batxOthersReposDay.delete(batxOthers);	
      batxOthersReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxOthersReposMon.delete(batxOthers);	
      batxOthersReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxOthersReposHist.delete(batxOthers);
      batxOthersReposHist.flush();
    }
    else {
      batxOthersRepos.delete(batxOthers);
      batxOthersRepos.flush();
    }
   }

  @Override
  public void insertAll(List<BatxOthers> batxOthers, TitaVo... titaVo) throws DBException {
    if (batxOthers == null || batxOthers.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (BatxOthers t : batxOthers){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      batxOthers = batxOthersReposDay.saveAll(batxOthers);	
      batxOthersReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxOthers = batxOthersReposMon.saveAll(batxOthers);	
      batxOthersReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxOthers = batxOthersReposHist.saveAll(batxOthers);
      batxOthersReposHist.flush();
    }
    else {
      batxOthers = batxOthersRepos.saveAll(batxOthers);
      batxOthersRepos.flush();
    }
    }

  @Override
  public void updateAll(List<BatxOthers> batxOthers, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (batxOthers == null || batxOthers.size() == 0)
      throw new DBException(6);

    for (BatxOthers t : batxOthers) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      batxOthers = batxOthersReposDay.saveAll(batxOthers);	
      batxOthersReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxOthers = batxOthersReposMon.saveAll(batxOthers);	
      batxOthersReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxOthers = batxOthersReposHist.saveAll(batxOthers);
      batxOthersReposHist.flush();
    }
    else {
      batxOthers = batxOthersRepos.saveAll(batxOthers);
      batxOthersRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<BatxOthers> batxOthers, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (batxOthers == null || batxOthers.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      batxOthersReposDay.deleteAll(batxOthers);	
      batxOthersReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxOthersReposMon.deleteAll(batxOthers);	
      batxOthersReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxOthersReposHist.deleteAll(batxOthers);
      batxOthersReposHist.flush();
    }
    else {
      batxOthersRepos.deleteAll(batxOthers);
      batxOthersRepos.flush();
    }
  }

}
