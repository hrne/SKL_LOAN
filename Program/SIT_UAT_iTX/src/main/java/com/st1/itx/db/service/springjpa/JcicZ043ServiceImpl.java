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
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ043Id;
import com.st1.itx.db.repository.online.JcicZ043Repository;
import com.st1.itx.db.repository.day.JcicZ043RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ043RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ043RepositoryHist;
import com.st1.itx.db.service.JcicZ043Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ043Service")
@Repository
public class JcicZ043ServiceImpl extends ASpringJpaParm implements JcicZ043Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ043Repository jcicZ043Repos;

  @Autowired
  private JcicZ043RepositoryDay jcicZ043ReposDay;

  @Autowired
  private JcicZ043RepositoryMon jcicZ043ReposMon;

  @Autowired
  private JcicZ043RepositoryHist jcicZ043ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ043Repos);
    org.junit.Assert.assertNotNull(jcicZ043ReposDay);
    org.junit.Assert.assertNotNull(jcicZ043ReposMon);
    org.junit.Assert.assertNotNull(jcicZ043ReposHist);
  }

  @Override
  public JcicZ043 findById(JcicZ043Id jcicZ043Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ043Id);
    Optional<JcicZ043> jcicZ043 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ043 = jcicZ043ReposDay.findById(jcicZ043Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ043 = jcicZ043ReposMon.findById(jcicZ043Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ043 = jcicZ043ReposHist.findById(jcicZ043Id);
    else 
      jcicZ043 = jcicZ043Repos.findById(jcicZ043Id);
    JcicZ043 obj = jcicZ043.isPresent() ? jcicZ043.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ043> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ043> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "MaxMainCode", "Account"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "MaxMainCode", "Account"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ043ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ043ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ043ReposHist.findAll(pageable);
    else 
      slice = jcicZ043Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ043> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ043> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ043ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ043ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ043ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ043Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ043> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ043> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("RcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ043ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ043ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ043ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ043Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ043> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ043> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ043ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ043ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ043ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ043Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ043> CoutCollaterals(String custId_0, int rcDate_1, String submitKey_2, String maxMainCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ043> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CoutCollaterals " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1 + " submitKey_2 : " +  submitKey_2 + " maxMainCode_3 : " +  maxMainCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ043ReposDay.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsAndMaxMainCodeIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, submitKey_2, maxMainCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ043ReposMon.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsAndMaxMainCodeIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, submitKey_2, maxMainCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ043ReposHist.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsAndMaxMainCodeIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, submitKey_2, maxMainCode_3, pageable);
    else 
      slice = jcicZ043Repos.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsAndMaxMainCodeIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, submitKey_2, maxMainCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ043> otherEq(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, String account_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ043> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " maxMainCode_3 : " +  maxMainCode_3 + " account_4 : " +  account_4);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ043ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndAccountIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, account_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ043ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndAccountIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, account_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ043ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndAccountIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, account_4, pageable);
    else 
      slice = jcicZ043Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndAccountIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, account_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ043 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ043> jcicZ043T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ043T = jcicZ043ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ043T = jcicZ043ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ043T = jcicZ043ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ043T = jcicZ043Repos.findTopByUkeyIs(ukey_0);

    return jcicZ043T.isPresent() ? jcicZ043T.get() : null;
  }

  @Override
  public JcicZ043 otherFirst(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, String account_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " maxMainCode_3 : " +  maxMainCode_3 + " account_4 : " +  account_4);
    Optional<JcicZ043> jcicZ043T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ043T = jcicZ043ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndAccountIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, account_4);
    else if (dbName.equals(ContentName.onMon))
      jcicZ043T = jcicZ043ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndAccountIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, account_4);
    else if (dbName.equals(ContentName.onHist))
      jcicZ043T = jcicZ043ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndAccountIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, account_4);
    else 
      jcicZ043T = jcicZ043Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndAccountIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, account_4);

    return jcicZ043T.isPresent() ? jcicZ043T.get() : null;
  }

  @Override
  public JcicZ043 holdById(JcicZ043Id jcicZ043Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ043Id);
    Optional<JcicZ043> jcicZ043 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ043 = jcicZ043ReposDay.findByJcicZ043Id(jcicZ043Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ043 = jcicZ043ReposMon.findByJcicZ043Id(jcicZ043Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ043 = jcicZ043ReposHist.findByJcicZ043Id(jcicZ043Id);
    else 
      jcicZ043 = jcicZ043Repos.findByJcicZ043Id(jcicZ043Id);
    return jcicZ043.isPresent() ? jcicZ043.get() : null;
  }

  @Override
  public JcicZ043 holdById(JcicZ043 jcicZ043, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ043.getJcicZ043Id());
    Optional<JcicZ043> jcicZ043T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ043T = jcicZ043ReposDay.findByJcicZ043Id(jcicZ043.getJcicZ043Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ043T = jcicZ043ReposMon.findByJcicZ043Id(jcicZ043.getJcicZ043Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ043T = jcicZ043ReposHist.findByJcicZ043Id(jcicZ043.getJcicZ043Id());
    else 
      jcicZ043T = jcicZ043Repos.findByJcicZ043Id(jcicZ043.getJcicZ043Id());
    return jcicZ043T.isPresent() ? jcicZ043T.get() : null;
  }

  @Override
  public JcicZ043 insert(JcicZ043 jcicZ043, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ043.getJcicZ043Id());
    if (this.findById(jcicZ043.getJcicZ043Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ043.setCreateEmpNo(empNot);

    if(jcicZ043.getLastUpdateEmpNo() == null || jcicZ043.getLastUpdateEmpNo().isEmpty())
      jcicZ043.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ043ReposDay.saveAndFlush(jcicZ043);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ043ReposMon.saveAndFlush(jcicZ043);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ043ReposHist.saveAndFlush(jcicZ043);
    else 
    return jcicZ043Repos.saveAndFlush(jcicZ043);
  }

  @Override
  public JcicZ043 update(JcicZ043 jcicZ043, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ043.getJcicZ043Id());
    if (!empNot.isEmpty())
      jcicZ043.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ043ReposDay.saveAndFlush(jcicZ043);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ043ReposMon.saveAndFlush(jcicZ043);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ043ReposHist.saveAndFlush(jcicZ043);
    else 
    return jcicZ043Repos.saveAndFlush(jcicZ043);
  }

  @Override
  public JcicZ043 update2(JcicZ043 jcicZ043, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ043.getJcicZ043Id());
    if (!empNot.isEmpty())
      jcicZ043.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ043ReposDay.saveAndFlush(jcicZ043);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ043ReposMon.saveAndFlush(jcicZ043);
    else if (dbName.equals(ContentName.onHist))
        jcicZ043ReposHist.saveAndFlush(jcicZ043);
    else 
      jcicZ043Repos.saveAndFlush(jcicZ043);	
    return this.findById(jcicZ043.getJcicZ043Id());
  }

  @Override
  public void delete(JcicZ043 jcicZ043, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ043.getJcicZ043Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ043ReposDay.delete(jcicZ043);	
      jcicZ043ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ043ReposMon.delete(jcicZ043);	
      jcicZ043ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ043ReposHist.delete(jcicZ043);
      jcicZ043ReposHist.flush();
    }
    else {
      jcicZ043Repos.delete(jcicZ043);
      jcicZ043Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ043> jcicZ043, TitaVo... titaVo) throws DBException {
    if (jcicZ043 == null || jcicZ043.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ043 t : jcicZ043){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ043 = jcicZ043ReposDay.saveAll(jcicZ043);	
      jcicZ043ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ043 = jcicZ043ReposMon.saveAll(jcicZ043);	
      jcicZ043ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ043 = jcicZ043ReposHist.saveAll(jcicZ043);
      jcicZ043ReposHist.flush();
    }
    else {
      jcicZ043 = jcicZ043Repos.saveAll(jcicZ043);
      jcicZ043Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ043> jcicZ043, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ043 == null || jcicZ043.size() == 0)
      throw new DBException(6);

    for (JcicZ043 t : jcicZ043) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ043 = jcicZ043ReposDay.saveAll(jcicZ043);	
      jcicZ043ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ043 = jcicZ043ReposMon.saveAll(jcicZ043);	
      jcicZ043ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ043 = jcicZ043ReposHist.saveAll(jcicZ043);
      jcicZ043ReposHist.flush();
    }
    else {
      jcicZ043 = jcicZ043Repos.saveAll(jcicZ043);
      jcicZ043Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ043> jcicZ043, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ043 == null || jcicZ043.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ043ReposDay.deleteAll(jcicZ043);	
      jcicZ043ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ043ReposMon.deleteAll(jcicZ043);	
      jcicZ043ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ043ReposHist.deleteAll(jcicZ043);
      jcicZ043ReposHist.flush();
    }
    else {
      jcicZ043Repos.deleteAll(jcicZ043);
      jcicZ043Repos.flush();
    }
  }

}
