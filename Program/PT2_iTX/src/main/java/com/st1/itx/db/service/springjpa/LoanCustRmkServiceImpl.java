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
import com.st1.itx.db.domain.LoanCustRmk;
import com.st1.itx.db.domain.LoanCustRmkId;
import com.st1.itx.db.repository.online.LoanCustRmkRepository;
import com.st1.itx.db.repository.day.LoanCustRmkRepositoryDay;
import com.st1.itx.db.repository.mon.LoanCustRmkRepositoryMon;
import com.st1.itx.db.repository.hist.LoanCustRmkRepositoryHist;
import com.st1.itx.db.service.LoanCustRmkService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanCustRmkService")
@Repository
public class LoanCustRmkServiceImpl extends ASpringJpaParm implements LoanCustRmkService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanCustRmkRepository loanCustRmkRepos;

	@Autowired
	private LoanCustRmkRepositoryDay loanCustRmkReposDay;

	@Autowired
	private LoanCustRmkRepositoryMon loanCustRmkReposMon;

	@Autowired
	private LoanCustRmkRepositoryHist loanCustRmkReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanCustRmkRepos);
		org.junit.Assert.assertNotNull(loanCustRmkReposDay);
		org.junit.Assert.assertNotNull(loanCustRmkReposMon);
		org.junit.Assert.assertNotNull(loanCustRmkReposHist);
	}

	@Override
	public LoanCustRmk findById(LoanCustRmkId loanCustRmkId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + loanCustRmkId);
		Optional<LoanCustRmk> loanCustRmk = null;
		if (dbName.equals(ContentName.onDay))
			loanCustRmk = loanCustRmkReposDay.findById(loanCustRmkId);
		else if (dbName.equals(ContentName.onMon))
			loanCustRmk = loanCustRmkReposMon.findById(loanCustRmkId);
		else if (dbName.equals(ContentName.onHist))
			loanCustRmk = loanCustRmkReposHist.findById(loanCustRmkId);
		else
			loanCustRmk = loanCustRmkRepos.findById(loanCustRmkId);
		LoanCustRmk obj = loanCustRmk.isPresent() ? loanCustRmk.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<LoanCustRmk> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanCustRmk> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "AcDate", "RmkNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "AcDate", "RmkNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = loanCustRmkReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanCustRmkReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanCustRmkReposHist.findAll(pageable);
		else
			slice = loanCustRmkRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanCustRmk> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanCustRmk> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = loanCustRmkReposDay.findAllByCustNoIsOrderByCustNoAscAcDateAscRmkNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanCustRmkReposMon.findAllByCustNoIsOrderByCustNoAscAcDateAscRmkNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanCustRmkReposHist.findAllByCustNoIsOrderByCustNoAscAcDateAscRmkNoAsc(custNo_0, pageable);
		else
			slice = loanCustRmkRepos.findAllByCustNoIsOrderByCustNoAscAcDateAscRmkNoAsc(custNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanCustRmk> findRmkCode(String rmkCode_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanCustRmk> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findRmkCode " + dbName + " : " + "rmkCode_0 : " + rmkCode_0);
		if (dbName.equals(ContentName.onDay))
			slice = loanCustRmkReposDay.findAllByRmkCodeIsOrderByCustNoAscAcDateAscRmkNoAsc(rmkCode_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanCustRmkReposMon.findAllByRmkCodeIsOrderByCustNoAscAcDateAscRmkNoAsc(rmkCode_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanCustRmkReposHist.findAllByRmkCodeIsOrderByCustNoAscAcDateAscRmkNoAsc(rmkCode_0, pageable);
		else
			slice = loanCustRmkRepos.findAllByRmkCodeIsOrderByCustNoAscAcDateAscRmkNoAsc(rmkCode_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public LoanCustRmk maxRmkNoFirst(int custNo_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("maxRmkNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0);
		Optional<LoanCustRmk> loanCustRmkT = null;
		if (dbName.equals(ContentName.onDay))
			loanCustRmkT = loanCustRmkReposDay.findTopByCustNoIsOrderByRmkNoDesc(custNo_0);
		else if (dbName.equals(ContentName.onMon))
			loanCustRmkT = loanCustRmkReposMon.findTopByCustNoIsOrderByRmkNoDesc(custNo_0);
		else if (dbName.equals(ContentName.onHist))
			loanCustRmkT = loanCustRmkReposHist.findTopByCustNoIsOrderByRmkNoDesc(custNo_0);
		else
			loanCustRmkT = loanCustRmkRepos.findTopByCustNoIsOrderByRmkNoDesc(custNo_0);

		return loanCustRmkT.isPresent() ? loanCustRmkT.get() : null;
	}

	@Override
	public LoanCustRmk holdById(LoanCustRmkId loanCustRmkId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanCustRmkId);
		Optional<LoanCustRmk> loanCustRmk = null;
		if (dbName.equals(ContentName.onDay))
			loanCustRmk = loanCustRmkReposDay.findByLoanCustRmkId(loanCustRmkId);
		else if (dbName.equals(ContentName.onMon))
			loanCustRmk = loanCustRmkReposMon.findByLoanCustRmkId(loanCustRmkId);
		else if (dbName.equals(ContentName.onHist))
			loanCustRmk = loanCustRmkReposHist.findByLoanCustRmkId(loanCustRmkId);
		else
			loanCustRmk = loanCustRmkRepos.findByLoanCustRmkId(loanCustRmkId);
		return loanCustRmk.isPresent() ? loanCustRmk.get() : null;
	}

	@Override
	public LoanCustRmk holdById(LoanCustRmk loanCustRmk, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanCustRmk.getLoanCustRmkId());
		Optional<LoanCustRmk> loanCustRmkT = null;
		if (dbName.equals(ContentName.onDay))
			loanCustRmkT = loanCustRmkReposDay.findByLoanCustRmkId(loanCustRmk.getLoanCustRmkId());
		else if (dbName.equals(ContentName.onMon))
			loanCustRmkT = loanCustRmkReposMon.findByLoanCustRmkId(loanCustRmk.getLoanCustRmkId());
		else if (dbName.equals(ContentName.onHist))
			loanCustRmkT = loanCustRmkReposHist.findByLoanCustRmkId(loanCustRmk.getLoanCustRmkId());
		else
			loanCustRmkT = loanCustRmkRepos.findByLoanCustRmkId(loanCustRmk.getLoanCustRmkId());
		return loanCustRmkT.isPresent() ? loanCustRmkT.get() : null;
	}

	@Override
	public LoanCustRmk insert(LoanCustRmk loanCustRmk, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + loanCustRmk.getLoanCustRmkId());
		if (this.findById(loanCustRmk.getLoanCustRmkId(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			loanCustRmk.setCreateEmpNo(empNot);

		if (loanCustRmk.getLastUpdateEmpNo() == null || loanCustRmk.getLastUpdateEmpNo().isEmpty())
			loanCustRmk.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanCustRmkReposDay.saveAndFlush(loanCustRmk);
		else if (dbName.equals(ContentName.onMon))
			return loanCustRmkReposMon.saveAndFlush(loanCustRmk);
		else if (dbName.equals(ContentName.onHist))
			return loanCustRmkReposHist.saveAndFlush(loanCustRmk);
		else
			return loanCustRmkRepos.saveAndFlush(loanCustRmk);
	}

	@Override
	public LoanCustRmk update(LoanCustRmk loanCustRmk, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanCustRmk.getLoanCustRmkId());
		if (!empNot.isEmpty())
			loanCustRmk.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanCustRmkReposDay.saveAndFlush(loanCustRmk);
		else if (dbName.equals(ContentName.onMon))
			return loanCustRmkReposMon.saveAndFlush(loanCustRmk);
		else if (dbName.equals(ContentName.onHist))
			return loanCustRmkReposHist.saveAndFlush(loanCustRmk);
		else
			return loanCustRmkRepos.saveAndFlush(loanCustRmk);
	}

	@Override
	public LoanCustRmk update2(LoanCustRmk loanCustRmk, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanCustRmk.getLoanCustRmkId());
		if (!empNot.isEmpty())
			loanCustRmk.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			loanCustRmkReposDay.saveAndFlush(loanCustRmk);
		else if (dbName.equals(ContentName.onMon))
			loanCustRmkReposMon.saveAndFlush(loanCustRmk);
		else if (dbName.equals(ContentName.onHist))
			loanCustRmkReposHist.saveAndFlush(loanCustRmk);
		else
			loanCustRmkRepos.saveAndFlush(loanCustRmk);
		return this.findById(loanCustRmk.getLoanCustRmkId());
	}

	@Override
	public void delete(LoanCustRmk loanCustRmk, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + loanCustRmk.getLoanCustRmkId());
		if (dbName.equals(ContentName.onDay)) {
			loanCustRmkReposDay.delete(loanCustRmk);
			loanCustRmkReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanCustRmkReposMon.delete(loanCustRmk);
			loanCustRmkReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanCustRmkReposHist.delete(loanCustRmk);
			loanCustRmkReposHist.flush();
		} else {
			loanCustRmkRepos.delete(loanCustRmk);
			loanCustRmkRepos.flush();
		}
	}

	@Override
	public void insertAll(List<LoanCustRmk> loanCustRmk, TitaVo... titaVo) throws DBException {
		if (loanCustRmk == null || loanCustRmk.size() == 0)
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
		for (LoanCustRmk t : loanCustRmk) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			loanCustRmk = loanCustRmkReposDay.saveAll(loanCustRmk);
			loanCustRmkReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanCustRmk = loanCustRmkReposMon.saveAll(loanCustRmk);
			loanCustRmkReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanCustRmk = loanCustRmkReposHist.saveAll(loanCustRmk);
			loanCustRmkReposHist.flush();
		} else {
			loanCustRmk = loanCustRmkRepos.saveAll(loanCustRmk);
			loanCustRmkRepos.flush();
		}
	}

	@Override
	public void updateAll(List<LoanCustRmk> loanCustRmk, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (loanCustRmk == null || loanCustRmk.size() == 0)
			throw new DBException(6);

		for (LoanCustRmk t : loanCustRmk)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanCustRmk = loanCustRmkReposDay.saveAll(loanCustRmk);
			loanCustRmkReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanCustRmk = loanCustRmkReposMon.saveAll(loanCustRmk);
			loanCustRmkReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanCustRmk = loanCustRmkReposHist.saveAll(loanCustRmk);
			loanCustRmkReposHist.flush();
		} else {
			loanCustRmk = loanCustRmkRepos.saveAll(loanCustRmk);
			loanCustRmkRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<LoanCustRmk> loanCustRmk, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (loanCustRmk == null || loanCustRmk.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			loanCustRmkReposDay.deleteAll(loanCustRmk);
			loanCustRmkReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanCustRmkReposMon.deleteAll(loanCustRmk);
			loanCustRmkReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanCustRmkReposHist.deleteAll(loanCustRmk);
			loanCustRmkReposHist.flush();
		} else {
			loanCustRmkRepos.deleteAll(loanCustRmk);
			loanCustRmkRepos.flush();
		}
	}

}
