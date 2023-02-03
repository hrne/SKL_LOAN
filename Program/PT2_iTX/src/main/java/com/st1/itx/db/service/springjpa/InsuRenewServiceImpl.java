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
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewId;
import com.st1.itx.db.repository.online.InsuRenewRepository;
import com.st1.itx.db.repository.day.InsuRenewRepositoryDay;
import com.st1.itx.db.repository.mon.InsuRenewRepositoryMon;
import com.st1.itx.db.repository.hist.InsuRenewRepositoryHist;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("insuRenewService")
@Repository
public class InsuRenewServiceImpl extends ASpringJpaParm implements InsuRenewService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private InsuRenewRepository insuRenewRepos;

  @Autowired
  private InsuRenewRepositoryDay insuRenewReposDay;

  @Autowired
  private InsuRenewRepositoryMon insuRenewReposMon;

  @Autowired
  private InsuRenewRepositoryHist insuRenewReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(insuRenewRepos);
    org.junit.Assert.assertNotNull(insuRenewReposDay);
    org.junit.Assert.assertNotNull(insuRenewReposMon);
    org.junit.Assert.assertNotNull(insuRenewReposHist);
  }

  @Override
  public InsuRenew findById(InsuRenewId insuRenewId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + insuRenewId);
    Optional<InsuRenew> insuRenew = null;
    if (dbName.equals(ContentName.onDay))
      insuRenew = insuRenewReposDay.findById(insuRenewId);
    else if (dbName.equals(ContentName.onMon))
      insuRenew = insuRenewReposMon.findById(insuRenewId);
    else if (dbName.equals(ContentName.onHist))
      insuRenew = insuRenewReposHist.findById(insuRenewId);
    else 
      insuRenew = insuRenewRepos.findById(insuRenewId);
    InsuRenew obj = insuRenew.isPresent() ? insuRenew.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<InsuRenew> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "PrevInsuNo", "EndoInsuNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "PrevInsuNo", "EndoInsuNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAll(pageable);
    else 
      slice = insuRenewRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectA(int acDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectA " + dbName + " : " + "acDate_0 : " + acDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByAcDateIsOrderByInsuEndDateDescInsuStartDateAsc(acDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByAcDateIsOrderByInsuEndDateDescInsuStartDateAsc(acDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByAcDateIsOrderByInsuEndDateDescInsuStartDateAsc(acDate_0, pageable);
    else 
      slice = insuRenewRepos.findAllByAcDateIsOrderByInsuEndDateDescInsuStartDateAsc(acDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectB(int acDate_0, int repayCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectB " + dbName + " : " + "acDate_0 : " + acDate_0 + " repayCode_1 : " +  repayCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByAcDateIsAndRepayCodeIsOrderByInsuEndDateDescInsuStartDateAsc(acDate_0, repayCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByAcDateIsAndRepayCodeIsOrderByInsuEndDateDescInsuStartDateAsc(acDate_0, repayCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByAcDateIsAndRepayCodeIsOrderByInsuEndDateDescInsuStartDateAsc(acDate_0, repayCode_1, pageable);
    else 
      slice = insuRenewRepos.findAllByAcDateIsAndRepayCodeIsOrderByInsuEndDateDescInsuStartDateAsc(acDate_0, repayCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectC(int insuYearMonth_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectC " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectD(int insuYearMonth_0, int repayCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectD " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " repayCode_1 : " +  repayCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndRepayCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndRepayCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndRepayCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndRepayCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectE(int insuYearMonth_0, int acDate_1, int statusCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectE " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " acDate_1 : " +  acDate_1 + " statusCode_2 : " +  statusCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndAcDateIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, acDate_1, statusCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndAcDateIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, acDate_1, statusCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndAcDateIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, acDate_1, statusCode_2, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndAcDateIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, acDate_1, statusCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectF(int insuYearMonth_0, int acDate_1, int statusCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectF " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " acDate_1 : " +  acDate_1 + " statusCode_2 : " +  statusCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndAcDateGreaterThanAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, acDate_1, statusCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndAcDateGreaterThanAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, acDate_1, statusCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndAcDateGreaterThanAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, acDate_1, statusCode_2, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndAcDateGreaterThanAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, acDate_1, statusCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectG(int insuYearMonth_0, int statusCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectG " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " statusCode_1 : " +  statusCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, statusCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, statusCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, statusCode_1, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, statusCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectH(int insuYearMonth_0, int renewCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectH " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " renewCode_1 : " +  renewCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndRenewCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, renewCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndRenewCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, renewCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndRenewCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, renewCode_1, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndRenewCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, renewCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectI(int insuYearMonth_0, int repayCode_1, int acDate_2, int statusCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectI " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " repayCode_1 : " +  repayCode_1 + " acDate_2 : " +  acDate_2 + " statusCode_3 : " +  statusCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndRepayCodeIsAndAcDateIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, acDate_2, statusCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndRepayCodeIsAndAcDateIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, acDate_2, statusCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndRepayCodeIsAndAcDateIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, acDate_2, statusCode_3, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndRepayCodeIsAndAcDateIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, acDate_2, statusCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectJ(int insuYearMonth_0, int repayCode_1, int acDate_2, int statusCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectJ " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " repayCode_1 : " +  repayCode_1 + " acDate_2 : " +  acDate_2 + " statusCode_3 : " +  statusCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndRepayCodeIsAndAcDateGreaterThanAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, acDate_2, statusCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndRepayCodeIsAndAcDateGreaterThanAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, acDate_2, statusCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndRepayCodeIsAndAcDateGreaterThanAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, acDate_2, statusCode_3, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndRepayCodeIsAndAcDateGreaterThanAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, acDate_2, statusCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectK(int insuYearMonth_0, int repayCode_1, int statusCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectK " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " repayCode_1 : " +  repayCode_1 + " statusCode_2 : " +  statusCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndRepayCodeIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, statusCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndRepayCodeIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, statusCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndRepayCodeIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, statusCode_2, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndRepayCodeIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, statusCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> selectL(int insuYearMonth_0, int repayCode_1, int renewCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("selectL " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " repayCode_1 : " +  repayCode_1 + " renewCode_2 : " +  renewCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndRepayCodeIsAndRenewCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, renewCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndRepayCodeIsAndRenewCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, renewCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndRepayCodeIsAndRenewCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, renewCode_2, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndRepayCodeIsAndRenewCodeIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, repayCode_1, renewCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> findNowInsuEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findNowInsuEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, pageable);
    else 
      slice = insuRenewRepos.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> insuEndDateRange(int insuEndDate_0, int insuEndDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("insuEndDateRange " + dbName + " : " + "insuEndDate_0 : " + insuEndDate_0 + " insuEndDate_1 : " +  insuEndDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuEndDateGreaterThanEqualAndInsuEndDateLessThanEqualOrderByInsuEndDateDescOrigInsuNoAscEndoInsuNoAsc(insuEndDate_0, insuEndDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuEndDateGreaterThanEqualAndInsuEndDateLessThanEqualOrderByInsuEndDateDescOrigInsuNoAscEndoInsuNoAsc(insuEndDate_0, insuEndDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuEndDateGreaterThanEqualAndInsuEndDateLessThanEqualOrderByInsuEndDateDescOrigInsuNoAscEndoInsuNoAsc(insuEndDate_0, insuEndDate_1, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuEndDateGreaterThanEqualAndInsuEndDateLessThanEqualOrderByInsuEndDateDescOrigInsuNoAscEndoInsuNoAsc(insuEndDate_0, insuEndDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public InsuRenew prevInsuNoFirst(int custNo_0, int facmNo_1, String prevInsuNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("prevInsuNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " prevInsuNo_2 : " +  prevInsuNo_2);
    Optional<InsuRenew> insuRenewT = null;
    if (dbName.equals(ContentName.onDay))
      insuRenewT = insuRenewReposDay.findTopByCustNoIsAndFacmNoIsAndPrevInsuNoIsOrderByInsuEndDateDescInsuStartDateAscEndoInsuNoAsc(custNo_0, facmNo_1, prevInsuNo_2);
    else if (dbName.equals(ContentName.onMon))
      insuRenewT = insuRenewReposMon.findTopByCustNoIsAndFacmNoIsAndPrevInsuNoIsOrderByInsuEndDateDescInsuStartDateAscEndoInsuNoAsc(custNo_0, facmNo_1, prevInsuNo_2);
    else if (dbName.equals(ContentName.onHist))
      insuRenewT = insuRenewReposHist.findTopByCustNoIsAndFacmNoIsAndPrevInsuNoIsOrderByInsuEndDateDescInsuStartDateAscEndoInsuNoAsc(custNo_0, facmNo_1, prevInsuNo_2);
    else 
      insuRenewT = insuRenewRepos.findTopByCustNoIsAndFacmNoIsAndPrevInsuNoIsOrderByInsuEndDateDescInsuStartDateAscEndoInsuNoAsc(custNo_0, facmNo_1, prevInsuNo_2);

    return insuRenewT.isPresent() ? insuRenewT.get() : null;
  }

  @Override
  public Slice<InsuRenew> findL4601A(int insuYearMonth_0, int custNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4601A " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndCustNoIsAndFacmNoIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, custNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndCustNoIsAndFacmNoIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, custNo_1, facmNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndCustNoIsAndFacmNoIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, custNo_1, facmNo_2, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndCustNoIsAndFacmNoIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, custNo_1, facmNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> findL4601B(int insuYearMonth_0, int clCode1_1, int clCode2_2, int clNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4601B " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " clCode1_1 : " +  clCode1_1 + " clCode2_2 : " +  clCode2_2 + " clNo_3 : " +  clNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, clCode1_1, clCode2_2, clNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, clCode1_1, clCode2_2, clNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, clCode1_1, clCode2_2, clNo_3, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, clCode1_1, clCode2_2, clNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> findL4604A(int insuYearMonth_0, int renewCode_1, int acDate_2, int acDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4604A " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " renewCode_1 : " +  renewCode_1 + " acDate_2 : " +  acDate_2 + " acDate_3 : " +  acDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthIsAndRenewCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, renewCode_1, acDate_2, acDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthIsAndRenewCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, renewCode_1, acDate_2, acDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthIsAndRenewCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, renewCode_1, acDate_2, acDate_3, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthIsAndRenewCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, renewCode_1, acDate_2, acDate_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> findL4962A(int insuYearMonth_0, int insuYearMonth_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4962A " + dbName + " : " + "insuYearMonth_0 : " + insuYearMonth_0 + " insuYearMonth_1 : " +  insuYearMonth_1);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByInsuYearMonthGreaterThanEqualAndInsuYearMonthLessThanEqualOrderByCustNoAscInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, insuYearMonth_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByInsuYearMonthGreaterThanEqualAndInsuYearMonthLessThanEqualOrderByCustNoAscInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, insuYearMonth_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByInsuYearMonthGreaterThanEqualAndInsuYearMonthLessThanEqualOrderByCustNoAscInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, insuYearMonth_1, pageable);
    else 
      slice = insuRenewRepos.findAllByInsuYearMonthGreaterThanEqualAndInsuYearMonthLessThanEqualOrderByCustNoAscInsuEndDateDescInsuStartDateAsc(insuYearMonth_0, insuYearMonth_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public InsuRenew findL4600AFirst(int clCode1_0, int clCode2_1, int clNo_2, String nowInsuNo_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findL4600AFirst " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2 + " nowInsuNo_3 : " +  nowInsuNo_3);
    Optional<InsuRenew> insuRenewT = null;
    if (dbName.equals(ContentName.onDay))
      insuRenewT = insuRenewReposDay.findTopByClCode1IsAndClCode2IsAndClNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, nowInsuNo_3);
    else if (dbName.equals(ContentName.onMon))
      insuRenewT = insuRenewReposMon.findTopByClCode1IsAndClCode2IsAndClNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, nowInsuNo_3);
    else if (dbName.equals(ContentName.onHist))
      insuRenewT = insuRenewReposHist.findTopByClCode1IsAndClCode2IsAndClNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, nowInsuNo_3);
    else 
      insuRenewT = insuRenewRepos.findTopByClCode1IsAndClCode2IsAndClNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, nowInsuNo_3);

    return insuRenewT.isPresent() ? insuRenewT.get() : null;
  }

  @Override
  public Slice<InsuRenew> findPrevInsuNoEq(int clCode1_0, int clCode2_1, int clNo_2, String prevInsuNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findPrevInsuNoEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2 + " prevInsuNo_3 : " +  prevInsuNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsAndPrevInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, prevInsuNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsAndPrevInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, prevInsuNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsAndPrevInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, prevInsuNo_3, pageable);
    else 
      slice = insuRenewRepos.findAllByClCode1IsAndClCode2IsAndClNoIsAndPrevInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, prevInsuNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public InsuRenew findNotiTempFgFirst(String notiTempFg_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findNotiTempFgFirst " + dbName + " : " + "notiTempFg_0 : " + notiTempFg_0);
    Optional<InsuRenew> insuRenewT = null;
    if (dbName.equals(ContentName.onDay))
      insuRenewT = insuRenewReposDay.findTopByNotiTempFgIsOrderByInsuYearMonthDesc(notiTempFg_0);
    else if (dbName.equals(ContentName.onMon))
      insuRenewT = insuRenewReposMon.findTopByNotiTempFgIsOrderByInsuYearMonthDesc(notiTempFg_0);
    else if (dbName.equals(ContentName.onHist))
      insuRenewT = insuRenewReposHist.findTopByNotiTempFgIsOrderByInsuYearMonthDesc(notiTempFg_0);
    else 
      insuRenewT = insuRenewRepos.findTopByNotiTempFgIsOrderByInsuYearMonthDesc(notiTempFg_0);

    return insuRenewT.isPresent() ? insuRenewT.get() : null;
  }

  @Override
  public Slice<InsuRenew> findCustEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCustEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByCustNoIsOrderByInsuEndDateDescInsuStartDateAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByCustNoIsOrderByInsuEndDateDescInsuStartDateAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByCustNoIsOrderByInsuEndDateDescInsuStartDateAsc(custNo_0, pageable);
    else 
      slice = insuRenewRepos.findAllByCustNoIsOrderByInsuEndDateDescInsuStartDateAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<InsuRenew> findNowInsuNoEq(int clCode1_0, int clCode2_1, int clNo_2, String nowInsuNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<InsuRenew> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findNowInsuNoEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " +  clCode2_1 + " clNo_2 : " +  clNo_2 + " nowInsuNo_3 : " +  nowInsuNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = insuRenewReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, nowInsuNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = insuRenewReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, nowInsuNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = insuRenewReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, nowInsuNo_3, pageable);
    else 
      slice = insuRenewRepos.findAllByClCode1IsAndClCode2IsAndClNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(clCode1_0, clCode2_1, clNo_2, nowInsuNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public InsuRenew findNowInsuNoFirst(int custNo_0, int facmNo_1, String nowInsuNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findNowInsuNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " nowInsuNo_2 : " +  nowInsuNo_2);
    Optional<InsuRenew> insuRenewT = null;
    if (dbName.equals(ContentName.onDay))
      insuRenewT = insuRenewReposDay.findTopByCustNoIsAndFacmNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(custNo_0, facmNo_1, nowInsuNo_2);
    else if (dbName.equals(ContentName.onMon))
      insuRenewT = insuRenewReposMon.findTopByCustNoIsAndFacmNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(custNo_0, facmNo_1, nowInsuNo_2);
    else if (dbName.equals(ContentName.onHist))
      insuRenewT = insuRenewReposHist.findTopByCustNoIsAndFacmNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(custNo_0, facmNo_1, nowInsuNo_2);
    else 
      insuRenewT = insuRenewRepos.findTopByCustNoIsAndFacmNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(custNo_0, facmNo_1, nowInsuNo_2);

    return insuRenewT.isPresent() ? insuRenewT.get() : null;
  }

  @Override
  public InsuRenew findEndoInsuNoFirst(int custNo_0, int facmNo_1, String prevInsuNo_2, String endoInsuNo_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findEndoInsuNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " prevInsuNo_2 : " +  prevInsuNo_2 + " endoInsuNo_3 : " +  endoInsuNo_3);
    Optional<InsuRenew> insuRenewT = null;
    if (dbName.equals(ContentName.onDay))
      insuRenewT = insuRenewReposDay.findTopByCustNoIsAndFacmNoIsAndPrevInsuNoIsAndEndoInsuNoIsOrderByEndoInsuNoDesc(custNo_0, facmNo_1, prevInsuNo_2, endoInsuNo_3);
    else if (dbName.equals(ContentName.onMon))
      insuRenewT = insuRenewReposMon.findTopByCustNoIsAndFacmNoIsAndPrevInsuNoIsAndEndoInsuNoIsOrderByEndoInsuNoDesc(custNo_0, facmNo_1, prevInsuNo_2, endoInsuNo_3);
    else if (dbName.equals(ContentName.onHist))
      insuRenewT = insuRenewReposHist.findTopByCustNoIsAndFacmNoIsAndPrevInsuNoIsAndEndoInsuNoIsOrderByEndoInsuNoDesc(custNo_0, facmNo_1, prevInsuNo_2, endoInsuNo_3);
    else 
      insuRenewT = insuRenewRepos.findTopByCustNoIsAndFacmNoIsAndPrevInsuNoIsAndEndoInsuNoIsOrderByEndoInsuNoDesc(custNo_0, facmNo_1, prevInsuNo_2, endoInsuNo_3);

    return insuRenewT.isPresent() ? insuRenewT.get() : null;
  }

  @Override
  public InsuRenew holdById(InsuRenewId insuRenewId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + insuRenewId);
    Optional<InsuRenew> insuRenew = null;
    if (dbName.equals(ContentName.onDay))
      insuRenew = insuRenewReposDay.findByInsuRenewId(insuRenewId);
    else if (dbName.equals(ContentName.onMon))
      insuRenew = insuRenewReposMon.findByInsuRenewId(insuRenewId);
    else if (dbName.equals(ContentName.onHist))
      insuRenew = insuRenewReposHist.findByInsuRenewId(insuRenewId);
    else 
      insuRenew = insuRenewRepos.findByInsuRenewId(insuRenewId);
    return insuRenew.isPresent() ? insuRenew.get() : null;
  }

  @Override
  public InsuRenew holdById(InsuRenew insuRenew, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + insuRenew.getInsuRenewId());
    Optional<InsuRenew> insuRenewT = null;
    if (dbName.equals(ContentName.onDay))
      insuRenewT = insuRenewReposDay.findByInsuRenewId(insuRenew.getInsuRenewId());
    else if (dbName.equals(ContentName.onMon))
      insuRenewT = insuRenewReposMon.findByInsuRenewId(insuRenew.getInsuRenewId());
    else if (dbName.equals(ContentName.onHist))
      insuRenewT = insuRenewReposHist.findByInsuRenewId(insuRenew.getInsuRenewId());
    else 
      insuRenewT = insuRenewRepos.findByInsuRenewId(insuRenew.getInsuRenewId());
    return insuRenewT.isPresent() ? insuRenewT.get() : null;
  }

  @Override
  public InsuRenew insert(InsuRenew insuRenew, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + insuRenew.getInsuRenewId());
    if (this.findById(insuRenew.getInsuRenewId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      insuRenew.setCreateEmpNo(empNot);

    if(insuRenew.getLastUpdateEmpNo() == null || insuRenew.getLastUpdateEmpNo().isEmpty())
      insuRenew.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return insuRenewReposDay.saveAndFlush(insuRenew);	
    else if (dbName.equals(ContentName.onMon))
      return insuRenewReposMon.saveAndFlush(insuRenew);
    else if (dbName.equals(ContentName.onHist))
      return insuRenewReposHist.saveAndFlush(insuRenew);
    else 
    return insuRenewRepos.saveAndFlush(insuRenew);
  }

  @Override
  public InsuRenew update(InsuRenew insuRenew, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + insuRenew.getInsuRenewId());
    if (!empNot.isEmpty())
      insuRenew.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return insuRenewReposDay.saveAndFlush(insuRenew);	
    else if (dbName.equals(ContentName.onMon))
      return insuRenewReposMon.saveAndFlush(insuRenew);
    else if (dbName.equals(ContentName.onHist))
      return insuRenewReposHist.saveAndFlush(insuRenew);
    else 
    return insuRenewRepos.saveAndFlush(insuRenew);
  }

  @Override
  public InsuRenew update2(InsuRenew insuRenew, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + insuRenew.getInsuRenewId());
    if (!empNot.isEmpty())
      insuRenew.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      insuRenewReposDay.saveAndFlush(insuRenew);	
    else if (dbName.equals(ContentName.onMon))
      insuRenewReposMon.saveAndFlush(insuRenew);
    else if (dbName.equals(ContentName.onHist))
        insuRenewReposHist.saveAndFlush(insuRenew);
    else 
      insuRenewRepos.saveAndFlush(insuRenew);	
    return this.findById(insuRenew.getInsuRenewId());
  }

  @Override
  public void delete(InsuRenew insuRenew, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + insuRenew.getInsuRenewId());
    if (dbName.equals(ContentName.onDay)) {
      insuRenewReposDay.delete(insuRenew);	
      insuRenewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      insuRenewReposMon.delete(insuRenew);	
      insuRenewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      insuRenewReposHist.delete(insuRenew);
      insuRenewReposHist.flush();
    }
    else {
      insuRenewRepos.delete(insuRenew);
      insuRenewRepos.flush();
    }
   }

  @Override
  public void insertAll(List<InsuRenew> insuRenew, TitaVo... titaVo) throws DBException {
    if (insuRenew == null || insuRenew.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (InsuRenew t : insuRenew){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      insuRenew = insuRenewReposDay.saveAll(insuRenew);	
      insuRenewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      insuRenew = insuRenewReposMon.saveAll(insuRenew);	
      insuRenewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      insuRenew = insuRenewReposHist.saveAll(insuRenew);
      insuRenewReposHist.flush();
    }
    else {
      insuRenew = insuRenewRepos.saveAll(insuRenew);
      insuRenewRepos.flush();
    }
    }

  @Override
  public void updateAll(List<InsuRenew> insuRenew, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (insuRenew == null || insuRenew.size() == 0)
      throw new DBException(6);

    for (InsuRenew t : insuRenew) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      insuRenew = insuRenewReposDay.saveAll(insuRenew);	
      insuRenewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      insuRenew = insuRenewReposMon.saveAll(insuRenew);	
      insuRenewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      insuRenew = insuRenewReposHist.saveAll(insuRenew);
      insuRenewReposHist.flush();
    }
    else {
      insuRenew = insuRenewRepos.saveAll(insuRenew);
      insuRenewRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<InsuRenew> insuRenew, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (insuRenew == null || insuRenew.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      insuRenewReposDay.deleteAll(insuRenew);	
      insuRenewReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      insuRenewReposMon.deleteAll(insuRenew);	
      insuRenewReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      insuRenewReposHist.deleteAll(insuRenew);
      insuRenewReposHist.flush();
    }
    else {
      insuRenewRepos.deleteAll(insuRenew);
      insuRenewRepos.flush();
    }
  }

}
