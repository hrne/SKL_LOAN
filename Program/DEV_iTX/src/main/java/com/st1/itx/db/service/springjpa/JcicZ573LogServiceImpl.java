package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

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
import com.st1.itx.db.domain.JcicZ573Log;
import com.st1.itx.db.domain.JcicZ573LogId;
import com.st1.itx.db.repository.online.JcicZ573LogRepository;
import com.st1.itx.db.repository.day.JcicZ573LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ573LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ573LogRepositoryHist;
import com.st1.itx.db.service.JcicZ573LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ573LogService")
@Repository
public class JcicZ573LogServiceImpl extends ASpringJpaParm implements JcicZ573LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ573LogRepository jcicZ573LogRepos;

  @Autowired
  private JcicZ573LogRepositoryDay jcicZ573LogReposDay;

  @Autowired
  private JcicZ573LogRepositoryMon jcicZ573LogReposMon;

  @Autowired
  private JcicZ573LogRepositoryHist jcicZ573LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ573LogRepos);
    org.junit.Assert.assertNotNull(jcicZ573LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ573LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ573LogReposHist);
  }

  @Override
  public JcicZ573Log findById(JcicZ573LogId jcicZ573LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ573LogId);
    Optional<JcicZ573Log> jcicZ573Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ573Log = jcicZ573LogReposDay.findById(jcicZ573LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ573Log = jcicZ573LogReposMon.findById(jcicZ573LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ573Log = jcicZ573LogReposHist.findById(jcicZ573LogId);
    else 
      jcicZ573Log = jcicZ573LogRepos.findById(jcicZ573LogId);
    JcicZ573Log obj = jcicZ573Log.isPresent() ? jcicZ573Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ573Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ573Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ573LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ573LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ573LogReposHist.findAll(pageable);
    else 
      slice = jcicZ573LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ573Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ573Log> jcicZ573LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ573LogT = jcicZ573LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ573LogT = jcicZ573LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ573LogT = jcicZ573LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ573LogT = jcicZ573LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ573LogT.isPresent() ? jcicZ573LogT.get() : null;
  }

  @Override
  public Slice<JcicZ573Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ573Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ573LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ573LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ573LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ573LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ573Log holdById(JcicZ573LogId jcicZ573LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ573LogId);
    Optional<JcicZ573Log> jcicZ573Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ573Log = jcicZ573LogReposDay.findByJcicZ573LogId(jcicZ573LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ573Log = jcicZ573LogReposMon.findByJcicZ573LogId(jcicZ573LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ573Log = jcicZ573LogReposHist.findByJcicZ573LogId(jcicZ573LogId);
    else 
      jcicZ573Log = jcicZ573LogRepos.findByJcicZ573LogId(jcicZ573LogId);
    return jcicZ573Log.isPresent() ? jcicZ573Log.get() : null;
  }

  @Override
  public JcicZ573Log holdById(JcicZ573Log jcicZ573Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ573Log.getJcicZ573LogId());
    Optional<JcicZ573Log> jcicZ573LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ573LogT = jcicZ573LogReposDay.findByJcicZ573LogId(jcicZ573Log.getJcicZ573LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ573LogT = jcicZ573LogReposMon.findByJcicZ573LogId(jcicZ573Log.getJcicZ573LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ573LogT = jcicZ573LogReposHist.findByJcicZ573LogId(jcicZ573Log.getJcicZ573LogId());
    else 
      jcicZ573LogT = jcicZ573LogRepos.findByJcicZ573LogId(jcicZ573Log.getJcicZ573LogId());
    return jcicZ573LogT.isPresent() ? jcicZ573LogT.get() : null;
  }

  @Override
  public JcicZ573Log insert(JcicZ573Log jcicZ573Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ573Log.getJcicZ573LogId());
    if (this.findById(jcicZ573Log.getJcicZ573LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ573Log.setCreateEmpNo(empNot);

    if(jcicZ573Log.getLastUpdateEmpNo() == null || jcicZ573Log.getLastUpdateEmpNo().isEmpty())
      jcicZ573Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ573LogReposDay.saveAndFlush(jcicZ573Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ573LogReposMon.saveAndFlush(jcicZ573Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ573LogReposHist.saveAndFlush(jcicZ573Log);
    else 
    return jcicZ573LogRepos.saveAndFlush(jcicZ573Log);
  }

  @Override
  public JcicZ573Log update(JcicZ573Log jcicZ573Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ573Log.getJcicZ573LogId());
    if (!empNot.isEmpty())
      jcicZ573Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ573LogReposDay.saveAndFlush(jcicZ573Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ573LogReposMon.saveAndFlush(jcicZ573Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ573LogReposHist.saveAndFlush(jcicZ573Log);
    else 
    return jcicZ573LogRepos.saveAndFlush(jcicZ573Log);
  }

  @Override
  public JcicZ573Log update2(JcicZ573Log jcicZ573Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ573Log.getJcicZ573LogId());
    if (!empNot.isEmpty())
      jcicZ573Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ573LogReposDay.saveAndFlush(jcicZ573Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ573LogReposMon.saveAndFlush(jcicZ573Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ573LogReposHist.saveAndFlush(jcicZ573Log);
    else 
      jcicZ573LogRepos.saveAndFlush(jcicZ573Log);	
    return this.findById(jcicZ573Log.getJcicZ573LogId());
  }

  @Override
  public void delete(JcicZ573Log jcicZ573Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ573Log.getJcicZ573LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ573LogReposDay.delete(jcicZ573Log);	
      jcicZ573LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ573LogReposMon.delete(jcicZ573Log);	
      jcicZ573LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ573LogReposHist.delete(jcicZ573Log);
      jcicZ573LogReposHist.flush();
    }
    else {
      jcicZ573LogRepos.delete(jcicZ573Log);
      jcicZ573LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ573Log> jcicZ573Log, TitaVo... titaVo) throws DBException {
    if (jcicZ573Log == null || jcicZ573Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ573Log t : jcicZ573Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ573Log = jcicZ573LogReposDay.saveAll(jcicZ573Log);	
      jcicZ573LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ573Log = jcicZ573LogReposMon.saveAll(jcicZ573Log);	
      jcicZ573LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ573Log = jcicZ573LogReposHist.saveAll(jcicZ573Log);
      jcicZ573LogReposHist.flush();
    }
    else {
      jcicZ573Log = jcicZ573LogRepos.saveAll(jcicZ573Log);
      jcicZ573LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ573Log> jcicZ573Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ573Log == null || jcicZ573Log.size() == 0)
      throw new DBException(6);

    for (JcicZ573Log t : jcicZ573Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ573Log = jcicZ573LogReposDay.saveAll(jcicZ573Log);	
      jcicZ573LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ573Log = jcicZ573LogReposMon.saveAll(jcicZ573Log);	
      jcicZ573LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ573Log = jcicZ573LogReposHist.saveAll(jcicZ573Log);
      jcicZ573LogReposHist.flush();
    }
    else {
      jcicZ573Log = jcicZ573LogRepos.saveAll(jcicZ573Log);
      jcicZ573LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ573Log> jcicZ573Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ573Log == null || jcicZ573Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ573LogReposDay.deleteAll(jcicZ573Log);	
      jcicZ573LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ573LogReposMon.deleteAll(jcicZ573Log);	
      jcicZ573LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ573LogReposHist.deleteAll(jcicZ573Log);
      jcicZ573LogReposHist.flush();
    }
    else {
      jcicZ573LogRepos.deleteAll(jcicZ573Log);
      jcicZ573LogRepos.flush();
    }
  }

}
