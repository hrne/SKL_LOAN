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
import com.st1.itx.db.domain.JcicB094;
import com.st1.itx.db.domain.JcicB094Id;
import com.st1.itx.db.repository.online.JcicB094Repository;
import com.st1.itx.db.repository.day.JcicB094RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB094RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB094RepositoryHist;
import com.st1.itx.db.service.JcicB094Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB094Service")
@Repository
public class JcicB094ServiceImpl extends ASpringJpaParm implements JcicB094Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicB094Repository jcicB094Repos;

  @Autowired
  private JcicB094RepositoryDay jcicB094ReposDay;

  @Autowired
  private JcicB094RepositoryMon jcicB094ReposMon;

  @Autowired
  private JcicB094RepositoryHist jcicB094ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicB094Repos);
    org.junit.Assert.assertNotNull(jcicB094ReposDay);
    org.junit.Assert.assertNotNull(jcicB094ReposMon);
    org.junit.Assert.assertNotNull(jcicB094ReposHist);
  }

  @Override
  public JcicB094 findById(JcicB094Id jcicB094Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicB094Id);
    Optional<JcicB094> jcicB094 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB094 = jcicB094ReposDay.findById(jcicB094Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB094 = jcicB094ReposMon.findById(jcicB094Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB094 = jcicB094ReposHist.findById(jcicB094Id);
    else 
      jcicB094 = jcicB094Repos.findById(jcicB094Id);
    JcicB094 obj = jcicB094.isPresent() ? jcicB094.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicB094> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicB094> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo", "OwnerId", "CompanyId", "StockCode", "StockType"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo", "OwnerId", "CompanyId", "StockCode", "StockType"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicB094ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicB094ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicB094ReposHist.findAll(pageable);
    else 
      slice = jcicB094Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicB094 holdById(JcicB094Id jcicB094Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB094Id);
    Optional<JcicB094> jcicB094 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB094 = jcicB094ReposDay.findByJcicB094Id(jcicB094Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB094 = jcicB094ReposMon.findByJcicB094Id(jcicB094Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB094 = jcicB094ReposHist.findByJcicB094Id(jcicB094Id);
    else 
      jcicB094 = jcicB094Repos.findByJcicB094Id(jcicB094Id);
    return jcicB094.isPresent() ? jcicB094.get() : null;
  }

  @Override
  public JcicB094 holdById(JcicB094 jcicB094, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB094.getJcicB094Id());
    Optional<JcicB094> jcicB094T = null;
    if (dbName.equals(ContentName.onDay))
      jcicB094T = jcicB094ReposDay.findByJcicB094Id(jcicB094.getJcicB094Id());
    else if (dbName.equals(ContentName.onMon))
      jcicB094T = jcicB094ReposMon.findByJcicB094Id(jcicB094.getJcicB094Id());
    else if (dbName.equals(ContentName.onHist))
      jcicB094T = jcicB094ReposHist.findByJcicB094Id(jcicB094.getJcicB094Id());
    else 
      jcicB094T = jcicB094Repos.findByJcicB094Id(jcicB094.getJcicB094Id());
    return jcicB094T.isPresent() ? jcicB094T.get() : null;
  }

  @Override
  public JcicB094 insert(JcicB094 jcicB094, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicB094.getJcicB094Id());
    if (this.findById(jcicB094.getJcicB094Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicB094.setCreateEmpNo(empNot);

    if(jcicB094.getLastUpdateEmpNo() == null || jcicB094.getLastUpdateEmpNo().isEmpty())
      jcicB094.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB094ReposDay.saveAndFlush(jcicB094);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB094ReposMon.saveAndFlush(jcicB094);
    else if (dbName.equals(ContentName.onHist))
      return jcicB094ReposHist.saveAndFlush(jcicB094);
    else 
    return jcicB094Repos.saveAndFlush(jcicB094);
  }

  @Override
  public JcicB094 update(JcicB094 jcicB094, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB094.getJcicB094Id());
    if (!empNot.isEmpty())
      jcicB094.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB094ReposDay.saveAndFlush(jcicB094);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB094ReposMon.saveAndFlush(jcicB094);
    else if (dbName.equals(ContentName.onHist))
      return jcicB094ReposHist.saveAndFlush(jcicB094);
    else 
    return jcicB094Repos.saveAndFlush(jcicB094);
  }

  @Override
  public JcicB094 update2(JcicB094 jcicB094, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB094.getJcicB094Id());
    if (!empNot.isEmpty())
      jcicB094.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicB094ReposDay.saveAndFlush(jcicB094);	
    else if (dbName.equals(ContentName.onMon))
      jcicB094ReposMon.saveAndFlush(jcicB094);
    else if (dbName.equals(ContentName.onHist))
        jcicB094ReposHist.saveAndFlush(jcicB094);
    else 
      jcicB094Repos.saveAndFlush(jcicB094);	
    return this.findById(jcicB094.getJcicB094Id());
  }

  @Override
  public void delete(JcicB094 jcicB094, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicB094.getJcicB094Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicB094ReposDay.delete(jcicB094);	
      jcicB094ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB094ReposMon.delete(jcicB094);	
      jcicB094ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB094ReposHist.delete(jcicB094);
      jcicB094ReposHist.flush();
    }
    else {
      jcicB094Repos.delete(jcicB094);
      jcicB094Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicB094> jcicB094, TitaVo... titaVo) throws DBException {
    if (jcicB094 == null || jcicB094.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicB094 t : jcicB094){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicB094 = jcicB094ReposDay.saveAll(jcicB094);	
      jcicB094ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB094 = jcicB094ReposMon.saveAll(jcicB094);	
      jcicB094ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB094 = jcicB094ReposHist.saveAll(jcicB094);
      jcicB094ReposHist.flush();
    }
    else {
      jcicB094 = jcicB094Repos.saveAll(jcicB094);
      jcicB094Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicB094> jcicB094, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicB094 == null || jcicB094.size() == 0)
      throw new DBException(6);

    for (JcicB094 t : jcicB094) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicB094 = jcicB094ReposDay.saveAll(jcicB094);	
      jcicB094ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB094 = jcicB094ReposMon.saveAll(jcicB094);	
      jcicB094ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB094 = jcicB094ReposHist.saveAll(jcicB094);
      jcicB094ReposHist.flush();
    }
    else {
      jcicB094 = jcicB094Repos.saveAll(jcicB094);
      jcicB094Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicB094> jcicB094, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicB094 == null || jcicB094.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicB094ReposDay.deleteAll(jcicB094);	
      jcicB094ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB094ReposMon.deleteAll(jcicB094);	
      jcicB094ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB094ReposHist.deleteAll(jcicB094);
      jcicB094ReposHist.flush();
    }
    else {
      jcicB094Repos.deleteAll(jcicB094);
      jcicB094Repos.flush();
    }
  }

  @Override
  public void Usp_L8_JcicB094_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jcicB094ReposDay.uspL8Jcicb094Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jcicB094ReposMon.uspL8Jcicb094Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jcicB094ReposHist.uspL8Jcicb094Upd(TBSDYF, EmpNo);
   else
      jcicB094Repos.uspL8Jcicb094Upd(TBSDYF, EmpNo);
  }

}
