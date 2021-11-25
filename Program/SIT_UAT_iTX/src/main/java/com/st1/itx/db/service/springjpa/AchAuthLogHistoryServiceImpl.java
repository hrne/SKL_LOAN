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
import com.st1.itx.db.domain.AchAuthLogHistory;
import com.st1.itx.db.repository.online.AchAuthLogHistoryRepository;
import com.st1.itx.db.repository.day.AchAuthLogHistoryRepositoryDay;
import com.st1.itx.db.repository.mon.AchAuthLogHistoryRepositoryMon;
import com.st1.itx.db.repository.hist.AchAuthLogHistoryRepositoryHist;
import com.st1.itx.db.service.AchAuthLogHistoryService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("achAuthLogHistoryService")
@Repository
public class AchAuthLogHistoryServiceImpl extends ASpringJpaParm implements AchAuthLogHistoryService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private AchAuthLogHistoryRepository achAuthLogHistoryRepos;

  @Autowired
  private AchAuthLogHistoryRepositoryDay achAuthLogHistoryReposDay;

  @Autowired
  private AchAuthLogHistoryRepositoryMon achAuthLogHistoryReposMon;

  @Autowired
  private AchAuthLogHistoryRepositoryHist achAuthLogHistoryReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(achAuthLogHistoryRepos);
    org.junit.Assert.assertNotNull(achAuthLogHistoryReposDay);
    org.junit.Assert.assertNotNull(achAuthLogHistoryReposMon);
    org.junit.Assert.assertNotNull(achAuthLogHistoryReposHist);
  }

  @Override
  public AchAuthLogHistory findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<AchAuthLogHistory> achAuthLogHistory = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistory = achAuthLogHistoryReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistory = achAuthLogHistoryReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistory = achAuthLogHistoryReposHist.findById(logNo);
    else 
      achAuthLogHistory = achAuthLogHistoryRepos.findById(logNo);
    AchAuthLogHistory obj = achAuthLogHistory.isPresent() ? achAuthLogHistory.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<AchAuthLogHistory> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAll(pageable);
    else 
      slice = achAuthLogHistoryRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public Slice<AchAuthLogHistory> facmNoEq(int custNo_0, int facmNo_1, int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<AchAuthLogHistory> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
     Pageable pageable = null;

    if(limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
    else
         pageable = PageRequest.of(index, limit);
    this.info("facmNoEq " + dbName + " : " + "custNo_0 : " + custNo_0 + " facmNo_1 : " +  facmNo_1);
    if (dbName.equals(ContentName.onDay))
      slice = achAuthLogHistoryReposDay.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = achAuthLogHistoryReposMon.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = achAuthLogHistoryReposHist.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);
    else 
      slice = achAuthLogHistoryRepos.findAllByCustNoIsAndFacmNoIsOrderByAuthCreateDateDescCreateDateDesc(custNo_0, facmNo_1, pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public AchAuthLogHistory holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<AchAuthLogHistory> achAuthLogHistory = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistory = achAuthLogHistoryReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistory = achAuthLogHistoryReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistory = achAuthLogHistoryReposHist.findByLogNo(logNo);
    else 
      achAuthLogHistory = achAuthLogHistoryRepos.findByLogNo(logNo);
    return achAuthLogHistory.isPresent() ? achAuthLogHistory.get() : null;
  }

  @Override
  public AchAuthLogHistory holdById(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + achAuthLogHistory.getLogNo());
    Optional<AchAuthLogHistory> achAuthLogHistoryT = null;
    if (dbName.equals(ContentName.onDay))
      achAuthLogHistoryT = achAuthLogHistoryReposDay.findByLogNo(achAuthLogHistory.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistoryT = achAuthLogHistoryReposMon.findByLogNo(achAuthLogHistory.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      achAuthLogHistoryT = achAuthLogHistoryReposHist.findByLogNo(achAuthLogHistory.getLogNo());
    else 
      achAuthLogHistoryT = achAuthLogHistoryRepos.findByLogNo(achAuthLogHistory.getLogNo());
    return achAuthLogHistoryT.isPresent() ? achAuthLogHistoryT.get() : null;
  }

  @Override
  public AchAuthLogHistory insert(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + achAuthLogHistory.getLogNo());
    if (this.findById(achAuthLogHistory.getLogNo()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      achAuthLogHistory.setCreateEmpNo(empNot);

    if(achAuthLogHistory.getLastUpdateEmpNo() == null || achAuthLogHistory.getLastUpdateEmpNo().isEmpty())
      achAuthLogHistory.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return achAuthLogHistoryReposDay.saveAndFlush(achAuthLogHistory);	
    else if (dbName.equals(ContentName.onMon))
      return achAuthLogHistoryReposMon.saveAndFlush(achAuthLogHistory);
    else if (dbName.equals(ContentName.onHist))
      return achAuthLogHistoryReposHist.saveAndFlush(achAuthLogHistory);
    else 
    return achAuthLogHistoryRepos.saveAndFlush(achAuthLogHistory);
  }

  @Override
  public AchAuthLogHistory update(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + achAuthLogHistory.getLogNo());
    if (!empNot.isEmpty())
      achAuthLogHistory.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return achAuthLogHistoryReposDay.saveAndFlush(achAuthLogHistory);	
    else if (dbName.equals(ContentName.onMon))
      return achAuthLogHistoryReposMon.saveAndFlush(achAuthLogHistory);
    else if (dbName.equals(ContentName.onHist))
      return achAuthLogHistoryReposHist.saveAndFlush(achAuthLogHistory);
    else 
    return achAuthLogHistoryRepos.saveAndFlush(achAuthLogHistory);
  }

  @Override
  public AchAuthLogHistory update2(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + achAuthLogHistory.getLogNo());
    if (!empNot.isEmpty())
      achAuthLogHistory.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      achAuthLogHistoryReposDay.saveAndFlush(achAuthLogHistory);	
    else if (dbName.equals(ContentName.onMon))
      achAuthLogHistoryReposMon.saveAndFlush(achAuthLogHistory);
    else if (dbName.equals(ContentName.onHist))
        achAuthLogHistoryReposHist.saveAndFlush(achAuthLogHistory);
    else 
      achAuthLogHistoryRepos.saveAndFlush(achAuthLogHistory);	
    return this.findById(achAuthLogHistory.getLogNo());
  }

  @Override
  public void delete(AchAuthLogHistory achAuthLogHistory, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + achAuthLogHistory.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      achAuthLogHistoryReposDay.delete(achAuthLogHistory);	
      achAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLogHistoryReposMon.delete(achAuthLogHistory);	
      achAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLogHistoryReposHist.delete(achAuthLogHistory);
      achAuthLogHistoryReposHist.flush();
    }
    else {
      achAuthLogHistoryRepos.delete(achAuthLogHistory);
      achAuthLogHistoryRepos.flush();
    }
   }

  @Override
  public void insertAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException {
    if (achAuthLogHistory == null || achAuthLogHistory.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (AchAuthLogHistory t : achAuthLogHistory){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      achAuthLogHistory = achAuthLogHistoryReposDay.saveAll(achAuthLogHistory);	
      achAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLogHistory = achAuthLogHistoryReposMon.saveAll(achAuthLogHistory);	
      achAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLogHistory = achAuthLogHistoryReposHist.saveAll(achAuthLogHistory);
      achAuthLogHistoryReposHist.flush();
    }
    else {
      achAuthLogHistory = achAuthLogHistoryRepos.saveAll(achAuthLogHistory);
      achAuthLogHistoryRepos.flush();
    }
    }

  @Override
  public void updateAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (achAuthLogHistory == null || achAuthLogHistory.size() == 0)
      throw new DBException(6);

    for (AchAuthLogHistory t : achAuthLogHistory) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      achAuthLogHistory = achAuthLogHistoryReposDay.saveAll(achAuthLogHistory);	
      achAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLogHistory = achAuthLogHistoryReposMon.saveAll(achAuthLogHistory);	
      achAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLogHistory = achAuthLogHistoryReposHist.saveAll(achAuthLogHistory);
      achAuthLogHistoryReposHist.flush();
    }
    else {
      achAuthLogHistory = achAuthLogHistoryRepos.saveAll(achAuthLogHistory);
      achAuthLogHistoryRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<AchAuthLogHistory> achAuthLogHistory, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (achAuthLogHistory == null || achAuthLogHistory.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      achAuthLogHistoryReposDay.deleteAll(achAuthLogHistory);	
      achAuthLogHistoryReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      achAuthLogHistoryReposMon.deleteAll(achAuthLogHistory);	
      achAuthLogHistoryReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      achAuthLogHistoryReposHist.deleteAll(achAuthLogHistory);
      achAuthLogHistoryReposHist.flush();
    }
    else {
      achAuthLogHistoryRepos.deleteAll(achAuthLogHistory);
      achAuthLogHistoryRepos.flush();
    }
  }

}
