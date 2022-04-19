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
import com.st1.itx.db.domain.GraceCondition;
import com.st1.itx.db.domain.GraceConditionId;
import com.st1.itx.db.repository.online.GraceConditionRepository;
import com.st1.itx.db.repository.day.GraceConditionRepositoryDay;
import com.st1.itx.db.repository.mon.GraceConditionRepositoryMon;
import com.st1.itx.db.repository.hist.GraceConditionRepositoryHist;
import com.st1.itx.db.service.GraceConditionService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("graceConditionService")
@Repository
public class GraceConditionServiceImpl extends ASpringJpaParm implements GraceConditionService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private GraceConditionRepository graceConditionRepos;

	@Autowired
	private GraceConditionRepositoryDay graceConditionReposDay;

	@Autowired
	private GraceConditionRepositoryMon graceConditionReposMon;

	@Autowired
	private GraceConditionRepositoryHist graceConditionReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(graceConditionRepos);
		org.junit.Assert.assertNotNull(graceConditionReposDay);
		org.junit.Assert.assertNotNull(graceConditionReposMon);
		org.junit.Assert.assertNotNull(graceConditionReposHist);
	}

	@Override
	public GraceCondition findById(GraceConditionId graceConditionId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + graceConditionId);
		Optional<GraceCondition> graceCondition = null;
		if (dbName.equals(ContentName.onDay))
			graceCondition = graceConditionReposDay.findById(graceConditionId);
		else if (dbName.equals(ContentName.onMon))
			graceCondition = graceConditionReposMon.findById(graceConditionId);
		else if (dbName.equals(ContentName.onHist))
			graceCondition = graceConditionReposHist.findById(graceConditionId);
		else
			graceCondition = graceConditionRepos.findById(graceConditionId);
		GraceCondition obj = graceCondition.isPresent() ? graceCondition.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<GraceCondition> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<GraceCondition> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = graceConditionReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = graceConditionReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = graceConditionReposHist.findAll(pageable);
		else
			slice = graceConditionRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<GraceCondition> custNoEq(int custNo_0, int custNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<GraceCondition> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " + custNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = graceConditionReposDay.findAllByCustNoLessThanEqualAndCustNoGreaterThanEqual(custNo_0, custNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = graceConditionReposMon.findAllByCustNoLessThanEqualAndCustNoGreaterThanEqual(custNo_0, custNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = graceConditionReposHist.findAllByCustNoLessThanEqualAndCustNoGreaterThanEqual(custNo_0, custNo_1, pageable);
		else
			slice = graceConditionRepos.findAllByCustNoLessThanEqualAndCustNoGreaterThanEqual(custNo_0, custNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public GraceCondition holdById(GraceConditionId graceConditionId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + graceConditionId);
		Optional<GraceCondition> graceCondition = null;
		if (dbName.equals(ContentName.onDay))
			graceCondition = graceConditionReposDay.findByGraceConditionId(graceConditionId);
		else if (dbName.equals(ContentName.onMon))
			graceCondition = graceConditionReposMon.findByGraceConditionId(graceConditionId);
		else if (dbName.equals(ContentName.onHist))
			graceCondition = graceConditionReposHist.findByGraceConditionId(graceConditionId);
		else
			graceCondition = graceConditionRepos.findByGraceConditionId(graceConditionId);
		return graceCondition.isPresent() ? graceCondition.get() : null;
	}

	@Override
	public GraceCondition holdById(GraceCondition graceCondition, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + graceCondition.getGraceConditionId());
		Optional<GraceCondition> graceConditionT = null;
		if (dbName.equals(ContentName.onDay))
			graceConditionT = graceConditionReposDay.findByGraceConditionId(graceCondition.getGraceConditionId());
		else if (dbName.equals(ContentName.onMon))
			graceConditionT = graceConditionReposMon.findByGraceConditionId(graceCondition.getGraceConditionId());
		else if (dbName.equals(ContentName.onHist))
			graceConditionT = graceConditionReposHist.findByGraceConditionId(graceCondition.getGraceConditionId());
		else
			graceConditionT = graceConditionRepos.findByGraceConditionId(graceCondition.getGraceConditionId());
		return graceConditionT.isPresent() ? graceConditionT.get() : null;
	}

	@Override
	public GraceCondition insert(GraceCondition graceCondition, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + graceCondition.getGraceConditionId());
		if (this.findById(graceCondition.getGraceConditionId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			graceCondition.setCreateEmpNo(empNot);

		if (graceCondition.getLastUpdateEmpNo() == null || graceCondition.getLastUpdateEmpNo().isEmpty())
			graceCondition.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return graceConditionReposDay.saveAndFlush(graceCondition);
		else if (dbName.equals(ContentName.onMon))
			return graceConditionReposMon.saveAndFlush(graceCondition);
		else if (dbName.equals(ContentName.onHist))
			return graceConditionReposHist.saveAndFlush(graceCondition);
		else
			return graceConditionRepos.saveAndFlush(graceCondition);
	}

	@Override
	public GraceCondition update(GraceCondition graceCondition, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + graceCondition.getGraceConditionId());
		if (!empNot.isEmpty())
			graceCondition.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return graceConditionReposDay.saveAndFlush(graceCondition);
		else if (dbName.equals(ContentName.onMon))
			return graceConditionReposMon.saveAndFlush(graceCondition);
		else if (dbName.equals(ContentName.onHist))
			return graceConditionReposHist.saveAndFlush(graceCondition);
		else
			return graceConditionRepos.saveAndFlush(graceCondition);
	}

	@Override
	public GraceCondition update2(GraceCondition graceCondition, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + graceCondition.getGraceConditionId());
		if (!empNot.isEmpty())
			graceCondition.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			graceConditionReposDay.saveAndFlush(graceCondition);
		else if (dbName.equals(ContentName.onMon))
			graceConditionReposMon.saveAndFlush(graceCondition);
		else if (dbName.equals(ContentName.onHist))
			graceConditionReposHist.saveAndFlush(graceCondition);
		else
			graceConditionRepos.saveAndFlush(graceCondition);
		return this.findById(graceCondition.getGraceConditionId());
	}

	@Override
	public void delete(GraceCondition graceCondition, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + graceCondition.getGraceConditionId());
		if (dbName.equals(ContentName.onDay)) {
			graceConditionReposDay.delete(graceCondition);
			graceConditionReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			graceConditionReposMon.delete(graceCondition);
			graceConditionReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			graceConditionReposHist.delete(graceCondition);
			graceConditionReposHist.flush();
		} else {
			graceConditionRepos.delete(graceCondition);
			graceConditionRepos.flush();
		}
	}

	@Override
	public void insertAll(List<GraceCondition> graceCondition, TitaVo... titaVo) throws DBException {
		if (graceCondition == null || graceCondition.size() == 0)
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
		for (GraceCondition t : graceCondition) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			graceCondition = graceConditionReposDay.saveAll(graceCondition);
			graceConditionReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			graceCondition = graceConditionReposMon.saveAll(graceCondition);
			graceConditionReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			graceCondition = graceConditionReposHist.saveAll(graceCondition);
			graceConditionReposHist.flush();
		} else {
			graceCondition = graceConditionRepos.saveAll(graceCondition);
			graceConditionRepos.flush();
		}
	}

	@Override
	public void updateAll(List<GraceCondition> graceCondition, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (graceCondition == null || graceCondition.size() == 0)
			throw new DBException(6);

		for (GraceCondition t : graceCondition)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			graceCondition = graceConditionReposDay.saveAll(graceCondition);
			graceConditionReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			graceCondition = graceConditionReposMon.saveAll(graceCondition);
			graceConditionReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			graceCondition = graceConditionReposHist.saveAll(graceCondition);
			graceConditionReposHist.flush();
		} else {
			graceCondition = graceConditionRepos.saveAll(graceCondition);
			graceConditionRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<GraceCondition> graceCondition, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (graceCondition == null || graceCondition.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			graceConditionReposDay.deleteAll(graceCondition);
			graceConditionReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			graceConditionReposMon.deleteAll(graceCondition);
			graceConditionReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			graceConditionReposHist.deleteAll(graceCondition);
			graceConditionReposHist.flush();
		} else {
			graceConditionRepos.deleteAll(graceCondition);
			graceConditionRepos.flush();
		}
	}

}
