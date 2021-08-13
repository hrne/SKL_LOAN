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
import com.st1.itx.db.domain.JcicZ443Log;
import com.st1.itx.db.domain.JcicZ443LogId;
import com.st1.itx.db.repository.online.JcicZ443LogRepository;
import com.st1.itx.db.repository.day.JcicZ443LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ443LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ443LogRepositoryHist;
import com.st1.itx.db.service.JcicZ443LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ443LogService")
@Repository
public class JcicZ443LogServiceImpl implements JcicZ443LogService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ443LogServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ443LogRepository jcicZ443LogRepos;

  @Autowired
  private JcicZ443LogRepositoryDay jcicZ443LogReposDay;

  @Autowired
  private JcicZ443LogRepositoryMon jcicZ443LogReposMon;

  @Autowired
  private JcicZ443LogRepositoryHist jcicZ443LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ443LogRepos);
    org.junit.Assert.assertNotNull(jcicZ443LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ443LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ443LogReposHist);
  }

  @Override
  public JcicZ443Log findById(JcicZ443LogId jcicZ443LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ443LogId);
    Optional<JcicZ443Log> jcicZ443Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443Log = jcicZ443LogReposDay.findById(jcicZ443LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ443Log = jcicZ443LogReposMon.findById(jcicZ443LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ443Log = jcicZ443LogReposHist.findById(jcicZ443LogId);
    else 
      jcicZ443Log = jcicZ443LogRepos.findById(jcicZ443LogId);
    JcicZ443Log obj = jcicZ443Log.isPresent() ? jcicZ443Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ443Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ443Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ443LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ443LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ443LogReposHist.findAll(pageable);
    else 
      slice = jcicZ443LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ443Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ443Log> jcicZ443LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443LogT = jcicZ443LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ443LogT = jcicZ443LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ443LogT = jcicZ443LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ443LogT = jcicZ443LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ443LogT.isPresent() ? jcicZ443LogT.get() : null;
  }

  @Override
  public Slice<JcicZ443Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ443Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ443LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ443LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ443LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ443LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ443Log holdById(JcicZ443LogId jcicZ443LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ443LogId);
    Optional<JcicZ443Log> jcicZ443Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443Log = jcicZ443LogReposDay.findByJcicZ443LogId(jcicZ443LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ443Log = jcicZ443LogReposMon.findByJcicZ443LogId(jcicZ443LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ443Log = jcicZ443LogReposHist.findByJcicZ443LogId(jcicZ443LogId);
    else 
      jcicZ443Log = jcicZ443LogRepos.findByJcicZ443LogId(jcicZ443LogId);
    return jcicZ443Log.isPresent() ? jcicZ443Log.get() : null;
  }

  @Override
  public JcicZ443Log holdById(JcicZ443Log jcicZ443Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ443Log.getJcicZ443LogId());
    Optional<JcicZ443Log> jcicZ443LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ443LogT = jcicZ443LogReposDay.findByJcicZ443LogId(jcicZ443Log.getJcicZ443LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ443LogT = jcicZ443LogReposMon.findByJcicZ443LogId(jcicZ443Log.getJcicZ443LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ443LogT = jcicZ443LogReposHist.findByJcicZ443LogId(jcicZ443Log.getJcicZ443LogId());
    else 
      jcicZ443LogT = jcicZ443LogRepos.findByJcicZ443LogId(jcicZ443Log.getJcicZ443LogId());
    return jcicZ443LogT.isPresent() ? jcicZ443LogT.get() : null;
  }

  @Override
  public JcicZ443Log insert(JcicZ443Log jcicZ443Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ443Log.getJcicZ443LogId());
    if (this.findById(jcicZ443Log.getJcicZ443LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ443Log.setCreateEmpNo(empNot);

    if(jcicZ443Log.getLastUpdateEmpNo() == null || jcicZ443Log.getLastUpdateEmpNo().isEmpty())
      jcicZ443Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ443LogReposDay.saveAndFlush(jcicZ443Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ443LogReposMon.saveAndFlush(jcicZ443Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ443LogReposHist.saveAndFlush(jcicZ443Log);
    else 
    return jcicZ443LogRepos.saveAndFlush(jcicZ443Log);
  }

  @Override
  public JcicZ443Log update(JcicZ443Log jcicZ443Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ443Log.getJcicZ443LogId());
    if (!empNot.isEmpty())
      jcicZ443Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ443LogReposDay.saveAndFlush(jcicZ443Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ443LogReposMon.saveAndFlush(jcicZ443Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ443LogReposHist.saveAndFlush(jcicZ443Log);
    else 
    return jcicZ443LogRepos.saveAndFlush(jcicZ443Log);
  }

  @Override
  public JcicZ443Log update2(JcicZ443Log jcicZ443Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ443Log.getJcicZ443LogId());
    if (!empNot.isEmpty())
      jcicZ443Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ443LogReposDay.saveAndFlush(jcicZ443Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ443LogReposMon.saveAndFlush(jcicZ443Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ443LogReposHist.saveAndFlush(jcicZ443Log);
    else 
      jcicZ443LogRepos.saveAndFlush(jcicZ443Log);	
    return this.findById(jcicZ443Log.getJcicZ443LogId());
  }

  @Override
  public void delete(JcicZ443Log jcicZ443Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ443Log.getJcicZ443LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ443LogReposDay.delete(jcicZ443Log);	
      jcicZ443LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443LogReposMon.delete(jcicZ443Log);	
      jcicZ443LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443LogReposHist.delete(jcicZ443Log);
      jcicZ443LogReposHist.flush();
    }
    else {
      jcicZ443LogRepos.delete(jcicZ443Log);
      jcicZ443LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ443Log> jcicZ443Log, TitaVo... titaVo) throws DBException {
    if (jcicZ443Log == null || jcicZ443Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ443Log t : jcicZ443Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ443Log = jcicZ443LogReposDay.saveAll(jcicZ443Log);	
      jcicZ443LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443Log = jcicZ443LogReposMon.saveAll(jcicZ443Log);	
      jcicZ443LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443Log = jcicZ443LogReposHist.saveAll(jcicZ443Log);
      jcicZ443LogReposHist.flush();
    }
    else {
      jcicZ443Log = jcicZ443LogRepos.saveAll(jcicZ443Log);
      jcicZ443LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ443Log> jcicZ443Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ443Log == null || jcicZ443Log.size() == 0)
      throw new DBException(6);

    for (JcicZ443Log t : jcicZ443Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ443Log = jcicZ443LogReposDay.saveAll(jcicZ443Log);	
      jcicZ443LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443Log = jcicZ443LogReposMon.saveAll(jcicZ443Log);	
      jcicZ443LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443Log = jcicZ443LogReposHist.saveAll(jcicZ443Log);
      jcicZ443LogReposHist.flush();
    }
    else {
      jcicZ443Log = jcicZ443LogRepos.saveAll(jcicZ443Log);
      jcicZ443LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ443Log> jcicZ443Log, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ443Log == null || jcicZ443Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ443LogReposDay.deleteAll(jcicZ443Log);	
      jcicZ443LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ443LogReposMon.deleteAll(jcicZ443Log);	
      jcicZ443LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ443LogReposHist.deleteAll(jcicZ443Log);
      jcicZ443LogReposHist.flush();
    }
    else {
      jcicZ443LogRepos.deleteAll(jcicZ443Log);
      jcicZ443LogRepos.flush();
    }
  }

}
