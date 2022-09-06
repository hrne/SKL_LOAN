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
import com.st1.itx.db.domain.LoanFacTmp;
import com.st1.itx.db.domain.LoanFacTmpId;
import com.st1.itx.db.repository.online.LoanFacTmpRepository;
import com.st1.itx.db.repository.day.LoanFacTmpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanFacTmpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanFacTmpRepositoryHist;
import com.st1.itx.db.service.LoanFacTmpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanFacTmpService")
@Repository
public class LoanFacTmpServiceImpl extends ASpringJpaParm implements LoanFacTmpService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanFacTmpRepository loanFacTmpRepos;

	@Autowired
	private LoanFacTmpRepositoryDay loanFacTmpReposDay;

	@Autowired
	private LoanFacTmpRepositoryMon loanFacTmpReposMon;

	@Autowired
	private LoanFacTmpRepositoryHist loanFacTmpReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanFacTmpRepos);
		org.junit.Assert.assertNotNull(loanFacTmpReposDay);
		org.junit.Assert.assertNotNull(loanFacTmpReposMon);
		org.junit.Assert.assertNotNull(loanFacTmpReposHist);
	}

	@Override
	public LoanFacTmp findById(LoanFacTmpId loanFacTmpId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + loanFacTmpId);
		Optional<LoanFacTmp> loanFacTmp = null;
		if (dbName.equals(ContentName.onDay))
			loanFacTmp = loanFacTmpReposDay.findById(loanFacTmpId);
		else if (dbName.equals(ContentName.onMon))
			loanFacTmp = loanFacTmpReposMon.findById(loanFacTmpId);
		else if (dbName.equals(ContentName.onHist))
			loanFacTmp = loanFacTmpReposHist.findById(loanFacTmpId);
		else
			loanFacTmp = loanFacTmpRepos.findById(loanFacTmpId);
		LoanFacTmp obj = loanFacTmp.isPresent() ? loanFacTmp.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<LoanFacTmp> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanFacTmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = loanFacTmpReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanFacTmpReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanFacTmpReposHist.findAll(pageable);
		else
			slice = loanFacTmpRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<LoanFacTmp> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanFacTmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = loanFacTmpReposDay.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanFacTmpReposMon.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanFacTmpReposHist.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
		else
			slice = loanFacTmpRepos.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public LoanFacTmp holdById(LoanFacTmpId loanFacTmpId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanFacTmpId);
		Optional<LoanFacTmp> loanFacTmp = null;
		if (dbName.equals(ContentName.onDay))
			loanFacTmp = loanFacTmpReposDay.findByLoanFacTmpId(loanFacTmpId);
		else if (dbName.equals(ContentName.onMon))
			loanFacTmp = loanFacTmpReposMon.findByLoanFacTmpId(loanFacTmpId);
		else if (dbName.equals(ContentName.onHist))
			loanFacTmp = loanFacTmpReposHist.findByLoanFacTmpId(loanFacTmpId);
		else
			loanFacTmp = loanFacTmpRepos.findByLoanFacTmpId(loanFacTmpId);
		return loanFacTmp.isPresent() ? loanFacTmp.get() : null;
	}

	@Override
	public LoanFacTmp holdById(LoanFacTmp loanFacTmp, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanFacTmp.getLoanFacTmpId());
		Optional<LoanFacTmp> loanFacTmpT = null;
		if (dbName.equals(ContentName.onDay))
			loanFacTmpT = loanFacTmpReposDay.findByLoanFacTmpId(loanFacTmp.getLoanFacTmpId());
		else if (dbName.equals(ContentName.onMon))
			loanFacTmpT = loanFacTmpReposMon.findByLoanFacTmpId(loanFacTmp.getLoanFacTmpId());
		else if (dbName.equals(ContentName.onHist))
			loanFacTmpT = loanFacTmpReposHist.findByLoanFacTmpId(loanFacTmp.getLoanFacTmpId());
		else
			loanFacTmpT = loanFacTmpRepos.findByLoanFacTmpId(loanFacTmp.getLoanFacTmpId());
		return loanFacTmpT.isPresent() ? loanFacTmpT.get() : null;
	}

	@Override
	public LoanFacTmp insert(LoanFacTmp loanFacTmp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + loanFacTmp.getLoanFacTmpId());
		if (this.findById(loanFacTmp.getLoanFacTmpId(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			loanFacTmp.setCreateEmpNo(empNot);

		if (loanFacTmp.getLastUpdateEmpNo() == null || loanFacTmp.getLastUpdateEmpNo().isEmpty())
			loanFacTmp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanFacTmpReposDay.saveAndFlush(loanFacTmp);
		else if (dbName.equals(ContentName.onMon))
			return loanFacTmpReposMon.saveAndFlush(loanFacTmp);
		else if (dbName.equals(ContentName.onHist))
			return loanFacTmpReposHist.saveAndFlush(loanFacTmp);
		else
			return loanFacTmpRepos.saveAndFlush(loanFacTmp);
	}

	@Override
	public LoanFacTmp update(LoanFacTmp loanFacTmp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanFacTmp.getLoanFacTmpId());
		if (!empNot.isEmpty())
			loanFacTmp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanFacTmpReposDay.saveAndFlush(loanFacTmp);
		else if (dbName.equals(ContentName.onMon))
			return loanFacTmpReposMon.saveAndFlush(loanFacTmp);
		else if (dbName.equals(ContentName.onHist))
			return loanFacTmpReposHist.saveAndFlush(loanFacTmp);
		else
			return loanFacTmpRepos.saveAndFlush(loanFacTmp);
	}

	@Override
	public LoanFacTmp update2(LoanFacTmp loanFacTmp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanFacTmp.getLoanFacTmpId());
		if (!empNot.isEmpty())
			loanFacTmp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			loanFacTmpReposDay.saveAndFlush(loanFacTmp);
		else if (dbName.equals(ContentName.onMon))
			loanFacTmpReposMon.saveAndFlush(loanFacTmp);
		else if (dbName.equals(ContentName.onHist))
			loanFacTmpReposHist.saveAndFlush(loanFacTmp);
		else
			loanFacTmpRepos.saveAndFlush(loanFacTmp);
		return this.findById(loanFacTmp.getLoanFacTmpId());
	}

	@Override
	public void delete(LoanFacTmp loanFacTmp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + loanFacTmp.getLoanFacTmpId());
		if (dbName.equals(ContentName.onDay)) {
			loanFacTmpReposDay.delete(loanFacTmp);
			loanFacTmpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanFacTmpReposMon.delete(loanFacTmp);
			loanFacTmpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanFacTmpReposHist.delete(loanFacTmp);
			loanFacTmpReposHist.flush();
		} else {
			loanFacTmpRepos.delete(loanFacTmp);
			loanFacTmpRepos.flush();
		}
	}

	@Override
	public void insertAll(List<LoanFacTmp> loanFacTmp, TitaVo... titaVo) throws DBException {
		if (loanFacTmp == null || loanFacTmp.size() == 0)
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
		for (LoanFacTmp t : loanFacTmp) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			loanFacTmp = loanFacTmpReposDay.saveAll(loanFacTmp);
			loanFacTmpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanFacTmp = loanFacTmpReposMon.saveAll(loanFacTmp);
			loanFacTmpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanFacTmp = loanFacTmpReposHist.saveAll(loanFacTmp);
			loanFacTmpReposHist.flush();
		} else {
			loanFacTmp = loanFacTmpRepos.saveAll(loanFacTmp);
			loanFacTmpRepos.flush();
		}
	}

	@Override
	public void updateAll(List<LoanFacTmp> loanFacTmp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (loanFacTmp == null || loanFacTmp.size() == 0)
			throw new DBException(6);

		for (LoanFacTmp t : loanFacTmp)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanFacTmp = loanFacTmpReposDay.saveAll(loanFacTmp);
			loanFacTmpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanFacTmp = loanFacTmpReposMon.saveAll(loanFacTmp);
			loanFacTmpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanFacTmp = loanFacTmpReposHist.saveAll(loanFacTmp);
			loanFacTmpReposHist.flush();
		} else {
			loanFacTmp = loanFacTmpRepos.saveAll(loanFacTmp);
			loanFacTmpRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<LoanFacTmp> loanFacTmp, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (loanFacTmp == null || loanFacTmp.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			loanFacTmpReposDay.deleteAll(loanFacTmp);
			loanFacTmpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanFacTmpReposMon.deleteAll(loanFacTmp);
			loanFacTmpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanFacTmpReposHist.deleteAll(loanFacTmp);
			loanFacTmpReposHist.flush();
		} else {
			loanFacTmpRepos.deleteAll(loanFacTmp);
			loanFacTmpRepos.flush();
		}
	}

}
