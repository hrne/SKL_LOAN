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
import com.st1.itx.db.domain.TxAuthority;
import com.st1.itx.db.domain.TxAuthorityId;
import com.st1.itx.db.repository.online.TxAuthorityRepository;
import com.st1.itx.db.repository.day.TxAuthorityRepositoryDay;
import com.st1.itx.db.repository.mon.TxAuthorityRepositoryMon;
import com.st1.itx.db.repository.hist.TxAuthorityRepositoryHist;
import com.st1.itx.db.service.TxAuthorityService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txAuthorityService")
@Repository
public class TxAuthorityServiceImpl implements TxAuthorityService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(TxAuthorityServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxAuthorityRepository txAuthorityRepos;

  @Autowired
  private TxAuthorityRepositoryDay txAuthorityReposDay;

  @Autowired
  private TxAuthorityRepositoryMon txAuthorityReposMon;

  @Autowired
  private TxAuthorityRepositoryHist txAuthorityReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txAuthorityRepos);
    org.junit.Assert.assertNotNull(txAuthorityReposDay);
    org.junit.Assert.assertNotNull(txAuthorityReposMon);
    org.junit.Assert.assertNotNull(txAuthorityReposHist);
  }

  @Override
  public TxAuthority findById(TxAuthorityId txAuthorityId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + txAuthorityId);
    Optional<TxAuthority> txAuthority = null;
    if (dbName.equals(ContentName.onDay))
      txAuthority = txAuthorityReposDay.findById(txAuthorityId);
    else if (dbName.equals(ContentName.onMon))
      txAuthority = txAuthorityReposMon.findById(txAuthorityId);
    else if (dbName.equals(ContentName.onHist))
      txAuthority = txAuthorityReposHist.findById(txAuthorityId);
    else 
      txAuthority = txAuthorityRepos.findById(txAuthorityId);
    TxAuthority obj = txAuthority.isPresent() ? txAuthority.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxAuthority> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAuthority> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AuthNo", "TranNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txAuthorityReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAuthorityReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAuthorityReposHist.findAll(pageable);
    else 
      slice = txAuthorityRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAuthority> findByAuthNo(String authNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAuthority> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findByAuthNo " + dbName + " : " + "authNo_0 : " + authNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = txAuthorityReposDay.findAllByAuthNoIsOrderByTranNoAsc(authNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAuthorityReposMon.findAllByAuthNoIsOrderByTranNoAsc(authNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAuthorityReposHist.findAllByAuthNoIsOrderByTranNoAsc(authNo_0, pageable);
    else 
      slice = txAuthorityRepos.findAllByAuthNoIsOrderByTranNoAsc(authNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAuthority> findByAuthNo2(String authNo_0, String tranNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAuthority> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findByAuthNo2 " + dbName + " : " + "authNo_0 : " + authNo_0 + " tranNo_1 : " +  tranNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = txAuthorityReposDay.findAllByAuthNoIsAndTranNoNotLikeOrderByTranNoAsc(authNo_0, tranNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAuthorityReposMon.findAllByAuthNoIsAndTranNoNotLikeOrderByTranNoAsc(authNo_0, tranNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAuthorityReposHist.findAllByAuthNoIsAndTranNoNotLikeOrderByTranNoAsc(authNo_0, tranNo_1, pageable);
    else 
      slice = txAuthorityRepos.findAllByAuthNoIsAndTranNoNotLikeOrderByTranNoAsc(authNo_0, tranNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxAuthority holdById(TxAuthorityId txAuthorityId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + txAuthorityId);
    Optional<TxAuthority> txAuthority = null;
    if (dbName.equals(ContentName.onDay))
      txAuthority = txAuthorityReposDay.findByTxAuthorityId(txAuthorityId);
    else if (dbName.equals(ContentName.onMon))
      txAuthority = txAuthorityReposMon.findByTxAuthorityId(txAuthorityId);
    else if (dbName.equals(ContentName.onHist))
      txAuthority = txAuthorityReposHist.findByTxAuthorityId(txAuthorityId);
    else 
      txAuthority = txAuthorityRepos.findByTxAuthorityId(txAuthorityId);
    return txAuthority.isPresent() ? txAuthority.get() : null;
  }

  @Override
  public TxAuthority holdById(TxAuthority txAuthority, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + txAuthority.getTxAuthorityId());
    Optional<TxAuthority> txAuthorityT = null;
    if (dbName.equals(ContentName.onDay))
      txAuthorityT = txAuthorityReposDay.findByTxAuthorityId(txAuthority.getTxAuthorityId());
    else if (dbName.equals(ContentName.onMon))
      txAuthorityT = txAuthorityReposMon.findByTxAuthorityId(txAuthority.getTxAuthorityId());
    else if (dbName.equals(ContentName.onHist))
      txAuthorityT = txAuthorityReposHist.findByTxAuthorityId(txAuthority.getTxAuthorityId());
    else 
      txAuthorityT = txAuthorityRepos.findByTxAuthorityId(txAuthority.getTxAuthorityId());
    return txAuthorityT.isPresent() ? txAuthorityT.get() : null;
  }

  @Override
  public TxAuthority insert(TxAuthority txAuthority, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + txAuthority.getTxAuthorityId());
    if (this.findById(txAuthority.getTxAuthorityId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txAuthority.setCreateEmpNo(empNot);

    if(txAuthority.getLastUpdateEmpNo() == null || txAuthority.getLastUpdateEmpNo().isEmpty())
      txAuthority.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAuthorityReposDay.saveAndFlush(txAuthority);	
    else if (dbName.equals(ContentName.onMon))
      return txAuthorityReposMon.saveAndFlush(txAuthority);
    else if (dbName.equals(ContentName.onHist))
      return txAuthorityReposHist.saveAndFlush(txAuthority);
    else 
    return txAuthorityRepos.saveAndFlush(txAuthority);
  }

  @Override
  public TxAuthority update(TxAuthority txAuthority, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + txAuthority.getTxAuthorityId());
    if (!empNot.isEmpty())
      txAuthority.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAuthorityReposDay.saveAndFlush(txAuthority);	
    else if (dbName.equals(ContentName.onMon))
      return txAuthorityReposMon.saveAndFlush(txAuthority);
    else if (dbName.equals(ContentName.onHist))
      return txAuthorityReposHist.saveAndFlush(txAuthority);
    else 
    return txAuthorityRepos.saveAndFlush(txAuthority);
  }

  @Override
  public TxAuthority update2(TxAuthority txAuthority, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + txAuthority.getTxAuthorityId());
    if (!empNot.isEmpty())
      txAuthority.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txAuthorityReposDay.saveAndFlush(txAuthority);	
    else if (dbName.equals(ContentName.onMon))
      txAuthorityReposMon.saveAndFlush(txAuthority);
    else if (dbName.equals(ContentName.onHist))
        txAuthorityReposHist.saveAndFlush(txAuthority);
    else 
      txAuthorityRepos.saveAndFlush(txAuthority);	
    return this.findById(txAuthority.getTxAuthorityId());
  }

  @Override
  public void delete(TxAuthority txAuthority, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + txAuthority.getTxAuthorityId());
    if (dbName.equals(ContentName.onDay)) {
      txAuthorityReposDay.delete(txAuthority);	
      txAuthorityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAuthorityReposMon.delete(txAuthority);	
      txAuthorityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAuthorityReposHist.delete(txAuthority);
      txAuthorityReposHist.flush();
    }
    else {
      txAuthorityRepos.delete(txAuthority);
      txAuthorityRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxAuthority> txAuthority, TitaVo... titaVo) throws DBException {
    if (txAuthority == null || txAuthority.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (TxAuthority t : txAuthority){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txAuthority = txAuthorityReposDay.saveAll(txAuthority);	
      txAuthorityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAuthority = txAuthorityReposMon.saveAll(txAuthority);	
      txAuthorityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAuthority = txAuthorityReposHist.saveAll(txAuthority);
      txAuthorityReposHist.flush();
    }
    else {
      txAuthority = txAuthorityRepos.saveAll(txAuthority);
      txAuthorityRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxAuthority> txAuthority, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (txAuthority == null || txAuthority.size() == 0)
      throw new DBException(6);

    for (TxAuthority t : txAuthority) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txAuthority = txAuthorityReposDay.saveAll(txAuthority);	
      txAuthorityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAuthority = txAuthorityReposMon.saveAll(txAuthority);	
      txAuthorityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAuthority = txAuthorityReposHist.saveAll(txAuthority);
      txAuthorityReposHist.flush();
    }
    else {
      txAuthority = txAuthorityRepos.saveAll(txAuthority);
      txAuthorityRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxAuthority> txAuthority, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txAuthority == null || txAuthority.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txAuthorityReposDay.deleteAll(txAuthority);	
      txAuthorityReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAuthorityReposMon.deleteAll(txAuthority);	
      txAuthorityReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAuthorityReposHist.deleteAll(txAuthority);
      txAuthorityReposHist.flush();
    }
    else {
      txAuthorityRepos.deleteAll(txAuthority);
      txAuthorityRepos.flush();
    }
  }

}
