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
import com.st1.itx.db.domain.CdOverdue;
import com.st1.itx.db.domain.CdOverdueId;
import com.st1.itx.db.repository.online.CdOverdueRepository;
import com.st1.itx.db.repository.day.CdOverdueRepositoryDay;
import com.st1.itx.db.repository.mon.CdOverdueRepositoryMon;
import com.st1.itx.db.repository.hist.CdOverdueRepositoryHist;
import com.st1.itx.db.service.CdOverdueService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdOverdueService")
@Repository
public class CdOverdueServiceImpl implements CdOverdueService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CdOverdueServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdOverdueRepository cdOverdueRepos;

  @Autowired
  private CdOverdueRepositoryDay cdOverdueReposDay;

  @Autowired
  private CdOverdueRepositoryMon cdOverdueReposMon;

  @Autowired
  private CdOverdueRepositoryHist cdOverdueReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdOverdueRepos);
    org.junit.Assert.assertNotNull(cdOverdueReposDay);
    org.junit.Assert.assertNotNull(cdOverdueReposMon);
    org.junit.Assert.assertNotNull(cdOverdueReposHist);
  }

  @Override
  public CdOverdue findById(CdOverdueId cdOverdueId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + cdOverdueId);
    Optional<CdOverdue> cdOverdue = null;
    if (dbName.equals(ContentName.onDay))
      cdOverdue = cdOverdueReposDay.findById(cdOverdueId);
    else if (dbName.equals(ContentName.onMon))
      cdOverdue = cdOverdueReposMon.findById(cdOverdueId);
    else if (dbName.equals(ContentName.onHist))
      cdOverdue = cdOverdueReposHist.findById(cdOverdueId);
    else 
      cdOverdue = cdOverdueRepos.findById(cdOverdueId);
    CdOverdue obj = cdOverdue.isPresent() ? cdOverdue.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdOverdue> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdOverdue> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "OverdueSign", "OverdueCode"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdOverdueReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdOverdueReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdOverdueReposHist.findAll(pageable);
    else 
      slice = cdOverdueRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdOverdue> findOverdueSign(String overdueSign_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdOverdue> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findOverdueSign " + dbName + " : " + "overdueSign_0 : " + overdueSign_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdOverdueReposDay.findAllByOverdueSignIs(overdueSign_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdOverdueReposMon.findAllByOverdueSignIs(overdueSign_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdOverdueReposHist.findAllByOverdueSignIs(overdueSign_0, pageable);
    else 
      slice = cdOverdueRepos.findAllByOverdueSignIs(overdueSign_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdOverdue> findOverdueCode(String overdueCode_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdOverdue> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findOverdueCode " + dbName + " : " + "overdueCode_0 : " + overdueCode_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdOverdueReposDay.findAllByOverdueCodeIs(overdueCode_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdOverdueReposMon.findAllByOverdueCodeIs(overdueCode_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdOverdueReposHist.findAllByOverdueCodeIs(overdueCode_0, pageable);
    else 
      slice = cdOverdueRepos.findAllByOverdueCodeIs(overdueCode_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdOverdue> findEnable(String enable_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdOverdue> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findEnable " + dbName + " : " + "enable_0 : " + enable_0);
    if (dbName.equals(ContentName.onDay))
      slice = cdOverdueReposDay.findAllByEnableIs(enable_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdOverdueReposMon.findAllByEnableIs(enable_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdOverdueReposHist.findAllByEnableIs(enable_0, pageable);
    else 
      slice = cdOverdueRepos.findAllByEnableIs(enable_0, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdOverdue> overdueCodeRange(String overdueSign_0, String overdueSign_1, String overdueCode_2, String overdueCode_3, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdOverdue> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("overdueCodeRange " + dbName + " : " + "overdueSign_0 : " + overdueSign_0 + " overdueSign_1 : " +  overdueSign_1 + " overdueCode_2 : " +  overdueCode_2 + " overdueCode_3 : " +  overdueCode_3);
    if (dbName.equals(ContentName.onDay))
      slice = cdOverdueReposDay.findAllByOverdueSignGreaterThanEqualAndOverdueSignLessThanEqualAndOverdueCodeGreaterThanEqualAndOverdueCodeLessThanEqualOrderByOverdueSignAscOverdueCodeAsc(overdueSign_0, overdueSign_1, overdueCode_2, overdueCode_3, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdOverdueReposMon.findAllByOverdueSignGreaterThanEqualAndOverdueSignLessThanEqualAndOverdueCodeGreaterThanEqualAndOverdueCodeLessThanEqualOrderByOverdueSignAscOverdueCodeAsc(overdueSign_0, overdueSign_1, overdueCode_2, overdueCode_3, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdOverdueReposHist.findAllByOverdueSignGreaterThanEqualAndOverdueSignLessThanEqualAndOverdueCodeGreaterThanEqualAndOverdueCodeLessThanEqualOrderByOverdueSignAscOverdueCodeAsc(overdueSign_0, overdueSign_1, overdueCode_2, overdueCode_3, pageable);
    else 
      slice = cdOverdueRepos.findAllByOverdueSignGreaterThanEqualAndOverdueSignLessThanEqualAndOverdueCodeGreaterThanEqualAndOverdueCodeLessThanEqualOrderByOverdueSignAscOverdueCodeAsc(overdueSign_0, overdueSign_1, overdueCode_2, overdueCode_3, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdOverdue holdById(CdOverdueId cdOverdueId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdOverdueId);
    Optional<CdOverdue> cdOverdue = null;
    if (dbName.equals(ContentName.onDay))
      cdOverdue = cdOverdueReposDay.findByCdOverdueId(cdOverdueId);
    else if (dbName.equals(ContentName.onMon))
      cdOverdue = cdOverdueReposMon.findByCdOverdueId(cdOverdueId);
    else if (dbName.equals(ContentName.onHist))
      cdOverdue = cdOverdueReposHist.findByCdOverdueId(cdOverdueId);
    else 
      cdOverdue = cdOverdueRepos.findByCdOverdueId(cdOverdueId);
    return cdOverdue.isPresent() ? cdOverdue.get() : null;
  }

  @Override
  public CdOverdue holdById(CdOverdue cdOverdue, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdOverdue.getCdOverdueId());
    Optional<CdOverdue> cdOverdueT = null;
    if (dbName.equals(ContentName.onDay))
      cdOverdueT = cdOverdueReposDay.findByCdOverdueId(cdOverdue.getCdOverdueId());
    else if (dbName.equals(ContentName.onMon))
      cdOverdueT = cdOverdueReposMon.findByCdOverdueId(cdOverdue.getCdOverdueId());
    else if (dbName.equals(ContentName.onHist))
      cdOverdueT = cdOverdueReposHist.findByCdOverdueId(cdOverdue.getCdOverdueId());
    else 
      cdOverdueT = cdOverdueRepos.findByCdOverdueId(cdOverdue.getCdOverdueId());
    return cdOverdueT.isPresent() ? cdOverdueT.get() : null;
  }

  @Override
  public CdOverdue insert(CdOverdue cdOverdue, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + cdOverdue.getCdOverdueId());
    if (this.findById(cdOverdue.getCdOverdueId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdOverdue.setCreateEmpNo(empNot);

    if(cdOverdue.getLastUpdateEmpNo() == null || cdOverdue.getLastUpdateEmpNo().isEmpty())
      cdOverdue.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdOverdueReposDay.saveAndFlush(cdOverdue);	
    else if (dbName.equals(ContentName.onMon))
      return cdOverdueReposMon.saveAndFlush(cdOverdue);
    else if (dbName.equals(ContentName.onHist))
      return cdOverdueReposHist.saveAndFlush(cdOverdue);
    else 
    return cdOverdueRepos.saveAndFlush(cdOverdue);
  }

  @Override
  public CdOverdue update(CdOverdue cdOverdue, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdOverdue.getCdOverdueId());
    if (!empNot.isEmpty())
      cdOverdue.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdOverdueReposDay.saveAndFlush(cdOverdue);	
    else if (dbName.equals(ContentName.onMon))
      return cdOverdueReposMon.saveAndFlush(cdOverdue);
    else if (dbName.equals(ContentName.onHist))
      return cdOverdueReposHist.saveAndFlush(cdOverdue);
    else 
    return cdOverdueRepos.saveAndFlush(cdOverdue);
  }

  @Override
  public CdOverdue update2(CdOverdue cdOverdue, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdOverdue.getCdOverdueId());
    if (!empNot.isEmpty())
      cdOverdue.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdOverdueReposDay.saveAndFlush(cdOverdue);	
    else if (dbName.equals(ContentName.onMon))
      cdOverdueReposMon.saveAndFlush(cdOverdue);
    else if (dbName.equals(ContentName.onHist))
        cdOverdueReposHist.saveAndFlush(cdOverdue);
    else 
      cdOverdueRepos.saveAndFlush(cdOverdue);	
    return this.findById(cdOverdue.getCdOverdueId());
  }

  @Override
  public void delete(CdOverdue cdOverdue, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + cdOverdue.getCdOverdueId());
    if (dbName.equals(ContentName.onDay)) {
      cdOverdueReposDay.delete(cdOverdue);	
      cdOverdueReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdOverdueReposMon.delete(cdOverdue);	
      cdOverdueReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdOverdueReposHist.delete(cdOverdue);
      cdOverdueReposHist.flush();
    }
    else {
      cdOverdueRepos.delete(cdOverdue);
      cdOverdueRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdOverdue> cdOverdue, TitaVo... titaVo) throws DBException {
    if (cdOverdue == null || cdOverdue.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (CdOverdue t : cdOverdue){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdOverdue = cdOverdueReposDay.saveAll(cdOverdue);	
      cdOverdueReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdOverdue = cdOverdueReposMon.saveAll(cdOverdue);	
      cdOverdueReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdOverdue = cdOverdueReposHist.saveAll(cdOverdue);
      cdOverdueReposHist.flush();
    }
    else {
      cdOverdue = cdOverdueRepos.saveAll(cdOverdue);
      cdOverdueRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdOverdue> cdOverdue, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (cdOverdue == null || cdOverdue.size() == 0)
      throw new DBException(6);

    for (CdOverdue t : cdOverdue) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdOverdue = cdOverdueReposDay.saveAll(cdOverdue);	
      cdOverdueReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdOverdue = cdOverdueReposMon.saveAll(cdOverdue);	
      cdOverdueReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdOverdue = cdOverdueReposHist.saveAll(cdOverdue);
      cdOverdueReposHist.flush();
    }
    else {
      cdOverdue = cdOverdueRepos.saveAll(cdOverdue);
      cdOverdueRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdOverdue> cdOverdue, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdOverdue == null || cdOverdue.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdOverdueReposDay.deleteAll(cdOverdue);	
      cdOverdueReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdOverdueReposMon.deleteAll(cdOverdue);	
      cdOverdueReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdOverdueReposHist.deleteAll(cdOverdue);
      cdOverdueReposHist.flush();
    }
    else {
      cdOverdueRepos.deleteAll(cdOverdue);
      cdOverdueRepos.flush();
    }
  }

}
