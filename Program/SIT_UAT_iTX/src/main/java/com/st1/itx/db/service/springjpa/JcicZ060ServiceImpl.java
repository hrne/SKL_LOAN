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
import com.st1.itx.db.domain.JcicZ060;
import com.st1.itx.db.domain.JcicZ060Id;
import com.st1.itx.db.repository.online.JcicZ060Repository;
import com.st1.itx.db.repository.day.JcicZ060RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ060RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ060RepositoryHist;
import com.st1.itx.db.service.JcicZ060Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ060Service")
@Repository
public class JcicZ060ServiceImpl implements JcicZ060Service, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ060ServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ060Repository jcicZ060Repos;

  @Autowired
  private JcicZ060RepositoryDay jcicZ060ReposDay;

  @Autowired
  private JcicZ060RepositoryMon jcicZ060ReposMon;

  @Autowired
  private JcicZ060RepositoryHist jcicZ060ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ060Repos);
    org.junit.Assert.assertNotNull(jcicZ060ReposDay);
    org.junit.Assert.assertNotNull(jcicZ060ReposMon);
    org.junit.Assert.assertNotNull(jcicZ060ReposHist);
  }

  @Override
  public JcicZ060 findById(JcicZ060Id jcicZ060Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ060Id);
    Optional<JcicZ060> jcicZ060 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ060 = jcicZ060ReposDay.findById(jcicZ060Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ060 = jcicZ060ReposMon.findById(jcicZ060Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ060 = jcicZ060ReposHist.findById(jcicZ060Id);
    else 
      jcicZ060 = jcicZ060Repos.findById(jcicZ060Id);
    JcicZ060 obj = jcicZ060.isPresent() ? jcicZ060.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ060> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ060> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "ChangePayDate"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ060ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ060ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ060ReposHist.findAll(pageable);
    else 
      slice = jcicZ060Repos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ060> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ060> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ060ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ060ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ060ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);
    else 
      slice = jcicZ060Repos.findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ060> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ060> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("RcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ060ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ060ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ060ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ060Repos.findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(rcDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ060> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ060> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ060ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ060ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ060ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ060Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(custId_0, rcDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ060 holdById(JcicZ060Id jcicZ060Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ060Id);
    Optional<JcicZ060> jcicZ060 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ060 = jcicZ060ReposDay.findByJcicZ060Id(jcicZ060Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ060 = jcicZ060ReposMon.findByJcicZ060Id(jcicZ060Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ060 = jcicZ060ReposHist.findByJcicZ060Id(jcicZ060Id);
    else 
      jcicZ060 = jcicZ060Repos.findByJcicZ060Id(jcicZ060Id);
    return jcicZ060.isPresent() ? jcicZ060.get() : null;
  }

  @Override
  public JcicZ060 holdById(JcicZ060 jcicZ060, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ060.getJcicZ060Id());
    Optional<JcicZ060> jcicZ060T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ060T = jcicZ060ReposDay.findByJcicZ060Id(jcicZ060.getJcicZ060Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ060T = jcicZ060ReposMon.findByJcicZ060Id(jcicZ060.getJcicZ060Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ060T = jcicZ060ReposHist.findByJcicZ060Id(jcicZ060.getJcicZ060Id());
    else 
      jcicZ060T = jcicZ060Repos.findByJcicZ060Id(jcicZ060.getJcicZ060Id());
    return jcicZ060T.isPresent() ? jcicZ060T.get() : null;
  }

  @Override
  public JcicZ060 insert(JcicZ060 jcicZ060, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ060.getJcicZ060Id());
    if (this.findById(jcicZ060.getJcicZ060Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ060.setCreateEmpNo(empNot);

    if(jcicZ060.getLastUpdateEmpNo() == null || jcicZ060.getLastUpdateEmpNo().isEmpty())
      jcicZ060.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ060ReposDay.saveAndFlush(jcicZ060);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ060ReposMon.saveAndFlush(jcicZ060);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ060ReposHist.saveAndFlush(jcicZ060);
    else 
    return jcicZ060Repos.saveAndFlush(jcicZ060);
  }

  @Override
  public JcicZ060 update(JcicZ060 jcicZ060, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ060.getJcicZ060Id());
    if (!empNot.isEmpty())
      jcicZ060.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ060ReposDay.saveAndFlush(jcicZ060);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ060ReposMon.saveAndFlush(jcicZ060);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ060ReposHist.saveAndFlush(jcicZ060);
    else 
    return jcicZ060Repos.saveAndFlush(jcicZ060);
  }

  @Override
  public JcicZ060 update2(JcicZ060 jcicZ060, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ060.getJcicZ060Id());
    if (!empNot.isEmpty())
      jcicZ060.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ060ReposDay.saveAndFlush(jcicZ060);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ060ReposMon.saveAndFlush(jcicZ060);
    else if (dbName.equals(ContentName.onHist))
        jcicZ060ReposHist.saveAndFlush(jcicZ060);
    else 
      jcicZ060Repos.saveAndFlush(jcicZ060);	
    return this.findById(jcicZ060.getJcicZ060Id());
  }

  @Override
  public void delete(JcicZ060 jcicZ060, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ060.getJcicZ060Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ060ReposDay.delete(jcicZ060);	
      jcicZ060ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ060ReposMon.delete(jcicZ060);	
      jcicZ060ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ060ReposHist.delete(jcicZ060);
      jcicZ060ReposHist.flush();
    }
    else {
      jcicZ060Repos.delete(jcicZ060);
      jcicZ060Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ060> jcicZ060, TitaVo... titaVo) throws DBException {
    if (jcicZ060 == null || jcicZ060.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ060 t : jcicZ060){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ060 = jcicZ060ReposDay.saveAll(jcicZ060);	
      jcicZ060ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ060 = jcicZ060ReposMon.saveAll(jcicZ060);	
      jcicZ060ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ060 = jcicZ060ReposHist.saveAll(jcicZ060);
      jcicZ060ReposHist.flush();
    }
    else {
      jcicZ060 = jcicZ060Repos.saveAll(jcicZ060);
      jcicZ060Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ060> jcicZ060, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ060 == null || jcicZ060.size() == 0)
      throw new DBException(6);

    for (JcicZ060 t : jcicZ060) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ060 = jcicZ060ReposDay.saveAll(jcicZ060);	
      jcicZ060ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ060 = jcicZ060ReposMon.saveAll(jcicZ060);	
      jcicZ060ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ060 = jcicZ060ReposHist.saveAll(jcicZ060);
      jcicZ060ReposHist.flush();
    }
    else {
      jcicZ060 = jcicZ060Repos.saveAll(jcicZ060);
      jcicZ060Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ060> jcicZ060, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ060 == null || jcicZ060.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ060ReposDay.deleteAll(jcicZ060);	
      jcicZ060ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ060ReposMon.deleteAll(jcicZ060);	
      jcicZ060ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ060ReposHist.deleteAll(jcicZ060);
      jcicZ060ReposHist.flush();
    }
    else {
      jcicZ060Repos.deleteAll(jcicZ060);
      jcicZ060Repos.flush();
    }
  }

}
