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
import com.st1.itx.db.domain.ClLandReason;
import com.st1.itx.db.domain.ClLandReasonId;
import com.st1.itx.db.repository.online.ClLandReasonRepository;
import com.st1.itx.db.repository.day.ClLandReasonRepositoryDay;
import com.st1.itx.db.repository.mon.ClLandReasonRepositoryMon;
import com.st1.itx.db.repository.hist.ClLandReasonRepositoryHist;
import com.st1.itx.db.service.ClLandReasonService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clLandReasonService")
@Repository
public class ClLandReasonServiceImpl extends ASpringJpaParm implements ClLandReasonService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ClLandReasonRepository clLandReasonRepos;

	@Autowired
	private ClLandReasonRepositoryDay clLandReasonReposDay;

	@Autowired
	private ClLandReasonRepositoryMon clLandReasonReposMon;

	@Autowired
	private ClLandReasonRepositoryHist clLandReasonReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(clLandReasonRepos);
		org.junit.Assert.assertNotNull(clLandReasonReposDay);
		org.junit.Assert.assertNotNull(clLandReasonReposMon);
		org.junit.Assert.assertNotNull(clLandReasonReposHist);
	}

	@Override
	public ClLandReason findById(ClLandReasonId clLandReasonId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + clLandReasonId);
		Optional<ClLandReason> clLandReason = null;
		if (dbName.equals(ContentName.onDay))
			clLandReason = clLandReasonReposDay.findById(clLandReasonId);
		else if (dbName.equals(ContentName.onMon))
			clLandReason = clLandReasonReposMon.findById(clLandReasonId);
		else if (dbName.equals(ContentName.onHist))
			clLandReason = clLandReasonReposHist.findById(clLandReasonId);
		else
			clLandReason = clLandReasonRepos.findById(clLandReasonId);
		ClLandReason obj = clLandReason.isPresent() ? clLandReason.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ClLandReason> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClLandReason> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "ReasonSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = clLandReasonReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clLandReasonReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clLandReasonReposHist.findAll(pageable);
		else
			slice = clLandReasonRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ClLandReason clNoFirst(int clCode1_0, int clCode2_1, int clNo_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("clNoFirst " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clNo_2 : " + clNo_2);
		Optional<ClLandReason> clLandReasonT = null;
		if (dbName.equals(ContentName.onDay))
			clLandReasonT = clLandReasonReposDay.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqDesc(clCode1_0, clCode2_1, clNo_2);
		else if (dbName.equals(ContentName.onMon))
			clLandReasonT = clLandReasonReposMon.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqDesc(clCode1_0, clCode2_1, clNo_2);
		else if (dbName.equals(ContentName.onHist))
			clLandReasonT = clLandReasonReposHist.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqDesc(clCode1_0, clCode2_1, clNo_2);
		else
			clLandReasonT = clLandReasonRepos.findTopByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqDesc(clCode1_0, clCode2_1, clNo_2);
		return clLandReasonT.isPresent() ? clLandReasonT.get() : null;
	}

	@Override
	public Slice<ClLandReason> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClLandReason> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("clNoEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clNo_2 : " + clNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = clLandReasonReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clLandReasonReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clLandReasonReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
		else
			slice = clLandReasonRepos.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ClLandReason holdById(ClLandReasonId clLandReasonId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clLandReasonId);
		Optional<ClLandReason> clLandReason = null;
		if (dbName.equals(ContentName.onDay))
			clLandReason = clLandReasonReposDay.findByClLandReasonId(clLandReasonId);
		else if (dbName.equals(ContentName.onMon))
			clLandReason = clLandReasonReposMon.findByClLandReasonId(clLandReasonId);
		else if (dbName.equals(ContentName.onHist))
			clLandReason = clLandReasonReposHist.findByClLandReasonId(clLandReasonId);
		else
			clLandReason = clLandReasonRepos.findByClLandReasonId(clLandReasonId);
		return clLandReason.isPresent() ? clLandReason.get() : null;
	}

	@Override
	public ClLandReason holdById(ClLandReason clLandReason, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clLandReason.getClLandReasonId());
		Optional<ClLandReason> clLandReasonT = null;
		if (dbName.equals(ContentName.onDay))
			clLandReasonT = clLandReasonReposDay.findByClLandReasonId(clLandReason.getClLandReasonId());
		else if (dbName.equals(ContentName.onMon))
			clLandReasonT = clLandReasonReposMon.findByClLandReasonId(clLandReason.getClLandReasonId());
		else if (dbName.equals(ContentName.onHist))
			clLandReasonT = clLandReasonReposHist.findByClLandReasonId(clLandReason.getClLandReasonId());
		else
			clLandReasonT = clLandReasonRepos.findByClLandReasonId(clLandReason.getClLandReasonId());
		return clLandReasonT.isPresent() ? clLandReasonT.get() : null;
	}

	@Override
	public ClLandReason insert(ClLandReason clLandReason, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + clLandReason.getClLandReasonId());
		if (this.findById(clLandReason.getClLandReasonId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			clLandReason.setCreateEmpNo(empNot);

		if (clLandReason.getLastUpdateEmpNo() == null || clLandReason.getLastUpdateEmpNo().isEmpty())
			clLandReason.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clLandReasonReposDay.saveAndFlush(clLandReason);
		else if (dbName.equals(ContentName.onMon))
			return clLandReasonReposMon.saveAndFlush(clLandReason);
		else if (dbName.equals(ContentName.onHist))
			return clLandReasonReposHist.saveAndFlush(clLandReason);
		else
			return clLandReasonRepos.saveAndFlush(clLandReason);
	}

	@Override
	public ClLandReason update(ClLandReason clLandReason, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clLandReason.getClLandReasonId());
		if (!empNot.isEmpty())
			clLandReason.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clLandReasonReposDay.saveAndFlush(clLandReason);
		else if (dbName.equals(ContentName.onMon))
			return clLandReasonReposMon.saveAndFlush(clLandReason);
		else if (dbName.equals(ContentName.onHist))
			return clLandReasonReposHist.saveAndFlush(clLandReason);
		else
			return clLandReasonRepos.saveAndFlush(clLandReason);
	}

	@Override
	public ClLandReason update2(ClLandReason clLandReason, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clLandReason.getClLandReasonId());
		if (!empNot.isEmpty())
			clLandReason.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			clLandReasonReposDay.saveAndFlush(clLandReason);
		else if (dbName.equals(ContentName.onMon))
			clLandReasonReposMon.saveAndFlush(clLandReason);
		else if (dbName.equals(ContentName.onHist))
			clLandReasonReposHist.saveAndFlush(clLandReason);
		else
			clLandReasonRepos.saveAndFlush(clLandReason);
		return this.findById(clLandReason.getClLandReasonId());
	}

	@Override
	public void delete(ClLandReason clLandReason, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + clLandReason.getClLandReasonId());
		if (dbName.equals(ContentName.onDay)) {
			clLandReasonReposDay.delete(clLandReason);
			clLandReasonReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clLandReasonReposMon.delete(clLandReason);
			clLandReasonReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clLandReasonReposHist.delete(clLandReason);
			clLandReasonReposHist.flush();
		} else {
			clLandReasonRepos.delete(clLandReason);
			clLandReasonRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ClLandReason> clLandReason, TitaVo... titaVo) throws DBException {
		if (clLandReason == null || clLandReason.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (ClLandReason t : clLandReason) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			clLandReason = clLandReasonReposDay.saveAll(clLandReason);
			clLandReasonReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clLandReason = clLandReasonReposMon.saveAll(clLandReason);
			clLandReasonReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clLandReason = clLandReasonReposHist.saveAll(clLandReason);
			clLandReasonReposHist.flush();
		} else {
			clLandReason = clLandReasonRepos.saveAll(clLandReason);
			clLandReasonRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ClLandReason> clLandReason, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (clLandReason == null || clLandReason.size() == 0)
			throw new DBException(6);

		for (ClLandReason t : clLandReason)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			clLandReason = clLandReasonReposDay.saveAll(clLandReason);
			clLandReasonReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clLandReason = clLandReasonReposMon.saveAll(clLandReason);
			clLandReasonReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clLandReason = clLandReasonReposHist.saveAll(clLandReason);
			clLandReasonReposHist.flush();
		} else {
			clLandReason = clLandReasonRepos.saveAll(clLandReason);
			clLandReasonRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ClLandReason> clLandReason, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (clLandReason == null || clLandReason.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			clLandReasonReposDay.deleteAll(clLandReason);
			clLandReasonReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clLandReasonReposMon.deleteAll(clLandReason);
			clLandReasonReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clLandReasonReposHist.deleteAll(clLandReason);
			clLandReasonReposHist.flush();
		} else {
			clLandReasonRepos.deleteAll(clLandReason);
			clLandReasonRepos.flush();
		}
	}

}
