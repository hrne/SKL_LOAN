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
import com.st1.itx.db.domain.QuitEmp;
import com.st1.itx.db.repository.online.QuitEmpRepository;
import com.st1.itx.db.repository.day.QuitEmpRepositoryDay;
import com.st1.itx.db.repository.mon.QuitEmpRepositoryMon;
import com.st1.itx.db.repository.hist.QuitEmpRepositoryHist;
import com.st1.itx.db.service.QuitEmpService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("quitEmpService")
@Repository
public class QuitEmpServiceImpl extends ASpringJpaParm implements QuitEmpService, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private QuitEmpRepository quitEmpRepos;

  @Autowired
  private QuitEmpRepositoryDay quitEmpReposDay;

  @Autowired
  private QuitEmpRepositoryMon quitEmpReposMon;

  @Autowired
  private QuitEmpRepositoryHist quitEmpReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(quitEmpRepos);
    org.junit.Assert.assertNotNull(quitEmpReposDay);
    org.junit.Assert.assertNotNull(quitEmpReposMon);
    org.junit.Assert.assertNotNull(quitEmpReposHist);
  }

  @Override
  public QuitEmp findById(String empNo, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + empNo);
    Optional<QuitEmp> quitEmp = null;
    if (dbName.equals(ContentName.onDay))
      quitEmp = quitEmpReposDay.findById(empNo);
    else if (dbName.equals(ContentName.onMon))
      quitEmp = quitEmpReposMon.findById(empNo);
    else if (dbName.equals(ContentName.onHist))
      quitEmp = quitEmpReposHist.findById(empNo);
    else 
      quitEmp = quitEmpRepos.findById(empNo);
    QuitEmp obj = quitEmp.isPresent() ? quitEmp.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<QuitEmp> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<QuitEmp> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "EmpNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "EmpNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = quitEmpReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = quitEmpReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = quitEmpReposHist.findAll(pageable);
    else 
      slice = quitEmpRepos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public QuitEmp holdById(String empNo, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + empNo);
    Optional<QuitEmp> quitEmp = null;
    if (dbName.equals(ContentName.onDay))
      quitEmp = quitEmpReposDay.findByEmpNo(empNo);
    else if (dbName.equals(ContentName.onMon))
      quitEmp = quitEmpReposMon.findByEmpNo(empNo);
    else if (dbName.equals(ContentName.onHist))
      quitEmp = quitEmpReposHist.findByEmpNo(empNo);
    else 
      quitEmp = quitEmpRepos.findByEmpNo(empNo);
    return quitEmp.isPresent() ? quitEmp.get() : null;
  }

  @Override
  public QuitEmp holdById(QuitEmp quitEmp, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + quitEmp.getEmpNo());
    Optional<QuitEmp> quitEmpT = null;
    if (dbName.equals(ContentName.onDay))
      quitEmpT = quitEmpReposDay.findByEmpNo(quitEmp.getEmpNo());
    else if (dbName.equals(ContentName.onMon))
      quitEmpT = quitEmpReposMon.findByEmpNo(quitEmp.getEmpNo());
    else if (dbName.equals(ContentName.onHist))
      quitEmpT = quitEmpReposHist.findByEmpNo(quitEmp.getEmpNo());
    else 
      quitEmpT = quitEmpRepos.findByEmpNo(quitEmp.getEmpNo());
    return quitEmpT.isPresent() ? quitEmpT.get() : null;
  }

  @Override
  public QuitEmp insert(QuitEmp quitEmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + quitEmp.getEmpNo());
    if (this.findById(quitEmp.getEmpNo(), titaVo) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      quitEmp.setCreateEmpNo(empNot);

    if(quitEmp.getLastUpdateEmpNo() == null || quitEmp.getLastUpdateEmpNo().isEmpty())
      quitEmp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return quitEmpReposDay.saveAndFlush(quitEmp);	
    else if (dbName.equals(ContentName.onMon))
      return quitEmpReposMon.saveAndFlush(quitEmp);
    else if (dbName.equals(ContentName.onHist))
      return quitEmpReposHist.saveAndFlush(quitEmp);
    else 
    return quitEmpRepos.saveAndFlush(quitEmp);
  }

  @Override
  public QuitEmp update(QuitEmp quitEmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + quitEmp.getEmpNo());
    if (!empNot.isEmpty())
      quitEmp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return quitEmpReposDay.saveAndFlush(quitEmp);	
    else if (dbName.equals(ContentName.onMon))
      return quitEmpReposMon.saveAndFlush(quitEmp);
    else if (dbName.equals(ContentName.onHist))
      return quitEmpReposHist.saveAndFlush(quitEmp);
    else 
    return quitEmpRepos.saveAndFlush(quitEmp);
  }

  @Override
  public QuitEmp update2(QuitEmp quitEmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + quitEmp.getEmpNo());
    if (!empNot.isEmpty())
      quitEmp.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      quitEmpReposDay.saveAndFlush(quitEmp);	
    else if (dbName.equals(ContentName.onMon))
      quitEmpReposMon.saveAndFlush(quitEmp);
    else if (dbName.equals(ContentName.onHist))
        quitEmpReposHist.saveAndFlush(quitEmp);
    else 
      quitEmpRepos.saveAndFlush(quitEmp);	
    return this.findById(quitEmp.getEmpNo());
  }

  @Override
  public void delete(QuitEmp quitEmp, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + quitEmp.getEmpNo());
    if (dbName.equals(ContentName.onDay)) {
      quitEmpReposDay.delete(quitEmp);	
      quitEmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      quitEmpReposMon.delete(quitEmp);	
      quitEmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      quitEmpReposHist.delete(quitEmp);
      quitEmpReposHist.flush();
    }
    else {
      quitEmpRepos.delete(quitEmp);
      quitEmpRepos.flush();
    }
   }

  @Override
  public void insertAll(List<QuitEmp> quitEmp, TitaVo... titaVo) throws DBException {
    if (quitEmp == null || quitEmp.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (QuitEmp t : quitEmp){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      quitEmp = quitEmpReposDay.saveAll(quitEmp);	
      quitEmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      quitEmp = quitEmpReposMon.saveAll(quitEmp);	
      quitEmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      quitEmp = quitEmpReposHist.saveAll(quitEmp);
      quitEmpReposHist.flush();
    }
    else {
      quitEmp = quitEmpRepos.saveAll(quitEmp);
      quitEmpRepos.flush();
    }
    }

  @Override
  public void updateAll(List<QuitEmp> quitEmp, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (quitEmp == null || quitEmp.size() == 0)
      throw new DBException(6);

    for (QuitEmp t : quitEmp) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      quitEmp = quitEmpReposDay.saveAll(quitEmp);	
      quitEmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      quitEmp = quitEmpReposMon.saveAll(quitEmp);	
      quitEmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      quitEmp = quitEmpReposHist.saveAll(quitEmp);
      quitEmpReposHist.flush();
    }
    else {
      quitEmp = quitEmpRepos.saveAll(quitEmp);
      quitEmpRepos.flush();
    }
    }

  @Override
  public void deleteAll(List<QuitEmp> quitEmp, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (quitEmp == null || quitEmp.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      quitEmpReposDay.deleteAll(quitEmp);	
      quitEmpReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      quitEmpReposMon.deleteAll(quitEmp);	
      quitEmpReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      quitEmpReposHist.deleteAll(quitEmp);
      quitEmpReposHist.flush();
    }
    else {
      quitEmpRepos.deleteAll(quitEmp);
      quitEmpRepos.flush();
    }
  }

}
