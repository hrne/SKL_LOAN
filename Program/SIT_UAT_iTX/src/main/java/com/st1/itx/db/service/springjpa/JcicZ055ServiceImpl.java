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
import com.st1.itx.db.domain.JcicZ055;
import com.st1.itx.db.domain.JcicZ055Id;
import com.st1.itx.db.repository.online.JcicZ055Repository;
import com.st1.itx.db.repository.day.JcicZ055RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ055RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ055RepositoryHist;
import com.st1.itx.db.service.JcicZ055Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ055Service")
@Repository
public class JcicZ055ServiceImpl extends ASpringJpaParm implements JcicZ055Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ055Repository jcicZ055Repos;

  @Autowired
  private JcicZ055RepositoryDay jcicZ055ReposDay;

  @Autowired
  private JcicZ055RepositoryMon jcicZ055ReposMon;

  @Autowired
  private JcicZ055RepositoryHist jcicZ055ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ055Repos);
    org.junit.Assert.assertNotNull(jcicZ055ReposDay);
    org.junit.Assert.assertNotNull(jcicZ055ReposMon);
    org.junit.Assert.assertNotNull(jcicZ055ReposHist);
  }

  @Override
  public JcicZ055 findById(JcicZ055Id jcicZ055Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ055Id);
    Optional<JcicZ055> jcicZ055 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ055 = jcicZ055ReposDay.findById(jcicZ055Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ055 = jcicZ055ReposMon.findById(jcicZ055Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ055 = jcicZ055ReposHist.findById(jcicZ055Id);
    else 
      jcicZ055 = jcicZ055Repos.findById(jcicZ055Id);
    JcicZ055 obj = jcicZ055.isPresent() ? jcicZ055.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ055> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ055> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "CaseStatus", "ClaimDate", "CourtCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "CaseStatus", "ClaimDate", "CourtCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ055ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ055ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ055ReposHist.findAll(pageable);
    else 
      slice = jcicZ055Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ055> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ055> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ055ReposDay.findAllByCustIdIsOrderByCustIdAscClaimDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ055ReposMon.findAllByCustIdIsOrderByCustIdAscClaimDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ055ReposHist.findAllByCustIdIsOrderByCustIdAscClaimDateDesc(custId_0, pageable);
    else 
      slice = jcicZ055Repos.findAllByCustIdIsOrderByCustIdAscClaimDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ055> ClaimDateEq(int claimDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ055> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ClaimDateEq " + dbName + " : " + "claimDate_0 : " + claimDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ055ReposDay.findAllByClaimDateIsOrderByCustIdAscClaimDateDesc(claimDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ055ReposMon.findAllByClaimDateIsOrderByCustIdAscClaimDateDesc(claimDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ055ReposHist.findAllByClaimDateIsOrderByCustIdAscClaimDateDesc(claimDate_0, pageable);
    else 
      slice = jcicZ055Repos.findAllByClaimDateIsOrderByCustIdAscClaimDateDesc(claimDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ055> CustRcEq(String custId_0, int claimDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ055> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " claimDate_1 : " +  claimDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ055ReposDay.findAllByCustIdIsAndClaimDateIsOrderByCustIdAscClaimDateDesc(custId_0, claimDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ055ReposMon.findAllByCustIdIsAndClaimDateIsOrderByCustIdAscClaimDateDesc(custId_0, claimDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ055ReposHist.findAllByCustIdIsAndClaimDateIsOrderByCustIdAscClaimDateDesc(custId_0, claimDate_1, pageable);
    else 
      slice = jcicZ055Repos.findAllByCustIdIsAndClaimDateIsOrderByCustIdAscClaimDateDesc(custId_0, claimDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ055> CheckCaseStatus(String submitKey_0, String custId_1, int claimDate_2, String courtCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ055> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CheckCaseStatus " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " claimDate_2 : " +  claimDate_2 + " courtCode_3 : " +  courtCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ055ReposDay.findAllBySubmitKeyIsAndCustIdIsAndClaimDateIsAndCourtCodeIsOrderByCustIdAscClaimDateDescCaseStatusAsc(submitKey_0, custId_1, claimDate_2, courtCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ055ReposMon.findAllBySubmitKeyIsAndCustIdIsAndClaimDateIsAndCourtCodeIsOrderByCustIdAscClaimDateDescCaseStatusAsc(submitKey_0, custId_1, claimDate_2, courtCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ055ReposHist.findAllBySubmitKeyIsAndCustIdIsAndClaimDateIsAndCourtCodeIsOrderByCustIdAscClaimDateDescCaseStatusAsc(submitKey_0, custId_1, claimDate_2, courtCode_3, pageable);
    else 
      slice = jcicZ055Repos.findAllBySubmitKeyIsAndCustIdIsAndClaimDateIsAndCourtCodeIsOrderByCustIdAscClaimDateDescCaseStatusAsc(submitKey_0, custId_1, claimDate_2, courtCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ055> otherEq(String submitKey_0, String custId_1, String caseStatus_2, int claimDate_3, String courtCode_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ055> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " caseStatus_2 : " +  caseStatus_2 + " claimDate_3 : " +  claimDate_3 + " courtCode_4 : " +  courtCode_4);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ055ReposDay.findAllBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ055ReposMon.findAllBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ055ReposHist.findAllBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4, pageable);
    else 
      slice = jcicZ055Repos.findAllBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ055 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ055> jcicZ055T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ055T = jcicZ055ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ055T = jcicZ055ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ055T = jcicZ055ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ055T = jcicZ055Repos.findTopByUkeyIs(ukey_0);

    return jcicZ055T.isPresent() ? jcicZ055T.get() : null;
  }

  @Override
  public JcicZ055 otherFirst(String submitKey_0, String custId_1, String caseStatus_2, int claimDate_3, String courtCode_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " caseStatus_2 : " +  caseStatus_2 + " claimDate_3 : " +  claimDate_3 + " courtCode_4 : " +  courtCode_4);
    Optional<JcicZ055> jcicZ055T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ055T = jcicZ055ReposDay.findTopBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4);
    else if (dbName.equals(ContentName.onMon))
      jcicZ055T = jcicZ055ReposMon.findTopBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4);
    else if (dbName.equals(ContentName.onHist))
      jcicZ055T = jcicZ055ReposHist.findTopBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4);
    else 
      jcicZ055T = jcicZ055Repos.findTopBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, caseStatus_2, claimDate_3, courtCode_4);

    return jcicZ055T.isPresent() ? jcicZ055T.get() : null;
  }

  @Override
  public JcicZ055 holdById(JcicZ055Id jcicZ055Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ055Id);
    Optional<JcicZ055> jcicZ055 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ055 = jcicZ055ReposDay.findByJcicZ055Id(jcicZ055Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ055 = jcicZ055ReposMon.findByJcicZ055Id(jcicZ055Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ055 = jcicZ055ReposHist.findByJcicZ055Id(jcicZ055Id);
    else 
      jcicZ055 = jcicZ055Repos.findByJcicZ055Id(jcicZ055Id);
    return jcicZ055.isPresent() ? jcicZ055.get() : null;
  }

  @Override
  public JcicZ055 holdById(JcicZ055 jcicZ055, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ055.getJcicZ055Id());
    Optional<JcicZ055> jcicZ055T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ055T = jcicZ055ReposDay.findByJcicZ055Id(jcicZ055.getJcicZ055Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ055T = jcicZ055ReposMon.findByJcicZ055Id(jcicZ055.getJcicZ055Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ055T = jcicZ055ReposHist.findByJcicZ055Id(jcicZ055.getJcicZ055Id());
    else 
      jcicZ055T = jcicZ055Repos.findByJcicZ055Id(jcicZ055.getJcicZ055Id());
    return jcicZ055T.isPresent() ? jcicZ055T.get() : null;
  }

  @Override
  public JcicZ055 insert(JcicZ055 jcicZ055, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ055.getJcicZ055Id());
    if (this.findById(jcicZ055.getJcicZ055Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ055.setCreateEmpNo(empNot);

    if(jcicZ055.getLastUpdateEmpNo() == null || jcicZ055.getLastUpdateEmpNo().isEmpty())
      jcicZ055.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ055ReposDay.saveAndFlush(jcicZ055);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ055ReposMon.saveAndFlush(jcicZ055);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ055ReposHist.saveAndFlush(jcicZ055);
    else 
    return jcicZ055Repos.saveAndFlush(jcicZ055);
  }

  @Override
  public JcicZ055 update(JcicZ055 jcicZ055, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ055.getJcicZ055Id());
    if (!empNot.isEmpty())
      jcicZ055.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ055ReposDay.saveAndFlush(jcicZ055);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ055ReposMon.saveAndFlush(jcicZ055);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ055ReposHist.saveAndFlush(jcicZ055);
    else 
    return jcicZ055Repos.saveAndFlush(jcicZ055);
  }

  @Override
  public JcicZ055 update2(JcicZ055 jcicZ055, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ055.getJcicZ055Id());
    if (!empNot.isEmpty())
      jcicZ055.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ055ReposDay.saveAndFlush(jcicZ055);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ055ReposMon.saveAndFlush(jcicZ055);
    else if (dbName.equals(ContentName.onHist))
        jcicZ055ReposHist.saveAndFlush(jcicZ055);
    else 
      jcicZ055Repos.saveAndFlush(jcicZ055);	
    return this.findById(jcicZ055.getJcicZ055Id());
  }

  @Override
  public void delete(JcicZ055 jcicZ055, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ055.getJcicZ055Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ055ReposDay.delete(jcicZ055);	
      jcicZ055ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ055ReposMon.delete(jcicZ055);	
      jcicZ055ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ055ReposHist.delete(jcicZ055);
      jcicZ055ReposHist.flush();
    }
    else {
      jcicZ055Repos.delete(jcicZ055);
      jcicZ055Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ055> jcicZ055, TitaVo... titaVo) throws DBException {
    if (jcicZ055 == null || jcicZ055.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ055 t : jcicZ055){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ055 = jcicZ055ReposDay.saveAll(jcicZ055);	
      jcicZ055ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ055 = jcicZ055ReposMon.saveAll(jcicZ055);	
      jcicZ055ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ055 = jcicZ055ReposHist.saveAll(jcicZ055);
      jcicZ055ReposHist.flush();
    }
    else {
      jcicZ055 = jcicZ055Repos.saveAll(jcicZ055);
      jcicZ055Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ055> jcicZ055, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ055 == null || jcicZ055.size() == 0)
      throw new DBException(6);

    for (JcicZ055 t : jcicZ055) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ055 = jcicZ055ReposDay.saveAll(jcicZ055);	
      jcicZ055ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ055 = jcicZ055ReposMon.saveAll(jcicZ055);	
      jcicZ055ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ055 = jcicZ055ReposHist.saveAll(jcicZ055);
      jcicZ055ReposHist.flush();
    }
    else {
      jcicZ055 = jcicZ055Repos.saveAll(jcicZ055);
      jcicZ055Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ055> jcicZ055, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ055 == null || jcicZ055.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ055ReposDay.deleteAll(jcicZ055);	
      jcicZ055ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ055ReposMon.deleteAll(jcicZ055);	
      jcicZ055ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ055ReposHist.deleteAll(jcicZ055);
      jcicZ055ReposHist.flush();
    }
    else {
      jcicZ055Repos.deleteAll(jcicZ055);
      jcicZ055Repos.flush();
    }
  }

}
