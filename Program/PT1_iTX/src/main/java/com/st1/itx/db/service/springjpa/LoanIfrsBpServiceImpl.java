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
import com.st1.itx.db.domain.LoanIfrsBp;
import com.st1.itx.db.domain.LoanIfrsBpId;
import com.st1.itx.db.repository.online.LoanIfrsBpRepository;
import com.st1.itx.db.repository.day.LoanIfrsBpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrsBpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrsBpRepositoryHist;
import com.st1.itx.db.service.LoanIfrsBpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrsBpService")
@Repository
public class LoanIfrsBpServiceImpl extends ASpringJpaParm implements LoanIfrsBpService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanIfrsBpRepository loanIfrsBpRepos;

	@Autowired
	private LoanIfrsBpRepositoryDay loanIfrsBpReposDay;

	@Autowired
	private LoanIfrsBpRepositoryMon loanIfrsBpReposMon;

	@Autowired
	private LoanIfrsBpRepositoryHist loanIfrsBpReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanIfrsBpRepos);
		org.junit.Assert.assertNotNull(loanIfrsBpReposDay);
		org.junit.Assert.assertNotNull(loanIfrsBpReposMon);
		org.junit.Assert.assertNotNull(loanIfrsBpReposHist);
	}

	@Override
	public LoanIfrsBp findById(LoanIfrsBpId loanIfrsBpId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + loanIfrsBpId);
		Optional<LoanIfrsBp> loanIfrsBp = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrsBp = loanIfrsBpReposDay.findById(loanIfrsBpId);
		else if (dbName.equals(ContentName.onMon))
			loanIfrsBp = loanIfrsBpReposMon.findById(loanIfrsBpId);
		else if (dbName.equals(ContentName.onHist))
			loanIfrsBp = loanIfrsBpReposHist.findById(loanIfrsBpId);
		else
			loanIfrsBp = loanIfrsBpRepos.findById(loanIfrsBpId);
		LoanIfrsBp obj = loanIfrsBp.isPresent() ? loanIfrsBp.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<LoanIfrsBp> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanIfrsBp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo", "BormNo", "EffectDate"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = loanIfrsBpReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanIfrsBpReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanIfrsBpReposHist.findAll(pageable);
		else
			slice = loanIfrsBpRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public LoanIfrsBp holdById(LoanIfrsBpId loanIfrsBpId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanIfrsBpId);
		Optional<LoanIfrsBp> loanIfrsBp = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrsBp = loanIfrsBpReposDay.findByLoanIfrsBpId(loanIfrsBpId);
		else if (dbName.equals(ContentName.onMon))
			loanIfrsBp = loanIfrsBpReposMon.findByLoanIfrsBpId(loanIfrsBpId);
		else if (dbName.equals(ContentName.onHist))
			loanIfrsBp = loanIfrsBpReposHist.findByLoanIfrsBpId(loanIfrsBpId);
		else
			loanIfrsBp = loanIfrsBpRepos.findByLoanIfrsBpId(loanIfrsBpId);
		return loanIfrsBp.isPresent() ? loanIfrsBp.get() : null;
	}

	@Override
	public LoanIfrsBp holdById(LoanIfrsBp loanIfrsBp, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanIfrsBp.getLoanIfrsBpId());
		Optional<LoanIfrsBp> loanIfrsBpT = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrsBpT = loanIfrsBpReposDay.findByLoanIfrsBpId(loanIfrsBp.getLoanIfrsBpId());
		else if (dbName.equals(ContentName.onMon))
			loanIfrsBpT = loanIfrsBpReposMon.findByLoanIfrsBpId(loanIfrsBp.getLoanIfrsBpId());
		else if (dbName.equals(ContentName.onHist))
			loanIfrsBpT = loanIfrsBpReposHist.findByLoanIfrsBpId(loanIfrsBp.getLoanIfrsBpId());
		else
			loanIfrsBpT = loanIfrsBpRepos.findByLoanIfrsBpId(loanIfrsBp.getLoanIfrsBpId());
		return loanIfrsBpT.isPresent() ? loanIfrsBpT.get() : null;
	}

	@Override
	public LoanIfrsBp insert(LoanIfrsBp loanIfrsBp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + loanIfrsBp.getLoanIfrsBpId());
		if (this.findById(loanIfrsBp.getLoanIfrsBpId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			loanIfrsBp.setCreateEmpNo(empNot);

		if (loanIfrsBp.getLastUpdateEmpNo() == null || loanIfrsBp.getLastUpdateEmpNo().isEmpty())
			loanIfrsBp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanIfrsBpReposDay.saveAndFlush(loanIfrsBp);
		else if (dbName.equals(ContentName.onMon))
			return loanIfrsBpReposMon.saveAndFlush(loanIfrsBp);
		else if (dbName.equals(ContentName.onHist))
			return loanIfrsBpReposHist.saveAndFlush(loanIfrsBp);
		else
			return loanIfrsBpRepos.saveAndFlush(loanIfrsBp);
	}

	@Override
	public LoanIfrsBp update(LoanIfrsBp loanIfrsBp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanIfrsBp.getLoanIfrsBpId());
		if (!empNot.isEmpty())
			loanIfrsBp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanIfrsBpReposDay.saveAndFlush(loanIfrsBp);
		else if (dbName.equals(ContentName.onMon))
			return loanIfrsBpReposMon.saveAndFlush(loanIfrsBp);
		else if (dbName.equals(ContentName.onHist))
			return loanIfrsBpReposHist.saveAndFlush(loanIfrsBp);
		else
			return loanIfrsBpRepos.saveAndFlush(loanIfrsBp);
	}

	@Override
	public LoanIfrsBp update2(LoanIfrsBp loanIfrsBp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanIfrsBp.getLoanIfrsBpId());
		if (!empNot.isEmpty())
			loanIfrsBp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			loanIfrsBpReposDay.saveAndFlush(loanIfrsBp);
		else if (dbName.equals(ContentName.onMon))
			loanIfrsBpReposMon.saveAndFlush(loanIfrsBp);
		else if (dbName.equals(ContentName.onHist))
			loanIfrsBpReposHist.saveAndFlush(loanIfrsBp);
		else
			loanIfrsBpRepos.saveAndFlush(loanIfrsBp);
		return this.findById(loanIfrsBp.getLoanIfrsBpId());
	}

	@Override
	public void delete(LoanIfrsBp loanIfrsBp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + loanIfrsBp.getLoanIfrsBpId());
		if (dbName.equals(ContentName.onDay)) {
			loanIfrsBpReposDay.delete(loanIfrsBp);
			loanIfrsBpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrsBpReposMon.delete(loanIfrsBp);
			loanIfrsBpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrsBpReposHist.delete(loanIfrsBp);
			loanIfrsBpReposHist.flush();
		} else {
			loanIfrsBpRepos.delete(loanIfrsBp);
			loanIfrsBpRepos.flush();
		}
	}

	@Override
	public void insertAll(List<LoanIfrsBp> loanIfrsBp, TitaVo... titaVo) throws DBException {
		if (loanIfrsBp == null || loanIfrsBp.size() == 0)
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
		for (LoanIfrsBp t : loanIfrsBp) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			loanIfrsBp = loanIfrsBpReposDay.saveAll(loanIfrsBp);
			loanIfrsBpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrsBp = loanIfrsBpReposMon.saveAll(loanIfrsBp);
			loanIfrsBpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrsBp = loanIfrsBpReposHist.saveAll(loanIfrsBp);
			loanIfrsBpReposHist.flush();
		} else {
			loanIfrsBp = loanIfrsBpRepos.saveAll(loanIfrsBp);
			loanIfrsBpRepos.flush();
		}
	}

	@Override
	public void updateAll(List<LoanIfrsBp> loanIfrsBp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (loanIfrsBp == null || loanIfrsBp.size() == 0)
			throw new DBException(6);

		for (LoanIfrsBp t : loanIfrsBp)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanIfrsBp = loanIfrsBpReposDay.saveAll(loanIfrsBp);
			loanIfrsBpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrsBp = loanIfrsBpReposMon.saveAll(loanIfrsBp);
			loanIfrsBpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrsBp = loanIfrsBpReposHist.saveAll(loanIfrsBp);
			loanIfrsBpReposHist.flush();
		} else {
			loanIfrsBp = loanIfrsBpRepos.saveAll(loanIfrsBp);
			loanIfrsBpRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<LoanIfrsBp> loanIfrsBp, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (loanIfrsBp == null || loanIfrsBp.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			loanIfrsBpReposDay.deleteAll(loanIfrsBp);
			loanIfrsBpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrsBpReposMon.deleteAll(loanIfrsBp);
			loanIfrsBpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrsBpReposHist.deleteAll(loanIfrsBp);
			loanIfrsBpReposHist.flush();
		} else {
			loanIfrsBpRepos.deleteAll(loanIfrsBp);
			loanIfrsBpRepos.flush();
		}
	}

	@Override
	public void Usp_L7_LoanIfrsBp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			loanIfrsBpReposDay.uspL7LoanifrsbpUpd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onMon))
			loanIfrsBpReposMon.uspL7LoanifrsbpUpd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onHist))
			loanIfrsBpReposHist.uspL7LoanifrsbpUpd(TBSDYF, EmpNo);
		else
			loanIfrsBpRepos.uspL7LoanifrsbpUpd(TBSDYF, EmpNo);
	}

}
