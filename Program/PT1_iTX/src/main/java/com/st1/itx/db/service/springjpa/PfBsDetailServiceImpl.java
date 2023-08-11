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
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.repository.online.PfBsDetailRepository;
import com.st1.itx.db.repository.day.PfBsDetailRepositoryDay;
import com.st1.itx.db.repository.mon.PfBsDetailRepositoryMon;
import com.st1.itx.db.repository.hist.PfBsDetailRepositoryHist;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfBsDetailService")
@Repository
public class PfBsDetailServiceImpl extends ASpringJpaParm implements PfBsDetailService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private PfBsDetailRepository pfBsDetailRepos;

  @Autowired
  private PfBsDetailRepositoryDay pfBsDetailReposDay;

  @Autowired
  private PfBsDetailRepositoryMon pfBsDetailReposMon;

  @Autowired
  private PfBsDetailRepositoryHist pfBsDetailReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(pfBsDetailRepos);
    org.junit.Assert.assertNotNull(pfBsDetailReposDay);
    org.junit.Assert.assertNotNull(pfBsDetailReposMon);
    org.junit.Assert.assertNotNull(pfBsDetailReposHist);
  }

  @Override
  public PfBsDetail findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<PfBsDetail> pfBsDetail = null;
    if (dbName.equals(ContentName.onDay))
      pfBsDetail = pfBsDetailReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfBsDetail = pfBsDetailReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfBsDetail = pfBsDetailReposHist.findById(logNo);
    else 
      pfBsDetail = pfBsDetailRepos.findById(logNo);
    PfBsDetail obj = pfBsDetail.isPresent() ? pfBsDetail.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<PfBsDetail> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAll(pageable);
    else 
      slice = pfBsDetailRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfBsDetail> findFacmNoRange(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findFacmNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
    else 
      slice = pfBsDetailRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfBsDetail> findBsOfficerAndWorkMonth(String bsOfficer_0, int workMonth_1, int workMonth_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findBsOfficerAndWorkMonth " + dbName + " : " + "bsOfficer_0 : " + bsOfficer_0 + " workMonth_1 : " +  workMonth_1 + " workMonth_2 : " +  workMonth_2);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAllByBsOfficerIsAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqual(bsOfficer_0, workMonth_1, workMonth_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAllByBsOfficerIsAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqual(bsOfficer_0, workMonth_1, workMonth_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAllByBsOfficerIsAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqual(bsOfficer_0, workMonth_1, workMonth_2, pageable);
    else 
      slice = pfBsDetailRepos.findAllByBsOfficerIsAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqual(bsOfficer_0, workMonth_1, workMonth_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfBsDetail> findBsOfficerOneMonth(String bsOfficer_0, int workMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findBsOfficerOneMonth " + dbName + " : " + "bsOfficer_0 : " + bsOfficer_0 + " workMonth_1 : " +  workMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAllByBsOfficerIsAndWorkMonthIs(bsOfficer_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAllByBsOfficerIsAndWorkMonthIs(bsOfficer_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAllByBsOfficerIsAndWorkMonthIs(bsOfficer_0, workMonth_1, pageable);
    else 
      slice = pfBsDetailRepos.findAllByBsOfficerIsAndWorkMonthIs(bsOfficer_0, workMonth_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfBsDetail> findByPerfDate(int perfDate_0, int perfDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByPerfDate " + dbName + " : " + "perfDate_0 : " + perfDate_0 + " perfDate_1 : " +  perfDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByPerfDateAscDeptCodeAscBsOfficerAsc(perfDate_0, perfDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByPerfDateAscDeptCodeAscBsOfficerAsc(perfDate_0, perfDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByPerfDateAscDeptCodeAscBsOfficerAsc(perfDate_0, perfDate_1, pageable);
    else 
      slice = pfBsDetailRepos.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByPerfDateAscDeptCodeAscBsOfficerAsc(perfDate_0, perfDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfBsDetail> findByBsOfficerAndPerfDate(String bsOfficer_0, int perfDate_1, int perfDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBsOfficerAndPerfDate " + dbName + " : " + "bsOfficer_0 : " + bsOfficer_0 + " perfDate_1 : " +  perfDate_1 + " perfDate_2 : " +  perfDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAllByBsOfficerIsAndPerfDateGreaterThanEqualAndPerfDateLessThanEqual(bsOfficer_0, perfDate_1, perfDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAllByBsOfficerIsAndPerfDateGreaterThanEqualAndPerfDateLessThanEqual(bsOfficer_0, perfDate_1, perfDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAllByBsOfficerIsAndPerfDateGreaterThanEqualAndPerfDateLessThanEqual(bsOfficer_0, perfDate_1, perfDate_2, pageable);
    else 
      slice = pfBsDetailRepos.findAllByBsOfficerIsAndPerfDateGreaterThanEqualAndPerfDateLessThanEqual(bsOfficer_0, perfDate_1, perfDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfBsDetail> findByPerfDateAndCustNo(int perfDate_0, int perfDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByPerfDateAndCustNo " + dbName + " : " + "perfDate_0 : " + perfDate_0 + " perfDate_1 : " +  perfDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAsc(perfDate_0, perfDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAsc(perfDate_0, perfDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAsc(perfDate_0, perfDate_1, pageable);
    else 
      slice = pfBsDetailRepos.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAsc(perfDate_0, perfDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfBsDetail> findByWorkMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByWorkMonth " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " workMonth_1 : " +  workMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
    else 
      slice = pfBsDetailRepos.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfBsDetail> findByCustNoAndFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByCustNoAndFacmNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, pageable);
    else 
      slice = pfBsDetailRepos.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfBsDetail> findByCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAllByCustNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAllByCustNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAllByCustNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, pageable);
    else 
      slice = pfBsDetailRepos.findAllByCustNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfBsDetail findByTxFirst(int custNo_0, int facmNo_1, int bormNo_2, int perfDate_3, int repayType_4, String pieceCode_5, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findByTxFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " perfDate_3 : " +  perfDate_3 + " repayType_4 : " +  repayType_4 + " pieceCode_5 : " +  pieceCode_5);
    Optional<PfBsDetail> pfBsDetailT = null;
    if (dbName.equals(ContentName.onDay))
      pfBsDetailT = pfBsDetailReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);
    else if (dbName.equals(ContentName.onMon))
      pfBsDetailT = pfBsDetailReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);
    else if (dbName.equals(ContentName.onHist))
      pfBsDetailT = pfBsDetailReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);
    else 
      pfBsDetailT = pfBsDetailRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);

    return pfBsDetailT.isPresent() ? pfBsDetailT.get() : null;
  }

  @Override
  public PfBsDetail findBormNoLatestFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findBormNoLatestFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2);
    Optional<PfBsDetail> pfBsDetailT = null;
    if (dbName.equals(ContentName.onDay))
      pfBsDetailT = pfBsDetailReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateDesc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onMon))
      pfBsDetailT = pfBsDetailReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateDesc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onHist))
      pfBsDetailT = pfBsDetailReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateDesc(custNo_0, facmNo_1, bormNo_2);
    else 
      pfBsDetailT = pfBsDetailRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateDesc(custNo_0, facmNo_1, bormNo_2);

    return pfBsDetailT.isPresent() ? pfBsDetailT.get() : null;
  }

  @Override
  public Slice<PfBsDetail> findBormNoEq(int custNo_0, int facmNo_1, int bormNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findBormNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);
    else 
      slice = pfBsDetailRepos.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfBsDetail> findDrawdownBetween(int drawdownDate_0, int drawdownDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfBsDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findDrawdownBetween " + dbName + " : " + "drawdownDate_0 : " + drawdownDate_0 + " drawdownDate_1 : " +  drawdownDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfBsDetailReposDay.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByDrawdownDateAsc(drawdownDate_0, drawdownDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfBsDetailReposMon.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByDrawdownDateAsc(drawdownDate_0, drawdownDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfBsDetailReposHist.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByDrawdownDateAsc(drawdownDate_0, drawdownDate_1, pageable);
    else 
      slice = pfBsDetailRepos.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByDrawdownDateAsc(drawdownDate_0, drawdownDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfBsDetail findBormNoFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findBormNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2);
    Optional<PfBsDetail> pfBsDetailT = null;
    if (dbName.equals(ContentName.onDay))
      pfBsDetailT = pfBsDetailReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAscLogNoAsc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onMon))
      pfBsDetailT = pfBsDetailReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAscLogNoAsc(custNo_0, facmNo_1, bormNo_2);
    else if (dbName.equals(ContentName.onHist))
      pfBsDetailT = pfBsDetailReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAscLogNoAsc(custNo_0, facmNo_1, bormNo_2);
    else 
      pfBsDetailT = pfBsDetailRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAscLogNoAsc(custNo_0, facmNo_1, bormNo_2);

    return pfBsDetailT.isPresent() ? pfBsDetailT.get() : null;
  }

  @Override
  public PfBsDetail holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<PfBsDetail> pfBsDetail = null;
    if (dbName.equals(ContentName.onDay))
      pfBsDetail = pfBsDetailReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      pfBsDetail = pfBsDetailReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      pfBsDetail = pfBsDetailReposHist.findByLogNo(logNo);
    else 
      pfBsDetail = pfBsDetailRepos.findByLogNo(logNo);
    return pfBsDetail.isPresent() ? pfBsDetail.get() : null;
  }

  @Override
  public PfBsDetail holdById(PfBsDetail pfBsDetail, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfBsDetail.getLogNo());
    Optional<PfBsDetail> pfBsDetailT = null;
    if (dbName.equals(ContentName.onDay))
      pfBsDetailT = pfBsDetailReposDay.findByLogNo(pfBsDetail.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      pfBsDetailT = pfBsDetailReposMon.findByLogNo(pfBsDetail.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      pfBsDetailT = pfBsDetailReposHist.findByLogNo(pfBsDetail.getLogNo());
    else 
      pfBsDetailT = pfBsDetailRepos.findByLogNo(pfBsDetail.getLogNo());
    return pfBsDetailT.isPresent() ? pfBsDetailT.get() : null;
  }

  @Override
  public PfBsDetail insert(PfBsDetail pfBsDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + pfBsDetail.getLogNo());
    if (this.findById(pfBsDetail.getLogNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      pfBsDetail.setCreateEmpNo(empNot);

    if(pfBsDetail.getLastUpdateEmpNo() == null || pfBsDetail.getLastUpdateEmpNo().isEmpty())
      pfBsDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfBsDetailReposDay.saveAndFlush(pfBsDetail);	
    else if (dbName.equals(ContentName.onMon))
      return pfBsDetailReposMon.saveAndFlush(pfBsDetail);
    else if (dbName.equals(ContentName.onHist))
      return pfBsDetailReposHist.saveAndFlush(pfBsDetail);
    else 
    return pfBsDetailRepos.saveAndFlush(pfBsDetail);
  }

  @Override
  public PfBsDetail update(PfBsDetail pfBsDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfBsDetail.getLogNo());
    if (!empNot.isEmpty())
      pfBsDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfBsDetailReposDay.saveAndFlush(pfBsDetail);	
    else if (dbName.equals(ContentName.onMon))
      return pfBsDetailReposMon.saveAndFlush(pfBsDetail);
    else if (dbName.equals(ContentName.onHist))
      return pfBsDetailReposHist.saveAndFlush(pfBsDetail);
    else 
    return pfBsDetailRepos.saveAndFlush(pfBsDetail);
  }

  @Override
  public PfBsDetail update2(PfBsDetail pfBsDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfBsDetail.getLogNo());
    if (!empNot.isEmpty())
      pfBsDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      pfBsDetailReposDay.saveAndFlush(pfBsDetail);	
    else if (dbName.equals(ContentName.onMon))
      pfBsDetailReposMon.saveAndFlush(pfBsDetail);
    else if (dbName.equals(ContentName.onHist))
        pfBsDetailReposHist.saveAndFlush(pfBsDetail);
    else 
      pfBsDetailRepos.saveAndFlush(pfBsDetail);	
    return this.findById(pfBsDetail.getLogNo());
  }

  @Override
  public void delete(PfBsDetail pfBsDetail, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + pfBsDetail.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      pfBsDetailReposDay.delete(pfBsDetail);	
      pfBsDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfBsDetailReposMon.delete(pfBsDetail);	
      pfBsDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfBsDetailReposHist.delete(pfBsDetail);
      pfBsDetailReposHist.flush();
    }
    else {
      pfBsDetailRepos.delete(pfBsDetail);
      pfBsDetailRepos.flush();
    }
   }

  @Override
  public void insertAll(List<PfBsDetail> pfBsDetail, TitaVo... titaVo) throws DBException {
    if (pfBsDetail == null || pfBsDetail.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (PfBsDetail t : pfBsDetail){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      pfBsDetail = pfBsDetailReposDay.saveAll(pfBsDetail);	
      pfBsDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfBsDetail = pfBsDetailReposMon.saveAll(pfBsDetail);	
      pfBsDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfBsDetail = pfBsDetailReposHist.saveAll(pfBsDetail);
      pfBsDetailReposHist.flush();
    }
    else {
      pfBsDetail = pfBsDetailRepos.saveAll(pfBsDetail);
      pfBsDetailRepos.flush();
    }
    }

  @Override
  public void updateAll(List<PfBsDetail> pfBsDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (pfBsDetail == null || pfBsDetail.size() == 0)
      throw new DBException(6);

    for (PfBsDetail t : pfBsDetail) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      pfBsDetail = pfBsDetailReposDay.saveAll(pfBsDetail);	
      pfBsDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfBsDetail = pfBsDetailReposMon.saveAll(pfBsDetail);	
      pfBsDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfBsDetail = pfBsDetailReposHist.saveAll(pfBsDetail);
      pfBsDetailReposHist.flush();
    }
    else {
      pfBsDetail = pfBsDetailRepos.saveAll(pfBsDetail);
      pfBsDetailRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<PfBsDetail> pfBsDetail, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (pfBsDetail == null || pfBsDetail.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      pfBsDetailReposDay.deleteAll(pfBsDetail);	
      pfBsDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfBsDetailReposMon.deleteAll(pfBsDetail);	
      pfBsDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfBsDetailReposHist.deleteAll(pfBsDetail);
      pfBsDetailReposHist.flush();
    }
    else {
      pfBsDetailRepos.deleteAll(pfBsDetail);
      pfBsDetailRepos.flush();
    }
  }

}
