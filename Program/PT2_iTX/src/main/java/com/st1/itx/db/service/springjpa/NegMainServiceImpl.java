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
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;
import com.st1.itx.db.repository.online.NegMainRepository;
import com.st1.itx.db.repository.day.NegMainRepositoryDay;
import com.st1.itx.db.repository.mon.NegMainRepositoryMon;
import com.st1.itx.db.repository.hist.NegMainRepositoryHist;
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("negMainService")
@Repository
public class NegMainServiceImpl extends ASpringJpaParm implements NegMainService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private NegMainRepository negMainRepos;

  @Autowired
  private NegMainRepositoryDay negMainReposDay;

  @Autowired
  private NegMainRepositoryMon negMainReposMon;

  @Autowired
  private NegMainRepositoryHist negMainReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(negMainRepos);
    org.junit.Assert.assertNotNull(negMainReposDay);
    org.junit.Assert.assertNotNull(negMainReposMon);
    org.junit.Assert.assertNotNull(negMainReposHist);
  }

  @Override
  public NegMain findById(NegMainId negMainId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + negMainId);
    Optional<NegMain> negMain = null;
    if (dbName.equals(ContentName.onDay))
      negMain = negMainReposDay.findById(negMainId);
    else if (dbName.equals(ContentName.onMon))
      negMain = negMainReposMon.findById(negMainId);
    else if (dbName.equals(ContentName.onHist))
      negMain = negMainReposHist.findById(negMainId);
    else 
      negMain = negMainRepos.findById(negMainId);
    NegMain obj = negMain.isPresent() ? negMain.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<NegMain> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "CaseSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "CaseSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAll(pageable);
    else 
      slice = negMainRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegMain> haveCustNo(String caseKindCode_0, String custLoanKind_1, String status_2, int custNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("haveCustNo " + dbName + " : " + "caseKindCode_0 : " + caseKindCode_0 + " custLoanKind_1 : " +  custLoanKind_1 + " status_2 : " +  status_2 + " custNo_3 : " +  custNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAllByCaseKindCodeIsAndCustLoanKindIsAndStatusIsAndCustNoIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, custLoanKind_1, status_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAllByCaseKindCodeIsAndCustLoanKindIsAndStatusIsAndCustNoIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, custLoanKind_1, status_2, custNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAllByCaseKindCodeIsAndCustLoanKindIsAndStatusIsAndCustNoIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, custLoanKind_1, status_2, custNo_3, pageable);
    else 
      slice = negMainRepos.findAllByCaseKindCodeIsAndCustLoanKindIsAndStatusIsAndCustNoIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, custLoanKind_1, status_2, custNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegMain> noCustNo(String caseKindCode_0, String custLoanKind_1, String status_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("noCustNo " + dbName + " : " + "caseKindCode_0 : " + caseKindCode_0 + " custLoanKind_1 : " +  custLoanKind_1 + " status_2 : " +  status_2);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAllByCaseKindCodeIsAndCustLoanKindIsAndStatusIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, custLoanKind_1, status_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAllByCaseKindCodeIsAndCustLoanKindIsAndStatusIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, custLoanKind_1, status_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAllByCaseKindCodeIsAndCustLoanKindIsAndStatusIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, custLoanKind_1, status_2, pageable);
    else 
      slice = negMainRepos.findAllByCaseKindCodeIsAndCustLoanKindIsAndStatusIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, custLoanKind_1, status_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegMain> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAllByCustNoIsOrderByCustNoDescCaseSeqAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAllByCustNoIsOrderByCustNoDescCaseSeqAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAllByCustNoIsOrderByCustNoDescCaseSeqAsc(custNo_0, pageable);
    else 
      slice = negMainRepos.findAllByCustNoIsOrderByCustNoDescCaseSeqAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegMain> caseKindCodeEq(String caseKindCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("caseKindCodeEq " + dbName + " : " + "caseKindCode_0 : " + caseKindCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAllByCaseKindCodeIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAllByCaseKindCodeIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAllByCaseKindCodeIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, pageable);
    else 
      slice = negMainRepos.findAllByCaseKindCodeIsOrderByCustNoDescCaseSeqAsc(caseKindCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegMain> custLoanKindEq(String custLoanKind_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custLoanKindEq " + dbName + " : " + "custLoanKind_0 : " + custLoanKind_0);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAllByCustLoanKindIsOrderByCustNoDescCaseSeqAsc(custLoanKind_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAllByCustLoanKindIsOrderByCustNoDescCaseSeqAsc(custLoanKind_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAllByCustLoanKindIsOrderByCustNoDescCaseSeqAsc(custLoanKind_0, pageable);
    else 
      slice = negMainRepos.findAllByCustLoanKindIsOrderByCustNoDescCaseSeqAsc(custLoanKind_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegMain> statusEq(String status_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("statusEq " + dbName + " : " + "status_0 : " + status_0);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAllByStatusIsOrderByCustNoDescCaseSeqAsc(status_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAllByStatusIsOrderByCustNoDescCaseSeqAsc(status_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAllByStatusIsOrderByCustNoDescCaseSeqAsc(status_0, pageable);
    else 
      slice = negMainRepos.findAllByStatusIsOrderByCustNoDescCaseSeqAsc(status_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegMain custNoFirst(int custNo_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("custNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0);
    Optional<NegMain> negMainT = null;
    if (dbName.equals(ContentName.onDay))
      negMainT = negMainReposDay.findTopByCustNoIsOrderByCaseSeqDesc(custNo_0);
    else if (dbName.equals(ContentName.onMon))
      negMainT = negMainReposMon.findTopByCustNoIsOrderByCaseSeqDesc(custNo_0);
    else if (dbName.equals(ContentName.onHist))
      negMainT = negMainReposHist.findTopByCustNoIsOrderByCaseSeqDesc(custNo_0);
    else 
      negMainT = negMainRepos.findTopByCustNoIsOrderByCaseSeqDesc(custNo_0);

    return negMainT.isPresent() ? negMainT.get() : null;
  }

  @Override
  public NegMain statusFirst(String status_0, int custNo_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("statusFirst " + dbName + " : " + "status_0 : " + status_0 + " custNo_1 : " +  custNo_1);
    Optional<NegMain> negMainT = null;
    if (dbName.equals(ContentName.onDay))
      negMainT = negMainReposDay.findTopByStatusIsAndCustNoIsOrderByCaseSeqAsc(status_0, custNo_1);
    else if (dbName.equals(ContentName.onMon))
      negMainT = negMainReposMon.findTopByStatusIsAndCustNoIsOrderByCaseSeqAsc(status_0, custNo_1);
    else if (dbName.equals(ContentName.onHist))
      negMainT = negMainReposHist.findTopByStatusIsAndCustNoIsOrderByCaseSeqAsc(status_0, custNo_1);
    else 
      negMainT = negMainRepos.findTopByStatusIsAndCustNoIsOrderByCaseSeqAsc(status_0, custNo_1);

    return negMainT.isPresent() ? negMainT.get() : null;
  }

  @Override
  public Slice<NegMain> l5705HadCustId(List<String> status_0, String isMainFin_1, int nextPayDate_2, int nextPayDate_3, int custNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("l5705HadCustId " + dbName + " : " + "status_0 : " + status_0 + " isMainFin_1 : " +  isMainFin_1 + " nextPayDate_2 : " +  nextPayDate_2 + " nextPayDate_3 : " +  nextPayDate_3 + " custNo_4 : " +  custNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAllByStatusInAndIsMainFinIsAndNextPayDateGreaterThanEqualAndNextPayDateLessThanEqualAndCustNoIsOrderByCustNoDescCaseSeqAsc(status_0, isMainFin_1, nextPayDate_2, nextPayDate_3, custNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAllByStatusInAndIsMainFinIsAndNextPayDateGreaterThanEqualAndNextPayDateLessThanEqualAndCustNoIsOrderByCustNoDescCaseSeqAsc(status_0, isMainFin_1, nextPayDate_2, nextPayDate_3, custNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAllByStatusInAndIsMainFinIsAndNextPayDateGreaterThanEqualAndNextPayDateLessThanEqualAndCustNoIsOrderByCustNoDescCaseSeqAsc(status_0, isMainFin_1, nextPayDate_2, nextPayDate_3, custNo_4, pageable);
    else 
      slice = negMainRepos.findAllByStatusInAndIsMainFinIsAndNextPayDateGreaterThanEqualAndNextPayDateLessThanEqualAndCustNoIsOrderByCustNoDescCaseSeqAsc(status_0, isMainFin_1, nextPayDate_2, nextPayDate_3, custNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegMain> l5705NoCustId(List<String> status_0, String isMainFin_1, int nextPayDate_2, int nextPayDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("l5705NoCustId " + dbName + " : " + "status_0 : " + status_0 + " isMainFin_1 : " +  isMainFin_1 + " nextPayDate_2 : " +  nextPayDate_2 + " nextPayDate_3 : " +  nextPayDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAllByStatusInAndIsMainFinIsAndNextPayDateGreaterThanEqualAndNextPayDateLessThanEqualOrderByCustNoDescCaseSeqAsc(status_0, isMainFin_1, nextPayDate_2, nextPayDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAllByStatusInAndIsMainFinIsAndNextPayDateGreaterThanEqualAndNextPayDateLessThanEqualOrderByCustNoDescCaseSeqAsc(status_0, isMainFin_1, nextPayDate_2, nextPayDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAllByStatusInAndIsMainFinIsAndNextPayDateGreaterThanEqualAndNextPayDateLessThanEqualOrderByCustNoDescCaseSeqAsc(status_0, isMainFin_1, nextPayDate_2, nextPayDate_3, pageable);
    else 
      slice = negMainRepos.findAllByStatusInAndIsMainFinIsAndNextPayDateGreaterThanEqualAndNextPayDateLessThanEqualOrderByCustNoDescCaseSeqAsc(status_0, isMainFin_1, nextPayDate_2, nextPayDate_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegMain> custNoAndApplDate(int custNo_0, int applDate_1, String mainFinCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoAndApplDate " + dbName + " : " + "custNo_0 : " + custNo_0 + " applDate_1 : " +  applDate_1 + " mainFinCode_2 : " +  mainFinCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAllByCustNoIsAndApplDateIsAndMainFinCodeIsOrderByCustNoDescCaseSeqAsc(custNo_0, applDate_1, mainFinCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAllByCustNoIsAndApplDateIsAndMainFinCodeIsOrderByCustNoDescCaseSeqAsc(custNo_0, applDate_1, mainFinCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAllByCustNoIsAndApplDateIsAndMainFinCodeIsOrderByCustNoDescCaseSeqAsc(custNo_0, applDate_1, mainFinCode_2, pageable);
    else 
      slice = negMainRepos.findAllByCustNoIsAndApplDateIsAndMainFinCodeIsOrderByCustNoDescCaseSeqAsc(custNo_0, applDate_1, mainFinCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegMain custNoAndApplDateFirst(int custNo_0, int applDate_1, String mainFinCode_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("custNoAndApplDateFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " applDate_1 : " +  applDate_1 + " mainFinCode_2 : " +  mainFinCode_2);
    Optional<NegMain> negMainT = null;
    if (dbName.equals(ContentName.onDay))
      negMainT = negMainReposDay.findTopByCustNoIsAndApplDateIsAndMainFinCodeIsOrderByCustNoDescCaseSeqDesc(custNo_0, applDate_1, mainFinCode_2);
    else if (dbName.equals(ContentName.onMon))
      negMainT = negMainReposMon.findTopByCustNoIsAndApplDateIsAndMainFinCodeIsOrderByCustNoDescCaseSeqDesc(custNo_0, applDate_1, mainFinCode_2);
    else if (dbName.equals(ContentName.onHist))
      negMainT = negMainReposHist.findTopByCustNoIsAndApplDateIsAndMainFinCodeIsOrderByCustNoDescCaseSeqDesc(custNo_0, applDate_1, mainFinCode_2);
    else 
      negMainT = negMainRepos.findTopByCustNoIsAndApplDateIsAndMainFinCodeIsOrderByCustNoDescCaseSeqDesc(custNo_0, applDate_1, mainFinCode_2);

    return negMainT.isPresent() ? negMainT.get() : null;
  }

  @Override
  public Slice<NegMain> forLetter(int custNo_0, String caseKindCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("forLetter " + dbName + " : " + "custNo_0 : " + custNo_0 + " caseKindCode_1 : " +  caseKindCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAllByCustNoIsAndCaseKindCodeIsOrderByCaseSeqDesc(custNo_0, caseKindCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAllByCustNoIsAndCaseKindCodeIsOrderByCaseSeqDesc(custNo_0, caseKindCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAllByCustNoIsAndCaseKindCodeIsOrderByCaseSeqDesc(custNo_0, caseKindCode_1, pageable);
    else 
      slice = negMainRepos.findAllByCustNoIsAndCaseKindCodeIsOrderByCaseSeqDesc(custNo_0, caseKindCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegMain payerCustNoFirst(int payerCustNo_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("payerCustNoFirst " + dbName + " : " + "payerCustNo_0 : " + payerCustNo_0);
    Optional<NegMain> negMainT = null;
    if (dbName.equals(ContentName.onDay))
      negMainT = negMainReposDay.findTopByPayerCustNoIs(payerCustNo_0);
    else if (dbName.equals(ContentName.onMon))
      negMainT = negMainReposMon.findTopByPayerCustNoIs(payerCustNo_0);
    else if (dbName.equals(ContentName.onHist))
      negMainT = negMainReposHist.findTopByPayerCustNoIs(payerCustNo_0);
    else 
      negMainT = negMainRepos.findTopByPayerCustNoIs(payerCustNo_0);

    return negMainT.isPresent() ? negMainT.get() : null;
  }

  @Override
  public Slice<NegMain> negCustIdEq(String negCustId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("negCustIdEq " + dbName + " : " + "negCustId_0 : " + negCustId_0);
    if (dbName.equals(ContentName.onDay))
      slice = negMainReposDay.findAllByNegCustIdIsOrderByCustNoAsc(negCustId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negMainReposMon.findAllByNegCustIdIsOrderByCustNoAsc(negCustId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negMainReposHist.findAllByNegCustIdIsOrderByCustNoAsc(negCustId_0, pageable);
    else 
      slice = negMainRepos.findAllByNegCustIdIsOrderByCustNoAsc(negCustId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegMain negCustIdFirst(String negCustId_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("negCustIdFirst " + dbName + " : " + "negCustId_0 : " + negCustId_0);
    Optional<NegMain> negMainT = null;
    if (dbName.equals(ContentName.onDay))
      negMainT = negMainReposDay.findTopByNegCustIdIsOrderByCustNoDesc(negCustId_0);
    else if (dbName.equals(ContentName.onMon))
      negMainT = negMainReposMon.findTopByNegCustIdIsOrderByCustNoDesc(negCustId_0);
    else if (dbName.equals(ContentName.onHist))
      negMainT = negMainReposHist.findTopByNegCustIdIsOrderByCustNoDesc(negCustId_0);
    else 
      negMainT = negMainRepos.findTopByNegCustIdIsOrderByCustNoDesc(negCustId_0);

    return negMainT.isPresent() ? negMainT.get() : null;
  }

  @Override
  public NegMain holdById(NegMainId negMainId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + negMainId);
    Optional<NegMain> negMain = null;
    if (dbName.equals(ContentName.onDay))
      negMain = negMainReposDay.findByNegMainId(negMainId);
    else if (dbName.equals(ContentName.onMon))
      negMain = negMainReposMon.findByNegMainId(negMainId);
    else if (dbName.equals(ContentName.onHist))
      negMain = negMainReposHist.findByNegMainId(negMainId);
    else 
      negMain = negMainRepos.findByNegMainId(negMainId);
    return negMain.isPresent() ? negMain.get() : null;
  }

  @Override
  public NegMain holdById(NegMain negMain, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + negMain.getNegMainId());
    Optional<NegMain> negMainT = null;
    if (dbName.equals(ContentName.onDay))
      negMainT = negMainReposDay.findByNegMainId(negMain.getNegMainId());
    else if (dbName.equals(ContentName.onMon))
      negMainT = negMainReposMon.findByNegMainId(negMain.getNegMainId());
    else if (dbName.equals(ContentName.onHist))
      negMainT = negMainReposHist.findByNegMainId(negMain.getNegMainId());
    else 
      negMainT = negMainRepos.findByNegMainId(negMain.getNegMainId());
    return negMainT.isPresent() ? negMainT.get() : null;
  }

  @Override
  public NegMain insert(NegMain negMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + negMain.getNegMainId());
    if (this.findById(negMain.getNegMainId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      negMain.setCreateEmpNo(empNot);

    if(negMain.getLastUpdateEmpNo() == null || negMain.getLastUpdateEmpNo().isEmpty())
      negMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negMainReposDay.saveAndFlush(negMain);	
    else if (dbName.equals(ContentName.onMon))
      return negMainReposMon.saveAndFlush(negMain);
    else if (dbName.equals(ContentName.onHist))
      return negMainReposHist.saveAndFlush(negMain);
    else 
    return negMainRepos.saveAndFlush(negMain);
  }

  @Override
  public NegMain update(NegMain negMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + negMain.getNegMainId());
    if (!empNot.isEmpty())
      negMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negMainReposDay.saveAndFlush(negMain);	
    else if (dbName.equals(ContentName.onMon))
      return negMainReposMon.saveAndFlush(negMain);
    else if (dbName.equals(ContentName.onHist))
      return negMainReposHist.saveAndFlush(negMain);
    else 
    return negMainRepos.saveAndFlush(negMain);
  }

  @Override
  public NegMain update2(NegMain negMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + negMain.getNegMainId());
    if (!empNot.isEmpty())
      negMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      negMainReposDay.saveAndFlush(negMain);	
    else if (dbName.equals(ContentName.onMon))
      negMainReposMon.saveAndFlush(negMain);
    else if (dbName.equals(ContentName.onHist))
        negMainReposHist.saveAndFlush(negMain);
    else 
      negMainRepos.saveAndFlush(negMain);	
    return this.findById(negMain.getNegMainId());
  }

  @Override
  public void delete(NegMain negMain, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + negMain.getNegMainId());
    if (dbName.equals(ContentName.onDay)) {
      negMainReposDay.delete(negMain);	
      negMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negMainReposMon.delete(negMain);	
      negMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negMainReposHist.delete(negMain);
      negMainReposHist.flush();
    }
    else {
      negMainRepos.delete(negMain);
      negMainRepos.flush();
    }
   }

  @Override
  public void insertAll(List<NegMain> negMain, TitaVo... titaVo) throws DBException {
    if (negMain == null || negMain.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (NegMain t : negMain){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      negMain = negMainReposDay.saveAll(negMain);	
      negMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negMain = negMainReposMon.saveAll(negMain);	
      negMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negMain = negMainReposHist.saveAll(negMain);
      negMainReposHist.flush();
    }
    else {
      negMain = negMainRepos.saveAll(negMain);
      negMainRepos.flush();
    }
    }

  @Override
  public void updateAll(List<NegMain> negMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (negMain == null || negMain.size() == 0)
      throw new DBException(6);

    for (NegMain t : negMain) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      negMain = negMainReposDay.saveAll(negMain);	
      negMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negMain = negMainReposMon.saveAll(negMain);	
      negMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negMain = negMainReposHist.saveAll(negMain);
      negMainReposHist.flush();
    }
    else {
      negMain = negMainRepos.saveAll(negMain);
      negMainRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<NegMain> negMain, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (negMain == null || negMain.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      negMainReposDay.deleteAll(negMain);	
      negMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negMainReposMon.deleteAll(negMain);	
      negMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negMainReposHist.deleteAll(negMain);
      negMainReposHist.flush();
    }
    else {
      negMainRepos.deleteAll(negMain);
      negMainRepos.flush();
    }
  }

}
