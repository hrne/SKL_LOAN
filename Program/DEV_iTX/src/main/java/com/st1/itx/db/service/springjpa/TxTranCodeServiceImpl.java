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
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.repository.online.TxTranCodeRepository;
import com.st1.itx.db.repository.day.TxTranCodeRepositoryDay;
import com.st1.itx.db.repository.mon.TxTranCodeRepositoryMon;
import com.st1.itx.db.repository.hist.TxTranCodeRepositoryHist;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txTranCodeService")
@Repository
public class TxTranCodeServiceImpl implements TxTranCodeService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(TxTranCodeServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxTranCodeRepository txTranCodeRepos;

  @Autowired
  private TxTranCodeRepositoryDay txTranCodeReposDay;

  @Autowired
  private TxTranCodeRepositoryMon txTranCodeReposMon;

  @Autowired
  private TxTranCodeRepositoryHist txTranCodeReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txTranCodeRepos);
    org.junit.Assert.assertNotNull(txTranCodeReposDay);
    org.junit.Assert.assertNotNull(txTranCodeReposMon);
    org.junit.Assert.assertNotNull(txTranCodeReposHist);
  }

  @Override
  public TxTranCode findById(String tranNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + tranNo);
    Optional<TxTranCode> txTranCode = null;
    if (dbName.equals(ContentName.onDay))
      txTranCode = txTranCodeReposDay.findById(tranNo);
    else if (dbName.equals(ContentName.onMon))
      txTranCode = txTranCodeReposMon.findById(tranNo);
    else if (dbName.equals(ContentName.onHist))
      txTranCode = txTranCodeReposHist.findById(tranNo);
    else 
      txTranCode = txTranCodeRepos.findById(tranNo);
    TxTranCode obj = txTranCode.isPresent() ? txTranCode.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxTranCode> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxTranCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "TranNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txTranCodeReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txTranCodeReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txTranCodeReposHist.findAll(pageable);
    else 
      slice = txTranCodeRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxTranCode> TranNoLike(String tranNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxTranCode> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("TranNoLike " + dbName + " : " + "tranNo_0 : " + tranNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = txTranCodeReposDay.findAllByTranNoLikeOrderByTranNoAsc(tranNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txTranCodeReposMon.findAllByTranNoLikeOrderByTranNoAsc(tranNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txTranCodeReposHist.findAllByTranNoLikeOrderByTranNoAsc(tranNo_0, pageable);
    else 
      slice = txTranCodeRepos.findAllByTranNoLikeOrderByTranNoAsc(tranNo_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxTranCode holdById(String tranNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + tranNo);
    Optional<TxTranCode> txTranCode = null;
    if (dbName.equals(ContentName.onDay))
      txTranCode = txTranCodeReposDay.findByTranNo(tranNo);
    else if (dbName.equals(ContentName.onMon))
      txTranCode = txTranCodeReposMon.findByTranNo(tranNo);
    else if (dbName.equals(ContentName.onHist))
      txTranCode = txTranCodeReposHist.findByTranNo(tranNo);
    else 
      txTranCode = txTranCodeRepos.findByTranNo(tranNo);
    return txTranCode.isPresent() ? txTranCode.get() : null;
  }

  @Override
  public TxTranCode holdById(TxTranCode txTranCode, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + txTranCode.getTranNo());
    Optional<TxTranCode> txTranCodeT = null;
    if (dbName.equals(ContentName.onDay))
      txTranCodeT = txTranCodeReposDay.findByTranNo(txTranCode.getTranNo());
    else if (dbName.equals(ContentName.onMon))
      txTranCodeT = txTranCodeReposMon.findByTranNo(txTranCode.getTranNo());
    else if (dbName.equals(ContentName.onHist))
      txTranCodeT = txTranCodeReposHist.findByTranNo(txTranCode.getTranNo());
    else 
      txTranCodeT = txTranCodeRepos.findByTranNo(txTranCode.getTranNo());
    return txTranCodeT.isPresent() ? txTranCodeT.get() : null;
  }

  @Override
  public TxTranCode insert(TxTranCode txTranCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + txTranCode.getTranNo());
    if (this.findById(txTranCode.getTranNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txTranCode.setCreateEmpNo(empNot);

    if(txTranCode.getLastUpdateEmpNo() == null || txTranCode.getLastUpdateEmpNo().isEmpty())
      txTranCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txTranCodeReposDay.saveAndFlush(txTranCode);	
    else if (dbName.equals(ContentName.onMon))
      return txTranCodeReposMon.saveAndFlush(txTranCode);
    else if (dbName.equals(ContentName.onHist))
      return txTranCodeReposHist.saveAndFlush(txTranCode);
    else 
    return txTranCodeRepos.saveAndFlush(txTranCode);
  }

  @Override
  public TxTranCode update(TxTranCode txTranCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + txTranCode.getTranNo());
    if (!empNot.isEmpty())
      txTranCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txTranCodeReposDay.saveAndFlush(txTranCode);	
    else if (dbName.equals(ContentName.onMon))
      return txTranCodeReposMon.saveAndFlush(txTranCode);
    else if (dbName.equals(ContentName.onHist))
      return txTranCodeReposHist.saveAndFlush(txTranCode);
    else 
    return txTranCodeRepos.saveAndFlush(txTranCode);
  }

  @Override
  public TxTranCode update2(TxTranCode txTranCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + txTranCode.getTranNo());
    if (!empNot.isEmpty())
      txTranCode.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txTranCodeReposDay.saveAndFlush(txTranCode);	
    else if (dbName.equals(ContentName.onMon))
      txTranCodeReposMon.saveAndFlush(txTranCode);
    else if (dbName.equals(ContentName.onHist))
        txTranCodeReposHist.saveAndFlush(txTranCode);
    else 
      txTranCodeRepos.saveAndFlush(txTranCode);	
    return this.findById(txTranCode.getTranNo());
  }

  @Override
  public void delete(TxTranCode txTranCode, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + txTranCode.getTranNo());
    if (dbName.equals(ContentName.onDay)) {
      txTranCodeReposDay.delete(txTranCode);	
      txTranCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTranCodeReposMon.delete(txTranCode);	
      txTranCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTranCodeReposHist.delete(txTranCode);
      txTranCodeReposHist.flush();
    }
    else {
      txTranCodeRepos.delete(txTranCode);
      txTranCodeRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxTranCode> txTranCode, TitaVo... titaVo) throws DBException {
    if (txTranCode == null || txTranCode.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (TxTranCode t : txTranCode){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txTranCode = txTranCodeReposDay.saveAll(txTranCode);	
      txTranCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTranCode = txTranCodeReposMon.saveAll(txTranCode);	
      txTranCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTranCode = txTranCodeReposHist.saveAll(txTranCode);
      txTranCodeReposHist.flush();
    }
    else {
      txTranCode = txTranCodeRepos.saveAll(txTranCode);
      txTranCodeRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxTranCode> txTranCode, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (txTranCode == null || txTranCode.size() == 0)
      throw new DBException(6);

    for (TxTranCode t : txTranCode) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txTranCode = txTranCodeReposDay.saveAll(txTranCode);	
      txTranCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTranCode = txTranCodeReposMon.saveAll(txTranCode);	
      txTranCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTranCode = txTranCodeReposHist.saveAll(txTranCode);
      txTranCodeReposHist.flush();
    }
    else {
      txTranCode = txTranCodeRepos.saveAll(txTranCode);
      txTranCodeRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxTranCode> txTranCode, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txTranCode == null || txTranCode.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txTranCodeReposDay.deleteAll(txTranCode);	
      txTranCodeReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txTranCodeReposMon.deleteAll(txTranCode);	
      txTranCodeReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txTranCodeReposHist.deleteAll(txTranCode);
      txTranCodeReposHist.flush();
    }
    else {
      txTranCodeRepos.deleteAll(txTranCode);
      txTranCodeRepos.flush();
    }
  }

}
