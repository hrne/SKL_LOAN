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
import com.st1.itx.db.domain.JcicB680;
import com.st1.itx.db.domain.JcicB680Id;
import com.st1.itx.db.repository.online.JcicB680Repository;
import com.st1.itx.db.repository.day.JcicB680RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB680RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB680RepositoryHist;
import com.st1.itx.db.service.JcicB680Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB680Service")
@Repository
public class JcicB680ServiceImpl extends ASpringJpaParm implements JcicB680Service, InitializingBean {
  @Autowired
  private BaseEntityManager baseEntityManager;

  @Autowired
  private JcicB680Repository jcicB680Repos;

  @Autowired
  private JcicB680RepositoryDay jcicB680ReposDay;

  @Autowired
  private JcicB680RepositoryMon jcicB680ReposMon;

  @Autowired
  private JcicB680RepositoryHist jcicB680ReposHist;

  @Override
  public void afterPropertiesSet() throws Exception {
    org.junit.Assert.assertNotNull(jcicB680Repos);
    org.junit.Assert.assertNotNull(jcicB680ReposDay);
    org.junit.Assert.assertNotNull(jcicB680ReposMon);
    org.junit.Assert.assertNotNull(jcicB680ReposHist);
  }

  @Override
  public JcicB680 findById(JcicB680Id jcicB680Id, TitaVo... titaVo) {
    String dbName = "";

    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("findById " + dbName + " " + jcicB680Id);
    Optional<JcicB680> jcicB680 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB680 = jcicB680ReposDay.findById(jcicB680Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB680 = jcicB680ReposMon.findById(jcicB680Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB680 = jcicB680ReposHist.findById(jcicB680Id);
    else 
      jcicB680 = jcicB680Repos.findById(jcicB680Id);
    JcicB680 obj = jcicB680.isPresent() ? jcicB680.get() : null;
      if(obj != null) {
        EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
        em.detach(obj);
em = null;
}
    return obj;
  }

  @Override
  public Slice<JcicB680> findAll(int index, int limit, TitaVo... titaVo) {
    String dbName = "";
    Slice<JcicB680> slice = null;
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    Pageable pageable = null;
    if(limit == Integer.MAX_VALUE)
         pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "TranCode", "CustId", "CustNo", "FacmNo", "BormNo"));
    else
         pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "TranCode", "CustId", "CustNo", "FacmNo", "BormNo"));
    this.info("findAll " + dbName);
    if (dbName.equals(ContentName.onDay))
      slice = jcicB680ReposDay.findAll(pageable);
    else if (dbName.equals(ContentName.onMon))
      slice = jcicB680ReposMon.findAll(pageable);
    else if (dbName.equals(ContentName.onHist))
      slice = jcicB680ReposHist.findAll(pageable);
    else 
      slice = jcicB680Repos.findAll(pageable);

		if (slice != null) 
			this.baseEntityManager.clearEntityManager(dbName);

    return slice != null && !slice.isEmpty() ? slice : null;
  }

  @Override
  public JcicB680 holdById(JcicB680Id jcicB680Id, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB680Id);
    Optional<JcicB680> jcicB680 = null;
    if (dbName.equals(ContentName.onDay))
      jcicB680 = jcicB680ReposDay.findByJcicB680Id(jcicB680Id);
    else if (dbName.equals(ContentName.onMon))
      jcicB680 = jcicB680ReposMon.findByJcicB680Id(jcicB680Id);
    else if (dbName.equals(ContentName.onHist))
      jcicB680 = jcicB680ReposHist.findByJcicB680Id(jcicB680Id);
    else 
      jcicB680 = jcicB680Repos.findByJcicB680Id(jcicB680Id);
    return jcicB680.isPresent() ? jcicB680.get() : null;
  }

  @Override
  public JcicB680 holdById(JcicB680 jcicB680, TitaVo... titaVo) {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Hold " + dbName + " " + jcicB680.getJcicB680Id());
    Optional<JcicB680> jcicB680T = null;
    if (dbName.equals(ContentName.onDay))
      jcicB680T = jcicB680ReposDay.findByJcicB680Id(jcicB680.getJcicB680Id());
    else if (dbName.equals(ContentName.onMon))
      jcicB680T = jcicB680ReposMon.findByJcicB680Id(jcicB680.getJcicB680Id());
    else if (dbName.equals(ContentName.onHist))
      jcicB680T = jcicB680ReposHist.findByJcicB680Id(jcicB680.getJcicB680Id());
    else 
      jcicB680T = jcicB680Repos.findByJcicB680Id(jcicB680.getJcicB680Id());
    return jcicB680T.isPresent() ? jcicB680T.get() : null;
  }

  @Override
  public JcicB680 insert(JcicB680 jcicB680, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Insert..." + dbName + " " + jcicB680.getJcicB680Id());
    if (this.findById(jcicB680.getJcicB680Id()) != null)
      throw new DBException(2);

    if (!empNot.isEmpty())
      jcicB680.setCreateEmpNo(empNot);

    if(jcicB680.getLastUpdateEmpNo() == null || jcicB680.getLastUpdateEmpNo().isEmpty())
      jcicB680.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB680ReposDay.saveAndFlush(jcicB680);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB680ReposMon.saveAndFlush(jcicB680);
    else if (dbName.equals(ContentName.onHist))
      return jcicB680ReposHist.saveAndFlush(jcicB680);
    else 
    return jcicB680Repos.saveAndFlush(jcicB680);
  }

  @Override
  public JcicB680 update(JcicB680 jcicB680, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB680.getJcicB680Id());
    if (!empNot.isEmpty())
      jcicB680.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      return jcicB680ReposDay.saveAndFlush(jcicB680);	
    else if (dbName.equals(ContentName.onMon))
      return jcicB680ReposMon.saveAndFlush(jcicB680);
    else if (dbName.equals(ContentName.onHist))
      return jcicB680ReposHist.saveAndFlush(jcicB680);
    else 
    return jcicB680Repos.saveAndFlush(jcicB680);
  }

  @Override
  public JcicB680 update2(JcicB680 jcicB680, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("Update..." + dbName + " " + jcicB680.getJcicB680Id());
    if (!empNot.isEmpty())
      jcicB680.setLastUpdateEmpNo(empNot);

    if (dbName.equals(ContentName.onDay))
      jcicB680ReposDay.saveAndFlush(jcicB680);	
    else if (dbName.equals(ContentName.onMon))
      jcicB680ReposMon.saveAndFlush(jcicB680);
    else if (dbName.equals(ContentName.onHist))
        jcicB680ReposHist.saveAndFlush(jcicB680);
    else 
      jcicB680Repos.saveAndFlush(jcicB680);	
    return this.findById(jcicB680.getJcicB680Id());
  }

  @Override
  public void delete(JcicB680 jcicB680, TitaVo... titaVo) throws DBException {
    String dbName = "";
    if (titaVo.length != 0)
      dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    this.info("Delete..." + dbName + " " + jcicB680.getJcicB680Id());
    if (dbName.equals(ContentName.onDay)) {
      jcicB680ReposDay.delete(jcicB680);	
      jcicB680ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB680ReposMon.delete(jcicB680);	
      jcicB680ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB680ReposHist.delete(jcicB680);
      jcicB680ReposHist.flush();
    }
    else {
      jcicB680Repos.delete(jcicB680);
      jcicB680Repos.flush();
    }
   }

  @Override
  public void insertAll(List<JcicB680> jcicB680, TitaVo... titaVo) throws DBException {
    if (jcicB680 == null || jcicB680.size() == 0)
      throw new DBException(6);
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
         empNot = empNot.isEmpty() ? "System" : empNot;		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("InsertAll...");
    for (JcicB680 t : jcicB680){ 
      if (!empNot.isEmpty())
        t.setCreateEmpNo(empNot);
      if(t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
        t.setLastUpdateEmpNo(empNot);
}		

    if (dbName.equals(ContentName.onDay)) {
      jcicB680 = jcicB680ReposDay.saveAll(jcicB680);	
      jcicB680ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB680 = jcicB680ReposMon.saveAll(jcicB680);	
      jcicB680ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB680 = jcicB680ReposHist.saveAll(jcicB680);
      jcicB680ReposHist.flush();
    }
    else {
      jcicB680 = jcicB680Repos.saveAll(jcicB680);
      jcicB680Repos.flush();
    }
    }

  @Override
  public void updateAll(List<JcicB680> jcicB680, TitaVo... titaVo) throws DBException {
     String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
       empNot = ThreadVariable.getEmpNot();

    this.info("UpdateAll...");
    if (jcicB680 == null || jcicB680.size() == 0)
      throw new DBException(6);

    for (JcicB680 t : jcicB680) 
    if (!empNot.isEmpty())
        t.setLastUpdateEmpNo(empNot);
		

    if (dbName.equals(ContentName.onDay)) {
      jcicB680 = jcicB680ReposDay.saveAll(jcicB680);	
      jcicB680ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB680 = jcicB680ReposMon.saveAll(jcicB680);	
      jcicB680ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB680 = jcicB680ReposHist.saveAll(jcicB680);
      jcicB680ReposHist.flush();
    }
    else {
      jcicB680 = jcicB680Repos.saveAll(jcicB680);
      jcicB680Repos.flush();
    }
    }

  @Override
  public void deleteAll(List<JcicB680> jcicB680, TitaVo... titaVo) throws DBException {
    this.info("DeleteAll...");
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (jcicB680 == null || jcicB680.size() == 0)
      throw new DBException(6);
    if (dbName.equals(ContentName.onDay)) {
      jcicB680ReposDay.deleteAll(jcicB680);	
      jcicB680ReposDay.flush();
    }
    else if (dbName.equals(ContentName.onMon)) {
      jcicB680ReposMon.deleteAll(jcicB680);	
      jcicB680ReposMon.flush();
    }
    else if (dbName.equals(ContentName.onHist)) {
      jcicB680ReposHist.deleteAll(jcicB680);
      jcicB680ReposHist.flush();
    }
    else {
      jcicB680Repos.deleteAll(jcicB680);
      jcicB680Repos.flush();
    }
  }

  @Override
  public void Usp_L8_JcicB680_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
    String dbName = "";
    
    if (titaVo.length != 0)
    dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
    if (dbName.equals(ContentName.onDay))
      jcicB680ReposDay.uspL8Jcicb680Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onMon))
      jcicB680ReposMon.uspL8Jcicb680Upd(TBSDYF, EmpNo);
    else if (dbName.equals(ContentName.onHist))
      jcicB680ReposHist.uspL8Jcicb680Upd(TBSDYF, EmpNo);
   else
      jcicB680Repos.uspL8Jcicb680Upd(TBSDYF, EmpNo);
  }

}
