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
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ571Id;
import com.st1.itx.db.repository.online.JcicZ571Repository;
import com.st1.itx.db.repository.day.JcicZ571RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ571RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ571RepositoryHist;
import com.st1.itx.db.service.JcicZ571Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ571Service")
@Repository
public class JcicZ571ServiceImpl extends ASpringJpaParm implements JcicZ571Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ571Repository jcicZ571Repos;

  @Autowired
  private JcicZ571RepositoryDay jcicZ571ReposDay;

  @Autowired
  private JcicZ571RepositoryMon jcicZ571ReposMon;

  @Autowired
  private JcicZ571RepositoryHist jcicZ571ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ571Repos);
    org.junit.Assert.assertNotNull(jcicZ571ReposDay);
    org.junit.Assert.assertNotNull(jcicZ571ReposMon);
    org.junit.Assert.assertNotNull(jcicZ571ReposHist);
  }

  @Override
  public JcicZ571 findById(JcicZ571Id jcicZ571Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ571Id);
    Optional<JcicZ571> jcicZ571 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ571 = jcicZ571ReposDay.findById(jcicZ571Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ571 = jcicZ571ReposMon.findById(jcicZ571Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ571 = jcicZ571ReposHist.findById(jcicZ571Id);
    else 
      jcicZ571 = jcicZ571Repos.findById(jcicZ571Id);
    JcicZ571 obj = jcicZ571.isPresent() ? jcicZ571.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ571> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ571> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "BankId"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "BankId"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ571ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ571ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ571ReposHist.findAll(pageable);
    else 
      slice = jcicZ571Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ571> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ571> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ571ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ571ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ571ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ571Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ571> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ571> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ571ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ571ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ571ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ571Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ571> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ571> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ571ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ571ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ571ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ571Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ571> otherEq(String submitKey_0, String custId_1, int applyDate_2, String bankId_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ571> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " bankId_3 : " +  bankId_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ571ReposDay.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, bankId_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ571ReposMon.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, bankId_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ571ReposHist.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, bankId_3, pageable);
    else 
      slice = jcicZ571Repos.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, bankId_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ571 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ571> jcicZ571T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ571T = jcicZ571ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ571T = jcicZ571ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ571T = jcicZ571ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ571T = jcicZ571Repos.findTopByUkeyIs(ukey_0);

    return jcicZ571T.isPresent() ? jcicZ571T.get() : null;
  }

  @Override
  public JcicZ571 otherFirst(String submitKey_0, String custId_1, int applyDate_2, String bankId_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " bankId_3 : " +  bankId_3);
    Optional<JcicZ571> jcicZ571T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ571T = jcicZ571ReposDay.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, bankId_3);
    else if (dbName.equals(ContentName.onMon))
      jcicZ571T = jcicZ571ReposMon.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, bankId_3);
    else if (dbName.equals(ContentName.onHist))
      jcicZ571T = jcicZ571ReposHist.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, bankId_3);
    else 
      jcicZ571T = jcicZ571Repos.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, bankId_3);

    return jcicZ571T.isPresent() ? jcicZ571T.get() : null;
  }

  @Override
  public JcicZ571 holdById(JcicZ571Id jcicZ571Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ571Id);
    Optional<JcicZ571> jcicZ571 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ571 = jcicZ571ReposDay.findByJcicZ571Id(jcicZ571Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ571 = jcicZ571ReposMon.findByJcicZ571Id(jcicZ571Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ571 = jcicZ571ReposHist.findByJcicZ571Id(jcicZ571Id);
    else 
      jcicZ571 = jcicZ571Repos.findByJcicZ571Id(jcicZ571Id);
    return jcicZ571.isPresent() ? jcicZ571.get() : null;
  }

  @Override
  public JcicZ571 holdById(JcicZ571 jcicZ571, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ571.getJcicZ571Id());
    Optional<JcicZ571> jcicZ571T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ571T = jcicZ571ReposDay.findByJcicZ571Id(jcicZ571.getJcicZ571Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ571T = jcicZ571ReposMon.findByJcicZ571Id(jcicZ571.getJcicZ571Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ571T = jcicZ571ReposHist.findByJcicZ571Id(jcicZ571.getJcicZ571Id());
    else 
      jcicZ571T = jcicZ571Repos.findByJcicZ571Id(jcicZ571.getJcicZ571Id());
    return jcicZ571T.isPresent() ? jcicZ571T.get() : null;
  }

  @Override
  public JcicZ571 insert(JcicZ571 jcicZ571, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ571.getJcicZ571Id());
    if (this.findById(jcicZ571.getJcicZ571Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ571.setCreateEmpNo(empNot);

    if(jcicZ571.getLastUpdateEmpNo() == null || jcicZ571.getLastUpdateEmpNo().isEmpty())
      jcicZ571.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ571ReposDay.saveAndFlush(jcicZ571);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ571ReposMon.saveAndFlush(jcicZ571);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ571ReposHist.saveAndFlush(jcicZ571);
    else 
    return jcicZ571Repos.saveAndFlush(jcicZ571);
  }

  @Override
  public JcicZ571 update(JcicZ571 jcicZ571, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ571.getJcicZ571Id());
    if (!empNot.isEmpty())
      jcicZ571.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ571ReposDay.saveAndFlush(jcicZ571);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ571ReposMon.saveAndFlush(jcicZ571);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ571ReposHist.saveAndFlush(jcicZ571);
    else 
    return jcicZ571Repos.saveAndFlush(jcicZ571);
  }

  @Override
  public JcicZ571 update2(JcicZ571 jcicZ571, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ571.getJcicZ571Id());
    if (!empNot.isEmpty())
      jcicZ571.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ571ReposDay.saveAndFlush(jcicZ571);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ571ReposMon.saveAndFlush(jcicZ571);
    else if (dbName.equals(ContentName.onHist))
        jcicZ571ReposHist.saveAndFlush(jcicZ571);
    else 
      jcicZ571Repos.saveAndFlush(jcicZ571);	
    return this.findById(jcicZ571.getJcicZ571Id());
  }

  @Override
  public void delete(JcicZ571 jcicZ571, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ571.getJcicZ571Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ571ReposDay.delete(jcicZ571);	
      jcicZ571ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ571ReposMon.delete(jcicZ571);	
      jcicZ571ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ571ReposHist.delete(jcicZ571);
      jcicZ571ReposHist.flush();
    }
    else {
      jcicZ571Repos.delete(jcicZ571);
      jcicZ571Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ571> jcicZ571, TitaVo... titaVo) throws DBException {
    if (jcicZ571 == null || jcicZ571.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ571 t : jcicZ571){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ571 = jcicZ571ReposDay.saveAll(jcicZ571);	
      jcicZ571ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ571 = jcicZ571ReposMon.saveAll(jcicZ571);	
      jcicZ571ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ571 = jcicZ571ReposHist.saveAll(jcicZ571);
      jcicZ571ReposHist.flush();
    }
    else {
      jcicZ571 = jcicZ571Repos.saveAll(jcicZ571);
      jcicZ571Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ571> jcicZ571, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ571 == null || jcicZ571.size() == 0)
      throw new DBException(6);

    for (JcicZ571 t : jcicZ571) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ571 = jcicZ571ReposDay.saveAll(jcicZ571);	
      jcicZ571ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ571 = jcicZ571ReposMon.saveAll(jcicZ571);	
      jcicZ571ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ571 = jcicZ571ReposHist.saveAll(jcicZ571);
      jcicZ571ReposHist.flush();
    }
    else {
      jcicZ571 = jcicZ571Repos.saveAll(jcicZ571);
      jcicZ571Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ571> jcicZ571, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ571 == null || jcicZ571.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ571ReposDay.deleteAll(jcicZ571);	
      jcicZ571ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ571ReposMon.deleteAll(jcicZ571);	
      jcicZ571ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ571ReposHist.deleteAll(jcicZ571);
      jcicZ571ReposHist.flush();
    }
    else {
      jcicZ571Repos.deleteAll(jcicZ571);
      jcicZ571Repos.flush();
    }
  }

}
