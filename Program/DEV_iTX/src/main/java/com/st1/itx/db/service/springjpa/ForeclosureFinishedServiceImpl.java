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
import com.st1.itx.db.domain.ForeclosureFinished;
import com.st1.itx.db.domain.ForeclosureFinishedId;
import com.st1.itx.db.repository.online.ForeclosureFinishedRepository;
import com.st1.itx.db.repository.day.ForeclosureFinishedRepositoryDay;
import com.st1.itx.db.repository.mon.ForeclosureFinishedRepositoryMon;
import com.st1.itx.db.repository.hist.ForeclosureFinishedRepositoryHist;
import com.st1.itx.db.service.ForeclosureFinishedService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("foreclosureFinishedService")
@Repository
public class ForeclosureFinishedServiceImpl extends ASpringJpaParm implements ForeclosureFinishedService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private ForeclosureFinishedRepository foreclosureFinishedRepos;

  @Autowired
  private ForeclosureFinishedRepositoryDay foreclosureFinishedReposDay;

  @Autowired
  private ForeclosureFinishedRepositoryMon foreclosureFinishedReposMon;

  @Autowired
  private ForeclosureFinishedRepositoryHist foreclosureFinishedReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(foreclosureFinishedRepos);
    org.junit.Assert.assertNotNull(foreclosureFinishedReposDay);
    org.junit.Assert.assertNotNull(foreclosureFinishedReposMon);
    org.junit.Assert.assertNotNull(foreclosureFinishedReposHist);
  }

  @Override
  public ForeclosureFinished findById(ForeclosureFinishedId foreclosureFinishedId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + foreclosureFinishedId);
    Optional<ForeclosureFinished> foreclosureFinished = null;
    if (dbName.equals(ContentName.onDay))
      foreclosureFinished = foreclosureFinishedReposDay.findById(foreclosureFinishedId);
    else if (dbName.equals(ContentName.onMon))
      foreclosureFinished = foreclosureFinishedReposMon.findById(foreclosureFinishedId);
    else if (dbName.equals(ContentName.onHist))
      foreclosureFinished = foreclosureFinishedReposHist.findById(foreclosureFinishedId);
    else 
      foreclosureFinished = foreclosureFinishedRepos.findById(foreclosureFinishedId);
    ForeclosureFinished obj = foreclosureFinished.isPresent() ? foreclosureFinished.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<ForeclosureFinished> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<ForeclosureFinished> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo", "FacmNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = foreclosureFinishedReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = foreclosureFinishedReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = foreclosureFinishedReposHist.findAll(pageable);
    else 
      slice = foreclosureFinishedRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public ForeclosureFinished holdById(ForeclosureFinishedId foreclosureFinishedId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + foreclosureFinishedId);
    Optional<ForeclosureFinished> foreclosureFinished = null;
    if (dbName.equals(ContentName.onDay))
      foreclosureFinished = foreclosureFinishedReposDay.findByForeclosureFinishedId(foreclosureFinishedId);
    else if (dbName.equals(ContentName.onMon))
      foreclosureFinished = foreclosureFinishedReposMon.findByForeclosureFinishedId(foreclosureFinishedId);
    else if (dbName.equals(ContentName.onHist))
      foreclosureFinished = foreclosureFinishedReposHist.findByForeclosureFinishedId(foreclosureFinishedId);
    else 
      foreclosureFinished = foreclosureFinishedRepos.findByForeclosureFinishedId(foreclosureFinishedId);
    return foreclosureFinished.isPresent() ? foreclosureFinished.get() : null;
  }

  @Override
  public ForeclosureFinished holdById(ForeclosureFinished foreclosureFinished, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + foreclosureFinished.getForeclosureFinishedId());
    Optional<ForeclosureFinished> foreclosureFinishedT = null;
    if (dbName.equals(ContentName.onDay))
      foreclosureFinishedT = foreclosureFinishedReposDay.findByForeclosureFinishedId(foreclosureFinished.getForeclosureFinishedId());
    else if (dbName.equals(ContentName.onMon))
      foreclosureFinishedT = foreclosureFinishedReposMon.findByForeclosureFinishedId(foreclosureFinished.getForeclosureFinishedId());
    else if (dbName.equals(ContentName.onHist))
      foreclosureFinishedT = foreclosureFinishedReposHist.findByForeclosureFinishedId(foreclosureFinished.getForeclosureFinishedId());
    else 
      foreclosureFinishedT = foreclosureFinishedRepos.findByForeclosureFinishedId(foreclosureFinished.getForeclosureFinishedId());
    return foreclosureFinishedT.isPresent() ? foreclosureFinishedT.get() : null;
  }

  @Override
  public ForeclosureFinished insert(ForeclosureFinished foreclosureFinished, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + foreclosureFinished.getForeclosureFinishedId());
    if (this.findById(foreclosureFinished.getForeclosureFinishedId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      foreclosureFinished.setCreateEmpNo(empNot);

    if(foreclosureFinished.getLastUpdateEmpNo() == null || foreclosureFinished.getLastUpdateEmpNo().isEmpty())
      foreclosureFinished.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return foreclosureFinishedReposDay.saveAndFlush(foreclosureFinished);	
    else if (dbName.equals(ContentName.onMon))
      return foreclosureFinishedReposMon.saveAndFlush(foreclosureFinished);
    else if (dbName.equals(ContentName.onHist))
      return foreclosureFinishedReposHist.saveAndFlush(foreclosureFinished);
    else 
    return foreclosureFinishedRepos.saveAndFlush(foreclosureFinished);
  }

  @Override
  public ForeclosureFinished update(ForeclosureFinished foreclosureFinished, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + foreclosureFinished.getForeclosureFinishedId());
    if (!empNot.isEmpty())
      foreclosureFinished.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return foreclosureFinishedReposDay.saveAndFlush(foreclosureFinished);	
    else if (dbName.equals(ContentName.onMon))
      return foreclosureFinishedReposMon.saveAndFlush(foreclosureFinished);
    else if (dbName.equals(ContentName.onHist))
      return foreclosureFinishedReposHist.saveAndFlush(foreclosureFinished);
    else 
    return foreclosureFinishedRepos.saveAndFlush(foreclosureFinished);
  }

  @Override
  public ForeclosureFinished update2(ForeclosureFinished foreclosureFinished, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + foreclosureFinished.getForeclosureFinishedId());
    if (!empNot.isEmpty())
      foreclosureFinished.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      foreclosureFinishedReposDay.saveAndFlush(foreclosureFinished);	
    else if (dbName.equals(ContentName.onMon))
      foreclosureFinishedReposMon.saveAndFlush(foreclosureFinished);
    else if (dbName.equals(ContentName.onHist))
        foreclosureFinishedReposHist.saveAndFlush(foreclosureFinished);
    else 
      foreclosureFinishedRepos.saveAndFlush(foreclosureFinished);	
    return this.findById(foreclosureFinished.getForeclosureFinishedId());
  }

  @Override
  public void delete(ForeclosureFinished foreclosureFinished, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + foreclosureFinished.getForeclosureFinishedId());
    if (dbName.equals(ContentName.onDay)) {
      foreclosureFinishedReposDay.delete(foreclosureFinished);	
      foreclosureFinishedReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      foreclosureFinishedReposMon.delete(foreclosureFinished);	
      foreclosureFinishedReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      foreclosureFinishedReposHist.delete(foreclosureFinished);
      foreclosureFinishedReposHist.flush();
    }
    else {
      foreclosureFinishedRepos.delete(foreclosureFinished);
      foreclosureFinishedRepos.flush();
    }
   }

  @Override
  public void insertAll(List<ForeclosureFinished> foreclosureFinished, TitaVo... titaVo) throws DBException {
    if (foreclosureFinished == null || foreclosureFinished.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (ForeclosureFinished t : foreclosureFinished){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      foreclosureFinished = foreclosureFinishedReposDay.saveAll(foreclosureFinished);	
      foreclosureFinishedReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      foreclosureFinished = foreclosureFinishedReposMon.saveAll(foreclosureFinished);	
      foreclosureFinishedReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      foreclosureFinished = foreclosureFinishedReposHist.saveAll(foreclosureFinished);
      foreclosureFinishedReposHist.flush();
    }
    else {
      foreclosureFinished = foreclosureFinishedRepos.saveAll(foreclosureFinished);
      foreclosureFinishedRepos.flush();
    }
    }

  @Override
  public void updateAll(List<ForeclosureFinished> foreclosureFinished, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (foreclosureFinished == null || foreclosureFinished.size() == 0)
      throw new DBException(6);

    for (ForeclosureFinished t : foreclosureFinished) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      foreclosureFinished = foreclosureFinishedReposDay.saveAll(foreclosureFinished);	
      foreclosureFinishedReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      foreclosureFinished = foreclosureFinishedReposMon.saveAll(foreclosureFinished);	
      foreclosureFinishedReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      foreclosureFinished = foreclosureFinishedReposHist.saveAll(foreclosureFinished);
      foreclosureFinishedReposHist.flush();
    }
    else {
      foreclosureFinished = foreclosureFinishedRepos.saveAll(foreclosureFinished);
      foreclosureFinishedRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<ForeclosureFinished> foreclosureFinished, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (foreclosureFinished == null || foreclosureFinished.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      foreclosureFinishedReposDay.deleteAll(foreclosureFinished);	
      foreclosureFinishedReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      foreclosureFinishedReposMon.deleteAll(foreclosureFinished);	
      foreclosureFinishedReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      foreclosureFinishedReposHist.deleteAll(foreclosureFinished);
      foreclosureFinishedReposHist.flush();
    }
    else {
      foreclosureFinishedRepos.deleteAll(foreclosureFinished);
      foreclosureFinishedRepos.flush();
    }
  }

}
