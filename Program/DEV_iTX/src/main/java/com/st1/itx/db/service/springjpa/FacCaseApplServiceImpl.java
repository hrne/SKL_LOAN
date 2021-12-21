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
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.repository.online.FacCaseApplRepository;
import com.st1.itx.db.repository.day.FacCaseApplRepositoryDay;
import com.st1.itx.db.repository.mon.FacCaseApplRepositoryMon;
import com.st1.itx.db.repository.hist.FacCaseApplRepositoryHist;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facCaseApplService")
@Repository
public class FacCaseApplServiceImpl extends ASpringJpaParm implements FacCaseApplService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private FacCaseApplRepository facCaseApplRepos;

	@Autowired
	private FacCaseApplRepositoryDay facCaseApplReposDay;

	@Autowired
	private FacCaseApplRepositoryMon facCaseApplReposMon;

	@Autowired
	private FacCaseApplRepositoryHist facCaseApplReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(facCaseApplRepos);
		org.junit.Assert.assertNotNull(facCaseApplReposDay);
		org.junit.Assert.assertNotNull(facCaseApplReposMon);
		org.junit.Assert.assertNotNull(facCaseApplReposHist);
	}

	@Override
	public FacCaseAppl findById(int applNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + applNo);
		Optional<FacCaseAppl> facCaseAppl = null;
		if (dbName.equals(ContentName.onDay))
			facCaseAppl = facCaseApplReposDay.findById(applNo);
		else if (dbName.equals(ContentName.onMon))
			facCaseAppl = facCaseApplReposMon.findById(applNo);
		else if (dbName.equals(ContentName.onHist))
			facCaseAppl = facCaseApplReposHist.findById(applNo);
		else
			facCaseAppl = facCaseApplRepos.findById(applNo);
		FacCaseAppl obj = facCaseAppl.isPresent() ? facCaseAppl.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<FacCaseAppl> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacCaseAppl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ApplNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ApplNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = facCaseApplReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCaseApplReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCaseApplReposHist.findAll(pageable);
		else
			slice = facCaseApplRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacCaseAppl> caseApplNoRange(int applNo_0, int applNo_1, String processCode_2, String processCode_3, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacCaseAppl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("caseApplNoRange " + dbName + " : " + "applNo_0 : " + applNo_0 + " applNo_1 : " + applNo_1 + " processCode_2 : " + processCode_2 + " processCode_3 : " + processCode_3);
		if (dbName.equals(ContentName.onDay))
			slice = facCaseApplReposDay.findAllByApplNoGreaterThanEqualAndApplNoLessThanEqualAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(applNo_0, applNo_1,
					processCode_2, processCode_3, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCaseApplReposMon.findAllByApplNoGreaterThanEqualAndApplNoLessThanEqualAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(applNo_0, applNo_1,
					processCode_2, processCode_3, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCaseApplReposHist.findAllByApplNoGreaterThanEqualAndApplNoLessThanEqualAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(applNo_0, applNo_1,
					processCode_2, processCode_3, pageable);
		else
			slice = facCaseApplRepos.findAllByApplNoGreaterThanEqualAndApplNoLessThanEqualAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(applNo_0, applNo_1, processCode_2,
					processCode_3, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacCaseAppl> caseApplCustUKeyEq(String custUKey_0, String processCode_1, String processCode_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacCaseAppl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("caseApplCustUKeyEq " + dbName + " : " + "custUKey_0 : " + custUKey_0 + " processCode_1 : " + processCode_1 + " processCode_2 : " + processCode_2);
		if (dbName.equals(ContentName.onDay))
			slice = facCaseApplReposDay.findAllByCustUKeyIsAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(custUKey_0, processCode_1, processCode_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCaseApplReposMon.findAllByCustUKeyIsAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(custUKey_0, processCode_1, processCode_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCaseApplReposHist.findAllByCustUKeyIsAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(custUKey_0, processCode_1, processCode_2, pageable);
		else
			slice = facCaseApplRepos.findAllByCustUKeyIsAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(custUKey_0, processCode_1, processCode_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<FacCaseAppl> caseApplGroupUKeyEq(String groupUKey_0, String processCode_1, String processCode_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacCaseAppl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("caseApplGroupUKeyEq " + dbName + " : " + "groupUKey_0 : " + groupUKey_0 + " processCode_1 : " + processCode_1 + " processCode_2 : " + processCode_2);
		if (dbName.equals(ContentName.onDay))
			slice = facCaseApplReposDay.findAllByGroupUKeyIsAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(groupUKey_0, processCode_1, processCode_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCaseApplReposMon.findAllByGroupUKeyIsAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(groupUKey_0, processCode_1, processCode_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCaseApplReposHist.findAllByGroupUKeyIsAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(groupUKey_0, processCode_1, processCode_2, pageable);
		else
			slice = facCaseApplRepos.findAllByGroupUKeyIsAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(groupUKey_0, processCode_1, processCode_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacCaseAppl caseApplGroupUKeyFirst(String groupUKey_0, int applNo_1, int applNo_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("caseApplGroupUKeyFirst " + dbName + " : " + "groupUKey_0 : " + groupUKey_0 + " applNo_1 : " + applNo_1 + " applNo_2 : " + applNo_2);
		Optional<FacCaseAppl> facCaseApplT = null;
		if (dbName.equals(ContentName.onDay))
			facCaseApplT = facCaseApplReposDay.findTopByGroupUKeyIsAndApplNoGreaterThanEqualAndApplNoLessThanEqualOrderByApplNoDesc(groupUKey_0, applNo_1, applNo_2);
		else if (dbName.equals(ContentName.onMon))
			facCaseApplT = facCaseApplReposMon.findTopByGroupUKeyIsAndApplNoGreaterThanEqualAndApplNoLessThanEqualOrderByApplNoDesc(groupUKey_0, applNo_1, applNo_2);
		else if (dbName.equals(ContentName.onHist))
			facCaseApplT = facCaseApplReposHist.findTopByGroupUKeyIsAndApplNoGreaterThanEqualAndApplNoLessThanEqualOrderByApplNoDesc(groupUKey_0, applNo_1, applNo_2);
		else
			facCaseApplT = facCaseApplRepos.findTopByGroupUKeyIsAndApplNoGreaterThanEqualAndApplNoLessThanEqualOrderByApplNoDesc(groupUKey_0, applNo_1, applNo_2);

		return facCaseApplT.isPresent() ? facCaseApplT.get() : null;
	}

	@Override
	public FacCaseAppl CreditSysNoFirst(int creditSysNo_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("CreditSysNoFirst " + dbName + " : " + "creditSysNo_0 : " + creditSysNo_0);
		Optional<FacCaseAppl> facCaseApplT = null;
		if (dbName.equals(ContentName.onDay))
			facCaseApplT = facCaseApplReposDay.findTopByCreditSysNoIsOrderByApplDateAsc(creditSysNo_0);
		else if (dbName.equals(ContentName.onMon))
			facCaseApplT = facCaseApplReposMon.findTopByCreditSysNoIsOrderByApplDateAsc(creditSysNo_0);
		else if (dbName.equals(ContentName.onHist))
			facCaseApplT = facCaseApplReposHist.findTopByCreditSysNoIsOrderByApplDateAsc(creditSysNo_0);
		else
			facCaseApplT = facCaseApplRepos.findTopByCreditSysNoIsOrderByApplDateAsc(creditSysNo_0);

		return facCaseApplT.isPresent() ? facCaseApplT.get() : null;
	}

	@Override
	public Slice<FacCaseAppl> syndNoEq(int syndNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<FacCaseAppl> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("syndNoEq " + dbName + " : " + "syndNo_0 : " + syndNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = facCaseApplReposDay.findAllBySyndNoIsOrderByApplNoAsc(syndNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = facCaseApplReposMon.findAllBySyndNoIsOrderByApplNoAsc(syndNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = facCaseApplReposHist.findAllBySyndNoIsOrderByApplNoAsc(syndNo_0, pageable);
		else
			slice = facCaseApplRepos.findAllBySyndNoIsOrderByApplNoAsc(syndNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public FacCaseAppl holdById(int applNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + applNo);
		Optional<FacCaseAppl> facCaseAppl = null;
		if (dbName.equals(ContentName.onDay))
			facCaseAppl = facCaseApplReposDay.findByApplNo(applNo);
		else if (dbName.equals(ContentName.onMon))
			facCaseAppl = facCaseApplReposMon.findByApplNo(applNo);
		else if (dbName.equals(ContentName.onHist))
			facCaseAppl = facCaseApplReposHist.findByApplNo(applNo);
		else
			facCaseAppl = facCaseApplRepos.findByApplNo(applNo);
		return facCaseAppl.isPresent() ? facCaseAppl.get() : null;
	}

	@Override
	public FacCaseAppl holdById(FacCaseAppl facCaseAppl, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + facCaseAppl.getApplNo());
		Optional<FacCaseAppl> facCaseApplT = null;
		if (dbName.equals(ContentName.onDay))
			facCaseApplT = facCaseApplReposDay.findByApplNo(facCaseAppl.getApplNo());
		else if (dbName.equals(ContentName.onMon))
			facCaseApplT = facCaseApplReposMon.findByApplNo(facCaseAppl.getApplNo());
		else if (dbName.equals(ContentName.onHist))
			facCaseApplT = facCaseApplReposHist.findByApplNo(facCaseAppl.getApplNo());
		else
			facCaseApplT = facCaseApplRepos.findByApplNo(facCaseAppl.getApplNo());
		return facCaseApplT.isPresent() ? facCaseApplT.get() : null;
	}

	@Override
	public FacCaseAppl insert(FacCaseAppl facCaseAppl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("Insert..." + dbName + " " + facCaseAppl.getApplNo());
		if (this.findById(facCaseAppl.getApplNo()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			facCaseAppl.setCreateEmpNo(empNot);

		if (facCaseAppl.getLastUpdateEmpNo() == null || facCaseAppl.getLastUpdateEmpNo().isEmpty())
			facCaseAppl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facCaseApplReposDay.saveAndFlush(facCaseAppl);
		else if (dbName.equals(ContentName.onMon))
			return facCaseApplReposMon.saveAndFlush(facCaseAppl);
		else if (dbName.equals(ContentName.onHist))
			return facCaseApplReposHist.saveAndFlush(facCaseAppl);
		else
			return facCaseApplRepos.saveAndFlush(facCaseAppl);
	}

	@Override
	public FacCaseAppl update(FacCaseAppl facCaseAppl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facCaseAppl.getApplNo());
		if (!empNot.isEmpty())
			facCaseAppl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return facCaseApplReposDay.saveAndFlush(facCaseAppl);
		else if (dbName.equals(ContentName.onMon))
			return facCaseApplReposMon.saveAndFlush(facCaseAppl);
		else if (dbName.equals(ContentName.onHist))
			return facCaseApplReposHist.saveAndFlush(facCaseAppl);
		else
			return facCaseApplRepos.saveAndFlush(facCaseAppl);
	}

	@Override
	public FacCaseAppl update2(FacCaseAppl facCaseAppl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + facCaseAppl.getApplNo());
		if (!empNot.isEmpty())
			facCaseAppl.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			facCaseApplReposDay.saveAndFlush(facCaseAppl);
		else if (dbName.equals(ContentName.onMon))
			facCaseApplReposMon.saveAndFlush(facCaseAppl);
		else if (dbName.equals(ContentName.onHist))
			facCaseApplReposHist.saveAndFlush(facCaseAppl);
		else
			facCaseApplRepos.saveAndFlush(facCaseAppl);
		return this.findById(facCaseAppl.getApplNo());
	}

	@Override
	public void delete(FacCaseAppl facCaseAppl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + facCaseAppl.getApplNo());
		if (dbName.equals(ContentName.onDay)) {
			facCaseApplReposDay.delete(facCaseAppl);
			facCaseApplReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facCaseApplReposMon.delete(facCaseAppl);
			facCaseApplReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facCaseApplReposHist.delete(facCaseAppl);
			facCaseApplReposHist.flush();
		} else {
			facCaseApplRepos.delete(facCaseAppl);
			facCaseApplRepos.flush();
		}
	}

	@Override
	public void insertAll(List<FacCaseAppl> facCaseAppl, TitaVo... titaVo) throws DBException {
		if (facCaseAppl == null || facCaseAppl.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		}
		this.info("InsertAll...");
		for (FacCaseAppl t : facCaseAppl) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			facCaseAppl = facCaseApplReposDay.saveAll(facCaseAppl);
			facCaseApplReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facCaseAppl = facCaseApplReposMon.saveAll(facCaseAppl);
			facCaseApplReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facCaseAppl = facCaseApplReposHist.saveAll(facCaseAppl);
			facCaseApplReposHist.flush();
		} else {
			facCaseAppl = facCaseApplRepos.saveAll(facCaseAppl);
			facCaseApplRepos.flush();
		}
	}

	@Override
	public void updateAll(List<FacCaseAppl> facCaseAppl, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (facCaseAppl == null || facCaseAppl.size() == 0)
			throw new DBException(6);

		for (FacCaseAppl t : facCaseAppl)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			facCaseAppl = facCaseApplReposDay.saveAll(facCaseAppl);
			facCaseApplReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facCaseAppl = facCaseApplReposMon.saveAll(facCaseAppl);
			facCaseApplReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facCaseAppl = facCaseApplReposHist.saveAll(facCaseAppl);
			facCaseApplReposHist.flush();
		} else {
			facCaseAppl = facCaseApplRepos.saveAll(facCaseAppl);
			facCaseApplRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<FacCaseAppl> facCaseAppl, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (facCaseAppl == null || facCaseAppl.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			facCaseApplReposDay.deleteAll(facCaseAppl);
			facCaseApplReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			facCaseApplReposMon.deleteAll(facCaseAppl);
			facCaseApplReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			facCaseApplReposHist.deleteAll(facCaseAppl);
			facCaseApplReposHist.flush();
		} else {
			facCaseApplRepos.deleteAll(facCaseAppl);
			facCaseApplRepos.flush();
		}
	}

}
