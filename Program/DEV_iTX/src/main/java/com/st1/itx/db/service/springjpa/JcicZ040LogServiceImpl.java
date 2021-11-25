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
import com.st1.itx.db.domain.JcicZ040Log;
import com.st1.itx.db.domain.JcicZ040LogId;
import com.st1.itx.db.repository.online.JcicZ040LogRepository;
import com.st1.itx.db.repository.day.JcicZ040LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ040LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ040LogRepositoryHist;
import com.st1.itx.db.service.JcicZ040LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ040LogService")
@Repository
public class JcicZ040LogServiceImpl extends ASpringJpaParm implements JcicZ040LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ040LogRepository jcicZ040LogRepos;

  @Autowired
  private JcicZ040LogRepositoryDay jcicZ040LogReposDay;

  @Autowired
  private JcicZ040LogRepositoryMon jcicZ040LogReposMon;

  @Autowired
  private JcicZ040LogRepositoryHist jcicZ040LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ040LogRepos);
    org.junit.Assert.assertNotNull(jcicZ040LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ040LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ040LogReposHist);
  }

  @Override
  public JcicZ040Log findById(JcicZ040LogId jcicZ040LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ040LogId);
    Optional<JcicZ040Log> jcicZ040Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ040Log = jcicZ040LogReposDay.findById(jcicZ040LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ040Log = jcicZ040LogReposMon.findById(jcicZ040LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ040Log = jcicZ040LogReposHist.findById(jcicZ040LogId);
    else 
      jcicZ040Log = jcicZ040LogRepos.findById(jcicZ040LogId);
    JcicZ040Log obj = jcicZ040Log.isPresent() ? jcicZ040Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ040Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ040Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ040LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ040LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ040LogReposHist.findAll(pageable);
    else 
      slice = jcicZ040LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ040Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ040Log> jcicZ040LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ040LogT = jcicZ040LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ040LogT = jcicZ040LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ040LogT = jcicZ040LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ040LogT = jcicZ040LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ040LogT.isPresent() ? jcicZ040LogT.get() : null;
  }

  @Override
  public Slice<JcicZ040Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ040Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ040LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ040LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ040LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ040LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ040Log holdById(JcicZ040LogId jcicZ040LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ040LogId);
    Optional<JcicZ040Log> jcicZ040Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ040Log = jcicZ040LogReposDay.findByJcicZ040LogId(jcicZ040LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ040Log = jcicZ040LogReposMon.findByJcicZ040LogId(jcicZ040LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ040Log = jcicZ040LogReposHist.findByJcicZ040LogId(jcicZ040LogId);
    else 
      jcicZ040Log = jcicZ040LogRepos.findByJcicZ040LogId(jcicZ040LogId);
    return jcicZ040Log.isPresent() ? jcicZ040Log.get() : null;
  }

  @Override
  public JcicZ040Log holdById(JcicZ040Log jcicZ040Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ040Log.getJcicZ040LogId());
    Optional<JcicZ040Log> jcicZ040LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ040LogT = jcicZ040LogReposDay.findByJcicZ040LogId(jcicZ040Log.getJcicZ040LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ040LogT = jcicZ040LogReposMon.findByJcicZ040LogId(jcicZ040Log.getJcicZ040LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ040LogT = jcicZ040LogReposHist.findByJcicZ040LogId(jcicZ040Log.getJcicZ040LogId());
    else 
      jcicZ040LogT = jcicZ040LogRepos.findByJcicZ040LogId(jcicZ040Log.getJcicZ040LogId());
    return jcicZ040LogT.isPresent() ? jcicZ040LogT.get() : null;
  }

  @Override
  public JcicZ040Log insert(JcicZ040Log jcicZ040Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ040Log.getJcicZ040LogId());
    if (this.findById(jcicZ040Log.getJcicZ040LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ040Log.setCreateEmpNo(empNot);

    if(jcicZ040Log.getLastUpdateEmpNo() == null || jcicZ040Log.getLastUpdateEmpNo().isEmpty())
      jcicZ040Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ040LogReposDay.saveAndFlush(jcicZ040Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ040LogReposMon.saveAndFlush(jcicZ040Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ040LogReposHist.saveAndFlush(jcicZ040Log);
    else 
    return jcicZ040LogRepos.saveAndFlush(jcicZ040Log);
  }

  @Override
  public JcicZ040Log update(JcicZ040Log jcicZ040Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ040Log.getJcicZ040LogId());
    if (!empNot.isEmpty())
      jcicZ040Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ040LogReposDay.saveAndFlush(jcicZ040Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ040LogReposMon.saveAndFlush(jcicZ040Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ040LogReposHist.saveAndFlush(jcicZ040Log);
    else 
    return jcicZ040LogRepos.saveAndFlush(jcicZ040Log);
  }

  @Override
  public JcicZ040Log update2(JcicZ040Log jcicZ040Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ040Log.getJcicZ040LogId());
    if (!empNot.isEmpty())
      jcicZ040Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ040LogReposDay.saveAndFlush(jcicZ040Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ040LogReposMon.saveAndFlush(jcicZ040Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ040LogReposHist.saveAndFlush(jcicZ040Log);
    else 
      jcicZ040LogRepos.saveAndFlush(jcicZ040Log);	
    return this.findById(jcicZ040Log.getJcicZ040LogId());
  }

  @Override
  public void delete(JcicZ040Log jcicZ040Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ040Log.getJcicZ040LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ040LogReposDay.delete(jcicZ040Log);	
      jcicZ040LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ040LogReposMon.delete(jcicZ040Log);	
      jcicZ040LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ040LogReposHist.delete(jcicZ040Log);
      jcicZ040LogReposHist.flush();
    }
    else {
      jcicZ040LogRepos.delete(jcicZ040Log);
      jcicZ040LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ040Log> jcicZ040Log, TitaVo... titaVo) throws DBException {
    if (jcicZ040Log == null || jcicZ040Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ040Log t : jcicZ040Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ040Log = jcicZ040LogReposDay.saveAll(jcicZ040Log);	
      jcicZ040LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ040Log = jcicZ040LogReposMon.saveAll(jcicZ040Log);	
      jcicZ040LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ040Log = jcicZ040LogReposHist.saveAll(jcicZ040Log);
      jcicZ040LogReposHist.flush();
    }
    else {
      jcicZ040Log = jcicZ040LogRepos.saveAll(jcicZ040Log);
      jcicZ040LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ040Log> jcicZ040Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ040Log == null || jcicZ040Log.size() == 0)
      throw new DBException(6);

    for (JcicZ040Log t : jcicZ040Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ040Log = jcicZ040LogReposDay.saveAll(jcicZ040Log);	
      jcicZ040LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ040Log = jcicZ040LogReposMon.saveAll(jcicZ040Log);	
      jcicZ040LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ040Log = jcicZ040LogReposHist.saveAll(jcicZ040Log);
      jcicZ040LogReposHist.flush();
    }
    else {
      jcicZ040Log = jcicZ040LogRepos.saveAll(jcicZ040Log);
      jcicZ040LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ040Log> jcicZ040Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ040Log == null || jcicZ040Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ040LogReposDay.deleteAll(jcicZ040Log);	
      jcicZ040LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ040LogReposMon.deleteAll(jcicZ040Log);	
      jcicZ040LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ040LogReposHist.deleteAll(jcicZ040Log);
      jcicZ040LogReposHist.flush();
    }
    else {
      jcicZ040LogRepos.deleteAll(jcicZ040Log);
      jcicZ040LogRepos.flush();
    }
  }

}
