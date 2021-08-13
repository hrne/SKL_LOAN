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
import com.st1.itx.db.domain.LoanIntDetail;
import com.st1.itx.db.domain.LoanIntDetailId;
import com.st1.itx.db.repository.online.LoanIntDetailRepository;
import com.st1.itx.db.repository.day.LoanIntDetailRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIntDetailRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIntDetailRepositoryHist;
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIntDetailService")
@Repository
public class LoanIntDetailServiceImpl implements LoanIntDetailService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LoanIntDetailServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanIntDetailRepository loanIntDetailRepos;

	@Autowired
	private LoanIntDetailRepositoryDay loanIntDetailReposDay;

	@Autowired
	private LoanIntDetailRepositoryMon loanIntDetailReposMon;

	@Autowired
	private LoanIntDetailRepositoryHist loanIntDetailReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanIntDetailRepos);
		org.junit.Assert.assertNotNull(loanIntDetailReposDay);
		org.junit.Assert.assertNotNull(loanIntDetailReposMon);
		org.junit.Assert.assertNotNull(loanIntDetailReposHist);
	}

	@Override
	public LoanIntDetail findById(LoanIntDetailId loanIntDetailId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("findById " + dbName + " " + loanIntDetailId);
		Optional<LoanIntDetail> loanIntDetail = null;
		if (dbName.equals(ContentName.onDay))
			loanIntDetail = loanIntDetailReposDay.findById(loanIntDetailId);
		else if (dbName.equals(ContentName.onMon))
			loanIntDetail = loanIntDetailReposMon.findById(loanIntDetailId);
		else if (dbName.equals(ContentName.onHist))
			loanIntDetail = loanIntDetailReposHist.findById(loanIntDetailId);
		else
			loanIntDetail = loanIntDetailRepos.findById(loanIntDetailId);
		LoanIntDetail obj = loanIntDetail.isPresent() ? loanIntDetail.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<LoanIntDetail> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanIntDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "BormNo", "AcDate", "TlrNo", "TxtNo", "IntSeq"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = loanIntDetailReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanIntDetailReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanIntDetailReposHist.findAll(pageable);
		else
			slice = loanIntDetailRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanIntDetail> intDetailBreachGetCodeEq(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, String breachGetCode_5, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanIntDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("intDetailBreachGetCodeEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " facmNo_2 : " + facmNo_2 + " bormNo_3 : " + bormNo_3 + " bormNo_4 : "
				+ bormNo_4 + " breachGetCode_5 : " + breachGetCode_5);
		if (dbName.equals(ContentName.onDay))
			slice = loanIntDetailReposDay.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndBreachGetCodeIsOrderByAcDateAsc(custNo_0,
					facmNo_1, facmNo_2, bormNo_3, bormNo_4, breachGetCode_5, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanIntDetailReposMon.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndBreachGetCodeIsOrderByAcDateAsc(custNo_0,
					facmNo_1, facmNo_2, bormNo_3, bormNo_4, breachGetCode_5, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanIntDetailReposHist.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndBreachGetCodeIsOrderByAcDateAsc(custNo_0,
					facmNo_1, facmNo_2, bormNo_3, bormNo_4, breachGetCode_5, pageable);
		else
			slice = loanIntDetailRepos.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndBreachGetCodeIsOrderByAcDateAsc(custNo_0,
					facmNo_1, facmNo_2, bormNo_3, bormNo_4, breachGetCode_5, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanIntDetail> fildFacmNoEq(int custNo_0, int facmNo_1, int bormNo_2, int acDate_3, String tlrNo_4, String txtNo_5, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanIntDetail> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		logger.info("fildFacmNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " bormNo_2 : " + bormNo_2 + " acDate_3 : " + acDate_3 + " tlrNo_4 : " + tlrNo_4
				+ " txtNo_5 : " + txtNo_5);
		if (dbName.equals(ContentName.onDay))
			slice = loanIntDetailReposDay.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndAcDateIsAndTlrNoIsAndTxtNoIsOrderByAcDateAsc(custNo_0, facmNo_1, bormNo_2, acDate_3, tlrNo_4, txtNo_5, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanIntDetailReposMon.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndAcDateIsAndTlrNoIsAndTxtNoIsOrderByAcDateAsc(custNo_0, facmNo_1, bormNo_2, acDate_3, tlrNo_4, txtNo_5, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanIntDetailReposHist.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndAcDateIsAndTlrNoIsAndTxtNoIsOrderByAcDateAsc(custNo_0, facmNo_1, bormNo_2, acDate_3, tlrNo_4, txtNo_5, pageable);
		else
			slice = loanIntDetailRepos.findAllByCustNoIsAndFacmNoIsAndBormNoIsAndAcDateIsAndTlrNoIsAndTxtNoIsOrderByAcDateAsc(custNo_0, facmNo_1, bormNo_2, acDate_3, tlrNo_4, txtNo_5, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public LoanIntDetail holdById(LoanIntDetailId loanIntDetailId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + loanIntDetailId);
		Optional<LoanIntDetail> loanIntDetail = null;
		if (dbName.equals(ContentName.onDay))
			loanIntDetail = loanIntDetailReposDay.findByLoanIntDetailId(loanIntDetailId);
		else if (dbName.equals(ContentName.onMon))
			loanIntDetail = loanIntDetailReposMon.findByLoanIntDetailId(loanIntDetailId);
		else if (dbName.equals(ContentName.onHist))
			loanIntDetail = loanIntDetailReposHist.findByLoanIntDetailId(loanIntDetailId);
		else
			loanIntDetail = loanIntDetailRepos.findByLoanIntDetailId(loanIntDetailId);
		return loanIntDetail.isPresent() ? loanIntDetail.get() : null;
	}

	@Override
	public LoanIntDetail holdById(LoanIntDetail loanIntDetail, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Hold " + dbName + " " + loanIntDetail.getLoanIntDetailId());
		Optional<LoanIntDetail> loanIntDetailT = null;
		if (dbName.equals(ContentName.onDay))
			loanIntDetailT = loanIntDetailReposDay.findByLoanIntDetailId(loanIntDetail.getLoanIntDetailId());
		else if (dbName.equals(ContentName.onMon))
			loanIntDetailT = loanIntDetailReposMon.findByLoanIntDetailId(loanIntDetail.getLoanIntDetailId());
		else if (dbName.equals(ContentName.onHist))
			loanIntDetailT = loanIntDetailReposHist.findByLoanIntDetailId(loanIntDetail.getLoanIntDetailId());
		else
			loanIntDetailT = loanIntDetailRepos.findByLoanIntDetailId(loanIntDetail.getLoanIntDetailId());
		return loanIntDetailT.isPresent() ? loanIntDetailT.get() : null;
	}

	@Override
	public LoanIntDetail insert(LoanIntDetail loanIntDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Insert..." + dbName + " " + loanIntDetail.getLoanIntDetailId());
		if (this.findById(loanIntDetail.getLoanIntDetailId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			loanIntDetail.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanIntDetailReposDay.saveAndFlush(loanIntDetail);
		else if (dbName.equals(ContentName.onMon))
			return loanIntDetailReposMon.saveAndFlush(loanIntDetail);
		else if (dbName.equals(ContentName.onHist))
			return loanIntDetailReposHist.saveAndFlush(loanIntDetail);
		else
			return loanIntDetailRepos.saveAndFlush(loanIntDetail);
	}

	@Override
	public LoanIntDetail update(LoanIntDetail loanIntDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + loanIntDetail.getLoanIntDetailId());
		if (!empNot.isEmpty())
			loanIntDetail.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanIntDetailReposDay.saveAndFlush(loanIntDetail);
		else if (dbName.equals(ContentName.onMon))
			return loanIntDetailReposMon.saveAndFlush(loanIntDetail);
		else if (dbName.equals(ContentName.onHist))
			return loanIntDetailReposHist.saveAndFlush(loanIntDetail);
		else
			return loanIntDetailRepos.saveAndFlush(loanIntDetail);
	}

	@Override
	public LoanIntDetail update2(LoanIntDetail loanIntDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("Update..." + dbName + " " + loanIntDetail.getLoanIntDetailId());
		if (!empNot.isEmpty())
			loanIntDetail.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			loanIntDetailReposDay.saveAndFlush(loanIntDetail);
		else if (dbName.equals(ContentName.onMon))
			loanIntDetailReposMon.saveAndFlush(loanIntDetail);
		else if (dbName.equals(ContentName.onHist))
			loanIntDetailReposHist.saveAndFlush(loanIntDetail);
		else
			loanIntDetailRepos.saveAndFlush(loanIntDetail);
		return this.findById(loanIntDetail.getLoanIntDetailId());
	}

	@Override
	public void delete(LoanIntDetail loanIntDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		logger.info("Delete..." + dbName + " " + loanIntDetail.getLoanIntDetailId());
		if (dbName.equals(ContentName.onDay)) {
			loanIntDetailReposDay.delete(loanIntDetail);
			loanIntDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIntDetailReposMon.delete(loanIntDetail);
			loanIntDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIntDetailReposHist.delete(loanIntDetail);
			loanIntDetailReposHist.flush();
		} else {
			loanIntDetailRepos.delete(loanIntDetail);
			loanIntDetailRepos.flush();
		}
	}

	@Override
	public void insertAll(List<LoanIntDetail> loanIntDetail, TitaVo... titaVo) throws DBException {
		if (loanIntDetail == null || loanIntDetail.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("InsertAll...");
		for (LoanIntDetail t : loanIntDetail)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanIntDetail = loanIntDetailReposDay.saveAll(loanIntDetail);
			loanIntDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIntDetail = loanIntDetailReposMon.saveAll(loanIntDetail);
			loanIntDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIntDetail = loanIntDetailReposHist.saveAll(loanIntDetail);
			loanIntDetailReposHist.flush();
		} else {
			loanIntDetail = loanIntDetailRepos.saveAll(loanIntDetail);
			loanIntDetailRepos.flush();
		}
	}

	@Override
	public void updateAll(List<LoanIntDetail> loanIntDetail, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		logger.info("UpdateAll...");
		if (loanIntDetail == null || loanIntDetail.size() == 0)
			throw new DBException(6);

		for (LoanIntDetail t : loanIntDetail)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanIntDetail = loanIntDetailReposDay.saveAll(loanIntDetail);
			loanIntDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIntDetail = loanIntDetailReposMon.saveAll(loanIntDetail);
			loanIntDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIntDetail = loanIntDetailReposHist.saveAll(loanIntDetail);
			loanIntDetailReposHist.flush();
		} else {
			loanIntDetail = loanIntDetailRepos.saveAll(loanIntDetail);
			loanIntDetailRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<LoanIntDetail> loanIntDetail, TitaVo... titaVo) throws DBException {
		logger.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (loanIntDetail == null || loanIntDetail.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			loanIntDetailReposDay.deleteAll(loanIntDetail);
			loanIntDetailReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIntDetailReposMon.deleteAll(loanIntDetail);
			loanIntDetailReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIntDetailReposHist.deleteAll(loanIntDetail);
			loanIntDetailReposHist.flush();
		} else {
			loanIntDetailRepos.deleteAll(loanIntDetail);
			loanIntDetailRepos.flush();
		}
	}

}
