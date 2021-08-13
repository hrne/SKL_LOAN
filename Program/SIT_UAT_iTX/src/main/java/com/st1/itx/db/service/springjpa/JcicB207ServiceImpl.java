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
import com.st1.itx.db.domain.JcicB207;
import com.st1.itx.db.domain.JcicB207Id;
import com.st1.itx.db.repository.online.JcicB207Repository;
import com.st1.itx.db.repository.day.JcicB207RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB207RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB207RepositoryHist;
import com.st1.itx.db.service.JcicB207Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB207Service")
@Repository
public class JcicB207ServiceImpl implements JcicB207Service, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicB207ServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicB207Repository jcicB207Repos;

  @Autowired
  private JcicB207RepositoryDay jcicB207ReposDay;

  @Autowired
  private JcicB207RepositoryMon jcicB207ReposMon;

  @Autowired
  private JcicB207RepositoryHist jcicB207ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicB207Repos);
    org.junit.Assert.assertNotNull(jcicB207ReposDay);
    org.junit.Assert.assertNotNull(jcicB207ReposMon);
    org.junit.Assert.assertNotNull(jcicB207ReposHist);
  }

  @Override
  public JcicB207 findById(JcicB207Id jcicB207Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicB207Id);
    Optional<JcicB207> jcicB207 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB207 = jcicB207ReposDay.findById(jcicB207Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB207 = jcicB207ReposMon.findById(jcicB207Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB207 = jcicB207ReposHist.findById(jcicB207Id);
    else 
      jcicB207 = jcicB207Repos.findById(jcicB207Id);
    JcicB207 obj = jcicB207.isPresent() ? jcicB207.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicB207> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicB207> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "BankItem", "CustId"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicB207ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicB207ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicB207ReposHist.findAll(pageable);
    else 
      slice = jcicB207Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicB207 holdById(JcicB207Id jcicB207Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicB207Id);
    Optional<JcicB207> jcicB207 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB207 = jcicB207ReposDay.findByJcicB207Id(jcicB207Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB207 = jcicB207ReposMon.findByJcicB207Id(jcicB207Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB207 = jcicB207ReposHist.findByJcicB207Id(jcicB207Id);
    else 
      jcicB207 = jcicB207Repos.findByJcicB207Id(jcicB207Id);
    return jcicB207.isPresent() ? jcicB207.get() : null;
  }

  @Override
  public JcicB207 holdById(JcicB207 jcicB207, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicB207.getJcicB207Id());
    Optional<JcicB207> jcicB207T = null;
    if (dbName.equals(ContentName.onDay))
      jcicB207T = jcicB207ReposDay.findByJcicB207Id(jcicB207.getJcicB207Id());
    else if (dbName.equals(ContentName.onMon))
      jcicB207T = jcicB207ReposMon.findByJcicB207Id(jcicB207.getJcicB207Id());
    else if (dbName.equals(ContentName.onHist))
      jcicB207T = jcicB207ReposHist.findByJcicB207Id(jcicB207.getJcicB207Id());
    else 
      jcicB207T = jcicB207Repos.findByJcicB207Id(jcicB207.getJcicB207Id());
    return jcicB207T.isPresent() ? jcicB207T.get() : null;
  }

  @Override
  public JcicB207 insert(JcicB207 jcicB207, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicB207.getJcicB207Id());
    if (this.findById(jcicB207.getJcicB207Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicB207.setCreateEmpNo(empNot);

    if(jcicB207.getLastUpdateEmpNo() == null || jcicB207.getLastUpdateEmpNo().isEmpty())
      jcicB207.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB207ReposDay.saveAndFlush(jcicB207);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB207ReposMon.saveAndFlush(jcicB207);
    else if (dbName.equals(ContentName.onHist))
      return jcicB207ReposHist.saveAndFlush(jcicB207);
    else 
    return jcicB207Repos.saveAndFlush(jcicB207);
  }

  @Override
  public JcicB207 update(JcicB207 jcicB207, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicB207.getJcicB207Id());
    if (!empNot.isEmpty())
      jcicB207.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB207ReposDay.saveAndFlush(jcicB207);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB207ReposMon.saveAndFlush(jcicB207);
    else if (dbName.equals(ContentName.onHist))
      return jcicB207ReposHist.saveAndFlush(jcicB207);
    else 
    return jcicB207Repos.saveAndFlush(jcicB207);
  }

  @Override
  public JcicB207 update2(JcicB207 jcicB207, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicB207.getJcicB207Id());
    if (!empNot.isEmpty())
      jcicB207.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicB207ReposDay.saveAndFlush(jcicB207);	
    else if (dbName.equals(ContentName.onMon))
      jcicB207ReposMon.saveAndFlush(jcicB207);
    else if (dbName.equals(ContentName.onHist))
        jcicB207ReposHist.saveAndFlush(jcicB207);
    else 
      jcicB207Repos.saveAndFlush(jcicB207);	
    return this.findById(jcicB207.getJcicB207Id());
  }

  @Override
  public void delete(JcicB207 jcicB207, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicB207.getJcicB207Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicB207ReposDay.delete(jcicB207);	
      jcicB207ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB207ReposMon.delete(jcicB207);	
      jcicB207ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB207ReposHist.delete(jcicB207);
      jcicB207ReposHist.flush();
    }
    else {
      jcicB207Repos.delete(jcicB207);
      jcicB207Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicB207> jcicB207, TitaVo... titaVo) throws DBException {
    if (jcicB207 == null || jcicB207.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicB207 t : jcicB207){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicB207 = jcicB207ReposDay.saveAll(jcicB207);	
      jcicB207ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB207 = jcicB207ReposMon.saveAll(jcicB207);	
      jcicB207ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB207 = jcicB207ReposHist.saveAll(jcicB207);
      jcicB207ReposHist.flush();
    }
    else {
      jcicB207 = jcicB207Repos.saveAll(jcicB207);
      jcicB207Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicB207> jcicB207, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicB207 == null || jcicB207.size() == 0)
      throw new DBException(6);

    for (JcicB207 t : jcicB207) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicB207 = jcicB207ReposDay.saveAll(jcicB207);	
      jcicB207ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB207 = jcicB207ReposMon.saveAll(jcicB207);	
      jcicB207ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB207 = jcicB207ReposHist.saveAll(jcicB207);
      jcicB207ReposHist.flush();
    }
    else {
      jcicB207 = jcicB207Repos.saveAll(jcicB207);
      jcicB207Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicB207> jcicB207, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicB207 == null || jcicB207.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicB207ReposDay.deleteAll(jcicB207);	
      jcicB207ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB207ReposMon.deleteAll(jcicB207);	
      jcicB207ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB207ReposHist.deleteAll(jcicB207);
      jcicB207ReposHist.flush();
    }
    else {
      jcicB207Repos.deleteAll(jcicB207);
      jcicB207Repos.flush();
    }
  }

  @Override
  public void Usp_L8_JcicB207_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jcicB207ReposDay.uspL8Jcicb207Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jcicB207ReposMon.uspL8Jcicb207Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jcicB207ReposHist.uspL8Jcicb207Upd(TBSDYF, EmpNo);
   else
      jcicB207Repos.uspL8Jcicb207Upd(TBSDYF, EmpNo);
  }

}
