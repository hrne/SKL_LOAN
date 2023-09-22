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
import com.st1.itx.db.domain.L7206Cust;
import com.st1.itx.db.repository.online.L7206CustRepository;
import com.st1.itx.db.repository.day.L7206CustRepositoryDay;
import com.st1.itx.db.repository.mon.L7206CustRepositoryMon;
import com.st1.itx.db.repository.hist.L7206CustRepositoryHist;
import com.st1.itx.db.service.L7206CustService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("l7206CustService")
@Repository
public class L7206CustServiceImpl extends ASpringJpaParm implements L7206CustService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private L7206CustRepository l7206CustRepos;

  @Autowired
  private L7206CustRepositoryDay l7206CustReposDay;

  @Autowired
  private L7206CustRepositoryMon l7206CustReposMon;

  @Autowired
  private L7206CustRepositoryHist l7206CustReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(l7206CustRepos);
    org.junit.Assert.assertNotNull(l7206CustReposDay);
    org.junit.Assert.assertNotNull(l7206CustReposMon);
    org.junit.Assert.assertNotNull(l7206CustReposHist);
  }

  @Override
  public L7206Cust findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<L7206Cust> l7206Cust = null;
    if (dbName.equals(ContentName.onDay))
      l7206Cust = l7206CustReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      l7206Cust = l7206CustReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      l7206Cust = l7206CustReposHist.findById(logNo);
    else 
      l7206Cust = l7206CustRepos.findById(logNo);
    L7206Cust obj = l7206Cust.isPresent() ? l7206Cust.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<L7206Cust> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<L7206Cust> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = l7206CustReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = l7206CustReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = l7206CustReposHist.findAll(pageable);
    else 
      slice = l7206CustRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public L7206Cust holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<L7206Cust> l7206Cust = null;
    if (dbName.equals(ContentName.onDay))
      l7206Cust = l7206CustReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      l7206Cust = l7206CustReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      l7206Cust = l7206CustReposHist.findByLogNo(logNo);
    else 
      l7206Cust = l7206CustRepos.findByLogNo(logNo);
    return l7206Cust.isPresent() ? l7206Cust.get() : null;
  }

  @Override
  public L7206Cust holdById(L7206Cust l7206Cust, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + l7206Cust.getLogNo());
    Optional<L7206Cust> l7206CustT = null;
    if (dbName.equals(ContentName.onDay))
      l7206CustT = l7206CustReposDay.findByLogNo(l7206Cust.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      l7206CustT = l7206CustReposMon.findByLogNo(l7206Cust.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      l7206CustT = l7206CustReposHist.findByLogNo(l7206Cust.getLogNo());
    else 
      l7206CustT = l7206CustRepos.findByLogNo(l7206Cust.getLogNo());
    return l7206CustT.isPresent() ? l7206CustT.get() : null;
  }

  @Override
  public L7206Cust insert(L7206Cust l7206Cust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + l7206Cust.getLogNo());
    if (this.findById(l7206Cust.getLogNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      l7206Cust.setCreateEmpNo(empNot);

    if(l7206Cust.getLastUpdateEmpNo() == null || l7206Cust.getLastUpdateEmpNo().isEmpty())
      l7206Cust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return l7206CustReposDay.saveAndFlush(l7206Cust);	
    else if (dbName.equals(ContentName.onMon))
      return l7206CustReposMon.saveAndFlush(l7206Cust);
    else if (dbName.equals(ContentName.onHist))
      return l7206CustReposHist.saveAndFlush(l7206Cust);
    else 
    return l7206CustRepos.saveAndFlush(l7206Cust);
  }

  @Override
  public L7206Cust update(L7206Cust l7206Cust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + l7206Cust.getLogNo());
    if (!empNot.isEmpty())
      l7206Cust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return l7206CustReposDay.saveAndFlush(l7206Cust);	
    else if (dbName.equals(ContentName.onMon))
      return l7206CustReposMon.saveAndFlush(l7206Cust);
    else if (dbName.equals(ContentName.onHist))
      return l7206CustReposHist.saveAndFlush(l7206Cust);
    else 
    return l7206CustRepos.saveAndFlush(l7206Cust);
  }

  @Override
  public L7206Cust update2(L7206Cust l7206Cust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + l7206Cust.getLogNo());
    if (!empNot.isEmpty())
      l7206Cust.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      l7206CustReposDay.saveAndFlush(l7206Cust);	
    else if (dbName.equals(ContentName.onMon))
      l7206CustReposMon.saveAndFlush(l7206Cust);
    else if (dbName.equals(ContentName.onHist))
        l7206CustReposHist.saveAndFlush(l7206Cust);
    else 
      l7206CustRepos.saveAndFlush(l7206Cust);	
    return this.findById(l7206Cust.getLogNo());
  }

  @Override
  public void delete(L7206Cust l7206Cust, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + l7206Cust.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      l7206CustReposDay.delete(l7206Cust);	
      l7206CustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206CustReposMon.delete(l7206Cust);	
      l7206CustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206CustReposHist.delete(l7206Cust);
      l7206CustReposHist.flush();
    }
    else {
      l7206CustRepos.delete(l7206Cust);
      l7206CustRepos.flush();
    }
   }

  @Override
  public void insertAll(List<L7206Cust> l7206Cust, TitaVo... titaVo) throws DBException {
    if (l7206Cust == null || l7206Cust.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (L7206Cust t : l7206Cust){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      l7206Cust = l7206CustReposDay.saveAll(l7206Cust);	
      l7206CustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206Cust = l7206CustReposMon.saveAll(l7206Cust);	
      l7206CustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206Cust = l7206CustReposHist.saveAll(l7206Cust);
      l7206CustReposHist.flush();
    }
    else {
      l7206Cust = l7206CustRepos.saveAll(l7206Cust);
      l7206CustRepos.flush();
    }
    }

  @Override
  public void updateAll(List<L7206Cust> l7206Cust, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (l7206Cust == null || l7206Cust.size() == 0)
      throw new DBException(6);

    for (L7206Cust t : l7206Cust) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      l7206Cust = l7206CustReposDay.saveAll(l7206Cust);	
      l7206CustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206Cust = l7206CustReposMon.saveAll(l7206Cust);	
      l7206CustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206Cust = l7206CustReposHist.saveAll(l7206Cust);
      l7206CustReposHist.flush();
    }
    else {
      l7206Cust = l7206CustRepos.saveAll(l7206Cust);
      l7206CustRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<L7206Cust> l7206Cust, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (l7206Cust == null || l7206Cust.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      l7206CustReposDay.deleteAll(l7206Cust);	
      l7206CustReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206CustReposMon.deleteAll(l7206Cust);	
      l7206CustReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206CustReposHist.deleteAll(l7206Cust);
      l7206CustReposHist.flush();
    }
    else {
      l7206CustRepos.deleteAll(l7206Cust);
      l7206CustRepos.flush();
    }
  }

  @Override
  public void Usp_L7_L7206Cust_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      l7206CustReposDay.uspL7L7206custIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      l7206CustReposMon.uspL7L7206custIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      l7206CustReposHist.uspL7L7206custIns(EmpNo);
   else
      l7206CustRepos.uspL7L7206custIns(EmpNo);
  }

}
