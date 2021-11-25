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
import com.st1.itx.db.domain.JcicB091;
import com.st1.itx.db.domain.JcicB091Id;
import com.st1.itx.db.repository.online.JcicB091Repository;
import com.st1.itx.db.repository.day.JcicB091RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB091RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB091RepositoryHist;
import com.st1.itx.db.service.JcicB091Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB091Service")
@Repository
public class JcicB091ServiceImpl extends ASpringJpaParm implements JcicB091Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicB091Repository jcicB091Repos;

  @Autowired
  private JcicB091RepositoryDay jcicB091ReposDay;

  @Autowired
  private JcicB091RepositoryMon jcicB091ReposMon;

  @Autowired
  private JcicB091RepositoryHist jcicB091ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicB091Repos);
    org.junit.Assert.assertNotNull(jcicB091ReposDay);
    org.junit.Assert.assertNotNull(jcicB091ReposMon);
    org.junit.Assert.assertNotNull(jcicB091ReposHist);
  }

  @Override
  public JcicB091 findById(JcicB091Id jcicB091Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicB091Id);
    Optional<JcicB091> jcicB091 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB091 = jcicB091ReposDay.findById(jcicB091Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB091 = jcicB091ReposMon.findById(jcicB091Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB091 = jcicB091ReposHist.findById(jcicB091Id);
    else 
      jcicB091 = jcicB091Repos.findById(jcicB091Id);
    JcicB091 obj = jcicB091.isPresent() ? jcicB091.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicB091> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicB091> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo", "OwnerId", "CompanyId"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo", "OwnerId", "CompanyId"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicB091ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicB091ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicB091ReposHist.findAll(pageable);
    else 
      slice = jcicB091Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicB091 holdById(JcicB091Id jcicB091Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB091Id);
    Optional<JcicB091> jcicB091 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB091 = jcicB091ReposDay.findByJcicB091Id(jcicB091Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB091 = jcicB091ReposMon.findByJcicB091Id(jcicB091Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB091 = jcicB091ReposHist.findByJcicB091Id(jcicB091Id);
    else 
      jcicB091 = jcicB091Repos.findByJcicB091Id(jcicB091Id);
    return jcicB091.isPresent() ? jcicB091.get() : null;
  }

  @Override
  public JcicB091 holdById(JcicB091 jcicB091, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB091.getJcicB091Id());
    Optional<JcicB091> jcicB091T = null;
    if (dbName.equals(ContentName.onDay))
      jcicB091T = jcicB091ReposDay.findByJcicB091Id(jcicB091.getJcicB091Id());
    else if (dbName.equals(ContentName.onMon))
      jcicB091T = jcicB091ReposMon.findByJcicB091Id(jcicB091.getJcicB091Id());
    else if (dbName.equals(ContentName.onHist))
      jcicB091T = jcicB091ReposHist.findByJcicB091Id(jcicB091.getJcicB091Id());
    else 
      jcicB091T = jcicB091Repos.findByJcicB091Id(jcicB091.getJcicB091Id());
    return jcicB091T.isPresent() ? jcicB091T.get() : null;
  }

  @Override
  public JcicB091 insert(JcicB091 jcicB091, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicB091.getJcicB091Id());
    if (this.findById(jcicB091.getJcicB091Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicB091.setCreateEmpNo(empNot);

    if(jcicB091.getLastUpdateEmpNo() == null || jcicB091.getLastUpdateEmpNo().isEmpty())
      jcicB091.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB091ReposDay.saveAndFlush(jcicB091);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB091ReposMon.saveAndFlush(jcicB091);
    else if (dbName.equals(ContentName.onHist))
      return jcicB091ReposHist.saveAndFlush(jcicB091);
    else 
    return jcicB091Repos.saveAndFlush(jcicB091);
  }

  @Override
  public JcicB091 update(JcicB091 jcicB091, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB091.getJcicB091Id());
    if (!empNot.isEmpty())
      jcicB091.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB091ReposDay.saveAndFlush(jcicB091);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB091ReposMon.saveAndFlush(jcicB091);
    else if (dbName.equals(ContentName.onHist))
      return jcicB091ReposHist.saveAndFlush(jcicB091);
    else 
    return jcicB091Repos.saveAndFlush(jcicB091);
  }

  @Override
  public JcicB091 update2(JcicB091 jcicB091, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB091.getJcicB091Id());
    if (!empNot.isEmpty())
      jcicB091.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicB091ReposDay.saveAndFlush(jcicB091);	
    else if (dbName.equals(ContentName.onMon))
      jcicB091ReposMon.saveAndFlush(jcicB091);
    else if (dbName.equals(ContentName.onHist))
        jcicB091ReposHist.saveAndFlush(jcicB091);
    else 
      jcicB091Repos.saveAndFlush(jcicB091);	
    return this.findById(jcicB091.getJcicB091Id());
  }

  @Override
  public void delete(JcicB091 jcicB091, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicB091.getJcicB091Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicB091ReposDay.delete(jcicB091);	
      jcicB091ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB091ReposMon.delete(jcicB091);	
      jcicB091ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB091ReposHist.delete(jcicB091);
      jcicB091ReposHist.flush();
    }
    else {
      jcicB091Repos.delete(jcicB091);
      jcicB091Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicB091> jcicB091, TitaVo... titaVo) throws DBException {
    if (jcicB091 == null || jcicB091.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicB091 t : jcicB091){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicB091 = jcicB091ReposDay.saveAll(jcicB091);	
      jcicB091ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB091 = jcicB091ReposMon.saveAll(jcicB091);	
      jcicB091ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB091 = jcicB091ReposHist.saveAll(jcicB091);
      jcicB091ReposHist.flush();
    }
    else {
      jcicB091 = jcicB091Repos.saveAll(jcicB091);
      jcicB091Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicB091> jcicB091, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicB091 == null || jcicB091.size() == 0)
      throw new DBException(6);

    for (JcicB091 t : jcicB091) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicB091 = jcicB091ReposDay.saveAll(jcicB091);	
      jcicB091ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB091 = jcicB091ReposMon.saveAll(jcicB091);	
      jcicB091ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB091 = jcicB091ReposHist.saveAll(jcicB091);
      jcicB091ReposHist.flush();
    }
    else {
      jcicB091 = jcicB091Repos.saveAll(jcicB091);
      jcicB091Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicB091> jcicB091, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicB091 == null || jcicB091.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicB091ReposDay.deleteAll(jcicB091);	
      jcicB091ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB091ReposMon.deleteAll(jcicB091);	
      jcicB091ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB091ReposHist.deleteAll(jcicB091);
      jcicB091ReposHist.flush();
    }
    else {
      jcicB091Repos.deleteAll(jcicB091);
      jcicB091Repos.flush();
    }
  }

  @Override
  public void Usp_L8_JcicB091_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jcicB091ReposDay.uspL8Jcicb091Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jcicB091ReposMon.uspL8Jcicb091Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jcicB091ReposHist.uspL8Jcicb091Upd(TBSDYF, EmpNo);
   else
      jcicB091Repos.uspL8Jcicb091Upd(TBSDYF, EmpNo);
  }

}
