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
import com.st1.itx.db.domain.CollListTmp;
import com.st1.itx.db.domain.CollListTmpId;
import com.st1.itx.db.repository.online.CollListTmpRepository;
import com.st1.itx.db.repository.day.CollListTmpRepositoryDay;
import com.st1.itx.db.repository.mon.CollListTmpRepositoryMon;
import com.st1.itx.db.repository.hist.CollListTmpRepositoryHist;
import com.st1.itx.db.service.CollListTmpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("collListTmpService")
@Repository
public class CollListTmpServiceImpl extends ASpringJpaParm implements CollListTmpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CollListTmpRepository collListTmpRepos;

  @Autowired
  private CollListTmpRepositoryDay collListTmpReposDay;

  @Autowired
  private CollListTmpRepositoryMon collListTmpReposMon;

  @Autowired
  private CollListTmpRepositoryHist collListTmpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(collListTmpRepos);
    org.junit.Assert.assertNotNull(collListTmpReposDay);
    org.junit.Assert.assertNotNull(collListTmpReposMon);
    org.junit.Assert.assertNotNull(collListTmpReposHist);
  }

  @Override
  public CollListTmp findById(CollListTmpId collListTmpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + collListTmpId);
    Optional<CollListTmp> collListTmp = null;
    if (dbName.equals(ContentName.onDay))
      collListTmp = collListTmpReposDay.findById(collListTmpId);
    else if (dbName.equals(ContentName.onMon))
      collListTmp = collListTmpReposMon.findById(collListTmpId);
    else if (dbName.equals(ContentName.onHist))
      collListTmp = collListTmpReposHist.findById(collListTmpId);
    else 
      collListTmp = collListTmpRepos.findById(collListTmpId);
    CollListTmp obj = collListTmp.isPresent() ? collListTmp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CollListTmp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CollListTmp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "ClCode1", "ClCode2", "ClNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo", "ClCode1", "ClCode2", "ClNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = collListTmpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = collListTmpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = collListTmpReposHist.findAll(pageable);
    else 
      slice = collListTmpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CollListTmp holdById(CollListTmpId collListTmpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collListTmpId);
    Optional<CollListTmp> collListTmp = null;
    if (dbName.equals(ContentName.onDay))
      collListTmp = collListTmpReposDay.findByCollListTmpId(collListTmpId);
    else if (dbName.equals(ContentName.onMon))
      collListTmp = collListTmpReposMon.findByCollListTmpId(collListTmpId);
    else if (dbName.equals(ContentName.onHist))
      collListTmp = collListTmpReposHist.findByCollListTmpId(collListTmpId);
    else 
      collListTmp = collListTmpRepos.findByCollListTmpId(collListTmpId);
    return collListTmp.isPresent() ? collListTmp.get() : null;
  }

  @Override
  public CollListTmp holdById(CollListTmp collListTmp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + collListTmp.getCollListTmpId());
    Optional<CollListTmp> collListTmpT = null;
    if (dbName.equals(ContentName.onDay))
      collListTmpT = collListTmpReposDay.findByCollListTmpId(collListTmp.getCollListTmpId());
    else if (dbName.equals(ContentName.onMon))
      collListTmpT = collListTmpReposMon.findByCollListTmpId(collListTmp.getCollListTmpId());
    else if (dbName.equals(ContentName.onHist))
      collListTmpT = collListTmpReposHist.findByCollListTmpId(collListTmp.getCollListTmpId());
    else 
      collListTmpT = collListTmpRepos.findByCollListTmpId(collListTmp.getCollListTmpId());
    return collListTmpT.isPresent() ? collListTmpT.get() : null;
  }

  @Override
  public CollListTmp insert(CollListTmp collListTmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + collListTmp.getCollListTmpId());
    if (this.findById(collListTmp.getCollListTmpId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      collListTmp.setCreateEmpNo(empNot);

    if(collListTmp.getLastUpdateEmpNo() == null || collListTmp.getLastUpdateEmpNo().isEmpty())
      collListTmp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collListTmpReposDay.saveAndFlush(collListTmp);	
    else if (dbName.equals(ContentName.onMon))
      return collListTmpReposMon.saveAndFlush(collListTmp);
    else if (dbName.equals(ContentName.onHist))
      return collListTmpReposHist.saveAndFlush(collListTmp);
    else 
    return collListTmpRepos.saveAndFlush(collListTmp);
  }

  @Override
  public CollListTmp update(CollListTmp collListTmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + collListTmp.getCollListTmpId());
    if (!empNot.isEmpty())
      collListTmp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return collListTmpReposDay.saveAndFlush(collListTmp);	
    else if (dbName.equals(ContentName.onMon))
      return collListTmpReposMon.saveAndFlush(collListTmp);
    else if (dbName.equals(ContentName.onHist))
      return collListTmpReposHist.saveAndFlush(collListTmp);
    else 
    return collListTmpRepos.saveAndFlush(collListTmp);
  }

  @Override
  public CollListTmp update2(CollListTmp collListTmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + collListTmp.getCollListTmpId());
    if (!empNot.isEmpty())
      collListTmp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      collListTmpReposDay.saveAndFlush(collListTmp);	
    else if (dbName.equals(ContentName.onMon))
      collListTmpReposMon.saveAndFlush(collListTmp);
    else if (dbName.equals(ContentName.onHist))
        collListTmpReposHist.saveAndFlush(collListTmp);
    else 
      collListTmpRepos.saveAndFlush(collListTmp);	
    return this.findById(collListTmp.getCollListTmpId());
  }

  @Override
  public void delete(CollListTmp collListTmp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + collListTmp.getCollListTmpId());
    if (dbName.equals(ContentName.onDay)) {
      collListTmpReposDay.delete(collListTmp);	
      collListTmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collListTmpReposMon.delete(collListTmp);	
      collListTmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collListTmpReposHist.delete(collListTmp);
      collListTmpReposHist.flush();
    }
    else {
      collListTmpRepos.delete(collListTmp);
      collListTmpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CollListTmp> collListTmp, TitaVo... titaVo) throws DBException {
    if (collListTmp == null || collListTmp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CollListTmp t : collListTmp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      collListTmp = collListTmpReposDay.saveAll(collListTmp);	
      collListTmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collListTmp = collListTmpReposMon.saveAll(collListTmp);	
      collListTmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collListTmp = collListTmpReposHist.saveAll(collListTmp);
      collListTmpReposHist.flush();
    }
    else {
      collListTmp = collListTmpRepos.saveAll(collListTmp);
      collListTmpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CollListTmp> collListTmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (collListTmp == null || collListTmp.size() == 0)
      throw new DBException(6);

    for (CollListTmp t : collListTmp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      collListTmp = collListTmpReposDay.saveAll(collListTmp);	
      collListTmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collListTmp = collListTmpReposMon.saveAll(collListTmp);	
      collListTmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collListTmp = collListTmpReposHist.saveAll(collListTmp);
      collListTmpReposHist.flush();
    }
    else {
      collListTmp = collListTmpRepos.saveAll(collListTmp);
      collListTmpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CollListTmp> collListTmp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (collListTmp == null || collListTmp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      collListTmpReposDay.deleteAll(collListTmp);	
      collListTmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      collListTmpReposMon.deleteAll(collListTmp);	
      collListTmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      collListTmpReposHist.deleteAll(collListTmp);
      collListTmpReposHist.flush();
    }
    else {
      collListTmpRepos.deleteAll(collListTmp);
      collListTmpRepos.flush();
    }
  }

}
