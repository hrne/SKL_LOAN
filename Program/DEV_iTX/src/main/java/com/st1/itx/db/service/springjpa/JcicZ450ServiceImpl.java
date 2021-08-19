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
import com.st1.itx.db.domain.JcicZ450;
import com.st1.itx.db.domain.JcicZ450Id;
import com.st1.itx.db.repository.online.JcicZ450Repository;
import com.st1.itx.db.repository.day.JcicZ450RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ450RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ450RepositoryHist;
import com.st1.itx.db.service.JcicZ450Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ450Service")
@Repository
public class JcicZ450ServiceImpl extends ASpringJpaParm implements JcicZ450Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ450Repository jcicZ450Repos;

  @Autowired
  private JcicZ450RepositoryDay jcicZ450ReposDay;

  @Autowired
  private JcicZ450RepositoryMon jcicZ450ReposMon;

  @Autowired
  private JcicZ450RepositoryHist jcicZ450ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ450Repos);
    org.junit.Assert.assertNotNull(jcicZ450ReposDay);
    org.junit.Assert.assertNotNull(jcicZ450ReposMon);
    org.junit.Assert.assertNotNull(jcicZ450ReposHist);
  }

  @Override
  public JcicZ450 findById(JcicZ450Id jcicZ450Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ450Id);
    Optional<JcicZ450> jcicZ450 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ450 = jcicZ450ReposDay.findById(jcicZ450Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ450 = jcicZ450ReposMon.findById(jcicZ450Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ450 = jcicZ450ReposHist.findById(jcicZ450Id);
    else 
      jcicZ450 = jcicZ450Repos.findById(jcicZ450Id);
    JcicZ450 obj = jcicZ450.isPresent() ? jcicZ450.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ450> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ450> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode", "PayDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "CourtCode", "PayDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ450ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ450ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ450ReposHist.findAll(pageable);
    else 
      slice = jcicZ450Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ450> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ450> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ450ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ450ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ450ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ450Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ450> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ450> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ450ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ450ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ450ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ450Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ450> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ450> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ450ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ450ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ450ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ450Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ450> otherEq(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int payDate_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ450> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3 + " payDate_4 : " +  payDate_4);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ450ReposDay.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndPayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, payDate_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ450ReposMon.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndPayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, payDate_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ450ReposHist.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndPayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, payDate_4, pageable);
    else 
      slice = jcicZ450Repos.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndPayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, payDate_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ450 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ450> jcicZ450T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ450T = jcicZ450ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ450T = jcicZ450ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ450T = jcicZ450ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ450T = jcicZ450Repos.findTopByUkeyIs(ukey_0);

    return jcicZ450T.isPresent() ? jcicZ450T.get() : null;
  }

  @Override
  public JcicZ450 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int payDate_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " courtCode_3 : " +  courtCode_3 + " payDate_4 : " +  payDate_4);
    Optional<JcicZ450> jcicZ450T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ450T = jcicZ450ReposDay.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndPayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, payDate_4);
    else if (dbName.equals(ContentName.onMon))
      jcicZ450T = jcicZ450ReposMon.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndPayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, payDate_4);
    else if (dbName.equals(ContentName.onHist))
      jcicZ450T = jcicZ450ReposHist.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndPayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, payDate_4);
    else 
      jcicZ450T = jcicZ450Repos.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndPayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, courtCode_3, payDate_4);

    return jcicZ450T.isPresent() ? jcicZ450T.get() : null;
  }

  @Override
  public JcicZ450 holdById(JcicZ450Id jcicZ450Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ450Id);
    Optional<JcicZ450> jcicZ450 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ450 = jcicZ450ReposDay.findByJcicZ450Id(jcicZ450Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ450 = jcicZ450ReposMon.findByJcicZ450Id(jcicZ450Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ450 = jcicZ450ReposHist.findByJcicZ450Id(jcicZ450Id);
    else 
      jcicZ450 = jcicZ450Repos.findByJcicZ450Id(jcicZ450Id);
    return jcicZ450.isPresent() ? jcicZ450.get() : null;
  }

  @Override
  public JcicZ450 holdById(JcicZ450 jcicZ450, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ450.getJcicZ450Id());
    Optional<JcicZ450> jcicZ450T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ450T = jcicZ450ReposDay.findByJcicZ450Id(jcicZ450.getJcicZ450Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ450T = jcicZ450ReposMon.findByJcicZ450Id(jcicZ450.getJcicZ450Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ450T = jcicZ450ReposHist.findByJcicZ450Id(jcicZ450.getJcicZ450Id());
    else 
      jcicZ450T = jcicZ450Repos.findByJcicZ450Id(jcicZ450.getJcicZ450Id());
    return jcicZ450T.isPresent() ? jcicZ450T.get() : null;
  }

  @Override
  public JcicZ450 insert(JcicZ450 jcicZ450, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ450.getJcicZ450Id());
    if (this.findById(jcicZ450.getJcicZ450Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ450.setCreateEmpNo(empNot);

    if(jcicZ450.getLastUpdateEmpNo() == null || jcicZ450.getLastUpdateEmpNo().isEmpty())
      jcicZ450.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ450ReposDay.saveAndFlush(jcicZ450);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ450ReposMon.saveAndFlush(jcicZ450);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ450ReposHist.saveAndFlush(jcicZ450);
    else 
    return jcicZ450Repos.saveAndFlush(jcicZ450);
  }

  @Override
  public JcicZ450 update(JcicZ450 jcicZ450, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ450.getJcicZ450Id());
    if (!empNot.isEmpty())
      jcicZ450.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ450ReposDay.saveAndFlush(jcicZ450);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ450ReposMon.saveAndFlush(jcicZ450);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ450ReposHist.saveAndFlush(jcicZ450);
    else 
    return jcicZ450Repos.saveAndFlush(jcicZ450);
  }

  @Override
  public JcicZ450 update2(JcicZ450 jcicZ450, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ450.getJcicZ450Id());
    if (!empNot.isEmpty())
      jcicZ450.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ450ReposDay.saveAndFlush(jcicZ450);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ450ReposMon.saveAndFlush(jcicZ450);
    else if (dbName.equals(ContentName.onHist))
        jcicZ450ReposHist.saveAndFlush(jcicZ450);
    else 
      jcicZ450Repos.saveAndFlush(jcicZ450);	
    return this.findById(jcicZ450.getJcicZ450Id());
  }

  @Override
  public void delete(JcicZ450 jcicZ450, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ450.getJcicZ450Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ450ReposDay.delete(jcicZ450);	
      jcicZ450ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ450ReposMon.delete(jcicZ450);	
      jcicZ450ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ450ReposHist.delete(jcicZ450);
      jcicZ450ReposHist.flush();
    }
    else {
      jcicZ450Repos.delete(jcicZ450);
      jcicZ450Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ450> jcicZ450, TitaVo... titaVo) throws DBException {
    if (jcicZ450 == null || jcicZ450.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ450 t : jcicZ450){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ450 = jcicZ450ReposDay.saveAll(jcicZ450);	
      jcicZ450ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ450 = jcicZ450ReposMon.saveAll(jcicZ450);	
      jcicZ450ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ450 = jcicZ450ReposHist.saveAll(jcicZ450);
      jcicZ450ReposHist.flush();
    }
    else {
      jcicZ450 = jcicZ450Repos.saveAll(jcicZ450);
      jcicZ450Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ450> jcicZ450, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ450 == null || jcicZ450.size() == 0)
      throw new DBException(6);

    for (JcicZ450 t : jcicZ450) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ450 = jcicZ450ReposDay.saveAll(jcicZ450);	
      jcicZ450ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ450 = jcicZ450ReposMon.saveAll(jcicZ450);	
      jcicZ450ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ450 = jcicZ450ReposHist.saveAll(jcicZ450);
      jcicZ450ReposHist.flush();
    }
    else {
      jcicZ450 = jcicZ450Repos.saveAll(jcicZ450);
      jcicZ450Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ450> jcicZ450, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ450 == null || jcicZ450.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ450ReposDay.deleteAll(jcicZ450);	
      jcicZ450ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ450ReposMon.deleteAll(jcicZ450);	
      jcicZ450ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ450ReposHist.deleteAll(jcicZ450);
      jcicZ450ReposHist.flush();
    }
    else {
      jcicZ450Repos.deleteAll(jcicZ450);
      jcicZ450Repos.flush();
    }
  }

}
