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
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ570Id;
import com.st1.itx.db.repository.online.JcicZ570Repository;
import com.st1.itx.db.repository.day.JcicZ570RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ570RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ570RepositoryHist;
import com.st1.itx.db.service.JcicZ570Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ570Service")
@Repository
public class JcicZ570ServiceImpl extends ASpringJpaParm implements JcicZ570Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ570Repository jcicZ570Repos;

  @Autowired
  private JcicZ570RepositoryDay jcicZ570ReposDay;

  @Autowired
  private JcicZ570RepositoryMon jcicZ570ReposMon;

  @Autowired
  private JcicZ570RepositoryHist jcicZ570ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ570Repos);
    org.junit.Assert.assertNotNull(jcicZ570ReposDay);
    org.junit.Assert.assertNotNull(jcicZ570ReposMon);
    org.junit.Assert.assertNotNull(jcicZ570ReposHist);
  }

  @Override
  public JcicZ570 findById(JcicZ570Id jcicZ570Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ570Id);
    Optional<JcicZ570> jcicZ570 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ570 = jcicZ570ReposDay.findById(jcicZ570Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ570 = jcicZ570ReposMon.findById(jcicZ570Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ570 = jcicZ570ReposHist.findById(jcicZ570Id);
    else 
      jcicZ570 = jcicZ570Repos.findById(jcicZ570Id);
    JcicZ570 obj = jcicZ570.isPresent() ? jcicZ570.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ570> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAll(pageable);
    else 
      slice = jcicZ570Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ570Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> otherEq(String custId_0, int applyDate_1, String submitKey_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1 + " submitKey_2 : " +  submitKey_2);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ570 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ570> jcicZ570T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ570T = jcicZ570ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ570T = jcicZ570ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ570T = jcicZ570ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ570T = jcicZ570Repos.findTopByUkeyIs(ukey_0);

    return jcicZ570T.isPresent() ? jcicZ570T.get() : null;
  }

  @Override
  public JcicZ570 otherFirst(String custId_0, int applyDate_1, String submitKey_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1 + " submitKey_2 : " +  submitKey_2);
    Optional<JcicZ570> jcicZ570T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ570T = jcicZ570ReposDay.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2);
    else if (dbName.equals(ContentName.onMon))
      jcicZ570T = jcicZ570ReposMon.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2);
    else if (dbName.equals(ContentName.onHist))
      jcicZ570T = jcicZ570ReposHist.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2);
    else 
      jcicZ570T = jcicZ570Repos.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2);

    return jcicZ570T.isPresent() ? jcicZ570T.get() : null;
  }

  @Override
  public Slice<JcicZ570> findByBank1(String custId_0, String bank1_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank1 " + dbName + " : " + "custId_0 : " + custId_0 + " bank1_1 : " +  bank1_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank1Is(custId_0, bank1_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank1Is(custId_0, bank1_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank1Is(custId_0, bank1_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank1Is(custId_0, bank1_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank2(String custId_0, String bank2_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank2 " + dbName + " : " + "custId_0 : " + custId_0 + " bank2_1 : " +  bank2_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank2Is(custId_0, bank2_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank2Is(custId_0, bank2_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank2Is(custId_0, bank2_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank2Is(custId_0, bank2_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank3(String custId_0, String bank3_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank3 " + dbName + " : " + "custId_0 : " + custId_0 + " bank3_1 : " +  bank3_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank3Is(custId_0, bank3_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank3Is(custId_0, bank3_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank3Is(custId_0, bank3_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank3Is(custId_0, bank3_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank4(String custId_0, String bank4_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank4 " + dbName + " : " + "custId_0 : " + custId_0 + " bank4_1 : " +  bank4_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank4Is(custId_0, bank4_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank4Is(custId_0, bank4_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank4Is(custId_0, bank4_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank4Is(custId_0, bank4_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank5(String custId_0, String bank5_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank5 " + dbName + " : " + "custId_0 : " + custId_0 + " bank5_1 : " +  bank5_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank5Is(custId_0, bank5_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank5Is(custId_0, bank5_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank5Is(custId_0, bank5_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank5Is(custId_0, bank5_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank6(String custId_0, String bank6_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank6 " + dbName + " : " + "custId_0 : " + custId_0 + " bank6_1 : " +  bank6_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank6Is(custId_0, bank6_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank6Is(custId_0, bank6_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank6Is(custId_0, bank6_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank6Is(custId_0, bank6_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank7(String custId_0, String bank7_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank7 " + dbName + " : " + "custId_0 : " + custId_0 + " bank7_1 : " +  bank7_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank7Is(custId_0, bank7_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank7Is(custId_0, bank7_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank7Is(custId_0, bank7_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank7Is(custId_0, bank7_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank8(String custId_0, String bank8_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank8 " + dbName + " : " + "custId_0 : " + custId_0 + " bank8_1 : " +  bank8_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank8Is(custId_0, bank8_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank8Is(custId_0, bank8_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank8Is(custId_0, bank8_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank8Is(custId_0, bank8_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank9(String custId_0, String bank9_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank9 " + dbName + " : " + "custId_0 : " + custId_0 + " bank9_1 : " +  bank9_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank9Is(custId_0, bank9_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank9Is(custId_0, bank9_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank9Is(custId_0, bank9_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank9Is(custId_0, bank9_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank10(String custId_0, String bank10_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank10 " + dbName + " : " + "custId_0 : " + custId_0 + " bank10_1 : " +  bank10_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank10Is(custId_0, bank10_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank10Is(custId_0, bank10_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank10Is(custId_0, bank10_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank10Is(custId_0, bank10_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank11(String custId_0, String bank11_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank11 " + dbName + " : " + "custId_0 : " + custId_0 + " bank11_1 : " +  bank11_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank11Is(custId_0, bank11_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank11Is(custId_0, bank11_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank11Is(custId_0, bank11_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank11Is(custId_0, bank11_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank12(String custId_0, String bank12_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank12 " + dbName + " : " + "custId_0 : " + custId_0 + " bank12_1 : " +  bank12_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank12Is(custId_0, bank12_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank12Is(custId_0, bank12_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank12Is(custId_0, bank12_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank12Is(custId_0, bank12_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank13(String custId_0, String bank13_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank13 " + dbName + " : " + "custId_0 : " + custId_0 + " bank13_1 : " +  bank13_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank13Is(custId_0, bank13_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank13Is(custId_0, bank13_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank13Is(custId_0, bank13_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank13Is(custId_0, bank13_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank14(String custId_0, String bank14_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank14 " + dbName + " : " + "custId_0 : " + custId_0 + " bank14_1 : " +  bank14_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank14Is(custId_0, bank14_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank14Is(custId_0, bank14_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank14Is(custId_0, bank14_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank14Is(custId_0, bank14_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank15(String custId_0, String bank15_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank15 " + dbName + " : " + "custId_0 : " + custId_0 + " bank15_1 : " +  bank15_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank15Is(custId_0, bank15_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank15Is(custId_0, bank15_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank15Is(custId_0, bank15_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank15Is(custId_0, bank15_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank16(String custId_0, String bank16_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank16 " + dbName + " : " + "custId_0 : " + custId_0 + " bank16_1 : " +  bank16_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank16Is(custId_0, bank16_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank16Is(custId_0, bank16_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank16Is(custId_0, bank16_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank16Is(custId_0, bank16_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank17(String custId_0, String bank17_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank17 " + dbName + " : " + "custId_0 : " + custId_0 + " bank17_1 : " +  bank17_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank17Is(custId_0, bank17_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank17Is(custId_0, bank17_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank17Is(custId_0, bank17_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank17Is(custId_0, bank17_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank18(String custId_0, String bank18_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank18 " + dbName + " : " + "custId_0 : " + custId_0 + " bank18_1 : " +  bank18_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank18Is(custId_0, bank18_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank18Is(custId_0, bank18_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank18Is(custId_0, bank18_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank18Is(custId_0, bank18_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank19(String custId_0, String bank19_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank19 " + dbName + " : " + "custId_0 : " + custId_0 + " bank19_1 : " +  bank19_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank19Is(custId_0, bank19_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank19Is(custId_0, bank19_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank19Is(custId_0, bank19_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank19Is(custId_0, bank19_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank20(String custId_0, String bank20_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank20 " + dbName + " : " + "custId_0 : " + custId_0 + " bank20_1 : " +  bank20_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank20Is(custId_0, bank20_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank20Is(custId_0, bank20_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank20Is(custId_0, bank20_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank20Is(custId_0, bank20_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank21(String custId_0, String bank21_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank21 " + dbName + " : " + "custId_0 : " + custId_0 + " bank21_1 : " +  bank21_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank21Is(custId_0, bank21_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank21Is(custId_0, bank21_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank21Is(custId_0, bank21_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank21Is(custId_0, bank21_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank22(String custId_0, String bank22_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank22 " + dbName + " : " + "custId_0 : " + custId_0 + " bank22_1 : " +  bank22_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank22Is(custId_0, bank22_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank22Is(custId_0, bank22_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank22Is(custId_0, bank22_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank22Is(custId_0, bank22_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank23(String custId_0, String bank23_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank23 " + dbName + " : " + "custId_0 : " + custId_0 + " bank23_1 : " +  bank23_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank23Is(custId_0, bank23_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank23Is(custId_0, bank23_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank23Is(custId_0, bank23_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank23Is(custId_0, bank23_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank24(String custId_0, String bank24_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank24 " + dbName + " : " + "custId_0 : " + custId_0 + " bank24_1 : " +  bank24_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank24Is(custId_0, bank24_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank24Is(custId_0, bank24_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank24Is(custId_0, bank24_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank24Is(custId_0, bank24_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank25(String custId_0, String bank25_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank25 " + dbName + " : " + "custId_0 : " + custId_0 + " bank25_1 : " +  bank25_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank25Is(custId_0, bank25_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank25Is(custId_0, bank25_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank25Is(custId_0, bank25_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank25Is(custId_0, bank25_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank26(String custId_0, String bank26_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank26 " + dbName + " : " + "custId_0 : " + custId_0 + " bank26_1 : " +  bank26_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank26Is(custId_0, bank26_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank26Is(custId_0, bank26_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank26Is(custId_0, bank26_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank26Is(custId_0, bank26_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank27(String custId_0, String bank27_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank27 " + dbName + " : " + "custId_0 : " + custId_0 + " bank27_1 : " +  bank27_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank27Is(custId_0, bank27_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank27Is(custId_0, bank27_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank27Is(custId_0, bank27_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank27Is(custId_0, bank27_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank28(String custId_0, String bank28_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank28 " + dbName + " : " + "custId_0 : " + custId_0 + " bank28_1 : " +  bank28_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank28Is(custId_0, bank28_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank28Is(custId_0, bank28_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank28Is(custId_0, bank28_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank28Is(custId_0, bank28_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank29(String custId_0, String bank29_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank29 " + dbName + " : " + "custId_0 : " + custId_0 + " bank29_1 : " +  bank29_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank29Is(custId_0, bank29_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank29Is(custId_0, bank29_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank29Is(custId_0, bank29_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank29Is(custId_0, bank29_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ570> findByBank30(String custId_0, String bank30_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ570> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByBank30 " + dbName + " : " + "custId_0 : " + custId_0 + " bank30_1 : " +  bank30_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ570ReposDay.findAllByCustIdIsAndBank30Is(custId_0, bank30_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ570ReposMon.findAllByCustIdIsAndBank30Is(custId_0, bank30_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ570ReposHist.findAllByCustIdIsAndBank30Is(custId_0, bank30_1, pageable);
    else 
      slice = jcicZ570Repos.findAllByCustIdIsAndBank30Is(custId_0, bank30_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ570 holdById(JcicZ570Id jcicZ570Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ570Id);
    Optional<JcicZ570> jcicZ570 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ570 = jcicZ570ReposDay.findByJcicZ570Id(jcicZ570Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ570 = jcicZ570ReposMon.findByJcicZ570Id(jcicZ570Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ570 = jcicZ570ReposHist.findByJcicZ570Id(jcicZ570Id);
    else 
      jcicZ570 = jcicZ570Repos.findByJcicZ570Id(jcicZ570Id);
    return jcicZ570.isPresent() ? jcicZ570.get() : null;
  }

  @Override
  public JcicZ570 holdById(JcicZ570 jcicZ570, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ570.getJcicZ570Id());
    Optional<JcicZ570> jcicZ570T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ570T = jcicZ570ReposDay.findByJcicZ570Id(jcicZ570.getJcicZ570Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ570T = jcicZ570ReposMon.findByJcicZ570Id(jcicZ570.getJcicZ570Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ570T = jcicZ570ReposHist.findByJcicZ570Id(jcicZ570.getJcicZ570Id());
    else 
      jcicZ570T = jcicZ570Repos.findByJcicZ570Id(jcicZ570.getJcicZ570Id());
    return jcicZ570T.isPresent() ? jcicZ570T.get() : null;
  }

  @Override
  public JcicZ570 insert(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ570.getJcicZ570Id());
    if (this.findById(jcicZ570.getJcicZ570Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ570.setCreateEmpNo(empNot);

    if(jcicZ570.getLastUpdateEmpNo() == null || jcicZ570.getLastUpdateEmpNo().isEmpty())
      jcicZ570.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ570ReposDay.saveAndFlush(jcicZ570);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ570ReposMon.saveAndFlush(jcicZ570);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ570ReposHist.saveAndFlush(jcicZ570);
    else 
    return jcicZ570Repos.saveAndFlush(jcicZ570);
  }

  @Override
  public JcicZ570 update(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ570.getJcicZ570Id());
    if (!empNot.isEmpty())
      jcicZ570.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ570ReposDay.saveAndFlush(jcicZ570);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ570ReposMon.saveAndFlush(jcicZ570);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ570ReposHist.saveAndFlush(jcicZ570);
    else 
    return jcicZ570Repos.saveAndFlush(jcicZ570);
  }

  @Override
  public JcicZ570 update2(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ570.getJcicZ570Id());
    if (!empNot.isEmpty())
      jcicZ570.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ570ReposDay.saveAndFlush(jcicZ570);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ570ReposMon.saveAndFlush(jcicZ570);
    else if (dbName.equals(ContentName.onHist))
        jcicZ570ReposHist.saveAndFlush(jcicZ570);
    else 
      jcicZ570Repos.saveAndFlush(jcicZ570);	
    return this.findById(jcicZ570.getJcicZ570Id());
  }

  @Override
  public void delete(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ570.getJcicZ570Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ570ReposDay.delete(jcicZ570);	
      jcicZ570ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ570ReposMon.delete(jcicZ570);	
      jcicZ570ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ570ReposHist.delete(jcicZ570);
      jcicZ570ReposHist.flush();
    }
    else {
      jcicZ570Repos.delete(jcicZ570);
      jcicZ570Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ570> jcicZ570, TitaVo... titaVo) throws DBException {
    if (jcicZ570 == null || jcicZ570.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ570 t : jcicZ570){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ570 = jcicZ570ReposDay.saveAll(jcicZ570);	
      jcicZ570ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ570 = jcicZ570ReposMon.saveAll(jcicZ570);	
      jcicZ570ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ570 = jcicZ570ReposHist.saveAll(jcicZ570);
      jcicZ570ReposHist.flush();
    }
    else {
      jcicZ570 = jcicZ570Repos.saveAll(jcicZ570);
      jcicZ570Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ570> jcicZ570, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ570 == null || jcicZ570.size() == 0)
      throw new DBException(6);

    for (JcicZ570 t : jcicZ570) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ570 = jcicZ570ReposDay.saveAll(jcicZ570);	
      jcicZ570ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ570 = jcicZ570ReposMon.saveAll(jcicZ570);	
      jcicZ570ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ570 = jcicZ570ReposHist.saveAll(jcicZ570);
      jcicZ570ReposHist.flush();
    }
    else {
      jcicZ570 = jcicZ570Repos.saveAll(jcicZ570);
      jcicZ570Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ570> jcicZ570, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ570 == null || jcicZ570.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ570ReposDay.deleteAll(jcicZ570);	
      jcicZ570ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ570ReposMon.deleteAll(jcicZ570);	
      jcicZ570ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ570ReposHist.deleteAll(jcicZ570);
      jcicZ570ReposHist.flush();
    }
    else {
      jcicZ570Repos.deleteAll(jcicZ570);
      jcicZ570Repos.flush();
    }
  }

}
