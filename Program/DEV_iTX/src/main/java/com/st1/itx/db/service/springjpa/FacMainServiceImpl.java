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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.repository.online.FacMainRepository;
import com.st1.itx.db.repository.day.FacMainRepositoryDay;
import com.st1.itx.db.repository.mon.FacMainRepositoryMon;
import com.st1.itx.db.repository.hist.FacMainRepositoryHist;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("facMainService")
@Repository
public class FacMainServiceImpl extends ASpringJpaParm implements FacMainService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private FacMainRepository facMainRepos;

  @Autowired
  private FacMainRepositoryDay facMainReposDay;

  @Autowired
  private FacMainRepositoryMon facMainReposMon;

  @Autowired
  private FacMainRepositoryHist facMainReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(facMainRepos);
    org.junit.Assert.assertNotNull(facMainReposDay);
    org.junit.Assert.assertNotNull(facMainReposMon);
    org.junit.Assert.assertNotNull(facMainReposHist);
  }

  @Override
  public FacMain findById(FacMainId facMainId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + facMainId);
    Optional<FacMain> facMain = null;
    if (dbName.equals(ContentName.onDay))
      facMain = facMainReposDay.findById(facMainId);
    else if (dbName.equals(ContentName.onMon))
      facMain = facMainReposMon.findById(facMainId);
    else if (dbName.equals(ContentName.onHist))
      facMain = facMainReposHist.findById(facMainId);
    else 
      facMain = facMainRepos.findById(facMainId);
    FacMain obj = facMain.isPresent() ? facMain.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<FacMain> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = facMainReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facMainReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facMainReposHist.findAll(pageable);
    else 
      slice = facMainRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacMain> facmCustNoRange(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmCustNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = facMainReposDay.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(custNo_0, custNo_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facMainReposMon.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(custNo_0, custNo_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facMainReposHist.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(custNo_0, custNo_1, facmNo_2, facmNo_3, pageable);
    else 
      slice = facMainRepos.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(custNo_0, custNo_1, facmNo_2, facmNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacMain> facmApplNoRange(int applNo_0, int applNo_1, int facmNo_2, int facmNo_3, String colSetFlag_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmApplNoRange " + dbName + " : " + "applNo_0 : " + applNo_0 + " applNo_1 : " +  applNo_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3 + " colSetFlag_4 : " +  colSetFlag_4);
    if (dbName.equals(ContentName.onDay))
      slice = facMainReposDay.findAllByApplNoGreaterThanEqualAndApplNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndColSetFlagLikeOrderByApplNoAscFacmNoAsc(applNo_0, applNo_1, facmNo_2, facmNo_3, colSetFlag_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facMainReposMon.findAllByApplNoGreaterThanEqualAndApplNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndColSetFlagLikeOrderByApplNoAscFacmNoAsc(applNo_0, applNo_1, facmNo_2, facmNo_3, colSetFlag_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facMainReposHist.findAllByApplNoGreaterThanEqualAndApplNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndColSetFlagLikeOrderByApplNoAscFacmNoAsc(applNo_0, applNo_1, facmNo_2, facmNo_3, colSetFlag_4, pageable);
    else 
      slice = facMainRepos.findAllByApplNoGreaterThanEqualAndApplNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndColSetFlagLikeOrderByApplNoAscFacmNoAsc(applNo_0, applNo_1, facmNo_2, facmNo_3, colSetFlag_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FacMain facmApplNoFirst(int applNo_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("facmApplNoFirst " + dbName + " : " + "applNo_0 : " + applNo_0);
    Optional<FacMain> facMainT = null;
    if (dbName.equals(ContentName.onDay))
      facMainT = facMainReposDay.findTopByApplNoIs(applNo_0);
    else if (dbName.equals(ContentName.onMon))
      facMainT = facMainReposMon.findTopByApplNoIs(applNo_0);
    else if (dbName.equals(ContentName.onHist))
      facMainT = facMainReposHist.findTopByApplNoIs(applNo_0);
    else 
      facMainT = facMainRepos.findTopByApplNoIs(applNo_0);

    return facMainT.isPresent() ? facMainT.get() : null;
  }

  @Override
  public Slice<FacMain> facmCreditSysNoRange(int creditSysNo_0, int creditSysNo_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmCreditSysNoRange " + dbName + " : " + "creditSysNo_0 : " + creditSysNo_0 + " creditSysNo_1 : " +  creditSysNo_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = facMainReposDay.findAllByCreditSysNoGreaterThanEqualAndCreditSysNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCreditSysNoAscFacmNoAsc(creditSysNo_0, creditSysNo_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facMainReposMon.findAllByCreditSysNoGreaterThanEqualAndCreditSysNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCreditSysNoAscFacmNoAsc(creditSysNo_0, creditSysNo_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facMainReposHist.findAllByCreditSysNoGreaterThanEqualAndCreditSysNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCreditSysNoAscFacmNoAsc(creditSysNo_0, creditSysNo_1, facmNo_2, facmNo_3, pageable);
    else 
      slice = facMainRepos.findAllByCreditSysNoGreaterThanEqualAndCreditSysNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCreditSysNoAscFacmNoAsc(creditSysNo_0, creditSysNo_1, facmNo_2, facmNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FacMain facmCreditSysNoFirst(int creditSysNo_0, int creditSysNo_1, int facmNo_2, int facmNo_3, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("facmCreditSysNoFirst " + dbName + " : " + "creditSysNo_0 : " + creditSysNo_0 + " creditSysNo_1 : " +  creditSysNo_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3);
    Optional<FacMain> facMainT = null;
    if (dbName.equals(ContentName.onDay))
      facMainT = facMainReposDay.findTopByCreditSysNoGreaterThanEqualAndCreditSysNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCreditSysNoAscFacmNoAsc(creditSysNo_0, creditSysNo_1, facmNo_2, facmNo_3);
    else if (dbName.equals(ContentName.onMon))
      facMainT = facMainReposMon.findTopByCreditSysNoGreaterThanEqualAndCreditSysNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCreditSysNoAscFacmNoAsc(creditSysNo_0, creditSysNo_1, facmNo_2, facmNo_3);
    else if (dbName.equals(ContentName.onHist))
      facMainT = facMainReposHist.findTopByCreditSysNoGreaterThanEqualAndCreditSysNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCreditSysNoAscFacmNoAsc(creditSysNo_0, creditSysNo_1, facmNo_2, facmNo_3);
    else 
      facMainT = facMainRepos.findTopByCreditSysNoGreaterThanEqualAndCreditSysNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCreditSysNoAscFacmNoAsc(creditSysNo_0, creditSysNo_1, facmNo_2, facmNo_3);

    return facMainT.isPresent() ? facMainT.get() : null;
  }

  @Override
  public Slice<FacMain> facmCreditOfficerRange(String creditOfficer_0, String creditOfficer_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmCreditOfficerRange " + dbName + " : " + "creditOfficer_0 : " + creditOfficer_0 + " creditOfficer_1 : " +  creditOfficer_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = facMainReposDay.findAllByCreditOfficerGreaterThanEqualAndCreditOfficerLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(creditOfficer_0, creditOfficer_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facMainReposMon.findAllByCreditOfficerGreaterThanEqualAndCreditOfficerLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(creditOfficer_0, creditOfficer_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facMainReposHist.findAllByCreditOfficerGreaterThanEqualAndCreditOfficerLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(creditOfficer_0, creditOfficer_1, facmNo_2, facmNo_3, pageable);
    else 
      slice = facMainRepos.findAllByCreditOfficerGreaterThanEqualAndCreditOfficerLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(creditOfficer_0, creditOfficer_1, facmNo_2, facmNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacMain> fildCustNoRange(int custNo_0, int custNo_1, String colSetFlag_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("fildCustNoRange " + dbName + " : " + "custNo_0 : " + custNo_0 + " custNo_1 : " +  custNo_1 + " colSetFlag_2 : " +  colSetFlag_2);
    if (dbName.equals(ContentName.onDay))
      slice = facMainReposDay.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndColSetFlagLikeOrderByApplNoAscFacmNoAsc(custNo_0, custNo_1, colSetFlag_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facMainReposMon.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndColSetFlagLikeOrderByApplNoAscFacmNoAsc(custNo_0, custNo_1, colSetFlag_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facMainReposHist.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndColSetFlagLikeOrderByApplNoAscFacmNoAsc(custNo_0, custNo_1, colSetFlag_2, pageable);
    else 
      slice = facMainRepos.findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndColSetFlagLikeOrderByApplNoAscFacmNoAsc(custNo_0, custNo_1, colSetFlag_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<FacMain> facmBusinessOfficerRange(String businessOfficer_0, String businessOfficer_1, int facmNo_2, int facmNo_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmBusinessOfficerRange " + dbName + " : " + "businessOfficer_0 : " + businessOfficer_0 + " businessOfficer_1 : " +  businessOfficer_1 + " facmNo_2 : " +  facmNo_2 + " facmNo_3 : " +  facmNo_3);
    if (dbName.equals(ContentName.onDay))
      slice = facMainReposDay.findAllByBusinessOfficerGreaterThanEqualAndBusinessOfficerLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(businessOfficer_0, businessOfficer_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facMainReposMon.findAllByBusinessOfficerGreaterThanEqualAndBusinessOfficerLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(businessOfficer_0, businessOfficer_1, facmNo_2, facmNo_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facMainReposHist.findAllByBusinessOfficerGreaterThanEqualAndBusinessOfficerLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(businessOfficer_0, businessOfficer_1, facmNo_2, facmNo_3, pageable);
    else 
      slice = facMainRepos.findAllByBusinessOfficerGreaterThanEqualAndBusinessOfficerLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByCustNoAscFacmNoAsc(businessOfficer_0, businessOfficer_1, facmNo_2, facmNo_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FacMain fildCustNoCreditSysNoFirst(int custNo_0, int creditSysNo_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("fildCustNoCreditSysNoFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " creditSysNo_1 : " +  creditSysNo_1);
    Optional<FacMain> facMainT = null;
    if (dbName.equals(ContentName.onDay))
      facMainT = facMainReposDay.findTopByCustNoIsAndCreditSysNoIsOrderByCustNoAscFacmNoAsc(custNo_0, creditSysNo_1);
    else if (dbName.equals(ContentName.onMon))
      facMainT = facMainReposMon.findTopByCustNoIsAndCreditSysNoIsOrderByCustNoAscFacmNoAsc(custNo_0, creditSysNo_1);
    else if (dbName.equals(ContentName.onHist))
      facMainT = facMainReposHist.findTopByCustNoIsAndCreditSysNoIsOrderByCustNoAscFacmNoAsc(custNo_0, creditSysNo_1);
    else 
      facMainT = facMainRepos.findTopByCustNoIsAndCreditSysNoIsOrderByCustNoAscFacmNoAsc(custNo_0, creditSysNo_1);

    return facMainT.isPresent() ? facMainT.get() : null;
  }

  @Override
  public Slice<FacMain> CustNoAll(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("CustNoAll " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = facMainReposDay.findAllByCustNoIsOrderByCreditSysNoAscFacmNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facMainReposMon.findAllByCustNoIsOrderByCreditSysNoAscFacmNoAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facMainReposHist.findAllByCustNoIsOrderByCreditSysNoAscFacmNoAsc(custNo_0, pageable);
    else 
      slice = facMainRepos.findAllByCustNoIsOrderByCreditSysNoAscFacmNoAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FacMain findProdNoFirst(String prodNo_0, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findProdNoFirst " + dbName + " : " + "prodNo_0 : " + prodNo_0);
    Optional<FacMain> facMainT = null;
    if (dbName.equals(ContentName.onDay))
      facMainT = facMainReposDay.findTopByProdNoIsOrderByCustNoAscFacmNoAsc(prodNo_0);
    else if (dbName.equals(ContentName.onMon))
      facMainT = facMainReposMon.findTopByProdNoIsOrderByCustNoAscFacmNoAsc(prodNo_0);
    else if (dbName.equals(ContentName.onHist))
      facMainT = facMainReposHist.findTopByProdNoIsOrderByCustNoAscFacmNoAsc(prodNo_0);
    else 
      facMainT = facMainRepos.findTopByProdNoIsOrderByCustNoAscFacmNoAsc(prodNo_0);

    return facMainT.isPresent() ? facMainT.get() : null;
  }

  @Override
  public Slice<FacMain> findRepayCodeEq(int repayCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<FacMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findRepayCodeEq " + dbName + " : " + "repayCode_0 : " + repayCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = facMainReposDay.findAllByRepayCodeIsOrderByCustNoAscFacmNoAsc(repayCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = facMainReposMon.findAllByRepayCodeIsOrderByCustNoAscFacmNoAsc(repayCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = facMainReposHist.findAllByRepayCodeIsOrderByCustNoAscFacmNoAsc(repayCode_0, pageable);
    else 
      slice = facMainRepos.findAllByRepayCodeIsOrderByCustNoAscFacmNoAsc(repayCode_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public FacMain holdById(FacMainId facMainId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + facMainId);
    Optional<FacMain> facMain = null;
    if (dbName.equals(ContentName.onDay))
      facMain = facMainReposDay.findByFacMainId(facMainId);
    else if (dbName.equals(ContentName.onMon))
      facMain = facMainReposMon.findByFacMainId(facMainId);
    else if (dbName.equals(ContentName.onHist))
      facMain = facMainReposHist.findByFacMainId(facMainId);
    else 
      facMain = facMainRepos.findByFacMainId(facMainId);
    return facMain.isPresent() ? facMain.get() : null;
  }

  @Override
  public FacMain holdById(FacMain facMain, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + facMain.getFacMainId());
    Optional<FacMain> facMainT = null;
    if (dbName.equals(ContentName.onDay))
      facMainT = facMainReposDay.findByFacMainId(facMain.getFacMainId());
    else if (dbName.equals(ContentName.onMon))
      facMainT = facMainReposMon.findByFacMainId(facMain.getFacMainId());
    else if (dbName.equals(ContentName.onHist))
      facMainT = facMainReposHist.findByFacMainId(facMain.getFacMainId());
    else 
      facMainT = facMainRepos.findByFacMainId(facMain.getFacMainId());
    return facMainT.isPresent() ? facMainT.get() : null;
  }

  @Override
  public FacMain insert(FacMain facMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + facMain.getFacMainId());
    if (this.findById(facMain.getFacMainId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      facMain.setCreateEmpNo(empNot);

    if(facMain.getLastUpdateEmpNo() == null || facMain.getLastUpdateEmpNo().isEmpty())
      facMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return facMainReposDay.saveAndFlush(facMain);	
    else if (dbName.equals(ContentName.onMon))
      return facMainReposMon.saveAndFlush(facMain);
    else if (dbName.equals(ContentName.onHist))
      return facMainReposHist.saveAndFlush(facMain);
    else 
    return facMainRepos.saveAndFlush(facMain);
  }

  @Override
  public FacMain update(FacMain facMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + facMain.getFacMainId());
    if (!empNot.isEmpty())
      facMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return facMainReposDay.saveAndFlush(facMain);	
    else if (dbName.equals(ContentName.onMon))
      return facMainReposMon.saveAndFlush(facMain);
    else if (dbName.equals(ContentName.onHist))
      return facMainReposHist.saveAndFlush(facMain);
    else 
    return facMainRepos.saveAndFlush(facMain);
  }

  @Override
  public FacMain update2(FacMain facMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + facMain.getFacMainId());
    if (!empNot.isEmpty())
      facMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      facMainReposDay.saveAndFlush(facMain);	
    else if (dbName.equals(ContentName.onMon))
      facMainReposMon.saveAndFlush(facMain);
    else if (dbName.equals(ContentName.onHist))
        facMainReposHist.saveAndFlush(facMain);
    else 
      facMainRepos.saveAndFlush(facMain);	
    return this.findById(facMain.getFacMainId());
  }

  @Override
  public void delete(FacMain facMain, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + facMain.getFacMainId());
    if (dbName.equals(ContentName.onDay)) {
      facMainReposDay.delete(facMain);	
      facMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facMainReposMon.delete(facMain);	
      facMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facMainReposHist.delete(facMain);
      facMainReposHist.flush();
    }
    else {
      facMainRepos.delete(facMain);
      facMainRepos.flush();
    }
   }

  @Override
  public void insertAll(List<FacMain> facMain, TitaVo... titaVo) throws DBException {
    if (facMain == null || facMain.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (FacMain t : facMain){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      facMain = facMainReposDay.saveAll(facMain);	
      facMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facMain = facMainReposMon.saveAll(facMain);	
      facMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facMain = facMainReposHist.saveAll(facMain);
      facMainReposHist.flush();
    }
    else {
      facMain = facMainRepos.saveAll(facMain);
      facMainRepos.flush();
    }
    }

  @Override
  public void updateAll(List<FacMain> facMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (facMain == null || facMain.size() == 0)
      throw new DBException(6);

    for (FacMain t : facMain) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      facMain = facMainReposDay.saveAll(facMain);	
      facMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facMain = facMainReposMon.saveAll(facMain);	
      facMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facMain = facMainReposHist.saveAll(facMain);
      facMainReposHist.flush();
    }
    else {
      facMain = facMainRepos.saveAll(facMain);
      facMainRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<FacMain> facMain, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (facMain == null || facMain.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      facMainReposDay.deleteAll(facMain);	
      facMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      facMainReposMon.deleteAll(facMain);	
      facMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      facMainReposHist.deleteAll(facMain);
      facMainReposHist.flush();
    }
    else {
      facMainRepos.deleteAll(facMain);
      facMainRepos.flush();
    }
  }

}
