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
import com.st1.itx.db.domain.TxApLog;
import com.st1.itx.db.repository.online.TxApLogRepository;
import com.st1.itx.db.repository.day.TxApLogRepositoryDay;
import com.st1.itx.db.repository.mon.TxApLogRepositoryMon;
import com.st1.itx.db.repository.hist.TxApLogRepositoryHist;
import com.st1.itx.db.service.TxApLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txApLogService")
@Repository
public class TxApLogServiceImpl extends ASpringJpaParm implements TxApLogService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxApLogRepository txApLogRepos;

  @Autowired
  private TxApLogRepositoryDay txApLogReposDay;

  @Autowired
  private TxApLogRepositoryMon txApLogReposMon;

  @Autowired
  private TxApLogRepositoryHist txApLogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txApLogRepos);
    org.junit.Assert.assertNotNull(txApLogReposDay);
    org.junit.Assert.assertNotNull(txApLogReposMon);
    org.junit.Assert.assertNotNull(txApLogReposHist);
  }

  @Override
  public TxApLog findById(Long autoSeq, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + autoSeq);
    Optional<TxApLog> txApLog = null;
    if (dbName.equals(ContentName.onDay))
      txApLog = txApLogReposDay.findById(autoSeq);
    else if (dbName.equals(ContentName.onMon))
      txApLog = txApLogReposMon.findById(autoSeq);
    else if (dbName.equals(ContentName.onHist))
      txApLog = txApLogReposHist.findById(autoSeq);
    else 
      txApLog = txApLogRepos.findById(autoSeq);
    TxApLog obj = txApLog.isPresent() ? txApLog.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxApLog> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxApLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AutoSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AutoSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txApLogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txApLogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txApLogReposHist.findAll(pageable);
    else 
      slice = txApLogRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxApLog> findUserID(String userID_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxApLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findUserID " + dbName + " : " + "userID_0 : " + userID_0);
    if (dbName.equals(ContentName.onDay))
      slice = txApLogReposDay.findAllByUserIDIsOrderByUserIDAsc(userID_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txApLogReposMon.findAllByUserIDIsOrderByUserIDAsc(userID_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txApLogReposHist.findAllByUserIDIsOrderByUserIDAsc(userID_0, pageable);
    else 
      slice = txApLogRepos.findAllByUserIDIsOrderByUserIDAsc(userID_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxApLog> findEntdy(int entdy_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxApLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findEntdy " + dbName + " : " + "entdy_0 : " + entdy_0);
    if (dbName.equals(ContentName.onDay))
      slice = txApLogReposDay.findAllByEntdyIsOrderByEntdyAsc(entdy_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txApLogReposMon.findAllByEntdyIsOrderByEntdyAsc(entdy_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txApLogReposHist.findAllByEntdyIsOrderByEntdyAsc(entdy_0, pageable);
    else 
      slice = txApLogRepos.findAllByEntdyIsOrderByEntdyAsc(entdy_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxApLog> findEntdyAndUserID(int entdy_0, String userID_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxApLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findEntdyAndUserID " + dbName + " : " + "entdy_0 : " + entdy_0 + " userID_1 : " +  userID_1);
    if (dbName.equals(ContentName.onDay))
      slice = txApLogReposDay.findAllByEntdyIsAndUserIDIsOrderByEntdyAscUserIDAsc(entdy_0, userID_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txApLogReposMon.findAllByEntdyIsAndUserIDIsOrderByEntdyAscUserIDAsc(entdy_0, userID_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txApLogReposHist.findAllByEntdyIsAndUserIDIsOrderByEntdyAscUserIDAsc(entdy_0, userID_1, pageable);
    else 
      slice = txApLogRepos.findAllByEntdyIsAndUserIDIsOrderByEntdyAscUserIDAsc(entdy_0, userID_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxApLog holdById(Long autoSeq, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + autoSeq);
    Optional<TxApLog> txApLog = null;
    if (dbName.equals(ContentName.onDay))
      txApLog = txApLogReposDay.findByAutoSeq(autoSeq);
    else if (dbName.equals(ContentName.onMon))
      txApLog = txApLogReposMon.findByAutoSeq(autoSeq);
    else if (dbName.equals(ContentName.onHist))
      txApLog = txApLogReposHist.findByAutoSeq(autoSeq);
    else 
      txApLog = txApLogRepos.findByAutoSeq(autoSeq);
    return txApLog.isPresent() ? txApLog.get() : null;
  }

  @Override
  public TxApLog holdById(TxApLog txApLog, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txApLog.getAutoSeq());
    Optional<TxApLog> txApLogT = null;
    if (dbName.equals(ContentName.onDay))
      txApLogT = txApLogReposDay.findByAutoSeq(txApLog.getAutoSeq());
    else if (dbName.equals(ContentName.onMon))
      txApLogT = txApLogReposMon.findByAutoSeq(txApLog.getAutoSeq());
    else if (dbName.equals(ContentName.onHist))
      txApLogT = txApLogReposHist.findByAutoSeq(txApLog.getAutoSeq());
    else 
      txApLogT = txApLogRepos.findByAutoSeq(txApLog.getAutoSeq());
    return txApLogT.isPresent() ? txApLogT.get() : null;
  }

  @Override
  public TxApLog insert(TxApLog txApLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txApLog.getAutoSeq());
    if (this.findById(txApLog.getAutoSeq(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txApLog.setCreateEmpNo(empNot);

    if(txApLog.getLastUpdateEmpNo() == null || txApLog.getLastUpdateEmpNo().isEmpty())
      txApLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txApLogReposDay.saveAndFlush(txApLog);	
    else if (dbName.equals(ContentName.onMon))
      return txApLogReposMon.saveAndFlush(txApLog);
    else if (dbName.equals(ContentName.onHist))
      return txApLogReposHist.saveAndFlush(txApLog);
    else 
    return txApLogRepos.saveAndFlush(txApLog);
  }

  @Override
  public TxApLog update(TxApLog txApLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txApLog.getAutoSeq());
    if (!empNot.isEmpty())
      txApLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txApLogReposDay.saveAndFlush(txApLog);	
    else if (dbName.equals(ContentName.onMon))
      return txApLogReposMon.saveAndFlush(txApLog);
    else if (dbName.equals(ContentName.onHist))
      return txApLogReposHist.saveAndFlush(txApLog);
    else 
    return txApLogRepos.saveAndFlush(txApLog);
  }

  @Override
  public TxApLog update2(TxApLog txApLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txApLog.getAutoSeq());
    if (!empNot.isEmpty())
      txApLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txApLogReposDay.saveAndFlush(txApLog);	
    else if (dbName.equals(ContentName.onMon))
      txApLogReposMon.saveAndFlush(txApLog);
    else if (dbName.equals(ContentName.onHist))
        txApLogReposHist.saveAndFlush(txApLog);
    else 
      txApLogRepos.saveAndFlush(txApLog);	
    return this.findById(txApLog.getAutoSeq());
  }

  @Override
  public void delete(TxApLog txApLog, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txApLog.getAutoSeq());
    if (dbName.equals(ContentName.onDay)) {
      txApLogReposDay.delete(txApLog);	
      txApLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txApLogReposMon.delete(txApLog);	
      txApLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txApLogReposHist.delete(txApLog);
      txApLogReposHist.flush();
    }
    else {
      txApLogRepos.delete(txApLog);
      txApLogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxApLog> txApLog, TitaVo... titaVo) throws DBException {
    if (txApLog == null || txApLog.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxApLog t : txApLog){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txApLog = txApLogReposDay.saveAll(txApLog);	
      txApLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txApLog = txApLogReposMon.saveAll(txApLog);	
      txApLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txApLog = txApLogReposHist.saveAll(txApLog);
      txApLogReposHist.flush();
    }
    else {
      txApLog = txApLogRepos.saveAll(txApLog);
      txApLogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxApLog> txApLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txApLog == null || txApLog.size() == 0)
      throw new DBException(6);

    for (TxApLog t : txApLog) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txApLog = txApLogReposDay.saveAll(txApLog);	
      txApLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txApLog = txApLogReposMon.saveAll(txApLog);	
      txApLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txApLog = txApLogReposHist.saveAll(txApLog);
      txApLogReposHist.flush();
    }
    else {
      txApLog = txApLogRepos.saveAll(txApLog);
      txApLogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxApLog> txApLog, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txApLog == null || txApLog.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txApLogReposDay.deleteAll(txApLog);	
      txApLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txApLogReposMon.deleteAll(txApLog);	
      txApLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txApLogReposHist.deleteAll(txApLog);
      txApLogReposHist.flush();
    }
    else {
      txApLogRepos.deleteAll(txApLog);
      txApLogRepos.flush();
    }
  }

}
