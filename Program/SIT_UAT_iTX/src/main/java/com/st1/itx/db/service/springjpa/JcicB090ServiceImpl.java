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
import com.st1.itx.db.domain.JcicB090;
import com.st1.itx.db.domain.JcicB090Id;
import com.st1.itx.db.repository.online.JcicB090Repository;
import com.st1.itx.db.repository.day.JcicB090RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB090RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB090RepositoryHist;
import com.st1.itx.db.service.JcicB090Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB090Service")
@Repository
public class JcicB090ServiceImpl extends ASpringJpaParm implements JcicB090Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicB090Repository jcicB090Repos;

  @Autowired
  private JcicB090RepositoryDay jcicB090ReposDay;

  @Autowired
  private JcicB090RepositoryMon jcicB090ReposMon;

  @Autowired
  private JcicB090RepositoryHist jcicB090ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicB090Repos);
    org.junit.Assert.assertNotNull(jcicB090ReposDay);
    org.junit.Assert.assertNotNull(jcicB090ReposMon);
    org.junit.Assert.assertNotNull(jcicB090ReposHist);
  }

  @Override
  public JcicB090 findById(JcicB090Id jcicB090Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicB090Id);
    Optional<JcicB090> jcicB090 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB090 = jcicB090ReposDay.findById(jcicB090Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB090 = jcicB090ReposMon.findById(jcicB090Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB090 = jcicB090ReposHist.findById(jcicB090Id);
    else 
      jcicB090 = jcicB090Repos.findById(jcicB090Id);
    JcicB090 obj = jcicB090.isPresent() ? jcicB090.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicB090> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicB090> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "CustId", "ClActNo", "FacmNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "CustId", "ClActNo", "FacmNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicB090ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicB090ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicB090ReposHist.findAll(pageable);
    else 
      slice = jcicB090Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicB090 holdById(JcicB090Id jcicB090Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB090Id);
    Optional<JcicB090> jcicB090 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB090 = jcicB090ReposDay.findByJcicB090Id(jcicB090Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB090 = jcicB090ReposMon.findByJcicB090Id(jcicB090Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB090 = jcicB090ReposHist.findByJcicB090Id(jcicB090Id);
    else 
      jcicB090 = jcicB090Repos.findByJcicB090Id(jcicB090Id);
    return jcicB090.isPresent() ? jcicB090.get() : null;
  }

  @Override
  public JcicB090 holdById(JcicB090 jcicB090, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB090.getJcicB090Id());
    Optional<JcicB090> jcicB090T = null;
    if (dbName.equals(ContentName.onDay))
      jcicB090T = jcicB090ReposDay.findByJcicB090Id(jcicB090.getJcicB090Id());
    else if (dbName.equals(ContentName.onMon))
      jcicB090T = jcicB090ReposMon.findByJcicB090Id(jcicB090.getJcicB090Id());
    else if (dbName.equals(ContentName.onHist))
      jcicB090T = jcicB090ReposHist.findByJcicB090Id(jcicB090.getJcicB090Id());
    else 
      jcicB090T = jcicB090Repos.findByJcicB090Id(jcicB090.getJcicB090Id());
    return jcicB090T.isPresent() ? jcicB090T.get() : null;
  }

  @Override
  public JcicB090 insert(JcicB090 jcicB090, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicB090.getJcicB090Id());
    if (this.findById(jcicB090.getJcicB090Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicB090.setCreateEmpNo(empNot);

    if(jcicB090.getLastUpdateEmpNo() == null || jcicB090.getLastUpdateEmpNo().isEmpty())
      jcicB090.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB090ReposDay.saveAndFlush(jcicB090);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB090ReposMon.saveAndFlush(jcicB090);
    else if (dbName.equals(ContentName.onHist))
      return jcicB090ReposHist.saveAndFlush(jcicB090);
    else 
    return jcicB090Repos.saveAndFlush(jcicB090);
  }

  @Override
  public JcicB090 update(JcicB090 jcicB090, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB090.getJcicB090Id());
    if (!empNot.isEmpty())
      jcicB090.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB090ReposDay.saveAndFlush(jcicB090);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB090ReposMon.saveAndFlush(jcicB090);
    else if (dbName.equals(ContentName.onHist))
      return jcicB090ReposHist.saveAndFlush(jcicB090);
    else 
    return jcicB090Repos.saveAndFlush(jcicB090);
  }

  @Override
  public JcicB090 update2(JcicB090 jcicB090, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB090.getJcicB090Id());
    if (!empNot.isEmpty())
      jcicB090.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicB090ReposDay.saveAndFlush(jcicB090);	
    else if (dbName.equals(ContentName.onMon))
      jcicB090ReposMon.saveAndFlush(jcicB090);
    else if (dbName.equals(ContentName.onHist))
        jcicB090ReposHist.saveAndFlush(jcicB090);
    else 
      jcicB090Repos.saveAndFlush(jcicB090);	
    return this.findById(jcicB090.getJcicB090Id());
  }

  @Override
  public void delete(JcicB090 jcicB090, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicB090.getJcicB090Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicB090ReposDay.delete(jcicB090);	
      jcicB090ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB090ReposMon.delete(jcicB090);	
      jcicB090ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB090ReposHist.delete(jcicB090);
      jcicB090ReposHist.flush();
    }
    else {
      jcicB090Repos.delete(jcicB090);
      jcicB090Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicB090> jcicB090, TitaVo... titaVo) throws DBException {
    if (jcicB090 == null || jcicB090.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicB090 t : jcicB090){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicB090 = jcicB090ReposDay.saveAll(jcicB090);	
      jcicB090ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB090 = jcicB090ReposMon.saveAll(jcicB090);	
      jcicB090ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB090 = jcicB090ReposHist.saveAll(jcicB090);
      jcicB090ReposHist.flush();
    }
    else {
      jcicB090 = jcicB090Repos.saveAll(jcicB090);
      jcicB090Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicB090> jcicB090, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicB090 == null || jcicB090.size() == 0)
      throw new DBException(6);

    for (JcicB090 t : jcicB090) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicB090 = jcicB090ReposDay.saveAll(jcicB090);	
      jcicB090ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB090 = jcicB090ReposMon.saveAll(jcicB090);	
      jcicB090ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB090 = jcicB090ReposHist.saveAll(jcicB090);
      jcicB090ReposHist.flush();
    }
    else {
      jcicB090 = jcicB090Repos.saveAll(jcicB090);
      jcicB090Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicB090> jcicB090, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicB090 == null || jcicB090.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicB090ReposDay.deleteAll(jcicB090);	
      jcicB090ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB090ReposMon.deleteAll(jcicB090);	
      jcicB090ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB090ReposHist.deleteAll(jcicB090);
      jcicB090ReposHist.flush();
    }
    else {
      jcicB090Repos.deleteAll(jcicB090);
      jcicB090Repos.flush();
    }
  }

  @Override
  public void Usp_L8_JcicB090_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jcicB090ReposDay.uspL8Jcicb090Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jcicB090ReposMon.uspL8Jcicb090Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jcicB090ReposHist.uspL8Jcicb090Upd(TBSDYF, EmpNo);
   else
      jcicB090Repos.uspL8Jcicb090Upd(TBSDYF, EmpNo);
  }

}
