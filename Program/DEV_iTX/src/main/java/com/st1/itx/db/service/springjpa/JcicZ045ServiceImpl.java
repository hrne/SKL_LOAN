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
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.domain.JcicZ045Id;
import com.st1.itx.db.repository.online.JcicZ045Repository;
import com.st1.itx.db.repository.day.JcicZ045RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ045RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ045RepositoryHist;
import com.st1.itx.db.service.JcicZ045Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ045Service")
@Repository
public class JcicZ045ServiceImpl extends ASpringJpaParm implements JcicZ045Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ045Repository jcicZ045Repos;

  @Autowired
  private JcicZ045RepositoryDay jcicZ045ReposDay;

  @Autowired
  private JcicZ045RepositoryMon jcicZ045ReposMon;

  @Autowired
  private JcicZ045RepositoryHist jcicZ045ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ045Repos);
    org.junit.Assert.assertNotNull(jcicZ045ReposDay);
    org.junit.Assert.assertNotNull(jcicZ045ReposMon);
    org.junit.Assert.assertNotNull(jcicZ045ReposHist);
  }

  @Override
  public JcicZ045 findById(JcicZ045Id jcicZ045Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ045Id);
    Optional<JcicZ045> jcicZ045 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ045 = jcicZ045ReposDay.findById(jcicZ045Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ045 = jcicZ045ReposMon.findById(jcicZ045Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ045 = jcicZ045ReposHist.findById(jcicZ045Id);
    else 
      jcicZ045 = jcicZ045Repos.findById(jcicZ045Id);
    JcicZ045 obj = jcicZ045.isPresent() ? jcicZ045.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ045> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ045> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "MaxMainCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "MaxMainCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ045ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ045ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ045ReposHist.findAll(pageable);
    else 
      slice = jcicZ045Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ045> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ045> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ045ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ045ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ045ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ045Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ045> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ045> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("RcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ045ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ045ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ045ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ045Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ045> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ045> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ045ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ045ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ045ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ045Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ045> otherEq(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ045> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " maxMainCode_3 : " +  maxMainCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ045ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ045ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ045ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);
    else 
      slice = jcicZ045Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ045 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ045> jcicZ045T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ045T = jcicZ045ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ045T = jcicZ045ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ045T = jcicZ045ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ045T = jcicZ045Repos.findTopByUkeyIs(ukey_0);

    return jcicZ045T.isPresent() ? jcicZ045T.get() : null;
  }

  @Override
  public JcicZ045 otherFirst(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " maxMainCode_3 : " +  maxMainCode_3);
    Optional<JcicZ045> jcicZ045T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ045T = jcicZ045ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);
    else if (dbName.equals(ContentName.onMon))
      jcicZ045T = jcicZ045ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);
    else if (dbName.equals(ContentName.onHist))
      jcicZ045T = jcicZ045ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);
    else 
      jcicZ045T = jcicZ045Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);

    return jcicZ045T.isPresent() ? jcicZ045T.get() : null;
  }

  @Override
  public JcicZ045 holdById(JcicZ045Id jcicZ045Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ045Id);
    Optional<JcicZ045> jcicZ045 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ045 = jcicZ045ReposDay.findByJcicZ045Id(jcicZ045Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ045 = jcicZ045ReposMon.findByJcicZ045Id(jcicZ045Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ045 = jcicZ045ReposHist.findByJcicZ045Id(jcicZ045Id);
    else 
      jcicZ045 = jcicZ045Repos.findByJcicZ045Id(jcicZ045Id);
    return jcicZ045.isPresent() ? jcicZ045.get() : null;
  }

  @Override
  public JcicZ045 holdById(JcicZ045 jcicZ045, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ045.getJcicZ045Id());
    Optional<JcicZ045> jcicZ045T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ045T = jcicZ045ReposDay.findByJcicZ045Id(jcicZ045.getJcicZ045Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ045T = jcicZ045ReposMon.findByJcicZ045Id(jcicZ045.getJcicZ045Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ045T = jcicZ045ReposHist.findByJcicZ045Id(jcicZ045.getJcicZ045Id());
    else 
      jcicZ045T = jcicZ045Repos.findByJcicZ045Id(jcicZ045.getJcicZ045Id());
    return jcicZ045T.isPresent() ? jcicZ045T.get() : null;
  }

  @Override
  public JcicZ045 insert(JcicZ045 jcicZ045, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ045.getJcicZ045Id());
    if (this.findById(jcicZ045.getJcicZ045Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ045.setCreateEmpNo(empNot);

    if(jcicZ045.getLastUpdateEmpNo() == null || jcicZ045.getLastUpdateEmpNo().isEmpty())
      jcicZ045.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ045ReposDay.saveAndFlush(jcicZ045);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ045ReposMon.saveAndFlush(jcicZ045);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ045ReposHist.saveAndFlush(jcicZ045);
    else 
    return jcicZ045Repos.saveAndFlush(jcicZ045);
  }

  @Override
  public JcicZ045 update(JcicZ045 jcicZ045, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ045.getJcicZ045Id());
    if (!empNot.isEmpty())
      jcicZ045.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ045ReposDay.saveAndFlush(jcicZ045);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ045ReposMon.saveAndFlush(jcicZ045);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ045ReposHist.saveAndFlush(jcicZ045);
    else 
    return jcicZ045Repos.saveAndFlush(jcicZ045);
  }

  @Override
  public JcicZ045 update2(JcicZ045 jcicZ045, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ045.getJcicZ045Id());
    if (!empNot.isEmpty())
      jcicZ045.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ045ReposDay.saveAndFlush(jcicZ045);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ045ReposMon.saveAndFlush(jcicZ045);
    else if (dbName.equals(ContentName.onHist))
        jcicZ045ReposHist.saveAndFlush(jcicZ045);
    else 
      jcicZ045Repos.saveAndFlush(jcicZ045);	
    return this.findById(jcicZ045.getJcicZ045Id());
  }

  @Override
  public void delete(JcicZ045 jcicZ045, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ045.getJcicZ045Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ045ReposDay.delete(jcicZ045);	
      jcicZ045ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ045ReposMon.delete(jcicZ045);	
      jcicZ045ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ045ReposHist.delete(jcicZ045);
      jcicZ045ReposHist.flush();
    }
    else {
      jcicZ045Repos.delete(jcicZ045);
      jcicZ045Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ045> jcicZ045, TitaVo... titaVo) throws DBException {
    if (jcicZ045 == null || jcicZ045.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ045 t : jcicZ045){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ045 = jcicZ045ReposDay.saveAll(jcicZ045);	
      jcicZ045ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ045 = jcicZ045ReposMon.saveAll(jcicZ045);	
      jcicZ045ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ045 = jcicZ045ReposHist.saveAll(jcicZ045);
      jcicZ045ReposHist.flush();
    }
    else {
      jcicZ045 = jcicZ045Repos.saveAll(jcicZ045);
      jcicZ045Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ045> jcicZ045, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ045 == null || jcicZ045.size() == 0)
      throw new DBException(6);

    for (JcicZ045 t : jcicZ045) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ045 = jcicZ045ReposDay.saveAll(jcicZ045);	
      jcicZ045ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ045 = jcicZ045ReposMon.saveAll(jcicZ045);	
      jcicZ045ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ045 = jcicZ045ReposHist.saveAll(jcicZ045);
      jcicZ045ReposHist.flush();
    }
    else {
      jcicZ045 = jcicZ045Repos.saveAll(jcicZ045);
      jcicZ045Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ045> jcicZ045, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ045 == null || jcicZ045.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ045ReposDay.deleteAll(jcicZ045);	
      jcicZ045ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ045ReposMon.deleteAll(jcicZ045);	
      jcicZ045ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ045ReposHist.deleteAll(jcicZ045);
      jcicZ045ReposHist.flush();
    }
    else {
      jcicZ045Repos.deleteAll(jcicZ045);
      jcicZ045Repos.flush();
    }
  }

}
