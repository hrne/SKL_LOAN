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
import com.st1.itx.db.domain.JcicZ056Log;
import com.st1.itx.db.domain.JcicZ056LogId;
import com.st1.itx.db.repository.online.JcicZ056LogRepository;
import com.st1.itx.db.repository.day.JcicZ056LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ056LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ056LogRepositoryHist;
import com.st1.itx.db.service.JcicZ056LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ056LogService")
@Repository
public class JcicZ056LogServiceImpl implements JcicZ056LogService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(JcicZ056LogServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ056LogRepository jcicZ056LogRepos;

  @Autowired
  private JcicZ056LogRepositoryDay jcicZ056LogReposDay;

  @Autowired
  private JcicZ056LogRepositoryMon jcicZ056LogReposMon;

  @Autowired
  private JcicZ056LogRepositoryHist jcicZ056LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ056LogRepos);
    org.junit.Assert.assertNotNull(jcicZ056LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ056LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ056LogReposHist);
  }

  @Override
  public JcicZ056Log findById(JcicZ056LogId jcicZ056LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + jcicZ056LogId);
    Optional<JcicZ056Log> jcicZ056Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ056Log = jcicZ056LogReposDay.findById(jcicZ056LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ056Log = jcicZ056LogReposMon.findById(jcicZ056LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ056Log = jcicZ056LogReposHist.findById(jcicZ056LogId);
    else 
      jcicZ056Log = jcicZ056LogRepos.findById(jcicZ056LogId);
    JcicZ056Log obj = jcicZ056Log.isPresent() ? jcicZ056Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ056Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ056Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ056LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ056LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ056LogReposHist.findAll(pageable);
    else 
      slice = jcicZ056LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ056Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ056Log> jcicZ056LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ056LogT = jcicZ056LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ056LogT = jcicZ056LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ056LogT = jcicZ056LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ056LogT = jcicZ056LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ056LogT.isPresent() ? jcicZ056LogT.get() : null;
  }

  @Override
  public Slice<JcicZ056Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ056Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ056LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ056LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ056LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ056LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ056Log holdById(JcicZ056LogId jcicZ056LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ056LogId);
    Optional<JcicZ056Log> jcicZ056Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ056Log = jcicZ056LogReposDay.findByJcicZ056LogId(jcicZ056LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ056Log = jcicZ056LogReposMon.findByJcicZ056LogId(jcicZ056LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ056Log = jcicZ056LogReposHist.findByJcicZ056LogId(jcicZ056LogId);
    else 
      jcicZ056Log = jcicZ056LogRepos.findByJcicZ056LogId(jcicZ056LogId);
    return jcicZ056Log.isPresent() ? jcicZ056Log.get() : null;
  }

  @Override
  public JcicZ056Log holdById(JcicZ056Log jcicZ056Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + jcicZ056Log.getJcicZ056LogId());
    Optional<JcicZ056Log> jcicZ056LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ056LogT = jcicZ056LogReposDay.findByJcicZ056LogId(jcicZ056Log.getJcicZ056LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ056LogT = jcicZ056LogReposMon.findByJcicZ056LogId(jcicZ056Log.getJcicZ056LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ056LogT = jcicZ056LogReposHist.findByJcicZ056LogId(jcicZ056Log.getJcicZ056LogId());
    else 
      jcicZ056LogT = jcicZ056LogRepos.findByJcicZ056LogId(jcicZ056Log.getJcicZ056LogId());
    return jcicZ056LogT.isPresent() ? jcicZ056LogT.get() : null;
  }

  @Override
  public JcicZ056Log insert(JcicZ056Log jcicZ056Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + jcicZ056Log.getJcicZ056LogId());
    if (this.findById(jcicZ056Log.getJcicZ056LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ056Log.setCreateEmpNo(empNot);

    if(jcicZ056Log.getLastUpdateEmpNo() == null || jcicZ056Log.getLastUpdateEmpNo().isEmpty())
      jcicZ056Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ056LogReposDay.saveAndFlush(jcicZ056Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ056LogReposMon.saveAndFlush(jcicZ056Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ056LogReposHist.saveAndFlush(jcicZ056Log);
    else 
    return jcicZ056LogRepos.saveAndFlush(jcicZ056Log);
  }

  @Override
  public JcicZ056Log update(JcicZ056Log jcicZ056Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ056Log.getJcicZ056LogId());
    if (!empNot.isEmpty())
      jcicZ056Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ056LogReposDay.saveAndFlush(jcicZ056Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ056LogReposMon.saveAndFlush(jcicZ056Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ056LogReposHist.saveAndFlush(jcicZ056Log);
    else 
    return jcicZ056LogRepos.saveAndFlush(jcicZ056Log);
  }

  @Override
  public JcicZ056Log update2(JcicZ056Log jcicZ056Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + jcicZ056Log.getJcicZ056LogId());
    if (!empNot.isEmpty())
      jcicZ056Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ056LogReposDay.saveAndFlush(jcicZ056Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ056LogReposMon.saveAndFlush(jcicZ056Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ056LogReposHist.saveAndFlush(jcicZ056Log);
    else 
      jcicZ056LogRepos.saveAndFlush(jcicZ056Log);	
    return this.findById(jcicZ056Log.getJcicZ056LogId());
  }

  @Override
  public void delete(JcicZ056Log jcicZ056Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + jcicZ056Log.getJcicZ056LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ056LogReposDay.delete(jcicZ056Log);	
      jcicZ056LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ056LogReposMon.delete(jcicZ056Log);	
      jcicZ056LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ056LogReposHist.delete(jcicZ056Log);
      jcicZ056LogReposHist.flush();
    }
    else {
      jcicZ056LogRepos.delete(jcicZ056Log);
      jcicZ056LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ056Log> jcicZ056Log, TitaVo... titaVo) throws DBException {
    if (jcicZ056Log == null || jcicZ056Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (JcicZ056Log t : jcicZ056Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ056Log = jcicZ056LogReposDay.saveAll(jcicZ056Log);	
      jcicZ056LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ056Log = jcicZ056LogReposMon.saveAll(jcicZ056Log);	
      jcicZ056LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ056Log = jcicZ056LogReposHist.saveAll(jcicZ056Log);
      jcicZ056LogReposHist.flush();
    }
    else {
      jcicZ056Log = jcicZ056LogRepos.saveAll(jcicZ056Log);
      jcicZ056LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ056Log> jcicZ056Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (jcicZ056Log == null || jcicZ056Log.size() == 0)
      throw new DBException(6);

    for (JcicZ056Log t : jcicZ056Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ056Log = jcicZ056LogReposDay.saveAll(jcicZ056Log);	
      jcicZ056LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ056Log = jcicZ056LogReposMon.saveAll(jcicZ056Log);	
      jcicZ056LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ056Log = jcicZ056LogReposHist.saveAll(jcicZ056Log);
      jcicZ056LogReposHist.flush();
    }
    else {
      jcicZ056Log = jcicZ056LogRepos.saveAll(jcicZ056Log);
      jcicZ056LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ056Log> jcicZ056Log, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ056Log == null || jcicZ056Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ056LogReposDay.deleteAll(jcicZ056Log);	
      jcicZ056LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ056LogReposMon.deleteAll(jcicZ056Log);	
      jcicZ056LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ056LogReposHist.deleteAll(jcicZ056Log);
      jcicZ056LogReposHist.flush();
    }
    else {
      jcicZ056LogRepos.deleteAll(jcicZ056Log);
      jcicZ056LogRepos.flush();
    }
  }

}
