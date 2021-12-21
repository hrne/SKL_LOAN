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
import com.st1.itx.db.domain.ClBuildingReason;
import com.st1.itx.db.domain.ClBuildingReasonId;
import com.st1.itx.db.repository.online.ClBuildingReasonRepository;
import com.st1.itx.db.repository.day.ClBuildingReasonRepositoryDay;
import com.st1.itx.db.repository.mon.ClBuildingReasonRepositoryMon;
import com.st1.itx.db.repository.hist.ClBuildingReasonRepositoryHist;
import com.st1.itx.db.service.ClBuildingReasonService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clBuildingReasonService")
@Repository
public class ClBuildingReasonServiceImpl extends ASpringJpaParm implements ClBuildingReasonService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ClBuildingReasonRepository clBuildingReasonRepos;

	@Autowired
	private ClBuildingReasonRepositoryDay clBuildingReasonReposDay;

	@Autowired
	private ClBuildingReasonRepositoryMon clBuildingReasonReposMon;

	@Autowired
	private ClBuildingReasonRepositoryHist clBuildingReasonReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(clBuildingReasonRepos);
		org.junit.Assert.assertNotNull(clBuildingReasonReposDay);
		org.junit.Assert.assertNotNull(clBuildingReasonReposMon);
		org.junit.Assert.assertNotNull(clBuildingReasonReposHist);
	}

	@Override
	public ClBuildingReason findById(ClBuildingReasonId clBuildingReasonId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + clBuildingReasonId);
		Optional<ClBuildingReason> clBuildingReason = null;
		if (dbName.equals(ContentName.onDay))
			clBuildingReason = clBuildingReasonReposDay.findById(clBuildingReasonId);
		else if (dbName.equals(ContentName.onMon))
			clBuildingReason = clBuildingReasonReposMon.findById(clBuildingReasonId);
		else if (dbName.equals(ContentName.onHist))
			clBuildingReason = clBuildingReasonReposHist.findById(clBuildingReasonId);
		else
			clBuildingReason = clBuildingReasonRepos.findById(clBuildingReasonId);
		ClBuildingReason obj = clBuildingReason.isPresent() ? clBuildingReason.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ClBuildingReason> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClBuildingReason> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "ReasonSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "ReasonSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = clBuildingReasonReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clBuildingReasonReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clBuildingReasonReposHist.findAll(pageable);
		else
			slice = clBuildingReasonRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ClBuildingReason clNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("clNoFirst " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clNo_2 : " + clNo_2);
		Optional<ClBuildingReason> clBuildingReasonT = null;
		if (dbName.equals(ContentName.onDay))
			clBuildingReasonT = clBuildingReasonReposDay.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqDesc(clCode1_0, clCode2_1, clNo_2);
		else if (dbName.equals(ContentName.onMon))
			clBuildingReasonT = clBuildingReasonReposMon.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqDesc(clCode1_0, clCode2_1, clNo_2);
		else if (dbName.equals(ContentName.onHist))
			clBuildingReasonT = clBuildingReasonReposHist.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqDesc(clCode1_0, clCode2_1, clNo_2);
		else
			clBuildingReasonT = clBuildingReasonRepos.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqDesc(clCode1_0, clCode2_1, clNo_2);

		return clBuildingReasonT.isPresent() ? clBuildingReasonT.get() : null;
	}

	@Override
	public Slice<ClBuildingReason> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClBuildingReason> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("clNoEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clNo_2 : " + clNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = clBuildingReasonReposDay.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clBuildingReasonReposMon.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clBuildingReasonReposHist.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else
			slice = clBuildingReasonRepos.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ClBuildingReason holdById(ClBuildingReasonId clBuildingReasonId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clBuildingReasonId);
		Optional<ClBuildingReason> clBuildingReason = null;
		if (dbName.equals(ContentName.onDay))
			clBuildingReason = clBuildingReasonReposDay.findByClBuildingReasonId(clBuildingReasonId);
		else if (dbName.equals(ContentName.onMon))
			clBuildingReason = clBuildingReasonReposMon.findByClBuildingReasonId(clBuildingReasonId);
		else if (dbName.equals(ContentName.onHist))
			clBuildingReason = clBuildingReasonReposHist.findByClBuildingReasonId(clBuildingReasonId);
		else
			clBuildingReason = clBuildingReasonRepos.findByClBuildingReasonId(clBuildingReasonId);
		return clBuildingReason.isPresent() ? clBuildingReason.get() : null;
	}

	@Override
	public ClBuildingReason holdById(ClBuildingReason clBuildingReason, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clBuildingReason.getClBuildingReasonId());
		Optional<ClBuildingReason> clBuildingReasonT = null;
		if (dbName.equals(ContentName.onDay))
			clBuildingReasonT = clBuildingReasonReposDay.findByClBuildingReasonId(clBuildingReason.getClBuildingReasonId());
		else if (dbName.equals(ContentName.onMon))
			clBuildingReasonT = clBuildingReasonReposMon.findByClBuildingReasonId(clBuildingReason.getClBuildingReasonId());
		else if (dbName.equals(ContentName.onHist))
			clBuildingReasonT = clBuildingReasonReposHist.findByClBuildingReasonId(clBuildingReason.getClBuildingReasonId());
		else
			clBuildingReasonT = clBuildingReasonRepos.findByClBuildingReasonId(clBuildingReason.getClBuildingReasonId());
		return clBuildingReasonT.isPresent() ? clBuildingReasonT.get() : null;
	}

	@Override
	public ClBuildingReason insert(ClBuildingReason clBuildingReason, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + clBuildingReason.getClBuildingReasonId());
		if (this.findById(clBuildingReason.getClBuildingReasonId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			clBuildingReason.setCreateEmpNo(empNot);

		if (clBuildingReason.getLastUpdateEmpNo() == null || clBuildingReason.getLastUpdateEmpNo().isEmpty())
			clBuildingReason.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clBuildingReasonReposDay.saveAndFlush(clBuildingReason);
		else if (dbName.equals(ContentName.onMon))
			return clBuildingReasonReposMon.saveAndFlush(clBuildingReason);
		else if (dbName.equals(ContentName.onHist))
			return clBuildingReasonReposHist.saveAndFlush(clBuildingReason);
		else
			return clBuildingReasonRepos.saveAndFlush(clBuildingReason);
	}

	@Override
	public ClBuildingReason update(ClBuildingReason clBuildingReason, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + clBuildingReason.getClBuildingReasonId());
		if (!empNot.isEmpty())
			clBuildingReason.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clBuildingReasonReposDay.saveAndFlush(clBuildingReason);
		else if (dbName.equals(ContentName.onMon))
			return clBuildingReasonReposMon.saveAndFlush(clBuildingReason);
		else if (dbName.equals(ContentName.onHist))
			return clBuildingReasonReposHist.saveAndFlush(clBuildingReason);
		else
			return clBuildingReasonRepos.saveAndFlush(clBuildingReason);
	}

	@Override
	public ClBuildingReason update2(ClBuildingReason clBuildingReason, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + clBuildingReason.getClBuildingReasonId());
		if (!empNot.isEmpty())
			clBuildingReason.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			clBuildingReasonReposDay.saveAndFlush(clBuildingReason);
		else if (dbName.equals(ContentName.onMon))
			clBuildingReasonReposMon.saveAndFlush(clBuildingReason);
		else if (dbName.equals(ContentName.onHist))
			clBuildingReasonReposHist.saveAndFlush(clBuildingReason);
		else
			clBuildingReasonRepos.saveAndFlush(clBuildingReason);
		return this.findById(clBuildingReason.getClBuildingReasonId());
	}

	@Override
	public void delete(ClBuildingReason clBuildingReason, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + clBuildingReason.getClBuildingReasonId());
		if (dbName.equals(ContentName.onDay)) {
			clBuildingReasonReposDay.delete(clBuildingReason);
			clBuildingReasonReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuildingReasonReposMon.delete(clBuildingReason);
			clBuildingReasonReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuildingReasonReposHist.delete(clBuildingReason);
			clBuildingReasonReposHist.flush();
		} else {
			clBuildingReasonRepos.delete(clBuildingReason);
			clBuildingReasonRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ClBuildingReason> clBuildingReason, TitaVo... titaVo) throws DBException {
		if (clBuildingReason == null || clBuildingReason.size() == 0)
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
		for (ClBuildingReason t : clBuildingReason) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			clBuildingReason = clBuildingReasonReposDay.saveAll(clBuildingReason);
			clBuildingReasonReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuildingReason = clBuildingReasonReposMon.saveAll(clBuildingReason);
			clBuildingReasonReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuildingReason = clBuildingReasonReposHist.saveAll(clBuildingReason);
			clBuildingReasonReposHist.flush();
		} else {
			clBuildingReason = clBuildingReasonRepos.saveAll(clBuildingReason);
			clBuildingReasonRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ClBuildingReason> clBuildingReason, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (clBuildingReason == null || clBuildingReason.size() == 0)
			throw new DBException(6);

		for (ClBuildingReason t : clBuildingReason)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			clBuildingReason = clBuildingReasonReposDay.saveAll(clBuildingReason);
			clBuildingReasonReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuildingReason = clBuildingReasonReposMon.saveAll(clBuildingReason);
			clBuildingReasonReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuildingReason = clBuildingReasonReposHist.saveAll(clBuildingReason);
			clBuildingReasonReposHist.flush();
		} else {
			clBuildingReason = clBuildingReasonRepos.saveAll(clBuildingReason);
			clBuildingReasonRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ClBuildingReason> clBuildingReason, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (clBuildingReason == null || clBuildingReason.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			clBuildingReasonReposDay.deleteAll(clBuildingReason);
			clBuildingReasonReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuildingReasonReposMon.deleteAll(clBuildingReason);
			clBuildingReasonReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuildingReasonReposHist.deleteAll(clBuildingReason);
			clBuildingReasonReposHist.flush();
		} else {
			clBuildingReasonRepos.deleteAll(clBuildingReason);
			clBuildingReasonRepos.flush();
		}
	}

}
