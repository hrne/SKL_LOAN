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
import com.st1.itx.db.domain.JcicZ454Log;
import com.st1.itx.db.domain.JcicZ454LogId;
import com.st1.itx.db.repository.online.JcicZ454LogRepository;
import com.st1.itx.db.repository.day.JcicZ454LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ454LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ454LogRepositoryHist;
import com.st1.itx.db.service.JcicZ454LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ454LogService")
@Repository
public class JcicZ454LogServiceImpl extends ASpringJpaParm implements JcicZ454LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ454LogRepository jcicZ454LogRepos;

  @Autowired
  private JcicZ454LogRepositoryDay jcicZ454LogReposDay;

  @Autowired
  private JcicZ454LogRepositoryMon jcicZ454LogReposMon;

  @Autowired
  private JcicZ454LogRepositoryHist jcicZ454LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ454LogRepos);
    org.junit.Assert.assertNotNull(jcicZ454LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ454LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ454LogReposHist);
  }

  @Override
  public JcicZ454Log findById(JcicZ454LogId jcicZ454LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ454LogId);
    Optional<JcicZ454Log> jcicZ454Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ454Log = jcicZ454LogReposDay.findById(jcicZ454LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ454Log = jcicZ454LogReposMon.findById(jcicZ454LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ454Log = jcicZ454LogReposHist.findById(jcicZ454LogId);
    else 
      jcicZ454Log = jcicZ454LogRepos.findById(jcicZ454LogId);
    JcicZ454Log obj = jcicZ454Log.isPresent() ? jcicZ454Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ454Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ454Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ454LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ454LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ454LogReposHist.findAll(pageable);
    else 
      slice = jcicZ454LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ454Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ454Log> jcicZ454LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ454LogT = jcicZ454LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ454LogT = jcicZ454LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ454LogT = jcicZ454LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ454LogT = jcicZ454LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ454LogT.isPresent() ? jcicZ454LogT.get() : null;
  }

  @Override
  public Slice<JcicZ454Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ454Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ454LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ454LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ454LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ454LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ454Log holdById(JcicZ454LogId jcicZ454LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ454LogId);
    Optional<JcicZ454Log> jcicZ454Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ454Log = jcicZ454LogReposDay.findByJcicZ454LogId(jcicZ454LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ454Log = jcicZ454LogReposMon.findByJcicZ454LogId(jcicZ454LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ454Log = jcicZ454LogReposHist.findByJcicZ454LogId(jcicZ454LogId);
    else 
      jcicZ454Log = jcicZ454LogRepos.findByJcicZ454LogId(jcicZ454LogId);
    return jcicZ454Log.isPresent() ? jcicZ454Log.get() : null;
  }

  @Override
  public JcicZ454Log holdById(JcicZ454Log jcicZ454Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ454Log.getJcicZ454LogId());
    Optional<JcicZ454Log> jcicZ454LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ454LogT = jcicZ454LogReposDay.findByJcicZ454LogId(jcicZ454Log.getJcicZ454LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ454LogT = jcicZ454LogReposMon.findByJcicZ454LogId(jcicZ454Log.getJcicZ454LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ454LogT = jcicZ454LogReposHist.findByJcicZ454LogId(jcicZ454Log.getJcicZ454LogId());
    else 
      jcicZ454LogT = jcicZ454LogRepos.findByJcicZ454LogId(jcicZ454Log.getJcicZ454LogId());
    return jcicZ454LogT.isPresent() ? jcicZ454LogT.get() : null;
  }

  @Override
  public JcicZ454Log insert(JcicZ454Log jcicZ454Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ454Log.getJcicZ454LogId());
    if (this.findById(jcicZ454Log.getJcicZ454LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ454Log.setCreateEmpNo(empNot);

    if(jcicZ454Log.getLastUpdateEmpNo() == null || jcicZ454Log.getLastUpdateEmpNo().isEmpty())
      jcicZ454Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ454LogReposDay.saveAndFlush(jcicZ454Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ454LogReposMon.saveAndFlush(jcicZ454Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ454LogReposHist.saveAndFlush(jcicZ454Log);
    else 
    return jcicZ454LogRepos.saveAndFlush(jcicZ454Log);
  }

  @Override
  public JcicZ454Log update(JcicZ454Log jcicZ454Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ454Log.getJcicZ454LogId());
    if (!empNot.isEmpty())
      jcicZ454Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ454LogReposDay.saveAndFlush(jcicZ454Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ454LogReposMon.saveAndFlush(jcicZ454Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ454LogReposHist.saveAndFlush(jcicZ454Log);
    else 
    return jcicZ454LogRepos.saveAndFlush(jcicZ454Log);
  }

  @Override
  public JcicZ454Log update2(JcicZ454Log jcicZ454Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ454Log.getJcicZ454LogId());
    if (!empNot.isEmpty())
      jcicZ454Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ454LogReposDay.saveAndFlush(jcicZ454Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ454LogReposMon.saveAndFlush(jcicZ454Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ454LogReposHist.saveAndFlush(jcicZ454Log);
    else 
      jcicZ454LogRepos.saveAndFlush(jcicZ454Log);	
    return this.findById(jcicZ454Log.getJcicZ454LogId());
  }

  @Override
  public void delete(JcicZ454Log jcicZ454Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ454Log.getJcicZ454LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ454LogReposDay.delete(jcicZ454Log);	
      jcicZ454LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ454LogReposMon.delete(jcicZ454Log);	
      jcicZ454LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ454LogReposHist.delete(jcicZ454Log);
      jcicZ454LogReposHist.flush();
    }
    else {
      jcicZ454LogRepos.delete(jcicZ454Log);
      jcicZ454LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ454Log> jcicZ454Log, TitaVo... titaVo) throws DBException {
    if (jcicZ454Log == null || jcicZ454Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ454Log t : jcicZ454Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ454Log = jcicZ454LogReposDay.saveAll(jcicZ454Log);	
      jcicZ454LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ454Log = jcicZ454LogReposMon.saveAll(jcicZ454Log);	
      jcicZ454LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ454Log = jcicZ454LogReposHist.saveAll(jcicZ454Log);
      jcicZ454LogReposHist.flush();
    }
    else {
      jcicZ454Log = jcicZ454LogRepos.saveAll(jcicZ454Log);
      jcicZ454LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ454Log> jcicZ454Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ454Log == null || jcicZ454Log.size() == 0)
      throw new DBException(6);

    for (JcicZ454Log t : jcicZ454Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ454Log = jcicZ454LogReposDay.saveAll(jcicZ454Log);	
      jcicZ454LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ454Log = jcicZ454LogReposMon.saveAll(jcicZ454Log);	
      jcicZ454LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ454Log = jcicZ454LogReposHist.saveAll(jcicZ454Log);
      jcicZ454LogReposHist.flush();
    }
    else {
      jcicZ454Log = jcicZ454LogRepos.saveAll(jcicZ454Log);
      jcicZ454LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ454Log> jcicZ454Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ454Log == null || jcicZ454Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ454LogReposDay.deleteAll(jcicZ454Log);	
      jcicZ454LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ454LogReposMon.deleteAll(jcicZ454Log);	
      jcicZ454LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ454LogReposHist.deleteAll(jcicZ454Log);
      jcicZ454LogReposHist.flush();
    }
    else {
      jcicZ454LogRepos.deleteAll(jcicZ454Log);
      jcicZ454LogRepos.flush();
    }
  }

}
