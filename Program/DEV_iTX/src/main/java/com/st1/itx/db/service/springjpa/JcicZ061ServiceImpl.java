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
import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.domain.JcicZ061Id;
import com.st1.itx.db.repository.online.JcicZ061Repository;
import com.st1.itx.db.repository.day.JcicZ061RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ061RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ061RepositoryHist;
import com.st1.itx.db.service.JcicZ061Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ061Service")
@Repository
public class JcicZ061ServiceImpl extends ASpringJpaParm implements JcicZ061Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ061Repository jcicZ061Repos;

  @Autowired
  private JcicZ061RepositoryDay jcicZ061ReposDay;

  @Autowired
  private JcicZ061RepositoryMon jcicZ061ReposMon;

  @Autowired
  private JcicZ061RepositoryHist jcicZ061ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ061Repos);
    org.junit.Assert.assertNotNull(jcicZ061ReposDay);
    org.junit.Assert.assertNotNull(jcicZ061ReposMon);
    org.junit.Assert.assertNotNull(jcicZ061ReposHist);
  }

  @Override
  public JcicZ061 findById(JcicZ061Id jcicZ061Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ061Id);
    Optional<JcicZ061> jcicZ061 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ061 = jcicZ061ReposDay.findById(jcicZ061Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ061 = jcicZ061ReposMon.findById(jcicZ061Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ061 = jcicZ061ReposHist.findById(jcicZ061Id);
    else 
      jcicZ061 = jcicZ061Repos.findById(jcicZ061Id);
    JcicZ061 obj = jcicZ061.isPresent() ? jcicZ061.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ061> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ061> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "ChangePayDate", "MaxMainCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "ChangePayDate", "MaxMainCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ061ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ061ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ061ReposHist.findAll(pageable);
    else 
      slice = jcicZ061Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ061> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ061> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ061ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ061ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ061ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);
    else 
      slice = jcicZ061Repos.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ061> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ061> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("RcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ061ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ061ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ061ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ061Repos.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ061> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ061> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ061ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ061ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ061ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ061Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ061> otherEq(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, String maxMainCode_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ061> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " changePayDate_3 : " +  changePayDate_3 + " maxMainCode_4 : " +  maxMainCode_4);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ061ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, maxMainCode_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ061ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, maxMainCode_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ061ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, maxMainCode_4, pageable);
    else 
      slice = jcicZ061Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, maxMainCode_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ061 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ061> jcicZ061T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ061T = jcicZ061ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ061T = jcicZ061ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ061T = jcicZ061ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ061T = jcicZ061Repos.findTopByUkeyIs(ukey_0);

    return jcicZ061T.isPresent() ? jcicZ061T.get() : null;
  }

  @Override
  public JcicZ061 otherFirst(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, String maxMainCode_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " changePayDate_3 : " +  changePayDate_3 + " maxMainCode_4 : " +  maxMainCode_4);
    Optional<JcicZ061> jcicZ061T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ061T = jcicZ061ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, maxMainCode_4);
    else if (dbName.equals(ContentName.onMon))
      jcicZ061T = jcicZ061ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, maxMainCode_4);
    else if (dbName.equals(ContentName.onHist))
      jcicZ061T = jcicZ061ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, maxMainCode_4);
    else 
      jcicZ061T = jcicZ061Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, maxMainCode_4);

    return jcicZ061T.isPresent() ? jcicZ061T.get() : null;
  }

  @Override
  public JcicZ061 holdById(JcicZ061Id jcicZ061Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ061Id);
    Optional<JcicZ061> jcicZ061 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ061 = jcicZ061ReposDay.findByJcicZ061Id(jcicZ061Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ061 = jcicZ061ReposMon.findByJcicZ061Id(jcicZ061Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ061 = jcicZ061ReposHist.findByJcicZ061Id(jcicZ061Id);
    else 
      jcicZ061 = jcicZ061Repos.findByJcicZ061Id(jcicZ061Id);
    return jcicZ061.isPresent() ? jcicZ061.get() : null;
  }

  @Override
  public JcicZ061 holdById(JcicZ061 jcicZ061, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ061.getJcicZ061Id());
    Optional<JcicZ061> jcicZ061T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ061T = jcicZ061ReposDay.findByJcicZ061Id(jcicZ061.getJcicZ061Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ061T = jcicZ061ReposMon.findByJcicZ061Id(jcicZ061.getJcicZ061Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ061T = jcicZ061ReposHist.findByJcicZ061Id(jcicZ061.getJcicZ061Id());
    else 
      jcicZ061T = jcicZ061Repos.findByJcicZ061Id(jcicZ061.getJcicZ061Id());
    return jcicZ061T.isPresent() ? jcicZ061T.get() : null;
  }

  @Override
  public JcicZ061 insert(JcicZ061 jcicZ061, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ061.getJcicZ061Id());
    if (this.findById(jcicZ061.getJcicZ061Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ061.setCreateEmpNo(empNot);

    if(jcicZ061.getLastUpdateEmpNo() == null || jcicZ061.getLastUpdateEmpNo().isEmpty())
      jcicZ061.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ061ReposDay.saveAndFlush(jcicZ061);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ061ReposMon.saveAndFlush(jcicZ061);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ061ReposHist.saveAndFlush(jcicZ061);
    else 
    return jcicZ061Repos.saveAndFlush(jcicZ061);
  }

  @Override
  public JcicZ061 update(JcicZ061 jcicZ061, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ061.getJcicZ061Id());
    if (!empNot.isEmpty())
      jcicZ061.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ061ReposDay.saveAndFlush(jcicZ061);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ061ReposMon.saveAndFlush(jcicZ061);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ061ReposHist.saveAndFlush(jcicZ061);
    else 
    return jcicZ061Repos.saveAndFlush(jcicZ061);
  }

  @Override
  public JcicZ061 update2(JcicZ061 jcicZ061, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ061.getJcicZ061Id());
    if (!empNot.isEmpty())
      jcicZ061.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ061ReposDay.saveAndFlush(jcicZ061);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ061ReposMon.saveAndFlush(jcicZ061);
    else if (dbName.equals(ContentName.onHist))
        jcicZ061ReposHist.saveAndFlush(jcicZ061);
    else 
      jcicZ061Repos.saveAndFlush(jcicZ061);	
    return this.findById(jcicZ061.getJcicZ061Id());
  }

  @Override
  public void delete(JcicZ061 jcicZ061, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ061.getJcicZ061Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ061ReposDay.delete(jcicZ061);	
      jcicZ061ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ061ReposMon.delete(jcicZ061);	
      jcicZ061ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ061ReposHist.delete(jcicZ061);
      jcicZ061ReposHist.flush();
    }
    else {
      jcicZ061Repos.delete(jcicZ061);
      jcicZ061Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ061> jcicZ061, TitaVo... titaVo) throws DBException {
    if (jcicZ061 == null || jcicZ061.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ061 t : jcicZ061){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ061 = jcicZ061ReposDay.saveAll(jcicZ061);	
      jcicZ061ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ061 = jcicZ061ReposMon.saveAll(jcicZ061);	
      jcicZ061ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ061 = jcicZ061ReposHist.saveAll(jcicZ061);
      jcicZ061ReposHist.flush();
    }
    else {
      jcicZ061 = jcicZ061Repos.saveAll(jcicZ061);
      jcicZ061Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ061> jcicZ061, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ061 == null || jcicZ061.size() == 0)
      throw new DBException(6);

    for (JcicZ061 t : jcicZ061) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ061 = jcicZ061ReposDay.saveAll(jcicZ061);	
      jcicZ061ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ061 = jcicZ061ReposMon.saveAll(jcicZ061);	
      jcicZ061ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ061 = jcicZ061ReposHist.saveAll(jcicZ061);
      jcicZ061ReposHist.flush();
    }
    else {
      jcicZ061 = jcicZ061Repos.saveAll(jcicZ061);
      jcicZ061Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ061> jcicZ061, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ061 == null || jcicZ061.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ061ReposDay.deleteAll(jcicZ061);	
      jcicZ061ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ061ReposMon.deleteAll(jcicZ061);	
      jcicZ061ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ061ReposHist.deleteAll(jcicZ061);
      jcicZ061ReposHist.flush();
    }
    else {
      jcicZ061Repos.deleteAll(jcicZ061);
      jcicZ061Repos.flush();
    }
  }

}
