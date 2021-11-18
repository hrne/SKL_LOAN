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
import com.st1.itx.db.domain.TxToDoDetailReserve;
import com.st1.itx.db.repository.online.TxToDoDetailReserveRepository;
import com.st1.itx.db.repository.day.TxToDoDetailReserveRepositoryDay;
import com.st1.itx.db.repository.mon.TxToDoDetailReserveRepositoryMon;
import com.st1.itx.db.repository.hist.TxToDoDetailReserveRepositoryHist;
import com.st1.itx.db.service.TxToDoDetailReserveService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txToDoDetailReserveService")
@Repository
public class TxToDoDetailReserveServiceImpl extends ASpringJpaParm implements TxToDoDetailReserveService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxToDoDetailReserveRepository txToDoDetailReserveRepos;

  @Autowired
  private TxToDoDetailReserveRepositoryDay txToDoDetailReserveReposDay;

  @Autowired
  private TxToDoDetailReserveRepositoryMon txToDoDetailReserveReposMon;

  @Autowired
  private TxToDoDetailReserveRepositoryHist txToDoDetailReserveReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txToDoDetailReserveRepos);
    org.junit.Assert.assertNotNull(txToDoDetailReserveReposDay);
    org.junit.Assert.assertNotNull(txToDoDetailReserveReposMon);
    org.junit.Assert.assertNotNull(txToDoDetailReserveReposHist);
  }

  @Override
  public TxToDoDetailReserve findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<TxToDoDetailReserve> txToDoDetailReserve = null;
    if (dbName.equals(ContentName.onDay))
      txToDoDetailReserve = txToDoDetailReserveReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      txToDoDetailReserve = txToDoDetailReserveReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      txToDoDetailReserve = txToDoDetailReserveReposHist.findById(logNo);
    else 
      txToDoDetailReserve = txToDoDetailReserveRepos.findById(logNo);
    TxToDoDetailReserve obj = txToDoDetailReserve.isPresent() ? txToDoDetailReserve.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxToDoDetailReserve> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxToDoDetailReserve> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txToDoDetailReserveReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txToDoDetailReserveReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txToDoDetailReserveReposHist.findAll(pageable);
    else 
      slice = txToDoDetailReserveRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxToDoDetailReserve> detailStatusRange(String itemCode_0, int status_1, int status_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxToDoDetailReserve> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("detailStatusRange " + dbName + " : " + "itemCode_0 : " + itemCode_0 + " status_1 : " +  status_1 + " status_2 : " +  status_2);
    if (dbName.equals(ContentName.onDay))
      slice = txToDoDetailReserveReposDay.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(itemCode_0, status_1, status_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txToDoDetailReserveReposMon.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(itemCode_0, status_1, status_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txToDoDetailReserveReposHist.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(itemCode_0, status_1, status_2, pageable);
    else 
      slice = txToDoDetailReserveRepos.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(itemCode_0, status_1, status_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxToDoDetailReserve> DataDateRange(String itemCode_0, int status_1, int status_2, int dataDate_3, int dataDate_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxToDoDetailReserve> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("DataDateRange " + dbName + " : " + "itemCode_0 : " + itemCode_0 + " status_1 : " +  status_1 + " status_2 : " +  status_2 + " dataDate_3 : " +  dataDate_3 + " dataDate_4 : " +  dataDate_4);
    if (dbName.equals(ContentName.onDay))
      slice = txToDoDetailReserveReposDay.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, status_1, status_2, dataDate_3, dataDate_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txToDoDetailReserveReposMon.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, status_1, status_2, dataDate_3, dataDate_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txToDoDetailReserveReposHist.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, status_1, status_2, dataDate_3, dataDate_4, pageable);
    else 
      slice = txToDoDetailReserveRepos.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, status_1, status_2, dataDate_3, dataDate_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxToDoDetailReserve> findTxNoEq(String itemCode_0, int titaEntdy_1, String titaKinbr_2, String titaTlrNo_3, int titaTxtNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxToDoDetailReserve> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findTxNoEq " + dbName + " : " + "itemCode_0 : " + itemCode_0 + " titaEntdy_1 : " +  titaEntdy_1 + " titaKinbr_2 : " +  titaKinbr_2 + " titaTlrNo_3 : " +  titaTlrNo_3 + " titaTxtNo_4 : " +  titaTxtNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = txToDoDetailReserveReposDay.findAllByItemCodeIsAndTitaEntdyIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByItemCodeAscCustNoAscBormNoAscDtlValueAsc(itemCode_0, titaEntdy_1, titaKinbr_2, titaTlrNo_3, titaTxtNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txToDoDetailReserveReposMon.findAllByItemCodeIsAndTitaEntdyIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByItemCodeAscCustNoAscBormNoAscDtlValueAsc(itemCode_0, titaEntdy_1, titaKinbr_2, titaTlrNo_3, titaTxtNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txToDoDetailReserveReposHist.findAllByItemCodeIsAndTitaEntdyIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByItemCodeAscCustNoAscBormNoAscDtlValueAsc(itemCode_0, titaEntdy_1, titaKinbr_2, titaTlrNo_3, titaTxtNo_4, pageable);
    else 
      slice = txToDoDetailReserveRepos.findAllByItemCodeIsAndTitaEntdyIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByItemCodeAscCustNoAscBormNoAscDtlValueAsc(itemCode_0, titaEntdy_1, titaKinbr_2, titaTlrNo_3, titaTxtNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxToDoDetailReserve holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<TxToDoDetailReserve> txToDoDetailReserve = null;
    if (dbName.equals(ContentName.onDay))
      txToDoDetailReserve = txToDoDetailReserveReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      txToDoDetailReserve = txToDoDetailReserveReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      txToDoDetailReserve = txToDoDetailReserveReposHist.findByLogNo(logNo);
    else 
      txToDoDetailReserve = txToDoDetailReserveRepos.findByLogNo(logNo);
    return txToDoDetailReserve.isPresent() ? txToDoDetailReserve.get() : null;
  }

  @Override
  public TxToDoDetailReserve holdById(TxToDoDetailReserve txToDoDetailReserve, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txToDoDetailReserve.getLogNo());
    Optional<TxToDoDetailReserve> txToDoDetailReserveT = null;
    if (dbName.equals(ContentName.onDay))
      txToDoDetailReserveT = txToDoDetailReserveReposDay.findByLogNo(txToDoDetailReserve.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      txToDoDetailReserveT = txToDoDetailReserveReposMon.findByLogNo(txToDoDetailReserve.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      txToDoDetailReserveT = txToDoDetailReserveReposHist.findByLogNo(txToDoDetailReserve.getLogNo());
    else 
      txToDoDetailReserveT = txToDoDetailReserveRepos.findByLogNo(txToDoDetailReserve.getLogNo());
    return txToDoDetailReserveT.isPresent() ? txToDoDetailReserveT.get() : null;
  }

  @Override
  public TxToDoDetailReserve insert(TxToDoDetailReserve txToDoDetailReserve, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + txToDoDetailReserve.getLogNo());
    if (this.findById(txToDoDetailReserve.getLogNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txToDoDetailReserve.setCreateEmpNo(empNot);

    if(txToDoDetailReserve.getLastUpdateEmpNo() == null || txToDoDetailReserve.getLastUpdateEmpNo().isEmpty())
      txToDoDetailReserve.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txToDoDetailReserveReposDay.saveAndFlush(txToDoDetailReserve);	
    else if (dbName.equals(ContentName.onMon))
      return txToDoDetailReserveReposMon.saveAndFlush(txToDoDetailReserve);
    else if (dbName.equals(ContentName.onHist))
      return txToDoDetailReserveReposHist.saveAndFlush(txToDoDetailReserve);
    else 
    return txToDoDetailReserveRepos.saveAndFlush(txToDoDetailReserve);
  }

  @Override
  public TxToDoDetailReserve update(TxToDoDetailReserve txToDoDetailReserve, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + txToDoDetailReserve.getLogNo());
    if (!empNot.isEmpty())
      txToDoDetailReserve.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txToDoDetailReserveReposDay.saveAndFlush(txToDoDetailReserve);	
    else if (dbName.equals(ContentName.onMon))
      return txToDoDetailReserveReposMon.saveAndFlush(txToDoDetailReserve);
    else if (dbName.equals(ContentName.onHist))
      return txToDoDetailReserveReposHist.saveAndFlush(txToDoDetailReserve);
    else 
    return txToDoDetailReserveRepos.saveAndFlush(txToDoDetailReserve);
  }

  @Override
  public TxToDoDetailReserve update2(TxToDoDetailReserve txToDoDetailReserve, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + txToDoDetailReserve.getLogNo());
    if (!empNot.isEmpty())
      txToDoDetailReserve.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txToDoDetailReserveReposDay.saveAndFlush(txToDoDetailReserve);	
    else if (dbName.equals(ContentName.onMon))
      txToDoDetailReserveReposMon.saveAndFlush(txToDoDetailReserve);
    else if (dbName.equals(ContentName.onHist))
        txToDoDetailReserveReposHist.saveAndFlush(txToDoDetailReserve);
    else 
      txToDoDetailReserveRepos.saveAndFlush(txToDoDetailReserve);	
    return this.findById(txToDoDetailReserve.getLogNo());
  }

  @Override
  public void delete(TxToDoDetailReserve txToDoDetailReserve, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txToDoDetailReserve.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      txToDoDetailReserveReposDay.delete(txToDoDetailReserve);	
      txToDoDetailReserveReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txToDoDetailReserveReposMon.delete(txToDoDetailReserve);	
      txToDoDetailReserveReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txToDoDetailReserveReposHist.delete(txToDoDetailReserve);
      txToDoDetailReserveReposHist.flush();
    }
    else {
      txToDoDetailReserveRepos.delete(txToDoDetailReserve);
      txToDoDetailReserveRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxToDoDetailReserve> txToDoDetailReserve, TitaVo... titaVo) throws DBException {
    if (txToDoDetailReserve == null || txToDoDetailReserve.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (TxToDoDetailReserve t : txToDoDetailReserve){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txToDoDetailReserve = txToDoDetailReserveReposDay.saveAll(txToDoDetailReserve);	
      txToDoDetailReserveReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txToDoDetailReserve = txToDoDetailReserveReposMon.saveAll(txToDoDetailReserve);	
      txToDoDetailReserveReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txToDoDetailReserve = txToDoDetailReserveReposHist.saveAll(txToDoDetailReserve);
      txToDoDetailReserveReposHist.flush();
    }
    else {
      txToDoDetailReserve = txToDoDetailReserveRepos.saveAll(txToDoDetailReserve);
      txToDoDetailReserveRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxToDoDetailReserve> txToDoDetailReserve, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (txToDoDetailReserve == null || txToDoDetailReserve.size() == 0)
      throw new DBException(6);

    for (TxToDoDetailReserve t : txToDoDetailReserve) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txToDoDetailReserve = txToDoDetailReserveReposDay.saveAll(txToDoDetailReserve);	
      txToDoDetailReserveReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txToDoDetailReserve = txToDoDetailReserveReposMon.saveAll(txToDoDetailReserve);	
      txToDoDetailReserveReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txToDoDetailReserve = txToDoDetailReserveReposHist.saveAll(txToDoDetailReserve);
      txToDoDetailReserveReposHist.flush();
    }
    else {
      txToDoDetailReserve = txToDoDetailReserveRepos.saveAll(txToDoDetailReserve);
      txToDoDetailReserveRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxToDoDetailReserve> txToDoDetailReserve, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txToDoDetailReserve == null || txToDoDetailReserve.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txToDoDetailReserveReposDay.deleteAll(txToDoDetailReserve);	
      txToDoDetailReserveReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txToDoDetailReserveReposMon.deleteAll(txToDoDetailReserve);	
      txToDoDetailReserveReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txToDoDetailReserveReposHist.deleteAll(txToDoDetailReserve);
      txToDoDetailReserveReposHist.flush();
    }
    else {
      txToDoDetailReserveRepos.deleteAll(txToDoDetailReserve);
      txToDoDetailReserveRepos.flush();
    }
  }

}
