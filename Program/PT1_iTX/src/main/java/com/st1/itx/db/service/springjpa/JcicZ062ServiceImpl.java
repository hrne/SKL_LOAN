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
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ062Id;
import com.st1.itx.db.repository.online.JcicZ062Repository;
import com.st1.itx.db.repository.day.JcicZ062RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ062RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ062RepositoryHist;
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ062Service")
@Repository
public class JcicZ062ServiceImpl extends ASpringJpaParm implements JcicZ062Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ062Repository jcicZ062Repos;

  @Autowired
  private JcicZ062RepositoryDay jcicZ062ReposDay;

  @Autowired
  private JcicZ062RepositoryMon jcicZ062ReposMon;

  @Autowired
  private JcicZ062RepositoryHist jcicZ062ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ062Repos);
    org.junit.Assert.assertNotNull(jcicZ062ReposDay);
    org.junit.Assert.assertNotNull(jcicZ062ReposMon);
    org.junit.Assert.assertNotNull(jcicZ062ReposHist);
  }

  @Override
  public JcicZ062 findById(JcicZ062Id jcicZ062Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ062Id);
    Optional<JcicZ062> jcicZ062 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ062 = jcicZ062ReposDay.findById(jcicZ062Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ062 = jcicZ062ReposMon.findById(jcicZ062Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ062 = jcicZ062ReposHist.findById(jcicZ062Id);
    else 
      jcicZ062 = jcicZ062Repos.findById(jcicZ062Id);
    JcicZ062 obj = jcicZ062.isPresent() ? jcicZ062.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ062> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ062> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "ChangePayDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "ChangePayDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ062ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ062ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ062ReposHist.findAll(pageable);
    else 
      slice = jcicZ062Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ062> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ062> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ062ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ062ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ062ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);
    else 
      slice = jcicZ062Repos.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ062> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ062> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ062ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ062ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ062ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ062Repos.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ062> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ062> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ062ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ062ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ062ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ062Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ062> otherEq(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ062> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " changePayDate_3 : " +  changePayDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ062ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ062ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ062ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, pageable);
    else 
      slice = jcicZ062Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ062 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ062> jcicZ062T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ062T = jcicZ062ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ062T = jcicZ062ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ062T = jcicZ062ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ062T = jcicZ062Repos.findTopByUkeyIs(ukey_0);

    return jcicZ062T.isPresent() ? jcicZ062T.get() : null;
  }

  @Override
  public JcicZ062 otherFirst(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " changePayDate_3 : " +  changePayDate_3);
    Optional<JcicZ062> jcicZ062T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ062T = jcicZ062ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3);
    else if (dbName.equals(ContentName.onMon))
      jcicZ062T = jcicZ062ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3);
    else if (dbName.equals(ContentName.onHist))
      jcicZ062T = jcicZ062ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3);
    else 
      jcicZ062T = jcicZ062Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3);

    return jcicZ062T.isPresent() ? jcicZ062T.get() : null;
  }

  @Override
  public JcicZ062 holdById(JcicZ062Id jcicZ062Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ062Id);
    Optional<JcicZ062> jcicZ062 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ062 = jcicZ062ReposDay.findByJcicZ062Id(jcicZ062Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ062 = jcicZ062ReposMon.findByJcicZ062Id(jcicZ062Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ062 = jcicZ062ReposHist.findByJcicZ062Id(jcicZ062Id);
    else 
      jcicZ062 = jcicZ062Repos.findByJcicZ062Id(jcicZ062Id);
    return jcicZ062.isPresent() ? jcicZ062.get() : null;
  }

  @Override
  public JcicZ062 holdById(JcicZ062 jcicZ062, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ062.getJcicZ062Id());
    Optional<JcicZ062> jcicZ062T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ062T = jcicZ062ReposDay.findByJcicZ062Id(jcicZ062.getJcicZ062Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ062T = jcicZ062ReposMon.findByJcicZ062Id(jcicZ062.getJcicZ062Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ062T = jcicZ062ReposHist.findByJcicZ062Id(jcicZ062.getJcicZ062Id());
    else 
      jcicZ062T = jcicZ062Repos.findByJcicZ062Id(jcicZ062.getJcicZ062Id());
    return jcicZ062T.isPresent() ? jcicZ062T.get() : null;
  }

  @Override
  public JcicZ062 insert(JcicZ062 jcicZ062, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ062.getJcicZ062Id());
    if (this.findById(jcicZ062.getJcicZ062Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ062.setCreateEmpNo(empNot);

    if(jcicZ062.getLastUpdateEmpNo() == null || jcicZ062.getLastUpdateEmpNo().isEmpty())
      jcicZ062.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ062ReposDay.saveAndFlush(jcicZ062);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ062ReposMon.saveAndFlush(jcicZ062);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ062ReposHist.saveAndFlush(jcicZ062);
    else 
    return jcicZ062Repos.saveAndFlush(jcicZ062);
  }

  @Override
  public JcicZ062 update(JcicZ062 jcicZ062, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ062.getJcicZ062Id());
    if (!empNot.isEmpty())
      jcicZ062.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ062ReposDay.saveAndFlush(jcicZ062);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ062ReposMon.saveAndFlush(jcicZ062);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ062ReposHist.saveAndFlush(jcicZ062);
    else 
    return jcicZ062Repos.saveAndFlush(jcicZ062);
  }

  @Override
  public JcicZ062 update2(JcicZ062 jcicZ062, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ062.getJcicZ062Id());
    if (!empNot.isEmpty())
      jcicZ062.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ062ReposDay.saveAndFlush(jcicZ062);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ062ReposMon.saveAndFlush(jcicZ062);
    else if (dbName.equals(ContentName.onHist))
        jcicZ062ReposHist.saveAndFlush(jcicZ062);
    else 
      jcicZ062Repos.saveAndFlush(jcicZ062);	
    return this.findById(jcicZ062.getJcicZ062Id());
  }

  @Override
  public void delete(JcicZ062 jcicZ062, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ062.getJcicZ062Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ062ReposDay.delete(jcicZ062);	
      jcicZ062ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ062ReposMon.delete(jcicZ062);	
      jcicZ062ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ062ReposHist.delete(jcicZ062);
      jcicZ062ReposHist.flush();
    }
    else {
      jcicZ062Repos.delete(jcicZ062);
      jcicZ062Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ062> jcicZ062, TitaVo... titaVo) throws DBException {
    if (jcicZ062 == null || jcicZ062.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ062 t : jcicZ062){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ062 = jcicZ062ReposDay.saveAll(jcicZ062);	
      jcicZ062ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ062 = jcicZ062ReposMon.saveAll(jcicZ062);	
      jcicZ062ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ062 = jcicZ062ReposHist.saveAll(jcicZ062);
      jcicZ062ReposHist.flush();
    }
    else {
      jcicZ062 = jcicZ062Repos.saveAll(jcicZ062);
      jcicZ062Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ062> jcicZ062, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ062 == null || jcicZ062.size() == 0)
      throw new DBException(6);

    for (JcicZ062 t : jcicZ062) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ062 = jcicZ062ReposDay.saveAll(jcicZ062);	
      jcicZ062ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ062 = jcicZ062ReposMon.saveAll(jcicZ062);	
      jcicZ062ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ062 = jcicZ062ReposHist.saveAll(jcicZ062);
      jcicZ062ReposHist.flush();
    }
    else {
      jcicZ062 = jcicZ062Repos.saveAll(jcicZ062);
      jcicZ062Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ062> jcicZ062, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ062 == null || jcicZ062.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ062ReposDay.deleteAll(jcicZ062);	
      jcicZ062ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ062ReposMon.deleteAll(jcicZ062);	
      jcicZ062ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ062ReposHist.deleteAll(jcicZ062);
      jcicZ062ReposHist.flush();
    }
    else {
      jcicZ062Repos.deleteAll(jcicZ062);
      jcicZ062Repos.flush();
    }
  }

}
