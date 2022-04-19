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
import com.st1.itx.db.domain.CdSupv;
import com.st1.itx.db.repository.online.CdSupvRepository;
import com.st1.itx.db.repository.day.CdSupvRepositoryDay;
import com.st1.itx.db.repository.mon.CdSupvRepositoryMon;
import com.st1.itx.db.repository.hist.CdSupvRepositoryHist;
import com.st1.itx.db.service.CdSupvService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdSupvService")
@Repository
public class CdSupvServiceImpl extends ASpringJpaParm implements CdSupvService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdSupvRepository cdSupvRepos;

	@Autowired
	private CdSupvRepositoryDay cdSupvReposDay;

	@Autowired
	private CdSupvRepositoryMon cdSupvReposMon;

	@Autowired
	private CdSupvRepositoryHist cdSupvReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdSupvRepos);
		org.junit.Assert.assertNotNull(cdSupvReposDay);
		org.junit.Assert.assertNotNull(cdSupvReposMon);
		org.junit.Assert.assertNotNull(cdSupvReposHist);
	}

	@Override
	public CdSupv findById(String supvReasonCode, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + supvReasonCode);
		Optional<CdSupv> cdSupv = null;
		if (dbName.equals(ContentName.onDay))
			cdSupv = cdSupvReposDay.findById(supvReasonCode);
		else if (dbName.equals(ContentName.onMon))
			cdSupv = cdSupvReposMon.findById(supvReasonCode);
		else if (dbName.equals(ContentName.onHist))
			cdSupv = cdSupvReposHist.findById(supvReasonCode);
		else
			cdSupv = cdSupvRepos.findById(supvReasonCode);
		CdSupv obj = cdSupv.isPresent() ? cdSupv.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdSupv> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdSupv> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SupvReasonCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SupvReasonCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdSupvReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdSupvReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdSupvReposHist.findAll(pageable);
		else
			slice = cdSupvRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdSupv> findSupvReasonLevel(String supvReasonLevel_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdSupv> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findSupvReasonLevel " + dbName + " : " + "supvReasonLevel_0 : " + supvReasonLevel_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdSupvReposDay.findAllBySupvReasonLevelIs(supvReasonLevel_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdSupvReposMon.findAllBySupvReasonLevelIs(supvReasonLevel_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdSupvReposHist.findAllBySupvReasonLevelIs(supvReasonLevel_0, pageable);
		else
			slice = cdSupvRepos.findAllBySupvReasonLevelIs(supvReasonLevel_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdSupv> findSupvReasonCode(String supvReasonCode_0, String supvReasonCode_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdSupv> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findSupvReasonCode " + dbName + " : " + "supvReasonCode_0 : " + supvReasonCode_0 + " supvReasonCode_1 : " + supvReasonCode_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdSupvReposDay.findAllBySupvReasonCodeGreaterThanEqualAndSupvReasonCodeLessThanEqualOrderBySupvReasonCodeAsc(supvReasonCode_0, supvReasonCode_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdSupvReposMon.findAllBySupvReasonCodeGreaterThanEqualAndSupvReasonCodeLessThanEqualOrderBySupvReasonCodeAsc(supvReasonCode_0, supvReasonCode_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdSupvReposHist.findAllBySupvReasonCodeGreaterThanEqualAndSupvReasonCodeLessThanEqualOrderBySupvReasonCodeAsc(supvReasonCode_0, supvReasonCode_1, pageable);
		else
			slice = cdSupvRepos.findAllBySupvReasonCodeGreaterThanEqualAndSupvReasonCodeLessThanEqualOrderBySupvReasonCodeAsc(supvReasonCode_0, supvReasonCode_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdSupv holdById(String supvReasonCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + supvReasonCode);
		Optional<CdSupv> cdSupv = null;
		if (dbName.equals(ContentName.onDay))
			cdSupv = cdSupvReposDay.findBySupvReasonCode(supvReasonCode);
		else if (dbName.equals(ContentName.onMon))
			cdSupv = cdSupvReposMon.findBySupvReasonCode(supvReasonCode);
		else if (dbName.equals(ContentName.onHist))
			cdSupv = cdSupvReposHist.findBySupvReasonCode(supvReasonCode);
		else
			cdSupv = cdSupvRepos.findBySupvReasonCode(supvReasonCode);
		return cdSupv.isPresent() ? cdSupv.get() : null;
	}

	@Override
	public CdSupv holdById(CdSupv cdSupv, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdSupv.getSupvReasonCode());
		Optional<CdSupv> cdSupvT = null;
		if (dbName.equals(ContentName.onDay))
			cdSupvT = cdSupvReposDay.findBySupvReasonCode(cdSupv.getSupvReasonCode());
		else if (dbName.equals(ContentName.onMon))
			cdSupvT = cdSupvReposMon.findBySupvReasonCode(cdSupv.getSupvReasonCode());
		else if (dbName.equals(ContentName.onHist))
			cdSupvT = cdSupvReposHist.findBySupvReasonCode(cdSupv.getSupvReasonCode());
		else
			cdSupvT = cdSupvRepos.findBySupvReasonCode(cdSupv.getSupvReasonCode());
		return cdSupvT.isPresent() ? cdSupvT.get() : null;
	}

	@Override
	public CdSupv insert(CdSupv cdSupv, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdSupv.getSupvReasonCode());
		if (this.findById(cdSupv.getSupvReasonCode()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdSupv.setCreateEmpNo(empNot);

		if (cdSupv.getLastUpdateEmpNo() == null || cdSupv.getLastUpdateEmpNo().isEmpty())
			cdSupv.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdSupvReposDay.saveAndFlush(cdSupv);
		else if (dbName.equals(ContentName.onMon))
			return cdSupvReposMon.saveAndFlush(cdSupv);
		else if (dbName.equals(ContentName.onHist))
			return cdSupvReposHist.saveAndFlush(cdSupv);
		else
			return cdSupvRepos.saveAndFlush(cdSupv);
	}

	@Override
	public CdSupv update(CdSupv cdSupv, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdSupv.getSupvReasonCode());
		if (!empNot.isEmpty())
			cdSupv.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdSupvReposDay.saveAndFlush(cdSupv);
		else if (dbName.equals(ContentName.onMon))
			return cdSupvReposMon.saveAndFlush(cdSupv);
		else if (dbName.equals(ContentName.onHist))
			return cdSupvReposHist.saveAndFlush(cdSupv);
		else
			return cdSupvRepos.saveAndFlush(cdSupv);
	}

	@Override
	public CdSupv update2(CdSupv cdSupv, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdSupv.getSupvReasonCode());
		if (!empNot.isEmpty())
			cdSupv.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdSupvReposDay.saveAndFlush(cdSupv);
		else if (dbName.equals(ContentName.onMon))
			cdSupvReposMon.saveAndFlush(cdSupv);
		else if (dbName.equals(ContentName.onHist))
			cdSupvReposHist.saveAndFlush(cdSupv);
		else
			cdSupvRepos.saveAndFlush(cdSupv);
		return this.findById(cdSupv.getSupvReasonCode());
	}

	@Override
	public void delete(CdSupv cdSupv, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdSupv.getSupvReasonCode());
		if (dbName.equals(ContentName.onDay)) {
			cdSupvReposDay.delete(cdSupv);
			cdSupvReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSupvReposMon.delete(cdSupv);
			cdSupvReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSupvReposHist.delete(cdSupv);
			cdSupvReposHist.flush();
		} else {
			cdSupvRepos.delete(cdSupv);
			cdSupvRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdSupv> cdSupv, TitaVo... titaVo) throws DBException {
		if (cdSupv == null || cdSupv.size() == 0)
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
		for (CdSupv t : cdSupv) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdSupv = cdSupvReposDay.saveAll(cdSupv);
			cdSupvReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSupv = cdSupvReposMon.saveAll(cdSupv);
			cdSupvReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSupv = cdSupvReposHist.saveAll(cdSupv);
			cdSupvReposHist.flush();
		} else {
			cdSupv = cdSupvRepos.saveAll(cdSupv);
			cdSupvRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdSupv> cdSupv, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdSupv == null || cdSupv.size() == 0)
			throw new DBException(6);

		for (CdSupv t : cdSupv)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdSupv = cdSupvReposDay.saveAll(cdSupv);
			cdSupvReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSupv = cdSupvReposMon.saveAll(cdSupv);
			cdSupvReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSupv = cdSupvReposHist.saveAll(cdSupv);
			cdSupvReposHist.flush();
		} else {
			cdSupv = cdSupvRepos.saveAll(cdSupv);
			cdSupvRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdSupv> cdSupv, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdSupv == null || cdSupv.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdSupvReposDay.deleteAll(cdSupv);
			cdSupvReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdSupvReposMon.deleteAll(cdSupv);
			cdSupvReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdSupvReposHist.deleteAll(cdSupv);
			cdSupvReposHist.flush();
		} else {
			cdSupvRepos.deleteAll(cdSupv);
			cdSupvRepos.flush();
		}
	}

}
