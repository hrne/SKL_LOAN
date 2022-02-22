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
import com.st1.itx.db.domain.TxInquiry;
import com.st1.itx.db.repository.online.TxInquiryRepository;
import com.st1.itx.db.repository.day.TxInquiryRepositoryDay;
import com.st1.itx.db.repository.mon.TxInquiryRepositoryMon;
import com.st1.itx.db.repository.hist.TxInquiryRepositoryHist;
import com.st1.itx.db.service.TxInquiryService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txInquiryService")
@Repository
public class TxInquiryServiceImpl extends ASpringJpaParm implements TxInquiryService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxInquiryRepository txInquiryRepos;

  @Autowired
  private TxInquiryRepositoryDay txInquiryReposDay;

  @Autowired
  private TxInquiryRepositoryMon txInquiryReposMon;

  @Autowired
  private TxInquiryRepositoryHist txInquiryReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txInquiryRepos);
    org.junit.Assert.assertNotNull(txInquiryReposDay);
    org.junit.Assert.assertNotNull(txInquiryReposMon);
    org.junit.Assert.assertNotNull(txInquiryReposHist);
  }

  @Override
  public TxInquiry findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<TxInquiry> txInquiry = null;
    if (dbName.equals(ContentName.onDay))
      txInquiry = txInquiryReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      txInquiry = txInquiryReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      txInquiry = txInquiryReposHist.findById(logNo);
    else 
      txInquiry = txInquiryRepos.findById(logNo);
    TxInquiry obj = txInquiry.isPresent() ? txInquiry.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxInquiry> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxInquiry> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txInquiryReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txInquiryReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txInquiryReposHist.findAll(pageable);
    else 
      slice = txInquiryRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxInquiry> findByCalDate(int calDate_0, int calDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxInquiry> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByCalDate " + dbName + " : " + "calDate_0 : " + calDate_0 + " calDate_1 : " +  calDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = txInquiryReposDay.findAllByCalDateGreaterThanEqualAndCalDateLessThanEqualOrderByCreateDateAsc(calDate_0, calDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txInquiryReposMon.findAllByCalDateGreaterThanEqualAndCalDateLessThanEqualOrderByCreateDateAsc(calDate_0, calDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txInquiryReposHist.findAllByCalDateGreaterThanEqualAndCalDateLessThanEqualOrderByCreateDateAsc(calDate_0, calDate_1, pageable);
    else 
      slice = txInquiryRepos.findAllByCalDateGreaterThanEqualAndCalDateLessThanEqualOrderByCreateDateAsc(calDate_0, calDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxInquiry holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<TxInquiry> txInquiry = null;
    if (dbName.equals(ContentName.onDay))
      txInquiry = txInquiryReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      txInquiry = txInquiryReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      txInquiry = txInquiryReposHist.findByLogNo(logNo);
    else 
      txInquiry = txInquiryRepos.findByLogNo(logNo);
    return txInquiry.isPresent() ? txInquiry.get() : null;
  }

  @Override
  public TxInquiry holdById(TxInquiry txInquiry, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txInquiry.getLogNo());
    Optional<TxInquiry> txInquiryT = null;
    if (dbName.equals(ContentName.onDay))
      txInquiryT = txInquiryReposDay.findByLogNo(txInquiry.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      txInquiryT = txInquiryReposMon.findByLogNo(txInquiry.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      txInquiryT = txInquiryReposHist.findByLogNo(txInquiry.getLogNo());
    else 
      txInquiryT = txInquiryRepos.findByLogNo(txInquiry.getLogNo());
    return txInquiryT.isPresent() ? txInquiryT.get() : null;
  }

  @Override
  public TxInquiry insert(TxInquiry txInquiry, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txInquiry.getLogNo());
    if (this.findById(txInquiry.getLogNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txInquiry.setCreateEmpNo(empNot);

    if(txInquiry.getLastUpdateEmpNo() == null || txInquiry.getLastUpdateEmpNo().isEmpty())
      txInquiry.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txInquiryReposDay.saveAndFlush(txInquiry);	
    else if (dbName.equals(ContentName.onMon))
      return txInquiryReposMon.saveAndFlush(txInquiry);
    else if (dbName.equals(ContentName.onHist))
      return txInquiryReposHist.saveAndFlush(txInquiry);
    else 
    return txInquiryRepos.saveAndFlush(txInquiry);
  }

  @Override
  public TxInquiry update(TxInquiry txInquiry, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txInquiry.getLogNo());
    if (!empNot.isEmpty())
      txInquiry.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txInquiryReposDay.saveAndFlush(txInquiry);	
    else if (dbName.equals(ContentName.onMon))
      return txInquiryReposMon.saveAndFlush(txInquiry);
    else if (dbName.equals(ContentName.onHist))
      return txInquiryReposHist.saveAndFlush(txInquiry);
    else 
    return txInquiryRepos.saveAndFlush(txInquiry);
  }

  @Override
  public TxInquiry update2(TxInquiry txInquiry, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txInquiry.getLogNo());
    if (!empNot.isEmpty())
      txInquiry.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txInquiryReposDay.saveAndFlush(txInquiry);	
    else if (dbName.equals(ContentName.onMon))
      txInquiryReposMon.saveAndFlush(txInquiry);
    else if (dbName.equals(ContentName.onHist))
        txInquiryReposHist.saveAndFlush(txInquiry);
    else 
      txInquiryRepos.saveAndFlush(txInquiry);	
    return this.findById(txInquiry.getLogNo());
  }

  @Override
  public void delete(TxInquiry txInquiry, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txInquiry.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      txInquiryReposDay.delete(txInquiry);	
      txInquiryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txInquiryReposMon.delete(txInquiry);	
      txInquiryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txInquiryReposHist.delete(txInquiry);
      txInquiryReposHist.flush();
    }
    else {
      txInquiryRepos.delete(txInquiry);
      txInquiryRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxInquiry> txInquiry, TitaVo... titaVo) throws DBException {
    if (txInquiry == null || txInquiry.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxInquiry t : txInquiry){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txInquiry = txInquiryReposDay.saveAll(txInquiry);	
      txInquiryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txInquiry = txInquiryReposMon.saveAll(txInquiry);	
      txInquiryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txInquiry = txInquiryReposHist.saveAll(txInquiry);
      txInquiryReposHist.flush();
    }
    else {
      txInquiry = txInquiryRepos.saveAll(txInquiry);
      txInquiryRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxInquiry> txInquiry, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txInquiry == null || txInquiry.size() == 0)
      throw new DBException(6);

    for (TxInquiry t : txInquiry) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txInquiry = txInquiryReposDay.saveAll(txInquiry);	
      txInquiryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txInquiry = txInquiryReposMon.saveAll(txInquiry);	
      txInquiryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txInquiry = txInquiryReposHist.saveAll(txInquiry);
      txInquiryReposHist.flush();
    }
    else {
      txInquiry = txInquiryRepos.saveAll(txInquiry);
      txInquiryRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxInquiry> txInquiry, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txInquiry == null || txInquiry.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txInquiryReposDay.deleteAll(txInquiry);	
      txInquiryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txInquiryReposMon.deleteAll(txInquiry);	
      txInquiryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txInquiryReposHist.deleteAll(txInquiry);
      txInquiryReposHist.flush();
    }
    else {
      txInquiryRepos.deleteAll(txInquiry);
      txInquiryRepos.flush();
    }
  }

}
