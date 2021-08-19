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
import com.st1.itx.db.domain.JcicZ046Log;
import com.st1.itx.db.domain.JcicZ046LogId;
import com.st1.itx.db.repository.online.JcicZ046LogRepository;
import com.st1.itx.db.repository.day.JcicZ046LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ046LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ046LogRepositoryHist;
import com.st1.itx.db.service.JcicZ046LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ046LogService")
@Repository
public class JcicZ046LogServiceImpl extends ASpringJpaParm implements JcicZ046LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ046LogRepository jcicZ046LogRepos;

  @Autowired
  private JcicZ046LogRepositoryDay jcicZ046LogReposDay;

  @Autowired
  private JcicZ046LogRepositoryMon jcicZ046LogReposMon;

  @Autowired
  private JcicZ046LogRepositoryHist jcicZ046LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ046LogRepos);
    org.junit.Assert.assertNotNull(jcicZ046LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ046LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ046LogReposHist);
  }

  @Override
  public JcicZ046Log findById(JcicZ046LogId jcicZ046LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ046LogId);
    Optional<JcicZ046Log> jcicZ046Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ046Log = jcicZ046LogReposDay.findById(jcicZ046LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ046Log = jcicZ046LogReposMon.findById(jcicZ046LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ046Log = jcicZ046LogReposHist.findById(jcicZ046LogId);
    else 
      jcicZ046Log = jcicZ046LogRepos.findById(jcicZ046LogId);
    JcicZ046Log obj = jcicZ046Log.isPresent() ? jcicZ046Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ046Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ046Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ046LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ046LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ046LogReposHist.findAll(pageable);
    else 
      slice = jcicZ046LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ046Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ046Log> jcicZ046LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ046LogT = jcicZ046LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ046LogT = jcicZ046LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ046LogT = jcicZ046LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ046LogT = jcicZ046LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ046LogT.isPresent() ? jcicZ046LogT.get() : null;
  }

  @Override
  public Slice<JcicZ046Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ046Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ046LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ046LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ046LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ046LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ046Log holdById(JcicZ046LogId jcicZ046LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ046LogId);
    Optional<JcicZ046Log> jcicZ046Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ046Log = jcicZ046LogReposDay.findByJcicZ046LogId(jcicZ046LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ046Log = jcicZ046LogReposMon.findByJcicZ046LogId(jcicZ046LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ046Log = jcicZ046LogReposHist.findByJcicZ046LogId(jcicZ046LogId);
    else 
      jcicZ046Log = jcicZ046LogRepos.findByJcicZ046LogId(jcicZ046LogId);
    return jcicZ046Log.isPresent() ? jcicZ046Log.get() : null;
  }

  @Override
  public JcicZ046Log holdById(JcicZ046Log jcicZ046Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ046Log.getJcicZ046LogId());
    Optional<JcicZ046Log> jcicZ046LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ046LogT = jcicZ046LogReposDay.findByJcicZ046LogId(jcicZ046Log.getJcicZ046LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ046LogT = jcicZ046LogReposMon.findByJcicZ046LogId(jcicZ046Log.getJcicZ046LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ046LogT = jcicZ046LogReposHist.findByJcicZ046LogId(jcicZ046Log.getJcicZ046LogId());
    else 
      jcicZ046LogT = jcicZ046LogRepos.findByJcicZ046LogId(jcicZ046Log.getJcicZ046LogId());
    return jcicZ046LogT.isPresent() ? jcicZ046LogT.get() : null;
  }

  @Override
  public JcicZ046Log insert(JcicZ046Log jcicZ046Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ046Log.getJcicZ046LogId());
    if (this.findById(jcicZ046Log.getJcicZ046LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ046Log.setCreateEmpNo(empNot);

    if(jcicZ046Log.getLastUpdateEmpNo() == null || jcicZ046Log.getLastUpdateEmpNo().isEmpty())
      jcicZ046Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ046LogReposDay.saveAndFlush(jcicZ046Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ046LogReposMon.saveAndFlush(jcicZ046Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ046LogReposHist.saveAndFlush(jcicZ046Log);
    else 
    return jcicZ046LogRepos.saveAndFlush(jcicZ046Log);
  }

  @Override
  public JcicZ046Log update(JcicZ046Log jcicZ046Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ046Log.getJcicZ046LogId());
    if (!empNot.isEmpty())
      jcicZ046Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ046LogReposDay.saveAndFlush(jcicZ046Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ046LogReposMon.saveAndFlush(jcicZ046Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ046LogReposHist.saveAndFlush(jcicZ046Log);
    else 
    return jcicZ046LogRepos.saveAndFlush(jcicZ046Log);
  }

  @Override
  public JcicZ046Log update2(JcicZ046Log jcicZ046Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ046Log.getJcicZ046LogId());
    if (!empNot.isEmpty())
      jcicZ046Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ046LogReposDay.saveAndFlush(jcicZ046Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ046LogReposMon.saveAndFlush(jcicZ046Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ046LogReposHist.saveAndFlush(jcicZ046Log);
    else 
      jcicZ046LogRepos.saveAndFlush(jcicZ046Log);	
    return this.findById(jcicZ046Log.getJcicZ046LogId());
  }

  @Override
  public void delete(JcicZ046Log jcicZ046Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ046Log.getJcicZ046LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ046LogReposDay.delete(jcicZ046Log);	
      jcicZ046LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ046LogReposMon.delete(jcicZ046Log);	
      jcicZ046LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ046LogReposHist.delete(jcicZ046Log);
      jcicZ046LogReposHist.flush();
    }
    else {
      jcicZ046LogRepos.delete(jcicZ046Log);
      jcicZ046LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ046Log> jcicZ046Log, TitaVo... titaVo) throws DBException {
    if (jcicZ046Log == null || jcicZ046Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ046Log t : jcicZ046Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ046Log = jcicZ046LogReposDay.saveAll(jcicZ046Log);	
      jcicZ046LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ046Log = jcicZ046LogReposMon.saveAll(jcicZ046Log);	
      jcicZ046LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ046Log = jcicZ046LogReposHist.saveAll(jcicZ046Log);
      jcicZ046LogReposHist.flush();
    }
    else {
      jcicZ046Log = jcicZ046LogRepos.saveAll(jcicZ046Log);
      jcicZ046LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ046Log> jcicZ046Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ046Log == null || jcicZ046Log.size() == 0)
      throw new DBException(6);

    for (JcicZ046Log t : jcicZ046Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ046Log = jcicZ046LogReposDay.saveAll(jcicZ046Log);	
      jcicZ046LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ046Log = jcicZ046LogReposMon.saveAll(jcicZ046Log);	
      jcicZ046LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ046Log = jcicZ046LogReposHist.saveAll(jcicZ046Log);
      jcicZ046LogReposHist.flush();
    }
    else {
      jcicZ046Log = jcicZ046LogRepos.saveAll(jcicZ046Log);
      jcicZ046LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ046Log> jcicZ046Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ046Log == null || jcicZ046Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ046LogReposDay.deleteAll(jcicZ046Log);	
      jcicZ046LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ046LogReposMon.deleteAll(jcicZ046Log);	
      jcicZ046LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ046LogReposHist.deleteAll(jcicZ046Log);
      jcicZ046LogReposHist.flush();
    }
    else {
      jcicZ046LogRepos.deleteAll(jcicZ046Log);
      jcicZ046LogRepos.flush();
    }
  }

}
