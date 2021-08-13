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
import com.st1.itx.db.domain.JcicZ053;
import com.st1.itx.db.domain.JcicZ053Id;
import com.st1.itx.db.repository.online.JcicZ053Repository;
import com.st1.itx.db.repository.day.JcicZ053RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ053RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ053RepositoryHist;
import com.st1.itx.db.service.JcicZ053Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ053Service")
@Repository
public class JcicZ053ServiceImpl implements JcicZ053Service, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ053ServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ053Repository jcicZ053Repos;

  @Autowired
  private JcicZ053RepositoryDay jcicZ053ReposDay;

  @Autowired
  private JcicZ053RepositoryMon jcicZ053ReposMon;

  @Autowired
  private JcicZ053RepositoryHist jcicZ053ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ053Repos);
    org.junit.Assert.assertNotNull(jcicZ053ReposDay);
    org.junit.Assert.assertNotNull(jcicZ053ReposMon);
    org.junit.Assert.assertNotNull(jcicZ053ReposHist);
  }

  @Override
  public JcicZ053 findById(JcicZ053Id jcicZ053Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ053Id);
    Optional<JcicZ053> jcicZ053 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ053 = jcicZ053ReposDay.findById(jcicZ053Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ053 = jcicZ053ReposMon.findById(jcicZ053Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ053 = jcicZ053ReposHist.findById(jcicZ053Id);
    else 
      jcicZ053 = jcicZ053Repos.findById(jcicZ053Id);
    JcicZ053 obj = jcicZ053.isPresent() ? jcicZ053.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ053> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ053> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "MaxMainCode"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ053ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ053ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ053ReposHist.findAll(pageable);
    else 
      slice = jcicZ053Repos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ053> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ053> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ053ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ053ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ053ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ053Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ053> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ053> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("RcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ053ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ053ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ053ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ053Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ053> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ053> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ053ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ053ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ053ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ053Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ053 holdById(JcicZ053Id jcicZ053Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ053Id);
    Optional<JcicZ053> jcicZ053 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ053 = jcicZ053ReposDay.findByJcicZ053Id(jcicZ053Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ053 = jcicZ053ReposMon.findByJcicZ053Id(jcicZ053Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ053 = jcicZ053ReposHist.findByJcicZ053Id(jcicZ053Id);
    else 
      jcicZ053 = jcicZ053Repos.findByJcicZ053Id(jcicZ053Id);
    return jcicZ053.isPresent() ? jcicZ053.get() : null;
  }

  @Override
  public JcicZ053 holdById(JcicZ053 jcicZ053, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ053.getJcicZ053Id());
    Optional<JcicZ053> jcicZ053T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ053T = jcicZ053ReposDay.findByJcicZ053Id(jcicZ053.getJcicZ053Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ053T = jcicZ053ReposMon.findByJcicZ053Id(jcicZ053.getJcicZ053Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ053T = jcicZ053ReposHist.findByJcicZ053Id(jcicZ053.getJcicZ053Id());
    else 
      jcicZ053T = jcicZ053Repos.findByJcicZ053Id(jcicZ053.getJcicZ053Id());
    return jcicZ053T.isPresent() ? jcicZ053T.get() : null;
  }

  @Override
  public JcicZ053 insert(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ053.getJcicZ053Id());
    if (this.findById(jcicZ053.getJcicZ053Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ053.setCreateEmpNo(empNot);

    if(jcicZ053.getLastUpdateEmpNo() == null || jcicZ053.getLastUpdateEmpNo().isEmpty())
      jcicZ053.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ053ReposDay.saveAndFlush(jcicZ053);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ053ReposMon.saveAndFlush(jcicZ053);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ053ReposHist.saveAndFlush(jcicZ053);
    else 
    return jcicZ053Repos.saveAndFlush(jcicZ053);
  }

  @Override
  public JcicZ053 update(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ053.getJcicZ053Id());
    if (!empNot.isEmpty())
      jcicZ053.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ053ReposDay.saveAndFlush(jcicZ053);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ053ReposMon.saveAndFlush(jcicZ053);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ053ReposHist.saveAndFlush(jcicZ053);
    else 
    return jcicZ053Repos.saveAndFlush(jcicZ053);
  }

  @Override
  public JcicZ053 update2(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ053.getJcicZ053Id());
    if (!empNot.isEmpty())
      jcicZ053.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ053ReposDay.saveAndFlush(jcicZ053);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ053ReposMon.saveAndFlush(jcicZ053);
    else if (dbName.equals(ContentName.onHist))
        jcicZ053ReposHist.saveAndFlush(jcicZ053);
    else 
      jcicZ053Repos.saveAndFlush(jcicZ053);	
    return this.findById(jcicZ053.getJcicZ053Id());
  }

  @Override
  public void delete(JcicZ053 jcicZ053, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ053.getJcicZ053Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ053ReposDay.delete(jcicZ053);	
      jcicZ053ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ053ReposMon.delete(jcicZ053);	
      jcicZ053ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ053ReposHist.delete(jcicZ053);
      jcicZ053ReposHist.flush();
    }
    else {
      jcicZ053Repos.delete(jcicZ053);
      jcicZ053Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ053> jcicZ053, TitaVo... titaVo) throws DBException {
    if (jcicZ053 == null || jcicZ053.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ053 t : jcicZ053){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ053 = jcicZ053ReposDay.saveAll(jcicZ053);	
      jcicZ053ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ053 = jcicZ053ReposMon.saveAll(jcicZ053);	
      jcicZ053ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ053 = jcicZ053ReposHist.saveAll(jcicZ053);
      jcicZ053ReposHist.flush();
    }
    else {
      jcicZ053 = jcicZ053Repos.saveAll(jcicZ053);
      jcicZ053Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ053> jcicZ053, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ053 == null || jcicZ053.size() == 0)
      throw new DBException(6);

    for (JcicZ053 t : jcicZ053) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ053 = jcicZ053ReposDay.saveAll(jcicZ053);	
      jcicZ053ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ053 = jcicZ053ReposMon.saveAll(jcicZ053);	
      jcicZ053ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ053 = jcicZ053ReposHist.saveAll(jcicZ053);
      jcicZ053ReposHist.flush();
    }
    else {
      jcicZ053 = jcicZ053Repos.saveAll(jcicZ053);
      jcicZ053Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ053> jcicZ053, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ053 == null || jcicZ053.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ053ReposDay.deleteAll(jcicZ053);	
      jcicZ053ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ053ReposMon.deleteAll(jcicZ053);	
      jcicZ053ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ053ReposHist.deleteAll(jcicZ053);
      jcicZ053ReposHist.flush();
    }
    else {
      jcicZ053Repos.deleteAll(jcicZ053);
      jcicZ053Repos.flush();
    }
  }

}
