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
import com.st1.itx.db.domain.PfCoOfficerLog;
import com.st1.itx.db.domain.PfCoOfficerLogId;
import com.st1.itx.db.repository.online.PfCoOfficerLogRepository;
import com.st1.itx.db.repository.day.PfCoOfficerLogRepositoryDay;
import com.st1.itx.db.repository.mon.PfCoOfficerLogRepositoryMon;
import com.st1.itx.db.repository.hist.PfCoOfficerLogRepositoryHist;
import com.st1.itx.db.service.PfCoOfficerLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("pfCoOfficerLogService")
@Repository
public class PfCoOfficerLogServiceImpl extends ASpringJpaParm implements PfCoOfficerLogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private PfCoOfficerLogRepository pfCoOfficerLogRepos;

  @Autowired
  private PfCoOfficerLogRepositoryDay pfCoOfficerLogReposDay;

  @Autowired
  private PfCoOfficerLogRepositoryMon pfCoOfficerLogReposMon;

  @Autowired
  private PfCoOfficerLogRepositoryHist pfCoOfficerLogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(pfCoOfficerLogRepos);
    org.junit.Assert.assertNotNull(pfCoOfficerLogReposDay);
    org.junit.Assert.assertNotNull(pfCoOfficerLogReposMon);
    org.junit.Assert.assertNotNull(pfCoOfficerLogReposHist);
  }

  @Override
  public PfCoOfficerLog findById(PfCoOfficerLogId pfCoOfficerLogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + pfCoOfficerLogId);
    Optional<PfCoOfficerLog> pfCoOfficerLog = null;
    if (dbName.equals(ContentName.onDay))
      pfCoOfficerLog = pfCoOfficerLogReposDay.findById(pfCoOfficerLogId);
    else if (dbName.equals(ContentName.onMon))
      pfCoOfficerLog = pfCoOfficerLogReposMon.findById(pfCoOfficerLogId);
    else if (dbName.equals(ContentName.onHist))
      pfCoOfficerLog = pfCoOfficerLogReposHist.findById(pfCoOfficerLogId);
    else 
      pfCoOfficerLog = pfCoOfficerLogRepos.findById(pfCoOfficerLogId);
    PfCoOfficerLog obj = pfCoOfficerLog.isPresent() ? pfCoOfficerLog.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<PfCoOfficerLog> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfCoOfficerLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "EmpNo", "EffectiveDate", "SerialNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EmpNo", "EffectiveDate", "SerialNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = pfCoOfficerLogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfCoOfficerLogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfCoOfficerLogReposHist.findAll(pageable);
    else 
      slice = pfCoOfficerLogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<PfCoOfficerLog> otherEq(String empNo_0, int effectiveDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<PfCoOfficerLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("otherEq " + dbName + " : " + "empNo_0 : " + empNo_0 + " effectiveDate_1 : " +  effectiveDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = pfCoOfficerLogReposDay.findAllByEmpNoIsAndEffectiveDateIsOrderBySerialNoAsc(empNo_0, effectiveDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = pfCoOfficerLogReposMon.findAllByEmpNoIsAndEffectiveDateIsOrderBySerialNoAsc(empNo_0, effectiveDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = pfCoOfficerLogReposHist.findAllByEmpNoIsAndEffectiveDateIsOrderBySerialNoAsc(empNo_0, effectiveDate_1, pageable);
    else 
      slice = pfCoOfficerLogRepos.findAllByEmpNoIsAndEffectiveDateIsOrderBySerialNoAsc(empNo_0, effectiveDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public PfCoOfficerLog holdById(PfCoOfficerLogId pfCoOfficerLogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfCoOfficerLogId);
    Optional<PfCoOfficerLog> pfCoOfficerLog = null;
    if (dbName.equals(ContentName.onDay))
      pfCoOfficerLog = pfCoOfficerLogReposDay.findByPfCoOfficerLogId(pfCoOfficerLogId);
    else if (dbName.equals(ContentName.onMon))
      pfCoOfficerLog = pfCoOfficerLogReposMon.findByPfCoOfficerLogId(pfCoOfficerLogId);
    else if (dbName.equals(ContentName.onHist))
      pfCoOfficerLog = pfCoOfficerLogReposHist.findByPfCoOfficerLogId(pfCoOfficerLogId);
    else 
      pfCoOfficerLog = pfCoOfficerLogRepos.findByPfCoOfficerLogId(pfCoOfficerLogId);
    return pfCoOfficerLog.isPresent() ? pfCoOfficerLog.get() : null;
  }

  @Override
  public PfCoOfficerLog holdById(PfCoOfficerLog pfCoOfficerLog, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + pfCoOfficerLog.getPfCoOfficerLogId());
    Optional<PfCoOfficerLog> pfCoOfficerLogT = null;
    if (dbName.equals(ContentName.onDay))
      pfCoOfficerLogT = pfCoOfficerLogReposDay.findByPfCoOfficerLogId(pfCoOfficerLog.getPfCoOfficerLogId());
    else if (dbName.equals(ContentName.onMon))
      pfCoOfficerLogT = pfCoOfficerLogReposMon.findByPfCoOfficerLogId(pfCoOfficerLog.getPfCoOfficerLogId());
    else if (dbName.equals(ContentName.onHist))
      pfCoOfficerLogT = pfCoOfficerLogReposHist.findByPfCoOfficerLogId(pfCoOfficerLog.getPfCoOfficerLogId());
    else 
      pfCoOfficerLogT = pfCoOfficerLogRepos.findByPfCoOfficerLogId(pfCoOfficerLog.getPfCoOfficerLogId());
    return pfCoOfficerLogT.isPresent() ? pfCoOfficerLogT.get() : null;
  }

  @Override
  public PfCoOfficerLog insert(PfCoOfficerLog pfCoOfficerLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + pfCoOfficerLog.getPfCoOfficerLogId());
    if (this.findById(pfCoOfficerLog.getPfCoOfficerLogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      pfCoOfficerLog.setCreateEmpNo(empNot);

    if(pfCoOfficerLog.getLastUpdateEmpNo() == null || pfCoOfficerLog.getLastUpdateEmpNo().isEmpty())
      pfCoOfficerLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfCoOfficerLogReposDay.saveAndFlush(pfCoOfficerLog);	
    else if (dbName.equals(ContentName.onMon))
      return pfCoOfficerLogReposMon.saveAndFlush(pfCoOfficerLog);
    else if (dbName.equals(ContentName.onHist))
      return pfCoOfficerLogReposHist.saveAndFlush(pfCoOfficerLog);
    else 
    return pfCoOfficerLogRepos.saveAndFlush(pfCoOfficerLog);
  }

  @Override
  public PfCoOfficerLog update(PfCoOfficerLog pfCoOfficerLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfCoOfficerLog.getPfCoOfficerLogId());
    if (!empNot.isEmpty())
      pfCoOfficerLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return pfCoOfficerLogReposDay.saveAndFlush(pfCoOfficerLog);	
    else if (dbName.equals(ContentName.onMon))
      return pfCoOfficerLogReposMon.saveAndFlush(pfCoOfficerLog);
    else if (dbName.equals(ContentName.onHist))
      return pfCoOfficerLogReposHist.saveAndFlush(pfCoOfficerLog);
    else 
    return pfCoOfficerLogRepos.saveAndFlush(pfCoOfficerLog);
  }

  @Override
  public PfCoOfficerLog update2(PfCoOfficerLog pfCoOfficerLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + pfCoOfficerLog.getPfCoOfficerLogId());
    if (!empNot.isEmpty())
      pfCoOfficerLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      pfCoOfficerLogReposDay.saveAndFlush(pfCoOfficerLog);	
    else if (dbName.equals(ContentName.onMon))
      pfCoOfficerLogReposMon.saveAndFlush(pfCoOfficerLog);
    else if (dbName.equals(ContentName.onHist))
        pfCoOfficerLogReposHist.saveAndFlush(pfCoOfficerLog);
    else 
      pfCoOfficerLogRepos.saveAndFlush(pfCoOfficerLog);	
    return this.findById(pfCoOfficerLog.getPfCoOfficerLogId());
  }

  @Override
  public void delete(PfCoOfficerLog pfCoOfficerLog, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + pfCoOfficerLog.getPfCoOfficerLogId());
    if (dbName.equals(ContentName.onDay)) {
      pfCoOfficerLogReposDay.delete(pfCoOfficerLog);	
      pfCoOfficerLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfCoOfficerLogReposMon.delete(pfCoOfficerLog);	
      pfCoOfficerLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfCoOfficerLogReposHist.delete(pfCoOfficerLog);
      pfCoOfficerLogReposHist.flush();
    }
    else {
      pfCoOfficerLogRepos.delete(pfCoOfficerLog);
      pfCoOfficerLogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<PfCoOfficerLog> pfCoOfficerLog, TitaVo... titaVo) throws DBException {
    if (pfCoOfficerLog == null || pfCoOfficerLog.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (PfCoOfficerLog t : pfCoOfficerLog){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      pfCoOfficerLog = pfCoOfficerLogReposDay.saveAll(pfCoOfficerLog);	
      pfCoOfficerLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfCoOfficerLog = pfCoOfficerLogReposMon.saveAll(pfCoOfficerLog);	
      pfCoOfficerLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfCoOfficerLog = pfCoOfficerLogReposHist.saveAll(pfCoOfficerLog);
      pfCoOfficerLogReposHist.flush();
    }
    else {
      pfCoOfficerLog = pfCoOfficerLogRepos.saveAll(pfCoOfficerLog);
      pfCoOfficerLogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<PfCoOfficerLog> pfCoOfficerLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (pfCoOfficerLog == null || pfCoOfficerLog.size() == 0)
      throw new DBException(6);

    for (PfCoOfficerLog t : pfCoOfficerLog) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      pfCoOfficerLog = pfCoOfficerLogReposDay.saveAll(pfCoOfficerLog);	
      pfCoOfficerLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfCoOfficerLog = pfCoOfficerLogReposMon.saveAll(pfCoOfficerLog);	
      pfCoOfficerLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfCoOfficerLog = pfCoOfficerLogReposHist.saveAll(pfCoOfficerLog);
      pfCoOfficerLogReposHist.flush();
    }
    else {
      pfCoOfficerLog = pfCoOfficerLogRepos.saveAll(pfCoOfficerLog);
      pfCoOfficerLogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<PfCoOfficerLog> pfCoOfficerLog, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (pfCoOfficerLog == null || pfCoOfficerLog.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      pfCoOfficerLogReposDay.deleteAll(pfCoOfficerLog);	
      pfCoOfficerLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      pfCoOfficerLogReposMon.deleteAll(pfCoOfficerLog);	
      pfCoOfficerLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      pfCoOfficerLogReposHist.deleteAll(pfCoOfficerLog);
      pfCoOfficerLogReposHist.flush();
    }
    else {
      pfCoOfficerLogRepos.deleteAll(pfCoOfficerLog);
      pfCoOfficerLogRepos.flush();
    }
  }

}
