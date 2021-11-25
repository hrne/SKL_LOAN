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
import com.st1.itx.db.domain.JcicZ049Log;
import com.st1.itx.db.domain.JcicZ049LogId;
import com.st1.itx.db.repository.online.JcicZ049LogRepository;
import com.st1.itx.db.repository.day.JcicZ049LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ049LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ049LogRepositoryHist;
import com.st1.itx.db.service.JcicZ049LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ049LogService")
@Repository
public class JcicZ049LogServiceImpl extends ASpringJpaParm implements JcicZ049LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ049LogRepository jcicZ049LogRepos;

  @Autowired
  private JcicZ049LogRepositoryDay jcicZ049LogReposDay;

  @Autowired
  private JcicZ049LogRepositoryMon jcicZ049LogReposMon;

  @Autowired
  private JcicZ049LogRepositoryHist jcicZ049LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ049LogRepos);
    org.junit.Assert.assertNotNull(jcicZ049LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ049LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ049LogReposHist);
  }

  @Override
  public JcicZ049Log findById(JcicZ049LogId jcicZ049LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ049LogId);
    Optional<JcicZ049Log> jcicZ049Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ049Log = jcicZ049LogReposDay.findById(jcicZ049LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ049Log = jcicZ049LogReposMon.findById(jcicZ049LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ049Log = jcicZ049LogReposHist.findById(jcicZ049LogId);
    else 
      jcicZ049Log = jcicZ049LogRepos.findById(jcicZ049LogId);
    JcicZ049Log obj = jcicZ049Log.isPresent() ? jcicZ049Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ049Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ049Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ049LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ049LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ049LogReposHist.findAll(pageable);
    else 
      slice = jcicZ049LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ049Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ049Log> jcicZ049LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ049LogT = jcicZ049LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ049LogT = jcicZ049LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ049LogT = jcicZ049LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ049LogT = jcicZ049LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ049LogT.isPresent() ? jcicZ049LogT.get() : null;
  }

  @Override
  public Slice<JcicZ049Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ049Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ049LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ049LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ049LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ049LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ049Log holdById(JcicZ049LogId jcicZ049LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ049LogId);
    Optional<JcicZ049Log> jcicZ049Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ049Log = jcicZ049LogReposDay.findByJcicZ049LogId(jcicZ049LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ049Log = jcicZ049LogReposMon.findByJcicZ049LogId(jcicZ049LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ049Log = jcicZ049LogReposHist.findByJcicZ049LogId(jcicZ049LogId);
    else 
      jcicZ049Log = jcicZ049LogRepos.findByJcicZ049LogId(jcicZ049LogId);
    return jcicZ049Log.isPresent() ? jcicZ049Log.get() : null;
  }

  @Override
  public JcicZ049Log holdById(JcicZ049Log jcicZ049Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ049Log.getJcicZ049LogId());
    Optional<JcicZ049Log> jcicZ049LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ049LogT = jcicZ049LogReposDay.findByJcicZ049LogId(jcicZ049Log.getJcicZ049LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ049LogT = jcicZ049LogReposMon.findByJcicZ049LogId(jcicZ049Log.getJcicZ049LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ049LogT = jcicZ049LogReposHist.findByJcicZ049LogId(jcicZ049Log.getJcicZ049LogId());
    else 
      jcicZ049LogT = jcicZ049LogRepos.findByJcicZ049LogId(jcicZ049Log.getJcicZ049LogId());
    return jcicZ049LogT.isPresent() ? jcicZ049LogT.get() : null;
  }

  @Override
  public JcicZ049Log insert(JcicZ049Log jcicZ049Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ049Log.getJcicZ049LogId());
    if (this.findById(jcicZ049Log.getJcicZ049LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ049Log.setCreateEmpNo(empNot);

    if(jcicZ049Log.getLastUpdateEmpNo() == null || jcicZ049Log.getLastUpdateEmpNo().isEmpty())
      jcicZ049Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ049LogReposDay.saveAndFlush(jcicZ049Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ049LogReposMon.saveAndFlush(jcicZ049Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ049LogReposHist.saveAndFlush(jcicZ049Log);
    else 
    return jcicZ049LogRepos.saveAndFlush(jcicZ049Log);
  }

  @Override
  public JcicZ049Log update(JcicZ049Log jcicZ049Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ049Log.getJcicZ049LogId());
    if (!empNot.isEmpty())
      jcicZ049Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ049LogReposDay.saveAndFlush(jcicZ049Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ049LogReposMon.saveAndFlush(jcicZ049Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ049LogReposHist.saveAndFlush(jcicZ049Log);
    else 
    return jcicZ049LogRepos.saveAndFlush(jcicZ049Log);
  }

  @Override
  public JcicZ049Log update2(JcicZ049Log jcicZ049Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ049Log.getJcicZ049LogId());
    if (!empNot.isEmpty())
      jcicZ049Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ049LogReposDay.saveAndFlush(jcicZ049Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ049LogReposMon.saveAndFlush(jcicZ049Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ049LogReposHist.saveAndFlush(jcicZ049Log);
    else 
      jcicZ049LogRepos.saveAndFlush(jcicZ049Log);	
    return this.findById(jcicZ049Log.getJcicZ049LogId());
  }

  @Override
  public void delete(JcicZ049Log jcicZ049Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ049Log.getJcicZ049LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ049LogReposDay.delete(jcicZ049Log);	
      jcicZ049LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ049LogReposMon.delete(jcicZ049Log);	
      jcicZ049LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ049LogReposHist.delete(jcicZ049Log);
      jcicZ049LogReposHist.flush();
    }
    else {
      jcicZ049LogRepos.delete(jcicZ049Log);
      jcicZ049LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ049Log> jcicZ049Log, TitaVo... titaVo) throws DBException {
    if (jcicZ049Log == null || jcicZ049Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ049Log t : jcicZ049Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ049Log = jcicZ049LogReposDay.saveAll(jcicZ049Log);	
      jcicZ049LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ049Log = jcicZ049LogReposMon.saveAll(jcicZ049Log);	
      jcicZ049LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ049Log = jcicZ049LogReposHist.saveAll(jcicZ049Log);
      jcicZ049LogReposHist.flush();
    }
    else {
      jcicZ049Log = jcicZ049LogRepos.saveAll(jcicZ049Log);
      jcicZ049LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ049Log> jcicZ049Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ049Log == null || jcicZ049Log.size() == 0)
      throw new DBException(6);

    for (JcicZ049Log t : jcicZ049Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ049Log = jcicZ049LogReposDay.saveAll(jcicZ049Log);	
      jcicZ049LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ049Log = jcicZ049LogReposMon.saveAll(jcicZ049Log);	
      jcicZ049LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ049Log = jcicZ049LogReposHist.saveAll(jcicZ049Log);
      jcicZ049LogReposHist.flush();
    }
    else {
      jcicZ049Log = jcicZ049LogRepos.saveAll(jcicZ049Log);
      jcicZ049LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ049Log> jcicZ049Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ049Log == null || jcicZ049Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ049LogReposDay.deleteAll(jcicZ049Log);	
      jcicZ049LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ049LogReposMon.deleteAll(jcicZ049Log);	
      jcicZ049LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ049LogReposHist.deleteAll(jcicZ049Log);
      jcicZ049LogReposHist.flush();
    }
    else {
      jcicZ049LogRepos.deleteAll(jcicZ049Log);
      jcicZ049LogRepos.flush();
    }
  }

}
