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
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.repository.online.TxToDoDetailRepository;
import com.st1.itx.db.repository.day.TxToDoDetailRepositoryDay;
import com.st1.itx.db.repository.mon.TxToDoDetailRepositoryMon;
import com.st1.itx.db.repository.hist.TxToDoDetailRepositoryHist;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("txToDoDetailService")
@Repository
public class TxToDoDetailServiceImpl extends ASpringJpaParm implements TxToDoDetailService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private TxToDoDetailRepository txToDoDetailRepos;

  @Autowired
  private TxToDoDetailRepositoryDay txToDoDetailReposDay;

  @Autowired
  private TxToDoDetailRepositoryMon txToDoDetailReposMon;

  @Autowired
  private TxToDoDetailRepositoryHist txToDoDetailReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(txToDoDetailRepos);
    org.junit.Assert.assertNotNull(txToDoDetailReposDay);
    org.junit.Assert.assertNotNull(txToDoDetailReposMon);
    org.junit.Assert.assertNotNull(txToDoDetailReposHist);
  }

  @Override
  public TxToDoDetail findById(TxToDoDetailId txToDoDetailId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + txToDoDetailId);
    Optional<TxToDoDetail> txToDoDetail = null;
    if (dbName.equals(ContentName.onDay))
      txToDoDetail = txToDoDetailReposDay.findById(txToDoDetailId);
    else if (dbName.equals(ContentName.onMon))
      txToDoDetail = txToDoDetailReposMon.findById(txToDoDetailId);
    else if (dbName.equals(ContentName.onHist))
      txToDoDetail = txToDoDetailReposHist.findById(txToDoDetailId);
    else 
      txToDoDetail = txToDoDetailRepos.findById(txToDoDetailId);
    TxToDoDetail obj = txToDoDetail.isPresent() ? txToDoDetail.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<TxToDoDetail> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxToDoDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ItemCode", "CustNo", "FacmNo", "BormNo", "DtlValue"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ItemCode", "CustNo", "FacmNo", "BormNo", "DtlValue"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = txToDoDetailReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txToDoDetailReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txToDoDetailReposHist.findAll(pageable);
    else 
      slice = txToDoDetailRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxToDoDetail> detailStatusRange(String itemCode_0, int status_1, int status_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxToDoDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("detailStatusRange " + dbName + " : " + "itemCode_0 : " + itemCode_0 + " status_1 : " +  status_1 + " status_2 : " +  status_2);
    if (dbName.equals(ContentName.onDay))
      slice = txToDoDetailReposDay.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(itemCode_0, status_1, status_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txToDoDetailReposMon.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(itemCode_0, status_1, status_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txToDoDetailReposHist.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(itemCode_0, status_1, status_2, pageable);
    else 
      slice = txToDoDetailRepos.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAscDtlValueAsc(itemCode_0, status_1, status_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxToDoDetail> DtlValueRange(String itemCode_0, String dtlValue_1, String dtlValue_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxToDoDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("DtlValueRange " + dbName + " : " + "itemCode_0 : " + itemCode_0 + " dtlValue_1 : " +  dtlValue_1 + " dtlValue_2 : " +  dtlValue_2);
    if (dbName.equals(ContentName.onDay))
      slice = txToDoDetailReposDay.findAllByItemCodeIsAndDtlValueGreaterThanEqualAndDtlValueLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, dtlValue_1, dtlValue_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txToDoDetailReposMon.findAllByItemCodeIsAndDtlValueGreaterThanEqualAndDtlValueLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, dtlValue_1, dtlValue_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txToDoDetailReposHist.findAllByItemCodeIsAndDtlValueGreaterThanEqualAndDtlValueLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, dtlValue_1, dtlValue_2, pageable);
    else 
      slice = txToDoDetailRepos.findAllByItemCodeIsAndDtlValueGreaterThanEqualAndDtlValueLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, dtlValue_1, dtlValue_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxToDoDetail> itemCodeRange(String itemCode_0, String dtlValue_1, int status_2, int status_3, int dataDate_4, int dataDate_5, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxToDoDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("itemCodeRange " + dbName + " : " + "itemCode_0 : " + itemCode_0 + " dtlValue_1 : " +  dtlValue_1 + " status_2 : " +  status_2 + " status_3 : " +  status_3 + " dataDate_4 : " +  dataDate_4 + " dataDate_5 : " +  dataDate_5);
    if (dbName.equals(ContentName.onDay))
      slice = txToDoDetailReposDay.findAllByItemCodeIsAndDtlValueIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, dtlValue_1, status_2, status_3, dataDate_4, dataDate_5, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txToDoDetailReposMon.findAllByItemCodeIsAndDtlValueIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, dtlValue_1, status_2, status_3, dataDate_4, dataDate_5, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txToDoDetailReposHist.findAllByItemCodeIsAndDtlValueIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, dtlValue_1, status_2, status_3, dataDate_4, dataDate_5, pageable);
    else 
      slice = txToDoDetailRepos.findAllByItemCodeIsAndDtlValueIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, dtlValue_1, status_2, status_3, dataDate_4, dataDate_5, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxToDoDetail> DataDateRange(String itemCode_0, int status_1, int status_2, int dataDate_3, int dataDate_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxToDoDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("DataDateRange " + dbName + " : " + "itemCode_0 : " + itemCode_0 + " status_1 : " +  status_1 + " status_2 : " +  status_2 + " dataDate_3 : " +  dataDate_3 + " dataDate_4 : " +  dataDate_4);
    if (dbName.equals(ContentName.onDay))
      slice = txToDoDetailReposDay.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, status_1, status_2, dataDate_3, dataDate_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txToDoDetailReposMon.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, status_1, status_2, dataDate_3, dataDate_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txToDoDetailReposHist.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, status_1, status_2, dataDate_3, dataDate_4, pageable);
    else 
      slice = txToDoDetailRepos.findAllByItemCodeIsAndStatusGreaterThanEqualAndStatusLessThanEqualAndDataDateGreaterThanEqualAndDataDateLessThanEqualOrderByStatusAscCustNoAscFacmNoAscBormNoAsc(itemCode_0, status_1, status_2, dataDate_3, dataDate_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<TxToDoDetail> findTxNoEq(String itemCode_0, int titaEntdy_1, String titaKinbr_2, String titaTlrNo_3, int titaTxtNo_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<TxToDoDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findTxNoEq " + dbName + " : " + "itemCode_0 : " + itemCode_0 + " titaEntdy_1 : " +  titaEntdy_1 + " titaKinbr_2 : " +  titaKinbr_2 + " titaTlrNo_3 : " +  titaTlrNo_3 + " titaTxtNo_4 : " +  titaTxtNo_4);
    if (dbName.equals(ContentName.onDay))
      slice = txToDoDetailReposDay.findAllByItemCodeIsAndTitaEntdyIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByItemCodeAscCustNoAscBormNoAscDtlValueAsc(itemCode_0, titaEntdy_1, titaKinbr_2, titaTlrNo_3, titaTxtNo_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = txToDoDetailReposMon.findAllByItemCodeIsAndTitaEntdyIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByItemCodeAscCustNoAscBormNoAscDtlValueAsc(itemCode_0, titaEntdy_1, titaKinbr_2, titaTlrNo_3, titaTxtNo_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = txToDoDetailReposHist.findAllByItemCodeIsAndTitaEntdyIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByItemCodeAscCustNoAscBormNoAscDtlValueAsc(itemCode_0, titaEntdy_1, titaKinbr_2, titaTlrNo_3, titaTxtNo_4, pageable);
    else 
      slice = txToDoDetailRepos.findAllByItemCodeIsAndTitaEntdyIsAndTitaKinbrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByItemCodeAscCustNoAscBormNoAscDtlValueAsc(itemCode_0, titaEntdy_1, titaKinbr_2, titaTlrNo_3, titaTxtNo_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public TxToDoDetail holdById(TxToDoDetailId txToDoDetailId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txToDoDetailId);
    Optional<TxToDoDetail> txToDoDetail = null;
    if (dbName.equals(ContentName.onDay))
      txToDoDetail = txToDoDetailReposDay.findByTxToDoDetailId(txToDoDetailId);
    else if (dbName.equals(ContentName.onMon))
      txToDoDetail = txToDoDetailReposMon.findByTxToDoDetailId(txToDoDetailId);
    else if (dbName.equals(ContentName.onHist))
      txToDoDetail = txToDoDetailReposHist.findByTxToDoDetailId(txToDoDetailId);
    else 
      txToDoDetail = txToDoDetailRepos.findByTxToDoDetailId(txToDoDetailId);
    return txToDoDetail.isPresent() ? txToDoDetail.get() : null;
  }

  @Override
  public TxToDoDetail holdById(TxToDoDetail txToDoDetail, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + txToDoDetail.getTxToDoDetailId());
    Optional<TxToDoDetail> txToDoDetailT = null;
    if (dbName.equals(ContentName.onDay))
      txToDoDetailT = txToDoDetailReposDay.findByTxToDoDetailId(txToDoDetail.getTxToDoDetailId());
    else if (dbName.equals(ContentName.onMon))
      txToDoDetailT = txToDoDetailReposMon.findByTxToDoDetailId(txToDoDetail.getTxToDoDetailId());
    else if (dbName.equals(ContentName.onHist))
      txToDoDetailT = txToDoDetailReposHist.findByTxToDoDetailId(txToDoDetail.getTxToDoDetailId());
    else 
      txToDoDetailT = txToDoDetailRepos.findByTxToDoDetailId(txToDoDetail.getTxToDoDetailId());
    return txToDoDetailT.isPresent() ? txToDoDetailT.get() : null;
  }

  @Override
  public TxToDoDetail insert(TxToDoDetail txToDoDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + txToDoDetail.getTxToDoDetailId());
    if (this.findById(txToDoDetail.getTxToDoDetailId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      txToDoDetail.setCreateEmpNo(empNot);

    if(txToDoDetail.getLastUpdateEmpNo() == null || txToDoDetail.getLastUpdateEmpNo().isEmpty())
      txToDoDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txToDoDetailReposDay.saveAndFlush(txToDoDetail);	
    else if (dbName.equals(ContentName.onMon))
      return txToDoDetailReposMon.saveAndFlush(txToDoDetail);
    else if (dbName.equals(ContentName.onHist))
      return txToDoDetailReposHist.saveAndFlush(txToDoDetail);
    else 
    return txToDoDetailRepos.saveAndFlush(txToDoDetail);
  }

  @Override
  public TxToDoDetail update(TxToDoDetail txToDoDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txToDoDetail.getTxToDoDetailId());
    if (!empNot.isEmpty())
      txToDoDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return txToDoDetailReposDay.saveAndFlush(txToDoDetail);	
    else if (dbName.equals(ContentName.onMon))
      return txToDoDetailReposMon.saveAndFlush(txToDoDetail);
    else if (dbName.equals(ContentName.onHist))
      return txToDoDetailReposHist.saveAndFlush(txToDoDetail);
    else 
    return txToDoDetailRepos.saveAndFlush(txToDoDetail);
  }

  @Override
  public TxToDoDetail update2(TxToDoDetail txToDoDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + txToDoDetail.getTxToDoDetailId());
    if (!empNot.isEmpty())
      txToDoDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      txToDoDetailReposDay.saveAndFlush(txToDoDetail);	
    else if (dbName.equals(ContentName.onMon))
      txToDoDetailReposMon.saveAndFlush(txToDoDetail);
    else if (dbName.equals(ContentName.onHist))
        txToDoDetailReposHist.saveAndFlush(txToDoDetail);
    else 
      txToDoDetailRepos.saveAndFlush(txToDoDetail);	
    return this.findById(txToDoDetail.getTxToDoDetailId());
  }

  @Override
  public void delete(TxToDoDetail txToDoDetail, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + txToDoDetail.getTxToDoDetailId());
    if (dbName.equals(ContentName.onDay)) {
      txToDoDetailReposDay.delete(txToDoDetail);	
      txToDoDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txToDoDetailReposMon.delete(txToDoDetail);	
      txToDoDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txToDoDetailReposHist.delete(txToDoDetail);
      txToDoDetailReposHist.flush();
    }
    else {
      txToDoDetailRepos.delete(txToDoDetail);
      txToDoDetailRepos.flush();
    }
   }

  @Override
  public void insertAll(List<TxToDoDetail> txToDoDetail, TitaVo... titaVo) throws DBException {
    if (txToDoDetail == null || txToDoDetail.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (TxToDoDetail t : txToDoDetail){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      txToDoDetail = txToDoDetailReposDay.saveAll(txToDoDetail);	
      txToDoDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txToDoDetail = txToDoDetailReposMon.saveAll(txToDoDetail);	
      txToDoDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txToDoDetail = txToDoDetailReposHist.saveAll(txToDoDetail);
      txToDoDetailReposHist.flush();
    }
    else {
      txToDoDetail = txToDoDetailRepos.saveAll(txToDoDetail);
      txToDoDetailRepos.flush();
    }
    }

  @Override
  public void updateAll(List<TxToDoDetail> txToDoDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (txToDoDetail == null || txToDoDetail.size() == 0)
      throw new DBException(6);

    for (TxToDoDetail t : txToDoDetail) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      txToDoDetail = txToDoDetailReposDay.saveAll(txToDoDetail);	
      txToDoDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txToDoDetail = txToDoDetailReposMon.saveAll(txToDoDetail);	
      txToDoDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txToDoDetail = txToDoDetailReposHist.saveAll(txToDoDetail);
      txToDoDetailReposHist.flush();
    }
    else {
      txToDoDetail = txToDoDetailRepos.saveAll(txToDoDetail);
      txToDoDetailRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<TxToDoDetail> txToDoDetail, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (txToDoDetail == null || txToDoDetail.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      txToDoDetailReposDay.deleteAll(txToDoDetail);	
      txToDoDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      txToDoDetailReposMon.deleteAll(txToDoDetail);	
      txToDoDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      txToDoDetailReposHist.deleteAll(txToDoDetail);
      txToDoDetailReposHist.flush();
    }
    else {
      txToDoDetailRepos.deleteAll(txToDoDetail);
      txToDoDetailRepos.flush();
    }
  }

}
