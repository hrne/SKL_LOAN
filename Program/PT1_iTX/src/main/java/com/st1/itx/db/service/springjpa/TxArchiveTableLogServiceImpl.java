package com.st1.itx.db.service.springjpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

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
import com.st1.itx.db.domain.TxArchiveTableLog;
import com.st1.itx.db.repository.online.TxArchiveTableLogRepository;
import com.st1.itx.db.repository.day.TxArchiveTableLogRepositoryDay;
import com.st1.itx.db.repository.mon.TxArchiveTableLogRepositoryMon;
import com.st1.itx.db.repository.hist.TxArchiveTableLogRepositoryHist;
import com.st1.itx.db.service.TxArchiveTableLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txArchiveTableLogService")
@Repository
public class TxArchiveTableLogServiceImpl extends ASpringJpaParm implements TxArchiveTableLogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxArchiveTableLogRepository txArchiveTableLogRepos;

  @Autowired
  private TxArchiveTableLogRepositoryDay txArchiveTableLogReposDay;

  @Autowired
  private TxArchiveTableLogRepositoryMon txArchiveTableLogReposMon;

  @Autowired
  private TxArchiveTableLogRepositoryHist txArchiveTableLogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txArchiveTableLogRepos);
    org.junit.Assert.assertNotNull(txArchiveTableLogReposDay);
    org.junit.Assert.assertNotNull(txArchiveTableLogReposMon);
    org.junit.Assert.assertNotNull(txArchiveTableLogReposHist);
  }

  @Override
  public TxArchiveTableLog findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<TxArchiveTableLog> txArchiveTableLog = null;
    if (dbName.equals(ContentName.onDay))
      txArchiveTableLog = txArchiveTableLogReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      txArchiveTableLog = txArchiveTableLogReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      txArchiveTableLog = txArchiveTableLogReposHist.findById(logNo);
    else 
      txArchiveTableLog = txArchiveTableLogRepos.findById(logNo);
    TxArchiveTableLog obj = txArchiveTableLog.isPresent() ? txArchiveTableLog.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxArchiveTableLog> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxArchiveTableLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txArchiveTableLogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txArchiveTableLogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txArchiveTableLogReposHist.findAll(pageable);
    else 
      slice = txArchiveTableLogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxArchiveTableLog> findLogs(String type_0, String tableName_1, int executeDate_2, String dataFrom_3, String dataTo_4, int batchNo_5, int custNo_6, int facmNo_7, int bormNo_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxArchiveTableLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findLogs " + dbName + " : " + "type_0 : " + type_0 + " tableName_1 : " +  tableName_1 + " executeDate_2 : " +  executeDate_2 + " dataFrom_3 : " +  dataFrom_3 + " dataTo_4 : " +  dataTo_4 + " batchNo_5 : " +  batchNo_5 + " custNo_6 : " +  custNo_6 + " facmNo_7 : " +  facmNo_7 + " bormNo_8 : " +  bormNo_8);
    if (dbName.equals(ContentName.onDay))
      slice = txArchiveTableLogReposDay.findAllByTypeIsAndTableNameIsAndExecuteDateIsAndDataFromIsAndDataToIsAndBatchNoIsAndCustNoIsAndFacmNoIsAndBormNoIs(type_0, tableName_1, executeDate_2, dataFrom_3, dataTo_4, batchNo_5, custNo_6, facmNo_7, bormNo_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txArchiveTableLogReposMon.findAllByTypeIsAndTableNameIsAndExecuteDateIsAndDataFromIsAndDataToIsAndBatchNoIsAndCustNoIsAndFacmNoIsAndBormNoIs(type_0, tableName_1, executeDate_2, dataFrom_3, dataTo_4, batchNo_5, custNo_6, facmNo_7, bormNo_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txArchiveTableLogReposHist.findAllByTypeIsAndTableNameIsAndExecuteDateIsAndDataFromIsAndDataToIsAndBatchNoIsAndCustNoIsAndFacmNoIsAndBormNoIs(type_0, tableName_1, executeDate_2, dataFrom_3, dataTo_4, batchNo_5, custNo_6, facmNo_7, bormNo_8, pageable);
    else 
      slice = txArchiveTableLogRepos.findAllByTypeIsAndTableNameIsAndExecuteDateIsAndDataFromIsAndDataToIsAndBatchNoIsAndCustNoIsAndFacmNoIsAndBormNoIs(type_0, tableName_1, executeDate_2, dataFrom_3, dataTo_4, batchNo_5, custNo_6, facmNo_7, bormNo_8, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxArchiveTableLog holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<TxArchiveTableLog> txArchiveTableLog = null;
    if (dbName.equals(ContentName.onDay))
      txArchiveTableLog = txArchiveTableLogReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      txArchiveTableLog = txArchiveTableLogReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      txArchiveTableLog = txArchiveTableLogReposHist.findByLogNo(logNo);
    else 
      txArchiveTableLog = txArchiveTableLogRepos.findByLogNo(logNo);
    return txArchiveTableLog.isPresent() ? txArchiveTableLog.get() : null;
  }

  @Override
  public TxArchiveTableLog holdById(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txArchiveTableLog.getLogNo());
    Optional<TxArchiveTableLog> txArchiveTableLogT = null;
    if (dbName.equals(ContentName.onDay))
      txArchiveTableLogT = txArchiveTableLogReposDay.findByLogNo(txArchiveTableLog.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      txArchiveTableLogT = txArchiveTableLogReposMon.findByLogNo(txArchiveTableLog.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      txArchiveTableLogT = txArchiveTableLogReposHist.findByLogNo(txArchiveTableLog.getLogNo());
    else 
      txArchiveTableLogT = txArchiveTableLogRepos.findByLogNo(txArchiveTableLog.getLogNo());
    return txArchiveTableLogT.isPresent() ? txArchiveTableLogT.get() : null;
  }

  @Override
  public TxArchiveTableLog insert(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txArchiveTableLog.getLogNo());
    if (this.findById(txArchiveTableLog.getLogNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txArchiveTableLog.setCreateEmpNo(empNot);

    if(txArchiveTableLog.getLastUpdateEmpNo() == null || txArchiveTableLog.getLastUpdateEmpNo().isEmpty())
      txArchiveTableLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txArchiveTableLogReposDay.saveAndFlush(txArchiveTableLog);	
    else if (dbName.equals(ContentName.onMon))
      return txArchiveTableLogReposMon.saveAndFlush(txArchiveTableLog);
    else if (dbName.equals(ContentName.onHist))
      return txArchiveTableLogReposHist.saveAndFlush(txArchiveTableLog);
    else 
    return txArchiveTableLogRepos.saveAndFlush(txArchiveTableLog);
  }

  @Override
  public TxArchiveTableLog update(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txArchiveTableLog.getLogNo());
    if (!empNot.isEmpty())
      txArchiveTableLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txArchiveTableLogReposDay.saveAndFlush(txArchiveTableLog);	
    else if (dbName.equals(ContentName.onMon))
      return txArchiveTableLogReposMon.saveAndFlush(txArchiveTableLog);
    else if (dbName.equals(ContentName.onHist))
      return txArchiveTableLogReposHist.saveAndFlush(txArchiveTableLog);
    else 
    return txArchiveTableLogRepos.saveAndFlush(txArchiveTableLog);
  }

  @Override
  public TxArchiveTableLog update2(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txArchiveTableLog.getLogNo());
    if (!empNot.isEmpty())
      txArchiveTableLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txArchiveTableLogReposDay.saveAndFlush(txArchiveTableLog);	
    else if (dbName.equals(ContentName.onMon))
      txArchiveTableLogReposMon.saveAndFlush(txArchiveTableLog);
    else if (dbName.equals(ContentName.onHist))
        txArchiveTableLogReposHist.saveAndFlush(txArchiveTableLog);
    else 
      txArchiveTableLogRepos.saveAndFlush(txArchiveTableLog);	
    return this.findById(txArchiveTableLog.getLogNo());
  }

  @Override
  public void delete(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txArchiveTableLog.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      txArchiveTableLogReposDay.delete(txArchiveTableLog);	
      txArchiveTableLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txArchiveTableLogReposMon.delete(txArchiveTableLog);	
      txArchiveTableLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txArchiveTableLogReposHist.delete(txArchiveTableLog);
      txArchiveTableLogReposHist.flush();
    }
    else {
      txArchiveTableLogRepos.delete(txArchiveTableLog);
      txArchiveTableLogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException {
    if (txArchiveTableLog == null || txArchiveTableLog.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxArchiveTableLog t : txArchiveTableLog){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txArchiveTableLog = txArchiveTableLogReposDay.saveAll(txArchiveTableLog);	
      txArchiveTableLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txArchiveTableLog = txArchiveTableLogReposMon.saveAll(txArchiveTableLog);	
      txArchiveTableLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txArchiveTableLog = txArchiveTableLogReposHist.saveAll(txArchiveTableLog);
      txArchiveTableLogReposHist.flush();
    }
    else {
      txArchiveTableLog = txArchiveTableLogRepos.saveAll(txArchiveTableLog);
      txArchiveTableLogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txArchiveTableLog == null || txArchiveTableLog.size() == 0)
      throw new DBException(6);

    for (TxArchiveTableLog t : txArchiveTableLog) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txArchiveTableLog = txArchiveTableLogReposDay.saveAll(txArchiveTableLog);	
      txArchiveTableLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txArchiveTableLog = txArchiveTableLogReposMon.saveAll(txArchiveTableLog);	
      txArchiveTableLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txArchiveTableLog = txArchiveTableLogReposHist.saveAll(txArchiveTableLog);
      txArchiveTableLogReposHist.flush();
    }
    else {
      txArchiveTableLog = txArchiveTableLogRepos.saveAll(txArchiveTableLog);
      txArchiveTableLogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txArchiveTableLog == null || txArchiveTableLog.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txArchiveTableLogReposDay.deleteAll(txArchiveTableLog);	
      txArchiveTableLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txArchiveTableLogReposMon.deleteAll(txArchiveTableLog);	
      txArchiveTableLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txArchiveTableLogReposHist.deleteAll(txArchiveTableLog);
      txArchiveTableLogReposHist.flush();
    }
    else {
      txArchiveTableLogRepos.deleteAll(txArchiveTableLog);
      txArchiveTableLogRepos.flush();
    }
  }

}
