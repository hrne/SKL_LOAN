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
import com.st1.itx.db.domain.CdIndustry;
import com.st1.itx.db.repository.online.CdIndustryRepository;
import com.st1.itx.db.repository.day.CdIndustryRepositoryDay;
import com.st1.itx.db.repository.mon.CdIndustryRepositoryMon;
import com.st1.itx.db.repository.hist.CdIndustryRepositoryHist;
import com.st1.itx.db.service.CdIndustryService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdIndustryService")
@Repository
public class CdIndustryServiceImpl extends ASpringJpaParm implements CdIndustryService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdIndustryRepository cdIndustryRepos;

	@Autowired
	private CdIndustryRepositoryDay cdIndustryReposDay;

	@Autowired
	private CdIndustryRepositoryMon cdIndustryReposMon;

	@Autowired
	private CdIndustryRepositoryHist cdIndustryReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdIndustryRepos);
		org.junit.Assert.assertNotNull(cdIndustryReposDay);
		org.junit.Assert.assertNotNull(cdIndustryReposMon);
		org.junit.Assert.assertNotNull(cdIndustryReposHist);
	}

	@Override
	public CdIndustry findById(String industryCode, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + industryCode);
		Optional<CdIndustry> cdIndustry = null;
		if (dbName.equals(ContentName.onDay))
			cdIndustry = cdIndustryReposDay.findById(industryCode);
		else if (dbName.equals(ContentName.onMon))
			cdIndustry = cdIndustryReposMon.findById(industryCode);
		else if (dbName.equals(ContentName.onHist))
			cdIndustry = cdIndustryReposHist.findById(industryCode);
		else
			cdIndustry = cdIndustryRepos.findById(industryCode);
		CdIndustry obj = cdIndustry.isPresent() ? cdIndustry.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdIndustry> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdIndustry> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "IndustryCode"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "IndustryCode"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdIndustryReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdIndustryReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdIndustryReposHist.findAll(pageable);
		else
			slice = cdIndustryRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdIndustry> findMainType(String mainType_0, String mainType_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdIndustry> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findMainType " + dbName + " : " + "mainType_0 : " + mainType_0 + " mainType_1 : " + mainType_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdIndustryReposDay.findAllByMainTypeGreaterThanEqualAndMainTypeLessThanEqual(mainType_0, mainType_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdIndustryReposMon.findAllByMainTypeGreaterThanEqualAndMainTypeLessThanEqual(mainType_0, mainType_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdIndustryReposHist.findAllByMainTypeGreaterThanEqualAndMainTypeLessThanEqual(mainType_0, mainType_1, pageable);
		else
			slice = cdIndustryRepos.findAllByMainTypeGreaterThanEqualAndMainTypeLessThanEqual(mainType_0, mainType_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdIndustry> findIndustryCode(String industryCode_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdIndustry> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findIndustryCode " + dbName + " : " + "industryCode_0 : " + industryCode_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdIndustryReposDay.findAllByIndustryCodeLikeOrderByIndustryCodeAsc(industryCode_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdIndustryReposMon.findAllByIndustryCodeLikeOrderByIndustryCodeAsc(industryCode_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdIndustryReposHist.findAllByIndustryCodeLikeOrderByIndustryCodeAsc(industryCode_0, pageable);
		else
			slice = cdIndustryRepos.findAllByIndustryCodeLikeOrderByIndustryCodeAsc(industryCode_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdIndustry> findIndustryItem(String industryItem_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdIndustry> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findIndustryItem " + dbName + " : " + "industryItem_0 : " + industryItem_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdIndustryReposDay.findAllByIndustryItemLikeOrderByIndustryCodeAsc(industryItem_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdIndustryReposMon.findAllByIndustryItemLikeOrderByIndustryCodeAsc(industryItem_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdIndustryReposHist.findAllByIndustryItemLikeOrderByIndustryCodeAsc(industryItem_0, pageable);
		else
			slice = cdIndustryRepos.findAllByIndustryItemLikeOrderByIndustryCodeAsc(industryItem_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdIndustry holdById(String industryCode, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + industryCode);
		Optional<CdIndustry> cdIndustry = null;
		if (dbName.equals(ContentName.onDay))
			cdIndustry = cdIndustryReposDay.findByIndustryCode(industryCode);
		else if (dbName.equals(ContentName.onMon))
			cdIndustry = cdIndustryReposMon.findByIndustryCode(industryCode);
		else if (dbName.equals(ContentName.onHist))
			cdIndustry = cdIndustryReposHist.findByIndustryCode(industryCode);
		else
			cdIndustry = cdIndustryRepos.findByIndustryCode(industryCode);
		return cdIndustry.isPresent() ? cdIndustry.get() : null;
	}

	@Override
	public CdIndustry holdById(CdIndustry cdIndustry, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdIndustry.getIndustryCode());
		Optional<CdIndustry> cdIndustryT = null;
		if (dbName.equals(ContentName.onDay))
			cdIndustryT = cdIndustryReposDay.findByIndustryCode(cdIndustry.getIndustryCode());
		else if (dbName.equals(ContentName.onMon))
			cdIndustryT = cdIndustryReposMon.findByIndustryCode(cdIndustry.getIndustryCode());
		else if (dbName.equals(ContentName.onHist))
			cdIndustryT = cdIndustryReposHist.findByIndustryCode(cdIndustry.getIndustryCode());
		else
			cdIndustryT = cdIndustryRepos.findByIndustryCode(cdIndustry.getIndustryCode());
		return cdIndustryT.isPresent() ? cdIndustryT.get() : null;
	}

	@Override
	public CdIndustry insert(CdIndustry cdIndustry, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdIndustry.getIndustryCode());
		if (this.findById(cdIndustry.getIndustryCode(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdIndustry.setCreateEmpNo(empNot);

		if (cdIndustry.getLastUpdateEmpNo() == null || cdIndustry.getLastUpdateEmpNo().isEmpty())
			cdIndustry.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdIndustryReposDay.saveAndFlush(cdIndustry);
		else if (dbName.equals(ContentName.onMon))
			return cdIndustryReposMon.saveAndFlush(cdIndustry);
		else if (dbName.equals(ContentName.onHist))
			return cdIndustryReposHist.saveAndFlush(cdIndustry);
		else
			return cdIndustryRepos.saveAndFlush(cdIndustry);
	}

	@Override
	public CdIndustry update(CdIndustry cdIndustry, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdIndustry.getIndustryCode());
		if (!empNot.isEmpty())
			cdIndustry.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdIndustryReposDay.saveAndFlush(cdIndustry);
		else if (dbName.equals(ContentName.onMon))
			return cdIndustryReposMon.saveAndFlush(cdIndustry);
		else if (dbName.equals(ContentName.onHist))
			return cdIndustryReposHist.saveAndFlush(cdIndustry);
		else
			return cdIndustryRepos.saveAndFlush(cdIndustry);
	}

	@Override
	public CdIndustry update2(CdIndustry cdIndustry, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdIndustry.getIndustryCode());
		if (!empNot.isEmpty())
			cdIndustry.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdIndustryReposDay.saveAndFlush(cdIndustry);
		else if (dbName.equals(ContentName.onMon))
			cdIndustryReposMon.saveAndFlush(cdIndustry);
		else if (dbName.equals(ContentName.onHist))
			cdIndustryReposHist.saveAndFlush(cdIndustry);
		else
			cdIndustryRepos.saveAndFlush(cdIndustry);
		return this.findById(cdIndustry.getIndustryCode());
	}

	@Override
	public void delete(CdIndustry cdIndustry, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdIndustry.getIndustryCode());
		if (dbName.equals(ContentName.onDay)) {
			cdIndustryReposDay.delete(cdIndustry);
			cdIndustryReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdIndustryReposMon.delete(cdIndustry);
			cdIndustryReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdIndustryReposHist.delete(cdIndustry);
			cdIndustryReposHist.flush();
		} else {
			cdIndustryRepos.delete(cdIndustry);
			cdIndustryRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdIndustry> cdIndustry, TitaVo... titaVo) throws DBException {
		if (cdIndustry == null || cdIndustry.size() == 0)
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
		for (CdIndustry t : cdIndustry) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdIndustry = cdIndustryReposDay.saveAll(cdIndustry);
			cdIndustryReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdIndustry = cdIndustryReposMon.saveAll(cdIndustry);
			cdIndustryReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdIndustry = cdIndustryReposHist.saveAll(cdIndustry);
			cdIndustryReposHist.flush();
		} else {
			cdIndustry = cdIndustryRepos.saveAll(cdIndustry);
			cdIndustryRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdIndustry> cdIndustry, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdIndustry == null || cdIndustry.size() == 0)
			throw new DBException(6);

		for (CdIndustry t : cdIndustry)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdIndustry = cdIndustryReposDay.saveAll(cdIndustry);
			cdIndustryReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdIndustry = cdIndustryReposMon.saveAll(cdIndustry);
			cdIndustryReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdIndustry = cdIndustryReposHist.saveAll(cdIndustry);
			cdIndustryReposHist.flush();
		} else {
			cdIndustry = cdIndustryRepos.saveAll(cdIndustry);
			cdIndustryRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdIndustry> cdIndustry, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdIndustry == null || cdIndustry.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdIndustryReposDay.deleteAll(cdIndustry);
			cdIndustryReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdIndustryReposMon.deleteAll(cdIndustry);
			cdIndustryReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdIndustryReposHist.deleteAll(cdIndustry);
			cdIndustryReposHist.flush();
		} else {
			cdIndustryRepos.deleteAll(cdIndustry);
			cdIndustryRepos.flush();
		}
	}

}
