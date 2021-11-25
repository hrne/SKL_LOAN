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
import com.st1.itx.db.domain.JcicZ047Log;
import com.st1.itx.db.domain.JcicZ047LogId;
import com.st1.itx.db.repository.online.JcicZ047LogRepository;
import com.st1.itx.db.repository.day.JcicZ047LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ047LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ047LogRepositoryHist;
import com.st1.itx.db.service.JcicZ047LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ047LogService")
@Repository
public class JcicZ047LogServiceImpl extends ASpringJpaParm implements JcicZ047LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ047LogRepository jcicZ047LogRepos;

  @Autowired
  private JcicZ047LogRepositoryDay jcicZ047LogReposDay;

  @Autowired
  private JcicZ047LogRepositoryMon jcicZ047LogReposMon;

  @Autowired
  private JcicZ047LogRepositoryHist jcicZ047LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ047LogRepos);
    org.junit.Assert.assertNotNull(jcicZ047LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ047LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ047LogReposHist);
  }

  @Override
  public JcicZ047Log findById(JcicZ047LogId jcicZ047LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ047LogId);
    Optional<JcicZ047Log> jcicZ047Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ047Log = jcicZ047LogReposDay.findById(jcicZ047LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ047Log = jcicZ047LogReposMon.findById(jcicZ047LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ047Log = jcicZ047LogReposHist.findById(jcicZ047LogId);
    else 
      jcicZ047Log = jcicZ047LogRepos.findById(jcicZ047LogId);
    JcicZ047Log obj = jcicZ047Log.isPresent() ? jcicZ047Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ047Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ047Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ047LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ047LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ047LogReposHist.findAll(pageable);
    else 
      slice = jcicZ047LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ047Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ047Log> jcicZ047LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ047LogT = jcicZ047LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ047LogT = jcicZ047LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ047LogT = jcicZ047LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ047LogT = jcicZ047LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ047LogT.isPresent() ? jcicZ047LogT.get() : null;
  }

  @Override
  public Slice<JcicZ047Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ047Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ047LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ047LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ047LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ047LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ047Log holdById(JcicZ047LogId jcicZ047LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ047LogId);
    Optional<JcicZ047Log> jcicZ047Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ047Log = jcicZ047LogReposDay.findByJcicZ047LogId(jcicZ047LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ047Log = jcicZ047LogReposMon.findByJcicZ047LogId(jcicZ047LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ047Log = jcicZ047LogReposHist.findByJcicZ047LogId(jcicZ047LogId);
    else 
      jcicZ047Log = jcicZ047LogRepos.findByJcicZ047LogId(jcicZ047LogId);
    return jcicZ047Log.isPresent() ? jcicZ047Log.get() : null;
  }

  @Override
  public JcicZ047Log holdById(JcicZ047Log jcicZ047Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ047Log.getJcicZ047LogId());
    Optional<JcicZ047Log> jcicZ047LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ047LogT = jcicZ047LogReposDay.findByJcicZ047LogId(jcicZ047Log.getJcicZ047LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ047LogT = jcicZ047LogReposMon.findByJcicZ047LogId(jcicZ047Log.getJcicZ047LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ047LogT = jcicZ047LogReposHist.findByJcicZ047LogId(jcicZ047Log.getJcicZ047LogId());
    else 
      jcicZ047LogT = jcicZ047LogRepos.findByJcicZ047LogId(jcicZ047Log.getJcicZ047LogId());
    return jcicZ047LogT.isPresent() ? jcicZ047LogT.get() : null;
  }

  @Override
  public JcicZ047Log insert(JcicZ047Log jcicZ047Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ047Log.getJcicZ047LogId());
    if (this.findById(jcicZ047Log.getJcicZ047LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ047Log.setCreateEmpNo(empNot);

    if(jcicZ047Log.getLastUpdateEmpNo() == null || jcicZ047Log.getLastUpdateEmpNo().isEmpty())
      jcicZ047Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ047LogReposDay.saveAndFlush(jcicZ047Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ047LogReposMon.saveAndFlush(jcicZ047Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ047LogReposHist.saveAndFlush(jcicZ047Log);
    else 
    return jcicZ047LogRepos.saveAndFlush(jcicZ047Log);
  }

  @Override
  public JcicZ047Log update(JcicZ047Log jcicZ047Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ047Log.getJcicZ047LogId());
    if (!empNot.isEmpty())
      jcicZ047Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ047LogReposDay.saveAndFlush(jcicZ047Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ047LogReposMon.saveAndFlush(jcicZ047Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ047LogReposHist.saveAndFlush(jcicZ047Log);
    else 
    return jcicZ047LogRepos.saveAndFlush(jcicZ047Log);
  }

  @Override
  public JcicZ047Log update2(JcicZ047Log jcicZ047Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ047Log.getJcicZ047LogId());
    if (!empNot.isEmpty())
      jcicZ047Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ047LogReposDay.saveAndFlush(jcicZ047Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ047LogReposMon.saveAndFlush(jcicZ047Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ047LogReposHist.saveAndFlush(jcicZ047Log);
    else 
      jcicZ047LogRepos.saveAndFlush(jcicZ047Log);	
    return this.findById(jcicZ047Log.getJcicZ047LogId());
  }

  @Override
  public void delete(JcicZ047Log jcicZ047Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ047Log.getJcicZ047LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ047LogReposDay.delete(jcicZ047Log);	
      jcicZ047LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ047LogReposMon.delete(jcicZ047Log);	
      jcicZ047LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ047LogReposHist.delete(jcicZ047Log);
      jcicZ047LogReposHist.flush();
    }
    else {
      jcicZ047LogRepos.delete(jcicZ047Log);
      jcicZ047LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ047Log> jcicZ047Log, TitaVo... titaVo) throws DBException {
    if (jcicZ047Log == null || jcicZ047Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ047Log t : jcicZ047Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ047Log = jcicZ047LogReposDay.saveAll(jcicZ047Log);	
      jcicZ047LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ047Log = jcicZ047LogReposMon.saveAll(jcicZ047Log);	
      jcicZ047LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ047Log = jcicZ047LogReposHist.saveAll(jcicZ047Log);
      jcicZ047LogReposHist.flush();
    }
    else {
      jcicZ047Log = jcicZ047LogRepos.saveAll(jcicZ047Log);
      jcicZ047LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ047Log> jcicZ047Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ047Log == null || jcicZ047Log.size() == 0)
      throw new DBException(6);

    for (JcicZ047Log t : jcicZ047Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ047Log = jcicZ047LogReposDay.saveAll(jcicZ047Log);	
      jcicZ047LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ047Log = jcicZ047LogReposMon.saveAll(jcicZ047Log);	
      jcicZ047LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ047Log = jcicZ047LogReposHist.saveAll(jcicZ047Log);
      jcicZ047LogReposHist.flush();
    }
    else {
      jcicZ047Log = jcicZ047LogRepos.saveAll(jcicZ047Log);
      jcicZ047LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ047Log> jcicZ047Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ047Log == null || jcicZ047Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ047LogReposDay.deleteAll(jcicZ047Log);	
      jcicZ047LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ047LogReposMon.deleteAll(jcicZ047Log);	
      jcicZ047LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ047LogReposHist.deleteAll(jcicZ047Log);
      jcicZ047LogReposHist.flush();
    }
    else {
      jcicZ047LogRepos.deleteAll(jcicZ047Log);
      jcicZ047LogRepos.flush();
    }
  }

}
