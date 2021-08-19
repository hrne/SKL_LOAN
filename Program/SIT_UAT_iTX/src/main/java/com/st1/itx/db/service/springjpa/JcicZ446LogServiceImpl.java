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
import com.st1.itx.db.domain.JcicZ446Log;
import com.st1.itx.db.domain.JcicZ446LogId;
import com.st1.itx.db.repository.online.JcicZ446LogRepository;
import com.st1.itx.db.repository.day.JcicZ446LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ446LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ446LogRepositoryHist;
import com.st1.itx.db.service.JcicZ446LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ446LogService")
@Repository
public class JcicZ446LogServiceImpl extends ASpringJpaParm implements JcicZ446LogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicZ446LogRepository jcicZ446LogRepos;

  @Autowired
  private JcicZ446LogRepositoryDay jcicZ446LogReposDay;

  @Autowired
  private JcicZ446LogRepositoryMon jcicZ446LogReposMon;

  @Autowired
  private JcicZ446LogRepositoryHist jcicZ446LogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicZ446LogRepos);
    org.junit.Assert.assertNotNull(jcicZ446LogReposDay);
    org.junit.Assert.assertNotNull(jcicZ446LogReposMon);
    org.junit.Assert.assertNotNull(jcicZ446LogReposHist);
  }

  @Override
  public JcicZ446Log findById(JcicZ446LogId jcicZ446LogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicZ446LogId);
    Optional<JcicZ446Log> jcicZ446Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ446Log = jcicZ446LogReposDay.findById(jcicZ446LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ446Log = jcicZ446LogReposMon.findById(jcicZ446LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ446Log = jcicZ446LogReposHist.findById(jcicZ446LogId);
    else 
      jcicZ446Log = jcicZ446LogRepos.findById(jcicZ446LogId);
    JcicZ446Log obj = jcicZ446Log.isPresent() ? jcicZ446Log.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicZ446Log> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ446Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ446LogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ446LogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ446LogReposHist.findAll(pageable);
    else 
      slice = jcicZ446LogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ446Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
    Optional<JcicZ446Log> jcicZ446LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ446LogT = jcicZ446LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onMon))
      jcicZ446LogT = jcicZ446LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else if (dbName.equals(ContentName.onHist))
      jcicZ446LogT = jcicZ446LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
    else 
      jcicZ446LogT = jcicZ446LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

    return jcicZ446LogT.isPresent() ? jcicZ446LogT.get() : null;
  }

  @Override
  public Slice<JcicZ446Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicZ446Log> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
    if (dbName.equals(ContentName.onDay))
      slice = jcicZ446LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicZ446LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicZ446LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
    else 
      slice = jcicZ446LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicZ446Log holdById(JcicZ446LogId jcicZ446LogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ446LogId);
    Optional<JcicZ446Log> jcicZ446Log = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ446Log = jcicZ446LogReposDay.findByJcicZ446LogId(jcicZ446LogId);
    else if (dbName.equals(ContentName.onMon))
      jcicZ446Log = jcicZ446LogReposMon.findByJcicZ446LogId(jcicZ446LogId);
    else if (dbName.equals(ContentName.onHist))
      jcicZ446Log = jcicZ446LogReposHist.findByJcicZ446LogId(jcicZ446LogId);
    else 
      jcicZ446Log = jcicZ446LogRepos.findByJcicZ446LogId(jcicZ446LogId);
    return jcicZ446Log.isPresent() ? jcicZ446Log.get() : null;
  }

  @Override
  public JcicZ446Log holdById(JcicZ446Log jcicZ446Log, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicZ446Log.getJcicZ446LogId());
    Optional<JcicZ446Log> jcicZ446LogT = null;
    if (dbName.equals(ContentName.onDay))
      jcicZ446LogT = jcicZ446LogReposDay.findByJcicZ446LogId(jcicZ446Log.getJcicZ446LogId());
    else if (dbName.equals(ContentName.onMon))
      jcicZ446LogT = jcicZ446LogReposMon.findByJcicZ446LogId(jcicZ446Log.getJcicZ446LogId());
    else if (dbName.equals(ContentName.onHist))
      jcicZ446LogT = jcicZ446LogReposHist.findByJcicZ446LogId(jcicZ446Log.getJcicZ446LogId());
    else 
      jcicZ446LogT = jcicZ446LogRepos.findByJcicZ446LogId(jcicZ446Log.getJcicZ446LogId());
    return jcicZ446LogT.isPresent() ? jcicZ446LogT.get() : null;
  }

  @Override
  public JcicZ446Log insert(JcicZ446Log jcicZ446Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + jcicZ446Log.getJcicZ446LogId());
    if (this.findById(jcicZ446Log.getJcicZ446LogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicZ446Log.setCreateEmpNo(empNot);

    if(jcicZ446Log.getLastUpdateEmpNo() == null || jcicZ446Log.getLastUpdateEmpNo().isEmpty())
      jcicZ446Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ446LogReposDay.saveAndFlush(jcicZ446Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ446LogReposMon.saveAndFlush(jcicZ446Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ446LogReposHist.saveAndFlush(jcicZ446Log);
    else 
    return jcicZ446LogRepos.saveAndFlush(jcicZ446Log);
  }

  @Override
  public JcicZ446Log update(JcicZ446Log jcicZ446Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ446Log.getJcicZ446LogId());
    if (!empNot.isEmpty())
      jcicZ446Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicZ446LogReposDay.saveAndFlush(jcicZ446Log);	
    else if (dbName.equals(ContentName.onMon))
      return jcicZ446LogReposMon.saveAndFlush(jcicZ446Log);
    else if (dbName.equals(ContentName.onHist))
      return jcicZ446LogReposHist.saveAndFlush(jcicZ446Log);
    else 
    return jcicZ446LogRepos.saveAndFlush(jcicZ446Log);
  }

  @Override
  public JcicZ446Log update2(JcicZ446Log jcicZ446Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + jcicZ446Log.getJcicZ446LogId());
    if (!empNot.isEmpty())
      jcicZ446Log.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicZ446LogReposDay.saveAndFlush(jcicZ446Log);	
    else if (dbName.equals(ContentName.onMon))
      jcicZ446LogReposMon.saveAndFlush(jcicZ446Log);
    else if (dbName.equals(ContentName.onHist))
        jcicZ446LogReposHist.saveAndFlush(jcicZ446Log);
    else 
      jcicZ446LogRepos.saveAndFlush(jcicZ446Log);	
    return this.findById(jcicZ446Log.getJcicZ446LogId());
  }

  @Override
  public void delete(JcicZ446Log jcicZ446Log, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicZ446Log.getJcicZ446LogId());
    if (dbName.equals(ContentName.onDay)) {
      jcicZ446LogReposDay.delete(jcicZ446Log);	
      jcicZ446LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ446LogReposMon.delete(jcicZ446Log);	
      jcicZ446LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ446LogReposHist.delete(jcicZ446Log);
      jcicZ446LogReposHist.flush();
    }
    else {
      jcicZ446LogRepos.delete(jcicZ446Log);
      jcicZ446LogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicZ446Log> jcicZ446Log, TitaVo... titaVo) throws DBException {
    if (jcicZ446Log == null || jcicZ446Log.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (JcicZ446Log t : jcicZ446Log){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ446Log = jcicZ446LogReposDay.saveAll(jcicZ446Log);	
      jcicZ446LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ446Log = jcicZ446LogReposMon.saveAll(jcicZ446Log);	
      jcicZ446LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ446Log = jcicZ446LogReposHist.saveAll(jcicZ446Log);
      jcicZ446LogReposHist.flush();
    }
    else {
      jcicZ446Log = jcicZ446LogRepos.saveAll(jcicZ446Log);
      jcicZ446LogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicZ446Log> jcicZ446Log, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (jcicZ446Log == null || jcicZ446Log.size() == 0)
      throw new DBException(6);

    for (JcicZ446Log t : jcicZ446Log) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicZ446Log = jcicZ446LogReposDay.saveAll(jcicZ446Log);	
      jcicZ446LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ446Log = jcicZ446LogReposMon.saveAll(jcicZ446Log);	
      jcicZ446LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ446Log = jcicZ446LogReposHist.saveAll(jcicZ446Log);
      jcicZ446LogReposHist.flush();
    }
    else {
      jcicZ446Log = jcicZ446LogRepos.saveAll(jcicZ446Log);
      jcicZ446LogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicZ446Log> jcicZ446Log, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicZ446Log == null || jcicZ446Log.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicZ446LogReposDay.deleteAll(jcicZ446Log);	
      jcicZ446LogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicZ446LogReposMon.deleteAll(jcicZ446Log);	
      jcicZ446LogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicZ446LogReposHist.deleteAll(jcicZ446Log);
      jcicZ446LogReposHist.flush();
    }
    else {
      jcicZ446LogRepos.deleteAll(jcicZ446Log);
      jcicZ446LogRepos.flush();
    }
  }

}
