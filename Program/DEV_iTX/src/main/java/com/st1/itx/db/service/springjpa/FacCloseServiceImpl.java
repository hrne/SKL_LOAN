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
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.repository.online.FacCloseRepository;
import com.st1.itx.db.repository.day.FacCloseRepositoryDay;
import com.st1.itx.db.repository.mon.FacCloseRepositoryMon;
import com.st1.itx.db.repository.hist.FacCloseRepositoryHist;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facCloseService")
@Repository
public class FacCloseServiceImpl extends ASpringJpaParm implements FacCloseService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private FacCloseRepository facCloseRepos;

	@Autowired
	private FacCloseRepositoryDay facCloseReposDay;

	@Autowired
	private FacCloseRepositoryMon facCloseReposMon;

	@Autowired
	private FacCloseRepositoryHist facCloseReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(facCloseRepos);
		org.junit.Assert.assertNotNull(facCloseReposDay);
		org.junit.Assert.assertNotNull(facCloseReposMon);
		org.junit.Assert.assertNotNull(facCloseReposHist);
	}

	@Override
	public FacClose findById(FacCloseId facCloseId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + facCloseId);
		Optional<FacClose> facClose = null;
		if (dbName.equals(ContentName.onDay))
			facClose = facCloseReposDay.findById(facCloseId);
		else if (dbName.equals(ContentName.onMon))
			facClose = facCloseReposMon.findById(facCloseId);
		else if (dbName.equals(ContentName.onHist))
			facClose = facCloseReposHist.findById(facCloseId);
		else
			facClose = facCloseRepos.findById(facCloseId);
		FacClose obj = facClose.isPresent() ? facClose.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<FacClose> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacClose> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "CloseNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "CloseNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = facCloseReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCloseReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCloseReposHist.findAll(pageable);
		else
			slice = facCloseRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacClose> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacClose> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = facCloseReposDay.findAllByCustNoIsOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCloseReposMon.findAllByCustNoIsOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCloseReposHist.findAllByCustNoIsOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(custNo_0, pageable);
		else
			slice = facCloseRepos.findAllByCustNoIsOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(custNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacClose> findFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacClose> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findFacmNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = facCloseReposDay.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscCloseDateAsc(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCloseReposMon.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscCloseDateAsc(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCloseReposHist.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscCloseDateAsc(custNo_0, facmNo_1, pageable);
		else
			slice = facCloseRepos.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscCloseDateAsc(custNo_0, facmNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacClose> findCloseDate(int closeDate_0, int closeDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacClose> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCloseDate " + dbName + " : " + "closeDate_0 : " + closeDate_0 + " closeDate_1 : " + closeDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = facCloseReposDay.findAllByCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscFacmNoAscCloseDateAsc(closeDate_0, closeDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCloseReposMon.findAllByCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscFacmNoAscCloseDateAsc(closeDate_0, closeDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCloseReposHist.findAllByCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscFacmNoAscCloseDateAsc(closeDate_0, closeDate_1, pageable);
		else
			slice = facCloseRepos.findAllByCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscFacmNoAscCloseDateAsc(closeDate_0, closeDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacClose findMaxCloseNoFirst(int custNo_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findMaxCloseNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0);
		Optional<FacClose> facCloseT = null;
		if (dbName.equals(ContentName.onDay))
			facCloseT = facCloseReposDay.findTopByCustNoIsOrderByCloseNoDesc(custNo_0);
		else if (dbName.equals(ContentName.onMon))
			facCloseT = facCloseReposMon.findTopByCustNoIsOrderByCloseNoDesc(custNo_0);
		else if (dbName.equals(ContentName.onHist))
			facCloseT = facCloseReposHist.findTopByCustNoIsOrderByCloseNoDesc(custNo_0);
		else
			facCloseT = facCloseRepos.findTopByCustNoIsOrderByCloseNoDesc(custNo_0);

		return facCloseT.isPresent() ? facCloseT.get() : null;
	}

	@Override
	public Slice<FacClose> findEntryDate(int entryDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacClose> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findEntryDate " + dbName + " : " + "entryDate_0 : " + entryDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = facCloseReposDay.findAllByEntryDateIsOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(entryDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCloseReposMon.findAllByEntryDateIsOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(entryDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCloseReposHist.findAllByEntryDateIsOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(entryDate_0, pageable);
		else
			slice = facCloseRepos.findAllByEntryDateIsOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(entryDate_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacClose> findCloseNo(int closeDate_0, int closeNo_1, int closeNo_2, int carLoan_3, int carLoan_4, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacClose> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCloseNo " + dbName + " : " + "closeDate_0 : " + closeDate_0 + " closeNo_1 : " + closeNo_1 + " closeNo_2 : " + closeNo_2 + " carLoan_3 : " + carLoan_3 + " carLoan_4 : "
				+ carLoan_4);
		if (dbName.equals(ContentName.onDay))
			slice = facCloseReposDay.findAllByCloseDateIsAndCloseNoGreaterThanEqualAndCloseNoLessThanEqualAndCarLoanGreaterThanEqualAndCarLoanLessThanEqualOrderByCustNoAscFacmNoAscCloseDateAsc(
					closeDate_0, closeNo_1, closeNo_2, carLoan_3, carLoan_4, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCloseReposMon.findAllByCloseDateIsAndCloseNoGreaterThanEqualAndCloseNoLessThanEqualAndCarLoanGreaterThanEqualAndCarLoanLessThanEqualOrderByCustNoAscFacmNoAscCloseDateAsc(
					closeDate_0, closeNo_1, closeNo_2, carLoan_3, carLoan_4, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCloseReposHist.findAllByCloseDateIsAndCloseNoGreaterThanEqualAndCloseNoLessThanEqualAndCarLoanGreaterThanEqualAndCarLoanLessThanEqualOrderByCustNoAscFacmNoAscCloseDateAsc(
					closeDate_0, closeNo_1, closeNo_2, carLoan_3, carLoan_4, pageable);
		else
			slice = facCloseRepos.findAllByCloseDateIsAndCloseNoGreaterThanEqualAndCloseNoLessThanEqualAndCarLoanGreaterThanEqualAndCarLoanLessThanEqualOrderByCustNoAscFacmNoAscCloseDateAsc(
					closeDate_0, closeNo_1, closeNo_2, carLoan_3, carLoan_4, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacClose findFacmNoFirst(int custNo_0, int facmNo_1, List<String> funCode_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findFacmNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1 + " funCode_2 : " + funCode_2);
		Optional<FacClose> facCloseT = null;
		if (dbName.equals(ContentName.onDay))
			facCloseT = facCloseReposDay.findTopByCustNoIsAndFacmNoIsAndFunCodeInOrderByCloseNoDesc(custNo_0, facmNo_1, funCode_2);
		else if (dbName.equals(ContentName.onMon))
			facCloseT = facCloseReposMon.findTopByCustNoIsAndFacmNoIsAndFunCodeInOrderByCloseNoDesc(custNo_0, facmNo_1, funCode_2);
		else if (dbName.equals(ContentName.onHist))
			facCloseT = facCloseReposHist.findTopByCustNoIsAndFacmNoIsAndFunCodeInOrderByCloseNoDesc(custNo_0, facmNo_1, funCode_2);
		else
			facCloseT = facCloseRepos.findTopByCustNoIsAndFacmNoIsAndFunCodeInOrderByCloseNoDesc(custNo_0, facmNo_1, funCode_2);

		return facCloseT.isPresent() ? facCloseT.get() : null;
	}

	@Override
	public Slice<FacClose> findApplDateEq(int applDate_0, int applDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacClose> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findApplDateEq " + dbName + " : " + "applDate_0 : " + applDate_0 + " applDate_1 : " + applDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = facCloseReposDay.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(applDate_0, applDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCloseReposMon.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(applDate_0, applDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCloseReposHist.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(applDate_0, applDate_1, pageable);
		else
			slice = facCloseRepos.findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(applDate_0, applDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacClose holdById(FacCloseId facCloseId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facCloseId);
		Optional<FacClose> facClose = null;
		if (dbName.equals(ContentName.onDay))
			facClose = facCloseReposDay.findByFacCloseId(facCloseId);
		else if (dbName.equals(ContentName.onMon))
			facClose = facCloseReposMon.findByFacCloseId(facCloseId);
		else if (dbName.equals(ContentName.onHist))
			facClose = facCloseReposHist.findByFacCloseId(facCloseId);
		else
			facClose = facCloseRepos.findByFacCloseId(facCloseId);
		return facClose.isPresent() ? facClose.get() : null;
	}

	@Override
	public FacClose holdById(FacClose facClose, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facClose.getFacCloseId());
		Optional<FacClose> facCloseT = null;
		if (dbName.equals(ContentName.onDay))
			facCloseT = facCloseReposDay.findByFacCloseId(facClose.getFacCloseId());
		else if (dbName.equals(ContentName.onMon))
			facCloseT = facCloseReposMon.findByFacCloseId(facClose.getFacCloseId());
		else if (dbName.equals(ContentName.onHist))
			facCloseT = facCloseReposHist.findByFacCloseId(facClose.getFacCloseId());
		else
			facCloseT = facCloseRepos.findByFacCloseId(facClose.getFacCloseId());
		return facCloseT.isPresent() ? facCloseT.get() : null;
	}

	@Override
	public FacClose insert(FacClose facClose, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + facClose.getFacCloseId());
		if (this.findById(facClose.getFacCloseId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			facClose.setCreateEmpNo(empNot);

		if (facClose.getLastUpdateEmpNo() == null || facClose.getLastUpdateEmpNo().isEmpty())
			facClose.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facCloseReposDay.saveAndFlush(facClose);
		else if (dbName.equals(ContentName.onMon))
			return facCloseReposMon.saveAndFlush(facClose);
		else if (dbName.equals(ContentName.onHist))
			return facCloseReposHist.saveAndFlush(facClose);
		else
			return facCloseRepos.saveAndFlush(facClose);
	}

	@Override
	public FacClose update(FacClose facClose, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + facClose.getFacCloseId());
		if (!empNot.isEmpty())
			facClose.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facCloseReposDay.saveAndFlush(facClose);
		else if (dbName.equals(ContentName.onMon))
			return facCloseReposMon.saveAndFlush(facClose);
		else if (dbName.equals(ContentName.onHist))
			return facCloseReposHist.saveAndFlush(facClose);
		else
			return facCloseRepos.saveAndFlush(facClose);
	}

	@Override
	public FacClose update2(FacClose facClose, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + facClose.getFacCloseId());
		if (!empNot.isEmpty())
			facClose.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			facCloseReposDay.saveAndFlush(facClose);
		else if (dbName.equals(ContentName.onMon))
			facCloseReposMon.saveAndFlush(facClose);
		else if (dbName.equals(ContentName.onHist))
			facCloseReposHist.saveAndFlush(facClose);
		else
			facCloseRepos.saveAndFlush(facClose);
		return this.findById(facClose.getFacCloseId());
	}

	@Override
	public void delete(FacClose facClose, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + facClose.getFacCloseId());
		if (dbName.equals(ContentName.onDay)) {
			facCloseReposDay.delete(facClose);
			facCloseReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facCloseReposMon.delete(facClose);
			facCloseReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facCloseReposHist.delete(facClose);
			facCloseReposHist.flush();
		} else {
			facCloseRepos.delete(facClose);
			facCloseRepos.flush();
		}
	}

	@Override
	public void insertAll(List<FacClose> facClose, TitaVo... titaVo) throws DBException {
		if (facClose == null || facClose.size() == 0)
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
		for (FacClose t : facClose) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			facClose = facCloseReposDay.saveAll(facClose);
			facCloseReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facClose = facCloseReposMon.saveAll(facClose);
			facCloseReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facClose = facCloseReposHist.saveAll(facClose);
			facCloseReposHist.flush();
		} else {
			facClose = facCloseRepos.saveAll(facClose);
			facCloseRepos.flush();
		}
	}

	@Override
	public void updateAll(List<FacClose> facClose, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (facClose == null || facClose.size() == 0)
			throw new DBException(6);

		for (FacClose t : facClose)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			facClose = facCloseReposDay.saveAll(facClose);
			facCloseReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facClose = facCloseReposMon.saveAll(facClose);
			facCloseReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facClose = facCloseReposHist.saveAll(facClose);
			facCloseReposHist.flush();
		} else {
			facClose = facCloseRepos.saveAll(facClose);
			facCloseRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<FacClose> facClose, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (facClose == null || facClose.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			facCloseReposDay.deleteAll(facClose);
			facCloseReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facCloseReposMon.deleteAll(facClose);
			facCloseReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facCloseReposHist.deleteAll(facClose);
			facCloseReposHist.flush();
		} else {
			facCloseRepos.deleteAll(facClose);
			facCloseRepos.flush();
		}
	}

}
