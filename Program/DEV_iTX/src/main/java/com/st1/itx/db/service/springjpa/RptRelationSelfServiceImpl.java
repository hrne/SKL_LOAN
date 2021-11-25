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
import com.st1.itx.db.domain.RptRelationSelf;
import com.st1.itx.db.domain.RptRelationSelfId;
import com.st1.itx.db.repository.online.RptRelationSelfRepository;
import com.st1.itx.db.repository.day.RptRelationSelfRepositoryDay;
import com.st1.itx.db.repository.mon.RptRelationSelfRepositoryMon;
import com.st1.itx.db.repository.hist.RptRelationSelfRepositoryHist;
import com.st1.itx.db.service.RptRelationSelfService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("rptRelationSelfService")
@Repository
public class RptRelationSelfServiceImpl extends ASpringJpaParm implements RptRelationSelfService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private RptRelationSelfRepository rptRelationSelfRepos;

  @Autowired
  private RptRelationSelfRepositoryDay rptRelationSelfReposDay;

  @Autowired
  private RptRelationSelfRepositoryMon rptRelationSelfReposMon;

  @Autowired
  private RptRelationSelfRepositoryHist rptRelationSelfReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(rptRelationSelfRepos);
    org.junit.Assert.assertNotNull(rptRelationSelfReposDay);
    org.junit.Assert.assertNotNull(rptRelationSelfReposMon);
    org.junit.Assert.assertNotNull(rptRelationSelfReposHist);
  }

  @Override
  public RptRelationSelf findById(RptRelationSelfId rptRelationSelfId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + rptRelationSelfId);
    Optional<RptRelationSelf> rptRelationSelf = null;
    if (dbName.equals(ContentName.onDay))
      rptRelationSelf = rptRelationSelfReposDay.findById(rptRelationSelfId);
    else if (dbName.equals(ContentName.onMon))
      rptRelationSelf = rptRelationSelfReposMon.findById(rptRelationSelfId);
    else if (dbName.equals(ContentName.onHist))
      rptRelationSelf = rptRelationSelfReposHist.findById(rptRelationSelfId);
    else 
      rptRelationSelf = rptRelationSelfRepos.findById(rptRelationSelfId);
    RptRelationSelf obj = rptRelationSelf.isPresent() ? rptRelationSelf.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<RptRelationSelf> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<RptRelationSelf> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CusId", "STSCD", "CusSCD"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CusId", "STSCD", "CusSCD"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = rptRelationSelfReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = rptRelationSelfReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = rptRelationSelfReposHist.findAll(pageable);
    else 
      slice = rptRelationSelfRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public RptRelationSelf holdById(RptRelationSelfId rptRelationSelfId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + rptRelationSelfId);
    Optional<RptRelationSelf> rptRelationSelf = null;
    if (dbName.equals(ContentName.onDay))
      rptRelationSelf = rptRelationSelfReposDay.findByRptRelationSelfId(rptRelationSelfId);
    else if (dbName.equals(ContentName.onMon))
      rptRelationSelf = rptRelationSelfReposMon.findByRptRelationSelfId(rptRelationSelfId);
    else if (dbName.equals(ContentName.onHist))
      rptRelationSelf = rptRelationSelfReposHist.findByRptRelationSelfId(rptRelationSelfId);
    else 
      rptRelationSelf = rptRelationSelfRepos.findByRptRelationSelfId(rptRelationSelfId);
    return rptRelationSelf.isPresent() ? rptRelationSelf.get() : null;
  }

  @Override
  public RptRelationSelf holdById(RptRelationSelf rptRelationSelf, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + rptRelationSelf.getRptRelationSelfId());
    Optional<RptRelationSelf> rptRelationSelfT = null;
    if (dbName.equals(ContentName.onDay))
      rptRelationSelfT = rptRelationSelfReposDay.findByRptRelationSelfId(rptRelationSelf.getRptRelationSelfId());
    else if (dbName.equals(ContentName.onMon))
      rptRelationSelfT = rptRelationSelfReposMon.findByRptRelationSelfId(rptRelationSelf.getRptRelationSelfId());
    else if (dbName.equals(ContentName.onHist))
      rptRelationSelfT = rptRelationSelfReposHist.findByRptRelationSelfId(rptRelationSelf.getRptRelationSelfId());
    else 
      rptRelationSelfT = rptRelationSelfRepos.findByRptRelationSelfId(rptRelationSelf.getRptRelationSelfId());
    return rptRelationSelfT.isPresent() ? rptRelationSelfT.get() : null;
  }

  @Override
  public RptRelationSelf insert(RptRelationSelf rptRelationSelf, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + rptRelationSelf.getRptRelationSelfId());
    if (this.findById(rptRelationSelf.getRptRelationSelfId()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      rptRelationSelf.setCreateEmpNo(empNot);

    if(rptRelationSelf.getLastUpdateEmpNo() == null || rptRelationSelf.getLastUpdateEmpNo().isEmpty())
      rptRelationSelf.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return rptRelationSelfReposDay.saveAndFlush(rptRelationSelf);	
    else if (dbName.equals(ContentName.onMon))
      return rptRelationSelfReposMon.saveAndFlush(rptRelationSelf);
    else if (dbName.equals(ContentName.onHist))
      return rptRelationSelfReposHist.saveAndFlush(rptRelationSelf);
    else 
    return rptRelationSelfRepos.saveAndFlush(rptRelationSelf);
  }

  @Override
  public RptRelationSelf update(RptRelationSelf rptRelationSelf, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + rptRelationSelf.getRptRelationSelfId());
    if (!empNot.isEmpty())
      rptRelationSelf.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return rptRelationSelfReposDay.saveAndFlush(rptRelationSelf);	
    else if (dbName.equals(ContentName.onMon))
      return rptRelationSelfReposMon.saveAndFlush(rptRelationSelf);
    else if (dbName.equals(ContentName.onHist))
      return rptRelationSelfReposHist.saveAndFlush(rptRelationSelf);
    else 
    return rptRelationSelfRepos.saveAndFlush(rptRelationSelf);
  }

  @Override
  public RptRelationSelf update2(RptRelationSelf rptRelationSelf, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + rptRelationSelf.getRptRelationSelfId());
    if (!empNot.isEmpty())
      rptRelationSelf.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      rptRelationSelfReposDay.saveAndFlush(rptRelationSelf);	
    else if (dbName.equals(ContentName.onMon))
      rptRelationSelfReposMon.saveAndFlush(rptRelationSelf);
    else if (dbName.equals(ContentName.onHist))
        rptRelationSelfReposHist.saveAndFlush(rptRelationSelf);
    else 
      rptRelationSelfRepos.saveAndFlush(rptRelationSelf);	
    return this.findById(rptRelationSelf.getRptRelationSelfId());
  }

  @Override
  public void delete(RptRelationSelf rptRelationSelf, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + rptRelationSelf.getRptRelationSelfId());
    if (dbName.equals(ContentName.onDay)) {
      rptRelationSelfReposDay.delete(rptRelationSelf);	
      rptRelationSelfReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptRelationSelfReposMon.delete(rptRelationSelf);	
      rptRelationSelfReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptRelationSelfReposHist.delete(rptRelationSelf);
      rptRelationSelfReposHist.flush();
    }
    else {
      rptRelationSelfRepos.delete(rptRelationSelf);
      rptRelationSelfRepos.flush();
    }
   }

  @Override
  public void insertAll(List<RptRelationSelf> rptRelationSelf, TitaVo... titaVo) throws DBException {
    if (rptRelationSelf == null || rptRelationSelf.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (RptRelationSelf t : rptRelationSelf){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      rptRelationSelf = rptRelationSelfReposDay.saveAll(rptRelationSelf);	
      rptRelationSelfReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptRelationSelf = rptRelationSelfReposMon.saveAll(rptRelationSelf);	
      rptRelationSelfReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptRelationSelf = rptRelationSelfReposHist.saveAll(rptRelationSelf);
      rptRelationSelfReposHist.flush();
    }
    else {
      rptRelationSelf = rptRelationSelfRepos.saveAll(rptRelationSelf);
      rptRelationSelfRepos.flush();
    }
    }

  @Override
  public void updateAll(List<RptRelationSelf> rptRelationSelf, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (rptRelationSelf == null || rptRelationSelf.size() == 0)
      throw new DBException(6);

    for (RptRelationSelf t : rptRelationSelf) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      rptRelationSelf = rptRelationSelfReposDay.saveAll(rptRelationSelf);	
      rptRelationSelfReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptRelationSelf = rptRelationSelfReposMon.saveAll(rptRelationSelf);	
      rptRelationSelfReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptRelationSelf = rptRelationSelfReposHist.saveAll(rptRelationSelf);
      rptRelationSelfReposHist.flush();
    }
    else {
      rptRelationSelf = rptRelationSelfRepos.saveAll(rptRelationSelf);
      rptRelationSelfRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<RptRelationSelf> rptRelationSelf, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (rptRelationSelf == null || rptRelationSelf.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      rptRelationSelfReposDay.deleteAll(rptRelationSelf);	
      rptRelationSelfReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      rptRelationSelfReposMon.deleteAll(rptRelationSelf);	
      rptRelationSelfReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      rptRelationSelfReposHist.deleteAll(rptRelationSelf);
      rptRelationSelfReposHist.flush();
    }
    else {
      rptRelationSelfRepos.deleteAll(rptRelationSelf);
      rptRelationSelfRepos.flush();
    }
  }

}
