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
import com.st1.itx.db.domain.LoanIfrsIp;
import com.st1.itx.db.domain.LoanIfrsIpId;
import com.st1.itx.db.repository.online.LoanIfrsIpRepository;
import com.st1.itx.db.repository.day.LoanIfrsIpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrsIpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrsIpRepositoryHist;
import com.st1.itx.db.service.LoanIfrsIpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrsIpService")
@Repository
public class LoanIfrsIpServiceImpl extends ASpringJpaParm implements LoanIfrsIpService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanIfrsIpRepository loanIfrsIpRepos;

	@Autowired
	private LoanIfrsIpRepositoryDay loanIfrsIpReposDay;

	@Autowired
	private LoanIfrsIpRepositoryMon loanIfrsIpReposMon;

	@Autowired
	private LoanIfrsIpRepositoryHist loanIfrsIpReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanIfrsIpRepos);
		org.junit.Assert.assertNotNull(loanIfrsIpReposDay);
		org.junit.Assert.assertNotNull(loanIfrsIpReposMon);
		org.junit.Assert.assertNotNull(loanIfrsIpReposHist);
	}

	@Override
	public LoanIfrsIp findById(LoanIfrsIpId loanIfrsIpId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + loanIfrsIpId);
		Optional<LoanIfrsIp> loanIfrsIp = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrsIp = loanIfrsIpReposDay.findById(loanIfrsIpId);
		else if (dbName.equals(ContentName.onMon))
			loanIfrsIp = loanIfrsIpReposMon.findById(loanIfrsIpId);
		else if (dbName.equals(ContentName.onHist))
			loanIfrsIp = loanIfrsIpReposHist.findById(loanIfrsIpId);
		else
			loanIfrsIp = loanIfrsIpRepos.findById(loanIfrsIpId);
		LoanIfrsIp obj = loanIfrsIp.isPresent() ? loanIfrsIp.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<LoanIfrsIp> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanIfrsIp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = loanIfrsIpReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanIfrsIpReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanIfrsIpReposHist.findAll(pageable);
		else
			slice = loanIfrsIpRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public LoanIfrsIp holdById(LoanIfrsIpId loanIfrsIpId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanIfrsIpId);
		Optional<LoanIfrsIp> loanIfrsIp = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrsIp = loanIfrsIpReposDay.findByLoanIfrsIpId(loanIfrsIpId);
		else if (dbName.equals(ContentName.onMon))
			loanIfrsIp = loanIfrsIpReposMon.findByLoanIfrsIpId(loanIfrsIpId);
		else if (dbName.equals(ContentName.onHist))
			loanIfrsIp = loanIfrsIpReposHist.findByLoanIfrsIpId(loanIfrsIpId);
		else
			loanIfrsIp = loanIfrsIpRepos.findByLoanIfrsIpId(loanIfrsIpId);
		return loanIfrsIp.isPresent() ? loanIfrsIp.get() : null;
	}

	@Override
	public LoanIfrsIp holdById(LoanIfrsIp loanIfrsIp, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanIfrsIp.getLoanIfrsIpId());
		Optional<LoanIfrsIp> loanIfrsIpT = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrsIpT = loanIfrsIpReposDay.findByLoanIfrsIpId(loanIfrsIp.getLoanIfrsIpId());
		else if (dbName.equals(ContentName.onMon))
			loanIfrsIpT = loanIfrsIpReposMon.findByLoanIfrsIpId(loanIfrsIp.getLoanIfrsIpId());
		else if (dbName.equals(ContentName.onHist))
			loanIfrsIpT = loanIfrsIpReposHist.findByLoanIfrsIpId(loanIfrsIp.getLoanIfrsIpId());
		else
			loanIfrsIpT = loanIfrsIpRepos.findByLoanIfrsIpId(loanIfrsIp.getLoanIfrsIpId());
		return loanIfrsIpT.isPresent() ? loanIfrsIpT.get() : null;
	}

	@Override
	public LoanIfrsIp insert(LoanIfrsIp loanIfrsIp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + loanIfrsIp.getLoanIfrsIpId());
		if (this.findById(loanIfrsIp.getLoanIfrsIpId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			loanIfrsIp.setCreateEmpNo(empNot);

		if (loanIfrsIp.getLastUpdateEmpNo() == null || loanIfrsIp.getLastUpdateEmpNo().isEmpty())
			loanIfrsIp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanIfrsIpReposDay.saveAndFlush(loanIfrsIp);
		else if (dbName.equals(ContentName.onMon))
			return loanIfrsIpReposMon.saveAndFlush(loanIfrsIp);
		else if (dbName.equals(ContentName.onHist))
			return loanIfrsIpReposHist.saveAndFlush(loanIfrsIp);
		else
			return loanIfrsIpRepos.saveAndFlush(loanIfrsIp);
	}

	@Override
	public LoanIfrsIp update(LoanIfrsIp loanIfrsIp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanIfrsIp.getLoanIfrsIpId());
		if (!empNot.isEmpty())
			loanIfrsIp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanIfrsIpReposDay.saveAndFlush(loanIfrsIp);
		else if (dbName.equals(ContentName.onMon))
			return loanIfrsIpReposMon.saveAndFlush(loanIfrsIp);
		else if (dbName.equals(ContentName.onHist))
			return loanIfrsIpReposHist.saveAndFlush(loanIfrsIp);
		else
			return loanIfrsIpRepos.saveAndFlush(loanIfrsIp);
	}

	@Override
	public LoanIfrsIp update2(LoanIfrsIp loanIfrsIp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanIfrsIp.getLoanIfrsIpId());
		if (!empNot.isEmpty())
			loanIfrsIp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			loanIfrsIpReposDay.saveAndFlush(loanIfrsIp);
		else if (dbName.equals(ContentName.onMon))
			loanIfrsIpReposMon.saveAndFlush(loanIfrsIp);
		else if (dbName.equals(ContentName.onHist))
			loanIfrsIpReposHist.saveAndFlush(loanIfrsIp);
		else
			loanIfrsIpRepos.saveAndFlush(loanIfrsIp);
		return this.findById(loanIfrsIp.getLoanIfrsIpId());
	}

	@Override
	public void delete(LoanIfrsIp loanIfrsIp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + loanIfrsIp.getLoanIfrsIpId());
		if (dbName.equals(ContentName.onDay)) {
			loanIfrsIpReposDay.delete(loanIfrsIp);
			loanIfrsIpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrsIpReposMon.delete(loanIfrsIp);
			loanIfrsIpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrsIpReposHist.delete(loanIfrsIp);
			loanIfrsIpReposHist.flush();
		} else {
			loanIfrsIpRepos.delete(loanIfrsIp);
			loanIfrsIpRepos.flush();
		}
	}

	@Override
	public void insertAll(List<LoanIfrsIp> loanIfrsIp, TitaVo... titaVo) throws DBException {
		if (loanIfrsIp == null || loanIfrsIp.size() == 0)
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
		for (LoanIfrsIp t : loanIfrsIp) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			loanIfrsIp = loanIfrsIpReposDay.saveAll(loanIfrsIp);
			loanIfrsIpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrsIp = loanIfrsIpReposMon.saveAll(loanIfrsIp);
			loanIfrsIpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrsIp = loanIfrsIpReposHist.saveAll(loanIfrsIp);
			loanIfrsIpReposHist.flush();
		} else {
			loanIfrsIp = loanIfrsIpRepos.saveAll(loanIfrsIp);
			loanIfrsIpRepos.flush();
		}
	}

	@Override
	public void updateAll(List<LoanIfrsIp> loanIfrsIp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (loanIfrsIp == null || loanIfrsIp.size() == 0)
			throw new DBException(6);

		for (LoanIfrsIp t : loanIfrsIp)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanIfrsIp = loanIfrsIpReposDay.saveAll(loanIfrsIp);
			loanIfrsIpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrsIp = loanIfrsIpReposMon.saveAll(loanIfrsIp);
			loanIfrsIpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrsIp = loanIfrsIpReposHist.saveAll(loanIfrsIp);
			loanIfrsIpReposHist.flush();
		} else {
			loanIfrsIp = loanIfrsIpRepos.saveAll(loanIfrsIp);
			loanIfrsIpRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<LoanIfrsIp> loanIfrsIp, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (loanIfrsIp == null || loanIfrsIp.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			loanIfrsIpReposDay.deleteAll(loanIfrsIp);
			loanIfrsIpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrsIpReposMon.deleteAll(loanIfrsIp);
			loanIfrsIpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrsIpReposHist.deleteAll(loanIfrsIp);
			loanIfrsIpReposHist.flush();
		} else {
			loanIfrsIpRepos.deleteAll(loanIfrsIp);
			loanIfrsIpRepos.flush();
		}
	}

	@Override
	public void Usp_L7_LoanIfrsIp_Upd(int TBSDYF, String EmpNo, int NewAcFg, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			loanIfrsIpReposDay.uspL7LoanifrsipUpd(TBSDYF, EmpNo, NewAcFg);
		else if (dbName.equals(ContentName.onMon))
			loanIfrsIpReposMon.uspL7LoanifrsipUpd(TBSDYF, EmpNo, NewAcFg);
		else if (dbName.equals(ContentName.onHist))
			loanIfrsIpReposHist.uspL7LoanifrsipUpd(TBSDYF, EmpNo, NewAcFg);
		else
			loanIfrsIpRepos.uspL7LoanifrsipUpd(TBSDYF, EmpNo, NewAcFg);
	}

}
