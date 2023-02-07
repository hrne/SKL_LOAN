package com.st1.itx.db.service.springjpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

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
import com.st1.itx.db.domain.TxAmlRatingAppl;
import com.st1.itx.db.repository.online.TxAmlRatingApplRepository;
import com.st1.itx.db.repository.day.TxAmlRatingApplRepositoryDay;
import com.st1.itx.db.repository.mon.TxAmlRatingApplRepositoryMon;
import com.st1.itx.db.repository.hist.TxAmlRatingApplRepositoryHist;
import com.st1.itx.db.service.TxAmlRatingApplService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txAmlRatingApplService")
@Repository
public class TxAmlRatingApplServiceImpl extends ASpringJpaParm implements TxAmlRatingApplService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxAmlRatingApplRepository txAmlRatingApplRepos;

  @Autowired
  private TxAmlRatingApplRepositoryDay txAmlRatingApplReposDay;

  @Autowired
  private TxAmlRatingApplRepositoryMon txAmlRatingApplReposMon;

  @Autowired
  private TxAmlRatingApplRepositoryHist txAmlRatingApplReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txAmlRatingApplRepos);
    org.junit.Assert.assertNotNull(txAmlRatingApplReposDay);
    org.junit.Assert.assertNotNull(txAmlRatingApplReposMon);
    org.junit.Assert.assertNotNull(txAmlRatingApplReposHist);
  }

  @Override
  public TxAmlRatingAppl findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<TxAmlRatingAppl> txAmlRatingAppl = null;
    if (dbName.equals(ContentName.onDay))
      txAmlRatingAppl = txAmlRatingApplReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      txAmlRatingAppl = txAmlRatingApplReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      txAmlRatingAppl = txAmlRatingApplReposHist.findById(logNo);
    else 
      txAmlRatingAppl = txAmlRatingApplRepos.findById(logNo);
    TxAmlRatingAppl obj = txAmlRatingAppl.isPresent() ? txAmlRatingAppl.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxAmlRatingAppl> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAmlRatingAppl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txAmlRatingApplReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAmlRatingApplReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAmlRatingApplReposHist.findAll(pageable);
    else 
      slice = txAmlRatingApplRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxAmlRatingAppl> findByCaseNo(String caseNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxAmlRatingAppl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByCaseNo " + dbName + " : " + "caseNo_0 : " + caseNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = txAmlRatingApplReposDay.findAllByCaseNoIsOrderByCaseNoAsc(caseNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txAmlRatingApplReposMon.findAllByCaseNoIsOrderByCaseNoAsc(caseNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txAmlRatingApplReposHist.findAllByCaseNoIsOrderByCaseNoAsc(caseNo_0, pageable);
    else 
      slice = txAmlRatingApplRepos.findAllByCaseNoIsOrderByCaseNoAsc(caseNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxAmlRatingAppl holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<TxAmlRatingAppl> txAmlRatingAppl = null;
    if (dbName.equals(ContentName.onDay))
      txAmlRatingAppl = txAmlRatingApplReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      txAmlRatingAppl = txAmlRatingApplReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      txAmlRatingAppl = txAmlRatingApplReposHist.findByLogNo(logNo);
    else 
      txAmlRatingAppl = txAmlRatingApplRepos.findByLogNo(logNo);
    return txAmlRatingAppl.isPresent() ? txAmlRatingAppl.get() : null;
  }

  @Override
  public TxAmlRatingAppl holdById(TxAmlRatingAppl txAmlRatingAppl, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txAmlRatingAppl.getLogNo());
    Optional<TxAmlRatingAppl> txAmlRatingApplT = null;
    if (dbName.equals(ContentName.onDay))
      txAmlRatingApplT = txAmlRatingApplReposDay.findByLogNo(txAmlRatingAppl.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      txAmlRatingApplT = txAmlRatingApplReposMon.findByLogNo(txAmlRatingAppl.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      txAmlRatingApplT = txAmlRatingApplReposHist.findByLogNo(txAmlRatingAppl.getLogNo());
    else 
      txAmlRatingApplT = txAmlRatingApplRepos.findByLogNo(txAmlRatingAppl.getLogNo());
    return txAmlRatingApplT.isPresent() ? txAmlRatingApplT.get() : null;
  }

  @Override
  public TxAmlRatingAppl insert(TxAmlRatingAppl txAmlRatingAppl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txAmlRatingAppl.getLogNo());
    if (this.findById(txAmlRatingAppl.getLogNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txAmlRatingAppl.setCreateEmpNo(empNot);

    if(txAmlRatingAppl.getLastUpdateEmpNo() == null || txAmlRatingAppl.getLastUpdateEmpNo().isEmpty())
      txAmlRatingAppl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAmlRatingApplReposDay.saveAndFlush(txAmlRatingAppl);	
    else if (dbName.equals(ContentName.onMon))
      return txAmlRatingApplReposMon.saveAndFlush(txAmlRatingAppl);
    else if (dbName.equals(ContentName.onHist))
      return txAmlRatingApplReposHist.saveAndFlush(txAmlRatingAppl);
    else 
    return txAmlRatingApplRepos.saveAndFlush(txAmlRatingAppl);
  }

  @Override
  public TxAmlRatingAppl update(TxAmlRatingAppl txAmlRatingAppl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txAmlRatingAppl.getLogNo());
    if (!empNot.isEmpty())
      txAmlRatingAppl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txAmlRatingApplReposDay.saveAndFlush(txAmlRatingAppl);	
    else if (dbName.equals(ContentName.onMon))
      return txAmlRatingApplReposMon.saveAndFlush(txAmlRatingAppl);
    else if (dbName.equals(ContentName.onHist))
      return txAmlRatingApplReposHist.saveAndFlush(txAmlRatingAppl);
    else 
    return txAmlRatingApplRepos.saveAndFlush(txAmlRatingAppl);
  }

  @Override
  public TxAmlRatingAppl update2(TxAmlRatingAppl txAmlRatingAppl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txAmlRatingAppl.getLogNo());
    if (!empNot.isEmpty())
      txAmlRatingAppl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txAmlRatingApplReposDay.saveAndFlush(txAmlRatingAppl);	
    else if (dbName.equals(ContentName.onMon))
      txAmlRatingApplReposMon.saveAndFlush(txAmlRatingAppl);
    else if (dbName.equals(ContentName.onHist))
        txAmlRatingApplReposHist.saveAndFlush(txAmlRatingAppl);
    else 
      txAmlRatingApplRepos.saveAndFlush(txAmlRatingAppl);	
    return this.findById(txAmlRatingAppl.getLogNo());
  }

  @Override
  public void delete(TxAmlRatingAppl txAmlRatingAppl, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txAmlRatingAppl.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      txAmlRatingApplReposDay.delete(txAmlRatingAppl);	
      txAmlRatingApplReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlRatingApplReposMon.delete(txAmlRatingAppl);	
      txAmlRatingApplReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlRatingApplReposHist.delete(txAmlRatingAppl);
      txAmlRatingApplReposHist.flush();
    }
    else {
      txAmlRatingApplRepos.delete(txAmlRatingAppl);
      txAmlRatingApplRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxAmlRatingAppl> txAmlRatingAppl, TitaVo... titaVo) throws DBException {
    if (txAmlRatingAppl == null || txAmlRatingAppl.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxAmlRatingAppl t : txAmlRatingAppl){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txAmlRatingAppl = txAmlRatingApplReposDay.saveAll(txAmlRatingAppl);	
      txAmlRatingApplReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlRatingAppl = txAmlRatingApplReposMon.saveAll(txAmlRatingAppl);	
      txAmlRatingApplReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlRatingAppl = txAmlRatingApplReposHist.saveAll(txAmlRatingAppl);
      txAmlRatingApplReposHist.flush();
    }
    else {
      txAmlRatingAppl = txAmlRatingApplRepos.saveAll(txAmlRatingAppl);
      txAmlRatingApplRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxAmlRatingAppl> txAmlRatingAppl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txAmlRatingAppl == null || txAmlRatingAppl.size() == 0)
      throw new DBException(6);

    for (TxAmlRatingAppl t : txAmlRatingAppl) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txAmlRatingAppl = txAmlRatingApplReposDay.saveAll(txAmlRatingAppl);	
      txAmlRatingApplReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlRatingAppl = txAmlRatingApplReposMon.saveAll(txAmlRatingAppl);	
      txAmlRatingApplReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlRatingAppl = txAmlRatingApplReposHist.saveAll(txAmlRatingAppl);
      txAmlRatingApplReposHist.flush();
    }
    else {
      txAmlRatingAppl = txAmlRatingApplRepos.saveAll(txAmlRatingAppl);
      txAmlRatingApplRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxAmlRatingAppl> txAmlRatingAppl, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txAmlRatingAppl == null || txAmlRatingAppl.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txAmlRatingApplReposDay.deleteAll(txAmlRatingAppl);	
      txAmlRatingApplReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txAmlRatingApplReposMon.deleteAll(txAmlRatingAppl);	
      txAmlRatingApplReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txAmlRatingApplReposHist.deleteAll(txAmlRatingAppl);
      txAmlRatingApplReposHist.flush();
    }
    else {
      txAmlRatingApplRepos.deleteAll(txAmlRatingAppl);
      txAmlRatingApplRepos.flush();
    }
  }

}
