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
import com.st1.itx.db.domain.JcicZ451Log;
import com.st1.itx.db.domain.JcicZ451LogId;
import com.st1.itx.db.repository.online.JcicZ451LogRepository;
import com.st1.itx.db.repository.day.JcicZ451LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ451LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ451LogRepositoryHist;
import com.st1.itx.db.service.JcicZ451LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ451LogService")
@Repository
public class JcicZ451LogServiceImpl extends ASpringJpaParm implements JcicZ451LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ451LogRepository jcicZ451LogRepos;

  @Autowired
  private JcicZ451LogRepositoryDay jcicZ451LogReposDay;

  @Autowired
  private JcicZ451LogRepositoryMon jcicZ451LogReposMon;

  @Autowired
  private JcicZ451LogRepositoryHist jcicZ451LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ451LogRepos);
    org.junit.Assert.assertNotNull(jcicZ451LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ451LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ451LogReposHist);
  }

  @Override
  public JcicZ451Log findById(JcicZ451LogId jcicZ451LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ451LogId);
    Optional<JcicZ451Log> jcicZ451Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ451Log = jcicZ451LogReposDay.findById(jcicZ451LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ451Log = jcicZ451LogReposMon.findById(jcicZ451LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ451Log = jcicZ451LogReposHist.findById(jcicZ451LogId);
    else 
      jcicZ451Log = jcicZ451LogRepos.findById(jcicZ451LogId);
    JcicZ451Log obj = jcicZ451Log.isPresent() ? jcicZ451Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ451Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ451Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ451LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ451LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ451LogReposHist.findAll(pageable);
    else 
      slice = jcicZ451LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ451Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ451Log> jcicZ451LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ451LogT = jcicZ451LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ451LogT = jcicZ451LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ451LogT = jcicZ451LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ451LogT = jcicZ451LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ451LogT.isPresent() ? jcicZ451LogT.get() : null;
  }

  @Override
  public Slice<JcicZ451Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ451Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ451LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ451LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ451LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ451LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ451Log holdById(JcicZ451LogId jcicZ451LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ451LogId);
    Optional<JcicZ451Log> jcicZ451Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ451Log = jcicZ451LogReposDay.findByJcicZ451LogId(jcicZ451LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ451Log = jcicZ451LogReposMon.findByJcicZ451LogId(jcicZ451LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ451Log = jcicZ451LogReposHist.findByJcicZ451LogId(jcicZ451LogId);
    else 
      jcicZ451Log = jcicZ451LogRepos.findByJcicZ451LogId(jcicZ451LogId);
    return jcicZ451Log.isPresent() ? jcicZ451Log.get() : null;
  }

  @Override
  public JcicZ451Log holdById(JcicZ451Log jcicZ451Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ451Log.getJcicZ451LogId());
    Optional<JcicZ451Log> jcicZ451LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ451LogT = jcicZ451LogReposDay.findByJcicZ451LogId(jcicZ451Log.getJcicZ451LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ451LogT = jcicZ451LogReposMon.findByJcicZ451LogId(jcicZ451Log.getJcicZ451LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ451LogT = jcicZ451LogReposHist.findByJcicZ451LogId(jcicZ451Log.getJcicZ451LogId());
    else 
      jcicZ451LogT = jcicZ451LogRepos.findByJcicZ451LogId(jcicZ451Log.getJcicZ451LogId());
    return jcicZ451LogT.isPresent() ? jcicZ451LogT.get() : null;
  }

  @Override
  public JcicZ451Log insert(JcicZ451Log jcicZ451Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ451Log.getJcicZ451LogId());
    if (this.findById(jcicZ451Log.getJcicZ451LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ451Log.setCreateEmpNo(empNot);

    if(jcicZ451Log.getLastUpdateEmpNo() == null || jcicZ451Log.getLastUpdateEmpNo().isEmpty())
      jcicZ451Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ451LogReposDay.saveAndFlush(jcicZ451Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ451LogReposMon.saveAndFlush(jcicZ451Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ451LogReposHist.saveAndFlush(jcicZ451Log);
    else 
    return jcicZ451LogRepos.saveAndFlush(jcicZ451Log);
  }

  @Override
  public JcicZ451Log update(JcicZ451Log jcicZ451Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ451Log.getJcicZ451LogId());
    if (!empNot.isEmpty())
      jcicZ451Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ451LogReposDay.saveAndFlush(jcicZ451Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ451LogReposMon.saveAndFlush(jcicZ451Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ451LogReposHist.saveAndFlush(jcicZ451Log);
    else 
    return jcicZ451LogRepos.saveAndFlush(jcicZ451Log);
  }

  @Override
  public JcicZ451Log update2(JcicZ451Log jcicZ451Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ451Log.getJcicZ451LogId());
    if (!empNot.isEmpty())
      jcicZ451Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ451LogReposDay.saveAndFlush(jcicZ451Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ451LogReposMon.saveAndFlush(jcicZ451Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ451LogReposHist.saveAndFlush(jcicZ451Log);
    else 
      jcicZ451LogRepos.saveAndFlush(jcicZ451Log);	
    return this.findById(jcicZ451Log.getJcicZ451LogId());
  }

  @Override
  public void delete(JcicZ451Log jcicZ451Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ451Log.getJcicZ451LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ451LogReposDay.delete(jcicZ451Log);	
      jcicZ451LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ451LogReposMon.delete(jcicZ451Log);	
      jcicZ451LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ451LogReposHist.delete(jcicZ451Log);
      jcicZ451LogReposHist.flush();
    }
    else {
      jcicZ451LogRepos.delete(jcicZ451Log);
      jcicZ451LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ451Log> jcicZ451Log, TitaVo... titaVo) throws DBException {
    if (jcicZ451Log == null || jcicZ451Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ451Log t : jcicZ451Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ451Log = jcicZ451LogReposDay.saveAll(jcicZ451Log);	
      jcicZ451LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ451Log = jcicZ451LogReposMon.saveAll(jcicZ451Log);	
      jcicZ451LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ451Log = jcicZ451LogReposHist.saveAll(jcicZ451Log);
      jcicZ451LogReposHist.flush();
    }
    else {
      jcicZ451Log = jcicZ451LogRepos.saveAll(jcicZ451Log);
      jcicZ451LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ451Log> jcicZ451Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ451Log == null || jcicZ451Log.size() == 0)
      throw new DBException(6);

    for (JcicZ451Log t : jcicZ451Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ451Log = jcicZ451LogReposDay.saveAll(jcicZ451Log);	
      jcicZ451LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ451Log = jcicZ451LogReposMon.saveAll(jcicZ451Log);	
      jcicZ451LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ451Log = jcicZ451LogReposHist.saveAll(jcicZ451Log);
      jcicZ451LogReposHist.flush();
    }
    else {
      jcicZ451Log = jcicZ451LogRepos.saveAll(jcicZ451Log);
      jcicZ451LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ451Log> jcicZ451Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ451Log == null || jcicZ451Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ451LogReposDay.deleteAll(jcicZ451Log);	
      jcicZ451LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ451LogReposMon.deleteAll(jcicZ451Log);	
      jcicZ451LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ451LogReposHist.deleteAll(jcicZ451Log);
      jcicZ451LogReposHist.flush();
    }
    else {
      jcicZ451LogRepos.deleteAll(jcicZ451Log);
      jcicZ451LogRepos.flush();
    }
  }

}
