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
import com.st1.itx.db.domain.JcicB096;
import com.st1.itx.db.domain.JcicB096Id;
import com.st1.itx.db.repository.online.JcicB096Repository;
import com.st1.itx.db.repository.day.JcicB096RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB096RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB096RepositoryHist;
import com.st1.itx.db.service.JcicB096Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB096Service")
@Repository
public class JcicB096ServiceImpl extends ASpringJpaParm implements JcicB096Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicB096Repository jcicB096Repos;

  @Autowired
  private JcicB096RepositoryDay jcicB096ReposDay;

  @Autowired
  private JcicB096RepositoryMon jcicB096ReposMon;

  @Autowired
  private JcicB096RepositoryHist jcicB096ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicB096Repos);
    org.junit.Assert.assertNotNull(jcicB096ReposDay);
    org.junit.Assert.assertNotNull(jcicB096ReposMon);
    org.junit.Assert.assertNotNull(jcicB096ReposHist);
  }

  @Override
  public JcicB096 findById(JcicB096Id jcicB096Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicB096Id);
    Optional<JcicB096> jcicB096 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB096 = jcicB096ReposDay.findById(jcicB096Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB096 = jcicB096ReposMon.findById(jcicB096Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB096 = jcicB096ReposHist.findById(jcicB096Id);
    else 
      jcicB096 = jcicB096Repos.findById(jcicB096Id);
    JcicB096 obj = jcicB096.isPresent() ? jcicB096.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicB096> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicB096> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo", "LandSeq", "OwnerId", "CityJCICCode", "AreaJCICCode", "IrCode", "LandNo1", "LandNo2"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo", "LandSeq", "OwnerId", "CityJCICCode", "AreaJCICCode", "IrCode", "LandNo1", "LandNo2"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicB096ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicB096ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicB096ReposHist.findAll(pageable);
    else 
      slice = jcicB096Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicB096 holdById(JcicB096Id jcicB096Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB096Id);
    Optional<JcicB096> jcicB096 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB096 = jcicB096ReposDay.findByJcicB096Id(jcicB096Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB096 = jcicB096ReposMon.findByJcicB096Id(jcicB096Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB096 = jcicB096ReposHist.findByJcicB096Id(jcicB096Id);
    else 
      jcicB096 = jcicB096Repos.findByJcicB096Id(jcicB096Id);
    return jcicB096.isPresent() ? jcicB096.get() : null;
  }

  @Override
  public JcicB096 holdById(JcicB096 jcicB096, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB096.getJcicB096Id());
    Optional<JcicB096> jcicB096T = null;
    if (dbName.equals(ContentName.onDay))
      jcicB096T = jcicB096ReposDay.findByJcicB096Id(jcicB096.getJcicB096Id());
    else if (dbName.equals(ContentName.onMon))
      jcicB096T = jcicB096ReposMon.findByJcicB096Id(jcicB096.getJcicB096Id());
    else if (dbName.equals(ContentName.onHist))
      jcicB096T = jcicB096ReposHist.findByJcicB096Id(jcicB096.getJcicB096Id());
    else 
      jcicB096T = jcicB096Repos.findByJcicB096Id(jcicB096.getJcicB096Id());
    return jcicB096T.isPresent() ? jcicB096T.get() : null;
  }

  @Override
  public JcicB096 insert(JcicB096 jcicB096, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicB096.getJcicB096Id());
    if (this.findById(jcicB096.getJcicB096Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicB096.setCreateEmpNo(empNot);

    if(jcicB096.getLastUpdateEmpNo() == null || jcicB096.getLastUpdateEmpNo().isEmpty())
      jcicB096.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB096ReposDay.saveAndFlush(jcicB096);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB096ReposMon.saveAndFlush(jcicB096);
    else if (dbName.equals(ContentName.onHist))
      return jcicB096ReposHist.saveAndFlush(jcicB096);
    else 
    return jcicB096Repos.saveAndFlush(jcicB096);
  }

  @Override
  public JcicB096 update(JcicB096 jcicB096, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB096.getJcicB096Id());
    if (!empNot.isEmpty())
      jcicB096.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB096ReposDay.saveAndFlush(jcicB096);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB096ReposMon.saveAndFlush(jcicB096);
    else if (dbName.equals(ContentName.onHist))
      return jcicB096ReposHist.saveAndFlush(jcicB096);
    else 
    return jcicB096Repos.saveAndFlush(jcicB096);
  }

  @Override
  public JcicB096 update2(JcicB096 jcicB096, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB096.getJcicB096Id());
    if (!empNot.isEmpty())
      jcicB096.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicB096ReposDay.saveAndFlush(jcicB096);	
    else if (dbName.equals(ContentName.onMon))
      jcicB096ReposMon.saveAndFlush(jcicB096);
    else if (dbName.equals(ContentName.onHist))
        jcicB096ReposHist.saveAndFlush(jcicB096);
    else 
      jcicB096Repos.saveAndFlush(jcicB096);	
    return this.findById(jcicB096.getJcicB096Id());
  }

  @Override
  public void delete(JcicB096 jcicB096, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicB096.getJcicB096Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicB096ReposDay.delete(jcicB096);	
      jcicB096ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB096ReposMon.delete(jcicB096);	
      jcicB096ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB096ReposHist.delete(jcicB096);
      jcicB096ReposHist.flush();
    }
    else {
      jcicB096Repos.delete(jcicB096);
      jcicB096Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicB096> jcicB096, TitaVo... titaVo) throws DBException {
    if (jcicB096 == null || jcicB096.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicB096 t : jcicB096){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicB096 = jcicB096ReposDay.saveAll(jcicB096);	
      jcicB096ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB096 = jcicB096ReposMon.saveAll(jcicB096);	
      jcicB096ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB096 = jcicB096ReposHist.saveAll(jcicB096);
      jcicB096ReposHist.flush();
    }
    else {
      jcicB096 = jcicB096Repos.saveAll(jcicB096);
      jcicB096Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicB096> jcicB096, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicB096 == null || jcicB096.size() == 0)
      throw new DBException(6);

    for (JcicB096 t : jcicB096) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicB096 = jcicB096ReposDay.saveAll(jcicB096);	
      jcicB096ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB096 = jcicB096ReposMon.saveAll(jcicB096);	
      jcicB096ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB096 = jcicB096ReposHist.saveAll(jcicB096);
      jcicB096ReposHist.flush();
    }
    else {
      jcicB096 = jcicB096Repos.saveAll(jcicB096);
      jcicB096Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicB096> jcicB096, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicB096 == null || jcicB096.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicB096ReposDay.deleteAll(jcicB096);	
      jcicB096ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB096ReposMon.deleteAll(jcicB096);	
      jcicB096ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB096ReposHist.deleteAll(jcicB096);
      jcicB096ReposHist.flush();
    }
    else {
      jcicB096Repos.deleteAll(jcicB096);
      jcicB096Repos.flush();
    }
  }

  @Override
  public void Usp_L8_JcicB096_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jcicB096ReposDay.uspL8Jcicb096Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jcicB096ReposMon.uspL8Jcicb096Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jcicB096ReposHist.uspL8Jcicb096Upd(TBSDYF, EmpNo);
   else
      jcicB096Repos.uspL8Jcicb096Upd(TBSDYF, EmpNo);
  }

}
