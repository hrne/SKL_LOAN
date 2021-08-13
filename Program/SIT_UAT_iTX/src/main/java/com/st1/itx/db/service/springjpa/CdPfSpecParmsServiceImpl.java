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
import com.st1.itx.db.domain.CdPfSpecParms;
import com.st1.itx.db.domain.CdPfSpecParmsId;
import com.st1.itx.db.repository.online.CdPfSpecParmsRepository;
import com.st1.itx.db.repository.day.CdPfSpecParmsRepositoryDay;
import com.st1.itx.db.repository.mon.CdPfSpecParmsRepositoryMon;
import com.st1.itx.db.repository.hist.CdPfSpecParmsRepositoryHist;
import com.st1.itx.db.service.CdPfSpecParmsService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdPfSpecParmsService")
@Repository
public class CdPfSpecParmsServiceImpl implements CdPfSpecParmsService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CdPfSpecParmsServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdPfSpecParmsRepository cdPfSpecParmsRepos;

  @Autowired
  private CdPfSpecParmsRepositoryDay cdPfSpecParmsReposDay;

  @Autowired
  private CdPfSpecParmsRepositoryMon cdPfSpecParmsReposMon;

  @Autowired
  private CdPfSpecParmsRepositoryHist cdPfSpecParmsReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdPfSpecParmsRepos);
    org.junit.Assert.assertNotNull(cdPfSpecParmsReposDay);
    org.junit.Assert.assertNotNull(cdPfSpecParmsReposMon);
    org.junit.Assert.assertNotNull(cdPfSpecParmsReposHist);
  }

  @Override
  public CdPfSpecParms findById(CdPfSpecParmsId cdPfSpecParmsId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + cdPfSpecParmsId);
    Optional<CdPfSpecParms> cdPfSpecParms = null;
    if (dbName.equals(ContentName.onDay))
      cdPfSpecParms = cdPfSpecParmsReposDay.findById(cdPfSpecParmsId);
    else if (dbName.equals(ContentName.onMon))
      cdPfSpecParms = cdPfSpecParmsReposMon.findById(cdPfSpecParmsId);
    else if (dbName.equals(ContentName.onHist))
      cdPfSpecParms = cdPfSpecParmsReposHist.findById(cdPfSpecParmsId);
    else 
      cdPfSpecParms = cdPfSpecParmsRepos.findById(cdPfSpecParmsId);
    CdPfSpecParms obj = cdPfSpecParms.isPresent() ? cdPfSpecParms.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdPfSpecParms> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdPfSpecParms> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "ConditionCode", "Condition"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdPfSpecParmsReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdPfSpecParmsReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdPfSpecParmsReposHist.findAll(pageable);
    else 
      slice = cdPfSpecParmsRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdPfSpecParms> conditionCodeEq(String conditionCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdPfSpecParms> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("conditionCodeEq " + dbName + " : " + "conditionCode_0 : " + conditionCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdPfSpecParmsReposDay.findAllByConditionCodeIsOrderByConditionAsc(conditionCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdPfSpecParmsReposMon.findAllByConditionCodeIsOrderByConditionAsc(conditionCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdPfSpecParmsReposHist.findAllByConditionCodeIsOrderByConditionAsc(conditionCode_0, pageable);
    else 
      slice = cdPfSpecParmsRepos.findAllByConditionCodeIsOrderByConditionAsc(conditionCode_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdPfSpecParms holdById(CdPfSpecParmsId cdPfSpecParmsId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdPfSpecParmsId);
    Optional<CdPfSpecParms> cdPfSpecParms = null;
    if (dbName.equals(ContentName.onDay))
      cdPfSpecParms = cdPfSpecParmsReposDay.findByCdPfSpecParmsId(cdPfSpecParmsId);
    else if (dbName.equals(ContentName.onMon))
      cdPfSpecParms = cdPfSpecParmsReposMon.findByCdPfSpecParmsId(cdPfSpecParmsId);
    else if (dbName.equals(ContentName.onHist))
      cdPfSpecParms = cdPfSpecParmsReposHist.findByCdPfSpecParmsId(cdPfSpecParmsId);
    else 
      cdPfSpecParms = cdPfSpecParmsRepos.findByCdPfSpecParmsId(cdPfSpecParmsId);
    return cdPfSpecParms.isPresent() ? cdPfSpecParms.get() : null;
  }

  @Override
  public CdPfSpecParms holdById(CdPfSpecParms cdPfSpecParms, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdPfSpecParms.getCdPfSpecParmsId());
    Optional<CdPfSpecParms> cdPfSpecParmsT = null;
    if (dbName.equals(ContentName.onDay))
      cdPfSpecParmsT = cdPfSpecParmsReposDay.findByCdPfSpecParmsId(cdPfSpecParms.getCdPfSpecParmsId());
    else if (dbName.equals(ContentName.onMon))
      cdPfSpecParmsT = cdPfSpecParmsReposMon.findByCdPfSpecParmsId(cdPfSpecParms.getCdPfSpecParmsId());
    else if (dbName.equals(ContentName.onHist))
      cdPfSpecParmsT = cdPfSpecParmsReposHist.findByCdPfSpecParmsId(cdPfSpecParms.getCdPfSpecParmsId());
    else 
      cdPfSpecParmsT = cdPfSpecParmsRepos.findByCdPfSpecParmsId(cdPfSpecParms.getCdPfSpecParmsId());
    return cdPfSpecParmsT.isPresent() ? cdPfSpecParmsT.get() : null;
  }

  @Override
  public CdPfSpecParms insert(CdPfSpecParms cdPfSpecParms, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + cdPfSpecParms.getCdPfSpecParmsId());
    if (this.findById(cdPfSpecParms.getCdPfSpecParmsId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdPfSpecParms.setCreateEmpNo(empNot);

    if(cdPfSpecParms.getLastUpdateEmpNo() == null || cdPfSpecParms.getLastUpdateEmpNo().isEmpty())
      cdPfSpecParms.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdPfSpecParmsReposDay.saveAndFlush(cdPfSpecParms);	
    else if (dbName.equals(ContentName.onMon))
      return cdPfSpecParmsReposMon.saveAndFlush(cdPfSpecParms);
    else if (dbName.equals(ContentName.onHist))
      return cdPfSpecParmsReposHist.saveAndFlush(cdPfSpecParms);
    else 
    return cdPfSpecParmsRepos.saveAndFlush(cdPfSpecParms);
  }

  @Override
  public CdPfSpecParms update(CdPfSpecParms cdPfSpecParms, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdPfSpecParms.getCdPfSpecParmsId());
    if (!empNot.isEmpty())
      cdPfSpecParms.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdPfSpecParmsReposDay.saveAndFlush(cdPfSpecParms);	
    else if (dbName.equals(ContentName.onMon))
      return cdPfSpecParmsReposMon.saveAndFlush(cdPfSpecParms);
    else if (dbName.equals(ContentName.onHist))
      return cdPfSpecParmsReposHist.saveAndFlush(cdPfSpecParms);
    else 
    return cdPfSpecParmsRepos.saveAndFlush(cdPfSpecParms);
  }

  @Override
  public CdPfSpecParms update2(CdPfSpecParms cdPfSpecParms, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdPfSpecParms.getCdPfSpecParmsId());
    if (!empNot.isEmpty())
      cdPfSpecParms.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdPfSpecParmsReposDay.saveAndFlush(cdPfSpecParms);	
    else if (dbName.equals(ContentName.onMon))
      cdPfSpecParmsReposMon.saveAndFlush(cdPfSpecParms);
    else if (dbName.equals(ContentName.onHist))
        cdPfSpecParmsReposHist.saveAndFlush(cdPfSpecParms);
    else 
      cdPfSpecParmsRepos.saveAndFlush(cdPfSpecParms);	
    return this.findById(cdPfSpecParms.getCdPfSpecParmsId());
  }

  @Override
  public void delete(CdPfSpecParms cdPfSpecParms, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + cdPfSpecParms.getCdPfSpecParmsId());
    if (dbName.equals(ContentName.onDay)) {
      cdPfSpecParmsReposDay.delete(cdPfSpecParms);	
      cdPfSpecParmsReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdPfSpecParmsReposMon.delete(cdPfSpecParms);	
      cdPfSpecParmsReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdPfSpecParmsReposHist.delete(cdPfSpecParms);
      cdPfSpecParmsReposHist.flush();
    }
    else {
      cdPfSpecParmsRepos.delete(cdPfSpecParms);
      cdPfSpecParmsRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdPfSpecParms> cdPfSpecParms, TitaVo... titaVo) throws DBException {
    if (cdPfSpecParms == null || cdPfSpecParms.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (CdPfSpecParms t : cdPfSpecParms){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdPfSpecParms = cdPfSpecParmsReposDay.saveAll(cdPfSpecParms);	
      cdPfSpecParmsReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdPfSpecParms = cdPfSpecParmsReposMon.saveAll(cdPfSpecParms);	
      cdPfSpecParmsReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdPfSpecParms = cdPfSpecParmsReposHist.saveAll(cdPfSpecParms);
      cdPfSpecParmsReposHist.flush();
    }
    else {
      cdPfSpecParms = cdPfSpecParmsRepos.saveAll(cdPfSpecParms);
      cdPfSpecParmsRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdPfSpecParms> cdPfSpecParms, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (cdPfSpecParms == null || cdPfSpecParms.size() == 0)
      throw new DBException(6);

    for (CdPfSpecParms t : cdPfSpecParms) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdPfSpecParms = cdPfSpecParmsReposDay.saveAll(cdPfSpecParms);	
      cdPfSpecParmsReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdPfSpecParms = cdPfSpecParmsReposMon.saveAll(cdPfSpecParms);	
      cdPfSpecParmsReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdPfSpecParms = cdPfSpecParmsReposHist.saveAll(cdPfSpecParms);
      cdPfSpecParmsReposHist.flush();
    }
    else {
      cdPfSpecParms = cdPfSpecParmsRepos.saveAll(cdPfSpecParms);
      cdPfSpecParmsRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdPfSpecParms> cdPfSpecParms, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdPfSpecParms == null || cdPfSpecParms.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdPfSpecParmsReposDay.deleteAll(cdPfSpecParms);	
      cdPfSpecParmsReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdPfSpecParmsReposMon.deleteAll(cdPfSpecParms);	
      cdPfSpecParmsReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdPfSpecParmsReposHist.deleteAll(cdPfSpecParms);
      cdPfSpecParmsReposHist.flush();
    }
    else {
      cdPfSpecParmsRepos.deleteAll(cdPfSpecParms);
      cdPfSpecParmsRepos.flush();
    }
  }

}
