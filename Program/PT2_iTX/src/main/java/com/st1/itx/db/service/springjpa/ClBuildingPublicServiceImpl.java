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
import com.st1.itx.db.domain.ClBuildingPublic;
import com.st1.itx.db.domain.ClBuildingPublicId;
import com.st1.itx.db.repository.online.ClBuildingPublicRepository;
import com.st1.itx.db.repository.day.ClBuildingPublicRepositoryDay;
import com.st1.itx.db.repository.mon.ClBuildingPublicRepositoryMon;
import com.st1.itx.db.repository.hist.ClBuildingPublicRepositoryHist;
import com.st1.itx.db.service.ClBuildingPublicService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clBuildingPublicService")
@Repository
public class ClBuildingPublicServiceImpl extends ASpringJpaParm implements ClBuildingPublicService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ClBuildingPublicRepository clBuildingPublicRepos;

	@Autowired
	private ClBuildingPublicRepositoryDay clBuildingPublicReposDay;

	@Autowired
	private ClBuildingPublicRepositoryMon clBuildingPublicReposMon;

	@Autowired
	private ClBuildingPublicRepositoryHist clBuildingPublicReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(clBuildingPublicRepos);
		org.junit.Assert.assertNotNull(clBuildingPublicReposDay);
		org.junit.Assert.assertNotNull(clBuildingPublicReposMon);
		org.junit.Assert.assertNotNull(clBuildingPublicReposHist);
	}

	@Override
	public ClBuildingPublic findById(ClBuildingPublicId clBuildingPublicId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + clBuildingPublicId);
		Optional<ClBuildingPublic> clBuildingPublic = null;
		if (dbName.equals(ContentName.onDay))
			clBuildingPublic = clBuildingPublicReposDay.findById(clBuildingPublicId);
		else if (dbName.equals(ContentName.onMon))
			clBuildingPublic = clBuildingPublicReposMon.findById(clBuildingPublicId);
		else if (dbName.equals(ContentName.onHist))
			clBuildingPublic = clBuildingPublicReposHist.findById(clBuildingPublicId);
		else
			clBuildingPublic = clBuildingPublicRepos.findById(clBuildingPublicId);
		ClBuildingPublic obj = clBuildingPublic.isPresent() ? clBuildingPublic.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ClBuildingPublic> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClBuildingPublic> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "PublicSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "PublicSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = clBuildingPublicReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clBuildingPublicReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clBuildingPublicReposHist.findAll(pageable);
		else
			slice = clBuildingPublicRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClBuildingPublic> clNoEq(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClBuildingPublic> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("clNoEq " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clNo_2 : " + clNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = clBuildingPublicReposDay.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clBuildingPublicReposMon.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clBuildingPublicReposHist.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else
			slice = clBuildingPublicRepos.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClBuildingPublic> publicBdNo1Eq(int publicBdNo1_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClBuildingPublic> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("publicBdNo1Eq " + dbName + " : " + "publicBdNo1_0 : " + publicBdNo1_0);
		if (dbName.equals(ContentName.onDay))
			slice = clBuildingPublicReposDay.findAllByPublicBdNo1Is(publicBdNo1_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clBuildingPublicReposMon.findAllByPublicBdNo1Is(publicBdNo1_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clBuildingPublicReposHist.findAllByPublicBdNo1Is(publicBdNo1_0, pageable);
		else
			slice = clBuildingPublicRepos.findAllByPublicBdNo1Is(publicBdNo1_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClBuildingPublic> publicBdNo2Eq(int publicBdNo1_0, int publicBdNo2_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClBuildingPublic> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("publicBdNo2Eq " + dbName + " : " + "publicBdNo1_0 : " + publicBdNo1_0 + " publicBdNo2_1 : " + publicBdNo2_1);
		if (dbName.equals(ContentName.onDay))
			slice = clBuildingPublicReposDay.findAllByPublicBdNo1IsAndPublicBdNo2Is(publicBdNo1_0, publicBdNo2_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clBuildingPublicReposMon.findAllByPublicBdNo1IsAndPublicBdNo2Is(publicBdNo1_0, publicBdNo2_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clBuildingPublicReposHist.findAllByPublicBdNo1IsAndPublicBdNo2Is(publicBdNo1_0, publicBdNo2_1, pageable);
		else
			slice = clBuildingPublicRepos.findAllByPublicBdNo1IsAndPublicBdNo2Is(publicBdNo1_0, publicBdNo2_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ClBuildingPublic holdById(ClBuildingPublicId clBuildingPublicId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clBuildingPublicId);
		Optional<ClBuildingPublic> clBuildingPublic = null;
		if (dbName.equals(ContentName.onDay))
			clBuildingPublic = clBuildingPublicReposDay.findByClBuildingPublicId(clBuildingPublicId);
		else if (dbName.equals(ContentName.onMon))
			clBuildingPublic = clBuildingPublicReposMon.findByClBuildingPublicId(clBuildingPublicId);
		else if (dbName.equals(ContentName.onHist))
			clBuildingPublic = clBuildingPublicReposHist.findByClBuildingPublicId(clBuildingPublicId);
		else
			clBuildingPublic = clBuildingPublicRepos.findByClBuildingPublicId(clBuildingPublicId);
		return clBuildingPublic.isPresent() ? clBuildingPublic.get() : null;
	}

	@Override
	public ClBuildingPublic holdById(ClBuildingPublic clBuildingPublic, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clBuildingPublic.getClBuildingPublicId());
		Optional<ClBuildingPublic> clBuildingPublicT = null;
		if (dbName.equals(ContentName.onDay))
			clBuildingPublicT = clBuildingPublicReposDay.findByClBuildingPublicId(clBuildingPublic.getClBuildingPublicId());
		else if (dbName.equals(ContentName.onMon))
			clBuildingPublicT = clBuildingPublicReposMon.findByClBuildingPublicId(clBuildingPublic.getClBuildingPublicId());
		else if (dbName.equals(ContentName.onHist))
			clBuildingPublicT = clBuildingPublicReposHist.findByClBuildingPublicId(clBuildingPublic.getClBuildingPublicId());
		else
			clBuildingPublicT = clBuildingPublicRepos.findByClBuildingPublicId(clBuildingPublic.getClBuildingPublicId());
		return clBuildingPublicT.isPresent() ? clBuildingPublicT.get() : null;
	}

	@Override
	public ClBuildingPublic insert(ClBuildingPublic clBuildingPublic, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + clBuildingPublic.getClBuildingPublicId());
		if (this.findById(clBuildingPublic.getClBuildingPublicId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			clBuildingPublic.setCreateEmpNo(empNot);

		if (clBuildingPublic.getLastUpdateEmpNo() == null || clBuildingPublic.getLastUpdateEmpNo().isEmpty())
			clBuildingPublic.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clBuildingPublicReposDay.saveAndFlush(clBuildingPublic);
		else if (dbName.equals(ContentName.onMon))
			return clBuildingPublicReposMon.saveAndFlush(clBuildingPublic);
		else if (dbName.equals(ContentName.onHist))
			return clBuildingPublicReposHist.saveAndFlush(clBuildingPublic);
		else
			return clBuildingPublicRepos.saveAndFlush(clBuildingPublic);
	}

	@Override
	public ClBuildingPublic update(ClBuildingPublic clBuildingPublic, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + clBuildingPublic.getClBuildingPublicId());
		if (!empNot.isEmpty())
			clBuildingPublic.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clBuildingPublicReposDay.saveAndFlush(clBuildingPublic);
		else if (dbName.equals(ContentName.onMon))
			return clBuildingPublicReposMon.saveAndFlush(clBuildingPublic);
		else if (dbName.equals(ContentName.onHist))
			return clBuildingPublicReposHist.saveAndFlush(clBuildingPublic);
		else
			return clBuildingPublicRepos.saveAndFlush(clBuildingPublic);
	}

	@Override
	public ClBuildingPublic update2(ClBuildingPublic clBuildingPublic, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + clBuildingPublic.getClBuildingPublicId());
		if (!empNot.isEmpty())
			clBuildingPublic.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			clBuildingPublicReposDay.saveAndFlush(clBuildingPublic);
		else if (dbName.equals(ContentName.onMon))
			clBuildingPublicReposMon.saveAndFlush(clBuildingPublic);
		else if (dbName.equals(ContentName.onHist))
			clBuildingPublicReposHist.saveAndFlush(clBuildingPublic);
		else
			clBuildingPublicRepos.saveAndFlush(clBuildingPublic);
		return this.findById(clBuildingPublic.getClBuildingPublicId());
	}

	@Override
	public void delete(ClBuildingPublic clBuildingPublic, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + clBuildingPublic.getClBuildingPublicId());
		if (dbName.equals(ContentName.onDay)) {
			clBuildingPublicReposDay.delete(clBuildingPublic);
			clBuildingPublicReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuildingPublicReposMon.delete(clBuildingPublic);
			clBuildingPublicReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuildingPublicReposHist.delete(clBuildingPublic);
			clBuildingPublicReposHist.flush();
		} else {
			clBuildingPublicRepos.delete(clBuildingPublic);
			clBuildingPublicRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ClBuildingPublic> clBuildingPublic, TitaVo... titaVo) throws DBException {
		if (clBuildingPublic == null || clBuildingPublic.size() == 0)
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
		for (ClBuildingPublic t : clBuildingPublic) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			clBuildingPublic = clBuildingPublicReposDay.saveAll(clBuildingPublic);
			clBuildingPublicReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuildingPublic = clBuildingPublicReposMon.saveAll(clBuildingPublic);
			clBuildingPublicReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuildingPublic = clBuildingPublicReposHist.saveAll(clBuildingPublic);
			clBuildingPublicReposHist.flush();
		} else {
			clBuildingPublic = clBuildingPublicRepos.saveAll(clBuildingPublic);
			clBuildingPublicRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ClBuildingPublic> clBuildingPublic, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (clBuildingPublic == null || clBuildingPublic.size() == 0)
			throw new DBException(6);

		for (ClBuildingPublic t : clBuildingPublic)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			clBuildingPublic = clBuildingPublicReposDay.saveAll(clBuildingPublic);
			clBuildingPublicReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuildingPublic = clBuildingPublicReposMon.saveAll(clBuildingPublic);
			clBuildingPublicReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuildingPublic = clBuildingPublicReposHist.saveAll(clBuildingPublic);
			clBuildingPublicReposHist.flush();
		} else {
			clBuildingPublic = clBuildingPublicRepos.saveAll(clBuildingPublic);
			clBuildingPublicRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ClBuildingPublic> clBuildingPublic, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (clBuildingPublic == null || clBuildingPublic.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			clBuildingPublicReposDay.deleteAll(clBuildingPublic);
			clBuildingPublicReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuildingPublicReposMon.deleteAll(clBuildingPublic);
			clBuildingPublicReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuildingPublicReposHist.deleteAll(clBuildingPublic);
			clBuildingPublicReposHist.flush();
		} else {
			clBuildingPublicRepos.deleteAll(clBuildingPublic);
			clBuildingPublicRepos.flush();
		}
	}

}
