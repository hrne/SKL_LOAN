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
import com.st1.itx.db.domain.TxArchiveTable;
import com.st1.itx.db.domain.TxArchiveTableId;
import com.st1.itx.db.repository.online.TxArchiveTableRepository;
import com.st1.itx.db.repository.day.TxArchiveTableRepositoryDay;
import com.st1.itx.db.repository.mon.TxArchiveTableRepositoryMon;
import com.st1.itx.db.repository.hist.TxArchiveTableRepositoryHist;
import com.st1.itx.db.service.TxArchiveTableService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txArchiveTableService")
@Repository
public class TxArchiveTableServiceImpl extends ASpringJpaParm implements TxArchiveTableService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxArchiveTableRepository txArchiveTableRepos;

  @Autowired
  private TxArchiveTableRepositoryDay txArchiveTableReposDay;

  @Autowired
  private TxArchiveTableRepositoryMon txArchiveTableReposMon;

  @Autowired
  private TxArchiveTableRepositoryHist txArchiveTableReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txArchiveTableRepos);
    org.junit.Assert.assertNotNull(txArchiveTableReposDay);
    org.junit.Assert.assertNotNull(txArchiveTableReposMon);
    org.junit.Assert.assertNotNull(txArchiveTableReposHist);
  }

  @Override
  public TxArchiveTable findById(TxArchiveTableId txArchiveTableId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + txArchiveTableId);
    Optional<TxArchiveTable> txArchiveTable = null;
    if (dbName.equals(ContentName.onDay))
      txArchiveTable = txArchiveTableReposDay.findById(txArchiveTableId);
    else if (dbName.equals(ContentName.onMon))
      txArchiveTable = txArchiveTableReposMon.findById(txArchiveTableId);
    else if (dbName.equals(ContentName.onHist))
      txArchiveTable = txArchiveTableReposHist.findById(txArchiveTableId);
    else 
      txArchiveTable = txArchiveTableRepos.findById(txArchiveTableId);
    TxArchiveTable obj = txArchiveTable.isPresent() ? txArchiveTable.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxArchiveTable> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxArchiveTable> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Type", "TableName"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Type", "TableName"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txArchiveTableReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txArchiveTableReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txArchiveTableReposHist.findAll(pageable);
    else 
      slice = txArchiveTableRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxArchiveTable holdById(TxArchiveTableId txArchiveTableId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txArchiveTableId);
    Optional<TxArchiveTable> txArchiveTable = null;
    if (dbName.equals(ContentName.onDay))
      txArchiveTable = txArchiveTableReposDay.findByTxArchiveTableId(txArchiveTableId);
    else if (dbName.equals(ContentName.onMon))
      txArchiveTable = txArchiveTableReposMon.findByTxArchiveTableId(txArchiveTableId);
    else if (dbName.equals(ContentName.onHist))
      txArchiveTable = txArchiveTableReposHist.findByTxArchiveTableId(txArchiveTableId);
    else 
      txArchiveTable = txArchiveTableRepos.findByTxArchiveTableId(txArchiveTableId);
    return txArchiveTable.isPresent() ? txArchiveTable.get() : null;
  }

  @Override
  public TxArchiveTable holdById(TxArchiveTable txArchiveTable, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txArchiveTable.getTxArchiveTableId());
    Optional<TxArchiveTable> txArchiveTableT = null;
    if (dbName.equals(ContentName.onDay))
      txArchiveTableT = txArchiveTableReposDay.findByTxArchiveTableId(txArchiveTable.getTxArchiveTableId());
    else if (dbName.equals(ContentName.onMon))
      txArchiveTableT = txArchiveTableReposMon.findByTxArchiveTableId(txArchiveTable.getTxArchiveTableId());
    else if (dbName.equals(ContentName.onHist))
      txArchiveTableT = txArchiveTableReposHist.findByTxArchiveTableId(txArchiveTable.getTxArchiveTableId());
    else 
      txArchiveTableT = txArchiveTableRepos.findByTxArchiveTableId(txArchiveTable.getTxArchiveTableId());
    return txArchiveTableT.isPresent() ? txArchiveTableT.get() : null;
  }

  @Override
  public TxArchiveTable insert(TxArchiveTable txArchiveTable, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txArchiveTable.getTxArchiveTableId());
    if (this.findById(txArchiveTable.getTxArchiveTableId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txArchiveTable.setCreateEmpNo(empNot);

    if(txArchiveTable.getLastUpdateEmpNo() == null || txArchiveTable.getLastUpdateEmpNo().isEmpty())
      txArchiveTable.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txArchiveTableReposDay.saveAndFlush(txArchiveTable);	
    else if (dbName.equals(ContentName.onMon))
      return txArchiveTableReposMon.saveAndFlush(txArchiveTable);
    else if (dbName.equals(ContentName.onHist))
      return txArchiveTableReposHist.saveAndFlush(txArchiveTable);
    else 
    return txArchiveTableRepos.saveAndFlush(txArchiveTable);
  }

  @Override
  public TxArchiveTable update(TxArchiveTable txArchiveTable, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txArchiveTable.getTxArchiveTableId());
    if (!empNot.isEmpty())
      txArchiveTable.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txArchiveTableReposDay.saveAndFlush(txArchiveTable);	
    else if (dbName.equals(ContentName.onMon))
      return txArchiveTableReposMon.saveAndFlush(txArchiveTable);
    else if (dbName.equals(ContentName.onHist))
      return txArchiveTableReposHist.saveAndFlush(txArchiveTable);
    else 
    return txArchiveTableRepos.saveAndFlush(txArchiveTable);
  }

  @Override
  public TxArchiveTable update2(TxArchiveTable txArchiveTable, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txArchiveTable.getTxArchiveTableId());
    if (!empNot.isEmpty())
      txArchiveTable.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txArchiveTableReposDay.saveAndFlush(txArchiveTable);	
    else if (dbName.equals(ContentName.onMon))
      txArchiveTableReposMon.saveAndFlush(txArchiveTable);
    else if (dbName.equals(ContentName.onHist))
        txArchiveTableReposHist.saveAndFlush(txArchiveTable);
    else 
      txArchiveTableRepos.saveAndFlush(txArchiveTable);	
    return this.findById(txArchiveTable.getTxArchiveTableId());
  }

  @Override
  public void delete(TxArchiveTable txArchiveTable, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txArchiveTable.getTxArchiveTableId());
    if (dbName.equals(ContentName.onDay)) {
      txArchiveTableReposDay.delete(txArchiveTable);	
      txArchiveTableReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txArchiveTableReposMon.delete(txArchiveTable);	
      txArchiveTableReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txArchiveTableReposHist.delete(txArchiveTable);
      txArchiveTableReposHist.flush();
    }
    else {
      txArchiveTableRepos.delete(txArchiveTable);
      txArchiveTableRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxArchiveTable> txArchiveTable, TitaVo... titaVo) throws DBException {
    if (txArchiveTable == null || txArchiveTable.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxArchiveTable t : txArchiveTable){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txArchiveTable = txArchiveTableReposDay.saveAll(txArchiveTable);	
      txArchiveTableReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txArchiveTable = txArchiveTableReposMon.saveAll(txArchiveTable);	
      txArchiveTableReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txArchiveTable = txArchiveTableReposHist.saveAll(txArchiveTable);
      txArchiveTableReposHist.flush();
    }
    else {
      txArchiveTable = txArchiveTableRepos.saveAll(txArchiveTable);
      txArchiveTableRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxArchiveTable> txArchiveTable, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txArchiveTable == null || txArchiveTable.size() == 0)
      throw new DBException(6);

    for (TxArchiveTable t : txArchiveTable) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txArchiveTable = txArchiveTableReposDay.saveAll(txArchiveTable);	
      txArchiveTableReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txArchiveTable = txArchiveTableReposMon.saveAll(txArchiveTable);	
      txArchiveTableReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txArchiveTable = txArchiveTableReposHist.saveAll(txArchiveTable);
      txArchiveTableReposHist.flush();
    }
    else {
      txArchiveTable = txArchiveTableRepos.saveAll(txArchiveTable);
      txArchiveTableRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxArchiveTable> txArchiveTable, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txArchiveTable == null || txArchiveTable.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txArchiveTableReposDay.deleteAll(txArchiveTable);	
      txArchiveTableReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txArchiveTableReposMon.deleteAll(txArchiveTable);	
      txArchiveTableReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txArchiveTableReposHist.deleteAll(txArchiveTable);
      txArchiveTableReposHist.flush();
    }
    else {
      txArchiveTableRepos.deleteAll(txArchiveTable);
      txArchiveTableRepos.flush();
    }
  }

}
