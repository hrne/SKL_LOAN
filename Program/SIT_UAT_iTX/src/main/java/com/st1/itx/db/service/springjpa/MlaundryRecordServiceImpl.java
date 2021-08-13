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
import com.st1.itx.db.domain.MlaundryRecord;
import com.st1.itx.db.domain.MlaundryRecordId;
import com.st1.itx.db.repository.online.MlaundryRecordRepository;
import com.st1.itx.db.repository.day.MlaundryRecordRepositoryDay;
import com.st1.itx.db.repository.mon.MlaundryRecordRepositoryMon;
import com.st1.itx.db.repository.hist.MlaundryRecordRepositoryHist;
import com.st1.itx.db.service.MlaundryRecordService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("mlaundryRecordService")
@Repository
public class MlaundryRecordServiceImpl implements MlaundryRecordService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(MlaundryRecordServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private MlaundryRecordRepository mlaundryRecordRepos;

  @Autowired
  private MlaundryRecordRepositoryDay mlaundryRecordReposDay;

  @Autowired
  private MlaundryRecordRepositoryMon mlaundryRecordReposMon;

  @Autowired
  private MlaundryRecordRepositoryHist mlaundryRecordReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(mlaundryRecordRepos);
    org.junit.Assert.assertNotNull(mlaundryRecordReposDay);
    org.junit.Assert.assertNotNull(mlaundryRecordReposMon);
    org.junit.Assert.assertNotNull(mlaundryRecordReposHist);
  }

  @Override
  public MlaundryRecord findById(MlaundryRecordId mlaundryRecordId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + mlaundryRecordId);
    Optional<MlaundryRecord> mlaundryRecord = null;
    if (dbName.equals(ContentName.onDay))
      mlaundryRecord = mlaundryRecordReposDay.findById(mlaundryRecordId);
    else if (dbName.equals(ContentName.onMon))
      mlaundryRecord = mlaundryRecordReposMon.findById(mlaundryRecordId);
    else if (dbName.equals(ContentName.onHist))
      mlaundryRecord = mlaundryRecordReposHist.findById(mlaundryRecordId);
    else 
      mlaundryRecord = mlaundryRecordRepos.findById(mlaundryRecordId);
    MlaundryRecord obj = mlaundryRecord.isPresent() ? mlaundryRecord.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<MlaundryRecord> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MlaundryRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "RecordDate", "CustNo", "FacmNo", "BormNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = mlaundryRecordReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = mlaundryRecordReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = mlaundryRecordReposHist.findAll(pageable);
    else 
      slice = mlaundryRecordRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<MlaundryRecord> findRecordDate(int recordDate_0, int recordDate_1, int actualRepayDate_2, int actualRepayDate_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MlaundryRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findRecordDate " + dbName + " : " + "recordDate_0 : " + recordDate_0 + " recordDate_1 : " +  recordDate_1 + " actualRepayDate_2 : " +  actualRepayDate_2 + " actualRepayDate_3 : " +  actualRepayDate_3);
    if (dbName.equals(ContentName.onDay))
      slice = mlaundryRecordReposDay.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0, recordDate_1, actualRepayDate_2, actualRepayDate_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = mlaundryRecordReposMon.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0, recordDate_1, actualRepayDate_2, actualRepayDate_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = mlaundryRecordReposHist.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0, recordDate_1, actualRepayDate_2, actualRepayDate_3, pageable);
    else 
      slice = mlaundryRecordRepos.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(recordDate_0, recordDate_1, actualRepayDate_2, actualRepayDate_3, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<MlaundryRecord> findRecordD(int recordDate_0, int recordDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MlaundryRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findRecordD " + dbName + " : " + "recordDate_0 : " + recordDate_0 + " recordDate_1 : " +  recordDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = mlaundryRecordReposDay.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqual(recordDate_0, recordDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = mlaundryRecordReposMon.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqual(recordDate_0, recordDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = mlaundryRecordReposHist.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqual(recordDate_0, recordDate_1, pageable);
    else 
      slice = mlaundryRecordRepos.findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqual(recordDate_0, recordDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<MlaundryRecord> findRepayD(int actualRepayDate_0, int actualRepayDate_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<MlaundryRecord> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findRepayD " + dbName + " : " + "actualRepayDate_0 : " + actualRepayDate_0 + " actualRepayDate_1 : " +  actualRepayDate_1);
    if (dbName.equals(ContentName.onDay))
      slice = mlaundryRecordReposDay.findAllByActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqual(actualRepayDate_0, actualRepayDate_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = mlaundryRecordReposMon.findAllByActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqual(actualRepayDate_0, actualRepayDate_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = mlaundryRecordReposHist.findAllByActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqual(actualRepayDate_0, actualRepayDate_1, pageable);
    else 
      slice = mlaundryRecordRepos.findAllByActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqual(actualRepayDate_0, actualRepayDate_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public MlaundryRecord holdById(MlaundryRecordId mlaundryRecordId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + mlaundryRecordId);
    Optional<MlaundryRecord> mlaundryRecord = null;
    if (dbName.equals(ContentName.onDay))
      mlaundryRecord = mlaundryRecordReposDay.findByMlaundryRecordId(mlaundryRecordId);
    else if (dbName.equals(ContentName.onMon))
      mlaundryRecord = mlaundryRecordReposMon.findByMlaundryRecordId(mlaundryRecordId);
    else if (dbName.equals(ContentName.onHist))
      mlaundryRecord = mlaundryRecordReposHist.findByMlaundryRecordId(mlaundryRecordId);
    else 
      mlaundryRecord = mlaundryRecordRepos.findByMlaundryRecordId(mlaundryRecordId);
    return mlaundryRecord.isPresent() ? mlaundryRecord.get() : null;
  }

  @Override
  public MlaundryRecord holdById(MlaundryRecord mlaundryRecord, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + mlaundryRecord.getMlaundryRecordId());
    Optional<MlaundryRecord> mlaundryRecordT = null;
    if (dbName.equals(ContentName.onDay))
      mlaundryRecordT = mlaundryRecordReposDay.findByMlaundryRecordId(mlaundryRecord.getMlaundryRecordId());
    else if (dbName.equals(ContentName.onMon))
      mlaundryRecordT = mlaundryRecordReposMon.findByMlaundryRecordId(mlaundryRecord.getMlaundryRecordId());
    else if (dbName.equals(ContentName.onHist))
      mlaundryRecordT = mlaundryRecordReposHist.findByMlaundryRecordId(mlaundryRecord.getMlaundryRecordId());
    else 
      mlaundryRecordT = mlaundryRecordRepos.findByMlaundryRecordId(mlaundryRecord.getMlaundryRecordId());
    return mlaundryRecordT.isPresent() ? mlaundryRecordT.get() : null;
  }

  @Override
  public MlaundryRecord insert(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Insert..." + dbName + " " + mlaundryRecord.getMlaundryRecordId());
    if (this.findById(mlaundryRecord.getMlaundryRecordId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      mlaundryRecord.setCreateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return mlaundryRecordReposDay.saveAndFlush(mlaundryRecord);	
    else if (dbName.equals(ContentName.onMon))
      return mlaundryRecordReposMon.saveAndFlush(mlaundryRecord);
    else if (dbName.equals(ContentName.onHist))
      return mlaundryRecordReposHist.saveAndFlush(mlaundryRecord);
    else 
    return mlaundryRecordRepos.saveAndFlush(mlaundryRecord);
  }

  @Override
  public MlaundryRecord update(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + mlaundryRecord.getMlaundryRecordId());
    if (!empNot.isEmpty())
      mlaundryRecord.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return mlaundryRecordReposDay.saveAndFlush(mlaundryRecord);	
    else if (dbName.equals(ContentName.onMon))
      return mlaundryRecordReposMon.saveAndFlush(mlaundryRecord);
    else if (dbName.equals(ContentName.onHist))
      return mlaundryRecordReposHist.saveAndFlush(mlaundryRecord);
    else 
    return mlaundryRecordRepos.saveAndFlush(mlaundryRecord);
  }

  @Override
  public MlaundryRecord update2(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + mlaundryRecord.getMlaundryRecordId());
    if (!empNot.isEmpty())
      mlaundryRecord.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      mlaundryRecordReposDay.saveAndFlush(mlaundryRecord);	
    else if (dbName.equals(ContentName.onMon))
      mlaundryRecordReposMon.saveAndFlush(mlaundryRecord);
    else if (dbName.equals(ContentName.onHist))
        mlaundryRecordReposHist.saveAndFlush(mlaundryRecord);
    else 
      mlaundryRecordRepos.saveAndFlush(mlaundryRecord);	
    return this.findById(mlaundryRecord.getMlaundryRecordId());
  }

  @Override
  public void delete(MlaundryRecord mlaundryRecord, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + mlaundryRecord.getMlaundryRecordId());
    if (dbName.equals(ContentName.onDay)) {
      mlaundryRecordReposDay.delete(mlaundryRecord);	
      mlaundryRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      mlaundryRecordReposMon.delete(mlaundryRecord);	
      mlaundryRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      mlaundryRecordReposHist.delete(mlaundryRecord);
      mlaundryRecordReposHist.flush();
    }
    else {
      mlaundryRecordRepos.delete(mlaundryRecord);
      mlaundryRecordRepos.flush();
    }
   }

  @Override
  public void insertAll(List<MlaundryRecord> mlaundryRecord, TitaVo... titaVo) throws DBException {
    if (mlaundryRecord == null || mlaundryRecord.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}    logger.info("InsertAll...");
    for (MlaundryRecord t : mlaundryRecord) 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      mlaundryRecord = mlaundryRecordReposDay.saveAll(mlaundryRecord);	
      mlaundryRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      mlaundryRecord = mlaundryRecordReposMon.saveAll(mlaundryRecord);	
      mlaundryRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      mlaundryRecord = mlaundryRecordReposHist.saveAll(mlaundryRecord);
      mlaundryRecordReposHist.flush();
    }
    else {
      mlaundryRecord = mlaundryRecordRepos.saveAll(mlaundryRecord);
      mlaundryRecordRepos.flush();
    }
    }

  @Override
  public void updateAll(List<MlaundryRecord> mlaundryRecord, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (mlaundryRecord == null || mlaundryRecord.size() == 0)
      throw new DBException(6);

    for (MlaundryRecord t : mlaundryRecord) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      mlaundryRecord = mlaundryRecordReposDay.saveAll(mlaundryRecord);	
      mlaundryRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      mlaundryRecord = mlaundryRecordReposMon.saveAll(mlaundryRecord);	
      mlaundryRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      mlaundryRecord = mlaundryRecordReposHist.saveAll(mlaundryRecord);
      mlaundryRecordReposHist.flush();
    }
    else {
      mlaundryRecord = mlaundryRecordRepos.saveAll(mlaundryRecord);
      mlaundryRecordRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<MlaundryRecord> mlaundryRecord, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (mlaundryRecord == null || mlaundryRecord.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      mlaundryRecordReposDay.deleteAll(mlaundryRecord);	
      mlaundryRecordReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      mlaundryRecordReposMon.deleteAll(mlaundryRecord);	
      mlaundryRecordReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      mlaundryRecordReposHist.deleteAll(mlaundryRecord);
      mlaundryRecordReposHist.flush();
    }
    else {
      mlaundryRecordRepos.deleteAll(mlaundryRecord);
      mlaundryRecordRepos.flush();
    }
  }

}
