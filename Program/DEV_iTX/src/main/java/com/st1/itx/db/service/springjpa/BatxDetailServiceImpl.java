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
import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;
import com.st1.itx.db.repository.online.BatxDetailRepository;
import com.st1.itx.db.repository.day.BatxDetailRepositoryDay;
import com.st1.itx.db.repository.mon.BatxDetailRepositoryMon;
import com.st1.itx.db.repository.hist.BatxDetailRepositoryHist;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("batxDetailService")
@Repository
public class BatxDetailServiceImpl extends ASpringJpaParm implements BatxDetailService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private BatxDetailRepository batxDetailRepos;

  @Autowired
  private BatxDetailRepositoryDay batxDetailReposDay;

  @Autowired
  private BatxDetailRepositoryMon batxDetailReposMon;

  @Autowired
  private BatxDetailRepositoryHist batxDetailReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(batxDetailRepos);
    org.junit.Assert.assertNotNull(batxDetailReposDay);
    org.junit.Assert.assertNotNull(batxDetailReposMon);
    org.junit.Assert.assertNotNull(batxDetailReposHist);
  }

  @Override
  public BatxDetail findById(BatxDetailId batxDetailId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + batxDetailId);
    Optional<BatxDetail> batxDetail = null;
    if (dbName.equals(ContentName.onDay))
      batxDetail = batxDetailReposDay.findById(batxDetailId);
    else if (dbName.equals(ContentName.onMon))
      batxDetail = batxDetailReposMon.findById(batxDetailId);
    else if (dbName.equals(ContentName.onHist))
      batxDetail = batxDetailReposHist.findById(batxDetailId);
    else 
      batxDetail = batxDetailRepos.findById(batxDetailId);
    BatxDetail obj = batxDetail.isPresent() ? batxDetail.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<BatxDetail> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcDate", "BatchNo", "DetailSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcDate", "BatchNo", "DetailSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAll(pageable);
    else 
      slice = batxDetailRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL492AAEq(int custNo_0, int facmNo_1, int repayCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL492AAEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " repayCode_2 : " +  repayCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByCustNoIsAndFacmNoIsAndRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByCustNoIsAndFacmNoIsAndRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByCustNoIsAndFacmNoIsAndRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayCode_2, pageable);
    else 
      slice = batxDetailRepos.findAllByCustNoIsAndFacmNoIsAndRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL492ABEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL492ABEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, pageable);
    else 
      slice = batxDetailRepos.findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL492ACEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL492ACEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByCustNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByCustNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByCustNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, pageable);
    else 
      slice = batxDetailRepos.findAllByCustNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL492ADEq(int repayCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL492ADEq " + dbName + " : " + "repayCode_0 : " + repayCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayCode_0, pageable);
    else 
      slice = batxDetailRepos.findAllByRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL492AEEq(int custNo_0, int repayCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL492AEEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " repayCode_1 : " +  repayCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByCustNoIsAndRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByCustNoIsAndRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByCustNoIsAndRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayCode_1, pageable);
    else 
      slice = batxDetailRepos.findAllByCustNoIsAndRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayCode_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL492AFEq(int custNo_0, int facmNo_1, int repayCode_2, int repayType_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL492AFEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " repayCode_2 : " +  repayCode_2 + " repayType_3 : " +  repayType_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByCustNoIsAndFacmNoIsAndRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayCode_2, repayType_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByCustNoIsAndFacmNoIsAndRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayCode_2, repayType_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByCustNoIsAndFacmNoIsAndRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayCode_2, repayType_3, pageable);
    else 
      slice = batxDetailRepos.findAllByCustNoIsAndFacmNoIsAndRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayCode_2, repayType_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL492AGEq(int custNo_0, int facmNo_1, int repayType_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL492AGEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " repayType_2 : " +  repayType_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByCustNoIsAndFacmNoIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayType_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByCustNoIsAndFacmNoIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayType_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByCustNoIsAndFacmNoIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayType_2, pageable);
    else 
      slice = batxDetailRepos.findAllByCustNoIsAndFacmNoIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, facmNo_1, repayType_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL492AHEq(int custNo_0, int repayType_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL492AHEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " repayType_1 : " +  repayType_1);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByCustNoIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayType_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByCustNoIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayType_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByCustNoIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayType_1, pageable);
    else 
      slice = batxDetailRepos.findAllByCustNoIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayType_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL492AIEq(int repayCode_0, int repayType_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL492AIEq " + dbName + " : " + "repayCode_0 : " + repayCode_0 + " repayType_1 : " +  repayType_1);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayCode_0, repayType_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayCode_0, repayType_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayCode_0, repayType_1, pageable);
    else 
      slice = batxDetailRepos.findAllByRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayCode_0, repayType_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL492AJEq(int custNo_0, int repayCode_1, int repayType_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL492AJEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " repayCode_1 : " +  repayCode_1 + " repayType_2 : " +  repayType_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByCustNoIsAndRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayCode_1, repayType_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByCustNoIsAndRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayCode_1, repayType_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByCustNoIsAndRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayCode_1, repayType_2, pageable);
    else 
      slice = batxDetailRepos.findAllByCustNoIsAndRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(custNo_0, repayCode_1, repayType_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL492AKEq(int repayType_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL492AKEq " + dbName + " : " + "repayType_0 : " + repayType_0);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayType_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayType_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayType_0, pageable);
    else 
      slice = batxDetailRepos.findAllByRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(repayType_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL4002AEq(int acDate_0, String batchNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4002AEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateIsAndBatchNoIsOrderByCustNoAscFacmNoAscRepayCodeAscDetailSeqAsc(acDate_0, batchNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateIsAndBatchNoIsOrderByCustNoAscFacmNoAscRepayCodeAscDetailSeqAsc(acDate_0, batchNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateIsAndBatchNoIsOrderByCustNoAscFacmNoAscRepayCodeAscDetailSeqAsc(acDate_0, batchNo_1, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateIsAndBatchNoIsOrderByCustNoAscFacmNoAscRepayCodeAscDetailSeqAsc(acDate_0, batchNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL4002BEq(int acDate_0, String batchNo_1, String titaTlrNo_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4002BEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1 + " titaTlrNo_2 : " +  titaTlrNo_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateIsAndBatchNoIsAndTitaTlrNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(acDate_0, batchNo_1, titaTlrNo_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateIsAndBatchNoIsAndTitaTlrNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(acDate_0, batchNo_1, titaTlrNo_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateIsAndBatchNoIsAndTitaTlrNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(acDate_0, batchNo_1, titaTlrNo_2, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateIsAndBatchNoIsAndTitaTlrNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(acDate_0, batchNo_1, titaTlrNo_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL4925AEq(int acDate_0, int acDate_1, int custNo_2, int custNo_3, int repayCode_4, int repayCode_5, List<String> procStsCode_6, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4925AEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " custNo_2 : " +  custNo_2 + " custNo_3 : " +  custNo_3 + " repayCode_4 : " +  repayCode_4 + " repayCode_5 : " +  repayCode_5 + " procStsCode_6 : " +  procStsCode_6);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndRepayCodeGreaterThanEqualAndRepayCodeLessThanEqualAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeAscDetailSeqAsc(acDate_0, acDate_1, custNo_2, custNo_3, repayCode_4, repayCode_5, procStsCode_6, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndRepayCodeGreaterThanEqualAndRepayCodeLessThanEqualAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeAscDetailSeqAsc(acDate_0, acDate_1, custNo_2, custNo_3, repayCode_4, repayCode_5, procStsCode_6, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndRepayCodeGreaterThanEqualAndRepayCodeLessThanEqualAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeAscDetailSeqAsc(acDate_0, acDate_1, custNo_2, custNo_3, repayCode_4, repayCode_5, procStsCode_6, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndRepayCodeGreaterThanEqualAndRepayCodeLessThanEqualAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeAscDetailSeqAsc(acDate_0, acDate_1, custNo_2, custNo_3, repayCode_4, repayCode_5, procStsCode_6, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL4200AEq(int acDate_0, String batchNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4200AEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateIsAndBatchNoIsOrderByMediaDateAscMediaKindAscMediaSeqAsc(acDate_0, batchNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateIsAndBatchNoIsOrderByMediaDateAscMediaKindAscMediaSeqAsc(acDate_0, batchNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateIsAndBatchNoIsOrderByMediaDateAscMediaKindAscMediaSeqAsc(acDate_0, batchNo_1, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateIsAndBatchNoIsOrderByMediaDateAscMediaKindAscMediaSeqAsc(acDate_0, batchNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BatxDetail findL4200BFirst(int acDate_0, String fileName_1, int custNo_2, BigDecimal repayAmt_3, List<String> procStsCode_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findL4200BFirst " + dbName + " : " + "acDate_0 : " + acDate_0 + " fileName_1 : " +  fileName_1 + " custNo_2 : " +  custNo_2 + " repayAmt_3 : " +  repayAmt_3 + " procStsCode_4 : " +  procStsCode_4);
    Optional<BatxDetail> batxDetailT = null;
    if (dbName.equals(ContentName.onDay))
      batxDetailT = batxDetailReposDay.findTopByAcDateIsAndFileNameIsAndCustNoIsAndRepayAmtIsAndProcStsCodeInOrderByBatchNoDescDetailSeqDesc(acDate_0, fileName_1, custNo_2, repayAmt_3, procStsCode_4);
    else if (dbName.equals(ContentName.onMon))
      batxDetailT = batxDetailReposMon.findTopByAcDateIsAndFileNameIsAndCustNoIsAndRepayAmtIsAndProcStsCodeInOrderByBatchNoDescDetailSeqDesc(acDate_0, fileName_1, custNo_2, repayAmt_3, procStsCode_4);
    else if (dbName.equals(ContentName.onHist))
      batxDetailT = batxDetailReposHist.findTopByAcDateIsAndFileNameIsAndCustNoIsAndRepayAmtIsAndProcStsCodeInOrderByBatchNoDescDetailSeqDesc(acDate_0, fileName_1, custNo_2, repayAmt_3, procStsCode_4);
    else 
      batxDetailT = batxDetailRepos.findTopByAcDateIsAndFileNameIsAndCustNoIsAndRepayAmtIsAndProcStsCodeInOrderByBatchNoDescDetailSeqDesc(acDate_0, fileName_1, custNo_2, repayAmt_3, procStsCode_4);

    return batxDetailT.isPresent() ? batxDetailT.get() : null;
  }

  @Override
  public Slice<BatxDetail> findL4930CAEq(int acDate_0, String batchNo_1, int custNo_2, List<String> procStsCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4930CAEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1 + " custNo_2 : " +  custNo_2 + " procStsCode_3 : " +  procStsCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateIsAndBatchNoIsAndCustNoIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, custNo_2, procStsCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateIsAndBatchNoIsAndCustNoIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, custNo_2, procStsCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateIsAndBatchNoIsAndCustNoIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, custNo_2, procStsCode_3, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateIsAndBatchNoIsAndCustNoIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, custNo_2, procStsCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL4930CHEq(int acDate_0, String batchNo_1, int custNo_2, List<String> procStsCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4930CHEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1 + " custNo_2 : " +  custNo_2 + " procStsCode_3 : " +  procStsCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateIsAndBatchNoIsAndCustNoIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, custNo_2, procStsCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateIsAndBatchNoIsAndCustNoIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, custNo_2, procStsCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateIsAndBatchNoIsAndCustNoIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, custNo_2, procStsCode_3, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateIsAndBatchNoIsAndCustNoIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, custNo_2, procStsCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL4930BAEq(int acDate_0, String batchNo_1, List<String> procStsCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4930BAEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1 + " procStsCode_2 : " +  procStsCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateIsAndBatchNoIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, procStsCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateIsAndBatchNoIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, procStsCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateIsAndBatchNoIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, procStsCode_2, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateIsAndBatchNoIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, procStsCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL4930BHEq(int acDate_0, String batchNo_1, List<String> procStsCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4930BHEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1 + " procStsCode_2 : " +  procStsCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateIsAndBatchNoIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, procStsCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateIsAndBatchNoIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, procStsCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateIsAndBatchNoIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, procStsCode_2, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateIsAndBatchNoIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, procStsCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL4930RAEq(int acDate_0, String batchNo_1, String reconCode_2, List<String> procStsCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4930RAEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1 + " reconCode_2 : " +  reconCode_2 + " procStsCode_3 : " +  procStsCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateIsAndBatchNoIsAndReconCodeIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, reconCode_2, procStsCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateIsAndBatchNoIsAndReconCodeIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, reconCode_2, procStsCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateIsAndBatchNoIsAndReconCodeIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, reconCode_2, procStsCode_3, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateIsAndBatchNoIsAndReconCodeIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(acDate_0, batchNo_1, reconCode_2, procStsCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL4930RHEq(int acDate_0, String batchNo_1, String reconCode_2, List<String> procStsCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4930RHEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1 + " reconCode_2 : " +  reconCode_2 + " procStsCode_3 : " +  procStsCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateIsAndBatchNoIsAndReconCodeIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, reconCode_2, procStsCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateIsAndBatchNoIsAndReconCodeIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, reconCode_2, procStsCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateIsAndBatchNoIsAndReconCodeIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, reconCode_2, procStsCode_3, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateIsAndBatchNoIsAndReconCodeIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(acDate_0, batchNo_1, reconCode_2, procStsCode_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> findL4454AEq(int acDate_0, int acDate_1, int repayCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4454AEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " acDate_1 : " +  acDate_1 + " repayCode_2 : " +  repayCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIsOrderByBatchNoDescDetailSeqAsc(acDate_0, acDate_1, repayCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIsOrderByBatchNoDescDetailSeqAsc(acDate_0, acDate_1, repayCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIsOrderByBatchNoDescDetailSeqAsc(acDate_0, acDate_1, repayCode_2, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIsOrderByBatchNoDescDetailSeqAsc(acDate_0, acDate_1, repayCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BatxDetail> fileCheck(int acDate_0, String fileName_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BatxDetail> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("fileCheck " + dbName + " : " + "acDate_0 : " + acDate_0 + " fileName_1 : " +  fileName_1);
    if (dbName.equals(ContentName.onDay))
      slice = batxDetailReposDay.findAllByAcDateIsAndFileNameIsOrderByBatchNoDesc(acDate_0, fileName_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = batxDetailReposMon.findAllByAcDateIsAndFileNameIsOrderByBatchNoDesc(acDate_0, fileName_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = batxDetailReposHist.findAllByAcDateIsAndFileNameIsOrderByBatchNoDesc(acDate_0, fileName_1, pageable);
    else 
      slice = batxDetailRepos.findAllByAcDateIsAndFileNameIsOrderByBatchNoDesc(acDate_0, fileName_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BatxDetail holdById(BatxDetailId batxDetailId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + batxDetailId);
    Optional<BatxDetail> batxDetail = null;
    if (dbName.equals(ContentName.onDay))
      batxDetail = batxDetailReposDay.findByBatxDetailId(batxDetailId);
    else if (dbName.equals(ContentName.onMon))
      batxDetail = batxDetailReposMon.findByBatxDetailId(batxDetailId);
    else if (dbName.equals(ContentName.onHist))
      batxDetail = batxDetailReposHist.findByBatxDetailId(batxDetailId);
    else 
      batxDetail = batxDetailRepos.findByBatxDetailId(batxDetailId);
    return batxDetail.isPresent() ? batxDetail.get() : null;
  }

  @Override
  public BatxDetail holdById(BatxDetail batxDetail, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + batxDetail.getBatxDetailId());
    Optional<BatxDetail> batxDetailT = null;
    if (dbName.equals(ContentName.onDay))
      batxDetailT = batxDetailReposDay.findByBatxDetailId(batxDetail.getBatxDetailId());
    else if (dbName.equals(ContentName.onMon))
      batxDetailT = batxDetailReposMon.findByBatxDetailId(batxDetail.getBatxDetailId());
    else if (dbName.equals(ContentName.onHist))
      batxDetailT = batxDetailReposHist.findByBatxDetailId(batxDetail.getBatxDetailId());
    else 
      batxDetailT = batxDetailRepos.findByBatxDetailId(batxDetail.getBatxDetailId());
    return batxDetailT.isPresent() ? batxDetailT.get() : null;
  }

  @Override
  public BatxDetail insert(BatxDetail batxDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + batxDetail.getBatxDetailId());
    if (this.findById(batxDetail.getBatxDetailId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      batxDetail.setCreateEmpNo(empNot);

    if(batxDetail.getLastUpdateEmpNo() == null || batxDetail.getLastUpdateEmpNo().isEmpty())
      batxDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return batxDetailReposDay.saveAndFlush(batxDetail);	
    else if (dbName.equals(ContentName.onMon))
      return batxDetailReposMon.saveAndFlush(batxDetail);
    else if (dbName.equals(ContentName.onHist))
      return batxDetailReposHist.saveAndFlush(batxDetail);
    else 
    return batxDetailRepos.saveAndFlush(batxDetail);
  }

  @Override
  public BatxDetail update(BatxDetail batxDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + batxDetail.getBatxDetailId());
    if (!empNot.isEmpty())
      batxDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return batxDetailReposDay.saveAndFlush(batxDetail);	
    else if (dbName.equals(ContentName.onMon))
      return batxDetailReposMon.saveAndFlush(batxDetail);
    else if (dbName.equals(ContentName.onHist))
      return batxDetailReposHist.saveAndFlush(batxDetail);
    else 
    return batxDetailRepos.saveAndFlush(batxDetail);
  }

  @Override
  public BatxDetail update2(BatxDetail batxDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + batxDetail.getBatxDetailId());
    if (!empNot.isEmpty())
      batxDetail.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      batxDetailReposDay.saveAndFlush(batxDetail);	
    else if (dbName.equals(ContentName.onMon))
      batxDetailReposMon.saveAndFlush(batxDetail);
    else if (dbName.equals(ContentName.onHist))
        batxDetailReposHist.saveAndFlush(batxDetail);
    else 
      batxDetailRepos.saveAndFlush(batxDetail);	
    return this.findById(batxDetail.getBatxDetailId());
  }

  @Override
  public void delete(BatxDetail batxDetail, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + batxDetail.getBatxDetailId());
    if (dbName.equals(ContentName.onDay)) {
      batxDetailReposDay.delete(batxDetail);	
      batxDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxDetailReposMon.delete(batxDetail);	
      batxDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxDetailReposHist.delete(batxDetail);
      batxDetailReposHist.flush();
    }
    else {
      batxDetailRepos.delete(batxDetail);
      batxDetailRepos.flush();
    }
   }

  @Override
  public void insertAll(List<BatxDetail> batxDetail, TitaVo... titaVo) throws DBException {
    if (batxDetail == null || batxDetail.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (BatxDetail t : batxDetail){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      batxDetail = batxDetailReposDay.saveAll(batxDetail);	
      batxDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxDetail = batxDetailReposMon.saveAll(batxDetail);	
      batxDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxDetail = batxDetailReposHist.saveAll(batxDetail);
      batxDetailReposHist.flush();
    }
    else {
      batxDetail = batxDetailRepos.saveAll(batxDetail);
      batxDetailRepos.flush();
    }
    }

  @Override
  public void updateAll(List<BatxDetail> batxDetail, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (batxDetail == null || batxDetail.size() == 0)
      throw new DBException(6);

    for (BatxDetail t : batxDetail) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      batxDetail = batxDetailReposDay.saveAll(batxDetail);	
      batxDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxDetail = batxDetailReposMon.saveAll(batxDetail);	
      batxDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxDetail = batxDetailReposHist.saveAll(batxDetail);
      batxDetailReposHist.flush();
    }
    else {
      batxDetail = batxDetailRepos.saveAll(batxDetail);
      batxDetailRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<BatxDetail> batxDetail, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (batxDetail == null || batxDetail.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      batxDetailReposDay.deleteAll(batxDetail);	
      batxDetailReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      batxDetailReposMon.deleteAll(batxDetail);	
      batxDetailReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      batxDetailReposHist.deleteAll(batxDetail);
      batxDetailReposHist.flush();
    }
    else {
      batxDetailRepos.deleteAll(batxDetail);
      batxDetailRepos.flush();
    }
  }

}
