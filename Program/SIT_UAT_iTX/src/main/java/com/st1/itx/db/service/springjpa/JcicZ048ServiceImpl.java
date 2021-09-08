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
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ048Id;
import com.st1.itx.db.repository.online.JcicZ048Repository;
import com.st1.itx.db.repository.day.JcicZ048RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ048RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ048RepositoryHist;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ048Service")
@Repository
public class JcicZ048ServiceImpl extends ASpringJpaParm implements JcicZ048Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ048Repository jcicZ048Repos;

  @Autowired
  private JcicZ048RepositoryDay jcicZ048ReposDay;

  @Autowired
  private JcicZ048RepositoryMon jcicZ048ReposMon;

  @Autowired
  private JcicZ048RepositoryHist jcicZ048ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ048Repos);
    org.junit.Assert.assertNotNull(jcicZ048ReposDay);
    org.junit.Assert.assertNotNull(jcicZ048ReposMon);
    org.junit.Assert.assertNotNull(jcicZ048ReposHist);
  }

  @Override
  public JcicZ048 findById(JcicZ048Id jcicZ048Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ048Id);
    Optional<JcicZ048> jcicZ048 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ048 = jcicZ048ReposDay.findById(jcicZ048Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ048 = jcicZ048ReposMon.findById(jcicZ048Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ048 = jcicZ048ReposHist.findById(jcicZ048Id);
    else 
      jcicZ048 = jcicZ048Repos.findById(jcicZ048Id);
    JcicZ048 obj = jcicZ048.isPresent() ? jcicZ048.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ048> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ048> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ048ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ048ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ048ReposHist.findAll(pageable);
    else 
      slice = jcicZ048Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ048> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ048> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ048ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ048ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ048ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ048Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ048> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ048> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("rcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ048ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ048ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ048ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ048Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ048> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ048> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ048ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ048ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ048ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ048Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ048> RcDateBetween(int rcDate_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ048> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("RcDateBetween " + dbName + " : " + "rcDate_0 : " + rcDate_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ048ReposDay.findAllByRcDateGreaterThanEqualAndRcDateLessThanEqualOrderByCustIdAscRcDateDesc(rcDate_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ048ReposMon.findAllByRcDateGreaterThanEqualAndRcDateLessThanEqualOrderByCustIdAscRcDateDesc(rcDate_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ048ReposHist.findAllByRcDateGreaterThanEqualAndRcDateLessThanEqualOrderByCustIdAscRcDateDesc(rcDate_0, rcDate_1, pageable);
    else 
      slice = jcicZ048Repos.findAllByRcDateGreaterThanEqualAndRcDateLessThanEqualOrderByCustIdAscRcDateDesc(rcDate_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ048> CustIdRcBetween(String custId_0, int rcDate_1, int rcDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ048> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustIdRcBetween " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1 + " rcDate_2 : " +  rcDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ048ReposDay.findAllByCustIdIsAndRcDateGreaterThanEqualAndRcDateLessThanEqualOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ048ReposMon.findAllByCustIdIsAndRcDateGreaterThanEqualAndRcDateLessThanEqualOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ048ReposHist.findAllByCustIdIsAndRcDateGreaterThanEqualAndRcDateLessThanEqualOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, rcDate_2, pageable);
    else 
      slice = jcicZ048Repos.findAllByCustIdIsAndRcDateGreaterThanEqualAndRcDateLessThanEqualOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, rcDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ048> otherEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ048> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ048ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ048ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ048ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
    else 
      slice = jcicZ048Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ048 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ048> jcicZ048T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ048T = jcicZ048ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ048T = jcicZ048ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ048T = jcicZ048ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ048T = jcicZ048Repos.findTopByUkeyIs(ukey_0);

    return jcicZ048T.isPresent() ? jcicZ048T.get() : null;
  }

  @Override
  public JcicZ048 otherFirst(String submitKey_0, String custId_1, int rcDate_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2);
    Optional<JcicZ048> jcicZ048T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ048T = jcicZ048ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else if (dbName.equals(ContentName.onMon))
      jcicZ048T = jcicZ048ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else if (dbName.equals(ContentName.onHist))
      jcicZ048T = jcicZ048ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
    else 
      jcicZ048T = jcicZ048Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);

    return jcicZ048T.isPresent() ? jcicZ048T.get() : null;
  }

  @Override
  public JcicZ048 holdById(JcicZ048Id jcicZ048Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ048Id);
    Optional<JcicZ048> jcicZ048 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ048 = jcicZ048ReposDay.findByJcicZ048Id(jcicZ048Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ048 = jcicZ048ReposMon.findByJcicZ048Id(jcicZ048Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ048 = jcicZ048ReposHist.findByJcicZ048Id(jcicZ048Id);
    else 
      jcicZ048 = jcicZ048Repos.findByJcicZ048Id(jcicZ048Id);
    return jcicZ048.isPresent() ? jcicZ048.get() : null;
  }

  @Override
  public JcicZ048 holdById(JcicZ048 jcicZ048, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ048.getJcicZ048Id());
    Optional<JcicZ048> jcicZ048T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ048T = jcicZ048ReposDay.findByJcicZ048Id(jcicZ048.getJcicZ048Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ048T = jcicZ048ReposMon.findByJcicZ048Id(jcicZ048.getJcicZ048Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ048T = jcicZ048ReposHist.findByJcicZ048Id(jcicZ048.getJcicZ048Id());
    else 
      jcicZ048T = jcicZ048Repos.findByJcicZ048Id(jcicZ048.getJcicZ048Id());
    return jcicZ048T.isPresent() ? jcicZ048T.get() : null;
  }

  @Override
  public JcicZ048 insert(JcicZ048 jcicZ048, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ048.getJcicZ048Id());
    if (this.findById(jcicZ048.getJcicZ048Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ048.setCreateEmpNo(empNot);

    if(jcicZ048.getLastUpdateEmpNo() == null || jcicZ048.getLastUpdateEmpNo().isEmpty())
      jcicZ048.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ048ReposDay.saveAndFlush(jcicZ048);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ048ReposMon.saveAndFlush(jcicZ048);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ048ReposHist.saveAndFlush(jcicZ048);
    else 
    return jcicZ048Repos.saveAndFlush(jcicZ048);
  }

  @Override
  public JcicZ048 update(JcicZ048 jcicZ048, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ048.getJcicZ048Id());
    if (!empNot.isEmpty())
      jcicZ048.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ048ReposDay.saveAndFlush(jcicZ048);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ048ReposMon.saveAndFlush(jcicZ048);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ048ReposHist.saveAndFlush(jcicZ048);
    else 
    return jcicZ048Repos.saveAndFlush(jcicZ048);
  }

  @Override
  public JcicZ048 update2(JcicZ048 jcicZ048, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ048.getJcicZ048Id());
    if (!empNot.isEmpty())
      jcicZ048.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ048ReposDay.saveAndFlush(jcicZ048);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ048ReposMon.saveAndFlush(jcicZ048);
    else if (dbName.equals(ContentName.onHist))
        jcicZ048ReposHist.saveAndFlush(jcicZ048);
    else 
      jcicZ048Repos.saveAndFlush(jcicZ048);	
    return this.findById(jcicZ048.getJcicZ048Id());
  }

  @Override
  public void delete(JcicZ048 jcicZ048, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ048.getJcicZ048Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ048ReposDay.delete(jcicZ048);	
      jcicZ048ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ048ReposMon.delete(jcicZ048);	
      jcicZ048ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ048ReposHist.delete(jcicZ048);
      jcicZ048ReposHist.flush();
    }
    else {
      jcicZ048Repos.delete(jcicZ048);
      jcicZ048Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ048> jcicZ048, TitaVo... titaVo) throws DBException {
    if (jcicZ048 == null || jcicZ048.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ048 t : jcicZ048){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ048 = jcicZ048ReposDay.saveAll(jcicZ048);	
      jcicZ048ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ048 = jcicZ048ReposMon.saveAll(jcicZ048);	
      jcicZ048ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ048 = jcicZ048ReposHist.saveAll(jcicZ048);
      jcicZ048ReposHist.flush();
    }
    else {
      jcicZ048 = jcicZ048Repos.saveAll(jcicZ048);
      jcicZ048Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ048> jcicZ048, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ048 == null || jcicZ048.size() == 0)
      throw new DBException(6);

    for (JcicZ048 t : jcicZ048) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ048 = jcicZ048ReposDay.saveAll(jcicZ048);	
      jcicZ048ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ048 = jcicZ048ReposMon.saveAll(jcicZ048);	
      jcicZ048ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ048 = jcicZ048ReposHist.saveAll(jcicZ048);
      jcicZ048ReposHist.flush();
    }
    else {
      jcicZ048 = jcicZ048Repos.saveAll(jcicZ048);
      jcicZ048Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ048> jcicZ048, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ048 == null || jcicZ048.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ048ReposDay.deleteAll(jcicZ048);	
      jcicZ048ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ048ReposMon.deleteAll(jcicZ048);	
      jcicZ048ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ048ReposHist.deleteAll(jcicZ048);
      jcicZ048ReposHist.flush();
    }
    else {
      jcicZ048Repos.deleteAll(jcicZ048);
      jcicZ048Repos.flush();
    }
  }

}
