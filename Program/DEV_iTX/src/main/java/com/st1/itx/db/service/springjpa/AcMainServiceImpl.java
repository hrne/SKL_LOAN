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
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.AcMainId;
import com.st1.itx.db.repository.online.AcMainRepository;
import com.st1.itx.db.repository.day.AcMainRepositoryDay;
import com.st1.itx.db.repository.mon.AcMainRepositoryMon;
import com.st1.itx.db.repository.hist.AcMainRepositoryHist;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("acMainService")
@Repository
public class AcMainServiceImpl implements AcMainService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(AcMainServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AcMainRepository acMainRepos;

  @Autowired
  private AcMainRepositoryDay acMainReposDay;

  @Autowired
  private AcMainRepositoryMon acMainReposMon;

  @Autowired
  private AcMainRepositoryHist acMainReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(acMainRepos);
    org.junit.Assert.assertNotNull(acMainReposDay);
    org.junit.Assert.assertNotNull(acMainReposMon);
    org.junit.Assert.assertNotNull(acMainReposHist);
  }

  @Override
  public AcMain findById(AcMainId acMainId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + acMainId);
    Optional<AcMain> acMain = null;
    if (dbName.equals(ContentName.onDay))
      acMain = acMainReposDay.findById(acMainId);
    else if (dbName.equals(ContentName.onMon))
      acMain = acMainReposMon.findById(acMainId);
    else if (dbName.equals(ContentName.onHist))
      acMain = acMainReposHist.findById(acMainId);
    else 
      acMain = acMainRepos.findById(acMainId);
    AcMain obj = acMain.isPresent() ? acMain.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AcMain> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcBookCode", "AcSubBookCode", "BranchNo", "CurrencyCode", "AcNoCode", "AcSubCode", "AcDtlCode", "AcDate"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = acMainReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acMainReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acMainReposHist.findAll(pageable);
    else 
      slice = acMainRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcMain> acmainAcDateRange(String acBookCode_0, String branchNo_1, String currencyCode_2, String acNoCode_3, String acSubCode_4, String acDtlCode_5, int acDate_6, int acDate_7, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("acmainAcDateRange " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " branchNo_1 : " +  branchNo_1 + " currencyCode_2 : " +  currencyCode_2 + " acNoCode_3 : " +  acNoCode_3 + " acSubCode_4 : " +  acSubCode_4 + " acDtlCode_5 : " +  acDtlCode_5 + " acDate_6 : " +  acDate_6 + " acDate_7 : " +  acDate_7);
    if (dbName.equals(ContentName.onDay))
      slice = acMainReposDay.findAllByAcBookCodeIsAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, branchNo_1, currencyCode_2, acNoCode_3, acSubCode_4, acDtlCode_5, acDate_6, acDate_7, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acMainReposMon.findAllByAcBookCodeIsAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, branchNo_1, currencyCode_2, acNoCode_3, acSubCode_4, acDtlCode_5, acDate_6, acDate_7, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acMainReposHist.findAllByAcBookCodeIsAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, branchNo_1, currencyCode_2, acNoCode_3, acSubCode_4, acDtlCode_5, acDate_6, acDate_7, pageable);
    else 
      slice = acMainRepos.findAllByAcBookCodeIsAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(acBookCode_0, branchNo_1, currencyCode_2, acNoCode_3, acSubCode_4, acDtlCode_5, acDate_6, acDate_7, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcMain> acmainAcCodeRange(String acBookCode_0, String branchNo_1, String currencyCode_2, int acDate_3, String acNoCode_4, String acNoCode_5, String acSubCode_6, String acSubCode_7, String acDtlCode_8, String acDtlCode_9, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("acmainAcCodeRange " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " branchNo_1 : " +  branchNo_1 + " currencyCode_2 : " +  currencyCode_2 + " acDate_3 : " +  acDate_3 + " acNoCode_4 : " +  acNoCode_4 + " acNoCode_5 : " +  acNoCode_5 + " acSubCode_6 : " +  acSubCode_6 + " acSubCode_7 : " +  acSubCode_7 + " acDtlCode_8 : " +  acDtlCode_8 + " acDtlCode_9 : " +  acDtlCode_9);
    if (dbName.equals(ContentName.onDay))
      slice = acMainReposDay.findAllByAcBookCodeIsAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, branchNo_1, currencyCode_2, acDate_3, acNoCode_4, acNoCode_5, acSubCode_6, acSubCode_7, acDtlCode_8, acDtlCode_9, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acMainReposMon.findAllByAcBookCodeIsAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, branchNo_1, currencyCode_2, acDate_3, acNoCode_4, acNoCode_5, acSubCode_6, acSubCode_7, acDtlCode_8, acDtlCode_9, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acMainReposHist.findAllByAcBookCodeIsAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, branchNo_1, currencyCode_2, acDate_3, acNoCode_4, acNoCode_5, acSubCode_6, acSubCode_7, acDtlCode_8, acDtlCode_9, pageable);
    else 
      slice = acMainRepos.findAllByAcBookCodeIsAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acBookCode_0, branchNo_1, currencyCode_2, acDate_3, acNoCode_4, acNoCode_5, acSubCode_6, acSubCode_7, acDtlCode_8, acDtlCode_9, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcMain> acmainAcBookCodeRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, String acDtlCode_6, int acDate_7, int acDate_8, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("acmainAcBookCodeRange " + dbName + " : " + "acBookCode_0 : " + acBookCode_0 + " acSubBookCode_1 : " +  acSubBookCode_1 + " branchNo_2 : " +  branchNo_2 + " currencyCode_3 : " +  currencyCode_3 + " acNoCode_4 : " +  acNoCode_4 + " acSubCode_5 : " +  acSubCode_5 + " acDtlCode_6 : " +  acDtlCode_6 + " acDate_7 : " +  acDate_7 + " acDate_8 : " +  acDate_8);
    if (dbName.equals(ContentName.onDay))
      slice = acMainReposDay.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAscAcSubBookCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, acDate_7, acDate_8, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acMainReposMon.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAscAcSubBookCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, acDate_7, acDate_8, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acMainReposHist.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAscAcSubBookCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, acDate_7, acDate_8, pageable);
    else 
      slice = acMainRepos.findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAscAcSubBookCodeAsc(acBookCode_0, acSubBookCode_1, branchNo_2, currencyCode_3, acNoCode_4, acSubCode_5, acDtlCode_6, acDate_7, acDate_8, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcMain> acmainAcDateEq(int acDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("acmainAcDateEq " + dbName + " : " + "acDate_0 : " + acDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = acMainReposDay.findAllByAcDateIsOrderByAcBookCodeAscBranchNoAscCurrencyCodeAscAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acMainReposMon.findAllByAcDateIsOrderByAcBookCodeAscBranchNoAscCurrencyCodeAscAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acMainReposHist.findAllByAcDateIsOrderByAcBookCodeAscBranchNoAscCurrencyCodeAscAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acDate_0, pageable);
    else 
      slice = acMainRepos.findAllByAcDateIsOrderByAcBookCodeAscBranchNoAscCurrencyCodeAscAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(acDate_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AcMain> acctCodeEq(int acDate_0, String acctCode_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AcMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("acctCodeEq " + dbName + " : " + "acDate_0 : " + acDate_0 + " acctCode_1 : " +  acctCode_1);
    if (dbName.equals(ContentName.onDay))
      slice = acMainReposDay.findAllByAcDateIsAndAcctCodeIsOrderByAcBookCodeAscBranchNoAscCurrencyCodeAsc(acDate_0, acctCode_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = acMainReposMon.findAllByAcDateIsAndAcctCodeIsOrderByAcBookCodeAscBranchNoAscCurrencyCodeAsc(acDate_0, acctCode_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = acMainReposHist.findAllByAcDateIsAndAcctCodeIsOrderByAcBookCodeAscBranchNoAscCurrencyCodeAsc(acDate_0, acctCode_1, pageable);
    else 
      slice = acMainRepos.findAllByAcDateIsAndAcctCodeIsOrderByAcBookCodeAscBranchNoAscCurrencyCodeAsc(acDate_0, acctCode_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AcMain holdById(AcMainId acMainId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + acMainId);
    Optional<AcMain> acMain = null;
    if (dbName.equals(ContentName.onDay))
      acMain = acMainReposDay.findByAcMainId(acMainId);
    else if (dbName.equals(ContentName.onMon))
      acMain = acMainReposMon.findByAcMainId(acMainId);
    else if (dbName.equals(ContentName.onHist))
      acMain = acMainReposHist.findByAcMainId(acMainId);
    else 
      acMain = acMainRepos.findByAcMainId(acMainId);
    return acMain.isPresent() ? acMain.get() : null;
  }

  @Override
  public AcMain holdById(AcMain acMain, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + acMain.getAcMainId());
    Optional<AcMain> acMainT = null;
    if (dbName.equals(ContentName.onDay))
      acMainT = acMainReposDay.findByAcMainId(acMain.getAcMainId());
    else if (dbName.equals(ContentName.onMon))
      acMainT = acMainReposMon.findByAcMainId(acMain.getAcMainId());
    else if (dbName.equals(ContentName.onHist))
      acMainT = acMainReposHist.findByAcMainId(acMain.getAcMainId());
    else 
      acMainT = acMainRepos.findByAcMainId(acMain.getAcMainId());
    return acMainT.isPresent() ? acMainT.get() : null;
  }

  @Override
  public AcMain insert(AcMain acMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + acMain.getAcMainId());
    if (this.findById(acMain.getAcMainId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      acMain.setCreateEmpNo(empNot);

    if(acMain.getLastUpdateEmpNo() == null || acMain.getLastUpdateEmpNo().isEmpty())
      acMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acMainReposDay.saveAndFlush(acMain);	
    else if (dbName.equals(ContentName.onMon))
      return acMainReposMon.saveAndFlush(acMain);
    else if (dbName.equals(ContentName.onHist))
      return acMainReposHist.saveAndFlush(acMain);
    else 
    return acMainRepos.saveAndFlush(acMain);
  }

  @Override
  public AcMain update(AcMain acMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + acMain.getAcMainId());
    if (!empNot.isEmpty())
      acMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return acMainReposDay.saveAndFlush(acMain);	
    else if (dbName.equals(ContentName.onMon))
      return acMainReposMon.saveAndFlush(acMain);
    else if (dbName.equals(ContentName.onHist))
      return acMainReposHist.saveAndFlush(acMain);
    else 
    return acMainRepos.saveAndFlush(acMain);
  }

  @Override
  public AcMain update2(AcMain acMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + acMain.getAcMainId());
    if (!empNot.isEmpty())
      acMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      acMainReposDay.saveAndFlush(acMain);	
    else if (dbName.equals(ContentName.onMon))
      acMainReposMon.saveAndFlush(acMain);
    else if (dbName.equals(ContentName.onHist))
        acMainReposHist.saveAndFlush(acMain);
    else 
      acMainRepos.saveAndFlush(acMain);	
    return this.findById(acMain.getAcMainId());
  }

  @Override
  public void delete(AcMain acMain, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + acMain.getAcMainId());
    if (dbName.equals(ContentName.onDay)) {
      acMainReposDay.delete(acMain);	
      acMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acMainReposMon.delete(acMain);	
      acMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acMainReposHist.delete(acMain);
      acMainReposHist.flush();
    }
    else {
      acMainRepos.delete(acMain);
      acMainRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AcMain> acMain, TitaVo... titaVo) throws DBException {
    if (acMain == null || acMain.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (AcMain t : acMain){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      acMain = acMainReposDay.saveAll(acMain);	
      acMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acMain = acMainReposMon.saveAll(acMain);	
      acMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acMain = acMainReposHist.saveAll(acMain);
      acMainReposHist.flush();
    }
    else {
      acMain = acMainRepos.saveAll(acMain);
      acMainRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AcMain> acMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (acMain == null || acMain.size() == 0)
      throw new DBException(6);

    for (AcMain t : acMain) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      acMain = acMainReposDay.saveAll(acMain);	
      acMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acMain = acMainReposMon.saveAll(acMain);	
      acMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acMain = acMainReposHist.saveAll(acMain);
      acMainReposHist.flush();
    }
    else {
      acMain = acMainRepos.saveAll(acMain);
      acMainRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AcMain> acMain, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (acMain == null || acMain.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      acMainReposDay.deleteAll(acMain);	
      acMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      acMainReposMon.deleteAll(acMain);	
      acMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      acMainReposHist.deleteAll(acMain);
      acMainReposHist.flush();
    }
    else {
      acMainRepos.deleteAll(acMain);
      acMainRepos.flush();
    }
  }

}
