package com.st1.itx.db.service.springjpa;

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
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.domain.EmpDeductMediaId;
import com.st1.itx.db.repository.online.EmpDeductMediaRepository;
import com.st1.itx.db.repository.day.EmpDeductMediaRepositoryDay;
import com.st1.itx.db.repository.mon.EmpDeductMediaRepositoryMon;
import com.st1.itx.db.repository.hist.EmpDeductMediaRepositoryHist;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("empDeductMediaService")
@Repository
public class EmpDeductMediaServiceImpl extends ASpringJpaParm implements EmpDeductMediaService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private EmpDeductMediaRepository empDeductMediaRepos;

  @Autowired
  private EmpDeductMediaRepositoryDay empDeductMediaReposDay;

  @Autowired
  private EmpDeductMediaRepositoryMon empDeductMediaReposMon;

  @Autowired
  private EmpDeductMediaRepositoryHist empDeductMediaReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(empDeductMediaRepos);
    org.junit.Assert.assertNotNull(empDeductMediaReposDay);
    org.junit.Assert.assertNotNull(empDeductMediaReposMon);
    org.junit.Assert.assertNotNull(empDeductMediaReposHist);
  }

  @Override
  public EmpDeductMedia findById(EmpDeductMediaId empDeductMediaId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + empDeductMediaId);
    Optional<EmpDeductMedia> empDeductMedia = null;
    if (dbName.equals(ContentName.onDay))
      empDeductMedia = empDeductMediaReposDay.findById(empDeductMediaId);
    else if (dbName.equals(ContentName.onMon))
      empDeductMedia = empDeductMediaReposMon.findById(empDeductMediaId);
    else if (dbName.equals(ContentName.onHist))
      empDeductMedia = empDeductMediaReposHist.findById(empDeductMediaId);
    else 
      empDeductMedia = empDeductMediaRepos.findById(empDeductMediaId);
    EmpDeductMedia obj = empDeductMedia.isPresent() ? empDeductMedia.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<EmpDeductMedia> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductMedia> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "MediaDate", "MediaKind", "MediaSeq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "MediaDate", "MediaKind", "MediaSeq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductMediaReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductMediaReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductMediaReposHist.findAll(pageable);
    else 
      slice = empDeductMediaRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public EmpDeductMedia detailSeqFirst(int acDate_0, String batchNo_1, int detailSeq_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("detailSeqFirst " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1 + " detailSeq_2 : " +  detailSeq_2);
    Optional<EmpDeductMedia> empDeductMediaT = null;
    if (dbName.equals(ContentName.onDay))
      empDeductMediaT = empDeductMediaReposDay.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);
    else if (dbName.equals(ContentName.onMon))
      empDeductMediaT = empDeductMediaReposMon.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);
    else if (dbName.equals(ContentName.onHist))
      empDeductMediaT = empDeductMediaReposHist.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);
    else 
      empDeductMediaT = empDeductMediaRepos.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);

    return empDeductMediaT.isPresent() ? empDeductMediaT.get() : null;
  }

  @Override
  public EmpDeductMedia receiveCheckFirst(String mediaKind_0, int custNo_1, int entryDate_2, int repayCode_3, BigDecimal repayAmt_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("receiveCheckFirst " + dbName + " : " + "mediaKind_0 : " + mediaKind_0 + " custNo_1 : " +  custNo_1 + " entryDate_2 : " +  entryDate_2 + " repayCode_3 : " +  repayCode_3 + " repayAmt_4 : " +  repayAmt_4);
    Optional<EmpDeductMedia> empDeductMediaT = null;
    if (dbName.equals(ContentName.onDay))
      empDeductMediaT = empDeductMediaReposDay.findTopByMediaKindIsAndCustNoIsAndEntryDateIsAndRepayCodeIsAndRepayAmtIsOrderByMediaDateDesc(mediaKind_0, custNo_1, entryDate_2, repayCode_3, repayAmt_4);
    else if (dbName.equals(ContentName.onMon))
      empDeductMediaT = empDeductMediaReposMon.findTopByMediaKindIsAndCustNoIsAndEntryDateIsAndRepayCodeIsAndRepayAmtIsOrderByMediaDateDesc(mediaKind_0, custNo_1, entryDate_2, repayCode_3, repayAmt_4);
    else if (dbName.equals(ContentName.onHist))
      empDeductMediaT = empDeductMediaReposHist.findTopByMediaKindIsAndCustNoIsAndEntryDateIsAndRepayCodeIsAndRepayAmtIsOrderByMediaDateDesc(mediaKind_0, custNo_1, entryDate_2, repayCode_3, repayAmt_4);
    else 
      empDeductMediaT = empDeductMediaRepos.findTopByMediaKindIsAndCustNoIsAndEntryDateIsAndRepayCodeIsAndRepayAmtIsOrderByMediaDateDesc(mediaKind_0, custNo_1, entryDate_2, repayCode_3, repayAmt_4);

    return empDeductMediaT.isPresent() ? empDeductMediaT.get() : null;
  }

  @Override
  public Slice<EmpDeductMedia> mediaDateRng(int mediaDate_0, int mediaDate_1, String mediaKind_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductMedia> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("mediaDateRng " + dbName + " : " + "mediaDate_0 : " + mediaDate_0 + " mediaDate_1 : " +  mediaDate_1 + " mediaKind_2 : " +  mediaKind_2);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductMediaReposDay.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqualAndMediaKindIsOrderByMediaSeqAsc(mediaDate_0, mediaDate_1, mediaKind_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductMediaReposMon.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqualAndMediaKindIsOrderByMediaSeqAsc(mediaDate_0, mediaDate_1, mediaKind_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductMediaReposHist.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqualAndMediaKindIsOrderByMediaSeqAsc(mediaDate_0, mediaDate_1, mediaKind_2, pageable);
    else 
      slice = empDeductMediaRepos.findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqualAndMediaKindIsOrderByMediaSeqAsc(mediaDate_0, mediaDate_1, mediaKind_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<EmpDeductMedia> findL4520A(int acDate_0, int perfMonth_1, String flowCode_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductMedia> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findL4520A " + dbName + " : " + "acDate_0 : " + acDate_0 + " perfMonth_1 : " +  perfMonth_1 + " flowCode_2 : " +  flowCode_2);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductMediaReposDay.findAllByAcDateIsAndPerfMonthIsAndFlowCodeIsOrderByMediaSeqAsc(acDate_0, perfMonth_1, flowCode_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductMediaReposMon.findAllByAcDateIsAndPerfMonthIsAndFlowCodeIsOrderByMediaSeqAsc(acDate_0, perfMonth_1, flowCode_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductMediaReposHist.findAllByAcDateIsAndPerfMonthIsAndFlowCodeIsOrderByMediaSeqAsc(acDate_0, perfMonth_1, flowCode_2, pageable);
    else 
      slice = empDeductMediaRepos.findAllByAcDateIsAndPerfMonthIsAndFlowCodeIsOrderByMediaSeqAsc(acDate_0, perfMonth_1, flowCode_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public EmpDeductMedia lastMediaSeqFirst(int mediaDate_0, String mediaKind_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("lastMediaSeqFirst " + dbName + " : " + "mediaDate_0 : " + mediaDate_0 + " mediaKind_1 : " +  mediaKind_1);
    Optional<EmpDeductMedia> empDeductMediaT = null;
    if (dbName.equals(ContentName.onDay))
      empDeductMediaT = empDeductMediaReposDay.findTopByMediaDateIsAndMediaKindIsOrderByMediaSeqDesc(mediaDate_0, mediaKind_1);
    else if (dbName.equals(ContentName.onMon))
      empDeductMediaT = empDeductMediaReposMon.findTopByMediaDateIsAndMediaKindIsOrderByMediaSeqDesc(mediaDate_0, mediaKind_1);
    else if (dbName.equals(ContentName.onHist))
      empDeductMediaT = empDeductMediaReposHist.findTopByMediaDateIsAndMediaKindIsOrderByMediaSeqDesc(mediaDate_0, mediaKind_1);
    else 
      empDeductMediaT = empDeductMediaRepos.findTopByMediaDateIsAndMediaKindIsOrderByMediaSeqDesc(mediaDate_0, mediaKind_1);

    return empDeductMediaT.isPresent() ? empDeductMediaT.get() : null;
  }

  @Override
  public Slice<EmpDeductMedia> entryDateRng(int entryDate_0, int entryDate_1, String mediaKind_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<EmpDeductMedia> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("entryDateRng " + dbName + " : " + "entryDate_0 : " + entryDate_0 + " entryDate_1 : " +  entryDate_1 + " mediaKind_2 : " +  mediaKind_2);
    if (dbName.equals(ContentName.onDay))
      slice = empDeductMediaReposDay.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndMediaKindIsOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, mediaKind_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = empDeductMediaReposMon.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndMediaKindIsOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, mediaKind_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = empDeductMediaReposHist.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndMediaKindIsOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, mediaKind_2, pageable);
    else 
      slice = empDeductMediaRepos.findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndMediaKindIsOrderByEntryDateAscCustNoAsc(entryDate_0, entryDate_1, mediaKind_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public EmpDeductMedia holdById(EmpDeductMediaId empDeductMediaId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + empDeductMediaId);
    Optional<EmpDeductMedia> empDeductMedia = null;
    if (dbName.equals(ContentName.onDay))
      empDeductMedia = empDeductMediaReposDay.findByEmpDeductMediaId(empDeductMediaId);
    else if (dbName.equals(ContentName.onMon))
      empDeductMedia = empDeductMediaReposMon.findByEmpDeductMediaId(empDeductMediaId);
    else if (dbName.equals(ContentName.onHist))
      empDeductMedia = empDeductMediaReposHist.findByEmpDeductMediaId(empDeductMediaId);
    else 
      empDeductMedia = empDeductMediaRepos.findByEmpDeductMediaId(empDeductMediaId);
    return empDeductMedia.isPresent() ? empDeductMedia.get() : null;
  }

  @Override
  public EmpDeductMedia holdById(EmpDeductMedia empDeductMedia, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + empDeductMedia.getEmpDeductMediaId());
    Optional<EmpDeductMedia> empDeductMediaT = null;
    if (dbName.equals(ContentName.onDay))
      empDeductMediaT = empDeductMediaReposDay.findByEmpDeductMediaId(empDeductMedia.getEmpDeductMediaId());
    else if (dbName.equals(ContentName.onMon))
      empDeductMediaT = empDeductMediaReposMon.findByEmpDeductMediaId(empDeductMedia.getEmpDeductMediaId());
    else if (dbName.equals(ContentName.onHist))
      empDeductMediaT = empDeductMediaReposHist.findByEmpDeductMediaId(empDeductMedia.getEmpDeductMediaId());
    else 
      empDeductMediaT = empDeductMediaRepos.findByEmpDeductMediaId(empDeductMedia.getEmpDeductMediaId());
    return empDeductMediaT.isPresent() ? empDeductMediaT.get() : null;
  }

  @Override
  public EmpDeductMedia insert(EmpDeductMedia empDeductMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + empDeductMedia.getEmpDeductMediaId());
    if (this.findById(empDeductMedia.getEmpDeductMediaId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      empDeductMedia.setCreateEmpNo(empNot);

    if(empDeductMedia.getLastUpdateEmpNo() == null || empDeductMedia.getLastUpdateEmpNo().isEmpty())
      empDeductMedia.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return empDeductMediaReposDay.saveAndFlush(empDeductMedia);	
    else if (dbName.equals(ContentName.onMon))
      return empDeductMediaReposMon.saveAndFlush(empDeductMedia);
    else if (dbName.equals(ContentName.onHist))
      return empDeductMediaReposHist.saveAndFlush(empDeductMedia);
    else 
    return empDeductMediaRepos.saveAndFlush(empDeductMedia);
  }

  @Override
  public EmpDeductMedia update(EmpDeductMedia empDeductMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + empDeductMedia.getEmpDeductMediaId());
    if (!empNot.isEmpty())
      empDeductMedia.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return empDeductMediaReposDay.saveAndFlush(empDeductMedia);	
    else if (dbName.equals(ContentName.onMon))
      return empDeductMediaReposMon.saveAndFlush(empDeductMedia);
    else if (dbName.equals(ContentName.onHist))
      return empDeductMediaReposHist.saveAndFlush(empDeductMedia);
    else 
    return empDeductMediaRepos.saveAndFlush(empDeductMedia);
  }

  @Override
  public EmpDeductMedia update2(EmpDeductMedia empDeductMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + empDeductMedia.getEmpDeductMediaId());
    if (!empNot.isEmpty())
      empDeductMedia.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      empDeductMediaReposDay.saveAndFlush(empDeductMedia);	
    else if (dbName.equals(ContentName.onMon))
      empDeductMediaReposMon.saveAndFlush(empDeductMedia);
    else if (dbName.equals(ContentName.onHist))
        empDeductMediaReposHist.saveAndFlush(empDeductMedia);
    else 
      empDeductMediaRepos.saveAndFlush(empDeductMedia);	
    return this.findById(empDeductMedia.getEmpDeductMediaId());
  }

  @Override
  public void delete(EmpDeductMedia empDeductMedia, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + empDeductMedia.getEmpDeductMediaId());
    if (dbName.equals(ContentName.onDay)) {
      empDeductMediaReposDay.delete(empDeductMedia);	
      empDeductMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductMediaReposMon.delete(empDeductMedia);	
      empDeductMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductMediaReposHist.delete(empDeductMedia);
      empDeductMediaReposHist.flush();
    }
    else {
      empDeductMediaRepos.delete(empDeductMedia);
      empDeductMediaRepos.flush();
    }
   }

  @Override
  public void insertAll(List<EmpDeductMedia> empDeductMedia, TitaVo... titaVo) throws DBException {
    if (empDeductMedia == null || empDeductMedia.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (EmpDeductMedia t : empDeductMedia){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      empDeductMedia = empDeductMediaReposDay.saveAll(empDeductMedia);	
      empDeductMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductMedia = empDeductMediaReposMon.saveAll(empDeductMedia);	
      empDeductMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductMedia = empDeductMediaReposHist.saveAll(empDeductMedia);
      empDeductMediaReposHist.flush();
    }
    else {
      empDeductMedia = empDeductMediaRepos.saveAll(empDeductMedia);
      empDeductMediaRepos.flush();
    }
    }

  @Override
  public void updateAll(List<EmpDeductMedia> empDeductMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (empDeductMedia == null || empDeductMedia.size() == 0)
      throw new DBException(6);

    for (EmpDeductMedia t : empDeductMedia) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      empDeductMedia = empDeductMediaReposDay.saveAll(empDeductMedia);	
      empDeductMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductMedia = empDeductMediaReposMon.saveAll(empDeductMedia);	
      empDeductMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductMedia = empDeductMediaReposHist.saveAll(empDeductMedia);
      empDeductMediaReposHist.flush();
    }
    else {
      empDeductMedia = empDeductMediaRepos.saveAll(empDeductMedia);
      empDeductMediaRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<EmpDeductMedia> empDeductMedia, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (empDeductMedia == null || empDeductMedia.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      empDeductMediaReposDay.deleteAll(empDeductMedia);	
      empDeductMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      empDeductMediaReposMon.deleteAll(empDeductMedia);	
      empDeductMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      empDeductMediaReposHist.deleteAll(empDeductMedia);
      empDeductMediaReposHist.flush();
    }
    else {
      empDeductMediaRepos.deleteAll(empDeductMedia);
      empDeductMediaRepos.flush();
    }
  }

}
