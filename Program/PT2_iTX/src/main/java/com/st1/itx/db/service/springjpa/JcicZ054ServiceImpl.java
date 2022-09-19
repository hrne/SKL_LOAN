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
import com.st1.itx.db.domain.JcicZ054;
import com.st1.itx.db.domain.JcicZ054Id;
import com.st1.itx.db.repository.online.JcicZ054Repository;
import com.st1.itx.db.repository.day.JcicZ054RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ054RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ054RepositoryHist;
import com.st1.itx.db.service.JcicZ054Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ054Service")
@Repository
public class JcicZ054ServiceImpl extends ASpringJpaParm implements JcicZ054Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ054Repository jcicZ054Repos;

  @Autowired
  private JcicZ054RepositoryDay jcicZ054ReposDay;

  @Autowired
  private JcicZ054RepositoryMon jcicZ054ReposMon;

  @Autowired
  private JcicZ054RepositoryHist jcicZ054ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ054Repos);
    org.junit.Assert.assertNotNull(jcicZ054ReposDay);
    org.junit.Assert.assertNotNull(jcicZ054ReposMon);
    org.junit.Assert.assertNotNull(jcicZ054ReposHist);
  }

  @Override
  public JcicZ054 findById(JcicZ054Id jcicZ054Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ054Id);
    Optional<JcicZ054> jcicZ054 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ054 = jcicZ054ReposDay.findById(jcicZ054Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ054 = jcicZ054ReposMon.findById(jcicZ054Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ054 = jcicZ054ReposHist.findById(jcicZ054Id);
    else 
      jcicZ054 = jcicZ054Repos.findById(jcicZ054Id);
    JcicZ054 obj = jcicZ054.isPresent() ? jcicZ054.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ054> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustId", "SubmitKey", "RcDate", "MaxMainCode", "PayOffDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustId", "SubmitKey", "RcDate", "MaxMainCode", "PayOffDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054ReposHist.findAll(pageable);
    else 
      slice = jcicZ054Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ054> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ054Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ054> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ054Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ054> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ054Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ054> otherEq(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, int payOffDate_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " maxMainCode_3 : " +  maxMainCode_3 + " payOffDate_4 : " +  payOffDate_4);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndPayOffDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, payOffDate_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndPayOffDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, payOffDate_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndPayOffDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, payOffDate_4, pageable);
    else 
      slice = jcicZ054Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndPayOffDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, payOffDate_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ054 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ054> jcicZ054T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ054T = jcicZ054ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ054T = jcicZ054ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ054T = jcicZ054ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ054T = jcicZ054Repos.findTopByUkeyIs(ukey_0);

    return jcicZ054T.isPresent() ? jcicZ054T.get() : null;
  }

  @Override
  public JcicZ054 otherFirst(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, int payOffDate_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " maxMainCode_3 : " +  maxMainCode_3 + " payOffDate_4 : " +  payOffDate_4);
    Optional<JcicZ054> jcicZ054T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ054T = jcicZ054ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndPayOffDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, payOffDate_4);
    else if (dbName.equals(ContentName.onMon))
      jcicZ054T = jcicZ054ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndPayOffDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, payOffDate_4);
    else if (dbName.equals(ContentName.onHist))
      jcicZ054T = jcicZ054ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndPayOffDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, payOffDate_4);
    else 
      jcicZ054T = jcicZ054Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsAndPayOffDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, maxMainCode_3, payOffDate_4);

    return jcicZ054T.isPresent() ? jcicZ054T.get() : null;
  }

  @Override
  public Slice<JcicZ054> custRcSubEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcSubEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else 
      slice = jcicZ054Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ054> findkeyFilingDate(int actualFilingDate_0, String actualFilingMark_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findkeyFilingDate " + dbName + " : " + "actualFilingDate_0 : " + actualFilingDate_0 + " actualFilingMark_1 : " +  actualFilingMark_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054ReposDay.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054ReposMon.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054ReposHist.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);
    else 
      slice = jcicZ054Repos.findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(actualFilingDate_0, actualFilingMark_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ054 holdById(JcicZ054Id jcicZ054Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ054Id);
    Optional<JcicZ054> jcicZ054 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ054 = jcicZ054ReposDay.findByJcicZ054Id(jcicZ054Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ054 = jcicZ054ReposMon.findByJcicZ054Id(jcicZ054Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ054 = jcicZ054ReposHist.findByJcicZ054Id(jcicZ054Id);
    else 
      jcicZ054 = jcicZ054Repos.findByJcicZ054Id(jcicZ054Id);
    return jcicZ054.isPresent() ? jcicZ054.get() : null;
  }

  @Override
  public JcicZ054 holdById(JcicZ054 jcicZ054, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ054.getJcicZ054Id());
    Optional<JcicZ054> jcicZ054T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ054T = jcicZ054ReposDay.findByJcicZ054Id(jcicZ054.getJcicZ054Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ054T = jcicZ054ReposMon.findByJcicZ054Id(jcicZ054.getJcicZ054Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ054T = jcicZ054ReposHist.findByJcicZ054Id(jcicZ054.getJcicZ054Id());
    else 
      jcicZ054T = jcicZ054Repos.findByJcicZ054Id(jcicZ054.getJcicZ054Id());
    return jcicZ054T.isPresent() ? jcicZ054T.get() : null;
  }

  @Override
  public JcicZ054 insert(JcicZ054 jcicZ054, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ054.getJcicZ054Id());
    if (this.findById(jcicZ054.getJcicZ054Id(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ054.setCreateEmpNo(empNot);

    if(jcicZ054.getLastUpdateEmpNo() == null || jcicZ054.getLastUpdateEmpNo().isEmpty())
      jcicZ054.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ054ReposDay.saveAndFlush(jcicZ054);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ054ReposMon.saveAndFlush(jcicZ054);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ054ReposHist.saveAndFlush(jcicZ054);
    else 
    return jcicZ054Repos.saveAndFlush(jcicZ054);
  }

  @Override
  public JcicZ054 update(JcicZ054 jcicZ054, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ054.getJcicZ054Id());
    if (!empNot.isEmpty())
      jcicZ054.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ054ReposDay.saveAndFlush(jcicZ054);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ054ReposMon.saveAndFlush(jcicZ054);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ054ReposHist.saveAndFlush(jcicZ054);
    else 
    return jcicZ054Repos.saveAndFlush(jcicZ054);
  }

  @Override
  public JcicZ054 update2(JcicZ054 jcicZ054, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ054.getJcicZ054Id());
    if (!empNot.isEmpty())
      jcicZ054.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ054ReposDay.saveAndFlush(jcicZ054);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ054ReposMon.saveAndFlush(jcicZ054);
    else if (dbName.equals(ContentName.onHist))
        jcicZ054ReposHist.saveAndFlush(jcicZ054);
    else 
      jcicZ054Repos.saveAndFlush(jcicZ054);	
    return this.findById(jcicZ054.getJcicZ054Id());
  }

  @Override
  public void delete(JcicZ054 jcicZ054, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ054.getJcicZ054Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ054ReposDay.delete(jcicZ054);	
      jcicZ054ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ054ReposMon.delete(jcicZ054);	
      jcicZ054ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ054ReposHist.delete(jcicZ054);
      jcicZ054ReposHist.flush();
    }
    else {
      jcicZ054Repos.delete(jcicZ054);
      jcicZ054Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ054> jcicZ054, TitaVo... titaVo) throws DBException {
    if (jcicZ054 == null || jcicZ054.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ054 t : jcicZ054){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ054 = jcicZ054ReposDay.saveAll(jcicZ054);	
      jcicZ054ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ054 = jcicZ054ReposMon.saveAll(jcicZ054);	
      jcicZ054ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ054 = jcicZ054ReposHist.saveAll(jcicZ054);
      jcicZ054ReposHist.flush();
    }
    else {
      jcicZ054 = jcicZ054Repos.saveAll(jcicZ054);
      jcicZ054Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ054> jcicZ054, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ054 == null || jcicZ054.size() == 0)
      throw new DBException(6);

    for (JcicZ054 t : jcicZ054) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ054 = jcicZ054ReposDay.saveAll(jcicZ054);	
      jcicZ054ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ054 = jcicZ054ReposMon.saveAll(jcicZ054);	
      jcicZ054ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ054 = jcicZ054ReposHist.saveAll(jcicZ054);
      jcicZ054ReposHist.flush();
    }
    else {
      jcicZ054 = jcicZ054Repos.saveAll(jcicZ054);
      jcicZ054Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ054> jcicZ054, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ054 == null || jcicZ054.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ054ReposDay.deleteAll(jcicZ054);	
      jcicZ054ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ054ReposMon.deleteAll(jcicZ054);	
      jcicZ054ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ054ReposHist.deleteAll(jcicZ054);
      jcicZ054ReposHist.flush();
    }
    else {
      jcicZ054Repos.deleteAll(jcicZ054);
      jcicZ054Repos.flush();
    }
  }

}
