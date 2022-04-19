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
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuOrignalId;
import com.st1.itx.db.repository.online.InsuOrignalRepository;
import com.st1.itx.db.repository.day.InsuOrignalRepositoryDay;
import com.st1.itx.db.repository.mon.InsuOrignalRepositoryMon;
import com.st1.itx.db.repository.hist.InsuOrignalRepositoryHist;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("insuOrignalService")
@Repository
public class InsuOrignalServiceImpl extends ASpringJpaParm implements InsuOrignalService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private InsuOrignalRepository insuOrignalRepos;

	@Autowired
	private InsuOrignalRepositoryDay insuOrignalReposDay;

	@Autowired
	private InsuOrignalRepositoryMon insuOrignalReposMon;

	@Autowired
	private InsuOrignalRepositoryHist insuOrignalReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(insuOrignalRepos);
		org.junit.Assert.assertNotNull(insuOrignalReposDay);
		org.junit.Assert.assertNotNull(insuOrignalReposMon);
		org.junit.Assert.assertNotNull(insuOrignalReposHist);
	}

	@Override
	public InsuOrignal findById(InsuOrignalId insuOrignalId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + insuOrignalId);
		Optional<InsuOrignal> insuOrignal = null;
		if (dbName.equals(ContentName.onDay))
			insuOrignal = insuOrignalReposDay.findById(insuOrignalId);
		else if (dbName.equals(ContentName.onMon))
			insuOrignal = insuOrignalReposMon.findById(insuOrignalId);
		else if (dbName.equals(ContentName.onHist))
			insuOrignal = insuOrignalReposHist.findById(insuOrignalId);
		else
			insuOrignal = insuOrignalRepos.findById(insuOrignalId);
		InsuOrignal obj = insuOrignal.isPresent() ? insuOrignal.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<InsuOrignal> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InsuOrignal> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "OrigInsuNo", "EndoInsuNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "OrigInsuNo", "EndoInsuNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = insuOrignalReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = insuOrignalReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = insuOrignalReposHist.findAll(pageable);
		else
			slice = insuOrignalRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public InsuOrignal clNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("clNoFirst " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clNo_2 : " + clNo_2);
		Optional<InsuOrignal> insuOrignalT = null;
		if (dbName.equals(ContentName.onDay))
			insuOrignalT = insuOrignalReposDay.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDesc(clCode1_0, clCode2_1, clNo_2);
		else if (dbName.equals(ContentName.onMon))
			insuOrignalT = insuOrignalReposMon.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDesc(clCode1_0, clCode2_1, clNo_2);
		else if (dbName.equals(ContentName.onHist))
			insuOrignalT = insuOrignalReposHist.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDesc(clCode1_0, clCode2_1, clNo_2);
		else
			insuOrignalT = insuOrignalRepos.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDesc(clCode1_0, clCode2_1, clNo_2);

		return insuOrignalT.isPresent() ? insuOrignalT.get() : null;
	}

	@Override
	public Slice<InsuOrignal> insuEndDateRange(int insuEndDate_0, int insuEndDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InsuOrignal> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("insuEndDateRange " + dbName + " : " + "insuEndDate_0 : " + insuEndDate_0 + " insuEndDate_1 : " + insuEndDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = insuOrignalReposDay.findAllByInsuEndDateGreaterThanEqualAndInsuEndDateLessThanEqualOrderByOrigInsuNoAscEndoInsuNoAsc(insuEndDate_0, insuEndDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = insuOrignalReposMon.findAllByInsuEndDateGreaterThanEqualAndInsuEndDateLessThanEqualOrderByOrigInsuNoAscEndoInsuNoAsc(insuEndDate_0, insuEndDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = insuOrignalReposHist.findAllByInsuEndDateGreaterThanEqualAndInsuEndDateLessThanEqualOrderByOrigInsuNoAscEndoInsuNoAsc(insuEndDate_0, insuEndDate_1, pageable);
		else
			slice = insuOrignalRepos.findAllByInsuEndDateGreaterThanEqualAndInsuEndDateLessThanEqualOrderByOrigInsuNoAscEndoInsuNoAsc(insuEndDate_0, insuEndDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<InsuOrignal> clNoEqual(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InsuOrignal> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("clNoEqual " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clNo_2 : " + clNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = insuOrignalReposDay.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = insuOrignalReposMon.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = insuOrignalReposHist.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else
			slice = insuOrignalRepos.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<InsuOrignal> findOrigInsuNoEq(int clCode1_0, int clCode2_1, int clNo_2, String origInsuNo_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<InsuOrignal> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findOrigInsuNoEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clNo_2 : " + clNo_2 + " origInsuNo_3 : " + origInsuNo_3);
		if (dbName.equals(ContentName.onDay))
			slice = insuOrignalReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsAndOrigInsuNoIs(clCode1_0, clCode2_1, clNo_2, origInsuNo_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = insuOrignalReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsAndOrigInsuNoIs(clCode1_0, clCode2_1, clNo_2, origInsuNo_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = insuOrignalReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsAndOrigInsuNoIs(clCode1_0, clCode2_1, clNo_2, origInsuNo_3, pageable);
		else
			slice = insuOrignalRepos.findAllByClCode1IsAndClCode2IsAndClNoIsAndOrigInsuNoIs(clCode1_0, clCode2_1, clNo_2, origInsuNo_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public InsuOrignal holdById(InsuOrignalId insuOrignalId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + insuOrignalId);
		Optional<InsuOrignal> insuOrignal = null;
		if (dbName.equals(ContentName.onDay))
			insuOrignal = insuOrignalReposDay.findByInsuOrignalId(insuOrignalId);
		else if (dbName.equals(ContentName.onMon))
			insuOrignal = insuOrignalReposMon.findByInsuOrignalId(insuOrignalId);
		else if (dbName.equals(ContentName.onHist))
			insuOrignal = insuOrignalReposHist.findByInsuOrignalId(insuOrignalId);
		else
			insuOrignal = insuOrignalRepos.findByInsuOrignalId(insuOrignalId);
		return insuOrignal.isPresent() ? insuOrignal.get() : null;
	}

	@Override
	public InsuOrignal holdById(InsuOrignal insuOrignal, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + insuOrignal.getInsuOrignalId());
		Optional<InsuOrignal> insuOrignalT = null;
		if (dbName.equals(ContentName.onDay))
			insuOrignalT = insuOrignalReposDay.findByInsuOrignalId(insuOrignal.getInsuOrignalId());
		else if (dbName.equals(ContentName.onMon))
			insuOrignalT = insuOrignalReposMon.findByInsuOrignalId(insuOrignal.getInsuOrignalId());
		else if (dbName.equals(ContentName.onHist))
			insuOrignalT = insuOrignalReposHist.findByInsuOrignalId(insuOrignal.getInsuOrignalId());
		else
			insuOrignalT = insuOrignalRepos.findByInsuOrignalId(insuOrignal.getInsuOrignalId());
		return insuOrignalT.isPresent() ? insuOrignalT.get() : null;
	}

	@Override
	public InsuOrignal insert(InsuOrignal insuOrignal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + insuOrignal.getInsuOrignalId());
		if (this.findById(insuOrignal.getInsuOrignalId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			insuOrignal.setCreateEmpNo(empNot);

		if (insuOrignal.getLastUpdateEmpNo() == null || insuOrignal.getLastUpdateEmpNo().isEmpty())
			insuOrignal.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return insuOrignalReposDay.saveAndFlush(insuOrignal);
		else if (dbName.equals(ContentName.onMon))
			return insuOrignalReposMon.saveAndFlush(insuOrignal);
		else if (dbName.equals(ContentName.onHist))
			return insuOrignalReposHist.saveAndFlush(insuOrignal);
		else
			return insuOrignalRepos.saveAndFlush(insuOrignal);
	}

	@Override
	public InsuOrignal update(InsuOrignal insuOrignal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + insuOrignal.getInsuOrignalId());
		if (!empNot.isEmpty())
			insuOrignal.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return insuOrignalReposDay.saveAndFlush(insuOrignal);
		else if (dbName.equals(ContentName.onMon))
			return insuOrignalReposMon.saveAndFlush(insuOrignal);
		else if (dbName.equals(ContentName.onHist))
			return insuOrignalReposHist.saveAndFlush(insuOrignal);
		else
			return insuOrignalRepos.saveAndFlush(insuOrignal);
	}

	@Override
	public InsuOrignal update2(InsuOrignal insuOrignal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + insuOrignal.getInsuOrignalId());
		if (!empNot.isEmpty())
			insuOrignal.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			insuOrignalReposDay.saveAndFlush(insuOrignal);
		else if (dbName.equals(ContentName.onMon))
			insuOrignalReposMon.saveAndFlush(insuOrignal);
		else if (dbName.equals(ContentName.onHist))
			insuOrignalReposHist.saveAndFlush(insuOrignal);
		else
			insuOrignalRepos.saveAndFlush(insuOrignal);
		return this.findById(insuOrignal.getInsuOrignalId());
	}

	@Override
	public void delete(InsuOrignal insuOrignal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + insuOrignal.getInsuOrignalId());
		if (dbName.equals(ContentName.onDay)) {
			insuOrignalReposDay.delete(insuOrignal);
			insuOrignalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			insuOrignalReposMon.delete(insuOrignal);
			insuOrignalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			insuOrignalReposHist.delete(insuOrignal);
			insuOrignalReposHist.flush();
		} else {
			insuOrignalRepos.delete(insuOrignal);
			insuOrignalRepos.flush();
		}
	}

	@Override
	public void insertAll(List<InsuOrignal> insuOrignal, TitaVo... titaVo) throws DBException {
		if (insuOrignal == null || insuOrignal.size() == 0)
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
		for (InsuOrignal t : insuOrignal) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			insuOrignal = insuOrignalReposDay.saveAll(insuOrignal);
			insuOrignalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			insuOrignal = insuOrignalReposMon.saveAll(insuOrignal);
			insuOrignalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			insuOrignal = insuOrignalReposHist.saveAll(insuOrignal);
			insuOrignalReposHist.flush();
		} else {
			insuOrignal = insuOrignalRepos.saveAll(insuOrignal);
			insuOrignalRepos.flush();
		}
	}

	@Override
	public void updateAll(List<InsuOrignal> insuOrignal, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (insuOrignal == null || insuOrignal.size() == 0)
			throw new DBException(6);

		for (InsuOrignal t : insuOrignal)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			insuOrignal = insuOrignalReposDay.saveAll(insuOrignal);
			insuOrignalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			insuOrignal = insuOrignalReposMon.saveAll(insuOrignal);
			insuOrignalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			insuOrignal = insuOrignalReposHist.saveAll(insuOrignal);
			insuOrignalReposHist.flush();
		} else {
			insuOrignal = insuOrignalRepos.saveAll(insuOrignal);
			insuOrignalRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<InsuOrignal> insuOrignal, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (insuOrignal == null || insuOrignal.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			insuOrignalReposDay.deleteAll(insuOrignal);
			insuOrignalReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			insuOrignalReposMon.deleteAll(insuOrignal);
			insuOrignalReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			insuOrignalReposHist.deleteAll(insuOrignal);
			insuOrignalReposHist.flush();
		} else {
			insuOrignalRepos.deleteAll(insuOrignal);
			insuOrignalRepos.flush();
		}
	}

}
