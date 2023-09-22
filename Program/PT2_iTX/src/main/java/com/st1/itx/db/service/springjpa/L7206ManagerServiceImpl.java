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
import com.st1.itx.db.domain.L7206Manager;
import com.st1.itx.db.repository.online.L7206ManagerRepository;
import com.st1.itx.db.repository.day.L7206ManagerRepositoryDay;
import com.st1.itx.db.repository.mon.L7206ManagerRepositoryMon;
import com.st1.itx.db.repository.hist.L7206ManagerRepositoryHist;
import com.st1.itx.db.service.L7206ManagerService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("l7206ManagerService")
@Repository
public class L7206ManagerServiceImpl extends ASpringJpaParm implements L7206ManagerService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private L7206ManagerRepository l7206ManagerRepos;

  @Autowired
  private L7206ManagerRepositoryDay l7206ManagerReposDay;

  @Autowired
  private L7206ManagerRepositoryMon l7206ManagerReposMon;

  @Autowired
  private L7206ManagerRepositoryHist l7206ManagerReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(l7206ManagerRepos);
    org.junit.Assert.assertNotNull(l7206ManagerReposDay);
    org.junit.Assert.assertNotNull(l7206ManagerReposMon);
    org.junit.Assert.assertNotNull(l7206ManagerReposHist);
  }

  @Override
  public L7206Manager findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<L7206Manager> l7206Manager = null;
    if (dbName.equals(ContentName.onDay))
      l7206Manager = l7206ManagerReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      l7206Manager = l7206ManagerReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      l7206Manager = l7206ManagerReposHist.findById(logNo);
    else 
      l7206Manager = l7206ManagerRepos.findById(logNo);
    L7206Manager obj = l7206Manager.isPresent() ? l7206Manager.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<L7206Manager> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<L7206Manager> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = l7206ManagerReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = l7206ManagerReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = l7206ManagerReposHist.findAll(pageable);
    else 
      slice = l7206ManagerRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public L7206Manager holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<L7206Manager> l7206Manager = null;
    if (dbName.equals(ContentName.onDay))
      l7206Manager = l7206ManagerReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      l7206Manager = l7206ManagerReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      l7206Manager = l7206ManagerReposHist.findByLogNo(logNo);
    else 
      l7206Manager = l7206ManagerRepos.findByLogNo(logNo);
    return l7206Manager.isPresent() ? l7206Manager.get() : null;
  }

  @Override
  public L7206Manager holdById(L7206Manager l7206Manager, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + l7206Manager.getLogNo());
    Optional<L7206Manager> l7206ManagerT = null;
    if (dbName.equals(ContentName.onDay))
      l7206ManagerT = l7206ManagerReposDay.findByLogNo(l7206Manager.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      l7206ManagerT = l7206ManagerReposMon.findByLogNo(l7206Manager.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      l7206ManagerT = l7206ManagerReposHist.findByLogNo(l7206Manager.getLogNo());
    else 
      l7206ManagerT = l7206ManagerRepos.findByLogNo(l7206Manager.getLogNo());
    return l7206ManagerT.isPresent() ? l7206ManagerT.get() : null;
  }

  @Override
  public L7206Manager insert(L7206Manager l7206Manager, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + l7206Manager.getLogNo());
    if (this.findById(l7206Manager.getLogNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      l7206Manager.setCreateEmpNo(empNot);

    if(l7206Manager.getLastUpdateEmpNo() == null || l7206Manager.getLastUpdateEmpNo().isEmpty())
      l7206Manager.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return l7206ManagerReposDay.saveAndFlush(l7206Manager);	
    else if (dbName.equals(ContentName.onMon))
      return l7206ManagerReposMon.saveAndFlush(l7206Manager);
    else if (dbName.equals(ContentName.onHist))
      return l7206ManagerReposHist.saveAndFlush(l7206Manager);
    else 
    return l7206ManagerRepos.saveAndFlush(l7206Manager);
  }

  @Override
  public L7206Manager update(L7206Manager l7206Manager, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + l7206Manager.getLogNo());
    if (!empNot.isEmpty())
      l7206Manager.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return l7206ManagerReposDay.saveAndFlush(l7206Manager);	
    else if (dbName.equals(ContentName.onMon))
      return l7206ManagerReposMon.saveAndFlush(l7206Manager);
    else if (dbName.equals(ContentName.onHist))
      return l7206ManagerReposHist.saveAndFlush(l7206Manager);
    else 
    return l7206ManagerRepos.saveAndFlush(l7206Manager);
  }

  @Override
  public L7206Manager update2(L7206Manager l7206Manager, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + l7206Manager.getLogNo());
    if (!empNot.isEmpty())
      l7206Manager.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      l7206ManagerReposDay.saveAndFlush(l7206Manager);	
    else if (dbName.equals(ContentName.onMon))
      l7206ManagerReposMon.saveAndFlush(l7206Manager);
    else if (dbName.equals(ContentName.onHist))
        l7206ManagerReposHist.saveAndFlush(l7206Manager);
    else 
      l7206ManagerRepos.saveAndFlush(l7206Manager);	
    return this.findById(l7206Manager.getLogNo());
  }

  @Override
  public void delete(L7206Manager l7206Manager, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + l7206Manager.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      l7206ManagerReposDay.delete(l7206Manager);	
      l7206ManagerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206ManagerReposMon.delete(l7206Manager);	
      l7206ManagerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206ManagerReposHist.delete(l7206Manager);
      l7206ManagerReposHist.flush();
    }
    else {
      l7206ManagerRepos.delete(l7206Manager);
      l7206ManagerRepos.flush();
    }
   }

  @Override
  public void insertAll(List<L7206Manager> l7206Manager, TitaVo... titaVo) throws DBException {
    if (l7206Manager == null || l7206Manager.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (L7206Manager t : l7206Manager){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      l7206Manager = l7206ManagerReposDay.saveAll(l7206Manager);	
      l7206ManagerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206Manager = l7206ManagerReposMon.saveAll(l7206Manager);	
      l7206ManagerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206Manager = l7206ManagerReposHist.saveAll(l7206Manager);
      l7206ManagerReposHist.flush();
    }
    else {
      l7206Manager = l7206ManagerRepos.saveAll(l7206Manager);
      l7206ManagerRepos.flush();
    }
    }

  @Override
  public void updateAll(List<L7206Manager> l7206Manager, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (l7206Manager == null || l7206Manager.size() == 0)
      throw new DBException(6);

    for (L7206Manager t : l7206Manager) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      l7206Manager = l7206ManagerReposDay.saveAll(l7206Manager);	
      l7206ManagerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206Manager = l7206ManagerReposMon.saveAll(l7206Manager);	
      l7206ManagerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206Manager = l7206ManagerReposHist.saveAll(l7206Manager);
      l7206ManagerReposHist.flush();
    }
    else {
      l7206Manager = l7206ManagerRepos.saveAll(l7206Manager);
      l7206ManagerRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<L7206Manager> l7206Manager, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (l7206Manager == null || l7206Manager.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      l7206ManagerReposDay.deleteAll(l7206Manager);	
      l7206ManagerReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206ManagerReposMon.deleteAll(l7206Manager);	
      l7206ManagerReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206ManagerReposHist.deleteAll(l7206Manager);
      l7206ManagerReposHist.flush();
    }
    else {
      l7206ManagerRepos.deleteAll(l7206Manager);
      l7206ManagerRepos.flush();
    }
  }

  @Override
  public void Usp_L7_L7206Manager_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      l7206ManagerReposDay.uspL7L7206managerIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      l7206ManagerReposMon.uspL7L7206managerIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      l7206ManagerReposHist.uspL7L7206managerIns(EmpNo);
   else
      l7206ManagerRepos.uspL7L7206managerIns(EmpNo);
  }

}
