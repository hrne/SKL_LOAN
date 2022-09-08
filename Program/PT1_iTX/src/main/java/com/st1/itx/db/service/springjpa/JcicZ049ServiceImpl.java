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
import com.st1.itx.db.domain.JcicZ049;
import com.st1.itx.db.domain.JcicZ049Id;
import com.st1.itx.db.repository.online.JcicZ049Repository;
import com.st1.itx.db.repository.day.JcicZ049RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ049RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ049RepositoryHist;
import com.st1.itx.db.service.JcicZ049Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ049Service")
@Repository
public class JcicZ049ServiceImpl extends ASpringJpaParm implements JcicZ049Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ049Repository jcicZ049Repos;

  @Autowired
  private JcicZ049RepositoryDay jcicZ049ReposDay;

  @Autowired
  private JcicZ049RepositoryMon jcicZ049ReposMon;

  @Autowired
  private JcicZ049RepositoryHist jcicZ049ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ049Repos);
    org.junit.Assert.assertNotNull(jcicZ049ReposDay);
    org.junit.Assert.assertNotNull(jcicZ049ReposMon);
    org.junit.Assert.assertNotNull(jcicZ049ReposHist);
  }

  @Override
  public JcicZ049 findById(JcicZ049Id jcicZ049Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ049Id);
    Optional<JcicZ049> jcicZ049 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ049 = jcicZ049ReposDay.findById(jcicZ049Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ049 = jcicZ049ReposMon.findById(jcicZ049Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ049 = jcicZ049ReposHist.findById(jcicZ049Id);
    else 
      jcicZ049 = jcicZ049Repos.findById(jcicZ049Id);
    JcicZ049 obj = jcicZ049.isPresent() ? jcicZ049.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ049> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ049> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ049ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ049ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ049ReposHist.findAll(pageable);
    else 
      slice = jcicZ049Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ049> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ049> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ049ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ049ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ049ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ049Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ049> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ049> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ049ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ049ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ049ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ049Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ049> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ049> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ049ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ049ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ049ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ049Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ049> otherEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ049> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ049ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ049ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ049ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else 
      slice = jcicZ049Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ049 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ049> jcicZ049T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ049T = jcicZ049ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ049T = jcicZ049ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ049T = jcicZ049ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ049T = jcicZ049Repos.findTopByUkeyIs(ukey_0);

    return jcicZ049T.isPresent() ? jcicZ049T.get() : null;
  }

  @Override
  public JcicZ049 otherFirst(String submitKey_0, String custId_1, int rcDate_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2);
    Optional<JcicZ049> jcicZ049T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ049T = jcicZ049ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else if (dbName.equals(ContentName.onMon))
      jcicZ049T = jcicZ049ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else if (dbName.equals(ContentName.onHist))
      jcicZ049T = jcicZ049ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else 
      jcicZ049T = jcicZ049Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);

    return jcicZ049T.isPresent() ? jcicZ049T.get() : null;
  }

  @Override
  public Slice<JcicZ049> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ049> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findkeyFilingDate " + dbName + " : " + "actualFilingDate_0 : " + actualFilingDate_0 + " actualFilingMark_1 : " +  actualFilingMark_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ049ReposDay.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ049ReposMon.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ049ReposHist.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else 
      slice = jcicZ049Repos.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ049 holdById(JcicZ049Id jcicZ049Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ049Id);
    Optional<JcicZ049> jcicZ049 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ049 = jcicZ049ReposDay.findByJcicZ049Id(jcicZ049Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ049 = jcicZ049ReposMon.findByJcicZ049Id(jcicZ049Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ049 = jcicZ049ReposHist.findByJcicZ049Id(jcicZ049Id);
    else 
      jcicZ049 = jcicZ049Repos.findByJcicZ049Id(jcicZ049Id);
    return jcicZ049.isPresent() ? jcicZ049.get() : null;
  }

  @Override
  public JcicZ049 holdById(JcicZ049 jcicZ049, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ049.getJcicZ049Id());
    Optional<JcicZ049> jcicZ049T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ049T = jcicZ049ReposDay.findByJcicZ049Id(jcicZ049.getJcicZ049Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ049T = jcicZ049ReposMon.findByJcicZ049Id(jcicZ049.getJcicZ049Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ049T = jcicZ049ReposHist.findByJcicZ049Id(jcicZ049.getJcicZ049Id());
    else 
      jcicZ049T = jcicZ049Repos.findByJcicZ049Id(jcicZ049.getJcicZ049Id());
    return jcicZ049T.isPresent() ? jcicZ049T.get() : null;
  }

  @Override
  public JcicZ049 insert(JcicZ049 jcicZ049, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ049.getJcicZ049Id());
    if (this.findById(jcicZ049.getJcicZ049Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ049.setCreateEmpNo(empNot);

    if(jcicZ049.getLastUpdateEmpNo() == null || jcicZ049.getLastUpdateEmpNo().isEmpty())
      jcicZ049.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ049ReposDay.saveAndFlush(jcicZ049);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ049ReposMon.saveAndFlush(jcicZ049);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ049ReposHist.saveAndFlush(jcicZ049);
    else 
    return jcicZ049Repos.saveAndFlush(jcicZ049);
  }

  @Override
  public JcicZ049 update(JcicZ049 jcicZ049, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ049.getJcicZ049Id());
    if (!empNot.isEmpty())
      jcicZ049.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ049ReposDay.saveAndFlush(jcicZ049);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ049ReposMon.saveAndFlush(jcicZ049);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ049ReposHist.saveAndFlush(jcicZ049);
    else 
    return jcicZ049Repos.saveAndFlush(jcicZ049);
  }

  @Override
  public JcicZ049 update2(JcicZ049 jcicZ049, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ049.getJcicZ049Id());
    if (!empNot.isEmpty())
      jcicZ049.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ049ReposDay.saveAndFlush(jcicZ049);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ049ReposMon.saveAndFlush(jcicZ049);
    else if (dbName.equals(ContentName.onHist))
        jcicZ049ReposHist.saveAndFlush(jcicZ049);
    else 
      jcicZ049Repos.saveAndFlush(jcicZ049);	
    return this.findById(jcicZ049.getJcicZ049Id());
  }

  @Override
  public void delete(JcicZ049 jcicZ049, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ049.getJcicZ049Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ049ReposDay.delete(jcicZ049);	
      jcicZ049ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ049ReposMon.delete(jcicZ049);	
      jcicZ049ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ049ReposHist.delete(jcicZ049);
      jcicZ049ReposHist.flush();
    }
    else {
      jcicZ049Repos.delete(jcicZ049);
      jcicZ049Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ049> jcicZ049, TitaVo... titaVo) throws DBException {
    if (jcicZ049 == null || jcicZ049.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ049 t : jcicZ049){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ049 = jcicZ049ReposDay.saveAll(jcicZ049);	
      jcicZ049ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ049 = jcicZ049ReposMon.saveAll(jcicZ049);	
      jcicZ049ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ049 = jcicZ049ReposHist.saveAll(jcicZ049);
      jcicZ049ReposHist.flush();
    }
    else {
      jcicZ049 = jcicZ049Repos.saveAll(jcicZ049);
      jcicZ049Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ049> jcicZ049, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ049 == null || jcicZ049.size() == 0)
      throw new DBException(6);

    for (JcicZ049 t : jcicZ049) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ049 = jcicZ049ReposDay.saveAll(jcicZ049);	
      jcicZ049ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ049 = jcicZ049ReposMon.saveAll(jcicZ049);	
      jcicZ049ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ049 = jcicZ049ReposHist.saveAll(jcicZ049);
      jcicZ049ReposHist.flush();
    }
    else {
      jcicZ049 = jcicZ049Repos.saveAll(jcicZ049);
      jcicZ049Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ049> jcicZ049, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ049 == null || jcicZ049.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ049ReposDay.deleteAll(jcicZ049);	
      jcicZ049ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ049ReposMon.deleteAll(jcicZ049);	
      jcicZ049ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ049ReposHist.deleteAll(jcicZ049);
      jcicZ049ReposHist.flush();
    }
    else {
      jcicZ049Repos.deleteAll(jcicZ049);
      jcicZ049Repos.flush();
    }
  }

}
