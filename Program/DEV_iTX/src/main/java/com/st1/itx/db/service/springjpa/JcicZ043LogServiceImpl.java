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
import com.st1.itx.db.domain.JcicZ043Log;
import com.st1.itx.db.domain.JcicZ043LogId;
import com.st1.itx.db.repository.online.JcicZ043LogRepository;
import com.st1.itx.db.repository.day.JcicZ043LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ043LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ043LogRepositoryHist;
import com.st1.itx.db.service.JcicZ043LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ043LogService")
@Repository
public class JcicZ043LogServiceImpl extends ASpringJpaParm implements JcicZ043LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ043LogRepository jcicZ043LogRepos;

  @Autowired
  private JcicZ043LogRepositoryDay jcicZ043LogReposDay;

  @Autowired
  private JcicZ043LogRepositoryMon jcicZ043LogReposMon;

  @Autowired
  private JcicZ043LogRepositoryHist jcicZ043LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ043LogRepos);
    org.junit.Assert.assertNotNull(jcicZ043LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ043LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ043LogReposHist);
  }

  @Override
  public JcicZ043Log findById(JcicZ043LogId jcicZ043LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ043LogId);
    Optional<JcicZ043Log> jcicZ043Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ043Log = jcicZ043LogReposDay.findById(jcicZ043LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ043Log = jcicZ043LogReposMon.findById(jcicZ043LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ043Log = jcicZ043LogReposHist.findById(jcicZ043LogId);
    else 
      jcicZ043Log = jcicZ043LogRepos.findById(jcicZ043LogId);
    JcicZ043Log obj = jcicZ043Log.isPresent() ? jcicZ043Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ043Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ043Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ043LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ043LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ043LogReposHist.findAll(pageable);
    else 
      slice = jcicZ043LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ043Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ043Log> jcicZ043LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ043LogT = jcicZ043LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ043LogT = jcicZ043LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ043LogT = jcicZ043LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ043LogT = jcicZ043LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ043LogT.isPresent() ? jcicZ043LogT.get() : null;
  }

  @Override
  public Slice<JcicZ043Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ043Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ043LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ043LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ043LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ043LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ043Log holdById(JcicZ043LogId jcicZ043LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ043LogId);
    Optional<JcicZ043Log> jcicZ043Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ043Log = jcicZ043LogReposDay.findByJcicZ043LogId(jcicZ043LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ043Log = jcicZ043LogReposMon.findByJcicZ043LogId(jcicZ043LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ043Log = jcicZ043LogReposHist.findByJcicZ043LogId(jcicZ043LogId);
    else 
      jcicZ043Log = jcicZ043LogRepos.findByJcicZ043LogId(jcicZ043LogId);
    return jcicZ043Log.isPresent() ? jcicZ043Log.get() : null;
  }

  @Override
  public JcicZ043Log holdById(JcicZ043Log jcicZ043Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ043Log.getJcicZ043LogId());
    Optional<JcicZ043Log> jcicZ043LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ043LogT = jcicZ043LogReposDay.findByJcicZ043LogId(jcicZ043Log.getJcicZ043LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ043LogT = jcicZ043LogReposMon.findByJcicZ043LogId(jcicZ043Log.getJcicZ043LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ043LogT = jcicZ043LogReposHist.findByJcicZ043LogId(jcicZ043Log.getJcicZ043LogId());
    else 
      jcicZ043LogT = jcicZ043LogRepos.findByJcicZ043LogId(jcicZ043Log.getJcicZ043LogId());
    return jcicZ043LogT.isPresent() ? jcicZ043LogT.get() : null;
  }

  @Override
  public JcicZ043Log insert(JcicZ043Log jcicZ043Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ043Log.getJcicZ043LogId());
    if (this.findById(jcicZ043Log.getJcicZ043LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ043Log.setCreateEmpNo(empNot);

    if(jcicZ043Log.getLastUpdateEmpNo() == null || jcicZ043Log.getLastUpdateEmpNo().isEmpty())
      jcicZ043Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ043LogReposDay.saveAndFlush(jcicZ043Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ043LogReposMon.saveAndFlush(jcicZ043Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ043LogReposHist.saveAndFlush(jcicZ043Log);
    else 
    return jcicZ043LogRepos.saveAndFlush(jcicZ043Log);
  }

  @Override
  public JcicZ043Log update(JcicZ043Log jcicZ043Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ043Log.getJcicZ043LogId());
    if (!empNot.isEmpty())
      jcicZ043Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ043LogReposDay.saveAndFlush(jcicZ043Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ043LogReposMon.saveAndFlush(jcicZ043Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ043LogReposHist.saveAndFlush(jcicZ043Log);
    else 
    return jcicZ043LogRepos.saveAndFlush(jcicZ043Log);
  }

  @Override
  public JcicZ043Log update2(JcicZ043Log jcicZ043Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ043Log.getJcicZ043LogId());
    if (!empNot.isEmpty())
      jcicZ043Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ043LogReposDay.saveAndFlush(jcicZ043Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ043LogReposMon.saveAndFlush(jcicZ043Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ043LogReposHist.saveAndFlush(jcicZ043Log);
    else 
      jcicZ043LogRepos.saveAndFlush(jcicZ043Log);	
    return this.findById(jcicZ043Log.getJcicZ043LogId());
  }

  @Override
  public void delete(JcicZ043Log jcicZ043Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ043Log.getJcicZ043LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ043LogReposDay.delete(jcicZ043Log);	
      jcicZ043LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ043LogReposMon.delete(jcicZ043Log);	
      jcicZ043LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ043LogReposHist.delete(jcicZ043Log);
      jcicZ043LogReposHist.flush();
    }
    else {
      jcicZ043LogRepos.delete(jcicZ043Log);
      jcicZ043LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ043Log> jcicZ043Log, TitaVo... titaVo) throws DBException {
    if (jcicZ043Log == null || jcicZ043Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ043Log t : jcicZ043Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ043Log = jcicZ043LogReposDay.saveAll(jcicZ043Log);	
      jcicZ043LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ043Log = jcicZ043LogReposMon.saveAll(jcicZ043Log);	
      jcicZ043LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ043Log = jcicZ043LogReposHist.saveAll(jcicZ043Log);
      jcicZ043LogReposHist.flush();
    }
    else {
      jcicZ043Log = jcicZ043LogRepos.saveAll(jcicZ043Log);
      jcicZ043LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ043Log> jcicZ043Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ043Log == null || jcicZ043Log.size() == 0)
      throw new DBException(6);

    for (JcicZ043Log t : jcicZ043Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ043Log = jcicZ043LogReposDay.saveAll(jcicZ043Log);	
      jcicZ043LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ043Log = jcicZ043LogReposMon.saveAll(jcicZ043Log);	
      jcicZ043LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ043Log = jcicZ043LogReposHist.saveAll(jcicZ043Log);
      jcicZ043LogReposHist.flush();
    }
    else {
      jcicZ043Log = jcicZ043LogRepos.saveAll(jcicZ043Log);
      jcicZ043LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ043Log> jcicZ043Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ043Log == null || jcicZ043Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ043LogReposDay.deleteAll(jcicZ043Log);	
      jcicZ043LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ043LogReposMon.deleteAll(jcicZ043Log);	
      jcicZ043LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ043LogReposHist.deleteAll(jcicZ043Log);
      jcicZ043LogReposHist.flush();
    }
    else {
      jcicZ043LogRepos.deleteAll(jcicZ043Log);
      jcicZ043LogRepos.flush();
    }
  }

}
