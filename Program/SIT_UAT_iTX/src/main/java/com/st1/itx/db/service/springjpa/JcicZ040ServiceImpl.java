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
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.domain.JcicZ040Id;
import com.st1.itx.db.repository.online.JcicZ040Repository;
import com.st1.itx.db.repository.day.JcicZ040RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ040RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ040RepositoryHist;
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ040Service")
@Repository
public class JcicZ040ServiceImpl implements JcicZ040Service, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ040ServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ040Repository jcicZ040Repos;

  @Autowired
  private JcicZ040RepositoryDay jcicZ040ReposDay;

  @Autowired
  private JcicZ040RepositoryMon jcicZ040ReposMon;

  @Autowired
  private JcicZ040RepositoryHist jcicZ040ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ040Repos);
    org.junit.Assert.assertNotNull(jcicZ040ReposDay);
    org.junit.Assert.assertNotNull(jcicZ040ReposMon);
    org.junit.Assert.assertNotNull(jcicZ040ReposHist);
  }

  @Override
  public JcicZ040 findById(JcicZ040Id jcicZ040Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ040Id);
    Optional<JcicZ040> jcicZ040 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ040 = jcicZ040ReposDay.findById(jcicZ040Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ040 = jcicZ040ReposMon.findById(jcicZ040Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ040 = jcicZ040ReposHist.findById(jcicZ040Id);
    else 
      jcicZ040 = jcicZ040Repos.findById(jcicZ040Id);
    JcicZ040 obj = jcicZ040.isPresent() ? jcicZ040.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ040> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ040> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustId", "SubmitKey", "RcDate"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ040ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ040ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ040ReposHist.findAll(pageable);
    else 
      slice = jcicZ040Repos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ040> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ040> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ040ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ040ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ040ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ040Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ040> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ040> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("RcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ040ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ040ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ040ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ040Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ040> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ040> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ040ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ040ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ040ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ040Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ040 holdById(JcicZ040Id jcicZ040Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ040Id);
    Optional<JcicZ040> jcicZ040 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ040 = jcicZ040ReposDay.findByJcicZ040Id(jcicZ040Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ040 = jcicZ040ReposMon.findByJcicZ040Id(jcicZ040Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ040 = jcicZ040ReposHist.findByJcicZ040Id(jcicZ040Id);
    else 
      jcicZ040 = jcicZ040Repos.findByJcicZ040Id(jcicZ040Id);
    return jcicZ040.isPresent() ? jcicZ040.get() : null;
  }

  @Override
  public JcicZ040 holdById(JcicZ040 jcicZ040, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ040.getJcicZ040Id());
    Optional<JcicZ040> jcicZ040T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ040T = jcicZ040ReposDay.findByJcicZ040Id(jcicZ040.getJcicZ040Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ040T = jcicZ040ReposMon.findByJcicZ040Id(jcicZ040.getJcicZ040Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ040T = jcicZ040ReposHist.findByJcicZ040Id(jcicZ040.getJcicZ040Id());
    else 
      jcicZ040T = jcicZ040Repos.findByJcicZ040Id(jcicZ040.getJcicZ040Id());
    return jcicZ040T.isPresent() ? jcicZ040T.get() : null;
  }

  @Override
  public JcicZ040 insert(JcicZ040 jcicZ040, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ040.getJcicZ040Id());
    if (this.findById(jcicZ040.getJcicZ040Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ040.setCreateEmpNo(empNot);

    if(jcicZ040.getLastUpdateEmpNo() == null || jcicZ040.getLastUpdateEmpNo().isEmpty())
      jcicZ040.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ040ReposDay.saveAndFlush(jcicZ040);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ040ReposMon.saveAndFlush(jcicZ040);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ040ReposHist.saveAndFlush(jcicZ040);
    else 
    return jcicZ040Repos.saveAndFlush(jcicZ040);
  }

  @Override
  public JcicZ040 update(JcicZ040 jcicZ040, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ040.getJcicZ040Id());
    if (!empNot.isEmpty())
      jcicZ040.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ040ReposDay.saveAndFlush(jcicZ040);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ040ReposMon.saveAndFlush(jcicZ040);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ040ReposHist.saveAndFlush(jcicZ040);
    else 
    return jcicZ040Repos.saveAndFlush(jcicZ040);
  }

  @Override
  public JcicZ040 update2(JcicZ040 jcicZ040, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ040.getJcicZ040Id());
    if (!empNot.isEmpty())
      jcicZ040.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ040ReposDay.saveAndFlush(jcicZ040);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ040ReposMon.saveAndFlush(jcicZ040);
    else if (dbName.equals(ContentName.onHist))
        jcicZ040ReposHist.saveAndFlush(jcicZ040);
    else 
      jcicZ040Repos.saveAndFlush(jcicZ040);	
    return this.findById(jcicZ040.getJcicZ040Id());
  }

  @Override
  public void delete(JcicZ040 jcicZ040, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ040.getJcicZ040Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ040ReposDay.delete(jcicZ040);	
      jcicZ040ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ040ReposMon.delete(jcicZ040);	
      jcicZ040ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ040ReposHist.delete(jcicZ040);
      jcicZ040ReposHist.flush();
    }
    else {
      jcicZ040Repos.delete(jcicZ040);
      jcicZ040Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ040> jcicZ040, TitaVo... titaVo) throws DBException {
    if (jcicZ040 == null || jcicZ040.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ040 t : jcicZ040){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ040 = jcicZ040ReposDay.saveAll(jcicZ040);	
      jcicZ040ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ040 = jcicZ040ReposMon.saveAll(jcicZ040);	
      jcicZ040ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ040 = jcicZ040ReposHist.saveAll(jcicZ040);
      jcicZ040ReposHist.flush();
    }
    else {
      jcicZ040 = jcicZ040Repos.saveAll(jcicZ040);
      jcicZ040Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ040> jcicZ040, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ040 == null || jcicZ040.size() == 0)
      throw new DBException(6);

    for (JcicZ040 t : jcicZ040) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ040 = jcicZ040ReposDay.saveAll(jcicZ040);	
      jcicZ040ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ040 = jcicZ040ReposMon.saveAll(jcicZ040);	
      jcicZ040ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ040 = jcicZ040ReposHist.saveAll(jcicZ040);
      jcicZ040ReposHist.flush();
    }
    else {
      jcicZ040 = jcicZ040Repos.saveAll(jcicZ040);
      jcicZ040Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ040> jcicZ040, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ040 == null || jcicZ040.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ040ReposDay.deleteAll(jcicZ040);	
      jcicZ040ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ040ReposMon.deleteAll(jcicZ040);	
      jcicZ040ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ040ReposHist.deleteAll(jcicZ040);
      jcicZ040ReposHist.flush();
    }
    else {
      jcicZ040Repos.deleteAll(jcicZ040);
      jcicZ040Repos.flush();
    }
  }

}
