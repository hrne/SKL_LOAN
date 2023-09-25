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
import com.st1.itx.db.domain.TxPrinter;
import com.st1.itx.db.domain.TxPrinterId;
import com.st1.itx.db.repository.online.TxPrinterRepository;
import com.st1.itx.db.repository.day.TxPrinterRepositoryDay;
import com.st1.itx.db.repository.mon.TxPrinterRepositoryMon;
import com.st1.itx.db.repository.hist.TxPrinterRepositoryHist;
import com.st1.itx.db.service.TxPrinterService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txPrinterService")
@Repository
public class TxPrinterServiceImpl extends ASpringJpaParm implements TxPrinterService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxPrinterRepository txPrinterRepos;

  @Autowired
  private TxPrinterRepositoryDay txPrinterReposDay;

  @Autowired
  private TxPrinterRepositoryMon txPrinterReposMon;

  @Autowired
  private TxPrinterRepositoryHist txPrinterReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txPrinterRepos);
    org.junit.Assert.assertNotNull(txPrinterReposDay);
    org.junit.Assert.assertNotNull(txPrinterReposMon);
    org.junit.Assert.assertNotNull(txPrinterReposHist);
  }

  @Override
  public TxPrinter findById(TxPrinterId txPrinterId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + txPrinterId);
    Optional<TxPrinter> txPrinter = null;
    if (dbName.equals(ContentName.onDay))
      txPrinter = txPrinterReposDay.findById(txPrinterId);
    else if (dbName.equals(ContentName.onMon))
      txPrinter = txPrinterReposMon.findById(txPrinterId);
    else if (dbName.equals(ContentName.onHist))
      txPrinter = txPrinterReposHist.findById(txPrinterId);
    else 
      txPrinter = txPrinterRepos.findById(txPrinterId);
    TxPrinter obj = txPrinter.isPresent() ? txPrinter.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxPrinter> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxPrinter> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "StanIp", "FileCode"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "StanIp", "FileCode"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txPrinterReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txPrinterReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txPrinterReposHist.findAll(pageable);
    else 
      slice = txPrinterRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxPrinter> findByStanIp(String stanIp_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxPrinter> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByStanIp " + dbName + " : " + "stanIp_0 : " + stanIp_0);
    if (dbName.equals(ContentName.onDay))
      slice = txPrinterReposDay.findAllByStanIpIsOrderByStanIpAscFileCodeAsc(stanIp_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txPrinterReposMon.findAllByStanIpIsOrderByStanIpAscFileCodeAsc(stanIp_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txPrinterReposHist.findAllByStanIpIsOrderByStanIpAscFileCodeAsc(stanIp_0, pageable);
    else 
      slice = txPrinterRepos.findAllByStanIpIsOrderByStanIpAscFileCodeAsc(stanIp_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxPrinter> findByStanIpAndSourceEnv(String stanIp_0, String sourceEnv_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxPrinter> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByStanIpAndSourceEnv " + dbName + " : " + "stanIp_0 : " + stanIp_0 + " sourceEnv_1 : " +  sourceEnv_1);
    if (dbName.equals(ContentName.onDay))
      slice = txPrinterReposDay.findAllByStanIpIsAndSourceEnvIsOrderByStanIpAscFileCodeAsc(stanIp_0, sourceEnv_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txPrinterReposMon.findAllByStanIpIsAndSourceEnvIsOrderByStanIpAscFileCodeAsc(stanIp_0, sourceEnv_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txPrinterReposHist.findAllByStanIpIsAndSourceEnvIsOrderByStanIpAscFileCodeAsc(stanIp_0, sourceEnv_1, pageable);
    else 
      slice = txPrinterRepos.findAllByStanIpIsAndSourceEnvIsOrderByStanIpAscFileCodeAsc(stanIp_0, sourceEnv_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxPrinter holdById(TxPrinterId txPrinterId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txPrinterId);
    Optional<TxPrinter> txPrinter = null;
    if (dbName.equals(ContentName.onDay))
      txPrinter = txPrinterReposDay.findByTxPrinterId(txPrinterId);
    else if (dbName.equals(ContentName.onMon))
      txPrinter = txPrinterReposMon.findByTxPrinterId(txPrinterId);
    else if (dbName.equals(ContentName.onHist))
      txPrinter = txPrinterReposHist.findByTxPrinterId(txPrinterId);
    else 
      txPrinter = txPrinterRepos.findByTxPrinterId(txPrinterId);
    return txPrinter.isPresent() ? txPrinter.get() : null;
  }

  @Override
  public TxPrinter holdById(TxPrinter txPrinter, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txPrinter.getTxPrinterId());
    Optional<TxPrinter> txPrinterT = null;
    if (dbName.equals(ContentName.onDay))
      txPrinterT = txPrinterReposDay.findByTxPrinterId(txPrinter.getTxPrinterId());
    else if (dbName.equals(ContentName.onMon))
      txPrinterT = txPrinterReposMon.findByTxPrinterId(txPrinter.getTxPrinterId());
    else if (dbName.equals(ContentName.onHist))
      txPrinterT = txPrinterReposHist.findByTxPrinterId(txPrinter.getTxPrinterId());
    else 
      txPrinterT = txPrinterRepos.findByTxPrinterId(txPrinter.getTxPrinterId());
    return txPrinterT.isPresent() ? txPrinterT.get() : null;
  }

  @Override
  public TxPrinter insert(TxPrinter txPrinter, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txPrinter.getTxPrinterId());
    if (this.findById(txPrinter.getTxPrinterId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txPrinter.setCreateEmpNo(empNot);

    if(txPrinter.getLastUpdateEmpNo() == null || txPrinter.getLastUpdateEmpNo().isEmpty())
      txPrinter.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txPrinterReposDay.saveAndFlush(txPrinter);	
    else if (dbName.equals(ContentName.onMon))
      return txPrinterReposMon.saveAndFlush(txPrinter);
    else if (dbName.equals(ContentName.onHist))
      return txPrinterReposHist.saveAndFlush(txPrinter);
    else 
    return txPrinterRepos.saveAndFlush(txPrinter);
  }

  @Override
  public TxPrinter update(TxPrinter txPrinter, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txPrinter.getTxPrinterId());
    if (!empNot.isEmpty())
      txPrinter.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txPrinterReposDay.saveAndFlush(txPrinter);	
    else if (dbName.equals(ContentName.onMon))
      return txPrinterReposMon.saveAndFlush(txPrinter);
    else if (dbName.equals(ContentName.onHist))
      return txPrinterReposHist.saveAndFlush(txPrinter);
    else 
    return txPrinterRepos.saveAndFlush(txPrinter);
  }

  @Override
  public TxPrinter update2(TxPrinter txPrinter, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txPrinter.getTxPrinterId());
    if (!empNot.isEmpty())
      txPrinter.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txPrinterReposDay.saveAndFlush(txPrinter);	
    else if (dbName.equals(ContentName.onMon))
      txPrinterReposMon.saveAndFlush(txPrinter);
    else if (dbName.equals(ContentName.onHist))
        txPrinterReposHist.saveAndFlush(txPrinter);
    else 
      txPrinterRepos.saveAndFlush(txPrinter);	
    return this.findById(txPrinter.getTxPrinterId());
  }

  @Override
  public void delete(TxPrinter txPrinter, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txPrinter.getTxPrinterId());
    if (dbName.equals(ContentName.onDay)) {
      txPrinterReposDay.delete(txPrinter);	
      txPrinterReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txPrinterReposMon.delete(txPrinter);	
      txPrinterReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txPrinterReposHist.delete(txPrinter);
      txPrinterReposHist.flush();
    }
    else {
      txPrinterRepos.delete(txPrinter);
      txPrinterRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxPrinter> txPrinter, TitaVo... titaVo) throws DBException {
    if (txPrinter == null || txPrinter.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxPrinter t : txPrinter){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txPrinter = txPrinterReposDay.saveAll(txPrinter);	
      txPrinterReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txPrinter = txPrinterReposMon.saveAll(txPrinter);	
      txPrinterReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txPrinter = txPrinterReposHist.saveAll(txPrinter);
      txPrinterReposHist.flush();
    }
    else {
      txPrinter = txPrinterRepos.saveAll(txPrinter);
      txPrinterRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxPrinter> txPrinter, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txPrinter == null || txPrinter.size() == 0)
      throw new DBException(6);

    for (TxPrinter t : txPrinter) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txPrinter = txPrinterReposDay.saveAll(txPrinter);	
      txPrinterReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txPrinter = txPrinterReposMon.saveAll(txPrinter);	
      txPrinterReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txPrinter = txPrinterReposHist.saveAll(txPrinter);
      txPrinterReposHist.flush();
    }
    else {
      txPrinter = txPrinterRepos.saveAll(txPrinter);
      txPrinterRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxPrinter> txPrinter, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txPrinter == null || txPrinter.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txPrinterReposDay.deleteAll(txPrinter);	
      txPrinterReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txPrinterReposMon.deleteAll(txPrinter);	
      txPrinterReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txPrinterReposHist.deleteAll(txPrinter);
      txPrinterReposHist.flush();
    }
    else {
      txPrinterRepos.deleteAll(txPrinter);
      txPrinterRepos.flush();
    }
  }

}
