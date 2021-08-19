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
import com.st1.itx.db.domain.JcicZ063;
import com.st1.itx.db.domain.JcicZ063Id;
import com.st1.itx.db.repository.online.JcicZ063Repository;
import com.st1.itx.db.repository.day.JcicZ063RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ063RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ063RepositoryHist;
import com.st1.itx.db.service.JcicZ063Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ063Service")
@Repository
public class JcicZ063ServiceImpl extends ASpringJpaParm implements JcicZ063Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ063Repository jcicZ063Repos;

  @Autowired
  private JcicZ063RepositoryDay jcicZ063ReposDay;

  @Autowired
  private JcicZ063RepositoryMon jcicZ063ReposMon;

  @Autowired
  private JcicZ063RepositoryHist jcicZ063ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ063Repos);
    org.junit.Assert.assertNotNull(jcicZ063ReposDay);
    org.junit.Assert.assertNotNull(jcicZ063ReposMon);
    org.junit.Assert.assertNotNull(jcicZ063ReposHist);
  }

  @Override
  public JcicZ063 findById(JcicZ063Id jcicZ063Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ063Id);
    Optional<JcicZ063> jcicZ063 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ063 = jcicZ063ReposDay.findById(jcicZ063Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ063 = jcicZ063ReposMon.findById(jcicZ063Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ063 = jcicZ063ReposHist.findById(jcicZ063Id);
    else 
      jcicZ063 = jcicZ063Repos.findById(jcicZ063Id);
    JcicZ063 obj = jcicZ063.isPresent() ? jcicZ063.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ063> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ063> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "ChangePayDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "ChangePayDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ063ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ063ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ063ReposHist.findAll(pageable);
    else 
      slice = jcicZ063Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ063> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ063> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ063ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ063ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ063ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(custId_0, pageable);
    else 
      slice = jcicZ063Repos.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(custId_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ063> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ063> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("RcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ063ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ063ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ063ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ063Repos.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(rcDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ063> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ063> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ063ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ063ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ063ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ063Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ063> otherEq(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ063> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " changePayDate_3 : " +  changePayDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ063ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ063ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ063ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, pageable);
    else 
      slice = jcicZ063Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ063 ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ063> jcicZ063T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ063T = jcicZ063ReposDay.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ063T = jcicZ063ReposMon.findTopByUkeyIs(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ063T = jcicZ063ReposHist.findTopByUkeyIs(ukey_0);
    else 
      jcicZ063T = jcicZ063Repos.findTopByUkeyIs(ukey_0);

    return jcicZ063T.isPresent() ? jcicZ063T.get() : null;
  }

  @Override
  public JcicZ063 otherFirst(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " +  custId_1 + " rcDate_2 : " +  rcDate_2 + " changePayDate_3 : " +  changePayDate_3);
    Optional<JcicZ063> jcicZ063T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ063T = jcicZ063ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3);
    else if (dbName.equals(ContentName.onMon))
      jcicZ063T = jcicZ063ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3);
    else if (dbName.equals(ContentName.onHist))
      jcicZ063T = jcicZ063ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3);
    else 
      jcicZ063T = jcicZ063Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, changePayDate_3);

    return jcicZ063T.isPresent() ? jcicZ063T.get() : null;
  }

  @Override
  public JcicZ063 holdById(JcicZ063Id jcicZ063Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ063Id);
    Optional<JcicZ063> jcicZ063 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ063 = jcicZ063ReposDay.findByJcicZ063Id(jcicZ063Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ063 = jcicZ063ReposMon.findByJcicZ063Id(jcicZ063Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ063 = jcicZ063ReposHist.findByJcicZ063Id(jcicZ063Id);
    else 
      jcicZ063 = jcicZ063Repos.findByJcicZ063Id(jcicZ063Id);
    return jcicZ063.isPresent() ? jcicZ063.get() : null;
  }

  @Override
  public JcicZ063 holdById(JcicZ063 jcicZ063, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ063.getJcicZ063Id());
    Optional<JcicZ063> jcicZ063T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ063T = jcicZ063ReposDay.findByJcicZ063Id(jcicZ063.getJcicZ063Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ063T = jcicZ063ReposMon.findByJcicZ063Id(jcicZ063.getJcicZ063Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ063T = jcicZ063ReposHist.findByJcicZ063Id(jcicZ063.getJcicZ063Id());
    else 
      jcicZ063T = jcicZ063Repos.findByJcicZ063Id(jcicZ063.getJcicZ063Id());
    return jcicZ063T.isPresent() ? jcicZ063T.get() : null;
  }

  @Override
  public JcicZ063 insert(JcicZ063 jcicZ063, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ063.getJcicZ063Id());
    if (this.findById(jcicZ063.getJcicZ063Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ063.setCreateEmpNo(empNot);

    if(jcicZ063.getLastUpdateEmpNo() == null || jcicZ063.getLastUpdateEmpNo().isEmpty())
      jcicZ063.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ063ReposDay.saveAndFlush(jcicZ063);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ063ReposMon.saveAndFlush(jcicZ063);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ063ReposHist.saveAndFlush(jcicZ063);
    else 
    return jcicZ063Repos.saveAndFlush(jcicZ063);
  }

  @Override
  public JcicZ063 update(JcicZ063 jcicZ063, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ063.getJcicZ063Id());
    if (!empNot.isEmpty())
      jcicZ063.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ063ReposDay.saveAndFlush(jcicZ063);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ063ReposMon.saveAndFlush(jcicZ063);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ063ReposHist.saveAndFlush(jcicZ063);
    else 
    return jcicZ063Repos.saveAndFlush(jcicZ063);
  }

  @Override
  public JcicZ063 update2(JcicZ063 jcicZ063, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ063.getJcicZ063Id());
    if (!empNot.isEmpty())
      jcicZ063.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ063ReposDay.saveAndFlush(jcicZ063);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ063ReposMon.saveAndFlush(jcicZ063);
    else if (dbName.equals(ContentName.onHist))
        jcicZ063ReposHist.saveAndFlush(jcicZ063);
    else 
      jcicZ063Repos.saveAndFlush(jcicZ063);	
    return this.findById(jcicZ063.getJcicZ063Id());
  }

  @Override
  public void delete(JcicZ063 jcicZ063, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ063.getJcicZ063Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ063ReposDay.delete(jcicZ063);	
      jcicZ063ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ063ReposMon.delete(jcicZ063);	
      jcicZ063ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ063ReposHist.delete(jcicZ063);
      jcicZ063ReposHist.flush();
    }
    else {
      jcicZ063Repos.delete(jcicZ063);
      jcicZ063Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ063> jcicZ063, TitaVo... titaVo) throws DBException {
    if (jcicZ063 == null || jcicZ063.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ063 t : jcicZ063){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ063 = jcicZ063ReposDay.saveAll(jcicZ063);	
      jcicZ063ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ063 = jcicZ063ReposMon.saveAll(jcicZ063);	
      jcicZ063ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ063 = jcicZ063ReposHist.saveAll(jcicZ063);
      jcicZ063ReposHist.flush();
    }
    else {
      jcicZ063 = jcicZ063Repos.saveAll(jcicZ063);
      jcicZ063Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ063> jcicZ063, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ063 == null || jcicZ063.size() == 0)
      throw new DBException(6);

    for (JcicZ063 t : jcicZ063) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ063 = jcicZ063ReposDay.saveAll(jcicZ063);	
      jcicZ063ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ063 = jcicZ063ReposMon.saveAll(jcicZ063);	
      jcicZ063ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ063 = jcicZ063ReposHist.saveAll(jcicZ063);
      jcicZ063ReposHist.flush();
    }
    else {
      jcicZ063 = jcicZ063Repos.saveAll(jcicZ063);
      jcicZ063Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ063> jcicZ063, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ063 == null || jcicZ063.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ063ReposDay.deleteAll(jcicZ063);	
      jcicZ063ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ063ReposMon.deleteAll(jcicZ063);	
      jcicZ063ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ063ReposHist.deleteAll(jcicZ063);
      jcicZ063ReposHist.flush();
    }
    else {
      jcicZ063Repos.deleteAll(jcicZ063);
      jcicZ063Repos.flush();
    }
  }

}
