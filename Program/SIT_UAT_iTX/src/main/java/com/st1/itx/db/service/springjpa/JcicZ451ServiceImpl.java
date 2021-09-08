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
import com.st1.itx.db.domain.JcicZ451;
import com.st1.itx.db.domain.JcicZ451Id;
import com.st1.itx.db.repository.online.JcicZ451Repository;
import com.st1.itx.db.repository.day.JcicZ451RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ451RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ451RepositoryHist;
import com.st1.itx.db.service.JcicZ451Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ451Service")
@Repository
public class JcicZ451ServiceImpl extends ASpringJpaParm implements JcicZ451Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ451Repository jcicZ451Repos;

  @Autowired
  private JcicZ451RepositoryDay jcicZ451ReposDay;

  @Autowired
  private JcicZ451RepositoryMon jcicZ451ReposMon;

  @Autowired
  private JcicZ451RepositoryHist jcicZ451ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ451Repos);
    org.junit.Assert.assertNotNull(jcicZ451ReposDay);
    org.junit.Assert.assertNotNull(jcicZ451ReposMon);
    org.junit.Assert.assertNotNull(jcicZ451ReposHist);
  }

  @Override
  public JcicZ451 findById(JcicZ451Id jcicZ451Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ451Id);
    Optional<JcicZ451> jcicZ451 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ451 = jcicZ451ReposDay.findById(jcicZ451Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ451 = jcicZ451ReposMon.findById(jcicZ451Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ451 = jcicZ451ReposHist.findById(jcicZ451Id);
    else 
      jcicZ451 = jcicZ451Repos.findById(jcicZ451Id);
    JcicZ451 obj = jcicZ451.isPresent() ? jcicZ451.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ451> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ451> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode", "DelayYM"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode", "DelayYM"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ451ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ451ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ451ReposHist.findAll(pageable);
    else 
      slice = jcicZ451Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ451> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ451> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ451ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ451ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ451ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ451Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ451> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ451> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ451ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ451ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ451ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ451Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ451> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ451> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ451ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ451ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ451ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ451Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ451> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int delayYM_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ451> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3 + " delayYM_4 : " +  delayYM_4);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ451ReposDay.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, delayYM_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ451ReposMon.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, delayYM_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ451ReposHist.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, delayYM_4, pageable);
    else 
      slice = jcicZ451Repos.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, delayYM_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ451 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ451> jcicZ451T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ451T = jcicZ451ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ451T = jcicZ451ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ451T = jcicZ451ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ451T = jcicZ451Repos.findTopByUkeyIs(ukey_0);

    return jcicZ451T.isPresent() ? jcicZ451T.get() : null;
  }

  @Override
  public JcicZ451 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int delayYM_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3 + " delayYM_4 : " +  delayYM_4);
    Optional<JcicZ451> jcicZ451T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ451T = jcicZ451ReposDay.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, delayYM_4);
    else if (dbName.equals(ContentName.onMon))
      jcicZ451T = jcicZ451ReposMon.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, delayYM_4);
    else if (dbName.equals(ContentName.onHist))
      jcicZ451T = jcicZ451ReposHist.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, delayYM_4);
    else 
      jcicZ451T = jcicZ451Repos.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, delayYM_4);

    return jcicZ451T.isPresent() ? jcicZ451T.get() : null;
  }

  @Override
  public JcicZ451 holdById(JcicZ451Id jcicZ451Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ451Id);
    Optional<JcicZ451> jcicZ451 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ451 = jcicZ451ReposDay.findByJcicZ451Id(jcicZ451Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ451 = jcicZ451ReposMon.findByJcicZ451Id(jcicZ451Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ451 = jcicZ451ReposHist.findByJcicZ451Id(jcicZ451Id);
    else 
      jcicZ451 = jcicZ451Repos.findByJcicZ451Id(jcicZ451Id);
    return jcicZ451.isPresent() ? jcicZ451.get() : null;
  }

  @Override
  public JcicZ451 holdById(JcicZ451 jcicZ451, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ451.getJcicZ451Id());
    Optional<JcicZ451> jcicZ451T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ451T = jcicZ451ReposDay.findByJcicZ451Id(jcicZ451.getJcicZ451Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ451T = jcicZ451ReposMon.findByJcicZ451Id(jcicZ451.getJcicZ451Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ451T = jcicZ451ReposHist.findByJcicZ451Id(jcicZ451.getJcicZ451Id());
    else 
      jcicZ451T = jcicZ451Repos.findByJcicZ451Id(jcicZ451.getJcicZ451Id());
    return jcicZ451T.isPresent() ? jcicZ451T.get() : null;
  }

  @Override
  public JcicZ451 insert(JcicZ451 jcicZ451, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ451.getJcicZ451Id());
    if (this.findById(jcicZ451.getJcicZ451Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ451.setCreateEmpNo(empNot);

    if(jcicZ451.getLastUpdateEmpNo() == null || jcicZ451.getLastUpdateEmpNo().isEmpty())
      jcicZ451.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ451ReposDay.saveAndFlush(jcicZ451);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ451ReposMon.saveAndFlush(jcicZ451);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ451ReposHist.saveAndFlush(jcicZ451);
    else 
    return jcicZ451Repos.saveAndFlush(jcicZ451);
  }

  @Override
  public JcicZ451 update(JcicZ451 jcicZ451, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ451.getJcicZ451Id());
    if (!empNot.isEmpty())
      jcicZ451.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ451ReposDay.saveAndFlush(jcicZ451);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ451ReposMon.saveAndFlush(jcicZ451);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ451ReposHist.saveAndFlush(jcicZ451);
    else 
    return jcicZ451Repos.saveAndFlush(jcicZ451);
  }

  @Override
  public JcicZ451 update2(JcicZ451 jcicZ451, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ451.getJcicZ451Id());
    if (!empNot.isEmpty())
      jcicZ451.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ451ReposDay.saveAndFlush(jcicZ451);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ451ReposMon.saveAndFlush(jcicZ451);
    else if (dbName.equals(ContentName.onHist))
        jcicZ451ReposHist.saveAndFlush(jcicZ451);
    else 
      jcicZ451Repos.saveAndFlush(jcicZ451);	
    return this.findById(jcicZ451.getJcicZ451Id());
  }

  @Override
  public void delete(JcicZ451 jcicZ451, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ451.getJcicZ451Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ451ReposDay.delete(jcicZ451);	
      jcicZ451ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ451ReposMon.delete(jcicZ451);	
      jcicZ451ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ451ReposHist.delete(jcicZ451);
      jcicZ451ReposHist.flush();
    }
    else {
      jcicZ451Repos.delete(jcicZ451);
      jcicZ451Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ451> jcicZ451, TitaVo... titaVo) throws DBException {
    if (jcicZ451 == null || jcicZ451.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ451 t : jcicZ451){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ451 = jcicZ451ReposDay.saveAll(jcicZ451);	
      jcicZ451ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ451 = jcicZ451ReposMon.saveAll(jcicZ451);	
      jcicZ451ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ451 = jcicZ451ReposHist.saveAll(jcicZ451);
      jcicZ451ReposHist.flush();
    }
    else {
      jcicZ451 = jcicZ451Repos.saveAll(jcicZ451);
      jcicZ451Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ451> jcicZ451, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ451 == null || jcicZ451.size() == 0)
      throw new DBException(6);

    for (JcicZ451 t : jcicZ451) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ451 = jcicZ451ReposDay.saveAll(jcicZ451);	
      jcicZ451ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ451 = jcicZ451ReposMon.saveAll(jcicZ451);	
      jcicZ451ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ451 = jcicZ451ReposHist.saveAll(jcicZ451);
      jcicZ451ReposHist.flush();
    }
    else {
      jcicZ451 = jcicZ451Repos.saveAll(jcicZ451);
      jcicZ451Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ451> jcicZ451, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ451 == null || jcicZ451.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ451ReposDay.deleteAll(jcicZ451);	
      jcicZ451ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ451ReposMon.deleteAll(jcicZ451);	
      jcicZ451ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ451ReposHist.deleteAll(jcicZ451);
      jcicZ451ReposHist.flush();
    }
    else {
      jcicZ451Repos.deleteAll(jcicZ451);
      jcicZ451Repos.flush();
    }
  }

}
