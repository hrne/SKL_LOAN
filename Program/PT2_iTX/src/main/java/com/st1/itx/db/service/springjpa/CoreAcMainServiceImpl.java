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
import com.st1.itx.db.domain.CoreAcMain;
import com.st1.itx.db.domain.CoreAcMainId;
import com.st1.itx.db.repository.online.CoreAcMainRepository;
import com.st1.itx.db.repository.day.CoreAcMainRepositoryDay;
import com.st1.itx.db.repository.mon.CoreAcMainRepositoryMon;
import com.st1.itx.db.repository.hist.CoreAcMainRepositoryHist;
import com.st1.itx.db.service.CoreAcMainService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("coreAcMainService")
@Repository
public class CoreAcMainServiceImpl extends ASpringJpaParm implements CoreAcMainService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private CoreAcMainRepository coreAcMainRepos;

  @Autowired
  private CoreAcMainRepositoryDay coreAcMainReposDay;

  @Autowired
  private CoreAcMainRepositoryMon coreAcMainReposMon;

  @Autowired
  private CoreAcMainRepositoryHist coreAcMainReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(coreAcMainRepos);
    org.junit.Assert.assertNotNull(coreAcMainReposDay);
    org.junit.Assert.assertNotNull(coreAcMainReposMon);
    org.junit.Assert.assertNotNull(coreAcMainReposHist);
  }

  @Override
  public CoreAcMain findById(CoreAcMainId coreAcMainId, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + coreAcMainId);
    Optional<CoreAcMain> coreAcMain = null;
    if (dbName.equals(ContentName.onDay))
      coreAcMain = coreAcMainReposDay.findById(coreAcMainId);
    else if (dbName.equals(ContentName.onMon))
      coreAcMain = coreAcMainReposMon.findById(coreAcMainId);
    else if (dbName.equals(ContentName.onHist))
      coreAcMain = coreAcMainReposHist.findById(coreAcMainId);
    else 
      coreAcMain = coreAcMainRepos.findById(coreAcMainId);
    CoreAcMain obj = coreAcMain.isPresent() ? coreAcMain.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<CoreAcMain> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CoreAcMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "AcBookCode", "AcSubBookCode", "CurrencyCode", "AcNoCode", "AcSubCode", "AcDate"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "AcBookCode", "AcSubBookCode", "CurrencyCode", "AcNoCode", "AcSubCode", "AcDate"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = coreAcMainReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = coreAcMainReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = coreAcMainReposHist.findAll(pageable);
    else 
      slice = coreAcMainRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<CoreAcMain> findByAcDate(int acDate_0, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<CoreAcMain> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("findByAcDate " + dbName + " : " + "acDate_0 : " + acDate_0);
    if (dbName.equals(ContentName.onDay))
      slice = coreAcMainReposDay.findAllByAcDateIs(acDate_0, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = coreAcMainReposMon.findAllByAcDateIs(acDate_0, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = coreAcMainReposHist.findAllByAcDateIs(acDate_0, pageable);
    else 
      slice = coreAcMainRepos.findAllByAcDateIs(acDate_0, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public CoreAcMain holdById(CoreAcMainId coreAcMainId, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + coreAcMainId);
    Optional<CoreAcMain> coreAcMain = null;
    if (dbName.equals(ContentName.onDay))
      coreAcMain = coreAcMainReposDay.findByCoreAcMainId(coreAcMainId);
    else if (dbName.equals(ContentName.onMon))
      coreAcMain = coreAcMainReposMon.findByCoreAcMainId(coreAcMainId);
    else if (dbName.equals(ContentName.onHist))
      coreAcMain = coreAcMainReposHist.findByCoreAcMainId(coreAcMainId);
    else 
      coreAcMain = coreAcMainRepos.findByCoreAcMainId(coreAcMainId);
    return coreAcMain.isPresent() ? coreAcMain.get() : null;
  }

  @Override
  public CoreAcMain holdById(CoreAcMain coreAcMain, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + coreAcMain.getCoreAcMainId());
    Optional<CoreAcMain> coreAcMainT = null;
    if (dbName.equals(ContentName.onDay))
      coreAcMainT = coreAcMainReposDay.findByCoreAcMainId(coreAcMain.getCoreAcMainId());
    else if (dbName.equals(ContentName.onMon))
      coreAcMainT = coreAcMainReposMon.findByCoreAcMainId(coreAcMain.getCoreAcMainId());
    else if (dbName.equals(ContentName.onHist))
      coreAcMainT = coreAcMainReposHist.findByCoreAcMainId(coreAcMain.getCoreAcMainId());
    else 
      coreAcMainT = coreAcMainRepos.findByCoreAcMainId(coreAcMain.getCoreAcMainId());
    return coreAcMainT.isPresent() ? coreAcMainT.get() : null;
  }

  @Override
  public CoreAcMain insert(CoreAcMain coreAcMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + coreAcMain.getCoreAcMainId());
    if (this.findById(coreAcMain.getCoreAcMainId(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      coreAcMain.setCreateEmpNo(empNot);

    if(coreAcMain.getLastUpdateEmpNo() == null || coreAcMain.getLastUpdateEmpNo().isEmpty())
      coreAcMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return coreAcMainReposDay.saveAndFlush(coreAcMain);	
    else if (dbName.equals(ContentName.onMon))
      return coreAcMainReposMon.saveAndFlush(coreAcMain);
    else if (dbName.equals(ContentName.onHist))
      return coreAcMainReposHist.saveAndFlush(coreAcMain);
    else 
    return coreAcMainRepos.saveAndFlush(coreAcMain);
  }

  @Override
  public CoreAcMain update(CoreAcMain coreAcMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + coreAcMain.getCoreAcMainId());
    if (!empNot.isEmpty())
      coreAcMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return coreAcMainReposDay.saveAndFlush(coreAcMain);	
    else if (dbName.equals(ContentName.onMon))
      return coreAcMainReposMon.saveAndFlush(coreAcMain);
    else if (dbName.equals(ContentName.onHist))
      return coreAcMainReposHist.saveAndFlush(coreAcMain);
    else 
    return coreAcMainRepos.saveAndFlush(coreAcMain);
  }

  @Override
  public CoreAcMain update2(CoreAcMain coreAcMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + coreAcMain.getCoreAcMainId());
    if (!empNot.isEmpty())
      coreAcMain.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      coreAcMainReposDay.saveAndFlush(coreAcMain);	
    else if (dbName.equals(ContentName.onMon))
      coreAcMainReposMon.saveAndFlush(coreAcMain);
    else if (dbName.equals(ContentName.onHist))
        coreAcMainReposHist.saveAndFlush(coreAcMain);
    else 
      coreAcMainRepos.saveAndFlush(coreAcMain);	
    return this.findById(coreAcMain.getCoreAcMainId());
  }

  @Override
  public void delete(CoreAcMain coreAcMain, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + coreAcMain.getCoreAcMainId());
    if (dbName.equals(ContentName.onDay)) {
      coreAcMainReposDay.delete(coreAcMain);	
      coreAcMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      coreAcMainReposMon.delete(coreAcMain);	
      coreAcMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      coreAcMainReposHist.delete(coreAcMain);
      coreAcMainReposHist.flush();
    }
    else {
      coreAcMainRepos.delete(coreAcMain);
      coreAcMainRepos.flush();
    }
   }

  @Override
  public void insertAll(List<CoreAcMain> coreAcMain, TitaVo... titaVo) throws DBException {
    if (coreAcMain == null || coreAcMain.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (CoreAcMain t : coreAcMain){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      coreAcMain = coreAcMainReposDay.saveAll(coreAcMain);	
      coreAcMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      coreAcMain = coreAcMainReposMon.saveAll(coreAcMain);	
      coreAcMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      coreAcMain = coreAcMainReposHist.saveAll(coreAcMain);
      coreAcMainReposHist.flush();
    }
    else {
      coreAcMain = coreAcMainRepos.saveAll(coreAcMain);
      coreAcMainRepos.flush();
    }
    }

  @Override
  public void updateAll(List<CoreAcMain> coreAcMain, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (coreAcMain == null || coreAcMain.size() == 0)
      throw new DBException(6);

    for (CoreAcMain t : coreAcMain) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      coreAcMain = coreAcMainReposDay.saveAll(coreAcMain);	
      coreAcMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      coreAcMain = coreAcMainReposMon.saveAll(coreAcMain);	
      coreAcMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      coreAcMain = coreAcMainReposHist.saveAll(coreAcMain);
      coreAcMainReposHist.flush();
    }
    else {
      coreAcMain = coreAcMainRepos.saveAll(coreAcMain);
      coreAcMainRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<CoreAcMain> coreAcMain, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (coreAcMain == null || coreAcMain.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      coreAcMainReposDay.deleteAll(coreAcMain);	
      coreAcMainReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      coreAcMainReposMon.deleteAll(coreAcMain);	
      coreAcMainReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      coreAcMainReposHist.deleteAll(coreAcMain);
      coreAcMainReposHist.flush();
    }
    else {
      coreAcMainRepos.deleteAll(coreAcMain);
      coreAcMainRepos.flush();
    }
  }

}
