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
import com.st1.itx.db.domain.LifeRelEmp;
import com.st1.itx.db.domain.LifeRelEmpId;
import com.st1.itx.db.repository.online.LifeRelEmpRepository;
import com.st1.itx.db.repository.day.LifeRelEmpRepositoryDay;
import com.st1.itx.db.repository.mon.LifeRelEmpRepositoryMon;
import com.st1.itx.db.repository.hist.LifeRelEmpRepositoryHist;
import com.st1.itx.db.service.LifeRelEmpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("lifeRelEmpService")
@Repository
public class LifeRelEmpServiceImpl extends ASpringJpaParm implements LifeRelEmpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LifeRelEmpRepository lifeRelEmpRepos;

  @Autowired
  private LifeRelEmpRepositoryDay lifeRelEmpReposDay;

  @Autowired
  private LifeRelEmpRepositoryMon lifeRelEmpReposMon;

  @Autowired
  private LifeRelEmpRepositoryHist lifeRelEmpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(lifeRelEmpRepos);
    org.junit.Assert.assertNotNull(lifeRelEmpReposDay);
    org.junit.Assert.assertNotNull(lifeRelEmpReposMon);
    org.junit.Assert.assertNotNull(lifeRelEmpReposHist);
  }

  @Override
  public LifeRelEmp findById(LifeRelEmpId lifeRelEmpId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + lifeRelEmpId);
    Optional<LifeRelEmp> lifeRelEmp = null;
    if (dbName.equals(ContentName.onDay))
      lifeRelEmp = lifeRelEmpReposDay.findById(lifeRelEmpId);
    else if (dbName.equals(ContentName.onMon))
      lifeRelEmp = lifeRelEmpReposMon.findById(lifeRelEmpId);
    else if (dbName.equals(ContentName.onHist))
      lifeRelEmp = lifeRelEmpReposHist.findById(lifeRelEmpId);
    else 
      lifeRelEmp = lifeRelEmpRepos.findById(lifeRelEmpId);
    LifeRelEmp obj = lifeRelEmp.isPresent() ? lifeRelEmp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LifeRelEmp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LifeRelEmp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "EmpId", "AcDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EmpId", "AcDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = lifeRelEmpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = lifeRelEmpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = lifeRelEmpReposHist.findAll(pageable);
    else 
      slice = lifeRelEmpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<LifeRelEmp> findAcDate(int acDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LifeRelEmp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findAcDate " + dbName + " : " + "acDate_0 : " + acDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = lifeRelEmpReposDay.findAllByAcDateIs(acDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = lifeRelEmpReposMon.findAllByAcDateIs(acDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = lifeRelEmpReposHist.findAllByAcDateIs(acDate_0, pageable);
    else 
      slice = lifeRelEmpRepos.findAllByAcDateIs(acDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LifeRelEmp holdById(LifeRelEmpId lifeRelEmpId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + lifeRelEmpId);
    Optional<LifeRelEmp> lifeRelEmp = null;
    if (dbName.equals(ContentName.onDay))
      lifeRelEmp = lifeRelEmpReposDay.findByLifeRelEmpId(lifeRelEmpId);
    else if (dbName.equals(ContentName.onMon))
      lifeRelEmp = lifeRelEmpReposMon.findByLifeRelEmpId(lifeRelEmpId);
    else if (dbName.equals(ContentName.onHist))
      lifeRelEmp = lifeRelEmpReposHist.findByLifeRelEmpId(lifeRelEmpId);
    else 
      lifeRelEmp = lifeRelEmpRepos.findByLifeRelEmpId(lifeRelEmpId);
    return lifeRelEmp.isPresent() ? lifeRelEmp.get() : null;
  }

  @Override
  public LifeRelEmp holdById(LifeRelEmp lifeRelEmp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + lifeRelEmp.getLifeRelEmpId());
    Optional<LifeRelEmp> lifeRelEmpT = null;
    if (dbName.equals(ContentName.onDay))
      lifeRelEmpT = lifeRelEmpReposDay.findByLifeRelEmpId(lifeRelEmp.getLifeRelEmpId());
    else if (dbName.equals(ContentName.onMon))
      lifeRelEmpT = lifeRelEmpReposMon.findByLifeRelEmpId(lifeRelEmp.getLifeRelEmpId());
    else if (dbName.equals(ContentName.onHist))
      lifeRelEmpT = lifeRelEmpReposHist.findByLifeRelEmpId(lifeRelEmp.getLifeRelEmpId());
    else 
      lifeRelEmpT = lifeRelEmpRepos.findByLifeRelEmpId(lifeRelEmp.getLifeRelEmpId());
    return lifeRelEmpT.isPresent() ? lifeRelEmpT.get() : null;
  }

  @Override
  public LifeRelEmp insert(LifeRelEmp lifeRelEmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + lifeRelEmp.getLifeRelEmpId());
    if (this.findById(lifeRelEmp.getLifeRelEmpId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      lifeRelEmp.setCreateEmpNo(empNot);

    if(lifeRelEmp.getLastUpdateEmpNo() == null || lifeRelEmp.getLastUpdateEmpNo().isEmpty())
      lifeRelEmp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return lifeRelEmpReposDay.saveAndFlush(lifeRelEmp);	
    else if (dbName.equals(ContentName.onMon))
      return lifeRelEmpReposMon.saveAndFlush(lifeRelEmp);
    else if (dbName.equals(ContentName.onHist))
      return lifeRelEmpReposHist.saveAndFlush(lifeRelEmp);
    else 
    return lifeRelEmpRepos.saveAndFlush(lifeRelEmp);
  }

  @Override
  public LifeRelEmp update(LifeRelEmp lifeRelEmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + lifeRelEmp.getLifeRelEmpId());
    if (!empNot.isEmpty())
      lifeRelEmp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return lifeRelEmpReposDay.saveAndFlush(lifeRelEmp);	
    else if (dbName.equals(ContentName.onMon))
      return lifeRelEmpReposMon.saveAndFlush(lifeRelEmp);
    else if (dbName.equals(ContentName.onHist))
      return lifeRelEmpReposHist.saveAndFlush(lifeRelEmp);
    else 
    return lifeRelEmpRepos.saveAndFlush(lifeRelEmp);
  }

  @Override
  public LifeRelEmp update2(LifeRelEmp lifeRelEmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + lifeRelEmp.getLifeRelEmpId());
    if (!empNot.isEmpty())
      lifeRelEmp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      lifeRelEmpReposDay.saveAndFlush(lifeRelEmp);	
    else if (dbName.equals(ContentName.onMon))
      lifeRelEmpReposMon.saveAndFlush(lifeRelEmp);
    else if (dbName.equals(ContentName.onHist))
        lifeRelEmpReposHist.saveAndFlush(lifeRelEmp);
    else 
      lifeRelEmpRepos.saveAndFlush(lifeRelEmp);	
    return this.findById(lifeRelEmp.getLifeRelEmpId());
  }

  @Override
  public void delete(LifeRelEmp lifeRelEmp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + lifeRelEmp.getLifeRelEmpId());
    if (dbName.equals(ContentName.onDay)) {
      lifeRelEmpReposDay.delete(lifeRelEmp);	
      lifeRelEmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lifeRelEmpReposMon.delete(lifeRelEmp);	
      lifeRelEmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lifeRelEmpReposHist.delete(lifeRelEmp);
      lifeRelEmpReposHist.flush();
    }
    else {
      lifeRelEmpRepos.delete(lifeRelEmp);
      lifeRelEmpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LifeRelEmp> lifeRelEmp, TitaVo... titaVo) throws DBException {
    if (lifeRelEmp == null || lifeRelEmp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LifeRelEmp t : lifeRelEmp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      lifeRelEmp = lifeRelEmpReposDay.saveAll(lifeRelEmp);	
      lifeRelEmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lifeRelEmp = lifeRelEmpReposMon.saveAll(lifeRelEmp);	
      lifeRelEmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lifeRelEmp = lifeRelEmpReposHist.saveAll(lifeRelEmp);
      lifeRelEmpReposHist.flush();
    }
    else {
      lifeRelEmp = lifeRelEmpRepos.saveAll(lifeRelEmp);
      lifeRelEmpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LifeRelEmp> lifeRelEmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (lifeRelEmp == null || lifeRelEmp.size() == 0)
      throw new DBException(6);

    for (LifeRelEmp t : lifeRelEmp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      lifeRelEmp = lifeRelEmpReposDay.saveAll(lifeRelEmp);	
      lifeRelEmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lifeRelEmp = lifeRelEmpReposMon.saveAll(lifeRelEmp);	
      lifeRelEmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lifeRelEmp = lifeRelEmpReposHist.saveAll(lifeRelEmp);
      lifeRelEmpReposHist.flush();
    }
    else {
      lifeRelEmp = lifeRelEmpRepos.saveAll(lifeRelEmp);
      lifeRelEmpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LifeRelEmp> lifeRelEmp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (lifeRelEmp == null || lifeRelEmp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      lifeRelEmpReposDay.deleteAll(lifeRelEmp);	
      lifeRelEmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lifeRelEmpReposMon.deleteAll(lifeRelEmp);	
      lifeRelEmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lifeRelEmpReposHist.deleteAll(lifeRelEmp);
      lifeRelEmpReposHist.flush();
    }
    else {
      lifeRelEmpRepos.deleteAll(lifeRelEmp);
      lifeRelEmpRepos.flush();
    }
  }

}
