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
import com.st1.itx.db.domain.JcicB211;
import com.st1.itx.db.domain.JcicB211Id;
import com.st1.itx.db.repository.online.JcicB211Repository;
import com.st1.itx.db.repository.day.JcicB211RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB211RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB211RepositoryHist;
import com.st1.itx.db.service.JcicB211Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB211Service")
@Repository
public class JcicB211ServiceImpl extends ASpringJpaParm implements JcicB211Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicB211Repository jcicB211Repos;

  @Autowired
  private JcicB211RepositoryDay jcicB211ReposDay;

  @Autowired
  private JcicB211RepositoryMon jcicB211ReposMon;

  @Autowired
  private JcicB211RepositoryHist jcicB211ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicB211Repos);
    org.junit.Assert.assertNotNull(jcicB211ReposDay);
    org.junit.Assert.assertNotNull(jcicB211ReposMon);
    org.junit.Assert.assertNotNull(jcicB211ReposHist);
  }

  @Override
  public JcicB211 findById(JcicB211Id jcicB211Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicB211Id);
    Optional<JcicB211> jcicB211 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB211 = jcicB211ReposDay.findById(jcicB211Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB211 = jcicB211ReposMon.findById(jcicB211Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB211 = jcicB211ReposHist.findById(jcicB211Id);
    else 
      jcicB211 = jcicB211Repos.findById(jcicB211Id);
    JcicB211 obj = jcicB211.isPresent() ? jcicB211.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicB211> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicB211> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYMD", "BankItem", "BranchItem", "CustId", "AcDate", "AcctNo", "BorxNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYMD", "BankItem", "BranchItem", "CustId", "AcDate", "AcctNo", "BorxNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicB211ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicB211ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicB211ReposHist.findAll(pageable);
    else 
      slice = jcicB211Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicB211 holdById(JcicB211Id jcicB211Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB211Id);
    Optional<JcicB211> jcicB211 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB211 = jcicB211ReposDay.findByJcicB211Id(jcicB211Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB211 = jcicB211ReposMon.findByJcicB211Id(jcicB211Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB211 = jcicB211ReposHist.findByJcicB211Id(jcicB211Id);
    else 
      jcicB211 = jcicB211Repos.findByJcicB211Id(jcicB211Id);
    return jcicB211.isPresent() ? jcicB211.get() : null;
  }

  @Override
  public JcicB211 holdById(JcicB211 jcicB211, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB211.getJcicB211Id());
    Optional<JcicB211> jcicB211T = null;
    if (dbName.equals(ContentName.onDay))
      jcicB211T = jcicB211ReposDay.findByJcicB211Id(jcicB211.getJcicB211Id());
    else if (dbName.equals(ContentName.onMon))
      jcicB211T = jcicB211ReposMon.findByJcicB211Id(jcicB211.getJcicB211Id());
    else if (dbName.equals(ContentName.onHist))
      jcicB211T = jcicB211ReposHist.findByJcicB211Id(jcicB211.getJcicB211Id());
    else 
      jcicB211T = jcicB211Repos.findByJcicB211Id(jcicB211.getJcicB211Id());
    return jcicB211T.isPresent() ? jcicB211T.get() : null;
  }

  @Override
  public JcicB211 insert(JcicB211 jcicB211, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicB211.getJcicB211Id());
    if (this.findById(jcicB211.getJcicB211Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicB211.setCreateEmpNo(empNot);

    if(jcicB211.getLastUpdateEmpNo() == null || jcicB211.getLastUpdateEmpNo().isEmpty())
      jcicB211.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB211ReposDay.saveAndFlush(jcicB211);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB211ReposMon.saveAndFlush(jcicB211);
    else if (dbName.equals(ContentName.onHist))
      return jcicB211ReposHist.saveAndFlush(jcicB211);
    else 
    return jcicB211Repos.saveAndFlush(jcicB211);
  }

  @Override
  public JcicB211 update(JcicB211 jcicB211, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB211.getJcicB211Id());
    if (!empNot.isEmpty())
      jcicB211.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB211ReposDay.saveAndFlush(jcicB211);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB211ReposMon.saveAndFlush(jcicB211);
    else if (dbName.equals(ContentName.onHist))
      return jcicB211ReposHist.saveAndFlush(jcicB211);
    else 
    return jcicB211Repos.saveAndFlush(jcicB211);
  }

  @Override
  public JcicB211 update2(JcicB211 jcicB211, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB211.getJcicB211Id());
    if (!empNot.isEmpty())
      jcicB211.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicB211ReposDay.saveAndFlush(jcicB211);	
    else if (dbName.equals(ContentName.onMon))
      jcicB211ReposMon.saveAndFlush(jcicB211);
    else if (dbName.equals(ContentName.onHist))
        jcicB211ReposHist.saveAndFlush(jcicB211);
    else 
      jcicB211Repos.saveAndFlush(jcicB211);	
    return this.findById(jcicB211.getJcicB211Id());
  }

  @Override
  public void delete(JcicB211 jcicB211, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicB211.getJcicB211Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicB211ReposDay.delete(jcicB211);	
      jcicB211ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB211ReposMon.delete(jcicB211);	
      jcicB211ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB211ReposHist.delete(jcicB211);
      jcicB211ReposHist.flush();
    }
    else {
      jcicB211Repos.delete(jcicB211);
      jcicB211Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicB211> jcicB211, TitaVo... titaVo) throws DBException {
    if (jcicB211 == null || jcicB211.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicB211 t : jcicB211){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicB211 = jcicB211ReposDay.saveAll(jcicB211);	
      jcicB211ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB211 = jcicB211ReposMon.saveAll(jcicB211);	
      jcicB211ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB211 = jcicB211ReposHist.saveAll(jcicB211);
      jcicB211ReposHist.flush();
    }
    else {
      jcicB211 = jcicB211Repos.saveAll(jcicB211);
      jcicB211Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicB211> jcicB211, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicB211 == null || jcicB211.size() == 0)
      throw new DBException(6);

    for (JcicB211 t : jcicB211) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicB211 = jcicB211ReposDay.saveAll(jcicB211);	
      jcicB211ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB211 = jcicB211ReposMon.saveAll(jcicB211);	
      jcicB211ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB211 = jcicB211ReposHist.saveAll(jcicB211);
      jcicB211ReposHist.flush();
    }
    else {
      jcicB211 = jcicB211Repos.saveAll(jcicB211);
      jcicB211Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicB211> jcicB211, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicB211 == null || jcicB211.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicB211ReposDay.deleteAll(jcicB211);	
      jcicB211ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB211ReposMon.deleteAll(jcicB211);	
      jcicB211ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB211ReposHist.deleteAll(jcicB211);
      jcicB211ReposHist.flush();
    }
    else {
      jcicB211Repos.deleteAll(jcicB211);
      jcicB211Repos.flush();
    }
  }

  @Override
  public void Usp_L8_JcicB211_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jcicB211ReposDay.uspL8Jcicb211Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jcicB211ReposMon.uspL8Jcicb211Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jcicB211ReposHist.uspL8Jcicb211Upd(TBSDYF, EmpNo);
   else
      jcicB211Repos.uspL8Jcicb211Upd(TBSDYF, EmpNo);
  }

}
