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
import com.st1.itx.db.domain.LoanIfrs9Hp;
import com.st1.itx.db.domain.LoanIfrs9HpId;
import com.st1.itx.db.repository.online.LoanIfrs9HpRepository;
import com.st1.itx.db.repository.day.LoanIfrs9HpRepositoryDay;
import com.st1.itx.db.repository.mon.LoanIfrs9HpRepositoryMon;
import com.st1.itx.db.repository.hist.LoanIfrs9HpRepositoryHist;
import com.st1.itx.db.service.LoanIfrs9HpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("loanIfrs9HpService")
@Repository
public class LoanIfrs9HpServiceImpl extends ASpringJpaParm implements LoanIfrs9HpService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanIfrs9HpRepository loanIfrs9HpRepos;

	@Autowired
	private LoanIfrs9HpRepositoryDay loanIfrs9HpReposDay;

	@Autowired
	private LoanIfrs9HpRepositoryMon loanIfrs9HpReposMon;

	@Autowired
	private LoanIfrs9HpRepositoryHist loanIfrs9HpReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanIfrs9HpRepos);
		org.junit.Assert.assertNotNull(loanIfrs9HpReposDay);
		org.junit.Assert.assertNotNull(loanIfrs9HpReposMon);
		org.junit.Assert.assertNotNull(loanIfrs9HpReposHist);
	}

	@Override
	public LoanIfrs9Hp findById(LoanIfrs9HpId loanIfrs9HpId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + loanIfrs9HpId);
		Optional<LoanIfrs9Hp> loanIfrs9Hp = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrs9Hp = loanIfrs9HpReposDay.findById(loanIfrs9HpId);
		else if (dbName.equals(ContentName.onMon))
			loanIfrs9Hp = loanIfrs9HpReposMon.findById(loanIfrs9HpId);
		else if (dbName.equals(ContentName.onHist))
			loanIfrs9Hp = loanIfrs9HpReposHist.findById(loanIfrs9HpId);
		else
			loanIfrs9Hp = loanIfrs9HpRepos.findById(loanIfrs9HpId);
		LoanIfrs9Hp obj = loanIfrs9Hp.isPresent() ? loanIfrs9Hp.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<LoanIfrs9Hp> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<LoanIfrs9Hp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustNo", "FacmNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = loanIfrs9HpReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = loanIfrs9HpReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = loanIfrs9HpReposHist.findAll(pageable);
		else
			slice = loanIfrs9HpRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public LoanIfrs9Hp holdById(LoanIfrs9HpId loanIfrs9HpId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanIfrs9HpId);
		Optional<LoanIfrs9Hp> loanIfrs9Hp = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrs9Hp = loanIfrs9HpReposDay.findByLoanIfrs9HpId(loanIfrs9HpId);
		else if (dbName.equals(ContentName.onMon))
			loanIfrs9Hp = loanIfrs9HpReposMon.findByLoanIfrs9HpId(loanIfrs9HpId);
		else if (dbName.equals(ContentName.onHist))
			loanIfrs9Hp = loanIfrs9HpReposHist.findByLoanIfrs9HpId(loanIfrs9HpId);
		else
			loanIfrs9Hp = loanIfrs9HpRepos.findByLoanIfrs9HpId(loanIfrs9HpId);
		return loanIfrs9Hp.isPresent() ? loanIfrs9Hp.get() : null;
	}

	@Override
	public LoanIfrs9Hp holdById(LoanIfrs9Hp loanIfrs9Hp, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + loanIfrs9Hp.getLoanIfrs9HpId());
		Optional<LoanIfrs9Hp> loanIfrs9HpT = null;
		if (dbName.equals(ContentName.onDay))
			loanIfrs9HpT = loanIfrs9HpReposDay.findByLoanIfrs9HpId(loanIfrs9Hp.getLoanIfrs9HpId());
		else if (dbName.equals(ContentName.onMon))
			loanIfrs9HpT = loanIfrs9HpReposMon.findByLoanIfrs9HpId(loanIfrs9Hp.getLoanIfrs9HpId());
		else if (dbName.equals(ContentName.onHist))
			loanIfrs9HpT = loanIfrs9HpReposHist.findByLoanIfrs9HpId(loanIfrs9Hp.getLoanIfrs9HpId());
		else
			loanIfrs9HpT = loanIfrs9HpRepos.findByLoanIfrs9HpId(loanIfrs9Hp.getLoanIfrs9HpId());
		return loanIfrs9HpT.isPresent() ? loanIfrs9HpT.get() : null;
	}

	@Override
	public LoanIfrs9Hp insert(LoanIfrs9Hp loanIfrs9Hp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + loanIfrs9Hp.getLoanIfrs9HpId());
		if (this.findById(loanIfrs9Hp.getLoanIfrs9HpId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			loanIfrs9Hp.setCreateEmpNo(empNot);

		if (loanIfrs9Hp.getLastUpdateEmpNo() == null || loanIfrs9Hp.getLastUpdateEmpNo().isEmpty())
			loanIfrs9Hp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanIfrs9HpReposDay.saveAndFlush(loanIfrs9Hp);
		else if (dbName.equals(ContentName.onMon))
			return loanIfrs9HpReposMon.saveAndFlush(loanIfrs9Hp);
		else if (dbName.equals(ContentName.onHist))
			return loanIfrs9HpReposHist.saveAndFlush(loanIfrs9Hp);
		else
			return loanIfrs9HpRepos.saveAndFlush(loanIfrs9Hp);
	}

	@Override
	public LoanIfrs9Hp update(LoanIfrs9Hp loanIfrs9Hp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanIfrs9Hp.getLoanIfrs9HpId());
		if (!empNot.isEmpty())
			loanIfrs9Hp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return loanIfrs9HpReposDay.saveAndFlush(loanIfrs9Hp);
		else if (dbName.equals(ContentName.onMon))
			return loanIfrs9HpReposMon.saveAndFlush(loanIfrs9Hp);
		else if (dbName.equals(ContentName.onHist))
			return loanIfrs9HpReposHist.saveAndFlush(loanIfrs9Hp);
		else
			return loanIfrs9HpRepos.saveAndFlush(loanIfrs9Hp);
	}

	@Override
	public LoanIfrs9Hp update2(LoanIfrs9Hp loanIfrs9Hp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + loanIfrs9Hp.getLoanIfrs9HpId());
		if (!empNot.isEmpty())
			loanIfrs9Hp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			loanIfrs9HpReposDay.saveAndFlush(loanIfrs9Hp);
		else if (dbName.equals(ContentName.onMon))
			loanIfrs9HpReposMon.saveAndFlush(loanIfrs9Hp);
		else if (dbName.equals(ContentName.onHist))
			loanIfrs9HpReposHist.saveAndFlush(loanIfrs9Hp);
		else
			loanIfrs9HpRepos.saveAndFlush(loanIfrs9Hp);
		return this.findById(loanIfrs9Hp.getLoanIfrs9HpId());
	}

	@Override
	public void delete(LoanIfrs9Hp loanIfrs9Hp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + loanIfrs9Hp.getLoanIfrs9HpId());
		if (dbName.equals(ContentName.onDay)) {
			loanIfrs9HpReposDay.delete(loanIfrs9Hp);
			loanIfrs9HpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrs9HpReposMon.delete(loanIfrs9Hp);
			loanIfrs9HpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrs9HpReposHist.delete(loanIfrs9Hp);
			loanIfrs9HpReposHist.flush();
		} else {
			loanIfrs9HpRepos.delete(loanIfrs9Hp);
			loanIfrs9HpRepos.flush();
		}
	}

	@Override
	public void insertAll(List<LoanIfrs9Hp> loanIfrs9Hp, TitaVo... titaVo) throws DBException {
		if (loanIfrs9Hp == null || loanIfrs9Hp.size() == 0)
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
		for (LoanIfrs9Hp t : loanIfrs9Hp) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			loanIfrs9Hp = loanIfrs9HpReposDay.saveAll(loanIfrs9Hp);
			loanIfrs9HpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrs9Hp = loanIfrs9HpReposMon.saveAll(loanIfrs9Hp);
			loanIfrs9HpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrs9Hp = loanIfrs9HpReposHist.saveAll(loanIfrs9Hp);
			loanIfrs9HpReposHist.flush();
		} else {
			loanIfrs9Hp = loanIfrs9HpRepos.saveAll(loanIfrs9Hp);
			loanIfrs9HpRepos.flush();
		}
	}

	@Override
	public void updateAll(List<LoanIfrs9Hp> loanIfrs9Hp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (loanIfrs9Hp == null || loanIfrs9Hp.size() == 0)
			throw new DBException(6);

		for (LoanIfrs9Hp t : loanIfrs9Hp)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			loanIfrs9Hp = loanIfrs9HpReposDay.saveAll(loanIfrs9Hp);
			loanIfrs9HpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrs9Hp = loanIfrs9HpReposMon.saveAll(loanIfrs9Hp);
			loanIfrs9HpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrs9Hp = loanIfrs9HpReposHist.saveAll(loanIfrs9Hp);
			loanIfrs9HpReposHist.flush();
		} else {
			loanIfrs9Hp = loanIfrs9HpRepos.saveAll(loanIfrs9Hp);
			loanIfrs9HpRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<LoanIfrs9Hp> loanIfrs9Hp, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (loanIfrs9Hp == null || loanIfrs9Hp.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			loanIfrs9HpReposDay.deleteAll(loanIfrs9Hp);
			loanIfrs9HpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			loanIfrs9HpReposMon.deleteAll(loanIfrs9Hp);
			loanIfrs9HpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			loanIfrs9HpReposHist.deleteAll(loanIfrs9Hp);
			loanIfrs9HpReposHist.flush();
		} else {
			loanIfrs9HpRepos.deleteAll(loanIfrs9Hp);
			loanIfrs9HpRepos.flush();
		}
	}

	@Override
	public void Usp_L7_LoanIfrs9Hp_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			loanIfrs9HpReposDay.uspL7Loanifrs9hpUpd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onMon))
			loanIfrs9HpReposMon.uspL7Loanifrs9hpUpd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onHist))
			loanIfrs9HpReposHist.uspL7Loanifrs9hpUpd(TBSDYF, EmpNo);
		else
			loanIfrs9HpRepos.uspL7Loanifrs9hpUpd(TBSDYF, EmpNo);
	}

}
