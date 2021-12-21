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
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.repository.online.ClBuildingRepository;
import com.st1.itx.db.repository.day.ClBuildingRepositoryDay;
import com.st1.itx.db.repository.mon.ClBuildingRepositoryMon;
import com.st1.itx.db.repository.hist.ClBuildingRepositoryHist;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clBuildingService")
@Repository
public class ClBuildingServiceImpl extends ASpringJpaParm implements ClBuildingService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ClBuildingRepository clBuildingRepos;

	@Autowired
	private ClBuildingRepositoryDay clBuildingReposDay;

	@Autowired
	private ClBuildingRepositoryMon clBuildingReposMon;

	@Autowired
	private ClBuildingRepositoryHist clBuildingReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(clBuildingRepos);
		org.junit.Assert.assertNotNull(clBuildingReposDay);
		org.junit.Assert.assertNotNull(clBuildingReposMon);
		org.junit.Assert.assertNotNull(clBuildingReposHist);
	}

	@Override
	public ClBuilding findById(ClBuildingId clBuildingId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + clBuildingId);
		Optional<ClBuilding> clBuilding = null;
		if (dbName.equals(ContentName.onDay))
			clBuilding = clBuildingReposDay.findById(clBuildingId);
		else if (dbName.equals(ContentName.onMon))
			clBuilding = clBuildingReposMon.findById(clBuildingId);
		else if (dbName.equals(ContentName.onHist))
			clBuilding = clBuildingReposHist.findById(clBuildingId);
		else
			clBuilding = clBuildingRepos.findById(clBuildingId);
		ClBuilding obj = clBuilding.isPresent() ? clBuilding.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ClBuilding> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClBuilding> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = clBuildingReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clBuildingReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clBuildingReposHist.findAll(pageable);
		else
			slice = clBuildingRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClBuilding> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClBuilding> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClCode1 " + dbName + " : " + "clCode1_0 : " + clCode1_0);
		if (dbName.equals(ContentName.onDay))
			slice = clBuildingReposDay.findAllByClCode1Is(clCode1_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clBuildingReposMon.findAllByClCode1Is(clCode1_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clBuildingReposHist.findAllByClCode1Is(clCode1_0, pageable);
		else
			slice = clBuildingRepos.findAllByClCode1Is(clCode1_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClBuilding> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClBuilding> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClCode2 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1);
		if (dbName.equals(ContentName.onDay))
			slice = clBuildingReposDay.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clBuildingReposMon.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clBuildingReposHist.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else
			slice = clBuildingRepos.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClBuilding> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClBuilding> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClNo " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clNo_2 : " + clNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = clBuildingReposDay.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clBuildingReposMon.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clBuildingReposHist.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);
		else
			slice = clBuildingRepos.findAllByClCode1IsAndClCode2IsAndClNoIs(clCode1_0, clCode2_1, clNo_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClBuilding> findBdLocationEq(String cityCode_0, String areaCode_1, String irCode_2, String bdNo1_3, String bdNo2_4, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClBuilding> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findBdLocationEq " + dbName + " : " + "cityCode_0 : " + cityCode_0 + " areaCode_1 : " + areaCode_1 + " irCode_2 : " + irCode_2 + " bdNo1_3 : " + bdNo1_3 + " bdNo2_4 : " + bdNo2_4);
		if (dbName.equals(ContentName.onDay))
			slice = clBuildingReposDay.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndBdNo1IsAndBdNo2IsOrderByClCode1AscClCode2AscClNoAsc(cityCode_0, areaCode_1, irCode_2, bdNo1_3, bdNo2_4, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clBuildingReposMon.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndBdNo1IsAndBdNo2IsOrderByClCode1AscClCode2AscClNoAsc(cityCode_0, areaCode_1, irCode_2, bdNo1_3, bdNo2_4, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clBuildingReposHist.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndBdNo1IsAndBdNo2IsOrderByClCode1AscClCode2AscClNoAsc(cityCode_0, areaCode_1, irCode_2, bdNo1_3, bdNo2_4, pageable);
		else
			slice = clBuildingRepos.findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndBdNo1IsAndBdNo2IsOrderByClCode1AscClCode2AscClNoAsc(cityCode_0, areaCode_1, irCode_2, bdNo1_3, bdNo2_4, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ClBuilding holdById(ClBuildingId clBuildingId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clBuildingId);
		Optional<ClBuilding> clBuilding = null;
		if (dbName.equals(ContentName.onDay))
			clBuilding = clBuildingReposDay.findByClBuildingId(clBuildingId);
		else if (dbName.equals(ContentName.onMon))
			clBuilding = clBuildingReposMon.findByClBuildingId(clBuildingId);
		else if (dbName.equals(ContentName.onHist))
			clBuilding = clBuildingReposHist.findByClBuildingId(clBuildingId);
		else
			clBuilding = clBuildingRepos.findByClBuildingId(clBuildingId);
		return clBuilding.isPresent() ? clBuilding.get() : null;
	}

	@Override
	public ClBuilding holdById(ClBuilding clBuilding, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clBuilding.getClBuildingId());
		Optional<ClBuilding> clBuildingT = null;
		if (dbName.equals(ContentName.onDay))
			clBuildingT = clBuildingReposDay.findByClBuildingId(clBuilding.getClBuildingId());
		else if (dbName.equals(ContentName.onMon))
			clBuildingT = clBuildingReposMon.findByClBuildingId(clBuilding.getClBuildingId());
		else if (dbName.equals(ContentName.onHist))
			clBuildingT = clBuildingReposHist.findByClBuildingId(clBuilding.getClBuildingId());
		else
			clBuildingT = clBuildingRepos.findByClBuildingId(clBuilding.getClBuildingId());
		return clBuildingT.isPresent() ? clBuildingT.get() : null;
	}

	@Override
	public ClBuilding insert(ClBuilding clBuilding, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + clBuilding.getClBuildingId());
		if (this.findById(clBuilding.getClBuildingId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			clBuilding.setCreateEmpNo(empNot);

		if (clBuilding.getLastUpdateEmpNo() == null || clBuilding.getLastUpdateEmpNo().isEmpty())
			clBuilding.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clBuildingReposDay.saveAndFlush(clBuilding);
		else if (dbName.equals(ContentName.onMon))
			return clBuildingReposMon.saveAndFlush(clBuilding);
		else if (dbName.equals(ContentName.onHist))
			return clBuildingReposHist.saveAndFlush(clBuilding);
		else
			return clBuildingRepos.saveAndFlush(clBuilding);
	}

	@Override
	public ClBuilding update(ClBuilding clBuilding, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clBuilding.getClBuildingId());
		if (!empNot.isEmpty())
			clBuilding.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clBuildingReposDay.saveAndFlush(clBuilding);
		else if (dbName.equals(ContentName.onMon))
			return clBuildingReposMon.saveAndFlush(clBuilding);
		else if (dbName.equals(ContentName.onHist))
			return clBuildingReposHist.saveAndFlush(clBuilding);
		else
			return clBuildingRepos.saveAndFlush(clBuilding);
	}

	@Override
	public ClBuilding update2(ClBuilding clBuilding, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clBuilding.getClBuildingId());
		if (!empNot.isEmpty())
			clBuilding.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			clBuildingReposDay.saveAndFlush(clBuilding);
		else if (dbName.equals(ContentName.onMon))
			clBuildingReposMon.saveAndFlush(clBuilding);
		else if (dbName.equals(ContentName.onHist))
			clBuildingReposHist.saveAndFlush(clBuilding);
		else
			clBuildingRepos.saveAndFlush(clBuilding);
		return this.findById(clBuilding.getClBuildingId());
	}

	@Override
	public void delete(ClBuilding clBuilding, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + clBuilding.getClBuildingId());
		if (dbName.equals(ContentName.onDay)) {
			clBuildingReposDay.delete(clBuilding);
			clBuildingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuildingReposMon.delete(clBuilding);
			clBuildingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuildingReposHist.delete(clBuilding);
			clBuildingReposHist.flush();
		} else {
			clBuildingRepos.delete(clBuilding);
			clBuildingRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ClBuilding> clBuilding, TitaVo... titaVo) throws DBException {
		if (clBuilding == null || clBuilding.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (ClBuilding t : clBuilding) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			clBuilding = clBuildingReposDay.saveAll(clBuilding);
			clBuildingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuilding = clBuildingReposMon.saveAll(clBuilding);
			clBuildingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuilding = clBuildingReposHist.saveAll(clBuilding);
			clBuildingReposHist.flush();
		} else {
			clBuilding = clBuildingRepos.saveAll(clBuilding);
			clBuildingRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ClBuilding> clBuilding, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (clBuilding == null || clBuilding.size() == 0)
			throw new DBException(6);

		for (ClBuilding t : clBuilding)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			clBuilding = clBuildingReposDay.saveAll(clBuilding);
			clBuildingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuilding = clBuildingReposMon.saveAll(clBuilding);
			clBuildingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuilding = clBuildingReposHist.saveAll(clBuilding);
			clBuildingReposHist.flush();
		} else {
			clBuilding = clBuildingRepos.saveAll(clBuilding);
			clBuildingRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ClBuilding> clBuilding, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (clBuilding == null || clBuilding.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			clBuildingReposDay.deleteAll(clBuilding);
			clBuildingReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clBuildingReposMon.deleteAll(clBuilding);
			clBuildingReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clBuildingReposHist.deleteAll(clBuilding);
			clBuildingReposHist.flush();
		} else {
			clBuildingRepos.deleteAll(clBuilding);
			clBuildingRepos.flush();
		}
	}

}
