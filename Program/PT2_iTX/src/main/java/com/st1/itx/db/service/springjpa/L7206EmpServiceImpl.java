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
import com.st1.itx.db.domain.L7206Emp;
import com.st1.itx.db.repository.online.L7206EmpRepository;
import com.st1.itx.db.repository.day.L7206EmpRepositoryDay;
import com.st1.itx.db.repository.mon.L7206EmpRepositoryMon;
import com.st1.itx.db.repository.hist.L7206EmpRepositoryHist;
import com.st1.itx.db.service.L7206EmpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("l7206EmpService")
@Repository
public class L7206EmpServiceImpl extends ASpringJpaParm implements L7206EmpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private L7206EmpRepository l7206EmpRepos;

  @Autowired
  private L7206EmpRepositoryDay l7206EmpReposDay;

  @Autowired
  private L7206EmpRepositoryMon l7206EmpReposMon;

  @Autowired
  private L7206EmpRepositoryHist l7206EmpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(l7206EmpRepos);
    org.junit.Assert.assertNotNull(l7206EmpReposDay);
    org.junit.Assert.assertNotNull(l7206EmpReposMon);
    org.junit.Assert.assertNotNull(l7206EmpReposHist);
  }

  @Override
  public L7206Emp findById(Long logNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + logNo);
    Optional<L7206Emp> l7206Emp = null;
    if (dbName.equals(ContentName.onDay))
      l7206Emp = l7206EmpReposDay.findById(logNo);
    else if (dbName.equals(ContentName.onMon))
      l7206Emp = l7206EmpReposMon.findById(logNo);
    else if (dbName.equals(ContentName.onHist))
      l7206Emp = l7206EmpReposHist.findById(logNo);
    else 
      l7206Emp = l7206EmpRepos.findById(logNo);
    L7206Emp obj = l7206Emp.isPresent() ? l7206Emp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<L7206Emp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<L7206Emp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "LogNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "LogNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = l7206EmpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = l7206EmpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = l7206EmpReposHist.findAll(pageable);
    else 
      slice = l7206EmpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public L7206Emp holdById(Long logNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + logNo);
    Optional<L7206Emp> l7206Emp = null;
    if (dbName.equals(ContentName.onDay))
      l7206Emp = l7206EmpReposDay.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onMon))
      l7206Emp = l7206EmpReposMon.findByLogNo(logNo);
    else if (dbName.equals(ContentName.onHist))
      l7206Emp = l7206EmpReposHist.findByLogNo(logNo);
    else 
      l7206Emp = l7206EmpRepos.findByLogNo(logNo);
    return l7206Emp.isPresent() ? l7206Emp.get() : null;
  }

  @Override
  public L7206Emp holdById(L7206Emp l7206Emp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + l7206Emp.getLogNo());
    Optional<L7206Emp> l7206EmpT = null;
    if (dbName.equals(ContentName.onDay))
      l7206EmpT = l7206EmpReposDay.findByLogNo(l7206Emp.getLogNo());
    else if (dbName.equals(ContentName.onMon))
      l7206EmpT = l7206EmpReposMon.findByLogNo(l7206Emp.getLogNo());
    else if (dbName.equals(ContentName.onHist))
      l7206EmpT = l7206EmpReposHist.findByLogNo(l7206Emp.getLogNo());
    else 
      l7206EmpT = l7206EmpRepos.findByLogNo(l7206Emp.getLogNo());
    return l7206EmpT.isPresent() ? l7206EmpT.get() : null;
  }

  @Override
  public L7206Emp insert(L7206Emp l7206Emp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + l7206Emp.getLogNo());
    if (this.findById(l7206Emp.getLogNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      l7206Emp.setCreateEmpNo(empNot);

    if(l7206Emp.getLastUpdateEmpNo() == null || l7206Emp.getLastUpdateEmpNo().isEmpty())
      l7206Emp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return l7206EmpReposDay.saveAndFlush(l7206Emp);	
    else if (dbName.equals(ContentName.onMon))
      return l7206EmpReposMon.saveAndFlush(l7206Emp);
    else if (dbName.equals(ContentName.onHist))
      return l7206EmpReposHist.saveAndFlush(l7206Emp);
    else 
    return l7206EmpRepos.saveAndFlush(l7206Emp);
  }

  @Override
  public L7206Emp update(L7206Emp l7206Emp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + l7206Emp.getLogNo());
    if (!empNot.isEmpty())
      l7206Emp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return l7206EmpReposDay.saveAndFlush(l7206Emp);	
    else if (dbName.equals(ContentName.onMon))
      return l7206EmpReposMon.saveAndFlush(l7206Emp);
    else if (dbName.equals(ContentName.onHist))
      return l7206EmpReposHist.saveAndFlush(l7206Emp);
    else 
    return l7206EmpRepos.saveAndFlush(l7206Emp);
  }

  @Override
  public L7206Emp update2(L7206Emp l7206Emp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + l7206Emp.getLogNo());
    if (!empNot.isEmpty())
      l7206Emp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      l7206EmpReposDay.saveAndFlush(l7206Emp);	
    else if (dbName.equals(ContentName.onMon))
      l7206EmpReposMon.saveAndFlush(l7206Emp);
    else if (dbName.equals(ContentName.onHist))
        l7206EmpReposHist.saveAndFlush(l7206Emp);
    else 
      l7206EmpRepos.saveAndFlush(l7206Emp);	
    return this.findById(l7206Emp.getLogNo());
  }

  @Override
  public void delete(L7206Emp l7206Emp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + l7206Emp.getLogNo());
    if (dbName.equals(ContentName.onDay)) {
      l7206EmpReposDay.delete(l7206Emp);	
      l7206EmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206EmpReposMon.delete(l7206Emp);	
      l7206EmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206EmpReposHist.delete(l7206Emp);
      l7206EmpReposHist.flush();
    }
    else {
      l7206EmpRepos.delete(l7206Emp);
      l7206EmpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<L7206Emp> l7206Emp, TitaVo... titaVo) throws DBException {
    if (l7206Emp == null || l7206Emp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (L7206Emp t : l7206Emp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      l7206Emp = l7206EmpReposDay.saveAll(l7206Emp);	
      l7206EmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206Emp = l7206EmpReposMon.saveAll(l7206Emp);	
      l7206EmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206Emp = l7206EmpReposHist.saveAll(l7206Emp);
      l7206EmpReposHist.flush();
    }
    else {
      l7206Emp = l7206EmpRepos.saveAll(l7206Emp);
      l7206EmpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<L7206Emp> l7206Emp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (l7206Emp == null || l7206Emp.size() == 0)
      throw new DBException(6);

    for (L7206Emp t : l7206Emp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      l7206Emp = l7206EmpReposDay.saveAll(l7206Emp);	
      l7206EmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206Emp = l7206EmpReposMon.saveAll(l7206Emp);	
      l7206EmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206Emp = l7206EmpReposHist.saveAll(l7206Emp);
      l7206EmpReposHist.flush();
    }
    else {
      l7206Emp = l7206EmpRepos.saveAll(l7206Emp);
      l7206EmpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<L7206Emp> l7206Emp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (l7206Emp == null || l7206Emp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      l7206EmpReposDay.deleteAll(l7206Emp);	
      l7206EmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      l7206EmpReposMon.deleteAll(l7206Emp);	
      l7206EmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      l7206EmpReposHist.deleteAll(l7206Emp);
      l7206EmpReposHist.flush();
    }
    else {
      l7206EmpRepos.deleteAll(l7206Emp);
      l7206EmpRepos.flush();
    }
  }

  @Override
  public void Usp_L7_L7206Emp_Ins(String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      l7206EmpReposDay.uspL7L7206empIns(EmpNo);
    else if (dbName.equals(ContentName.onMon))
      l7206EmpReposMon.uspL7L7206empIns(EmpNo);
    else if (dbName.equals(ContentName.onHist))
      l7206EmpReposHist.uspL7L7206empIns(EmpNo);
   else
      l7206EmpRepos.uspL7L7206empIns(EmpNo);
  }

}
