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
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Id;
import com.st1.itx.db.repository.online.JcicZ440Repository;
import com.st1.itx.db.repository.day.JcicZ440RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ440RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ440RepositoryHist;
import com.st1.itx.db.service.JcicZ440Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ440Service")
@Repository
public class JcicZ440ServiceImpl extends ASpringJpaParm implements JcicZ440Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ440Repository jcicZ440Repos;

  @Autowired
  private JcicZ440RepositoryDay jcicZ440ReposDay;

  @Autowired
  private JcicZ440RepositoryMon jcicZ440ReposMon;

  @Autowired
  private JcicZ440RepositoryHist jcicZ440ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ440Repos);
    org.junit.Assert.assertNotNull(jcicZ440ReposDay);
    org.junit.Assert.assertNotNull(jcicZ440ReposMon);
    org.junit.Assert.assertNotNull(jcicZ440ReposHist);
  }

  @Override
  public JcicZ440 findById(JcicZ440Id jcicZ440Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ440Id);
    Optional<JcicZ440> jcicZ440 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ440 = jcicZ440ReposDay.findById(jcicZ440Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ440 = jcicZ440ReposMon.findById(jcicZ440Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ440 = jcicZ440ReposHist.findById(jcicZ440Id);
    else 
      jcicZ440 = jcicZ440Repos.findById(jcicZ440Id);
    JcicZ440 obj = jcicZ440.isPresent() ? jcicZ440.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ440> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ440> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ440ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ440ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ440ReposHist.findAll(pageable);
    else 
      slice = jcicZ440Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ440> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ440> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ440ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ440ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ440ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ440Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ440> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ440> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ440ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ440ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ440ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ440Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ440> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ440> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ440ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ440ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ440ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ440Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ440> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ440> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ440ReposDay.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ440ReposMon.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ440ReposHist.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);
    else 
      slice = jcicZ440Repos.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ440 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ440> jcicZ440T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ440T = jcicZ440ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ440T = jcicZ440ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ440T = jcicZ440ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ440T = jcicZ440Repos.findTopByUkeyIs(ukey_0);

    return jcicZ440T.isPresent() ? jcicZ440T.get() : null;
  }

  @Override
  public JcicZ440 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3);
    Optional<JcicZ440> jcicZ440T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ440T = jcicZ440ReposDay.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);
    else if (dbName.equals(ContentName.onMon))
      jcicZ440T = jcicZ440ReposMon.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);
    else if (dbName.equals(ContentName.onHist))
      jcicZ440T = jcicZ440ReposHist.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);
    else 
      jcicZ440T = jcicZ440Repos.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);

    return jcicZ440T.isPresent() ? jcicZ440T.get() : null;
  }

  @Override
  public JcicZ440 holdById(JcicZ440Id jcicZ440Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ440Id);
    Optional<JcicZ440> jcicZ440 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ440 = jcicZ440ReposDay.findByJcicZ440Id(jcicZ440Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ440 = jcicZ440ReposMon.findByJcicZ440Id(jcicZ440Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ440 = jcicZ440ReposHist.findByJcicZ440Id(jcicZ440Id);
    else 
      jcicZ440 = jcicZ440Repos.findByJcicZ440Id(jcicZ440Id);
    return jcicZ440.isPresent() ? jcicZ440.get() : null;
  }

  @Override
  public JcicZ440 holdById(JcicZ440 jcicZ440, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ440.getJcicZ440Id());
    Optional<JcicZ440> jcicZ440T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ440T = jcicZ440ReposDay.findByJcicZ440Id(jcicZ440.getJcicZ440Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ440T = jcicZ440ReposMon.findByJcicZ440Id(jcicZ440.getJcicZ440Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ440T = jcicZ440ReposHist.findByJcicZ440Id(jcicZ440.getJcicZ440Id());
    else 
      jcicZ440T = jcicZ440Repos.findByJcicZ440Id(jcicZ440.getJcicZ440Id());
    return jcicZ440T.isPresent() ? jcicZ440T.get() : null;
  }

  @Override
  public JcicZ440 insert(JcicZ440 jcicZ440, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ440.getJcicZ440Id());
    if (this.findById(jcicZ440.getJcicZ440Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ440.setCreateEmpNo(empNot);

    if(jcicZ440.getLastUpdateEmpNo() == null || jcicZ440.getLastUpdateEmpNo().isEmpty())
      jcicZ440.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ440ReposDay.saveAndFlush(jcicZ440);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ440ReposMon.saveAndFlush(jcicZ440);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ440ReposHist.saveAndFlush(jcicZ440);
    else 
    return jcicZ440Repos.saveAndFlush(jcicZ440);
  }

  @Override
  public JcicZ440 update(JcicZ440 jcicZ440, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ440.getJcicZ440Id());
    if (!empNot.isEmpty())
      jcicZ440.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ440ReposDay.saveAndFlush(jcicZ440);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ440ReposMon.saveAndFlush(jcicZ440);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ440ReposHist.saveAndFlush(jcicZ440);
    else 
    return jcicZ440Repos.saveAndFlush(jcicZ440);
  }

  @Override
  public JcicZ440 update2(JcicZ440 jcicZ440, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ440.getJcicZ440Id());
    if (!empNot.isEmpty())
      jcicZ440.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ440ReposDay.saveAndFlush(jcicZ440);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ440ReposMon.saveAndFlush(jcicZ440);
    else if (dbName.equals(ContentName.onHist))
        jcicZ440ReposHist.saveAndFlush(jcicZ440);
    else 
      jcicZ440Repos.saveAndFlush(jcicZ440);	
    return this.findById(jcicZ440.getJcicZ440Id());
  }

  @Override
  public void delete(JcicZ440 jcicZ440, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ440.getJcicZ440Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ440ReposDay.delete(jcicZ440);	
      jcicZ440ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ440ReposMon.delete(jcicZ440);	
      jcicZ440ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ440ReposHist.delete(jcicZ440);
      jcicZ440ReposHist.flush();
    }
    else {
      jcicZ440Repos.delete(jcicZ440);
      jcicZ440Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ440> jcicZ440, TitaVo... titaVo) throws DBException {
    if (jcicZ440 == null || jcicZ440.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ440 t : jcicZ440){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ440 = jcicZ440ReposDay.saveAll(jcicZ440);	
      jcicZ440ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ440 = jcicZ440ReposMon.saveAll(jcicZ440);	
      jcicZ440ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ440 = jcicZ440ReposHist.saveAll(jcicZ440);
      jcicZ440ReposHist.flush();
    }
    else {
      jcicZ440 = jcicZ440Repos.saveAll(jcicZ440);
      jcicZ440Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ440> jcicZ440, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ440 == null || jcicZ440.size() == 0)
      throw new DBException(6);

    for (JcicZ440 t : jcicZ440) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ440 = jcicZ440ReposDay.saveAll(jcicZ440);	
      jcicZ440ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ440 = jcicZ440ReposMon.saveAll(jcicZ440);	
      jcicZ440ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ440 = jcicZ440ReposHist.saveAll(jcicZ440);
      jcicZ440ReposHist.flush();
    }
    else {
      jcicZ440 = jcicZ440Repos.saveAll(jcicZ440);
      jcicZ440Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ440> jcicZ440, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ440 == null || jcicZ440.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ440ReposDay.deleteAll(jcicZ440);	
      jcicZ440ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ440ReposMon.deleteAll(jcicZ440);	
      jcicZ440ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ440ReposHist.deleteAll(jcicZ440);
      jcicZ440ReposHist.flush();
    }
    else {
      jcicZ440Repos.deleteAll(jcicZ440);
      jcicZ440Repos.flush();
    }
  }

}
