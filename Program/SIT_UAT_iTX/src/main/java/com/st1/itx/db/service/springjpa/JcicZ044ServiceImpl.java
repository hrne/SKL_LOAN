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
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.domain.JcicZ044Id;
import com.st1.itx.db.repository.online.JcicZ044Repository;
import com.st1.itx.db.repository.day.JcicZ044RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ044RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ044RepositoryHist;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ044Service")
@Repository
public class JcicZ044ServiceImpl extends ASpringJpaParm implements JcicZ044Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ044Repository jcicZ044Repos;

  @Autowired
  private JcicZ044RepositoryDay jcicZ044ReposDay;

  @Autowired
  private JcicZ044RepositoryMon jcicZ044ReposMon;

  @Autowired
  private JcicZ044RepositoryHist jcicZ044ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ044Repos);
    org.junit.Assert.assertNotNull(jcicZ044ReposDay);
    org.junit.Assert.assertNotNull(jcicZ044ReposMon);
    org.junit.Assert.assertNotNull(jcicZ044ReposHist);
  }

  @Override
  public JcicZ044 findById(JcicZ044Id jcicZ044Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ044Id);
    Optional<JcicZ044> jcicZ044 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ044 = jcicZ044ReposDay.findById(jcicZ044Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ044 = jcicZ044ReposMon.findById(jcicZ044Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ044 = jcicZ044ReposHist.findById(jcicZ044Id);
    else 
      jcicZ044 = jcicZ044Repos.findById(jcicZ044Id);
    JcicZ044 obj = jcicZ044.isPresent() ? jcicZ044.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ044> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ044> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ044ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ044ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ044ReposHist.findAll(pageable);
    else 
      slice = jcicZ044Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ044> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ044> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ044ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ044ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ044ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ044Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ044> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ044> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ044ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ044ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ044ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ044Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ044> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ044> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ044ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ044ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ044ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ044Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ044> otherEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ044> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ044ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ044ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ044ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else 
      slice = jcicZ044Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ044 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ044> jcicZ044T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ044T = jcicZ044ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ044T = jcicZ044ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ044T = jcicZ044ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ044T = jcicZ044Repos.findTopByUkeyIs(ukey_0);

    return jcicZ044T.isPresent() ? jcicZ044T.get() : null;
  }

  @Override
  public JcicZ044 otherFirst(String submitKey_0, String custId_1, int rcDate_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2);
    Optional<JcicZ044> jcicZ044T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ044T = jcicZ044ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else if (dbName.equals(ContentName.onMon))
      jcicZ044T = jcicZ044ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else if (dbName.equals(ContentName.onHist))
      jcicZ044T = jcicZ044ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else 
      jcicZ044T = jcicZ044Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);

    return jcicZ044T.isPresent() ? jcicZ044T.get() : null;
  }

  @Override
  public JcicZ044 holdById(JcicZ044Id jcicZ044Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ044Id);
    Optional<JcicZ044> jcicZ044 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ044 = jcicZ044ReposDay.findByJcicZ044Id(jcicZ044Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ044 = jcicZ044ReposMon.findByJcicZ044Id(jcicZ044Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ044 = jcicZ044ReposHist.findByJcicZ044Id(jcicZ044Id);
    else 
      jcicZ044 = jcicZ044Repos.findByJcicZ044Id(jcicZ044Id);
    return jcicZ044.isPresent() ? jcicZ044.get() : null;
  }

  @Override
  public JcicZ044 holdById(JcicZ044 jcicZ044, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ044.getJcicZ044Id());
    Optional<JcicZ044> jcicZ044T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ044T = jcicZ044ReposDay.findByJcicZ044Id(jcicZ044.getJcicZ044Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ044T = jcicZ044ReposMon.findByJcicZ044Id(jcicZ044.getJcicZ044Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ044T = jcicZ044ReposHist.findByJcicZ044Id(jcicZ044.getJcicZ044Id());
    else 
      jcicZ044T = jcicZ044Repos.findByJcicZ044Id(jcicZ044.getJcicZ044Id());
    return jcicZ044T.isPresent() ? jcicZ044T.get() : null;
  }

  @Override
  public JcicZ044 insert(JcicZ044 jcicZ044, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ044.getJcicZ044Id());
    if (this.findById(jcicZ044.getJcicZ044Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ044.setCreateEmpNo(empNot);

    if(jcicZ044.getLastUpdateEmpNo() == null || jcicZ044.getLastUpdateEmpNo().isEmpty())
      jcicZ044.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ044ReposDay.saveAndFlush(jcicZ044);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ044ReposMon.saveAndFlush(jcicZ044);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ044ReposHist.saveAndFlush(jcicZ044);
    else 
    return jcicZ044Repos.saveAndFlush(jcicZ044);
  }

  @Override
  public JcicZ044 update(JcicZ044 jcicZ044, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ044.getJcicZ044Id());
    if (!empNot.isEmpty())
      jcicZ044.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ044ReposDay.saveAndFlush(jcicZ044);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ044ReposMon.saveAndFlush(jcicZ044);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ044ReposHist.saveAndFlush(jcicZ044);
    else 
    return jcicZ044Repos.saveAndFlush(jcicZ044);
  }

  @Override
  public JcicZ044 update2(JcicZ044 jcicZ044, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ044.getJcicZ044Id());
    if (!empNot.isEmpty())
      jcicZ044.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ044ReposDay.saveAndFlush(jcicZ044);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ044ReposMon.saveAndFlush(jcicZ044);
    else if (dbName.equals(ContentName.onHist))
        jcicZ044ReposHist.saveAndFlush(jcicZ044);
    else 
      jcicZ044Repos.saveAndFlush(jcicZ044);	
    return this.findById(jcicZ044.getJcicZ044Id());
  }

  @Override
  public void delete(JcicZ044 jcicZ044, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ044.getJcicZ044Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ044ReposDay.delete(jcicZ044);	
      jcicZ044ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ044ReposMon.delete(jcicZ044);	
      jcicZ044ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ044ReposHist.delete(jcicZ044);
      jcicZ044ReposHist.flush();
    }
    else {
      jcicZ044Repos.delete(jcicZ044);
      jcicZ044Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ044> jcicZ044, TitaVo... titaVo) throws DBException {
    if (jcicZ044 == null || jcicZ044.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ044 t : jcicZ044){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ044 = jcicZ044ReposDay.saveAll(jcicZ044);	
      jcicZ044ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ044 = jcicZ044ReposMon.saveAll(jcicZ044);	
      jcicZ044ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ044 = jcicZ044ReposHist.saveAll(jcicZ044);
      jcicZ044ReposHist.flush();
    }
    else {
      jcicZ044 = jcicZ044Repos.saveAll(jcicZ044);
      jcicZ044Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ044> jcicZ044, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ044 == null || jcicZ044.size() == 0)
      throw new DBException(6);

    for (JcicZ044 t : jcicZ044) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ044 = jcicZ044ReposDay.saveAll(jcicZ044);	
      jcicZ044ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ044 = jcicZ044ReposMon.saveAll(jcicZ044);	
      jcicZ044ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ044 = jcicZ044ReposHist.saveAll(jcicZ044);
      jcicZ044ReposHist.flush();
    }
    else {
      jcicZ044 = jcicZ044Repos.saveAll(jcicZ044);
      jcicZ044Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ044> jcicZ044, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ044 == null || jcicZ044.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ044ReposDay.deleteAll(jcicZ044);	
      jcicZ044ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ044ReposMon.deleteAll(jcicZ044);	
      jcicZ044ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ044ReposHist.deleteAll(jcicZ044);
      jcicZ044ReposHist.flush();
    }
    else {
      jcicZ044Repos.deleteAll(jcicZ044);
      jcicZ044Repos.flush();
    }
  }

}
