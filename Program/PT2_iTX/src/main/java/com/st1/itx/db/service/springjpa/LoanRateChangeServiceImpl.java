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
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.domain.LoanRateChangeId;
import com.st1.itx.db.repository.online.LoanRateChangeRepository;
import com.st1.itx.db.repository.day.LoanRateChangeRepositoryDay;
import com.st1.itx.db.repository.mon.LoanRateChangeRepositoryMon;
import com.st1.itx.db.repository.hist.LoanRateChangeRepositoryHist;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanRateChangeService")
@Repository
public class LoanRateChangeServiceImpl extends ASpringJpaParm implements LoanRateChangeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LoanRateChangeRepository loanRateChangeRepos;

  @Autowired
  private LoanRateChangeRepositoryDay loanRateChangeReposDay;

  @Autowired
  private LoanRateChangeRepositoryMon loanRateChangeReposMon;

  @Autowired
  private LoanRateChangeRepositoryHist loanRateChangeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(loanRateChangeRepos);
    org.junit.Assert.assertNotNull(loanRateChangeReposDay);
    org.junit.Assert.assertNotNull(loanRateChangeReposMon);
    org.junit.Assert.assertNotNull(loanRateChangeReposHist);
  }

  @Override
  public LoanRateChange findById(LoanRateChangeId loanRateChangeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + loanRateChangeId);
    Optional<LoanRateChange> loanRateChange = null;
    if (dbName.equals(ContentName.onDay))
      loanRateChange = loanRateChangeReposDay.findById(loanRateChangeId);
    else if (dbName.equals(ContentName.onMon))
      loanRateChange = loanRateChangeReposMon.findById(loanRateChangeId);
    else if (dbName.equals(ContentName.onHist))
      loanRateChange = loanRateChangeReposHist.findById(loanRateChangeId);
    else 
      loanRateChange = loanRateChangeRepos.findById(loanRateChangeId);
    LoanRateChange obj = loanRateChange.isPresent() ? loanRateChange.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LoanRateChange> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "BormNo", "EffectDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "BormNo", "EffectDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = loanRateChangeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanRateChangeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanRateChangeReposHist.findAll(pageable);
    else 
      slice = loanRateChangeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanRateChange> rateChangeTxtNoEq(int acDate_0, String tellerNo_1, String txtNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rateChangeTxtNoEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " tellerNo_1 : " +  tellerNo_1 + " txtNo_2 : " +  txtNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = loanRateChangeReposDay.findAllByAcDateIsAndTellerNoIsAndTxtNoIs(acDate_0, tellerNo_1, txtNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanRateChangeReposMon.findAllByAcDateIsAndTellerNoIsAndTxtNoIs(acDate_0, tellerNo_1, txtNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanRateChangeReposHist.findAllByAcDateIsAndTellerNoIsAndTxtNoIs(acDate_0, tellerNo_1, txtNo_2, pageable);
    else 
      slice = loanRateChangeRepos.findAllByAcDateIsAndTellerNoIsAndTxtNoIs(acDate_0, tellerNo_1, txtNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanRateChange rateChangeEffectDateDescFirst(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("rateChangeEffectDateDescFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " effectDate_3 : " +  effectDate_3);
    Optional<LoanRateChange> loanRateChangeT = null;
    if (dbName.equals(ContentName.onDay))
      loanRateChangeT = loanRateChangeReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateLessThanEqualOrderByEffectDateDesc(custNo_0, facmNo_1, bormNo_2, effectDate_3);
    else if (dbName.equals(ContentName.onMon))
      loanRateChangeT = loanRateChangeReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateLessThanEqualOrderByEffectDateDesc(custNo_0, facmNo_1, bormNo_2, effectDate_3);
    else if (dbName.equals(ContentName.onHist))
      loanRateChangeT = loanRateChangeReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateLessThanEqualOrderByEffectDateDesc(custNo_0, facmNo_1, bormNo_2, effectDate_3);
    else 
      loanRateChangeT = loanRateChangeRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateLessThanEqualOrderByEffectDateDesc(custNo_0, facmNo_1, bormNo_2, effectDate_3);

    return loanRateChangeT.isPresent() ? loanRateChangeT.get() : null;
  }

  @Override
  public LoanRateChange rateChangeEffectDateAscFirst(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("rateChangeEffectDateAscFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " effectDate_3 : " +  effectDate_3);
    Optional<LoanRateChange> loanRateChangeT = null;
    if (dbName.equals(ContentName.onDay))
      loanRateChangeT = loanRateChangeReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(custNo_0, facmNo_1, bormNo_2, effectDate_3);
    else if (dbName.equals(ContentName.onMon))
      loanRateChangeT = loanRateChangeReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(custNo_0, facmNo_1, bormNo_2, effectDate_3);
    else if (dbName.equals(ContentName.onHist))
      loanRateChangeT = loanRateChangeReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(custNo_0, facmNo_1, bormNo_2, effectDate_3);
    else 
      loanRateChangeT = loanRateChangeRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(custNo_0, facmNo_1, bormNo_2, effectDate_3);

    return loanRateChangeT.isPresent() ? loanRateChangeT.get() : null;
  }

  @Override
  public Slice<LoanRateChange> rateChangeBormNoEq(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rateChangeBormNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " effectDate_3 : " +  effectDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = loanRateChangeReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(custNo_0, facmNo_1, bormNo_2, effectDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanRateChangeReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(custNo_0, facmNo_1, bormNo_2, effectDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanRateChangeReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(custNo_0, facmNo_1, bormNo_2, effectDate_3, pageable);
    else 
      slice = loanRateChangeRepos.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(custNo_0, facmNo_1, bormNo_2, effectDate_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanRateChange rateChangeEffectDateFirst(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("rateChangeEffectDateFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " effectDate_3 : " +  effectDate_3);
    Optional<LoanRateChange> loanRateChangeT = null;
    if (dbName.equals(ContentName.onDay))
      loanRateChangeT = loanRateChangeReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateIs(custNo_0, facmNo_1, bormNo_2, effectDate_3);
    else if (dbName.equals(ContentName.onMon))
      loanRateChangeT = loanRateChangeReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateIs(custNo_0, facmNo_1, bormNo_2, effectDate_3);
    else if (dbName.equals(ContentName.onHist))
      loanRateChangeT = loanRateChangeReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateIs(custNo_0, facmNo_1, bormNo_2, effectDate_3);
    else 
      loanRateChangeT = loanRateChangeRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateIs(custNo_0, facmNo_1, bormNo_2, effectDate_3);

    return loanRateChangeT.isPresent() ? loanRateChangeT.get() : null;
  }

  @Override
  public Slice<LoanRateChange> rateChangeEffectDateRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int effectDate_5, int effectDate_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rateChangeEffectDateRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2 + " bormNo_3 : " +  bormNo_3 + " bormNo_4 : " +  bormNo_4 + " effectDate_5 : " +  effectDate_5 + " effectDate_6 : " +  effectDate_6);
    if (dbName.equals(ContentName.onDay))
      slice = loanRateChangeReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, effectDate_5, effectDate_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanRateChangeReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, effectDate_5, effectDate_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanRateChangeReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, effectDate_5, effectDate_6, pageable);
    else 
      slice = loanRateChangeRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, effectDate_5, effectDate_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LoanRateChange> rateChangeFacmNoRange(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int effectDate_5, int effectDate_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LoanRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rateChangeFacmNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " facmNo_2 : " +  facmNo_2 + " bormNo_3 : " +  bormNo_3 + " bormNo_4 : " +  bormNo_4 + " effectDate_5 : " +  effectDate_5 + " effectDate_6 : " +  effectDate_6);
    if (dbName.equals(ContentName.onDay))
      slice = loanRateChangeReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByFacmNoAscBormNoAscEffectDateAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, effectDate_5, effectDate_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = loanRateChangeReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByFacmNoAscBormNoAscEffectDateAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, effectDate_5, effectDate_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = loanRateChangeReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByFacmNoAscBormNoAscEffectDateAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, effectDate_5, effectDate_6, pageable);
    else 
      slice = loanRateChangeRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByFacmNoAscBormNoAscEffectDateAsc(custNo_0, facmNo_1, facmNo_2, bormNo_3, bormNo_4, effectDate_5, effectDate_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LoanRateChange holdById(LoanRateChangeId loanRateChangeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanRateChangeId);
    Optional<LoanRateChange> loanRateChange = null;
    if (dbName.equals(ContentName.onDay))
      loanRateChange = loanRateChangeReposDay.findByLoanRateChangeId(loanRateChangeId);
    else if (dbName.equals(ContentName.onMon))
      loanRateChange = loanRateChangeReposMon.findByLoanRateChangeId(loanRateChangeId);
    else if (dbName.equals(ContentName.onHist))
      loanRateChange = loanRateChangeReposHist.findByLoanRateChangeId(loanRateChangeId);
    else 
      loanRateChange = loanRateChangeRepos.findByLoanRateChangeId(loanRateChangeId);
    return loanRateChange.isPresent() ? loanRateChange.get() : null;
  }

  @Override
  public LoanRateChange holdById(LoanRateChange loanRateChange, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + loanRateChange.getLoanRateChangeId());
    Optional<LoanRateChange> loanRateChangeT = null;
    if (dbName.equals(ContentName.onDay))
      loanRateChangeT = loanRateChangeReposDay.findByLoanRateChangeId(loanRateChange.getLoanRateChangeId());
    else if (dbName.equals(ContentName.onMon))
      loanRateChangeT = loanRateChangeReposMon.findByLoanRateChangeId(loanRateChange.getLoanRateChangeId());
    else if (dbName.equals(ContentName.onHist))
      loanRateChangeT = loanRateChangeReposHist.findByLoanRateChangeId(loanRateChange.getLoanRateChangeId());
    else 
      loanRateChangeT = loanRateChangeRepos.findByLoanRateChangeId(loanRateChange.getLoanRateChangeId());
    return loanRateChangeT.isPresent() ? loanRateChangeT.get() : null;
  }

  @Override
  public LoanRateChange insert(LoanRateChange loanRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + loanRateChange.getLoanRateChangeId());
    if (this.findById(loanRateChange.getLoanRateChangeId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      loanRateChange.setCreateEmpNo(empNot);

    if(loanRateChange.getLastUpdateEmpNo() == null || loanRateChange.getLastUpdateEmpNo().isEmpty())
      loanRateChange.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanRateChangeReposDay.saveAndFlush(loanRateChange);	
    else if (dbName.equals(ContentName.onMon))
      return loanRateChangeReposMon.saveAndFlush(loanRateChange);
    else if (dbName.equals(ContentName.onHist))
      return loanRateChangeReposHist.saveAndFlush(loanRateChange);
    else 
    return loanRateChangeRepos.saveAndFlush(loanRateChange);
  }

  @Override
  public LoanRateChange update(LoanRateChange loanRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanRateChange.getLoanRateChangeId());
    if (!empNot.isEmpty())
      loanRateChange.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return loanRateChangeReposDay.saveAndFlush(loanRateChange);	
    else if (dbName.equals(ContentName.onMon))
      return loanRateChangeReposMon.saveAndFlush(loanRateChange);
    else if (dbName.equals(ContentName.onHist))
      return loanRateChangeReposHist.saveAndFlush(loanRateChange);
    else 
    return loanRateChangeRepos.saveAndFlush(loanRateChange);
  }

  @Override
  public LoanRateChange update2(LoanRateChange loanRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + loanRateChange.getLoanRateChangeId());
    if (!empNot.isEmpty())
      loanRateChange.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      loanRateChangeReposDay.saveAndFlush(loanRateChange);	
    else if (dbName.equals(ContentName.onMon))
      loanRateChangeReposMon.saveAndFlush(loanRateChange);
    else if (dbName.equals(ContentName.onHist))
        loanRateChangeReposHist.saveAndFlush(loanRateChange);
    else 
      loanRateChangeRepos.saveAndFlush(loanRateChange);	
    return this.findById(loanRateChange.getLoanRateChangeId());
  }

  @Override
  public void delete(LoanRateChange loanRateChange, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + loanRateChange.getLoanRateChangeId());
    if (dbName.equals(ContentName.onDay)) {
      loanRateChangeReposDay.delete(loanRateChange);	
      loanRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanRateChangeReposMon.delete(loanRateChange);	
      loanRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanRateChangeReposHist.delete(loanRateChange);
      loanRateChangeReposHist.flush();
    }
    else {
      loanRateChangeRepos.delete(loanRateChange);
      loanRateChangeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LoanRateChange> loanRateChange, TitaVo... titaVo) throws DBException {
    if (loanRateChange == null || loanRateChange.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LoanRateChange t : loanRateChange){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      loanRateChange = loanRateChangeReposDay.saveAll(loanRateChange);	
      loanRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanRateChange = loanRateChangeReposMon.saveAll(loanRateChange);	
      loanRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanRateChange = loanRateChangeReposHist.saveAll(loanRateChange);
      loanRateChangeReposHist.flush();
    }
    else {
      loanRateChange = loanRateChangeRepos.saveAll(loanRateChange);
      loanRateChangeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LoanRateChange> loanRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (loanRateChange == null || loanRateChange.size() == 0)
      throw new DBException(6);

    for (LoanRateChange t : loanRateChange) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      loanRateChange = loanRateChangeReposDay.saveAll(loanRateChange);	
      loanRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanRateChange = loanRateChangeReposMon.saveAll(loanRateChange);	
      loanRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanRateChange = loanRateChangeReposHist.saveAll(loanRateChange);
      loanRateChangeReposHist.flush();
    }
    else {
      loanRateChange = loanRateChangeRepos.saveAll(loanRateChange);
      loanRateChangeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LoanRateChange> loanRateChange, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (loanRateChange == null || loanRateChange.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      loanRateChangeReposDay.deleteAll(loanRateChange);	
      loanRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      loanRateChangeReposMon.deleteAll(loanRateChange);	
      loanRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      loanRateChangeReposHist.deleteAll(loanRateChange);
      loanRateChangeReposHist.flush();
    }
    else {
      loanRateChangeRepos.deleteAll(loanRateChange);
      loanRateChangeRepos.flush();
    }
  }

}
