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
import com.st1.itx.db.domain.NegFinShareLog;
import com.st1.itx.db.domain.NegFinShareLogId;
import com.st1.itx.db.repository.online.NegFinShareLogRepository;
import com.st1.itx.db.repository.day.NegFinShareLogRepositoryDay;
import com.st1.itx.db.repository.mon.NegFinShareLogRepositoryMon;
import com.st1.itx.db.repository.hist.NegFinShareLogRepositoryHist;
import com.st1.itx.db.service.NegFinShareLogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("negFinShareLogService")
@Repository
public class NegFinShareLogServiceImpl implements NegFinShareLogService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(NegFinShareLogServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private NegFinShareLogRepository negFinShareLogRepos;

  @Autowired
  private NegFinShareLogRepositoryDay negFinShareLogReposDay;

  @Autowired
  private NegFinShareLogRepositoryMon negFinShareLogReposMon;

  @Autowired
  private NegFinShareLogRepositoryHist negFinShareLogReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(negFinShareLogRepos);
    org.junit.Assert.assertNotNull(negFinShareLogReposDay);
    org.junit.Assert.assertNotNull(negFinShareLogReposMon);
    org.junit.Assert.assertNotNull(negFinShareLogReposHist);
  }

  @Override
  public NegFinShareLog findById(NegFinShareLogId negFinShareLogId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + negFinShareLogId);
    Optional<NegFinShareLog> negFinShareLog = null;
    if (dbName.equals(ContentName.onDay))
      negFinShareLog = negFinShareLogReposDay.findById(negFinShareLogId);
    else if (dbName.equals(ContentName.onMon))
      negFinShareLog = negFinShareLogReposMon.findById(negFinShareLogId);
    else if (dbName.equals(ContentName.onHist))
      negFinShareLog = negFinShareLogReposHist.findById(negFinShareLogId);
    else 
      negFinShareLog = negFinShareLogRepos.findById(negFinShareLogId);
    NegFinShareLog obj = negFinShareLog.isPresent() ? negFinShareLog.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<NegFinShareLog> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegFinShareLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "CaseSeq", "Seq", "FinCode"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = negFinShareLogReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negFinShareLogReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negFinShareLogReposHist.findAll(pageable);
    else 
      slice = negFinShareLogRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegFinShareLog> FindAllFinCode(int custNo_0, int caseSeq_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegFinShareLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("FindAllFinCode " + dbName + " : " + "custNo_0 : " + custNo_0 + " caseSeq_1 : " +  caseSeq_1);
    if (dbName.equals(ContentName.onDay))
      slice = negFinShareLogReposDay.findAllByCustNoIsAndCaseSeqIsOrderByCustNoAscCaseSeqAscSeqDescFinCodeAsc(custNo_0, caseSeq_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negFinShareLogReposMon.findAllByCustNoIsAndCaseSeqIsOrderByCustNoAscCaseSeqAscSeqDescFinCodeAsc(custNo_0, caseSeq_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negFinShareLogReposHist.findAllByCustNoIsAndCaseSeqIsOrderByCustNoAscCaseSeqAscSeqDescFinCodeAsc(custNo_0, caseSeq_1, pageable);
    else 
      slice = negFinShareLogRepos.findAllByCustNoIsAndCaseSeqIsOrderByCustNoAscCaseSeqAscSeqDescFinCodeAsc(custNo_0, caseSeq_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<NegFinShareLog> FindNewSeq(int custNo_0, int caseSeq_1, int seq_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<NegFinShareLog> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("FindNewSeq " + dbName + " : " + "custNo_0 : " + custNo_0 + " caseSeq_1 : " +  caseSeq_1 + " seq_2 : " +  seq_2);
    if (dbName.equals(ContentName.onDay))
      slice = negFinShareLogReposDay.findAllByCustNoIsAndCaseSeqIsAndSeqIs(custNo_0, caseSeq_1, seq_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = negFinShareLogReposMon.findAllByCustNoIsAndCaseSeqIsAndSeqIs(custNo_0, caseSeq_1, seq_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = negFinShareLogReposHist.findAllByCustNoIsAndCaseSeqIsAndSeqIs(custNo_0, caseSeq_1, seq_2, pageable);
    else 
      slice = negFinShareLogRepos.findAllByCustNoIsAndCaseSeqIsAndSeqIs(custNo_0, caseSeq_1, seq_2, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public NegFinShareLog holdById(NegFinShareLogId negFinShareLogId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + negFinShareLogId);
    Optional<NegFinShareLog> negFinShareLog = null;
    if (dbName.equals(ContentName.onDay))
      negFinShareLog = negFinShareLogReposDay.findByNegFinShareLogId(negFinShareLogId);
    else if (dbName.equals(ContentName.onMon))
      negFinShareLog = negFinShareLogReposMon.findByNegFinShareLogId(negFinShareLogId);
    else if (dbName.equals(ContentName.onHist))
      negFinShareLog = negFinShareLogReposHist.findByNegFinShareLogId(negFinShareLogId);
    else 
      negFinShareLog = negFinShareLogRepos.findByNegFinShareLogId(negFinShareLogId);
    return negFinShareLog.isPresent() ? negFinShareLog.get() : null;
  }

  @Override
  public NegFinShareLog holdById(NegFinShareLog negFinShareLog, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + negFinShareLog.getNegFinShareLogId());
    Optional<NegFinShareLog> negFinShareLogT = null;
    if (dbName.equals(ContentName.onDay))
      negFinShareLogT = negFinShareLogReposDay.findByNegFinShareLogId(negFinShareLog.getNegFinShareLogId());
    else if (dbName.equals(ContentName.onMon))
      negFinShareLogT = negFinShareLogReposMon.findByNegFinShareLogId(negFinShareLog.getNegFinShareLogId());
    else if (dbName.equals(ContentName.onHist))
      negFinShareLogT = negFinShareLogReposHist.findByNegFinShareLogId(negFinShareLog.getNegFinShareLogId());
    else 
      negFinShareLogT = negFinShareLogRepos.findByNegFinShareLogId(negFinShareLog.getNegFinShareLogId());
    return negFinShareLogT.isPresent() ? negFinShareLogT.get() : null;
  }

  @Override
  public NegFinShareLog insert(NegFinShareLog negFinShareLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + negFinShareLog.getNegFinShareLogId());
    if (this.findById(negFinShareLog.getNegFinShareLogId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      negFinShareLog.setCreateEmpNo(empNot);

    if(negFinShareLog.getLastUpdateEmpNo() == null || negFinShareLog.getLastUpdateEmpNo().isEmpty())
      negFinShareLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negFinShareLogReposDay.saveAndFlush(negFinShareLog);	
    else if (dbName.equals(ContentName.onMon))
      return negFinShareLogReposMon.saveAndFlush(negFinShareLog);
    else if (dbName.equals(ContentName.onHist))
      return negFinShareLogReposHist.saveAndFlush(negFinShareLog);
    else 
    return negFinShareLogRepos.saveAndFlush(negFinShareLog);
  }

  @Override
  public NegFinShareLog update(NegFinShareLog negFinShareLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + negFinShareLog.getNegFinShareLogId());
    if (!empNot.isEmpty())
      negFinShareLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return negFinShareLogReposDay.saveAndFlush(negFinShareLog);	
    else if (dbName.equals(ContentName.onMon))
      return negFinShareLogReposMon.saveAndFlush(negFinShareLog);
    else if (dbName.equals(ContentName.onHist))
      return negFinShareLogReposHist.saveAndFlush(negFinShareLog);
    else 
    return negFinShareLogRepos.saveAndFlush(negFinShareLog);
  }

  @Override
  public NegFinShareLog update2(NegFinShareLog negFinShareLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + negFinShareLog.getNegFinShareLogId());
    if (!empNot.isEmpty())
      negFinShareLog.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      negFinShareLogReposDay.saveAndFlush(negFinShareLog);	
    else if (dbName.equals(ContentName.onMon))
      negFinShareLogReposMon.saveAndFlush(negFinShareLog);
    else if (dbName.equals(ContentName.onHist))
        negFinShareLogReposHist.saveAndFlush(negFinShareLog);
    else 
      negFinShareLogRepos.saveAndFlush(negFinShareLog);	
    return this.findById(negFinShareLog.getNegFinShareLogId());
  }

  @Override
  public void delete(NegFinShareLog negFinShareLog, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + negFinShareLog.getNegFinShareLogId());
    if (dbName.equals(ContentName.onDay)) {
      negFinShareLogReposDay.delete(negFinShareLog);	
      negFinShareLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinShareLogReposMon.delete(negFinShareLog);	
      negFinShareLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinShareLogReposHist.delete(negFinShareLog);
      negFinShareLogReposHist.flush();
    }
    else {
      negFinShareLogRepos.delete(negFinShareLog);
      negFinShareLogRepos.flush();
    }
   }

  @Override
  public void insertAll(List<NegFinShareLog> negFinShareLog, TitaVo... titaVo) throws DBException {
    if (negFinShareLog == null || negFinShareLog.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (NegFinShareLog t : negFinShareLog){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      negFinShareLog = negFinShareLogReposDay.saveAll(negFinShareLog);	
      negFinShareLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinShareLog = negFinShareLogReposMon.saveAll(negFinShareLog);	
      negFinShareLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinShareLog = negFinShareLogReposHist.saveAll(negFinShareLog);
      negFinShareLogReposHist.flush();
    }
    else {
      negFinShareLog = negFinShareLogRepos.saveAll(negFinShareLog);
      negFinShareLogRepos.flush();
    }
    }

  @Override
  public void updateAll(List<NegFinShareLog> negFinShareLog, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (negFinShareLog == null || negFinShareLog.size() == 0)
      throw new DBException(6);

    for (NegFinShareLog t : negFinShareLog) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      negFinShareLog = negFinShareLogReposDay.saveAll(negFinShareLog);	
      negFinShareLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinShareLog = negFinShareLogReposMon.saveAll(negFinShareLog);	
      negFinShareLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinShareLog = negFinShareLogReposHist.saveAll(negFinShareLog);
      negFinShareLogReposHist.flush();
    }
    else {
      negFinShareLog = negFinShareLogRepos.saveAll(negFinShareLog);
      negFinShareLogRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<NegFinShareLog> negFinShareLog, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (negFinShareLog == null || negFinShareLog.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      negFinShareLogReposDay.deleteAll(negFinShareLog);	
      negFinShareLogReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      negFinShareLogReposMon.deleteAll(negFinShareLog);	
      negFinShareLogReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      negFinShareLogReposHist.deleteAll(negFinShareLog);
      negFinShareLogReposHist.flush();
    }
    else {
      negFinShareLogRepos.deleteAll(negFinShareLog);
      negFinShareLogRepos.flush();
    }
  }

}
