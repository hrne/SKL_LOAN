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
import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.domain.BatxRateChangeId;
import com.st1.itx.db.repository.online.BatxRateChangeRepository;
import com.st1.itx.db.repository.day.BatxRateChangeRepositoryDay;
import com.st1.itx.db.repository.mon.BatxRateChangeRepositoryMon;
import com.st1.itx.db.repository.hist.BatxRateChangeRepositoryHist;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("batxRateChangeService")
@Repository
public class BatxRateChangeServiceImpl extends ASpringJpaParm implements BatxRateChangeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private BatxRateChangeRepository batxRateChangeRepos;

  @Autowired
  private BatxRateChangeRepositoryDay batxRateChangeReposDay;

  @Autowired
  private BatxRateChangeRepositoryMon batxRateChangeReposMon;

  @Autowired
  private BatxRateChangeRepositoryHist batxRateChangeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(batxRateChangeRepos);
    org.junit.Assert.assertNotNull(batxRateChangeReposDay);
    org.junit.Assert.assertNotNull(batxRateChangeReposMon);
    org.junit.Assert.assertNotNull(batxRateChangeReposHist);
  }

  @Override
  public BatxRateChange findById(BatxRateChangeId batxRateChangeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + batxRateChangeId);
    Optional<BatxRateChange> batxRateChange = null;
    if (dbName.equals(ContentName.onDay))
      batxRateChange = batxRateChangeReposDay.findById(batxRateChangeId);
    else if (dbName.equals(ContentName.onMon))
      batxRateChange = batxRateChangeReposMon.findById(batxRateChangeId);
    else if (dbName.equals(ContentName.onHist))
      batxRateChange = batxRateChangeReposHist.findById(batxRateChangeId);
    else 
      batxRateChange = batxRateChangeRepos.findById(batxRateChangeId);
    BatxRateChange obj = batxRateChange.isPresent() ? batxRateChange.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<BatxRateChange> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AdjDate", "CustNo", "FacmNo", "BormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AdjDate", "CustNo", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = batxRateChangeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxRateChangeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxRateChangeReposHist.findAll(pageable);
    else 
      slice = batxRateChangeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxRateChange> baseRateCodeEq(int adjDate_0, int adjDate_1, String baseRateCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("baseRateCodeEq " + dbName + " : " + "adjDate_0 : " + adjDate_0 + " adjDate_1 : " +  adjDate_1 + " baseRateCode_2 : " +  baseRateCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxRateChangeReposDay.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndBaseRateCodeIs(adjDate_0, adjDate_1, baseRateCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxRateChangeReposMon.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndBaseRateCodeIs(adjDate_0, adjDate_1, baseRateCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxRateChangeReposHist.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndBaseRateCodeIs(adjDate_0, adjDate_1, baseRateCode_2, pageable);
    else 
      slice = batxRateChangeRepos.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndBaseRateCodeIs(adjDate_0, adjDate_1, baseRateCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxRateChange> custCodeEq(int adjDate_0, int adjDate_1, int custCode_2, int custCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custCodeEq " + dbName + " : " + "adjDate_0 : " + adjDate_0 + " adjDate_1 : " +  adjDate_1 + " custCode_2 : " +  custCode_2 + " custCode_3 : " +  custCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxRateChangeReposDay.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndCustCodeGreaterThanEqualAndCustCodeLessThanEqual(adjDate_0, adjDate_1, custCode_2, custCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxRateChangeReposMon.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndCustCodeGreaterThanEqualAndCustCodeLessThanEqual(adjDate_0, adjDate_1, custCode_2, custCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxRateChangeReposHist.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndCustCodeGreaterThanEqualAndCustCodeLessThanEqual(adjDate_0, adjDate_1, custCode_2, custCode_3, pageable);
    else 
      slice = batxRateChangeRepos.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndCustCodeGreaterThanEqualAndCustCodeLessThanEqual(adjDate_0, adjDate_1, custCode_2, custCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxRateChange> findL4931AEq(int custCode_0, int custCode_1, int txKind_2, int txKind_3, int adjCode_4, int adjCode_5, int adjDate_6, int adjDate_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4931AEq " + dbName + " : " + "custCode_0 : " + custCode_0 + " custCode_1 : " +  custCode_1 + " txKind_2 : " +  txKind_2 + " txKind_3 : " +  txKind_3 + " adjCode_4 : " +  adjCode_4 + " adjCode_5 : " +  adjCode_5 + " adjDate_6 : " +  adjDate_6 + " adjDate_7 : " +  adjDate_7);
    if (dbName.equals(ContentName.onDay))
      slice = batxRateChangeReposDay.findAllByCustCodeGreaterThanEqualAndCustCodeLessThanEqualAndTxKindGreaterThanEqualAndTxKindLessThanEqualAndAdjCodeGreaterThanEqualAndAdjCodeLessThanEqualAndAdjDateGreaterThanEqualAndAdjDateLessThanEqual(custCode_0, custCode_1, txKind_2, txKind_3, adjCode_4, adjCode_5, adjDate_6, adjDate_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxRateChangeReposMon.findAllByCustCodeGreaterThanEqualAndCustCodeLessThanEqualAndTxKindGreaterThanEqualAndTxKindLessThanEqualAndAdjCodeGreaterThanEqualAndAdjCodeLessThanEqualAndAdjDateGreaterThanEqualAndAdjDateLessThanEqual(custCode_0, custCode_1, txKind_2, txKind_3, adjCode_4, adjCode_5, adjDate_6, adjDate_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxRateChangeReposHist.findAllByCustCodeGreaterThanEqualAndCustCodeLessThanEqualAndTxKindGreaterThanEqualAndTxKindLessThanEqualAndAdjCodeGreaterThanEqualAndAdjCodeLessThanEqualAndAdjDateGreaterThanEqualAndAdjDateLessThanEqual(custCode_0, custCode_1, txKind_2, txKind_3, adjCode_4, adjCode_5, adjDate_6, adjDate_7, pageable);
    else 
      slice = batxRateChangeRepos.findAllByCustCodeGreaterThanEqualAndCustCodeLessThanEqualAndTxKindGreaterThanEqualAndTxKindLessThanEqualAndAdjCodeGreaterThanEqualAndAdjCodeLessThanEqualAndAdjDateGreaterThanEqualAndAdjDateLessThanEqual(custCode_0, custCode_1, txKind_2, txKind_3, adjCode_4, adjCode_5, adjDate_6, adjDate_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxRateChange> adjCodeEq(int adjDate_0, int txKind_1, int rateKeyInCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("adjCodeEq " + dbName + " : " + "adjDate_0 : " + adjDate_0 + " txKind_1 : " +  txKind_1 + " rateKeyInCode_2 : " +  rateKeyInCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxRateChangeReposDay.findAllByAdjDateIsAndTxKindIsAndRateKeyInCodeIsOrderByCustNoAscFacmNoAscBormNoAsc(adjDate_0, txKind_1, rateKeyInCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxRateChangeReposMon.findAllByAdjDateIsAndTxKindIsAndRateKeyInCodeIsOrderByCustNoAscFacmNoAscBormNoAsc(adjDate_0, txKind_1, rateKeyInCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxRateChangeReposHist.findAllByAdjDateIsAndTxKindIsAndRateKeyInCodeIsOrderByCustNoAscFacmNoAscBormNoAsc(adjDate_0, txKind_1, rateKeyInCode_2, pageable);
    else 
      slice = batxRateChangeRepos.findAllByAdjDateIsAndTxKindIsAndRateKeyInCodeIsOrderByCustNoAscFacmNoAscBormNoAsc(adjDate_0, txKind_1, rateKeyInCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxRateChange> findL4321Report(int adjDate_0, int adjDate_1, int custCode_2, int custCode_3, int txKind_4, int adjCode_5, int adjCode_6, int confirmFlag_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4321Report " + dbName + " : " + "adjDate_0 : " + adjDate_0 + " adjDate_1 : " +  adjDate_1 + " custCode_2 : " +  custCode_2 + " custCode_3 : " +  custCode_3 + " txKind_4 : " +  txKind_4 + " adjCode_5 : " +  adjCode_5 + " adjCode_6 : " +  adjCode_6 + " confirmFlag_7 : " +  confirmFlag_7);
    if (dbName.equals(ContentName.onDay))
      slice = batxRateChangeReposDay.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndCustCodeGreaterThanEqualAndCustCodeLessThanEqualAndTxKindIsAndAdjCodeGreaterThanEqualAndAdjCodeLessThanEqualAndConfirmFlagIsOrderByCustNoAscFacmNoAscBormNoAsc(adjDate_0, adjDate_1, custCode_2, custCode_3, txKind_4, adjCode_5, adjCode_6, confirmFlag_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxRateChangeReposMon.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndCustCodeGreaterThanEqualAndCustCodeLessThanEqualAndTxKindIsAndAdjCodeGreaterThanEqualAndAdjCodeLessThanEqualAndConfirmFlagIsOrderByCustNoAscFacmNoAscBormNoAsc(adjDate_0, adjDate_1, custCode_2, custCode_3, txKind_4, adjCode_5, adjCode_6, confirmFlag_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxRateChangeReposHist.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndCustCodeGreaterThanEqualAndCustCodeLessThanEqualAndTxKindIsAndAdjCodeGreaterThanEqualAndAdjCodeLessThanEqualAndConfirmFlagIsOrderByCustNoAscFacmNoAscBormNoAsc(adjDate_0, adjDate_1, custCode_2, custCode_3, txKind_4, adjCode_5, adjCode_6, confirmFlag_7, pageable);
    else 
      slice = batxRateChangeRepos.findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndCustCodeGreaterThanEqualAndCustCodeLessThanEqualAndTxKindIsAndAdjCodeGreaterThanEqualAndAdjCodeLessThanEqualAndConfirmFlagIsOrderByCustNoAscFacmNoAscBormNoAsc(adjDate_0, adjDate_1, custCode_2, custCode_3, txKind_4, adjCode_5, adjCode_6, confirmFlag_7, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxRateChange> findL4320Erase(int adjDate_0, String titaTlrNo_1, String titaTxtNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxRateChange> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4320Erase " + dbName + " : " + "adjDate_0 : " + adjDate_0 + " titaTlrNo_1 : " +  titaTlrNo_1 + " titaTxtNo_2 : " +  titaTxtNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxRateChangeReposDay.findAllByAdjDateIsAndTitaTlrNoIsAndTitaTxtNoIs(adjDate_0, titaTlrNo_1, titaTxtNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxRateChangeReposMon.findAllByAdjDateIsAndTitaTlrNoIsAndTitaTxtNoIs(adjDate_0, titaTlrNo_1, titaTxtNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxRateChangeReposHist.findAllByAdjDateIsAndTitaTlrNoIsAndTitaTxtNoIs(adjDate_0, titaTlrNo_1, titaTxtNo_2, pageable);
    else 
      slice = batxRateChangeRepos.findAllByAdjDateIsAndTitaTlrNoIsAndTitaTxtNoIs(adjDate_0, titaTlrNo_1, titaTxtNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BatxRateChange findL2980printFirst(int custNo_0, int facmNo_1, int bormNo_2, int preNextAdjDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findL2980printFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " bormNo_2 : " +  bormNo_2 + " preNextAdjDate_3 : " +  preNextAdjDate_3);
    Optional<BatxRateChange> batxRateChangeT = null;
    if (dbName.equals(ContentName.onDay))
      batxRateChangeT = batxRateChangeReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPreNextAdjDateIs(custNo_0, facmNo_1, bormNo_2, preNextAdjDate_3);
    else if (dbName.equals(ContentName.onMon))
      batxRateChangeT = batxRateChangeReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPreNextAdjDateIs(custNo_0, facmNo_1, bormNo_2, preNextAdjDate_3);
    else if (dbName.equals(ContentName.onHist))
      batxRateChangeT = batxRateChangeReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPreNextAdjDateIs(custNo_0, facmNo_1, bormNo_2, preNextAdjDate_3);
    else 
      batxRateChangeT = batxRateChangeRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPreNextAdjDateIs(custNo_0, facmNo_1, bormNo_2, preNextAdjDate_3);

    return batxRateChangeT.isPresent() ? batxRateChangeT.get() : null;
  }

  @Override
  public BatxRateChange holdById(BatxRateChangeId batxRateChangeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + batxRateChangeId);
    Optional<BatxRateChange> batxRateChange = null;
    if (dbName.equals(ContentName.onDay))
      batxRateChange = batxRateChangeReposDay.findByBatxRateChangeId(batxRateChangeId);
    else if (dbName.equals(ContentName.onMon))
      batxRateChange = batxRateChangeReposMon.findByBatxRateChangeId(batxRateChangeId);
    else if (dbName.equals(ContentName.onHist))
      batxRateChange = batxRateChangeReposHist.findByBatxRateChangeId(batxRateChangeId);
    else 
      batxRateChange = batxRateChangeRepos.findByBatxRateChangeId(batxRateChangeId);
    return batxRateChange.isPresent() ? batxRateChange.get() : null;
  }

  @Override
  public BatxRateChange holdById(BatxRateChange batxRateChange, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + batxRateChange.getBatxRateChangeId());
    Optional<BatxRateChange> batxRateChangeT = null;
    if (dbName.equals(ContentName.onDay))
      batxRateChangeT = batxRateChangeReposDay.findByBatxRateChangeId(batxRateChange.getBatxRateChangeId());
    else if (dbName.equals(ContentName.onMon))
      batxRateChangeT = batxRateChangeReposMon.findByBatxRateChangeId(batxRateChange.getBatxRateChangeId());
    else if (dbName.equals(ContentName.onHist))
      batxRateChangeT = batxRateChangeReposHist.findByBatxRateChangeId(batxRateChange.getBatxRateChangeId());
    else 
      batxRateChangeT = batxRateChangeRepos.findByBatxRateChangeId(batxRateChange.getBatxRateChangeId());
    return batxRateChangeT.isPresent() ? batxRateChangeT.get() : null;
  }

  @Override
  public BatxRateChange insert(BatxRateChange batxRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + batxRateChange.getBatxRateChangeId());
    if (this.findById(batxRateChange.getBatxRateChangeId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      batxRateChange.setCreateEmpNo(empNot);

    if(batxRateChange.getLastUpdateEmpNo() == null || batxRateChange.getLastUpdateEmpNo().isEmpty())
      batxRateChange.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return batxRateChangeReposDay.saveAndFlush(batxRateChange);	
    else if (dbName.equals(ContentName.onMon))
      return batxRateChangeReposMon.saveAndFlush(batxRateChange);
    else if (dbName.equals(ContentName.onHist))
      return batxRateChangeReposHist.saveAndFlush(batxRateChange);
    else 
    return batxRateChangeRepos.saveAndFlush(batxRateChange);
  }

  @Override
  public BatxRateChange update(BatxRateChange batxRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + batxRateChange.getBatxRateChangeId());
    if (!empNot.isEmpty())
      batxRateChange.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return batxRateChangeReposDay.saveAndFlush(batxRateChange);	
    else if (dbName.equals(ContentName.onMon))
      return batxRateChangeReposMon.saveAndFlush(batxRateChange);
    else if (dbName.equals(ContentName.onHist))
      return batxRateChangeReposHist.saveAndFlush(batxRateChange);
    else 
    return batxRateChangeRepos.saveAndFlush(batxRateChange);
  }

  @Override
  public BatxRateChange update2(BatxRateChange batxRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + batxRateChange.getBatxRateChangeId());
    if (!empNot.isEmpty())
      batxRateChange.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      batxRateChangeReposDay.saveAndFlush(batxRateChange);	
    else if (dbName.equals(ContentName.onMon))
      batxRateChangeReposMon.saveAndFlush(batxRateChange);
    else if (dbName.equals(ContentName.onHist))
        batxRateChangeReposHist.saveAndFlush(batxRateChange);
    else 
      batxRateChangeRepos.saveAndFlush(batxRateChange);	
    return this.findById(batxRateChange.getBatxRateChangeId());
  }

  @Override
  public void delete(BatxRateChange batxRateChange, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + batxRateChange.getBatxRateChangeId());
    if (dbName.equals(ContentName.onDay)) {
      batxRateChangeReposDay.delete(batxRateChange);	
      batxRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxRateChangeReposMon.delete(batxRateChange);	
      batxRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxRateChangeReposHist.delete(batxRateChange);
      batxRateChangeReposHist.flush();
    }
    else {
      batxRateChangeRepos.delete(batxRateChange);
      batxRateChangeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<BatxRateChange> batxRateChange, TitaVo... titaVo) throws DBException {
    if (batxRateChange == null || batxRateChange.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (BatxRateChange t : batxRateChange){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      batxRateChange = batxRateChangeReposDay.saveAll(batxRateChange);	
      batxRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxRateChange = batxRateChangeReposMon.saveAll(batxRateChange);	
      batxRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxRateChange = batxRateChangeReposHist.saveAll(batxRateChange);
      batxRateChangeReposHist.flush();
    }
    else {
      batxRateChange = batxRateChangeRepos.saveAll(batxRateChange);
      batxRateChangeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<BatxRateChange> batxRateChange, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (batxRateChange == null || batxRateChange.size() == 0)
      throw new DBException(6);

    for (BatxRateChange t : batxRateChange) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      batxRateChange = batxRateChangeReposDay.saveAll(batxRateChange);	
      batxRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxRateChange = batxRateChangeReposMon.saveAll(batxRateChange);	
      batxRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxRateChange = batxRateChangeReposHist.saveAll(batxRateChange);
      batxRateChangeReposHist.flush();
    }
    else {
      batxRateChange = batxRateChangeRepos.saveAll(batxRateChange);
      batxRateChangeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<BatxRateChange> batxRateChange, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (batxRateChange == null || batxRateChange.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      batxRateChangeReposDay.deleteAll(batxRateChange);	
      batxRateChangeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxRateChangeReposMon.deleteAll(batxRateChange);	
      batxRateChangeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxRateChangeReposHist.deleteAll(batxRateChange);
      batxRateChangeReposHist.flush();
    }
    else {
      batxRateChangeRepos.deleteAll(batxRateChange);
      batxRateChangeRepos.flush();
    }
  }

}
