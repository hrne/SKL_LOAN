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
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.repository.online.TxTellerRepository;
import com.st1.itx.db.repository.day.TxTellerRepositoryDay;
import com.st1.itx.db.repository.mon.TxTellerRepositoryMon;
import com.st1.itx.db.repository.hist.TxTellerRepositoryHist;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txTellerService")
@Repository
public class TxTellerServiceImpl extends ASpringJpaParm implements TxTellerService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxTellerRepository txTellerRepos;

  @Autowired
  private TxTellerRepositoryDay txTellerReposDay;

  @Autowired
  private TxTellerRepositoryMon txTellerReposMon;

  @Autowired
  private TxTellerRepositoryHist txTellerReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txTellerRepos);
    org.junit.Assert.assertNotNull(txTellerReposDay);
    org.junit.Assert.assertNotNull(txTellerReposMon);
    org.junit.Assert.assertNotNull(txTellerReposHist);
  }

  @Override
  public TxTeller findById(String tlrNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + tlrNo);
    Optional<TxTeller> txTeller = null;
    if (dbName.equals(ContentName.onDay))
      txTeller = txTellerReposDay.findById(tlrNo);
    else if (dbName.equals(ContentName.onMon))
      txTeller = txTellerReposMon.findById(tlrNo);
    else if (dbName.equals(ContentName.onHist))
      txTeller = txTellerReposHist.findById(tlrNo);
    else 
      txTeller = txTellerRepos.findById(tlrNo);
    TxTeller obj = txTeller.isPresent() ? txTeller.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxTeller> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxTeller> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "TlrNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "TlrNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txTellerReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txTellerReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txTellerReposHist.findAll(pageable);
    else 
      slice = txTellerRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxTeller> findByL6041(String brNo_0, String tlrNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxTeller> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByL6041 " + dbName + " : " + "brNo_0 : " + brNo_0 + " tlrNo_1 : " +  tlrNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = txTellerReposDay.findAllByBrNoIsAndTlrNoLikeOrderByTlrNoAsc(brNo_0, tlrNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txTellerReposMon.findAllByBrNoIsAndTlrNoLikeOrderByTlrNoAsc(brNo_0, tlrNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txTellerReposHist.findAllByBrNoIsAndTlrNoLikeOrderByTlrNoAsc(brNo_0, tlrNo_1, pageable);
    else 
      slice = txTellerRepos.findAllByBrNoIsAndTlrNoLikeOrderByTlrNoAsc(brNo_0, tlrNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxTeller> findByGroupNo(String brNo_0, String groupNo_1, String groupNo_2, int levelFg_3, int levelFg_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxTeller> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByGroupNo " + dbName + " : " + "brNo_0 : " + brNo_0 + " groupNo_1 : " +  groupNo_1 + " groupNo_2 : " +  groupNo_2 + " levelFg_3 : " +  levelFg_3 + " levelFg_4 : " +  levelFg_4);
    if (dbName.equals(ContentName.onDay))
      slice = txTellerReposDay.findAllByBrNoIsAndGroupNoGreaterThanEqualAndGroupNoLessThanEqualAndLevelFgGreaterThanEqualAndLevelFgLessThanEqualOrderByTlrNoAsc(brNo_0, groupNo_1, groupNo_2, levelFg_3, levelFg_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txTellerReposMon.findAllByBrNoIsAndGroupNoGreaterThanEqualAndGroupNoLessThanEqualAndLevelFgGreaterThanEqualAndLevelFgLessThanEqualOrderByTlrNoAsc(brNo_0, groupNo_1, groupNo_2, levelFg_3, levelFg_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txTellerReposHist.findAllByBrNoIsAndGroupNoGreaterThanEqualAndGroupNoLessThanEqualAndLevelFgGreaterThanEqualAndLevelFgLessThanEqualOrderByTlrNoAsc(brNo_0, groupNo_1, groupNo_2, levelFg_3, levelFg_4, pageable);
    else 
      slice = txTellerRepos.findAllByBrNoIsAndGroupNoGreaterThanEqualAndGroupNoLessThanEqualAndLevelFgGreaterThanEqualAndLevelFgLessThanEqualOrderByTlrNoAsc(brNo_0, groupNo_1, groupNo_2, levelFg_3, levelFg_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxTeller holdById(String tlrNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + tlrNo);
    Optional<TxTeller> txTeller = null;
    if (dbName.equals(ContentName.onDay))
      txTeller = txTellerReposDay.findByTlrNo(tlrNo);
    else if (dbName.equals(ContentName.onMon))
      txTeller = txTellerReposMon.findByTlrNo(tlrNo);
    else if (dbName.equals(ContentName.onHist))
      txTeller = txTellerReposHist.findByTlrNo(tlrNo);
    else 
      txTeller = txTellerRepos.findByTlrNo(tlrNo);
    return txTeller.isPresent() ? txTeller.get() : null;
  }

  @Override
  public TxTeller holdById(TxTeller txTeller, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txTeller.getTlrNo());
    Optional<TxTeller> txTellerT = null;
    if (dbName.equals(ContentName.onDay))
      txTellerT = txTellerReposDay.findByTlrNo(txTeller.getTlrNo());
    else if (dbName.equals(ContentName.onMon))
      txTellerT = txTellerReposMon.findByTlrNo(txTeller.getTlrNo());
    else if (dbName.equals(ContentName.onHist))
      txTellerT = txTellerReposHist.findByTlrNo(txTeller.getTlrNo());
    else 
      txTellerT = txTellerRepos.findByTlrNo(txTeller.getTlrNo());
    return txTellerT.isPresent() ? txTellerT.get() : null;
  }

  @Override
  public TxTeller insert(TxTeller txTeller, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txTeller.getTlrNo());
    if (this.findById(txTeller.getTlrNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txTeller.setCreateEmpNo(empNot);

    if(txTeller.getLastUpdateEmpNo() == null || txTeller.getLastUpdateEmpNo().isEmpty())
      txTeller.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txTellerReposDay.saveAndFlush(txTeller);	
    else if (dbName.equals(ContentName.onMon))
      return txTellerReposMon.saveAndFlush(txTeller);
    else if (dbName.equals(ContentName.onHist))
      return txTellerReposHist.saveAndFlush(txTeller);
    else 
    return txTellerRepos.saveAndFlush(txTeller);
  }

  @Override
  public TxTeller update(TxTeller txTeller, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txTeller.getTlrNo());
    if (!empNot.isEmpty())
      txTeller.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txTellerReposDay.saveAndFlush(txTeller);	
    else if (dbName.equals(ContentName.onMon))
      return txTellerReposMon.saveAndFlush(txTeller);
    else if (dbName.equals(ContentName.onHist))
      return txTellerReposHist.saveAndFlush(txTeller);
    else 
    return txTellerRepos.saveAndFlush(txTeller);
  }

  @Override
  public TxTeller update2(TxTeller txTeller, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txTeller.getTlrNo());
    if (!empNot.isEmpty())
      txTeller.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txTellerReposDay.saveAndFlush(txTeller);	
    else if (dbName.equals(ContentName.onMon))
      txTellerReposMon.saveAndFlush(txTeller);
    else if (dbName.equals(ContentName.onHist))
        txTellerReposHist.saveAndFlush(txTeller);
    else 
      txTellerRepos.saveAndFlush(txTeller);	
    return this.findById(txTeller.getTlrNo());
  }

  @Override
  public void delete(TxTeller txTeller, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txTeller.getTlrNo());
    if (dbName.equals(ContentName.onDay)) {
      txTellerReposDay.delete(txTeller);	
      txTellerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTellerReposMon.delete(txTeller);	
      txTellerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTellerReposHist.delete(txTeller);
      txTellerReposHist.flush();
    }
    else {
      txTellerRepos.delete(txTeller);
      txTellerRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxTeller> txTeller, TitaVo... titaVo) throws DBException {
    if (txTeller == null || txTeller.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxTeller t : txTeller){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txTeller = txTellerReposDay.saveAll(txTeller);	
      txTellerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTeller = txTellerReposMon.saveAll(txTeller);	
      txTellerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTeller = txTellerReposHist.saveAll(txTeller);
      txTellerReposHist.flush();
    }
    else {
      txTeller = txTellerRepos.saveAll(txTeller);
      txTellerRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxTeller> txTeller, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txTeller == null || txTeller.size() == 0)
      throw new DBException(6);

    for (TxTeller t : txTeller) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txTeller = txTellerReposDay.saveAll(txTeller);	
      txTellerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTeller = txTellerReposMon.saveAll(txTeller);	
      txTellerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTeller = txTellerReposHist.saveAll(txTeller);
      txTellerReposHist.flush();
    }
    else {
      txTeller = txTellerRepos.saveAll(txTeller);
      txTellerRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxTeller> txTeller, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txTeller == null || txTeller.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txTellerReposDay.deleteAll(txTeller);	
      txTellerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTellerReposMon.deleteAll(txTeller);	
      txTellerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTellerReposHist.deleteAll(txTeller);
      txTellerReposHist.flush();
    }
    else {
      txTellerRepos.deleteAll(txTeller);
      txTellerRepos.flush();
    }
  }

}
