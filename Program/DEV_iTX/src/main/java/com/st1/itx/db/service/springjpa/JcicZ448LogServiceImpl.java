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
import com.st1.itx.db.domain.JcicZ448Log;
import com.st1.itx.db.domain.JcicZ448LogId;
import com.st1.itx.db.repository.online.JcicZ448LogRepository;
import com.st1.itx.db.repository.day.JcicZ448LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ448LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ448LogRepositoryHist;
import com.st1.itx.db.service.JcicZ448LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ448LogService")
@Repository
public class JcicZ448LogServiceImpl extends ASpringJpaParm implements JcicZ448LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ448LogRepository jcicZ448LogRepos;

  @Autowired
  private JcicZ448LogRepositoryDay jcicZ448LogReposDay;

  @Autowired
  private JcicZ448LogRepositoryMon jcicZ448LogReposMon;

  @Autowired
  private JcicZ448LogRepositoryHist jcicZ448LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ448LogRepos);
    org.junit.Assert.assertNotNull(jcicZ448LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ448LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ448LogReposHist);
  }

  @Override
  public JcicZ448Log findById(JcicZ448LogId jcicZ448LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ448LogId);
    Optional<JcicZ448Log> jcicZ448Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ448Log = jcicZ448LogReposDay.findById(jcicZ448LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ448Log = jcicZ448LogReposMon.findById(jcicZ448LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ448Log = jcicZ448LogReposHist.findById(jcicZ448LogId);
    else 
      jcicZ448Log = jcicZ448LogRepos.findById(jcicZ448LogId);
    JcicZ448Log obj = jcicZ448Log.isPresent() ? jcicZ448Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ448Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ448Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ448LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ448LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ448LogReposHist.findAll(pageable);
    else 
      slice = jcicZ448LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ448Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ448Log> jcicZ448LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ448LogT = jcicZ448LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ448LogT = jcicZ448LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ448LogT = jcicZ448LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ448LogT = jcicZ448LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ448LogT.isPresent() ? jcicZ448LogT.get() : null;
  }

  @Override
  public Slice<JcicZ448Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ448Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ448LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ448LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ448LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ448LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ448Log holdById(JcicZ448LogId jcicZ448LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ448LogId);
    Optional<JcicZ448Log> jcicZ448Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ448Log = jcicZ448LogReposDay.findByJcicZ448LogId(jcicZ448LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ448Log = jcicZ448LogReposMon.findByJcicZ448LogId(jcicZ448LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ448Log = jcicZ448LogReposHist.findByJcicZ448LogId(jcicZ448LogId);
    else 
      jcicZ448Log = jcicZ448LogRepos.findByJcicZ448LogId(jcicZ448LogId);
    return jcicZ448Log.isPresent() ? jcicZ448Log.get() : null;
  }

  @Override
  public JcicZ448Log holdById(JcicZ448Log jcicZ448Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ448Log.getJcicZ448LogId());
    Optional<JcicZ448Log> jcicZ448LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ448LogT = jcicZ448LogReposDay.findByJcicZ448LogId(jcicZ448Log.getJcicZ448LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ448LogT = jcicZ448LogReposMon.findByJcicZ448LogId(jcicZ448Log.getJcicZ448LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ448LogT = jcicZ448LogReposHist.findByJcicZ448LogId(jcicZ448Log.getJcicZ448LogId());
    else 
      jcicZ448LogT = jcicZ448LogRepos.findByJcicZ448LogId(jcicZ448Log.getJcicZ448LogId());
    return jcicZ448LogT.isPresent() ? jcicZ448LogT.get() : null;
  }

  @Override
  public JcicZ448Log insert(JcicZ448Log jcicZ448Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ448Log.getJcicZ448LogId());
    if (this.findById(jcicZ448Log.getJcicZ448LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ448Log.setCreateEmpNo(empNot);

    if(jcicZ448Log.getLastUpdateEmpNo() == null || jcicZ448Log.getLastUpdateEmpNo().isEmpty())
      jcicZ448Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ448LogReposDay.saveAndFlush(jcicZ448Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ448LogReposMon.saveAndFlush(jcicZ448Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ448LogReposHist.saveAndFlush(jcicZ448Log);
    else 
    return jcicZ448LogRepos.saveAndFlush(jcicZ448Log);
  }

  @Override
  public JcicZ448Log update(JcicZ448Log jcicZ448Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ448Log.getJcicZ448LogId());
    if (!empNot.isEmpty())
      jcicZ448Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ448LogReposDay.saveAndFlush(jcicZ448Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ448LogReposMon.saveAndFlush(jcicZ448Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ448LogReposHist.saveAndFlush(jcicZ448Log);
    else 
    return jcicZ448LogRepos.saveAndFlush(jcicZ448Log);
  }

  @Override
  public JcicZ448Log update2(JcicZ448Log jcicZ448Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ448Log.getJcicZ448LogId());
    if (!empNot.isEmpty())
      jcicZ448Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ448LogReposDay.saveAndFlush(jcicZ448Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ448LogReposMon.saveAndFlush(jcicZ448Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ448LogReposHist.saveAndFlush(jcicZ448Log);
    else 
      jcicZ448LogRepos.saveAndFlush(jcicZ448Log);	
    return this.findById(jcicZ448Log.getJcicZ448LogId());
  }

  @Override
  public void delete(JcicZ448Log jcicZ448Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ448Log.getJcicZ448LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ448LogReposDay.delete(jcicZ448Log);	
      jcicZ448LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ448LogReposMon.delete(jcicZ448Log);	
      jcicZ448LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ448LogReposHist.delete(jcicZ448Log);
      jcicZ448LogReposHist.flush();
    }
    else {
      jcicZ448LogRepos.delete(jcicZ448Log);
      jcicZ448LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ448Log> jcicZ448Log, TitaVo... titaVo) throws DBException {
    if (jcicZ448Log == null || jcicZ448Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ448Log t : jcicZ448Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ448Log = jcicZ448LogReposDay.saveAll(jcicZ448Log);	
      jcicZ448LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ448Log = jcicZ448LogReposMon.saveAll(jcicZ448Log);	
      jcicZ448LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ448Log = jcicZ448LogReposHist.saveAll(jcicZ448Log);
      jcicZ448LogReposHist.flush();
    }
    else {
      jcicZ448Log = jcicZ448LogRepos.saveAll(jcicZ448Log);
      jcicZ448LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ448Log> jcicZ448Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ448Log == null || jcicZ448Log.size() == 0)
      throw new DBException(6);

    for (JcicZ448Log t : jcicZ448Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ448Log = jcicZ448LogReposDay.saveAll(jcicZ448Log);	
      jcicZ448LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ448Log = jcicZ448LogReposMon.saveAll(jcicZ448Log);	
      jcicZ448LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ448Log = jcicZ448LogReposHist.saveAll(jcicZ448Log);
      jcicZ448LogReposHist.flush();
    }
    else {
      jcicZ448Log = jcicZ448LogRepos.saveAll(jcicZ448Log);
      jcicZ448LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ448Log> jcicZ448Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ448Log == null || jcicZ448Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ448LogReposDay.deleteAll(jcicZ448Log);	
      jcicZ448LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ448LogReposMon.deleteAll(jcicZ448Log);	
      jcicZ448LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ448LogReposHist.deleteAll(jcicZ448Log);
      jcicZ448LogReposHist.flush();
    }
    else {
      jcicZ448LogRepos.deleteAll(jcicZ448Log);
      jcicZ448LogRepos.flush();
    }
  }

}
