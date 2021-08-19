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
import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.domain.JcicZ056Id;
import com.st1.itx.db.repository.online.JcicZ056Repository;
import com.st1.itx.db.repository.day.JcicZ056RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ056RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ056RepositoryHist;
import com.st1.itx.db.service.JcicZ056Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ056Service")
@Repository
public class JcicZ056ServiceImpl extends ASpringJpaParm implements JcicZ056Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ056Repository jcicZ056Repos;

  @Autowired
  private JcicZ056RepositoryDay jcicZ056ReposDay;

  @Autowired
  private JcicZ056RepositoryMon jcicZ056ReposMon;

  @Autowired
  private JcicZ056RepositoryHist jcicZ056ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ056Repos);
    org.junit.Assert.assertNotNull(jcicZ056ReposDay);
    org.junit.Assert.assertNotNull(jcicZ056ReposMon);
    org.junit.Assert.assertNotNull(jcicZ056ReposHist);
  }

  @Override
  public JcicZ056 findById(JcicZ056Id jcicZ056Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ056Id);
    Optional<JcicZ056> jcicZ056 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ056 = jcicZ056ReposDay.findById(jcicZ056Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ056 = jcicZ056ReposMon.findById(jcicZ056Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ056 = jcicZ056ReposHist.findById(jcicZ056Id);
    else 
      jcicZ056 = jcicZ056Repos.findById(jcicZ056Id);
    JcicZ056 obj = jcicZ056.isPresent() ? jcicZ056.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ056> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ056> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "CaseStatus", "ClaimDate", "CourtCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "CaseStatus", "ClaimDate", "CourtCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ056ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ056ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ056ReposHist.findAll(pageable);
    else 
      slice = jcicZ056Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ056> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ056> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ056ReposDay.findAllByCustIdIsOrderByCustIdAscClaimDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ056ReposMon.findAllByCustIdIsOrderByCustIdAscClaimDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ056ReposHist.findAllByCustIdIsOrderByCustIdAscClaimDateDesc(custId_0, pageable);
    else 
      slice = jcicZ056Repos.findAllByCustIdIsOrderByCustIdAscClaimDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ056> ClaimDateEq(int claimDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ056> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ClaimDateEq " + dbName + " : " + "claimDate_0 : " + claimDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ056ReposDay.findAllByClaimDateIsOrderByCustIdAscClaimDateDesc(claimDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ056ReposMon.findAllByClaimDateIsOrderByCustIdAscClaimDateDesc(claimDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ056ReposHist.findAllByClaimDateIsOrderByCustIdAscClaimDateDesc(claimDate_0, pageable);
    else 
      slice = jcicZ056Repos.findAllByClaimDateIsOrderByCustIdAscClaimDateDesc(claimDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ056> CustRcEq(String custId_0, int claimDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ056> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " claimDate_1 : " +  claimDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ056ReposDay.findAllByCustIdIsAndClaimDateIsOrderByCustIdAscClaimDateDesc(custId_0, claimDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ056ReposMon.findAllByCustIdIsAndClaimDateIsOrderByCustIdAscClaimDateDesc(custId_0, claimDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ056ReposHist.findAllByCustIdIsAndClaimDateIsOrderByCustIdAscClaimDateDesc(custId_0, claimDate_1, pageable);
    else 
      slice = jcicZ056Repos.findAllByCustIdIsAndClaimDateIsOrderByCustIdAscClaimDateDesc(custId_0, claimDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ056> CheckCaseStatus(String submitKey_0, String custId_1, int claimDate_2, String courtCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ056> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CheckCaseStatus " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " claimDate_2 : " +  claimDate_2 + " courtCode_3 : " +  courtCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ056ReposDay.findAllBySubmitKeyIsAndCustIdIsAndClaimDateIsAndCourtCodeIsOrderByCustIdAscClaimDateDescCaseStatusAsc(submitKey_0, custId_1, claimDate_2, courtCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ056ReposMon.findAllBySubmitKeyIsAndCustIdIsAndClaimDateIsAndCourtCodeIsOrderByCustIdAscClaimDateDescCaseStatusAsc(submitKey_0, custId_1, claimDate_2, courtCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ056ReposHist.findAllBySubmitKeyIsAndCustIdIsAndClaimDateIsAndCourtCodeIsOrderByCustIdAscClaimDateDescCaseStatusAsc(submitKey_0, custId_1, claimDate_2, courtCode_3, pageable);
    else 
      slice = jcicZ056Repos.findAllBySubmitKeyIsAndCustIdIsAndClaimDateIsAndCourtCodeIsOrderByCustIdAscClaimDateDescCaseStatusAsc(submitKey_0, custId_1, claimDate_2, courtCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ056> otherEq(String submitKey_0, String custId_1, String caseStatus_2, int claimDate_3, String courtCode_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ056> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " caseStatus_2 : " +  caseStatus_2 + " claimDate_3 : " +  claimDate_3 + " courtCode_4 : " +  courtCode_4);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ056ReposDay.findAllBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ056ReposMon.findAllBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ056ReposHist.findAllBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4, pageable);
    else 
      slice = jcicZ056Repos.findAllBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ056 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ056> jcicZ056T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ056T = jcicZ056ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ056T = jcicZ056ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ056T = jcicZ056ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ056T = jcicZ056Repos.findTopByUkeyIs(ukey_0);

    return jcicZ056T.isPresent() ? jcicZ056T.get() : null;
  }

  @Override
  public JcicZ056 otherFirst(String submitKey_0, String custId_1, String caseStatus_2, int claimDate_3, String courtCode_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " caseStatus_2 : " +  caseStatus_2 + " claimDate_3 : " +  claimDate_3 + " courtCode_4 : " +  courtCode_4);
    Optional<JcicZ056> jcicZ056T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ056T = jcicZ056ReposDay.findTopBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4);
    else if (dbName.equals(ContentName.onMon))
      jcicZ056T = jcicZ056ReposMon.findTopBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4);
    else if (dbName.equals(ContentName.onHist))
      jcicZ056T = jcicZ056ReposHist.findTopBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4);
    else 
      jcicZ056T = jcicZ056Repos.findTopBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4);

    return jcicZ056T.isPresent() ? jcicZ056T.get() : null;
  }

  @Override
  public JcicZ056 holdById(JcicZ056Id jcicZ056Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ056Id);
    Optional<JcicZ056> jcicZ056 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ056 = jcicZ056ReposDay.findByJcicZ056Id(jcicZ056Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ056 = jcicZ056ReposMon.findByJcicZ056Id(jcicZ056Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ056 = jcicZ056ReposHist.findByJcicZ056Id(jcicZ056Id);
    else 
      jcicZ056 = jcicZ056Repos.findByJcicZ056Id(jcicZ056Id);
    return jcicZ056.isPresent() ? jcicZ056.get() : null;
  }

  @Override
  public JcicZ056 holdById(JcicZ056 jcicZ056, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ056.getJcicZ056Id());
    Optional<JcicZ056> jcicZ056T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ056T = jcicZ056ReposDay.findByJcicZ056Id(jcicZ056.getJcicZ056Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ056T = jcicZ056ReposMon.findByJcicZ056Id(jcicZ056.getJcicZ056Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ056T = jcicZ056ReposHist.findByJcicZ056Id(jcicZ056.getJcicZ056Id());
    else 
      jcicZ056T = jcicZ056Repos.findByJcicZ056Id(jcicZ056.getJcicZ056Id());
    return jcicZ056T.isPresent() ? jcicZ056T.get() : null;
  }

  @Override
  public JcicZ056 insert(JcicZ056 jcicZ056, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ056.getJcicZ056Id());
    if (this.findById(jcicZ056.getJcicZ056Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ056.setCreateEmpNo(empNot);

    if(jcicZ056.getLastUpdateEmpNo() == null || jcicZ056.getLastUpdateEmpNo().isEmpty())
      jcicZ056.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ056ReposDay.saveAndFlush(jcicZ056);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ056ReposMon.saveAndFlush(jcicZ056);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ056ReposHist.saveAndFlush(jcicZ056);
    else 
    return jcicZ056Repos.saveAndFlush(jcicZ056);
  }

  @Override
  public JcicZ056 update(JcicZ056 jcicZ056, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ056.getJcicZ056Id());
    if (!empNot.isEmpty())
      jcicZ056.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ056ReposDay.saveAndFlush(jcicZ056);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ056ReposMon.saveAndFlush(jcicZ056);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ056ReposHist.saveAndFlush(jcicZ056);
    else 
    return jcicZ056Repos.saveAndFlush(jcicZ056);
  }

  @Override
  public JcicZ056 update2(JcicZ056 jcicZ056, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ056.getJcicZ056Id());
    if (!empNot.isEmpty())
      jcicZ056.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ056ReposDay.saveAndFlush(jcicZ056);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ056ReposMon.saveAndFlush(jcicZ056);
    else if (dbName.equals(ContentName.onHist))
        jcicZ056ReposHist.saveAndFlush(jcicZ056);
    else 
      jcicZ056Repos.saveAndFlush(jcicZ056);	
    return this.findById(jcicZ056.getJcicZ056Id());
  }

  @Override
  public void delete(JcicZ056 jcicZ056, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ056.getJcicZ056Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ056ReposDay.delete(jcicZ056);	
      jcicZ056ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ056ReposMon.delete(jcicZ056);	
      jcicZ056ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ056ReposHist.delete(jcicZ056);
      jcicZ056ReposHist.flush();
    }
    else {
      jcicZ056Repos.delete(jcicZ056);
      jcicZ056Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ056> jcicZ056, TitaVo... titaVo) throws DBException {
    if (jcicZ056 == null || jcicZ056.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ056 t : jcicZ056){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ056 = jcicZ056ReposDay.saveAll(jcicZ056);	
      jcicZ056ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ056 = jcicZ056ReposMon.saveAll(jcicZ056);	
      jcicZ056ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ056 = jcicZ056ReposHist.saveAll(jcicZ056);
      jcicZ056ReposHist.flush();
    }
    else {
      jcicZ056 = jcicZ056Repos.saveAll(jcicZ056);
      jcicZ056Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ056> jcicZ056, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ056 == null || jcicZ056.size() == 0)
      throw new DBException(6);

    for (JcicZ056 t : jcicZ056) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ056 = jcicZ056ReposDay.saveAll(jcicZ056);	
      jcicZ056ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ056 = jcicZ056ReposMon.saveAll(jcicZ056);	
      jcicZ056ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ056 = jcicZ056ReposHist.saveAll(jcicZ056);
      jcicZ056ReposHist.flush();
    }
    else {
      jcicZ056 = jcicZ056Repos.saveAll(jcicZ056);
      jcicZ056Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ056> jcicZ056, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ056 == null || jcicZ056.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ056ReposDay.deleteAll(jcicZ056);	
      jcicZ056ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ056ReposMon.deleteAll(jcicZ056);	
      jcicZ056ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ056ReposHist.deleteAll(jcicZ056);
      jcicZ056ReposHist.flush();
    }
    else {
      jcicZ056Repos.deleteAll(jcicZ056);
      jcicZ056Repos.flush();
    }
  }

}
