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
import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.ClOtherRightsId;
import com.st1.itx.db.repository.online.ClOtherRightsRepository;
import com.st1.itx.db.repository.day.ClOtherRightsRepositoryDay;
import com.st1.itx.db.repository.mon.ClOtherRightsRepositoryMon;
import com.st1.itx.db.repository.hist.ClOtherRightsRepositoryHist;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clOtherRightsService")
@Repository
public class ClOtherRightsServiceImpl extends ASpringJpaParm implements ClOtherRightsService, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ClOtherRightsRepository clOtherRightsRepos;

	@Autowired
	private ClOtherRightsRepositoryDay clOtherRightsReposDay;

	@Autowired
	private ClOtherRightsRepositoryMon clOtherRightsReposMon;

	@Autowired
	private ClOtherRightsRepositoryHist clOtherRightsReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(clOtherRightsRepos);
		org.junit.Assert.assertNotNull(clOtherRightsReposDay);
		org.junit.Assert.assertNotNull(clOtherRightsReposMon);
		org.junit.Assert.assertNotNull(clOtherRightsReposHist);
	}

	@Override
	public ClOtherRights findById(ClOtherRightsId clOtherRightsId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + clOtherRightsId);
		Optional<ClOtherRights> clOtherRights = null;
		if (dbName.equals(ContentName.onDay))
			clOtherRights = clOtherRightsReposDay.findById(clOtherRightsId);
		else if (dbName.equals(ContentName.onMon))
			clOtherRights = clOtherRightsReposMon.findById(clOtherRightsId);
		else if (dbName.equals(ContentName.onHist))
			clOtherRights = clOtherRightsReposHist.findById(clOtherRightsId);
		else
			clOtherRights = clOtherRightsRepos.findById(clOtherRightsId);
		ClOtherRights obj = clOtherRights.isPresent() ? clOtherRights.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ClOtherRights> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClOtherRights> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo", "Seq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = clOtherRightsReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clOtherRightsReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clOtherRightsReposHist.findAll(pageable);
		else
			slice = clOtherRightsRepos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClOtherRights> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClOtherRights> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClCode1 " + dbName + " : " + "clCode1_0 : " + clCode1_0);
		if (dbName.equals(ContentName.onDay))
			slice = clOtherRightsReposDay.findAllByClCode1IsOrderByClCode2AscClNoAscSeqAsc(clCode1_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clOtherRightsReposMon.findAllByClCode1IsOrderByClCode2AscClNoAscSeqAsc(clCode1_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clOtherRightsReposHist.findAllByClCode1IsOrderByClCode2AscClNoAscSeqAsc(clCode1_0, pageable);
		else
			slice = clOtherRightsRepos.findAllByClCode1IsOrderByClCode2AscClNoAscSeqAsc(clCode1_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClOtherRights> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClOtherRights> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClCode2 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1);
		if (dbName.equals(ContentName.onDay))
			slice = clOtherRightsReposDay.findAllByClCode1IsAndClCode2IsOrderByClNoAscSeqAsc(clCode1_0, clCode2_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clOtherRightsReposMon.findAllByClCode1IsAndClCode2IsOrderByClNoAscSeqAsc(clCode1_0, clCode2_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clOtherRightsReposHist.findAllByClCode1IsAndClCode2IsOrderByClNoAscSeqAsc(clCode1_0, clCode2_1, pageable);
		else
			slice = clOtherRightsRepos.findAllByClCode1IsAndClCode2IsOrderByClNoAscSeqAsc(clCode1_0, clCode2_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClOtherRights> findClNo(int clCode1_0, int clCode2_1, int clNo_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClOtherRights> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClNo " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clNo_2 : " + clNo_2);
		if (dbName.equals(ContentName.onDay))
			slice = clOtherRightsReposDay.findAllByClCode1IsAndClCode2IsAndClNoIsOrderBySeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clOtherRightsReposMon.findAllByClCode1IsAndClCode2IsAndClNoIsOrderBySeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clOtherRightsReposHist.findAllByClCode1IsAndClCode2IsAndClNoIsOrderBySeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);
		else
			slice = clOtherRightsRepos.findAllByClCode1IsAndClCode2IsAndClNoIsOrderBySeqAsc(clCode1_0, clCode2_1, clNo_2, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClOtherRights> findClCodeRange(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int clNo_4, int clNo_5, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClOtherRights> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClCodeRange " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode1_1 : " + clCode1_1 + " clCode2_2 : " + clCode2_2 + " clCode2_3 : " + clCode2_3 + " clNo_4 : " + clNo_4
				+ " clNo_5 : " + clNo_5);
		if (dbName.equals(ContentName.onDay))
			slice = clOtherRightsReposDay
					.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscSeqAsc(
							clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clOtherRightsReposMon
					.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscSeqAsc(
							clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clOtherRightsReposHist
					.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscSeqAsc(
							clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, pageable);
		else
			slice = clOtherRightsRepos
					.findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscSeqAsc(
							clCode1_0, clCode1_1, clCode2_2, clCode2_3, clNo_4, clNo_5, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ClOtherRights holdById(ClOtherRightsId clOtherRightsId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clOtherRightsId);
		Optional<ClOtherRights> clOtherRights = null;
		if (dbName.equals(ContentName.onDay))
			clOtherRights = clOtherRightsReposDay.findByClOtherRightsId(clOtherRightsId);
		else if (dbName.equals(ContentName.onMon))
			clOtherRights = clOtherRightsReposMon.findByClOtherRightsId(clOtherRightsId);
		else if (dbName.equals(ContentName.onHist))
			clOtherRights = clOtherRightsReposHist.findByClOtherRightsId(clOtherRightsId);
		else
			clOtherRights = clOtherRightsRepos.findByClOtherRightsId(clOtherRightsId);
		return clOtherRights.isPresent() ? clOtherRights.get() : null;
	}

	@Override
	public ClOtherRights holdById(ClOtherRights clOtherRights, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clOtherRights.getClOtherRightsId());
		Optional<ClOtherRights> clOtherRightsT = null;
		if (dbName.equals(ContentName.onDay))
			clOtherRightsT = clOtherRightsReposDay.findByClOtherRightsId(clOtherRights.getClOtherRightsId());
		else if (dbName.equals(ContentName.onMon))
			clOtherRightsT = clOtherRightsReposMon.findByClOtherRightsId(clOtherRights.getClOtherRightsId());
		else if (dbName.equals(ContentName.onHist))
			clOtherRightsT = clOtherRightsReposHist.findByClOtherRightsId(clOtherRights.getClOtherRightsId());
		else
			clOtherRightsT = clOtherRightsRepos.findByClOtherRightsId(clOtherRights.getClOtherRightsId());
		return clOtherRightsT.isPresent() ? clOtherRightsT.get() : null;
	}

	@Override
	public ClOtherRights insert(ClOtherRights clOtherRights, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + clOtherRights.getClOtherRightsId());
		if (this.findById(clOtherRights.getClOtherRightsId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			clOtherRights.setCreateEmpNo(empNot);

		if (clOtherRights.getLastUpdateEmpNo() == null || clOtherRights.getLastUpdateEmpNo().isEmpty())
			clOtherRights.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clOtherRightsReposDay.saveAndFlush(clOtherRights);
		else if (dbName.equals(ContentName.onMon))
			return clOtherRightsReposMon.saveAndFlush(clOtherRights);
		else if (dbName.equals(ContentName.onHist))
			return clOtherRightsReposHist.saveAndFlush(clOtherRights);
		else
			return clOtherRightsRepos.saveAndFlush(clOtherRights);
	}

	@Override
	public ClOtherRights update(ClOtherRights clOtherRights, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clOtherRights.getClOtherRightsId());
		if (!empNot.isEmpty())
			clOtherRights.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clOtherRightsReposDay.saveAndFlush(clOtherRights);
		else if (dbName.equals(ContentName.onMon))
			return clOtherRightsReposMon.saveAndFlush(clOtherRights);
		else if (dbName.equals(ContentName.onHist))
			return clOtherRightsReposHist.saveAndFlush(clOtherRights);
		else
			return clOtherRightsRepos.saveAndFlush(clOtherRights);
	}

	@Override
	public ClOtherRights update2(ClOtherRights clOtherRights, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clOtherRights.getClOtherRightsId());
		if (!empNot.isEmpty())
			clOtherRights.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			clOtherRightsReposDay.saveAndFlush(clOtherRights);
		else if (dbName.equals(ContentName.onMon))
			clOtherRightsReposMon.saveAndFlush(clOtherRights);
		else if (dbName.equals(ContentName.onHist))
			clOtherRightsReposHist.saveAndFlush(clOtherRights);
		else
			clOtherRightsRepos.saveAndFlush(clOtherRights);
		return this.findById(clOtherRights.getClOtherRightsId());
	}

	@Override
	public void delete(ClOtherRights clOtherRights, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + clOtherRights.getClOtherRightsId());
		if (dbName.equals(ContentName.onDay)) {
			clOtherRightsReposDay.delete(clOtherRights);
			clOtherRightsReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clOtherRightsReposMon.delete(clOtherRights);
			clOtherRightsReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clOtherRightsReposHist.delete(clOtherRights);
			clOtherRightsReposHist.flush();
		} else {
			clOtherRightsRepos.delete(clOtherRights);
			clOtherRightsRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ClOtherRights> clOtherRights, TitaVo... titaVo) throws DBException {
		if (clOtherRights == null || clOtherRights.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (ClOtherRights t : clOtherRights) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			clOtherRights = clOtherRightsReposDay.saveAll(clOtherRights);
			clOtherRightsReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clOtherRights = clOtherRightsReposMon.saveAll(clOtherRights);
			clOtherRightsReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clOtherRights = clOtherRightsReposHist.saveAll(clOtherRights);
			clOtherRightsReposHist.flush();
		} else {
			clOtherRights = clOtherRightsRepos.saveAll(clOtherRights);
			clOtherRightsRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ClOtherRights> clOtherRights, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (clOtherRights == null || clOtherRights.size() == 0)
			throw new DBException(6);

		for (ClOtherRights t : clOtherRights)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			clOtherRights = clOtherRightsReposDay.saveAll(clOtherRights);
			clOtherRightsReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clOtherRights = clOtherRightsReposMon.saveAll(clOtherRights);
			clOtherRightsReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clOtherRights = clOtherRightsReposHist.saveAll(clOtherRights);
			clOtherRightsReposHist.flush();
		} else {
			clOtherRights = clOtherRightsRepos.saveAll(clOtherRights);
			clOtherRightsRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ClOtherRights> clOtherRights, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (clOtherRights == null || clOtherRights.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			clOtherRightsReposDay.deleteAll(clOtherRights);
			clOtherRightsReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clOtherRightsReposMon.deleteAll(clOtherRights);
			clOtherRightsReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clOtherRightsReposHist.deleteAll(clOtherRights);
			clOtherRightsReposHist.flush();
		} else {
			clOtherRightsRepos.deleteAll(clOtherRights);
			clOtherRightsRepos.flush();
		}
	}

}
