package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.domain.AchAuthLogHistory;
import com.st1.itx.db.domain.AchAuthLogHistoryId;
import com.st1.itx.db.repository.online.AchAuthLogHistoryRepository;
import com.st1.itx.db.repository.day.AchAuthLogHistoryRepositoryDay;
import com.st1.itx.db.repository.mon.AchAuthLogHistoryRepositoryMon;
import com.st1.itx.db.repository.hist.AchAuthLogHistoryRepositoryHist;
import com.st1.itx.db.service.AchAuthLogHistoryService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("achAuthLogHistoryService")
@Repository
public class AchAuthLogHistoryServiceImpl implements AchAuthLogHistoryService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(AchAuthLogHistoryServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AchAuthLogHistoryRepository achAuthLogHistoryRepos;

  @Autowired
  private AchAuthLogHistoryRepositoryDay achAuthLogHistoryReposDay;

  @Autowired
  private AchAuthLogHistoryRepositoryMon achAuthLogHistoryReposMon;

  @Autowired
  private AchAuthLogHistoryRepositoryHist achAuthLogHistoryReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(achAuthLogHistoryRepos);
    org.junit.Assert.assertNotNull(achAuthLogHistoryReposDay);
    org.junit.Assert.assertNotNull(achAuthLogHistoryReposMon);
    org.junit.Assert.assertNotNull(achAuthLogHistoryReposHist);
  }

  @Override
  public AchAuthLogHistory findById(AchAuthLogHistoryId achAuthLogHistoryId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + achAuthLogHistoryId);
    Optional<AchAuthLogHistory> achAuthLogHistory = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistory = achAuthLogHistoryReposDay.findById(achAuthLogHistoryId);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistory = achAuthLogHistoryReposMon.findById(achAuthLogHistoryId);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistory = achAuthLogHistoryReposHist.findById(achAuthLogHistoryId);
    else 
      achAuthLogHistory = achAuthLogHistoryRepos.findById(achAuthLogHistoryId);
    AchAuthLogHistory obj = achAuthLogHistory.isPresent() ? achAuthLogHistory.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AchAuthLogHistory> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "DetailSeq"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAll(pageable);
    else 
      slice = achAuthLogHistoryRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> custNoLike(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("custNoLike " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByCustNoLike(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByCustNoLike(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByCustNoLike(custNo_0, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByCustNoLike(custNo_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> repayAcctLike(String repayAcct_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("repayAcctLike " + dbName + " : " + "repayAcct_0 : " + repayAcct_0);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByRepayAcctLike(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByRepayAcctLike(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByRepayAcctLike(repayAcct_0, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByRepayAcctLike(repayAcct_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByCustNoIs(custNo_0, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByCustNoIs(custNo_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> repayAcctEq(String repayAcct_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("repayAcctEq " + dbName + " : " + "repayAcct_0 : " + repayAcct_0);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByRepayAcctIs(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByRepayAcctIs(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByRepayAcctIs(repayAcct_0, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByRepayAcctIs(repayAcct_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> authCreateDateEq(int authCreateDate_0, int authCreateDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("authCreateDateEq " + dbName + " : " + "authCreateDate_0 : " + authCreateDate_0 + " authCreateDate_1 : " +  authCreateDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> propDateEq(int propDate_0, int propDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("propDateEq " + dbName + " : " + "propDate_0 : " + propDate_0 + " propDate_1 : " +  propDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_0, propDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_0, propDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_0, propDate_1, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_0, propDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> retrDateEq(int retrDate_0, int retrDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("retrDateEq " + dbName + " : " + "retrDate_0 : " + retrDate_0 + " retrDate_1 : " +  retrDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqual(retrDate_0, retrDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqual(retrDate_0, retrDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqual(retrDate_0, retrDate_1, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqual(retrDate_0, retrDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AchAuthLogHistory pkFacmNoFirst(int custNo_0, String repayBank_1, String repayAcct_2, int facmNo_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("pkFacmNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " repayBank_1 : " +  repayBank_1 + " repayAcct_2 : " +  repayAcct_2 + " facmNo_3 : " +  facmNo_3);
    Optional<AchAuthLogHistory> achAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistoryT = achAuthLogHistoryReposDay.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsAndFacmNoIsOrderByAuthCreateDateDesc(custNo_0, repayBank_1, repayAcct_2, facmNo_3);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistoryT = achAuthLogHistoryReposMon.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsAndFacmNoIsOrderByAuthCreateDateDesc(custNo_0, repayBank_1, repayAcct_2, facmNo_3);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistoryT = achAuthLogHistoryReposHist.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsAndFacmNoIsOrderByAuthCreateDateDesc(custNo_0, repayBank_1, repayAcct_2, facmNo_3);
    else 
      achAuthLogHistoryT = achAuthLogHistoryRepos.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsAndFacmNoIsOrderByAuthCreateDateDesc(custNo_0, repayBank_1, repayAcct_2, facmNo_3);
    return achAuthLogHistoryT.isPresent() ? achAuthLogHistoryT.get() : null;
  }

  @Override
  public Slice<AchAuthLogHistory> l4040ARg(List<String> authStatus_1, int custNo_2, int propDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("l4040ARg " + dbName + " : " + "authStatus_1 : " + authStatus_1 + " custNo_2 : " +  custNo_2 + " propDate_3 : " +  propDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIsAndPropDateIs(authStatus_1, custNo_2, propDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIsAndPropDateIs(authStatus_1, custNo_2, propDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIsAndPropDateIs(authStatus_1, custNo_2, propDate_3, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIsAndPropDateIs(authStatus_1, custNo_2, propDate_3, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> l4040BRg(List<String> authStatus_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("l4040BRg " + dbName + " : " + "authStatus_1 : " + authStatus_1 + " propDate_2 : " +  propDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByMediaCodeIsNullAndAuthStatusInAndPropDateIs(authStatus_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByMediaCodeIsNullAndAuthStatusInAndPropDateIs(authStatus_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByMediaCodeIsNullAndAuthStatusInAndPropDateIs(authStatus_1, propDate_2, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByMediaCodeIsNullAndAuthStatusInAndPropDateIs(authStatus_1, propDate_2, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> l4040CRg(List<String> authStatus_1, int custNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("l4040CRg " + dbName + " : " + "authStatus_1 : " + authStatus_1 + " custNo_2 : " +  custNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIs(authStatus_1, custNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIs(authStatus_1, custNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIs(authStatus_1, custNo_2, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByMediaCodeIsNullAndAuthStatusInAndCustNoIs(authStatus_1, custNo_2, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> l4040DRg(List<String> authStatus_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("l4040DRg " + dbName + " : " + "authStatus_1 : " + authStatus_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByMediaCodeIsNullAndAuthStatusIn(authStatus_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByMediaCodeIsNullAndAuthStatusIn(authStatus_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByMediaCodeIsNullAndAuthStatusIn(authStatus_1, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByMediaCodeIsNullAndAuthStatusIn(authStatus_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> mediaCodeIsnull(int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("mediaCodeIsnull " + dbName + " : " + "propDate_1 : " + propDate_1 + " propDate_2 : " +  propDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> mediaCodeEq(String mediaCode_0, int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("mediaCodeEq " + dbName + " : " + "mediaCode_0 : " + mediaCode_0 + " propDate_1 : " +  propDate_1 + " propDate_2 : " +  propDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(mediaCode_0, propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(mediaCode_0, propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(mediaCode_0, propDate_1, propDate_2, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(mediaCode_0, propDate_1, propDate_2, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AchAuthLogHistory facmNoFirst(int custNo_0, int facmNo_1, String createFlag_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("facmNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " createFlag_2 : " +  createFlag_2);
    Optional<AchAuthLogHistory> achAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistoryT = achAuthLogHistoryReposDay.findTopByCustNoIsAndFacmNoIsAndCreateFlagIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, createFlag_2);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistoryT = achAuthLogHistoryReposMon.findTopByCustNoIsAndFacmNoIsAndCreateFlagIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, createFlag_2);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistoryT = achAuthLogHistoryReposHist.findTopByCustNoIsAndFacmNoIsAndCreateFlagIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, createFlag_2);
    else 
      achAuthLogHistoryT = achAuthLogHistoryRepos.findTopByCustNoIsAndFacmNoIsAndCreateFlagIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, createFlag_2);
    return achAuthLogHistoryT.isPresent() ? achAuthLogHistoryT.get() : null;
  }

  @Override
  public AchAuthLogHistory repayAcctFirst(int custNo_0, String repayBank_1, String repayAcct_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("repayAcctFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " repayBank_1 : " +  repayBank_1 + " repayAcct_2 : " +  repayAcct_2);
    Optional<AchAuthLogHistory> achAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistoryT = achAuthLogHistoryReposDay.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, repayBank_1, repayAcct_2);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistoryT = achAuthLogHistoryReposMon.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, repayBank_1, repayAcct_2);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistoryT = achAuthLogHistoryReposHist.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, repayBank_1, repayAcct_2);
    else 
      achAuthLogHistoryT = achAuthLogHistoryRepos.findTopByCustNoIsAndRepayBankIsAndRepayAcctIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, repayBank_1, repayAcct_2);
    return achAuthLogHistoryT.isPresent() ? achAuthLogHistoryT.get() : null;
  }

  @Override
  public Slice<AchAuthLogHistory> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("facmNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AchAuthLogHistory facmNoBFirst(int custNo_0, int facmNo_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("facmNoBFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    Optional<AchAuthLogHistory> achAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistoryT = achAuthLogHistoryReposDay.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistoryT = achAuthLogHistoryReposMon.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistoryT = achAuthLogHistoryReposHist.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);
    else 
      achAuthLogHistoryT = achAuthLogHistoryRepos.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);
    return achAuthLogHistoryT.isPresent() ? achAuthLogHistoryT.get() : null;
  }

  @Override
  public AchAuthLogHistory facmNoCFirst(int custNo_0, int facmNo_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("facmNoCFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    Optional<AchAuthLogHistory> achAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistoryT = achAuthLogHistoryReposDay.findTopByCustNoIsAndFacmNoIsOrderByDetailSeqDesc(custNo_0, facmNo_1);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistoryT = achAuthLogHistoryReposMon.findTopByCustNoIsAndFacmNoIsOrderByDetailSeqDesc(custNo_0, facmNo_1);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistoryT = achAuthLogHistoryReposHist.findTopByCustNoIsAndFacmNoIsOrderByDetailSeqDesc(custNo_0, facmNo_1);
    else 
      achAuthLogHistoryT = achAuthLogHistoryRepos.findTopByCustNoIsAndFacmNoIsOrderByDetailSeqDesc(custNo_0, facmNo_1);
    return achAuthLogHistoryT.isPresent() ? achAuthLogHistoryT.get() : null;
  }

  @Override
  public AchAuthLogHistory AuthLogKeyFirst(int authCreateDate_0, int custNo_1, String repayBank_2, String repayAcct_3, String createFlag_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("AuthLogKeyFirst " + dbName + " : " + "authCreateDate_0 : " + authCreateDate_0 + " custNo_1 : " +  custNo_1 + " repayBank_2 : " +  repayBank_2 + " repayAcct_3 : " +  repayAcct_3 + " createFlag_4 : " +  createFlag_4);
    Optional<AchAuthLogHistory> achAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistoryT = achAuthLogHistoryReposDay.findTopByAuthCreateDateIsAndCustNoIsAndRepayBankIsAndRepayAcctIsAndCreateFlagIsOrderByDetailSeqDesc(authCreateDate_0, custNo_1, repayBank_2, repayAcct_3, createFlag_4);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistoryT = achAuthLogHistoryReposMon.findTopByAuthCreateDateIsAndCustNoIsAndRepayBankIsAndRepayAcctIsAndCreateFlagIsOrderByDetailSeqDesc(authCreateDate_0, custNo_1, repayBank_2, repayAcct_3, createFlag_4);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistoryT = achAuthLogHistoryReposHist.findTopByAuthCreateDateIsAndCustNoIsAndRepayBankIsAndRepayAcctIsAndCreateFlagIsOrderByDetailSeqDesc(authCreateDate_0, custNo_1, repayBank_2, repayAcct_3, createFlag_4);
    else 
      achAuthLogHistoryT = achAuthLogHistoryRepos.findTopByAuthCreateDateIsAndCustNoIsAndRepayBankIsAndRepayAcctIsAndCreateFlagIsOrderByDetailSeqDesc(authCreateDate_0, custNo_1, repayBank_2, repayAcct_3, createFlag_4);
    return achAuthLogHistoryT.isPresent() ? achAuthLogHistoryT.get() : null;
  }

  @Override
  public AchAuthLogHistory holdById(AchAuthLogHistoryId achAuthLogHistoryId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + achAuthLogHistoryId);
    Optional<AchAuthLogHistory> achAuthLogHistory = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistory = achAuthLogHistoryReposDay.findByAchAuthLogHistoryId(achAuthLogHistoryId);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistory = achAuthLogHistoryReposMon.findByAchAuthLogHistoryId(achAuthLogHistoryId);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistory = achAuthLogHistoryReposHist.findByAchAuthLogHistoryId(achAuthLogHistoryId);
    else 
      achAuthLogHistory = achAuthLogHistoryRepos.findByAchAuthLogHistoryId(achAuthLogHistoryId);
    return achAuthLogHistory.isPresent() ? achAuthLogHistory.get() : null;
  }

  @Override
  public AchAuthLogHistory holdById(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + achAuthLogHistory.getAchAuthLogHistoryId());
    Optional<AchAuthLogHistory> achAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistoryT = achAuthLogHistoryReposDay.findByAchAuthLogHistoryId(achAuthLogHistory.getAchAuthLogHistoryId());
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistoryT = achAuthLogHistoryReposMon.findByAchAuthLogHistoryId(achAuthLogHistory.getAchAuthLogHistoryId());
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistoryT = achAuthLogHistoryReposHist.findByAchAuthLogHistoryId(achAuthLogHistory.getAchAuthLogHistoryId());
    else 
      achAuthLogHistoryT = achAuthLogHistoryRepos.findByAchAuthLogHistoryId(achAuthLogHistory.getAchAuthLogHistoryId());
    return achAuthLogHistoryT.isPresent() ? achAuthLogHistoryT.get() : null;
  }

  @Override
  public AchAuthLogHistory insert(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + achAuthLogHistory.getAchAuthLogHistoryId());
    if (this.findById(achAuthLogHistory.getAchAuthLogHistoryId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      achAuthLogHistory.setCreateEmpNo(empNot);

    if(achAuthLogHistory.getLastUpdateEmpNo() == null || achAuthLogHistory.getLastUpdateEmpNo().isEmpty())
      achAuthLogHistory.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return achAuthLogHistoryReposDay.saveAndFlush(achAuthLogHistory);	
    else if (dbName.equals(ContentName.onMon))
      return achAuthLogHistoryReposMon.saveAndFlush(achAuthLogHistory);
    else if (dbName.equals(ContentName.onHist))
      return achAuthLogHistoryReposHist.saveAndFlush(achAuthLogHistory);
    else 
    return achAuthLogHistoryRepos.saveAndFlush(achAuthLogHistory);
  }

  @Override
  public AchAuthLogHistory update(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + achAuthLogHistory.getAchAuthLogHistoryId());
    if (!empNot.isEmpty())
      achAuthLogHistory.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return achAuthLogHistoryReposDay.saveAndFlush(achAuthLogHistory);	
    else if (dbName.equals(ContentName.onMon))
      return achAuthLogHistoryReposMon.saveAndFlush(achAuthLogHistory);
    else if (dbName.equals(ContentName.onHist))
      return achAuthLogHistoryReposHist.saveAndFlush(achAuthLogHistory);
    else 
    return achAuthLogHistoryRepos.saveAndFlush(achAuthLogHistory);
  }

  @Override
  public AchAuthLogHistory update2(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + achAuthLogHistory.getAchAuthLogHistoryId());
    if (!empNot.isEmpty())
      achAuthLogHistory.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      achAuthLogHistoryReposDay.saveAndFlush(achAuthLogHistory);	
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistoryReposMon.saveAndFlush(achAuthLogHistory);
    else if (dbName.equals(ContentName.onHist))
        achAuthLogHistoryReposHist.saveAndFlush(achAuthLogHistory);
    else 
      achAuthLogHistoryRepos.saveAndFlush(achAuthLogHistory);	
    return this.findById(achAuthLogHistory.getAchAuthLogHistoryId());
  }

  @Override
  public void delete(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + achAuthLogHistory.getAchAuthLogHistoryId());
    if (dbName.equals(ContentName.onDay)) {
      achAuthLogHistoryReposDay.delete(achAuthLogHistory);	
      achAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLogHistoryReposMon.delete(achAuthLogHistory);	
      achAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLogHistoryReposHist.delete(achAuthLogHistory);
      achAuthLogHistoryReposHist.flush();
    }
    else {
      achAuthLogHistoryRepos.delete(achAuthLogHistory);
      achAuthLogHistoryRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException {
    if (achAuthLogHistory == null || achAuthLogHistory.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (AchAuthLogHistory t : achAuthLogHistory){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      achAuthLogHistory = achAuthLogHistoryReposDay.saveAll(achAuthLogHistory);	
      achAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLogHistory = achAuthLogHistoryReposMon.saveAll(achAuthLogHistory);	
      achAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLogHistory = achAuthLogHistoryReposHist.saveAll(achAuthLogHistory);
      achAuthLogHistoryReposHist.flush();
    }
    else {
      achAuthLogHistory = achAuthLogHistoryRepos.saveAll(achAuthLogHistory);
      achAuthLogHistoryRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (achAuthLogHistory == null || achAuthLogHistory.size() == 0)
      throw new DBException(6);

    for (AchAuthLogHistory t : achAuthLogHistory) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      achAuthLogHistory = achAuthLogHistoryReposDay.saveAll(achAuthLogHistory);	
      achAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLogHistory = achAuthLogHistoryReposMon.saveAll(achAuthLogHistory);	
      achAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLogHistory = achAuthLogHistoryReposHist.saveAll(achAuthLogHistory);
      achAuthLogHistoryReposHist.flush();
    }
    else {
      achAuthLogHistory = achAuthLogHistoryRepos.saveAll(achAuthLogHistory);
      achAuthLogHistoryRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (achAuthLogHistory == null || achAuthLogHistory.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      achAuthLogHistoryReposDay.deleteAll(achAuthLogHistory);	
      achAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLogHistoryReposMon.deleteAll(achAuthLogHistory);	
      achAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLogHistoryReposHist.deleteAll(achAuthLogHistory);
      achAuthLogHistoryReposHist.flush();
    }
    else {
      achAuthLogHistoryRepos.deleteAll(achAuthLogHistory);
      achAuthLogHistoryRepos.flush();
    }
  }

}
