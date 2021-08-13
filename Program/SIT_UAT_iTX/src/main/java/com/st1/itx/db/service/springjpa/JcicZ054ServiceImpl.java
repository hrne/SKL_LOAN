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
import com.st1.itx.db.domain.JcicZ054;
import com.st1.itx.db.domain.JcicZ054Id;
import com.st1.itx.db.repository.online.JcicZ054Repository;
import com.st1.itx.db.repository.day.JcicZ054RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ054RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ054RepositoryHist;
import com.st1.itx.db.service.JcicZ054Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ054Service")
@Repository
public class JcicZ054ServiceImpl implements JcicZ054Service, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ054ServiceImpl.class);

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
    logger.info("findById " + dbName + " " + jcicZ054Id);
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
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate", "MaxMainCode"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054ReposHist.findAll(pageable);
    else 
      slice = jcicZ054Repos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ054> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ054Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ054> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("RcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ054Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ054> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ054Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ054 holdById(JcicZ054Id jcicZ054Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ054Id);
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
    logger.info("Hold " + dbName + " " + jcicZ054.getJcicZ054Id());
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
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ054.getJcicZ054Id());
    if (this.findById(jcicZ054.getJcicZ054Id()) != null)
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
		}
    logger.info("Update..." + dbName + " " + jcicZ054.getJcicZ054Id());
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
		}
    logger.info("Update..." + dbName + " " + jcicZ054.getJcicZ054Id());
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
    logger.info("Delete..." + dbName + " " + jcicZ054.getJcicZ054Id());
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
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
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
		}
    logger.info("UpdateAll...");
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
    logger.info("DeleteAll...");
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
