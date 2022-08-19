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
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.domain.JcicZ573Id;
import com.st1.itx.db.repository.online.JcicZ573Repository;
import com.st1.itx.db.repository.day.JcicZ573RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ573RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ573RepositoryHist;
import com.st1.itx.db.service.JcicZ573Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ573Service")
@Repository
public class JcicZ573ServiceImpl extends ASpringJpaParm implements JcicZ573Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ573Repository jcicZ573Repos;

  @Autowired
  private JcicZ573RepositoryDay jcicZ573ReposDay;

  @Autowired
  private JcicZ573RepositoryMon jcicZ573ReposMon;

  @Autowired
  private JcicZ573RepositoryHist jcicZ573ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ573Repos);
    org.junit.Assert.assertNotNull(jcicZ573ReposDay);
    org.junit.Assert.assertNotNull(jcicZ573ReposMon);
    org.junit.Assert.assertNotNull(jcicZ573ReposHist);
  }

  @Override
  public JcicZ573 findById(JcicZ573Id jcicZ573Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ573Id);
    Optional<JcicZ573> jcicZ573 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ573 = jcicZ573ReposDay.findById(jcicZ573Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ573 = jcicZ573ReposMon.findById(jcicZ573Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ573 = jcicZ573ReposHist.findById(jcicZ573Id);
    else 
      jcicZ573 = jcicZ573Repos.findById(jcicZ573Id);
    JcicZ573 obj = jcicZ573.isPresent() ? jcicZ573.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ573> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ573> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "PayDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "PayDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ573ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ573ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ573ReposHist.findAll(pageable);
    else 
      slice = jcicZ573Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ573> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ573> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ573ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ573ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ573ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, pageable);
    else 
      slice = jcicZ573Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ573> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ573> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ573ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ573ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ573ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ573Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(applyDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ573> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ573> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ573ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ573ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ573ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ573Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ573 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ573> jcicZ573T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ573T = jcicZ573ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ573T = jcicZ573ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ573T = jcicZ573ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ573T = jcicZ573Repos.findTopByUkeyIs(ukey_0);

    return jcicZ573T.isPresent() ? jcicZ573T.get() : null;
  }

  @Override
  public Slice<JcicZ573> otherEq(String custId_0, int applyDate_1, String submitKey_2, int payDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ573> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1 + " submitKey_2 : " +  submitKey_2 + " payDate_3 : " +  payDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ573ReposDay.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsAndPayDateIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, payDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ573ReposMon.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsAndPayDateIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, payDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ573ReposHist.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsAndPayDateIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, payDate_3, pageable);
    else 
      slice = jcicZ573Repos.findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsAndPayDateIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, payDate_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ573 otherFirst(String custId_0, int applyDate_1, String submitKey_2, int payDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1 + " submitKey_2 : " +  submitKey_2 + " payDate_3 : " +  payDate_3);
    Optional<JcicZ573> jcicZ573T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ573T = jcicZ573ReposDay.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsAndPayDateIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, payDate_3);
    else if (dbName.equals(ContentName.onMon))
      jcicZ573T = jcicZ573ReposMon.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsAndPayDateIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, payDate_3);
    else if (dbName.equals(ContentName.onHist))
      jcicZ573T = jcicZ573ReposHist.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsAndPayDateIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, payDate_3);
    else 
      jcicZ573T = jcicZ573Repos.findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsAndPayDateIsOrderByCreateDateDesc(custId_0, applyDate_1, submitKey_2, payDate_3);

    return jcicZ573T.isPresent() ? jcicZ573T.get() : null;
  }

  @Override
  public JcicZ573 holdById(JcicZ573Id jcicZ573Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ573Id);
    Optional<JcicZ573> jcicZ573 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ573 = jcicZ573ReposDay.findByJcicZ573Id(jcicZ573Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ573 = jcicZ573ReposMon.findByJcicZ573Id(jcicZ573Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ573 = jcicZ573ReposHist.findByJcicZ573Id(jcicZ573Id);
    else 
      jcicZ573 = jcicZ573Repos.findByJcicZ573Id(jcicZ573Id);
    return jcicZ573.isPresent() ? jcicZ573.get() : null;
  }

  @Override
  public JcicZ573 holdById(JcicZ573 jcicZ573, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ573.getJcicZ573Id());
    Optional<JcicZ573> jcicZ573T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ573T = jcicZ573ReposDay.findByJcicZ573Id(jcicZ573.getJcicZ573Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ573T = jcicZ573ReposMon.findByJcicZ573Id(jcicZ573.getJcicZ573Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ573T = jcicZ573ReposHist.findByJcicZ573Id(jcicZ573.getJcicZ573Id());
    else 
      jcicZ573T = jcicZ573Repos.findByJcicZ573Id(jcicZ573.getJcicZ573Id());
    return jcicZ573T.isPresent() ? jcicZ573T.get() : null;
  }

  @Override
  public JcicZ573 insert(JcicZ573 jcicZ573, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ573.getJcicZ573Id());
    if (this.findById(jcicZ573.getJcicZ573Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ573.setCreateEmpNo(empNot);

    if(jcicZ573.getLastUpdateEmpNo() == null || jcicZ573.getLastUpdateEmpNo().isEmpty())
      jcicZ573.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ573ReposDay.saveAndFlush(jcicZ573);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ573ReposMon.saveAndFlush(jcicZ573);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ573ReposHist.saveAndFlush(jcicZ573);
    else 
    return jcicZ573Repos.saveAndFlush(jcicZ573);
  }

  @Override
  public JcicZ573 update(JcicZ573 jcicZ573, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ573.getJcicZ573Id());
    if (!empNot.isEmpty())
      jcicZ573.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ573ReposDay.saveAndFlush(jcicZ573);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ573ReposMon.saveAndFlush(jcicZ573);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ573ReposHist.saveAndFlush(jcicZ573);
    else 
    return jcicZ573Repos.saveAndFlush(jcicZ573);
  }

  @Override
  public JcicZ573 update2(JcicZ573 jcicZ573, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ573.getJcicZ573Id());
    if (!empNot.isEmpty())
      jcicZ573.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ573ReposDay.saveAndFlush(jcicZ573);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ573ReposMon.saveAndFlush(jcicZ573);
    else if (dbName.equals(ContentName.onHist))
        jcicZ573ReposHist.saveAndFlush(jcicZ573);
    else 
      jcicZ573Repos.saveAndFlush(jcicZ573);	
    return this.findById(jcicZ573.getJcicZ573Id());
  }

  @Override
  public void delete(JcicZ573 jcicZ573, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ573.getJcicZ573Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ573ReposDay.delete(jcicZ573);	
      jcicZ573ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ573ReposMon.delete(jcicZ573);	
      jcicZ573ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ573ReposHist.delete(jcicZ573);
      jcicZ573ReposHist.flush();
    }
    else {
      jcicZ573Repos.delete(jcicZ573);
      jcicZ573Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ573> jcicZ573, TitaVo... titaVo) throws DBException {
    if (jcicZ573 == null || jcicZ573.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ573 t : jcicZ573){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ573 = jcicZ573ReposDay.saveAll(jcicZ573);	
      jcicZ573ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ573 = jcicZ573ReposMon.saveAll(jcicZ573);	
      jcicZ573ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ573 = jcicZ573ReposHist.saveAll(jcicZ573);
      jcicZ573ReposHist.flush();
    }
    else {
      jcicZ573 = jcicZ573Repos.saveAll(jcicZ573);
      jcicZ573Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ573> jcicZ573, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ573 == null || jcicZ573.size() == 0)
      throw new DBException(6);

    for (JcicZ573 t : jcicZ573) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ573 = jcicZ573ReposDay.saveAll(jcicZ573);	
      jcicZ573ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ573 = jcicZ573ReposMon.saveAll(jcicZ573);	
      jcicZ573ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ573 = jcicZ573ReposHist.saveAll(jcicZ573);
      jcicZ573ReposHist.flush();
    }
    else {
      jcicZ573 = jcicZ573Repos.saveAll(jcicZ573);
      jcicZ573Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ573> jcicZ573, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ573 == null || jcicZ573.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ573ReposDay.deleteAll(jcicZ573);	
      jcicZ573ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ573ReposMon.deleteAll(jcicZ573);	
      jcicZ573ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ573ReposHist.deleteAll(jcicZ573);
      jcicZ573ReposHist.flush();
    }
    else {
      jcicZ573Repos.deleteAll(jcicZ573);
      jcicZ573Repos.flush();
    }
  }

}
