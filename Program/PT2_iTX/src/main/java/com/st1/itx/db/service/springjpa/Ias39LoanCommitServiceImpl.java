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
import com.st1.itx.db.domain.Ias39LoanCommit;
import com.st1.itx.db.domain.Ias39LoanCommitId;
import com.st1.itx.db.repository.online.Ias39LoanCommitRepository;
import com.st1.itx.db.repository.day.Ias39LoanCommitRepositoryDay;
import com.st1.itx.db.repository.mon.Ias39LoanCommitRepositoryMon;
import com.st1.itx.db.repository.hist.Ias39LoanCommitRepositoryHist;
import com.st1.itx.db.service.Ias39LoanCommitService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("ias39LoanCommitService")
@Repository
public class Ias39LoanCommitServiceImpl extends ASpringJpaParm implements Ias39LoanCommitService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Ias39LoanCommitRepository ias39LoanCommitRepos;

	@Autowired
	private Ias39LoanCommitRepositoryDay ias39LoanCommitReposDay;

	@Autowired
	private Ias39LoanCommitRepositoryMon ias39LoanCommitReposMon;

	@Autowired
	private Ias39LoanCommitRepositoryHist ias39LoanCommitReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(ias39LoanCommitRepos);
		org.junit.Assert.assertNotNull(ias39LoanCommitReposDay);
		org.junit.Assert.assertNotNull(ias39LoanCommitReposMon);
		org.junit.Assert.assertNotNull(ias39LoanCommitReposHist);
	}

	@Override
	public Ias39LoanCommit findById(Ias39LoanCommitId ias39LoanCommitId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + ias39LoanCommitId);
		Optional<Ias39LoanCommit> ias39LoanCommit = null;
		if (dbName.equals(ContentName.onDay))
			ias39LoanCommit = ias39LoanCommitReposDay.findById(ias39LoanCommitId);
		else if (dbName.equals(ContentName.onMon))
			ias39LoanCommit = ias39LoanCommitReposMon.findById(ias39LoanCommitId);
		else if (dbName.equals(ContentName.onHist))
			ias39LoanCommit = ias39LoanCommitReposHist.findById(ias39LoanCommitId);
		else
			ias39LoanCommit = ias39LoanCommitRepos.findById(ias39LoanCommitId);
		Ias39LoanCommit obj = ias39LoanCommit.isPresent() ? ias39LoanCommit.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<Ias39LoanCommit> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias39LoanCommit> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYm", "CustNo", "FacmNo", "ApplNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYm", "CustNo", "FacmNo", "ApplNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = ias39LoanCommitReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias39LoanCommitReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias39LoanCommitReposHist.findAll(pageable);
		else
			slice = ias39LoanCommitRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<Ias39LoanCommit> ApplNoEq(int dataYm_0, int custNo_1, int facmNo_2, int applNo_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias39LoanCommit> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ApplNoEq " + dbName + " : " + "dataYm_0 : " + dataYm_0 + " custNo_1 : " + custNo_1 + " facmNo_2 : " + facmNo_2 + " applNo_3 : " + applNo_3);
		if (dbName.equals(ContentName.onDay))
			slice = ias39LoanCommitReposDay.findAllByDataYmIsAndCustNoIsAndFacmNoIsAndApplNoIs(dataYm_0, custNo_1, facmNo_2, applNo_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias39LoanCommitReposMon.findAllByDataYmIsAndCustNoIsAndFacmNoIsAndApplNoIs(dataYm_0, custNo_1, facmNo_2, applNo_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias39LoanCommitReposHist.findAllByDataYmIsAndCustNoIsAndFacmNoIsAndApplNoIs(dataYm_0, custNo_1, facmNo_2, applNo_3, pageable);
		else
			slice = ias39LoanCommitRepos.findAllByDataYmIsAndCustNoIsAndFacmNoIsAndApplNoIs(dataYm_0, custNo_1, facmNo_2, applNo_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<Ias39LoanCommit> findDataYmEq(int dataYm_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<Ias39LoanCommit> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findDataYmEq " + dbName + " : " + "dataYm_0 : " + dataYm_0);
		if (dbName.equals(ContentName.onDay))
			slice = ias39LoanCommitReposDay.findAllByDataYmIsOrderByAcBookCodeAsc(dataYm_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = ias39LoanCommitReposMon.findAllByDataYmIsOrderByAcBookCodeAsc(dataYm_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = ias39LoanCommitReposHist.findAllByDataYmIsOrderByAcBookCodeAsc(dataYm_0, pageable);
		else
			slice = ias39LoanCommitRepos.findAllByDataYmIsOrderByAcBookCodeAsc(dataYm_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Ias39LoanCommit holdById(Ias39LoanCommitId ias39LoanCommitId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias39LoanCommitId);
		Optional<Ias39LoanCommit> ias39LoanCommit = null;
		if (dbName.equals(ContentName.onDay))
			ias39LoanCommit = ias39LoanCommitReposDay.findByIas39LoanCommitId(ias39LoanCommitId);
		else if (dbName.equals(ContentName.onMon))
			ias39LoanCommit = ias39LoanCommitReposMon.findByIas39LoanCommitId(ias39LoanCommitId);
		else if (dbName.equals(ContentName.onHist))
			ias39LoanCommit = ias39LoanCommitReposHist.findByIas39LoanCommitId(ias39LoanCommitId);
		else
			ias39LoanCommit = ias39LoanCommitRepos.findByIas39LoanCommitId(ias39LoanCommitId);
		return ias39LoanCommit.isPresent() ? ias39LoanCommit.get() : null;
	}

	@Override
	public Ias39LoanCommit holdById(Ias39LoanCommit ias39LoanCommit, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + ias39LoanCommit.getIas39LoanCommitId());
		Optional<Ias39LoanCommit> ias39LoanCommitT = null;
		if (dbName.equals(ContentName.onDay))
			ias39LoanCommitT = ias39LoanCommitReposDay.findByIas39LoanCommitId(ias39LoanCommit.getIas39LoanCommitId());
		else if (dbName.equals(ContentName.onMon))
			ias39LoanCommitT = ias39LoanCommitReposMon.findByIas39LoanCommitId(ias39LoanCommit.getIas39LoanCommitId());
		else if (dbName.equals(ContentName.onHist))
			ias39LoanCommitT = ias39LoanCommitReposHist.findByIas39LoanCommitId(ias39LoanCommit.getIas39LoanCommitId());
		else
			ias39LoanCommitT = ias39LoanCommitRepos.findByIas39LoanCommitId(ias39LoanCommit.getIas39LoanCommitId());
		return ias39LoanCommitT.isPresent() ? ias39LoanCommitT.get() : null;
	}

	@Override
	public Ias39LoanCommit insert(Ias39LoanCommit ias39LoanCommit, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + ias39LoanCommit.getIas39LoanCommitId());
		if (this.findById(ias39LoanCommit.getIas39LoanCommitId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			ias39LoanCommit.setCreateEmpNo(empNot);

		if (ias39LoanCommit.getLastUpdateEmpNo() == null || ias39LoanCommit.getLastUpdateEmpNo().isEmpty())
			ias39LoanCommit.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias39LoanCommitReposDay.saveAndFlush(ias39LoanCommit);
		else if (dbName.equals(ContentName.onMon))
			return ias39LoanCommitReposMon.saveAndFlush(ias39LoanCommit);
		else if (dbName.equals(ContentName.onHist))
			return ias39LoanCommitReposHist.saveAndFlush(ias39LoanCommit);
		else
			return ias39LoanCommitRepos.saveAndFlush(ias39LoanCommit);
	}

	@Override
	public Ias39LoanCommit update(Ias39LoanCommit ias39LoanCommit, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias39LoanCommit.getIas39LoanCommitId());
		if (!empNot.isEmpty())
			ias39LoanCommit.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return ias39LoanCommitReposDay.saveAndFlush(ias39LoanCommit);
		else if (dbName.equals(ContentName.onMon))
			return ias39LoanCommitReposMon.saveAndFlush(ias39LoanCommit);
		else if (dbName.equals(ContentName.onHist))
			return ias39LoanCommitReposHist.saveAndFlush(ias39LoanCommit);
		else
			return ias39LoanCommitRepos.saveAndFlush(ias39LoanCommit);
	}

	@Override
	public Ias39LoanCommit update2(Ias39LoanCommit ias39LoanCommit, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + ias39LoanCommit.getIas39LoanCommitId());
		if (!empNot.isEmpty())
			ias39LoanCommit.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			ias39LoanCommitReposDay.saveAndFlush(ias39LoanCommit);
		else if (dbName.equals(ContentName.onMon))
			ias39LoanCommitReposMon.saveAndFlush(ias39LoanCommit);
		else if (dbName.equals(ContentName.onHist))
			ias39LoanCommitReposHist.saveAndFlush(ias39LoanCommit);
		else
			ias39LoanCommitRepos.saveAndFlush(ias39LoanCommit);
		return this.findById(ias39LoanCommit.getIas39LoanCommitId());
	}

	@Override
	public void delete(Ias39LoanCommit ias39LoanCommit, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + ias39LoanCommit.getIas39LoanCommitId());
		if (dbName.equals(ContentName.onDay)) {
			ias39LoanCommitReposDay.delete(ias39LoanCommit);
			ias39LoanCommitReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39LoanCommitReposMon.delete(ias39LoanCommit);
			ias39LoanCommitReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39LoanCommitReposHist.delete(ias39LoanCommit);
			ias39LoanCommitReposHist.flush();
		} else {
			ias39LoanCommitRepos.delete(ias39LoanCommit);
			ias39LoanCommitRepos.flush();
		}
	}

	@Override
	public void insertAll(List<Ias39LoanCommit> ias39LoanCommit, TitaVo... titaVo) throws DBException {
		if (ias39LoanCommit == null || ias39LoanCommit.size() == 0)
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
		for (Ias39LoanCommit t : ias39LoanCommit) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			ias39LoanCommit = ias39LoanCommitReposDay.saveAll(ias39LoanCommit);
			ias39LoanCommitReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39LoanCommit = ias39LoanCommitReposMon.saveAll(ias39LoanCommit);
			ias39LoanCommitReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39LoanCommit = ias39LoanCommitReposHist.saveAll(ias39LoanCommit);
			ias39LoanCommitReposHist.flush();
		} else {
			ias39LoanCommit = ias39LoanCommitRepos.saveAll(ias39LoanCommit);
			ias39LoanCommitRepos.flush();
		}
	}

	@Override
	public void updateAll(List<Ias39LoanCommit> ias39LoanCommit, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (ias39LoanCommit == null || ias39LoanCommit.size() == 0)
			throw new DBException(6);

		for (Ias39LoanCommit t : ias39LoanCommit)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			ias39LoanCommit = ias39LoanCommitReposDay.saveAll(ias39LoanCommit);
			ias39LoanCommitReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39LoanCommit = ias39LoanCommitReposMon.saveAll(ias39LoanCommit);
			ias39LoanCommitReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39LoanCommit = ias39LoanCommitReposHist.saveAll(ias39LoanCommit);
			ias39LoanCommitReposHist.flush();
		} else {
			ias39LoanCommit = ias39LoanCommitRepos.saveAll(ias39LoanCommit);
			ias39LoanCommitRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<Ias39LoanCommit> ias39LoanCommit, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (ias39LoanCommit == null || ias39LoanCommit.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			ias39LoanCommitReposDay.deleteAll(ias39LoanCommit);
			ias39LoanCommitReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			ias39LoanCommitReposMon.deleteAll(ias39LoanCommit);
			ias39LoanCommitReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			ias39LoanCommitReposHist.deleteAll(ias39LoanCommit);
			ias39LoanCommitReposHist.flush();
		} else {
			ias39LoanCommitRepos.deleteAll(ias39LoanCommit);
			ias39LoanCommitRepos.flush();
		}
	}

	@Override
	public void Usp_L7_Ias39LoanCommit_Upd(int tbsdyf, String empNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			ias39LoanCommitReposDay.uspL7Ias39loancommitUpd(tbsdyf, empNo);
		else if (dbName.equals(ContentName.onMon))
			ias39LoanCommitReposMon.uspL7Ias39loancommitUpd(tbsdyf, empNo);
		else if (dbName.equals(ContentName.onHist))
			ias39LoanCommitReposHist.uspL7Ias39loancommitUpd(tbsdyf, empNo);
		else
			ias39LoanCommitRepos.uspL7Ias39loancommitUpd(tbsdyf, empNo);
	}

}
