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
import com.st1.itx.db.domain.JcicZ055Log;
import com.st1.itx.db.domain.JcicZ055LogId;
import com.st1.itx.db.repository.online.JcicZ055LogRepository;
import com.st1.itx.db.repository.day.JcicZ055LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ055LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ055LogRepositoryHist;
import com.st1.itx.db.service.JcicZ055LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ055LogService")
@Repository
public class JcicZ055LogServiceImpl implements JcicZ055LogService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ055LogServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ055LogRepository jcicZ055LogRepos;

  @Autowired
  private JcicZ055LogRepositoryDay jcicZ055LogReposDay;

  @Autowired
  private JcicZ055LogRepositoryMon jcicZ055LogReposMon;

  @Autowired
  private JcicZ055LogRepositoryHist jcicZ055LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ055LogRepos);
    org.junit.Assert.assertNotNull(jcicZ055LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ055LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ055LogReposHist);
  }

  @Override
  public JcicZ055Log findById(JcicZ055LogId jcicZ055LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ055LogId);
    Optional<JcicZ055Log> jcicZ055Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ055Log = jcicZ055LogReposDay.findById(jcicZ055LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ055Log = jcicZ055LogReposMon.findById(jcicZ055LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ055Log = jcicZ055LogReposHist.findById(jcicZ055LogId);
    else 
      jcicZ055Log = jcicZ055LogRepos.findById(jcicZ055LogId);
    JcicZ055Log obj = jcicZ055Log.isPresent() ? jcicZ055Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ055Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ055Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ055LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ055LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ055LogReposHist.findAll(pageable);
    else 
      slice = jcicZ055LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ055Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ055Log> jcicZ055LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ055LogT = jcicZ055LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ055LogT = jcicZ055LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ055LogT = jcicZ055LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ055LogT = jcicZ055LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ055LogT.isPresent() ? jcicZ055LogT.get() : null;
  }

  @Override
  public Slice<JcicZ055Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ055Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ055LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ055LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ055LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ055LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ055Log holdById(JcicZ055LogId jcicZ055LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ055LogId);
    Optional<JcicZ055Log> jcicZ055Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ055Log = jcicZ055LogReposDay.findByJcicZ055LogId(jcicZ055LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ055Log = jcicZ055LogReposMon.findByJcicZ055LogId(jcicZ055LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ055Log = jcicZ055LogReposHist.findByJcicZ055LogId(jcicZ055LogId);
    else 
      jcicZ055Log = jcicZ055LogRepos.findByJcicZ055LogId(jcicZ055LogId);
    return jcicZ055Log.isPresent() ? jcicZ055Log.get() : null;
  }

  @Override
  public JcicZ055Log holdById(JcicZ055Log jcicZ055Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ055Log.getJcicZ055LogId());
    Optional<JcicZ055Log> jcicZ055LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ055LogT = jcicZ055LogReposDay.findByJcicZ055LogId(jcicZ055Log.getJcicZ055LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ055LogT = jcicZ055LogReposMon.findByJcicZ055LogId(jcicZ055Log.getJcicZ055LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ055LogT = jcicZ055LogReposHist.findByJcicZ055LogId(jcicZ055Log.getJcicZ055LogId());
    else 
      jcicZ055LogT = jcicZ055LogRepos.findByJcicZ055LogId(jcicZ055Log.getJcicZ055LogId());
    return jcicZ055LogT.isPresent() ? jcicZ055LogT.get() : null;
  }

  @Override
  public JcicZ055Log insert(JcicZ055Log jcicZ055Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ055Log.getJcicZ055LogId());
    if (this.findById(jcicZ055Log.getJcicZ055LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ055Log.setCreateEmpNo(empNot);

    if(jcicZ055Log.getLastUpdateEmpNo() == null || jcicZ055Log.getLastUpdateEmpNo().isEmpty())
      jcicZ055Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ055LogReposDay.saveAndFlush(jcicZ055Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ055LogReposMon.saveAndFlush(jcicZ055Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ055LogReposHist.saveAndFlush(jcicZ055Log);
    else 
    return jcicZ055LogRepos.saveAndFlush(jcicZ055Log);
  }

  @Override
  public JcicZ055Log update(JcicZ055Log jcicZ055Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ055Log.getJcicZ055LogId());
    if (!empNot.isEmpty())
      jcicZ055Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ055LogReposDay.saveAndFlush(jcicZ055Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ055LogReposMon.saveAndFlush(jcicZ055Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ055LogReposHist.saveAndFlush(jcicZ055Log);
    else 
    return jcicZ055LogRepos.saveAndFlush(jcicZ055Log);
  }

  @Override
  public JcicZ055Log update2(JcicZ055Log jcicZ055Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ055Log.getJcicZ055LogId());
    if (!empNot.isEmpty())
      jcicZ055Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ055LogReposDay.saveAndFlush(jcicZ055Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ055LogReposMon.saveAndFlush(jcicZ055Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ055LogReposHist.saveAndFlush(jcicZ055Log);
    else 
      jcicZ055LogRepos.saveAndFlush(jcicZ055Log);	
    return this.findById(jcicZ055Log.getJcicZ055LogId());
  }

  @Override
  public void delete(JcicZ055Log jcicZ055Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ055Log.getJcicZ055LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ055LogReposDay.delete(jcicZ055Log);	
      jcicZ055LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ055LogReposMon.delete(jcicZ055Log);	
      jcicZ055LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ055LogReposHist.delete(jcicZ055Log);
      jcicZ055LogReposHist.flush();
    }
    else {
      jcicZ055LogRepos.delete(jcicZ055Log);
      jcicZ055LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ055Log> jcicZ055Log, TitaVo... titaVo) throws DBException {
    if (jcicZ055Log == null || jcicZ055Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ055Log t : jcicZ055Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ055Log = jcicZ055LogReposDay.saveAll(jcicZ055Log);	
      jcicZ055LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ055Log = jcicZ055LogReposMon.saveAll(jcicZ055Log);	
      jcicZ055LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ055Log = jcicZ055LogReposHist.saveAll(jcicZ055Log);
      jcicZ055LogReposHist.flush();
    }
    else {
      jcicZ055Log = jcicZ055LogRepos.saveAll(jcicZ055Log);
      jcicZ055LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ055Log> jcicZ055Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ055Log == null || jcicZ055Log.size() == 0)
      throw new DBException(6);

    for (JcicZ055Log t : jcicZ055Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ055Log = jcicZ055LogReposDay.saveAll(jcicZ055Log);	
      jcicZ055LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ055Log = jcicZ055LogReposMon.saveAll(jcicZ055Log);	
      jcicZ055LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ055Log = jcicZ055LogReposHist.saveAll(jcicZ055Log);
      jcicZ055LogReposHist.flush();
    }
    else {
      jcicZ055Log = jcicZ055LogRepos.saveAll(jcicZ055Log);
      jcicZ055LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ055Log> jcicZ055Log, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ055Log == null || jcicZ055Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ055LogReposDay.deleteAll(jcicZ055Log);	
      jcicZ055LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ055LogReposMon.deleteAll(jcicZ055Log);	
      jcicZ055LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ055LogReposHist.deleteAll(jcicZ055Log);
      jcicZ055LogReposHist.flush();
    }
    else {
      jcicZ055LogRepos.deleteAll(jcicZ055Log);
      jcicZ055LogRepos.flush();
    }
  }

}
