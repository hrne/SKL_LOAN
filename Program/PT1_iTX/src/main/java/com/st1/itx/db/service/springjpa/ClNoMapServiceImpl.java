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
import com.st1.itx.db.domain.ClNoMap;
import com.st1.itx.db.domain.ClNoMapId;
import com.st1.itx.db.repository.online.ClNoMapRepository;
import com.st1.itx.db.repository.day.ClNoMapRepositoryDay;
import com.st1.itx.db.repository.mon.ClNoMapRepositoryMon;
import com.st1.itx.db.repository.hist.ClNoMapRepositoryHist;
import com.st1.itx.db.service.ClNoMapService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clNoMapService")
@Repository
public class ClNoMapServiceImpl extends ASpringJpaParm implements ClNoMapService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ClNoMapRepository clNoMapRepos;

	@Autowired
	private ClNoMapRepositoryDay clNoMapReposDay;

	@Autowired
	private ClNoMapRepositoryMon clNoMapReposMon;

	@Autowired
	private ClNoMapRepositoryHist clNoMapReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(clNoMapRepos);
		org.junit.Assert.assertNotNull(clNoMapReposDay);
		org.junit.Assert.assertNotNull(clNoMapReposMon);
		org.junit.Assert.assertNotNull(clNoMapReposHist);
	}

	@Override
	public ClNoMap findById(ClNoMapId clNoMapId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + clNoMapId);
		Optional<ClNoMap> clNoMap = null;
		if (dbName.equals(ContentName.onDay))
			clNoMap = clNoMapReposDay.findById(clNoMapId);
		else if (dbName.equals(ContentName.onMon))
			clNoMap = clNoMapReposMon.findById(clNoMapId);
		else if (dbName.equals(ContentName.onHist))
			clNoMap = clNoMapReposHist.findById(clNoMapId);
		else
			clNoMap = clNoMapRepos.findById(clNoMapId);
		ClNoMap obj = clNoMap.isPresent() ? clNoMap.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ClNoMap> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClNoMap> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "GdrId1", "GdrId2", "GdrNum", "LgtSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "GdrId1", "GdrId2", "GdrNum", "LgtSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = clNoMapReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clNoMapReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clNoMapReposHist.findAll(pageable);
		else
			slice = clNoMapRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClNoMap> findGdrNum(int gdrId1_0, int gdrId2_1, int gdrNum_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClNoMap> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findGdrNum " + dbName + " : " + "gdrId1_0 : " + gdrId1_0 + " gdrId2_1 : " + gdrId2_1 + " gdrNum_2 : " + gdrNum_2);
		if (dbName.equals(ContentName.onDay))
			slice = clNoMapReposDay.findAllByGdrId1IsAndGdrId2IsAndGdrNumIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(gdrId1_0, gdrId2_1, gdrNum_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clNoMapReposMon.findAllByGdrId1IsAndGdrId2IsAndGdrNumIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(gdrId1_0, gdrId2_1, gdrNum_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clNoMapReposHist.findAllByGdrId1IsAndGdrId2IsAndGdrNumIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(gdrId1_0, gdrId2_1, gdrNum_2, pageable);
		else
			slice = clNoMapRepos.findAllByGdrId1IsAndGdrId2IsAndGdrNumIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(gdrId1_0, gdrId2_1, gdrNum_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClNoMap> findGdrNum2(int gdrId1_0, int gdrId2_1, int gdrNum_2, int lgtSeq_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClNoMap> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findGdrNum2 " + dbName + " : " + "gdrId1_0 : " + gdrId1_0 + " gdrId2_1 : " + gdrId2_1 + " gdrNum_2 : " + gdrNum_2 + " lgtSeq_3 : " + lgtSeq_3);
		if (dbName.equals(ContentName.onDay))
			slice = clNoMapReposDay.findAllByGdrId1IsAndGdrId2IsAndGdrNumIsAndLgtSeqIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(gdrId1_0, gdrId2_1, gdrNum_2, lgtSeq_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clNoMapReposMon.findAllByGdrId1IsAndGdrId2IsAndGdrNumIsAndLgtSeqIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(gdrId1_0, gdrId2_1, gdrNum_2, lgtSeq_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clNoMapReposHist.findAllByGdrId1IsAndGdrId2IsAndGdrNumIsAndLgtSeqIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(gdrId1_0, gdrId2_1, gdrNum_2, lgtSeq_3, pageable);
		else
			slice = clNoMapRepos.findAllByGdrId1IsAndGdrId2IsAndGdrNumIsAndLgtSeqIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(gdrId1_0, gdrId2_1, gdrNum_2, lgtSeq_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClNoMap> findMainLgtseq(int mainGdrId1_0, int mainGdrId2_1, int mainGdrNum_2, int mainLgtSeq_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClNoMap> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findMainLgtseq " + dbName + " : " + "mainGdrId1_0 : " + mainGdrId1_0 + " mainGdrId2_1 : " + mainGdrId2_1 + " mainGdrNum_2 : " + mainGdrNum_2 + " mainLgtSeq_3 : " + mainLgtSeq_3);
		if (dbName.equals(ContentName.onDay))
			slice = clNoMapReposDay.findAllByMainGdrId1IsAndMainGdrId2IsAndMainGdrNumIsAndMainLgtSeqIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(mainGdrId1_0, mainGdrId2_1, mainGdrNum_2,
					mainLgtSeq_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clNoMapReposMon.findAllByMainGdrId1IsAndMainGdrId2IsAndMainGdrNumIsAndMainLgtSeqIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(mainGdrId1_0, mainGdrId2_1, mainGdrNum_2,
					mainLgtSeq_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clNoMapReposHist.findAllByMainGdrId1IsAndMainGdrId2IsAndMainGdrNumIsAndMainLgtSeqIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(mainGdrId1_0, mainGdrId2_1, mainGdrNum_2,
					mainLgtSeq_3, pageable);
		else
			slice = clNoMapRepos.findAllByMainGdrId1IsAndMainGdrId2IsAndMainGdrNumIsAndMainLgtSeqIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(mainGdrId1_0, mainGdrId2_1, mainGdrNum_2, mainLgtSeq_3,
					pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClNoMap> findMainGdrNum(int mainGdrId1_0, int mainGdrId2_1, int mainGdrNum_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClNoMap> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findMainGdrNum " + dbName + " : " + "mainGdrId1_0 : " + mainGdrId1_0 + " mainGdrId2_1 : " + mainGdrId2_1 + " mainGdrNum_2 : " + mainGdrNum_2);
		if (dbName.equals(ContentName.onDay))
			slice = clNoMapReposDay.findAllByMainGdrId1IsAndMainGdrId2IsAndMainGdrNumIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(mainGdrId1_0, mainGdrId2_1, mainGdrNum_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clNoMapReposMon.findAllByMainGdrId1IsAndMainGdrId2IsAndMainGdrNumIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(mainGdrId1_0, mainGdrId2_1, mainGdrNum_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clNoMapReposHist.findAllByMainGdrId1IsAndMainGdrId2IsAndMainGdrNumIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(mainGdrId1_0, mainGdrId2_1, mainGdrNum_2, pageable);
		else
			slice = clNoMapRepos.findAllByMainGdrId1IsAndMainGdrId2IsAndMainGdrNumIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(mainGdrId1_0, mainGdrId2_1, mainGdrNum_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClNoMap> findNewClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClNoMap> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findNewClNo " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clNo_2 : " + clNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = clNoMapReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clNoMapReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clNoMapReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
		else
			slice = clNoMapRepos.findAllByClCode1IsAndClCode2IsAndClNoIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ClNoMap holdById(ClNoMapId clNoMapId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clNoMapId);
		Optional<ClNoMap> clNoMap = null;
		if (dbName.equals(ContentName.onDay))
			clNoMap = clNoMapReposDay.findByClNoMapId(clNoMapId);
		else if (dbName.equals(ContentName.onMon))
			clNoMap = clNoMapReposMon.findByClNoMapId(clNoMapId);
		else if (dbName.equals(ContentName.onHist))
			clNoMap = clNoMapReposHist.findByClNoMapId(clNoMapId);
		else
			clNoMap = clNoMapRepos.findByClNoMapId(clNoMapId);
		return clNoMap.isPresent() ? clNoMap.get() : null;
	}

	@Override
	public ClNoMap holdById(ClNoMap clNoMap, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clNoMap.getClNoMapId());
		Optional<ClNoMap> clNoMapT = null;
		if (dbName.equals(ContentName.onDay))
			clNoMapT = clNoMapReposDay.findByClNoMapId(clNoMap.getClNoMapId());
		else if (dbName.equals(ContentName.onMon))
			clNoMapT = clNoMapReposMon.findByClNoMapId(clNoMap.getClNoMapId());
		else if (dbName.equals(ContentName.onHist))
			clNoMapT = clNoMapReposHist.findByClNoMapId(clNoMap.getClNoMapId());
		else
			clNoMapT = clNoMapRepos.findByClNoMapId(clNoMap.getClNoMapId());
		return clNoMapT.isPresent() ? clNoMapT.get() : null;
	}

	@Override
	public ClNoMap insert(ClNoMap clNoMap, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + clNoMap.getClNoMapId());
		if (this.findById(clNoMap.getClNoMapId(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			clNoMap.setCreateEmpNo(empNot);

		if (clNoMap.getLastUpdateEmpNo() == null || clNoMap.getLastUpdateEmpNo().isEmpty())
			clNoMap.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clNoMapReposDay.saveAndFlush(clNoMap);
		else if (dbName.equals(ContentName.onMon))
			return clNoMapReposMon.saveAndFlush(clNoMap);
		else if (dbName.equals(ContentName.onHist))
			return clNoMapReposHist.saveAndFlush(clNoMap);
		else
			return clNoMapRepos.saveAndFlush(clNoMap);
	}

	@Override
	public ClNoMap update(ClNoMap clNoMap, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + clNoMap.getClNoMapId());
		if (!empNot.isEmpty())
			clNoMap.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clNoMapReposDay.saveAndFlush(clNoMap);
		else if (dbName.equals(ContentName.onMon))
			return clNoMapReposMon.saveAndFlush(clNoMap);
		else if (dbName.equals(ContentName.onHist))
			return clNoMapReposHist.saveAndFlush(clNoMap);
		else
			return clNoMapRepos.saveAndFlush(clNoMap);
	}

	@Override
	public ClNoMap update2(ClNoMap clNoMap, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + clNoMap.getClNoMapId());
		if (!empNot.isEmpty())
			clNoMap.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			clNoMapReposDay.saveAndFlush(clNoMap);
		else if (dbName.equals(ContentName.onMon))
			clNoMapReposMon.saveAndFlush(clNoMap);
		else if (dbName.equals(ContentName.onHist))
			clNoMapReposHist.saveAndFlush(clNoMap);
		else
			clNoMapRepos.saveAndFlush(clNoMap);
		return this.findById(clNoMap.getClNoMapId());
	}

	@Override
	public void delete(ClNoMap clNoMap, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + clNoMap.getClNoMapId());
		if (dbName.equals(ContentName.onDay)) {
			clNoMapReposDay.delete(clNoMap);
			clNoMapReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clNoMapReposMon.delete(clNoMap);
			clNoMapReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clNoMapReposHist.delete(clNoMap);
			clNoMapReposHist.flush();
		} else {
			clNoMapRepos.delete(clNoMap);
			clNoMapRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ClNoMap> clNoMap, TitaVo... titaVo) throws DBException {
		if (clNoMap == null || clNoMap.size() == 0)
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
		for (ClNoMap t : clNoMap) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			clNoMap = clNoMapReposDay.saveAll(clNoMap);
			clNoMapReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clNoMap = clNoMapReposMon.saveAll(clNoMap);
			clNoMapReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clNoMap = clNoMapReposHist.saveAll(clNoMap);
			clNoMapReposHist.flush();
		} else {
			clNoMap = clNoMapRepos.saveAll(clNoMap);
			clNoMapRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ClNoMap> clNoMap, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (clNoMap == null || clNoMap.size() == 0)
			throw new DBException(6);

		for (ClNoMap t : clNoMap)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			clNoMap = clNoMapReposDay.saveAll(clNoMap);
			clNoMapReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clNoMap = clNoMapReposMon.saveAll(clNoMap);
			clNoMapReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clNoMap = clNoMapReposHist.saveAll(clNoMap);
			clNoMapReposHist.flush();
		} else {
			clNoMap = clNoMapRepos.saveAll(clNoMap);
			clNoMapRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ClNoMap> clNoMap, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (clNoMap == null || clNoMap.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			clNoMapReposDay.deleteAll(clNoMap);
			clNoMapReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clNoMapReposMon.deleteAll(clNoMap);
			clNoMapReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clNoMapReposHist.deleteAll(clNoMap);
			clNoMapReposHist.flush();
		} else {
			clNoMapRepos.deleteAll(clNoMap);
			clNoMapRepos.flush();
		}
	}

}
