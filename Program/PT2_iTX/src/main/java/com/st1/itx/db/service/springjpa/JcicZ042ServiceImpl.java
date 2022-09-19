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
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.domain.JcicZ042Id;
import com.st1.itx.db.repository.online.JcicZ042Repository;
import com.st1.itx.db.repository.day.JcicZ042RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ042RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ042RepositoryHist;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ042Service")
@Repository
public class JcicZ042ServiceImpl extends ASpringJpaParm implements JcicZ042Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ042Repository jcicZ042Repos;

  @Autowired
  private JcicZ042RepositoryDay jcicZ042ReposDay;

  @Autowired
  private JcicZ042RepositoryMon jcicZ042ReposMon;

  @Autowired
  private JcicZ042RepositoryHist jcicZ042ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ042Repos);
    org.junit.Assert.assertNotNull(jcicZ042ReposDay);
    org.junit.Assert.assertNotNull(jcicZ042ReposMon);
    org.junit.Assert.assertNotNull(jcicZ042ReposHist);
  }

  @Override
  public JcicZ042 findById(JcicZ042Id jcicZ042Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ042Id);
    Optional<JcicZ042> jcicZ042 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ042 = jcicZ042ReposDay.findById(jcicZ042Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ042 = jcicZ042ReposMon.findById(jcicZ042Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ042 = jcicZ042ReposHist.findById(jcicZ042Id);
    else 
      jcicZ042 = jcicZ042Repos.findById(jcicZ042Id);
    JcicZ042 obj = jcicZ042.isPresent() ? jcicZ042.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ042> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "MaxMainCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "MaxMainCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042ReposHist.findAll(pageable);
    else 
      slice = jcicZ042Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ042> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ042Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ042> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ042Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ042> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ042Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ042> otherEq(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " maxMainCode_3 : " +  maxMainCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);
    else 
      slice = jcicZ042Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ042 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ042> jcicZ042T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ042T = jcicZ042ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ042T = jcicZ042ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ042T = jcicZ042ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ042T = jcicZ042Repos.findTopByUkeyIs(ukey_0);

    return jcicZ042T.isPresent() ? jcicZ042T.get() : null;
  }

  @Override
  public JcicZ042 otherFirst(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " maxMainCode_3 : " +  maxMainCode_3);
    Optional<JcicZ042> jcicZ042T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ042T = jcicZ042ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);
    else if (dbName.equals(ContentName.onMon))
      jcicZ042T = jcicZ042ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);
    else if (dbName.equals(ContentName.onHist))
      jcicZ042T = jcicZ042ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);
    else 
      jcicZ042T = jcicZ042Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3);

    return jcicZ042T.isPresent() ? jcicZ042T.get() : null;
  }

  @Override
  public Slice<JcicZ042> custRcSubEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcSubEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else 
      slice = jcicZ042Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ042> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findkeyFilingDate " + dbName + " : " + "actualFilingDate_0 : " + actualFilingDate_0 + " actualFilingMark_1 : " +  actualFilingMark_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042ReposDay.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042ReposMon.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042ReposHist.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else 
      slice = jcicZ042Repos.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ042 holdById(JcicZ042Id jcicZ042Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ042Id);
    Optional<JcicZ042> jcicZ042 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ042 = jcicZ042ReposDay.findByJcicZ042Id(jcicZ042Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ042 = jcicZ042ReposMon.findByJcicZ042Id(jcicZ042Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ042 = jcicZ042ReposHist.findByJcicZ042Id(jcicZ042Id);
    else 
      jcicZ042 = jcicZ042Repos.findByJcicZ042Id(jcicZ042Id);
    return jcicZ042.isPresent() ? jcicZ042.get() : null;
  }

  @Override
  public JcicZ042 holdById(JcicZ042 jcicZ042, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ042.getJcicZ042Id());
    Optional<JcicZ042> jcicZ042T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ042T = jcicZ042ReposDay.findByJcicZ042Id(jcicZ042.getJcicZ042Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ042T = jcicZ042ReposMon.findByJcicZ042Id(jcicZ042.getJcicZ042Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ042T = jcicZ042ReposHist.findByJcicZ042Id(jcicZ042.getJcicZ042Id());
    else 
      jcicZ042T = jcicZ042Repos.findByJcicZ042Id(jcicZ042.getJcicZ042Id());
    return jcicZ042T.isPresent() ? jcicZ042T.get() : null;
  }

  @Override
  public JcicZ042 insert(JcicZ042 jcicZ042, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ042.getJcicZ042Id());
    if (this.findById(jcicZ042.getJcicZ042Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ042.setCreateEmpNo(empNot);

    if(jcicZ042.getLastUpdateEmpNo() == null || jcicZ042.getLastUpdateEmpNo().isEmpty())
      jcicZ042.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ042ReposDay.saveAndFlush(jcicZ042);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ042ReposMon.saveAndFlush(jcicZ042);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ042ReposHist.saveAndFlush(jcicZ042);
    else 
    return jcicZ042Repos.saveAndFlush(jcicZ042);
  }

  @Override
  public JcicZ042 update(JcicZ042 jcicZ042, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ042.getJcicZ042Id());
    if (!empNot.isEmpty())
      jcicZ042.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ042ReposDay.saveAndFlush(jcicZ042);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ042ReposMon.saveAndFlush(jcicZ042);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ042ReposHist.saveAndFlush(jcicZ042);
    else 
    return jcicZ042Repos.saveAndFlush(jcicZ042);
  }

  @Override
  public JcicZ042 update2(JcicZ042 jcicZ042, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ042.getJcicZ042Id());
    if (!empNot.isEmpty())
      jcicZ042.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ042ReposDay.saveAndFlush(jcicZ042);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ042ReposMon.saveAndFlush(jcicZ042);
    else if (dbName.equals(ContentName.onHist))
        jcicZ042ReposHist.saveAndFlush(jcicZ042);
    else 
      jcicZ042Repos.saveAndFlush(jcicZ042);	
    return this.findById(jcicZ042.getJcicZ042Id());
  }

  @Override
  public void delete(JcicZ042 jcicZ042, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ042.getJcicZ042Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ042ReposDay.delete(jcicZ042);	
      jcicZ042ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ042ReposMon.delete(jcicZ042);	
      jcicZ042ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ042ReposHist.delete(jcicZ042);
      jcicZ042ReposHist.flush();
    }
    else {
      jcicZ042Repos.delete(jcicZ042);
      jcicZ042Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ042> jcicZ042, TitaVo... titaVo) throws DBException {
    if (jcicZ042 == null || jcicZ042.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ042 t : jcicZ042){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ042 = jcicZ042ReposDay.saveAll(jcicZ042);	
      jcicZ042ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ042 = jcicZ042ReposMon.saveAll(jcicZ042);	
      jcicZ042ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ042 = jcicZ042ReposHist.saveAll(jcicZ042);
      jcicZ042ReposHist.flush();
    }
    else {
      jcicZ042 = jcicZ042Repos.saveAll(jcicZ042);
      jcicZ042Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ042> jcicZ042, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ042 == null || jcicZ042.size() == 0)
      throw new DBException(6);

    for (JcicZ042 t : jcicZ042) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ042 = jcicZ042ReposDay.saveAll(jcicZ042);	
      jcicZ042ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ042 = jcicZ042ReposMon.saveAll(jcicZ042);	
      jcicZ042ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ042 = jcicZ042ReposHist.saveAll(jcicZ042);
      jcicZ042ReposHist.flush();
    }
    else {
      jcicZ042 = jcicZ042Repos.saveAll(jcicZ042);
      jcicZ042Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ042> jcicZ042, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ042 == null || jcicZ042.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ042ReposDay.deleteAll(jcicZ042);	
      jcicZ042ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ042ReposMon.deleteAll(jcicZ042);	
      jcicZ042ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ042ReposHist.deleteAll(jcicZ042);
      jcicZ042ReposHist.flush();
    }
    else {
      jcicZ042Repos.deleteAll(jcicZ042);
      jcicZ042Repos.flush();
    }
  }

}
