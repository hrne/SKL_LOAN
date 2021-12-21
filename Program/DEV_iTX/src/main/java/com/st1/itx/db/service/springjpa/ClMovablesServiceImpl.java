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
import com.st1.itx.db.domain.ClMovables;
import com.st1.itx.db.domain.ClMovablesId;
import com.st1.itx.db.repository.online.ClMovablesRepository;
import com.st1.itx.db.repository.day.ClMovablesRepositoryDay;
import com.st1.itx.db.repository.mon.ClMovablesRepositoryMon;
import com.st1.itx.db.repository.hist.ClMovablesRepositoryHist;
import com.st1.itx.db.service.ClMovablesService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("clMovablesService")
@Repository
public class ClMovablesServiceImpl extends ASpringJpaParm implements ClMovablesService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private ClMovablesRepository clMovablesRepos;

	@Autowired
	private ClMovablesRepositoryDay clMovablesReposDay;

	@Autowired
	private ClMovablesRepositoryMon clMovablesReposMon;

	@Autowired
	private ClMovablesRepositoryHist clMovablesReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(clMovablesRepos);
		org.junit.Assert.assertNotNull(clMovablesReposDay);
		org.junit.Assert.assertNotNull(clMovablesReposMon);
		org.junit.Assert.assertNotNull(clMovablesReposHist);
	}

	@Override
	public ClMovables findById(ClMovablesId clMovablesId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + clMovablesId);
		Optional<ClMovables> clMovables = null;
		if (dbName.equals(ContentName.onDay))
			clMovables = clMovablesReposDay.findById(clMovablesId);
		else if (dbName.equals(ContentName.onMon))
			clMovables = clMovablesReposMon.findById(clMovablesId);
		else if (dbName.equals(ContentName.onHist))
			clMovables = clMovablesReposHist.findById(clMovablesId);
		else
			clMovables = clMovablesRepos.findById(clMovablesId);
		ClMovables obj = clMovables.isPresent() ? clMovables.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<ClMovables> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClMovables> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ClCode1", "ClCode2", "ClNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = clMovablesReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clMovablesReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clMovablesReposHist.findAll(pageable);
		else
			slice = clMovablesRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClMovables> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClMovables> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClCode1 " + dbName + " : " + "clCode1_0 : " + clCode1_0);
		if (dbName.equals(ContentName.onDay))
			slice = clMovablesReposDay.findAllByClCode1Is(clCode1_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clMovablesReposMon.findAllByClCode1Is(clCode1_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clMovablesReposHist.findAllByClCode1Is(clCode1_0, pageable);
		else
			slice = clMovablesRepos.findAllByClCode1Is(clCode1_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClMovables> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClMovables> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findClCode2 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1);
		if (dbName.equals(ContentName.onDay))
			slice = clMovablesReposDay.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clMovablesReposMon.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clMovablesReposHist.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);
		else
			slice = clMovablesRepos.findAllByClCode1IsAndClCode2Is(clCode1_0, clCode2_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClMovables> selectL2047(int clCode1_0, int clCode2_1, int clCode2_2, int clNo_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClMovables> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("selectL2047 " + dbName + " : " + "clCode1_0 : " + clCode1_0 + " clCode2_1 : " + clCode2_1 + " clCode2_2 : " + clCode2_2 + " clNo_3 : " + clNo_3);
		if (dbName.equals(ContentName.onDay))
			slice = clMovablesReposDay.findAllByClCode1IsAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoIs(clCode1_0, clCode2_1, clCode2_2, clNo_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clMovablesReposMon.findAllByClCode1IsAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoIs(clCode1_0, clCode2_1, clCode2_2, clNo_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clMovablesReposHist.findAllByClCode1IsAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoIs(clCode1_0, clCode2_1, clCode2_2, clNo_3, pageable);
		else
			slice = clMovablesRepos.findAllByClCode1IsAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoIs(clCode1_0, clCode2_1, clCode2_2, clNo_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClMovables> findUnique1(String productBrand_0, String productSpec_1, String ownerCustUKey_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClMovables> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findUnique1 " + dbName + " : " + "productBrand_0 : " + productBrand_0 + " productSpec_1 : " + productSpec_1 + " ownerCustUKey_2 : " + ownerCustUKey_2);
		if (dbName.equals(ContentName.onDay))
			slice = clMovablesReposDay.findAllByProductBrandIsAndProductSpecIsAndOwnerCustUKeyIs(productBrand_0, productSpec_1, ownerCustUKey_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clMovablesReposMon.findAllByProductBrandIsAndProductSpecIsAndOwnerCustUKeyIs(productBrand_0, productSpec_1, ownerCustUKey_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clMovablesReposHist.findAllByProductBrandIsAndProductSpecIsAndOwnerCustUKeyIs(productBrand_0, productSpec_1, ownerCustUKey_2, pageable);
		else
			slice = clMovablesRepos.findAllByProductBrandIsAndProductSpecIsAndOwnerCustUKeyIs(productBrand_0, productSpec_1, ownerCustUKey_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClMovables> findUnique2(String productBrand_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClMovables> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findUnique2 " + dbName + " : " + "productBrand_0 : " + productBrand_0);
		if (dbName.equals(ContentName.onDay))
			slice = clMovablesReposDay.findAllByProductBrandIs(productBrand_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clMovablesReposMon.findAllByProductBrandIs(productBrand_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clMovablesReposHist.findAllByProductBrandIs(productBrand_0, pageable);
		else
			slice = clMovablesRepos.findAllByProductBrandIs(productBrand_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClMovables> findUnique3(String licenseNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClMovables> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findUnique3 " + dbName + " : " + "licenseNo_0 : " + licenseNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = clMovablesReposDay.findAllByLicenseNoIs(licenseNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clMovablesReposMon.findAllByLicenseNoIs(licenseNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clMovablesReposHist.findAllByLicenseNoIs(licenseNo_0, pageable);
		else
			slice = clMovablesRepos.findAllByLicenseNoIs(licenseNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<ClMovables> findUnique4(String engineSN_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<ClMovables> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findUnique4 " + dbName + " : " + "engineSN_0 : " + engineSN_0);
		if (dbName.equals(ContentName.onDay))
			slice = clMovablesReposDay.findAllByEngineSNIs(engineSN_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = clMovablesReposMon.findAllByEngineSNIs(engineSN_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = clMovablesReposHist.findAllByEngineSNIs(engineSN_0, pageable);
		else
			slice = clMovablesRepos.findAllByEngineSNIs(engineSN_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public ClMovables holdById(ClMovablesId clMovablesId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clMovablesId);
		Optional<ClMovables> clMovables = null;
		if (dbName.equals(ContentName.onDay))
			clMovables = clMovablesReposDay.findByClMovablesId(clMovablesId);
		else if (dbName.equals(ContentName.onMon))
			clMovables = clMovablesReposMon.findByClMovablesId(clMovablesId);
		else if (dbName.equals(ContentName.onHist))
			clMovables = clMovablesReposHist.findByClMovablesId(clMovablesId);
		else
			clMovables = clMovablesRepos.findByClMovablesId(clMovablesId);
		return clMovables.isPresent() ? clMovables.get() : null;
	}

	@Override
	public ClMovables holdById(ClMovables clMovables, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + clMovables.getClMovablesId());
		Optional<ClMovables> clMovablesT = null;
		if (dbName.equals(ContentName.onDay))
			clMovablesT = clMovablesReposDay.findByClMovablesId(clMovables.getClMovablesId());
		else if (dbName.equals(ContentName.onMon))
			clMovablesT = clMovablesReposMon.findByClMovablesId(clMovables.getClMovablesId());
		else if (dbName.equals(ContentName.onHist))
			clMovablesT = clMovablesReposHist.findByClMovablesId(clMovables.getClMovablesId());
		else
			clMovablesT = clMovablesRepos.findByClMovablesId(clMovables.getClMovablesId());
		return clMovablesT.isPresent() ? clMovablesT.get() : null;
	}

	@Override
	public ClMovables insert(ClMovables clMovables, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + clMovables.getClMovablesId());
		if (this.findById(clMovables.getClMovablesId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			clMovables.setCreateEmpNo(empNot);

		if (clMovables.getLastUpdateEmpNo() == null || clMovables.getLastUpdateEmpNo().isEmpty())
			clMovables.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clMovablesReposDay.saveAndFlush(clMovables);
		else if (dbName.equals(ContentName.onMon))
			return clMovablesReposMon.saveAndFlush(clMovables);
		else if (dbName.equals(ContentName.onHist))
			return clMovablesReposHist.saveAndFlush(clMovables);
		else
			return clMovablesRepos.saveAndFlush(clMovables);
	}

	@Override
	public ClMovables update(ClMovables clMovables, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clMovables.getClMovablesId());
		if (!empNot.isEmpty())
			clMovables.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return clMovablesReposDay.saveAndFlush(clMovables);
		else if (dbName.equals(ContentName.onMon))
			return clMovablesReposMon.saveAndFlush(clMovables);
		else if (dbName.equals(ContentName.onHist))
			return clMovablesReposHist.saveAndFlush(clMovables);
		else
			return clMovablesRepos.saveAndFlush(clMovables);
	}

	@Override
	public ClMovables update2(ClMovables clMovables, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + clMovables.getClMovablesId());
		if (!empNot.isEmpty())
			clMovables.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			clMovablesReposDay.saveAndFlush(clMovables);
		else if (dbName.equals(ContentName.onMon))
			clMovablesReposMon.saveAndFlush(clMovables);
		else if (dbName.equals(ContentName.onHist))
			clMovablesReposHist.saveAndFlush(clMovables);
		else
			clMovablesRepos.saveAndFlush(clMovables);
		return this.findById(clMovables.getClMovablesId());
	}

	@Override
	public void delete(ClMovables clMovables, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + clMovables.getClMovablesId());
		if (dbName.equals(ContentName.onDay)) {
			clMovablesReposDay.delete(clMovables);
			clMovablesReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clMovablesReposMon.delete(clMovables);
			clMovablesReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clMovablesReposHist.delete(clMovables);
			clMovablesReposHist.flush();
		} else {
			clMovablesRepos.delete(clMovables);
			clMovablesRepos.flush();
		}
	}

	@Override
	public void insertAll(List<ClMovables> clMovables, TitaVo... titaVo) throws DBException {
		if (clMovables == null || clMovables.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (ClMovables t : clMovables) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			clMovables = clMovablesReposDay.saveAll(clMovables);
			clMovablesReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clMovables = clMovablesReposMon.saveAll(clMovables);
			clMovablesReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clMovables = clMovablesReposHist.saveAll(clMovables);
			clMovablesReposHist.flush();
		} else {
			clMovables = clMovablesRepos.saveAll(clMovables);
			clMovablesRepos.flush();
		}
	}

	@Override
	public void updateAll(List<ClMovables> clMovables, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (clMovables == null || clMovables.size() == 0)
			throw new DBException(6);

		for (ClMovables t : clMovables)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			clMovables = clMovablesReposDay.saveAll(clMovables);
			clMovablesReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clMovables = clMovablesReposMon.saveAll(clMovables);
			clMovablesReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clMovables = clMovablesReposHist.saveAll(clMovables);
			clMovablesReposHist.flush();
		} else {
			clMovables = clMovablesRepos.saveAll(clMovables);
			clMovablesRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<ClMovables> clMovables, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (clMovables == null || clMovables.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			clMovablesReposDay.deleteAll(clMovables);
			clMovablesReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			clMovablesReposMon.deleteAll(clMovables);
			clMovablesReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			clMovablesReposHist.deleteAll(clMovables);
			clMovablesReposHist.flush();
		} else {
			clMovablesRepos.deleteAll(clMovables);
			clMovablesRepos.flush();
		}
	}

}
