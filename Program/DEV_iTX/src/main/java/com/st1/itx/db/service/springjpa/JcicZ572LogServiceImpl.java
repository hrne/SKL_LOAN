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
import com.st1.itx.db.domain.JcicZ572Log;
import com.st1.itx.db.domain.JcicZ572LogId;
import com.st1.itx.db.repository.online.JcicZ572LogRepository;
import com.st1.itx.db.repository.day.JcicZ572LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ572LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ572LogRepositoryHist;
import com.st1.itx.db.service.JcicZ572LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ572LogService")
@Repository
public class JcicZ572LogServiceImpl extends ASpringJpaParm implements JcicZ572LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ572LogRepository jcicZ572LogRepos;

  @Autowired
  private JcicZ572LogRepositoryDay jcicZ572LogReposDay;

  @Autowired
  private JcicZ572LogRepositoryMon jcicZ572LogReposMon;

  @Autowired
  private JcicZ572LogRepositoryHist jcicZ572LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ572LogRepos);
    org.junit.Assert.assertNotNull(jcicZ572LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ572LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ572LogReposHist);
  }

  @Override
  public JcicZ572Log findById(JcicZ572LogId jcicZ572LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ572LogId);
    Optional<JcicZ572Log> jcicZ572Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ572Log = jcicZ572LogReposDay.findById(jcicZ572LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ572Log = jcicZ572LogReposMon.findById(jcicZ572LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ572Log = jcicZ572LogReposHist.findById(jcicZ572LogId);
    else 
      jcicZ572Log = jcicZ572LogRepos.findById(jcicZ572LogId);
    JcicZ572Log obj = jcicZ572Log.isPresent() ? jcicZ572Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ572Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ572Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ572LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ572LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ572LogReposHist.findAll(pageable);
    else 
      slice = jcicZ572LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ572Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ572Log> jcicZ572LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ572LogT = jcicZ572LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ572LogT = jcicZ572LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ572LogT = jcicZ572LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ572LogT = jcicZ572LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ572LogT.isPresent() ? jcicZ572LogT.get() : null;
  }

  @Override
  public Slice<JcicZ572Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ572Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ572LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ572LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ572LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ572LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ572Log holdById(JcicZ572LogId jcicZ572LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ572LogId);
    Optional<JcicZ572Log> jcicZ572Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ572Log = jcicZ572LogReposDay.findByJcicZ572LogId(jcicZ572LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ572Log = jcicZ572LogReposMon.findByJcicZ572LogId(jcicZ572LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ572Log = jcicZ572LogReposHist.findByJcicZ572LogId(jcicZ572LogId);
    else 
      jcicZ572Log = jcicZ572LogRepos.findByJcicZ572LogId(jcicZ572LogId);
    return jcicZ572Log.isPresent() ? jcicZ572Log.get() : null;
  }

  @Override
  public JcicZ572Log holdById(JcicZ572Log jcicZ572Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ572Log.getJcicZ572LogId());
    Optional<JcicZ572Log> jcicZ572LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ572LogT = jcicZ572LogReposDay.findByJcicZ572LogId(jcicZ572Log.getJcicZ572LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ572LogT = jcicZ572LogReposMon.findByJcicZ572LogId(jcicZ572Log.getJcicZ572LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ572LogT = jcicZ572LogReposHist.findByJcicZ572LogId(jcicZ572Log.getJcicZ572LogId());
    else 
      jcicZ572LogT = jcicZ572LogRepos.findByJcicZ572LogId(jcicZ572Log.getJcicZ572LogId());
    return jcicZ572LogT.isPresent() ? jcicZ572LogT.get() : null;
  }

  @Override
  public JcicZ572Log insert(JcicZ572Log jcicZ572Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ572Log.getJcicZ572LogId());
    if (this.findById(jcicZ572Log.getJcicZ572LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ572Log.setCreateEmpNo(empNot);

    if(jcicZ572Log.getLastUpdateEmpNo() == null || jcicZ572Log.getLastUpdateEmpNo().isEmpty())
      jcicZ572Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ572LogReposDay.saveAndFlush(jcicZ572Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ572LogReposMon.saveAndFlush(jcicZ572Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ572LogReposHist.saveAndFlush(jcicZ572Log);
    else 
    return jcicZ572LogRepos.saveAndFlush(jcicZ572Log);
  }

  @Override
  public JcicZ572Log update(JcicZ572Log jcicZ572Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ572Log.getJcicZ572LogId());
    if (!empNot.isEmpty())
      jcicZ572Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ572LogReposDay.saveAndFlush(jcicZ572Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ572LogReposMon.saveAndFlush(jcicZ572Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ572LogReposHist.saveAndFlush(jcicZ572Log);
    else 
    return jcicZ572LogRepos.saveAndFlush(jcicZ572Log);
  }

  @Override
  public JcicZ572Log update2(JcicZ572Log jcicZ572Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ572Log.getJcicZ572LogId());
    if (!empNot.isEmpty())
      jcicZ572Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ572LogReposDay.saveAndFlush(jcicZ572Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ572LogReposMon.saveAndFlush(jcicZ572Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ572LogReposHist.saveAndFlush(jcicZ572Log);
    else 
      jcicZ572LogRepos.saveAndFlush(jcicZ572Log);	
    return this.findById(jcicZ572Log.getJcicZ572LogId());
  }

  @Override
  public void delete(JcicZ572Log jcicZ572Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ572Log.getJcicZ572LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ572LogReposDay.delete(jcicZ572Log);	
      jcicZ572LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ572LogReposMon.delete(jcicZ572Log);	
      jcicZ572LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ572LogReposHist.delete(jcicZ572Log);
      jcicZ572LogReposHist.flush();
    }
    else {
      jcicZ572LogRepos.delete(jcicZ572Log);
      jcicZ572LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ572Log> jcicZ572Log, TitaVo... titaVo) throws DBException {
    if (jcicZ572Log == null || jcicZ572Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ572Log t : jcicZ572Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ572Log = jcicZ572LogReposDay.saveAll(jcicZ572Log);	
      jcicZ572LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ572Log = jcicZ572LogReposMon.saveAll(jcicZ572Log);	
      jcicZ572LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ572Log = jcicZ572LogReposHist.saveAll(jcicZ572Log);
      jcicZ572LogReposHist.flush();
    }
    else {
      jcicZ572Log = jcicZ572LogRepos.saveAll(jcicZ572Log);
      jcicZ572LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ572Log> jcicZ572Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ572Log == null || jcicZ572Log.size() == 0)
      throw new DBException(6);

    for (JcicZ572Log t : jcicZ572Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ572Log = jcicZ572LogReposDay.saveAll(jcicZ572Log);	
      jcicZ572LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ572Log = jcicZ572LogReposMon.saveAll(jcicZ572Log);	
      jcicZ572LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ572Log = jcicZ572LogReposHist.saveAll(jcicZ572Log);
      jcicZ572LogReposHist.flush();
    }
    else {
      jcicZ572Log = jcicZ572LogRepos.saveAll(jcicZ572Log);
      jcicZ572LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ572Log> jcicZ572Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ572Log == null || jcicZ572Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ572LogReposDay.deleteAll(jcicZ572Log);	
      jcicZ572LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ572LogReposMon.deleteAll(jcicZ572Log);	
      jcicZ572LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ572LogReposHist.deleteAll(jcicZ572Log);
      jcicZ572LogReposHist.flush();
    }
    else {
      jcicZ572LogRepos.deleteAll(jcicZ572Log);
      jcicZ572LogRepos.flush();
    }
  }

}
