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
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.repository.online.PfItDetailRepository;
import com.st1.itx.db.repository.day.PfItDetailRepositoryDay;
import com.st1.itx.db.repository.mon.PfItDetailRepositoryMon;
import com.st1.itx.db.repository.hist.PfItDetailRepositoryHist;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfItDetailService")
@Repository
public class PfItDetailServiceImpl extends ASpringJpaParm implements PfItDetailService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private PfItDetailRepository pfItDetailRepos;

	@Autowired
	private PfItDetailRepositoryDay pfItDetailReposDay;

	@Autowired
	private PfItDetailRepositoryMon pfItDetailReposMon;

	@Autowired
	private PfItDetailRepositoryHist pfItDetailReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(pfItDetailRepos);
		org.junit.Assert.assertNotNull(pfItDetailReposDay);
		org.junit.Assert.assertNotNull(pfItDetailReposMon);
		org.junit.Assert.assertNotNull(pfItDetailReposHist);
	}

	@Override
	public PfItDetail findById(Long logNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + logNo);
		Optional<PfItDetail> pfItDetail = null;
		if (dbName.equals(ContentName.onDay))
			pfItDetail = pfItDetailReposDay.findById(logNo);
		else if (dbName.equals(ContentName.onMon))
			pfItDetail = pfItDetailReposMon.findById(logNo);
		else if (dbName.equals(ContentName.onHist))
			pfItDetail = pfItDetailReposHist.findById(logNo);
		else
			pfItDetail = pfItDetailRepos.findById(logNo);
		PfItDetail obj = pfItDetail.isPresent() ? pfItDetail.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<PfItDetail> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfItDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = pfItDetailReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfItDetailReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfItDetailReposHist.findAll(pageable);
		else
			slice = pfItDetailRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfItDetail> findFacmNoRange(int custNo_0, int facmNo_1, int facmNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfItDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findFacmNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " facmNo_2 : " + facmNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = pfItDetailReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfItDetailReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfItDetailReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);
		else
			slice = pfItDetailRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, facmNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfItDetail> findByWorkMonth(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfItDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByWorkMonth " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " workMonth_1 : " + workMonth_1);
		if (dbName.equals(ContentName.onDay))
			slice = pfItDetailReposDay.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfItDetailReposMon.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfItDetailReposHist.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);
		else
			slice = pfItDetailRepos.findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(workMonth_0, workMonth_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfItDetail> findByCustNoAndFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfItDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByCustNoAndFacmNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = pfItDetailReposDay.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfItDetailReposMon.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfItDetailReposHist.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, pageable);
		else
			slice = pfItDetailRepos.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, facmNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfItDetail> findByCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfItDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = pfItDetailReposDay.findAllByCustNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfItDetailReposMon.findAllByCustNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfItDetailReposHist.findAllByCustNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, pageable);
		else
			slice = pfItDetailRepos.findAllByCustNoIsOrderByCustNoAscFacmNoAscBormNoAsc(custNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfItDetail> findByDrawdownDate(int drawdownDate_0, int drawdownDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfItDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByDrawdownDate " + dbName + " : " + "drawdownDate_0 : " + drawdownDate_0 + " drawdownDate_1 : " + drawdownDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = pfItDetailReposDay.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(drawdownDate_0, drawdownDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfItDetailReposMon.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(drawdownDate_0, drawdownDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfItDetailReposHist.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(drawdownDate_0, drawdownDate_1, pageable);
		else
			slice = pfItDetailRepos.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(drawdownDate_0, drawdownDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfItDetail> findByPerfDate(int perfDate_0, int perfDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfItDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByPerfDate " + dbName + " : " + "perfDate_0 : " + perfDate_0 + " perfDate_1 : " + perfDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = pfItDetailReposDay.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfItDetailReposMon.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfItDetailReposHist.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);
		else
			slice = pfItDetailRepos.findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(perfDate_0, perfDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfItDetail> findByRewardDate(int rewardDate_0, int mediaFg_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfItDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByRewardDate " + dbName + " : " + "rewardDate_0 : " + rewardDate_0 + " mediaFg_1 : " + mediaFg_1);
		if (dbName.equals(ContentName.onDay))
			slice = pfItDetailReposDay.findAllByRewardDateIsAndMediaFgIsOrderByIntroducerAscCustNoAscFacmNoAscBormNoAsc(rewardDate_0, mediaFg_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfItDetailReposMon.findAllByRewardDateIsAndMediaFgIsOrderByIntroducerAscCustNoAscFacmNoAscBormNoAsc(rewardDate_0, mediaFg_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfItDetailReposHist.findAllByRewardDateIsAndMediaFgIsOrderByIntroducerAscCustNoAscFacmNoAscBormNoAsc(rewardDate_0, mediaFg_1, pageable);
		else
			slice = pfItDetailRepos.findAllByRewardDateIsAndMediaFgIsOrderByIntroducerAscCustNoAscFacmNoAscBormNoAsc(rewardDate_0, mediaFg_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public PfItDetail findByTxFirst(int custNo_0, int facmNo_1, int bormNo_2, int perfDate_3, int repayType_4, String pieceCode_5, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findByTxFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " bormNo_2 : " + bormNo_2 + " perfDate_3 : " + perfDate_3 + " repayType_4 : " + repayType_4
				+ " pieceCode_5 : " + pieceCode_5);
		Optional<PfItDetail> pfItDetailT = null;
		if (dbName.equals(ContentName.onDay))
			pfItDetailT = pfItDetailReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);
		else if (dbName.equals(ContentName.onMon))
			pfItDetailT = pfItDetailReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);
		else if (dbName.equals(ContentName.onHist))
			pfItDetailT = pfItDetailReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);
		else
			pfItDetailT = pfItDetailRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(custNo_0, facmNo_1, bormNo_2, perfDate_3, repayType_4, pieceCode_5);

		return pfItDetailT.isPresent() ? pfItDetailT.get() : null;
	}

	@Override
	public PfItDetail findBormNoLatestFirst(int custNo_0, int facmNo_1, int bormNo_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findBormNoLatestFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " bormNo_2 : " + bormNo_2);
		Optional<PfItDetail> pfItDetailT = null;
		if (dbName.equals(ContentName.onDay))
			pfItDetailT = pfItDetailReposDay.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateDesc(custNo_0, facmNo_1, bormNo_2);
		else if (dbName.equals(ContentName.onMon))
			pfItDetailT = pfItDetailReposMon.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateDesc(custNo_0, facmNo_1, bormNo_2);
		else if (dbName.equals(ContentName.onHist))
			pfItDetailT = pfItDetailReposHist.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateDesc(custNo_0, facmNo_1, bormNo_2);
		else
			pfItDetailT = pfItDetailRepos.findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateDesc(custNo_0, facmNo_1, bormNo_2);

		return pfItDetailT.isPresent() ? pfItDetailT.get() : null;
	}

	@Override
	public Slice<PfItDetail> findBormNoEq(int custNo_0, int facmNo_1, int bormNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfItDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findBormNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " bormNo_2 : " + bormNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = pfItDetailReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfItDetailReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfItDetailReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);
		else
			slice = pfItDetailRepos.findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(custNo_0, facmNo_1, bormNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public PfItDetail holdById(Long logNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + logNo);
		Optional<PfItDetail> pfItDetail = null;
		if (dbName.equals(ContentName.onDay))
			pfItDetail = pfItDetailReposDay.findByLogNo(logNo);
		else if (dbName.equals(ContentName.onMon))
			pfItDetail = pfItDetailReposMon.findByLogNo(logNo);
		else if (dbName.equals(ContentName.onHist))
			pfItDetail = pfItDetailReposHist.findByLogNo(logNo);
		else
			pfItDetail = pfItDetailRepos.findByLogNo(logNo);
		return pfItDetail.isPresent() ? pfItDetail.get() : null;
	}

	@Override
	public PfItDetail holdById(PfItDetail pfItDetail, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + pfItDetail.getLogNo());
		Optional<PfItDetail> pfItDetailT = null;
		if (dbName.equals(ContentName.onDay))
			pfItDetailT = pfItDetailReposDay.findByLogNo(pfItDetail.getLogNo());
		else if (dbName.equals(ContentName.onMon))
			pfItDetailT = pfItDetailReposMon.findByLogNo(pfItDetail.getLogNo());
		else if (dbName.equals(ContentName.onHist))
			pfItDetailT = pfItDetailReposHist.findByLogNo(pfItDetail.getLogNo());
		else
			pfItDetailT = pfItDetailRepos.findByLogNo(pfItDetail.getLogNo());
		return pfItDetailT.isPresent() ? pfItDetailT.get() : null;
	}

	@Override
	public PfItDetail insert(PfItDetail pfItDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + pfItDetail.getLogNo());
		if (this.findById(pfItDetail.getLogNo(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			pfItDetail.setCreateEmpNo(empNot);

		if (pfItDetail.getLastUpdateEmpNo() == null || pfItDetail.getLastUpdateEmpNo().isEmpty())
			pfItDetail.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return pfItDetailReposDay.saveAndFlush(pfItDetail);
		else if (dbName.equals(ContentName.onMon))
			return pfItDetailReposMon.saveAndFlush(pfItDetail);
		else if (dbName.equals(ContentName.onHist))
			return pfItDetailReposHist.saveAndFlush(pfItDetail);
		else
			return pfItDetailRepos.saveAndFlush(pfItDetail);
	}

	@Override
	public PfItDetail update(PfItDetail pfItDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + pfItDetail.getLogNo());
		if (!empNot.isEmpty())
			pfItDetail.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return pfItDetailReposDay.saveAndFlush(pfItDetail);
		else if (dbName.equals(ContentName.onMon))
			return pfItDetailReposMon.saveAndFlush(pfItDetail);
		else if (dbName.equals(ContentName.onHist))
			return pfItDetailReposHist.saveAndFlush(pfItDetail);
		else
			return pfItDetailRepos.saveAndFlush(pfItDetail);
	}

	@Override
	public PfItDetail update2(PfItDetail pfItDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + pfItDetail.getLogNo());
		if (!empNot.isEmpty())
			pfItDetail.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			pfItDetailReposDay.saveAndFlush(pfItDetail);
		else if (dbName.equals(ContentName.onMon))
			pfItDetailReposMon.saveAndFlush(pfItDetail);
		else if (dbName.equals(ContentName.onHist))
			pfItDetailReposHist.saveAndFlush(pfItDetail);
		else
			pfItDetailRepos.saveAndFlush(pfItDetail);
		return this.findById(pfItDetail.getLogNo());
	}

	@Override
	public void delete(PfItDetail pfItDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + pfItDetail.getLogNo());
		if (dbName.equals(ContentName.onDay)) {
			pfItDetailReposDay.delete(pfItDetail);
			pfItDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfItDetailReposMon.delete(pfItDetail);
			pfItDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfItDetailReposHist.delete(pfItDetail);
			pfItDetailReposHist.flush();
		} else {
			pfItDetailRepos.delete(pfItDetail);
			pfItDetailRepos.flush();
		}
	}

	@Override
	public void insertAll(List<PfItDetail> pfItDetail, TitaVo... titaVo) throws DBException {
		if (pfItDetail == null || pfItDetail.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("InsertAll...");
		for (PfItDetail t : pfItDetail) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			pfItDetail = pfItDetailReposDay.saveAll(pfItDetail);
			pfItDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfItDetail = pfItDetailReposMon.saveAll(pfItDetail);
			pfItDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfItDetail = pfItDetailReposHist.saveAll(pfItDetail);
			pfItDetailReposHist.flush();
		} else {
			pfItDetail = pfItDetailRepos.saveAll(pfItDetail);
			pfItDetailRepos.flush();
		}
	}

	@Override
	public void updateAll(List<PfItDetail> pfItDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (pfItDetail == null || pfItDetail.size() == 0)
			throw new DBException(6);

		for (PfItDetail t : pfItDetail)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			pfItDetail = pfItDetailReposDay.saveAll(pfItDetail);
			pfItDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfItDetail = pfItDetailReposMon.saveAll(pfItDetail);
			pfItDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfItDetail = pfItDetailReposHist.saveAll(pfItDetail);
			pfItDetailReposHist.flush();
		} else {
			pfItDetail = pfItDetailRepos.saveAll(pfItDetail);
			pfItDetailRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<PfItDetail> pfItDetail, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (pfItDetail == null || pfItDetail.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			pfItDetailReposDay.deleteAll(pfItDetail);
			pfItDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfItDetailReposMon.deleteAll(pfItDetail);
			pfItDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfItDetailReposHist.deleteAll(pfItDetail);
			pfItDetailReposHist.flush();
		} else {
			pfItDetailRepos.deleteAll(pfItDetail);
			pfItDetailRepos.flush();
		}
	}

}
