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
import com.st1.itx.db.domain.SlipEbsRecord;
import com.st1.itx.db.repository.online.SlipEbsRecordRepository;
import com.st1.itx.db.repository.day.SlipEbsRecordRepositoryDay;
import com.st1.itx.db.repository.mon.SlipEbsRecordRepositoryMon;
import com.st1.itx.db.repository.hist.SlipEbsRecordRepositoryHist;
import com.st1.itx.db.service.SlipEbsRecordService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("slipEbsRecordService")
@Repository
public class SlipEbsRecordServiceImpl extends ASpringJpaParm implements SlipEbsRecordService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private SlipEbsRecordRepository slipEbsRecordRepos;

  @Autowired
  private SlipEbsRecordRepositoryDay slipEbsRecordReposDay;

  @Autowired
  private SlipEbsRecordRepositoryMon slipEbsRecordReposMon;

  @Autowired
  private SlipEbsRecordRepositoryHist slipEbsRecordReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(slipEbsRecordRepos);
    org.junit.Assert.assertNotNull(slipEbsRecordReposDay);
    org.junit.Assert.assertNotNull(slipEbsRecordReposMon);
    org.junit.Assert.assertNotNull(slipEbsRecordReposHist);
  }

  @Override
  public SlipEbsRecord findById(Long uploadNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + uploadNo);
    Optional<SlipEbsRecord> slipEbsRecord = null;
    if (dbName.equals(ContentName.onDay))
      slipEbsRecord = slipEbsRecordReposDay.findById(uploadNo);
    else if (dbName.equals(ContentName.onMon))
      slipEbsRecord = slipEbsRecordReposMon.findById(uploadNo);
    else if (dbName.equals(ContentName.onHist))
      slipEbsRecord = slipEbsRecordReposHist.findById(uploadNo);
    else 
      slipEbsRecord = slipEbsRecordRepos.findById(uploadNo);
    SlipEbsRecord obj = slipEbsRecord.isPresent() ? slipEbsRecord.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<SlipEbsRecord> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<SlipEbsRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "UploadNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "UploadNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = slipEbsRecordReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = slipEbsRecordReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = slipEbsRecordReposHist.findAll(pageable);
    else 
      slice = slipEbsRecordRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public SlipEbsRecord holdById(Long uploadNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + uploadNo);
    Optional<SlipEbsRecord> slipEbsRecord = null;
    if (dbName.equals(ContentName.onDay))
      slipEbsRecord = slipEbsRecordReposDay.findByUploadNo(uploadNo);
    else if (dbName.equals(ContentName.onMon))
      slipEbsRecord = slipEbsRecordReposMon.findByUploadNo(uploadNo);
    else if (dbName.equals(ContentName.onHist))
      slipEbsRecord = slipEbsRecordReposHist.findByUploadNo(uploadNo);
    else 
      slipEbsRecord = slipEbsRecordRepos.findByUploadNo(uploadNo);
    return slipEbsRecord.isPresent() ? slipEbsRecord.get() : null;
  }

  @Override
  public SlipEbsRecord holdById(SlipEbsRecord slipEbsRecord, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + slipEbsRecord.getUploadNo());
    Optional<SlipEbsRecord> slipEbsRecordT = null;
    if (dbName.equals(ContentName.onDay))
      slipEbsRecordT = slipEbsRecordReposDay.findByUploadNo(slipEbsRecord.getUploadNo());
    else if (dbName.equals(ContentName.onMon))
      slipEbsRecordT = slipEbsRecordReposMon.findByUploadNo(slipEbsRecord.getUploadNo());
    else if (dbName.equals(ContentName.onHist))
      slipEbsRecordT = slipEbsRecordReposHist.findByUploadNo(slipEbsRecord.getUploadNo());
    else 
      slipEbsRecordT = slipEbsRecordRepos.findByUploadNo(slipEbsRecord.getUploadNo());
    return slipEbsRecordT.isPresent() ? slipEbsRecordT.get() : null;
  }

  @Override
  public SlipEbsRecord insert(SlipEbsRecord slipEbsRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + slipEbsRecord.getUploadNo());
    if (this.findById(slipEbsRecord.getUploadNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      slipEbsRecord.setCreateEmpNo(empNot);

    if(slipEbsRecord.getLastUpdateEmpNo() == null || slipEbsRecord.getLastUpdateEmpNo().isEmpty())
      slipEbsRecord.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return slipEbsRecordReposDay.saveAndFlush(slipEbsRecord);	
    else if (dbName.equals(ContentName.onMon))
      return slipEbsRecordReposMon.saveAndFlush(slipEbsRecord);
    else if (dbName.equals(ContentName.onHist))
      return slipEbsRecordReposHist.saveAndFlush(slipEbsRecord);
    else 
    return slipEbsRecordRepos.saveAndFlush(slipEbsRecord);
  }

  @Override
  public SlipEbsRecord update(SlipEbsRecord slipEbsRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + slipEbsRecord.getUploadNo());
    if (!empNot.isEmpty())
      slipEbsRecord.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return slipEbsRecordReposDay.saveAndFlush(slipEbsRecord);	
    else if (dbName.equals(ContentName.onMon))
      return slipEbsRecordReposMon.saveAndFlush(slipEbsRecord);
    else if (dbName.equals(ContentName.onHist))
      return slipEbsRecordReposHist.saveAndFlush(slipEbsRecord);
    else 
    return slipEbsRecordRepos.saveAndFlush(slipEbsRecord);
  }

  @Override
  public SlipEbsRecord update2(SlipEbsRecord slipEbsRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + slipEbsRecord.getUploadNo());
    if (!empNot.isEmpty())
      slipEbsRecord.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      slipEbsRecordReposDay.saveAndFlush(slipEbsRecord);	
    else if (dbName.equals(ContentName.onMon))
      slipEbsRecordReposMon.saveAndFlush(slipEbsRecord);
    else if (dbName.equals(ContentName.onHist))
        slipEbsRecordReposHist.saveAndFlush(slipEbsRecord);
    else 
      slipEbsRecordRepos.saveAndFlush(slipEbsRecord);	
    return this.findById(slipEbsRecord.getUploadNo());
  }

  @Override
  public void delete(SlipEbsRecord slipEbsRecord, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + slipEbsRecord.getUploadNo());
    if (dbName.equals(ContentName.onDay)) {
      slipEbsRecordReposDay.delete(slipEbsRecord);	
      slipEbsRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      slipEbsRecordReposMon.delete(slipEbsRecord);	
      slipEbsRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      slipEbsRecordReposHist.delete(slipEbsRecord);
      slipEbsRecordReposHist.flush();
    }
    else {
      slipEbsRecordRepos.delete(slipEbsRecord);
      slipEbsRecordRepos.flush();
    }
   }

  @Override
  public void insertAll(List<SlipEbsRecord> slipEbsRecord, TitaVo... titaVo) throws DBException {
    if (slipEbsRecord == null || slipEbsRecord.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (SlipEbsRecord t : slipEbsRecord){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      slipEbsRecord = slipEbsRecordReposDay.saveAll(slipEbsRecord);	
      slipEbsRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      slipEbsRecord = slipEbsRecordReposMon.saveAll(slipEbsRecord);	
      slipEbsRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      slipEbsRecord = slipEbsRecordReposHist.saveAll(slipEbsRecord);
      slipEbsRecordReposHist.flush();
    }
    else {
      slipEbsRecord = slipEbsRecordRepos.saveAll(slipEbsRecord);
      slipEbsRecordRepos.flush();
    }
    }

  @Override
  public void updateAll(List<SlipEbsRecord> slipEbsRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (slipEbsRecord == null || slipEbsRecord.size() == 0)
      throw new DBException(6);

    for (SlipEbsRecord t : slipEbsRecord) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      slipEbsRecord = slipEbsRecordReposDay.saveAll(slipEbsRecord);	
      slipEbsRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      slipEbsRecord = slipEbsRecordReposMon.saveAll(slipEbsRecord);	
      slipEbsRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      slipEbsRecord = slipEbsRecordReposHist.saveAll(slipEbsRecord);
      slipEbsRecordReposHist.flush();
    }
    else {
      slipEbsRecord = slipEbsRecordRepos.saveAll(slipEbsRecord);
      slipEbsRecordRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<SlipEbsRecord> slipEbsRecord, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (slipEbsRecord == null || slipEbsRecord.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      slipEbsRecordReposDay.deleteAll(slipEbsRecord);	
      slipEbsRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      slipEbsRecordReposMon.deleteAll(slipEbsRecord);	
      slipEbsRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      slipEbsRecordReposHist.deleteAll(slipEbsRecord);
      slipEbsRecordReposHist.flush();
    }
    else {
      slipEbsRecordRepos.deleteAll(slipEbsRecord);
      slipEbsRecordRepos.flush();
    }
  }

}
