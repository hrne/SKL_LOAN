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
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.domain.JcicZ042Id;
import com.st1.itx.db.repository.online.JcicZ042Repository;
import com.st1.itx.db.repository.day.JcicZ042RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ042RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ042RepositoryHist;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ042Service")
@Repository
public class JcicZ042ServiceImpl implements JcicZ042Service, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ042ServiceImpl.class);

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
    logger.info("findById " + dbName + " " + jcicZ042Id);
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
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustId", "SubmitKey", "RcDate"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042ReposHist.findAll(pageable);
    else 
      slice = jcicZ042Repos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ042> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ042Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ042> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("RcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ042Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ042> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ042Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ042 holdById(JcicZ042Id jcicZ042Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ042Id);
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
    logger.info("Hold " + dbName + " " + jcicZ042.getJcicZ042Id());
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
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ042.getJcicZ042Id());
    if (this.findById(jcicZ042.getJcicZ042Id()) != null)
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
		}
    logger.info("Update..." + dbName + " " + jcicZ042.getJcicZ042Id());
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
		}
    logger.info("Update..." + dbName + " " + jcicZ042.getJcicZ042Id());
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
    logger.info("Delete..." + dbName + " " + jcicZ042.getJcicZ042Id());
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
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
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
		}
    logger.info("UpdateAll...");
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
    logger.info("DeleteAll...");
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
