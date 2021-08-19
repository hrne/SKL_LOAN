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
import com.st1.itx.db.domain.JcicZ048Log;
import com.st1.itx.db.domain.JcicZ048LogId;
import com.st1.itx.db.repository.online.JcicZ048LogRepository;
import com.st1.itx.db.repository.day.JcicZ048LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ048LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ048LogRepositoryHist;
import com.st1.itx.db.service.JcicZ048LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ048LogService")
@Repository
public class JcicZ048LogServiceImpl extends ASpringJpaParm implements JcicZ048LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ048LogRepository jcicZ048LogRepos;

  @Autowired
  private JcicZ048LogRepositoryDay jcicZ048LogReposDay;

  @Autowired
  private JcicZ048LogRepositoryMon jcicZ048LogReposMon;

  @Autowired
  private JcicZ048LogRepositoryHist jcicZ048LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ048LogRepos);
    org.junit.Assert.assertNotNull(jcicZ048LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ048LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ048LogReposHist);
  }

  @Override
  public JcicZ048Log findById(JcicZ048LogId jcicZ048LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ048LogId);
    Optional<JcicZ048Log> jcicZ048Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ048Log = jcicZ048LogReposDay.findById(jcicZ048LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ048Log = jcicZ048LogReposMon.findById(jcicZ048LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ048Log = jcicZ048LogReposHist.findById(jcicZ048LogId);
    else 
      jcicZ048Log = jcicZ048LogRepos.findById(jcicZ048LogId);
    JcicZ048Log obj = jcicZ048Log.isPresent() ? jcicZ048Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ048Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ048Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ048LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ048LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ048LogReposHist.findAll(pageable);
    else 
      slice = jcicZ048LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ048Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ048Log> jcicZ048LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ048LogT = jcicZ048LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ048LogT = jcicZ048LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ048LogT = jcicZ048LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ048LogT = jcicZ048LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ048LogT.isPresent() ? jcicZ048LogT.get() : null;
  }

  @Override
  public Slice<JcicZ048Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ048Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ048LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ048LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ048LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ048LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ048Log holdById(JcicZ048LogId jcicZ048LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ048LogId);
    Optional<JcicZ048Log> jcicZ048Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ048Log = jcicZ048LogReposDay.findByJcicZ048LogId(jcicZ048LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ048Log = jcicZ048LogReposMon.findByJcicZ048LogId(jcicZ048LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ048Log = jcicZ048LogReposHist.findByJcicZ048LogId(jcicZ048LogId);
    else 
      jcicZ048Log = jcicZ048LogRepos.findByJcicZ048LogId(jcicZ048LogId);
    return jcicZ048Log.isPresent() ? jcicZ048Log.get() : null;
  }

  @Override
  public JcicZ048Log holdById(JcicZ048Log jcicZ048Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ048Log.getJcicZ048LogId());
    Optional<JcicZ048Log> jcicZ048LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ048LogT = jcicZ048LogReposDay.findByJcicZ048LogId(jcicZ048Log.getJcicZ048LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ048LogT = jcicZ048LogReposMon.findByJcicZ048LogId(jcicZ048Log.getJcicZ048LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ048LogT = jcicZ048LogReposHist.findByJcicZ048LogId(jcicZ048Log.getJcicZ048LogId());
    else 
      jcicZ048LogT = jcicZ048LogRepos.findByJcicZ048LogId(jcicZ048Log.getJcicZ048LogId());
    return jcicZ048LogT.isPresent() ? jcicZ048LogT.get() : null;
  }

  @Override
  public JcicZ048Log insert(JcicZ048Log jcicZ048Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ048Log.getJcicZ048LogId());
    if (this.findById(jcicZ048Log.getJcicZ048LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ048Log.setCreateEmpNo(empNot);

    if(jcicZ048Log.getLastUpdateEmpNo() == null || jcicZ048Log.getLastUpdateEmpNo().isEmpty())
      jcicZ048Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ048LogReposDay.saveAndFlush(jcicZ048Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ048LogReposMon.saveAndFlush(jcicZ048Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ048LogReposHist.saveAndFlush(jcicZ048Log);
    else 
    return jcicZ048LogRepos.saveAndFlush(jcicZ048Log);
  }

  @Override
  public JcicZ048Log update(JcicZ048Log jcicZ048Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ048Log.getJcicZ048LogId());
    if (!empNot.isEmpty())
      jcicZ048Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ048LogReposDay.saveAndFlush(jcicZ048Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ048LogReposMon.saveAndFlush(jcicZ048Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ048LogReposHist.saveAndFlush(jcicZ048Log);
    else 
    return jcicZ048LogRepos.saveAndFlush(jcicZ048Log);
  }

  @Override
  public JcicZ048Log update2(JcicZ048Log jcicZ048Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ048Log.getJcicZ048LogId());
    if (!empNot.isEmpty())
      jcicZ048Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ048LogReposDay.saveAndFlush(jcicZ048Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ048LogReposMon.saveAndFlush(jcicZ048Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ048LogReposHist.saveAndFlush(jcicZ048Log);
    else 
      jcicZ048LogRepos.saveAndFlush(jcicZ048Log);	
    return this.findById(jcicZ048Log.getJcicZ048LogId());
  }

  @Override
  public void delete(JcicZ048Log jcicZ048Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ048Log.getJcicZ048LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ048LogReposDay.delete(jcicZ048Log);	
      jcicZ048LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ048LogReposMon.delete(jcicZ048Log);	
      jcicZ048LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ048LogReposHist.delete(jcicZ048Log);
      jcicZ048LogReposHist.flush();
    }
    else {
      jcicZ048LogRepos.delete(jcicZ048Log);
      jcicZ048LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ048Log> jcicZ048Log, TitaVo... titaVo) throws DBException {
    if (jcicZ048Log == null || jcicZ048Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ048Log t : jcicZ048Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ048Log = jcicZ048LogReposDay.saveAll(jcicZ048Log);	
      jcicZ048LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ048Log = jcicZ048LogReposMon.saveAll(jcicZ048Log);	
      jcicZ048LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ048Log = jcicZ048LogReposHist.saveAll(jcicZ048Log);
      jcicZ048LogReposHist.flush();
    }
    else {
      jcicZ048Log = jcicZ048LogRepos.saveAll(jcicZ048Log);
      jcicZ048LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ048Log> jcicZ048Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ048Log == null || jcicZ048Log.size() == 0)
      throw new DBException(6);

    for (JcicZ048Log t : jcicZ048Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ048Log = jcicZ048LogReposDay.saveAll(jcicZ048Log);	
      jcicZ048LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ048Log = jcicZ048LogReposMon.saveAll(jcicZ048Log);	
      jcicZ048LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ048Log = jcicZ048LogReposHist.saveAll(jcicZ048Log);
      jcicZ048LogReposHist.flush();
    }
    else {
      jcicZ048Log = jcicZ048LogRepos.saveAll(jcicZ048Log);
      jcicZ048LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ048Log> jcicZ048Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ048Log == null || jcicZ048Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ048LogReposDay.deleteAll(jcicZ048Log);	
      jcicZ048LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ048LogReposMon.deleteAll(jcicZ048Log);	
      jcicZ048LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ048LogReposHist.deleteAll(jcicZ048Log);
      jcicZ048LogReposHist.flush();
    }
    else {
      jcicZ048LogRepos.deleteAll(jcicZ048Log);
      jcicZ048LogRepos.flush();
    }
  }

}
