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
import com.st1.itx.db.domain.TxAmlNotice;
import com.st1.itx.db.domain.TxAmlNoticeId;
import com.st1.itx.db.repository.online.TxAmlNoticeRepository;
import com.st1.itx.db.repository.day.TxAmlNoticeRepositoryDay;
import com.st1.itx.db.repository.mon.TxAmlNoticeRepositoryMon;
import com.st1.itx.db.repository.hist.TxAmlNoticeRepositoryHist;
import com.st1.itx.db.service.TxAmlNoticeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txAmlNoticeService")
@Repository
public class TxAmlNoticeServiceImpl extends ASpringJpaParm implements TxAmlNoticeService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxAmlNoticeRepository txAmlNoticeRepos;

  @Autowired
  private TxAmlNoticeRepositoryDay txAmlNoticeReposDay;

  @Autowired
  private TxAmlNoticeRepositoryMon txAmlNoticeReposMon;

  @Autowired
  private TxAmlNoticeRepositoryHist txAmlNoticeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txAmlNoticeRepos);
    org.junit.Assert.assertNotNull(txAmlNoticeReposDay);
    org.junit.Assert.assertNotNull(txAmlNoticeReposMon);
    org.junit.Assert.assertNotNull(txAmlNoticeReposHist);
  }

  @Override
  public TxAmlNotice findById(TxAmlNoticeId txAmlNoticeId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + txAmlNoticeId);
    Optional<TxAmlNotice> txAmlNotice = null;
    if (dbName.equals(ContentName.onDay))
      txAmlNotice = txAmlNoticeReposDay.findById(txAmlNoticeId);
    else if (dbName.equals(ContentName.onMon))
      txAmlNotice = txAmlNoticeReposMon.findById(txAmlNoticeId);
    else if (dbName.equals(ContentName.onHist))
      txAmlNotice = txAmlNoticeReposHist.findById(txAmlNoticeId);
    else 
      txAmlNotice = txAmlNoticeRepos.findById(txAmlNoticeId);
    TxAmlNotice obj = txAmlNotice.isPresent() ? txAmlNotice.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxAmlNotice> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAmlNotice> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataDt", "CustKey", "ProcessSno"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataDt", "CustKey", "ProcessSno"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txAmlNoticeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAmlNoticeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAmlNoticeReposHist.findAll(pageable);
    else 
      slice = txAmlNoticeRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAmlNotice> processAll(int dataDt_0, String custKey_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAmlNotice> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("processAll " + dbName + " : " + "dataDt_0 : " + dataDt_0 + " custKey_1 : " +  custKey_1);
    if (dbName.equals(ContentName.onDay))
      slice = txAmlNoticeReposDay.findAllByDataDtIsAndCustKeyIsOrderByProcessSnoAsc(dataDt_0, custKey_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAmlNoticeReposMon.findAllByDataDtIsAndCustKeyIsOrderByProcessSnoAsc(dataDt_0, custKey_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAmlNoticeReposHist.findAllByDataDtIsAndCustKeyIsOrderByProcessSnoAsc(dataDt_0, custKey_1, pageable);
    else 
      slice = txAmlNoticeRepos.findAllByDataDtIsAndCustKeyIsOrderByProcessSnoAsc(dataDt_0, custKey_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAmlNotice> dataDtAll(int dataDt_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAmlNotice> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("dataDtAll " + dbName + " : " + "dataDt_0 : " + dataDt_0);
    if (dbName.equals(ContentName.onDay))
      slice = txAmlNoticeReposDay.findAllByDataDtIsOrderByDataDtAscCustKeyAscProcessSnoAsc(dataDt_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAmlNoticeReposMon.findAllByDataDtIsOrderByDataDtAscCustKeyAscProcessSnoAsc(dataDt_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAmlNoticeReposHist.findAllByDataDtIsOrderByDataDtAscCustKeyAscProcessSnoAsc(dataDt_0, pageable);
    else 
      slice = txAmlNoticeRepos.findAllByDataDtIsOrderByDataDtAscCustKeyAscProcessSnoAsc(dataDt_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAmlNotice> processDateAll(int processDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAmlNotice> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("processDateAll " + dbName + " : " + "processDate_0 : " + processDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = txAmlNoticeReposDay.findAllByProcessDateIsOrderByDataDtAscCustKeyAscProcessSnoAsc(processDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAmlNoticeReposMon.findAllByProcessDateIsOrderByDataDtAscCustKeyAscProcessSnoAsc(processDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAmlNoticeReposHist.findAllByProcessDateIsOrderByDataDtAscCustKeyAscProcessSnoAsc(processDate_0, pageable);
    else 
      slice = txAmlNoticeRepos.findAllByProcessDateIsOrderByDataDtAscCustKeyAscProcessSnoAsc(processDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxAmlNotice holdById(TxAmlNoticeId txAmlNoticeId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txAmlNoticeId);
    Optional<TxAmlNotice> txAmlNotice = null;
    if (dbName.equals(ContentName.onDay))
      txAmlNotice = txAmlNoticeReposDay.findByTxAmlNoticeId(txAmlNoticeId);
    else if (dbName.equals(ContentName.onMon))
      txAmlNotice = txAmlNoticeReposMon.findByTxAmlNoticeId(txAmlNoticeId);
    else if (dbName.equals(ContentName.onHist))
      txAmlNotice = txAmlNoticeReposHist.findByTxAmlNoticeId(txAmlNoticeId);
    else 
      txAmlNotice = txAmlNoticeRepos.findByTxAmlNoticeId(txAmlNoticeId);
    return txAmlNotice.isPresent() ? txAmlNotice.get() : null;
  }

  @Override
  public TxAmlNotice holdById(TxAmlNotice txAmlNotice, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txAmlNotice.getTxAmlNoticeId());
    Optional<TxAmlNotice> txAmlNoticeT = null;
    if (dbName.equals(ContentName.onDay))
      txAmlNoticeT = txAmlNoticeReposDay.findByTxAmlNoticeId(txAmlNotice.getTxAmlNoticeId());
    else if (dbName.equals(ContentName.onMon))
      txAmlNoticeT = txAmlNoticeReposMon.findByTxAmlNoticeId(txAmlNotice.getTxAmlNoticeId());
    else if (dbName.equals(ContentName.onHist))
      txAmlNoticeT = txAmlNoticeReposHist.findByTxAmlNoticeId(txAmlNotice.getTxAmlNoticeId());
    else 
      txAmlNoticeT = txAmlNoticeRepos.findByTxAmlNoticeId(txAmlNotice.getTxAmlNoticeId());
    return txAmlNoticeT.isPresent() ? txAmlNoticeT.get() : null;
  }

  @Override
  public TxAmlNotice insert(TxAmlNotice txAmlNotice, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txAmlNotice.getTxAmlNoticeId());
    if (this.findById(txAmlNotice.getTxAmlNoticeId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txAmlNotice.setCreateEmpNo(empNot);

    if(txAmlNotice.getLastUpdateEmpNo() == null || txAmlNotice.getLastUpdateEmpNo().isEmpty())
      txAmlNotice.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAmlNoticeReposDay.saveAndFlush(txAmlNotice);	
    else if (dbName.equals(ContentName.onMon))
      return txAmlNoticeReposMon.saveAndFlush(txAmlNotice);
    else if (dbName.equals(ContentName.onHist))
      return txAmlNoticeReposHist.saveAndFlush(txAmlNotice);
    else 
    return txAmlNoticeRepos.saveAndFlush(txAmlNotice);
  }

  @Override
  public TxAmlNotice update(TxAmlNotice txAmlNotice, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txAmlNotice.getTxAmlNoticeId());
    if (!empNot.isEmpty())
      txAmlNotice.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAmlNoticeReposDay.saveAndFlush(txAmlNotice);	
    else if (dbName.equals(ContentName.onMon))
      return txAmlNoticeReposMon.saveAndFlush(txAmlNotice);
    else if (dbName.equals(ContentName.onHist))
      return txAmlNoticeReposHist.saveAndFlush(txAmlNotice);
    else 
    return txAmlNoticeRepos.saveAndFlush(txAmlNotice);
  }

  @Override
  public TxAmlNotice update2(TxAmlNotice txAmlNotice, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txAmlNotice.getTxAmlNoticeId());
    if (!empNot.isEmpty())
      txAmlNotice.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txAmlNoticeReposDay.saveAndFlush(txAmlNotice);	
    else if (dbName.equals(ContentName.onMon))
      txAmlNoticeReposMon.saveAndFlush(txAmlNotice);
    else if (dbName.equals(ContentName.onHist))
        txAmlNoticeReposHist.saveAndFlush(txAmlNotice);
    else 
      txAmlNoticeRepos.saveAndFlush(txAmlNotice);	
    return this.findById(txAmlNotice.getTxAmlNoticeId());
  }

  @Override
  public void delete(TxAmlNotice txAmlNotice, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txAmlNotice.getTxAmlNoticeId());
    if (dbName.equals(ContentName.onDay)) {
      txAmlNoticeReposDay.delete(txAmlNotice);	
      txAmlNoticeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlNoticeReposMon.delete(txAmlNotice);	
      txAmlNoticeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlNoticeReposHist.delete(txAmlNotice);
      txAmlNoticeReposHist.flush();
    }
    else {
      txAmlNoticeRepos.delete(txAmlNotice);
      txAmlNoticeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxAmlNotice> txAmlNotice, TitaVo... titaVo) throws DBException {
    if (txAmlNotice == null || txAmlNotice.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxAmlNotice t : txAmlNotice){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txAmlNotice = txAmlNoticeReposDay.saveAll(txAmlNotice);	
      txAmlNoticeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlNotice = txAmlNoticeReposMon.saveAll(txAmlNotice);	
      txAmlNoticeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlNotice = txAmlNoticeReposHist.saveAll(txAmlNotice);
      txAmlNoticeReposHist.flush();
    }
    else {
      txAmlNotice = txAmlNoticeRepos.saveAll(txAmlNotice);
      txAmlNoticeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxAmlNotice> txAmlNotice, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txAmlNotice == null || txAmlNotice.size() == 0)
      throw new DBException(6);

    for (TxAmlNotice t : txAmlNotice) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txAmlNotice = txAmlNoticeReposDay.saveAll(txAmlNotice);	
      txAmlNoticeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlNotice = txAmlNoticeReposMon.saveAll(txAmlNotice);	
      txAmlNoticeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlNotice = txAmlNoticeReposHist.saveAll(txAmlNotice);
      txAmlNoticeReposHist.flush();
    }
    else {
      txAmlNotice = txAmlNoticeRepos.saveAll(txAmlNotice);
      txAmlNoticeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxAmlNotice> txAmlNotice, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txAmlNotice == null || txAmlNotice.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txAmlNoticeReposDay.deleteAll(txAmlNotice);	
      txAmlNoticeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlNoticeReposMon.deleteAll(txAmlNotice);	
      txAmlNoticeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlNoticeReposHist.deleteAll(txAmlNotice);
      txAmlNoticeReposHist.flush();
    }
    else {
      txAmlNoticeRepos.deleteAll(txAmlNotice);
      txAmlNoticeRepos.flush();
    }
  }

}
