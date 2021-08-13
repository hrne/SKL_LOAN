package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

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
import com.st1.itx.db.domain.AchDeductMedia;
import com.st1.itx.db.domain.AchDeductMediaId;
import com.st1.itx.db.repository.online.AchDeductMediaRepository;
import com.st1.itx.db.repository.day.AchDeductMediaRepositoryDay;
import com.st1.itx.db.repository.mon.AchDeductMediaRepositoryMon;
import com.st1.itx.db.repository.hist.AchDeductMediaRepositoryHist;
import com.st1.itx.db.service.AchDeductMediaService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("achDeductMediaService")
@Repository
public class AchDeductMediaServiceImpl implements AchDeductMediaService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(AchDeductMediaServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AchDeductMediaRepository achDeductMediaRepos;

  @Autowired
  private AchDeductMediaRepositoryDay achDeductMediaReposDay;

  @Autowired
  private AchDeductMediaRepositoryMon achDeductMediaReposMon;

  @Autowired
  private AchDeductMediaRepositoryHist achDeductMediaReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(achDeductMediaRepos);
    org.junit.Assert.assertNotNull(achDeductMediaReposDay);
    org.junit.Assert.assertNotNull(achDeductMediaReposMon);
    org.junit.Assert.assertNotNull(achDeductMediaReposHist);
  }

  @Override
  public AchDeductMedia findById(AchDeductMediaId achDeductMediaId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + achDeductMediaId);
    Optional<AchDeductMedia> achDeductMedia = null;
    if (dbName.equals(ContentName.onDay))
      achDeductMedia = achDeductMediaReposDay.findById(achDeductMediaId);
    else if (dbName.equals(ContentName.onMon))
      achDeductMedia = achDeductMediaReposMon.findById(achDeductMediaId);
    else if (dbName.equals(ContentName.onHist))
      achDeductMedia = achDeductMediaReposHist.findById(achDeductMediaId);
    else 
      achDeductMedia = achDeductMediaRepos.findById(achDeductMediaId);
    AchDeductMedia obj = achDeductMedia.isPresent() ? achDeductMedia.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AchDeductMedia> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchDeductMedia> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "MediaDate", "MediaKind", "MediaSeq"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = achDeductMediaReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achDeductMediaReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achDeductMediaReposHist.findAll(pageable);
    else 
      slice = achDeductMediaRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AchDeductMedia detailSeqFirst(int acDate_0, String batchNo_1, int detailSeq_2, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("detailSeqFirst " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1 + " detailSeq_2 : " +  detailSeq_2);
    Optional<AchDeductMedia> achDeductMediaT = null;
    if (dbName.equals(ContentName.onDay))
      achDeductMediaT = achDeductMediaReposDay.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);
    else if (dbName.equals(ContentName.onMon))
      achDeductMediaT = achDeductMediaReposMon.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);
    else if (dbName.equals(ContentName.onHist))
      achDeductMediaT = achDeductMediaReposHist.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);
    else 
      achDeductMediaT = achDeductMediaRepos.findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(acDate_0, batchNo_1, detailSeq_2);
    return achDeductMediaT.isPresent() ? achDeductMediaT.get() : null;
  }

  @Override
  public AchDeductMedia reseiveCheckFirst(int custNo_0, int facmNo_1, String achRepayCode_2, int prevIntDate_3, BigDecimal repayAmt_4, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("reseiveCheckFirst " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1 + " achRepayCode_2 : " +  achRepayCode_2 + " prevIntDate_3 : " +  prevIntDate_3 + " repayAmt_4 : " +  repayAmt_4);
    Optional<AchDeductMedia> achDeductMediaT = null;
    if (dbName.equals(ContentName.onDay))
      achDeductMediaT = achDeductMediaReposDay.findTopByCustNoIsAndFacmNoIsAndAchRepayCodeIsAndPrevIntDateIsAndRepayAmtIsOrderByMediaDateDesc(custNo_0, facmNo_1, achRepayCode_2, prevIntDate_3, repayAmt_4);
    else if (dbName.equals(ContentName.onMon))
      achDeductMediaT = achDeductMediaReposMon.findTopByCustNoIsAndFacmNoIsAndAchRepayCodeIsAndPrevIntDateIsAndRepayAmtIsOrderByMediaDateDesc(custNo_0, facmNo_1, achRepayCode_2, prevIntDate_3, repayAmt_4);
    else if (dbName.equals(ContentName.onHist))
      achDeductMediaT = achDeductMediaReposHist.findTopByCustNoIsAndFacmNoIsAndAchRepayCodeIsAndPrevIntDateIsAndRepayAmtIsOrderByMediaDateDesc(custNo_0, facmNo_1, achRepayCode_2, prevIntDate_3, repayAmt_4);
    else 
      achDeductMediaT = achDeductMediaRepos.findTopByCustNoIsAndFacmNoIsAndAchRepayCodeIsAndPrevIntDateIsAndRepayAmtIsOrderByMediaDateDesc(custNo_0, facmNo_1, achRepayCode_2, prevIntDate_3, repayAmt_4);
    return achDeductMediaT.isPresent() ? achDeductMediaT.get() : null;
  }

  @Override
  public Slice<AchDeductMedia> mediaDateEq(int mediaDate_0, String mediaKind_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchDeductMedia> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("mediaDateEq " + dbName + " : " + "mediaDate_0 : " + mediaDate_0 + " mediaKind_1 : " +  mediaKind_1);
    if (dbName.equals(ContentName.onDay))
      slice = achDeductMediaReposDay.findAllByMediaDateIsAndMediaKindIsOrderByMediaSeqAsc(mediaDate_0, mediaKind_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achDeductMediaReposMon.findAllByMediaDateIsAndMediaKindIsOrderByMediaSeqAsc(mediaDate_0, mediaKind_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achDeductMediaReposHist.findAllByMediaDateIsAndMediaKindIsOrderByMediaSeqAsc(mediaDate_0, mediaKind_1, pageable);
    else 
      slice = achDeductMediaRepos.findAllByMediaDateIsAndMediaKindIsOrderByMediaSeqAsc(mediaDate_0, mediaKind_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AchDeductMedia holdById(AchDeductMediaId achDeductMediaId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + achDeductMediaId);
    Optional<AchDeductMedia> achDeductMedia = null;
    if (dbName.equals(ContentName.onDay))
      achDeductMedia = achDeductMediaReposDay.findByAchDeductMediaId(achDeductMediaId);
    else if (dbName.equals(ContentName.onMon))
      achDeductMedia = achDeductMediaReposMon.findByAchDeductMediaId(achDeductMediaId);
    else if (dbName.equals(ContentName.onHist))
      achDeductMedia = achDeductMediaReposHist.findByAchDeductMediaId(achDeductMediaId);
    else 
      achDeductMedia = achDeductMediaRepos.findByAchDeductMediaId(achDeductMediaId);
    return achDeductMedia.isPresent() ? achDeductMedia.get() : null;
  }

  @Override
  public AchDeductMedia holdById(AchDeductMedia achDeductMedia, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + achDeductMedia.getAchDeductMediaId());
    Optional<AchDeductMedia> achDeductMediaT = null;
    if (dbName.equals(ContentName.onDay))
      achDeductMediaT = achDeductMediaReposDay.findByAchDeductMediaId(achDeductMedia.getAchDeductMediaId());
    else if (dbName.equals(ContentName.onMon))
      achDeductMediaT = achDeductMediaReposMon.findByAchDeductMediaId(achDeductMedia.getAchDeductMediaId());
    else if (dbName.equals(ContentName.onHist))
      achDeductMediaT = achDeductMediaReposHist.findByAchDeductMediaId(achDeductMedia.getAchDeductMediaId());
    else 
      achDeductMediaT = achDeductMediaRepos.findByAchDeductMediaId(achDeductMedia.getAchDeductMediaId());
    return achDeductMediaT.isPresent() ? achDeductMediaT.get() : null;
  }

  @Override
  public AchDeductMedia insert(AchDeductMedia achDeductMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + achDeductMedia.getAchDeductMediaId());
    if (this.findById(achDeductMedia.getAchDeductMediaId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      achDeductMedia.setCreateEmpNo(empNot);

    if(achDeductMedia.getLastUpdateEmpNo() == null || achDeductMedia.getLastUpdateEmpNo().isEmpty())
      achDeductMedia.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return achDeductMediaReposDay.saveAndFlush(achDeductMedia);	
    else if (dbName.equals(ContentName.onMon))
      return achDeductMediaReposMon.saveAndFlush(achDeductMedia);
    else if (dbName.equals(ContentName.onHist))
      return achDeductMediaReposHist.saveAndFlush(achDeductMedia);
    else 
    return achDeductMediaRepos.saveAndFlush(achDeductMedia);
  }

  @Override
  public AchDeductMedia update(AchDeductMedia achDeductMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + achDeductMedia.getAchDeductMediaId());
    if (!empNot.isEmpty())
      achDeductMedia.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return achDeductMediaReposDay.saveAndFlush(achDeductMedia);	
    else if (dbName.equals(ContentName.onMon))
      return achDeductMediaReposMon.saveAndFlush(achDeductMedia);
    else if (dbName.equals(ContentName.onHist))
      return achDeductMediaReposHist.saveAndFlush(achDeductMedia);
    else 
    return achDeductMediaRepos.saveAndFlush(achDeductMedia);
  }

  @Override
  public AchDeductMedia update2(AchDeductMedia achDeductMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + achDeductMedia.getAchDeductMediaId());
    if (!empNot.isEmpty())
      achDeductMedia.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      achDeductMediaReposDay.saveAndFlush(achDeductMedia);	
    else if (dbName.equals(ContentName.onMon))
      achDeductMediaReposMon.saveAndFlush(achDeductMedia);
    else if (dbName.equals(ContentName.onHist))
        achDeductMediaReposHist.saveAndFlush(achDeductMedia);
    else 
      achDeductMediaRepos.saveAndFlush(achDeductMedia);	
    return this.findById(achDeductMedia.getAchDeductMediaId());
  }

  @Override
  public void delete(AchDeductMedia achDeductMedia, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + achDeductMedia.getAchDeductMediaId());
    if (dbName.equals(ContentName.onDay)) {
      achDeductMediaReposDay.delete(achDeductMedia);	
      achDeductMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achDeductMediaReposMon.delete(achDeductMedia);	
      achDeductMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achDeductMediaReposHist.delete(achDeductMedia);
      achDeductMediaReposHist.flush();
    }
    else {
      achDeductMediaRepos.delete(achDeductMedia);
      achDeductMediaRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AchDeductMedia> achDeductMedia, TitaVo... titaVo) throws DBException {
    if (achDeductMedia == null || achDeductMedia.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (AchDeductMedia t : achDeductMedia){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      achDeductMedia = achDeductMediaReposDay.saveAll(achDeductMedia);	
      achDeductMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achDeductMedia = achDeductMediaReposMon.saveAll(achDeductMedia);	
      achDeductMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achDeductMedia = achDeductMediaReposHist.saveAll(achDeductMedia);
      achDeductMediaReposHist.flush();
    }
    else {
      achDeductMedia = achDeductMediaRepos.saveAll(achDeductMedia);
      achDeductMediaRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AchDeductMedia> achDeductMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (achDeductMedia == null || achDeductMedia.size() == 0)
      throw new DBException(6);

    for (AchDeductMedia t : achDeductMedia) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      achDeductMedia = achDeductMediaReposDay.saveAll(achDeductMedia);	
      achDeductMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achDeductMedia = achDeductMediaReposMon.saveAll(achDeductMedia);	
      achDeductMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achDeductMedia = achDeductMediaReposHist.saveAll(achDeductMedia);
      achDeductMediaReposHist.flush();
    }
    else {
      achDeductMedia = achDeductMediaRepos.saveAll(achDeductMedia);
      achDeductMediaRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AchDeductMedia> achDeductMedia, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (achDeductMedia == null || achDeductMedia.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      achDeductMediaReposDay.deleteAll(achDeductMedia);	
      achDeductMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achDeductMediaReposMon.deleteAll(achDeductMedia);	
      achDeductMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achDeductMediaReposHist.deleteAll(achDeductMedia);
      achDeductMediaReposHist.flush();
    }
    else {
      achDeductMediaRepos.deleteAll(achDeductMedia);
      achDeductMediaRepos.flush();
    }
  }

}
