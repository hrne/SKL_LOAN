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
import com.st1.itx.db.domain.JcicZ060Log;
import com.st1.itx.db.domain.JcicZ060LogId;
import com.st1.itx.db.repository.online.JcicZ060LogRepository;
import com.st1.itx.db.repository.day.JcicZ060LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ060LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ060LogRepositoryHist;
import com.st1.itx.db.service.JcicZ060LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ060LogService")
@Repository
public class JcicZ060LogServiceImpl extends ASpringJpaParm implements JcicZ060LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ060LogRepository jcicZ060LogRepos;

  @Autowired
  private JcicZ060LogRepositoryDay jcicZ060LogReposDay;

  @Autowired
  private JcicZ060LogRepositoryMon jcicZ060LogReposMon;

  @Autowired
  private JcicZ060LogRepositoryHist jcicZ060LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ060LogRepos);
    org.junit.Assert.assertNotNull(jcicZ060LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ060LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ060LogReposHist);
  }

  @Override
  public JcicZ060Log findById(JcicZ060LogId jcicZ060LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ060LogId);
    Optional<JcicZ060Log> jcicZ060Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ060Log = jcicZ060LogReposDay.findById(jcicZ060LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ060Log = jcicZ060LogReposMon.findById(jcicZ060LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ060Log = jcicZ060LogReposHist.findById(jcicZ060LogId);
    else 
      jcicZ060Log = jcicZ060LogRepos.findById(jcicZ060LogId);
    JcicZ060Log obj = jcicZ060Log.isPresent() ? jcicZ060Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ060Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ060Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ060LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ060LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ060LogReposHist.findAll(pageable);
    else 
      slice = jcicZ060LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ060Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ060Log> jcicZ060LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ060LogT = jcicZ060LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ060LogT = jcicZ060LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ060LogT = jcicZ060LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ060LogT = jcicZ060LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ060LogT.isPresent() ? jcicZ060LogT.get() : null;
  }

  @Override
  public Slice<JcicZ060Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ060Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ060LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ060LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ060LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ060LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ060Log holdById(JcicZ060LogId jcicZ060LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ060LogId);
    Optional<JcicZ060Log> jcicZ060Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ060Log = jcicZ060LogReposDay.findByJcicZ060LogId(jcicZ060LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ060Log = jcicZ060LogReposMon.findByJcicZ060LogId(jcicZ060LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ060Log = jcicZ060LogReposHist.findByJcicZ060LogId(jcicZ060LogId);
    else 
      jcicZ060Log = jcicZ060LogRepos.findByJcicZ060LogId(jcicZ060LogId);
    return jcicZ060Log.isPresent() ? jcicZ060Log.get() : null;
  }

  @Override
  public JcicZ060Log holdById(JcicZ060Log jcicZ060Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ060Log.getJcicZ060LogId());
    Optional<JcicZ060Log> jcicZ060LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ060LogT = jcicZ060LogReposDay.findByJcicZ060LogId(jcicZ060Log.getJcicZ060LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ060LogT = jcicZ060LogReposMon.findByJcicZ060LogId(jcicZ060Log.getJcicZ060LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ060LogT = jcicZ060LogReposHist.findByJcicZ060LogId(jcicZ060Log.getJcicZ060LogId());
    else 
      jcicZ060LogT = jcicZ060LogRepos.findByJcicZ060LogId(jcicZ060Log.getJcicZ060LogId());
    return jcicZ060LogT.isPresent() ? jcicZ060LogT.get() : null;
  }

  @Override
  public JcicZ060Log insert(JcicZ060Log jcicZ060Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ060Log.getJcicZ060LogId());
    if (this.findById(jcicZ060Log.getJcicZ060LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ060Log.setCreateEmpNo(empNot);

    if(jcicZ060Log.getLastUpdateEmpNo() == null || jcicZ060Log.getLastUpdateEmpNo().isEmpty())
      jcicZ060Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ060LogReposDay.saveAndFlush(jcicZ060Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ060LogReposMon.saveAndFlush(jcicZ060Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ060LogReposHist.saveAndFlush(jcicZ060Log);
    else 
    return jcicZ060LogRepos.saveAndFlush(jcicZ060Log);
  }

  @Override
  public JcicZ060Log update(JcicZ060Log jcicZ060Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ060Log.getJcicZ060LogId());
    if (!empNot.isEmpty())
      jcicZ060Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ060LogReposDay.saveAndFlush(jcicZ060Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ060LogReposMon.saveAndFlush(jcicZ060Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ060LogReposHist.saveAndFlush(jcicZ060Log);
    else 
    return jcicZ060LogRepos.saveAndFlush(jcicZ060Log);
  }

  @Override
  public JcicZ060Log update2(JcicZ060Log jcicZ060Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ060Log.getJcicZ060LogId());
    if (!empNot.isEmpty())
      jcicZ060Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ060LogReposDay.saveAndFlush(jcicZ060Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ060LogReposMon.saveAndFlush(jcicZ060Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ060LogReposHist.saveAndFlush(jcicZ060Log);
    else 
      jcicZ060LogRepos.saveAndFlush(jcicZ060Log);	
    return this.findById(jcicZ060Log.getJcicZ060LogId());
  }

  @Override
  public void delete(JcicZ060Log jcicZ060Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ060Log.getJcicZ060LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ060LogReposDay.delete(jcicZ060Log);	
      jcicZ060LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ060LogReposMon.delete(jcicZ060Log);	
      jcicZ060LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ060LogReposHist.delete(jcicZ060Log);
      jcicZ060LogReposHist.flush();
    }
    else {
      jcicZ060LogRepos.delete(jcicZ060Log);
      jcicZ060LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ060Log> jcicZ060Log, TitaVo... titaVo) throws DBException {
    if (jcicZ060Log == null || jcicZ060Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ060Log t : jcicZ060Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ060Log = jcicZ060LogReposDay.saveAll(jcicZ060Log);	
      jcicZ060LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ060Log = jcicZ060LogReposMon.saveAll(jcicZ060Log);	
      jcicZ060LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ060Log = jcicZ060LogReposHist.saveAll(jcicZ060Log);
      jcicZ060LogReposHist.flush();
    }
    else {
      jcicZ060Log = jcicZ060LogRepos.saveAll(jcicZ060Log);
      jcicZ060LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ060Log> jcicZ060Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ060Log == null || jcicZ060Log.size() == 0)
      throw new DBException(6);

    for (JcicZ060Log t : jcicZ060Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ060Log = jcicZ060LogReposDay.saveAll(jcicZ060Log);	
      jcicZ060LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ060Log = jcicZ060LogReposMon.saveAll(jcicZ060Log);	
      jcicZ060LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ060Log = jcicZ060LogReposHist.saveAll(jcicZ060Log);
      jcicZ060LogReposHist.flush();
    }
    else {
      jcicZ060Log = jcicZ060LogRepos.saveAll(jcicZ060Log);
      jcicZ060LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ060Log> jcicZ060Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ060Log == null || jcicZ060Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ060LogReposDay.deleteAll(jcicZ060Log);	
      jcicZ060LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ060LogReposMon.deleteAll(jcicZ060Log);	
      jcicZ060LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ060LogReposHist.deleteAll(jcicZ060Log);
      jcicZ060LogReposHist.flush();
    }
    else {
      jcicZ060LogRepos.deleteAll(jcicZ060Log);
      jcicZ060LogRepos.flush();
    }
  }

}
