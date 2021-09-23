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
import com.st1.itx.db.domain.PfCoOfficer;
import com.st1.itx.db.domain.PfCoOfficerId;
import com.st1.itx.db.repository.online.PfCoOfficerRepository;
import com.st1.itx.db.repository.day.PfCoOfficerRepositoryDay;
import com.st1.itx.db.repository.mon.PfCoOfficerRepositoryMon;
import com.st1.itx.db.repository.hist.PfCoOfficerRepositoryHist;
import com.st1.itx.db.service.PfCoOfficerService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfCoOfficerService")
@Repository
public class PfCoOfficerServiceImpl extends ASpringJpaParm implements PfCoOfficerService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private PfCoOfficerRepository pfCoOfficerRepos;

  @Autowired
  private PfCoOfficerRepositoryDay pfCoOfficerReposDay;

  @Autowired
  private PfCoOfficerRepositoryMon pfCoOfficerReposMon;

  @Autowired
  private PfCoOfficerRepositoryHist pfCoOfficerReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(pfCoOfficerRepos);
    org.junit.Assert.assertNotNull(pfCoOfficerReposDay);
    org.junit.Assert.assertNotNull(pfCoOfficerReposMon);
    org.junit.Assert.assertNotNull(pfCoOfficerReposHist);
  }

  @Override
  public PfCoOfficer findById(PfCoOfficerId pfCoOfficerId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + pfCoOfficerId);
    Optional<PfCoOfficer> pfCoOfficer = null;
    if (dbName.equals(ContentName.onDay))
      pfCoOfficer = pfCoOfficerReposDay.findById(pfCoOfficerId);
    else if (dbName.equals(ContentName.onMon))
      pfCoOfficer = pfCoOfficerReposMon.findById(pfCoOfficerId);
    else if (dbName.equals(ContentName.onHist))
      pfCoOfficer = pfCoOfficerReposHist.findById(pfCoOfficerId);
    else 
      pfCoOfficer = pfCoOfficerRepos.findById(pfCoOfficerId);
    PfCoOfficer obj = pfCoOfficer.isPresent() ? pfCoOfficer.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<PfCoOfficer> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfCoOfficer> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "EmpNo", "EffectiveDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EmpNo", "EffectiveDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = pfCoOfficerReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfCoOfficerReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfCoOfficerReposHist.findAll(pageable);
    else 
      slice = pfCoOfficerRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfCoOfficer effectiveDateFirst(String empNo_0, int effectiveDate_1, int effectiveDate_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("effectiveDateFirst " + dbName + " : " + "empNo_0 : " + empNo_0 + " effectiveDate_1 : " +  effectiveDate_1 + " effectiveDate_2 : " +  effectiveDate_2);
    Optional<PfCoOfficer> pfCoOfficerT = null;
    if (dbName.equals(ContentName.onDay))
      pfCoOfficerT = pfCoOfficerReposDay.findTopByEmpNoIsAndEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(empNo_0, effectiveDate_1, effectiveDate_2);
    else if (dbName.equals(ContentName.onMon))
      pfCoOfficerT = pfCoOfficerReposMon.findTopByEmpNoIsAndEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(empNo_0, effectiveDate_1, effectiveDate_2);
    else if (dbName.equals(ContentName.onHist))
      pfCoOfficerT = pfCoOfficerReposHist.findTopByEmpNoIsAndEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(empNo_0, effectiveDate_1, effectiveDate_2);
    else 
      pfCoOfficerT = pfCoOfficerRepos.findTopByEmpNoIsAndEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(empNo_0, effectiveDate_1, effectiveDate_2);

    return pfCoOfficerT.isPresent() ? pfCoOfficerT.get() : null;
  }

  @Override
  public Slice<PfCoOfficer> findByEmpNo(String empNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfCoOfficer> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByEmpNo " + dbName + " : " + "empNo_0 : " + empNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = pfCoOfficerReposDay.findAllByEmpNoIsOrderByEffectiveDateDesc(empNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfCoOfficerReposMon.findAllByEmpNoIsOrderByEffectiveDateDesc(empNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfCoOfficerReposHist.findAllByEmpNoIsOrderByEffectiveDateDesc(empNo_0, pageable);
    else 
      slice = pfCoOfficerRepos.findAllByEmpNoIsOrderByEffectiveDateDesc(empNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfCoOfficer findByEmpNoFirst(String empNo_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findByEmpNoFirst " + dbName + " : " + "empNo_0 : " + empNo_0);
    Optional<PfCoOfficer> pfCoOfficerT = null;
    if (dbName.equals(ContentName.onDay))
      pfCoOfficerT = pfCoOfficerReposDay.findTopByEmpNoIsOrderByEffectiveDateDesc(empNo_0);
    else if (dbName.equals(ContentName.onMon))
      pfCoOfficerT = pfCoOfficerReposMon.findTopByEmpNoIsOrderByEffectiveDateDesc(empNo_0);
    else if (dbName.equals(ContentName.onHist))
      pfCoOfficerT = pfCoOfficerReposHist.findTopByEmpNoIsOrderByEffectiveDateDesc(empNo_0);
    else 
      pfCoOfficerT = pfCoOfficerRepos.findTopByEmpNoIsOrderByEffectiveDateDesc(empNo_0);

    return pfCoOfficerT.isPresent() ? pfCoOfficerT.get() : null;
  }

  @Override
  public Slice<PfCoOfficer> findByEffectiveDateDate(int effectiveDate_0, int effectiveDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfCoOfficer> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByEffectiveDateDate " + dbName + " : " + "effectiveDate_0 : " + effectiveDate_0 + " effectiveDate_1 : " +  effectiveDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfCoOfficerReposDay.findAllByEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEmpNoAscEffectiveDateDesc(effectiveDate_0, effectiveDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfCoOfficerReposMon.findAllByEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEmpNoAscEffectiveDateDesc(effectiveDate_0, effectiveDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfCoOfficerReposHist.findAllByEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEmpNoAscEffectiveDateDesc(effectiveDate_0, effectiveDate_1, pageable);
    else 
      slice = pfCoOfficerRepos.findAllByEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEmpNoAscEffectiveDateDesc(effectiveDate_0, effectiveDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfCoOfficer> effectiveDateEq(String empNo_0, int effectiveDate_1, int effectiveDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfCoOfficer> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("effectiveDateEq " + dbName + " : " + "empNo_0 : " + empNo_0 + " effectiveDate_1 : " +  effectiveDate_1 + " effectiveDate_2 : " +  effectiveDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = pfCoOfficerReposDay.findAllByEmpNoIsAndEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(empNo_0, effectiveDate_1, effectiveDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfCoOfficerReposMon.findAllByEmpNoIsAndEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(empNo_0, effectiveDate_1, effectiveDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfCoOfficerReposHist.findAllByEmpNoIsAndEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(empNo_0, effectiveDate_1, effectiveDate_2, pageable);
    else 
      slice = pfCoOfficerRepos.findAllByEmpNoIsAndEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(empNo_0, effectiveDate_1, effectiveDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfCoOfficer> findNotYet(int effectiveDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfCoOfficer> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findNotYet " + dbName + " : " + "effectiveDate_0 : " + effectiveDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = pfCoOfficerReposDay.findAllByEffectiveDateGreaterThanOrderByEffectiveDateDesc(effectiveDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfCoOfficerReposMon.findAllByEffectiveDateGreaterThanOrderByEffectiveDateDesc(effectiveDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfCoOfficerReposHist.findAllByEffectiveDateGreaterThanOrderByEffectiveDateDesc(effectiveDate_0, pageable);
    else 
      slice = pfCoOfficerRepos.findAllByEffectiveDateGreaterThanOrderByEffectiveDateDesc(effectiveDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfCoOfficer> findOutOf(int ineffectiveDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfCoOfficer> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findOutOf " + dbName + " : " + "ineffectiveDate_0 : " + ineffectiveDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = pfCoOfficerReposDay.findAllByIneffectiveDateLessThanOrderByEffectiveDateDesc(ineffectiveDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfCoOfficerReposMon.findAllByIneffectiveDateLessThanOrderByEffectiveDateDesc(ineffectiveDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfCoOfficerReposHist.findAllByIneffectiveDateLessThanOrderByEffectiveDateDesc(ineffectiveDate_0, pageable);
    else 
      slice = pfCoOfficerRepos.findAllByIneffectiveDateLessThanOrderByEffectiveDateDesc(ineffectiveDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfCoOfficer> findIng(int effectiveDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfCoOfficer> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findIng " + dbName + " : " + "effectiveDate_0 : " + effectiveDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = pfCoOfficerReposDay.findAllByEffectiveDateGreaterThanEqualOrderByEffectiveDateDesc(effectiveDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfCoOfficerReposMon.findAllByEffectiveDateGreaterThanEqualOrderByEffectiveDateDesc(effectiveDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfCoOfficerReposHist.findAllByEffectiveDateGreaterThanEqualOrderByEffectiveDateDesc(effectiveDate_0, pageable);
    else 
      slice = pfCoOfficerRepos.findAllByEffectiveDateGreaterThanEqualOrderByEffectiveDateDesc(effectiveDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfCoOfficer holdById(PfCoOfficerId pfCoOfficerId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfCoOfficerId);
    Optional<PfCoOfficer> pfCoOfficer = null;
    if (dbName.equals(ContentName.onDay))
      pfCoOfficer = pfCoOfficerReposDay.findByPfCoOfficerId(pfCoOfficerId);
    else if (dbName.equals(ContentName.onMon))
      pfCoOfficer = pfCoOfficerReposMon.findByPfCoOfficerId(pfCoOfficerId);
    else if (dbName.equals(ContentName.onHist))
      pfCoOfficer = pfCoOfficerReposHist.findByPfCoOfficerId(pfCoOfficerId);
    else 
      pfCoOfficer = pfCoOfficerRepos.findByPfCoOfficerId(pfCoOfficerId);
    return pfCoOfficer.isPresent() ? pfCoOfficer.get() : null;
  }

  @Override
  public PfCoOfficer holdById(PfCoOfficer pfCoOfficer, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfCoOfficer.getPfCoOfficerId());
    Optional<PfCoOfficer> pfCoOfficerT = null;
    if (dbName.equals(ContentName.onDay))
      pfCoOfficerT = pfCoOfficerReposDay.findByPfCoOfficerId(pfCoOfficer.getPfCoOfficerId());
    else if (dbName.equals(ContentName.onMon))
      pfCoOfficerT = pfCoOfficerReposMon.findByPfCoOfficerId(pfCoOfficer.getPfCoOfficerId());
    else if (dbName.equals(ContentName.onHist))
      pfCoOfficerT = pfCoOfficerReposHist.findByPfCoOfficerId(pfCoOfficer.getPfCoOfficerId());
    else 
      pfCoOfficerT = pfCoOfficerRepos.findByPfCoOfficerId(pfCoOfficer.getPfCoOfficerId());
    return pfCoOfficerT.isPresent() ? pfCoOfficerT.get() : null;
  }

  @Override
  public PfCoOfficer insert(PfCoOfficer pfCoOfficer, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + pfCoOfficer.getPfCoOfficerId());
    if (this.findById(pfCoOfficer.getPfCoOfficerId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      pfCoOfficer.setCreateEmpNo(empNot);

    if(pfCoOfficer.getLastUpdateEmpNo() == null || pfCoOfficer.getLastUpdateEmpNo().isEmpty())
      pfCoOfficer.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfCoOfficerReposDay.saveAndFlush(pfCoOfficer);	
    else if (dbName.equals(ContentName.onMon))
      return pfCoOfficerReposMon.saveAndFlush(pfCoOfficer);
    else if (dbName.equals(ContentName.onHist))
      return pfCoOfficerReposHist.saveAndFlush(pfCoOfficer);
    else 
    return pfCoOfficerRepos.saveAndFlush(pfCoOfficer);
  }

  @Override
  public PfCoOfficer update(PfCoOfficer pfCoOfficer, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + pfCoOfficer.getPfCoOfficerId());
    if (!empNot.isEmpty())
      pfCoOfficer.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfCoOfficerReposDay.saveAndFlush(pfCoOfficer);	
    else if (dbName.equals(ContentName.onMon))
      return pfCoOfficerReposMon.saveAndFlush(pfCoOfficer);
    else if (dbName.equals(ContentName.onHist))
      return pfCoOfficerReposHist.saveAndFlush(pfCoOfficer);
    else 
    return pfCoOfficerRepos.saveAndFlush(pfCoOfficer);
  }

  @Override
  public PfCoOfficer update2(PfCoOfficer pfCoOfficer, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + pfCoOfficer.getPfCoOfficerId());
    if (!empNot.isEmpty())
      pfCoOfficer.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      pfCoOfficerReposDay.saveAndFlush(pfCoOfficer);	
    else if (dbName.equals(ContentName.onMon))
      pfCoOfficerReposMon.saveAndFlush(pfCoOfficer);
    else if (dbName.equals(ContentName.onHist))
        pfCoOfficerReposHist.saveAndFlush(pfCoOfficer);
    else 
      pfCoOfficerRepos.saveAndFlush(pfCoOfficer);	
    return this.findById(pfCoOfficer.getPfCoOfficerId());
  }

  @Override
  public void delete(PfCoOfficer pfCoOfficer, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + pfCoOfficer.getPfCoOfficerId());
    if (dbName.equals(ContentName.onDay)) {
      pfCoOfficerReposDay.delete(pfCoOfficer);	
      pfCoOfficerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfCoOfficerReposMon.delete(pfCoOfficer);	
      pfCoOfficerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfCoOfficerReposHist.delete(pfCoOfficer);
      pfCoOfficerReposHist.flush();
    }
    else {
      pfCoOfficerRepos.delete(pfCoOfficer);
      pfCoOfficerRepos.flush();
    }
   }

  @Override
  public void insertAll(List<PfCoOfficer> pfCoOfficer, TitaVo... titaVo) throws DBException {
    if (pfCoOfficer == null || pfCoOfficer.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (PfCoOfficer t : pfCoOfficer){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      pfCoOfficer = pfCoOfficerReposDay.saveAll(pfCoOfficer);	
      pfCoOfficerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfCoOfficer = pfCoOfficerReposMon.saveAll(pfCoOfficer);	
      pfCoOfficerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfCoOfficer = pfCoOfficerReposHist.saveAll(pfCoOfficer);
      pfCoOfficerReposHist.flush();
    }
    else {
      pfCoOfficer = pfCoOfficerRepos.saveAll(pfCoOfficer);
      pfCoOfficerRepos.flush();
    }
    }

  @Override
  public void updateAll(List<PfCoOfficer> pfCoOfficer, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (pfCoOfficer == null || pfCoOfficer.size() == 0)
      throw new DBException(6);

    for (PfCoOfficer t : pfCoOfficer) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      pfCoOfficer = pfCoOfficerReposDay.saveAll(pfCoOfficer);	
      pfCoOfficerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfCoOfficer = pfCoOfficerReposMon.saveAll(pfCoOfficer);	
      pfCoOfficerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfCoOfficer = pfCoOfficerReposHist.saveAll(pfCoOfficer);
      pfCoOfficerReposHist.flush();
    }
    else {
      pfCoOfficer = pfCoOfficerRepos.saveAll(pfCoOfficer);
      pfCoOfficerRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<PfCoOfficer> pfCoOfficer, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (pfCoOfficer == null || pfCoOfficer.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      pfCoOfficerReposDay.deleteAll(pfCoOfficer);	
      pfCoOfficerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfCoOfficerReposMon.deleteAll(pfCoOfficer);	
      pfCoOfficerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfCoOfficerReposHist.deleteAll(pfCoOfficer);
      pfCoOfficerReposHist.flush();
    }
    else {
      pfCoOfficerRepos.deleteAll(pfCoOfficer);
      pfCoOfficerRepos.flush();
    }
  }

}
