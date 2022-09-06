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
import com.st1.itx.db.domain.LoanIfrs9Gp;
import com.st1.itx.db.domain.LoanIfrs9GpId;
import com.st1.itx.db.repository.online.LoanIfrs9GpRepository;
import com.st1.itx.db.repository.day.LoanIfrs9GpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrs9GpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrs9GpRepositoryHist;
import com.st1.itx.db.service.LoanIfrs9GpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrs9GpService")
@Repository
public class LoanIfrs9GpServiceImpl extends ASpringJpaParm implements LoanIfrs9GpService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanIfrs9GpRepository loanIfrs9GpRepos;

	@Autowired
	private LoanIfrs9GpRepositoryDay loanIfrs9GpReposDay;

	@Autowired
	private LoanIfrs9GpRepositoryMon loanIfrs9GpReposMon;

	@Autowired
	private LoanIfrs9GpRepositoryHist loanIfrs9GpReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanIfrs9GpRepos);
		org.junit.Assert.assertNotNull(loanIfrs9GpReposDay);
		org.junit.Assert.assertNotNull(loanIfrs9GpReposMon);
		org.junit.Assert.assertNotNull(loanIfrs9GpReposHist);
	}

	@Override
	public LoanIfrs9Gp findById(LoanIfrs9GpId loanIfrs9GpId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + loanIfrs9GpId);
		Optional<LoanIfrs9Gp> loanIfrs9Gp = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrs9Gp = loanIfrs9GpReposDay.findById(loanIfrs9GpId);
		else if (dbName.equals(ContentName.onMon))
			loanIfrs9Gp = loanIfrs9GpReposMon.findById(loanIfrs9GpId);
		else if (dbName.equals(ContentName.onHist))
			loanIfrs9Gp = loanIfrs9GpReposHist.findById(loanIfrs9GpId);
		else
			loanIfrs9Gp = loanIfrs9GpRepos.findById(loanIfrs9GpId);
		LoanIfrs9Gp obj = loanIfrs9Gp.isPresent() ? loanIfrs9Gp.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<LoanIfrs9Gp> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanIfrs9Gp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = loanIfrs9GpReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanIfrs9GpReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanIfrs9GpReposHist.findAll(pageable);
		else
			slice = loanIfrs9GpRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public LoanIfrs9Gp holdById(LoanIfrs9GpId loanIfrs9GpId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanIfrs9GpId);
		Optional<LoanIfrs9Gp> loanIfrs9Gp = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrs9Gp = loanIfrs9GpReposDay.findByLoanIfrs9GpId(loanIfrs9GpId);
		else if (dbName.equals(ContentName.onMon))
			loanIfrs9Gp = loanIfrs9GpReposMon.findByLoanIfrs9GpId(loanIfrs9GpId);
		else if (dbName.equals(ContentName.onHist))
			loanIfrs9Gp = loanIfrs9GpReposHist.findByLoanIfrs9GpId(loanIfrs9GpId);
		else
			loanIfrs9Gp = loanIfrs9GpRepos.findByLoanIfrs9GpId(loanIfrs9GpId);
		return loanIfrs9Gp.isPresent() ? loanIfrs9Gp.get() : null;
	}

	@Override
	public LoanIfrs9Gp holdById(LoanIfrs9Gp loanIfrs9Gp, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanIfrs9Gp.getLoanIfrs9GpId());
		Optional<LoanIfrs9Gp> loanIfrs9GpT = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrs9GpT = loanIfrs9GpReposDay.findByLoanIfrs9GpId(loanIfrs9Gp.getLoanIfrs9GpId());
		else if (dbName.equals(ContentName.onMon))
			loanIfrs9GpT = loanIfrs9GpReposMon.findByLoanIfrs9GpId(loanIfrs9Gp.getLoanIfrs9GpId());
		else if (dbName.equals(ContentName.onHist))
			loanIfrs9GpT = loanIfrs9GpReposHist.findByLoanIfrs9GpId(loanIfrs9Gp.getLoanIfrs9GpId());
		else
			loanIfrs9GpT = loanIfrs9GpRepos.findByLoanIfrs9GpId(loanIfrs9Gp.getLoanIfrs9GpId());
		return loanIfrs9GpT.isPresent() ? loanIfrs9GpT.get() : null;
	}

	@Override
	public LoanIfrs9Gp insert(LoanIfrs9Gp loanIfrs9Gp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + loanIfrs9Gp.getLoanIfrs9GpId());
		if (this.findById(loanIfrs9Gp.getLoanIfrs9GpId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			loanIfrs9Gp.setCreateEmpNo(empNot);

		if (loanIfrs9Gp.getLastUpdateEmpNo() == null || loanIfrs9Gp.getLastUpdateEmpNo().isEmpty())
			loanIfrs9Gp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanIfrs9GpReposDay.saveAndFlush(loanIfrs9Gp);
		else if (dbName.equals(ContentName.onMon))
			return loanIfrs9GpReposMon.saveAndFlush(loanIfrs9Gp);
		else if (dbName.equals(ContentName.onHist))
			return loanIfrs9GpReposHist.saveAndFlush(loanIfrs9Gp);
		else
			return loanIfrs9GpRepos.saveAndFlush(loanIfrs9Gp);
	}

	@Override
	public LoanIfrs9Gp update(LoanIfrs9Gp loanIfrs9Gp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanIfrs9Gp.getLoanIfrs9GpId());
		if (!empNot.isEmpty())
			loanIfrs9Gp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanIfrs9GpReposDay.saveAndFlush(loanIfrs9Gp);
		else if (dbName.equals(ContentName.onMon))
			return loanIfrs9GpReposMon.saveAndFlush(loanIfrs9Gp);
		else if (dbName.equals(ContentName.onHist))
			return loanIfrs9GpReposHist.saveAndFlush(loanIfrs9Gp);
		else
			return loanIfrs9GpRepos.saveAndFlush(loanIfrs9Gp);
	}

	@Override
	public LoanIfrs9Gp update2(LoanIfrs9Gp loanIfrs9Gp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanIfrs9Gp.getLoanIfrs9GpId());
		if (!empNot.isEmpty())
			loanIfrs9Gp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			loanIfrs9GpReposDay.saveAndFlush(loanIfrs9Gp);
		else if (dbName.equals(ContentName.onMon))
			loanIfrs9GpReposMon.saveAndFlush(loanIfrs9Gp);
		else if (dbName.equals(ContentName.onHist))
			loanIfrs9GpReposHist.saveAndFlush(loanIfrs9Gp);
		else
			loanIfrs9GpRepos.saveAndFlush(loanIfrs9Gp);
		return this.findById(loanIfrs9Gp.getLoanIfrs9GpId());
	}

	@Override
	public void delete(LoanIfrs9Gp loanIfrs9Gp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + loanIfrs9Gp.getLoanIfrs9GpId());
		if (dbName.equals(ContentName.onDay)) {
			loanIfrs9GpReposDay.delete(loanIfrs9Gp);
			loanIfrs9GpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrs9GpReposMon.delete(loanIfrs9Gp);
			loanIfrs9GpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrs9GpReposHist.delete(loanIfrs9Gp);
			loanIfrs9GpReposHist.flush();
		} else {
			loanIfrs9GpRepos.delete(loanIfrs9Gp);
			loanIfrs9GpRepos.flush();
		}
	}

	@Override
	public void insertAll(List<LoanIfrs9Gp> loanIfrs9Gp, TitaVo... titaVo) throws DBException {
		if (loanIfrs9Gp == null || loanIfrs9Gp.size() == 0)
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
		for (LoanIfrs9Gp t : loanIfrs9Gp) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			loanIfrs9Gp = loanIfrs9GpReposDay.saveAll(loanIfrs9Gp);
			loanIfrs9GpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrs9Gp = loanIfrs9GpReposMon.saveAll(loanIfrs9Gp);
			loanIfrs9GpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrs9Gp = loanIfrs9GpReposHist.saveAll(loanIfrs9Gp);
			loanIfrs9GpReposHist.flush();
		} else {
			loanIfrs9Gp = loanIfrs9GpRepos.saveAll(loanIfrs9Gp);
			loanIfrs9GpRepos.flush();
		}
	}

	@Override
	public void updateAll(List<LoanIfrs9Gp> loanIfrs9Gp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (loanIfrs9Gp == null || loanIfrs9Gp.size() == 0)
			throw new DBException(6);

		for (LoanIfrs9Gp t : loanIfrs9Gp)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanIfrs9Gp = loanIfrs9GpReposDay.saveAll(loanIfrs9Gp);
			loanIfrs9GpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrs9Gp = loanIfrs9GpReposMon.saveAll(loanIfrs9Gp);
			loanIfrs9GpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrs9Gp = loanIfrs9GpReposHist.saveAll(loanIfrs9Gp);
			loanIfrs9GpReposHist.flush();
		} else {
			loanIfrs9Gp = loanIfrs9GpRepos.saveAll(loanIfrs9Gp);
			loanIfrs9GpRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<LoanIfrs9Gp> loanIfrs9Gp, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (loanIfrs9Gp == null || loanIfrs9Gp.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			loanIfrs9GpReposDay.deleteAll(loanIfrs9Gp);
			loanIfrs9GpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrs9GpReposMon.deleteAll(loanIfrs9Gp);
			loanIfrs9GpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrs9GpReposHist.deleteAll(loanIfrs9Gp);
			loanIfrs9GpReposHist.flush();
		} else {
			loanIfrs9GpRepos.deleteAll(loanIfrs9Gp);
			loanIfrs9GpRepos.flush();
		}
	}

	@Override
	public void Usp_L7_LoanIfrs9Gp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			loanIfrs9GpReposDay.uspL7Loanifrs9gpUpd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onMon))
			loanIfrs9GpReposMon.uspL7Loanifrs9gpUpd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onHist))
			loanIfrs9GpReposHist.uspL7Loanifrs9gpUpd(TBSDYF, EmpNo);
		else
			loanIfrs9GpRepos.uspL7Loanifrs9gpUpd(TBSDYF, EmpNo);
	}

}
