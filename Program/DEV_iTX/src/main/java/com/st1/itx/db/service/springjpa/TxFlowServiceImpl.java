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
import com.st1.itx.db.domain.TxFlow;
import com.st1.itx.db.domain.TxFlowId;
import com.st1.itx.db.repository.online.TxFlowRepository;
import com.st1.itx.db.repository.day.TxFlowRepositoryDay;
import com.st1.itx.db.repository.mon.TxFlowRepositoryMon;
import com.st1.itx.db.repository.hist.TxFlowRepositoryHist;
import com.st1.itx.db.service.TxFlowService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txFlowService")
@Repository
public class TxFlowServiceImpl implements TxFlowService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(TxFlowServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxFlowRepository txFlowRepos;

  @Autowired
  private TxFlowRepositoryDay txFlowReposDay;

  @Autowired
  private TxFlowRepositoryMon txFlowReposMon;

  @Autowired
  private TxFlowRepositoryHist txFlowReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txFlowRepos);
    org.junit.Assert.assertNotNull(txFlowReposDay);
    org.junit.Assert.assertNotNull(txFlowReposMon);
    org.junit.Assert.assertNotNull(txFlowReposHist);
  }

  @Override
  public TxFlow findById(TxFlowId txFlowId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + txFlowId);
    Optional<TxFlow> txFlow = null;
    if (dbName.equals(ContentName.onDay))
      txFlow = txFlowReposDay.findById(txFlowId);
    else if (dbName.equals(ContentName.onMon))
      txFlow = txFlowReposMon.findById(txFlowId);
    else if (dbName.equals(ContentName.onHist))
      txFlow = txFlowReposHist.findById(txFlowId);
    else 
      txFlow = txFlowRepos.findById(txFlowId);
    TxFlow obj = txFlow.isPresent() ? txFlow.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxFlow> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxFlow> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Entdy", "FlowNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txFlowReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txFlowReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txFlowReposHist.findAll(pageable);
    else 
      slice = txFlowRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxFlow> findByFlowBrNo(int entdy_0, String flowBrNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxFlow> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findByFlowBrNo " + dbName + " : " + "entdy_0 : " + entdy_0 + " flowBrNo_1 : " +  flowBrNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = txFlowReposDay.findAllByEntdyIsAndFlowBrNoIsOrderByFlowNoAsc(entdy_0, flowBrNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txFlowReposMon.findAllByEntdyIsAndFlowBrNoIsOrderByFlowNoAsc(entdy_0, flowBrNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txFlowReposHist.findAllByEntdyIsAndFlowBrNoIsOrderByFlowNoAsc(entdy_0, flowBrNo_1, pageable);
    else 
      slice = txFlowRepos.findAllByEntdyIsAndFlowBrNoIsOrderByFlowNoAsc(entdy_0, flowBrNo_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxFlow> findByLC003(int entdy_0, String flowBrNo_1, int flowMode_2, String tranNo_3, List<String> flowGroupNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxFlow> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findByLC003 " + dbName + " : " + "entdy_0 : " + entdy_0 + " flowBrNo_1 : " +  flowBrNo_1 + " flowMode_2 : " +  flowMode_2 + " tranNo_3 : " +  tranNo_3 + " flowGroupNo_4 : " +  flowGroupNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = txFlowReposDay.findAllByEntdyIsAndFlowBrNoIsAndFlowModeIsAndTranNoLikeAndFlowGroupNoInOrderByFlowNoAsc(entdy_0, flowBrNo_1, flowMode_2, tranNo_3, flowGroupNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txFlowReposMon.findAllByEntdyIsAndFlowBrNoIsAndFlowModeIsAndTranNoLikeAndFlowGroupNoInOrderByFlowNoAsc(entdy_0, flowBrNo_1, flowMode_2, tranNo_3, flowGroupNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txFlowReposHist.findAllByEntdyIsAndFlowBrNoIsAndFlowModeIsAndTranNoLikeAndFlowGroupNoInOrderByFlowNoAsc(entdy_0, flowBrNo_1, flowMode_2, tranNo_3, flowGroupNo_4, pageable);
    else 
      slice = txFlowRepos.findAllByEntdyIsAndFlowBrNoIsAndFlowModeIsAndTranNoLikeAndFlowGroupNoInOrderByFlowNoAsc(entdy_0, flowBrNo_1, flowMode_2, tranNo_3, flowGroupNo_4, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxFlow> findBySecNo(int entdy_0, String secNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxFlow> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findBySecNo " + dbName + " : " + "entdy_0 : " + entdy_0 + " secNo_1 : " +  secNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = txFlowReposDay.findAllByEntdyIsAndSecNoIs(entdy_0, secNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txFlowReposMon.findAllByEntdyIsAndSecNoIs(entdy_0, secNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txFlowReposHist.findAllByEntdyIsAndSecNoIs(entdy_0, secNo_1, pageable);
    else 
      slice = txFlowRepos.findAllByEntdyIsAndSecNoIs(entdy_0, secNo_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxFlow holdById(TxFlowId txFlowId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + txFlowId);
    Optional<TxFlow> txFlow = null;
    if (dbName.equals(ContentName.onDay))
      txFlow = txFlowReposDay.findByTxFlowId(txFlowId);
    else if (dbName.equals(ContentName.onMon))
      txFlow = txFlowReposMon.findByTxFlowId(txFlowId);
    else if (dbName.equals(ContentName.onHist))
      txFlow = txFlowReposHist.findByTxFlowId(txFlowId);
    else 
      txFlow = txFlowRepos.findByTxFlowId(txFlowId);
    return txFlow.isPresent() ? txFlow.get() : null;
  }

  @Override
  public TxFlow holdById(TxFlow txFlow, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + txFlow.getTxFlowId());
    Optional<TxFlow> txFlowT = null;
    if (dbName.equals(ContentName.onDay))
      txFlowT = txFlowReposDay.findByTxFlowId(txFlow.getTxFlowId());
    else if (dbName.equals(ContentName.onMon))
      txFlowT = txFlowReposMon.findByTxFlowId(txFlow.getTxFlowId());
    else if (dbName.equals(ContentName.onHist))
      txFlowT = txFlowReposHist.findByTxFlowId(txFlow.getTxFlowId());
    else 
      txFlowT = txFlowRepos.findByTxFlowId(txFlow.getTxFlowId());
    return txFlowT.isPresent() ? txFlowT.get() : null;
  }

  @Override
  public TxFlow insert(TxFlow txFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Insert..." + dbName + " " + txFlow.getTxFlowId());
    if (this.findById(txFlow.getTxFlowId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txFlow.setCreateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txFlowReposDay.saveAndFlush(txFlow);	
    else if (dbName.equals(ContentName.onMon))
      return txFlowReposMon.saveAndFlush(txFlow);
    else if (dbName.equals(ContentName.onHist))
      return txFlowReposHist.saveAndFlush(txFlow);
    else 
    return txFlowRepos.saveAndFlush(txFlow);
  }

  @Override
  public TxFlow update(TxFlow txFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + txFlow.getTxFlowId());
    if (!empNot.isEmpty())
      txFlow.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txFlowReposDay.saveAndFlush(txFlow);	
    else if (dbName.equals(ContentName.onMon))
      return txFlowReposMon.saveAndFlush(txFlow);
    else if (dbName.equals(ContentName.onHist))
      return txFlowReposHist.saveAndFlush(txFlow);
    else 
    return txFlowRepos.saveAndFlush(txFlow);
  }

  @Override
  public TxFlow update2(TxFlow txFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + txFlow.getTxFlowId());
    if (!empNot.isEmpty())
      txFlow.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txFlowReposDay.saveAndFlush(txFlow);	
    else if (dbName.equals(ContentName.onMon))
      txFlowReposMon.saveAndFlush(txFlow);
    else if (dbName.equals(ContentName.onHist))
        txFlowReposHist.saveAndFlush(txFlow);
    else 
      txFlowRepos.saveAndFlush(txFlow);	
    return this.findById(txFlow.getTxFlowId());
  }

  @Override
  public void delete(TxFlow txFlow, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + txFlow.getTxFlowId());
    if (dbName.equals(ContentName.onDay)) {
      txFlowReposDay.delete(txFlow);	
      txFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txFlowReposMon.delete(txFlow);	
      txFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txFlowReposHist.delete(txFlow);
      txFlowReposHist.flush();
    }
    else {
      txFlowRepos.delete(txFlow);
      txFlowRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxFlow> txFlow, TitaVo... titaVo) throws DBException {
    if (txFlow == null || txFlow.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}    logger.info("InsertAll...");
    for (TxFlow t : txFlow) 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txFlow = txFlowReposDay.saveAll(txFlow);	
      txFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txFlow = txFlowReposMon.saveAll(txFlow);	
      txFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txFlow = txFlowReposHist.saveAll(txFlow);
      txFlowReposHist.flush();
    }
    else {
      txFlow = txFlowRepos.saveAll(txFlow);
      txFlowRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxFlow> txFlow, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (txFlow == null || txFlow.size() == 0)
      throw new DBException(6);

    for (TxFlow t : txFlow) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txFlow = txFlowReposDay.saveAll(txFlow);	
      txFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txFlow = txFlowReposMon.saveAll(txFlow);	
      txFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txFlow = txFlowReposHist.saveAll(txFlow);
      txFlowReposHist.flush();
    }
    else {
      txFlow = txFlowRepos.saveAll(txFlow);
      txFlowRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxFlow> txFlow, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txFlow == null || txFlow.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txFlowReposDay.deleteAll(txFlow);	
      txFlowReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txFlowReposMon.deleteAll(txFlow);	
      txFlowReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txFlowReposHist.deleteAll(txFlow);
      txFlowReposHist.flush();
    }
    else {
      txFlowRepos.deleteAll(txFlow);
      txFlowRepos.flush();
    }
  }

}
