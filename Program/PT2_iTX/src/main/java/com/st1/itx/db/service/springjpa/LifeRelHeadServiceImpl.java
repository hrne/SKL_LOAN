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
import com.st1.itx.db.domain.LifeRelHead;
import com.st1.itx.db.domain.LifeRelHeadId;
import com.st1.itx.db.repository.online.LifeRelHeadRepository;
import com.st1.itx.db.repository.day.LifeRelHeadRepositoryDay;
import com.st1.itx.db.repository.mon.LifeRelHeadRepositoryMon;
import com.st1.itx.db.repository.hist.LifeRelHeadRepositoryHist;
import com.st1.itx.db.service.LifeRelHeadService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("lifeRelHeadService")
@Repository
public class LifeRelHeadServiceImpl extends ASpringJpaParm implements LifeRelHeadService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private LifeRelHeadRepository lifeRelHeadRepos;

  @Autowired
  private LifeRelHeadRepositoryDay lifeRelHeadReposDay;

  @Autowired
  private LifeRelHeadRepositoryMon lifeRelHeadReposMon;

  @Autowired
  private LifeRelHeadRepositoryHist lifeRelHeadReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(lifeRelHeadRepos);
    org.junit.Assert.assertNotNull(lifeRelHeadReposDay);
    org.junit.Assert.assertNotNull(lifeRelHeadReposMon);
    org.junit.Assert.assertNotNull(lifeRelHeadReposHist);
  }

  @Override
  public LifeRelHead findById(LifeRelHeadId lifeRelHeadId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + lifeRelHeadId);
    Optional<LifeRelHead> lifeRelHead = null;
    if (dbName.equals(ContentName.onDay))
      lifeRelHead = lifeRelHeadReposDay.findById(lifeRelHeadId);
    else if (dbName.equals(ContentName.onMon))
      lifeRelHead = lifeRelHeadReposMon.findById(lifeRelHeadId);
    else if (dbName.equals(ContentName.onHist))
      lifeRelHead = lifeRelHeadReposHist.findById(lifeRelHeadId);
    else 
      lifeRelHead = lifeRelHeadRepos.findById(lifeRelHeadId);
    LifeRelHead obj = lifeRelHead.isPresent() ? lifeRelHead.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<LifeRelHead> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<LifeRelHead> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "HeadId", "RelId", "BusId"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "HeadId", "RelId", "BusId"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = lifeRelHeadReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = lifeRelHeadReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = lifeRelHeadReposHist.findAll(pageable);
    else 
      slice = lifeRelHeadRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public LifeRelHead holdById(LifeRelHeadId lifeRelHeadId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + lifeRelHeadId);
    Optional<LifeRelHead> lifeRelHead = null;
    if (dbName.equals(ContentName.onDay))
      lifeRelHead = lifeRelHeadReposDay.findByLifeRelHeadId(lifeRelHeadId);
    else if (dbName.equals(ContentName.onMon))
      lifeRelHead = lifeRelHeadReposMon.findByLifeRelHeadId(lifeRelHeadId);
    else if (dbName.equals(ContentName.onHist))
      lifeRelHead = lifeRelHeadReposHist.findByLifeRelHeadId(lifeRelHeadId);
    else 
      lifeRelHead = lifeRelHeadRepos.findByLifeRelHeadId(lifeRelHeadId);
    return lifeRelHead.isPresent() ? lifeRelHead.get() : null;
  }

  @Override
  public LifeRelHead holdById(LifeRelHead lifeRelHead, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + lifeRelHead.getLifeRelHeadId());
    Optional<LifeRelHead> lifeRelHeadT = null;
    if (dbName.equals(ContentName.onDay))
      lifeRelHeadT = lifeRelHeadReposDay.findByLifeRelHeadId(lifeRelHead.getLifeRelHeadId());
    else if (dbName.equals(ContentName.onMon))
      lifeRelHeadT = lifeRelHeadReposMon.findByLifeRelHeadId(lifeRelHead.getLifeRelHeadId());
    else if (dbName.equals(ContentName.onHist))
      lifeRelHeadT = lifeRelHeadReposHist.findByLifeRelHeadId(lifeRelHead.getLifeRelHeadId());
    else 
      lifeRelHeadT = lifeRelHeadRepos.findByLifeRelHeadId(lifeRelHead.getLifeRelHeadId());
    return lifeRelHeadT.isPresent() ? lifeRelHeadT.get() : null;
  }

  @Override
  public LifeRelHead insert(LifeRelHead lifeRelHead, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + lifeRelHead.getLifeRelHeadId());
    if (this.findById(lifeRelHead.getLifeRelHeadId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      lifeRelHead.setCreateEmpNo(empNot);

    if(lifeRelHead.getLastUpdateEmpNo() == null || lifeRelHead.getLastUpdateEmpNo().isEmpty())
      lifeRelHead.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return lifeRelHeadReposDay.saveAndFlush(lifeRelHead);	
    else if (dbName.equals(ContentName.onMon))
      return lifeRelHeadReposMon.saveAndFlush(lifeRelHead);
    else if (dbName.equals(ContentName.onHist))
      return lifeRelHeadReposHist.saveAndFlush(lifeRelHead);
    else 
    return lifeRelHeadRepos.saveAndFlush(lifeRelHead);
  }

  @Override
  public LifeRelHead update(LifeRelHead lifeRelHead, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + lifeRelHead.getLifeRelHeadId());
    if (!empNot.isEmpty())
      lifeRelHead.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return lifeRelHeadReposDay.saveAndFlush(lifeRelHead);	
    else if (dbName.equals(ContentName.onMon))
      return lifeRelHeadReposMon.saveAndFlush(lifeRelHead);
    else if (dbName.equals(ContentName.onHist))
      return lifeRelHeadReposHist.saveAndFlush(lifeRelHead);
    else 
    return lifeRelHeadRepos.saveAndFlush(lifeRelHead);
  }

  @Override
  public LifeRelHead update2(LifeRelHead lifeRelHead, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + lifeRelHead.getLifeRelHeadId());
    if (!empNot.isEmpty())
      lifeRelHead.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      lifeRelHeadReposDay.saveAndFlush(lifeRelHead);	
    else if (dbName.equals(ContentName.onMon))
      lifeRelHeadReposMon.saveAndFlush(lifeRelHead);
    else if (dbName.equals(ContentName.onHist))
        lifeRelHeadReposHist.saveAndFlush(lifeRelHead);
    else 
      lifeRelHeadRepos.saveAndFlush(lifeRelHead);	
    return this.findById(lifeRelHead.getLifeRelHeadId());
  }

  @Override
  public void delete(LifeRelHead lifeRelHead, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + lifeRelHead.getLifeRelHeadId());
    if (dbName.equals(ContentName.onDay)) {
      lifeRelHeadReposDay.delete(lifeRelHead);	
      lifeRelHeadReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lifeRelHeadReposMon.delete(lifeRelHead);	
      lifeRelHeadReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lifeRelHeadReposHist.delete(lifeRelHead);
      lifeRelHeadReposHist.flush();
    }
    else {
      lifeRelHeadRepos.delete(lifeRelHead);
      lifeRelHeadRepos.flush();
    }
   }

  @Override
  public void insertAll(List<LifeRelHead> lifeRelHead, TitaVo... titaVo) throws DBException {
    if (lifeRelHead == null || lifeRelHead.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (LifeRelHead t : lifeRelHead){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      lifeRelHead = lifeRelHeadReposDay.saveAll(lifeRelHead);	
      lifeRelHeadReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lifeRelHead = lifeRelHeadReposMon.saveAll(lifeRelHead);	
      lifeRelHeadReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lifeRelHead = lifeRelHeadReposHist.saveAll(lifeRelHead);
      lifeRelHeadReposHist.flush();
    }
    else {
      lifeRelHead = lifeRelHeadRepos.saveAll(lifeRelHead);
      lifeRelHeadRepos.flush();
    }
    }

  @Override
  public void updateAll(List<LifeRelHead> lifeRelHead, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (lifeRelHead == null || lifeRelHead.size() == 0)
      throw new DBException(6);

    for (LifeRelHead t : lifeRelHead) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      lifeRelHead = lifeRelHeadReposDay.saveAll(lifeRelHead);	
      lifeRelHeadReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lifeRelHead = lifeRelHeadReposMon.saveAll(lifeRelHead);	
      lifeRelHeadReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lifeRelHead = lifeRelHeadReposHist.saveAll(lifeRelHead);
      lifeRelHeadReposHist.flush();
    }
    else {
      lifeRelHead = lifeRelHeadRepos.saveAll(lifeRelHead);
      lifeRelHeadRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<LifeRelHead> lifeRelHead, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (lifeRelHead == null || lifeRelHead.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      lifeRelHeadReposDay.deleteAll(lifeRelHead);	
      lifeRelHeadReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      lifeRelHeadReposMon.deleteAll(lifeRelHead);	
      lifeRelHeadReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      lifeRelHeadReposHist.deleteAll(lifeRelHead);
      lifeRelHeadReposHist.flush();
    }
    else {
      lifeRelHeadRepos.deleteAll(lifeRelHead);
      lifeRelHeadRepos.flush();
    }
  }

}
