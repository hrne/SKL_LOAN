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
import com.st1.itx.db.domain.JcicZ448;
import com.st1.itx.db.domain.JcicZ448Id;
import com.st1.itx.db.repository.online.JcicZ448Repository;
import com.st1.itx.db.repository.day.JcicZ448RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ448RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ448RepositoryHist;
import com.st1.itx.db.service.JcicZ448Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ448Service")
@Repository
public class JcicZ448ServiceImpl extends ASpringJpaParm implements JcicZ448Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ448Repository jcicZ448Repos;

  @Autowired
  private JcicZ448RepositoryDay jcicZ448ReposDay;

  @Autowired
  private JcicZ448RepositoryMon jcicZ448ReposMon;

  @Autowired
  private JcicZ448RepositoryHist jcicZ448ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ448Repos);
    org.junit.Assert.assertNotNull(jcicZ448ReposDay);
    org.junit.Assert.assertNotNull(jcicZ448ReposMon);
    org.junit.Assert.assertNotNull(jcicZ448ReposHist);
  }

  @Override
  public JcicZ448 findById(JcicZ448Id jcicZ448Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ448Id);
    Optional<JcicZ448> jcicZ448 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ448 = jcicZ448ReposDay.findById(jcicZ448Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ448 = jcicZ448ReposMon.findById(jcicZ448Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ448 = jcicZ448ReposHist.findById(jcicZ448Id);
    else 
      jcicZ448 = jcicZ448Repos.findById(jcicZ448Id);
    JcicZ448 obj = jcicZ448.isPresent() ? jcicZ448.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ448> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ448> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode", "MaxMainCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode", "MaxMainCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ448ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ448ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ448ReposHist.findAll(pageable);
    else 
      slice = jcicZ448Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ448> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ448> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ448ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ448ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ448ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ448Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ448> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ448> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ448ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ448ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ448ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ448Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ448> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ448> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ448ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ448ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ448ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ448Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ448> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ448> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3 + " maxMainCode_4 : " +  maxMainCode_4);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ448ReposDay.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ448ReposMon.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ448ReposHist.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);
    else 
      slice = jcicZ448Repos.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ448 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ448> jcicZ448T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ448T = jcicZ448ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ448T = jcicZ448ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ448T = jcicZ448ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ448T = jcicZ448Repos.findTopByUkeyIs(ukey_0);

    return jcicZ448T.isPresent() ? jcicZ448T.get() : null;
  }

  @Override
  public JcicZ448 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3 + " maxMainCode_4 : " +  maxMainCode_4);
    Optional<JcicZ448> jcicZ448T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ448T = jcicZ448ReposDay.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);
    else if (dbName.equals(ContentName.onMon))
      jcicZ448T = jcicZ448ReposMon.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);
    else if (dbName.equals(ContentName.onHist))
      jcicZ448T = jcicZ448ReposHist.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);
    else 
      jcicZ448T = jcicZ448Repos.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);

    return jcicZ448T.isPresent() ? jcicZ448T.get() : null;
  }

  @Override
  public JcicZ448 holdById(JcicZ448Id jcicZ448Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ448Id);
    Optional<JcicZ448> jcicZ448 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ448 = jcicZ448ReposDay.findByJcicZ448Id(jcicZ448Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ448 = jcicZ448ReposMon.findByJcicZ448Id(jcicZ448Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ448 = jcicZ448ReposHist.findByJcicZ448Id(jcicZ448Id);
    else 
      jcicZ448 = jcicZ448Repos.findByJcicZ448Id(jcicZ448Id);
    return jcicZ448.isPresent() ? jcicZ448.get() : null;
  }

  @Override
  public JcicZ448 holdById(JcicZ448 jcicZ448, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ448.getJcicZ448Id());
    Optional<JcicZ448> jcicZ448T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ448T = jcicZ448ReposDay.findByJcicZ448Id(jcicZ448.getJcicZ448Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ448T = jcicZ448ReposMon.findByJcicZ448Id(jcicZ448.getJcicZ448Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ448T = jcicZ448ReposHist.findByJcicZ448Id(jcicZ448.getJcicZ448Id());
    else 
      jcicZ448T = jcicZ448Repos.findByJcicZ448Id(jcicZ448.getJcicZ448Id());
    return jcicZ448T.isPresent() ? jcicZ448T.get() : null;
  }

  @Override
  public JcicZ448 insert(JcicZ448 jcicZ448, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ448.getJcicZ448Id());
    if (this.findById(jcicZ448.getJcicZ448Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ448.setCreateEmpNo(empNot);

    if(jcicZ448.getLastUpdateEmpNo() == null || jcicZ448.getLastUpdateEmpNo().isEmpty())
      jcicZ448.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ448ReposDay.saveAndFlush(jcicZ448);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ448ReposMon.saveAndFlush(jcicZ448);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ448ReposHist.saveAndFlush(jcicZ448);
    else 
    return jcicZ448Repos.saveAndFlush(jcicZ448);
  }

  @Override
  public JcicZ448 update(JcicZ448 jcicZ448, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ448.getJcicZ448Id());
    if (!empNot.isEmpty())
      jcicZ448.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ448ReposDay.saveAndFlush(jcicZ448);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ448ReposMon.saveAndFlush(jcicZ448);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ448ReposHist.saveAndFlush(jcicZ448);
    else 
    return jcicZ448Repos.saveAndFlush(jcicZ448);
  }

  @Override
  public JcicZ448 update2(JcicZ448 jcicZ448, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ448.getJcicZ448Id());
    if (!empNot.isEmpty())
      jcicZ448.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ448ReposDay.saveAndFlush(jcicZ448);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ448ReposMon.saveAndFlush(jcicZ448);
    else if (dbName.equals(ContentName.onHist))
        jcicZ448ReposHist.saveAndFlush(jcicZ448);
    else 
      jcicZ448Repos.saveAndFlush(jcicZ448);	
    return this.findById(jcicZ448.getJcicZ448Id());
  }

  @Override
  public void delete(JcicZ448 jcicZ448, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ448.getJcicZ448Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ448ReposDay.delete(jcicZ448);	
      jcicZ448ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ448ReposMon.delete(jcicZ448);	
      jcicZ448ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ448ReposHist.delete(jcicZ448);
      jcicZ448ReposHist.flush();
    }
    else {
      jcicZ448Repos.delete(jcicZ448);
      jcicZ448Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ448> jcicZ448, TitaVo... titaVo) throws DBException {
    if (jcicZ448 == null || jcicZ448.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ448 t : jcicZ448){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ448 = jcicZ448ReposDay.saveAll(jcicZ448);	
      jcicZ448ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ448 = jcicZ448ReposMon.saveAll(jcicZ448);	
      jcicZ448ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ448 = jcicZ448ReposHist.saveAll(jcicZ448);
      jcicZ448ReposHist.flush();
    }
    else {
      jcicZ448 = jcicZ448Repos.saveAll(jcicZ448);
      jcicZ448Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ448> jcicZ448, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ448 == null || jcicZ448.size() == 0)
      throw new DBException(6);

    for (JcicZ448 t : jcicZ448) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ448 = jcicZ448ReposDay.saveAll(jcicZ448);	
      jcicZ448ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ448 = jcicZ448ReposMon.saveAll(jcicZ448);	
      jcicZ448ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ448 = jcicZ448ReposHist.saveAll(jcicZ448);
      jcicZ448ReposHist.flush();
    }
    else {
      jcicZ448 = jcicZ448Repos.saveAll(jcicZ448);
      jcicZ448Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ448> jcicZ448, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ448 == null || jcicZ448.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ448ReposDay.deleteAll(jcicZ448);	
      jcicZ448ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ448ReposMon.deleteAll(jcicZ448);	
      jcicZ448ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ448ReposHist.deleteAll(jcicZ448);
      jcicZ448ReposHist.flush();
    }
    else {
      jcicZ448Repos.deleteAll(jcicZ448);
      jcicZ448Repos.flush();
    }
  }

}
