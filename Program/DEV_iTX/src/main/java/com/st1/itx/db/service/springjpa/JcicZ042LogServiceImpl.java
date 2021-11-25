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
import com.st1.itx.db.domain.JcicZ042Log;
import com.st1.itx.db.domain.JcicZ042LogId;
import com.st1.itx.db.repository.online.JcicZ042LogRepository;
import com.st1.itx.db.repository.day.JcicZ042LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ042LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ042LogRepositoryHist;
import com.st1.itx.db.service.JcicZ042LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ042LogService")
@Repository
public class JcicZ042LogServiceImpl extends ASpringJpaParm implements JcicZ042LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ042LogRepository jcicZ042LogRepos;

  @Autowired
  private JcicZ042LogRepositoryDay jcicZ042LogReposDay;

  @Autowired
  private JcicZ042LogRepositoryMon jcicZ042LogReposMon;

  @Autowired
  private JcicZ042LogRepositoryHist jcicZ042LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ042LogRepos);
    org.junit.Assert.assertNotNull(jcicZ042LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ042LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ042LogReposHist);
  }

  @Override
  public JcicZ042Log findById(JcicZ042LogId jcicZ042LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ042LogId);
    Optional<JcicZ042Log> jcicZ042Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ042Log = jcicZ042LogReposDay.findById(jcicZ042LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ042Log = jcicZ042LogReposMon.findById(jcicZ042LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ042Log = jcicZ042LogReposHist.findById(jcicZ042LogId);
    else 
      jcicZ042Log = jcicZ042LogRepos.findById(jcicZ042LogId);
    JcicZ042Log obj = jcicZ042Log.isPresent() ? jcicZ042Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ042Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042LogReposHist.findAll(pageable);
    else 
      slice = jcicZ042LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ042Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ042Log> jcicZ042LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ042LogT = jcicZ042LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ042LogT = jcicZ042LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ042LogT = jcicZ042LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ042LogT = jcicZ042LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ042LogT.isPresent() ? jcicZ042LogT.get() : null;
  }

  @Override
  public Slice<JcicZ042Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ042Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ042LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ042LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ042LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ042LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ042Log holdById(JcicZ042LogId jcicZ042LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ042LogId);
    Optional<JcicZ042Log> jcicZ042Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ042Log = jcicZ042LogReposDay.findByJcicZ042LogId(jcicZ042LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ042Log = jcicZ042LogReposMon.findByJcicZ042LogId(jcicZ042LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ042Log = jcicZ042LogReposHist.findByJcicZ042LogId(jcicZ042LogId);
    else 
      jcicZ042Log = jcicZ042LogRepos.findByJcicZ042LogId(jcicZ042LogId);
    return jcicZ042Log.isPresent() ? jcicZ042Log.get() : null;
  }

  @Override
  public JcicZ042Log holdById(JcicZ042Log jcicZ042Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ042Log.getJcicZ042LogId());
    Optional<JcicZ042Log> jcicZ042LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ042LogT = jcicZ042LogReposDay.findByJcicZ042LogId(jcicZ042Log.getJcicZ042LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ042LogT = jcicZ042LogReposMon.findByJcicZ042LogId(jcicZ042Log.getJcicZ042LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ042LogT = jcicZ042LogReposHist.findByJcicZ042LogId(jcicZ042Log.getJcicZ042LogId());
    else 
      jcicZ042LogT = jcicZ042LogRepos.findByJcicZ042LogId(jcicZ042Log.getJcicZ042LogId());
    return jcicZ042LogT.isPresent() ? jcicZ042LogT.get() : null;
  }

  @Override
  public JcicZ042Log insert(JcicZ042Log jcicZ042Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ042Log.getJcicZ042LogId());
    if (this.findById(jcicZ042Log.getJcicZ042LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ042Log.setCreateEmpNo(empNot);

    if(jcicZ042Log.getLastUpdateEmpNo() == null || jcicZ042Log.getLastUpdateEmpNo().isEmpty())
      jcicZ042Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ042LogReposDay.saveAndFlush(jcicZ042Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ042LogReposMon.saveAndFlush(jcicZ042Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ042LogReposHist.saveAndFlush(jcicZ042Log);
    else 
    return jcicZ042LogRepos.saveAndFlush(jcicZ042Log);
  }

  @Override
  public JcicZ042Log update(JcicZ042Log jcicZ042Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ042Log.getJcicZ042LogId());
    if (!empNot.isEmpty())
      jcicZ042Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ042LogReposDay.saveAndFlush(jcicZ042Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ042LogReposMon.saveAndFlush(jcicZ042Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ042LogReposHist.saveAndFlush(jcicZ042Log);
    else 
    return jcicZ042LogRepos.saveAndFlush(jcicZ042Log);
  }

  @Override
  public JcicZ042Log update2(JcicZ042Log jcicZ042Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ042Log.getJcicZ042LogId());
    if (!empNot.isEmpty())
      jcicZ042Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ042LogReposDay.saveAndFlush(jcicZ042Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ042LogReposMon.saveAndFlush(jcicZ042Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ042LogReposHist.saveAndFlush(jcicZ042Log);
    else 
      jcicZ042LogRepos.saveAndFlush(jcicZ042Log);	
    return this.findById(jcicZ042Log.getJcicZ042LogId());
  }

  @Override
  public void delete(JcicZ042Log jcicZ042Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ042Log.getJcicZ042LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ042LogReposDay.delete(jcicZ042Log);	
      jcicZ042LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ042LogReposMon.delete(jcicZ042Log);	
      jcicZ042LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ042LogReposHist.delete(jcicZ042Log);
      jcicZ042LogReposHist.flush();
    }
    else {
      jcicZ042LogRepos.delete(jcicZ042Log);
      jcicZ042LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ042Log> jcicZ042Log, TitaVo... titaVo) throws DBException {
    if (jcicZ042Log == null || jcicZ042Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ042Log t : jcicZ042Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ042Log = jcicZ042LogReposDay.saveAll(jcicZ042Log);	
      jcicZ042LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ042Log = jcicZ042LogReposMon.saveAll(jcicZ042Log);	
      jcicZ042LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ042Log = jcicZ042LogReposHist.saveAll(jcicZ042Log);
      jcicZ042LogReposHist.flush();
    }
    else {
      jcicZ042Log = jcicZ042LogRepos.saveAll(jcicZ042Log);
      jcicZ042LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ042Log> jcicZ042Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ042Log == null || jcicZ042Log.size() == 0)
      throw new DBException(6);

    for (JcicZ042Log t : jcicZ042Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ042Log = jcicZ042LogReposDay.saveAll(jcicZ042Log);	
      jcicZ042LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ042Log = jcicZ042LogReposMon.saveAll(jcicZ042Log);	
      jcicZ042LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ042Log = jcicZ042LogReposHist.saveAll(jcicZ042Log);
      jcicZ042LogReposHist.flush();
    }
    else {
      jcicZ042Log = jcicZ042LogRepos.saveAll(jcicZ042Log);
      jcicZ042LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ042Log> jcicZ042Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ042Log == null || jcicZ042Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ042LogReposDay.deleteAll(jcicZ042Log);	
      jcicZ042LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ042LogReposMon.deleteAll(jcicZ042Log);	
      jcicZ042LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ042LogReposHist.deleteAll(jcicZ042Log);
      jcicZ042LogReposHist.flush();
    }
    else {
      jcicZ042LogRepos.deleteAll(jcicZ042Log);
      jcicZ042LogRepos.flush();
    }
  }

}
