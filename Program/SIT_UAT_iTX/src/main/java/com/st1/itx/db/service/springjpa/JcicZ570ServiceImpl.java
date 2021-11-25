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
import com.st1.itx.eum.ThreadVariable;

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
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

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
		} else
       empNot = ThreadVariable.getEmpNot();

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
		} else
       empNot = ThreadVariable.getEmpNot();

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
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
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
		} else
       empNot = ThreadVariable.getEmpNot();

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
