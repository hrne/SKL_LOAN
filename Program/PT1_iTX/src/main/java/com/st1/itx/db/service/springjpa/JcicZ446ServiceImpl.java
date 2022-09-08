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
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.domain.JcicZ446Id;
import com.st1.itx.db.repository.online.JcicZ446Repository;
import com.st1.itx.db.repository.day.JcicZ446RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ446RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ446RepositoryHist;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ446Service")
@Repository
public class JcicZ446ServiceImpl extends ASpringJpaParm implements JcicZ446Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ446Repository jcicZ446Repos;

  @Autowired
  private JcicZ446RepositoryDay jcicZ446ReposDay;

  @Autowired
  private JcicZ446RepositoryMon jcicZ446ReposMon;

  @Autowired
  private JcicZ446RepositoryHist jcicZ446ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ446Repos);
    org.junit.Assert.assertNotNull(jcicZ446ReposDay);
    org.junit.Assert.assertNotNull(jcicZ446ReposMon);
    org.junit.Assert.assertNotNull(jcicZ446ReposHist);
  }

  @Override
  public JcicZ446 findById(JcicZ446Id jcicZ446Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ446Id);
    Optional<JcicZ446> jcicZ446 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ446 = jcicZ446ReposDay.findById(jcicZ446Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ446 = jcicZ446ReposMon.findById(jcicZ446Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ446 = jcicZ446ReposHist.findById(jcicZ446Id);
    else 
      jcicZ446 = jcicZ446Repos.findById(jcicZ446Id);
    JcicZ446 obj = jcicZ446.isPresent() ? jcicZ446.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ446> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ446> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ446ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ446ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ446ReposHist.findAll(pageable);
    else 
      slice = jcicZ446Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ446> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ446> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ446ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ446ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ446ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ446Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ446> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ446> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ446ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ446ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ446ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ446Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ446> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ446> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ446ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ446ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ446ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ446Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ446> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ446> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ446ReposDay.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ446ReposMon.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ446ReposHist.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);
    else 
      slice = jcicZ446Repos.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ446 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ446> jcicZ446T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ446T = jcicZ446ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ446T = jcicZ446ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ446T = jcicZ446ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ446T = jcicZ446Repos.findTopByUkeyIs(ukey_0);

    return jcicZ446T.isPresent() ? jcicZ446T.get() : null;
  }

  @Override
  public JcicZ446 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3);
    Optional<JcicZ446> jcicZ446T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ446T = jcicZ446ReposDay.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);
    else if (dbName.equals(ContentName.onMon))
      jcicZ446T = jcicZ446ReposMon.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);
    else if (dbName.equals(ContentName.onHist))
      jcicZ446T = jcicZ446ReposHist.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);
    else 
      jcicZ446T = jcicZ446Repos.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3);

    return jcicZ446T.isPresent() ? jcicZ446T.get() : null;
  }

  @Override
  public Slice<JcicZ446> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ446> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findkeyFilingDate " + dbName + " : " + "actualFilingDate_0 : " + actualFilingDate_0 + " actualFilingMark_1 : " +  actualFilingMark_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ446ReposDay.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ446ReposMon.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ446ReposHist.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else 
      slice = jcicZ446Repos.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ446 holdById(JcicZ446Id jcicZ446Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ446Id);
    Optional<JcicZ446> jcicZ446 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ446 = jcicZ446ReposDay.findByJcicZ446Id(jcicZ446Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ446 = jcicZ446ReposMon.findByJcicZ446Id(jcicZ446Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ446 = jcicZ446ReposHist.findByJcicZ446Id(jcicZ446Id);
    else 
      jcicZ446 = jcicZ446Repos.findByJcicZ446Id(jcicZ446Id);
    return jcicZ446.isPresent() ? jcicZ446.get() : null;
  }

  @Override
  public JcicZ446 holdById(JcicZ446 jcicZ446, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ446.getJcicZ446Id());
    Optional<JcicZ446> jcicZ446T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ446T = jcicZ446ReposDay.findByJcicZ446Id(jcicZ446.getJcicZ446Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ446T = jcicZ446ReposMon.findByJcicZ446Id(jcicZ446.getJcicZ446Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ446T = jcicZ446ReposHist.findByJcicZ446Id(jcicZ446.getJcicZ446Id());
    else 
      jcicZ446T = jcicZ446Repos.findByJcicZ446Id(jcicZ446.getJcicZ446Id());
    return jcicZ446T.isPresent() ? jcicZ446T.get() : null;
  }

  @Override
  public JcicZ446 insert(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ446.getJcicZ446Id());
    if (this.findById(jcicZ446.getJcicZ446Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ446.setCreateEmpNo(empNot);

    if(jcicZ446.getLastUpdateEmpNo() == null || jcicZ446.getLastUpdateEmpNo().isEmpty())
      jcicZ446.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ446ReposDay.saveAndFlush(jcicZ446);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ446ReposMon.saveAndFlush(jcicZ446);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ446ReposHist.saveAndFlush(jcicZ446);
    else 
    return jcicZ446Repos.saveAndFlush(jcicZ446);
  }

  @Override
  public JcicZ446 update(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ446.getJcicZ446Id());
    if (!empNot.isEmpty())
      jcicZ446.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ446ReposDay.saveAndFlush(jcicZ446);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ446ReposMon.saveAndFlush(jcicZ446);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ446ReposHist.saveAndFlush(jcicZ446);
    else 
    return jcicZ446Repos.saveAndFlush(jcicZ446);
  }

  @Override
  public JcicZ446 update2(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ446.getJcicZ446Id());
    if (!empNot.isEmpty())
      jcicZ446.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ446ReposDay.saveAndFlush(jcicZ446);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ446ReposMon.saveAndFlush(jcicZ446);
    else if (dbName.equals(ContentName.onHist))
        jcicZ446ReposHist.saveAndFlush(jcicZ446);
    else 
      jcicZ446Repos.saveAndFlush(jcicZ446);	
    return this.findById(jcicZ446.getJcicZ446Id());
  }

  @Override
  public void delete(JcicZ446 jcicZ446, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ446.getJcicZ446Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ446ReposDay.delete(jcicZ446);	
      jcicZ446ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ446ReposMon.delete(jcicZ446);	
      jcicZ446ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ446ReposHist.delete(jcicZ446);
      jcicZ446ReposHist.flush();
    }
    else {
      jcicZ446Repos.delete(jcicZ446);
      jcicZ446Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ446> jcicZ446, TitaVo... titaVo) throws DBException {
    if (jcicZ446 == null || jcicZ446.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ446 t : jcicZ446){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ446 = jcicZ446ReposDay.saveAll(jcicZ446);	
      jcicZ446ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ446 = jcicZ446ReposMon.saveAll(jcicZ446);	
      jcicZ446ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ446 = jcicZ446ReposHist.saveAll(jcicZ446);
      jcicZ446ReposHist.flush();
    }
    else {
      jcicZ446 = jcicZ446Repos.saveAll(jcicZ446);
      jcicZ446Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ446> jcicZ446, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ446 == null || jcicZ446.size() == 0)
      throw new DBException(6);

    for (JcicZ446 t : jcicZ446) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ446 = jcicZ446ReposDay.saveAll(jcicZ446);	
      jcicZ446ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ446 = jcicZ446ReposMon.saveAll(jcicZ446);	
      jcicZ446ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ446 = jcicZ446ReposHist.saveAll(jcicZ446);
      jcicZ446ReposHist.flush();
    }
    else {
      jcicZ446 = jcicZ446Repos.saveAll(jcicZ446);
      jcicZ446Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ446> jcicZ446, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ446 == null || jcicZ446.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ446ReposDay.deleteAll(jcicZ446);	
      jcicZ446ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ446ReposMon.deleteAll(jcicZ446);	
      jcicZ446ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ446ReposHist.deleteAll(jcicZ446);
      jcicZ446ReposHist.flush();
    }
    else {
      jcicZ446Repos.deleteAll(jcicZ446);
      jcicZ446Repos.flush();
    }
  }

}
