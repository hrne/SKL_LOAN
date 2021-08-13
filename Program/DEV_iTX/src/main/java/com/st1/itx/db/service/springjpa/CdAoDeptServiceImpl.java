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
import com.st1.itx.db.domain.CdAoDept;
import com.st1.itx.db.repository.online.CdAoDeptRepository;
import com.st1.itx.db.repository.day.CdAoDeptRepositoryDay;
import com.st1.itx.db.repository.mon.CdAoDeptRepositoryMon;
import com.st1.itx.db.repository.hist.CdAoDeptRepositoryHist;
import com.st1.itx.db.service.CdAoDeptService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("cdAoDeptService")
@Repository
public class CdAoDeptServiceImpl implements CdAoDeptService, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(CdAoDeptServiceImpl.class);

  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CdAoDeptRepository cdAoDeptRepos;

  @Autowired
  private CdAoDeptRepositoryDay cdAoDeptReposDay;

  @Autowired
  private CdAoDeptRepositoryMon cdAoDeptReposMon;

  @Autowired
  private CdAoDeptRepositoryHist cdAoDeptReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(cdAoDeptRepos);
    org.junit.Assert.assertNotNull(cdAoDeptReposDay);
    org.junit.Assert.assertNotNull(cdAoDeptReposMon);
    org.junit.Assert.assertNotNull(cdAoDeptReposHist);
  }

  @Override
  public CdAoDept findById(String employeeNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("findById " + dbName + " " + employeeNo);
    Optional<CdAoDept> cdAoDept = null;
    if (dbName.equals(ContentName.onDay))
      cdAoDept = cdAoDeptReposDay.findById(employeeNo);
    else if (dbName.equals(ContentName.onMon))
      cdAoDept = cdAoDeptReposMon.findById(employeeNo);
    else if (dbName.equals(ContentName.onHist))
      cdAoDept = cdAoDeptReposHist.findById(employeeNo);
    else 
      cdAoDept = cdAoDeptRepos.findById(employeeNo);
    CdAoDept obj = cdAoDept.isPresent() ? cdAoDept.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CdAoDept> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdAoDept> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EmployeeNo"));
    logger.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = cdAoDeptReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdAoDeptReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdAoDeptReposHist.findAll(pageable);
    else 
      slice = cdAoDeptRepos.findAll(pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CdAoDept> findEmployeeNo(String employeeNo_0, String employeeNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CdAoDept> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    logger.info("findEmployeeNo " + dbName + " : " + "employeeNo_0 : " + employeeNo_0 + " employeeNo_1 : " +  employeeNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = cdAoDeptReposDay.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = cdAoDeptReposMon.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = cdAoDeptReposHist.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, pageable);
    else 
      slice = cdAoDeptRepos.findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualOrderByEmployeeNoAsc(employeeNo_0, employeeNo_1, pageable);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CdAoDept holdById(String employeeNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + employeeNo);
    Optional<CdAoDept> cdAoDept = null;
    if (dbName.equals(ContentName.onDay))
      cdAoDept = cdAoDeptReposDay.findByEmployeeNo(employeeNo);
    else if (dbName.equals(ContentName.onMon))
      cdAoDept = cdAoDeptReposMon.findByEmployeeNo(employeeNo);
    else if (dbName.equals(ContentName.onHist))
      cdAoDept = cdAoDeptReposHist.findByEmployeeNo(employeeNo);
    else 
      cdAoDept = cdAoDeptRepos.findByEmployeeNo(employeeNo);
    return cdAoDept.isPresent() ? cdAoDept.get() : null;
  }

  @Override
  public CdAoDept holdById(CdAoDept cdAoDept, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Hold " + dbName + " " + cdAoDept.getEmployeeNo());
    Optional<CdAoDept> cdAoDeptT = null;
    if (dbName.equals(ContentName.onDay))
      cdAoDeptT = cdAoDeptReposDay.findByEmployeeNo(cdAoDept.getEmployeeNo());
    else if (dbName.equals(ContentName.onMon))
      cdAoDeptT = cdAoDeptReposMon.findByEmployeeNo(cdAoDept.getEmployeeNo());
    else if (dbName.equals(ContentName.onHist))
      cdAoDeptT = cdAoDeptReposHist.findByEmployeeNo(cdAoDept.getEmployeeNo());
    else 
      cdAoDeptT = cdAoDeptRepos.findByEmployeeNo(cdAoDept.getEmployeeNo());
    return cdAoDeptT.isPresent() ? cdAoDeptT.get() : null;
  }

  @Override
  public CdAoDept insert(CdAoDept cdAoDept, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}
    logger.info("Insert..." + dbName + " " + cdAoDept.getEmployeeNo());
    if (this.findById(cdAoDept.getEmployeeNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      cdAoDept.setCreateEmpNo(empNot);

    if(cdAoDept.getLastUpdateEmpNo() == null || cdAoDept.getLastUpdateEmpNo().isEmpty())
      cdAoDept.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdAoDeptReposDay.saveAndFlush(cdAoDept);	
    else if (dbName.equals(ContentName.onMon))
      return cdAoDeptReposMon.saveAndFlush(cdAoDept);
    else if (dbName.equals(ContentName.onHist))
      return cdAoDeptReposHist.saveAndFlush(cdAoDept);
    else 
    return cdAoDeptRepos.saveAndFlush(cdAoDept);
  }

  @Override
  public CdAoDept update(CdAoDept cdAoDept, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdAoDept.getEmployeeNo());
    if (!empNot.isEmpty())
      cdAoDept.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return cdAoDeptReposDay.saveAndFlush(cdAoDept);	
    else if (dbName.equals(ContentName.onMon))
      return cdAoDeptReposMon.saveAndFlush(cdAoDept);
    else if (dbName.equals(ContentName.onHist))
      return cdAoDeptReposHist.saveAndFlush(cdAoDept);
    else 
    return cdAoDeptRepos.saveAndFlush(cdAoDept);
  }

  @Override
  public CdAoDept update2(CdAoDept cdAoDept, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("Update..." + dbName + " " + cdAoDept.getEmployeeNo());
    if (!empNot.isEmpty())
      cdAoDept.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      cdAoDeptReposDay.saveAndFlush(cdAoDept);	
    else if (dbName.equals(ContentName.onMon))
      cdAoDeptReposMon.saveAndFlush(cdAoDept);
    else if (dbName.equals(ContentName.onHist))
        cdAoDeptReposHist.saveAndFlush(cdAoDept);
    else 
      cdAoDeptRepos.saveAndFlush(cdAoDept);	
    return this.findById(cdAoDept.getEmployeeNo());
  }

  @Override
  public void delete(CdAoDept cdAoDept, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    logger.info("Delete..." + dbName + " " + cdAoDept.getEmployeeNo());
    if (dbName.equals(ContentName.onDay)) {
      cdAoDeptReposDay.delete(cdAoDept);	
      cdAoDeptReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAoDeptReposMon.delete(cdAoDept);	
      cdAoDeptReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAoDeptReposHist.delete(cdAoDept);
      cdAoDeptReposHist.flush();
    }
    else {
      cdAoDeptRepos.delete(cdAoDept);
      cdAoDeptRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CdAoDept> cdAoDept, TitaVo... titaVo) throws DBException {
    if (cdAoDept == null || cdAoDept.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		}    logger.info("InsertAll...");
    for (CdAoDept t : cdAoDept){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      cdAoDept = cdAoDeptReposDay.saveAll(cdAoDept);	
      cdAoDeptReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAoDept = cdAoDeptReposMon.saveAll(cdAoDept);	
      cdAoDeptReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAoDept = cdAoDeptReposHist.saveAll(cdAoDept);
      cdAoDeptReposHist.flush();
    }
    else {
      cdAoDept = cdAoDeptRepos.saveAll(cdAoDept);
      cdAoDeptRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CdAoDept> cdAoDept, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
    logger.info("UpdateAll...");
    if (cdAoDept == null || cdAoDept.size() == 0)
      throw new DBException(6);

    for (CdAoDept t : cdAoDept) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      cdAoDept = cdAoDeptReposDay.saveAll(cdAoDept);	
      cdAoDeptReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAoDept = cdAoDeptReposMon.saveAll(cdAoDept);	
      cdAoDeptReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAoDept = cdAoDeptReposHist.saveAll(cdAoDept);
      cdAoDeptReposHist.flush();
    }
    else {
      cdAoDept = cdAoDeptRepos.saveAll(cdAoDept);
      cdAoDeptRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CdAoDept> cdAoDept, TitaVo... titaVo) throws DBException {
    logger.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (cdAoDept == null || cdAoDept.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      cdAoDeptReposDay.deleteAll(cdAoDept);	
      cdAoDeptReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      cdAoDeptReposMon.deleteAll(cdAoDept);	
      cdAoDeptReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      cdAoDeptReposHist.deleteAll(cdAoDept);
      cdAoDeptReposHist.flush();
    }
    else {
      cdAoDeptRepos.deleteAll(cdAoDept);
      cdAoDeptRepos.flush();
    }
  }

}
