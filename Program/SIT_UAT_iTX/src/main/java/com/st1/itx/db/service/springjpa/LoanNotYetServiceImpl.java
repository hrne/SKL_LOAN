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
import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.domain.LoanNotYetId;
import com.st1.itx.db.repository.online.LoanNotYetRepository;
import com.st1.itx.db.repository.day.LoanNotYetRepositoryDay;
import com.st1.itx.db.repository.mon.LoanNotYetRepositoryMon;
import com.st1.itx.db.repository.hist.LoanNotYetRepositoryHist;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanNotYetService")
@Repository
public class LoanNotYetServiceImpl extends ASpringJpaParm implements LoanNotYetService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanNotYetRepository loanNotYetRepos;

	@Autowired
	private LoanNotYetRepositoryDay loanNotYetReposDay;

	@Autowired
	private LoanNotYetRepositoryMon loanNotYetReposMon;

	@Autowired
	private LoanNotYetRepositoryHist loanNotYetReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanNotYetRepos);
		org.junit.Assert.assertNotNull(loanNotYetReposDay);
		org.junit.Assert.assertNotNull(loanNotYetReposMon);
		org.junit.Assert.assertNotNull(loanNotYetReposHist);
	}

	@Override
	public LoanNotYet findById(LoanNotYetId loanNotYetId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + loanNotYetId);
		Optional<LoanNotYet> loanNotYet = null;
		if (dbName.equals(ContentName.onDay))
			loanNotYet = loanNotYetReposDay.findById(loanNotYetId);
		else if (dbName.equals(ContentName.onMon))
			loanNotYet = loanNotYetReposMon.findById(loanNotYetId);
		else if (dbName.equals(ContentName.onHist))
			loanNotYet = loanNotYetReposHist.findById(loanNotYetId);
		else
			loanNotYet = loanNotYetRepos.findById(loanNotYetId);
		LoanNotYet obj = loanNotYet.isPresent() ? loanNotYet.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<LoanNotYet> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanNotYet> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "NotYetCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "NotYetCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = loanNotYetReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanNotYetReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanNotYetReposHist.findAll(pageable);
		else
			slice = loanNotYetRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanNotYet> notYetCustNoEq(int custNo_0, int facmNo_1, int facmNo_2, int yetDate_3, int yetDate_4, int closeDate_5, int closeDate_6, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanNotYet> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("notYetCustNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " facmNo_2 : " + facmNo_2 + " yetDate_3 : " + yetDate_3 + " yetDate_4 : " + yetDate_4
				+ " closeDate_5 : " + closeDate_5 + " closeDate_6 : " + closeDate_6);
		if (dbName.equals(ContentName.onDay))
			slice = loanNotYetReposDay
					.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndYetDateGreaterThanEqualAndYetDateLessThanEqualAndCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscFacmNoAscNotYetCodeAsc(
							custNo_0, facmNo_1, facmNo_2, yetDate_3, yetDate_4, closeDate_5, closeDate_6, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanNotYetReposMon
					.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndYetDateGreaterThanEqualAndYetDateLessThanEqualAndCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscFacmNoAscNotYetCodeAsc(
							custNo_0, facmNo_1, facmNo_2, yetDate_3, yetDate_4, closeDate_5, closeDate_6, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanNotYetReposHist
					.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndYetDateGreaterThanEqualAndYetDateLessThanEqualAndCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscFacmNoAscNotYetCodeAsc(
							custNo_0, facmNo_1, facmNo_2, yetDate_3, yetDate_4, closeDate_5, closeDate_6, pageable);
		else
			slice = loanNotYetRepos
					.findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndYetDateGreaterThanEqualAndYetDateLessThanEqualAndCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscFacmNoAscNotYetCodeAsc(
							custNo_0, facmNo_1, facmNo_2, yetDate_3, yetDate_4, closeDate_5, closeDate_6, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanNotYet> findCustNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanNotYet> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCustNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = loanNotYetReposDay.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanNotYetReposMon.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanNotYetReposHist.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
		else
			slice = loanNotYetRepos.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanNotYet> notYetCodeFisrt(int custNo_0, int facmNo_1, String notYetCode_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanNotYet> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("notYetCodeFisrt " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " notYetCode_2 : " + notYetCode_2);
		if (dbName.equals(ContentName.onDay))
			slice = loanNotYetReposDay.findAllByCustNoIsAndFacmNoIsAndNotYetCodeIs(custNo_0, facmNo_1, notYetCode_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanNotYetReposMon.findAllByCustNoIsAndFacmNoIsAndNotYetCodeIs(custNo_0, facmNo_1, notYetCode_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanNotYetReposHist.findAllByCustNoIsAndFacmNoIsAndNotYetCodeIs(custNo_0, facmNo_1, notYetCode_2, pageable);
		else
			slice = loanNotYetRepos.findAllByCustNoIsAndFacmNoIsAndNotYetCodeIs(custNo_0, facmNo_1, notYetCode_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanNotYet> allNoClose(int closeDate_0, int yetDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanNotYet> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("allNoClose " + dbName + " : " + "closeDate_0 : " + closeDate_0 + " yetDate_1 : " + yetDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = loanNotYetReposDay.findAllByCloseDateIsAndYetDateLessThanEqualOrderByCustNoAscFacmNoAscYetDateAsc(closeDate_0, yetDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanNotYetReposMon.findAllByCloseDateIsAndYetDateLessThanEqualOrderByCustNoAscFacmNoAscYetDateAsc(closeDate_0, yetDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanNotYetReposHist.findAllByCloseDateIsAndYetDateLessThanEqualOrderByCustNoAscFacmNoAscYetDateAsc(closeDate_0, yetDate_1, pageable);
		else
			slice = loanNotYetRepos.findAllByCloseDateIsAndYetDateLessThanEqualOrderByCustNoAscFacmNoAscYetDateAsc(closeDate_0, yetDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public LoanNotYet holdById(LoanNotYetId loanNotYetId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanNotYetId);
		Optional<LoanNotYet> loanNotYet = null;
		if (dbName.equals(ContentName.onDay))
			loanNotYet = loanNotYetReposDay.findByLoanNotYetId(loanNotYetId);
		else if (dbName.equals(ContentName.onMon))
			loanNotYet = loanNotYetReposMon.findByLoanNotYetId(loanNotYetId);
		else if (dbName.equals(ContentName.onHist))
			loanNotYet = loanNotYetReposHist.findByLoanNotYetId(loanNotYetId);
		else
			loanNotYet = loanNotYetRepos.findByLoanNotYetId(loanNotYetId);
		return loanNotYet.isPresent() ? loanNotYet.get() : null;
	}

	@Override
	public LoanNotYet holdById(LoanNotYet loanNotYet, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanNotYet.getLoanNotYetId());
		Optional<LoanNotYet> loanNotYetT = null;
		if (dbName.equals(ContentName.onDay))
			loanNotYetT = loanNotYetReposDay.findByLoanNotYetId(loanNotYet.getLoanNotYetId());
		else if (dbName.equals(ContentName.onMon))
			loanNotYetT = loanNotYetReposMon.findByLoanNotYetId(loanNotYet.getLoanNotYetId());
		else if (dbName.equals(ContentName.onHist))
			loanNotYetT = loanNotYetReposHist.findByLoanNotYetId(loanNotYet.getLoanNotYetId());
		else
			loanNotYetT = loanNotYetRepos.findByLoanNotYetId(loanNotYet.getLoanNotYetId());
		return loanNotYetT.isPresent() ? loanNotYetT.get() : null;
	}

	@Override
	public LoanNotYet insert(LoanNotYet loanNotYet, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + loanNotYet.getLoanNotYetId());
		if (this.findById(loanNotYet.getLoanNotYetId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			loanNotYet.setCreateEmpNo(empNot);

		if (loanNotYet.getLastUpdateEmpNo() == null || loanNotYet.getLastUpdateEmpNo().isEmpty())
			loanNotYet.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanNotYetReposDay.saveAndFlush(loanNotYet);
		else if (dbName.equals(ContentName.onMon))
			return loanNotYetReposMon.saveAndFlush(loanNotYet);
		else if (dbName.equals(ContentName.onHist))
			return loanNotYetReposHist.saveAndFlush(loanNotYet);
		else
			return loanNotYetRepos.saveAndFlush(loanNotYet);
	}

	@Override
	public LoanNotYet update(LoanNotYet loanNotYet, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + loanNotYet.getLoanNotYetId());
		if (!empNot.isEmpty())
			loanNotYet.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanNotYetReposDay.saveAndFlush(loanNotYet);
		else if (dbName.equals(ContentName.onMon))
			return loanNotYetReposMon.saveAndFlush(loanNotYet);
		else if (dbName.equals(ContentName.onHist))
			return loanNotYetReposHist.saveAndFlush(loanNotYet);
		else
			return loanNotYetRepos.saveAndFlush(loanNotYet);
	}

	@Override
	public LoanNotYet update2(LoanNotYet loanNotYet, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + loanNotYet.getLoanNotYetId());
		if (!empNot.isEmpty())
			loanNotYet.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			loanNotYetReposDay.saveAndFlush(loanNotYet);
		else if (dbName.equals(ContentName.onMon))
			loanNotYetReposMon.saveAndFlush(loanNotYet);
		else if (dbName.equals(ContentName.onHist))
			loanNotYetReposHist.saveAndFlush(loanNotYet);
		else
			loanNotYetRepos.saveAndFlush(loanNotYet);
		return this.findById(loanNotYet.getLoanNotYetId());
	}

	@Override
	public void delete(LoanNotYet loanNotYet, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + loanNotYet.getLoanNotYetId());
		if (dbName.equals(ContentName.onDay)) {
			loanNotYetReposDay.delete(loanNotYet);
			loanNotYetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanNotYetReposMon.delete(loanNotYet);
			loanNotYetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanNotYetReposHist.delete(loanNotYet);
			loanNotYetReposHist.flush();
		} else {
			loanNotYetRepos.delete(loanNotYet);
			loanNotYetRepos.flush();
		}
	}

	@Override
	public void insertAll(List<LoanNotYet> loanNotYet, TitaVo... titaVo) throws DBException {
		if (loanNotYet == null || loanNotYet.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (LoanNotYet t : loanNotYet) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			loanNotYet = loanNotYetReposDay.saveAll(loanNotYet);
			loanNotYetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanNotYet = loanNotYetReposMon.saveAll(loanNotYet);
			loanNotYetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanNotYet = loanNotYetReposHist.saveAll(loanNotYet);
			loanNotYetReposHist.flush();
		} else {
			loanNotYet = loanNotYetRepos.saveAll(loanNotYet);
			loanNotYetRepos.flush();
		}
	}

	@Override
	public void updateAll(List<LoanNotYet> loanNotYet, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (loanNotYet == null || loanNotYet.size() == 0)
			throw new DBException(6);

		for (LoanNotYet t : loanNotYet)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanNotYet = loanNotYetReposDay.saveAll(loanNotYet);
			loanNotYetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanNotYet = loanNotYetReposMon.saveAll(loanNotYet);
			loanNotYetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanNotYet = loanNotYetReposHist.saveAll(loanNotYet);
			loanNotYetReposHist.flush();
		} else {
			loanNotYet = loanNotYetRepos.saveAll(loanNotYet);
			loanNotYetRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<LoanNotYet> loanNotYet, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (loanNotYet == null || loanNotYet.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			loanNotYetReposDay.deleteAll(loanNotYet);
			loanNotYetReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanNotYetReposMon.deleteAll(loanNotYet);
			loanNotYetReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanNotYetReposHist.deleteAll(loanNotYet);
			loanNotYetReposHist.flush();
		} else {
			loanNotYetRepos.deleteAll(loanNotYet);
			loanNotYetRepos.flush();
		}
	}

}
