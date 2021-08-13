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
import com.st1.itx.db.domain.LoanBorHistory;
import com.st1.itx.db.domain.LoanBorHistoryId;
import com.st1.itx.db.repository.online.LoanBorHistoryRepository;
import com.st1.itx.db.repository.day.LoanBorHistoryRepositoryDay;
import com.st1.itx.db.repository.mon.LoanBorHistoryRepositoryMon;
import com.st1.itx.db.repository.hist.LoanBorHistoryRepositoryHist;
import com.st1.itx.db.service.LoanBorHistoryService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanBorHistoryService")
@Repository
public class LoanBorHistoryServiceImpl implements LoanBorHistoryService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LoanBorHistoryServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorHistoryRepository loanBorHistoryRepos;

	@Autowired
	private LoanBorHistoryRepositoryDay loanBorHistoryReposDay;

	@Autowired
	private LoanBorHistoryRepositoryMon loanBorHistoryReposMon;

	@Autowired
	private LoanBorHistoryRepositoryHist loanBorHistoryReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorHistoryRepos);
		org.junit.Assert.assertNotNull(loanBorHistoryReposDay);
		org.junit.Assert.assertNotNull(loanBorHistoryReposMon);
		org.junit.Assert.assertNotNull(loanBorHistoryReposHist);
	}

	@Override
	public LoanBorHistory findById(LoanBorHistoryId loanBorHistoryId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + loanBorHistoryId);
		Optional<LoanBorHistory> loanBorHistory = null;
		if (dbName.equals(ContentName.onDay))
			loanBorHistory = loanBorHistoryReposDay.findById(loanBorHistoryId);
		else if (dbName.equals(ContentName.onMon))
			loanBorHistory = loanBorHistoryReposMon.findById(loanBorHistoryId);
		else if (dbName.equals(ContentName.onHist))
			loanBorHistory = loanBorHistoryReposHist.findById(loanBorHistoryId);
		else
			loanBorHistory = loanBorHistoryRepos.findById(loanBorHistoryId);
		LoanBorHistory obj = loanBorHistory.isPresent() ? loanBorHistory.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<LoanBorHistory> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanBorHistory> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "BormNo"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = loanBorHistoryReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanBorHistoryReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanBorHistoryReposHist.findAll(pageable);
		else
			slice = loanBorHistoryRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanBorHistory> bormCustNoEq(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanBorHistory> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("bormCustNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " facmNo_2 : " + facmNo_2 + " bormNo_3 : " + bormNo_3 + " bormNo_4 : " + bormNo_4);
		if (dbName.equals(ContentName.onDay))
			slice = loanBorHistoryReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1,
					facmNo_2, bormNo_3, bormNo_4, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanBorHistoryReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1,
					facmNo_2, bormNo_3, bormNo_4, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanBorHistoryReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1,
					facmNo_2, bormNo_3, bormNo_4, pageable);
		else
			slice = loanBorHistoryRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1,
					facmNo_2, bormNo_3, bormNo_4, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanBorHistory> bormFacmNoIn(int custNo_0, List<Integer> facmNo_1, int bormNo_2, int bormNo_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanBorHistory> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("bormFacmNoIn " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " bormNo_2 : " + bormNo_2 + " bormNo_3 : " + bormNo_3);
		if (dbName.equals(ContentName.onDay))
			slice = loanBorHistoryReposDay.findAllByCustNoIsAndFacmNoInAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanBorHistoryReposMon.findAllByCustNoIsAndFacmNoInAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanBorHistoryReposHist.findAllByCustNoIsAndFacmNoInAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);
		else
			slice = loanBorHistoryRepos.findAllByCustNoIsAndFacmNoInAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(custNo_0, facmNo_1, bormNo_2, bormNo_3, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanBorHistory> bormDrawdownDateRange(int drawdownDate_0, int drawdownDate_1, int bormNo_2, int bormNo_3, List<Integer> status_4, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanBorHistory> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("bormDrawdownDateRange " + dbName + " : " + "drawdownDate_0 : " + drawdownDate_0 + " drawdownDate_1 : " + drawdownDate_1 + " bormNo_2 : " + bormNo_2 + " bormNo_3 : " + bormNo_3
				+ " status_4 : " + status_4);
		if (dbName.equals(ContentName.onDay))
			slice = loanBorHistoryReposDay
					.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndStatusInOrderByDrawdownDateAscCustNoAscFacmNoAscBormNoAsc(
							drawdownDate_0, drawdownDate_1, bormNo_2, bormNo_3, status_4, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanBorHistoryReposMon
					.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndStatusInOrderByDrawdownDateAscCustNoAscFacmNoAscBormNoAsc(
							drawdownDate_0, drawdownDate_1, bormNo_2, bormNo_3, status_4, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanBorHistoryReposHist
					.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndStatusInOrderByDrawdownDateAscCustNoAscFacmNoAscBormNoAsc(
							drawdownDate_0, drawdownDate_1, bormNo_2, bormNo_3, status_4, pageable);
		else
			slice = loanBorHistoryRepos
					.findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndStatusInOrderByDrawdownDateAscCustNoAscFacmNoAscBormNoAsc(
							drawdownDate_0, drawdownDate_1, bormNo_2, bormNo_3, status_4, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanBorHistory> nextPayIntDateRange(int nextPayIntDate_0, int nextPayIntDate_1, int status_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanBorHistory> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("nextPayIntDateRange " + dbName + " : " + "nextPayIntDate_0 : " + nextPayIntDate_0 + " nextPayIntDate_1 : " + nextPayIntDate_1 + " status_2 : " + status_2);
		if (dbName.equals(ContentName.onDay))
			slice = loanBorHistoryReposDay.findAllByNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndStatusIsOrderByCustNoAscFacmNoAscBormNoAscNextPayIntDateAsc(nextPayIntDate_0,
					nextPayIntDate_1, status_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanBorHistoryReposMon.findAllByNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndStatusIsOrderByCustNoAscFacmNoAscBormNoAscNextPayIntDateAsc(nextPayIntDate_0,
					nextPayIntDate_1, status_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanBorHistoryReposHist.findAllByNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndStatusIsOrderByCustNoAscFacmNoAscBormNoAscNextPayIntDateAsc(nextPayIntDate_0,
					nextPayIntDate_1, status_2, pageable);
		else
			slice = loanBorHistoryRepos.findAllByNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndStatusIsOrderByCustNoAscFacmNoAscBormNoAscNextPayIntDateAsc(nextPayIntDate_0,
					nextPayIntDate_1, status_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanBorHistory> findStatusRange(List<Integer> status_0, int drawdownDate_1, int drawdownDate_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanBorHistory> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findStatusRange " + dbName + " : " + "status_0 : " + status_0 + " drawdownDate_1 : " + drawdownDate_1 + " drawdownDate_2 : " + drawdownDate_2);
		if (dbName.equals(ContentName.onDay))
			slice = loanBorHistoryReposDay.findAllByStatusInAndDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(status_0, drawdownDate_1, drawdownDate_2,
					pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanBorHistoryReposMon.findAllByStatusInAndDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(status_0, drawdownDate_1, drawdownDate_2,
					pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanBorHistoryReposHist.findAllByStatusInAndDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(status_0, drawdownDate_1, drawdownDate_2,
					pageable);
		else
			slice = loanBorHistoryRepos.findAllByStatusInAndDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(status_0, drawdownDate_1, drawdownDate_2,
					pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanBorHistory> AmortizedCodeEq(String amortizedCode_0, int status_1, int nextPayIntDate_2, int nextPayIntDate_3, int bormNo_4, int bormNo_5, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanBorHistory> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("AmortizedCodeEq " + dbName + " : " + "amortizedCode_0 : " + amortizedCode_0 + " status_1 : " + status_1 + " nextPayIntDate_2 : " + nextPayIntDate_2 + " nextPayIntDate_3 : "
				+ nextPayIntDate_3 + " bormNo_4 : " + bormNo_4 + " bormNo_5 : " + bormNo_5);
		if (dbName.equals(ContentName.onDay))
			slice = loanBorHistoryReposDay
					.findAllByAmortizedCodeIsAndStatusIsAndNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAscNextPayIntDateAsc(
							amortizedCode_0, status_1, nextPayIntDate_2, nextPayIntDate_3, bormNo_4, bormNo_5, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanBorHistoryReposMon
					.findAllByAmortizedCodeIsAndStatusIsAndNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAscNextPayIntDateAsc(
							amortizedCode_0, status_1, nextPayIntDate_2, nextPayIntDate_3, bormNo_4, bormNo_5, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanBorHistoryReposHist
					.findAllByAmortizedCodeIsAndStatusIsAndNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAscNextPayIntDateAsc(
							amortizedCode_0, status_1, nextPayIntDate_2, nextPayIntDate_3, bormNo_4, bormNo_5, pageable);
		else
			slice = loanBorHistoryRepos
					.findAllByAmortizedCodeIsAndStatusIsAndNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAscNextPayIntDateAsc(
							amortizedCode_0, status_1, nextPayIntDate_2, nextPayIntDate_3, bormNo_4, bormNo_5, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanBorHistory> findStatusEq(List<Integer> status_0, int custNo_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanBorHistory> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findStatusEq " + dbName + " : " + "status_0 : " + status_0 + " custNo_1 : " + custNo_1 + " facmNo_2 : " + facmNo_2 + " facmNo_3 : " + facmNo_3);
		if (dbName.equals(ContentName.onDay))
			slice = loanBorHistoryReposDay.findAllByStatusInAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscBormNoAsc(status_0, custNo_1, facmNo_2, facmNo_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanBorHistoryReposMon.findAllByStatusInAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscBormNoAsc(status_0, custNo_1, facmNo_2, facmNo_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanBorHistoryReposHist.findAllByStatusInAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscBormNoAsc(status_0, custNo_1, facmNo_2, facmNo_3, pageable);
		else
			slice = loanBorHistoryRepos.findAllByStatusInAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscBormNoAsc(status_0, custNo_1, facmNo_2, facmNo_3, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public LoanBorHistory holdById(LoanBorHistoryId loanBorHistoryId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + loanBorHistoryId);
		Optional<LoanBorHistory> loanBorHistory = null;
		if (dbName.equals(ContentName.onDay))
			loanBorHistory = loanBorHistoryReposDay.findByLoanBorHistoryId(loanBorHistoryId);
		else if (dbName.equals(ContentName.onMon))
			loanBorHistory = loanBorHistoryReposMon.findByLoanBorHistoryId(loanBorHistoryId);
		else if (dbName.equals(ContentName.onHist))
			loanBorHistory = loanBorHistoryReposHist.findByLoanBorHistoryId(loanBorHistoryId);
		else
			loanBorHistory = loanBorHistoryRepos.findByLoanBorHistoryId(loanBorHistoryId);
		return loanBorHistory.isPresent() ? loanBorHistory.get() : null;
	}

	@Override
	public LoanBorHistory holdById(LoanBorHistory loanBorHistory, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + loanBorHistory.getLoanBorHistoryId());
		Optional<LoanBorHistory> loanBorHistoryT = null;
		if (dbName.equals(ContentName.onDay))
			loanBorHistoryT = loanBorHistoryReposDay.findByLoanBorHistoryId(loanBorHistory.getLoanBorHistoryId());
		else if (dbName.equals(ContentName.onMon))
			loanBorHistoryT = loanBorHistoryReposMon.findByLoanBorHistoryId(loanBorHistory.getLoanBorHistoryId());
		else if (dbName.equals(ContentName.onHist))
			loanBorHistoryT = loanBorHistoryReposHist.findByLoanBorHistoryId(loanBorHistory.getLoanBorHistoryId());
		else
			loanBorHistoryT = loanBorHistoryRepos.findByLoanBorHistoryId(loanBorHistory.getLoanBorHistoryId());
		return loanBorHistoryT.isPresent() ? loanBorHistoryT.get() : null;
	}

	@Override
	public LoanBorHistory insert(LoanBorHistory loanBorHistory, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + loanBorHistory.getLoanBorHistoryId());
		if (this.findById(loanBorHistory.getLoanBorHistoryId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			loanBorHistory.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanBorHistoryReposDay.saveAndFlush(loanBorHistory);
		else if (dbName.equals(ContentName.onMon))
			return loanBorHistoryReposMon.saveAndFlush(loanBorHistory);
		else if (dbName.equals(ContentName.onHist))
			return loanBorHistoryReposHist.saveAndFlush(loanBorHistory);
		else
			return loanBorHistoryRepos.saveAndFlush(loanBorHistory);
	}

	@Override
	public LoanBorHistory update(LoanBorHistory loanBorHistory, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + loanBorHistory.getLoanBorHistoryId());
		if (!empNot.isEmpty())
			loanBorHistory.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanBorHistoryReposDay.saveAndFlush(loanBorHistory);
		else if (dbName.equals(ContentName.onMon))
			return loanBorHistoryReposMon.saveAndFlush(loanBorHistory);
		else if (dbName.equals(ContentName.onHist))
			return loanBorHistoryReposHist.saveAndFlush(loanBorHistory);
		else
			return loanBorHistoryRepos.saveAndFlush(loanBorHistory);
	}

	@Override
	public LoanBorHistory update2(LoanBorHistory loanBorHistory, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + loanBorHistory.getLoanBorHistoryId());
		if (!empNot.isEmpty())
			loanBorHistory.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			loanBorHistoryReposDay.saveAndFlush(loanBorHistory);
		else if (dbName.equals(ContentName.onMon))
			loanBorHistoryReposMon.saveAndFlush(loanBorHistory);
		else if (dbName.equals(ContentName.onHist))
			loanBorHistoryReposHist.saveAndFlush(loanBorHistory);
		else
			loanBorHistoryRepos.saveAndFlush(loanBorHistory);
		return this.findById(loanBorHistory.getLoanBorHistoryId());
	}

	@Override
	public void delete(LoanBorHistory loanBorHistory, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + loanBorHistory.getLoanBorHistoryId());
		if (dbName.equals(ContentName.onDay)) {
			loanBorHistoryReposDay.delete(loanBorHistory);
			loanBorHistoryReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanBorHistoryReposMon.delete(loanBorHistory);
			loanBorHistoryReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanBorHistoryReposHist.delete(loanBorHistory);
			loanBorHistoryReposHist.flush();
		} else {
			loanBorHistoryRepos.delete(loanBorHistory);
			loanBorHistoryRepos.flush();
		}
	}

	@Override
	public void insertAll(List<LoanBorHistory> loanBorHistory, TitaVo... titaVo) throws DBException {
		if (loanBorHistory == null || loanBorHistory.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (LoanBorHistory t : loanBorHistory)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanBorHistory = loanBorHistoryReposDay.saveAll(loanBorHistory);
			loanBorHistoryReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanBorHistory = loanBorHistoryReposMon.saveAll(loanBorHistory);
			loanBorHistoryReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanBorHistory = loanBorHistoryReposHist.saveAll(loanBorHistory);
			loanBorHistoryReposHist.flush();
		} else {
			loanBorHistory = loanBorHistoryRepos.saveAll(loanBorHistory);
			loanBorHistoryRepos.flush();
		}
	}

	@Override
	public void updateAll(List<LoanBorHistory> loanBorHistory, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (loanBorHistory == null || loanBorHistory.size() == 0)
			throw new DBException(6);

		for (LoanBorHistory t : loanBorHistory)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanBorHistory = loanBorHistoryReposDay.saveAll(loanBorHistory);
			loanBorHistoryReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanBorHistory = loanBorHistoryReposMon.saveAll(loanBorHistory);
			loanBorHistoryReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanBorHistory = loanBorHistoryReposHist.saveAll(loanBorHistory);
			loanBorHistoryReposHist.flush();
		} else {
			loanBorHistory = loanBorHistoryRepos.saveAll(loanBorHistory);
			loanBorHistoryRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<LoanBorHistory> loanBorHistory, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (loanBorHistory == null || loanBorHistory.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			loanBorHistoryReposDay.deleteAll(loanBorHistory);
			loanBorHistoryReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanBorHistoryReposMon.deleteAll(loanBorHistory);
			loanBorHistoryReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanBorHistoryReposHist.deleteAll(loanBorHistory);
			loanBorHistoryReposHist.flush();
		} else {
			loanBorHistoryRepos.deleteAll(loanBorHistory);
			loanBorHistoryRepos.flush();
		}
	}

}
