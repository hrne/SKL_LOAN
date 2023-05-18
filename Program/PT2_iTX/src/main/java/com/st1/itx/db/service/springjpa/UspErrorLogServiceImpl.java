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
import com.st1.itx.db.domain.UspErrorLog;
import com.st1.itx.db.repository.online.UspErrorLogRepository;
import com.st1.itx.db.repository.day.UspErrorLogRepositoryDay;
import com.st1.itx.db.repository.mon.UspErrorLogRepositoryMon;
import com.st1.itx.db.repository.hist.UspErrorLogRepositoryHist;
import com.st1.itx.db.service.UspErrorLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("uspErrorLogService")
@Repository
public class UspErrorLogServiceImpl extends ASpringJpaParm implements UspErrorLogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private UspErrorLogRepository uspErrorLogRepos;

  @Autowired
  private UspErrorLogRepositoryDay uspErrorLogReposDay;

  @Autowired
  private UspErrorLogRepositoryMon uspErrorLogReposMon;

  @Autowired
  private UspErrorLogRepositoryHist uspErrorLogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(uspErrorLogRepos);
    org.junit.Assert.assertNotNull(uspErrorLogReposDay);
    org.junit.Assert.assertNotNull(uspErrorLogReposMon);
    org.junit.Assert.assertNotNull(uspErrorLogReposHist);
  }

  @Override
  public UspErrorLog findById(String logUkey, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logUkey);
    Optional<UspErrorLog> uspErrorLog = null;
    if (dbName.equals(ContentName.onDay))
      uspErrorLog = uspErrorLogReposDay.findById(logUkey);
    else if (dbName.equals(ContentName.onMon))
      uspErrorLog = uspErrorLogReposMon.findById(logUkey);
    else if (dbName.equals(ContentName.onHist))
      uspErrorLog = uspErrorLogReposHist.findById(logUkey);
    else 
      uspErrorLog = uspErrorLogRepos.findById(logUkey);
    UspErrorLog obj = uspErrorLog.isPresent() ? uspErrorLog.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<UspErrorLog> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<UspErrorLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogUkey"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogUkey"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = uspErrorLogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = uspErrorLogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = uspErrorLogReposHist.findAll(pageable);
    else 
      slice = uspErrorLogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<UspErrorLog> findByLogDate(int logDate_0, int logDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<UspErrorLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByLogDate " + dbName + " : " + "logDate_0 : " + logDate_0 + " logDate_1 : " +  logDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = uspErrorLogReposDay.findAllByLogDateGreaterThanEqualAndLogDateLessThanEqualOrderByLogDateDescLogTimeDescUspNameDesc(logDate_0, logDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = uspErrorLogReposMon.findAllByLogDateGreaterThanEqualAndLogDateLessThanEqualOrderByLogDateDescLogTimeDescUspNameDesc(logDate_0, logDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = uspErrorLogReposHist.findAllByLogDateGreaterThanEqualAndLogDateLessThanEqualOrderByLogDateDescLogTimeDescUspNameDesc(logDate_0, logDate_1, pageable);
    else 
      slice = uspErrorLogRepos.findAllByLogDateGreaterThanEqualAndLogDateLessThanEqualOrderByLogDateDescLogTimeDescUspNameDesc(logDate_0, logDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<UspErrorLog> findByLogDateAndUspName(int logDate_0, int logDate_1, String uspName_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<UspErrorLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByLogDateAndUspName " + dbName + " : " + "logDate_0 : " + logDate_0 + " logDate_1 : " +  logDate_1 + " uspName_2 : " +  uspName_2);
    if (dbName.equals(ContentName.onDay))
      slice = uspErrorLogReposDay.findAllByLogDateGreaterThanEqualAndLogDateLessThanEqualAndUspNameLikeOrderByLogDateDescLogTimeDescUspNameDesc(logDate_0, logDate_1, uspName_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = uspErrorLogReposMon.findAllByLogDateGreaterThanEqualAndLogDateLessThanEqualAndUspNameLikeOrderByLogDateDescLogTimeDescUspNameDesc(logDate_0, logDate_1, uspName_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = uspErrorLogReposHist.findAllByLogDateGreaterThanEqualAndLogDateLessThanEqualAndUspNameLikeOrderByLogDateDescLogTimeDescUspNameDesc(logDate_0, logDate_1, uspName_2, pageable);
    else 
      slice = uspErrorLogRepos.findAllByLogDateGreaterThanEqualAndLogDateLessThanEqualAndUspNameLikeOrderByLogDateDescLogTimeDescUspNameDesc(logDate_0, logDate_1, uspName_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public UspErrorLog holdById(String logUkey, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logUkey);
    Optional<UspErrorLog> uspErrorLog = null;
    if (dbName.equals(ContentName.onDay))
      uspErrorLog = uspErrorLogReposDay.findByLogUkey(logUkey);
    else if (dbName.equals(ContentName.onMon))
      uspErrorLog = uspErrorLogReposMon.findByLogUkey(logUkey);
    else if (dbName.equals(ContentName.onHist))
      uspErrorLog = uspErrorLogReposHist.findByLogUkey(logUkey);
    else 
      uspErrorLog = uspErrorLogRepos.findByLogUkey(logUkey);
    return uspErrorLog.isPresent() ? uspErrorLog.get() : null;
  }

  @Override
  public UspErrorLog holdById(UspErrorLog uspErrorLog, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + uspErrorLog.getLogUkey());
    Optional<UspErrorLog> uspErrorLogT = null;
    if (dbName.equals(ContentName.onDay))
      uspErrorLogT = uspErrorLogReposDay.findByLogUkey(uspErrorLog.getLogUkey());
    else if (dbName.equals(ContentName.onMon))
      uspErrorLogT = uspErrorLogReposMon.findByLogUkey(uspErrorLog.getLogUkey());
    else if (dbName.equals(ContentName.onHist))
      uspErrorLogT = uspErrorLogReposHist.findByLogUkey(uspErrorLog.getLogUkey());
    else 
      uspErrorLogT = uspErrorLogRepos.findByLogUkey(uspErrorLog.getLogUkey());
    return uspErrorLogT.isPresent() ? uspErrorLogT.get() : null;
  }

  @Override
  public UspErrorLog insert(UspErrorLog uspErrorLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + uspErrorLog.getLogUkey());
    if (this.findById(uspErrorLog.getLogUkey(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      uspErrorLog.setCreateEmpNo(empNot);

    if(uspErrorLog.getLastUpdateEmpNo() == null || uspErrorLog.getLastUpdateEmpNo().isEmpty())
      uspErrorLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return uspErrorLogReposDay.saveAndFlush(uspErrorLog);	
    else if (dbName.equals(ContentName.onMon))
      return uspErrorLogReposMon.saveAndFlush(uspErrorLog);
    else if (dbName.equals(ContentName.onHist))
      return uspErrorLogReposHist.saveAndFlush(uspErrorLog);
    else 
    return uspErrorLogRepos.saveAndFlush(uspErrorLog);
  }

  @Override
  public UspErrorLog update(UspErrorLog uspErrorLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + uspErrorLog.getLogUkey());
    if (!empNot.isEmpty())
      uspErrorLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return uspErrorLogReposDay.saveAndFlush(uspErrorLog);	
    else if (dbName.equals(ContentName.onMon))
      return uspErrorLogReposMon.saveAndFlush(uspErrorLog);
    else if (dbName.equals(ContentName.onHist))
      return uspErrorLogReposHist.saveAndFlush(uspErrorLog);
    else 
    return uspErrorLogRepos.saveAndFlush(uspErrorLog);
  }

  @Override
  public UspErrorLog update2(UspErrorLog uspErrorLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + uspErrorLog.getLogUkey());
    if (!empNot.isEmpty())
      uspErrorLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      uspErrorLogReposDay.saveAndFlush(uspErrorLog);	
    else if (dbName.equals(ContentName.onMon))
      uspErrorLogReposMon.saveAndFlush(uspErrorLog);
    else if (dbName.equals(ContentName.onHist))
        uspErrorLogReposHist.saveAndFlush(uspErrorLog);
    else 
      uspErrorLogRepos.saveAndFlush(uspErrorLog);	
    return this.findById(uspErrorLog.getLogUkey());
  }

  @Override
  public void delete(UspErrorLog uspErrorLog, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + uspErrorLog.getLogUkey());
    if (dbName.equals(ContentName.onDay)) {
      uspErrorLogReposDay.delete(uspErrorLog);	
      uspErrorLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      uspErrorLogReposMon.delete(uspErrorLog);	
      uspErrorLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      uspErrorLogReposHist.delete(uspErrorLog);
      uspErrorLogReposHist.flush();
    }
    else {
      uspErrorLogRepos.delete(uspErrorLog);
      uspErrorLogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<UspErrorLog> uspErrorLog, TitaVo... titaVo) throws DBException {
    if (uspErrorLog == null || uspErrorLog.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (UspErrorLog t : uspErrorLog){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      uspErrorLog = uspErrorLogReposDay.saveAll(uspErrorLog);	
      uspErrorLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      uspErrorLog = uspErrorLogReposMon.saveAll(uspErrorLog);	
      uspErrorLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      uspErrorLog = uspErrorLogReposHist.saveAll(uspErrorLog);
      uspErrorLogReposHist.flush();
    }
    else {
      uspErrorLog = uspErrorLogRepos.saveAll(uspErrorLog);
      uspErrorLogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<UspErrorLog> uspErrorLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (uspErrorLog == null || uspErrorLog.size() == 0)
      throw new DBException(6);

    for (UspErrorLog t : uspErrorLog) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      uspErrorLog = uspErrorLogReposDay.saveAll(uspErrorLog);	
      uspErrorLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      uspErrorLog = uspErrorLogReposMon.saveAll(uspErrorLog);	
      uspErrorLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      uspErrorLog = uspErrorLogReposHist.saveAll(uspErrorLog);
      uspErrorLogReposHist.flush();
    }
    else {
      uspErrorLog = uspErrorLogRepos.saveAll(uspErrorLog);
      uspErrorLogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<UspErrorLog> uspErrorLog, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (uspErrorLog == null || uspErrorLog.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      uspErrorLogReposDay.deleteAll(uspErrorLog);	
      uspErrorLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      uspErrorLogReposMon.deleteAll(uspErrorLog);	
      uspErrorLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      uspErrorLogReposHist.deleteAll(uspErrorLog);
      uspErrorLogReposHist.flush();
    }
    else {
      uspErrorLogRepos.deleteAll(uspErrorLog);
      uspErrorLogRepos.flush();
    }
  }

}
