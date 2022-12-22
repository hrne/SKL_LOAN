package com.st1.itx.db.service.springjpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

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
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.repository.online.CdBcmRepository;
import com.st1.itx.db.repository.day.CdBcmRepositoryDay;
import com.st1.itx.db.repository.mon.CdBcmRepositoryMon;
import com.st1.itx.db.repository.hist.CdBcmRepositoryHist;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdBcmService")
@Repository
public class CdBcmServiceImpl extends ASpringJpaParm implements CdBcmService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdBcmRepository cdBcmRepos;

  @Autowired
  private CdBcmRepositoryDay cdBcmReposDay;

  @Autowired
  private CdBcmRepositoryMon cdBcmReposMon;

  @Autowired
  private CdBcmRepositoryHist cdBcmReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdBcmRepos);
    org.junit.Assert.assertNotNull(cdBcmReposDay);
    org.junit.Assert.assertNotNull(cdBcmReposMon);
    org.junit.Assert.assertNotNull(cdBcmReposHist);
  }

  @Override
  public CdBcm findById(String unitCode, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + unitCode);
    Optional<CdBcm> cdBcm = null;
    if (dbName.equals(ContentName.onDay))
      cdBcm = cdBcmReposDay.findById(unitCode);
    else if (dbName.equals(ContentName.onMon))
      cdBcm = cdBcmReposMon.findById(unitCode);
    else if (dbName.equals(ContentName.onHist))
      cdBcm = cdBcmReposHist.findById(unitCode);
    else 
      cdBcm = cdBcmRepos.findById(unitCode);
    CdBcm obj = cdBcm.isPresent() ? cdBcm.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdBcm> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBcm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "UnitCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "UnitCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdBcmReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBcmReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBcmReposHist.findAll(pageable);
    else 
      slice = cdBcmRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBcm> findDeptCode(String deptCode_0, String deptCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBcm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findDeptCode " + dbName + " : " + "deptCode_0 : " + deptCode_0 + " deptCode_1 : " +  deptCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdBcmReposDay.findAllByDeptCodeGreaterThanEqualAndDeptCodeLessThanEqualOrderByDeptCodeAsc(deptCode_0, deptCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBcmReposMon.findAllByDeptCodeGreaterThanEqualAndDeptCodeLessThanEqualOrderByDeptCodeAsc(deptCode_0, deptCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBcmReposHist.findAllByDeptCodeGreaterThanEqualAndDeptCodeLessThanEqualOrderByDeptCodeAsc(deptCode_0, deptCode_1, pageable);
    else 
      slice = cdBcmRepos.findAllByDeptCodeGreaterThanEqualAndDeptCodeLessThanEqualOrderByDeptCodeAsc(deptCode_0, deptCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBcm> findDistCode(String distCode_0, String distCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBcm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findDistCode " + dbName + " : " + "distCode_0 : " + distCode_0 + " distCode_1 : " +  distCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdBcmReposDay.findAllByDistCodeGreaterThanEqualAndDistCodeLessThanEqualOrderByDistCodeAsc(distCode_0, distCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBcmReposMon.findAllByDistCodeGreaterThanEqualAndDistCodeLessThanEqualOrderByDistCodeAsc(distCode_0, distCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBcmReposHist.findAllByDistCodeGreaterThanEqualAndDistCodeLessThanEqualOrderByDistCodeAsc(distCode_0, distCode_1, pageable);
    else 
      slice = cdBcmRepos.findAllByDistCodeGreaterThanEqualAndDistCodeLessThanEqualOrderByDistCodeAsc(distCode_0, distCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBcm> findUnitCode(String unitCode_0, String unitCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBcm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findUnitCode " + dbName + " : " + "unitCode_0 : " + unitCode_0 + " unitCode_1 : " +  unitCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdBcmReposDay.findAllByUnitCodeGreaterThanEqualAndUnitCodeLessThanEqualOrderByUnitCodeAsc(unitCode_0, unitCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBcmReposMon.findAllByUnitCodeGreaterThanEqualAndUnitCodeLessThanEqualOrderByUnitCodeAsc(unitCode_0, unitCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBcmReposHist.findAllByUnitCodeGreaterThanEqualAndUnitCodeLessThanEqualOrderByUnitCodeAsc(unitCode_0, unitCode_1, pageable);
    else 
      slice = cdBcmRepos.findAllByUnitCodeGreaterThanEqualAndUnitCodeLessThanEqualOrderByUnitCodeAsc(unitCode_0, unitCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdBcm deptCodeFirst(String deptCode_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("deptCodeFirst " + dbName + " : " + "deptCode_0 : " + deptCode_0);
    Optional<CdBcm> cdBcmT = null;
    if (dbName.equals(ContentName.onDay))
      cdBcmT = cdBcmReposDay.findTopByDeptCodeIsOrderByUnitCodeAsc(deptCode_0);
    else if (dbName.equals(ContentName.onMon))
      cdBcmT = cdBcmReposMon.findTopByDeptCodeIsOrderByUnitCodeAsc(deptCode_0);
    else if (dbName.equals(ContentName.onHist))
      cdBcmT = cdBcmReposHist.findTopByDeptCodeIsOrderByUnitCodeAsc(deptCode_0);
    else 
      cdBcmT = cdBcmRepos.findTopByDeptCodeIsOrderByUnitCodeAsc(deptCode_0);

    return cdBcmT.isPresent() ? cdBcmT.get() : null;
  }

  @Override
  public CdBcm distCodeFirst(String distCode_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("distCodeFirst " + dbName + " : " + "distCode_0 : " + distCode_0);
    Optional<CdBcm> cdBcmT = null;
    if (dbName.equals(ContentName.onDay))
      cdBcmT = cdBcmReposDay.findTopByDistCodeIsOrderByUnitCodeAsc(distCode_0);
    else if (dbName.equals(ContentName.onMon))
      cdBcmT = cdBcmReposMon.findTopByDistCodeIsOrderByUnitCodeAsc(distCode_0);
    else if (dbName.equals(ContentName.onHist))
      cdBcmT = cdBcmReposHist.findTopByDistCodeIsOrderByUnitCodeAsc(distCode_0);
    else 
      cdBcmT = cdBcmRepos.findTopByDistCodeIsOrderByUnitCodeAsc(distCode_0);

    return cdBcmT.isPresent() ? cdBcmT.get() : null;
  }

  @Override
  public Slice<CdBcm> findUnitManager(String unitManager_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBcm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findUnitManager " + dbName + " : " + "unitManager_0 : " + unitManager_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdBcmReposDay.findAllByUnitManagerLikeOrderByUnitManagerAsc(unitManager_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBcmReposMon.findAllByUnitManagerLikeOrderByUnitManagerAsc(unitManager_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBcmReposHist.findAllByUnitManagerLikeOrderByUnitManagerAsc(unitManager_0, pageable);
    else 
      slice = cdBcmRepos.findAllByUnitManagerLikeOrderByUnitManagerAsc(unitManager_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBcm> findDeptManager(String deptManager_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBcm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findDeptManager " + dbName + " : " + "deptManager_0 : " + deptManager_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdBcmReposDay.findAllByDeptManagerLikeOrderByDeptManagerAsc(deptManager_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBcmReposMon.findAllByDeptManagerLikeOrderByDeptManagerAsc(deptManager_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBcmReposHist.findAllByDeptManagerLikeOrderByDeptManagerAsc(deptManager_0, pageable);
    else 
      slice = cdBcmRepos.findAllByDeptManagerLikeOrderByDeptManagerAsc(deptManager_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBcm> findDistManager(String distManager_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBcm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findDistManager " + dbName + " : " + "distManager_0 : " + distManager_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdBcmReposDay.findAllByDistManagerLikeOrderByDistManagerAsc(distManager_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBcmReposMon.findAllByDistManagerLikeOrderByDistManagerAsc(distManager_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBcmReposHist.findAllByDistManagerLikeOrderByDistManagerAsc(distManager_0, pageable);
    else 
      slice = cdBcmRepos.findAllByDistManagerLikeOrderByDistManagerAsc(distManager_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBcm> findDeptCode1(String deptCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBcm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findDeptCode1 " + dbName + " : " + "deptCode_0 : " + deptCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdBcmReposDay.findAllByDeptCodeLikeOrderByDeptCodeAsc(deptCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBcmReposMon.findAllByDeptCodeLikeOrderByDeptCodeAsc(deptCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBcmReposHist.findAllByDeptCodeLikeOrderByDeptCodeAsc(deptCode_0, pageable);
    else 
      slice = cdBcmRepos.findAllByDeptCodeLikeOrderByDeptCodeAsc(deptCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBcm> findDistCode1(String distCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBcm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findDistCode1 " + dbName + " : " + "distCode_0 : " + distCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdBcmReposDay.findAllByDistCodeLikeOrderByDistCodeAsc(distCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBcmReposMon.findAllByDistCodeLikeOrderByDistCodeAsc(distCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBcmReposHist.findAllByDistCodeLikeOrderByDistCodeAsc(distCode_0, pageable);
    else 
      slice = cdBcmRepos.findAllByDistCodeLikeOrderByDistCodeAsc(distCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdBcm> findUnitCode1(String unitCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdBcm> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findUnitCode1 " + dbName + " : " + "unitCode_0 : " + unitCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdBcmReposDay.findAllByUnitCodeLikeOrderByUnitCodeAsc(unitCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdBcmReposMon.findAllByUnitCodeLikeOrderByUnitCodeAsc(unitCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdBcmReposHist.findAllByUnitCodeLikeOrderByUnitCodeAsc(unitCode_0, pageable);
    else 
      slice = cdBcmRepos.findAllByUnitCodeLikeOrderByUnitCodeAsc(unitCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdBcm distItemFirst(String distItem_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("distItemFirst " + dbName + " : " + "distItem_0 : " + distItem_0);
    Optional<CdBcm> cdBcmT = null;
    if (dbName.equals(ContentName.onDay))
      cdBcmT = cdBcmReposDay.findTopByDistItemLikeOrderByUnitCodeAsc(distItem_0);
    else if (dbName.equals(ContentName.onMon))
      cdBcmT = cdBcmReposMon.findTopByDistItemLikeOrderByUnitCodeAsc(distItem_0);
    else if (dbName.equals(ContentName.onHist))
      cdBcmT = cdBcmReposHist.findTopByDistItemLikeOrderByUnitCodeAsc(distItem_0);
    else 
      cdBcmT = cdBcmRepos.findTopByDistItemLikeOrderByUnitCodeAsc(distItem_0);

    return cdBcmT.isPresent() ? cdBcmT.get() : null;
  }

  @Override
  public CdBcm holdById(String unitCode, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + unitCode);
    Optional<CdBcm> cdBcm = null;
    if (dbName.equals(ContentName.onDay))
      cdBcm = cdBcmReposDay.findByUnitCode(unitCode);
    else if (dbName.equals(ContentName.onMon))
      cdBcm = cdBcmReposMon.findByUnitCode(unitCode);
    else if (dbName.equals(ContentName.onHist))
      cdBcm = cdBcmReposHist.findByUnitCode(unitCode);
    else 
      cdBcm = cdBcmRepos.findByUnitCode(unitCode);
    return cdBcm.isPresent() ? cdBcm.get() : null;
  }

  @Override
  public CdBcm holdById(CdBcm cdBcm, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdBcm.getUnitCode());
    Optional<CdBcm> cdBcmT = null;
    if (dbName.equals(ContentName.onDay))
      cdBcmT = cdBcmReposDay.findByUnitCode(cdBcm.getUnitCode());
    else if (dbName.equals(ContentName.onMon))
      cdBcmT = cdBcmReposMon.findByUnitCode(cdBcm.getUnitCode());
    else if (dbName.equals(ContentName.onHist))
      cdBcmT = cdBcmReposHist.findByUnitCode(cdBcm.getUnitCode());
    else 
      cdBcmT = cdBcmRepos.findByUnitCode(cdBcm.getUnitCode());
    return cdBcmT.isPresent() ? cdBcmT.get() : null;
  }

  @Override
  public CdBcm insert(CdBcm cdBcm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + cdBcm.getUnitCode());
    if (this.findById(cdBcm.getUnitCode(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdBcm.setCreateEmpNo(empNot);

    if(cdBcm.getLastUpdateEmpNo() == null || cdBcm.getLastUpdateEmpNo().isEmpty())
      cdBcm.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBcmReposDay.saveAndFlush(cdBcm);	
    else if (dbName.equals(ContentName.onMon))
      return cdBcmReposMon.saveAndFlush(cdBcm);
    else if (dbName.equals(ContentName.onHist))
      return cdBcmReposHist.saveAndFlush(cdBcm);
    else 
    return cdBcmRepos.saveAndFlush(cdBcm);
  }

  @Override
  public CdBcm update(CdBcm cdBcm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdBcm.getUnitCode());
    if (!empNot.isEmpty())
      cdBcm.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdBcmReposDay.saveAndFlush(cdBcm);	
    else if (dbName.equals(ContentName.onMon))
      return cdBcmReposMon.saveAndFlush(cdBcm);
    else if (dbName.equals(ContentName.onHist))
      return cdBcmReposHist.saveAndFlush(cdBcm);
    else 
    return cdBcmRepos.saveAndFlush(cdBcm);
  }

  @Override
  public CdBcm update2(CdBcm cdBcm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + cdBcm.getUnitCode());
    if (!empNot.isEmpty())
      cdBcm.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdBcmReposDay.saveAndFlush(cdBcm);	
    else if (dbName.equals(ContentName.onMon))
      cdBcmReposMon.saveAndFlush(cdBcm);
    else if (dbName.equals(ContentName.onHist))
        cdBcmReposHist.saveAndFlush(cdBcm);
    else 
      cdBcmRepos.saveAndFlush(cdBcm);	
    return this.findById(cdBcm.getUnitCode());
  }

  @Override
  public void delete(CdBcm cdBcm, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdBcm.getUnitCode());
    if (dbName.equals(ContentName.onDay)) {
      cdBcmReposDay.delete(cdBcm);	
      cdBcmReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBcmReposMon.delete(cdBcm);	
      cdBcmReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBcmReposHist.delete(cdBcm);
      cdBcmReposHist.flush();
    }
    else {
      cdBcmRepos.delete(cdBcm);
      cdBcmRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdBcm> cdBcm, TitaVo... titaVo) throws DBException {
    if (cdBcm == null || cdBcm.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CdBcm t : cdBcm){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdBcm = cdBcmReposDay.saveAll(cdBcm);	
      cdBcmReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBcm = cdBcmReposMon.saveAll(cdBcm);	
      cdBcmReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBcm = cdBcmReposHist.saveAll(cdBcm);
      cdBcmReposHist.flush();
    }
    else {
      cdBcm = cdBcmRepos.saveAll(cdBcm);
      cdBcmRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdBcm> cdBcm, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (cdBcm == null || cdBcm.size() == 0)
      throw new DBException(6);

    for (CdBcm t : cdBcm) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdBcm = cdBcmReposDay.saveAll(cdBcm);	
      cdBcmReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBcm = cdBcmReposMon.saveAll(cdBcm);	
      cdBcmReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBcm = cdBcmReposHist.saveAll(cdBcm);
      cdBcmReposHist.flush();
    }
    else {
      cdBcm = cdBcmRepos.saveAll(cdBcm);
      cdBcmRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdBcm> cdBcm, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdBcm == null || cdBcm.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdBcmReposDay.deleteAll(cdBcm);	
      cdBcmReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdBcmReposMon.deleteAll(cdBcm);	
      cdBcmReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdBcmReposHist.deleteAll(cdBcm);
      cdBcmReposHist.flush();
    }
    else {
      cdBcmRepos.deleteAll(cdBcm);
      cdBcmRepos.flush();
    }
  }

}
