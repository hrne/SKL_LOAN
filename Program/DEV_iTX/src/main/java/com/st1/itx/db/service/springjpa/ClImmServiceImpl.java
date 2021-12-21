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
import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;
import com.st1.itx.db.repository.online.ClImmRepository;
import com.st1.itx.db.repository.day.ClImmRepositoryDay;
import com.st1.itx.db.repository.mon.ClImmRepositoryMon;
import com.st1.itx.db.repository.hist.ClImmRepositoryHist;
import com.st1.itx.db.service.ClImmService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clImmService")
@Repository
public class ClImmServiceImpl extends ASpringJpaParm implements ClImmService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ClImmRepository clImmRepos;

	@Autowired
	private ClImmRepositoryDay clImmReposDay;

	@Autowired
	private ClImmRepositoryMon clImmReposMon;

	@Autowired
	private ClImmRepositoryHist clImmReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(clImmRepos);
		org.junit.Assert.assertNotNull(clImmReposDay);
		org.junit.Assert.assertNotNull(clImmReposMon);
		org.junit.Assert.assertNotNull(clImmReposHist);
	}

	@Override
	public ClImm findById(ClImmId clImmId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + clImmId);
		Optional<ClImm> clImm = null;
		if (dbName.equals(ContentName.onDay))
			clImm = clImmReposDay.findById(clImmId);
		else if (dbName.equals(ContentName.onMon))
			clImm = clImmReposMon.findById(clImmId);
		else if (dbName.equals(ContentName.onHist))
			clImm = clImmReposHist.findById(clImmId);
		else
			clImm = clImmRepos.findById(clImmId);
		ClImm obj = clImm.isPresent() ? clImm.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ClImm> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClImm> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = clImmReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clImmReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clImmReposHist.findAll(pageable);
		else
			slice = clImmRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClImm> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClImm> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClCode1 " + dbName + " : " + "clCode1_0 : " + clCode1_0);
		if (dbName.equals(ContentName.onDay))
			slice = clImmReposDay.findAllByClCode1Is(clCode1_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clImmReposMon.findAllByClCode1Is(clCode1_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clImmReposHist.findAllByClCode1Is(clCode1_0, pageable);
		else
			slice = clImmRepos.findAllByClCode1Is(clCode1_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClImm> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClImm> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClCode2 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1);
		if (dbName.equals(ContentName.onDay))
			slice = clImmReposDay.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clImmReposMon.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clImmReposHist.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else
			slice = clImmRepos.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClImm> findRange(String settingStat_0, String settingStat_1, String clStat_2, String clStat_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClImm> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findRange " + dbName + " : " + "settingStat_0 : " + settingStat_0 + " settingStat_1 : " + settingStat_1 + " clStat_2 : " + clStat_2 + " clStat_3 : " + clStat_3);
		if (dbName.equals(ContentName.onDay))
			slice = clImmReposDay.findAllBySettingStatGreaterThanEqualAndSettingStatLessThanEqualAndClStatGreaterThanEqualAndClStatLessThanEqual(settingStat_0, settingStat_1, clStat_2, clStat_3,
					pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clImmReposMon.findAllBySettingStatGreaterThanEqualAndSettingStatLessThanEqualAndClStatGreaterThanEqualAndClStatLessThanEqual(settingStat_0, settingStat_1, clStat_2, clStat_3,
					pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clImmReposHist.findAllBySettingStatGreaterThanEqualAndSettingStatLessThanEqualAndClStatGreaterThanEqualAndClStatLessThanEqual(settingStat_0, settingStat_1, clStat_2, clStat_3,
					pageable);
		else
			slice = clImmRepos.findAllBySettingStatGreaterThanEqualAndSettingStatLessThanEqualAndClStatGreaterThanEqualAndClStatLessThanEqual(settingStat_0, settingStat_1, clStat_2, clStat_3,
					pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ClImm holdById(ClImmId clImmId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clImmId);
		Optional<ClImm> clImm = null;
		if (dbName.equals(ContentName.onDay))
			clImm = clImmReposDay.findByClImmId(clImmId);
		else if (dbName.equals(ContentName.onMon))
			clImm = clImmReposMon.findByClImmId(clImmId);
		else if (dbName.equals(ContentName.onHist))
			clImm = clImmReposHist.findByClImmId(clImmId);
		else
			clImm = clImmRepos.findByClImmId(clImmId);
		return clImm.isPresent() ? clImm.get() : null;
	}

	@Override
	public ClImm holdById(ClImm clImm, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clImm.getClImmId());
		Optional<ClImm> clImmT = null;
		if (dbName.equals(ContentName.onDay))
			clImmT = clImmReposDay.findByClImmId(clImm.getClImmId());
		else if (dbName.equals(ContentName.onMon))
			clImmT = clImmReposMon.findByClImmId(clImm.getClImmId());
		else if (dbName.equals(ContentName.onHist))
			clImmT = clImmReposHist.findByClImmId(clImm.getClImmId());
		else
			clImmT = clImmRepos.findByClImmId(clImm.getClImmId());
		return clImmT.isPresent() ? clImmT.get() : null;
	}

	@Override
	public ClImm insert(ClImm clImm, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + clImm.getClImmId());
		if (this.findById(clImm.getClImmId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			clImm.setCreateEmpNo(empNot);

		if (clImm.getLastUpdateEmpNo() == null || clImm.getLastUpdateEmpNo().isEmpty())
			clImm.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clImmReposDay.saveAndFlush(clImm);
		else if (dbName.equals(ContentName.onMon))
			return clImmReposMon.saveAndFlush(clImm);
		else if (dbName.equals(ContentName.onHist))
			return clImmReposHist.saveAndFlush(clImm);
		else
			return clImmRepos.saveAndFlush(clImm);
	}

	@Override
	public ClImm update(ClImm clImm, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clImm.getClImmId());
		if (!empNot.isEmpty())
			clImm.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clImmReposDay.saveAndFlush(clImm);
		else if (dbName.equals(ContentName.onMon))
			return clImmReposMon.saveAndFlush(clImm);
		else if (dbName.equals(ContentName.onHist))
			return clImmReposHist.saveAndFlush(clImm);
		else
			return clImmRepos.saveAndFlush(clImm);
	}

	@Override
	public ClImm update2(ClImm clImm, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clImm.getClImmId());
		if (!empNot.isEmpty())
			clImm.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			clImmReposDay.saveAndFlush(clImm);
		else if (dbName.equals(ContentName.onMon))
			clImmReposMon.saveAndFlush(clImm);
		else if (dbName.equals(ContentName.onHist))
			clImmReposHist.saveAndFlush(clImm);
		else
			clImmRepos.saveAndFlush(clImm);
		return this.findById(clImm.getClImmId());
	}

	@Override
	public void delete(ClImm clImm, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + clImm.getClImmId());
		if (dbName.equals(ContentName.onDay)) {
			clImmReposDay.delete(clImm);
			clImmReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clImmReposMon.delete(clImm);
			clImmReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clImmReposHist.delete(clImm);
			clImmReposHist.flush();
		} else {
			clImmRepos.delete(clImm);
			clImmRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ClImm> clImm, TitaVo... titaVo) throws DBException {
		if (clImm == null || clImm.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (ClImm t : clImm) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			clImm = clImmReposDay.saveAll(clImm);
			clImmReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clImm = clImmReposMon.saveAll(clImm);
			clImmReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clImm = clImmReposHist.saveAll(clImm);
			clImmReposHist.flush();
		} else {
			clImm = clImmRepos.saveAll(clImm);
			clImmRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ClImm> clImm, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (clImm == null || clImm.size() == 0)
			throw new DBException(6);

		for (ClImm t : clImm)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			clImm = clImmReposDay.saveAll(clImm);
			clImmReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clImm = clImmReposMon.saveAll(clImm);
			clImmReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clImm = clImmReposHist.saveAll(clImm);
			clImmReposHist.flush();
		} else {
			clImm = clImmRepos.saveAll(clImm);
			clImmRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ClImm> clImm, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (clImm == null || clImm.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			clImmReposDay.deleteAll(clImm);
			clImmReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clImmReposMon.deleteAll(clImm);
			clImmReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clImmReposHist.deleteAll(clImm);
			clImmReposHist.flush();
		} else {
			clImmRepos.deleteAll(clImm);
			clImmRepos.flush();
		}
	}

}
