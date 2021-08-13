package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.db.domain.TxAmlCredit;
import com.st1.itx.db.domain.TxAmlCreditId;
import com.st1.itx.db.repository.online.TxAmlCreditRepository;
import com.st1.itx.db.repository.day.TxAmlCreditRepositoryDay;
import com.st1.itx.db.repository.mon.TxAmlCreditRepositoryMon;
import com.st1.itx.db.repository.hist.TxAmlCreditRepositoryHist;
import com.st1.itx.db.service.TxAmlCreditService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txAmlCreditService")
@Repository
public class TxAmlCreditServiceImpl implements TxAmlCreditService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(TxAmlCreditServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxAmlCreditRepository txAmlCreditRepos;

  @Autowired
  private TxAmlCreditRepositoryDay txAmlCreditReposDay;

  @Autowired
  private TxAmlCreditRepositoryMon txAmlCreditReposMon;

  @Autowired
  private TxAmlCreditRepositoryHist txAmlCreditReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txAmlCreditRepos);
    org.junit.Assert.assertNotNull(txAmlCreditReposDay);
    org.junit.Assert.assertNotNull(txAmlCreditReposMon);
    org.junit.Assert.assertNotNull(txAmlCreditReposHist);
  }

  @Override
  public TxAmlCredit findById(TxAmlCreditId txAmlCreditId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + txAmlCreditId);
    Optional<TxAmlCredit> txAmlCredit = null;
    if (dbName.equals(ContentName.onDay))
      txAmlCredit = txAmlCreditReposDay.findById(txAmlCreditId);
    else if (dbName.equals(ContentName.onMon))
      txAmlCredit = txAmlCreditReposMon.findById(txAmlCreditId);
    else if (dbName.equals(ContentName.onHist))
      txAmlCredit = txAmlCreditReposHist.findById(txAmlCreditId);
    else 
      txAmlCredit = txAmlCreditRepos.findById(txAmlCreditId);
    TxAmlCredit obj = txAmlCredit.isPresent() ? txAmlCredit.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxAmlCredit> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAmlCredit> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataDt", "CustKey"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txAmlCreditReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAmlCreditReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAmlCreditReposHist.findAll(pageable);
    else 
      slice = txAmlCreditRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAmlCredit> processAll(String reviewType_0, int dataDt_1, int dataDt_2, String processType_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAmlCredit> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("processAll " + dbName + " : " + "reviewType_0 : " + reviewType_0 + " dataDt_1 : " +  dataDt_1 + " dataDt_2 : " +  dataDt_2 + " processType_3 : " +  processType_3);
    if (dbName.equals(ContentName.onDay))
      slice = txAmlCreditReposDay.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAmlCreditReposMon.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAmlCreditReposHist.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, pageable);
    else 
      slice = txAmlCreditRepos.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAmlCredit> processNo(String reviewType_0, int dataDt_1, int dataDt_2, String processType_3, int processCount_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAmlCredit> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("processNo " + dbName + " : " + "reviewType_0 : " + reviewType_0 + " dataDt_1 : " +  dataDt_1 + " dataDt_2 : " +  dataDt_2 + " processType_3 : " +  processType_3 + " processCount_4 : " +  processCount_4);
    if (dbName.equals(ContentName.onDay))
      slice = txAmlCreditReposDay.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeAndProcessCountIsOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, processCount_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAmlCreditReposMon.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeAndProcessCountIsOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, processCount_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAmlCreditReposHist.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeAndProcessCountIsOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, processCount_4, pageable);
    else 
      slice = txAmlCreditRepos.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeAndProcessCountIsOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, processCount_4, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAmlCredit> processYes(String reviewType_0, int dataDt_1, int dataDt_2, String processType_3, int processCount_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAmlCredit> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("processYes " + dbName + " : " + "reviewType_0 : " + reviewType_0 + " dataDt_1 : " +  dataDt_1 + " dataDt_2 : " +  dataDt_2 + " processType_3 : " +  processType_3 + " processCount_4 : " +  processCount_4);
    if (dbName.equals(ContentName.onDay))
      slice = txAmlCreditReposDay.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeAndProcessCountGreaterThanOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, processCount_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAmlCreditReposMon.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeAndProcessCountGreaterThanOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, processCount_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAmlCreditReposHist.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeAndProcessCountGreaterThanOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, processCount_4, pageable);
    else 
      slice = txAmlCreditRepos.findAllByReviewTypeIsAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeAndProcessCountGreaterThanOrderByDataDtAscCustKeyAsc(reviewType_0, dataDt_1, dataDt_2, processType_3, processCount_4, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAmlCredit> dataDtAll(int dataDt_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAmlCredit> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("dataDtAll " + dbName + " : " + "dataDt_0 : " + dataDt_0);
    if (dbName.equals(ContentName.onDay))
      slice = txAmlCreditReposDay.findAllByDataDtIs(dataDt_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAmlCreditReposMon.findAllByDataDtIs(dataDt_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAmlCreditReposHist.findAllByDataDtIs(dataDt_0, pageable);
    else 
      slice = txAmlCreditRepos.findAllByDataDtIs(dataDt_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxAmlCredit holdById(TxAmlCreditId txAmlCreditId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + txAmlCreditId);
    Optional<TxAmlCredit> txAmlCredit = null;
    if (dbName.equals(ContentName.onDay))
      txAmlCredit = txAmlCreditReposDay.findByTxAmlCreditId(txAmlCreditId);
    else if (dbName.equals(ContentName.onMon))
      txAmlCredit = txAmlCreditReposMon.findByTxAmlCreditId(txAmlCreditId);
    else if (dbName.equals(ContentName.onHist))
      txAmlCredit = txAmlCreditReposHist.findByTxAmlCreditId(txAmlCreditId);
    else 
      txAmlCredit = txAmlCreditRepos.findByTxAmlCreditId(txAmlCreditId);
    return txAmlCredit.isPresent() ? txAmlCredit.get() : null;
  }

  @Override
  public TxAmlCredit holdById(TxAmlCredit txAmlCredit, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + txAmlCredit.getTxAmlCreditId());
    Optional<TxAmlCredit> txAmlCreditT = null;
    if (dbName.equals(ContentName.onDay))
      txAmlCreditT = txAmlCreditReposDay.findByTxAmlCreditId(txAmlCredit.getTxAmlCreditId());
    else if (dbName.equals(ContentName.onMon))
      txAmlCreditT = txAmlCreditReposMon.findByTxAmlCreditId(txAmlCredit.getTxAmlCreditId());
    else if (dbName.equals(ContentName.onHist))
      txAmlCreditT = txAmlCreditReposHist.findByTxAmlCreditId(txAmlCredit.getTxAmlCreditId());
    else 
      txAmlCreditT = txAmlCreditRepos.findByTxAmlCreditId(txAmlCredit.getTxAmlCreditId());
    return txAmlCreditT.isPresent() ? txAmlCreditT.get() : null;
  }

  @Override
  public TxAmlCredit insert(TxAmlCredit txAmlCredit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Insert..." + dbName + " " + txAmlCredit.getTxAmlCreditId());
    if (this.findById(txAmlCredit.getTxAmlCreditId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txAmlCredit.setCreateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAmlCreditReposDay.saveAndFlush(txAmlCredit);	
    else if (dbName.equals(ContentName.onMon))
      return txAmlCreditReposMon.saveAndFlush(txAmlCredit);
    else if (dbName.equals(ContentName.onHist))
      return txAmlCreditReposHist.saveAndFlush(txAmlCredit);
    else 
    return txAmlCreditRepos.saveAndFlush(txAmlCredit);
  }

  @Override
  public TxAmlCredit update(TxAmlCredit txAmlCredit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + txAmlCredit.getTxAmlCreditId());
    if (!empNot.isEmpty())
      txAmlCredit.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAmlCreditReposDay.saveAndFlush(txAmlCredit);	
    else if (dbName.equals(ContentName.onMon))
      return txAmlCreditReposMon.saveAndFlush(txAmlCredit);
    else if (dbName.equals(ContentName.onHist))
      return txAmlCreditReposHist.saveAndFlush(txAmlCredit);
    else 
    return txAmlCreditRepos.saveAndFlush(txAmlCredit);
  }

  @Override
  public TxAmlCredit update2(TxAmlCredit txAmlCredit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + txAmlCredit.getTxAmlCreditId());
    if (!empNot.isEmpty())
      txAmlCredit.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txAmlCreditReposDay.saveAndFlush(txAmlCredit);	
    else if (dbName.equals(ContentName.onMon))
      txAmlCreditReposMon.saveAndFlush(txAmlCredit);
    else if (dbName.equals(ContentName.onHist))
        txAmlCreditReposHist.saveAndFlush(txAmlCredit);
    else 
      txAmlCreditRepos.saveAndFlush(txAmlCredit);	
    return this.findById(txAmlCredit.getTxAmlCreditId());
  }

  @Override
  public void delete(TxAmlCredit txAmlCredit, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + txAmlCredit.getTxAmlCreditId());
    if (dbName.equals(ContentName.onDay)) {
      txAmlCreditReposDay.delete(txAmlCredit);	
      txAmlCreditReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlCreditReposMon.delete(txAmlCredit);	
      txAmlCreditReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlCreditReposHist.delete(txAmlCredit);
      txAmlCreditReposHist.flush();
    }
    else {
      txAmlCreditRepos.delete(txAmlCredit);
      txAmlCreditRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxAmlCredit> txAmlCredit, TitaVo... titaVo) throws DBException {
    if (txAmlCredit == null || txAmlCredit.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}    logger.info("InsertAll...");
    for (TxAmlCredit t : txAmlCredit) 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txAmlCredit = txAmlCreditReposDay.saveAll(txAmlCredit);	
      txAmlCreditReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlCredit = txAmlCreditReposMon.saveAll(txAmlCredit);	
      txAmlCreditReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlCredit = txAmlCreditReposHist.saveAll(txAmlCredit);
      txAmlCreditReposHist.flush();
    }
    else {
      txAmlCredit = txAmlCreditRepos.saveAll(txAmlCredit);
      txAmlCreditRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxAmlCredit> txAmlCredit, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (txAmlCredit == null || txAmlCredit.size() == 0)
      throw new DBException(6);

    for (TxAmlCredit t : txAmlCredit) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txAmlCredit = txAmlCreditReposDay.saveAll(txAmlCredit);	
      txAmlCreditReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlCredit = txAmlCreditReposMon.saveAll(txAmlCredit);	
      txAmlCreditReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlCredit = txAmlCreditReposHist.saveAll(txAmlCredit);
      txAmlCreditReposHist.flush();
    }
    else {
      txAmlCredit = txAmlCreditRepos.saveAll(txAmlCredit);
      txAmlCreditRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxAmlCredit> txAmlCredit, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txAmlCredit == null || txAmlCredit.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txAmlCreditReposDay.deleteAll(txAmlCredit);	
      txAmlCreditReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlCreditReposMon.deleteAll(txAmlCredit);	
      txAmlCreditReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlCreditReposHist.deleteAll(txAmlCredit);
      txAmlCreditReposHist.flush();
    }
    else {
      txAmlCreditRepos.deleteAll(txAmlCredit);
      txAmlCreditRepos.flush();
    }
  }

}
