package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ447Id;
import com.st1.itx.db.repository.online.JcicZ447Repository;
import com.st1.itx.db.repository.day.JcicZ447RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ447RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ447RepositoryHist;
import com.st1.itx.db.service.JcicZ447Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ447Service")
@Repository
public class JcicZ447ServiceImpl implements JcicZ447Service, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ447ServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ447Repository jcicZ447Repos;

  @Autowired
  private JcicZ447RepositoryDay jcicZ447ReposDay;

  @Autowired
  private JcicZ447RepositoryMon jcicZ447ReposMon;

  @Autowired
  private JcicZ447RepositoryHist jcicZ447ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ447Repos);
    org.junit.Assert.assertNotNull(jcicZ447ReposDay);
    org.junit.Assert.assertNotNull(jcicZ447ReposMon);
    org.junit.Assert.assertNotNull(jcicZ447ReposHist);
  }

  @Override
  public JcicZ447 findById(JcicZ447Id jcicZ447Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ447Id);
    Optional<JcicZ447> jcicZ447 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ447 = jcicZ447ReposDay.findById(jcicZ447Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ447 = jcicZ447ReposMon.findById(jcicZ447Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ447 = jcicZ447ReposHist.findById(jcicZ447Id);
    else 
      jcicZ447 = jcicZ447Repos.findById(jcicZ447Id);
    JcicZ447 obj = jcicZ447.isPresent() ? jcicZ447.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ447> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ447> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "BankId"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ447ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ447ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ447ReposHist.findAll(pageable);
    else 
      slice = jcicZ447Repos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ447> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ447> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ447ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ447ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ447ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
    else 
      slice = jcicZ447Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ447> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ447> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ447ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ447ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ447ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
    else 
      slice = jcicZ447Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ447> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ447> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " +  applyDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ447ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ447ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ447ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
    else 
      slice = jcicZ447Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ447 holdById(JcicZ447Id jcicZ447Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ447Id);
    Optional<JcicZ447> jcicZ447 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ447 = jcicZ447ReposDay.findByJcicZ447Id(jcicZ447Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ447 = jcicZ447ReposMon.findByJcicZ447Id(jcicZ447Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ447 = jcicZ447ReposHist.findByJcicZ447Id(jcicZ447Id);
    else 
      jcicZ447 = jcicZ447Repos.findByJcicZ447Id(jcicZ447Id);
    return jcicZ447.isPresent() ? jcicZ447.get() : null;
  }

  @Override
  public JcicZ447 holdById(JcicZ447 jcicZ447, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ447.getJcicZ447Id());
    Optional<JcicZ447> jcicZ447T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ447T = jcicZ447ReposDay.findByJcicZ447Id(jcicZ447.getJcicZ447Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ447T = jcicZ447ReposMon.findByJcicZ447Id(jcicZ447.getJcicZ447Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ447T = jcicZ447ReposHist.findByJcicZ447Id(jcicZ447.getJcicZ447Id());
    else 
      jcicZ447T = jcicZ447Repos.findByJcicZ447Id(jcicZ447.getJcicZ447Id());
    return jcicZ447T.isPresent() ? jcicZ447T.get() : null;
  }

  @Override
  public JcicZ447 insert(JcicZ447 jcicZ447, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ447.getJcicZ447Id());
    if (this.findById(jcicZ447.getJcicZ447Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ447.setCreateEmpNo(empNot);

    if(jcicZ447.getLastUpdateEmpNo() == null || jcicZ447.getLastUpdateEmpNo().isEmpty())
      jcicZ447.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ447ReposDay.saveAndFlush(jcicZ447);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ447ReposMon.saveAndFlush(jcicZ447);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ447ReposHist.saveAndFlush(jcicZ447);
    else 
    return jcicZ447Repos.saveAndFlush(jcicZ447);
  }

  @Override
  public JcicZ447 update(JcicZ447 jcicZ447, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ447.getJcicZ447Id());
    if (!empNot.isEmpty())
      jcicZ447.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ447ReposDay.saveAndFlush(jcicZ447);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ447ReposMon.saveAndFlush(jcicZ447);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ447ReposHist.saveAndFlush(jcicZ447);
    else 
    return jcicZ447Repos.saveAndFlush(jcicZ447);
  }

  @Override
  public JcicZ447 update2(JcicZ447 jcicZ447, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ447.getJcicZ447Id());
    if (!empNot.isEmpty())
      jcicZ447.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ447ReposDay.saveAndFlush(jcicZ447);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ447ReposMon.saveAndFlush(jcicZ447);
    else if (dbName.equals(ContentName.onHist))
        jcicZ447ReposHist.saveAndFlush(jcicZ447);
    else 
      jcicZ447Repos.saveAndFlush(jcicZ447);	
    return this.findById(jcicZ447.getJcicZ447Id());
  }

  @Override
  public void delete(JcicZ447 jcicZ447, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ447.getJcicZ447Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ447ReposDay.delete(jcicZ447);	
      jcicZ447ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ447ReposMon.delete(jcicZ447);	
      jcicZ447ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ447ReposHist.delete(jcicZ447);
      jcicZ447ReposHist.flush();
    }
    else {
      jcicZ447Repos.delete(jcicZ447);
      jcicZ447Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ447> jcicZ447, TitaVo... titaVo) throws DBException {
    if (jcicZ447 == null || jcicZ447.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ447 t : jcicZ447){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ447 = jcicZ447ReposDay.saveAll(jcicZ447);	
      jcicZ447ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ447 = jcicZ447ReposMon.saveAll(jcicZ447);	
      jcicZ447ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ447 = jcicZ447ReposHist.saveAll(jcicZ447);
      jcicZ447ReposHist.flush();
    }
    else {
      jcicZ447 = jcicZ447Repos.saveAll(jcicZ447);
      jcicZ447Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ447> jcicZ447, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ447 == null || jcicZ447.size() == 0)
      throw new DBException(6);

    for (JcicZ447 t : jcicZ447) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ447 = jcicZ447ReposDay.saveAll(jcicZ447);	
      jcicZ447ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ447 = jcicZ447ReposMon.saveAll(jcicZ447);	
      jcicZ447ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ447 = jcicZ447ReposHist.saveAll(jcicZ447);
      jcicZ447ReposHist.flush();
    }
    else {
      jcicZ447 = jcicZ447Repos.saveAll(jcicZ447);
      jcicZ447Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ447> jcicZ447, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ447 == null || jcicZ447.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ447ReposDay.deleteAll(jcicZ447);	
      jcicZ447ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ447ReposMon.deleteAll(jcicZ447);	
      jcicZ447ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ447ReposHist.deleteAll(jcicZ447);
      jcicZ447ReposHist.flush();
    }
    else {
      jcicZ447Repos.deleteAll(jcicZ447);
      jcicZ447Repos.flush();
    }
  }

}
