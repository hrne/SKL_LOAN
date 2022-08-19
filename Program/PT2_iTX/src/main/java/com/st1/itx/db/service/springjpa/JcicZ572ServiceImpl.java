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
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.domain.JcicZ572Id;
import com.st1.itx.db.repository.online.JcicZ572Repository;
import com.st1.itx.db.repository.day.JcicZ572RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ572RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ572RepositoryHist;
import com.st1.itx.db.service.JcicZ572Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ572Service")
@Repository
public class JcicZ572ServiceImpl extends ASpringJpaParm implements JcicZ572Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ572Repository jcicZ572Repos;

  @Autowired
  private JcicZ572RepositoryDay jcicZ572ReposDay;

  @Autowired
  private JcicZ572RepositoryMon jcicZ572ReposMon;

  @Autowired
  private JcicZ572RepositoryHist jcicZ572ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ572Repos);
    org.junit.Assert.assertNotNull(jcicZ572ReposDay);
    org.junit.Assert.assertNotNull(jcicZ572ReposMon);
    org.junit.Assert.assertNotNull(jcicZ572ReposHist);
  }

  @Override
  public JcicZ572 findById(JcicZ572Id jcicZ572Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ572Id);
    Optional<JcicZ572> jcicZ572 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ572 = jcicZ572ReposDay.findById(jcicZ572Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ572 = jcicZ572ReposMon.findById(jcicZ572Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ572 = jcicZ572ReposHist.findById(jcicZ572Id);
    else 
      jcicZ572 = jcicZ572Repos.findById(jcicZ572Id);
    JcicZ572 obj = jcicZ572.isPresent() ? jcicZ572.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ572> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ572> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "PayDate", "BankId"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "PayDate", "BankId"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ572ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ572ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ572ReposHist.findAll(pageable);
    else 
      slice = jcicZ572Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ572> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ572> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ572ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ572ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ572ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, pageable);
    else 
      slice = jcicZ572Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ572> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ572> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ572ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ572ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ572ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ572Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(applyDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ572> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ572> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ572ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ572ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ572ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ572Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(custId_0, applyDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ572> otherEq(String submitKey_0, String custId_1, int applyDate_2, int payDate_3, String bankId_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ572> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " payDate_3 : " +  payDate_3 + " bankId_4 : " +  bankId_4);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ572ReposDay.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndPayDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, payDate_3, bankId_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ572ReposMon.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndPayDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, payDate_3, bankId_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ572ReposHist.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndPayDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, payDate_3, bankId_4, pageable);
    else 
      slice = jcicZ572Repos.findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndPayDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, payDate_3, bankId_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ572 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ572> jcicZ572T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ572T = jcicZ572ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ572T = jcicZ572ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ572T = jcicZ572ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ572T = jcicZ572Repos.findTopByUkeyIs(ukey_0);

    return jcicZ572T.isPresent() ? jcicZ572T.get() : null;
  }

  @Override
  public JcicZ572 otherFirst(String submitKey_0, String custId_1, int applyDate_2, int payDate_3, String bankId_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " applyDate_2 : " +  applyDate_2 + " payDate_3 : " +  payDate_3 + " bankId_4 : " +  bankId_4);
    Optional<JcicZ572> jcicZ572T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ572T = jcicZ572ReposDay.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndPayDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, payDate_3, bankId_4);
    else if (dbName.equals(ContentName.onMon))
      jcicZ572T = jcicZ572ReposMon.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndPayDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, payDate_3, bankId_4);
    else if (dbName.equals(ContentName.onHist))
      jcicZ572T = jcicZ572ReposHist.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndPayDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, payDate_3, bankId_4);
    else 
      jcicZ572T = jcicZ572Repos.findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndPayDateIsAndBankIdIsOrderByCreateDateDesc(submitKey_0, custId_1, applyDate_2, payDate_3, bankId_4);

    return jcicZ572T.isPresent() ? jcicZ572T.get() : null;
  }

  @Override
  public JcicZ572 holdById(JcicZ572Id jcicZ572Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ572Id);
    Optional<JcicZ572> jcicZ572 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ572 = jcicZ572ReposDay.findByJcicZ572Id(jcicZ572Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ572 = jcicZ572ReposMon.findByJcicZ572Id(jcicZ572Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ572 = jcicZ572ReposHist.findByJcicZ572Id(jcicZ572Id);
    else 
      jcicZ572 = jcicZ572Repos.findByJcicZ572Id(jcicZ572Id);
    return jcicZ572.isPresent() ? jcicZ572.get() : null;
  }

  @Override
  public JcicZ572 holdById(JcicZ572 jcicZ572, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ572.getJcicZ572Id());
    Optional<JcicZ572> jcicZ572T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ572T = jcicZ572ReposDay.findByJcicZ572Id(jcicZ572.getJcicZ572Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ572T = jcicZ572ReposMon.findByJcicZ572Id(jcicZ572.getJcicZ572Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ572T = jcicZ572ReposHist.findByJcicZ572Id(jcicZ572.getJcicZ572Id());
    else 
      jcicZ572T = jcicZ572Repos.findByJcicZ572Id(jcicZ572.getJcicZ572Id());
    return jcicZ572T.isPresent() ? jcicZ572T.get() : null;
  }

  @Override
  public JcicZ572 insert(JcicZ572 jcicZ572, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ572.getJcicZ572Id());
    if (this.findById(jcicZ572.getJcicZ572Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ572.setCreateEmpNo(empNot);

    if(jcicZ572.getLastUpdateEmpNo() == null || jcicZ572.getLastUpdateEmpNo().isEmpty())
      jcicZ572.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ572ReposDay.saveAndFlush(jcicZ572);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ572ReposMon.saveAndFlush(jcicZ572);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ572ReposHist.saveAndFlush(jcicZ572);
    else 
    return jcicZ572Repos.saveAndFlush(jcicZ572);
  }

  @Override
  public JcicZ572 update(JcicZ572 jcicZ572, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ572.getJcicZ572Id());
    if (!empNot.isEmpty())
      jcicZ572.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ572ReposDay.saveAndFlush(jcicZ572);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ572ReposMon.saveAndFlush(jcicZ572);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ572ReposHist.saveAndFlush(jcicZ572);
    else 
    return jcicZ572Repos.saveAndFlush(jcicZ572);
  }

  @Override
  public JcicZ572 update2(JcicZ572 jcicZ572, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ572.getJcicZ572Id());
    if (!empNot.isEmpty())
      jcicZ572.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ572ReposDay.saveAndFlush(jcicZ572);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ572ReposMon.saveAndFlush(jcicZ572);
    else if (dbName.equals(ContentName.onHist))
        jcicZ572ReposHist.saveAndFlush(jcicZ572);
    else 
      jcicZ572Repos.saveAndFlush(jcicZ572);	
    return this.findById(jcicZ572.getJcicZ572Id());
  }

  @Override
  public void delete(JcicZ572 jcicZ572, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ572.getJcicZ572Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ572ReposDay.delete(jcicZ572);	
      jcicZ572ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ572ReposMon.delete(jcicZ572);	
      jcicZ572ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ572ReposHist.delete(jcicZ572);
      jcicZ572ReposHist.flush();
    }
    else {
      jcicZ572Repos.delete(jcicZ572);
      jcicZ572Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ572> jcicZ572, TitaVo... titaVo) throws DBException {
    if (jcicZ572 == null || jcicZ572.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ572 t : jcicZ572){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ572 = jcicZ572ReposDay.saveAll(jcicZ572);	
      jcicZ572ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ572 = jcicZ572ReposMon.saveAll(jcicZ572);	
      jcicZ572ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ572 = jcicZ572ReposHist.saveAll(jcicZ572);
      jcicZ572ReposHist.flush();
    }
    else {
      jcicZ572 = jcicZ572Repos.saveAll(jcicZ572);
      jcicZ572Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ572> jcicZ572, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ572 == null || jcicZ572.size() == 0)
      throw new DBException(6);

    for (JcicZ572 t : jcicZ572) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ572 = jcicZ572ReposDay.saveAll(jcicZ572);	
      jcicZ572ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ572 = jcicZ572ReposMon.saveAll(jcicZ572);	
      jcicZ572ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ572 = jcicZ572ReposHist.saveAll(jcicZ572);
      jcicZ572ReposHist.flush();
    }
    else {
      jcicZ572 = jcicZ572Repos.saveAll(jcicZ572);
      jcicZ572Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ572> jcicZ572, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ572 == null || jcicZ572.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ572ReposDay.deleteAll(jcicZ572);	
      jcicZ572ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ572ReposMon.deleteAll(jcicZ572);	
      jcicZ572ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ572ReposHist.deleteAll(jcicZ572);
      jcicZ572ReposHist.flush();
    }
    else {
      jcicZ572Repos.deleteAll(jcicZ572);
      jcicZ572Repos.flush();
    }
  }

}
