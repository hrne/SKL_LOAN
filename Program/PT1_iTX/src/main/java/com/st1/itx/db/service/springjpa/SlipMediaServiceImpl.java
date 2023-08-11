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
import com.st1.itx.db.domain.SlipMedia;
import com.st1.itx.db.domain.SlipMediaId;
import com.st1.itx.db.repository.online.SlipMediaRepository;
import com.st1.itx.db.repository.day.SlipMediaRepositoryDay;
import com.st1.itx.db.repository.mon.SlipMediaRepositoryMon;
import com.st1.itx.db.repository.hist.SlipMediaRepositoryHist;
import com.st1.itx.db.service.SlipMediaService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("slipMediaService")
@Repository
public class SlipMediaServiceImpl extends ASpringJpaParm implements SlipMediaService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private SlipMediaRepository slipMediaRepos;

  @Autowired
  private SlipMediaRepositoryDay slipMediaReposDay;

  @Autowired
  private SlipMediaRepositoryMon slipMediaReposMon;

  @Autowired
  private SlipMediaRepositoryHist slipMediaReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(slipMediaRepos);
    org.junit.Assert.assertNotNull(slipMediaReposDay);
    org.junit.Assert.assertNotNull(slipMediaReposMon);
    org.junit.Assert.assertNotNull(slipMediaReposHist);
  }

  @Override
  public SlipMedia findById(SlipMediaId slipMediaId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + slipMediaId);
    Optional<SlipMedia> slipMedia = null;
    if (dbName.equals(ContentName.onDay))
      slipMedia = slipMediaReposDay.findById(slipMediaId);
    else if (dbName.equals(ContentName.onMon))
      slipMedia = slipMediaReposMon.findById(slipMediaId);
    else if (dbName.equals(ContentName.onHist))
      slipMedia = slipMediaReposHist.findById(slipMediaId);
    else 
      slipMedia = slipMediaRepos.findById(slipMediaId);
    SlipMedia obj = slipMedia.isPresent() ? slipMedia.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<SlipMedia> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<SlipMedia> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "BranchNo", "AcDate", "BatchNo", "AcBookCode", "MediaSeq", "MediaSlipNo", "Seq"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "BranchNo", "AcDate", "BatchNo", "AcBookCode", "MediaSeq", "MediaSlipNo", "Seq"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = slipMediaReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = slipMediaReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = slipMediaReposHist.findAll(pageable);
    else 
      slice = slipMediaRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<SlipMedia> findMediaSeq(int acDate_0, int batchNo_1, int mediaSeq_2, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<SlipMedia> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findMediaSeq " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1 + " mediaSeq_2 : " +  mediaSeq_2);
    if (dbName.equals(ContentName.onDay))
      slice = slipMediaReposDay.findAllByAcDateIsAndBatchNoIsAndMediaSeqIsOrderByAcDateAscBatchNoAscMediaSeqAscMediaSlipNoAscAcBookCodeAscSeqAsc(acDate_0, batchNo_1, mediaSeq_2, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = slipMediaReposMon.findAllByAcDateIsAndBatchNoIsAndMediaSeqIsOrderByAcDateAscBatchNoAscMediaSeqAscMediaSlipNoAscAcBookCodeAscSeqAsc(acDate_0, batchNo_1, mediaSeq_2, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = slipMediaReposHist.findAllByAcDateIsAndBatchNoIsAndMediaSeqIsOrderByAcDateAscBatchNoAscMediaSeqAscMediaSlipNoAscAcBookCodeAscSeqAsc(acDate_0, batchNo_1, mediaSeq_2, pageable);
    else 
      slice = slipMediaRepos.findAllByAcDateIsAndBatchNoIsAndMediaSeqIsOrderByAcDateAscBatchNoAscMediaSeqAscMediaSlipNoAscAcBookCodeAscSeqAsc(acDate_0, batchNo_1, mediaSeq_2, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<SlipMedia> findBatchNo(int acDate_0, int batchNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<SlipMedia> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findBatchNo " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = slipMediaReposDay.findAllByAcDateIsAndBatchNoIsOrderByAcDateAscBatchNoAscMediaSeqAscMediaSlipNoAscAcBookCodeAscSeqAsc(acDate_0, batchNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = slipMediaReposMon.findAllByAcDateIsAndBatchNoIsOrderByAcDateAscBatchNoAscMediaSeqAscMediaSlipNoAscAcBookCodeAscSeqAsc(acDate_0, batchNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = slipMediaReposHist.findAllByAcDateIsAndBatchNoIsOrderByAcDateAscBatchNoAscMediaSeqAscMediaSlipNoAscAcBookCodeAscSeqAsc(acDate_0, batchNo_1, pageable);
    else 
      slice = slipMediaRepos.findAllByAcDateIsAndBatchNoIsOrderByAcDateAscBatchNoAscMediaSeqAscMediaSlipNoAscAcBookCodeAscSeqAsc(acDate_0, batchNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public SlipMedia findMediaSeqFirst(int acDate_0, int batchNo_1, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findMediaSeqFirst " + dbName + " : " + "acDate_0 : " + acDate_0 + " batchNo_1 : " +  batchNo_1);
    Optional<SlipMedia> slipMediaT = null;
    if (dbName.equals(ContentName.onDay))
      slipMediaT = slipMediaReposDay.findTopByAcDateIsAndBatchNoIsOrderByAcDateAscBatchNoAscMediaSeqDesc(acDate_0, batchNo_1);
    else if (dbName.equals(ContentName.onMon))
      slipMediaT = slipMediaReposMon.findTopByAcDateIsAndBatchNoIsOrderByAcDateAscBatchNoAscMediaSeqDesc(acDate_0, batchNo_1);
    else if (dbName.equals(ContentName.onHist))
      slipMediaT = slipMediaReposHist.findTopByAcDateIsAndBatchNoIsOrderByAcDateAscBatchNoAscMediaSeqDesc(acDate_0, batchNo_1);
    else 
      slipMediaT = slipMediaRepos.findTopByAcDateIsAndBatchNoIsOrderByAcDateAscBatchNoAscMediaSeqDesc(acDate_0, batchNo_1);

    return slipMediaT.isPresent() ? slipMediaT.get() : null;
  }

  @Override
  public SlipMedia holdById(SlipMediaId slipMediaId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + slipMediaId);
    Optional<SlipMedia> slipMedia = null;
    if (dbName.equals(ContentName.onDay))
      slipMedia = slipMediaReposDay.findBySlipMediaId(slipMediaId);
    else if (dbName.equals(ContentName.onMon))
      slipMedia = slipMediaReposMon.findBySlipMediaId(slipMediaId);
    else if (dbName.equals(ContentName.onHist))
      slipMedia = slipMediaReposHist.findBySlipMediaId(slipMediaId);
    else 
      slipMedia = slipMediaRepos.findBySlipMediaId(slipMediaId);
    return slipMedia.isPresent() ? slipMedia.get() : null;
  }

  @Override
  public SlipMedia holdById(SlipMedia slipMedia, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + slipMedia.getSlipMediaId());
    Optional<SlipMedia> slipMediaT = null;
    if (dbName.equals(ContentName.onDay))
      slipMediaT = slipMediaReposDay.findBySlipMediaId(slipMedia.getSlipMediaId());
    else if (dbName.equals(ContentName.onMon))
      slipMediaT = slipMediaReposMon.findBySlipMediaId(slipMedia.getSlipMediaId());
    else if (dbName.equals(ContentName.onHist))
      slipMediaT = slipMediaReposHist.findBySlipMediaId(slipMedia.getSlipMediaId());
    else 
      slipMediaT = slipMediaRepos.findBySlipMediaId(slipMedia.getSlipMediaId());
    return slipMediaT.isPresent() ? slipMediaT.get() : null;
  }

  @Override
  public SlipMedia insert(SlipMedia slipMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + slipMedia.getSlipMediaId());
    if (this.findById(slipMedia.getSlipMediaId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      slipMedia.setCreateEmpNo(empNot);

    if(slipMedia.getLastUpdateEmpNo() == null || slipMedia.getLastUpdateEmpNo().isEmpty())
      slipMedia.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return slipMediaReposDay.saveAndFlush(slipMedia);	
    else if (dbName.equals(ContentName.onMon))
      return slipMediaReposMon.saveAndFlush(slipMedia);
    else if (dbName.equals(ContentName.onHist))
      return slipMediaReposHist.saveAndFlush(slipMedia);
    else 
    return slipMediaRepos.saveAndFlush(slipMedia);
  }

  @Override
  public SlipMedia update(SlipMedia slipMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + slipMedia.getSlipMediaId());
    if (!empNot.isEmpty())
      slipMedia.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return slipMediaReposDay.saveAndFlush(slipMedia);	
    else if (dbName.equals(ContentName.onMon))
      return slipMediaReposMon.saveAndFlush(slipMedia);
    else if (dbName.equals(ContentName.onHist))
      return slipMediaReposHist.saveAndFlush(slipMedia);
    else 
    return slipMediaRepos.saveAndFlush(slipMedia);
  }

  @Override
  public SlipMedia update2(SlipMedia slipMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + slipMedia.getSlipMediaId());
    if (!empNot.isEmpty())
      slipMedia.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      slipMediaReposDay.saveAndFlush(slipMedia);	
    else if (dbName.equals(ContentName.onMon))
      slipMediaReposMon.saveAndFlush(slipMedia);
    else if (dbName.equals(ContentName.onHist))
        slipMediaReposHist.saveAndFlush(slipMedia);
    else 
      slipMediaRepos.saveAndFlush(slipMedia);	
    return this.findById(slipMedia.getSlipMediaId());
  }

  @Override
  public void delete(SlipMedia slipMedia, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + slipMedia.getSlipMediaId());
    if (dbName.equals(ContentName.onDay)) {
      slipMediaReposDay.delete(slipMedia);	
      slipMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      slipMediaReposMon.delete(slipMedia);	
      slipMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      slipMediaReposHist.delete(slipMedia);
      slipMediaReposHist.flush();
    }
    else {
      slipMediaRepos.delete(slipMedia);
      slipMediaRepos.flush();
    }
   }

  @Override
  public void insertAll(List<SlipMedia> slipMedia, TitaVo... titaVo) throws DBException {
    if (slipMedia == null || slipMedia.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (SlipMedia t : slipMedia){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      slipMedia = slipMediaReposDay.saveAll(slipMedia);	
      slipMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      slipMedia = slipMediaReposMon.saveAll(slipMedia);	
      slipMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      slipMedia = slipMediaReposHist.saveAll(slipMedia);
      slipMediaReposHist.flush();
    }
    else {
      slipMedia = slipMediaRepos.saveAll(slipMedia);
      slipMediaRepos.flush();
    }
    }

  @Override
  public void updateAll(List<SlipMedia> slipMedia, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (slipMedia == null || slipMedia.size() == 0)
      throw new DBException(6);

    for (SlipMedia t : slipMedia) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      slipMedia = slipMediaReposDay.saveAll(slipMedia);	
      slipMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      slipMedia = slipMediaReposMon.saveAll(slipMedia);	
      slipMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      slipMedia = slipMediaReposHist.saveAll(slipMedia);
      slipMediaReposHist.flush();
    }
    else {
      slipMedia = slipMediaRepos.saveAll(slipMedia);
      slipMediaRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<SlipMedia> slipMedia, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (slipMedia == null || slipMedia.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      slipMediaReposDay.deleteAll(slipMedia);	
      slipMediaReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      slipMediaReposMon.deleteAll(slipMedia);	
      slipMediaReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      slipMediaReposHist.deleteAll(slipMedia);
      slipMediaReposHist.flush();
    }
    else {
      slipMediaRepos.deleteAll(slipMedia);
      slipMediaRepos.flush();
    }
  }

}
