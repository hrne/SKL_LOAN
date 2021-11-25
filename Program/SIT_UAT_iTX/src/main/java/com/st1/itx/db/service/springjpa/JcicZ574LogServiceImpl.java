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
import com.st1.itx.db.domain.JcicZ574Log;
import com.st1.itx.db.domain.JcicZ574LogId;
import com.st1.itx.db.repository.online.JcicZ574LogRepository;
import com.st1.itx.db.repository.day.JcicZ574LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ574LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ574LogRepositoryHist;
import com.st1.itx.db.service.JcicZ574LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ574LogService")
@Repository
public class JcicZ574LogServiceImpl extends ASpringJpaParm implements JcicZ574LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ574LogRepository jcicZ574LogRepos;

  @Autowired
  private JcicZ574LogRepositoryDay jcicZ574LogReposDay;

  @Autowired
  private JcicZ574LogRepositoryMon jcicZ574LogReposMon;

  @Autowired
  private JcicZ574LogRepositoryHist jcicZ574LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ574LogRepos);
    org.junit.Assert.assertNotNull(jcicZ574LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ574LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ574LogReposHist);
  }

  @Override
  public JcicZ574Log findById(JcicZ574LogId jcicZ574LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ574LogId);
    Optional<JcicZ574Log> jcicZ574Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ574Log = jcicZ574LogReposDay.findById(jcicZ574LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ574Log = jcicZ574LogReposMon.findById(jcicZ574LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ574Log = jcicZ574LogReposHist.findById(jcicZ574LogId);
    else 
      jcicZ574Log = jcicZ574LogRepos.findById(jcicZ574LogId);
    JcicZ574Log obj = jcicZ574Log.isPresent() ? jcicZ574Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ574Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ574Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ574LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ574LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ574LogReposHist.findAll(pageable);
    else 
      slice = jcicZ574LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ574Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ574Log> jcicZ574LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ574LogT = jcicZ574LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ574LogT = jcicZ574LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ574LogT = jcicZ574LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ574LogT = jcicZ574LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ574LogT.isPresent() ? jcicZ574LogT.get() : null;
  }

  @Override
  public Slice<JcicZ574Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ574Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ574LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ574LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ574LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ574LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ574Log holdById(JcicZ574LogId jcicZ574LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ574LogId);
    Optional<JcicZ574Log> jcicZ574Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ574Log = jcicZ574LogReposDay.findByJcicZ574LogId(jcicZ574LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ574Log = jcicZ574LogReposMon.findByJcicZ574LogId(jcicZ574LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ574Log = jcicZ574LogReposHist.findByJcicZ574LogId(jcicZ574LogId);
    else 
      jcicZ574Log = jcicZ574LogRepos.findByJcicZ574LogId(jcicZ574LogId);
    return jcicZ574Log.isPresent() ? jcicZ574Log.get() : null;
  }

  @Override
  public JcicZ574Log holdById(JcicZ574Log jcicZ574Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ574Log.getJcicZ574LogId());
    Optional<JcicZ574Log> jcicZ574LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ574LogT = jcicZ574LogReposDay.findByJcicZ574LogId(jcicZ574Log.getJcicZ574LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ574LogT = jcicZ574LogReposMon.findByJcicZ574LogId(jcicZ574Log.getJcicZ574LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ574LogT = jcicZ574LogReposHist.findByJcicZ574LogId(jcicZ574Log.getJcicZ574LogId());
    else 
      jcicZ574LogT = jcicZ574LogRepos.findByJcicZ574LogId(jcicZ574Log.getJcicZ574LogId());
    return jcicZ574LogT.isPresent() ? jcicZ574LogT.get() : null;
  }

  @Override
  public JcicZ574Log insert(JcicZ574Log jcicZ574Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ574Log.getJcicZ574LogId());
    if (this.findById(jcicZ574Log.getJcicZ574LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ574Log.setCreateEmpNo(empNot);

    if(jcicZ574Log.getLastUpdateEmpNo() == null || jcicZ574Log.getLastUpdateEmpNo().isEmpty())
      jcicZ574Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ574LogReposDay.saveAndFlush(jcicZ574Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ574LogReposMon.saveAndFlush(jcicZ574Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ574LogReposHist.saveAndFlush(jcicZ574Log);
    else 
    return jcicZ574LogRepos.saveAndFlush(jcicZ574Log);
  }

  @Override
  public JcicZ574Log update(JcicZ574Log jcicZ574Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ574Log.getJcicZ574LogId());
    if (!empNot.isEmpty())
      jcicZ574Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ574LogReposDay.saveAndFlush(jcicZ574Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ574LogReposMon.saveAndFlush(jcicZ574Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ574LogReposHist.saveAndFlush(jcicZ574Log);
    else 
    return jcicZ574LogRepos.saveAndFlush(jcicZ574Log);
  }

  @Override
  public JcicZ574Log update2(JcicZ574Log jcicZ574Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ574Log.getJcicZ574LogId());
    if (!empNot.isEmpty())
      jcicZ574Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ574LogReposDay.saveAndFlush(jcicZ574Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ574LogReposMon.saveAndFlush(jcicZ574Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ574LogReposHist.saveAndFlush(jcicZ574Log);
    else 
      jcicZ574LogRepos.saveAndFlush(jcicZ574Log);	
    return this.findById(jcicZ574Log.getJcicZ574LogId());
  }

  @Override
  public void delete(JcicZ574Log jcicZ574Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ574Log.getJcicZ574LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ574LogReposDay.delete(jcicZ574Log);	
      jcicZ574LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ574LogReposMon.delete(jcicZ574Log);	
      jcicZ574LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ574LogReposHist.delete(jcicZ574Log);
      jcicZ574LogReposHist.flush();
    }
    else {
      jcicZ574LogRepos.delete(jcicZ574Log);
      jcicZ574LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ574Log> jcicZ574Log, TitaVo... titaVo) throws DBException {
    if (jcicZ574Log == null || jcicZ574Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ574Log t : jcicZ574Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ574Log = jcicZ574LogReposDay.saveAll(jcicZ574Log);	
      jcicZ574LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ574Log = jcicZ574LogReposMon.saveAll(jcicZ574Log);	
      jcicZ574LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ574Log = jcicZ574LogReposHist.saveAll(jcicZ574Log);
      jcicZ574LogReposHist.flush();
    }
    else {
      jcicZ574Log = jcicZ574LogRepos.saveAll(jcicZ574Log);
      jcicZ574LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ574Log> jcicZ574Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ574Log == null || jcicZ574Log.size() == 0)
      throw new DBException(6);

    for (JcicZ574Log t : jcicZ574Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ574Log = jcicZ574LogReposDay.saveAll(jcicZ574Log);	
      jcicZ574LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ574Log = jcicZ574LogReposMon.saveAll(jcicZ574Log);	
      jcicZ574LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ574Log = jcicZ574LogReposHist.saveAll(jcicZ574Log);
      jcicZ574LogReposHist.flush();
    }
    else {
      jcicZ574Log = jcicZ574LogRepos.saveAll(jcicZ574Log);
      jcicZ574LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ574Log> jcicZ574Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ574Log == null || jcicZ574Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ574LogReposDay.deleteAll(jcicZ574Log);	
      jcicZ574LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ574LogReposMon.deleteAll(jcicZ574Log);	
      jcicZ574LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ574LogReposHist.deleteAll(jcicZ574Log);
      jcicZ574LogReposHist.flush();
    }
    else {
      jcicZ574LogRepos.deleteAll(jcicZ574Log);
      jcicZ574LogRepos.flush();
    }
  }

}
