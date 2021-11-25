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
import com.st1.itx.db.domain.JcicZ045Log;
import com.st1.itx.db.domain.JcicZ045LogId;
import com.st1.itx.db.repository.online.JcicZ045LogRepository;
import com.st1.itx.db.repository.day.JcicZ045LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ045LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ045LogRepositoryHist;
import com.st1.itx.db.service.JcicZ045LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ045LogService")
@Repository
public class JcicZ045LogServiceImpl extends ASpringJpaParm implements JcicZ045LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ045LogRepository jcicZ045LogRepos;

  @Autowired
  private JcicZ045LogRepositoryDay jcicZ045LogReposDay;

  @Autowired
  private JcicZ045LogRepositoryMon jcicZ045LogReposMon;

  @Autowired
  private JcicZ045LogRepositoryHist jcicZ045LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ045LogRepos);
    org.junit.Assert.assertNotNull(jcicZ045LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ045LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ045LogReposHist);
  }

  @Override
  public JcicZ045Log findById(JcicZ045LogId jcicZ045LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ045LogId);
    Optional<JcicZ045Log> jcicZ045Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ045Log = jcicZ045LogReposDay.findById(jcicZ045LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ045Log = jcicZ045LogReposMon.findById(jcicZ045LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ045Log = jcicZ045LogReposHist.findById(jcicZ045LogId);
    else 
      jcicZ045Log = jcicZ045LogRepos.findById(jcicZ045LogId);
    JcicZ045Log obj = jcicZ045Log.isPresent() ? jcicZ045Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ045Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ045Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ045LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ045LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ045LogReposHist.findAll(pageable);
    else 
      slice = jcicZ045LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ045Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ045Log> jcicZ045LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ045LogT = jcicZ045LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ045LogT = jcicZ045LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ045LogT = jcicZ045LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ045LogT = jcicZ045LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ045LogT.isPresent() ? jcicZ045LogT.get() : null;
  }

  @Override
  public Slice<JcicZ045Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ045Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ045LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ045LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ045LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ045LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ045Log holdById(JcicZ045LogId jcicZ045LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ045LogId);
    Optional<JcicZ045Log> jcicZ045Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ045Log = jcicZ045LogReposDay.findByJcicZ045LogId(jcicZ045LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ045Log = jcicZ045LogReposMon.findByJcicZ045LogId(jcicZ045LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ045Log = jcicZ045LogReposHist.findByJcicZ045LogId(jcicZ045LogId);
    else 
      jcicZ045Log = jcicZ045LogRepos.findByJcicZ045LogId(jcicZ045LogId);
    return jcicZ045Log.isPresent() ? jcicZ045Log.get() : null;
  }

  @Override
  public JcicZ045Log holdById(JcicZ045Log jcicZ045Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ045Log.getJcicZ045LogId());
    Optional<JcicZ045Log> jcicZ045LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ045LogT = jcicZ045LogReposDay.findByJcicZ045LogId(jcicZ045Log.getJcicZ045LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ045LogT = jcicZ045LogReposMon.findByJcicZ045LogId(jcicZ045Log.getJcicZ045LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ045LogT = jcicZ045LogReposHist.findByJcicZ045LogId(jcicZ045Log.getJcicZ045LogId());
    else 
      jcicZ045LogT = jcicZ045LogRepos.findByJcicZ045LogId(jcicZ045Log.getJcicZ045LogId());
    return jcicZ045LogT.isPresent() ? jcicZ045LogT.get() : null;
  }

  @Override
  public JcicZ045Log insert(JcicZ045Log jcicZ045Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ045Log.getJcicZ045LogId());
    if (this.findById(jcicZ045Log.getJcicZ045LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ045Log.setCreateEmpNo(empNot);

    if(jcicZ045Log.getLastUpdateEmpNo() == null || jcicZ045Log.getLastUpdateEmpNo().isEmpty())
      jcicZ045Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ045LogReposDay.saveAndFlush(jcicZ045Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ045LogReposMon.saveAndFlush(jcicZ045Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ045LogReposHist.saveAndFlush(jcicZ045Log);
    else 
    return jcicZ045LogRepos.saveAndFlush(jcicZ045Log);
  }

  @Override
  public JcicZ045Log update(JcicZ045Log jcicZ045Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ045Log.getJcicZ045LogId());
    if (!empNot.isEmpty())
      jcicZ045Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ045LogReposDay.saveAndFlush(jcicZ045Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ045LogReposMon.saveAndFlush(jcicZ045Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ045LogReposHist.saveAndFlush(jcicZ045Log);
    else 
    return jcicZ045LogRepos.saveAndFlush(jcicZ045Log);
  }

  @Override
  public JcicZ045Log update2(JcicZ045Log jcicZ045Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ045Log.getJcicZ045LogId());
    if (!empNot.isEmpty())
      jcicZ045Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ045LogReposDay.saveAndFlush(jcicZ045Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ045LogReposMon.saveAndFlush(jcicZ045Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ045LogReposHist.saveAndFlush(jcicZ045Log);
    else 
      jcicZ045LogRepos.saveAndFlush(jcicZ045Log);	
    return this.findById(jcicZ045Log.getJcicZ045LogId());
  }

  @Override
  public void delete(JcicZ045Log jcicZ045Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ045Log.getJcicZ045LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ045LogReposDay.delete(jcicZ045Log);	
      jcicZ045LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ045LogReposMon.delete(jcicZ045Log);	
      jcicZ045LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ045LogReposHist.delete(jcicZ045Log);
      jcicZ045LogReposHist.flush();
    }
    else {
      jcicZ045LogRepos.delete(jcicZ045Log);
      jcicZ045LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ045Log> jcicZ045Log, TitaVo... titaVo) throws DBException {
    if (jcicZ045Log == null || jcicZ045Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ045Log t : jcicZ045Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ045Log = jcicZ045LogReposDay.saveAll(jcicZ045Log);	
      jcicZ045LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ045Log = jcicZ045LogReposMon.saveAll(jcicZ045Log);	
      jcicZ045LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ045Log = jcicZ045LogReposHist.saveAll(jcicZ045Log);
      jcicZ045LogReposHist.flush();
    }
    else {
      jcicZ045Log = jcicZ045LogRepos.saveAll(jcicZ045Log);
      jcicZ045LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ045Log> jcicZ045Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ045Log == null || jcicZ045Log.size() == 0)
      throw new DBException(6);

    for (JcicZ045Log t : jcicZ045Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ045Log = jcicZ045LogReposDay.saveAll(jcicZ045Log);	
      jcicZ045LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ045Log = jcicZ045LogReposMon.saveAll(jcicZ045Log);	
      jcicZ045LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ045Log = jcicZ045LogReposHist.saveAll(jcicZ045Log);
      jcicZ045LogReposHist.flush();
    }
    else {
      jcicZ045Log = jcicZ045LogRepos.saveAll(jcicZ045Log);
      jcicZ045LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ045Log> jcicZ045Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ045Log == null || jcicZ045Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ045LogReposDay.deleteAll(jcicZ045Log);	
      jcicZ045LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ045LogReposMon.deleteAll(jcicZ045Log);	
      jcicZ045LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ045LogReposHist.deleteAll(jcicZ045Log);
      jcicZ045LogReposHist.flush();
    }
    else {
      jcicZ045LogRepos.deleteAll(jcicZ045Log);
      jcicZ045LogRepos.flush();
    }
  }

}
