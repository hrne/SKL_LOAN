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
import com.st1.itx.db.domain.JcicZ053;
import com.st1.itx.db.domain.JcicZ053Id;
import com.st1.itx.db.repository.online.JcicZ053Repository;
import com.st1.itx.db.repository.day.JcicZ053RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ053RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ053RepositoryHist;
import com.st1.itx.db.service.JcicZ053Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ053Service")
@Repository
public class JcicZ053ServiceImpl extends ASpringJpaParm implements JcicZ053Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ053Repository jcicZ053Repos;

  @Autowired
  private JcicZ053RepositoryDay jcicZ053ReposDay;

  @Autowired
  private JcicZ053RepositoryMon jcicZ053ReposMon;

  @Autowired
  private JcicZ053RepositoryHist jcicZ053ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ053Repos);
    org.junit.Assert.assertNotNull(jcicZ053ReposDay);
    org.junit.Assert.assertNotNull(jcicZ053ReposMon);
    org.junit.Assert.assertNotNull(jcicZ053ReposHist);
  }

  @Override
  public JcicZ053 findById(JcicZ053Id jcicZ053Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ053Id);
    Optional<JcicZ053> jcicZ053 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ053 = jcicZ053ReposDay.findById(jcicZ053Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ053 = jcicZ053ReposMon.findById(jcicZ053Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ053 = jcicZ053ReposHist.findById(jcicZ053Id);
    else 
      jcicZ053 = jcicZ053Repos.findById(jcicZ053Id);
    JcicZ053 obj = jcicZ053.isPresent() ? jcicZ053.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ053> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ053> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "MaxMainCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "MaxMainCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ053ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ053ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ053ReposHist.findAll(pageable);
    else 
      slice = jcicZ053Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ053> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ053> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ053ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ053ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ053ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ053Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ053> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ053> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ053ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ053ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ053ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ053Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ053> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ053> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ053ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ053ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ053ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ053Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ053> otherEq(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ053> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " maxMainCode_3 : " +  maxMainCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ053ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ053ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ053ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);
    else 
      slice = jcicZ053Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ053 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ053> jcicZ053T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ053T = jcicZ053ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ053T = jcicZ053ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ053T = jcicZ053ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ053T = jcicZ053Repos.findTopByUkeyIs(ukey_0);

    return jcicZ053T.isPresent() ? jcicZ053T.get() : null;
  }

  @Override
  public JcicZ053 otherFirst(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " maxMainCode_3 : " +  maxMainCode_3);
    Optional<JcicZ053> jcicZ053T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ053T = jcicZ053ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);
    else if (dbName.equals(ContentName.onMon))
      jcicZ053T = jcicZ053ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);
    else if (dbName.equals(ContentName.onHist))
      jcicZ053T = jcicZ053ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);
    else 
      jcicZ053T = jcicZ053Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);

    return jcicZ053T.isPresent() ? jcicZ053T.get() : null;
  }

  @Override
  public JcicZ053 holdById(JcicZ053Id jcicZ053Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ053Id);
    Optional<JcicZ053> jcicZ053 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ053 = jcicZ053ReposDay.findByJcicZ053Id(jcicZ053Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ053 = jcicZ053ReposMon.findByJcicZ053Id(jcicZ053Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ053 = jcicZ053ReposHist.findByJcicZ053Id(jcicZ053Id);
    else 
      jcicZ053 = jcicZ053Repos.findByJcicZ053Id(jcicZ053Id);
    return jcicZ053.isPresent() ? jcicZ053.get() : null;
  }

  @Override
  public JcicZ053 holdById(JcicZ053 jcicZ053, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ053.getJcicZ053Id());
    Optional<JcicZ053> jcicZ053T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ053T = jcicZ053ReposDay.findByJcicZ053Id(jcicZ053.getJcicZ053Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ053T = jcicZ053ReposMon.findByJcicZ053Id(jcicZ053.getJcicZ053Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ053T = jcicZ053ReposHist.findByJcicZ053Id(jcicZ053.getJcicZ053Id());
    else 
      jcicZ053T = jcicZ053Repos.findByJcicZ053Id(jcicZ053.getJcicZ053Id());
    return jcicZ053T.isPresent() ? jcicZ053T.get() : null;
  }

  @Override
  public JcicZ053 insert(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ053.getJcicZ053Id());
    if (this.findById(jcicZ053.getJcicZ053Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ053.setCreateEmpNo(empNot);

    if(jcicZ053.getLastUpdateEmpNo() == null || jcicZ053.getLastUpdateEmpNo().isEmpty())
      jcicZ053.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ053ReposDay.saveAndFlush(jcicZ053);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ053ReposMon.saveAndFlush(jcicZ053);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ053ReposHist.saveAndFlush(jcicZ053);
    else 
    return jcicZ053Repos.saveAndFlush(jcicZ053);
  }

  @Override
  public JcicZ053 update(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ053.getJcicZ053Id());
    if (!empNot.isEmpty())
      jcicZ053.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ053ReposDay.saveAndFlush(jcicZ053);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ053ReposMon.saveAndFlush(jcicZ053);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ053ReposHist.saveAndFlush(jcicZ053);
    else 
    return jcicZ053Repos.saveAndFlush(jcicZ053);
  }

  @Override
  public JcicZ053 update2(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ053.getJcicZ053Id());
    if (!empNot.isEmpty())
      jcicZ053.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ053ReposDay.saveAndFlush(jcicZ053);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ053ReposMon.saveAndFlush(jcicZ053);
    else if (dbName.equals(ContentName.onHist))
        jcicZ053ReposHist.saveAndFlush(jcicZ053);
    else 
      jcicZ053Repos.saveAndFlush(jcicZ053);	
    return this.findById(jcicZ053.getJcicZ053Id());
  }

  @Override
  public void delete(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ053.getJcicZ053Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ053ReposDay.delete(jcicZ053);	
      jcicZ053ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ053ReposMon.delete(jcicZ053);	
      jcicZ053ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ053ReposHist.delete(jcicZ053);
      jcicZ053ReposHist.flush();
    }
    else {
      jcicZ053Repos.delete(jcicZ053);
      jcicZ053Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ053> jcicZ053, TitaVo... titaVo) throws DBException {
    if (jcicZ053 == null || jcicZ053.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ053 t : jcicZ053){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ053 = jcicZ053ReposDay.saveAll(jcicZ053);	
      jcicZ053ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ053 = jcicZ053ReposMon.saveAll(jcicZ053);	
      jcicZ053ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ053 = jcicZ053ReposHist.saveAll(jcicZ053);
      jcicZ053ReposHist.flush();
    }
    else {
      jcicZ053 = jcicZ053Repos.saveAll(jcicZ053);
      jcicZ053Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ053> jcicZ053, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ053 == null || jcicZ053.size() == 0)
      throw new DBException(6);

    for (JcicZ053 t : jcicZ053) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ053 = jcicZ053ReposDay.saveAll(jcicZ053);	
      jcicZ053ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ053 = jcicZ053ReposMon.saveAll(jcicZ053);	
      jcicZ053ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ053 = jcicZ053ReposHist.saveAll(jcicZ053);
      jcicZ053ReposHist.flush();
    }
    else {
      jcicZ053 = jcicZ053Repos.saveAll(jcicZ053);
      jcicZ053Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ053> jcicZ053, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ053 == null || jcicZ053.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ053ReposDay.deleteAll(jcicZ053);	
      jcicZ053ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ053ReposMon.deleteAll(jcicZ053);	
      jcicZ053ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ053ReposHist.deleteAll(jcicZ053);
      jcicZ053ReposHist.flush();
    }
    else {
      jcicZ053Repos.deleteAll(jcicZ053);
      jcicZ053Repos.flush();
    }
  }

}
