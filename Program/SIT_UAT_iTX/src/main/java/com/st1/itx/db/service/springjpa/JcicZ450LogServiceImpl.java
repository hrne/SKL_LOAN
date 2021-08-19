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
import com.st1.itx.db.domain.JcicZ450Log;
import com.st1.itx.db.domain.JcicZ450LogId;
import com.st1.itx.db.repository.online.JcicZ450LogRepository;
import com.st1.itx.db.repository.day.JcicZ450LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ450LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ450LogRepositoryHist;
import com.st1.itx.db.service.JcicZ450LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ450LogService")
@Repository
public class JcicZ450LogServiceImpl extends ASpringJpaParm implements JcicZ450LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ450LogRepository jcicZ450LogRepos;

  @Autowired
  private JcicZ450LogRepositoryDay jcicZ450LogReposDay;

  @Autowired
  private JcicZ450LogRepositoryMon jcicZ450LogReposMon;

  @Autowired
  private JcicZ450LogRepositoryHist jcicZ450LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ450LogRepos);
    org.junit.Assert.assertNotNull(jcicZ450LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ450LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ450LogReposHist);
  }

  @Override
  public JcicZ450Log findById(JcicZ450LogId jcicZ450LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ450LogId);
    Optional<JcicZ450Log> jcicZ450Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ450Log = jcicZ450LogReposDay.findById(jcicZ450LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ450Log = jcicZ450LogReposMon.findById(jcicZ450LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ450Log = jcicZ450LogReposHist.findById(jcicZ450LogId);
    else 
      jcicZ450Log = jcicZ450LogRepos.findById(jcicZ450LogId);
    JcicZ450Log obj = jcicZ450Log.isPresent() ? jcicZ450Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ450Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ450Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ450LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ450LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ450LogReposHist.findAll(pageable);
    else 
      slice = jcicZ450LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ450Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ450Log> jcicZ450LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ450LogT = jcicZ450LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ450LogT = jcicZ450LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ450LogT = jcicZ450LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ450LogT = jcicZ450LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ450LogT.isPresent() ? jcicZ450LogT.get() : null;
  }

  @Override
  public Slice<JcicZ450Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ450Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ450LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ450LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ450LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ450LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ450Log holdById(JcicZ450LogId jcicZ450LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ450LogId);
    Optional<JcicZ450Log> jcicZ450Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ450Log = jcicZ450LogReposDay.findByJcicZ450LogId(jcicZ450LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ450Log = jcicZ450LogReposMon.findByJcicZ450LogId(jcicZ450LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ450Log = jcicZ450LogReposHist.findByJcicZ450LogId(jcicZ450LogId);
    else 
      jcicZ450Log = jcicZ450LogRepos.findByJcicZ450LogId(jcicZ450LogId);
    return jcicZ450Log.isPresent() ? jcicZ450Log.get() : null;
  }

  @Override
  public JcicZ450Log holdById(JcicZ450Log jcicZ450Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ450Log.getJcicZ450LogId());
    Optional<JcicZ450Log> jcicZ450LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ450LogT = jcicZ450LogReposDay.findByJcicZ450LogId(jcicZ450Log.getJcicZ450LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ450LogT = jcicZ450LogReposMon.findByJcicZ450LogId(jcicZ450Log.getJcicZ450LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ450LogT = jcicZ450LogReposHist.findByJcicZ450LogId(jcicZ450Log.getJcicZ450LogId());
    else 
      jcicZ450LogT = jcicZ450LogRepos.findByJcicZ450LogId(jcicZ450Log.getJcicZ450LogId());
    return jcicZ450LogT.isPresent() ? jcicZ450LogT.get() : null;
  }

  @Override
  public JcicZ450Log insert(JcicZ450Log jcicZ450Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ450Log.getJcicZ450LogId());
    if (this.findById(jcicZ450Log.getJcicZ450LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ450Log.setCreateEmpNo(empNot);

    if(jcicZ450Log.getLastUpdateEmpNo() == null || jcicZ450Log.getLastUpdateEmpNo().isEmpty())
      jcicZ450Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ450LogReposDay.saveAndFlush(jcicZ450Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ450LogReposMon.saveAndFlush(jcicZ450Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ450LogReposHist.saveAndFlush(jcicZ450Log);
    else 
    return jcicZ450LogRepos.saveAndFlush(jcicZ450Log);
  }

  @Override
  public JcicZ450Log update(JcicZ450Log jcicZ450Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ450Log.getJcicZ450LogId());
    if (!empNot.isEmpty())
      jcicZ450Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ450LogReposDay.saveAndFlush(jcicZ450Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ450LogReposMon.saveAndFlush(jcicZ450Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ450LogReposHist.saveAndFlush(jcicZ450Log);
    else 
    return jcicZ450LogRepos.saveAndFlush(jcicZ450Log);
  }

  @Override
  public JcicZ450Log update2(JcicZ450Log jcicZ450Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ450Log.getJcicZ450LogId());
    if (!empNot.isEmpty())
      jcicZ450Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ450LogReposDay.saveAndFlush(jcicZ450Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ450LogReposMon.saveAndFlush(jcicZ450Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ450LogReposHist.saveAndFlush(jcicZ450Log);
    else 
      jcicZ450LogRepos.saveAndFlush(jcicZ450Log);	
    return this.findById(jcicZ450Log.getJcicZ450LogId());
  }

  @Override
  public void delete(JcicZ450Log jcicZ450Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ450Log.getJcicZ450LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ450LogReposDay.delete(jcicZ450Log);	
      jcicZ450LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ450LogReposMon.delete(jcicZ450Log);	
      jcicZ450LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ450LogReposHist.delete(jcicZ450Log);
      jcicZ450LogReposHist.flush();
    }
    else {
      jcicZ450LogRepos.delete(jcicZ450Log);
      jcicZ450LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ450Log> jcicZ450Log, TitaVo... titaVo) throws DBException {
    if (jcicZ450Log == null || jcicZ450Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ450Log t : jcicZ450Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ450Log = jcicZ450LogReposDay.saveAll(jcicZ450Log);	
      jcicZ450LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ450Log = jcicZ450LogReposMon.saveAll(jcicZ450Log);	
      jcicZ450LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ450Log = jcicZ450LogReposHist.saveAll(jcicZ450Log);
      jcicZ450LogReposHist.flush();
    }
    else {
      jcicZ450Log = jcicZ450LogRepos.saveAll(jcicZ450Log);
      jcicZ450LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ450Log> jcicZ450Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ450Log == null || jcicZ450Log.size() == 0)
      throw new DBException(6);

    for (JcicZ450Log t : jcicZ450Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ450Log = jcicZ450LogReposDay.saveAll(jcicZ450Log);	
      jcicZ450LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ450Log = jcicZ450LogReposMon.saveAll(jcicZ450Log);	
      jcicZ450LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ450Log = jcicZ450LogReposHist.saveAll(jcicZ450Log);
      jcicZ450LogReposHist.flush();
    }
    else {
      jcicZ450Log = jcicZ450LogRepos.saveAll(jcicZ450Log);
      jcicZ450LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ450Log> jcicZ450Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ450Log == null || jcicZ450Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ450LogReposDay.deleteAll(jcicZ450Log);	
      jcicZ450LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ450LogReposMon.deleteAll(jcicZ450Log);	
      jcicZ450LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ450LogReposHist.deleteAll(jcicZ450Log);
      jcicZ450LogReposHist.flush();
    }
    else {
      jcicZ450LogRepos.deleteAll(jcicZ450Log);
      jcicZ450LogRepos.flush();
    }
  }

}
