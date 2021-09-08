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
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.domain.PostAuthLogId;
import com.st1.itx.db.repository.online.PostAuthLogRepository;
import com.st1.itx.db.repository.day.PostAuthLogRepositoryDay;
import com.st1.itx.db.repository.mon.PostAuthLogRepositoryMon;
import com.st1.itx.db.repository.hist.PostAuthLogRepositoryHist;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("postAuthLogService")
@Repository
public class PostAuthLogServiceImpl extends ASpringJpaParm implements PostAuthLogService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private PostAuthLogRepository postAuthLogRepos;

	@Autowired
	private PostAuthLogRepositoryDay postAuthLogReposDay;

	@Autowired
	private PostAuthLogRepositoryMon postAuthLogReposMon;

	@Autowired
	private PostAuthLogRepositoryHist postAuthLogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(postAuthLogRepos);
		org.junit.Assert.assertNotNull(postAuthLogReposDay);
		org.junit.Assert.assertNotNull(postAuthLogReposMon);
		org.junit.Assert.assertNotNull(postAuthLogReposHist);
	}

	@Override
	public PostAuthLog findById(PostAuthLogId postAuthLogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + postAuthLogId);
		Optional<PostAuthLog> postAuthLog = null;
		if (dbName.equals(ContentName.onDay))
			postAuthLog = postAuthLogReposDay.findById(postAuthLogId);
		else if (dbName.equals(ContentName.onMon))
			postAuthLog = postAuthLogReposMon.findById(postAuthLogId);
		else if (dbName.equals(ContentName.onHist))
			postAuthLog = postAuthLogReposHist.findById(postAuthLogId);
		else
			postAuthLog = postAuthLogRepos.findById(postAuthLogId);
		PostAuthLog obj = postAuthLog.isPresent() ? postAuthLog.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<PostAuthLog> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AuthCreateDate", "AuthApplCode", "CustNo", "PostDepCode", "RepayAcct", "AuthCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAll(pageable);
		else
			slice = postAuthLogRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PostAuthLog> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByCustNoIs(custNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByCustNoIs(custNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByCustNoIs(custNo_0, pageable);
		else
			slice = postAuthLogRepos.findAllByCustNoIs(custNo_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PostAuthLog> repayAcctEq(String repayAcct_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("repayAcctEq " + dbName + " : " + "repayAcct_0 : " + repayAcct_0);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByRepayAcctIs(repayAcct_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByRepayAcctIs(repayAcct_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByRepayAcctIs(repayAcct_0, pageable);
		else
			slice = postAuthLogRepos.findAllByRepayAcctIs(repayAcct_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PostAuthLog> repayAcctLike(String repayAcct_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("repayAcctLike " + dbName + " : " + "repayAcct_0 : " + repayAcct_0);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByRepayAcctLike(repayAcct_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByRepayAcctLike(repayAcct_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByRepayAcctLike(repayAcct_0, pageable);
		else
			slice = postAuthLogRepos.findAllByRepayAcctLike(repayAcct_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PostAuthLog> authCreateDateEq(int authCreateDate_0, int authCreateDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("authCreateDateEq " + dbName + " : " + "authCreateDate_0 : " + authCreateDate_0 + " authCreateDate_1 : " + authCreateDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);
		else
			slice = postAuthLogRepos.findAllByAuthCreateDateGreaterThanEqualAndAuthCreateDateLessThanEqual(authCreateDate_0, authCreateDate_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PostAuthLog> propDateEq(int propDate_0, int propDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("propDateEq " + dbName + " : " + "propDate_0 : " + propDate_0 + " propDate_1 : " + propDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(propDate_0, propDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(propDate_0, propDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(propDate_0, propDate_1, pageable);
		else
			slice = postAuthLogRepos.findAllByPropDateGreaterThanEqualAndPropDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(propDate_0, propDate_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PostAuthLog> retrDateEq(int retrDate_0, int retrDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("retrDateEq " + dbName + " : " + "retrDate_0 : " + retrDate_0 + " retrDate_1 : " + retrDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(retrDate_0, retrDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(retrDate_0, retrDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(retrDate_0, retrDate_1, pageable);
		else
			slice = postAuthLogRepos.findAllByRetrDateGreaterThanEqualAndRetrDateLessThanEqualOrderByAuthCreateDateDescCreateDateDesc(retrDate_0, retrDate_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public PostAuthLog acctSeqFirst(String custId_0, String postDepCode_1, int custNo_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("acctSeqFirst " + dbName + " : " + "custId_0 : " + custId_0 + " postDepCode_1 : " + postDepCode_1 + " custNo_2 : " + custNo_2);
		Optional<PostAuthLog> postAuthLogT = null;
		if (dbName.equals(ContentName.onDay))
			postAuthLogT = postAuthLogReposDay.findTopByCustIdIsAndPostDepCodeIsAndCustNoIsOrderByRepayAcctSeqDesc(custId_0, postDepCode_1, custNo_2);
		else if (dbName.equals(ContentName.onMon))
			postAuthLogT = postAuthLogReposMon.findTopByCustIdIsAndPostDepCodeIsAndCustNoIsOrderByRepayAcctSeqDesc(custId_0, postDepCode_1, custNo_2);
		else if (dbName.equals(ContentName.onHist))
			postAuthLogT = postAuthLogReposHist.findTopByCustIdIsAndPostDepCodeIsAndCustNoIsOrderByRepayAcctSeqDesc(custId_0, postDepCode_1, custNo_2);
		else
			postAuthLogT = postAuthLogRepos.findTopByCustIdIsAndPostDepCodeIsAndCustNoIsOrderByRepayAcctSeqDesc(custId_0, postDepCode_1, custNo_2);
		return postAuthLogT.isPresent() ? postAuthLogT.get() : null;
	}

	@Override
	public PostAuthLog pkFacmNoFirst(String authApplCode_0, int custNo_1, String postDepCode_2, String repayAcct_3, String authCode_4, int facmNo_5, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("pkFacmNoFirst " + dbName + " : " + "authApplCode_0 : " + authApplCode_0 + " custNo_1 : " + custNo_1 + " postDepCode_2 : " + postDepCode_2 + " repayAcct_3 : " + repayAcct_3
				+ " authCode_4 : " + authCode_4 + " facmNo_5 : " + facmNo_5);
		Optional<PostAuthLog> postAuthLogT = null;
		if (dbName.equals(ContentName.onDay))
			postAuthLogT = postAuthLogReposDay.findTopByAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsAndFacmNoIsOrderByRepayAcctSeqDesc(authApplCode_0, custNo_1, postDepCode_2,
					repayAcct_3, authCode_4, facmNo_5);
		else if (dbName.equals(ContentName.onMon))
			postAuthLogT = postAuthLogReposMon.findTopByAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsAndFacmNoIsOrderByRepayAcctSeqDesc(authApplCode_0, custNo_1, postDepCode_2,
					repayAcct_3, authCode_4, facmNo_5);
		else if (dbName.equals(ContentName.onHist))
			postAuthLogT = postAuthLogReposHist.findTopByAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsAndFacmNoIsOrderByRepayAcctSeqDesc(authApplCode_0, custNo_1, postDepCode_2,
					repayAcct_3, authCode_4, facmNo_5);
		else
			postAuthLogT = postAuthLogRepos.findTopByAuthApplCodeIsAndCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsAndFacmNoIsOrderByRepayAcctSeqDesc(authApplCode_0, custNo_1, postDepCode_2,
					repayAcct_3, authCode_4, facmNo_5);
		return postAuthLogT.isPresent() ? postAuthLogT.get() : null;
	}

	@Override
	public Slice<PostAuthLog> l4041ARg(List<String> authErrorCode_1, int custNo_2, int propDate_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("l4041ARg " + dbName + " : " + "authErrorCode_1 : " + authErrorCode_1 + " custNo_2 : " + custNo_2 + " propDate_3 : " + propDate_3);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIsAndPropDateIs(authErrorCode_1, custNo_2, propDate_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIsAndPropDateIs(authErrorCode_1, custNo_2, propDate_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIsAndPropDateIs(authErrorCode_1, custNo_2, propDate_3, pageable);
		else
			slice = postAuthLogRepos.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIsAndPropDateIs(authErrorCode_1, custNo_2, propDate_3, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PostAuthLog> l4041BRg(List<String> authErrorCode_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("l4041BRg " + dbName + " : " + "authErrorCode_1 : " + authErrorCode_1 + " propDate_2 : " + propDate_2);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndPropDateIs(authErrorCode_1, propDate_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndPropDateIs(authErrorCode_1, propDate_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndPropDateIs(authErrorCode_1, propDate_2, pageable);
		else
			slice = postAuthLogRepos.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndPropDateIs(authErrorCode_1, propDate_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PostAuthLog> l4041CRg(List<String> authErrorCode_1, int custNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("l4041CRg " + dbName + " : " + "authErrorCode_1 : " + authErrorCode_1 + " custNo_2 : " + custNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIs(authErrorCode_1, custNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIs(authErrorCode_1, custNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIs(authErrorCode_1, custNo_2, pageable);
		else
			slice = postAuthLogRepos.findAllByPostMediaCodeIsNullAndAuthErrorCodeInAndCustNoIs(authErrorCode_1, custNo_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PostAuthLog> l4041DRg(List<String> authErrorCode_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("l4041DRg " + dbName + " : " + "authErrorCode_1 : " + authErrorCode_1);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByPostMediaCodeIsNullAndAuthErrorCodeIn(authErrorCode_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByPostMediaCodeIsNullAndAuthErrorCodeIn(authErrorCode_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByPostMediaCodeIsNullAndAuthErrorCodeIn(authErrorCode_1, pageable);
		else
			slice = postAuthLogRepos.findAllByPostMediaCodeIsNullAndAuthErrorCodeIn(authErrorCode_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PostAuthLog> mediaCodeEq(String postMediaCode_0, int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("mediaCodeEq " + dbName + " : " + "postMediaCode_0 : " + postMediaCode_0 + " propDate_1 : " + propDate_1 + " propDate_2 : " + propDate_2);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByPostMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(postMediaCode_0, propDate_1, propDate_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByPostMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(postMediaCode_0, propDate_1, propDate_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByPostMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(postMediaCode_0, propDate_1, propDate_2, pageable);
		else
			slice = postAuthLogRepos.findAllByPostMediaCodeIsAndPropDateGreaterThanEqualAndPropDateLessThanEqual(postMediaCode_0, propDate_1, propDate_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PostAuthLog> mediaCodeIsnull(int propDate_1, int propDate_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("mediaCodeIsnull " + dbName + " : " + "propDate_1 : " + propDate_1 + " propDate_2 : " + propDate_2);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByPostMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByPostMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByPostMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);
		else
			slice = postAuthLogRepos.findAllByPostMediaCodeIsNullAndPropDateGreaterThanEqualAndPropDateLessThanEqual(propDate_1, propDate_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public PostAuthLog fileSeqFirst(int propDate_0, String authCode_1, int fileSeq_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("fileSeqFirst " + dbName + " : " + "propDate_0 : " + propDate_0 + " authCode_1 : " + authCode_1 + " fileSeq_2 : " + fileSeq_2);
		Optional<PostAuthLog> postAuthLogT = null;
		if (dbName.equals(ContentName.onDay))
			postAuthLogT = postAuthLogReposDay.findTopByPropDateIsAndAuthCodeIsAndFileSeqIs(propDate_0, authCode_1, fileSeq_2);
		else if (dbName.equals(ContentName.onMon))
			postAuthLogT = postAuthLogReposMon.findTopByPropDateIsAndAuthCodeIsAndFileSeqIs(propDate_0, authCode_1, fileSeq_2);
		else if (dbName.equals(ContentName.onHist))
			postAuthLogT = postAuthLogReposHist.findTopByPropDateIsAndAuthCodeIsAndFileSeqIs(propDate_0, authCode_1, fileSeq_2);
		else
			postAuthLogT = postAuthLogRepos.findTopByPropDateIsAndAuthCodeIsAndFileSeqIs(propDate_0, authCode_1, fileSeq_2);
		return postAuthLogT.isPresent() ? postAuthLogT.get() : null;
	}

	@Override
	public Slice<PostAuthLog> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PostAuthLog> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("facmNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = postAuthLogReposDay.findAllByCustNoIsAndFacmNoIsOrderByRepayAcctSeqDesc(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = postAuthLogReposMon.findAllByCustNoIsAndFacmNoIsOrderByRepayAcctSeqDesc(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = postAuthLogReposHist.findAllByCustNoIsAndFacmNoIsOrderByRepayAcctSeqDesc(custNo_0, facmNo_1, pageable);
		else
			slice = postAuthLogRepos.findAllByCustNoIsAndFacmNoIsOrderByRepayAcctSeqDesc(custNo_0, facmNo_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public PostAuthLog repayAcctFirst(int custNo_0, String postDepCode_1, String repayAcct_2, String authCode_3, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("repayAcctFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " postDepCode_1 : " + postDepCode_1 + " repayAcct_2 : " + repayAcct_2 + " authCode_3 : " + authCode_3);
		Optional<PostAuthLog> postAuthLogT = null;
		if (dbName.equals(ContentName.onDay))
			postAuthLogT = postAuthLogReposDay.findTopByCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, postDepCode_1, repayAcct_2, authCode_3);
		else if (dbName.equals(ContentName.onMon))
			postAuthLogT = postAuthLogReposMon.findTopByCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, postDepCode_1, repayAcct_2, authCode_3);
		else if (dbName.equals(ContentName.onHist))
			postAuthLogT = postAuthLogReposHist.findTopByCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, postDepCode_1, repayAcct_2, authCode_3);
		else
			postAuthLogT = postAuthLogRepos.findTopByCustNoIsAndPostDepCodeIsAndRepayAcctIsAndAuthCodeIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, postDepCode_1, repayAcct_2, authCode_3);
		return postAuthLogT.isPresent() ? postAuthLogT.get() : null;
	}

	@Override
	public PostAuthLog facmNoBFirst(int custNo_0, int facmNo_1, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("facmNoBFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1);
		Optional<PostAuthLog> postAuthLogT = null;
		if (dbName.equals(ContentName.onDay))
			postAuthLogT = postAuthLogReposDay.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);
		else if (dbName.equals(ContentName.onMon))
			postAuthLogT = postAuthLogReposMon.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);
		else if (dbName.equals(ContentName.onHist))
			postAuthLogT = postAuthLogReposHist.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);
		else
			postAuthLogT = postAuthLogRepos.findTopByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1);
		return postAuthLogT.isPresent() ? postAuthLogT.get() : null;
	}

	@Override
	public PostAuthLog holdById(PostAuthLogId postAuthLogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + postAuthLogId);
		Optional<PostAuthLog> postAuthLog = null;
		if (dbName.equals(ContentName.onDay))
			postAuthLog = postAuthLogReposDay.findByPostAuthLogId(postAuthLogId);
		else if (dbName.equals(ContentName.onMon))
			postAuthLog = postAuthLogReposMon.findByPostAuthLogId(postAuthLogId);
		else if (dbName.equals(ContentName.onHist))
			postAuthLog = postAuthLogReposHist.findByPostAuthLogId(postAuthLogId);
		else
			postAuthLog = postAuthLogRepos.findByPostAuthLogId(postAuthLogId);
		return postAuthLog.isPresent() ? postAuthLog.get() : null;
	}

	@Override
	public PostAuthLog holdById(PostAuthLog postAuthLog, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + postAuthLog.getPostAuthLogId());
		Optional<PostAuthLog> postAuthLogT = null;
		if (dbName.equals(ContentName.onDay))
			postAuthLogT = postAuthLogReposDay.findByPostAuthLogId(postAuthLog.getPostAuthLogId());
		else if (dbName.equals(ContentName.onMon))
			postAuthLogT = postAuthLogReposMon.findByPostAuthLogId(postAuthLog.getPostAuthLogId());
		else if (dbName.equals(ContentName.onHist))
			postAuthLogT = postAuthLogReposHist.findByPostAuthLogId(postAuthLog.getPostAuthLogId());
		else
			postAuthLogT = postAuthLogRepos.findByPostAuthLogId(postAuthLog.getPostAuthLogId());
		return postAuthLogT.isPresent() ? postAuthLogT.get() : null;
	}

	@Override
	public PostAuthLog insert(PostAuthLog postAuthLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + postAuthLog.getPostAuthLogId());
		if (this.findById(postAuthLog.getPostAuthLogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			postAuthLog.setCreateEmpNo(empNot);

		if (postAuthLog.getLastUpdateEmpNo() == null || postAuthLog.getLastUpdateEmpNo().isEmpty())
			postAuthLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return postAuthLogReposDay.saveAndFlush(postAuthLog);
		else if (dbName.equals(ContentName.onMon))
			return postAuthLogReposMon.saveAndFlush(postAuthLog);
		else if (dbName.equals(ContentName.onHist))
			return postAuthLogReposHist.saveAndFlush(postAuthLog);
		else
			return postAuthLogRepos.saveAndFlush(postAuthLog);
	}

	@Override
	public PostAuthLog update(PostAuthLog postAuthLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + postAuthLog.getPostAuthLogId());
		if (!empNot.isEmpty())
			postAuthLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return postAuthLogReposDay.saveAndFlush(postAuthLog);
		else if (dbName.equals(ContentName.onMon))
			return postAuthLogReposMon.saveAndFlush(postAuthLog);
		else if (dbName.equals(ContentName.onHist))
			return postAuthLogReposHist.saveAndFlush(postAuthLog);
		else
			return postAuthLogRepos.saveAndFlush(postAuthLog);
	}

	@Override
	public PostAuthLog update2(PostAuthLog postAuthLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + postAuthLog.getPostAuthLogId());
		if (!empNot.isEmpty())
			postAuthLog.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			postAuthLogReposDay.saveAndFlush(postAuthLog);
		else if (dbName.equals(ContentName.onMon))
			postAuthLogReposMon.saveAndFlush(postAuthLog);
		else if (dbName.equals(ContentName.onHist))
			postAuthLogReposHist.saveAndFlush(postAuthLog);
		else
			postAuthLogRepos.saveAndFlush(postAuthLog);
		return this.findById(postAuthLog.getPostAuthLogId());
	}

	@Override
	public void delete(PostAuthLog postAuthLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + postAuthLog.getPostAuthLogId());
		if (dbName.equals(ContentName.onDay)) {
			postAuthLogReposDay.delete(postAuthLog);
			postAuthLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			postAuthLogReposMon.delete(postAuthLog);
			postAuthLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			postAuthLogReposHist.delete(postAuthLog);
			postAuthLogReposHist.flush();
		} else {
			postAuthLogRepos.delete(postAuthLog);
			postAuthLogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<PostAuthLog> postAuthLog, TitaVo... titaVo) throws DBException {
		if (postAuthLog == null || postAuthLog.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (PostAuthLog t : postAuthLog) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			postAuthLog = postAuthLogReposDay.saveAll(postAuthLog);
			postAuthLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			postAuthLog = postAuthLogReposMon.saveAll(postAuthLog);
			postAuthLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			postAuthLog = postAuthLogReposHist.saveAll(postAuthLog);
			postAuthLogReposHist.flush();
		} else {
			postAuthLog = postAuthLogRepos.saveAll(postAuthLog);
			postAuthLogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<PostAuthLog> postAuthLog, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (postAuthLog == null || postAuthLog.size() == 0)
			throw new DBException(6);

		for (PostAuthLog t : postAuthLog)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			postAuthLog = postAuthLogReposDay.saveAll(postAuthLog);
			postAuthLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			postAuthLog = postAuthLogReposMon.saveAll(postAuthLog);
			postAuthLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			postAuthLog = postAuthLogReposHist.saveAll(postAuthLog);
			postAuthLogReposHist.flush();
		} else {
			postAuthLog = postAuthLogRepos.saveAll(postAuthLog);
			postAuthLogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<PostAuthLog> postAuthLog, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (postAuthLog == null || postAuthLog.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			postAuthLogReposDay.deleteAll(postAuthLog);
			postAuthLogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			postAuthLogReposMon.deleteAll(postAuthLog);
			postAuthLogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			postAuthLogReposHist.deleteAll(postAuthLog);
			postAuthLogReposHist.flush();
		} else {
			postAuthLogRepos.deleteAll(postAuthLog);
			postAuthLogRepos.flush();
		}
	}

}
