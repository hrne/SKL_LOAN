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
import com.st1.itx.db.domain.JcicZ571Log;
import com.st1.itx.db.domain.JcicZ571LogId;
import com.st1.itx.db.repository.online.JcicZ571LogRepository;
import com.st1.itx.db.repository.day.JcicZ571LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ571LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ571LogRepositoryHist;
import com.st1.itx.db.service.JcicZ571LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ571LogService")
@Repository
public class JcicZ571LogServiceImpl extends ASpringJpaParm implements JcicZ571LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ571LogRepository jcicZ571LogRepos;

  @Autowired
  private JcicZ571LogRepositoryDay jcicZ571LogReposDay;

  @Autowired
  private JcicZ571LogRepositoryMon jcicZ571LogReposMon;

  @Autowired
  private JcicZ571LogRepositoryHist jcicZ571LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ571LogRepos);
    org.junit.Assert.assertNotNull(jcicZ571LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ571LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ571LogReposHist);
  }

  @Override
  public JcicZ571Log findById(JcicZ571LogId jcicZ571LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ571LogId);
    Optional<JcicZ571Log> jcicZ571Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ571Log = jcicZ571LogReposDay.findById(jcicZ571LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ571Log = jcicZ571LogReposMon.findById(jcicZ571LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ571Log = jcicZ571LogReposHist.findById(jcicZ571LogId);
    else 
      jcicZ571Log = jcicZ571LogRepos.findById(jcicZ571LogId);
    JcicZ571Log obj = jcicZ571Log.isPresent() ? jcicZ571Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ571Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ571Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ571LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ571LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ571LogReposHist.findAll(pageable);
    else 
      slice = jcicZ571LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ571Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ571Log> jcicZ571LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ571LogT = jcicZ571LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ571LogT = jcicZ571LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ571LogT = jcicZ571LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ571LogT = jcicZ571LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ571LogT.isPresent() ? jcicZ571LogT.get() : null;
  }

  @Override
  public Slice<JcicZ571Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ571Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ571LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ571LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ571LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ571LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ571Log holdById(JcicZ571LogId jcicZ571LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ571LogId);
    Optional<JcicZ571Log> jcicZ571Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ571Log = jcicZ571LogReposDay.findByJcicZ571LogId(jcicZ571LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ571Log = jcicZ571LogReposMon.findByJcicZ571LogId(jcicZ571LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ571Log = jcicZ571LogReposHist.findByJcicZ571LogId(jcicZ571LogId);
    else 
      jcicZ571Log = jcicZ571LogRepos.findByJcicZ571LogId(jcicZ571LogId);
    return jcicZ571Log.isPresent() ? jcicZ571Log.get() : null;
  }

  @Override
  public JcicZ571Log holdById(JcicZ571Log jcicZ571Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ571Log.getJcicZ571LogId());
    Optional<JcicZ571Log> jcicZ571LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ571LogT = jcicZ571LogReposDay.findByJcicZ571LogId(jcicZ571Log.getJcicZ571LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ571LogT = jcicZ571LogReposMon.findByJcicZ571LogId(jcicZ571Log.getJcicZ571LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ571LogT = jcicZ571LogReposHist.findByJcicZ571LogId(jcicZ571Log.getJcicZ571LogId());
    else 
      jcicZ571LogT = jcicZ571LogRepos.findByJcicZ571LogId(jcicZ571Log.getJcicZ571LogId());
    return jcicZ571LogT.isPresent() ? jcicZ571LogT.get() : null;
  }

  @Override
  public JcicZ571Log insert(JcicZ571Log jcicZ571Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ571Log.getJcicZ571LogId());
    if (this.findById(jcicZ571Log.getJcicZ571LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ571Log.setCreateEmpNo(empNot);

    if(jcicZ571Log.getLastUpdateEmpNo() == null || jcicZ571Log.getLastUpdateEmpNo().isEmpty())
      jcicZ571Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ571LogReposDay.saveAndFlush(jcicZ571Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ571LogReposMon.saveAndFlush(jcicZ571Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ571LogReposHist.saveAndFlush(jcicZ571Log);
    else 
    return jcicZ571LogRepos.saveAndFlush(jcicZ571Log);
  }

  @Override
  public JcicZ571Log update(JcicZ571Log jcicZ571Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ571Log.getJcicZ571LogId());
    if (!empNot.isEmpty())
      jcicZ571Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ571LogReposDay.saveAndFlush(jcicZ571Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ571LogReposMon.saveAndFlush(jcicZ571Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ571LogReposHist.saveAndFlush(jcicZ571Log);
    else 
    return jcicZ571LogRepos.saveAndFlush(jcicZ571Log);
  }

  @Override
  public JcicZ571Log update2(JcicZ571Log jcicZ571Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ571Log.getJcicZ571LogId());
    if (!empNot.isEmpty())
      jcicZ571Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ571LogReposDay.saveAndFlush(jcicZ571Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ571LogReposMon.saveAndFlush(jcicZ571Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ571LogReposHist.saveAndFlush(jcicZ571Log);
    else 
      jcicZ571LogRepos.saveAndFlush(jcicZ571Log);	
    return this.findById(jcicZ571Log.getJcicZ571LogId());
  }

  @Override
  public void delete(JcicZ571Log jcicZ571Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ571Log.getJcicZ571LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ571LogReposDay.delete(jcicZ571Log);	
      jcicZ571LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ571LogReposMon.delete(jcicZ571Log);	
      jcicZ571LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ571LogReposHist.delete(jcicZ571Log);
      jcicZ571LogReposHist.flush();
    }
    else {
      jcicZ571LogRepos.delete(jcicZ571Log);
      jcicZ571LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ571Log> jcicZ571Log, TitaVo... titaVo) throws DBException {
    if (jcicZ571Log == null || jcicZ571Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ571Log t : jcicZ571Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ571Log = jcicZ571LogReposDay.saveAll(jcicZ571Log);	
      jcicZ571LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ571Log = jcicZ571LogReposMon.saveAll(jcicZ571Log);	
      jcicZ571LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ571Log = jcicZ571LogReposHist.saveAll(jcicZ571Log);
      jcicZ571LogReposHist.flush();
    }
    else {
      jcicZ571Log = jcicZ571LogRepos.saveAll(jcicZ571Log);
      jcicZ571LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ571Log> jcicZ571Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ571Log == null || jcicZ571Log.size() == 0)
      throw new DBException(6);

    for (JcicZ571Log t : jcicZ571Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ571Log = jcicZ571LogReposDay.saveAll(jcicZ571Log);	
      jcicZ571LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ571Log = jcicZ571LogReposMon.saveAll(jcicZ571Log);	
      jcicZ571LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ571Log = jcicZ571LogReposHist.saveAll(jcicZ571Log);
      jcicZ571LogReposHist.flush();
    }
    else {
      jcicZ571Log = jcicZ571LogRepos.saveAll(jcicZ571Log);
      jcicZ571LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ571Log> jcicZ571Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ571Log == null || jcicZ571Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ571LogReposDay.deleteAll(jcicZ571Log);	
      jcicZ571LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ571LogReposMon.deleteAll(jcicZ571Log);	
      jcicZ571LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ571LogReposHist.deleteAll(jcicZ571Log);
      jcicZ571LogReposHist.flush();
    }
    else {
      jcicZ571LogRepos.deleteAll(jcicZ571Log);
      jcicZ571LogRepos.flush();
    }
  }

}
