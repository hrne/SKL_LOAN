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
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ051Id;
import com.st1.itx.db.repository.online.JcicZ051Repository;
import com.st1.itx.db.repository.day.JcicZ051RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ051RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ051RepositoryHist;
import com.st1.itx.db.service.JcicZ051Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ051Service")
@Repository
public class JcicZ051ServiceImpl extends ASpringJpaParm implements JcicZ051Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ051Repository jcicZ051Repos;

  @Autowired
  private JcicZ051RepositoryDay jcicZ051ReposDay;

  @Autowired
  private JcicZ051RepositoryMon jcicZ051ReposMon;

  @Autowired
  private JcicZ051RepositoryHist jcicZ051ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ051Repos);
    org.junit.Assert.assertNotNull(jcicZ051ReposDay);
    org.junit.Assert.assertNotNull(jcicZ051ReposMon);
    org.junit.Assert.assertNotNull(jcicZ051ReposHist);
  }

  @Override
  public JcicZ051 findById(JcicZ051Id jcicZ051Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ051Id);
    Optional<JcicZ051> jcicZ051 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ051 = jcicZ051ReposDay.findById(jcicZ051Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ051 = jcicZ051ReposMon.findById(jcicZ051Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ051 = jcicZ051ReposHist.findById(jcicZ051Id);
    else 
      jcicZ051 = jcicZ051Repos.findById(jcicZ051Id);
    JcicZ051 obj = jcicZ051.isPresent() ? jcicZ051.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ051> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ051> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "DelayYM"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "DelayYM"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ051ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ051ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ051ReposHist.findAll(pageable);
    else 
      slice = jcicZ051Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ051> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ051> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ051ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDescDelayYMDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ051ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDescDelayYMDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ051ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDescDelayYMDesc(custId_0, pageable);
    else 
      slice = jcicZ051Repos.findAllByCustIdIsOrderByCustIdAscRcDateDescDelayYMDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ051> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ051> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ051ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDescDelayYMDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ051ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDescDelayYMDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ051ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDescDelayYMDesc(rcDate_0, pageable);
    else 
      slice = jcicZ051Repos.findAllByRcDateIsOrderByCustIdAscRcDateDescDelayYMDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ051> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ051> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ051ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescDelayYMDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ051ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescDelayYMDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ051ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescDelayYMDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ051Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescDelayYMDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ051> InJcicZ051(String submitKey_0, String custId_1, int rcDate_2, int delayYM_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ051> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("InJcicZ051 " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " delayYM_3 : " +  delayYM_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ051ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIs(submitKey_0, custId_1, rcDate_2, delayYM_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ051ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIs(submitKey_0, custId_1, rcDate_2, delayYM_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ051ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIs(submitKey_0, custId_1, rcDate_2, delayYM_3, pageable);
    else 
      slice = jcicZ051Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIs(submitKey_0, custId_1, rcDate_2, delayYM_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ051> SubCustRcEq(String custId_0, int rcDate_1, String submitKey_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ051> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("SubCustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1 + " submitKey_2 : " +  submitKey_2);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ051ReposDay.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsOrderByDelayYMDesc(custId_0, rcDate_1, submitKey_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ051ReposMon.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsOrderByDelayYMDesc(custId_0, rcDate_1, submitKey_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ051ReposHist.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsOrderByDelayYMDesc(custId_0, rcDate_1, submitKey_2, pageable);
    else 
      slice = jcicZ051Repos.findAllByCustIdIsAndRcDateIsAndSubmitKeyIsOrderByDelayYMDesc(custId_0, rcDate_1, submitKey_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ051> otherEq(String submitKey_0, String custId_1, int rcDate_2, int delayYM_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ051> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " delayYM_3 : " +  delayYM_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ051ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, delayYM_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ051ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, delayYM_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ051ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, delayYM_3, pageable);
    else 
      slice = jcicZ051Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, delayYM_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ051 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ051> jcicZ051T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ051T = jcicZ051ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ051T = jcicZ051ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ051T = jcicZ051ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ051T = jcicZ051Repos.findTopByUkeyIs(ukey_0);

    return jcicZ051T.isPresent() ? jcicZ051T.get() : null;
  }

  @Override
  public JcicZ051 otherFirst(String submitKey_0, String custId_1, int rcDate_2, int delayYM_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " delayYM_3 : " +  delayYM_3);
    Optional<JcicZ051> jcicZ051T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ051T = jcicZ051ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, delayYM_3);
    else if (dbName.equals(ContentName.onMon))
      jcicZ051T = jcicZ051ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, delayYM_3);
    else if (dbName.equals(ContentName.onHist))
      jcicZ051T = jcicZ051ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, delayYM_3);
    else 
      jcicZ051T = jcicZ051Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, delayYM_3);

    return jcicZ051T.isPresent() ? jcicZ051T.get() : null;
  }

  @Override
  public Slice<JcicZ051> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ051> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findkeyFilingDate " + dbName + " : " + "actualFilingDate_0 : " + actualFilingDate_0 + " actualFilingMark_1 : " +  actualFilingMark_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ051ReposDay.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ051ReposMon.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ051ReposHist.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else 
      slice = jcicZ051Repos.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ051 holdById(JcicZ051Id jcicZ051Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ051Id);
    Optional<JcicZ051> jcicZ051 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ051 = jcicZ051ReposDay.findByJcicZ051Id(jcicZ051Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ051 = jcicZ051ReposMon.findByJcicZ051Id(jcicZ051Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ051 = jcicZ051ReposHist.findByJcicZ051Id(jcicZ051Id);
    else 
      jcicZ051 = jcicZ051Repos.findByJcicZ051Id(jcicZ051Id);
    return jcicZ051.isPresent() ? jcicZ051.get() : null;
  }

  @Override
  public JcicZ051 holdById(JcicZ051 jcicZ051, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ051.getJcicZ051Id());
    Optional<JcicZ051> jcicZ051T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ051T = jcicZ051ReposDay.findByJcicZ051Id(jcicZ051.getJcicZ051Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ051T = jcicZ051ReposMon.findByJcicZ051Id(jcicZ051.getJcicZ051Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ051T = jcicZ051ReposHist.findByJcicZ051Id(jcicZ051.getJcicZ051Id());
    else 
      jcicZ051T = jcicZ051Repos.findByJcicZ051Id(jcicZ051.getJcicZ051Id());
    return jcicZ051T.isPresent() ? jcicZ051T.get() : null;
  }

  @Override
  public JcicZ051 insert(JcicZ051 jcicZ051, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ051.getJcicZ051Id());
    if (this.findById(jcicZ051.getJcicZ051Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ051.setCreateEmpNo(empNot);

    if(jcicZ051.getLastUpdateEmpNo() == null || jcicZ051.getLastUpdateEmpNo().isEmpty())
      jcicZ051.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ051ReposDay.saveAndFlush(jcicZ051);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ051ReposMon.saveAndFlush(jcicZ051);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ051ReposHist.saveAndFlush(jcicZ051);
    else 
    return jcicZ051Repos.saveAndFlush(jcicZ051);
  }

  @Override
  public JcicZ051 update(JcicZ051 jcicZ051, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ051.getJcicZ051Id());
    if (!empNot.isEmpty())
      jcicZ051.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ051ReposDay.saveAndFlush(jcicZ051);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ051ReposMon.saveAndFlush(jcicZ051);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ051ReposHist.saveAndFlush(jcicZ051);
    else 
    return jcicZ051Repos.saveAndFlush(jcicZ051);
  }

  @Override
  public JcicZ051 update2(JcicZ051 jcicZ051, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ051.getJcicZ051Id());
    if (!empNot.isEmpty())
      jcicZ051.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ051ReposDay.saveAndFlush(jcicZ051);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ051ReposMon.saveAndFlush(jcicZ051);
    else if (dbName.equals(ContentName.onHist))
        jcicZ051ReposHist.saveAndFlush(jcicZ051);
    else 
      jcicZ051Repos.saveAndFlush(jcicZ051);	
    return this.findById(jcicZ051.getJcicZ051Id());
  }

  @Override
  public void delete(JcicZ051 jcicZ051, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ051.getJcicZ051Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ051ReposDay.delete(jcicZ051);	
      jcicZ051ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ051ReposMon.delete(jcicZ051);	
      jcicZ051ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ051ReposHist.delete(jcicZ051);
      jcicZ051ReposHist.flush();
    }
    else {
      jcicZ051Repos.delete(jcicZ051);
      jcicZ051Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ051> jcicZ051, TitaVo... titaVo) throws DBException {
    if (jcicZ051 == null || jcicZ051.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ051 t : jcicZ051){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ051 = jcicZ051ReposDay.saveAll(jcicZ051);	
      jcicZ051ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ051 = jcicZ051ReposMon.saveAll(jcicZ051);	
      jcicZ051ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ051 = jcicZ051ReposHist.saveAll(jcicZ051);
      jcicZ051ReposHist.flush();
    }
    else {
      jcicZ051 = jcicZ051Repos.saveAll(jcicZ051);
      jcicZ051Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ051> jcicZ051, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ051 == null || jcicZ051.size() == 0)
      throw new DBException(6);

    for (JcicZ051 t : jcicZ051) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ051 = jcicZ051ReposDay.saveAll(jcicZ051);	
      jcicZ051ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ051 = jcicZ051ReposMon.saveAll(jcicZ051);	
      jcicZ051ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ051 = jcicZ051ReposHist.saveAll(jcicZ051);
      jcicZ051ReposHist.flush();
    }
    else {
      jcicZ051 = jcicZ051Repos.saveAll(jcicZ051);
      jcicZ051Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ051> jcicZ051, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ051 == null || jcicZ051.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ051ReposDay.deleteAll(jcicZ051);	
      jcicZ051ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ051ReposMon.deleteAll(jcicZ051);	
      jcicZ051ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ051ReposHist.deleteAll(jcicZ051);
      jcicZ051ReposHist.flush();
    }
    else {
      jcicZ051Repos.deleteAll(jcicZ051);
      jcicZ051Repos.flush();
    }
  }

}
