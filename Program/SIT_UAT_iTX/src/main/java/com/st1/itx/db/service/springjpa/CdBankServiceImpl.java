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
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.repository.online.CdBankRepository;
import com.st1.itx.db.repository.day.CdBankRepositoryDay;
import com.st1.itx.db.repository.mon.CdBankRepositoryMon;
import com.st1.itx.db.repository.hist.CdBankRepositoryHist;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdBankService")
@Repository
public class CdBankServiceImpl extends ASpringJpaParm implements CdBankService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdBankRepository cdBankRepos;

	@Autowired
	private CdBankRepositoryDay cdBankReposDay;

	@Autowired
	private CdBankRepositoryMon cdBankReposMon;

	@Autowired
	private CdBankRepositoryHist cdBankReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdBankRepos);
		org.junit.Assert.assertNotNull(cdBankReposDay);
		org.junit.Assert.assertNotNull(cdBankReposMon);
		org.junit.Assert.assertNotNull(cdBankReposHist);
	}

	@Override
	public CdBank findById(CdBankId cdBankId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + cdBankId);
		Optional<CdBank> cdBank = null;
		if (dbName.equals(ContentName.onDay))
			cdBank = cdBankReposDay.findById(cdBankId);
		else if (dbName.equals(ContentName.onMon))
			cdBank = cdBankReposMon.findById(cdBankId);
		else if (dbName.equals(ContentName.onHist))
			cdBank = cdBankReposHist.findById(cdBankId);
		else
			cdBank = cdBankRepos.findById(cdBankId);
		CdBank obj = cdBank.isPresent() ? cdBank.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdBank> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBank> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "BankCode", "BranchCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BankCode", "BranchCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdBankReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBankReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBankReposHist.findAll(pageable);
		else
			slice = cdBankRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdBank> bankCodeLike(String bankCode_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBank> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("bankCodeLike " + dbName + " : " + "bankCode_0 : " + bankCode_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdBankReposDay.findAllByBankCodeLikeOrderByBankCodeAsc(bankCode_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBankReposMon.findAllByBankCodeLikeOrderByBankCodeAsc(bankCode_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBankReposHist.findAllByBankCodeLikeOrderByBankCodeAsc(bankCode_0, pageable);
		else
			slice = cdBankRepos.findAllByBankCodeLikeOrderByBankCodeAsc(bankCode_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdBank> branchCodeLike(String bankCode_0, String branchCode_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBank> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("branchCodeLike " + dbName + " : " + "bankCode_0 : " + bankCode_0 + " branchCode_1 : " + branchCode_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdBankReposDay.findAllByBankCodeLikeAndBranchCodeLikeOrderByBankCodeAscBranchCodeAsc(bankCode_0, branchCode_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBankReposMon.findAllByBankCodeLikeAndBranchCodeLikeOrderByBankCodeAscBranchCodeAsc(bankCode_0, branchCode_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBankReposHist.findAllByBankCodeLikeAndBranchCodeLikeOrderByBankCodeAscBranchCodeAsc(bankCode_0, branchCode_1, pageable);
		else
			slice = cdBankRepos.findAllByBankCodeLikeAndBranchCodeLikeOrderByBankCodeAscBranchCodeAsc(bankCode_0, branchCode_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdBank> bankItemLike(String bankItem_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBank> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("bankItemLike " + dbName + " : " + "bankItem_0 : " + bankItem_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdBankReposDay.findAllByBankItemLikeOrderByBankCodeAsc(bankItem_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBankReposMon.findAllByBankItemLikeOrderByBankCodeAsc(bankItem_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBankReposHist.findAllByBankItemLikeOrderByBankCodeAsc(bankItem_0, pageable);
		else
			slice = cdBankRepos.findAllByBankItemLikeOrderByBankCodeAsc(bankItem_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdBank holdById(CdBankId cdBankId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdBankId);
		Optional<CdBank> cdBank = null;
		if (dbName.equals(ContentName.onDay))
			cdBank = cdBankReposDay.findByCdBankId(cdBankId);
		else if (dbName.equals(ContentName.onMon))
			cdBank = cdBankReposMon.findByCdBankId(cdBankId);
		else if (dbName.equals(ContentName.onHist))
			cdBank = cdBankReposHist.findByCdBankId(cdBankId);
		else
			cdBank = cdBankRepos.findByCdBankId(cdBankId);
		return cdBank.isPresent() ? cdBank.get() : null;
	}

	@Override
	public CdBank holdById(CdBank cdBank, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdBank.getCdBankId());
		Optional<CdBank> cdBankT = null;
		if (dbName.equals(ContentName.onDay))
			cdBankT = cdBankReposDay.findByCdBankId(cdBank.getCdBankId());
		else if (dbName.equals(ContentName.onMon))
			cdBankT = cdBankReposMon.findByCdBankId(cdBank.getCdBankId());
		else if (dbName.equals(ContentName.onHist))
			cdBankT = cdBankReposHist.findByCdBankId(cdBank.getCdBankId());
		else
			cdBankT = cdBankRepos.findByCdBankId(cdBank.getCdBankId());
		return cdBankT.isPresent() ? cdBankT.get() : null;
	}

	@Override
	public CdBank insert(CdBank cdBank, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdBank.getCdBankId());
		if (this.findById(cdBank.getCdBankId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdBank.setCreateEmpNo(empNot);

		if (cdBank.getLastUpdateEmpNo() == null || cdBank.getLastUpdateEmpNo().isEmpty())
			cdBank.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdBankReposDay.saveAndFlush(cdBank);
		else if (dbName.equals(ContentName.onMon))
			return cdBankReposMon.saveAndFlush(cdBank);
		else if (dbName.equals(ContentName.onHist))
			return cdBankReposHist.saveAndFlush(cdBank);
		else
			return cdBankRepos.saveAndFlush(cdBank);
	}

	@Override
	public CdBank update(CdBank cdBank, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdBank.getCdBankId());
		if (!empNot.isEmpty())
			cdBank.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdBankReposDay.saveAndFlush(cdBank);
		else if (dbName.equals(ContentName.onMon))
			return cdBankReposMon.saveAndFlush(cdBank);
		else if (dbName.equals(ContentName.onHist))
			return cdBankReposHist.saveAndFlush(cdBank);
		else
			return cdBankRepos.saveAndFlush(cdBank);
	}

	@Override
	public CdBank update2(CdBank cdBank, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdBank.getCdBankId());
		if (!empNot.isEmpty())
			cdBank.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdBankReposDay.saveAndFlush(cdBank);
		else if (dbName.equals(ContentName.onMon))
			cdBankReposMon.saveAndFlush(cdBank);
		else if (dbName.equals(ContentName.onHist))
			cdBankReposHist.saveAndFlush(cdBank);
		else
			cdBankRepos.saveAndFlush(cdBank);
		return this.findById(cdBank.getCdBankId());
	}

	@Override
	public void delete(CdBank cdBank, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdBank.getCdBankId());
		if (dbName.equals(ContentName.onDay)) {
			cdBankReposDay.delete(cdBank);
			cdBankReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBankReposMon.delete(cdBank);
			cdBankReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBankReposHist.delete(cdBank);
			cdBankReposHist.flush();
		} else {
			cdBankRepos.delete(cdBank);
			cdBankRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdBank> cdBank, TitaVo... titaVo) throws DBException {
		if (cdBank == null || cdBank.size() == 0)
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
		for (CdBank t : cdBank) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdBank = cdBankReposDay.saveAll(cdBank);
			cdBankReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBank = cdBankReposMon.saveAll(cdBank);
			cdBankReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBank = cdBankReposHist.saveAll(cdBank);
			cdBankReposHist.flush();
		} else {
			cdBank = cdBankRepos.saveAll(cdBank);
			cdBankRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdBank> cdBank, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdBank == null || cdBank.size() == 0)
			throw new DBException(6);

		for (CdBank t : cdBank)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdBank = cdBankReposDay.saveAll(cdBank);
			cdBankReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBank = cdBankReposMon.saveAll(cdBank);
			cdBankReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBank = cdBankReposHist.saveAll(cdBank);
			cdBankReposHist.flush();
		} else {
			cdBank = cdBankRepos.saveAll(cdBank);
			cdBankRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdBank> cdBank, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdBank == null || cdBank.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdBankReposDay.deleteAll(cdBank);
			cdBankReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBankReposMon.deleteAll(cdBank);
			cdBankReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBankReposHist.deleteAll(cdBank);
			cdBankReposHist.flush();
		} else {
			cdBankRepos.deleteAll(cdBank);
			cdBankRepos.flush();
		}
	}

}
