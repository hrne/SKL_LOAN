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
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.repository.day.LoanBorMainRepositoryDay;
import com.st1.itx.db.repository.mon.LoanBorMainRepositoryMon;
import com.st1.itx.db.repository.hist.LoanBorMainRepositoryHist;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanBorMainService")
@Repository
public class LoanBorMainServiceImpl extends ASpringJpaParm implements LoanBorMainService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanBorMainRepository loanBorMainRepos;

  @Autowired
  private LoanBorMainRepositoryDay loanBorMainReposDay;

  @Autowired
  private LoanBorMainRepositoryMon loanBorMainReposMon;

  @Autowired
  private LoanBorMainRepositoryHist loanBorMainReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanBorMainRepos);
    org.junit.Assert.assertNotNull(loanBorMainReposDay);
    org.junit.Assert.assertNotNull(loanBorMainReposMon);
    org.junit.Assert.assertNotNull(loanBorMainReposHist);
  }

  @Override
  public LoanBorMain findById(LoanBorMainId loanBorMainId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanBorMainId);
    Optional<LoanBorMain> loanBorMain = null;
    if (dbName.equals(ContentName.onDay))
      loanBorMain = loanBorMainReposDay.findById(loanBorMainId);
    else if (dbName.equals(ContentName.onMon))
      loanBorMain = loanBorMainReposMon.findById(loanBorMainId);
    else if (dbName.equals(ContentName.onHist))
      loanBorMain = loanBorMainReposHist.findById(loanBorMainId);
    else 
      loanBorMain = loanBorMainRepos.findById(loanBorMainId);
    LoanBorMain obj = loanBorMain.isPresent() ? loanBorMain.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanBorMain> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorMainReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorMainReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorMainReposHist.findAll(pageable);
    else 
      slice = loanBorMainRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorMain> bormCustNoEq(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("bormCustNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2 + " bormNo_3 : " +  bormNo_3 + " bormNo_4 : " +  bormNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorMainReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorMainReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorMainReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, pageable);
    else 
      slice = loanBorMainRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorMain> bormFacmNoIn(int custNo_0, List<Integer> facmNo_1, int bormNo_2, int bormNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("bormFacmNoIn " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " bormNo_3 : " +  bormNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorMainReposDay.findAllByCustNoIsAndFacmNoInAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorMainReposMon.findAllByCustNoIsAndFacmNoInAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorMainReposHist.findAllByCustNoIsAndFacmNoInAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);
    else 
      slice = loanBorMainRepos.findAllByCustNoIsAndFacmNoInAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorMain> bormDrawdownDateRange(int drawdownDate_0, int drawdownDate_1, int bormNo_2, int bormNo_3, List<Integer> status_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("bormDrawdownDateRange " + dbName + " : " + "drawdownDate_0 : " + drawdownDate_0 + " drawdownDate_1 : " +  drawdownDate_1 + " bormNo_2 : " +  bormNo_2 + " bormNo_3 : " +  bormNo_3 + " status_4 : " +  status_4);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorMainReposDay.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndStatusInOrderByDrawdownDateAscCustNoAscFacmNoAscBormNoAsc(drawdownDate_0, drawdownDate_1, bormNo_2, bormNo_3, status_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorMainReposMon.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndStatusInOrderByDrawdownDateAscCustNoAscFacmNoAscBormNoAsc(drawdownDate_0, drawdownDate_1, bormNo_2, bormNo_3, status_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorMainReposHist.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndStatusInOrderByDrawdownDateAscCustNoAscFacmNoAscBormNoAsc(drawdownDate_0, drawdownDate_1, bormNo_2, bormNo_3, status_4, pageable);
    else 
      slice = loanBorMainRepos.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndStatusInOrderByDrawdownDateAscCustNoAscFacmNoAscBormNoAsc(drawdownDate_0, drawdownDate_1, bormNo_2, bormNo_3, status_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorMain> nextPayIntDateRange(int nextPayIntDate_0, int nextPayIntDate_1, int status_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("nextPayIntDateRange " + dbName + " : " + "nextPayIntDate_0 : " + nextPayIntDate_0 + " nextPayIntDate_1 : " +  nextPayIntDate_1 + " status_2 : " +  status_2);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorMainReposDay.findAllByNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndStatusIsOrderByCustNoAscFacmNoAscBormNoAscNextPayIntDateAsc(nextPayIntDate_0, nextPayIntDate_1, status_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorMainReposMon.findAllByNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndStatusIsOrderByCustNoAscFacmNoAscBormNoAscNextPayIntDateAsc(nextPayIntDate_0, nextPayIntDate_1, status_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorMainReposHist.findAllByNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndStatusIsOrderByCustNoAscFacmNoAscBormNoAscNextPayIntDateAsc(nextPayIntDate_0, nextPayIntDate_1, status_2, pageable);
    else 
      slice = loanBorMainRepos.findAllByNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndStatusIsOrderByCustNoAscFacmNoAscBormNoAscNextPayIntDateAsc(nextPayIntDate_0, nextPayIntDate_1, status_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorMain> findStatusRange(List<Integer> status_0, int drawdownDate_1, int drawdownDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findStatusRange " + dbName + " : " + "status_0 : " + status_0 + " drawdownDate_1 : " +  drawdownDate_1 + " drawdownDate_2 : " +  drawdownDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorMainReposDay.findAllByStatusInAndDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(status_0, drawdownDate_1, drawdownDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorMainReposMon.findAllByStatusInAndDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(status_0, drawdownDate_1, drawdownDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorMainReposHist.findAllByStatusInAndDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(status_0, drawdownDate_1, drawdownDate_2, pageable);
    else 
      slice = loanBorMainRepos.findAllByStatusInAndDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(status_0, drawdownDate_1, drawdownDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorMain> AmortizedCodeEq(String amortizedCode_0, int status_1, int nextPayIntDate_2, int nextPayIntDate_3, int bormNo_4, int bormNo_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("AmortizedCodeEq " + dbName + " : " + "amortizedCode_0 : " + amortizedCode_0 + " status_1 : " +  status_1 + " nextPayIntDate_2 : " +  nextPayIntDate_2 + " nextPayIntDate_3 : " +  nextPayIntDate_3 + " bormNo_4 : " +  bormNo_4 + " bormNo_5 : " +  bormNo_5);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorMainReposDay.findAllByAmortizedCodeIsAndStatusIsAndNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAscNextPayIntDateAsc(amortizedCode_0, status_1, nextPayIntDate_2, nextPayIntDate_3, bormNo_4, bormNo_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorMainReposMon.findAllByAmortizedCodeIsAndStatusIsAndNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAscNextPayIntDateAsc(amortizedCode_0, status_1, nextPayIntDate_2, nextPayIntDate_3, bormNo_4, bormNo_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorMainReposHist.findAllByAmortizedCodeIsAndStatusIsAndNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAscNextPayIntDateAsc(amortizedCode_0, status_1, nextPayIntDate_2, nextPayIntDate_3, bormNo_4, bormNo_5, pageable);
    else 
      slice = loanBorMainRepos.findAllByAmortizedCodeIsAndStatusIsAndNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAscNextPayIntDateAsc(amortizedCode_0, status_1, nextPayIntDate_2, nextPayIntDate_3, bormNo_4, bormNo_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorMain> findStatusEq(List<Integer> status_0, int custNo_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findStatusEq " + dbName + " : " + "status_0 : " + status_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorMainReposDay.findAllByStatusInAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscBormNoAsc(status_0, custNo_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorMainReposMon.findAllByStatusInAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscBormNoAsc(status_0, custNo_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorMainReposHist.findAllByStatusInAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscBormNoAsc(status_0, custNo_1, facmNo_2, facmNo_3, pageable);
    else 
      slice = loanBorMainRepos.findAllByStatusInAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscBormNoAsc(status_0, custNo_1, facmNo_2, facmNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanBorMain> findByCustNoandFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanBorMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByCustNoandFacmNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = loanBorMainReposDay.findAllByCustNoIsAndFacmNoIsOrderByBormNoAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanBorMainReposMon.findAllByCustNoIsAndFacmNoIsOrderByBormNoAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanBorMainReposHist.findAllByCustNoIsAndFacmNoIsOrderByBormNoAsc(custNo_0, facmNo_1, pageable);
    else 
      slice = loanBorMainRepos.findAllByCustNoIsAndFacmNoIsOrderByBormNoAsc(custNo_0, facmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanBorMain holdById(LoanBorMainId loanBorMainId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanBorMainId);
    Optional<LoanBorMain> loanBorMain = null;
    if (dbName.equals(ContentName.onDay))
      loanBorMain = loanBorMainReposDay.findByLoanBorMainId(loanBorMainId);
    else if (dbName.equals(ContentName.onMon))
      loanBorMain = loanBorMainReposMon.findByLoanBorMainId(loanBorMainId);
    else if (dbName.equals(ContentName.onHist))
      loanBorMain = loanBorMainReposHist.findByLoanBorMainId(loanBorMainId);
    else 
      loanBorMain = loanBorMainRepos.findByLoanBorMainId(loanBorMainId);
    return loanBorMain.isPresent() ? loanBorMain.get() : null;
  }

  @Override
  public LoanBorMain holdById(LoanBorMain loanBorMain, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanBorMain.getLoanBorMainId());
    Optional<LoanBorMain> loanBorMainT = null;
    if (dbName.equals(ContentName.onDay))
      loanBorMainT = loanBorMainReposDay.findByLoanBorMainId(loanBorMain.getLoanBorMainId());
    else if (dbName.equals(ContentName.onMon))
      loanBorMainT = loanBorMainReposMon.findByLoanBorMainId(loanBorMain.getLoanBorMainId());
    else if (dbName.equals(ContentName.onHist))
      loanBorMainT = loanBorMainReposHist.findByLoanBorMainId(loanBorMain.getLoanBorMainId());
    else 
      loanBorMainT = loanBorMainRepos.findByLoanBorMainId(loanBorMain.getLoanBorMainId());
    return loanBorMainT.isPresent() ? loanBorMainT.get() : null;
  }

  @Override
  public LoanBorMain insert(LoanBorMain loanBorMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + loanBorMain.getLoanBorMainId());
    if (this.findById(loanBorMain.getLoanBorMainId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanBorMain.setCreateEmpNo(empNot);

    if(loanBorMain.getLastUpdateEmpNo() == null || loanBorMain.getLastUpdateEmpNo().isEmpty())
      loanBorMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanBorMainReposDay.saveAndFlush(loanBorMain);	
    else if (dbName.equals(ContentName.onMon))
      return loanBorMainReposMon.saveAndFlush(loanBorMain);
    else if (dbName.equals(ContentName.onHist))
      return loanBorMainReposHist.saveAndFlush(loanBorMain);
    else 
    return loanBorMainRepos.saveAndFlush(loanBorMain);
  }

  @Override
  public LoanBorMain update(LoanBorMain loanBorMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + loanBorMain.getLoanBorMainId());
    if (!empNot.isEmpty())
      loanBorMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanBorMainReposDay.saveAndFlush(loanBorMain);	
    else if (dbName.equals(ContentName.onMon))
      return loanBorMainReposMon.saveAndFlush(loanBorMain);
    else if (dbName.equals(ContentName.onHist))
      return loanBorMainReposHist.saveAndFlush(loanBorMain);
    else 
    return loanBorMainRepos.saveAndFlush(loanBorMain);
  }

  @Override
  public LoanBorMain update2(LoanBorMain loanBorMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + loanBorMain.getLoanBorMainId());
    if (!empNot.isEmpty())
      loanBorMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanBorMainReposDay.saveAndFlush(loanBorMain);	
    else if (dbName.equals(ContentName.onMon))
      loanBorMainReposMon.saveAndFlush(loanBorMain);
    else if (dbName.equals(ContentName.onHist))
        loanBorMainReposHist.saveAndFlush(loanBorMain);
    else 
      loanBorMainRepos.saveAndFlush(loanBorMain);	
    return this.findById(loanBorMain.getLoanBorMainId());
  }

  @Override
  public void delete(LoanBorMain loanBorMain, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanBorMain.getLoanBorMainId());
    if (dbName.equals(ContentName.onDay)) {
      loanBorMainReposDay.delete(loanBorMain);	
      loanBorMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBorMainReposMon.delete(loanBorMain);	
      loanBorMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBorMainReposHist.delete(loanBorMain);
      loanBorMainReposHist.flush();
    }
    else {
      loanBorMainRepos.delete(loanBorMain);
      loanBorMainRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanBorMain> loanBorMain, TitaVo... titaVo) throws DBException {
    if (loanBorMain == null || loanBorMain.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (LoanBorMain t : loanBorMain){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanBorMain = loanBorMainReposDay.saveAll(loanBorMain);	
      loanBorMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBorMain = loanBorMainReposMon.saveAll(loanBorMain);	
      loanBorMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBorMain = loanBorMainReposHist.saveAll(loanBorMain);
      loanBorMainReposHist.flush();
    }
    else {
      loanBorMain = loanBorMainRepos.saveAll(loanBorMain);
      loanBorMainRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanBorMain> loanBorMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (loanBorMain == null || loanBorMain.size() == 0)
      throw new DBException(6);

    for (LoanBorMain t : loanBorMain) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanBorMain = loanBorMainReposDay.saveAll(loanBorMain);	
      loanBorMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBorMain = loanBorMainReposMon.saveAll(loanBorMain);	
      loanBorMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBorMain = loanBorMainReposHist.saveAll(loanBorMain);
      loanBorMainReposHist.flush();
    }
    else {
      loanBorMain = loanBorMainRepos.saveAll(loanBorMain);
      loanBorMainRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanBorMain> loanBorMain, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanBorMain == null || loanBorMain.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanBorMainReposDay.deleteAll(loanBorMain);	
      loanBorMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanBorMainReposMon.deleteAll(loanBorMain);	
      loanBorMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanBorMainReposHist.deleteAll(loanBorMain);
      loanBorMainReposHist.flush();
    }
    else {
      loanBorMainRepos.deleteAll(loanBorMain);
      loanBorMainRepos.flush();
    }
  }

}
