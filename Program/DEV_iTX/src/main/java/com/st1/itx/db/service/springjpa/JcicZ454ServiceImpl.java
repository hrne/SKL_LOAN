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
import com.st1.itx.db.domain.JcicZ454;
import com.st1.itx.db.domain.JcicZ454Id;
import com.st1.itx.db.repository.online.JcicZ454Repository;
import com.st1.itx.db.repository.day.JcicZ454RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ454RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ454RepositoryHist;
import com.st1.itx.db.service.JcicZ454Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ454Service")
@Repository
public class JcicZ454ServiceImpl extends ASpringJpaParm implements JcicZ454Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ454Repository jcicZ454Repos;

  @Autowired
  private JcicZ454RepositoryDay jcicZ454ReposDay;

  @Autowired
  private JcicZ454RepositoryMon jcicZ454ReposMon;

  @Autowired
  private JcicZ454RepositoryHist jcicZ454ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ454Repos);
    org.junit.Assert.assertNotNull(jcicZ454ReposDay);
    org.junit.Assert.assertNotNull(jcicZ454ReposMon);
    org.junit.Assert.assertNotNull(jcicZ454ReposHist);
  }

  @Override
  public JcicZ454 findById(JcicZ454Id jcicZ454Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ454Id);
    Optional<JcicZ454> jcicZ454 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ454 = jcicZ454ReposDay.findById(jcicZ454Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ454 = jcicZ454ReposMon.findById(jcicZ454Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ454 = jcicZ454ReposHist.findById(jcicZ454Id);
    else 
      jcicZ454 = jcicZ454Repos.findById(jcicZ454Id);
    JcicZ454 obj = jcicZ454.isPresent() ? jcicZ454.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ454> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ454> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode", "MaxMainCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode", "MaxMainCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ454ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ454ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ454ReposHist.findAll(pageable);
    else 
      slice = jcicZ454Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ454> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ454> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ454ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ454ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ454ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ454Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ454> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ454> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ454ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ454ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ454ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ454Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ454> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ454> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ454ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ454ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ454ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ454Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ454> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ454> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3 + " maxMainCode_4 : " +  maxMainCode_4);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ454ReposDay.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ454ReposMon.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ454ReposHist.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);
    else 
      slice = jcicZ454Repos.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ454 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ454> jcicZ454T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ454T = jcicZ454ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ454T = jcicZ454ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ454T = jcicZ454ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ454T = jcicZ454Repos.findTopByUkeyIs(ukey_0);

    return jcicZ454T.isPresent() ? jcicZ454T.get() : null;
  }

  @Override
  public JcicZ454 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3 + " maxMainCode_4 : " +  maxMainCode_4);
    Optional<JcicZ454> jcicZ454T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ454T = jcicZ454ReposDay.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);
    else if (dbName.equals(ContentName.onMon))
      jcicZ454T = jcicZ454ReposMon.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);
    else if (dbName.equals(ContentName.onHist))
      jcicZ454T = jcicZ454ReposHist.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);
    else 
      jcicZ454T = jcicZ454Repos.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, maxMainCode_4);

    return jcicZ454T.isPresent() ? jcicZ454T.get() : null;
  }

  @Override
  public JcicZ454 holdById(JcicZ454Id jcicZ454Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ454Id);
    Optional<JcicZ454> jcicZ454 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ454 = jcicZ454ReposDay.findByJcicZ454Id(jcicZ454Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ454 = jcicZ454ReposMon.findByJcicZ454Id(jcicZ454Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ454 = jcicZ454ReposHist.findByJcicZ454Id(jcicZ454Id);
    else 
      jcicZ454 = jcicZ454Repos.findByJcicZ454Id(jcicZ454Id);
    return jcicZ454.isPresent() ? jcicZ454.get() : null;
  }

  @Override
  public JcicZ454 holdById(JcicZ454 jcicZ454, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ454.getJcicZ454Id());
    Optional<JcicZ454> jcicZ454T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ454T = jcicZ454ReposDay.findByJcicZ454Id(jcicZ454.getJcicZ454Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ454T = jcicZ454ReposMon.findByJcicZ454Id(jcicZ454.getJcicZ454Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ454T = jcicZ454ReposHist.findByJcicZ454Id(jcicZ454.getJcicZ454Id());
    else 
      jcicZ454T = jcicZ454Repos.findByJcicZ454Id(jcicZ454.getJcicZ454Id());
    return jcicZ454T.isPresent() ? jcicZ454T.get() : null;
  }

  @Override
  public JcicZ454 insert(JcicZ454 jcicZ454, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ454.getJcicZ454Id());
    if (this.findById(jcicZ454.getJcicZ454Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ454.setCreateEmpNo(empNot);

    if(jcicZ454.getLastUpdateEmpNo() == null || jcicZ454.getLastUpdateEmpNo().isEmpty())
      jcicZ454.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ454ReposDay.saveAndFlush(jcicZ454);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ454ReposMon.saveAndFlush(jcicZ454);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ454ReposHist.saveAndFlush(jcicZ454);
    else 
    return jcicZ454Repos.saveAndFlush(jcicZ454);
  }

  @Override
  public JcicZ454 update(JcicZ454 jcicZ454, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ454.getJcicZ454Id());
    if (!empNot.isEmpty())
      jcicZ454.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ454ReposDay.saveAndFlush(jcicZ454);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ454ReposMon.saveAndFlush(jcicZ454);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ454ReposHist.saveAndFlush(jcicZ454);
    else 
    return jcicZ454Repos.saveAndFlush(jcicZ454);
  }

  @Override
  public JcicZ454 update2(JcicZ454 jcicZ454, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ454.getJcicZ454Id());
    if (!empNot.isEmpty())
      jcicZ454.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ454ReposDay.saveAndFlush(jcicZ454);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ454ReposMon.saveAndFlush(jcicZ454);
    else if (dbName.equals(ContentName.onHist))
        jcicZ454ReposHist.saveAndFlush(jcicZ454);
    else 
      jcicZ454Repos.saveAndFlush(jcicZ454);	
    return this.findById(jcicZ454.getJcicZ454Id());
  }

  @Override
  public void delete(JcicZ454 jcicZ454, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ454.getJcicZ454Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ454ReposDay.delete(jcicZ454);	
      jcicZ454ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ454ReposMon.delete(jcicZ454);	
      jcicZ454ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ454ReposHist.delete(jcicZ454);
      jcicZ454ReposHist.flush();
    }
    else {
      jcicZ454Repos.delete(jcicZ454);
      jcicZ454Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ454> jcicZ454, TitaVo... titaVo) throws DBException {
    if (jcicZ454 == null || jcicZ454.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ454 t : jcicZ454){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ454 = jcicZ454ReposDay.saveAll(jcicZ454);	
      jcicZ454ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ454 = jcicZ454ReposMon.saveAll(jcicZ454);	
      jcicZ454ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ454 = jcicZ454ReposHist.saveAll(jcicZ454);
      jcicZ454ReposHist.flush();
    }
    else {
      jcicZ454 = jcicZ454Repos.saveAll(jcicZ454);
      jcicZ454Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ454> jcicZ454, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ454 == null || jcicZ454.size() == 0)
      throw new DBException(6);

    for (JcicZ454 t : jcicZ454) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ454 = jcicZ454ReposDay.saveAll(jcicZ454);	
      jcicZ454ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ454 = jcicZ454ReposMon.saveAll(jcicZ454);	
      jcicZ454ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ454 = jcicZ454ReposHist.saveAll(jcicZ454);
      jcicZ454ReposHist.flush();
    }
    else {
      jcicZ454 = jcicZ454Repos.saveAll(jcicZ454);
      jcicZ454Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ454> jcicZ454, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ454 == null || jcicZ454.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ454ReposDay.deleteAll(jcicZ454);	
      jcicZ454ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ454ReposMon.deleteAll(jcicZ454);	
      jcicZ454ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ454ReposHist.deleteAll(jcicZ454);
      jcicZ454ReposHist.flush();
    }
    else {
      jcicZ454Repos.deleteAll(jcicZ454);
      jcicZ454Repos.flush();
    }
  }

}
