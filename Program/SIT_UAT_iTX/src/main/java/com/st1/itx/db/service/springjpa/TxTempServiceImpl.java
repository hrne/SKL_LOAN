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
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.repository.online.TxTempRepository;
import com.st1.itx.db.repository.day.TxTempRepositoryDay;
import com.st1.itx.db.repository.mon.TxTempRepositoryMon;
import com.st1.itx.db.repository.hist.TxTempRepositoryHist;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txTempService")
@Repository
public class TxTempServiceImpl extends ASpringJpaParm implements TxTempService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxTempRepository txTempRepos;

  @Autowired
  private TxTempRepositoryDay txTempReposDay;

  @Autowired
  private TxTempRepositoryMon txTempReposMon;

  @Autowired
  private TxTempRepositoryHist txTempReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txTempRepos);
    org.junit.Assert.assertNotNull(txTempReposDay);
    org.junit.Assert.assertNotNull(txTempReposMon);
    org.junit.Assert.assertNotNull(txTempReposHist);
  }

  @Override
  public TxTemp findById(TxTempId txTempId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + txTempId);
    Optional<TxTemp> txTemp = null;
    if (dbName.equals(ContentName.onDay))
      txTemp = txTempReposDay.findById(txTempId);
    else if (dbName.equals(ContentName.onMon))
      txTemp = txTempReposMon.findById(txTempId);
    else if (dbName.equals(ContentName.onHist))
      txTemp = txTempReposHist.findById(txTempId);
    else 
      txTemp = txTempRepos.findById(txTempId);
    TxTemp obj = txTemp.isPresent() ? txTemp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxTemp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxTemp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Entdy", "Kinbr", "TlrNo", "TxtNo", "SeqNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Entdy", "Kinbr", "TlrNo", "TxtNo", "SeqNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txTempReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txTempReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txTempReposHist.findAll(pageable);
    else 
      slice = txTempRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxTemp> txTempTxtNoEq(int entdy_0, String kinbr_1, String tlrNo_2, String txtNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxTemp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("txTempTxtNoEq " + dbName + " : " + "entdy_0 : " + entdy_0 + " kinbr_1 : " +  kinbr_1 + " tlrNo_2 : " +  tlrNo_2 + " txtNo_3 : " +  txtNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = txTempReposDay.findAllByEntdyIsAndKinbrIsAndTlrNoIsAndTxtNoIsOrderBySeqNoAsc(entdy_0, kinbr_1, tlrNo_2, txtNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txTempReposMon.findAllByEntdyIsAndKinbrIsAndTlrNoIsAndTxtNoIsOrderBySeqNoAsc(entdy_0, kinbr_1, tlrNo_2, txtNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txTempReposHist.findAllByEntdyIsAndKinbrIsAndTlrNoIsAndTxtNoIsOrderBySeqNoAsc(entdy_0, kinbr_1, tlrNo_2, txtNo_3, pageable);
    else 
      slice = txTempRepos.findAllByEntdyIsAndKinbrIsAndTlrNoIsAndTxtNoIsOrderBySeqNoAsc(entdy_0, kinbr_1, tlrNo_2, txtNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxTemp txtNoLastFirst(String tlrNo_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("txtNoLastFirst " + dbName + " : " + "tlrNo_0 : " + tlrNo_0);
    Optional<TxTemp> txTempT = null;
    if (dbName.equals(ContentName.onDay))
      txTempT = txTempReposDay.findTopByTlrNoIsOrderByCreateDateDesc(tlrNo_0);
    else if (dbName.equals(ContentName.onMon))
      txTempT = txTempReposMon.findTopByTlrNoIsOrderByCreateDateDesc(tlrNo_0);
    else if (dbName.equals(ContentName.onHist))
      txTempT = txTempReposHist.findTopByTlrNoIsOrderByCreateDateDesc(tlrNo_0);
    else 
      txTempT = txTempRepos.findTopByTlrNoIsOrderByCreateDateDesc(tlrNo_0);

    return txTempT.isPresent() ? txTempT.get() : null;
  }

  @Override
  public TxTemp holdById(TxTempId txTempId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txTempId);
    Optional<TxTemp> txTemp = null;
    if (dbName.equals(ContentName.onDay))
      txTemp = txTempReposDay.findByTxTempId(txTempId);
    else if (dbName.equals(ContentName.onMon))
      txTemp = txTempReposMon.findByTxTempId(txTempId);
    else if (dbName.equals(ContentName.onHist))
      txTemp = txTempReposHist.findByTxTempId(txTempId);
    else 
      txTemp = txTempRepos.findByTxTempId(txTempId);
    return txTemp.isPresent() ? txTemp.get() : null;
  }

  @Override
  public TxTemp holdById(TxTemp txTemp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txTemp.getTxTempId());
    Optional<TxTemp> txTempT = null;
    if (dbName.equals(ContentName.onDay))
      txTempT = txTempReposDay.findByTxTempId(txTemp.getTxTempId());
    else if (dbName.equals(ContentName.onMon))
      txTempT = txTempReposMon.findByTxTempId(txTemp.getTxTempId());
    else if (dbName.equals(ContentName.onHist))
      txTempT = txTempReposHist.findByTxTempId(txTemp.getTxTempId());
    else 
      txTempT = txTempRepos.findByTxTempId(txTemp.getTxTempId());
    return txTempT.isPresent() ? txTempT.get() : null;
  }

  @Override
  public TxTemp insert(TxTemp txTemp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txTemp.getTxTempId());
    if (this.findById(txTemp.getTxTempId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txTemp.setCreateEmpNo(empNot);

    if(txTemp.getLastUpdateEmpNo() == null || txTemp.getLastUpdateEmpNo().isEmpty())
      txTemp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txTempReposDay.saveAndFlush(txTemp);	
    else if (dbName.equals(ContentName.onMon))
      return txTempReposMon.saveAndFlush(txTemp);
    else if (dbName.equals(ContentName.onHist))
      return txTempReposHist.saveAndFlush(txTemp);
    else 
    return txTempRepos.saveAndFlush(txTemp);
  }

  @Override
  public TxTemp update(TxTemp txTemp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txTemp.getTxTempId());
    if (!empNot.isEmpty())
      txTemp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txTempReposDay.saveAndFlush(txTemp);	
    else if (dbName.equals(ContentName.onMon))
      return txTempReposMon.saveAndFlush(txTemp);
    else if (dbName.equals(ContentName.onHist))
      return txTempReposHist.saveAndFlush(txTemp);
    else 
    return txTempRepos.saveAndFlush(txTemp);
  }

  @Override
  public TxTemp update2(TxTemp txTemp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txTemp.getTxTempId());
    if (!empNot.isEmpty())
      txTemp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txTempReposDay.saveAndFlush(txTemp);	
    else if (dbName.equals(ContentName.onMon))
      txTempReposMon.saveAndFlush(txTemp);
    else if (dbName.equals(ContentName.onHist))
        txTempReposHist.saveAndFlush(txTemp);
    else 
      txTempRepos.saveAndFlush(txTemp);	
    return this.findById(txTemp.getTxTempId());
  }

  @Override
  public void delete(TxTemp txTemp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txTemp.getTxTempId());
    if (dbName.equals(ContentName.onDay)) {
      txTempReposDay.delete(txTemp);	
      txTempReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTempReposMon.delete(txTemp);	
      txTempReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTempReposHist.delete(txTemp);
      txTempReposHist.flush();
    }
    else {
      txTempRepos.delete(txTemp);
      txTempRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxTemp> txTemp, TitaVo... titaVo) throws DBException {
    if (txTemp == null || txTemp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxTemp t : txTemp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txTemp = txTempReposDay.saveAll(txTemp);	
      txTempReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTemp = txTempReposMon.saveAll(txTemp);	
      txTempReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTemp = txTempReposHist.saveAll(txTemp);
      txTempReposHist.flush();
    }
    else {
      txTemp = txTempRepos.saveAll(txTemp);
      txTempRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxTemp> txTemp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txTemp == null || txTemp.size() == 0)
      throw new DBException(6);

    for (TxTemp t : txTemp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txTemp = txTempReposDay.saveAll(txTemp);	
      txTempReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTemp = txTempReposMon.saveAll(txTemp);	
      txTempReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTemp = txTempReposHist.saveAll(txTemp);
      txTempReposHist.flush();
    }
    else {
      txTemp = txTempRepos.saveAll(txTemp);
      txTempRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxTemp> txTemp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txTemp == null || txTemp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txTempReposDay.deleteAll(txTemp);	
      txTempReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTempReposMon.deleteAll(txTemp);	
      txTempReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTempReposHist.deleteAll(txTemp);
      txTempReposHist.flush();
    }
    else {
      txTempRepos.deleteAll(txTemp);
      txTempRepos.flush();
    }
  }

}
