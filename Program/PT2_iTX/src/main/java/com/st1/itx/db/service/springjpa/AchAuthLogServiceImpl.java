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
import com.st1.itx.db.domain.AchAuthLog;
import com.st1.itx.db.domain.AchAuthLogId;
import com.st1.itx.db.repository.online.AchAuthLogRepository;
import com.st1.itx.db.repository.day.AchAuthLogRepositoryDay;
import com.st1.itx.db.repository.mon.AchAuthLogRepositoryMon;
import com.st1.itx.db.repository.hist.AchAuthLogRepositoryHist;
import com.st1.itx.db.service.AchAuthLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("achAuthLogService")
@Repository
public class AchAuthLogServiceImpl extends ASpringJpaParm implements AchAuthLogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AchAuthLogRepository achAuthLogRepos;

  @Autowired
  private AchAuthLogRepositoryDay achAuthLogReposDay;

  @Autowired
  private AchAuthLogRepositoryMon achAuthLogReposMon;

  @Autowired
  private AchAuthLogRepositoryHist achAuthLogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(achAuthLogRepos);
    org.junit.Assert.assertNotNull(achAuthLogReposDay);
    org.junit.Assert.assertNotNull(achAuthLogReposMon);
    org.junit.Assert.assertNotNull(achAuthLogReposHist);
  }

  @Override
  public AchAuthLog findById(AchAuthLogId achAuthLogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + achAuthLogId);
    Optional<AchAuthLog> achAuthLog = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLog = achAuthLogReposDay.findById(achAuthLogId);
    else if (dbName.equals(ContentName.onMon))
      achAuthLog = achAuthLogReposMon.findById(achAuthLogId);
    else if (dbName.equals(ContentName.onHist))
      achAuthLog = achAuthLogReposHist.findById(achAuthLogId);
    else 
      achAuthLog = achAuthLogRepos.findById(achAuthLogId);
    AchAuthLog obj = achAuthLog.isPresent() ? achAuthLog.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AchAuthLog> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AuthCreateDate", "CustNo", "RepayBank", "RepayAcct", "CreateFlag"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AuthCreateDate", "CustNo", "RepayBank", "RepayAcct", "CreateFlag"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAll(pageable);
    else 
      slice = achAuthLogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> custNoLike(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoLike " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByCustNoLike(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByCustNoLike(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByCustNoLike(custNo_0, pageable);
    else 
      slice = achAuthLogRepos.findAllByCustNoLike(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> repayAcctLike(String repayAcct_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("repayAcctLike " + dbName + " : " + "repayAcct_0 : " + repayAcct_0);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByRepayAcctLike(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByRepayAcctLike(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByRepayAcctLike(repayAcct_0, pageable);
    else 
      slice = achAuthLogRepos.findAllByRepayAcctLike(repayAcct_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByCustNoIs(custNo_0, pageable);
    else 
      slice = achAuthLogRepos.findAllByCustNoIs(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> repayAcctEq(String repayAcct_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("repayAcctEq " + dbName + " : " + "repayAcct_0 : " + repayAcct_0);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByRepayAcctIs(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByRepayAcctIs(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByRepayAcctIs(repayAcct_0, pageable);
    else 
      slice = achAuthLogRepos.findAllByRepayAcctIs(repayAcct_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> authCreateDateEq(int authCreateDate_0, int authCreateDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("authCreateDateEq " + dbName + " : " + "authCreateDate_0 : " + authCreateDate_0 + " authCreateDate_1 : " +  authCreateDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
    else 
      slice = achAuthLogRepos.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> propDateEq(int propDate_0, int propDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("propDateEq " + dbName + " : " + "propDate_0 : " + propDate_0 + " propDate_1 : " +  propDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_0, propDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_0, propDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_0, propDate_1, pageable);
    else 
      slice = achAuthLogRepos.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_0, propDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> retrDateEq(int retrDate_0, int retrDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("retrDateEq " + dbName + " : " + "retrDate_0 : " + retrDate_0 + " retrDate_1 : " +  retrDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqual(retrDate_0, retrDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqual(retrDate_0, retrDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqual(retrDate_0, retrDate_1, pageable);
    else 
      slice = achAuthLogRepos.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqual(retrDate_0, retrDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AchAuthLog pkFacmNoFirst(int custNo_0, String repayBank_1, String repayAcct_2, int facmNo_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("pkFacmNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " repayBank_1 : " +  repayBank_1 + " repayAcct_2 : " +  repayAcct_2 + " facmNo_3 : " +  facmNo_3);
    Optional<AchAuthLog> achAuthLogT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogT = achAuthLogReposDay.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsAndFacmNoIsOrderByAuthCreateDateDesc(custNo_0, repayBank_1, repayAcct_2, facmNo_3);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogT = achAuthLogReposMon.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsAndFacmNoIsOrderByAuthCreateDateDesc(custNo_0, repayBank_1, repayAcct_2, facmNo_3);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogT = achAuthLogReposHist.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsAndFacmNoIsOrderByAuthCreateDateDesc(custNo_0, repayBank_1, repayAcct_2, facmNo_3);
    else 
      achAuthLogT = achAuthLogRepos.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsAndFacmNoIsOrderByAuthCreateDateDesc(custNo_0, repayBank_1, repayAcct_2, facmNo_3);

    return achAuthLogT.isPresent() ? achAuthLogT.get() : null;
  }

  @Override
  public Slice<AchAuthLog> l4040ARg(List<String> authStatus_1, int custNo_2, int propDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("l4040ARg " + dbName + " : " + "authStatus_1 : " + authStatus_1 + " custNo_2 : " +  custNo_2 + " propDate_3 : " +  propDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIsAndPropDateIs(authStatus_1, custNo_2, propDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIsAndPropDateIs(authStatus_1, custNo_2, propDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIsAndPropDateIs(authStatus_1, custNo_2, propDate_3, pageable);
    else 
      slice = achAuthLogRepos.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIsAndPropDateIs(authStatus_1, custNo_2, propDate_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> l4040BRg(List<String> authStatus_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("l4040BRg " + dbName + " : " + "authStatus_1 : " + authStatus_1 + " propDate_2 : " +  propDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByMediaCodeIsNullAndAuthStatusInAndPropDateIs(authStatus_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByMediaCodeIsNullAndAuthStatusInAndPropDateIs(authStatus_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByMediaCodeIsNullAndAuthStatusInAndPropDateIs(authStatus_1, propDate_2, pageable);
    else 
      slice = achAuthLogRepos.findAllByMediaCodeIsNullAndAuthStatusInAndPropDateIs(authStatus_1, propDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> l4040CRg(List<String> authStatus_1, int custNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("l4040CRg " + dbName + " : " + "authStatus_1 : " + authStatus_1 + " custNo_2 : " +  custNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIs(authStatus_1, custNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIs(authStatus_1, custNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIs(authStatus_1, custNo_2, pageable);
    else 
      slice = achAuthLogRepos.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIs(authStatus_1, custNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> l4040DRg(List<String> authStatus_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("l4040DRg " + dbName + " : " + "authStatus_1 : " + authStatus_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByMediaCodeIsNullAndAuthStatusIn(authStatus_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByMediaCodeIsNullAndAuthStatusIn(authStatus_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByMediaCodeIsNullAndAuthStatusIn(authStatus_1, pageable);
    else 
      slice = achAuthLogRepos.findAllByMediaCodeIsNullAndAuthStatusIn(authStatus_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> mediaCodeIsnull(int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("mediaCodeIsnull " + dbName + " : " + "propDate_1 : " + propDate_1 + " propDate_2 : " +  propDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
    else 
      slice = achAuthLogRepos.findAllByMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLog> mediaCodeEq(String mediaCode_0, int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("mediaCodeEq " + dbName + " : " + "mediaCode_0 : " + mediaCode_0 + " propDate_1 : " +  propDate_1 + " propDate_2 : " +  propDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(mediaCode_0, propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(mediaCode_0, propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(mediaCode_0, propDate_1, propDate_2, pageable);
    else 
      slice = achAuthLogRepos.findAllByMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(mediaCode_0, propDate_1, propDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AchAuthLog facmNoFirst(int custNo_0, int facmNo_1, String createFlag_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("facmNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " createFlag_2 : " +  createFlag_2);
    Optional<AchAuthLog> achAuthLogT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogT = achAuthLogReposDay.findTopByCustNoIsAndFacmNoIsAndCreateFlagIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, createFlag_2);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogT = achAuthLogReposMon.findTopByCustNoIsAndFacmNoIsAndCreateFlagIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, createFlag_2);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogT = achAuthLogReposHist.findTopByCustNoIsAndFacmNoIsAndCreateFlagIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, createFlag_2);
    else 
      achAuthLogT = achAuthLogRepos.findTopByCustNoIsAndFacmNoIsAndCreateFlagIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, createFlag_2);

    return achAuthLogT.isPresent() ? achAuthLogT.get() : null;
  }

  @Override
  public AchAuthLog repayAcctFirst(int custNo_0, String repayBank_1, String repayAcct_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("repayAcctFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " repayBank_1 : " +  repayBank_1 + " repayAcct_2 : " +  repayAcct_2);
    Optional<AchAuthLog> achAuthLogT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogT = achAuthLogReposDay.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, repayBank_1, repayAcct_2);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogT = achAuthLogReposMon.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, repayBank_1, repayAcct_2);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogT = achAuthLogReposHist.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, repayBank_1, repayAcct_2);
    else 
      achAuthLogT = achAuthLogRepos.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, repayBank_1, repayAcct_2);

    return achAuthLogT.isPresent() ? achAuthLogT.get() : null;
  }

  @Override
  public Slice<AchAuthLog> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);
    else 
      slice = achAuthLogRepos.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AchAuthLog facmNoBFirst(int custNo_0, int facmNo_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("facmNoBFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    Optional<AchAuthLog> achAuthLogT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogT = achAuthLogReposDay.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogT = achAuthLogReposMon.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogT = achAuthLogReposHist.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);
    else 
      achAuthLogT = achAuthLogRepos.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);

    return achAuthLogT.isPresent() ? achAuthLogT.get() : null;
  }

  @Override
  public Slice<AchAuthLog> custNoRepayBankEq(int custNo_0, String repayBank_1, List<String> authStatus_2, List<String> createFlag_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoRepayBankEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " repayBank_1 : " +  repayBank_1 + " authStatus_2 : " +  authStatus_2 + " createFlag_3 : " +  createFlag_3);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByCustNoIsAndRepayBankIsAndAuthStatusInAndCreateFlagInOrderByRepayAcctDescAuthCreateDateDesc(custNo_0, repayBank_1, authStatus_2, createFlag_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByCustNoIsAndRepayBankIsAndAuthStatusInAndCreateFlagInOrderByRepayAcctDescAuthCreateDateDesc(custNo_0, repayBank_1, authStatus_2, createFlag_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByCustNoIsAndRepayBankIsAndAuthStatusInAndCreateFlagInOrderByRepayAcctDescAuthCreateDateDesc(custNo_0, repayBank_1, authStatus_2, createFlag_3, pageable);
    else 
      slice = achAuthLogRepos.findAllByCustNoIsAndRepayBankIsAndAuthStatusInAndCreateFlagInOrderByRepayAcctDescAuthCreateDateDesc(custNo_0, repayBank_1, authStatus_2, createFlag_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AchAuthLog facmNoRepayAcctFirst(int custNo_0, int facmNo_1, String repayBank_2, String repayAcct_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("facmNoRepayAcctFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " repayBank_2 : " +  repayBank_2 + " repayAcct_3 : " +  repayAcct_3);
    Optional<AchAuthLog> achAuthLogT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogT = achAuthLogReposDay.findTopByCustNoIsAndFacmNoIsAndRepayBankIsAndRepayAcctIs(custNo_0, facmNo_1, repayBank_2, repayAcct_3);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogT = achAuthLogReposMon.findTopByCustNoIsAndFacmNoIsAndRepayBankIsAndRepayAcctIs(custNo_0, facmNo_1, repayBank_2, repayAcct_3);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogT = achAuthLogReposHist.findTopByCustNoIsAndFacmNoIsAndRepayBankIsAndRepayAcctIs(custNo_0, facmNo_1, repayBank_2, repayAcct_3);
    else 
      achAuthLogT = achAuthLogRepos.findTopByCustNoIsAndFacmNoIsAndRepayBankIsAndRepayAcctIs(custNo_0, facmNo_1, repayBank_2, repayAcct_3);

    return achAuthLogT.isPresent() ? achAuthLogT.get() : null;
  }

  @Override
  public AchAuthLog facmNoPropDateFirst(int custNo_0, int facmNo_1, String repayBank_2, String repayAcct_3, int propDate_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("facmNoPropDateFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " repayBank_2 : " +  repayBank_2 + " repayAcct_3 : " +  repayAcct_3 + " propDate_4 : " +  propDate_4);
    Optional<AchAuthLog> achAuthLogT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogT = achAuthLogReposDay.findTopByCustNoIsAndFacmNoIsAndRepayBankIsAndRepayAcctIsAndPropDateIs(custNo_0, facmNo_1, repayBank_2, repayAcct_3, propDate_4);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogT = achAuthLogReposMon.findTopByCustNoIsAndFacmNoIsAndRepayBankIsAndRepayAcctIsAndPropDateIs(custNo_0, facmNo_1, repayBank_2, repayAcct_3, propDate_4);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogT = achAuthLogReposHist.findTopByCustNoIsAndFacmNoIsAndRepayBankIsAndRepayAcctIsAndPropDateIs(custNo_0, facmNo_1, repayBank_2, repayAcct_3, propDate_4);
    else 
      achAuthLogT = achAuthLogRepos.findTopByCustNoIsAndFacmNoIsAndRepayBankIsAndRepayAcctIsAndPropDateIs(custNo_0, facmNo_1, repayBank_2, repayAcct_3, propDate_4);

    return achAuthLogT.isPresent() ? achAuthLogT.get() : null;
  }

  @Override
  public Slice<AchAuthLog> propBatchNoEq(int propDate_0, String batchNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("propBatchNoEq " + dbName + " : " + "propDate_0 : " + propDate_0 + " batchNo_1 : " +  batchNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogReposDay.findAllByPropDateIsAndBatchNoLikeOrderByBatchNoDesc(propDate_0, batchNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogReposMon.findAllByPropDateIsAndBatchNoLikeOrderByBatchNoDesc(propDate_0, batchNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogReposHist.findAllByPropDateIsAndBatchNoLikeOrderByBatchNoDesc(propDate_0, batchNo_1, pageable);
    else 
      slice = achAuthLogRepos.findAllByPropDateIsAndBatchNoLikeOrderByBatchNoDesc(propDate_0, batchNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AchAuthLog holdById(AchAuthLogId achAuthLogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + achAuthLogId);
    Optional<AchAuthLog> achAuthLog = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLog = achAuthLogReposDay.findByAchAuthLogId(achAuthLogId);
    else if (dbName.equals(ContentName.onMon))
      achAuthLog = achAuthLogReposMon.findByAchAuthLogId(achAuthLogId);
    else if (dbName.equals(ContentName.onHist))
      achAuthLog = achAuthLogReposHist.findByAchAuthLogId(achAuthLogId);
    else 
      achAuthLog = achAuthLogRepos.findByAchAuthLogId(achAuthLogId);
    return achAuthLog.isPresent() ? achAuthLog.get() : null;
  }

  @Override
  public AchAuthLog holdById(AchAuthLog achAuthLog, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + achAuthLog.getAchAuthLogId());
    Optional<AchAuthLog> achAuthLogT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogT = achAuthLogReposDay.findByAchAuthLogId(achAuthLog.getAchAuthLogId());
    else if (dbName.equals(ContentName.onMon))
      achAuthLogT = achAuthLogReposMon.findByAchAuthLogId(achAuthLog.getAchAuthLogId());
    else if (dbName.equals(ContentName.onHist))
      achAuthLogT = achAuthLogReposHist.findByAchAuthLogId(achAuthLog.getAchAuthLogId());
    else 
      achAuthLogT = achAuthLogRepos.findByAchAuthLogId(achAuthLog.getAchAuthLogId());
    return achAuthLogT.isPresent() ? achAuthLogT.get() : null;
  }

  @Override
  public AchAuthLog insert(AchAuthLog achAuthLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + achAuthLog.getAchAuthLogId());
    if (this.findById(achAuthLog.getAchAuthLogId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      achAuthLog.setCreateEmpNo(empNot);

    if(achAuthLog.getLastUpdateEmpNo() == null || achAuthLog.getLastUpdateEmpNo().isEmpty())
      achAuthLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return achAuthLogReposDay.saveAndFlush(achAuthLog);	
    else if (dbName.equals(ContentName.onMon))
      return achAuthLogReposMon.saveAndFlush(achAuthLog);
    else if (dbName.equals(ContentName.onHist))
      return achAuthLogReposHist.saveAndFlush(achAuthLog);
    else 
    return achAuthLogRepos.saveAndFlush(achAuthLog);
  }

  @Override
  public AchAuthLog update(AchAuthLog achAuthLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + achAuthLog.getAchAuthLogId());
    if (!empNot.isEmpty())
      achAuthLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return achAuthLogReposDay.saveAndFlush(achAuthLog);	
    else if (dbName.equals(ContentName.onMon))
      return achAuthLogReposMon.saveAndFlush(achAuthLog);
    else if (dbName.equals(ContentName.onHist))
      return achAuthLogReposHist.saveAndFlush(achAuthLog);
    else 
    return achAuthLogRepos.saveAndFlush(achAuthLog);
  }

  @Override
  public AchAuthLog update2(AchAuthLog achAuthLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + achAuthLog.getAchAuthLogId());
    if (!empNot.isEmpty())
      achAuthLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      achAuthLogReposDay.saveAndFlush(achAuthLog);	
    else if (dbName.equals(ContentName.onMon))
      achAuthLogReposMon.saveAndFlush(achAuthLog);
    else if (dbName.equals(ContentName.onHist))
        achAuthLogReposHist.saveAndFlush(achAuthLog);
    else 
      achAuthLogRepos.saveAndFlush(achAuthLog);	
    return this.findById(achAuthLog.getAchAuthLogId());
  }

  @Override
  public void delete(AchAuthLog achAuthLog, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + achAuthLog.getAchAuthLogId());
    if (dbName.equals(ContentName.onDay)) {
      achAuthLogReposDay.delete(achAuthLog);	
      achAuthLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLogReposMon.delete(achAuthLog);	
      achAuthLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLogReposHist.delete(achAuthLog);
      achAuthLogReposHist.flush();
    }
    else {
      achAuthLogRepos.delete(achAuthLog);
      achAuthLogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AchAuthLog> achAuthLog, TitaVo... titaVo) throws DBException {
    if (achAuthLog == null || achAuthLog.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (AchAuthLog t : achAuthLog){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      achAuthLog = achAuthLogReposDay.saveAll(achAuthLog);	
      achAuthLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLog = achAuthLogReposMon.saveAll(achAuthLog);	
      achAuthLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLog = achAuthLogReposHist.saveAll(achAuthLog);
      achAuthLogReposHist.flush();
    }
    else {
      achAuthLog = achAuthLogRepos.saveAll(achAuthLog);
      achAuthLogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AchAuthLog> achAuthLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (achAuthLog == null || achAuthLog.size() == 0)
      throw new DBException(6);

    for (AchAuthLog t : achAuthLog) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      achAuthLog = achAuthLogReposDay.saveAll(achAuthLog);	
      achAuthLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLog = achAuthLogReposMon.saveAll(achAuthLog);	
      achAuthLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLog = achAuthLogReposHist.saveAll(achAuthLog);
      achAuthLogReposHist.flush();
    }
    else {
      achAuthLog = achAuthLogRepos.saveAll(achAuthLog);
      achAuthLogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AchAuthLog> achAuthLog, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (achAuthLog == null || achAuthLog.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      achAuthLogReposDay.deleteAll(achAuthLog);	
      achAuthLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLogReposMon.deleteAll(achAuthLog);	
      achAuthLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLogReposHist.deleteAll(achAuthLog);
      achAuthLogReposHist.flush();
    }
    else {
      achAuthLogRepos.deleteAll(achAuthLog);
      achAuthLogRepos.flush();
    }
  }

}
