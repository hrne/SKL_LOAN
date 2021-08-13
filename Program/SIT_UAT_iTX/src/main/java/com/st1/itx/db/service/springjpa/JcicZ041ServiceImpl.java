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
import com.st1.itx.db.domain.JcicZ041;
import com.st1.itx.db.domain.JcicZ041Id;
import com.st1.itx.db.repository.online.JcicZ041Repository;
import com.st1.itx.db.repository.day.JcicZ041RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ041RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ041RepositoryHist;
import com.st1.itx.db.service.JcicZ041Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ041Service")
@Repository
public class JcicZ041ServiceImpl implements JcicZ041Service, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ041ServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ041Repository jcicZ041Repos;

  @Autowired
  private JcicZ041RepositoryDay jcicZ041ReposDay;

  @Autowired
  private JcicZ041RepositoryMon jcicZ041ReposMon;

  @Autowired
  private JcicZ041RepositoryHist jcicZ041ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ041Repos);
    org.junit.Assert.assertNotNull(jcicZ041ReposDay);
    org.junit.Assert.assertNotNull(jcicZ041ReposMon);
    org.junit.Assert.assertNotNull(jcicZ041ReposHist);
  }

  @Override
  public JcicZ041 findById(JcicZ041Id jcicZ041Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ041Id);
    Optional<JcicZ041> jcicZ041 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ041 = jcicZ041ReposDay.findById(jcicZ041Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ041 = jcicZ041ReposMon.findById(jcicZ041Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ041 = jcicZ041ReposHist.findById(jcicZ041Id);
    else 
      jcicZ041 = jcicZ041Repos.findById(jcicZ041Id);
    JcicZ041 obj = jcicZ041.isPresent() ? jcicZ041.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ041> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ041> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustId", "SubmitKey", "RcDate"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ041ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ041ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ041ReposHist.findAll(pageable);
    else 
      slice = jcicZ041Repos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ041> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ041> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ041ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ041ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ041ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
    else 
      slice = jcicZ041Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ041> RcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ041> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("RcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ041ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ041ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ041ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
    else 
      slice = jcicZ041Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<JcicZ041> CustRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ041> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " +  rcDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ041ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ041ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ041ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
    else 
      slice = jcicZ041Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ041 holdById(JcicZ041Id jcicZ041Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ041Id);
    Optional<JcicZ041> jcicZ041 = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ041 = jcicZ041ReposDay.findByJcicZ041Id(jcicZ041Id);
    else if (dbName.equals(ContentName.onMon))
      jcicZ041 = jcicZ041ReposMon.findByJcicZ041Id(jcicZ041Id);
    else if (dbName.equals(ContentName.onHist))
      jcicZ041 = jcicZ041ReposHist.findByJcicZ041Id(jcicZ041Id);
    else 
      jcicZ041 = jcicZ041Repos.findByJcicZ041Id(jcicZ041Id);
    return jcicZ041.isPresent() ? jcicZ041.get() : null;
  }

  @Override
  public JcicZ041 holdById(JcicZ041 jcicZ041, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ041.getJcicZ041Id());
    Optional<JcicZ041> jcicZ041T = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ041T = jcicZ041ReposDay.findByJcicZ041Id(jcicZ041.getJcicZ041Id());
    else if (dbName.equals(ContentName.onMon))
      jcicZ041T = jcicZ041ReposMon.findByJcicZ041Id(jcicZ041.getJcicZ041Id());
    else if (dbName.equals(ContentName.onHist))
      jcicZ041T = jcicZ041ReposHist.findByJcicZ041Id(jcicZ041.getJcicZ041Id());
    else 
      jcicZ041T = jcicZ041Repos.findByJcicZ041Id(jcicZ041.getJcicZ041Id());
    return jcicZ041T.isPresent() ? jcicZ041T.get() : null;
  }

  @Override
  public JcicZ041 insert(JcicZ041 jcicZ041, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ041.getJcicZ041Id());
    if (this.findById(jcicZ041.getJcicZ041Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ041.setCreateEmpNo(empNot);

    if(jcicZ041.getLastUpdateEmpNo() == null || jcicZ041.getLastUpdateEmpNo().isEmpty())
      jcicZ041.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ041ReposDay.saveAndFlush(jcicZ041);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ041ReposMon.saveAndFlush(jcicZ041);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ041ReposHist.saveAndFlush(jcicZ041);
    else 
    return jcicZ041Repos.saveAndFlush(jcicZ041);
  }

  @Override
  public JcicZ041 update(JcicZ041 jcicZ041, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ041.getJcicZ041Id());
    if (!empNot.isEmpty())
      jcicZ041.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ041ReposDay.saveAndFlush(jcicZ041);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ041ReposMon.saveAndFlush(jcicZ041);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ041ReposHist.saveAndFlush(jcicZ041);
    else 
    return jcicZ041Repos.saveAndFlush(jcicZ041);
  }

  @Override
  public JcicZ041 update2(JcicZ041 jcicZ041, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ041.getJcicZ041Id());
    if (!empNot.isEmpty())
      jcicZ041.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ041ReposDay.saveAndFlush(jcicZ041);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ041ReposMon.saveAndFlush(jcicZ041);
    else if (dbName.equals(ContentName.onHist))
        jcicZ041ReposHist.saveAndFlush(jcicZ041);
    else 
      jcicZ041Repos.saveAndFlush(jcicZ041);	
    return this.findById(jcicZ041.getJcicZ041Id());
  }

  @Override
  public void delete(JcicZ041 jcicZ041, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ041.getJcicZ041Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ041ReposDay.delete(jcicZ041);	
      jcicZ041ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ041ReposMon.delete(jcicZ041);	
      jcicZ041ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ041ReposHist.delete(jcicZ041);
      jcicZ041ReposHist.flush();
    }
    else {
      jcicZ041Repos.delete(jcicZ041);
      jcicZ041Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ041> jcicZ041, TitaVo... titaVo) throws DBException {
    if (jcicZ041 == null || jcicZ041.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ041 t : jcicZ041){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ041 = jcicZ041ReposDay.saveAll(jcicZ041);	
      jcicZ041ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ041 = jcicZ041ReposMon.saveAll(jcicZ041);	
      jcicZ041ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ041 = jcicZ041ReposHist.saveAll(jcicZ041);
      jcicZ041ReposHist.flush();
    }
    else {
      jcicZ041 = jcicZ041Repos.saveAll(jcicZ041);
      jcicZ041Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ041> jcicZ041, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ041 == null || jcicZ041.size() == 0)
      throw new DBException(6);

    for (JcicZ041 t : jcicZ041) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ041 = jcicZ041ReposDay.saveAll(jcicZ041);	
      jcicZ041ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ041 = jcicZ041ReposMon.saveAll(jcicZ041);	
      jcicZ041ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ041 = jcicZ041ReposHist.saveAll(jcicZ041);
      jcicZ041ReposHist.flush();
    }
    else {
      jcicZ041 = jcicZ041Repos.saveAll(jcicZ041);
      jcicZ041Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ041> jcicZ041, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ041 == null || jcicZ041.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ041ReposDay.deleteAll(jcicZ041);	
      jcicZ041ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ041ReposMon.deleteAll(jcicZ041);	
      jcicZ041ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ041ReposHist.deleteAll(jcicZ041);
      jcicZ041ReposHist.flush();
    }
    else {
      jcicZ041Repos.deleteAll(jcicZ041);
      jcicZ041Repos.flush();
    }
  }

}
