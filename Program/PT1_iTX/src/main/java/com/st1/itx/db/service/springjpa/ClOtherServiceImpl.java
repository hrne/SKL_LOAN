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
import com.st1.itx.db.domain.ClOther;
import com.st1.itx.db.domain.ClOtherId;
import com.st1.itx.db.repository.online.ClOtherRepository;
import com.st1.itx.db.repository.day.ClOtherRepositoryDay;
import com.st1.itx.db.repository.mon.ClOtherRepositoryMon;
import com.st1.itx.db.repository.hist.ClOtherRepositoryHist;
import com.st1.itx.db.service.ClOtherService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clOtherService")
@Repository
public class ClOtherServiceImpl extends ASpringJpaParm implements ClOtherService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ClOtherRepository clOtherRepos;

	@Autowired
	private ClOtherRepositoryDay clOtherReposDay;

	@Autowired
	private ClOtherRepositoryMon clOtherReposMon;

	@Autowired
	private ClOtherRepositoryHist clOtherReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(clOtherRepos);
		org.junit.Assert.assertNotNull(clOtherReposDay);
		org.junit.Assert.assertNotNull(clOtherReposMon);
		org.junit.Assert.assertNotNull(clOtherReposHist);
	}

	@Override
	public ClOther findById(ClOtherId clOtherId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + clOtherId);
		Optional<ClOther> clOther = null;
		if (dbName.equals(ContentName.onDay))
			clOther = clOtherReposDay.findById(clOtherId);
		else if (dbName.equals(ContentName.onMon))
			clOther = clOtherReposMon.findById(clOtherId);
		else if (dbName.equals(ContentName.onHist))
			clOther = clOtherReposHist.findById(clOtherId);
		else
			clOther = clOtherRepos.findById(clOtherId);
		ClOther obj = clOther.isPresent() ? clOther.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ClOther> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClOther> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = clOtherReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clOtherReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clOtherReposHist.findAll(pageable);
		else
			slice = clOtherRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClOther> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClOther> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClCode1 " + dbName + " : " + "clCode1_0 : " + clCode1_0);
		if (dbName.equals(ContentName.onDay))
			slice = clOtherReposDay.findAllByClCode1Is(clCode1_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clOtherReposMon.findAllByClCode1Is(clCode1_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clOtherReposHist.findAllByClCode1Is(clCode1_0, pageable);
		else
			slice = clOtherRepos.findAllByClCode1Is(clCode1_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClOther> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClOther> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClCode2 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1);
		if (dbName.equals(ContentName.onDay))
			slice = clOtherReposDay.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clOtherReposMon.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clOtherReposHist.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else
			slice = clOtherRepos.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClOther> findUnique(String issuingId_0, String docNo_1, String ownerCustUKey_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClOther> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findUnique " + dbName + " : " + "issuingId_0 : " + issuingId_0 + " docNo_1 : " + docNo_1 + " ownerCustUKey_2 : " + ownerCustUKey_2);
		if (dbName.equals(ContentName.onDay))
			slice = clOtherReposDay.findAllByIssuingIdIsAndDocNoIsAndOwnerCustUKeyIs(issuingId_0, docNo_1, ownerCustUKey_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clOtherReposMon.findAllByIssuingIdIsAndDocNoIsAndOwnerCustUKeyIs(issuingId_0, docNo_1, ownerCustUKey_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clOtherReposHist.findAllByIssuingIdIsAndDocNoIsAndOwnerCustUKeyIs(issuingId_0, docNo_1, ownerCustUKey_2, pageable);
		else
			slice = clOtherRepos.findAllByIssuingIdIsAndDocNoIsAndOwnerCustUKeyIs(issuingId_0, docNo_1, ownerCustUKey_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ClOther holdById(ClOtherId clOtherId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clOtherId);
		Optional<ClOther> clOther = null;
		if (dbName.equals(ContentName.onDay))
			clOther = clOtherReposDay.findByClOtherId(clOtherId);
		else if (dbName.equals(ContentName.onMon))
			clOther = clOtherReposMon.findByClOtherId(clOtherId);
		else if (dbName.equals(ContentName.onHist))
			clOther = clOtherReposHist.findByClOtherId(clOtherId);
		else
			clOther = clOtherRepos.findByClOtherId(clOtherId);
		return clOther.isPresent() ? clOther.get() : null;
	}

	@Override
	public ClOther holdById(ClOther clOther, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clOther.getClOtherId());
		Optional<ClOther> clOtherT = null;
		if (dbName.equals(ContentName.onDay))
			clOtherT = clOtherReposDay.findByClOtherId(clOther.getClOtherId());
		else if (dbName.equals(ContentName.onMon))
			clOtherT = clOtherReposMon.findByClOtherId(clOther.getClOtherId());
		else if (dbName.equals(ContentName.onHist))
			clOtherT = clOtherReposHist.findByClOtherId(clOther.getClOtherId());
		else
			clOtherT = clOtherRepos.findByClOtherId(clOther.getClOtherId());
		return clOtherT.isPresent() ? clOtherT.get() : null;
	}

	@Override
	public ClOther insert(ClOther clOther, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + clOther.getClOtherId());
		if (this.findById(clOther.getClOtherId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			clOther.setCreateEmpNo(empNot);

		if (clOther.getLastUpdateEmpNo() == null || clOther.getLastUpdateEmpNo().isEmpty())
			clOther.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clOtherReposDay.saveAndFlush(clOther);
		else if (dbName.equals(ContentName.onMon))
			return clOtherReposMon.saveAndFlush(clOther);
		else if (dbName.equals(ContentName.onHist))
			return clOtherReposHist.saveAndFlush(clOther);
		else
			return clOtherRepos.saveAndFlush(clOther);
	}

	@Override
	public ClOther update(ClOther clOther, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clOther.getClOtherId());
		if (!empNot.isEmpty())
			clOther.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clOtherReposDay.saveAndFlush(clOther);
		else if (dbName.equals(ContentName.onMon))
			return clOtherReposMon.saveAndFlush(clOther);
		else if (dbName.equals(ContentName.onHist))
			return clOtherReposHist.saveAndFlush(clOther);
		else
			return clOtherRepos.saveAndFlush(clOther);
	}

	@Override
	public ClOther update2(ClOther clOther, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clOther.getClOtherId());
		if (!empNot.isEmpty())
			clOther.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			clOtherReposDay.saveAndFlush(clOther);
		else if (dbName.equals(ContentName.onMon))
			clOtherReposMon.saveAndFlush(clOther);
		else if (dbName.equals(ContentName.onHist))
			clOtherReposHist.saveAndFlush(clOther);
		else
			clOtherRepos.saveAndFlush(clOther);
		return this.findById(clOther.getClOtherId());
	}

	@Override
	public void delete(ClOther clOther, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + clOther.getClOtherId());
		if (dbName.equals(ContentName.onDay)) {
			clOtherReposDay.delete(clOther);
			clOtherReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clOtherReposMon.delete(clOther);
			clOtherReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clOtherReposHist.delete(clOther);
			clOtherReposHist.flush();
		} else {
			clOtherRepos.delete(clOther);
			clOtherRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ClOther> clOther, TitaVo... titaVo) throws DBException {
		if (clOther == null || clOther.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (ClOther t : clOther) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			clOther = clOtherReposDay.saveAll(clOther);
			clOtherReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clOther = clOtherReposMon.saveAll(clOther);
			clOtherReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clOther = clOtherReposHist.saveAll(clOther);
			clOtherReposHist.flush();
		} else {
			clOther = clOtherRepos.saveAll(clOther);
			clOtherRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ClOther> clOther, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (clOther == null || clOther.size() == 0)
			throw new DBException(6);

		for (ClOther t : clOther)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			clOther = clOtherReposDay.saveAll(clOther);
			clOtherReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clOther = clOtherReposMon.saveAll(clOther);
			clOtherReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clOther = clOtherReposHist.saveAll(clOther);
			clOtherReposHist.flush();
		} else {
			clOther = clOtherRepos.saveAll(clOther);
			clOtherRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ClOther> clOther, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (clOther == null || clOther.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			clOtherReposDay.deleteAll(clOther);
			clOtherReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clOtherReposMon.deleteAll(clOther);
			clOtherReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clOtherReposHist.deleteAll(clOther);
			clOtherReposHist.flush();
		} else {
			clOtherRepos.deleteAll(clOther);
			clOtherRepos.flush();
		}
	}

}
