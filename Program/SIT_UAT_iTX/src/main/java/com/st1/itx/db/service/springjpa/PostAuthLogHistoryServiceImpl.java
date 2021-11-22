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
import com.st1.itx.db.domain.PostAuthLogHistory;
import com.st1.itx.db.repository.online.PostAuthLogHistoryRepository;
import com.st1.itx.db.repository.day.PostAuthLogHistoryRepositoryDay;
import com.st1.itx.db.repository.mon.PostAuthLogHistoryRepositoryMon;
import com.st1.itx.db.repository.hist.PostAuthLogHistoryRepositoryHist;
import com.st1.itx.db.service.PostAuthLogHistoryService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("postAuthLogHistoryService")
@Repository
public class PostAuthLogHistoryServiceImpl extends ASpringJpaParm implements PostAuthLogHistoryService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private PostAuthLogHistoryRepository postAuthLogHistoryRepos;

  @Autowired
  private PostAuthLogHistoryRepositoryDay postAuthLogHistoryReposDay;

  @Autowired
  private PostAuthLogHistoryRepositoryMon postAuthLogHistoryReposMon;

  @Autowired
  private PostAuthLogHistoryRepositoryHist postAuthLogHistoryReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(postAuthLogHistoryRepos);
    org.junit.Assert.assertNotNull(postAuthLogHistoryReposDay);
    org.junit.Assert.assertNotNull(postAuthLogHistoryReposMon);
    org.junit.Assert.assertNotNull(postAuthLogHistoryReposHist);
  }

  @Override
  public PostAuthLogHistory findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<PostAuthLogHistory> postAuthLogHistory = null;
    if (dbName.equals(ContentName.onDay))
      postAuthLogHistory = postAuthLogHistoryReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      postAuthLogHistory = postAuthLogHistoryReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      postAuthLogHistory = postAuthLogHistoryReposHist.findById(logNo);
    else 
      postAuthLogHistory = postAuthLogHistoryRepos.findById(logNo);
    PostAuthLogHistory obj = postAuthLogHistory.isPresent() ? postAuthLogHistory.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<PostAuthLogHistory> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAll(pageable);
    else 
      slice = postAuthLogHistoryRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PostAuthLogHistory> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByCustNoIs(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByCustNoIs(custNo_0, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByCustNoIs(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PostAuthLogHistory> repayAcctEq(String repayAcct_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("repayAcctEq " + dbName + " : " + "repayAcct_0 : " + repayAcct_0);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByRepayAcctIs(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByRepayAcctIs(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByRepayAcctIs(repayAcct_0, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByRepayAcctIs(repayAcct_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PostAuthLogHistory> repayAcctLike(String repayAcct_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("repayAcctLike " + dbName + " : " + "repayAcct_0 : " + repayAcct_0);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByRepayAcctLike(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByRepayAcctLike(repayAcct_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByRepayAcctLike(repayAcct_0, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByRepayAcctLike(repayAcct_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PostAuthLogHistory> authCreateDateEq(int authCreateDate_0, int authCreateDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("authCreateDateEq " + dbName + " : " + "authCreateDate_0 : " + authCreateDate_0 + " authCreateDate_1 : " +  authCreateDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PostAuthLogHistory> propDateEq(int propDate_0, int propDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("propDateEq " + dbName + " : " + "propDate_0 : " + propDate_0 + " propDate_1 : " +  propDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(propDate_0, propDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(propDate_0, propDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(propDate_0, propDate_1, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(propDate_0, propDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PostAuthLogHistory> retrDateEq(int retrDate_0, int retrDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("retrDateEq " + dbName + " : " + "retrDate_0 : " + retrDate_0 + " retrDate_1 : " +  retrDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(retrDate_0, retrDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(retrDate_0, retrDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(retrDate_0, retrDate_1, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(retrDate_0, retrDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PostAuthLogHistory acctSeqFirst(String custId_0, String postDepCode_1, int custNo_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("acctSeqFirst " + dbName + " : " + "custId_0 : " + custId_0 + " postDepCode_1 : " +  postDepCode_1 + " custNo_2 : " +  custNo_2);
    Optional<PostAuthLogHistory> postAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      postAuthLogHistoryT = postAuthLogHistoryReposDay.findTopByCustIdIsAndPostDepCodeIsAndCustNoIsOrderByRepayAcctSeqDesc(custId_0, postDepCode_1, custNo_2);
    else if (dbName.equals(ContentName.onMon))
      postAuthLogHistoryT = postAuthLogHistoryReposMon.findTopByCustIdIsAndPostDepCodeIsAndCustNoIsOrderByRepayAcctSeqDesc(custId_0, postDepCode_1, custNo_2);
    else if (dbName.equals(ContentName.onHist))
      postAuthLogHistoryT = postAuthLogHistoryReposHist.findTopByCustIdIsAndPostDepCodeIsAndCustNoIsOrderByRepayAcctSeqDesc(custId_0, postDepCode_1, custNo_2);
    else 
      postAuthLogHistoryT = postAuthLogHistoryRepos.findTopByCustIdIsAndPostDepCodeIsAndCustNoIsOrderByRepayAcctSeqDesc(custId_0, postDepCode_1, custNo_2);

    return postAuthLogHistoryT.isPresent() ? postAuthLogHistoryT.get() : null;
  }

  @Override
  public PostAuthLogHistory pkFacmNoFirst(String authApplCode_0, int custNo_1, String postDepCode_2, String repayAcct_3, String authCode_4, int facmNo_5, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("pkFacmNoFirst " + dbName + " : " + "authApplCode_0 : " + authApplCode_0 + " custNo_1 : " +  custNo_1 + " postDepCode_2 : " +  postDepCode_2 + " repayAcct_3 : " +  repayAcct_3 + " authCode_4 : " +  authCode_4 + " facmNo_5 : " +  facmNo_5);
    Optional<PostAuthLogHistory> postAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      postAuthLogHistoryT = postAuthLogHistoryReposDay.findTopByAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsAndFacmNoIsOrderByRepayAcctSeqDesc(authApplCode_0, custNo_1, postDepCode_2, repayAcct_3, authCode_4, facmNo_5);
    else if (dbName.equals(ContentName.onMon))
      postAuthLogHistoryT = postAuthLogHistoryReposMon.findTopByAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsAndFacmNoIsOrderByRepayAcctSeqDesc(authApplCode_0, custNo_1, postDepCode_2, repayAcct_3, authCode_4, facmNo_5);
    else if (dbName.equals(ContentName.onHist))
      postAuthLogHistoryT = postAuthLogHistoryReposHist.findTopByAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsAndFacmNoIsOrderByRepayAcctSeqDesc(authApplCode_0, custNo_1, postDepCode_2, repayAcct_3, authCode_4, facmNo_5);
    else 
      postAuthLogHistoryT = postAuthLogHistoryRepos.findTopByAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsAndFacmNoIsOrderByRepayAcctSeqDesc(authApplCode_0, custNo_1, postDepCode_2, repayAcct_3, authCode_4, facmNo_5);

    return postAuthLogHistoryT.isPresent() ? postAuthLogHistoryT.get() : null;
  }

  @Override
  public Slice<PostAuthLogHistory> l4041ARg(List<String> authErrorCode_1, int custNo_2, int propDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("l4041ARg " + dbName + " : " + "authErrorCode_1 : " + authErrorCode_1 + " custNo_2 : " +  custNo_2 + " propDate_3 : " +  propDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIsAndPropDateIs(authErrorCode_1, custNo_2, propDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIsAndPropDateIs(authErrorCode_1, custNo_2, propDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIsAndPropDateIs(authErrorCode_1, custNo_2, propDate_3, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIsAndPropDateIs(authErrorCode_1, custNo_2, propDate_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PostAuthLogHistory> l4041BRg(List<String> authErrorCode_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("l4041BRg " + dbName + " : " + "authErrorCode_1 : " + authErrorCode_1 + " propDate_2 : " +  propDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndPropDateIs(authErrorCode_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndPropDateIs(authErrorCode_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndPropDateIs(authErrorCode_1, propDate_2, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndPropDateIs(authErrorCode_1, propDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PostAuthLogHistory> l4041CRg(List<String> authErrorCode_1, int custNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("l4041CRg " + dbName + " : " + "authErrorCode_1 : " + authErrorCode_1 + " custNo_2 : " +  custNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIs(authErrorCode_1, custNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIs(authErrorCode_1, custNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIs(authErrorCode_1, custNo_2, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIs(authErrorCode_1, custNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PostAuthLogHistory> l4041DRg(List<String> authErrorCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("l4041DRg " + dbName + " : " + "authErrorCode_1 : " + authErrorCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByPostMediaCodeIsNullAndAuthErrorCodeIn(authErrorCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByPostMediaCodeIsNullAndAuthErrorCodeIn(authErrorCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByPostMediaCodeIsNullAndAuthErrorCodeIn(authErrorCode_1, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByPostMediaCodeIsNullAndAuthErrorCodeIn(authErrorCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PostAuthLogHistory> mediaCodeEq(String postMediaCode_0, int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("mediaCodeEq " + dbName + " : " + "postMediaCode_0 : " + postMediaCode_0 + " propDate_1 : " +  propDate_1 + " propDate_2 : " +  propDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByPostMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(postMediaCode_0, propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByPostMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(postMediaCode_0, propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByPostMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(postMediaCode_0, propDate_1, propDate_2, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByPostMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(postMediaCode_0, propDate_1, propDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PostAuthLogHistory> mediaCodeIsnull(int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("mediaCodeIsnull " + dbName + " : " + "propDate_1 : " + propDate_1 + " propDate_2 : " +  propDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByPostMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByPostMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByPostMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByPostMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PostAuthLogHistory fileSeqFirst(int propDate_0, String authCode_1, int fileSeq_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("fileSeqFirst " + dbName + " : " + "propDate_0 : " + propDate_0 + " authCode_1 : " +  authCode_1 + " fileSeq_2 : " +  fileSeq_2);
    Optional<PostAuthLogHistory> postAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      postAuthLogHistoryT = postAuthLogHistoryReposDay.findTopByPropDateIsAndAuthCodeIsAndFileSeqIs(propDate_0, authCode_1, fileSeq_2);
    else if (dbName.equals(ContentName.onMon))
      postAuthLogHistoryT = postAuthLogHistoryReposMon.findTopByPropDateIsAndAuthCodeIsAndFileSeqIs(propDate_0, authCode_1, fileSeq_2);
    else if (dbName.equals(ContentName.onHist))
      postAuthLogHistoryT = postAuthLogHistoryReposHist.findTopByPropDateIsAndAuthCodeIsAndFileSeqIs(propDate_0, authCode_1, fileSeq_2);
    else 
      postAuthLogHistoryT = postAuthLogHistoryRepos.findTopByPropDateIsAndAuthCodeIsAndFileSeqIs(propDate_0, authCode_1, fileSeq_2);

    return postAuthLogHistoryT.isPresent() ? postAuthLogHistoryT.get() : null;
  }

  @Override
  public Slice<PostAuthLogHistory> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByCustNoIsAndFacmNoIsOrderByLogNoDescAuthCodeAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByCustNoIsAndFacmNoIsOrderByLogNoDescAuthCodeAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByCustNoIsAndFacmNoIsOrderByLogNoDescAuthCodeAsc(custNo_0, facmNo_1, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByCustNoIsAndFacmNoIsOrderByLogNoDescAuthCodeAsc(custNo_0, facmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PostAuthLogHistory repayAcctFirst(int custNo_0, String postDepCode_1, String repayAcct_2, String authCode_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("repayAcctFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " postDepCode_1 : " +  postDepCode_1 + " repayAcct_2 : " +  repayAcct_2 + " authCode_3 : " +  authCode_3);
    Optional<PostAuthLogHistory> postAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      postAuthLogHistoryT = postAuthLogHistoryReposDay.findTopByCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, postDepCode_1, repayAcct_2, authCode_3);
    else if (dbName.equals(ContentName.onMon))
      postAuthLogHistoryT = postAuthLogHistoryReposMon.findTopByCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, postDepCode_1, repayAcct_2, authCode_3);
    else if (dbName.equals(ContentName.onHist))
      postAuthLogHistoryT = postAuthLogHistoryReposHist.findTopByCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, postDepCode_1, repayAcct_2, authCode_3);
    else 
      postAuthLogHistoryT = postAuthLogHistoryRepos.findTopByCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, postDepCode_1, repayAcct_2, authCode_3);

    return postAuthLogHistoryT.isPresent() ? postAuthLogHistoryT.get() : null;
  }

  @Override
  public PostAuthLogHistory facmNoBFirst(int custNo_0, int facmNo_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("facmNoBFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    Optional<PostAuthLogHistory> postAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      postAuthLogHistoryT = postAuthLogHistoryReposDay.findTopByCustNoIsAndFacmNoIsOrderByLogNoDescAuthCodeAsc(custNo_0, facmNo_1);
    else if (dbName.equals(ContentName.onMon))
      postAuthLogHistoryT = postAuthLogHistoryReposMon.findTopByCustNoIsAndFacmNoIsOrderByLogNoDescAuthCodeAsc(custNo_0, facmNo_1);
    else if (dbName.equals(ContentName.onHist))
      postAuthLogHistoryT = postAuthLogHistoryReposHist.findTopByCustNoIsAndFacmNoIsOrderByLogNoDescAuthCodeAsc(custNo_0, facmNo_1);
    else 
      postAuthLogHistoryT = postAuthLogHistoryRepos.findTopByCustNoIsAndFacmNoIsOrderByLogNoDescAuthCodeAsc(custNo_0, facmNo_1);

    return postAuthLogHistoryT.isPresent() ? postAuthLogHistoryT.get() : null;
  }

  @Override
  public PostAuthLogHistory facmNoCFirst(int custNo_0, int facmNo_1, String authCode_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("facmNoCFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " authCode_2 : " +  authCode_2);
    Optional<PostAuthLogHistory> postAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      postAuthLogHistoryT = postAuthLogHistoryReposDay.findTopByCustNoIsAndFacmNoIsAndAuthCodeIsOrderByLogNoDesc(custNo_0, facmNo_1, authCode_2);
    else if (dbName.equals(ContentName.onMon))
      postAuthLogHistoryT = postAuthLogHistoryReposMon.findTopByCustNoIsAndFacmNoIsAndAuthCodeIsOrderByLogNoDesc(custNo_0, facmNo_1, authCode_2);
    else if (dbName.equals(ContentName.onHist))
      postAuthLogHistoryT = postAuthLogHistoryReposHist.findTopByCustNoIsAndFacmNoIsAndAuthCodeIsOrderByLogNoDesc(custNo_0, facmNo_1, authCode_2);
    else 
      postAuthLogHistoryT = postAuthLogHistoryRepos.findTopByCustNoIsAndFacmNoIsAndAuthCodeIsOrderByLogNoDesc(custNo_0, facmNo_1, authCode_2);

    return postAuthLogHistoryT.isPresent() ? postAuthLogHistoryT.get() : null;
  }

  @Override
  public PostAuthLogHistory authLogFirst(int authCreateDate_0, String authApplCode_1, int custNo_2, String postDepCode_3, String repayAcct_4, String authCode_5, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("authLogFirst " + dbName + " : " + "authCreateDate_0 : " + authCreateDate_0 + " authApplCode_1 : " +  authApplCode_1 + " custNo_2 : " +  custNo_2 + " postDepCode_3 : " +  postDepCode_3 + " repayAcct_4 : " +  repayAcct_4 + " authCode_5 : " +  authCode_5);
    Optional<PostAuthLogHistory> postAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      postAuthLogHistoryT = postAuthLogHistoryReposDay.findTopByAuthCreateDateIsAndAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByLogNoDesc(authCreateDate_0, authApplCode_1, custNo_2, postDepCode_3, repayAcct_4, authCode_5);
    else if (dbName.equals(ContentName.onMon))
      postAuthLogHistoryT = postAuthLogHistoryReposMon.findTopByAuthCreateDateIsAndAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByLogNoDesc(authCreateDate_0, authApplCode_1, custNo_2, postDepCode_3, repayAcct_4, authCode_5);
    else if (dbName.equals(ContentName.onHist))
      postAuthLogHistoryT = postAuthLogHistoryReposHist.findTopByAuthCreateDateIsAndAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByLogNoDesc(authCreateDate_0, authApplCode_1, custNo_2, postDepCode_3, repayAcct_4, authCode_5);
    else 
      postAuthLogHistoryT = postAuthLogHistoryRepos.findTopByAuthCreateDateIsAndAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByLogNoDesc(authCreateDate_0, authApplCode_1, custNo_2, postDepCode_3, repayAcct_4, authCode_5);

    return postAuthLogHistoryT.isPresent() ? postAuthLogHistoryT.get() : null;
  }

  @Override
  public Slice<PostAuthLogHistory> facmNoAuthCodeEq(int custNo_0, int facmNo_1, String authCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PostAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmNoAuthCodeEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " authCode_2 : " +  authCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = postAuthLogHistoryReposDay.findAllByCustNoIsAndFacmNoIsAndAuthCodeIs(custNo_0, facmNo_1, authCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = postAuthLogHistoryReposMon.findAllByCustNoIsAndFacmNoIsAndAuthCodeIs(custNo_0, facmNo_1, authCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = postAuthLogHistoryReposHist.findAllByCustNoIsAndFacmNoIsAndAuthCodeIs(custNo_0, facmNo_1, authCode_2, pageable);
    else 
      slice = postAuthLogHistoryRepos.findAllByCustNoIsAndFacmNoIsAndAuthCodeIs(custNo_0, facmNo_1, authCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PostAuthLogHistory holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<PostAuthLogHistory> postAuthLogHistory = null;
    if (dbName.equals(ContentName.onDay))
      postAuthLogHistory = postAuthLogHistoryReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      postAuthLogHistory = postAuthLogHistoryReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      postAuthLogHistory = postAuthLogHistoryReposHist.findByLogNo(logNo);
    else 
      postAuthLogHistory = postAuthLogHistoryRepos.findByLogNo(logNo);
    return postAuthLogHistory.isPresent() ? postAuthLogHistory.get() : null;
  }

  @Override
  public PostAuthLogHistory holdById(PostAuthLogHistory postAuthLogHistory, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + postAuthLogHistory.getLogNo());
    Optional<PostAuthLogHistory> postAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      postAuthLogHistoryT = postAuthLogHistoryReposDay.findByLogNo(postAuthLogHistory.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      postAuthLogHistoryT = postAuthLogHistoryReposMon.findByLogNo(postAuthLogHistory.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      postAuthLogHistoryT = postAuthLogHistoryReposHist.findByLogNo(postAuthLogHistory.getLogNo());
    else 
      postAuthLogHistoryT = postAuthLogHistoryRepos.findByLogNo(postAuthLogHistory.getLogNo());
    return postAuthLogHistoryT.isPresent() ? postAuthLogHistoryT.get() : null;
  }

  @Override
  public PostAuthLogHistory insert(PostAuthLogHistory postAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + postAuthLogHistory.getLogNo());
    if (this.findById(postAuthLogHistory.getLogNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      postAuthLogHistory.setCreateEmpNo(empNot);

    if(postAuthLogHistory.getLastUpdateEmpNo() == null || postAuthLogHistory.getLastUpdateEmpNo().isEmpty())
      postAuthLogHistory.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return postAuthLogHistoryReposDay.saveAndFlush(postAuthLogHistory);	
    else if (dbName.equals(ContentName.onMon))
      return postAuthLogHistoryReposMon.saveAndFlush(postAuthLogHistory);
    else if (dbName.equals(ContentName.onHist))
      return postAuthLogHistoryReposHist.saveAndFlush(postAuthLogHistory);
    else 
    return postAuthLogHistoryRepos.saveAndFlush(postAuthLogHistory);
  }

  @Override
  public PostAuthLogHistory update(PostAuthLogHistory postAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + postAuthLogHistory.getLogNo());
    if (!empNot.isEmpty())
      postAuthLogHistory.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return postAuthLogHistoryReposDay.saveAndFlush(postAuthLogHistory);	
    else if (dbName.equals(ContentName.onMon))
      return postAuthLogHistoryReposMon.saveAndFlush(postAuthLogHistory);
    else if (dbName.equals(ContentName.onHist))
      return postAuthLogHistoryReposHist.saveAndFlush(postAuthLogHistory);
    else 
    return postAuthLogHistoryRepos.saveAndFlush(postAuthLogHistory);
  }

  @Override
  public PostAuthLogHistory update2(PostAuthLogHistory postAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + postAuthLogHistory.getLogNo());
    if (!empNot.isEmpty())
      postAuthLogHistory.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      postAuthLogHistoryReposDay.saveAndFlush(postAuthLogHistory);	
    else if (dbName.equals(ContentName.onMon))
      postAuthLogHistoryReposMon.saveAndFlush(postAuthLogHistory);
    else if (dbName.equals(ContentName.onHist))
        postAuthLogHistoryReposHist.saveAndFlush(postAuthLogHistory);
    else 
      postAuthLogHistoryRepos.saveAndFlush(postAuthLogHistory);	
    return this.findById(postAuthLogHistory.getLogNo());
  }

  @Override
  public void delete(PostAuthLogHistory postAuthLogHistory, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + postAuthLogHistory.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      postAuthLogHistoryReposDay.delete(postAuthLogHistory);	
      postAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      postAuthLogHistoryReposMon.delete(postAuthLogHistory);	
      postAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      postAuthLogHistoryReposHist.delete(postAuthLogHistory);
      postAuthLogHistoryReposHist.flush();
    }
    else {
      postAuthLogHistoryRepos.delete(postAuthLogHistory);
      postAuthLogHistoryRepos.flush();
    }
   }

  @Override
  public void insertAll(List<PostAuthLogHistory> postAuthLogHistory, TitaVo... titaVo) throws DBException {
    if (postAuthLogHistory == null || postAuthLogHistory.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (PostAuthLogHistory t : postAuthLogHistory){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      postAuthLogHistory = postAuthLogHistoryReposDay.saveAll(postAuthLogHistory);	
      postAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      postAuthLogHistory = postAuthLogHistoryReposMon.saveAll(postAuthLogHistory);	
      postAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      postAuthLogHistory = postAuthLogHistoryReposHist.saveAll(postAuthLogHistory);
      postAuthLogHistoryReposHist.flush();
    }
    else {
      postAuthLogHistory = postAuthLogHistoryRepos.saveAll(postAuthLogHistory);
      postAuthLogHistoryRepos.flush();
    }
    }

  @Override
  public void updateAll(List<PostAuthLogHistory> postAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (postAuthLogHistory == null || postAuthLogHistory.size() == 0)
      throw new DBException(6);

    for (PostAuthLogHistory t : postAuthLogHistory) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      postAuthLogHistory = postAuthLogHistoryReposDay.saveAll(postAuthLogHistory);	
      postAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      postAuthLogHistory = postAuthLogHistoryReposMon.saveAll(postAuthLogHistory);	
      postAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      postAuthLogHistory = postAuthLogHistoryReposHist.saveAll(postAuthLogHistory);
      postAuthLogHistoryReposHist.flush();
    }
    else {
      postAuthLogHistory = postAuthLogHistoryRepos.saveAll(postAuthLogHistory);
      postAuthLogHistoryRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<PostAuthLogHistory> postAuthLogHistory, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (postAuthLogHistory == null || postAuthLogHistory.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      postAuthLogHistoryReposDay.deleteAll(postAuthLogHistory);	
      postAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      postAuthLogHistoryReposMon.deleteAll(postAuthLogHistory);	
      postAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      postAuthLogHistoryReposHist.deleteAll(postAuthLogHistory);
      postAuthLogHistoryReposHist.flush();
    }
    else {
      postAuthLogHistoryRepos.deleteAll(postAuthLogHistory);
      postAuthLogHistoryRepos.flush();
    }
  }

}
