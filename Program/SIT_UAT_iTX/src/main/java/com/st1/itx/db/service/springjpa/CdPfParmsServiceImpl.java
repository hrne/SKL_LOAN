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
import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.domain.CdPfParmsId;
import com.st1.itx.db.repository.online.CdPfParmsRepository;
import com.st1.itx.db.repository.day.CdPfParmsRepositoryDay;
import com.st1.itx.db.repository.mon.CdPfParmsRepositoryMon;
import com.st1.itx.db.repository.hist.CdPfParmsRepositoryHist;
import com.st1.itx.db.service.CdPfParmsService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdPfParmsService")
@Repository
public class CdPfParmsServiceImpl extends ASpringJpaParm implements CdPfParmsService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdPfParmsRepository cdPfParmsRepos;

  @Autowired
  private CdPfParmsRepositoryDay cdPfParmsReposDay;

  @Autowired
  private CdPfParmsRepositoryMon cdPfParmsReposMon;

  @Autowired
  private CdPfParmsRepositoryHist cdPfParmsReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdPfParmsRepos);
    org.junit.Assert.assertNotNull(cdPfParmsReposDay);
    org.junit.Assert.assertNotNull(cdPfParmsReposMon);
    org.junit.Assert.assertNotNull(cdPfParmsReposHist);
  }

  @Override
  public CdPfParms findById(CdPfParmsId cdPfParmsId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + cdPfParmsId);
    Optional<CdPfParms> cdPfParms = null;
    if (dbName.equals(ContentName.onDay))
      cdPfParms = cdPfParmsReposDay.findById(cdPfParmsId);
    else if (dbName.equals(ContentName.onMon))
      cdPfParms = cdPfParmsReposMon.findById(cdPfParmsId);
    else if (dbName.equals(ContentName.onHist))
      cdPfParms = cdPfParmsReposHist.findById(cdPfParmsId);
    else 
      cdPfParms = cdPfParmsRepos.findById(cdPfParmsId);
    CdPfParms obj = cdPfParms.isPresent() ? cdPfParms.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdPfParms> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdPfParms> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "ConditionCode1", "ConditionCode2", "Condition"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ConditionCode1", "ConditionCode2", "Condition"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdPfParmsReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdPfParmsReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdPfParmsReposHist.findAll(pageable);
    else 
      slice = cdPfParmsRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdPfParms> findConditionCode1Eq(String conditionCode1_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdPfParms> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findConditionCode1Eq " + dbName + " : " + "conditionCode1_0 : " + conditionCode1_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdPfParmsReposDay.findAllByConditionCode1IsOrderByConditionCode2AscConditionAsc(conditionCode1_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdPfParmsReposMon.findAllByConditionCode1IsOrderByConditionCode2AscConditionAsc(conditionCode1_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdPfParmsReposHist.findAllByConditionCode1IsOrderByConditionCode2AscConditionAsc(conditionCode1_0, pageable);
    else 
      slice = cdPfParmsRepos.findAllByConditionCode1IsOrderByConditionCode2AscConditionAsc(conditionCode1_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdPfParms> findCode1AndCode2Eq(String conditionCode1_0, String conditionCode2_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdPfParms> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findCode1AndCode2Eq " + dbName + " : " + "conditionCode1_0 : " + conditionCode1_0 + " conditionCode2_1 : " +  conditionCode2_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdPfParmsReposDay.findAllByConditionCode1IsAndConditionCode2IsOrderByConditionAsc(conditionCode1_0, conditionCode2_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdPfParmsReposMon.findAllByConditionCode1IsAndConditionCode2IsOrderByConditionAsc(conditionCode1_0, conditionCode2_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdPfParmsReposHist.findAllByConditionCode1IsAndConditionCode2IsOrderByConditionAsc(conditionCode1_0, conditionCode2_1, pageable);
    else 
      slice = cdPfParmsRepos.findAllByConditionCode1IsAndConditionCode2IsOrderByConditionAsc(conditionCode1_0, conditionCode2_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdPfParms holdById(CdPfParmsId cdPfParmsId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdPfParmsId);
    Optional<CdPfParms> cdPfParms = null;
    if (dbName.equals(ContentName.onDay))
      cdPfParms = cdPfParmsReposDay.findByCdPfParmsId(cdPfParmsId);
    else if (dbName.equals(ContentName.onMon))
      cdPfParms = cdPfParmsReposMon.findByCdPfParmsId(cdPfParmsId);
    else if (dbName.equals(ContentName.onHist))
      cdPfParms = cdPfParmsReposHist.findByCdPfParmsId(cdPfParmsId);
    else 
      cdPfParms = cdPfParmsRepos.findByCdPfParmsId(cdPfParmsId);
    return cdPfParms.isPresent() ? cdPfParms.get() : null;
  }

  @Override
  public CdPfParms holdById(CdPfParms cdPfParms, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + cdPfParms.getCdPfParmsId());
    Optional<CdPfParms> cdPfParmsT = null;
    if (dbName.equals(ContentName.onDay))
      cdPfParmsT = cdPfParmsReposDay.findByCdPfParmsId(cdPfParms.getCdPfParmsId());
    else if (dbName.equals(ContentName.onMon))
      cdPfParmsT = cdPfParmsReposMon.findByCdPfParmsId(cdPfParms.getCdPfParmsId());
    else if (dbName.equals(ContentName.onHist))
      cdPfParmsT = cdPfParmsReposHist.findByCdPfParmsId(cdPfParms.getCdPfParmsId());
    else 
      cdPfParmsT = cdPfParmsRepos.findByCdPfParmsId(cdPfParms.getCdPfParmsId());
    return cdPfParmsT.isPresent() ? cdPfParmsT.get() : null;
  }

  @Override
  public CdPfParms insert(CdPfParms cdPfParms, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    this.info("Insert..." + dbName + " " + cdPfParms.getCdPfParmsId());
    if (this.findById(cdPfParms.getCdPfParmsId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdPfParms.setCreateEmpNo(empNot);

    if(cdPfParms.getLastUpdateEmpNo() == null || cdPfParms.getLastUpdateEmpNo().isEmpty())
      cdPfParms.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdPfParmsReposDay.saveAndFlush(cdPfParms);	
    else if (dbName.equals(ContentName.onMon))
      return cdPfParmsReposMon.saveAndFlush(cdPfParms);
    else if (dbName.equals(ContentName.onHist))
      return cdPfParmsReposHist.saveAndFlush(cdPfParms);
    else 
    return cdPfParmsRepos.saveAndFlush(cdPfParms);
  }

  @Override
  public CdPfParms update(CdPfParms cdPfParms, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + cdPfParms.getCdPfParmsId());
    if (!empNot.isEmpty())
      cdPfParms.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdPfParmsReposDay.saveAndFlush(cdPfParms);	
    else if (dbName.equals(ContentName.onMon))
      return cdPfParmsReposMon.saveAndFlush(cdPfParms);
    else if (dbName.equals(ContentName.onHist))
      return cdPfParmsReposHist.saveAndFlush(cdPfParms);
    else 
    return cdPfParmsRepos.saveAndFlush(cdPfParms);
  }

  @Override
  public CdPfParms update2(CdPfParms cdPfParms, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("Update..." + dbName + " " + cdPfParms.getCdPfParmsId());
    if (!empNot.isEmpty())
      cdPfParms.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdPfParmsReposDay.saveAndFlush(cdPfParms);	
    else if (dbName.equals(ContentName.onMon))
      cdPfParmsReposMon.saveAndFlush(cdPfParms);
    else if (dbName.equals(ContentName.onHist))
        cdPfParmsReposHist.saveAndFlush(cdPfParms);
    else 
      cdPfParmsRepos.saveAndFlush(cdPfParms);	
    return this.findById(cdPfParms.getCdPfParmsId());
  }

  @Override
  public void delete(CdPfParms cdPfParms, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + cdPfParms.getCdPfParmsId());
    if (dbName.equals(ContentName.onDay)) {
      cdPfParmsReposDay.delete(cdPfParms);	
      cdPfParmsReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdPfParmsReposMon.delete(cdPfParms);	
      cdPfParmsReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdPfParmsReposHist.delete(cdPfParms);
      cdPfParmsReposHist.flush();
    }
    else {
      cdPfParmsRepos.delete(cdPfParms);
      cdPfParmsRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdPfParms> cdPfParms, TitaVo... titaVo) throws DBException {
    if (cdPfParms == null || cdPfParms.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    this.info("InsertAll...");
    for (CdPfParms t : cdPfParms){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdPfParms = cdPfParmsReposDay.saveAll(cdPfParms);	
      cdPfParmsReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdPfParms = cdPfParmsReposMon.saveAll(cdPfParms);	
      cdPfParmsReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdPfParms = cdPfParmsReposHist.saveAll(cdPfParms);
      cdPfParmsReposHist.flush();
    }
    else {
      cdPfParms = cdPfParmsRepos.saveAll(cdPfParms);
      cdPfParmsRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdPfParms> cdPfParms, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    this.info("UpdateAll...");
    if (cdPfParms == null || cdPfParms.size() == 0)
      throw new DBException(6);

    for (CdPfParms t : cdPfParms) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdPfParms = cdPfParmsReposDay.saveAll(cdPfParms);	
      cdPfParmsReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdPfParms = cdPfParmsReposMon.saveAll(cdPfParms);	
      cdPfParmsReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdPfParms = cdPfParmsReposHist.saveAll(cdPfParms);
      cdPfParmsReposHist.flush();
    }
    else {
      cdPfParms = cdPfParmsRepos.saveAll(cdPfParms);
      cdPfParmsRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdPfParms> cdPfParms, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdPfParms == null || cdPfParms.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdPfParmsReposDay.deleteAll(cdPfParms);	
      cdPfParmsReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdPfParmsReposMon.deleteAll(cdPfParms);	
      cdPfParmsReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdPfParmsReposHist.deleteAll(cdPfParms);
      cdPfParmsReposHist.flush();
    }
    else {
      cdPfParmsRepos.deleteAll(cdPfParms);
      cdPfParmsRepos.flush();
    }
  }

}
