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
import com.st1.itx.db.domain.SpecInnReCheck;
import com.st1.itx.db.domain.SpecInnReCheckId;
import com.st1.itx.db.repository.online.SpecInnReCheckRepository;
import com.st1.itx.db.repository.day.SpecInnReCheckRepositoryDay;
import com.st1.itx.db.repository.mon.SpecInnReCheckRepositoryMon;
import com.st1.itx.db.repository.hist.SpecInnReCheckRepositoryHist;
import com.st1.itx.db.service.SpecInnReCheckService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("specInnReCheckService")
@Repository
public class SpecInnReCheckServiceImpl extends ASpringJpaParm implements SpecInnReCheckService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private SpecInnReCheckRepository specInnReCheckRepos;

	@Autowired
	private SpecInnReCheckRepositoryDay specInnReCheckReposDay;

	@Autowired
	private SpecInnReCheckRepositoryMon specInnReCheckReposMon;

	@Autowired
	private SpecInnReCheckRepositoryHist specInnReCheckReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(specInnReCheckRepos);
		org.junit.Assert.assertNotNull(specInnReCheckReposDay);
		org.junit.Assert.assertNotNull(specInnReCheckReposMon);
		org.junit.Assert.assertNotNull(specInnReCheckReposHist);
	}

	@Override
	public SpecInnReCheck findById(SpecInnReCheckId specInnReCheckId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + specInnReCheckId);
		Optional<SpecInnReCheck> specInnReCheck = null;
		if (dbName.equals(ContentName.onDay))
			specInnReCheck = specInnReCheckReposDay.findById(specInnReCheckId);
		else if (dbName.equals(ContentName.onMon))
			specInnReCheck = specInnReCheckReposMon.findById(specInnReCheckId);
		else if (dbName.equals(ContentName.onHist))
			specInnReCheck = specInnReCheckReposHist.findById(specInnReCheckId);
		else
			specInnReCheck = specInnReCheckRepos.findById(specInnReCheckId);
		SpecInnReCheck obj = specInnReCheck.isPresent() ? specInnReCheck.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<SpecInnReCheck> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<SpecInnReCheck> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = specInnReCheckReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = specInnReCheckReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = specInnReCheckReposHist.findAll(pageable);
		else
			slice = specInnReCheckRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<SpecInnReCheck> findCustNo(int custNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<SpecInnReCheck> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCustNo " + dbName + " : " + "custNo_0 : " + custNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = specInnReCheckReposDay.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = specInnReCheckReposMon.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = specInnReCheckReposHist.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);
		else
			slice = specInnReCheckRepos.findAllByCustNoIsOrderByFacmNoAsc(custNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<SpecInnReCheck> findCustFacmNo(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<SpecInnReCheck> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCustFacmNo " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " + facmNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = specInnReCheckReposDay.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = specInnReCheckReposMon.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = specInnReCheckReposHist.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);
		else
			slice = specInnReCheckRepos.findAllByCustNoIsAndFacmNoIs(custNo_0, facmNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public SpecInnReCheck holdById(SpecInnReCheckId specInnReCheckId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + specInnReCheckId);
		Optional<SpecInnReCheck> specInnReCheck = null;
		if (dbName.equals(ContentName.onDay))
			specInnReCheck = specInnReCheckReposDay.findBySpecInnReCheckId(specInnReCheckId);
		else if (dbName.equals(ContentName.onMon))
			specInnReCheck = specInnReCheckReposMon.findBySpecInnReCheckId(specInnReCheckId);
		else if (dbName.equals(ContentName.onHist))
			specInnReCheck = specInnReCheckReposHist.findBySpecInnReCheckId(specInnReCheckId);
		else
			specInnReCheck = specInnReCheckRepos.findBySpecInnReCheckId(specInnReCheckId);
		return specInnReCheck.isPresent() ? specInnReCheck.get() : null;
	}

	@Override
	public SpecInnReCheck holdById(SpecInnReCheck specInnReCheck, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + specInnReCheck.getSpecInnReCheckId());
		Optional<SpecInnReCheck> specInnReCheckT = null;
		if (dbName.equals(ContentName.onDay))
			specInnReCheckT = specInnReCheckReposDay.findBySpecInnReCheckId(specInnReCheck.getSpecInnReCheckId());
		else if (dbName.equals(ContentName.onMon))
			specInnReCheckT = specInnReCheckReposMon.findBySpecInnReCheckId(specInnReCheck.getSpecInnReCheckId());
		else if (dbName.equals(ContentName.onHist))
			specInnReCheckT = specInnReCheckReposHist.findBySpecInnReCheckId(specInnReCheck.getSpecInnReCheckId());
		else
			specInnReCheckT = specInnReCheckRepos.findBySpecInnReCheckId(specInnReCheck.getSpecInnReCheckId());
		return specInnReCheckT.isPresent() ? specInnReCheckT.get() : null;
	}

	@Override
	public SpecInnReCheck insert(SpecInnReCheck specInnReCheck, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + specInnReCheck.getSpecInnReCheckId());
		if (this.findById(specInnReCheck.getSpecInnReCheckId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			specInnReCheck.setCreateEmpNo(empNot);

		if (specInnReCheck.getLastUpdateEmpNo() == null || specInnReCheck.getLastUpdateEmpNo().isEmpty())
			specInnReCheck.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return specInnReCheckReposDay.saveAndFlush(specInnReCheck);
		else if (dbName.equals(ContentName.onMon))
			return specInnReCheckReposMon.saveAndFlush(specInnReCheck);
		else if (dbName.equals(ContentName.onHist))
			return specInnReCheckReposHist.saveAndFlush(specInnReCheck);
		else
			return specInnReCheckRepos.saveAndFlush(specInnReCheck);
	}

	@Override
	public SpecInnReCheck update(SpecInnReCheck specInnReCheck, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + specInnReCheck.getSpecInnReCheckId());
		if (!empNot.isEmpty())
			specInnReCheck.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return specInnReCheckReposDay.saveAndFlush(specInnReCheck);
		else if (dbName.equals(ContentName.onMon))
			return specInnReCheckReposMon.saveAndFlush(specInnReCheck);
		else if (dbName.equals(ContentName.onHist))
			return specInnReCheckReposHist.saveAndFlush(specInnReCheck);
		else
			return specInnReCheckRepos.saveAndFlush(specInnReCheck);
	}

	@Override
	public SpecInnReCheck update2(SpecInnReCheck specInnReCheck, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + specInnReCheck.getSpecInnReCheckId());
		if (!empNot.isEmpty())
			specInnReCheck.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			specInnReCheckReposDay.saveAndFlush(specInnReCheck);
		else if (dbName.equals(ContentName.onMon))
			specInnReCheckReposMon.saveAndFlush(specInnReCheck);
		else if (dbName.equals(ContentName.onHist))
			specInnReCheckReposHist.saveAndFlush(specInnReCheck);
		else
			specInnReCheckRepos.saveAndFlush(specInnReCheck);
		return this.findById(specInnReCheck.getSpecInnReCheckId());
	}

	@Override
	public void delete(SpecInnReCheck specInnReCheck, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + specInnReCheck.getSpecInnReCheckId());
		if (dbName.equals(ContentName.onDay)) {
			specInnReCheckReposDay.delete(specInnReCheck);
			specInnReCheckReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			specInnReCheckReposMon.delete(specInnReCheck);
			specInnReCheckReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			specInnReCheckReposHist.delete(specInnReCheck);
			specInnReCheckReposHist.flush();
		} else {
			specInnReCheckRepos.delete(specInnReCheck);
			specInnReCheckRepos.flush();
		}
	}

	@Override
	public void insertAll(List<SpecInnReCheck> specInnReCheck, TitaVo... titaVo) throws DBException {
		if (specInnReCheck == null || specInnReCheck.size() == 0)
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
		for (SpecInnReCheck t : specInnReCheck) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			specInnReCheck = specInnReCheckReposDay.saveAll(specInnReCheck);
			specInnReCheckReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			specInnReCheck = specInnReCheckReposMon.saveAll(specInnReCheck);
			specInnReCheckReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			specInnReCheck = specInnReCheckReposHist.saveAll(specInnReCheck);
			specInnReCheckReposHist.flush();
		} else {
			specInnReCheck = specInnReCheckRepos.saveAll(specInnReCheck);
			specInnReCheckRepos.flush();
		}
	}

	@Override
	public void updateAll(List<SpecInnReCheck> specInnReCheck, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (specInnReCheck == null || specInnReCheck.size() == 0)
			throw new DBException(6);

		for (SpecInnReCheck t : specInnReCheck)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			specInnReCheck = specInnReCheckReposDay.saveAll(specInnReCheck);
			specInnReCheckReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			specInnReCheck = specInnReCheckReposMon.saveAll(specInnReCheck);
			specInnReCheckReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			specInnReCheck = specInnReCheckReposHist.saveAll(specInnReCheck);
			specInnReCheckReposHist.flush();
		} else {
			specInnReCheck = specInnReCheckRepos.saveAll(specInnReCheck);
			specInnReCheckRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<SpecInnReCheck> specInnReCheck, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (specInnReCheck == null || specInnReCheck.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			specInnReCheckReposDay.deleteAll(specInnReCheck);
			specInnReCheckReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			specInnReCheckReposMon.deleteAll(specInnReCheck);
			specInnReCheckReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			specInnReCheckReposHist.deleteAll(specInnReCheck);
			specInnReCheckReposHist.flush();
		} else {
			specInnReCheckRepos.deleteAll(specInnReCheck);
			specInnReCheckRepos.flush();
		}
	}

	@Override
	public void Usp_L5_InnReCheck_Upd(int tbsdyf, String empNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			specInnReCheckReposDay.uspL5InnrecheckUpd(tbsdyf, empNo);
		else if (dbName.equals(ContentName.onMon))
			specInnReCheckReposMon.uspL5InnrecheckUpd(tbsdyf, empNo);
		else if (dbName.equals(ContentName.onHist))
			specInnReCheckReposHist.uspL5InnrecheckUpd(tbsdyf, empNo);
		else
			specInnReCheckRepos.uspL5InnrecheckUpd(tbsdyf, empNo);
	}

}
