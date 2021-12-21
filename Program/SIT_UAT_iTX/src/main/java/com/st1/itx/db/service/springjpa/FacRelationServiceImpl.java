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
import com.st1.itx.db.domain.FacRelation;
import com.st1.itx.db.domain.FacRelationId;
import com.st1.itx.db.repository.online.FacRelationRepository;
import com.st1.itx.db.repository.day.FacRelationRepositoryDay;
import com.st1.itx.db.repository.mon.FacRelationRepositoryMon;
import com.st1.itx.db.repository.hist.FacRelationRepositoryHist;
import com.st1.itx.db.service.FacRelationService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facRelationService")
@Repository
public class FacRelationServiceImpl extends ASpringJpaParm implements FacRelationService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private FacRelationRepository facRelationRepos;

	@Autowired
	private FacRelationRepositoryDay facRelationReposDay;

	@Autowired
	private FacRelationRepositoryMon facRelationReposMon;

	@Autowired
	private FacRelationRepositoryHist facRelationReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(facRelationRepos);
		org.junit.Assert.assertNotNull(facRelationReposDay);
		org.junit.Assert.assertNotNull(facRelationReposMon);
		org.junit.Assert.assertNotNull(facRelationReposHist);
	}

	@Override
	public FacRelation findById(FacRelationId facRelationId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + facRelationId);
		Optional<FacRelation> facRelation = null;
		if (dbName.equals(ContentName.onDay))
			facRelation = facRelationReposDay.findById(facRelationId);
		else if (dbName.equals(ContentName.onMon))
			facRelation = facRelationReposMon.findById(facRelationId);
		else if (dbName.equals(ContentName.onHist))
			facRelation = facRelationReposHist.findById(facRelationId);
		else
			facRelation = facRelationRepos.findById(facRelationId);
		FacRelation obj = facRelation.isPresent() ? facRelation.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<FacRelation> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacRelation> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CreditSysNo", "CustUKey"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CreditSysNo", "CustUKey"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = facRelationReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facRelationReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facRelationReposHist.findAll(pageable);
		else
			slice = facRelationRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacRelation> CreditSysNoAll(int creditSysNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacRelation> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("CreditSysNoAll " + dbName + " : " + "creditSysNo_0 : " + creditSysNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = facRelationReposDay.findAllByCreditSysNoIsOrderByCustUKeyAsc(creditSysNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facRelationReposMon.findAllByCreditSysNoIsOrderByCustUKeyAsc(creditSysNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facRelationReposHist.findAllByCreditSysNoIsOrderByCustUKeyAsc(creditSysNo_0, pageable);
		else
			slice = facRelationRepos.findAllByCreditSysNoIsOrderByCustUKeyAsc(creditSysNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacRelation> CustUKeyAll(String custUKey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacRelation> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("CustUKeyAll " + dbName + " : " + "custUKey_0 : " + custUKey_0);
		if (dbName.equals(ContentName.onDay))
			slice = facRelationReposDay.findAllByCustUKeyIsOrderByCustUKeyAsc(custUKey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facRelationReposMon.findAllByCustUKeyIsOrderByCustUKeyAsc(custUKey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facRelationReposHist.findAllByCustUKeyIsOrderByCustUKeyAsc(custUKey_0, pageable);
		else
			slice = facRelationRepos.findAllByCustUKeyIsOrderByCustUKeyAsc(custUKey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacRelation holdById(FacRelationId facRelationId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facRelationId);
		Optional<FacRelation> facRelation = null;
		if (dbName.equals(ContentName.onDay))
			facRelation = facRelationReposDay.findByFacRelationId(facRelationId);
		else if (dbName.equals(ContentName.onMon))
			facRelation = facRelationReposMon.findByFacRelationId(facRelationId);
		else if (dbName.equals(ContentName.onHist))
			facRelation = facRelationReposHist.findByFacRelationId(facRelationId);
		else
			facRelation = facRelationRepos.findByFacRelationId(facRelationId);
		return facRelation.isPresent() ? facRelation.get() : null;
	}

	@Override
	public FacRelation holdById(FacRelation facRelation, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facRelation.getFacRelationId());
		Optional<FacRelation> facRelationT = null;
		if (dbName.equals(ContentName.onDay))
			facRelationT = facRelationReposDay.findByFacRelationId(facRelation.getFacRelationId());
		else if (dbName.equals(ContentName.onMon))
			facRelationT = facRelationReposMon.findByFacRelationId(facRelation.getFacRelationId());
		else if (dbName.equals(ContentName.onHist))
			facRelationT = facRelationReposHist.findByFacRelationId(facRelation.getFacRelationId());
		else
			facRelationT = facRelationRepos.findByFacRelationId(facRelation.getFacRelationId());
		return facRelationT.isPresent() ? facRelationT.get() : null;
	}

	@Override
	public FacRelation insert(FacRelation facRelation, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + facRelation.getFacRelationId());
		if (this.findById(facRelation.getFacRelationId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			facRelation.setCreateEmpNo(empNot);

		if (facRelation.getLastUpdateEmpNo() == null || facRelation.getLastUpdateEmpNo().isEmpty())
			facRelation.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facRelationReposDay.saveAndFlush(facRelation);
		else if (dbName.equals(ContentName.onMon))
			return facRelationReposMon.saveAndFlush(facRelation);
		else if (dbName.equals(ContentName.onHist))
			return facRelationReposHist.saveAndFlush(facRelation);
		else
			return facRelationRepos.saveAndFlush(facRelation);
	}

	@Override
	public FacRelation update(FacRelation facRelation, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facRelation.getFacRelationId());
		if (!empNot.isEmpty())
			facRelation.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facRelationReposDay.saveAndFlush(facRelation);
		else if (dbName.equals(ContentName.onMon))
			return facRelationReposMon.saveAndFlush(facRelation);
		else if (dbName.equals(ContentName.onHist))
			return facRelationReposHist.saveAndFlush(facRelation);
		else
			return facRelationRepos.saveAndFlush(facRelation);
	}

	@Override
	public FacRelation update2(FacRelation facRelation, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facRelation.getFacRelationId());
		if (!empNot.isEmpty())
			facRelation.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			facRelationReposDay.saveAndFlush(facRelation);
		else if (dbName.equals(ContentName.onMon))
			facRelationReposMon.saveAndFlush(facRelation);
		else if (dbName.equals(ContentName.onHist))
			facRelationReposHist.saveAndFlush(facRelation);
		else
			facRelationRepos.saveAndFlush(facRelation);
		return this.findById(facRelation.getFacRelationId());
	}

	@Override
	public void delete(FacRelation facRelation, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + facRelation.getFacRelationId());
		if (dbName.equals(ContentName.onDay)) {
			facRelationReposDay.delete(facRelation);
			facRelationReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facRelationReposMon.delete(facRelation);
			facRelationReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facRelationReposHist.delete(facRelation);
			facRelationReposHist.flush();
		} else {
			facRelationRepos.delete(facRelation);
			facRelationRepos.flush();
		}
	}

	@Override
	public void insertAll(List<FacRelation> facRelation, TitaVo... titaVo) throws DBException {
		if (facRelation == null || facRelation.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (FacRelation t : facRelation) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			facRelation = facRelationReposDay.saveAll(facRelation);
			facRelationReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facRelation = facRelationReposMon.saveAll(facRelation);
			facRelationReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facRelation = facRelationReposHist.saveAll(facRelation);
			facRelationReposHist.flush();
		} else {
			facRelation = facRelationRepos.saveAll(facRelation);
			facRelationRepos.flush();
		}
	}

	@Override
	public void updateAll(List<FacRelation> facRelation, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (facRelation == null || facRelation.size() == 0)
			throw new DBException(6);

		for (FacRelation t : facRelation)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			facRelation = facRelationReposDay.saveAll(facRelation);
			facRelationReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facRelation = facRelationReposMon.saveAll(facRelation);
			facRelationReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facRelation = facRelationReposHist.saveAll(facRelation);
			facRelationReposHist.flush();
		} else {
			facRelation = facRelationRepos.saveAll(facRelation);
			facRelationRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<FacRelation> facRelation, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (facRelation == null || facRelation.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			facRelationReposDay.deleteAll(facRelation);
			facRelationReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facRelationReposMon.deleteAll(facRelation);
			facRelationReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facRelationReposHist.deleteAll(facRelation);
			facRelationReposHist.flush();
		} else {
			facRelationRepos.deleteAll(facRelation);
			facRelationRepos.flush();
		}
	}

}
