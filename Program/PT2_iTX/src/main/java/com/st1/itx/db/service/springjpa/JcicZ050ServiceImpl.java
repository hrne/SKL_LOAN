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
import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.domain.JcicZ050Id;
import com.st1.itx.db.repository.online.JcicZ050Repository;
import com.st1.itx.db.repository.day.JcicZ050RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ050RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ050RepositoryHist;
import com.st1.itx.db.service.JcicZ050Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ050Service")
@Repository
public class JcicZ050ServiceImpl extends ASpringJpaParm implements JcicZ050Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ050Repository jcicZ050Repos;

  @Autowired
  private JcicZ050RepositoryDay jcicZ050ReposDay;

  @Autowired
  private JcicZ050RepositoryMon jcicZ050ReposMon;

  @Autowired
  private JcicZ050RepositoryHist jcicZ050ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ050Repos);
    org.junit.Assert.assertNotNull(jcicZ050ReposDay);
    org.junit.Assert.assertNotNull(jcicZ050ReposMon);
    org.junit.Assert.assertNotNull(jcicZ050ReposHist);
  }

  @Override
  public JcicZ050 findById(JcicZ050Id jcicZ050Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ050Id);
    Optional<JcicZ050> jcicZ050 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ050 = jcicZ050ReposDay.findById(jcicZ050Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ050 = jcicZ050ReposMon.findById(jcicZ050Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ050 = jcicZ050ReposHist.findById(jcicZ050Id);
    else 
      jcicZ050 = jcicZ050Repos.findById(jcicZ050Id);
    JcicZ050 obj = jcicZ050.isPresent() ? jcicZ050.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ050> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ050> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustId", "RcDate", "PayDate", "SubmitKey"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustId", "RcDate", "PayDate", "SubmitKey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ050ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ050ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ050ReposHist.findAll(pageable);
    else 
      slice = jcicZ050Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ050> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ050> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ050ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDescPayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ050ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDescPayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ050ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDescPayDateDesc(custId_0, pageable);
    else 
      slice = jcicZ050Repos.findAllByCustIdIsOrderByCustIdAscRcDateDescPayDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ050> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ050> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ050ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDescPayDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ050ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDescPayDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ050ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDescPayDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ050Repos.findAllByRcDateIsOrderByCustIdAscRcDateDescPayDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ050> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ050> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ050ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescPayDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ050ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescPayDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ050ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescPayDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ050Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescPayDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ050> RepayActualAmt(String custId_0, List<String> tranKey_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ050> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("RepayActualAmt " + dbName + " : " + "custId_0 : " + custId_0 + " tranKey_1 : " +  tranKey_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ050ReposDay.findAllByCustIdIsAndTranKeyInOrderByCustIdAscRcDateDescPayDateDesc(custId_0, tranKey_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ050ReposMon.findAllByCustIdIsAndTranKeyInOrderByCustIdAscRcDateDescPayDateDesc(custId_0, tranKey_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ050ReposHist.findAllByCustIdIsAndTranKeyInOrderByCustIdAscRcDateDescPayDateDesc(custId_0, tranKey_1, pageable);
    else 
      slice = jcicZ050Repos.findAllByCustIdIsAndTranKeyInOrderByCustIdAscRcDateDescPayDateDesc(custId_0, tranKey_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ050> otherEq(String custId_0, int rcDate_1, int payDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ050> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1 + " payDate_2 : " +  payDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ050ReposDay.findAllByCustIdIsAndRcDateIsAndPayDateIsOrderByCreateDateDesc(custId_0, rcDate_1, payDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ050ReposMon.findAllByCustIdIsAndRcDateIsAndPayDateIsOrderByCreateDateDesc(custId_0, rcDate_1, payDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ050ReposHist.findAllByCustIdIsAndRcDateIsAndPayDateIsOrderByCreateDateDesc(custId_0, rcDate_1, payDate_2, pageable);
    else 
      slice = jcicZ050Repos.findAllByCustIdIsAndRcDateIsAndPayDateIsOrderByCreateDateDesc(custId_0, rcDate_1, payDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ050 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ050> jcicZ050T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ050T = jcicZ050ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ050T = jcicZ050ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ050T = jcicZ050ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ050T = jcicZ050Repos.findTopByUkeyIs(ukey_0);

    return jcicZ050T.isPresent() ? jcicZ050T.get() : null;
  }

  @Override
  public JcicZ050 otherFirst(String custId_0, int rcDate_1, int payDate_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1 + " payDate_2 : " +  payDate_2);
    Optional<JcicZ050> jcicZ050T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ050T = jcicZ050ReposDay.findTopByCustIdIsAndRcDateIsAndPayDateIsOrderByCreateDateDesc(custId_0, rcDate_1, payDate_2);
    else if (dbName.equals(ContentName.onMon))
      jcicZ050T = jcicZ050ReposMon.findTopByCustIdIsAndRcDateIsAndPayDateIsOrderByCreateDateDesc(custId_0, rcDate_1, payDate_2);
    else if (dbName.equals(ContentName.onHist))
      jcicZ050T = jcicZ050ReposHist.findTopByCustIdIsAndRcDateIsAndPayDateIsOrderByCreateDateDesc(custId_0, rcDate_1, payDate_2);
    else 
      jcicZ050T = jcicZ050Repos.findTopByCustIdIsAndRcDateIsAndPayDateIsOrderByCreateDateDesc(custId_0, rcDate_1, payDate_2);

    return jcicZ050T.isPresent() ? jcicZ050T.get() : null;
  }

  @Override
  public Slice<JcicZ050> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ050> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findkeyFilingDate " + dbName + " : " + "actualFilingDate_0 : " + actualFilingDate_0 + " actualFilingMark_1 : " +  actualFilingMark_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ050ReposDay.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ050ReposMon.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ050ReposHist.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else 
      slice = jcicZ050Repos.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ050 holdById(JcicZ050Id jcicZ050Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ050Id);
    Optional<JcicZ050> jcicZ050 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ050 = jcicZ050ReposDay.findByJcicZ050Id(jcicZ050Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ050 = jcicZ050ReposMon.findByJcicZ050Id(jcicZ050Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ050 = jcicZ050ReposHist.findByJcicZ050Id(jcicZ050Id);
    else 
      jcicZ050 = jcicZ050Repos.findByJcicZ050Id(jcicZ050Id);
    return jcicZ050.isPresent() ? jcicZ050.get() : null;
  }

  @Override
  public JcicZ050 holdById(JcicZ050 jcicZ050, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ050.getJcicZ050Id());
    Optional<JcicZ050> jcicZ050T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ050T = jcicZ050ReposDay.findByJcicZ050Id(jcicZ050.getJcicZ050Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ050T = jcicZ050ReposMon.findByJcicZ050Id(jcicZ050.getJcicZ050Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ050T = jcicZ050ReposHist.findByJcicZ050Id(jcicZ050.getJcicZ050Id());
    else 
      jcicZ050T = jcicZ050Repos.findByJcicZ050Id(jcicZ050.getJcicZ050Id());
    return jcicZ050T.isPresent() ? jcicZ050T.get() : null;
  }

  @Override
  public JcicZ050 insert(JcicZ050 jcicZ050, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ050.getJcicZ050Id());
    if (this.findById(jcicZ050.getJcicZ050Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ050.setCreateEmpNo(empNot);

    if(jcicZ050.getLastUpdateEmpNo() == null || jcicZ050.getLastUpdateEmpNo().isEmpty())
      jcicZ050.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ050ReposDay.saveAndFlush(jcicZ050);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ050ReposMon.saveAndFlush(jcicZ050);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ050ReposHist.saveAndFlush(jcicZ050);
    else 
    return jcicZ050Repos.saveAndFlush(jcicZ050);
  }

  @Override
  public JcicZ050 update(JcicZ050 jcicZ050, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ050.getJcicZ050Id());
    if (!empNot.isEmpty())
      jcicZ050.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ050ReposDay.saveAndFlush(jcicZ050);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ050ReposMon.saveAndFlush(jcicZ050);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ050ReposHist.saveAndFlush(jcicZ050);
    else 
    return jcicZ050Repos.saveAndFlush(jcicZ050);
  }

  @Override
  public JcicZ050 update2(JcicZ050 jcicZ050, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ050.getJcicZ050Id());
    if (!empNot.isEmpty())
      jcicZ050.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ050ReposDay.saveAndFlush(jcicZ050);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ050ReposMon.saveAndFlush(jcicZ050);
    else if (dbName.equals(ContentName.onHist))
        jcicZ050ReposHist.saveAndFlush(jcicZ050);
    else 
      jcicZ050Repos.saveAndFlush(jcicZ050);	
    return this.findById(jcicZ050.getJcicZ050Id());
  }

  @Override
  public void delete(JcicZ050 jcicZ050, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ050.getJcicZ050Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ050ReposDay.delete(jcicZ050);	
      jcicZ050ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ050ReposMon.delete(jcicZ050);	
      jcicZ050ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ050ReposHist.delete(jcicZ050);
      jcicZ050ReposHist.flush();
    }
    else {
      jcicZ050Repos.delete(jcicZ050);
      jcicZ050Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ050> jcicZ050, TitaVo... titaVo) throws DBException {
    if (jcicZ050 == null || jcicZ050.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ050 t : jcicZ050){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ050 = jcicZ050ReposDay.saveAll(jcicZ050);	
      jcicZ050ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ050 = jcicZ050ReposMon.saveAll(jcicZ050);	
      jcicZ050ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ050 = jcicZ050ReposHist.saveAll(jcicZ050);
      jcicZ050ReposHist.flush();
    }
    else {
      jcicZ050 = jcicZ050Repos.saveAll(jcicZ050);
      jcicZ050Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ050> jcicZ050, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ050 == null || jcicZ050.size() == 0)
      throw new DBException(6);

    for (JcicZ050 t : jcicZ050) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ050 = jcicZ050ReposDay.saveAll(jcicZ050);	
      jcicZ050ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ050 = jcicZ050ReposMon.saveAll(jcicZ050);	
      jcicZ050ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ050 = jcicZ050ReposHist.saveAll(jcicZ050);
      jcicZ050ReposHist.flush();
    }
    else {
      jcicZ050 = jcicZ050Repos.saveAll(jcicZ050);
      jcicZ050Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ050> jcicZ050, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ050 == null || jcicZ050.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ050ReposDay.deleteAll(jcicZ050);	
      jcicZ050ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ050ReposMon.deleteAll(jcicZ050);	
      jcicZ050ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ050ReposHist.deleteAll(jcicZ050);
      jcicZ050ReposHist.flush();
    }
    else {
      jcicZ050Repos.deleteAll(jcicZ050);
      jcicZ050Repos.flush();
    }
  }

}
