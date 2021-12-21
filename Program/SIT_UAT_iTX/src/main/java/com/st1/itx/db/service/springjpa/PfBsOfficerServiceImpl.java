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
import com.st1.itx.db.domain.PfBsOfficer;
import com.st1.itx.db.domain.PfBsOfficerId;
import com.st1.itx.db.repository.online.PfBsOfficerRepository;
import com.st1.itx.db.repository.day.PfBsOfficerRepositoryDay;
import com.st1.itx.db.repository.mon.PfBsOfficerRepositoryMon;
import com.st1.itx.db.repository.hist.PfBsOfficerRepositoryHist;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfBsOfficerService")
@Repository
public class PfBsOfficerServiceImpl extends ASpringJpaParm implements PfBsOfficerService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private PfBsOfficerRepository pfBsOfficerRepos;

	@Autowired
	private PfBsOfficerRepositoryDay pfBsOfficerReposDay;

	@Autowired
	private PfBsOfficerRepositoryMon pfBsOfficerReposMon;

	@Autowired
	private PfBsOfficerRepositoryHist pfBsOfficerReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(pfBsOfficerRepos);
		org.junit.Assert.assertNotNull(pfBsOfficerReposDay);
		org.junit.Assert.assertNotNull(pfBsOfficerReposMon);
		org.junit.Assert.assertNotNull(pfBsOfficerReposHist);
	}

	@Override
	public PfBsOfficer findById(PfBsOfficerId pfBsOfficerId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + pfBsOfficerId);
		Optional<PfBsOfficer> pfBsOfficer = null;
		if (dbName.equals(ContentName.onDay))
			pfBsOfficer = pfBsOfficerReposDay.findById(pfBsOfficerId);
		else if (dbName.equals(ContentName.onMon))
			pfBsOfficer = pfBsOfficerReposMon.findById(pfBsOfficerId);
		else if (dbName.equals(ContentName.onHist))
			pfBsOfficer = pfBsOfficerReposHist.findById(pfBsOfficerId);
		else
			pfBsOfficer = pfBsOfficerRepos.findById(pfBsOfficerId);
		PfBsOfficer obj = pfBsOfficer.isPresent() ? pfBsOfficer.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<PfBsOfficer> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfBsOfficer> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "WorkMonth", "EmpNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "WorkMonth", "EmpNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = pfBsOfficerReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfBsOfficerReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfBsOfficerReposHist.findAll(pageable);
		else
			slice = pfBsOfficerRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfBsOfficer> findByMonth(int workMonth_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfBsOfficer> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByMonth " + dbName + " : " + "workMonth_0 : " + workMonth_0);
		if (dbName.equals(ContentName.onDay))
			slice = pfBsOfficerReposDay.findAllByWorkMonthIs(workMonth_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfBsOfficerReposMon.findAllByWorkMonthIs(workMonth_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfBsOfficerReposHist.findAllByWorkMonthIs(workMonth_0, pageable);
		else
			slice = pfBsOfficerRepos.findAllByWorkMonthIs(workMonth_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfBsOfficer> findBetween(int workMonth_0, int workMonth_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfBsOfficer> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findBetween " + dbName + " : " + "workMonth_0 : " + workMonth_0 + " workMonth_1 : " + workMonth_1);
		if (dbName.equals(ContentName.onDay))
			slice = pfBsOfficerReposDay.findAllByWorkMonthLessThanEqualAndWorkMonthGreaterThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfBsOfficerReposMon.findAllByWorkMonthLessThanEqualAndWorkMonthGreaterThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfBsOfficerReposHist.findAllByWorkMonthLessThanEqualAndWorkMonthGreaterThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);
		else
			slice = pfBsOfficerRepos.findAllByWorkMonthLessThanEqualAndWorkMonthGreaterThanEqualOrderByWorkMonthAsc(workMonth_0, workMonth_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfBsOfficer> findByEmpNoAndMonth(String empNo_0, int workMonth_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfBsOfficer> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByEmpNoAndMonth " + dbName + " : " + "empNo_0 : " + empNo_0 + " workMonth_1 : " + workMonth_1);
		if (dbName.equals(ContentName.onDay))
			slice = pfBsOfficerReposDay.findAllByEmpNoIsAndWorkMonthIs(empNo_0, workMonth_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfBsOfficerReposMon.findAllByEmpNoIsAndWorkMonthIs(empNo_0, workMonth_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfBsOfficerReposHist.findAllByEmpNoIsAndWorkMonthIs(empNo_0, workMonth_1, pageable);
		else
			slice = pfBsOfficerRepos.findAllByEmpNoIsAndWorkMonthIs(empNo_0, workMonth_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfBsOfficer> findByEmpNo(String empNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfBsOfficer> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByEmpNo " + dbName + " : " + "empNo_0 : " + empNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = pfBsOfficerReposDay.findAllByEmpNoIs(empNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfBsOfficerReposMon.findAllByEmpNoIs(empNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfBsOfficerReposHist.findAllByEmpNoIs(empNo_0, pageable);
		else
			slice = pfBsOfficerRepos.findAllByEmpNoIs(empNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<PfBsOfficer> findByEmpNoAndRange(String empNo_0, int workMonth_1, int workMonth_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<PfBsOfficer> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findByEmpNoAndRange " + dbName + " : " + "empNo_0 : " + empNo_0 + " workMonth_1 : " + workMonth_1 + " workMonth_2 : " + workMonth_2);
		if (dbName.equals(ContentName.onDay))
			slice = pfBsOfficerReposDay.findAllByEmpNoIsAndWorkMonthLessThanEqualAndWorkMonthGreaterThanEqualOrderByWorkMonthAsc(empNo_0, workMonth_1, workMonth_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = pfBsOfficerReposMon.findAllByEmpNoIsAndWorkMonthLessThanEqualAndWorkMonthGreaterThanEqualOrderByWorkMonthAsc(empNo_0, workMonth_1, workMonth_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = pfBsOfficerReposHist.findAllByEmpNoIsAndWorkMonthLessThanEqualAndWorkMonthGreaterThanEqualOrderByWorkMonthAsc(empNo_0, workMonth_1, workMonth_2, pageable);
		else
			slice = pfBsOfficerRepos.findAllByEmpNoIsAndWorkMonthLessThanEqualAndWorkMonthGreaterThanEqualOrderByWorkMonthAsc(empNo_0, workMonth_1, workMonth_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public PfBsOfficer holdById(PfBsOfficerId pfBsOfficerId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + pfBsOfficerId);
		Optional<PfBsOfficer> pfBsOfficer = null;
		if (dbName.equals(ContentName.onDay))
			pfBsOfficer = pfBsOfficerReposDay.findByPfBsOfficerId(pfBsOfficerId);
		else if (dbName.equals(ContentName.onMon))
			pfBsOfficer = pfBsOfficerReposMon.findByPfBsOfficerId(pfBsOfficerId);
		else if (dbName.equals(ContentName.onHist))
			pfBsOfficer = pfBsOfficerReposHist.findByPfBsOfficerId(pfBsOfficerId);
		else
			pfBsOfficer = pfBsOfficerRepos.findByPfBsOfficerId(pfBsOfficerId);
		return pfBsOfficer.isPresent() ? pfBsOfficer.get() : null;
	}

	@Override
	public PfBsOfficer holdById(PfBsOfficer pfBsOfficer, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + pfBsOfficer.getPfBsOfficerId());
		Optional<PfBsOfficer> pfBsOfficerT = null;
		if (dbName.equals(ContentName.onDay))
			pfBsOfficerT = pfBsOfficerReposDay.findByPfBsOfficerId(pfBsOfficer.getPfBsOfficerId());
		else if (dbName.equals(ContentName.onMon))
			pfBsOfficerT = pfBsOfficerReposMon.findByPfBsOfficerId(pfBsOfficer.getPfBsOfficerId());
		else if (dbName.equals(ContentName.onHist))
			pfBsOfficerT = pfBsOfficerReposHist.findByPfBsOfficerId(pfBsOfficer.getPfBsOfficerId());
		else
			pfBsOfficerT = pfBsOfficerRepos.findByPfBsOfficerId(pfBsOfficer.getPfBsOfficerId());
		return pfBsOfficerT.isPresent() ? pfBsOfficerT.get() : null;
	}

	@Override
	public PfBsOfficer insert(PfBsOfficer pfBsOfficer, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + pfBsOfficer.getPfBsOfficerId());
		if (this.findById(pfBsOfficer.getPfBsOfficerId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			pfBsOfficer.setCreateEmpNo(empNot);

		if (pfBsOfficer.getLastUpdateEmpNo() == null || pfBsOfficer.getLastUpdateEmpNo().isEmpty())
			pfBsOfficer.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return pfBsOfficerReposDay.saveAndFlush(pfBsOfficer);
		else if (dbName.equals(ContentName.onMon))
			return pfBsOfficerReposMon.saveAndFlush(pfBsOfficer);
		else if (dbName.equals(ContentName.onHist))
			return pfBsOfficerReposHist.saveAndFlush(pfBsOfficer);
		else
			return pfBsOfficerRepos.saveAndFlush(pfBsOfficer);
	}

	@Override
	public PfBsOfficer update(PfBsOfficer pfBsOfficer, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + pfBsOfficer.getPfBsOfficerId());
		if (!empNot.isEmpty())
			pfBsOfficer.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return pfBsOfficerReposDay.saveAndFlush(pfBsOfficer);
		else if (dbName.equals(ContentName.onMon))
			return pfBsOfficerReposMon.saveAndFlush(pfBsOfficer);
		else if (dbName.equals(ContentName.onHist))
			return pfBsOfficerReposHist.saveAndFlush(pfBsOfficer);
		else
			return pfBsOfficerRepos.saveAndFlush(pfBsOfficer);
	}

	@Override
	public PfBsOfficer update2(PfBsOfficer pfBsOfficer, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + pfBsOfficer.getPfBsOfficerId());
		if (!empNot.isEmpty())
			pfBsOfficer.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			pfBsOfficerReposDay.saveAndFlush(pfBsOfficer);
		else if (dbName.equals(ContentName.onMon))
			pfBsOfficerReposMon.saveAndFlush(pfBsOfficer);
		else if (dbName.equals(ContentName.onHist))
			pfBsOfficerReposHist.saveAndFlush(pfBsOfficer);
		else
			pfBsOfficerRepos.saveAndFlush(pfBsOfficer);
		return this.findById(pfBsOfficer.getPfBsOfficerId());
	}

	@Override
	public void delete(PfBsOfficer pfBsOfficer, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + pfBsOfficer.getPfBsOfficerId());
		if (dbName.equals(ContentName.onDay)) {
			pfBsOfficerReposDay.delete(pfBsOfficer);
			pfBsOfficerReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfBsOfficerReposMon.delete(pfBsOfficer);
			pfBsOfficerReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfBsOfficerReposHist.delete(pfBsOfficer);
			pfBsOfficerReposHist.flush();
		} else {
			pfBsOfficerRepos.delete(pfBsOfficer);
			pfBsOfficerRepos.flush();
		}
	}

	@Override
	public void insertAll(List<PfBsOfficer> pfBsOfficer, TitaVo... titaVo) throws DBException {
		if (pfBsOfficer == null || pfBsOfficer.size() == 0)
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
		for (PfBsOfficer t : pfBsOfficer) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			pfBsOfficer = pfBsOfficerReposDay.saveAll(pfBsOfficer);
			pfBsOfficerReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfBsOfficer = pfBsOfficerReposMon.saveAll(pfBsOfficer);
			pfBsOfficerReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfBsOfficer = pfBsOfficerReposHist.saveAll(pfBsOfficer);
			pfBsOfficerReposHist.flush();
		} else {
			pfBsOfficer = pfBsOfficerRepos.saveAll(pfBsOfficer);
			pfBsOfficerRepos.flush();
		}
	}

	@Override
	public void updateAll(List<PfBsOfficer> pfBsOfficer, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (pfBsOfficer == null || pfBsOfficer.size() == 0)
			throw new DBException(6);

		for (PfBsOfficer t : pfBsOfficer)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			pfBsOfficer = pfBsOfficerReposDay.saveAll(pfBsOfficer);
			pfBsOfficerReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfBsOfficer = pfBsOfficerReposMon.saveAll(pfBsOfficer);
			pfBsOfficerReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfBsOfficer = pfBsOfficerReposHist.saveAll(pfBsOfficer);
			pfBsOfficerReposHist.flush();
		} else {
			pfBsOfficer = pfBsOfficerRepos.saveAll(pfBsOfficer);
			pfBsOfficerRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<PfBsOfficer> pfBsOfficer, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (pfBsOfficer == null || pfBsOfficer.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			pfBsOfficerReposDay.deleteAll(pfBsOfficer);
			pfBsOfficerReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			pfBsOfficerReposMon.deleteAll(pfBsOfficer);
			pfBsOfficerReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			pfBsOfficerReposHist.deleteAll(pfBsOfficer);
			pfBsOfficerReposHist.flush();
		} else {
			pfBsOfficerRepos.deleteAll(pfBsOfficer);
			pfBsOfficerRepos.flush();
		}
	}

}
