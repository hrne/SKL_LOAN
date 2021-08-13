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
import com.st1.itx.db.domain.JcicZ061Log;
import com.st1.itx.db.domain.JcicZ061LogId;
import com.st1.itx.db.repository.online.JcicZ061LogRepository;
import com.st1.itx.db.repository.day.JcicZ061LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ061LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ061LogRepositoryHist;
import com.st1.itx.db.service.JcicZ061LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ061LogService")
@Repository
public class JcicZ061LogServiceImpl implements JcicZ061LogService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ061LogServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ061LogRepository jcicZ061LogRepos;

  @Autowired
  private JcicZ061LogRepositoryDay jcicZ061LogReposDay;

  @Autowired
  private JcicZ061LogRepositoryMon jcicZ061LogReposMon;

  @Autowired
  private JcicZ061LogRepositoryHist jcicZ061LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ061LogRepos);
    org.junit.Assert.assertNotNull(jcicZ061LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ061LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ061LogReposHist);
  }

  @Override
  public JcicZ061Log findById(JcicZ061LogId jcicZ061LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ061LogId);
    Optional<JcicZ061Log> jcicZ061Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ061Log = jcicZ061LogReposDay.findById(jcicZ061LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ061Log = jcicZ061LogReposMon.findById(jcicZ061LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ061Log = jcicZ061LogReposHist.findById(jcicZ061LogId);
    else 
      jcicZ061Log = jcicZ061LogRepos.findById(jcicZ061LogId);
    JcicZ061Log obj = jcicZ061Log.isPresent() ? jcicZ061Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ061Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ061Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ061LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ061LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ061LogReposHist.findAll(pageable);
    else 
      slice = jcicZ061LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ061Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ061Log> jcicZ061LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ061LogT = jcicZ061LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ061LogT = jcicZ061LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ061LogT = jcicZ061LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ061LogT = jcicZ061LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ061LogT.isPresent() ? jcicZ061LogT.get() : null;
  }

  @Override
  public Slice<JcicZ061Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ061Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ061LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ061LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ061LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ061LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ061Log holdById(JcicZ061LogId jcicZ061LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ061LogId);
    Optional<JcicZ061Log> jcicZ061Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ061Log = jcicZ061LogReposDay.findByJcicZ061LogId(jcicZ061LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ061Log = jcicZ061LogReposMon.findByJcicZ061LogId(jcicZ061LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ061Log = jcicZ061LogReposHist.findByJcicZ061LogId(jcicZ061LogId);
    else 
      jcicZ061Log = jcicZ061LogRepos.findByJcicZ061LogId(jcicZ061LogId);
    return jcicZ061Log.isPresent() ? jcicZ061Log.get() : null;
  }

  @Override
  public JcicZ061Log holdById(JcicZ061Log jcicZ061Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ061Log.getJcicZ061LogId());
    Optional<JcicZ061Log> jcicZ061LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ061LogT = jcicZ061LogReposDay.findByJcicZ061LogId(jcicZ061Log.getJcicZ061LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ061LogT = jcicZ061LogReposMon.findByJcicZ061LogId(jcicZ061Log.getJcicZ061LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ061LogT = jcicZ061LogReposHist.findByJcicZ061LogId(jcicZ061Log.getJcicZ061LogId());
    else 
      jcicZ061LogT = jcicZ061LogRepos.findByJcicZ061LogId(jcicZ061Log.getJcicZ061LogId());
    return jcicZ061LogT.isPresent() ? jcicZ061LogT.get() : null;
  }

  @Override
  public JcicZ061Log insert(JcicZ061Log jcicZ061Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ061Log.getJcicZ061LogId());
    if (this.findById(jcicZ061Log.getJcicZ061LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ061Log.setCreateEmpNo(empNot);

    if(jcicZ061Log.getLastUpdateEmpNo() == null || jcicZ061Log.getLastUpdateEmpNo().isEmpty())
      jcicZ061Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ061LogReposDay.saveAndFlush(jcicZ061Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ061LogReposMon.saveAndFlush(jcicZ061Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ061LogReposHist.saveAndFlush(jcicZ061Log);
    else 
    return jcicZ061LogRepos.saveAndFlush(jcicZ061Log);
  }

  @Override
  public JcicZ061Log update(JcicZ061Log jcicZ061Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ061Log.getJcicZ061LogId());
    if (!empNot.isEmpty())
      jcicZ061Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ061LogReposDay.saveAndFlush(jcicZ061Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ061LogReposMon.saveAndFlush(jcicZ061Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ061LogReposHist.saveAndFlush(jcicZ061Log);
    else 
    return jcicZ061LogRepos.saveAndFlush(jcicZ061Log);
  }

  @Override
  public JcicZ061Log update2(JcicZ061Log jcicZ061Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ061Log.getJcicZ061LogId());
    if (!empNot.isEmpty())
      jcicZ061Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ061LogReposDay.saveAndFlush(jcicZ061Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ061LogReposMon.saveAndFlush(jcicZ061Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ061LogReposHist.saveAndFlush(jcicZ061Log);
    else 
      jcicZ061LogRepos.saveAndFlush(jcicZ061Log);	
    return this.findById(jcicZ061Log.getJcicZ061LogId());
  }

  @Override
  public void delete(JcicZ061Log jcicZ061Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ061Log.getJcicZ061LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ061LogReposDay.delete(jcicZ061Log);	
      jcicZ061LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ061LogReposMon.delete(jcicZ061Log);	
      jcicZ061LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ061LogReposHist.delete(jcicZ061Log);
      jcicZ061LogReposHist.flush();
    }
    else {
      jcicZ061LogRepos.delete(jcicZ061Log);
      jcicZ061LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ061Log> jcicZ061Log, TitaVo... titaVo) throws DBException {
    if (jcicZ061Log == null || jcicZ061Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ061Log t : jcicZ061Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ061Log = jcicZ061LogReposDay.saveAll(jcicZ061Log);	
      jcicZ061LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ061Log = jcicZ061LogReposMon.saveAll(jcicZ061Log);	
      jcicZ061LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ061Log = jcicZ061LogReposHist.saveAll(jcicZ061Log);
      jcicZ061LogReposHist.flush();
    }
    else {
      jcicZ061Log = jcicZ061LogRepos.saveAll(jcicZ061Log);
      jcicZ061LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ061Log> jcicZ061Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ061Log == null || jcicZ061Log.size() == 0)
      throw new DBException(6);

    for (JcicZ061Log t : jcicZ061Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ061Log = jcicZ061LogReposDay.saveAll(jcicZ061Log);	
      jcicZ061LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ061Log = jcicZ061LogReposMon.saveAll(jcicZ061Log);	
      jcicZ061LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ061Log = jcicZ061LogReposHist.saveAll(jcicZ061Log);
      jcicZ061LogReposHist.flush();
    }
    else {
      jcicZ061Log = jcicZ061LogRepos.saveAll(jcicZ061Log);
      jcicZ061LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ061Log> jcicZ061Log, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ061Log == null || jcicZ061Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ061LogReposDay.deleteAll(jcicZ061Log);	
      jcicZ061LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ061LogReposMon.deleteAll(jcicZ061Log);	
      jcicZ061LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ061LogReposHist.deleteAll(jcicZ061Log);
      jcicZ061LogReposHist.flush();
    }
    else {
      jcicZ061LogRepos.deleteAll(jcicZ061Log);
      jcicZ061LogRepos.flush();
    }
  }

}
