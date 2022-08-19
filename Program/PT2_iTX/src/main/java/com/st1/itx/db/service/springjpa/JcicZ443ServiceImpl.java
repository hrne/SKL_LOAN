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
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ443Id;
import com.st1.itx.db.repository.online.JcicZ443Repository;
import com.st1.itx.db.repository.day.JcicZ443RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ443RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ443RepositoryHist;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ443Service")
@Repository
public class JcicZ443ServiceImpl extends ASpringJpaParm implements JcicZ443Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ443Repository jcicZ443Repos;

  @Autowired
  private JcicZ443RepositoryDay jcicZ443ReposDay;

  @Autowired
  private JcicZ443RepositoryMon jcicZ443ReposMon;

  @Autowired
  private JcicZ443RepositoryHist jcicZ443ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ443Repos);
    org.junit.Assert.assertNotNull(jcicZ443ReposDay);
    org.junit.Assert.assertNotNull(jcicZ443ReposMon);
    org.junit.Assert.assertNotNull(jcicZ443ReposHist);
  }

  @Override
  public JcicZ443 findById(JcicZ443Id jcicZ443Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ443Id);
    Optional<JcicZ443> jcicZ443 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443 = jcicZ443ReposDay.findById(jcicZ443Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ443 = jcicZ443ReposMon.findById(jcicZ443Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ443 = jcicZ443ReposHist.findById(jcicZ443Id);
    else 
      jcicZ443 = jcicZ443Repos.findById(jcicZ443Id);
    JcicZ443 obj = jcicZ443.isPresent() ? jcicZ443.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ443> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ443> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode", "MaxMainCode", "Account"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode", "MaxMainCode", "Account"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ443ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ443ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ443ReposHist.findAll(pageable);
    else 
      slice = jcicZ443Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ443> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ443> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ443ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ443ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ443ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ443Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ443> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ443> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ443ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ443ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ443ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ443Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ443> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ443> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ443ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ443ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ443ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ443Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ443> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ443> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3 + " maxMainCode_4 : " +  maxMainCode_4);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ443ReposDay.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ443ReposMon.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ443ReposHist.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);
    else 
      slice = jcicZ443Repos.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ443 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ443> jcicZ443T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443T = jcicZ443ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ443T = jcicZ443ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ443T = jcicZ443ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ443T = jcicZ443Repos.findTopByUkeyIs(ukey_0);

    return jcicZ443T.isPresent() ? jcicZ443T.get() : null;
  }

  @Override
  public JcicZ443 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3 + " maxMainCode_4 : " +  maxMainCode_4);
    Optional<JcicZ443> jcicZ443T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443T = jcicZ443ReposDay.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);
    else if (dbName.equals(ContentName.onMon))
      jcicZ443T = jcicZ443ReposMon.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);
    else if (dbName.equals(ContentName.onHist))
      jcicZ443T = jcicZ443ReposHist.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);
    else 
      jcicZ443T = jcicZ443Repos.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);

    return jcicZ443T.isPresent() ? jcicZ443T.get() : null;
  }

  @Override
  public JcicZ443 holdById(JcicZ443Id jcicZ443Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ443Id);
    Optional<JcicZ443> jcicZ443 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443 = jcicZ443ReposDay.findByJcicZ443Id(jcicZ443Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ443 = jcicZ443ReposMon.findByJcicZ443Id(jcicZ443Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ443 = jcicZ443ReposHist.findByJcicZ443Id(jcicZ443Id);
    else 
      jcicZ443 = jcicZ443Repos.findByJcicZ443Id(jcicZ443Id);
    return jcicZ443.isPresent() ? jcicZ443.get() : null;
  }

  @Override
  public JcicZ443 holdById(JcicZ443 jcicZ443, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ443.getJcicZ443Id());
    Optional<JcicZ443> jcicZ443T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443T = jcicZ443ReposDay.findByJcicZ443Id(jcicZ443.getJcicZ443Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ443T = jcicZ443ReposMon.findByJcicZ443Id(jcicZ443.getJcicZ443Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ443T = jcicZ443ReposHist.findByJcicZ443Id(jcicZ443.getJcicZ443Id());
    else 
      jcicZ443T = jcicZ443Repos.findByJcicZ443Id(jcicZ443.getJcicZ443Id());
    return jcicZ443T.isPresent() ? jcicZ443T.get() : null;
  }

  @Override
  public JcicZ443 insert(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ443.getJcicZ443Id());
    if (this.findById(jcicZ443.getJcicZ443Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ443.setCreateEmpNo(empNot);

    if(jcicZ443.getLastUpdateEmpNo() == null || jcicZ443.getLastUpdateEmpNo().isEmpty())
      jcicZ443.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ443ReposDay.saveAndFlush(jcicZ443);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ443ReposMon.saveAndFlush(jcicZ443);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ443ReposHist.saveAndFlush(jcicZ443);
    else 
    return jcicZ443Repos.saveAndFlush(jcicZ443);
  }

  @Override
  public JcicZ443 update(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ443.getJcicZ443Id());
    if (!empNot.isEmpty())
      jcicZ443.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ443ReposDay.saveAndFlush(jcicZ443);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ443ReposMon.saveAndFlush(jcicZ443);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ443ReposHist.saveAndFlush(jcicZ443);
    else 
    return jcicZ443Repos.saveAndFlush(jcicZ443);
  }

  @Override
  public JcicZ443 update2(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ443.getJcicZ443Id());
    if (!empNot.isEmpty())
      jcicZ443.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ443ReposDay.saveAndFlush(jcicZ443);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ443ReposMon.saveAndFlush(jcicZ443);
    else if (dbName.equals(ContentName.onHist))
        jcicZ443ReposHist.saveAndFlush(jcicZ443);
    else 
      jcicZ443Repos.saveAndFlush(jcicZ443);	
    return this.findById(jcicZ443.getJcicZ443Id());
  }

  @Override
  public void delete(JcicZ443 jcicZ443, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ443.getJcicZ443Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ443ReposDay.delete(jcicZ443);	
      jcicZ443ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443ReposMon.delete(jcicZ443);	
      jcicZ443ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443ReposHist.delete(jcicZ443);
      jcicZ443ReposHist.flush();
    }
    else {
      jcicZ443Repos.delete(jcicZ443);
      jcicZ443Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException {
    if (jcicZ443 == null || jcicZ443.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ443 t : jcicZ443){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ443 = jcicZ443ReposDay.saveAll(jcicZ443);	
      jcicZ443ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443 = jcicZ443ReposMon.saveAll(jcicZ443);	
      jcicZ443ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443 = jcicZ443ReposHist.saveAll(jcicZ443);
      jcicZ443ReposHist.flush();
    }
    else {
      jcicZ443 = jcicZ443Repos.saveAll(jcicZ443);
      jcicZ443Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ443 == null || jcicZ443.size() == 0)
      throw new DBException(6);

    for (JcicZ443 t : jcicZ443) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ443 = jcicZ443ReposDay.saveAll(jcicZ443);	
      jcicZ443ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443 = jcicZ443ReposMon.saveAll(jcicZ443);	
      jcicZ443ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443 = jcicZ443ReposHist.saveAll(jcicZ443);
      jcicZ443ReposHist.flush();
    }
    else {
      jcicZ443 = jcicZ443Repos.saveAll(jcicZ443);
      jcicZ443Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ443> jcicZ443, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ443 == null || jcicZ443.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ443ReposDay.deleteAll(jcicZ443);	
      jcicZ443ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443ReposMon.deleteAll(jcicZ443);	
      jcicZ443ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443ReposHist.deleteAll(jcicZ443);
      jcicZ443ReposHist.flush();
    }
    else {
      jcicZ443Repos.deleteAll(jcicZ443);
      jcicZ443Repos.flush();
    }
  }

}
