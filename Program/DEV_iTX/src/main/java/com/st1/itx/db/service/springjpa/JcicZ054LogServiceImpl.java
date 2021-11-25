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
import com.st1.itx.db.domain.JcicZ054Log;
import com.st1.itx.db.domain.JcicZ054LogId;
import com.st1.itx.db.repository.online.JcicZ054LogRepository;
import com.st1.itx.db.repository.day.JcicZ054LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ054LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ054LogRepositoryHist;
import com.st1.itx.db.service.JcicZ054LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ054LogService")
@Repository
public class JcicZ054LogServiceImpl extends ASpringJpaParm implements JcicZ054LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ054LogRepository jcicZ054LogRepos;

  @Autowired
  private JcicZ054LogRepositoryDay jcicZ054LogReposDay;

  @Autowired
  private JcicZ054LogRepositoryMon jcicZ054LogReposMon;

  @Autowired
  private JcicZ054LogRepositoryHist jcicZ054LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ054LogRepos);
    org.junit.Assert.assertNotNull(jcicZ054LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ054LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ054LogReposHist);
  }

  @Override
  public JcicZ054Log findById(JcicZ054LogId jcicZ054LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ054LogId);
    Optional<JcicZ054Log> jcicZ054Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ054Log = jcicZ054LogReposDay.findById(jcicZ054LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ054Log = jcicZ054LogReposMon.findById(jcicZ054LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ054Log = jcicZ054LogReposHist.findById(jcicZ054LogId);
    else 
      jcicZ054Log = jcicZ054LogRepos.findById(jcicZ054LogId);
    JcicZ054Log obj = jcicZ054Log.isPresent() ? jcicZ054Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ054Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054LogReposHist.findAll(pageable);
    else 
      slice = jcicZ054LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ054Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ054Log> jcicZ054LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ054LogT = jcicZ054LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ054LogT = jcicZ054LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ054LogT = jcicZ054LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ054LogT = jcicZ054LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ054LogT.isPresent() ? jcicZ054LogT.get() : null;
  }

  @Override
  public Slice<JcicZ054Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ054Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ054LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ054LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ054LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ054LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ054Log holdById(JcicZ054LogId jcicZ054LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ054LogId);
    Optional<JcicZ054Log> jcicZ054Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ054Log = jcicZ054LogReposDay.findByJcicZ054LogId(jcicZ054LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ054Log = jcicZ054LogReposMon.findByJcicZ054LogId(jcicZ054LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ054Log = jcicZ054LogReposHist.findByJcicZ054LogId(jcicZ054LogId);
    else 
      jcicZ054Log = jcicZ054LogRepos.findByJcicZ054LogId(jcicZ054LogId);
    return jcicZ054Log.isPresent() ? jcicZ054Log.get() : null;
  }

  @Override
  public JcicZ054Log holdById(JcicZ054Log jcicZ054Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ054Log.getJcicZ054LogId());
    Optional<JcicZ054Log> jcicZ054LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ054LogT = jcicZ054LogReposDay.findByJcicZ054LogId(jcicZ054Log.getJcicZ054LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ054LogT = jcicZ054LogReposMon.findByJcicZ054LogId(jcicZ054Log.getJcicZ054LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ054LogT = jcicZ054LogReposHist.findByJcicZ054LogId(jcicZ054Log.getJcicZ054LogId());
    else 
      jcicZ054LogT = jcicZ054LogRepos.findByJcicZ054LogId(jcicZ054Log.getJcicZ054LogId());
    return jcicZ054LogT.isPresent() ? jcicZ054LogT.get() : null;
  }

  @Override
  public JcicZ054Log insert(JcicZ054Log jcicZ054Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ054Log.getJcicZ054LogId());
    if (this.findById(jcicZ054Log.getJcicZ054LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ054Log.setCreateEmpNo(empNot);

    if(jcicZ054Log.getLastUpdateEmpNo() == null || jcicZ054Log.getLastUpdateEmpNo().isEmpty())
      jcicZ054Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ054LogReposDay.saveAndFlush(jcicZ054Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ054LogReposMon.saveAndFlush(jcicZ054Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ054LogReposHist.saveAndFlush(jcicZ054Log);
    else 
    return jcicZ054LogRepos.saveAndFlush(jcicZ054Log);
  }

  @Override
  public JcicZ054Log update(JcicZ054Log jcicZ054Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ054Log.getJcicZ054LogId());
    if (!empNot.isEmpty())
      jcicZ054Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ054LogReposDay.saveAndFlush(jcicZ054Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ054LogReposMon.saveAndFlush(jcicZ054Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ054LogReposHist.saveAndFlush(jcicZ054Log);
    else 
    return jcicZ054LogRepos.saveAndFlush(jcicZ054Log);
  }

  @Override
  public JcicZ054Log update2(JcicZ054Log jcicZ054Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ054Log.getJcicZ054LogId());
    if (!empNot.isEmpty())
      jcicZ054Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ054LogReposDay.saveAndFlush(jcicZ054Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ054LogReposMon.saveAndFlush(jcicZ054Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ054LogReposHist.saveAndFlush(jcicZ054Log);
    else 
      jcicZ054LogRepos.saveAndFlush(jcicZ054Log);	
    return this.findById(jcicZ054Log.getJcicZ054LogId());
  }

  @Override
  public void delete(JcicZ054Log jcicZ054Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ054Log.getJcicZ054LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ054LogReposDay.delete(jcicZ054Log);	
      jcicZ054LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ054LogReposMon.delete(jcicZ054Log);	
      jcicZ054LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ054LogReposHist.delete(jcicZ054Log);
      jcicZ054LogReposHist.flush();
    }
    else {
      jcicZ054LogRepos.delete(jcicZ054Log);
      jcicZ054LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ054Log> jcicZ054Log, TitaVo... titaVo) throws DBException {
    if (jcicZ054Log == null || jcicZ054Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ054Log t : jcicZ054Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ054Log = jcicZ054LogReposDay.saveAll(jcicZ054Log);	
      jcicZ054LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ054Log = jcicZ054LogReposMon.saveAll(jcicZ054Log);	
      jcicZ054LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ054Log = jcicZ054LogReposHist.saveAll(jcicZ054Log);
      jcicZ054LogReposHist.flush();
    }
    else {
      jcicZ054Log = jcicZ054LogRepos.saveAll(jcicZ054Log);
      jcicZ054LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ054Log> jcicZ054Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ054Log == null || jcicZ054Log.size() == 0)
      throw new DBException(6);

    for (JcicZ054Log t : jcicZ054Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ054Log = jcicZ054LogReposDay.saveAll(jcicZ054Log);	
      jcicZ054LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ054Log = jcicZ054LogReposMon.saveAll(jcicZ054Log);	
      jcicZ054LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ054Log = jcicZ054LogReposHist.saveAll(jcicZ054Log);
      jcicZ054LogReposHist.flush();
    }
    else {
      jcicZ054Log = jcicZ054LogRepos.saveAll(jcicZ054Log);
      jcicZ054LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ054Log> jcicZ054Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ054Log == null || jcicZ054Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ054LogReposDay.deleteAll(jcicZ054Log);	
      jcicZ054LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ054LogReposMon.deleteAll(jcicZ054Log);	
      jcicZ054LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ054LogReposHist.deleteAll(jcicZ054Log);
      jcicZ054LogReposHist.flush();
    }
    else {
      jcicZ054LogRepos.deleteAll(jcicZ054Log);
      jcicZ054LogRepos.flush();
    }
  }

}
