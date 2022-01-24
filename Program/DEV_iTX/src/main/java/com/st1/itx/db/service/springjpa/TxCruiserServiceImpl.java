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
import com.st1.itx.db.domain.TxCruiser;
import com.st1.itx.db.domain.TxCruiserId;
import com.st1.itx.db.repository.online.TxCruiserRepository;
import com.st1.itx.db.repository.day.TxCruiserRepositoryDay;
import com.st1.itx.db.repository.mon.TxCruiserRepositoryMon;
import com.st1.itx.db.repository.hist.TxCruiserRepositoryHist;
import com.st1.itx.db.service.TxCruiserService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txCruiserService")
@Repository
public class TxCruiserServiceImpl extends ASpringJpaParm implements TxCruiserService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxCruiserRepository txCruiserRepos;

  @Autowired
  private TxCruiserRepositoryDay txCruiserReposDay;

  @Autowired
  private TxCruiserRepositoryMon txCruiserReposMon;

  @Autowired
  private TxCruiserRepositoryHist txCruiserReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txCruiserRepos);
    org.junit.Assert.assertNotNull(txCruiserReposDay);
    org.junit.Assert.assertNotNull(txCruiserReposMon);
    org.junit.Assert.assertNotNull(txCruiserReposHist);
  }

  @Override
  public TxCruiser findById(TxCruiserId txCruiserId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + txCruiserId);
    Optional<TxCruiser> txCruiser = null;
    if (dbName.equals(ContentName.onDay))
      txCruiser = txCruiserReposDay.findById(txCruiserId);
    else if (dbName.equals(ContentName.onMon))
      txCruiser = txCruiserReposMon.findById(txCruiserId);
    else if (dbName.equals(ContentName.onHist))
      txCruiser = txCruiserReposHist.findById(txCruiserId);
    else 
      txCruiser = txCruiserRepos.findById(txCruiserId);
    TxCruiser obj = txCruiser.isPresent() ? txCruiser.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxCruiser> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxCruiser> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "TxSeq", "TlrNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "TxSeq", "TlrNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txCruiserReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txCruiserReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txCruiserReposHist.findAll(pageable);
    else 
      slice = txCruiserRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxCruiser> FindAllByStatus(String status_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxCruiser> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("FindAllByStatus " + dbName + " : " + "status_0 : " + status_0);
    if (dbName.equals(ContentName.onDay))
      slice = txCruiserReposDay.findAllByStatusIsOrderByTxSeqAsc(status_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txCruiserReposMon.findAllByStatusIsOrderByTxSeqAsc(status_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txCruiserReposHist.findAllByStatusIsOrderByTxSeqAsc(status_0, pageable);
    else 
      slice = txCruiserRepos.findAllByStatusIsOrderByTxSeqAsc(status_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxCruiser holdById(TxCruiserId txCruiserId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txCruiserId);
    Optional<TxCruiser> txCruiser = null;
    if (dbName.equals(ContentName.onDay))
      txCruiser = txCruiserReposDay.findByTxCruiserId(txCruiserId);
    else if (dbName.equals(ContentName.onMon))
      txCruiser = txCruiserReposMon.findByTxCruiserId(txCruiserId);
    else if (dbName.equals(ContentName.onHist))
      txCruiser = txCruiserReposHist.findByTxCruiserId(txCruiserId);
    else 
      txCruiser = txCruiserRepos.findByTxCruiserId(txCruiserId);
    return txCruiser.isPresent() ? txCruiser.get() : null;
  }

  @Override
  public TxCruiser holdById(TxCruiser txCruiser, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txCruiser.getTxCruiserId());
    Optional<TxCruiser> txCruiserT = null;
    if (dbName.equals(ContentName.onDay))
      txCruiserT = txCruiserReposDay.findByTxCruiserId(txCruiser.getTxCruiserId());
    else if (dbName.equals(ContentName.onMon))
      txCruiserT = txCruiserReposMon.findByTxCruiserId(txCruiser.getTxCruiserId());
    else if (dbName.equals(ContentName.onHist))
      txCruiserT = txCruiserReposHist.findByTxCruiserId(txCruiser.getTxCruiserId());
    else 
      txCruiserT = txCruiserRepos.findByTxCruiserId(txCruiser.getTxCruiserId());
    return txCruiserT.isPresent() ? txCruiserT.get() : null;
  }

  @Override
  public TxCruiser insert(TxCruiser txCruiser, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txCruiser.getTxCruiserId());
    if (this.findById(txCruiser.getTxCruiserId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txCruiser.setCreateEmpNo(empNot);

    if(txCruiser.getLastUpdateEmpNo() == null || txCruiser.getLastUpdateEmpNo().isEmpty())
      txCruiser.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txCruiserReposDay.saveAndFlush(txCruiser);	
    else if (dbName.equals(ContentName.onMon))
      return txCruiserReposMon.saveAndFlush(txCruiser);
    else if (dbName.equals(ContentName.onHist))
      return txCruiserReposHist.saveAndFlush(txCruiser);
    else 
    return txCruiserRepos.saveAndFlush(txCruiser);
  }

  @Override
  public TxCruiser update(TxCruiser txCruiser, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txCruiser.getTxCruiserId());
    if (!empNot.isEmpty())
      txCruiser.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txCruiserReposDay.saveAndFlush(txCruiser);	
    else if (dbName.equals(ContentName.onMon))
      return txCruiserReposMon.saveAndFlush(txCruiser);
    else if (dbName.equals(ContentName.onHist))
      return txCruiserReposHist.saveAndFlush(txCruiser);
    else 
    return txCruiserRepos.saveAndFlush(txCruiser);
  }

  @Override
  public TxCruiser update2(TxCruiser txCruiser, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txCruiser.getTxCruiserId());
    if (!empNot.isEmpty())
      txCruiser.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txCruiserReposDay.saveAndFlush(txCruiser);	
    else if (dbName.equals(ContentName.onMon))
      txCruiserReposMon.saveAndFlush(txCruiser);
    else if (dbName.equals(ContentName.onHist))
        txCruiserReposHist.saveAndFlush(txCruiser);
    else 
      txCruiserRepos.saveAndFlush(txCruiser);	
    return this.findById(txCruiser.getTxCruiserId());
  }

  @Override
  public void delete(TxCruiser txCruiser, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txCruiser.getTxCruiserId());
    if (dbName.equals(ContentName.onDay)) {
      txCruiserReposDay.delete(txCruiser);	
      txCruiserReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txCruiserReposMon.delete(txCruiser);	
      txCruiserReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txCruiserReposHist.delete(txCruiser);
      txCruiserReposHist.flush();
    }
    else {
      txCruiserRepos.delete(txCruiser);
      txCruiserRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxCruiser> txCruiser, TitaVo... titaVo) throws DBException {
    if (txCruiser == null || txCruiser.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxCruiser t : txCruiser){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txCruiser = txCruiserReposDay.saveAll(txCruiser);	
      txCruiserReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txCruiser = txCruiserReposMon.saveAll(txCruiser);	
      txCruiserReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txCruiser = txCruiserReposHist.saveAll(txCruiser);
      txCruiserReposHist.flush();
    }
    else {
      txCruiser = txCruiserRepos.saveAll(txCruiser);
      txCruiserRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxCruiser> txCruiser, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txCruiser == null || txCruiser.size() == 0)
      throw new DBException(6);

    for (TxCruiser t : txCruiser) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txCruiser = txCruiserReposDay.saveAll(txCruiser);	
      txCruiserReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txCruiser = txCruiserReposMon.saveAll(txCruiser);	
      txCruiserReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txCruiser = txCruiserReposHist.saveAll(txCruiser);
      txCruiserReposHist.flush();
    }
    else {
      txCruiser = txCruiserRepos.saveAll(txCruiser);
      txCruiserRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxCruiser> txCruiser, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txCruiser == null || txCruiser.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txCruiserReposDay.deleteAll(txCruiser);	
      txCruiserReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txCruiserReposMon.deleteAll(txCruiser);	
      txCruiserReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txCruiserReposHist.deleteAll(txCruiser);
      txCruiserReposHist.flush();
    }
    else {
      txCruiserRepos.deleteAll(txCruiser);
      txCruiserRepos.flush();
    }
  }

}
