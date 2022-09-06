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
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.repository.online.CdEmpRepository;
import com.st1.itx.db.repository.day.CdEmpRepositoryDay;
import com.st1.itx.db.repository.mon.CdEmpRepositoryMon;
import com.st1.itx.db.repository.hist.CdEmpRepositoryHist;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdEmpService")
@Repository
public class CdEmpServiceImpl extends ASpringJpaParm implements CdEmpService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private CdEmpRepository cdEmpRepos;

	@Autowired
	private CdEmpRepositoryDay cdEmpReposDay;

	@Autowired
	private CdEmpRepositoryMon cdEmpReposMon;

	@Autowired
	private CdEmpRepositoryHist cdEmpReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(cdEmpRepos);
		org.junit.Assert.assertNotNull(cdEmpReposDay);
		org.junit.Assert.assertNotNull(cdEmpReposMon);
		org.junit.Assert.assertNotNull(cdEmpReposHist);
	}

	@Override
	public CdEmp findById(String employeeNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + employeeNo);
		Optional<CdEmp> cdEmp = null;
		if (dbName.equals(ContentName.onDay))
			cdEmp = cdEmpReposDay.findById(employeeNo);
		else if (dbName.equals(ContentName.onMon))
			cdEmp = cdEmpReposMon.findById(employeeNo);
		else if (dbName.equals(ContentName.onHist))
			cdEmp = cdEmpReposHist.findById(employeeNo);
		else
			cdEmp = cdEmpRepos.findById(employeeNo);
		CdEmp obj = cdEmp.isPresent() ? cdEmp.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<CdEmp> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "EmployeeNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EmployeeNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAll(pageable);
		else
			slice = cdEmpRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> findEmployeeNo(String employeeNo_0, String employeeNo_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findEmployeeNo " + dbName + " : " + "employeeNo_0 : " + employeeNo_0 + " employeeNo_1 : " + employeeNo_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, pageable);
		else
			slice = cdEmpRepos.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> EmployeeNoLike(String employeeNo_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("EmployeeNoLike " + dbName + " : " + "employeeNo_0 : " + employeeNo_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByEmployeeNoLikeOrderByEmployeeNoAsc(employeeNo_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByEmployeeNoLikeOrderByEmployeeNoAsc(employeeNo_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByEmployeeNoLikeOrderByEmployeeNoAsc(employeeNo_0, pageable);
		else
			slice = cdEmpRepos.findAllByEmployeeNoLikeOrderByEmployeeNoAsc(employeeNo_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdEmp findAgentIdFirst(String agentId_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findAgentIdFirst " + dbName + " : " + "agentId_0 : " + agentId_0);
		Optional<CdEmp> cdEmpT = null;
		if (dbName.equals(ContentName.onDay))
			cdEmpT = cdEmpReposDay.findTopByAgentIdIs(agentId_0);
		else if (dbName.equals(ContentName.onMon))
			cdEmpT = cdEmpReposMon.findTopByAgentIdIs(agentId_0);
		else if (dbName.equals(ContentName.onHist))
			cdEmpT = cdEmpReposHist.findTopByAgentIdIs(agentId_0);
		else
			cdEmpT = cdEmpRepos.findTopByAgentIdIs(agentId_0);

		return cdEmpT.isPresent() ? cdEmpT.get() : null;
	}

	@Override
	public Slice<CdEmp> findCenterCode(String centerCode_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCenterCode " + dbName + " : " + "centerCode_0 : " + centerCode_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByCenterCodeIsOrderByEmployeeNoAsc(centerCode_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByCenterCodeIsOrderByEmployeeNoAsc(centerCode_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByCenterCodeIsOrderByEmployeeNoAsc(centerCode_0, pageable);
		else
			slice = cdEmpRepos.findAllByCenterCodeIsOrderByEmployeeNoAsc(centerCode_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> findFullname(String fullname_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findFullname " + dbName + " : " + "fullname_0 : " + fullname_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByFullnameIsOrderByEmployeeNoAsc(fullname_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByFullnameIsOrderByEmployeeNoAsc(fullname_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByFullnameIsOrderByEmployeeNoAsc(fullname_0, pageable);
		else
			slice = cdEmpRepos.findAllByFullnameIsOrderByEmployeeNoAsc(fullname_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> findFullnameLike(String fullname_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findFullnameLike " + dbName + " : " + "fullname_0 : " + fullname_0);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByFullnameLikeOrderByEmployeeNoAsc(fullname_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByFullnameLikeOrderByEmployeeNoAsc(fullname_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByFullnameLikeOrderByEmployeeNoAsc(fullname_0, pageable);
		else
			slice = cdEmpRepos.findAllByFullnameLikeOrderByEmployeeNoAsc(fullname_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> findCenterCodeAndAgCurInd(String centerCode_0, String agCurInd_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCenterCodeAndAgCurInd " + dbName + " : " + "centerCode_0 : " + centerCode_0 + " agCurInd_1 : " + agCurInd_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByCenterCodeIsAndAgCurIndIsOrderByEmployeeNoAsc(centerCode_0, agCurInd_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByCenterCodeIsAndAgCurIndIsOrderByEmployeeNoAsc(centerCode_0, agCurInd_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByCenterCodeIsAndAgCurIndIsOrderByEmployeeNoAsc(centerCode_0, agCurInd_1, pageable);
		else
			slice = cdEmpRepos.findAllByCenterCodeIsAndAgCurIndIsOrderByEmployeeNoAsc(centerCode_0, agCurInd_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> EmployeeNoLikeAndAgCurInd(String employeeNo_0, String agCurInd_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("EmployeeNoLikeAndAgCurInd " + dbName + " : " + "employeeNo_0 : " + employeeNo_0 + " agCurInd_1 : " + agCurInd_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByEmployeeNoLikeAndAgCurIndIsOrderByEmployeeNoAsc(employeeNo_0, agCurInd_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByEmployeeNoLikeAndAgCurIndIsOrderByEmployeeNoAsc(employeeNo_0, agCurInd_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByEmployeeNoLikeAndAgCurIndIsOrderByEmployeeNoAsc(employeeNo_0, agCurInd_1, pageable);
		else
			slice = cdEmpRepos.findAllByEmployeeNoLikeAndAgCurIndIsOrderByEmployeeNoAsc(employeeNo_0, agCurInd_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> findFullnameLikeAndAgCurInd(String fullname_0, String agCurInd_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findFullnameLikeAndAgCurInd " + dbName + " : " + "fullname_0 : " + fullname_0 + " agCurInd_1 : " + agCurInd_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByFullnameLikeAndAgCurIndIsOrderByEmployeeNoAsc(fullname_0, agCurInd_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByFullnameLikeAndAgCurIndIsOrderByEmployeeNoAsc(fullname_0, agCurInd_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByFullnameLikeAndAgCurIndIsOrderByEmployeeNoAsc(fullname_0, agCurInd_1, pageable);
		else
			slice = cdEmpRepos.findAllByFullnameLikeAndAgCurIndIsOrderByEmployeeNoAsc(fullname_0, agCurInd_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> findEmployeeNoAndAgCurInd(String employeeNo_0, String employeeNo_1, String agCurInd_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findEmployeeNoAndAgCurInd " + dbName + " : " + "employeeNo_0 : " + employeeNo_0 + " employeeNo_1 : " + employeeNo_1 + " agCurInd_2 : " + agCurInd_2);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualAndAgCurIndIsOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, agCurInd_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualAndAgCurIndIsOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, agCurInd_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualAndAgCurIndIsOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, agCurInd_2, pageable);
		else
			slice = cdEmpRepos.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualAndAgCurIndIsOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, agCurInd_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> findCenterCodeAndAgStatusCode(String centerCode_0, String agStatusCode_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCenterCodeAndAgStatusCode " + dbName + " : " + "centerCode_0 : " + centerCode_0 + " agStatusCode_1 : " + agStatusCode_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByCenterCodeIsAndAgStatusCodeIsOrderByEmployeeNoAsc(centerCode_0, agStatusCode_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByCenterCodeIsAndAgStatusCodeIsOrderByEmployeeNoAsc(centerCode_0, agStatusCode_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByCenterCodeIsAndAgStatusCodeIsOrderByEmployeeNoAsc(centerCode_0, agStatusCode_1, pageable);
		else
			slice = cdEmpRepos.findAllByCenterCodeIsAndAgStatusCodeIsOrderByEmployeeNoAsc(centerCode_0, agStatusCode_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> EmployeeNoLikeAndAgStatusCode(String employeeNo_0, String agStatusCode_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("EmployeeNoLikeAndAgStatusCode " + dbName + " : " + "employeeNo_0 : " + employeeNo_0 + " agStatusCode_1 : " + agStatusCode_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByEmployeeNoLikeAndAgStatusCodeIsOrderByEmployeeNoAsc(employeeNo_0, agStatusCode_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByEmployeeNoLikeAndAgStatusCodeIsOrderByEmployeeNoAsc(employeeNo_0, agStatusCode_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByEmployeeNoLikeAndAgStatusCodeIsOrderByEmployeeNoAsc(employeeNo_0, agStatusCode_1, pageable);
		else
			slice = cdEmpRepos.findAllByEmployeeNoLikeAndAgStatusCodeIsOrderByEmployeeNoAsc(employeeNo_0, agStatusCode_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> findFullnameLikeAndAgStatusCode(String fullname_0, String agStatusCode_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findFullnameLikeAndAgStatusCode " + dbName + " : " + "fullname_0 : " + fullname_0 + " agStatusCode_1 : " + agStatusCode_1);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByFullnameLikeAndAgStatusCodeIsOrderByEmployeeNoAsc(fullname_0, agStatusCode_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByFullnameLikeAndAgStatusCodeIsOrderByEmployeeNoAsc(fullname_0, agStatusCode_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByFullnameLikeAndAgStatusCodeIsOrderByEmployeeNoAsc(fullname_0, agStatusCode_1, pageable);
		else
			slice = cdEmpRepos.findAllByFullnameLikeAndAgStatusCodeIsOrderByEmployeeNoAsc(fullname_0, agStatusCode_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<CdEmp> findEmployeeNoAndAgStatusCode(String employeeNo_0, String employeeNo_1, String agStatusCode_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<CdEmp> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findEmployeeNoAndAgStatusCode " + dbName + " : " + "employeeNo_0 : " + employeeNo_0 + " employeeNo_1 : " + employeeNo_1 + " agStatusCode_2 : " + agStatusCode_2);
		if (dbName.equals(ContentName.onDay))
			slice = cdEmpReposDay.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualAndAgStatusCodeIsOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, agStatusCode_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = cdEmpReposMon.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualAndAgStatusCodeIsOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, agStatusCode_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = cdEmpReposHist.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualAndAgStatusCodeIsOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, agStatusCode_2, pageable);
		else
			slice = cdEmpRepos.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualAndAgStatusCodeIsOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, agStatusCode_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public CdEmp holdById(String employeeNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + employeeNo);
		Optional<CdEmp> cdEmp = null;
		if (dbName.equals(ContentName.onDay))
			cdEmp = cdEmpReposDay.findByEmployeeNo(employeeNo);
		else if (dbName.equals(ContentName.onMon))
			cdEmp = cdEmpReposMon.findByEmployeeNo(employeeNo);
		else if (dbName.equals(ContentName.onHist))
			cdEmp = cdEmpReposHist.findByEmployeeNo(employeeNo);
		else
			cdEmp = cdEmpRepos.findByEmployeeNo(employeeNo);
		return cdEmp.isPresent() ? cdEmp.get() : null;
	}

	@Override
	public CdEmp holdById(CdEmp cdEmp, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + cdEmp.getEmployeeNo());
		Optional<CdEmp> cdEmpT = null;
		if (dbName.equals(ContentName.onDay))
			cdEmpT = cdEmpReposDay.findByEmployeeNo(cdEmp.getEmployeeNo());
		else if (dbName.equals(ContentName.onMon))
			cdEmpT = cdEmpReposMon.findByEmployeeNo(cdEmp.getEmployeeNo());
		else if (dbName.equals(ContentName.onHist))
			cdEmpT = cdEmpReposHist.findByEmployeeNo(cdEmp.getEmployeeNo());
		else
			cdEmpT = cdEmpRepos.findByEmployeeNo(cdEmp.getEmployeeNo());
		return cdEmpT.isPresent() ? cdEmpT.get() : null;
	}

	@Override
	public CdEmp insert(CdEmp cdEmp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + cdEmp.getEmployeeNo());
		if (this.findById(cdEmp.getEmployeeNo(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			cdEmp.setCreateEmpNo(empNot);

		if (cdEmp.getLastUpdateEmpNo() == null || cdEmp.getLastUpdateEmpNo().isEmpty())
			cdEmp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdEmpReposDay.saveAndFlush(cdEmp);
		else if (dbName.equals(ContentName.onMon))
			return cdEmpReposMon.saveAndFlush(cdEmp);
		else if (dbName.equals(ContentName.onHist))
			return cdEmpReposHist.saveAndFlush(cdEmp);
		else
			return cdEmpRepos.saveAndFlush(cdEmp);
	}

	@Override
	public CdEmp update(CdEmp cdEmp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdEmp.getEmployeeNo());
		if (!empNot.isEmpty())
			cdEmp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return cdEmpReposDay.saveAndFlush(cdEmp);
		else if (dbName.equals(ContentName.onMon))
			return cdEmpReposMon.saveAndFlush(cdEmp);
		else if (dbName.equals(ContentName.onHist))
			return cdEmpReposHist.saveAndFlush(cdEmp);
		else
			return cdEmpRepos.saveAndFlush(cdEmp);
	}

	@Override
	public CdEmp update2(CdEmp cdEmp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + cdEmp.getEmployeeNo());
		if (!empNot.isEmpty())
			cdEmp.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			cdEmpReposDay.saveAndFlush(cdEmp);
		else if (dbName.equals(ContentName.onMon))
			cdEmpReposMon.saveAndFlush(cdEmp);
		else if (dbName.equals(ContentName.onHist))
			cdEmpReposHist.saveAndFlush(cdEmp);
		else
			cdEmpRepos.saveAndFlush(cdEmp);
		return this.findById(cdEmp.getEmployeeNo());
	}

	@Override
	public void delete(CdEmp cdEmp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + cdEmp.getEmployeeNo());
		if (dbName.equals(ContentName.onDay)) {
			cdEmpReposDay.delete(cdEmp);
			cdEmpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdEmpReposMon.delete(cdEmp);
			cdEmpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdEmpReposHist.delete(cdEmp);
			cdEmpReposHist.flush();
		} else {
			cdEmpRepos.delete(cdEmp);
			cdEmpRepos.flush();
		}
	}

	@Override
	public void insertAll(List<CdEmp> cdEmp, TitaVo... titaVo) throws DBException {
		if (cdEmp == null || cdEmp.size() == 0)
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
		for (CdEmp t : cdEmp) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			cdEmp = cdEmpReposDay.saveAll(cdEmp);
			cdEmpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdEmp = cdEmpReposMon.saveAll(cdEmp);
			cdEmpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdEmp = cdEmpReposHist.saveAll(cdEmp);
			cdEmpReposHist.flush();
		} else {
			cdEmp = cdEmpRepos.saveAll(cdEmp);
			cdEmpRepos.flush();
		}
	}

	@Override
	public void updateAll(List<CdEmp> cdEmp, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (cdEmp == null || cdEmp.size() == 0)
			throw new DBException(6);

		for (CdEmp t : cdEmp)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			cdEmp = cdEmpReposDay.saveAll(cdEmp);
			cdEmpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdEmp = cdEmpReposMon.saveAll(cdEmp);
			cdEmpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdEmp = cdEmpReposHist.saveAll(cdEmp);
			cdEmpReposHist.flush();
		} else {
			cdEmp = cdEmpRepos.saveAll(cdEmp);
			cdEmpRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<CdEmp> cdEmp, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (cdEmp == null || cdEmp.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			cdEmpReposDay.deleteAll(cdEmp);
			cdEmpReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			cdEmpReposMon.deleteAll(cdEmp);
			cdEmpReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			cdEmpReposHist.deleteAll(cdEmp);
			cdEmpReposHist.flush();
		} else {
			cdEmpRepos.deleteAll(cdEmp);
			cdEmpRepos.flush();
		}
	}

}
