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
import com.st1.itx.db.domain.CdBankOld;
import com.st1.itx.db.domain.CdBankOldId;
import com.st1.itx.db.repository.online.CdBankOldRepository;
import com.st1.itx.db.repository.day.CdBankOldRepositoryDay;
import com.st1.itx.db.repository.mon.CdBankOldRepositoryMon;
import com.st1.itx.db.repository.hist.CdBankOldRepositoryHist;
import com.st1.itx.db.service.CdBankOldService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdBankOldService")
@Repository
public class CdBankOldServiceImpl extends ASpringJpaParm implements CdBankOldService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdBankOldRepository cdBankOldRepos;

	@Autowired
	private CdBankOldRepositoryDay cdBankOldReposDay;

	@Autowired
	private CdBankOldRepositoryMon cdBankOldReposMon;

	@Autowired
	private CdBankOldRepositoryHist cdBankOldReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdBankOldRepos);
		org.junit.Assert.assertNotNull(cdBankOldReposDay);
		org.junit.Assert.assertNotNull(cdBankOldReposMon);
		org.junit.Assert.assertNotNull(cdBankOldReposHist);
	}

	@Override
	public CdBankOld findById(CdBankOldId cdBankOldId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + cdBankOldId);
		Optional<CdBankOld> cdBankOld = null;
		if (dbName.equals(ContentName.onDay))
			cdBankOld = cdBankOldReposDay.findById(cdBankOldId);
		else if (dbName.equals(ContentName.onMon))
			cdBankOld = cdBankOldReposMon.findById(cdBankOldId);
		else if (dbName.equals(ContentName.onHist))
			cdBankOld = cdBankOldReposHist.findById(cdBankOldId);
		else
			cdBankOld = cdBankOldRepos.findById(cdBankOldId);
		CdBankOld obj = cdBankOld.isPresent() ? cdBankOld.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdBankOld> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBankOld> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "BankCode", "BranchCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BankCode", "BranchCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdBankOldReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBankOldReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBankOldReposHist.findAll(pageable);
		else
			slice = cdBankOldRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdBankOld> bankCodeLike(String bankCode_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBankOld> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("bankCodeLike " + dbName + " : " + "bankCode_0 : " + bankCode_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdBankOldReposDay.findAllByBankCodeLikeOrderByBankCodeAsc(bankCode_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBankOldReposMon.findAllByBankCodeLikeOrderByBankCodeAsc(bankCode_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBankOldReposHist.findAllByBankCodeLikeOrderByBankCodeAsc(bankCode_0, pageable);
		else
			slice = cdBankOldRepos.findAllByBankCodeLikeOrderByBankCodeAsc(bankCode_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdBankOld> branchCodeLike(String bankCode_0, String branchCode_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBankOld> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("branchCodeLike " + dbName + " : " + "bankCode_0 : " + bankCode_0 + " branchCode_1 : " + branchCode_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdBankOldReposDay.findAllByBankCodeLikeAndBranchCodeLikeOrderByBankCodeAscBranchCodeAsc(bankCode_0, branchCode_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBankOldReposMon.findAllByBankCodeLikeAndBranchCodeLikeOrderByBankCodeAscBranchCodeAsc(bankCode_0, branchCode_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBankOldReposHist.findAllByBankCodeLikeAndBranchCodeLikeOrderByBankCodeAscBranchCodeAsc(bankCode_0, branchCode_1, pageable);
		else
			slice = cdBankOldRepos.findAllByBankCodeLikeAndBranchCodeLikeOrderByBankCodeAscBranchCodeAsc(bankCode_0, branchCode_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdBankOld> bankItemLike(String bankItem_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdBankOld> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("bankItemLike " + dbName + " : " + "bankItem_0 : " + bankItem_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdBankOldReposDay.findAllByBankItemLikeOrderByBankCodeAsc(bankItem_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdBankOldReposMon.findAllByBankItemLikeOrderByBankCodeAsc(bankItem_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdBankOldReposHist.findAllByBankItemLikeOrderByBankCodeAsc(bankItem_0, pageable);
		else
			slice = cdBankOldRepos.findAllByBankItemLikeOrderByBankCodeAsc(bankItem_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdBankOld holdById(CdBankOldId cdBankOldId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdBankOldId);
		Optional<CdBankOld> cdBankOld = null;
		if (dbName.equals(ContentName.onDay))
			cdBankOld = cdBankOldReposDay.findByCdBankOldId(cdBankOldId);
		else if (dbName.equals(ContentName.onMon))
			cdBankOld = cdBankOldReposMon.findByCdBankOldId(cdBankOldId);
		else if (dbName.equals(ContentName.onHist))
			cdBankOld = cdBankOldReposHist.findByCdBankOldId(cdBankOldId);
		else
			cdBankOld = cdBankOldRepos.findByCdBankOldId(cdBankOldId);
		return cdBankOld.isPresent() ? cdBankOld.get() : null;
	}

	@Override
	public CdBankOld holdById(CdBankOld cdBankOld, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdBankOld.getCdBankOldId());
		Optional<CdBankOld> cdBankOldT = null;
		if (dbName.equals(ContentName.onDay))
			cdBankOldT = cdBankOldReposDay.findByCdBankOldId(cdBankOld.getCdBankOldId());
		else if (dbName.equals(ContentName.onMon))
			cdBankOldT = cdBankOldReposMon.findByCdBankOldId(cdBankOld.getCdBankOldId());
		else if (dbName.equals(ContentName.onHist))
			cdBankOldT = cdBankOldReposHist.findByCdBankOldId(cdBankOld.getCdBankOldId());
		else
			cdBankOldT = cdBankOldRepos.findByCdBankOldId(cdBankOld.getCdBankOldId());
		return cdBankOldT.isPresent() ? cdBankOldT.get() : null;
	}

	@Override
	public CdBankOld insert(CdBankOld cdBankOld, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdBankOld.getCdBankOldId());
		if (this.findById(cdBankOld.getCdBankOldId(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdBankOld.setCreateEmpNo(empNot);

		if (cdBankOld.getLastUpdateEmpNo() == null || cdBankOld.getLastUpdateEmpNo().isEmpty())
			cdBankOld.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdBankOldReposDay.saveAndFlush(cdBankOld);
		else if (dbName.equals(ContentName.onMon))
			return cdBankOldReposMon.saveAndFlush(cdBankOld);
		else if (dbName.equals(ContentName.onHist))
			return cdBankOldReposHist.saveAndFlush(cdBankOld);
		else
			return cdBankOldRepos.saveAndFlush(cdBankOld);
	}

	@Override
	public CdBankOld update(CdBankOld cdBankOld, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdBankOld.getCdBankOldId());
		if (!empNot.isEmpty())
			cdBankOld.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdBankOldReposDay.saveAndFlush(cdBankOld);
		else if (dbName.equals(ContentName.onMon))
			return cdBankOldReposMon.saveAndFlush(cdBankOld);
		else if (dbName.equals(ContentName.onHist))
			return cdBankOldReposHist.saveAndFlush(cdBankOld);
		else
			return cdBankOldRepos.saveAndFlush(cdBankOld);
	}

	@Override
	public CdBankOld update2(CdBankOld cdBankOld, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdBankOld.getCdBankOldId());
		if (!empNot.isEmpty())
			cdBankOld.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdBankOldReposDay.saveAndFlush(cdBankOld);
		else if (dbName.equals(ContentName.onMon))
			cdBankOldReposMon.saveAndFlush(cdBankOld);
		else if (dbName.equals(ContentName.onHist))
			cdBankOldReposHist.saveAndFlush(cdBankOld);
		else
			cdBankOldRepos.saveAndFlush(cdBankOld);
		return this.findById(cdBankOld.getCdBankOldId());
	}

	@Override
	public void delete(CdBankOld cdBankOld, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdBankOld.getCdBankOldId());
		if (dbName.equals(ContentName.onDay)) {
			cdBankOldReposDay.delete(cdBankOld);
			cdBankOldReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBankOldReposMon.delete(cdBankOld);
			cdBankOldReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBankOldReposHist.delete(cdBankOld);
			cdBankOldReposHist.flush();
		} else {
			cdBankOldRepos.delete(cdBankOld);
			cdBankOldRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdBankOld> cdBankOld, TitaVo... titaVo) throws DBException {
		if (cdBankOld == null || cdBankOld.size() == 0)
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
		for (CdBankOld t : cdBankOld) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdBankOld = cdBankOldReposDay.saveAll(cdBankOld);
			cdBankOldReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBankOld = cdBankOldReposMon.saveAll(cdBankOld);
			cdBankOldReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBankOld = cdBankOldReposHist.saveAll(cdBankOld);
			cdBankOldReposHist.flush();
		} else {
			cdBankOld = cdBankOldRepos.saveAll(cdBankOld);
			cdBankOldRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdBankOld> cdBankOld, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdBankOld == null || cdBankOld.size() == 0)
			throw new DBException(6);

		for (CdBankOld t : cdBankOld)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdBankOld = cdBankOldReposDay.saveAll(cdBankOld);
			cdBankOldReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBankOld = cdBankOldReposMon.saveAll(cdBankOld);
			cdBankOldReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBankOld = cdBankOldReposHist.saveAll(cdBankOld);
			cdBankOldReposHist.flush();
		} else {
			cdBankOld = cdBankOldRepos.saveAll(cdBankOld);
			cdBankOldRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdBankOld> cdBankOld, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdBankOld == null || cdBankOld.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdBankOldReposDay.deleteAll(cdBankOld);
			cdBankOldReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdBankOldReposMon.deleteAll(cdBankOld);
			cdBankOldReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdBankOldReposHist.deleteAll(cdBankOld);
			cdBankOldReposHist.flush();
		} else {
			cdBankOldRepos.deleteAll(cdBankOld);
			cdBankOldRepos.flush();
		}
	}

}
