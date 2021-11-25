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
import com.st1.itx.db.domain.JcicZ050Log;
import com.st1.itx.db.domain.JcicZ050LogId;
import com.st1.itx.db.repository.online.JcicZ050LogRepository;
import com.st1.itx.db.repository.day.JcicZ050LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ050LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ050LogRepositoryHist;
import com.st1.itx.db.service.JcicZ050LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ050LogService")
@Repository
public class JcicZ050LogServiceImpl extends ASpringJpaParm implements JcicZ050LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ050LogRepository jcicZ050LogRepos;

  @Autowired
  private JcicZ050LogRepositoryDay jcicZ050LogReposDay;

  @Autowired
  private JcicZ050LogRepositoryMon jcicZ050LogReposMon;

  @Autowired
  private JcicZ050LogRepositoryHist jcicZ050LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ050LogRepos);
    org.junit.Assert.assertNotNull(jcicZ050LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ050LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ050LogReposHist);
  }

  @Override
  public JcicZ050Log findById(JcicZ050LogId jcicZ050LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ050LogId);
    Optional<JcicZ050Log> jcicZ050Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ050Log = jcicZ050LogReposDay.findById(jcicZ050LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ050Log = jcicZ050LogReposMon.findById(jcicZ050LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ050Log = jcicZ050LogReposHist.findById(jcicZ050LogId);
    else 
      jcicZ050Log = jcicZ050LogRepos.findById(jcicZ050LogId);
    JcicZ050Log obj = jcicZ050Log.isPresent() ? jcicZ050Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ050Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ050Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ050LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ050LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ050LogReposHist.findAll(pageable);
    else 
      slice = jcicZ050LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ050Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ050Log> jcicZ050LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ050LogT = jcicZ050LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ050LogT = jcicZ050LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ050LogT = jcicZ050LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ050LogT = jcicZ050LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ050LogT.isPresent() ? jcicZ050LogT.get() : null;
  }

  @Override
  public Slice<JcicZ050Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ050Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ050LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ050LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ050LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ050LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ050Log holdById(JcicZ050LogId jcicZ050LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ050LogId);
    Optional<JcicZ050Log> jcicZ050Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ050Log = jcicZ050LogReposDay.findByJcicZ050LogId(jcicZ050LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ050Log = jcicZ050LogReposMon.findByJcicZ050LogId(jcicZ050LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ050Log = jcicZ050LogReposHist.findByJcicZ050LogId(jcicZ050LogId);
    else 
      jcicZ050Log = jcicZ050LogRepos.findByJcicZ050LogId(jcicZ050LogId);
    return jcicZ050Log.isPresent() ? jcicZ050Log.get() : null;
  }

  @Override
  public JcicZ050Log holdById(JcicZ050Log jcicZ050Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ050Log.getJcicZ050LogId());
    Optional<JcicZ050Log> jcicZ050LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ050LogT = jcicZ050LogReposDay.findByJcicZ050LogId(jcicZ050Log.getJcicZ050LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ050LogT = jcicZ050LogReposMon.findByJcicZ050LogId(jcicZ050Log.getJcicZ050LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ050LogT = jcicZ050LogReposHist.findByJcicZ050LogId(jcicZ050Log.getJcicZ050LogId());
    else 
      jcicZ050LogT = jcicZ050LogRepos.findByJcicZ050LogId(jcicZ050Log.getJcicZ050LogId());
    return jcicZ050LogT.isPresent() ? jcicZ050LogT.get() : null;
  }

  @Override
  public JcicZ050Log insert(JcicZ050Log jcicZ050Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ050Log.getJcicZ050LogId());
    if (this.findById(jcicZ050Log.getJcicZ050LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ050Log.setCreateEmpNo(empNot);

    if(jcicZ050Log.getLastUpdateEmpNo() == null || jcicZ050Log.getLastUpdateEmpNo().isEmpty())
      jcicZ050Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ050LogReposDay.saveAndFlush(jcicZ050Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ050LogReposMon.saveAndFlush(jcicZ050Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ050LogReposHist.saveAndFlush(jcicZ050Log);
    else 
    return jcicZ050LogRepos.saveAndFlush(jcicZ050Log);
  }

  @Override
  public JcicZ050Log update(JcicZ050Log jcicZ050Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ050Log.getJcicZ050LogId());
    if (!empNot.isEmpty())
      jcicZ050Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ050LogReposDay.saveAndFlush(jcicZ050Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ050LogReposMon.saveAndFlush(jcicZ050Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ050LogReposHist.saveAndFlush(jcicZ050Log);
    else 
    return jcicZ050LogRepos.saveAndFlush(jcicZ050Log);
  }

  @Override
  public JcicZ050Log update2(JcicZ050Log jcicZ050Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ050Log.getJcicZ050LogId());
    if (!empNot.isEmpty())
      jcicZ050Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ050LogReposDay.saveAndFlush(jcicZ050Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ050LogReposMon.saveAndFlush(jcicZ050Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ050LogReposHist.saveAndFlush(jcicZ050Log);
    else 
      jcicZ050LogRepos.saveAndFlush(jcicZ050Log);	
    return this.findById(jcicZ050Log.getJcicZ050LogId());
  }

  @Override
  public void delete(JcicZ050Log jcicZ050Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ050Log.getJcicZ050LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ050LogReposDay.delete(jcicZ050Log);	
      jcicZ050LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ050LogReposMon.delete(jcicZ050Log);	
      jcicZ050LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ050LogReposHist.delete(jcicZ050Log);
      jcicZ050LogReposHist.flush();
    }
    else {
      jcicZ050LogRepos.delete(jcicZ050Log);
      jcicZ050LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ050Log> jcicZ050Log, TitaVo... titaVo) throws DBException {
    if (jcicZ050Log == null || jcicZ050Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ050Log t : jcicZ050Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ050Log = jcicZ050LogReposDay.saveAll(jcicZ050Log);	
      jcicZ050LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ050Log = jcicZ050LogReposMon.saveAll(jcicZ050Log);	
      jcicZ050LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ050Log = jcicZ050LogReposHist.saveAll(jcicZ050Log);
      jcicZ050LogReposHist.flush();
    }
    else {
      jcicZ050Log = jcicZ050LogRepos.saveAll(jcicZ050Log);
      jcicZ050LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ050Log> jcicZ050Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ050Log == null || jcicZ050Log.size() == 0)
      throw new DBException(6);

    for (JcicZ050Log t : jcicZ050Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ050Log = jcicZ050LogReposDay.saveAll(jcicZ050Log);	
      jcicZ050LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ050Log = jcicZ050LogReposMon.saveAll(jcicZ050Log);	
      jcicZ050LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ050Log = jcicZ050LogReposHist.saveAll(jcicZ050Log);
      jcicZ050LogReposHist.flush();
    }
    else {
      jcicZ050Log = jcicZ050LogRepos.saveAll(jcicZ050Log);
      jcicZ050LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ050Log> jcicZ050Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ050Log == null || jcicZ050Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ050LogReposDay.deleteAll(jcicZ050Log);	
      jcicZ050LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ050LogReposMon.deleteAll(jcicZ050Log);	
      jcicZ050LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ050LogReposHist.deleteAll(jcicZ050Log);
      jcicZ050LogReposHist.flush();
    }
    else {
      jcicZ050LogRepos.deleteAll(jcicZ050Log);
      jcicZ050LogRepos.flush();
    }
  }

}
