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
import com.st1.itx.db.domain.JcicZ444Log;
import com.st1.itx.db.domain.JcicZ444LogId;
import com.st1.itx.db.repository.online.JcicZ444LogRepository;
import com.st1.itx.db.repository.day.JcicZ444LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ444LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ444LogRepositoryHist;
import com.st1.itx.db.service.JcicZ444LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ444LogService")
@Repository
public class JcicZ444LogServiceImpl extends ASpringJpaParm implements JcicZ444LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ444LogRepository jcicZ444LogRepos;

  @Autowired
  private JcicZ444LogRepositoryDay jcicZ444LogReposDay;

  @Autowired
  private JcicZ444LogRepositoryMon jcicZ444LogReposMon;

  @Autowired
  private JcicZ444LogRepositoryHist jcicZ444LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ444LogRepos);
    org.junit.Assert.assertNotNull(jcicZ444LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ444LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ444LogReposHist);
  }

  @Override
  public JcicZ444Log findById(JcicZ444LogId jcicZ444LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ444LogId);
    Optional<JcicZ444Log> jcicZ444Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ444Log = jcicZ444LogReposDay.findById(jcicZ444LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ444Log = jcicZ444LogReposMon.findById(jcicZ444LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ444Log = jcicZ444LogReposHist.findById(jcicZ444LogId);
    else 
      jcicZ444Log = jcicZ444LogRepos.findById(jcicZ444LogId);
    JcicZ444Log obj = jcicZ444Log.isPresent() ? jcicZ444Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ444Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ444Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ444LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ444LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ444LogReposHist.findAll(pageable);
    else 
      slice = jcicZ444LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ444Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ444Log> jcicZ444LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ444LogT = jcicZ444LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ444LogT = jcicZ444LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ444LogT = jcicZ444LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ444LogT = jcicZ444LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ444LogT.isPresent() ? jcicZ444LogT.get() : null;
  }

  @Override
  public Slice<JcicZ444Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ444Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ444LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ444LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ444LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ444LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ444Log holdById(JcicZ444LogId jcicZ444LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ444LogId);
    Optional<JcicZ444Log> jcicZ444Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ444Log = jcicZ444LogReposDay.findByJcicZ444LogId(jcicZ444LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ444Log = jcicZ444LogReposMon.findByJcicZ444LogId(jcicZ444LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ444Log = jcicZ444LogReposHist.findByJcicZ444LogId(jcicZ444LogId);
    else 
      jcicZ444Log = jcicZ444LogRepos.findByJcicZ444LogId(jcicZ444LogId);
    return jcicZ444Log.isPresent() ? jcicZ444Log.get() : null;
  }

  @Override
  public JcicZ444Log holdById(JcicZ444Log jcicZ444Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ444Log.getJcicZ444LogId());
    Optional<JcicZ444Log> jcicZ444LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ444LogT = jcicZ444LogReposDay.findByJcicZ444LogId(jcicZ444Log.getJcicZ444LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ444LogT = jcicZ444LogReposMon.findByJcicZ444LogId(jcicZ444Log.getJcicZ444LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ444LogT = jcicZ444LogReposHist.findByJcicZ444LogId(jcicZ444Log.getJcicZ444LogId());
    else 
      jcicZ444LogT = jcicZ444LogRepos.findByJcicZ444LogId(jcicZ444Log.getJcicZ444LogId());
    return jcicZ444LogT.isPresent() ? jcicZ444LogT.get() : null;
  }

  @Override
  public JcicZ444Log insert(JcicZ444Log jcicZ444Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicZ444Log.getJcicZ444LogId());
    if (this.findById(jcicZ444Log.getJcicZ444LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ444Log.setCreateEmpNo(empNot);

    if(jcicZ444Log.getLastUpdateEmpNo() == null || jcicZ444Log.getLastUpdateEmpNo().isEmpty())
      jcicZ444Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ444LogReposDay.saveAndFlush(jcicZ444Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ444LogReposMon.saveAndFlush(jcicZ444Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ444LogReposHist.saveAndFlush(jcicZ444Log);
    else 
    return jcicZ444LogRepos.saveAndFlush(jcicZ444Log);
  }

  @Override
  public JcicZ444Log update(JcicZ444Log jcicZ444Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ444Log.getJcicZ444LogId());
    if (!empNot.isEmpty())
      jcicZ444Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ444LogReposDay.saveAndFlush(jcicZ444Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ444LogReposMon.saveAndFlush(jcicZ444Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ444LogReposHist.saveAndFlush(jcicZ444Log);
    else 
    return jcicZ444LogRepos.saveAndFlush(jcicZ444Log);
  }

  @Override
  public JcicZ444Log update2(JcicZ444Log jcicZ444Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicZ444Log.getJcicZ444LogId());
    if (!empNot.isEmpty())
      jcicZ444Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ444LogReposDay.saveAndFlush(jcicZ444Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ444LogReposMon.saveAndFlush(jcicZ444Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ444LogReposHist.saveAndFlush(jcicZ444Log);
    else 
      jcicZ444LogRepos.saveAndFlush(jcicZ444Log);	
    return this.findById(jcicZ444Log.getJcicZ444LogId());
  }

  @Override
  public void delete(JcicZ444Log jcicZ444Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ444Log.getJcicZ444LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ444LogReposDay.delete(jcicZ444Log);	
      jcicZ444LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ444LogReposMon.delete(jcicZ444Log);	
      jcicZ444LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ444LogReposHist.delete(jcicZ444Log);
      jcicZ444LogReposHist.flush();
    }
    else {
      jcicZ444LogRepos.delete(jcicZ444Log);
      jcicZ444LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ444Log> jcicZ444Log, TitaVo... titaVo) throws DBException {
    if (jcicZ444Log == null || jcicZ444Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicZ444Log t : jcicZ444Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ444Log = jcicZ444LogReposDay.saveAll(jcicZ444Log);	
      jcicZ444LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ444Log = jcicZ444LogReposMon.saveAll(jcicZ444Log);	
      jcicZ444LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ444Log = jcicZ444LogReposHist.saveAll(jcicZ444Log);
      jcicZ444LogReposHist.flush();
    }
    else {
      jcicZ444Log = jcicZ444LogRepos.saveAll(jcicZ444Log);
      jcicZ444LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ444Log> jcicZ444Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicZ444Log == null || jcicZ444Log.size() == 0)
      throw new DBException(6);

    for (JcicZ444Log t : jcicZ444Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ444Log = jcicZ444LogReposDay.saveAll(jcicZ444Log);	
      jcicZ444LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ444Log = jcicZ444LogReposMon.saveAll(jcicZ444Log);	
      jcicZ444LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ444Log = jcicZ444LogReposHist.saveAll(jcicZ444Log);
      jcicZ444LogReposHist.flush();
    }
    else {
      jcicZ444Log = jcicZ444LogRepos.saveAll(jcicZ444Log);
      jcicZ444LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ444Log> jcicZ444Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ444Log == null || jcicZ444Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ444LogReposDay.deleteAll(jcicZ444Log);	
      jcicZ444LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ444LogReposMon.deleteAll(jcicZ444Log);	
      jcicZ444LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ444LogReposHist.deleteAll(jcicZ444Log);
      jcicZ444LogReposHist.flush();
    }
    else {
      jcicZ444LogRepos.deleteAll(jcicZ444Log);
      jcicZ444LogRepos.flush();
    }
  }

}
