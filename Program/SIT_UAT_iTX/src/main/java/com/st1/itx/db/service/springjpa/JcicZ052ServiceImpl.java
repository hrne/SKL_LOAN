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
import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.domain.JcicZ052Id;
import com.st1.itx.db.repository.online.JcicZ052Repository;
import com.st1.itx.db.repository.day.JcicZ052RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ052RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ052RepositoryHist;
import com.st1.itx.db.service.JcicZ052Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ052Service")
@Repository
public class JcicZ052ServiceImpl extends ASpringJpaParm implements JcicZ052Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ052Repository jcicZ052Repos;

  @Autowired
  private JcicZ052RepositoryDay jcicZ052ReposDay;

  @Autowired
  private JcicZ052RepositoryMon jcicZ052ReposMon;

  @Autowired
  private JcicZ052RepositoryHist jcicZ052ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ052Repos);
    org.junit.Assert.assertNotNull(jcicZ052ReposDay);
    org.junit.Assert.assertNotNull(jcicZ052ReposMon);
    org.junit.Assert.assertNotNull(jcicZ052ReposHist);
  }

  @Override
  public JcicZ052 findById(JcicZ052Id jcicZ052Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ052Id);
    Optional<JcicZ052> jcicZ052 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ052 = jcicZ052ReposDay.findById(jcicZ052Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ052 = jcicZ052ReposMon.findById(jcicZ052Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ052 = jcicZ052ReposHist.findById(jcicZ052Id);
    else 
      jcicZ052 = jcicZ052Repos.findById(jcicZ052Id);
    JcicZ052 obj = jcicZ052.isPresent() ? jcicZ052.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ052> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ052> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ052ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ052ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ052ReposHist.findAll(pageable);
    else 
      slice = jcicZ052Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ052> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ052> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ052ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ052ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ052ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ052Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ052> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ052> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("RcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ052ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ052ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ052ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ052Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ052> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ052> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ052ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ052ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ052ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ052Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ052> otherEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ052> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ052ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ052ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ052ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else 
      slice = jcicZ052Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ052 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ052> jcicZ052T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ052T = jcicZ052ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ052T = jcicZ052ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ052T = jcicZ052ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ052T = jcicZ052Repos.findTopByUkeyIs(ukey_0);

    return jcicZ052T.isPresent() ? jcicZ052T.get() : null;
  }

  @Override
  public JcicZ052 otherFirst(String submitKey_0, String custId_1, int rcDate_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2);
    Optional<JcicZ052> jcicZ052T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ052T = jcicZ052ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else if (dbName.equals(ContentName.onMon))
      jcicZ052T = jcicZ052ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else if (dbName.equals(ContentName.onHist))
      jcicZ052T = jcicZ052ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else 
      jcicZ052T = jcicZ052Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);

    return jcicZ052T.isPresent() ? jcicZ052T.get() : null;
  }

  @Override
  public JcicZ052 holdById(JcicZ052Id jcicZ052Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ052Id);
    Optional<JcicZ052> jcicZ052 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ052 = jcicZ052ReposDay.findByJcicZ052Id(jcicZ052Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ052 = jcicZ052ReposMon.findByJcicZ052Id(jcicZ052Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ052 = jcicZ052ReposHist.findByJcicZ052Id(jcicZ052Id);
    else 
      jcicZ052 = jcicZ052Repos.findByJcicZ052Id(jcicZ052Id);
    return jcicZ052.isPresent() ? jcicZ052.get() : null;
  }

  @Override
  public JcicZ052 holdById(JcicZ052 jcicZ052, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ052.getJcicZ052Id());
    Optional<JcicZ052> jcicZ052T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ052T = jcicZ052ReposDay.findByJcicZ052Id(jcicZ052.getJcicZ052Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ052T = jcicZ052ReposMon.findByJcicZ052Id(jcicZ052.getJcicZ052Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ052T = jcicZ052ReposHist.findByJcicZ052Id(jcicZ052.getJcicZ052Id());
    else 
      jcicZ052T = jcicZ052Repos.findByJcicZ052Id(jcicZ052.getJcicZ052Id());
    return jcicZ052T.isPresent() ? jcicZ052T.get() : null;
  }

  @Override
  public JcicZ052 insert(JcicZ052 jcicZ052, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ052.getJcicZ052Id());
    if (this.findById(jcicZ052.getJcicZ052Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ052.setCreateEmpNo(empNot);

    if(jcicZ052.getLastUpdateEmpNo() == null || jcicZ052.getLastUpdateEmpNo().isEmpty())
      jcicZ052.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ052ReposDay.saveAndFlush(jcicZ052);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ052ReposMon.saveAndFlush(jcicZ052);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ052ReposHist.saveAndFlush(jcicZ052);
    else 
    return jcicZ052Repos.saveAndFlush(jcicZ052);
  }

  @Override
  public JcicZ052 update(JcicZ052 jcicZ052, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ052.getJcicZ052Id());
    if (!empNot.isEmpty())
      jcicZ052.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ052ReposDay.saveAndFlush(jcicZ052);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ052ReposMon.saveAndFlush(jcicZ052);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ052ReposHist.saveAndFlush(jcicZ052);
    else 
    return jcicZ052Repos.saveAndFlush(jcicZ052);
  }

  @Override
  public JcicZ052 update2(JcicZ052 jcicZ052, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ052.getJcicZ052Id());
    if (!empNot.isEmpty())
      jcicZ052.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ052ReposDay.saveAndFlush(jcicZ052);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ052ReposMon.saveAndFlush(jcicZ052);
    else if (dbName.equals(ContentName.onHist))
        jcicZ052ReposHist.saveAndFlush(jcicZ052);
    else 
      jcicZ052Repos.saveAndFlush(jcicZ052);	
    return this.findById(jcicZ052.getJcicZ052Id());
  }

  @Override
  public void delete(JcicZ052 jcicZ052, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ052.getJcicZ052Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ052ReposDay.delete(jcicZ052);	
      jcicZ052ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ052ReposMon.delete(jcicZ052);	
      jcicZ052ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ052ReposHist.delete(jcicZ052);
      jcicZ052ReposHist.flush();
    }
    else {
      jcicZ052Repos.delete(jcicZ052);
      jcicZ052Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ052> jcicZ052, TitaVo... titaVo) throws DBException {
    if (jcicZ052 == null || jcicZ052.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ052 t : jcicZ052){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ052 = jcicZ052ReposDay.saveAll(jcicZ052);	
      jcicZ052ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ052 = jcicZ052ReposMon.saveAll(jcicZ052);	
      jcicZ052ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ052 = jcicZ052ReposHist.saveAll(jcicZ052);
      jcicZ052ReposHist.flush();
    }
    else {
      jcicZ052 = jcicZ052Repos.saveAll(jcicZ052);
      jcicZ052Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ052> jcicZ052, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ052 == null || jcicZ052.size() == 0)
      throw new DBException(6);

    for (JcicZ052 t : jcicZ052) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ052 = jcicZ052ReposDay.saveAll(jcicZ052);	
      jcicZ052ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ052 = jcicZ052ReposMon.saveAll(jcicZ052);	
      jcicZ052ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ052 = jcicZ052ReposHist.saveAll(jcicZ052);
      jcicZ052ReposHist.flush();
    }
    else {
      jcicZ052 = jcicZ052Repos.saveAll(jcicZ052);
      jcicZ052Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ052> jcicZ052, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ052 == null || jcicZ052.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ052ReposDay.deleteAll(jcicZ052);	
      jcicZ052ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ052ReposMon.deleteAll(jcicZ052);	
      jcicZ052ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ052ReposHist.deleteAll(jcicZ052);
      jcicZ052ReposHist.flush();
    }
    else {
      jcicZ052Repos.deleteAll(jcicZ052);
      jcicZ052Repos.flush();
    }
  }

}
