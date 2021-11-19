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
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.BankDeductDtlId;
import com.st1.itx.db.repository.online.BankDeductDtlRepository;
import com.st1.itx.db.repository.day.BankDeductDtlRepositoryDay;
import com.st1.itx.db.repository.mon.BankDeductDtlRepositoryMon;
import com.st1.itx.db.repository.hist.BankDeductDtlRepositoryHist;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("bankDeductDtlService")
@Repository
public class BankDeductDtlServiceImpl extends ASpringJpaParm implements BankDeductDtlService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private BankDeductDtlRepository bankDeductDtlRepos;

  @Autowired
  private BankDeductDtlRepositoryDay bankDeductDtlReposDay;

  @Autowired
  private BankDeductDtlRepositoryMon bankDeductDtlReposMon;

  @Autowired
  private BankDeductDtlRepositoryHist bankDeductDtlReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(bankDeductDtlRepos);
    org.junit.Assert.assertNotNull(bankDeductDtlReposDay);
    org.junit.Assert.assertNotNull(bankDeductDtlReposMon);
    org.junit.Assert.assertNotNull(bankDeductDtlReposHist);
  }

  @Override
  public BankDeductDtl findById(BankDeductDtlId bankDeductDtlId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + bankDeductDtlId);
    Optional<BankDeductDtl> bankDeductDtl = null;
    if (dbName.equals(ContentName.onDay))
      bankDeductDtl = bankDeductDtlReposDay.findById(bankDeductDtlId);
    else if (dbName.equals(ContentName.onMon))
      bankDeductDtl = bankDeductDtlReposMon.findById(bankDeductDtlId);
    else if (dbName.equals(ContentName.onHist))
      bankDeductDtl = bankDeductDtlReposHist.findById(bankDeductDtlId);
    else 
      bankDeductDtl = bankDeductDtlRepos.findById(bankDeductDtlId);
    BankDeductDtl obj = bankDeductDtl.isPresent() ? bankDeductDtl.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<BankDeductDtl> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "EntryDate", "CustNo", "FacmNo", "RepayType", "PayIntDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EntryDate", "CustNo", "FacmNo", "RepayType", "PayIntDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = bankDeductDtlReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankDeductDtlReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankDeductDtlReposHist.findAll(pageable);
    else 
      slice = bankDeductDtlRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankDeductDtl> sortByAll(int custNo_0, int entryDate_1, int entryDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("sortByAll " + dbName + " : " + "custNo_0 : " + custNo_0 + " entryDate_1 : " +  entryDate_1 + " entryDate_2 : " +  entryDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = bankDeductDtlReposDay.findAllByCustNoIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAsc(custNo_0, entryDate_1, entryDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankDeductDtlReposMon.findAllByCustNoIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAsc(custNo_0, entryDate_1, entryDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankDeductDtlReposHist.findAllByCustNoIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAsc(custNo_0, entryDate_1, entryDate_2, pageable);
    else 
      slice = bankDeductDtlRepos.findAllByCustNoIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateAsc(custNo_0, entryDate_1, entryDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankDeductDtl> custNoEq(int custNo_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("custNoEq " + dbName + " : " + "custNo_0 : " + custNo_0);
    if (dbName.equals(ContentName.onDay))
      slice = bankDeductDtlReposDay.findAllByCustNoIsOrderByEntryDateAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankDeductDtlReposMon.findAllByCustNoIsOrderByEntryDateAsc(custNo_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankDeductDtlReposHist.findAllByCustNoIsOrderByEntryDateAsc(custNo_0, pageable);
    else 
      slice = bankDeductDtlRepos.findAllByCustNoIsOrderByEntryDateAsc(custNo_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankDeductDtl> entryDateRng(int entryDate_0, int entryDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("entryDateRng " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " +  entryDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = bankDeductDtlReposDay.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(entryDate_0, entryDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankDeductDtlReposMon.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(entryDate_0, entryDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankDeductDtlReposHist.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(entryDate_0, entryDate_1, pageable);
    else 
      slice = bankDeductDtlRepos.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(entryDate_0, entryDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankDeductDtl> mediaSeqRng(int mediaDate_0, String mediaKind_1, int mediaSeq_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("mediaSeqRng " + dbName + " : " + "mediaDate_0 : " + mediaDate_0 + " mediaKind_1 : " +  mediaKind_1 + " mediaSeq_2 : " +  mediaSeq_2);
    if (dbName.equals(ContentName.onDay))
      slice = bankDeductDtlReposDay.findAllByMediaDateIsAndMediaKindIsAndMediaSeqIsOrderByMediaKindAscMediaSeqAsc(mediaDate_0, mediaKind_1, mediaSeq_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankDeductDtlReposMon.findAllByMediaDateIsAndMediaKindIsAndMediaSeqIsOrderByMediaKindAscMediaSeqAsc(mediaDate_0, mediaKind_1, mediaSeq_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankDeductDtlReposHist.findAllByMediaDateIsAndMediaKindIsAndMediaSeqIsOrderByMediaKindAscMediaSeqAsc(mediaDate_0, mediaKind_1, mediaSeq_2, pageable);
    else 
      slice = bankDeductDtlRepos.findAllByMediaDateIsAndMediaKindIsAndMediaSeqIsOrderByMediaKindAscMediaSeqAsc(mediaDate_0, mediaKind_1, mediaSeq_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankDeductDtl> repayBankEq(String repayBank_0, int entryDate_1, int entryDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("repayBankEq " + dbName + " : " + "repayBank_0 : " + repayBank_0 + " entryDate_1 : " +  entryDate_1 + " entryDate_2 : " +  entryDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = bankDeductDtlReposDay.findAllByRepayBankIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(repayBank_0, entryDate_1, entryDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankDeductDtlReposMon.findAllByRepayBankIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(repayBank_0, entryDate_1, entryDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankDeductDtlReposHist.findAllByRepayBankIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(repayBank_0, entryDate_1, entryDate_2, pageable);
    else 
      slice = bankDeductDtlRepos.findAllByRepayBankIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(repayBank_0, entryDate_1, entryDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankDeductDtl> deductNoticeA(int entryDate_0, int entryDate_1, int repayType_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("deductNoticeA " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " +  entryDate_1 + " repayType_2 : " +  repayType_2);
    if (dbName.equals(ContentName.onDay))
      slice = bankDeductDtlReposDay.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRepayTypeIs(entryDate_0, entryDate_1, repayType_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankDeductDtlReposMon.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRepayTypeIs(entryDate_0, entryDate_1, repayType_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankDeductDtlReposHist.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRepayTypeIs(entryDate_0, entryDate_1, repayType_2, pageable);
    else 
      slice = bankDeductDtlRepos.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRepayTypeIs(entryDate_0, entryDate_1, repayType_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankDeductDtl> facmNoRange(int entryDate_0, int custNo_1, int facmNo_2, int repayType_3, int payIntDate_4, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmNoRange " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " custNo_1 : " +  custNo_1 + " facmNo_2 : " +  facmNo_2 + " repayType_3 : " +  repayType_3 + " payIntDate_4 : " +  payIntDate_4);
    if (dbName.equals(ContentName.onDay))
      slice = bankDeductDtlReposDay.findAllByEntryDateIsAndCustNoIsAndFacmNoIsAndRepayTypeIsAndPayIntDateIs(entryDate_0, custNo_1, facmNo_2, repayType_3, payIntDate_4, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankDeductDtlReposMon.findAllByEntryDateIsAndCustNoIsAndFacmNoIsAndRepayTypeIsAndPayIntDateIs(entryDate_0, custNo_1, facmNo_2, repayType_3, payIntDate_4, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankDeductDtlReposHist.findAllByEntryDateIsAndCustNoIsAndFacmNoIsAndRepayTypeIsAndPayIntDateIs(entryDate_0, custNo_1, facmNo_2, repayType_3, payIntDate_4, pageable);
    else 
      slice = bankDeductDtlRepos.findAllByEntryDateIsAndCustNoIsAndFacmNoIsAndRepayTypeIsAndPayIntDateIs(entryDate_0, custNo_1, facmNo_2, repayType_3, payIntDate_4, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankDeductDtl> findL4943Eq(int entryDate_0, int entryDate_1, String repayBank_2, int repayType_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4943Eq " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " +  entryDate_1 + " repayBank_2 : " +  repayBank_2 + " repayType_3 : " +  repayType_3);
    if (dbName.equals(ContentName.onDay))
      slice = bankDeductDtlReposDay.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRepayBankIsAndRepayTypeIs(entryDate_0, entryDate_1, repayBank_2, repayType_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankDeductDtlReposMon.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRepayBankIsAndRepayTypeIs(entryDate_0, entryDate_1, repayBank_2, repayType_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankDeductDtlReposHist.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRepayBankIsAndRepayTypeIs(entryDate_0, entryDate_1, repayBank_2, repayType_3, pageable);
    else 
      slice = bankDeductDtlRepos.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndRepayBankIsAndRepayTypeIs(entryDate_0, entryDate_1, repayBank_2, repayType_3, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<BankDeductDtl> findL4452Rng(int mediaDate_0, int mediaDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4452Rng " + dbName + " : " + "mediaDate_0 : " + mediaDate_0 + " mediaDate_1 : " +  mediaDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = bankDeductDtlReposDay.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqual(mediaDate_0, mediaDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankDeductDtlReposMon.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqual(mediaDate_0, mediaDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankDeductDtlReposHist.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqual(mediaDate_0, mediaDate_1, pageable);
    else 
      slice = bankDeductDtlRepos.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqual(mediaDate_0, mediaDate_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BankDeductDtl findL4450EntryDateFirst(int custNo_0, int facmNo_1, int repayType_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findL4450EntryDateFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " repayType_2 : " +  repayType_2);
    Optional<BankDeductDtl> bankDeductDtlT = null;
    if (dbName.equals(ContentName.onDay))
      bankDeductDtlT = bankDeductDtlReposDay.findTopByCustNoIsAndFacmNoIsAndRepayTypeIsOrderByEntryDateDesc(custNo_0, facmNo_1, repayType_2);
    else if (dbName.equals(ContentName.onMon))
      bankDeductDtlT = bankDeductDtlReposMon.findTopByCustNoIsAndFacmNoIsAndRepayTypeIsOrderByEntryDateDesc(custNo_0, facmNo_1, repayType_2);
    else if (dbName.equals(ContentName.onHist))
      bankDeductDtlT = bankDeductDtlReposHist.findTopByCustNoIsAndFacmNoIsAndRepayTypeIsOrderByEntryDateDesc(custNo_0, facmNo_1, repayType_2);
    else 
      bankDeductDtlT = bankDeductDtlRepos.findTopByCustNoIsAndFacmNoIsAndRepayTypeIsOrderByEntryDateDesc(custNo_0, facmNo_1, repayType_2);

    return bankDeductDtlT.isPresent() ? bankDeductDtlT.get() : null;
  }

  @Override
  public BankDeductDtl findL4450PrevIntDateFirst(int custNo_0, int facmNo_1, int prevIntDate_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findL4450PrevIntDateFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " prevIntDate_2 : " +  prevIntDate_2);
    Optional<BankDeductDtl> bankDeductDtlT = null;
    if (dbName.equals(ContentName.onDay))
      bankDeductDtlT = bankDeductDtlReposDay.findTopByCustNoIsAndFacmNoIsAndPrevIntDateIsOrderByEntryDateDesc(custNo_0, facmNo_1, prevIntDate_2);
    else if (dbName.equals(ContentName.onMon))
      bankDeductDtlT = bankDeductDtlReposMon.findTopByCustNoIsAndFacmNoIsAndPrevIntDateIsOrderByEntryDateDesc(custNo_0, facmNo_1, prevIntDate_2);
    else if (dbName.equals(ContentName.onHist))
      bankDeductDtlT = bankDeductDtlReposHist.findTopByCustNoIsAndFacmNoIsAndPrevIntDateIsOrderByEntryDateDesc(custNo_0, facmNo_1, prevIntDate_2);
    else 
      bankDeductDtlT = bankDeductDtlRepos.findTopByCustNoIsAndFacmNoIsAndPrevIntDateIsOrderByEntryDateDesc(custNo_0, facmNo_1, prevIntDate_2);

    return bankDeductDtlT.isPresent() ? bankDeductDtlT.get() : null;
  }

  @Override
  public BankDeductDtl findL4451First(int entryDate_0, int entryDate_1, String mediaKind_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findL4451First " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " +  entryDate_1 + " mediaKind_2 : " +  mediaKind_2);
    Optional<BankDeductDtl> bankDeductDtlT = null;
    if (dbName.equals(ContentName.onDay))
      bankDeductDtlT = bankDeductDtlReposDay.findTopByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndMediaKindIsOrderByMediaSeqDesc(entryDate_0, entryDate_1, mediaKind_2);
    else if (dbName.equals(ContentName.onMon))
      bankDeductDtlT = bankDeductDtlReposMon.findTopByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndMediaKindIsOrderByMediaSeqDesc(entryDate_0, entryDate_1, mediaKind_2);
    else if (dbName.equals(ContentName.onHist))
      bankDeductDtlT = bankDeductDtlReposHist.findTopByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndMediaKindIsOrderByMediaSeqDesc(entryDate_0, entryDate_1, mediaKind_2);
    else 
      bankDeductDtlT = bankDeductDtlRepos.findTopByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndMediaKindIsOrderByMediaSeqDesc(entryDate_0, entryDate_1, mediaKind_2);

    return bankDeductDtlT.isPresent() ? bankDeductDtlT.get() : null;
  }

  @Override
  public Slice<BankDeductDtl> repayBankNotEq(String repayBank_0, int entryDate_1, int entryDate_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<BankDeductDtl> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("repayBankNotEq " + dbName + " : " + "repayBank_0 : " + repayBank_0 + " entryDate_1 : " +  entryDate_1 + " entryDate_2 : " +  entryDate_2);
    if (dbName.equals(ContentName.onDay))
      slice = bankDeductDtlReposDay.findAllByRepayBankNotAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(repayBank_0, entryDate_1, entryDate_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = bankDeductDtlReposMon.findAllByRepayBankNotAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(repayBank_0, entryDate_1, entryDate_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = bankDeductDtlReposHist.findAllByRepayBankNotAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(repayBank_0, entryDate_1, entryDate_2, pageable);
    else 
      slice = bankDeductDtlRepos.findAllByRepayBankNotAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscFacmNoAscPayIntDateAscRepayTypeDesc(repayBank_0, entryDate_1, entryDate_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public BankDeductDtl holdById(BankDeductDtlId bankDeductDtlId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankDeductDtlId);
    Optional<BankDeductDtl> bankDeductDtl = null;
    if (dbName.equals(ContentName.onDay))
      bankDeductDtl = bankDeductDtlReposDay.findByBankDeductDtlId(bankDeductDtlId);
    else if (dbName.equals(ContentName.onMon))
      bankDeductDtl = bankDeductDtlReposMon.findByBankDeductDtlId(bankDeductDtlId);
    else if (dbName.equals(ContentName.onHist))
      bankDeductDtl = bankDeductDtlReposHist.findByBankDeductDtlId(bankDeductDtlId);
    else 
      bankDeductDtl = bankDeductDtlRepos.findByBankDeductDtlId(bankDeductDtlId);
    return bankDeductDtl.isPresent() ? bankDeductDtl.get() : null;
  }

  @Override
  public BankDeductDtl holdById(BankDeductDtl bankDeductDtl, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + bankDeductDtl.getBankDeductDtlId());
    Optional<BankDeductDtl> bankDeductDtlT = null;
    if (dbName.equals(ContentName.onDay))
      bankDeductDtlT = bankDeductDtlReposDay.findByBankDeductDtlId(bankDeductDtl.getBankDeductDtlId());
    else if (dbName.equals(ContentName.onMon))
      bankDeductDtlT = bankDeductDtlReposMon.findByBankDeductDtlId(bankDeductDtl.getBankDeductDtlId());
    else if (dbName.equals(ContentName.onHist))
      bankDeductDtlT = bankDeductDtlReposHist.findByBankDeductDtlId(bankDeductDtl.getBankDeductDtlId());
    else 
      bankDeductDtlT = bankDeductDtlRepos.findByBankDeductDtlId(bankDeductDtl.getBankDeductDtlId());
    return bankDeductDtlT.isPresent() ? bankDeductDtlT.get() : null;
  }

  @Override
  public BankDeductDtl insert(BankDeductDtl bankDeductDtl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + bankDeductDtl.getBankDeductDtlId());
    if (this.findById(bankDeductDtl.getBankDeductDtlId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      bankDeductDtl.setCreateEmpNo(empNot);

    if(bankDeductDtl.getLastUpdateEmpNo() == null || bankDeductDtl.getLastUpdateEmpNo().isEmpty())
      bankDeductDtl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankDeductDtlReposDay.saveAndFlush(bankDeductDtl);	
    else if (dbName.equals(ContentName.onMon))
      return bankDeductDtlReposMon.saveAndFlush(bankDeductDtl);
    else if (dbName.equals(ContentName.onHist))
      return bankDeductDtlReposHist.saveAndFlush(bankDeductDtl);
    else 
    return bankDeductDtlRepos.saveAndFlush(bankDeductDtl);
  }

  @Override
  public BankDeductDtl update(BankDeductDtl bankDeductDtl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + bankDeductDtl.getBankDeductDtlId());
    if (!empNot.isEmpty())
      bankDeductDtl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return bankDeductDtlReposDay.saveAndFlush(bankDeductDtl);	
    else if (dbName.equals(ContentName.onMon))
      return bankDeductDtlReposMon.saveAndFlush(bankDeductDtl);
    else if (dbName.equals(ContentName.onHist))
      return bankDeductDtlReposHist.saveAndFlush(bankDeductDtl);
    else 
    return bankDeductDtlRepos.saveAndFlush(bankDeductDtl);
  }

  @Override
  public BankDeductDtl update2(BankDeductDtl bankDeductDtl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + bankDeductDtl.getBankDeductDtlId());
    if (!empNot.isEmpty())
      bankDeductDtl.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      bankDeductDtlReposDay.saveAndFlush(bankDeductDtl);	
    else if (dbName.equals(ContentName.onMon))
      bankDeductDtlReposMon.saveAndFlush(bankDeductDtl);
    else if (dbName.equals(ContentName.onHist))
        bankDeductDtlReposHist.saveAndFlush(bankDeductDtl);
    else 
      bankDeductDtlRepos.saveAndFlush(bankDeductDtl);	
    return this.findById(bankDeductDtl.getBankDeductDtlId());
  }

  @Override
  public void delete(BankDeductDtl bankDeductDtl, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + bankDeductDtl.getBankDeductDtlId());
    if (dbName.equals(ContentName.onDay)) {
      bankDeductDtlReposDay.delete(bankDeductDtl);	
      bankDeductDtlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankDeductDtlReposMon.delete(bankDeductDtl);	
      bankDeductDtlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankDeductDtlReposHist.delete(bankDeductDtl);
      bankDeductDtlReposHist.flush();
    }
    else {
      bankDeductDtlRepos.delete(bankDeductDtl);
      bankDeductDtlRepos.flush();
    }
   }

  @Override
  public void insertAll(List<BankDeductDtl> bankDeductDtl, TitaVo... titaVo) throws DBException {
    if (bankDeductDtl == null || bankDeductDtl.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (BankDeductDtl t : bankDeductDtl){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      bankDeductDtl = bankDeductDtlReposDay.saveAll(bankDeductDtl);	
      bankDeductDtlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankDeductDtl = bankDeductDtlReposMon.saveAll(bankDeductDtl);	
      bankDeductDtlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankDeductDtl = bankDeductDtlReposHist.saveAll(bankDeductDtl);
      bankDeductDtlReposHist.flush();
    }
    else {
      bankDeductDtl = bankDeductDtlRepos.saveAll(bankDeductDtl);
      bankDeductDtlRepos.flush();
    }
    }

  @Override
  public void updateAll(List<BankDeductDtl> bankDeductDtl, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (bankDeductDtl == null || bankDeductDtl.size() == 0)
      throw new DBException(6);

    for (BankDeductDtl t : bankDeductDtl) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      bankDeductDtl = bankDeductDtlReposDay.saveAll(bankDeductDtl);	
      bankDeductDtlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankDeductDtl = bankDeductDtlReposMon.saveAll(bankDeductDtl);	
      bankDeductDtlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankDeductDtl = bankDeductDtlReposHist.saveAll(bankDeductDtl);
      bankDeductDtlReposHist.flush();
    }
    else {
      bankDeductDtl = bankDeductDtlRepos.saveAll(bankDeductDtl);
      bankDeductDtlRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<BankDeductDtl> bankDeductDtl, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (bankDeductDtl == null || bankDeductDtl.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      bankDeductDtlReposDay.deleteAll(bankDeductDtl);	
      bankDeductDtlReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      bankDeductDtlReposMon.deleteAll(bankDeductDtl);	
      bankDeductDtlReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      bankDeductDtlReposHist.deleteAll(bankDeductDtl);
      bankDeductDtlReposHist.flush();
    }
    else {
      bankDeductDtlRepos.deleteAll(bankDeductDtl);
      bankDeductDtlRepos.flush();
    }
  }

}
